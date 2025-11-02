#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
LeetCode 76. 最小覆盖子串 (Minimum Window Substring)

题目描述:
给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。

注意：
- 对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量。
- 如果 s 中存在这样的子串，我们保证它是唯一的答案。

示例1:
输入: s = "ADOBECODEBANC", t = "ABC"
输出: "BANC"
解释: 最小覆盖子串 "BANC" 包含 'A', 'B', 'C' 各一个。

示例2:
输入: s = "a", t = "a"
输出: "a"

示例3:
输入: s = "a", t = "aa"
输出: ""
解释: t 中有两个 'a'，但 s 中只有一个 'a'，所以返回空字符串。

提示:
- 1 <= s.length, t.length <= 10^5
- s 和 t 由英文字母组成

题目链接: https://leetcode.cn/problems/minimum-window-substring/

解题思路:
这道题可以使用滑动窗口（双指针）的方法来解决：

方法一（滑动窗口 + 字典）：
1. 使用两个指针 left 和 right 表示当前窗口的左右边界
2. 使用字典记录 t 中每个字符的出现次数
3. 使用另一个字典记录当前窗口中包含 t 字符的情况
4. 移动右指针扩展窗口，直到窗口包含 t 的所有字符
5. 然后移动左指针收缩窗口，找到最小覆盖子串

时间复杂度: O(n + m)，n为s长度，m为t长度
空间复杂度: O(m)，存储t的字符频率
是否最优解：是
"""

from collections import defaultdict

class Solution:
    """
    最小覆盖子串解决方案类
    """
    
    @staticmethod
    def min_window(s: str, t: str) -> str:
        """
        解法一: 滑动窗口 + 字典（最优解）
        
        Args:
            s: 源字符串
            t: 目标字符串
            
        Returns:
            str: 最小覆盖子串
            
        时间复杂度: O(n + m) - n为s长度，m为t长度
        空间复杂度: O(m) - 存储t的字符频率
        """
        if not s or not t or len(s) < len(t):
            return ""
        
        # 记录t中每个字符的出现次数
        target_dict = defaultdict(int)
        for char in t:
            target_dict[char] += 1
        
        # 记录当前窗口中字符的出现次数
        window_dict = defaultdict(int)
        
        left, right = 0, 0
        min_len = float('inf')
        min_start = 0
        required = len(target_dict)  # 需要匹配的字符种类数
        formed = 0  # 当前窗口中已匹配的字符种类数
        
        while right < len(s):
            right_char = s[right]
            window_dict[right_char] += 1
            
            # 如果当前字符在t中，且窗口中出现次数等于t中出现次数
            if (right_char in target_dict and 
                window_dict[right_char] == target_dict[right_char]):
                formed += 1
            
            # 当窗口包含t的所有字符时，尝试收缩窗口
            while left <= right and formed == required:
                left_char = s[left]
                
                # 更新最小覆盖子串
                if right - left + 1 < min_len:
                    min_len = right - left + 1
                    min_start = left
                
                # 移动左指针
                window_dict[left_char] -= 1
                if (left_char in target_dict and 
                    window_dict[left_char] < target_dict[left_char]):
                    formed -= 1
                left += 1
            
            right += 1
        
        return "" if min_len == float('inf') else s[min_start:min_start + min_len]
    
    @staticmethod
    def min_window_optimized(s: str, t: str) -> str:
        """
        解法二: 优化版滑动窗口（使用数组替代字典）
        
        Args:
            s: 源字符串
            t: 目标字符串
            
        Returns:
            str: 最小覆盖子串
            
        时间复杂度: O(n + m)
        空间复杂度: O(1) - 固定大小的数组
        """
        if not s or not t or len(s) < len(t):
            return ""
        
        # 使用数组记录字符频率（假设字符为ASCII）
        target_freq = [0] * 128
        window_freq = [0] * 128
        
        for char in t:
            target_freq[ord(char)] += 1
        
        left, right = 0, 0
        min_len = float('inf')
        min_start = 0
        required = 0
        
        # 计算需要匹配的字符种类数
        for freq in target_freq:
            if freq > 0:
                required += 1
        
        formed = 0
        
        while right < len(s):
            right_char = s[right]
            right_char_code = ord(right_char)
            window_freq[right_char_code] += 1
            
            # 如果当前字符在t中，且窗口中出现次数等于t中出现次数
            if (target_freq[right_char_code] > 0 and 
                window_freq[right_char_code] == target_freq[right_char_code]):
                formed += 1
            
            # 当窗口包含t的所有字符时，尝试收缩窗口
            while left <= right and formed == required:
                left_char = s[left]
                left_char_code = ord(left_char)
                
                # 更新最小覆盖子串
                if right - left + 1 < min_len:
                    min_len = right - left + 1
                    min_start = left
                
                # 移动左指针
                window_freq[left_char_code] -= 1
                if (target_freq[left_char_code] > 0 and 
                    window_freq[left_char_code] < target_freq[left_char_code]):
                    formed -= 1
                left += 1
            
            right += 1
        
        return "" if min_len == float('inf') else s[min_start:min_start + min_len]
    
    @staticmethod
    def min_window_advanced(s: str, t: str) -> str:
        """
        解法三: 进一步优化的滑动窗口（跳过无关字符）
        
        Args:
            s: 源字符串
            t: 目标字符串
            
        Returns:
            str: 最小覆盖子串
            
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        if not s or not t or len(s) < len(t):
            return ""
        
        # 使用数组记录字符频率
        target_freq = [0] * 128
        for char in t:
            target_freq[ord(char)] += 1
        
        # 预处理：只保留s中在t中出现的字符及其位置
        count = len(t)
        left, right = 0, 0
        min_len = float('inf')
        min_start = 0
        
        while right < len(s):
            right_char_code = ord(s[right])
            
            # 如果当前字符在t中，减少计数
            if target_freq[right_char_code] > 0:
                count -= 1
            
            target_freq[right_char_code] -= 1
            right += 1
            
            # 当计数为0时，表示窗口包含t的所有字符
            while count == 0:
                # 更新最小覆盖子串
                if right - left < min_len:
                    min_len = right - left
                    min_start = left
                
                # 移动左指针
                left_char_code = ord(s[left])
                target_freq[left_char_code] += 1
                if target_freq[left_char_code] > 0:
                    count += 1
                left += 1
        
        return "" if min_len == float('inf') else s[min_start:min_start + min_len]

def test():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    s1 = "ADOBECODEBANC"
    t1 = "ABC"
    expected1 = "BANC"
    print("测试用例1:")
    print(f"s = \"{s1}\", t = \"{t1}\"")
    print(f"解法一结果: \"{solution.min_window(s1, t1)}\"")
    print(f"解法二结果: \"{solution.min_window_optimized(s1, t1)}\"")
    print(f"解法三结果: \"{solution.min_window_advanced(s1, t1)}\"")
    print(f"期望: \"{expected1}\"")
    print()
    
    # 测试用例2
    s2 = "a"
    t2 = "a"
    expected2 = "a"
    print("测试用例2:")
    print(f"s = \"{s2}\", t = \"{t2}\"")
    print(f"解法一结果: \"{solution.min_window(s2, t2)}\"")
    print(f"解法二结果: \"{solution.min_window_optimized(s2, t2)}\"")
    print(f"解法三结果: \"{solution.min_window_advanced(s2, t2)}\"")
    print(f"期望: \"{expected2}\"")
    print()
    
    # 测试用例3
    s3 = "a"
    t3 = "aa"
    expected3 = ""
    print("测试用例3:")
    print(f"s = \"{s3}\", t = \"{t3}\"")
    print(f"解法一结果: \"{solution.min_window(s3, t3)}\"")
    print(f"解法二结果: \"{solution.min_window_optimized(s3, t3)}\"")
    print(f"解法三结果: \"{solution.min_window_advanced(s3, t3)}\"")
    print(f"期望: \"{expected3}\"")
    print()
    
    # 测试用例4 - 边界情况：s和t相同
    s4 = "abc"
    t4 = "abc"
    expected4 = "abc"
    print("测试用例4（s和t相同）:")
    print(f"s = \"{s4}\", t = \"{t4}\"")
    print(f"解法一结果: \"{solution.min_window(s4, t4)}\"")
    print(f"解法二结果: \"{solution.min_window_optimized(s4, t4)}\"")
    print(f"解法三结果: \"{solution.min_window_advanced(s4, t4)}\"")
    print(f"期望: \"{expected4}\"")
    print()
    
    # 测试用例5 - 边界情况：t不在s中
    s5 = "abcdef"
    t5 = "xyz"
    expected5 = ""
    print("测试用例5（t不在s中）:")
    print(f"s = \"{s5}\", t = \"{t5}\"")
    print(f"解法一结果: \"{solution.min_window(s5, t5)}\"")
    print(f"解法二结果: \"{solution.min_window_optimized(s5, t5)}\"")
    print(f"解法三结果: \"{solution.min_window_advanced(s5, t5)}\"")
    print(f"期望: \"{expected5}\"")
    print()

def performance_test():
    """
    性能测试函数
    """
    import time
    solution = Solution()
    
    # 创建长字符串进行性能测试
    long_s = "ABCDEFG" * 10000
    long_t = "ABC"
    
    # 测试解法一的性能
    start_time = time.time()
    result1 = solution.min_window(long_s, long_t)
    end_time = time.time()
    duration1 = (end_time - start_time) * 1000  # 转换为毫秒
    print(f"解法一（字典）耗时: {duration1:.2f}ms, 结果长度: {len(result1)}")
    
    # 测试解法二的性能
    start_time = time.time()
    result2 = solution.min_window_optimized(long_s, long_t)
    end_time = time.time()
    duration2 = (end_time - start_time) * 1000
    print(f"解法二（数组优化）耗时: {duration2:.2f}ms, 结果长度: {len(result2)}")
    
    # 测试解法三的性能
    start_time = time.time()
    result3 = solution.min_window_advanced(long_s, long_t)
    end_time = time.time()
    duration3 = (end_time - start_time) * 1000
    print(f"解法三（高级优化）耗时: {duration3:.2f}ms, 结果长度: {len(result3)}")
    
    # 验证结果一致性
    print(f"所有解法结果一致: {result1 == result2 == result3}")

def algorithm_analysis():
    """
    算法分析函数
    """
    print("=== 算法分析 ===")
    print("1. 解法一（滑动窗口 + 字典）")
    print("   - 时间复杂度: O(n + m) - n为s长度，m为t长度")
    print("   - 空间复杂度: O(m) - 存储t的字符频率")
    print("   - 优点: 通用性强，适用于任意字符集")
    print("   - 缺点: 字典操作有一定开销")
    print()
    
    print("2. 解法二（数组优化版）")
    print("   - 时间复杂度: O(n + m)")
    print("   - 空间复杂度: O(1) - 固定大小的数组")
    print("   - 优点: 效率高，适用于ASCII字符集")
    print("   - 缺点: 仅适用于有限字符集")
    print()
    
    print("3. 解法三（进一步优化）")
    print("   - 时间复杂度: O(n)")
    print("   - 空间复杂度: O(1)")
    print("   - 优点: 最优化实现，跳过无关字符")
    print("   - 缺点: 实现相对复杂")
    print()
    
    print("推荐使用解法二作为通用解决方案")

if __name__ == "__main__":
    print("=== 最小覆盖子串 算法实现 ===")
    print()
    
    print("=== 测试用例 ===")
    test()
    
    print("=== 性能测试 ===")
    performance_test()
    
    print("=== 算法分析 ===")
    algorithm_analysis()