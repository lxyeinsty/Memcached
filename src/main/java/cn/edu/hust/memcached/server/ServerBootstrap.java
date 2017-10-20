package cn.edu.hust.memcached.server;

import cn.edu.hust.memcached.server.message.MessageHandler;
import cn.edu.hust.memcached.server.thread.ListeningThread;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by lxy on 2017/10/20.
 *
 */
public class ServerBootstrap {
    private ServerSocket serverSocket;
    private MessageHandler messageHandler;
    private ListeningThread listeningThread;

    public ServerBootstrap(int port, MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        try {
            this.serverSocket = new ServerSocket(port);
            //创建并启动监听线程
            this.listeningThread = new ListeningThread(serverSocket, messageHandler);
            listeningThread.start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public MessageHandler getMessageHandler() {
        return this.messageHandler;
    }

    //ready for use
    public void close() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                listeningThread.stopRun();
                serverSocket.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
