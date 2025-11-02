#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
寻找最近和次近 (Python实现)

题目描述：
给定一个长度为n的数组arr，下标1 ~ n范围，数组无重复值
关于近的定义，距离的定义如下:
对i位置的数字x来说，只关注右侧的数字，和x的差值绝对值越小就越近
距离为差值绝对值，如果距离一样，数值越小的越近

解题思路：
这是一个寻找最近邻元素的问题，可以使用两种不同的方法解决。

方法一：使用有序集合（sorted list）
1. 从右向左遍历数组
2. 对于每个元素，使用有序集合查找最近和次近的元素
3. 更新结果数组

方法二：使用双向链表
1. 将数组元素按值排序，建立双向链表
2. 从左向右遍历原数组
3. 对于每个元素，在双向链表中查找最近和次近的元素
4. 删除当前元素，避免影响后续查找

时间复杂度：
- 有序集合方法：O(n * log n)
- 双向链表方法：O(n * log n)
空间复杂度：O(n)

相关题目：
1. LeetCode 220. 存在重复元素 III (TreeSet滑动窗口)
2. LeetCode 219. 存在重复元素 II (哈希表滑动窗口)
3. LeetCode 480. 滑动窗口中位数
4. LeetCode 992. K 个不同整数的子数组
5. LeetCode 76. 最小覆盖子串
6. LeetCode 3. 无重复字符的最长子串
7. LintCode 363. 接雨水
8. HackerRank - Sliding Window Median
9. Codeforces 372C. Watching Fireworks is Fun
10. AtCoder ABC134F. Permutation Oddness
11. 牛客网 NC123. 滑动窗口的最大值
12. 杭电OJ 6827. Master of Subgraph
13. POJ 2823. Sliding Window
14. UVa 11572. Unique Snowflakes
15. CodeChef - CHEFCOMP

工程化考量：
1. 在实际应用中，最近邻查找算法常用于：
   - 推荐系统中的相似度计算
   - 图像处理中的特征匹配
   - 数据库查询优化
   - 机器学习中的K近邻算法
2. 实现优化：
   - 对于大规模数据，可以使用KD树或球树优化
   - 使用空间换时间，预处理可能的查询结果
   - 考虑使用更高效的数据结构存储数据
3. 可扩展性：
   - 支持多维数据的最近邻查找
   - 处理动态添加和删除数据
   - 扩展到分布式计算环境
4. 鲁棒性考虑：
   - 处理重复值和边界情况
   - 优化大规模数据的性能
   - 处理数值溢出和精度问题
5. 跨语言特性对比：
   - Python: 使用sorted和bisect模块，代码简洁
   - Java: 使用TreeSet和Comparator
   - C++: 使用set和自定义比较函数，性能最优
"""

import bisect
from typing import List

class Code04_FindNear:
    """
    寻找最近和次近 - Python实现类
    """
    
    @staticmethod
    def find_near_with_sorted_list(arr: List[int]) -> List[List[int]]:
        """
        方法一：使用有序集合查找最近和次近元素
        
        Args:
            arr: 输入数组，下标从1开始
            
        Returns:
            List[List[int]]: 二维数组，result[i][0]表示最近元素索引，result[i][1]表示次近元素索引
        """
        n = len(arr)
        result = [[-1, -1] for _ in range(n + 1)]
        
        # 使用有序列表存储右侧元素（值, 索引）
        right_list = []
        
        # 从右向左遍历数组
        for i in range(n, 0, -1):
            current_value = arr[i - 1]
            
            if right_list:
                # 查找插入位置
                pos = bisect.bisect_left(right_list, (current_value, i))
                
                candidates = []
                
                # 检查右侧的较大元素
                if pos < len(right_list):
                    candidates.append(right_list[pos])
                    if pos + 1 < len(right_list):
                        candidates.append(right_list[pos + 1])
                
                # 检查左侧的较小元素
                if pos > 0:
                    candidates.append(right_list[pos - 1])
                    if pos > 1:
                        candidates.append(right_list[pos - 2])
                
                # 按距离排序候选元素
                candidates.sort(key=lambda x: (abs(x[0] - current_value), x[0]))
                
                # 取前两个作为最近和次近
                if len(candidates) >= 1:
                    result[i][0] = candidates[0][1]
                if len(candidates) >= 2:
                    result[i][1] = candidates[1][1]
            
            # 将当前元素插入有序列表
            bisect.insort(right_list, (current_value, i))
        
        return result
    
    @staticmethod
    def find_near_with_linked_list(arr: List[int]) -> List[List[int]]:
        """
        方法二：使用双向链表查找最近和次近元素
        
        Args:
            arr: 输入数组
            
        Returns:
            List[List[int]]: 二维数组，result[i][0]表示最近元素索引，result[i][1]表示次近元素索引
        """
        n = len(arr)
        result = [[-1, -1] for _ in range(n + 1)]
        
        # 创建(值, 索引)对并排序
        value_index_pairs = [(arr[i], i + 1) for i in range(n)]
        value_index_pairs.sort()
        
        # 构建双向链表
        prev = [-1] * (n + 2)  # 前驱指针
        next_ptr = [-1] * (n + 2)  # 后继指针
        pos_in_sorted = [-1] * (n + 1)  # 原始索引在排序数组中的位置
        
        # 初始化链表
        for i in range(n):
            original_index = value_index_pairs[i][1]
            pos_in_sorted[original_index] = i + 1  # 1-based位置
            
            if i > 0:
                prev[i + 1] = i
            if i < n - 1:
                next_ptr[i + 1] = i + 2
        
        # 按原始顺序从左向右处理
        for i in range(1, n + 1):
            current_pos = pos_in_sorted[i]
            
            if current_pos == -1:
                continue
            
            candidates = []
            
            # 检查前驱
            if prev[current_pos] != -1:
                prev_index = value_index_pairs[prev[current_pos] - 1][1]
                candidates.append(prev_index)
                if prev[prev[current_pos]] != -1:
                    prev_prev_index = value_index_pairs[prev[prev[current_pos]] - 1][1]
                    candidates.append(prev_prev_index)
            
            # 检查后继
            if next_ptr[current_pos] != -1:
                next_index = value_index_pairs[next_ptr[current_pos] - 1][1]
                candidates.append(next_index)
                if next_ptr[next_ptr[current_pos]] != -1:
                    next_next_index = value_index_pairs[next_ptr[next_ptr[current_pos]] - 1][1]
                    candidates.append(next_next_index)
            
            # 按距离排序候选元素
            candidates.sort(key=lambda x: (abs(arr[x - 1] - arr[i - 1]), arr[x - 1]))
            
            # 取前两个作为最近和次近
            if len(candidates) >= 1:
                result[i][0] = candidates[0]
            if len(candidates) >= 2:
                result[i][1] = candidates[1]
            
            # 从链表中删除当前元素
            if prev[current_pos] != -1:
                next_ptr[prev[current_pos]] = next_ptr[current_pos]
            if next_ptr[current_pos] != -1:
                prev[next_ptr[current_pos]] = prev[current_pos]
        
        return result
    
    @staticmethod
    def find_near(arr: List[int], use_sorted_list: bool = True) -> List[List[int]]:
        """
        统一接口：根据参数选择不同的实现方法
        
        Args:
            arr: 输入数组
            use_sorted_list: 是否使用有序集合方法，True使用有序集合，False使用链表
            
        Returns:
            List[List[int]]: 最近和次近元素索引
        """
        if use_sorted_list:
            return Code04_FindNear.find_near_with_sorted_list(arr)
        else:
            return Code04_FindNear.find_near_with_linked_list(arr)


def test_find_near():
    """
    测试函数 - 验证算法正确性
    """
    print("=== 测试Code04_FindNear ===")
    
    # 测试用例1：基本功能测试
    arr1 = [3, 1, 4, 2, 5]
    
    print(f"测试用例1 - 输入数组: {arr1}")
    
    # 使用有序集合方法
    result1_sorted = Code04_FindNear.find_near_with_sorted_list(arr1)
    print("有序集合方法结果:")
    for i in range(1, len(arr1) + 1):
        print(f"位置{i} (值{arr1[i-1]}): ", end="")
        print(f"最近={result1_sorted[i][0]} (值{arr1[result1_sorted[i][0]-1] if result1_sorted[i][0] != -1 else -1}), ", end="")
        print(f"次近={result1_sorted[i][1]} (值{arr1[result1_sorted[i][1]-1] if result1_sorted[i][1] != -1 else -1})")
    
    # 使用链表方法
    result1_list = Code04_FindNear.find_near_with_linked_list(arr1)
    print("链表方法结果:")
    for i in range(1, len(arr1) + 1):
        print(f"位置{i} (值{arr1[i-1]}): ", end="")
        print(f"最近={result1_list[i][0]} (值{arr1[result1_list[i][0]-1] if result1_list[i][0] != -1 else -1}), ", end="")
        print(f"次近={result1_list[i][1]} (值{arr1[result1_list[i][1]-1] if result1_list[i][1] != -1 else -1})")
    
    # 测试用例2：单元素数组
    arr2 = [5]
    result2 = Code04_FindNear.find_near_with_sorted_list(arr2)
    print(f"测试用例2 - 单元素数组: {arr2}")
    print(f"位置1: 最近={result2[1][0]}, 次近={result2[1][1]}")
    
    # 测试用例3：有序数组
    arr3 = [1, 2, 3, 4, 5]
    result3 = Code04_FindNear.find_near_with_sorted_list(arr3)
    print(f"测试用例3 - 有序数组: {arr3}")
    for i in range(1, len(arr3) + 1):
        print(f"位置{i}: 最近={result3[i][0]}, 次近={result3[i][1]}")
    
    print("=== 测试完成 ===")


def performance_analysis():
    """
    性能分析函数
    """
    import time
    import random
    
    print("=== 性能分析 ===")
    
    # 生成大规模测试数据
    n = 10000
    large_arr = [random.randint(0, 1000000) for _ in range(n)]
    
    # 测试有序集合方法性能
    start_time = time.time()
    result_sorted = Code04_FindNear.find_near_with_sorted_list(large_arr)
    end_time = time.time()
    duration_sorted = (end_time - start_time) * 1000  # 转换为毫秒
    
    print(f"有序集合方法 - 数据规模: {n} 元素")
    print(f"执行时间: {duration_sorted:.2f} 毫秒")
    
    # 测试链表方法性能
    start_time = time.time()
    result_list = Code04_FindNear.find_near_with_linked_list(large_arr)
    end_time = time.time()
    duration_list = (end_time - start_time) * 1000  # 转换为毫秒
    
    print(f"链表方法 - 数据规模: {n} 元素")
    print(f"执行时间: {duration_list:.2f} 毫秒")
    
    print(f"性能对比: 有序集合方法/链表方法 = {duration_sorted / duration_list:.2f}")
    
    print("时间复杂度: O(n log n)")
    print("空间复杂度: O(n)")


def complexity_analysis():
    """
    算法复杂度分析
    """
    print("=== 算法复杂度分析 ===")
    
    print("1. 有序集合方法复杂度分析:")
    print("   - 插入操作: O(log n)")
    print("   - 查找操作: O(log n)")
    print("   - 总时间复杂度: O(n log n)")
    print("   - 空间复杂度: O(n)")
    
    print("2. 链表方法复杂度分析:")
    print("   - 排序操作: O(n log n)")
    print("   - 链表操作: O(n)")
    print("   - 总时间复杂度: O(n log n)")
    print("   - 空间复杂度: O(n)")
    
    print("3. 优化方向:")
    print("   - 对于特定分布的数据，可以使用更优化的数据结构")
    print("   - 使用空间换时间，预处理可能的查询结果")
    print("   - 考虑使用更高效的数据结构存储数据")
    
    print("4. Python特定优化:")
    print("   - 使用bisect模块提高插入和查找效率")
    print("   - 使用生成器表达式减少内存使用")
    print("   - 使用局部变量缓存频繁访问的数据")


def memory_usage_analysis():
    """
    内存使用分析
    """
    import sys
    
    print("=== 内存使用分析 ===")
    
    # 分析不同规模下的内存使用
    sizes = [100, 1000, 5000, 10000]
    
    for n in sizes:
        # 估算内存使用
        # 有序集合: n * 16字节（每个元素存储值和索引）
        # 链表: n * 24字节（值、索引、前驱、后继指针）
        sorted_memory = n * 16 / 1024  # KB
        list_memory = n * 24 / 1024  # KB
        
        print(f"n={n}: 有序集合 {sorted_memory:.2f}KB, 链表 {list_memory:.2f}KB")


if __name__ == "__main__":
    """
    主函数 - 程序入口
    """
    print("=== Code04_FindNear Python实现 ===")
    
    # 运行测试
    test_find_near()
    
    # 性能分析
    performance_analysis()
    
    # 算法复杂度分析
    complexity_analysis()
    
    # 内存使用分析
    memory_usage_analysis()
    
    print("\n=== 算法特点总结 ===")
    print("1. 核心算法: 最近邻查找")
    print("2. 适用场景: 推荐系统、相似度计算、特征匹配")
    print("3. 时间复杂度: O(n log n)")
    print("4. 空间复杂度: O(n)")
    print("5. 优化方向: 使用KD树处理多维数据")
    print("6. 工程应用: 推荐算法、图像处理、数据库优化")