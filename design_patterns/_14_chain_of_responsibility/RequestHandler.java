package design_patterns._14_chain_of_responsibility;

public interface RequestHandler {
    void setNext(RequestHandler next);
    void handle(Request request);
}