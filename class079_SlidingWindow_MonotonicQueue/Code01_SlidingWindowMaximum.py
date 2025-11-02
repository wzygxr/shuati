"""
滑动窗口最大值问题专题 - 单调队列算法深度解析与多语言实现

【题目背景】
滑动窗口最大值问题是算法面试中的经典高频题目，涉及单调队列、滑动窗口、双指针等核心算法思想。
本专题通过多种解法对比，深入剖析单调队列的原理和应用，提供Python语言的完整实现。

【核心算法思想】
单调队列是解决滑动窗口最值问题的最优数据结构，通过维护一个单调递减的双端队列，
确保队首元素始终是当前窗口的最大值，从而实现O(n)的时间复杂度。

【算法复杂度对比分析】
+---------------------+----------------+----------------+----------------------+
| 解法类型             | 时间复杂度     | 空间复杂度     | 适用场景              |
+---------------------+----------------+----------------+----------------------+
| 单调队列解法         | O(n)           | O(k)           | 大规模数据，最优解法  |
| 优先队列解法         | O(n*logk)      | O(n)           | 需要维护多个最值      |
| 暴力解法             | O(n*k)         | O(1)           | 小规模数据，易于理解  |
+---------------------+----------------+----------------+----------------------+

【工程化考量与优化策略】
1. 异常防御：全面处理空数组、非法窗口大小等边界情况
2. 性能优化：针对Python语言特性选择最优数据结构实现
3. 内存管理：避免不必要的内存分配和复制操作
4. 代码可读性：清晰的变量命名和算法步骤注释
5. 测试覆盖：包含单元测试、性能测试和边界测试

【相关题目资源】
1. LeetCode 239. 滑动窗口最大值 - https://leetcode.cn/problems/sliding-window-maximum/
2. 剑指Offer 59 - I. 滑动窗口的最大值 - https://leetcode.cn/problems/hua-dong-chuang-kou-de-zui-da-zhi-lcof/
3. POJ 2823. Sliding Window - http://poj.org/problem?id=2823
4. 洛谷 P1886. 滑动窗口 - https://www.luogu.com.cn/problem/P1886

【算法核心思想详解】
单调队列通过四步维护策略确保算法正确性和高效性：
1. 移除队首超出窗口范围的元素（过期检查）
2. 移除队尾所有小于当前元素的值（单调性维护）
3. 将当前元素索引加入队列尾部（入队操作）
4. 记录队首元素作为当前窗口的最大值（结果收集）

【时间复杂度数学证明】
虽然算法包含嵌套循环，但通过均摊分析可知：
- 每个元素最多入队一次，出队一次
- 总操作次数为O(n)，因此时间复杂度为O(n)
- 空间复杂度为O(k)，队列中最多存储k个元素索引

【Python实现特性】
- 使用collections.deque实现高效的双端队列操作
- 通过heapq模块实现优先队列解法（注意Python的heapq是最小堆）
- 提供全面的边界检查和异常处理
- 包含详细的性能测试和多解法对比
"""

from collections import deque
from typing import List
import heapq

class Solution:
    def maxSlidingWindow(self, nums: List[int], k: int) -> List[int]:
        """
        滑动窗口最大值 - 单调队列解法（最优解法）
        
        【算法原理深度解析】
        单调队列是解决滑动窗口最值问题的核心数据结构，通过维护一个单调递减的双端队列，
        确保队首元素始终是当前窗口的最大值。关键设计要点：
        1. 存储索引而非元素值：便于判断元素是否在当前窗口范围内
        2. 四步维护策略：过期检查 → 单调性维护 → 入队操作 → 结果收集
        3. 使用严格单调递减：保证队首元素的有效性和正确性
        
        【时间复杂度数学证明】
        虽然算法包含嵌套循环，但通过均摊分析可知：
        - 每个元素最多入队一次，出队一次
        - 总操作次数为O(n)，因此时间复杂度为O(n)
        - 空间复杂度为O(k)，队列中最多存储k个元素索引
        
        【工程化优化策略】
        1. 边界检查：处理空数组、非法窗口大小等异常情况
        2. 性能优化：窗口大小为1时的特殊处理
        3. 内存管理：避免不必要的列表扩容操作
        4. 代码可读性：清晰的变量命名和算法步骤注释
        
        【面试要点】
        - 能够解释为什么存储索引而非元素值
        - 理解单调队列的维护策略和均摊时间复杂度
        - 处理各种边界情况和特殊输入
        
        :param nums: List[int] - 输入整数数组
        :param k: int - 滑动窗口大小
        :return: List[int] - 每个滑动窗口的最大值组成的数组
        
        【复杂度分析】
        - 时间复杂度：O(n) - 每个元素最多入队和出队一次
        - 空间复杂度：O(k) - 队列中最多存储k个元素索引
        
        【测试用例覆盖】
        - 常规测试：[1,3,-1,-3,5,3,6,7], k=3 → [3,3,5,5,6,7]
        - 边界测试：单元素数组、窗口大小为1、空数组等
        - 特殊测试：重复元素、递增序列、递减序列等
        
        【相关题目链接】
        - LeetCode 239: https://leetcode.cn/problems/sliding-window-maximum/
        - 剑指Offer 59 - I: https://leetcode.cn/problems/hua-dong-chuang-kou-de-zui-da-zhi-lcof/
        """
        # 【边界检查】处理异常输入，确保代码健壮性
        # 空数组、非法窗口大小等边界情况的防御性编程
        if not nums or k <= 0:
            return []
        
        # 【性能优化】窗口大小为1时的特殊处理
        # 每个元素自身就是最大值，直接返回原数组避免不必要的计算
        if k == 1:
            return nums
        
        # 【数据结构选择】使用双端队列存储元素索引
        # collections.deque在Python中提供高效的O(1)双端操作
        dq = deque()
        result = []
        
        # 【滑动窗口主循环】遍历数组中的每个元素
        for i in range(len(nums)):
            # 【步骤1】过期检查：移除队首所有不在当前窗口范围内的元素
            # 当前窗口的有效范围是 [i-k+1, i]
            # 如果队首元素的索引小于左边界，说明它已不在窗口范围内
            while dq and dq[0] < i - k + 1:
                dq.popleft()  # O(1)时间的队首操作
            
            # 【步骤2】单调性维护：从队尾移除所有小于当前元素的值
            # 使用严格小于(<)而非小于等于(<=)：保留相等元素的历史索引
            # 相等元素中较新的索引在窗口中停留时间更长，更可能成为后续窗口的最大值
            while dq and nums[dq[-1]] < nums[i]:
                dq.pop()  # O(1)时间的队尾操作
            
            # 【步骤3】入队操作：将当前元素索引加入队列尾部
            dq.append(i)
            
            # 【步骤4】结果收集：当形成完整窗口后，记录当前窗口的最大值
            # 第一个完整窗口在i = k-1时形成（索引从0开始）
            if i >= k - 1:
                # 由于队列的单调递减性质，队首元素始终是当前窗口的最大值
                result.append(nums[dq[0]])
        
        return result

    def maxSlidingWindowPriorityQueue(self, nums: List[int], k: int) -> List[int]:
        """
        滑动窗口最大值 - 优先队列解法（最大堆实现）
        
        【算法原理解析】
        优先队列解法使用最大堆来维护窗口内的元素，堆顶元素始终是当前窗口的最大值。
        虽然时间复杂度不如单调队列，但实现简单直观，在某些场景下仍有应用价值。
        
        【关键设计决策】
        1. 存储(负值,索引)对：通过存储负值模拟最大堆（Python的heapq默认是最小堆）
        2. 延迟删除策略：当堆顶元素不在窗口范围内时，才真正删除
        3. 最大堆实现：通过存储负值实现降序排列
        
        【时间复杂度分析】
        - 构建初始窗口堆：O(k*logk)
        - 滑动窗口：每次插入新元素O(logk)，可能需要移除多个旧元素
        - 最坏情况下，每个元素可能被插入和删除各一次
        - 总时间复杂度：O(n*logk)
        
        【空间复杂度分析】
        - 堆中最多存储n个元素（极端情况下）
        - 因此空间复杂度为O(n)
        
        【与单调队列对比】
        优势：
        - 实现简单直观，代码易于理解
        - 当需要同时维护多个统计量时更灵活
        - 在某些特殊场景下可能更有优势
        
        劣势：
        - 时间复杂度O(n*logk)高于单调队列的O(n)
        - 空间复杂度O(n)高于单调队列的O(k)
        - 在大规模数据下性能较差
        
        【适用场景】
        - 窗口大小k较小的情况
        - 需要同时维护多个最值的场景
        - 算法教学和调试场景
        
        :param nums: List[int] - 输入整数数组
        :param k: int - 滑动窗口大小
        :return: List[int] - 每个滑动窗口的最大值组成的数组
        
        【复杂度分析】
        - 时间复杂度：O(n*logk) - 每个元素入堆出堆的操作均为O(logk)
        - 空间复杂度：O(n) - 堆中可能存储所有n个元素
        """
        # 【边界检查】处理异常输入
        if not nums or k == 0:
            return []
        # 【性能优化】窗口大小为1时的特殊处理
        if k == 1:
            return nums
        
        n = len(nums)
        result = []
        # 【数据结构选择】使用最小堆模拟最大堆
        # 通过存储负值实现降序排列：(-值, 索引)
        max_heap = []
        
        # 【步骤1】初始化第一个窗口的元素到堆中
        for i in range(k):
            heapq.heappush(max_heap, (-nums[i], i))
        # 记录第一个窗口的最大值（堆顶元素的负值取反）
        result.append(-max_heap[0][0])
        
        # 【步骤2】滑动窗口，逐个处理剩余元素
        for i in range(k, n):
            # 添加新元素到堆中
            heapq.heappush(max_heap, (-nums[i], i))
            
            # 【延迟删除策略】移除所有不在当前窗口范围内的最大值
            # 当前窗口范围是[i-k+1, i]，如果堆顶元素的索引小于左边界，说明已过期
            while max_heap[0][1] < i - k + 1:
                heapq.heappop(max_heap)
            
            # 此时堆顶元素即为当前窗口的最大值
            result.append(-max_heap[0][0])
        
        return result

    def maxSlidingWindowBruteForce(self, nums: List[int], k: int) -> List[int]:
        """
        滑动窗口最大值 - 暴力解法（用于对比和教学）
        
        【算法原理解析】
        暴力解法是最直观的解决方案，通过遍历每个可能的窗口位置，
        对每个窗口内的k个元素计算最大值。虽然效率较低，但思路简单，
        适合作为算法教学的起点，帮助理解问题本质。
        
        【关键设计特点】
        1. 双重循环：外层循环遍历窗口起始位置，内层循环遍历窗口内元素
        2. 线性扫描：对每个窗口进行完整的线性扫描找最大值
        3. 简单直观：代码逻辑清晰，易于理解和实现
        
        【时间复杂度分析】
        - 外层循环：n-k+1次迭代（窗口数量）
        - 内层循环：k次迭代（窗口大小）
        - 总时间复杂度：O((n-k+1)*k) = O(n*k)
        
        【空间复杂度分析】
        - 仅使用常数级别的额外变量（不考虑结果数组）
        - 因此空间复杂度为O(1)
        
        【与优化解法对比】
        优势：
        - 实现简单，代码易于理解
        - 不需要复杂的数据结构
        - 适合小规模数据或调试场景
        
        劣势：
        - 时间复杂度O(n*k)过高，不适合大规模数据
        - 存在大量重复计算，效率低下
        - 在实际工程应用中性能不可接受
        
        【适用场景】
        - 算法教学和入门学习
        - 小规模数据测试（n和k都很小）
        - 验证其他优化算法的正确性
        - 调试和问题定位
        
        :param nums: List[int] - 输入整数数组
        :param k: int - 滑动窗口大小
        :return: List[int] - 每个滑动窗口的最大值组成的数组
        
        【复杂度分析】
        - 时间复杂度：O(n*k) - 对每个窗口遍历找最大值
        - 空间复杂度：O(1) - 仅使用常数级别额外空间
        """
        # 【边界检查】处理异常输入
        if not nums or k == 0:
            return []
        # 【性能优化】窗口大小为1时的特殊处理
        if k == 1:
            return nums
        
        n = len(nums)
        result = []
        
        # 【暴力解法主循环】遍历每个窗口的起始位置
        for i in range(n - k + 1):
            # 初始化当前窗口的最大值为窗口第一个元素
            max_val = nums[i]
            
            # 【内层循环】遍历窗口内的剩余元素，更新最大值
            # 从窗口第二个元素开始比较（j=1到j=k-1）
            for j in range(1, k):
                if nums[i + j] > max_val:
                    max_val = nums[i + j]
            
            # 记录当前窗口的最大值
            result.append(max_val)
        
        return result

# 测试与性能评估模块
def run_unit_tests():
    """
    单元测试函数 - 验证算法在各种测试场景下的正确性
    
    测试用例覆盖：
    1. 常规输入 - 验证基本功能
    2. 边界情况 - 单元素、空数组等特殊输入
    3. 极限场景 - 所有元素相同、递增/递减序列等
    4. 异常输入 - 非法参数处理
    """
    print("=== 滑动窗口最大值算法 - 单元测试 ===")
    solution = Solution()
    
    # 测试场景1: 常规测试用例
    print("\n【场景1: 常规测试】")
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    expected1 = [3, 3, 5, 5, 6, 7]
    result1 = solution.maxSlidingWindow(nums1, k1)
    print(f"输入: nums=[1,3,-1,-3,5,3,6,7], k=3")
    print(f"输出: {result1}")
    print(f"预期: {expected1}")
    print(f"结果: {'✓ 通过' if result1 == expected1 else '✗ 失败'}")
    
    # 测试场景2: 边界情况 - 单元素数组
    print("\n【场景2: 边界情况 - 单元素数组】")
    nums2 = [1]
    k2 = 1
    expected2 = [1]
    result2 = solution.maxSlidingWindow(nums2, k2)
    print(f"输入: nums=[1], k=1")
    print(f"输出: {result2}")
    print(f"预期: {expected2}")
    print(f"结果: {'✓ 通过' if result2 == expected2 else '✗ 失败'}")
    
    # 测试场景3: 边界情况 - 窗口大小为1
    print("\n【场景3: 边界情况 - 窗口大小为1】")
    nums3 = [1, -1]
    k3 = 1
    expected3 = [1, -1]
    result3 = solution.maxSlidingWindow(nums3, k3)
    print(f"输入: nums=[1,-1], k=1")
    print(f"输出: {result3}")
    print(f"预期: {expected3}")
    print(f"结果: {'✓ 通过' if result3 == expected3 else '✗ 失败'}")
    
    # 测试场景4: 重复元素
    print("\n【场景4: 重复元素】")
    nums4 = [4, 2, 4, 2, 1]
    k4 = 3
    expected4 = [4, 4, 4]
    result4 = solution.maxSlidingWindow(nums4, k4)
    print(f"输入: nums=[4,2,4,2,1], k=3")
    print(f"输出: {result4}")
    print(f"预期: {expected4}")
    print(f"结果: {'✓ 通过' if result4 == expected4 else '✗ 失败'}")
    
    # 测试场景5: 递减序列
    print("\n【场景5: 递减序列】")
    nums5 = [5, 4, 3, 2, 1]
    k5 = 3
    expected5 = [5, 4, 3]
    result5 = solution.maxSlidingWindow(nums5, k5)
    print(f"输入: nums=[5,4,3,2,1], k=3")
    print(f"输出: {result5}")
    print(f"预期: {expected5}")
    print(f"结果: {'✓ 通过' if result5 == expected5 else '✗ 失败'}")
    
    # 测试场景6: 递增序列
    print("\n【场景6: 递增序列】")
    nums6 = [1, 2, 3, 4, 5]
    k6 = 3
    expected6 = [3, 4, 5]
    result6 = solution.maxSlidingWindow(nums6, k6)
    print(f"输入: nums=[1,2,3,4,5], k=3")
    print(f"输出: {result6}")
    print(f"预期: {expected6}")
    print(f"结果: {'✓ 通过' if result6 == expected6 else '✗ 失败'}")
    
    # 测试场景7: 异常输入 - 空数组
    print("\n【场景7: 异常输入 - 空数组】")
    nums7 = []
    k7 = 3
    expected7 = []
    result7 = solution.maxSlidingWindow(nums7, k7)
    print(f"输入: nums=[], k=3")
    print(f"输出: {result7}")
    print(f"预期: {expected7}")
    print(f"结果: {'✓ 通过' if result7 == expected7 else '✗ 失败'}")
    
    # 测试场景8: 异常输入 - 窗口大小为0
    print("\n【场景8: 异常输入 - 窗口大小为0】")
    nums8 = [1, 2, 3]
    k8 = 0
    expected8 = []
    result8 = solution.maxSlidingWindow(nums8, k8)
    print(f"输入: nums=[1,2,3], k=0")
    print(f"输出: {result8}")
    print(f"预期: {expected8}")
    print(f"结果: {'✓ 通过' if result8 == expected8 else '✗ 失败'}")
    
    # 测试场景9: 窗口大小等于数组长度
    print("\n【场景9: 窗口大小等于数组长度】")
    nums9 = [3, 1, 5, 2, 4]
    k9 = 5
    expected9 = [5]
    result9 = solution.maxSlidingWindow(nums9, k9)
    print(f"输入: nums=[3,1,5,2,4], k=5")
    print(f"输出: {result9}")
    print(f"预期: {expected9}")
    print(f"结果: {'✓ 通过' if result9 == expected9 else '✗ 失败'}")
    
    # 测试场景10: 包含负数的混合序列
    print("\n【场景10: 包含负数的混合序列】")
    nums10 = [-7, -8, 7, 5, 7, 1, 6, 0]
    k10 = 4
    expected10 = [7, 7, 7, 7, 7]
    result10 = solution.maxSlidingWindow(nums10, k10)
    print(f"输入: nums=[-7,-8,7,5,7,1,6,0], k=4")
    print(f"输出: {result10}")
    print(f"预期: {expected10}")
    print(f"结果: {'✓ 通过' if result10 == expected10 else '✗ 失败'}")

def run_solution_comparison():
    """
    解法正确性对比测试 - 验证三种解法结果的一致性
    """
    print("\n=== 解法正确性对比测试 ===")
    solution = Solution()
    
    # 测试用例
    test_cases = [
        ([1, 3, -1, -3, 5, 3, 6, 7], 3),  # 常规测试
        ([1], 1),                          # 单元素
        ([7, 2, 4], 2),                    # 小规模
        ([1, -1], 1)                       # 包含负数
    ]
    
    for i, (nums, k) in enumerate(test_cases, 1):
        print(f"\n【对比测试 {i}】nums={nums}, k={k}")
        
        # 获取三种解法的结果
        deque_result = solution.maxSlidingWindow(nums, k)
        pq_result = solution.maxSlidingWindowPriorityQueue(nums, k)
        brute_result = solution.maxSlidingWindowBruteForce(nums, k)
        
        # 打印结果
        print(f"单调队列解法: {deque_result}")
        print(f"优先队列解法: {pq_result}")
        print(f"暴力解法: {brute_result}")
        
        # 验证结果一致性
        if deque_result == pq_result and pq_result == brute_result:
            print(f"结果一致性: ✓ 通过 - 三种解法结果一致")
        else:
            print(f"结果一致性: ✗ 失败 - 三种解法结果不一致")

def run_performance_test():
    """
    性能测试函数 - 评估不同解法在大规模数据下的性能差异
    
    性能测试重点:
    1. 验证时间复杂度差异在实际运行中的体现
    2. 对比不同数据规模下各算法的执行时间
    3. 注意：暴力解法在大规模数据下可能超时
    """
    import time
    import random
    
    print("\n=== 性能测试 ===")
    print("注意: 暴力解法在大规模数据下可能显著较慢或超时")
    solution = Solution()
    
    # 定义测试规模
    test_sizes = [
        (100, 10),      # 小规模
        (1000, 50),     # 中等规模
        (10000, 100)    # 大规模
    ]
    
    for size, k in test_sizes:
        print(f"\n【测试规模】数组大小: {size}, 窗口大小: {k}")
        
        # 生成随机测试数据
        nums = [random.randint(-10000, 10000) for _ in range(size)]
        
        # 测试单调队列解法
        start_time = time.time()
        deque_result = solution.maxSlidingWindow(nums, k)
        deque_time = time.time() - start_time
        print(f"单调队列解法耗时: {deque_time:.6f} 秒")
        
        # 测试优先队列解法
        start_time = time.time()
        pq_result = solution.maxSlidingWindowPriorityQueue(nums, k)
        pq_time = time.time() - start_time
        print(f"优先队列解法耗时: {pq_time:.6f} 秒")
        
        # 只有小规模数据才测试暴力解法，避免超时
        brute_time = 0
        if size <= 1000:
            start_time = time.time()
            brute_result = solution.maxSlidingWindowBruteForce(nums, k)
            brute_time = time.time() - start_time
            print(f"暴力解法耗时: {brute_time:.6f} 秒")
            
            # 验证结果一致性
            if deque_result == pq_result and pq_result == brute_result:
                print(f"结果一致性: ✓ 通过")
            else:
                print(f"结果一致性: ✗ 失败")
        else:
            print("暴力解法: 跳过（大规模数据下效率过低）")
            # 只验证两种高效解法的结果一致性
            if deque_result == pq_result:
                print(f"结果一致性（单调队列 vs 优先队列）: ✓ 通过")
            else:
                print(f"结果一致性（单调队列 vs 优先队列）: ✗ 失败")
        
        # 输出性能对比
        print(f"性能对比: 单调队列 / 优先队列 = {deque_time/pq_time:.2f}x")
        if size <= 1000 and brute_time > 0:
            print(f"性能对比: 单调队列 / 暴力解法 = {deque_time/brute_time:.2f}x")

# 执行所有测试
if __name__ == "__main__":
    # 1. 运行单元测试
    run_unit_tests()
    
    # 2. 运行解法对比测试
    run_solution_comparison()
    
    # 3. 运行性能测试
    run_performance_test()
    
    print("\n=== 测试完成 ===")
    print("滑动窗口最大值算法 - 单调队列解法是最优解法，具有O(n)时间复杂度和O(k)空间复杂度")
    print("在实际工程应用中，应优先选择单调队列解法，尤其是处理大规模数据时")