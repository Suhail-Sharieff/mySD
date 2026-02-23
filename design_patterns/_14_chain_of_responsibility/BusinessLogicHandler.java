package design_patterns._14_chain_of_responsibility;

public class BusinessLogicHandler extends BaseHandler {
    @Override
    public void handle(Request request) {
        System.out.println("BusinessLogicHandler: 🚀 Processing request...");
        // Core application logic goes here
    }
}
