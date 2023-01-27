import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

public class PlatformThread {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        IntStream.rangeClosed(0, 1_000_000).forEach( i -> {
            if (i % 10_000 == 0) {
                System.out.println(i);
            }
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread());
                    Thread.sleep(Duration.ofMinutes(10).toMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Run time: " + timeElapsed);
    }
}
