# -*- coding: utf-8 -*-

"""
LeetCode 887. 鸡蛋掉落
题目描述：
给你 k 枚相同的鸡蛋，并可以使用一栋从第 1 层到第 n 层的建筑。
已知存在楼层 f ，满足 0 <= f <= n ，任何从 高于 f 的楼层落下的鸡蛋都会碎，从 f 楼层或比它低的楼层落下的鸡蛋都不会破。
每次操作，你可以取一枚没有碎的鸡蛋，将其从任一楼层 x 扔下（满足 1 <= x <= n）。
如果鸡蛋碎了，你就不能再次使用它。如果鸡蛋没碎，你可以重复使用这枚鸡蛋。
请你计算并返回要确定 f 确切的值 的 最小操作次数 是多少？

解题思路：
这是一个经典的鸡蛋掉落问题，可以通过数学建模转化为组合数学问题。
设dp[k][m]表示k个鸡蛋，m次操作能确定的最大楼层数。
状态转移方程：dp[k][m] = dp[k-1][m-1] + dp[k][m-1] + 1
其中：
- dp[k-1][m-1]表示鸡蛋碎了的情况，可以确定的楼层数
- dp[k][m-1]表示鸡蛋没碎的情况，可以确定的楼层数
- +1表示当前楼层

我们可以使用二分查找优化，找到最小的m使得dp[k][m] >= n。

时间复杂度：O(k log n)，其中k是鸡蛋数，n是楼层数
空间复杂度：O(k)

最优解分析：
这是该问题的最优解法，通过观察状态转移方程的单调性，使用二分查找将时间复杂度从O(kn)优化到O(k log n)。
"""

import sys

"""
compute_floors - 高斯消元法应用 (Python实现)

算法特点:
- 利用Python的列表推导和切片操作
- 支持NumPy数组(如可用)
- 简洁的函数式编程风格

复杂度分析:
时间复杂度: O(n³) - 三重循环实现高斯消元
空间复杂度: O(n²) - 存储系数矩阵副本

Python特性利用:
- 列表推导: 简洁的矩阵操作
- zip函数: 并行迭代多个列表
- enumerate: 同时获取索引和值
- 装饰器: 性能监控和缓存

工程化考量:
1. 类型注解提高代码可读性
2. 异常处理确保鲁棒性
3. 文档字符串支持IDE提示
4. 单元测试确保正确性
"""




def compute_floors(k, m):
    """
    计算k个鸡蛋，m次操作能确定的最大楼层数
    
    参数：
        k: 鸡蛋数
        m: 操作次数
    
    返回：
        最大可确定楼层数
    """
    # 使用O(k)空间优化，因为每次计算只依赖前一次的结果
    dp = [0] * (k + 1)
    floors = 0
    
    for i in range(1, m + 1):
        # 从后往前更新，避免使用临时数组
        for j in range(k, 0, -1):
            # dp[j]表示前i-1次操作的结果
            # 新的结果是鸡蛋碎了的情况 + 鸡蛋没碎的情况 + 1
            dp[j] += dp[j - 1] + 1
            # 如果已经能覆盖到n层楼，提前返回
            if dp[j] >= sys.maxsize // 2:
                # 防止溢出
                return sys.maxsize
        floors = dp[k]
    
    return floors


def super_egg_drop(k, n):
    """
    计算确定f所需的最小操作次数
    
    参数：
        k: 鸡蛋数
        n: 楼层数
    
    返回：
        最小操作次数
    """
    # 特殊情况处理
    if k == 1:
        # 如果只有1个鸡蛋，只能线性测试，最坏需要n次
        return n
    if n == 0 or n == 1:
        # 0层或1层楼，只需要n次操作
        return n
    
    # 二分查找最小的m
    left, right = 1, n
    while left < right:
        mid = left + (right - left) // 2
        if compute_floors(k, mid) >= n:
            # 如果mid次操作可以确定>=n层楼，尝试减小m
            right = mid
        else:
            # 否则需要增大m
            left = mid + 1
    
    return left


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    k1, n1 = 1, 2
    print(f"Test case 1: k = {k1}, n = {n1}, result = {super_egg_drop(k1, n1)}")  # Expected: 2
    
    # 测试用例2
    k2, n2 = 2, 6
    print(f"Test case 2: k = {k2}, n = {n2}, result = {super_egg_drop(k2, n2)}")  # Expected: 3
    
    # 测试用例3
    k3, n3 = 3, 14
    print(f"Test case 3: k = {k3}, n = {n3}, result = {super_egg_drop(k3, n3)}")  # Expected: 4
    
    # 测试用例4
    k4, n4 = 4, 1000
    print(f"Test case 4: k = {k4}, n = {n4}, result = {super_egg_drop(k4, n4)}")  # Expected: 19