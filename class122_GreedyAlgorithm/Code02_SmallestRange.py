# 最小区间
# 你有k个非递减排列的整数列表
# 找到一个最小区间，使得k个列表中的每个列表至少有一个数包含在其中
# 测试链接 : https://leetcode.cn/problems/smallest-range-covering-elements-from-k-lists/

import heapq

def smallestRange(nums):
    """
    找到最小区间，使得k个列表中的每个列表至少有一个数包含在其中
    
    算法思路：
    使用滑动窗口 + 最小堆的贪心策略：
    1. 将每个数组的第一个元素加入最小堆
    2. 每次取出最小值，将其对应数组的下一个元素加入最小堆
    3. 在过程中记录最小的区间
    
    时间复杂度：O(n*logk) - n是所有元素总数，k是数组数量
    空间复杂度：O(k) - 最小堆中最多存储k个元素
    
    :param nums: k个非递减排列的整数列表
    :return: 最小区间 [start, end]
    """
    k = len(nums)
    
    # 初始化最小堆，存储 (值, 数组索引, 元素索引)
    heap = []
    
    # 当前的最大值
    max_val = float('-inf')
    
    # 将每个数组的第一个元素加入堆
    for i in range(k):
        heapq.heappush(heap, (nums[i][0], i, 0))
        max_val = max(max_val, nums[i][0])
    
    # 记录最小区间
    range_size = float('inf')
    a, b = 0, 0
    
    # 当堆中有k个元素时继续循环
    while len(heap) == k:
        min_val, list_idx, elem_idx = heapq.heappop(heap)
        
        # 更新最小区间
        if max_val - min_val < range_size:
            range_size = max_val - min_val
            a, b = min_val, max_val
            
        # 如果当前数组还有下一个元素，则将其加入堆
        if elem_idx + 1 < len(nums[list_idx]):
            next_val = nums[list_idx][elem_idx + 1]
            heapq.heappush(heap, (next_val, list_idx, elem_idx + 1))
            max_val = max(max_val, next_val)
    
    return [a, b]

# 测试用例
if __name__ == "__main__":
    # 测试用例: [[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]
    # 期望结果: [20,24]
    nums = [
        [4, 10, 15, 24, 26],
        [0, 9, 12, 20],
        [5, 18, 22, 30]
    ]
    
    result = smallestRange(nums)
    print("测试用例: [[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]")
    print("结果: [" + str(result[0]) + ", " + str(result[1]) + "]")  # 期望输出: [20, 24]