#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
区域和检索 - 数组可修改
题目来源：LeetCode 307. 区域和检索 - 数组可修改
题目链接：https://leetcode.cn/problems/range-sum-query-mutable/

核心算法：线段树
难度：中等

【题目详细描述】
给你一个数组 nums ，请你完成两类查询：
1. 一类查询要求更新数组 nums 下标对应的值
2. 一类查询要求返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和，其中 left <= right
实现 NumArray 类：
- NumArray(int[] nums) 用整数数组 nums 初始化对象
- void update(int index, int val) 将 nums[index] 的值更新为 val
- int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和

【解题思路】
使用线段树来维护区间和，支持单点更新和区间查询操作。

【核心算法】
1. 线段树构建：构建支持区间求和的线段树
2. 单点更新：支持更新数组中某个位置的值
3. 区间查询：支持查询任意区间的元素和

【复杂度分析】
- 时间复杂度：
  - 构建线段树：O(n)
  - 单点更新：O(log n)
  - 区间查询：O(log n)
- 空间复杂度：O(n)，线段树所需空间

【算法优化点】
1. 数组索引优化：使用位运算优化索引计算
2. 递归优化：尾递归优化或迭代实现
3. 内存优化：预分配固定大小数组

【工程化考量】
1. 输入输出效率：使用标准输入输出处理
2. 边界条件处理：处理空数组、单元素数组等特殊情况
3. 错误处理：处理非法索引访问

【类似题目推荐】
1. LeetCode 308. 二维区域和检索 - 可变 - https://leetcode.cn/problems/range-sum-query-2d-mutable/
2. LeetCode 315. 计算右侧小于当前元素的个数 - https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
3. 洛谷 P3372 【模板】线段树 1 - https://www.luogu.com.cn/problem/P3372
4. HDU 1166 敌兵布阵 - http://acm.hdu.edu.cn/showproblem.php?pid=1166
"""

class SegmentTree:
    def __init__(self, nums):
        """
        初始化线段树
        
        Args:
            nums: 初始数组
        """
        self.n = len(nums)
        self.nums = nums
        # 线段树数组大小通常为4n
        self.tree = [0] * (4 * self.n)
        # 构建线段树
        self.build(0, 0, self.n - 1)
    
    def build(self, node, start, end):
        """
        构建线段树
        
        Args:
            node: 当前节点索引
            start: 当前区间左边界
            end: 当前区间右边界
        """
        if start == end:
            # 叶子节点，存储数组元素值
            self.tree[node] = self.nums[start]
        else:
            # 非叶子节点，递归构建左右子树
            mid = (start + end) // 2
            self.build(2 * node + 1, start, mid)      # 左子树
            self.build(2 * node + 2, mid + 1, end)    # 右子树
            # 合并左右子树的结果
            self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]
    
    def update_tree(self, node, start, end, index, diff):
        """
        更新线段树中的节点值
        
        Args:
            node: 当前节点索引
            start: 当前区间左边界
            end: 当前区间右边界
            index: 要更新的数组索引
            diff: 值的变化量
        """
        if start == end:
            # 到达叶子节点，直接更新
            self.tree[node] += diff
        else:
            mid = (start + end) // 2
            if index <= mid:
                # 目标索引在左子树中
                self.update_tree(2 * node + 1, start, mid, index, diff)
            else:
                # 目标索引在右子树中
                self.update_tree(2 * node + 2, mid + 1, end, index, diff)
            # 更新当前节点的值
            self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]
    
    def query(self, node, start, end, left, right):
        """
        查询线段树中指定区间的元素和
        
        Args:
            node: 当前节点索引
            start: 当前区间左边界
            end: 当前区间右边界
            left: 查询区间左边界
            right: 查询区间右边界
            
        Returns:
            int: 区间元素和
        """
        if right < start or end < left:
            # 查询区间与当前区间无交集
            return 0
        if left <= start and end <= right:
            # 当前区间完全包含在查询区间内
            return self.tree[node]
        # 查询区间与当前区间有部分交集，递归查询左右子树
        mid = (start + end) // 2
        left_sum = self.query(2 * node + 1, start, mid, left, right)
        right_sum = self.query(2 * node + 2, mid + 1, end, left, right)
        return left_sum + right_sum


class NumArray:
    def __init__(self, nums):
        """
        构造函数：用整数数组 nums 初始化对象
        
        Args:
            nums: 初始数组
        """
        self.nums = nums
        self.segment_tree = SegmentTree(nums)
    
    def update(self, index, val):
        """
        更新数组中指定索引的值
        
        Args:
            index: 要更新的数组索引
            val: 新的值
        """
        # 计算值的变化量
        diff = val - self.nums[index]
        self.nums[index] = val
        # 更新线段树中相关的节点
        self.segment_tree.update_tree(0, 0, len(self.nums) - 1, index, diff)
    
    def sumRange(self, left, right):
        """
        查询指定区间的元素和
        
        Args:
            left: 查询区间左边界
            right: 查询区间右边界
            
        Returns:
            int: 区间元素和
        """
        return self.segment_tree.query(0, 0, len(self.nums) - 1, left, right)


def main():
    """
    主函数：测试区域和检索功能
    """
    # 测试用例
    nums = [1, 3, 5]
    numArray = NumArray(nums)
    
    # 测试区间求和
    print("sumRange(0, 2):", numArray.sumRange(0, 2))  # 预期输出: 9
    
    # 测试更新操作
    numArray.update(1, 2)
    
    # 测试更新后的区间求和
    print("sumRange(0, 2):", numArray.sumRange(0, 2))  # 预期输出: 8


if __name__ == "__main__":
    main()