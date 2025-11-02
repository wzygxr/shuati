package class036;

import java.util.*;

// LeetCode 101. 对称二叉树
// 题目链接: https://leetcode.cn/problems/symmetric-tree/
// 题目大意: 给你一个二叉树的根节点 root ，检查它是否轴对称。

public class LeetCode101_SymmetricTree {
    
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
     * 方法1: 递归实现判断二叉树是否对称
     * 思路:
     * 1. 创建辅助函数比较两个子树是否互为镜像
     * 2. 如果两个节点都为空，返回true
     * 3. 如果其中一个节点为空，另一个不为空，返回false
     * 4. 如果两个节点的值不相等，返回false
     * 5. 递归比较左子树的左子树与右子树的右子树，以及左子树的右子树与右子树的左子树
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(h) - h是树的高度，递归调用栈的深度
     */
    public static boolean isSymmetric1(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isMirror(root.left, root.right);
    }
    
    /**
     * 辅助函数：判断两个子树是否互为镜像
     * @param left 左子树根节点
     * @param right 右子树根节点
     * @return 是否互为镜像
     */
    private static boolean isMirror(TreeNode left, TreeNode right) {
        // 如果两个节点都为空，返回true
        if (left == null && right == null) {
            return true;
        }
        
        // 如果其中一个节点为空，另一个不为空，返回false
        if (left == null || right == null) {
            return false;
        }
        
        // 如果两个节点的值不相等，返回false
        if (left.val != right.val) {
            return false;
        }
        
        // 递归比较左子树的左子树与右子树的右子树，以及左子树的右子树与右子树的左子树
        return isMirror(left.left, right.right) && isMirror(left.right, right.left);
    }
    
    /**
     * 方法2: 迭代实现判断二叉树是否对称
     * 思路:
     * 1. 使用队列存储待比较的节点对
     * 2. 每次从队列中取出一对节点进行比较
     * 3. 如果节点对都为空，继续下一对
     * 4. 如果其中一个为空或值不相等，返回false
     * 5. 将左子树的左子节点与右子树的右子节点、左子树的右子节点与右子树的左子节点加入队列
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    public static boolean isSymmetric2(TreeNode root) {
        if (root == null) {
            return true;
        }
        
        // 使用队列存储待比较的节点对
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root.left);
        queue.offer(root.right);
        
        while (!queue.isEmpty()) {
            // 取出一对节点
            TreeNode left = queue.poll();
            TreeNode right = queue.poll();
            
            // 如果两个节点都为空，继续下一对
            if (left == null && right == null) {
                continue;
            }
            
            // 如果其中一个节点为空或值不相等，返回false
            if (left == null || right == null || left.val != right.val) {
                return false;
            }
            
            // 将左子树的左子节点与右子树的右子节点、左子树的右子节点与右子树的左子节点加入队列
            queue.offer(left.left);
            queue.offer(right.right);
            queue.offer(left.right);
            queue.offer(right.left);
        }
        
        return true;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试对称二叉树:
        //     1
        //    / \
        //   2   2
        //  / \ / \
        // 3  4 4  3
        TreeNode symmetricRoot = new TreeNode(1);
        symmetricRoot.left = new TreeNode(2);
        symmetricRoot.right = new TreeNode(2);
        symmetricRoot.left.left = new TreeNode(3);
        symmetricRoot.left.right = new TreeNode(4);
        symmetricRoot.right.left = new TreeNode(4);
        symmetricRoot.right.right = new TreeNode(3);
        
        System.out.println("测试用例1 - 对称二叉树:");
        System.out.println("递归方法: " + isSymmetric1(symmetricRoot));
        System.out.println("迭代方法: " + isSymmetric2(symmetricRoot));
        
        // 构建测试非对称二叉树:
        //     1
        //    / \
        //   2   2
        //    \   \
        //     3   3
        TreeNode asymmetricRoot = new TreeNode(1);
        asymmetricRoot.left = new TreeNode(2);
        asymmetricRoot.right = new TreeNode(2);
        asymmetricRoot.left.right = new TreeNode(3);
        asymmetricRoot.right.right = new TreeNode(3);
        
        System.out.println("\n测试用例2 - 非对称二叉树:");
        System.out.println("递归方法: " + isSymmetric1(asymmetricRoot));
        System.out.println("迭代方法: " + isSymmetric2(asymmetricRoot));
        
        // 测试空树
        TreeNode emptyRoot = null;
        System.out.println("\n测试用例3 - 空树:");
        System.out.println("递归方法: " + isSymmetric1(emptyRoot));
        System.out.println("迭代方法: " + isSymmetric2(emptyRoot));
    }
}