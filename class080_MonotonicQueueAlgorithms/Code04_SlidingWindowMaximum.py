# 滑动窗口最大值
# 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
# 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
# 返回滑动窗口中的最大值。
# 测试链接 : https://leetcode.cn/problems/sliding-window-maximum/

from collections import deque

'''
题目名称：滑动窗口最大值
来源：LeetCode
链接：https://leetcode.cn/problems/sliding-window-maximum/

题目描述：
给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
返回滑动窗口中的最大值。

解题思路：
使用单调递减队列解决该问题。队列中存储数组元素的下标，队列中的下标对应的元素值保持单调递减。
1. 维护队列的单调性：当新元素进入时，从队尾开始比较，如果新元素大于等于队尾元素，则队尾元素出队
2. 维护窗口大小：检查队首元素是否超出窗口范围，如果超出则出队
3. 记录结果：当窗口形成后（i >= k-1），队首元素即为当前窗口的最大值

算法步骤：
1. 遍历数组中的每个元素
2. 维护单调递减队列的性质
3. 移除过期的下标（超出窗口范围）
4. 当窗口大小达到k时，记录最大值（队首元素对应的值）

时间复杂度分析：
O(n) - 每个元素最多入队出队一次

空间复杂度分析：
O(k) - 双端队列最多存储k个元素

是否最优解：
是，这是处理此类问题的最优解法
'''
def maxSlidingWindow(nums, k):
    dq = deque()
    result = []
    
    for i in range(len(nums)):
        # 维护单调性：队尾元素小于当前元素时，队尾出队
        while dq and nums[dq[-1]] <= nums[i]:
            dq.pop()
        # 当前下标入队
        dq.append(i)
        
        # 检查队首元素是否过期
        if dq[0] <= i - k:
            dq.popleft()
        
        # 当窗口形成后，记录最大值
        if i >= k - 1:
            result.append(nums[dq[0]])
    
    return result

# 测试方法
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    result1 = maxSlidingWindow(nums1, k1)
    print("测试用例1:")
    print("输入: nums = [1,3,-1,-3,5,3,6,7], k = 3")
    print("输出:", result1)
    # 预期输出: [3,3,5,5,6,7]
    
    # 测试用例2
    nums2 = [1]
    k2 = 1
    result2 = maxSlidingWindow(nums2, k2)
    print("\n测试用例2:")
    print("输入: nums = [1], k = 1")
    print("输出:", result2)
    # 预期输出: [1]