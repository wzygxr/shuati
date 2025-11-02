#include <iostream>
#include <algorithm>
#include <cstring>

/**
 * 最小路径和 (Minimum Path Sum) - C++实现
 * 
 * 题目描述：
 * 给定一个包含非负整数的 m x n 网格 grid，请找出一条从左上角到右下角的路径，
 * 使得路径上的数字总和为最小。每次只能向下或者向右移动一步。
 * 
 * 题目来源：LeetCode 64. 最小路径和
 * 题目链接：https://leetcode.cn/problems/minimum-path-sum/
 * 
 * 解题思路分析：
 * 1. 严格位置依赖的动态规划：自底向上填表，避免递归开销
 * 2. 空间优化版本：利用滚动数组思想，只保存必要的状态
 * 
 * 时间复杂度分析：
 * - 动态规划：O(m*n) - 需要遍历整个网格
 * - 空间优化DP：O(m*n) - 需要遍历整个网格
 * 
 * 空间复杂度分析：
 * - 动态规划：O(m*n) - 使用二维DP数组
 * - 空间优化DP：O(min(m,n)) - 只使用一维数组
 * 
 * 是否最优解：是 - 动态规划是解决此类最优路径问题的标准方法
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性，处理空网格等特殊情况
 * 2. 边界处理：处理单行、单列网格等边界情况
 * 3. 性能优化：空间压缩降低内存使用
 * 4. 可测试性：提供完整的测试用例
 * 
 * C++特性：
 * - 使用数组而非vector，性能更高但需要预设最大尺寸
 * - 需要手动管理内存，注意数组边界
 * - 使用const引用传递参数，避免不必要的拷贝
 * 
 * 跨语言差异：
 * - 与Java相比：需要预设数组最大尺寸，手动管理内存
 * - 与Python相比：性能更高，但代码更复杂
 */

#define MAXN 100  // 预设网格最大尺寸为100x100

class Code01_MinimumPathSum {
public:
    /**
     * 方法1：严格位置依赖的动态规划方法
     * 
     * 算法思想：自底向上填表，从起点开始逐步计算每个位置的最小路径和
     * 状态定义：dp[i][j] 表示从(0,0)到(i,j)的最小路径和
     * 状态转移方程：
     * - 当i=0且j=0：dp[0][0] = grid[0][0]
     * - 当i=0且j>0：dp[0][j] = dp[0][j-1] + grid[0][j]（只能从左方来）
     * - 当i>0且j=0：dp[i][0] = dp[i-1][0] + grid[i][0]（只能从上方来）
     * - 当i>0且j>0：dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
     * 
     * 时间复杂度：O(m*n) - 需要遍历整个网格
     * 空间复杂度：O(m*n) - 使用二维DP数组
     * 
     * @param grid 二维网格数组
     * @param n 网格行数
     * @param m 网格列数
     * @return 最小路径和
     * 
     * C++注意事项：
     * - 使用静态数组，需要预设最大尺寸MAXN
     * - 数组作为参数传递时，需要显式传递维度信息
     * - 注意数组边界检查，避免越界访问
     */
    static int minPathSum3(int grid[][MAXN], int n, int m) {
        // 输入验证：检查网格维度是否合法
        if (n <= 0 || m <= 0) {
            return 0;
        }
        
        // 创建DP数组：使用静态数组，性能更高但需要预设尺寸
        int dp[MAXN][MAXN];
        
        // 初始化起点：dp[0][0] = grid[0][0]
        dp[0][0] = grid[0][0];
        
        // 初始化第一列：只能从上方来
        for (int i = 1; i < n; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        
        // 初始化第一行：只能从左方来
        for (int j = 1; j < m; j++) {
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }
        
        // 填充其余位置：对于非边界位置，可以从上方或左方来
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                // 使用条件判断代替Math.min，避免函数调用开销
                if (dp[i - 1][j] < dp[i][j - 1]) {
                    dp[i][j] = dp[i - 1][j] + grid[i][j];
                } else {
                    dp[i][j] = dp[i][j - 1] + grid[i][j];
                }
            }
        }
        
        // 返回终点位置的最小路径和
        return dp[n - 1][m - 1];
    }

    /**
     * 方法2：严格位置依赖的动态规划 + 空间压缩技巧
     * 
     * 算法思想：利用滚动数组思想，将空间复杂度从O(m*n)优化到O(min(m,n))
     * 关键观察：在计算第i行时，只需要第i-1行的dp值和当前行已经计算的部分
     * 因此可以使用一维数组来存储状态，通过滚动更新来节省空间
     * 
     * 状态定义：dp[j] 表示当前行第j列的最小路径和
     * 状态转移：
     * - 对于第一列：只能从上方来，dp[0] = dp[0] + grid[i][0]
     * - 对于其他列：dp[j] = min(dp[j-1], dp[j]) + grid[i][j]
     * 
     * 时间复杂度：O(m*n) - 需要遍历整个网格
     * 空间复杂度：O(min(m,n)) - 只使用一维数组
     * 
     * @param grid 二维网格数组
     * @param n 网格行数
     * @param m 网格列数
     * @return 最小路径和
     * 
     * C++优化技巧：
     * - 使用一维静态数组，避免动态内存分配
     * - 选择较小的维度作为数组长度，进一步优化空间
     * - 注意数组边界，确保不越界访问
     */
    static int minPathSum4(int grid[][MAXN], int n, int m) {
        // 输入验证
        if (n <= 0 || m <= 0) {
            return 0;
        }
        
        // 空间优化：使用一维数组代替二维数组
        // 选择较小的维度作为数组长度，这里假设列数m较小
        int dp[MAXN];
        
        // 初始化第一行：只能从左方来
        dp[0] = grid[0][0];
        for (int j = 1; j < m; j++) {
            dp[j] = dp[j - 1] + grid[0][j];
        }
        
        // 逐行更新：从第二行开始处理
        for (int i = 1; i < n; i++) {
            // 更新第一列：只能从上方来
            dp[0] = dp[0] + grid[i][0];
            
            // 更新其余列：可以从上方或左方来
            for (int j = 1; j < m; j++) {
                // 使用条件判断选择较小的路径和
                if (dp[j - 1] < dp[j]) {
                    dp[j] = dp[j - 1] + grid[i][j];
                } else {
                    dp[j] = dp[j] + grid[i][j];
                }
            }
        }
        
        // 返回终点位置的最小路径和
        return dp[m - 1];
    }

    /**
     * 补充题目1：0-1背包问题标准实现（二维DP）
     * 
     * 问题描述：给定n个物品，每个物品有重量w[i]和价值v[i]，背包容量为C
     * 选择一些物品放入背包，使得总重量不超过C，且总价值最大
     * 
     * 算法思想：动态规划，状态dp[i][j]表示前i个物品，背包容量为j时的最大价值
     * 状态转移方程：
     * - 不选择第i个物品：dp[i][j] = dp[i-1][j]
     * - 选择第i个物品：dp[i][j] = dp[i-1][j-w[i-1]] + v[i-1]（如果j >= w[i-1]）
     * - 取两种情况的最大值
     * 
     * 时间复杂度：O(n*C) - 需要填充n*C的DP表
     * 空间复杂度：O(n*C) - 使用二维DP数组
     * 
     * @param w 物品重量数组
     * @param v 物品价值数组
     * @param n 物品数量
     * @param C 背包容量
     * @return 背包能装的最大价值
     * 
     * C++注意事项：
     * - 使用静态数组，需要预设最大尺寸MAXN
     * - 注意数组索引从0开始，物品索引需要调整
     */
    static int knapsack1(int w[], int v[], int n, int C) {
        // 输入验证
        if (w == nullptr || v == nullptr || n <= 0 || C <= 0) {
            return 0;
        }
        
        // dp[i][j] 表示前i个物品，背包容量为j时的最大价值
        int dp[MAXN][MAXN];
        
        // 初始化DP数组为0
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= C; j++) {
                dp[i][j] = 0;
            }
        }
        
        // 逐行填充DP表：i从1到n，表示考虑前i个物品
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= C; j++) {
                // 不选择第i个物品：最大价值等于前i-1个物品在容量j时的最大价值
                dp[i][j] = dp[i - 1][j];
                // 选择第i个物品：如果当前容量j足够放下第i个物品
                if (j >= w[i - 1]) {
                    // 计算选择当前物品的价值
                    int temp = dp[i - 1][j - w[i - 1]] + v[i - 1];
                    // 取最大值
                    if (temp > dp[i][j]) {
                        dp[i][j] = temp;
                    }
                }
            }
        }
        
        return dp[n][C];
    }
    
    /**
     * 补充题目2：0-1背包问题空间优化版本（一维DP）
     * 
     * 算法思想：利用滚动数组思想，将二维DP压缩为一维DP
     * 关键观察：在计算第i行时，只需要第i-1行的数据
     * 因此可以使用一维数组，通过逆序遍历容量来避免覆盖需要的数据
     * 
     * 状态定义：dp[j] 表示背包容量为j时的最大价值
     * 状态转移：dp[j] = max(dp[j], dp[j-w[i]] + v[i])
     * 
     * 时间复杂度：O(n*C) - 需要遍历所有物品和容量
     * 空间复杂度：O(C) - 只使用一维数组
     * 
     * @param w 物品重量数组
     * @param v 物品价值数组
     * @param n 物品数量
     * @param C 背包容量
     * @return 背包能装的最大价值
     * 
     * 关键点：必须逆序遍历容量，避免同一物品被多次选择
     */
    static int knapsack2(int w[], int v[], int n, int C) {
        // 输入验证
        if (w == nullptr || v == nullptr || n <= 0 || C <= 0) {
            return 0;
        }
        
        // 只使用一维数组：dp[j]表示背包容量为j时的最大价值
        int dp[MAXN];
        
        // 初始化DP数组为0
        for (int j = 0; j <= C; j++) {
            dp[j] = 0;
        }
        
        // 遍历每个物品
        for (int i = 0; i < n; i++) {
            // 关键：逆序遍历容量，从大到小遍历
            // 这样可以确保每个物品只被选择一次
            for (int j = C; j >= w[i]; j--) {
                // 计算选择当前物品的价值
                int temp = dp[j - w[i]] + v[i];
                // 取最大值
                if (temp > dp[j]) {
                    dp[j] = temp;
                }
            }
        }
        
        return dp[C];
    }
    
    /**
     * 补充题目3：分割等和子集问题（0-1背包的变形）
     * 
     * 问题描述：给定一个只包含正整数的非空数组，判断是否可以将这个数组分割成两个子集，
     * 使得两个子集的元素和相等。
     * 
     * 算法思想：转化为0-1背包问题
     * - 背包容量：数组总和的一半（target）
     * - 物品重量：数组中的每个数字
     * - 物品价值：不重要，这里关心的是能否恰好装满背包
     * 
     * 状态定义：dp[j] 表示能否用数组中的数字凑出和j
     * 状态转移：dp[j] = dp[j] || dp[j-num]
     * 
     * 时间复杂度：O(n*target) - 其中target为数组总和的一半
     * 空间复杂度：O(target) - 使用一维布尔数组
     * 
     * @param nums 正整数数组
     * @param n 数组长度
     * @return 是否可以将数组分割成两个和相等的子集
     */
    static bool canPartition(int nums[], int n) {
        // 输入验证
        if (nums == nullptr || n < 2) {
            return false;
        }
        
        // 计算数组总和
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
        }
        
        // 如果总和为奇数，不可能分割成两个和相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        // 转化为0-1背包问题：能否从数组中选择一些数，使得它们的和等于target
        bool dp[MAXN] = {false};
        dp[0] = true; // 空集的和为0，总是可以凑出
        
        // 遍历数组中的每个数字
        for (int i = 0; i < n; i++) {
            // 逆序遍历容量，避免同一数字被多次使用
            for (int j = target; j >= nums[i]; j--) {
                // 状态转移：当前容量j能否凑出 = 原来就能凑出 或 减去当前数字后能凑出
                if (dp[j - nums[i]]) {
                    dp[j] = true;
                }
            }
        }
        
        return dp[target];
    }
    
    /**
     * 测试方法：验证所有算法的正确性
     * 
     * 测试用例设计：
     * 1. 正常网格测试
     * 2. 边界情况测试（单行、单列、单元素）
     * 3. 背包问题测试
     * 4. 分割等和子集测试
     * 
     * 测试目的：确保各种实现方法结果一致，验证算法正确性
     */
    static void test() {
        std::cout << "=== 最小路径和算法测试 ===" << std::endl;
        
        // 测试用例1：标准3x3网格
        int grid1[][MAXN] = {
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        };
        std::cout << "测试用例1 - 标准3x3网格:" << std::endl;
        std::cout << "动态规划: " << minPathSum3(grid1, 3, 3) << std::endl;
        std::cout << "空间优化DP: " << minPathSum4(grid1, 3, 3) << std::endl;
        std::cout << "预期结果: 7" << std::endl;
        std::cout << std::endl;
        
        // 测试用例2：2x3网格
        int grid2[][MAXN] = {
            {1, 2, 3},
            {4, 5, 6}
        };
        std::cout << "测试用例2 - 2x3网格:" << std::endl;
        std::cout << "动态规划: " << minPathSum3(grid2, 2, 3) << std::endl;
        std::cout << "空间优化DP: " << minPathSum4(grid2, 2, 3) << std::endl;
        std::cout << "预期结果: 12" << std::endl;
        std::cout << std::endl;
        
        // 测试用例3：单行网格
        int grid3[][MAXN] = {{1, 2, 3, 4, 5}};
        std::cout << "测试用例3 - 单行网格:" << std::endl;
        std::cout << "动态规划: " << minPathSum3(grid3, 1, 5) << std::endl;
        std::cout << "空间优化DP: " << minPathSum4(grid3, 1, 5) << std::endl;
        std::cout << "预期结果: 15" << std::endl;
        std::cout << std::endl;
        
        // 测试用例4：单列网格
        int grid4[][MAXN] = {{1}, {2}, {3}, {4}, {5}};
        std::cout << "测试用例4 - 单列网格:" << std::endl;
        std::cout << "动态规划: " << minPathSum3(grid4, 5, 1) << std::endl;
        std::cout << "空间优化DP: " << minPathSum4(grid4, 5, 1) << std::endl;
        std::cout << "预期结果: 15" << std::endl;
        std::cout << std::endl;
        
        // 测试用例5：单元素网格
        int grid5[][MAXN] = {{5}};
        std::cout << "测试用例5 - 单元素网格:" << std::endl;
        std::cout << "动态规划: " << minPathSum3(grid5, 1, 1) << std::endl;
        std::cout << "空间优化DP: " << minPathSum4(grid5, 1, 1) << std::endl;
        std::cout << "预期结果: 5" << std::endl;
        std::cout << std::endl;
        
        std::cout << "=== 0-1背包问题测试 ===" << std::endl;
        int w[] = {2, 3, 4, 5};
        int v[] = {3, 4, 5, 6};
        int C = 8;
        int n = 4;
        std::cout << "物品重量: [2, 3, 4, 5]" << std::endl;
        std::cout << "物品价值: [3, 4, 5, 6]" << std::endl;
        std::cout << "背包容量: " << C << std::endl;
        std::cout << "标准DP最大价值: " << knapsack1(w, v, n, C) << std::endl;
        std::cout << "空间优化最大价值: " << knapsack2(w, v, n, C) << std::endl;
        std::cout << "预期结果: 9" << std::endl;
        std::cout << std::endl;
        
        std::cout << "=== 分割等和子集测试 ===" << std::endl;
        int nums1[] = {1, 5, 11, 5};  // 可以分割
        int nums2[] = {1, 2, 3, 5};   // 无法分割
        std::cout << "数组1 [1, 5, 11, 5] 能否分割: " << (canPartition(nums1, 4) ? "true" : "false") << std::endl;
        std::cout << "数组2 [1, 2, 3, 5] 能否分割: " << (canPartition(nums2, 4) ? "true" : "false") << std::endl;
        std::cout << "预期结果: true, false" << std::endl;
        
        std::cout << std::endl << "=== 测试完成 ===" << std::endl;
    }
};

// 主函数：运行测试用例
int main() {
    Code01_MinimumPathSum::test();
    return 0;
}