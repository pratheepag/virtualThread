import java.util.stream.IntStream;

public class VirtualThreadSample {
    public static void main(String args[] ) throws InterruptedException {
        /*Runnable runnable = () -> System.out.println(Thread.currentThread().threadId());
        Thread thread = Thread.ofVirtual().name("testVT").unstarted(runnable);
        Thread testPT = Thread.ofPlatform().name("testPT").unstarted(runnable);
        testPT.start(); */

        /*long start = System.currentTimeMillis();

        Random random = new Random();
        Runnable runnable = () -> { double i = random.nextDouble(1000) % random.nextDouble(1000);  };
        Thread thread1 = Thread.startVirtualThread(() -> {
            System.out.println("Hello World from virtual thread");
        });

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Run time: " + timeElapsed); */

        Thread pthread = Thread.ofPlatform().unstarted(
                () -> { System.out.println(Thread.currentThread());
                });
        pthread.start();
        pthread.join();

        Thread vthread = Thread.ofVirtual().unstarted(() -> { System.out.println(Thread.currentThread());});
        vthread.start();
        vthread.join();

        Thread vthread1 = Thread.ofVirtual().unstarted(() -> { System.out.println(Thread.currentThread());});
        vthread1.start();
        vthread1.join();

        Thread vthread2 = Thread.ofVirtual().unstarted(() -> { System.out.println(Thread.currentThread());});
        vthread2.start();
        vthread2.join();

        Thread vthread3 = Thread.ofVirtual().unstarted(() -> { System.out.println(Thread.currentThread());});
        vthread3.start();
        vthread3.join();

        Thread vthread4 = Thread.ofVirtual().unstarted(() -> { System.out.println(Thread.currentThread());});
        vthread4.start();
        vthread4.join(); 
    }
}
