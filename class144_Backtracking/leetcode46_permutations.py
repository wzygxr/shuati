#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 46. 全排列

题目描述：
给定一个不含重复数字的数组 nums ，返回其所有可能的全排列。你可以按任意顺序返回答案。

示例：
输入：nums = [1,2,3]
输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]

输入：nums = [0,1]
输出：[[0,1],[1,0]]

输入：nums = [1]
输出：[[1]]

提示：
1 <= nums.length <= 6
-10 <= nums[i] <= 10
nums 中的所有整数互不相同

链接：https://leetcode.cn/problems/permutations/

算法思路：
1. 使用回溯算法生成所有可能的排列
2. 对于每个位置，尝试放置每个未使用的数字
3. 通过递归和回溯生成所有满足条件的排列
4. 使用布尔数组标记数字是否已被使用

剪枝策略：
1. 可行性剪枝：使用布尔数组避免重复使用数字
2. 最优性剪枝：当已选择的数字个数等于数组长度时终止
3. 约束传播：一旦某个数字被使用，立即标记为已使用

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

class LeetCode46_Permutations:
    def permute(self, nums: list[int]) -> list[list[int]]:
        """
        生成数组的所有全排列
        
        Args:
            nums: 输入数组
            
        Returns:
            所有可能的全排列
        """
        result = []
        
        # 边界条件检查
        if not nums:
            return result
        
        path = []
        used = [False] * len(nums)
        self._backtrack(nums, path, used, result)
        return result
    
    def _backtrack(self, nums: list[int], path: list[int], used: list[bool], result: list[list[int]]) -> None:
        """
        回溯函数生成排列
        
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
            
            # 选择当前数字
            path.append(nums[i])
            used[i] = True
            
            # 递归处理下一个位置
            self._backtrack(nums, path, used, result)
            
            # 回溯：撤销选择
            path.pop()
            used[i] = False
    
    def permute2(self, nums: list[int]) -> list[list[int]]:
        """
        解法二：交换元素法生成排列
        通过交换数组元素生成所有排列，避免使用额外的标记数组
        
        Args:
            nums: 输入数组
            
        Returns:
            所有可能的全排列
        """
        result = []
        
        # 边界条件检查
        if not nums:
            return result
        
        self._backtrack2(nums, 0, result)
        return result
    
    def _backtrack2(self, nums: list[int], start: int, result: list[list[int]]) -> None:
        """
        通过交换元素生成排列的回溯函数
        
        Args:
            nums: 数字列表
            start: 当前处理的位置
            result: 结果列表
        """
        # 终止条件：已处理完所有位置
        if start == len(nums):
            result.append(nums[:])  # 添加当前排列的副本
            return
        
        # 尝试将每个后续元素交换到当前位置
        for i in range(start, len(nums)):
            # 交换元素
            nums[start], nums[i] = nums[i], nums[start]
            
            # 递归处理下一个位置
            self._backtrack2(nums, start + 1, result)
            
            # 回溯：恢复交换前的状态
            nums[start], nums[i] = nums[i], nums[start]
    
    def permute3(self, nums: list[int]) -> list[list[int]]:
        """
        解法三：使用itertools.permutations
        利用Python标准库生成排列
        
        Args:
            nums: 输入数组
            
        Returns:
            所有可能的全排列
        """
        import itertools
        return [list(p) for p in itertools.permutations(nums)]


def main():
    """测试示例"""
    solution = LeetCode46_Permutations()
    
    # 测试用例1
    nums1 = [1, 2, 3]
    result1 = solution.permute(nums1)
    print(f"输入: nums = {nums1}")
    print(f"输出: {result1}")
    
    # 测试用例2
    nums2 = [0, 1]
    result2 = solution.permute(nums2)
    print(f"\n输入: nums = {nums2}")
    print(f"输出: {result2}")
    
    # 测试用例3
    nums3 = [1]
    result3 = solution.permute(nums3)
    print(f"\n输入: nums = {nums3}")
    print(f"输出: {result3}")
    
    # 测试解法二
    print("\n=== 解法二测试 ===")
    result4 = solution.permute2(nums1)
    print(f"输入: nums = {nums1}")
    print(f"输出: {result4}")
    
    # 测试解法三
    print("\n=== 解法三测试 ===")
    result5 = solution.permute3(nums1)
    print(f"输入: nums = {nums1}")
    print(f"输出: {result5}")


if __name__ == "__main__":
    main()