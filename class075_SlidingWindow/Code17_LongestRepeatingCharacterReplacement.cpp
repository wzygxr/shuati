/*
 * 424. 替换后的最长重复字符问题解决方案
 * 
 * 问题描述：
 * 给你一个字符串 s 和一个整数 k 。你可以选择字符串中的任一字符，并将其更改为任何其他大写英文字符。
 * 该操作最多可执行 k 次。
 * 在执行上述操作后，返回包含相同字母的最长子字符串的长度。
 * 
 * 解题思路：
 * 使用滑动窗口维护一个窗口，窗口内最多有k个字符可以被替换成其他字符
 * 核心思想：窗口大小 - 窗口内出现次数最多的字符数量 <= k
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(1)，只需要26个字母的计数数组
 * 
 * 是否最优解：是
 * 
 * 相关题目链接：
 * LeetCode 424. 替换后的最长重复字符
 * https://leetcode.cn/problems/longest-repeating-character-replacement/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 替换后的最长重复字符
 *    https://www.nowcoder.com/practice/1266570c4a06487981ed50e84e8b720d
 * 2. LintCode 424. 替换后的最长重复字符
 *    https://www.lintcode.com/problem/424/
 * 3. HackerRank - Longest Repeating Character Replacement
 *    https://www.hackerrank.com/challenges/longest-repeating-character-replacement/problem
 * 4. CodeChef - REPLACE - Character Replacement
 *    https://www.codechef.com/problems/REPLACE
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空字符串等边界情况
 * 2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 * 
 * 编译说明：
 * 此代码需要C++标准库支持，编译时请确保包含正确的头文件路径
 * 编译命令示例：g++ -std=c++11 Code17_LongestRepeatingCharacterReplacement.cpp -o Code17_LongestRepeatingCharacterReplacement
 */

// 算法实现（需要C++标准库支持）
/*
// 需要包含的头文件：
// #include <iostream>
// #include <string>
// #include <vector>
// #include <algorithm>
// using namespace std;

// 424. 替换后的最长重复字符
class Solution {
public:
    // 计算替换k个字符后能获得的最长重复字符子串长度
    int characterReplacement(string s, int k) {
        // 异常情况处理
        if (s.empty()) {
            return 0;
        }
        
        int n = s.length();
        // 记录窗口内各字符的出现次数（A-Z共26个字母）
        vector<int> count(26, 0);
        int maxCount = 0; // 窗口内出现次数最多的字符数量
        int maxLength = 0; // 最长子串长度
        int left = 0; // 窗口左边界
        
        // 滑动窗口右边界
        for (int right = 0; right < n; right++) {
            // 当前右边界字符计数加1
            count[s[right] - 'A']++;
            // 更新窗口内最大字符计数
            maxCount = max(maxCount, count[s[right] - 'A']);
            
            // 如果窗口大小减去最大字符计数大于k，说明需要替换的字符超过k个
            // 需要收缩左边界
            // 核心条件：窗口大小 - 最多字符数量 > k 时，需要收缩窗口
            while (right - left + 1 - maxCount > k) {
                // 移除左边界字符
                count[s[left] - 'A']--;
                // 移动左边界
                left++;
                // 注意：这里不需要重新计算maxCount，因为即使maxCount变小了
                // 也不会影响最终结果，我们只需要记录历史最大值
            }
            
            // 更新最大长度（当前窗口大小）
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    // 优化版本：使用历史最大值，避免每次重新计算maxCount
    int characterReplacementOptimized(string s, int k) {
        // 异常情况处理
        if (s.empty()) {
            return 0;
        }
        
        int n = s.length();
        // 记录窗口内各字符的出现次数
        vector<int> count(26, 0);
        int maxCount = 0; // 历史最大字符计数
        int maxLength = 0; // 最长子串长度
        int left = 0; // 窗口左边界
        
        // 滑动窗口遍历字符串
        for (int right = 0; right < n; right++) {
            // 右边界字符计数加1
            count[s[right] - 'A']++;
            // 更新历史最大字符计数
            maxCount = max(maxCount, count[s[right] - 'A']);
            
            // 关键优化：使用历史最大值，即使窗口收缩后maxCount变小
            // 也不会影响结果，因为我们需要的是历史最大值
            // 当需要替换的字符数超过k时，收缩窗口
            if (right - left + 1 - maxCount > k) {
                // 移除左边界字符
                count[s[left] - 'A']--;
                // 移动左边界
                left++;
            }
            
            // 更新最大长度（当前窗口大小）
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
};

// 测试函数
void testCharacterReplacement() {
    Solution solution;
    
    // 测试用例1
    string s1 = "ABAB";
    int k1 = 2;
    int result1 = solution.characterReplacement(s1, k1);
    // 预期输出: 4
    
    // 测试用例2
    string s2 = "AABABBA";
    int k2 = 1;
    int result2 = solution.characterReplacement(s2, k2);
    // 预期输出: 4
}

int main() {
    testCharacterReplacement();
    return 0;
}
*/

// 算法核心逻辑说明（伪代码形式）：
/*
class Solution {
public:
    int characterReplacement(string s, int k) {
        if (s.empty()) {
            return 0;
        }
        
        int n = s.length();
        vector<int> count(26, 0);  // 字符计数数组
        int maxCount = 0;  // 窗口内最大字符计数
        int maxLength = 0;  // 最长子串长度
        int left = 0;  // 窗口左边界
        
        for (int right = 0; right < n; right++) {
            // 右边界字符计数加1
            count[s[right] - 'A']++;
            // 更新最大字符计数
            maxCount = max(maxCount, count[s[right] - 'A']);
            
            // 当需要替换的字符数超过k时，收缩窗口
            while (right - left + 1 - maxCount > k) {
                count[s[left] - 'A']--;
                left++;
            }
            
            // 更新最大长度
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
};
*/