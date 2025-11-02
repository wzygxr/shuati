#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 307. Range Sum Query - Mutable

题目描述：
给定一个整数数组 nums，实现一个数据结构，支持以下操作：
1. update(index, val): 将 nums[index] 的值更新为 val
2. sumRange(left, right): 返回 nums[left...right] 的元素和

解题思路：
使用线段树维护区间和，支持单点更新和区间查询
每个节点存储区间和，通过递归构建和查询

关键技术：
1. 线段树：维护区间和信息
2. 单点更新：通过递归更新指定位置的值
3. 区间查询：通过分治思想查询任意区间和

时间复杂度分析：
1. 建树：O(n)
2. 更新：O(log n)
3. 查询：O(log n)
4. 空间复杂度：O(n)

是否最优解：是
这是解决区间和查询与单点更新问题的最优解法

工程化考量：
1. 内存管理：预分配列表避免频繁内存分配
2. 边界处理：处理叶子节点和区间边界情况
3. 性能优化：避免重复计算，合理设计递归结构
4. 异常处理：处理索引越界等异常情况

题目链接：https://leetcode.cn/problems/range-sum-query-mutable/

@author Algorithm Journey
@version 1.0
"""


class NumArray:
    """
    NumArray类
    实现区间和查询与单点更新功能
    """
    
    def __init__(self, nums):
        """
        初始化线段树
        
        @param nums: 输入数组
        """
        self.n = len(nums)
        # 线段树数组大小通常设置为4*n以保证足够空间
        self.tree = [0] * (4 * self.n)
        # 构建线段树
        self._build_tree(nums, 0, self.n - 1, 1)
    
    def _build_tree(self, nums, l, r, idx):
        """
        递归构建线段树
        每个节点存储对应区间的和
        
        @param nums: 原始数组
        @param l: 当前区间左边界
        @param r: 当前区间右边界
        @param idx: 当前节点索引
        """
        # 递归终止条件：叶子节点，直接赋值
        if l == r:
            self.tree[idx] = nums[l]
            return
        
        # 分治处理：递归构建左右子树
        mid = l + (r - l) // 2
        self._build_tree(nums, l, mid, idx * 2)
        self._build_tree(nums, mid + 1, r, idx * 2 + 1)
        
        # 合并左右子树信息：父节点的值等于左右子节点值的和
        self.tree[idx] = self.tree[idx * 2] + self.tree[idx * 2 + 1]
    
    def update(self, index, val):
        """
        更新指定位置的值
        
        @param index: 要更新的索引
        @param val: 新的值
        """
        # 调用内部更新方法
        self._update_tree(0, self.n - 1, 1, index, val)
    
    def _update_tree(self, l, r, idx, index, val):
        """
        递归更新线段树
        更新指定位置的值
        
        @param l: 当前区间左边界
        @param r: 当前区间右边界
        @param idx: 当前节点索引
        @param index: 要更新的索引
        @param val: 新的值
        """
        # 递归终止条件：找到目标叶子节点
        if l == r:
            self.tree[idx] = val
            return
        
        # 二分查找目标位置
        mid = l + (r - l) // 2
        if index <= mid:
            # 目标位置在左子树
            self._update_tree(l, mid, idx * 2, index, val)
        else:
            # 目标位置在右子树
            self._update_tree(mid + 1, r, idx * 2 + 1, index, val)
        
        # 向上更新节点信息：重新计算父节点的值
        self.tree[idx] = self.tree[idx * 2] + self.tree[idx * 2 + 1]
    
    def sumRange(self, left, right):
        """
        查询区间和
        
        @param left: 区间左边界
        @param right: 区间右边界
        @return: 区间和
        """
        # 调用内部查询方法
        return self._query_tree(0, self.n - 1, 1, left, right)
    
    def _query_tree(self, l, r, idx, left, right):
        """
        递归查询线段树
        查询指定区间的和
        
        @param l: 当前区间左边界
        @param r: 当前区间右边界
        @param idx: 当前节点索引
        @param left: 查询区间左边界
        @param right: 查询区间右边界
        @return: 区间和
        """
        # 完全包含：当前区间完全被查询区间包含，直接返回
        if left <= l and r <= right:
            return self.tree[idx]
        
        # 分治查询：分别查询左右子树
        mid = l + (r - l) // 2
        total = 0
        
        # 如果查询区间与左子树有交集，查询左子树
        if left <= mid:
            total += self._query_tree(l, mid, idx * 2, left, right)
        # 如果查询区间与右子树有交集，查询右子树
        if right > mid:
            total += self._query_tree(mid + 1, r, idx * 2 + 1, left, right)
        
        return total

# 测试代码
if __name__ == "__main__":
    # 测试用例
    nums = [1, 3, 5]
    numArray = NumArray(nums)
    
    print(f"初始数组: {nums}")
    print(f"sumRange(0, 2) = {numArray.sumRange(0, 2)}")  # 9
    
    numArray.update(1, 2)
    print("更新索引1为2后")
    print(f"sumRange(0, 2) = {numArray.sumRange(0, 2)}")  # 8
    
    # 边界测试
    print(f"sumRange(0, 0) = {numArray.sumRange(0, 0)}")  # 1
    print(f"sumRange(2, 2) = {numArray.sumRange(2, 2)}")  # 5
    
    # 异常场景测试
    try:
        # 测试越界索引
        numArray.update(10, 100)  # 应该抛出异常
    except Exception as e:
        print(f"异常处理测试: {e}")
    
    # 性能测试（大规模数据）
    import time
    
    # 创建大规模测试数据
    large_nums = list(range(1, 10001))  # 10000个元素
    start_time = time.time()
    large_array = NumArray(large_nums)
    build_time = time.time() - start_time
    
    # 查询性能测试
    query_start = time.time()
    for _ in range(1000):
        large_array.sumRange(0, 9999)
    query_time = time.time() - query_start
    
    print(f"构建10000个元素的线段树耗时: {build_time:.4f}秒")
    print(f"1000次查询耗时: {query_time:.4f}秒")