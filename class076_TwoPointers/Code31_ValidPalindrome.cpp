#include <iostream>
#include <string>
#include <cctype>
#include <algorithm>
#include <chrono>
using namespace std;

/**
 * LeetCode 125. 验证回文串 (Valid Palindrome)
 * 
 * 题目描述:
 * 如果在将所有大写字符转换为小写字符、并移除所有非字母数字字符之后，短语正着读和反着读都一样，则可以认为该短语是一个 回文串 。
 * 字母和数字都属于字母数字字符。
 * 给你一个字符串 s，如果它是 回文串 ，返回 true ；否则，返回 false 。
 * 
 * 示例1:
 * 输入: s = "A man, a plan, a canal: Panama"
 * 输出: true
 * 解释: "amanaplanacanalpanama" 是回文串。
 * 
 * 示例2:
 * 输入: s = "race a car"
 * 输出: false
 * 解释: "raceacar" 不是回文串。
 * 
 * 示例3:
 * 输入: s = " "
 * 输出: true
 * 解释: 在移除非字母数字字符后，s 变为 "" 。由于空字符串正着反着读都一样，所以是回文串。
 * 
 * 提示:
 * 1 <= s.length <= 2 * 10^5
 * s 仅由可打印的 ASCII 字符组成
 * 
 * 题目链接: https://leetcode.cn/problems/valid-palindrome/
 * 
 * 解题思路:
 * 这道题可以使用双指针的方法来解决：
 * 
 * 方法一（双指针 + 字符处理）：
 * 1. 使用两个指针 left 和 right 分别指向字符串的首尾
 * 2. 跳过非字母数字字符，只比较字母数字字符
 * 3. 比较左右指针指向的字符（忽略大小写）
 * 4. 如果所有字符都匹配，则返回true，否则返回false
 * 
 * 时间复杂度: O(n)，n为字符串长度
 * 空间复杂度: O(1)
 * 是否最优解：是
 */

class Solution {
public:
    /**
     * 解法一: 双指针（最优解）
     * 
     * @param s 输入字符串
     * @return 是否为回文串
     */
    static bool isPalindrome(const string& s) {
        if (s.empty()) {
            return true;
        }
        
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            // 跳过非字母数字字符（左指针）
            while (left < right && !isalnum(s[left])) {
                left++;
            }
            
            // 跳过非字母数字字符（右指针）
            while (left < right && !isalnum(s[right])) {
                right--;
            }
            
            // 比较字符（忽略大小写）
            if (tolower(s[left]) != tolower(s[right])) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
    
    /**
     * 解法二: 使用字符串反转比较
     * 
     * @param s 输入字符串
     * @return 是否为回文串
     */
    static bool isPalindromeString(const string& s) {
        if (s.empty()) {
            return true;
        }
        
        // 过滤非字母数字字符并转换为小写
        string filtered;
        for (char c : s) {
            if (isalnum(c)) {
                filtered += tolower(c);
            }
        }
        
        // 比较原字符串和反转后的字符串
        string reversed = filtered;
        reverse(reversed.begin(), reversed.end());
        
        return filtered == reversed;
    }
    
    /**
     * 解法三: 优化的双指针实现（避免重复计算）
     * 
     * @param s 输入字符串
     * @return 是否为回文串
     */
    static bool isPalindromeOptimized(const string& s) {
        if (s.empty()) {
            return true;
        }
        
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            char leftChar = s[left];
            char rightChar = s[right];
            
            // 如果左字符不是字母数字，跳过
            if (!isAlphanumeric(leftChar)) {
                left++;
                continue;
            }
            
            // 如果右字符不是字母数字，跳过
            if (!isAlphanumeric(rightChar)) {
                right--;
                continue;
            }
            
            // 比较字符（忽略大小写）
            if (toLower(leftChar) != toLower(rightChar)) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
    
private:
    /**
     * 判断字符是否为字母或数字
     * 
     * @param c 字符
     * @return 是否为字母数字
     */
    static bool isAlphanumeric(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               (c >= '0' && c <= '9');
    }
    
    /**
     * 将字符转换为小写（自定义实现）
     * 
     * @param c 字符
     * @return 小写字符
     */
    static char toLower(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 'a';
        }
        return c;
    }
};

/**
 * 测试函数
 */
void test() {
    // 测试用例1
    string s1 = "A man, a plan, a canal: Panama";
    bool expected1 = true;
    cout << "测试用例1:" << endl;
    cout << "输入: \"" << s1 << "\"" << endl;
    cout << "解法一结果: " << (Solution::isPalindrome(s1) ? "true" : "false") << endl;
    cout << "解法二结果: " << (Solution::isPalindromeString(s1) ? "true" : "false") << endl;
    cout << "解法三结果: " << (Solution::isPalindromeOptimized(s1) ? "true" : "false") << endl;
    cout << "期望: " << (expected1 ? "true" : "false") << endl;
    cout << endl;
    
    // 测试用例2
    string s2 = "race a car";
    bool expected2 = false;
    cout << "测试用例2:" << endl;
    cout << "输入: \"" << s2 << "\"" << endl;
    cout << "解法一结果: " << (Solution::isPalindrome(s2) ? "true" : "false") << endl;
    cout << "解法二结果: " << (Solution::isPalindromeString(s2) ? "true" : "false") << endl;
    cout << "解法三结果: " << (Solution::isPalindromeOptimized(s2) ? "true" : "false") << endl;
    cout << "期望: " << (expected2 ? "true" : "false") << endl;
    cout << endl;
    
    // 测试用例3
    string s3 = " ";
    bool expected3 = true;
    cout << "测试用例3:" << endl;
    cout << "输入: \"" << s3 << "\"" << endl;
    cout << "解法一结果: " << (Solution::isPalindrome(s3) ? "true" : "false") << endl;
    cout << "解法二结果: " << (Solution::isPalindromeString(s3) ? "true" : "false") << endl;
    cout << "解法三结果: " << (Solution::isPalindromeOptimized(s3) ? "true" : "false") << endl;
    cout << "期望: " << (expected3 ? "true" : "false") << endl;
    cout << endl;
    
    // 测试用例4 - 边界情况：空字符串
    string s4 = "";
    bool expected4 = true;
    cout << "测试用例4（空字符串）:" << endl;
    cout << "输入: \"" << s4 << "\"" << endl;
    cout << "解法一结果: " << (Solution::isPalindrome(s4) ? "true" : "false") << endl;
    cout << "解法二结果: " << (Solution::isPalindromeString(s4) ? "true" : "false") << endl;
    cout << "解法三结果: " << (Solution::isPalindromeOptimized(s4) ? "true" : "false") << endl;
    cout << "期望: " << (expected4 ? "true" : "false") << endl;
    cout << endl;
    
    // 测试用例5 - 边界情况：纯数字
    string s5 = "12321";
    bool expected5 = true;
    cout << "测试用例5（纯数字）:" << endl;
    cout << "输入: \"" << s5 << "\"" << endl;
    cout << "解法一结果: " << (Solution::isPalindrome(s5) ? "true" : "false") << endl;
    cout << "解法二结果: " << (Solution::isPalindromeString(s5) ? "true" : "false") << endl;
    cout << "解法三结果: " << (Solution::isPalindromeOptimized(s5) ? "true" : "false") << endl;
    cout << "期望: " << (expected5 ? "true" : "false") << endl;
    cout << endl;
    
    // 测试用例6 - 边界情况：混合字符
    string s6 = "0P";
    bool expected6 = false;
    cout << "测试用例6（混合字符）:" << endl;
    cout << "输入: \"" << s6 << "\"" << endl;
    cout << "解法一结果: " << (Solution::isPalindrome(s6) ? "true" : "false") << endl;
    cout << "解法二结果: " << (Solution::isPalindromeString(s6) ? "true" : "false") << endl;
    cout << "解法三结果: " << (Solution::isPalindromeOptimized(s6) ? "true" : "false") << endl;
    cout << "期望: " << (expected6 ? "true" : "false") << endl;
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    // 创建长字符串进行性能测试
    string longString;
    for (int i = 0; i < 100000; i++) {
        if (i % 3 == 0) {
            longString += "!@#";
        } else if (i % 3 == 1) {
            longString += "abc";
        } else {
            longString += "123";
        }
    }
    
    // 测试解法一的性能
    auto start = chrono::high_resolution_clock::now();
    bool result1 = Solution::isPalindrome(longString);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "解法一（双指针）耗时: " << duration1 << "ms, 结果: " << (result1 ? "true" : "false") << endl;
    
    // 测试解法二的性能
    start = chrono::high_resolution_clock::now();
    bool result2 = Solution::isPalindromeString(longString);
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "解法二（字符串反转）耗时: " << duration2 << "ms, 结果: " << (result2 ? "true" : "false") << endl;
    
    // 测试解法三的性能
    start = chrono::high_resolution_clock::now();
    bool result3 = Solution::isPalindromeOptimized(longString);
    end = chrono::high_resolution_clock::now();
    auto duration3 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "解法三（优化双指针）耗时: " << duration3 << "ms, 结果: " << (result3 ? "true" : "false") << endl;
    
    // 验证结果一致性
    cout << "所有解法结果一致: " << (result1 == result2 && result2 == result3 ? "true" : "false") << endl;
}

/**
 * 边界条件测试函数
 */
void boundaryTest() {
    // 测试极端长字符串
    string extremeString(1000000, 'a');
    
    auto start = chrono::high_resolution_clock::now();
    bool result = Solution::isPalindrome(extremeString);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "极端长字符串测试耗时: " << duration << "ms, 结果: " << (result ? "true" : "false") << endl;
}

/**
 * 算法分析函数
 */
void algorithmAnalysis() {
    cout << "=== 算法分析 ===" << endl;
    cout << "1. 解法一（双指针）" << endl;
    cout << "   - 时间复杂度: O(n) - 每个字符最多被访问一次" << endl;
    cout << "   - 空间复杂度: O(1) - 只使用常数级别的额外空间" << endl;
    cout << "   - 优点: 原地操作，空间效率高" << endl;
    cout << "   - 缺点: 需要处理字符过滤逻辑" << endl;
    cout << endl;
    
    cout << "2. 解法二（字符串反转）" << endl;
    cout << "   - 时间复杂度: O(n) - 需要遍历字符串两次" << endl;
    cout << "   - 空间复杂度: O(n) - 需要额外的字符串存储空间" << endl;
    cout << "   - 优点: 实现简单，易于理解" << endl;
    cout << "   - 缺点: 空间效率较低" << endl;
    cout << endl;
    
    cout << "3. 解法三（优化双指针）" << endl;
    cout << "   - 时间复杂度: O(n)" << endl;
    cout << "   - 空间复杂度: O(1)" << endl;
    cout << "   - 优点: 避免重复字符检查，效率最高" << endl;
    cout << "   - 缺点: 实现相对复杂" << endl;
    cout << endl;
    
    cout << "推荐使用解法一作为通用解决方案" << endl;
}

int main() {
    cout << "=== 验证回文串 算法实现 ===" << endl;
    cout << endl;
    
    cout << "=== 测试用例 ===" << endl;
    test();
    
    cout << "=== 性能测试 ===" << endl;
    performanceTest();
    
    cout << "=== 边界条件测试 ===" << endl;
    boundaryTest();
    
    cout << "=== 算法分析 ===" << endl;
    algorithmAnalysis();
    
    return 0;
}