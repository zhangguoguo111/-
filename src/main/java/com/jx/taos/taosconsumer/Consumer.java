package com.jx.taos.taosconsumer;

/**
 * @author lixingao
 */
public interface Consumer<T> {
	/**
	 * 如果队列满了，此方法是会阻塞的，要慎用
	 * @param t
	 * @throws InterruptedException
	 */
	public void put(T t) throws InterruptedException;
	
	/**
	 * 如果放不进就返回false，放成功就true
	 * @param t
	 * @return
	 */
	public boolean offer(T t);

	/**
	 * 获取队列名称
	 * @return
	 */
	public String getConsumerName();

	/**
	 * 消息总数
	 * @return
	 */
	public int getTotalMsgNum();

	/**
	 * 队列长度
	 * @return
	 */
	public int getQueueSize();

	/**
	 * 处理消息
	 * @return
	 */
	public int getDiscardedMsgNum();

}
