#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 973. K Closest Points to Origin 解决方案

题目链接: https://leetcode.com/problems/k-closest-points-to-origin/
题目描述: 给定一个点数组，返回离原点最近的k个点
解题思路: 可以使用平面分治算法，也可以使用堆或排序

时间复杂度: O(n log n) - 排序方法
空间复杂度: O(1) - 不考虑输出数组
"""

import math
import random
from typing import List

class Solution:
    def kClosest(self, points: List[List[int]], k: int) -> List[List[int]]:
        """
        使用排序方法找出离原点最近的k个点
        
        Args:
            points: 点数组
            k: 要返回的点数量
            
        Returns:
            离原点最近的k个点
        """
        # 检查输入有效性
        if not points or k <= 0:
            return []
        
        # 按照距离原点的距离排序
        points.sort(key=lambda p: math.sqrt(p[0]**2 + p[1]**2))
        
        # 返回前k个点
        return points[:min(k, len(points))]
    
    def kClosestDivideConquer(self, points: List[List[int]], k: int) -> List[List[int]]:
        """
        使用平面分治算法找出离原点最近的k个点
        
        Args:
            points: 点数组
            k: 要返回的点数量
            
        Returns:
            离原点最近的k个点
        """
        # 检查输入有效性
        if not points or k <= 0:
            return []
        
        # 转换为Point对象列表以便使用平面分治算法
        point_objects = [Point(p[0], p[1]) for p in points]
        
        # 按照x坐标排序
        points_sorted_by_x = sorted(point_objects, key=lambda p: p.x)
        
        # 按照y坐标排序
        points_sorted_by_y = sorted(point_objects, key=lambda p: p.y)
        
        # 找出最近的k个点
        closest_points = []
        self._find_k_closest_points(points_sorted_by_x, points_sorted_by_y, k, closest_points)
        
        # 转换回列表格式
        result = []
        for point in closest_points[:min(k, len(closest_points))]:
            result.append([int(point.x), int(point.y)])
        
        return result
    
    def _find_k_closest_points(self, points_sorted_by_x: List['Point'], 
                              points_sorted_by_y: List['Point'], 
                              k: int, result: List['Point']) -> None:
        """
        辅助方法：找出最近的k个点
        """
        # 对于这个特定问题，使用排序更简单
        # 这里只是为了演示平面分治算法的思想
        
        origin = Point(0, 0)
        points = points_sorted_by_x.copy()
        
        # 按照距离原点的距离排序
        points.sort(key=lambda p: p.distance_to(origin))
        
        # 添加前k个点到结果列表
        result.extend(points[:min(k, len(points))])
    
    @staticmethod
    def test_solution():
        """测试方法"""
        solution = Solution()
        
        # 测试用例1
        points1 = [[1, 1], [3, 3], [2, 2]]
        k1 = 1
        result1 = solution.kClosest(points1, k1)
        print("测试用例1 - 排序方法:")
        print(f"输入: points = {points1}, k = {k1}")
        print(f"输出: {result1}")
        
        # 测试用例2
        points2 = [[3, 3], [5, -1], [-2, 4]]
        k2 = 2
        result2 = solution.kClosest(points2, k2)
        print("\n测试用例2 - 排序方法:")
        print(f"输入: points = {points2}, k = {k2}")
        print(f"输出: {result2}")
        
        # 测试平面分治方法
        result3 = solution.kClosestDivideConquer(points1, k1)
        print("\n测试用例1 - 平面分治方法:")
        print(f"输入: points = {points1}, k = {k1}")
        print(f"输出: {result3}")

class Point:
    """点类，用于存储二维坐标"""
    
    def __init__(self, x: float, y: float):
        self.x = x
        self.y = y
    
    def distance_to(self, p: 'Point') -> float:
        """
        计算两个点之间的欧几里得距离
        """
        dx = self.x - p.x
        dy = self.y - p.y
        return math.sqrt(dx * dx + dy * dy)
    
    def __str__(self) -> str:
        return f"({self.x}, {self.y})"
    
    def __repr__(self) -> str:
        return f"Point({self.x}, {self.y})"

if __name__ == "__main__":
    Solution.test_solution()