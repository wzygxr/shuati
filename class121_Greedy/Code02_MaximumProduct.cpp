/**
 * 分成k份的最大乘积 - 贪心算法
 * 
 * 题目描述：
 * 一个数字n一定要分成k份，得到的乘积尽量大是多少。
 * 数字n和k，可能非常大，到达10^12规模，结果可能更大，所以返回结果对 1000000007 取模。
 * 
 * 解题思路：
 * 1. 要使乘积最大，各份数字应尽可能接近
 * 2. 最优策略是让每份的值尽可能相等
 * 3. 当n不能被k整除时，余数部分需要分配给某些份数，使其值为n/k+1
 * 
 * 数学原理：
 * - 设每份的基础值为 a = n/k
 * - 余数为 b = n%k
 * - 则有b份的值为a+1，有k-b份的值为a
 * - 最大乘积为 (a+1)^b * a^(k-b)
 * 
 * 时间复杂度：O(log n) - 快速幂的时间复杂度
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * - 类似于均值不等式的应用
 * - 大厂笔试真题
 */

#include <iostream>
using namespace std;

/**
 * 快速幂运算，用于计算大数幂次方并取模
 * 
 * @param x   底数
 * @param n   指数
 * @param mod 模数
 * @return (x^n) % mod 的结果
 */
long long power(long long x, int n, int mod) {
    long long ans = 1;
    while (n > 0) {
        // 如果n的最低位为1，则将当前x乘入结果
        if ((n & 1) == 1) {
            ans = (ans * x) % mod;
        }
        // x自乘，相当于指数翻倍
        x = (x * x) % mod;
        // n右移一位，相当于指数除以2
        n >>= 1;
    }
    return ans;
}

/**
 * 贪心解法（最优解）
 * 
 * @param n 总数
 * @param k 要分成的份数
 * @return 最大乘积对1000000007取模的结果
 */
int maxValue2(long long n, int k) {
    int mod = 1000000007;
    long long a = n / k;  // 每份的基础值
    int b = n % k;        // 余数
    
    // b份的值为a+1，k-b份的值为a
    long long part1 = power(a + 1, b, mod);  // (a+1)的b次方
    long long part2 = power(a, k - b, mod);  // a的(k-b)次方
    
    // 返回结果：(a+1)^b * a^(k-b) 对mod取模
    return (part1 * part2) % mod;
}

// 测试代码
int main() {
    // 测试用例
    cout << maxValue2(10, 3) << endl;  // 应该输出 36 (4*3*3)
    cout << maxValue2(15, 4) << endl;  // 应该输出 162 (4*4*4*3)
    return 0;
}