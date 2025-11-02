import heapq
import random

class Solution:
    """
    相关题目26: LeetCode 215. 数组中的第K个最大元素
    题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
    题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
    请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
    解题思路1: 使用最小堆维护前k个最大元素
    解题思路2: 使用快速选择算法
    时间复杂度: 最小堆O(n log k)，快速选择平均O(n)，最坏O(n²)
    空间复杂度: 最小堆O(k)，快速选择O(1)（原地版本）
    是否最优解: 快速选择算法在平均情况下是最优解，但堆方法更为稳定
    
    本题属于堆的应用场景：Top K问题，特别是需要高效获取第k个最大元素
    """
    
    def findKthLargestHeap(self, nums, k):
        """
        使用最小堆实现查找数组中的第K个最大元素
        
        Args:
            nums: 整数数组
            k: 要查找的第k个最大元素的位置
            
        Returns:
            int: 数组中第k个最大的元素
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查nums和k是否有效
        if not nums or k <= 0 or k > len(nums):
            raise ValueError("输入参数无效")
        
        # 使用最小堆，保持堆的大小为k
        min_heap = []
        
        # 遍历数组中的每个元素
        for num in nums:
            # 如果堆的大小小于k，直接添加
            if len(min_heap) < k:
                heapq.heappush(min_heap, num)
            # 否则，如果当前元素大于堆顶元素，替换堆顶元素
            elif num > min_heap[0]:
                heapq.heappushpop(min_heap, num)
        
        # 堆顶元素就是第k个最大的元素
        return min_heap[0]
    
    def findKthLargestSort(self, nums, k):
        """
        使用排序实现查找数组中的第K个最大元素（简单方法作为对比）
        
        Args:
            nums: 整数数组
            k: 要查找的第k个最大元素的位置
            
        Returns:
            int: 数组中第k个最大的元素
        """
        # 异常处理
        if not nums or k <= 0 or k > len(nums):
            raise ValueError("输入参数无效")
        
        # 排序数组（降序）
        nums.sort(reverse=True)
        
        # 返回第k-1个索引的元素（因为Python是0索引）
        return nums[k-1]

class QuickSelectSolution:
    """
    使用快速选择算法实现查找第K个最大元素
    快速选择是一种基于快速排序的算法，在平均情况下可以达到O(n)的时间复杂度
    """
    
    def findKthLargest(self, nums, k):
        """
        使用快速选择算法查找数组中的第K个最大元素
        
        Args:
            nums: 整数数组
            k: 要查找的第k个最大元素的位置
            
        Returns:
            int: 数组中第k个最大元素
        """
        # 异常处理
        if not nums or k <= 0 or k > len(nums):
            raise ValueError("输入参数无效")
        
        # 我们需要找的是第k大的元素，转换为在0-indexed数组中查找第(len(nums)-k)小的元素
        target_index = len(nums) - k
        
        # 调用快速选择函数
        return self._quickSelect(nums, 0, len(nums) - 1, target_index)
    
    def _quickSelect(self, nums, left, right, target_index):
        """
        快速选择的核心实现
        
        Args:
            nums: 整数数组
            left: 当前子数组的左边界
            right: 当前子数组的右边界
            target_index: 目标索引（0-indexed的第target_index小的元素）
            
        Returns:
            int: 目标索引处的元素
        """
        # 如果区间只有一个元素，直接返回
        if left == right:
            return nums[left]
        
        # 分区并获取基准元素的索引
        pivot_index = self._partition(nums, left, right)
        
        # 根据基准元素的位置决定下一步搜索的区间
        if pivot_index == target_index:
            # 找到目标元素
            return nums[pivot_index]
        elif pivot_index < target_index:
            # 在右半部分继续搜索
            return self._quickSelect(nums, pivot_index + 1, right, target_index)
        else:
            # 在左半部分继续搜索
            return self._quickSelect(nums, left, pivot_index - 1, target_index)
    
    def _partition(self, nums, left, right):
        """
        分区函数：选择一个基准元素，将小于基准的元素放在左边，大于基准的元素放在右边
        
        Args:
            nums: 整数数组
            left: 子数组的左边界
            right: 子数组的右边界
            
        Returns:
            int: 基准元素的最终位置
        """
        # 随机选择一个元素作为基准，避免最坏情况
        pivot_idx = random.randint(left, right)
        # 将基准元素交换到末尾
        nums[pivot_idx], nums[right] = nums[right], nums[pivot_idx]
        
        # 基准元素的值
        pivot = nums[right]
        
        # i表示小于基准元素的区域的边界
        i = left
        
        # 遍历区间内的元素
        for j in range(left, right):
            # 如果当前元素小于基准元素，将其交换到小于区域
            if nums[j] <= pivot:
                nums[i], nums[j] = nums[j], nums[i]
                i += 1
        
        # 将基准元素交换到正确的位置
        nums[i], nums[right] = nums[right], nums[i]
        
        # 返回基准元素的索引
        return i

class OptimizedHeapSolution:
    """
    优化的堆实现，直接使用Python的heapq模块中的函数
    """
    
    def findKthLargest(self, nums, k):
        """
        使用heapq模块中的nlargest函数直接获取前k个最大元素
        
        Args:
            nums: 整数数组
            k: 要查找的第k个最大元素的位置
            
        Returns:
            int: 数组中第k个最大元素
        """
        # 异常处理
        if not nums or k <= 0 or k > len(nums):
            raise ValueError("输入参数无效")
        
        # 使用heapq.nlargest获取前k个最大元素，然后取最后一个
        # nlargest返回的是降序排列的列表，所以第k大元素是第k-1个索引处的元素
        return heapq.nlargest(k, nums)[-1]

# 测试函数，验证算法在不同输入情况下的正确性
def test_find_kth_largest():
    print("=== 测试数组中的第K个最大元素算法 ===")
    solution = Solution()
    quick_select_solution = QuickSelectSolution()
    optimized_solution = OptimizedHeapSolution()
    
    # 测试用例1：基本用例
    print("\n测试用例1：基本用例")
    nums1 = [3, 2, 1, 5, 6, 4]
    k1 = 2
    expected1 = 5
    
    result_heap1 = solution.findKthLargestHeap(nums1.copy(), k1)
    result_sort1 = solution.findKthLargestSort(nums1.copy(), k1)
    result_quick_select1 = quick_select_solution.findKthLargest(nums1.copy(), k1)
    result_optimized1 = optimized_solution.findKthLargest(nums1.copy(), k1)
    
    print(f"最小堆实现: {result_heap1}, 期望: {expected1}, {'✓' if result_heap1 == expected1 else '✗'}")
    print(f"排序实现: {result_sort1}, 期望: {expected1}, {'✓' if result_sort1 == expected1 else '✗'}")
    print(f"快速选择实现: {result_quick_select1}, 期望: {expected1}, {'✓' if result_quick_select1 == expected1 else '✗'}")
    print(f"优化堆实现: {result_optimized1}, 期望: {expected1}, {'✓' if result_optimized1 == expected1 else '✗'}")
    
    # 测试用例2：有重复元素
    print("\n测试用例2：有重复元素")
    nums2 = [3, 2, 3, 1, 2, 4, 5, 5, 6]
    k2 = 4
    expected2 = 4
    
    result_heap2 = solution.findKthLargestHeap(nums2.copy(), k2)
    result_quick_select2 = quick_select_solution.findKthLargest(nums2.copy(), k2)
    
    print(f"最小堆实现: {result_heap2}, 期望: {expected2}, {'✓' if result_heap2 == expected2 else '✗'}")
    print(f"快速选择实现: {result_quick_select2}, 期望: {expected2}, {'✓' if result_quick_select2 == expected2 else '✗'}")
    
    # 测试用例3：单元素数组
    print("\n测试用例3：单元素数组")
    nums3 = [1]
    k3 = 1
    expected3 = 1
    
    result_heap3 = solution.findKthLargestHeap(nums3.copy(), k3)
    result_quick_select3 = quick_select_solution.findKthLargest(nums3.copy(), k3)
    
    print(f"最小堆实现: {result_heap3}, 期望: {expected3}, {'✓' if result_heap3 == expected3 else '✗'}")
    print(f"快速选择实现: {result_quick_select3}, 期望: {expected3}, {'✓' if result_quick_select3 == expected3 else '✗'}")
    
    # 测试用例4：倒序数组
    print("\n测试用例4：倒序数组")
    nums4 = [6, 5, 4, 3, 2, 1]
    k4 = 3
    expected4 = 4
    
    result_heap4 = solution.findKthLargestHeap(nums4.copy(), k4)
    result_quick_select4 = quick_select_solution.findKthLargest(nums4.copy(), k4)
    
    print(f"最小堆实现: {result_heap4}, 期望: {expected4}, {'✓' if result_heap4 == expected4 else '✗'}")
    print(f"快速选择实现: {result_quick_select4}, 期望: {expected4}, {'✓' if result_quick_select4 == expected4 else '✗'}")
    
    # 测试异常情况
    print("\n=== 测试异常情况 ===")
    try:
        solution.findKthLargestHeap([], 1)
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    try:
        quick_select_solution.findKthLargest([1, 2, 3], 5)
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    import time
    
    # 测试大规模输入
    n = 1000000
    nums5 = [random.randint(0, 1000000) for _ in range(n)]
    k5 = 500000  # 查找第50万个最大元素
    
    # 最小堆实现
    start_time = time.time()
    result_heap = solution.findKthLargestHeap(nums5.copy(), k5)
    heap_time = time.time() - start_time
    print(f"最小堆实现结果: {result_heap}, 用时: {heap_time:.6f}秒")
    
    # 快速选择实现
    start_time = time.time()
    result_quick_select = quick_select_solution.findKthLargest(nums5.copy(), k5)
    quick_select_time = time.time() - start_time
    print(f"快速选择实现结果: {result_quick_select}, 用时: {quick_select_time:.6f}秒")
    
    # 优化堆实现
    start_time = time.time()
    result_optimized = optimized_solution.findKthLargest(nums5.copy(), k5)
    optimized_time = time.time() - start_time
    print(f"优化堆实现结果: {result_optimized}, 用时: {optimized_time:.6f}秒")
    
    # 排序实现（对于大数组可能较慢）
    if n <= 100000:  # 对于太大的数组，排序可能会很慢，所以只测试较小的数组
        start_time = time.time()
        result_sort = solution.findKthLargestSort(nums5.copy(), k5)
        sort_time = time.time() - start_time
        print(f"排序实现结果: {result_sort}, 用时: {sort_time:.6f}秒")
    else:
        print("排序实现：对于大规模数据，排序实现可能较慢，跳过测试")
    
    # 验证所有方法结果一致
    is_consistent = result_heap == result_quick_select == result_optimized
    print(f"\n结果一致性检查: {'✓' if is_consistent else '✗'}")
    
    # 性能比较
    print("\n性能比较:")
    print(f"最小堆 vs 快速选择: {'快速选择更快' if quick_select_time < heap_time else '最小堆更快'} 约 {(max(heap_time, quick_select_time) / min(heap_time, quick_select_time)):.2f}倍")
    print(f"最小堆 vs 优化堆: {'优化堆更快' if optimized_time < heap_time else '最小堆更快'} 约 {(max(heap_time, optimized_time) / min(heap_time, optimized_time)):.2f}倍")

# 运行测试
if __name__ == "__main__":
    test_find_kth_largest()

# 解题思路总结：
# 1. 最小堆方法：
#    - 维护一个大小为k的最小堆
#    - 遍历数组，保持堆中有k个最大的元素
#    - 堆顶元素即为第k个最大元素
#    - 时间复杂度：O(n log k)，其中n是数组长度，k是要找的第k大元素的位置
#    - 空间复杂度：O(k)
# 
# 2. 快速选择算法：
#    - 基于快速排序的思想，但只需要递归处理一半的区间
#    - 平均时间复杂度为O(n)，最坏情况为O(n²)（但通过随机选择基准元素可以避免最坏情况）
#    - 空间复杂度：O(log n)（递归调用栈的空间），原地版本可以达到O(1)
# 
# 3. 排序方法：
#    - 对数组进行排序，然后返回第k-1个索引的元素
#    - 时间复杂度：O(n log n)
#    - 空间复杂度：O(1)（原地排序）或O(n)（需要额外空间的排序）
# 
# 4. 优化技巧：
#    - 在Python中，可以直接使用heapq.nlargest函数来简化最小堆的实现
#    - 快速选择算法中使用随机选择基准元素可以避免最坏情况
#    - 对于非常大的k值（接近n），可以考虑找第(n-k+1)小的元素，可能更高效
# 
# 5. 应用场景：
#    - 当需要找到数组中第k个最大元素时
#    - 这种方法在数据分析、统计等领域有广泛应用
# 
# 6. 边界情况处理：
#    - 空数组
#    - k为0或大于数组长度
#    - 单元素数组
#    - 所有元素都相同的数组
#    - 已排序或接近排序的数组