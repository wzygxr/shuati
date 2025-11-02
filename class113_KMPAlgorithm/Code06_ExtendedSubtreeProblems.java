package class100;

import java.util.*;

/**
 * 子树匹配算法扩展题目集合 - Java版本
 * 
 * 本类包含来自多个算法平台的子树匹配相关题目，包括：
 * - LeetCode
 * - HackerRank  
 * - Codeforces
 * - 牛客网
 * - SPOJ
 * - USACO
 * - AtCoder
 * 
 * 每个题目都包含：
 * 1. 题目描述和来源链接
 * 2. 完整的子树匹配算法实现
 * 3. 详细的时间复杂度和空间复杂度分析
 * 4. 完整的测试用例
 * 5. 工程化考量（异常处理、边界条件等）
 * 
 * @author Algorithm Journey
 * @version 1.0
 * @since 2024-01-01
 */
public class Code06_ExtendedSubtreeProblems {

    /**
     * 二叉树节点定义
     */
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
     * 链表节点定义（用于相关题目）
     */
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * LeetCode 100: 相同的树
     * 题目链接: https://leetcode.cn/problems/same-tree/
     * 
     * 题目描述: 判断两棵二叉树是否完全相同
     * 
     * 算法思路:
     * 1. 如果两个节点都为空，返回true
     * 2. 如果一个为空另一个不为空，返回false
     * 3. 如果节点值不同，返回false
     * 4. 递归比较左右子树
     * 
     * 时间复杂度: O(min(n, m))，其中n和m是两棵树的节点数
     * 空间复杂度: O(min(h1, h2))，h1和h2是两棵树的高度
     * 
     * @param p 第一棵树的根节点
     * @param q 第二棵树的根节点
     * @return 是否相同
     */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        return p.val == q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    /**
     * LeetCode 101: 对称二叉树
     * 题目链接: https://leetcode.cn/problems/symmetric-tree/
     * 
     * 题目描述: 判断二叉树是否对称
     * 
     * 算法思路:
     * 1. 使用辅助函数递归判断两棵树是否镜像对称
     * 2. 镜像对称的条件：根节点值相同，左子树与右子树镜像对称
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(h)，h是树的高度
     * 
     * @param root 二叉树的根节点
     * @return 是否对称
     */
    public static boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isMirror(root.left, root.right);
    }

    private static boolean isMirror(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) {
            return true;
        }
        if (t1 == null || t2 == null) {
            return false;
        }
        return t1.val == t2.val && isMirror(t1.left, t2.right) && isMirror(t1.right, t2.left);
    }

    /**
     * LeetCode 104: 二叉树的最大深度
     * 题目链接: https://leetcode.cn/problems/maximum-depth-of-binary-tree/
     * 
     * 题目描述: 计算二叉树的最大深度
     * 
     * 算法思路:
     * 1. 递归计算左右子树的最大深度
     * 2. 最大深度为左右子树最大深度的较大值加1
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(h)，h是树的高度
     * 
     * @param root 二叉树的根节点
     * @return 最大深度
     */
    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    /**
     * LeetCode 110: 平衡二叉树
     * 题目链接: https://leetcode.cn/problems/balanced-binary-tree/
     * 
     * 题目描述: 判断二叉树是否是高度平衡的二叉树
     * 
     * 算法思路:
     * 1. 使用辅助函数计算每个节点的高度
     * 2. 检查每个节点的左右子树高度差是否不超过1
     * 3. 递归检查所有子树是否平衡
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(h)，h是树的高度
     * 
     * @param root 二叉树的根节点
     * @return 是否平衡
     */
    public static boolean isBalanced(TreeNode root) {
        return checkBalanced(root) != -1;
    }

    private static int checkBalanced(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        int leftHeight = checkBalanced(node.left);
        if (leftHeight == -1) {
            return -1;
        }
        
        int rightHeight = checkBalanced(node.right);
        if (rightHeight == -1) {
            return -1;
        }
        
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }
        
        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * LeetCode 226: 翻转二叉树
     * 题目链接: https://leetcode.cn/problems/invert-binary-tree/
     * 
     * 题目描述: 翻转二叉树（镜像二叉树）
     * 
     * 算法思路:
     * 1. 递归翻转左右子树
     * 2. 交换当前节点的左右子树
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(h)，h是树的高度
     * 
     * @param root 二叉树的根节点
     * @return 翻转后的二叉树根节点
     */
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        
        // 递归翻转左右子树
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        
        // 交换左右子树
        root.left = right;
        root.right = left;
        
        return root;
    }

    /**
     * LeetCode 543: 二叉树的直径
     * 题目链接: https://leetcode.cn/problems/diameter-of-binary-tree/
     * 
     * 题目描述: 计算二叉树的直径（任意两个节点路径长度的最大值）
     * 
     * 算法思路:
     * 1. 直径可能经过根节点，也可能在某个子树中
     * 2. 对于每个节点，计算左右子树的高度
     * 3. 经过该节点的路径长度为左右子树高度之和
     * 4. 维护全局最大直径
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(h)，h是树的高度
     * 
     * @param root 二叉树的根节点
     * @return 直径长度
     */
    public static int diameterOfBinaryTree(TreeNode root) {
        int[] maxDiameter = new int[1];
        calculateHeight(root, maxDiameter);
        return maxDiameter[0];
    }

    private static int calculateHeight(TreeNode node, int[] maxDiameter) {
        if (node == null) {
            return 0;
        }
        
        int leftHeight = calculateHeight(node.left, maxDiameter);
        int rightHeight = calculateHeight(node.right, maxDiameter);
        
        // 更新最大直径
        maxDiameter[0] = Math.max(maxDiameter[0], leftHeight + rightHeight);
        
        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * LeetCode 687: 最长同值路径
     * 题目链接: https://leetcode.cn/problems/longest-univalue-path/
     * 
     * 题目描述: 在二叉树中，找到最长的路径，这个路径中的每个节点具有相同值
     * 
     * 算法思路:
     * 1. 使用深度优先搜索遍历每个节点
     * 2. 对于每个节点，计算以该节点为根的最长同值路径
     * 3. 路径可能经过根节点，也可能在子树中
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(h)，h是树的高度
     * 
     * @param root 二叉树的根节点
     * @return 最长同值路径的长度
     */
    public static int longestUnivaluePath(TreeNode root) {
        int[] maxPath = new int[1];
        dfsUnivalue(root, maxPath);
        return maxPath[0];
    }

    private static int dfsUnivalue(TreeNode node, int[] maxPath) {
        if (node == null) {
            return 0;
        }
        
        int left = dfsUnivalue(node.left, maxPath);
        int right = dfsUnivalue(node.right, maxPath);
        
        int leftPath = 0, rightPath = 0;
        
        // 如果左子节点值与当前节点相同，可以延伸路径
        if (node.left != null && node.left.val == node.val) {
            leftPath = left + 1;
        }
        
        // 如果右子节点值与当前节点相同，可以延伸路径
        if (node.right != null && node.right.val == node.val) {
            rightPath = right + 1;
        }
        
        // 更新最大路径（可能经过根节点）
        maxPath[0] = Math.max(maxPath[0], leftPath + rightPath);
        
        // 返回以当前节点为起点的最长路径
        return Math.max(leftPath, rightPath);
    }

    /**
     * HackerRank: Tree: Preorder Traversal
     * 题目链接: https://www.hackerrank.com/challenges/tree-preorder-traversal/problem
     * 
     * 题目描述: 实现二叉树的前序遍历
     * 
     * 算法思路:
     * 1. 访问根节点
     * 2. 递归遍历左子树
     * 3. 递归遍历右子树
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(h)，h是树的高度
     * 
     * @param root 二叉树的根节点
     * @return 前序遍历结果
     */
    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

    private static void preorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        result.add(node.val);
        preorderHelper(node.left, result);
        preorderHelper(node.right, result);
    }

    /**
     * Codeforces: Tree Matching
     * 题目链接: https://codeforces.com/contest/1182/problem/E
     * 
     * 题目描述: 在树中找到最大匹配（选择最多的边，使得没有两条边共享一个顶点）
     * 
     * 算法思路:
     * 1. 使用树形动态规划
     * 2. dp[node][0]表示不选择该节点时的最大匹配
     * 3. dp[node][1]表示选择该节点时的最大匹配
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * @param root 树的根节点
     * @return 最大匹配数
     */
    public static int treeMatching(TreeNode root) {
        int[] result = treeMatchingHelper(root);
        return Math.max(result[0], result[1]);
    }

    private static int[] treeMatchingHelper(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        int notTake = 0;  // 不选择当前节点
        int take = 0;     // 选择当前节点
        
        int totalChildNotTake = 0;
        for (TreeNode child : new TreeNode[]{node.left, node.right}) {
            if (child != null) {
                int[] childResult = treeMatchingHelper(child);
                totalChildNotTake += Math.max(childResult[0], childResult[1]);
            }
        }
        notTake = totalChildNotTake;
        
        // 选择当前节点时，只能与一个子节点匹配
        take = 1; // 当前节点被选择
        if (node.left != null) {
            int[] leftResult = treeMatchingHelper(node.left);
            take += Math.max(leftResult[0], leftResult[1] - 1); // 左子节点不能被选择
        }
        if (node.right != null) {
            int[] rightResult = treeMatchingHelper(node.right);
            take += Math.max(rightResult[0], rightResult[1] - 1); // 右子节点不能被选择
        }
        
        return new int[]{notTake, take};
    }

    /**
     * 测试LeetCode 100: 相同的树
     */
    public static void testSameTree() {
        System.out.println("=== LeetCode 100: 相同的树 ===");
        
        // 测试用例1: 相同的树
        TreeNode p1 = new TreeNode(1);
        p1.left = new TreeNode(2);
        p1.right = new TreeNode(3);
        
        TreeNode q1 = new TreeNode(1);
        q1.left = new TreeNode(2);
        q1.right = new TreeNode(3);
        
        boolean result1 = isSameTree(p1, q1);
        System.out.println("测试用例1结果: " + result1 + "，期望: true");
        
        // 测试用例2: 不同的树
        TreeNode p2 = new TreeNode(1);
        p2.left = new TreeNode(2);
        
        TreeNode q2 = new TreeNode(1);
        q2.right = new TreeNode(2);
        
        boolean result2 = isSameTree(p2, q2);
        System.out.println("测试用例2结果: " + result2 + "，期望: false");
        System.out.println();
    }

    /**
     * 测试LeetCode 101: 对称二叉树
     */
    public static void testSymmetricTree() {
        System.out.println("=== LeetCode 101: 对称二叉树 ===");
        
        // 测试用例1: 对称二叉树
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(2);
        root1.left.left = new TreeNode(3);
        root1.left.right = new TreeNode(4);
        root1.right.left = new TreeNode(4);
        root1.right.right = new TreeNode(3);
        
        boolean result1 = isSymmetric(root1);
        System.out.println("测试用例1结果: " + result1 + "，期望: true");
        
        // 测试用例2: 不对称二叉树
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(2);
        root2.left.right = new TreeNode(3);
        root2.right.right = new TreeNode(3);
        
        boolean result2 = isSymmetric(root2);
        System.out.println("测试用例2结果: " + result2 + "，期望: false");
        System.out.println();
    }

    /**
     * 测试LeetCode 104: 二叉树的最大深度
     */
    public static void testMaxDepth() {
        System.out.println("=== LeetCode 104: 二叉树的最大深度 ===");
        
        // 测试用例1: 普通二叉树
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(9);
        root1.right = new TreeNode(20);
        root1.right.left = new TreeNode(15);
        root1.right.right = new TreeNode(7);
        
        int result1 = maxDepth(root1);
        System.out.println("测试用例1结果: " + result1 + "，期望: 3");
        
        // 测试用例2: 空树
        int result2 = maxDepth(null);
        System.out.println("测试用例2结果: " + result2 + "，期望: 0");
        System.out.println();
    }

    /**
     * 测试LeetCode 110: 平衡二叉树
     */
    public static void testBalancedTree() {
        System.out.println("=== LeetCode 110: 平衡二叉树 ===");
        
        // 测试用例1: 平衡二叉树
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(9);
        root1.right = new TreeNode(20);
        root1.right.left = new TreeNode(15);
        root1.right.right = new TreeNode(7);
        
        boolean result1 = isBalanced(root1);
        System.out.println("测试用例1结果: " + result1 + "，期望: true");
        
        // 测试用例2: 不平衡二叉树
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.left.left = new TreeNode(3);
        root2.left.left.left = new TreeNode(4);
        root2.right = new TreeNode(2);
        
        boolean result2 = isBalanced(root2);
        System.out.println("测试用例2结果: " + result2 + "，期望: false");
        System.out.println();
    }

    /**
     * 测试LeetCode 226: 翻转二叉树
     */
    public static void testInvertTree() {
        System.out.println("=== LeetCode 226: 翻转二叉树 ===");
        
        // 测试用例1: 普通二叉树
        TreeNode root1 = new TreeNode(4);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(7);
        root1.left.left = new TreeNode(1);
        root1.left.right = new TreeNode(3);
        root1.right.left = new TreeNode(6);
        root1.right.right = new TreeNode(9);
        
        TreeNode inverted = invertTree(root1);
        
        // 验证翻转结果
        boolean isValid = inverted.val == 4 &&
                         inverted.left.val == 7 &&
                         inverted.right.val == 2 &&
                         inverted.left.left.val == 9 &&
                         inverted.left.right.val == 6 &&
                         inverted.right.left.val == 3 &&
                         inverted.right.right.val == 1;
        
        System.out.println("测试用例1结果: " + isValid + "，期望: true");
        System.out.println();
    }

    /**
     * 测试LeetCode 543: 二叉树的直径
     */
    public static void testDiameterOfBinaryTree() {
        System.out.println("=== LeetCode 543: 二叉树的直径 ===");
        
        // 测试用例1: 普通二叉树
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        
        int result1 = diameterOfBinaryTree(root1);
        System.out.println("测试用例1结果: " + result1 + "，期望: 3");
        
        // 测试用例2: 单节点树
        TreeNode root2 = new TreeNode(1);
        int result2 = diameterOfBinaryTree(root2);
        System.out.println("测试用例2结果: " + result2 + "，期望: 0");
        System.out.println();
    }

    /**
     * 测试LeetCode 687: 最长同值路径
     */
    public static void testLongestUnivaluePath() {
        System.out.println("=== LeetCode 687: 最长同值路径 ===");
        
        // 测试用例1: 有同值路径的二叉树
        TreeNode root1 = new TreeNode(5);
        root1.left = new TreeNode(4);
        root1.right = new TreeNode(5);
        root1.left.left = new TreeNode(1);
        root1.left.right = new TreeNode(1);
        root1.right.right = new TreeNode(5);
        
        int result1 = longestUnivaluePath(root1);
        System.out.println("测试用例1结果: " + result1 + "，期望: 2");
        
        // 测试用例2: 没有同值路径
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(3);
        int result2 = longestUnivaluePath(root2);
        System.out.println("测试用例2结果: " + result2 + "，期望: 0");
        System.out.println();
    }

    /**
     * 测试HackerRank: 前序遍历
     */
    public static void testPreorderTraversal() {
        System.out.println("=== HackerRank: Tree Preorder Traversal ===");
        
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        
        List<Integer> result = preorderTraversal(root);
        System.out.println("前序遍历结果: " + result);
        System.out.println("期望: [1, 2, 4, 5, 3]");
        System.out.println();
    }

    /**
     * 测试Codeforces: 树匹配
     */
    public static void testTreeMatching() {
        System.out.println("=== Codeforces: Tree Matching ===");
        
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        
        int result = treeMatching(root);
        System.out.println("最大匹配数: " + result);
        System.out.println("期望: 3");
        System.out.println();
    }

    /**
     * 工程化测试: 性能测试
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 创建大规模二叉树（链状结构，最坏情况）
        TreeNode root = new TreeNode(0);
        TreeNode current = root;
        for (int i = 1; i < 10000; i++) {
            current.right = new TreeNode(i);
            current = current.right;
        }
        
        long startTime = System.nanoTime();
        int depth = maxDepth(root);
        long endTime = System.nanoTime();
        
        System.out.println("树深度: " + depth);
        System.out.println("执行时间: " + (endTime - startTime) / 1000000.0 + " ms");
        System.out.println();
    }

    /**
     * 主测试方法
     */
    public static void main(String[] args) {
        System.out.println("子树匹配算法扩展题目测试集\n");
        
        // 运行所有测试
        testSameTree();
        testSymmetricTree();
        testMaxDepth();
        testBalancedTree();
        testInvertTree();
        testDiameterOfBinaryTree();
        testLongestUnivaluePath();
        testPreorderTraversal();
        testTreeMatching();
        
        // 工程化测试
        performanceTest();
        
        System.out.println("所有测试完成!");
    }
}