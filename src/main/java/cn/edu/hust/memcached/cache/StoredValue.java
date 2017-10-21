package cn.edu.hust.memcached.cache;

/**
 * Created by lxy on 2017/10/20.
 * 存储在缓存中的Value
 */
public class StoredValue {
    private String data;
    private int flags;
    //存储时间,单位秒
    private int targetTime;

    public StoredValue(String value, int flags, int targetTime) {
        this.data = value;
        this.flags = flags;
        this.targetTime = targetTime;
    }

    public String getData() {
        return this.data;
    }

    public int getFalgs() {
        return this.flags;
    }

    public int getTargetTime() {
        return this.targetTime;
    }

    @Override
    public String toString() {
        return "Value = { data = " + data + ", flags = " + flags + ", targetTime = " + targetTime + "}";
    }
}
