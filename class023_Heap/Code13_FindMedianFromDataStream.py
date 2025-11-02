import heapq

class MedianFinder:
    """
    相关题目5: LeetCode 295. 数据流的中位数
    题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
    题目描述: 设计一个支持以下两种操作的数据结构：
    1. void addNum(int num) - 从数据流中添加一个整数到数据结构中
    2. double findMedian() - 返回目前所有元素的中位数
    解题思路: 使用两个堆，一个最大堆保存较小的一半元素，一个最小堆保存较大的一半元素
    时间复杂度: addNum() - O(log n)，findMedian() - O(1)
    空间复杂度: O(n)，其中n是添加的元素数量
    是否最优解: 是，这是解决数据流中位数问题的最优解法
    
    本题属于堆的典型应用场景：需要在动态数据中快速获取中间值
    """
    
    def __init__(self):
        """
        初始化数据结构
        
        注意：Python的heapq模块只实现了最小堆，所以我们通过对元素取负数来模拟最大堆
        """
        # 最大堆，保存较小的一半元素（左半部分）
        # 使用负数来模拟最大堆
        self.max_heap = []
        # 最小堆，保存较大的一半元素（右半部分）
        self.min_heap = []
    
    def addNum(self, num):
        """
        从数据流中添加一个整数到数据结构中
        
        Args:
            num: 要添加的整数
        """
        # 策略：保持 len(max_heap) >= len(min_heap) 不超过1个元素
        
        # 1. 首先将num添加到合适的堆中
        # 如果num小于等于max_heap的最大值（注意这里我们存储的是负数），添加到max_heap
        # 否则添加到min_heap
        if not self.max_heap or num <= -self.max_heap[0]:
            heapq.heappush(self.max_heap, -num)  # 存储负数以模拟最大堆
        else:
            heapq.heappush(self.min_heap, num)
        
        # 调试信息：打印添加元素后的堆状态
        # print(f"添加元素 {num} 后:")
        # print(f"max_heap: {-x for x in self.max_heap}")
        # print(f"min_heap: {self.min_heap}")
        
        # 2. 重新平衡两个堆的大小，确保 len(max_heap) == len(min_heap) 或 len(max_heap) == len(min_heap) + 1
        # 如果max_heap的大小比min_heap大2或更多，将max_heap的堆顶元素移动到min_heap
        if len(self.max_heap) > len(self.min_heap) + 1:
            # 注意这里要取负数再存储到min_heap
            heapq.heappush(self.min_heap, -heapq.heappop(self.max_heap))
        # 如果min_heap的大小比max_heap大，将min_heap的堆顶元素移动到max_heap
        elif len(self.min_heap) > len(self.max_heap):
            # 注意这里要取负数再存储到max_heap
            heapq.heappush(self.max_heap, -heapq.heappop(self.min_heap))
    
    def findMedian(self):
        """
        返回目前所有元素的中位数
        
        Returns:
            float: 所有元素的中位数
        """
        # 如果总元素个数为奇数，中位数是max_heap的堆顶元素（注意要取负数）
        if len(self.max_heap) > len(self.min_heap):
            return -self.max_heap[0]
        # 如果总元素个数为偶数，中位数是两个堆顶元素的平均值
        else:
            return (-self.max_heap[0] + self.min_heap[0]) / 2.0

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    # 测试用例1：基本操作
    median_finder = MedianFinder()
    median_finder.addNum(1)
    result1 = median_finder.findMedian()
    print(f"添加1后，中位数: {result1}")
    assert abs(result1 - 1.0) < 1e-9, f"测试用例1失败，期望1.0，实际得到{result1}"
    
    median_finder.addNum(2)
    result2 = median_finder.findMedian()
    print(f"添加2后，中位数: {result2}")
    assert abs(result2 - 1.5) < 1e-9, f"测试用例2失败，期望1.5，实际得到{result2}"
    
    median_finder.addNum(3)
    result3 = median_finder.findMedian()
    print(f"添加3后，中位数: {result3}")
    assert abs(result3 - 2.0) < 1e-9, f"测试用例3失败，期望2.0，实际得到{result3}"
    
    # 测试用例2：逆序添加
    median_finder2 = MedianFinder()
    median_finder2.addNum(5)
    median_finder2.addNum(4)
    median_finder2.addNum(3)
    median_finder2.addNum(2)
    median_finder2.addNum(1)
    result4 = median_finder2.findMedian()
    print(f"逆序添加1-5后，中位数: {result4}")
    assert abs(result4 - 3.0) < 1e-9, f"测试用例4失败，期望3.0，实际得到{result4}"
    
    # 测试用例3：随机顺序添加
    median_finder3 = MedianFinder()
    median_finder3.addNum(3)
    median_finder3.addNum(1)
    median_finder3.addNum(5)
    median_finder3.addNum(2)
    result5 = median_finder3.findMedian()
    print(f"随机添加3,1,5,2后，中位数: {result5}")
    assert abs(result5 - 2.5) < 1e-9, f"测试用例5失败，期望2.5，实际得到{result5}"
    
    median_finder3.addNum(4)
    result6 = median_finder3.findMedian()
    print(f"再添加4后，中位数: {result6}")
    assert abs(result6 - 3.0) < 1e-9, f"测试用例6失败，期望3.0，实际得到{result6}"

# 运行测试
if __name__ == "__main__":
    test_solution()
    print("所有测试用例通过！")