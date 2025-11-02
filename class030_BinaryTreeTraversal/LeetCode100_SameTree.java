package class036;

import java.util.*;

// LeetCode 100. 相同的树
// 题目链接: https://leetcode.cn/problems/same-tree/
// 题目大意: 给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验这两棵树是否相同。
// 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。

public class LeetCode100_SameTree {
    
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
     * 方法1: 递归实现判断两棵树是否相同
     * 思路:
     * 1. 如果两个节点都为空，返回true
     * 2. 如果其中一个节点为空，另一个不为空，返回false
     * 3. 如果两个节点的值不相等，返回false
     * 4. 递归判断左子树和右子树是否相同
     * 5. 返回左右子树都相同的判断结果
     * 时间复杂度: O(min(m,n)) - m和n分别是两棵树的节点数
     * 空间复杂度: O(min(h1,h2)) - h1和h2分别是两棵树的高度，递归调用栈的深度
     */
    public static boolean isSameTree1(TreeNode p, TreeNode q) {
        // 如果两个节点都为空，返回true
        if (p == null && q == null) {
            return true;
        }
        
        // 如果其中一个节点为空，另一个不为空，返回false
        if (p == null || q == null) {
            return false;
        }
        
        // 如果两个节点的值不相等，返回false
        if (p.val != q.val) {
            return false;
        }
        
        // 递归判断左子树和右子树是否相同
        return isSameTree1(p.left, q.left) && isSameTree1(p.right, q.right);
    }
    
    /**
     * 方法2: 迭代实现判断两棵树是否相同
     * 思路:
     * 1. 使用队列存储待比较的节点对
     * 2. 每次从队列中取出一对节点进行比较
     * 3. 如果节点对都为空，继续下一对
     * 4. 如果其中一个为空或值不相等，返回false
     * 5. 将左右子节点对加入队列继续比较
     * 时间复杂度: O(min(m,n)) - m和n分别是两棵树的节点数
     * 空间复杂度: O(min(w1,w2)) - w1和w2分别是两棵树的最大宽度
     */
    public static boolean isSameTree2(TreeNode p, TreeNode q) {
        // 使用队列存储待比较的节点对
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(p);
        queue.offer(q);
        
        while (!queue.isEmpty()) {
            // 取出一对节点
            TreeNode node1 = queue.poll();
            TreeNode node2 = queue.poll();
            
            // 如果两个节点都为空，继续下一对
            if (node1 == null && node2 == null) {
                continue;
            }
            
            // 如果其中一个节点为空或值不相等，返回false
            if (node1 == null || node2 == null || node1.val != node2.val) {
                return false;
            }
            
            // 将左右子节点对加入队列继续比较
            queue.offer(node1.left);
            queue.offer(node2.left);
            queue.offer(node1.right);
            queue.offer(node2.right);
        }
        
        return true;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试二叉树1:
        //     1
        //    / \
        //   2   3
        TreeNode p1 = new TreeNode(1);
        p1.left = new TreeNode(2);
        p1.right = new TreeNode(3);
        
        // 构建测试二叉树2:
        //     1
        //    / \
        //   2   3
        TreeNode q1 = new TreeNode(1);
        q1.left = new TreeNode(2);
        q1.right = new TreeNode(3);
        
        System.out.println("测试用例1 - 相同的树:");
        System.out.println("递归方法: " + isSameTree1(p1, q1));
        System.out.println("迭代方法: " + isSameTree2(p1, q1));
        
        // 构建测试二叉树3:
        //     1
        //    /
        //   2
        TreeNode p2 = new TreeNode(1);
        p2.left = new TreeNode(2);
        
        // 构建测试二叉树4:
        //     1
        //      \
        //       2
        TreeNode q2 = new TreeNode(1);
        q2.right = new TreeNode(2);
        
        System.out.println("\n测试用例2 - 不同的树:");
        System.out.println("递归方法: " + isSameTree1(p2, q2));
        System.out.println("迭代方法: " + isSameTree2(p2, q2));
        
        // 测试空树
        TreeNode empty1 = null;
        TreeNode empty2 = null;
        System.out.println("\n测试用例3 - 空树:");
        System.out.println("递归方法: " + isSameTree1(empty1, empty2));
        System.out.println("迭代方法: " + isSameTree2(empty1, empty2));
    }
}