package design_patterns._14_chain_of_responsibility;

public 
class Check1 extends RequestHandler{
    @Override
    public boolean check(Request req) {
        if(req.has("TOKEN_1")){
            System.out.println("Check 1 PASS");
            return true;
        }
        System.out.println("Check 1 FAIL : No \"TOKEN_1\" found");
        return false;
    }
}
