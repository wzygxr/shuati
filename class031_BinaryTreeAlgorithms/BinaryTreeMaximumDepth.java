package class037;

// LeetCode 104. Maximum Depth of Binary Tree
// 题目链接: https://leetcode.cn/problems/maximum-depth-of-binary-tree/
// 题目描述: 给定一个二叉树，找出其最大深度。
// 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
//
// 解题思路:
// 1. 递归方法：最大深度 = max(左子树深度, 右子树深度) + 1
// 2. BFS层序遍历：记录层数
// 3. DFS迭代遍历：使用栈模拟递归
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: 
//   - 递归: O(h) - h为树的高度，递归调用栈的深度
//   - BFS: O(w) - w为树的最大宽度
// 是否为最优解: 是，这是计算二叉树最大深度的标准方法

// 补充题目:
// 1. LeetCode 111. Minimum Depth of Binary Tree - 二叉树的最小深度
// 2. LeetCode 110. Balanced Binary Tree - 平衡二叉树
// 3. 牛客NC13. 二叉树的最大深度 - 与LeetCode 104相同

public class BinaryTreeMaximumDepth {

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
    // 核心思想: 最大深度 = max(左子树深度, 右子树深度) + 1
    public int maxDepthRecursive(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        int leftDepth = maxDepthRecursive(root.left);
        int rightDepth = maxDepthRecursive(root.right);
        
        return Math.max(leftDepth, rightDepth) + 1;
    }

    // 方法2: BFS层序遍历
    // 核心思想: 使用队列进行层序遍历，记录层数
    public int maxDepthBFS(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.offer(root);
        int depth = 0;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            depth++;
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        
        return depth;
    }

    // 方法3: DFS迭代遍历（使用栈）
    // 核心思想: 使用栈模拟递归，记录每个节点的深度
    public int maxDepthDFS(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        java.util.Stack<TreeNode> nodeStack = new java.util.Stack<>();
        java.util.Stack<Integer> depthStack = new java.util.Stack<>();
        nodeStack.push(root);
        depthStack.push(1);
        int maxDepth = 0;
        
        while (!nodeStack.isEmpty()) {
            TreeNode node = nodeStack.pop();
            int currentDepth = depthStack.pop();
            maxDepth = Math.max(maxDepth, currentDepth);
            
            if (node.right != null) {
                nodeStack.push(node.right);
                depthStack.push(currentDepth + 1);
            }
            if (node.left != null) {
                nodeStack.push(node.left);
                depthStack.push(currentDepth + 1);
            }
        }
        
        return maxDepth;
    }

    // 提交如下的方法（使用递归版本，最简洁）
    public int maxDepth(TreeNode root) {
        return maxDepthRecursive(root);
    }

    // 测试用例
    public static void main(String[] args) {
        BinaryTreeMaximumDepth solution = new BinaryTreeMaximumDepth();

        // 测试用例1:
        //       3
        //      / \
        //     9  20
        //       /  \
        //      15   7
        // 最大深度: 3
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(9);
        root1.right = new TreeNode(20);
        root1.right.left = new TreeNode(15);
        root1.right.right = new TreeNode(7);
        
        int result1 = solution.maxDepth(root1);
        System.out.println("测试用例1结果: " + result1); // 应该输出3

        // 测试用例2: 单节点树
        TreeNode root2 = new TreeNode(1);
        int result2 = solution.maxDepth(root2);
        System.out.println("测试用例2结果: " + result2); // 应该输出1

        // 测试用例3: 空树
        TreeNode root3 = null;
        int result3 = solution.maxDepth(root3);
        System.out.println("测试用例3结果: " + result3); // 应该输出0

        // 测试用例4: 退化为链表的树
        //       1
        //        \
        //         2
        //          \
        //           3
        // 最大深度: 3
        TreeNode root4 = new TreeNode(1);
        root4.right = new TreeNode(2);
        root4.right.right = new TreeNode(3);
        
        int result4 = solution.maxDepth(root4);
        System.out.println("测试用例4结果: " + result4); // 应该输出3

        // 补充题目测试: 二叉树的最小深度
        System.out.println("\n=== 补充题目测试: 二叉树的最小深度 ===");
        int minDepth = minDepth(root1);
        System.out.println("最小深度结果: " + minDepth); // 应该输出2
    }
    
    // 补充题目: LeetCode 111. Minimum Depth of Binary Tree
    // 题目链接: https://leetcode.cn/problems/minimum-depth-of-binary-tree/
    public static int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        // 如果是叶节点，深度为1
        if (root.left == null && root.right == null) {
            return 1;
        }
        
        int minDepth = Integer.MAX_VALUE;
        
        // 只有左子树或只有右子树的情况需要特殊处理
        if (root.left != null) {
            minDepth = Math.min(minDepth, minDepth(root.left));
        }
        if (root.right != null) {
            minDepth = Math.min(minDepth, minDepth(root.right));
        }
        
        return minDepth + 1;
    }
}