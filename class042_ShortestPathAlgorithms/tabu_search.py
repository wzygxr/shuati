#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
禁忌搜索算法 (Tabu Search)

算法原理：
禁忌搜索是一种局部搜索的改进算法，通过引入禁忌表来避免循环搜索和陷入局部最优。
算法允许接受劣解，以尽可能地搜索解空间的不同区域。

算法特点：
1. 属于元启发式算法，适用于解决组合优化问题
2. 通过禁忌表避免循环搜索
3. 具有较强的爬山能力
4. 可以跳出局部最优解

应用场景：
- 旅行商问题(TSP)
- 调度问题
- 图着色问题
- 背包问题
- 车间调度问题

算法流程：
1. 初始化当前解和禁忌表
2. 循环迭代：
   a. 在邻域中寻找非禁忌的最佳移动
   b. 执行移动，更新当前解
   c. 更新禁忌表
   d. 更新全局最优解
3. 直到满足终止条件

时间复杂度：O(G×N)，G为迭代次数，N为邻域大小
空间复杂度：O(T)，T为禁忌表大小
"""

import random
import time
from typing import List, Tuple


class TabuSearch:
    def __init__(self, max_iterations: int, tabu_tenure: int, neighborhood_size: int):
        """
        初始化禁忌搜索算法参数
        
        Args:
            max_iterations: 最大迭代次数
            tabu_tenure: 禁忌表长度
            neighborhood_size: 邻域大小
        """
        self.max_iterations = max_iterations
        self.tabu_tenure = tabu_tenure
        self.neighborhood_size = neighborhood_size
        self.tabu_list: List[List[int]] = []
        self.best_solution: List[int] = []
        self.best_value = float('-inf')
        self.random = random.Random()
    
    def initialize_solution(self, dimension: int) -> List[int]:
        """
        初始化解 - 需要根据具体问题定义
        
        Args:
            dimension: 解的维度
            
        Returns:
            初始解
        """
        return [self.random.randint(0, 1) for _ in range(dimension)]
    
    def objective_function(self, solution: List[int]) -> float:
        """
        目标函数 - 需要根据具体问题定义
        这里以最大化函数 f(x) = sum(x_i) 为例（二进制编码）
        
        Args:
            solution: 解
            
        Returns:
            目标函数值
        """
        return sum(solution)
    
    def generate_neighborhood(self, solution: List[int]) -> List[List[int]]:
        """
        生成邻域解
        
        Args:
            solution: 当前解
            
        Returns:
            邻域解集合
        """
        neighborhood = []
        
        # 通过翻转一位生成邻域解
        for i in range(min(self.neighborhood_size, len(solution))):
            neighbor = solution.copy()
            # 翻转第i位
            neighbor[i] = 1 - neighbor[i]
            neighborhood.append(neighbor)
        
        return neighborhood
    
    def is_tabu(self, move: List[int]) -> bool:
        """
        检查移动是否在禁忌表中
        
        Args:
            move: 移动操作
            
        Returns:
            是否在禁忌表中
        """
        return move in self.tabu_list
    
    def update_tabu_list(self, move: List[int]) -> None:
        """
        更新禁忌表
        
        Args:
            move: 新的移动操作
        """
        # 添加新移动到禁忌表
        self.tabu_list.append(move.copy())
        
        # 如果禁忌表超过长度限制，移除最老的移动
        if len(self.tabu_list) > self.tabu_tenure:
            self.tabu_list.pop(0)
    
    def solve(self, dimension: int) -> List[int]:
        """
        执行禁忌搜索算法
        
        Args:
            dimension: 解的维度
            
        Returns:
            最优解
        """
        # 初始化
        current_solution = self.initialize_solution(dimension)
        current_value = self.objective_function(current_solution)
        self.best_solution = current_solution.copy()
        self.best_value = current_value
        
        # 迭代优化
        for iteration in range(self.max_iterations):
            # 生成邻域
            neighborhood = self.generate_neighborhood(current_solution)
            
            # 寻找最佳移动
            best_move = None
            best_move_value = float('-inf')
            
            for neighbor in neighborhood:
                neighbor_value = self.objective_function(neighbor)
                
                # 如果不是禁忌移动或者优于全局最优，则考虑接受
                if not self.is_tabu(neighbor) or neighbor_value > self.best_value:
                    if neighbor_value > best_move_value:
                        best_move = neighbor
                        best_move_value = neighbor_value
            
            # 如果找到了有效的移动
            if best_move is not None:
                # 执行移动
                current_solution = best_move
                current_value = best_move_value
                
                # 更新全局最优
                if current_value > self.best_value:
                    self.best_solution = current_solution.copy()
                    self.best_value = current_value
                
                # 更新禁忌表
                self.update_tabu_list(best_move)
            
            # 可选：打印当前进度
            # print(f"Iteration {iteration + 1}: Best Value = {self.best_value:.2f}")
        
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
    dimension = 20         # 解的维度
    max_iterations = 100   # 最大迭代次数
    tabu_tenure = 10       # 禁忌表长度
    neighborhood_size = 5  # 邻域大小
    
    # 创建禁忌搜索算法实例
    ts = TabuSearch(max_iterations, tabu_tenure, neighborhood_size)
    
    # 执行算法
    print("开始执行禁忌搜索算法...")
    start_time = time.time()
    result = ts.solve(dimension)
    end_time = time.time()
    
    # 输出结果
    print("算法执行完成！")
    print(f"最优解: {result}")
    print(f"最优值: {ts.get_best_value():.2f}")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # 验证结果 (理论上最优解应该全为1)
    print("\n结果分析:")
    print("理论最优解: 全1向量")
    print(f"理论最优值: {dimension}")
    print(f"误差: {dimension - ts.get_best_value():.2f}")


if __name__ == "__main__":
    main()