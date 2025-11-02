#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'''
LeetCode 621. 任务调度器
题目链接：https://leetcode.cn/problems/task-scheduler/
难度：中等

问题描述：
给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。
然而，两个相同种类的任务之间必须有长度为整数 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
你需要计算完成所有任务所需要的最短时间。

示例：
输入：tasks = ["A","A","A","B","B","B"], n = 2
输出：8
解释：A -> B -> (待命) -> A -> B -> (待命) -> A -> B
      在本示例中，两个相同类型任务之间必须间隔长度为 n = 2 的冷却时间，而执行一个任务只需要一个单位时间，所以中间出现了（待命）状态。 

解题思路：
贪心算法 + 优先队列（最大堆）
1. 统计每个任务的频率
2. 使用最大堆存储任务频率，确保每次优先处理频率最高的任务
3. 维护一个时间计数器，在每个时间单位：
   a. 从堆中取出最多n+1个任务（确保同类型任务间隔n个时间单位）
   b. 将取出的任务频率减1，如果还有剩余频率则暂时保存
   c. 计算该时间片实际消耗的时间（如果有任务执行，消耗的时间等于取出的任务数；否则消耗1个时间单位）
   d. 将还有剩余频率的任务重新放回堆中
4. 重复步骤3直到堆为空

时间复杂度分析：
- 统计频率：O(n)
- 堆操作：O(m log k)，其中m是任务总数，k是不同任务的数量
总体时间复杂度：O(n + m log k)

空间复杂度分析：
- 统计频率的哈希表：O(k)
- 最大堆：O(k)
总体空间复杂度：O(k)

最优性证明：
贪心策略确保每次处理剩余频率最高的任务，这样可以最小化空闲时间。通过优先处理高频任务，可以最大程度地填充任务之间的冷却时间，避免不必要的等待。
'''

import heapq
from typing import List
from collections import Counter
import time
import random

class TaskScheduler:
    def __init__(self):
        """初始化任务调度器"""
        pass
    
    def least_interval_heap(self, tasks: List[str], n: int) -> int:
        """
        基于优先队列（堆）的解法
        
        Args:
            tasks: 任务列表
            n: 冷却时间
            
        Returns:
            完成所有任务所需的最短时间
        """
        # 特殊情况处理
        if not tasks:
            return 0
        
        if n == 0:
            return len(tasks)  # 没有冷却时间，直接返回任务数量
        
        # 统计每个任务的频率
        task_counts = Counter(tasks)
        
        # Python的heapq是最小堆，我们需要最大堆，所以将频率取负值
        max_heap = [-count for count in task_counts.values()]
        heapq.heapify(max_heap)
        
        time_count = 0  # 总时间计数器
        
        # 当堆不为空时继续处理
        while max_heap:
            current_time_slot = 0  # 当前时间片中处理的任务数
            temp = []  # 临时保存本时间片中处理过的任务频率
            
            # 尝试在当前时间片（n+1个连续时间单位）中处理尽可能多的任务
            while current_time_slot <= n and max_heap:
                count = -heapq.heappop(max_heap)  # 取出频率最高的任务
                count -= 1  # 执行一次该任务，频率减1
                
                if count > 0:  # 如果任务还有剩余次数，保存到临时列表
                    temp.append(-count)
                
                current_time_slot += 1  # 当前时间片处理的任务数加1
            
            # 将剩余任务放回堆中
            for count in temp:
                heapq.heappush(max_heap, count)
            
            # 计算本次时间片消耗的时间：
            # 如果堆不为空，说明还有任务需要处理，本次时间片消耗n+1个时间单位
            # 如果堆为空，说明所有任务都处理完了，本次时间片只消耗实际处理的任务数
            if max_heap:
                time_count += (n + 1)
            else:
                time_count += current_time_slot
        
        return time_count
    
    def least_interval_optimal(self, tasks: List[str], n: int) -> int:
        """
        优化解法：数学公式推导
        
        Args:
            tasks: 任务列表
            n: 冷却时间
            
        Returns:
            完成所有任务所需的最短时间
        """
        # 特殊情况处理
        if not tasks:
            return 0
        
        if n == 0:
            return len(tasks)  # 没有冷却时间，直接返回任务数量
        
        # 统计每个任务的频率
        task_counts = Counter(tasks)
        
        # 找出最高频率和具有最高频率的任务数量
        max_freq = max(task_counts.values())
        max_freq_count = sum(1 for count in task_counts.values() if count == max_freq)
        
        # 计算最小时间：由最高频率任务决定的最小时间
        min_time = (max_freq - 1) * (n + 1) + max_freq_count
        
        # 最终结果取任务总数和最小时间的较大值
        return max(min_time, len(tasks))
    
    def least_interval(self, tasks: List[str], n: int, use_optimal: bool = True) -> int:
        """
        计算完成所有任务所需的最短时间
        
        Args:
            tasks: 任务列表
            n: 冷却时间
            use_optimal: 是否使用优化解法
            
        Returns:
            完成所有任务所需的最短时间
        """
        if use_optimal:
            return self.least_interval_optimal(tasks, n)
        else:
            return self.least_interval_heap(tasks, n)

# 测试代码
def test_task_scheduler():
    print("开始测试任务调度器...")
    scheduler = TaskScheduler()
    
    # 测试用例1：基本情况
    tasks1 = ['A', 'A', 'A', 'B', 'B', 'B']
    n1 = 2
    result1_heap = scheduler.least_interval(tasks1, n1, False)
    result1_optimal = scheduler.least_interval(tasks1, n1, True)
    print(f"测试用例1：")
    print(f"  任务: {tasks1}, 冷却时间: {n1}")
    print(f"  堆解法结果: {result1_heap}, 期望: 8")
    print(f"  优化解法结果: {result1_optimal}, 期望: 8")
    print(f"  测试通过: {result1_heap == 8 and result1_optimal == 8}")
    
    # 测试用例2：没有冷却时间
    tasks2 = ['A', 'A', 'A', 'B', 'B', 'B']
    n2 = 0
    result2_heap = scheduler.least_interval(tasks2, n2, False)
    result2_optimal = scheduler.least_interval(tasks2, n2, True)
    print(f"\n测试用例2：")
    print(f"  任务: {tasks2}, 冷却时间: {n2}")
    print(f"  堆解法结果: {result2_heap}, 期望: 6")
    print(f"  优化解法结果: {result2_optimal}, 期望: 6")
    print(f"  测试通过: {result2_heap == 6 and result2_optimal == 6}")
    
    # 测试用例3：只有一种任务
    tasks3 = ['A', 'A', 'A', 'A', 'A', 'A']
    n3 = 2
    result3_heap = scheduler.least_interval(tasks3, n3, False)
    result3_optimal = scheduler.least_interval(tasks3, n3, True)
    print(f"\n测试用例3：")
    print(f"  任务: {tasks3}, 冷却时间: {n3}")
    print(f"  堆解法结果: {result3_heap}, 期望: 16")
    print(f"  优化解法结果: {result3_optimal}, 期望: 16")
    print(f"  测试通过: {result3_heap == 16 and result3_optimal == 16}")
    
    # 测试用例4：任务种类足够多，无需等待
    tasks4 = ['A', 'A', 'A', 'B', 'B', 'B', 'C', 'C', 'D', 'D']
    n4 = 2
    result4_heap = scheduler.least_interval(tasks4, n4, False)
    result4_optimal = scheduler.least_interval(tasks4, n4, True)
    print(f"\n测试用例4：")
    print(f"  任务: {tasks4}, 冷却时间: {n4}")
    print(f"  堆解法结果: {result4_heap}, 期望: 10")
    print(f"  优化解法结果: {result4_optimal}, 期望: 10")
    print(f"  测试通过: {result4_heap == 10 and result4_optimal == 10}")
    
    # 测试用例5：边界情况 - 空数组
    tasks5 = []
    n5 = 2
    result5_heap = scheduler.least_interval(tasks5, n5, False)
    result5_optimal = scheduler.least_interval(tasks5, n5, True)
    print(f"\n测试用例5：")
    print(f"  任务: {tasks5}, 冷却时间: {n5}")
    print(f"  堆解法结果: {result5_heap}, 期望: 0")
    print(f"  优化解法结果: {result5_optimal}, 期望: 0")
    print(f"  测试通过: {result5_heap == 0 and result5_optimal == 0}")
    
    # 测试用例6：多种任务，频率各不相同
    tasks6 = ['A', 'A', 'A', 'B', 'B', 'C', 'C', 'C', 'C', 'D', 'E', 'F']
    n6 = 3
    result6_heap = scheduler.least_interval(tasks6, n6, False)
    result6_optimal = scheduler.least_interval(tasks6, n6, True)
    print(f"\n测试用例6：")
    print(f"  任务: {tasks6}, 冷却时间: {n6}")
    print(f"  堆解法结果: {result6_heap}")
    print(f"  优化解法结果: {result6_optimal}")
    print(f"  解法结果一致: {result6_heap == result6_optimal}")

# 性能测试
def performance_test():
    print("\n开始性能测试...")
    scheduler = TaskScheduler()
    
    # 生成大规模测试数据
    def generate_large_tasks(size, num_types=10):
        """生成大规模任务数据"""
        tasks = []
        for i in range(size):
            task = chr(65 + (i % num_types))  # 生成A-Z的任务
            tasks.append(task)
        return tasks
    
    # 测试不同规模的数据
    sizes = [100, 1000, 10000, 100000]
    n = 5
    
    for size in sizes:
        print(f"\n测试规模: {size}个任务")
        tasks = generate_large_tasks(size)
        
        # 测试堆解法性能
        start_time = time.time()
        result_heap = scheduler.least_interval(tasks, n, False)
        heap_time = time.time() - start_time
        print(f"  堆解法: 结果={result_heap}, 耗时={heap_time:.6f}秒")
        
        # 测试优化解法性能
        start_time = time.time()
        result_optimal = scheduler.least_interval(tasks, n, True)
        optimal_time = time.time() - start_time
        print(f"  优化解法: 结果={result_optimal}, 耗时={optimal_time:.6f}秒")
        
        # 计算性能提升
        speedup = heap_time / optimal_time if optimal_time > 0 else float('inf')
        print(f"  性能提升: 优化解法比堆解法快约 {speedup:.2f} 倍")
        
        # 验证结果一致性
        print(f"  结果一致性: {result_heap == result_optimal}")
    
    # 测试不同冷却时间的影响
    print("\n测试不同冷却时间的影响:")
    tasks = generate_large_tasks(10000)
    cool_down_times = [0, 1, 2, 5, 10, 20]
    
    for cool_down in cool_down_times:
        result = scheduler.least_interval(tasks, cool_down, True)
        print(f"  冷却时间={cool_down}, 最短时间={result}")

# 可视化算法执行过程（用于教学目的）
def visualize_algorithm(tasks, n):
    """可视化任务调度算法的执行过程"""
    print(f"\n可视化任务调度过程: 任务={tasks}, 冷却时间={n}")
    
    # 统计任务频率
    task_counts = Counter(tasks)
    max_heap = [(-count, task) for task, count in task_counts.items()]
    heapq.heapify(max_heap)
    
    time_count = 0
    execution_order = []
    
    while max_heap:
        current_time_slot = 0
        temp = []
        
        while current_time_slot <= n and max_heap:
            neg_count, task = heapq.heappop(max_heap)
            count = -neg_count
            count -= 1
            
            execution_order.append(task)
            
            if count > 0:
                temp.append((-count, task))
            
            current_time_slot += 1
        
        # 添加空闲时间
        while current_time_slot <= n:
            execution_order.append('IDLE')
            current_time_slot += 1
        
        # 将剩余任务放回堆中
        for item in temp:
            heapq.heappush(max_heap, item)
        
        time_count += (n + 1) if max_heap else current_time_slot
    
    print(f"执行顺序: {' -> '.join(execution_order[:time_count])}")
    print(f"总时间: {time_count}")

if __name__ == "__main__":
    # 运行基本测试
    test_task_scheduler()
    
    # 运行可视化示例
    visualize_algorithm(['A', 'A', 'A', 'B', 'B', 'B'], 2)
    
    # 运行性能测试
    performance_test()

"""
Python语言特性与优化：

1. 使用collections.Counter高效统计任务频率
2. 利用heapq模块实现优先队列功能，通过取负值模拟最大堆
3. 使用列表推导式和生成器表达式简化代码
4. 使用类型提示增强代码可读性和IDE支持
5. 使用docstring详细说明函数功能、参数和返回值

工程化建议：

1. 代码组织：
   - 使用类封装相关功能，提高代码的模块化和可复用性
   - 将不同的解法实现为独立方法，便于比较和选择
   - 提供统一的接口，通过参数控制使用哪种解法

2. 测试与验证：
   - 实现全面的测试用例，覆盖基本情况、边界情况和特殊情况
   - 添加性能测试，验证算法在不同规模数据下的表现
   - 可视化算法执行过程，便于理解和调试

3. 异常处理：
   - 处理空输入和特殊情况
   - 在实际应用中可以添加参数验证和异常抛出

4. 性能优化：
   - 对于大规模数据，优先使用数学公式优化解法
   - 合理使用Python内置数据结构和算法
   - 避免不必要的计算和数据复制

5. 扩展性考虑：
   - 可以扩展支持任务优先级
   - 可以扩展支持任务执行时间不同的情况
   - 可以添加更复杂的调度策略

Python特有的调试技巧：

1. 使用print语句或logging模块输出中间状态
2. 使用pdb模块进行交互式调试
3. 使用cProfile或line_profiler分析性能瓶颈
4. 使用assert语句验证中间结果
5. 使用ipython或jupyter notebook进行交互式开发和调试

算法本质理解：

1. 贪心策略的核心是每次选择当前最优解，即处理剩余频率最高的任务
2. 堆解法直观地模拟了任务调度过程，易于理解但效率较低
3. 数学公式解法基于对问题的深入分析，发现了决定最短时间的关键因素
4. 两种解法结果一致，但在时间和空间复杂度上有很大差异
"""