import heapq

class MedianFinder:
    """
    相关题目12: LeetCode 295. 数据流的中位数
    题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
    题目描述: 设计一个支持以下两种操作的数据结构：
    1. void addNum(int num) - 从数据流中添加一个整数到数据结构中
    2. double findMedian() - 返回目前所有元素的中位数
    解题思路: 使用两个堆维护数据流：最大堆存储较小的一半元素，最小堆存储较大的一半元素
    时间复杂度: addNum() O(log n)，findMedian() O(1)
    空间复杂度: O(n)，其中n是数据流中的元素个数
    是否最优解: 是，这是求解数据流中位数的最优解法之一
    
    本题属于堆的典型应用场景：需要动态维护数据的中间值
    """
    
    def __init__(self):
        """
        初始化数据结构
        创建两个堆：最大堆（存储较小的一半元素）和最小堆（存储较大的一半元素）
        注意：Python的heapq默认是最小堆，所以我们通过存储负数来模拟最大堆
        """
        # 最大堆存储较小的一半元素
        # 由于Python的heapq是最小堆，我们通过存储负数来实现最大堆的效果
        self.max_heap = []  # 存储较小的一半元素，堆顶是最大值
        
        # 最小堆存储较大的一半元素
        self.min_heap = []  # 存储较大的一半元素，堆顶是最小值
    
    def addNum(self, num):
        """
        从数据流中添加一个整数到数据结构中
        
        Args:
            num: 要添加的整数
        """
        # 策略：保持两个堆的平衡，使max_heap的大小等于min_heap或比min_heap大1
        
        # 先将num加入到max_heap中（注意：存储的是负数）
        heapq.heappush(self.max_heap, -num)
        
        # 然后将max_heap的最大值（即堆顶的负数）转移到min_heap中
        # 确保max_heap中的所有元素都小于或等于min_heap中的所有元素
        # 取出max_heap的堆顶元素（即最大值的负数），取反后放入min_heap
        max_top = -heapq.heappop(self.max_heap)
        heapq.heappush(self.min_heap, max_top)
        
        # 如果min_heap的大小超过max_heap，则将min_heap的最小值转移到max_heap中
        # 这样可以保证max_heap的大小等于min_heap或比min_heap大1
        if len(self.min_heap) > len(self.max_heap):
            min_top = heapq.heappop(self.min_heap)
            heapq.heappush(self.max_heap, -min_top)
    
    def findMedian(self):
        """
        返回目前所有元素的中位数
        
        Returns:
            float: 中位数
            
        Raises:
            ValueError: 当没有元素时抛出异常
        """
        # 异常处理：当没有元素时抛出异常
        if not self.max_heap:
            raise ValueError("没有元素，无法计算中位数")
        
        # 如果max_heap的大小大于min_heap，说明总共有奇数个元素，中位数就是max_heap的堆顶（取反后的值）
        if len(self.max_heap) > len(self.min_heap):
            return -self.max_heap[0]
        else:
            # 如果max_heap和min_heap的大小相等，说明总共有偶数个元素，中位数是两个堆顶的平均值
            return (-self.max_heap[0] + self.min_heap[0]) / 2.0
    
    def size(self):
        """
        获取当前存储的元素数量
        
        Returns:
            int: 元素数量
        """
        return len(self.max_heap) + len(self.min_heap)

class AlternativeMedianFinder:
    """
    数据流中位数的另一种实现方式
    使用排序数组实现，但时间复杂度不如堆实现高效
    这个实现主要用于对比和教学目的
    """
    
    def __init__(self):
        """
        初始化数据结构，使用一个列表存储元素
        """
        self.nums = []
    
    def addNum(self, num):
        """
        从数据流中添加一个整数到数据结构中
        使用二分查找找到插入位置，以保持数组有序
        
        Args:
            num: 要添加的整数
        """
        # 二分查找找到插入位置
        left, right = 0, len(self.nums)
        while left < right:
            mid = left + (right - left) // 2
            if self.nums[mid] < num:
                left = mid + 1
            else:
                right = mid
        
        # 在正确的位置插入元素
        self.nums.insert(left, num)
    
    def findMedian(self):
        """
        返回目前所有元素的中位数
        
        Returns:
            float: 中位数
            
        Raises:
            ValueError: 当没有元素时抛出异常
        """
        # 异常处理：当没有元素时抛出异常
        if not self.nums:
            raise ValueError("没有元素，无法计算中位数")
        
        n = len(self.nums)
        if n % 2 == 1:
            # 奇数个元素，中位数是中间的那个元素
            return self.nums[n // 2]
        else:
            # 偶数个元素，中位数是中间两个元素的平均值
            return (self.nums[n // 2 - 1] + self.nums[n // 2]) / 2.0

# 测试函数，验证算法在不同输入情况下的正确性
def test_median_finder():
    print("=== 测试堆实现的中位数查找器 ===")
    
    # 测试用例1：基本操作
    print("\n测试用例1：基本操作")
    median_finder1 = MedianFinder()
    
    # 添加元素并打印中位数
    median_finder1.addNum(1)
    print(f"添加1后，中位数 = {median_finder1.findMedian()}")  # 期望输出: 1.0
    
    median_finder1.addNum(2)
    print(f"添加2后，中位数 = {median_finder1.findMedian()}")  # 期望输出: 1.5
    
    median_finder1.addNum(3)
    print(f"添加3后，中位数 = {median_finder1.findMedian()}")  # 期望输出: 2.0
    
    median_finder1.addNum(4)
    print(f"添加4后，中位数 = {median_finder1.findMedian()}")  # 期望输出: 2.5
    
    median_finder1.addNum(5)
    print(f"添加5后，中位数 = {median_finder1.findMedian()}")  # 期望输出: 3.0
    
    # 测试用例2：无序输入
    print("\n测试用例2：无序输入")
    median_finder2 = MedianFinder()
    nums = [5, 2, 8, 4, 1, 9, 3, 6, 7]
    
    for num in nums:
        median_finder2.addNum(num)
        print(f"添加{num}后，中位数 = {median_finder2.findMedian()}")
    
    # 测试用例3：负数和零
    print("\n测试用例3：负数和零")
    median_finder3 = MedianFinder()
    nums_with_negatives = [-1, 0, 5, -10, 2, 7]
    
    for num in nums_with_negatives:
        median_finder3.addNum(num)
        print(f"添加{num}后，中位数 = {median_finder3.findMedian()}")
    
    # 测试异常情况
    print("\n测试异常情况")
    empty_median_finder = MedianFinder()
    try:
        empty_median_finder.findMedian()
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    # 测试替代实现（用于对比）
    print("\n=== 测试替代实现的中位数查找器 ===")
    alt_median_finder = AlternativeMedianFinder()
    for num in [1, 2, 3, 4, 5]:
        alt_median_finder.addNum(num)
        print(f"添加{num}后，中位数 = {alt_median_finder.findMedian()}")

# 运行测试
if __name__ == "__main__":
    test_median_finder()