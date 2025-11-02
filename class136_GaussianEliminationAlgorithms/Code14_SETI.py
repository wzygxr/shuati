# POJ 2065 SETI
# 题目链接：http://poj.org/problem?id=2065
# 题目大意：根据给定的模数p和字符串，建立模线性方程组求解多项式系数
# 字符串中每个字符代表方程的常数项，求多项式的系数
#
# 解题思路：
# 1. 根据题目描述建立模线性方程组
# 2. 对于字符串中的每个字符，建立一个方程
# 3. 方程的形式为：a1*x1 + a2*x2 + ... + an*xn ≡ b (mod p)
# 4. 通过高斯消元法求解模线性方程组

import sys

MAXN = 80

# 增广矩阵，用于高斯消元求解模线性方程组
# mat[i][j] 表示第i个方程中第j个变量的系数
# mat[i][n+1] 表示第i个方程的常数项
mat = [[0 for _ in range(MAXN)] for _ in range(MAXN)]

# 结果数组，result[i] 表示第i个变量的值
result = [0 for _ in range(MAXN)]

# 模数和变量数量
p = 0
n = 0


def exgcd(a, b):
    """
    扩展欧几里得算法
    求解 ax + by = gcd(a, b) 的整数解
    :param a: 系数a
    :param b: 系数b
    :return: 包含gcd和解的数组，[0]为gcd，[1]为x，[2]为y
    """
    if b == 0:
        return [a, 1, 0]  # gcd, x, y
    res = exgcd(b, a % b)
    gcd, x, y = res[0], res[2], res[1] - (a // b) * res[2]
    return [gcd, x, y]


def mod_linear_equation(a, b, n):
    """
    求解模线性方程 ax ≡ b (mod n)
    :param a: 系数a
    :param b: 等式右边
    :param n: 模数
    :return: 解，无解返回-1
    """
    res = exgcd(a, n)
    gcd, x = res[0], res[1]
    
    # 如果b不能被gcd整除，则无解
    if b % gcd != 0:
        return -1  # 无解
    
    # 计算解
    mod = n // gcd
    sol = ((x * (b // gcd)) % mod + mod) % mod
    return sol


def power(base, exp, mod):
    """
    快速幂运算
    :param base: 底数
    :param exp: 指数
    :param mod: 模数
    :return: (base^exp) % mod
    """
    result = 1
    base %= mod
    while exp > 0:
        if exp % 2 == 1:
            result = (result * base) % mod
        base = (base * base) % mod
        exp //= 2
    return result


def gcd(a, b):
    """求两个数的最大公约数"""
    return a if b == 0 else gcd(b, a % b)


def gauss():
    """
    高斯消元法求解模线性方程组
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
    
    数学原理：
    模线性方程组形式：
    a11*x1 + a12*x2 + ... + a1n*xn ≡ b1 (mod p)
    a21*x1 + a22*x2 + ... + a2n*xn ≡ b2 (mod p)
    ...
    an1*x1 + an2*x2 + ... + ann*xn ≡ bn (mod p)
    
    其中：
    - xi 表示多项式的系数
    - aij 表示j^i mod p
    - bi 表示字符串中第i个字符对应的数值(*=0, a=1, b=2, ..., z=26)
    
    算法步骤：
    1. 对于每一列col，找到一个行pivot_row使得mat[pivot_row][col] != 0
    2. 将该行与第col行交换
    3. 用第col行消除其他所有行的第col列系数
    4. 回代求解
    
    :return: 是否有解
    """
    # 对每一列进行处理
    for col in range(1, n + 1):
        # 寻找第col列中系数不为0的行，将其交换到第col行
        pivot_row = col
        for i in range(col, n + 1):
            if mat[i][col] != 0:
                pivot_row = i
                break
        
        # 如果找不到系数不为0的行，继续处理下一列
        if mat[pivot_row][col] == 0:
            continue
        
        # 将找到的行与第col行交换
        if pivot_row != col:
            for j in range(1, n + 2):
                mat[col][j], mat[pivot_row][j] = mat[pivot_row][j], mat[col][j]
        
        # 用第col行消除其他行的第col列系数
        for i in range(1, n + 1):
            if i != col and mat[i][col] != 0:
                # 计算最小公倍数
                lcm_val = mat[col][col] * mat[i][col] // gcd(abs(mat[col][col]), abs(mat[i][col]))
                rate1 = lcm_val // mat[col][col]
                rate2 = lcm_val // mat[i][col]
                
                # 消元操作
                for j in range(1, n + 2):
                    mat[i][j] = (mat[i][j] * rate2 - mat[col][j] * rate1) % p
                    # 确保结果为正数
                    if mat[i][j] < 0:
                        mat[i][j] += p
    
    # 回代求解
    for i in range(n, 0, -1):
        sum_val = mat[i][n + 1]
        # 计算已知变量对当前方程的贡献
        for j in range(i + 1, n + 1):
            sum_val = (sum_val - mat[i][j] * result[j] % p + p) % p
        
        # 求解 mat[i][i] * result[i] ≡ sum (mod p)
        sol = mod_linear_equation(mat[i][i], sum_val, p)
        if sol == -1:
            return False  # 无解
        result[i] = sol
    
    return True  # 有解


def main():
    """
    主函数
    读取输入数据，构建系数矩阵，调用高斯消元法求解，输出结果
    
    算法流程：
    1. 读取测试用例数量
    2. 对于每个测试用例：
       a. 读取模数p和字符串
       b. 初始化增广矩阵
       c. 构造系数矩阵和常数项
       d. 使用高斯消元法求解
       e. 输出结果
    """
    global p, n
    
    cases = int(sys.stdin.readline().strip())
    
    for t in range(1, cases + 1):
        line = sys.stdin.readline().strip().split()
        p = int(line[0])
        string = line[1]
        n = len(string)
        
        # 初始化矩阵，将所有元素置为0
        for i in range(1, n + 1):
            for j in range(1, n + 2):
                mat[i][j] = 0
        
        # 构造系数矩阵和常数项
        for i in range(1, n + 1):
            ch = string[i - 1]
            # 将字符转换为数值
            value = 0
            if ch == '*':
                value = 0
            else:
                value = ord(ch) - ord('a') + 1
            mat[i][n + 1] = value  # 设置常数项
            
            # 构造系数矩阵，第i行第j列表示j^i mod p
            for j in range(1, n + 1):
                mat[i][j] = power(j, i - 1, p)
        
        # 使用高斯消元法求解模线性方程组
        if gauss():
            # 输出结果
            output = []
            for i in range(1, n + 1):
                output.append(str(result[i]))
            print(" ".join(output))
        
        if t < cases:
            print()


if __name__ == "__main__":
    main()