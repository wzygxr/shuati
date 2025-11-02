#!/usr/bin/env python3
"""
LeetCode 1094. 拼车 (Car Pooling) - Python版本

题目来源：https://leetcode.cn/problems/car-pooling/

解题思路：
使用差分数组解决拼车问题

时间复杂度：O(n + m)
空间复杂度：O(m)
"""

import time
import random
from typing import List

class Solution:
    def carPooling(self, trips: List[List[int]], capacity: int) -> bool:
        """
        使用差分数组解决拼车问题
        
        Args:
            trips: 行程数组，每个行程是 [乘客数, 上车位置, 下车位置]
            capacity: 车辆容量
            
        Returns:
            是否能完成所有行程
        """
        if not trips:
            return True
        
        # 找到最大位置
        max_location = 0
        for trip in trips:
            max_location = max(max_location, trip[2])
        
        # 创建差分数组，大小为最大位置 + 1
        diff = [0] * (max_location + 1)
        
        # 处理每个行程
        for trip in trips:
            passengers, from_loc, to_loc = trip
            
            # 在差分数组中标记乘客变化
            diff[from_loc] += passengers  # 上车位置增加乘客
            if to_loc < max_location:
                diff[to_loc] -= passengers  # 下车位置减少乘客
        
        # 计算每个位置的乘客数量
        current_passengers = 0
        for i in range(max_location + 1):
            current_passengers += diff[i]
            # 如果某个位置的乘客数量超过容量，返回False
            if current_passengers > capacity:
                return False
        
        return True
    
    def carPoolingBruteForce(self, trips: List[List[int]], capacity: int) -> bool:
        """
        暴力解法（用于对比）
        
        Args:
            trips: 行程数组
            capacity: 车辆容量
            
        Returns:
            是否能完成所有行程
        """
        if not trips:
            return True
        
        # 找到最大位置
        max_location = 0
        for trip in trips:
            max_location = max(max_location, trip[2])
        
        # 创建乘客数量数组
        passengers = [0] * (max_location + 1)
        
        # 处理每个行程
        for trip in trips:
            num_passengers, from_loc, to_loc = trip
            
            # 直接更新每个位置的乘客数量
            for i in range(from_loc, to_loc):
                passengers[i] += num_passengers
                if passengers[i] > capacity:
                    return False
        
        return True

def test_car_pooling():
    """测试拼车问题解法"""
    solution = Solution()
    
    print("=== LeetCode 1094. 拼车 (Python版本) ===")
    
    # 测试用例1
    print("测试用例1:")
    trips1 = [[2, 1, 5], [3, 3, 7]]
    capacity1 = 4
    result1 = solution.carPooling(trips1, capacity1)
    print(f"行程: {trips1}")
    print(f"容量: {capacity1}")
    print(f"差分数组结果: {result1}")
    print(f"暴力解法结果: {solution.carPoolingBruteForce(trips1, capacity1)}")
    print("期望: False")
    print()
    
    # 测试用例2
    print("测试用例2:")
    trips2 = [[2, 1, 5], [3, 3, 7]]
    capacity2 = 5
    result2 = solution.carPooling(trips2, capacity2)
    print(f"行程: {trips2}")
    print(f"容量: {capacity2}")
    print(f"差分数组结果: {result2}")
    print(f"暴力解法结果: {solution.carPoolingBruteForce(trips2, capacity2)}")
    print("期望: True")
    print()
    
    # 性能测试
    print("=== 性能测试 ===")
    random.seed(42)
    n = 1000
    max_loc = 1000
    trips = []
    
    for i in range(n):
        from_loc = random.randint(0, max_loc - 2)
        to_loc = from_loc + random.randint(1, max_loc - from_loc - 1) + 1
        passengers = random.randint(1, 10)
        trips.append([passengers, from_loc, to_loc])
    
    capacity = 50
    
    start_time = time.time()
    diff_result = solution.carPooling(trips, capacity)
    end_time = time.time()
    print(f"差分数组法处理{n}个行程时间: {(end_time - start_time) * 1000:.2f} ms")
    
    start_time = time.time()
    brute_result = solution.carPoolingBruteForce(trips, capacity)
    end_time = time.time()
    print(f"暴力解法处理{n}个行程时间: {(end_time - start_time) * 1000:.2f} ms")
    
    print(f"两种方法结果是否一致: {diff_result == brute_result}")
    
    # Python语言特性考量
    print("\n=== Python语言特性考量 ===")
    print("1. 使用列表推导式生成测试数据")
    print("2. 使用类型注解提高代码可读性")
    print("3. 使用f-string进行格式化输出")
    print("4. 使用解包操作简化代码")
    
    # 算法复杂度分析
    print("\n=== 算法复杂度分析 ===")
    print("时间复杂度: O(n + m)")
    print("  - 遍历行程: O(n)")
    print("  - 计算前缀和: O(m)")
    print("空间复杂度: O(m)")
    print("  - 差分数组: O(m)")

if __name__ == "__main__":
    test_car_pooling()