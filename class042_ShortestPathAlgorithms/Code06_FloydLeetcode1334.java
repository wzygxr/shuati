package class065;

import java.util.*;

// LeetCode 1334. 阈值距离内邻居最少的城市 - Floyd算法实现
// 题目链接: https://leetcode.cn/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/
// 题目描述: 有 n 个城市，按从 0 到 n-1 编号。给你一个边数组 edges，其中 edges[i] = [fromi, toi, weighti] 
// 代表 fromi 和 toi 两个城市之间的双向加权边，距离阈值是一个整数 distanceThreshold。
// 返回在路径距离限制为 distanceThreshold 以内可到达城市最少的城市。如果有多个这样的城市，则返回编号最大的城市。
//
// Floyd算法核心思想:
// 使用动态规划思想，通过三重循环不断尝试以每个节点为中间点，更新任意两点间的最短距离
// 状态转移方程: distance[i][j] = min(distance[i][j], distance[i][k] + distance[k][j])
//
// 时间复杂度: O(N^3)，其中N是城市数量
// 空间复杂度: O(N^2)，需要二维数组存储距离矩阵

public class Code06_FloydLeetcode1334 {
    
    public static int findTheCity(int n, int[][] edges, int distanceThreshold) {
        // 初始化距离矩阵
        int[][] distance = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distance[i][j] = 0;
                } else {
                    distance[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        
        // 根据边的信息初始化距离矩阵
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            int weight = edge[2];
            distance[from][to] = weight;
            distance[to][from] = weight;  // 因为是无向图
        }
        
        // Floyd算法求所有点对之间的最短距离
        floyd(n, distance);
        
        // 统计每个城市在距离阈值内能到达的城市数量
        int minCount = n;  // 最少城市数量
        int result = -1;   // 结果城市编号
        
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (i != j && distance[i][j] <= distanceThreshold) {
                    count++;
                }
            }
            
            // 更新结果：城市数量更少，或者城市数量相同但编号更大
            if (count < minCount || (count == minCount && i > result)) {
                minCount = count;
                result = i;
            }
        }
        
        return result;
    }
    
    // Floyd算法核心实现
    public static void floyd(int n, int[][] distance) {
        // 三层循环：中间节点k，起点i，终点j
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // 注意处理Integer.MAX_VALUE的情况，避免溢出
                    if (distance[i][k] != Integer.MAX_VALUE && 
                        distance[k][j] != Integer.MAX_VALUE && 
                        distance[i][k] + distance[k][j] < distance[i][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 4;
        int[][] edges1 = {{0,1,3},{1,2,1},{1,3,4},{2,3,1}};
        int distanceThreshold1 = 4;
        System.out.println("测试用例1结果: " + findTheCity(n1, edges1, distanceThreshold1)); // 期望输出: 3
        
        // 测试用例2
        int n2 = 5;
        int[][] edges2 = {{0,1,2},{0,4,8},{1,2,3},{1,4,2},{2,3,1},{3,4,1}};
        int distanceThreshold2 = 2;
        System.out.println("测试用例2结果: " + findTheCity(n2, edges2, distanceThreshold2)); // 期望输出: 0
    }
}


/* ----------------------------- 补充题目1: LeetCode 399. 除法求值 ----------------------------- */
// 题目链接: https://leetcode.cn/problems/evaluate-division/
// 题目描述: 给你一个变量对数组 equations 和一个实数值数组 values 作为已知条件，
// 其中 equations[i] = [Ai, Bi] 和 values[i] 共同表示等式 Ai / Bi = values[i]。每个 Ai 或 Bi 是一个表示单个变量的字符串。
// 另有一些以数组 queries 表示的问题，其中 queries[j] = [Cj, Dj] 表示第 j 个问题，请你根据已知条件找出 Cj / Dj = ?
// 的结果作为答案。如果无法确定，则返回 -1.0。

// Floyd算法解决思路:
// 1. 将变量视为图的节点，除法关系视为有向边的权重
// 2. 对于等式 Ai/Bi = values[i]，我们可以得到两条边：Ai->Bi (权重为values[i]) 和 Bi->Ai (权重为1/values[i])
// 3. 使用Floyd算法计算任意两个节点之间的乘积关系，类似于最短路径问题
// 4. 最终查询任意两点间的乘积即为除法结果

class DivisionEvaluator {
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // 构建变量到索引的映射
        Map<String, Integer> varMap = new HashMap<>();
        int idx = 0;
        for (List<String> eq : equations) {
            for (String var : eq) {
                if (!varMap.containsKey(var)) {
                    varMap.put(var, idx++);
                }
            }
        }
        
        int n = varMap.size();
        // 初始化距离矩阵
        double[][] dist = new double[n][n];
        // 初始化为-1，表示无法到达
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], -1.0);
            dist[i][i] = 1.0; // 自己除以自己等于1
        }
        
        // 填充初始边
        for (int i = 0; i < equations.size(); i++) {
            String a = equations.get(i).get(0);
            String b = equations.get(i).get(1);
            int ai = varMap.get(a);
            int bi = varMap.get(b);
            dist[ai][bi] = values[i];     // a/b = values[i]
            dist[bi][ai] = 1.0 / values[i]; // b/a = 1/values[i]
        }
        
        // Floyd算法计算任意两点间的乘积关系
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // 如果i到k和k到j都可达，则更新i到j的路径
                    if (dist[i][k] != -1 && dist[k][j] != -1) {
                        dist[i][j] = dist[i][k] * dist[k][j];
                    }
                }
            }
        }
        
        // 处理查询
        double[] result = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String c = queries.get(i).get(0);
            String d = queries.get(i).get(1);
            
            // 如果变量不存在，结果为-1
            if (!varMap.containsKey(c) || !varMap.containsKey(d)) {
                result[i] = -1.0;
            } else {
                int ci = varMap.get(c);
                int di = varMap.get(d);
                result[i] = dist[ci][di];
            }
        }
        
        return result;
    }
}

/* ----------------------------- 补充题目2: 传递闭包问题 ----------------------------- */
// 题目描述: 给定一个有向图，确定任意两个节点之间是否存在路径
// Floyd算法解决思路:
// 1. 使用布尔矩阵表示可达性
// 2. 通过三重循环更新可达性：如果i到k可达且k到j可达，则i到j可达

/* ----------------------------- 补充题目3: 最小环问题 ----------------------------- */
// 题目描述: 寻找图中总权值最小的环
// Floyd算法解决思路:
// 1. 在Floyd算法的三重循环中，维护一个全局最小值
// 2. 对于每个中间节点k，考虑经过k的环：i到k-1的最短路径 + k到i的边

/* ----------------------------- Floyd算法工程实践建议 ----------------------------- */
// 1. 适用场景:
//    - 节点数较少(N < 300)的全源最短路径问题
//    - 需要频繁查询任意两点间距离的场景
//    - 可以处理负权边，但不能处理负权环
//
// 2. 性能优化技巧:
//    - 对于稀疏图，可以先进行邻接表优化
//    - 可以用位运算优化传递闭包问题
//    - 使用滚动数组优化空间（但会丢失中间结果）
//
// 3. 与其他算法对比:
//    - 相比Dijkstra多次运行: Floyd代码更简洁，但时间复杂度更高
//    - 相比Bellman-Ford多次运行: Floyd在稠密图中更高效
//    - 优势在于代码简单，易于实现和调试
//
// 4. 常见陷阱:
//    - 整数溢出问题: 使用long或浮点数类型
//    - 初始值设置错误: 正确设置无穷大值
//    - 负权环处理: Floyd无法自动检测负权环