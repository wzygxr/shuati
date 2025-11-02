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

using namespace std;

/**
 * 位运算算法实现
 * 包含LeetCode多个位运算相关题目的解决方案
 * 
 * 题目列表:
 * 1. LeetCode 136 - 只出现一次的数字
 * 2. LeetCode 137 - 只出现一次的数字 II
 * 3. LeetCode 260 - 只出现一次的数字 III
 * 4. LeetCode 191 - 位1的个数
 * 5. LeetCode 231 - 2的幂
 * 6. LeetCode 342 - 4的幂
 * 7. LeetCode 371 - 两整数之和
 * 8. LeetCode 201 - 数字范围按位与
 * 9. LeetCode 338 - 比特位计数
 * 10. LeetCode 405 - 数字转换为十六进制数
 * 
 * 时间复杂度分析:
 * - 位运算操作: O(1) 或 O(n)
 * - 空间复杂度: O(1) 或 O(n)
 * 
 * 工程化考量:
 * 1. 位运算技巧: 使用位运算优化算法
 * 2. 边界处理: 处理负数、零等边界情况
 * 3. 性能优化: 利用位运算的常数时间优势
 * 4. 可读性: 添加详细注释说明位运算原理
 */

class BitwiseOperations {
public:
    /**
     * LeetCode 136 - 只出现一次的数字
     * 题目链接: https://leetcode.com/problems/single-number/
     * 给定一个非空整数数组，除了某个元素只出现一次外，其余每个元素均出现两次。找出那个只出现一次的元素。
     * 
     * 方法: 异或运算
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 原理: a ^ a = 0, a ^ 0 = a
     * 所有出现两次的数字异或后为0，最后剩下的就是只出现一次的数字
     */
    static int singleNumber(vector<int>& nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
    
    /**
     * LeetCode 137 - 只出现一次的数字 II
     * 题目链接: https://leetcode.com/problems/single-number-ii/
     * 给定一个非空整数数组，除了某个元素只出现一次外，其余每个元素均出现三次。找出那个只出现一次的元素。
     * 
     * 方法: 位计数法
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 原理: 统计每个位上1出现的次数，对3取模
     * 如果某位上1出现的次数不是3的倍数，说明只出现一次的数字在该位为1
     */
    static int singleNumberII(vector<int>& nums) {
        int result = 0;
        
        // 遍历32位
        for (int i = 0; i < 32; i++) {
            int count = 0;
            
            // 统计第i位为1的数字个数
            for (int num : nums) {
                if ((num >> i) & 1) {
                    count++;
                }
            }
            
            // 如果count % 3 != 0，说明只出现一次的数字在该位为1
            if (count % 3 != 0) {
                result |= (1 << i);
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 260 - 只出现一次的数字 III
     * 题目链接: https://leetcode.com/problems/single-number-iii/
     * 给定一个整数数组，其中恰好有两个元素只出现一次，其余所有元素均出现两次。找出只出现一次的那两个元素。
     * 
     * 方法: 分组异或
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 原理:
     * 1. 先对所有数字异或，得到两个不同数字的异或结果
     * 2. 找到异或结果中为1的某一位（两个数字在该位不同）
     * 3. 根据这一位将数组分成两组，分别异或得到两个数字
     */
    static vector<int> singleNumberIII(vector<int>& nums) {
        // 第一步：计算所有数字的异或
        int xor_all = 0;
        for (int num : nums) {
            xor_all ^= num;
        }
        
        // 第二步：找到为1的最低位
        int diff_bit = 1;
        while ((xor_all & diff_bit) == 0) {
            diff_bit <<= 1;
        }
        
        // 第三步：根据diff_bit分组异或
        int num1 = 0, num2 = 0;
        for (int num : nums) {
            if (num & diff_bit) {
                num1 ^= num;
            } else {
                num2 ^= num;
            }
        }
        
        return {num1, num2};
    }
    
    /**
     * LeetCode 191 - 位1的个数
     * 题目链接: https://leetcode.com/problems/number-of-1-bits/
     * 编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
     * 
     * 方法1: 循环检查二进制位
     * 方法2: Brian Kernighan算法
     * 时间复杂度: O(1) - 固定32位
     * 空间复杂度: O(1)
     */
    static int hammingWeight(uint32_t n) {
        // 方法1: 循环检查
        int count = 0;
        while (n) {
            count += (n & 1);
            n >>= 1;
        }
        return count;
        
        // 方法2: Brian Kernighan算法（更高效）
        // int count = 0;
        // while (n) {
        //     n &= (n - 1);  // 清除最低位的1
        //     count++;
        // }
        // return count;
    }
    
    /**
     * LeetCode 231 - 2的幂
     * 题目链接: https://leetcode.com/problems/power-of-two/
     * 给定一个整数，编写一个函数来判断它是否是 2 的幂次方。
     * 
     * 方法: 位运算性质
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * 原理: 2的幂的二进制表示中只有一位是1
     * n > 0 && (n & (n - 1)) == 0
     */
    static bool isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    /**
     * LeetCode 342 - 4的幂
     * 题目链接: https://leetcode.com/problems/power-of-four/
     * 给定一个整数，编写一个函数来判断它是否是 4 的幂次方。
     * 
     * 方法: 位运算 + 数学性质
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * 原理:
     * 1. 必须是2的幂: n > 0 && (n & (n - 1)) == 0
     * 2. 4的幂的1出现在奇数位: (n & 0x55555555) != 0
     */
    static bool isPowerOfFour(int n) {
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }
    
    /**
     * LeetCode 371 - 两整数之和
     * 题目链接: https://leetcode.com/problems/sum-of-two-integers/
     * 不使用运算符 + 和 - ，计算两整数 a 、b 之和。
     * 
     * 方法: 位运算模拟加法
     * 时间复杂度: O(1) - 最多32次循环
     * 空间复杂度: O(1)
     * 
     * 原理:
     * 1. 异或运算得到无进位和
     * 2. 与运算左移一位得到进位
     * 3. 循环直到进位为0
     */
    static int getSum(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;  // 进位
            a = a ^ b;                // 无进位和
            b = carry;                // 进位作为新的b
        }
        return a;
    }
    
    /**
     * LeetCode 201 - 数字范围按位与
     * 题目链接: https://leetcode.com/problems/bitwise-and-of-numbers-range/
     * 给定两个整数 left 和 right，返回区间 [left, right] 内所有数字按位与的结果。
     * 
     * 方法: 寻找公共前缀
     * 时间复杂度: O(1) - 最多32次循环
     * 空间复杂度: O(1)
     * 
     * 原理: 区间内所有数字的按位与结果等于它们的公共二进制前缀
     */
    static int rangeBitwiseAnd(int left, int right) {
        int shift = 0;
        
        // 找到公共前缀
        while (left < right) {
            left >>= 1;
            right >>= 1;
            shift++;
        }
        
        return left << shift;
    }
    
    /**
     * LeetCode 338 - 比特位计数
     * 题目链接: https://leetcode.com/problems/counting-bits/
     * 给定一个非负整数 num。对于 0 ≤ i ≤ num 范围内的每个数字 i ，计算其二进制数中的 1 的数目并将它们作为数组返回。
     * 
     * 方法1: 直接计算每个数字的汉明重量
     * 方法2: 动态规划 + 最高有效位
     * 方法3: 动态规划 + 最低设置位
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    static vector<int> countBits(int n) {
        vector<int> result(n + 1, 0);
        
        // 方法1: 直接计算（简单但较慢）
        // for (int i = 0; i <= n; i++) {
        //     result[i] = hammingWeight(i);
        // }
        
        // 方法2: 动态规划 + 最高有效位（推荐）
        // result[0] = 0;
        // int highestBit = 0;
        // for (int i = 1; i <= n; i++) {
        //     if ((i & (i - 1)) == 0) {  // 检查是否是2的幂
        //         highestBit = i;
        //     }
        //     result[i] = result[i - highestBit] + 1;
        // }
        
        // 方法3: 动态规划 + 最低设置位（最优）
        result[0] = 0;
        for (int i = 1; i <= n; i++) {
            result[i] = result[i & (i - 1)] + 1;
        }
        
        return result;
    }
    
    /**
     * LeetCode 405 - 数字转换为十六进制数
     * 题目链接: https://leetcode.com/problems/convert-a-number-to-a-hexadecimal/
     * 给定一个整数，编写一个算法将这个数转换为十六进制数。对于负整数，我们通常使用补码运算方法。
     * 
     * 方法: 位运算 + 查表
     * 时间复杂度: O(1) - 最多8次循环
     * 空间复杂度: O(1)
     */
    static string toHex(int num) {
        if (num == 0) return "0";
        
        string hex_chars = "0123456789abcdef";
        string result;
        unsigned int n = num;  // 处理负数补码
        
        while (n > 0) {
            int digit = n & 0xf;  // 取最低4位
            result = hex_chars[digit] + result;
            n >>= 4;             // 右移4位
        }
        
        return result;
    }
    
    /**
     * 额外题目: 交换两个数字（不使用临时变量）
     * 使用异或运算交换两个整数
     */
    static void swapNumbers(int& a, int& b) {
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
    }
    
    /**
     * 额外题目: 判断奇偶性
     * 使用位运算判断数字的奇偶性
     */
    static bool isOdd(int n) {
        return (n & 1) == 1;
    }
    
    /**
     * 额外题目: 取绝对值
     * 使用位运算计算整数的绝对值
     */
    static int absoluteValue(int n) {
        int mask = n >> 31;           // 对于负数，mask = -1；对于正数，mask = 0
        return (n + mask) ^ mask;     // 对于负数：n + (-1) = n-1，然后异或-1相当于取反
    }
};

class PerformanceTester {
public:
    static void testSingleNumber() {
        cout << "=== 只出现一次的数字性能测试 ===" << endl;
        
        // 生成测试数据
        vector<int> nums1(1000000, 0);
        for (int i = 0; i < 500000; i++) {
            nums1[2*i] = i;
            nums1[2*i+1] = i;
        }
        nums1.push_back(1000000);  // 只出现一次的数字
        
        auto start = chrono::high_resolution_clock::now();
        int result = BitwiseOperations::singleNumber(nums1);
        auto time = chrono::duration_cast<chrono::microseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "singleNumber: 结果=" << result << ", 耗时=" << time << " μs" << endl;
    }
    
    static void testHammingWeight() {
        cout << "\n=== 汉明重量性能测试 ===" << endl;
        
        uint32_t test_num = 0xFFFFFFFF;  // 所有位都是1
        
        auto start = chrono::high_resolution_clock::now();
        int result = BitwiseOperations::hammingWeight(test_num);
        auto time = chrono::duration_cast<chrono::nanoseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "hammingWeight: 结果=" << result << ", 耗时=" << time << " ns" << endl;
    }
    
    static void runUnitTests() {
        cout << "=== 位运算算法单元测试 ===" << endl;
        
        // 测试singleNumber
        vector<int> nums1 = {2, 2, 1};
        assert(BitwiseOperations::singleNumber(nums1) == 1);
        
        vector<int> nums2 = {4, 1, 2, 1, 2};
        assert(BitwiseOperations::singleNumber(nums2) == 4);
        
        // 测试isPowerOfTwo
        assert(BitwiseOperations::isPowerOfTwo(1) == true);
        assert(BitwiseOperations::isPowerOfTwo(16) == true);
        assert(BitwiseOperations::isPowerOfTwo(3) == false);
        
        // 测试getSum
        assert(BitwiseOperations::getSum(1, 2) == 3);
        assert(BitwiseOperations::getSum(-1, 1) == 0);
        
        cout << "所有单元测试通过!" << endl;
    }
    
    static void complexityAnalysis() {
        cout << "\n=== 复杂度分析 ===" << endl;
        
        vector<pair<string, string>> algorithms = {
            {"singleNumber", "O(n), O(1)"},
            {"singleNumberII", "O(n), O(1)"},
            {"singleNumberIII", "O(n), O(1)"},
            {"hammingWeight", "O(1), O(1)"},
            {"isPowerOfTwo", "O(1), O(1)"},
            {"getSum", "O(1), O(1)"},
            {"countBits", "O(n), O(n)"}
        };
        
        for (auto& algo : algorithms) {
            cout << algo.first << ": 时间复杂度=" << algo.second << endl;
        }
    }
};

int main() {
    cout << "位运算算法实现" << endl;
    cout << "包含LeetCode多个位运算相关题目的解决方案" << endl;
    cout << "==========================================" << endl;
    
    // 运行单元测试
    PerformanceTester::runUnitTests();
    
    // 运行性能测试
    PerformanceTester::testSingleNumber();
    PerformanceTester::testHammingWeight();
    
    // 复杂度分析
    PerformanceTester::complexityAnalysis();
    
    // 示例使用
    cout << "\n=== 示例使用 ===" << endl;
    
    // 只出现一次的数字示例
    vector<int> nums = {4, 1, 2, 1, 2};
    cout << "数组: ";
    for (int num : nums) cout << num << " ";
    cout << endl;
    cout << "只出现一次的数字: " << BitwiseOperations::singleNumber(nums) << endl;
    
    // 2的幂示例
    int test_num = 16;
    cout << test_num << " 是2的幂: " << (BitwiseOperations::isPowerOfTwo(test_num) ? "是" : "否") << endl;
    
    // 汉明重量示例
    uint32_t n = 11;  // 二进制: 1011
    cout << n << " 的汉明重量: " << BitwiseOperations::hammingWeight(n) << endl;
    
    // 数字交换示例
    int a = 5, b = 10;
    cout << "交换前: a=" << a << ", b=" << b << endl;
    BitwiseOperations::swapNumbers(a, b);
    cout << "交换后: a=" << a << ", b=" << b << endl;
    
    return 0;
}