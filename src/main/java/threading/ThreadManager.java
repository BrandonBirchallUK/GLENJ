package threading;

import java.util.Optional;
import java.util.concurrent.*;

public class ThreadManager {
    private static final int DEFAULT_THREADS = 12;
    private static ThreadManager instance;

    //double checked locking singleton
    public static ThreadManager getInstance(int nThreads) {
        if(instance == null) {
            synchronized (ThreadManager.class) {
                if(instance == null) {
                    instance = new ThreadManager(nThreads);
                }
            }
        }
        return instance;
    }

    public static ThreadManager getInstance() {
        if(instance == null) {
            synchronized (ThreadManager.class) {
                if(instance == null) {
                    instance = new ThreadManager(DEFAULT_THREADS);
                }
            }
        }
        return instance;
    }

    //////////////////////////////////////////////////////////




    private final ExecutorService threadPool;

    private ThreadManager(int nThreads) {
        threadPool = Executors.newFixedThreadPool(nThreads);
    }

    public void submitTask(Runnable toSubmit) {
        threadPool.submit(toSubmit);
    }

    public <T> Future<T> call(Callable<T> toCall) {
        return threadPool.submit(toCall);
    }

    public void submitFromTaskGroup(TaskGroup tg, int length) throws InterruptedException {
        for (int i = 0; i < length; i++) {
            Optional<Runnable> runnableOptional = tg.pop();
            runnableOptional.ifPresent(threadPool::submit);
        }
    }

    public void cleanUp() {
        threadPool.shutdown();
    }
}
