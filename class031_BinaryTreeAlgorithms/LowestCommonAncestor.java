package class037;

// LeetCode 236. Lowest Common Ancestor of a Binary Tree
// 题目链接: https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
// 题目描述: 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
// 最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
// 满足 x 是 p、q 的祖先且 x 的深度尽可能大。"
//
// 解题思路:
// 1. 使用递归深度优先搜索(DFS)
// 2. 对于每个节点，递归检查其左右子树
// 3. 如果当前节点是p或q，则直接返回当前节点
// 4. 如果左右子树分别找到了p和q，则当前节点就是LCA
// 5. 如果只在一侧子树找到了p或q，则返回找到的节点
//
// 时间复杂度: O(n) - n为树中节点的数量，最坏情况下需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是寻找LCA的标准方法

public class LowestCommonAncestor {

    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    // 提交如下的方法
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 基本情况：空节点或找到p或q
        if (root == null || root == p || root == q) {
            return root;
        }
        
        // 递归在左右子树中查找p和q
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        // 如果左右子树都找到了，说明当前节点是LCA
        if (left != null && right != null) {
            return root;
        }
        
        // 如果只在一侧找到了，返回找到的节点
        return left != null ? left : right;
    }

    // 测试用例
    public static void main(String[] args) {
        LowestCommonAncestor solution = new LowestCommonAncestor();

        // 构造测试用例:
        //       3
        //      / \
        //     5   1
        //    / \ / \
        //   6  2 0  8
        //     / \
        //    7   4
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(5);
        root.right = new TreeNode(1);
        root.left.left = new TreeNode(6);
        root.left.right = new TreeNode(2);
        root.right.left = new TreeNode(0);
        root.right.right = new TreeNode(8);
        root.left.right.left = new TreeNode(7);
        root.left.right.right = new TreeNode(4);
        
        TreeNode p1 = root.left;  // 节点5
        TreeNode q1 = root.right; // 节点1
        TreeNode result1 = solution.lowestCommonAncestor(root, p1, q1);
        System.out.println("LCA(5, 1) = " + result1.val); // 应该输出3

        TreeNode p2 = root.left;        // 节点5
        TreeNode q2 = root.left.right.right; // 节点4
        TreeNode result2 = solution.lowestCommonAncestor(root, p2, q2);
        System.out.println("LCA(5, 4) = " + result2.val); // 应该输出5
    }
}