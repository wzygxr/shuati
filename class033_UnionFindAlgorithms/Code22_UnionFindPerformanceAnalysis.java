package class056;

import java.util.*;
import java.util.concurrent.*;

/**
 * 并查集性能分析与优化实验
 * 本文件包含并查集各种实现的性能对比和优化实验
 */
public class Code22_UnionFindPerformanceAnalysis {
    
    /**
     * 1. 不同并查集实现的性能对比
     */
    public static void compareUnionFindImplementations() {
        System.out.println("=== 不同并查集实现性能对比 ===");
        
        int n = 1000000;  // 100万节点
        int operations = 2000000; // 200万次操作
        
        // 生成随机操作序列
        int[][] operationsSeq = generateRandomOperations(n, operations);
        
        // 测试不同实现的性能
        testImplementation("基础并查集（无优化）", new BasicUnionFind(n), operationsSeq);
        testImplementation("路径压缩并查集", new PathCompressionUnionFind(n), operationsSeq);
        testImplementation("按秩合并并查集", new UnionByRankUnionFind(n), operationsSeq);
        testImplementation("路径压缩+按秩合并", new OptimizedUnionFind(n), operationsSeq);
    }
    
    /**
     * 基础并查集（无优化）
     */
    static class BasicUnionFind {
        protected int[] parent;
        
        public BasicUnionFind(int n) {
            parent = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        public int find(int x) {
            while (parent[x] != x) {
                x = parent[x];
            }
            return x;
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                parent[rootY] = rootX;
            }
        }
        
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }
    
    /**
     * 路径压缩并查集
     */
    static class PathCompressionUnionFind extends BasicUnionFind {
        public PathCompressionUnionFind(int n) {
            super(n);
        }
        
        @Override
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
    }
    
    /**
     * 按秩合并并查集
     */
    static class UnionByRankUnionFind extends BasicUnionFind {
        private int[] rank;
        
        public UnionByRankUnionFind(int n) {
            super(n);
            rank = new int[n];
            Arrays.fill(rank, 1);
        }
        
        @Override
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }
    
    /**
     * 优化版并查集（路径压缩 + 按秩合并）
     */
    static class OptimizedUnionFind extends UnionByRankUnionFind {
        public OptimizedUnionFind(int n) {
            super(n);
        }
        
        @Override
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
    }
    
    /**
     * 性能测试方法
     */
    private static void testImplementation(String name, BasicUnionFind uf, int[][] operations) {
        long startTime = System.currentTimeMillis();
        
        for (int[] op : operations) {
            if (op[0] == 0) { // union操作
                uf.union(op[1], op[2]);
            } else { // find操作
                uf.isConnected(op[1], op[2]);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.printf("%s: %d ms%n", name, endTime - startTime);
    }
    
    /**
     * 生成随机操作序列
     */
    private static int[][] generateRandomOperations(int n, int count) {
        Random random = new Random();
        int[][] operations = new int[count][3];
        
        for (int i = 0; i < count; i++) {
            int type = random.nextInt(2); // 0: union, 1: find
            int x = random.nextInt(n);
            int y = random.nextInt(n);
            operations[i] = new int[]{type, x, y};
        }
        
        return operations;
    }
    
    /**
     * 2. 并查集在不同数据分布下的性能分析
     */
    public static void analyzePerformanceUnderDifferentDistributions() {
        System.out.println("\n=== 不同数据分布下的性能分析 ===");
        
        int n = 100000;
        int operations = 500000;
        
        // 测试不同数据分布
        testDistribution("随机分布", n, operations, Distribution.RANDOM);
        testDistribution("链式分布", n, operations, Distribution.CHAIN);
        testDistribution("星形分布", n, operations, Distribution.STAR);
        testDistribution("完全二分分布", n, operations, Distribution.BIPARTITE);
    }
    
    enum Distribution {
        RANDOM, CHAIN, STAR, BIPARTITE
    }
    
    private static void testDistribution(String name, int n, int operations, Distribution dist) {
        int[][] operationsSeq = generateOperationsByDistribution(n, operations, dist);
        
        long startTime = System.currentTimeMillis();
        OptimizedUnionFind uf = new OptimizedUnionFind(n);
        
        for (int[] op : operationsSeq) {
            if (op[0] == 0) {
                uf.union(op[1], op[2]);
            } else {
                uf.isConnected(op[1], op[2]);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.printf("%s: %d ms%n", name, endTime - startTime);
    }
    
    private static int[][] generateOperationsByDistribution(int n, int count, Distribution dist) {
        Random random = new Random();
        int[][] operations = new int[count][3];
        
        switch (dist) {
            case RANDOM:
                return generateRandomOperations(n, count);
                
            case CHAIN:
                // 生成链式操作：连续合并形成长链
                for (int i = 0; i < count; i++) {
                    if (i < n - 1) {
                        operations[i] = new int[]{0, i, i + 1}; // 形成链
                    } else {
                        operations[i] = new int[]{1, 0, n - 1}; // 查询链的两端
                    }
                }
                break;
                
            case STAR:
                // 生成星形操作：所有节点连接到中心节点
                int center = 0;
                for (int i = 0; i < count; i++) {
                    if (i < n - 1) {
                        operations[i] = new int[]{0, center, i + 1}; // 连接到中心
                    } else {
                        operations[i] = new int[]{1, random.nextInt(n), random.nextInt(n)};
                    }
                }
                break;
                
            case BIPARTITE:
                // 生成二分图操作：交替合并
                for (int i = 0; i < count; i++) {
                    if (i < Math.min(count, n/2)) {
                        int group1 = i * 2;
                        int group2 = i * 2 + 1;
                        if (group2 < n) {
                            operations[i] = new int[]{0, group1, group2};
                        } else {
                            operations[i] = new int[]{1, random.nextInt(n), random.nextInt(n)};
                        }
                    } else {
                        operations[i] = new int[]{1, random.nextInt(n), random.nextInt(n)};
                    }
                }
                break;
        }
        
        return operations;
    }
    
    /**
     * 3. 内存使用分析
     */
    public static void analyzeMemoryUsage() {
        System.out.println("\n=== 内存使用分析 ===");
        
        int[] sizes = {1000, 10000, 100000, 1000000};
        
        for (int size : sizes) {
            long memoryBefore = getMemoryUsage();
            
            // 创建并查集实例
            OptimizedUnionFind uf = new OptimizedUnionFind(size);
            
            long memoryAfter = getMemoryUsage();
            long memoryUsed = memoryAfter - memoryBefore;
            
            System.out.printf("节点数: %d, 内存使用: %d bytes, 平均每个节点: %.2f bytes%n",
                    size, memoryUsed, (double)memoryUsed / size);
        }
    }
    
    private static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    /**
     * 4. 多线程环境下的并查集性能
     */
    public static void analyzeConcurrentPerformance() throws InterruptedException {
        System.out.println("\n=== 多线程性能分析 ===");
        
        int n = 100000;
        int threads = 4;
        int operationsPerThread = 100000;
        
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        OptimizedUnionFind uf = new OptimizedUnionFind(n);
        
        long startTime = System.currentTimeMillis();
        
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            futures.add(executor.submit(new UnionFindTask(uf, n, operationsPerThread)));
        }
        
        // 等待所有任务完成
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        long endTime = System.currentTimeMillis();
        executor.shutdown();
        
        System.out.printf("多线程(%d线程)处理时间: %d ms%n", threads, endTime - startTime);
    }
    
    static class UnionFindTask implements Runnable {
        private final OptimizedUnionFind uf;
        private final int n;
        private final int operations;
        private final Random random = new Random();
        
        public UnionFindTask(OptimizedUnionFind uf, int n, int operations) {
            this.uf = uf;
            this.n = n;
            this.operations = operations;
        }
        
        @Override
        public void run() {
            for (int i = 0; i < operations; i++) {
                int x = random.nextInt(n);
                int y = random.nextInt(n);
                
                if (random.nextBoolean()) {
                    uf.union(x, y);
                } else {
                    uf.isConnected(x, y);
                }
            }
        }
    }
    
    /**
     * 5. 实际应用场景性能测试
     */
    public static void testRealWorldScenarios() {
        System.out.println("\n=== 实际应用场景性能测试 ===");
        
        // 场景1: 社交网络好友关系
        testSocialNetworkScenario();
        
        // 场景2: 图像连通区域标记
        testImageConnectedComponentsScenario();
        
        // 场景3: 最小生成树算法
        testMSTScenario();
    }
    
    private static void testSocialNetworkScenario() {
        System.out.println("社交网络场景测试:");
        
        int users = 50000;
        int friendships = 200000;
        int queries = 100000;
        
        // 生成社交网络数据
        int[][] friendshipsData = generateSocialNetworkData(users, friendships);
        int[][] queriesData = generateQueries(users, queries);
        
        long startTime = System.currentTimeMillis();
        
        OptimizedUnionFind uf = new OptimizedUnionFind(users);
        
        // 建立好友关系
        for (int[] friendship : friendshipsData) {
            uf.union(friendship[0], friendship[1]);
        }
        
        // 处理查询
        int connectedCount = 0;
        for (int[] query : queriesData) {
            if (uf.isConnected(query[0], query[1])) {
                connectedCount++;
            }
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.printf("用户数: %d, 好友关系: %d, 查询数: %d, 处理时间: %d ms, 连通查询: %d%n",
                users, friendships, queries, endTime - startTime, connectedCount);
    }
    
    private static int[][] generateSocialNetworkData(int users, int friendships) {
        Random random = new Random();
        int[][] data = new int[friendships][2];
        
        for (int i = 0; i < friendships; i++) {
            int u = random.nextInt(users);
            int v = random.nextInt(users);
            while (u == v) {
                v = random.nextInt(users);
            }
            data[i] = new int[]{u, v};
        }
        
        return data;
    }
    
    private static int[][] generateQueries(int users, int queries) {
        Random random = new Random();
        int[][] data = new int[queries][2];
        
        for (int i = 0; i < queries; i++) {
            int u = random.nextInt(users);
            int v = random.nextInt(users);
            data[i] = new int[]{u, v};
        }
        
        return data;
    }
    
    private static void testImageConnectedComponentsScenario() {
        System.out.println("图像连通区域标记测试:");
        
        int width = 1000;
        int height = 1000;
        int totalPixels = width * height;
        
        // 生成随机二值图像
        Random random = new Random();
        int[] image = new int[totalPixels];
        for (int i = 0; i < totalPixels; i++) {
            image[i] = random.nextDouble() < 0.3 ? 1 : 0; // 30%的概率为前景
        }
        
        long startTime = System.currentTimeMillis();
        
        // 使用并查集标记连通区域
        OptimizedUnionFind uf = new OptimizedUnionFind(totalPixels);
        
        // 连接相邻像素
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int index = i * width + j;
                if (image[index] == 1) {
                    // 检查右方像素
                    if (j < width - 1 && image[index + 1] == 1) {
                        uf.union(index, index + 1);
                    }
                    // 检查下方像素
                    if (i < height - 1 && image[index + width] == 1) {
                        uf.union(index, index + width);
                    }
                }
            }
        }
        
        // 统计连通区域数量
        Set<Integer> components = new HashSet<>();
        for (int i = 0; i < totalPixels; i++) {
            if (image[i] == 1) {
                components.add(uf.find(i));
            }
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.printf("图像大小: %dx%d, 连通区域数: %d, 处理时间: %d ms%n",
                width, height, components.size(), endTime - startTime);
    }
    
    private static void testMSTScenario() {
        System.out.println("最小生成树算法测试:");
        
        int vertices = 10000;
        int edges = 50000;
        
        // 生成随机图
        int[][] graphEdges = generateGraphEdges(vertices, edges);
        
        long startTime = System.currentTimeMillis();
        
        // 使用Kruskal算法求最小生成树
        Arrays.sort(graphEdges, (a, b) -> Integer.compare(a[2], b[2]));
        
        OptimizedUnionFind uf = new OptimizedUnionFind(vertices);
        int mstEdges = 0;
        int totalWeight = 0;
        
        for (int[] edge : graphEdges) {
            if (mstEdges == vertices - 1) break;
            
            if (!uf.isConnected(edge[0], edge[1])) {
                uf.union(edge[0], edge[1]);
                totalWeight += edge[2];
                mstEdges++;
            }
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.printf("顶点数: %d, 边数: %d, MST边数: %d, 总权重: %d, 处理时间: %d ms%n",
                vertices, edges, mstEdges, totalWeight, endTime - startTime);
    }
    
    private static int[][] generateGraphEdges(int vertices, int edges) {
        Random random = new Random();
        int[][] graphEdges = new int[edges][3];
        
        for (int i = 0; i < edges; i++) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);
            while (u == v) {
                v = random.nextInt(vertices);
            }
            int weight = random.nextInt(1000) + 1;
            graphEdges[i] = new int[]{u, v, weight};
        }
        
        return graphEdges;
    }
    
    /**
     * 6. 性能优化建议总结
     */
    public static void performanceOptimizationSummary() {
        System.out.println("\n=== 性能优化建议总结 ===");
        
        System.out.println("1. 算法选择优化:");
        System.out.println("   - 优先使用路径压缩 + 按秩合并的优化版本");
        System.out.println("   - 对于特定场景，考虑使用带权并查集或可撤销并查集");
        
        System.out.println("2. 内存优化:");
        System.out.println("   - 使用基本类型数组代替对象数组");
        System.out.println("   - 对于超大数组，考虑分块存储或使用更紧凑的数据结构");
        
        System.out.println("3. 并发优化:");
        System.out.println("   - 在多线程环境下，考虑使用线程本地存储");
        System.out.println("   - 或者使用并发安全的数据结构包装");
        
        System.out.println("4. 实际应用优化:");
        System.out.println("   - 根据数据分布特点选择合适的数据结构");
        System.out.println("   - 对于稀疏图，考虑使用其他数据结构");
        System.out.println("   - 预处理数据，减少运行时计算");
    }
    
    // 主测试方法
    public static void main(String[] args) throws InterruptedException {
        System.out.println("并查集性能分析与优化实验");
        System.out.println("=========================");
        
        // 运行各种性能测试
        compareUnionFindImplementations();
        analyzePerformanceUnderDifferentDistributions();
        analyzeMemoryUsage();
        analyzeConcurrentPerformance();
        testRealWorldScenarios();
        performanceOptimizationSummary();
        
        System.out.println("\n性能分析完成！");
    }
    
    /**
     * 关键性能指标监控:
     * 1. 时间复杂度: 关注最坏情况和平均情况
     * 2. 空间复杂度: 关注内存使用和缓存友好性
     * 3. 实际运行时间: 在不同规模和数据分布下的表现
     * 4. 并发性能: 多线程环境下的扩展性
     * 
     * 调试和问题定位技巧:
     * 1. 使用性能分析工具（如JProfiler、VisualVM）
     * 2. 添加详细的日志输出
     * 3. 编写基准测试对比不同实现
     * 4. 监控内存使用和GC情况
     */
}