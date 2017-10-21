package cn.edu.hust.memcached.server.thread;

import cn.edu.hust.memcached.server.message.MessageHandler;
import cn.edu.hust.memcached.server.message.MessageInBound;
import cn.edu.hust.memcached.server.message.exeception.UnSupportedCommandException;
import cn.edu.hust.memcached.server.message.Decoder;

import java.io.*;
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
                MessageInBound messageInBound = Decoder.decodeMessage(socket.getInputStream());
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                messageHandler.onReceive(writer, messageInBound);
            } catch (Exception exception) {
                if (exception instanceof UnSupportedCommandException) {
                    try {
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                        writer.println(exception.getMessage());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    exception.printStackTrace();
                }
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
