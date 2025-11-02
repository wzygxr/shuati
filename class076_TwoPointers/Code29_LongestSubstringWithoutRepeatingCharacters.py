#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
LeetCode 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)

题目描述:
给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。

示例1:
输入: s = "abcabcbb"
输出: 3
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。

示例2:
输入: s = "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。

示例3:
输入: s = "pwwkew"
输出: 3
解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
    请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。

提示:
0 <= s.length <= 5 * 10^4
s 由英文字母、数字、符号和空格组成

题目链接: https://leetcode.cn/problems/longest-substring-without-repeating-characters/

解题思路:
这道题可以使用滑动窗口（双指针）的方法来解决：

方法一（滑动窗口 + 集合）：
1. 使用两个指针 left 和 right 表示当前窗口的左右边界
2. 使用集合记录当前窗口中的字符
3. 右指针向右移动，如果当前字符不在集合中，加入集合并更新最大长度
4. 如果当前字符在集合中，移动左指针直到移除重复字符

方法二（滑动窗口 + 字典优化）：
1. 使用字典记录每个字符最后出现的位置
2. 当遇到重复字符时，可以直接将左指针移动到重复字符的下一个位置
3. 避免左指针的逐步移动，提高效率

时间复杂度: O(n)，每个字符最多被访问两次
空间复杂度: O(min(m, n))，m为字符集大小
是否最优解：是
"""

class Solution:
    """
    无重复字符的最长子串解决方案类
    """
    
    @staticmethod
    def length_of_longest_substring_set(s: str) -> int:
        """
        解法一: 滑动窗口 + 集合
        
        Args:
            s: 输入字符串
            
        Returns:
            int: 无重复字符的最长子串长度
            
        时间复杂度: O(n) - 每个字符最多被访问两次
        空间复杂度: O(min(m, n)) - m为字符集大小
        """
        if not s:
            return 0
            
        char_set = set()
        max_length = 0
        left = 0
        n = len(s)
        
        for right in range(n):
            current_char = s[right]
            
            # 如果字符在集合中，移动左指针直到移除重复字符
            while current_char in char_set:
                char_set.remove(s[left])
                left += 1
                
            # 添加当前字符到集合
            char_set.add(current_char)
            # 更新最大长度
            max_length = max(max_length, right - left + 1)
            
        return max_length
    
    @staticmethod
    def length_of_longest_substring_dict(s: str) -> int:
        """
        解法二: 滑动窗口 + 字典优化（最优解）
        
        Args:
            s: 输入字符串
            
        Returns:
            int: 无重复字符的最长子串长度
            
        时间复杂度: O(n) - 每个字符只被访问一次
        空间复杂度: O(min(m, n)) - m为字符集大小
        """
        if not s:
            return 0
            
        char_dict = {}  # 存储字符最后出现的位置
        max_length = 0
        left = 0
        
        for right, char in enumerate(s):
            # 如果字符已经存在，并且其位置在左指针右侧
            if char in char_dict and char_dict[char] >= left:
                # 移动左指针到重复字符的下一个位置
                left = char_dict[char] + 1
                
            # 更新字符的最新位置
            char_dict[char] = right
            # 更新最大长度
            max_length = max(max_length, right - left + 1)
            
        return max_length
    
    @staticmethod
    def length_of_longest_substring_array(s: str) -> int:
        """
        解法三: 数组优化版（适用于ASCII字符）
        
        Args:
            s: 输入字符串
            
        Returns:
            int: 无重复字符的最长子串长度
            
        时间复杂度: O(n) - 每个字符只被访问一次
        空间复杂度: O(1) - 固定大小的数组
        """
        if not s:
            return 0
            
        # 假设字符集为ASCII，使用数组记录字符最后出现的位置
        last_index = [-1] * 128  # ASCII字符集大小
        
        max_length = 0
        left = 0
        
        for right, char in enumerate(s):
            char_code = ord(char)
            
            # 如果字符已经存在，并且其位置在左指针右侧
            if last_index[char_code] >= left:
                # 移动左指针到重复字符的下一个位置
                left = last_index[char_code] + 1
                
            # 更新字符的最新位置
            last_index[char_code] = right
            # 更新最大长度
            max_length = max(max_length, right - left + 1)
            
        return max_length

def test():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    s1 = "abcabcbb"
    expected1 = 3
    print("测试用例1:")
    print(f"输入: \"{s1}\"")
    print(f"解法一结果: {solution.length_of_longest_substring_set(s1)}")
    print(f"解法二结果: {solution.length_of_longest_substring_dict(s1)}")
    print(f"解法三结果: {solution.length_of_longest_substring_array(s1)}")
    print(f"期望: {expected1}")
    print()
    
    # 测试用例2
    s2 = "bbbbb"
    expected2 = 1
    print("测试用例2:")
    print(f"输入: \"{s2}\"")
    print(f"解法一结果: {solution.length_of_longest_substring_set(s2)}")
    print(f"解法二结果: {solution.length_of_longest_substring_dict(s2)}")
    print(f"解法三结果: {solution.length_of_longest_substring_array(s2)}")
    print(f"期望: {expected2}")
    print()
    
    # 测试用例3
    s3 = "pwwkew"
    expected3 = 3
    print("测试用例3:")
    print(f"输入: \"{s3}\"")
    print(f"解法一结果: {solution.length_of_longest_substring_set(s3)}")
    print(f"解法二结果: {solution.length_of_longest_substring_dict(s3)}")
    print(f"解法三结果: {solution.length_of_longest_substring_array(s3)}")
    print(f"期望: {expected3}")
    print()
    
    # 测试用例4 - 边界情况：空字符串
    s4 = ""
    expected4 = 0
    print("测试用例4（空字符串）:")
    print(f"输入: \"{s4}\"")
    print(f"解法一结果: {solution.length_of_longest_substring_set(s4)}")
    print(f"解法二结果: {solution.length_of_longest_substring_dict(s4)}")
    print(f"解法三结果: {solution.length_of_longest_substring_array(s4)}")
    print(f"期望: {expected4}")
    print()
    
    # 测试用例5 - 边界情况：单个字符
    s5 = "a"
    expected5 = 1
    print("测试用例5（单个字符）:")
    print(f"输入: \"{s5}\"")
    print(f"解法一结果: {solution.length_of_longest_substring_set(s5)}")
    print(f"解法二结果: {solution.length_of_longest_substring_dict(s5)}")
    print(f"解法三结果: {solution.length_of_longest_substring_array(s5)}")
    print(f"期望: {expected5}")
    print()
    
    # 测试用例6 - 复杂情况
    s6 = "dvdf"
    expected6 = 3
    print("测试用例6（复杂情况）:")
    print(f"输入: \"{s6}\"")
    print(f"解法一结果: {solution.length_of_longest_substring_set(s6)}")
    print(f"解法二结果: {solution.length_of_longest_substring_dict(s6)}")
    print(f"解法三结果: {solution.length_of_longest_substring_array(s6)}")
    print(f"期望: {expected6}")
    print()

def performance_test():
    """
    性能测试函数
    """
    import time
    solution = Solution()
    
    # 创建长字符串进行性能测试
    long_string = ''.join(chr(ord('a') + i % 26) for i in range(100000))
    
    # 测试解法一的性能
    start_time = time.time()
    result1 = solution.length_of_longest_substring_set(long_string)
    end_time = time.time()
    duration1 = (end_time - start_time) * 1000  # 转换为毫秒
    print(f"解法一（集合）耗时: {duration1:.2f}ms, 结果: {result1}")
    
    # 测试解法二的性能
    start_time = time.time()
    result2 = solution.length_of_longest_substring_dict(long_string)
    end_time = time.time()
    duration2 = (end_time - start_time) * 1000
    print(f"解法二（字典优化）耗时: {duration2:.2f}ms, 结果: {result2}")
    
    # 测试解法三的性能
    start_time = time.time()
    result3 = solution.length_of_longest_substring_array(long_string)
    end_time = time.time()
    duration3 = (end_time - start_time) * 1000
    print(f"解法三（数组优化）耗时: {duration3:.2f}ms, 结果: {result3}")
    
    # 验证结果一致性
    print(f"所有解法结果一致: {result1 == result2 == result3}")

def algorithm_analysis():
    """
    算法分析函数
    """
    print("=== 算法分析 ===")
    print("1. 解法一（滑动窗口 + 集合）")
    print("   - 时间复杂度: O(n) - 每个字符最多被访问两次")
    print("   - 空间复杂度: O(min(m, n)) - m为字符集大小")
    print("   - 优点: 实现简单，易于理解")
    print("   - 缺点: 最坏情况下需要逐步移动左指针")
    print()
    
    print("2. 解法二（滑动窗口 + 字典优化）")
    print("   - 时间复杂度: O(n) - 每个字符只被访问一次")
    print("   - 空间复杂度: O(min(m, n)) - m为字符集大小")
    print("   - 优点: 效率最高，直接跳转到重复字符位置")
    print("   - 缺点: 需要额外的字典空间")
    print()
    
    print("3. 解法三（数组优化版）")
    print("   - 时间复杂度: O(n) - 每个字符只被访问一次")
    print("   - 空间复杂度: O(1) - 固定大小的数组")
    print("   - 优点: 空间效率最高，适用于ASCII字符集")
    print("   - 缺点: 仅适用于有限字符集")
    print()
    
    print("推荐使用解法二作为通用解决方案")

if __name__ == "__main__":
    print("=== 无重复字符的最长子串 算法实现 ===")
    print()
    
    print("=== 测试用例 ===")
    test()
    
    print("=== 性能测试 ===")
    performance_test()
    
    print("=== 算法分析 ===")
    algorithm_analysis()