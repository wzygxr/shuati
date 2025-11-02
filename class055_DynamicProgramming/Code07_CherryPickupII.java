package class127;

// Cherry Pickup II
// 给定一个 rows x cols 的矩阵 grid，表示一个樱桃田，grid[i][j] 表示在位置 (i,j) 的樱桃数量
// 两个机器人同时从矩阵顶部开始收集樱桃：
// - 机器人1从位置 (0, 0) 开始
// - 机器人2从位置 (0, cols-1) 开始
// 两个机器人的移动规则：
// - 从任何单元格 (i, j) 出发，机器人可以移动到下一行的三个单元格之一：
//   (i+1, j-1), (i+1, j), (i+1, j+1)
// - 机器人不能移动到矩阵边界之外
// - 两个机器人都必须到达最后一行
// 樱桃收集规则：
// - 当机器人经过一个单元格时，它会收集该单元格的所有樱桃
// - 收集后，该单元格变为空（0个樱桃）
// - 如果两个机器人同时占据同一单元格，则只有一个机器人收集该单元格的樱桃
// 目标是找到两个机器人一起收集的最大樱桃总数
// 测试链接 : https://leetcode.cn/problems/cherry-pickup-ii/

/**
 * 算法思路深度解析：
 * 1. 这是一个典型的三维动态规划问题，两个机器人同时从顶部向底部移动
 *    - 与摘樱桃I不同，这里两个机器人有明确的起始位置和移动规则
 *    - 问题的关键是如何有效跟踪两个机器人的路径并避免重复计算
 * 2. 使用三维动态规划 dp[i][j1][j2]，其中：
 *    - i 表示当前行
 *    - j1 表示机器人1的列位置
 *    - j2 表示机器人2的列位置
 *    - dp[i][j1][j2] 表示当机器人1在(i,j1)，机器人2在(i,j2)时能收集的最大樱桃数
 * 3. 状态转移方程：
 *    - 对于每个位置(i,j1,j2)，考虑两个机器人从前一行的可能位置转移而来
 *    - 每个机器人可以从三个位置转移：(i-1, j-1), (i-1, j), (i-1, j+1)
 *    - 所以总共有 3*3=9 种可能的前驱状态组合
 *    - 关键优化：如果两个机器人在同一位置，只计算一次樱桃数量
 * 4. 初始化策略：
 *    - dp[0][0][cols-1] = grid[0][0] + grid[0][cols-1]（两个机器人的起始位置）
 *    - 使用-1作为未访问状态的标记，避免混淆
 * 5. 结果提取：
 *    - 在最后一行中找到所有可能位置组合的最大值
 *    - 不需要考虑边界情况，因为题目保证两个机器人都能到达最后一行
 *
 * 时间复杂度分析：
 * - 状态总数：O(rows * cols^2)
 * - 每个状态需要考虑9种前驱状态
 * - 总时间复杂度：O(rows * cols^2 * 9) = O(rows * cols^2)
 *
 * 空间复杂度分析：
 * - 三维DP数组大小：O(rows * cols^2)
 * - 可以通过滚动数组优化到O(cols^2)，只保存当前行和上一行的状态
 *
 * Java实现注意事项：
 * 1. 数组初始化：使用三层循环初始化三维数组为-1
 * 2. 边界条件处理：确保机器人不越出矩阵边界
 * 3. 性能优化：可以使用滚动数组减少内存使用
 * 4. 代码可读性：使用有意义的变量名和适当的注释
 *
 * 工程化考量：
 * 1. 输入验证：可以添加对输入矩阵的合法性检查
 * 2. 异常处理：处理空矩阵或特殊情况
 * 3. 测试用例：覆盖各种边界情况和典型场景
 * 4. 空间优化：对于大规模数据，考虑使用滚动数组
 * 5. 多线程优化：对于非常大的矩阵，可以考虑并行计算
 */
public class Code07_CherryPickupII {

    /**
     * 计算两个机器人能收集的最大樱桃数
     * @param grid rows x cols 的矩阵
     * @return 两个机器人能收集的最大樱桃数
     */
    public static int cherryPickup(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        
        // dp[i][j1][j2] 表示当机器人1在(i,j1)，机器人2在(i,j2)时能收集的最大樱桃数
        int[][][] dp = new int[rows][cols][cols];
        
        // 初始化dp数组为-1，表示未访问过
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < cols; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }
        
        // 初始化起始位置
        dp[0][0][cols - 1] = grid[0][0] + grid[0][cols - 1];
        
        // 填充dp表
        for (int i = 1; i < rows; i++) {
            for (int j1 = 0; j1 < cols; j1++) {
                for (int j2 = 0; j2 < cols; j2++) {
                    // 计算当前位置的樱桃数
                    int cherries = (j1 == j2) ? grid[i][j1] : (grid[i][j1] + grid[i][j2]);
                    
                    // 检查所有可能的前驱状态
                    for (int p1 = j1 - 1; p1 <= j1 + 1; p1++) {
                        for (int p2 = j2 - 1; p2 <= j2 + 1; p2++) {
                            // 检查前驱状态是否有效
                            if (p1 >= 0 && p1 < cols && p2 >= 0 && p2 < cols && dp[i - 1][p1][p2] != -1) {
                                dp[i][j1][j2] = Math.max(dp[i][j1][j2], dp[i - 1][p1][p2] + cherries);
                            }
                        }
                    }
                }
            }
        }
        
        // 找到最后一行的最大值
        int result = 0;
        for (int j1 = 0; j1 < cols; j1++) {
            for (int j2 = 0; j2 < cols; j2++) {
                result = Math.max(result, dp[rows - 1][j1][j2]);
            }
        }
        
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] grid1 = {
            {3, 1, 1},
            {2, 5, 1},
            {1, 5, 5}
        };
        System.out.println("测试用例1结果: " + cherryPickup(grid1)); // 预期输出: 21
        
        // 测试用例2
        int[][] grid2 = {
            {1, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 1}
        };
        System.out.println("测试用例2结果: " + cherryPickup(grid2)); // 预期输出: 8
        
        // 测试用例3
        int[][] grid3 = {
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 0}
        };
        System.out.println("测试用例3结果: " + cherryPickup(grid3)); // 预期输出: 94
    }

    // 类似题目与训练拓展：
    // 1. LeetCode 741. Cherry Pickup (摘樱桃 I)
    //    链接：https://leetcode.cn/problems/cherry-pickup/
    //    区别：一个人从(0,0)走到(n-1,n-1)再走回(0,0)，求最大收集樱桃数
    //    算法：三维动态规划，转化为两个人同时从起点到终点的问题
    //
    // 2. LeetCode 64. Minimum Path Sum (最小路径和)
    //    链接：https://leetcode.cn/problems/minimum-path-sum/
    //    区别：求从左上角到右下角的最小路径和，每步只能向下或向右
    //    算法：二维动态规划，dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
    //
    // 3. LeetCode 174. Dungeon Game (地下城游戏)
    //    链接：https://leetcode.cn/problems/dungeon-game/
    //    区别：骑士需要从左上角到右下角，保证健康点数始终大于0的最小初始值
    //    算法：从右下角向左上角动态规划，dp[i][j] = max(1, min(dp[i+1][j], dp[i][j+1]) - dungeon[i][j])
    //
    // 4. LeetCode 1463. Cherry Pickup II (本题)
    //    链接：https://leetcode.cn/problems/cherry-pickup-ii/
    //    算法：三维动态规划，两个机器人同时移动的路径规划问题
    //
    // 5. LeetCode 62. Unique Paths (不同路径)
    //    链接：https://leetcode.cn/problems/unique-paths/
    //    区别：计算从左上角到右下角的不同路径数量，每步只能向下或向右
    //    算法：组合数学或二维动态规划
    //
    // 6. LeetCode 63. Unique Paths II (不同路径 II)
    //    链接：https://leetcode.cn/problems/unique-paths-ii/
    //    区别：网格中有障碍物，计算不同路径数量
    //    算法：二维动态规划，遇到障碍物时dp[i][j] = 0
    //
    // 7. Codeforces 1296D - Fight with Monsters
    //    链接：https://codeforces.com/problemset/problem/1296/D
    //    区别：贪心策略解决怪物战斗问题，但状态转移思想类似
    //    算法：排序后贪心选择最优策略
    //
    // 8. AtCoder ABC159E - Dividing Chocolate
    //    链接：https://atcoder.jp/contests/abc159/tasks/abc159_e
    //    区别：二维网格分割问题，但需要类似的状态转移思路
    //    算法：前缀和+状态压缩动态规划
    //
    // 9. 洛谷 P1434 [SHOI2002] 滑雪
    //    链接：https://www.luogu.com.cn/problem/P1434
    //    区别：寻找最长滑雪路径，每步只能滑向相邻四个方向且高度更低的位置
    //    算法：记忆化搜索或拓扑排序动态规划
    //
    // 10. 牛客网 NC14552 方格取数
    //    链接：https://ac.nowcoder.com/acm/problem/14552
    //    区别：与摘樱桃I非常相似，也是两个人从左上角出发到右下角取数
    //    算法：三维动态规划，状态定义与摘樱桃I类似
    //
    // 11. UVa 10913 - Walking on a Grid
    //    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1854
    //    区别：在网格中行走，有负数，最多允许k次负数
    //    算法：四维动态规划，状态定义为dp[i][j][k][d]，表示在(i,j)位置，已经经过k次负数，方向为d
    //
    // 12. SPOJ - SBANK
    //    链接：https://www.spoj.com/problems/SBANK/
    //    区别：银行账号排序问题，使用哈希表优化
    //    算法：哈希表+排序
    //
    // 13. HackerEarth - Roy and Coin Boxes
    //    链接：https://www.hackerearth.com/practice/data-structures/arrays/1-d/practice-problems/algorithm/roy-and-coin-boxes-1/description/
    //    区别：区间更新问题，使用差分数组优化
    //    算法：差分数组+前缀和
    //
    // 14. 杭电 HDU 1024 - Max Sum Plus Plus
    //    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1024
    //    区别：最大m段子数组和问题
    //    算法：二维动态规划优化为一维
    //
    // 15. Codeforces 1295E - Permutation Separation
    //    链接：https://codeforces.com/problemset/problem/1295/E
    //    区别：排列分割问题，需要找到最优分割点
    //    算法：前缀和+动态规划
    //
    // 算法本质与技巧总结：
    // 1. 三维状态设计：同时跟踪两个机器人的位置，有效表达复杂状态
    // 2. 状态转移优化：考虑所有可能的前驱状态组合
    // 3. 避免重复计算：同一位置的樱桃只计算一次
    // 4. 边界条件处理：仔细检查机器人移动是否越界
    // 5. 空间优化：可以使用滚动数组优化空间复杂度
    //
    // Java工程化实战建议：
    // 1. 测试用例设计：
    //    - 空矩阵或单行矩阵
    //    - 两个机器人起始位置重合的情况（但题目保证起始位置不同）
    //    - 矩阵中有大量0或负数的情况
    //    - 最优路径需要两个机器人交叉的情况
    // 2. 性能优化：
    //    - 使用滚动数组将空间复杂度从O(rows*cols^2)优化到O(cols^2)
    //    - 预先计算矩阵的行数和列数，避免重复访问数组长度
    //    - 考虑使用并行流处理大规模数据
    // 3. 代码健壮性：
    //    - 添加输入合法性检查
    //    - 使用try-catch块处理可能的异常
    //    - 考虑使用Optional类型处理可能的空结果
    // 4. 调试技巧：
    //    - 添加打印语句输出中间状态
    //    - 使用断言验证关键条件
    //    - 考虑使用JDB调试器
    // 5. Java特性应用：
    //    - 可以使用Stream API简化最后一行最大值的计算
    //    - 考虑使用数组初始化工具类简化三维数组的初始化
    //    - 可以使用Enum定义机器人的移动方向
}