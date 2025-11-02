package class058;

/**
 * UVa 572 - Oil Deposits (石油沉积)
 * 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=7&page=show_problem&problem=513
 * 
 * 题目描述:
 * 地质探测公司负责探测地下石油储藏。地质探测公司在一个矩形网格中工作，
 * 网格被划分为多个单元。有些单元格含有石油，用'@'表示，其他单元格不含石油，用'*'表示。
 * 如果两个含油单元格相邻（水平、垂直或对角线方向），则它们属于同一个油藏。
 * 你的任务是确定网格中有多少个不同的油藏。
 * 
 * 解题思路:
 * 使用Flood Fill算法（8连通）统计连通分量数量。
 * 与POJ 2386类似，但使用不同的字符表示。
 * 
 * 时间复杂度: O(m*n)
 * 空间复杂度: O(m*n)
 * 是否最优解: 是
 */
public class Code16_UVa_572_OilDeposits {
    
    // 八个方向的偏移量
    private static final int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
    
    /**
     * 计算油藏数量
     * 
     * @param grid 网格矩阵，'@'表示石油，'*'表示无石油
     * @return 油藏数量
     */
    public static int countOilDeposits(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length, n = grid[0].length;
        int count = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '@') {
                    dfs(grid, i, j, m, n);
                    count++;
                }
            }
        }
        
        return count;
    }
    
    private static void dfs(char[][] grid, int x, int y, int m, int n) {
        if (x < 0 || x >= m || y < 0 || y >= n || grid[x][y] != '@') {
            return;
        }
        
        grid[x][y] = '*'; // 标记为已访问
        
        for (int i = 0; i < 8; i++) {
            dfs(grid, x + dx[i], y + dy[i], m, n);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        char[][] grid1 = {
            {'*', '*', '*', '*', '@'},
            {'*', '@', '@', '*', '@'},
            {'*', '@', '*', '*', '@'},
            {'@', '@', '*', '*', '*'},
            {'@', '@', '*', '*', '*'}
        };
        
        System.out.println("测试用例1 - UVa 572 Oil Deposits:");
        System.out.println("网格:");
        printGrid(grid1);
        System.out.println("油藏数量: " + countOilDeposits(grid1));
    }
    
    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}