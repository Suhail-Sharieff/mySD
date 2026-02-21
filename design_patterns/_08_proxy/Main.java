package design_patterns._08_proxy;
/*

used for security bfr accessing for some websites
proxy server can be used as cache as well, client makes req, proxy checks its cache it it exists returen else ask data from real subjet

*/



//Client
public class Main {
    public static void main(String[] args) {

        Server client=new ProxyServer(new WebServer());//in future say we can add new ProxyServer(AppServer()) also

        client.handleRequest("alibaba.com");//Access to this web is restricted

    }
}
