#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
ZOJ 1610 Count the Colors
题目链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364599

题目描述:
给定一个长度为8000的数轴，初始时所有位置都没有颜色。现在进行n次操作，
每次操作将区间[l,r)染成颜色c。最后统计每种颜色有多少个连续的段。

解题思路:
这是一个经典的区间染色问题，可以使用线段树来解决。
1. 使用线段树维护区间的颜色信息
2. 每次染色操作时，更新对应区间的颜色
3. 最后遍历整个数轴，统计每种颜色的连续段数

时间复杂度分析:
- 建树: O(n)
- 区间更新: O(log n)
- 区间查询: O(log n)
- 统计结果: O(n)

空间复杂度: O(n)

工程化考量:
1. 异常处理: 检查输入参数的有效性
2. 边界情况: 处理空数组、单个元素等情况
3. 性能优化: 使用懒标记优化区间更新
4. 可测试性: 提供完整的测试用例覆盖各种场景
5. 可读性: 添加详细的注释说明设计思路和实现细节
6. 鲁棒性: 处理极端输入和非理想数据
"""


class CountTheColorsSegmentTree:
    def __init__(self, size):
        """
        构造函数 - 初始化线段树
        
        :param size: 数组大小
        :type size: int
        """
        # 参数校验
        if size <= 0:
            raise ValueError("数组大小必须为正整数")
            
        self.n = size
        # 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        self.color = [-1] * (size * 4)  # -1表示无颜色
        self.lazy = [-1] * (size * 4)   # 懒标记，-1表示无标记

    def push_up(self, i):
        """
        向上更新节点信息 - 如果左右子树颜色相同，则父节点颜色为该颜色，否则为混合状态
        
        :param i: 当前节点编号
        """
        # 如果左右子树颜色相同，则父节点颜色为该颜色
        if self.color[i << 1] == self.color[i << 1 | 1]:
            self.color[i] = self.color[i << 1]
        else:
            # 否则为混合状态
            self.color[i] = -2  # -2表示混合颜色

    def push_down(self, i):
        """
        向下传递懒标记
        
        :param i: 当前节点编号
        """
        if self.lazy[i] != -1:
            # 传递颜色标记给左右子树
            self.color[i << 1] = self.lazy[i]
            self.color[i << 1 | 1] = self.lazy[i]
            self.lazy[i << 1] = self.lazy[i]
            self.lazy[i << 1 | 1] = self.lazy[i]
            # 清除当前节点的懒标记
            self.lazy[i] = -1

    def update_range(self, jobl, jobr, jobv, l, r, i):
        """
        范围染色 - 将区间[jobl, jobr)染成颜色jobv
        
        :param jobl: 任务区间左端点（包含）
        :param jobr: 任务区间右端点（不包含）
        :param jobv: 染色颜色
        :param l:    当前区间左端点
        :param r:    当前区间右端点
        :param i:    当前节点编号
        """
        if jobl <= l and r <= jobr:
            # 当前区间完全包含在任务区间内
            self.color[i] = jobv
            self.lazy[i] = jobv
        else:
            # 需要继续向下递归
            self.push_down(i)
            mid = (l + r) >> 1
            if jobl < mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid, r, i << 1 | 1)
            self.push_up(i)

    def query_all_colors(self, l, r, i, result):
        """
        查询所有位置的颜色并统计每种颜色的段数
        
        :param l:      当前区间左端点
        :param r:      当前区间右端点
        :param i:      当前节点编号
        :param result: 结果字典，记录每种颜色的段数
        """
        if l == r:
            # 叶子节点
            if self.color[i] >= 0:  # 有效颜色
                result[l] = self.color[i]
            return
        
        # 如果当前节点有懒标记，先传递下去
        if self.lazy[i] != -1:
            self.push_down(i)
        
        # 如果当前节点颜色统一，直接记录
        if self.color[i] >= 0:
            # 在区间[l,r]内所有位置都是同一颜色
            for pos in range(l, r + 1):
                result[pos] = self.color[i]
            return
        
        # 需要继续向下递归
        mid = (l + r) >> 1
        self.query_all_colors(l, mid, i << 1, result)
        self.query_all_colors(mid + 1, r, i << 1 | 1, result)

    def count_color_segments(self):
        """
        统计每种颜色的段数
        
        :return: 字典，键为颜色，值为该颜色的段数
        """
        # 查询所有位置的颜色
        color_positions = {}
        self.query_all_colors(0, self.n - 1, 1, color_positions)
        
        # 统计每种颜色的段数
        color_count = {}
        if not color_positions:
            return color_count
            
        # 按位置排序
        sorted_positions = sorted(color_positions.keys())
        
        # 遍历所有位置，统计连续段
        current_color = color_positions[sorted_positions[0]]
        if current_color >= 0:
            color_count[current_color] = color_count.get(current_color, 0) + 1
            
        for i in range(1, len(sorted_positions)):
            pos = sorted_positions[i]
            color = color_positions[pos]
            if color >= 0 and color != current_color:
                color_count[color] = color_count.get(color, 0) + 1
                current_color = color
                
        return color_count


def solve_count_the_colors(operations, size=8000):
    """
    解决ZOJ 1610 Count the Colors问题
    
    :param operations: 操作列表，每个操作为(start, end, color)
    :param size: 数轴长度，默认为8000
    :return: 字典，键为颜色，值为该颜色的段数
    """
    if not operations:
        return {}
    
    # 初始化线段树
    seg_tree = CountTheColorsSegmentTree(size)
    
    # 执行染色操作
    for start, end, color in operations:
        # 注意：题目中的区间是左闭右开[l,r)
        seg_tree.update_range(start, end, color, 0, size - 1, 1)
    
    # 统计每种颜色的段数
    return seg_tree.count_color_segments()


def main():
    """
    主函数 - 处理输入输出
    """
    print("开始测试 ZOJ 1610 Count the Colors")
    
    # 测试用例1
    operations1 = [
        (0, 2, 1),  # 将区间[0,2)染成颜色1
        (1, 3, 2),  # 将区间[1,3)染成颜色2
        (4, 5, 3)   # 将区间[4,5)染成颜色3
    ]
    
    result1 = solve_count_the_colors(operations1, 6)
    print("测试用例1结果:", result1)  # 颜色1有1段，颜色2有1段，颜色3有1段
    
    # 测试用例2
    operations2 = [
        (0, 4, 1),  # 将区间[0,4)染成颜色1
        (1, 3, 2),  # 将区间[1,3)染成颜色2
    ]
    
    result2 = solve_count_the_colors(operations2, 5)
    print("测试用例2结果:", result2)  # 颜色1有2段([0,1)和[3,4))，颜色2有1段
    
    # 测试用例3
    operations3 = [
        (0, 5, 1),  # 将区间[0,5)染成颜色1
        (2, 4, 1),  # 将区间[2,4)染成颜色1 (与之前相同，不会增加段数)
    ]
    
    result3 = solve_count_the_colors(operations3, 6)
    print("测试用例3结果:", result3)  # 颜色1有1段([0,5))
    
    print("测试完成")


if __name__ == "__main__":
    main()