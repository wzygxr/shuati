#include <iostream>
#include <string>
#include <algorithm>
#include <climits>
#include <vector>
#include <cctype>

using namespace std;

/**
 * 回文数判断问题 - C++版本
 * 
 * 问题描述：
 * 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * 
 * 算法思路：
 * 方法1：数学方法 - 通过位运算判断回文
 * 方法2：字符串方法 - 转换为字符串后判断
 * 
 * 时间复杂度分析：
 * - 数学方法：O(log n)，其中n为数字的位数
 * - 字符串方法：O(log n)，字符串转换和比较
 * 
 * 空间复杂度分析：
 * - 数学方法：O(1)，常数额外空间
 * - 字符串方法：O(log n)，需要存储字符串
 * 
 * 工程化考量：
 * 1. 边界处理：负数、0、边界值
 * 2. 溢出处理：反转时可能出现的溢出问题
 * 3. 性能优化：选择合适的算法策略
 * 4. 可测试性：设计全面的测试用例
 */

class PalindromeSolver {
public:
    /**
     * 方法1：数学方法
     * 通过数学运算反转数字，避免字符串转换
     */
    bool isPalindromeMath(int x) {
        // 边界情况处理
        if (x < 0) return false;  // 负数不是回文数
        if (x < 10) return true; // 单个数字是回文数
        if (x % 10 == 0) return false; // 以0结尾的数字不是回文数（除了0本身）
        
        int original = x;
        long reversed = 0; // 使用long防止溢出
        
        while (x > 0) {
            reversed = reversed * 10 + x % 10;
            x /= 10;
        }
        
        return original == reversed;
    }
    
    /**
     * 方法2：优化数学方法
     * 只反转一半数字，避免完全反转可能导致的溢出问题
     */
    bool isPalindromeOptimized(int x) {
        // 边界情况处理
        if (x < 0) return false;
        if (x < 10) return true;
        if (x % 10 == 0) return false;
        
        int reversed = 0;
        
        // 当原始数字大于反转数字时继续
        while (x > reversed) {
            reversed = reversed * 10 + x % 10;
            x /= 10;
        }
        
        // 数字长度为奇数时，通过reversed/10去除中间位
        return x == reversed || x == reversed / 10;
    }
    
    /**
     * 方法3：字符串方法
     * 转换为字符串后判断回文
     */
    bool isPalindromeString(int x) {
        if (x < 0) return false;
        
        string s = to_string(x);
        int left = 0, right = s.length() - 1;
        
        while (left < right) {
            if (s[left] != s[right]) {
                return false;
            }
            left++;
            right--;
        }
        
        return true;
    }
    
    /**
     * 方法4：递归方法
     * 使用递归判断回文数
     */
    bool isPalindromeRecursive(int x) {
        if (x < 0) return false;
        if (x < 10) return true;
        
        // 获取数字的位数
        int digits = 0;
        int temp = x;
        while (temp > 0) {
            digits++;
            temp /= 10;
        }
        
        return isPalindromeRecursiveHelper(x, digits);
    }
    
private:
    bool isPalindromeRecursiveHelper(int x, int digits) {
        if (digits <= 1) return true;
        
        // 获取最高位和最低位
        int power = 1;
        for (int i = 1; i < digits; i++) {
            power *= 10;
        }
        
        int firstDigit = x / power;
        int lastDigit = x % 10;
        
        if (firstDigit != lastDigit) {
            return false;
        }
        
        // 去掉最高位和最低位
        int remaining = (x % power) / 10;
        
        return isPalindromeRecursiveHelper(remaining, digits - 2);
    }
};

/**
 * 补充训练题目 - C++实现
 */

/**
 * LeetCode 125. 验证回文串
 * 给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写
 */
bool isPalindromeString(string s) {
    int left = 0, right = s.length() - 1;
    
    while (left < right) {
        // 跳过非字母数字字符
        while (left < right && !isalnum(s[left])) {
            left++;
        }
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
 * 辅助函数：判断字符串指定范围是否是回文
 */
bool isPalindromeRange(string s, int left, int right) {
    while (left < right) {
        if (s[left] != s[right]) {
            return false;
        }
        left++;
        right--;
    }
    return true;
}

/**
 * LeetCode 680. 验证回文字符串 II
 * 给定一个非空字符串s，最多删除一个字符，判断是否能成为回文字符串
 */
bool validPalindrome(string s) {
    int left = 0, right = s.length() - 1;
    
    while (left < right) {
        if (s[left] != s[right]) {
            // 尝试删除左边或右边的字符
            return isPalindromeRange(s, left + 1, right) || 
                   isPalindromeRange(s, left, right - 1);
        }
        left++;
        right--;
    }
    
    return true;
}

/**
 * 辅助函数：中心扩展法
 */
int expandAroundCenter(string s, int left, int right) {
    while (left >= 0 && right < s.length() && s[left] == s[right]) {
        left--;
        right++;
    }
    return right - left - 1;
}

/**
 * LeetCode 5. 最长回文子串
 * 使用中心扩展法找到最长回文子串
 */
string longestPalindrome(string s) {
    if (s.empty()) return "";
    
    int start = 0, end = 0;
    
    for (int i = 0; i < s.length(); i++) {
        // 奇数长度回文
        int len1 = expandAroundCenter(s, i, i);
        // 偶数长度回文
        int len2 = expandAroundCenter(s, i, i + 1);
        
        int len = (len1 > len2) ? len1 : len2;
        
        if (len > end - start) {
            start = i - (len - 1) / 2;
            end = i + len / 2;
        }
    }
    
    return s.substr(start, end - start + 1);
}

/**
 * 辅助函数：中心扩展法统计回文子串
 */
int expandAroundCenterCount(string s, int left, int right) {
    int count = 0;
    while (left >= 0 && right < s.length() && s[left] == s[right]) {
        count++;
        left--;
        right++;
    }
    return count;
}

/**
 * LeetCode 647. 回文子串
 * 统计字符串中的回文子串数目
 */
int countSubstrings(string s) {
    int count = 0;
    int n = s.length();
    
    for (int i = 0; i < n; i++) {
        // 奇数长度回文子串
        count += expandAroundCenterCount(s, i, i);
        // 偶数长度回文子串
        count += expandAroundCenterCount(s, i, i + 1);
    }
    
    return count;
}

/**
 * LeetCode 516. 最长回文子序列
 * 找出字符串中最长的回文子序列的长度
 */
int longestPalindromeSubseq(string s) {
    int n = s.length();
    // dp[i][j]表示s[i..j]的最长回文子序列长度
    vector<vector<int>> dp(n, vector<int>(n, 0));
    
    // 单个字符的回文子序列长度为1
    for (int i = 0; i < n; i++) {
        dp[i][i] = 1;
    }
    
    // 从短到长填充dp数组
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            if (s[i] == s[j]) {
                dp[i][j] = dp[i + 1][j - 1] + 2;
            } else {
                dp[i][j] = (dp[i + 1][j] > dp[i][j - 1]) ? dp[i + 1][j] : dp[i][j - 1];
            }
        }
    }
    
    return dp[0][n - 1];
}

/**
 * LeetCode 9. 回文数（简化版）
 * 不使用字符串转换的判断方法
 */
bool isPalindrome(int x) {
    if (x < 0) return false;
    if (x < 10) return true;
    if (x % 10 == 0) return false;
    
    int reversed = 0;
    while (x > reversed) {
        reversed = reversed * 10 + x % 10;
        x /= 10;
    }
    
    return x == reversed || x == reversed / 10;
}

// 测试函数
void testPalindrome() {
    PalindromeSolver solver;
    
    // 测试用例
    vector<int> testCases = {
        121, -121, 10, -101, 0, 1, 12321, 12345, 1001, 9999
    };
    
    cout << "回文数判断测试:" << endl;
    for (int num : testCases) {
        bool result1 = solver.isPalindromeMath(num);
        bool result2 = solver.isPalindromeOptimized(num);
        bool result3 = solver.isPalindromeString(num);
        bool result4 = solver.isPalindromeRecursive(num);
        
        cout << "数字: " << num << " -> ";
        cout << "数学方法: " << (result1 ? "是" : "否") << ", ";
        cout << "优化方法: " << (result2 ? "是" : "否") << ", ";
        cout << "字符串方法: " << (result3 ? "是" : "否") << ", ";
        cout << "递归方法: " << (result4 ? "是" : "否") << endl;
    }
    cout << endl;
    
    // 测试补充题目
    cout << "=== 补充训练题目测试 ===" << endl;
    
    // 测试回文串验证
    string testStr = "A man, a plan, a canal: Panama";
    cout << "回文串验证: \"" << testStr << "\" -> " << (isPalindromeString(testStr) ? "是" : "否") << endl;
    
    // 测试验证回文字符串II
    string testStr2 = "aba";
    string testStr3 = "abca";
    cout << "验证回文字符串II: \"" << testStr2 << "\" -> " << (validPalindrome(testStr2) ? "是" : "否") << endl;
    cout << "验证回文字符串II: \"" << testStr3 << "\" -> " << (validPalindrome(testStr3) ? "是" : "否") << endl;
    
    // 测试最长回文子串
    string testStr4 = "babad";
    cout << "最长回文子串: \"" << testStr4 << "\" -> \"" << longestPalindrome(testStr4) << "\"" << endl;
    
    // 测试回文子串统计
    string testStr5 = "abc";
    cout << "回文子串统计: \"" << testStr5 << "\" -> " << countSubstrings(testStr5) << endl;
    
    // 测试最长回文子序列
    string testStr6 = "bbbab";
    cout << "最长回文子序列: \"" << testStr6 << "\" -> " << longestPalindromeSubseq(testStr6) << endl;
}

int main() {
    testPalindrome();
    return 0;
}

/**
 * 算法技巧总结 - C++版本
 * 
 * 核心概念：
 * 1. 回文数判断技术：
 *    - 数学方法：通过数字反转判断回文
 *    - 优化方法：只反转一半数字，避免溢出
 *    - 字符串方法：转换为字符串后判断
 *    - 递归方法：递归判断首尾字符
 * 
 * 2. 算法选择策略：
 *    - 性能要求高：使用优化数学方法
 *    - 代码简洁：使用字符串方法
 *    - 教学演示：使用递归方法
 * 
 * 3. 边界情况处理：
 *    - 负数处理：负数不是回文数
 *    - 零处理：0是回文数
 *    - 溢出处理：使用long类型或优化方法
 * 
 * 调试技巧：
 * 1. 边界值测试：测试各种边界情况
 * 2. 性能分析：比较不同方法的执行时间
 * 3. 内存分析：检查内存使用情况
 * 
 * 工程化实践：
 * 1. 模块化设计：分离不同算法实现
 * 2. 异常安全：确保资源正确释放
 * 3. 代码可读性：使用有意义的变量名和注释
 */