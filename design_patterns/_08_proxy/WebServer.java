package design_patterns._08_proxy;
//RealSubject
public class WebServer implements Server{

    @Override
    public void handleRequest(String url) {
        System.out.println("Received request for "+url+" .....");        
        try{Thread.sleep(2000);}catch(Exception e){}
        System.out.println("Sent Response...");
    }
    
} 