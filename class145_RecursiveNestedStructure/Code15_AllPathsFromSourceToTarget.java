package class039;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 797. All Paths From Source to Target
 * 所有可能的路径
 * 题目来源：https://leetcode.cn/problems/all-paths-from-source-to-target/
 * 
 * 问题描述：
 * 给你一个有 n 个节点的 有向无环图（DAG），请你找出所有从节点 0 到节点 n-1 的路径并输出（不要求按特定顺序）。
 * 二维数组的第 i 个数组中的元素表示从节点 i 可以直接到达的所有节点，空就是没有可以直接到达的节点。
 * 
 * 解题思路：
 * 1. 由于图是有向无环的（DAG），我们可以使用深度优先搜索（DFS）来遍历所有可能的路径
 * 2. 递归地探索每个节点的邻居，并记录路径
 * 3. 当到达目标节点（n-1）时，将当前路径添加到结果中
 * 
 * 时间复杂度：O(2^N * N)，其中N是节点数量，最坏情况下每个节点都可以选择是否加入路径，且路径长度最多为N
 * 空间复杂度：O(N)，递归调用栈的深度最多为N，同时存储路径的空间也是O(N)
 */
public class Code15_AllPathsFromSourceToTarget {
    
    /**
     * 查找从源节点到目标节点的所有路径
     * @param graph 图的邻接表表示
     * @return 所有可能的路径列表
     */
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();
        currentPath.add(0); // 起始节点是0
        
        // 从节点0开始深度优先搜索
        dfs(graph, 0, graph.length - 1, currentPath, result);
        
        return result;
    }
    
    /**
     * 深度优先搜索辅助方法
     * @param graph 图的邻接表表示
     * @param current 当前节点
     * @param target 目标节点（n-1）
     * @param currentPath 当前路径
     * @param result 结果列表，存储所有路径
     */
    private void dfs(int[][] graph, int current, int target, 
                     List<Integer> currentPath, List<List<Integer>> result) {
        // 基础情况：到达目标节点
        if (current == target) {
            // 将当前路径的副本添加到结果中
            result.add(new ArrayList<>(currentPath));
            return;
        }
        
        // 遍历当前节点的所有邻居
        for (int neighbor : graph[current]) {
            // 将邻居节点添加到当前路径
            currentPath.add(neighbor);
            // 递归探索邻居节点
            dfs(graph, neighbor, target, currentPath, result);
            // 回溯：移除最后添加的节点
            currentPath.remove(currentPath.size() - 1);
        }
    }
    
    /**
     * 迭代版本的深度优先搜索实现
     * @param graph 图的邻接表表示
     * @return 所有可能的路径列表
     */
    public List<List<Integer>> allPathsSourceTargetIterative(int[][] graph) {
        List<List<Integer>> result = new ArrayList<>();
        int target = graph.length - 1;
        
        // 使用栈来模拟递归调用
        // 栈中每个元素是一个包含当前节点和当前路径的对象
        java.util.Stack<NodeWithPath> stack = new java.util.Stack<>();
        List<Integer> initialPath = new ArrayList<>();
        initialPath.add(0);
        stack.push(new NodeWithPath(0, initialPath));
        
        // 迭代DFS
        while (!stack.isEmpty()) {
            NodeWithPath current = stack.pop();
            
            // 如果到达目标节点，将路径添加到结果
            if (current.node == target) {
                result.add(current.path);
                continue;
            }
            
            // 将当前节点的所有邻居加入栈中
            for (int neighbor : graph[current.node]) {
                List<Integer> newPath = new ArrayList<>(current.path);
                newPath.add(neighbor);
                stack.push(new NodeWithPath(neighbor, newPath));
            }
        }
        
        return result;
    }
    
    /**
     * 辅助类：用于迭代DFS中存储节点和对应的路径
     */
    private static class NodeWithPath {
        int node;          // 当前节点
        List<Integer> path; // 到达当前节点的路径
        
        NodeWithPath(int node, List<Integer> path) {
            this.node = node;
            this.path = path;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code15_AllPathsFromSourceToTarget solution = new Code15_AllPathsFromSourceToTarget();
        
        // 测试用例1
        int[][] graph1 = {{1, 2}, {3}, {3}, {}};
        System.out.println("递归DFS结果:");
        List<List<Integer>> result1 = solution.allPathsSourceTarget(graph1);
        printPaths(result1);
        
        System.out.println("\n迭代DFS结果:");
        List<List<Integer>> result1Iterative = solution.allPathsSourceTargetIterative(graph1);
        printPaths(result1Iterative);
        
        // 测试用例2
        int[][] graph2 = {{4, 3, 1}, {3, 2, 4}, {3}, {4}, {}};
        System.out.println("\n递归DFS结果:");
        List<List<Integer>> result2 = solution.allPathsSourceTarget(graph2);
        printPaths(result2);
        
        System.out.println("\n迭代DFS结果:");
        List<List<Integer>> result2Iterative = solution.allPathsSourceTargetIterative(graph2);
        printPaths(result2Iterative);
    }
    
    /**
     * 打印路径列表的辅助方法
     */
    private static void printPaths(List<List<Integer>> paths) {
        for (List<Integer> path : paths) {
            System.out.print("[");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i));
                if (i < path.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
    }
    
    /**
     * 性能分析：
     * - 时间复杂度：O(2^N * N)，其中N是节点数量
     *   最坏情况下，每个节点都可以选择是否加入路径，且路径长度最多为N
     *   例如，在完全二叉树形状的DAG中，路径数量可能达到2^(N-1)级别
     *   每个路径需要O(N)时间来复制
     * 
     * - 空间复杂度：O(N)，递归调用栈的深度最多为N，同时存储路径的空间也是O(N)
     *   注意：最终结果占用的空间不计入算法的空间复杂度分析
     * 
     * 工程化考量：
     * 1. 异常处理：在实际应用中，应该检查输入图是否为null，节点数量是否合法
     * 2. 对于大型图，可以考虑使用更高效的数据结构来存储路径，避免频繁复制
     * 3. 可以添加并行处理来加速搜索，但需要注意线程安全问题
     * 4. 当图中存在环时，这个算法会陷入死循环，因此需要确保图是DAG或添加访问标记
     */
}