# 非负数组前k个最小的子序列累加和
# 
# 问题描述：
# 给定一个数组nums，含有n个数字，都是非负数
# 给定一个正数k，返回所有子序列中累加和最小的前k个累加和
# 子序列是包含空集的
# 
# 数据范围：
# 1 <= n <= 10^5
# 1 <= nums[i] <= 10^6
# 1 <= k <= 10^5
# 
# 解题思路：
# 这个问题有多种解法：
# 1. 暴力方法：生成所有子序列的和，然后排序取前k个
# 2. 01背包方法：使用动态规划计算每个和的方案数，然后按顺序取前k个
# 3. 堆方法：使用最小堆来逐步生成前k个最小的子序列和
# 
# 由于数据量较大，01背包方法的时间复杂度过高，最优解是使用堆的方法。
# 
# 堆方法的核心思想：
# 1. 首先对数组进行排序
# 2. 使用最小堆来维护当前可能的最小和
# 3. 从空集开始，逐步扩展子序列
# 4. 对于当前的子序列，可以有两种扩展方式：
#    - 替换最右元素为下一个元素
#    - 添加下一个元素
# 
# 时间复杂度：O(n * log n) + O(k * log k)
# 空间复杂度：O(k)

import heapq
from typing import List

def topKSum1(nums: List[int], k: int) -> List[int]:
    """
    暴力方法
    
    解题思路：
    生成所有子序列的和，然后排序取前k个
    
    时间复杂度：O(2^n * log(2^n)) = O(2^n * n)
    空间复杂度：O(2^n)
    
    Args:
        nums: 非负数组
        k: 前k个最小的子序列和
    
    Returns:
        前k个最小的子序列和
    """
    # 存储所有子序列的和
    all_subsequences = []
    
    # 递归生成所有子序列的和
    def f1(index: int, sum_val: int) -> None:
        # 基础情况：已经处理完所有元素
        if index == len(nums):
            # 将当前子序列的和添加到结果列表中
            all_subsequences.append(sum_val)
        else:
            # 递归情况：对当前元素有两种选择
            # 1. 不选择当前元素
            f1(index + 1, sum_val)
            # 2. 选择当前元素
            f1(index + 1, sum_val + nums[index])
    
    # 生成所有子序列的和
    f1(0, 0)
    
    # 对所有子序列和进行排序
    all_subsequences.sort()
    
    # 取前k个最小的子序列和
    return all_subsequences[:k]

def topKSum2(nums: List[int], k: int) -> List[int]:
    """
    01背包方法
    
    解题思路：
    使用动态规划计算每个和的方案数，然后按顺序取前k个
    
    时间复杂度：O(n * sum)，其中sum是数组元素和
    空间复杂度：O(sum)
    
    注意：由于数据量较大，这种方法的时间复杂度过高，不是最优解
    
    Args:
        nums: 非负数组
        k: 前k个最小的子序列和
    
    Returns:
        前k个最小的子序列和
    """
    # 计算数组元素和
    total_sum = sum(nums)
    
    # dp[j] 表示组成和为j的方案数
    # 1) dp[j] 表示不选择当前元素
    # 2) dp[j - num] 表示选择当前元素
    dp = [0] * (total_sum + 1)
    # 初始状态：和为0的方案数为1（空集）
    dp[0] = 1
    
    # 遍历每个元素
    for num in nums:
        # 倒序遍历和，确保每个元素只使用一次
        for j in range(total_sum, num - 1, -1):
            # 状态转移方程：dp[j] = dp[j] + dp[j - num]
            dp[j] += dp[j - num]
    
    # 按顺序取前k个最小的子序列和
    ans = []
    for j in range(total_sum + 1):
        # 对于和为j的情况，有dp[j]个方案
        for _ in range(min(dp[j], k - len(ans))):
            ans.append(j)
            if len(ans) == k:
                return ans
    
    return ans

def topKSum3(nums: List[int], k: int) -> List[int]:
    """
    正式方法（最优解）
    
    解题思路：
    使用最小堆来逐步生成前k个最小的子序列和
    
    核心思想：
    1. 首先对数组进行排序
    2. 使用最小堆来维护当前可能的最小和
    3. 从空集开始，逐步扩展子序列
    4. 对于当前的子序列，可以有两种扩展方式：
       - 替换最右元素为下一个元素
       - 添加下一个元素
    
    时间复杂度：O(n * log n) + O(k * log k)
    空间复杂度：O(k)
    
    Args:
        nums: 非负数组
        k: 前k个最小的子序列和
    
    Returns:
        前k个最小的子序列和
    """
    import heapq
    
    # 对数组进行排序
    nums.sort()
    
    # 最小堆，存储(子序列的累加和, 子序列的最右下标)
    heap = [(nums[0], 0)]
    
    # 存储结果
    ans = [0]  # 空集的和为0
    
    # 逐步生成前k个最小的子序列和
    while len(ans) < k:
        # 取出当前最小的子序列和
        sum_val, right = heapq.heappop(heap)
        ans.append(sum_val)
        
        # 扩展当前子序列
        if right + 1 < len(nums):
            # 替换最右元素为下一个元素
            heapq.heappush(heap, (sum_val - nums[right] + nums[right + 1], right + 1))
            # 添加下一个元素
            heapq.heappush(heap, (sum_val + nums[right + 1], right + 1))
    
    return ans[:k]

# 测试函数
def random_array(length: int, value: int) -> List[int]:
    """
    生成随机数组用于测试
    
    Args:
        length: 数组长度
        value: 数组元素的最大值
    
    Returns:
        随机数组
    """
    import random
    return [random.randint(0, value) for _ in range(length)]

# 对数器测试
if __name__ == "__main__":
    import random
    
    n = 15
    v = 40
    test_time = 5000
    print("测试开始")
    
    for i in range(test_time):
        length = random.randint(1, n)
        nums = random_array(length, v)
        k = random.randint(1, (1 << length) - 1)
        
        ans1 = topKSum1(nums, k)
        ans2 = topKSum2(nums, k)
        ans3 = topKSum3(nums, k)
        
        if ans1 != ans2 or ans1 != ans3:
            print("出错了！")
            print(f"nums: {nums}")
            print(f"k: {k}")
            print(f"ans1: {ans1}")
            print(f"ans2: {ans2}")
            print(f"ans3: {ans3}")
            break
    
    print("测试结束")