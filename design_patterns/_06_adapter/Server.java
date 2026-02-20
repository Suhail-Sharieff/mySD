package design_patterns._06_adapter;

import java.util.LinkedList;
import java.util.Queue;

public class Server {// Legacy Server
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
