#include <iostream>
#include <vector>
#include <bitset>
#include <stdexcept>
#include <chrono>
#include <random>
#include <algorithm>
#include <functional>
#include <map>
#include <unordered_map>
#include <queue>
#include <stack>
#include <string>
#include <sstream>
#include <iomanip>
#include <cmath>
#include <climits>
#include <cassert>
#include <limits>
#include <unordered_set>
#include <cstdint>
#include <set>

using namespace std;

/**
 * 高级位操作算法实现
 * 包含LeetCode多个高级位操作相关题目的解决方案
 * 
 * 题目列表:
 * 1. LeetCode 78 - 子集
 * 2. LeetCode 90 - 子集 II
 * 3. LeetCode 187 - 重复的DNA序列
 * 4. LeetCode 190 - 颠倒二进制位
 * 5. LeetCode 318 - 最大单词长度乘积
 * 6. LeetCode 393 - UTF-8 编码验证
 * 7. LeetCode 397 - 整数替换
 * 8. LeetCode 401 - 二进制手表
 * 9. LeetCode 421 - 数组中两个数的最大异或值
 * 10. LeetCode 461 - 汉明距离
 * 
 * 时间复杂度分析:
 * - 位运算操作: O(1) 到 O(2^n)
 * - 空间复杂度: O(1) 到 O(n)
 * 
 * 工程化考量:
 * 1. 位掩码技巧: 使用位掩码表示集合
 * 2. 状态压缩: 使用位运算压缩状态
 * 3. 性能优化: 利用位运算的并行性
 * 4. 边界处理: 处理整数溢出、负数等边界情况
 */

class BitManipulationAdvanced {
public:
    /**
     * LeetCode 78 - 子集
     * 题目链接: https://leetcode.com/problems/subsets/
     * 给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
     * 
     * 方法: 位掩码法
     * 时间复杂度: O(n * 2^n)
     * 空间复杂度: O(2^n)
     * 
     * 原理: 使用二进制位表示每个元素是否在子集中
     * 从0到2^n-1的每个数字对应一个子集
     */
    static vector<vector<int>> subsets(vector<int>& nums) {
        int n = nums.size();
        int total = 1 << n;  // 2^n个子集
        vector<vector<int>> result;
        
        for (int mask = 0; mask < total; mask++) {
            vector<int> subset;
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    subset.push_back(nums[i]);
                }
            }
            result.push_back(subset);
        }
        
        return result;
    }
    
    /**
     * LeetCode 90 - 子集 II
     * 题目链接: https://leetcode.com/problems/subsets-ii/
     * 给定一个可能包含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
     * 
     * 方法: 排序 + 位掩码 + 去重
     * 时间复杂度: O(n * 2^n)
     * 空间复杂度: O(2^n)
     * 
     * 原理: 先排序，然后使用位掩码生成子集，使用集合去重
     */
    static vector<vector<int>> subsetsWithDup(vector<int>& nums) {
        sort(nums.begin(), nums.end());
        int n = nums.size();
        int total = 1 << n;
        set<vector<int>> unique_subsets;
        
        for (int mask = 0; mask < total; mask++) {
            vector<int> subset;
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    subset.push_back(nums[i]);
                }
            }
            unique_subsets.insert(subset);
        }
        
        vector<vector<int>> result(unique_subsets.begin(), unique_subsets.end());
        return result;
    }
    
    /**
     * LeetCode 187 - 重复的DNA序列
     * 题目链接: https://leetcode.com/problems/repeated-dna-sequences/
     * 所有 DNA 都由一系列缩写为 'A'，'C'，'G' 和 'T' 的核苷酸组成，例如："ACGAATTCCG"。
     * 编写一个函数来找出所有目标子串，目标子串的长度为 10，且在 DNA 字符串 s 中出现超过一次。
     * 
     * 方法: 滑动窗口 + 位编码
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * 原理: 使用2位表示每个字符，10个字符需要20位，可以用整数表示
     */
    static vector<string> findRepeatedDnaSequences(string s) {
        if (s.length() < 10) return {};
        
        // 字符到数字的映射
        unordered_map<char, int> mapping = {{'A', 0}, {'C', 1}, {'G', 2}, {'T', 3}};
        unordered_map<int, int> count;
        vector<string> result;
        
        int hash = 0;
        // 计算第一个窗口的哈希值
        for (int i = 0; i < 10; i++) {
            hash = (hash << 2) | mapping[s[i]];
        }
        count[hash]++;
        
        // 滑动窗口
        for (int i = 10; i < s.length(); i++) {
            // 移除最左边的字符，添加新字符
            hash = ((hash << 2) & 0xFFFFF) | mapping[s[i]];
            count[hash]++;
            
            if (count[hash] == 2) {  // 第一次重复出现
                result.push_back(s.substr(i - 9, 10));
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 190 - 颠倒二进制位
     * 题目链接: https://leetcode.com/problems/reverse-bits/
     * 颠倒给定的 32 位无符号整数的二进制位。
     * 
     * 方法: 逐位反转
     * 时间复杂度: O(1) - 固定32位
     * 空间复杂度: O(1)
     */
    static uint32_t reverseBits(uint32_t n) {
        uint32_t result = 0;
        
        for (int i = 0; i < 32; i++) {
            result = (result << 1) | (n & 1);
            n >>= 1;
        }
        
        return result;
    }
    
    /**
     * LeetCode 318 - 最大单词长度乘积
     * 题目链接: https://leetcode.com/problems/maximum-product-of-word-lengths/
     * 给定一个字符串数组 words，找到 length(word[i]) * length(word[j]) 的最大值，
     * 并且这两个单词不含有公共字母。你可以认为每个单词只包含小写字母。
     * 
     * 方法: 位掩码 + 预计算
     * 时间复杂度: O(n^2 + n * L)
     * 空间复杂度: O(n)
     * 
     * 原理: 使用26位表示每个单词包含的字母，没有公共字母即位掩码与运算为0
     */
    static int maxProduct(vector<string>& words) {
        int n = words.size();
        vector<int> masks(n, 0);
        vector<int> lengths(n, 0);
        
        // 预计算每个单词的位掩码和长度
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (char c : words[i]) {
                mask |= (1 << (c - 'a'));
            }
            masks[i] = mask;
            lengths[i] = words[i].length();
        }
        
        int max_product = 0;
        // 检查所有单词对
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((masks[i] & masks[j]) == 0) {  // 没有公共字母
                    max_product = max(max_product, lengths[i] * lengths[j]);
                }
            }
        }
        
        return max_product;
    }
    
    /**
     * LeetCode 393 - UTF-8 编码验证
     * 题目链接: https://leetcode.com/problems/utf-8-validation/
     * 给定一个表示数据的整数数组 data ，返回它是否为有效的 UTF-8 编码。
     * 
     * 方法: 位运算检查编码规则
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * UTF-8编码规则:
     * 1. 1字节字符: 0xxxxxxx
     * 2. 2字节字符: 110xxxxx 10xxxxxx
     * 3. 3字节字符: 1110xxxx 10xxxxxx 10xxxxxx
     * 4. 4字节字符: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
     */
    static bool validUtf8(vector<int>& data) {
        int count = 0;  // 后续字节数量
        
        for (int byte : data) {
            if (count == 0) {
                // 检查首字节
                if ((byte >> 5) == 0b110) {        // 2字节字符
                    count = 1;
                } else if ((byte >> 4) == 0b1110) { // 3字节字符
                    count = 2;
                } else if ((byte >> 3) == 0b11110) { // 4字节字符
                    count = 3;
                } else if ((byte >> 7) != 0) {      // 无效首字节
                    return false;
                }
            } else {
                // 检查后续字节
                if ((byte >> 6) != 0b10) {
                    return false;
                }
                count--;
            }
        }
        
        return count == 0;  // 所有多字节字符都完整
    }
    
    /**
     * LeetCode 397 - 整数替换
     * 题目链接: https://leetcode.com/problems/integer-replacement/
     * 给定一个正整数 n ，你可以做如下操作：
     * 1. 如果 n 是偶数，则用 n / 2替换 n
     * 2. 如果 n 是奇数，则可以用 n + 1或n - 1替换 n
     * 返回 n 变为 1 所需的最小替换次数。
     * 
     * 方法: 贪心 + 位运算
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     */
    static int integerReplacement(int n) {
        return integerReplacementHelper((long long)n, 0);
    }
    
private:
    static int integerReplacementHelper(long long n, int steps) {
        if (n == 1) return steps;
        
        if (n % 2 == 0) {
            return integerReplacementHelper(n / 2, steps + 1);
        } else {
            return min(integerReplacementHelper(n + 1, steps + 1),
                      integerReplacementHelper(n - 1, steps + 1));
        }
    }
    
public:
    /**
     * LeetCode 401 - 二进制手表
     * 题目链接: https://leetcode.com/problems/binary-watch/
     * 二进制手表顶部有 4 个 LED 代表 小时（0-11），底部的 6 个 LED 代表 分钟（0-59）。
     * 给定一个非负整数 turnedOn ，表示当前亮着的 LED 的数量，返回二进制手表可能表示的所有时间。
     * 
     * 方法: 枚举所有可能的时间组合
     * 时间复杂度: O(1) - 固定12*60种可能
     * 空间复杂度: O(1)
     */
    static vector<string> readBinaryWatch(int turnedOn) {
        vector<string> result;
        
        for (int h = 0; h < 12; h++) {
            for (int m = 0; m < 60; m++) {
                if (__builtin_popcount(h) + __builtin_popcount(m) == turnedOn) {
                    result.push_back(to_string(h) + ":" + 
                                   (m < 10 ? "0" : "") + to_string(m));
                }
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 421 - 数组中两个数的最大异或值
     * 题目链接: https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
     * 给定一个非空数组，数组中元素为 a0, a1, a2, … , an-1，其中 0 ≤ ai < 2^31。
     * 找到 ai 和 aj 最大的异或 (XOR) 运算结果，其中 0 ≤ i, j < n。
     * 
     * 方法: 前缀树 + 贪心
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    static int findMaximumXOR(vector<int>& nums) {
        int max_xor = 0, mask = 0;
        
        for (int i = 31; i >= 0; i--) {
            mask |= (1 << i);
            unordered_set<int> prefixes;
            
            // 提取前缀
            for (int num : nums) {
                prefixes.insert(num & mask);
            }
            
            // 尝试设置当前位为1
            int candidate = max_xor | (1 << i);
            for (int prefix : prefixes) {
                if (prefixes.find(candidate ^ prefix) != prefixes.end()) {
                    max_xor = candidate;
                    break;
                }
            }
        }
        
        return max_xor;
    }
    
    /**
     * LeetCode 461 - 汉明距离
     * 题目链接: https://leetcode.com/problems/hamming-distance/
     * 两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目。
     * 
     * 方法: 异或 + 统计1的个数
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    static int hammingDistance(int x, int y) {
        int xor_val = x ^ y;
        return __builtin_popcount(xor_val);
    }
};

class PerformanceTester {
public:
    static void testSubsets() {
        cout << "=== 子集算法性能测试 ===" << endl;
        
        // 生成测试数据
        vector<int> nums;
        for (int i = 0; i < 20; i++) {
            nums.push_back(i);
        }
        
        auto start = chrono::high_resolution_clock::now();
        auto result = BitManipulationAdvanced::subsets(nums);
        auto time = chrono::duration_cast<chrono::milliseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "subsets: 子集数量=" << result.size() << ", 耗时=" << time << " ms" << endl;
    }
    
    static void testMaximumXOR() {
        cout << "\n=== 最大异或值性能测试 ===" << endl;
        
        // 生成测试数据
        vector<int> nums(10000);
        for (int i = 0; i < 10000; i++) {
            nums[i] = rand() % 1000000;
        }
        
        auto start = chrono::high_resolution_clock::now();
        int result = BitManipulationAdvanced::findMaximumXOR(nums);
        auto time = chrono::duration_cast<chrono::microseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "findMaximumXOR: 结果=" << result << ", 耗时=" << time << " μs" << endl;
    }
    
    static void runUnitTests() {
        cout << "=== 高级位操作算法单元测试 ===" << endl;
        
        // 测试子集
        vector<int> nums = {1, 2, 3};
        auto subsets = BitManipulationAdvanced::subsets(nums);
        assert(subsets.size() == 8);  // 2^3 = 8个子集
        
        // 测试汉明距离
        assert(BitManipulationAdvanced::hammingDistance(1, 4) == 2);
        
        // 测试UTF-8验证
        vector<int> utf8_data = {197, 130, 1};
        assert(BitManipulationAdvanced::validUtf8(utf8_data) == true);
        
        cout << "所有单元测试通过!" << endl;
    }
    
    static void complexityAnalysis() {
        cout << "\n=== 复杂度分析 ===" << endl;
        
        vector<pair<string, string>> algorithms = {
            {"subsets", "O(n * 2^n), O(2^n)"},
            {"subsetsWithDup", "O(n * 2^n), O(2^n)"},
            {"findRepeatedDnaSequences", "O(n), O(n)"},
            {"reverseBits", "O(1), O(1)"},
            {"maxProduct", "O(n^2 + n*L), O(n)"},
            {"validUtf8", "O(n), O(1)"},
            {"integerReplacement", "O(log n), O(log n)"},
            {"findMaximumXOR", "O(n), O(n)"}
        };
        
        for (auto& algo : algorithms) {
            cout << algo.first << ": 时间复杂度=" << algo.second << endl;
        }
    }
};

int main() {
    cout << "高级位操作算法实现" << endl;
    cout << "包含LeetCode多个高级位操作相关题目的解决方案" << endl;
    cout << "=============================================" << endl;
    
    // 运行单元测试
    PerformanceTester::runUnitTests();
    
    // 运行性能测试
    PerformanceTester::testSubsets();
    PerformanceTester::testMaximumXOR();
    
    // 复杂度分析
    PerformanceTester::complexityAnalysis();
    
    // 示例使用
    cout << "\n=== 示例使用 ===" << endl;
    
    // 子集示例
    vector<int> nums = {1, 2, 3};
    cout << "数组: ";
    for (int num : nums) cout << num << " ";
    cout << endl;
    
    auto subsets = BitManipulationAdvanced::subsets(nums);
    cout << "子集数量: " << subsets.size() << endl;
    cout << "前3个子集: " << endl;
    for (int i = 0; i < min(3, (int)subsets.size()); i++) {
        cout << "  {";
        for (int j = 0; j < subsets[i].size(); j++) {
            cout << subsets[i][j];
            if (j < subsets[i].size() - 1) cout << ", ";
        }
        cout << "}" << endl;
    }
    
    // 汉明距离示例
    int x = 1, y = 4;
    cout << "汉明距离(" << x << ", " << y << ") = " 
         << BitManipulationAdvanced::hammingDistance(x, y) << endl;
    
    // 二进制表示示例
    uint32_t n = 43261596;  // 00000010100101000001111010011100
    cout << "原始数字: " << n << endl;
    cout << "反转后: " << BitManipulationAdvanced::reverseBits(n) << endl;
    
    return 0;
}