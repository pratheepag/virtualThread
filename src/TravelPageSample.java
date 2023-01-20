public class TravelPageSample {
    public static void main(String args[]) throws InterruptedException {
        Weather weather = Weather.readWeather();
        System.out.println(weather);
        Quotation quotation = Quotation.readQuotation();


    }
}
