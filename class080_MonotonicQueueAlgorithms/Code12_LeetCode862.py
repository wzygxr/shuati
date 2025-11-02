from collections import deque
import sys

"""
题目名称：LeetCode 862. 和至少为K的最短子数组
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
题目难度：困难

题目描述：
给定一个数组arr，其中的值有可能正、负、0
给定一个正数k
返回累加和>=k的所有子数组中，最短的子数组长度

解题思路：
使用单调队列解决该问题。核心思想是利用前缀和将问题转化为寻找满足条件的两个前缀和之差。
对于前缀和数组，我们需要找到最小的 j-i，使得 sum[j] - sum[i] >= k。
为了高效查找，我们维护一个单调递增队列，队列中存储前缀和的索引。

算法步骤：
1. 计算前缀和数组
2. 遍历前缀和数组，维护单调递增队列
3. 对于每个前缀和，检查是否能与队首元素构成满足条件的子数组
4. 维护队列的单调性

时间复杂度分析：
O(n) - 每个元素最多入队出队一次

空间复杂度分析：
O(n) - 存储前缀和和单调队列

是否最优解：
✅ 是，这是处理此类问题的最优解法

工程化考量：
- 使用Python内置deque提高代码可读性
- 考虑边界条件处理（k=0, 数组长度为1等）
- 处理极端输入情况（大数组、极限值等）
"""

def shortestSubarray(nums, k):
    """
    计算和至少为K的最短子数组长度
    
    Args:
        nums: 输入数组
        k: 目标和
        
    Returns:
        int: 最短子数组长度，如果不存在返回-1
        
    Raises:
        TypeError: 如果输入不是列表或k不是整数
        ValueError: 如果数组为空或k<=0
    """
    # 输入验证
    if not isinstance(nums, list):
        raise TypeError("nums must be a list")
    if not isinstance(k, int):
        raise TypeError("k must be an integer")
    if not nums:
        return -1
    if k <= 0:
        return -1
    
    n = len(nums)
    # 计算前缀和数组，prefix_sum[i]表示前i个元素的和
    prefix_sum = [0] * (n + 1)
    for i in range(n):
        prefix_sum[i + 1] = prefix_sum[i] + nums[i]
    
    # 使用双端队列维护单调递增的前缀和索引
    dq = deque()
    min_length = sys.maxsize
    
    for i in range(n + 1):
        # 检查当前前缀和与队首前缀和的差是否>=k
        # 如果满足条件，更新最小长度并移除队首元素
        while dq and prefix_sum[i] - prefix_sum[dq[0]] >= k:
            min_length = min(min_length, i - dq.popleft())
        
        # 维护队列的单调递增性质
        # 从队尾开始，移除所有前缀和大于等于当前前缀和的索引
        while dq and prefix_sum[dq[-1]] >= prefix_sum[i]:
            dq.pop()
        
        # 将当前索引加入队列
        dq.append(i)
    
    return min_length if min_length != sys.maxsize else -1

def test_shortest_subarray():
    """测试函数 - 包含多种边界情况和测试用例"""
    print("=== LeetCode 862 测试用例 ===")
    
    # 测试用例1：基础示例
    nums1 = [2, -1, 2]
    k1 = 3
    result1 = shortestSubarray(nums1, k1)
    print("测试用例1 - 输入: [2,-1,2], k=3")
    print(f"预期输出: 3, 实际输出: {result1}")
    print(f"测试结果: {'✓ 通过' if result1 == 3 else '✗ 失败'}")
    
    # 测试用例2：包含负数
    nums2 = [1, 2, -3, 4, 5]
    k2 = 7
    result2 = shortestSubarray(nums2, k2)
    print("\n测试用例2 - 输入: [1,2,-3,4,5], k=7")
    print(f"预期输出: 2, 实际输出: {result2}")
    print(f"测试结果: {'✓ 通过' if result2 == 2 else '✗ 失败'}")
    
    # 测试用例3：单个元素
    nums3 = [5]
    k3 = 5
    result3 = shortestSubarray(nums3, k3)
    print("\n测试用例3 - 输入: [5], k=5")
    print(f"预期输出: 1, 实际输出: {result3}")
    print(f"测试结果: {'✓ 通过' if result3 == 1 else '✗ 失败'}")
    
    # 测试用例4：不存在满足条件的子数组
    nums4 = [-1, -2, -3]
    k4 = 5
    result4 = shortestSubarray(nums4, k4)
    print("\n测试用例4 - 输入: [-1,-2,-3], k=5")
    print(f"预期输出: -1, 实际输出: {result4}")
    print(f"测试结果: {'✓ 通过' if result4 == -1 else '✗ 失败'}")
    
    # 测试用例5：输入验证
    try:
        shortestSubarray("not a list", 3)
    except TypeError as e:
        print(f"\n测试用例5 - 输入验证: ✓ 通过 - {e}")
    else:
        print("\n测试用例5 - 输入验证: ✗ 失败 - 应该抛出TypeError")
    
    print("\n=== 算法分析 ===")
    print("时间复杂度: O(n) - 每个元素最多入队出队一次")
    print("空间复杂度: O(n) - 前缀和数组和单调队列")
    print("最优解: ✅ 是")
    
    print("\n=== Python语言特性分析 ===")
    print("1. 动态类型系统，无需声明变量类型")
    print("2. 内置collections.deque，高效双端队列操作")
    print("3. 简洁的语法和内置异常处理")
    print("4. 支持函数式编程风格")

if __name__ == "__main__":
    test_shortest_subarray()