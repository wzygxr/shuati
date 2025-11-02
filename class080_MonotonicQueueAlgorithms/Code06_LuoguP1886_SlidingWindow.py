#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
题目名称：洛谷 P1886 滑动窗口/【模板】单调队列
题目来源：洛谷
题目链接：https://www.luogu.com.cn/problem/P1886
题目难度：中等

题目描述：
有一个长为 n 的序列 a，以及一个大小为 k 的窗口。
现在这个窗口从左边开始向右滑动，每次滑动一个单位，求出每次滑动后窗口中的最小值和最大值。

解题思路：
这是单调队列的经典模板题。我们需要在O(n)时间内找到每个滑动窗口的最大值和最小值。
使用两个单调队列：
1. 单调递减队列：队首为窗口最大值
2. 单调递增队列：队首为窗口最小值

算法步骤：
1. 使用双端队列维护窗口内元素的索引
2. 维护一个单调递减队列求最大值
3. 维持一个单调递增队列求最小值
4. 每次窗口移动时更新两个队列并记录结果

时间复杂度：O(n) - 每个元素最多入队和出队各两次
空间复杂度：O(k) - 两个队列最多存储k个元素的索引

是否为最优解：✅ 是，这是解决该问题的最优时间复杂度解法

Python实现注意事项：
- 使用collections.deque实现高效的双端队列操作
- 注意处理大数据输入时的性能优化
"""

from collections import deque
import sys

def get_max(arr, n, k):
    """
    单调递减队列求最大值
    :param arr: 输入数组
    :param n: 数组长度
    :param k: 窗口大小
    :return: 每个窗口的最大值列表
    """
    dq = deque()
    result = []
    
    for i in range(n):
        # 移除队列中超出窗口范围的元素索引
        while dq and dq[0] <= i - k:
            dq.popleft()
        
        # 维护队列的单调递减性质
        while dq and arr[dq[-1]] <= arr[i]:
            dq.pop()
        
        # 将当前元素索引入队
        dq.append(i)
        
        # 当窗口大小达到k时，记录窗口最大值（队首元素）
        if i >= k - 1:
            result.append(arr[dq[0]])
    
    return result

def get_min(arr, n, k):
    """
    单调递增队列求最小值
    :param arr: 输入数组
    :param n: 数组长度
    :param k: 窗口大小
    :return: 每个窗口的最小值列表
    """
    dq = deque()
    result = []
    
    for i in range(n):
        # 移除队列中超出窗口范围的元素索引
        while dq and dq[0] <= i - k:
            dq.popleft()
        
        # 维护队列的单调递增性质
        while dq and arr[dq[-1]] >= arr[i]:
            dq.pop()
        
        # 将当前元素索引入队
        dq.append(i)
        
        # 当窗口大小达到k时，记录窗口最小值（队首元素）
        if i >= k - 1:
            result.append(arr[dq[0]])
    
    return result

def main():
    # 读取输入（针对大数据量进行优化）
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    k = int(input[ptr])
    ptr += 1
    
    arr = list(map(int, input[ptr:ptr+n]))
    
    # 计算最大值和最小值
    min_values = get_min(arr, n, k)
    max_values = get_max(arr, n, k)
    
    # 输出结果（优化输出性能）
    print(' '.join(map(str, min_values)))
    print(' '.join(map(str, max_values)))

if __name__ == "__main__":
    main()