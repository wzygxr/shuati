"""
HDU 1575 - Tr A
题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1575
题目大意: 给定一个n×n的矩阵A，求A^k的迹（主对角线元素之和）mod 9973
解法: 使用矩阵快速幂求解
时间复杂度: O(n^3 * logk)
空间复杂度: O(n^2)

数学分析:
1. 矩阵的迹定义为矩阵主对角线元素之和
2. 对于矩阵幂A^k，其迹等于A^k的主对角线元素之和
3. 使用矩阵快速幂计算A^k，然后求迹

优化思路:
1. 使用矩阵快速幂将时间复杂度从O(k*n^3)降低到O(n^3 * logk)
2. 注意模运算防止溢出

工程化考虑:
1. 边界条件处理: k=0的特殊情况（单位矩阵的迹为n）
2. 输入验证: 检查矩阵维度和k的有效性
3. 模运算: 防止整数溢出

与其他解法对比:
1. 暴力解法: 直接计算A^k然后求迹，时间复杂度O(k*n^3)
2. 矩阵快速幂: 时间复杂度O(n^3 * logk)
3. 最优性: 当k较大时，矩阵快速幂明显优于暴力解法
"""

MOD = 9973

def matrix_multiply(a, b, n):
    """
    矩阵乘法
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
    """
    res = [[0] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            total = 0
            for c in range(n):
                total = (total + a[i][c] * b[c][j]) % MOD
            res[i][j] = total
    return res

def identity_matrix(n):
    """
    构造单位矩阵
    时间复杂度: O(n^2)
    空间复杂度: O(n^2)
    """
    res = [[0] * n for _ in range(n)]
    for i in range(n):
        res[i][i] = 1
    return res

def matrix_power(base, exp, n):
    """
    矩阵快速幂
    时间复杂度: O(n^3 * logk)
    空间复杂度: O(n^2)
    """
    res = identity_matrix(n)
    temp = base
    temp_exp = exp
    
    while temp_exp > 0:
        if temp_exp & 1:
            res = matrix_multiply(res, temp, n)
        temp = matrix_multiply(temp, temp, n)
        temp_exp >>= 1
    
    return res

def solve(A, n, k):
    """
    计算A^k的迹mod 9973
    时间复杂度: O(n^3 * logk)
    空间复杂度: O(n^2)
    
    算法思路:
    1. 使用矩阵快速幂计算A^k
    2. 计算结果矩阵的迹（主对角线元素之和）
    3. 对结果取模
    """
    # 特殊情况处理: k=0时，A^0是单位矩阵，迹为n
    if k == 0:
        return n % MOD
    
    # 计算A^k
    result = matrix_power(A, k, n)
    
    # 计算迹
    trace = 0
    for i in range(n):
        trace = (trace + result[i][i]) % MOD
    
    return trace

if __name__ == "__main__":
    import sys
    data = sys.stdin.read().split()
    if not data:
        exit(0)
    
    idx = 0
    T = int(data[idx]); idx += 1  # 测试用例数量
    
    for _ in range(T):
        n = int(data[idx]); idx += 1
        k = int(data[idx]); idx += 1
        
        A = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(n):
                A[i][j] = int(data[idx]) % MOD
                idx += 1
        
        result = solve(A, n, k)
        print(result)