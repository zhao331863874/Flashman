package com.ddinfo.flashman.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Java 线程池
 * Java通过Executors提供四种线程池，分别为：
 * newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
 * newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
 * newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
 * newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
 *
 * @author weitf
 */
public class ExecutorPoolUtils {
  private static final int nThreads = 3;
  //创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
  private static ExecutorService cashedThreadPool = Executors.newCachedThreadPool();
  //创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
  private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(nThreads);
  //创建一个定长线程池，支持定时及周期性任务执行。延迟执行示
  public static ScheduledExecutorService scheduledExecutorService =
      Executors.newScheduledThreadPool(nThreads);

  /**
   * 执行指定的Runnable对象
   */
  public static void cashedExecute(Runnable runnable) {
    cashedThreadPool.execute(runnable);
  }

  /**
   * 可控制线程最大并发数，超出的线程会在队列中等待
   */
  public static void fixedExecute(Runnable runnable) {
    fixedThreadPool.equals(runnable);
  }

  /**
   * 延迟delay秒执行
   */
  public static void schedule(Runnable runnable, long delay) {
    scheduledExecutorService.schedule(runnable, delay, TimeUnit.SECONDS);
  }

  /**
   * 定期执行 表示延迟initialDelay秒后每period秒执行一次。
   */
  public static void scheduleAtFixedRate(Runnable runnable, long initialDelay, long period) {
    scheduledExecutorService.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.SECONDS);
  }
}
