package class031;

// 汉明距离
// 测试链接 : https://leetcode.cn/problems/hamming-distance/
/*
题目描述：
两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目。
给你两个整数 x 和 y，计算并返回它们之间的汉明距离。

示例：
输入：x = 1, y = 4
输出：2
解释：
1   (0 0 0 1)
4   (0 1 0 0)
       ↑   ↑
上面的箭头指出了对应二进制位不同的位置。

输入：x = 3, y = 1
输出：1

提示：
0 <= x, y <= 2^31 - 1

解题思路：
1. 首先对两个数字进行异或运算，这样不同的位就会被设置为1
2. 然后统计异或结果中1的个数，这个个数就是汉明距离

计算二进制中1的个数可以使用多种方法：
- 逐位检查（最直接的方法）
- Brian Kernighan算法：利用n & (n - 1)移除最右边的1，直到n变为0
- 查表法（适用于需要频繁计算的场景）

时间复杂度：O(1) - 因为整数的位数是固定的（32位）
空间复杂度：O(1) - 只使用了常数级别的额外空间
*/
public class Code16_HammingDistance {

    /**
     * 计算两个整数之间的汉明距离
     * @param x 第一个整数
     * @param y 第二个整数
     * @return 汉明距离
     */
    public static int hammingDistance(int x, int y) {
        // 对两个数字进行异或运算，不同的位会被设置为1
        int xor = x ^ y;
        
        // 方法1：逐位检查（最直接的方法）
        // int distance = 0;
        // for (int i = 0; i < 32; i++) {
        //     // 检查当前位是否为1
        //     if ((xor & (1 << i)) != 0) {
        //         distance++;
        //     }
        // }
        // return distance;
        
        // 方法2：Brian Kernighan算法（更高效的方法）
        // 每次使用n & (n - 1)移除最右边的1，直到n变为0
        int distance = 0;
        while (xor != 0) {
            distance++;
            // 移除最右边的1
            xor = xor & (xor - 1);
        }
        return distance;
    }
    
    /**
     * 另一种实现方式：使用Java内置方法
     * @param x 第一个整数
     * @param y 第二个整数
     * @return 汉明距离
     */
    public static int hammingDistance2(int x, int y) {
        // Integer.bitCount() 方法可以直接计算整数二进制表示中1的个数
        return Integer.bitCount(x ^ y);
    }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("Test 1: " + hammingDistance(1, 4));   // 输出: 2
        System.out.println("Test 2: " + hammingDistance(3, 1));   // 输出: 1
        System.out.println("Test 3: " + hammingDistance(0, 0));   // 输出: 0
        System.out.println("Test 4: " + hammingDistance(2147483647, 0)); // 输出: 31
        
        // 测试方法2
        System.out.println("\nUsing method 2:");
        System.out.println("Test 1: " + hammingDistance2(1, 4));   // 输出: 2
        System.out.println("Test 2: " + hammingDistance2(3, 1));   // 输出: 1
    }
}