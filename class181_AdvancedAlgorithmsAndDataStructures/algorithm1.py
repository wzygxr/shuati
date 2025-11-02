#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Python版本算法与数据结构实现
包含：
1. 平面分治 (Closest Pair of Points)
2. 棋盘模拟 (Game of Life)
3. 间隔打表 (Sparse Table)
4. 事件排序 (Time Sweep)
5. 差分驱动模拟 (Difference Array)
6. 双向循环链表 (Doubly Circular Linked List)
7. 斐波那契堆 (Fibonacci Heap)
8. 块状链表 (Unrolled Linked List)

时间复杂度分析：
- 平面分治: O(n log n)
- 棋盘模拟: O(m*n)
- 间隔打表: O(n log n) 预处理, O(1) 查询
- 事件排序: O(n log n)
- 差分驱动: O(1) 区间更新, O(n) 获取结果
- 双向循环链表: 插入/删除头部/尾部 O(1), 其他 O(n)
- 斐波那契堆: 插入 O(1) 均摊, 提取最小 O(log n) 均摊
- 块状链表: O(n/b) 操作复杂度，b为块大小
"""

import math
import heapq
from collections import namedtuple
import sys

# ================================
# 1. 平面分治 - 最近点对问题
# ================================

class Point:
    """点类，用于表示平面上的点"""
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def __repr__(self):
        return f"Point({self.x}, {self.y})"
    
    def distance_to(self, other):
        """计算到另一个点的欧几里得距离"""
        return math.sqrt((self.x - other.x) ** 2 + (self.y - other.y) ** 2)


def closest_pair(points):
    """
    使用分治法求解最近点对问题
    时间复杂度: O(n log n)
    
    参数:
        points: 点列表
    返回:
        (最小距离, 最近点对)
    """
    if not points:
        raise ValueError("输入点列表不能为空")
    if len(points) < 2:
        raise ValueError("至少需要两个点来计算距离")
    
    # 按x坐标排序
    points_sorted_by_x = sorted(points, key=lambda p: p.x)
    # 按y坐标排序，用于带内搜索
    points_sorted_by_y = sorted(points, key=lambda p: p.y)
    
    # 调用递归函数
    min_dist, pair = closest_pair_recursive(points_sorted_by_x, points_sorted_by_y)
    return min_dist, pair

def closest_pair_recursive(points_sorted_by_x, points_sorted_by_y):
    """递归求解最近点对"""
    n = len(points_sorted_by_x)
    
    # 基本情况：小于等于3个点，使用暴力法
    if n <= 3:
        return brute_force_closest_pair(points_sorted_by_x)
    
    # 分治：将点集分为左右两部分
    mid = n // 2
    mid_point = points_sorted_by_x[mid]
    
    # 分割y排序的点列表
    left_points_y = [p for p in points_sorted_by_y if p.x <= mid_point.x]
    right_points_y = [p for p in points_sorted_by_y if p.x > mid_point.x]
    
    # 递归求解左右两部分的最近点对
    left_min_dist, left_pair = closest_pair_recursive(points_sorted_by_x[:mid], left_points_y)
    right_min_dist, right_pair = closest_pair_recursive(points_sorted_by_x[mid:], right_points_y)
    
    # 合并：取左右两部分中的最小距离
    if left_min_dist <= right_min_dist:
        min_dist = left_min_dist
        min_pair = left_pair
    else:
        min_dist = right_min_dist
        min_pair = right_pair
    
    # 带内搜索：查找跨越中线的点对
    # 构建带内的点列表，只考虑x坐标在中线附近min_dist范围内的点
    strip = [p for p in points_sorted_by_y if abs(p.x - mid_point.x) < min_dist]
    
    # 在带内查找可能的更近点对
    strip_min_dist, strip_pair = strip_closest(strip, min_dist, min_pair)
    
    # 比较并返回全局最小距离
    if strip_min_dist < min_dist:
        return strip_min_dist, strip_pair
    else:
        return min_dist, min_pair

def brute_force_closest_pair(points):
    """暴力法计算最近点对，用于小规模输入"""
    n = len(points)
    min_dist = float('inf')
    min_pair = None
    
    for i in range(n):
        for j in range(i + 1, n):
            dist = points[i].distance_to(points[j])
            if dist < min_dist:
                min_dist = dist
                min_pair = (points[i], points[j])
    
    return min_dist, min_pair

def strip_closest(strip, min_dist, min_pair):
    """在带内查找最近点对"""
    # 按y坐标排序已经完成
    size = len(strip)
    
    # 对于带内的每个点，只需要检查后面y坐标相差不超过min_dist的点
    # 理论上最多检查6个点（平面分治的关键优化）
    for i in range(size):
        j = i + 1
        while j < size and (strip[j].y - strip[i].y) < min_dist:
            dist = strip[i].distance_to(strip[j])
            if dist < min_dist:
                min_dist = dist
                min_pair = (strip[i], strip[j])
            j += 1
    
    return min_dist, min_pair

# ================================
# 2. 棋盘模拟 - 生命游戏
# ================================

class GameOfLife:
    """
    康威生命游戏实现
    时间复杂度: O(m*n)，空间复杂度: 标准版O(m*n)，原地版O(1)
    """
    
    def __init__(self, board):
        """
        初始化生命游戏
        
        参数:
            board: 二维列表，表示初始棋盘状态
        """
        if not board or not board[0]:
            raise ValueError("输入棋盘不能为空")
        
        # 深拷贝输入棋盘，避免修改原始数据
        self.rows = len(board)
        self.cols = len(board[0])
        self.board = [row[:] for row in board]
    
    def count_neighbors(self, board, row, col):
        """
        计算指定位置的邻居数量
        
        参数:
            board: 当前棋盘状态
            row: 行索引
            col: 列索引
        返回:
            邻居数量
        """
        neighbors = 0
        # 八个方向的偏移
        directions = [
            (-1, -1), (-1, 0), (-1, 1),
            (0, -1),          (0, 1),
            (1, -1),  (1, 0), (1, 1)
        ]
        
        for dr, dc in directions:
            r, c = row + dr, col + dc
            # 检查边界并计数
            if 0 <= r < self.rows and 0 <= c < self.cols:
                # 注意：对于原地版本，我们需要考虑标记后的状态
                # 1和2表示原始状态为活细胞（1：保持活，2：将死亡）
                if board[r][c] == 1 or board[r][c] == 2:
                    neighbors += 1
        
        return neighbors
    
    def next_generation_standard(self):
        """
        使用额外空间计算下一代
        空间复杂度: O(m*n)
        
        返回:
            下一代棋盘状态
        """
        # 创建新棋盘存储下一代状态
        next_board = [[0 for _ in range(self.cols)] for _ in range(self.rows)]
        
        # 计算每个细胞的下一代状态
        for i in range(self.rows):
            for j in range(self.cols):
                neighbors = self.count_neighbors(self.board, i, j)
                
                # 应用生命游戏规则
                if self.board[i][j] == 1:  # 活细胞
                    if neighbors < 2 or neighbors > 3:
                        next_board[i][j] = 0  # 死亡：人口稀少或过度拥挤
                    else:
                        next_board[i][j] = 1  # 存活
                else:  # 死细胞
                    if neighbors == 3:
                        next_board[i][j] = 1  # 繁殖
        
        # 更新当前棋盘
        self.board = next_board
        return self.board
    
    def next_generation_inplace(self):
        """
        原地计算下一代，不使用额外空间
        空间复杂度: O(1)
        
        返回:
            更新后的棋盘（原地修改）
        """
        # 编码规则：
        # 0: 死细胞 -> 死细胞
        # 1: 活细胞 -> 活细胞
        # 2: 活细胞 -> 死细胞
        # 3: 死细胞 -> 活细胞
        
        for i in range(self.rows):
            for j in range(self.cols):
                neighbors = self.count_neighbors(self.board, i, j)
                
                if self.board[i][j] == 1:  # 活细胞
                    if neighbors < 2 or neighbors > 3:
                        self.board[i][j] = 2  # 标记为将死亡
                else:  # 死细胞
                    if neighbors == 3:
                        self.board[i][j] = 3  # 标记为将复活
        
        # 解码：将标记转换回0和1
        for i in range(self.rows):
            for j in range(self.cols):
                self.board[i][j] %= 2  # 2 -> 0, 3 -> 1
        
        return self.board
    
    def simulate(self, generations, inplace=True):
        """
        模拟多代生命游戏
        
        参数:
            generations: 要模拟的代数
            inplace: 是否使用原地算法
        返回:
            最终棋盘状态
        """
        if generations <= 0:
            return self.board
        
        for _ in range(generations):
            if inplace:
                self.next_generation_inplace()
            else:
                self.next_generation_standard()
        
        return self.board
    
    def get_board(self):
        """
        获取当前棋盘状态
        """
        # 返回深拷贝，避免外部修改
        return [row[:] for row in self.board]
    
    def print_board(self):
        """
        打印当前棋盘状态
        """
        for row in self.board:
            print(' '.join('█' if cell else ' ' for cell in row))
        print()

# ================================
# 3. 间隔打表 - 稀疏表
# ================================

class SparseTable:
    """
    稀疏表实现，用于高效的区间查询
    支持区间最小值查询和区间最大值查询
    时间复杂度：预处理O(n log n)，查询O(1)
    """
    
    def __init__(self, data, func=min):
        """
        初始化稀疏表
        
        参数:
            data: 一维数组数据
            func: 查询函数，可以是min或max
        """
        if not data:
            raise ValueError("输入数据不能为空")
        
        self.data = data
        self.n = len(data)
        self.func = func
        # 计算log2(n)的上界
        self.log_table = self._build_log_table()
        # 构建稀疏表
        self.st = self._build_sparse_table()
    
    def _build_log_table(self):
        """
        构建log表，用于快速计算区间长度对应的k值
        """
        log_table = [0] * (self.n + 1)
        for i in range(2, self.n + 1):
            log_table[i] = log_table[i // 2] + 1
        return log_table
    
    def _build_sparse_table(self):
        """
        构建稀疏表
        st[k][i]表示从i开始，长度为2^k的区间的查询结果
        """
        k_max = self.log_table[self.n] + 1
        # 创建k_max行n列的稀疏表
        st = [[0] * self.n for _ in range(k_max)]
        
        # 初始化k=0的情况（长度为1的区间）
        for i in range(self.n):
            st[0][i] = self.data[i]
        
        # 动态规划构建其他k值
        k = 1
        while (1 << k) <= self.n:
            i = 0
            while i + (1 << k) <= self.n:
                # 合并两个长度为2^(k-1)的区间
                st[k][i] = self.func(
                    st[k-1][i],
                    st[k-1][i + (1 << (k-1))]
                )
                i += 1
            k += 1
        
        return st
    
    def query_range(self, l, r):
        """
        查询区间[l, r]的结果（包含端点）
        时间复杂度: O(1)
        
        参数:
            l: 左边界（包含）
            r: 右边界（包含）
        返回:
            查询结果
        """
        # 检查边界
        if l < 0 or r >= self.n or l > r:
            raise ValueError(f"无效的区间边界: [{l}, {r}]")
        
        # 计算区间长度
        length = r - l + 1
        # 找到最大的k，使得2^k <= length
        k = self.log_table[length]
        
        # 查询两个重叠的子区间并合并结果
        # 第一个子区间: [l, l+2^k-1]
        # 第二个子区间: [r-2^k+1, r]
        return self.func(
            self.st[k][l],
            self.st[k][r - (1 << k) + 1]
        )
    
    def batch_query(self, queries):
        """
        批量处理多个区间查询
        
        参数:
            queries: 查询列表，每个查询是(l, r)元组
        返回:
            查询结果列表
        """
        results = []
        for l, r in queries:
            results.append(self.query_range(l, r))
        return results
    
    # 以下是一些常见的应用
    
    def is_range_all_same(self, l, r):
        """
        检查区间内所有元素是否相同
        """
        if l == r:
            return True
        return self.query_range(l, r) == self.data[l]  # 假设使用min查询
    
    def get_range_extreme(self, l, r):
        """
        获取区间的极值（最小值或最大值，取决于初始化时的func）
        """
        return self.query_range(l, r)

# ================================
# 4. 事件排序 - 扫描线算法
# ================================

class EventSweep:
    """
    扫描线算法实现，用于处理平面扫描问题
    """
    
    @staticmethod
    def interval_overlap(intervals):
        """
        计算区间重叠的最大数量
        时间复杂度: O(n log n)
        
        参数:
            intervals: 区间列表，每个区间是[start, end]的形式
        返回:
            最大重叠数量和发生重叠的区间
        """
        if not intervals:
            return 0, []
        
        # 创建事件点：(位置, 类型)，类型1表示开始，-1表示结束
        events = []
        for i, (start, end) in enumerate(intervals):
            if start > end:
                raise ValueError(f"无效的区间: [{start}, {end}]")
            events.append((start, 1, i))  # 开始事件
            events.append((end, -1, i))   # 结束事件
        
        # 按位置排序事件，当位置相同时，结束事件优先（确保[1,2]和[2,3]不被视为重叠）
        events.sort(key=lambda x: (x[0], x[1]))
        
        current_overlap = 0
        max_overlap = 0
        active_intervals = []
        max_overlap_intervals = []
        
        # 扫描事件
        for pos, typ, idx in events:
            # 更新当前重叠数量
            current_overlap += typ
            
            # 更新活动区间列表
            if typ == 1:
                active_intervals.append(idx)
            else:
                active_intervals.remove(idx)
            
            # 更新最大重叠
            if current_overlap > max_overlap:
                max_overlap = current_overlap
                max_overlap_intervals = active_intervals.copy()
            # 当重叠数量相同时，如果是结束事件，我们不更新，因为我们想要最大重叠开始的位置
        
        # 返回最大重叠数量和对应的原始区间
        overlapping_intervals = [intervals[i] for i in max_overlap_intervals]
        return max_overlap, overlapping_intervals
    
    @staticmethod
    def rectangle_area(rectangles):
        """
        计算多个矩形的总面积（去除重叠部分）
        时间复杂度: O(n^2 log n)
        
        参数:
            rectangles: 矩形列表，每个矩形是[x1, y1, x2, y2]，其中(x1,y1)是左下角，(x2,y2)是右上角
        返回:
            总面积
        """
        if not rectangles:
            return 0
        
        # 创建垂直边事件
        events = []
        x_coords = set()
        
        for x1, y1, x2, y2 in rectangles:
            if x1 >= x2 or y1 >= y2:
                raise ValueError(f"无效的矩形: [{x1}, {y1}, {x2}, {y2}]")
            # 添加垂直线事件
            events.append((x1, 'start', y1, y2))  # 左边界
            events.append((x2, 'end', y1, y2))    # 右边界
            x_coords.add(x1)
            x_coords.add(x2)
        
        # 按x坐标排序事件
        events.sort(key=lambda x: x[0])
        # 排序x坐标，用于计算相邻扫描线之间的距离
        sorted_x = sorted(x_coords)
        
        active_intervals = []
        area = 0
        prev_x = None
        
        # 扫描事件
        for x, typ, y1, y2 in events:
            # 计算当前扫描线和前一条扫描线之间的面积
            if prev_x is not None and x > prev_x and active_intervals:
                # 计算当前活动的y区间的总长度
                active_length = EventSweep._merge_and_calculate_length(active_intervals)
                # 面积 = 宽度 * 高度
                area += (x - prev_x) * active_length
            
            # 更新活动区间
            if typ == 'start':
                active_intervals.append((y1, y2))
            else:
                # 移除对应的区间
                active_intervals = [(a, b) for a, b in active_intervals if not (a == y1 and b == y2)]
            
            prev_x = x
        
        return area
    
    @staticmethod
    def _merge_and_calculate_length(intervals):
        """
        合并重叠的区间并计算总长度
        """
        if not intervals:
            return 0
        
        # 按起始位置排序
        sorted_intervals = sorted(intervals, key=lambda x: x[0])
        
        merged = [sorted_intervals[0]]
        for current_start, current_end in sorted_intervals[1:]:
            last_start, last_end = merged[-1]
            
            if current_start <= last_end:  # 有重叠
                # 合并区间
                new_start = last_start
                new_end = max(last_end, current_end)
                merged[-1] = (new_start, new_end)
            else:
                merged.append((current_start, current_end))
        
        # 计算总长度
        total_length = 0
        for start, end in merged:
            total_length += end - start
        
        return total_length
    
    @staticmethod
    def plane_sweep_for_intersections(segments):
        """
        使用扫描线算法检测线段相交
        注意：这是一个简化实现，仅用于演示
        
        参数:
            segments: 线段列表，每个线段是[(x1,y1), (x2,y2)]的形式
        返回:
            相交的线段对列表
        """
        if not segments:
            return []
        
        intersections = []
        events = []
        
        # 创建事件：线段的左右端点
        for i, ((x1, y1), (x2, y2)) in enumerate(segments):
            # 确保左端点的x坐标较小
            if x1 > x2:
                x1, x2 = x2, x1
                y1, y2 = y2, y1
            # 添加事件
            events.append((x1, 'start', i, y1, y2))  # 左端点
            events.append((x2, 'end', i, y1, y2))    # 右端点
        
        # 按x坐标排序事件
        events.sort(key=lambda x: x[0])
        
        active_segments = []
        
        for x, typ, idx, y1, y2 in events:
            if typ == 'start':
                # 检查与所有活动线段的相交
                for active_idx, active_y1, active_y2 in active_segments:
                    # 简化判断，实际应该使用线段相交算法
                    # 这里仅作为示例
                    if (min(y1, y2) <= max(active_y1, active_y2) and 
                        max(y1, y2) >= min(active_y1, active_y2)):
                        # 有潜在相交，记录
                        intersections.append((idx, active_idx))
                # 添加到活动线段
                active_segments.append((idx, y1, y2))
                # 按y坐标排序，用于处理垂直线段
                active_segments.sort(key=lambda s: min(s[1], s[2]))
            else:
                # 从活动线段中移除
                active_segments = [(i, ay1, ay2) for i, ay1, ay2 in active_segments if i != idx]
        
        return intersections

# ================================
# 5. 差分驱动模拟 - 差分数组
# ================================

class DifferenceArray:
    """
    差分数组实现，用于高效处理区间更新操作
    支持O(1)区间更新，O(n)获取结果数组
    """
    
    def __init__(self, size=None, initial_array=None):
        """
        初始化差分数组
        可以通过指定大小或初始数组来初始化
        
        参数:
            size: 数组大小
            initial_array: 初始数组
        """
        if initial_array is not None:
            self.size = len(initial_array)
            # 从初始数组构建差分数组
            self.diff = [0] * self.size
            self.diff[0] = initial_array[0]
            for i in range(1, self.size):
                self.diff[i] = initial_array[i] - initial_array[i-1]
        elif size is not None and size > 0:
            self.size = size
            self.diff = [0] * size  # 初始化为全0数组的差分数组
        else:
            raise ValueError("必须提供有效的size或initial_array")
    
    def range_add(self, l, r, val):
        """
        对区间[l, r]中的每个元素加上val
        时间复杂度: O(1)
        
        参数:
            l: 左边界（包含）
            r: 右边界（包含）
            val: 要添加的值
        """
        # 检查边界
        if l < 0 or r >= self.size or l > r:
            raise ValueError(f"无效的区间边界: [{l}, {r}]")
        
        # 在差分数组上进行标记
        self.diff[l] += val
        if r + 1 < self.size:
            self.diff[r + 1] -= val
    
    def get_result(self):
        """
        根据差分数组重构原始数组
        时间复杂度: O(n)
        
        返回:
            结果数组
        """
        res = [0] * self.size
        res[0] = self.diff[0]
        
        # 前缀和恢复原始数组
        for i in range(1, self.size):
            res[i] = res[i-1] + self.diff[i]
        
        return res
    
    def get_difference_array(self):
        """
        获取差分数组的副本
        
        返回:
            差分数组的副本
        """
        return self.diff.copy()
    
    def multiple_range_updates(self, updates):
        """
        批量执行多个区间更新操作
        
        参数:
            updates: 更新操作列表，每个操作是(l, r, val)的元组
        """
        for l, r, val in updates:
            self.range_add(l, r, val)
    
    def reset(self):
        """
        重置差分数组为全0
        """
        self.diff = [0] * self.size

# ================================
# 6. 双向循环链表
# ================================

class DoublyCircularLinkedListNode:
    """
    双向循环链表节点类
    """
    
    def __init__(self, data):
        self.data = data      # 节点数据
        self.prev = self      # 前驱节点，初始指向自己
        self.next = self      # 后继节点，初始指向自己


class DoublyCircularLinkedList:
    """
    双向循环链表实现
    支持高效的头部/尾部插入删除，以及任意位置的操作
    """
    
    def __init__(self):
        self.head = None  # 头节点指针
        self.size = 0     # 链表大小
    
    def is_empty(self):
        """
        检查链表是否为空
        """
        return self.head is None
    
    def get_size(self):
        """
        获取链表大小
        """
        return self.size
    
    def insert_at_head(self, data):
        """
        在链表头部插入元素
        时间复杂度: O(1)
        """
        new_node = DoublyCircularLinkedListNode(data)
        
        if self.is_empty():
            # 空链表情况
            self.head = new_node
        else:
            # 非空链表，插入到头部
            tail = self.head.prev
            
            # 连接新节点与尾节点
            new_node.prev = tail
            tail.next = new_node
            
            # 连接新节点与头节点
            new_node.next = self.head
            self.head.prev = new_node
            
            # 更新头节点
            self.head = new_node
        
        self.size += 1
    
    def insert_at_tail(self, data):
        """
        在链表尾部插入元素
        时间复杂度: O(1)
        """
        if self.is_empty():
            # 空链表情况，直接调用insert_at_head
            self.insert_at_head(data)
            return
        
        new_node = DoublyCircularLinkedListNode(data)
        tail = self.head.prev
        
        # 连接尾节点与新节点
        tail.next = new_node
        new_node.prev = tail
        
        # 连接新节点与头节点
        new_node.next = self.head
        self.head.prev = new_node
        
        self.size += 1
    
    def insert_at_position(self, index, data):
        """
        在指定位置插入元素
        时间复杂度: O(n)
        """
        if index < 0 or index > self.size:
            raise IndexError(f"插入位置无效: {index}")
        
        if index == 0:
            # 在头部插入
            self.insert_at_head(data)
            return
        
        if index == self.size:
            # 在尾部插入
            self.insert_at_tail(data)
            return
        
        # 找到插入位置的前一个节点
        # 优化：根据索引位置选择从头还是从尾开始遍历
        if index <= self.size // 2:
            # 从头开始遍历
            current = self.head
            for _ in range(index - 1):
                current = current.next
        else:
            # 从尾开始遍历
            current = self.head.prev
            for _ in range(self.size - index):
                current = current.prev
        
        # 创建新节点
        new_node = DoublyCircularLinkedListNode(data)
        next_node = current.next
        
        # 建立连接
        new_node.prev = current
        new_node.next = next_node
        current.next = new_node
        next_node.prev = new_node
        
        self.size += 1
    
    def delete_head(self):
        """
        删除链表头部元素
        时间复杂度: O(1)
        """
        if self.is_empty():
            raise IndexError("无法从空链表删除元素")
        
        data = self.head.data
        
        if self.size == 1:
            # 链表只有一个节点
            self.head = None
        else:
            # 链表有多个节点
            tail = self.head.prev
            new_head = self.head.next
            
            # 更新连接
            tail.next = new_head
            new_head.prev = tail
            
            # 更新头节点
            self.head = new_head
        
        self.size -= 1
        return data
    
    def delete_tail(self):
        """
        删除链表尾部元素
        时间复杂度: O(1)
        """
        if self.is_empty():
            raise IndexError("无法从空链表删除元素")
        
        if self.size == 1:
            # 链表只有一个节点，直接调用delete_head
            return self.delete_head()
        
        tail = self.head.prev
        data = tail.data
        
        # 更新连接
        new_tail = tail.prev
        new_tail.next = self.head
        self.head.prev = new_tail
        
        self.size -= 1
        return data
    
    def delete_at_position(self, index):
        """
        删除指定位置的元素
        时间复杂度: O(n)
        """
        if self.is_empty():
            raise IndexError("无法从空链表删除元素")
        
        if index < 0 or index >= self.size:
            raise IndexError(f"删除位置无效: {index}")
        
        if index == 0:
            return self.delete_head()
        
        if index == self.size - 1:
            return self.delete_tail()
        
        # 找到要删除的节点
        # 优化：根据索引位置选择从头还是从尾开始遍历
        if index <= self.size // 2:
            current = self.head
            for _ in range(index):
                current = current.next
        else:
            current = self.head.prev
            for _ in range(self.size - 1 - index):
                current = current.prev
        
        # 保存数据
        data = current.data
        
        # 更新连接
        prev_node = current.prev
        next_node = current.next
        prev_node.next = next_node
        next_node.prev = prev_node
        
        self.size -= 1
        return data
    
    def delete_by_value(self, value):
        """
        删除第一个出现的指定值的元素
        时间复杂度: O(n)
        """
        if self.is_empty():
            return False
        
        # 特殊情况：头节点就是要删除的节点
        if self.head.data == value:
            self.delete_head()
            return True
        
        # 遍历链表查找值
        current = self.head.next
        while current != self.head:
            if current.data == value:
                # 找到要删除的节点
                prev_node = current.prev
                next_node = current.next
                
                # 更新连接
                prev_node.next = next_node
                next_node.prev = prev_node
                
                self.size -= 1
                return True
            current = current.next
        
        # 未找到值
        return False
    
    def traverse_forward(self):
        """
        正向遍历链表
        时间复杂度: O(n)
        """
        result = []
        if self.is_empty():
            return result
        
        current = self.head
        while True:
            result.append(current.data)
            current = current.next
            if current == self.head:
                break
        
        return result
    
    def traverse_backward(self):
        """
        反向遍历链表
        时间复杂度: O(n)
        """
        result = []
        if self.is_empty():
            return result
        
        # 从尾节点开始
        current = self.head.prev
        while True:
            result.append(current.data)
            current = current.prev
            if current == self.head.prev:
                break
        
        return result
    
    def search(self, value):
        """
        查找第一个出现的指定值的索引
        时间复杂度: O(n)
        """
        if self.is_empty():
            return -1
        
        index = 0
        current = self.head
        while True:
            if current.data == value:
                return index
            current = current.next
            index += 1
            if current == self.head:
                break
        
        return -1
    
    def get(self, index):
        """
        获取指定索引的元素值
        时间复杂度: O(n)
        """
        if self.is_empty():
            raise IndexError("链表为空")
        
        if index < 0 or index >= self.size:
            raise IndexError(f"索引无效: {index}")
        
        # 优化：根据索引位置选择从头还是从尾开始遍历
        if index <= self.size // 2:
            current = self.head
            for _ in range(index):
                current = current.next
        else:
            current = self.head.prev
            for _ in range(self.size - 1 - index):
                current = current.prev
        
        return current.data
    
    def set(self, index, value):
        """
        设置指定索引的元素值
        时间复杂度: O(n)
        """
        if self.is_empty():
            raise IndexError("链表为空")
        
        if index < 0 or index >= self.size:
            raise IndexError(f"索引无效: {index}")
        
        # 优化：根据索引位置选择从头还是从尾开始遍历
        if index <= self.size // 2:
            current = self.head
            for _ in range(index):
                current = current.next
        else:
            current = self.head.prev
            for _ in range(self.size - 1 - index):
                current = current.prev
        
        old_value = current.data
        current.data = value
        return old_value
    
    def clear(self):
        """
        清空链表
        时间复杂度: O(n)
        """
        self.head = None
        self.size = 0
    
    def reverse(self):
        """
        反转链表
        时间复杂度: O(n)
        """
        if self.is_empty() or self.size == 1:
            return  # 空链表或只有一个节点不需要反转
        
        # 保存头节点和尾节点
        current = self.head
        tail = self.head.prev
        
        # 交换每个节点的prev和next指针
        while True:
            # 交换prev和next
            current.prev, current.next = current.next, current.prev
            
            # 移动到下一个节点（现在是prev指针）
            current = current.prev
            
            if current == self.head:
                break
        
        # 更新头节点为原来的尾节点
        self.head = tail
    
    def rotate(self, k):
        """
        旋转链表
        时间复杂度: O(n)
        
        参数:
            k: 旋转步数，正数表示向右旋转，负数表示向左旋转
        """
        if self.is_empty() or self.size == 1 or k % self.size == 0:
            return  # 无需旋转
        
        # 标准化k值，使其在[0, size-1]范围内
        k = k % self.size
        if k < 0:
            k += self.size  # 转换为正向旋转
        
        # 向右旋转k步相当于将倒数第k个节点作为新的头节点
        if k > 0:
            # 找到新的头节点（倒数第k个节点）
            new_head = self.head
            for _ in range(self.size - k):
                new_head = new_head.next
            
            # 更新头节点
            self.head = new_head
    
    def print_list(self):
        """
        打印链表内容
        时间复杂度: O(n)
        """
        if self.is_empty():
            print("链表为空")
            return
        
        elements = []
        current = self.head
        while True:
            elements.append(str(current.data))
            current = current.next
            if current == self.head:
                break
        
        print(" <-> ".join(elements) + " (循环)")