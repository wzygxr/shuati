package class037;

import java.util.*;

// LeetCode 1650. Lowest Common Ancestor of a Binary Tree III
// 题目链接: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/
// 题目描述: 给定二叉树中的两个节点p和q，返回它们的最低公共祖先(LCA)。
// 每个节点都有指向其父节点的引用。
//
// 解题思路:
// 1. 利用每个节点都有父指针的特点
// 2. 首先从节点p向上遍历到根节点，将路径上的所有节点存储在HashSet中
// 3. 然后从节点q向上遍历，第一个出现在HashSet中的节点就是LCA
//
// 时间复杂度: O(h) - h为树的高度，最坏情况下需要遍历从叶子节点到根节点的路径两次
// 空间复杂度: O(h) - HashSet最多存储从p到根节点的h个节点
// 是否为最优解: 是，这是利用父指针求LCA的标准解法

public class Code09_LowestCommonAncestorIII {
    
    // 二叉树节点定义（带父指针）
    static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node parent;
        
        public Node(int val) {
            this.val = val;
        }
    }
    
    /**
     * 寻找二叉树中两个节点的最低公共祖先
     * 
     * @param p 第一个节点
     * @param q 第二个节点
     * @return 最低公共祖先节点
     */
    public Node lowestCommonAncestor(Node p, Node q) {
        // 创建HashSet存储节点p的所有祖先节点（包括p本身）
        Set<Node> visitedNodes = new HashSet<>();
        
        // 从节点p向上遍历到根节点，将路径上的所有节点加入HashSet
        Node current = p;
        while (current != null) {
            visitedNodes.add(current);
            current = current.parent;
        }
        
        // 从节点q向上遍历到根节点，第一个出现在HashSet中的节点就是LCA
        current = q;
        while (current != null) {
            if (visitedNodes.contains(current)) {
                return current; // 找到LCA
            }
            current = current.parent;
        }
        
        return null; // 理论上不会执行到这里
    }
    
    // 测试用例
    public static void main(String[] args) {
        Code09_LowestCommonAncestorIII solution = new Code09_LowestCommonAncestorIII();
        
        // 构建测试用例:
        //       3
        //      / \
        //     5   1
        //    / \   \
        //   6   2   8
        //      / \
        //     7   4
        
        Node root = new Node(3);
        Node node5 = new Node(5);
        Node node1 = new Node(1);
        Node node6 = new Node(6);
        Node node2 = new Node(2);
        Node node8 = new Node(8);
        Node node7 = new Node(7);
        Node node4 = new Node(4);
        
        // 建立父子关系
        root.left = node5;
        root.right = node1;
        node5.parent = root;
        node1.parent = root;
        
        node5.left = node6;
        node5.right = node2;
        node6.parent = node5;
        node2.parent = node5;
        
        node1.right = node8;
        node8.parent = node1;
        
        node2.left = node7;
        node2.right = node4;
        node7.parent = node2;
        node4.parent = node2;
        
        // 测试用例1: p=7, q=4, LCA应该是2
        Node result1 = solution.lowestCommonAncestor(node7, node4);
        System.out.println("测试用例1结果: " + result1.val); // 应该输出2
        
        // 测试用例2: p=6, q=8, LCA应该是3
        Node result2 = solution.lowestCommonAncestor(node6, node8);
        System.out.println("测试用例2结果: " + result2.val); // 应该输出3
        
        // 测试用例3: p=5, q=1, LCA应该是3
        Node result3 = solution.lowestCommonAncestor(node5, node1);
        System.out.println("测试用例3结果: " + result3.val); // 应该输出3
    }
}