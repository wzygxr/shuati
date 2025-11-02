#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
加入差值绝对值直到长度固定 - 数学 + 贪心算法

题目描述：
给定一个非负数组arr，计算任何两个数差值的绝对值，
如果arr中没有，都要加入到arr里，但是只加一份。
然后新的arr继续计算任何两个数差值的绝对值，
如果arr中没有，都要加入到arr里，但是只加一份。
一直到arr大小固定，返回arr最终的长度。

解题思路：
1. 暴力方法：不断计算数组中任意两数的差值绝对值，直到数组大小不再变化
2. 优化方法：基于数学性质：
   - 如果数组中有0，则最终数组包含0到max的所有gcd的倍数
   - 如果数组中没有0，则最终数组包含0到max的所有gcd的倍数，但不包含0
   - 重复元素只需保留一个

算法原理：
- 数学性质：两个数的线性组合可以生成它们最大公约数的任意倍数
- 贪心策略：利用最大公约数的性质快速计算最终数组大小

时间复杂度：
- 暴力方法：O(n^2 * k) - k为迭代次数
- 优化方法：O(n * log(max)) - 计算最大公约数的时间复杂度
空间复杂度：O(n) - 存储数组元素的哈希表

相关题目：
- 大厂笔试真题
- 数论相关问题
"""

import math
from collections import Counter

def gcd(a, b):
    """
    计算两个数的最大公约数
    
    Args:
        a: 第一个数
        b: 第二个数
    
    Returns:
        最大公约数
    """
    return math.gcd(a, b)

def len2(arr):
    """
    正式方法（优化解法）
    
    Args:
        arr: 输入数组
    
    Returns:
        最终数组的长度
    """
    if not arr:
        return 0
    
    max_val = max(arr)
    
    # 找到任意一个非0的值
    gcd_val = 0
    for num in arr:
        if num != 0:
            gcd_val = num
            break
    
    if gcd_val == 0:  # 数组中都是0
        return len(arr)
    
    # 不都是0
    cnts = Counter(arr)
    
    for num in arr:
        if num != 0:
            gcd_val = gcd(gcd_val, num)
    
    ans = max_val // gcd_val
    max_cnt = 0
    
    for key, count in cnts.items():
        if key != 0:
            ans += count - 1
        max_cnt = max(max_cnt, count)
    
    ans += cnts.get(0, 1 if max_cnt > 1 else 0)
    
    return ans

# 测试代码
if __name__ == "__main__":
    # 测试用例
    arr1 = [6, 2, 4]
    print(len2(arr1))  # 应该输出 4 (0, 2, 4, 6)
    
    arr2 = [1, 2, 3]
    print(len2(arr2))  # 应该输出 4 (0, 1, 2, 3)
    
    arr3 = [0, 0, 0]
    print(len2(arr3))  # 应该输出 3