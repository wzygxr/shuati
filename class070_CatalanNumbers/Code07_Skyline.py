#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
不含递增三元组的排列方法数 - 卡特兰数应用

问题描述：
数字从1到n，可以形成很多排列，要求任意从左往右的三个位置，不能出现依次递增的样子
返回排列的方法数，答案对 1000000 取模

数学背景：
这是卡特兰数的一个应用，与避免特定模式的排列相关。
前几项为：1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, ...

解法思路：
1. 动态规划方法：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
2. 递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)

相关题目链接：
- SPOJ SKYLINE: https://www.spoj.com/problems/SKYLINE/
- 洛谷 SP7897: https://www.luogu.com.cn/problem/SP7897
- LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
- LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/

时间复杂度分析：
- 动态规划方法：O(n²)
- 递推公式：O(n)

空间复杂度分析：
- 动态规划方法：O(n)
- 递推公式：O(1)

工程化考量：
- 因为取模的数字含有很多因子，无法用费马小定理或者扩展欧几里得求逆元
- 同时注意到n的范围并不大，直接使用公式4（动态规划方法）
- 1 <= n <= 1000
- 答案对 1000000 取模
"""

MOD = 1000000
MAXN = 1001

f = [0] * MAXN

# 因为取模的数字含有很多因子
# 无法用费马小定理或者扩展欧几里得求逆元
# 同时注意到n的范围并不大，直接使用公式4
def build():
    f[0] = f[1] = 1
    for i in range(2, MAXN):
        f[i] = 0
        for l in range(i):
            r = i - 1 - l
            f[i] = (f[i] + f[l] * f[r] % MOD) % MOD

# 测试函数
if __name__ == "__main__":
    build()
    n = int(input())
    print(f[n])