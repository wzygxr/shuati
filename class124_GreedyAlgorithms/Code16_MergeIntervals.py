# 合并区间（Merge Intervals）
# 题目来源：LeetCode 56
# 题目链接：https://leetcode.cn/problems/merge-intervals/
# 
# 问题描述：
# 以数组intervals表示若干个区间的集合，请合并所有重叠的区间，并返回一个不重叠的区间数组。
# 
# 算法思路：
# 使用贪心策略，按照区间开始时间排序：
# 1. 将区间按照开始时间从小到大排序
# 2. 遍历排序后的区间，维护当前合并区间
# 3. 如果当前区间与合并区间重叠，更新合并区间的结束时间
# 4. 否则，将当前合并区间加入结果，开始新的合并区间
# 
# 时间复杂度：O(n log n) - 排序的时间复杂度
# 空间复杂度：O(n) - 需要存储结果区间
# 
# 是否最优解：是。这是该问题的最优解法。
# 
# 适用场景：
# 1. 区间合并问题
# 2. 重叠区间处理
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
# 1. LeetCode 57. 插入区间 - 区间插入问题
# 2. LeetCode 252. 会议室 - 区间重叠判断
# 3. LeetCode 253. 会议室 II - 区间重叠计数
# 4. 牛客网 NC135 买票需要多少时间 - 队列模拟相关
# 5. LintCode 391. 数飞机 - 区间调度相关
# 6. HackerRank - Jim and the Orders - 贪心调度问题
# 7. CodeChef - TACHSTCK - 区间配对问题
# 8. AtCoder ABC104C - All Green - 动态规划相关
# 9. Codeforces 1363C - Game On Leaves - 博弈论相关
# 10. POJ 3169 - Layout - 差分约束系统

class Solution:
    """
    合并重叠区间
    
    Args:
        intervals: List[List[int]] - 区间数组，每个区间包含开始和结束时间
    
    Returns:
        List[List[int]] - 合并后的不重叠区间数组
    """
    def merge(self, intervals):
        # 边界条件检查
        if not intervals:
            return []
        
        n = len(intervals)
        if n == 1:
            return intervals  # 只有一个区间，直接返回
        
        # 按照区间开始时间排序
        intervals.sort(key=lambda x: x[0])
        
        result = []
        current_interval = intervals[0]
        result.append(current_interval)
        
        for i in range(1, n):
            current_end = current_interval[1]
            next_start = intervals[i][0]
            next_end = intervals[i][1]
            
            # 如果当前区间与下一个区间重叠
            if current_end >= next_start:
                # 合并区间，取较大的结束时间
                current_interval[1] = max(current_end, next_end)
                result[-1][1] = current_interval[1]  # 更新结果中的最后一个区间
            else:
                # 不重叠，开始新的合并区间
                current_interval = intervals[i]
                result.append(current_interval)
        
        return result


def main():
    solution = Solution()
    
    # 测试用例1: 基本情况 - 有重叠区间
    intervals1 = [[1, 3], [2, 6], [8, 10], [15, 18]]
    result1 = solution.merge(intervals1)
    print("测试用例1:")
    print(f"输入区间: {intervals1}")
    print(f"合并结果: {result1}")
    print("期望输出: [[1, 6], [8, 10], [15, 18]]")
    print()
    
    # 测试用例2: 基本情况 - 无重叠区间
    intervals2 = [[1, 4], [5, 8], [9, 12]]
    result2 = solution.merge(intervals2)
    print("测试用例2:")
    print(f"输入区间: {intervals2}")
    print(f"合并结果: {result2}")
    print("期望输出: [[1, 4], [5, 8], [9, 12]]")
    print()
    
    # 测试用例3: 复杂情况 - 完全重叠
    intervals3 = [[1, 4], [2, 3], [3, 5]]
    result3 = solution.merge(intervals3)
    print("测试用例3:")
    print(f"输入区间: {intervals3}")
    print(f"合并结果: {result3}")
    print("期望输出: [[1, 5]]")
    print()
    
    # 测试用例4: 边界情况 - 单元素数组
    intervals4 = [[1, 3]]
    result4 = solution.merge(intervals4)
    print("测试用例4:")
    print(f"输入区间: {intervals4}")
    print(f"合并结果: {result4}")
    print("期望输出: [[1, 3]]")
    print()
    
    # 测试用例5: 边界情况 - 空数组
    intervals5 = []
    result5 = solution.merge(intervals5)
    print("测试用例5:")
    print(f"输入区间: {intervals5}")
    print(f"合并结果: {result5}")
    print("期望输出: []")
    print()
    
    # 测试用例6: 复杂情况 - 包含区间
    intervals6 = [[1, 10], [2, 3], [4, 5], [6, 7], [8, 9]]
    result6 = solution.merge(intervals6)
    print("测试用例6:")
    print(f"输入区间: {intervals6}")
    print(f"合并结果: {result6}")
    print("期望输出: [[1, 10]]")


if __name__ == "__main__":
    main()