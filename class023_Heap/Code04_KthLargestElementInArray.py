import heapq

class Solution:
    """
    相关题目1: LeetCode 215. 数组中的第K个最大元素
    题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
    题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
    解题思路: 使用大小为k的最小堆维护前k个最大元素，遍历数组时保持堆的大小不超过k
    时间复杂度: O(n log k)，其中n是数组长度，每个元素最多入堆出堆一次，每次堆操作复杂度为log k
    空间复杂度: O(k)，堆的大小始终保持为k
    是否最优解: 是，这是处理动态第K大元素的经典解法，虽然理论上可以用快速选择算法达到O(n)的平均时间复杂度，但堆解法在数据流场景更有优势
    
    本题属于Top K问题的典型应用，堆算法是解决此类问题的最优选择之一
    """
    
    def findKthLargest(self, nums, k):
        """
        查找数组中第K个最大元素
        
        Args:
            nums: 输入整数数组
            k: 第K大的元素的位置（从1开始计数）
            
        Returns:
            int: 第K大的元素值
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查输入数组是否为空
        if not nums:
            raise ValueError("输入数组不能为空")
        
        # 异常处理：检查k是否在有效范围内
        if k <= 0 or k > len(nums):
            raise ValueError(f"k的值必须在1到数组长度之间，当前k={k}，数组长度={len(nums)}")
        
        # 使用最小堆维护前k个最大元素
        # Python的heapq模块实现的是最小堆
        min_heap = []
        
        # 遍历数组中的每个元素
        for num in nums:
            # 调试信息：打印当前处理的元素和堆的状态
            # print(f"Processing: {num}, Heap size: {len(min_heap)}, Heap: {min_heap}")
            
            if len(min_heap) < k:
                # 如果堆的大小小于k，直接将当前元素加入堆
                heapq.heappush(min_heap, num)
            elif num > min_heap[0]:
                # 如果堆的大小已达到k，且当前元素大于堆顶元素
                # 则移除堆顶元素（当前k个元素中最小的），并加入新元素
                heapq.heappop(min_heap)
                heapq.heappush(min_heap, num)
            # 否则（当前元素小于等于堆顶元素），不做任何操作
        
        # 此时堆顶元素就是第k个最大元素
        return min_heap[0]

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    solution = Solution()
    
    # 测试用例1：普通情况
    nums1 = [3, 2, 1, 5, 6, 4]
    k1 = 2
    result1 = solution.findKthLargest(nums1, k1)
    print(f"示例1输出: {result1}")  # 期望输出: 5
    assert result1 == 5, f"测试用例1失败，期望5，实际得到{result1}"
    
    # 测试用例2：包含重复元素
    nums2 = [3, 2, 3, 1, 2, 4, 5, 5, 6]
    k2 = 4
    result2 = solution.findKthLargest(nums2, k2)
    print(f"示例2输出: {result2}")  # 期望输出: 4
    assert result2 == 4, f"测试用例2失败，期望4，实际得到{result2}"
    
    # 测试用例3：边界情况 - k等于数组长度
    nums3 = [3, 2, 1]
    k3 = 3
    result3 = solution.findKthLargest(nums3, k3)
    print(f"示例3输出: {result3}")  # 期望输出: 1
    assert result3 == 1, f"测试用例3失败，期望1，实际得到{result3}"
    
    # 测试用例4：边界情况 - k等于1
    nums4 = [3, 2, 1]
    k4 = 1
    result4 = solution.findKthLargest(nums4, k4)
    print(f"示例4输出: {result4}")  # 期望输出: 3
    assert result4 == 3, f"测试用例4失败，期望3，实际得到{result4}"
    
    try:
        # 测试用例5：异常测试 - 空数组
        solution.findKthLargest([], 1)
        print("测试用例5失败：未抛出预期的异常")
    except ValueError as e:
        print(f"测试用例5成功捕获异常: {e}")
    
    try:
        # 测试用例6：异常测试 - k超出范围
        solution.findKthLargest([1, 2, 3], 4)
        print("测试用例6失败：未抛出预期的异常")
    except ValueError as e:
        print(f"测试用例6成功捕获异常: {e}")

# 运行测试
if __name__ == "__main__":
    test_solution()
    print("所有测试用例通过！")