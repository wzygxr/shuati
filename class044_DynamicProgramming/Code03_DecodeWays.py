#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
解码方法 (Decode Ways)

题目来源：LeetCode 91. 解码方法
题目链接：https://leetcode.cn/problems/decode-ways/

题目描述：
一条包含字母 A-Z 的消息通过以下映射进行了 编码 ：
'A' -> "1"
'B' -> "2"
...
'Z' -> "26"
要 解码 已编码的消息，所有数字必须基于上述映射的方法，反向映射回字母（可能有多种方法）。
例如，"11106" 可以映射为：
"AAJF"，将消息分组为 (1 1 10 6)
"KJF"，将消息分组为 (11 10 6)
注意，消息不能分组为 (1 11 06) ，因为 "06" 不能映射为 "F" ，这是由于 "6" 和 "06" 在映射中并不等价。
给你一个只含数字的 非空 字符串 s ，请计算并返回 解码 方法的 总数 。
题目数据保证答案肯定是一个 32 位 的整数。

示例 1：
输入：s = "12"
输出：2
解释：它可以解码为 "AB"（1 2）或者 "L"（12）。

示例 2：
输入：s = "226"
输出：3
解释：它可以解码为 "BZ" (2 26), "VF" (22 6), 或者 "BBF" (2 2 6) 。

示例 3：
输入：s = "06"
输出：0
解释："06" 无法映射到 "F" ，因为存在前导零（"6" 和 "06" 并不等价）。

提示：
1 <= s.length <= 100
s 只包含数字，并且可能包含前导零。

解题思路：
这是一个典型的动态规划问题，我们需要计算字符串可以解码的方法数。
我们提供了四种解法：
1. 暴力递归：直接按照定义递归求解，但存在大量重复计算。
2. 记忆化搜索：在暴力递归的基础上，通过缓存已计算的结果来避免重复计算。
3. 动态规划：自底向上计算，先计算小问题的解，再逐步构建大问题的解。
4. 空间优化的动态规划：在动态规划的基础上，只保存必要的状态，将空间复杂度优化到O(1)。

算法复杂度分析：
- 暴力递归：时间复杂度 O(2^n)，空间复杂度 O(n)
- 记忆化搜索：时间复杂度 O(n)，空间复杂度 O(n)
- 动态规划：时间复杂度 O(n)，空间复杂度 O(n)
- 空间优化DP：时间复杂度 O(n)，空间复杂度 O(1)

工程化考量：
1. 边界处理：正确处理字符'0'的特殊情况
2. 性能优化：提供多种解法，从低效到高效，展示优化过程
3. 代码质量：清晰的变量命名和详细的注释说明
4. 测试覆盖：包含基本测试用例和边界情况测试

相关题目：
- LeetCode 639. 解码方法 II
- LintCode 512. 解码方法
- AtCoder Educational DP Contest C - Vacation
- 牛客网 动态规划专题 - 字符串解码
- HackerRank Decode Ways
- CodeChef DECODE
- SPOJ DECODEW
"""


class Solution:
    # 暴力尝试
    # 时间复杂度：O(2^n)，指数级时间复杂度
    # 空间复杂度：O(n)，递归调用栈深度
    # 问题：存在大量重复计算
    def numDecodings1(self, s: str) -> int:
        return self.f1(list(s), 0)

    # s : 数字字符串 
    # s[i....]有多少种有效的转化方案
    def f1(self, s: list, i: int) -> int:
        if i == len(s):
            return 1
        if s[i] == '0':
            return 0
        ans = self.f1(s, i + 1)
        if i + 1 < len(s) and (int(s[i]) * 10 + int(s[i + 1])) <= 26:
            ans += self.f1(s, i + 2)
        return ans

    # 暴力尝试改记忆化搜索
    # 时间复杂度：O(n)，其中n是字符串长度，每个状态只计算一次
    # 空间复杂度：O(n)，dp数组和递归调用栈
    # 优化：通过缓存已经计算的结果避免重复计算
    def numDecodings2(self, s: str) -> int:
        dp = {}
        return self.f2(list(s), 0, dp)

    def f2(self, s: list, i: int, dp: dict) -> int:
        if i == len(s):
            return 1
        if i in dp:
            return dp[i]
        if s[i] == '0':
            dp[i] = 0
            return 0
        ans = self.f2(s, i + 1, dp)
        if i + 1 < len(s) and (int(s[i]) * 10 + int(s[i + 1])) <= 26:
            ans += self.f2(s, i + 2, dp)
        dp[i] = ans
        return ans

    # 严格位置依赖的动态规划
    # 时间复杂度：O(n)，其中n是字符串长度
    # 空间复杂度：O(n)，dp数组
    # 优化：避免了递归调用的开销，自底向上计算
    def numDecodings3(self, s: str) -> int:
        n = len(s)
        dp = [0] * (n + 1)
        dp[n] = 1
        for i in range(n - 1, -1, -1):
            if s[i] == '0':
                dp[i] = 0
            else:
                dp[i] = dp[i + 1]
                if i + 1 < n and (int(s[i]) * 10 + int(s[i + 1])) <= 26:
                    dp[i] += dp[i + 2]
        return dp[0]

    # 严格位置依赖的动态规划 + 空间压缩
    # 时间复杂度：O(n)，其中n是字符串长度
    # 空间复杂度：O(1)，只使用几个变量
    # 优化：只保存必要的状态，大幅减少空间使用
    def numDecodings4(self, s: str) -> int:
        # dp[i+1]
        next_val = 1
        # dp[i+2]
        next_next = 0
        for i in range(len(s) - 1, -1, -1):
            if s[i] == '0':
                cur = 0
            else:
                cur = next_val
                if i + 1 < len(s) and (int(s[i]) * 10 + int(s[i + 1])) <= 26:
                    cur += next_next
            next_next = next_val
            next_val = cur
        return next_val


# 测试用例
if __name__ == "__main__":
    solution = Solution()
    print("测试解码方法问题：")
    
    # 测试用例1
    s1 = "12"
    print(f"s = \"{s1}\"")
    print(f"解码方法数（方法2）: {solution.numDecodings2(s1)}")
    print(f"解码方法数（方法3）: {solution.numDecodings3(s1)}")
    print(f"解码方法数（方法4）: {solution.numDecodings4(s1)}")
    
    # 测试用例2
    s2 = "226"
    print(f"\ns = \"{s2}\"")
    print(f"解码方法数（方法2）: {solution.numDecodings2(s2)}")
    print(f"解码方法数（方法3）: {solution.numDecodings3(s2)}")
    print(f"解码方法数（方法4）: {solution.numDecodings4(s2)}")
    
    # 测试用例3
    s3 = "06"
    print(f"\ns = \"{s3}\"")
    print(f"解码方法数（方法2）: {solution.numDecodings2(s3)}")
    print(f"解码方法数（方法3）: {solution.numDecodings3(s3)}")
    print(f"解码方法数（方法4）: {solution.numDecodings4(s3)}")