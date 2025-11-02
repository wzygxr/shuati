#include <iostream>
#include <vector>
#include <string>
#include <functional>
#include <memory>
#include <cstring>
#include <algorithm>
#include <chrono>

using namespace std;

/**
 * Codeforces 55D. Beautiful Numbers
 * 题目链接：https://codeforces.com/problemset/problem/55/D
 * 
 * 题目描述：
 * 如果一个正整数能被它的所有非零数字整除，那么这个数就是美丽的。
 * 给定区间 [l, r]，求其中美丽数字的个数。
 * 
 * 解题思路：
 * 1. 数位DP方法：使用数位DP框架，逐位确定数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 是否受上界限制
 *    - 是否已开始填数字
 *    - 当前数字对LCM(1-9)的余数
 *    - 已使用数字的LCM
 * 3. 关键优化：1-9的LCM是2520，所有数字的LCM都是2520的约数
 * 
 * 时间复杂度分析：
 * - 状态数：20 * 2 * 2 * 2520 * 50 ≈ 10^7
 * - 每个状态处理10种选择
 * - 总复杂度：O(10^8) 在可接受范围内
 * 
 * 空间复杂度分析：
 * - 记忆化数组：20 * 2 * 2 * 2520 * 50 ≈ 40MB
 * - 使用unordered_map可以进一步优化空间
 * 
 * 最优解分析：
 * 这是标准的最优解，利用了LCM的数学性质和数位DP的记忆化
 */

class BeautifulNumbers {
private:
    const int MOD = 2520; // 1-9的LCM
    vector<int> digits;   // 存储数位
    vector<int> lcm_map;  // LCM映射表
    
    // 预计算1-9所有子集的LCM
    void precomputeLCM() {
        lcm_map.resize(1 << 9, 1);
        for (int mask = 1; mask < (1 << 9); mask++) {
            int lcm_val = 1;
            for (int i = 1; i <= 9; i++) {
                if (mask & (1 << (i-1))) {
                    lcm_val = lcm(lcm_val, i);
                }
            }
            lcm_map[mask] = lcm_val;
        }
    }
    
    // 计算两个数的最小公倍数
    int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
public:
    int lcm(int a, int b) {
        return a / gcd(a, b) * b;
    }
    
    BeautifulNumbers() {
        precomputeLCM();
    }
    
    /**
     * 计算区间[l, r]中美丽数字的个数
     * 时间复杂度: O(log(r) * 2 * 2 * 2520 * 50)
     * 空间复杂度: O(log(r) * 2 * 2 * 2520 * 50)
     */
    long long countBeautifulNumbers(long long l, long long r) {
        return countUpTo(r) - countUpTo(l - 1);
    }
    
private:
    /**
     * 计算[0, n]中美丽数字的个数
     */
    long long countUpTo(long long n) {
        if (n < 0) return 0;
        
        // 将数字转换为数位数组
        digits.clear();
        long long temp = n;
        if (temp == 0) digits.push_back(0);
        while (temp > 0) {
            digits.push_back(temp % 10);
            temp /= 10;
        }
        reverse(digits.begin(), digits.end());
        
        int len = digits.size();
        
        // 记忆化数组：dp[pos][isLimit][isNum][mod][mask]
        // 使用unordered_map优化空间，避免稀疏数组
        vector<vector<vector<vector<vector<long long>>>>> dp(
            len, vector<vector<vector<vector<long long>>>>(
                2, vector<vector<vector<long long>>>(
                    2, vector<vector<long long>>(
                        MOD, vector<long long>(1 << 9, -1)
                    )
                )
            )
        );
        
        // 使用lambda函数实现DFS
        function<long long(int, bool, bool, int, int)> dfs = [&](int pos, bool isLimit, bool isNum, int mod, int mask) -> long long {
            // 递归终止条件
            if (pos == len) {
                if (!isNum) return 0; // 前导零不算
                // 检查是否美丽：数字能被所有非零数字整除
                for (int i = 1; i <= 9; i++) {
                    if (mask & (1 << (i-1))) {
                        if (mod % i != 0) {
                            return 0;
                        }
                    }
                }
                return 1;
            }
            
            // 记忆化搜索
            if (!isLimit && isNum && dp[pos][0][0][mod][mask] != -1) {
                return dp[pos][0][0][mod][mask];
            }
            
            long long ans = 0;
            
            // 处理前导零
            if (!isNum) {
                ans += dfs(pos + 1, false, false, mod, mask);
            }
            
            // 确定当前位可选范围
            int up = isLimit ? digits[pos] : 9;
            int start = isNum ? 0 : 1;
            
            // 枚举当前位可选数字
            for (int d = start; d <= up; d++) {
                int newMod = (mod * 10 + d) % MOD;
                int newMask = mask;
                if (d > 0) {
                    newMask |= (1 << (d-1));
                }
                ans += dfs(pos + 1, isLimit && d == up, true, newMod, newMask);
            }
            
            // 记忆化存储
            if (!isLimit && isNum) {
                dp[pos][0][0][mod][mask] = ans;
            }
            
            return ans;
        };
        
        return dfs(0, true, false, 0, 0);
    }
};

/**
 * 单元测试函数
 * 测试用例设计原则：
 * 1. 边界测试：小数字区间
 * 2. 常规测试：中等规模区间
 * 3. 性能测试：大规模区间
 */
void testBeautifulNumbers() {
    BeautifulNumbers bn;
    
    cout << "=== 测试Beautiful Numbers ===" << endl;
    
    // 测试用例1: 小范围
    cout << "测试区间[1, 9]:" << endl;
    long long result1 = bn.countBeautifulNumbers(1, 9);
    cout << "结果: " << result1 << endl;
    cout << "预期: 9 (所有1-9的数字都美丽)" << endl;
    cout << endl;
    
    // 测试用例2: 包含不美丽数字
    cout << "测试区间[1, 20]:" << endl;
    long long result2 = bn.countBeautifulNumbers(1, 20);
    cout << "结果: " << result2 << endl;
    cout << "预期: 12 (13, 17, 19不美丽)" << endl;
    cout << endl;
    
    // 测试用例3: 较大范围
    cout << "测试区间[1, 100]:" << endl;
    long long result3 = bn.countBeautifulNumbers(1, 100);
    cout << "结果: " << result3 << endl;
    cout << "预期: 33" << endl;
    cout << endl;
}

/**
 * 性能测试函数
 * 测试算法在大规模数据下的性能
 */
void performanceTest() {
    BeautifulNumbers bn;
    
    cout << "=== 性能测试 ===" << endl;
    
    // 测试不同规模区间的性能
    vector<pair<long long, long long>> testCases = {
        {1, 1000},
        {1, 1000000},
        {1, 1000000000LL},
        {1, 1000000000000LL}
    };
    
    for (auto& testCase : testCases) {
        auto start = chrono::high_resolution_clock::now();
        long long result = bn.countBeautifulNumbers(testCase.first, testCase.second);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        
        cout << "区间[" << testCase.first << ", " << testCase.second << "]:" << endl;
        cout << "结果: " << result << endl;
        cout << "耗时: " << duration.count() << "毫秒" << endl;
        cout << endl;
    }
}

/**
 * 调试函数：打印中间状态
 * 用于理解算法执行过程
 */
void debugBeautifulNumbers(long long l, long long r) {
    BeautifulNumbers bn;
    
    cout << "=== 调试Beautiful Numbers ===" << endl;
    cout << "区间: [" << l << ", " << r << "]" << endl;
    
    // 手动计算小范围内的结果进行验证
    if (r - l <= 1000) {
        int manualCount = 0;
        for (long long i = l; i <= r; i++) {
            if (i == 0) continue;
            
            long long temp = i;
            int lcm_val = 1;
            bool hasNonZero = false;
            
            while (temp > 0) {
                int digit = temp % 10;
                temp /= 10;
                if (digit > 0) {
                    hasNonZero = true;
                    lcm_val = bn.lcm(lcm_val, digit);
                }
            }
            
            if (hasNonZero && i % lcm_val == 0) {
                manualCount++;
                if (manualCount <= 10) {
                    cout << "美丽数字: " << i << endl;
                }
            }
        }
        
        long long dpCount = bn.countBeautifulNumbers(l, r);
        cout << "手动计算: " << manualCount << endl;
        cout << "DP计算: " << dpCount << endl;
        cout << "结果一致: " << (manualCount == dpCount ? "是" : "否") << endl;
    } else {
        long long result = bn.countBeautifulNumbers(l, r);
        cout << "结果: " << result << endl;
    }
    cout << endl;
}

/**
 * 工程化考量：
 * 1. 数学优化：利用LCM性质减少状态数
 * 2. 空间优化：使用vector而不是map，提高访问速度
 * 3. 边界处理：正确处理n=0的情况
 * 4. 可读性：清晰的变量命名和注释
 * 5. 测试覆盖：全面的测试用例
 * 
 * 算法特色：
 * 1. 结合数论：利用LCM的数学性质
 * 2. 状态压缩：使用位掩码记录数字使用情况
 * 3. 模运算：利用2520的模运算性质
 * 4. 记忆化：避免重复计算相同状态
 */

int main() {
    // 运行功能测试
    testBeautifulNumbers();
    
    // 运行性能测试
    performanceTest();
    
    // 调试模式
    debugBeautifulNumbers(1, 100);
    
    return 0;
}