#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
蒙特卡洛方法 (Monte Carlo Method)

算法原理：
蒙特卡洛方法是一种基于随机采样的数值计算方法，通过大量随机实验来求解问题。
它将所求解的问题同一定的概率模型相联系，利用电子计算机实现统计模拟或抽样，
以获得问题的近似解。

算法特点：
1. 基于概率统计理论的数值计算方法
2. 收敛速度与问题维度无关
3. 适用于高维问题和复杂积分计算
4. 结果具有随机性，需要多次实验取平均

应用场景：
- 数值积分计算
- 概率统计问题
- 物理模拟
- 金融工程
- 图形学渲染

算法流程：
1. 构造概率模型
2. 进行大量随机抽样实验
3. 统计实验结果
4. 根据大数定律得到近似解

时间复杂度：O(N)，N为抽样次数
空间复杂度：O(1)
"""

import random
import time
import math
from typing import List, Tuple


class MonteCarlo:
    def __init__(self):
        """构造函数"""
        self.random = random.Random()
    
    def calculate_pi(self, num_samples: int) -> float:
        """
        使用蒙特卡洛方法计算π值
        原理：在单位正方形内随机撒点，统计落在单位圆内的点的比例
        单位圆面积 = π，单位正方形面积 = 4
        π/4 = 圆内点数/总点数
        
        Args:
            num_samples: 抽样次数
            
        Returns:
            π的近似值
        """
        inside_circle = 0
        
        for _ in range(num_samples):
            # 在[-1, 1]范围内随机生成点坐标
            x = self.random.random() * 2 - 1
            y = self.random.random() * 2 - 1
            
            # 判断点是否在单位圆内
            if x * x + y * y <= 1:
                inside_circle += 1
        
        # 根据比例计算π值
        return 4.0 * inside_circle / num_samples
    
    def calculate_integral(self, num_samples: int, a: float, b: float) -> float:
        """
        使用蒙特卡洛方法计算定积分
        示例：计算函数 f(x) = x^2 在区间 [0, 1] 上的定积分
        理论值为 1/3
        
        Args:
            num_samples: 抽样次数
            a: 积分下限
            b: 积分上限
            
        Returns:
            定积分的近似值
        """
        sum_fx = 0.0
        
        for _ in range(num_samples):
            # 在区间[a, b]内随机生成点
            x = a + self.random.random() * (b - a)
            
            # 计算函数值 f(x) = x^2
            fx = x * x
            
            sum_fx += fx
        
        # 根据蒙特卡洛积分公式计算结果
        return (b - a) * sum_fx / num_samples
    
    def estimate_circle_area(self, num_samples: int, radius: float) -> float:
        """
        使用蒙特卡洛方法估算圆的面积
        通过在矩形区域内随机撒点，统计落在圆内的点的比例来估算圆的面积
        
        Args:
            num_samples: 抽样次数
            radius: 圆的半径
            
        Returns:
            圆面积的近似值
        """
        # 外接正方形的边长
        side = 2 * radius
        # 外接正方形的面积
        square_area = side * side
        
        inside_circle = 0
        
        for _ in range(num_samples):
            # 在正方形内随机生成点坐标
            x = self.random.random() * side - radius
            y = self.random.random() * side - radius
            
            # 判断点是否在圆内
            if x * x + y * y <= radius * radius:
                inside_circle += 1
        
        # 根据比例计算圆面积
        return square_area * inside_circle / num_samples
    
    def buffon_needle(self, num_samples: int, needle_length: float, line_spacing: float) -> float:
        """
        使用蒙特卡洛方法求解Buffon投针问题
        计算概率 π ≈ (2 * 针长度 * 投掷次数) / (平行线间距 * 与线相交次数)
        
        Args:
            num_samples: 投掷次数
            needle_length: 针的长度
            line_spacing: 平行线间距
            
        Returns:
            π的近似值
        """
        cross_count = 0
        
        for _ in range(num_samples):
            # 随机生成针的中心位置
            center = self.random.random() * line_spacing
            
            # 随机生成针的角度 (0到π)
            angle = self.random.random() * math.pi
            
            # 计算针端点到最近平行线的距离
            distance = min(center, line_spacing - center)
            
            # 计算针端点在垂直方向的投影
            projection = (needle_length / 2) * math.sin(angle)
            
            # 如果投影大于距离，则针与平行线相交
            if projection >= distance:
                cross_count += 1
        
        # 根据Buffon公式计算π
        if cross_count == 0:
            return float('inf')  # 避免除零错误
        return (2.0 * needle_length * num_samples) / (line_spacing * cross_count)


def main():
    """测试示例"""
    mc = MonteCarlo()
    
    print("=== 蒙特卡洛方法测试 ===")
    
    # 测试π值计算
    print("\n1. 计算π值:")
    sample_sizes = [1000, 10000, 100000, 1000000]
    for samples in sample_sizes:
        start_time = time.time()
        pi = mc.calculate_pi(samples)
        end_time = time.time()
        
        print(f"抽样次数: {samples}, π ≈ {pi:.6f}, 误差: {abs(math.pi - pi):.6f}, "
              f"时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # 测试定积分计算
    print("\n2. 计算定积分 ∫x²dx (0到1):")
    theoretical_value = 1.0 / 3.0  # 理论值
    for samples in sample_sizes:
        start_time = time.time()
        integral = mc.calculate_integral(samples, 0, 1)
        end_time = time.time()
        
        print(f"抽样次数: {samples}, 积分值 ≈ {integral:.6f}, "
              f"误差: {abs(theoretical_value - integral):.6f}, "
              f"时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # 测试圆面积估算
    print("\n3. 估算圆面积 (半径=1):")
    theoretical_area = math.pi  # 理论面积
    for samples in sample_sizes:
        start_time = time.time()
        area = mc.estimate_circle_area(samples, 1.0)
        end_time = time.time()
        
        print(f"抽样次数: {samples}, 面积 ≈ {area:.6f}, "
              f"误差: {abs(theoretical_area - area):.6f}, "
              f"时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # 测试Buffon投针问题
    print("\n4. Buffon投针问题 (针长=0.5, 线间距=1):")
    for samples in sample_sizes:
        start_time = time.time()
        pi_buffon = mc.buffon_needle(samples, 0.5, 1.0)
        end_time = time.time()
        
        if math.isfinite(pi_buffon):
            print(f"投掷次数: {samples}, π ≈ {pi_buffon:.6f}, "
                  f"误差: {abs(math.pi - pi_buffon):.6f}, "
                  f"时间: {(end_time - start_time) * 1000:.2f} ms")
        else:
            print(f"投掷次数: {samples}, π ≈ 无穷大, "
                  f"时间: {(end_time - start_time) * 1000:.2f} ms")


if __name__ == "__main__":
    main()