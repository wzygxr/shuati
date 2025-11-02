import heapq
from collections import Counter

class Solution:
    """
    相关题目11: LeetCode 347. 前 K 个高频元素
    题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
    题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
    解题思路: 使用哈希表统计每个元素的频率，然后使用最小堆筛选出频率最高的k个元素
    时间复杂度: O(n log k)，其中n是数组长度，构建哈希表需要O(n)，维护大小为k的堆需要O(n log k)
    空间复杂度: O(n)，哈希表需要O(n)空间，堆需要O(k)空间
    是否最优解: 是，这是求解前K个高频元素的最优解法之一
    
    本题属于堆的典型应用场景：需要在一组元素中快速找出前K个最大值（或最小值）
    """
    
    def topKFrequent(self, nums, k):
        """
        使用最小堆求解前K个高频元素
        
        Args:
            nums: 输入的整数数组
            k: 需要返回的高频元素数量
            
        Returns:
            list: 出现频率前k高的元素组成的数组
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查输入数组是否为None或空
        if not nums:
            raise ValueError("输入数组不能为空")
        
        # 异常处理：检查k是否在有效范围内
        if k <= 0 or k > len(nums):
            raise ValueError(f"k的值必须在1到数组长度之间，当前k={k}，数组长度={len(nums)}")
        
        # 特殊情况：如果数组只有一个元素且k=1
        if len(nums) == 1 and k == 1:
            return nums
        
        # 1. 使用Counter统计每个元素的出现频率
        frequency_map = Counter(nums)
        
        # 2. 使用最小堆维护频率最高的k个元素
        # 堆中存储的是(-频率, 元素)的元组，按频率升序排列
        # 使用负数是因为Python的heapq默认是最小堆，这样可以模拟最大堆的效果
        min_heap = []
        
        # 遍历哈希表，维护一个大小为k的最小堆
        for num, freq in frequency_map.items():
            if len(min_heap) < k:
                # 如果堆的大小小于k，直接将元素加入堆
                heapq.heappush(min_heap, (freq, num))
            elif freq > min_heap[0][0]:
                # 如果当前元素的频率大于堆顶元素的频率
                # 则移除堆顶元素，加入当前元素
                heapq.heappop(min_heap)
                heapq.heappush(min_heap, (freq, num))
            # 否则，不做任何操作
        
        # 3. 从堆中取出k个元素，放入结果数组
        result = [0] * k
        for i in range(k - 1, -1, -1):
            result[i] = heapq.heappop(min_heap)[1]
        
        return result
    
    def topKFrequentBucketSort(self, nums, k):
        """
        使用桶排序求解前K个高频元素（另一种实现方式，时间复杂度更优）
        
        Args:
            nums: 输入的整数数组
            k: 需要返回的高频元素数量
            
        Returns:
            list: 出现频率前k高的元素组成的数组
        """
        if not nums:
            return []
        
        # 统计每个元素的频率
        frequency_map = Counter(nums)
        
        # 创建桶，桶的索引表示频率，桶中存储具有该频率的元素
        n = len(nums)
        buckets = [[] for _ in range(n + 1)]
        
        # 将元素放入对应的桶中
        for num, freq in frequency_map.items():
            buckets[freq].append(num)
        
        # 从高频率到低频率遍历桶，收集前k个元素
        result = []
        for i in range(n, -1, -1):
            if len(result) >= k:
                break
            result.extend(buckets[i])
        
        # 确保结果数组只有k个元素
        return result[:k]
    
    def topKFrequentCounter(self, nums, k):
        """
        使用Counter的most_common方法求解前K个高频元素（更简洁的实现）
        
        Args:
            nums: 输入的整数数组
            k: 需要返回的高频元素数量
            
        Returns:
            list: 出现频率前k高的元素组成的数组
        """
        # 使用Counter的most_common方法直接获取频率最高的k个元素
        # most_common返回的是[(元素, 频率)]的列表
        return [num for num, _ in Counter(nums).most_common(k)]

# 打印数组的辅助函数
def print_array(arr):
    print(f"[{', '.join(map(str, arr))}]")

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    solution = Solution()
    
    # 测试用例1：基本情况
    nums1 = [1, 1, 1, 2, 2, 3]
    k1 = 2
    print("测试用例1（堆实现）: ")
    result1 = solution.topKFrequent(nums1, k1)
    print_array(result1)  # 期望输出: [1, 2]（或[2, 1]，顺序不要求）
    
    print("测试用例1（桶排序实现）: ")
    result1_bucket = solution.topKFrequentBucketSort(nums1, k1)
    print_array(result1_bucket)
    
    print("测试用例1（Counter most_common实现）: ")
    result1_counter = solution.topKFrequentCounter(nums1, k1)
    print_array(result1_counter)
    
    # 测试用例2：所有元素都相同
    nums2 = [1]
    k2 = 1
    print("\n测试用例2: ")
    result2 = solution.topKFrequent(nums2, k2)
    print_array(result2)  # 期望输出: [1]
    
    # 测试用例3：所有元素频率都不同
    nums3 = [1, 1, 1, 2, 2, 3, 4, 4, 4, 4]
    k3 = 2
    print("\n测试用例3: ")
    result3 = solution.topKFrequent(nums3, k3)
    print_array(result3)  # 期望输出: [4, 1] 或 [1, 4]，取决于实现
    
    # 测试用例4：边界情况 - k等于元素种类数
    nums4 = [1, 2, 3, 4]
    k4 = 4
    print("\n测试用例4: ")
    result4 = solution.topKFrequent(nums4, k4)
    print_array(result4)  # 期望输出: [1, 2, 3, 4]（顺序不要求）
    
    # 测试异常情况
    try:
        empty_nums = []
        solution.topKFrequent(empty_nums, 1)
        print("\n异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"\n异常测试通过: {e}")

# 运行测试
if __name__ == "__main__":
    test_solution()