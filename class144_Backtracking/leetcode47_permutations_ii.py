#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 47. 全排列 II

题目描述：
给定一个可包含重复数字的序列 nums ，按任意顺序返回所有不重复的全排列。

示例：
输入：nums = [1,1,2]
输出：
[[1,1,2],
 [1,2,1],
 [2,1,1]]

输入：nums = [1,2,3]
输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]

提示：
1 <= nums.length <= 8
-10 <= nums[i] <= 10

链接：https://leetcode.cn/problems/permutations-ii/

算法思路：
1. 使用回溯算法生成所有可能的排列
2. 先对数组进行排序，使相同元素相邻
3. 对于每个位置，尝试放置每个未使用的数字
4. 通过递归和回溯生成所有满足条件的排列
5. 使用布尔数组标记数字是否已被使用
6. 通过剪枝避免生成重复的排列

剪枝策略：
1. 可行性剪枝：使用布尔数组避免重复使用数字
2. 最优性剪枝：当已选择的数字个数等于数组长度时终止
3. 约束传播：一旦某个数字被使用，立即标记为已使用
4. 重复剪枝：对于相同元素，只允许第一个未使用的元素被选择

时间复杂度：O(n! * n)，其中n是数组长度，共有n!种排列，每种排列需要O(n)时间复制
空间复杂度：O(n)，递归栈深度和存储路径的空间

工程化考量：
1. 边界处理：处理空数组和单元素数组的特殊情况
2. 参数验证：验证输入参数的有效性
3. 性能优化：通过剪枝减少不必要的计算
4. 内存管理：合理使用数据结构减少内存占用
5. 可读性：添加详细注释和变量命名
6. 异常处理：处理可能的异常情况
7. 模块化设计：将核心逻辑封装成独立方法
8. 可维护性：添加详细注释和文档说明
"""

class LeetCode47_PermutationsII:
    def permute_unique(self, nums: list[int]) -> list[list[int]]:
        """
        生成数组的所有不重复全排列
        
        Args:
            nums: 输入数组（可能包含重复元素）
            
        Returns:
            所有不重复的全排列
        """
        result = []
        
        # 边界条件检查
        if not nums:
            return result
        
        # 排序使相同元素相邻，便于剪枝
        nums.sort()
        
        path = []
        used = [False] * len(nums)
        self._backtrack(nums, path, used, result)
        return result
    
    def _backtrack(self, nums: list[int], path: list[int], used: list[bool], result: list[list[int]]) -> None:
        """
        回溯函数生成不重复排列
        
        Args:
            nums: 输入数组
            path: 当前路径
            used: 标记数字是否已被使用的数组
            result: 结果列表
        """
        # 终止条件：已选择所有数字
        if len(path) == len(nums):
            result.append(path[:])  # 添加路径的副本
            return
        
        # 尝试每个未使用的数字
        for i in range(len(nums)):
            # 可行性剪枝：如果数字已被使用，跳过
            if used[i]:
                continue
            
            # 重复剪枝：对于相同元素，只允许第一个未使用的元素被选择
            # 如果当前元素与前一个元素相同，且前一个元素未被使用，则跳过当前元素
            if i > 0 and nums[i] == nums[i - 1] and not used[i - 1]:
                continue
            
            # 选择当前数字
            path.append(nums[i])
            used[i] = True
            
            # 递归处理下一个位置
            self._backtrack(nums, path, used, result)
            
            # 回溯：撤销选择
            path.pop()
            used[i] = False
    
    def permute_unique2(self, nums: list[int]) -> list[list[int]]:
        """
        解法二：使用计数法处理重复元素
        统计每个元素的出现次数，然后基于计数生成排列
        
        Args:
            nums: 输入数组（可能包含重复元素）
            
        Returns:
            所有不重复的全排列
        """
        result = []
        
        # 边界条件检查
        if not nums:
            return result
        
        # 统计每个元素的出现次数
        from collections import Counter
        counter = Counter(nums)
        
        path = []
        self._backtrack2(counter, len(nums), path, result)
        return result
    
    def _backtrack2(self, counter: dict, remaining: int, path: list[int], result: list[list[int]]) -> None:
        """
        基于计数的回溯函数
        
        Args:
            counter: 每个元素的出现次数
            remaining: 剩余需要选择的元素个数
            path: 当前路径
            result: 结果列表
        """
        # 终止条件：已选择所有数字
        if remaining == 0:
            result.append(path[:])  # 添加路径的副本
            return
        
        # 尝试每个可用的数字
        for num in list(counter.keys()):
            # 如果当前数字还有剩余
            if counter[num] > 0:
                # 选择当前数字
                path.append(num)
                counter[num] -= 1
                
                # 递归处理剩余位置
                self._backtrack2(counter, remaining - 1, path, result)
                
                # 回溯：撤销选择
                path.pop()
                counter[num] += 1
    
    def permute_unique3(self, nums: list[int]) -> list[list[int]]:
        """
        解法三：使用set去重
        生成所有排列后使用set去除重复
        
        Args:
            nums: 输入数组（可能包含重复元素）
            
        Returns:
            所有不重复的全排列
        """
        from itertools import permutations
        # 使用set去除重复排列，然后转换为列表格式
        return [list(p) for p in set(permutations(nums))]


def main():
    """测试示例"""
    solution = LeetCode47_PermutationsII()
    
    # 测试用例1
    nums1 = [1, 1, 2]
    result1 = solution.permute_unique(nums1)
    print(f"输入: nums = {nums1}")
    print(f"输出: {result1}")
    
    # 测试用例2
    nums2 = [1, 2, 3]
    result2 = solution.permute_unique(nums2)
    print(f"\n输入: nums = {nums2}")
    print(f"输出: {result2}")
    
    # 测试用例3
    nums3 = [2, 2, 1, 1]
    result3 = solution.permute_unique(nums3)
    print(f"\n输入: nums = {nums3}")
    print(f"输出: {result3}")
    
    # 测试解法二
    print("\n=== 解法二测试 ===")
    result4 = solution.permute_unique2(nums1)
    print(f"输入: nums = {nums1}")
    print(f"输出: {result4}")
    
    # 测试解法三
    print("\n=== 解法三测试 ===")
    result5 = solution.permute_unique3(nums1)
    print(f"输入: nums = {nums1}")
    print(f"输出: {result5}")


if __name__ == "__main__":
    main()