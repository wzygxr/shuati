# 无重叠区间（Non-overlapping Intervals）
# 题目来源：LeetCode 435
# 题目链接：https://leetcode.cn/problems/non-overlapping-intervals/
# 
# 问题描述：
# 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
# 
# 算法思路：
# 使用贪心策略，按照区间结束时间排序：
# 1. 将区间按照结束时间从小到大排序
# 2. 遍历排序后的区间，记录当前选择的最后一个区间的结束时间
# 3. 如果当前区间的开始时间大于等于前一个区间的结束时间，说明不重叠，选择该区间
# 4. 否则，需要移除该区间，计数加1
# 
# 时间复杂度：O(n log n) - 排序的时间复杂度
# 空间复杂度：O(1) - 只使用了常数额外空间
# 
# 是否最优解：是。这是该问题的最优解法。
# 
# 适用场景：
# 1. 区间调度问题
# 2. 最大不重叠区间选择
# 
# 异常处理：
# 1. 处理空数组情况
# 2. 处理单元素数组
# 
# 工程化考量：
# 1. 输入验证：检查数组是否为空
# 2. 边界条件：处理单元素和双元素数组
# 3. 性能优化：使用内置排序提高效率
# 
# 相关题目：
# 1. LeetCode 452. 用最少数量的箭引爆气球 - 类似区间问题
# 2. LeetCode 56. 合并区间 - 区间合并问题
# 3. LeetCode 252. 会议室 - 区间重叠判断
# 4. 牛客网 NC135 买票需要多少时间 - 队列模拟相关
# 5. LintCode 391. 数飞机 - 区间调度相关
# 6. HackerRank - Jim and the Orders - 贪心调度问题
# 7. CodeChef - TACHSTCK - 区间配对问题
# 8. AtCoder ABC104C - All Green - 动态规划相关
# 9. Codeforces 1363C - Game On Leaves - 博弈论相关
# 10. POJ 3169 - Layout - 差分约束系统

class Solution:
    """
    计算需要移除的最小区间数量
    
    Args:
        intervals: List[List[int]] - 区间数组，每个区间包含开始和结束时间
    
    Returns:
        int - 需要移除的最小区间数量
    """
    def eraseOverlapIntervals(self, intervals):
        # 边界条件检查
        if not intervals:
            return 0
        
        n = len(intervals)
        if n == 1:
            return 0  # 只有一个区间，不需要移除
        
        # 按照区间结束时间排序
        intervals.sort(key=lambda x: x[1])
        
        count = 0  # 需要移除的区间数量
        end = intervals[0][1]  # 当前选择的最后一个区间的结束时间
        
        for i in range(1, n):
            # 如果当前区间的开始时间小于前一个区间的结束时间，说明重叠
            if intervals[i][0] < end:
                count += 1  # 需要移除当前区间
            else:
                # 不重叠，更新结束时间
                end = intervals[i][1]
        
        return count


def main():
    solution = Solution()
    
    # 测试用例1: 基本情况 - 有重叠区间
    intervals1 = [[1, 2], [2, 3], [3, 4], [1, 3]]
    result1 = solution.eraseOverlapIntervals(intervals1)
    print("测试用例1:")
    print(f"区间数组: {intervals1}")
    print(f"需要移除的区间数量: {result1}")
    print("期望输出: 1")
    print()
    
    # 测试用例2: 基本情况 - 无重叠区间
    intervals2 = [[1, 2], [2, 3]]
    result2 = solution.eraseOverlapIntervals(intervals2)
    print("测试用例2:")
    print(f"区间数组: {intervals2}")
    print(f"需要移除的区间数量: {result2}")
    print("期望输出: 0")
    print()
    
    # 测试用例3: 复杂情况 - 多个重叠
    intervals3 = [[1, 100], [11, 22], [1, 11], [2, 12]]
    result3 = solution.eraseOverlapIntervals(intervals3)
    print("测试用例3:")
    print(f"区间数组: {intervals3}")
    print(f"需要移除的区间数量: {result3}")
    print("期望输出: 3")
    print()
    
    # 测试用例4: 边界情况 - 单元素数组
    intervals4 = [[1, 2]]
    result4 = solution.eraseOverlapIntervals(intervals4)
    print("测试用例4:")
    print(f"区间数组: {intervals4}")
    print(f"需要移除的区间数量: {result4}")
    print("期望输出: 0")
    print()
    
    # 测试用例5: 边界情况 - 空数组
    intervals5 = []
    result5 = solution.eraseOverlapIntervals(intervals5)
    print("测试用例5:")
    print(f"区间数组: {intervals5}")
    print(f"需要移除的区间数量: {result5}")
    print("期望输出: 0")
    print()
    
    # 测试用例6: 复杂情况 - 完全重叠
    intervals6 = [[1, 10], [2, 9], [3, 8], [4, 7]]
    result6 = solution.eraseOverlapIntervals(intervals6)
    print("测试用例6:")
    print(f"区间数组: {intervals6}")
    print(f"需要移除的区间数量: {result6}")
    print("期望输出: 3")


if __name__ == "__main__":
    main()