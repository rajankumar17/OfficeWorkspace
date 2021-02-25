package CountDownLatch_6;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatch works in latch principle, the main thread will wait until the gate is open.
 * One thread waits for n threads, specified while creating the CountDownLatch.
 *
 * Any thread, usually the main thread of the application, which calls CountDownLatch.await()
 * will wait until count reaches zero or it's interrupted by another thread. All other threads
 * are required to count down by calling CountDownLatch.countDown() once they are completed or ready.
 *
 * As soon as count reaches zero, the waiting thread continues. One of the disadvantages/advantages
 * of CountDownLatch is that it's not reusable: once count reaches zero you cannot use CountDownLatch any more.
 *
 * Use CountDownLatch when one thread (like the main thread) requires to wait for one or more threads to
 * complete, before it can continue processing.
 *
 * A classical example of using CountDownLatch in Java is a server side core Java application which uses
 * services architecture, where multiple services are provided by multiple threads and the application
 * cannot start processing until all services have started successfully.
 * <br><br>
 */
class Processor implements Runnable {

    private CountDownLatch latch;

    public Processor(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        System.out.println("Started." + latch.getCount());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
        latch.countDown();
    }
}

public class App {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(30);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 30; i++) {
            executor.submit(new Processor(latch));
        }
        executor.shutdown();

        try {
            // Application’s main thread waits, till other service threads which are
            // as an example responsible for starting framework services have completed started all services.
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Completed.");
    }

}
