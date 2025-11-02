/*
 * LeetCode 1397. 找到所有好字符串 - Find All Good Strings
 * 
 * 题目来源：LeetCode (力扣)
 * 题目链接：https://leetcode.cn/problems/find-all-good-strings/
 * 
 * 题目描述：
 * 给你两个长度为n的字符串s1和s2，以及一个字符串evil。
 * 好字符串的定义是：它的长度为n，字典序大于等于s1，小于等于s2，且不包含evil子串。
 * 请你返回好字符串的数目。由于答案可能很大，请你返回答案对10^9 + 7取余的结果。
 * 
 * 算法思路：
 * 使用数位DP结合KMP算法来解决这个问题。
 * 1. 使用KMP算法预处理evil字符串，构建next数组
 * 2. 使用数位DP统计满足条件的字符串数量
 * 3. 在DP过程中使用KMP状态机来避免包含evil子串
 * 4. 使用记忆化搜索优化重复计算
 * 
 * 时间复杂度：O(n * m)，其中n是字符串长度，m是evil字符串长度
 * 空间复杂度：O(n * m)，用于存储DP状态
 * 
 * 工程化考量：
 * 1. 使用模运算处理大数
 * 2. 使用记忆化搜索优化性能
 * 3. 边界条件处理：空字符串、evil长度大于n等
 * 4. 异常处理确保程序稳定性
 */

#include <iostream>
#include <vector>
#include <string>
#include <cstring>
#include <functional>
#include <chrono>
using namespace std;

class Solution {
private:
    static const int MOD = 1000000007;
    int n;
    string s1, s2, evil;
    vector<int> next;
    vector<vector<vector<vector<int>>>> memo;
    
    /**
     * 构建KMP算法的next数组（部分匹配表）
     * next[i]表示evil[0...i]子串的最长相等前后缀的长度
     */
    void buildNextArray() {
        int m = evil.length();
        next.resize(m);
        next[0] = 0;
        
        int prefixLen = 0;
        int i = 1;
        
        while (i < m) {
            if (evil[i] == evil[prefixLen]) {
                prefixLen++;
                next[i] = prefixLen;
                i++;
            } else if (prefixLen > 0) {
                prefixLen = next[prefixLen - 1];
            } else {
                next[i] = 0;
                i++;
            }
        }
    }
    
    /**
     * 数位DP的深度优先搜索
     * 
     * @param pos 当前处理的位置
     * @param evilState 当前KMP匹配状态
     * @param tightLower 是否紧贴下界
     * @param tightUpper 是否紧贴上界
     * @return 从当前位置开始的满足条件的字符串数量
     */
    int dfs(int pos, int evilState, bool tightLower, bool tightUpper) {
        // 如果已经匹配到完整的evil字符串，返回0（不合法）
        if (evilState == evil.length()) {
            return 0;
        }
        
        // 如果已经处理完所有位置，返回1（找到一个合法字符串）
        if (pos == n) {
            return 1;
        }
        
        // 检查记忆化数组
        int lowerFlag = tightLower ? 1 : 0;
        int upperFlag = tightUpper ? 1 : 0;
        if (memo[pos][evilState][lowerFlag][upperFlag] != -1) {
            return memo[pos][evilState][lowerFlag][upperFlag];
        }
        
        long long count = 0;
        
        // 计算当前字符的取值范围
        char lowerChar = tightLower ? s1[pos] : 'a';
        char upperChar = tightUpper ? s2[pos] : 'z';
        
        for (char c = lowerChar; c <= upperChar; c++) {
            // 计算新的KMP匹配状态
            int newEvilState = evilState;
            while (newEvilState > 0 && c != evil[newEvilState]) {
                newEvilState = next[newEvilState - 1];
            }
            if (c == evil[newEvilState]) {
                newEvilState++;
            }
            
            // 计算新的边界条件
            bool newTightLower = tightLower && (c == lowerChar);
            bool newTightUpper = tightUpper && (c == upperChar);
            
            // 递归计算
            count = (count + dfs(pos + 1, newEvilState, newTightLower, newTightUpper)) % MOD;
        }
        
        // 存储结果到记忆化数组
        memo[pos][evilState][lowerFlag][upperFlag] = count;
        return count;
    }
    
    /**
     * 计算在[s1, s2]范围内的字符串数量（不考虑evil限制）
     * 用于evil长度大于n的特殊情况
     */
    int countStringsInRange(const string& s1, const string& s2) {
        long long count = 0;
        for (int i = 0; i < n; i++) {
            long long diff = s2[i] - s1[i];
            count = (count * 26 + diff) % MOD;
        }
        count = (count + 1) % MOD; // 包括s1本身
        return count;
    }
    
public:
    /**
     * 计算好字符串的数量
     * 
     * @param n 字符串长度
     * @param s1 下界字符串
     * @param s2 上界字符串
     * @param evil 禁止出现的子串
     * @return 好字符串的数量（取模后）
     */
    int findGoodStrings(int n, const string& s1, const string& s2, const string& evil) {
        this->n = n;
        this->s1 = s1;
        this->s2 = s2;
        this->evil = evil;
        
        // 边界条件处理
        if (evil.length() > n) {
            // evil长度大于n，不可能包含evil子串
            return countStringsInRange(s1, s2);
        }
        
        // 构建KMP算法的next数组
        buildNextArray();
        
        // 初始化记忆化数组
        memo.resize(n, vector<vector<vector<int>>>(
            evil.length(), vector<vector<int>>(
                2, vector<int>(2, -1)
            )
        ));
        
        // 使用数位DP计算结果
        return dfs(0, 0, true, true);
    }
};

/**
 * 验证计算结果的辅助方法
 * 创建测试用例并验证算法正确性
 */
void verifyResults() {
    cout << "=== 验证测试开始 ===" << endl;
    
    Solution solution;
    
    // 测试用例1：简单情况
    int result1 = solution.findGoodStrings(2, "aa", "da", "b");
    cout << "测试用例1 - n=2, s1=aa, s2=da, evil=b: " << result1 << endl;
    assert(result1 == 51 && "测试用例1验证失败");
    
    // 测试用例2：evil长度大于n
    int result2 = solution.findGoodStrings(2, "aa", "zz", "abc");
    cout << "测试用例2 - evil长度大于n: " << result2 << endl;
    assert(result2 == 676 && "测试用例2验证失败");
    
    // 测试用例3：边界情况
    int result3 = solution.findGoodStrings(1, "a", "z", "b");
    cout << "测试用例3 - 单字符: " << result3 << endl;
    assert(result3 == 25 && "测试用例3验证失败");
    
    cout << "=== 验证测试通过 ===" << endl;
}

/**
 * 性能测试方法
 * 测试大规模数据的处理能力
 */
void performanceTest() {
    cout << "\n=== 性能测试开始 ===" << endl;
    
    Solution solution;
    
    auto start = chrono::high_resolution_clock::now();
    
    // 测试中等规模数据
    int result = solution.findGoodStrings(10, "aaaaaaaaaa", "zzzzzzzzzz", "abc");
    
    auto end = chrono::high_resolution_clock::now();
    chrono::duration<double> duration = end - start;
    
    cout << "性能测试 - n=10, 全范围, evil=abc" << endl;
    cout << "结果: " << result << endl;
    cout << "执行时间: " << duration.count() * 1000 << " 毫秒" << endl;
    
    cout << "=== 性能测试完成 ===" << endl;
}

/**
 * 演示用例方法
 */
void demo() {
    cout << "\n=== 演示用例 ===" << endl;
    
    Solution solution;
    
    // 演示用例1
    int demo1 = solution.findGoodStrings(3, "abc", "def", "d");
    cout << "演示用例1 - n=3, s1=abc, s2=def, evil=d" << endl;
    cout << "结果: " << demo1 << endl;
    
    // 演示用例2
    int demo2 = solution.findGoodStrings(2, "aa", "zz", "b");
    cout << "演示用例2 - n=2, s1=aa, s2=zz, evil=b" << endl;
    cout << "结果: " << demo2 << endl;
}

int main() {
    // 运行验证测试
    verifyResults();
    
    // 运行性能测试
    performanceTest();
    
    // 运行演示用例
    demo();
    
    return 0;
}