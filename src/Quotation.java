public record Quotation(String agency, int quotation) {
    public static Quotation readQuotation() throws InterruptedException{
        return new Quotation("NA", 100);
    }
}
