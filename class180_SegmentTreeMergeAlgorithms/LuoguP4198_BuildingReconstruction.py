#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
洛谷 P4198 - 楼房重建
题目链接: https://www.luogu.com.cn/problem/P4198

题目描述:
有n栋楼房，第i栋楼房高度为h_i。从原点看这些楼房，能看到多少栋楼房？
楼房i能被看到当且仅当连接原点和楼房i顶点的线段与之前的所有楼房都不相交

线段树解法:
使用线段树维护区间最大斜率和可见楼房数量
时间复杂度: O(n log n)
空间复杂度: O(n)

工程化考量:
1. 浮点数精度处理
2. 边界条件处理
3. 性能优化
"""

import math
import sys

class SegmentTree:
    """线段树维护楼房可见性"""
    
    def __init__(self, n):
        """
        初始化线段树
        
        参数:
            n: 楼房数量
        """
        self.n = n
        self.size = 1
        while self.size < n:
            self.size *= 2
        
        # 存储区间最大斜率
        self.max_slope = [0.0] * (2 * self.size)
        # 存储区间可见楼房数量
        self.visible_count = [0] * (2 * self.size)
    
    def update(self, idx, height):
        """
        更新第idx栋楼房的高度
        
        参数:
            idx: 楼房索引(0-based)
            height: 新的高度
        """
        # 计算斜率: 高度/距离
        slope = height / (idx + 1)
        
        pos = self.size + idx
        self.max_slope[pos] = slope
        self.visible_count[pos] = 1
        
        pos //= 2
        while pos > 0:
            self._pull(pos)
            pos //= 2
    
    def _pull(self, i):
        """更新父节点信息"""
        left = 2 * i
        right = 2 * i + 1
        
        # 左子区间最大斜率
        left_max = self.max_slope[left]
        
        # 在右子区间中，只有斜率大于left_max的楼房才可见
        self.max_slope[i] = max(left_max, self.max_slope[right])
        self.visible_count[i] = self.visible_count[left] + self._count_visible(right, left_max)
    
    def _count_visible(self, i, threshold):
        """
        在节点i代表的区间中，统计斜率大于threshold的可见楼房数量
        
        参数:
            i: 节点索引
            threshold: 斜率阈值
        """
        if i >= self.size + self.n:
            return 0
        
        # 如果整个区间的最大斜率都不大于threshold，返回0
        if self.max_slope[i] <= threshold:
            return 0
        
        # 如果是叶子节点
        if i >= self.size:
            return 1
        
        left = 2 * i
        right = 2 * i + 1
        
        # 如果左子区间的最大斜率不大于threshold，只在右子区间查找
        if self.max_slope[left] <= threshold:
            return self._count_visible(right, threshold)
        else:
            # 左子区间有可见楼房，右子区间也需要查找
            return self.visible_count[left] - self.visible_count[i] + self._count_visible(right, self.max_slope[left])
    
    def query(self):
        """查询总共可见的楼房数量"""
        return self.visible_count[1]

def solve():
    """主解法函数"""
    # 读取输入
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    m = int(data[1])
    
    # 构建线段树
    seg_tree = SegmentTree(n)
    
    # 初始所有楼房高度为0
    heights = [0] * n
    
    results = []
    idx = 2
    for _ in range(m):
        x = int(data[idx]) - 1  # 转换为0-based
        y = int(data[idx+1])
        idx += 2
        
        # 更新楼房高度
        heights[x] = y
        seg_tree.update(x, y)
        
        # 查询可见楼房数量
        results.append(str(seg_tree.query()))
    
    # 输出结果
    print('\n'.join(results))

def test():
    """测试函数"""
    print("=== 洛谷 P4198 - 楼房重建 测试 ===")
    
    # 测试用例1: 3栋楼房，2次更新
    print("测试用例1: 3栋楼房")
    seg_tree = SegmentTree(3)
    
    # 初始状态
    print(f"初始可见楼房数量: {seg_tree.query()}")
    
    # 更新第一栋楼房高度为5
    seg_tree.update(0, 5)
    print(f"更新楼房1高度为5后可见数量: {seg_tree.query()}")
    
    # 更新第二栋楼房高度为3
    seg_tree.update(1, 3)
    print(f"更新楼房2高度为3后可见数量: {seg_tree.query()}")
    
    # 更新第三栋楼房高度为8
    seg_tree.update(2, 8)
    print(f"更新楼房3高度为8后可见数量: {seg_tree.query()}")
    
    # 测试用例2: 边界情况
    print("\n测试用例2: 边界情况")
    seg_tree2 = SegmentTree(1)
    seg_tree2.update(0, 10)
    print(f"单栋楼房高度10可见数量: {seg_tree2.query()}")
    
    # 测试用例3: 相同高度
    print("\n测试用例3: 相同高度")
    seg_tree3 = SegmentTree(2)
    seg_tree3.update(0, 5)
    seg_tree3.update(1, 5)
    print(f"两栋相同高度楼房可见数量: {seg_tree3.query()}")

if __name__ == "__main__":
    # 如果是直接运行，执行测试
    if len(sys.argv) > 1 and sys.argv[1] == "--test":
        test()
    else:
        solve()