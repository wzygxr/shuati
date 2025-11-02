package class053;

import java.util.*;

/**
 * 岛屿数量
 * 
 * 题目描述：
 * 给你一个由 '1'（陆地）和 '0'（水）组成的二维网格，请你计算网格中岛屿的数量。
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 * 
 * 测试链接：https://leetcode.cn/problems/number-of-islands/
 * 题目来源：LeetCode
 * 难度：中等
 * 
 * 核心算法：深度优先搜索（DFS）或广度优先搜索（BFS）
 * 
 * 解题思路：
 * 1. 遍历网格中的每个单元格
 * 2. 当遇到陆地（'1'）时，进行DFS/BFS标记所有相连的陆地
 * 3. 每次完整的DFS/BFS标记过程计数为一个岛屿
 * 4. 继续遍历直到所有单元格都被访问
 * 
 * 时间复杂度分析：
 * O(m*n) - 每个单元格最多被访问一次，m和n分别是网格的行数和列数
 * 
 * 空间复杂度分析：
 * O(m*n) - 最坏情况下DFS递归栈深度或BFS队列大小可能达到m*n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 * 
 * 工程化考量：
 * 1. 健壮性：处理空网格、单行/单列网格等边界情况
 * 2. 性能优化：使用方向数组简化代码，避免重复访问
 * 3. 可读性：使用清晰的变量名和注释说明算法步骤
 * 
 * 算法调试技巧：
 * 1. 可视化调试：打印网格状态和访问标记
 * 2. 边界测试：测试各种边界情况如全陆地、全水域等
 * 3. 性能测试：测试大规模网格下的性能表现
 */
public class Code19_NumberOfIslands {
    
    // 方向数组：上、右、下、左
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    
    /**
     * DFS解法：使用递归实现深度优先搜索
     */
    public static int numIslandsDFS(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        
        // 遍历所有单元格
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j, m, n);
                }
            }
        }
        
        return count;
    }
    
    /**
     * DFS辅助函数：标记所有相连的陆地
     */
    private static void dfs(char[][] grid, int i, int j, int m, int n) {
        // 边界检查或已经访问过（标记为'0'）
        if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] != '1') {
            return;
        }
        
        // 标记当前单元格为已访问（改为'0'）
        grid[i][j] = '0';
        
        // 向四个方向递归搜索
        for (int[] dir : DIRECTIONS) {
            int newI = i + dir[0];
            int newJ = j + dir[1];
            dfs(grid, newI, newJ, m, n);
        }
    }
    
    /**
     * BFS解法：使用队列实现广度优先搜索
     */
    public static int numIslandsBFS(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    bfs(grid, i, j, m, n);
                }
            }
        }
        
        return count;
    }
    
    /**
     * BFS辅助函数：使用队列标记所有相连的陆地
     */
    private static void bfs(char[][] grid, int i, int j, int m, int n) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{i, j});
        grid[i][j] = '0';  // 标记为已访问
        
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1];
            
            // 检查四个方向
            for (int[] dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                
                if (newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] == '1') {
                    queue.offer(new int[]{newX, newY});
                    grid[newX][newY] = '0';  // 标记为已访问
                }
            }
        }
    }
    
    /**
     * 并查集解法：使用并查集数据结构
     */
    public static int numIslandsUnionFind(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        UnionFind uf = new UnionFind(grid);
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    // 检查右边和下面的邻居
                    if (i + 1 < m && grid[i + 1][j] == '1') {
                        uf.union(i * n + j, (i + 1) * n + j);
                    }
                    if (j + 1 < n && grid[i][j + 1] == '1') {
                        uf.union(i * n + j, i * n + (j + 1));
                    }
                }
            }
        }
        
        return uf.getCount();
    }
    
    /**
     * 并查集实现类
     */
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        private int count;
        
        public UnionFind(char[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            parent = new int[m * n];
            rank = new int[m * n];
            count = 0;
            
            // 初始化并查集
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == '1') {
                        int index = i * n + j;
                        parent[index] = index;
                        rank[index] = 0;
                        count++;
                    }
                }
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // 路径压缩
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                // 按秩合并
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
                count--;
            }
        }
        
        public int getCount() {
            return count;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void testNumIslands() {
        System.out.println("=== 岛屿数量单元测试 ===");
        
        // 测试用例1：常规情况
        char[][] grid1 = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        int result1 = numIslandsDFS(grid1);
        System.out.println("测试用例1 - 网格1:");
        printGrid(grid1);
        System.out.println("岛屿数量 (DFS): " + result1);
        System.out.println("期望: 1");
        
        // 测试用例2：多个岛屿
        char[][] grid2 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        int result2 = numIslandsDFS(grid2);
        System.out.println("\n测试用例2 - 网格2:");
        printGrid(grid2);
        System.out.println("岛屿数量 (DFS): " + result2);
        System.out.println("期望: 3");
        
        // 测试用例3：边界情况 - 空网格
        char[][] grid3 = {};
        int result3 = numIslandsDFS(grid3);
        System.out.println("\n测试用例3 - 空网格");
        System.out.println("岛屿数量: " + result3);
        System.out.println("期望: 0");
        
        // 测试用例4：全陆地
        char[][] grid4 = {
            {'1','1','1'},
            {'1','1','1'},
            {'1','1','1'}
        };
        int result4 = numIslandsDFS(grid4);
        System.out.println("\n测试用例4 - 全陆地:");
        printGrid(grid4);
        System.out.println("岛屿数量: " + result4);
        System.out.println("期望: 1");
        
        // 测试用例5：全水域
        char[][] grid5 = {
            {'0','0','0'},
            {'0','0','0'},
            {'0','0','0'}
        };
        int result5 = numIslandsDFS(grid5);
        System.out.println("\n测试用例5 - 全水域:");
        printGrid(grid5);
        System.out.println("岛屿数量: " + result5);
        System.out.println("期望: 0");
    }
    
    /**
     * 性能对比测试：DFS vs BFS vs 并查集
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能对比测试 ===");
        
        // 生成大规模测试网格
        int m = 1000, n = 1000;
        char[][] largeGrid = generateLargeGrid(m, n, 0.3);  // 30%陆地
        
        // 测试DFS
        char[][] gridDFS = copyGrid(largeGrid);
        long startTime1 = System.currentTimeMillis();
        int result1 = numIslandsDFS(gridDFS);
        long endTime1 = System.currentTimeMillis();
        
        // 测试BFS
        char[][] gridBFS = copyGrid(largeGrid);
        long startTime2 = System.currentTimeMillis();
        int result2 = numIslandsBFS(gridBFS);
        long endTime2 = System.currentTimeMillis();
        
        // 测试并查集
        char[][] gridUF = copyGrid(largeGrid);
        long startTime3 = System.currentTimeMillis();
        int result3 = numIslandsUnionFind(gridUF);
        long endTime3 = System.currentTimeMillis();
        
        System.out.println("网格规模: " + m + " × " + n);
        System.out.println("DFS执行时间: " + (endTime1 - startTime1) + "ms, 岛屿数量: " + result1);
        System.out.println("BFS执行时间: " + (endTime2 - startTime2) + "ms, 岛屿数量: " + result2);
        System.out.println("并查集执行时间: " + (endTime3 - startTime3) + "ms, 岛屿数量: " + result3);
        System.out.println("结果一致性: " + (result1 == result2 && result2 == result3));
    }
    
    /**
     * 生成大规模测试网格
     */
    private static char[][] generateLargeGrid(int m, int n, double landRatio) {
        char[][] grid = new char[m][n];
        Random random = new Random();
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = random.nextDouble() < landRatio ? '1' : '0';
            }
        }
        
        return grid;
    }
    
    /**
     * 复制网格（深拷贝）
     */
    private static char[][] copyGrid(char[][] original) {
        char[][] copy = new char[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }
    
    /**
     * 打印网格（用于调试）
     */
    private static void printGrid(char[][] grid) {
        if (grid.length == 0) {
            System.out.println("[]");
            return;
        }
        
        for (int i = 0; i < Math.min(grid.length, 10); i++) {  // 限制打印行数
            for (int j = 0; j < Math.min(grid[0].length, 10); j++) {  // 限制打印列数
                System.out.print(grid[i][j] + " ");
            }
            if (grid[0].length > 10) System.out.print("...");
            System.out.println();
        }
        if (grid.length > 10) System.out.println("...");
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testNumIslands();
        
        // 运行性能对比测试
        performanceComparison();
        
        System.out.println("\n=== 岛屿数量算法验证完成 ===");
    }
}