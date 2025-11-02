package class127;

// Minimum Path Sum
// 给定一个包含非负整数的 m x n 网格 grid，请找出一条从左上角到右下角的路径，
// 使得路径上的数字总和为最小。每次只能向下或者向右移动一步。
// 测试链接 : https://leetcode.cn/problems/minimum-path-sum/

/**
 * 算法思路：
 * 1. 这是一个典型的动态规划问题
 * 2. 使用二维动态规划 dp[i][j]，表示从起点(0,0)到位置(i,j)的最小路径和
 * 3. 状态转移方程：
 *    - dp[0][0] = grid[0][0]（起点）
 *    - 如果 i == 0 且 j > 0，只能从左边来，dp[i][j] = dp[i][j-1] + grid[i][j]
 *    - 如果 j == 0 且 i > 0，只能从上面来，dp[i][j] = dp[i-1][j] + grid[i][j]
 *    - 其他情况，可以从上面或左边来，dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
 * 4. 初始化：
 *    - dp[0][0] = grid[0][0]
 * 5. 结果：
 *    - dp[m-1][n-1]即为所求的最小路径和
 *
 * 时间复杂度：O(m * n)
 * 空间复杂度：O(m * n)
 */
public class Code09_MinimumPathSum {

    /**
     * 计算从左上角到右下角的最小路径和
     * @param grid m x n 的网格，包含非负整数
     * @return 最小路径和
     */
    public static int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        
        // dp[i][j] 表示从起点到位置(i,j)的最小路径和
        int[][] dp = new int[m][n];
        
        // 初始化起点
        dp[0][0] = grid[0][0];
        
        // 初始化第一行
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j-1] + grid[0][j];
        }
        
        // 初始化第一列
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i-1][0] + grid[i][0];
        }
        
        // 填充dp表
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // 路径和等于从上面和左边来的较小路径和加上当前位置的值
                dp[i][j] = Math.min(dp[i-1][j], dp[i][j-1]) + grid[i][j];
            }
        }
        
        return dp[m-1][n-1];
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] grid1 = {
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        };
        System.out.println("测试用例1结果: " + minPathSum(grid1)); // 预期输出: 7
        
        // 测试用例2
        int[][] grid2 = {
            {1, 2, 3},
            {4, 5, 6}
        };
        System.out.println("测试用例2结果: " + minPathSum(grid2)); // 预期输出: 12
        
        // 测试用例3
        int[][] grid3 = {
            {1, 2},
            {1, 1}
        };
        System.out.println("测试用例3结果: " + minPathSum(grid3)); // 预期输出: 3
        
        // 测试用例4
        int[][] grid4 = {
            {1, 3, 1, 2},
            {1, 5, 1, 3},
            {4, 2, 1, 1}
        };
        System.out.println("测试用例4结果: " + minPathSum(grid4)); // 预期输出: 8
    }

    // 相关题目：
    // 1. LeetCode 62. Unique Paths (不同路径)
    //    链接：https://leetcode.cn/problems/unique-paths/
    //    区别：计算不同路径的数量而不是路径和
    // 2. LeetCode 63. Unique Paths II (不同路径 II)
    //    链接：https://leetcode.cn/problems/unique-paths-ii/
    //    区别：网格中有障碍物，计算不同路径的数量
    // 3. LeetCode 174. Dungeon Game (地下城游戏)
    //    链接：https://leetcode.cn/problems/dungeon-game/
    //    区别：骑士需要从左上角到右下角，保证健康点数始终大于0的最小初始值
}