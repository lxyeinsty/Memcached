package cn.edu.hust.memcached.server.thread;

import cn.edu.hust.memcached.server.message.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by lxy on 2017/10/20.
 * 监听线程
 */
public class ListeningThread extends Thread {
    private ServerSocket serverSocket;
    private MessageHandler messageHandler;
    private Vector<HandlerThread> handlerThreads = new Vector<HandlerThread>();
    private boolean isRun;

    public ListeningThread(ServerSocket serverSocket, MessageHandler messageHandler) {
        this.serverSocket = serverSocket;
        this.messageHandler = messageHandler;
        this.isRun = true;
    }

    @Override
    public void run() {
        while (isRun) {
            //检查socket是否关闭
            if (serverSocket.isClosed()) {
                this.isRun = false;
                break;
            }
            try {
                Socket socket = serverSocket.accept();
                //每接收到一个Socket就建立一个新的线程来处理
                HandlerThread handlerThread = new HandlerThread(socket, messageHandler);
                handlerThreads.addElement(handlerThread);
                handlerThread.start();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void stopRun() {
        for (HandlerThread handlerThread : handlerThreads) {
            handlerThread.stopRun();
        }
        this.isRun = false;
    }
}
