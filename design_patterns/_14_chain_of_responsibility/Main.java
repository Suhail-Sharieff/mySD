package design_patterns._14_chain_of_responsibility;

//can be used to build chained middlewares or tests






public class Main {

    public static void main(String[] args) {

        Request req=new Request("TOKEN_1");

        RequestHandler handler=RequestHandler.ChainBuilder
        .buildChain(
            new Check1(),
            new Check2(),
            new Check3()
        );


        handler.handle(req);
    }
}

/*Check 1 PASS
Check 2 FAIL : No "TOKEN_2" found

//see that it didnt move to Check 3 ahead
*/