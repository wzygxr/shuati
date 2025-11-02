#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <functional>
using namespace std;

/**
 * LeetCode 664. 奇怪的打印机
 * 题目链接：https://leetcode.cn/problems/strange-printer/
 * 
 * 题目描述：
 * 有台奇怪的打印机有以下两个特殊要求：
 * 1. 打印机每次只能打印由同一个字符组成的序列
 * 2. 每次可以在任意起始和结束位置打印新字符，并且会覆盖掉原来已有的字符
 * 给你一个字符串 s ，计算打印机打印它需要的最少打印次数
 * 
 * 解题思路：
 * 这是一个区间动态规划问题，关键在于理解打印策略。
 * 状态定义：dp[i][j]表示打印子串 s[i...j] 需要的最少打印次数。
 * 状态转移方程：
 * 1. 如果 s[i] == s[j]，则 dp[i][j] = dp[i][j-1]（可以在打印 s[i] 时一起打印 s[j]）
 * 2. 如果 s[i] != s[j]，则 dp[i][j] = min(dp[i][k] + dp[k+1][j]) for k in [i, j-1]
 * 
 * 时间复杂度：O(n³)
 * 空间复杂度：O(n²)
 * 
 * 工程化考量：
 * 1. 边界条件处理：单个字符只需打印1次
 * 2. 优化：可以预处理压缩连续重复字符，减少状态数量
 * 3. 输入验证：检查字符串是否为空
 * 
 * 相关题目扩展：
 * 1. LeetCode 664. 奇怪的打印机 - https://leetcode.cn/problems/strange-printer/
 * 2. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 3. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
 * 4. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
 * 5. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
 * 6. LeetCode 1246. 删除回文子数组 - https://leetcode.cn/problems/palindrome-removal/
 * 7. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
 * 8. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 9. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 10. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 */

class Solution {
public:
    // 方法一：记忆化搜索（递归实现）
    /**
     * @brief 使用记忆化搜索求解最少打印次数
     * 
     * @param s 输入字符串
     * @return int 最少打印次数
     */
    int strangePrinter1(string s) {
        /**
         * 【区间动态规划核心思想】
         * 1. 将大区间问题分解为小区间子问题
         * 2. 通过小区间的解组合出大区间的解
         * 3. 按照区间长度从小到大求解
         */
        if (s.empty()) {
            return 0;
        }
        
        int n = s.size();
        // 创建二维memo数组，初始值为-1表示未计算
        vector<vector<int>> memo(n, vector<int>(n, -1));
        
        // 调用记忆化搜索函数，计算整个字符串的最少打印次数
        return dfs(memo, s, 0, n - 1);
    }
    
private:
    /**
     * @brief 深度优先搜索，计算打印s[i...j]所需的最少次数
     * 
     * @param memo 记忆化数组，存储已计算过的子问题结果
     * @param s 输入字符串
     * @param i 区间起点
     * @param j 区间终点
     * @return int 打印s[i...j]所需的最少次数
     */
    int dfs(vector<vector<int>>& memo, const string& s, int i, int j) {
        // 基本情况：单个字符或空区间
        if (i > j) {
            return 0;
        }
        // 如果已经计算过，直接返回结果
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        
        /**
         * 【解题思路】
         * 1. 初始情况：假设我们第一次打印字符s[i]，覆盖整个区间[i,j]
         *    这需要1次打印，加上打印剩余部分的次数
         * 2. 优化点：如果在区间中存在字符等于s[i]，可以在打印s[i]时同时打印这些位置
         */
        // 初始化为最坏情况：先打印s[i]，然后打印剩余部分
        int minTurns = dfs(memo, s, i + 1, j) + 1;
        
        // 寻找区间中与s[i]相同的字符，尝试合并打印
        for (int k = i + 1; k <= j; ++k) {
            if (s[k] == s[i]) {
                // 可以在打印s[i]时同时打印s[k]
                // 此时问题分解为打印[i+1, k-1]和[k+1, j]
                int current = dfs(memo, s, i + 1, k - 1) + dfs(memo, s, k + 1, j);
                minTurns = min(minTurns, current);
            }
        }
        
        // 记忆化存储结果
        memo[i][j] = minTurns;
        return minTurns;
    }
    
public:
    // 方法二：动态规划（迭代实现）
    /**
     * @brief 使用动态规划求解最少打印次数
     * 
     * @param s 输入字符串
     * @return int 最少打印次数
     */
    int strangePrinter2(string s) {
        /**
         * 【解法思路】严格位置依赖的动态规划（迭代实现）
         * 与记忆化搜索思路相同，但使用迭代方式实现，按照区间长度从小到大填充dp表。
         */
        if (s.empty()) {
            return 0;
        }
        
        int n = s.size();
        // dp[i][j]表示打印区间[i,j]所需的最少次数
        vector<vector<int>> dp(n, vector<int>(n, 0));
        
        // 初始化：单个字符只需打印一次
        for (int i = 0; i < n; ++i) {
            dp[i][i] = 1;
        }
        
        /**
         * 【填表顺序】
         * 1. 按照区间长度从小到大填表
         * 2. 区间长度从2开始（长度为1的已经初始化）
         */
        for (int len = 2; len <= n; ++len) { // len表示区间长度
            for (int i = 0; i <= n - len; ++i) { // i是区间起点
                int j = i + len - 1; // j是区间终点
                
                // 初始化为最坏情况：比前一个多打印一次
                dp[i][j] = dp[i][j - 1] + 1;
                
                // 枚举分割点k
                for (int k = i; k < j; ++k) {
                    // 状态转移
                    int temp = dp[i][k] + dp[k + 1][j];
                    // 如果分割点k和j的字符相同，可以减少打印次数
                    if (s[k] == s[j]) {
                        temp--;
                    }
                    dp[i][j] = min(dp[i][j], temp);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    // 方法三：带优化的动态规划
    /**
     * @brief 使用优化版动态规划求解最少打印次数
     * 
     * @param s 输入字符串
     * @return int 最少打印次数
     */
    int strangePrinter3(string s) {
        /**
         * 【优化版动态规划】
         * 优化点：预处理字符串，压缩连续重复的字符
         */
        if (s.empty()) {
            return 0;
        }
        
        // 预处理：压缩连续重复的字符
        string compressed;
        for (char c : s) {
            if (compressed.empty() || compressed.back() != c) {
                compressed.push_back(c);
            }
        }
        
        int n = compressed.size();
        vector<vector<int>> dp(n, vector<int>(n, 0));
        
        // 初始化
        for (int i = 0; i < n; ++i) {
            dp[i][i] = 1;
        }
        
        // 填表
        for (int len = 2; len <= n; ++len) {
            for (int i = 0; i <= n - len; ++i) {
                int j = i + len - 1;
                
                // 初始化为最坏情况
                dp[i][j] = dp[i][j - 1] + 1;
                
                // 枚举分割点
                for (int k = i; k < j; ++k) {
                    int temp = dp[i][k] + dp[k + 1][j];
                    if (compressed[k] == compressed[j]) {
                        temp--;
                    }
                    dp[i][j] = min(dp[i][j], temp);
                    
                    // 进一步优化：如果找到可能的最小值，可以提前剪枝
                    if (dp[i][j] == dp[i][k] && compressed[k+1] == compressed[j]) {
                        break;
                    }
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * @brief 测试函数，验证不同解法在各种测试用例上的正确性
     * 
     * @param testName 测试名称
     * @param s 测试字符串
     * @param expected 预期结果
     * @return bool 测试是否通过
     */
    bool testStrangePrinter(const string& testName, const string& s, int expected) {
        int result1 = strangePrinter1(s);
        int result2 = strangePrinter2(s);
        int result3 = strangePrinter3(s);
        
        bool passed = (result1 == expected) && (result2 == expected) && (result3 == expected);
        
        cout << "测试用例: " << testName << endl;
        cout << "  输入: \"" << s << "\"" << endl;
        cout << "  预期输出: " << expected << endl;
        cout << "  解法1结果: " << result1 << endl;
        cout << "  解法2结果: " << result2 << endl;
        cout << "  解法3结果: " << result3 << endl;
        cout << "  测试结果: " << (passed ? "通过" : "失败") << endl << endl;
        
        return passed;
    }
};

// 主函数
int main() {
    Solution solution;
    
    // 运行所有测试用例
    cout << "========== 奇怪的打印机算法测试 ==========" << endl;
    
    // 测试用例1：常规情况
    solution.testStrangePrinter("常规情况", "aaabbb", 2);
    
    // 测试用例2：回文串
    solution.testStrangePrinter("回文串", "aba", 2);
    
    // 测试用例3：全相同字符
    solution.testStrangePrinter("全相同字符", "aaaaa", 1);
    
    // 测试用例4：全不同字符
    solution.testStrangePrinter("全不同字符", "abcdef", 6);
    
    // 测试用例5：空字符串
    solution.testStrangePrinter("空字符串", "", 0);
    
    // 测试用例6：单个字符
    solution.testStrangePrinter("单个字符", "a", 1);
    
    // 测试用例7：复杂混合
    solution.testStrangePrinter("复杂混合", "abacaba", 3);
    
    // 测试用例8：包含重复连续字符
    solution.testStrangePrinter("包含重复连续字符", "aabbccaabbcc", 4);
    
    /**
     * 【复杂度分析】
     * 
     * 时间复杂度：
     * - 三种解法的时间复杂度均为O(n³)，其中n是字符串长度
     * - 对于每个区间[i,j]，我们需要枚举分割点k
     * 
     * 空间复杂度：
     * - 记忆化搜索：O(n²)，用于存储memo数组
     * - 动态规划：O(n²)，用于存储dp数组
     * - 优化版动态规划：O(n')，其中n'是压缩后的字符串长度，最坏情况仍为O(n)
     * 
     * 【是否为最优解】
     * 目前这三种解法都是该问题的最优解，时间复杂度为O(n³)。
     * 对于LeetCode上的测试用例，都能在合理时间内通过。
     */
    
    /**
     * 【工程化考量】
     * 1. 异常处理：
     *    - 空字符串处理
     *    - 长字符串性能考虑
     *    
     * 2. 线程安全：
     *    - 代码中的vector等容器是局部变量，不共享状态
     *    - 可以安全地在多线程环境中使用
     *    
     * 3. 性能优化：
     *    - 预处理压缩连续重复字符
     *    - 剪枝策略减少不必要的计算
     *    - 对于特定问题，可以考虑位运算或其他优化
     *    
     * 4. 代码复用：
     *    - 该动态规划模式可应用于其他区间DP问题
     */
    
    cout << "========== 区间动态规划算法总结 ==========" << endl;
    cout << "1. 核心特征：将问题分解为区间子问题，按照区间长度递增顺序求解" << endl;
    cout << "2. 常见应用：字符串处理、数组分割合并、几何问题、博弈问题" << endl;
    cout << "3. 解题技巧：合理定义状态、处理边界条件、注意填表顺序" << endl;
    cout << "4. 优化方向：预处理、剪枝、空间优化" << endl;
    
    return 0;
}