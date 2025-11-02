#!/usr/bin/env python3
"""
LeetCode 56. 合并区间 (Merge Intervals) - Python版本

题目来源：https://leetcode.cn/problems/merge-intervals/

解题思路：
使用扫描线算法解决区间合并问题

时间复杂度：O(n log n)
空间复杂度：O(n)
"""

import time
import random
from typing import List

class Solution:
    def merge(self, intervals: List[List[int]]) -> List[List[int]]:
        """
        合并区间的扫描线解法
        
        Args:
            intervals: 输入区间数组
            
        Returns:
            合并后的区间数组
        """
        if not intervals:
            return []
        
        # 按照区间开始时间排序
        intervals.sort(key=lambda x: x[0])
        
        merged = []
        current_interval = intervals[0]
        
        for i in range(1, len(intervals)):
            interval = intervals[i]
            
            if interval[0] <= current_interval[1]:
                # 当前区间与合并区间重叠，合并
                current_interval[1] = max(current_interval[1], interval[1])
            else:
                # 不重叠，将当前合并区间加入结果，开始新的合并区间
                merged.append(current_interval)
                current_interval = interval
        
        # 添加最后一个合并区间
        merged.append(current_interval)
        
        return merged

def test_merge_intervals():
    """测试合并区间解法"""
    solution = Solution()
    
    print("=== LeetCode 56. 合并区间 (Python版本) ===")
    
    # 测试用例1
    print("测试用例1:")
    intervals1 = [[1, 3], [2, 6], [8, 10], [15, 18]]
    result1 = solution.merge(intervals1)
    print(f"输入: {intervals1}")
    print(f"输出: {result1}")
    print("期望: [[1,6],[8,10],[15,18]]")
    print()
    
    # 测试用例2
    print("测试用例2:")
    intervals2 = [[1, 4], [4, 5]]
    result2 = solution.merge(intervals2)
    print(f"输入: {intervals2}")
    print(f"输出: {result2}")
    print("期望: [[1,5]]")
    print()
    
    # 边界测试
    print("边界测试:")
    intervals3 = [[1, 4], [0, 4]]
    result3 = solution.merge(intervals3)
    print(f"输入: {intervals3}")
    print(f"输出: {result3}")
    print("期望: [[0,4]]")
    print()
    
    # 性能测试
    print("=== 性能测试 ===")
    random.seed(42)
    n = 10000
    large_intervals = []
    
    for i in range(n):
        start = random.randint(0, 100000)
        end = start + random.randint(1, 1000)
        large_intervals.append([start, end])
    
    start_time = time.time()
    large_result = solution.merge(large_intervals)
    end_time = time.time()
    
    print(f"{n}个区间的合并计算完成")
    print(f"合并后区间数量: {len(large_result)}")
    print(f"运行时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # Python语言特性考量
    print("\n=== Python语言特性考量 ===")
    print("1. 使用lambda表达式进行自定义排序")
    print("2. 使用列表推导式生成测试数据")
    print("3. 使用类型注解提高代码可读性")
    print("4. 使用f-string进行格式化输出")
    
    # 算法复杂度分析
    print("\n=== 算法复杂度分析 ===")
    print("时间复杂度: O(n log n)")
    print("  - 排序: O(n log n)")
    print("  - 扫描: O(n)")
    print("空间复杂度: O(n)")
    print("  - 存储结果: O(n)")
    print("  - 排序: O(log n) 或 O(n) 取决于排序算法")

if __name__ == "__main__":
    test_merge_intervals()