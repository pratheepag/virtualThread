import jdk.incubator.concurrent.StructuredTaskScope;

import java.time.Instant;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public class TravelPageSample {
    private static class TravelPageScope extends StructuredTaskScope<PageComponent>{
        private volatile Quotation quotation;
        private volatile Weather weather = Weather.UNKNOWN;
        private volatile Quotation.QuotationException exception;
        private Future<PageComponent> future;

        @Override
        protected void handleComplete(Future<PageComponent> future) {
            switch(future.state()){
                case RUNNING -> throw new IllegalStateException("Nope");
                case SUCCESS -> {
                    switch (future.resultNow()){
                        case Quotation quotation -> this.quotation = quotation;
                        case Weather weather -> this.weather = weather;
                    }
                }
                case FAILED -> {
                    switch(future.exceptionNow()){
                        case Quotation.QuotationException exception ->
                            this.exception = exception;
                        case Throwable t -> throw new RuntimeException();
                    }
                }
                case CANCELLED -> {

                }
            }
        }
        public TravelPage travelPage(){
            if(quotation != null){
                return new TravelPage(quotation, weather);
            }else{
                throw this.exception;
            }
        }
        @Override
        public TravelPageScope joinUntil(Instant deadline) throws InterruptedException {
            try{
                super.joinUntil(deadline);
            }catch(TimeoutException t){
                this.shutdown();
            }
            return this;
        }
    }
    public static void main(String args[]) throws InterruptedException {
        try(var scope = new TravelPageScope()){
            scope.fork(Weather::readWeather);
            scope.fork(Quotation::readQuotation);

            scope.joinUntil(Instant.now().plusMillis(1000));
            TravelPage travelPage = scope.travelPage();
            System.out.println("Travel Page = "+travelPage);
       }
    }
}
