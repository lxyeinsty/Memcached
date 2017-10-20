package cn.edu.hust.memcached.server;

import cn.edu.hust.memcached.server.message.MessageHandler;
import cn.edu.hust.memcached.server.message.MessageHandlerImpl;

/**
 * Created by lxy on 2017/10/20.
 * 服务启动类
 */
public class Application {
    public static void main(String[] args)  throws Exception {
        MessageHandler messageHandler = new MessageHandlerImpl();
        //配置启动server
        ServerBootstrap serverBootstrap = new ServerBootstrap(8888, messageHandler);
    }
}
