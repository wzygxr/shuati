package class080;

// 岛屿数量 (Number of Islands)
// 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，
// 请你计算网格中岛屿的数量。
// 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
// 此外，你可以假设该网格的四条边均被水包围。
// 测试链接 : https://leetcode.cn/problems/number-of-islands/
public class Code18_NumberOfIslands {

    // 使用DFS解决岛屿数量问题
    // 核心思想：遍历网格，遇到陆地时进行DFS标记整个岛屿，统计岛屿数量
    // 时间复杂度: O(M * N)
    // 空间复杂度: O(min(M, N))
    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        
        // 遍历网格
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 如果遇到陆地
                if (grid[i][j] == '1') {
                    // 增加岛屿计数
                    count++;
                    // 使用DFS标记整个岛屿
                    dfs(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    // 深度优先搜索标记岛屿
    // grid: 网格
    // i, j: 当前位置坐标
    private static void dfs(char[][] grid, int i, int j) {
        int m = grid.length;
        int n = grid[0].length;
        
        // 边界条件检查
        if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] == '0') {
            return;
        }
        
        // 标记当前位置为已访问
        grid[i][j] = '0';
        
        // 递归访问四个方向的相邻位置
        dfs(grid, i - 1, j); // 上
        dfs(grid, i + 1, j); // 下
        dfs(grid, i, j - 1); // 左
        dfs(grid, i, j + 1); // 右
    }

}
