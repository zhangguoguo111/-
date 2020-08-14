package com.jx.taos.taosconsumer;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lixingao
 * @date 2020/7/14 17:39
 */
public abstract class MessageConsumer<T> implements Consumer<T>{

    private BlockingQueue<T> queue;

    private final AtomicInteger counter = new AtomicInteger(0);

    private int maxOnceConsumeCount;

    private String queueName;

    private Integer queueSize;

    private Integer totalMsgNum;

    private Integer discardedMsgNum;

    private Logger log = LoggerFactory.getLogger(getClass());

    protected MessageConsumer(){
        this(10000, "DefaultMessageConsumerName", 1000, 1);
    }
    /**
     *
     * @param queueCapacity  队列的大小
     * @param name  消费者名称
     * @param maxOnceConsumeCount 每次消费最大数量
     * @param workerCount  消费者数量
     */
    protected MessageConsumer(int queueCapacity, String name, int maxOnceConsumeCount, int workerCount ){

        queue=new LinkedBlockingDeque<>(queueCapacity);
        queueName = name;
        queueSize = queueCapacity;
        this.maxOnceConsumeCount=maxOnceConsumeCount;
        for(int i=0;i<workerCount;i++){
            new Thread(new Worker(), name+i).start();
        }
    }
    /**
     * 获取队列名称
     * @return
     */
    @Override
    public String getConsumerName(){
        return queueName;
    };
    /**
     * 队列长度
     * @return
     */
    @Override
    public int getQueueSize(){
        return queueSize;
    };

    /**
     * 消息总数
     * @return
     */
    @Override
    public int getTotalMsgNum(){
        return totalMsgNum;
    };

    /**
     * 丢弃消息
     * @return
     */
    @Override
    public int getDiscardedMsgNum(){
        return discardedMsgNum;
    };




    /**
     * 如果队列满了，此方法是会阻塞的，要慎用
     * @param t
     * @throws InterruptedException
     */


    @Override
    public void put(T t) throws InterruptedException {
        queue.put(t);
        counter.incrementAndGet();
    }

    public int getOfferedCount() {
        return counter.get();
    }

    /**
     * 如果放不进就返回false，放成功就true
     */


    @Override
    public boolean offer(T t){
        boolean re= queue.offer(t);
        counter.incrementAndGet();
        if(!re) {
            log.error("the messageQueue is full and the message will be discarded:"+t.toString());
        }
        return re;
    }

    public boolean offer(T t, long timeout, TimeUnit unit){
        try {
            counter.incrementAndGet();
            return queue.offer(t, timeout, unit);
        } catch (InterruptedException e) {
            log.error("an error has accured when offering data into messageQueue", e);
        }
        return false;
    }

    public boolean offer(List<T> list){
        boolean re = true;
        if(CollectionUtils.isNotEmpty(list)){
            for (T t:list) {
                re = this.offer(t);
            }
        }

        return re;
    }

    class Worker extends Thread {
        @Override
        public void run(){
            while(true){
                try{
                    T t=queue.take();
                    List<T> list=new ArrayList<>(maxOnceConsumeCount);
                    list.add(t);
                    int count=1;
                    if(maxOnceConsumeCount>1){
                        while((t=queue.poll())!=null){
                            list.add(t);
                            count++;
                            if(count>=maxOnceConsumeCount) {
                                break;
                            }
                        }
                    }
                    totalMsgNum = count + queue.size();
                    discardedMsgNum = count;
                    log.error("get message "+count+" over,begin working, left "+queue.size()+" messages");
                    work(list);
                    sleep(1000);
                }catch(Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    protected abstract void work(List<T> list) throws IOException, MqttException, SQLException;

}
