"""
矩阵运算类

算法简介:
实现矩阵的基本运算，包括加法、减法、乘法、转置、求逆、行列式计算等。
同时实现高斯消元法和异或方程组求解。

适用场景:
1. 线性方程组求解
2. 图论问题（邻接矩阵幂运算）
3. 递推关系求解
4. 自动机状态转移

核心思想:
1. 矩阵乘法结合律优化递推计算
2. 高斯消元法求解线性方程组
3. 异或方程组的特殊求解方法
4. 矩阵快速幂优化递推关系

时间复杂度: 
- 矩阵乘法: O(n^3)
- 矩阵快速幂: O(n^3 log k)
- 高斯消元: O(n^3)
空间复杂度: O(n^2)
"""

MOD = 1000000007

def multiply(a, b):
    """
    矩阵乘法
    :param a: 第一个矩阵
    :param b: 第二个矩阵
    :return: 乘积矩阵
    """
    n = len(a)
    m = len(b[0])
    p = len(b)
    
    result = [[0] * m for _ in range(n)]
    
    for i in range(n):
        for j in range(m):
            for k in range(p):
                result[i][j] = (result[i][j] + a[i][k] * b[k][j]) % MOD
    
    return result

def matrix_pow(base, exp):
    """
    矩阵快速幂
    :param base: 底数矩阵
    :param exp: 指数
    :return: 幂运算结果
    """
    n = len(base)
    result = [[0] * n for _ in range(n)]
    
    # 初始化为单位矩阵
    for i in range(n):
        result[i][i] = 1
    
    while exp > 0:
        if exp & 1:
            result = multiply(result, base)
        base = multiply(base, base)
        exp >>= 1
    
    return result

def gaussian_elimination(a):
    """
    高斯消元法求解线性方程组
    :param a: 增广矩阵
    :return: 方程组的解，无解返回None
    """
    n = len(a)
    m = len(a[0]) - 1
    
    for i in range(min(n, m)):
        # 选择主元
        pivot = i
        for j in range(i + 1, n):
            if abs(a[j][i]) > abs(a[pivot][i]):
                pivot = j
        
        # 交换行
        if pivot != i:
            a[i], a[pivot] = a[pivot], a[i]
        
        # 消元
        if a[i][i] == 0:
            continue
        
        for j in range(i + 1, n):
            factor = a[j][i] * mod_inverse(a[i][i], MOD) % MOD
            for k in range(i, m + 1):
                a[j][k] = (a[j][k] - factor * a[i][k] % MOD + MOD) % MOD
    
    # 回代求解
    result = [0] * m
    for i in range(min(n, m) - 1, -1, -1):
        sum_val = 0
        for j in range(i + 1, m):
            sum_val = (sum_val + a[i][j] * result[j]) % MOD
        
        if a[i][i] == 0:
            if a[i][m] != 0:
                return None  # 无解
            result[i] = 0
        else:
            result[i] = (a[i][m] - sum_val + MOD) % MOD * mod_inverse(a[i][i], MOD) % MOD
    
    return result

def xor_gaussian_elimination(a):
    """
    异或方程组求解（XOR Gaussian Elimination）
    :param a: 系数矩阵（01矩阵）
    :return: 方程组的解，无解返回None
    """
    n = len(a)
    m = len(a[0]) - 1
    
    for i in range(min(n, m)):
        # 选择主元
        pivot = i
        for j in range(i + 1, n):
            if a[j][i] > a[pivot][i]:
                pivot = j
        
        # 交换行
        if pivot != i:
            a[i], a[pivot] = a[pivot], a[i]
        
        # 消元
        if a[i][i] == 0:
            continue
        
        for j in range(i + 1, n):
            if a[j][i] == 1:
                for k in range(i, m + 1):
                    a[j][k] ^= a[i][k]
    
    # 回代求解
    result = [0] * m
    for i in range(min(n, m) - 1, -1, -1):
        sum_val = 0
        for j in range(i + 1, m):
            sum_val ^= (a[i][j] & result[j])
        
        if a[i][i] == 0:
            if a[i][m] != sum_val:
                return None  # 无解
            result[i] = 0
        else:
            result[i] = a[i][m] ^ sum_val
    
    return result

def determinant(a):
    """
    计算矩阵的行列式
    :param a: 方阵
    :return: 行列式值
    """
    n = len(a)
    temp = [[0] * n for _ in range(n)]
    
    # 复制矩阵
    for i in range(n):
        for j in range(n):
            temp[i][j] = a[i][j]
    
    result = 1
    for i in range(n):
        # 选择主元
        pivot = i
        for j in range(i + 1, n):
            if abs(temp[j][i]) > abs(temp[pivot][i]):
                pivot = j
        
        # 交换行
        if pivot != i:
            temp[i], temp[pivot] = temp[pivot], temp[i]
            result = -result
        
        if temp[i][i] == 0:
            return 0
        
        result = result * temp[i][i] % MOD
        
        # 消元
        for j in range(i + 1, n):
            factor = temp[j][i] * mod_inverse(temp[i][i], MOD) % MOD
            for k in range(i, n):
                temp[j][k] = (temp[j][k] - factor * temp[i][k] % MOD + MOD) % MOD
    
    return (result + MOD) % MOD

def transpose(a):
    """
    矩阵转置
    :param a: 矩阵
    :return: 转置矩阵
    """
    n = len(a)
    m = len(a[0])
    result = [[0] * n for _ in range(m)]
    
    for i in range(n):
        for j in range(m):
            result[j][i] = a[i][j]
    
    return result

def pow_mod(base, exp, mod):
    """
    快速幂运算
    """
    result = 1
    base %= mod
    while exp > 0:
        if exp & 1:
            result = result * base % mod
        base = base * base % mod
        exp >>= 1
    return result

def mod_inverse(a, mod):
    """
    模逆元
    """
    return pow_mod(a, mod - 2, mod)

def solve_p3390(n, k, a):
    """
    洛谷P3390 【模板】矩阵快速幂
    题目来源: https://www.luogu.com.cn/problem/P3390
    题目描述: 给定n×n的矩阵A，求A^k。
    解题思路: 直接使用矩阵快速幂算法
    时间复杂度: O(n^3 log k)
    空间复杂度: O(n^2)
    
    :param n: 矩阵大小
    :param k: 指数
    :param a: 矩阵A
    :return: A^k
    """
    return matrix_pow(a, k)

# 测试用例
if __name__ == "__main__":
    # 测试矩阵乘法
    a = [[1, 2], [3, 4]]
    b = [[5, 6], [7, 8]]
    result = multiply(a, b)
    print("Matrix multiplication result:")
    for i in range(len(result)):
        for j in range(len(result[0])):
            print(result[i][j], end=" ")
        print()
    
    # 测试洛谷P3390题目
    n1 = 2
    k1 = 3
    a1 = [[1, 2], [3, 4]]
    result1 = solve_p3390(n1, k1, a1)
    print("P3390 result:")
    for i in range(len(result1)):
        for j in range(len(result1[0])):
            print(result1[i][j], end=" ")
        print()
    
    # 边界情况测试
    # 测试单位矩阵
    n2, k2 = 2, 5
    identity = [[1, 0], [0, 1]]
    result2 = solve_p3390(n2, k2, identity)
    print("Boundary test 1 - identity matrix to power 5:")
    for i in range(len(result2)):
        for j in range(len(result2[0])):
            print(result2[i][j], end=" ")
        print()
    
    # 测试零矩阵
    zero = [[0, 0], [0, 0]]
    result3 = solve_p3390(n2, k2, zero)
    print("Boundary test 2 - zero matrix to power 5:")
    for i in range(len(result3)):
        for j in range(len(result3[0])):
            print(result3[i][j], end=" ")
        print()
    
    # 测试1x1矩阵
    n3, k3 = 1, 10
    single = [[3]]
    result4 = solve_p3390(n3, k3, single)
    print("Boundary test 3 - 1x1 matrix to power 10:", result4[0][0])