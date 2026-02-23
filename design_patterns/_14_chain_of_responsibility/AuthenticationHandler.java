package design_patterns._14_chain_of_responsibility;

public class AuthenticationHandler extends BaseHandler {
    @Override
    public void handle(Request request) {
        if (request.user == null) {
            System.out.println("AuthHandler: ❌ User not authenticated.");
            return; // Stop the chain
        }
        System.out.println("AuthHandler: ✅ Authenticated.");
        forward(request);
    }
}
