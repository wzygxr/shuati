# HDU 5950 Recursive sequence
# 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=5950
# 题目大意: 给定递推式 f[n] = 2*f[n-2] + f[n-1] + n^4，以及f[1]和f[2]的值，求f[n]
# 解法: 使用矩阵快速幂
# 时间复杂度: O(logn)
# 空间复杂度: O(1)

MOD = 1000000007

def matrix_multiply(a, b):
    """矩阵乘法"""
    rows_a, cols_a = len(a), len(a[0])
    rows_b, cols_b = len(b), len(b[0])
    result = [[0] * cols_b for _ in range(rows_a)]
    for i in range(rows_a):
        for j in range(cols_b):
            for k in range(cols_a):
                result[i][j] = (result[i][j] + a[i][k] * b[k][j]) % MOD
    return result

def vector_matrix_multiply(vector, matrix):
    """向量与矩阵相乘"""
    cols = len(matrix[0])
    result = [0] * cols
    for j in range(cols):
        for i in range(len(vector)):
            result[j] = (result[j] + vector[i] * matrix[i][j]) % MOD
    return result

def matrix_power(base, exp):
    """矩阵快速幂"""
    size = len(base)
    result = [[0] * size for _ in range(size)]
    for i in range(size):
        result[i][i] = 1  # 单位矩阵
    
    while exp > 0:
        if exp & 1:
            result = matrix_multiply(result, base)
        base = matrix_multiply(base, base)
        exp >>= 1
    return result

def fast_power(base, exp):
    """快速幂"""
    result = 1
    while exp > 0:
        if exp & 1:
            result = (result * base) % MOD
        base = (base * base) % MOD
        exp >>= 1
    return result

def solve(n, a, b):
    """解决问题"""
    if n == 1:
        return a
    if n == 2:
        return b
    
    # 初始状态: [f(2), f(1), 81, 27, 9, 3, 1]
    start = [b, a, fast_power(3, 4), fast_power(3, 3), fast_power(3, 2), 3, 1]
    
    # 转移矩阵
    base = [
        [1, 2, 1, 4, 6, 4, 1],  # f(n) = f(n-1) + 2*f(n-2) + (n+1)^4
        [1, 0, 0, 0, 0, 0, 0],  # f(n-1)
        [0, 0, 1, 4, 6, 4, 1],  # (n+1)^4 展开
        [0, 0, 0, 1, 3, 3, 1],  # (n+1)^3 展开
        [0, 0, 0, 0, 1, 2, 1],  # (n+1)^2 展开
        [0, 0, 0, 0, 0, 1, 1],  # (n+1)^1 展开
        [0, 0, 0, 0, 0, 0, 1]   # 1
    ]
    
    result_matrix = matrix_power(base, n - 2)
    result = vector_matrix_multiply(start, result_matrix)
    return result[0]

# 主程序
if __name__ == "__main__":
    test_cases = int(input())
    for _ in range(test_cases):
        n, a, b = map(int, input().split())
        print(solve(n, a, b))