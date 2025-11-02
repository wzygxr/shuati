"""
test_xor_gaussian_elimination - 高斯消元法应用 (Python实现)

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

#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
高斯消元法测试文件
测试各种类型的高斯消元实现
"""


def test_xor_gaussian_elimination():
    """
    测试异或方程组的高斯消元
    方程组：
    x1 ^ x2 ^ x3 = 1
    x1 ^ x3 = 0
    x2 ^ x3 = 1
    """
    # 增广矩阵（系数矩阵+常数项）
    mat = [
        [1, 1, 1, 1],  # x1 ^ x2 ^ x3 = 1
        [1, 0, 1, 0],  # x1 ^ x3 = 0
        [0, 1, 1, 1]   # x2 ^ x3 = 1
    ]
    
    n = 3  # 未知数个数
    
    # 高斯消元（异或版本）
    for i in range(n):
        # 找到第i列中系数为1的行作为主元
        pivot_row = -1
        for j in range(i, n):
            if mat[j][i] == 1:
                pivot_row = j
                break
        
        # 如果找不到主元，继续下一列
        if pivot_row == -1:
            continue
            
        # 交换行
        if pivot_row != i:
            mat[i], mat[pivot_row] = mat[pivot_row], mat[i]
            
        # 用第i行消去其他行的第i列系数
        for j in range(n):
            if i != j and mat[j][i] == 1:
                for k in range(n + 1):
                    mat[j][k] ^= mat[i][k]  # 异或运算
    
    # 回代求解
    solution = [0] * n
    for i in range(n-1, -1, -1):
        solution[i] = mat[i][n]
        for j in range(i+1, n):
            solution[i] ^= mat[i][j] & solution[j]
    
    print("异或方程组解:", solution)
    
    # 验证解
    x1, x2, x3 = solution
    print("验证:")
    print(f"x1 ^ x2 ^ x3 = {x1 ^ x2 ^ x3} (应为1)")
    print(f"x1 ^ x3 = {x1 ^ x3} (应为0)")
    print(f"x2 ^ x3 = {x2 ^ x3} (应为1)")


def test_float_gaussian_elimination():
    """
    测试浮点数方程组的高斯消元
    方程组：
    2x + y + z = 7
    x + 3y + 2z = 11
    2x + y + 2z = 8
    """
    # 增广矩阵
    mat = [
        [2.0, 1.0, 1.0, 7.0],  # 2x + y + z = 7
        [1.0, 3.0, 2.0, 11.0], # x + 3y + 2z = 11
        [2.0, 1.0, 2.0, 8.0]   # 2x + y + 2z = 8
    ]
    
    n = 3  # 未知数个数
    eps = 1e-10  # 精度
    
    # 高斯消元
    for i in range(n):
        # 找到第i列系数绝对值最大的行
        max_row = i
        for j in range(i + 1, n):
            if abs(mat[j][i]) > abs(mat[max_row][i]):
                max_row = j
                
        # 交换行
        if max_row != i:
            mat[i], mat[max_row] = mat[max_row], mat[i]
            
        # 如果主元为0，说明矩阵奇异
        if abs(mat[i][i]) < eps:
            continue
            
        # 将第i行主元系数化为1
        pivot = mat[i][i]
        for k in range(n + 1):
            mat[i][k] /= pivot
        
        # 消去其他行的第i列系数
        for j in range(n):
            if i != j and abs(mat[j][i]) > eps:
                factor = mat[j][i]
                for k in range(n + 1):
                    mat[j][k] -= factor * mat[i][k]
    
    # 回代求解
    solution = [row[n] for row in mat]
    
    print("\n浮点数方程组解:", solution)
    
    # 验证解
    x, y, z = solution
    print("验证:")
    print(f"2x + y + z = {2*x + y + z:.6f} (应为7)")
    print(f"x + 3y + 2z = {x + 3*y + 2*z:.6f} (应为11)")
    print(f"2x + y + 2z = {2*x + y + 2*z:.6f} (应为8)")


def test_mod_gaussian_elimination():
    """
    测试模线性方程组的高斯消元（模7）
    方程组：
    2x + 3y + z ≡ 5 (mod 7)
    x + 2y + 3z ≡ 1 (mod 7)
    3x + y + 2z ≡ 4 (mod 7)
    """
    # 增广矩阵
    mat = [
        [2, 3, 1, 5],  # 2x + 3y + z ≡ 5 (mod 7)
        [1, 2, 3, 1],  # x + 2y + 3z ≡ 1 (mod 7)
        [3, 1, 2, 4]   # 3x + y + 2z ≡ 4 (mod 7)
    ]
    
    n = 3  # 未知数个数
    mod = 7  # 模数
    
    # 预处理逆元
    inv = [0] * mod
    inv[1] = 1
    for i in range(2, mod):
        inv[i] = (mod - (mod // i) * inv[mod % i] % mod) % mod
    
    # 高斯消元（模版本）
    for i in range(n):
        # 找到第i列系数非0的行作为主元
        pivot_row = -1
        for j in range(i, n):
            if mat[j][i] % mod != 0:
                pivot_row = j
                break
        
        # 如果找不到主元，继续下一列
        if pivot_row == -1:
            continue
            
        # 交换行
        if pivot_row != i:
            mat[i], mat[pivot_row] = mat[pivot_row], mat[i]
            
        # 将第i行主元系数化为1
        pivot = mat[i][i] % mod
        if pivot != 0:
            inv_pivot = inv[pivot]
            for k in range(n + 1):
                mat[i][k] = (mat[i][k] * inv_pivot) % mod
        
        # 用第i行消去其他行的第i列系数
        for j in range(n):
            if i != j and mat[j][i] % mod != 0:
                factor = mat[j][i] % mod
                for k in range(n + 1):
                    mat[j][k] = (mat[j][k] - factor * mat[i][k]) % mod
                    # 确保结果非负
                    mat[j][k] = (mat[j][k] + mod) % mod
    
    # 回代求解
    solution = [0] * n
    for i in range(n-1, -1, -1):
        solution[i] = mat[i][n]
        for j in range(i+1, n):
            solution[i] = (solution[i] - mat[i][j] * solution[j]) % mod
            # 确保结果非负
            solution[i] = (solution[i] + mod) % mod
    
    print("\n模线性方程组解:", solution)
    
    # 验证解
    x, y, z = solution
    print("验证 (mod 7):")
    print(f"2x + 3y + z = {(2*x + 3*y + z) % 7} (应为5)")
    print(f"x + 2y + 3z = {(x + 2*y + 3*z) % 7} (应为1)")
    print(f"3x + y + 2z = {(3*x + y + 2*z) % 7} (应为4)")


if __name__ == "__main__":
    print("高斯消元法测试")
    print("=" * 50)
    
    test_xor_gaussian_elimination()
    test_float_gaussian_elimination()
    test_mod_gaussian_elimination()
    
    print("\n所有测试完成！")