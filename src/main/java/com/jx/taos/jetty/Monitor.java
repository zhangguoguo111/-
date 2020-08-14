package com.jx.taos.jetty;


import com.alibaba.druid.util.StringUtils;
import com.jx.mns.utils.SpringContextHolder;
import com.jx.taos.consts.SystemConsts;
import com.jx.taos.taosconsumer.Consumer;
import com.jx.taos.taosconsumer.TaosGpsMessageConsumer;
import com.jx.taos.taosconsumer.TaosTankMessageConsumer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.BindException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * 关键数据监控
 */

public class Monitor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void start() throws Exception {
        int port = 8484;
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/monitor");
        server.setHandler(context);
        context.addFilter(new FilterHolder(new LoginFilter()), "*.do", null);
        context.addServlet(new ServletHolder(new Queue()), "/queue.do");
        context.addServlet(new ServletHolder(new Login()), "/login");

        try {
            server.start();
            server.join();
            if (logger.isInfoEnabled()) {
                logger.info("start monitor success.port:{}", port);
            }
        } catch (BindException be) {
            logger.error("{} Address already in use: bind ", port);
            System.exit(0);
        }
    }
}

class Queue extends HttpServlet {

    private TaosGpsMessageConsumer taosGpsMessageConsumer = SpringContextHolder.getBean("taosGpsMessageConsumer");
    private TaosTankMessageConsumer taosTankMessageConsumer = SpringContextHolder.getBean("taosTankMessageConsumer");

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private List<Consumer> consumers;

    public Queue() {
        consumers = new ArrayList<>();
        consumers.add(taosGpsMessageConsumer);
        consumers.add(taosTankMessageConsumer);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> </head>");

        sb.append("<table>");
        sb.append("<tr><td>序号 |</td><td>队列 |</td><td>消息总数 |</td><td>队列长度 |</td><td>处理消息 </tr>");

        for (int i = 0; i < consumers.size(); i++) {
            sb.append("<tr>");
            sb.append("<td>").append(i + 1).append("</td>");
            sb.append("<td>").append(consumers.get(i).getConsumerName()).append("</td>");
            sb.append("<td>").append(consumers.get(i).getTotalMsgNum()).append("</td>");
            sb.append("<td>").append(consumers.get(i).getQueueSize()).append("</td>");
            sb.append("<td>").append(consumers.get(i).getDiscardedMsgNum()).append("</td>");
            sb.append("</tr>");
        }

        sb.append("</table>");
        resp.getOutputStream().write(sb.toString().getBytes(Charset.forName("UTF-8")));
    }
}

class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> </head>");
        sb.append("<form action='login' method='post'>");
        sb.append("<p>用户名: <input type='text' name='username' /></p>");
        sb.append("<p>密码: <input type='password' name='password' /></p>");
        sb.append("<input type='submit' value='Submit' />");
        resp.getOutputStream().write(sb.toString().getBytes(Charset.forName("UTF-8")));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (SystemConsts.getSystemName().equals(username)){
            if (SystemConsts.getALIAS().equals(password)){
                Cookie cookie = new Cookie(DsgConstans.SESSION_USER, username);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60);
                resp.addCookie(cookie);
                resp.sendRedirect("queue.do");
            }

        } else {
            resp.sendRedirect("login");
        }
    }
}

class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String userName = "";
        Cookie[] cookies = req.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (DsgConstans.SESSION_USER.equals(cookie.getName())) {
                    userName = cookie.getValue();
                }
            }
        }
        if (StringUtils.isEmpty(userName) ) {
            resp.sendRedirect("login");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
