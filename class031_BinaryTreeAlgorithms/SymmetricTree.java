package class037;

import java.util.LinkedList;
import java.util.Queue;

// LeetCode 101. Symmetric Tree
// 题目链接: https://leetcode.cn/problems/symmetric-tree/
// 题目描述: 给定一个二叉树，检查它是否是镜像对称的。
// 例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
//
// 解题思路:
// 1. 递归方法：同时遍历左右子树，检查是否镜像对称
// 2. 迭代方法：使用队列进行层序遍历，检查对称性
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: 
//   - 递归: O(h) - h为树的高度，递归调用栈的深度
//   - 迭代: O(w) - w为树的最大宽度，队列中最多存储一层的节点
// 是否为最优解: 是，这是检查对称二叉树的标准方法

// 补充题目:
// 1. LeetCode 100. Same Tree - 判断两棵树是否相同
// 2. LeetCode 226. Invert Binary Tree - 翻转二叉树
// 3. 牛客NC16. 对称的二叉树 - 与LeetCode 101相同

public class SymmetricTree {

    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // 方法1: 递归解法
    // 核心思想: 同时遍历左右子树，检查左子树的左节点是否等于右子树的右节点，
    //          左子树的右节点是否等于右子树的左节点
    public boolean isSymmetricRecursive(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isMirror(root.left, root.right);
    }
    
    private boolean isMirror(TreeNode left, TreeNode right) {
        // 两个节点都为空，对称
        if (left == null && right == null) {
            return true;
        }
        // 一个为空一个不为空，不对称
        if (left == null || right == null) {
            return false;
        }
        // 节点值不相等，不对称
        if (left.val != right.val) {
            return false;
        }
        // 递归检查左子树的左节点和右子树的右节点，
        // 以及左子树的右节点和右子树的左节点
        return isMirror(left.left, right.right) && isMirror(left.right, right.left);
    }

    // 方法2: 迭代解法（BFS）
    // 核心思想: 使用队列进行层序遍历，每次入队两个节点进行比较
    public boolean isSymmetricIterative(TreeNode root) {
        if (root == null) {
            return true;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root.left);
        queue.offer(root.right);
        
        while (!queue.isEmpty()) {
            TreeNode left = queue.poll();
            TreeNode right = queue.poll();
            
            // 两个节点都为空，继续检查
            if (left == null && right == null) {
                continue;
            }
            // 一个为空一个不为空，不对称
            if (left == null || right == null) {
                return false;
            }
            // 节点值不相等，不对称
            if (left.val != right.val) {
                return false;
            }
            
            // 按对称顺序入队子节点
            queue.offer(left.left);
            queue.offer(right.right);
            queue.offer(left.right);
            queue.offer(right.left);
        }
        
        return true;
    }

    // 提交如下的方法（使用递归版本）
    public boolean isSymmetric(TreeNode root) {
        return isSymmetricRecursive(root);
    }

    // 测试用例
    public static void main(String[] args) {
        SymmetricTree solution = new SymmetricTree();

        // 测试用例1: 对称二叉树
        //       1
        //      / \
        //     2   2
        //    / \ / \
        //   3  4 4  3
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(2);
        root1.left.left = new TreeNode(3);
        root1.left.right = new TreeNode(4);
        root1.right.left = new TreeNode(4);
        root1.right.right = new TreeNode(3);
        
        boolean result1 = solution.isSymmetric(root1);
        System.out.println("测试用例1结果: " + result1); // 应该输出true

        // 测试用例2: 不对称二叉树
        //       1
        //      / \
        //     2   2
        //      \   \
        //       3   3
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(2);
        root2.left.right = new TreeNode(3);
        root2.right.right = new TreeNode(3);
        
        boolean result2 = solution.isSymmetric(root2);
        System.out.println("测试用例2结果: " + result2); // 应该输出false

        // 测试用例3: 空树
        TreeNode root3 = null;
        boolean result3 = solution.isSymmetric(root3);
        System.out.println("测试用例3结果: " + result3); // 应该输出true

        // 测试用例4: 单节点树
        TreeNode root4 = new TreeNode(1);
        boolean result4 = solution.isSymmetric(root4);
        System.out.println("测试用例4结果: " + result4); // 应该输出true

        // 补充题目测试: LeetCode 100 - 相同的树
        System.out.println("\n=== 补充题目测试: 相同的树 ===");
        // 构造两棵相同的树
        TreeNode tree1 = new TreeNode(1);
        tree1.left = new TreeNode(2);
        tree1.right = new TreeNode(3);
        
        TreeNode tree2 = new TreeNode(1);
        tree2.left = new TreeNode(2);
        tree2.right = new TreeNode(3);
        
        boolean sameResult = isSameTree(tree1, tree2);
        System.out.println("相同的树测试: " + sameResult); // 应该输出true
    }
    
    // 补充题目: LeetCode 100. Same Tree
    // 题目链接: https://leetcode.cn/problems/same-tree/
    // 判断两棵树是否相同
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        // 两个节点都为空，相同
        if (p == null && q == null) {
            return true;
        }
        // 一个为空一个不为空，不同
        if (p == null || q == null) {
            return false;
        }
        // 节点值不相等，不同
        if (p.val != q.val) {
            return false;
        }
        // 递归检查左右子树
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}