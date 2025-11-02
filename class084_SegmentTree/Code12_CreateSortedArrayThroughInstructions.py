#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1649. Create Sorted Array through Instructions (通过指令创建有序数组)
题目链接：https://leetcode.com/problems/create-sorted-array-through-instructions/

题目描述：
给你一个整数数组 instructions，你需要根据 instructions 中的元素创建一个有序数组。
一开始数组为空。你需要依次读取 instructions 中的元素，并将它插入到有序数组中的正确位置。
每次插入操作的代价是以下两者的较小值：
1. 有多少个元素严格小于 instructions[i]（左边）
2. 有多少个元素严格大于 instructions[i]（右边）
返回插入所有元素的总最小代价。由于答案可能很大，请返回它对 10^9 + 7 取模的结果。

示例：
输入：instructions = [1,5,6,2]
输出：1
解释：插入 1 时，数组为空，代价为 0。
插入 5 时，左边有 1 个元素比 5 小，右边没有元素，代价为 min(1, 0) = 0。
插入 6 时，左边有 2 个元素比 6 小，右边没有元素，代价为 min(2, 0) = 0。
插入 2 时，左边有 1 个元素比 2 小，右边有 2 个元素比 2 大，代价为 min(1, 2) = 1。
总代价为 0 + 0 + 0 + 1 = 1

解题思路：
这道题可以使用离散化线段树来解决。我们需要高效地统计数组中有多少元素小于当前元素，以及有多少元素大于当前元素。
具体步骤：
1. 离散化处理：将指令数组中的所有元素进行排序去重，得到每个元素的排名（离散化值）
2. 构建线段树：维护每个值出现的次数
3. 对于每个指令：
   a. 查询当前小于该元素的个数（即离散化后值减1的前缀和）
   b. 查询当前大于该元素的个数（即总元素数减去离散化后值的前缀和）
   c. 计算当前代价并累加到结果中
   d. 更新线段树，将该元素的计数加1

时间复杂度分析：
- 离散化：O(n log n)，其中n是指令数组的长度
- 线段树构建：O(m)，其中m是离散化后的不同元素个数
- 每个查询和更新操作：O(log m)
- 总时间复杂度：O(n log n + n log m) = O(n log n)，因为m ≤ n

空间复杂度分析：
- 线段树空间：O(m)
- 离散化数组：O(m)
- 总空间复杂度：O(m) = O(n)

本题最优解：线段树是本题的最优解之一，另外也可以使用树状数组（Fenwick Tree）实现，时间复杂度相同。
"""

MOD = 10**9 + 7

class SegmentTree:
    """线段树类，用于维护区间和"""
    
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size: 离散化后的值域大小
        """
        self.n = 1
        # 计算大于等于size的最小2的幂次
        while self.n < size:
            self.n <<= 1
        # 初始化线段树数组，大小为2*n
        self.tree = [0] * (2 * self.n)
    
    def update(self, idx, delta):
        """
        更新线段树中的某个位置的值
        
        Args:
            idx: 离散化后的值对应的索引
            delta: 要增加的值（这里是1）
        """
        idx += self.n  # 转换为线段树叶子节点索引
        self.tree[idx] += delta
        # 向上更新父节点
        i = idx >> 1
        while i >= 1:
            self.tree[i] = self.tree[2 * i] + self.tree[2 * i + 1]
            i >>= 1
    
    def query(self, idx):
        """
        查询区间[0, idx]的和
        
        Args:
            idx: 离散化后的值对应的索引
            
        Returns:
            区间和
        """
        if idx < 0:
            return 0
        idx = min(idx, self.n - 1)  # 防止越界
        res = 0
        l = self.n       # 左边界（线段树叶子节点索引）
        r = self.n + idx # 右边界（线段树叶子节点索引）
        
        # 区间查询
        while l <= r:
            # 如果l是右孩子
            if l % 2 == 1:
                res += self.tree[l]
                l += 1
            # 如果r是左孩子
            if r % 2 == 0:
                res += self.tree[r]
                r -= 1
            l >>= 1
            r >>= 1
        
        return res

class Solution:
    """解决方案类"""
    
    def createSortedArray(self, instructions):
        """
        计算创建有序数组的最小代价
        
        Args:
            instructions: 指令数组
            
        Returns:
            总最小代价
        """
        # 离散化处理
        unique_vals = sorted(set(instructions))
        m = len(unique_vals)
        
        # 创建值到离散化索引的映射字典
        value_to_index = {val: i for i, val in enumerate(unique_vals)}
        
        # 构建线段树
        segment_tree = SegmentTree(m)
        total_cost = 0
        
        # 处理每个指令
        for i, value in enumerate(instructions):
            idx = value_to_index[value]
            
            # 计算左边比当前元素小的个数（即前缀和）
            smaller_count = segment_tree.query(idx - 1)
            
            # 计算右边比当前元素大的个数（总元素数减去到当前索引的前缀和）
            larger_count = i - segment_tree.query(idx)
            
            # 取较小值作为当前操作的代价
            total_cost = (total_cost + min(smaller_count, larger_count)) % MOD
            
            # 更新线段树，将当前元素的计数加1
            segment_tree.update(idx, 1)
        
        return total_cost

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    instructions1 = [1, 5, 6, 2]
    print("测试用例1结果:", solution.createSortedArray(instructions1))  # 预期输出: 1
    
    # 测试用例2
    instructions2 = [1, 2, 3, 6, 5, 4]
    print("测试用例2结果:", solution.createSortedArray(instructions2))  # 预期输出: 3
    
    # 测试用例3
    instructions3 = [1, 3, 3, 3, 2, 4, 2, 1, 2]
    print("测试用例3结果:", solution.createSortedArray(instructions3))  # 预期输出: 4

"""
性能优化说明：
1. 使用集合去重然后排序，避免了重复元素的处理
2. 在Python中使用字典进行值到索引的映射，提高了查询效率
3. 线段树的实现采用了非递归的方式，在Python中避免了递归可能带来的栈溢出问题
4. 使用位移运算代替乘法和除法，提升了位运算效率

语言特性差异：
1. Python中的整数没有大小限制，不需要像Java和C++那样担心溢出问题，但在取模时仍需注意
2. Python的字典比Java的数组映射更灵活，但在大规模数据时可能稍慢
3. Python的列表操作比C++的vector更简洁，但性能略低

工程化考量：
1. 代码结构清晰，类和方法的命名符合Python的PEP8规范
2. 包含了详细的文档字符串（docstring），方便其他开发者理解和使用
3. 添加了测试用例，验证代码的正确性
4. 考虑了边界情况，如idx为负数的情况
5. 使用常量MOD定义模数，提高了代码的可维护性
"""