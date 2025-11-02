"""
区间最值查询 - 线段树实现

题目描述：
实现一个支持区间最值查询和单点更新的数据结构
支持以下操作：
1. 构造函数：用整数数组初始化对象
2. update：将数组中某个位置的值更新为新值
3. query_max：查询数组中某个区间内的最大值
4. query_min：查询数组中某个区间内的最小值

解题思路：
使用线段树来维护区间最值信息。线段树是一种二叉树结构，每个节点代表一个区间，
节点中存储该区间的最值信息。对于叶子节点，它代表数组中的单个元素；对于非叶子节点，
它代表其左右子树所覆盖区间的合并结果（在这里是最值）。

算法步骤：
1. 构建线段树：
   - 对于长度为n的数组，线段树通常需要4*n的空间
   - 递归地将数组分成两半，直到每个区间只包含一个元素
   - 每个非叶子节点存储其左右子节点最值的合并结果

2. 单点更新：
   - 从根节点开始，找到对应位置的叶子节点
   - 更新该叶子节点的值
   - 自底向上更新所有祖先节点的最值

3. 区间最值查询：
   - 从根节点开始递归查询
   - 如果当前节点表示的区间完全包含在查询区间内，则直接返回该节点的最值
   - 如果当前节点表示的区间与查询区间无交集，则返回无效值（最大值查询返回负无穷，最小值查询返回正无穷）
   - 否则递归查询左右子树，并返回合并后的结果

时间复杂度分析：
- 构建线段树：O(n)，其中n是数组长度
- 单点更新：O(log n)
- 区间最值查询：O(log n)

空间复杂度分析：
- 线段树需要O(n)的额外空间
"""

import sys
from typing import List

class SegmentTree:
    """线段树类 - 维护区间最值"""
    
    def __init__(self, nums: List[int]):
        """
        构造函数 - 初始化线段树
        :param nums: 原始数组
        """
        self.n = len(nums)
        # 线段树通常需要4倍空间
        self.tree_max = [float('-inf')] * (4 * self.n)
        self.tree_min = [float('inf')] * (4 * self.n)
        self.build_tree(nums, 0, 0, self.n - 1)
    
    def build_tree(self, nums: List[int], node: int, start: int, end: int) -> None:
        """
        构建线段树
        递归地将数组构建成线段树结构
        :param nums: 原始数组
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        if start == end:
            # 叶子节点 - 直接存储数组元素值
            self.tree_max[node] = nums[start]
            self.tree_min[node] = nums[start]
        else:
            mid = (start + end) // 2
            # 递归构建左子树
            self.build_tree(nums, 2 * node + 1, start, mid)
            # 递归构建右子树
            self.build_tree(nums, 2 * node + 2, mid + 1, end)
            # 合并左右子树的结果
            self.tree_max[node] = max(self.tree_max[2 * node + 1], self.tree_max[2 * node + 2])
            self.tree_min[node] = min(self.tree_min[2 * node + 1], self.tree_min[2 * node + 2])
    
    def update(self, index: int, val: int) -> None:
        """
        更新数组中某个位置的值
        :param index: 要更新的数组索引
        :param val: 新的值
        
        时间复杂度: O(log n)
        """
        self._update_helper(0, 0, self.n - 1, index, val)
    
    def _update_helper(self, node: int, start: int, end: int, index: int, val: int) -> None:
        """
        更新辅助函数 - 递归更新线段树节点
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param index: 要更新的数组索引
        :param val: 新的值
        """
        if start == end:
            # 找到叶子节点，更新值
            self.tree_max[node] = val
            self.tree_min[node] = val
        else:
            mid = (start + end) // 2
            if index <= mid:
                # 要更新的位置在左子树中
                self._update_helper(2 * node + 1, start, mid, index, val)
            else:
                # 要更新的位置在右子树中
                self._update_helper(2 * node + 2, mid + 1, end, index, val)
            # 更新父节点的值
            self.tree_max[node] = max(self.tree_max[2 * node + 1], self.tree_max[2 * node + 2])
            self.tree_min[node] = min(self.tree_min[2 * node + 1], self.tree_min[2 * node + 2])
    
    def query_max(self, left: int, right: int) -> int:
        """
        查询区间最大值
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间[left, right]内的最大值
        
        时间复杂度: O(log n)
        """
        return self._query_max_helper(0, 0, self.n - 1, left, right)
    
    def _query_max_helper(self, node: int, start: int, end: int, left: int, right: int) -> int:
        """
        查询区间最大值辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间[left, right]内的最大值
        """
        if right < start or end < left:
            # 查询区间与当前区间无交集 - 返回无效值
            return float('-inf')
        if left <= start and end <= right:
            # 当前区间完全包含在查询区间内 - 直接返回节点值
            return self.tree_max[node]
        # 部分重叠，递归查询左右子树
        mid = (start + end) // 2
        left_max = self._query_max_helper(2 * node + 1, start, mid, left, right)
        right_max = self._query_max_helper(2 * node + 2, mid + 1, end, left, right)
        # 返回左右子树最大值中的较大者
        return max(left_max, right_max)
    
    def query_min(self, left: int, right: int) -> int:
        """
        查询区间最小值
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间[left, right]内的最小值
        
        时间复杂度: O(log n)
        """
        return self._query_min_helper(0, 0, self.n - 1, left, right)
    
    def _query_min_helper(self, node: int, start: int, end: int, left: int, right: int) -> int:
        """
        查询区间最小值辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间[left, right]内的最小值
        """
        if right < start or end < left:
            # 查询区间与当前区间无交集 - 返回无效值
            return float('inf')
        if left <= start and end <= right:
            # 当前区间完全包含在查询区间内 - 直接返回节点值
            return self.tree_min[node]
        # 部分重叠，递归查询左右子树
        mid = (start + end) // 2
        left_min = self._query_min_helper(2 * node + 1, start, mid, left, right)
        right_min = self._query_min_helper(2 * node + 2, mid + 1, end, left, right)
        # 返回左右子树最小值中的较小者
        return min(left_min, right_min)


class RangeMaximumQuery:
    """区间最值查询主类"""
    
    def __init__(self, nums: List[int]):
        """
        构造函数 - 用整数数组初始化对象
        :param nums: 整数数组
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        self.st = SegmentTree(nums)
    
    def update(self, index: int, val: int) -> None:
        """
        更新数组中某个位置的值
        :param index: 要更新的数组索引
        :param val: 新的值
        
        时间复杂度: O(log n)
        """
        self.st.update(index, val)
    
    def query_max(self, left: int, right: int) -> int:
        """
        查询区间最大值
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间[left, right]内的最大值
        
        时间复杂度: O(log n)
        """
        return self.st.query_max(left, right)
    
    def query_min(self, left: int, right: int) -> int:
        """
        查询区间最小值
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间[left, right]内的最小值
        
        时间复杂度: O(log n)
        """
        return self.st.query_min(left, right)


# 测试代码
if __name__ == "__main__":
    # 测试用例:
    nums = [1, 3, 5, 2, 8, 4]
    rmq = RangeMaximumQuery(nums)
    
    # 测试区间最大值查询
    print(f"区间[0,2]的最大值: {rmq.query_max(0, 2)}")  # 应该输出 5
    print(f"区间[2,5]的最大值: {rmq.query_max(2, 5)}")  # 应该输出 8
    print(f"区间[1,3]的最大值: {rmq.query_max(1, 3)}")  # 应该输出 5
    
    # 测试区间最小值查询
    print(f"区间[0,2]的最小值: {rmq.query_min(0, 2)}")  # 应该输出 1
    print(f"区间[2,5]的最小值: {rmq.query_min(2, 5)}")  # 应该输出 2
    print(f"区间[1,3]的最小值: {rmq.query_min(1, 3)}")  # 应该输出 2
    
    # 测试更新操作
    rmq.update(3, 10)  # 将索引3的值从2更新为10
    print(f"更新后区间[2,5]的最大值: {rmq.query_max(2, 5)}")  # 应该输出 10
    print(f"更新后区间[1,3]的最小值: {rmq.query_min(1, 3)}")  # 应该输出 3