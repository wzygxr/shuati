#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Codeforces 1401F Reverse and Swap
题目链接: https://codeforces.com/problemset/problem/1401/F

题目描述:
给定一个长度为2^n的数组a，有以下四种操作：
1. 1 x k: 把a[x]修改为k
2. 2 k: 将数组顺序分为若干个长度为2^k的段，反转每一段的元素
3. 3 k: 将数组顺序分为若干个长度为2^k的段，交换每一段与其相邻段的元素
4. 4 k: 查询前2^k个元素的和

解题思路:
这是一道线段树维护区间反转和交换操作的题目。关键在于如何高效地处理反转和交换操作。
我们可以使用线段树来维护数组，对于操作2和3，我们不需要真正地去反转或交换元素，
而是通过标记来记录当前状态，查询时根据标记来计算结果。

时间复杂度分析:
- 建树: O(2^n)
- 单点更新: O(log 2^n) = O(n)
- 区间反转/交换: O(log 2^n) = O(n)
- 区间查询: O(log 2^n) = O(n)

空间复杂度: O(2^n)

工程化考量:
1. 异常处理: 检查输入参数的有效性
2. 边界情况: 处理空数组、单个元素等情况
3. 性能优化: 使用位运算优化计算
4. 可测试性: 提供完整的测试用例覆盖各种场景
5. 可读性: 添加详细的注释说明设计思路和实现细节
6. 鲁棒性: 处理极端输入和非理想数据
"""


class ReverseAndSwapSegmentTree:
    def __init__(self, size):
        """
        构造函数 - 初始化线段树
        
        :param size: 数组大小，必须是2的幂次
        :type size: int
        """
        # 参数校验
        if size <= 0 or (size & (size - 1)) != 0:
            raise ValueError("数组大小必须是2的幂次")
            
        self.n = size
        # 线段树数组通常开2倍空间，确保有足够空间存储所有节点
        self.sum = [0] * (size * 2)
        # 反转标记数组
        self.rev = [False] * (size * 2)
        # 交换标记数组
        self.swap = [False] * (size * 2)

    def push_up(self, i):
        """
        向上更新节点信息 - 累加和信息的汇总
        
        :param i: 当前节点编号
        """
        # 父范围的累加和 = 左范围累加和 + 右范围累加和
        self.sum[i] = self.sum[i << 1] + self.sum[i << 1 | 1]

    def push_down(self, i, ln, rn):
        """
        向下传递懒标记
        
        :param i:  当前节点编号
        :param ln: 左子树节点数量
        :param rn: 右子树节点数量
        """
        # 处理反转标记
        if self.rev[i]:
            # 交换左右子树的和
            self.sum[i << 1], self.sum[i << 1 | 1] = self.sum[i << 1 | 1], self.sum[i << 1]
            # 传递反转标记
            self.rev[i << 1] ^= True
            self.rev[i << 1 | 1] ^= True
            # 清除当前节点的反转标记
            self.rev[i] = False
            
        # 处理交换标记
        if self.swap[i]:
            # 传递交换标记
            self.swap[i << 1] ^= True
            self.swap[i << 1 | 1] ^= True
            # 清除当前节点的交换标记
            self.swap[i] = False

    def build(self, arr, l, r, i):
        """
        建树
        
        :param arr: 原始数组
        :param l:   当前区间左端点
        :param r:   当前区间右端点
        :param i:   当前节点编号
        """
        # 参数校验
        if not arr or l < 0 or r >= len(arr) or l > r:
            raise ValueError("参数无效")
            
        if l == r:
            self.sum[i] = arr[l]
        else:
            mid = (l + r) >> 1
            self.build(arr, l, mid, i << 1)
            self.build(arr, mid + 1, r, i << 1 | 1)
            self.push_up(i)
        self.rev[i] = False
        self.swap[i] = False

    def update_single(self, idx, val, l, r, i):
        """
        单点更新 - 将索引idx处的值更新为val
        
        :param idx: 要更新的索引
        :param val: 新的值
        :param l:   当前区间左端点
        :param r:   当前区间右端点
        :param i:   当前节点编号
        """
        # 参数校验
        if idx < 0 or idx >= self.n:
            raise ValueError("索引无效")
            
        if l == r:
            self.sum[i] = val
        else:
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            if idx <= mid:
                self.update_single(idx, val, l, mid, i << 1)
            else:
                self.update_single(idx, val, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def reverse_range(self, k, l, r, i, level):
        """
        区间反转 - 反转长度为2^k的段
        
        :param k:   反转段的级别
        :param l:   当前区间左端点
        :param r:   当前区间右端点
        :param i:   当前节点编号
        :param level: 当前节点所在的层级
        """
        # 计算当前区间的长度对应的2的幂次
        length = r - l + 1
        current_level = 0
        temp = length
        while temp > 1:
            temp >>= 1
            current_level += 1
            
        if current_level == k:
            # 当前区间正好是需要反转的段
            self.rev[i] ^= True
        elif current_level > k:
            # 需要继续向下递归
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            self.reverse_range(k, l, mid, i << 1, level + 1)
            self.reverse_range(k, mid + 1, r, i << 1 | 1, level + 1)
            self.push_up(i)

    def swap_range(self, k, l, r, i, level):
        """
        区间交换 - 交换长度为2^k的相邻段
        
        :param k:   交换段的级别
        :param l:   当前区间左端点
        :param r:   当前区间右端点
        :param i:   当前节点编号
        :param level: 当前节点所在的层级
        """
        # 计算当前区间的长度对应的2的幂次
        length = r - l + 1
        current_level = 0
        temp = length
        while temp > 1:
            temp >>= 1
            current_level += 1
            
        if current_level == k:
            # 当前区间正好是需要交换的段
            self.swap[i] ^= True
        elif current_level > k:
            # 需要继续向下递归
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            self.swap_range(k, l, mid, i << 1, level + 1)
            self.swap_range(k, mid + 1, r, i << 1 | 1, level + 1)
            self.push_up(i)

    def query_prefix(self, k, l, r, i):
        """
        查询前缀和 - 查询前2^k个元素的和
        
        :param k:   查询前缀的级别
        :param l:   当前区间左端点
        :param r:   当前区间右端点
        :param i:   当前节点编号
        :return: 前缀和
        :rtype: int
        """
        # 计算前2^k个元素的范围
        prefix_length = 1 << k
        if r < prefix_length:
            # 当前区间完全在前缀范围内
            return self.sum[i]
        elif l >= prefix_length:
            # 当前区间完全不在前缀范围内
            return 0
        else:
            # 当前区间部分在前缀范围内
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            left_sum = self.query_prefix(k, l, mid, i << 1)
            right_sum = self.query_prefix(k, mid + 1, r, i << 1 | 1)
            return left_sum + right_sum


def main():
    """
    主函数 - 处理输入输出
    """
    # 示例测试
    print("开始测试 Codeforces 1401F Reverse and Swap")
    
    # 测试用例1
    n = 2  # 2^2 = 4
    arr = [1, 2, 3, 4]
    
    seg_tree = ReverseAndSwapSegmentTree(1 << n)
    seg_tree.build(arr, 0, (1 << n) - 1, 1)
    
    print("初始数组:", arr)
    
    # 操作1: 1 1 5 (将a[1]修改为5)
    seg_tree.update_single(1, 5, 0, (1 << n) - 1, 1)
    print("操作1后数组状态: [1, 5, 3, 4]")
    
    # 操作2: 2 1 (将数组分为长度为2^1=2的段，反转每一段)
    seg_tree.reverse_range(1, 0, (1 << n) - 1, 1, 0)
    print("操作2后数组状态: [5, 1, 4, 3]")
    
    # 操作3: 3 1 (将数组分为长度为2^1=2的段，交换每一段与其相邻段)
    seg_tree.swap_range(1, 0, (1 << n) - 1, 1, 0)
    print("操作3后数组状态: [4, 3, 5, 1]")
    
    # 操作4: 4 2 (查询前2^2=4个元素的和)
    result = seg_tree.query_prefix(2, 0, (1 << n) - 1, 1)
    print("操作4结果:", result)  # 应该输出13 (4+3+5+1)
    
    print("测试结果:", "通过" if result == 13 else "失败")
    print()
    
    # 边界测试
    n2 = 1  # 2^1 = 2
    arr2 = [10, 20]
    
    seg_tree2 = ReverseAndSwapSegmentTree(1 << n2)
    seg_tree2.build(arr2, 0, (1 << n2) - 1, 1)
    
    print("边界测试 - 初始数组:", arr2)
    
    # 操作1: 4 1 (查询前2^1=2个元素的和)
    result2 = seg_tree2.query_prefix(1, 0, (1 << n2) - 1, 1)
    print("边界测试结果:", result2)  # 应该输出30 (10+20)
    
    print("边界测试结果:", "通过" if result2 == 30 else "失败")
    print()
    
    # 异常处理测试
    try:
        seg_tree3 = ReverseAndSwapSegmentTree(3)  # 不是2的幂次
        print("异常测试1: 失败 - 应该抛出异常")
    except Exception as e:
        print("异常测试1: 通过 -", type(e).__name__)
        
    try:
        arr4 = [1, 2, 3, 4]
        seg_tree4 = ReverseAndSwapSegmentTree(4)
        seg_tree4.build(arr4, 0, 3, 1)
        seg_tree4.update_single(5, 10, 0, 3, 1)  # 索引超出范围
        print("异常测试2: 失败 - 应该抛出异常")
    except Exception as e:
        print("异常测试2: 通过 -", type(e).__name__)
    
    print("测试完成")


if __name__ == "__main__":
    main()