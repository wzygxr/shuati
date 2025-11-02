"""
LeetCode 239. Sliding Window Maximum

题目描述：
给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
返回滑动窗口中的最大值。

解题思路：
这是一个经典的滑动窗口问题，可以使用多种方法解决。

方法一：使用优先队列（最大堆）
1. 使用优先队列维护窗口内的元素，队列中存储 (value, index) 对
2. 按照值的大小排序，最大值在队首
3. 当窗口滑动时，添加新元素并移除超出窗口范围的元素

方法二：使用双端队列（单调队列）
1. 维护一个单调递减的双端队列
2. 队列中存储数组下标，对应的值单调递减
3. 当新元素加入时，从队尾移除所有小于等于新元素的元素
4. 从队首移除超出窗口范围的元素

时间复杂度：
- 优先队列方法：O(n log k)
- 双端队列方法：O(n)
空间复杂度：O(k)

相关题目：
- LeetCode 220. 存在重复元素 III（TreeSet滑动窗口）
- LeetCode 219. 存在重复元素 II（哈希表滑动窗口）
- LeetCode 480. 滑动窗口中位数
"""

import heapq
from collections import deque

def maxSlidingWindowWithHeap(nums, k):
    """
    使用优先队列（最大堆）解决滑动窗口最大值问题
    
    Args:
        nums: 整数数组
        k: 滑动窗口大小
    
    Returns:
        每个滑动窗口中的最大值数组
    """
    if not nums or k <= 0:
        return []
    
    if k == 1:
        return nums[:]
    
    n = len(nums)
    # 结果数组
    result = []
    
    # 最大堆，存储 (-value, index) 对（Python的heapq是最小堆）
    max_heap = []
    
    # 初始化堆，添加前 k-1 个元素
    for i in range(min(k - 1, n)):
        heapq.heappush(max_heap, (-nums[i], i))
    
    # 处理每个窗口
    for i in range(k - 1, n):
        # 添加当前元素
        heapq.heappush(max_heap, (-nums[i], i))
        
        # 移除超出窗口范围的元素
        while max_heap and max_heap[0][1] <= i - k:
            heapq.heappop(max_heap)
        
        # 记录当前窗口的最大值
        result.append(-max_heap[0][0])
    
    return result

def maxSlidingWindowWithDeque(nums, k):
    """
    使用双端队列（单调队列）解决滑动窗口最大值问题
    
    Args:
        nums: 整数数组
        k: 滑动窗口大小
    
    Returns:
        每个滑动窗口中的最大值数组
    """
    if not nums or k <= 0:
        return []
    
    if k == 1:
        return nums[:]
    
    n = len(nums)
    # 结果数组
    result = []
    
    # 双端队列，存储数组下标
    # 队列中对应的值保持单调递减
    dq = deque()
    
    for i in range(n):
        # 移除队首超出窗口范围的元素
        while dq and dq[0] <= i - k:
            dq.popleft()
        
        # 从队尾移除所有小于当前元素的元素
        while dq and nums[dq[-1]] <= nums[i]:
            dq.pop()
        
        # 添加当前元素的下标
        dq.append(i)
        
        # 当窗口大小达到 k 时，记录最大值
        if i >= k - 1:
            result.append(nums[dq[0]])
    
    return result

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    print("测试用例1:")
    print(f"输入: nums = {nums1}, k = {k1}")
    print(f"使用优先队列: {maxSlidingWindowWithHeap(nums1, k1)}")
    print(f"使用双端队列: {maxSlidingWindowWithDeque(nums1, k1)}")
    # 期望输出: [3, 3, 5, 5, 6, 7]
    
    # 测试用例2
    nums2 = [1]
    k2 = 1
    print("\n测试用例2:")
    print(f"输入: nums = {nums2}, k = {k2}")
    print(f"使用优先队列: {maxSlidingWindowWithHeap(nums2, k2)}")
    print(f"使用双端队列: {maxSlidingWindowWithDeque(nums2, k2)}")
    # 期望输出: [1]
    
    # 测试用例3
    nums3 = [1, -1]
    k3 = 1
    print("\n测试用例3:")
    print(f"输入: nums = {nums3}, k = {k3}")
    print(f"使用优先队列: {maxSlidingWindowWithHeap(nums3, k3)}")
    print(f"使用双端队列: {maxSlidingWindowWithDeque(nums3, k3)}")
    # 期望输出: [1, -1]
    
    # 测试用例4
    nums4 = [9, 11]
    k4 = 2
    print("\n测试用例4:")
    print(f"输入: nums = {nums4}, k = {k4}")
    print(f"使用优先队列: {maxSlidingWindowWithHeap(nums4, k4)}")
    print(f"使用双端队列: {maxSlidingWindowWithDeque(nums4, k4)}")
    # 期望输出: [11]