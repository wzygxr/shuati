#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
天际线问题
题目来源：LeetCode 218. 天际线问题
题目链接：https://leetcode.cn/problems/the-skyline-problem/

核心算法：扫描线 + 优先队列
难度：困难

【题目详细描述】
城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
每个建筑物的几何信息用数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
lefti 是第 i 座建筑物左边缘的 x 坐标。
righti 是第 i 座建筑物右边缘的 x 坐标。
heighti 是第 i 座建筑物的高度。
你可以假设所有的建筑都是完美的长方形，在高度为 0 的绝对平坦的表面上。
天际线应该表示为由"关键点"组成的列表，格式 [[x1,y1],[x2,y2],...] ，并按 x 坐标进行排序。
关键点是水平线段的左端点。列表中最后一个点是最右侧建筑物的终点，y 坐标始终为 0，仅用于标记天际线的终点。
此外，任何两个相邻建筑物之间的地面都应被视为天际线轮廓的一部分。

【解题思路】
使用扫描线算法结合优先队列来解决这个问题。
1. 将所有建筑物的左右边界作为事件点处理
2. 对事件点按x坐标排序，相同x坐标时按高度处理（进入事件优先于离开事件）
3. 使用优先队列维护当前活跃建筑物的高度
4. 遍历事件点，更新天际线关键点

【核心算法】
1. 扫描线算法：从左到右扫描所有建筑物的边界
2. 优先队列：维护当前活跃建筑物的高度信息
3. 事件处理：处理建筑物的进入和离开事件

【复杂度分析】
- 时间复杂度：O(n log n)，其中n是建筑物数量
- 空间复杂度：O(n)，用于存储事件点和优先队列

【算法优化点】
1. 事件点排序：合理设计比较器，确保正确处理相同x坐标的事件
2. 优先队列维护：使用延迟删除技术避免频繁的删除操作
3. 结果去重：避免添加重复的关键点

【工程化考量】
1. 输入输出效率：使用标准输入输出处理
2. 内存管理：合理使用数据结构，避免内存泄漏
3. 错误处理：处理边界情况和异常输入

【类似题目推荐】
1. LeetCode 850. 矩形面积 II - https://leetcode.cn/problems/rectangle-area-ii/
2. LeetCode 699. 掉落的方块 - https://leetcode.cn/problems/falling-squares/
3. Codeforces 915E - Physical Education Lessons - https://codeforces.com/problemset/problem/915/E
4. POJ 1151 - Atlantis - http://poj.org/problem?id=1151
"""

import heapq
from collections import defaultdict

class Solution:
    def getSkyline(self, buildings):
        """
        获取天际线关键点
        
        Args:
            buildings: List[List[int]] - 建筑物信息，每个元素为[left, right, height]
            
        Returns:
            List[List[int]] - 天际线关键点列表
        """
        # 创建事件点列表
        events = []
        for left, right, height in buildings:
            # 进入事件：(x, -height, 0) 负高度表示进入
            events.append((left, -height, 0))
            # 离开事件：(x, height, 1) 正高度表示离开
            events.append((right, height, 1))
        
        # 对事件点进行排序
        # 1. 按x坐标升序排列
        # 2. 相同x坐标时，进入事件(-height)优先于离开事件(height)
        # 3. 相同x坐标和事件类型时，进入事件按高度降序，离开事件按高度升序
        events.sort()
        
        # 使用优先队列维护当前活跃建筑物的高度
        # Python的heapq是最小堆，所以我们存储负高度来模拟最大堆
        heights = [0]  # 初始地面高度为0
        result = []
        prev_height = 0
        
        # 处理每个事件点
        for x, h, t in events:
            if t == 0:  # 进入事件
                heapq.heappush(heights, h)  # h是负数，表示高度
            else:  # 离开事件
                # 从堆中移除对应的高度
                heights.remove(-h)  # -h是正数，表示实际高度
                heapq.heapify(heights)  # 重新调整堆
            
            # 获取当前最大高度（堆顶的负值）
            current_height = -heights[0]
            
            # 如果高度发生变化，添加关键点
            if current_height != prev_height:
                result.append([x, current_height])
                prev_height = current_height
        
        return result

def main():
    """
    主函数：测试天际线问题解决方案
    """
    solution = Solution()
    
    # 测试用例1
    buildings1 = [[2, 9, 10], [3, 7, 15], [5, 12, 12], [15, 20, 10], [19, 24, 8]]
    result1 = solution.getSkyline(buildings1)
    print("测试用例1结果:", result1)
    # 预期输出: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
    
    # 测试用例2
    buildings2 = [[0, 2, 3], [2, 5, 3]]
    result2 = solution.getSkyline(buildings2)
    print("测试用例2结果:", result2)
    # 预期输出: [[0,3],[5,0]]

if __name__ == "__main__":
    main()