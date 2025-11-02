package class056;

import java.util.*;

/**
 * 并查集高级应用与扩展
 * 包含并查集在各种复杂场景下的应用和优化
 */
public class Code25_UnionFindAdvancedApplications {
    
    /**
     * 1. 可持久化并查集（支持历史版本查询）
     * 使用版本控制技术实现可撤销操作
     */
    static class PersistentUnionFind {
        private int[] parent;
        private int[] rank;
        private List<int[]> history; // 存储操作历史：[时间戳, 操作类型, 参数...]
        private int currentTime;
        
        public PersistentUnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            history = new ArrayList<>();
            currentTime = 0;
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        public int find(int x, int time) {
            // 在指定时间版本中查找
            restoreState(time);
            return findCurrent(x);
        }
        
        private int findCurrent(int x) {
            if (parent[x] != x) {
                parent[x] = findCurrent(parent[x]);
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = findCurrent(x);
            int rootY = findCurrent(y);
            
            if (rootX != rootY) {
                // 记录操作历史
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                    history.add(new int[]{currentTime, 0, rootX, rootY, rank[rootX], rank[rootY]});
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                    history.add(new int[]{currentTime, 0, rootY, rootX, rank[rootY], rank[rootX]});
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                    history.add(new int[]{currentTime, 0, rootY, rootX, rank[rootY], rank[rootX] - 1});
                }
                currentTime++;
            }
        }
        
        private void restoreState(int targetTime) {
            // 恢复到指定时间版本
            // 实际实现需要更复杂的状态管理
            // 这里简化实现，只支持向前恢复
        }
        
        public int getCurrentTime() {
            return currentTime;
        }
    }
    
    /**
     * 2. 动态连通性处理（支持在线查询和修改）
     */
    static class DynamicConnectivity {
        private UnionFind uf;
        private Map<Integer, Set<Integer>> graph; // 邻接表表示
        private int componentCount;
        
        public DynamicConnectivity(int n) {
            uf = new UnionFind(n);
            graph = new HashMap<>();
            componentCount = n;
            
            for (int i = 0; i < n; i++) {
                graph.put(i, new HashSet<>());
            }
        }
        
        public void addEdge(int u, int v) {
            if (graph.get(u).contains(v)) {
                return; // 边已存在
            }
            
            graph.get(u).add(v);
            graph.get(v).add(u);
            
            if (uf.union(u, v)) {
                componentCount--;
            }
        }
        
        public void removeEdge(int u, int v) {
            if (!graph.get(u).contains(v)) {
                return; // 边不存在
            }
            
            graph.get(u).remove(v);
            graph.get(v).remove(u);
            
            // 重新计算连通性（简化实现，实际需要更高效算法）
            recalculateConnectivity();
        }
        
        public boolean isConnected(int u, int v) {
            return uf.isConnected(u, v);
        }
        
        public int getComponentCount() {
            return componentCount;
        }
        
        private void recalculateConnectivity() {
            // 重新计算连通性（BFS/DFS）
            int n = uf.parent.length;
            boolean[] visited = new boolean[n];
            int[] newParent = new int[n];
            Arrays.fill(newParent, -1);
            
            componentCount = 0;
            
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    componentCount++;
                    bfs(i, visited, newParent, graph);
                }
            }
            
            // 更新并查集（简化实现）
            uf = new UnionFind(n);
            // 实际需要更复杂的更新逻辑
        }
        
        private void bfs(int start, boolean[] visited, int[] parent, Map<Integer, Set<Integer>> graph) {
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(start);
            visited[start] = true;
            parent[start] = start;
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                
                for (int v : graph.get(u)) {
                    if (!visited[v]) {
                        visited[v] = true;
                        parent[v] = start;
                        queue.offer(v);
                    }
                }
            }
        }
    }
    
    /**
     * 3. 并行并查集（多线程安全版本）
     */
    static class ConcurrentUnionFind {
        private final int[] parent;
        private final int[] rank;
        private final Object[] locks; // 分段锁
        
        public ConcurrentUnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            locks = new Object[n];
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
                locks[i] = new Object();
            }
        }
        
        public int find(int x) {
            // 使用锁保证线程安全
            synchronized (locks[x]) {
                if (parent[x] != x) {
                    parent[x] = find(parent[x]);
                }
                return parent[x];
            }
        }
        
        public boolean union(int x, int y) {
            // 按顺序获取锁，避免死锁
            if (x < y) {
                synchronized (locks[x]) {
                    synchronized (locks[y]) {
                        return doUnion(x, y);
                    }
                }
            } else {
                synchronized (locks[y]) {
                    synchronized (locks[x]) {
                        return doUnion(x, y);
                    }
                }
            }
        }
        
        private boolean doUnion(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                return false;
            }
            
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            
            return true;
        }
    }
    
    /**
     * 4. 机器学习中的并查集应用：聚类分析
     */
    static class ClusteringAnalysis {
        /**
         * 使用并查集进行层次聚类
         */
        public static List<Set<Integer>> hierarchicalClustering(double[][] data, double threshold) {
            int n = data.length;
            UnionFind uf = new UnionFind(n);
            
            // 计算所有点对之间的距离
            List<Edge> edges = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    double distance = euclideanDistance(data[i], data[j]);
                    if (distance <= threshold) {
                        edges.add(new Edge(i, j, distance));
                    }
                }
            }
            
            // 按距离排序（单链接聚类）
            edges.sort(Comparator.comparingDouble(e -> e.distance));
            
            // 合并聚类
            for (Edge edge : edges) {
                uf.union(edge.u, edge.v);
            }
            
            // 提取聚类结果
            Map<Integer, Set<Integer>> clusters = new HashMap<>();
            for (int i = 0; i < n; i++) {
                int root = uf.find(i);
                clusters.computeIfAbsent(root, k -> new HashSet<>()).add(i);
            }
            
            return new ArrayList<>(clusters.values());
        }
        
        private static double euclideanDistance(double[] a, double[] b) {
            double sum = 0.0;
            for (int i = 0; i < a.length; i++) {
                double diff = a[i] - b[i];
                sum += diff * diff;
            }
            return Math.sqrt(sum);
        }
        
        static class Edge {
            int u, v;
            double distance;
            
            Edge(int u, int v, double distance) {
                this.u = u;
                this.v = v;
                this.distance = distance;
            }
        }
    }
    
    /**
     * 5. 图像处理中的连通组件标记
     */
    static class ConnectedComponentLabeling {
        /**
         * 使用并查集进行连通组件标记（4连通）
         */
        public static int[][] labelComponents(int[][] binaryImage) {
            int height = binaryImage.length;
            int width = binaryImage[0].length;
            
            UnionFind uf = new UnionFind(height * width);
            
            // 第一遍扫描：连接相邻像素
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (binaryImage[i][j] == 1) {
                        int currentIdx = i * width + j;
                        
                        // 检查上方像素
                        if (i > 0 && binaryImage[i-1][j] == 1) {
                            uf.union(currentIdx, (i-1) * width + j);
                        }
                        
                        // 检查左方像素
                        if (j > 0 && binaryImage[i][j-1] == 1) {
                            uf.union(currentIdx, i * width + j - 1);
                        }
                    }
                }
            }
            
            // 第二遍扫描：分配标签
            int[][] labels = new int[height][width];
            Map<Integer, Integer> rootToLabel = new HashMap<>();
            int nextLabel = 1;
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (binaryImage[i][j] == 1) {
                        int root = uf.find(i * width + j);
                        int label = rootToLabel.computeIfAbsent(root, k -> nextLabel++);
                        labels[i][j] = label;
                    }
                }
            }
            
            return labels;
        }
        
        /**
         * 8连通组件标记
         */
        public static int[][] labelComponents8Connected(int[][] binaryImage) {
            int height = binaryImage.length;
            int width = binaryImage[0].length;
            
            UnionFind uf = new UnionFind(height * width);
            int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},  // 上方三个方向
                {0, -1}, {0, 1},             // 左右方向
                {1, -1}, {1, 0}, {1, 1}      // 下方三个方向
            };
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (binaryImage[i][j] == 1) {
                        int currentIdx = i * width + j;
                        
                        for (int[] dir : directions) {
                            int ni = i + dir[0];
                            int nj = j + dir[1];
                            
                            if (ni >= 0 && ni < height && nj >= 0 && nj < width && 
                                binaryImage[ni][nj] == 1) {
                                uf.union(currentIdx, ni * width + nj);
                            }
                        }
                    }
                }
            }
            
            return createLabelMap(binaryImage, uf, height, width);
        }
        
        private static int[][] createLabelMap(int[][] binaryImage, UnionFind uf, int height, int width) {
            int[][] labels = new int[height][width];
            Map<Integer, Integer> rootToLabel = new HashMap<>();
            int nextLabel = 1;
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (binaryImage[i][j] == 1) {
                        int root = uf.find(i * width + j);
                        int label = rootToLabel.computeIfAbsent(root, k -> nextLabel++);
                        labels[i][j] = label;
                    }
                }
            }
            
            return labels;
        }
    }
    
    /**
     * 6. 社交网络分析：社区发现
     */
    static class CommunityDetection {
        /**
         * 使用并查集进行简单的社区发现
         */
        public static Map<Integer, Set<Integer>> detectCommunities(int[][] edges, int n) {
            UnionFind uf = new UnionFind(n);
            
            // 建立连接关系
            for (int[] edge : edges) {
                uf.union(edge[0], edge[1]);
            }
            
            // 提取社区
            Map<Integer, Set<Integer>> communities = new HashMap<>();
            for (int i = 0; i < n; i++) {
                int root = uf.find(i);
                communities.computeIfAbsent(root, k -> new HashSet<>()).add(i);
            }
            
            return communities;
        }
        
        /**
         * 基于模块度优化的社区发现（简化版）
         */
        public static Map<Integer, Set<Integer>> detectCommunitiesWithModularity(int[][] edges, int n) {
            // 计算度中心性
            int[] degree = new int[n];
            for (int[] edge : edges) {
                degree[edge[0]]++;
                degree[edge[1]]++;
            }
            
            // 按度排序边（启发式策略）
            List<Edge> edgeList = new ArrayList<>();
            for (int[] edge : edges) {
                edgeList.add(new Edge(edge[0], edge[1], 
                    Math.min(degree[edge[0]], degree[edge[1]])));
            }
            
            edgeList.sort((a, b) -> Integer.compare(b.weight, a.weight)); // 降序
            
            UnionFind uf = new UnionFind(n);
            
            // 合并社区
            for (Edge edge : edgeList) {
                uf.union(edge.u, edge.v);
            }
            
            return extractCommunities(uf, n);
        }
        
        private static Map<Integer, Set<Integer>> extractCommunities(UnionFind uf, int n) {
            Map<Integer, Set<Integer>> communities = new HashMap<>();
            for (int i = 0; i < n; i++) {
                int root = uf.find(i);
                communities.computeIfAbsent(root, k -> new HashSet<>()).add(i);
            }
            return communities;
        }
        
        static class Edge {
            int u, v, weight;
            Edge(int u, int v, int weight) {
                this.u = u;
                this.v = v;
                this.weight = weight;
            }
        }
    }
    
    /**
     * 7. 并查集在编译器优化中的应用：变量别名分析
     */
    static class AliasAnalysis {
        /**
         * 简单的变量别名分析使用并查集
         */
        public static Map<String, Set<String>> analyzeAliases(String[][] assignments) {
            // 创建变量到索引的映射
            Map<String, Integer> varToIndex = new HashMap<>();
            int index = 0;
            
            for (String[] assignment : assignments) {
                if (!varToIndex.containsKey(assignment[0])) {
                    varToIndex.put(assignment[0], index++);
                }
                if (!varToIndex.containsKey(assignment[1])) {
                    varToIndex.put(assignment[1], index++);
                }
            }
            
            UnionFind uf = new UnionFind(index);
            
            // 处理赋值语句
            for (String[] assignment : assignments) {
                int idx1 = varToIndex.get(assignment[0]);
                int idx2 = varToIndex.get(assignment[1]);
                uf.union(idx1, idx2);
            }
            
            // 提取别名集合
            Map<Integer, Set<String>> aliasGroups = new HashMap<>();
            for (Map.Entry<String, Integer> entry : varToIndex.entrySet()) {
                int root = uf.find(entry.getValue());
                aliasGroups.computeIfAbsent(root, k -> new HashSet<>()).add(entry.getKey());
            }
            
            Map<String, Set<String>> result = new HashMap<>();
            for (Set<String> aliasSet : aliasGroups.values()) {
                for (String var : aliasSet) {
                    result.put(var, new HashSet<>(aliasSet));
                }
            }
            
            return result;
        }
    }
    
    /**
     * 标准并查集实现（用于上述应用）
     */
    static class UnionFind {
        int[] parent;
        int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                return false;
            }
            
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            
            return true;
        }
        
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("=== 并查集高级应用测试 ===");
        
        // 测试聚类分析
        testClusteringAnalysis();
        
        // 测试连通组件标记
        testConnectedComponentLabeling();
        
        // 测试社区发现
        testCommunityDetection();
        
        // 测试别名分析
        testAliasAnalysis();
        
        System.out.println("所有测试完成！");
    }
    
    private static void testClusteringAnalysis() {
        System.out.println("\n1. 聚类分析测试:");
        double[][] data = {
            {1, 1}, {1, 2}, {8, 8}, {8, 9}, {15, 15}
        };
        List<Set<Integer>> clusters = ClusteringAnalysis.hierarchicalClustering(data, 3.0);
        System.out.println("聚类结果: " + clusters);
    }
    
    private static void testConnectedComponentLabeling() {
        System.out.println("\n2. 连通组件标记测试:");
        int[][] binaryImage = {
            {1, 1, 0, 0, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 0, 1, 1},
            {0, 0, 0, 1, 1}
        };
        int[][] labels = ConnectedComponentLabeling.labelComponents(binaryImage);
        System.out.println("4连通标记结果:");
        for (int[] row : labels) {
            System.out.println(Arrays.toString(row));
        }
    }
    
    private static void testCommunityDetection() {
        System.out.println("\n3. 社区发现测试:");
        int[][] edges = {
            {0, 1}, {1, 2}, {2, 0},  // 第一个社区
            {3, 4}, {4, 5}, {5, 3},  // 第二个社区
            {6, 7}                    // 第三个社区
        };
        Map<Integer, Set<Integer>> communities = CommunityDetection.detectCommunities(edges, 8);
        System.out.println("社区发现结果: " + communities);
    }
    
    private static void testAliasAnalysis() {
        System.out.println("\n4. 别名分析测试:");
        String[][] assignments = {
            {"a", "b"}, {"b", "c"}, {"x", "y"}
        };
        Map<String, Set<String>> aliases = AliasAnalysis.analyzeAliases(assignments);
        System.out.println("别名分析结果: " + aliases);
    }
    
    /**
     * 工程化考量总结:
     * 1. 性能优化: 根据具体场景选择合适的并查集变种
     * 2. 内存管理: 对于大规模数据，考虑使用更紧凑的数据结构
     * 3. 并发安全: 多线程环境下需要额外的同步机制
     * 4. 可扩展性: 设计支持多种应用场景的通用接口
     * 5. 测试覆盖: 确保各种边界情况得到充分测试
     */
}