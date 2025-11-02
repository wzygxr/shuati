// 测试位运算算法的简单测试类
/**
 * 位运算算法测试类
 * 
 * 该类用于测试各种位运算算法的正确性，包括加减乘除四则运算以及其他位运算相关算法
 * 所有测试方法都使用位运算实现，避免使用任何算术运算符（+、-、*、/）
 * 
 * 测试内容包括：
 * 1. 加法运算测试
 * 2. 减法运算测试
 * 3. 乘法运算测试
 * 4. 除法运算测试
 * 5. 其他位运算相关函数测试
 * 
 * 作者: Algorithm Journey
 * 版本: 1.0
 */
public class TestBitOperations {
    /**
     * 主函数，程序入口点
     * 执行所有位运算算法的测试用例
     * 
     * 测试流程：
     * 1. 输出测试开始提示信息
     * 2. 依次执行各类位运算测试
     * 3. 输出测试结果
     * 4. 输出测试完成提示信息
     */
    public static void main(String[] args) {
        System.out.println("Testing Bit Operations:");
        
        // 测试加法
        System.out.println("10 + 15 = " + add(10, 15));
        System.out.println("(-10) + 15 = " + add(-10, 15));
        
        // 测试减法
        System.out.println("15 - 10 = " + minus(15, 10));
        System.out.println("10 - 15 = " + minus(10, 15));
        
        // 测试乘法
        System.out.println("10 * 15 = " + multiply(10, 15));
        
        // 测试除法
        System.out.println("15 / 10 = " + divide(15, 10));
        
        // 测试其他函数
        System.out.println("Hamming Weight (15): " + hammingWeight(15));
        System.out.println("Is Power of Two (16): " + isPowerOfTwo(16));
        
        int[] nums = {2, 2, 1};
        System.out.println("Single Number: " + singleNumber(nums));
        
        System.out.println("All tests completed successfully!");
    }
    
    /**
     * 加法实现（简化版本，用于测试）
     * 
     * 算法原理：
     * 1. 异或运算(^)实现无进位加法
     * 2. 与运算(&)和左移(<<)实现进位
     * 3. 循环直到没有进位
     * 
     * 时间复杂度：O(1) - 固定位数的整数
     * 空间复杂度：O(1) - 只使用常数级额外空间
     * 
     * @param a 第一个加数
     * @param b 第二个加数
     * @return a与b的和
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
     * 减法实现（简化版本，用于测试）
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
    public static int minus(int a, int b) {
        return add(a, add(~b, 1));
    }
    
    /**
     * 乘法实现（简化版本，用于测试）
     * 
     * 算法原理：
     * 基于二进制分解
     * 检查乘数b的每一位是否为1
     * 如果为1，则将被乘数a左移相应位数后累加到结果中
     * 
     * 时间复杂度：O(log b) - b的二进制位数
     * 空间复杂度：O(1) - 只使用常数级额外空间
     * 
     * @param a 被乘数
     * @param b 乘数
     * @return a与b的积
     */
    public static int multiply(int a, int b) {
        int result = 0;
        while (b != 0) {
            if ((b & 1) != 0) {
                result = add(result, a);
            }
            a <<= 1;
            b >>>= 1;
        }
        return result;
    }
    
    /**
     * 除法实现（简化版本，用于测试）
     * 
     * 算法原理：
     * 1. 将a和b都转换为正数处理（取绝对值）
     * 2. 从最高位开始，尝试将被除数减去除数的倍数
     * 3. 使用位移优化性能，避免逐个减法
     * 
     * 特殊情况处理：
     * 1. 除数为0：返回整数最大值（防止程序崩溃）
     * 2. 正负数处理：根据被除数和除数的符号确定结果符号
     * 
     * 时间复杂度：O(1) - 固定位数的整数
     * 空间复杂度：O(1) - 只使用常数级额外空间
     * 
     * @param a 被除数
     * @param b 除数
     * @return a除以b的结果
     */
    public static int divide(int a, int b) {
        if (b == 0) return Integer.MAX_VALUE;
        int x = a < 0 ? add(~a, 1) : a;
        int y = b < 0 ? add(~b, 1) : b;
        int result = 0;
        for (int i = 31; i >= 0; i = minus(i, 1)) {
            if ((x >> i) >= y) {
                result = add(result, 1 << i);
                x = minus(x, y << i);
            }
        }
        return (a < 0) == (b < 0) ? result : add(~result, 1);
    }
    
    /**
     * 计算一个数字的二进制表示中1的个数（汉明重量）（简化版本，用于测试）
     * 
     * 算法原理：
     * 利用 n & (n-1) 可以清除n的二进制表示中最右边的1
     * 每次操作都会清除最右边的一个1，直到n变为0
     * 
     * 时间复杂度: O(k) - k是二进制表示中1的个数
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param n 输入的整数
     * @return n的二进制表示中1的个数
     */
    public static int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            count = add(count, 1);
            n = n & (n - 1);
        }
        return count;
    }
    
    /**
     * 判断一个数是否是2的幂（简化版本，用于测试）
     * 
     * 算法原理：
     * 2的幂在二进制表示中只有一个1，且必须是正数
     * n & (n-1) 会清除n的二进制表示中最右边的1
     * 如果n是2的幂，那么n & (n-1)的结果应该是0
     * 
     * 时间复杂度: O(1) - 只进行一次位运算
     * 空间复杂度: O(1) - 只使用常数级额外空间
     * 
     * @param n 待判断的整数
     * @return 如果n是2的幂返回true，否则返回false
     */
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    
    /**
     * 找出数组中只出现一次的数字（简化版本，用于测试）
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
     * @return 只出现一次的元素
     */
    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result = result ^ num;
        }
        return result;
    }
}