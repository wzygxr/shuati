from collections import deque

"""
相关题目4: LeetCode 239. 滑动窗口最大值
题目链接: https://leetcode.cn/problems/sliding-window-maximum/
题目描述: 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
解题思路: 使用双端队列维护窗口中的最大值
时间复杂度: O(n)
空间复杂度: O(k)
是否最优解: 是，这是处理滑动窗口最大值的最优解法
"""
def maxSlidingWindow(nums, k):
    if not nums or k <= 0:
        return []
    
    # 双端队列，存储数组索引，队首是当前窗口的最大值索引
    dq = deque()
    result = []
    
    for i in range(len(nums)):
        # 移除队列中超出窗口范围的索引
        while dq and dq[0] <= i - k:
            dq.popleft()
        
        # 维护队列的单调性，移除所有小于当前元素的索引
        while dq and nums[dq[-1]] <= nums[i]:
            dq.pop()
        
        # 将当前索引加入队列
        dq.append(i)
        
        # 当窗口形成后，记录最大值
        if i >= k - 1:
            result.append(nums[dq[0]])
    
    return result

# 测试方法
if __name__ == "__main__":
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    result1 = maxSlidingWindow(nums1, k1)
    print("示例1输出:", result1)  # 期望输出: [3, 3, 5, 5, 6, 7]
    
    nums2 = [1]
    k2 = 1
    result2 = maxSlidingWindow(nums2, k2)
    print("示例2输出:", result2)  # 期望输出: [1]