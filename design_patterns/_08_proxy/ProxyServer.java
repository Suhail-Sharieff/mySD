package design_patterns._08_proxy;

import java.util.Arrays;
import java.util.HashSet;


/*ProxySubject */
public class ProxyServer implements Server{
    private HashSet<String>isRestricted;//for caching also we can use then use cache
    private Server server;
    public ProxyServer(Server server){
        isRestricted=new HashSet<>(Arrays.asList("alibaba.com","facebook.com"));
        this.server=server;
    }
    @Override
    public void handleRequest(String url) {
        if(isRestricted.contains(url)){
            System.out.println("Access to this web is restricted");
            return;
        }
        server.handleRequest(url);
    }
    
}
 