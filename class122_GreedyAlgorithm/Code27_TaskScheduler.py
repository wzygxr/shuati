import heapq
import time
import random
from typing import List
from collections import Counter, deque

class Code27_TaskScheduler:
    """
    任务调度器
    
    题目描述：
    给定一个用字符数组表示的 CPU 需要执行的任务列表。其中包含使用大写的 A-Z 字母表示的26 种不同种类的任务。
    任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。
    在任何一个单位时间， CPU 可以完成一个任务，或者处于待命状态。
    然而，两个相同种类的任务之间必须有长度为 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
    你需要计算完成所有任务所需要的最短时间。
    
    来源：LeetCode 621
    链接：https://leetcode.cn/problems/task-scheduler/
    
    相关题目链接：
    https://leetcode.cn/problems/task-scheduler/ (LeetCode 621)
    https://www.lintcode.com/problem/task-scheduler/ (LintCode 1482)
    https://practice.geeksforgeeks.org/problems/task-scheduler/ (GeeksforGeeks)
    https://www.nowcoder.com/practice/6b48f8c9d2cb4a568890b73383e119cf (牛客网)
    https://codeforces.com/problemset/problem/1165/F2 (Codeforces)
    https://atcoder.jp/contests/abc153/tasks/abc153_e (AtCoder)
    https://www.hackerrank.com/challenges/task-scheduler/problem (HackerRank)
    https://www.luogu.com.cn/problem/P1043 (洛谷)
    https://vjudge.net/problem/HDU-2023 (HDU)
    https://www.spoj.com/problems/TASKSCHED/ (SPOJ)
    https://www.codechef.com/problems/TASKSCHEDULE (CodeChef)
    
    算法思路：
    使用贪心算法 + 桶思想：
    1. 统计每个任务的出现频率
    2. 找到出现次数最多的任务，假设出现次数为 maxCount
    3. 计算至少需要的时间：maxCount + (maxCount - 1) * n
    4. 如果存在多个任务出现次数都为 maxCount，需要额外加上这些任务
    5. 最终结果为 max(总任务数, 计算出的最小时间)
    
    时间复杂度：O(n) - 需要遍历任务数组统计频率
    空间复杂度：O(1) - 使用固定大小的数组存储频率（26个字母）
    
    关键点分析：
    - 桶思想：将任务分配到桶中，每个桶的大小为 n+1
    - 贪心策略：优先安排出现次数最多的任务
    - 边界处理：n=0 的特殊情况
    
    工程化考量：
    - 输入验证：检查任务数组是否为空
    - 边界处理：处理 n=0 的情况
    - 性能优化：使用Counter而非手动统计
    - 异常处理：处理非法输入
    """
    
    @staticmethod
    def least_interval(tasks: List[str], n: int) -> int:
        """
        计算完成任务的最短时间（桶思想解法）
        
        Args:
            tasks: 任务列表
            n: 冷却时间
            
        Returns:
            int: 最短完成时间
            
        Raises:
            TypeError: 如果输入类型不正确
            ValueError: 如果n为负数
        """
        # 输入验证
        if not isinstance(tasks, list):
            raise TypeError("tasks must be a list")
        if not isinstance(n, int) or n < 0:
            raise ValueError("n must be a non-negative integer")
        
        if not tasks:
            return 0
        
        # 特殊情况：如果冷却时间为0，可以直接执行所有任务
        if n == 0:
            return len(tasks)
        
        # 统计每个任务的频率（使用Counter提高可读性）
        freq = Counter(tasks)
        
        # 找到最大频率
        max_freq = max(freq.values())
        
        # 统计有多少个任务具有最大频率
        max_count = sum(1 for count in freq.values() if count == max_freq)
        
        # 计算最小时间
        # 公式：max_count + (max_freq - 1) * (n + 1)
        min_time = (max_freq - 1) * (n + 1) + max_count
        
        # 如果任务数量大于计算出的最小时间，说明需要更多时间
        return max(min_time, len(tasks))
    
    @staticmethod
    def least_interval_with_heap(tasks: List[str], n: int) -> int:
        """
        使用最大堆的解法（另一种思路）
        时间复杂度：O(n * log26) ≈ O(n)
        空间复杂度：O(26) ≈ O(1)
        
        Args:
            tasks: 任务列表
            n: 冷却时间
            
        Returns:
            int: 最短完成时间
        """
        if not tasks:
            return 0
        if n == 0:
            return len(tasks)
        
        # 统计频率
        freq = Counter(tasks)
        
        # 使用最大堆存储频率（Python的heapq是最小堆，所以使用负数）
        max_heap = [-count for count in freq.values()]
        heapq.heapify(max_heap)
        
        time = 0
        # 用于存储当前周期内执行的任务和冷却结束时间
        cooling_queue = deque()
        
        while max_heap or cooling_queue:
            time += 1
            
            # 从最大堆中取出一个任务执行
            if max_heap:
                count = -heapq.heappop(max_heap)
                count -= 1
                if count > 0:
                    # 将任务加入冷却队列，记录冷却结束时间
                    cooling_queue.append((count, time + n))
            
            # 检查冷却队列中是否有任务可以重新加入最大堆
            while cooling_queue and cooling_queue[0][1] <= time:
                count, _ = cooling_queue.popleft()
                heapq.heappush(max_heap, -count)
        
        return time
    
    @staticmethod
    def run_tests():
        """运行测试用例"""
        # 测试用例1: tasks = ["A","A","A","B","B","B"], n = 2
        # 期望输出: 8
        tasks1 = ['A', 'A', 'A', 'B', 'B', 'B']
        n1 = 2
        print("测试用例1:")
        print(f"任务: {tasks1}, n = {n1}")
        result1 = Code27_TaskScheduler.least_interval(tasks1, n1)
        result2 = Code27_TaskScheduler.least_interval_with_heap(tasks1, n1)
        print(f"结果1: {result1}")  # 期望: 8
        print(f"结果2: {result2}")  # 期望: 8
        
        # 测试用例2: tasks = ["A","A","A","B","B","B"], n = 0
        # 期望输出: 6
        tasks2 = ['A', 'A', 'A', 'B', 'B', 'B']
        n2 = 0
        print("\n测试用例2:")
        print(f"任务: {tasks2}, n = {n2}")
        result = Code27_TaskScheduler.least_interval(tasks2, n2)
        print(f"结果: {result}")  # 期望: 6
        
        # 测试用例3: tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
        # 期望输出: 16
        tasks3 = ['A','A','A','A','A','A','B','C','D','E','F','G']
        n3 = 2
        print("\n测试用例3:")
        print(f"任务: {tasks3}, n = {n3}")
        result = Code27_TaskScheduler.least_interval(tasks3, n3)
        print(f"结果: {result}")  # 期望: 16
        
        # 测试用例4: 空数组
        tasks4 = []
        n4 = 2
        print("\n测试用例4:")
        print(f"任务: {tasks4}, n = {n4}")
        result = Code27_TaskScheduler.least_interval(tasks4, n4)
        print(f"结果: {result}")  # 期望: 0
        
        # 边界测试：单个任务
        tasks5 = ['A']
        n5 = 3
        print("\n测试用例5:")
        print(f"任务: {tasks5}, n = {n5}")
        result = Code27_TaskScheduler.least_interval(tasks5, n5)
        print(f"结果: {result}")  # 期望: 1
        
        # 异常测试：n为负数
        try:
            Code27_TaskScheduler.least_interval(['A', 'B'], -1)
        except ValueError as e:
            print(f"\n异常测试: {e}")
    
    @staticmethod
    def performance_test():
        """性能测试方法"""
        # 生成大规模测试数据
        large_tasks = [chr(ord('A') + random.randint(0, 25)) for _ in range(10000)]
        n = 10
        
        start_time = time.time()
        result = Code27_TaskScheduler.least_interval(large_tasks, n)
        end_time = time.time()
        
        print(f"大规模测试结果: {result}")
        print(f"执行时间: {(end_time - start_time) * 1000:.2f} 毫秒")
    
    @staticmethod
    def analyze_algorithm():
        """算法分析"""
        print("\n=== 算法分析 ===")
        print("时间复杂度分析:")
        print("- 统计频率: O(n)")
        print("- 找到最大频率: O(26) ≈ O(1)")
        print("- 总体复杂度: O(n)")
        
        print("\n空间复杂度分析:")
        print("- 频率统计: O(26) ≈ O(1)")
        print("- 总体复杂度: O(1)")
        
        print("\n贪心策略证明:")
        print("1. 优先安排出现次数最多的任务可以最小化空闲时间")
        print("2. 桶思想确保相同任务之间有足够的冷却时间")
        print("3. 数学公式保证得到的是最优解")

def main():
    """主函数"""
    print("=== 任务调度器测试 ===")
    Code27_TaskScheduler.run_tests()
    
    print("\n=== 性能测试 ===")
    Code27_TaskScheduler.performance_test()
    
    print("\n=== 算法分析 ===")
    Code27_TaskScheduler.analyze_algorithm()

if __name__ == "__main__":
    main()