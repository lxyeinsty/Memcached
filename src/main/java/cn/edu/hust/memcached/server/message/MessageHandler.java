package cn.edu.hust.memcached.server.message;

import cn.edu.hust.memcached.cache.ICache;
import cn.edu.hust.memcached.cache.StoredValue;
import cn.edu.hust.memcached.server.message.enums.Status;

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

    public void onReceive(PrintWriter writer, MessageInBound messageInBound) {
        switch (messageInBound.getCommandType()) {
            case SET: {
                //如果存储时间为负数,立即清除
                if (messageInBound.getStoredValue().getTargetTime() < 0) {
                    cache.delete(messageInBound.getKey());
                } else {
                    cache.set(messageInBound.getKey(), messageInBound.getStoredValue());
                }
                writer.println(Status.STORED.getStatusMsg());
                break;
            }
            case GET: {
                String mulKey = messageInBound.getKey();
                String[] keys = mulKey.split(Decoder.GET_OR_DEL_KEY_SEPARATOR);
                for (int i = 0; i < keys.length; i++) {
                    StoredValue value = cache.get(keys[i]);
                    //如果数据过期,立即清除,0表示永不过期
                    if (value != null && value.getTargetTime() != 0
                            && (int)(System.currentTimeMillis() / 1000) > value.getTargetTime()) {
                        value = null;
                        cache.delete(messageInBound.getKey());
                    }

                    if (value != null) {
                        String result = String.format("VALUE %s %d %d\n%s",
                                keys[i], value.getFlags(), value.getBytes(), value.getData());
                        writer.println(result);
                    }

                    if (i == keys.length - 1) {
                        writer.println(Status.END.getStatusMsg());
                    }
                }
                break;

            }
            default: {
                writer.println("Unsupported this command");
            }
        }
    }
}
