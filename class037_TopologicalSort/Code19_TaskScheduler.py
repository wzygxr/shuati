import heapq

def least_interval(tasks, n):
    """
    任务调度器 - 贪心+优先队列 - Python实现
    题目解析：安排任务执行顺序，满足冷却时间约束
    
    算法思路：
    1. 统计每个任务的频率
    2. 使用最大堆存储任务频率
    3. 每次选择频率最高的n+1个任务执行
    4. 更新剩余任务频率并重新加入堆中
    
    时间复杂度：O(n log 26)
    空间复杂度：O(26)
    
    工程化考虑：
    1. 频率统计：使用字典统计任务出现次数
    2. 贪心策略：优先安排频率高的任务
    3. 冷却时间：处理任务间隔约束
    4. 边界处理：空任务、单任务等情况
    """
    if len(tasks) == 0:
        return 0
    if n == 0:
        return len(tasks)
    
    # 统计任务频率
    freq = {}
    for task in tasks:
        freq[task] = freq.get(task, 0) + 1
    
    # 使用最大堆存储任务频率（通过负数实现最大堆）
    max_heap = []
    for count in freq.values():
        heapq.heappush(max_heap, -count)
    
    time = 0
    
    while max_heap:
        temp = []
        cycle = n + 1  # 每个周期可以执行的任务数
        
        # 执行一个周期的任务
        for i in range(cycle):
            if max_heap:
                count = -heapq.heappop(max_heap)
                if count > 1:
                    temp.append(count - 1)
            time += 1
            
            # 如果堆为空且没有剩余任务，结束
            if not max_heap and not temp:
                break
        
        # 将剩余任务重新加入堆中
        for count in temp:
            heapq.heappush(max_heap, -count)
    
    return time

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    tasks1 = ['A', 'A', 'A', 'B', 'B', 'B']
    n1 = 2
    print("测试用例1:", least_interval(tasks1, n1))  # 输出: 8
    
    # 测试用例2
    tasks2 = ['A', 'A', 'A', 'B', 'B', 'B']
    n2 = 0
    print("测试用例2:", least_interval(tasks2, n2))  # 输出: 6
    
    # 测试用例3
    tasks3 = ['A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G']
    n3 = 2
    print("测试用例3:", least_interval(tasks3, n3))  # 输出: 16