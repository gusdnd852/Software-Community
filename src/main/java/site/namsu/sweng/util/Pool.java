package site.namsu.sweng.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @Author : Hyunwoong
 * @When : 2018-11-27 오전 3:22
 * @Homepage : https://github.com/gusdnd852
 */
public class Pool {
    private static final Map<String, Object> singletonPool = new ConcurrentHashMap<>();
    private static final Executor singleBackgroundThreadPool = Executors.newSingleThreadExecutor();
    private static final Executor cachedBackgroundThreadPool = Executors.newCachedThreadPool();
    private static final Executor mainThread = Runnable::run;

    public static void single(Runnable runnable) {
        singleBackgroundThreadPool.execute(runnable);
    }

    public static void main(Runnable runnable) {
        mainThread.execute(runnable);
    }

    public static void cached(Runnable runnable) {
        singleBackgroundThreadPool.execute(runnable);
    }

    public static Map<String, Object> singleton() {
        return singletonPool;
    }
}
