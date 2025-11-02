import heapq

"""
相关题目3: LeetCode 295. 数据流的中位数
题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
题目描述: 中位数是有序整数列表中的中间值。如果列表大小是偶数，则没有中间值，中位数是两个中间值的平均值。
解题思路: 使用两个堆，一个最大堆维护较小的一半，一个最小堆维护较大的一半
时间复杂度: addNum: O(log n), findMedian: O(1)
空间复杂度: O(n)
是否最优解: 是，这是处理动态中位数的经典解法
"""
class MedianFinder:
    def __init__(self):
        # 最小堆，存储较大的一半元素
        self.min_heap = []
        # 最大堆，存储较小的一半元素（通过存储负值实现）
        self.max_heap = []
    
    def addNum(self, num: int) -> None:
        # 保证max_heap的元素数量不少于min_heap
        if not self.max_heap or num <= -self.max_heap[0]:
            heapq.heappush(self.max_heap, -num)
        else:
            heapq.heappush(self.min_heap, num)
        
        # 平衡两个堆的大小
        if len(self.max_heap) > len(self.min_heap) + 1:
            heapq.heappush(self.min_heap, -heapq.heappop(self.max_heap))
        elif len(self.min_heap) > len(self.max_heap):
            heapq.heappush(self.max_heap, -heapq.heappop(self.min_heap))
    
    def findMedian(self) -> float:
        if len(self.max_heap) == len(self.min_heap):
            # 偶数个元素，返回两堆顶的平均值
            return (-self.max_heap[0] + self.min_heap[0]) / 2.0
        else:
            # 奇数个元素，返回max_heap的堆顶
            return -self.max_heap[0]

# 测试方法
if __name__ == "__main__":
    medianFinder = MedianFinder()
    
    medianFinder.addNum(1)
    medianFinder.addNum(2)
    print("添加1,2后中位数:", medianFinder.findMedian())  # 期望输出: 1.5
    
    medianFinder.addNum(3)
    print("添加3后中位数:", medianFinder.findMedian())  # 期望输出: 2.0