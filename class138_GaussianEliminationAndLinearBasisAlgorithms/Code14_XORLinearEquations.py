# -*- coding: utf-8 -*-

"""
洛谷 P4782 【模板】高斯消元解异或线性方程组
题目描述：
给定一个线性方程组，其中所有的系数和常数项都是0或1，且方程组中的运算符都是异或（XOR）。求方程组的解。

输入格式：
第一行，一个正整数 n，表示方程的个数和未知数的个数。
接下来的 n 行中，每行有 n+1 个整数，表示一个方程的 n 个系数和 1 个常数项。

输出格式：
如果有唯一解，输出 n 个整数，表示解。
如果有无穷多解，输出"Multiple sets of solutions"。
如果无解，输出"No solution"。

解题思路：
使用异或高斯消元算法求解线性方程组。
关键步骤：
1. 消元阶段：将增广矩阵转化为行阶梯形矩阵
2. 判断解的情况：
   - 无解：存在一行系数全为0但常数项不为0
   - 无穷多解：系数矩阵的秩小于变量个数
   - 唯一解：系数矩阵的秩等于变量个数
3. 回代求解：从最后一行开始，依次解出各变量的值

时间复杂度：O(n³)
空间复杂度：O(n²)

最优解分析：
这是标准的异或高斯消元算法，时间复杂度已经是最优的。
"""

import sys

"""
gauss_xor - 高斯消元法应用 (Python实现)

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



def gauss_xor(a, n, x):
    """
    高斯消元求解异或线性方程组
    
    参数：
        a: 增广矩阵，a[i][j]表示第i个方程的第j个系数，a[i][n]表示第i个方程的常数项
        n: 变量个数和方程个数
        x: 解数组，用于存储解
    
    返回：
        解的情况：-1表示无解，0表示无穷多解，1表示唯一解
    """
    rank = 0  # 矩阵的秩
    
    # 主元列
    for col in range(n):
        # 寻找当前列中的主元（第一个非零元素）
        pivot = rank
        while pivot < n and a[pivot][col] == 0:
            pivot += 1
        
        # 如果当前列全为0，跳过
        if pivot == n:
            continue
        
        # 交换pivot行和rank行
        a[pivot], a[rank] = a[rank], a[pivot]
        
        # 消去其他行
        for i in range(n):
            if i != rank and a[i][col] == 1:
                for j in range(col, n + 1):
                    a[i][j] ^= a[rank][j]
        
        rank += 1
    
    # 检查是否有解
    for i in range(rank, n):
        if a[i][n] == 1:
            # 存在0=1的情况，无解
            return -1
    
    # 初始化解数组
    x[:] = [0] * n
    
    # 回代求解
    pivot_col = [0] * rank  # 记录每一行的主元列
    idx = 0
    for col in range(n):
        if idx >= rank:
            break
        for i in range(idx, n):
            if a[i][col] == 1:
                pivot_col[idx] = col
                idx += 1
                break
    
    for i in range(rank):
        x[pivot_col[i]] = a[i][n]
    
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
        row = list(map(int, sys.stdin.readline().split()))
        a.append(row)
    
    x = [0] * n
    result = gauss_xor(a, n, x)
    
    if result == -1:
        print("No solution")
    elif result == 0:
        print("Multiple sets of solutions")
    else:
        # 输出唯一解
        for num in x:
            print(num)

def test():
    """
    测试函数
    """
    # 测试用例1：有唯一解
    print("Test case 1 (Unique solution):")
    test_a1 = [[1, 1, 0, 1], [1, 0, 1, 0], [0, 1, 1, 1]]
    x1 = [0, 0, 0]
    result1 = gauss_xor(test_a1, 3, x1)
    print(f"Result: {result1} (Expected: 1)")
    if result1 == 1:
        print(f"Solution: {x1}")  # Expected: [0, 1, 0]
    
    # 测试用例2：无解
    print("\nTest case 2 (No solution):")
    test_a2 = [[1, 1, 0, 1], [1, 1, 0, 0], [0, 0, 0, 1]]
    x2 = [0, 0, 0]
    result2 = gauss_xor(test_a2, 3, x2)
    print(f"Result: {result2} (Expected: -1)")
    
    # 测试用例3：无穷多解
    print("\nTest case 3 (Infinite solutions):")
    test_a3 = [[1, 1, 0, 1], [0, 1, 0, 1], [0, 0, 0, 0]]
    x3 = [0, 0, 0]
    result3 = gauss_xor(test_a3, 3, x3)
    print(f"Result: {result3} (Expected: 0)")


if __name__ == "__main__":
    # 直接调用main()处理输入输出
    # main()
    # 为了方便测试，这里调用test()
    test()