package cn.edu.hust.memcached.server.message;

import cn.edu.hust.memcached.cache.ICache;
import cn.edu.hust.memcached.cache.StoredValue;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by lxy on 2017/10/20.
 *
 */
public class MessageHandler {

    private final ICache<StoredValue> cache;

    public MessageHandler(ICache<StoredValue> cache) {
        this.cache = cache;
    }

    public void onReceive(PrintWriter writer, MessageInBound message) {
        switch (message.getCommandType()) {
            case SET: {
                //如果存储时间为负数,立即清除
                if (message.getStoredValue().getTargetTime() < 0) {
                    cache.delete(message.getKey());
                } else {
                    cache.set(message.getKey(), message.getStoredValue());
                }
                writer.println("STORED");
                break;
            }
            case GET: {
                StoredValue value = cache.get(message.getKey());
                //如果数据过期,立即清除,0表示永不过期
                if (value != null && value.getTargetTime() != 0
                        && (int)(System.currentTimeMillis() / 1000) > value.getTargetTime()) {
                    value = null;
                    cache.delete(message.getKey());
                }

                if (value != null) {
                    String result = String.format("VALUE %s %d",
                            message.getKey(), value.getFlags());
                    writer.println(result);
                    writer.println(value.getData());
                }
                writer.println("END");
                break;

            }
            default: {
                writer.println("Unsupported this command");
            }
        }
    }
}
