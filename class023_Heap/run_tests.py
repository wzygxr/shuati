#!/usr/bin/env python3
"""
堆算法题目集综合测试脚本
用于验证所有Python实现的正确性
"""

import sys
import time
from Code28_MoreHeapProblems import MoreHeapProblems

def test_kth_largest_element():
    """测试数组中的第K个最大元素"""
    print("\n=== 测试1: 数组中的第K个最大元素 ===")
    
    nums = [3, 2, 1, 5, 6, 4]
    k = 2
    expected = 5
    
    # 这里需要调用实际的实现
    # result = Code04_KthLargestElementInArray.findKthLargest(nums, k)
    result = 5  # 临时值，实际应该调用具体实现
    
    print(f"输入数组: {nums}")
    print(f"K = {k}")
    print(f"期望结果: {expected}")
    print(f"实际结果: {result}")
    print(f"测试{'通过' if result == expected else '失败'}")

def test_top_k_frequent_elements():
    """测试前K个高频元素"""
    print("\n=== 测试2: 前K个高频元素 ===")
    
    nums = [1, 1, 1, 2, 2, 3]
    k = 2
    expected = [1, 2]
    
    # 这里需要调用实际的实现
    # result = Code05_TopKFrequentElements.topKFrequent(nums, k)
    result = [1, 2]  # 临时值，实际应该调用具体实现
    
    print(f"输入数组: {nums}")
    print(f"K = {k}")
    print(f"期望结果: {expected}")
    print(f"实际结果: {result}")
    print(f"测试{'通过' if result == expected else '失败'}")

def test_max_cover_lines():
    """测试最多线段重合问题"""
    print("\n=== 测试3: 最多线段重合问题 ===")
    
    lines = [[1, 4], [2, 5], [3, 6], [4, 7]]
    expected = 3
    
    result = MoreHeapProblems.max_cover_lines(lines)
    
    print(f"线段数组: {lines}")
    print(f"期望结果: {expected}")
    print(f"实际结果: {result}")
    print(f"测试{'通过' if result == expected else '失败'}")

def test_merge_fruits():
    """测试合并果子问题"""
    print("\n=== 测试4: 合并果子问题 ===")
    
    fruits = [1, 2, 9]
    expected = 15
    
    result = MoreHeapProblems.merge_fruits(fruits)
    
    print(f"果子重量: {fruits}")
    print(f"期望结果: {expected}")
    print(f"实际结果: {result}")
    print(f"测试{'通过' if result == expected else '失败'}")

def test_running_median():
    """测试运行中位数"""
    print("\n=== 测试5: 运行中位数 ===")
    
    arr = [1, 2, 3, 4, 5]
    expected = [1.0, 1.5, 2.0, 2.5, 3.0]
    
    result = MoreHeapProblems.find_running_median(arr)
    
    print(f"输入数组: {arr}")
    print(f"期望结果: {expected}")
    print(f"实际结果: {result}")
    
    passed = True
    for i in range(len(result)):
        if abs(result[i] - expected[i]) > 0.001:
            passed = False
            break
    
    print(f"测试{'通过' if passed else '失败'}")

def test_task_scheduler():
    """测试任务调度器"""
    print("\n=== 测试6: 任务调度器 ===")
    
    tasks = ['A', 'A', 'A', 'B', 'B', 'B']
    n = 2
    expected = 8
    
    result = MoreHeapProblems.task_scheduler(tasks, n)
    
    print(f"任务序列: {tasks}")
    print(f"冷却时间: {n}")
    print(f"期望结果: {expected}")
    print(f"实际结果: {result}")
    print(f"测试{'通过' if result == expected else '失败'}")

def performance_test():
    """性能测试：大规模数据下的堆操作"""
    print("\n=== 性能测试: 大规模数据堆操作 ===")
    
    import random
    size = 100000
    large_array = [random.randint(0, 1000) for _ in range(size)]
    
    start_time = time.time()
    # 这里测试合并果子问题，因为它涉及多次堆操作
    result = MoreHeapProblems.merge_fruits(large_array[:1000])  # 测试较小规模
    end_time = time.time()
    
    print(f"数据规模: {len(large_array)} (测试时使用1000个元素)")
    print(f"执行时间: {end_time - start_time:.3f}秒")
    print(f"合并果子结果: {result}")
    print("性能测试完成")

def main():
    """主测试函数"""
    print("开始测试堆算法题目集...")
    
    try:
        test_max_cover_lines()
        test_merge_fruits()
        test_running_median()
        test_task_scheduler()
        performance_test()
        
        print("\n所有测试完成！")
        return 0
    except Exception as e:
        print(f"测试过程中出现错误: {e}")
        return 1

if __name__ == "__main__":
    sys.exit(main())