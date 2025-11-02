# 任务调度器
# 给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。
# 任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。
# 在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。
# 然而，两个相同种类的任务之间必须有长度为整数 n 的冷却时间，
# 因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
# 你需要计算完成所有任务所需要的最短时间。
# 测试链接: https://leetcode.cn/problems/task-scheduler/

import heapq
from collections import Counter
from typing import List

class Solution:
    def leastInterval(self, tasks: List[str], n: int) -> int:
        """
        任务调度器问题的贪心解法
        
        解题思路：
        1. 统计每个任务的出现频率
        2. 找到出现频率最高的任务，计算需要的最少时间
        3. 考虑冷却时间的影响，计算实际需要的时间
        
        贪心策略的正确性：
        局部最优：优先安排出现次数最多的任务，这样可以减少冷却时间的浪费
        全局最优：完成所有任务所需的最短时间
        
        时间复杂度：O(n)，其中n是任务数量
        空间复杂度：O(1)，因为任务种类最多26个
        
        Args:
            tasks: 任务数组
            n: 冷却时间
            
        Returns:
            完成所有任务所需的最短时间
        """
        # 边界条件处理
        if not tasks:
            return 0
        if n == 0:
            return len(tasks)
        
        # 统计每个任务的频率
        freq = [0] * 26
        for task in tasks:
            freq[ord(task) - ord('A')] += 1
        
        # 找到最大频率
        max_freq = max(freq)
        
        # 统计具有最大频率的任务数量
        max_count = 0
        for count in freq:
            if count == max_freq:
                max_count += 1
        
        # 计算最短时间
        # 公式：(max_freq - 1) * (n + 1) + max_count
        # 解释：
        # - (max_freq - 1) * (n + 1)：安排前max_freq-1轮任务
        # - max_count：最后一轮任务的数量
        result = (max_freq - 1) * (n + 1) + max_count
        
        # 如果计算结果小于任务总数，说明冷却时间不够，需要更多时间
        # 但实际上这种情况不会发生，因为公式已经考虑了最坏情况
        # 这里取最大值是为了确保正确性
        return max(result, len(tasks))
    
    def leastInterval2(self, tasks: List[str], n: int) -> int:
        """
        任务调度器问题的另一种解法（使用最大堆）
        
        解题思路：
        1. 使用最大堆来存储任务频率
        2. 每次从堆中取出n+1个任务（如果可用）
        3. 执行任务后，如果还有剩余次数，重新加入堆中
        4. 重复直到所有任务完成
        
        时间复杂度：O(n log k)，其中k是任务种类数
        空间复杂度：O(k)
        """
        if not tasks:
            return 0
        if n == 0:
            return len(tasks)
        
        # 统计任务频率
        freq_map = Counter(tasks)
        
        # 使用最大堆存储任务频率（Python的heapq是最小堆，所以用负数）
        max_heap = [-count for count in freq_map.values()]
        heapq.heapify(max_heap)
        
        time = 0
        
        while max_heap:
            temp = []
            
            # 尝试执行n+1个任务
            for i in range(n + 1):
                if max_heap:
                    count = -heapq.heappop(max_heap)
                    if count > 1:
                        temp.append(count - 1)
                time += 1
                
                # 如果堆为空且没有待执行的任务，结束循环
                if not max_heap and not temp:
                    break
            
            # 将剩余任务重新加入堆中
            for count in temp:
                heapq.heappush(max_heap, -count)
        
        return time

# 测试代码
def test_least_interval():
    solution = Solution()
    
    # 测试用例1
    # 输入: tasks = ["A","A","A","B","B","B"], n = 2
    # 输出: 8
    # 解释: A -> B -> (待命) -> A -> B -> (待命) -> A -> B
    tasks1 = ["A", "A", "A", "B", "B", "B"]
    print(f"测试用例1结果: {solution.leastInterval(tasks1, 2)}")  # 期望输出: 8
    
    # 测试用例2
    # 输入: tasks = ["A","A","A","B","B","B"], n = 0
    # 输出: 6
    # 解释: 没有冷却时间，可以连续执行
    tasks2 = ["A", "A", "A", "B", "B", "B"]
    print(f"测试用例2结果: {solution.leastInterval(tasks2, 0)}")  # 期望输出: 6
    
    # 测试用例3
    # 输入: tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
    # 输出: 16
    # 解释: 一种可能的解决方案是：A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> (待命) -> (待命) -> A -> (待命) -> (待命) -> A
    tasks3 = ["A", "A", "A", "A", "A", "A", "B", "C", "D", "E", "F", "G"]
    print(f"测试用例3结果: {solution.leastInterval(tasks3, 2)}")  # 期望输出: 16
    
    # 测试用例4：边界情况
    # 输入: tasks = ["A"], n = 1
    # 输出: 1
    tasks4 = ["A"]
    print(f"测试用例4结果: {solution.leastInterval(tasks4, 1)}")  # 期望输出: 1
    
    # 测试用例5：复杂情况
    # 输入: tasks = ["A","A","A","B","B","B","C","C","C","D","D","E"], n = 2
    # 输出: 12
    tasks5 = ["A", "A", "A", "B", "B", "B", "C", "C", "C", "D", "D", "E"]
    print(f"测试用例5结果: {solution.leastInterval(tasks5, 2)}")  # 期望输出: 12

if __name__ == "__main__":
    test_least_interval()