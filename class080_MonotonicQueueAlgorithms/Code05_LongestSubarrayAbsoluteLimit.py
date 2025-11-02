# 绝对差不超过限制的最长连续子数组
# 给你一个整数数组 nums ，和一个表示限制的整数 limit，
# 请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit。
# 如果不存在满足条件的子数组，则返回 0。
# 测试链接 : https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/

from collections import deque

'''
题目名称：绝对差不超过限制的最长连续子数组
来源：LeetCode
链接：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/

题目描述：
给你一个整数数组 nums ，和一个表示限制的整数 limit，
请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit。
如果不存在满足条件的子数组，则返回 0。

解题思路：
使用滑动窗口配合双单调队列解决该问题。
1. 使用单调递增队列维护窗口内的最小值
2. 使用单调递减队列维护窗口内的最大值
3. 滑动窗口右边界不断扩展，当窗口内最大值与最小值的差超过limit时，收缩左边界
4. 记录满足条件的最长窗口长度

算法步骤：
1. 初始化双指针和双单调队列
2. 右指针不断向右扩展窗口
3. 维护两个单调队列的性质
4. 当窗口不满足条件时，收缩左边界
5. 记录最长窗口长度

时间复杂度分析：
O(n) - 每个元素最多入队出队一次

空间复杂度分析：
O(n) - 两个单调队列最多存储n个元素

是否最优解：
是，这是处理此类问题的最优解法
'''
def longestSubarray(nums, limit):
    minDq = deque()
    maxDq = deque()
    n = len(nums)
    left = 0
    ans = 0

    for right in range(n):
        # 维护单调递增队列（维护最小值）
        while minDq and nums[minDq[-1]] >= nums[right]:
            minDq.pop()
        minDq.append(right)

        # 维护单调递减队列（维护最大值）
        while maxDq and nums[maxDq[-1]] <= nums[right]:
            maxDq.pop()
        maxDq.append(right)

        # 当窗口不满足条件时，收缩左边界
        while nums[maxDq[0]] - nums[minDq[0]] > limit:
            if minDq[0] == left:
                minDq.popleft()
            if maxDq[0] == left:
                maxDq.popleft()
            left += 1

        # 更新最长窗口长度
        ans = max(ans, right - left + 1)

    return ans

# 测试方法
if __name__ == "__main__":
    # 测试用例1
    nums1 = [8, 2, 4, 7]
    limit1 = 4
    result1 = longestSubarray(nums1, limit1)
    print("测试用例1:")
    print("输入: nums = [8,2,4,7], limit = 4")
    print("输出:", result1)
    # 预期输出: 2

    # 测试用例2
    nums2 = [10, 1, 2, 4, 7, 2]
    limit2 = 5
    result2 = longestSubarray(nums2, limit2)
    print("\n测试用例2:")
    print("输入: nums = [10,1,2,4,7,2], limit = 5")
    print("输出:", result2)
    # 预期输出: 4

    # 测试用例3
    nums3 = [4, 2, 2, 2, 4, 4, 2, 2]
    limit3 = 0
    result3 = longestSubarray(nums3, limit3)
    print("\n测试用例3:")
    print("输入: nums = [4,2,2,2,4,4,2,2], limit = 0")
    print("输出:", result3)
    # 预期输出: 3