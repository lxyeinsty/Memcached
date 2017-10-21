package cn.edu.hust.memcached.server.message;

import cn.edu.hust.memcached.cache.StoredValue;

/**
 * Created by lxy on 2017/10/21.
 *
 */
public class MessageInBound {
    private final CommandType commandType;
    private final String key;
    private final StoredValue storedValue;

    //私有构造方法,外部通过静态方法获取实例
    private MessageInBound(CommandType commandType, String key, StoredValue storedValue) {
        this.commandType = commandType;
        this.key = key;
        this.storedValue = storedValue;
    }

    public static MessageInBound newSetMessageInBound(String key, StoredValue storedValue) {
        return new MessageInBound(CommandType.SET, key, storedValue);
    }


    public static MessageInBound newGetMessageInBound(String key) {
        return new MessageInBound(CommandType.GET, key, null);
    }

    public String getKey() {
        return this.key;
    }

    public StoredValue getStoredValue() {
        return this.storedValue;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

}
