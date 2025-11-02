import heapq

class MedianFinder:
    """
    相关题目24: LeetCode 295. 数据流的中位数
    题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
    题目描述: 设计一个支持以下两种操作的数据结构：
    - void addNum(int num) - 从数据流中添加一个整数到数据结构中
    - double findMedian() - 返回目前所有元素的中位数
    解题思路: 使用两个堆（最大堆和最小堆）维护数据流的中位数
    时间复杂度: addNum() O(log n)，findMedian() O(1)
    空间复杂度: O(n)
    是否最优解: 是，这种解法在时间和空间上都是最优的
    
    本题属于堆的应用场景：需要高效地找到动态数据流中的中位数
    """
    
    def __init__(self):
        """
        初始化数据结构
        使用最大堆存储较小的一半元素，最小堆存储较大的一半元素
        确保最大堆的大小等于或比最小堆大1
        """
        # 最大堆存储较小的一半元素（Python中heapq默认是最小堆，所以使用负数实现最大堆）
        self.max_heap = []  # 存储较小的一半元素
        # 最小堆存储较大的一半元素
        self.min_heap = []  # 存储较大的一半元素
    
    def addNum(self, num):
        """
        向数据结构中添加一个整数
        
        Args:
            num: 要添加的整数
        """
        # 首先将元素添加到最大堆中
        heapq.heappush(self.max_heap, -num)  # 存储为负数以实现最大堆
        
        # 确保最大堆顶元素（较小一半中的最大值）不大于最小堆顶元素（较大一半中的最小值）
        # 如果最大堆顶元素大于最小堆顶元素，则进行调整
        if self.min_heap and -self.max_heap[0] > self.min_heap[0]:
            # 将最大堆顶元素移动到最小堆
            max_val = -heapq.heappop(self.max_heap)
            heapq.heappush(self.min_heap, max_val)
        
        # 平衡两个堆的大小，确保最大堆的大小等于或比最小堆大1
        # 如果最大堆比最小堆大超过1，则移动一个元素到最小堆
        if len(self.max_heap) > len(self.min_heap) + 1:
            max_val = -heapq.heappop(self.max_heap)
            heapq.heappush(self.min_heap, max_val)
        # 如果最小堆比最大堆大，则移动一个元素到最大堆
        elif len(self.min_heap) > len(self.max_heap):
            min_val = heapq.heappop(self.min_heap)
            heapq.heappush(self.max_heap, -min_val)
    
    def findMedian(self):
        """
        返回目前所有元素的中位数
        
        Returns:
            float: 当前所有元素的中位数
            
        Raises:
            ValueError: 当没有元素时抛出异常
        """
        # 如果没有元素，抛出异常
        if not self.max_heap and not self.min_heap:
            raise ValueError("没有元素，无法计算中位数")
        
        # 如果最大堆的大小比最小堆大1，则中位数是最大堆的堆顶元素
        if len(self.max_heap) > len(self.min_heap):
            return -self.max_heap[0]
        # 否则，中位数是两个堆顶元素的平均值
        else:
            return (-self.max_heap[0] + self.min_heap[0]) / 2.0

class AlternativeApproach:
    """
    查找中位数的其他实现方式
    这个类提供了不同的实现方法，用于对比和教学目的
    """
    
    def __init__(self):
        """
        使用更简洁的方式实现两个堆的平衡
        """
        self.small = []  # 最大堆（存储较小的一半元素）
        self.large = []  # 最小堆（存储较大的一半元素）
    
    def addNum(self, num):
        """
        更简洁的添加元素实现
        
        Args:
            num: 要添加的整数
        """
        # 先添加到small堆，然后将small堆的最大值移到large堆
        heapq.heappush(self.small, -num)
        # 确保small堆的最大值不大于large堆的最小值
        if self.small and self.large and -self.small[0] > self.large[0]:
            val = -heapq.heappop(self.small)
            heapq.heappush(self.large, val)
        # 平衡两个堆的大小
        if len(self.small) > len(self.large) + 1:
            val = -heapq.heappop(self.small)
            heapq.heappush(self.large, val)
        if len(self.large) > len(self.small):
            val = heapq.heappop(self.large)
            heapq.heappush(self.small, -val)
    
    def findMedian(self):
        """
        返回中位数
        
        Returns:
            float: 当前所有元素的中位数
        """
        if len(self.small) > len(self.large):
            return -self.small[0]
        return (-self.small[0] + self.large[0]) / 2.0

# 测试函数，验证算法在不同输入情况下的正确性
def test_median_finder():
    print("=== 测试数据流的中位数算法 ===")
    
    # 测试基本实现
    print("\n=== 测试基本实现 ===")
    median_finder = MedianFinder()
    
    # 测试用例1：添加元素并计算中位数
    print("测试用例1：添加元素并计算中位数")
    operations = [
        ("addNum", 1), ("findMedian", None),
        ("addNum", 2), ("findMedian", None),
        ("addNum", 3), ("findMedian", None),
        ("addNum", 4), ("findMedian", None),
        ("addNum", 5), ("findMedian", None),
        ("addNum", 6), ("findMedian", None)
    ]
    
    expected_results = [1.0, 1.5, 2.0, 2.5, 3.0, 3.5]
    results = []
    
    for op, val in operations:
        if op == "addNum":
            median_finder.addNum(val)
        else:
            median = median_finder.findMedian()
            results.append(median)
            print(f"当前中位数: {median}")
    
    # 验证结果
    all_correct = True
    for i, (result, expected) in enumerate(zip(results, expected_results)):
        if abs(result - expected) > 1e-9:
            print(f"测试用例1 第{i+1}步失败: 期望{expected}, 实际{result}")
            all_correct = False
    
    if all_correct:
        print("测试用例1 全部通过 ✓")
    
    # 测试用例2：负数和零
    print("\n测试用例2：负数和零")
    median_finder2 = MedianFinder()
    median_finder2.addNum(-1)
    median_finder2.addNum(0)
    median_finder2.addNum(-2)
    
    result = median_finder2.findMedian()
    expected = -1.0
    print(f"当前中位数: {result}, 期望: {expected}, {'✓' if abs(result - expected) < 1e-9 else '✗'}")
    
    # 测试用例3：重复元素
    print("\n测试用例3：重复元素")
    median_finder3 = MedianFinder()
    for num in [2, 2, 2, 2, 2]:
        median_finder3.addNum(num)
    
    result = median_finder3.findMedian()
    expected = 2.0
    print(f"当前中位数: {result}, 期望: {expected}, {'✓' if abs(result - expected) < 1e-9 else '✗'}")
    
    # 测试异常情况
    print("\n=== 测试异常情况 ===")
    median_finder4 = MedianFinder()
    try:
        median_finder4.findMedian()
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    # 测试替代实现
    print("\n=== 测试替代实现 ===")
    alt_finder = AlternativeApproach()
    
    for num in [1, 2, 3, 4, 5]:
        alt_finder.addNum(num)
    
    result = alt_finder.findMedian()
    expected = 3.0
    print(f"替代实现中位数: {result}, 期望: {expected}, {'✓' if abs(result - expected) < 1e-9 else '✗'}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    import time
    
    # 测试大规模输入
    large_finder = MedianFinder()
    n = 100000
    
    start_time = time.time()
    for i in range(n):
        large_finder.addNum(i)
    median = large_finder.findMedian()
    end_time = time.time()
    
    print(f"添加{n}个元素后中位数: {median}")
    print(f"总耗时: {end_time - start_time:.6f}秒")
    print(f"平均每个操作耗时: {(end_time - start_time) / n * 1e6:.2f}微秒")

# 运行测试
if __name__ == "__main__":
    test_median_finder()

# 解题思路总结：
# 1. 双堆方法：
#    - 使用最大堆存储较小的一半元素，最小堆存储较大的一半元素
#    - 维护两个堆的大小关系，确保最大堆的大小等于或比最小堆大1
#    - 这样，如果元素总数是奇数，中位数就是最大堆的堆顶；如果是偶数，中位数是两个堆顶的平均值
#    - 时间复杂度：addNum() O(log n)，findMedian() O(1)
#    - 空间复杂度：O(n)
# 
# 2. 优化技巧：
#    - 在Python中，可以通过存储负数来模拟最大堆
#    - 注意添加元素后的平衡调整步骤，确保两个堆的大小关系和元素有序性
#    - 使用更简洁的实现方式可以减少代码行数，但核心逻辑保持不变
# 
# 3. 应用场景：
#    - 当需要频繁地从动态变化的数据集中获取中位数时，双堆方法是一个很好的选择
#    - 这种方法在金融数据分析、实时统计等场景中非常有用
# 
# 4. 边界情况处理：
#    - 空数据集时返回适当的错误
#    - 处理负数和零的情况
#    - 处理重复元素的情况
# 
# 5. 与其他方法的比较：
#    - 如果使用排序数组，addNum()操作将需要O(n)时间复杂度
#    - 如果使用二叉搜索树，可以实现O(log n)的addNum()操作，但实现更复杂
#    - 双堆方法在实现复杂度和性能之间取得了很好的平衡