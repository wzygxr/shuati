package class031;

// 反转比特位
// 测试链接 : https://leetcode.cn/problems/reverse-bits/
/*
题目描述：
颠倒给定的 32 位无符号整数的二进制位。

示例：
输入：n = 00000010100101000001111010011100
输出：964176192 (00111001011110000010100101000000)
解释：输入的二进制串 00000010100101000001111010011100 表示无符号整数 43261596，
     因此返回 964176192，其二进制表示形式为 00111001011110000010100101000000。

输入：n = 11111111111111111111111111111101
输出：3221225471 (10111111111111111111111111111111)
解释：输入的二进制串 11111111111111111111111111111101 表示无符号整数 4294967293，
     因此返回 3221225471，其二进制表示形式为 10111111111111111111111111111111。

提示：
请注意，在某些语言（如 Java）中，没有无符号整数类型。在这种情况下，输入和输出都将被指定为有符号整数类型，并且不应影响您的实现，因为无论整数是有符号的还是无符号的，其内部的二进制表示形式都是相同的。
在 Java 中，编译器使用二进制补码记法来表示有符号整数。因此，在 示例 2 中，输入表示有符号整数 -3，输出表示有符号整数 -1073741825。

解题思路：
方法1：逐位反转
1. 初始化结果res = 0
2. 对于每一位（0到31），执行以下操作：
   a. 将res左移1位，为新位腾出位置
   b. 获取n的最低位并加到res中
   c. 将n右移1位，处理下一位

方法2：位运算分治
可以使用位运算分治法，通过多次交换相邻的1位、2位、4位、8位和16位来实现反转。
例如：
- 首先交换每两个相邻位
- 然后交换每两个相邻的2位组
- 接着交换每两个相邻的4位组
- 然后交换每两个相邻的8位组
- 最后交换高16位和低16位

时间复杂度：O(1) - 因为我们只处理固定的32位
空间复杂度：O(1) - 只使用了常数级别的额外空间
*/
public class Code18_ReverseBits {

    /**
     * 反转32位无符号整数的二进制位
     * 使用逐位反转方法
     * @param n 输入的32位无符号整数
     * @return 反转后的32位无符号整数
     */
    public static int reverseBits(int n) {
        int res = 0;
        // 处理每一位，从最低位到最高位
        for (int i = 0; i < 32; i++) {
            // 将结果左移一位，为新位腾出空间
            res <<= 1;
            // 获取n的最低位并加到结果中
            res |= (n & 1);
            // 将n右移一位，处理下一位
            n >>>= 1; // 使用无符号右移，确保高位补0而不是符号位
        }
        return res;
    }
    
    /**
     * 反转32位无符号整数的二进制位
     * 使用位运算分治方法（更高效）
     * @param n 输入的32位无符号整数
     * @return 反转后的32位无符号整数
     */
    public static int reverseBits2(int n) {
        // 为了可读性，我们先将n转换为long类型处理
        long num = n & 0xFFFFFFFFL; // 确保将有符号整数视为无符号整数
        
        // 分治反转：交换相邻的位组
        // 交换每两位
        num = ((num >>> 1) & 0x55555555L) | ((num & 0x55555555L) << 1);
        // 交换每四位中的两位组
        num = ((num >>> 2) & 0x33333333L) | ((num & 0x33333333L) << 2);
        // 交换每八位中的四位组
        num = ((num >>> 4) & 0x0F0F0F0FL) | ((num & 0x0F0F0F0FL) << 4);
        // 交换每16位中的八位组
        num = ((num >>> 8) & 0x00FF00FFL) | ((num & 0x00FF00FFL) << 8);
        // 交换高16位和低16位
        num = (num >>> 16) | (num << 16);
        
        // 将结果转换回int类型
        return (int) num;
    }
    
    /**
     * 反转32位无符号整数的二进制位
     * 使用更简洁的位运算分治方法
     * @param n 输入的32位无符号整数
     * @return 反转后的32位无符号整数
     */
    public static int reverseBits3(int n) {
        // 注意：在Java中，int是有符号的，所以我们需要使用无符号右移运算符 >>>
        
        // 交换相邻的位
        n = ((n >>> 1) & 0x55555555) | ((n & 0x55555555) << 1);
        // 交换相邻的2位组
        n = ((n >>> 2) & 0x33333333) | ((n & 0x33333333) << 2);
        // 交换相邻的4位组
        n = ((n >>> 4) & 0x0F0F0F0F) | ((n & 0x0F0F0F0F) << 4);
        // 交换相邻的8位组
        n = ((n >>> 8) & 0x00FF00FF) | ((n & 0x00FF00FF) << 8);
        // 交换高16位和低16位
        n = (n >>> 16) | (n << 16);
        
        return n;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例1: 43261596 (00000010100101000001111010011100)
        int n1 = 0B00000010100101000001111010011100;
        System.out.println("Test 1:");
        System.out.println("Input: " + n1);
        System.out.println("Output1: " + reverseBits(n1));
        System.out.println("Output2: " + reverseBits2(n1));
        System.out.println("Output3: " + reverseBits3(n1));
        System.out.println("Expected: 964176192");
        
        // 示例2: -3 (在二进制中是 11111111111111111111111111111101)
        int n2 = -3;
        System.out.println("\nTest 2:");
        System.out.println("Input: " + n2);
        System.out.println("Output1: " + reverseBits(n2));
        System.out.println("Output2: " + reverseBits2(n2));
        System.out.println("Output3: " + reverseBits3(n2));
        System.out.println("Expected: -1073741825");
        
        // 额外测试
        int n3 = 0; // 全0
        int n4 = -1; // 全1
        System.out.println("\nAdditional Tests:");
        System.out.println("Input: 0, Output: " + reverseBits(n3));
        System.out.println("Input: -1, Output: " + reverseBits(n4));
    }
}