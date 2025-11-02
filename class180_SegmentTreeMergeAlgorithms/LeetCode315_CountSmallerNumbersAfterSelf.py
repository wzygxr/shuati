"""
LeetCode 315. 计算右侧小于当前元素的个数
题目链接: https://leetcode.com/problems/count-of-smaller-numbers-after-self/

题目描述:
给定一个整数数组 nums，按要求返回一个新数组 counts。
数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

示例:
输入: [5,2,6,1]
输出: [2,1,1,0]
解释:
5 的右侧有 2 个更小的元素 (2 和 1)
2 的右侧有 1 个更小的元素 (1)
6 的右侧有 1 个更小的元素 (1)
1 的右侧有 0 个更小的元素

解题思路:
方法一：线段树 + 离散化
1. 对数组进行离散化处理，将原始数值映射到连续的索引
2. 从右向左遍历数组，使用线段树统计每个数值出现的次数
3. 对于当前元素，查询线段树中比它小的所有数值的总出现次数
4. 将当前元素插入线段树中

时间复杂度分析:
- 离散化: O(n log n)
- 线段树操作: O(n log n)
- 总时间复杂度: O(n log n)

空间复杂度分析:
- 离散化映射: O(n)
- 线段树: O(n)
- 总空间复杂度: O(n)

工程化考量:
1. 异常处理: 处理空数组和单个元素的情况
2. 边界情况: 处理重复元素和极端值
3. 性能优化: 使用离散化减少线段树大小
4. 可读性: 详细的注释和清晰的代码结构
"""

from typing import List

class SegmentTree:
    """
    线段树类 - 用于统计数值出现次数
    """
    
    def __init__(self, size: int):
        """
        构造函数
        Args:
            size: 线段树大小
        """
        self.n = size
        self.tree = [0] * (4 * size)
    
    def update(self, pos: int, val: int) -> None:
        """
        单点更新 - 在位置pos处增加val
        Args:
            pos: 位置
            val: 增加值
        """
        self._update(0, self.n - 1, 1, pos, val)
    
    def query(self, pos: int) -> int:
        """
        区间查询 - 查询区间[0, pos-1]的和
        Args:
            pos: 位置
        Returns:
            区间和
        """
        if pos <= 0:
            return 0
        return self._query(0, self.n - 1, 1, 0, pos - 1)
    
    def _update(self, l: int, r: int, idx: int, pos: int, val: int) -> None:
        """
        递归更新实现
        """
        if l == r:
            self.tree[idx] += val
            return
        
        mid = (l + r) // 2
        if pos <= mid:
            self._update(l, mid, idx * 2, pos, val)
        else:
            self._update(mid + 1, r, idx * 2 + 1, pos, val)
        
        self.tree[idx] = self.tree[idx * 2] + self.tree[idx * 2 + 1]
    
    def _query(self, l: int, r: int, idx: int, ql: int, qr: int) -> int:
        """
        递归查询实现
        """
        if ql > qr:
            return 0
        if ql <= l and r <= qr:
            return self.tree[idx]
        
        mid = (l + r) // 2
        total = 0
        if ql <= mid:
            total += self._query(l, mid, idx * 2, ql, qr)
        if qr > mid:
            total += self._query(mid + 1, r, idx * 2 + 1, ql, qr)
        
        return total

def countSmaller(nums: List[int]) -> List[int]:
    """
    主函数 - 计算右侧小于当前元素的个数
    Args:
        nums: 输入数组
    Returns:
        结果数组
    """
    # 处理空数组情况
    if not nums:
        return []
    
    n = len(nums)
    result = []
    
    # 单个元素情况
    if n == 1:
        return [0]
    
    # 离散化处理
    sorted_nums = sorted(nums)
    mapping = {}
    idx = 0
    for num in sorted_nums:
        if num not in mapping:
            mapping[num] = idx
            idx += 1
    
    # 初始化线段树
    seg_tree = SegmentTree(idx)
    
    # 从右向左遍历
    for i in range(n - 1, -1, -1):
        pos = mapping[nums[i]]
        # 查询比当前元素小的数量
        count = seg_tree.query(pos)
        result.insert(0, count)
        # 更新当前元素出现次数
        seg_tree.update(pos, 1)
    
    return result

def test_count_smaller():
    """
    测试函数
    """
    # 测试用例1: 示例输入
    nums1 = [5, 2, 6, 1]
    result1 = countSmaller(nums1)
    print(f"测试用例1: {result1}")  # 期望输出: [2, 1, 1, 0]
    
    # 测试用例2: 空数组
    nums2 = []
    result2 = countSmaller(nums2)
    print(f"测试用例2: {result2}")  # 期望输出: []
    
    # 测试用例3: 单个元素
    nums3 = [1]
    result3 = countSmaller(nums3)
    print(f"测试用例3: {result3}")  # 期望输出: [0]
    
    # 测试用例4: 重复元素
    nums4 = [2, 2, 2, 2]
    result4 = countSmaller(nums4)
    print(f"测试用例4: {result4}")  # 期望输出: [0, 0, 0, 0]
    
    # 测试用例5: 递减序列
    nums5 = [5, 4, 3, 2, 1]
    result5 = countSmaller(nums5)
    print(f"测试用例5: {result5}")  # 期望输出: [4, 3, 2, 1, 0]
    
    # 测试用例6: 递增序列
    nums6 = [1, 2, 3, 4, 5]
    result6 = countSmaller(nums6)
    print(f"测试用例6: {result6}")  # 期望输出: [0, 0, 0, 0, 0]

if __name__ == "__main__":
    test_count_smaller()

"""
算法复杂度详细分析:

时间复杂度:
1. 数组排序: O(n log n)
2. 离散化映射构建: O(n)
3. 线段树操作(更新和查询): O(n log n)
总时间复杂度: O(n log n)

空间复杂度:
1. 排序数组: O(n)
2. 离散化映射: O(n)
3. 线段树: O(n)
4. 结果数组: O(n)
总空间复杂度: O(n)

算法优化点:
1. 离散化处理: 将原始数值映射到连续索引，减少线段树大小
2. 从右向左遍历: 避免重复计算，每个元素只处理一次
3. 线段树优化: 使用递归实现，代码清晰易懂

面试要点:
1. 理解离散化的必要性
2. 掌握线段树的基本操作
3. 能够分析时间空间复杂度
4. 处理边界情况和异常输入

Python语言特性:
1. 使用类型注解提高代码可读性
2. 利用列表推导式简化代码
3. 注意Python的列表插入操作时间复杂度为O(n)
4. 使用字典进行高效的离散化映射

工程化改进建议:
1. 可以添加输入验证和异常抛出
2. 考虑使用更高效的数据结构
3. 添加性能测试和基准测试
4. 提供多种解法的对比分析
"""