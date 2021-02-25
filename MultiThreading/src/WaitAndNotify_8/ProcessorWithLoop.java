package WaitAndNotify_8;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

/**
 * Some background knowledge<br>
 * Source: <em>http://www.programcreek.com/2009/02/notify-and-wait-example/</em>
 * <br><br>
 * {@code synchronized} keyword is used for exclusive accessing. To make a
 * method {@code synchronized}, simply add the {@code synchronized} keyword to its
 * declaration.<be>
 * Then no two invocations of synchronized methods on the same object can
 * interleave with each other.
 * <br>
 * Synchronized statements must specify the object that
 * provides the intrinsic lock. When {@code synchronized(this)} is used, you
 * have to avoid to synchronizing invocations of other objects' methods.
 * <br>
 * {@link Object#wait()} tells
 * the calling thread to give up the lock and go to sleep (not polling) until
 * some other thread enters the same lock and calls {@link Object#notify()}.
 * <br>
 * {@link Object#notify()} wakes up the first thread that called wait() on
 * the same object.
 * <br><br>
 * Codes with minor comments are from
 * <a href="http://www.caveofprogramming.com/youtube/">
 * <em>http://www.caveofprogramming.com/youtube/</em>
 * </a>
 * <br>
 * also freely available at
 * <a href="https://www.udemy.com/java-multithreading/?couponCode=FREE">
 * <em>https://www.udemy.com/java-multithreading/?couponCode=FREE</em>
 * </a>
 *
 * @author Z.B. Celik <celik.berkay@gmail.com>
 */
public class ProcessorWithLoop {
    public static void main(String[] args) throws InterruptedException {
        final ProcessorLoop processor = new ProcessorLoop();
        final Object lock = new Object();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    processor.produce(lock);
                } catch (InterruptedException ignored) {
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    processor.consume(lock);
                } catch (InterruptedException ignored) {
                }
            }
        });

        t1.start();
        t2.start();
        /*t1.join();
        t2.join();*/
    }
}

class ProcessorLoop {

    /*
     * public synchronized void getSomething(){ this.hello = "hello World"; }
     * public void getSomething(){ synchronized(this){ this.hello = "hello
     * World"; } }
     * two code blocks by specification, functionally identical.
     */

    private Queue<Integer> queue = new LinkedList<>();
    private final int LIMIT = 10;

    public void produce(Object lock) throws InterruptedException {
        while (true) {//loop indefinitely
            Random random = new Random();
            synchronized (lock) {
                int ranVal = random.nextInt(100);
                if (queue.size() == LIMIT) {
                    lock.wait();
                }
                queue.add(ranVal);//if queue is full (10) waits
                System.out.println("Added value: " + ranVal + "; Queue size is: " + queue.size());
                lock.notify();
            }
        }
    }

    public void consume(Object lock) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Thread.sleep(2000);
        while (true) {
            Random random = new Random();
            synchronized (lock) {
                if (random.nextInt(10) == 0) {
                    if (queue.size() == 0) {
                        lock.wait();
                    }
                    Integer value = queue.remove();//if queue is empty waits
                    System.out.println("Taken value: " + value + "; Queue size is: " + queue.size());
                    lock.notify();
                }
            }
            Thread.sleep(100);
        }
    }
}
