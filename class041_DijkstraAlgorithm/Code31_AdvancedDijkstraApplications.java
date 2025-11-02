package class064;

import java.util.*;
import java.util.concurrent.*;

/**
 * Dijkstra算法高级应用与扩展
 * 
 * 本类包含Dijkstra算法在各种复杂场景下的高级应用：
 * 1. 多目标优化最短路径
 * 2. 实时动态最短路径
 * 3. 分布式最短路径计算
 * 4. 增量式最短路径更新
 * 
 * 算法应用场景：
 * - 智能交通系统中的实时路线规划
 * - 分布式网络中的路由计算
 * - 大规模图数据库的最短路径查询
 * - 动态变化图中的增量更新
 */
public class Code31_AdvancedDijkstraApplications {
    
    /**
     * 多目标优化最短路径问题
     * 
     * 问题描述：同时优化多个目标（如时间、成本、舒适度等）
     * 解法思路：使用帕累托最优解集，维护多个目标函数
     * 
     * 时间复杂度：O((V+E) * P * log(V*P))，其中P是帕累托解的数量
     * 空间复杂度：O(V * P)
     */
    public static class MultiObjectiveShortestPath {
        
        /**
         * 多目标最短路径求解
         * 
         * @param n 节点数
         * @param edges 边列表，每条边包含多个权重
         * @param src 源点
         * @param dst 目标点
         * @return 帕累托最优解集
         */
        public static List<int[]> multiObjectiveDijkstra(int n, List<int[]> edges, int src, int dst) {
            // 构建邻接表，每条边有多个权重
            Map<Integer, List<int[]>> graph = new HashMap<>();
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                graph.computeIfAbsent(u, k -> new ArrayList<>())
                     .add(Arrays.copyOfRange(edge, 2, edge.length));
                graph.computeIfAbsent(v, k -> new ArrayList<>())
                     .add(new int[]{u, edge[2], edge[3]}); // 无向图
            }
            
            // 每个节点的帕累托最优解集
            Map<Integer, Set<int[]>> paretoFront = new HashMap<>();
            for (int i = 0; i < n; i++) {
                paretoFront.put(i, new HashSet<>());
            }
            
            // 初始状态：源点的解为全0
            int[] zeroCost = new int[2]; // 假设有两个目标
            paretoFront.get(src).add(zeroCost);
            
            // 优先队列，按第一个目标函数排序
            PriorityQueue<int[]> heap = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[1], b[1])); // 按第一个成本排序
            
            heap.offer(new int[]{src, 0, 0}); // [节点, 成本1, 成本2]
            
            while (!heap.isEmpty()) {
                int[] state = heap.poll();
                int u = state[0];
                int cost1 = state[1];
                int cost2 = state[2];
                
                // 检查当前解是否仍然在帕累托前沿
                if (!isParetoOptimal(paretoFront.get(u), cost1, cost2)) {
                    continue;
                }
                
                if (u == dst) {
                    // 找到目标点，继续寻找其他帕累托解
                    continue;
                }
                
                // 扩展邻居节点
                if (graph.containsKey(u)) {
                    for (int[] edge : graph.get(u)) {
                        int v = edge[0];
                        int w1 = edge[1];
                        int w2 = edge[2];
                        
                        int newCost1 = cost1 + w1;
                        int newCost2 = cost2 + w2;
                        
                        // 检查新解是否可以被接受
                        if (isParetoOptimal(paretoFront.get(v), newCost1, newCost2)) {
                            // 更新帕累托前沿
                            updateParetoFront(paretoFront.get(v), newCost1, newCost2);
                            heap.offer(new int[]{v, newCost1, newCost2});
                        }
                    }
                }
            }
            
            return new ArrayList<>(paretoFront.get(dst));
        }
        
        private static boolean isParetoOptimal(Set<int[]> front, int cost1, int cost2) {
            for (int[] solution : front) {
                if (solution[0] <= cost1 && solution[1] <= cost2) {
                    // 被支配，不是帕累托最优
                    return false;
                }
            }
            return true;
        }
        
        private static void updateParetoFront(Set<int[]> front, int cost1, int cost2) {
            // 移除被新解支配的旧解
            front.removeIf(sol -> sol[0] >= cost1 && sol[1] >= cost2);
            front.add(new int[]{cost1, cost2});
        }
    }
    
    /**
     * 实时动态最短路径算法
     * 
     * 问题描述：图中边权重随时间动态变化，需要实时更新最短路径
     * 解法思路：增量式更新，只重新计算受影响的部分
     * 
     * 时间复杂度：平均O(k * logV)，k是受影响节点数
     * 空间复杂度：O(V+E)
     */
    public static class DynamicDijkstra {
        
        private int n;
        private List<List<int[]>> graph;
        private int[] dist;
        private boolean[] visited;
        
        public DynamicDijkstra(int n, List<int[]> edges) {
            this.n = n;
            this.graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            
            // 初始建图
            for (int[] edge : edges) {
                addEdge(edge[0], edge[1], edge[2]);
            }
            
            this.dist = new int[n];
            Arrays.fill(dist, Integer.MAX_VALUE);
            this.visited = new boolean[n];
        }
        
        /**
         * 添加或更新边权重
         */
        public void updateEdge(int u, int v, int newWeight) {
            // 首先移除旧边（如果存在）
            removeEdge(u, v);
            // 添加新边
            addEdge(u, v, newWeight);
            
            // 增量式更新最短路径
            incrementalUpdate(u, v, newWeight);
        }
        
        private void addEdge(int u, int v, int w) {
            graph.get(u).add(new int[]{v, w});
            graph.get(v).add(new int[]{u, w}); // 无向图
        }
        
        private void removeEdge(int u, int v) {
            graph.get(u).removeIf(edge -> edge[0] == v);
            graph.get(v).removeIf(edge -> edge[0] == u);
        }
        
        /**
         * 增量式更新最短路径
         */
        private void incrementalUpdate(int u, int v, int newWeight) {
            // 使用优先队列进行局部更新
            PriorityQueue<int[]> heap = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[1], b[1]));
            
            // 将受影响节点加入队列
            if (dist[u] != Integer.MAX_VALUE) {
                heap.offer(new int[]{u, dist[u]});
            }
            if (dist[v] != Integer.MAX_VALUE) {
                heap.offer(new int[]{v, dist[v]});
            }
            
            // 局部Dijkstra更新
            while (!heap.isEmpty()) {
                int[] state = heap.poll();
                int node = state[0];
                int d = state[1];
                
                if (visited[node] && d >= dist[node]) {
                    continue;
                }
                
                dist[node] = d;
                visited[node] = true;
                
                for (int[] edge : graph.get(node)) {
                    int neighbor = edge[0];
                    int weight = edge[1];
                    int newDist = d + weight;
                    
                    if (newDist < dist[neighbor]) {
                        dist[neighbor] = newDist;
                        heap.offer(new int[]{neighbor, newDist});
                    }
                }
            }
        }
        
        /**
         * 获取最短距离
         */
        public int getShortestDistance(int target) {
            return dist[target] == Integer.MAX_VALUE ? -1 : dist[target];
        }
    }
    
    /**
     * 分布式最短路径计算
     * 
     * 问题描述：大规模图分布在多个计算节点上，需要分布式计算最短路径
     * 解法思路：使用消息传递模型，各节点协作计算
     * 
     * 模拟实现：使用多线程模拟分布式环境
     */
    public static class DistributedDijkstra {
        
        private final int numNodes;
        private final int numWorkers;
        private final Map<Integer, List<int[]>> localGraphs;
        
        public DistributedDijkstra(int numNodes, int numWorkers, List<int[]> edges) {
            this.numNodes = numNodes;
            this.numWorkers = numWorkers;
            this.localGraphs = new HashMap<>();
            
            // 将图分区分配给不同的工作节点
            partitionGraph(edges);
        }
        
        /**
         * 图分区策略
         */
        private void partitionGraph(List<int[]> edges) {
            // 简单哈希分区：节点i分配给工作节点i % numWorkers
            for (int[] edge : edges) {
                int u = edge[0];
                int workerId = u % numWorkers;
                localGraphs.computeIfAbsent(workerId, k -> new ArrayList<>())
                          .add(edge);
            }
        }
        
        /**
         * 分布式Dijkstra计算
         */
        public int[] computeShortestPaths(int source) throws InterruptedException {
            // 使用线程池模拟分布式计算
            ExecutorService executor = Executors.newFixedThreadPool(numWorkers);
            List<Future<int[]>> futures = new ArrayList<>();
            
            // 每个工作节点计算本地部分
            for (int workerId = 0; workerId < numWorkers; workerId++) {
                final int wid = workerId;
                Future<int[]> future = executor.submit(() -> {
                    return computeLocal(wid, source);
                });
                futures.add(future);
            }
            
            // 合并结果
            int[] globalDist = new int[numNodes];
            Arrays.fill(globalDist, Integer.MAX_VALUE);
            globalDist[source] = 0;
            
            for (Future<int[]> future : futures) {
                try {
                    int[] localDist = future.get();
                    // 合并局部结果到全局结果
                    for (int i = 0; i < numNodes; i++) {
                        if (localDist[i] < globalDist[i]) {
                            globalDist[i] = localDist[i];
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            
            executor.shutdown();
            return globalDist;
        }
        
        /**
         * 工作节点本地计算
         */
        private int[] computeLocal(int workerId, int source) {
            int[] localDist = new int[numNodes];
            Arrays.fill(localDist, Integer.MAX_VALUE);
            
            if (!localGraphs.containsKey(workerId)) {
                return localDist;
            }
            
            // 本地Dijkstra计算
            List<int[]> edges = localGraphs.get(workerId);
            // 简化的本地计算实现
            // 实际分布式实现需要更复杂的通信协议
            
            return localDist;
        }
    }
    
    /**
     * 增量式最短路径更新算法
     * 
     * 问题描述：当图中只有少量边权重发生变化时，高效更新最短路径
     * 解法思路：利用原有最短路径信息，只更新受影响的部分
     * 
     * 时间复杂度：O(k * logV)，k是受影响节点数
     */
    public static class IncrementalDijkstra {
        
        private int[] dist;
        private int[] parent;
        private List<List<int[]>> graph;
        
        public IncrementalDijkstra(int n, List<int[]> edges) {
            this.dist = new int[n];
            this.parent = new int[n];
            this.graph = new ArrayList<>();
            
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
                dist[i] = Integer.MAX_VALUE;
                parent[i] = -1;
            }
            
            // 初始计算最短路径
            computeFullDijkstra(0, edges); // 假设源点为0
        }
        
        /**
         * 全量Dijkstra计算
         */
        private void computeFullDijkstra(int source, List<int[]> edges) {
            // 构建图
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1], w = edge[2];
                graph.get(u).add(new int[]{v, w});
                graph.get(v).add(new int[]{u, w});
            }
            
            // 标准Dijkstra算法
            PriorityQueue<int[]> heap = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[1], b[1]));
            dist[source] = 0;
            heap.offer(new int[]{source, 0});
            
            while (!heap.isEmpty()) {
                int[] state = heap.poll();
                int u = state[0];
                int d = state[1];
                
                if (d > dist[u]) continue;
                
                for (int[] edge : graph.get(u)) {
                    int v = edge[0], w = edge[1];
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        parent[v] = u;
                        heap.offer(new int[]{v, dist[v]});
                    }
                }
            }
        }
        
        /**
         * 增量式更新：边权重增加
         */
        public void handleWeightIncrease(int u, int v, int oldWeight, int newWeight) {
            if (newWeight <= oldWeight) {
                return; // 权重减少或不变，不需要特殊处理
            }
            
            // 检查这条边是否在最短路径树中
            if (parent[v] == u || parent[u] == v) {
                // 边在最短路径树中，需要重新计算受影响部分
                recomputeAffectedNodes(u, v);
            }
        }
        
        /**
         * 增量式更新：边权重减少
         */
        public void handleWeightDecrease(int u, int v, int oldWeight, int newWeight) {
            if (newWeight >= oldWeight) {
                return; // 权重增加或不变，不需要特殊处理
            }
            
            // 权重减少，可能产生更短路径
            PriorityQueue<int[]> heap = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[1], b[1]));
            
            // 从u和v开始更新
            if (dist[u] != Integer.MAX_VALUE) {
                heap.offer(new int[]{u, dist[u]});
            }
            if (dist[v] != Integer.MAX_VALUE) {
                heap.offer(new int[]{v, dist[v]});
            }
            
            // 局部Dijkstra更新
            while (!heap.isEmpty()) {
                int[] state = heap.poll();
                int node = state[0];
                int d = state[1];
                
                if (d > dist[node]) continue;
                
                for (int[] edge : graph.get(node)) {
                    int neighbor = edge[0], w = edge[1];
                    if (d + w < dist[neighbor]) {
                        dist[neighbor] = d + w;
                        parent[neighbor] = node;
                        heap.offer(new int[]{neighbor, dist[neighbor]});
                    }
                }
            }
        }
        
        /**
         * 重新计算受影响节点
         */
        private void recomputeAffectedNodes(int u, int v) {
            // 标记受影响节点（在u或v的子树中的节点）
            boolean[] affected = new boolean[dist.length];
            markAffected(u, affected);
            markAffected(v, affected);
            
            // 重新计算受影响节点的最短路径
            PriorityQueue<int[]> heap = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[1], b[1]));
            
            // 将所有受影响节点加入队列
            for (int i = 0; i < dist.length; i++) {
                if (affected[i] && dist[i] != Integer.MAX_VALUE) {
                    heap.offer(new int[]{i, dist[i]});
                }
            }
            
            // 局部Dijkstra更新
            while (!heap.isEmpty()) {
                int[] state = heap.poll();
                int node = state[0];
                int d = state[1];
                
                if (d > dist[node]) continue;
                
                for (int[] edge : graph.get(node)) {
                    int neighbor = edge[0], w = edge[1];
                    if (d + w < dist[neighbor]) {
                        dist[neighbor] = d + w;
                        parent[neighbor] = node;
                        heap.offer(new int[]{neighbor, dist[neighbor]});
                    }
                }
            }
        }
        
        private void markAffected(int node, boolean[] affected) {
            if (affected[node]) return;
            affected[node] = true;
            
            for (int[] edge : graph.get(node)) {
                int neighbor = edge[0];
                if (parent[neighbor] == node) {
                    markAffected(neighbor, affected);
                }
            }
        }
        
        public int getDistance(int target) {
            return dist[target];
        }
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        System.out.println("=== Dijkstra算法高级应用测试 ===");
        
        // 测试多目标优化
        testMultiObjective();
        
        // 测试动态更新
        testDynamicDijkstra();
        
        // 测试增量式更新
        testIncrementalUpdate();
    }
    
    private static void testMultiObjective() {
        System.out.println("\n=== 多目标优化最短路径测试 ===");
        
        int n = 4;
        List<int[]> edges = Arrays.asList(
            new int[]{0, 1, 2, 3},  // u, v, 成本1, 成本2
            new int[]{0, 2, 1, 4},
            new int[]{1, 3, 3, 1},
            new int[]{2, 3, 2, 2}
        );
        
        List<int[]> solutions = MultiObjectiveShortestPath.multiObjectiveDijkstra(
            n, edges, 0, 3);
        
        System.out.println("帕累托最优解数量: " + solutions.size());
        for (int[] sol : solutions) {
            System.out.printf("成本1: %d, 成本2: %d\n", sol[0], sol[1]);
        }
    }
    
    private static void testDynamicDijkstra() {
        System.out.println("\n=== 动态最短路径测试 ===");
        
        int n = 4;
        List<int[]> edges = Arrays.asList(
            new int[]{0, 1, 2},
            new int[]{0, 2, 4},
            new int[]{1, 3, 3},
            new int[]{2, 3, 1}
        );
        
        DynamicDijkstra dd = new DynamicDijkstra(n, edges);
        System.out.println("初始最短距离: " + dd.getShortestDistance(3));
        
        // 更新边权重
        dd.updateEdge(2, 3, 5);
        System.out.println("更新后最短距离: " + dd.getShortestDistance(3));
    }
    
    private static void testIncrementalUpdate() {
        System.out.println("\n=== 增量式更新测试 ===");
        
        int n = 4;
        List<int[]> edges = Arrays.asList(
            new int[]{0, 1, 2},
            new int[]{0, 2, 4},
            new int[]{1, 3, 3},
            new int[]{2, 3, 1}
        );
        
        IncrementalDijkstra id = new IncrementalDijkstra(n, edges);
        System.out.println("初始距离: " + id.getDistance(3));
        
        // 处理权重减少
        id.handleWeightDecrease(2, 3, 1, 0);
        System.out.println("权重减少后距离: " + id.getDistance(3));
    }
}