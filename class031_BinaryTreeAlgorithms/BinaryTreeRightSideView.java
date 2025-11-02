package class037;

import java.util.*;

// LeetCode 199. Binary Tree Right Side View
// 题目链接: https://leetcode.cn/problems/binary-tree-right-side-view/
// 题目描述: 给定一个二叉树的根节点 root，想象自己站在它的右侧，
// 按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
//
// 解题思路:
// 1. BFS层序遍历：记录每层的最后一个节点
// 2. DFS递归遍历：先访问右子树，记录每层第一个访问到的节点
// 3. 使用队列进行BFS是更直观的解法
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: O(w) - w为树的最大宽度，队列中最多存储一层的节点
// 是否为最优解: 是，这是解决右视图问题的标准方法

// 补充题目:
// 1. LeetCode 102. Binary Tree Level Order Traversal - 二叉树的层序遍历
// 2. LeetCode 103. Binary Tree Zigzag Level Order Traversal - 锯齿形层序遍历
// 3. 牛客NC14. 二叉树的之字形层序遍历 - 与LeetCode 103相同

public class BinaryTreeRightSideView {

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

    // 方法1: BFS层序遍历
    // 核心思想: 使用队列进行层序遍历，记录每层的最后一个节点
    public List<Integer> rightSideViewBFS(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                
                // 如果是当前层的最后一个节点，加入结果
                if (i == levelSize - 1) {
                    result.add(node.val);
                }
                
                // 将子节点加入队列
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        
        return result;
    }

    // 方法2: DFS递归遍历
    // 核心思想: 先访问右子树，再访问左子树，记录每层第一个访问到的节点
    public List<Integer> rightSideViewDFS(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        dfs(root, result, 0);
        return result;
    }
    
    private void dfs(TreeNode node, List<Integer> result, int depth) {
        if (node == null) {
            return;
        }
        
        // 如果当前深度还没有记录节点，说明这是该层第一个访问到的节点（因为是先右后左）
        if (depth == result.size()) {
            result.add(node.val);
        }
        
        // 先递归右子树，再递归左子树
        dfs(node.right, result, depth + 1);
        dfs(node.left, result, depth + 1);
    }

    // 提交如下的方法（使用BFS版本，更直观）
    public List<Integer> rightSideView(TreeNode root) {
        return rightSideViewBFS(root);
    }

    // 测试用例
    public static void main(String[] args) {
        BinaryTreeRightSideView solution = new BinaryTreeRightSideView();

        // 测试用例1:
        //       1
        //      / \
        //     2   3
        //      \   \
        //       5   4
        // 右视图: [1, 3, 4]
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.right = new TreeNode(5);
        root1.right.right = new TreeNode(4);
        
        List<Integer> result1 = solution.rightSideView(root1);
        System.out.println("测试用例1结果: " + result1); // 应该输出[1, 3, 4]

        // 测试用例2:
        //       1
        //      / 
        //     2
        //    /
        //   3
        // 右视图: [1, 2, 3]
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.left.left = new TreeNode(3);
        
        List<Integer> result2 = solution.rightSideView(root2);
        System.out.println("测试用例2结果: " + result2); // 应该输出[1, 2, 3]

        // 测试用例3: 空树
        TreeNode root3 = null;
        List<Integer> result3 = solution.rightSideView(root3);
        System.out.println("测试用例3结果: " + result3); // 应该输出[]

        // 测试用例4: 完全二叉树
        //       1
        //      / \
        //     2   3
        //    / \ / \
        //   4  5 6  7
        // 右视图: [1, 3, 7]
        TreeNode root4 = new TreeNode(1);
        root4.left = new TreeNode(2);
        root4.right = new TreeNode(3);
        root4.left.left = new TreeNode(4);
        root4.left.right = new TreeNode(5);
        root4.right.left = new TreeNode(6);
        root4.right.right = new TreeNode(7);
        
        List<Integer> result4 = solution.rightSideView(root4);
        System.out.println("测试用例4结果: " + result4); // 应该输出[1, 3, 7]

        // 补充题目测试: 二叉树的左视图
        System.out.println("\n=== 补充题目测试: 二叉树的左视图 ===");
        List<Integer> leftView = leftSideView(root4);
        System.out.println("左视图结果: " + leftView); // 应该输出[1, 2, 4]
    }
    
    // 补充功能: 二叉树的左视图
    public static List<Integer> leftSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                
                // 如果是当前层的第一个节点，加入结果
                if (i == 0) {
                    result.add(node.val);
                }
                
                // 将子节点加入队列（先左后右）
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        
        return result;
    }
}