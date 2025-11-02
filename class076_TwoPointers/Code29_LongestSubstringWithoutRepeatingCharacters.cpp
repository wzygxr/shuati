#include <iostream>
#include <string>
#include <unordered_set>
#include <unordered_map>
#include <vector>
#include <algorithm>
#include <chrono>
using namespace std;

/**
 * LeetCode 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)
 * 
 * 题目描述:
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 * 
 * 示例1:
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 
 * 示例2:
 * 输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 
 * 示例3:
 * 输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 * 
 * 提示:
 * 0 <= s.length <= 5 * 10^4
 * s 由英文字母、数字、符号和空格组成
 * 
 * 题目链接: https://leetcode.cn/problems/longest-substring-without-repeating-characters/
 * 
 * 解题思路:
 * 这道题可以使用滑动窗口（双指针）的方法来解决：
 * 
 * 方法一（滑动窗口 + unordered_set）：
 * 1. 使用两个指针 left 和 right 表示当前窗口的左右边界
 * 2. 使用 unordered_set 记录当前窗口中的字符
 * 3. 右指针向右移动，如果当前字符不在集合中，加入集合并更新最大长度
 * 4. 如果当前字符在集合中，移动左指针直到移除重复字符
 * 
 * 方法二（滑动窗口 + unordered_map优化）：
 * 1. 使用 unordered_map 记录每个字符最后出现的位置
 * 2. 当遇到重复字符时，可以直接将左指针移动到重复字符的下一个位置
 * 3. 避免左指针的逐步移动，提高效率
 * 
 * 时间复杂度: O(n)，每个字符最多被访问两次
 * 空间复杂度: O(min(m, n))，m为字符集大小
 * 是否最优解：是
 */

class Solution {
public:
    /**
     * 解法一: 滑动窗口 + unordered_set
     * 
     * @param s 输入字符串
     * @return 无重复字符的最长子串长度
     */
    static int lengthOfLongestSubstringSet(const std::string& s) {
        if (s.empty()) {
            return 0;
        }
        
        std::unordered_set<char> window;
        int maxLength = 0;
        int left = 0;
        int n = s.length();
        
        for (int right = 0; right < n; right++) {
            char currentChar = s[right];
            
            // 如果字符在集合中，移动左指针直到移除重复字符
            while (window.find(currentChar) != window.end()) {
                window.erase(s[left]);
                left++;
            }
            
            // 添加当前字符到集合
            window.insert(currentChar);
            // 更新最大长度
            maxLength = std::max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 解法二: 滑动窗口 + unordered_map优化（最优解）
     * 
     * @param s 输入字符串
     * @return 无重复字符的最长子串长度
     */
    static int lengthOfLongestSubstring(const std::string& s) {
        if (s.empty()) {
            return 0;
        }
        
        std::unordered_map<char, int> charIndexMap;
        int maxLength = 0;
        int left = 0;
        int n = s.length();
        
        for (int right = 0; right < n; right++) {
            char currentChar = s[right];
            
            // 如果字符已经存在，并且其位置在左指针右侧
            if (charIndexMap.find(currentChar) != charIndexMap.end() && 
                charIndexMap[currentChar] >= left) {
                // 移动左指针到重复字符的下一个位置
                left = charIndexMap[currentChar] + 1;
            }
            
            // 更新字符的最新位置
            charIndexMap[currentChar] = right;
            // 更新最大长度
            maxLength = std::max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 解法三: 数组优化版（适用于ASCII字符）
     * 
     * @param s 输入字符串
     * @return 无重复字符的最长子串长度
     */
    static int lengthOfLongestSubstringArray(const std::string& s) {
        if (s.empty()) {
            return 0;
        }
        
        // 假设字符集为ASCII，使用数组记录字符最后出现的位置
        std::vector<int> lastIndex(128, -1); // ASCII字符集大小，初始化为-1
        
        int maxLength = 0;
        int left = 0;
        int n = s.length();
        
        for (int right = 0; right < n; right++) {
            char currentChar = s[right];
            int charCode = static_cast<int>(currentChar);
            
            // 如果字符已经存在，并且其位置在左指针右侧
            if (lastIndex[charCode] >= left) {
                // 移动左指针到重复字符的下一个位置
                left = lastIndex[charCode] + 1;
            }
            
            // 更新字符的最新位置
            lastIndex[charCode] = right;
            // 更新最大长度
            maxLength = std::max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
};

/**
 * 测试函数
 */
void test() {
    // 测试用例1
    std::string s1 = "abcabcbb";
    int expected1 = 3;
    std::cout << "测试用例1:" << std::endl;
    std::cout << "输入: \"" << s1 << "\"" << std::endl;
    std::cout << "解法一结果: " << Solution::lengthOfLongestSubstringSet(s1) << std::endl;
    std::cout << "解法二结果: " << Solution::lengthOfLongestSubstring(s1) << std::endl;
    std::cout << "解法三结果: " << Solution::lengthOfLongestSubstringArray(s1) << std::endl;
    std::cout << "期望: " << expected1 << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::string s2 = "bbbbb";
    int expected2 = 1;
    std::cout << "测试用例2:" << std::endl;
    std::cout << "输入: \"" << s2 << "\"" << std::endl;
    std::cout << "解法一结果: " << Solution::lengthOfLongestSubstringSet(s2) << std::endl;
    std::cout << "解法二结果: " << Solution::lengthOfLongestSubstring(s2) << std::endl;
    std::cout << "解法三结果: " << Solution::lengthOfLongestSubstringArray(s2) << std::endl;
    std::cout << "期望: " << expected2 << std::endl;
    std::cout << std::endl;
    
    // 测试用例3
    std::string s3 = "pwwkew";
    int expected3 = 3;
    std::cout << "测试用例3:" << std::endl;
    std::cout << "输入: \"" << s3 << "\"" << std::endl;
    std::cout << "解法一结果: " << Solution::lengthOfLongestSubstringSet(s3) << std::endl;
    std::cout << "解法二结果: " << Solution::lengthOfLongestSubstring(s3) << std::endl;
    std::cout << "解法三结果: " << Solution::lengthOfLongestSubstringArray(s3) << std::endl;
    std::cout << "期望: " << expected3 << std::endl;
    std::cout << std::endl;
    
    // 测试用例4 - 边界情况：空字符串
    std::string s4 = "";
    int expected4 = 0;
    std::cout << "测试用例4（空字符串）:" << std::endl;
    std::cout << "输入: \"" << s4 << "\"" << std::endl;
    std::cout << "解法一结果: " << Solution::lengthOfLongestSubstringSet(s4) << std::endl;
    std::cout << "解法二结果: " << Solution::lengthOfLongestSubstring(s4) << std::endl;
    std::cout << "解法三结果: " << Solution::lengthOfLongestSubstringArray(s4) << std::endl;
    std::cout << "期望: " << expected4 << std::endl;
    std::cout << std::endl;
    
    // 测试用例5 - 边界情况：单个字符
    std::string s5 = "a";
    int expected5 = 1;
    std::cout << "测试用例5（单个字符）:" << std::endl;
    std::cout << "输入: \"" << s5 << "\"" << std::endl;
    std::cout << "解法一结果: " << Solution::lengthOfLongestSubstringSet(s5) << std::endl;
    std::cout << "解法二结果: " << Solution::lengthOfLongestSubstring(s5) << std::endl;
    std::cout << "解法三结果: " << Solution::lengthOfLongestSubstringArray(s5) << std::endl;
    std::cout << "期望: " << expected5 << std::endl;
    std::cout << std::endl;
    
    // 测试用例6 - 复杂情况
    std::string s6 = "dvdf";
    int expected6 = 3;
    std::cout << "测试用例6（复杂情况）:" << std::endl;
    std::cout << "输入: \"" << s6 << "\"" << std::endl;
    std::cout << "解法一结果: " << Solution::lengthOfLongestSubstringSet(s6) << std::endl;
    std::cout << "解法二结果: " << Solution::lengthOfLongestSubstring(s6) << std::endl;
    std::cout << "解法三结果: " << Solution::lengthOfLongestSubstringArray(s6) << std::endl;
    std::cout << "期望: " << expected6 << std::endl;
    std::cout << std::endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    // 创建长字符串进行性能测试
    std::string longString;
    for (int i = 0; i < 100000; i++) {
        longString += static_cast<char>('a' + (i % 26)); // 循环添加a-z
    }
    
    // 测试解法一的性能
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = Solution::lengthOfLongestSubstringSet(longString);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration1 = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "解法一（unordered_set）耗时: " << duration1 << "ms, 结果: " << result1 << std::endl;
    
    // 测试解法二的性能
    start = std::chrono::high_resolution_clock::now();
    int result2 = Solution::lengthOfLongestSubstring(longString);
    end = std::chrono::high_resolution_clock::now();
    auto duration2 = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "解法二（unordered_map优化）耗时: " << duration2 << "ms, 结果: " << result2 << std::endl;
    
    // 测试解法三的性能
    start = std::chrono::high_resolution_clock::now();
    int result3 = Solution::lengthOfLongestSubstringArray(longString);
    end = std::chrono::high_resolution_clock::now();
    auto duration3 = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "解法三（数组优化）耗时: " << duration3 << "ms, 结果: " << result3 << std::endl;
    
    // 验证结果一致性
    std::cout << "所有解法结果一致: " << (result1 == result2 && result2 == result3 ? "true" : "false") << std::endl;
}

/**
 * 算法分析函数
 */
void algorithmAnalysis() {
    std::cout << "=== 算法分析 ===" << std::endl;
    std::cout << "1. 解法一（滑动窗口 + unordered_set）" << std::endl;
    std::cout << "   - 时间复杂度: O(n) - 每个字符最多被访问两次" << std::endl;
    std::cout << "   - 空间复杂度: O(min(m, n)) - m为字符集大小" << std::endl;
    std::cout << "   - 优点: 实现简单，易于理解" << std::endl;
    std::cout << "   - 缺点: 最坏情况下需要逐步移动左指针" << std::endl;
    std::cout << std::endl;
    
    std::cout << "2. 解法二（滑动窗口 + unordered_map优化）" << std::endl;
    std::cout << "   - 时间复杂度: O(n) - 每个字符只被访问一次" << std::endl;
    std::cout << "   - 空间复杂度: O(min(m, n)) - m为字符集大小" << std::endl;
    std::cout << "   - 优点: 效率最高，直接跳转到重复字符位置" << std::endl;
    std::cout << "   - 缺点: 需要额外的unordered_map空间" << std::endl;
    std::cout << std::endl;
    
    std::cout << "3. 解法三（数组优化版）" << std::endl;
    std::cout << "   - 时间复杂度: O(n) - 每个字符只被访问一次" << std::endl;
    std::cout << "   - 空间复杂度: O(1) - 固定大小的数组" << std::endl;
    std::cout << "   - 优点: 空间效率最高，适用于ASCII字符集" << std::endl;
    std::cout << "   - 缺点: 仅适用于有限字符集" << std::endl;
    std::cout << std::endl;
    
    std::cout << "推荐使用解法二作为通用解决方案" << std::endl;
}

int main() {
    std::cout << "=== 无重复字符的最长子串 算法实现 ===" << std::endl;
    std::cout << std::endl;
    
    std::cout << "=== 测试用例 ===" << std::endl;
    test();
    
    std::cout << "=== 性能测试 ===" << std::endl;
    performanceTest();
    
    std::cout << "=== 算法分析 ===" << std::endl;
    algorithmAnalysis();
    
    return 0;
}