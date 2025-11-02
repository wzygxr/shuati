import heapq

class Solution:
    """
    相关题目9: LeetCode 239. 滑动窗口最大值
    题目链接: https://leetcode.cn/problems/sliding-window-maximum/
    题目描述: 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
    你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。
    解题思路: 使用优先队列（最大堆）维护当前窗口内的元素，堆顶始终是最大值
    时间复杂度: O(n log k)，每个元素入堆和出堆的时间复杂度为O(log k)
    空间复杂度: O(k)，堆中最多存储k个元素
    是否最优解: 不是最优解，最优解是使用单调队列，时间复杂度为O(n)，但这里使用堆作为实现方案
    
    本题属于堆的典型应用场景：需要在滑动窗口中快速获取最大值
    """
    
    def maxSlidingWindow(self, nums, k):
        """
        使用最大堆解决滑动窗口最大值问题
        
        Args:
            nums: 输入的整数数组
            k: 滑动窗口的大小
            
        Returns:
            list: 每个滑动窗口的最大值组成的数组
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查输入数组是否为None或空
        if not nums:
            raise ValueError("输入数组不能为空")
        
        # 异常处理：检查k是否在有效范围内
        if k <= 0 or k > len(nums):
            raise ValueError(f"k的值必须在1到数组长度之间，当前k={k}，数组长度={len(nums)}")
        
        n = len(nums)
        # 结果数组的长度是n - k + 1
        result = [0] * (n - k + 1)
        
        # 创建最大堆，Python的heapq默认是最小堆，所以我们通过存储负数来模拟最大堆
        # 堆中存储的是[-值, 索引]的元组，按值降序排列
        max_heap = []
        
        # 首先将前k个元素加入堆
        for i in range(k):
            heapq.heappush(max_heap, (-nums[i], i))
        
        # 第一个窗口的最大值
        result[0] = -max_heap[0][0]
        
        # 滑动窗口从k开始，逐个处理剩余元素
        for i in range(k, n):
            # 将当前元素加入堆
            heapq.heappush(max_heap, (-nums[i], i))
            
            # 移除堆中不在当前窗口范围内的元素（索引小于i - k + 1的元素）
            # 注意：Python的heapq没有直接删除任意元素的方法，所以我们只是在需要时检查堆顶元素是否在窗口内
            while max_heap[0][1] < i - k + 1:
                heapq.heappop(max_heap)
            
            # 当前堆顶就是当前窗口的最大值
            result[i - k + 1] = -max_heap[0][0]
        
        return result
    
    def maxSlidingWindowWithDeque(self, nums, k):
        """
        使用双端队列解决滑动窗口最大值问题（最优解，O(n)时间复杂度）
        
        Args:
            nums: 输入的整数数组
            k: 滑动窗口的大小
            
        Returns:
            list: 每个滑动窗口的最大值组成的数组
        """
        if not nums or k <= 0:
            return []
        
        from collections import deque
        
        n = len(nums)
        result = []
        # 双端队列，存储的是索引，且对应的元素按降序排列
        # 队首元素始终是当前窗口的最大值
        dq = deque()
        
        for i in range(n):
            # 1. 移除队列中不在当前窗口范围内的元素（队首元素索引 < i-k+1）
            while dq and dq[0] < i - k + 1:
                dq.popleft()
            
            # 2. 移除队列中所有小于当前元素的值对应的索引
            # 这保证了队列中的索引对应的元素是单调递减的
            while dq and nums[dq[-1]] < nums[i]:
                dq.pop()
            
            # 3. 将当前元素的索引加入队列
            dq.append(i)
            
            # 4. 当窗口形成时（i >= k-1），队首元素就是当前窗口的最大值
            if i >= k - 1:
                result.append(nums[dq[0]])
        
        return result

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    solution = Solution()
    
    # 测试用例1：基本情况
    print("测试用例1：")
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    result1 = solution.maxSlidingWindow(nums1, k1)
    print(f"maxSlidingWindow输出: {result1}")  # 期望输出: [3, 3, 5, 5, 6, 7]
    assert result1 == [3, 3, 5, 5, 6, 7], f"测试失败：期望[3, 3, 5, 5, 6, 7]，实际{result1}"
    
    # 测试双端队列实现
    deque_result1 = solution.maxSlidingWindowWithDeque(nums1, k1)
    print(f"maxSlidingWindowWithDeque输出: {deque_result1}")
    assert deque_result1 == [3, 3, 5, 5, 6, 7], f"测试失败：期望[3, 3, 5, 5, 6, 7]，实际{deque_result1}"
    
    # 测试用例2：k=1，只有一个元素的窗口
    print("\n测试用例2：")
    nums2 = [1, -1]
    k2 = 1
    result2 = solution.maxSlidingWindow(nums2, k2)
    print(f"输出: {result2}")  # 期望输出: [1, -1]
    assert result2 == [1, -1], f"测试失败：期望[1, -1]，实际{result2}"
    
    # 测试用例3：k等于数组长度，整个数组为一个窗口
    print("\n测试用例3：")
    nums3 = [9, 10, 9, -7, -4, -8, 2, -6]
    k3 = 5
    result3 = solution.maxSlidingWindow(nums3, k3)
    print(f"输出: {result3}")  # 期望输出: [10, 10, 9, 2]
    assert result3 == [10, 10, 9, 2], f"测试失败：期望[10, 10, 9, 2]，实际{result3}"
    
    # 测试用例4：边界情况 - 数组只有一个元素
    print("\n测试用例4：")
    nums4 = [1]
    k4 = 1
    result4 = solution.maxSlidingWindow(nums4, k4)
    print(f"输出: {result4}")  # 期望输出: [1]
    assert result4 == [1], f"测试失败：期望[1]，实际{result4}"
    
    # 测试用例5：异常情况
    print("\n测试用例5（异常处理）：")
    try:
        solution.maxSlidingWindow([], 1)
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    try:
        solution.maxSlidingWindow([1, 2, 3], 0)
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    print("\n所有测试用例通过！")

# 运行测试
if __name__ == "__main__":
    test_solution()