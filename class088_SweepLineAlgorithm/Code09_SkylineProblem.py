"""
天际线问题 (LeetCode 218)
题目链接: https://leetcode.cn/problems/the-skyline-problem/

解题思路:
使用扫描线算法，将建筑物的左右边界作为事件点
维护当前扫描线位置的所有建筑物高度，使用最大堆
当高度发生变化时，记录关键点

时间复杂度: O(n log n) - 排序和堆操作
空间复杂度: O(n) - 存储事件点和堆
"""

import heapq
from typing import List

class SkylineProblem:
    def getSkyline(self, buildings: List[List[int]]) -> List[List[int]]:
        if not buildings:
            return []
        
        # 创建事件点: (x坐标, 高度, 类型: 1表示进入，-1表示离开)
        events = []
        for building in buildings:
            left, right, height = building
            events.append((left, -height, 1))   # 进入事件，使用负高度便于最大堆
            events.append((right, height, -1))  # 离开事件
        
        # 按x坐标排序，x相同时进入事件优先
        events.sort(key=lambda x: (x[0], -x[1] if x[2] == 1 else x[1]))
        
        # 使用最大堆（通过存储负高度实现）
        heap = [0]  # 地面高度
        removed = {}  # 记录已移除但仍在堆中的高度
        
        result = []
        prev_height = 0
        
        for x, height, event_type in events:
            if event_type == 1:
                # 进入事件：添加高度（存储负值）
                heapq.heappush(heap, height)
            else:
                # 离开事件：标记高度为已移除
                removed[height] = removed.get(height, 0) + 1
            
            # 清理堆顶已移除的高度
            while heap and heap[0] in removed and removed[heap[0]] > 0:
                removed[heap[0]] -= 1
                heapq.heappop(heap)
            
            # 获取当前最大高度（取负值）
            curr_height = -heap[0] if heap else 0
            
            # 如果高度发生变化，记录关键点
            if curr_height != prev_height:
                result.append([x, curr_height])
                prev_height = curr_height
        
        return result

# 测试代码
if __name__ == "__main__":
    solver = SkylineProblem()
    
    # 测试用例1: 简单建筑物
    buildings1 = [[2, 9, 10], [3, 7, 15], [5, 12, 12], [15, 20, 10], [19, 24, 8]]
    result1 = solver.getSkyline(buildings1)
    print("测试用例1结果:")
    for point in result1:
        print(f"[{point[0]}, {point[1]}]")
    
    # 测试用例2: 单个建筑物
    buildings2 = [[0, 2, 3], [2, 5, 3]]
    result2 = solver.getSkyline(buildings2)
    print("测试用例2结果:")
    for point in result2:
        print(f"[{point[0]}, {point[1]}]")
    
    # 测试用例3: 重叠建筑物
    buildings3 = [[1, 5, 10], [2, 6, 8], [3, 7, 12]]
    result3 = solver.getSkyline(buildings3)
    print("测试用例3结果:")
    for point in result3:
        print(f"[{point[0]}, {point[1]}]")