package class079;

import java.util.*;

/**
 * 树形DP测试运行器
 * 用于验证所有算法的正确性和性能
 */
public class TEST_RUNNER {
    
    /**
     * 测试打家劫舍III算法
     */
    public static void testRobIII() {
        System.out.println("=== 测试打家劫舍III ===");
        
        // 测试用例1：简单二叉树
        Code13_TreeDPPractice.TreeNode root1 = new Code13_TreeDPPractice.TreeNode(3);
        root1.left = new Code13_TreeDPPractice.TreeNode(2);
        root1.right = new Code13_TreeDPPractice.TreeNode(3);
        root1.left.right = new Code13_TreeDPPractice.TreeNode(3);
        root1.right.right = new Code13_TreeDPPractice.TreeNode(1);
        
        Code13_TreeDPPractice solver = new Code13_TreeDPPractice();
        int result1 = solver.rob(root1);
        System.out.println("测试用例1结果: " + result1 + " (期望: 7)");
        
        // 测试用例2：复杂二叉树
        Code13_TreeDPPractice.TreeNode root2 = new Code13_TreeDPPractice.TreeNode(3);
        root2.left = new Code13_TreeDPPractice.TreeNode(4);
        root2.right = new Code13_TreeDPPractice.TreeNode(5);
        root2.left.left = new Code13_TreeDPPractice.TreeNode(1);
        root2.left.right = new Code13_TreeDPPractice.TreeNode(3);
        root2.right.right = new Code13_TreeDPPractice.TreeNode(1);
        
        int result2 = solver.rob(root2);
        System.out.println("测试用例2结果: " + result2 + " (期望: 9)");
        
        System.out.println();
    }
    
    /**
     * 测试二叉树直径算法
     */
    public static void testTreeDiameter() {
        System.out.println("=== 测试二叉树直径 ===");
        
        Code13_TreeDPPractice solver = new Code13_TreeDPPractice();
        
        // 测试用例1：简单二叉树
        Code13_TreeDPPractice.TreeNode root1 = new Code13_TreeDPPractice.TreeNode(1);
        root1.left = new Code13_TreeDPPractice.TreeNode(2);
        root1.right = new Code13_TreeDPPractice.TreeNode(3);
        root1.left.left = new Code13_TreeDPPractice.TreeNode(4);
        root1.left.right = new Code13_TreeDPPractice.TreeNode(5);
        
        int result1 = solver.diameterOfBinaryTree(root1);
        System.out.println("测试用例1结果: " + result1 + " (期望: 3)");
        
        // 测试用例2：单节点树
        Code13_TreeDPPractice.TreeNode root2 = new Code13_TreeDPPractice.TreeNode(1);
        int result2 = solver.diameterOfBinaryTree(root2);
        System.out.println("测试用例2结果: " + result2 + " (期望: 0)");
        
        System.out.println();
    }
    
    /**
     * 测试最长同值路径算法
     */
    public static void testLongestUnivaluePath() {
        System.out.println("=== 测试最长同值路径 ===");
        
        Code13_TreeDPPractice solver = new Code13_TreeDPPractice();
        
        // 测试用例1：简单二叉树
        Code13_TreeDPPractice.TreeNode root1 = new Code13_TreeDPPractice.TreeNode(5);
        root1.left = new Code13_TreeDPPractice.TreeNode(4);
        root1.right = new Code13_TreeDPPractice.TreeNode(5);
        root1.left.left = new Code13_TreeDPPractice.TreeNode(1);
        root1.left.right = new Code13_TreeDPPractice.TreeNode(1);
        root1.right.right = new Code13_TreeDPPractice.TreeNode(5);
        
        int result1 = solver.longestUnivaluePath(root1);
        System.out.println("测试用例1结果: " + result1 + " (期望: 2)");
        
        System.out.println();
    }
    
    /**
     * 测试最大匹配算法
     */
    public static void testMaximumMatching() {
        System.out.println("=== 测试树上最大匹配 ===");
        
        Code14_TreeDPComprehensive solver = new Code14_TreeDPComprehensive();
        
        // 测试用例1：简单树
        List<List<Integer>> graph1 = new ArrayList<>();
        for (int i = 0; i < 6; i++) graph1.add(new ArrayList<>());
        graph1.get(0).add(1); graph1.get(0).add(2);
        graph1.get(1).add(0); graph1.get(1).add(3); graph1.get(1).add(4);
        graph1.get(2).add(0); graph1.get(2).add(5);
        graph1.get(3).add(1); graph1.get(4).add(1); graph1.get(5).add(2);
        
        int result1 = solver.treeMaximumMatching(graph1);
        System.out.println("测试用例1结果: " + result1 + " (期望: 3)");
        
        System.out.println();
    }
    
    /**
     * 性能测试：大规模数据测试
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        Code13_TreeDPPractice solver = new Code13_TreeDPPractice();
        
        // 生成大规模测试数据
        int n = 10000;
        Code13_TreeDPPractice.TreeNode[] nodes = new Code13_TreeDPPractice.TreeNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Code13_TreeDPPractice.TreeNode(i % 100);
        }
        
        // 构建完全二叉树
        for (int i = 0; i < n; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < n) nodes[i].left = nodes[left];
            if (right < n) nodes[i].right = nodes[right];
        }
        
        long startTime = System.currentTimeMillis();
        int result = solver.rob(nodes[0]);
        long endTime = System.currentTimeMillis();
        
        System.out.println("大规模测试结果: " + result);
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
        System.out.println("数据规模: " + n + " 个节点");
        
        System.out.println();
    }
    
    /**
     * 边界条件测试
     */
    public static void boundaryTest() {
        System.out.println("=== 边界条件测试 ===");
        
        Code13_TreeDPPractice solver = new Code13_TreeDPPractice();
        
        // 测试空树
        int result1 = solver.rob(null);
        System.out.println("空树测试结果: " + result1 + " (期望: 0)");
        
        // 测试单节点树
        Code13_TreeDPPractice.TreeNode singleNode = new Code13_TreeDPPractice.TreeNode(10);
        int result2 = solver.rob(singleNode);
        System.out.println("单节点树测试结果: " + result2 + " (期望: 10)");
        
        // 测试只有左子树的树
        Code13_TreeDPPractice.TreeNode leftOnly = new Code13_TreeDPPractice.TreeNode(1);
        leftOnly.left = new Code13_TreeDPPractice.TreeNode(2);
        leftOnly.left.left = new Code13_TreeDPPractice.TreeNode(3);
        int result3 = solver.rob(leftOnly);
        System.out.println("只有左子树测试结果: " + result3 + " (期望: 4)");
        
        System.out.println();
    }
    
    /**
     * 运行所有测试
     */
    public static void runAllTests() {
        System.out.println("开始运行树形DP测试套件...\n");
        
        testRobIII();
        testTreeDiameter();
        testLongestUnivaluePath();
        testMaximumMatching();
        performanceTest();
        boundaryTest();
        
        System.out.println("所有测试完成！");
    }
    
    public static void main(String[] args) {
        runAllTests();
    }
}