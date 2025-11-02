import heapq
from collections import Counter

class Solution:
    """
    相关题目2: LeetCode 347. 前 K 个高频元素
    题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
    题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按 任意顺序 返回答案。
    解题思路: 使用Counter统计频率，再用最小堆维护前k个高频元素
    时间复杂度: O(n log k)，其中n是数组长度，统计频率需要O(n)，堆操作需要O(n log k)
    空间复杂度: O(n)，哈希表需要O(n)空间，堆需要O(k)空间
    是否最优解: 是，这是处理Top K频率问题的经典解法
    
    本题属于频率统计+Top K问题的组合应用，是堆数据结构的典型应用场景
    """
    
    def topKFrequent(self, nums, k):
        """
        查找数组中出现频率前k高的元素
        
        Args:
            nums: 输入整数数组
            k: 需要返回的高频元素数量
            
        Returns:
            list: 出现频率前k高的元素列表
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查输入数组是否为空
        if not nums:
            raise ValueError("输入数组不能为空")
        
        # 异常处理：检查k是否在有效范围内
        if k <= 0 or k > len(nums):
            raise ValueError(f"k的值必须在1到数组长度之间，当前k={k}，数组长度={len(nums)}")
        
        # 1. 统计频率 - 使用Counter高效统计每个元素出现的次数
        freq_map = Counter(nums)
        
        # 边界情况优化：如果不同元素的数量小于等于k，直接返回所有不同元素
        if len(freq_map) <= k:
            return list(freq_map.keys())
        
        # 2. 使用最小堆维护前k个高频元素
        # Python的heapq模块实现的是最小堆
        min_heap = []
        
        # 遍历频率映射，维护一个大小为k的最小堆
        for num, frequency in freq_map.items():
            # 调试信息：打印当前处理的元素及其频率
            # print(f"Processing: {num} with frequency: {frequency}")
            
            if len(min_heap) < k:
                # 如果堆的大小小于k，直接将当前元素-频率对加入堆
                # 注意：Python的堆默认是最小堆，所以我们按频率排序
                heapq.heappush(min_heap, (frequency, num))
            elif frequency > min_heap[0][0]:
                # 如果当前元素的频率大于堆顶元素的频率
                # 则移除堆顶元素（当前k个高频元素中频率最小的），并加入新元素
                heapq.heappop(min_heap)
                heapq.heappush(min_heap, (frequency, num))
            # 否则（当前元素的频率小于等于堆顶元素的频率），不做任何操作
        
        # 3. 提取结果 - 从堆中取出所有元素的值
        result = [item[1] for item in min_heap]
        
        # 注意：结果列表的顺序是按照频率从小到大排列的，题目允许任意顺序返回
        return result

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    solution = Solution()
    
    # 测试用例1：普通情况
    nums1 = [1, 1, 1, 2, 2, 3]
    k1 = 2
    result1 = solution.topKFrequent(nums1, k1)
    print(f"示例1输出: {result1}")
    # 验证结果是否包含1和2，顺序不要求
    assert set(result1) == {1, 2}, f"测试用例1失败，期望{{1, 2}}，实际得到{set(result1)}"
    
    # 测试用例2：只有一个元素
    nums2 = [1]
    k2 = 1
    result2 = solution.topKFrequent(nums2, k2)
    print(f"示例2输出: {result2}")
    assert result2 == [1], f"测试用例2失败，期望[1]，实际得到{result2}"
    
    # 测试用例3：所有元素频率相同
    nums3 = [1, 2, 3, 4, 5]
    k3 = 3
    result3 = solution.topKFrequent(nums3, k3)
    print(f"示例3输出: {result3}")
    assert len(result3) == 3, f"测试用例3失败，期望长度为3，实际长度为{len(result3)}"
    
    # 测试用例4：边界情况 - k等于不同元素的数量
    nums4 = [1, 1, 2, 2, 3, 3]
    k4 = 3
    result4 = solution.topKFrequent(nums4, k4)
    print(f"示例4输出: {result4}")
    assert set(result4) == {1, 2, 3}, f"测试用例4失败，期望{{1, 2, 3}}，实际得到{set(result4)}"
    
    # 测试异常情况
    try:
        # 测试用例5：异常测试 - 空数组
        solution.topKFrequent([], 1)
        print("测试用例5失败：未抛出预期的异常")
    except ValueError as e:
        print(f"测试用例5成功捕获异常: {e}")
    
    try:
        # 测试用例6：异常测试 - k超出范围
        solution.topKFrequent([1, 2, 3], 4)
        print("测试用例6失败：未抛出预期的异常")
    except ValueError as e:
        print(f"测试用例6成功捕获异常: {e}")

# 运行测试
if __name__ == "__main__":
    test_solution()
    print("所有测试用例通过！")