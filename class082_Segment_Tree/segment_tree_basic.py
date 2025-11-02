#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树基本实现
支持区间求和、区间更新操作

线段树是一种二叉树结构，每个节点代表一个区间，用于高效处理区间查询和更新操作。

核心思想：
1. 将数组区间划分为更小的子区间，直到单个元素
2. 每个节点存储其对应区间的相关信息（如区间和）
3. 通过合并子区间信息来维护父区间信息

时间复杂度：
- 建树：O(n)
- 单点更新：O(log n)
- 区间查询：O(log n)
- 区间更新：O(log n)

空间复杂度：O(n)
"""


class SegmentTreeBasic:
    """
    线段树基本实现类
    支持区间求和、区间更新操作
    """
    
    def __init__(self, nums):
        """
        构造函数
        :param nums: 输入数组
        """
        self.n = len(nums)
        self.arr = nums[:]
        # 线段树数组大小通常为4*n，确保足够容纳所有节点
        self.sum = [0] * (self.n << 2)
        self.lazy = [0] * (self.n << 2)
        
        # 构建线段树
        self._build(1, self.n, 1)
    
    def _build(self, l, r, rt):
        """
        构建线段树
        :param l: 区间左边界（从1开始）
        :param r: 区间右边界
        :param rt: 当前节点在sum数组中的索引
        """
        # 如果是叶子节点，直接赋值
        if l == r:
            self.sum[rt] = self.arr[l - 1]  # 注意数组索引从0开始
            return
        
        # 计算中点
        mid = l + ((r - l) >> 1)
        
        # 递归构建左右子树
        self._build(l, mid, rt << 1)
        self._build(mid + 1, r, rt << 1 | 1)
        
        # 合并左右子树信息
        self._push_up(rt)
    
    def _push_up(self, rt):
        """
        向上更新节点信息
        :param rt: 当前节点索引
        """
        self.sum[rt] = self.sum[rt << 1] + self.sum[rt << 1 | 1]
    
    def _push_down(self, rt, ln, rn):
        """
        下推懒标记
        :param rt: 当前节点索引
        :param ln: 左子树节点数量
        :param rn: 右子树节点数量
        """
        # 如果当前节点有懒标记
        if self.lazy[rt] != 0:
            # 将懒标记传递给左右子节点
            self.lazy[rt << 1] += self.lazy[rt]
            self.lazy[rt << 1 | 1] += self.lazy[rt]
            
            # 更新左右子节点的区间和
            self.sum[rt << 1] += self.lazy[rt] * ln
            self.sum[rt << 1 | 1] += self.lazy[rt] * rn
            
            # 清除当前节点的懒标记
            self.lazy[rt] = 0
    
    def update(self, L, R, C):
        """
        区间更新操作
        :param L: 更新区间左边界
        :param R: 更新区间右边界
        :param C: 更新值
        """
        self._update(L, R, C, 1, self.n, 1)
    
    def _update(self, L, R, C, l, r, rt):
        """
        区间更新操作（内部实现）
        :param L: 更新区间左边界
        :param R: 更新区间右边界
        :param C: 更新值
        :param l: 当前节点区间左边界
        :param r: 当前节点区间右边界
        :param rt: 当前节点索引
        """
        # 如果当前节点区间完全包含在更新区间内
        if L <= l and r <= R:
            self.sum[rt] += C * (r - l + 1)
            self.lazy[rt] += C
            return
        
        # 计算中点
        mid = l + ((r - l) >> 1)
        
        # 下推懒标记
        self._push_down(rt, mid - l + 1, r - mid)
        
        # 递归更新左右子树
        if L <= mid:
            self._update(L, R, C, l, mid, rt << 1)
        if R > mid:
            self._update(L, R, C, mid + 1, r, rt << 1 | 1)
        
        # 向上更新节点信息
        self._push_up(rt)
    
    def query(self, L, R):
        """
        区间查询操作
        :param L: 查询区间左边界
        :param R: 查询区间右边界
        :return: 区间和
        """
        return self._query(L, R, 1, self.n, 1)
    
    def _query(self, L, R, l, r, rt):
        """
        区间查询操作（内部实现）
        :param L: 查询区间左边界
        :param R: 查询区间右边界
        :param l: 当前节点区间左边界
        :param r: 当前节点区间右边界
        :param rt: 当前节点索引
        :return: 区间和
        """
        # 如果当前节点区间完全包含在查询区间内
        if L <= l and r <= R:
            return self.sum[rt]
        
        # 计算中点
        mid = l + ((r - l) >> 1)
        
        # 下推懒标记
        self._push_down(rt, mid - l + 1, r - mid)
        
        ans = 0
        
        # 递归查询左右子树
        if L <= mid:
            ans += self._query(L, R, l, mid, rt << 1)
        if R > mid:
            ans += self._query(L, R, mid + 1, r, rt << 1 | 1)
        
        return ans
    
    def update_point(self, index, value):
        """
        单点更新操作
        :param index: 更新位置（从1开始）
        :param value: 更新值
        """
        # 先查询当前值，然后计算差值进行区间更新
        old_value = self.query(index, index)
        self.update(index, index, value - old_value)
    
    def size(self):
        """
        获取数组长度
        :return: 数组长度
        """
        return self.n


# 测试函数
def test_segment_tree():
    """测试线段树实现"""
    print("测试线段树实现...")
    
    # 测试用例
    nums = [1, 3, 5, 7, 9, 11]
    seg_tree = SegmentTreeBasic(nums)
    
    print("初始数组: [1, 3, 5, 7, 9, 11]")
    print(f"查询区间[1,3]的和: {seg_tree.query(1, 3)}")  # 应该输出9 (1+3+5)
    print(f"查询区间[2,5]的和: {seg_tree.query(2, 5)}")  # 应该输出24 (3+5+7+9)
    
    # 区间更新：将区间[2,4]都加上2
    seg_tree.update(2, 4, 2)
    print("将区间[2,4]都加上2后:")
    print(f"查询区间[1,3]的和: {seg_tree.query(1, 3)}")  # 应该输出15 (1+5+9)
    print(f"查询区间[2,5]的和: {seg_tree.query(2, 5)}")  # 应该输出30 (5+9+11)
    
    # 单点更新：将位置3的值更新为10
    seg_tree.update_point(3, 10)
    print("将位置3的值更新为10后:")
    print(f"查询区间[1,3]的和: {seg_tree.query(1, 3)}")  # 应该输出16 (1+5+10)
    
    print("线段树测试完成！")


if __name__ == "__main__":
    test_segment_tree()