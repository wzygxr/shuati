"""
比特位计数（Python实现）
测试链接：https://leetcode.cn/problems/counting-bits/

题目描述：
给定一个非负整数 n，计算从 0 到 n 的每个整数的二进制表示中 1 的个数，并返回一个数组。

示例：
输入: n = 2
输出: [0,1,1]

输入: n = 5
输出: [0,1,1,2,1,2]

解题思路：
使用动态规划方法：
对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
i >> 1 相当于 i // 2（整数除法）
i & 1 判断i是否为奇数，奇数为1，偶数为0

时间复杂度：O(n)
空间复杂度：O(n)

补充题目：
1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
"""


class Code33_CountingBits:
    @staticmethod
    def count_bits(num: int) -> list:
        """
        计算0到num范围内每个数字二进制表示中1的个数
        使用动态规划方法：
        对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
        i >> 1 相当于 i // 2（整数除法）
        i & 1 判断i是否为奇数，奇数为1，偶数为0

        :param num: 非负整数
        :return: 结果数组，ans[i]表示i的二进制中1的个数
        """
        ans = [0] * (num + 1)

        # 动态规划方法
        # 对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
        # i >> 1 相当于 i // 2（整数除法）
        # i & 1 判断i是否为奇数，奇数为1，偶数为0
        for i in range(1, num + 1):
            ans[i] = ans[i >> 1] + (i & 1)

        return ans


# 测试方法
if __name__ == "__main__":
    solution = Code33_CountingBits()

    # 测试用例1：正常情况
    result1 = solution.count_bits(2)
    # 预期结果: [0,1,1]
    print("Test 1:", result1)

    # 测试用例2：正常情况
    result2 = solution.count_bits(5)
    # 预期结果: [0,1,1,2,1,2]
    print("Test 2:", result2)

    # 测试用例3：边界情况
    result3 = solution.count_bits(0)
    # 预期结果: [0]
    print("Test 3:", result3)