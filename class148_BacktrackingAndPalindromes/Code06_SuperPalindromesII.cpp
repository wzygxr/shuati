#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <climits>
#include <limits>

using namespace std;

/**
 * 超级回文数II问题 - C++版本
 * 
 * 问题描述：
 * 如果一个正整数自身是回文数，而且它也是一个回文数的平方，那么我们称这个数为超级回文数。
 * 给定两个正整数 L 和 R（以字符串形式表示），返回包含在范围 [L, R] 中的超级回文数的最小值和最大值。
 * 如果不存在超级回文数，返回[-1, -1]。
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

class SuperPalindromesIISolver {
public:
    /**
     * 方法1：枚举法
     * 通过生成回文数来优化搜索，避免直接遍历大范围
     */
    vector<long long> superpalindromesInRange(string left, string right) {
        long long L = stoll(left);
        long long R = stoll(right);
        long long minVal = numeric_limits<long long>::max();
        long long maxVal = numeric_limits<long long>::min();
        bool found = false;
        
        // 预计算的超级回文数数组
        vector<long long> superPalindromes = getSuperPalindromes();
        
        // 查找范围内的最小值和最大值
        for (long long num : superPalindromes) {
            if (num >= L && num <= R) {
                found = true;
                if (num < minVal) minVal = num;
                if (num > maxVal) maxVal = num;
            }
        }
        
        if (!found) {
            vector<long long> result;
            result.push_back(-1);
            result.push_back(-1);
            return result;
        }
        
        vector<long long> result;
        result.push_back(minVal);
        result.push_back(maxVal);
        return result;
    }
    
    /**
     * 方法2：枚举生成法
     * 通过生成回文数种子来构造超级回文数
     */
    vector<long long> superpalindromesInRangeGenerate(string left, string right) {
        long long L = stoll(left);
        long long R = stoll(right);
        long long minVal = numeric_limits<long long>::max();
        long long maxVal = numeric_limits<long long>::min();
        bool found = false;
        
        long long seedLimit = 100000; // 10^5
        
        // 枚举所有可能的种子
        for (long long seed = 1; seed < seedLimit; seed++) {
            // 生成偶数长度的回文数
            long long evenPal = evenEnlarge(seed);
            long long squareEven = evenPal * evenPal;
            
            // 检查是否在范围内且是回文数
            if (squareEven >= L && squareEven <= R && isPalindrome(squareEven)) {
                found = true;
                if (squareEven < minVal) minVal = squareEven;
                if (squareEven > maxVal) maxVal = squareEven;
            }
            
            // 生成奇数长度的回文数
            long long oddPal = oddEnlarge(seed);
            long long squareOdd = oddPal * oddPal;
            
            // 检查是否在范围内且是回文数
            if (squareOdd >= L && squareOdd <= R && isPalindrome(squareOdd)) {
                found = true;
                if (squareOdd < minVal) minVal = squareOdd;
                if (squareOdd > maxVal) maxVal = squareOdd;
            }
            
            // 如果生成的平方已经超过R，可以提前终止
            if (squareEven > R && squareOdd > R) {
                break;
            }
        }
        
        if (!found) {
            vector<long long> result;
            result.push_back(-1);
            result.push_back(-1);
            return result;
        }
        
        vector<long long> result;
        result.push_back(minVal);
        result.push_back(maxVal);
        return result;
    }
    
private:
    /**
     * 获取预计算的超级回文数列表
     */
    vector<long long> getSuperPalindromes() {
        return {
            1LL, 4LL, 9LL, 121LL, 484LL, 10201LL, 12321LL, 14641LL, 40804LL, 44944LL,
            1002001LL, 1234321LL, 4008004LL, 100020001LL, 102030201LL, 104060401LL,
            121242121LL, 123454321LL, 125686521LL, 400080004LL, 404090404LL,
            10000200001LL, 10221412201LL, 12102420121LL, 12345654321LL, 40000800004LL,
            1000002000001LL, 1002003002001LL, 1004006004001LL, 1020304030201LL,
            1022325232201LL, 1024348434201LL, 1210024200121LL, 1212225222121LL,
            1214428244121LL, 1232346432321LL, 1234567654321LL, 4000008000004LL,
            4004009004004LL, 100000020000001LL, 100220141022001LL, 102012040210201LL,
            102234363432201LL, 121000242000121LL, 121242363242121LL, 123212464212321LL,
            123456787654321LL, 400000080000004LL, 10000000200000001LL, 10002000300020001LL,
            10004000600040001LL, 10020210401202001LL, 10022212521222001LL, 10024214841242001LL,
            10201020402010201LL, 10203040504030201LL, 10205060806050201LL, 10221432623412201LL,
            10223454745432201LL, 12100002420000121LL, 12102202520220121LL, 12104402820440121LL,
            12122232623222121LL, 12124434743442121LL, 12321024642012321LL, 12323244744232321LL,
            12343456865434321LL, 12345678987654321LL, 40000000800000004LL, 40004000900040004LL
        };
    }
    
    /**
     * 根据种子扩充到偶数长度的回文数字
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
 * 判断一个数是否是回文数（全局函数版本）
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

/**
 * LeetCode 479. 最大回文数乘积
 * 给定一个整数n，返回可表示为两个n位整数乘积的最大回文整数
 */
long long largestPalindrome(int n) {
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
                    return palindrome;
                }
            }
        }
    }
    
    return -1;
}

/**
 * LeetCode 906. 超级回文数（统计版本）
 * 统计范围内的超级回文数个数
 */
int countSuperPalindromes(string left, string right) {
    long long L = stoll(left);
    long long R = stoll(right);
    int count = 0;
    
    vector<long long> superPalindromes = {
        1LL, 4LL, 9LL, 121LL, 484LL, 10201LL, 12321LL, 14641LL, 40804LL, 44944LL,
        1002001LL, 1234321LL, 4008004LL, 100020001LL, 102030201LL, 104060401LL,
        121242121LL, 123454321LL, 125686521LL, 400080004LL, 404090404LL,
        10000200001LL, 10221412201LL, 12102420121LL, 12345654321LL, 40000800004LL
    };
    
    for (long long num : superPalindromes) {
        if (num >= L && num <= R) {
            count++;
        }
    }
    
    return count;
}

/**
 * LeetCode 9. 回文数（大数版本）
 * 支持大数判断的回文数函数
 */
bool isPalindromeBig(string s) {
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
 * 生成指定范围内的所有回文数
 */
vector<long long> generatePalindromesInRange(long long start, long long end) {
    vector<long long> result;
    
    for (long long i = start; i <= end; i++) {
        if (isPalindrome(i)) {
            result.push_back(i);
        }
    }
    
    return result;
}

// 测试函数
void testSuperPalindromesII() {
    SuperPalindromesIISolver solver;
    
    // 测试用例1
    string left1 = "4";
    string right1 = "1000";
    vector<long long> result1 = solver.superpalindromesInRange(left1, right1);
    vector<long long> result1_gen = solver.superpalindromesInRangeGenerate(left1, right1);
    
    cout << "测试用例1:" << endl;
    cout << "范围: [" << left1 << ", " << right1 << "]" << endl;
    cout << "打表法结果: [" << result1[0] << ", " << result1[1] << "]" << endl;
    cout << "生成法结果: [" << result1_gen[0] << ", " << result1_gen[1] << "]" << endl;
    cout << endl;
    
    // 测试用例2：无超级回文数的情况
    string left2 = "1000000000000000000";
    string right2 = "1000000000000000000";
    vector<long long> result2 = solver.superpalindromesInRange(left2, right2);
    
    cout << "测试用例2:" << endl;
    cout << "范围: [" << left2 << ", " << right2 << "]" << endl;
    cout << "结果: [" << result2[0] << ", " << result2[1] << "]" << endl;
    cout << endl;
    
    // 测试补充题目
    cout << "=== 补充训练题目测试 ===" << endl;
    
    // 测试最大回文数乘积
    cout << "最大回文数乘积(n=2): " << largestPalindrome(2) << endl;
    
    // 测试超级回文数统计
    cout << "超级回文数统计: 范围[4, 1000] -> " << countSuperPalindromes("4", "1000") << endl;
    
    // 测试大数回文判断
    string bigNum = "12345678987654321";
    cout << "大数回文判断: " << bigNum << " -> " << (isPalindromeBig(bigNum) ? "是" : "否") << endl;
    
    // 测试回文数生成
    vector<long long> palindromes = generatePalindromesInRange(100, 200);
    cout << "回文数生成(100-200): 数量=" << palindromes.size() << endl;
}

int main() {
    testSuperPalindromesII();
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