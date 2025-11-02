# UVa 11542 Square
# 题目描述：
# 给定n个正整数，每个数的素因子都不超过500，从中选出1个或多个数，
# 使得选出的数的乘积是完全平方数，求有多少种选法。
# 1 <= n <= 100
# 1 <= xi <= 10^15
# 测试链接 : https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2577

# 高斯消元解决异或方程组 - UVa 11542 Square
# 
# 题目解析：
# 本题要求从给定的n个数中选出若干个数，使得它们的乘积是完全平方数。
# 一个数是完全平方数当且仅当它的每个素因子的指数都是偶数。
# 因此，我们需要选择一些数，使得每个素因子在所选数的乘积中的指数都是偶数。
# 
# 解题思路：
# 1. 素因子分解：
#    - 首先筛出500以内的所有素数
#    - 对每个输入的数进行素因子分解，记录每个素因子的指数的奇偶性
# 2. 建立异或方程组：
#    - 每个素数对应一个方程
#    - 每个数对应一个未知数
#    - 系数矩阵A[i][j]表示第j个数中第i个素因子的指数的奇偶性
#    - 常数项为0（因为我们要求所有素因子的指数都是偶数）
# 3. 高斯消元：
#    - 对系数矩阵进行高斯消元
#    - 统计自由元的个数
# 4. 计算方案数：
#    - 方案数为2^(自由元个数) - 1（减1是因为不能一个都不选）
# 
# 时间复杂度：O(n * π(500) + π(500)^3)
# 空间复杂度：O(n * π(500))
# 其中π(500)表示500以内的素数个数，约为95

MAXP = 505  # 素数上限
MAXN = 105  # 数组大小

# 素数相关
isPrime = [True] * MAXP
primes = []
primeCount = 0

# 系数矩阵，mat[i][j]表示第i个素数在第j个数中的指数奇偶性
mat = [[0 for _ in range(MAXN)] for _ in range(MAXP)]

# 输入的数
numbers = [0] * MAXN

def sieve(n):
    """
    线性筛法求素数
    
    算法原理：
    线性筛法是一种高效的素数筛法，每个合数只会被其最小的质因子筛掉一次，
    因此时间复杂度为O(n)。
    
    :param n: 筛法上限
    """
    global isPrime, primes, primeCount
    isPrime = [True] * MAXP
    isPrime[0] = isPrime[1] = False
    primes = []
    primeCount = 0
    
    for i in range(2, n + 1):
        if isPrime[i]:
            primes.append(i)
            primeCount += 1
        j = 0
        while j < primeCount and i * primes[j] <= n:
            isPrime[i * primes[j]] = False
            if i % primes[j] == 0:
                break
            j += 1

def factorize(num, col, n):
    """
    对一个数进行素因子分解，记录每个素因子指数的奇偶性
    
    算法思路：
    1. 遍历所有素数
    2. 对于每个素数，统计它在该数中的出现次数
    3. 记录指数的奇偶性（奇数为1，偶数为0）
    
    :param num: 要分解的数
    :param col: 系数矩阵的列号
    :param n: 素数个数
    """
    for i in range(n):
        cnt = 0
        while num % primes[i] == 0:
            cnt += 1
            num //= primes[i]
        mat[i][col] = cnt % 2  # 记录指数的奇偶性

def gauss(rows, cols):
    """
    高斯消元解决异或方程组
    
    算法步骤：
    1. 构造增广矩阵：将方程组的系数和常数项组成增广矩阵
    2. 消元过程：
       - 从第一行开始，选择主元（该列系数为1的行）
       - 将主元行交换到当前行
       - 用主元行消去其他行的当前列系数（通过异或运算）
    3. 判断解的情况：
       - 唯一解：系数矩阵可化为单位矩阵
       - 无解：出现形如 0 = 1 的矛盾方程
       - 无穷解：出现形如 0 = 0 的自由元方程
    
    :param rows: 方程个数（素数个数）
    :param cols: 未知数个数（输入数的个数）
    :return: 自由元个数
    """
    r = 0  # 当前行
    c = 0  # 当前列

    # 消元过程
    while r < rows and c < cols:
        pivot = r

        # 寻找主元（当前列中系数为1的行）
        for i in range(r, rows):
            if mat[i][c] == 1:
                pivot = i
                break

        # 如果找不到主元，说明当前列全为0，跳到下一列
        if mat[pivot][c] == 0:
            r -= 1  # 保持当前行不变
            c += 1
            continue

        # 交换第r行和第pivot行
        if pivot != r:
            for j in range(c, cols + 1):
                mat[r][j], mat[pivot][j] = mat[pivot][j], mat[r][j]

        # 消去其他行的当前列系数
        for i in range(rows):
            if i != r and mat[i][c] == 1:
                # 第i行异或第r行
                for j in range(c, cols + 1):
                    mat[i][j] ^= mat[r][j]

        r += 1
        c += 1

    # 返回自由元个数
    return cols - r

def power(base, exp):
    """
    快速幂运算
    
    :param base: 底数
    :param exp: 指数
    :return: base^exp
    """
    result = 1
    while exp > 0:
        if exp % 2 == 1:
            result *= base
        base *= base
        exp //= 2
    return result

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    
    # 预处理素数
    sieve(500)
    
    T = int(data[idx])
    idx += 1
    
    for _ in range(T):
        n = int(data[idx])
        idx += 1
        
        # 读取输入数据
        for i in range(n):
            numbers[i] = int(data[idx])
            idx += 1
        
        # 初始化矩阵
        for i in range(primeCount):
            for j in range(n + 1):
                mat[i][j] = 0
        
        # 对每个数进行素因子分解
        for i in range(n):
            factorize(numbers[i], i, primeCount)
        
        # 高斯消元
        free = gauss(primeCount, n)
        
        # 计算方案数：2^(自由元个数) - 1（减1是因为不能一个都不选）
        result = power(2, free) - 1
        print(result)

if __name__ == "__main__":
    main()