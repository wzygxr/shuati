"""
位1的个数 - Number of 1 Bits
测试链接 : https://leetcode.cn/problems/number-of-1-bits/
相关题目:
1. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
2. 汉明距离 - Hamming Distance: https://leetcode.cn/problems/hamming-distance/
3. 汉明距离总和 - Total Hamming Distance: https://leetcode.cn/problems/total-hamming-distance/
4. 2的幂 - Power of Two: https://leetcode.cn/problems/power-of-two/
5. 颠倒二进制位 - Reverse Bits: https://leetcode.cn/problems/reverse-bits/

题目描述：
编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量).

示例：
输入：n = 11 (控制台输入 00000000000000000000000000001011)
输出：3
解释：输入的二进制串 00000000000000000000000000001011 中，共有三位为 '1'。

输入：n = 128 (控制台输入 00000000000000000000000010000000)
输出：1
解释：输入的二进制串 00000000000000000000000010000000 中，共有一位为 '1'。

输入：n = 4294967293 (控制台输入 11111111111111111111111111111101，部分语言中 n = -3）
输出：31
解释：输入的二进制串 11111111111111111111111111111101 中，共有 31 位为 '1'。

提示：
输入必须是长度为 32 的 二进制串 。
注意：本题与主站 191 题相同

解题思路：
这道题要求计算一个无符号整数的二进制表示中1的个数，有多种解法：

方法1：逐位检查
通过循环32次，每次检查最低位是否为1，然后右移一位。

方法2：Brian Kernighan算法
通过 n & (n-1) 可以移除n最右边的1，直到n为0为止，统计操作次数。

方法3：分治法
通过并行计算每两位、每四位、每八位...中1的个数，和Code06_CountOnesBinarySystem中实现的一样。

时间复杂度：
方法1：O(1)（固定32次操作）
方法2：O(k)（k为1的个数）
方法3：O(1)

空间复杂度：O(1)

补充题目：
1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
"""


class Solution:
    def hammingWeight(self, n: int) -> int:
        """
        计算一个整数的二进制表示中1的个数（汉明重量）
        使用逐位检查方法：循环32次，每次检查最低位是否为1
        
        :param n: 无符号整数
        :return: 二进制表示中1的个数
        """
        count = 0
        # 逐位检查32位
        for i in range(32):
            # 检查最低位是否为1
            if (n & (1 << i)) != 0:
                count += 1
        return count
    
    # 方法2：Brian Kernighan算法
    # def hammingWeight(self, n: int) -> int:
    #     count = 0
    #     # n不为0时继续循环
    #     while n != 0:
    #         count += 1
    #         # 移除最右边的1
    #         n &= n - 1
    #     return count
    
    # 方法3：分治法（和Code06中的cntOnes方法一样）
    # def hammingWeight(self, n: int) -> int:
    #     n = (n & 0x55555555) + ((n >> 1) & 0x55555555)
    #     n = (n & 0x33333333) + ((n >> 2) & 0x33333333)
    #     n = (n & 0x0f0f0f0f) + ((n >> 4) & 0x0f0f0f0f)
    #     n = (n & 0x00ff00ff) + ((n >> 8) & 0x00ff00ff)
    #     n = (n & 0x0000ffff) + ((n >> 16) & 0x0000ffff)
    #     return n


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1：正常情况
    n1 = 11
    result1 = solution.hammingWeight(n1)
    # 预期结果: 3
    print(f"测试用例1 - 输入: {n1}")
    print(f"结果: {result1} (预期: 3)")
    
    # 测试用例2：正常情况
    n2 = 128
    result2 = solution.hammingWeight(n2)
    # 预期结果: 1
    print(f"测试用例2 - 输入: {n2}")
    print(f"结果: {result2} (预期: 1)")
    
    # 测试用例3：边界情况
    n3 = 4294967293  # -3的无符号表示
    result3 = solution.hammingWeight(n3)
    # 预期结果: 31
    print(f"测试用例3 - 输入: {n3}")
    print(f"结果: {result3} (预期: 31)")