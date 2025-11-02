# -*- coding: utf-8 -*-

"""
UVA 11149 Power of Matrix
题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2090
题目大意: 给定一个n×n的矩阵A，求A^1 + A^2 + ... + A^k的值，结果对10取模
解法: 使用矩阵快速幂和分治法求解
时间复杂度: O(n^3 * logk)
空间复杂度: O(n^2)

数学分析:
1. 当k为偶数时: S(k) = S(k/2) + A^(k/2) * S(k/2)
2. 当k为奇数时: S(k) = S(k-1) + A^k

优化思路:
1. 使用分治法优化求和过程
2. 结合矩阵快速幂计算矩阵的幂

工程化考虑:
1. 边界条件处理: k=0, k=1的特殊情况
2. 输入验证: 检查输入的有效性
3. 模运算: 防止整数溢出

与其他解法对比:
1. 暴力解法: 直接计算每一项然后求和，时间复杂度O(k*n^3)
2. 本解法: 使用分治和矩阵快速幂，时间复杂度O(n^3 * logk)
3. 最优性: 当k较大时，本解法明显优于暴力解法
"""

MOD = 10


class Matrix:
    """
    矩阵类
    """
    def __init__(self, n):
        self.n = n
        self.m = [[0 for _ in range(n)] for _ in range(n)]
    
    def __add__(self, other):
        """
        矩阵加法
        时间复杂度: O(n^2)
        空间复杂度: O(n^2)
        """
        res = Matrix(self.n)
        for i in range(self.n):
            for j in range(self.n):
                res.m[i][j] = (self.m[i][j] + other.m[i][j]) % MOD
        return res
    
    def __mul__(self, other):
        """
        矩阵乘法
        时间复杂度: O(n^3)
        空间复杂度: O(n^2)
        """
        res = Matrix(self.n)
        for i in range(self.n):
            for j in range(self.n):
                for c in range(self.n):
                    res.m[i][j] = (res.m[i][j] + self.m[i][c] * other.m[c][j]) % MOD
        return res


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
    时间复杂度: O(n^3 * logp)
    空间复杂度: O(n^2)
    """
    res = identity_matrix(base.n)
    while exp > 0:
        if exp & 1:
            res = res * base
        base = base * base
        exp >>= 1
    return res


def matrix_power_series(base, exp):
    """
    矩阵幂级数求和 - 分治法
    时间复杂度: O(n^3 * logk)
    空间复杂度: O(n^2)
    
    算法思路:
    1. 当exp=1时，直接返回base
    2. 当exp为奇数时，S(k) = S(k-1) + A^k
    3. 当exp为偶数时，S(k) = (A^(k/2) + I) * S(k/2)
    """
    # 边界条件处理
    if exp == 0:
        # 返回零矩阵
        return Matrix(base.n)
    
    if exp == 1:
        return base
    
    if exp & 1:
        # S(k) = S(k-1) + A^k
        sub = matrix_power_series(base, exp - 1)
        power = matrix_power(base, exp)
        return sub + power
    else:
        # S(k) = (A^(k/2) + I) * S(k/2)
        half = exp >> 1
        sub = matrix_power_series(base, half)
        power = matrix_power(base, half)
        identity = identity_matrix(base.n)
        factor = power + identity
        return factor * sub


def print_matrix(matrix):
    """
    打印矩阵
    """
    for i in range(matrix.n):
        for j in range(matrix.n):
            if j == 0:
                print(matrix.m[i][j], end='')
            else:
                print(' ' + str(matrix.m[i][j]), end='')
        print()


def main():
    """
    主函数
    """
    while True:
        line = input().split()
        n = int(line[0])
        if n == 0:
            break
        
        k = int(line[1])
        
        # 读取矩阵
        A = Matrix(n)
        for i in range(n):
            row = input().split()
            for j in range(n):
                A.m[i][j] = int(row[j]) % MOD
        
        result = matrix_power_series(A, k)
        print_matrix(result)
        print()  # 输出空行


# 为了兼容不同的运行环境，只有在直接运行此文件时才执行main函数
if __name__ == "__main__":
    main()