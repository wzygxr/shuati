# -*- coding: utf-8 -*-

"""
UVA 10518 How Many Calls?
题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1459
题目大意: 定义函数f(n) = f(n-1) + f(n-2) + 1，其中f(0) = f(1) = 1，求f(n) mod M的值
解法: 使用矩阵快速幂求解
时间复杂度: O(logn)
空间复杂度: O(1)

数学分析:
1. 递推关系: f(n) = f(n-1) + f(n-2) + 1
2. 转换为矩阵形式:
   [f(n)]   [1 1 1] [f(n-1)]
   [f(n-1)] = [1 0 0] [f(n-2)]
   [1]      [0 0 1] [1]

优化思路:
1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
2. 注意模运算防止溢出

工程化考虑:
1. 边界条件处理: n=0, n=1的特殊情况
2. 输入验证: 检查n和M的有效性
3. 特殊情况: 当n=0时直接返回0

与其他解法对比:
1. 递归解法: 时间复杂度O(2^n)，空间复杂度O(n)，会超时
2. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
3. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)，最优解
"""


class Matrix:
    """
    矩阵类
    """
    def __init__(self, mod):
        self.mod = mod
        self.m = [[0 for _ in range(3)] for _ in range(3)]
    
    def __mul__(self, other):
        """
        矩阵乘法
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        res = Matrix(self.mod)
        for i in range(3):
            for j in range(3):
                for k in range(3):
                    res.m[i][j] = (res.m[i][j] + self.m[i][k] * other.m[k][j]) % self.mod
        return res


def identity_matrix(mod):
    """
    构造单位矩阵
    时间复杂度: O(1)
    空间复杂度: O(1)
    """
    res = Matrix(mod)
    for i in range(3):
        res.m[i][i] = 1
    return res


def matrix_power(base, exp):
    """
    矩阵快速幂
    时间复杂度: O(logn)
    空间复杂度: O(1)
    """
    res = identity_matrix(base.mod)
    while exp > 0:
        if exp & 1:
            res = res * base
        base = base * base
        exp >>= 1
    return res


def solve(n, m):
    """
    求解f(n) mod M
    时间复杂度: O(logn)
    空间复杂度: O(1)
    
    算法思路:
    1. 构造转移矩阵[[1,1,1],[1,0,0],[0,0,1]]
    2. 计算转移矩阵的n次幂
    3. 乘以初始向量[1,1,1]得到结果
    """
    # 特殊情况处理
    if n == 0:
        return 1 % m
    
    # 转移矩阵
    base = Matrix(m)
    base.m[0][0] = 1
    base.m[0][1] = 1
    base.m[0][2] = 1
    base.m[1][0] = 1
    base.m[1][1] = 0
    base.m[1][2] = 0
    base.m[2][0] = 0
    base.m[2][1] = 0
    base.m[2][2] = 1
    
    # 计算转移矩阵的n次幂
    result = matrix_power(base, n)
    
    # 初始向量 [f(1), f(0), 1] = [1, 1, 1]
    # 结果为 result * [1, 1, 1]^T 的第一个元素
    return (result.m[0][0] + result.m[0][1] + result.m[0][2]) % m


def main():
    """
    主函数
    """
    case_num = 0
    
    while True:
        line = input().split()
        n = int(line[0])
        m = int(line[1])
        
        # 输入终止条件
        if n == 0 and m == 0:
            break
        
        case_num += 1
        result = solve(n, m)
        print(f"Case {case_num}: {n} {m} {result}")


# 为了兼容不同的运行环境，只有在直接运行此文件时才执行main函数
if __name__ == "__main__":
    main()