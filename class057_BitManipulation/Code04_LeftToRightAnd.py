"""
数字范围按位与 - Bitwise AND of Numbers Range
测试链接 : https://leetcode.cn/problems/bitwise-and-of-numbers-range/
相关题目:
1. 2的幂 - Power of Two: https://leetcode.cn/problems/power-of-two/
2. 4的幂 - Power of Four: https://leetcode.cn/problems/power-of-four/
3. 3的幂 - Power of Three: https://leetcode.cn/problems/power-of-three/
4. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
5. 汉明距离 - Hamming Distance: https://leetcode.cn/problems/hamming-distance/

题目描述：
给你两个整数 left 和 right ，表示区间 [left, right] ，返回此区间内所有数字按位AND的结果。

示例：
输入：left = 5, right = 7
输出：4
解释：5 & 6 & 7 = 4

输入：left = 0, right = 0
输出：0

输入：left = 1, right = 2147483647
输出：0

提示：
0 <= left <= right <= 2^31 - 1

解题思路：
方法1：Brian Kernighan算法
当left < right时，区间内一定存在相邻的两个数，它们的最低位分别是0和1，所以按位AND的结果最低位一定是0。
我们可以不断移除right最右边的1，直到left == right为止。

方法2：找公共前缀
所有数字按位AND的结果就是left和right的二进制表示的公共前缀。

时间复杂度：O(log n)，其中n是数字的位数
空间复杂度：O(1)

补充题目：
1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
"""


class Solution:
    def rangeBitwiseAnd(self, left: int, right: int) -> int:
        """
        计算区间[left, right]内所有数字按位AND的结果
        使用Brian Kernighan算法：不断移除right最右边的1，直到left >= right
        
        :param left: 区间左端点
        :param right: 区间右端点
        :return: 区间内所有数字按位AND的结果
        """
        # Brian Kernighan算法的应用
        # 不断移除right最右边的1，直到left >= right
        while left < right:
            # right & -right 可以提取出right最右边的1
            # right -= right & -right 相当于移除了最右边的1
            right -= right & -right
        return right
    
    # 另一种实现方式（找公共前缀）
    # def rangeBitwiseAnd(self, left: int, right: int) -> int:
    #     shift = 0
    #     # 找到left和right的公共前缀
    #     while left != right:
    #         left >>= 1
    #         right >>= 1
    #         shift += 1
    #     return left << shift


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1：正常情况
    left1, right1 = 5, 7
    result1 = solution.rangeBitwiseAnd(left1, right1)
    print(f"测试用例1 - 输入: left={left1}, right={right1}")
    print(f"结果: {result1} (预期: 4)")
    
    # 测试用例2：边界情况
    left2, right2 = 0, 0
    result2 = solution.rangeBitwiseAnd(left2, right2)
    print(f"测试用例2 - 输入: left={left2}, right={right2}")
    print(f"结果: {result2} (预期: 0)")
    
    # 测试用例3：大范围
    left3, right3 = 1, 2147483647
    result3 = solution.rangeBitwiseAnd(left3, right3)
    print(f"测试用例3 - 输入: left={left3}, right={right3}")
    print(f"结果: {result3} (预期: 0)")
    
    # 测试用例4：小范围
    left4, right4 = 26, 30
    result4 = solution.rangeBitwiseAnd(left4, right4)
    print(f"测试用例4 - 输入: left={left4}, right={right4}")
    print(f"结果: {result4} (预期: 24)")