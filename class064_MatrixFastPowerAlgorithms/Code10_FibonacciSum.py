# SPOJ FIBOSUM - Fibonacci Sum
# 题目链接: https://www.spoj.com/problems/FIBOSUM/
# 题目大意: 给定n和m，计算斐波那契数列第n项到第m项的和
# F(0) = 0, F(1) = 1, F(n) = F(n-1) + F(n-2) for n >= 2
# 解法: 使用矩阵快速幂
# 时间复杂度: O(logm)
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

def fib_sum(n):
    """计算斐波那契数列前n项的和"""
    if n < 0:
        return 0
    if n == 0:
        return 0
    if n == 1:
        return 1
    
    # 初始状态矩阵 [F(1), F(0), S(1)] = [1, 0, 1]
    # 其中S(n)表示前n项斐波那契数列的和
    start = [[1, 0, 1]]
    
    # 转移矩阵
    # [F(n+1)]   [1 1 0] [F(n)  ]
    # [F(n)  ] = [1 0 0] [F(n-1)]
    # [S(n)  ]   [1 1 1] [S(n-1)]
    base = [
        [1, 1, 0],
        [1, 0, 0],
        [1, 1, 1]
    ]
    
    result = matrix_multiply(start, matrix_power(base, n - 1))
    return result[0][2]  # 返回S(n)

def solve(n, m):
    """计算斐波那契数列第n项到第m项的和"""
    return (fib_sum(m) - fib_sum(n - 1) + MOD) % MOD

# 主程序
if __name__ == "__main__":
    test_cases = int(input())
    for _ in range(test_cases):
        n, m = map(int, input().split())
        print(solve(n, m))