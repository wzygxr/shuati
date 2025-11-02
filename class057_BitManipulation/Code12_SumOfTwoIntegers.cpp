/**
 * 两整数之和 - Sum of Two Integers
 * 测试链接 : https://leetcode.cn/problems/sum-of-two-integers/
 * 相关题目:
 * 1. 位运算技巧大全 - Bit Manipulation Tricks
 * 2. 只出现一次的数字 - Single Number: https://leetcode.cn/problems/single-number/
 * 3. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
 * 4. 汉明距离 - Hamming Distance: https://leetcode.cn/problems/hamming-distance/
 * 5. 2的幂 - Power of Two: https://leetcode.cn/problems/power-of-two/
 * 
 * 题目描述：
 * 给你两个整数 a 和 b ，不使用运算符 + 和 - ​​​​​​​，计算并返回两整数之和。
 * 
 * 示例：
 * 输入：a = 1, b = 2
 * 输出：3
 * 
 * 输入：a = 2, b = 3
 * 输出：5
 * 
 * 提示：
 * -1000 <= a, b <= 1000
 * 
 * 解题思路：
 * 这是一道经典的位运算题目，考察对加法运算本质的理解。
 * 加法运算可以分解为两个步骤：
 * 1. 不考虑进位的加法：这可以通过异或运算实现 a ^ b
 * 2. 进位：这可以通过按位与运算并左移一位实现 (a & b) << 1
 * 
 * 然后将这两个结果相加，就得到了最终的答案。由于我们不能使用加法运算符，所以需要递归或迭代地进行这个过程，直到进位为0。
 * 
 * 例如：计算 2 + 3
 * 2 的二进制：010
 * 3 的二进制：011
 * 
 * 不考虑进位的加法：010 ^ 011 = 001
 * 进位：(010 & 011) << 1 = 010 << 1 = 100
 * 
 * 继续计算 001 + 100：
 * 不考虑进位的加法：001 ^ 100 = 101
 * 进位：(001 & 100) << 1 = 000 << 1 = 000
 * 
 * 进位为0，计算结束，结果为 101（二进制）= 5（十进制）
 * 
 * 时间复杂度：O(1) - 因为整数的位数是固定的（32位）
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * 补充题目：
 * 1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
 * 2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
 * 3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
 * 4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
 * 5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
 */

class Solution {
public:
    /**
     * 计算两个整数的和，不使用运算符 + 和 -
     * 使用位运算实现加法：
     * 1. 不考虑进位的加法：a ^ b
     * 2. 进位：(a & b) << 1
     * 3. 重复上述过程直到进位为0
     * 
     * @param a 第一个整数
     * @param b 第二个整数
     * @return 两整数之和
     */
    int getSum(int a, int b) {
        // 当进位为0时，计算结束
        while (b != 0) {
            // 计算进位
            int carry = (a & b) << 1;
            // 计算不考虑进位的加法
            a = a ^ b;
            // 将进位赋值给b，继续下一轮计算
            b = carry;
        }
        return a;
    }
    
    // 递归实现方式
    // int getSum(int a, int b) {
    //     // 基础情况：当进位为0时，返回结果
    //     if (b == 0) {
    //         return a;
    //     }
    //     // 递归计算：不考虑进位的加法 + 进位
    //     return getSum(a ^ b, (a & b) << 1);
    // }
};