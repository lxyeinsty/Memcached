package cn.edu.hust.memcached.server.message;

import cn.edu.hust.memcached.cache.ICache;
import cn.edu.hust.memcached.cache.StoredValue;
import cn.edu.hust.memcached.server.message.enums.Status;
import cn.edu.hust.memcached.server.message.exeception.MessageException;
import javafx.scene.media.MediaException;

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

    public void onReceive(PrintWriter writer, MessageInBound messageInBound) throws Exception {
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
                        cache.delete(keys[i]);
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
            case DELETE: {
                String key = messageInBound.getKey();
                StoredValue value = cache.get(key);
                if (value != null) {
                    cache.delete(key);
                    writer.println(Status.DELETED.getStatusMsg());
                } else {
                    writer.println(Status.NOT_FOUND);
                }
                break;
            }
            default: {
                throw new MessageException(Status.ERROR);
            }
        }
    }
}
