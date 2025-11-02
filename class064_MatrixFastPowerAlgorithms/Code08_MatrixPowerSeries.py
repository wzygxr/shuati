# POJ 3233 Matrix Power Series
# 题目链接: http://poj.org/problem?id=3233
# 题目大意: 给定一个n×n的矩阵A和正整数k，求S = A + A^2 + A^3 + ... + A^k
# 解法: 使用矩阵快速幂和分治法求解
# 时间复杂度: O(n^3 * logk)
# 空间复杂度: O(n^2)

def matrix_add(a, b, mod):
    """矩阵加法"""
    rows = len(a)
    cols = len(a[0])
    result = [[0] * cols for _ in range(rows)]
    for i in range(rows):
        for j in range(cols):
            result[i][j] = (a[i][j] + b[i][j]) % mod
    return result

def matrix_multiply(a, b, mod):
    """矩阵乘法"""
    rows_a, cols_a = len(a), len(a[0])
    rows_b, cols_b = len(b), len(b[0])
    result = [[0] * cols_b for _ in range(rows_a)]
    for i in range(rows_a):
        for j in range(cols_b):
            for k_idx in range(cols_a):
                result[i][j] = (result[i][j] + a[i][k_idx] * b[k_idx][j]) % mod
    return result

def identity_matrix(size):
    """构造单位矩阵"""
    result = [[0] * size for _ in range(size)]
    for i in range(size):
        result[i][i] = 1
    return result

def matrix_power(base, exp, mod):
    """矩阵快速幂"""
    size = len(base)
    result = identity_matrix(size)
    while exp > 0:
        if exp & 1:
            result = matrix_multiply(result, base, mod)
        base = matrix_multiply(base, base, mod)
        exp >>= 1
    return result

def matrix_power_series(base, exp, mod):
    """矩阵幂级数求和 - 分治法"""
    if exp == 1:
        return base
    
    if exp & 1:
        # S(k) = S(k-1) + A^k
        sub = matrix_power_series(base, exp - 1, mod)
        power = matrix_power(base, exp, mod)
        return matrix_add(sub, power, mod)
    else:
        # S(k) = (A^(k/2) + I) * S(k/2)
        half = exp >> 1
        sub = matrix_power_series(base, half, mod)
        power = matrix_power(base, half, mod)
        identity = identity_matrix(len(base))
        factor = matrix_add(power, identity, mod)
        return matrix_multiply(factor, sub, mod)

def print_matrix(matrix):
    """打印矩阵"""
    for row in matrix:
        print(' '.join(map(str, row)))

# 主程序
if __name__ == "__main__":
    # 读取输入
    n, k, mod = map(int, input().split())
    A = []
    for i in range(n):
        row = list(map(int, input().split()))
        A.append([x % mod for x in row])
    
    # 计算结果并输出
    result = matrix_power_series(A, k, mod)
    print_matrix(result)