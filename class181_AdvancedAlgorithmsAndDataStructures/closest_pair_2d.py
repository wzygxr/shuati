#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
二维平面最近点对问题实现 (Python版本)

问题描述：
给定平面上的n个点，找出距离最近的两个点。

算法思路：
使用分治法解决最近点对问题：
1. 将点集按照x坐标排序
2. 递归地在左半部分和右半部分找最近点对
3. 找到跨越中线的最近点对
4. 返回三者中的最小值

时间复杂度：O(n log n)
空间复杂度：O(n)

应用场景：
1. 计算几何中的碰撞检测
2. 机器学习中的最近邻搜索
3. 地理信息系统中的最近设施查询

相关题目：
1. LeetCode 973. 最接近原点的K个点
2. POJ 3714 Raid
3. HDU 1007 Quoit Design
"""

import math
import random
import time

class Point:
    """点类，用于存储二维坐标"""
    
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def distance_to(self, p):
        """计算两个点之间的欧几里得距离"""
        dx = self.x - p.x
        dy = self.y - p.y
        return math.sqrt(dx * dx + dy * dy)
    
    def __str__(self):
        return f"({self.x}, {self.y})"
    
    def __repr__(self):
        return f"Point({self.x}, {self.y})"

class ClosestPairResult:
    """最近点对结果类"""
    
    def __init__(self, p1=None, p2=None, distance=float('inf')):
        self.p1 = p1
        self.p2 = p2
        self.distance = distance

class ClosestPair2D:
    """最近点对算法实现类"""
    
    @staticmethod
    def find_closest_pair(points):
        """
        查找最近点对
        :param points: 点集
        :return: 最近点对结果
        """
        if not points or len(points) < 2:
            raise ValueError("点集必须包含至少两个点")
        
        # 按照x坐标排序
        points_sorted_by_x = sorted(points, key=lambda p: p.x)
        
        # 按照y坐标排序
        points_sorted_by_y = sorted(points, key=lambda p: p.y)
        
        # 调用递归函数
        return ClosestPair2D._closest_pair_recursive(points_sorted_by_x, points_sorted_by_y)
    
    @staticmethod
    def _closest_pair_recursive(points_sorted_by_x, points_sorted_by_y):
        """
        递归求解最近点对
        :param points_sorted_by_x: 按x坐标排序的点集
        :param points_sorted_by_y: 按y坐标排序的点集
        :return: 最近点对结果
        """
        n = len(points_sorted_by_x)
        
        # 基本情况：小规模问题直接暴力求解
        if n <= 3:
            return ClosestPair2D._brute_force(points_sorted_by_x)
        
        # 分治求解
        mid = n // 2
        mid_point = points_sorted_by_x[mid]
        
        # 分割点集
        left_points_x = points_sorted_by_x[:mid]
        right_points_x = points_sorted_by_x[mid:]
        
        # 分割y排序的数组
        left_points_y = []
        right_points_y = []
        for p in points_sorted_by_y:
            if p.x <= mid_point.x:
                left_points_y.append(p)
            else:
                right_points_y.append(p)
        
        # 递归求解左右子数组
        left_result = ClosestPair2D._closest_pair_recursive(left_points_x, left_points_y)
        right_result = ClosestPair2D._closest_pair_recursive(right_points_x, right_points_y)
        
        # 确定左右子数组中的最小距离
        min_result = left_result if left_result.distance < right_result.distance else right_result
        
        # 处理跨越中线的点对
        # 筛选出在中线附近的点
        strip = []
        for p in points_sorted_by_y:
            if abs(p.x - mid_point.x) < min_result.distance:
                strip.append(p)
        
        # 检查strip中的点对
        strip_result = ClosestPair2D._check_strip(strip, min_result.distance)
        
        # 返回最小距离的点对
        if strip_result.distance < min_result.distance:
            return strip_result
        else:
            return min_result
    
    @staticmethod
    def _brute_force(points):
        """
        暴力求解小规模问题
        :param points: 点集
        :return: 最近点对结果
        """
        min_dist = float('inf')
        p1 = None
        p2 = None
        
        for i in range(len(points)):
            for j in range(i + 1, len(points)):
                dist = points[i].distance_to(points[j])
                if dist < min_dist:
                    min_dist = dist
                    p1 = points[i]
                    p2 = points[j]
        
        return ClosestPairResult(p1, p2, min_dist)
    
    @staticmethod
    def _check_strip(strip, min_dist):
        """
        检查跨越中线的点对
        :param strip: 中线附近的点集
        :param min_dist: 当前最小距离
        :return: 最近点对结果
        """
        current_min = min_dist
        p1 = None
        p2 = None
        
        # 按照y坐标排序（已经是排序好的）
        # 只需要检查相邻的最多7个点
        for i in range(len(strip)):
            j = i + 1
            while j < len(strip) and (strip[j].y - strip[i].y) < current_min:
                dist = strip[i].distance_to(strip[j])
                if dist < current_min:
                    current_min = dist
                    p1 = strip[i]
                    p2 = strip[j]
                j += 1
        
        # 如果没有找到更近的点对，返回一个无效结果
        if p1 is None:
            return ClosestPairResult(None, None, float('inf'))
        
        return ClosestPairResult(p1, p2, current_min)
    
    @staticmethod
    def test_closest_pair():
        """测试最近点对算法"""
        print("=== 测试最近点对算法 ===")
        
        # 测试用例1：随机点集
        points1 = [
            Point(2, 3),
            Point(12, 30),
            Point(40, 50),
            Point(5, 1),
            Point(12, 10),
            Point(3, 4)
        ]
        
        result1 = ClosestPair2D.find_closest_pair(points1)
        print(f"最近点对1: {result1.p1} 和 {result1.p2}")
        print(f"距离: {result1.distance}")
        
        # 测试用例2：所有点在一条直线上
        points2 = [
            Point(0, 0),
            Point(1, 0),
            Point(2, 0),
            Point(3, 0),
            Point(100, 0)
        ]
        
        result2 = ClosestPair2D.find_closest_pair(points2)
        print(f"最近点对2: {result2.p1} 和 {result2.p2}")
        print(f"距离: {result2.distance}")
        
        # 测试用例3：边界情况
        points3 = [
            Point(0, 0),
            Point(0, 0)  # 重复点
        ]
        
        result3 = ClosestPair2D.find_closest_pair(points3)
        print(f"最近点对3: {result3.p1} 和 {result3.p2}")
        print(f"距离: {result3.distance}")
        
        # 性能测试
        print("\n=== 性能测试 ===")
        random.seed(42)  # 固定种子以确保可重复性
        n = 10000
        points4 = [Point(random.uniform(0, 1000), random.uniform(0, 1000)) for _ in range(n)]
        
        start_time = time.time()
        result4 = ClosestPair2D.find_closest_pair(points4)
        end_time = time.time()
        
        print(f"10000个随机点的最近点对:")
        print(f"最近点对: {result4.p1} 和 {result4.p2}")
        print(f"距离: {result4.distance}")
        print(f"运行时间: {(end_time - start_time) * 1000:.2f} ms")

if __name__ == "__main__":
    ClosestPair2D.test_closest_pair()