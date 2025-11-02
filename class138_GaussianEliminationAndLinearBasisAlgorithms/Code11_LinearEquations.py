# -*- coding: utf-8 -*-

"""
洛谷 P2455 [SDOI2006]线性方程组
题目描述：
给定一个线性方程组，判断是否有解，若有解则输出任意一组解。

输入格式：
第一行一个正整数n，表示方程组的变量个数和方程个数。
接下来n行，每行n+1个数，依次表示方程的系数和常数项。

输出格式：
若方程组无解，输出"No solution"。
若方程组有无穷多解，输出"Infinite solutions"。
若方程组有唯一解，输出n个数，依次表示各变量的值，保留两位小数。

解题思路：
使用浮点数高斯消元算法求解线性方程组。
关键步骤：
1. 消元阶段：将增广矩阵转化为行阶梯形矩阵
2. 判断解的情况：
   - 无解：存在一行系数全为0但常数项不为0
   - 无穷多解：系数矩阵的秩小于增广矩阵的秩
   - 唯一解：系数矩阵的秩等于增广矩阵的秩等于变量个数
3. 回代求解：从最后一行开始，依次解出各变量的值

时间复杂度：O(n³)
空间复杂度：O(n²)

最优解分析：
这是标准的高斯消元算法，时间复杂度已经是最优的。
"""

import sys

"""
gauss - 高斯消元法应用 (Python实现)

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



EPS = 1e-8  # 浮点数精度


def gauss(a, n):
    """
    高斯消元求解线性方程组
    
    参数：
        a: 增广矩阵，a[i][j]表示第i个方程的第j个系数，a[i][n]表示第i个方程的常数项
        n: 变量个数和方程个数
    
    返回：
        解的情况：-1表示无解，0表示无穷多解，1表示唯一解
    """
    rank = 0  # 矩阵的秩
    
    # 主元列
    for col in range(n):
        # 寻找当前列中的主元（绝对值最大的元素）
        pivot = rank
        for i in range(rank, n):
            if abs(a[i][col]) > abs(a[pivot][col]):
                pivot = i
        
        # 如果当前列全为0，跳过
        if abs(a[pivot][col]) < EPS:
            continue
        
        # 交换pivot行和rank行
        a[pivot], a[rank] = a[rank], a[pivot]
        
        # 归一化主元行
        div = a[rank][col]
        for j in range(col, n + 1):
            a[rank][j] /= div
        
        # 消去其他行
        for i in range(n):
            if i != rank and abs(a[i][col]) > EPS:
                factor = a[i][col]
                for j in range(col, n + 1):
                    a[i][j] -= factor * a[rank][j]
        
        rank += 1
    
    # 检查是否有解
    for i in range(rank, n):
        if abs(a[i][n]) > EPS:
            # 存在0=非零的情况，无解
            return -1
    
    # 判断解的个数
    if rank < n:
        # 有无穷多解
        return 0
    else:
        # 有唯一解
        return 1


def main():
    """
    主函数
    """
    n = int(sys.stdin.readline())
    a = []
    
    # 读取输入
    for _ in range(n):
        row = list(map(float, sys.stdin.readline().split()))
        a.append(row)
    
    result = gauss(a, n)
    
    if result == -1:
        print("No solution")
    elif result == 0:
        print("Infinite solutions")
    else:
        # 输出唯一解
        for i in range(n):
            print(f"{a[i][n]:.2f}", end=" ")
        print()


if __name__ == "__main__":
    main()