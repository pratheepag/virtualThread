import java.util.stream.IntStream;

public class VirtualThreadSample {
    public static void main(String args[] ) throws InterruptedException {
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
