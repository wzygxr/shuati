package class058;

import java.util.*;

/**
 * 最大人工岛 (Making A Large Island)
 * 来源: LeetCode 827
 * 题目链接: https://leetcode.cn/problems/making-a-large-island/
 * 
 * 题目描述:
 * 给你一个大小为 n * n 二进制矩阵 grid 。最多 只能将一格 0 变成 1 。
 * 返回执行此操作后，grid 中最大的岛屿面积是多少？
 * 岛屿 由一组上、下、左、右四个方向相连的 1 形成。
 * 
 * 解题思路（两遍扫描法）:
 * 1. 第一遍扫描：使用DFS/BFS给每个岛屿编号，并计算每个岛屿的面积
 * 2. 第二遍扫描：遍历所有的0，计算将其变为1后能连接的最大岛屿面积
 * 3. 注意：连接的岛屿可能来自不同的方向，需要去重
 * 
 * 时间复杂度: O(n²) - 每个单元格最多被访问常数次
 * 空间复杂度: O(n²) - 需要存储岛屿编号和面积信息
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空，矩阵尺寸是否有效
 * 2. 边界条件：处理全0、全1矩阵
 * 3. 去重策略：使用标记数组避免重复计算同一岛屿
 * 4. 可扩展性：可以支持8个方向的连接
 * 
 * 语言特性差异:
 * Java: 使用对象和集合类方便处理
 * C++: 需要手动管理内存，但性能更高
 * Python: 使用字典和集合方便处理
 * 
 * 极端输入场景:
 * 1. 全0矩阵：返回1（只能创建一个岛屿）
 * 2. 全1矩阵：返回n²（整个矩阵就是一个岛屿）
 * 3. 单元素矩阵：根据元素值返回1或2
 * 4. 大规模矩阵：需要注意性能优化
 * 
 * 性能优化:
 * 1. 岛屿编号：从2开始编号，避免与0和1冲突
 * 2. 面积缓存：使用数组缓存每个岛屿的面积
 * 3. 去重标记：使用布尔数组标记已访问的岛屿
 * 4. 提前计算：预先计算所有岛屿的面积
 */
public class Code03_MakingLargeIsland {

    // 四个方向的偏移量：上、下、左、右
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    
    /**
     * 计算最大人工岛面积的主函数
     * 
     * @param grid 二进制矩阵，1表示陆地，0表示水
     * @return 最大可能的人工岛面积
     * 
     * 算法步骤:
     * 1. 给每个岛屿编号并计算面积
     * 2. 计算当前最大岛屿面积
     * 3. 尝试将每个0变为1，计算可能形成的最大面积
     * 4. 返回最大值
     * 
     * 时间复杂度分析:
     * - 岛屿编号: O(n²)
     * - 面积计算: O(n²)
     * - 0点尝试: O(n²)
     * - 总时间复杂度: O(n²)
     * 
     * 空间复杂度分析:
     * - 岛屿面积数组: O(n²)
     * - 访问标记数组: O(n²)
     * - 总空间复杂度: O(n²)
     */
    public static int largestIsland(int[][] grid) {
        // 边界条件检查
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int n = grid.length;
        int m = grid[0].length;
        int nextId = 2; // 从2开始编号，避免与0和1冲突
        
        // 步骤1: 给每个岛屿编号并计算面积
        int[] islandSizes = new int[n * m + 2]; // 索引从2开始
        int maxArea = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    int area = dfs(grid, n, m, i, j, nextId);
                    islandSizes[nextId] = area;
                    maxArea = Math.max(maxArea, area);
                    nextId++;
                }
            }
        }
        
        // 如果整个网格都是陆地，直接返回总面积
        if (maxArea == n * m) {
            return maxArea;
        }
        
        // 步骤2: 尝试将每个0变为1，计算可能形成的最大面积
        boolean[] visitedIslands = new boolean[nextId];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 0) {
                    int potentialArea = 1; // 当前0变为1的贡献
                    
                    // 检查四个方向的相邻岛屿
                    for (int k = 0; k < 4; k++) {
                        int ni = i + dx[k];
                        int nj = j + dy[k];
                        
                        if (ni >= 0 && ni < n && nj >= 0 && nj < m && grid[ni][nj] > 1) {
                            int islandId = grid[ni][nj];
                            if (!visitedIslands[islandId]) {
                                potentialArea += islandSizes[islandId];
                                visitedIslands[islandId] = true;
                            }
                        }
                    }
                    
                    maxArea = Math.max(maxArea, potentialArea);
                    
                    // 重置访问标记
                    for (int k = 0; k < 4; k++) {
                        int ni = i + dx[k];
                        int nj = j + dy[k];
                        if (ni >= 0 && ni < n && nj >= 0 && nj < m && grid[ni][nj] > 1) {
                            visitedIslands[grid[ni][nj]] = false;
                        }
                    }
                }
            }
        }
        
        return maxArea;
    }
    
    /**
     * 深度优先搜索给岛屿编号并计算面积
     * 
     * @param grid 二维网格
     * @param n 行数
     * @param m 列数
     * @param i 当前行坐标
     * @param j 当前列坐标
     * @param id 岛屿编号
     * @return 当前岛屿的面积
     */
    private static int dfs(int[][] grid, int n, int m, int i, int j, int id) {
        // 边界条件检查
        if (i < 0 || i >= n || j < 0 || j >= m || grid[i][j] != 1) {
            return 0;
        }
        
        // 标记当前单元格为已访问（赋予岛屿编号）
        grid[i][j] = id;
        int area = 1;
        
        // 递归处理四个方向的相邻单元格
        for (int k = 0; k < 4; k++) {
            int ni = i + dx[k];
            int nj = j + dy[k];
            area += dfs(grid, n, m, ni, nj, id);
        }
        
        return area;
    }
    
    /**
     * 广度优先搜索版本（避免递归深度问题）
     * 
     * @param grid 二进制矩阵
     * @return 最大人工岛面积
     */
    public static int largestIslandBFS(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int n = grid.length;
        int m = grid[0].length;
        int nextId = 2;
        int[] islandSizes = new int[n * m + 2];
        int maxArea = 0;
        
        // BFS给岛屿编号
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    int area = bfs(grid, n, m, i, j, nextId);
                    islandSizes[nextId] = area;
                    maxArea = Math.max(maxArea, area);
                    nextId++;
                }
            }
        }
        
        if (maxArea == n * m) {
            return maxArea;
        }
        
        boolean[] visitedIslands = new boolean[nextId];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 0) {
                    int potentialArea = 1;
                    
                    for (int k = 0; k < 4; k++) {
                        int ni = i + dx[k];
                        int nj = j + dy[k];
                        
                        if (ni >= 0 && ni < n && nj >= 0 && nj < m && grid[ni][nj] > 1) {
                            int islandId = grid[ni][nj];
                            if (!visitedIslands[islandId]) {
                                potentialArea += islandSizes[islandId];
                                visitedIslands[islandId] = true;
                            }
                        }
                    }
                    
                    maxArea = Math.max(maxArea, potentialArea);
                    
                    for (int k = 0; k < 4; k++) {
                        int ni = i + dx[k];
                        int nj = j + dy[k];
                        if (ni >= 0 && ni < n && nj >= 0 && nj < m && grid[ni][nj] > 1) {
                            visitedIslands[grid[ni][nj]] = false;
                        }
                    }
                }
            }
        }
        
        return maxArea;
    }
    
    /**
     * 广度优先搜索实现岛屿编号
     */
    private static int bfs(int[][] grid, int n, int m, int i, int j, int id) {
        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        queue.offer(new int[]{i, j});
        grid[i][j] = id;
        int area = 0;
        
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1];
            area++;
            
            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k];
                int ny = y + dy[k];
                
                if (nx >= 0 && nx < n && ny >= 0 && ny < m && grid[nx][ny] == 1) {
                    grid[nx][ny] = id;
                    queue.offer(new int[]{nx, ny});
                }
            }
        }
        
        return area;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：标准网格
        int[][] grid1 = {
            {1, 0},
            {0, 1}
        };
        
        System.out.println("测试用例1 - 标准网格:");
        System.out.println("原始网格:");
        printGrid(grid1);
        System.out.println("DFS版本最大人工岛面积: " + largestIsland(copyGrid(grid1)));
        System.out.println("BFS版本最大人工岛面积: " + largestIslandBFS(copyGrid(grid1)));
        
        // 测试用例2：全1网格
        int[][] grid2 = {
            {1, 1},
            {1, 1}
        };
        
        System.out.println("测试用例2 - 全1网格:");
        System.out.println("原始网格:");
        printGrid(grid2);
        System.out.println("DFS版本最大人工岛面积: " + largestIsland(copyGrid(grid2)));
        
        // 测试用例3：全0网格
        int[][] grid3 = {
            {0, 0},
            {0, 0}
        };
        
        System.out.println("测试用例3 - 全0网格:");
        System.out.println("原始网格:");
        printGrid(grid3);
        System.out.println("DFS版本最大人工岛面积: " + largestIsland(copyGrid(grid3)));
    }
    
    // 辅助方法：打印网格
    private static void printGrid(int[][] grid) {
        if (grid == null || grid.length == 0) {
            System.out.println("空网格");
            return;
        }
        
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    
    // 辅助方法：复制网格
    private static int[][] copyGrid(int[][] grid) {
        if (grid == null) return null;
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = grid[i].clone();
        }
        return copy;
    }
}
