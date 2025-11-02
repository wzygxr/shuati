#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1094. 拼车 (Car Pooling)

题目来源：https://leetcode.cn/problems/car-pooling/

题目描述：
车上最初有 capacity 个空座位。车只能向一个方向行驶（不允许掉头或改变方向）。
给定整数 capacity 和一个数组 trips ，trip[i] = [numPassengersi, fromi, toi]
表示第 i 次旅行有 numPassengersi 乘客，接他们和放他们的位置分别是 fromi 和 toi。
这些位置是从汽车的初始位置向东的公里数。
当且仅当你可以在所有给定的行程中接送所有乘客时，返回 true，否则返回 false。

算法思路：
这是一个典型的扫描线算法问题，可以使用以下方法解决：
1. 扫描线算法：将上车和下车事件排序后处理
2. 差分数组：记录每个位置的乘客变化
3. 模拟：按位置顺序模拟乘客上下车过程

时间复杂度：
- 扫描线算法：O(n log n)，其中n是行程数
- 差分数组：O(n + m)，其中n是行程数，m是最大位置
- 空间复杂度：O(n)

应用场景：
1. 交通调度：公交车、出租车调度
2. 资源分配：服务器负载均衡
3. 活动安排：会议室预订

相关题目：
1. LeetCode 1109. 航班预订统计
2. LeetCode 253. 会议室 II
3. LeetCode 218. 天际线问题
"""

from typing import List
import heapq
from collections import defaultdict

class Solution:
    """解决方案类"""
    
    @staticmethod
    def car_pooling_sweep_line(trips: List[List[int]], capacity: int) -> bool:
        """
        方法1：扫描线算法
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        :param trips: 行程信息数组
        :param capacity: 车辆容量
        :return: 是否可以完成所有行程
        """
        # 创建事件列表：[位置, 乘客变化]
        events = []
        
        # 为每个行程创建上车和下车事件
        for passengers, start, end in trips:
            # 上车事件（乘客增加）
            events.append((start, passengers))
            # 下车事件（乘客减少）
            events.append((end, -passengers))
        
        # 按位置排序事件，如果位置相同，下车事件优先于上车事件
        events.sort()
        
        current_passengers = 0
        
        # 扫描所有事件
        for position, change in events:
            current_passengers += change
            if current_passengers > capacity:
                return False
        
        return True
    
    @staticmethod
    def car_pooling_difference_array(trips: List[List[int]], capacity: int) -> bool:
        """
        方法2：差分数组
        时间复杂度：O(n + m)，其中n是行程数，m是最大位置
        空间复杂度：O(m)
        :param trips: 行程信息数组
        :param capacity: 车辆容量
        :return: 是否可以完成所有行程
        """
        # 找到最大位置
        max_location = 0
        for _, start, end in trips:
            max_location = max(max_location, start, end)
        
        # 创建差分数组
        diff = [0] * (max_location + 1)
        
        # 记录每个行程的乘客变化
        for passengers, start, end in trips:
            diff[start] += passengers
            diff[end] -= passengers
        
        # 通过前缀和计算每个位置的实际乘客数
        current_passengers = 0
        for i in range(max_location + 1):
            current_passengers += diff[i]
            if current_passengers > capacity:
                return False
        
        return True
    
    @staticmethod
    def car_pooling_tree_map(trips: List[List[int]], capacity: int) -> bool:
        """
        方法3：使用字典的扫描线算法（适用于位置范围很大的情况）
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        :param trips: 行程信息数组
        :param capacity: 车辆容量
        :return: 是否可以完成所有行程
        """
        # 使用字典记录每个位置的乘客变化
        changes = defaultdict(int)
        
        # 记录每个行程的乘客变化
        for passengers, start, end in trips:
            changes[start] += passengers
            changes[end] -= passengers
        
        # 按位置顺序处理所有变化
        current_passengers = 0
        for position in sorted(changes.keys()):
            current_passengers += changes[position]
            if current_passengers > capacity:
                return False
        
        return True
    
    @staticmethod
    def car_pooling_simulation(trips: List[List[int]], capacity: int) -> bool:
        """
        方法4：模拟法（适用于位置范围较小的情况）
        时间复杂度：O(n * m)，其中n是行程数，m是最大位置
        空间复杂度：O(m)
        :param trips: 行程信息数组
        :param capacity: 车辆容量
        :return: 是否可以完成所有行程
        """
        # 找到最大位置
        max_location = 0
        for _, start, end in trips:
            max_location = max(max_location, start, end)
        
        # 记录每个位置的乘客数
        passengers_at_location = [0] * (max_location + 1)
        
        # 模拟每个行程
        for passengers, start, end in trips:
            # 在行程区间内增加乘客数
            for i in range(start, end):
                passengers_at_location[i] += passengers
                if passengers_at_location[i] > capacity:
                    return False
        
        return True


def test_car_pooling():
    """测试函数"""
    print("=== 测试 LeetCode 1094. 拼车 ===")
    
    # 测试用例1
    trips1 = [[2,1,5],[3,3,7]]
    capacity1 = 4
    print("测试用例1:")
    print("行程:", trips1)
    print("容量:", capacity1)
    print("扫描线算法结果:", Solution.car_pooling_sweep_line(trips1, capacity1))
    print("差分数组结果:", Solution.car_pooling_difference_array(trips1, capacity1))
    print("TreeMap结果:", Solution.car_pooling_tree_map(trips1, capacity1))
    print("模拟法结果:", Solution.car_pooling_simulation(trips1, capacity1))
    print("期望结果: False")
    print()
    
    # 测试用例2
    trips2 = [[2,1,5],[3,3,7]]
    capacity2 = 5
    print("测试用例2:")
    print("行程:", trips2)
    print("容量:", capacity2)
    print("扫描线算法结果:", Solution.car_pooling_sweep_line(trips2, capacity2))
    print("差分数组结果:", Solution.car_pooling_difference_array(trips2, capacity2))
    print("TreeMap结果:", Solution.car_pooling_tree_map(trips2, capacity2))
    print("模拟法结果:", Solution.car_pooling_simulation(trips2, capacity2))
    print("期望结果: True")
    print()
    
    # 测试用例3
    trips3 = [[3,2,7],[3,7,9],[8,3,9]]
    capacity3 = 11
    print("测试用例3:")
    print("行程:", trips3)
    print("容量:", capacity3)
    print("扫描线算法结果:", Solution.car_pooling_sweep_line(trips3, capacity3))
    print("差分数组结果:", Solution.car_pooling_difference_array(trips3, capacity3))
    print("TreeMap结果:", Solution.car_pooling_tree_map(trips3, capacity3))
    print("模拟法结果:", Solution.car_pooling_simulation(trips3, capacity3))
    print("期望结果: True")
    print()


if __name__ == "__main__":
    test_car_pooling()