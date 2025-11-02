import heapq
from typing import List
import collections

class Solution:
    """
    480. 滑动窗口中位数
    中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
    给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。
    你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
    
    解题思路：
    使用两个堆（最大堆和最小堆）来维护滑动窗口的中位数
    最大堆存储窗口左半部分（较小的一半），最小堆存储窗口右半部分（较大的一半）
    保持两个堆的大小平衡，最大堆的大小等于最小堆的大小或比最小堆大1
    
    时间复杂度：O(n*log k)，其中n是数组长度，k是窗口大小
    空间复杂度：O(k)，用于存储窗口内的元素
    
    是否最优解：是，这是处理滑动窗口中位数的最优解法
    
    测试链接：https://leetcode.cn/problems/sliding-window-median/
    """
    
    def medianSlidingWindow(self, nums: List[int], k: int) -> List[float]:
        """
        计算滑动窗口中位数
        
        Args:
            nums: 输入数组
            k: 窗口大小
            
        Returns:
            每个窗口中位数的数组
        """
        if not nums or k <= 0:
            return []
        
        n = len(nums)
        result = []
        
        # 最大堆（存储较小的一半），最小堆（存储较大的一半）
        # Python中最小堆可以通过取负数实现最大堆
        max_heap = []  # 存储负数，实现最大堆
        min_heap = []  # 正常的最小堆
        
        # 初始化第一个窗口
        for i in range(k):
            self.add_number(nums[i], max_heap, min_heap)
        
        result.append(self.get_median(max_heap, min_heap, k))
        
        # 滑动窗口
        for i in range(k, n):
            # 移除窗口最左边的元素
            self.remove_number(nums[i - k], max_heap, min_heap)
            # 添加新元素
            self.add_number(nums[i], max_heap, min_heap)
            # 计算当前窗口中位数
            result.append(self.get_median(max_heap, min_heap, k))
        
        return result
    
    def add_number(self, num: int, max_heap: List[int], min_heap: List[int]) -> None:
        """
        添加数字到堆中，保持堆的平衡
        """
        # 先添加到最大堆（存储负数）
        heapq.heappush(max_heap, -num)
        # 将最大堆的最大值移动到最小堆
        heapq.heappush(min_heap, -heapq.heappop(max_heap))
        
        # 如果最小堆的大小大于最大堆，重新平衡
        if len(min_heap) > len(max_heap):
            heapq.heappush(max_heap, -heapq.heappop(min_heap))
    
    def remove_number(self, num: int, max_heap: List[int], min_heap: List[int]) -> None:
        """
        从堆中移除数字，保持堆的平衡
        """
        # 判断数字在哪个堆中
        if num <= -max_heap[0]:
            # 从最大堆中移除
            # 由于Python堆不支持直接删除，需要重建堆
            temp = []
            while max_heap and -max_heap[0] != num:
                temp.append(heapq.heappop(max_heap))
            if max_heap:
                heapq.heappop(max_heap)
            for val in temp:
                heapq.heappush(max_heap, val)
            
            # 如果最大堆的大小小于最小堆，从最小堆移动一个元素到最大堆
            if len(max_heap) < len(min_heap):
                heapq.heappush(max_heap, -heapq.heappop(min_heap))
        else:
            # 从最小堆中移除
            temp = []
            while min_heap and min_heap[0] != num:
                temp.append(heapq.heappop(min_heap))
            if min_heap:
                heapq.heappop(min_heap)
            for val in temp:
                heapq.heappush(min_heap, val)
            
            # 如果最大堆的大小比最小堆大1以上，从最大堆移动一个元素到最小堆
            if len(max_heap) > len(min_heap) + 1:
                heapq.heappush(min_heap, -heapq.heappop(max_heap))
    
    def get_median(self, max_heap: List[int], min_heap: List[int], k: int) -> float:
        """
        获取当前中位数
        """
        if k % 2 == 1:
            # 奇数长度，中位数是最大堆的堆顶（取负数）
            return -max_heap[0]
        else:
            # 偶数长度，中位数是两个堆顶的平均值
            return (-max_heap[0] + min_heap[0]) / 2.0


class SolutionOptimized:
    """
    优化版本：使用延迟删除技术处理重复元素
    时间复杂度：O(n*log k)，空间复杂度：O(k)
    """
    
    def medianSlidingWindow(self, nums: List[int], k: int) -> List[float]:
        if not nums or k <= 0:
            return []
        
        n = len(nums)
        result = []
        
        # 使用延迟删除技术
        max_heap = []  # 存储负数，实现最大堆
        min_heap = []  # 正常的最小堆
        delayed = collections.Counter()  # 延迟删除的计数器
        
        # 平衡因子：max_heap的大小 - min_heap的大小
        balance = 0
        
        # 初始化第一个窗口
        for i in range(k):
            self.add_number(nums[i], max_heap, min_heap, delayed, balance)
        
        result.append(self.get_median(max_heap, min_heap, k, delayed))
        
        # 滑动窗口
        for i in range(k, n):
            # 移除窗口最左边的元素
            self.remove_number(nums[i - k], max_heap, min_heap, delayed, balance)
            # 添加新元素
            self.add_number(nums[i], max_heap, min_heap, delayed, balance)
            # 清理延迟删除的元素
            self.prune_heaps(max_heap, min_heap, delayed)
            # 计算当前窗口中位数
            result.append(self.get_median(max_heap, min_heap, k, delayed))
        
        return result
    
    def add_number(self, num: int, max_heap: List[int], min_heap: List[int], 
                  delayed: collections.Counter, balance: int) -> None:
        if not max_heap or num <= -max_heap[0]:
            heapq.heappush(max_heap, -num)
            balance += 1
        else:
            heapq.heappush(min_heap, num)
            balance -= 1
        
        # 重新平衡堆
        self.rebalance_heaps(max_heap, min_heap, delayed, balance)
    
    def remove_number(self, num: int, max_heap: List[int], min_heap: List[int], 
                     delayed: collections.Counter, balance: int) -> None:
        delayed[num] += 1
        
        if not max_heap or num <= -max_heap[0]:
            balance -= 1
        else:
            balance += 1
        
        # 重新平衡堆
        self.rebalance_heaps(max_heap, min_heap, delayed, balance)
    
    def rebalance_heaps(self, max_heap: List[int], min_heap: List[int], 
                       delayed: collections.Counter, balance: int) -> None:
        # 平衡堆的大小
        if balance > 1:
            heapq.heappush(min_heap, -heapq.heappop(max_heap))
            balance -= 2
        elif balance < -1:
            heapq.heappush(max_heap, -heapq.heappop(min_heap))
            balance += 2
    
    def prune_heaps(self, max_heap: List[int], min_heap: List[int], 
                   delayed: collections.Counter) -> None:
        # 清理最大堆顶部的延迟删除元素
        while max_heap and delayed.get(-max_heap[0], 0) > 0:
            num = -heapq.heappop(max_heap)
            delayed[num] -= 1
            if delayed[num] == 0:
                del delayed[num]
        
        # 清理最小堆顶部的延迟删除元素
        while min_heap and delayed.get(min_heap[0], 0) > 0:
            num = heapq.heappop(min_heap)
            delayed[num] -= 1
            if delayed[num] == 0:
                del delayed[num]
    
    def get_median(self, max_heap: List[int], min_heap: List[int], 
                  k: int, delayed: collections.Counter) -> float:
        self.prune_heaps(max_heap, min_heap, delayed)
        
        if k % 2 == 1:
            return -max_heap[0]
        else:
            return (-max_heap[0] + min_heap[0]) / 2.0


def test_median_sliding_window():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    result1 = solution.medianSlidingWindow(nums1, k1)
    print(f"输入数组: {nums1}")
    print(f"窗口大小: {k1}")
    print(f"中位数序列: {result1}")
    print("预期: [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]")
    print()
    
    # 测试用例2
    nums2 = [1, 2, 3, 4, 2, 3, 1, 4, 2]
    k2 = 3
    result2 = solution.medianSlidingWindow(nums2, k2)
    print(f"输入数组: {nums2}")
    print(f"窗口大小: {k2}")
    print(f"中位数序列: {result2}")
    print()
    
    # 测试用例3：边界情况，k=1
    nums3 = [5]
    k3 = 1
    result3 = solution.medianSlidingWindow(nums3, k3)
    print(f"输入数组: {nums3}")
    print(f"窗口大小: {k3}")
    print(f"中位数序列: {result3}")
    print("预期: [5.0]")
    print()
    
    # 测试用例4：k等于数组长度
    nums4 = [1, 2, 3, 4, 5]
    k4 = 5
    result4 = solution.medianSlidingWindow(nums4, k4)
    print(f"输入数组: {nums4}")
    print(f"窗口大小: {k4}")
    print(f"中位数序列: {result4}")
    print("预期: [3.0]")


if __name__ == "__main__":
    test_median_sliding_window()