#include <iostream>
#include <vector>
#include <bitset>
using namespace std;

/**
 * 位运算技巧大全
 * 测试链接：综合题目，展示各种位运算技巧的实际应用
 * 
 * 题目描述：
 * 本文件包含各种实用的位运算技巧，涵盖基础操作、高级技巧和实际应用场景。
 * 
 * 解题思路：
 * 位运算技巧的核心是利用二进制表示的特性，通过位操作实现高效的计算和数据处理。
 * 
 * 时间复杂度：各种技巧的时间复杂度不同，但通常为O(1)或O(log n)
 * 空间复杂度：O(1) - 只使用常数个变量
 */
class BitManipulationTricks {
public:
    // ==================== 基础位操作技巧 ====================
    
    /**
     * 判断一个数是否是2的幂
     * 原理：2的幂在二进制中只有一个1
     */
    static bool isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    /**
     * 判断一个数是否是4的幂
     * 原理：4的幂在二进制中只有一个1，且1出现在奇数位
     */
    static bool isPowerOfFour(int n) {
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }
    
    /**
     * 计算一个数的二进制表示中1的个数（Brian Kernighan算法）
     */
    static int countOnes(int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1); // 清除最右边的1
            count++;
        }
        return count;
    }
    
    /**
     * 计算两个数的汉明距离（不同位的个数）
     */
    static int hammingDistance(int x, int y) {
        return countOnes(x ^ y);
    }
    
    /**
     * 反转一个整数的二进制位（32位）
     */
    static unsigned int reverseBits(unsigned int n) {
        unsigned int result = 0;
        for (int i = 0; i < 32; i++) {
            result <<= 1;
            result |= (n & 1);
            n >>= 1;
        }
        return result;
    }
    
    // ==================== 高级位操作技巧 ====================
    
    /**
     * 不使用临时变量交换两个数
     */
    static void swap(int& a, int& b) {
        cout << "交换前: a = " << a << ", b = " << b << endl;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        cout << "交换后: a = " << a << ", b = " << b << endl;
    }
    
    /**
     * 找到只出现一次的数字（其他数字都出现两次）
     */
    static int singleNumber(vector<int>& nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
    
    /**
     * 找到只出现一次的数字（其他数字都出现三次）
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
     * 找到数组中缺失的数字（0到n中缺失一个）
     */
    static int missingNumber(vector<int>& nums) {
        int n = nums.size();
        int result = n; // 因为0到n有n+1个数，但数组只有n个
        for (int i = 0; i < n; i++) {
            result ^= i ^ nums[i];
        }
        return result;
    }
    
    // ==================== 位掩码技巧 ====================
    
    /**
     * 使用位掩码表示集合（子集生成）
     */
    static vector<vector<int>> subsets(vector<int>& nums) {
        vector<vector<int>> result;
        int n = nums.size();
        int total = 1 << n;
        
        for (int mask = 0; mask < total; mask++) {
            vector<int> subset;
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    subset.push_back(nums[i]);
                }
            }
            result.push_back(subset);
        }
        return result;
    }
    
    /**
     * 判断一个数是否包含特定的位模式
     */
    static bool hasBitPattern(int num, int pattern) {
        return (num & pattern) == pattern;
    }
    
    /**
     * 设置特定位为1
     */
    static int setBit(int num, int pos) {
        return num | (1 << pos);
    }
    
    /**
     * 清除特定位（设为0）
     */
    static int clearBit(int num, int pos) {
        return num & ~(1 << pos);
    }
    
    /**
     * 切换特定位（0变1，1变0）
     */
    static int toggleBit(int num, int pos) {
        return num ^ (1 << pos);
    }
    
    /**
     * 检查特定位是否为1
     */
    static bool checkBit(int num, int pos) {
        return (num & (1 << pos)) != 0;
    }
    
    // ==================== 位运算在算法中的应用 ====================
    
    /**
     * 使用位运算实现加法
     */
    static int add(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;
            a = a ^ b;
            b = carry;
        }
        return a;
    }
    
    /**
     * 使用位运算实现减法
     */
    static int subtract(int a, int b) {
        return add(a, add(~b, 1));
    }
    
    /**
     * 快速判断奇偶性
     */
    static bool isOdd(int n) {
        return (n & 1) == 1;
    }
    
    /**
     * 快速计算绝对值（32位整数）
     */
    static int abs(int n) {
        int mask = n >> 31;
        return (n + mask) ^ mask;
    }
    
    /**
     * 快速计算2的n次方
     */
    static int powerOfTwo(int n) {
        return 1 << n;
    }
    
    /**
     * 快速判断是否是2的幂的倍数
     */
    static bool isMultipleOfPowerOfTwo(int n, int power) {
        return (n & ((1 << power) - 1)) == 0;
    }
    
    // ==================== 位运算在优化中的应用 ====================
    
    /**
     * 快速计算log2（整数部分）
     */
    static int log2(int n) {
        if (n <= 0) return -1;
        return 31 - __builtin_clz(n);
    }
    
    /**
     * 快速计算下一个2的幂（大于等于n的最小2的幂）
     */
    static int nextPowerOfTwo(int n) {
        if (n <= 0) return 1;
        n--;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n + 1;
    }
    
    /**
     * 快速计算前一个2的幂（小于等于n的最大2的幂）
     */
    static int prevPowerOfTwo(int n) {
        if (n <= 0) return 0;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n - (n >> 1);
    }
};

// ==================== 布隆过滤器实现 ====================

/**
 * 使用位集实现布隆过滤器（简化版）
 */
class BloomFilter {
private:
    vector<int> bitmap;
    int size;
    
public:
    BloomFilter(int size) : size(size) {
        bitmap.resize((size + 31) / 32, 0);
    }
    
    void add(int value) {
        int hash1 = hash1(value);
        int hash2 = hash2(value);
        int hash3 = hash3(value);
        
        setBit(hash1 % size);
        setBit(hash2 % size);
        setBit(hash3 % size);
    }
    
    bool contains(int value) {
        int hash1 = hash1(value);
        int hash2 = hash2(value);
        int hash3 = hash3(value);
        
        return checkBit(hash1 % size) && 
               checkBit(hash2 % size) && 
               checkBit(hash3 % size);
    }
    
private:
    void setBit(int pos) {
        int index = pos / 32;
        int bit = pos % 32;
        bitmap[index] |= (1 << bit);
    }
    
    bool checkBit(int pos) {
        int index = pos / 32;
        int bit = pos % 32;
        return (bitmap[index] & (1 << bit)) != 0;
    }
    
    int hash1(int value) {
        return value * 31;
    }
    
    int hash2(int value) {
        return value * 17 + 12345;
    }
    
    int hash3(int value) {
        return value * 13 + 67890;
    }
};

// ==================== 测试函数 ====================

int main() {
    cout << "=== 基础位操作测试 ===" << endl;
    cout << "8是2的幂: " << BitManipulationTricks::isPowerOfTwo(8) << endl; // true
    cout << "16是4的幂: " << BitManipulationTricks::isPowerOfFour(16) << endl; // true
    cout << "5的二进制中1的个数: " << BitManipulationTricks::countOnes(5) << endl; // 2
    cout << "1和4的汉明距离: " << BitManipulationTricks::hammingDistance(1, 4) << endl; // 2
    
    cout << "\n=== 高级位操作测试 ===" << endl;
    int a = 5, b = 3;
    BitManipulationTricks::swap(a, b);
    vector<int> nums1 = {2, 2, 1};
    cout << "只出现一次的数字: " << BitManipulationTricks::singleNumber(nums1) << endl; // 1
    vector<int> nums2 = {0, 1, 3};
    cout << "缺失的数字: " << BitManipulationTricks::missingNumber(nums2) << endl; // 2
    
    cout << "\n=== 位掩码测试 ===" << endl;
    vector<int> nums3 = {1, 2, 3};
    cout << "子集数量: " << BitManipulationTricks::subsets(nums3).size() << endl; // 8
    cout << "5包含模式101: " << BitManipulationTricks::hasBitPattern(5, 5) << endl; // true
    
    cout << "\n=== 算法应用测试 ===" << endl;
    cout << "5 + 3 = " << BitManipulationTricks::add(5, 3) << endl; // 8
    cout << "5 - 3 = " << BitManipulationTricks::subtract(5, 3) << endl; // 2
    cout << "5是奇数: " << BitManipulationTricks::isOdd(5) << endl; // true
    cout << "-5的绝对值: " << BitManipulationTricks::abs(-5) << endl; // 5
    
    cout << "\n=== 优化技巧测试 ===" << endl;
    cout << "log2(8) = " << BitManipulationTricks::log2(8) << endl; // 3
    cout << "15的下一个2的幂: " << BitManipulationTricks::nextPowerOfTwo(15) << endl; // 16
    cout << "15的前一个2的幂: " << BitManipulationTricks::prevPowerOfTwo(15) << endl; // 8
    
    cout << "\n=== 布隆过滤器测试 ===" << endl;
    BloomFilter filter(100);
    filter.add(42);
    filter.add(123);
    cout << "过滤器包含42: " << filter.contains(42) << endl; // true
    cout << "过滤器包含456: " << filter.contains(456) << endl; // false（可能误判）
    
    return 0;
}