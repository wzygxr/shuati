# POJ 2065 SETI
# 给定一个多项式在不同模数下的值，求多项式的系数
# 测试链接 : http://poj.org/problem?id=2065

'''
题目解析:
这是一个浮点数线性方程组问题，需要使用高斯消元求解。

解题思路:
1. 根据多项式的定义建立方程组
2. 对于每个观测点，建立一个方程表示多项式的值
3. 使用高斯消元求解线性方程组

时间复杂度: O(n^3)
空间复杂度: O(n^2)

工程化考虑:
1. 正确处理浮点数运算精度
2. 输入输出处理
'''

import sys

"""
pow - 高斯消元法应用 (Python实现)

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


import math

MAXN = 105
EPS = 1e-10  # 浮点数精度

# 增广矩阵
mat = [[0.0 for _ in range(MAXN)] for _ in range(MAXN)]

n = 0
mod = 0


def pow(base, exp, mod):
    '''
    快速幂运算
    计算 base^exp % mod
    时间复杂度: O(log exp)
    空间复杂度: O(1)
    '''
    result = 1
    while exp > 0:
        if exp % 2 == 1:
            result = (result * base) % mod
        base = (base * base) % mod
        exp //= 2
    return result


def initMatrix(s):
    '''
    初始化矩阵
    时间复杂度: O(n^2)
    空间复杂度: O(n^2)
    '''
    global n
    # 初始化矩阵
    for i in range(n):
        for j in range(n + 1):
            mat[i][j] = 0.0

    # 建立方程组
    for i in range(n):
        c = s[i]
        value = 0 if c == '*' else (ord(c) - ord('a') + 1)
        
        # 对于第i个方程，表示当x=i+1时多项式的值
        for j in range(n):
            # 系数为 (i+1)^j mod mod
            mat[i][j] = pow(i + 1, j, mod)
        # 常数项为value
        mat[i][n] = value


def gauss():
    '''
    高斯消元解决浮点数线性方程组
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
    '''
    for i in range(n):
        # 找到第i列系数绝对值最大的行
        max_row = i
        for j in range(i + 1, n):
            if abs(mat[j][i]) > abs(mat[max_row][i]):
                max_row = j

        # 交换行
        if max_row != i:
            for k in range(n + 1):
                mat[i][k], mat[max_row][k] = mat[max_row][k], mat[i][k]

        # 如果主元为0，说明矩阵奇异
        if abs(mat[i][i]) < EPS:
            continue

        # 将第i行主元系数化为1
        pivot = mat[i][i]
        for k in range(i, n + 1):
            mat[i][k] /= pivot

        # 消去其他行的第i列系数
        for j in range(n):
            if i != j and abs(mat[j][i]) > EPS:
                factor = mat[j][i]
                for k in range(i, n + 1):
                    mat[j][k] -= factor * mat[i][k]


def main():
    testCases = int(input())

    for t in range(testCases):
        global n, mod
        mod = int(input())
        s = input().strip()
        n = len(s)

        # 初始化矩阵
        initMatrix(s)

        # 高斯消元求解
        gauss()

        # 输出结果
        result = []
        for i in range(n):
            result.append(str(int(round(mat[i][n]))))
        print(' '.join(result))


if __name__ == "__main__":
    main()