package cn.edu.hust.memcached.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Cache;

/**
 * Created by lxy on 2017/10/20.
 * 基于Guava实现本地内存缓存
 */
public class MemoryCache implements ICache<StoredValue> {
    private final Cache<String, StoredValue> cache;

    public MemoryCache(long maximumSize, int concurrencyLevel) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        builder.maximumSize(maximumSize).
                concurrencyLevel(concurrencyLevel);
        this.cache = builder.<String, StoredValue>build();
    }

    @Override
    public void set(String key, StoredValue value) {
        cache.put(key, value);
    }

    @Override
    public StoredValue get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void delete(String key) {
        cache.invalidate(key);
    }
}
