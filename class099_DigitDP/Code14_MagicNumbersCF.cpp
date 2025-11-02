#include <iostream>
#include <vector>
#include <string>
#include <functional>
#include <memory>
#include <cstring>
#include <algorithm>
#include <chrono>
#include <tuple>

using namespace std;

/**
 * Codeforces 628D. Magic Numbers
 * 题目链接：https://codeforces.com/problemset/problem/628/D
 * 
 * 题目描述：
 * 定义一个d-magic number为满足以下条件的数字：
 * 1. 数字的十进制表示中，所有在偶数位置（从1开始计数）的数字都等于d
 * 2. 数字的十进制表示中，所有在奇数位置（从1开始计数）的数字都不等于d
 * 3. 数字不能有前导零
 * 给定区间[a, b]和数字d，求其中d-magic number的个数，结果对10^9+7取模。
 * 
 * 解题思路：
 * 1. 数位DP方法：使用数位DP框架，逐位确定数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 是否受上界限制
 *    - 当前数字对m的余数
 *    - 当前位置的奇偶性
 * 3. 关键点：根据位置奇偶性判断数字是否等于d
 * 
 * 时间复杂度分析：
 * - 状态数：2000 * 2 * 2 * 2000 ≈ 16,000,000
 * - 每个状态处理10种选择
 * - 总复杂度：O(160,000,000) 在可接受范围内
 * 
 * 空间复杂度分析：
 * - 记忆化数组：2000 * 2 * 2 * 2000 ≈ 64MB
 * 
 * 最优解分析：
 * 这是标准的最优解，利用数位DP处理位置相关的约束条件
 */

class MagicNumbers {
private:
    const int MOD = 1000000007;
    int d;          // 魔法数字d
    int m;          // 模数m
    vector<int> digits; // 存储数位
    
public:
    MagicNumbers(int d_val, int m_val) : d(d_val), m(m_val) {}
    
    /**
     * 检查一个字符串是否表示一个d-magic number
     * 用于验证边界情况
     */
    bool isMagicNumber(const string& s) {
        if (s.empty() || s[0] == '0') return false;
        
        int mod = 0;
        for (int i = 0; i < s.length(); i++) {
            int digit = s[i] - '0';
            int posType = (i + 1) % 2; // 1-indexed: 偶数位置对应i+1为偶数
            
            if (posType == 1) { // 偶数位置
                if (digit != d) return false;
            } else { // 奇数位置
                if (digit == d) return false;
            }
            
            mod = (mod * 10 + digit) % m;
        }
        
        return mod == 0;
    }
    
    /**
     * 计算区间[a, b]中d-magic number的个数
     * 时间复杂度: O(len(b) * 2 * 2 * m)
     * 空间复杂度: O(len(b) * 2 * 2 * m)
     */
    long long countMagicNumbers(const string& a, const string& b) {
        long long countB = countUpTo(b);
        long long countA = countUpTo(a);
        
        // 需要检查a本身是否是magic number
        if (isMagicNumber(a)) {
            countA = (countA - 1 + MOD) % MOD;
        }
        
        return (countB - countA + MOD) % MOD;
    }
    
private:
    /**
     * 计算[0, s]中d-magic number的个数
     */
    long long countUpTo(const string& s) {
        if (s.empty()) return 0;
        
        digits.clear();
        for (char c : s) {
            digits.push_back(c - '0');
        }
        
        int len = digits.size();
        
        // 记忆化数组：dp[pos][isLimit][mod][posType]
        // posType: 0表示奇数位置，1表示偶数位置
        vector<vector<vector<vector<long long>>>> dp(
            len, vector<vector<vector<long long>>>(
                2, vector<vector<long long>>(
                    m, vector<long long>(2, -1)
                )
            )
        );
        
        // 使用DFS进行数位DP
        // 从第0位开始，位置类型为1（偶数位置，因为从1开始计数）
        return dfs(0, true, 0, 1, dp);
    }
    
    /**
     * 数位DP递归函数
     * 
     * @param pos 当前处理的位置（0-indexed）
     * @param isLimit 是否受到上界限制
     * @param mod 当前数字对m的余数
     * @param posType 位置类型：1表示偶数位置，0表示奇数位置
     * @param dp 记忆化数组
     * @return 满足条件的数字个数
     */
    long long dfs(int pos, bool isLimit, int mod, int posType, 
                 vector<vector<vector<vector<long long>>>>& dp) {
        // 递归终止条件：处理完所有数位
        if (pos == digits.size()) {
            // 必须是一个有效的数字（不能是前导零情况）
            // 余数为0且是有效的magic number
            return (mod == 0) ? 1 : 0;
        }
        
        // 记忆化搜索
        if (!isLimit && dp[pos][0][mod][posType] != -1) {
            return dp[pos][0][mod][posType];
        }
        
        long long ans = 0;
        
        // 确定当前位可选数字范围
        int up = isLimit ? digits[pos] : 9;
        int start = (pos == 0) ? 1 : 0; // 第一位不能为0
        
        // 枚举当前位可选数字
        for (int d_val = start; d_val <= up; d_val++) {
            // 根据位置类型检查约束条件
            if (posType == 1) { // 偶数位置：必须等于d
                if (d_val != d) continue;
            } else { // 奇数位置：必须不等于d
                if (d_val == d) continue;
            }
            
            // 计算新的余数
            int newMod = (mod * 10 + d_val) % m;
            // 下一个位置类型：奇偶交替
            int newPosType = 1 - posType;
            
            ans = (ans + dfs(pos + 1, isLimit && d_val == up, newMod, newPosType, dp)) % MOD;
        }
        
        // 记忆化存储
        if (!isLimit) {
            dp[pos][0][mod][posType] = ans;
        }
        
        return ans;
    }
};

/**
 * 单元测试函数
 */
void testMagicNumbers() {
    cout << "=== 测试Magic Numbers ===" << endl;
    
    // 测试用例1: 简单情况
    {
        MagicNumbers mn(7, 7);
        string a = "1", b = "100";
        long long result = mn.countMagicNumbers(a, b);
        cout << "d=7, m=7, 区间[" << a << ", " << b << "]" << endl;
        cout << "结果: " << result << endl;
        cout << "预期: 包含7, 77等数字" << endl;
        cout << endl;
    }
    
    // 测试用例2: 边界情况
    {
        MagicNumbers mn(1, 1);
        string a = "1", b = "9";
        long long result = mn.countMagicNumbers(a, b);
        cout << "d=1, m=1, 区间[" << a << ", " << b << "]" << endl;
        cout << "结果: " << result << endl;
        cout << "预期: 所有奇数位置的数字不能是1" << endl;
        cout << endl;
    }
    
    // 测试用例3: 较大范围
    {
        MagicNumbers mn(4, 13);
        string a = "100", b = "1000";
        long long result = mn.countMagicNumbers(a, b);
        cout << "d=4, m=13, 区间[" << a << ", " << b << "]" << endl;
        cout << "结果: " << result << endl;
        cout << endl;
    }
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    vector<tuple<int, int, string, string>> testCases = {
        {7, 7, "1", "1000000"},
        {3, 11, "1", "1000000000"},
        {9, 19, "1", "1000000000000"}
    };
    
    for (auto& testCase : testCases) {
        int d = get<0>(testCase);
        int m = get<1>(testCase);
        string a = get<2>(testCase);
        string b = get<3>(testCase);
        
        MagicNumbers mn(d, m);
        
        auto start = chrono::high_resolution_clock::now();
        long long result = mn.countMagicNumbers(a, b);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        
        cout << "d=" << d << ", m=" << m << ", 区间[" << a << ", " << b << "]" << endl;
        cout << "结果: " << result << endl;
        cout << "耗时: " << duration.count() << "毫秒" << endl;
        cout << endl;
    }
}

/**
 * 调试函数：验证特定数字是否为magic number
 */
void debugMagicNumbers() {
    cout << "=== 调试Magic Numbers ===" << endl;
    
    MagicNumbers mn(7, 7);
    
    vector<string> testNumbers = {"7", "17", "27", "77", "177", "707", "717", "727"};
    
    for (const string& num : testNumbers) {
        bool isMagic = mn.isMagicNumber(num);
        cout << "数字 " << num << ": " << (isMagic ? "是" : "不是") << "magic number" << endl;
        
        if (isMagic) {
            int mod = 0;
            for (int i = 0; i < num.length(); i++) {
                int digit = num[i] - '0';
                int posType = (i + 1) % 2;
                cout << "  位置" << (i+1) << "(" << (posType == 1 ? "偶数" : "奇数") 
                     << "): 数字=" << digit;
                if (posType == 1) {
                    cout << " 必须等于7: " << (digit == 7 ? "满足" : "不满足");
                } else {
                    cout << " 必须不等于7: " << (digit != 7 ? "满足" : "不满足");
                }
                cout << endl;
                mod = (mod * 10 + digit) % 7;
            }
            cout << "  余数: " << mod << "%7=" << mod % 7 << endl;
        }
        cout << endl;
    }
}

/**
 * 工程化考量：
 * 1. 模运算：结果对10^9+7取模，避免溢出
 * 2. 字符串处理：支持大数字输入
 * 3. 边界处理：正确处理前导零和空字符串
 * 4. 状态设计：合理设计状态参数，减少状态数
 * 5. 记忆化优化：避免重复计算相同状态
 * 
 * 算法特色：
 * 1. 位置相关约束：根据位置奇偶性应用不同约束
 * 2. 模运算约束：数字必须能被m整除
 * 3. 前导零处理：第一位不能为0
 * 4. 记忆化搜索：提高算法效率
 */

int main() {
    // 运行功能测试
    testMagicNumbers();
    
    // 运行性能测试
    performanceTest();
    
    // 调试模式
    debugMagicNumbers();
    
    return 0;
}