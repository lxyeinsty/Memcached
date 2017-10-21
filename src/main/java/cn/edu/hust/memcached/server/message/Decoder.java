package cn.edu.hust.memcached.server.message;

import cn.edu.hust.memcached.cache.StoredValue;
import cn.edu.hust.memcached.server.message.MessageInBound;
import cn.edu.hust.memcached.server.message.exeception.UnSupportedCommandException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lxy on 2017/10/21.
 * 用于解析客户端发送过来的消息命令
 */
public class Decoder {

    public static MessageInBound decodeMessage(InputStream in) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        //数据没有换行接收,data数据在行尾
        final String[] message = reader.readLine().split(" ");
        final String command = message[0];
        MessageInBound messageInBound;
        switch (command) {
            case "set": {
                messageInBound = decodeSetMessage(message);
                break;
            }
            case "get": {
                messageInBound = decodeGetMessage(message);
                break;
            }
            default: {
                throw new UnSupportedCommandException(command);
            }

        }
        return messageInBound;
    }

    private static MessageInBound decodeSetMessage(final String[] message) {
        final String key = message[1];
        final int flags = Integer.parseInt(message[2]);
        final int expireTime = Integer.parseInt(message[3]);
        final int targetTime = expireTime < 1 ? expireTime : (int)(System.currentTimeMillis() / 1000) + expireTime;
        final String data = message[4];

        StoredValue storedValue = new StoredValue(data, flags, targetTime);
        return MessageInBound.newSetMessageInBound(key, storedValue);
    }

    private static MessageInBound decodeGetMessage(final String[] message) {
        final String key = message[1];
        return MessageInBound.newGetMessageInBound(key);
    }
}
