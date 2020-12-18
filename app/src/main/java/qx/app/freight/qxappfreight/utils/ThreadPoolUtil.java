package qx.app.freight.qxappfreight.utils;

/**
 * @ProjectName: qx_app_freight
 * @Package: qx.app.freight.qxappfreight.utils
 * @ClassName: ThreadPoolUtil
 * @Description: java类作用描述
 * @Author: 张耀
 * @CreateDate: 2020/12/15 11:33
 * @UpdateUser: 更新者：
 * @UpdateDate: 2020/12/15 11:33
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 * @author ytf
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUtil {
    //线程池核心线程数
    private static int CORE_POOL_SIZE = 5;
    //线程池最大线程数
    private static int MAX_POOL_SIZE = 100;
    //额外线程空状态生存时间
    private static int KEEP_ALIVE_TIME = 10000;
    //阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
    private static BlockingQueue workQueue = new ArrayBlockingQueue(10);
    //线程池
    public static ThreadPoolExecutor threadPool;

    private ThreadPoolUtil() {
    }

    //线程工厂
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "myThreadPool thread:" + integer.getAndIncrement());
        }
    };

    static {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, workQueue, threadFactory);
    }

    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }

    public static void execute(FutureTask futureTask) {
        threadPool.execute(futureTask);
    }

    public static void cancel(FutureTask futureTask) {
        futureTask.cancel(true);
    }
}
