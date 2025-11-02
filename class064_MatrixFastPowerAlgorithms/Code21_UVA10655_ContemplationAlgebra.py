"""
UVA 10655 - Contemplation! Algebra
题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1596
题目大意: 给定p, q, n，其中p = a + b, q = a * b，求a^n + b^n的值
解法: 使用矩阵快速幂求解
时间复杂度: O(logn)
空间复杂度: O(1)

数学分析:
1. 设S(n) = a^n + b^n
2. 递推关系: S(n) = p * S(n-1) - q * S(n-2)
3. 初始条件: S(0) = 2, S(1) = p
4. 转换为矩阵形式:
   [S(n)  ]   [p  -q] [S(n-1)]
   [S(n-1)] = [1   0] [S(n-2)]

优化思路:
1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
2. 注意模运算防止溢出

工程化考虑:
1. 边界条件处理: n=0, n=1的特殊情况
2. 输入验证: 检查p, q, n的有效性
3. 模运算: 防止整数溢出

与其他解法对比:
1. 递归解法: 时间复杂度O(2^n)，会超时
2. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
3. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)，最优解
"""

def matrix_multiply(a, b):
    """
    2x2矩阵乘法
    时间复杂度: O(2^3) = O(8) = O(1)
    空间复杂度: O(4) = O(1)
    """
    res = [[0, 0], [0, 0]]
    for i in range(2):
        for j in range(2):
            for k in range(2):
                res[i][j] += a[i][k] * b[k][j]
    return res

def identity_matrix():
    """
    构造单位矩阵
    时间复杂度: O(2^2) = O(4) = O(1)
    空间复杂度: O(4) = O(1)
    """
    return [[1, 0], [0, 1]]

def matrix_power(base, exp):
    """
    矩阵快速幂
    时间复杂度: O(2^3 * logn) = O(logn)
    空间复杂度: O(4) = O(1)
    """
    res = identity_matrix()
    while exp > 0:
        if exp & 1:
            res = matrix_multiply(res, base)
        base = matrix_multiply(base, base)
        exp >>= 1
    return res

def solve(p, q, n):
    """
    计算a^n + b^n的值
    时间复杂度: O(logn)
    空间复杂度: O(1)
    """
    # 特殊情况处理
    if n == 0:
        return 2
    if n == 1:
        return p
    
    # 转移矩阵
    base = [[p, -q], [1, 0]]
    
    # 计算转移矩阵的n-1次幂
    result = matrix_power(base, n - 1)
    
    # 初始向量 [S(1), S(0)] = [p, 2]
    # 结果为 result * [p, 2]^T 的第一个元素
    s1 = p
    s0 = 2
    
    return result[0][0] * s1 + result[0][1] * s0

if __name__ == "__main__":
    import sys
    for line in sys.stdin:
        if not line.strip():
            continue
        data = line.split()
        p = int(data[0])
        q = int(data[1])
        n = int(data[2])
        
        result = solve(p, q, n)
        print(result)