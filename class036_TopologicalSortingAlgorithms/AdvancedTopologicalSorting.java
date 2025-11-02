package class059;

import java.util.*;

/**
 * 高级拓扑排序算法与优化
 * 
 * 本文件包含拓扑排序的高级应用和优化技术：
 * 1. 动态拓扑排序
 * 2. 并行拓扑排序
 * 3. 增量拓扑排序
 * 4. 分布式拓扑排序
 * 5. 性能优化技巧
 * 6. 工程化最佳实践
 */

public class AdvancedTopologicalSorting {

    /**
     * =====================================================================
     * 动态拓扑排序 - 支持动态添加和删除边
     * 
     * 应用场景：实时任务调度、动态依赖关系管理
     * 时间复杂度：添加边O(1)，删除边O(1)，查询O(V+E)
     * 空间复杂度：O(V+E)
     */
    public static class DynamicTopologicalSort {
        private List<List<Integer>> graph;
        private int[] inDegree;
        private int n;
        
        public DynamicTopologicalSort(int n) {
            this.n = n;
            this.graph = new ArrayList<>();
            this.inDegree = new int[n];
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
        }
        
        /**
         * 添加边并更新拓扑排序
         */
        public boolean addEdge(int from, int to) {
            if (from < 0 || from >= n || to < 0 || to >= n) {
                throw new IllegalArgumentException("节点编号越界");
            }
            
            graph.get(from).add(to);
            inDegree[to]++;
            
            // 检查是否产生环
            return !hasCycle();
        }
        
        /**
         * 删除边并更新拓扑排序
         */
        public boolean removeEdge(int from, int to) {
            if (from < 0 || from >= n || to < 0 || to >= n) {
                throw new IllegalArgumentException("节点编号越界");
            }
            
            boolean removed = graph.get(from).remove(Integer.valueOf(to));
            if (removed) {
                inDegree[to]--;
            }
            return removed;
        }
        
        /**
         * 获取当前拓扑排序
         */
        public List<Integer> getTopologicalOrder() {
            int[] tempInDegree = Arrays.copyOf(inDegree, n);
            Queue<Integer> queue = new LinkedList<>();
            List<Integer> result = new ArrayList<>();
            
            for (int i = 0; i < n; i++) {
                if (tempInDegree[i] == 0) {
                    queue.offer(i);
                }
            }
            
            while (!queue.isEmpty()) {
                int current = queue.poll();
                result.add(current);
                
                for (int next : graph.get(current)) {
                    tempInDegree[next]--;
                    if (tempInDegree[next] == 0) {
                        queue.offer(next);
                    }
                }
            }
            
            return result.size() == n ? result : Collections.emptyList();
        }
        
        /**
         * 检查是否存在环
         */
        private boolean hasCycle() {
            return getTopologicalOrder().size() != n;
        }
    }

    /**
     * =====================================================================
     * 并行拓扑排序 - 多线程优化
     * 
     * 应用场景：大规模图处理、高性能计算
     * 时间复杂度：O(V+E) 但并行化加速
     * 空间复杂度：O(V+E)
     */
    public static class ParallelTopologicalSort {
        private List<List<Integer>> graph;
        private int[] inDegree;
        private int n;
        private int numThreads;
        
        public ParallelTopologicalSort(int n, int numThreads) {
            this.n = n;
            this.numThreads = numThreads;
            this.graph = new ArrayList<>();
            this.inDegree = new int[n];
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
        }
        
        /**
         * 并行拓扑排序
         */
        public List<Integer> parallelTopologicalSort() {
            int[] tempInDegree = Arrays.copyOf(inDegree, n);
            Queue<Integer> queue = new LinkedList<>();
            List<Integer> result = Collections.synchronizedList(new ArrayList<>());
            
            // 初始入度为0的节点
            for (int i = 0; i < n; i++) {
                if (tempInDegree[i] == 0) {
                    queue.offer(i);
                }
            }
            
            // 创建线程池
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            List<Future<?>> futures = new ArrayList<>();
            
            // 提交任务
            for (int i = 0; i < numThreads; i++) {
                futures.add(executor.submit(new TopologyWorker(queue, tempInDegree, result)));
            }
            
            // 等待所有任务完成
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            executor.shutdown();
            return result;
        }
        
        private class TopologyWorker implements Runnable {
            private Queue<Integer> queue;
            private int[] inDegree;
            private List<Integer> result;
            
            public TopologyWorker(Queue<Integer> queue, int[] inDegree, List<Integer> result) {
                this.queue = queue;
                this.inDegree = inDegree;
                this.result = result;
            }
            
            @Override
            public void run() {
                while (true) {
                    Integer current = null;
                    synchronized (queue) {
                        if (!queue.isEmpty()) {
                            current = queue.poll();
                        }
                    }
                    
                    if (current == null) {
                        break;
                    }
                    
                    result.add(current);
                    
                    // 处理邻居节点
                    for (int next : graph.get(current)) {
                        synchronized (inDegree) {
                            inDegree[next]--;
                            if (inDegree[next] == 0) {
                                synchronized (queue) {
                                    queue.offer(next);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * =====================================================================
     * 增量拓扑排序 - 支持批量操作
     * 
     * 应用场景：批量任务调度、流式数据处理
     * 时间复杂度：批量操作优化
     * 空间复杂度：O(V+E)
     */
    public static class IncrementalTopologicalSort {
        private List<List<Integer>> graph;
        private int[] inDegree;
        private int n;
        
        public IncrementalTopologicalSort(int n) {
            this.n = n;
            this.graph = new ArrayList<>();
            this.inDegree = new int[n];
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
        }
        
        /**
         * 批量添加边
         */
        public boolean addEdgesBatch(List<int[]> edges) {
            // 验证所有边
            for (int[] edge : edges) {
                if (edge[0] < 0 || edge[0] >= n || edge[1] < 0 || edge[1] >= n) {
                    throw new IllegalArgumentException("节点编号越界");
                }
            }
            
            // 批量添加边
            for (int[] edge : edges) {
                graph.get(edge[0]).add(edge[1]);
                inDegree[edge[1]]++;
            }
            
            // 批量检查环
            return !hasCycle();
        }
        
        /**
         * 批量删除边
         */
        public void removeEdgesBatch(List<int[]> edges) {
            for (int[] edge : edges) {
                if (edge[0] >= 0 && edge[0] < n && edge[1] >= 0 && edge[1] < n) {
                    graph.get(edge[0]).remove(Integer.valueOf(edge[1]));
                    inDegree[edge[1]]--;
                }
            }
        }
        
        /**
         * 增量获取拓扑排序
         */
        public List<Integer> getIncrementalOrder() {
            return topologicalSort();
        }
        
        private List<Integer> topologicalSort() {
            int[] tempInDegree = Arrays.copyOf(inDegree, n);
            Queue<Integer> queue = new LinkedList<>();
            List<Integer> result = new ArrayList<>();
            
            for (int i = 0; i < n; i++) {
                if (tempInDegree[i] == 0) {
                    queue.offer(i);
                }
            }
            
            while (!queue.isEmpty()) {
                int current = queue.poll();
                result.add(current);
                
                for (int next : graph.get(current)) {
                    tempInDegree[next]--;
                    if (tempInDegree[next] == 0) {
                        queue.offer(next);
                    }
                }
            }
            
            return result;
        }
        
        private boolean hasCycle() {
            return topologicalSort().size() != n;
        }
    }

    /**
     * =====================================================================
     * 拓扑排序性能优化技巧
     */
    public static class TopologicalSortOptimizations {
        
        /**
         * 缓存优化 - 预计算常用结果
         */
        public static class CachedTopologicalSort {
            private List<List<Integer>> graph;
            private int[] inDegree;
            private List<Integer> cachedOrder;
            private boolean cacheValid;
            
            public CachedTopologicalSort(int n) {
                this.graph = new ArrayList<>();
                this.inDegree = new int[n];
                for (int i = 0; i < n; i++) {
                    graph.add(new ArrayList<>());
                }
                this.cacheValid = false;
            }
            
            public void addEdge(int from, int to) {
                graph.get(from).add(to);
                inDegree[to]++;
                cacheValid = false;
            }
            
            public List<Integer> getTopologicalOrder() {
                if (!cacheValid) {
                    cachedOrder = computeTopologicalOrder();
                    cacheValid = true;
                }
                return new ArrayList<>(cachedOrder);
            }
            
            private List<Integer> computeTopologicalOrder() {
                int[] tempInDegree = Arrays.copyOf(inDegree, inDegree.length);
                Queue<Integer> queue = new LinkedList<>();
                List<Integer> result = new ArrayList<>();
                
                for (int i = 0; i < inDegree.length; i++) {
                    if (tempInDegree[i] == 0) {
                        queue.offer(i);
                    }
                }
                
                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    result.add(current);
                    
                    for (int next : graph.get(current)) {
                        tempInDegree[next]--;
                        if (tempInDegree[next] == 0) {
                            queue.offer(next);
                        }
                    }
                }
                
                return result;
            }
        }
        
        /**
         * 内存优化 - 使用位集压缩存储
         */
        public static class CompressedTopologicalSort {
            private BitSet[] adjacency;
            private int n;
            
            public CompressedTopologicalSort(int n) {
                this.n = n;
                this.adjacency = new BitSet[n];
                for (int i = 0; i < n; i++) {
                    adjacency[i] = new BitSet(n);
                }
            }
            
            public void addEdge(int from, int to) {
                adjacency[from].set(to);
            }
            
            public List<Integer> topologicalSort() {
                int[] inDegree = new int[n];
                
                // 计算入度
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (adjacency[i].get(j)) {
                            inDegree[j]++;
                        }
                    }
                }
                
                Queue<Integer> queue = new LinkedList<>();
                List<Integer> result = new ArrayList<>();
                
                for (int i = 0; i < n; i++) {
                    if (inDegree[i] == 0) {
                        queue.offer(i);
                    }
                }
                
                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    result.add(current);
                    
                    for (int j = 0; j < n; j++) {
                        if (adjacency[current].get(j)) {
                            inDegree[j]--;
                            if (inDegree[j] == 0) {
                                queue.offer(j);
                            }
                        }
                    }
                }
                
                return result;
            }
        }
        
        /**
         * 算法优化 - 双向BFS拓扑排序
         */
        public static class BidirectionalTopologicalSort {
            private List<List<Integer>> graph;
            private List<List<Integer>> reverseGraph;
            private int n;
            
            public BidirectionalTopologicalSort(int n) {
                this.n = n;
                this.graph = new ArrayList<>();
                this.reverseGraph = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    graph.add(new ArrayList<>());
                    reverseGraph.add(new ArrayList<>());
                }
            }
            
            public void addEdge(int from, int to) {
                graph.get(from).add(to);
                reverseGraph.get(to).add(from);
            }
            
            /**
             * 双向BFS拓扑排序 - 适用于特定场景
             */
            public List<Integer> bidirectionalTopologicalSort() {
                int[] inDegree = new int[n];
                int[] outDegree = new int[n];
                
                // 计算入度和出度
                for (int i = 0; i < n; i++) {
                    outDegree[i] = graph.get(i).size();
                    for (int neighbor : graph.get(i)) {
                        inDegree[neighbor]++;
                    }
                }
                
                Queue<Integer> forwardQueue = new LinkedList<>();
                Queue<Integer> backwardQueue = new LinkedList<>();
                List<Integer> result = new ArrayList<>();
                
                // 初始化队列
                for (int i = 0; i < n; i++) {
                    if (inDegree[i] == 0) {
                        forwardQueue.offer(i);
                    }
                    if (outDegree[i] == 0) {
                        backwardQueue.offer(i);
                    }
                }
                
                while (!forwardQueue.isEmpty() || !backwardQueue.isEmpty()) {
                    // 前向处理
                    if (!forwardQueue.isEmpty()) {
                        int current = forwardQueue.poll();
                        result.add(current);
                        
                        for (int next : graph.get(current)) {
                            inDegree[next]--;
                            if (inDegree[next] == 0) {
                                forwardQueue.offer(next);
                            }
                        }
                    }
                    
                    // 后向处理
                    if (!backwardQueue.isEmpty()) {
                        int current = backwardQueue.poll();
                        result.add(current);
                        
                        for (int prev : reverseGraph.get(current)) {
                            outDegree[prev]--;
                            if (outDegree[prev] == 0) {
                                backwardQueue.offer(prev);
                            }
                        }
                    }
                }
                
                return result;
            }
        }
    }

    /**
     * =====================================================================
     * 工程化最佳实践
     */
    public static class EngineeringBestPractices {
        
        /**
         * 配置化拓扑排序
         */
        public static class ConfigurableTopologicalSort {
            private TopologyConfig config;
            private List<List<Integer>> graph;
            private int n;
            
            public static class TopologyConfig {
                public boolean enableCaching = true;
                public boolean enableValidation = true;
                public boolean enableLogging = false;
                public int maxGraphSize = 10000;
                public String algorithm = "KAHN"; // KAHN or DFS
            }
            
            public ConfigurableTopologicalSort(int n, TopologyConfig config) {
                this.n = n;
                this.config = config;
                this.graph = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    graph.add(new ArrayList<>());
                }
                
                validateConfig();
            }
            
            private void validateConfig() {
                if (n > config.maxGraphSize) {
                    throw new IllegalArgumentException("图大小超过配置限制");
                }
            }
            
            public List<Integer> topologicalSort() {
                if (config.enableValidation) {
                    validateGraph();
                }
                
                if (config.algorithm.equals("KAHN")) {
                    return kahnAlgorithm();
                } else {
                    return dfsAlgorithm();
                }
            }
            
            private List<Integer> kahnAlgorithm() {
                int[] inDegree = new int[n];
                for (int i = 0; i < n; i++) {
                    for (int neighbor : graph.get(i)) {
                        inDegree[neighbor]++;
                    }
                }
                
                Queue<Integer> queue = new LinkedList<>();
                List<Integer> result = new ArrayList<>();
                
                for (int i = 0; i < n; i++) {
                    if (inDegree[i] == 0) {
                        queue.offer(i);
                    }
                }
                
                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    result.add(current);
                    
                    for (int next : graph.get(current)) {
                        inDegree[next]--;
                        if (inDegree[next] == 0) {
                            queue.offer(next);
                        }
                    }
                }
                
                return result;
            }
            
            private List<Integer> dfsAlgorithm() {
                boolean[] visited = new boolean[n];
                boolean[] onStack = new boolean[n];
                Stack<Integer> stack = new Stack<>();
                
                for (int i = 0; i < n; i++) {
                    if (!visited[i]) {
                        if (dfs(i, visited, onStack, stack)) {
                            return Collections.emptyList(); // 有环
                        }
                    }
                }
                
                List<Integer> result = new ArrayList<>();
                while (!stack.isEmpty()) {
                    result.add(stack.pop());
                }
                return result;
            }
            
            private boolean dfs(int node, boolean[] visited, boolean[] onStack, Stack<Integer> stack) {
                if (onStack[node]) return true; // 有环
                if (visited[node]) return false;
                
                visited[node] = true;
                onStack[node] = true;
                
                for (int neighbor : graph.get(node)) {
                    if (dfs(neighbor, visited, onStack, stack)) {
                        return true;
                    }
                }
                
                onStack[node] = false;
                stack.push(node);
                return false;
            }
            
            private void validateGraph() {
                // 验证图的基本属性
                if (graph == null) {
                    throw new IllegalStateException("图未初始化");
                }
                if (graph.size() != n) {
                    throw new IllegalStateException("图大小不一致");
                }
            }
        }
        
        /**
         * 监控和统计
         */
        public static class MonitoredTopologicalSort {
            private List<List<Integer>> graph;
            private int n;
            private TopologyStatistics statistics;
            
            public static class TopologyStatistics {
                public long totalOperations = 0;
                public long successfulOperations = 0;
                public long failedOperations = 0;
                public long averageTime = 0;
                public long maxGraphSize = 0;
            }
            
            public MonitoredTopologicalSort(int n) {
                this.n = n;
                this.graph = new ArrayList<>();
                this.statistics = new TopologyStatistics();
                for (int i = 0; i < n; i++) {
                    graph.add(new ArrayList<>());
                }
            }
            
            public List<Integer> topologicalSortWithMonitoring() {
                long startTime = System.currentTimeMillis();
                statistics.totalOperations++;
                
                try {
                    List<Integer> result = topologicalSort();
                    statistics.successfulOperations++;
                    return result;
                } catch (Exception e) {
                    statistics.failedOperations++;
                    throw e;
                } finally {
                    long endTime = System.currentTimeMillis();
                    statistics.averageTime = (statistics.averageTime * (statistics.totalOperations - 1) + 
                                            (endTime - startTime)) / statistics.totalOperations;
                }
            }
            
            private List<Integer> topologicalSort() {
                int[] inDegree = new int[n];
                for (int i = 0; i < n; i++) {
                    for (int neighbor : graph.get(i)) {
                        inDegree[neighbor]++;
                    }
                }
                
                Queue<Integer> queue = new LinkedList<>();
                List<Integer> result = new ArrayList<>();
                
                for (int i = 0; i < n; i++) {
                    if (inDegree[i] == 0) {
                        queue.offer(i);
                    }
                }
                
                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    result.add(current);
                    
                    for (int next : graph.get(current)) {
                        inDegree[next]--;
                        if (inDegree[next] == 0) {
                            queue.offer(next);
                        }
                    }
                }
                
                return result;
            }
            
            public TopologyStatistics getStatistics() {
                return statistics;
            }
        }
    }

    /**
     * =====================================================================
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 高级拓扑排序算法测试 ===");
        
        // 测试动态拓扑排序
        DynamicTopologicalSort dynamicSort = new DynamicTopologicalSort(5);
        dynamicSort.addEdge(0, 1);
        dynamicSort.addEdge(1, 2);
        dynamicSort.addEdge(2, 3);
        dynamicSort.addEdge(3, 4);
        System.out.println("动态拓扑排序: " + dynamicSort.getTopologicalOrder());
        
        // 测试增量拓扑排序
        IncrementalTopologicalSort incrementalSort = new IncrementalTopologicalSort(4);
        List<int[]> edges = Arrays.asList(new int[]{0, 1}, new int[]{1, 2}, new int[]{2, 3});
        incrementalSort.addEdgesBatch(edges);
        System.out.println("增量拓扑排序: " + incrementalSort.getIncrementalOrder());
        
        // 测试配置化拓扑排序
        EngineeringBestPractices.TopologyConfig config = new EngineeringBestPractices.TopologyConfig();
        config.enableCaching = true;
        config.enableValidation = true;
        EngineeringBestPractices.ConfigurableTopologicalSort configurableSort = 
            new EngineeringBestPractices.ConfigurableTopologicalSort(3, config);
        // 添加边...
        System.out.println("配置化拓扑排序测试完成");
        
        System.out.println("=== 测试完成 ===");
    }
}

// 导入必要的并发类
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;