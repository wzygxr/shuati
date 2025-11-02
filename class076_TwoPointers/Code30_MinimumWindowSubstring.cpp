#include <iostream>
#include <string>
#include <unordered_map>
#include <vector>
#include <algorithm>
#include <climits>
#include <chrono>
using namespace std;

/**
 * LeetCode 76. 最小覆盖子串 (Minimum Window Substring)
 * 
 * 题目描述:
 * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
 * 
 * 注意：
 * - 对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量。
 * - 如果 s 中存在这样的子串，我们保证它是唯一的答案。
 * 
 * 示例1:
 * 输入: s = "ADOBECODEBANC", t = "ABC"
 * 输出: "BANC"
 * 解释: 最小覆盖子串 "BANC" 包含 'A', 'B', 'C' 各一个。
 * 
 * 示例2:
 * 输入: s = "a", t = "a"
 * 输出: "a"
 * 
 * 示例3:
 * 输入: s = "a", t = "aa"
 * 输出: ""
 * 解释: t 中有两个 'a'，但 s 中只有一个 'a'，所以返回空字符串。
 * 
 * 提示:
 * - 1 <= s.length, t.length <= 10^5
 * - s 和 t 由英文字母组成
 * 
 * 题目链接: https://leetcode.cn/problems/minimum-window-substring/
 * 
 * 解题思路:
 * 这道题可以使用滑动窗口（双指针）的方法来解决：
 * 
 * 方法一（滑动窗口 + unordered_map）：
 * 1. 使用两个指针 left 和 right 表示当前窗口的左右边界
 * 2. 使用unordered_map记录 t 中每个字符的出现次数
 * 3. 使用另一个unordered_map记录当前窗口中包含 t 字符的情况
 * 4. 移动右指针扩展窗口，直到窗口包含 t 的所有字符
 * 5. 然后移动左指针收缩窗口，找到最小覆盖子串
 * 
 * 时间复杂度: O(n + m)，n为s长度，m为t长度
 * 空间复杂度: O(m)，存储t的字符频率
 * 是否最优解：是
 */

class Solution {
public:
    /**
     * 解法一: 滑动窗口 + unordered_map（最优解）
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最小覆盖子串
     */
    static string minWindow(const string& s, const string& t) {
        if (s.empty() || t.empty() || s.length() < t.length()) {
            return "";
        }
        
        // 记录t中每个字符的出现次数
        unordered_map<char, int> targetMap;
        for (char c : t) {
            targetMap[c]++;
        }
        
        // 记录当前窗口中字符的出现次数
        unordered_map<char, int> windowMap;
        
        int left = 0, right = 0;
        int minLen = INT_MAX;
        int minStart = 0;
        int required = targetMap.size(); // 需要匹配的字符种类数
        int formed = 0; // 当前窗口中已匹配的字符种类数
        
        while (right < s.length()) {
            char rightChar = s[right];
            windowMap[rightChar]++;
            
            // 如果当前字符在t中，且窗口中出现次数等于t中出现次数
            if (targetMap.find(rightChar) != targetMap.end() && 
                windowMap[rightChar] == targetMap[rightChar]) {
                formed++;
            }
            
            // 当窗口包含t的所有字符时，尝试收缩窗口
            while (left <= right && formed == required) {
                char leftChar = s[left];
                
                // 更新最小覆盖子串
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minStart = left;
                }
                
                // 移动左指针
                windowMap[leftChar]--;
                if (targetMap.find(leftChar) != targetMap.end() && 
                    windowMap[leftChar] < targetMap[leftChar]) {
                    formed--;
                }
                left++;
            }
            
            right++;
        }
        
        return minLen == INT_MAX ? "" : s.substr(minStart, minLen);
    }
    
    /**
     * 解法二: 优化版滑动窗口（使用数组替代unordered_map）
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最小覆盖子串
     */
    static string minWindowOptimized(const string& s, const string& t) {
        if (s.empty() || t.empty() || s.length() < t.length()) {
            return "";
        }
        
        // 使用数组记录字符频率（假设字符为ASCII）
        vector<int> targetFreq(128, 0);
        vector<int> windowFreq(128, 0);
        
        for (char c : t) {
            targetFreq[c]++;
        }
        
        int left = 0, right = 0;
        int minLen = INT_MAX;
        int minStart = 0;
        int required = 0;
        
        // 计算需要匹配的字符种类数
        for (int freq : targetFreq) {
            if (freq > 0) {
                required++;
            }
        }
        
        int formed = 0;
        
        while (right < s.length()) {
            char rightChar = s[right];
            windowFreq[rightChar]++;
            
            // 如果当前字符在t中，且窗口中出现次数等于t中出现次数
            if (targetFreq[rightChar] > 0 && 
                windowFreq[rightChar] == targetFreq[rightChar]) {
                formed++;
            }
            
            // 当窗口包含t的所有字符时，尝试收缩窗口
            while (left <= right && formed == required) {
                char leftChar = s[left];
                
                // 更新最小覆盖子串
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minStart = left;
                }
                
                // 移动左指针
                windowFreq[leftChar]--;
                if (targetFreq[leftChar] > 0 && 
                    windowFreq[leftChar] < targetFreq[leftChar]) {
                    formed--;
                }
                left++;
            }
            
            right++;
        }
        
        return minLen == INT_MAX ? "" : s.substr(minStart, minLen);
    }
    
    /**
     * 解法三: 进一步优化的滑动窗口（跳过无关字符）
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最小覆盖子串
     */
    static string minWindowAdvanced(const string& s, const string& t) {
        if (s.empty() || t.empty() || s.length() < t.length()) {
            return "";
        }
        
        // 使用数组记录字符频率
        vector<int> targetFreq(128, 0);
        for (char c : t) {
            targetFreq[c]++;
        }
        
        // 预处理：只保留s中在t中出现的字符及其位置
        int count = t.length();
        int left = 0, right = 0;
        int minLen = INT_MAX;
        int minStart = 0;
        
        while (right < s.length()) {
            // 如果当前字符在t中，减少计数
            if (targetFreq[s[right]] > 0) {
                count--;
            }
            targetFreq[s[right]]--;
            right++;
            
            // 当计数为0时，表示窗口包含t的所有字符
            while (count == 0) {
                // 更新最小覆盖子串
                if (right - left < minLen) {
                    minLen = right - left;
                    minStart = left;
                }
                
                // 移动左指针
                targetFreq[s[left]]++;
                if (targetFreq[s[left]] > 0) {
                    count++;
                }
                left++;
            }
        }
        
        return minLen == INT_MAX ? "" : s.substr(minStart, minLen);
    }
};

/**
 * 测试函数
 */
void test() {
    // 测试用例1
    string s1 = "ADOBECODEBANC";
    string t1 = "ABC";
    string expected1 = "BANC";
    cout << "测试用例1:" << endl;
    cout << "s = \"" << s1 << "\", t = \"" << t1 << "\"" << endl;
    cout << "解法一结果: \"" << Solution::minWindow(s1, t1) << "\"" << endl;
    cout << "解法二结果: \"" << Solution::minWindowOptimized(s1, t1) << "\"" << endl;
    cout << "解法三结果: \"" << Solution::minWindowAdvanced(s1, t1) << "\"" << endl;
    cout << "期望: \"" << expected1 << "\"" << endl;
    cout << endl;
    
    // 测试用例2
    string s2 = "a";
    string t2 = "a";
    string expected2 = "a";
    cout << "测试用例2:" << endl;
    cout << "s = \"" << s2 << "\", t = \"" << t2 << "\"" << endl;
    cout << "解法一结果: \"" << Solution::minWindow(s2, t2) << "\"" << endl;
    cout << "解法二结果: \"" << Solution::minWindowOptimized(s2, t2) << "\"" << endl;
    cout << "解法三结果: \"" << Solution::minWindowAdvanced(s2, t2) << "\"" << endl;
    cout << "期望: \"" << expected2 << "\"" << endl;
    cout << endl;
    
    // 测试用例3
    string s3 = "a";
    string t3 = "aa";
    string expected3 = "";
    cout << "测试用例3:" << endl;
    cout << "s = \"" << s3 << "\", t = \"" << t3 << "\"" << endl;
    cout << "解法一结果: \"" << Solution::minWindow(s3, t3) << "\"" << endl;
    cout << "解法二结果: \"" << Solution::minWindowOptimized(s3, t3) << "\"" << endl;
    cout << "解法三结果: \"" << Solution::minWindowAdvanced(s3, t3) << "\"" << endl;
    cout << "期望: \"" << expected3 << "\"" << endl;
    cout << endl;
    
    // 测试用例4 - 边界情况：s和t相同
    string s4 = "abc";
    string t4 = "abc";
    string expected4 = "abc";
    cout << "测试用例4（s和t相同）:" << endl;
    cout << "s = \"" << s4 << "\", t = \"" << t4 << "\"" << endl;
    cout << "解法一结果: \"" << Solution::minWindow(s4, t4) << "\"" << endl;
    cout << "解法二结果: \"" << Solution::minWindowOptimized(s4, t4) << "\"" << endl;
    cout << "解法三结果: \"" << Solution::minWindowAdvanced(s4, t4) << "\"" << endl;
    cout << "期望: \"" << expected4 << "\"" << endl;
    cout << endl;
    
    // 测试用例5 - 边界情况：t不在s中
    string s5 = "abcdef";
    string t5 = "xyz";
    string expected5 = "";
    cout << "测试用例5（t不在s中）:" << endl;
    cout << "s = \"" << s5 << "\", t = \"" << t5 << "\"" << endl;
    cout << "解法一结果: \"" << Solution::minWindow(s5, t5) << "\"" << endl;
    cout << "解法二结果: \"" << Solution::minWindowOptimized(s5, t5) << "\"" << endl;
    cout << "解法三结果: \"" << Solution::minWindowAdvanced(s5, t5) << "\"" << endl;
    cout << "期望: \"" << expected5 << "\"" << endl;
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    // 创建长字符串进行性能测试
    string longS;
    for (int i = 0; i < 10000; i++) {
        longS += "ABCDEFG";
    }
    string longT = "ABC";
    
    // 测试解法一的性能
    auto start = chrono::high_resolution_clock::now();
    string result1 = Solution::minWindow(longS, longT);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "解法一（unordered_map）耗时: " << duration1 << "ms, 结果长度: " << result1.length() << endl;
    
    // 测试解法二的性能
    start = chrono::high_resolution_clock::now();
    string result2 = Solution::minWindowOptimized(longS, longT);
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "解法二（数组优化）耗时: " << duration2 << "ms, 结果长度: " << result2.length() << endl;
    
    // 测试解法三的性能
    start = chrono::high_resolution_clock::now();
    string result3 = Solution::minWindowAdvanced(longS, longT);
    end = chrono::high_resolution_clock::now();
    auto duration3 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "解法三（高级优化）耗时: " << duration3 << "ms, 结果长度: " << result3.length() << endl;
    
    // 验证结果一致性
    cout << "所有解法结果一致: " << (result1 == result2 && result2 == result3 ? "true" : "false") << endl;
}

/**
 * 算法分析函数
 */
void algorithmAnalysis() {
    cout << "=== 算法分析 ===" << endl;
    cout << "1. 解法一（滑动窗口 + unordered_map）" << endl;
    cout << "   - 时间复杂度: O(n + m) - n为s长度，m为t长度" << endl;
    cout << "   - 空间复杂度: O(m) - 存储t的字符频率" << endl;
    cout << "   - 优点: 通用性强，适用于任意字符集" << endl;
    cout << "   - 缺点: unordered_map操作有一定开销" << endl;
    cout << endl;
    
    cout << "2. 解法二（数组优化版）" << endl;
    cout << "   - 时间复杂度: O(n + m)" << endl;
    cout << "   - 空间复杂度: O(1) - 固定大小的数组" << endl;
    cout << "   - 优点: 效率高，适用于ASCII字符集" << endl;
    cout << "   - 缺点: 仅适用于有限字符集" << endl;
    cout << endl;
    
    cout << "3. 解法三（进一步优化）" << endl;
    cout << "   - 时间复杂度: O(n)" << endl;
    cout << "   - 空间复杂度: O(1)" << endl;
    cout << "   - 优点: 最优化实现，跳过无关字符" << endl;
    cout << "   - 缺点: 实现相对复杂" << endl;
    cout << endl;
    
    cout << "推荐使用解法二作为通用解决方案" << endl;
}

int main() {
    cout << "=== 最小覆盖子串 算法实现 ===" << endl;
    cout << endl;
    
    cout << "=== 测试用例 ===" << endl;
    test();
    
    cout << "=== 性能测试 ===" << endl;
    performanceTest();
    
    cout << "=== 算法分析 ===" << endl;
    algorithmAnalysis();
    
    return 0;
}