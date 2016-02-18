package cc.meiwen.util.task;

import android.util.Log;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-29
 * Time: 14:10
 * Version 1.0
 * Info 线程池管理器
 */

public class ThreadPoolManager {

    //线程池大小
    private int poolSize = 0;
    private final static int MIN_POOL_SIEZ = 1;
    private final static int MAX_POOL_SIZE = 10;

    //线程池
    private ExecutorService threadPool;

    //执行队列
    private LinkedList<ThreadPoolTask> asyncTasks;

    //工作方式
    private int type;
    //先进先出
    public final static int TYPE_FIFO = 0;
    //后进先出
    public final static int TYPE_LIFO = 1;

    private Thread poolThread;

    private String tag = ThreadPoolManager.class.getSimpleName();

    public ThreadPoolManager(int type, int poolSize){
        this.type = (type==TYPE_FIFO)?TYPE_FIFO:TYPE_LIFO;

        if(poolSize<MIN_POOL_SIEZ) poolSize = MIN_POOL_SIEZ;
        if(poolSize>MAX_POOL_SIZE) poolSize = MAX_POOL_SIZE;

        this.poolSize = poolSize;

        threadPool = Executors.newFixedThreadPool(this.poolSize);

        asyncTasks = new LinkedList<>();
    }

    /**
     * 向任务队列中添加任务
     * */
    public void addAsyncTask(ThreadPoolTask task){
        synchronized (threadPool){
            Log.i(tag, "****addAsyncTask()****");
            asyncTasks.add(task);
        }
    }

    /**
     * 从任务队列中提取任务
     * */
    private ThreadPoolTask getAsyncTask(){
        synchronized (asyncTasks){
            if(asyncTasks.size()>0){
                ThreadPoolTask task = (this.type == TYPE_FIFO) ? asyncTasks.removeFirst() :
                        asyncTasks.removeLast();
                Log.i(tag, "****getAsyncTask()****");
                return task;
            }
        }
        return null;
    }

    /**
     * 开启线程池任务
     * */
    public void start(){
        if(poolThread == null){
            poolThread = new Thread(new PoolRunnable());
            poolThread.start();
        }
    }

    /**
     * 关闭线程池，结束任务
     * */
    public void stop(){
        poolThread.interrupt(); //中断
        poolThread = null;
    }

    private class PoolRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while(!Thread.currentThread().isInterrupted()){
                    ThreadPoolTask task = getAsyncTask();
                    if(task == null){
                        continue;
                    }
                    threadPool.execute(task);
                }
            } finally {
                if(threadPool.isShutdown()){
                    threadPool.shutdown();
                    Log.i(tag, "***关闭线程池***");
                }
            }
        }
    }


}
