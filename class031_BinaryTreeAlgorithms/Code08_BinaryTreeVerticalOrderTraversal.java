package class037;

import java.util.*;

// LeetCode 314. Binary Tree Vertical Order Traversal
// 题目链接: https://leetcode.cn/problems/binary-tree-vertical-order-traversal/
// 题目描述: 给你一个二叉树的根结点，返回其节点按垂直方向（从上到下，逐列）遍历的结果。
// 如果两个节点在同一行和列，那么顺序则为从左到右。
//
// 解题思路:
// 1. 使用BFS层序遍历，同时记录每个节点的列号
// 2. 根节点列号为0，左子节点列号减1，右子节点列号加1
// 3. 使用HashMap记录每列的节点值列表
// 4. 使用minCol和maxCol记录列号的范围
// 5. 按列号从小到大收集结果
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: O(n) - 队列和HashMap最多存储n个节点
// 是否为最优解: 是，这是垂直遍历的标准解法

public class Code08_BinaryTreeVerticalOrderTraversal {

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

    // 提交如下的方法
    public List<List<Integer>> verticalOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        // 使用HashMap记录每列的节点值列表
        Map<Integer, List<Integer>> columnMap = new HashMap<>();
        
        // 使用队列进行BFS，存储节点和对应的列号
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        Queue<Integer> columnQueue = new LinkedList<>();
        
        nodeQueue.offer(root);
        columnQueue.offer(0);
        
        // 记录列号的范围
        int minCol = 0, maxCol = 0;
        
        while (!nodeQueue.isEmpty()) {
            TreeNode node = nodeQueue.poll();
            int col = columnQueue.poll();
            
            // 将节点值添加到对应列的列表中
            columnMap.computeIfAbsent(col, k -> new ArrayList<>()).add(node.val);
            
            // 更新列号范围
            minCol = Math.min(minCol, col);
            maxCol = Math.max(maxCol, col);
            
            // 处理左右子节点
            if (node.left != null) {
                nodeQueue.offer(node.left);
                columnQueue.offer(col - 1);
            }
            if (node.right != null) {
                nodeQueue.offer(node.right);
                columnQueue.offer(col + 1);
            }
        }
        
        // 按列号从小到大收集结果
        for (int i = minCol; i <= maxCol; i++) {
            result.add(columnMap.get(i));
        }
        
        return result;
    }

    // 测试用例
    public static void main(String[] args) {
        Code08_BinaryTreeVerticalOrderTraversal solution = new Code08_BinaryTreeVerticalOrderTraversal();

        // 测试用例1:
        //       3
        //      / \
        //     9  20
        //       /  \
        //      15   7
        // 垂直遍历结果: [[9], [3, 15], [20], [7]]
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(9);
        root1.right = new TreeNode(20);
        root1.right.left = new TreeNode(15);
        root1.right.right = new TreeNode(7);
        
        List<List<Integer>> result1 = solution.verticalOrder(root1);
        System.out.println("测试用例1结果: " + result1); // 应该输出[[9], [3, 15], [20], [7]]

        // 测试用例2:
        //       3
        //      / \
        //     9   8
        //    / \   \
        //   4   0   1
        //      / \   \
        //     5   2   7
        // 垂直遍历结果: [[4], [9, 5], [3, 0, 1], [8, 2], [7]]
        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(9);
        root2.right = new TreeNode(8);
        root2.left.left = new TreeNode(4);
        root2.left.right = new TreeNode(0);
        root2.right.right = new TreeNode(1);
        root2.left.right.left = new TreeNode(5);
        root2.left.right.right = new TreeNode(2);
        root2.right.right.right = new TreeNode(7);
        
        List<List<Integer>> result2 = solution.verticalOrder(root2);
        System.out.println("测试用例2结果: " + result2); // 应该输出[[4], [9, 5], [3, 0, 1], [8, 2], [7]]

        // 测试用例3: 空树
        TreeNode root3 = null;
        List<List<Integer>> result3 = solution.verticalOrder(root3);
        System.out.println("测试用例3结果: " + result3); // 应该输出[]
    }
}