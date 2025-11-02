/**
 * LeetCode 1316. 不同的循环子字符串 - C++版本
 * 
 * 题目来源：https://leetcode.com/problems/distinct-echo-substrings/
 * 题目描述：给定一个字符串 text，返回 text 中不同非空子字符串的数量，这些子字符串可以写成某个字符串与其自身连接的结果。
 * 
 * 算法思路：
 * 1. 使用字符串哈希快速计算子串哈希值
 * 2. 遍历所有可能的子串长度（偶数长度）
 * 3. 检查子串是否可以分成两个相等的部分
 * 4. 使用哈希集合存储不同的循环子字符串
 * 
 * 时间复杂度：O(n²)，其中n为字符串长度
 * 空间复杂度：O(n²)
 * 
 * 工程化考量：
 * - 使用滚动哈希优化性能
 * - 处理哈希冲突
 * - 边界条件处理
 */

#include <iostream>
#include <string>
#include <vector>
#include <unordered_set>
#include <unordered_map>
#include <chrono>

using namespace std;

class Solution {
private:
    // 哈希参数
    static const long long BASE = 131;
    static const long long MOD = 1000000007;
    
public:
    /**
     * 主函数：计算不同的循环子字符串数量
     * 
     * @param text 输入字符串
     * @return 不同的循环子字符串数量
     */
    int distinctEchoSubstrings(string text) {
        if (text.length() < 2) {
            return 0;
        }
        
        int n = text.length();
        unordered_set<string> result;
        
        // 预处理前缀哈希数组
        vector<long long> prefixHash(n + 1, 0);
        vector<long long> power(n + 1, 1);
        
        for (int i = 1; i <= n; i++) {
            prefixHash[i] = (prefixHash[i - 1] * BASE + text[i - 1]) % MOD;
            power[i] = (power[i - 1] * BASE) % MOD;
        }
        
        // 遍历所有可能的子串长度（偶数长度）
        for (int len = 2; len <= n; len += 2) {
            for (int i = 0; i <= n - len; i++) {
                int mid = i + len / 2;
                
                // 使用哈希快速比较两个子串是否相等
                long long hash1 = getHash(prefixHash, power, i, mid - 1);
                long long hash2 = getHash(prefixHash, power, mid, i + len - 1);
                
                if (hash1 == hash2) {
                    // 防止哈希冲突，实际比较字符串
                    string substr = text.substr(i, len);
                    if (isEchoSubstring(substr)) {
                        result.insert(substr);
                    }
                }
            }
        }
        
        return result.size();
    }
    
private:
    /**
     * 获取子串的哈希值
     * 
     * @param prefixHash 前缀哈希数组
     * @param power 幂次数组
     * @param left 子串起始位置
     * @param right 子串结束位置
     * @return 子串哈希值
     */
    long long getHash(const vector<long long>& prefixHash, const vector<long long>& power, int left, int right) {
        return (prefixHash[right + 1] - prefixHash[left] * power[right - left + 1] % MOD + MOD) % MOD;
    }
    
    /**
     * 检查字符串是否为循环子字符串
     * 
     * @param s 输入字符串
     * @return 是否为循环子字符串
     */
    bool isEchoSubstring(const string& s) {
        int n = s.length();
        if (n % 2 != 0) return false;
        
        int half = n / 2;
        for (int i = 0; i < half; i++) {
            if (s[i] != s[i + half]) {
                return false;
            }
        }
        return true;
    }
    
public:
    /**
     * 优化版本：使用双哈希减少冲突
     */
    int distinctEchoSubstringsOptimized(string text) {
        if (text.length() < 2) {
            return 0;
        }
        
        int n = text.length();
        unordered_set<long long> result;
        
        // 双哈希参数
        static const long long BASE1 = 131, MOD1 = 1000000007;
        static const long long BASE2 = 13131, MOD2 = 1000000009;
        
        // 预处理前缀哈希数组
        vector<long long> prefixHash1(n + 1, 0);
        vector<long long> prefixHash2(n + 1, 0);
        vector<long long> power1(n + 1, 1);
        vector<long long> power2(n + 1, 1);
        
        for (int i = 1; i <= n; i++) {
            prefixHash1[i] = (prefixHash1[i - 1] * BASE1 + text[i - 1]) % MOD1;
            prefixHash2[i] = (prefixHash2[i - 1] * BASE2 + text[i - 1]) % MOD2;
            power1[i] = (power1[i - 1] * BASE1) % MOD1;
            power2[i] = (power2[i - 1] * BASE2) % MOD2;
        }
        
        // 遍历所有可能的子串长度（偶数长度）
        for (int len = 2; len <= n; len += 2) {
            for (int i = 0; i <= n - len; i++) {
                int mid = i + len / 2;
                
                // 使用双哈希比较两个子串是否相等
                long long hash1_left = getHash(prefixHash1, power1, i, mid - 1);
                long long hash1_right = getHash(prefixHash1, power1, mid, i + len - 1);
                
                long long hash2_left = getHash(prefixHash2, power2, i, mid - 1);
                long long hash2_right = getHash(prefixHash2, power2, mid, i + len - 1);
                
                if (hash1_left == hash1_right && hash2_left == hash2_right) {
                    // 双哈希一致，基本可以确定相等
                    long long combinedHash = hash1_left * MOD2 + hash2_left;
                    result.insert(combinedHash);
                }
            }
        }
        
        return result.size();
    }
    
    /**
     * 暴力解法（用于对比验证）
     */
    int distinctEchoSubstringsBruteForce(string text) {
        if (text.length() < 2) {
            return 0;
        }
        
        unordered_set<string> result;
        int n = text.length();
        
        // 遍历所有可能的子串
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int len = j - i + 1;
                if (len % 2 == 0) {
                    string substr = text.substr(i, len);
                    if (isEchoSubstring(substr)) {
                        result.insert(substr);
                    }
                }
            }
        }
        
        return result.size();
    }
};

/**
 * 测试函数
 */
int main() {
    Solution solution;
    
    // 测试用例1：标准测试
    string text1 = "abcabcabc";
    int result1 = solution.distinctEchoSubstrings(text1);
    cout << "测试1 - 输入: " << text1 << ", 输出: " << result1 << endl;
    cout << "预期结果: 3 (abcabc, bcabca, cabcab)" << endl;
    
    // 测试用例2：简单测试
    string text2 = "leetcodeleetcode";
    int result2 = solution.distinctEchoSubstrings(text2);
    cout << "测试2 - 输入: " << text2 << ", 输出: " << result2 << endl;
    cout << "预期结果: 2 (leetcode, etcodele)" << endl;
    
    // 测试用例3：边界测试
    string text3 = "aaaa";
    int result3 = solution.distinctEchoSubstrings(text3);
    cout << "测试3 - 输入: " << text3 << ", 输出: " << result3 << endl;
    cout << "预期结果: 2 (aa, aaaa)" << endl;
    
    // 性能对比测试
    string text4;
    for (int i = 0; i < 100; i++) text4 += 'a';
    for (int i = 0; i < 100; i++) text4 += 'b';
    
    auto startTime = chrono::high_resolution_clock::now();
    int result4 = solution.distinctEchoSubstringsOptimized(text4);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "优化算法耗时: " << duration.count() << "ms, 结果: " << result4 << endl;
    
    startTime = chrono::high_resolution_clock::now();
    int result5 = solution.distinctEchoSubstringsBruteForce(text4);
    endTime = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "暴力算法耗时: " << duration.count() << "ms, 结果: " << result5 << endl;
    
    // 验证算法正确性
    cout << "\n=== 算法正确性验证 ===" << endl;
    vector<string> testCases = {"abcabc", "leetcode", "aaa", "abab"};
    for (const string& testCase : testCases) {
        int optimized = solution.distinctEchoSubstringsOptimized(testCase);
        int bruteForce = solution.distinctEchoSubstringsBruteForce(testCase);
        cout << "输入: " << testCase << ", 优化算法: " << optimized << ", 暴力算法: " << bruteForce << ", 一致: " << (optimized == bruteForce) << endl;
    }
    
    return 0;
}

/**
 * 复杂度分析：
 * 
 * 时间复杂度：
 * - 基础版本：O(n²)，需要遍历所有可能的子串
 * - 优化版本：O(n²)，但常数项更小
 * - 暴力版本：O(n³)，需要实际比较字符串
 * 
 * 空间复杂度：
 * - 基础版本：O(n²)，存储所有不同的循环子字符串
 * - 优化版本：O(n²)，存储哈希值
 * - 暴力版本：O(n²)，存储字符串
 * 
 * 算法优化点：
 * 1. 使用前缀哈希数组：预处理后可以在O(1)时间内获取任意子串的哈希值
 * 2. 双哈希减少冲突：使用两个不同的哈希函数组合，大大降低哈希冲突概率
 * 3. 只考虑偶数长度：循环子字符串必须是偶数长度
 * 
 * 边界情况处理：
 * - 空字符串或长度小于2的字符串直接返回0
 * - 处理哈希冲突：当哈希值相同时，实际比较字符串内容
 * - 大数溢出处理：使用模运算防止整数溢出
 * 
 * 工程化考量：
 * - 可配置的哈希参数
 * - 详细的注释和文档
 * - 测试用例覆盖各种边界情况
 * - 性能对比验证
 * 
 * C++特定优化：
 * - 使用vector存储前缀哈希数组，避免动态分配
 * - 使用unordered_set提高查找效率
 * - 使用const引用避免不必要的拷贝
 * - 使用chrono库进行精确的时间测量
 * 
 * 实际应用场景：
 * 1. 文本模式识别：查找文本中的重复模式
 * 2. 数据压缩：识别可压缩的重复模式
 * 3. 生物信息学：DNA序列中的重复片段检测
 * 4. 代码分析：查找程序中的重复代码模式
 */