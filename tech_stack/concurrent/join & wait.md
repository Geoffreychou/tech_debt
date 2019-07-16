# join & wait

周末的时候，看到这样一个问题，让 3 个线程按顺序依次执行，当然，这个解决方法有很多，比如责任链模式的调用，future, join, sleep, lock & await & signal, synchronized & wait & notify 等等。

下面代码中 `joinTest()` & `waitTest()` 也是一个简单的实现，但其中的原理，一直比较模糊，今天正好有时间，大概研究了一下。

首先看`joinTest()` & `waitTest()`的实现：

2个方法的思路都是当前线程启动后，阻塞，直到当前线程执行完成， 在继续执行下面的逻辑

```java
    private static void joinTest() throws InterruptedException {
        Thread t1 = new Thread(() -> print("start"), "t1");
        t1.start();
        t1.join();
        Thread t2 = new Thread(() -> print("start"), "t2");
        t2.start();
        t2.join();
        Thread t3 = new Thread(() -> print("start"), "t3");
        t3.start();
    }

    private static void waitTest(){
        Thread t1 = new Thread(() -> print("start"), "t1");
        Thread t2 = new Thread(() -> print("start"), "t2");
        Thread t3 = new Thread(() -> print("start"), "t3");
        threadWait(t1);
        threadWait(t2);
    }

	private static void threadWait(Thread thread){
        if (Thread.State.NEW == thread.getState()){
            thread.start();
        }
        while(thread.isAlive()){
            synchronized (thread){
                try {
                    thread.wait();
                    print("wait end");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

	private static void print(String msg){
        System.out.printf("Thread-[%s]  %s \n", Thread.currentThread().getName(), msg);
    }
```

执行结果：

> joinTest()执行结果如下：
>
> Thread-[t1]  start 
> Thread-[t2]  start 
> Thread-[t3]  start 
>
> waitTest()执行结果如下：
>
> Thread-[t1]  start 
> Thread-[main]  wait end 
> Thread-[t2]  start 
> Thread-[main]  wait end 
> Thread-[t3]  start 
> Thread-[main]  wait end 



其实，最让人在意的是，在通常的理解中 当前线程 `wait()` 之后，是需要其他线程执行`notify()` 后，才可能会唤醒当前线程，但是，代码中并没有任何地方显式的执行了 `notify()`，这就让人很费解。

所以，先上一波 `join()` 的代码，值得注意的是，方法被 `synchronized` 修饰，而且，实际执行的也是 `wait()`方法。

其实我们 `waitTest()`的实现就是抄的 `join()`方法的实现。

```java
/**
     * Waits at most {@code millis} milliseconds for this thread to
     * die. A timeout of {@code 0} means to wait forever.
     *
     * <p> This implementation uses a loop of {@code this.wait} calls
     * conditioned on {@code this.isAlive}. As a thread terminates the
     * {@code this.notifyAll} method is invoked. It is recommended that
     * applications not use {@code wait}, {@code notify}, or
     * {@code notifyAll} on {@code Thread} instances.
     *
     * @param  millis
     *         the time to wait in milliseconds
     *
     * @throws  IllegalArgumentException
     *          if the value of {@code millis} is negative
     *
     * @throws  InterruptedException
     *          if any thread has interrupted the current thread. The
     *          <i>interrupted status</i> of the current thread is
     *          cleared when this exception is thrown.
     */
public final synchronized void join(long millis)
    throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (millis == 0) {
            while (isAlive()) {
                // 实际调用的是 wait(0) 方法，是 Object 中的 native 方法，线程此时释放锁并等待
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);
                now = System.currentTimeMillis() - base;
            }
        }
    }
```

```java
	//当前线程会处于等待，直到另一个线程执行了当前对象的 notify() or notifyAll()方法 或者是 当前线程已经等待了 timeout 的时间
	/**
     * Causes the current thread to wait until either another thread invokes the
     * {@link java.lang.Object#notify()} method or the
     * {@link java.lang.Object#notifyAll()} method for this object, or a
     * specified amount of time has elapsed.
     */
    public final native void wait(long timeout) throws InterruptedException;
```



那么现在所有的问题都集中在了何时执行了 `notify()`或者`notifyAll()`去唤醒线程呢？

我们来通过下面的例子还简单分析一下。

方法中创建了 3 个线程：

线程1: 启动后会 sleep 5s，然后结果

线程2: 启动后执行 wait() 等待

线程3: 启动 1s 后停止线程1

```java
    private static void waitAnalysisTest(){
        // 线程 1 sleep 5 seconds
        Thread t1 = new Thread(() -> {
            long start = System.currentTimeMillis();
            print("start time :" + start);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                long end = System.currentTimeMillis();
                print("end time :" + end);
                print("cost :" + (end - start));
            }
        }, "t1");
        t1.start();


        // 线程 2 等待被唤醒
        Thread t2 = new Thread(() -> {
            long start = System.currentTimeMillis();
            print("start time :" + start);
            try {
                synchronized (t1) {
                    while (t1.isAlive()) {
                        print("loop");
                        t1.wait();;
                        print("end wait");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                long end = System.currentTimeMillis();
                print("end time :" + end);
                print("cost :" + (end - start));
            }
        }, "t2");
        t2.start();

        // 线程 3 一秒后中断 线程 1
        new Thread(() -> {
            long start = System.currentTimeMillis();
            print("start time :" + start);
            try {
                TimeUnit.SECONDS.sleep(1);
                t1.stop();
                print("stop t1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                long end = System.currentTimeMillis();
                print("end time :" + end);
                print("cost :" + (end - start));
            }
        }, "t3").start();

    }

```

> 执行结果如下：
>
> ----------- 线程 1 2 3启动 --------------
>
> Thread-[t1]  start time :1554795590894 
> Thread-[t3]  start time :1554795590896 
> Thread-[t2]  start time :1554795590896 
> Thread-[t2]  loop 
>
> ----------- 线程  3 中执行停止线程 1 的操作 --------------
>
> Thread-[t3]  stop t1 
> Thread-[t1]  end time :1554795591932 
> Thread-[t3]  end time :1554795591932 
> Thread-[t1]  cost :1038 
> Thread-[t3]  cost :1036 
>
> ----------- 线程 1 终止后，线程 2 被唤醒执行 --------------
>
> Thread-[t2]  end wait 
> Thread-[t2]  end time :1554795591933 
> Thread-[t2]  cost :1037 

可以很明显的发现，当线程停止的时候，会执行唤醒操作。

其实，在 `join(long millis)`的方法注释上，有这样一段话，当一个线程终止时，会执行`notifyAll()`方法。

> As a thread terminates the {@code this.notifyAll} method is invoked



从 `Thread#stop()`方法中可以看出些许端倪，有一段注释如下，stop 操作会将所有 monitor 的锁释放。

> stop causes it to unlock all of the monitors that it has locked

猜测：释放锁之后，t2 中`wait()`方法获取到 t1 的 monitor，唤醒线程继续执行后续代码。

至于 JVM 是如何实现的，暂时就不去翻源码了，毕竟本人拙劣的 C / C++ 不忍直视。



以下是完整测试代码

```java
package xin.zero2one.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @author ZJD
 * @date 2019/4/9
 */
public class JoinAndWaitCompare {

    public static void main(String[] args) throws Exception {
        joinTest();
        waitTest();
        waitAnalysisTest();
    }
    
    private static void print(String msg){
        System.out.printf("Thread-[%s]  %s \n", Thread.currentThread().getName(), msg);
    }

    private static void joinTest() throws InterruptedException {
        Thread t1 = new Thread(() -> print("start"), "t1");
        t1.start();
        t1.join();
        Thread t2 = new Thread(() -> print("start"), "t2");
        t2.start();
        t2.join();
        Thread t3 = new Thread(() -> print("start"), "t3");
        t3.start();
    }

    private static void waitTest(){
        Thread t1 = new Thread(() -> print("start"), "t1");
        Thread t2 = new Thread(() -> print("start"), "t2");
        Thread t3 = new Thread(() -> print("start"), "t3");
        threadWait(t1);
        threadWait(t2);
    }

    private static void threadWait(Thread thread){
        if (Thread.State.NEW == thread.getState()){
            thread.start();
        }
        while(thread.isAlive()){
            synchronized (thread){
                try {
                    thread.wait();
                    print("wait end");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void waitAnalysisTest(){
        // 线程 1 sleep 5 seconds
        Thread t1 = new Thread(() -> {
            long start = System.currentTimeMillis();
            print("start time :" + start);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                long end = System.currentTimeMillis();
                print("end time :" + end);
                print("cost :" + (end - start));
            }
        }, "t1");
        t1.start();

        // 线程 2 等待被唤醒
        Thread t2 = new Thread(() -> {
            long start = System.currentTimeMillis();
            print("start time :" + start);
            try {
                synchronized (t1) {
                    while (t1.isAlive()) {
                        print("loop");
                        t1.wait();;
                        print("end wait");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                long end = System.currentTimeMillis();
                print("end time :" + end);
                print("cost :" + (end - start));
            }
        }, "t2");
        t2.start();

        // 线程 3 一秒后中断 线程 1
        new Thread(() -> {
            long start = System.currentTimeMillis();
            print("start time :" + start);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                long end = System.currentTimeMillis();
                print("end time :" + end);
                print("cost :" + (end - start));
            }
            t1.stop();
            print("stop t1");
        }, "t3").start();
    }
}

```

