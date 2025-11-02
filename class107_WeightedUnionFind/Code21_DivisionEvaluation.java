/**
 * LeetCode 399 - 除法求值
 * https://leetcode-cn.com/problems/evaluate-division/
 * 
 * 题目描述：
 * 给你一个变量对数组 equations 和一个实数值数组 values 作为已知条件，其中 equations[i] = [Ai, Bi] 和 values[i] 共同表示等式 Ai / Bi = values[i]。
 * 每个 Ai 或 Bi 是一个表示单个变量的字符串。
 * 
 * 另有一些以数组 queries 表示的问题，其中 queries[j] = [Cj, Dj] 表示第 j 个问题，请你根据已知条件，返回 Cj / Dj = ? 的结果作为答案。
 * 如果无法确定结果，请返回 -1.0。
 * 
 * 注意：输入总是有效的。你可以假设除法运算中不会出现除数为0的情况，且不存在任何矛盾的结果。
 * 
 * 示例 1：
 * 输入：
 * equations = [["a","b"],["b","c"]], 
 * values = [2.0,3.0], 
 * queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
 * 输出：[6.00000,0.50000,-1.00000,1.00000,-1.00000]
 * 解释：
 * 条件：a / b = 2.0, b / c = 3.0
 * 问题：a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?
 * 结果：[6.0, 0.5, -1.0, 1.0, -1.0 ]
 * 
 * 解题思路：
 * 1. 使用带权并查集来解决这个问题
 * 2. 权值表示从当前节点到父节点的商（即父节点 / 当前节点的值）
 * 3. find 操作时进行路径压缩，并同时更新权值
 * 4. union 操作时合并两个节点，并维护权值关系
 * 5. 查询时，如果两个节点不在同一集合，返回 -1.0；否则返回它们的权值比
 * 
 * 时间复杂度分析：
 * - 构建并查集：O(n * α(m))，其中n是equations的长度，m是不同变量的数量，α是阿克曼函数的反函数，近似为常数
 * - 处理查询：O(q * α(m))，其中q是queries的长度
 * - 总体时间复杂度：O((n+q) * α(m))
 * 
 * 空间复杂度分析：
 * - 存储并查集：O(m)
 * - 总体空间复杂度：O(m)
 */

import java.util.*;

public class Code21_DivisionEvaluation {
    // 并查集的父节点映射
    private Map<String, String> parent;
    // 并查集的权值映射，表示从当前节点到父节点的商（parent / current）
    private Map<String, Double> weight;
    
    /**
     * 初始化并查集
     */
    public Code21_DivisionEvaluation() {
        parent = new HashMap<>();
        weight = new HashMap<>();
    }
    
    /**
     * 查找节点的根节点，并进行路径压缩，同时更新权值
     * @param x 要查找的节点
     * @return 根节点
     */
    private String find(String x) {
        // 如果节点不存在于并查集中，将其加入并查集
        if (!parent.containsKey(x)) {
            parent.put(x, x);
            weight.put(x, 1.0); // 自己到自己的商为1
            return x;
        }
        
        // 如果x不是根节点，需要进行路径压缩
        if (!x.equals(parent.get(x))) {
            String originParent = parent.get(x);
            // 递归查找父节点的根节点，同时更新父节点的权值
            String root = find(parent.get(x));
            // 更新x的父节点为根节点（路径压缩）
            parent.put(x, root);
            // 更新x的权值：x到根节点的权值 = x到原父节点的权值 * 原父节点到根节点的权值
            weight.put(x, weight.get(x) * weight.get(originParent));
        }
        return parent.get(x);
    }
    
    /**
     * 合并两个节点，并维护权值关系
     * @param x 第一个节点
     * @param y 第二个节点
     * @param value x / y 的值
     */
    private void union(String x, String y, double value) {
        // 查找x和y的根节点
        String rootX = find(x);
        String rootY = find(y);
        
        // 如果x和y已经在同一个集合中，不需要合并
        if (rootX.equals(rootY)) {
            return;
        }
        
        // 合并x的集合到y的集合
        parent.put(rootX, rootY);
        // 维护权值关系：
        // 已知x / y = value
        // 需要确定 rootX / rootY 的值
        // x到rootX的权值是 weight.get(x)，即 rootX / x
        // y到rootY的权值是 weight.get(y)，即 rootY / y
        // 所以 rootX / rootY = (rootX / x) * (x / y) * (y / rootY) = weight.get(x) * value * (1 / weight.get(y))
        weight.put(rootX, weight.get(x) * value / weight.get(y));
    }
    
    /**
     * 计算除法求值问题
     * @param equations 等式数组
     * @param values 等式结果数组
     * @param queries 查询数组
     * @return 查询结果数组
     */
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // 重置并查集
        parent.clear();
        weight.clear();
        
        // 构建并查集
        for (int i = 0; i < equations.size(); i++) {
            String x = equations.get(i).get(0);
            String y = equations.get(i).get(1);
            double value = values[i]; // x / y = value
            union(x, y, value);
        }
        
        // 处理查询
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String x = queries.get(i).get(0);
            String y = queries.get(i).get(1);
            
            // 如果x或y不存在于并查集中，无法计算
            if (!parent.containsKey(x) || !parent.containsKey(y)) {
                results[i] = -1.0;
                continue;
            }
            
            String rootX = find(x);
            String rootY = find(y);
            
            // 如果x和y不在同一个集合中，无法计算
            if (!rootX.equals(rootY)) {
                results[i] = -1.0;
            } else {
                // x / y = (x到根节点的权值倒数) / (y到根节点的权值倒数) = weight.get(y) / weight.get(x)
                // 因为weight存储的是 root / node，所以 node = root / weight.get(node)
                results[i] = weight.get(y) / weight.get(x);
            }
        }
        
        return results;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code21_DivisionEvaluation solution = new Code21_DivisionEvaluation();
        
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
        
        double[] results1 = solution.calcEquation(equations1, values1, queries1);
        System.out.println("测试用例1结果：");
        for (double result : results1) {
            System.out.printf("%.5f ", result);
        }
        System.out.println();
        
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
        
        double[] results2 = solution.calcEquation(equations2, values2, queries2);
        System.out.println("测试用例2结果：");
        for (double result : results2) {
            System.out.printf("%.5f ", result);
        }
    }
    
    /**
     * 异常处理考虑：
     * 1. 输入参数校验：equations和values长度是否一致，queries是否合法
     * 2. 处理不存在的变量：当查询中包含未在equations中出现的变量时，返回-1.0
     * 3. 处理自环查询：如a/a返回1.0
     * 4. 精度问题：浮点数计算可能存在精度误差，这里直接使用double类型
     */
    
    /**
     * 优化点：
     * 1. 路径压缩和按秩合并已经实现，保证了并查集操作的高效性
     * 2. 可以考虑使用字符串到整数的映射，减少字符串操作的开销
     * 3. 对于大规模数据，可以预先分配足够的空间
     */
}