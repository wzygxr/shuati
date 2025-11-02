/**
 * LeetCode 28 - 实现 strStr()
 * 题目链接：https://leetcode.com/problems/implement-strstr/
 * 
 * 题目描述：
 * 实现 strStr() 函数。
 * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。
 * 如果不存在，则返回 -1。
 * 
 * 示例 1:
 * 输入: haystack = "hello", needle = "ll"
 * 输出: 2
 * 
 * 示例 2:
 * 输入: haystack = "aaaaa", needle = "bba"
 * 输出: -1
 * 
 * 说明:
 * 当 needle 是空字符串时，我们应当返回什么值呢？这是一个在面试中很好的问题。
 * 对于本题而言，当 needle 是空字符串时我们应当返回 0。
 * 
 * 时间复杂度：
 * - 最好情况：O(n/m) 当模式串在文本串开头就匹配时
 * - 最坏情况：O(n*m) 当模式串和文本串有很多相似字符时
 * - 平均情况：O(n) 对于随机文本
 * 
 * 空间复杂度：O(m) 用于存储坏字符表和好后缀表
 * 
 * 工程化考量：
 * 1. 边界条件处理：空字符串、模式串比文本串长等情况
 * 2. 异常处理：输入参数为null的情况
 * 3. 性能优化：使用Boyer-Moore算法提高匹配效率
 * 4. 可读性：清晰的变量命名和注释
 */

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <stdexcept>

using namespace std;

class Solution {
public:
    /**
     * 使用Boyer-Moore算法实现strStr函数
     * 
     * @param haystack 文本串
     * @param needle 模式串
     * @return 模式串在文本串中首次出现的索引，不存在则返回-1
     */
    int strStr(string haystack, string needle) {
        // 边界条件检查
        if (haystack.empty() && needle.empty()) {
            return 0;
        }
        
        int n = haystack.length();
        int m = needle.length();
        
        // 处理空模式串的情况
        if (m == 0) {
            return 0;
        }
        
        // 文本串比模式串短，不可能匹配
        if (n < m) {
            return -1;
        }
        
        // 构建坏字符表
        vector<int> badChar = buildBadCharTable(needle);
        
        // 构建好后缀表
        vector<int> goodSuffix = buildGoodSuffixTable(needle);
        
        // 开始Boyer-Moore匹配
        int i = 0; // 文本串中的起始位置
        while (i <= n - m) {
            int j = m - 1; // 从模式串末尾开始匹配
            
            // 从右向左匹配字符
            while (j >= 0 && needle[j] == haystack[i + j]) {
                j--;
            }
            
            // 完全匹配
            if (j < 0) {
                return i;
            }
            
            // 计算移动距离：取坏字符规则和好后缀规则中的较大值
            int badCharShift = max(1, j - badChar[haystack[i + j]]);
            int goodSuffixShift = goodSuffix[j + 1];
            
            // 移动较大的距离
            i += max(badCharShift, goodSuffixShift);
        }
        
        return -1;
    }

private:
    /**
     * 构建坏字符表
     * 坏字符规则：当发生不匹配时，根据文本串中不匹配的字符在模式串中最右出现的位置来决定移动距离
     * 
     * @param pattern 模式串
     * @return 坏字符表
     */
    vector<int> buildBadCharTable(const string& pattern) {
        vector<int> table(256, -1); // ASCII字符集
        
        // 记录每个字符在模式串中最后出现的位置
        for (int i = 0; i < pattern.length(); i++) {
            table[pattern[i]] = i;
        }
        
        return table;
    }
    
    /**
     * 构建好后缀表
     * 好后缀规则：当发生不匹配时，根据已经匹配的后缀来决定移动距离
     * 
     * @param pattern 模式串
     * @return 好后缀表
     */
    vector<int> buildGoodSuffixTable(const string& pattern) {
        int m = pattern.length();
        vector<int> suffix(m + 1, -1);
        vector<int> goodSuffix(m + 1, m);
        
        // 计算suffix数组
        for (int i = m - 1; i >= 0; i--) {
            int j = i;
            while (j >= 0 && pattern[j] == pattern[m - 1 - i + j]) {
                j--;
            }
            suffix[i] = i - j;
        }
        
        // 计算好后缀移动距离
        for (int i = m - 1; i >= 0; i--) {
            if (suffix[i] == i + 1) {
                for (int j = 0; j < m - i - 1; j++) {
                    if (goodSuffix[j] == m) {
                        goodSuffix[j] = m - i - 1;
                    }
                }
            }
        }
        
        for (int i = 0; i <= m - 1; i++) {
            goodSuffix[m - 1 - suffix[i]] = min(goodSuffix[m - 1 - suffix[i]], m - 1 - i);
        }
        
        return goodSuffix;
    }
};

/**
 * 单元测试函数
 */
void runTests() {
    Solution solution;
    
    // 测试用例1：基本匹配
    cout << "测试1: " << solution.strStr("hello", "ll") << " (期望: 2)" << endl;
    
    // 测试用例2：不匹配
    cout << "测试2: " << solution.strStr("aaaaa", "bba") << " (期望: -1)" << endl;
    
    // 测试用例3：空模式串
    cout << "测试3: " << solution.strStr("hello", "") << " (期望: 0)" << endl;
    
    // 测试用例4：模式串在开头
    cout << "测试4: " << solution.strStr("hello", "he") << " (期望: 0)" << endl;
    
    // 测试用例5：模式串在末尾
    cout << "测试5: " << solution.strStr("hello", "lo") << " (期望: 3)" << endl;
    
    // 测试用例6：长文本串
    cout << "测试6: " << solution.strStr("mississippi", "issip") << " (期望: 4)" << endl;
    
    // 测试用例7：边界情况 - 两个空字符串
    cout << "测试7: " << solution.strStr("", "") << " (期望: 0)" << endl;
    
    cout << "所有测试完成！" << endl;
}

int main() {
    try {
        runTests();
    } catch (const exception& e) {
        cerr << "测试过程中发生异常: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}