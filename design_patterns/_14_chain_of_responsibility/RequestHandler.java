package design_patterns._14_chain_of_responsibility;

public abstract class RequestHandler implements MiddlewareFunc{


    private RequestHandler next;
    protected abstract boolean check(Request req);//this wasnt added in MiddlewareFunc interface coz we dont want anyone to acess check() directly, we just want others to allow access to handle(), check() will be impl by Checker classes


    private RequestHandler setNext(RequestHandler next){
        this.next=next; 
        return this;
    }
    @Override
    public boolean handle(Request req) {
        if(!check(req)) return false;//check() will be impl by Checker classes
        if(next==null) return true;
        return next.handle(req);
    }
    //builder pattern to build chain of middlewares
    static class ChainBuilder{
        private static RequestHandler handler;
        public static RequestHandler buildChain(RequestHandler ...chain){
            handler=chain[0];
            RequestHandler ptr=handler;
            for(int i=1;i<chain.length;i++){
                ptr.setNext(chain[i]);
                ptr=ptr.next;
            }
            return handler;
        }
    }
}
