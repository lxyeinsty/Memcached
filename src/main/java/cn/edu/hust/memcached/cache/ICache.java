package cn.edu.hust.memcached.cache;

/**
 * Created by lxy on 2017/10/20.
 * Cache接口,包含缓存基本操作，ICache和Guava Cache区分
 */
public interface ICache<T> {

    void set(String key, T value);

    T get(String key);

    void delete(String key);
}
