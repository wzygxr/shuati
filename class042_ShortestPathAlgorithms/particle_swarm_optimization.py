#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
粒子群优化算法 (Particle Swarm Optimization, PSO)

算法原理：
粒子群优化算法是一种基于群体智能的优化算法，模拟鸟群觅食行为。
每个粒子代表一个候选解，在解空间中飞行，通过跟踪个体极值和全局极值来更新自己的速度和位置。

算法特点：
1. 属于群智能算法，适用于连续优化问题
2. 收敛速度快，算法简单易实现
3. 具有良好的全局搜索能力
4. 适用于函数优化、神经网络训练等领域

应用场景：
- 函数优化
- 神经网络训练
- 工程设计优化
- 调度问题
- 图像处理

算法流程：
1. 初始化粒子群（随机生成位置和速度）
2. 计算每个粒子的适应度值
3. 更新个体极值和全局极值
4. 更新粒子的速度和位置
5. 重复步骤2-4直到满足终止条件

时间复杂度：O(G×N×D)，G为迭代次数，N为粒子数量，D为问题维度
空间复杂度：O(N×D)，存储粒子信息
"""

import random
import time
from typing import List


class Particle:
    """粒子类"""
    def __init__(self, dimension: int):
        self.position: List[float] = [0.0] * dimension      # 位置
        self.velocity: List[float] = [0.0] * dimension      # 速度
        self.best_position: List[float] = [0.0] * dimension  # 个体最优位置
        self.best_value = float('inf')                      # 个体最优值


class ParticleSwarmOptimization:
    def __init__(self, num_particles: int, dimension: int, max_iterations: int,
                 w: float, c1: float, c2: float):
        """
        初始化粒子群优化算法参数
        
        Args:
            num_particles: 粒子数量
            dimension: 问题维度
            max_iterations: 最大迭代次数
            w: 惯性权重
            c1: 个体学习因子
            c2: 社会学习因子
        """
        self.num_particles = num_particles
        self.dimension = dimension
        self.max_iterations = max_iterations
        self.w = w
        self.c1 = c1
        self.c2 = c2
        self.lower_bounds: List[float] = []
        self.upper_bounds: List[float] = []
        self.velocity_bounds: List[float] = []
        self.particles: List[Particle] = []
        self.global_best_position: List[float] = [0.0] * dimension
        self.global_best_value = float('inf')
        self.random = random.Random()
    
    def set_bounds(self, lower_bounds: List[float], upper_bounds: List[float]) -> None:
        """
        设置边界
        
        Args:
            lower_bounds: 位置下界
            upper_bounds: 位置上界
        """
        self.lower_bounds = lower_bounds.copy()
        self.upper_bounds = upper_bounds.copy()
        
        # 设置速度边界为位置边界范围的10%
        self.velocity_bounds = [0.1 * (upper_bounds[i] - lower_bounds[i]) 
                               for i in range(self.dimension)]
    
    def initialize_particles(self) -> None:
        """初始化粒子群"""
        self.particles = []
        for i in range(self.num_particles):
            particle = Particle(self.dimension)
            self.particles.append(particle)
            
            # 随机初始化位置和速度
            for j in range(self.dimension):
                # 初始化位置
                particle.position[j] = (
                    self.lower_bounds[j] + 
                    self.random.random() * (self.upper_bounds[j] - self.lower_bounds[j])
                )
                
                # 初始化速度
                particle.velocity[j] = (
                    -self.velocity_bounds[j] + 
                    self.random.random() * (2 * self.velocity_bounds[j])
                )
            
            # 初始化个体最优位置
            particle.best_position = particle.position.copy()
    
    def objective_function(self, position: List[float]) -> float:
        """
        目标函数 - 需要根据具体问题定义
        这里以最小化函数 f(x) = sum(x_i^2) 为例
        
        Args:
            position: 位置向量
            
        Returns:
            目标函数值
        """
        return sum(x * x for x in position)
    
    def evaluate_particles(self) -> None:
        """评估粒子适应度"""
        for particle in self.particles:
            value = self.objective_function(particle.position)
            
            # 更新个体最优
            if value < particle.best_value:
                particle.best_value = value
                particle.best_position = particle.position.copy()
            
            # 更新全局最优
            if value < self.global_best_value:
                self.global_best_value = value
                self.global_best_position = particle.position.copy()
    
    def update_particles(self) -> None:
        """更新粒子速度和位置"""
        for particle in self.particles:
            for j in range(self.dimension):
                # 更新速度
                particle.velocity[j] = (
                    self.w * particle.velocity[j] +
                    self.c1 * self.random.random() * (particle.best_position[j] - particle.position[j]) +
                    self.c2 * self.random.random() * (self.global_best_position[j] - particle.position[j])
                )
                
                # 速度边界处理
                if particle.velocity[j] > self.velocity_bounds[j]:
                    particle.velocity[j] = self.velocity_bounds[j]
                elif particle.velocity[j] < -self.velocity_bounds[j]:
                    particle.velocity[j] = -self.velocity_bounds[j]
                
                # 更新位置
                particle.position[j] += particle.velocity[j]
                
                # 位置边界处理
                if particle.position[j] > self.upper_bounds[j]:
                    particle.position[j] = self.upper_bounds[j]
                elif particle.position[j] < self.lower_bounds[j]:
                    particle.position[j] = self.lower_bounds[j]
    
    def solve(self) -> List[float]:
        """
        执行粒子群优化算法
        
        Returns:
            全局最优位置
        """
        # 初始化
        self.initialize_particles()
        self.global_best_value = float('inf')
        
        # 迭代优化
        for iteration in range(self.max_iterations):
            # 评估适应度
            self.evaluate_particles()
            
            # 更新粒子
            self.update_particles()
            
            # 可选：打印当前进度
            # print(f"Iteration {iteration + 1}: Global Best Value = {self.global_best_value:.10f}")
        
        return self.global_best_position.copy()
    
    def get_global_best_value(self) -> float:
        """
        获取全局最优值
        
        Returns:
            全局最优值
        """
        return self.global_best_value


def main():
    """测试示例"""
    # 设置算法参数
    num_particles = 30      # 粒子数量
    dimension = 10         # 问题维度
    max_iterations = 1000   # 最大迭代次数
    w = 0.7                # 惯性权重
    c1 = 1.5               # 个体学习因子
    c2 = 1.5               # 社会学习因子
    
    # 位置边界
    lower_bounds = [-10.0] * dimension
    upper_bounds = [10.0] * dimension
    
    # 创建粒子群优化算法实例
    pso = ParticleSwarmOptimization(num_particles, dimension, max_iterations, w, c1, c2)
    pso.set_bounds(lower_bounds, upper_bounds)
    
    # 执行算法
    print("开始执行粒子群优化算法...")
    start_time = time.time()
    result = pso.solve()
    end_time = time.time()
    
    # 输出结果
    print("算法执行完成！")
    print("最优位置: [", end="")
    for i in range(len(result)):
        print(f"{result[i]:.6f}", end="")
        if i < len(result) - 1:
            print(", ", end="")
    print("]")
    print(f"最优值: {pso.get_global_best_value():.10f}")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # 验证结果 (理论上最优解应该接近全0向量)
    print("\n结果分析:")
    print("理论最优位置: 全0向量")
    print("理论最优值: 0")
    print(f"误差: {abs(pso.get_global_best_value()):.10f}")


if __name__ == "__main__":
    main()