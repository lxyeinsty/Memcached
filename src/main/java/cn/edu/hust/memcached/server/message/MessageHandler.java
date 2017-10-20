package cn.edu.hust.memcached.server.message;

import java.net.Socket;

/**
 * Created by lxy on 2017/10/20.
 * 消息处理
 */
public interface MessageHandler {
    void onReceive(Socket socket, String message);
}
