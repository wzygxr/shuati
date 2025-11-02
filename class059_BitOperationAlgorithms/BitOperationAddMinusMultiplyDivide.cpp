// 位运算实现四则运算及相关算法 - C++版本

/**
 * 位运算实现四则运算及相关算法题解
 * 
 * 本类使用纯位运算实现加减乘除四则运算，以及多种与位运算相关的算法题目解答。
 * 所有实现都避免使用任何算术运算符（+、-、*、/），仅使用位运算符。
 * 
 * 核心思想：
 * 1. 加法：利用异或运算实现无进位加法，利用与运算和左移实现进位
 * 2. 减法：基于加法和相反数实现，a - b = a + (-b)
 * 3. 乘法：基于二进制分解，检查乘数每一位是否为1，为1则将被乘数左移相应位数后累加
 * 4. 除法：从高位到低位尝试减法，使用位移优化性能
 * 
 * 作者: Algorithm Journey
 * 版本: 1.0
 */
class BitOperationUtils {
public:
    static const int MIN = -2147483648; // INT_MIN
    static const int MAX = 2147483647;  // INT_MAX

    /**
     * 两数相除
     * 
     * 算法原理：
     * 处理各种边界情况，特别是整数最小值的情况，然后调用div方法进行计算
     * 
     * 特殊情况处理：
     * 1. a和b都是整数最小值：返回1
     * 2. b是整数最小值：返回0
     * 3. a是整数最小值且b是-1：返回整数最大值（防止溢出）
     * 4. 其他情况：调用div方法计算
     * 
     * @param a 被除数
     * @param b 除数
     * @return a除以b的结果
     */
    static int divide(int a, int b) {
        if (a == MIN && b == MIN) {
            return 1;
        }
        if (a != MIN && b != MIN) {
            return div(a, b);
        }
        if (b == MIN) {
            return 0;
        }
        if (b == neg(1)) {
            return MAX;
        }
        a = add(a, b > 0 ? b : neg(b));
        int ans = div(a, b);
        int offset = b > 0 ? neg(1) : 1;
        return add(ans, offset);
    }

    /**
     * 除法辅助函数：必须保证a和b都不是整数最小值，返回a除以b的结果
     * 
     * 算法原理：
     * 1. 将a和b都转换为正数处理（取绝对值）
     * 2. 从最高位开始，尝试将被除数减去除数的倍数
     * 3. 使用位移优化性能，避免逐个减法
     * 
     * 时间复杂度：O(1) - 固定位数的整数
     * 空间复杂度：O(1) - 只使用常数级额外空间
     * 
     * @param a 被除数（非整数最小值）
     * @param b 除数（非整数最小值）
     * @return a除以b的结果
     */
    static int div(int a, int b) {
        int x = a < 0 ? neg(a) : a;
        int y = b < 0 ? neg(b) : b;
        int ans = 0;
        for (int i = 30; i >= 0; i = minus(i, 1)) {
            if ((x >> i) >= y) {
                ans |= (1 << i);
                x = minus(x, y << i);
            }
        }
        return (a < 0) ^ (b < 0) ? neg(ans) : ans;
    }

    /**
     * 加法实现
     * 
     * 算法原理：
     * 1. 异或运算(^)实现无进位加法
     * 2. 与运算(&)和左移(<<)实现进位
     * 3. 循环直到没有进位
     * 
     * 例如：计算 5 + 3
     * 5 的二进制: 101
     * 3 的二进制: 011
     * 第一次循环:
     *   无进位加法: 101 ^ 011 = 110
     *   进位: (101 & 011) << 1 = 001 << 1 = 010
     * 第二次循环:
     *   无进位加法: 110 ^ 010 = 100
     *   进位: (110 & 010) << 1 = 010 << 1 = 100
     * 第三次循环:
     *   无进位加法: 100 ^ 100 = 000
     *   进位: (100 & 100) << 1 = 100 << 1 = 1000
     * 第四次循环:
     *   无进位加法: 000 ^ 1000 = 1000
     *   进位: (000 & 1000) << 1 = 000 << 1 = 000
     * 进位为0，循环结束，结果为 1000 (二进制) = 8 (十进制)
     * 
     * 时间复杂度：O(1) - 固定位数的整数
     * 空间复杂度：O(1) - 只使用常数级额外空间
     * 
     * @param a 第一个加数
     * @param b 第二个加数
     * @return a与b的和
     */
    static int add(int a, int b) {
        int ans = a;
        while (b != 0) {
            ans = a ^ b;
            b = (a & b) << 1;
            a = ans;
        }
        return ans;
    }

    /**
     * 减法实现
     * 
     * 算法原理：
     * 基于加法和相反数实现
     * a - b = a + (-b)
     * 
     * 时间复杂度：O(1) - 固定位数的整数
     * 空间复杂度：O(1) - 只使用常数级额外空间
     * 
     * @param a 被减数
     * @param b 减数
     * @return a与b的差
     */
    static int minus(int a, int b) {
        return add(a, neg(b));
    }

    /**
     * 求相反数
     * 
     * 算法原理：
     * 基于补码表示法
     * -n = ~n + 1
     * 
     * 时间复杂度：O(1) - 固定位数的整数
     * 空间复杂度：O(1) - 只使用常数级额外空间
     * 
     * @param n 待求相反数的整数
     * @return n的相反数
     */
    static int neg(int n) {
        return add(~n, 1);
    }

    /**
     * 乘法实现（龟速乘）
     * 
     * 算法原理：
     * 基于二进制分解
     * 检查乘数b的每一位是否为1
     * 如果为1，则将被乘数a左移相应位数后累加到结果中
     * 
     * 例如：计算 5 * 3
     * 5 的二进制: 101
     * 3 的二进制: 011
     * 检查3的每一位：
     *   第0位：1，将5左移0位(5)累加到结果中
     *   第1位：1，将5左移1位(10)累加到结果中
     *   第2位：0，不累加
     * 结果：5 + 10 = 15
     * 
     * 时间复杂度：O(log b) - b的二进制位数
     * 空间复杂度：O(1) - 只使用常数级额外空间
     * 
     * @param a 被乘数
     * @param b 乘数
     * @return a与b的积
     */
    static int multiply(int a, int b) {
        int ans = 0;
        while (b != 0) {
            if ((b & 1) != 0) {
                ans = add(ans, a);
            }
            a <<= 1;
            b >>= 1;
        }
        return ans;
    }

    /**
     * 计算一个数字的二进制表示中1的个数（汉明重量）
     * LeetCode 191. 位1的个数
     * 题目链接: https://leetcode.cn/problems/number-of-1-bits/
     * 题目描述: 编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
     * 
     * 算法原理：
     * 遍历32位，检查每一位是否为1
     * 
     * 时间复杂度: O(1) - 最多循环32次（32位整数）
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param n 输入的无符号整数
     * @return n的二进制表示中1的个数
     */
    static int hammingWeight(unsigned int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & (1U << i)) != 0) {
                count = add(count, 1);
            }
        }
        return count;
    }

    /**
     * 优化版本的汉明重量计算（更高效）
     * 
     * 算法原理：
     * 利用 n & (n-1) 可以清除n的二进制表示中最右边的1
     * 每次操作都会清除最右边的一个1，直到n变为0
     * 
     * 例如：计算 12 的汉明重量
     * 12 的二进制: 1100
     * 第一次：1100 & 1011 = 1000 (清除最右边的1)
     * 第二次：1000 & 0111 = 0000 (清除最右边的1)
     * 循环2次，所以汉明重量为2
     * 
     * 时间复杂度: O(k) - k是二进制表示中1的个数
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param n 输入的无符号整数
     * @return n的二进制表示中1的个数
     */
    static int hammingWeightOptimized(unsigned int n) {
        int count = 0;
        while (n != 0) {
            count = add(count, 1);
            n = n & (n - 1);
        }
        return count;
    }

    /**
     * 判断一个数是否是2的幂
     * LeetCode 231. 2的幂
     * 题目链接: https://leetcode.cn/problems/power-of-two/
     * 题目描述: 给你一个整数 n，请你判断该整数是否是 2 的幂次方。
     * 
     * 算法原理：
     * 2的幂在二进制表示中只有一个1，且必须是正数
     * n & (n-1) 会清除n的二进制表示中最右边的1
     * 如果n是2的幂，那么n & (n-1)的结果应该是0
     * 
     * 例如：
     * 8 的二进制: 1000
     * 7 的二进制: 0111
     * 8 & 7 = 0000
     * 
     * 时间复杂度: O(1) - 只进行一次位运算
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param n 待判断的整数
     * @return 如果n是2的幂返回true，否则返回false
     */
    static bool isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * 计算两个数字的汉明距离（对应二进制位不同的位置的数目）
     * LeetCode 461. 汉明距离
     * 题目链接: https://leetcode.cn/problems/hamming-distance/
     * 题目描述: 两个整数之间的 汉明距离 指的是这两个数字对应二进制位不同的位置的数目。
     * 
     * 算法原理：
     * 1. 先对两个数进行异或运算，相同为0，不同为1
     * 2. 然后计算异或结果中1的个数
     * 
     * 时间复杂度: O(1) - 最多循环32次（32位整数）
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param x 第一个整数
     * @param y 第二个整数
     * @return x和y的汉明距离
     */
    static int hammingDistance(int x, int y) {
        int xor_result = x ^ y;
        return hammingWeight(xor_result);
    }

    /**
     * LeetCode 136. 只出现一次的数字
     * 题目链接: https://leetcode.cn/problems/single-number/
     * 题目描述: 给你一个非空整数数组 nums ，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
     * 
     * 算法原理:
     * 利用异或运算的性质：
     * 1. a ^ a = 0 (任何数与自己异或结果为0)
     * 2. a ^ 0 = a (任何数与0异或结果为自己)
     * 3. 异或运算满足交换律和结合律
     * 
     * 因此，将数组中所有元素异或，出现两次的元素会相互抵消为0，
     * 最终只剩下只出现一次的元素。
     * 
     * 时间复杂度: O(n) - 需要遍历整个数组
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param nums 输入的整数数组
     * @param n 数组长度
     * @return 只出现一次的元素
     */
    static int singleNumber(int nums[], int n) {
        int result = 0;
        for (int i = 0; i < n; i++) {
            result = result ^ nums[i];
        }
        return result;
    }

    /**
     * LeetCode 268. 缺失的数字
     * 题目链接: https://leetcode.cn/problems/missing-number/
     * 题目描述: 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。
     * 
     * 算法原理:
     * 利用异或运算的性质：
     * 1. 将索引0到n-1与数组元素nums[0]到nums[n-1]一起异或
     * 2. 再异或n
     * 3. 由于除了缺失的数字外，其他数字都会出现两次，最终结果就是缺失的数字
     * 
     * 例如：nums = [3, 0, 1]，n = 3
     * 初始result = 0
     * i=0: result = 0 ^ 0 ^ 3 = 3
     * i=1: result = 3 ^ 1 ^ 0 = 2
     * i=2: result = 2 ^ 2 ^ 1 = 3
     * 最后: result = 3 ^ 3 = 0
     * 但0在数组中存在，所以缺失的是另一个数字
     * 正确做法是最后再异或n: result = 3 ^ 3 = 0
     * 
     * 时间复杂度: O(n) - 需要遍历整个数组
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param nums 输入的整数数组
     * @param n 数组长度
     * @return 缺失的数字
     */
    static int missingNumber(int nums[], int n) {
        int result = 0;
        for (int i = 0; i < n; i++) {
            result = result ^ i ^ nums[i];
        }
        return result ^ n;
    }

    /**
     * LeetCode 231. 2的幂
     * 题目链接: https://leetcode.cn/problems/power-of-two/
     * 题目描述: 给你一个整数 n，请你判断该整数是否是 2 的幂次方。
     * 
     * 算法原理：
     * 2的幂在二进制表示中只有一个1，且必须是正数
     * n & (n-1) 会清除n的二进制表示中最右边的1
     * 如果n是2的幂，那么n & (n-1)的结果应该是0
     * 
     * 例如：
     * 8 的二进制: 1000
     * 7 的二进制: 0111
     * 8 & 7 = 0000
     * 
     * 时间复杂度: O(1) - 只进行一次位运算
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param n 待判断的整数
     * @return 如果n是2的幂返回true，否则返回false
     */
    static bool isPowerOfTwoSimple(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * LeetCode 461. 汉明距离
     * 题目链接: https://leetcode.cn/problems/hamming-distance/
     * 题目描述: 两个整数之间的 汉明距离 指的是这两个数字对应二进制位不同的位置的数目。
     * 
     * 算法原理：
     * 1. 先对两个数进行异或运算，相同为0，不同为1
     * 2. 然后计算异或结果中1的个数
     * 
     * 时间复杂度: O(1) - 最多循环32次（32位整数）
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param x 第一个整数
     * @param y 第二个整数
     * @return x和y的汉明距离
     */
    static int hammingDistanceSimple(int x, int y) {
        return hammingWeight(x ^ y);
    }

    /**
     * LeetCode 190. 颠倒二进制位
     * 题目链接: https://leetcode.cn/problems/reverse-bits/
     * 题目描述: 颠倒给定的 32 位无符号整数的二进制位
     * 
     * 算法原理:
     * 逐位颠倒，从最低位开始，将每一位移动到对应的高位位置
     * 
     * 例如：n = 12 (二进制: 1100)
     * i=0: result = (0<<1) | (1100&1) = 0 | 0 = 0, n = 0110
     * i=1: result = (0<<1) | (0110&1) = 0 | 0 = 0, n = 0011
     * i=2: result = (0<<1) | (0011&1) = 0 | 1 = 1, n = 0001
     * i=3: result = (1<<1) | (0001&1) = 10 | 1 = 11, n = 0000
     * ...
     * 
     * 时间复杂度: O(1) - 固定32次循环
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param n 输入的32位无符号整数
     * @return 颠倒二进制位后的结果
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
     * LeetCode 693. 交替位二进制数
     * 题目链接: https://leetcode.cn/problems/binary-number-with-alternating-bits/
     * 题目描述: 给定一个正整数，检查它的二进制表示是否总是 0、1 交替出现
     * 
     * 算法原理:
     * 检查 n ^ (n >> 1) 是否所有位都是1
     * 
     * 对于交替位二进制数，右移1位后与原数异或，结果应该是全1
     * 例如：n = 10 (二进制: 1010)
     * n>>1 = 0101
     * n^(n>>1) = 1111 (全1)
     * 全1的数字加1后是2的幂，与原数相与结果为0
     * 
     * 时间复杂度: O(1) - 最多循环32次
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param n 输入的正整数
     * @return 如果二进制表示是交替位返回true，否则返回false
     */
    static bool hasAlternatingBits(int n) {
        int xor_result = n ^ (n >> 1);
        return (xor_result & (xor_result + 1)) == 0;
    }

    /**
     * LeetCode 476. 数字的补数
     * 题目链接: https://leetcode.cn/problems/number-complement/
     * 题目描述: 对整数的二进制表示取反（0 变 1 ，1 变 0）后，再转换为十进制表示，可以得到这个整数的补数
     * 
     * 算法原理:
     * 找到最高位的1，然后构造掩码，最后异或得到补数
     * 
     * 例如：num = 5 (二进制: 101)
     * 1. 找到最高位的1：mask = 111
     * 2. 异或得到补数：101 ^ 111 = 010 (十进制: 2)
     * 
     * 时间复杂度: O(1) - 固定操作
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param num 输入的整数
     * @return num的补数
     */
    static int findComplement(int num) {
        int mask = 1;
        while (mask < num) {
            mask = (mask << 1) | 1;
        }
        return num ^ mask;
    }

    /**
     * LeetCode 371. 两整数之和（递归版本）
     * 题目链接: https://leetcode.cn/problems/sum-of-two-integers/
     * 题目描述: 给你两个整数 a 和 b ，不使用运算符 + 和 - ，计算并返回两整数之和。
     * 
     * 算法原理：
     * 1. 递归终止条件：当没有进位时，异或结果就是最终结果
     * 2. 递归计算：无进位相加的结果 + 进位
     * 
     * 时间复杂度: O(1) - 因为整数的位数是固定的
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param a 第一个整数
     * @param b 第二个整数
     * @return a与b的和
     */
    static int addRecursive(int a, int b) {
        if (b == 0) {
            return a;
        }
        return addRecursive(a ^ b, (a & b) << 1);
    }
    
    /**
     * LeetCode 868. 二进制间距
     * 题目链接: https://leetcode.cn/problems/binary-gap/
     * 题目描述: 给定一个正整数 n，找到并返回 n 的二进制表示中两个相邻的 1 之间的最长距离
     * 
     * 算法原理:
     * 记录上一个1的位置，计算当前1与上一个1的距离
     * 
     * 时间复杂度: O(log n) - 遍历n的二进制位
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param n 输入的正整数
     * @return 两个相邻1之间的最长距离
     */
    static int binaryGap(int n) {
        int maxGap = 0;
        int lastPos = -1;
        int pos = 0;
        
        while (n > 0) {
            if ((n & 1) == 1) {
                if (lastPos != -1) {
                    maxGap = (maxGap > (pos - lastPos)) ? maxGap : (pos - lastPos);
                }
                lastPos = pos;
            }
            pos++;
            n >>= 1;
        }
        
        return maxGap;
    }
    
    /**
     * LeetCode 1009. 十进制整数的反码
     * 题目链接: https://leetcode.cn/problems/complement-of-base-10-integer/
     * 题目描述: 每个非负整数 N 都有其二进制表示。例如， 5 可以被表示为二进制 "101"，11 可以用二进制 "1011" 表示，依此类推。注意，除 N = 0 外，任何二进制表示中都不含前导零。二进制的反码表示是将每个 1 改为 0 且每个 0 变为 1 。例如，二进制数 "101" 的二进制反码为 "010"。给你一个十进制数 N，请你返回其二进制表示的反码所对应的十进制整数
     * 
     * 算法原理:
     * 找到最高位的1，构造掩码，然后异或
     * 
     * 时间复杂度: O(1) - 固定操作
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param n 输入的十进制整数
     * @return n的二进制反码对应的十进制整数
     */
    static int bitwiseComplement(int n) {
        if (n == 0) return 1;
        
        int mask = 1;
        while (mask < n) {
            mask = (mask << 1) | 1;
        }
        
        return n ^ mask;
    }
};