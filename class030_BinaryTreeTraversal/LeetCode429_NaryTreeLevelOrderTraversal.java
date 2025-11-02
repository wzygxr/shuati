package class036;

import java.util.*;

// LeetCode 429. N 叉树的层序遍历
// 题目链接: https://leetcode.cn/problems/n-ary-tree-level-order-traversal/
// 题目大意: 给定一个 N 叉树，返回其节点值的层序遍历。（即从左到右，逐层遍历）
// 树的序列化输入是用层序遍历，每组子节点都由 null 值分隔

public class LeetCode429_NaryTreeLevelOrderTraversal {
    
    // N叉树节点定义
    static class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    };
    
    /**
     * 方法1: 使用BFS层序遍历
     * 时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    public static List<List<Integer>> levelOrder1(Node root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                level.add(node.val);
                
                // 将所有子节点加入队列
                if (node.children != null) {
                    for (Node child : node.children) {
                        if (child != null) {
                            queue.offer(child);
                        }
                    }
                }
            }
            
            result.add(level);
        }
        
        return result;
    }
    
    /**
     * 方法2: 使用DFS递归遍历
     * 时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
     * 空间复杂度: O(h) - h是树的高度，递归调用栈的深度
     */
    public static List<List<Integer>> levelOrder2(Node root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        dfs(root, 0, result);
        return result;
    }
    
    private static void dfs(Node node, int level, List<List<Integer>> result) {
        if (node == null) {
            return;
        }
        
        // 如果当前层级还没有对应的列表，创建一个新的
        if (result.size() <= level) {
            result.add(new ArrayList<>());
        }
        
        // 将当前节点值添加到对应层级的列表中
        result.get(level).add(node.val);
        
        // 递归处理所有子节点
        if (node.children != null) {
            for (Node child : node.children) {
                dfs(child, level + 1, result);
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试N叉树:
        //       1
        //    /  |  \
        //   3   2   4
        //  / \
        // 5   6
        Node root = new Node(1);
        List<Node> children1 = new ArrayList<>();
        Node node3 = new Node(3);
        Node node2 = new Node(2);
        Node node4 = new Node(4);
        children1.add(node3);
        children1.add(node2);
        children1.add(node4);
        root.children = children1;
        
        List<Node> children3 = new ArrayList<>();
        Node node5 = new Node(5);
        Node node6 = new Node(6);
        children3.add(node5);
        children3.add(node6);
        node3.children = children3;
        
        System.out.println("方法1结果: " + levelOrder1(root));
        System.out.println("方法2结果: " + levelOrder2(root));
    }
}