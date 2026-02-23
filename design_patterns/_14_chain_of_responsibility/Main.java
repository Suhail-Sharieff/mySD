package design_patterns._14_chain_of_responsibility;
//can be used to design chain of middle wares
public class Main {
    public static void main(String[] args) {
        // Create handlers
        RequestHandler auth = new AuthenticationHandler();
        RequestHandler authorization = new AuthorizationHandler();
        RequestHandler rateLimit = new RateLimitHandler();
        RequestHandler validation = new ValidationHandler();
        RequestHandler businessLogic = new BusinessLogicHandler();

        // Build the chain
        auth.setNext(authorization);
        authorization.setNext(rateLimit);
        rateLimit.setNext(validation);
        validation.setNext(businessLogic);

        // Send a request through the chain
        Request request = new Request("john", "ADMIN", 10, "{ \"data\": \"valid\" }");
        auth.handle(request);

        System.out.println("\n--- Trying an invalid request ---");
        Request badRequest = new Request(null, "USER", 150, "");
        auth.handle(badRequest);
    }
}
