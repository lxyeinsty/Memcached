package cn.edu.hust.memcached.server;

import cn.edu.hust.memcached.cache.ICache;
import cn.edu.hust.memcached.cache.MemoryCache;
import cn.edu.hust.memcached.cache.StoredValue;
import cn.edu.hust.memcached.server.message.MessageHandler;

/**
 * Created by lxy on 2017/10/20.
 * 服务启动类
 */
public class Application {
    //缓存相关配置
    //设置缓存最大值为1M
    private static final long CACHE_MAX_SIZE = 1024 * 1024;
    //设置并发数为5,即同一时间最多只能有5个线程往缓存执行操作
    private static final int CACHE_CONCURRENCY_LEVEL = 5;

    public static void main(String[] args) throws Exception {
        final ICache<StoredValue> cache = new MemoryCache(CACHE_MAX_SIZE, CACHE_CONCURRENCY_LEVEL);
        MessageHandler messageHandler = new MessageHandler(cache);
        //配置启动server
        ServerBootstrap serverBootstrap = new ServerBootstrap(8888, messageHandler);
    }
}
