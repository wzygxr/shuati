# LeetCode 307. 区域和检索 - 数组可修改 (树状数组实现)
# 给你一个数组 nums ，请你完成两类查询:
# 1. 将一个值加到 nums[index] 上
# 2. 返回数组 nums 中索引 left 和 right 之间的元素和（包含）
# 实现 NumArray 类:
# NumArray(int[] nums) 用整数数组 nums 初始化对象
# void update(int index, int val) 将 nums[index] 的值更新为 val
# int sumRange(int left, int right) 返回数组 nums 中索引 left 和 right 之间的元素和
# 测试链接: https://leetcode.cn/problems/range-sum-query-mutable/

from typing import List

class NumArray:
    """
    树状数组实现区域和检索 - 数组可修改
    
    解题思路:
    1. 使用树状数组(Fenwick Tree)数据结构来处理前缀和查询和单点更新
    2. 树状数组是一种基于数组的树形结构，利用二进制规律来组织数据
    3. 通过lowbit操作来确定节点间的关系
    
    时间复杂度分析:
    - 构建树状数组: O(n log n)，对每个元素进行更新操作
    - 单点更新: O(log n)，每次更新需要沿路径向上更新
    - 前缀和查询: O(log n)，每次查询需要沿路径向下累加
    - 区间查询: O(log n)，通过两次前缀和查询相减得到
    
    空间复杂度分析:
    - 树状数组: O(n)，只需要与原数组相同大小的额外空间
    
    工程化考量:
    1. 异常处理: 检查输入参数的有效性
    2. 边界条件: 处理空数组、单元素数组等情况
    3. 可读性: 添加详细注释，变量命名清晰
    4. 模块化: 将更新、查询操作分离
    """

    def __init__(self, nums: List[int]):
        """
        构造函数，初始化树状数组
        :param nums: 原始数组
        """
        self.n = len(nums)
        self.nums = nums[:]  # 原数组的副本
        self.tree = [0] * (self.n + 1)  # 树状数组，以下标1开始
        
        # 构建树状数组
        for i in range(self.n):
            # 更新操作，将nums[i]添加到位置(i+1)
            self._add(i + 1, nums[i])

    def _lowbit(self, x: int) -> int:
        """
        lowbit操作，获取x的二进制表示中最右边的1所代表的值
        :param x: 输入整数
        :return: x & (-x)
        """
        return x & (-x)

    def _add(self, index: int, delta: int) -> None:
        """
        在树状数组中更新指定位置的值（增加delta）
        :param index: 要更新的位置（从1开始）
        :param delta: 增加的值
        """
        # 沿路径向上更新所有相关节点
        while index <= self.n:
            self.tree[index] += delta
            index += self._lowbit(index)

    def _prefix_sum(self, index: int) -> int:
        """
        查询前缀和[1, index]的和
        :param index: 查询的右边界（从1开始）
        :return: 前缀和
        """
        sum_val = 0
        # 沿路径向下累加所有相关节点的值
        while index > 0:
            sum_val += self.tree[index]
            index -= self._lowbit(index)
        return sum_val

    def update(self, index: int, val: int) -> None:
        """
        更新数组中指定位置的值
        :param index: 要更新的位置
        :param val: 新的值
        """
        # 检查索引有效性
        if index < 0 or index >= self.n:
            raise IndexError("Index out of bounds")
        
        # 计算差值
        delta = val - self.nums[index]
        # 更新原数组
        self.nums[index] = val
        # 更新树状数组
        self._add(index + 1, delta)

    def sumRange(self, left: int, right: int) -> int:
        """
        查询指定区间的元素和
        :param left: 区间左边界（包含）
        :param right: 区间右边界（包含）
        :return: 区间元素和
        """
        # 检查参数有效性
        if left < 0 or right >= self.n or left > right:
            raise ValueError("Invalid range")
        
        # 区间和 = prefixSum(right+1) - prefixSum(left)
        return self._prefix_sum(right + 1) - self._prefix_sum(left)


# 测试函数
def test():
    # 测试用例1
    nums1 = [1, 3, 5]
    numArray1 = NumArray(nums1)
    
    # 测试 sumRange [0, 2] 应该返回 9
    print(f"Sum from index 0 to 2: {numArray1.sumRange(0, 2)}")  # 期望输出: 9
    
    # 测试 update 将索引1的值更新为2
    numArray1.update(1, 2)
    
    # 测试 sumRange [0, 2] 应该返回 8
    print(f"Sum from index 0 to 2 after update: {numArray1.sumRange(0, 2)}")  # 期望输出: 8
    
    # 测试用例2
    nums2 = [9, -8]
    numArray2 = NumArray(nums2)
    
    # 测试 update 将索引1的值更新为3
    numArray2.update(1, 3)
    
    # 测试 sumRange [1, 1] 应该返回 3
    print(f"Sum from index 1 to 1: {numArray2.sumRange(1, 1)}")  # 期望输出: 3
    
    # 测试 update 将索引1的值更新为-3
    numArray2.update(1, -3)
    
    # 测试 sumRange [0, 1] 应该返回 6
    print(f"Sum from index 0 to 1: {numArray2.sumRange(0, 1)}")  # 期望输出: 6


if __name__ == "__main__":
    test()