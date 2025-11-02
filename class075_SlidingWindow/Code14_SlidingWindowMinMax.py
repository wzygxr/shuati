# -*- coding: utf-8 -*-
"""
滑动窗口最大值和最小值问题解决方案

问题描述：
现在有一堆数字共N个数字（N<=10^6），以及一个大小为k的窗口。
现在这个从左边开始向右滑动，每次滑动一个单位，求出每次滑动后窗口中的最大值和最小值。

解题思路：
使用单调队列来解决滑动窗口的最值问题：
1. 维护两个双端队列：
   - 一个单调递增队列用于维护窗口最小值
   - 一个单调递减队列用于维护窗口最大值
2. 队列中存储数组元素的索引，便于判断元素是否在窗口范围内
3. 当窗口形成后（i >= k-1），记录当前窗口的最值

算法复杂度分析：
时间复杂度: O(n) - 每个元素最多入队和出队一次
空间复杂度: O(k) - 双端队列最多存储k个元素

是否最优解: 是，这是处理滑动窗口最值问题的最优解法

相关题目链接：
1. 洛谷 P1886 滑动窗口
   https://www.luogu.com.cn/problem/P1886
2. POJ 2823 Sliding Window
   http://poj.org/problem?id=2823
3. LeetCode 239. 滑动窗口最大值
   https://leetcode.cn/problems/sliding-window-maximum/
4. 牛客网 - 滑动窗口最大值
   https://www.nowcoder.com/practice/1624bc35a45c42c0bc17d17fa0cba788
5. LintCode 362. 滑动窗口的最大值
   https://www.lintcode.com/problem/362/
6. HackerRank - Sliding Window Maximum
   https://www.hackerrank.com/challenges/sliding-window-maximum/problem
7. CodeChef - MAXSWINDOW - Maximum in Sliding Window
   https://www.codechef.com/problems/MAXSWINDOW
8. AtCoder - ABC146 D - Enough Array
   https://atcoder.jp/contests/abc146/tasks/abc146_d
9. UVa OJ 11536 - Smallest Sub-Array
   https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
10. SPOJ - ADAFRIEN - Ada and Friends
    https://www.spoj.com/problems/ADAFRIEN/

工程化考量：
1. 异常处理：处理空数组、k为负数或0等边界情况
2. 性能优化：使用单调队列避免重复计算，达到线性时间复杂度
3. 可读性：变量命名清晰，添加详细注释，提供测试用例
"""

from collections import deque


def sliding_window_min_max(nums, k):
    """
    计算滑动窗口中的最大值和最小值
    
    Args:
        nums (List[int]): 输入的整数数组
        k (int): 滑动窗口的大小
    
    Returns:
        List[List[int]]: 二维数组，[0]存储最小值序列，[1]存储最大值序列
    
    Examples:
        >>> sliding_window_min_max([1, 3, -1, -3, 5, 3, 6, 7], 3)
        [[-1, -3, -3, -3, 3, 3], [3, 3, 5, 5, 6, 7]]
        >>> sliding_window_min_max([1], 1)
        [[1], [1]]
    """
    # 异常情况处理
    if not nums or k <= 0:
        return [[], []]
    
    n = len(nums)
    # 结果数组，[0]存储最小值序列，[1]存储最大值序列
    result = [[], []]
    # 单调递增队列，队首是当前窗口的最小值索引
    min_deque = deque()
    # 单调递减队列，队首是当前窗口的最大值索引
    max_deque = deque()
    
    # 遍历数组中的每个元素
    for i in range(n):
        # 移除队列中超出窗口范围的索引
        # 当前窗口范围是 [i-k+1, i]，所以队首索引小于 i-k+1 的元素已经不在窗口内
        while min_deque and min_deque[0] < i - k + 1:
            min_deque.popleft()
        while max_deque and max_deque[0] < i - k + 1:
            max_deque.popleft()
        
        # 维护单调递增队列（用于最小值）
        # 移除所有大于等于当前元素的索引，保持队列单调递增
        while min_deque and nums[min_deque[-1]] >= nums[i]:
            min_deque.pop()
        
        # 维护单调递减队列（用于最大值）
        # 移除所有小于等于当前元素的索引，保持队列单调递减
        while max_deque and nums[max_deque[-1]] <= nums[i]:
            max_deque.pop()
        
        # 将当前元素索引加入队列尾部
        min_deque.append(i)
        max_deque.append(i)
        
        # 当窗口形成后（i >= k-1），记录当前窗口的最值
        # 窗口形成的条件是已经遍历了至少k个元素
        if i >= k - 1:
            result[0].append(nums[min_deque[0]])  # 最小值
            result[1].append(nums[max_deque[0]])  # 最大值
    
    return result


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    result1 = sliding_window_min_max(nums1, k1)
    print("输入数组:", nums1)
    print("窗口大小:", k1)
    print("最小值序列:", result1[0])
    print("最大值序列:", result1[1])
    # 预期输出: 
    # 最小值序列: [-1, -3, -3, -3, 3, 3]
    # 最大值序列: [3, 3, 5, 5, 6, 7]
    
    # 测试用例2
    nums2 = [1]
    k2 = 1
    result2 = sliding_window_min_max(nums2, k2)
    print("\n输入数组:", nums2)
    print("窗口大小:", k2)
    print("最小值序列:", result2[0])
    print("最大值序列:", result2[1])
    # 预期输出: 
    # 最小值序列: [1]
    # 最大值序列: [1]
    
    # 测试用例3
    nums3 = [1, -1]
    k3 = 1
    result3 = sliding_window_min_max(nums3, k3)
    print("\n输入数组:", nums3)
    print("窗口大小:", k3)
    print("最小值序列:", result3[0])
    print("最大值序列:", result3[1])
    # 预期输出: 
    # 最小值序列: [1, -1]
    # 最大值序列: [1, -1]
    
    # 测试用例4：空数组
    nums4 = []
    k4 = 1
    result4 = sliding_window_min_max(nums4, k4)
    print("\n输入数组:", nums4)
    print("窗口大小:", k4)
    print("最小值序列长度:", len(result4[0]))
    print("最大值序列长度:", len(result4[1]))
    # 预期输出: 
    # 最小值序列长度: 0
    # 最大值序列长度: 0