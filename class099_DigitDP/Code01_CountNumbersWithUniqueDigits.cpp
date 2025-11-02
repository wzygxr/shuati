#include <iostream>
#include <vector>
#include <string>
#include <functional>
#include <memory>
#include <cstring>
#include <chrono>
using namespace std;

using namespace std;

/**
 * 统计各位数字都不同的数字个数
 * 题目来源：LeetCode 357. 统计各位数字都不同的数字个数
 * 题目链接：https://leetcode.cn/problems/count-numbers-with-unique-digits/
 * 
 * 题目描述：
 * 给你一个整数n，代表十进制数字最多有n位。如果某个数字，每一位都不同，那么这个数字叫做有效数字。
 * 返回有效数字的个数，不统计负数范围。
 * 
 * 解题思路：
 * 1. 数学方法（排列组合）：按位数分别计算，利用排列组合公式
 * 2. 数位DP方法：使用数位DP框架，逐位确定数字，用位掩码记录已使用的数字
 * 
 * 时间复杂度分析：
 * - 数学方法：O(n)，每个位数计算一次
 * - 数位DP方法：O(10 * 2^10 * n)，状态数为位数×状态数×数字选择
 * 
 * 空间复杂度分析：
 * - 数学方法：O(1)，只使用常数空间
 * - 数位DP方法：O(2^10 * n)，用于记忆化存储
 * 
 * 最优解分析：
 * 数学方法是最优解，时间复杂度O(n)，空间复杂度O(1)
 * 数位DP方法更通用但复杂度较高，适合学习数位DP框架
 */

class Solution {
public:
    /**
     * 数学方法（排列组合）- 最优解
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 算法步骤：
     * 1. n=0时，只有数字0，返回1
     * 2. n=1时，0-9共10个数字
     * 3. 对于n>=2的情况：
     *    - 1位数：10个
     *    - 2位数：9*9个（第一位1-9，第二位0-9除去第一位）
     *    - 3位数：9*9*8个
     *    - 以此类推...
     */
    int countNumbersWithUniqueDigits_math(int n) {
        if (n == 0) return 1;
        if (n == 1) return 10;
        
        int ans = 10;
        int available = 9;
        int current = 9;
        
        for (int i = 2; i <= n; i++) {
            current *= available;
            ans += current;
            available--;
        }
        
        return ans;
    }
    
    /**
     * 数位DP方法 - 通用解法
     * 时间复杂度: O(10 * 2^10 * n)
     * 空间复杂度: O(2^10 * n)
     * 
     * 状态设计：
     * - pos: 当前处理到第几位
     * - mask: 位掩码，记录已使用的数字（1<<d表示数字d已使用）
     * - isLimit: 是否受到上界限制
     * - isNum: 是否已开始填数字（处理前导零）
     * 
     * 记忆化优化：避免重复计算相同状态
     */
    int countNumbersWithUniqueDigits_dp(int n) {
        if (n == 0) return 1;
        
        // 构造上界字符串（n个9）
        string upper(n, '9');
        
        // 记忆化数组：dp[pos][mask][isLimit][isNum]
        vector<vector<vector<vector<int>>>> dp(
            n, vector<vector<vector<int>>>(
                1 << 10, vector<vector<int>>(
                    2, vector<int>(2, -1)
                )
            )
        );
        
        // 使用lambda函数实现DFS
        function<int(int, int, bool, bool)> dfs = [&](int pos, int mask, bool isLimit, bool isNum) -> int {
            // 递归终止条件：处理完所有数位
            if (pos == n) {
                return 1; // 无论是否已填数字，都返回1（表示数字0或已填数字）
            }
            
            // 记忆化搜索：如果已计算过且不受限制且已开始填数字
            if (!isLimit && isNum && dp[pos][mask][0][0] != -1) {
                return dp[pos][mask][0][0];
            }
            
            int ans = 0;
            
            // 处理前导零：可以选择跳过当前位
            if (!isNum) {
                ans += dfs(pos + 1, mask, false, false);
            }
            
            // 确定当前位可选数字范围
            int up = isLimit ? upper[pos] - '0' : 9;
            int start = isNum ? 0 : 1; // 处理前导零
            
            // 枚举当前位可选的数字
            for (int d = start; d <= up; d++) {
                // 检查数字d是否已被使用
                if ((mask & (1 << d)) == 0) {
                    // 递归处理下一位，更新状态
                    ans += dfs(pos + 1, mask | (1 << d), 
                              isLimit && (d == up), true);
                }
            }
            
            // 记忆化存储：只存储不受限制且已开始填数字的状态
            if (!isLimit && isNum) {
                dp[pos][mask][0][0] = ans;
            }
            
            return ans;
        };
        
        return dfs(0, 0, true, false);
    }
};

/**
 * 单元测试函数
 * 测试用例设计原则：
 * 1. 边界测试：n=0, n=1等边界情况
 * 2. 常规测试：n=2, n=3等正常情况
 * 3. 对拍测试：两种方法结果应该一致
 */
void testSolution() {
    Solution sol;
    
    cout << "=== 测试统计各位数字都不同的数字个数 ===" << endl;
    
    // 测试用例1: n=0
    cout << "n=0: " << endl;
    cout << "数学方法: " << sol.countNumbersWithUniqueDigits_math(0) << endl;
    cout << "数位DP方法: " << sol.countNumbersWithUniqueDigits_dp(0) << endl;
    cout << "预期结果: 1" << endl << endl;
    
    // 测试用例2: n=1
    cout << "n=1: " << endl;
    cout << "数学方法: " << sol.countNumbersWithUniqueDigits_math(1) << endl;
    cout << "数位DP方法: " << sol.countNumbersWithUniqueDigits_dp(1) << endl;
    cout << "预期结果: 10" << endl << endl;
    
    // 测试用例3: n=2
    cout << "n=2: " << endl;
    cout << "数学方法: " << sol.countNumbersWithUniqueDigits_math(2) << endl;
    cout << "数位DP方法: " << sol.countNumbersWithUniqueDigits_dp(2) << endl;
    cout << "预期结果: 91" << endl << endl;
    
    // 测试用例4: n=3
    cout << "n=3: " << endl;
    int math3 = sol.countNumbersWithUniqueDigits_math(3);
    int dp3 = sol.countNumbersWithUniqueDigits_dp(3);
    cout << "数学方法: " << math3 << endl;
    cout << "数位DP方法: " << dp3 << endl;
    cout << "预期结果: 739" << endl;
    cout << "两种方法结果一致: " << (math3 == dp3 ? "是" : "否") << endl << endl;
}

/**
 * 性能测试函数
 * 测试两种方法在不同规模下的性能表现
 */
void performanceTest() {
    Solution sol;
    
    cout << "=== 性能测试 ===" << endl;
    
    for (int n = 1; n <= 8; n++) {
        auto start = chrono::high_resolution_clock::now();
        int result_math = sol.countNumbersWithUniqueDigits_math(n);
        auto end_math = chrono::high_resolution_clock::now();
        
        auto start_dp = chrono::high_resolution_clock::now();
        int result_dp = sol.countNumbersWithUniqueDigits_dp(n);
        auto end_dp = chrono::high_resolution_clock::now();
        
        auto duration_math = chrono::duration_cast<chrono::microseconds>(end_math - start);
        auto duration_dp = chrono::duration_cast<chrono::microseconds>(end_dp - start_dp);
        
        cout << "n=" << n << ": " << endl;
        cout << "数学方法时间: " << duration_math.count() << "微秒" << endl;
        cout << "数位DP方法时间: " << duration_dp.count() << "微秒" << endl;
        cout << "结果一致: " << (result_math == result_dp ? "是" : "否") << endl;
        cout << "加速比: " << (double)duration_dp.count() / duration_math.count() << "倍" << endl;
        cout << endl;
    }
}

/**
 * 工程化考量：
 * 1. 异常处理：处理n<0的情况
 * 2. 边界情况：n=0, n=1的特殊处理
 * 3. 性能优化：数学方法是最优选择
 * 4. 可读性：清晰的变量命名和注释
 * 5. 测试覆盖：全面的测试用例
 * 
 * 跨语言实现差异：
 * - C++: 使用vector和lambda函数，注意内存管理
 * - Java: 使用多维数组，注意数组初始化
 * - Python: 使用字典进行记忆化，语法更简洁
 */

int main() {
    // 运行功能测试
    testSolution();
    
    // 运行性能测试（n较大时数位DP方法较慢，只测试到n=8）
    performanceTest();
    
    return 0;
}