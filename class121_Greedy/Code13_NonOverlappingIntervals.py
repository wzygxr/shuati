# 无重叠区间
# 给定一个区间的集合 intervals ，其中 intervals[i] = [starti, endi] 。
# 返回需要移除区间的最小数量，使剩余区间互不重叠。
# 测试链接: https://leetcode.cn/problems/non-overlapping-intervals/

def eraseOverlapIntervals(intervals):
    """
    无重叠区间问题的贪心解法
    
    解题思路：
    1. 将区间按右端点升序排序
    2. 贪心策略：优先选择右边界小的区间，这样能给后续区间留出更多空间
    3. 遍历排序后的区间，统计不重叠的区间数量
    4. 总区间数减去不重叠区间数就是需要移除的区间数
    
    贪心策略的正确性：
    选择右边界小的区间能最大化保留后续区间的机会，这是局部最优选择，
    最终能得到全局最优解（移除最少的区间数）。
    
    时间复杂度：O(n log n)，主要消耗在排序上
    空间复杂度：O(1)，只使用了常数个额外变量
    
    :param intervals: 区间数组
    :return: 需要移除区间的最小数量
    """
    # 边界条件处理：如果区间数组为空，则不需要移除任何区间
    if not intervals:
        return 0

    # 1. 按区间右端点升序排序
    intervals.sort(key=lambda x: x[1])

    # 2. 初始化变量
    count = 1              # 不重叠区间数量，初始化为1（第一个区间）
    end = intervals[0][1]  # 当前不重叠区间的右边界

    # 3. 从第二个区间开始遍历
    for i in range(1, len(intervals)):
        # 4. 如果当前区间与前一个不重叠区间不重叠
        if intervals[i][0] >= end:
            count += 1              # 不重叠区间数增加
            end = intervals[i][1]   # 更新右边界
        # 5. 如果重叠，则跳过当前区间（相当于移除）

    # 6. 返回需要移除的区间数
    return len(intervals) - count


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    # 输入: intervals = [[1,2],[2,3],[3,4],[1,3]]
    # 输出: 1
    # 解释: 移除 [1,3] 后，剩下的区间没有重叠。
    intervals1 = [[1, 2], [2, 3], [3, 4], [1, 3]]
    print("测试用例1结果:", eraseOverlapIntervals(intervals1))  # 期望输出: 1

    # 测试用例2
    # 输入: intervals = [[1,2],[1,2],[1,2]]
    # 输出: 2
    # 解释: 你需要移除两个 [1,2] 来使剩下的区间没有重叠。
    intervals2 = [[1, 2], [1, 2], [1, 2]]
    print("测试用例2结果:", eraseOverlapIntervals(intervals2))  # 期望输出: 2

    # 测试用例3
    # 输入: intervals = [[1,2],[2,3]]
    # 输出: 0
    # 解释: 你不需要移除任何区间，因为它们已经是无重叠的了。
    intervals3 = [[1, 2], [2, 3]]
    print("测试用例3结果:", eraseOverlapIntervals(intervals3))  # 期望输出: 0

    # 测试用例4：边界情况
    # 输入: intervals = []
    # 输出: 0
    intervals4 = []
    print("测试用例4结果:", eraseOverlapIntervals(intervals4))  # 期望输出: 0

    # 测试用例5：复杂情况
    # 输入: intervals = [[1,3],[2,4],[3,5],[4,6]]
    # 输出: 1
    intervals5 = [[1, 3], [2, 4], [3, 5], [4, 6]]
    print("测试用例5结果:", eraseOverlapIntervals(intervals5))  # 期望输出: 1