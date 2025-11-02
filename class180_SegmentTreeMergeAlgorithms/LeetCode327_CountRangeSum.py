"""
LeetCode 327. 区间和的个数
题目链接: https://leetcode.com/problems/count-of-range-sum/

题目描述:
给定一个整数数组 nums，返回区间和在 [lower, upper] 范围内的区间个数（包含 lower 和 upper）。
区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。

示例:
输入: nums = [-2,5,-1], lower = -2, upper = 2
输出: 3
解释: 三个区间分别是: [0,0], [2,2], [0,2]，它们对应的区间和分别是 -2, -1, 2。

解题思路:
方法一：线段树 + 前缀和 + 离散化
1. 计算前缀和数组 prefixSum
2. 问题转化为：对于每个 j，统计有多少个 i < j 满足 lower ≤ prefixSum[j] - prefixSum[i] ≤ upper
3. 等价于：prefixSum[j] - upper ≤ prefixSum[i] ≤ prefixSum[j] - lower
4. 使用线段树统计前缀和的出现次数
5. 从右向左遍历，动态维护前缀和的出现次数

时间复杂度分析:
- 前缀和计算: O(n)
- 离散化: O(n log n)
- 线段树操作: O(n log n)
- 总时间复杂度: O(n log n)

空间复杂度分析:
- 前缀和数组: O(n)
- 离散化映射: O(n)
- 线段树: O(n)
- 总空间复杂度: O(n)

工程化考量:
1. 异常处理: 处理空数组和边界值
2. 数值溢出: 使用int类型防止整数溢出
3. 边界情况: 处理lower > upper的情况
4. 性能优化: 使用离散化减少线段树大小
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
    
    def query(self, ql: int, qr: int) -> int:
        """
        区间查询 - 查询区间[ql, qr]的和
        Args:
            ql: 左边界
            qr: 右边界
        Returns:
            区间和
        """
        if ql > qr:
            return 0
        return self._query(0, self.n - 1, 1, ql, qr)
    
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
        if ql <= l and r <= qr:
            return self.tree[idx]
        
        mid = (l + r) // 2
        total = 0
        if ql <= mid:
            total += self._query(l, mid, idx * 2, ql, qr)
        if qr > mid:
            total += self._query(mid + 1, r, idx * 2 + 1, ql, qr)
        
        return total

def countRangeSum(nums: List[int], lower: int, upper: int) -> int:
    """
    主函数 - 计算区间和个数
    Args:
        nums: 输入数组
        lower: 区间和下界
        upper: 区间和上界
    Returns:
        满足条件的区间个数
    """
    # 处理空数组情况
    if not nums:
        return 0
    
    # 处理lower > upper的情况
    if lower > upper:
        return 0
    
    n = len(nums)
    
    # 计算前缀和数组，使用int防止溢出
    prefix_sum = [0] * (n + 1)
    for i in range(n):
        prefix_sum[i + 1] = prefix_sum[i] + nums[i]
    
    # 收集所有需要离散化的值
    values = set()
    for s in prefix_sum:
        values.add(s)
        values.add(s - lower)
        values.add(s - upper)
    
    # 离散化映射
    sorted_values = sorted(values)
    mapping = {val: idx for idx, val in enumerate(sorted_values)}
    
    # 初始化线段树
    seg_tree = SegmentTree(len(sorted_values))
    
    count = 0
    # 从右向左遍历前缀和
    for i in range(n, -1, -1):
        current = prefix_sum[i]
        left_bound = current - upper
        right_bound = current - lower
        
        left_idx = mapping[left_bound]
        right_idx = mapping[right_bound]
        
        # 查询满足条件的区间个数
        count += seg_tree.query(left_idx, right_idx)
        
        # 更新当前前缀和的出现次数
        current_idx = mapping[current]
        seg_tree.update(current_idx, 1)
    
    return count

def test_count_range_sum():
    """
    测试函数
    """
    # 测试用例1: 示例输入
    nums1 = [-2, 5, -1]
    result1 = countRangeSum(nums1, -2, 2)
    print(f"测试用例1: {result1}")  # 期望输出: 3
    
    # 测试用例2: 空数组
    nums2 = []
    result2 = countRangeSum(nums2, 0, 0)
    print(f"测试用例2: {result2}")  # 期望输出: 0
    
    # 测试用例3: 单个元素
    nums3 = [1]
    result3 = countRangeSum(nums3, 1, 1)
    print(f"测试用例3: {result3}")  # 期望输出: 1
    
    # 测试用例4: 全零数组
    nums4 = [0, 0, 0, 0]
    result4 = countRangeSum(nums4, 0, 0)
    print(f"测试用例4: {result4}")  # 期望输出: 10
    
    # 测试用例5: 边界情况 lower > upper
    nums5 = [1, 2, 3]
    result5 = countRangeSum(nums5, 5, 3)
    print(f"测试用例5: {result5}")  # 期望输出: 0
    
    # 测试用例6: 大数测试
    nums6 = [2147483647, -2147483648, 0]
    result6 = countRangeSum(nums6, -1, 1)
    print(f"测试用例6: {result6}")  # 期望输出: 2

if __name__ == "__main__":
    test_count_range_sum()

"""
算法复杂度详细分析:

时间复杂度:
1. 前缀和计算: O(n)
2. 离散化集合构建: O(n log n)
3. 离散化映射构建: O(n)
4. 线段树操作(更新和查询): O(n log n)
总时间复杂度: O(n log n)

空间复杂度:
1. 前缀和数组: O(n)
2. 离散化集合: O(n)
3. 离散化映射: O(n)
4. 线段树: O(n)
总空间复杂度: O(n)

算法优化点:
1. 前缀和技巧: 将区间和问题转化为前缀和之差
2. 离散化处理: 减少线段树的大小
3. 从右向左遍历: 避免重复计算
4. 使用int类型: 防止整数溢出

面试要点:
1. 理解前缀和的应用场景
2. 掌握线段树在统计问题中的应用
3. 能够处理数值溢出问题
4. 分析复杂的时间空间复杂度

Python语言特性:
1. 使用类型注解提高代码可读性
2. 利用集合和字典进行高效离散化
3. 注意Python的整数溢出处理
4. 使用生成器表达式简化代码

工程化改进建议:
1. 可以添加输入验证和异常抛出
2. 考虑使用更高效的数据结构
3. 添加性能测试和基准测试
4. 提供多种解法的对比分析
"""