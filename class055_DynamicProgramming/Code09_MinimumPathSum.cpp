#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>

using namespace std;

/**
 * 最小路径和问题 - C++实现
 * 算法思路：动态规划
 * 时间复杂度：O(m * n)
 * 空间复杂度：O(m * n)，可优化到O(n)
 * 
 * 核心思想：
 * 1. 使用二维DP数组dp[i][j]表示从起点(0,0)到位置(i,j)的最小路径和
 * 2. 状态转移：路径和等于从上面和左边来的较小路径和加上当前位置的值
 * 3. 边界条件：第一行只能从左边来，第一列只能从上面来
 */

class MinimumPathSum {
public:
    /**
     * 计算从左上角到右下角的最小路径和
     * @param grid m x n 的网格，包含非负整数
     * @return 最小路径和
     */
    int minPathSum(vector<vector<int>>& grid) {
        int m = grid.size();
        if (m == 0) return 0;
        int n = grid[0].size();
        
        // 输入验证
        if (n == 0) return 0;
        
        // dp[i][j] 表示从起点到位置(i,j)的最小路径和
        vector<vector<int>> dp(m, vector<int>(n, 0));
        
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
                dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j];
            }
        }
        
        return dp[m-1][n-1];
    }
    
    /**
     * 空间优化版本：使用滚动数组将空间复杂度优化到O(n)
     * @param grid m x n 的网格
     * @return 最小路径和
     */
    int minPathSumOptimized(vector<vector<int>>& grid) {
        int m = grid.size();
        if (m == 0) return 0;
        int n = grid[0].size();
        
        // 输入验证
        if (n == 0) return 0;
        
        // 使用一维数组优化空间
        vector<int> dp(n, 0);
        
        // 初始化第一行
        dp[0] = grid[0][0];
        for (int j = 1; j < n; j++) {
            dp[j] = dp[j-1] + grid[0][j];
        }
        
        // 填充dp表
        for (int i = 1; i < m; i++) {
            // 更新第一列
            dp[0] = dp[0] + grid[i][0];
            
            for (int j = 1; j < n; j++) {
                // 路径和等于从上面和左边来的较小路径和加上当前位置的值
                dp[j] = min(dp[j], dp[j-1]) + grid[i][j];
            }
        }
        
        return dp[n-1];
    }
    
    /**
     * 单元测试函数
     */
    void test() {
        // 测试用例1
        vector<vector<int>> grid1 = {
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        };
        int result1 = minPathSum(grid1);
        cout << "Test 1 - Basic: " << result1 << " (Expected: 7)" << endl;
        
        // 测试用例2
        vector<vector<int>> grid2 = {
            {1, 2, 3},
            {4, 5, 6}
        };
        int result2 = minPathSum(grid2);
        cout << "Test 2 - Small Grid: " << result2 << " (Expected: 12)" << endl;
        
        // 测试用例3
        vector<vector<int>> grid3 = {
            {1, 2},
            {1, 1}
        };
        int result3 = minPathSum(grid3);
        cout << "Test 3 - 2x2 Grid: " << result3 << " (Expected: 3)" << endl;
        
        // 测试用例4
        vector<vector<int>> grid4 = {
            {1, 3, 1, 2},
            {1, 5, 1, 3},
            {4, 2, 1, 1}
        };
        int result4 = minPathSum(grid4);
        cout << "Test 4 - Larger Grid: " << result4 << " (Expected: 8)" << endl;
        
        // 测试优化版本
        int result1Opt = minPathSumOptimized(grid1);
        cout << "Test 1 - Optimized: " << result1Opt << " (Expected: 7)" << endl;
        
        // 性能测试：大规模数据
        vector<vector<int>> largeGrid(100, vector<int>(100, 1));
        
        cout << "Performance test started..." << endl;
        int largeResult = minPathSumOptimized(largeGrid);
        cout << "Performance test completed. Result: " << largeResult << endl;
    }
};

int main() {
    MinimumPathSum solution;
    
    // 运行单元测试
    solution.test();
    
    return 0;
}

/*
 * 相关题目扩展：
 * 
 * 1. LeetCode 62 - Unique Paths (不同路径)
 *    链接：https://leetcode.cn/problems/unique-paths/
 *    区别：计算不同路径的数量而不是路径和
 *    算法：组合数学或动态规划
 * 
 * 2. LeetCode 63 - Unique Paths II (不同路径 II)
 *    链接：https://leetcode.cn/problems/unique-paths-ii/
 *    区别：网格中有障碍物，计算不同路径的数量
 *    算法：二维动态规划
 * 
 * 3. LeetCode 174 - Dungeon Game (地下城游戏)
 *    链接：https://leetcode.cn/problems/dungeon-game/
 *    区别：骑士需要从左上角到右下角，保证健康点数始终大于0的最小初始值
 *    算法：从右下角向左上角动态规划
 * 
 * 4. LeetCode 64 - Minimum Path Sum (本题)
 *    链接：https://leetcode.cn/problems/minimum-path-sum/
 *    算法：二维动态规划
 * 
 * 5. LeetCode 120 - Triangle (三角形最小路径和)
 *    链接：https://leetcode.cn/problems/triangle/
 *    区别：三角形网格的最小路径和
 *    算法：动态规划，从底向上
 * 
 * 6. LeetCode 931 - Minimum Falling Path Sum (下降路径最小和)
 *    链接：https://leetcode.cn/problems/minimum-falling-path-sum/
 *    区别：从第一行任意位置开始，到最后一行的最小路径和
 *    算法：动态规划
 * 
 * 7. LeetCode 1289 - Minimum Falling Path Sum II (下降路径最小和II)
 *    链接：https://leetcode.cn/problems/minimum-falling-path-sum-ii/
 *    区别：不能选择同一列相邻行的元素
 *    算法：动态规划+优化
 * 
 * 8. LeetCode 1301 - Number of Paths with Max Score (最大得分路径数)
 *    链接：https://leetcode.cn/problems/number-of-paths-with-max-score/
 *    区别：计算最大得分和对应的路径数
 *    算法：动态规划
 * 
 * 9. LeetCode 1575 - Count All Possible Routes (统计所有可能的路线)
 *    链接：https://leetcode.cn/problems/count-all-possible-routes/
 *    区别：在图中统计从起点到终点的所有可能路线
 *    算法：动态规划+记忆化搜索
 * 
 * 10. LeetCode 980 - Unique Paths III (不同路径III)
 *     链接：https://leetcode.cn/problems/unique-paths-iii/
 *     区别：需要访问所有空单元格一次且仅一次
 *     算法：回溯+状态压缩
 * 
 * 算法技巧总结：
 * 1. 动态规划是解决网格路径问题的核心方法
 * 2. 状态转移方程：dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
 * 3. 边界条件处理：第一行和第一列的特殊情况
 * 4. 空间优化：使用滚动数组将空间复杂度从O(m*n)优化到O(n)
 * 5. 输入验证：检查网格是否为空
 * 
 * C++工程化考量：
 * 1. 内存管理：使用vector避免手动内存管理
 * 2. 性能优化：使用引用避免不必要的拷贝
 * 3. 异常安全：使用RAII原则管理资源
 * 4. 代码可读性：使用有意义的变量名和注释
 * 5. 测试覆盖：包含边界测试、性能测试
 * 
 * 调试技巧：
 * 1. 使用cout输出中间变量值
 * 2. 使用gdb调试器进行调试
 * 3. 编写单元测试验证算法正确性
 * 4. 使用性能分析工具优化代码效率
 */