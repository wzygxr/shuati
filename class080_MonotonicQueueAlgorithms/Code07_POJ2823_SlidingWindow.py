#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
题目名称：POJ 2823 Sliding Window
题目来源：POJ (Peking University Online Judge)
题目链接：http://poj.org/problem?id=2823
题目难度：中等

题目描述：
给定一个大小为 n≤10^6 的数组。有一个大小为 k 的滑动窗口，它从数组的最左边移动到最右边。
你只能在窗口中看到 k 个数字。每次滑动窗口向右移动一个位置。
求出每次滑动窗口中的最大值和最小值。

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

工程化考量：
- 使用collections.deque实现高效的双端队列操作
- 优化输入读取性能，对于大数据量尤为重要
- 优化输出构建，避免频繁的字符串连接操作
- 使用类型注解提高代码可读性和可维护性
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
    dq = deque()  # 存储索引，而不是值本身
    result = []
    
    for i in range(n):
        # 移除队列中超出窗口范围的元素索引
        while dq and dq[0] <= i - k:
            dq.popleft()
        
        # 维护队列的单调递减性质
        # 从队尾移除所有小于当前元素的值对应的索引
        while dq and arr[dq[-1]] <= arr[i]:
            dq.pop()
        
        # 将当前元素索引入队
        dq.append(i)
        
        # 当窗口大小达到k时，记录窗口最大值（队首元素对应的值）
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
    dq = deque()  # 存储索引，而不是值本身
    result = []
    
    for i in range(n):
        # 移除队列中超出窗口范围的元素索引
        while dq and dq[0] <= i - k:
            dq.popleft()
        
        # 维护队列的单调递增性质
        # 从队尾移除所有大于当前元素的值对应的索引
        while dq and arr[dq[-1]] >= arr[i]:
            dq.pop()
        
        # 将当前元素索引入队
        dq.append(i)
        
        # 当窗口大小达到k时，记录窗口最小值（队首元素对应的值）
        if i >= k - 1:
            result.append(arr[dq[0]])
    
    return result

def main():
    """
    主函数：处理输入、调用算法、输出结果
    针对大数据量进行输入输出优化
    """
    try:
        # 优化输入读取，对于大输入数据尤为重要
        if sys.stdin.isatty():
            # 交互模式，逐行读取
            n, k = map(int, sys.stdin.readline().split())
            arr = list(map(int, sys.stdin.readline().split()))
        else:
            # 非交互模式，一次性读取所有输入
            data = sys.stdin.read().split()
            n = int(data[0])
            k = int(data[1])
            arr = list(map(int, data[2:2+n]))
        
        # 计算最大值和最小值
        min_values = get_min(arr, n, k)
        max_values = get_max(arr, n, k)
        
        # 优化输出构建和打印
        # 使用生成器表达式和join方法，减少内存使用
        print(' '.join(map(str, min_values)))
        print(' '.join(map(str, max_values)))
        
    except Exception as e:
        # 异常处理，增加代码鲁棒性
        sys.stderr.write(f"Error: {str(e)}")
        sys.exit(1)

if __name__ == "__main__":
    main()