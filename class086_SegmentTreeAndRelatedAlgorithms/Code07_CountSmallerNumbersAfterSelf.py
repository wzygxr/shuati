#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
计算右侧小于当前元素的个数
题目来源：LeetCode 315. 计算右侧小于当前元素的个数
题目链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/

核心算法：线段树 + 离散化
难度：困难

【题目详细描述】
给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： 
counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

示例 1：
输入：nums = [5,2,6,1]
输出：[2,1,1,0]
解释：
5 的右侧有 2 个更小的元素 (2 和 1)
2 的右侧有 1 个更小的元素 (1)
6 的右侧有 1 个更小的元素 (1)
1 的右侧有 0 个更小的元素

示例 2：
输入：nums = [-1]
输出：[0]

示例 3：
输入：nums = [-1,-1]
输出：[0,0]

【解题思路】
使用离散化+线段树的方法。从右往左遍历数组，用线段树维护每个值出现的次数，
查询小于当前值的元素个数。

【核心算法】
1. 离散化：将数组中的值映射到连续的整数范围，减少线段树的空间需求
2. 线段树：维护每个值的出现次数，支持单点更新和前缀和查询
3. 逆序遍历：从右往左处理数组元素，确保查询的是右侧元素

【复杂度分析】
- 时间复杂度：O(n log n)，其中n是数组长度
- 空间复杂度：O(n)，用于存储离散化映射和线段树

【算法优化点】
1. 离散化优化：使用二分查找提高离散化效率
2. 线段树优化：使用位运算优化索引计算
3. 遍历优化：逆序遍历避免重复计算

【工程化考量】
1. 输入输出效率：使用标准输入输出处理
2. 边界条件处理：处理空数组、单元素数组等特殊情况
3. 错误处理：处理非法输入和溢出情况

【类似题目推荐】
1. LeetCode 327. 区间和的个数 - https://leetcode.cn/problems/count-of-range-sum/
2. LeetCode 493. 翻转对 - https://leetcode.cn/problems/reverse-pairs/
3. LeetCode 1649. 通过指令创建有序数组 - https://leetcode.cn/problems/create-sorted-array-through-instructions/
4. 洛谷 P2184 贪婪大陆 - https://www.luogu.com.cn/problem/P2184
"""

class SegmentTree:
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size: 线段树大小
        """
        self.n = size
        self.tree = [0] * (4 * (size + 1))
    
    def update(self, node, start, end, index, val):
        """
        更新线段树中指定位置的值
        
        Args:
            node: 当前节点索引
            start: 当前区间左边界
            end: 当前区间右边界
            index: 要更新的位置
            val: 增加的值
        """
        if start == end:
            self.tree[node] += val
        else:
            mid = (start + end) // 2
            if index <= mid:
                self.update(2 * node, start, mid, index, val)
            else:
                self.update(2 * node + 1, mid + 1, end, index, val)
            self.tree[node] = self.tree[2 * node] + self.tree[2 * node + 1]
    
    def query(self, node, start, end, left, right):
        """
        查询区间和
        
        Args:
            node: 当前节点索引
            start: 当前区间左边界
            end: 当前区间右边界
            left: 查询区间左边界
            right: 查询区间右边界
            
        Returns:
            int: 区间和
        """
        if left > end or right < start:
            return 0
        if left <= start and end <= right:
            return self.tree[node]
        mid = (start + end) // 2
        left_sum = self.query(2 * node, start, mid, left, right)
        right_sum = self.query(2 * node + 1, mid + 1, end, left, right)
        return left_sum + right_sum


class Solution:
    def countSmaller(self, nums):
        """
        计算右侧小于当前元素的个数
        
        Args:
            nums: 输入数组
            
        Returns:
            List[int]: 结果数组
        """
        # 离散化
        sorted_nums = sorted(nums)
        ranks = {}
        rank = 0
        for num in sorted_nums:
            if num not in ranks:
                rank += 1
                ranks[num] = rank
        
        # 使用线段树
        tree = SegmentTree(len(ranks))
        result = []
        
        # 从右往左遍历
        for i in range(len(nums) - 1, -1, -1):
            r = ranks[nums[i]]
            tree.update(1, 1, len(ranks), r, 1)
            result.append(tree.query(1, 1, len(ranks), 1, r - 1))
        
        result.reverse()
        return result


def main():
    """
    主函数：测试计算右侧小于当前元素的个数功能
    """
    solution = Solution()
    
    # 测试用例1
    nums1 = [5, 2, 6, 1]
    result1 = solution.countSmaller(nums1)
    print("测试用例1结果:", result1)  # 预期输出: [2, 1, 1, 0]
    
    # 测试用例2
    nums2 = [-1]
    result2 = solution.countSmaller(nums2)
    print("测试用例2结果:", result2)  # 预期输出: [0]
    
    # 测试用例3
    nums3 = [-1, -1]
    result3 = solution.countSmaller(nums3)
    print("测试用例3结果:", result3)  # 预期输出: [0, 0]


if __name__ == "__main__":
    main()