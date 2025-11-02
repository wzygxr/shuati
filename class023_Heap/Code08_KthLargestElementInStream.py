import heapq

"""
相关题目5: LeetCode 703. 数据流的第K大元素
题目链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
题目描述: 设计一个找到数据流中第 k 大元素的类
解题思路: 使用大小为k的最小堆维护数据流中前k个最大元素
时间复杂度: 初始化: O(n log k), 添加元素: O(log k)
空间复杂度: O(k)
是否最优解: 是，这是处理动态第K大元素的经典解法
"""
class KthLargest:
    def __init__(self, k, nums):
        self.k = k
        # 使用最小堆维护前k个最大元素
        self.min_heap = []
        
        # 将初始数组中的元素加入堆中
        for num in nums:
            self.add(num)
    
    def add(self, val):
        if len(self.min_heap) < self.k:
            heapq.heappush(self.min_heap, val)
        elif val > self.min_heap[0]:
            heapq.heapreplace(self.min_heap, val)
        return self.min_heap[0]

# 测试方法
if __name__ == "__main__":
    k = 3
    nums = [4, 5, 8, 2]
    kthLargest = KthLargest(k, nums)
    
    print("添加3后第3大的元素:", kthLargest.add(3))   # 期望输出: 4
    print("添加5后第3大的元素:", kthLargest.add(5))   # 期望输出: 5
    print("添加10后第3大的元素:", kthLargest.add(10)) # 期望输出: 5
    print("添加9后第3大的元素:", kthLargest.add(9))   # 期望输出: 8
    print("添加4后第3大的元素:", kthLargest.add(4))   # 期望输出: 8