import java.time.Duration;
import java.util.stream.IntStream;

public class VirtualThread {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        IntStream.rangeClosed(0, 1_000_000).forEach(i -> {
            if (i%10_000 == 0) {
                System.out.println(i);
            }
            Thread.startVirtualThread(() -> {
                try {
                    System.out.println(Thread.currentThread());
                    Thread.sleep(Duration.ofMinutes(10).toMillis());

                } catch (Exception e) {
                    e.printStackTrace();
                }});
        });
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Run time: " + timeElapsed);
    }
}
