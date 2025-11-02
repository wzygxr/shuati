#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

/**
 * 1208. 尽可能使字符串相等
 * 给你两个长度相同的字符串，s 和 t。
 * 将 s 中的第 i 个字符变到 t 中的第 i 个字符需要 |s[i] - t[i]| 的开销（开销可能为 0），也就是两个字符的 ASCII 码值的差的绝对值。
 * 用于变更字符串的最大预算是 maxCost。在转化字符串时，总开销应当小于等于该预算，这也意味着字符串的转化可能是不完全的。
 * 如果你可以将 s 的子字符串转化为它在 t 中对应的子字符串，则返回可以转化的最大长度。
 * 如果 s 中没有子字符串可以转化成 t 中对应的子字符串，则返回 0。
 * 
 * 解题思路：
 * 使用滑动窗口维护一个子数组，使得子数组内字符转换的开销总和不超过maxCost
 * 当开销超过maxCost时，收缩左边界
 * 在滑动过程中记录最大窗口大小
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(1)
 * 
 * 是否最优解：是
 * 
 * 测试链接：https://leetcode.cn/problems/get-equal-substrings-within-budget/
 */
class Solution {
public:
    /**
     * 计算可以转化的最大子字符串长度
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @param maxCost 最大预算
     * @return 可以转化的最大长度
     */
    int equalSubstring(string s, string t, int maxCost) {
        int n = s.length();
        int maxLength = 0; // 最大长度
        int currentCost = 0; // 当前窗口的开销
        int left = 0; // 窗口左边界
        
        // 滑动窗口右边界
        for (int right = 0; right < n; right++) {
            // 计算当前字符的转换开销
            int cost = abs(s[right] - t[right]);
            currentCost += cost;
            
            // 如果当前开销超过最大预算，收缩左边界
            while (currentCost > maxCost) {
                int leftCost = abs(s[left] - t[left]);
                currentCost -= leftCost;
                left++;
            }
            
            // 更新最大长度
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 优化版本：使用数组预先计算开销
     * 时间复杂度：O(n)，空间复杂度：O(n)
     */
    int equalSubstringOptimized(string s, string t, int maxCost) {
        int n = s.length();
        if (n == 0) return 0;
        
        // 预先计算每个位置的转换开销
        vector<int> costs(n);
        for (int i = 0; i < n; i++) {
            costs[i] = abs(s[i] - t[i]);
        }
        
        int maxLength = 0;
        int currentCost = 0;
        int left = 0;
        
        for (int right = 0; right < n; right++) {
            currentCost += costs[right];
            
            // 如果当前开销超过最大预算，收缩左边界
            while (currentCost > maxCost) {
                currentCost -= costs[left];
                left++;
            }
            
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 另一种思路：使用双指针，不显式维护currentCost
     * 时间复杂度：O(n)，空间复杂度：O(1)
     */
    int equalSubstringAlternative(string s, string t, int maxCost) {
        int n = s.length();
        int maxLength = 0;
        int left = 0;
        int right = 0;
        int currentCost = 0;
        
        while (right < n) {
            // 扩展右边界
            int cost = abs(s[right] - t[right]);
            currentCost += cost;
            right++;
            
            // 如果开销超过最大预算，收缩左边界
            while (currentCost > maxCost) {
                int leftCost = abs(s[left] - t[left]);
                currentCost -= leftCost;
                left++;
            }
            
            // 更新最大长度
            maxLength = max(maxLength, right - left);
        }
        
        return maxLength;
    }
    
    /**
     * 使用前缀和思想（当maxCost较大时效率更高）
     * 时间复杂度：O(n)，空间复杂度：O(n)
     */
    int equalSubstringWithPrefixSum(string s, string t, int maxCost) {
        int n = s.length();
        if (n == 0) return 0;
        
        // 计算前缀和数组
        vector<int> prefixSum(n + 1, 0);
        for (int i = 0; i < n; i++) {
            int cost = abs(s[i] - t[i]);
            prefixSum[i + 1] = prefixSum[i] + cost;
        }
        
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < n; right++) {
            // 计算从left到right的开销
            int currentCost = prefixSum[right + 1] - prefixSum[left];
            
            // 如果开销不超过最大预算，更新最大长度
            if (currentCost <= maxCost) {
                maxLength = max(maxLength, right - left + 1);
            } else {
                // 开销超过预算，移动左边界
                left++;
            }
        }
        
        return maxLength;
    }
};

// 测试函数
void testEqualSubstring() {
    Solution solution;
    
    // 测试用例1
    string s1 = "abcd";
    string t1 = "bcdf";
    int maxCost1 = 3;
    int result1 = solution.equalSubstring(s1, t1, maxCost1);
    cout << "s = \"" << s1 << "\", t = \"" << t1 << "\", maxCost = " << maxCost1 << endl;
    cout << "最大长度: " << result1 << endl;
    cout << "预期: 3" << endl;
    cout << endl;
    
    // 测试用例2
    string s2 = "abcd";
    string t2 = "cdef";
    int maxCost2 = 3;
    int result2 = solution.equalSubstring(s2, t2, maxCost2);
    cout << "s = \"" << s2 << "\", t = \"" << t2 << "\", maxCost = " << maxCost2 << endl;
    cout << "最大长度: " << result2 << endl;
    cout << "预期: 1" << endl;
    cout << endl;
    
    // 测试用例3
    string s3 = "abcd";
    string t3 = "acde";
    int maxCost3 = 0;
    int result3 = solution.equalSubstring(s3, t3, maxCost3);
    cout << "s = \"" << s3 << "\", t = \"" << t3 << "\", maxCost = " << maxCost3 << endl;
    cout << "最大长度: " << result3 << endl;
    cout << "预期: 1" << endl;
    cout << endl;
    
    // 测试用例4：相同字符串
    string s4 = "abcd";
    string t4 = "abcd";
    int maxCost4 = 10;
    int result4 = solution.equalSubstring(s4, t4, maxCost4);
    cout << "s = \"" << s4 << "\", t = \"" << t4 << "\", maxCost = " << maxCost4 << endl;
    cout << "最大长度: " << result4 << endl;
    cout << "预期: 4" << endl;
    cout << endl;
    
    // 测试用例5：空字符串
    string s5 = "";
    string t5 = "";
    int maxCost5 = 10;
    int result5 = solution.equalSubstring(s5, t5, maxCost5);
    cout << "s = \"" << s5 << "\", t = \"" << t5 << "\", maxCost = " << maxCost5 << endl;
    cout << "最大长度: " << result5 << endl;
    cout << "预期: 0" << endl;
    cout << endl;
    
    // 测试用例6：边界情况，单个字符
    string s6 = "a";
    string t6 = "b";
    int maxCost6 = 1;
    int result6 = solution.equalSubstring(s6, t6, maxCost6);
    cout << "s = \"" << s6 << "\", t = \"" << t6 << "\", maxCost = " << maxCost6 << endl;
    cout << "最大长度: " << result6 << endl;
    cout << "预期: 1" << endl;
}

int main() {
    testEqualSubstring();
    return 0;
}