package class062;

// 岛屿数量
// 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
// 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
// 此外，你可以假设该网格的四条边均被水包围。
// 测试链接 : https://leetcode.cn/problems/number-of-islands/
// 
// 算法思路：
// 使用BFS进行岛屿的遍历和标记。遍历整个网格，当遇到未访问的陆地('1')时，
// 启动BFS遍历整个岛屿，并将所有相连的陆地标记为已访问。
// 岛屿数量就是启动BFS的次数。
// 
// 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数，每个单元格最多被访问一次
// 空间复杂度：O(min(m, n))，BFS队列的最大大小取决于网格的较小维度
// 
// 工程化考量：
// 1. 原地修改：使用原网格标记已访问，避免额外空间
// 2. 边界检查：确保移动后的位置在网格范围内
// 3. 方向选择：使用4方向移动（水平垂直）而非8方向
// 4. 性能优化：使用队列而非递归避免栈溢出
public class Code18_NumberOfIslands {

    // 四个方向的移动：上、右、下、左
    private static final int[] DIRECTIONS = {-1, 0, 1, 0, -1};
    
    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        int islandCount = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 找到未访问的陆地
                if (grid[i][j] == '1') {
                    islandCount++;
                    bfs(grid, i, j, m, n);
                }
            }
        }
        
        return islandCount;
    }
    
    private static void bfs(char[][] grid, int startX, int startY, int m, int n) {
        // 使用数组模拟队列，避免使用Queue接口的开销
        int[][] queue = new int[m * n][2];
        int front = 0, rear = 0;
        
        // 起点入队并标记为已访问（将'1'改为'0'）
        queue[rear][0] = startX;
        queue[rear][1] = startY;
        rear++;
        grid[startX][startY] = '0';
        
        while (front < rear) {
            int x = queue[front][0];
            int y = queue[front][1];
            front++;
            
            // 向四个方向扩展
            for (int d = 0; d < 4; d++) {
                int nx = x + DIRECTIONS[d];
                int ny = y + DIRECTIONS[d + 1];
                
                // 检查边界和是否为未访问的陆地
                if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] == '1') {
                    // 标记为已访问并入队
                    grid[nx][ny] = '0';
                    queue[rear][0] = nx;
                    queue[rear][1] = ny;
                    rear++;
                }
            }
        }
    }
    
    // 单元测试示例
    public static void main(String[] args) {
        // 测试用例1：标准情况
        char[][] grid1 = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        System.out.println("测试用例1 - 岛屿数量: " + numIslands(grid1)); // 期望输出: 1
        
        // 测试用例2：多个岛屿
        char[][] grid2 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        System.out.println("测试用例2 - 岛屿数量: " + numIslands(grid2)); // 期望输出: 3
        
        // 测试用例3：空网格
        char[][] grid3 = {};
        System.out.println("测试用例3 - 岛屿数量: " + numIslands(grid3)); // 期望输出: 0
        
        // 测试用例4：全为水
        char[][] grid4 = {
            {'0','0','0'},
            {'0','0','0'},
            {'0','0','0'}
        };
        System.out.println("测试用例4 - 岛屿数量: " + numIslands(grid4)); // 期望输出: 0
        
        // 测试用例5：全为陆地
        char[][] grid5 = {
            {'1','1','1'},
            {'1','1','1'},
            {'1','1','1'}
        };
        System.out.println("测试用例5 - 岛屿数量: " + numIslands(grid5)); // 期望输出: 1
    }
}