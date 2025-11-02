# POJ 1830 开关问题
# 题目描述：
# 有N个相同的开关，每个开关都与某些开关有着联系，每当你打开或者关闭某个开关的时候，
# 其他的与此开关相关联的开关也会相应地发生变化，即这些相联系的开关的状态会改变。
# 给出所有开关的初始状态和目标状态，求有多少种操作方法可以达到目标状态。
# 1 <= N <= 29
# 测试链接 : http://poj.org/problem?id=1830

# 高斯消元解决异或方程组 - POJ 1830 开关问题
# 
# 题目解析：
# 本题是一个典型的开关问题。每个开关有两种状态（0或1），操作一个开关会改变该开关及其相关联开关的状态。
# 目标是从初始状态转换到目标状态，求有多少种操作方法。
# 
# 解题思路：
# 1. 将问题转化为异或方程组：
#    - 设xi表示是否操作第i个开关（1表示操作，0表示不操作）
#    - 对于每个开关i，建立方程：xi ^ sum{ajxj} = (初始状态i ^ 目标状态i)
#    - 其中aj表示操作开关j是否会影响开关i的状态
# 2. 使用高斯消元求解异或方程组
# 3. 根据解的情况判断方案数：
#    - 无解：方案数为0
#    - 唯一解：方案数为1
#    - 无穷解：方案数为2^(自由元个数)
# 
# 时间复杂度：O(N^3)
# 空间复杂度：O(N^2)

MAXN = 35

# 增广矩阵，mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][n+1]表示第i个方程的常数项
mat = [[0 for _ in range(MAXN)] for _ in range(MAXN)]

def gauss(n):
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
    
    :param n: 未知数个数
    :return: 0表示有唯一解，1表示有无穷多解，-1表示无解
    """
    r = 1  # 当前行
    c = 1  # 当前列

    # 消元过程
    while r <= n and c <= n:
        pivot = r

        # 寻找主元（当前列中系数为1的行）
        for i in range(r, n + 1):
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
            for j in range(c, n + 2):
                mat[r][j], mat[pivot][j] = mat[pivot][j], mat[r][j]

        # 消去其他行的当前列系数
        for i in range(1, n + 1):
            if i != r and mat[i][c] == 1:
                # 第i行异或第r行
                for j in range(c, n + 2):
                    mat[i][j] ^= mat[r][j]

        r += 1
        c += 1

    # 判断解的情况
    # 检查是否有形如 0 = 1 的矛盾方程
    for i in range(r, n + 1):
        if mat[i][n + 1] == 1:
            return -1  # 无解

    # 判断是否有自由元（形如 0 = 0 的方程）
    if r <= n:
        return 1  # 有无穷多解

    return 0  # 有唯一解

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
    k = int(data[idx])
    idx += 1
    
    for _ in range(k):
        global mat
        n = int(data[idx])
        idx += 1
        
        # 读取初始状态
        start = [0] * MAXN
        for i in range(1, n + 1):
            start[i] = int(data[idx])
            idx += 1
            
        # 读取目标状态
        end = [0] * MAXN
        for i in range(1, n + 1):
            end[i] = int(data[idx])
            idx += 1
            
        # 初始化矩阵
        for i in range(1, n + 1):
            for j in range(1, n + 2):
                mat[i][j] = 0
            # 自己对自己有影响
            mat[i][i] = 1
            # 常数项为初始状态与目标状态的异或值
            mat[i][n + 1] = start[i] ^ end[i]
            
        # 读取开关关系
        while idx < len(data):
            i = int(data[idx])
            j = int(data[idx + 1])
            idx += 2
            if i == 0 and j == 0:
                break
            # 操作开关j会影响开关i
            mat[i][j] = 1
            
        # 高斯消元
        result = gauss(n)
        
        if result == -1:
            print("Oh,it's impossible~!!")
        elif result == 0:
            print(1)
        else:
            # 计算自由元个数
            free = 0
            for i in range(1, n + 1):
                if mat[i][i] == 0:
                    free += 1
            print(power(2, free))

if __name__ == "__main__":
    main()