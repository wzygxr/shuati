# 无重叠区间
# 给定一个区间的集合 intervals ，其中 intervals[i] = [starti, endi] 。
# 返回需要移除区间的最小数量，使剩余区间互不重叠。
# 测试链接 : https://leetcode.cn/problems/non-overlapping-intervals/

def eraseOverlapIntervals(intervals):
    """
    无重叠区间
    
    算法思路：
    使用贪心策略：
    1. 按照区间的结束位置进行升序排序
    2. 贪心选择结束时间最早的区间，这样能为后续区间留出最多空间
    3. 遍历排序后的区间，统计重叠的区间数量
    
    正确性分析：
    1. 为了保留最多的区间，我们应该优先选择结束时间早的区间
    2. 这样可以为后面的区间留出更多空间
    3. 重叠的区间需要被移除
    
    时间复杂度：O(n*logn) - 主要是排序的时间复杂度
    空间复杂度：O(logn) - 排序所需的额外空间
    
    :param intervals: 区间数组
    :return: 需要移除的区间数量
    """
    # 边界情况处理
    if not intervals:
        return 0
    
    # 按照区间的结束位置进行升序排序
    intervals.sort(key=lambda x: x[1])
    
    count = 0              # 需要移除的区间数量
    end = intervals[0][1]  # 当前选择区间的结束位置
    
    # 从第二个区间开始遍历
    for i in range(1, len(intervals)):
        # 如果当前区间的开始位置小于前一个区间的结束位置，说明重叠
        if intervals[i][0] < end:
            # 需要移除这个区间
            count += 1
        else:
            # 更新结束位置
            end = intervals[i][1]
    
    # 返回需要移除的区间数量
    return count

# 测试用例
if __name__ == "__main__":
    # 测试用例1: intervals = [[1,2],[2,3],[3,4],[1,3]] -> 输出: 1
    intervals1 = [[1, 2], [2, 3], [3, 4], [1, 3]]
    print("测试用例1:")
    print("区间数组:", intervals1)
    print("需要移除的区间数量:", eraseOverlapIntervals(intervals1))  # 期望输出: 1
    
    # 测试用例2: intervals = [[1,2],[1,2],[1,2]] -> 输出: 2
    intervals2 = [[1, 2], [1, 2], [1, 2]]
    print("\n测试用例2:")
    print("区间数组:", intervals2)
    print("需要移除的区间数量:", eraseOverlapIntervals(intervals2))  # 期望输出: 2
    
    # 测试用例3: intervals = [[1,2],[2,3]] -> 输出: 0
    intervals3 = [[1, 2], [2, 3]]
    print("\n测试用例3:")
    print("区间数组:", intervals3)
    print("需要移除的区间数量:", eraseOverlapIntervals(intervals3))  # 期望输出: 0
    
    # 测试用例4: intervals = [] -> 输出: 0
    intervals4 = []
    print("\n测试用例4:")
    print("区间数组:", intervals4)
    print("需要移除的区间数量:", eraseOverlapIntervals(intervals4))  # 期望输出: 0