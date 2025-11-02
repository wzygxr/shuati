#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 391 Perfect Rectangle

题目描述：
给定 N 个矩形，判断它们是否能精确覆盖某个矩形区域。

解题思路：
要判断N个矩形是否能精确覆盖某个矩形区域，需要满足以下条件：
1. 所有小矩形的面积之和等于大矩形的面积
2. 大矩形的四个顶点只出现一次，其他顶点都出现偶数次（2次或4次）

我们可以使用扫描线算法或直接计算顶点出现次数来解决这个问题。

时间复杂度：O(n)
空间复杂度：O(n)
"""

class Solution:
    def is_rectangle_cover(self, rectangles):
        """
        判断矩形是否能精确覆盖某个矩形区域
        
        Args:
            rectangles: 矩形列表，每个元素为[x1, y1, x2, y2]
            
        Returns:
            是否能精确覆盖
        """
        if not rectangles:
            return False
        
        # 计算总面积和边界
        total_area = 0
        min_x, min_y = rectangles[0][0], rectangles[0][1]
        max_x, max_y = rectangles[0][2], rectangles[0][3]
        
        # 使用集合记录所有顶点的出现次数
        vertices = set()
        
        for rect in rectangles:
            x1, y1, x2, y2 = rect
            
            # 累加面积
            total_area += (x2 - x1) * (y2 - y1)
            
            # 更新边界
            min_x = min(min_x, x1)
            min_y = min(min_y, y1)
            max_x = max(max_x, x2)
            max_y = max(max_y, y2)
            
            # 记录四个顶点
            corners = [(x1, y1), (x1, y2), (x2, y1), (x2, y2)]
            for corner in corners:
                if corner in vertices:
                    vertices.remove(corner)
                else:
                    vertices.add(corner)
        
        # 检查面积是否匹配
        expected_area = (max_x - min_x) * (max_y - min_y)
        if total_area != expected_area:
            return False
        
        # 检查顶点出现次数
        # 大矩形的四个顶点应该只出现一次
        # 其他顶点应该出现偶数次
        expected_corners = {(min_x, min_y), (min_x, max_y), (max_x, min_y), (max_x, max_y)}
        
        return vertices == expected_corners


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1：能精确覆盖
    rectangles1 = [[1,1,3,3],[3,1,4,2],[3,2,4,4],[1,3,2,4],[2,3,3,4]]
    print("测试用例1:")
    print("矩形:", rectangles1)
    print("结果:", solution.is_rectangle_cover(rectangles1))
    print()
    
    # 测试用例2：不能精确覆盖（有重叠）
    rectangles2 = [[1,1,2,3],[1,3,2,4],[3,1,4,2],[3,2,4,4]]
    print("测试用例2:")
    print("矩形:", rectangles2)
    print("结果:", solution.is_rectangle_cover(rectangles2))
    print()
    
    # 测试用例3：不能精确覆盖（有空隙）
    rectangles3 = [[1,1,3,3],[3,1,4,2],[1,3,2,4]]
    print("测试用例3:")
    print("矩形:", rectangles3)
    print("结果:", solution.is_rectangle_cover(rectangles3))


if __name__ == "__main__":
    main()