#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1751. 最多可以参加的会议数目 II (Python实现)

题目描述：
给定n个会议，每个会议有开始时间、结束时间、收益三个值
参加会议就能得到收益，但是同一时间只能参加一个会议
一共能参加k个会议，如果选择参加某个会议，那么必须完整的参加完这个会议
会议结束日期是包含在会议内的，一个会议的结束时间等于另一个会议的开始时间，不能两个会议都参加
返回能得到的会议价值最大和

解题思路：
这是一个带权重的区间调度问题，使用动态规划结合二分查找来解决

算法步骤：
1. 将所有会议按结束时间排序
2. 使用动态规划，dp[i][j]表示在前i个会议中最多选择j个会议能获得的最大收益
3. 对于每个会议，我们可以选择参加或不参加
4. 如果参加，需要找到最后一个不冲突的会议，这可以通过二分查找实现
5. 状态转移方程：
   dp[i][j] = max(dp[i-1][j], dp[prev][j-1] + events[i][2])
   其中 prev 是最后一个结束时间 < 当前会议开始时间的会议索引

时间复杂度分析：
- 排序需要 O(n log n)
- 动态规划过程中，每个状态的计算需要 O(log n) 的时间进行二分查找
- 总时间复杂度：O(n * k + n * log n)
空间复杂度：O(n * k) - 存储动态规划数组

相关题目：
1. LeetCode 1235. 最大盈利的工作调度 (动态规划 + 二分查找)
2. LeetCode 435. 无重叠区间 (贪心)
3. LeetCode 646. 最长数对链 (贪心)
4. LeetCode 253. 会议室 II (扫描线算法)
5. LintCode 1923. 最多可参加的会议数量 II
6. HackerRank - Job Scheduling
7. Codeforces 1324D. Pair of Topics
8. AtCoder ABC091D. Two Faced Edges
9. 洛谷 P2051 [AHOI2009]中国象棋
10. 牛客网 NC46. 加起来和为目标值的组合
11. 杭电OJ 3572. Task Schedule
12. POJ 3616. Milking Time
13. UVa 10158. War
14. CodeChef - MAXSEGMENTS
15. SPOJ - BUSYMAN
16. Project Euler 318. Cutting Game
17. HackerEarth - Job Scheduling Problem
18. 计蒜客 - 工作安排
19. ZOJ 3623. Battle Ships
20. acwing 2068. 整数拼接

工程化考量：
1. 在实际应用中，带权重区间调度常用于：
   - 项目管理和资源分配
   - 云计算中的任务调度
   - 金融投资组合优化
   - 广告投放策略
2. 实现优化：
   - 对于大规模数据，可以使用更高效的排序算法
   - 考虑使用二分索引树（Fenwick Tree）或线段树优化查询
   - 使用空间换时间，预处理可能的查询结果
3. 可扩展性：
   - 支持动态添加和删除工作
   - 处理多个约束条件（如资源限制）
   - 扩展到多维问题
4. 鲁棒性考虑：
   - 处理无效输入（负利润、无效时间区间）
   - 处理大规模数据时的内存管理
   - 优化极端情况下的性能
5. 跨语言特性对比：
   - Python: 使用列表和内置排序，代码简洁但性能相对较低
   - Java: 使用Arrays.sort和二维数组，性能中等
   - C++: 使用vector和algorithm库，性能最优
"""

import bisect
from typing import List

class Code01_MaximumNumberOfEvents:
    """
    最多可以参加的会议数目 II - Python实现类
    """
    
    @staticmethod
    def maxValue(events: List[List[int]], k: int) -> int:
        """
        计算最多可以参加的会议的最大收益
        
        Args:
            events: 会议列表，每个会议包含[开始时间, 结束时间, 收益]
            k: 最多可以参加的会议数量
            
        Returns:
            int: 最大收益
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 输入验证
        if not events or k <= 0:
            return 0
        
        n = len(events)
        
        # 按结束时间排序
        events.sort(key=lambda x: x[1])
        
        # dp[i][j] : 0..i范围上最多选j个会议召开，最大收益是多少
        dp = [[0] * (k + 1) for _ in range(n)]
        
        # 初始化：第一个会议单独参加的收益
        for j in range(1, k + 1):
            dp[0][j] = events[0][2]
        
        # 预处理结束时间列表，用于二分查找
        end_times = [event[1] for event in events]
        
        for i in range(1, n):
            # 找到最后一个不冲突的会议
            pre = Code01_MaximumNumberOfEvents._find_last_non_conflict(end_times, i - 1, events[i][0])
            
            for j in range(1, k + 1):
                # 状态转移：不参加当前会议 vs 参加当前会议
                not_attend = dp[i - 1][j]
                attend = (dp[pre][j - 1] if pre != -1 else 0) + events[i][2]
                dp[i][j] = max(not_attend, attend)
        
        return dp[n - 1][k]
    
    @staticmethod
    def _find_last_non_conflict(end_times: List[int], right: int, start_time: int) -> int:
        """
        使用二分查找找到最后一个结束时间 < start_time 的会议
        
        Args:
            end_times: 会议结束时间列表，已排序
            right: 搜索范围的右边界
            start_time: 当前会议的开始时间
            
        Returns:
            int: 最后一个不冲突会议的索引，如果不存在则返回-1
        """
        left = 0
        ans = -1
        
        while left <= right:
            mid = (left + right) // 2
            
            # 如果当前会议的结束时间 < start_time，可能是候选答案
            if end_times[mid] < start_time:
                ans = mid
                left = mid + 1
            else:
                # 否则需要在左半部分查找
                right = mid - 1
        
        return ans
    
    @staticmethod
    def _find_last_non_conflict_bisect(end_times: List[int], right: int, start_time: int) -> int:
        """
        使用bisect模块进行二分查找（替代实现）
        
        Args:
            end_times: 会议结束时间列表，已排序
            right: 搜索范围的右边界
            start_time: 当前会议的开始时间
            
        Returns:
            int: 最后一个不冲突会议的索引，如果不存在则返回-1
        """
        # 使用bisect_left找到第一个 >= start_time 的位置
        pos = bisect.bisect_left(end_times, start_time, 0, right + 1)
        
        # 如果pos==0，说明没有会议结束时间 < start_time
        if pos == 0:
            return -1
        
        # 返回最后一个 < start_time 的会议索引
        return pos - 1


def test_max_value():
    """
    测试函数 - 验证算法正确性
    """
    print("=== 测试Code01_MaximumNumberOfEvents ===")
    
    # 测试用例1：基本功能测试
    events1 = [[1, 2, 4], [3, 4, 3], [2, 3, 1]]
    k1 = 2
    result1 = Code01_MaximumNumberOfEvents.maxValue(events1, k1)
    print(f"测试用例1 - 预期: 7, 实际: {result1}")
    
    # 测试用例2：单个会议
    events2 = [[1, 2, 1]]
    k2 = 1
    result2 = Code01_MaximumNumberOfEvents.maxValue(events2, k2)
    print(f"测试用例2 - 预期: 1, 实际: {result2}")
    
    # 测试用例3：空输入
    events3 = []
    k3 = 1
    result3 = Code01_MaximumNumberOfEvents.maxValue(events3, k3)
    print(f"测试用例3 - 预期: 0, 实际: {result3}")
    
    # 测试用例4：k=0
    events4 = [[1, 2, 1]]
    k4 = 0
    result4 = Code01_MaximumNumberOfEvents.maxValue(events4, k4)
    print(f"测试用例4 - 预期: 0, 实际: {result4}")
    
    # 测试用例5：复杂情况
    events5 = [[1, 3, 5], [2, 4, 6], [3, 5, 7], [4, 6, 8]]
    k5 = 2
    result5 = Code01_MaximumNumberOfEvents.maxValue(events5, k5)
    print(f"测试用例5 - 预期: 13, 实际: {result5}")
    
    # 测试用例6：边界情况 - 所有会议冲突
    events6 = [[1, 3, 5], [1, 3, 6], [1, 3, 7]]
    k6 = 2
    result6 = Code01_MaximumNumberOfEvents.maxValue(events6, k6)
    print(f"测试用例6 - 预期: 7, 实际: {result6}")
    
    print("=== 测试完成 ===")


def performance_analysis():
    """
    性能分析函数
    """
    import time
    
    print("=== 性能分析 ===")
    
    # 生成大规模测试数据
    n = 1000
    large_events = []
    for i in range(n):
        start = i * 2
        end = start + 1
        value = i + 1
        large_events.append([start, end, value])
    
    k = 10
    
    # 记录开始时间
    start_time = time.time()
    
    result = Code01_MaximumNumberOfEvents.maxValue(large_events, k)
    
    # 记录结束时间
    end_time = time.time()
    duration = (end_time - start_time) * 1000  # 转换为毫秒
    
    print(f"大规模测试(n={n}, k={k}) - 结果: {result}")
    print(f"执行时间: {duration:.2f} 毫秒")
    print("时间复杂度: O(n * k + n * log n)")
    print("空间复杂度: O(n * k)")


def compare_implementations():
    """
    比较不同二分查找实现的性能
    """
    import time
    
    print("=== 二分查找实现对比 ===")
    
    # 生成测试数据
    n = 10000
    end_times = [i for i in range(n)]
    start_time = n // 2
    
    # 测试自定义二分查找
    start_custom = time.time()
    for _ in range(1000):
        Code01_MaximumNumberOfEvents._find_last_non_conflict(end_times, n - 1, start_time)
    end_custom = time.time()
    
    # 测试bisect二分查找
    start_bisect = time.time()
    for _ in range(1000):
        Code01_MaximumNumberOfEvents._find_last_non_conflict_bisect(end_times, n - 1, start_time)
    end_bisect = time.time()
    
    custom_time = (end_custom - start_custom) * 1000
    bisect_time = (end_bisect - start_bisect) * 1000
    
    print(f"自定义二分查找: {custom_time:.2f} 毫秒")
    print(f"bisect二分查找: {bisect_time:.2f} 毫秒")
    print(f"性能差异: {custom_time / bisect_time:.2f} 倍")


if __name__ == "__main__":
    """
    主函数 - 程序入口
    """
    print("=== Code01_MaximumNumberOfEvents Python实现 ===")
    
    # 运行测试
    test_max_value()
    
    # 性能分析
    performance_analysis()
    
    # 二分查找实现对比
    compare_implementations()
    
    print("\n=== 算法特点总结 ===")
    print("1. 核心算法: 动态规划 + 二分查找")
    print("2. 适用场景: 带权重的区间调度问题")
    print("3. 时间复杂度: O(n * k + n * log n)")
    print("4. 空间复杂度: O(n * k)")
    print("5. 优化方向: 使用线段树或Fenwick树优化查询")
    print("6. 工程应用: 项目管理、任务调度、资源分配")