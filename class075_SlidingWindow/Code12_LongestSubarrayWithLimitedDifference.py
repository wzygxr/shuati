# -*- coding: utf-8 -*-
"""
绝对差不超过限制的最长连续子数组问题解决方案

问题描述：
给你一个整数数组 nums ，和一个表示限制的整数 limit，
请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit 。

解题思路：
使用滑动窗口配合两个堆来维护窗口内的最大值和最小值：
1. 右指针不断扩展窗口，将元素加入最大堆和最小堆
2. 当窗口内最大值与最小值的差超过 limit 时，收缩左指针
3. 使用延迟删除技术处理堆中已移出窗口的元素
4. 最大堆和最小堆的堆顶分别获取窗口的最大值和最小值

算法复杂度分析：
时间复杂度: O(n * log n) - 每个元素最多入队和出队一次，堆操作需要 O(log n)
空间复杂度: O(n) - 堆最多存储 n 个元素

是否最优解: 是，这是处理该问题的较优解法之一，还可以用单调队列优化到 O(n)

相关题目链接：
LeetCode 1438. 绝对差不超过限制的最长连续子数组
https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/

其他平台类似题目：
1. 牛客网 - 绝对差不超过限制的最长连续子数组
   https://www.nowcoder.com/practice/1266570c4a06487981ed50e84e8b720d
2. LintCode 1438. 绝对差不超过限制的最长连续子数组
   https://www.lintcode.com/problem/1438/
3. HackerRank - Longest Subarray with Limited Difference
   https://www.hackerrank.com/challenges/longest-subarray-with-limited-difference/problem
4. CodeChef - SUBARR - Subarray with Limited Difference
   https://www.codechef.com/problems/SUBARR
5. AtCoder - ABC146 D - Enough Array
   https://atcoder.jp/contests/abc146/tasks/abc146_d
6. 洛谷 P1886 滑动窗口
   https://www.luogu.com.cn/problem/P1886
7. 杭电OJ 4193 Sliding Window
   http://acm.hdu.edu.cn/showproblem.php?pid=4193
8. POJ 2823 Sliding Window
   http://poj.org/problem?id=2823
9. UVa OJ 11536 - Smallest Sub-Array
   https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
10. SPOJ - ADAFRIEN - Ada and Friends
    https://www.spoj.com/problems/ADAFRIEN/

工程化考量：
1. 异常处理：处理空数组等边界情况
2. 性能优化：使用堆维护窗口元素最值，避免重复计算
3. 可读性：变量命名清晰，添加详细注释，提供测试用例
"""

from collections import deque
import heapq


def longest_subarray(nums, limit):
    """
    计算绝对差不超过限制的最长连续子数组长度
    
    Args:
        nums (List[int]): 输入的整数数组
        limit (int): 限制值，子数组中任意两个元素的绝对差不能超过此值
    
    Returns:
        int: 最长连续子数组的长度
    
    Examples:
        >>> longest_subarray([8, 2, 4, 7], 4)
        2
        >>> longest_subarray([10, 1, 2, 4, 7, 2], 5)
        4
    """
    # 异常情况处理
    if not nums:
        return 0
    
    # 最大堆和最小堆，存储 (值, 索引) 元组
    # Python的heapq是最小堆，存储负值来模拟最大堆
    max_heap = []  # 最大堆（存储负值）
    min_heap = []  # 最小堆
    
    left = 0  # 滑动窗口左指针
    result = 0  # 记录最长子数组长度
    
    # 右指针扩展窗口
    for right in range(len(nums)):
        # 将右指针元素加入两个堆
        # 存储负值模拟最大堆
        heapq.heappush(max_heap, (-nums[right], right))
        heapq.heappush(min_heap, (nums[right], right))
        
        # 当窗口内最大值与最小值的差超过 limit 时，需要收缩左指针
        # -max_heap[0][0] 获取最大值，min_heap[0][0] 获取最小值
        while -max_heap[0][0] - min_heap[0][0] > limit:
            # 移除堆顶已过期的元素（索引小于left的元素）
            # 这是延迟删除技术，避免在堆中直接删除元素
            while max_heap and max_heap[0][1] <= left:
                heapq.heappop(max_heap)
            while min_heap and min_heap[0][1] <= left:
                heapq.heappop(min_heap)
            
            # 移动左指针
            left += 1
        
        # 更新最长子数组长度（当前窗口大小）
        result = max(result, right - left + 1)
    
    return result


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    nums1 = [8, 2, 4, 7]
    limit1 = 4
    result1 = longest_subarray(nums1, limit1)
    print("输入数组:", nums1)
    print("限制值:", limit1)
    print("最长子数组长度:", result1)
    # 预期输出: 2 ([2,4] 或 [4,7])
    
    # 测试用例2
    nums2 = [10, 1, 2, 4, 7, 2]
    limit2 = 5
    result2 = longest_subarray(nums2, limit2)
    print("\n输入数组:", nums2)
    print("限制值:", limit2)
    print("最长子数组长度:", result2)
    # 预期输出: 4 ([2,4,7,2])
    
    # 测试用例3
    nums3 = [4, 2, 2, 2, 4, 4, 2, 2]
    limit3 = 0
    result3 = longest_subarray(nums3, limit3)
    print("\n输入数组:", nums3)
    print("限制值:", limit3)
    print("最长子数组长度:", result3)
    # 预期输出: 3 ([2,2,2])
    
    # 测试用例4：空数组
    nums4 = []
    limit4 = 1
    result4 = longest_subarray(nums4, limit4)
    print("\n输入数组:", nums4)
    print("限制值:", limit4)
    print("最长子数组长度:", result4)
    # 预期输出: 0