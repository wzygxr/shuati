#include <iostream>
#include <vector>
#include <string>
#include <functional>
#include <algorithm>
#include <cstring>

using namespace std;

/**
 * Codeforces 55D. Beautiful Numbers (增强版)
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
 * - 状态数：log(r) * 2 * 2 * 2520 * 50 ≈ 10^7
 * - 每个状态处理10种选择
 * - 总复杂度：O(10^8) 在可接受范围内
 * 
 * 空间复杂度分析：
 * - 记忆化数组：log(r) * 2 * 2 * 2520 * 50 ≈ 40MB
 * 
 * 最优解分析：
 * 这是标准的最优解，利用了LCM的数学性质和数位DP的记忆化
 */

class BeautifulNumbersCF {
private:
    static const int MOD = 2520; // 1-9的LCM
    vector<int> digits;          // 存储数位
    vector<int> lcm_map;         // LCM映射表
    
    // 计算两个数的最大公约数
    int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    // 计算两个数的最小公倍数
    int lcm(int a, int b) {
        return a / gcd(a, b) * b;
    }
    
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
    
public:
    /**
     * 计算区间[l, r]中美丽数字的个数
     * 时间复杂度: O(log(r) * 2 * 2 * 2520 * 50)
     * 空间复杂度: O(log(r) * 2 * 2 * 2520 * 50)
     */
    long long countBeautifulNumbers(long long l, long long r) {
        precomputeLCM();
        return countUpTo(r) - countUpTo(l - 1);
    }
    
private:
    /**
     * 计算[0, n]中美丽数字的个数
     */
    long long countUpTo(long long n) {
        if (n < 0) return 0;
        if (n == 0) return 1; // 0是美丽数字（特殊情况）
        
        // 将数字转换为字符串
        string s = to_string(n);
        digits.resize(s.length());
        for (int i = 0; i < s.length(); i++) {
            digits[i] = s[i] - '0';
        }
        
        int len = digits.size();
        
        // 记忆化数组：dp[pos][isLimit][isNum][mod][mask]
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
                if (!isNum) return 1; // 前导零也算美丽数字（特殊情况）
                
                // 检查是否美丽：数字能被所有非零数字整除
                int actualLCM = lcm_map[mask];
                return (mod % actualLCM == 0) ? 1 : 0;
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
 */
void testBeautifulNumbers() {
    cout << "=== 测试Beautiful Numbers ===" << endl;
    
    BeautifulNumbersCF bn;
    
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
    cout << "预期: 14 (1,2,3,4,5,6,7,8,9,10,11,12,15,18,20)" << endl;
    cout << endl;
    
    // 测试用例3: 较大范围
    cout << "测试区间[1, 100]:" << endl;
    long long result3 = bn.countBeautifulNumbers(1, 100);
    cout << "结果: " << result3 << endl;
    cout << endl;
}

int main() {
    // 运行功能测试
    testBeautifulNumbers();
    
    return 0;
}