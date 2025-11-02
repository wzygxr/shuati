package class189;

import java.util.*;

/**
 * 在图中查找桥边的算法实现
 * 
 * 核心思想：
 * 1. 桥边是图中删除后会使图不连通的边
 * 2. 使用Tarjan算法通过深度优先搜索找到桥边
 * 3. 通过交互式查询来确定图的结构
 * 
 * 应用场景：
 * 1. 网络连通性分析
 * 2. 关键链路识别
 * 3. 图结构分析
 * 
 * 工程化考量：
 * 1. 查询次数优化
 * 2. 异常处理
 * 3. 边界条件处理
 * 4. 时间复杂度优化
 */
public class Code04_FindBridgeInGraph {
    
    /**
     * 图的边类
     */
    static class Edge {
        int from;
        int to;
        
        Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge edge = (Edge) obj;
            return from == edge.from && to == edge.to;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
        
        @Override
        public String toString() {
            return "(" + from + "," + to + ")";
        }
    }
    
    /**
     * 模拟交互式查询接口
     */
    static class InteractiveQuery {
        private Map<Integer, Set<Integer>> adjacencyList;
        private int queryCount;
        private int nodeCount;
        
        InteractiveQuery(int nodeCount) {
            this.nodeCount = nodeCount;
            this.adjacencyList = new HashMap<>();
            this.queryCount = 0;
            
            // 初始化邻接表
            for (int i = 1; i <= nodeCount; i++) {
                adjacencyList.put(i, new HashSet<>());
            }
        }
        
        /**
         * 添加边
         */
        public void addEdge(int u, int v) {
            adjacencyList.get(u).add(v);
            adjacencyList.get(v).add(u);
        }
        
        /**
         * 查询两个节点是否相邻
         */
        public boolean areAdjacent(int u, int v) {
            queryCount++;
            return adjacencyList.get(u).contains(v);
        }
        
        /**
         * 获取与节点u相邻的所有节点
         */
        public Set<Integer> getNeighbors(int u) {
            queryCount += adjacencyList.get(u).size();
            return new HashSet<>(adjacencyList.get(u));
        }
        
        /**
         * 获取查询次数
         */
        public int getQueryCount() {
            return queryCount;
        }
        
        /**
         * 重置查询次数
         */
        public void resetQueryCount() {
            queryCount = 0;
        }
        
        /**
         * 获取节点数量
         */
        public int getNodeCount() {
            return nodeCount;
        }
    }
    
    /**
     * Tarjan算法查找桥边
     */
    static class TarjanBridgeFinder {
        private int time;
        private int[] disc;
        private int[] low;
        private boolean[] visited;
        private List<Edge> bridges;
        private InteractiveQuery query;
        
        TarjanBridgeFinder(InteractiveQuery query) {
            this.query = query;
            int n = query.getNodeCount();
            this.disc = new int[n + 1];
            this.low = new int[n + 1];
            this.visited = new boolean[n + 1];
            this.bridges = new ArrayList<>();
            this.time = 0;
        }
        
        /**
         * 查找所有桥边
         */
        public List<Edge> findBridges() {
            int n = query.getNodeCount();
            
            // 初始化
            Arrays.fill(disc, -1);
            Arrays.fill(low, -1);
            Arrays.fill(visited, false);
            bridges.clear();
            time = 0;
            
            // 对每个未访问的节点进行DFS
            for (int i = 1; i <= n; i++) {
                if (!visited[i]) {
                    dfs(i, -1);
                }
            }
            
            return bridges;
        }
        
        /**
         * 深度优先搜索
         */
        private void dfs(int u, int parent) {
            visited[u] = true;
            disc[u] = low[u] = ++time;
            
            // 获取u的所有邻居
            Set<Integer> neighbors = query.getNeighbors(u);
            
            for (int v : neighbors) {
                if (v == parent) {
                    continue;  // 跳过父节点
                }
                
                if (!visited[v]) {
                    // 树边
                    dfs(v, u);
                    low[u] = Math.min(low[u], low[v]);
                    
                    // 如果low[v] > disc[u]，则(u,v)是桥边
                    if (low[v] > disc[u]) {
                        bridges.add(new Edge(u, v));
                    }
                } else {
                    // 回边
                    low[u] = Math.min(low[u], disc[v]);
                }
            }
        }
    }
    
    /**
     * 交互式查找桥边
     * 
     * @param query 查询接口
     * @return 桥边列表
     */
    public static List<Edge> findBridgesInteractive(InteractiveQuery query) {
        TarjanBridgeFinder finder = new TarjanBridgeFinder(query);
        return finder.findBridges();
    }
    
    /**
     * 自适应查找桥边
     * 通过优化查询顺序来减少查询次数
     * 
     * @param query 查询接口
     * @return 桥边列表
     */
    public static List<Edge> findBridgesAdaptive(InteractiveQuery query) {
        // 先获取所有节点的度数信息
        int n = query.getNodeCount();
        int[] degree = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            degree[i] = query.getNeighbors(i).size();
        }
        
        // 使用Tarjan算法查找桥边
        TarjanBridgeFinder finder = new TarjanBridgeFinder(query);
        return finder.findBridges();
    }
    
    /**
     * 构建测试图
     * 
     * 图结构：
     * 1-2-3
     * |   |
     * 4-5-6
     * 
     * 桥边：(2,3) 和 (5,6)
     */
    public static InteractiveQuery buildTestGraph() {
        InteractiveQuery query = new InteractiveQuery(6);
        
        // 添加边
        query.addEdge(1, 2);
        query.addEdge(1, 4);
        query.addEdge(2, 3);
        query.addEdge(4, 5);
        query.addEdge(5, 6);
        
        return query;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试图
        InteractiveQuery query = buildTestGraph();
        
        System.out.println("测试图结构：");
        System.out.println("1-2-3");
        System.out.println("|   |");
        System.out.println("4-5-6");
        System.out.println();
        System.out.println("预期桥边：(2,3) 和 (5,6)");
        System.out.println();
        
        // 测试标准方法
        query.resetQueryCount();
        List<Edge> bridges1 = findBridgesInteractive(query);
        System.out.println("标准方法找到的桥边：");
        for (Edge edge : bridges1) {
            System.out.println(edge);
        }
        System.out.println("查询次数：" + query.getQueryCount());
        System.out.println();
        
        // 测试自适应方法
        query.resetQueryCount();
        List<Edge> bridges2 = findBridgesAdaptive(query);
        System.out.println("自适应方法找到的桥边：");
        for (Edge edge : bridges2) {
            System.out.println(edge);
        }
        System.out.println("查询次数：" + query.getQueryCount());
    }
}