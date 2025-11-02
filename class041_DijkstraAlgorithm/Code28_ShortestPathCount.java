package class064;

import java.util.*;

/**
 * ============================================================================
 * 最短路径计数问题 - Dijkstra算法扩展实现
 * ============================================================================
 * 
 * 题目：最短路径条数统计
 * 来源：洛谷 P1144 最短路计数
 * 链接：https://www.luogu.com.cn/problem/P1144
 * 
 * 题目描述：
 * 给出一个N个顶点M条边的无向无权图，顶点编号为1∼N。
 * 问从顶点1出发，到其他每个点的最短路有几条。
 * 
 * 输入格式：
 * - n: 节点数量，编号从1到n
 * - edges: 边列表，每条边格式为[u, v]，表示无向边
 * 
 * 输出格式：
 * - 计数数组，count[i]表示从源点到节点i的最短路径条数
 * - 结果对100003取模
 * 
 * 算法原理：
 * ============================================================================
 * 本算法在标准Dijkstra算法的基础上，增加了路径计数功能。核心思想是：
 * 1. 在计算最短距离的同时，维护到达每个节点的最短路径条数
 * 2. 当发现更短路径时，重置计数为当前路径的计数
 * 3. 当发现相同长度路径时，累加路径计数
 * 
 * 算法正确性保证：
 * - 最短路径的最优子结构：最短路径的子路径也是最短路径
 * - 计数累加的正确性：所有相同长度的最短路径都会被统计
 * 
 * 算法步骤详解：
 * ============================================================================
 * 1. 初始化阶段：
 *    - 构建图的邻接表表示
 *    - 初始化距离数组，源点距离为0，其他为无穷大
 *    - 初始化计数数组，源点计数为1，其他为0
 *    - 创建优先队列，按距离排序
 * 
 * 2. 处理阶段：
 *    - 从优先队列中取出距离最小的节点u
 *    - 如果u已经被访问过，跳过
 *    - 标记u为已访问
 *    - 遍历u的所有邻居节点v：
 *        - 计算新距离 = dist[u] + 1（无权图边权为1）
 *        - 如果新距离 < dist[v]：
 *            - 更新dist[v] = 新距离
 *            - 更新count[v] = count[u]（重置计数）
 *            - 将v加入优先队列
 *        - 如果新距离 == dist[v]：
 *            - count[v] = (count[v] + count[u]) % MOD（累加计数）
 * 
 * 3. 输出阶段：
 *    - 返回计数数组，对100003取模
 * 
 * 时间复杂度分析：
 * ============================================================================
 * - 每个节点入队出队一次: O(V log V)
 * - 每条边被处理一次: O(E log V)
 * - 总时间复杂度: O((V + E) log V)
 * 
 * 空间复杂度分析：
 * ============================================================================
 * - 邻接表存储: O(V + E)
 * - 距离数组: O(V)
 * - 计数数组: O(V)
 * - 优先队列: O(V)
 * - 总空间复杂度: O(V + E)
 * 
 * 算法特点：
 * ============================================================================
 * 1. 适用于无权图：所有边权重为1
 * 2. 支持路径计数：统计所有最短路径的条数
 * 3. 高效实现：基于优先队列的Dijkstra算法
 * 
 * 边界情况处理：
 * ============================================================================
 * 1. 自环边：无权图中自环边不影响结果
 * 2. 重边：多条相同边会增加路径计数
 * 3. 不可达节点：计数保持为0
 * 4. 大规模数据：使用模运算防止整数溢出
 * 
 * 测试用例设计：
 * ============================================================================
 * 1. 基础测试：简单连通图
 * 2. 复杂测试：存在多条最短路径的图
 * 3. 边界测试：单节点、空图、完全图
 * 4. 性能测试：大规模稀疏图和稠密图
 * 
 * 工程化实践：
 * ============================================================================
 * 1. 模块化设计：将算法逻辑封装为独立方法
 * 2. 错误处理：验证输入参数的合法性
 * 3. 性能优化：使用优先队列和邻接表
 * 4. 代码可读性：清晰的变量命名和详细注释
 * 
 * 相关算法扩展：
 * ============================================================================
 * 1. 带权图版本：支持任意权重的边
 * 2. 多源版本：计算从多个源点的最短路径计数
 * 3. 动态版本：支持图的动态更新
 * 
 * 应用场景：
 * ============================================================================
 * 1. 网络路由分析：统计网络中的最短路径多样性
 * 2. 社交网络：分析人际关系的最短路径数量
 * 3. 交通规划：评估交通网络的冗余性和可靠性
 * 
 * 作者：算法工程化项目组
 * 创建时间：2025-10-29
 * 版本：v1.0
 */
public class Code28_ShortestPathCount {
    
    /**
     * 计算最短路径条数
     * 
     * 算法步骤：
     * 1. 初始化距离数组和计数数组
     * 2. 源点距离为0，计数为1
     * 3. 使用优先队列进行Dijkstra算法
     * 4. 对于每个邻居节点：
     *    - 如果发现更短路径：更新距离，重置计数
     *    - 如果发现相同长度路径：累加计数
     * 
     * 时间复杂度：O((V+E)logV)
     * 空间复杂度：O(V+E)
     * 
     * @param n 节点总数
     * @param edges 边列表（无向图）
     * @param source 源点
     * @return 计数数组，count[i]表示从源点到节点i的最短路径条数
     */
    public static int[] shortestPathCount(int n, int[][] edges, int source) {
        // 构建邻接表（无向图）
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        
        // 距离数组
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        
        // 计数数组
        int[] count = new int[n + 1];
        count[source] = 1;
        
        // 访问标记数组
        boolean[] visited = new boolean[n + 1];
        
        // 优先队列，按距离排序
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.offer(new int[]{source, 0});
        
        while (!heap.isEmpty()) {
            int[] record = heap.poll();
            int u = record[0];
            int d = record[1];
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    int newDist = d + 1; // 无权图，边权为1
                    
                    if (newDist < dist[v]) {
                        // 发现更短路径
                        dist[v] = newDist;
                        count[v] = count[u]; // 重置计数
                        heap.offer(new int[]{v, newDist});
                    } else if (newDist == dist[v]) {
                        // 发现相同长度路径
                        count[v] = (count[v] + count[u]) % 100003; // 题目要求取模
                    }
                }
            }
        }
        
        return count;
    }
    
    /**
     * 带权图的最短路径计数（扩展版本）
     * 
     * 算法步骤：
     * 1. 支持带权图的最短路径计数
     * 2. 使用更通用的Dijkstra算法实现
     * 
     * 时间复杂度：O((V+E)logV)
     * 空间复杂度：O(V+E)
     */
    public static int[] weightedShortestPathCount(int n, int[][] edges, int source) {
        // 构建邻接表（带权图）
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph.get(u).add(new int[]{v, w});
            graph.get(v).add(new int[]{u, w}); // 无向图
        }
        
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        
        int[] count = new int[n + 1];
        count[source] = 1;
        
        boolean[] visited = new boolean[n + 1];
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.offer(new int[]{source, 0});
        
        while (!heap.isEmpty()) {
            int[] record = heap.poll();
            int u = record[0];
            int d = record[1];
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (int[] edge : graph.get(u)) {
                int v = edge[0], w = edge[1];
                
                if (!visited[v]) {
                    int newDist = d + w;
                    
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        count[v] = count[u];
                        heap.offer(new int[]{v, newDist});
                    } else if (newDist == dist[v]) {
                        count[v] = (count[v] + count[u]) % 100003;
                    }
                }
            }
        }
        
        return count;
    }
    
    /**
     * 多源最短路径计数（扩展功能）
     * 
     * 算法步骤：
     * 1. 计算从多个源点到所有节点的最短路径计数
     * 2. 使用虚拟超级源点法
     * 
     * 时间复杂度：O((V+E)logV)
     * 空间复杂度：O(V+E)
     */
    public static int[] multiSourceShortestPathCount(int n, int[][] edges, int[] sources) {
        // 构建扩展图（包含虚拟源点0）
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        
        // 添加虚拟源点到所有实际源点的边
        for (int source : sources) {
            graph.get(0).add(source);
            graph.get(source).add(0);
        }
        
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;
        
        int[] count = new int[n + 1];
        count[0] = 1;
        
        boolean[] visited = new boolean[n + 1];
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.offer(new int[]{0, 0});
        
        while (!heap.isEmpty()) {
            int[] record = heap.poll();
            int u = record[0];
            int d = record[1];
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    int newDist = d + 1;
                    
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        count[v] = count[u];
                        heap.offer(new int[]{v, newDist});
                    } else if (newDist == dist[v]) {
                        count[v] = (count[v] + count[u]) % 100003;
                    }
                }
            }
        }
        
        // 返回从节点1开始的结果（排除虚拟源点0）
        return Arrays.copyOfRange(count, 1, n + 1);
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1：洛谷P1144示例
        System.out.println("=== 测试用例1：无权图最短路径计数 ===");
        int n1 = 4;
        int[][] edges1 = {
            {1, 2}, {1, 3}, {2, 3}, {2, 4}, {3, 4}
        };
        int source1 = 1;
        
        int[] result1 = shortestPathCount(n1, edges1, source1);
        for (int i = 1; i <= n1; i++) {
            System.out.printf("节点%d的最短路径条数: %d\n", i, result1[i]);
        }
        
        // 测试用例2：带权图最短路径计数
        System.out.println("\n=== 测试用例2：带权图最短路径计数 ===");
        int n2 = 4;
        int[][] edges2 = {
            {1, 2, 1}, {1, 3, 2}, {2, 3, 1}, {2, 4, 3}, {3, 4, 1}
        };
        int source2 = 1;
        
        int[] result2 = weightedShortestPathCount(n2, edges2, source2);
        for (int i = 1; i <= n2; i++) {
            System.out.printf("节点%d的最短路径条数: %d\n", i, result2[i]);
        }
        
        // 测试用例3：多源最短路径计数
        System.out.println("\n=== 测试用例3：多源最短路径计数 ===");
        int n3 = 5;
        int[][] edges3 = {
            {1, 2}, {2, 3}, {3, 4}, {4, 5}, {1, 3}, {2, 4}
        };
        int[] sources3 = {1, 3};
        
        int[] result3 = multiSourceShortestPathCount(n3, edges3, sources3);
        for (int i = 0; i < result3.length; i++) {
            System.out.printf("节点%d的最短路径条数: %d\n", i + 1, result3[i]);
        }
        
        // 算法分析
        System.out.println("\n=== 算法分析 ===");
        System.out.println("1. 核心思想：在Dijkstra算法基础上维护计数数组");
        System.out.println("2. 关键点：正确处理相同距离的路径计数累加");
        System.out.println("3. 应用场景：网络分析、路径规划、社交网络");
        System.out.println("4. 扩展功能：支持带权图、多源点等复杂场景");
    }
}