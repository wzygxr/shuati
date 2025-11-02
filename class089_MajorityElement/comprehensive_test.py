#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
水王数相关算法综合测试
包含所有主要的水王数相关算法实现和测试用例
"""

from typing import List
import bisect
import random
from collections import defaultdict


def find_majority_element(nums: List[int]) -> int:
    """
    基础水王数问题 (出现次数大于n/2)
    
    Args:
        nums: 输入数组
        
    Returns:
        水王数，如果不存在则返回-1
    """
    # Boyer-Moore投票算法第一阶段：找出候选元素
    candidate = 0
    count = 0
    
    for num in nums:
        if count == 0:
            candidate = num
            count = 1
        elif num == candidate:
            count += 1
        else:
            count -= 1
    
    # 验证候选元素是否真的是水王数
    count = 0
    for num in nums:
        if num == candidate:
            count += 1
    
    return candidate if count > len(nums) // 2 else -1


def find_majority_elements_ii(nums: List[int]) -> List[int]:
    """
    多数元素 II (出现次数大于n/3)
    
    Args:
        nums: 输入数组
        
    Returns:
        所有出现次数大于n/3的元素列表
    """
    # 初始化两个候选元素和它们的计数
    cand1, cand2 = 0, 0
    count1, count2 = 0, 0
    
    # 第一遍遍历，找出候选元素
    for num in nums:
        if count1 > 0 and num == cand1:
            count1 += 1
        elif count2 > 0 and num == cand2:
            count2 += 1
        elif count1 == 0:
            cand1 = num
            count1 = 1
        elif count2 == 0:
            cand2 = num
            count2 = 1
        else:
            count1 -= 1
            count2 -= 1
    
    # 第二遍遍历，统计候选元素的真实出现次数
    count1, count2 = 0, 0
    for num in nums:
        if num == cand1:
            count1 += 1
        elif num == cand2:
            count2 += 1
    
    # 构造结果列表
    result = []
    n = len(nums)
    if count1 > n // 3:
        result.append(cand1)
    if count2 > n // 3:
        result.append(cand2)
    
    return result


def find_minimum_index_valid_split(nums: List[int]) -> int:
    """
    合法分割的最小下标
    
    Args:
        nums: 输入数组
        
    Returns:
        最小分割下标，如果不存在则返回-1
    """
    # 第一步：使用Boyer-Moore投票算法找出候选元素
    candidate = 0
    count = 0
    
    # 投票阶段：找出可能的支配元素
    for num in nums:
        if count == 0:
            candidate = num
            count = 1
        elif num == candidate:
            count += 1
        else:
            count -= 1
    
    # 第二步：统计候选元素在整个数组中的出现次数
    count = 0
    for num in nums:
        if num == candidate:
            count += 1
    
    # 第三步：遍历所有可能的分割点，检查是否满足条件
    n = len(nums)
    left_count = 0  # 左半部分中候选元素的出现次数
    
    # 遍历所有可能的分割点 i (0 <= i < n-1)
    for i in range(n - 1):
        # 更新左半部分中候选元素的出现次数
        if nums[i] == candidate:
            left_count += 1
        
        # 计算右半部分中候选元素的出现次数
        right_count = count - left_count
        
        # 检查左半部分是否满足支配元素条件
        left_valid = left_count * 2 > (i + 1)
        
        # 检查右半部分是否满足支配元素条件
        right_valid = right_count * 2 > (n - i - 1)
        
        # 如果两部分都满足条件，则找到了有效分割点
        if left_valid and right_valid:
            return i
    
    # 不存在有效分割点
    return -1


def find_more_than_nk(nums: List[int], k: int) -> List[int]:
    """
    出现次数大于n/k的数
    
    Args:
        nums: 输入数组
        k: 分母参数
        
    Returns:
        所有出现次数大于n/k的元素列表
    """
    # 初始化候选元素数组 [值, 计数]
    candidates = [[0, 0] for _ in range(k - 1)]
    
    def update_candidates(num):
        # 检查是否已存在
        for i in range(k - 1):
            if candidates[i][0] == num and candidates[i][1] > 0:
                candidates[i][1] += 1
                return
        
        # 检查是否有空位
        for i in range(k - 1):
            if candidates[i][1] == 0:
                candidates[i][0] = num
                candidates[i][1] = 1
                return
        
        # 所有位置都被占用，计数都减1
        for i in range(k - 1):
            if candidates[i][1] > 0:
                candidates[i][1] -= 1
    
    # 更新候选元素
    for num in nums:
        update_candidates(num)
    
    # 验证候选元素
    result = []
    n = len(nums)
    for i in range(k - 1):
        if candidates[i][1] > 0:
            candidate = candidates[i][0]
            count = sum(1 for num in nums if num == candidate)
            if count > n // k:
                result.append(candidate)
    
    return result


class MajorityChecker:
    """
    子数组中占绝大多数的元素
    使用随机化方法实现
    """
    
    def __init__(self, arr: List[int]):
        """
        初始化函数
        
        Args:
            arr: 输入数组
        """
        self.arr = arr
        # 预处理：记录每个元素出现的所有位置
        self.positions = defaultdict(list)
        for i, val in enumerate(arr):
            self.positions[val].append(i)
    
    def query(self, left: int, right: int, threshold: int) -> int:
        """
        查询指定区间内出现次数至少为threshold的元素
        
        Args:
            left: 区间左边界（包含）
            right: 区间右边界（包含）
            threshold: 阈值
            
        Returns:
            满足条件的元素，不存在则返回-1
        """
        # 随机化方法：随机选择区间内的元素进行验证
        # 由于多数元素出现次数超过threshold，随机选择命中多数元素的概率较高
        for _ in range(20):  # 尝试20次，可以调整次数以平衡准确率和性能
            # 随机选择区间内的一个位置
            random_index = random.randint(left, right)
            candidate = self.arr[random_index]
            
            # 使用二分查找计算该候选元素在区间[left, right]内的出现次数
            positions = self.positions[candidate]
            # 找到第一个大于等于left的位置
            left_bound = bisect.bisect_left(positions, left)
            # 找到第一个大于right的位置
            right_bound = bisect.bisect_right(positions, right)
            # 计算区间内出现次数
            count = right_bound - left_bound
            
            # 如果出现次数达到阈值，返回该元素
            if count >= threshold:
                return candidate
        
        # 未找到满足条件的元素
        return -1


def print_array(arr):
    """打印数组"""
    print("[", end="")
    for i, val in enumerate(arr):
        print(val, end="")
        if i < len(arr) - 1:
            print(", ", end="")
    print("]", end="")


def print_list(lst):
    """打印列表"""
    print("[", end="")
    for i, val in enumerate(lst):
        print(val, end="")
        if i < len(lst) - 1:
            print(", ", end="")
    print("]", end="")


def main():
    """主测试函数"""
    print("=== 水王数相关算法综合测试 ===\n")
    
    # 测试用例1: 基础水王数问题
    print("1. 基础水王数问题 (出现次数大于n/2):")
    nums1 = [3, 2, 3]
    print("输入: ", end="")
    print_array(nums1)
    print()
    print("输出:", find_majority_element(nums1))
    print()
    
    nums2 = [2, 2, 1, 1, 1, 2, 2]
    print("输入: ", end="")
    print_array(nums2)
    print()
    print("输出:", find_majority_element(nums2))
    print()
    
    # 测试用例2: 多数元素 II
    print("2. 多数元素 II (出现次数大于n/3):")
    nums3 = [3, 2, 3]
    print("输入: ", end="")
    print_array(nums3)
    print()
    print("输出: ", end="")
    print_list(find_majority_elements_ii(nums3))
    print("\n")
    
    nums4 = [1]
    print("输入: ", end="")
    print_array(nums4)
    print()
    print("输出: ", end="")
    print_list(find_majority_elements_ii(nums4))
    print("\n")
    
    # 测试用例3: 合法分割的最小下标
    print("3. 合法分割的最小下标:")
    nums5 = [1, 2, 2, 2]
    print("输入: [1, 2, 2, 2]")
    print("输出:", find_minimum_index_valid_split(nums5))
    print()
    
    nums6 = [2, 1, 3, 1, 1, 1, 7, 1, 2, 1]
    print("输入: [2, 1, 3, 1, 1, 1, 7, 1, 2, 1]")
    print("输出:", find_minimum_index_valid_split(nums6))
    print()
    
    # 测试用例4: 出现次数大于n/k的数
    print("4. 出现次数大于n/k的数 (k=3):")
    nums7 = [3, 2, 3]
    print("输入: ", end="")
    print_array(nums7)
    print()
    print("输出: ", end="")
    print_list(find_more_than_nk(nums7, 3))
    print("\n")
    
    nums8 = [1, 1, 1, 2, 2, 3, 3, 3, 3, 3, 3]
    print("输入: ", end="")
    print_array(nums8)
    print()
    print("输出: ", end="")
    print_list(find_more_than_nk(nums8, 3))
    print("\n")
    
    # 测试用例5: 子数组中占绝大多数的元素
    print("5. 子数组中占绝大多数的元素:")
    arr = [1, 1, 2, 2, 1, 1]
    checker = MajorityChecker(arr)
    print("数组: [1, 1, 2, 2, 1, 1]")
    print("query(0,5,4):", checker.query(0, 5, 4))  # 应该返回 1
    print("query(0,3,3):", checker.query(0, 3, 3))  # 应该返回 -1
    print("query(2,3,2):", checker.query(2, 3, 2))  # 应该返回 2
    print()
    
    print("=== 测试完成 ===")


if __name__ == "__main__":
    main()