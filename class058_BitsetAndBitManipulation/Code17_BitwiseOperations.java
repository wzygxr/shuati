package class032;

import java.util.*;

/**
 * 位运算经典题目集合
 * 题目来源: LeetCode, HackerRank, Codeforces等
 * 包含各种位运算技巧和常见问题
 * 
 * 解题思路:
 * 方法1: 位运算基础操作
 * 方法2: 位掩码技巧
 * 方法3: 位计数算法
 * 方法4: 位操作优化
 * 
 * 时间复杂度分析:
 * 方法1: O(1) - 常数时间操作
 * 方法2: O(n) - 线性扫描
 * 方法3: O(1) - 查表法
 * 方法4: O(log n) - 对数时间
 * 
 * 空间复杂度分析:
 * 方法1: O(1) - 常数空间
 * 方法2: O(1) - 常数空间
 * 方法3: O(1) - 常数空间
 * 方法4: O(1) - 常数空间
 * 
 * 工程化考量:
 * 1. 边界处理: 处理负数、零、溢出等情况
 * 2. 性能优化: 使用位运算替代算术运算
 * 3. 可读性: 添加详细注释说明位运算含义
 * 4. 测试覆盖: 覆盖各种边界情况
 */

public class Code17_BitwiseOperations {
    
    /**
     * LeetCode 191. Number of 1 Bits - 计算二进制中1的个数
     * 题目链接: https://leetcode.com/problems/number-of-1-bits/
     * 题目描述: 计算一个无符号整数的二进制表示中1的个数
     * 
     * 方法1: 循环检查每一位
     * 时间复杂度: O(32) = O(1)
     * 空间复杂度: O(1)
     */
    public static int hammingWeight1(int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & (1 << i)) != 0) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 方法2: 使用n & (n-1)技巧
     * 每次操作消除最低位的1
     * 时间复杂度: O(k) k为1的个数
     * 空间复杂度: O(1)
     */
    public static int hammingWeight2(int n) {
        int count = 0;
        while (n != 0) {
            n = n & (n - 1);  // 消除最低位的1
            count++;
        }
        return count;
    }
    
    /**
     * 方法3: 查表法（适用于大量查询）
     * 时间复杂度: O(1)
     * 空间复杂度: O(256)
     */
    public static int hammingWeight3(int n) {
        // 预计算0-255的汉明重量
        int[] table = new int[256];
        for (int i = 0; i < 256; i++) {
            table[i] = table[i >> 1] + (i & 1);
        }
        
        // 分4次查表
        return table[n & 0xff] + 
               table[(n >> 8) & 0xff] + 
               table[(n >> 16) & 0xff] + 
               table[(n >> 24) & 0xff];
    }
    
    /**
     * LeetCode 231. Power of Two - 判断是否为2的幂
     * 题目链接: https://leetcode.com/problems/power-of-two/
     * 题目描述: 判断一个整数是否是2的幂次方
     * 
     * 方法1: 使用n & (n-1)技巧
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static boolean isPowerOfTwo1(int n) {
        if (n <= 0) return false;
        return (n & (n - 1)) == 0;
    }
    
    /**
     * 方法2: 使用Integer.bitCount()
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static boolean isPowerOfTwo2(int n) {
        if (n <= 0) return false;
        return Integer.bitCount(n) == 1;
    }
    
    /**
     * LeetCode 136. Single Number - 只出现一次的数字
     * 题目链接: https://leetcode.com/problems/single-number/
     * 题目描述: 给定一个非空整数数组，除了某个元素只出现一次外，其余每个元素均出现两次。找出那个只出现一次的元素。
     * 
     * 方法: 使用异或运算
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;  // 异或运算：相同为0，不同为1
        }
        return result;
    }
    
    /**
     * LeetCode 268. Missing Number - 缺失的数字
     * 题目链接: https://leetcode.com/problems/missing-number/
     * 题目描述: 给定一个包含[0, n]中n个数的数组，找出数组中缺失的那个数
     * 
     * 方法1: 异或运算
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    public static int missingNumber1(int[] nums) {
        int n = nums.length;
        int result = n;  // 初始化为n，因为数组长度是n，但应该包含0到n共n+1个数
        
        for (int i = 0; i < n; i++) {
            result ^= i;
            result ^= nums[i];
        }
        
        return result;
    }
    
    /**
     * 方法2: 数学公式
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    public static int missingNumber2(int[] nums) {
        int n = nums.length;
        int expectedSum = n * (n + 1) / 2;
        int actualSum = 0;
        
        for (int num : nums) {
            actualSum += num;
        }
        
        return expectedSum - actualSum;
    }
    
    /**
     * LeetCode 338. Counting Bits - 比特位计数
     * 题目链接: https://leetcode.com/problems/counting-bits/
     * 题目描述: 给定一个非负整数n，计算0到n之间的每个数字的二进制表示中1的个数
     * 
     * 方法1: 动态规划 + 最高有效位
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public static int[] countBits1(int n) {
        int[] result = new int[n + 1];
        result[0] = 0;
        
        for (int i = 1; i <= n; i++) {
            // i & (i-1) 可以消除最低位的1
            result[i] = result[i & (i - 1)] + 1;
        }
        
        return result;
    }
    
    /**
     * 方法2: 动态规划 + 最低有效位
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public static int[] countBits2(int n) {
        int[] result = new int[n + 1];
        result[0] = 0;
        
        for (int i = 1; i <= n; i++) {
            // i >> 1 相当于除以2
            result[i] = result[i >> 1] + (i & 1);
        }
        
        return result;
    }
    
    /**
     * LeetCode 190. Reverse Bits - 颠倒二进制位
     * 题目链接: https://leetcode.com/problems/reverse-bits/
     * 题目描述: 颠倒给定的32位无符号整数的二进制位
     * 
     * 方法1: 逐位反转
     * 时间复杂度: O(32) = O(1)
     * 空间复杂度: O(1)
     */
    public static int reverseBits1(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            result <<= 1;  // 左移一位，为新的位腾出空间
            result |= (n & 1);  // 取n的最低位
            n >>>= 1;  // 无符号右移
        }
        return result;
    }
    
    /**
     * 方法2: 分治法（更高效）
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static int reverseBits2(int n) {
        // 分治交换：先交换16位块，然后8位，4位，2位，1位
        n = (n >>> 16) | (n << 16);
        n = ((n & 0xff00ff00) >>> 8) | ((n & 0x00ff00ff) << 8);
        n = ((n & 0xf0f0f0f0) >>> 4) | ((n & 0x0f0f0f0f) << 4);
        n = ((n & 0xcccccccc) >>> 2) | ((n & 0x33333333) << 2);
        n = ((n & 0xaaaaaaaa) >>> 1) | ((n & 0x55555555) << 1);
        return n;
    }
    
    /**
     * LeetCode 371. Sum of Two Integers - 两整数之和
     * 题目链接: https://leetcode.com/problems/sum-of-two-integers/
     * 题目描述: 不使用运算符+和-，计算两整数之和
     * 
     * 方法: 使用位运算模拟加法
     * 时间复杂度: O(1) 最多32次循环
     * 空间复杂度: O(1)
     */
    public static int getSum(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;  // 计算进位
            a = a ^ b;  // 计算无进位和
            b = carry;  // 将进位作为新的b
        }
        return a;
    }
    
    /**
     * LeetCode 201. Bitwise AND of Numbers Range - 数字范围按位与
     * 题目链接: https://leetcode.com/problems/bitwise-and-of-numbers-range/
     * 题目描述: 给定两个整数left和right，返回此区间内所有数字按位与的结果
     * 
     * 方法1: 寻找公共前缀
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static int rangeBitwiseAnd1(int left, int right) {
        int shift = 0;
        // 找到left和right的公共前缀
        while (left < right) {
            left >>= 1;
            right >>= 1;
            shift++;
        }
        return left << shift;
    }
    
    /**
     * 方法2: Brian Kernighan算法
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static int rangeBitwiseAnd2(int left, int right) {
        while (left < right) {
            // 清除right的最低位的1
            right = right & (right - 1);
        }
        return right;
    }
    
    /**
     * 位运算工具方法：判断奇偶性
     */
    public static boolean isOdd(int n) {
        return (n & 1) == 1;  // 奇数的最后一位是1
    }
    
    /**
     * 位运算工具方法：交换两个数
     */
    public static void swap(int a, int b) {
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println("交换后: a=" + a + ", b=" + b);
    }
    
    /**
     * 位运算工具方法：取绝对值
     */
    public static int abs(int n) {
        int mask = n >> 31;  // 对于负数，mask是全1；对于正数，mask是全0
        return (n + mask) ^ mask;  // 等价于 (n ^ mask) - mask
    }
    
    /**
     * 单元测试方法
     */
    public static void runTests() {
        System.out.println("=== 位运算经典题目 - 单元测试 ===");
        
        // 测试汉明重量
        System.out.println("汉明重量测试:");
        int test1 = 11;  // 二进制: 1011
        System.out.printf("数字%d的二进制中1的个数: %d (方法1)%n", test1, hammingWeight1(test1));
        System.out.printf("数字%d的二进制中1的个数: %d (方法2)%n", test1, hammingWeight2(test1));
        System.out.printf("数字%d的二进制中1的个数: %d (方法3)%n", test1, hammingWeight3(test1));
        
        // 测试2的幂
        System.out.println("\n2的幂测试:");
        System.out.printf("8是2的幂: %b%n", isPowerOfTwo1(8));
        System.out.printf("7是2的幂: %b%n", isPowerOfTwo1(7));
        
        // 测试只出现一次的数字
        System.out.println("\n只出现一次的数字测试:");
        int[] nums = {4, 1, 2, 1, 2};
        System.out.printf("数组%s中只出现一次的数字: %d%n", Arrays.toString(nums), singleNumber(nums));
        
        // 测试缺失的数字
        System.out.println("\n缺失的数字测试:");
        int[] nums2 = {3, 0, 1};
        System.out.printf("数组%s中缺失的数字: %d (方法1)%n", Arrays.toString(nums2), missingNumber1(nums2));
        System.out.printf("数组%s中缺失的数字: %d (方法2)%n", Arrays.toString(nums2), missingNumber2(nums2));
        
        // 测试比特位计数
        System.out.println("\n比特位计数测试:");
        int[] bits = countBits1(5);
        System.out.printf("0到5的比特位计数: %s%n", Arrays.toString(bits));
        
        // 测试两整数之和
        System.out.println("\n两整数之和测试:");
        System.out.printf("3 + 5 = %d%n", getSum(3, 5));
        
        // 测试数字范围按位与
        System.out.println("\n数字范围按位与测试:");
        System.out.printf("[5, 7]的按位与结果: %d%n", rangeBitwiseAnd1(5, 7));
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 测试汉明重量的不同方法
        int testValue = 0xffffffff;  // 全1的32位数
        
        long startTime = System.nanoTime();
        int result1 = hammingWeight1(testValue);
        long time1 = System.nanoTime() - startTime;
        System.out.printf("方法1 (循环): %d ns, 结果: %d%n", time1, result1);
        
        startTime = System.nanoTime();
        int result2 = hammingWeight2(testValue);
        long time2 = System.nanoTime() - startTime;
        System.out.printf("方法2 (n&(n-1)): %d ns, 结果: %d%n", time2, result2);
        
        startTime = System.nanoTime();
        int result3 = hammingWeight3(testValue);
        long time3 = System.nanoTime() - startTime;
        System.out.printf("方法3 (查表法): %d ns, 结果: %d%n", time3, result3);
        
        // 测试反转比特位的不同方法
        startTime = System.nanoTime();
        int reverse1 = reverseBits1(0x12345678);
        long time4 = System.nanoTime() - startTime;
        System.out.printf("反转比特位方法1: %d ns, 结果: %x%n", time4, reverse1);
        
        startTime = System.nanoTime();
        int reverse2 = reverseBits2(0x12345678);
        long time5 = System.nanoTime() - startTime;
        System.out.printf("反转比特位方法2: %d ns, 结果: %x%n", time5, reverse2);
    }
    
    /**
     * 复杂度分析
     */
    public static void complexityAnalysis() {
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("位运算算法的核心优势:");
        System.out.println("1. 时间复杂度通常为O(1)或O(n)");
        System.out.println("2. 空间复杂度通常为O(1)");
        System.out.println("3. 常数项较小，实际运行效率高");
        
        System.out.println("\n常用位运算技巧:");
        System.out.println("n & (n-1): 消除最低位的1");
        System.out.println("n & -n: 获取最低位的1");
        System.out.println("n ^ n = 0: 相同数异或为0");
        System.out.println("n << k: 乘以2的k次方");
        System.out.println("n >> k: 除以2的k次方");
        
        System.out.println("\n工程化建议:");
        System.out.println("1. 注意整数溢出问题");
        System.out.println("2. 考虑负数的情况");
        System.out.println("3. 添加详细注释说明位运算含义");
        System.out.println("4. 进行充分的边界测试");
    }
    
    public static void main(String[] args) {
        System.out.println("位运算经典题目集合");
        System.out.println("包含LeetCode、HackerRank等平台的经典位运算问题");
        
        // 运行单元测试
        runTests();
        
        // 运行性能测试
        performanceTest();
        
        // 复杂度分析
        complexityAnalysis();
        
        // 实用工具方法演示
        System.out.println("\n=== 实用工具方法演示 ===");
        System.out.printf("判断15是否为奇数: %b%n", isOdd(15));
        System.out.printf("-5的绝对值: %d%n", abs(-5));
        System.out.print("交换10和20: ");
        swap(10, 20);
        
        // 位运算在工程中的应用
        System.out.println("\n=== 位运算在工程中的应用 ===");
        System.out.println("1. 权限管理系统: 使用位掩码表示权限");
        System.out.println("2. 状态标记: 使用位标记多种状态");
        System.out.println("3. 数据压缩: 使用位操作减少存储空间");
        System.out.println("4. 加密算法: 位运算是加密算法的基础");
        System.out.println("5. 图形处理: 像素操作大量使用位运算");
    }
}