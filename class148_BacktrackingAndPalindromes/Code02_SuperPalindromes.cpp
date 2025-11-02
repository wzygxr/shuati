#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cmath>
#include <climits>
#include <cctype>

using namespace std;

/**
 * 超级回文数问题 - C++版本
 * 
 * 问题描述：
 * 如果一个正整数自身是回文数，而且它也是一个回文数的平方，那么我们称这个数为超级回文数。
 * 给定两个正整数 L 和 R（以字符串形式表示），返回包含在范围 [L, R] 中的超级回文数的数目。
 * 
 * 算法思路：
 * 方法1：枚举法 - 通过生成回文数来优化搜索
 * 方法2：打表法 - 预计算所有可能的超级回文数
 * 
 * 时间复杂度分析：
 * - 枚举法：O(√R * log R)，其中√R为平方根范围，log R为回文判断时间
 * - 打表法：预处理O(K * log K)，查询O(log K)，其中K为超级回文数个数（约70）
 * 
 * 空间复杂度分析：
 * - 枚举法：O(1)，常数额外空间
 * - 打表法：O(K)，存储预计算的超级回文数
 * 
 * 工程化考量：
 * 1. 大数处理：使用long long类型避免溢出
 * 2. 边界处理：处理L和R的边界情况
 * 3. 性能优化：选择合适的算法策略
 * 4. 可测试性：设计全面的测试用例
 */

class SuperPalindromesSolver {
public:
    /**
     * 方法1：枚举法
     * 通过生成回文数来优化搜索，避免直接遍历大范围
     */
    int superpalindromesInRange(string left, string right) {
        long long L = stoll(left);
        long long R = stoll(right);
        int count = 0;
        
        // 计算种子的上界（对于10^18范围，种子只需到10^5级别）
        long long seedLimit = 100000; // 10^5
        
        // 枚举所有可能的种子
        for (long long seed = 1; seed < seedLimit; seed++) {
            // 生成偶数长度的回文数
            long long evenPal = evenEnlarge(seed);
            long long squareEven = evenPal * evenPal;
            
            // 检查是否在范围内且是回文数
            if (squareEven >= L && squareEven <= R && isPalindrome(squareEven)) {
                count++;
            }
            
            // 生成奇数长度的回文数
            long long oddPal = oddEnlarge(seed);
            long long squareOdd = oddPal * oddPal;
            
            // 检查是否在范围内且是回文数
            if (squareOdd >= L && squareOdd <= R && isPalindrome(squareOdd)) {
                count++;
            }
            
            // 如果生成的平方已经超过R，可以提前终止
            if (squareEven > R && squareOdd > R) {
                break;
            }
        }
        
        return count;
    }
    
    /**
     * 方法2：打表法（最优解）
     * 预计算所有可能的超级回文数，查询时直接统计
     */
    int superpalindromesInRangeTable(string left, string right) {
        long long L = stoll(left);
        long long R = stoll(right);
        int count = 0;
        
        // 预计算的超级回文数数组
        vector<long long> superPalindromes;
        superPalindromes.push_back(1LL);
        superPalindromes.push_back(4LL);
        superPalindromes.push_back(9LL);
        superPalindromes.push_back(121LL);
        superPalindromes.push_back(484LL);
        superPalindromes.push_back(10201LL);
        superPalindromes.push_back(12321LL);
        superPalindromes.push_back(14641LL);
        superPalindromes.push_back(40804LL);
        superPalindromes.push_back(44944LL);
        superPalindromes.push_back(1002001LL);
        superPalindromes.push_back(1234321LL);
        superPalindromes.push_back(4008004LL);
        superPalindromes.push_back(100020001LL);
        superPalindromes.push_back(102030201LL);
        superPalindromes.push_back(104060401LL);
        superPalindromes.push_back(121242121LL);
        superPalindromes.push_back(123454321LL);
        superPalindromes.push_back(125686521LL);
        superPalindromes.push_back(400080004LL);
        superPalindromes.push_back(404090404LL);
        superPalindromes.push_back(10000200001LL);
        superPalindromes.push_back(10221412201LL);
        superPalindromes.push_back(12102420121LL);
        superPalindromes.push_back(12345654321LL);
        superPalindromes.push_back(40000800004LL);
        superPalindromes.push_back(1000002000001LL);
        superPalindromes.push_back(1002003002001LL);
        superPalindromes.push_back(1004006004001LL);
        superPalindromes.push_back(1020304030201LL);
        superPalindromes.push_back(1022325232201LL);
        superPalindromes.push_back(1024348434201LL);
        superPalindromes.push_back(1210024200121LL);
        superPalindromes.push_back(1212225222121LL);
        superPalindromes.push_back(1214428244121LL);
        superPalindromes.push_back(1232346432321LL);
        superPalindromes.push_back(1234567654321LL);
        superPalindromes.push_back(4000008000004LL);
        superPalindromes.push_back(4004009004004LL);
        superPalindromes.push_back(100000020000001LL);
        superPalindromes.push_back(100220141022001LL);
        superPalindromes.push_back(102012040210201LL);
        superPalindromes.push_back(102234363432201LL);
        superPalindromes.push_back(121000242000121LL);
        superPalindromes.push_back(121242363242121LL);
        superPalindromes.push_back(123212464212321LL);
        superPalindromes.push_back(123456787654321LL);
        superPalindromes.push_back(400000080000004LL);
        superPalindromes.push_back(10000000200000001LL);
        superPalindromes.push_back(10002000300020001LL);
        superPalindromes.push_back(10004000600040001LL);
        superPalindromes.push_back(10020210401202001LL);
        superPalindromes.push_back(10022212521222001LL);
        superPalindromes.push_back(10024214841242001LL);
        superPalindromes.push_back(10201020402010201LL);
        superPalindromes.push_back(10203040504030201LL);
        superPalindromes.push_back(10205060806050201LL);
        superPalindromes.push_back(10221432623412201LL);
        superPalindromes.push_back(10223454745432201LL);
        superPalindromes.push_back(12100002420000121LL);
        superPalindromes.push_back(12102202520220121LL);
        superPalindromes.push_back(12104402820440121LL);
        superPalindromes.push_back(12122232623222121LL);
        superPalindromes.push_back(12124434743442121LL);
        superPalindromes.push_back(12321024642012321LL);
        superPalindromes.push_back(12323244744232321LL);
        superPalindromes.push_back(12343456865434321LL);
        superPalindromes.push_back(12345678987654321LL);
        superPalindromes.push_back(40000000800000004LL);
        superPalindromes.push_back(40004000900040004LL);
        
        // 统计在范围内的超级回文数
        for (long long num : superPalindromes) {
            if (num >= L && num <= R) {
                count++;
            }
        }
        
        return count;
    }
    
private:
    /**
     * 根据种子扩充到偶数长度的回文数字
     * 例如：seed=123，返回123321
     */
    long long evenEnlarge(long long seed) {
        long long ans = seed;
        long long temp = seed;
        while (temp > 0) {
            ans = ans * 10 + temp % 10;
            temp /= 10;
        }
        return ans;
    }
    
    /**
     * 根据种子扩充到奇数长度的回文数字
     * 例如：seed=123，返回12321
     */
    long long oddEnlarge(long long seed) {
        long long ans = seed;
        long long temp = seed / 10;
        while (temp > 0) {
            ans = ans * 10 + temp % 10;
            temp /= 10;
        }
        return ans;
    }
    
    /**
     * 判断一个数是否是回文数
     * 使用数学方法避免字符串转换
     */
    bool isPalindrome(long long x) {
        if (x < 0) return false;
        if (x < 10) return true;
        
        long long original = x;
        long long reversed = 0;
        
        while (x > 0) {
            reversed = reversed * 10 + x % 10;
            x /= 10;
        }
        
        return original == reversed;
    }
};

/**
 * 补充训练题目 - C++实现
 */

/**
 * LeetCode 9. 回文数
 * 判断一个整数是否是回文数
 */
bool isPalindromeNumber(int x) {
    if (x < 0) return false;
    if (x < 10) return true;
    
    int original = x;
    int reversed = 0;
    
    while (x > 0) {
        // 检查溢出
        if (reversed > 214748364) return false; // INT_MAX / 10
        reversed = reversed * 10 + x % 10;
        x /= 10;
    }
    
    return original == reversed;
}

/**
 * LeetCode 479. 最大回文数乘积
 * 给定一个整数n，返回可表示为两个n位整数乘积的最大回文整数
 */
int largestPalindrome(int n) {
    if (n == 1) return 9;
    
    long long maxNum = 1;
    for (int i = 0; i < n; i++) {
        maxNum *= 10;
    }
    maxNum -= 1;
    
    long long minNum = 1;
    for (int i = 0; i < n - 1; i++) {
        minNum *= 10;
    }
    
    for (long long i = maxNum; i >= minNum; i--) {
        // 创建回文数
        string s = to_string(i);
        string rev = s;
        reverse(rev.begin(), rev.end());
        long long palindrome = stoll(s + rev);
        
        // 检查是否可以分解为两个n位数的乘积
        for (long long j = maxNum; j * j >= palindrome; j--) {
            if (palindrome % j == 0) {
                long long factor = palindrome / j;
                if (factor >= minNum && factor <= maxNum) {
                    return palindrome % 1337;
                }
            }
        }
    }
    
    return -1;
}

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

// 测试函数
void testSuperPalindromes() {
    SuperPalindromesSolver solver;
    
    // 测试用例1
    string left1 = "4";
    string right1 = "1000";
    int result1 = solver.superpalindromesInRange(left1, right1);
    int result1_table = solver.superpalindromesInRangeTable(left1, right1);
    
    cout << "测试用例1:" << endl;
    cout << "范围: [" << left1 << ", " << right1 << "]" << endl;
    cout << "枚举法结果: " << result1 << endl;
    cout << "打表法结果: " << result1_table << endl;
    cout << endl;
    
    // 测试用例2
    string left2 = "1";
    string right2 = "2";
    int result2 = solver.superpalindromesInRange(left2, right2);
    int result2_table = solver.superpalindromesInRangeTable(left2, right2);
    
    cout << "测试用例2:" << endl;
    cout << "范围: [" << left2 << ", " << right2 << "]" << endl;
    cout << "枚举法结果: " << result2 << endl;
    cout << "打表法结果: " << result2_table << endl;
    cout << endl;
    
    // 测试补充题目
    cout << "=== 补充训练题目测试 ===" << endl;
    
    // 测试回文数判断
    cout << "回文数判断: 12321 -> " << (isPalindromeNumber(12321) ? "是" : "否") << endl;
    cout << "回文数判断: 12345 -> " << (isPalindromeNumber(12345) ? "是" : "否") << endl;
    
    // 测试最大回文数乘积
    cout << "最大回文数乘积(n=2): " << largestPalindrome(2) << endl;
    
    // 测试回文串验证
    string testStr = "A man, a plan, a canal: Panama";
    cout << "回文串验证: \"" << testStr << "\" -> " << (isPalindromeString(testStr) ? "是" : "否") << endl;
    
    // 测试最长回文子串
    string testStr2 = "babad";
    cout << "最长回文子串: \"" << testStr2 << "\" -> \"" << longestPalindrome(testStr2) << "\"" << endl;
}

int main() {
    testSuperPalindromes();
    return 0;
}

/**
 * 算法技巧总结 - C++版本
 * 
 * 核心概念：
 * 1. 回文数生成技术：
 *    - 种子生成法：通过种子数字构造回文数
 *    - 对称性利用：利用回文数的对称特征
 *    - 数学方法：避免字符串转换的开销
 * 
 * 2. 算法选择策略：
 *    - 小范围查询：枚举法更节省内存
 *    - 多次查询：打表法性能最优
 *    - 大数据范围：需要数学优化和边界处理
 * 
 * 3. 性能优化技巧：
 *    - 提前终止：当结果超出范围时提前结束搜索
 *    - 数学优化：使用位运算和数学方法
 *    - 缓存策略：预计算常用结果
 * 
 * 调试技巧：
 * 1. 边界值测试：最小/最大输入、边界情况
 * 2. 中间结果打印：跟踪算法执行过程
 * 3. 性能分析：使用profiler工具分析瓶颈
 * 
 * 工程化实践：
 * 1. 模块化设计：分离算法逻辑和业务逻辑
 * 2. 异常安全：确保资源正确释放
 * 3. 代码可读性：使用有意义的变量名和注释
 */