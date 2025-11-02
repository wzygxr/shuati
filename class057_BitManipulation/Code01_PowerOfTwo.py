"""
2的幂 - Power of Two
测试链接 : https://leetcode.cn/problems/power-of-two/
相关题目:
1. 4的幂 - Power of Four: https://leetcode.cn/problems/power-of-four/
2. 3的幂 - Power of Three: https://leetcode.cn/problems/power-of-three/
3. 找不同 - Find the Difference: https://leetcode.cn/problems/find-the-difference/
4. 缺失的数字 - Missing Number: https://leetcode.cn/problems/missing-number/
5. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/

题目描述：
给你一个整数 n，请你判断该整数是否是 2 的幂次方。如果是，返回 true ；否则，返回 false 。
如果存在一个整数 x 使得 n == 2^x ，则认为 n 是 2 的幂次方。

解题思路：
一个数如果是2的幂次方，那么它的二进制表示中只有一个1，例如：
1 -> 1 (二进制)
2 -> 10 (二进制)
4 -> 100 (二进制)
8 -> 1000 (二进制)

对于这样的数n，n-1的二进制表示会是：
1-1 = 0 -> 0 (二进制)
2-1 = 1 -> 1 (二进制)
4-1 = 3 -> 11 (二进制)
8-1 = 7 -> 111 (二进制)

所以 n & (n-1) 的结果会是0。

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
    def isPowerOfTwo(self, n: int) -> bool:
        """
        判断一个整数是否是2的幂次方
        使用位运算技巧：n & (n-1) 可以消除n的二进制表示中最右边的1
        如果n是2的幂，则其二进制表示中只有一个1，所以n & (n-1)的结果为0
        
        :param n: 待判断的整数
        :return: 如果是2的幂次方返回True，否则返回False
        """
        # n > 0 确保是正数
        # n == (n & -n) 判断是否只有一个位为1
        # n & -n 可以提取出n的二进制中最右边的1
        return n > 0 and n == (n & -n)
    
    # 另一种实现方式
    # def isPowerOfTwo(self, n: int) -> bool:
    #     return n > 0 and (n & (n - 1)) == 0


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1：正常情况（是2的幂）
    n1 = 16
    result1 = solution.isPowerOfTwo(n1)
    print(f"测试用例1 - 输入: {n1} (是2的幂)")
    print(f"结果: {result1} (预期: True)")
    
    # 测试用例2：正常情况（不是2的幂）
    n2 = 18
    result2 = solution.isPowerOfTwo(n2)
    print(f"测试用例2 - 输入: {n2} (不是2的幂)")
    print(f"结果: {result2} (预期: False)")
    
    # 测试用例3：边界情况（0）
    n3 = 0
    result3 = solution.isPowerOfTwo(n3)
    print(f"测试用例3 - 输入: {n3}")
    print(f"结果: {result3} (预期: False)")
    
    # 测试用例4：边界情况（负数）
    n4 = -8
    result4 = solution.isPowerOfTwo(n4)
    print(f"测试用例4 - 输入: {n4}")
    print(f"结果: {result4} (预期: False)")
    
    # 测试用例5：边界情况（1）
    n5 = 1
    result5 = solution.isPowerOfTwo(n5)
    print(f"测试用例5 - 输入: {n5}")
    print(f"结果: {result5} (预期: True)")