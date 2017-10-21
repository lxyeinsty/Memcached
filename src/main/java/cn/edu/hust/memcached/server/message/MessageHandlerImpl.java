package cn.edu.hust.memcached.server.message;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by lxy on 2017/10/20.
 *
 */
public class MessageHandlerImpl implements MessageHandler {

    @Override
    public void onReceive(Socket socket, String message) {
        print(socket, message);
    }

    private void print(Socket socket, String message) {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
