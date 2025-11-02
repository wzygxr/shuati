/**
 * 文件名: Code10_LeetCode1044_LongestDuplicateSubstring.cpp
 * 算法名称: LeetCode 1044. 最长重复子串
 * 应用场景: 字符串处理、滚动哈希、二分查找
 * 实现语言: C++
 * 作者: 算法实现者
 * 创建时间: 2024-10-26
 * 最后修改: 2024-10-26
 * 版本: 1.0
 * 
 * 题目来源: https://leetcode.com/problems/longest-duplicate-substring/
 * 题目描述: 给定一个字符串 s，找出其中最长的重复子串。如果有多个最长重复子串，返回任意一个。
 * 
 * 解题思路:
 * 1. 使用二分查找确定可能的最长子串长度
 * 2. 对于每个长度mid，使用滚动哈希计算所有长度为mid的子串的哈希值
 * 3. 使用哈希集合检测是否存在重复的子串
 * 4. 使用双哈希技术减少哈希冲突的概率
 * 
 * 时间复杂度分析:
 * - 二分查找: O(log n)
 * - 每次检查: O(n) 计算哈希值
 * - 总时间复杂度: O(n log n)
 * 
 * 空间复杂度分析:
 * - 哈希集合存储哈希值: O(n)
 * - 辅助数组: O(n)
 * - 总空间复杂度: O(n)
 * 
 * 工程化考量:
 * - 使用大质数减少哈希冲突
 * - 双哈希技术提高准确性
 * - 处理边界情况(空字符串、单字符等)
 * - 优化内存使用，避免不必要的对象创建
 */

#include <iostream>
#include <string>
#include <unordered_set>
#include <vector>
#include <algorithm>

using namespace std;

class Solution {
private:
    // 双哈希技术使用的大质数
    static constexpr long long MOD1 = 1000000007LL;
    static constexpr long long MOD2 = 1000000009LL;
    
    // 基数，通常选择大于字符集大小的质数
    static constexpr long long BASE = 131LL;

public:
    /**
     * 主方法：寻找最长重复子串
     * 
     * @param s 输入字符串
     * @return 最长重复子串，如果没有重复子串则返回空字符串
     * 
     * 算法步骤:
     * 1. 边界检查：空字符串或单字符处理
     * 2. 二分查找确定可能的最长子串长度
     * 3. 对于每个长度mid，检查是否存在重复子串
     * 4. 使用滚动哈希优化性能
     */
    string longestDupSubstring(string s) {
        if (s.length() <= 1) {
            return "";
        }
        
        int n = s.length();
        int left = 1, right = n - 1;
        string result = "";
        
        // 预处理幂数组，用于快速计算哈希值
        vector<long long> pow1(n + 1);
        vector<long long> pow2(n + 1);
        pow1[0] = 1;
        pow2[0] = 1;
        for (int i = 1; i <= n; i++) {
            pow1[i] = (pow1[i - 1] * BASE) % MOD1;
            pow2[i] = (pow2[i - 1] * BASE) % MOD2;
        }
        
        // 预处理前缀哈希数组
        vector<long long> hash1(n + 1);
        vector<long long> hash2(n + 1);
        for (int i = 0; i < n; i++) {
            hash1[i + 1] = (hash1[i] * BASE + s[i]) % MOD1;
            hash2[i + 1] = (hash2[i] * BASE + s[i]) % MOD2;
        }
        
        // 二分查找最长重复子串长度
        while (left <= right) {
            int mid = left + (right - left) / 2;
            string dup = findDuplicate(s, mid, hash1, hash2, pow1, pow2);
            
            if (!dup.empty()) {
                // 找到重复子串，尝试更长的长度
                result = dup;
                left = mid + 1;
            } else {
                // 未找到重复子串，尝试更短的长度
                right = mid - 1;
            }
        }
        
        return result;
    }
    
private:
    /**
     * 查找指定长度的重复子串
     * 
     * @param s 输入字符串
     * @param len 要查找的子串长度
     * @param hash1 第一个哈希函数的前缀哈希数组
     * @param hash2 第二个哈希函数的前缀哈希数组
     * @param pow1 第一个哈希函数的幂数组
     * @param pow2 第二个哈希函数的幂数组
     * @return 重复子串，如果不存在则返回空字符串
     * 
     * 算法步骤:
     * 1. 使用滚动哈希计算所有长度为len的子串的哈希值
     * 2. 使用双哈希技术减少冲突
     * 3. 使用哈希集合检测重复
     * 4. 如果找到重复，返回对应的子串
     */
    string findDuplicate(const string& s, int len, const vector<long long>& hash1, 
                       const vector<long long>& hash2, const vector<long long>& pow1, 
                       const vector<long long>& pow2) {
        unordered_set<long long> seen;
        int n = s.length();
        
        for (int i = 0; i <= n - len; i++) {
            // 计算子串的哈希值（双哈希）
            long long h1 = (hash1[i + len] - hash1[i] * pow1[len] % MOD1 + MOD1) % MOD1;
            long long h2 = (hash2[i + len] - hash2[i] * pow2[len] % MOD2 + MOD2) % MOD2;
            
            // 将双哈希组合成一个唯一的键
            long long key = h1 * MOD2 + h2;
            
            if (seen.find(key) != seen.end()) {
                // 找到重复子串，返回该子串
                return s.substr(i, len);
            }
            
            seen.insert(key);
        }
        
        return "";
    }
};

/**
 * 测试函数：验证算法正确性
 * 
 * 测试用例设计:
 * 1. 空字符串和单字符边界情况
 * 2. 普通重复子串情况
 * 3. 多个重复子串情况
 * 4. 无重复子串情况
 * 5. 极端长字符串情况
 */
void testLongestDupSubstring() {
    Solution solution;
    
    // 测试用例1: 普通情况
    string test1 = "banana";
    cout << "测试1 (banana): " << solution.longestDupSubstring(test1) << endl;
    // 预期输出: "ana" 或 "na"
    
    // 测试用例2: 多个重复子串
    string test2 = "abcd";
    cout << "测试2 (abcd): " << solution.longestDupSubstring(test2) << endl;
    // 预期输出: ""
    
    // 测试用例3: 边界情况
    string test3 = "a";
    cout << "测试3 (a): " << solution.longestDupSubstring(test3) << endl;
    // 预期输出: ""
    
    // 测试用例4: 长重复子串
    string test4 = "abcabcabc";
    cout << "测试4 (abcabcabc): " << solution.longestDupSubstring(test4) << endl;
    // 预期输出: "abcabc"
    
    // 测试用例5: 空字符串
    string test5 = "";
    cout << "测试5 (空字符串): " << solution.longestDupSubstring(test5) << endl;
    // 预期输出: ""
    
    // 测试用例6: 性能测试 - 长字符串
    string test6 = "aaaaaaaaaa";
    cout << "测试6 (aaaaaaaaaa): " << solution.longestDupSubstring(test6) << endl;
    // 预期输出: "aaaaaaaaa"
}

int main() {
    testLongestDupSubstring();
    return 0;
}

/**
 * C++实现特点分析:
 * 
 * 性能优化:
 * 1. 使用constexpr编译时常量提高性能
 * 2. 使用vector代替动态数组，提供更好的内存管理
 * 3. 使用unordered_set提供O(1)的平均查找性能
 * 4. 避免不必要的字符串拷贝，使用const引用
 * 
 * 内存管理:
 * 1. vector自动管理内存，避免手动内存分配
 * 2. 使用RAII原则确保资源正确释放
 * 3. 避免内存泄漏和悬空指针
 * 
 * 异常安全:
 * 1. 使用标准库容器，提供强异常安全保证
 * 2. 边界检查防止数组越界
 * 3. 模运算处理防止整数溢出
 * 
 * 工程化优势:
 * 1. 类型安全：强类型系统减少运行时错误
 * 2. 模板支持：可扩展为泛型实现
 * 3. 标准库丰富：提供高效的数据结构和算法
 * 4. 性能可控：直接内存访问和优化支持
 * 
 * 与Java/Python对比:
 * 1. 性能：C++通常具有更好的运行时性能
 * 2. 内存控制：手动内存管理提供更精细的控制
 * 3. 编译时优化：模板和constexpr支持编译时计算
 * 4. 系统级访问：可直接操作内存和硬件资源
 */