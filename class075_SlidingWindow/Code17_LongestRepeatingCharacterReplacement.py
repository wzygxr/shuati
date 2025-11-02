# -*- coding: utf-8 -*-
"""
424. 替换后的最长重复字符问题解决方案

问题描述：
给你一个字符串 s 和一个整数 k 。你可以选择字符串中的任一字符，并将其更改为任何其他大写英文字符。
该操作最多可执行 k 次。
在执行上述操作后，返回包含相同字母的最长子字符串的长度。

解题思路：
使用滑动窗口维护一个窗口，窗口内最多有k个字符可以被替换成其他字符
核心思想：窗口大小 - 窗口内出现次数最多的字符数量 <= k

算法复杂度分析：
时间复杂度：O(n)，其中n是字符串长度
空间复杂度：O(1)，只需要26个字母的计数数组

是否最优解：是

相关题目链接：
LeetCode 424. 替换后的最长重复字符
https://leetcode.cn/problems/longest-repeating-character-replacement/

其他平台类似题目：
1. 牛客网 - 替换后的最长重复字符
   https://www.nowcoder.com/practice/1266570c4a06487981ed50e84e8b720d
2. LintCode 424. 替换后的最长重复字符
   https://www.lintcode.com/problem/424/
3. HackerRank - Longest Repeating Character Replacement
   https://www.hackerrank.com/challenges/longest-repeating-character-replacement/problem
4. CodeChef - REPLACE - Character Replacement
   https://www.codechef.com/problems/REPLACE
5. AtCoder - ABC146 D - Enough Array
   https://atcoder.jp/contests/abc146/tasks/abc146_d
6. 洛谷 P1886 滑动窗口
   https://www.luogu.com.cn/problem/P1886
7. 杭电OJ 4193 Sliding Window
   http://acm.hdu.edu.cn/showproblem.php?pid=4193
8. POJ 2823 Sliding Window
   http://poj.org/problem?id=2823
9. UVa OJ 11536 - Smallest Sub-Array
   https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
10. SPOJ - ADAFRIEN - Ada and Friends
    https://www.spoj.com/problems/ADAFRIEN/

工程化考量：
1. 异常处理：处理空字符串等边界情况
2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
3. 可读性：变量命名清晰，添加详细注释，提供测试用例
"""


class Solution:
    """
    424. 替换后的最长重复字符解决方案类
    """
    
    def characterReplacement(self, s: str, k: int) -> int:
        """
        计算替换k个字符后能获得的最长重复字符子串长度
        
        Args:
            s (str): 输入字符串，只包含大写英文字母
            k (int): 最多可以替换的字符次数
            
        Returns:
            int: 最长重复字符子串的长度
            
        Examples:
            >>> solution = Solution()
            >>> solution.characterReplacement("ABAB", 2)
            4
            >>> solution.characterReplacement("AABABBA", 1)
            4
        """
        # 异常情况处理
        if not s:
            return 0
        
        n = len(s)
        # 记录窗口内各字符的出现次数（A-Z共26个字母）
        count = [0] * 26
        max_count = 0     # 窗口内出现次数最多的字符数量
        max_length = 0    # 最长子串长度
        left = 0          # 窗口左边界
        
        # 滑动窗口右边界
        for right in range(n):
            # 当前右边界字符计数加1
            count[ord(s[right]) - ord('A')] += 1
            # 更新窗口内最大字符计数
            max_count = max(max_count, count[ord(s[right]) - ord('A')])
            
            # 如果窗口大小减去最大字符计数大于k，说明需要替换的字符超过k个
            # 需要收缩左边界
            # 核心条件：窗口大小 - 最多字符数量 > k 时，需要收缩窗口
            while right - left + 1 - max_count > k:
                # 移除左边界字符
                count[ord(s[left]) - ord('A')] -= 1
                # 移动左边界
                left += 1
                # 注意：这里不需要重新计算max_count，因为即使max_count变小了
                # 也不会影响最终结果，我们只需要记录历史最大值
            
            # 更新最大长度（当前窗口大小）
            max_length = max(max_length, right - left + 1)
        
        return max_length
    
    def characterReplacementOptimized(self, s: str, k: int) -> int:
        """
        优化版本：使用历史最大值，避免每次重新计算max_count
        时间复杂度：O(n)，空间复杂度：O(1)
        
        Args:
            s (str): 输入字符串，只包含大写英文字母
            k (int): 最多可以替换的字符次数
            
        Returns:
            int: 最长重复字符子串的长度
        """
        # 异常情况处理
        if not s:
            return 0
        
        n = len(s)
        # 记录窗口内各字符的出现次数
        count = [0] * 26
        max_count = 0     # 历史最大字符计数
        max_length = 0    # 最长子串长度
        left = 0          # 窗口左边界
        
        # 滑动窗口遍历字符串
        for right in range(n):
            # 右边界字符计数加1
            count[ord(s[right]) - ord('A')] += 1
            # 更新历史最大字符计数
            max_count = max(max_count, count[ord(s[right]) - ord('A')])
            
            # 关键优化：使用历史最大值，即使窗口收缩后max_count变小
            # 也不会影响结果，因为我们需要的是历史最大值
            # 当需要替换的字符数超过k时，收缩窗口
            if right - left + 1 - max_count > k:
                # 移除左边界字符
                count[ord(s[left]) - ord('A')] -= 1
                # 移动左边界
                left += 1
            
            # 更新最大长度（当前窗口大小）
            max_length = max(max_length, right - left + 1)
        
        return max_length


def test_character_replacement():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    s1 = "ABAB"
    k1 = 2
    result1 = solution.characterReplacement(s1, k1)
    print(f"输入: s = \"{s1}\", k = {k1}")
    print(f"输出: {result1}")
    print("预期: 4")
    print("解释: 将两个'A'替换为'B'，得到'BBBB'，长度为4")
    print()
    
    # 测试用例2
    s2 = "AABABBA"
    k2 = 1
    result2 = solution.characterReplacement(s2, k2)
    print(f"输入: s = \"{s2}\", k = {k2}")
    print(f"输出: {result2}")
    print("预期: 4")
    print("解释: 将中间的'A'替换为'B'，得到'AABBBBA'，最长'B'子串长度为4")
    print()
    
    # 测试用例3：边界情况
    s3 = "AAAA"
    k3 = 2
    result3 = solution.characterReplacement(s3, k3)
    print(f"输入: s = \"{s3}\", k = {k3}")
    print(f"输出: {result3}")
    print("预期: 4")
    print("解释: 所有字符都相同，无需替换")
    print()
    
    # 测试用例4：空字符串
    s4 = ""
    k4 = 0
    result4 = solution.characterReplacement(s4, k4)
    print(f"输入: s = \"{s4}\", k = {k4}")
    print(f"输出: {result4}")
    print("预期: 0")
    print("解释: 空字符串")
    print()
    
    # 测试用例5：k=0的情况
    s5 = "ABCDE"
    k5 = 0
    result5 = solution.characterReplacement(s5, k5)
    print(f"输入: s = \"{s5}\", k = {k5}")
    print(f"输出: {result5}")
    print("预期: 1")
    print("解释: 不能替换任何字符，最长重复字符子串长度为1")
    
    # 测试优化版本
    print("\n=== 优化版本测试 ===")
    result1_opt = solution.characterReplacementOptimized(s1, k1)
    print(f"优化版本结果1: {result1_opt}")
    
    result2_opt = solution.characterReplacementOptimized(s2, k2)
    print(f"优化版本结果2: {result2_opt}")


if __name__ == "__main__":
    test_character_replacement()