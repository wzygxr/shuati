package class036;

import java.util.*;

// LeetCode 662. 二叉树最大宽度
// 题目链接: https://leetcode.cn/problems/maximum-width-of-binary-tree/
// 题目大意: 给你一棵二叉树的根节点 root ，返回树的 最大宽度 。
// 树的 最大宽度 是所有层中最大的 宽度 。
// 每一层的 宽度 被定义为该层最左和最右的非空节点（即，两个端点）之间的长度。
// 将这个二叉树视作与满二叉树结构相同，两端点间会出现一些延伸到这一层的 null 节点，这些 null 节点也计入长度。

public class LeetCode662_MaximumWidthOfBinaryTree {
    
    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    /**
     * 方法1: 使用BFS层序遍历，给每个节点分配位置索引
     * 思路: 在完全二叉树中，如果父节点的位置是i，那么左子节点的位置是2*i，右子节点的位置是2*i+1
     * 时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    public static int widthOfBinaryTree1(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        // 使用队列存储节点及其位置索引
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        Queue<Integer> indexQueue = new LinkedList<>();
        nodeQueue.offer(root);
        indexQueue.offer(1);
        
        int maxWidth = 0;
        
        while (!nodeQueue.isEmpty()) {
            int size = nodeQueue.size();
            int leftIndex = indexQueue.peek(); // 当前层最左边节点的索引
            int rightIndex = leftIndex; // 初始化为最左边节点的索引
            
            for (int i = 0; i < size; i++) {
                TreeNode node = nodeQueue.poll();
                int index = indexQueue.poll();
                rightIndex = index; // 更新最右边节点的索引
                
                // 添加子节点到队列
                if (node.left != null) {
                    nodeQueue.offer(node.left);
                    indexQueue.offer(index * 2);
                }
                if (node.right != null) {
                    nodeQueue.offer(node.right);
                    indexQueue.offer(index * 2 + 1);
                }
            }
            
            // 计算当前层的宽度
            maxWidth = Math.max(maxWidth, rightIndex - leftIndex + 1);
        }
        
        return maxWidth;
    }
    
    /**
     * 方法2: 优化的BFS，避免索引过大导致的整数溢出
     * 思路: 每层重新编号，将最左边节点的索引作为基准(1)，其他节点相对编号
     * 时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    public static int widthOfBinaryTree2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        // 使用队列存储节点及其位置索引
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        Queue<Integer> indexQueue = new LinkedList<>();
        nodeQueue.offer(root);
        indexQueue.offer(1);
        
        int maxWidth = 0;
        
        while (!nodeQueue.isEmpty()) {
            int size = nodeQueue.size();
            int leftIndex = indexQueue.peek(); // 当前层最左边节点的索引
            
            for (int i = 0; i < size; i++) {
                TreeNode node = nodeQueue.poll();
                int index = indexQueue.poll();
                
                // 重新编号，避免索引过大
                int normalizedIndex = index - leftIndex + 1;
                
                // 添加子节点到队列
                if (node.left != null) {
                    nodeQueue.offer(node.left);
                    indexQueue.offer(normalizedIndex * 2);
                }
                if (node.right != null) {
                    nodeQueue.offer(node.right);
                    indexQueue.offer(normalizedIndex * 2 + 1);
                }
            }
            
            // 计算当前层的宽度
            int rightIndex = indexQueue.isEmpty() ? 1 : indexQueue.peek();
            maxWidth = Math.max(maxWidth, rightIndex);
        }
        
        return maxWidth;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试二叉树: [1,3,2,5,3,null,9]
        //       1
        //      / \
        //     3   2
        //    / \   \
        //   5   3   9
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(3);
        root.right = new TreeNode(2);
        root.left.left = new TreeNode(5);
        root.left.right = new TreeNode(3);
        root.right.right = new TreeNode(9);
        
        System.out.println("方法1结果: " + widthOfBinaryTree1(root));
        System.out.println("方法2结果: " + widthOfBinaryTree2(root));
    }
}