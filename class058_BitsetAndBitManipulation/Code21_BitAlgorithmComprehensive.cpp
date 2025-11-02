// 综合位算法实现 - 简化版本
// 不使用标准库，只使用基本C语言特性

#include <stdio.h>

// 综合位算法实现类
class BitAlgorithmComprehensive {
public:
    // LeetCode 191 - 位1的个数
    static int hammingWeight(unsigned int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1);
            count++;
        }
        return count;
    }
    
    // LeetCode 231 - 2的幂
    static bool isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    // LeetCode 342 - 4的幂
    static bool isPowerOfFour(int n) {
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }
    
    // LeetCode 268 - 丢失的数字
    static int missingNumber(int nums[], int n) {
        int result = n;
        for (int i = 0; i < n; i++) {
            result ^= i;
            result ^= nums[i];
        }
        return result;
    }
    
    // LeetCode 136 - 只出现一次的数字
    static int singleNumber(int nums[], int n) {
        int result = 0;
        for (int i = 0; i < n; i++) {
            result ^= nums[i];
        }
        return result;
    }
    
    // LeetCode 137 - 只出现一次的数字 II
    static int singleNumberII(int nums[], int n) {
        int ones = 0, twos = 0;
        for (int i = 0; i < n; i++) {
            ones = (ones ^ nums[i]) & ~twos;
            twos = (twos ^ nums[i]) & ~ones;
        }
        return ones;
    }
    
    // LeetCode 260 - 只出现一次的数字 III
    static void singleNumberIII(int nums[], int n, int* num1, int* num2) {
        int xor_result = 0;
        for (int i = 0; i < n; i++) {
            xor_result ^= nums[i];
        }
        
        int diff_bit = xor_result & -xor_result;
        
        *num1 = 0;
        *num2 = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] & diff_bit) {
                *num1 ^= nums[i];
            } else {
                *num2 ^= nums[i];
            }
        }
    }
    
    // LeetCode 190 - 颠倒二进制位
    static unsigned int reverseBits(unsigned int n) {
        n = (n >> 16) | (n << 16);
        n = ((n & 0xff00ff00) >> 8) | ((n & 0x00ff00ff) << 8);
        n = ((n & 0xf0f0f0f0) >> 4) | ((n & 0x0f0f0f0f) << 4);
        n = ((n & 0xcccccccc) >> 2) | ((n & 0x33333333) << 2);
        n = ((n & 0xaaaaaaaa) >> 1) | ((n & 0x55555555) << 1);
        return n;
    }
    
    // LeetCode 338 - 比特位计数
    static void countBits(int n, int result[]) {
        for (int i = 1; i <= n; i++) {
            result[i] = result[i >> 1] + (i & 1);
        }
    }
    
    // LeetCode 201 - 数字范围按位与
    static int rangeBitwiseAnd(int m, int n) {
        int shift = 0;
        while (m < n) {
            m >>= 1;
            n >>= 1;
            shift++;
        }
        return m << shift;
    }
};

// 测试函数
int main() {
    printf("综合位算法实现测试
");
    printf("==================
");
    
    // 测试位1的个数
    printf("位1的个数测试:
");
    printf("数字11(1011)的1的个数: %d
", BitAlgorithmComprehensive::hammingWeight(11));
    
    // 测试2的幂
    printf("
2的幂测试:
");
    printf("16是2的幂: %s
", BitAlgorithmComprehensive::isPowerOfTwo(16) ? "是" : "否");
    printf("15是2的幂: %s
", BitAlgorithmComprehensive::isPowerOfTwo(15) ? "是" : "否");
    
    // 测试只出现一次的数字
    printf("
只出现一次的数字测试:
");
    int nums[] = {4, 1, 2, 1, 2};
    printf("只出现一次的数字: %d
", BitAlgorithmComprehensive::singleNumber(nums, 5));
    
    // 测试丢失的数字
    printf("
丢失的数字测试:
");
    int nums2[] = {3, 0, 1};
    printf("丢失的数字: %d
", BitAlgorithmComprehensive::missingNumber(nums2, 3));
    
    // 测试只出现一次的数字 III
    printf("
只出现一次的数字 III 测试:
");
    int nums3[] = {1, 2, 1, 3, 2, 5};
    int num1, num2;
    BitAlgorithmComprehensive::singleNumberIII(nums3, 6, &num1, &num2);
    printf("两个只出现一次的数字: %d, %d
", num1, num2);
    
    // 测试颠倒二进制位
    printf("
颠倒二进制位测试:
");
    printf("43261596颠倒后: %u
", BitAlgorithmComprehensive::reverseBits(43261596));
    
    // 测试比特位计数
    printf("
比特位计数测试:
");
    int result[6] = {0};
    BitAlgorithmComprehensive::countBits(5, result);
    printf("比特位计数(0-5): ");
    for (int i = 0; i <= 5; i++) {
        printf("%d ", result[i]);
    }
    printf("
");
    
    // 测试数字范围按位与
    printf("
数字范围按位与测试:
");
    printf("[5,7]按位与结果: %d
", BitAlgorithmComprehensive::rangeBitwiseAnd(5, 7));
    
    return 0;
}

using namespace std;

/**
 * 综合位算法实现
 * 包含LeetCode多个综合位算法相关题目的解决方案
 * 
 * 题目列表:
 * 1. LeetCode 191 - 位1的个数
 * 2. LeetCode 231 - 2的幂
 * 3. LeetCode 342 - 4的幂
 * 4. LeetCode 268 - 丢失的数字
 * 5. LeetCode 136 - 只出现一次的数字
 * 6. LeetCode 137 - 只出现一次的数字 II
 * 7. LeetCode 260 - 只出现一次的数字 III
 * 8. LeetCode 190 - 颠倒二进制位
 * 9. LeetCode 338 - 比特位计数
 * 10. LeetCode 201 - 数字范围按位与
 * 
 * 时间复杂度分析:
 * - 位操作: O(1) 到 O(n)
 * - 空间复杂度: O(1) 到 O(n)
 * 
 * 工程化考量:
 * 1. 位运算优化: 使用位运算替代算术运算
 * 2. 状态压缩: 使用位掩码压缩状态
 * 3. 性能优化: 利用位运算的并行性
 * 4. 边界处理: 处理整数边界、负数等
 */

class BitAlgorithmComprehensive {
public:
    /**
     * LeetCode 191 - 位1的个数
     * 题目链接: https://leetcode.com/problems/number-of-1-bits/
     * 编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
     * 
     * 方法: Brian Kernighan算法
     * 时间复杂度: O(k) - k为1的个数
     * 空间复杂度: O(1)
     * 
     * 原理: n & (n-1) 会清除最低位的1
     */
    static int hammingWeight(uint32_t n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1);
            count++;
        }
        return count;
    }
    
    /**
     * LeetCode 231 - 2的幂
     * 题目链接: https://leetcode.com/problems/power-of-two/
     * 给定一个整数，编写一个函数来判断它是否是 2 的幂次方。
     * 
     * 方法: 位运算
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * 原理: 2的幂的二进制表示中只有1个1
     */
    static bool isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    /**
     * LeetCode 342 - 4的幂
     * 题目链接: https://leetcode.com/problems/power-of-four/
     * 给定一个整数，写一个函数来判断它是否是 4 的幂次方。
     * 
     * 方法: 位运算 + 数学
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * 原理: 4的幂是2的幂，且1出现在奇数位
     */
    static bool isPowerOfFour(int n) {
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }
    
    /**
     * LeetCode 268 - 丢失的数字
     * 题目链接: https://leetcode.com/problems/missing-number/
     * 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。
     * 
     * 方法: 位运算（异或）
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 原理: a ^ a = 0, a ^ 0 = a
     */
    static int missingNumber(vector<int>& nums) {
        int n = nums.size();
        int result = n;  // 因为nums包含0到n-1，所以初始化为n
        
        for (int i = 0; i < n; i++) {
            result ^= i;
            result ^= nums[i];
        }
        
        return result;
    }
    
    /**
     * LeetCode 136 - 只出现一次的数字
     * 题目链接: https://leetcode.com/problems/single-number/
     * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
     * 
     * 方法: 位运算（异或）
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
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
     * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现三次。找出那个只出现了一次的元素。
     * 
     * 方法: 位运算（状态机）
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 原理: 使用两个变量记录状态，模拟三进制计数
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
     * 给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。 找出只出现一次的那两个元素。
     * 
     * 方法: 位运算（分组异或）
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    static vector<int> singleNumberIII(vector<int>& nums) {
        // 获取两个不同数字的异或结果
        int xor_result = 0;
        for (int num : nums) {
            xor_result ^= num;
        }
        
        // 找到最低位的1（两个数字不同的位）
        int diff_bit = xor_result & -xor_result;
        
        // 根据该位分组
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
     * LeetCode 190 - 颠倒二进制位
     * 题目链接: https://leetcode.com/problems/reverse-bits/
     * 颠倒给定的 32 位无符号整数的二进制位。
     * 
     * 方法: 位运算（分治）
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    static uint32_t reverseBits(uint32_t n) {
        n = (n >> 16) | (n << 16);
        n = ((n & 0xff00ff00) >> 8) | ((n & 0x00ff00ff) << 8);
        n = ((n & 0xf0f0f0f0) >> 4) | ((n & 0x0f0f0f0f) << 4);
        n = ((n & 0xcccccccc) >> 2) | ((n & 0x33333333) << 2);
        n = ((n & 0xaaaaaaaa) >> 1) | ((n & 0x55555555) << 1);
        return n;
    }
    
    /**
     * LeetCode 338 - 比特位计数
     * 题目链接: https://leetcode.com/problems/counting-bits/
     * 给定一个非负整数 num。对于 0 ≤ i ≤ num 范围中的每个数字 i ，计算其二进制数中的 1 的数目并将它们作为数组返回。
     * 
     * 方法: 动态规划 + 位运算
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * 原理: i的1的个数 = i/2的1的个数 + i的最低位是否为1
     */
    static vector<int> countBits(int n) {
        vector<int> result(n + 1, 0);
        
        for (int i = 1; i <= n; i++) {
            result[i] = result[i >> 1] + (i & 1);
        }
        
        return result;
    }
    
    /**
     * LeetCode 201 - 数字范围按位与
     * 题目链接: https://leetcode.com/problems/bitwise-and-of-numbers-range/
     * 给定范围 [m, n]，返回此范围内所有数字的按位与（包含 m, n 两端点）。
     * 
     * 方法: 位运算（找公共前缀）
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * 原理: 结果就是m和n的二进制公共前缀
     */
    static int rangeBitwiseAnd(int m, int n) {
        int shift = 0;
        
        // 找到公共前缀
        while (m < n) {
            m >>= 1;
            n >>= 1;
            shift++;
        }
        
        return m << shift;
    }
};

class PerformanceTester {
public:
    static void testHammingWeight() {
        cout << "=== 位1的个数性能测试 ===" << endl;
        
        uint32_t n = 0b10101010101010101010101010101010;
        
        auto start = chrono::high_resolution_clock::now();
        int count = BitAlgorithmComprehensive::hammingWeight(n);
        auto time = chrono::duration_cast<chrono::nanoseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "hammingWeight: 数字" << n << "的1的个数=" << count << ", 耗时=" << time << " ns" << endl;
    }
    
    static void testSingleNumber() {
        cout << "\n=== 只出现一次的数字性能测试 ===" << endl;
        
        vector<int> nums = {4, 1, 2, 1, 2};
        
        auto start = chrono::high_resolution_clock::now();
        int result = BitAlgorithmComprehensive::singleNumber(nums);
        auto time = chrono::duration_cast<chrono::nanoseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "singleNumber: 结果=" << result << ", 耗时=" << time << " ns" << endl;
    }
    
    static void testCountBits() {
        cout << "\n=== 比特位计数性能测试 ===" << endl;
        
        int n = 1000000;
        
        auto start = chrono::high_resolution_clock::now();
        vector<int> result = BitAlgorithmComprehensive::countBits(n);
        auto time = chrono::duration_cast<chrono::milliseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "countBits: n=" << n << ", 耗时=" << time << " ms" << endl;
        cout << "示例结果: [0,1,1,2,1,2,...," << result[n] << "]" << endl;
    }
    
    static void runUnitTests() {
        cout << "=== 综合位算法单元测试 ===" << endl;
        
        // 测试位1的个数
        assert(BitAlgorithmComprehensive::hammingWeight(0b1011) == 3);
        
        // 测试2的幂
        assert(BitAlgorithmComprehensive::isPowerOfTwo(16) == true);
        assert(BitAlgorithmComprehensive::isPowerOfTwo(15) == false);
        
        // 测试只出现一次的数字
        vector<int> nums = {2, 2, 1};
        assert(BitAlgorithmComprehensive::singleNumber(nums) == 1);
        
        cout << "所有单元测试通过!" << endl;
    }
    
    static void complexityAnalysis() {
        cout << "\n=== 复杂度分析 ===" << endl;
        
        vector<pair<string, string>> algorithms = {
            {"hammingWeight", "O(k), O(1)"},
            {"isPowerOfTwo", "O(1), O(1)"},
            {"isPowerOfFour", "O(1), O(1)"},
            {"missingNumber", "O(n), O(1)"},
            {"singleNumber", "O(n), O(1)"},
            {"singleNumberII", "O(n), O(1)"},
            {"singleNumberIII", "O(n), O(1)"},
            {"reverseBits", "O(1), O(1)"},
            {"countBits", "O(n), O(n)"},
            {"rangeBitwiseAnd", "O(1), O(1)"}
        };
        
        for (auto& algo : algorithms) {
            cout << algo.first << ": 时间复杂度=" << algo.second << endl;
        }
    }
};

int main() {
    cout << "综合位算法实现" << endl;
    cout << "包含LeetCode多个综合位算法相关题目的解决方案" << endl;
    cout << "===========================================" << endl;
    
    // 运行单元测试
    PerformanceTester::runUnitTests();
    
    // 运行性能测试
    PerformanceTester::testHammingWeight();
    PerformanceTester::testSingleNumber();
    PerformanceTester::testCountBits();
    
    // 复杂度分析
    PerformanceTester::complexityAnalysis();
    
    // 示例使用
    cout << "\n=== 示例使用 ===" << endl;
    
    // 位1的个数示例
    uint32_t num = 0b1011;
    cout << "数字" << num << "的1的个数: " << BitAlgorithmComprehensive::hammingWeight(num) << endl;
    
    // 2的幂示例
    cout << "16是2的幂: " << (BitAlgorithmComprehensive::isPowerOfTwo(16) ? "是" : "否") << endl;
    
    // 只出现一次的数字示例
    vector<int> nums = {4, 1, 2, 1, 2};
    cout << "只出现一次的数字: " << BitAlgorithmComprehensive::singleNumber(nums) << endl;
    
    // 比特位计数示例
    vector<int> bits = BitAlgorithmComprehensive::countBits(5);
    cout << "比特位计数(0-5): ";
    for (int i = 0; i <= 5; i++) {
        cout << bits[i] << " ";
    }
    cout << endl;
    
    return 0;
}