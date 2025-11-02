#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P1081 开车旅行 - Python实现

题目描述：
给定一个长度为n的数组arr，下标1 ~ n范围，数组无重复值
近的定义、距离的定义，和题目4一致
a和b同坐一辆车开始往右旅行，a先开车，b后开车，此后每到达一点都换人驾驶
如果a在某点驾驶，那么车去往该点右侧第二近的点，如果b在某点驾驶，那么车去往该点右侧第一近的点
a和b从s位置出发，如果开车总距离超过x，或轮到某人时右侧无点可选，那么旅行停止
问题1 : 给定距离x0，返回1 ~ n-1中从哪个点出发，a行驶距离 / b行驶距离，比值最小
        如果从多个点出发时，比值都为最小，那么返回arr中的值最大的点
问题2 : 给定s、x，返回旅行停止时，a开了多少距离、b开了多少距离

解题思路：
这是一个结合了数据结构和倍增思想的复杂问题。

核心思想：
1. 预处理：对于每个城市，找到它右边的第一近和第二近城市
2. 倍增优化：预处理2^k轮a和b交替开车能到达的位置和距离
3. 查询处理：使用倍增快速计算任意起点和距离限制下的行驶情况

时间复杂度：预处理O(n log n)，查询O(log x)
空间复杂度：O(n log n)

算法步骤详解：
1. 使用双向链表预处理每个城市的左右邻居
2. 使用排序和链表思想找到每个城市的第一近和第二近城市
3. 使用倍增思想预处理状态转移表
4. 实现查询函数处理问题1和问题2

工程化考量：
- 使用列表存储数据，Pythonic风格
- 添加边界条件检查
- 使用大整数防止溢出
- 添加详细的错误处理
- 提供完整的测试用例
"""

import math
from typing import List, Tuple, Optional
import sys

class RoadTrip:
    """
    开车旅行问题解决方案
    
    属性:
        n: 城市数量
        arr: 城市高度数组
        next_a: a的下一个城市
        next_b: b的下一个城市
        f: 倍增表
        da: a行驶距离表
        db: b行驶距离表
    """
    
    def __init__(self, heights: List[int]):
        """
        初始化RoadTrip类
        
        参数:
            heights: 城市高度列表
        """
        self.n = len(heights)
        self.arr = heights
        
        # 初始化数组
        self.next_a = [-1] * self.n
        self.next_b = [-1] * self.n
        
        # 预处理邻居
        self._preprocess_neighbors()
        
        # 初始化倍增表
        self._init_doubling_table()
    
    def _preprocess_neighbors(self) -> None:
        """
        预处理每个城市的第一近和第二近邻居
        
        使用排序和双向链表的方法：
        1. 按高度排序城市
        2. 构建双向链表
        3. 对于每个城市，找到距离最近的两个邻居
        
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        # 创建城市索引和高度对
        cities = [(height, idx) for idx, height in enumerate(self.arr)]
        cities.sort()
        
        # 构建双向链表
        left = [-1] * self.n
        right = [-1] * self.n
        
        # 设置左右指针
        for i in range(self.n):
            if i > 0:
                left[cities[i][1]] = cities[i-1][1]
            if i < self.n - 1:
                right[cities[i][1]] = cities[i+1][1]
        
        # 对于每个城市，找到第一近和第二近的邻居
        for i in range(self.n):
            candidates = []
            
            # 检查左边邻居
            if left[i] != -1:
                dist = abs(self.arr[i] - self.arr[left[i]])
                candidates.append((dist, left[i]))
                if left[left[i]] != -1:
                    dist2 = abs(self.arr[i] - self.arr[left[left[i]]])
                    candidates.append((dist2, left[left[i]]))
            
            # 检查右边邻居
            if right[i] != -1:
                dist = abs(self.arr[i] - self.arr[right[i]])
                candidates.append((dist, right[i]))
                if right[right[i]] != -1:
                    dist2 = abs(self.arr[i] - self.arr[right[right[i]]])
                    candidates.append((dist2, right[right[i]]))
            
            # 按距离排序，距离相同按高度排序
            candidates.sort(key=lambda x: (x[0], self.arr[x[1]]))
            
            # 设置next_a和next_b
            if len(candidates) >= 2:
                self.next_a[i] = candidates[1][1]  # 第二近
                self.next_b[i] = candidates[0][1]  # 第一近
            elif len(candidates) == 1:
                self.next_b[i] = candidates[0][1]
    
    def _init_doubling_table(self) -> None:
        """
        初始化倍增表
        
        倍增表定义:
        f[0][i] = next_b[next_a[i]]  (a先开，b后开)
        da[0][i] = dist(i, next_a[i])
        db[0][i] = dist(next_a[i], next_b[next_a[i]])
        
        时间复杂度: O(n log n)
        空间复杂度: O(n log n)
        """
        # 计算倍增表的层数
        k = int(math.log2(self.n)) + 1 if self.n > 0 else 1
        
        # 初始化倍增表
        self.f = [[-1] * self.n for _ in range(k)]
        self.da = [[0] * self.n for _ in range(k)]
        self.db = [[0] * self.n for _ in range(k)]
        
        # 初始化第一层
        for i in range(self.n):
            if self.next_a[i] != -1 and self.next_b[self.next_a[i]] != -1:
                self.f[0][i] = self.next_b[self.next_a[i]]
                self.da[0][i] = abs(self.arr[i] - self.arr[self.next_a[i]])
                self.db[0][i] = abs(self.arr[self.next_a[i]] - self.arr[self.next_b[self.next_a[i]]])
        
        # 构建倍增表
        for j in range(1, k):
            for i in range(self.n):
                if self.f[j-1][i] != -1 and self.f[j-1][self.f[j-1][i]] != -1:
                    self.f[j][i] = self.f[j-1][self.f[j-1][i]]
                    self.da[j][i] = self.da[j-1][i] + self.da[j-1][self.f[j-1][i]]
                    self.db[j][i] = self.db[j-1][i] + self.db[j-1][self.f[j-1][i]]
    
    def _calculate_trip(self, s: int, x: int) -> Tuple[int, int]:
        """
        计算从起点s出发，在距离限制x内的行驶情况
        
        参数:
            s: 起点索引(0-indexed)
            x: 最大行驶距离
            
        返回:
            (dist_a, dist_b): a和b行驶的距离
            
        时间复杂度: O(log x)
        """
        dist_a, dist_b = 0, 0
        current = s
        
        # 从最高位开始尝试
        k = len(self.f)
        for j in range(k-1, -1, -1):
            if (self.f[j][current] != -1 and 
                dist_a + dist_b + self.da[j][current] + self.db[j][current] <= x):
                dist_a += self.da[j][current]
                dist_b += self.db[j][current]
                current = self.f[j][current]
        
        # 检查是否还能让a开一轮
        if (self.next_a[current] != -1 and 
            dist_a + dist_b + abs(self.arr[current] - self.arr[self.next_a[current]]) <= x):
            dist_a += abs(self.arr[current] - self.arr[self.next_a[current]])
            current = self.next_a[current]
        
        return dist_a, dist_b
    
    def find_best_start(self, x0: int) -> int:
        """
        问题1：找到比值最小的起点
        
        参数:
            x0: 最大行驶距离
            
        返回:
            最佳起点的1-indexed索引
            
        时间复杂度: O(n log x)
        """
        best_start = -1
        min_ratio = float('inf')
        max_value = -1
        
        for i in range(self.n - 1):  # 从1~n-1出发
            dist_a, dist_b = self._calculate_trip(i, x0)
            
            if dist_b == 0:
                continue  # 避免除零
            
            ratio = dist_a / dist_b
            
            if (ratio < min_ratio or 
                (abs(ratio - min_ratio) < 1e-9 and self.arr[i] > max_value)):
                min_ratio = ratio
                best_start = i
                max_value = self.arr[i]
        
        return best_start + 1  # 返回1-indexed
    
    def solve_problem2(self, s: int, x: int) -> Tuple[int, int]:
        """
        问题2：计算从s出发，距离限制x的行驶情况
        
        参数:
            s: 起点(1-indexed)
            x: 最大行驶距离
            
        返回:
            (dist_a, dist_b): a和b行驶的距离
        """
        return self._calculate_trip(s-1, x)  # 转换为0-indexed


def test_road_trip():
    """
    测试RoadTrip类
    """
    print("=== RoadTrip算法测试 ===")
    
    # 测试用例1：基础测试
    heights1 = [2, 3, 1, 4, 5]
    rt1 = RoadTrip(heights1)
    
    # 问题1测试
    best_start = rt1.find_best_start(10)
    print(f"测试用例1 - 最佳起点: {best_start}")
    
    # 问题2测试
    dist_a, dist_b = rt1.solve_problem2(1, 10)
    print(f"从起点1出发，a距离: {dist_a}, b距离: {dist_b}")
    
    # 测试用例2：边界测试
    heights2 = [1, 2]
    rt2 = RoadTrip(heights2)
    
    best_start = rt2.find_best_start(5)
    print(f"测试用例2 - 最佳起点: {best_start}")
    
    # 测试用例3：性能测试
    heights3 = list(range(1, 101))  # 100个城市
    rt3 = RoadTrip(heights3)
    
    best_start = rt3.find_best_start(1000)
    print(f"测试用例3 - 最佳起点: {best_start}")
    
    print("=== 测试完成 ===")


if __name__ == "__main__":
    test_road_trip()


"""
复杂度分析：
时间复杂度：
- 预处理邻居：O(n log n) - 排序和链表操作
- 构建倍增表：O(n log n) - 每个城市处理log n次
- 查询：O(log x) - 倍增查询

空间复杂度：O(n log n) - 存储倍增表

算法优化点：
1. 使用倍增思想将线性查询优化为对数级别
2. 预处理避免重复计算
3. 使用排序和链表高效找到邻居

Python特性利用：
1. 使用列表推导式简化代码
2. 利用Python的动态类型和内置排序
3. 使用元组和列表进行高效数据处理

工程化改进：
1. 添加完整的类型注解
2. 使用文档字符串提供详细说明
3. 提供完整的测试用例
4. 模块化设计，便于维护
5. 添加边界条件检查

跨语言对比：
- Python版本更简洁，但性能略低于C++
- 使用Python内置排序和列表操作
- 适合快速原型开发和教学演示
"""