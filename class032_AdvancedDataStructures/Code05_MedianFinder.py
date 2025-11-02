import heapq

# 快速获得数据流的中位数的结构
'''
一、题目解析
设计一个支持以下两种操作的数据结构：
1. void addNum(int num) - 从数据流中添加一个整数到数据结构中
2. double findMedian() - 返回目前所有元素的中位数

中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。

二、算法思路
使用两个堆来维护数据：
1. max_heap（最大堆）：存储较小的一半元素，使用负数模拟最大堆
2. min_heap（最小堆）：存储较大的一半元素

保持两个堆的大小平衡：
1. 当元素总数为偶数时，两个堆大小相等
2. 当元素总数为奇数时，max_heap比min_heap多一个元素

三、时间复杂度分析
addNum操作: O(log n) - 堆的插入和调整操作
findMedian操作: O(1) - 直接访问堆顶元素

四、空间复杂度分析
O(n) - n为添加的元素个数，需要两个堆分别存储元素

五、工程化考量
1. 异常处理: 处理空数据流的findMedian操作
2. 边界场景: 空数据流、单元素数据流等
3. 数值精度: 注意整数除法的精度问题

六、相关题目扩展
1. LeetCode 295. 数据流的中位数 (本题)
2. LeetCode 480. 滑动窗口中位数
3. 牛客网: 数据流中的中位数
4. 剑指Offer 41. 数据流中的中位数
'''

class MedianFinder:
    def __init__(self):
        """构造函数"""
        # Python的heapq是最小堆，使用负数来模拟最大堆
        self.max_heap = []  # 存储较小的一半元素（使用负数）
        self.min_heap = []  # 存储较大的一半元素

    def addNum(self, num: int) -> None:
        """
        添加数字到数据结构中
        :param num: 要添加的数字
        时间复杂度: O(log n)
        """
        # 如果最大堆为空或新数字小于等于最大堆堆顶，则添加到最大堆
        if not self.max_heap or -self.max_heap[0] >= num:
            heapq.heappush(self.max_heap, -num)
        else:
            # 否则添加到最小堆
            heapq.heappush(self.min_heap, num)
        # 平衡两个堆的大小
        self._balance()

    def findMedian(self) -> float:
        """
        查找当前所有元素的中位数
        :return: 中位数
        时间复杂度: O(1)
        """
        # 检查数据流是否为空
        if not self.max_heap and not self.min_heap:
            raise Exception("数据流为空，无法获取中位数")
        # 如果两个堆大小相等，返回两个堆堆顶的平均值
        if len(self.max_heap) == len(self.min_heap):
            return (-self.max_heap[0] + self.min_heap[0]) / 2
        else:
            # 否则返回较大堆的堆顶元素
            return -self.max_heap[0] if len(self.max_heap) > len(self.min_heap) else self.min_heap[0]

    def _balance(self) -> None:
        """
        平衡两个堆的大小
        确保两个堆的大小差不超过1
        """
        # 如果最大堆比最小堆多超过1个元素，则移动一个元素到最小堆
        if len(self.max_heap) > len(self.min_heap) + 1:
            heapq.heappush(self.min_heap, -heapq.heappop(self.max_heap))
        # 如果最小堆比最大堆多超过1个元素，则移动一个元素到最大堆
        elif len(self.min_heap) > len(self.max_heap) + 1:
            heapq.heappush(self.max_heap, -heapq.heappop(self.min_heap))

# 测试代码
if __name__ == "__main__":
    medianFinder = MedianFinder()
    
    # 测试用例: ["MedianFinder","addNum","addNum","findMedian","addNum","findMedian"]
    #           [[],[1],[2],[],[3],[]]
    
    medianFinder.addNum(1)    # arr = [1]
    medianFinder.addNum(2)    # arr = [1, 2]
    print("中位数:", medianFinder.findMedian())  # 返回 1.5 ((1 + 2) / 2)
    medianFinder.addNum(3)    # arr[1, 2, 3]
    print("中位数:", medianFinder.findMedian())  # 返回 2.0