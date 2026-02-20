package design_patterns._06_adapter;

import java.util.LinkedList;
import java.util.Queue;

/*Say client will send data as int array of nums and expects string 
But server is designed to take TreeNode as input and return string

So the client cant send the server nums, coz server doent know how to dela with nums,
so create an adapter to parse the nums to root, pass to server and get string, then return to client

now the client doent know about how server will do it, it justs sends data to adapter and gets ata from it
*/

public class _01_adapter {
    class TreeNode {
        int val;

        public TreeNode(int val) {
            this.val = val;
        }

        TreeNode left, right;
    }

    class Server {// Legacy Server
        TreeNode getRoot(int nums[]) {
            Queue<TreeNode> q = new LinkedList<>();
            int i = 1;
            TreeNode root = new TreeNode(nums[0]);
            q.offer(root);
            while (!q.isEmpty()) {
                TreeNode top = q.poll();
                if (i < nums.length)
                    top.left = new TreeNode(nums[i++]);
                if (i < nums.length)
                    top.right = new TreeNode(nums[i++]);
                if (top.left != null)
                    q.offer(top.left);
                if (top.right != null)
                    q.offer(top.right);
            }
            return root;
        }
    }

    // Target Interface (what client expects)
    interface StringDataProcessor {
        String process(int nums[]);
    }

    // Adapter
    class Adapter implements StringDataProcessor {
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

    // Client
    class Client {
        StringDataProcessor processor;

        public Client(StringDataProcessor processor) {
            this.processor = processor;
        }

        String process(int nums[]) {
            return processor.process(nums);
        }
    }
}