"""
比特位计数 - Counting Bits
测试链接 : https://leetcode.cn/problems/counting-bits/
相关题目:
1. 位1的个数 - Number of 1 Bits: https://leetcode.cn/problems/number-of-1-bits/
2. 2的幂 - Power of Two: https://leetcode.cn/problems/power-of-two/
3. 汉明距离 - Hamming Distance: https://leetcode.cn/problems/hamming-distance/
4. 汉明距离总和 - Total Hamming Distance: https://leetcode.cn/problems/total-hamming-distance/
5. 颠倒二进制位 - Reverse Bits: https://leetcode.cn/problems/reverse-bits/

题目描述：
给定一个非负整数 num。对于 0 ≤ i ≤ num 范围中的每个数字 i ，计算其二进制数中的 1 的数目并将它们作为数组返回。

示例：
输入: 2
输出: [0,1,1]

输入: 5
输出: [0,1,1,2,1,2]

解释:
0 --> 0
1 --> 1
2 --> 10
3 --> 11
4 --> 100
5 --> 101

提示：
0 <= num <= 10^5

进阶：
1. 给出时间复杂度为O(n*sizeof(integer))的解答非常容易。但你可以在线性时间O(n)内用一趟扫描做到吗？
2. 要求算法的空间复杂度为O(n)。
3. 你能进一步完善解法吗？要求在C++或任何其他语言中不使用任何内置函数（如 C++ 中的 __builtin_popcount ）来执行此操作。

解题思路：
这道题可以使用动态规划来解决，关键是要发现数字之间的规律。

对于任意一个数字i：
1. 如果i是偶数，那么它的二进制表示就是在i/2的二进制表示后面加一个0，所以1的个数和i/2相同。
2. 如果i是奇数，那么它的二进制表示就是在i/2的二进制表示后面加一个1，所以1的个数是i/2的1的个数加1。

也可以通过Brian Kernighan算法来理解：i & (i-1)可以移除i最右边的1，所以i的1的个数等于i&(i-1)的1的个数加1。

时间复杂度：O(n)
空间复杂度：O(1)（不考虑返回数组）

补充题目：
1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
"""


class Solution:
    def countBits(self, num: int) -> list[int]:
        """
        计算0到num范围内每个数字二进制表示中1的个数
        使用动态规划方法：
        对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
        i >> 1 相当于 i / 2（整数除法）
        i & 1 判断i是否为奇数，奇数为1，偶数为0
        
        :param num: 非负整数
        :return: 结果数组，ans[i]表示i的二进制中1的个数
        """
        ans = [0] * (num + 1)
        
        # 动态规划方法
        # 对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
        # i >> 1 相当于 i / 2（整数除法）
        # i & 1 判断i是否为奇数，奇数为1，偶数为0
        for i in range(1, num + 1):
            ans[i] = ans[i >> 1] + (i & 1)
        
        return ans


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1：正常情况
    result1 = solution.countBits(2)
    # 预期结果: [0,1,1]
    print(f"countBits(2): {result1} (预期: [0, 1, 1])")
    
    # 测试用例2：正常情况
    result2 = solution.countBits(5)
    # 预期结果: [0,1,1,2,1,2]
    print(f"countBits(5): {result2} (预期: [0, 1, 1, 2, 1, 2])")