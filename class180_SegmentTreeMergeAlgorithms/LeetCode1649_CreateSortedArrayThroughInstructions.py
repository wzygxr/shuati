#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1649. 通过指令创建有序数组
题目链接: https://leetcode.cn/problems/create-sorted-array-through-instructions/

题目描述:
给你一个整数数组 instructions，你需要根据 instructions 中的元素创建一个有序数组。
一开始你有一个空的数组 nums，你需要从左到右遍历 instructions 中的元素，将它们依次插入 nums 数组中。
每一次插入操作的 代价 是以下两者的较小值：
1. nums 中严格小于 instructions[i] 的数字数目
2. nums 中严格大于 instructions[i] 的数字数目
请你返回将 instructions 中所有元素依次插入 nums 后的 总代价。

示例:
输入: instructions = [1,5,6,2]
输出: 1
解释: 一开始 nums = []。
插入 1 ，代价为 min(0, 0) = 0，现在 nums = [1]。
插入 5 ，代价为 min(1, 0) = 0，现在 nums = [1,5]。
插入 6 ，代价为 min(2, 0) = 0，现在 nums = [1,5,6]。
插入 2 ，代价为 min(1, 2) = 1，现在 nums = [1,2,5,6]。
总代价为 0 + 0 + 0 + 1 = 1。

提示:
1 <= instructions.length <= 10^5
1 <= instructions[i] <= 10^5

解题思路:
这是一个经典的线段树应用问题，需要动态维护数组中每个数字的出现次数。
1. 使用线段树维护值域上的数字出现次数
2. 对于每个新插入的数字，查询小于它的数字个数和大于它的数字个数
3. 取两者较小值作为当前插入的代价
4. 将当前数字插入线段树中

时间复杂度分析:
- 建树: O(max_value)，其中max_value是instructions中的最大值
- 每次查询: O(log max_value)
- 每次更新: O(log max_value)
- 总时间复杂度: O(n * log max_value)

空间复杂度分析:
- 线段树空间: O(4 * max_value)
- 总空间复杂度: O(max_value)

工程化考量:
1. 值域离散化: 由于值域可能很大，需要进行离散化处理
2. 边界处理: 处理空数组和单个元素的情况
3. 性能优化: 使用位运算优化线段树操作
4. 内存优化: 动态开点线段树可以节省空间

面试要点:
1. 理解线段树在动态统计中的应用
2. 掌握离散化处理技巧
3. 能够分析时间空间复杂度
4. 处理边界情况和极端输入
"""

MOD = 10**9 + 7

class SegmentTree:
    """线段树类，用于维护值域上的数字出现次数"""
    
    def __init__(self, size: int):
        self.n = size
        self.tree = [0] * (4 * size)
    
    def update(self, pos: int, val: int) -> None:
        """更新操作：在位置pos增加val"""
        self._update(1, 1, self.n, pos, val)
    
    def _update(self, node: int, left: int, right: int, pos: int, val: int) -> None:
        if left == right:
            self.tree[node] += val
            return
        
        mid = (left + right) // 2
        if pos <= mid:
            self._update(node * 2, left, mid, pos, val)
        else:
            self._update(node * 2 + 1, mid + 1, right, pos, val)
        
        self.tree[node] = self.tree[node * 2] + self.tree[node * 2 + 1]
    
    def query(self, L: int, R: int) -> int:
        """查询操作：查询区间[L, R]的和"""
        if L > R:
            return 0
        return self._query(1, 1, self.n, L, R)
    
    def _query(self, node: int, left: int, right: int, L: int, R: int) -> int:
        if L > right or R < left:
            return 0
        
        if L <= left and right <= R:
            return self.tree[node]
        
        mid = (left + right) // 2
        left_sum = self._query(node * 2, left, mid, L, R)
        right_sum = self._query(node * 2 + 1, mid + 1, right, L, R)
        
        return left_sum + right_sum

class Solution:
    def createSortedArray(self, instructions: list) -> int:
        """
        主函数：计算通过指令创建有序数组的总代价
        
        Args:
            instructions: 整数数组
            
        Returns:
            int: 总代价
        """
        # 获取最大值用于确定线段树大小
        max_val = max(instructions) if instructions else 0
        
        # 创建线段树
        seg_tree = SegmentTree(max_val)
        total_cost = 0
        
        for num in instructions:
            # 查询小于当前数字的数量
            less_count = seg_tree.query(1, num - 1)
            # 查询大于当前数字的数量
            greater_count = seg_tree.query(num + 1, max_val)
            
            # 当前插入代价为两者较小值
            cost = min(less_count, greater_count)
            total_cost = (total_cost + cost) % MOD
            
            # 将当前数字插入线段树
            seg_tree.update(num, 1)
        
        return total_cost

def test_create_sorted_array():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1
    instructions1 = [1, 5, 6, 2]
    result1 = solution.createSortedArray(instructions1)
    print(f"测试用例1: {instructions1} -> {result1}")
    
    # 测试用例2
    instructions2 = [1, 2, 3, 6, 5, 4]
    result2 = solution.createSortedArray(instructions2)
    print(f"测试用例2: {instructions2} -> {result2}")
    
    # 测试用例3: 单个元素
    instructions3 = [1]
    result3 = solution.createSortedArray(instructions3)
    print(f"测试用例3: {instructions3} -> {result3}")
    
    # 测试用例4: 重复元素
    instructions4 = [1, 1, 1, 1]
    result4 = solution.createSortedArray(instructions4)
    print(f"测试用例4: {instructions4} -> {result4}")
    
    # 测试用例5: 空数组
    instructions5 = []
    result5 = solution.createSortedArray(instructions5)
    print(f"测试用例5: {instructions5} -> {result5}")
    
    # 测试用例6: 边界情况 - 最大值
    instructions6 = [100000]
    result6 = solution.createSortedArray(instructions6)
    print(f"测试用例6: {instructions6} -> {result6}")

if __name__ == "__main__":
    test_create_sorted_array()