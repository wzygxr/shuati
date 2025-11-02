#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
哈希算法经典题目集合 - Python版本

本文件包含各大算法平台（LeetCode、Codeforces、剑指Offer等）的哈希相关经典题目
每个题目都提供详细的注释、复杂度分析和多种解法

哈希算法核心思想：
1. 使用哈希表实现O(1)时间复杂度的查找、插入和删除
2. 处理哈希冲突的方法：链地址法、开放地址法、再哈希法等
3. 哈希函数设计原则：均匀分布、计算简单、冲突率低

时间复杂度分析：
- 理想情况下：O(1) 查找、插入、删除
- 最坏情况下：O(n) 当所有元素哈希到同一位置时

空间复杂度分析：
- 哈希表：O(n) 存储n个元素
- 额外空间：O(k) 用于存储辅助信息
"""

from typing import List, Dict, Set, Tuple, Optional
from collections import defaultdict, Counter
import sys


class TwoSumSolution:
    """
    LeetCode 1. 两数之和 (Two Sum)
    题目来源：https://leetcode.com/problems/two-sum/
    题目描述：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。
    
    算法思路：
    1. 使用哈希表存储每个数字及其对应的索引
    2. 遍历数组时检查 target - nums[i] 是否在哈希表中
    3. 如果存在，则返回两个索引
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    
    工程化考量：
    - 异常处理：空数组、无解情况
    - 边界条件：重复元素、负数、零
    - 性能优化：一次遍历完成查找
    """
    
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        """哈希表解法"""
        # 输入验证
        if not nums or len(nums) < 2:
            raise ValueError("数组长度必须大于等于2")
        
        num_map = {}
        
        for i, num in enumerate(nums):
            complement = target - num
            
            # 检查补数是否在哈希表中
            if complement in num_map:
                return [num_map[complement], i]
            
            # 将当前数字和索引存入哈希表
            num_map[num] = i
        
        raise ValueError("没有找到符合条件的两个数")
    
    def twoSumBruteForce(self, nums: List[int], target: int) -> List[int]:
        """
        两数之和的暴力解法（用于对比）
        时间复杂度：O(n²)
        空间复杂度：O(1)
        """
        for i in range(len(nums)):
            for j in range(i + 1, len(nums)):
                if nums[i] + nums[j] == target:
                    return [i, j]
        raise ValueError("没有找到符合条件的两个数")
    
    def twoSumTwoPointers(self, nums: List[int], target: int) -> List[int]:
        """
        两数之和的排序双指针解法
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        """
        # 创建索引数组
        indexed_nums = [(num, i) for i, num in enumerate(nums)]
        
        # 按数值排序
        indexed_nums.sort(key=lambda x: x[0])
        
        left, right = 0, len(nums) - 1
        while left < right:
            current_sum = indexed_nums[left][0] + indexed_nums[right][0]
            
            if current_sum == target:
                return [indexed_nums[left][1], indexed_nums[right][1]]
            elif current_sum < target:
                left += 1
            else:
                right -= 1
        
        raise ValueError("没有找到符合条件的两个数")


class GroupAnagramsSolution:
    """
    LeetCode 49. 字母异位词分组 (Group Anagrams)
    题目来源：https://leetcode.com/problems/group-anagrams/
    题目描述：给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
    
    算法思路：
    1. 使用排序后的字符串作为哈希表的键
    2. 将具有相同排序字符串的单词分组
    3. 返回分组后的结果
    
    时间复杂度：O(n * k log k)，其中n是字符串数量，k是字符串最大长度
    空间复杂度：O(n * k)
    
    优化思路：
    - 使用字符计数作为键，避免排序开销
    - 使用质数乘积作为键，减少字符串操作
    """
    
    def groupAnagrams(self, strs: List[str]) -> List[List[str]]:
        """排序字符串作为键"""
        if not strs:
            return []
        
        anagram_map = defaultdict(list)
        
        for s in strs:
            # 将字符串排序作为键
            sorted_str = ''.join(sorted(s))
            anagram_map[sorted_str].append(s)
        
        return list(anagram_map.values())
    
    def groupAnagramsOptimized(self, strs: List[str]) -> List[List[str]]:
        """
        使用字符计数作为键的优化版本
        时间复杂度：O(n * k)
        空间复杂度：O(n * k)
        """
        if not strs:
            return []
        
        anagram_map = defaultdict(list)
        
        for s in strs:
            # 使用字符计数作为键
            key = self._getCharacterCountKey(s)
            anagram_map[key].append(s)
        
        return list(anagram_map.values())
    
    def _getCharacterCountKey(self, s: str) -> str:
        """生成字符计数键"""
        count = [0] * 26
        for char in s:
            count[ord(char) - ord('a')] += 1
        
        # 将计数数组转换为字符串键
        return ''.join(f'{chr(ord("a") + i)}{count[i]}' for i in range(26) if count[i] > 0)
    
    def groupAnagramsPrime(self, strs: List[str]) -> List[List[str]]:
        """
        使用质数乘积作为键的优化版本
        时间复杂度：O(n * k)
        空间复杂度：O(n)
        """
        if not strs:
            return []
        
        # 前26个质数
        primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 
                  43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101]
        
        anagram_map = defaultdict(list)
        
        for s in strs:
            product = 1
            for char in s:
                product *= primes[ord(char) - ord('a')]
            
            anagram_map[product].append(s)
        
        return list(anagram_map.values())


class ValidAnagramSolution:
    """
    LeetCode 242. 有效的字母异位词 (Valid Anagram)
    题目来源：https://leetcode.com/problems/valid-anagram/
    题目描述：给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
    
    算法思路：
    1. 使用哈希表统计每个字符出现的次数
    2. 比较两个字符串的字符频率
    3. 如果所有字符频率相同，则是字母异位词
    
    时间复杂度：O(n)
    空间复杂度：O(1)，因为字符集大小固定为26
    """
    
    def isAnagram(self, s: str, t: str) -> bool:
        """字符计数解法"""
        if len(s) != len(t):
            return False
        
        char_count = [0] * 26
        
        # 统计字符串s的字符频率
        for char in s:
            char_count[ord(char) - ord('a')] += 1
        
        # 减去字符串t的字符频率
        for char in t:
            char_count[ord(char) - ord('a')] -= 1
            # 如果出现负数，说明t中某个字符比s中多
            if char_count[ord(char) - ord('a')] < 0:
                return False
        
        # 检查所有字符频率是否归零
        return all(count == 0 for count in char_count)
    
    def isAnagramSort(self, s: str, t: str) -> bool:
        """
        使用排序的解法
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        """
        if len(s) != len(t):
            return False
        
        return sorted(s) == sorted(t)
    
    def isAnagramUnicode(self, s: str, t: str) -> bool:
        """
        使用哈希表的通用解法（支持Unicode字符）
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if len(s) != len(t):
            return False
        
        char_map = {}
        
        # 统计字符串s的字符频率
        for char in s:
            char_map[char] = char_map.get(char, 0) + 1
        
        # 减去字符串t的字符频率
        for char in t:
            if char not in char_map:
                return False
            char_map[char] -= 1
            if char_map[char] == 0:
                del char_map[char]
        
        return len(char_map) == 0


class LongestSubstringSolution:
    """
    LeetCode 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)
    题目来源：https://leetcode.com/problems/longest-substring-without-repeating-characters/
    题目描述：给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。
    
    算法思路：
    1. 使用滑动窗口和哈希表记录字符最后出现的位置
    2. 维护左右指针，右指针遍历字符串
    3. 当遇到重复字符时，移动左指针到重复字符的下一个位置
    4. 更新最大长度
    
    时间复杂度：O(n)
    空间复杂度：O(min(m, n))，其中m是字符集大小
    """
    
    def lengthOfLongestSubstring(self, s: str) -> int:
        """滑动窗口解法"""
        if not s:
            return 0
        
        char_index_map = {}
        max_length = 0
        left = 0
        
        for right, char in enumerate(s):
            # 如果字符已经存在，并且索引在窗口内
            if char in char_index_map and char_index_map[char] >= left:
                # 移动左指针到重复字符的下一个位置
                left = char_index_map[char] + 1
            
            # 更新字符的最新位置
            char_index_map[char] = right
            
            # 更新最大长度
            max_length = max(max_length, right - left + 1)
        
        return max_length
    
    def lengthOfLongestSubstringArray(self, s: str) -> int:
        """
        使用数组代替哈希表的优化版本（仅适用于ASCII字符）
        时间复杂度：O(n)
        空间复杂度：O(128)
        """
        if not s:
            return 0
        
        char_index = [-1] * 128  # ASCII字符集
        max_length = 0
        left = 0
        
        for right, char in enumerate(s):
            char_code = ord(char)
            
            # 如果字符已经存在，并且索引在窗口内
            if char_index[char_code] >= left:
                left = char_index[char_code] + 1
            
            # 更新字符的最新位置
            char_index[char_code] = right
            
            # 更新最大长度
            max_length = max(max_length, right - left + 1)
        
        return max_length


class SubarraySumSolution:
    """
    LeetCode 560. 和为K的子数组 (Subarray Sum Equals K)
    题目来源：https://leetcode.com/problems/subarray-sum-equals-k/
    题目描述：给你一个整数数组 nums 和一个整数 k ，请你统计并返回该数组中和为 k 的连续子数组的个数。
    
    算法思路：
    1. 使用前缀和和哈希表
    2. 记录每个前缀和出现的次数
    3. 对于当前前缀和sum，检查sum - k是否在哈希表中
    4. 如果存在，则说明存在子数组和为k
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    
    def subarraySum(self, nums: List[int], k: int) -> int:
        """前缀和哈希表解法"""
        if not nums:
            return 0
        
        prefix_sum_count = defaultdict(int)
        prefix_sum_count[0] = 1  # 前缀和为0出现1次
        
        count = 0
        prefix_sum = 0
        
        for num in nums:
            prefix_sum += num
            
            # 检查是否存在前缀和等于prefix_sum - k
            if prefix_sum - k in prefix_sum_count:
                count += prefix_sum_count[prefix_sum - k]
            
            # 更新当前前缀和的出现次数
            prefix_sum_count[prefix_sum] += 1
        
        return count
    
    def subarraySumBruteForce(self, nums: List[int], k: int) -> int:
        """
        暴力解法（用于对比）
        时间复杂度：O(n²)
        空间复杂度：O(1)
        """
        count = 0
        for i in range(len(nums)):
            current_sum = 0
            for j in range(i, len(nums)):
                current_sum += nums[j]
                if current_sum == k:
                    count += 1
        return count


class FirstUniqueCharSolution:
    """
    剑指Offer 50. 第一个只出现一次的字符
    题目来源：剑指Offer面试题50
    题目描述：在字符串s中找出第一个只出现一次的字符
    
    算法思路：
    1. 使用哈希表统计每个字符出现的次数
    2. 再次遍历字符串，找到第一个出现次数为1的字符
    3. 返回该字符
    
    时间复杂度：O(n)
    空间复杂度：O(1)，因为字符集大小固定
    """
    
    def firstUniqChar(self, s: str) -> str:
        """字符计数解法"""
        if not s:
            return ' '
        
        char_count = [0] * 256  # 扩展ASCII字符集
        
        # 第一次遍历：统计字符频率
        for char in s:
            char_count[ord(char)] += 1
        
        # 第二次遍历：找到第一个唯一字符
        for char in s:
            if char_count[ord(char)] == 1:
                return char
        
        return ' '
    
    def firstUniqCharOrderedDict(self, s: str) -> str:
        """
        使用有序字典保持插入顺序的解法
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if not s:
            return ' '
        
        from collections import OrderedDict
        char_map = OrderedDict()
        
        # 统计字符频率，保持插入顺序
        for char in s:
            char_map[char] = char_map.get(char, 0) + 1
        
        # 找到第一个出现次数为1的字符
        for char, count in char_map.items():
            if count == 1:
                return char
        
        return ' '


def test_hash_algorithm_problems():
    """单元测试函数"""
    print("=== 哈希算法经典题目测试 ===\n")
    
    # 测试两数之和
    print("1. 两数之和测试:")
    two_sum = TwoSumSolution()
    nums1 = [2, 7, 11, 15]
    target1 = 9
    result1 = two_sum.twoSum(nums1, target1)
    print(f"数组: {nums1}, 目标: {target1}")
    print(f"结果: {result1}")
    
    # 测试字母异位词分组
    print("\n2. 字母异位词分组测试:")
    group_anagrams = GroupAnagramsSolution()
    strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
    anagram_groups = group_anagrams.groupAnagrams(strs)
    print(f"输入: {strs}")
    print(f"分组结果: {anagram_groups}")
    
    # 测试有效的字母异位词
    print("\n3. 有效的字母异位词测试:")
    valid_anagram = ValidAnagramSolution()
    s1, s2 = "anagram", "nagaram"
    is_anagram = valid_anagram.isAnagram(s1, s2)
    print(f"s1 = '{s1}', s2 = '{s2}'")
    print(f"是否是字母异位词: {is_anagram}")
    
    # 测试无重复字符的最长子串
    print("\n4. 无重复字符的最长子串测试:")
    longest_substring = LongestSubstringSolution()
    test_str = "abcabcbb"
    max_length = longest_substring.lengthOfLongestSubstring(test_str)
    print(f"字符串: '{test_str}'")
    print(f"最长无重复子串长度: {max_length}")
    
    # 测试和为K的子数组
    print("\n5. 和为K的子数组测试:")
    subarray_sum = SubarraySumSolution()
    nums2 = [1, 1, 1]
    k = 2
    subarray_count = subarray_sum.subarraySum(nums2, k)
    print(f"数组: {nums2}, k = {k}")
    print(f"和为{k}的子数组个数: {subarray_count}")
    
    # 测试第一个只出现一次的字符
    print("\n6. 第一个只出现一次的字符测试:")
    first_unique_char = FirstUniqueCharSolution()
    test_str2 = "leetcode"
    unique_char = first_unique_char.firstUniqChar(test_str2)
    print(f"字符串: '{test_str2}'")
    print(f"第一个只出现一次的字符: '{unique_char}'")
    
    print("\n=== 算法复杂度分析 ===")
    print("1. 两数之和: O(n) 时间, O(n) 空间")
    print("2. 字母异位词分组: O(n*k log k) 时间, O(n*k) 空间")
    print("3. 有效的字母异位词: O(n) 时间, O(1) 空间")
    print("4. 无重复字符的最长子串: O(n) 时间, O(min(m,n)) 空间")
    print("5. 和为K的子数组: O(n) 时间, O(n) 空间")
    print("6. 第一个只出现一次的字符: O(n) 时间, O(1) 空间")
    
    print("\n=== Python工程化建议 ===")
    print("1. 使用defaultdict和Counter简化代码")
    print("2. 注意Python字典的哈希冲突处理机制")
    print("3. 使用生成器表达式提高内存效率")
    print("4. 注意Python的GIL对多线程性能的影响")
    print("5. 使用类型注解提高代码可读性")
    print("6. 注意Python的动态类型特性")


if __name__ == "__main__":
    test_hash_algorithm_problems()