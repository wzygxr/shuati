"""
LeetCode 935. 骑士拨号器
题目链接: https://leetcode.cn/problems/knight-dialer/
题目大意: 国际象棋中的骑士可以按照"日"字形移动，骑士在电话拨号盘上移动，计算骑士走n步的不同路径数
解法: 使用矩阵快速幂求解
时间复杂度: O(logn)
空间复杂度: O(1)

数学分析:
1. 电话拨号盘布局:
   1 2 3
   4 5 6
   7 8 9
   * 0 #
2. 骑士移动规则: 从每个数字可以移动到特定的其他数字
3. 构建10×10的转移矩阵表示移动可能性

优化思路:
1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
2. 注意模运算防止溢出

工程化考虑:
1. 边界条件处理: n=1的特殊情况
2. 输入验证: 检查n的有效性
3. 模运算: 防止整数溢出

与其他解法对比:
1. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
2. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)
3. 最优性: 当n较大时，矩阵快速幂明显优于动态规划
"""

MOD = 1000000007

def matrix_multiply(a, b):
    """
    矩阵乘法
    时间复杂度: O(10^3) = O(1000) = O(1)
    空间复杂度: O(100) = O(1)
    """
    size = len(a)
    res = [[0] * size for _ in range(size)]
    for i in range(size):
        for j in range(size):
            for k in range(size):
                res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD
    return res

def identity_matrix(size):
    """
    构造单位矩阵
    时间复杂度: O(10^2) = O(100) = O(1)
    空间复杂度: O(100) = O(1)
    """
    res = [[0] * size for _ in range(size)]
    for i in range(size):
        res[i][i] = 1
    return res

def matrix_power(base, exp):
    """
    矩阵快速幂
    时间复杂度: O(10^3 * logn) = O(logn)
    空间复杂度: O(100) = O(1)
    """
    size = len(base)
    res = identity_matrix(size)
    while exp > 0:
        if exp & 1:
            res = matrix_multiply(res, base)
        base = matrix_multiply(base, base)
        exp >>= 1
    return res

def knight_dialer(n):
    """
    计算骑士在拨号盘上走n步的不同路径数
    时间复杂度: O(logn)
    空间复杂度: O(1)
    
    算法思路:
    1. 构建转移矩阵表示骑士移动规则
    2. 使用矩阵快速幂计算转移矩阵的n-1次幂
    3. 结果矩阵的所有元素之和即为答案
    """
    # 特殊情况处理
    if n == 1:
        return 10
    
    # 构建10×10的转移矩阵
    base = [[0] * 10 for _ in range(10)]
    
    # 从0可以移动到4,6
    base[0][4] = 1
    base[0][6] = 1
    
    # 从1可以移动到6,8
    base[1][6] = 1
    base[1][8] = 1
    
    # 从2可以移动到7,9
    base[2][7] = 1
    base[2][9] = 1
    
    # 从3可以移动到4,8
    base[3][4] = 1
    base[3][8] = 1
    
    # 从4可以移动到0,3,9
    base[4][0] = 1
    base[4][3] = 1
    base[4][9] = 1
    
    # 从5不能移动
    # base[5][*] = 0
    
    # 从6可以移动到0,1,7
    base[6][0] = 1
    base[6][1] = 1
    base[6][7] = 1
    
    # 从7可以移动到2,6
    base[7][2] = 1
    base[7][6] = 1
    
    # 从8可以移动到1,3
    base[8][1] = 1
    base[8][3] = 1
    
    # 从9可以移动到2,4
    base[9][2] = 1
    base[9][4] = 1
    
    # 计算转移矩阵的n-1次幂
    result = matrix_power(base, n - 1)
    
    # 计算结果：所有元素之和
    total = 0
    for i in range(10):
        for j in range(10):
            total = (total + result[i][j]) % MOD
    
    return total

if __name__ == "__main__":
    # 测试用例
    print(f"n=1: {knight_dialer(1)}")  # 10
    print(f"n=2: {knight_dialer(2)}")  # 20
    print(f"n=3: {knight_dialer(3)}")  # 46
    print(f"n=4: {knight_dialer(4)}")  # 104
    print(f"n=3131: {knight_dialer(3131)}")  # 136006598