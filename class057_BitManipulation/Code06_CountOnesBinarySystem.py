"""
汉明距离 - Hamming Distance
测试链接 : https://leetcode.cn/problems/hamming-distance/
相关题目:
1. 位1的个数 - Number of 1 Bits: https://leetcode.cn/problems/number-of-1-bits/
2. 汉明距离总和 - Total Hamming Distance: https://leetcode.cn/problems/total-hamming-distance/
3. 2的幂 - Power of Two: https://leetcode.cn/problems/power-of-two/
4. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
5. 颠倒二进制位 - Reverse Bits: https://leetcode.cn/problems/reverse-bits/

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
汉明距离就是两个数异或结果中1的个数。
例如：x = 1 (0001), y = 4 (0100)
x ^ y = 0101，其中有2个1，所以汉明距离是2。

计算一个数二进制中1的个数有多种方法：
1. Brian Kernighan算法：n & (n-1) 可以移除最右边的1
2. 分治法：通过位运算并行计算每两位、每四位、每八位...中1的个数

这里使用的是分治法，通过并行计算来统计1的个数。

时间复杂度：O(1)
空间复杂度：O(1)

补充题目：
1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
"""


class Solution:
    def hammingDistance(self, x: int, y: int) -> int:
        """
        计算两个整数之间的汉明距离
        汉明距离就是两个数异或结果中1的个数
        
        :param x: 第一个整数
        :param y: 第二个整数
        :return: 汉明距离（二进制位不同的位置的数目）
        """
        # 两个数异或的结果中1的个数就是汉明距离
        return self.cntOnes(x ^ y)

    # 返回n的二进制中有几个1
    # 这个实现脑洞太大了
    def cntOnes(self, n: int) -> int:
        """
        计算一个整数的二进制表示中1的个数
        使用分治法：通过并行计算来统计1的个数
        
        :param n: 待计算的整数
        :return: 二进制表示中1的个数
        """
        # 0x55555555 = 01010101010101010101010101010101 (奇数位为1)
        # 计算每两位中1的个数
        n = (n & 0x55555555) + ((n >> 1) & 0x55555555)
        
        # 0x33333333 = 00110011001100110011001100110011 (每两位为11)
        # 计算每四位中1的个数
        n = (n & 0x33333333) + ((n >> 2) & 0x33333333)
        
        # 0x0f0f0f0f = 00001111000011110000111100001111 (每四位为1111)
        # 计算每八位中1的个数
        n = (n & 0x0f0f0f0f) + ((n >> 4) & 0x0f0f0f0f)
        
        # 0x00ff00ff = 00000000111111110000000011111111
        # 计算每十六位中1的个数
        n = (n & 0x00ff00ff) + ((n >> 8) & 0x00ff00ff)
        
        # 0x0000ffff = 00000000000000001111111111111111
        # 计算每三十二位中1的个数
        n = (n & 0x0000ffff) + ((n >> 16) & 0x0000ffff)
        
        return n
    
    # 另一种实现方式（Brian Kernighan算法）
    # def cntOnes(self, n: int) -> int:
    #     count = 0
    #     while n != 0:
    #         count += 1
    #         n &= n - 1  # 移除最右边的1
    #     return count


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1：正常情况
    x1, y1 = 1, 4
    result1 = solution.hammingDistance(x1, y1)
    # 预期结果: 2
    print(f"测试用例1 - 输入: x={x1}, y={y1}")
    print(f"结果: {result1} (预期: 2)")
    
    # 测试用例2：边界情况
    x2, y2 = 3, 1
    result2 = solution.hammingDistance(x2, y2)
    # 预期结果: 1
    print(f"测试用例2 - 输入: x={x2}, y={y2}")
    print(f"结果: {result2} (预期: 1)")