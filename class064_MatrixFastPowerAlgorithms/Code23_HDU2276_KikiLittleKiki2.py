"""
杭电OJ 2276 - Kiki & Little Kiki 2
题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=2276
题目大意: 有n个灯泡排成一圈，每个灯泡有亮(1)和灭(0)两种状态。
         每秒，每个灯泡的状态会根据它左边灯泡的状态变化：
         如果左边灯泡是亮的，则当前灯泡状态翻转；否则保持不变。
         给定初始状态和秒数m，求m秒后的状态。
解法: 使用矩阵快速幂求解
时间复杂度: O(n^3 * logm)
空间复杂度: O(n^2)

数学分析:
1. 状态转移可以表示为线性递推关系
2. 设f_i(t)为第i个灯泡在t秒时的状态
3. 递推关系: f_i(t+1) = f_i(t) XOR f_{i-1}(t)
4. 由于是异或操作，可以转换为模2加法: f_i(t+1) = f_i(t) + f_{i-1}(t) mod 2
5. 构建n×n的转移矩阵表示状态转移

优化思路:
1. 使用矩阵快速幂将时间复杂度从O(m*n)降低到O(n^3 * logm)
2. 注意模2运算的特殊性

工程化考虑:
1. 边界条件处理: n=1, m=0的特殊情况
2. 输入验证: 检查n和m的有效性
3. 模运算: 使用模2运算

与其他解法对比:
1. 模拟解法: 时间复杂度O(m*n)，当m较大时会超时
2. 矩阵快速幂: 时间复杂度O(n^3 * logm)
3. 最优性: 当m较大时，矩阵快速幂明显优于模拟解法
"""

MOD = 2  # 模2运算

def build_transition_matrix(n):
    """
    构建转移矩阵
    时间复杂度: O(n^2)
    空间复杂度: O(n^2)
    """
    matrix = [[0] * n for _ in range(n)]
    for i in range(n):
        # 主对角线为1
        matrix[i][i] = 1
        # 左边相邻位置为1
        left = (i - 1 + n) % n
        matrix[i][left] = 1
    return matrix

def matrix_multiply(a, b, n):
    """
    矩阵乘法（模2）
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
    """
    res = [[0] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            for k in range(n):
                res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD
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
    矩阵快速幂（模2）
    时间复杂度: O(n^3 * logm)
    空间复杂度: O(n^2)
    """
    res = identity_matrix(n)
    while exp > 0:
        if exp & 1:
            res = matrix_multiply(res, base, n)
        base = matrix_multiply(base, base, n)
        exp >>= 1
    return res

def multiply_matrix_vector(matrix, vector, n):
    """
    矩阵与向量乘法（模2）
    时间复杂度: O(n^2)
    空间复杂度: O(n)
    """
    res = [0] * n
    for i in range(n):
        for j in range(n):
            res[i] = (res[i] + matrix[i][j] * vector[j]) % MOD
    return res

def main():
    import sys
    for line in sys.stdin:
        if not line.strip():
            continue
        
        data = line.split()
        if len(data) < 2:
            continue
            
        m = int(data[0])
        state = data[1]
        n = len(state)
        
        # 特殊情况处理
        if m == 0:
            print(state)
            continue
        
        # 构建初始状态向量
        initial = [int(c) for c in state]
        
        # 构建转移矩阵
        transition = build_transition_matrix(n)
        
        # 计算转移矩阵的m次幂
        result_matrix = matrix_power(transition, m, n)
        
        # 计算最终状态
        final_state = multiply_matrix_vector(result_matrix, initial, n)
        
        # 输出结果
        result_str = ''.join(str(x) for x in final_state)
        print(result_str)

if __name__ == "__main__":
    main()