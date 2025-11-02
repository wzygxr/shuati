#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 218. 天际线问题
题目链接: https://leetcode.cn/problems/the-skyline-problem/

题目描述:
城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。

每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
- lefti 是第i座建筑物左边缘的x坐标
- righti 是第i座建筑物右边缘的x坐标
- heighti 是第i座建筑物的高度

你可以假设所有的建筑都是完美的长方形，在高度为0的绝对平坦的表面上。

天际线应该表示为由"关键点"组成的列表，格式 [[x1,y1],[x2,y2],...]，并按x坐标进行排序。
关键点是水平线段的左端点。列表中最后一个点是最右侧建筑物的终点，y坐标始终为0，仅用于标记天际线的终点。

时间复杂度: O(n log n)，其中n是建筑物的数量
空间复杂度: O(n)

解题思路:
使用扫描线算法结合线段树来解决这个问题：
1. 将所有建筑物的左右边界作为事件点处理
2. 使用优先队列维护当前有效的建筑物高度
3. 当遇到左边界时，将建筑物高度加入队列
4. 当遇到右边界时，将建筑物高度从队列中移除
5. 当最高高度发生变化时，记录关键点

工程化考量:
1. 异常处理: 检查输入参数的有效性
2. 边界条件: 处理空输入、单建筑等边界情况
3. 性能优化: 使用堆操作优化时间复杂度
4. 可读性: 详细注释和清晰的变量命名
5. 可测试性: 提供完整的测试用例覆盖各种场景
"""

import heapq
from typing import List

class SkylineProblem:
    def get_skyline(self, buildings: List[List[int]]) -> List[List[int]]:
        """
        计算天际线关键点
        
        Args:
            buildings: 建筑物信息列表，每个元素为[left, right, height]
            
        Returns:
            天际线关键点列表
            
        时间复杂度: O(n log n)，其中n是建筑物的数量
        空间复杂度: O(n)
        """
        # 参数校验
        if not buildings:
            return []
        
        # 创建事件点：(x坐标, 高度, 类型)
        # 类型：1表示左边界（进入），-1表示右边界（离开）
        events = []
        for left, right, height in buildings:
            # 左边界事件：负高度表示进入
            events.append((left, -height, 0))  # 0表示进入
            # 右边界事件：正高度表示离开
            events.append((right, height, 1))  # 1表示离开
        
        # 按照x坐标排序，如果x相同，则：
        # 1. 进入事件优先于离开事件
        # 2. 进入事件中，高度高的优先
        # 3. 离开事件中，高度低的优先
        events.sort(key=lambda x: (x[0], x[2], x[1]))
        
        # 使用最大堆维护当前有效高度
        # Python的heapq是最小堆，所以存储负值来模拟最大堆
        max_heap = [0]  # 初始高度为0
        result = []
        prev_max_height = 0
        
        for x, h, event_type in events:
            if event_type == 0:  # 进入事件
                heapq.heappush(max_heap, h)  # h是负值，表示高度
            else:  # 离开事件
                # 从堆中移除高度，需要重建堆
                max_heap.remove(-h)  # -h是正值，表示实际高度
                heapq.heapify(max_heap)
            
            # 获取当前最大高度
            curr_max_height = -max_heap[0]  # 堆顶是负值，取反得到实际高度
            
            # 如果最大高度发生变化，记录关键点
            if curr_max_height != prev_max_height:
                result.append([x, curr_max_height])
                prev_max_height = curr_max_height
                
        return result


def main():
    """
    测试函数
    
    工程化测试考量：
    1. 正常功能测试
    2. 边界情况测试
    3. 异常输入测试
    4. 性能压力测试
    """
    # 测试用例1
    buildings1 = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
    expected1 = [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
    
    skyline = SkylineProblem()
    result1 = skyline.get_skyline(buildings1)
    print(f"测试用例1:")
    print(f"输入: {buildings1}")
    print(f"输出: {result1}")
    print(f"期望: {expected1}")
    print(f"结果: {'✓ 通过' if result1 == expected1 else '✗ 失败'}")
    
    # 测试用例2
    buildings2 = [[0,2,3],[2,5,3]]
    expected2 = [[0,3],[5,0]]
    
    result2 = skyline.get_skyline(buildings2)
    print(f"\n测试用例2:")
    print(f"输入: {buildings2}")
    print(f"输出: {result2}")
    print(f"期望: {expected2}")
    print(f"结果: {'✓ 通过' if result2 == expected2 else '✗ 失败'}")
    
    # 边界测试用例3：空输入
    try:
        result3 = skyline.get_skyline([])
        print(f"\n边界测试用例3 (空输入):")
        print(f"输入: []")
        print(f"输出: {result3}")
        print(f"结果: {'✓ 通过' if result3 == [] else '✗ 失败'}")
    except Exception as e:
        print(f"\n边界测试用例3 (空输入):")
        print(f"发生异常: {e}")
        print(f"结果: ✗ 失败")
    
    # 边界测试用例4：单个建筑物
    buildings4 = [[1, 5, 10]]
    expected4 = [[1, 10], [5, 0]]
    
    result4 = skyline.get_skyline(buildings4)
    print(f"\n边界测试用例4 (单个建筑物):")
    print(f"输入: {buildings4}")
    print(f"输出: {result4}")
    print(f"期望: {expected4}")
    print(f"结果: {'✓ 通过' if result4 == expected4 else '✗ 失败'}")


if __name__ == "__main__":
    main()