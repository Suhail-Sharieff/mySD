package design_patterns._14_chain_of_responsibility;

public interface MiddlewareFunc{
    boolean handle(Request req);
}