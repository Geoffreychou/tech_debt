package xin.zero2one.redis.client;

import jodd.time.TimeUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;

import java.util.concurrent.TimeUnit;

/**
 *
 * reference https://github.com/redisson/redisson
 *
 * @author ZJD
 * @date 2019/4/8
 */
public class RedissonDemo {

    private static final String LOCK = "lock";

    private static RedissonClient redisson;
    static {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.25.132:6379");
        redisson = Redisson.create(config);
    }

    public static void main(String[] args) {
        reentrantLockTest();
//        redisson.shutdown();
    }

    private static void reentrantLockTest(){
        RLock lock = redisson.getLock(LOCK);
        new Thread(new LockThread(lock),"t1").start();
        new Thread(new LockThread(lock),"t2").start();
    }

    private static class LockThread implements Runnable{
        private RLock lock;

        public LockThread(RLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            try{
                lock.tryLock(10, 5, TimeUnit.SECONDS);
                print();
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void print(){
        System.err.printf("Thread [%s] get lock \n", Thread.currentThread().getName());
    }

}
