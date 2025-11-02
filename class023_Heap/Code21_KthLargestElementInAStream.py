import heapq

class KthLargest:
    """
    相关题目13: LeetCode 703. 数据流中的第 K 大元素
    题目链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
    题目描述: 设计一个找到数据流中第 k 大元素的类（class）。注意是排序后的第 k 大元素，不是第 k 个不同的元素。
    实现 KthLargest 类:
    KthLargest(int k, int[] nums) 使用整数 k 和整数流 nums 初始化对象
    int add(int val) 将 val 插入数据流 nums 后，返回当前数据流中第 k 大的元素
    解题思路: 使用最小堆维护数据流中最大的k个元素，堆顶即为第k大元素
    时间复杂度: add() O(log k)，初始化 O(n log k)
    空间复杂度: O(k)，堆中最多存储k个元素
    是否最优解: 是，这是求解数据流中第K大元素的最优解法之一
    
    本题属于堆的典型应用场景：需要在动态数据流中维护前K个最大值
    """
    
    def __init__(self, k, nums):
        """
        初始化KthLargest类
        
        Args:
            k: 第K大元素的K值
            nums: 初始数据流数组
            
        Raises:
            ValueError: 当k或nums参数无效时抛出异常
        """
        # 异常处理：检查k是否有效
        if k <= 0:
            raise ValueError("k的值必须大于0")
        
        # 异常处理：检查nums是否为None
        if nums is None:
            raise ValueError("输入数组不能为None")
        
        self.k = k
        # 创建最小堆，Python的heapq默认就是最小堆
        self.min_heap = []
        
        # 将初始数组中的元素添加到堆中
        for num in nums:
            self.add(num)
    
    def add(self, val):
        """
        将val插入数据流nums后，返回当前数据流中第k大的元素
        
        Args:
            val: 要插入的整数
            
        Returns:
            int: 当前数据流中第k大的元素
        """
        # 如果堆的大小小于k，直接添加元素到堆中
        if len(self.min_heap) < self.k:
            heapq.heappush(self.min_heap, val)
        else:
            # 如果当前元素大于堆顶元素（第k大元素），则移除堆顶元素，添加当前元素
            if val > self.min_heap[0]:
                heapq.heappushpop(self.min_heap, val)
        # 否则，不做任何操作，因为当前元素小于第k大元素，不会影响结果
        
        # 如果堆中不足k个元素，返回None表示没有第k大元素
        # 但根据题目描述，初始化时nums可能为空，所以这种情况是允许的
        return self.min_heap[0] if self.min_heap else None
    
    def getHeapSize(self):
        """
        获取当前堆的大小
        
        Returns:
            int: 堆的大小
        """
        return len(self.min_heap)

class AlternativeKthLargest:
    """
    数据流中第K大元素的另一种实现方式
    使用列表存储所有元素并排序，但时间复杂度不如堆实现高效
    这个实现主要用于对比和教学目的
    """
    
    def __init__(self, k, nums):
        """
        初始化AlternativeKthLargest类
        
        Args:
            k: 第K大元素的K值
            nums: 初始数据流数组
        """
        if k <= 0:
            raise ValueError("k的值必须大于0")
        
        self.k = k
        self.nums = nums.copy()
        # 对初始数组进行排序
        self.nums.sort()
    
    def add(self, val):
        """
        将val插入数据流后，返回当前数据流中第k大的元素
        
        Args:
            val: 要插入的整数
            
        Returns:
            int: 当前数据流中第k大的元素
        """
        # 二分查找找到插入位置
        left, right = 0, len(self.nums)
        while left < right:
            mid = left + (right - left) // 2
            if self.nums[mid] < val:
                left = mid + 1
            else:
                right = mid
        
        # 在正确的位置插入元素
        self.nums.insert(left, val)
        
        # 如果元素数量少于k，返回None
        if len(self.nums) < self.k:
            return None
        
        # 返回第k大的元素（注意是从后往前数的第k个元素）
        return self.nums[-self.k]

# 测试函数，验证算法在不同输入情况下的正确性
def test_kth_largest():
    print("=== 测试堆实现的第K大元素查找器 ===")
    
    # 测试用例1：基本操作
    print("\n测试用例1：基本操作")
    k1 = 3
    nums1 = [4, 5, 8, 2]
    kth_largest1 = KthLargest(k1, nums1)
    
    print(f"添加3后，第3大的元素 = {kth_largest1.add(3)}")   # 期望输出: 4
    print(f"添加5后，第3大的元素 = {kth_largest1.add(5)}")   # 期望输出: 5
    print(f"添加10后，第3大的元素 = {kth_largest1.add(10)}") # 期望输出: 5
    print(f"添加9后，第3大的元素 = {kth_largest1.add(9)}")   # 期望输出: 8
    print(f"添加4后，第3大的元素 = {kth_largest1.add(4)}")   # 期望输出: 8
    
    # 测试用例2：初始数组为空
    print("\n测试用例2：初始数组为空")
    k2 = 1
    nums2 = []
    kth_largest2 = KthLargest(k2, nums2)
    
    print(f"添加-3后，第1大的元素 = {kth_largest2.add(-3)}") # 期望输出: -3
    print(f"添加-2后，第1大的元素 = {kth_largest2.add(-2)}") # 期望输出: -2
    print(f"添加-4后，第1大的元素 = {kth_largest2.add(-4)}") # 期望输出: -2
    print(f"添加0后，第1大的元素 = {kth_largest2.add(0)}")   # 期望输出: 0
    print(f"添加4后，第1大的元素 = {kth_largest2.add(4)}")   # 期望输出: 4
    
    # 测试用例3：初始数组长度大于k
    print("\n测试用例3：初始数组长度大于k")
    k3 = 2
    nums3 = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
    kth_largest3 = KthLargest(k3, nums3)
    
    print(f"初始第2大的元素 = {kth_largest3.add(-1)}") # 期望输出: 8
    print(f"添加10后，第2大的元素 = {kth_largest3.add(10)}") # 期望输出: 9
    
    # 测试异常情况
    print("\n测试异常情况")
    try:
        invalid_k = KthLargest(0, [1, 2, 3])
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    # 测试替代实现（用于对比）
    print("\n=== 测试替代实现的第K大元素查找器 ===")
    alt_kth_largest = AlternativeKthLargest(3, [4, 5, 8, 2])
    print(f"添加3后，第3大的元素 = {alt_kth_largest.add(3)}")  # 期望输出: 4
    print(f"添加5后，第3大的元素 = {alt_kth_largest.add(5)}")  # 期望输出: 5
    print(f"添加10后，第3大的元素 = {alt_kth_largest.add(10)}") # 期望输出: 5

# 运行测试
if __name__ == "__main__":
    test_kth_largest()