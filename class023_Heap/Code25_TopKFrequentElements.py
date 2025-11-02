import heapq
from collections import Counter

class Solution:
    """
    相关题目25: LeetCode 347. 前 K 个高频元素
    题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
    题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
    解题思路1: 使用最小堆维护前k个高频元素
    解题思路2: 使用桶排序，按照频率分组
    时间复杂度: 最小堆O(n log k)，桶排序O(n)
    空间复杂度: 最小堆O(n)，桶排序O(n)
    是否最优解: 桶排序是最优解，时间复杂度为O(n)
    
    本题属于堆的应用场景：需要高效地获取一组元素中的Top K问题
    """
    
    def topKFrequentHeap(self, nums, k):
        """
        使用最小堆实现前K个高频元素
        
        Args:
            nums: 整数数组
            k: 返回前k个高频元素
            
        Returns:
            list: 前k个高频元素的列表
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查nums和k是否有效
        if not nums or k <= 0 or k > len(set(nums)):
            raise ValueError("输入参数无效")
        
        # 统计每个元素出现的频率
        count = Counter(nums)
        
        # 使用最小堆，存储频率和元素
        # Python的heapq是最小堆，我们要保留最大的k个频率，所以存储负频率
        min_heap = []
        
        # 遍历所有元素及其频率
        for num, freq in count.items():
            # 如果堆的大小小于k，直接添加
            if len(min_heap) < k:
                heapq.heappush(min_heap, (freq, num))
            # 否则，如果当前元素的频率大于堆顶元素的频率，替换堆顶元素
            elif freq > min_heap[0][0]:
                heapq.heappushpop(min_heap, (freq, num))
        
        # 从堆中提取元素（不关心频率，只需要元素本身）
        return [num for freq, num in min_heap]
    
    def topKFrequentBucket(self, nums, k):
        """
        使用桶排序实现前K个高频元素
        
        Args:
            nums: 整数数组
            k: 返回前k个高频元素
            
        Returns:
            list: 前k个高频元素的列表
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查nums和k是否有效
        if not nums or k <= 0 or k > len(set(nums)):
            raise ValueError("输入参数无效")
        
        # 统计每个元素出现的频率
        count = Counter(nums)
        
        # 创建桶，索引表示频率，值是该频率的元素列表
        # 最大可能的频率是数组长度
        bucket = [[] for _ in range(len(nums) + 1)]
        
        # 将元素放入对应的桶中
        for num, freq in count.items():
            bucket[freq].append(num)
        
        # 从后向前遍历桶，收集前k个高频元素
        result = []
        for i in range(len(bucket) - 1, 0, -1):
            # 将当前频率的所有元素添加到结果中
            result.extend(bucket[i])
            # 如果已经收集了k个元素，返回结果
            if len(result) >= k:
                return result[:k]
        
        # 如果没有收集到k个元素（不应该发生，因为k <= 不同元素的数量）
        return result[:k]

class AlternativeApproach:
    """
    前K个高频元素的其他实现方式
    这个类提供了不同的实现方法，用于对比和教学目的
    """
    
    def topKFrequentSort(self, nums, k):
        """
        使用排序实现前K个高频元素
        
        Args:
            nums: 整数数组
            k: 返回前k个高频元素
            
        Returns:
            list: 前k个高频元素的列表
        """
        # 异常处理
        if not nums or k <= 0 or k > len(set(nums)):
            raise ValueError("输入参数无效")
        
        # 统计频率
        count = Counter(nums)
        
        # 按照频率排序（降序），并取前k个元素
        # sorted返回一个列表，其中每个元素是一个元组(频率, 元素)
        sorted_items = sorted(count.items(), key=lambda x: x[1], reverse=True)
        
        # 提取前k个元素
        return [item[0] for item in sorted_items[:k]]

# 测试函数，验证算法在不同输入情况下的正确性
def test_top_k_frequent_elements():
    print("=== 测试前K个高频元素算法 ===")
    solution = Solution()
    alternative = AlternativeApproach()
    
    # 测试用例1：基本用例
    print("\n测试用例1：基本用例")
    nums1 = [1, 1, 1, 2, 2, 3]
    k1 = 2
    expected1 = [1, 2]  # 顺序可能不同
    
    result_heap1 = solution.topKFrequentHeap(nums1, k1)
    result_bucket1 = solution.topKFrequentBucket(nums1, k1)
    result_sort1 = alternative.topKFrequentSort(nums1, k1)
    
    print(f"最小堆实现: {result_heap1}")
    print(f"桶排序实现: {result_bucket1}")
    print(f"排序实现: {result_sort1}")
    
    # 验证结果（不考虑顺序）
    is_heap_correct = set(result_heap1) == set(expected1)
    is_bucket_correct = set(result_bucket1) == set(expected1)
    is_sort_correct = set(result_sort1) == set(expected1)
    
    print(f"最小堆实现结果 {'✓' if is_heap_correct else '✗'}")
    print(f"桶排序实现结果 {'✓' if is_bucket_correct else '✗'}")
    print(f"排序实现结果 {'✓' if is_sort_correct else '✗'}")
    
    # 测试用例2：所有元素出现频率相同
    print("\n测试用例2：所有元素出现频率相同")
    nums2 = [1, 2, 3, 4, 5]
    k2 = 3
    expected2 = [1, 2, 3]  # 所有元素频率都是1，任意3个都可以
    
    result_heap2 = solution.topKFrequentHeap(nums2, k2)
    result_bucket2 = solution.topKFrequentBucket(nums2, k2)
    
    print(f"最小堆实现: {result_heap2}")
    print(f"桶排序实现: {result_bucket2}")
    print(f"结果长度正确: {'✓' if len(result_heap2) == k2 else '✗'}")
    
    # 测试用例3：单个元素
    print("\n测试用例3：单个元素")
    nums3 = [1]
    k3 = 1
    expected3 = [1]
    
    result_heap3 = solution.topKFrequentHeap(nums3, k3)
    result_bucket3 = solution.topKFrequentBucket(nums3, k3)
    
    print(f"最小堆实现: {result_heap3}, 期望: {expected3}, {'✓' if result_heap3 == expected3 else '✗'}")
    print(f"桶排序实现: {result_bucket3}, 期望: {expected3}, {'✓' if result_bucket3 == expected3 else '✗'}")
    
    # 测试异常情况
    print("\n=== 测试异常情况 ===")
    try:
        solution.topKFrequentHeap([], 2)
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    try:
        solution.topKFrequentBucket([1, 2, 3], 5)
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    import time
    
    # 测试大规模输入
    nums4 = [i % 1000 for i in range(100000)]  # 生成大规模数组，每个数字出现约100次
    k4 = 10
    
    start_time = time.time()
    result_heap = solution.topKFrequentHeap(nums4, k4)
    heap_time = time.time() - start_time
    print(f"最小堆实现结果: {result_heap}, 用时: {heap_time:.6f}秒")
    
    start_time = time.time()
    result_bucket = solution.topKFrequentBucket(nums4, k4)
    bucket_time = time.time() - start_time
    print(f"桶排序实现结果: {result_bucket}, 用时: {bucket_time:.6f}秒")
    
    start_time = time.time()
    result_sort = alternative.topKFrequentSort(nums4, k4)
    sort_time = time.time() - start_time
    print(f"排序实现结果: {result_sort}, 用时: {sort_time:.6f}秒")
    
    print(f"\n性能比较:")
    print(f"最小堆 vs 桶排序: {'桶排序更快' if bucket_time < heap_time else '最小堆更快'} 约 {(max(heap_time, bucket_time) / min(heap_time, bucket_time)):.2f}倍")
    print(f"最小堆 vs 排序: {'排序更快' if sort_time < heap_time else '最小堆更快'} 约 {(max(heap_time, sort_time) / min(heap_time, sort_time)):.2f}倍")
    print(f"桶排序 vs 排序: {'排序更快' if sort_time < bucket_time else '桶排序更快'} 约 {(max(bucket_time, sort_time) / min(bucket_time, sort_time)):.2f}倍")

# 运行测试
if __name__ == "__main__":
    test_top_k_frequent_elements()

# 解题思路总结：
# 1. 最小堆方法：
#    - 统计每个元素的频率
#    - 使用最小堆维护k个最高频率的元素
#    - 遍历所有元素，保持堆的大小为k
#    - 时间复杂度：O(n log k)，其中n是数组长度，k是要返回的元素数量
#    - 空间复杂度：O(n)，需要存储所有元素的频率
# 
# 2. 桶排序方法：
#    - 统计每个元素的频率
#    - 创建桶，索引表示频率，值是具有该频率的元素列表
#    - 从高频率到低频率遍历桶，收集元素直到达到k个
#    - 时间复杂度：O(n)，线性时间
#    - 空间复杂度：O(n)
# 
# 3. 排序方法：
#    - 统计每个元素的频率
#    - 按照频率排序
#    - 取前k个元素
#    - 时间复杂度：O(n log n)
#    - 空间复杂度：O(n)
# 
# 4. 优化技巧：
#    - 在Python中使用Counter可以快速统计频率
#    - 对于最小堆，可以直接使用heapq模块
#    - 桶排序在大多数情况下是最优的，尤其是当k较大时
# 
# 5. 应用场景：
#    - 当需要获取一组元素中出现频率最高的k个元素时
#    - 这种方法在数据分析、文本处理、推荐系统等领域有广泛应用
# 
# 6. 边界情况处理：
#    - 空数组
#    - k为0或大于不同元素的数量
#    - 所有元素频率相同的情况
#    - 单个元素的情况