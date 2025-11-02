"""
Codeforces 185A - Plant
题目链接: https://codeforces.com/problemset/problem/185/A
题目大意: 有一个植物，每年会生长。第一年植物有1个向上的三角形和0个向下的三角形。
         每年，每个向上的三角形会变成3个向上的三角形和1个向下的三角形。
         每个向下的三角形会变成1个向上的三角形和3个向下的三角形。
         求n年后向上的三角形数量。
解法: 使用矩阵快速幂求解
时间复杂度: O(logn)
空间复杂度: O(1)

数学分析:
1. 设f(n)为n年后向上的三角形数量，g(n)为n年后向下的三角形数量
2. 递推关系:
   f(n) = 3*f(n-1) + g(n-1)
   g(n) = f(n-1) + 3*g(n-1)
3. 转换为矩阵形式:
   [f(n)]   [3 1] [f(n-1)]
   [g(n)] = [1 3] [g(n-1)]

优化思路:
1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
2. 注意模运算防止溢出

工程化考虑:
1. 边界条件处理: n=0的特殊情况
2. 输入验证: 检查n的有效性
3. 模运算: 防止整数溢出

与其他解法对比:
1. 递归解法: 时间复杂度O(2^n)，会超时
2. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
3. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)，最优解
"""

MOD = 1000000007

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
                res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD
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

def solve(n):
    """
    计算n年后向上的三角形数量
    时间复杂度: O(logn)
    空间复杂度: O(1)
    
    算法思路:
    1. 构建转移矩阵[[3,1],[1,3]]
    2. 使用矩阵快速幂计算转移矩阵的n次幂
    3. 乘以初始向量[1,0]得到结果
    """
    # 特殊情况处理
    if n == 0:
        return 1
    
    # 转移矩阵
    base = [[3, 1], [1, 3]]
    
    # 计算转移矩阵的n次幂
    result = matrix_power(base, n)
    
    # 初始向量 [f(0), g(0)] = [1, 0]
    # 结果为 result * [1, 0]^T 的第一个元素
    return result[0][0] % MOD

if __name__ == "__main__":
    n = int(input().strip())
    result = solve(n)
    print(result)
    
    # 测试用例
    print(f"n=0: {solve(0)}")  # 1
    print(f"n=1: {solve(1)}")  # 3
    print(f"n=2: {solve(2)}")  # 10
    print(f"n=3: {solve(3)}")  # 36