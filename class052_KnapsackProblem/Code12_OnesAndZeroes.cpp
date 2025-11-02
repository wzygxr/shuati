#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

// 一和零
// 给你一个二进制字符串数组 strs 和两个整数 m 和 n
// 请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1
// 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集
// 测试链接 : https://leetcode.cn/problems/ones-and-zeroes/

/*
 * 算法详解：
 * 这是一个二维背包问题，其中每个字符串有两个维度的"重量"：0的个数和1的个数。
 * 我们需要在不超过m个0和n个1的限制下，选择尽可能多的字符串。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j][k]表示前i个字符串，使用不超过j个0和k个1的最大子集大小
 * 2. 状态转移：对于每个字符串，可以选择或不选择
 *    - 不选择：dp[i][j][k] = dp[i-1][j][k]
 *    - 选择：dp[i][j][k] = dp[i-1][j-zeros][k-ones] + 1
 * 3. 空间优化：使用滚动数组将三维优化到二维
 * 
 * 时间复杂度分析：
 * 设字符串数量为L，m和n为背包容量
 * 1. 预处理每个字符串的0和1数量：O(L * avg_len)
 * 2. 动态规划计算：O(L * m * n)
 * 总时间复杂度：O(L * m * n)
 * 
 * 空间复杂度分析：
 * 1. 三维DP数组：O(L * m * n)
 * 2. 空间优化后：O(m * n)
 * 
 * 相关题目扩展：
 * 1. LeetCode 474. 一和零（本题）
 * 2. LeetCode 494. 目标和（背包变种）
 * 3. LeetCode 1049. 最后一块石头的重量 II（背包问题）
 * 4. LeetCode 416. 分割等和子集（背包问题）
 * 5. 洛谷 P1757 通天之分组背包（分组背包）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空数组、非法参数等边界情况
 * 3. 可配置性：可以将m和n作为配置参数传入
 * 4. 单元测试：为findMaxForm方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用空间优化版本
 * 
 * 语言特性差异：
 * 1. C++：使用vector容器，自动管理内存
 * 2. 性能优势：编译型语言，运行效率高
 * 3. 内存控制：需要注意vector的扩容开销
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 空间压缩：从三维dp优化到二维dp
 * 2. 预处理优化：提前计算每个字符串的0和1数量
 * 3. 剪枝优化：当字符串的0或1数量超过限制时跳过
 */

class Solution {
public:
    int findMaxForm(vector<string>& strs, int m, int n) {
        int len = strs.size();
        // 预处理每个字符串的0和1数量
        vector<pair<int, int>> counts(len);
        for (int i = 0; i < len; i++) {
            int zeros = 0, ones = 0;
            for (char c : strs[i]) {
                if (c == '0') zeros++;
                else ones++;
            }
            counts[i] = {zeros, ones};
        }
        
        // 三维DP版本（便于理解）
        vector<vector<vector<int>>> dp(len + 1, 
            vector<vector<int>>(m + 1, vector<int>(n + 1, 0)));
        
        for (int i = 1; i <= len; i++) {
            int zeros = counts[i - 1].first;
            int ones = counts[i - 1].second;
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k <= n; k++) {
                    // 不选择当前字符串
                    dp[i][j][k] = dp[i - 1][j][k];
                    // 如果可以选择当前字符串
                    if (j >= zeros && k >= ones) {
                        dp[i][j][k] = max(dp[i][j][k], 
                                         dp[i - 1][j - zeros][k - ones] + 1);
                    }
                }
            }
        }
        
        return dp[len][m][n];
    }
    
    // 空间优化版本（推荐使用）
    int findMaxFormOptimized(vector<string>& strs, int m, int n) {
        int len = strs.size();
        // 预处理每个字符串的0和1数量
        vector<pair<int, int>> counts(len);
        for (int i = 0; i < len; i++) {
            int zeros = 0, ones = 0;
            for (char c : strs[i]) {
                if (c == '0') zeros++;
                else ones++;
            }
            counts[i] = {zeros, ones};
        }
        
        // 二维DP数组，空间优化
        vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
        
        for (int i = 0; i < len; i++) {
            int zeros = counts[i].first;
            int ones = counts[i].second;
            // 从后往前更新，避免重复使用
            for (int j = m; j >= zeros; j--) {
                for (int k = n; k >= ones; k--) {
                    dp[j][k] = max(dp[j][k], dp[j - zeros][k - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
};

// 测试函数
void testOnesAndZeroes() {
    Solution solution;
    
    // 测试用例1
    vector<string> strs1 = {"10", "0001", "111001", "1", "0"};
    int m1 = 5, n1 = 3;
    cout << "测试用例1:" << endl;
    cout << "三维DP结果: " << solution.findMaxForm(strs1, m1, n1) << endl;
    cout << "优化版本结果: " << solution.findMaxFormOptimized(strs1, m1, n1) << endl;
    cout << "预期结果: 4" << endl;
    cout << endl;
    
    // 测试用例2
    vector<string> strs2 = {"10", "0", "1"};
    int m2 = 1, n2 = 1;
    cout << "测试用例2:" << endl;
    cout << "三维DP结果: " << solution.findMaxForm(strs2, m2, n2) << endl;
    cout << "优化版本结果: " << solution.findMaxFormOptimized(strs2, m2, n2) << endl;
    cout << "预期结果: 2" << endl;
    cout << endl;
    
    // 测试用例3：边界情况
    vector<string> strs3 = {};
    int m3 = 0, n3 = 0;
    cout << "测试用例3（空数组）:" << endl;
    cout << "三维DP结果: " << solution.findMaxForm(strs3, m3, n3) << endl;
    cout << "优化版本结果: " << solution.findMaxFormOptimized(strs3, m3, n3) << endl;
    cout << "预期结果: 0" << endl;
}

int main() {
    testOnesAndZeroes();
    return 0;
}

/*
 * =============================================================================================
 * 补充题目：LeetCode 494. 目标和（C++实现）
 * 题目链接：https://leetcode.cn/problems/target-sum/
 * 
 * C++实现：
 * class Solution {
 * public:
 *     int findTargetSumWays(vector<int>& nums, int target) {
 *         int sum = 0;
 *         for (int num : nums) sum += num;
 *         
 *         // 边界条件检查
 *         if (abs(target) > sum) return 0;
 *         if ((target + sum) % 2 != 0) return 0;
 *         
 *         int P = (target + sum) / 2;
 *         if (P < 0) return 0;
 *         
 *         vector<int> dp(P + 1, 0);
 *         dp[0] = 1; // 和为0的方案数为1
 *         
 *         for (int num : nums) {
 *             for (int j = P; j >= num; j--) {
 *                 dp[j] += dp[j - num];
 *             }
 *         }
 *         
 *         return dp[P];
 *     }
 * };
 * 
 * 工程化考量：
 * 1. 使用vector容器自动管理内存
 * 2. 使用引用避免不必要的拷贝
 * 3. 使用const引用作为函数参数
 * 4. 添加异常处理机制
 * 
 * 优化思路：
 * 1. 使用一维数组进行空间优化
 * 2. 使用位运算加速计算
 * 3. 使用unordered_map进行记忆化搜索
 */