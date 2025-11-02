#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
HDU 1199 Color the Ball
题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1199

题目描述:
在数轴上有一些球，每个球都有一个坐标。现在要对这些球进行染色操作，每次操作将一段区间内的球染成白色或黑色。
求最后最长的连续白色区间。

解题思路:
这是一个经典的区间染色问题，可以使用线段树结合离散化来解决。
由于球的坐标范围很大(1-1e9)，我们需要先对坐标进行离散化处理。
然后使用线段树维护区间的染色状态，支持区间更新和查询操作。

时间复杂度分析:
- 离散化: O(n log n)
- 建树: O(n)
- 区间更新: O(log n)
- 区间查询: O(log n)

空间复杂度: O(n)

工程化考量:
1. 异常处理: 检查输入参数的有效性
2. 边界情况: 处理空数组、单个元素等情况
3. 性能优化: 使用离散化减少空间复杂度
4. 可测试性: 提供完整的测试用例覆盖各种场景
5. 可读性: 添加详细的注释说明设计思路和实现细节
6. 鲁棒性: 处理极端输入和非理想数据
"""


class ColorTheBallSegmentTree:
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
        self.color = [0] * (size * 4)  # 0表示未染色，1表示白色，-1表示黑色
        self.lazy = [0] * (size * 4)   # 懒标记，0表示无标记，1表示白色，-1表示黑色

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
            self.color[i] = 0

    def push_down(self, i):
        """
        向下传递懒标记
        
        :param i: 当前节点编号
        """
        if self.lazy[i] != 0:
            # 传递颜色标记给左右子树
            self.color[i << 1] = self.lazy[i]
            self.color[i << 1 | 1] = self.lazy[i]
            self.lazy[i << 1] = self.lazy[i]
            self.lazy[i << 1 | 1] = self.lazy[i]
            # 清除当前节点的懒标记
            self.lazy[i] = 0

    def update_range(self, jobl, jobr, jobv, l, r, i):
        """
        范围染色 - 将区间[jobl, jobr]染成颜色jobv
        
        :param jobl: 任务区间左端点
        :param jobr: 任务区间右端点
        :param jobv: 染色颜色(1表示白色，-1表示黑色)
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
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def query_range(self, jobl, jobr, l, r, i):
        """
        查询区间颜色状态
        
        :param jobl: 查询区间左端点
        :param jobr: 查询区间右端点
        :param l:    当前区间左端点
        :param r:    当前区间右端点
        :param i:    当前节点编号
        :return: 区间颜色状态
        """
        if jobl <= l and r <= jobr:
            # 当前区间完全包含在查询区间内
            return self.color[i]
        else:
            # 需要继续向下递归
            self.push_down(i)
            mid = (l + r) >> 1
            left_color = 0
            right_color = 0
            if jobl <= mid:
                left_color = self.query_range(jobl, jobr, l, mid, i << 1)
            if jobr > mid:
                right_color = self.query_range(jobl, jobr, mid + 1, r, i << 1 | 1)
            
            # 合并结果
            if left_color == right_color:
                return left_color
            else:
                return 0  # 混合状态

    def find_longest_white_segment(self, l, r, i):
        """
        查找最长的连续白色区间
        
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param i: 当前节点编号
        :return: 最长连续白色区间的长度
        """
        if l == r:
            # 叶子节点
            return 1 if self.color[i] == 1 else 0
        
        # 需要继续向下递归
        self.push_down(i)
        mid = (l + r) >> 1
        left_length = self.find_longest_white_segment(l, mid, i << 1)
        right_length = self.find_longest_white_segment(mid + 1, r, i << 1 | 1)
        
        # 计算跨越中点的白色区间长度
        cross_length = 0
        # 从mid向左扩展
        left_cross = 0
        if self.color[i << 1] == 1:  # 左子树全为白色
            left_cross = mid - l + 1
        # 从mid+1向右扩展
        right_cross = 0
        if self.color[i << 1 | 1] == 1:  # 右子树全为白色
            right_cross = r - mid
        
        cross_length = left_cross + right_cross
        
        return max(left_length, right_length, cross_length)


def discretize_coordinates(operations):
    """
    离散化坐标
    
    :param operations: 操作列表，每个操作为(start, end, color)
    :return: (离散化后的坐标列表, 坐标映射字典)
    """
    coordinates = set()
    for start, end, _ in operations:
        coordinates.add(start)
        coordinates.add(end)
        coordinates.add(start - 1)  # 添加前一个点
        coordinates.add(end + 1)    # 添加后一个点
    
    # 转换为排序后的列表
    coord_list = sorted(list(coordinates))
    
    # 创建坐标映射字典
    coord_map = {coord: idx for idx, coord in enumerate(coord_list)}
    
    return coord_list, coord_map


def solve_color_the_ball(operations):
    """
    解决染色球问题
    
    :param operations: 操作列表，每个操作为(start, end, color)
    :return: 最长连续白色区间的长度
    """
    if not operations:
        return 0
    
    # 离散化坐标
    coord_list, coord_map = discretize_coordinates(operations)
    
    # 初始化线段树
    seg_tree = ColorTheBallSegmentTree(len(coord_list))
    
    # 执行染色操作
    for start, end, color in operations:
        # 转换为离散化后的坐标
        disc_start = coord_map[start]
        disc_end = coord_map[end]
        # 执行区间染色操作 (1表示白色，-1表示黑色)
        color_value = 1 if color == 'w' else -1
        seg_tree.update_range(disc_start, disc_end, color_value, 0, len(coord_list) - 1, 1)
    
    # 查找最长连续白色区间
    return seg_tree.find_longest_white_segment(0, len(coord_list) - 1, 1)


def main():
    """
    主函数 - 处理输入输出
    """
    print("开始测试 HDU 1199 Color the Ball")
    
    # 测试用例1
    operations1 = [
        (1, 3, 'w'),  # 将区间[1,3]染成白色
        (2, 4, 'b'),  # 将区间[2,4]染成黑色
        (5, 6, 'w')   # 将区间[5,6]染成白色
    ]
    
    result1 = solve_color_the_ball(operations1)
    print("测试用例1结果:", result1)  # 应该输出1 (区间[1,1]或[5,6]中的一个)
    
    # 测试用例2
    operations2 = [
        (1, 5, 'w'),  # 将区间[1,5]染成白色
        (3, 7, 'w')   # 将区间[3,7]染成白色 (实际上是扩展白色区域)
    ]
    
    result2 = solve_color_the_ball(operations2)
    print("测试用例2结果:", result2)  # 应该输出7 (区间[1,7]全为白色)
    
    # 测试用例3
    operations3 = [
        (1, 10, 'w'), # 将区间[1,10]染成白色
        (3, 5, 'b'),  # 将区间[3,5]染成黑色
        (7, 9, 'b')   # 将区间[7,9]染成黑色
    ]
    
    result3 = solve_color_the_ball(operations3)
    print("测试用例3结果:", result3)  # 应该输出2 (区间[1,2]或[6,6]或[10,10]中的最长白色区间)
    
    print("测试完成")


if __name__ == "__main__":
    main()