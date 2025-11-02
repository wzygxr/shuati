#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
树屋阶梯问题 - 卡特兰数高精度计算

问题描述：
地面高度是0，想搭建一个阶梯，要求每一个台阶上升1的高度，最终到达高度n
有无穷多任意规格的矩形材料，但是必须选择n个矩形，希望能搭建出阶梯的样子
返回搭建阶梯的不同方法数，答案可能很大，不取模！就打印真实答案

数学背景：
这是卡特兰数的一个应用，需要使用高精度计算。
前几项为：1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, ...

解法思路：
1. 使用组合公式：C(2n, n) / (n+1)
2. python内置大整数支持，无需特殊处理

相关题目链接：
- 洛谷 P2532 树屋阶梯: https://www.luogu.com.cn/problem/P2532
- LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
- LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
- HDU 1023 Train Problem II: http://acm.hdu.edu.cn/showproblem.php?pid=1023

时间复杂度分析：
- 组合公式计算：O(n)

空间复杂度分析：
- 高精度计算存储：O(n)

工程化考量：
- 1 <= n <= 500
- 答案可能很大，不取模！就打印真实答案
"""

def compute(n):
    """
    这里用公式2
    python同学使用内置大整数即可
    C(2n, n)/(n+1)
    """
    if n <= 1:
        return 1
    
    # 计算卡特兰数：C(2n, n)/(n+1)
    result = 1
    for i in range(n):
        result = result * (2 * n - i) // (i + 1)
    
    # 除以(n+1)得到最终结果
    return result // (n + 1)

# 测试函数
if __name__ == "__main__":
    n = int(input())
    print(compute(n))