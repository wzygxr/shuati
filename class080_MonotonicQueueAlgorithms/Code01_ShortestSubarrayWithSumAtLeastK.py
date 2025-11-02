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
"""

def shortestSubarray(nums, k):
    if not nums or k <= 0:
        return -1
    
    n = len(nums)
    prefix_sum = [0] * (n + 1)
    for i in range(n):
        prefix_sum[i + 1] = prefix_sum[i] + nums[i]
    
    dq = deque()
    min_length = sys.maxsize
    
    for i in range(n + 1):
        while dq and prefix_sum[i] - prefix_sum[dq[0]] >= k:
            min_length = min(min_length, i - dq.popleft())
        
        while dq and prefix_sum[dq[-1]] >= prefix_sum[i]:
            dq.pop()
        
        dq.append(i)
    
    return min_length if min_length != sys.maxsize else -1

def test_shortest_subarray():
    print("=== LeetCode 862 测试用例 ===")
    
    nums1 = [2, -1, 2]
    k1 = 3
    result1 = shortestSubarray(nums1, k1)
    print("测试用例1 - 输入: [2,-1,2], k=3")
    print(f"预期输出: 3, 实际输出: {result1}")
    print(f"测试结果: {'✓ 通过' if result1 == 3 else '✗ 失败'}")
    
    nums2 = [1, 2, -3, 4, 5]
    k2 = 7
    result2 = shortestSubarray(nums2, k2)
    print("\n测试用例2 - 输入: [1,2,-3,4,5], k=7")
    print(f"预期输出: 2, 实际输出: {result2}")
    print(f"测试结果: {'✓ 通过' if result2 == 2 else '✗ 失败'}")
    
    print("\n=== 算法分析 ===")
    print("时间复杂度: O(n) - 每个元素最多入队出队一次")
    print("空间复杂度: O(n) - 前缀和数组和单调队列")
    print("最优解: ✅ 是")

if __name__ == "__main__":
    test_shortest_subarray()