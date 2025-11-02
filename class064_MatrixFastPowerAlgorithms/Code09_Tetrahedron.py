# Codeforces 166E Tetrahedron
# 题目链接: https://codeforces.com/problemset/problem/166/E
# 题目大意: 一个四面体有4个顶点A, B, C, D。一只蚂蚁从顶点D开始，
# 每次沿着棱移动到另一个顶点。求经过n步后回到顶点D的方案数。
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

def solve(n):
    """解决问题"""
    # 初始状态：[在D点的方案数, 不在D点的方案数]
    # 初始时在D点，所以是[1, 0]
    start = [1, 0]
    
    # 转移矩阵：
    # 从D点只能到非D点，有3种选择
    # 从非D点可以到D点(1种选择)或非D点(2种选择)
    # [D点方案数]   [0 1] [D点方案数]
    # [非D点方案数] = [3 2] [非D点方案数]
    base = [
        [0, 1],
        [3, 2]
    ]
    
    result_matrix = matrix_power(base, n)
    result = vector_matrix_multiply(start, result_matrix)
    return result[0]

# 主程序
if __name__ == "__main__":
    n = int(input())
    print(solve(n))