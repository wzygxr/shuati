package class056;

import java.util.*;

/**
 * LeetCode 399. 除法求值
 * 链接: https://leetcode.cn/problems/evaluate-division/
 * 难度: 中等
 * 
 * 题目描述:
 * 给你一个变量对数组 equations 和一个实数值数组 values 作为已知条件，其中 equations[i] = [Ai, Bi] 和 values[i] 共同表示等式 Ai / Bi = values[i]。
 * 每个 Ai 或 Bi 是一个表示单个变量的字符串。
 * 另有一些以数组 queries 表示的问题，其中 queries[j] = [Cj, Dj] 表示第 j 个问题，请你根据已知条件找出 Cj / Dj = ? 的结果作为答案。
 * 如果存在某个无法确定的答案，则用 -1.0 替代。
 * 
 * 注意：输入总是有效的，且不存在循环或冲突的结果。
 * 
 * 示例 1:
 * 输入: equations = [["a","b"],["b","c"]], values = [2.0,3.0], queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
 * 输出: [6.00000,0.50000,-1.00000,1.00000,-1.00000]
 * 解释:
 * 条件: a / b = 2.0, b / c = 3.0
 * 问题: a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?
 * 结果: [6.0, 0.5, -1.0, 1.0, -1.0]
 * 
 * 示例 2:
 * 输入: equations = [["a","b"],["b","c"],["bc","cd"]], values = [1.5,2.5,5.0], queries = [["a","c"],["c","b"],["bc","cd"],["cd","bc"]]
 * 输出: [3.75000,0.40000,5.00000,0.20000]
 * 
 * 示例 3:
 * 输入: equations = [["a","b"]], values = [0.5], queries = [["a","b"],["b","a"],["a","c"],["x","y"]]
 * 输出: [0.50000,2.00000,-1.00000,-1.00000]
 * 
 * 约束条件:
 * 1 <= equations.length <= 20
 * equations[i].length == 2
 * 1 <= Ai.length, Bi.length <= 5
 * values.length == equations.length
 * 0.0 < values[i] <= 20.0
 * 1 <= queries.length <= 20
 * queries[i].length == 2
 * 1 <= Cj.length, Dj.length <= 5
 * Ai, Bi, Cj, Dj 由小写英文字母与数字组成
 */
public class Code17_LeetCode399 {
    
    /**
     * 方法1: 使用带权并查集解决除法求值问题
     * 时间复杂度: O((E + Q) * α(N))，其中E是equations的长度，Q是queries的长度，N是不同变量的数量
     * 空间复杂度: O(N)
     * 
     * 解题思路:
     * 1. 使用带权并查集维护变量之间的倍数关系
     * 2. 权重表示当前节点值 / 父节点值
     * 3. 对于查询，如果两个变量在同一个集合中，结果等于权重比值
     */
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // 创建并初始化带权并查集
        WeightedUnionFind uf = new WeightedUnionFind();
        
        // 构建并查集
        for (int i = 0; i < equations.size(); i++) {
            String var1 = equations.get(i).get(0);
            String var2 = equations.get(i).get(1);
            uf.union(var1, var2, values[i]);
        }
        
        // 处理查询
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String var1 = queries.get(i).get(0);
            String var2 = queries.get(i).get(1);
            
            // 如果变量不存在于并查集中，结果为-1.0
            if (!uf.contains(var1) || !uf.contains(var2)) {
                results[i] = -1.0;
                continue;
            }
            
            // 查找两个变量的根节点和权重
            Pair root1Info = uf.find(var1);
            Pair root2Info = uf.find(var2);
            
            // 如果根节点不同，说明无法确定结果
            if (!root1Info.root.equals(root2Info.root)) {
                results[i] = -1.0;
            } else {
                // 结果等于 var1到根的权重除以 var2到根的权重
                results[i] = root1Info.weight / root2Info.weight;
            }
        }
        
        return results;
    }
    
    /**
     * 辅助类，存储根节点和权重信息
     */
    static class Pair {
        String root;
        double weight;
        
        Pair(String root, double weight) {
            this.root = root;
            this.weight = weight;
        }
    }
    
    /**
     * 带权并查集实现
     * 用于处理除法关系，维护变量之间的倍数关系
     */
    static class WeightedUnionFind {
        // 存储父节点
        private Map<String, String> parent;
        // 存储到父节点的权重（当前节点值 / 父节点值）
        private Map<String, Double> weight;
        
        public WeightedUnionFind() {
            parent = new HashMap<>();
            weight = new HashMap<>();
        }
        
        /**
         * 检查变量是否存在于并查集中
         */
        public boolean contains(String x) {
            return parent.containsKey(x);
        }
        
        /**
         * 初始化变量
         */
        private void ensureExists(String x) {
            if (!contains(x)) {
                parent.put(x, x);
                weight.put(x, 1.0);
            }
        }
        
        /**
         * 查找操作，返回根节点和权重（x的值 / 根节点的值）
         */
        public Pair find(String x) {
            ensureExists(x);
            
            if (!parent.get(x).equals(x)) {
                // 递归查找父节点
                Pair rootInfo = find(parent.get(x));
                String root = rootInfo.root;
                double rootWeight = rootInfo.weight;
                
                // 路径压缩：将x直接指向根节点
                parent.put(x, root);
                // 更新权重：x到根的权重 = x到父的权重 * 父到根的权重
                weight.put(x, weight.get(x) * rootWeight);
            }
            
            return new Pair(parent.get(x), weight.get(x));
        }
        
        /**
         * 合并操作，表示 x / y = value
         */
        public void union(String x, String y, double value) {
            ensureExists(x);
            ensureExists(y);
            
            Pair xRootInfo = find(x);
            Pair yRootInfo = find(y);
            
            String xRoot = xRootInfo.root;
            String yRoot = yRootInfo.root;
            double xWeight = xRootInfo.weight;
            double yWeight = yRootInfo.weight;
            
            if (!xRoot.equals(yRoot)) {
                // 将x的根节点连接到y的根节点
                parent.put(xRoot, yRoot);
                // 更新权重：xRoot / yRoot = (y / yRoot) * (x / y) / (x / xRoot) = yWeight * value / xWeight
                weight.put(xRoot, yWeight * value / xWeight);
            }
        }
    }
    
    /**
     * 方法2: 使用图搜索（DFS/BFS）解决除法求值问题
     * 时间复杂度: O(E + Q * (E + V))，其中E是边数，V是顶点数，Q是查询数
     * 空间复杂度: O(E + V)
     * 
     * 解题思路:
     * 1. 构建有向图，边权重表示除法关系
     * 2. 对于每个查询，使用DFS/BFS在图中搜索路径
     * 3. 路径上边权重的乘积就是结果
     */
    public double[] calcEquationDFS(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // 构建图
        Map<String, Map<String, Double>> graph = buildGraph(equations, values);
        
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String start = queries.get(i).get(0);
            String end = queries.get(i).get(1);
            results[i] = dfs(graph, start, end, new HashSet<>());
        }
        
        return results;
    }
    
    /**
     * 构建图
     */
    private Map<String, Map<String, Double>> buildGraph(List<List<String>> equations, double[] values) {
        Map<String, Map<String, Double>> graph = new HashMap<>();
        
        for (int i = 0; i < equations.size(); i++) {
            String u = equations.get(i).get(0);
            String v = equations.get(i).get(1);
            double value = values[i];
            
            // 添加正向边 u -> v
            graph.computeIfAbsent(u, k -> new HashMap<>()).put(v, value);
            // 添加反向边 v -> u
            graph.computeIfAbsent(v, k -> new HashMap<>()).put(u, 1.0 / value);
        }
        
        return graph;
    }
    
    /**
     * DFS搜索路径
     */
    private double dfs(Map<String, Map<String, Double>> graph, String start, String end, Set<String> visited) {
        // 如果节点不存在
        if (!graph.containsKey(start) || !graph.containsKey(end)) {
            return -1.0;
        }
        
        // 如果找到目标节点
        if (start.equals(end)) {
            return 1.0;
        }
        
        visited.add(start);
        
        // 遍历邻居节点
        for (Map.Entry<String, Double> neighbor : graph.get(start).entrySet()) {
            String next = neighbor.getKey();
            double weight = neighbor.getValue();
            
            if (!visited.contains(next)) {
                double result = dfs(graph, next, end, visited);
                if (result != -1.0) {
                    return weight * result;
                }
            }
        }
        
        visited.remove(start);
        return -1.0;
    }
    
    /**
     * 方法3: 使用Floyd-Warshall算法（动态规划）解决除法求值问题
     * 时间复杂度: O(V^3 + Q)，其中V是顶点数
     * 空间复杂度: O(V^2)
     * 
     * 适用场景: 当查询数量很大时，预处理后可以快速回答查询
     */
    public double[] calcEquationFloydWarshall(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // 收集所有变量
        Set<String> variables = new HashSet<>();
        for (List<String> equation : equations) {
            variables.add(equation.get(0));
            variables.add(equation.get(1));
        }
        
        // 创建变量到索引的映射
        List<String> varList = new ArrayList<>(variables);
        int n = varList.size();
        Map<String, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            indexMap.put(varList.get(i), i);
        }
        
        // 初始化距离矩阵
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], -1.0);
            dist[i][i] = 1.0;  // 自己除自己等于1
        }
        
        // 填充已知关系
        for (int i = 0; i < equations.size(); i++) {
            String u = equations.get(i).get(0);
            String v = equations.get(i).get(1);
            int uIndex = indexMap.get(u);
            int vIndex = indexMap.get(v);
            dist[uIndex][vIndex] = values[i];
            dist[vIndex][uIndex] = 1.0 / values[i];
        }
        
        // Floyd-Warshall算法
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != -1.0 && dist[k][j] != -1.0) {
                        dist[i][j] = dist[i][k] * dist[k][j];
                    }
                }
            }
        }
        
        // 处理查询
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String u = queries.get(i).get(0);
            String v = queries.get(i).get(1);
            
            if (!indexMap.containsKey(u) || !indexMap.containsKey(v)) {
                results[i] = -1.0;
            } else {
                int uIndex = indexMap.get(u);
                int vIndex = indexMap.get(v);
                results[i] = dist[uIndex][vIndex];
            }
        }
        
        return results;
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code17_LeetCode399 solution = new Code17_LeetCode399();
        
        // 测试用例1
        List<List<String>> equations1 = Arrays.asList(
            Arrays.asList("a", "b"),
            Arrays.asList("b", "c")
        );
        double[] values1 = {2.0, 3.0};
        List<List<String>> queries1 = Arrays.asList(
            Arrays.asList("a", "c"),
            Arrays.asList("b", "a"),
            Arrays.asList("a", "e"),
            Arrays.asList("a", "a"),
            Arrays.asList("x", "x")
        );
        
        double[] result1 = solution.calcEquation(equations1, values1, queries1);
        System.out.println("测试用例1 - 并查集解法: " + Arrays.toString(result1));
        // 预期: [6.0, 0.5, -1.0, 1.0, -1.0]
        
        double[] result1DFS = solution.calcEquationDFS(equations1, values1, queries1);
        System.out.println("测试用例1 - DFS解法: " + Arrays.toString(result1DFS));
        // 预期: [6.0, 0.5, -1.0, 1.0, -1.0]
        
        double[] result1FW = solution.calcEquationFloydWarshall(equations1, values1, queries1);
        System.out.println("测试用例1 - Floyd-Warshall解法: " + Arrays.toString(result1FW));
        // 预期: [6.0, 0.5, -1.0, 1.0, -1.0]
        
        // 测试用例2
        List<List<String>> equations2 = Arrays.asList(
            Arrays.asList("a", "b"),
            Arrays.asList("b", "c"),
            Arrays.asList("bc", "cd")
        );
        double[] values2 = {1.5, 2.5, 5.0};
        List<List<String>> queries2 = Arrays.asList(
            Arrays.asList("a", "c"),
            Arrays.asList("c", "b"),
            Arrays.asList("bc", "cd"),
            Arrays.asList("cd", "bc")
        );
        
        double[] result2 = solution.calcEquation(equations2, values2, queries2);
        System.out.println("测试用例2 - 并查集解法: " + Arrays.toString(result2));
        // 预期: [3.75, 0.4, 5.0, 0.2]
        
        // 测试用例3
        List<List<String>> equations3 = Arrays.asList(
            Arrays.asList("a", "b")
        );
        double[] values3 = {0.5};
        List<List<String>> queries3 = Arrays.asList(
            Arrays.asList("a", "b"),
            Arrays.asList("b", "a"),
            Arrays.asList("a", "c"),
            Arrays.asList("x", "y")
        );
        
        double[] result3 = solution.calcEquation(equations3, values3, queries3);
        System.out.println("测试用例3 - 并查集解法: " + Arrays.toString(result3));
        // 预期: [0.5, 2.0, -1.0, -1.0]
    }
}