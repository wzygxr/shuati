"""
找出到每个位置为止最长的非递减子序列

题目来源：LeetCode 1964. 找出到每个位置为止最长的非递减子序列
题目链接：https://leetcode.cn/problems/find-the-longest-valid-obstacle-course-at-each-position/
题目描述：你打算构建一些障碍赛跑路线。给你一个下标从 0 开始的整数数组 obstacles，数组长度为 n，
其中 obstacles[i] 表示第 i 个障碍的高度。对于每个介于 0 和 n - 1 之间（包含 0 和 n - 1）的下标 i，
在满足下述条件的前提下，请你找出 obstacles 能构成的最长障碍路线的长度。

条件：对于路线中的每个下标 j（0 <= j <= i），必须满足 obstacles[j] <= obstacles[i]。
路线必须包含下标 i。

算法思路：
1. 这实际上是求以每个位置结尾的最长非递减子序列长度
2. 使用贪心+二分查找优化，维护ends数组
3. ends[i]表示长度为i+1的非递减子序列的最小结尾元素
4. 对于每个障碍高度，在ends中查找>obstacles[i]的最左位置

时间复杂度：O(n*logn) - 每个位置二分查找O(logn)
空间复杂度：O(n) - 需要ends数组存储状态
是否最优解：是，这是最优解法

示例：
输入: obstacles = [1,2,3,2]
输出: [1,2,3,3]
解释：
- i=0: [1] -> 长度1
- i=1: [1,2] -> 长度2  
- i=2: [1,2,3] -> 长度3
- i=3: [1,2,3] 或 [1,2,2] -> 长度3

输入: obstacles = [2,2,1]
输出: [1,2,1]
解释：
- i=0: [2] -> 长度1
- i=1: [2,2] -> 长度2
- i=2: [1] -> 长度1
"""

from typing import List
import bisect

class Solution:
    """
    计算每个位置的最长非递减子序列长度
    
    Args:
        obstacles: 障碍高度数组
        
    Returns:
        每个位置的最长非递减子序列长度
    """
    def longestObstacleCourseAtEachPosition(self, obstacles: List[int]) -> List[int]:
        n = len(obstacles)
        result = [0] * n
        ends = []  # ends列表动态维护
        
        for i in range(n):
            # 使用bisect查找>obstacles[i]的最左位置
            # 注意：对于非递减序列，我们使用bisect_right来找到插入位置
            pos = bisect.bisect_right(ends, obstacles[i])
            
            if pos == len(ends):
                # 没有找到，说明obstacles[i]可以延长当前最长非递减子序列
                ends.append(obstacles[i])
                result[i] = len(ends)
            else:
                # 找到了位置，更新该位置的值为obstacles[i]
                ends[pos] = obstacles[i]
                result[i] = pos + 1
        
        return result

    """
    使用动态规划解法（时间复杂度较高，用于对比）
    
    算法思路：
    1. 使用动态规划计算每个位置的最长非递减子序列长度
    2. dp[i]表示以obstacles[i]结尾的最长非递减子序列长度
    3. 对于每个位置i，遍历前面所有位置j，如果obstacles[j] <= obstacles[i]，则更新dp[i]
    
    时间复杂度：O(n²) - 外层循环n次，内层循环最多n次
    空间复杂度：O(n) - 需要dp数组存储状态
    是否最优解：否，存在O(n*logn)的优化解法
    
    Args:
        obstacles: 障碍高度数组
        
    Returns:
        每个位置的最长非递减子序列长度
    """
    def longestObstacleCourseAtEachPositionDP(self, obstacles: List[int]) -> List[int]:
        n = len(obstacles)
        dp = [1] * n  # 每个位置至少可以单独构成长度为1的子序列
        
        for i in range(n):
            for j in range(i):
                if obstacles[j] <= obstacles[i]:
                    dp[i] = max(dp[i], dp[j] + 1)
        
        return dp

def test():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    obstacles1 = [1, 2, 3, 2]
    print("输入:", obstacles1)
    result1 = solution.longestObstacleCourseAtEachPosition(obstacles1)
    print("优化方法输出:", result1)
    print("期望: [1, 2, 3, 3]")
    
    result1_dp = solution.longestObstacleCourseAtEachPositionDP(obstacles1)
    print("DP方法输出:", result1_dp)
    print()
    
    # 测试用例2
    obstacles2 = [2, 2, 1]
    print("输入:", obstacles2)
    result2 = solution.longestObstacleCourseAtEachPosition(obstacles2)
    print("优化方法输出:", result2)
    print("期望: [1, 2, 1]")
    
    result2_dp = solution.longestObstacleCourseAtEachPositionDP(obstacles2)
    print("DP方法输出:", result2_dp)
    print()
    
    # 测试用例3
    obstacles3 = [3, 1, 5, 6, 4, 2]
    print("输入:", obstacles3)
    result3 = solution.longestObstacleCourseAtEachPosition(obstacles3)
    print("优化方法输出:", result3)
    
    result3_dp = solution.longestObstacleCourseAtEachPositionDP(obstacles3)
    print("DP方法输出:", result3_dp)
    print()

if __name__ == "__main__":
    test()