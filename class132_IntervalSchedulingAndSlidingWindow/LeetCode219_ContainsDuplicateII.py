"""
LeetCode 219. Contains Duplicate II

题目描述：
给你一个整数数组 nums 和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，
满足 nums[i] == nums[j] 且 abs(i - j) <= k。
如果存在，返回 true；否则，返回 false。

解题思路：
这是一个滑动窗口结合哈希表的问题。

核心思想：
1. 使用滑动窗口维护最多k+1个元素
2. 使用哈希表维护窗口内元素的存在性
3. 当窗口大小超过k+1时，移除最早加入的元素

具体步骤：
1. 遍历数组，维护一个大小为k+1的滑动窗口
2. 对于每个元素，检查它是否已在当前窗口中存在
3. 如果存在，返回true
4. 当窗口大小超过k+1时，移除最早加入的元素

时间复杂度：O(n)
空间复杂度：O(min(n, k))

相关题目：
- LeetCode 220. 存在重复元素 III（TreeSet滑动窗口）
- LeetCode 121. 买卖股票的最佳时机（滑动窗口）
- LeetCode 239. 滑动窗口最大值（双端队列）
"""

def containsNearbyDuplicate(nums, k):
    """
    判断数组中是否存在两个不同的索引满足条件（方法1：滑动窗口+集合）
    
    Args:
        nums: 整数数组
        k: 索引差的最大值
    
    Returns:
        是否存在满足条件的索引对
    """
    # 使用set维护滑动窗口内元素的存在性
    window = set()
    
    for i in range(len(nums)):
        # 如果当前元素已在窗口中存在，返回True
        if nums[i] in window:
            return True
        
        # 将当前元素加入窗口
        window.add(nums[i])
        
        # 如果窗口大小超过k+1，移除最早加入的元素
        if len(window) > k:
            window.discard(nums[i - k])
    
    return False

def containsNearbyDuplicateV2(nums, k):
    """
    判断数组中是否存在两个不同的索引满足条件（方法2：哈希表记录索引）
    
    Args:
        nums: 整数数组
        k: 索引差的最大值
    
    Returns:
        是否存在满足条件的索引对
    """
    # 使用字典维护元素及其最新索引
    index_map = {}
    
    for i in range(len(nums)):
        # 如果元素已存在且索引差满足条件，返回True
        if nums[i] in index_map and i - index_map[nums[i]] <= k:
            return True
        
        # 更新元素的最新索引
        index_map[nums[i]] = i
    
    return False

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 2, 3, 1]
    k1 = 3
    print("测试用例1:")
    print(f"输入: nums = {nums1}, k = {k1}")
    print(f"输出 (方法1): {containsNearbyDuplicate(nums1, k1)}")  # 期望输出: True
    print(f"输出 (方法2): {containsNearbyDuplicateV2(nums1, k1)}")  # 期望输出: True
    
    # 测试用例2
    nums2 = [1, 0, 1, 1]
    k2 = 1
    print("\n测试用例2:")
    print(f"输入: nums = {nums2}, k = {k2}")
    print(f"输出 (方法1): {containsNearbyDuplicate(nums2, k2)}")  # 期望输出: True
    print(f"输出 (方法2): {containsNearbyDuplicateV2(nums2, k2)}")  # 期望输出: True
    
    # 测试用例3
    nums3 = [1, 2, 3, 1, 2, 3]
    k3 = 2
    print("\n测试用例3:")
    print(f"输入: nums = {nums3}, k = {k3}")
    print(f"输出 (方法1): {containsNearbyDuplicate(nums3, k3)}")  # 期望输出: False
    print(f"输出 (方法2): {containsNearbyDuplicateV2(nums3, k3)}")  # 期望输出: False
    
    # 测试用例4
    nums4 = [99, 99]
    k4 = 2
    print("\n测试用例4:")
    print(f"输入: nums = {nums4}, k = {k4}")
    print(f"输出 (方法1): {containsNearbyDuplicate(nums4, k4)}")  # 期望输出: True
    print(f"输出 (方法2): {containsNearbyDuplicateV2(nums4, k4)}")  # 期望输出: True