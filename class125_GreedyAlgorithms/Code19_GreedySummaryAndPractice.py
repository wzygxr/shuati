"""
贪心算法总结与实战练习 - Python版本
包含贪心算法的核心思想、常见题型、解题模板和综合练习
"""

import heapq
from typing import List, Tuple
from functools import cmp_to_key

class GreedySummary:
    """
    贪心算法核心思想总结
    """
    
    @staticmethod
    def greedy_principles():
        """
        贪心算法的三个核心性质：
        1. 贪心选择性质：每一步都选择当前最优解
        2. 最优子结构：问题的最优解包含其子问题的最优解
        3. 无后效性：当前选择不会影响后续选择的最优性
        """
        principles = {
            "贪心选择性质": "每一步都选择当前最优解，希望通过局部最优达到全局最优",
            "最优子结构": "问题的最优解包含其子问题的最优解",
            "无后效性": "当前选择不会影响后续选择的最优性"
        }
        return principles
    
    @staticmethod
    def applicable_scenarios():
        """
        贪心算法适用场景
        """
        scenarios = {
            "区间调度问题": "活动选择、会议安排等",
            "资源分配问题": "分数背包、任务调度等",
            "路径优化问题": "最短路径、加油站问题等",
            "字符串处理": "字典序最小、字符重组等",
            "数学优化问题": "最大数、最小差值等"
        }
        return scenarios

class GreedyTemplates:
    """
    贪心算法解题模板
    """
    
    @staticmethod
    def template_sort_and_greedy(arr: List[int]) -> int:
        """
        模板1: 排序 + 贪心选择
        适用：需要按某种规则排序后选择的问题
        """
        # 1. 排序数组
        arr.sort()
        
        # 2. 贪心选择
        result = 0
        for i in range(len(arr)):
            # 根据问题需求进行选择
            if True:  # 满足选择条件
                result += arr[i]
        
        return result
    
    @staticmethod
    def template_heap_and_greedy(arr: List[int]) -> int:
        """
        模板2: 堆 + 贪心选择
        适用：需要动态维护最优选择的问题
        """
        # 1. 构建堆
        heap = arr[:]
        heapq.heapify(heap)
        
        # 2. 贪心选择
        result = 0
        while heap:
            current = heapq.heappop(heap)
            result += current
        
        return result
    
    @staticmethod
    def template_two_pointers(arr1: List[int], arr2: List[int]) -> int:
        """
        模板3: 双指针 + 贪心选择
        适用：需要同时处理两个序列的问题
        """
        # 1. 排序两个数组
        arr1.sort()
        arr2.sort()
        
        # 2. 双指针遍历
        i, j = 0, 0
        result = 0
        
        while i < len(arr1) and j < len(arr2):
            if arr1[i] <= arr2[j]:  # 满足匹配条件
                result += 1
                i += 1
                j += 1
            else:
                j += 1
        
        return result

class ComprehensivePractice:
    """
    综合实战练习
    """
    
    @staticmethod
    def comprehensive_interval_problems(intervals: List[List[int]]) -> int:
        """
        练习1: 综合区间调度
        结合多种区间问题的综合解法
        """
        if not intervals:
            return 0
        
        # 问题1: 最多不重叠区间数
        intervals.sort(key=lambda x: x[1])
        non_overlap_count = 1
        end = intervals[0][1]
        
        for i in range(1, len(intervals)):
            if intervals[i][0] >= end:
                non_overlap_count += 1
                end = intervals[i][1]
        
        # 问题2: 合并所有重叠区间
        merged = [intervals[0]]
        for i in range(1, len(intervals)):
            last = merged[-1]
            if intervals[i][0] <= last[1]:
                last[1] = max(last[1], intervals[i][1])
            else:
                merged.append(intervals[i])
        
        return len(merged)  # 返回合并后的区间数
    
    @staticmethod
    def comprehensive_resource_allocation(items: List[List[int]], capacity: int) -> int:
        """
        练习2: 资源分配综合问题
        多种资源分配策略的比较
        """
        if not items or capacity <= 0:
            return 0
        
        # 策略1: 按价值排序（0-1背包贪心近似）
        items_by_value = sorted(items, key=lambda x: x[1], reverse=True)
        value_strategy = 0
        remaining = capacity
        
        for item in items_by_value:
            cost, value = item
            if cost <= remaining:
                value_strategy += value
                remaining -= cost
        
        # 策略2: 按价值密度排序（分数背包最优）
        items_by_density = sorted(items, key=lambda x: x[1] / x[0], reverse=True)
        density_strategy = 0
        remaining = capacity
        
        for item in items_by_density:
            cost, value = item
            if cost <= remaining:
                density_strategy += value
                remaining -= cost
            else:
                density_strategy += value * remaining / cost
                break
        
        # 策略3: 按重量排序（尽量多装）
        items_by_weight = sorted(items, key=lambda x: x[0])
        weight_strategy = 0
        remaining = capacity
        
        for item in items_by_weight:
            cost, value = item
            if cost <= remaining:
                weight_strategy += value
                remaining -= cost
        
        # 返回三种策略中的最大值
        return max(value_strategy, density_strategy, weight_strategy)
    
    @staticmethod
    def comprehensive_string_problems(s: str, k: int) -> str:
        """
        练习3: 字符串处理综合问题
        多种字符串重构和优化问题
        """
        if not s:
            return ""
        
        # 问题1: 去除重复字母并使字典序最小
        last_pos = {}
        for i, char in enumerate(s):
            last_pos[char] = i
        
        visited = set()
        stack = []
        
        for i, char in enumerate(s):
            if char in visited:
                continue
            
            while stack and stack[-1] > char and last_pos[stack[-1]] > i:
                visited.remove(stack.pop())
            
            stack.append(char)
            visited.add(char)
        
        remove_duplicate = ''.join(stack)
        
        # 问题2: 重构字符串避免连续k个相同字符
        freq = {}
        for char in remove_duplicate:
            freq[char] = freq.get(char, 0) + 1
        
        # 使用最大堆（用最小堆存储负值模拟）
        heap = []
        for char, count in freq.items():
            heapq.heappush(heap, (-count, char))
        
        result = []
        while heap:
            temp = []
            
            for _ in range(min(k, len(heap))):
                if not heap:
                    break
                count, char = heapq.heappop(heap)
                result.append(char)
                count = -count - 1  # 转换为正数并减1
                
                if count > 0:
                    temp.append((-count, char))
            
            for item in temp:
                heapq.heappush(heap, item)
        
        return ''.join(result)

class DebuggingTechniques:
    """
    贪心算法调试技巧
    """
    
    @staticmethod
    def print_intermediate_results(arr: List[int]):
        """
        技巧1: 打印中间结果
        在关键步骤打印变量值，帮助理解算法执行过程
        """
        print(f"原始数组: {arr}")
        
        # 排序后
        sorted_arr = sorted(arr)
        print(f"排序后数组: {sorted_arr}")
        
        # 贪心选择过程
        total = 0
        for i, num in enumerate(sorted_arr):
            total += num
            print(f"第{i+1}步选择: {num}, 当前总和: {total}")
    
    @staticmethod
    def verify_greedy_choice(test_cases: List[Tuple[int, List[int]]]) -> bool:
        """
        技巧2: 验证贪心选择正确性
        通过小规模测试用例验证算法正确性
        """
        for expected, input_arr in test_cases:
            # 执行贪心算法
            actual = DebuggingTechniques.greedy_algorithm(input_arr)
            
            if actual != expected:
                print(f"测试失败: 输入={input_arr}, 期望={expected}, 实际={actual}")
                return False
        
        print("所有测试用例通过!")
        return True
    
    @staticmethod
    def greedy_algorithm(arr: List[int]) -> int:
        """
        示例贪心算法实现
        """
        if not arr:
            return 0
        return max(arr)  # 简单示例：选择最大值
    
    @staticmethod
    def analyze_performance(arr: List[int]):
        """
        技巧3: 性能分析
        分析算法的时间复杂度和空间复杂度
        """
        import time
        
        start_time = time.time()
        
        # 执行算法
        sorted_arr = sorted(arr)
        result = sum(sorted_arr)
        
        end_time = time.time()
        duration = end_time - start_time
        
        print(f"算法执行时间: {duration:.6f} 秒")
        print(f"输入规模: {len(arr)}")
        print("时间复杂度: O(n log n)")
        print("空间复杂度: O(n)")

def main():
    """
    主测试函数
    """
    # 测试贪心算法核心思想
    print("=== 贪心算法核心思想 ===")
    principles = GreedySummary.greedy_principles()
    for key, value in principles.items():
        print(f"{key}: {value}")
    
    # 测试综合区间调度
    print("\n=== 综合区间调度练习 ===")
    intervals = [[1, 3], [2, 4], [3, 5], [4, 6]]
    result = ComprehensivePractice.comprehensive_interval_problems(intervals)
    print(f"区间调度结果: {result}")
    
    # 测试资源分配综合问题
    print("\n=== 资源分配综合练习 ===")
    items = [[2, 10], [3, 15], [5, 20]]
    result = ComprehensivePractice.comprehensive_resource_allocation(items, 7)
    print(f"资源分配结果: {result}")
    
    # 测试调试技巧
    print("\n=== 调试技巧演示 ===")
    test_array = [3, 1, 4, 1, 5]
    DebuggingTechniques.print_intermediate_results(test_array)
    
    # 测试贪心选择验证
    print("\n=== 贪心选择验证 ===")
    test_cases = [
        (5, [1, 2, 3, 4, 5]),  # 期望结果, 输入数组
        (3, [1, 2, 3]),
        (0, [])
    ]
    DebuggingTechniques.verify_greedy_choice(test_cases)

if __name__ == "__main__":
    main()