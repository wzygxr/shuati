# -*- coding: utf-8 -*-

"""
Codeforces 691E Xor-sequences
题目链接: https://codeforces.com/problemset/problem/691/E
题目大意: 给定长度为n的序列，从序列中选择k个数（可以重复选择），使得得到的排列满足xi与xi+1异或的二进制中1的个数是3的倍数。
         问长度为k的满足条件的序列有多少个。
解法: 使用矩阵快速幂求解
时间复杂度: O(n^3 * logk)
空间复杂度: O(n^2)

数学分析:
1. 构造转移矩阵：如果两个数异或的结果二进制中1的个数是3的倍数，则矩阵对应位置为1，否则为0
2. 答案就是转移矩阵的k-1次幂的所有元素之和

优化思路:
1. 预处理转移矩阵
2. 使用矩阵快速幂计算矩阵的k-1次幂

工程化考虑:
1. 边界条件处理: k=1的特殊情况
2. 输入验证: 检查输入的有效性
3. 模运算: 防止整数溢出

与其他解法对比:
1. 暴力解法: 时间复杂度O(n^k)，会超时
2. 动态规划: 时间复杂度O(n^2 * k)
3. 矩阵快速幂: 时间复杂度O(n^3 * logk)
4. 最优性: 当k较大时，矩阵快速幂明显优于其他解法
"""

MOD = 1000000007


class Matrix:
    """
    矩阵类
    """
    def __init__(self, n):
        self.n = n
        self.m = [[0 for _ in range(n)] for _ in range(n)]
    
    def __mul__(self, other):
        """
        矩阵乘法
        时间复杂度: O(n^3)
        空间复杂度: O(n^2)
        """
        res = Matrix(self.n)
        for i in range(self.n):
            for j in range(self.n):
                for k in range(self.n):
                    res.m[i][j] = (res.m[i][j] + self.m[i][k] * other.m[k][j]) % MOD
        return res


def count_bits(x):
    """
    计算一个数二进制表示中1的个数
    时间复杂度: O(logx)
    空间复杂度: O(1)
    """
    count = 0
    while x > 0:
        count += x & 1
        x >>= 1
    return count


def identity_matrix(n):
    """
    构造单位矩阵
    时间复杂度: O(n^2)
    空间复杂度: O(n^2)
    """
    res = Matrix(n)
    for i in range(n):
        res.m[i][i] = 1
    return res


def matrix_power(base, exp):
    """
    矩阵快速幂
    时间复杂度: O(n^3 * logk)
    空间复杂度: O(n^2)
    """
    res = identity_matrix(base.n)
    while exp > 0:
        if exp & 1:
            res = res * base
        base = base * base
        exp >>= 1
    return res


def solve(n, k, a):
    """
    求解Xor-sequences问题
    时间复杂度: O(n^3 * logk)
    空间复杂度: O(n^2)
    """
    # 特殊情况处理
    if k == 1:
        return n
    
    # 构造转移矩阵
    matrix = Matrix(n)
    for i in range(n):
        for j in range(n):
            xor = a[i] ^ a[j]
            if count_bits(xor) % 3 == 0:
                matrix.m[i][j] = 1
    
    # 计算转移矩阵的k-1次幂
    result = matrix_power(matrix, k - 1)
    
    # 计算结果：所有元素之和
    sum_val = 0
    for i in range(n):
        for j in range(n):
            sum_val = (sum_val + result.m[i][j]) % MOD
    
    return sum_val


def main():
    """
    主函数
    """
    # 读取输入
    line = input().split()
    n = int(line[0])
    k = int(line[1])
    
    line = input().split()
    a = [int(x) for x in line]
    
    # 求解并输出结果
    result = solve(n, k, a)
    print(result)


# 为了兼容不同的运行环境，只有在直接运行此文件时才执行main函数
if __name__ == "__main__":
    main()