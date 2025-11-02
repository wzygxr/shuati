# 用最少数量的箭引爆气球（Minimum Number of Arrows to Burst Balloons）
# 题目来源：LeetCode 452
# 题目链接：https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
# 
# 问题描述：
# 在二维空间中有许多球形的气球，每个气球在水平方向上的直径范围是[xstart, xend]。
# 用最少数量的箭引爆所有气球。一支箭可以垂直向上射出，在xstart和xend之间穿过气球。
# 只要箭的x坐标在气球的直径范围内，气球就会被引爆。
# 
# 算法思路：
# 使用贪心策略，按照气球结束坐标排序：
# 1. 将气球按照结束坐标从小到大排序
# 2. 遍历排序后的气球，记录当前箭的位置
# 3. 如果当前气球的开始坐标大于箭的位置，说明需要新的箭
# 4. 否则，当前箭可以引爆这个气球
# 
# 时间复杂度：O(n log n) - 排序的时间复杂度
# 空间复杂度：O(1) - 只使用了常数额外空间
# 
# 是否最优解：是。这是该问题的最优解法。
# 
# 适用场景：
# 1. 区间覆盖问题
# 2. 最小点覆盖区间问题
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
# 1. LeetCode 435. 无重叠区间 - 类似区间问题
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
    计算引爆所有气球所需的最少箭数
    
    Args:
        points: List[List[int]] - 气球直径范围数组，每个元素是[xstart, xend]
    
    Returns:
        int - 最少需要的箭数
    """
    def findMinArrowShots(self, points):
        # 边界条件检查
        if not points:
            return 0
        
        n = len(points)
        if n == 1:
            return 1  # 只有一个气球，需要一支箭
        
        # 按照气球结束坐标排序
        points.sort(key=lambda x: x[1])
        
        arrows = 1  # 至少需要一支箭
        arrow_pos = points[0][1]  # 第一支箭的位置
        
        for i in range(1, n):
            # 如果当前气球的开始坐标大于箭的位置，需要新的箭
            if points[i][0] > arrow_pos:
                arrows += 1
                arrow_pos = points[i][1]  # 更新箭的位置
            # 否则，当前箭可以引爆这个气球，继续使用同一支箭
        
        return arrows


def main():
    solution = Solution()
    
    # 测试用例1: 基本情况 - 有重叠气球
    points1 = [[10, 16], [2, 8], [1, 6], [7, 12]]
    result1 = solution.findMinArrowShots(points1)
    print("测试用例1:")
    print(f"气球范围: {points1}")
    print(f"最少箭数: {result1}")
    print("期望输出: 2")
    print()
    
    # 测试用例2: 基本情况 - 无重叠气球
    points2 = [[1, 2], [3, 4], [5, 6], [7, 8]]
    result2 = solution.findMinArrowShots(points2)
    print("测试用例2:")
    print(f"气球范围: {points2}")
    print(f"最少箭数: {result2}")
    print("期望输出: 4")
    print()
    
    # 测试用例3: 复杂情况 - 完全重叠
    points3 = [[1, 2], [2, 3], [3, 4], [4, 5]]
    result3 = solution.findMinArrowShots(points3)
    print("测试用例3:")
    print(f"气球范围: {points3}")
    print(f"最少箭数: {result3}")
    print("期望输出: 2")
    print()
    
    # 测试用例4: 边界情况 - 单元素数组
    points4 = [[1, 2]]
    result4 = solution.findMinArrowShots(points4)
    print("测试用例4:")
    print(f"气球范围: {points4}")
    print(f"最少箭数: {result4}")
    print("期望输出: 1")
    print()
    
    # 测试用例5: 边界情况 - 空数组
    points5 = []
    result5 = solution.findMinArrowShots(points5)
    print("测试用例5:")
    print(f"气球范围: {points5}")
    print(f"最少箭数: {result5}")
    print("期望输出: 0")
    print()
    
    # 测试用例6: 复杂情况 - 大数测试
    points6 = [[-2147483646, -2147483645], [2147483646, 2147483647]]
    result6 = solution.findMinArrowShots(points6)
    print("测试用例6:")
    print(f"气球范围: {points6}")
    print(f"最少箭数: {result6}")
    print("期望输出: 2")


if __name__ == "__main__":
    main()