package design_patterns._14_chain_of_responsibility;

public 
class Check3 extends RequestHandler{
    @Override
    public boolean check(Request req) {
        if(req.has("TOKEN_3")){
            System.out.println("Check 3 PASS");
            return true;
        }
        System.out.println("Check 3 FAIL : No \"TOKEN_3\" found");
        return false;
    }
}
