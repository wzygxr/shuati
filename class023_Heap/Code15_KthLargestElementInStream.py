import heapq

class KthLargest:
    """
    相关题目7: LeetCode 703. 数据流中的第K大元素
    题目链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
    题目描述: 设计一个找到数据流中第K大元素的类。注意是排序后的第K大元素，不是第K个不同的元素。
    实现 KthLargest 类:
    1. KthLargest(int k, int[] nums) 使用整数 k 和整数流 nums 初始化对象
    2. int add(int val) 将 val 插入数据流 nums 后，返回当前数据流中第K大的元素
    解题思路: 使用最小堆维护当前最大的k个元素，堆顶就是第k大的元素
    时间复杂度: add() - O(log k)，初始化 - O(n log k)
    空间复杂度: O(k)，堆最多存储k个元素
    是否最优解: 是，这是解决数据流中第K大元素问题的最优解法
    
    本题属于堆的典型应用场景：需要在动态数据中快速获取第K大元素
    """
    
    def __init__(self, k, nums):
        """
        使用整数k和整数流nums初始化对象
        
        Args:
            k: 需要找的第k大元素
            nums: 初始整数流
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查k是否为正整数
        if k <= 0:
            raise ValueError("k必须是正整数")
        
        # 异常处理：检查nums是否为None
        if nums is None:
            raise ValueError("输入数组不能为None")
        
        self.k = k
        # 创建最小堆
        self.min_heap = []
        
        # 将初始数组中的元素添加到堆中
        for num in nums:
            self.add(num)
    
    def add(self, val):
        """
        将val插入数据流nums后，返回当前数据流中第K大的元素
        
        Args:
            val: 需要添加的新值
            
        Returns:
            int: 当前数据流中第K大的元素
        """
        # 调试信息：打印当前堆的状态和要添加的值
        # print(f"添加值: {val}, 当前堆: {self.min_heap}")
        
        if len(self.min_heap) < self.k:
            # 如果堆的大小小于k，直接将元素加入堆
            heapq.heappush(self.min_heap, val)
        elif val > self.min_heap[0]:
            # 如果当前值大于堆顶元素（堆中最小的元素）
            # 则移除堆顶元素，加入新值
            heapq.heappop(self.min_heap)
            heapq.heappush(self.min_heap, val)
        # 否则，不做任何操作，因为这个值不影响第k大元素的结果
        
        # 堆顶就是第k大的元素
        return self.min_heap[0]

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    # 测试用例1：基本情况
    try:
        k1 = 3
        nums1 = [4, 5, 8, 2]
        kth_largest1 = KthLargest(k1, nums1)
        print("测试用例1:")
        result1_1 = kth_largest1.add(3)
        print(f"添加3后，第3大元素: {result1_1}")
        assert result1_1 == 4, f"测试用例1-1失败，期望4，实际得到{result1_1}"
        
        result1_2 = kth_largest1.add(5)
        print(f"添加5后，第3大元素: {result1_2}")
        assert result1_2 == 5, f"测试用例1-2失败，期望5，实际得到{result1_2}"
        
        result1_3 = kth_largest1.add(10)
        print(f"添加10后，第3大元素: {result1_3}")
        assert result1_3 == 5, f"测试用例1-3失败，期望5，实际得到{result1_3}"
        
        result1_4 = kth_largest1.add(9)
        print(f"添加9后，第3大元素: {result1_4}")
        assert result1_4 == 8, f"测试用例1-4失败，期望8，实际得到{result1_4}"
        
        result1_5 = kth_largest1.add(4)
        print(f"添加4后，第3大元素: {result1_5}")
        assert result1_5 == 8, f"测试用例1-5失败，期望8，实际得到{result1_5}"
        
        # 测试用例2：初始数组为空
        k2 = 1
        nums2 = []
        kth_largest2 = KthLargest(k2, nums2)
        print("\n测试用例2:")
        result2_1 = kth_largest2.add(-3)
        print(f"空数组，添加-3后，第1大元素: {result2_1}")
        assert result2_1 == -3, f"测试用例2-1失败，期望-3，实际得到{result2_1}"
        
        result2_2 = kth_largest2.add(-2)
        print(f"添加-2后，第1大元素: {result2_2}")
        assert result2_2 == -2, f"测试用例2-2失败，期望-2，实际得到{result2_2}"
        
        result2_3 = kth_largest2.add(-4)
        print(f"添加-4后，第1大元素: {result2_3}")
        assert result2_3 == -2, f"测试用例2-3失败，期望-2，实际得到{result2_3}"
        
        result2_4 = kth_largest2.add(0)
        print(f"添加0后，第1大元素: {result2_4}")
        assert result2_4 == 0, f"测试用例2-4失败，期望0，实际得到{result2_4}"
        
        result2_5 = kth_largest2.add(4)
        print(f"添加4后，第1大元素: {result2_5}")
        assert result2_5 == 4, f"测试用例2-5失败，期望4，实际得到{result2_5}"
        
        # 测试用例3：边界情况 - 数组元素个数等于k
        k3 = 2
        nums3 = [1, 2]
        kth_largest3 = KthLargest(k3, nums3)
        print("\n测试用例3:")
        result3 = kth_largest3.add(0)
        print(f"数组元素个数等于k，添加0后，第2大元素: {result3}")
        assert result3 == 1, f"测试用例3失败，期望1，实际得到{result3}"
        
        # 测试异常情况
        print("\n测试异常情况:")
        try:
            kth_largest4 = KthLargest(0, [1, 2, 3])  # k=0是无效的
            print("异常测试失败：未抛出预期的异常")
        except ValueError as e:
            print(f"异常测试通过: {e}")
            
        try:
            kth_largest5 = KthLargest(2, None)  # nums=None是无效的
            print("异常测试失败：未抛出预期的异常")
        except ValueError as e:
            print(f"异常测试通过: {e}")
            
        print("\n所有测试用例通过！")
        
    except Exception as e:
        print(f"测试过程中发生异常: {e}")

# 运行测试
if __name__ == "__main__":
    test_solution()