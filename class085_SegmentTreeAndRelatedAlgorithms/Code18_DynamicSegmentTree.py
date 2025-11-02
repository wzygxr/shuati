"""
动态开点线段树 - 适用于值域较大或稀疏数据的场景

题目描述：
实现支持动态开点的线段树，避免预分配大量空间
应用场景：值域很大但实际数据稀疏的情况

题目来源：LeetCode 327. 区间和的个数
测试链接 : https://leetcode.cn/problems/count-of-range-sum/

解题思路：
使用动态开点线段树来处理值域较大但数据稀疏的情况。
与传统线段树不同，动态开点线段树只在需要时创建节点，节省空间。

核心思想：
1. 动态开点：只在需要时创建线段树节点，避免预分配大量空间
2. 按需分配：对于值域很大的情况，只创建实际用到的节点
3. 节点结构：每个节点维护区间信息和左右子节点指针

时间复杂度分析：
- 更新操作：O(log(maxVal-minVal))
- 查询操作：O(log(maxVal-minVal))

空间复杂度分析：
- O(n)，其中n是实际插入的元素个数
"""

from typing import List, Optional

class DynamicSegmentTreeNode:
    """动态开点线段树节点"""
    
    def __init__(self, min_val: int, max_val: int):
        """
        构造函数 - 创建线段树节点
        :param min_val: 节点管理区间的最小值
        :param max_val: 节点管理区间的最大值
        """
        self.min = min_val      # 节点管理区间的最小值
        self.max = max_val      # 节点管理区间的最大值
        self.count = 0          # 节点管理区间内的元素个数
        self.left = None        # 左子节点
        self.right = None       # 右子节点
    
    def get_mid(self) -> int:
        """
        获取区间中点
        用于将当前区间分成左右两个子区间
        :return: 区间中点值
        """
        return self.min + (self.max - self.min) // 2
    
    def update(self, val: int) -> None:
        """
        更新操作 - 在线段树中插入一个值
        :param val: 要插入的值
        
        时间复杂度: O(log(maxVal-minVal))
        """
        # 如果当前节点管理的区间只有一个值（叶子节点）
        if self.min == self.max:
            # 直接增加计数
            self.count += 1
            return
        
        # 计算区间中点
        mid = self.get_mid()
        # 根据值的大小决定插入左子树还是右子树
        if val <= mid:
            # 如果左子节点不存在，则创建左子节点
            if self.left is None:
                self.left = DynamicSegmentTreeNode(self.min, mid)
            # 递归更新左子树
            self.left.update(val)
        else:
            # 如果右子节点不存在，则创建右子节点
            if self.right is None:
                self.right = DynamicSegmentTreeNode(mid + 1, self.max)
            # 递归更新右子树
            self.right.update(val)
        
        # 更新当前节点的统计信息（左右子树元素个数之和）
        self.count = (self.left.count if self.left else 0) + (self.right.count if self.right else 0)
    
    def query(self, l: int, r: int) -> int:
        """
        查询操作 - 查询区间 [l, r] 内的元素个数
        :param l: 查询区间左边界
        :param r: 查询区间右边界
        :return: 区间[l, r]内的元素个数
        
        时间复杂度: O(log(maxVal-minVal))
        """
        # 如果查询区间与当前节点管理的区间无交集
        if l > self.max or r < self.min:
            # 返回0
            return 0
        # 如果当前节点管理的区间完全包含在查询区间内
        if l <= self.min and self.max <= r:
            # 直接返回当前节点的元素个数
            return self.count
        
        # 计算区间中点
        mid = self.get_mid()
        result = 0
        # 如果左子节点存在且查询区间与左子树区间有交集
        if self.left is not None and l <= mid:
            # 递归查询左子树
            result += self.left.query(l, r)
        # 如果右子节点存在且查询区间与右子树区间有交集
        if self.right is not None and r > mid:
            # 递归查询右子树
            result += self.right.query(l, r)
        
        return result


class DynamicSegmentTree:
    """动态开点线段树"""
    
    def __init__(self, min_val: int, max_val: int):
        """
        构造函数 - 创建动态开点线段树
        :param min_val: 值域最小值
        :param max_val: 值域最大值
        """
        self.min_val = min_val                  # 值域最小值
        self.max_val = max_val                  # 值域最大值
        # 创建根节点，管理整个值域范围
        self.root = DynamicSegmentTreeNode(min_val, max_val)
    
    def update(self, val: int) -> None:
        """
        插入一个值
        :param val: 要插入的值
        
        时间复杂度: O(log(maxVal-minVal))
        """
        self.root.update(val)
    
    def query(self, l: int, r: int) -> int:
        """
        查询区间 [l, r] 内的元素个数
        :param l: 查询区间左边界
        :param r: 查询区间右边界
        :return: 区间[l, r]内的元素个数
        
        时间复杂度: O(log(maxVal-minVal))
        """
        return self.root.query(l, r)


class Solution:
    """动态开点线段树解决方案"""
    
    def count_range_sum(self, nums: List[int], lower: int, upper: int) -> int:
        """
        LeetCode 327. 区间和的个数
        给定一个整数数组 nums，返回区间和在 [lower, upper] 之间的区间个数
        
        解题思路：
        使用前缀和 + 动态开点线段树的方法
        1. 计算前缀和数组
        2. 对于每个前缀和prefix_sum[i]，我们需要统计有多少个j>i满足：
           lower <= prefix_sum[j] - prefix_sum[i] <= upper
           即：prefix_sum[i] + lower <= prefix_sum[j] <= prefix_sum[i] + upper
        3. 从右向左遍历前缀和数组，使用动态开点线段树维护已遍历的前缀和
        
        时间复杂度: O(n log S) 其中S是前缀和的范围
        空间复杂度: O(n)
        
        :param nums: 整数数组
        :param lower: 区间下界
        :param upper: 区间上界
        :return: 区间和在[lower, upper]之间的区间个数
        """
        # 边界条件检查
        if not nums:
            return 0
        
        # 计算前缀和数组
        n = len(nums)
        prefix_sum = [0] * (n + 1)
        for i in range(n):
            prefix_sum[i + 1] = prefix_sum[i] + nums[i]
        
        # 获取前缀和的范围，用于确定动态开点线段树的值域
        min_prefix = min(prefix_sum)
        max_prefix = max(prefix_sum)
        
        # 创建动态开点线段树
        tree = DynamicSegmentTree(min_prefix, max_prefix)
        
        count = 0
        # 从右向左遍历前缀和数组
        for i in range(n, -1, -1):
            current = prefix_sum[i]
            # 查询满足 lower <= prefix_sum[j] - current <= upper 的j的个数
            # 即查询 prefix_sum[j] 在 [current + lower, current + upper] 范围内的个数
            count += tree.query(current + lower, current + upper)
            # 将当前前缀和插入线段树
            tree.update(current)
        
        return count
    
    def count_inversions(self, nums: List[int]) -> int:
        """
        动态开点线段树的其他应用：求逆序对
        使用动态开点线段树求解数组中的逆序对个数
        
        解题思路：
        逆序对是指对于数组中的两个元素nums[i]和nums[j]，如果i<j且nums[i]>nums[j]，则构成一个逆序对
        我们从右向左遍历数组，对于每个元素nums[i]，统计右侧有多少个元素比它小
        
        时间复杂度: O(n log maxVal)
        空间复杂度: O(n)
        
        :param nums: 整数数组
        :return: 数组中的逆序对个数
        """
        # 边界条件检查
        if not nums:
            return 0
        
        # 获取数组值的范围，用于确定动态开点线段树的值域
        min_val = min(nums)
        max_val = max(nums)
        
        # 创建动态开点线段树
        tree = DynamicSegmentTree(min_val, max_val)
        
        inversions = 0
        # 从右向左遍历，统计每个元素右侧比它小的元素个数
        for i in range(len(nums) - 1, -1, -1):
            # 查询[min_val, nums[i]-1]范围内的元素个数，即右侧比nums[i]小的元素个数
            inversions += tree.query(min_val, nums[i] - 1)
            # 将当前元素插入线段树
            tree.update(nums[i])
        
        return inversions


def test_dynamic_segment_tree():
    """测试动态开点线段树的基本功能"""
    print("=== 动态开点线段树测试 ===")
    
    # 创建动态开点线段树，值域为 [-1000, 1000]
    tree = DynamicSegmentTree(-1000, 1000)
    
    # 插入一些值
    tree.update(10)
    tree.update(20)
    tree.update(30)
    tree.update(40)
    tree.update(50)
    
    # 测试查询
    print(f"区间[15,35]内的元素个数: {tree.query(15, 35)}")  # 应该输出 2 (20, 30)
    print(f"区间[0,100]内的元素个数: {tree.query(0, 100)}")  # 应该输出 5
    print(f"区间[-10,10]内的元素个数: {tree.query(-10, 10)}")  # 应该输出 1 (10)
    
    # 插入更多值
    tree.update(5)
    tree.update(15)
    tree.update(25)
    
    print(f"插入后区间[0,30]内的元素个数: {tree.query(0, 30)}")  # 应该输出 6
    
    print("=== 测试完成 ===")


def test_range_sum():
    """测试区间和的个数"""
    print("=== 区间和的个数测试 ===")
    
    solution = Solution()
    
    # 测试用例1
    nums1 = [-2, 5, -1]
    lower1, upper1 = -2, 2
    result1 = solution.count_range_sum(nums1, lower1, upper1)
    print(f"测试用例1: nums = {nums1}, lower = {lower1}, upper = {upper1}")
    print(f"结果: {result1}")  # 应该输出 3
    
    # 测试用例2
    nums2 = [0]
    lower2, upper2 = 0, 0
    result2 = solution.count_range_sum(nums2, lower2, upper2)
    print(f"测试用例2: nums = {nums2}, lower = {lower2}, upper = {upper2}")
    print(f"结果: {result2}")  # 应该输出 1
    
    print("=== 测试完成 ===")


def test_inversions():
    """测试逆序对计数"""
    print("=== 逆序对计数测试 ===")
    
    solution = Solution()
    
    nums1 = [2, 4, 1, 3, 5]
    result1 = solution.count_inversions(nums1)
    print(f"数组: {nums1}")
    print(f"逆序对个数: {result1}")  # 应该输出 3
    
    nums2 = [5, 4, 3, 2, 1]
    result2 = solution.count_inversions(nums2)
    print(f"数组: {nums2}")
    print(f"逆序对个数: {result2}")  # 应该输出 10
    
    nums3 = [1, 2, 3, 4, 5]
    result3 = solution.count_inversions(nums3)
    print(f"数组: {nums3}")
    print(f"逆序对个数: {result3}")  # 应该输出 0
    
    print("=== 测试完成 ===")


# 运行测试
if __name__ == "__main__":
    test_dynamic_segment_tree()
    test_range_sum()
    test_inversions()