#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
模拟退火算法 (Simulated Annealing)

算法原理：
模拟退火算法是一种通用概率算法，用来在一个大的搜寻空间内找寻问题的最优解。
它模仿固体物质的退火过程：将固体加热至高温后缓慢冷却，在冷却过程中，
固体内部粒子逐渐有序排列，最终达到低能态（最优解）。

算法特点：
1. 属于元启发式算法，适用于解决NP难问题
2. 能以一定概率接受较差解，从而跳出局部最优
3. 温度参数控制接受差解的概率，随时间推移而降低

应用场景：
- TSP旅行商问题
- 函数优化
- 图着色问题
- 调度问题
- IOI2023、国集2023等竞赛考点

算法流程：
1. 初始化温度T和解状态
2. 在当前温度下进行迭代寻优
3. 产生新解并计算目标函数值
4. 根据Metropolis准则决定是否接受新解
5. 降温，重复步骤2-5直到终止条件

时间复杂度：取决于问题规模和迭代次数，通常为O(k×n)，k为迭代次数，n为问题规模
空间复杂度：O(1) 或 O(n)，取决于具体问题存储需求
"""

import math
import random
import time
from typing import List, Tuple


class SimulatedAnnealing:
    def __init__(self, initial_temperature: float, cooling_rate: float, 
                 min_temperature: float, iterations_per_temp: int):
        """
        初始化模拟退火算法参数
        
        Args:
            initial_temperature: 初始温度
            cooling_rate: 冷却系数 (0 < cooling_rate < 1)
            min_temperature: 终止温度
            iterations_per_temp: 每个温度下的迭代次数
        """
        self.initial_temperature = initial_temperature
        self.cooling_rate = cooling_rate
        self.min_temperature = min_temperature
        self.iterations_per_temp = iterations_per_temp
        self.random = random.Random()
        
        # 当前解和最优解
        self.current_solution: List[float] = []
        self.best_solution: List[float] = []
        self.current_value = float('inf')
        self.best_value = float('inf')
        self.temperature = initial_temperature
    
    def initialize_solution(self, dimensions: int, lower_bounds: List[float], 
                           upper_bounds: List[float]) -> None:
        """
        初始化解空间
        
        Args:
            dimensions: 解的维度
            lower_bounds: 下界数组
            upper_bounds: 上界数组
        """
        self.current_solution = []
        self.best_solution = []
        
        # 随机初始化解
        for i in range(dimensions):
            value = lower_bounds[i] + self.random.random() * (upper_bounds[i] - lower_bounds[i])
            self.current_solution.append(value)
            self.best_solution.append(value)
        
        # 计算初始目标函数值
        self.current_value = self.objective_function(self.current_solution)
        self.best_value = self.current_value
    
    def objective_function(self, solution: List[float]) -> float:
        """
        目标函数 - 需要根据具体问题定义
        这里以最小化函数 f(x) = x1^2 + x2^2 + ... + xn^2 为例
        
        Args:
            solution: 解向量
            
        Returns:
            目标函数值
        """
        return sum(x * x for x in solution)
    
    def generate_neighbor(self, solution: List[float], lower_bounds: List[float], 
                         upper_bounds: List[float]) -> List[float]:
        """
        产生邻域解
        
        Args:
            solution: 当前解
            lower_bounds: 下界
            upper_bounds: 上界
            
        Returns:
            新解
        """
        neighbor = solution.copy()
        index = self.random.randint(0, len(solution) - 1)
        
        # 在当前解的基础上添加一个小的随机扰动
        delta = (upper_bounds[index] - lower_bounds[index]) * 0.1
        neighbor[index] += (self.random.gauss(0, 1) * delta)
        
        # 确保新解在有效范围内
        if neighbor[index] < lower_bounds[index]:
            neighbor[index] = lower_bounds[index]
        elif neighbor[index] > upper_bounds[index]:
            neighbor[index] = upper_bounds[index]
        
        return neighbor
    
    def metropolis_criterion(self, new_value: float, old_value: float, 
                            temperature: float) -> bool:
        """
        Metropolis准则 - 决定是否接受新解
        
        Args:
            new_value: 新解的目标函数值
            old_value: 当前解的目标函数值
            temperature: 当前温度
            
        Returns:
            是否接受新解
        """
        # 如果新解更优，则直接接受
        if new_value < old_value:
            return True
        
        # 否则以一定概率接受较差解
        probability = math.exp(-(new_value - old_value) / temperature)
        return self.random.random() < probability
    
    def cool_down(self, temperature: float) -> float:
        """
        降温函数 - 指数降温
        
        Args:
            temperature: 当前温度
            
        Returns:
            新温度
        """
        return temperature * self.cooling_rate
    
    def solve(self, dimensions: int, lower_bounds: List[float], 
              upper_bounds: List[float]) -> List[float]:
        """
        执行模拟退火算法
        
        Args:
            dimensions: 解的维度
            lower_bounds: 下界数组
            upper_bounds: 上界数组
            
        Returns:
            最优解
        """
        # 初始化
        self.initialize_solution(dimensions, lower_bounds, upper_bounds)
        self.temperature = self.initial_temperature
        
        # 主循环 - 直到温度降到最低温度
        while self.temperature > self.min_temperature:
            # 在当前温度下进行多次迭代
            for _ in range(self.iterations_per_temp):
                # 产生邻域解
                new_solution = self.generate_neighbor(self.current_solution, lower_bounds, upper_bounds)
                new_value = self.objective_function(new_solution)
                
                # 根据Metropolis准则决定是否接受新解
                if self.metropolis_criterion(new_value, self.current_value, self.temperature):
                    # 接受新解
                    self.current_solution = new_solution
                    self.current_value = new_value
                    
                    # 更新最优解
                    if self.current_value < self.best_value:
                        self.best_solution = self.current_solution.copy()
                        self.best_value = self.current_value
            
            # 降温
            self.temperature = self.cool_down(self.temperature)
            
            # 可选：打印当前进度
            # print(f"Temperature: {self.temperature:.2f}, Best Value: {self.best_value:.6f}")
        
        return self.best_solution
    
    def get_best_value(self) -> float:
        """
        获取最优值
        
        Returns:
            最优目标函数值
        """
        return self.best_value


def main():
    """测试示例"""
    # 设置算法参数
    initial_temp = 1000.0     # 初始温度
    cooling_rate = 0.95       # 冷却系数
    min_temp = 1e-8          # 终止温度
    iterations = 100          # 每个温度下的迭代次数
    
    # 创建模拟退火算法实例
    sa = SimulatedAnnealing(initial_temp, cooling_rate, min_temp, iterations)
    
    # 定义问题参数 (以2维函数优化为例)
    dimensions = 2
    lower_bounds: List[float] = [-10.0, -10.0]  # 各维度下界
    upper_bounds: List[float] = [10.0, 10.0]    # 各维度上界
    
    # 执行算法
    print("开始执行模拟退火算法...")
    start_time = time.time()
    result = sa.solve(dimensions, lower_bounds, upper_bounds)
    end_time = time.time()
    
    # 输出结果
    print("算法执行完成！")
    print(f"最优解: [{result[0]:.6f}, {result[1]:.6f}]")
    print(f"最优值: {sa.get_best_value():.10f}")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # 验证结果 (理论上最优解应该接近 [0, 0])
    print("\n结果分析:")
    print("理论最优解: [0, 0]")
    print("理论最优值: 0")
    print(f"误差: {abs(sa.get_best_value()):.10f}")


if __name__ == "__main__":
    main()