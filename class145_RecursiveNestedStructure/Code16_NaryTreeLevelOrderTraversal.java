package class039;

import java.util.*;

/**
 * LeetCode 429. N-ary Tree Level Order Traversal
 * N叉树的层序遍历
 * 题目来源：https://leetcode.cn/problems/n-ary-tree-level-order-traversal/
 * 
 * 问题描述：
 * 给定一个 N 叉树，返回其节点值的层序遍历。（即从左到右，逐层遍历）。
 * 树的序列化输入是用层序遍历，每组子节点都由 null 值分隔（参见示例）。
 * 
 * 解题思路：
 * 1. 递归方法：使用深度优先搜索，记录每个节点的层级，并将节点值添加到对应层级的列表中
 * 2. 迭代方法：使用队列进行广度优先搜索，逐层处理节点
 * 
 * 时间复杂度：O(N)，其中N是树中的节点数，每个节点只被访问一次
 * 空间复杂度：
 *   - 递归：O(H)，H是树的高度，递归调用栈的最大深度
 *   - 迭代：O(W)，W是树中最宽层的节点数，队列的最大大小
 */

// N叉树节点定义
class Node {
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

public class Code16_NaryTreeLevelOrderTraversal {
    
    /**
     * 递归实现层序遍历
     * @param root N叉树的根节点
     * @return 层序遍历的结果列表
     */
    public List<List<Integer>> levelOrderRecursive(Node root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        // 从第0层开始递归遍历
        dfs(root, 0, result);
        return result;
    }
    
    /**
     * 深度优先搜索辅助方法，按层收集节点值
     * @param node 当前节点
     * @param level 当前节点的层级
     * @param result 存储层序遍历结果的列表
     */
    private void dfs(Node node, int level, List<List<Integer>> result) {
        // 如果当前层级的列表还不存在，创建它
        if (level >= result.size()) {
            result.add(new ArrayList<>());
        }
        
        // 将当前节点的值添加到对应层级的列表中
        result.get(level).add(node.val);
        
        // 递归处理所有子节点，层级加1
        if (node.children != null) {
            for (Node child : node.children) {
                dfs(child, level + 1, result);
            }
        }
    }
    
    /**
     * 迭代实现层序遍历（使用队列）
     * @param root N叉树的根节点
     * @return 层序遍历的结果列表
     */
    public List<List<Integer>> levelOrderIterative(Node root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        // 使用队列进行广度优先搜索
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        
        // 逐层处理节点
        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // 当前层的节点数量
            List<Integer> currentLevel = new ArrayList<>();
            
            // 处理当前层的所有节点
            for (int i = 0; i < levelSize; i++) {
                Node currentNode = queue.poll();
                currentLevel.add(currentNode.val);
                
                // 将子节点加入队列，用于处理下一层
                if (currentNode.children != null) {
                    for (Node child : currentNode.children) {
                        queue.offer(child);
                    }
                }
            }
            
            // 将当前层的结果添加到最终结果中
            result.add(currentLevel);
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code16_NaryTreeLevelOrderTraversal solution = new Code16_NaryTreeLevelOrderTraversal();
        
        // 构建测试用例的N叉树
        // 示例：[1,null,3,2,4,null,5,6]
        Node root = new Node(1);
        List<Node> children1 = new ArrayList<>();
        children1.add(new Node(3));
        children1.add(new Node(2));
        children1.add(new Node(4));
        root.children = children1;
        
        List<Node> children3 = new ArrayList<>();
        children3.add(new Node(5));
        children3.add(new Node(6));
        root.children.get(0).children = children3;
        
        // 递归方法测试
        System.out.println("递归实现结果:");
        List<List<Integer>> result1 = solution.levelOrderRecursive(root);
        printResult(result1);
        
        // 迭代方法测试
        System.out.println("\n迭代实现结果:");
        List<List<Integer>> result2 = solution.levelOrderIterative(root);
        printResult(result2);
        
        // 空树测试
        System.out.println("\n空树测试:");
        List<List<Integer>> result3 = solution.levelOrderRecursive(null);
        printResult(result3);
        
        List<List<Integer>> result4 = solution.levelOrderIterative(null);
        printResult(result4);
    }
    
    /**
     * 打印结果的辅助方法
     */
    private static void printResult(List<List<Integer>> result) {
        System.out.print("[");
        for (int i = 0; i < result.size(); i++) {
            System.out.print("[");
            List<Integer> level = result.get(i);
            for (int j = 0; j < level.size(); j++) {
                System.out.print(level.get(j));
                if (j < level.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("]");
            if (i < result.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    /**
     * 性能分析：
     * - 时间复杂度：两种实现都是O(N)，其中N是树中的节点数，每个节点只被访问一次
     * 
     * - 空间复杂度：
     *   - 递归：O(H)，H是树的高度，递归调用栈的最大深度
     *     最坏情况下，树是一条链，空间复杂度为O(N)
     *   - 迭代：O(W)，W是树中最宽层的节点数，队列的最大大小
     *     最坏情况下，最后一层全是叶子节点，空间复杂度为O(N)
     * 
     * 两种实现方法的对比：
     * 1. 递归实现更简洁，但对于非常深的树可能导致栈溢出
     * 2. 迭代实现更稳健，不受递归深度限制，对于大型树更安全
     * 
     * 工程化考量：
     * 1. 异常处理：在实际应用中，应该检查输入树是否为null，以及树的结构是否合法
     * 2. 对于非常大的树，应该优先考虑迭代实现，避免栈溢出风险
     * 3. 可以添加并行处理来加速遍历，但需要注意线程安全问题
     * 4. 在内存受限的环境中，需要考虑数据结构的选择，避免不必要的内存开销
     */
}