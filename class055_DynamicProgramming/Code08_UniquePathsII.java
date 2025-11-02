package class127;

// Unique Paths II
// 一个机器人位于一个 m x n 网格的左上角，机器人每次只能向下或者向右移动一步
// 机器人试图到达网格的右下角
// 网格中有障碍物，用 1 表示，空位置用 0 表示
// 计算从左上角到右下角有多少条不同的路径
// 测试链接 : https://leetcode.cn/problems/unique-paths-ii/

/**
 * 算法思路：
 * 1. 这是一个动态规划问题，类似于Unique Paths I，但增加了障碍物的处理
 * 2. 使用二维动态规划 dp[i][j]，表示从起点(0,0)到位置(i,j)的不同路径数
 * 3. 状态转移方程：
 *    - 如果 obstacleGrid[i][j] == 1，表示有障碍物，dp[i][j] = 0
 *    - 如果 i == 0 且 j == 0，dp[0][0] = 1（起点）
 *    - 如果 i == 0，只能从左边来，dp[i][j] = dp[i][j-1]
 *    - 如果 j == 0，只能从上面来，dp[i][j] = dp[i-1][j]
 *    - 其他情况，可以从上面或左边来，dp[i][j] = dp[i-1][j] + dp[i][j-1]
 * 4. 初始化：
 *    - 如果起点(0,0)或终点(m-1,n-1)有障碍物，返回0
 * 5. 结果：
 *    - dp[m-1][n-1]即为所求的不同路径数
 *
 * 时间复杂度：O(m * n)
 * 空间复杂度：O(m * n)
 */
public class Code08_UniquePathsII {

    /**
     * 计算从左上角到右下角的不同路径数
     * @param obstacleGrid m x n 的网格，1表示障碍物，0表示空位置
     * @return 不同路径的数量
     */
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        
        // 如果起点或终点有障碍物，直接返回0
        if (obstacleGrid[0][0] == 1 || obstacleGrid[m-1][n-1] == 1) {
            return 0;
        }
        
        // dp[i][j] 表示从起点到位置(i,j)的不同路径数
        int[][] dp = new int[m][n];
        
        // 初始化起点
        dp[0][0] = 1;
        
        // 初始化第一行
        for (int j = 1; j < n; j++) {
            dp[0][j] = (obstacleGrid[0][j] == 1) ? 0 : dp[0][j-1];
        }
        
        // 初始化第一列
        for (int i = 1; i < m; i++) {
            dp[i][0] = (obstacleGrid[i][0] == 1) ? 0 : dp[i-1][0];
        }
        
        // 填充dp表
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // 如果当前位置有障碍物，则路径数为0
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    // 否则路径数等于从上面和左边来的路径数之和
                    dp[i][j] = dp[i-1][j] + dp[i][j-1];
                }
            }
        }
        
        return dp[m-1][n-1];
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] obstacleGrid1 = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        System.out.println("测试用例1结果: " + uniquePathsWithObstacles(obstacleGrid1)); // 预期输出: 2
        
        // 测试用例2
        int[][] obstacleGrid2 = {
            {0, 1},
            {0, 0}
        };
        System.out.println("测试用例2结果: " + uniquePathsWithObstacles(obstacleGrid2)); // 预期输出: 1
        
        // 测试用例3
        int[][] obstacleGrid3 = {
            {0, 0},
            {1, 1},
            {0, 0}
        };
        System.out.println("测试用例3结果: " + uniquePathsWithObstacles(obstacleGrid3)); // 预期输出: 0
        
        // 测试用例4
        int[][] obstacleGrid4 = {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };
        System.out.println("测试用例4结果: " + uniquePathsWithObstacles(obstacleGrid4)); // 预期输出: 10
    }

    // 相关题目：
    // 1. LeetCode 62. Unique Paths (不同路径)
    //    链接：https://leetcode.cn/problems/unique-paths/
    //    区别：没有障碍物的网格路径问题
    // 2. LeetCode 64. Minimum Path Sum (最小路径和)
    //    链接：https://leetcode.cn/problems/minimum-path-sum/
    //    区别：求从左上角到右下角的最小路径和
    // 3. LeetCode 174. Dungeon Game (地下城游戏)
    //    链接：https://leetcode.cn/problems/dungeon-game/
    //    区别：骑士需要从左上角到右下角，保证健康点数始终大于0的最小初始值
}