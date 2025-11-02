/**
 * Codeforces 1073E. Segment Sum - C++实现
 * 题目链接：https://codeforces.com/problemset/problem/1073/E
 * 
 * 题目描述：
 * 给定区间[L, R]和整数K，求[L,R]范围内最多包含K个不同数字的数的和。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。需要同时计算满足条件的数字个数和它们的和。
 * 
 * 算法分析：
 * 时间复杂度：O(L × 2^10 × 2 × 2) = O(L) 其中L是数字位数
 * 空间复杂度：O(L × 2^10) 用于存储DP状态
 * 
 * C++实现特点：
 * 1. 使用pair同时存储个数和和值
 * 2. 使用vector进行动态内存管理
 * 3. 使用位运算高效处理数字状态
 */

#include <iostream>
#include <vector>
#include <string>
#include <utility>
#include <bitset>
#include <chrono>

using namespace std;

typedef long long ll;
typedef pair<ll, ll> pll;

const ll MOD = 998244353;

class Solution {
private:
    // 数位DP记忆化数组：dp[pos][mask][limit][lead] = {count, sum}
    vector<vector<vector<vector<pll>>>> dp;
    vector<int> digits;
    int len;
    
    /**
     * 检查数字使用状态是否满足条件
     */
    bool check(int mask, int K) {
        return __builtin_popcount(mask) <= K;
    }
    
    /**
     * 快速幂计算：a^b % MOD
     */
    ll powmod(ll a, ll b) {
        ll res = 1;
        while (b > 0) {
            if (b & 1) res = res * a % MOD;
            a = a * a % MOD;
            b >>= 1;
        }
        return res;
    }
    
    /**
     * 数位DP核心递归函数
     */
    pll dfs(int pos, int mask, bool limit, bool lead, int K) {
        // 递归终止条件
        if (pos == len) {
            if (!lead && check(mask, K)) {
                return {1, 0};
            }
            return {0, 0};
        }
        
        // 记忆化搜索优化
        if (!limit && !lead) {
            int limitIdx = limit ? 1 : 0;
            int leadIdx = lead ? 1 : 0;
            if (dp[pos][mask][limitIdx][leadIdx].first != -1) {
                return dp[pos][mask][limitIdx][leadIdx];
            }
        }
        
        ll count = 0, sum = 0;
        int maxDigit = limit ? digits[pos] : 9;
        
        for (int digit = 0; digit <= maxDigit; digit++) {
            int newMask = mask;
            if (!lead || digit != 0) {
                newMask |= (1 << digit);
            }
            
            if (__builtin_popcount(newMask) > K) {
                continue;
            }
            
            bool newLimit = limit && (digit == maxDigit);
            bool newLead = lead && (digit == 0);
            
            pll next = dfs(pos + 1, newMask, newLimit, newLead, K);
            
            count = (count + next.first) % MOD;
            
            // 计算当前位的贡献
            ll power = powmod(10, len - pos - 1);
            ll digitContrib = digit * power % MOD;
            digitContrib = digitContrib * next.first % MOD;
            
            sum = (sum + digitContrib + next.second) % MOD;
        }
        
        // 记忆化存储
        if (!limit && !lead) {
            int limitIdx = limit ? 1 : 0;
            int leadIdx = lead ? 1 : 0;
            dp[pos][mask][limitIdx][leadIdx] = {count, sum};
        }
        
        return {count, sum};
    }

public:
    /**
     * 计算[0,R]范围内最多包含K个不同数字的数的和
     */
    ll solve(ll R, int K) {
        if (R < 0) return 0;
        
        string numStr = to_string(R);
        len = numStr.length();
        digits.resize(len);
        
        for (int i = 0; i < len; i++) {
            digits[i] = numStr[i] - '0';
        }
        
        // 初始化DP数组：len × 1024 × 2 × 2
        dp.resize(len, vector<vector<vector<pll>>>(1024, 
            vector<vector<pll>>(2, vector<pll>(2, {-1, -1}))));
        
        pll result = dfs(0, 0, true, true, K);
        return result.second;
    }
    
    /**
     * 主函数：计算[L,R]范围内最多包含K个不同数字的数的和
     */
    ll segmentSum(ll L, ll R, int K) {
        ll result = (solve(R, K) - solve(L - 1, K) + MOD) % MOD;
        return result;
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1：小范围测试
    ll L1 = 10, R1 = 50;
    int K1 = 2;
    ll result1 = solution.segmentSum(L1, R1, K1);
    cout << "测试用例1 - L=" << L1 << ", R=" << R1 << ", K=" << K1 << endl;
    cout << "计算结果: " << result1 << endl;
    
    // 手动验证小范围结果
    ll manualSum = 0;
    for (ll i = L1; i <= R1; i++) {
        string numStr = to_string(i);
        vector<bool> used(10, false);
        int distinct = 0;
        for (char c : numStr) {
            int digit = c - '0';
            if (!used[digit]) {
                used[digit] = true;
                distinct++;
            }
        }
        if (distinct <= K1) {
            manualSum = (manualSum + i) % MOD;
        }
    }
    cout << "手动验证结果: " << manualSum << endl;
    cout << "验证: " << (result1 == manualSum ? "通过" : "失败") << endl;
    cout << endl;
    
    // 测试用例2：边界测试（K=10，可以使用所有数字）
    ll L2 = 1, R2 = 100;
    int K2 = 10;
    ll result2 = solution.segmentSum(L2, R2, K2);
    cout << "测试用例2 - L=" << L2 << ", R=" << R2 << ", K=" << K2 << endl;
    cout << "计算结果: " << result2 << endl;
    
    // 理论值：1到100的和 = 5050
    ll expected = 5050 % MOD;
    cout << "理论值: " << expected << endl;
    cout << "验证: " << (result2 == expected ? "通过" : "失败") << endl;
    cout << endl;
    
    // 性能测试
    ll L3 = 1, R3 = 1000000000;
    int K3 = 5;
    
    auto start = chrono::high_resolution_clock::now();
    ll result3 = solution.segmentSum(L3, R3, K3);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "性能测试 - L=" << L3 << ", R=" << R3 << ", K=" << K3 << endl;
    cout << "计算结果: " << result3 << endl;
    cout << "计算时间: " << duration.count() << "ms" << endl;
    
    return 0;
}