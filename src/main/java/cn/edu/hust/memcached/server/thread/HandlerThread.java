package cn.edu.hust.memcached.server.thread;

import cn.edu.hust.memcached.server.message.MessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by lxy on 2017/10/20.
 * 消息处理线程
 */
public class HandlerThread extends Thread {
    private Socket socket;
    private MessageHandler messageHandler;
    private boolean isRun;

    public HandlerThread(Socket socket, MessageHandler messageHandler) {
        this.socket = socket;
        this.messageHandler = messageHandler;
        this.isRun = true;
    }

    @Override
    public void run() {
        while (isRun) {
            //检查socket是否关闭
            if (socket.isClosed()) {
                isRun = false;
                break;
            }

            try {
                //接受消息并处理消息
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = reader.readLine();
                if (message != null) {
                    messageHandler.onReceive(socket, message);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void stopRun() {
        this.isRun = false;
        try {
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
