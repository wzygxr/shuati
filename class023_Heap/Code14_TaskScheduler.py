import heapq
from collections import Counter

class Solution:
    """
    相关题目14: LeetCode 621. 任务调度器
    题目链接: https://leetcode.cn/problems/task-scheduler/
    题目描述: 给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。
    任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。
    然而，两个相同种类的任务之间必须有长度为 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
    请你计算完成所有任务所需要的最短时间。
    解题思路: 使用最大堆按任务频率排序，然后贪心安排任务
    时间复杂度: O(m log k)，其中m是总任务数，k是不同任务的数量（最大为26）
    空间复杂度: O(k)，用于存储任务频率和堆
    是否最优解: 此方法是最优解，还有数学公式可以直接计算
    
    本题属于堆的应用场景：任务调度问题
    """
    
    def leastInterval(self, tasks: list[str], n: int) -> int:
        """
        计算完成所有任务所需要的最短时间
        
        Args:
            tasks: 任务列表，每个元素是一个字符表示的任务
            n: 相同任务之间的冷却时间
            
        Returns:
            int: 完成所有任务所需的最短时间
        """
        # 异常处理：检查输入参数
        if not tasks:
            return 0
        
        if n < 0:
            raise ValueError("冷却时间不能为负数")
        
        # 统计每个任务的频率
        task_counts = Counter(tasks)
        
        # 创建最大堆（使用负数表示频率，因为Python的heapq是最小堆）
        max_heap = [-count for count in task_counts.values()]
        heapq.heapify(max_heap)
        
        # 当前时间
        time = 0
        
        # 当堆不为空时，继续安排任务
        while max_heap:
            # 用于暂时存储本轮安排的任务（执行后需要放回堆中的任务）
            temp = []
            # 当前轮次需要安排的任务数（最多n+1个不同任务）
            cycle = n + 1
            
            # 尝试安排cycle个任务
            while cycle > 0 and max_heap:
                # 获取当前频率最高的任务
                count = -heapq.heappop(max_heap)
                # 如果任务执行后还有剩余次数，将剩余次数保存到temp中
                if count > 1:
                    temp.append(-(count - 1))
                # 时间增加1
                time += 1
                # 减少本轮可安排的任务数
                cycle -= 1
            
            # 将本轮执行后还有剩余次数的任务放回堆中
            for count in temp:
                heapq.heappush(max_heap, count)
            
            # 如果堆不为空，说明还有任务未完成，需要添加冷却时间
            # 即当前轮次剩下的cycle个时间单位都需要待命
            if max_heap:
                time += cycle
        
        return time

class MathSolution:
    """
    使用数学公式直接计算最短时间的解决方案
    这种方法更高效，时间复杂度为O(m)，其中m是任务总数
    """
    
    def leastInterval(self, tasks: list[str], n: int) -> int:
        """
        使用数学公式计算完成所有任务所需要的最短时间
        
        公式推导：
        1. 假设频率最高的任务的频率为max_freq
        2. 至少需要(max_freq - 1) * (n + 1) + 1的时间
        3. 如果有多个频率为max_freq的任务，需要加上这些任务的数量-1
        4. 最终结果取上述值和任务总数的最大值
        
        Args:
            tasks: 任务列表
            n: 冷却时间
            
        Returns:
            int: 最短时间
        """
        # 异常处理
        if not tasks:
            return 0
        
        if n < 0:
            raise ValueError("冷却时间不能为负数")
        
        # 统计任务频率
        task_counts = Counter(tasks)
        
        # 找到最高频率
        max_freq = max(task_counts.values())
        
        # 计算有多少个任务有最高频率
        max_freq_tasks = sum(1 for count in task_counts.values() if count == max_freq)
        
        # 计算根据公式的最短时间
        # (max_freq - 1) * (n + 1) 是安排max_freq-1个批次的时间
        # 每个批次有n+1个时间单位（执行一个任务，然后冷却n个单位）
        # 最后加上max_freq_tasks个任务（最后一个批次）
        formula_time = (max_freq - 1) * (n + 1) + max_freq_tasks
        
        # 实际最短时间不能少于任务总数
        return max(formula_time, len(tasks))

class OptimizedHeapSolution:
    """
    优化的堆实现，使用队列来跟踪冷却中的任务
    这种方法更直观地模拟了任务调度的过程
    """
    
    def leastInterval(self, tasks: list[str], n: int) -> int:
        """
        使用优化的堆和队列实现任务调度
        
        Args:
            tasks: 任务列表
            n: 冷却时间
            
        Returns:
            int: 最短时间
        """
        # 异常处理
        if not tasks:
            return 0
        
        if n < 0:
            raise ValueError("冷却时间不能为负数")
        
        # 统计任务频率
        task_counts = Counter(tasks)
        
        # 创建最大堆
        max_heap = [-count for count in task_counts.values()]
        heapq.heapify(max_heap)
        
        # 使用队列跟踪冷却中的任务
        # 每个元素是(剩余次数, 可用时间)
        cooldown = []
        
        time = 0
        
        while max_heap or cooldown:
            # 将冷却时间到期的任务放回堆中
            while cooldown and cooldown[0][1] == time:
                heapq.heappush(max_heap, cooldown[0][0])
                cooldown.pop(0)
            
            # 尝试执行一个任务
            if max_heap:
                # 获取并减少最高频率任务的次数
                count = -heapq.heappop(max_heap)
                count -= 1
                
                # 如果还有剩余次数，将其加入冷却队列
                if count > 0:
                    # 可用时间 = 当前时间 + 冷却时间 + 1（执行时间）
                    next_available_time = time + n + 1
                    cooldown.append((-count, next_available_time))
            
            # 时间增加1
            time += 1
        
        return time

# 测试函数，验证算法在不同输入情况下的正确性
def test_least_interval():
    print("=== 测试任务调度器算法 ===")
    heap_solution = Solution()
    math_solution = MathSolution()
    optimized_solution = OptimizedHeapSolution()
    
    # 测试用例1：基本用例
    print("\n测试用例1：基本用例")
    tasks1 = ["A","A","A","B","B","B"]
    n1 = 2
    result1_heap = heap_solution.leastInterval(tasks1, n1)
    result1_math = math_solution.leastInterval(tasks1, n1)
    result1_opt = optimized_solution.leastInterval(tasks1, n1)
    
    print(f"任务列表: {tasks1}, 冷却时间: {n1}")
    print(f"堆方法结果: {result1_heap}")
    print(f"数学公式结果: {result1_math}")
    print(f"优化堆方法结果: {result1_opt}")
    
    # 测试用例2：所有任务都相同
    print("\n测试用例2：所有任务都相同")
    tasks2 = ["A","A","A","A"]
    n2 = 2
    result2_heap = heap_solution.leastInterval(tasks2, n2)
    result2_math = math_solution.leastInterval(tasks2, n2)
    result2_opt = optimized_solution.leastInterval(tasks2, n2)
    
    print(f"任务列表: {tasks2}, 冷却时间: {n2}")
    print(f"堆方法结果: {result2_heap}")
    print(f"数学公式结果: {result2_math}")
    print(f"优化堆方法结果: {result2_opt}")
    
    # 测试用例3：冷却时间为0
    print("\n测试用例3：冷却时间为0")
    tasks3 = ["A","A","A","B","B","B"]
    n3 = 0
    result3_heap = heap_solution.leastInterval(tasks3, n3)
    result3_math = math_solution.leastInterval(tasks3, n3)
    result3_opt = optimized_solution.leastInterval(tasks3, n3)
    
    print(f"任务列表: {tasks3}, 冷却时间: {n3}")
    print(f"堆方法结果: {result3_heap}")
    print(f"数学公式结果: {result3_math}")
    print(f"优化堆方法结果: {result3_opt}")
    
    # 测试用例4：多种任务且频率不同
    print("\n测试用例4：多种任务且频率不同")
    tasks4 = ["A","A","A","B","B","B","C","C","C","D","D"]
    n4 = 2
    result4_heap = heap_solution.leastInterval(tasks4, n4)
    result4_math = math_solution.leastInterval(tasks4, n4)
    result4_opt = optimized_solution.leastInterval(tasks4, n4)
    
    print(f"任务列表: {tasks4}, 冷却时间: {n4}")
    print(f"堆方法结果: {result4_heap}")
    print(f"数学公式结果: {result4_math}")
    print(f"优化堆方法结果: {result4_opt}")
    
    # 测试用例5：只有一种任务
    print("\n测试用例5：只有一种任务")
    tasks5 = ["A"]
    n5 = 100
    result5_heap = heap_solution.leastInterval(tasks5, n5)
    result5_math = math_solution.leastInterval(tasks5, n5)
    result5_opt = optimized_solution.leastInterval(tasks5, n5)
    
    print(f"任务列表: {tasks5}, 冷却时间: {n5}")
    print(f"堆方法结果: {result5_heap}")
    print(f"数学公式结果: {result5_math}")
    print(f"优化堆方法结果: {result5_opt}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    import time
    
    # 创建一个大任务列表
    large_tasks = []
    # 添加1000个A任务
    large_tasks.extend(["A"] * 1000)
    # 添加500个B任务
    large_tasks.extend(["B"] * 500)
    # 添加300个C任务
    large_tasks.extend(["C"] * 300)
    # 添加200个D任务
    large_tasks.extend(["D"] * 200)
    # 添加100个E任务
    large_tasks.extend(["E"] * 100)
    
    n_large = 5
    
    # 测试堆方法性能
    start_time = time.time()
    large_result_heap = heap_solution.leastInterval(large_tasks, n_large)
    heap_time = time.time() - start_time
    print(f"堆方法处理大任务列表用时: {heap_time:.6f}秒, 结果: {large_result_heap}")
    
    # 测试数学公式方法性能
    start_time = time.time()
    large_result_math = math_solution.leastInterval(large_tasks, n_large)
    math_time = time.time() - start_time
    print(f"数学公式方法处理大任务列表用时: {math_time:.6f}秒, 结果: {large_result_math}")
    
    # 测试优化堆方法性能
    start_time = time.time()
    large_result_opt = optimized_solution.leastInterval(large_tasks, n_large)
    opt_time = time.time() - start_time
    print(f"优化堆方法处理大任务列表用时: {opt_time:.6f}秒, 结果: {large_result_opt}")
    
    # 性能比较
    print("\n性能比较:")
    print(f"堆方法 vs 数学公式方法: 数学公式方法更快 约 {(heap_time / math_time):.2f}倍")
    print(f"堆方法 vs 优化堆方法: {'优化堆方法更快' if opt_time < heap_time else '堆方法更快'} 约 {(max(heap_time, opt_time) / min(heap_time, opt_time)):.2f}倍")
    print(f"数学公式方法 vs 优化堆方法: 数学公式方法更快 约 {(opt_time / math_time):.2f}倍")

# 运行测试
if __name__ == "__main__":
    test_least_interval()

# 解题思路总结：
# 1. 问题分析：
#    - 需要安排任务使得相同任务之间至少有n个冷却时间
#    - 目标是找到完成所有任务的最短时间
#    - 关键观察：频率最高的任务决定了整体调度的框架
# 
# 2. 堆方法（优先队列）：
#    - 统计每个任务的频率
#    - 将任务频率放入最大堆
#    - 每次从堆中取出频率最高的任务执行
#    - 执行后，如果任务还有剩余次数，将其保存并在冷却时间后放回堆中
#    - 时间复杂度：O(m log k)，其中m是任务总数，k是不同任务的数量
#    - 空间复杂度：O(k)
# 
# 3. 数学公式方法：
#    - 观察到最短时间由两个因素决定：
#      a) 最高频率任务所需要的时间框架
#      b) 任务的总数
#    - 公式：max( (max_freq - 1) * (n + 1) + max_freq_tasks, total_tasks )
#    - 时间复杂度：O(m)
#    - 空间复杂度：O(k)
#    - 这是最优解法
# 
# 4. 优化的堆实现（使用队列）：
#    - 使用堆跟踪可执行的任务
#    - 使用队列跟踪冷却中的任务
#    - 每个时间单位，先检查冷却中的任务是否可以执行，然后执行一个任务（如果有）
#    - 这种方法更直观地模拟了任务调度的过程
#    - 时间复杂度：O(m log k)
#    - 空间复杂度：O(k)
# 
# 5. 边界情况处理：
#    - 空任务列表
#    - 冷却时间为0
#    - 只有一种任务
#    - 任务数量少于最大频率 * (n + 1)
# 
# 6. 堆方法的优势：
#    - 可以灵活处理不同优先级的任务调度
#    - 能够直观地模拟任务执行和冷却的过程
#    - 适用于更复杂的调度场景
# 
# 7. 应用场景：
#    - CPU任务调度
#    - 网络请求调度
#    - 资源分配问题
#    - 任何需要考虑冷却时间或优先级的调度问题