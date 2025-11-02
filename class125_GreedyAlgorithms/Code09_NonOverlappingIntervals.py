# 无重叠区间 (Non-overlapping Intervals)
# 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
# 
# 算法标签: 贪心算法(Greedy Algorithm)、区间调度(Interval Scheduling)
# 时间复杂度: O(n * logn)，其中n是区间数量
# 空间复杂度: O(1)，仅使用常数额外空间
# 测试链接 : https://leetcode.cn/problems/non-overlapping-intervals/
# 相关题目: LeetCode 56. 合并区间、LeetCode 452. 用最少数量的箭引爆气球
# 贪心算法专题 - 补充题目收集与详解

"""
算法思路详解：
1. 贪心策略：按区间结束位置排序，优先选择结束位置早的区间
   - 这个策略的核心思想是为后续区间留出更多空间
   - 结束位置早的区间不会影响太多后续区间的选取

2. 排序后遍历区间，统计不重叠的区间数量
   - 通过一次遍历完成所有计算
   - 只需要比较相邻区间的重叠情况

3. 总区间数减去不重叠区间数就是需要移除的区间数
   - 这是问题的转换：最大化保留区间数等价于最小化移除区间数

时间复杂度分析：
- 排序时间复杂度：O(n * logn)，其中n是区间数量
- 遍历时间复杂度：O(n)
- 总体时间复杂度：O(n * logn)

空间复杂度分析：
- 只使用了常数额外空间存储变量
- 空间复杂度：O(1)

是否最优解：
- 是，这是处理此类问题的最优解法
- 贪心策略保证了局部最优解能导致全局最优解

工程化最佳实践：
1. 异常处理：检查输入是否为空或格式不正确
2. 边界条件：处理空数组、单个区间等特殊情况
3. 性能优化：使用贪心策略避免复杂的动态规划
4. 可读性：清晰的变量命名和详细注释，便于维护

极端场景与边界情况处理：
1. 空输入：intervals为空数组
2. 极端值：只有一个区间、所有区间相同
3. 重复数据：多个区间相同
4. 有序/逆序数据：区间按开始位置或结束位置排序

跨语言实现差异与优化：
1. Java：使用Arrays.sort进行排序，性能稳定
2. C++：使用std::sort进行排序，底层实现可能更优化
3. Python：使用sorted函数或list.sort()方法，基于Timsort算法

调试与测试策略：
1. 打印中间过程：在循环中打印当前区间和上一个选择的区间
2. 用断言验证中间结果：确保选择的区间不重叠
3. 性能退化排查：检查排序和遍历的时间复杂度
4. 边界测试：测试空数组、单元素等边界情况

实际应用场景与拓展：
1. 时间调度问题：在会议安排中优化资源分配
2. 计算机视觉：用于非极大值抑制(NMS)算法
3. 自然语言处理：用于实体识别中的重叠实体处理

算法深入解析：
贪心算法在区间调度问题中的应用体现了其核心思想：
1. 局部最优选择：每次选择结束位置最早的区间
2. 无后效性：当前的选择不会影响之前的状态
3. 最优子结构：问题的最优解包含子问题的最优解
这个问题的关键洞察是，选择结束位置最早的区间能为后续区间留出最多空间。
"""


def eraseOverlapIntervals(intervals):
    """
    无重叠区间主函数 - 使用贪心算法计算需要移除的区间最小数量
    
    算法思路：
    1. 按区间结束位置排序，优先选择结束位置早的区间
    2. 排序后遍历区间，统计不重叠的区间数量
    3. 总区间数减去不重叠区间数就是需要移除的区间数
    
    Args:
        intervals (List[List[int]]): 区间列表，每个区间为[start, end]格式
        intervals[i] = [start_i, end_i]表示第i个区间
    
    Returns:
        int: 需要移除的区间最小数量
    
    时间复杂度: O(n * logn)，其中n是区间数量
    空间复杂度: O(1)，仅使用常数额外空间
    
    Examples:
        >>> eraseOverlapIntervals([[1, 2], [2, 3], [3, 4], [1, 3]])
        1
        >>> eraseOverlapIntervals([[1, 2], [1, 2], [1, 2]])
        2
    """
    # 异常处理：检查输入是否为空
    if not intervals:
        return 0
    
    # 边界条件：只有一个区间，不需要移除
    if len(intervals) == 1:
        return 0
    
    # 按区间结束位置排序
    # 关键点：按结束位置排序保证了贪心策略的正确性
    intervals.sort(key=lambda x: x[1])
    
    count = 1              # 不重叠区间数量，初始选择第一个区间
    end = intervals[0][1]  # 上一个选择区间的结束位置
    
    # 遍历排序后的区间
    # 时间复杂度：O(n)
    for i in range(1, len(intervals)):
        # 如果当前区间与上一个选择的区间不重叠
        if intervals[i][0] >= end:
            count += 1             # 不重叠区间数加1
            end = intervals[i][1]  # 更新结束位置
    
    # 需要移除的区间数 = 总区间数 - 不重叠区间数
    return len(intervals) - count


# 补充题目1: LeetCode 56. 合并区间
# 题目描述: 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
# 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
# 链接: https://leetcode.cn/problems/merge-intervals/

def merge(intervals):
    """
    合并重叠的区间 - 使用贪心算法
    
    算法思路：
    1. 按区间起始位置排序
    2. 依次合并重叠区间
    
    Args:
        intervals (List[List[int]]): 二维列表，表示区间集合，每个区间为[start, end]
    
    Returns:
        List[List[int]]: 合并后的区间列表
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(n)，用于存储合并后的区间
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组、单区间等情况
    3. 性能优化：排序后使用贪心策略避免重复计算
    """
    # 异常处理：检查输入是否为空或长度不足
    if not intervals or len(intervals) <= 1:
        return intervals  # 空数组或只有一个区间，无需合并
    
    # 贪心策略：按区间起始位置排序，然后依次合并重叠区间
    # 时间复杂度：O(n log n)
    intervals.sort(key=lambda x: x[0])
    
    merged = [intervals[0]]  # 添加第一个区间
    
    # 遍历其余区间
    # 时间复杂度：O(n)
    for i in range(1, len(intervals)):
        last = merged[-1]  # 获取上一个合并后的区间
        current = intervals[i]  # 当前区间
        
        # 如果当前区间与上一个合并后的区间重叠，则合并它们
        if current[0] <= last[1]:
            # 更新合并后的区间结束位置为两个区间结束位置的较大值
            last[1] = max(last[1], current[1])
        else:
            # 否则直接添加当前区间
            merged.append(current)
    
    return merged


# 补充题目2: LeetCode 57. 插入区间
# 题目描述: 给你一个 无重叠的 ，按照区间起始端点排序的区间列表。
# 在列表中插入一个新的区间，你需要确保列表中的区间仍然有序且不重叠（如果有必要的话，可以合并区间）。
# 链接: https://leetcode.cn/problems/insert-interval/

def insert(intervals, newInterval):
    """
    在排序且不重叠的区间列表中插入一个新区间，必要时合并 - 使用贪心算法
    
    算法思路：
    1. 添加所有在新区间之前且不重叠的区间
    2. 合并所有与新区间重叠的区间
    3. 添加剩余的区间
    
    Args:
        intervals (List[List[int]]): 二维列表，表示已排序且不重叠的区间集合
        newInterval (List[int]): 列表，表示要插入的新区间
    
    Returns:
        List[List[int]]: 插入并合并后的区间列表
    
    时间复杂度: O(n)，需要遍历整个区间列表一次
    空间复杂度: O(n)，用于存储结果
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组等情况
    3. 性能优化：三段式处理避免重复遍历
    """
    result = []
    i = 0
    n = len(intervals)
    
    # 添加所有在新区间之前且不重叠的区间
    # 时间复杂度：O(n)
    while i < n and intervals[i][1] < newInterval[0]:
        result.append(intervals[i])
        i += 1
    
    # 合并所有与新区间重叠的区间
    # 时间复杂度：O(n)
    while i < n and intervals[i][0] <= newInterval[1]:
        newInterval[0] = min(newInterval[0], intervals[i][0])
        newInterval[1] = max(newInterval[1], intervals[i][1])
        i += 1
    
    # 添加合并后的区间
    result.append(newInterval)
    
    # 添加剩余的区间
    # 时间复杂度：O(n)
    while i < n:
        result.append(intervals[i])
        i += 1
    
    return result


# 补充题目3: LeetCode 452. 用最少数量的箭引爆气球
# 题目描述: 有一些球形气球贴在一堵用XY平面表示的墙面上。气球可以用区间表示为 [start, end]，
# 飞镖必须从整个区间的内部穿过才能引爆气球。求解把所有气球射爆所需的最小飞镖数。
# 链接: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/

def findMinArrowShots(points):
    """
    计算引爆所有气球所需的最小飞镖数 - 使用贪心算法
    
    算法思路：
    1. 按区间结束位置排序
    2. 贪心策略：尽可能引爆更多气球
    
    Args:
        points (List[List[int]]): 二维列表，表示气球的区间，每个区间为[start, end]
    
    Returns:
        int: 所需的最小飞镖数
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(log n)，排序所需的额外空间
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组等情况
    3. 性能优化：排序后使用贪心策略
    """
    # 异常处理：检查输入是否为空
    if not points:
        return 0  # 没有气球，需要0支箭
    
    # 贪心策略：按区间结束位置排序，尽可能引爆更多气球
    # 注意：Python的sort对整数不会有溢出问题，所以不需要特殊处理
    # 时间复杂度：O(n log n)
    points.sort(key=lambda x: x[1])
    
    count = 1  # 需要的箭数，初始为1
    end = points[0][1]  # 第一支箭的位置
    
    # 遍历排序后的区间
    # 时间复杂度：O(n)
    for i in range(1, len(points)):
        # 如果当前气球的开始位置大于上一支箭的位置，需要一支新箭
        if points[i][0] > end:
            count += 1
            end = points[i][1]
        # 否则，当前气球会被上一支箭引爆，不需要额外的箭
    
    return count


# 补充题目4: LeetCode 986. 区间列表的交集
# 题目描述: 给定两个由一些 闭区间 组成的列表，firstList 和 secondList，
# 其中 firstList[i] = [starti, endi] 而 secondList[j] = [startj, endj]。
# 每个列表中的区间是不相交的，并且已经排序。
# 返回这 两个区间列表的交集 。
# 链接: https://leetcode.cn/problems/interval-list-intersections/

def intervalIntersection(firstList, secondList):
    """
    计算两个有序区间列表的交集 - 使用双指针算法
    
    算法思路：
    1. 使用双指针遍历两个列表
    2. 计算当前两个区间的交集
    
    Args:
        firstList (List[List[int]]): 第一个有序区间列表
        secondList (List[List[int]]): 第二个有序区间列表
    
    Returns:
        List[List[int]]: 交集区间列表
    
    时间复杂度: O(m + n)，其中m和n分别是两个区间列表的长度
    空间复杂度: O(min(m, n))，存储交集的最坏情况
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组等情况
    3. 性能优化：双指针避免重复计算
    """
    # 异常处理：检查输入是否为空
    if not firstList or not secondList:
        return []  # 任一列表为空，交集为空
    
    result = []
    i, j = 0, 0  # 两个指针分别指向两个列表
    m, n = len(firstList), len(secondList)
    
    # 双指针遍历两个列表
    # 时间复杂度：O(m + n)
    while i < m and j < n:
        start = max(firstList[i][0], secondList[j][0])  # 交集的起始位置
        end = min(firstList[i][1], secondList[j][1])   # 交集的结束位置
        
        # 如果有交集
        if start <= end:
            result.append([start, end])
        
        # 移动结束位置较小的区间的指针
        if firstList[i][1] < secondList[j][1]:
            i += 1
        else:
            j += 1
    
    return result


# 补充题目5: LeetCode 1288. 删除被覆盖区间
# 题目描述: 给你一个区间列表，请你删除列表中被其他区间完全覆盖的区间。
# 只有当 c <= a 且 b <= d 时，我们才认为区间 [a,b] 被区间 [c,d] 覆盖。
# 在完成所有删除操作后，请你返回列表中剩余区间的数目。
# 链接: https://leetcode.cn/problems/remove-covered-intervals/

def removeCoveredIntervals(intervals):
    """
    计算删除被覆盖区间后剩余的区间数 - 使用贪心算法
    
    算法思路：
    1. 按起始位置升序排序，起始位置相同时按结束位置降序排序
    2. 贪心策略：遍历区间，统计不被覆盖的区间
    
    Args:
        intervals (List[List[int]]): 二维列表，表示区间集合
    
    Returns:
        int: 剩余区间的数目
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(log n)，排序所需的额外空间
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组、单区间等情况
    3. 性能优化：排序后使用贪心策略
    """
    # 异常处理：检查输入是否为空或长度不足
    if not intervals or len(intervals) <= 1:
        return len(intervals)  # 空数组或只有一个区间
    
    # 贪心策略：按起始位置升序排序，起始位置相同时按结束位置降序排序
    # 时间复杂度：O(n log n)
    intervals.sort(key=lambda x: (x[0], -x[1]))
    
    count = 1  # 剩余区间数，至少有一个区间
    end = intervals[0][1]  # 当前最大的结束位置
    
    # 遍历排序后的区间
    # 时间复杂度：O(n)
    for i in range(1, len(intervals)):
        # 如果当前区间的结束位置大于最大结束位置，说明不被覆盖
        if intervals[i][1] > end:
            count += 1
            end = intervals[i][1]
    
    return count


# 主函数测试
if __name__ == "__main__":
    # 测试 eraseOverlapIntervals
    intervals1 = [[1, 2], [2, 3], [3, 4], [1, 3]]
    print("测试 eraseOverlapIntervals:")
    print(f"输入: {intervals1}")
    print(f"输出: {eraseOverlapIntervals(intervals1)}")
    
    # 测试 merge
    intervals2 = [[1, 3], [2, 6], [8, 10], [15, 18]]
    print("\n测试 merge:")
    print(f"输入: {intervals2}")
    print(f"输出: {merge(intervals2)}")
    
    # 测试 insert
    intervals3 = [[1, 3], [6, 9]]
    newInterval = [2, 5]
    print("\n测试 insert:")
    print(f"输入: {intervals3}, {newInterval}")
    print(f"输出: {insert(intervals3, newInterval)}")
    
    # 测试 findMinArrowShots
    points = [[10, 16], [2, 8], [1, 6], [7, 12]]
    print("\n测试 findMinArrowShots:")
    print(f"输入: {points}")
    print(f"输出: {findMinArrowShots(points)}")
    
    # 测试 intervalIntersection
    firstList = [[0, 2], [5, 10], [13, 23], [24, 25]]
    secondList = [[1, 5], [8, 12], [15, 24], [25, 26]]
    print("\n测试 intervalIntersection:")
    print(f"输入: {firstList}, {secondList}")
    print(f"输出: {intervalIntersection(firstList, secondList)}")
    
    # 测试 removeCoveredIntervals
    intervals4 = [[1, 4], [3, 6], [2, 8]]
    print("\n测试 removeCoveredIntervals:")
    print(f"输入: {intervals4}")
    print(f"输出: {removeCoveredIntervals(intervals4)}")