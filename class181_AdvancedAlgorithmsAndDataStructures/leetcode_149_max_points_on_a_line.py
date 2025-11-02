#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 149 Max Points on a Line

题目描述：
给你一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点。
求最多有多少个点在同一条直线上。

解题思路：
这是一个几何问题，可以使用斜率来判断点是否在同一条直线上。
对于每个点，我们计算它与其他所有点的斜率，斜率相同的点在同一条直线上。
为了避免浮点数精度问题，我们使用分数形式表示斜率，并将其化简为最简分数。

时间复杂度：O(n^2)
空间复杂度：O(n)
"""

from collections import defaultdict
from math import gcd

class Solution:
    def max_points_on_line(self, points):
        """
        计算最多有多少个点在同一条直线上
        
        Args:
            points: 点的坐标列表，每个元素为[x, y]
            
        Returns:
            最多在同一条直线上的点数
        """
        if len(points) <= 2:
            return len(points)
        
        max_points = 0
        
        # 对于每个点，计算它与其他点的斜率
        for i in range(len(points)):
            # 使用字典存储斜率及其对应的点数
            slope_count = defaultdict(int)
            duplicate = 1  # 重复点数（包括自身）
            vertical = 0   # 垂直线上的点数
            
            for j in range(i + 1, len(points)):
                # 检查是否为重复点
                if points[i][0] == points[j][0] and points[i][1] == points[j][1]:
                    duplicate += 1
                    continue
                
                # 检查是否为垂直线
                if points[i][0] == points[j][0]:
                    vertical += 1
                    continue
                
                # 计算斜率并化简为最简分数
                dy = points[j][1] - points[i][1]
                dx = points[j][0] - points[i][0]
                
                # 化简分数
                g = gcd(abs(dy), abs(dx))
                dy //= g
                dx //= g
                
                # 确保分母为正
                if dx < 0:
                    dy = -dy
                    dx = -dx
                
                # 统计相同斜率的点数
                slope_count[(dy, dx)] += 1
            
            # 更新最大点数
            current_max = vertical
            if slope_count:
                current_max = max(current_max, max(slope_count.values()))
            
            max_points = max(max_points, current_max + duplicate)
        
        return max_points


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    points1 = [[1,1],[2,2],[3,3]]
    print("测试用例1:")
    print("点集:", points1)
    print("结果:", solution.max_points_on_line(points1))
    print()
    
    # 测试用例2
    points2 = [[1,1],[3,2],[5,3],[4,1],[2,3],[1,4]]
    print("测试用例2:")
    print("点集:", points2)
    print("结果:", solution.max_points_on_line(points2))
    print()
    
    # 测试用例3
    points3 = [[0,0],[1,1],[0,0]]
    print("测试用例3:")
    print("点集:", points3)
    print("结果:", solution.max_points_on_line(points3))


if __name__ == "__main__":
    main()