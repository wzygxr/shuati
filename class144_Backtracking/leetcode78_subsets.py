#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 78. 子集

题目描述：
给你一个整数数组 nums ，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
解集不能包含重复的子集。你可以按任意顺序返回解集。

示例：
输入：nums = [1,2,3]
输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]

输入：nums = [0]
输出：[[],[0]]

提示：
1 <= nums.length <= 10
-10 <= nums[i] <= 10
nums 中的所有元素互不相同

链接：https://leetcode.cn/problems/subsets/

算法思路：
1. 使用回溯算法生成所有可能的子集
2. 对于每个元素，有两种选择：选择或不选择
3. 通过递归和回溯生成所有满足条件的子集
4. 每一步递归都将当前路径加入结果集

剪枝策略：
1. 可行性剪枝：通过起始索引避免重复选择元素
2. 最优性剪枝：当已选择的元素个数等于数组长度时终止
3. 约束传播：一旦某个元素被选择，后续只能选择后面的元素

时间复杂度：O(n * 2^n)，其中n是数组长度，共有2^n个子集，每个子集需要O(n)时间复制
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

class LeetCode78_Subsets:
    def subsets(self, nums: list[int]) -> list[list[int]]:
        """
        生成数组的所有子集
        
        Args:
            nums: 输入数组
            
        Returns:
            所有可能的子集
        """
        result = []
        
        # 边界条件检查
        if nums is None:
            return result
        
        path = []
        self._backtrack(nums, 0, path, result)
        return result
    
    def _backtrack(self, nums: list[int], start: int, path: list[int], result: list[list[int]]) -> None:
        """
        回溯函数生成子集
        
        Args:
            nums: 输入数组
            start: 当前起始索引
            path: 当前路径
            result: 结果列表
        """
        # 每一步都添加到结果中（空集也在其中）
        result.append(path[:])
        
        # 从start开始遍历，避免重复
        for i in range(start, len(nums)):
            # 选择当前元素
            path.append(nums[i])
            
            # 递归处理下一个元素
            self._backtrack(nums, i + 1, path, result)
            
            # 回溯：撤销选择
            path.pop()
    
    def subsets2(self, nums: list[int]) -> list[list[int]]:
        """
        解法二：使用位运算枚举所有可能的子集
        每个元素有两种状态：选择(1)或不选择(0)，共2^n种组合
        
        Args:
            nums: 输入数组
            
        Returns:
            所有可能的子集
        """
        result = []
        
        # 边界条件检查
        if nums is None:
            return result
        
        n = len(nums)
        
        # 枚举所有可能的子集（0到2^n-1）
        for mask in range(1 << n):
            subset = []
            
            # 根据位掩码构造子集
            for i in range(n):
                # 检查第i位是否为1
                if mask & (1 << i):
                    subset.append(nums[i])
            
            result.append(subset)
        
        return result
    
    def subsets3(self, nums: list[int]) -> list[list[int]]:
        """
        解法三：使用迭代法生成所有子集
        逐个添加元素，每次添加新元素时，将该元素添加到已有的所有子集中
        
        Args:
            nums: 输入数组
            
        Returns:
            所有可能的子集
        """
        result = []
        
        # 边界条件检查
        if nums is None:
            return result
        
        # 初始化空集
        result.append([])
        
        # 逐个添加元素
        for num in nums:
            size = len(result)
            
            # 将当前元素添加到已有的所有子集中
            for i in range(size):
                new_subset = result[i][:]
                new_subset.append(num)
                result.append(new_subset)
        
        return result
    
    def subsets4(self, nums: list[int]) -> list[list[int]]:
        """
        解法四：使用itertools.combinations
        利用Python标准库生成所有可能的组合
        
        Args:
            nums: 输入数组
            
        Returns:
            所有可能的子集
        """
        from itertools import combinations
        result = []
        
        # 生成所有长度的组合
        for i in range(len(nums) + 1):
            for combo in combinations(nums, i):
                result.append(list(combo))
        
        return result


def main():
    """测试示例"""
    solution = LeetCode78_Subsets()
    
    # 测试用例1
    nums1 = [1, 2, 3]
    result1 = solution.subsets(nums1)
    print(f"输入: nums = {nums1}")
    print(f"输出: {result1}")
    
    # 测试用例2
    nums2 = [0]
    result2 = solution.subsets(nums2)
    print(f"\n输入: nums = {nums2}")
    print(f"输出: {result2}")
    
    # 测试解法二
    print("\n=== 解法二测试 ===")
    result3 = solution.subsets2(nums1)
    print(f"输入: nums = {nums1}")
    print(f"输出: {result3}")
    
    # 测试解法三
    print("\n=== 解法三测试 ===")
    result4 = solution.subsets3(nums1)
    print(f"输入: nums = {nums1}")
    print(f"输出: {result4}")
    
    # 测试解法四
    print("\n=== 解法四测试 ===")
    result5 = solution.subsets4(nums1)
    print(f"输入: nums = {nums1}")
    print(f"输出: {result5}")


if __name__ == "__main__":
    main()