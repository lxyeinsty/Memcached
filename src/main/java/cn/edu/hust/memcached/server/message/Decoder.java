package cn.edu.hust.memcached.server.message;

import cn.edu.hust.memcached.cache.StoredValue;
import cn.edu.hust.memcached.server.message.enums.Status;
import cn.edu.hust.memcached.server.message.exception.MessageException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lxy on 2017/10/21.
 * 用于解析客户端发送过来的消息命令
 */
public class Decoder {
    private static final int SET_COMMAND_LENGTH = 5;
    public static final String GET_KEY_SEPARATOR = ",";

    public static MessageInBound decodeMessage(InputStream in) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        final String[] message = reader.readLine().split(" ");
        if (message.length == 0) {
            throw new MessageException(Status.ERROR);
        }
        final String command = message[0];
        MessageInBound messageInBound;
        switch (command) {
            case "set": {
                if (message.length != SET_COMMAND_LENGTH) {
                    throw new MessageException(Status.ERROR);
                }
                messageInBound = decodeSetMessage(message, reader);
                break;
            }
            case "get": {
                messageInBound = decodeGetMessage(message);
                break;
            }
            case "delete": {
                messageInBound = decodeDeleteMessage(message);
                break;
            }
            default: {
                throw new MessageException(Status.ERROR);
            }

        }
        return messageInBound;
    }

    private static MessageInBound decodeSetMessage(final String[] message,
                                                   BufferedReader reader) throws Exception {
        try {
            final String key = message[1];
            final int flags = Integer.parseInt(message[2]);
            final int expireTime = Integer.parseInt(message[3]);
            //读取存储的数据,始终位于第二行
            final String data = reader.readLine();
            //设置过期时间
            final int bytes = Integer.parseInt(message[4]);
            final int targetTime = expireTime < 1 ? expireTime : (int)(System.currentTimeMillis() / 1000) + expireTime;
            //字符长度不符合
            if (data.toCharArray().length != bytes) {
                throw new MessageException(Status.CLIENT_ERROR_BAD_DATA);
            }
            StoredValue storedValue = new StoredValue(data, flags, targetTime, bytes);
            return MessageInBound.newSetMessageInBound(key, storedValue);
        } catch (NumberFormatException exception) {
            throw new MessageException(Status.CLIENT_ERROR_BAD_COMMAND);
        }
    }

    private static MessageInBound decodeGetMessage(final String[] message) throws Exception {
        if (message.length < 2) {
            throw new MessageException(Status.ERROR);
        }
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 1; i < message.length; i++) {
            keyBuilder.append(message[i]);
            if (i != message.length - 1) {
                keyBuilder.append(GET_KEY_SEPARATOR);
            }
        }
        final String key = keyBuilder.toString();
        return MessageInBound.newGetMessageInBound(key);
    }

    private static MessageInBound decodeDeleteMessage(final String[] message) throws Exception {
        if (message.length < 2) {
            throw new MessageException(Status.ERROR);
        }
        if (message.length > 2) {
            throw  new MessageException(Status.CLIENT_ERROR_BATCH_DEL);
        }
        final String key = message[1];
        return MessageInBound.newDeleteMessageInBound(key);
    }
}
