#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 307. 区域和检索 - 数组可修改
题目链接: https://leetcode.cn/problems/range-sum-query-mutable/

给你一个数组 nums ，请你完成两类查询：
1. 将一个元素的值更新为 val
2. 返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的和

实现 NumArray 类：
- NumArray(int[] nums) 用整数数组 nums 初始化对象
- void update(int index, int val) 将 nums[index] 的值更新为 val
- int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的和

示例:
输入:
["NumArray", "sumRange", "update", "sumRange"]
[[[1, 3, 5]], [0, 2], [1, 2], [0, 2]]
输出:
[null, 9, null, 8]

解释:
NumArray numArray = new NumArray([1, 3, 5]);
numArray.sumRange(0, 2); // 返回 1 + 3 + 5 = 9
numArray.update(1, 2);   // nums = [1,2,5]
numArray.sumRange(0, 2); // 返回 1 + 2 + 5 = 8

提示:
1 <= nums.length <= 3 * 10^4
-100 <= nums[i] <= 100
0 <= index < nums.length
-100 <= val <= 100
0 <= left <= right < nums.length
调用 update 和 sumRange 方法次数不大于 3 * 10^4

解题思路:
这是一个经典的线段树应用问题，支持单点更新和区间查询。
1. 使用线段树维护数组区间和
2. 单点更新时，从根节点向下递归找到目标位置并更新，然后向上传递更新区间和
3. 区间查询时，根据查询区间与当前节点区间的关系进行递归查询

时间复杂度:
- 建树: O(n)
- 单点更新: O(log n)
- 区间查询: O(log n)
空间复杂度: O(n)

工程化考量:
1. 异常处理: 检查输入参数的有效性
2. 边界情况: 处理空数组、单个元素等情况
3. 性能优化: 使用位运算优化计算
4. 可测试性: 提供完整的测试用例覆盖各种场景
5. 可读性: 添加详细的注释说明设计思路和实现细节
6. 鲁棒性: 处理极端输入和非理想数据
"""


class LeetCode307_SegmentTree:
    def __init__(self, nums):
        """
        构造函数 - 初始化线段树
        
        :param nums: 初始数组
        :type nums: List[int]
        """
        # 参数校验
        if nums is None:
            raise ValueError("输入数组不能为None")
        
        self.nums = nums
        self.n = len(nums)
        
        # 特殊情况处理
        if self.n == 0:
            self.sum = []
            return
            
        # 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        self.sum = [0] * (self.n * 4)
        self.build(0, self.n - 1, 1)

    def build(self, l, r, i):
        """
        建树
        
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param i: 当前节点编号
        """
        # 递归终止条件：叶子节点
        if l == r:
            self.sum[i] = self.nums[l]
        else:
            # 计算中点
            mid = (l + r) >> 1
            # 递归构建左子树
            self.build(l, mid, i << 1)
            # 递归构建右子树
            self.build(mid + 1, r, i << 1 | 1)
            # 向上传递更新节点信息
            self.push_up(i)

    def push_up(self, i):
        """
        向上更新节点信息 - 累加和信息的汇总
        
        :param i: 当前节点编号
        """
        # 父范围的累加和 = 左范围累加和 + 右范围累加和
        self.sum[i] = self.sum[i << 1] + self.sum[i << 1 | 1]

    def update(self, index, val, l=0, r=None, i=1):
        """
        更新数组中指定索引的值
        
        :param index: 要更新的索引
        :param val: 新的值
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param i: 当前节点编号
        """
        # 参数校验
        if index < 0 or index >= self.n:
            raise IndexError("索引超出范围")
            
        if r is None:
            r = self.n - 1
            self.nums[index] = val
            
        # 递归终止条件：找到目标位置
        if l == r:
            self.sum[i] = val
        else:
            # 计算中点
            mid = (l + r) >> 1
            # 根据索引位置决定递归方向
            if index <= mid:
                self.update(index, val, l, mid, i << 1)
            else:
                self.update(index, val, mid + 1, r, i << 1 | 1)
            # 向上传递更新节点信息
            self.push_up(i)

    def sum_range(self, left, right, l=0, r=None, i=1):
        """
        查询区间和
        
        :param left: 查询区间左端点
        :param right: 查询区间右端点
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param i: 当前节点编号
        :return: 区间和
        :rtype: int
        """
        # 参数校验
        if left < 0 or right >= self.n or left > right:
            raise ValueError("查询区间无效")
            
        if r is None:
            r = self.n - 1
            
        # 当前区间完全包含在查询区间内，直接返回
        if left <= l and r <= right:
            return self.sum[i]
            
        # 计算中点
        mid = (l + r) >> 1
        ans = 0
        # 左子区间与查询区间有交集
        if left <= mid:
            ans += self.sum_range(left, right, l, mid, i << 1)
        # 右子区间与查询区间有交集
        if right > mid:
            ans += self.sum_range(left, right, mid + 1, r, i << 1 | 1)
        return ans


# 测试代码
if __name__ == "__main__":
    print("开始测试 LeetCode 307. Range Sum Query - Mutable")
    
    # 示例测试
    nums = [1, 3, 5]
    numArray = LeetCode307_SegmentTree(nums)
    result1 = numArray.sum_range(0, 2)
    print("初始数组和 [0,2]:", result1)  # 应该输出9
    numArray.update(1, 2)  # 更新索引1的值为2
    result2 = numArray.sum_range(0, 2)
    print("更新后数组和 [0,2]:", result2)  # 应该输出8
    print("示例测试结果:", "通过" if result1 == 9 and result2 == 8 else "失败")
    print()
    
    # 测试用例1: 单个元素
    nums1 = [5]
    numArray1 = LeetCode307_SegmentTree(nums1)
    result3 = numArray1.sum_range(0, 0)
    print("单个元素测试 [0,0]:", result3)  # 应该输出5
    numArray1.update(0, 10)
    result4 = numArray1.sum_range(0, 0)
    print("更新后单个元素 [0,0]:", result4)  # 应该输出10
    print("单个元素测试结果:", "通过" if result3 == 5 and result4 == 10 else "失败")
    print()
    
    # 测试用例2: 空数组
    try:
        nums2 = []
        numArray2 = LeetCode307_SegmentTree(nums2)
        print("空数组测试: 通过")
    except Exception as e:
        print("空数组测试: 失败 -", e)
    print()
    
    # 测试用例3: 大数组
    nums3 = list(range(1, 11))  # [1,2,3,4,5,6,7,8,9,10]
    numArray3 = LeetCode307_SegmentTree(nums3)
    result5 = numArray3.sum_range(0, 9)  # 应该是55
    result6 = numArray3.sum_range(2, 5)  # 应该是3+4+5+6=18
    print("大数组测试 [0,9]:", result5)
    print("大数组测试 [2,5]:", result6)
    numArray3.update(4, 0)  # 将索引4的值(5)更新为0
    result7 = numArray3.sum_range(2, 5)  # 应该是3+4+0+6=13
    print("更新后大数组测试 [2,5]:", result7)
    print("大数组测试结果:", "通过" if result5 == 55 and result6 == 18 and result7 == 13 else "失败")
    print()
    
    # 异常处理测试
    try:
        nums4 = [1, 2, 3]
        numArray4 = LeetCode307_SegmentTree(nums4)
        numArray4.update(5, 10)  # 索引超出范围
        print("异常测试1: 失败 - 应该抛出异常")
    except Exception as e:
        print("异常测试1: 通过 -", type(e).__name__)
        
    try:
        nums5 = [1, 2, 3]
        numArray5 = LeetCode307_SegmentTree(nums5)
        numArray5.sum_range(2, 1)  # 无效区间
        print("异常测试2: 失败 - 应该抛出异常")
    except Exception as e:
        print("异常测试2: 通过 -", type(e).__name__)
    
    print("测试完成")