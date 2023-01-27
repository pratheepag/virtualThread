import jdk.incubator.concurrent.StructuredTaskScope;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public record Quotation(String agency, int quotation) implements PageComponent{

    public static class QuotationException extends RuntimeException{
    }
    private static class QuotationScope extends StructuredTaskScope<Quotation>{

        private Collection<Quotation> quotations = new ConcurrentLinkedDeque<>();
        private final Collection<Throwable> exceptions = new ConcurrentLinkedQueue<>();
        protected void handleComplete(Future<Quotation> future) {
            switch(future.state()){
                case RUNNING -> throw new IllegalStateException("Nope");
                case SUCCESS -> this.quotations.add(future.resultNow());
                case FAILED -> this.exceptions.add(future.exceptionNow());
                case CANCELLED -> {

                }
            }
        }

        public Quotation quotation(){

            QuotationException exception = new QuotationException();
            exceptions.forEach(exception::addSuppressed);
            //System.out.println(quotations);
            return quotations.stream()
                    .min(Comparator.comparing(Quotation::quotation))
                    .orElseThrow(() -> exception);
        }

        @Override
        public QuotationScope joinUntil(Instant deadline) throws InterruptedException, TimeoutException {
            try{
                super.joinUntil(deadline);
            }catch(TimeoutException t){

            }
            return this;
        }
    }
    public static Quotation readQuotation() throws InterruptedException{
        Random random = new Random();
        try(var scope = new QuotationScope()){
            scope.fork(() ->{
                Thread.sleep(random.nextInt(80,120));
                return new Quotation("Agency A", 300);
            });

            scope.fork(() ->{
                Thread.sleep(random.nextInt(20,110));
                return new Quotation("Agency B", 100);
            });

            scope.fork(() ->{
                Thread.sleep(random.nextInt(10,130));
                return new Quotation("Agency C", 200);
            });

            scope.joinUntil(Instant.now().plusMillis(1000));

            return scope.quotation();
        }catch(InterruptedException | TimeoutException e){
            throw new RuntimeException(e);
        }
     //   return new Quotation("NA", 100);
    }
}
