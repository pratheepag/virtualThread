import jdk.incubator.concurrent.StructuredTaskScope;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public record Weather(String agency, String weather) {
    public static Weather UNKNOWN = new Weather("NA", "Mostly Sunny");
    private static class WeatherScope implements AutoCloseable{
        private StructuredTaskScope.ShutdownOnSuccess<Weather> scope = new StructuredTaskScope.ShutdownOnSuccess<>();
        private boolean timeout = false;
        public WeatherScope joinUntil(Instant deadline) throws  InterruptedException {
            try{
                scope.joinUntil(deadline);
            }catch(TimeoutException exp){
                this.timeout = true;
            }
            return this;
        }
        public <U extends Weather> Future<U> fork(Callable<? extends U> task ){
            return scope.fork(task);
        }
        @Override
        public void close() throws Exception {
            scope.close();
        }
        public Weather weather() throws ExecutionException {
            if(!timeout) return scope.result();
            return UNKNOWN;
        }
    }

    public static Weather readWeather() throws InterruptedException{
        Random random = new Random();
        Weather weather;
        try(var scope = new WeatherScope()){
            Future<Weather> weatherA = scope.fork(() -> {
                Thread.sleep(30, 110);
                return new Weather("WA1", "Sunny");
            });
            Future<Weather> weatherB = scope.fork(() -> {
                Thread.sleep(40, 90);
                return new Weather("WA2", "Sunny");
            });
            Future<Weather> weatherC = scope.fork(() -> {
                Thread.sleep(50, 110);
                return new Weather("WA3", "Sunny");
            });
            scope.joinUntil(Instant.now().plusMillis(100));

            weather = scope.weather();

            System.out.println("FutureA == "+weatherA.state());
            System.out.println("FutureB == "+weatherB.state());
            System.out.println("FutureC == "+weatherC.state());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            return UNKNOWN;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return weather;
    }
}
