package design_patterns._14_chain_of_responsibility;

public 
class Check2 extends RequestHandler{
    @Override
    public boolean check(Request req) {
        if(req.has("TOKEN_2")){
            System.out.println("Check 2 PASS");
            return true;
        }
        System.out.println("Check 2 FAIL : No \"TOKEN_2\" found");
        return false;
    }
}