# 合并区间
# 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
# 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
# 测试链接 : https://leetcode.cn/problems/merge-intervals/

def merge(intervals):
    """
    合并区间
    
    算法思路：
    使用贪心策略：
    1. 按照区间的开始位置进行升序排序
    2. 遍历排序后的区间：
       - 如果当前区间与前一个区间重叠，合并它们
       - 否则将前一个区间加入结果集
    
    正确性分析：
    1. 按开始位置排序后，重叠的区间会相邻
    2. 贪心选择：尽可能合并重叠区间
    3. 合并后的区间能覆盖所有被合并的区间
    
    时间复杂度：O(n*logn) - 主要是排序的时间复杂度
    空间复杂度：O(logn) - 排序所需的额外空间
    
    :param intervals: 区间数组
    :return: 合并后的区间数组
    """
    # 边界情况处理
    if not intervals:
        return []
    
    # 按照区间的开始位置进行升序排序
    intervals.sort(key=lambda x: x[0])
    
    # 初始化结果列表
    result = []
    # 第一个区间作为当前合并区间
    current_interval = intervals[0]
    
    # 从第二个区间开始遍历
    for i in range(1, len(intervals)):
        # 如果当前区间与前一个区间重叠
        if intervals[i][0] <= current_interval[1]:
            # 合并区间，更新结束位置为两者较大值
            current_interval[1] = max(current_interval[1], intervals[i][1])
        else:
            # 不重叠，将前一个区间加入结果集
            result.append(current_interval)
            # 更新当前合并区间
            current_interval = intervals[i]
    
    # 将最后一个区间加入结果集
    result.append(current_interval)
    
    return result

# 测试用例
if __name__ == "__main__":
    # 测试用例1: intervals = [[1,3],[2,6],[8,10],[15,18]] -> 输出: [[1,6],[8,10],[15,18]]
    intervals1 = [[1, 3], [2, 6], [8, 10], [15, 18]]
    print("测试用例1:")
    print("区间数组:", intervals1)
    result1 = merge(intervals1)
    print("合并结果:", result1)  # 期望输出: [[1,6],[8,10],[15,18]]
    
    # 测试用例2: intervals = [[1,4],[4,5]] -> 输出: [[1,5]]
    intervals2 = [[1, 4], [4, 5]]
    print("\n测试用例2:")
    print("区间数组:", intervals2)
    result2 = merge(intervals2)
    print("合并结果:", result2)  # 期望输出: [[1,5]]
    
    # 测试用例3: intervals = [[1,4],[2,3]] -> 输出: [[1,4]]
    intervals3 = [[1, 4], [2, 3]]
    print("\n测试用例3:")
    print("区间数组:", intervals3)
    result3 = merge(intervals3)
    print("合并结果:", result3)  # 期望输出: [[1,4]]
    
    # 测试用例4: intervals = [[1,3]] -> 输出: [[1,3]]
    intervals4 = [[1, 3]]
    print("\n测试用例4:")
    print("区间数组:", intervals4)
    result4 = merge(intervals4)
    print("合并结果:", result4)  # 期望输出: [[1,3]]