#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 612. 平面上的最短距离 (Python版本)

问题描述：
给定一个平面上的点集，找到其中距离最近的两个点之间的距离。

算法思路：
使用平面分治算法（Closest Pair of Points）解决最近点对问题。
1. 将点集按照x坐标排序
2. 递归地将点集分为左右两部分
3. 分别计算左右两部分的最近点对距离
4. 计算跨越分割线的最近点对距离
5. 返回三者中的最小值

时间复杂度：O(n log n)
空间复杂度：O(n)

应用场景：
1. 地理信息系统：最近设施查询
2. 计算机图形学：碰撞检测
3. 机器学习：最近邻搜索

相关题目：
1. LeetCode 973. 最接近原点的K个点
2. LeetCode 719. 找出第 k 小的距离对
3. LeetCode 149. 直线上最多的点数
"""

import math
import random
import time
from typing import List, Tuple
from dataclasses import dataclass


@dataclass
class Point:
    """点类，用于存储二维坐标"""
    x: float
    y: float
    
    def distance_to(self, other: 'Point') -> float:
        """计算两个点之间的欧几里得距离"""
        dx = self.x - other.x
        dy = self.y - other.y
        return math.sqrt(dx * dx + dy * dy)
    
    def __str__(self) -> str:
        return f"({self.x}, {self.y})"
    
    def __repr__(self) -> str:
        return self.__str__()


class ClosestPairSolver:
    """平面分治算法类"""
    
    @staticmethod
    def shortest_distance_brute_force(points: List[Point]) -> float:
        """
        暴力解法：计算所有点对的距离
        时间复杂度：O(n²)
        空间复杂度：O(1)
        """
        if len(points) < 2:
            raise ValueError("点集必须包含至少两个点")
        
        min_distance = float('inf')
        
        for i in range(len(points)):
            for j in range(i + 1, len(points)):
                distance = points[i].distance_to(points[j])
                if distance < min_distance:
                    min_distance = distance
        
        return min_distance
    
    @staticmethod
    def shortest_distance_divide_conquer(points: List[Point]) -> float:
        """
        平面分治算法：高效解决最近点对问题
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        """
        if len(points) < 2:
            raise ValueError("点集必须包含至少两个点")
        
        # 按照x坐标排序
        points_sorted_by_x = sorted(points, key=lambda p: p.x)
        
        # 按照y坐标排序（用于后续处理）
        points_sorted_by_y = sorted(points_sorted_by_x, key=lambda p: p.y)
        
        return ClosestPairSolver._closest_pair_recursive(
            points_sorted_by_x, 0, len(points_sorted_by_x) - 1, points_sorted_by_y
        )
    
    @staticmethod
    def _closest_pair_recursive(points_sorted_by_x: List[Point], 
                              left: int, right: int, 
                              points_sorted_by_y: List[Point]) -> float:
        """
        递归求解最近点对
        """
        # 基本情况：小规模问题直接暴力求解
        if right - left <= 3:
            return ClosestPairSolver._brute_force(points_sorted_by_x, left, right)
        
        # 分治求解
        mid = left + (right - left) // 2
        mid_point = points_sorted_by_x[mid]
        
        # 分割y排序的数组
        left_points_sorted_by_y = []
        right_points_sorted_by_y = []
        
        for point in points_sorted_by_y:
            if point.x <= mid_point.x:
                left_points_sorted_by_y.append(point)
            else:
                right_points_sorted_by_y.append(point)
        
        # 递归求解左右两部分的最近距离
        left_min = ClosestPairSolver._closest_pair_recursive(
            points_sorted_by_x, left, mid, left_points_sorted_by_y
        )
        right_min = ClosestPairSolver._closest_pair_recursive(
            points_sorted_by_x, mid + 1, right, right_points_sorted_by_y
        )
        min_distance = min(left_min, right_min)
        
        # 检查跨越分割线的点对
        strip = []
        for point in points_sorted_by_y:
            if abs(point.x - mid_point.x) < min_distance:
                strip.append(point)
        
        # 在strip中检查最近点对
        for i in range(len(strip)):
            # 只需要检查后面的7个点（理论证明最多需要检查7个点）
            j = i + 1
            while j < len(strip) and (strip[j].y - strip[i].y) < min_distance:
                distance = strip[i].distance_to(strip[j])
                if distance < min_distance:
                    min_distance = distance
                j += 1
        
        return min_distance
    
    @staticmethod
    def _brute_force(points: List[Point], left: int, right: int) -> float:
        """
        暴力求解小规模点集的最近距离
        """
        min_distance = float('inf')
        
        for i in range(left, right + 1):
            for j in range(i + 1, right + 1):
                distance = points[i].distance_to(points[j])
                if distance < min_distance:
                    min_distance = distance
        
        return min_distance


def test_closest_pair():
    """测试函数"""
    print("=== 测试 LeetCode 612. 平面上的最短距离 (Python版本) ===")
    
    # 测试用例1：简单点集
    points1 = [
        Point(1, 1),
        Point(2, 2),
        Point(3, 3),
        Point(4, 4)
    ]
    
    print("测试用例1:")
    print(f"点集: {points1}")
    print(f"暴力解法结果: {ClosestPairSolver.shortest_distance_brute_force(points1)}")
    print(f"分治算法结果: {ClosestPairSolver.shortest_distance_divide_conquer(points1)}")
    
    # 测试用例2：随机点集
    random.seed(42)
    points2 = [Point(random.uniform(0, 100), random.uniform(0, 100)) for _ in range(10)]
    
    print("\n测试用例2:")
    print(f"随机点集大小: {len(points2)}")
    print(f"暴力解法结果: {ClosestPairSolver.shortest_distance_brute_force(points2)}")
    print(f"分治算法结果: {ClosestPairSolver.shortest_distance_divide_conquer(points2)}")
    
    # 测试用例3：边界情况（两个点）
    points3 = [
        Point(0, 0),
        Point(3, 4)
    ]
    
    print("\n测试用例3:")
    print(f"点集: {points3}")
    print(f"暴力解法结果: {ClosestPairSolver.shortest_distance_brute_force(points3)}")
    print(f"分治算法结果: {ClosestPairSolver.shortest_distance_divide_conquer(points3)}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    
    # 小规模测试
    small_points = [Point(random.uniform(0, 1000), random.uniform(0, 1000)) for _ in range(100)]
    
    start_time = time.time()
    brute_result = ClosestPairSolver.shortest_distance_brute_force(small_points)
    brute_time = time.time() - start_time
    
    start_time = time.time()
    divide_result = ClosestPairSolver.shortest_distance_divide_conquer(small_points)
    divide_time = time.time() - start_time
    
    print("100个点:")
    print(f"暴力解法时间: {brute_time * 1000:.2f} ms, 结果: {brute_result}")
    print(f"分治算法时间: {divide_time * 1000:.2f} ms, 结果: {divide_result}")
    
    # 大规模测试
    large_points = [Point(random.uniform(0, 10000), random.uniform(0, 10000)) for _ in range(10000)]
    
    start_time = time.time()
    divide_result = ClosestPairSolver.shortest_distance_divide_conquer(large_points)
    divide_time = time.time() - start_time
    
    print("\n10000个点:")
    print(f"分治算法时间: {divide_time * 1000:.2f} ms, 结果: {divide_result}")
    
    # 验证算法正确性
    print("\n=== 算法正确性验证 ===")
    
    # 创建已知最小距离的点集
    known_points = [
        Point(0, 0),
        Point(1, 1),
        Point(3, 3),
        Point(0.5, 0.5)  # 这个点距离(0,0)和(1,1)都很近
    ]
    
    expected_min = math.sqrt(0.5)  # (0,0)到(0.5,0.5)的距离
    actual_min = ClosestPairSolver.shortest_distance_divide_conquer(known_points)
    
    print(f"预期最小距离: {expected_min}")
    print(f"实际最小距离: {actual_min}")
    print(f"算法正确性: {abs(expected_min - actual_min) < 1e-10}")
    
    # 工程化考量：异常处理测试
    print("\n=== 异常处理测试 ===")
    
    try:
        ClosestPairSolver.shortest_distance_divide_conquer([])
        print("空数组测试: 失败（应该抛出异常）")
    except ValueError as e:
        print(f"空数组测试: 通过（正确抛出异常: {e}")
    
    try:
        ClosestPairSolver.shortest_distance_divide_conquer([Point(1, 1)])
        print("单点数组测试: 失败（应该抛出异常）")
    except ValueError as e:
        print(f"单点数组测试: 通过（正确抛出异常: {e}")
    
    # Python特有考量：浮点数精度处理
    print("\n=== 浮点数精度考量 ===")
    
    # 测试浮点数精度问题
    precision_points = [
        Point(0.0000001, 0.0000001),
        Point(0.0000002, 0.0000002),
        Point(0.0000003, 0.0000003)
    ]
    
    precision_result = ClosestPairSolver.shortest_distance_divide_conquer(precision_points)
    print(f"高精度点集最小距离: {precision_result}")
    print("Python使用双精度浮点数，精度足够处理大多数应用场景")
    
    # 内存使用考量
    print("\n=== 内存使用考量 ===")
    print("分治算法在递归过程中会创建临时列表，但Python的垃圾回收机制会自动管理内存")
    print("对于超大规模数据，可以考虑使用迭代版本减少递归深度")


if __name__ == "__main__":
    test_closest_pair()