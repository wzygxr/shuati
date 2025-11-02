#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
蚁群算法 (Ant Colony Optimization, ACO)

算法原理：
蚁群算法是一种模拟蚂蚁觅食行为的群智能优化算法。
蚂蚁在寻找食物时会在路径上释放信息素，其他蚂蚁能够感知信息素浓度，
并倾向于选择信息素浓度高的路径，形成正反馈机制，最终找到最优路径。

算法特点：
1. 属于群智能算法，适用于解决组合优化问题
2. 基于分布式计算，具有良好的并行性
3. 正反馈机制使算法能够快速收敛
4. 适用于解决TSP、VRP等路径优化问题

应用场景：
- 旅行商问题(TSP)
- 车辆路径问题(VRP)
- 网络路由优化
- 作业车间调度问题
- 图着色问题

算法流程：
1. 初始化参数和信息素矩阵
2. 循环迭代：
   a. 每只蚂蚁根据状态转移规则构建解
   b. 计算每只蚂蚁的路径长度
   c. 更新全局最优解
   d. 根据信息素更新规则更新信息素矩阵
3. 直到满足终止条件

时间复杂度：O(G×M×N²)，G为迭代次数，M为蚂蚁数量，N为城市数量
空间复杂度：O(N²)，存储距离矩阵和信息素矩阵
"""

import math
import random
import time
from typing import List, Set, Tuple


class AntColonyOptimization:
    def __init__(self, num_cities: int, num_ants: int, max_iterations: int,
                 alpha: float, beta: float, rho: float, q: float):
        """
        初始化蚁群算法参数
        
        Args:
            num_cities: 城市数量
            num_ants: 蚂蚁数量
            max_iterations: 迭代次数
            alpha: 信息素重要程度参数
            beta: 启发因子重要程度参数
            rho: 信息素挥发系数
            q: 信息素总量
        """
        self.num_cities = num_cities
        self.num_ants = num_ants
        self.max_iterations = max_iterations
        self.alpha = alpha
        self.beta = beta
        self.rho = rho
        self.q = q
        self.distance_matrix = [[0.0 for _ in range(num_cities)] for _ in range(num_cities)]
        self.pheromone_matrix = [[1.0 for _ in range(num_cities)] for _ in range(num_cities)]
        self.best_tour: List[int] = []
        self.best_tour_length = float('inf')
        self.random = random.Random()
    
    def set_distance_matrix(self, distances: List[List[float]]) -> None:
        """
        设置距离矩阵
        
        Args:
            distances: 距离矩阵
        """
        self.distance_matrix = distances
    
    def initialize_pheromone(self, initial_value: float) -> None:
        """
        初始化信息素矩阵
        
        Args:
            initial_value: 初始信息素值
        """
        for i in range(self.num_cities):
            for j in range(self.num_cities):
                self.pheromone_matrix[i][j] = initial_value
    
    def calculate_tour_length(self, tour: List[int]) -> float:
        """
        计算路径长度
        
        Args:
            tour: 路径
            
        Returns:
            路径长度
        """
        length = 0
        for i in range(len(tour) - 1):
            length += self.distance_matrix[tour[i]][tour[i + 1]]
        # 回到起点
        length += self.distance_matrix[tour[-1]][tour[0]]
        return length
    
    def select_next_city(self, current_city: int, visited: Set[int]) -> int:
        """
        选择下一个城市
        
        Args:
            current_city: 当前城市
            visited: 已访问城市集合
            
        Returns:
            下一个城市
        """
        # 计算转移概率
        probabilities = [0.0] * self.num_cities
        total = 0.0
        
        # 计算所有未访问城市的转移概率
        for i in range(self.num_cities):
            if i not in visited:
                # 避免除零错误
                distance = self.distance_matrix[current_city][i]
                if distance == 0:
                    probabilities[i] = 0
                else:
                    # 状态转移规则
                    probabilities[i] = (
                        pow(self.pheromone_matrix[current_city][i], self.alpha) *
                        pow(1.0 / distance, self.beta)
                    )
                total += probabilities[i]
        
        # 如果所有概率都为0，则随机选择一个未访问城市
        if total == 0:
            unvisited = [i for i in range(self.num_cities) if i not in visited]
            return self.random.choice(unvisited)
        
        # 轮盘赌选择
        pick = self.random.random() * total
        current_sum = 0.0
        for i in range(self.num_cities):
            if i not in visited:
                current_sum += probabilities[i]
                if current_sum >= pick:
                    return i
        
        # 如果没有选中，则返回第一个未访问城市
        for i in range(self.num_cities):
            if i not in visited:
                return i
        
        return current_city  # 理论上不会执行到这里
    
    def construct_solution(self, ant_id: int) -> List[int]:
        """
        构建路径
        
        Args:
            ant_id: 蚂蚁编号
            
        Returns:
            路径
        """
        tour = []
        visited = set()
        
        # 随机选择起始城市
        current_city = self.random.randint(0, self.num_cities - 1)
        tour.append(current_city)
        visited.add(current_city)
        
        # 构建完整路径
        while len(visited) < self.num_cities:
            next_city = self.select_next_city(current_city, visited)
            tour.append(next_city)
            visited.add(next_city)
            current_city = next_city
        
        return tour
    
    def update_pheromone(self, ant_tours: List[List[int]], ant_tour_lengths: List[float]) -> None:
        """
        更新信息素
        
        Args:
            ant_tours: 所有蚂蚁的路径
            ant_tour_lengths: 所有蚂蚁的路径长度
        """
        # 信息素挥发
        for i in range(self.num_cities):
            for j in range(self.num_cities):
                self.pheromone_matrix[i][j] *= (1 - self.rho)
        
        # 信息素增强
        for ant in range(self.num_ants):
            contribution = self.q / ant_tour_lengths[ant]
            tour = ant_tours[ant]
            
            for i in range(len(tour) - 1):
                from_city = tour[i]
                to_city = tour[i + 1]
                self.pheromone_matrix[from_city][to_city] += contribution
                self.pheromone_matrix[to_city][from_city] += contribution  # 对称矩阵
            
            # 连接最后一个城市和第一个城市
            last_city = tour[-1]
            first_city = tour[0]
            self.pheromone_matrix[last_city][first_city] += contribution
            self.pheromone_matrix[first_city][last_city] += contribution
    
    def solve(self) -> List[int]:
        """
        执行蚁群算法
        
        Returns:
            最优路径
        """
        # 初始化信息素
        self.initialize_pheromone(1.0)
        self.best_tour_length = float('inf')
        
        # 迭代优化
        for iteration in range(self.max_iterations):
            ant_tours = []
            ant_tour_lengths = []
            
            # 每只蚂蚁构建解
            for ant in range(self.num_ants):
                tour = self.construct_solution(ant)
                tour_length = self.calculate_tour_length(tour)
                
                ant_tours.append(tour)
                ant_tour_lengths.append(tour_length)
                
                # 更新全局最优解
                if tour_length < self.best_tour_length:
                    self.best_tour_length = tour_length
                    self.best_tour = tour.copy()
            
            # 更新信息素
            self.update_pheromone(ant_tours, ant_tour_lengths)
            
            # 可选：打印当前进度
            # print(f"Iteration {iteration + 1}: Best Tour Length = {self.best_tour_length:.2f}")
        
        return self.best_tour
    
    def get_best_tour_length(self) -> float:
        """
        获取最优路径长度
        
        Returns:
            最优路径长度
        """
        return self.best_tour_length


def main():
    """测试示例 - 对称TSP问题"""
    # 创建一个简单的TSP实例（5个城市）
    num_cities = 5
    num_ants = 10
    max_iterations = 100
    alpha = 1.0   # 信息素重要程度
    beta = 2.0    # 启发因子重要程度
    rho = 0.5     # 信息素挥发系数
    q = 100.0     # 信息素总量
    
    # 距离矩阵（对称TSP）
    distances: List[List[float]] = [
        [0.0, 10.0, 15.0, 20.0, 25.0],
        [10.0, 0.0, 35.0, 25.0, 30.0],
        [15.0, 35.0, 0.0, 30.0, 20.0],
        [20.0, 25.0, 30.0, 0.0, 15.0],
        [25.0, 30.0, 20.0, 15.0, 0.0]
    ]
    
    # 创建蚁群算法实例
    aco = AntColonyOptimization(
        num_cities, num_ants, max_iterations, alpha, beta, rho, q
    )
    aco.set_distance_matrix(distances)
    
    # 执行算法
    print("开始执行蚁群算法...")
    start_time = time.time()
    result = aco.solve()
    end_time = time.time()
    
    # 输出结果
    print("算法执行完成！")
    print("最优路径: ", end="")
    for i in range(len(result)):
        print(result[i], end="")
        if i < len(result) - 1:
            print(" -> ", end="")
    print(" ->", result[0])  # 回到起点
    print(f"最优路径长度: {aco.get_best_tour_length():.2f}")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f} ms")


if __name__ == "__main__":
    main()