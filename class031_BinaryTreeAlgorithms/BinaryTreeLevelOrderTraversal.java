package class037;

import java.util.*;

// LeetCode 102. Binary Tree Level Order Traversal
// 题目链接: https://leetcode.cn/problems/binary-tree-level-order-traversal/
// 题目描述: 给你二叉树的根节点 root ，返回其节点值的层序遍历结果。
//           （即逐层地，从左到右访问所有节点）
//
// 解题思路:
// 1. 使用广度优先搜索(BFS)进行层序遍历
// 2. 使用队列存储每一层的节点
// 3. 对于每一层，先记录当前层的节点数，然后处理这些节点
// 4. 将每个节点的子节点加入队列，用于下一层的遍历
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点都需要访问一次
// 空间复杂度: O(w) - w为树的最大宽度，队列中最多存储一层的节点
// 是否为最优解: 是，这是层序遍历的标准解法

// 补充题目:
// 1. LeetCode 107. Binary Tree Level Order Traversal II - 自底向上的层序遍历
// 2. LeetCode 103. Binary Tree Zigzag Level Order Traversal - 锯齿形层序遍历
// 3. LintCode 69. Binary Tree Level Order Traversal - 与LeetCode 102相同

// 层序遍历的核心思想和技巧:
// 1. 利用队列实现广度优先搜索
// 2. 通过记录每层节点数量来控制层级处理
// 3. 可以通过栈、双端队列等数据结构实现变种遍历
// 4. 适用于需要按层处理节点的各种问题场景
// 5. 时间复杂度始终为O(n)，空间复杂度为O(w)

public class BinaryTreeLevelOrderTraversal {

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

    // 提交如下的方法 - 层序遍历主方法
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件：空树
        if (root == null) {
            return result;
        }
        
        // 使用队列进行BFS
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            // 记录当前层的节点数
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
            
            // 处理当前层的所有节点
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.val);
                
                // 将子节点加入队列，用于下一层遍历
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            
            // 将当前层的结果添加到最终结果中
            result.add(currentLevel);
        }
        
        return result;
    }

    // 补充题目1: LeetCode 107. Binary Tree Level Order Traversal II
    // 题目链接: https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/
    // 题目描述: 给定二叉树，返回其节点值自底向上的层序遍历结果。
    //           （即按从叶子节点所在层到根节点所在层，逐层从左到右遍历）
    //
    // 解题思路:
    // 1. 执行常规的层序遍历
    // 2. 将结果反转即可
    //
    // 时间复杂度: O(n) - 每个节点都需要访问一次，反转操作需要O(h)时间，h为树高
    // 空间复杂度: O(w) - 队列中最多存储一层的节点，w为树的最大宽度
    // 是否为最优解: 是，这是最直接且高效的解法
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件：空树
        if (root == null) {
            return result;
        }
        
        // 使用队列进行BFS
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.val);
                
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            
            result.add(currentLevel);
        }
        
        // 反转结果列表，实现自底向上
        Collections.reverse(result);
        return result;
    }
    
    // 补充题目2: LeetCode 103. Binary Tree Zigzag Level Order Traversal
    // 题目链接: https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/
    // 题目描述: 给定二叉树，返回其节点值的锯齿形层序遍历。
    //           （即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）
    //
    // 解题思路:
    // 1. 使用BFS进行层序遍历
    // 2. 使用一个标志位来决定当前层的遍历方向
    // 3. 对于偶数层（从0开始计数），正常从左到右添加
    // 4. 对于奇数层，从右到左添加（使用双端队列高效实现）
    //
    // 时间复杂度: O(n) - 每个节点只被访问一次
    // 空间复杂度: O(w) - 队列中最多存储一层的节点
    // 是否为最优解: 是，使用双端队列可以在O(1)时间内添加元素，避免了每层结束后的反转操作
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件：空树
        if (root == null) {
            return result;
        }
        
        // 使用队列进行BFS
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        // 标志位：true表示从左到右，false表示从右到左
        boolean leftToRight = true;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            // 使用双端队列来存储当前层的节点值
            Deque<Integer> currentLevel = new LinkedList<>();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                
                // 根据方向决定节点值添加到双端队列的哪一端
                if (leftToRight) {
                    currentLevel.offerLast(node.val);
                } else {
                    currentLevel.offerFirst(node.val);
                }
                
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            
            // 将当前层的结果转换为ArrayList并添加到结果中
            result.add(new ArrayList<>(currentLevel));
            // 切换遍历方向
            leftToRight = !leftToRight;
        }
        
        return result;
    }
    
    // 辅助方法：打印二叉树（用于调试）
    private static void printTree(TreeNode root) {
        if (root == null) {
            System.out.print("null ");
            return;
        }
        System.out.print(root.val + " ");
        printTree(root.left);
        printTree(root.right);
    }
    
    // 整合的测试方法，同时测试所有功能
    public static void main(String[] args) {
        BinaryTreeLevelOrderTraversal solution = new BinaryTreeLevelOrderTraversal();
        
        // 构造测试树
        //       3
        //      / \
        //     9  20
        //       /  \
        //      15   7
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        
        // 测试LeetCode 102: 标准层序遍历
        List<List<Integer>> resultNormal = solution.levelOrder(root);
        System.out.println("LeetCode 102 测试结果: " + resultNormal);
        // 预期输出: [[3], [9, 20], [15, 7]]
        
        // 测试LeetCode 107: 自底向上的层序遍历
        List<List<Integer>> resultBottom = solution.levelOrderBottom(root);
        System.out.println("LeetCode 107 测试结果: " + resultBottom);
        // 预期输出: [[15, 7], [9, 20], [3]]
        
        // 测试LeetCode 103: 锯齿形层序遍历
        List<List<Integer>> resultZigzag = solution.zigzagLevelOrder(root);
        System.out.println("LeetCode 103 测试结果: " + resultZigzag);
        // 预期输出: [[3], [20, 9], [15, 7]]
        
        // 边界测试用例: 空树
        System.out.println("\n边界测试用例: 空树");
        TreeNode emptyTree = null;
        System.out.println("标准层序遍历: " + solution.levelOrder(emptyTree));
        System.out.println("自底向上遍历: " + solution.levelOrderBottom(emptyTree));
        System.out.println("锯齿形遍历: " + solution.zigzagLevelOrder(emptyTree));
        
        // 边界测试用例: 单节点树
        System.out.println("\n边界测试用例: 单节点树");
        TreeNode singleNodeTree = new TreeNode(1);
        System.out.println("标准层序遍历: " + solution.levelOrder(singleNodeTree));
        System.out.println("自底向上遍历: " + solution.levelOrderBottom(singleNodeTree));
        System.out.println("锯齿形遍历: " + solution.zigzagLevelOrder(singleNodeTree));
    }
}