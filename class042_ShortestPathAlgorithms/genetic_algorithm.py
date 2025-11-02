#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
遗传算法 (Genetic Algorithm)

算法原理：
遗传算法是一种模拟自然选择和遗传机制的随机搜索算法。
它将问题的解编码为"染色体"，通过选择、交叉、变异等操作，
使种群不断进化，最终找到问题的最优解或近似最优解。

算法特点：
1. 属于元启发式算法，适用于解决NP难问题
2. 基于种群的全局搜索算法
3. 不需要导数信息，适用于离散和连续优化问题
4. 具有良好的并行性

应用场景：
- 函数优化
- 组合优化（TSP、背包问题等）
- 机器学习（特征选择、神经网络训练等）
- 调度问题
- 工程设计优化

算法流程：
1. 初始化种群（随机生成初始解）
2. 计算适应度（目标函数值）
3. 选择操作（根据适应度选择个体）
4. 交叉操作（模拟生物交配产生后代）
5. 变异操作（模拟生物基因突变）
6. 生成新种群，重复步骤2-6直到满足终止条件

时间复杂度：O(G×N×M)，G为迭代代数，N为种群大小，M为个体编码长度
空间复杂度：O(N×M)，存储种群信息
"""

import random
import time
from typing import List, Tuple


class GeneticAlgorithm:
    def __init__(self, population_size: int, chromosome_length: int, max_generations: int,
                 crossover_rate: float, mutation_rate: float):
        """
        初始化遗传算法参数
        
        Args:
            population_size: 种群大小
            chromosome_length: 染色体长度（问题维度）
            max_generations: 最大迭代代数
            crossover_rate: 交叉概率
            mutation_rate: 变异概率
        """
        self.population_size = population_size
        self.chromosome_length = chromosome_length
        self.max_generations = max_generations
        self.crossover_rate = crossover_rate
        self.mutation_rate = mutation_rate
        self.population: List[List[int]] = []
        self.fitness: List[float] = []
        self.best_individual: List[int] = []
        self.best_fitness = float('-inf')
        self.random = random.Random()
    
    def initialize_population(self) -> None:
        """初始化种群"""
        self.population = []
        self.fitness = []
        
        # 随机生成初始种群
        for _ in range(self.population_size):
            individual = [self.random.randint(0, 1) for _ in range(self.chromosome_length)]
            self.population.append(individual)
    
    def calculate_fitness(self, individual: List[int]) -> float:
        """
        计算适应度 - 需要根据具体问题定义
        这里以最大化函数 f(x) = sum(x_i) 为例（二进制编码）
        
        Args:
            individual: 个体（染色体）
            
        Returns:
            适应度值
        """
        return sum(individual)
    
    def evaluate_population(self) -> None:
        """评估整个种群的适应度"""
        self.fitness = []
        max_fitness = float('-inf')
        best_index = 0
        
        for i in range(self.population_size):
            fit = self.calculate_fitness(self.population[i])
            self.fitness.append(fit)
            
            # 更新最优个体
            if fit > max_fitness:
                max_fitness = fit
                best_index = i
        
        # 更新全局最优
        if max_fitness > self.best_fitness:
            self.best_fitness = max_fitness
            self.best_individual = self.population[best_index].copy()
    
    def select(self) -> int:
        """
        选择操作 - 轮盘赌选择
        
        Returns:
            选中的个体索引
        """
        # 计算总适应度
        total_fitness = sum(max(0, fit) for fit in self.fitness)  # 确保适应度非负
        
        # 如果总适应度为0，则随机选择
        if total_fitness <= 0:
            return self.random.randint(0, self.population_size - 1)
        
        # 轮盘赌选择
        pick = self.random.random() * total_fitness
        current_sum = 0
        for i in range(self.population_size):
            current_sum += max(0, self.fitness[i])
            if current_sum >= pick:
                return i
        
        return self.population_size - 1
    
    def crossover(self, parent1: List[int], parent2: List[int]) -> List[List[int]]:
        """
        交叉操作 - 单点交叉
        
        Args:
            parent1: 父代1
            parent2: 父代2
            
        Returns:
            两个子代
        """
        # 以一定概率进行交叉
        if self.random.random() > self.crossover_rate:
            return [parent1.copy(), parent2.copy()]
        
        # 随机选择交叉点
        crossover_point = self.random.randint(0, self.chromosome_length - 1)
        
        # 创建子代
        child1 = parent1[:crossover_point] + parent2[crossover_point:]
        child2 = parent2[:crossover_point] + parent1[crossover_point:]
        
        return [child1, child2]
    
    def mutate(self, individual: List[int]) -> None:
        """
        变异操作 - 位翻转变异
        
        Args:
            individual: 个体
        """
        for i in range(self.chromosome_length):
            # 以一定概率进行变异
            if self.random.random() < self.mutation_rate:
                # 位翻转
                individual[i] = 1 - individual[i]
    
    def generate_new_population(self) -> None:
        """生成新种群"""
        new_population = []
        
        # 保留最优个体（精英策略）
        best_index = 0
        max_fitness = float('-inf')
        for i in range(self.population_size):
            if self.fitness[i] > max_fitness:
                max_fitness = self.fitness[i]
                best_index = i
        new_population.append(self.population[best_index].copy())
        
        # 生成其余个体
        while len(new_population) < self.population_size:
            # 选择两个父代
            parent1_index = self.select()
            parent2_index = self.select()
            
            # 交叉
            offspring = self.crossover(
                self.population[parent1_index], 
                self.population[parent2_index]
            )
            
            # 变异
            for child in offspring:
                self.mutate(child)
                new_population.append(child)
                
                # 如果新种群已满，跳出循环
                if len(new_population) >= self.population_size:
                    break
        
        # 确保种群大小不变
        while len(new_population) > self.population_size:
            new_population.pop()
        
        self.population = new_population
    
    def solve(self) -> List[int]:
        """
        执行遗传算法
        
        Returns:
            最优个体
        """
        # 初始化
        self.initialize_population()
        self.best_fitness = float('-inf')
        
        # 迭代进化
        for generation in range(self.max_generations):
            # 评估适应度
            self.evaluate_population()
            
            # 生成新种群
            self.generate_new_population()
            
            # 可选：打印当前进度
            # print(f"Generation {generation + 1}: Best Fitness = {self.best_fitness:.2f}")
        
        return self.best_individual
    
    def get_best_fitness(self) -> float:
        """
        获取最优适应度值
        
        Returns:
            最优适应度值
        """
        return self.best_fitness


def main():
    """测试示例"""
    # 设置算法参数
    population_size = 100      # 种群大小
    chromosome_length = 20     # 染色体长度
    max_generations = 100      # 最大迭代代数
    crossover_rate = 0.8       # 交叉概率
    mutation_rate = 0.01       # 变异概率
    
    # 创建遗传算法实例
    ga = GeneticAlgorithm(
        population_size, chromosome_length, max_generations, 
        crossover_rate, mutation_rate
    )
    
    # 执行算法
    print("开始执行遗传算法...")
    start_time = time.time()
    result = ga.solve()
    end_time = time.time()
    
    # 输出结果
    print("算法执行完成！")
    print(f"最优个体: {result}")
    print(f"最优适应度: {ga.get_best_fitness():.2f}")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # 验证结果 (理论上最优解应该全为1)
    print("\n结果分析:")
    print("理论最优个体: 全1向量")
    print(f"理论最优适应度: {chromosome_length}")
    print(f"误差: {chromosome_length - ga.get_best_fitness():.2f}")


if __name__ == "__main__":
    main()