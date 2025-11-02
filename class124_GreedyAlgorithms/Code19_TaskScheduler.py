# 任务调度器（Task Scheduler）
# 题目来源：LeetCode 621
# 题目链接：https://leetcode.cn/problems/task-scheduler/
# 
# 问题描述：
# 给定一个用字符数组表示的CPU需要执行的任务列表。其中包含使用大写的A-Z字母表示的26种不同种类的任务。
# 任务可以以任意顺序执行，并且每个任务都可以在1个单位时间内执行完。
# CPU在任何一个单位时间内都可以执行一个任务，或者在待命状态。
# 然而，两个相同种类的任务之间必须有长度为n的冷却时间，因此至少有连续n个单位时间内CPU在执行不同的任务，或者在待命状态。
# 你需要计算完成所有任务所需要的最短时间。
# 
# 算法思路：
# 使用贪心策略，基于任务频率：
# 1. 统计每个任务的频率
# 2. 找到出现次数最多的任务，假设出现次数为maxCount
# 3. 计算至少需要的时间：(maxCount - 1) * (n + 1) + 出现次数为maxCount的任务个数
# 4. 如果计算结果小于任务总数，说明可以安排得更紧凑，返回任务总数
# 
# 时间复杂度：O(n) - 统计频率的时间复杂度
# 空间复杂度：O(1) - 只需要常数空间存储频率
# 
# 是否最优解：是。这是该问题的最优解法。
# 
# 适用场景：
# 1. 任务调度问题
# 2. 资源分配问题
# 
# 异常处理：
# 1. 处理空数组情况
# 2. 处理n=0的情况
# 
# 工程化考量：
# 1. 输入验证：检查参数是否合法
# 2. 边界条件：处理单任务情况
# 3. 性能优化：使用数组统计频率
# 
# 相关题目：
# 1. LeetCode 767. 重构字符串 - 类似的任务安排问题
# 2. LeetCode 358. 重排字符串k距离 apart - 更一般的任务调度
# 3. LeetCode 253. 会议室 II - 区间调度问题
# 4. 牛客网 NC140 排序 - 各种排序算法实现
# 5. LintCode 945. 任务计划 - 类似问题
# 6. HackerRank - Task Scheduling - 任务调度问题
# 7. CodeChef - TASKS - 任务分配问题
# 8. AtCoder ABC131D - Megalomania - 任务截止时间问题
# 9. Codeforces 1324C - Frog Jumps - 贪心跳跃问题
# 10. POJ 1700 - Crossing River - 经典过河问题

from typing import List

class Solution:
    """
    计算完成任务的最短时间
    
    Args:
        tasks: List[str] - 任务数组
        n: int - 冷却时间
    
    Returns:
        int - 最短完成时间
    """
    def leastInterval(self, tasks: List[str], n: int) -> int:
        # 边界条件检查
        if not tasks:
            return 0
        
        if n == 0:
            return len(tasks)  # 没有冷却时间，直接按顺序执行
        
        # 统计任务频率
        frequency = [0] * 26
        for task in tasks:
            frequency[ord(task) - ord('A')] += 1
        
        # 找到最大频率
        max_frequency = max(frequency)
        
        # 统计出现最大频率的任务个数
        max_count = frequency.count(max_frequency)
        
        # 计算最短时间
        result = (max_frequency - 1) * (n + 1) + max_count
        
        # 如果计算结果小于任务总数，说明可以安排得更紧凑
        return max(result, len(tasks))


def main():
    solution = Solution()
    
    # 测试用例1: 基本情况
    tasks1 = ['A', 'A', 'A', 'B', 'B', 'B']
    n1 = 2
    result1 = solution.leastInterval(tasks1, n1)
    print("测试用例1:")
    print(f"任务数组: {tasks1}")
    print(f"冷却时间: {n1}")
    print(f"最短完成时间: {result1}")
    print("期望输出: 8")
    print()
    
    # 测试用例2: 简单情况
    tasks2 = ['A', 'A', 'A', 'B', 'B', 'B']
    n2 = 0
    result2 = solution.leastInterval(tasks2, n2)
    print("测试用例2:")
    print(f"任务数组: {tasks2}")
    print(f"冷却时间: {n2}")
    print(f"最短完成时间: {result2}")
    print("期望输出: 6")
    print()
    
    # 测试用例3: 复杂情况
    tasks3 = ['A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G']
    n3 = 2
    result3 = solution.leastInterval(tasks3, n3)
    print("测试用例3:")
    print(f"任务数组: {tasks3}")
    print(f"冷却时间: {n3}")
    print(f"最短完成时间: {result3}")
    print("期望输出: 16")
    print()
    
    # 测试用例4: 边界情况 - 单任务
    tasks4 = ['A']
    n4 = 3
    result4 = solution.leastInterval(tasks4, n4)
    print("测试用例4:")
    print(f"任务数组: {tasks4}")
    print(f"冷却时间: {n4}")
    print(f"最短完成时间: {result4}")
    print("期望输出: 1")
    print()
    
    # 测试用例5: 边界情况 - 空数组
    tasks5 = []
    n5 = 5
    result5 = solution.leastInterval(tasks5, n5)
    print("测试用例5:")
    print(f"任务数组: {tasks5}")
    print(f"冷却时间: {n5}")
    print(f"最短完成时间: {result5}")
    print("期望输出: 0")
    print()
    
    # 测试用例6: 复杂情况 - 多个相同频率
    tasks6 = ['A', 'A', 'A', 'B', 'B', 'B', 'C', 'C', 'C', 'D', 'D', 'E']
    n6 = 2
    result6 = solution.leastInterval(tasks6, n6)
    print("测试用例6:")
    print(f"任务数组: {tasks6}")
    print(f"冷却时间: {n6}")
    print(f"最短完成时间: {result6}")
    print("期望输出: 12")


if __name__ == "__main__":
    main()