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
 * LeetCode 191 - 位1的个数
 * 题目链接: https://leetcode.com/problems/number-of-1-bits/
 * 题目描述: 编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 '1' 的个数
 * 
 * 解题思路:
 * 方法1: 逐位检查 - 检查每一位是否为1
 * 方法2: Brian Kernighan算法 - 利用n & (n-1)清除最低位的1
 * 方法3: 查表法 - 预计算8位数的1的个数
 * 方法4: 并行计算 - 使用位运算并行计算
 * 
 * 时间复杂度分析:
 * 方法1: O(32) - 固定32次循环
 * 方法2: O(k) - k为1的个数
 * 方法3: O(1) - 查表操作
 * 方法4: O(1) - 常数时间位运算
 * 
 * 空间复杂度分析:
 * 方法1: O(1) - 常数空间
 * 方法2: O(1) - 常数空间
 * 方法3: O(256) - 256字节的查找表
 * 方法4: O(1) - 常数空间
 * 
 * 工程化考量:
 * 1. 性能优化: 选择最优算法
 * 2. 可移植性: 处理不同整数大小
 * 3. 边界处理: 处理0和最大值的特殊情况
 */

class BitManipulation {
public:
    /**
     * 方法1: 逐位检查
     * 时间复杂度: O(32) - 固定32次循环
     * 空间复杂度: O(1)
     */
    static int hammingWeight1(uint32_t n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            if (n & (1 << i)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 方法2: Brian Kernighan算法
     * 利用n & (n-1)清除最低位的1
     * 时间复杂度: O(k) - k为1的个数
     * 空间复杂度: O(1)
     */
    static int hammingWeight2(uint32_t n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1);
            count++;
        }
        return count;
    }
    
    /**
     * 方法3: 查表法
     * 预计算8位数的1的个数
     * 时间复杂度: O(1) - 查表操作
     * 空间复杂度: O(256) - 256字节的查找表
     */
    static int hammingWeight3(uint32_t n) {
        static const int table[256] = {
            0,1,1,2,1,2,2,3,1,2,2,3,2,3,3,4,1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,
            1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
            1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
            2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
            1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
            2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
            2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
            3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,4,5,5,6,5,6,6,7,5,6,6,7,6,7,7,8
        };
        
        return table[n & 0xFF] + 
               table[(n >> 8) & 0xFF] + 
               table[(n >> 16) & 0xFF] + 
               table[(n >> 24) & 0xFF];
    }
    
    /**
     * 方法4: 并行计算
     * 使用位运算并行计算1的个数
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    static int hammingWeight4(uint32_t n) {
        n = (n & 0x55555555) + ((n >> 1) & 0x55555555);
        n = (n & 0x33333333) + ((n >> 2) & 0x33333333);
        n = (n & 0x0F0F0F0F) + ((n >> 4) & 0x0F0F0F0F);
        n = (n & 0x00FF00FF) + ((n >> 8) & 0x00FF00FF);
        n = (n & 0x0000FFFF) + ((n >> 16) & 0x0000FFFF);
        return n;
    }
    
    /**
     * LeetCode 231 - 2的幂
     * 题目链接: https://leetcode.com/problems/power-of-two/
     * 判断一个数是否是2的幂
     */
    static bool isPowerOfTwo(int n) {
        if (n <= 0) return false;
        return (n & (n - 1)) == 0;
    }
    
    /**
     * LeetCode 342 - 4的幂
     * 题目链接: https://leetcode.com/problems/power-of-four/
     * 判断一个数是否是4的幂
     */
    static bool isPowerOfFour(int n) {
        if (n <= 0) return false;
        // 必须是2的幂，且1出现在奇数位
        return (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }
    
    /**
     * LeetCode 136 - 只出现一次的数字
     * 题目链接: https://leetcode.com/problems/single-number/
     * 使用异或操作找出只出现一次的数字
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
     * 每个数字出现三次，只有一个出现一次
     */
    static int singleNumberII(vector<int>& nums) {
        int ones = 0, twos = 0;
        for (int num : nums) {
            ones = (ones ^ num) & ~twos;
            twos = (twos ^ num) & ~ones;
        }
        return ones;
    }
    
    /**
     * LeetCode 260 - 只出现一次的数字 III
     * 题目链接: https://leetcode.com/problems/single-number-iii/
     * 有两个数字只出现一次，其余出现两次
     */
    static vector<int> singleNumberIII(vector<int>& nums) {
        int xor_all = 0;
        for (int num : nums) {
            xor_all ^= num;
        }
        
        // 找到最低位的1
        int lowest_bit = xor_all & (-xor_all);
        
        int group1 = 0, group2 = 0;
        for (int num : nums) {
            if (num & lowest_bit) {
                group1 ^= num;
            } else {
                group2 ^= num;
            }
        }
        
        return {group1, group2};
    }
    
    /**
     * LeetCode 190 - 颠倒二进制位
     * 题目链接: https://leetcode.com/problems/reverse-bits/
     */
    static uint32_t reverseBits(uint32_t n) {
        n = ((n >> 1) & 0x55555555) | ((n & 0x55555555) << 1);
        n = ((n >> 2) & 0x33333333) | ((n & 0x33333333) << 2);
        n = ((n >> 4) & 0x0F0F0F0F) | ((n & 0x0F0F0F0F) << 4);
        n = ((n >> 8) & 0x00FF00FF) | ((n & 0x00FF00FF) << 8);
        n = (n >> 16) | (n << 16);
        return n;
    }
    
    /**
     * LeetCode 338 - 比特位计数
     * 题目链接: https://leetcode.com/problems/counting-bits/
     * 计算0到n每个数的二进制表示中1的个数
     */
    static vector<int> countBits(int n) {
        vector<int> result(n + 1, 0);
        for (int i = 1; i <= n; i++) {
            result[i] = result[i & (i - 1)] + 1;
        }
        return result;
    }
    
    /**
     * LeetCode 201 - 数字范围按位与
     * 题目链接: https://leetcode.com/problems/bitwise-and-of-numbers-range/
     * 计算区间[m, n]内所有数字的按位与
     */
    static int rangeBitwiseAnd(int m, int n) {
        int shift = 0;
        while (m < n) {
            m >>= 1;
            n >>= 1;
            shift++;
        }
        return m << shift;
    }
    
    /**
     * LeetCode 371 - 两整数之和
     * 题目链接: https://leetcode.com/problems/sum-of-two-integers/
     * 不使用+和-运算符计算两数之和
     */
    static int getSum(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;
            a = a ^ b;
            b = carry;
        }
        return a;
    }
};

class PerformanceTester {
public:
    static void testHammingWeight() {
        cout << "=== 位1的个数性能测试 ===" << endl;
        
        vector<uint32_t> test_cases = {
            0, 1, 15, 255, 1023, 0xFFFFFFFF, 0x55555555, 0xAAAAAAAA
        };
        
        for (uint32_t n : test_cases) {
            cout << "测试数字: " << n << " (" << bitset<32>(n) << ")" << endl;
            
            auto start = chrono::high_resolution_clock::now();
            int r1 = BitManipulation::hammingWeight1(n);
            auto time1 = chrono::duration_cast<chrono::nanoseconds>(
                chrono::high_resolution_clock::now() - start).count();
            
            start = chrono::high_resolution_clock::now();
            int r2 = BitManipulation::hammingWeight2(n);
            auto time2 = chrono::duration_cast<chrono::nanoseconds>(
                chrono::high_resolution_clock::now() - start).count();
            
            start = chrono::high_resolution_clock::now();
            int r3 = BitManipulation::hammingWeight3(n);
            auto time3 = chrono::duration_cast<chrono::nanoseconds>(
                chrono::high_resolution_clock::now() - start).count();
            
            start = chrono::high_resolution_clock::now();
            int r4 = BitManipulation::hammingWeight4(n);
            auto time4 = chrono::duration_cast<chrono::nanoseconds>(
                chrono::high_resolution_clock::now() - start).count();
            
            cout << "  方法1(逐位): " << r1 << ", 时间: " << time1 << " ns" << endl;
            cout << "  方法2(Kernighan): " << r2 << ", 时间: " << time2 << " ns" << endl;
            cout << "  方法3(查表): " << r3 << ", 时间: " << time3 << " ns" << endl;
            cout << "  方法4(并行): " << r4 << ", 时间: " << time4 << " ns" << endl;
            cout << "  结果一致: " << (r1 == r2 && r2 == r3 && r3 == r4 ? "是" : "否") << endl;
            cout << endl;
        }
    }
    
    static void runUnitTests() {
        cout << "=== 位操作单元测试 ===" << endl;
        
        // 测试hammingWeight
        assert(BitManipulation::hammingWeight1(11) == 3);
        assert(BitManipulation::hammingWeight2(11) == 3);
        assert(BitManipulation::hammingWeight3(11) == 3);
        assert(BitManipulation::hammingWeight4(11) == 3);
        cout << "hammingWeight测试通过" << endl;
        
        // 测试2的幂
        assert(BitManipulation::isPowerOfTwo(1) == true);
        assert(BitManipulation::isPowerOfTwo(16) == true);
        assert(BitManipulation::isPowerOfTwo(15) == false);
        cout << "isPowerOfTwo测试通过" << endl;
        
        // 测试4的幂
        assert(BitManipulation::isPowerOfFour(16) == true);
        assert(BitManipulation::isPowerOfFour(8) == false);
        cout << "isPowerOfFour测试通过" << endl;
        
        // 测试只出现一次的数字
        vector<int> nums1 = {2, 2, 1};
        assert(BitManipulation::singleNumber(nums1) == 1);
        cout << "singleNumber测试通过" << endl;
        
        // 测试比特位计数
        vector<int> result = BitManipulation::countBits(5);
        vector<int> expected = {0, 1, 1, 2, 1, 2};
        assert(result == expected);
        cout << "countBits测试通过" << endl;
        
        // 测试数字范围按位与
        assert(BitManipulation::rangeBitwiseAnd(5, 7) == 4);
        cout << "rangeBitwiseAnd测试通过" << endl;
        
        // 测试两整数之和
        assert(BitManipulation::getSum(3, 5) == 8);
        cout << "getSum测试通过" << endl;
        
        cout << "所有单元测试通过!" << endl;
    }
};

int main() {
    cout << "位操作算法实现" << endl;
    cout << "包含LeetCode多个位操作相关题目的解决方案" << endl;
    cout << "==========================================" << endl;
    
    // 运行单元测试
    PerformanceTester::runUnitTests();
    
    // 运行性能测试
    PerformanceTester::testHammingWeight();
    
    // 示例使用
    cout << "=== 示例使用 ===" << endl;
    
    // 测试hammingWeight
    uint32_t test_num = 11;
    cout << "数字 " << test_num << " 的二进制表示中1的个数:" << endl;
    cout << "  方法1: " << BitManipulation::hammingWeight1(test_num) << endl;
    cout << "  方法2: " << BitManipulation::hammingWeight2(test_num) << endl;
    cout << "  方法3: " << BitManipulation::hammingWeight3(test_num) << endl;
    cout << "  方法4: " << BitManipulation::hammingWeight4(test_num) << endl;
    
    // 测试2的幂
    cout << "\n2的幂判断:" << endl;
    cout << "16是2的幂: " << BitManipulation::isPowerOfTwo(16) << endl;
    cout << "15是2的幂: " << BitManipulation::isPowerOfTwo(15) << endl;
    
    // 测试比特位计数
    vector<int> bits_count = BitManipulation::countBits(5);
    cout << "\n0到5的比特位计数: ";
    for (int count : bits_count) {
        cout << count << " ";
    }
    cout << endl;
    
    return 0;
}