"""
贪心算法高级题目集合 - Python版本
包含更复杂的贪心算法问题和优化技巧
涵盖区间调度、资源分配、路径优化等高级应用

算法专题概览：
本文件包含7个高级贪心算法问题，展示了贪心算法在复杂场景中的应用：
1. 时间调度优化（课程表、会议安排）
2. 区间覆盖问题（交集大小、水龙头覆盖）
3. 资源分配优化（建筑爬升、块排序）
4. 字符串处理（去重优化）

贪心算法高级应用核心思想：
在复杂问题中，贪心算法通常需要结合其他数据结构（如堆、栈）来实现，
并且需要更精细的策略设计来保证局部最优选择能导致全局最优解。

工程化最佳实践：
1. 异常处理：检查输入参数的有效性
2. 边界条件：处理空输入、单元素等特殊情况
3. 性能优化：选择合适的数据结构和算法策略
4. 可读性：清晰的变量命名和详细注释
"""

import heapq
from collections import deque

def schedule_course(courses):
    """
    题目1: LeetCode 630. 课程表 III
    问题描述：有n门课程，每门课程有持续时间和截止时间，求最多能完成多少门课程。

    算法思路：按截止时间排序，使用最大堆维护已选课程的持续时间
    1. 贪心策略：按截止时间排序，优先考虑截止时间早的课程
    2. 使用最大堆维护已选课程的持续时间，当无法加入新课程时，替换掉持续时间最长的课程
    
    Args:
        courses (List[List[int]]): 课程信息列表，每个元素为[duration, last_day]
    
    Returns:
        int: 最多能完成的课程数量
    
    时间复杂度: O(n log n)，其中n是课程数量，主要是排序和堆操作的时间复杂度
    空间复杂度: O(n)，最大堆的空间
    """
    # 异常处理：空课程列表
    if not courses:
        return 0
    
    # 按截止时间排序
    # 时间复杂度：O(n log n)
    courses.sort(key=lambda x: x[1])
    
    # 最大堆（使用最小堆存储负值来模拟最大堆）
    max_heap = []
    current_time = 0  # 当前累计时间
    
    # 遍历所有课程
    # 时间复杂度：O(n log n)
    for course in courses:
        duration, last_day = course
        
        if current_time + duration <= last_day:
            # 可以完成当前课程
            current_time += duration
            heapq.heappush(max_heap, -duration)
        elif max_heap and -max_heap[0] > duration:
            # 替换掉持续时间最长的课程
            current_time = current_time - (-max_heap[0]) + duration
            heapq.heappop(max_heap)
            heapq.heappush(max_heap, -duration)
    
    return len(max_heap)

def intersection_size_two(intervals):
    """
    题目2: LeetCode 757. 设置交集大小至少为2
    问题描述：给定一些区间，找到最小的集合S，使得S与每个区间至少有2个整数相交。

    算法思路：按结束位置排序，维护两个最大的点
    1. 贪心策略：按结束位置升序排序，结束位置相同时按开始位置降序排序
    2. 维护两个最大的点，尽可能重用已选的点
    
    Args:
        intervals (List[List[int]]): 区间列表，每个元素为[start, end]
    
    Returns:
        int: 最小集合S的大小
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：空区间列表
    if not intervals:
        return 0
    
    # 按结束位置升序排序，结束位置相同时按开始位置降序排序
    # 时间复杂度：O(n log n)
    intervals.sort(key=lambda x: (x[1], -x[0]))
    
    result = 0      # 集合S的大小
    first, second = -1, -1  # 维护的两个最大点
    
    # 遍历所有区间
    # 时间复杂度：O(n)
    for interval in intervals:
        start, end = interval
        
        if start > second:
            # 需要添加两个新点
            result += 2
            first = end - 1
            second = end
        elif start > first:
            # 需要添加一个新点
            result += 1
            first = second
            second = end
    
    return result

def max_events(events):
    """
    题目3: LeetCode 1353. 最多可以参加的会议数目
    问题描述：给定一些会议的开始和结束时间，求最多能参加多少个会议。

    算法思路：按开始时间排序，使用最小堆维护当前可参加的会议
    1. 贪心策略：按时间顺序处理，每天参加结束时间最早的会议
    2. 使用最小堆维护当前可参加的会议的结束时间
    
    Args:
        events (List[List[int]]): 会议信息列表，每个元素为[start_day, end_day]
    
    Returns:
        int: 最多能参加的会议数目
    
    时间复杂度: O(n log n)，其中n是会议数量，主要是排序和堆操作的时间复杂度
    空间复杂度: O(n)，最小堆的空间
    """
    # 异常处理：空会议列表
    if not events:
        return 0
    
    # 按开始时间排序
    # 时间复杂度：O(n log n)
    events.sort(key=lambda x: x[0])
    
    # 最小堆，存储当前可参加的会议的结束时间
    min_heap = []
    result = 0    # 参加的会议数
    day = 1       # 当前天数
    index = 0     # 会议索引
    n = len(events)
    
    # 按天处理
    while index < n or min_heap:
        # 将今天开始的会议加入堆中
        while index < n and events[index][0] == day:
            heapq.heappush(min_heap, events[index][1])
            index += 1
        
        # 移除已经过期的会议
        while min_heap and min_heap[0] < day:
            heapq.heappop(min_heap)
        
        # 参加结束时间最早的会议
        if min_heap:
            heapq.heappop(min_heap)
            result += 1
        
        day += 1
    
    return result

def furthest_building(heights, bricks, ladders):
    """
    题目4: LeetCode 1642. 可以到达的最远建筑
    问题描述：从建筑物0开始，使用砖块和梯子爬升到尽可能远的建筑物。

    算法思路：使用最小堆维护已使用的梯子
    1. 贪心策略：优先使用梯子处理最大的高度差，用砖块处理较小的高度差
    2. 使用最小堆维护已使用的梯子对应的高度差
    
    Args:
        heights (List[int]): 建筑物高度列表
        bricks (int): 砖块数量
        ladders (int): 梯子数量
    
    Returns:
        int: 可以到达的最远建筑物索引
    
    时间复杂度: O(n log k)，其中n是建筑物数量，k是梯子数量
    空间复杂度: O(k)，最小堆的空间
    """
    # 异常处理：建筑物数量不足
    if len(heights) <= 1:
        return 0
    
    # 最小堆，存储已使用的梯子对应的高度差
    min_heap = []
    
    # 遍历所有相邻建筑物
    # 时间复杂度：O(n log k)
    for i in range(1, len(heights)):
        diff = heights[i] - heights[i - 1]
        
        if diff > 0:
            # 需要爬升
            heapq.heappush(min_heap, diff)
            
            # 如果梯子不够用，用砖块替换最小的梯子使用
            if len(min_heap) > ladders:
                bricks -= heapq.heappop(min_heap)
                if bricks < 0:
                    return i - 1  # 无法到达当前建筑
    
    return len(heights) - 1

def remove_duplicate_letters(s):
    """
    题目5: LeetCode 316. 去除重复字母
    问题描述：去除字符串中的重复字母，使得每个字母只出现一次，且结果字典序最小。

    算法思路：使用单调栈维护结果字符串
    1. 贪心策略：维护单调递增的栈，当遇到更小字符时，如果后面还会出现，则弹出栈顶
    2. 使用栈来构建结果字符串
    
    Args:
        s (str): 输入字符串
    
    Returns:
        str: 去除重复字母后的字典序最小字符串
    
    时间复杂度: O(n)，其中n是字符串长度
    空间复杂度: O(n)，栈和集合的空间
    """
    # 异常处理：空字符串
    if not s:
        return ""
    
    # 记录每个字符的最后出现位置
    # 时间复杂度：O(n)
    last_pos = {}
    for i, char in enumerate(s):
        last_pos[char] = i
    
    visited = set()  # 记录已在栈中的字符
    stack = []       # 单调栈
    
    # 遍历字符串
    # 时间复杂度：O(n)
    for i, char in enumerate(s):
        # 如果字符已经在栈中，跳过
        if char in visited:
            continue
        
        # 维护单调栈：当栈顶字符大于当前字符且后面还会出现时，弹出栈顶
        while stack and stack[-1] > char and last_pos[stack[-1]] > i:
            visited.remove(stack.pop())
        
        stack.append(char)
        visited.add(char)
    
    return ''.join(stack)

def max_chunks_to_sorted(arr):
    """
    题目6: LeetCode 768. 最多能完成排序的块 II
    问题描述：将数组分割成尽可能多的块，使得每个块排序后连接起来与原数组排序后相同。

    算法思路：维护当前块的最大值和前缀最大值
    1. 贪心策略：当左半部分的最大值小于等于右半部分的最小值时，可以分割
    2. 预处理计算前缀最大值和后缀最小值
    
    Args:
        arr (List[int]): 输入数组
    
    Returns:
        int: 最多能分割的块数
    
    时间复杂度: O(n)，其中n是数组长度
    空间复杂度: O(n)，存储前缀最大值和后缀最小值的空间
    """
    # 异常处理：空数组
    if not arr:
        return 0
    
    n = len(arr)
    max_left = [0] * n   # max_left[i]表示arr[0..i]的最大值
    min_right = [0] * n  # min_right[i]表示arr[i..n-1]的最小值
    
    # 计算从左到右的最大值
    # 时间复杂度：O(n)
    max_left[0] = arr[0]
    for i in range(1, n):
        max_left[i] = max(max_left[i - 1], arr[i])
    
    # 计算从右到左的最小值
    # 时间复杂度：O(n)
    min_right[n - 1] = arr[n - 1]
    for i in range(n - 2, -1, -1):
        min_right[i] = min(min_right[i + 1], arr[i])
    
    chunks = 0  # 块数
    
    # 判断每个位置是否可以分割
    # 时间复杂度：O(n)
    for i in range(n - 1):
        if max_left[i] <= min_right[i + 1]:
            chunks += 1
    
    return chunks + 1  # 最后一块

def min_taps(n, ranges):
    """
    题目7: LeetCode 1326. 灌溉花园的最少水龙头数目
    问题描述：在长度为n的花园中，每个位置有一个水龙头，求灌溉整个花园的最少水龙头数目。

    算法思路：区间覆盖问题，贪心选择
    1. 贪心策略：转换为区间覆盖问题，每次选择能覆盖当前位置的最远水龙头
    2. 按左端点排序后贪心选择
    
    Args:
        n (int): 花园长度
        ranges (List[int]): 每个位置水龙头的灌溉范围
    
    Returns:
        int: 灌溉整个花园的最少水龙头数目，无法灌溉返回-1
    
    时间复杂度: O(n)，其中n是花园长度
    空间复杂度: O(n)，存储区间信息的空间
    """
    # 异常处理：输入参数不匹配
    if len(ranges) != n + 1:
        return -1
    
    # 创建区间数组
    # 时间复杂度：O(n)
    intervals = []
    for i in range(n + 1):
        left = max(0, i - ranges[i])
        right = min(n, i + ranges[i])
        intervals.append((left, right))
    
    # 按左端点排序
    # 时间复杂度：O(n log n)
    intervals.sort(key=lambda x: x[0])
    
    taps = 0         # 水龙头数目
    current_end = 0  # 当前覆盖的结束位置
    farthest = 0     # 能覆盖的最远位置
    i = 0            # 区间索引
    
    # 贪心选择水龙头
    while current_end < n:
        # 找到能覆盖当前结束位置的最远水龙头
        while i <= n and intervals[i][0] <= current_end:
            farthest = max(farthest, intervals[i][1])
            i += 1
        
        if farthest <= current_end:
            return -1  # 无法覆盖
        
        taps += 1
        current_end = farthest
        
        if current_end >= n:
            break
    
    return taps

# 测试函数
if __name__ == "__main__":
    # 测试课程表III
    courses = [[100, 200], [200, 1300], [1000, 1250], [2000, 3200]]
    print("课程表III测试:", schedule_course(courses))  # 期望: 3
    
    # 测试去除重复字母
    print("去除重复字母测试:", remove_duplicate_letters("bcabc"))  # 期望: "abc"
    
    # 测试最多可以参加的会议数目
    events = [[1, 2], [2, 3], [3, 4], [1, 2]]
    print("最多会议测试:", max_events(events))  # 期望: 4
    
    # 测试可以到达的最远建筑
    heights = [4, 2, 7, 6, 9, 14, 12]
    print("最远建筑测试:", furthest_building(heights, 5, 1))  # 期望: 4