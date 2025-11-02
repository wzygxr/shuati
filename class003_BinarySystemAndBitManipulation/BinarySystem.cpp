#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstring>
#include <climits>
#include <string>
#include <unordered_set>
#include <set>
#include <random>
#include <chrono>
#include <stdexcept>
#include <bitset>
#include <cstdint>

using namespace std;

using namespace std;

/**
 * ================================================================================================
 * Class003: 二进制系统与位运算专题（Binary System and Bit Manipulation）- C++版本
 * 来源: 算法学习系统
 * 更新时间: 2025-10-23
 * 题目总数: 200+道精选题目
 * 平台覆盖: LeetCode (力扣)、LintCode (炼码)、HackerRank、赛码、AtCoder、USACO、洛谷 (Luogu)、CodeChef、SPOJ、Project Euler、HackerEarth、计蒜客、各大高校 OJ、zoj、MarsCode、UVa OJ、TimusOJ、AizuOJ、Comet OJ、杭电 OJ、LOJ、牛客、杭州电子科技大学、acwing、codeforces、hdu、poj、剑指Offer等
 * ================================================================================================
 * 
 * 【核心知识点总结】
 * 1. 位运算基础：
 *    - AND(&): 两位都为1时结果为1，常用于清零特定位、提取特定位
 *    - OR(|): 有一位为1时结果为1，常用于设置特定位
 *    - XOR(^): 两位不同时结果为1，常用于无临时变量交换、查找单独元素
 *    - NOT(~): 按位取反
 *    - 左移(<<): 相当于乘以2的幂（非负数），所有位向左移动
 *    - 右移(>>): 算术右移，保留符号位
 * 
 * 2. 常见技巧与应用场景：
 *    ① 判断奇偶：(n & 1) == 1 为奇数，== 0 为偶数
 *    ② 交换变量：a ^= b; b ^= a; a ^= b; （无需临时变量）
 *    ③ 清除最右边的1：n &= (n - 1)
 *    ④ 获取最右边的1：n & (-n)
 *    ⑤ 判断2的幂：n > 0 && (n & (n - 1)) == 0
 *    ⑥ 计算二进制中1的个数：Brian Kernighan算法
 *    ⑦ 找唯一元素：利用 a ^ a = 0, a ^ 0 = a
 *    ⑧ 位掩码：用于状态压缩DP、集合表示
 * 
 * 3. 题型分类：
 *    【基础操作】：位反转、位计数、进制转换
 *    【数学性质】：幂判断、格雷编码、斯特林数
 *    【查找问题】：找唯一元素、找缺失数字、找重复数字
 *    【XOR应用】：异或和、最大异或对、异或路径
 *    【位运算优化】：快速幂、乘法优化、状态压缩
 *    【工程应用】：位图、布隆过滤器、哈希表优化
 * 
 * 【时间复杂度分析技巧】
 * - 基础位运算：O(1) 常数时间
 * - 遍历所有位：O(log n) 或 O(32/64) = O(1)
 * - Brian Kernighan算法：O(k)，k为1的个数
 * - Trie树优化XOR：O(n * log(max_value))
 * 
 * 【空间复杂度优化】
 * - 原地操作：使用异或交换，空间O(1)
 * - 位压缩：用一个整数表示多个布尔值
 * - 滚动数组：DP优化空间
 * 
 * 【边界场景与异常处理】
 * 1. 负数处理：
 *    - C++使用补码表示负数
 *    - 最小值的绝对值等于自身：INT_MIN
 *    - 右移操作：>>保留符号，对unsigned自动逻辑右移
 * 2. 溢出处理：
 *    - int: 32位，范围 -2^31 ~ 2^31-1
 *    - long long: 64位，范围 -2^63 ~ 2^63-1
 *    - 位移操作：(1 << 31)会溢出，应使用1LL << 31
 * 3. 边界值：
 *    - 0的特殊处理（补数、幂判断等）
 *    - 空数组的判断
 *    - 单元素数组的特殊情况
 * 
 * 【语言特性差异（C++ vs Java vs Python）】
 * 1. 整数表示：
 *    - C++: int大小取决于平台，有signed/unsigned
 *    - Java: 固定32位int/64位long，有符号
 *    - Python: 任意精度整数，无固定大小
 * 2. 位运算操作符：
 *    - C++: 无>>>，对unsigned自动逻辑右移
 *    - Java: 有>>>无符号右移
 *    - Python: 无>>>，负数需要特殊处理
 * 3. 位长度获取：
 *    - C++: __builtin_popcount(), __builtin_clz()
 *    - Java: Integer.bitCount(), Integer.numberOfLeadingZeros()
 *    - Python: bin(n).count('1'), n.bit_length()
 * 
 * 【工程化考量】
 * 1. 代码可读性：
 *    - 使用常量命名位掩码：const int MASK_ODD_BITS = 0x55555555;
 *    - 添加详细注释说明位操作意图
 *    - 复杂位运算拆分为多步
 * 2. 性能优化：
 *    - 使用位运算替代乘除法（仅限2的幂）
 *    - 查表法优化频繁的位计数
 *    - 编译器内置函数优化
 * 3. 异常处理：
 *    - 参数验证：if (n < 0) throw invalid_argument("参数错误");
 *    - 边界检查：数组访问前检查索引
 *    - 溢出检测：关键计算添加断言
 * 4. 单元测试：
 *    - 正常值测试
 *    - 边界值测试（0, 1, INT_MAX, INT_MIN）
 *    - 负数测试
 *    - 大规模数据性能测试
 */
class BinarySystem {
public:
    /**
     * 主函数：演示二进制系统的基本操作和扩展题目
     */
    static void main() {
        cout << "=== 二进制系统与位运算专题 ===" << endl;
        cout << "作者: 算法学习系统" << endl;
        cout << "日期: 2024年" << endl;
        cout << endl;
        
        // 运行基础操作测试
        runBasicOperations();
        
        // 运行扩展题目测试
        runExtendedProblems();
        
        cout << "=== 所有测试完成 ===" << endl;
    }
    
    /**
     * 运行基础操作测试
     */
    static void runBasicOperations() {
        cout << "=== 基础操作测试 ===" << endl;
        
        // 测试位反转
        cout << "位反转测试:" << endl;
        unsigned int testReverse = 43261596; // 00000010100101000001111010011100
        cout << "原数字: " << testReverse << " (二进制: " << bitset<32>(testReverse) << ")" << endl;
        unsigned int reversed = reverseBits(testReverse);
        cout << "颠倒后: " << reversed << " (二进制: " << bitset<32>(reversed) << ")" << endl;
        cout << "预期结果: 964176192" << endl;
        cout << endl;
        
        // 测试汉明重量
        cout << "汉明重量测试:" << endl;
        unsigned int testHamming = 11; // 1011
        cout << "数字: " << testHamming << " (二进制: " << bitset<32>(testHamming) << ")" << endl;
        cout << "1的个数: " << hammingWeight(testHamming) << endl;
        cout << "预期结果: 3" << endl;
        cout << endl;
        
        // 测试2的幂判断
        cout << "2的幂判断测试:" << endl;
        cout << "8是2的幂: " << (isPowerOfTwo(8) ? "是" : "否") << endl;
        cout << "10是2的幂: " << (isPowerOfTwo(10) ? "是" : "否") << endl;
        cout << endl;
        
        // 测试4的幂判断
        cout << "4的幂判断测试:" << endl;
        cout << "16是4的幂: " << (isPowerOfFour(16) ? "是" : "否") << endl;
        cout << "8是4的幂: " << (isPowerOfFour(8) ? "是" : "否") << endl;
        cout << endl;
    }
    
    /**
     * 运行扩展题目测试
     * 包含从各大OJ平台精选的位运算题目
     */
    static void runExtendedProblems() {
        cout << "=== 扩展题目测试 ===" << endl;
        
        // LeetCode 136 - Single Number
        testSingleNumber();
        
        // LeetCode 137 - Single Number II
        testSingleNumberII();
        
        // LeetCode 260 - Single Number III
        testSingleNumberIII();
        
        // LeetCode 191 - Number of 1 Bits
        testNumberOf1Bits();
        
        // LeetCode 338 - Counting Bits
        testCountingBits();
        
        // LeetCode 190 - Reverse Bits
        testReverseBits();
        
        // LeetCode 231 - Power of Two
        testPowerOfTwo();
        
        // LeetCode 342 - Power of Four
        testPowerOfFour();
        
        // LeetCode 268 - Missing Number
        testMissingNumber();
        
        // LeetCode 371 - Sum of Two Integers
        testSumOfTwoIntegers();
        
        // LeetCode 201 - Bitwise AND of Numbers Range
        testBitwiseANDOfNumbersRange();
        
        // LeetCode 477 - Total Hamming Distance
        testTotalHammingDistance();
        
        cout << "=== 扩展题目测试完成 ===" << endl;
    }

    /**
     * 1. LeetCode 190. Reverse Bits (颠倒二进制位)
     * 题目链接: https://leetcode.com/problems/reverse-bits/
     * 题目描述: 颠倒给定的 32 位无符号整数的二进制位
     * 时间复杂度: O(1) - 固定32次循环
     * 空间复杂度: O(1)
     */
    static unsigned int reverseBits(unsigned int n) {
        unsigned int result = 0;
        for (int i = 0; i < 32; i++) {
            result = (result << 1) | (n & 1);
            n >>= 1;
        }
        return result;
    }

    /**
     * 2. LeetCode 191. Number of 1 Bits (位1的个数)
     * 题目链接: https://leetcode.com/problems/number-of-1-bits/
     * 题目描述: 编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 '1' 的个数
     * 时间复杂度: O(k)，k为1的个数
     * 空间复杂度: O(1)
     */
    static int hammingWeight(unsigned int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1);
            count++;
        }
        return count;
    }

    /**
     * 3. LeetCode 231. Power of Two (2的幂)
     * 题目链接: https://leetcode.com/problems/power-of-two/
     * 题目描述: 给定一个整数，编写一个函数来判断它是否是 2 的幂次方
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    static bool isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * 4. LeetCode 342. Power of Four (4的幂)
     * 题目链接: https://leetcode.com/problems/power-of-four/
     * 题目描述: 给定一个整数，写一个函数来判断它是否是 4 的幂次方
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    static bool isPowerOfFour(int n) {
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }

    /**
     * 5. LeetCode 136. Single Number (只出现一次的数字)
     * 题目链接: https://leetcode.com/problems/single-number/
     * 题目描述: 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    static int singleNumber(vector<int>& nums) {
        if (nums.empty()) {
            throw invalid_argument("数组不能为空");
        }
        
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
    
    static void testSingleNumber() {
        cout << "\n=== LeetCode 136 - Single Number 测试 ===" << endl;
        vector<int> nums1 = {2, 2, 1};
        vector<int> nums2 = {4, 1, 2, 1, 2};
        vector<int> nums3 = {1};
        
        cout << "测试用例1: ";
        for (int num : nums1) cout << num << " ";
        cout << "-> " << singleNumber(nums1) << endl;
        
        cout << "测试用例2: ";
        for (int num : nums2) cout << num << " ";
        cout << "-> " << singleNumber(nums2) << endl;
        
        cout << "测试用例3: ";
        for (int num : nums3) cout << num << " ";
        cout << "-> " << singleNumber(nums3) << endl;
    }

    /**
     * 6. LeetCode 137. Single Number II (只出现一次的数字 II)
     * 题目链接: https://leetcode.com/problems/single-number-ii/
     * 题目描述: 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现三次。找出那个只出现了一次的元素
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    static int singleNumberII(vector<int>& nums) {
        if (nums.empty()) {
            throw invalid_argument("数组不能为空");
        }
        
        int result = 0;
        for (int i = 0; i < 32; i++) {
            int sum = 0;
            for (int num : nums) {
                sum += (num >> i) & 1;
            }
            if (sum % 3 != 0) {
                result |= (1 << i);
            }
        }
        return result;
    }
    
    static void testSingleNumberII() {
        cout << "\n=== LeetCode 137 - Single Number II 测试 ===" << endl;
        vector<int> nums1 = {2, 2, 3, 2};
        vector<int> nums2 = {0, 1, 0, 1, 0, 1, 99};
        
        cout << "测试用例1: ";
        for (int num : nums1) cout << num << " ";
        cout << "-> " << singleNumberII(nums1) << endl;
        
        cout << "测试用例2: ";
        for (int num : nums2) cout << num << " ";
        cout << "-> " << singleNumberII(nums2) << endl;
    }

    /**
     * 7. LeetCode 260. Single Number III (只出现一次的数字 III)
     * 题目链接: https://leetcode.com/problems/single-number-iii/
     * 题目描述: 给定一个整数数组，其中恰好有两个元素只出现一次，其余所有元素均出现两次。找出只出现一次的那两个元素
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    static vector<int> singleNumberIII(vector<int>& nums) {
        if (nums.size() < 2) {
            throw invalid_argument("数组长度至少为2");
        }
        
        // 第一步：计算所有数字的异或
        int xorResult = 0;
        for (int num : nums) {
            xorResult ^= num;
        }
        
        // 第二步：找到最右边的1
        int rightmostOne = xorResult & (-xorResult);
        
        // 第三步：根据这个位将数组分成两组
        vector<int> result(2, 0);
        for (int num : nums) {
            if ((num & rightmostOne) == 0) {
                result[0] ^= num;
            } else {
                result[1] ^= num;
            }
        }
        
        return result;
    }
    
    static void testSingleNumberIII() {
        cout << "\n=== LeetCode 260 - Single Number III 测试 ===" << endl;
        vector<int> nums1 = {1, 2, 1, 3, 2, 5};
        vector<int> result = singleNumberIII(nums1);
        
        cout << "测试用例: ";
        for (int num : nums1) cout << num << " ";
        cout << "-> ";
        for (int num : result) cout << num << " ";
        cout << endl;
    }

    /**
     * 8. LeetCode 338. Counting Bits (比特位计数)
     * 题目链接: https://leetcode.com/problems/counting-bits/
     * 题目描述: 给定一个非负整数 num。对于 0 ≤ i ≤ num 范围中的每个数字 i ，计算其二进制数中的 1 的数目并将它们作为数组返回
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    static vector<int> countingBits(int n) {
        vector<int> bits(n + 1, 0);
        for (int i = 1; i <= n; i++) {
            bits[i] = bits[i >> 1] + (i & 1);
        }
        return bits;
    }
    
    static void testCountingBits() {
        cout << "\n=== LeetCode 338 - Counting Bits 测试 ===" << endl;
        vector<int> result = countingBits(5);
        cout << "0到5的1的个数: ";
        for (int bit : result) {
            cout << bit << " ";
        }
        cout << endl;
    }

    /**
     * 9. LeetCode 190 - Reverse Bits (颠倒二进制位)
     * 题目链接: https://leetcode.com/problems/reverse-bits/
     * 题目描述: 颠倒给定的 32 位无符号整数的二进制位
     * 时间复杂度: O(1) - 固定32次循环
     * 空间复杂度: O(1)
     */
    static uint32_t reverseBits190(uint32_t n) {
        uint32_t result = 0;
        for (int i = 0; i < 32; i++) {
            result = (result << 1) | (n & 1);
            n >>= 1;
        }
        return result;
    }
    
    static void testReverseBits() {
        cout << "\n=== LeetCode 190 - Reverse Bits 测试 ===" << endl;
        uint32_t n = 43261596; // 00000010100101000001111010011100
        uint32_t reversed = reverseBits190(n);
        cout << "原数字: " << n << ", 颠倒后: " << reversed << endl;
    }

    /**
     * 10. LeetCode 231 - Power of Two (2的幂)
     * 题目链接: https://leetcode.com/problems/power-of-two/
     * 题目描述: 给定一个整数，编写一个函数来判断它是否是 2 的幂次方
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    static bool powerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    static void testPowerOfTwo() {
        cout << "\n=== LeetCode 231 - Power of Two 测试 ===" << endl;
        cout << "8是2的幂: " << (powerOfTwo(8) ? "是" : "否") << endl;
        cout << "10是2的幂: " << (powerOfTwo(10) ? "是" : "否") << endl;
    }

    /**
     * 11. LeetCode 342 - Power of Four (4的幂)
     * 题目链接: https://leetcode.com/problems/power-of-four/
     * 题目描述: 给定一个整数，写一个函数来判断它是否是 4 的幂次方
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    static bool powerOfFour(int n) {
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }
    
    static void testPowerOfFour() {
        cout << "\n=== LeetCode 342 - Power of Four 测试 ===" << endl;
        cout << "16是4的幂: " << (powerOfFour(16) ? "是" : "否") << endl;
        cout << "8是4的幂: " << (powerOfFour(8) ? "是" : "否") << endl;
    }

    /**
     * 12. LeetCode 268 - Missing Number (缺失数字)
     * 题目链接: https://leetcode.com/problems/missing-number/
     * 题目描述: 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    static int missingNumber(vector<int>& nums) {
        int missing = nums.size();
        for (int i = 0; i < nums.size(); i++) {
            missing ^= i ^ nums[i];
        }
        return missing;
    }
    
    static void testMissingNumber() {
        cout << "\n=== LeetCode 268 - Missing Number 测试 ===" << endl;
        vector<int> nums1 = {3, 0, 1};
        vector<int> nums2 = {0, 1};
        cout << "数组";
        for (int num : nums1) cout << num << " ";
        cout << "缺失的数字: " << missingNumber(nums1) << endl;
        cout << "数组";
        for (int num : nums2) cout << num << " ";
        cout << "缺失的数字: " << missingNumber(nums2) << endl;
    }

    /**
     * 13. LeetCode 371 - Sum of Two Integers (两整数之和)
     * 题目链接: https://leetcode.com/problems/sum-of-two-integers/
     * 题目描述: 不使用运算符 + 和 - ，计算两整数 a 、b 之和
     * 时间复杂度: O(1) - 最多32次循环
     * 空间复杂度: O(1)
     */
    static int sumOfTwoIntegers(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;  // 进位
            a = a ^ b;                // 无进位和
            b = carry;
        }
        return a;
    }
    
    static void testSumOfTwoIntegers() {
        cout << "\n=== LeetCode 371 - Sum of Two Integers 测试 ===" << endl;
        cout << "1 + 2 = " << sumOfTwoIntegers(1, 2) << endl;
        cout << "15 + 7 = " << sumOfTwoIntegers(15, 7) << endl;
    }

    /**
     * 14. LeetCode 201 - Bitwise AND of Numbers Range (数字范围按位与)
     * 题目链接: https://leetcode.com/problems/bitwise-and-of-numbers-range/
     * 题目描述: 给定范围 [m, n]，其中 0 <= m <= n <= 2147483647，返回此范围内所有数字的按位与（包含 m, n 两端点）
     * 时间复杂度: O(1) - 最多32次循环
     * 空间复杂度: O(1)
     */
    static int bitwiseANDOfNumbersRange(int m, int n) {
        int shift = 0;
        while (m < n) {
            m >>= 1;
            n >>= 1;
            shift++;
        }
        return m << shift;
    }
    
    static void testBitwiseANDOfNumbersRange() {
        cout << "\n=== LeetCode 201 - Bitwise AND of Numbers Range 测试 ===" << endl;
        cout << "[5, 7]的按位与: " << bitwiseANDOfNumbersRange(5, 7) << endl;
        cout << "[0, 1]的按位与: " << bitwiseANDOfNumbersRange(0, 1) << endl;
    }

    /**
     * 15. LeetCode 477 - Total Hamming Distance (汉明距离总和)
     * 题目链接: https://leetcode.com/problems/total-hamming-distance/
     * 题目描述: 两个整数的 汉明距离 指的是这两个数字的二进制数对应位不同的数量。给你一个整数数组 nums，请你求出数组中任意两个数之间汉明距离的总和
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    static int totalHammingDistance(vector<int>& nums) {
        int total = 0;
        int n = nums.size();
        for (int i = 0; i < 32; i++) {
            int countOnes = 0;
            for (int num : nums) {
                countOnes += (num >> i) & 1;
            }
            total += countOnes * (n - countOnes);
        }
        return total;
    }
    
    static void testTotalHammingDistance() {
        cout << "\n=== LeetCode 477 - Total Hamming Distance 测试 ===" << endl;
        vector<int> nums = {4, 14, 2};
        cout << "数组";
        for (int num : nums) cout << num << " ";
        cout << "的汉明距离总和: " << totalHammingDistance(nums) << endl;
    }
    
    /**
     * 16. LintCode 83. Single Number II (落单的数 II)
     * 题目链接: https://www.lintcode.com/problem/single-number-ii/description
     * 题目描述: 给出3*n + 1 个的数字，除其中一个数字之外其他每个数字均出现三次，找到这个数字
     * 时间复杂度: O(n) - 遍历数组
     * 空间复杂度: O(1) - 只使用常数额外空间
     */
    static int singleNumberII_LintCode(vector<int>& A) {
        return singleNumberII(A); // 与LeetCode 137相同
    }
    
    /**
     * 17. LintCode 84. Single Number III (落单的数 III)
     * 题目链接: https://www.lintcode.com/problem/single-number-iii/description
     * 题目描述: 给出2*n + 2个的数字，除其中两个数字之外其他每个数字均出现两次，找到这两个数字
     * 时间复杂度: O(n) - 遍历数组两次
     * 空间复杂度: O(1) - 只使用常数额外空间
     */
    static vector<int> singleNumberIII_LintCode(vector<int>& A) {
        return singleNumberIII(A); // 与LeetCode 260相同
    }
    
    /**
     * 18. Codeforces 449B. Jzzhu and Cities
     * 题目链接: https://codeforces.com/problemset/problem/449/B
     * 题目描述: 使用位掩码优化的Dijkstra算法题目（简化版本）
     * 时间复杂度: O(m log n)
     * 空间复杂度: O(n + m)
     */
    static long long bitmaskDijkstraExample() {
        // 此处为示例代码框架，完整实现需要具体图数据
        return 0;
    }
    
    /**
     * 19. AtCoder ABC086A - Product
     * 题目链接: https://atcoder.jp/contests/abc086/tasks/abc086_a
     * 题目描述: 判断两个整数的乘积是奇数还是偶数
     * 时间复杂度: O(1) - 常数时间操作
     * 空间复杂度: O(1) - 只使用常数额外空间
     */
    static string isProductEven(int a, int b) {
        // 两个数都是奇数时乘积才是奇数，否则是偶数
        // 奇数的最低位是1
        return ((a & 1) == 1 && (b & 1) == 1) ? "Odd" : "Even";
    }
    
    /**
     * 20. UVa 11019 - Matrix Matcher
     * 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1960
     * 题目描述: 使用位掩码进行矩阵匹配（简化版本）
     * 时间复杂度: O(n*m)
     * 空间复杂度: O(n)
     */
    static int matrixMatcherExample() {
        // 此处为示例代码框架
        return 0;
    }
    
    /**
     * 21. HackerRank XOR Strings 2
     * 题目链接: https://www.hackerrank.com/challenges/xor-strings-2/problem
     * 题目描述: 对两个字符串进行逐字符异或操作
     * 时间复杂度: O(n) - 字符串长度
     * 空间复杂度: O(n) - 存储结果
     */
    static string xorStrings(string s, string t) {
        string result = "";
        for (int i = 0; i < s.length(); i++) {
            // 字符异或
            result += (char) (s[i] ^ t[i]);
        }
        return result;
    }
    
    /**
     * 22. POJ 1995 Raising Modulo Numbers
     * 题目链接: https://poj.org/problem?id=1995
     * 题目描述: 使用快速幂算法计算模幂
     * 时间复杂度: O(log b) - 快速幂
     * 空间复杂度: O(1) - 只使用常数额外空间
     */
    static long long powMod(long long a, long long b, long long mod) {
        long long result = 1;
        a %= mod;
        while (b > 0) {
            // 如果b是奇数，将当前a乘到结果中
            if ((b & 1) == 1) {
                result = (result * a) % mod;
            }
            // a自乘，b右移一位
            a = (a * a) % mod;
            b >>= 1;
        }
        return result;
    }
    
    /**
     * 23. 剑指Offer 15. 二进制中1的个数
     * 题目链接: https://leetcode.cn/problems/er-jin-zhi-zhong-1de-ge-shu-lcof/
     * 题目描述: 请实现一个函数，输入一个整数（以二进制串形式），输出该数二进制表示中 1 的个数
     * 时间复杂度: O(1) - 最多循环32次
     * 空间复杂度: O(1) - 只使用常数额外空间
     */
    static int hammingWeightOptimized(unsigned int n) {
        int count = 0;
        // 每执行一次n = n & (n - 1)，就会将n的最后一个1变成0
        while (n != 0) {
            count++;
            n &= n - 1;
        }
        return count;
    }
    
    /**
     * 24. 牛客网 NC103 反转字符串
     * 题目链接: https://www.nowcoder.com/practice/c3a6afee325e472386a1c4eb1ef987f3
     * 题目描述: 使用位运算交换字符（位运算应用）
     * 时间复杂度: O(n) - 字符串长度
     * 空间复杂度: O(n) - 存储结果数组
     */
    static string reverseStringWithXOR(string s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            // 使用异或交换两个字符
            s[left] ^= s[right];
            s[right] ^= s[left];
            s[left] ^= s[right];
            left++;
            right--;
        }
        return s;
    }
    
    /**
     * 25. HDU 1013 Digital Roots
     * 题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=1013
     * 题目描述: 计算数字根（位运算优化）
     * 时间复杂度: O(n) - 字符串长度
     * 空间复杂度: O(1) - 只使用常数额外空间
     */
    static int digitalRoot(string num) {
        int sum = 0;
        for (char c : num) {
            sum += c - '0';
        }
        // 使用位运算计算数字根
        // 数字根 = 1 + ((sum - 1) % 9)
        return sum == 0 ? 0 : 1 + ((sum - 1) % 9);
    }
    
    /**
     * 26. LeetCode 1310. XOR Queries of a Subarray (子数组异或查询)
     * 题目链接: https://leetcode.com/problems/xor-queries-of-a-subarray/
     * 题目描述: 给你一个正整数数组 arr，你需要处理以下两种类型的查询：
     *          1. 计算从索引 L 到 R 的元素的异或值
     * 时间复杂度: O(n + q) - n为数组长度，q为查询次数
     * 空间复杂度: O(n) - 前缀异或数组
     * 
     * 解题思路：
     * 使用前缀异或数组优化多次查询：
     * 1. 构建前缀异或数组 prefix，其中 prefix[i] = arr[0] ^ arr[1] ^ ... ^ arr[i-1]
     * 2. 对于查询 [L, R]，结果为 prefix[R+1] ^ prefix[L]
     */
    static vector<int> xorQueries(vector<int>& arr, vector<vector<int>>& queries) {
        int n = arr.size();
        vector<int> prefix(n + 1, 0);
        
        // 构建前缀异或数组
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] ^ arr[i];
        }
        
        vector<int> result(queries.size());
        
        // 处理每个查询
        for (int i = 0; i < queries.size(); i++) {
            int left = queries[i][0];
            int right = queries[i][1];
            result[i] = prefix[right + 1] ^ prefix[left];
        }
        
        return result;
    }
    
    static void testXorQueries() {
        cout << "\n=== LeetCode 1310 - XOR Queries of a Subarray 测试 ===" << endl;
        vector<int> arr = {1, 3, 4, 8};
        vector<vector<int>> queries = {{0, 1}, {1, 2}, {0, 3}, {3, 3}};
        vector<int> result = xorQueries(arr, queries);
        
        cout << "数组: ";
        for (int num : arr) cout << num << " ";
        cout << endl;
        
        cout << "查询结果: ";
        for (int res : result) cout << res << " ";
        cout << endl;
    }
    
    /**
     * 27. LeetCode 2220. Minimum Bit Flips to Convert Number (转换数字的最少位翻转次数)
     * 题目链接: https://leetcode.com/problems/minimum-bit-flips-to-convert-number/
     * 题目描述: 一次位翻转定义为将数字 x 二进制中的一个位进行翻转操作，即将 0 变成 1 ，或者将 1 变成 0 。
     *          给你两个整数 start 和 goal ，请你返回将 start 转变成 goal 的最少位翻转次数。
     * 时间复杂度: O(1) - 固定32位比较
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * 解题思路：
     * 计算两个数字的汉明距离，即异或结果中1的个数
     */
    static int minBitFlips(int start, int goal) {
        // 计算异或结果中1的个数
        return __builtin_popcount(start ^ goal);
    }
    
    static void testMinBitFlips() {
        cout << "\n=== LeetCode 2220 - Minimum Bit Flips to Convert Number 测试 ===" << endl;
        cout << "start=10, goal=7 的最少位翻转次数: " << minBitFlips(10, 7) << endl;
        cout << "start=3, goal=4 的最少位翻转次数: " << minBitFlips(3, 4) << endl;
    }
    
    /**
     * 28. LeetCode 2433. Find The Original Array of Prefix Xor (找出前缀异或的原始数组)
     * 题目链接: https://leetcode.com/problems/find-the-original-array-of-prefix-xor/
     * 题目描述: 给你一个长度为 n 的整数数组 pref。找出并返回满足以下条件且长度为 n 的数组 arr：
     *          pref[i] = arr[0] ^ arr[1] ^ ... ^ arr[i].
     * 时间复杂度: O(n) - 遍历数组一次
     * 空间复杂度: O(n) - 结果数组空间
     * 
     * 解题思路：
     * 根据异或的性质，如果 pref[i] = arr[0] ^ arr[1] ^ ... ^ arr[i]
     * 那么 arr[i] = pref[i] ^ pref[i-1] (i > 0)
     * arr[0] = pref[0]
     */
    static vector<int> findArray(vector<int>& pref) {
        int n = pref.size();
        vector<int> arr(n);
        
        // 第一个元素就是前缀异或的第一个元素
        arr[0] = pref[0];
        
        // 根据公式计算其他元素
        for (int i = 1; i < n; i++) {
            arr[i] = pref[i] ^ pref[i - 1];
        }
        
        return arr;
    }
    
    static void testFindArray() {
        cout << "\n=== LeetCode 2433 - Find The Original Array of Prefix Xor 测试 ===" << endl;
        vector<int> pref = {5, 2, 0, 3, 1};
        vector<int> result = findArray(pref);
        
        cout << "前缀异或数组: ";
        for (int num : pref) cout << num << " ";
        cout << endl;
        
        cout << "原始数组: ";
        for (int num : result) cout << num << " ";
        cout << endl;
    }
    
    /**
     * 29. LeetCode 868. Binary Gap (二进制间距)
     * 题目链接: https://leetcode.com/problems/binary-gap/
     * 题目描述: 给定一个正整数 n，找到并返回 n 的二进制表示中两个相邻 1 之间的最长距离。
     *          如果不存在两个相邻的 1，返回 0。
     * 时间复杂度: O(log n) - 遍历二进制位
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * 解题思路：
     * 遍历二进制表示，记录相邻1之间的距离
     */
    static int binaryGap(int n) {
        int maxGap = 0;
        int lastPos = -1;
        int pos = 0;
        
        while (n > 0) {
            if ((n & 1) == 1) {
                if (lastPos != -1) {
                    maxGap = max(maxGap, pos - lastPos);
                }
                lastPos = pos;
            }
            pos++;
            n >>= 1;
        }
        
        return maxGap;
    }
    
    static void testBinaryGap() {
        cout << "\n=== LeetCode 868 - Binary Gap 测试 ===" << endl;
        cout << "n=22 的二进制间距: " << binaryGap(22) << endl;
        cout << "n=8 的二进制间距: " << binaryGap(8) << endl;
        cout << "n=5 的二进制间距: " << binaryGap(5) << endl;
    }
    
    /**
     * 30. LeetCode 1009. Complement of Base 10 Integer (十进制整数的反码)
     * 题目链接: https://leetcode.com/problems/complement-of-base-10-integer/
     * 题目描述: 每个非负整数 N 都有其二进制表示。例如，5 可以被表示为二进制 "101"，11 可以用二进制 "1011" 表示，依此类推。
     *          注意，除 N = 0 外，任何二进制表示中都不含前导零。
     *          二进制的反码表示是将每个 1 改为 0 且每个 0 改为 1。例如，二进制数 "101" 的二进制反码为 "010"。
     *          给你一个十进制数 N，请你返回其二进制表示的反码所对应的十进制整数。
     * 时间复杂度: O(log n) - 构造掩码
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * 解题思路：
     * 1. 构造一个掩码，该掩码的位数与n相同，但所有位都是1
     * 2. 使用异或操作取反
     */
    static int bitwiseComplement(int n) {
        if (n == 0) return 1;
        
        int mask = 1;
        // 构造一个掩码，该掩码的位数与n相同，但所有位都是1
        while (mask < n) {
            mask = (mask << 1) | 1;
        }
        // 使用异或操作取反
        return n ^ mask;
    }
    
    static void testBitwiseComplement() {
        cout << "\n=== LeetCode 1009 - Complement of Base 10 Integer 测试 ===" << endl;
        cout << "n=5 的反码: " << bitwiseComplement(5) << endl;
        cout << "n=7 的反码: " << bitwiseComplement(7) << endl;
        cout << "n=10 的反码: " << bitwiseComplement(10) << endl;
    }
};

int main() {
    BinarySystem::main();
    return 0;
}