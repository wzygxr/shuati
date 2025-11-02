package class031;

import java.util.*;

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
public class Code24_BitManipulationTricks {
    
    // ==================== 基础位操作技巧 ====================
    
    /**
     * 判断一个数是否是2的幂
     * 原理：2的幂在二进制中只有一个1
     */
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    /**
     * 判断一个数是否是4的幂
     * 原理：4的幂在二进制中只有一个1，且1出现在奇数位
     */
    public static boolean isPowerOfFour(int n) {
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }
    
    /**
     * 计算一个数的二进制表示中1的个数（Brian Kernighan算法）
     */
    public static int countOnes(int n) {
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
    public static int hammingDistance(int x, int y) {
        return countOnes(x ^ y);
    }
    
    /**
     * 反转一个整数的二进制位（32位）
     */
    public static int reverseBits(int n) {
        int result = 0;
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
    public static void swap(int a, int b) {
        System.out.println("交换前: a = " + a + ", b = " + b);
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println("交换后: a = " + a + ", b = " + b);
    }
    
    /**
     * 找到只出现一次的数字（其他数字都出现两次）
     */
    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
    
    /**
     * 找到只出现一次的数字（其他数字都出现三次）
     */
    public static int singleNumberII(int[] nums) {
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
    public static int missingNumber(int[] nums) {
        int n = nums.length;
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
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;
        int total = 1 << n;
        
        for (int mask = 0; mask < total; mask++) {
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    subset.add(nums[i]);
                }
            }
            result.add(subset);
        }
        return result;
    }
    
    /**
     * 判断一个数是否包含特定的位模式
     */
    public static boolean hasBitPattern(int num, int pattern) {
        return (num & pattern) == pattern;
    }
    
    /**
     * 设置特定位为1
     */
    public static int setBit(int num, int pos) {
        return num | (1 << pos);
    }
    
    /**
     * 清除特定位（设为0）
     */
    public static int clearBit(int num, int pos) {
        return num & ~(1 << pos);
    }
    
    /**
     * 切换特定位（0变1，1变0）
     */
    public static int toggleBit(int num, int pos) {
        return num ^ (1 << pos);
    }
    
    /**
     * 检查特定位是否为1
     */
    public static boolean checkBit(int num, int pos) {
        return (num & (1 << pos)) != 0;
    }
    
    // ==================== 位运算在算法中的应用 ====================
    
    /**
     * 使用位运算实现加法
     */
    public static int add(int a, int b) {
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
    public static int subtract(int a, int b) {
        return add(a, add(~b, 1));
    }
    
    /**
     * 快速判断奇偶性
     */
    public static boolean isOdd(int n) {
        return (n & 1) == 1;
    }
    
    /**
     * 快速计算绝对值（32位整数）
     */
    public static int abs(int n) {
        int mask = n >> 31;
        return (n + mask) ^ mask;
    }
    
    /**
     * 快速计算2的n次方
     */
    public static int powerOfTwo(int n) {
        return 1 << n;
    }
    
    /**
     * 快速判断是否是2的幂的倍数
     */
    public static boolean isMultipleOfPowerOfTwo(int n, int power) {
        return (n & ((1 << power) - 1)) == 0;
    }
    
    // ==================== 位运算在数据结构中的应用 ====================
    
    /**
     * 使用位集实现布隆过滤器（简化版）
     */
    static class BloomFilter {
        private int[] bitmap;
        private int size;
        
        public BloomFilter(int size) {
            this.size = size;
            this.bitmap = new int[(size + 31) / 32];
        }
        
        public void add(int value) {
            int hash1 = hash1(value);
            int hash2 = hash2(value);
            int hash3 = hash3(value);
            
            setBit(hash1 % size);
            setBit(hash2 % size);
            setBit(hash3 % size);
        }
        
        public boolean contains(int value) {
            int hash1 = hash1(value);
            int hash2 = hash2(value);
            int hash3 = hash3(value);
            
            return checkBit(hash1 % size) && 
                   checkBit(hash2 % size) && 
                   checkBit(hash3 % size);
        }
        
        private void setBit(int pos) {
            int index = pos / 32;
            int bit = pos % 32;
            bitmap[index] |= (1 << bit);
        }
        
        private boolean checkBit(int pos) {
            int index = pos / 32;
            int bit = pos % 32;
            return (bitmap[index] & (1 << bit)) != 0;
        }
        
        private int hash1(int value) {
            return value * 31;
        }
        
        private int hash2(int value) {
            return value * 17 + 12345;
        }
        
        private int hash3(int value) {
            return value * 13 + 67890;
        }
    }
    
    // ==================== 位运算在优化中的应用 ====================
    
    /**
     * 快速计算log2（整数部分）
     */
    public static int log2(int n) {
        if (n <= 0) return -1;
        return 31 - Integer.numberOfLeadingZeros(n);
    }
    
    /**
     * 快速计算下一个2的幂（大于等于n的最小2的幂）
     */
    public static int nextPowerOfTwo(int n) {
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
    public static int prevPowerOfTwo(int n) {
        if (n <= 0) return 0;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n - (n >> 1);
    }
    
    // ==================== 测试方法 ====================
    
    public static void main(String[] args) {
        System.out.println("=== 基础位操作测试 ===");
        System.out.println("8是2的幂: " + isPowerOfTwo(8)); // true
        System.out.println("16是4的幂: " + isPowerOfFour(16)); // true
        System.out.println("5的二进制中1的个数: " + countOnes(5)); // 2
        System.out.println("1和4的汉明距离: " + hammingDistance(1, 4)); // 2
        
        System.out.println("\n=== 高级位操作测试 ===");
        swap(5, 3);
        int[] nums1 = {2, 2, 1};
        System.out.println("只出现一次的数字: " + singleNumber(nums1)); // 1
        int[] nums2 = {0, 1, 3};
        System.out.println("缺失的数字: " + missingNumber(nums2)); // 2
        
        System.out.println("\n=== 位掩码测试 ===");
        int[] nums3 = {1, 2, 3};
        System.out.println("子集数量: " + subsets(nums3).size()); // 8
        System.out.println("5包含模式101: " + hasBitPattern(5, 5)); // true
        
        System.out.println("\n=== 算法应用测试 ===");
        System.out.println("5 + 3 = " + add(5, 3)); // 8
        System.out.println("5 - 3 = " + subtract(5, 3)); // 2
        System.out.println("5是奇数: " + isOdd(5)); // true
        System.out.println("-5的绝对值: " + abs(-5)); // 5
        
        System.out.println("\n=== 优化技巧测试 ===");
        System.out.println("log2(8) = " + log2(8)); // 3
        System.out.println("15的下一个2的幂: " + nextPowerOfTwo(15)); // 16
        System.out.println("15的前一个2的幂: " + prevPowerOfTwo(15)); // 8
        
        System.out.println("\n=== 布隆过滤器测试 ===");
        BloomFilter filter = new BloomFilter(100);
        filter.add(42);
        filter.add(123);
        System.out.println("过滤器包含42: " + filter.contains(42)); // true
        System.out.println("过滤器包含456: " + filter.contains(456)); // false（可能误判）
    }
    
    /**
     * 工程化考量：
     * 1. 边界条件处理：负数、零、边界值等
     * 2. 性能优化：使用位运算替代算术运算
     * 3. 可读性：添加详细注释说明位运算原理
     * 4. 错误处理：输入验证和异常处理
     * 5. 测试覆盖：包含各种边界情况和特殊输入
     * 
     * 应用场景：
     * 1. 算法竞赛：快速实现各种计算
     * 2. 系统编程：底层优化
     * 3. 加密算法：位操作是加密的基础
     * 4. 图形学：颜色操作、像素处理
     * 5. 网络编程：协议解析、数据包处理
     * 
     * 学习建议：
     * 1. 理解二进制表示：掌握二进制、补码等概念
     * 2. 熟练基本操作：与、或、异或、取反、移位
     * 3. 掌握常用技巧：Brian Kernighan算法、位掩码等
     * 4. 实践应用：在具体问题中应用位运算技巧
     * 5. 理解原理：知道为什么这样操作能达到目的
     */
}