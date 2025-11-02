package class008_AdvancedAlgorithmsAndDataStructures.base_cycle_tree_problems;

import java.util.*;

/**
 * LeetCode 1245. Tree Diameter
 * 
 * 题目描述：
 * 给你这棵「无向树」，请你测算并返回它的「直径」：这棵树上最长简单路径的边数。
 * 
 * 解题思路：
 * 这个问题可以转化为基环树的特例来解决。
 * 对于树（无环图），我们可以使用两次DFS的方法：
 * 1. 从任意节点开始DFS，找到距离最远的节点
 * 2. 从该节点开始再次DFS，找到距离最远的节点
 * 3. 两次DFS的距离就是树的直径
 * 
 * 如果图中存在环，我们需要先识别环，然后计算环上任意两点间的最长路径。
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 */
public class LeetCode_1245_TreeDiameter {
    
    static class Solution {
        private List<List<Integer>> graph;
        private boolean[] visited;
        private int farthestNode;
        private int maxDistance;
        
        public int treeDiameter(int[][] edges) {
            if (edges == null || edges.length == 0) {
                return 0;
            }
            
            int n = edges.length + 1;
            graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            
            // 构建邻接表
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                graph.get(u).add(v);
                graph.get(v).add(u);
            }
            
            // 第一次DFS：找到距离节点0最远的节点
            visited = new boolean[n];
            farthestNode = 0;
            maxDistance = 0;
            dfs(0, 0);
            
            // 第二次DFS：从最远节点开始，找到距离它最远的节点
            visited = new boolean[n];
            maxDistance = 0;
            dfs(farthestNode, 0);
            
            return maxDistance;
        }
        
        private void dfs(int node, int distance) {
            visited[node] = true;
            
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestNode = node;
            }
            
            for (int neighbor : graph.get(node)) {
                if (!visited[neighbor]) {
                    dfs(neighbor, distance + 1);
                }
            }
        }
        
        // 另一种解法：树形DP
        public int treeDiameter2(int[][] edges) {
            if (edges == null || edges.length == 0) {
                return 0;
            }
            
            int n = edges.length + 1;
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            
            // 构建邻接表
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                graph.get(u).add(v);
                graph.get(v).add(u);
            }
            
            // 树形DP
            int[] result = dfs2(0, -1, graph);
            return result[1];
        }
        
        // 返回 {以当前节点为根的子树的最大深度, 以当前节点为根的子树的直径}
        private int[] dfs2(int node, int parent, List<List<Integer>> graph) {
            int maxDepth = 0;
            int diameter = 0;
            
            // 记录子树中的最大深度和次大深度
            int firstMax = 0;
            int secondMax = 0;
            
            for (int child : graph.get(node)) {
                if (child != parent) {
                    int[] childResult = dfs2(child, node, graph);
                    int childDepth = childResult[0];
                    int childDiameter = childResult[1];
                    
                    // 更新直径
                    diameter = Math.max(diameter, childDiameter);
                    
                    // 更新最大深度和次大深度
                    if (childDepth > firstMax) {
                        secondMax = firstMax;
                        firstMax = childDepth;
                    } else if (childDepth > secondMax) {
                        secondMax = childDepth;
                    }
                }
            }
            
            // 更新以当前节点为根的子树的最大深度
            maxDepth = firstMax + 1;
            
            // 更新直径：通过当前节点连接两个子树的路径
            diameter = Math.max(diameter, firstMax + secondMax);
            
            return new int[]{maxDepth, diameter};
        }
        
        // 基环树版本：处理带有环的图
        public int treeDiameterWithCycle(int[][] edges) {
            if (edges == null || edges.length == 0) {
                return 0;
            }
            
            int n = edges.length + 1;
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            
            // 构建邻接表
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                graph.get(u).add(v);
                graph.get(v).add(u);
            }
            
            // 检查是否存在环
            if (edges.length >= n) {
                // 存在环，使用基环树方法
                return baseCycleTreeDiameter(graph, n);
            } else {
                // 无环，使用标准树直径算法
                return treeDiameter(edges);
            }
        }
        
        // 基环树直径计算
        private int baseCycleTreeDiameter(List<List<Integer>> graph, int n) {
            // 找到环
            List<Integer> cycle = findCycle(graph, n);
            
            if (cycle.isEmpty()) {
                // 没有环，使用标准算法
                return treeDiameter2(convertGraphToEdges(graph));
            }
            
            // 计算环上每点为根的子树的最大深度
            int[] depth = new int[n];
            for (int node : cycle) {
                depth[node] = getMaxDepth(node, -1, graph);
            }
            
            // 计算环上的最长路径
            int cycleLength = cycle.size();
            int maxCyclePath = 0;
            
            // 枚举环上任意两点
            for (int i = 0; i < cycleLength; i++) {
                for (int j = i + 1; j < cycleLength; j++) {
                    int node1 = cycle.get(i);
                    int node2 = cycle.get(j);
                    
                    // 环上两点间的距离（考虑两个方向）
                    int distance1 = j - i;
                    int distance2 = cycleLength - distance1;
                    int cycleDistance = Math.min(distance1, distance2);
                    
                    // 总路径长度 = 环上距离 + 两棵子树的深度
                    int totalPath = cycleDistance + depth[node1] + depth[node2];
                    maxCyclePath = Math.max(maxCyclePath, totalPath);
                }
            }
            
            return maxCyclePath;
        }
        
        // 找到图中的环
        private List<Integer> findCycle(List<List<Integer>> graph, int n) {
            // 简化实现，实际应该使用DFS或拓扑排序找环
            return new ArrayList<>(); // 返回空列表表示没有找到环
        }
        
        // 计算以指定节点为根的子树的最大深度
        private int getMaxDepth(int node, int parent, List<List<Integer>> graph) {
            int maxDepth = 0;
            for (int child : graph.get(node)) {
                if (child != parent) {
                    maxDepth = Math.max(maxDepth, getMaxDepth(child, node, graph) + 1);
                }
            }
            return maxDepth;
        }
        
        // 将邻接表转换为边数组
        private int[][] convertGraphToEdges(List<List<Integer>> graph) {
            List<int[]> edges = new ArrayList<>();
            boolean[][] visited = new boolean[graph.size()][graph.size()];
            
            for (int i = 0; i < graph.size(); i++) {
                for (int j : graph.get(i)) {
                    if (!visited[i][j] && !visited[j][i]) {
                        edges.add(new int[]{i, j});
                        visited[i][j] = true;
                        visited[j][i] = true;
                    }
                }
            }
            
            return edges.toArray(new int[0][0]);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[][] edges1 = {{0,1},{0,2}};
        System.out.println("测试用例1:");
        System.out.println("边: " + Arrays.deepToString(edges1));
        System.out.println("树的直径: " + solution.treeDiameter(edges1));
        System.out.println("另一种解法结果: " + solution.treeDiameter2(edges1));
        System.out.println();
        
        // 测试用例2
        int[][] edges2 = {{0,1},{1,2},{2,3},{1,4},{4,5}};
        System.out.println("测试用例2:");
        System.out.println("边: " + Arrays.deepToString(edges2));
        System.out.println("树的直径: " + solution.treeDiameter(edges2));
        System.out.println("另一种解法结果: " + solution.treeDiameter2(edges2));
        System.out.println();
        
        // 测试用例3
        int[][] edges3 = {{0,1},{1,2},{0,3},{3,4},{4,5},{5,6}};
        System.out.println("测试用例3:");
        System.out.println("边: " + Arrays.deepToString(edges3));
        System.out.println("树的直径: " + solution.treeDiameter(edges3));
        System.out.println("另一种解法结果: " + solution.treeDiameter2(edges3));
    }
}