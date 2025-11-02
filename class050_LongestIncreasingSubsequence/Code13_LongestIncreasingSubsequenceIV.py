#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
最长递增子序列 IV - LeetCode 2407
题目来源：https://leetcode.cn/problems/longest-increasing-subsequence-iv/
难度：困难
题目描述：给你一个整数数组 nums 和一个整数 k 。找到最长子序列的长度，满足子序列中的每个元素 严格递增 ，且子序列中相邻元素的差的绝对值 至少 为 k 。
子序列 是指从原数组中删除一些元素（可能一个也不删除）后，剩余元素保持原来顺序而形成的新数组。

核心思路：
1. 这道题是LIS问题的一个变体，增加了相邻元素差的绝对值至少为k的约束
2. 使用动态规划 + 线段树优化来解决这个问题
3. 对于每个元素nums[i]，我们需要找到所有满足nums[j] < nums[i]且nums[i] - nums[j] >= k的j，然后dp[i] = max(dp[j]) + 1
4. 使用线段树来高效查询区间内的最大值

复杂度分析：
时间复杂度：O(n log n)，其中n是数组长度。离散化需要O(n log n)，构建线段树需要O(n)，每次查询和更新需要O(log n)，总共有n次查询和更新
空间复杂度：O(n)，用于存储离散化后的值、dp数组和线段树
"""

from typing import List
import time
import random


class SegmentTree:
    """
    线段树类，用于高效查询区间最大值和更新操作
    """
    def __init__(self, size):
        self.n = 1
        # 确保大小为2的幂次，简化实现
        while self.n < size:
            self.n <<= 1
        # 初始化线段树数组，大小为2*n
        self.tree = [0] * (2 * self.n)
    
    def update(self, pos, value):
        """
        更新线段树中指定位置的值（取最大值）
        
        参数:
            pos: 要更新的位置（从0开始）
            value: 要更新的值
        """
        pos += self.n  # 转换为叶子节点索引
        # 更新当前节点的值为最大值
        if self.tree[pos] < value:
            self.tree[pos] = value
            # 向上更新父节点
            while pos > 1:
                pos >>= 1  # 移动到父节点
                # 父节点的值为左右子节点的最大值
                new_val = max(self.tree[2 * pos], self.tree[2 * pos + 1])
                if self.tree[pos] == new_val:
                    break  # 如果父节点的值没有变化，停止更新
                self.tree[pos] = new_val
    
    def query_max(self, l, r):
        """
        查询区间[l, r)内的最大值
        
        参数:
            l: 区间左边界（包含）
            r: 区间右边界（不包含）
        返回:
            区间内的最大值
        """
        res = 0
        l += self.n  # 转换为叶子节点索引
        r += self.n
        
        while l < r:
            # 如果左边界是右孩子
            if l % 2 == 1:
                res = max(res, self.tree[l])
                l += 1
            # 如果右边界是左孩子
            if r % 2 == 1:
                r -= 1
                res = max(res, self.tree[r])
            # 移动到父节点
            l >>= 1
            r >>= 1
        
        return res


def lengthOfLIS(nums: List[int], k: int) -> int:
    """
    最优解法：动态规划 + 线段树优化
    
    参数:
        nums: 输入数组
        k: 相邻元素差的最小绝对值
    返回:
        满足条件的最长递增子序列的长度
    """
    if not nums:
        return 0
    
    # 离散化处理
    sorted_nums = sorted(set(nums))  # 使用set去重，避免重复的值
    # 创建值到索引的映射，从1开始
    value_to_index = {num: idx + 1 for idx, num in enumerate(sorted_nums)}
    n = len(sorted_nums)
    
    # 初始化线段树
    segment_tree = SegmentTree(n + 1)  # 索引从1开始，所以大小+1
    
    max_length = 0
    for num in nums:
        # 计算目标值
        target = num - k
        # 二分查找找到最大的不大于target的值
        # 使用bisect_right找到插入位置，然后减1
        left, right = 0, len(sorted_nums) - 1
        best = -1  # 保存找到的最大索引
        while left <= right:
            mid = (left + right) // 2
            if sorted_nums[mid] <= target:
                best = mid
                left = mid + 1
            else:
                right = mid - 1
        
        # 计算当前长度
        current_length = 1  # 至少包含自己
        if best != -1:
            # 查询[1, best_idx]区间内的最大值
            best_idx = value_to_index[sorted_nums[best]]
            current_length = segment_tree.query_max(1, best_idx + 1) + 1
        
        # 更新线段树
        num_idx = value_to_index[num]
        segment_tree.update(num_idx, current_length)
        
        # 更新全局最大值
        max_length = max(max_length, current_length)
    
    return max_length


def lengthOfLISAlternative(nums: List[int], k: int) -> int:
    """
    另一种实现方式：使用更简单的线段树实现
    
    参数:
        nums: 输入数组
        k: 相邻元素差的最小绝对值
    返回:
        满足条件的最长递增子序列的长度
    """
    if not nums:
        return 0
    
    # 离散化处理
    sorted_nums = sorted(set(nums))
    value_to_index = {num: idx + 1 for idx, num in enumerate(sorted_nums)}
    n = len(sorted_nums)
    
    # 简单的线段树实现（基于数组）
    # 使用4*n的大小来存储线段树
    size = 1
    while size < n:
        size <<= 1
    tree = [0] * (2 * size)
    
    def update(pos, value):
        """更新线段树中的值"""
        pos += size  # 转换为叶子节点位置
        if pos >= len(tree):
            return
        if tree[pos] < value:
            tree[pos] = value
            pos >>= 1  # 移动到父节点
            while pos >= 1:
                new_val = max(tree[2 * pos], tree[2 * pos + 1])
                if tree[pos] == new_val:
                    break
                tree[pos] = new_val
                pos >>= 1
    
    def query_max(l, r):
        """查询区间[l, r)的最大值"""
        res = 0
        l += size
        r += size
        
        while l < r:
            if l % 2 == 1:
                res = max(res, tree[l])
                l += 1
            if r % 2 == 1:
                r -= 1
                res = max(res, tree[r])
            l >>= 1
            r >>= 1
        
        return res
    
    max_length = 0
    for num in nums:
        target = num - k
        # 二分查找
        left, right = 0, len(sorted_nums) - 1
        best = -1
        while left <= right:
            mid = (left + right) // 2
            if sorted_nums[mid] <= target:
                best = mid
                left = mid + 1
            else:
                right = mid - 1
        
        current_length = 1
        if best != -1:
            best_idx = value_to_index[sorted_nums[best]]
            current_length = query_max(1, best_idx + 1) + 1
        
        num_idx = value_to_index[num]
        update(num_idx, current_length)
        max_length = max(max_length, current_length)
    
    return max_length


def lengthOfLISExplained(nums: List[int], k: int) -> int:
    """
    解释性更强的版本，添加了更多详细注释和中间变量
    
    参数:
        nums: 输入数组
        k: 相邻元素差的最小绝对值
    返回:
        满足条件的最长递增子序列的长度
    """
    # 边界条件检查
    if not nums:
        return 0
    
    # 步骤1: 离散化处理
    # 为什么需要离散化？因为数组中的值可能很大（如负数、很大的正数），直接使用这些值作为线段树的索引不现实
    # 离散化将原始值映射到连续的小整数范围内
    # 首先，我们对数组中的唯一值进行排序
    unique_sorted_nums = sorted(set(nums))  # 使用set去重，然后排序
    
    # 步骤2: 创建值到索引的映射
    # 我们将每个唯一值映射到一个连续的整数（从1开始，便于线段树处理）
    value_index_map = {value: index + 1 for index, value in enumerate(unique_sorted_nums)}
    num_unique_values = len(unique_sorted_nums)
    
    # 步骤3: 初始化线段树
    # 线段树用于高效查询区间内的最大值和更新操作
    # 我们使用一个足够大的线段树来覆盖所有可能的索引
    segment_tree = SegmentTree(num_unique_values + 1)  # +1 因为索引从1开始
    
    # 步骤4: 动态规划 + 线段树优化
    # 对于每个元素num，我们需要找到所有满足nums[j] < num且num - nums[j] >= k的j
    # 然后dp[num] = max(dp[j] for all valid j) + 1
    longest_subsequence = 0
    
    for current_num in nums:
        # 计算目标值：我们需要找的值必须小于等于current_num - k
        target_value = current_num - k
        
        # 使用二分查找找到最大的不大于target_value的值的索引
        left_idx, right_idx = 0, len(unique_sorted_nums) - 1
        best_idx = -1  # 记录找到的最大索引
        
        while left_idx <= right_idx:
            mid_idx = (left_idx + right_idx) // 2
            mid_value = unique_sorted_nums[mid_idx]
            
            if mid_value <= target_value:
                # 找到一个符合条件的候选，继续向右查找更大的符合条件的值
                best_idx = mid_idx
                left_idx = mid_idx + 1
            else:
                # 向左查找更小的值
                right_idx = mid_idx - 1
        
        # 计算以当前元素结尾的最长递增子序列长度
        current_subsequence_length = 1  # 至少包含当前元素
        
        if best_idx != -1:  # 如果找到了符合条件的元素
            # 获取该元素对应的索引
            valid_idx = value_index_map[unique_sorted_nums[best_idx]]
            # 查询[1, valid_idx]区间内的最大值，表示之前找到的最长子序列长度
            max_previous_length = segment_tree.query_max(1, valid_idx + 1)
            # 当前长度 = 之前的最大长度 + 1（加上当前元素）
            current_subsequence_length = max_previous_length + 1
        
        # 更新线段树中当前值的位置
        current_num_idx = value_index_map[current_num]
        segment_tree.update(current_num_idx, current_subsequence_length)
        
        # 更新全局最长子序列长度
        longest_subsequence = max(longest_subsequence, current_subsequence_length)
    
    return longest_subsequence


def runAllSolutionsTest(nums: List[int], k: int):
    """
    运行所有解法的对比测试
    
    参数:
        nums: 输入数组
        k: 相邻元素差的最小绝对值
    """
    print(f"\n对比测试：")
    print(f"数组: {nums}")
    print(f"k: {k}")
    
    # 测试线段树类实现
    start_time = time.time()
    result1 = lengthOfLIS(nums, k)
    end_time = time.time()
    print(f"线段树类实现结果: {result1}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 测试内部线段树实现
    start_time = time.time()
    result2 = lengthOfLISAlternative(nums, k)
    end_time = time.time()
    print(f"内部线段树实现结果: {result2}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 测试解释性版本
    start_time = time.time()
    result3 = lengthOfLISExplained(nums, k)
    end_time = time.time()
    print(f"解释性版本结果: {result3}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    print("-" * 40)


def performanceTest(size: int):
    """
    性能测试函数
    
    参数:
        size: 数组大小
    """
    # 生成随机测试数据
    nums = [random.randint(0, 10000) for _ in range(size)]
    k = random.randint(0, 100)
    
    print(f"\n性能测试：数组大小 = {size}")
    
    # 测试线段树类实现
    start_time = time.time()
    result1 = lengthOfLIS(nums, k)
    end_time = time.time()
    print(f"线段树类实现耗时: {(end_time - start_time) * 1000:.3f} ms, 结果: {result1}")


def testCase():
    """
    测试用例
    """
    # 测试用例1
    nums1 = [4, 2, 1, 4, 3, 4, 5, 8, 15]
    k1 = 3
    print("测试用例1：")
    print(f"数组: {nums1}")
    print(f"k: {k1}")
    print(f"结果: {lengthOfLIS(nums1, k1)}，预期: 5")
    print()
    
    # 测试用例2
    nums2 = [7, 4, 5, 1, 8, 12, 4, 7]
    k2 = 5
    print("测试用例2：")
    print(f"数组: {nums2}")
    print(f"k: {k2}")
    print(f"结果: {lengthOfLIS(nums2, k2)}，预期: 3")
    print()
    
    # 测试用例3：边界情况
    nums3 = [1]
    k3 = 1
    print("测试用例3：")
    print(f"数组: {nums3}")
    print(f"k: {k3}")
    print(f"结果: {lengthOfLIS(nums3, k3)}，预期: 1")
    
    # 运行所有解法的对比测试
    runAllSolutionsTest(nums1, k1)
    runAllSolutionsTest(nums2, k2)
    runAllSolutionsTest(nums3, k3)
    
    # 性能测试
    print("性能测试:")
    print("-" * 40)
    performanceTest(1000)
    performanceTest(5000)
    
    # 特殊测试用例：严格递增序列，且相邻差大于等于k
    print("\n特殊测试用例：严格递增序列，且相邻差大于等于k")
    numsIncreasing = [1, 4, 7, 10, 13]
    kIncreasing = 3
    print(f"数组: {numsIncreasing}")
    print(f"k: {kIncreasing}")
    print(f"结果: {lengthOfLIS(numsIncreasing, kIncreasing)}")
    
    # 特殊测试用例：所有元素相同
    print("\n特殊测试用例：所有元素相同")
    numsSame = [5, 5, 5, 5, 5]
    kSame = 1
    print(f"数组: {numsSame}")
    print(f"k: {kSame}")
    print(f"结果: {lengthOfLIS(numsSame, kSame)}")


if __name__ == "__main__":
    """
    主函数入口
    """
    testCase()