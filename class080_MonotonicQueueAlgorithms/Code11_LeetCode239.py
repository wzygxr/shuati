from collections import deque

"""
题目名称：LeetCode 239. 滑动窗口最大值
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/sliding-window-maximum/
题目难度：困难

题目描述：
给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
返回滑动窗口中的最大值。
"""

def maxSlidingWindow(nums, k):
    if not nums or k <= 0:
        return []
    
    n = len(nums)
    if k == 1:
        return nums
    
    result = []
    dq = deque()
    
    for i in range(n):
        while dq and nums[dq[-1]] <= nums[i]:
            dq.pop()
        
        dq.append(i)
        
        if dq[0] <= i - k:
            dq.popleft()
        
        if i >= k - 1:
            result.append(nums[dq[0]])
    
    return result

def test_max_sliding_window():
    print("=== LeetCode 239 测试用例 ===")
    
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    result1 = maxSlidingWindow(nums1, k1)
    print("测试用例1 - 输入: [1,3,-1,-3,5,3,6,7], k=3")
    print(f"预期输出: [3,3,5,5,6,7], 实际输出: {result1}")
    
    print("\n=== 算法分析 ===")
    print("时间复杂度: O(n) - 每个元素最多入队出队一次")
    print("空间复杂度: O(k) - 双端队列最多存储k个元素")
    print("最优解: ✅ 是")

if __name__ == "__main__":
    test_max_sliding_window()