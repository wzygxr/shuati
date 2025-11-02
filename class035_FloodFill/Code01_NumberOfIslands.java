package class058;

/**
 * 岛屿数量 (Number of Islands)
 * 来源: LeetCode 200
 * 题目链接: https://leetcode.cn/problems/number-of-islands/
 * 
 * 题目描述:
 * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 * 此外，你可以假设该网格的四条边均被水包围。
 * 
 * 解题思路:
 * 使用Flood Fill算法，每当遇到一个未访问过的陆地('1')，就进行深度优先搜索：
 * 1. 遍历整个网格，寻找未访问过的陆地
 * 2. 当找到陆地时，岛屿计数加1，并调用DFS/BFS将与该陆地相连的所有陆地标记为已访问
 * 3. 继续遍历，直到所有单元格都被检查过
 * 
 * 时间复杂度: O(m*n) - 每个单元格最多被访问一次
 * 空间复杂度: O(m*n) - 递归调用栈的深度在最坏情况下为m*n
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空，网格尺寸是否有效
 * 2. 边界条件：处理单行、单列网格
 * 3. 原地修改：通过修改原网格来标记访问状态
 * 4. 可扩展性：可以支持8个方向的连接
 * 
 * 语言特性差异:
 * Java: 递归实现简洁，但需要注意递归深度
 * C++: 可以选择递归或使用栈手动实现
 * Python: 递归实现简洁，但有递归深度限制
 * 
 * 极端输入场景:
 * 1. 空网格：返回0
 * 2. 全水网格：返回0
 * 3. 全陆网格：返回1
 * 4. 大规模网格：需要注意栈溢出问题
 * 
 * 性能优化:
 * 1. 提前终止：及时返回边界条件
 * 2. 方向数组：使用数组存储方向偏移量
 * 3. BFS替代：对于大规模网格使用BFS避免栈溢出
 */
public class Code01_NumberOfIslands {

    // 四个方向的偏移量：上、下、左、右
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    
    /**
     * 计算岛屿数量的主函数（DFS版本）
     * 
     * @param grid 二维字符网格，'1'表示陆地，'0'表示水
     * @return 岛屿数量
     * 
     * 算法步骤:
     * 1. 检查输入有效性
     * 2. 遍历网格中的每个单元格
     * 3. 当遇到未访问的陆地时，岛屿计数加1，并进行DFS标记
     * 4. 返回岛屿总数
     * 
     * 时间复杂度分析:
     * - 每个单元格最多被访问一次: O(m*n)
     * - 每个边最多被遍历两次: O(2*(m*n - m - n)) ≈ O(m*n)
     * - 总时间复杂度: O(m*n)
     * 
     * 空间复杂度分析:
     * - 递归调用栈深度最多为m*n: O(m*n)
     * - 没有使用额外空间: O(1)（不考虑输入空间）
     */
    public static int numIslands(char[][] grid) {
        // 边界条件检查
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;
        
        // 遍历网格中的每个单元格
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 找到未访问的陆地
                if (grid[i][j] == '1') {
                    count++;
                    // 使用DFS标记所有相连的陆地
                    dfs(grid, i, j, rows, cols);
                }
            }
        }
        
        return count;
    }
    
    /**
     * 深度优先搜索标记相连的陆地
     * 
     * @param grid 二维网格
     * @param i 当前行坐标
     * @param j 当前列坐标
     * @param rows 行数
     * @param cols 列数
     * 
     * 标记策略:
     * - 将访问过的陆地标记为'0'（水）
     * - 这样可以避免使用额外的访问标记数组
     * - 同时确保每个陆地只被访问一次
     */
    private static void dfs(char[][] grid, int i, int j, int rows, int cols) {
        // 边界条件检查
        if (i < 0 || i >= rows || j < 0 || j >= cols || grid[i][j] != '1') {
            return;
        }
        
        // 标记当前单元格为已访问（改为水）
        grid[i][j] = '0';
        
        // 递归处理四个方向的相邻单元格
        for (int k = 0; k < 4; k++) {
            int newI = i + dx[k];
            int newJ = j + dy[k];
            dfs(grid, newI, newJ, rows, cols);
        }
    }
    
    /**
     * 广度优先搜索版本（避免递归深度问题）
     * 
     * @param grid 二维字符网格
     * @return 岛屿数量
     * 
     * 优势:
     * - 避免递归深度过大导致的栈溢出
     * - 更适合大规模网格
     * - 代码逻辑更直观
     */
    public static int numIslandsBFS(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;
        
        // 使用队列进行BFS
        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    grid[i][j] = '0';
                    queue.offer(new int[]{i, j});
                    
                    // BFS扩展当前岛屿
                    while (!queue.isEmpty()) {
                        int[] cell = queue.poll();
                        int x = cell[0], y = cell[1];
                        
                        for (int k = 0; k < 4; k++) {
                            int newX = x + dx[k];
                            int newY = y + dy[k];
                            
                            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] == '1') {
                                grid[newX][newY] = '0';
                                queue.offer(new int[]{newX, newY});
                            }
                        }
                    }
                }
            }
        }
        
        return count;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：标准岛屿网格
        char[][] grid1 = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        
        System.out.println("测试用例1 - 标准岛屿网格:");
        System.out.println("网格布局:");
        printGrid(grid1);
        System.out.println("DFS版本岛屿数量: " + numIslands(copyGrid(grid1)));
        System.out.println("BFS版本岛屿数量: " + numIslandsBFS(copyGrid(grid1)));
        
        // 测试用例2：多个岛屿
        char[][] grid2 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        
        System.out.println("测试用例2 - 多个岛屿:");
        System.out.println("网格布局:");
        printGrid(grid2);
        System.out.println("DFS版本岛屿数量: " + numIslands(copyGrid(grid2)));
        System.out.println("BFS版本岛屿数量: " + numIslandsBFS(copyGrid(grid2)));
        
        // 测试用例3：空网格
        char[][] grid3 = {};
        System.out.println("测试用例3 - 空网格:");
        System.out.println("DFS版本岛屿数量: " + numIslands(grid3));
        System.out.println("BFS版本岛屿数量: " + numIslandsBFS(grid3));
        
        // 测试用例4：全水网格
        char[][] grid4 = {
            {'0','0','0'},
            {'0','0','0'}
        };
        
        System.out.println("测试用例4 - 全水网格:");
        System.out.println("网格布局:");
        printGrid(grid4);
        System.out.println("DFS版本岛屿数量: " + numIslands(grid4));
        System.out.println("BFS版本岛屿数量: " + numIslandsBFS(grid4));
    }
    
    // 辅助方法：打印网格
    private static void printGrid(char[][] grid) {
        if (grid == null || grid.length == 0) {
            System.out.println("空网格");
            return;
        }
        
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    
    // 辅助方法：复制网格
    private static char[][] copyGrid(char[][] grid) {
        if (grid == null) return null;
        char[][] copy = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = grid[i].clone();
        }
        return copy;
    }
}