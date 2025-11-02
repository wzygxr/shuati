package class008_AdvancedAlgorithmsAndDataStructures.fibonacci_heap_problems;

import java.util.*;

/**
 * LeetCode 1631. 最小体力消耗路径 (Path With Minimum Effort)
 * 
 * 题目来源：https://leetcode.cn/problems/path-with-minimum-effort/
 * 
 * 题目描述：
 * 你准备参加一场远足活动。给你一个二维 rows x columns 的地图 heights，
 * 其中 heights[row][col] 表示格子 (row, col) 的高度。
 * 一开始你在最左上角的格子 (0, 0)，且你希望去最右下角的格子 (rows-1, columns-1)。
 * 你每次可以往上下左右四个方向移动一个格子。
 * 你所需的体力消耗值为路径上相邻格子之间高度差绝对值的最大值。
 * 请你返回从左上角走到右下角的最小体力消耗值。
 * 
 * 算法思路：
 * 这个问题可以通过以下方法解决：
 * 1. Dijkstra算法：使用斐波那契堆优化的最短路径算法
 * 2. 二分搜索+BFS：二分答案并用BFS验证
 * 3. 并查集：按边权重排序后逐步合并
 * 
 * 使用斐波那契堆优化的Dijkstra算法：
 * 1. 将问题转化为最短路径问题
 * 2. 边的权重定义为相邻格子间高度差的绝对值
 * 3. 使用斐波那契堆优化Dijkstra算法
 * 
 * 时间复杂度：
 * - 斐波那契堆优化Dijkstra：O(mn log(mn))
 * - 二分搜索+BFS：O(mn log(maxHeight))
 * - 并查集：O(mn log(mn))
 * - 空间复杂度：O(mn)
 * 
 * 应用场景：
 * 1. 地图导航：寻找最省力的路径
 * 2. 图像处理：边缘检测和分割
 * 3. 网络路由：最小延迟路径选择
 * 
 * 相关题目：
 * 1. LeetCode 743. 网络延迟时间
 * 2. LeetCode 1584. 连接所有点的最小费用
 * 3. LeetCode 1102. 得分最高的路径
 */
public class LeetCode_1631_PathWithMinimumEffort {
    
    // 方向数组：上下左右
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    
    /**
     * 斐波那契堆节点类
     */
    static class FibonacciHeapNode {
        int row, col, distance;
        boolean visited;
        
        FibonacciHeapNode(int row, int col, int distance) {
            this.row = row;
            this.col = col;
            this.distance = distance;
            this.visited = false;
        }
    }
    
    /**
     * 简化版斐波那契堆实现（用于Dijkstra算法）
     */
    static class FibonacciHeap {
        private PriorityQueue<FibonacciHeapNode> pq;
        private Map<String, FibonacciHeapNode> nodeMap;
        
        public FibonacciHeap() {
            pq = new PriorityQueue<>((a, b) -> Integer.compare(a.distance, b.distance));
            nodeMap = new HashMap<>();
        }
        
        public void insert(int row, int col, int distance) {
            FibonacciHeapNode node = new FibonacciHeapNode(row, col, distance);
            String key = row + "," + col;
            nodeMap.put(key, node);
            pq.offer(node);
        }
        
        public FibonacciHeapNode extractMin() {
            while (!pq.isEmpty()) {
                FibonacciHeapNode node = pq.poll();
                if (!node.visited) {
                    String key = node.row + "," + node.col;
                    nodeMap.remove(key);
                    return node;
                }
            }
            return null;
        }
        
        public void decreaseKey(int row, int col, int newDistance) {
            String key = row + "," + col;
            FibonacciHeapNode node = nodeMap.get(key);
            if (node != null && newDistance < node.distance) {
                node.visited = true; // 标记为已访问，稍后会插入新节点
                insert(row, col, newDistance);
            }
        }
        
        public boolean isEmpty() {
            return nodeMap.isEmpty();
        }
    }
    
    /**
     * 方法1：使用斐波那契堆优化的Dijkstra算法
     * 时间复杂度：O(mn log(mn))
     * 空间复杂度：O(mn)
     * @param heights 高度矩阵
     * @return 最小体力消耗值
     */
    public static int minimumEffortPathFibonacciHeap(int[][] heights) {
        int rows = heights.length;
        int cols = heights[0].length;
        
        // 特殊情况：只有一个格子
        if (rows == 1 && cols == 1) {
            return 0;
        }
        
        // 初始化距离数组
        int[][] distances = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(distances[i], Integer.MAX_VALUE);
        }
        distances[0][0] = 0;
        
        // 使用斐波那契堆
        FibonacciHeap fibHeap = new FibonacciHeap();
        fibHeap.insert(0, 0, 0);
        
        // 记录已访问的节点
        boolean[][] visited = new boolean[rows][cols];
        
        while (!fibHeap.isEmpty()) {
            FibonacciHeapNode minNode = fibHeap.extractMin();
            if (minNode == null) break;
            
            int row = minNode.row;
            int col = minNode.col;
            
            if (visited[row][col]) continue;
            visited[row][col] = true;
            
            // 如果到达终点
            if (row == rows - 1 && col == cols - 1) {
                return minNode.distance;
            }
            
            // 检查四个方向的邻居
            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                // 检查边界
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && !visited[newRow][newCol]) {
                    // 计算到邻居的体力消耗
                    int effort = Math.abs(heights[newRow][newCol] - heights[row][col]);
                    int newDistance = Math.max(distances[row][col], effort);
                    
                    // 如果找到更小的体力消耗路径
                    if (newDistance < distances[newRow][newCol]) {
                        distances[newRow][newCol] = newDistance;
                        fibHeap.insert(newRow, newCol, newDistance);
                    }
                }
            }
        }
        
        return distances[rows - 1][cols - 1];
    }
    
    /**
     * 方法2：二分搜索+BFS
     * 时间复杂度：O(mn log(maxHeight))
     * 空间复杂度：O(mn)
     * @param heights 高度矩阵
     * @return 最小体力消耗值
     */
    public static int minimumEffortPathBinarySearch(int[][] heights) {
        int rows = heights.length;
        int cols = heights[0].length;
        
        // 特殊情况：只有一个格子
        if (rows == 1 && cols == 1) {
            return 0;
        }
        
        // 找到最大高度差
        int maxDiff = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i > 0) {
                    maxDiff = Math.max(maxDiff, Math.abs(heights[i][j] - heights[i-1][j]));
                }
                if (j > 0) {
                    maxDiff = Math.max(maxDiff, Math.abs(heights[i][j] - heights[i][j-1]));
                }
            }
        }
        
        // 二分搜索答案
        int left = 0, right = maxDiff;
        int result = maxDiff;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (canReach(heights, mid)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * BFS检查是否能在给定的最大体力消耗下到达终点
     * @param heights 高度矩阵
     * @param maxEffort 最大体力消耗
     * @return 是否能到达终点
     */
    private static boolean canReach(int[][] heights, int maxEffort) {
        int rows = heights.length;
        int cols = heights[0].length;
        
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        
        boolean[][] visited = new boolean[rows][cols];
        visited[0][0] = true;
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            
            // 如果到达终点
            if (row == rows - 1 && col == cols - 1) {
                return true;
            }
            
            // 检查四个方向的邻居
            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                // 检查边界和是否已访问
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && !visited[newRow][newCol]) {
                    // 检查体力消耗是否满足要求
                    int effort = Math.abs(heights[newRow][newCol] - heights[row][col]);
                    if (effort <= maxEffort) {
                        visited[newRow][newCol] = true;
                        queue.offer(new int[]{newRow, newCol});
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 方法3：并查集解法
     * 时间复杂度：O(mn log(mn))
     * 空间复杂度：O(mn)
     * @param heights 高度矩阵
     * @return 最小体力消耗值
     */
    public static int minimumEffortPathUnionFind(int[][] heights) {
        int rows = heights.length;
        int cols = heights[0].length;
        
        // 特殊情况：只有一个格子
        if (rows == 1 && cols == 1) {
            return 0;
        }
        
        // 创建边列表：[起点, 终点, 权重]
        List<int[]> edges = new ArrayList<>();
        
        // 添加水平边
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols - 1; j++) {
                int from = i * cols + j;
                int to = i * cols + (j + 1);
                int weight = Math.abs(heights[i][j] - heights[i][j + 1]);
                edges.add(new int[]{from, to, weight});
            }
        }
        
        // 添加垂直边
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols; j++) {
                int from = i * cols + j;
                int to = (i + 1) * cols + j;
                int weight = Math.abs(heights[i][j] - heights[i + 1][j]);
                edges.add(new int[]{from, to, weight});
            }
        }
        
        // 按权重排序边
        edges.sort((a, b) -> Integer.compare(a[2], b[2]));
        
        // 使用并查集
        UnionFind uf = new UnionFind(rows * cols);
        int source = 0;
        int target = rows * cols - 1;
        
        // 逐步添加边，直到起点和终点连通
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            int weight = edge[2];
            
            uf.union(from, to);
            if (uf.isConnected(source, target)) {
                return weight;
            }
        }
        
        return 0;
    }
    
    /**
     * 并查集类
     */
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // 路径压缩
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                // 按秩合并
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
        
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 1631. 最小体力消耗路径 ===");
        
        // 测试用例1
        int[][] heights1 = {{1,2,2},{3,8,2},{5,3,5}};
        System.out.println("测试用例1:");
        System.out.println("高度矩阵: [[1,2,2],[3,8,2],[5,3,5]]");
        System.out.println("斐波那契堆解法结果: " + minimumEffortPathFibonacciHeap(heights1));
        System.out.println("二分搜索解法结果: " + minimumEffortPathBinarySearch(heights1));
        System.out.println("并查集解法结果: " + minimumEffortPathUnionFind(heights1));
        System.out.println("期望结果: 2");
        System.out.println();
        
        // 测试用例2
        int[][] heights2 = {{1,2,3},{3,8,4},{5,3,5}};
        System.out.println("测试用例2:");
        System.out.println("高度矩阵: [[1,2,3],[3,8,4],[5,3,5]]");
        System.out.println("斐波那契堆解法结果: " + minimumEffortPathFibonacciHeap(heights2));
        System.out.println("二分搜索解法结果: " + minimumEffortPathBinarySearch(heights2));
        System.out.println("并查集解法结果: " + minimumEffortPathUnionFind(heights2));
        System.out.println("期望结果: 1");
        System.out.println();
        
        // 测试用例3
        int[][] heights3 = {{1,2,1,1,1},{1,2,1,2,1},{1,2,1,2,1},{1,2,1,2,1},{1,1,1,2,1}};
        System.out.println("测试用例3:");
        System.out.println("高度矩阵: [[1,2,1,1,1],[1,2,1,2,1],[1,2,1,2,1],[1,2,1,2,1],[1,1,1,2,1]]");
        System.out.println("斐波那契堆解法结果: " + minimumEffortPathFibonacciHeap(heights3));
        System.out.println("二分搜索解法结果: " + minimumEffortPathBinarySearch(heights3));
        System.out.println("并查集解法结果: " + minimumEffortPathUnionFind(heights3));
        System.out.println("期望结果: 0");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int rows = 100, cols = 100;
        int[][] heights = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                heights[i][j] = random.nextInt(1000000);
            }
        }
        
        long startTime = System.nanoTime();
        int result1 = minimumEffortPathFibonacciHeap(heights);
        long endTime = System.nanoTime();
        System.out.println("斐波那契堆解法处理" + rows + "x" + cols + "矩阵时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result1);
        
        startTime = System.nanoTime();
        int result2 = minimumEffortPathBinarySearch(heights);
        endTime = System.nanoTime();
        System.out.println("二分搜索解法处理" + rows + "x" + cols + "矩阵时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result2);
        
        startTime = System.nanoTime();
        int result3 = minimumEffortPathUnionFind(heights);
        endTime = System.nanoTime();
        System.out.println("并查集解法处理" + rows + "x" + cols + "矩阵时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result3);
    }
}