package design_patterns._06_adapter;


/*Say client will send data as int array of nums and expects string 
But server is designed to take TreeNode as input and return string

So the client cant send the server nums, coz server doent know how to dela with nums,
so create an adapter to parse the nums to root, pass to server and get string, then return to client

now the client doent know about how server will do it, it justs sends data to adapter and gets ata from it
*/

// Target Interface (what client expects)

// Adapter
public class Adapter implements StringDataProcessor {
    Server myServer;

    public Adapter() {
        myServer = new Server();
    }

    @Override
    public String process(int nums[]) {
        return getStringFormat(myServer.getRoot(nums));
    }

    private String getStringFormat(TreeNode root) {
        if (root == null)
            return "";
        return root.val + "->" + getStringFormat(root.left) + getStringFormat(root.right);
    }
}
