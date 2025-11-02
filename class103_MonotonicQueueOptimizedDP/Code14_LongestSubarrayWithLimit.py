import collections

'''
LeetCode 1438 绝对差不超过限制的最长连续子数组
题目来源：LeetCode
网址：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/

题目描述：
给你一个整数数组 nums ，和一个表示限制的整数 limit，请你返回最长连续子数组的长度，
该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit 。

解题思路：
这是一个使用双端单调队列的滑动窗口问题。
- 使用一个滑动窗口，维护窗口内的最大值和最小值
- 使用两个双端队列，一个维护窗口内的最大值（单调递减队列），一个维护窗口内的最小值（单调递增队列）
- 当窗口内的最大值和最小值之差超过limit时，移动左指针缩小窗口
- 记录窗口的最大长度

时间复杂度：O(n)，每个元素最多被加入和弹出队列各一次
空间复杂度：O(n)，最坏情况下队列中存储所有元素
'''

'''
找出最长子数组，其中任意两个元素的绝对差不超过limit
参数：
    nums: 整数数组
    limit: 绝对差限制
返回值：
    最长子数组的长度
'''
def longestSubarray(nums, limit):
    n = len(nums)
    left = 0  # 滑动窗口的左边界
    max_length = 0  # 记录最长子数组的长度
    
    # 单调递减队列，存储的是索引，队列头部是当前窗口的最大值
    max_deque = collections.deque()
    # 单调递增队列，存储的是索引，队列头部是当前窗口的最小值
    min_deque = collections.deque()
    
    # 遍历右边界
    for right in range(n):
        # 维护单调递减队列，确保队列头部是最大值
        while max_deque and nums[right] >= nums[max_deque[-1]]:
            max_deque.pop()
        max_deque.append(right)
        
        # 维护单调递增队列，确保队列头部是最小值
        while min_deque and nums[right] <= nums[min_deque[-1]]:
            min_deque.pop()
        min_deque.append(right)
        
        # 检查当前窗口是否满足条件
        while max_deque and min_deque and nums[max_deque[0]] - nums[min_deque[0]] > limit:
            # 如果最大值的索引是左边界，则移除
            if max_deque[0] == left:
                max_deque.popleft()
            # 如果最小值的索引是左边界，则移除
            if min_deque[0] == left:
                min_deque.popleft()
            # 移动左边界
            left += 1
        
        # 更新最大长度
        max_length = max(max_length, right - left + 1)
    
    return max_length

# 主函数，处理输入输出
def main():
    import sys
    nums = list(map(int, sys.stdin.readline().split()))
    limit = int(sys.stdin.readline())
    print(longestSubarray(nums, limit))

if __name__ == "__main__":
    main()