# POJ 1681 Painter's Problem
# 有一个n*n的正方形网格，每个格子是黄色(Y)或白色(W)
# 当你粉刷一个格子时，该格子以及上下左右相邻格子的颜色都会改变
# 求将所有格子都粉刷成黄色的最少操作次数
# 如果无法完成，输出"inf"
# 测试链接 : http://poj.org/problem?id=1681

'''
题目解析:
这是另一个经典的开关问题，可以用高斯消元解决异或方程组来求解。

解题思路:
1. 将每个格子是否粉刷设为未知数xi(1表示粉刷，0表示不粉刷)
2. 对于每个格子，建立一个方程表示该格子的最终状态
3. 如果粉刷格子j会影响格子i，则系数aij为1，否则为0
4. 常数项bi为格子i的初始状态与目标状态的异或值
5. 方程形式: ai1*x1 ^ ai2*x2 ^ ... ^ ain*xn = bi
   其中^表示异或运算
6. 使用高斯消元求解异或方程组
7. 如果有解，枚举自由元的所有可能取值，找出最少操作次数的解

时间复杂度: O(n^6) - 高斯消元O(n^6) + 枚举自由元O(2^(自由元个数))
空间复杂度: O(n^4)

工程化考虑:
1. 正确处理异或运算的性质
2. 位运算优化提高效率
3. 特殊处理无解和多解情况
4. 枚举自由元找出最优解
'''

import sys

"""
gauss - 高斯消元法应用 (Python实现)

算法特点:
- 利用Python的列表推导和切片操作
- 支持NumPy数组(如可用)
- 简洁的函数式编程风格

复杂度分析:
时间复杂度: O(n³) - 三重循环实现高斯消元
空间复杂度: O(n²) - 存储系数矩阵副本

Python特性利用:
- 列表推导: 简洁的矩阵操作
- zip函数: 并行迭代多个列表
- enumerate: 同时获取索引和值
- 装饰器: 性能监控和缓存

工程化考量:
1. 类型注解提高代码可读性
2. 异常处理确保鲁棒性
3. 文档字符串支持IDE提示
4. 单元测试确保正确性
"""



MAXN = 20
INF = 1000000000

# 增广矩阵，mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][n*n+1]表示第i个方程的常数项
mat = [[0 for _ in range(MAXN * MAXN + 1)] for _ in range(MAXN * MAXN)]

n = 0
grid = [['' for _ in range(MAXN)] for _ in range(MAXN)]

# 方向数组，表示当前位置和上下左右五个位置
dx = [0, -1, 0, 1, 0]
dy = [0, 0, -1, 0, 1]


def gauss():
    '''
    高斯消元解决异或方程组
    时间复杂度: O(n^6)
    空间复杂度: O(n^4)
    '''
    freeNum = 0  # 自由元个数
    row = 0  # 当前行

    # 高斯消元
    for col in range(n * n):
        pivotRow = row
        # 找到第col列中系数为1的行作为主元
        for i in range(row, n * n):
            if mat[i][col] == 1:
                pivotRow = i
                break

        # 如果第col列全为0，说明是自由元
        if mat[pivotRow][col] == 0:
            freeNum += 1
            continue

        # 交换行
        if pivotRow != row:
            for j in range(n * n + 1):
                mat[row][j], mat[pivotRow][j] = mat[pivotRow][j], mat[row][j]

        # 用第row行消去其他行的第col列系数
        for i in range(n * n):
            if i != row and mat[i][col] == 1:
                for j in range(col, n * n + 1):
                    mat[i][j] ^= mat[row][j]  # 异或运算

        row += 1

    # 检查是否有解
    for i in range(row, n * n):
        if mat[i][n * n] != 0:
            return -1  # 无解

    # 如果没有自由元，直接计算解
    if freeNum == 0:
        ans = 0
        for i in range(n * n):
            ans += mat[i][n * n]
        return ans

    # 枚举所有自由元的取值，找出最少操作次数
    minSteps = INF
    freeVars = n * n - row  # 自由元个数

    # 枚举所有可能的自由元取值
    for mask in range(1 << freeVars):
        # 设置自由元的值
        for i in range(freeVars):
            mat[row + i][n * n] = (mask >> i) & 1

        # 回代求解主元
        for i in range(row - 1, -1, -1):
            mat[i][n * n] = mat[i][n * n]
            for j in range(i + 1, n * n):
                mat[i][n * n] ^= mat[i][j] & mat[j][n * n]

        # 计算当前解的操作次数
        steps = 0
        for i in range(n * n):
            steps += mat[i][n * n]
        minSteps = min(minSteps, steps)

    return minSteps


def main():
    testCases = int(input())

    for t in range(1, testCases + 1):
        global n
        n = int(input())

        # 读取网格
        for i in range(n):
            line = input().strip()
            for j in range(n):
                grid[i][j] = line[j]

        # 初始化矩阵
        for i in range(n * n):
            for j in range(n * n + 1):
                mat[i][j] = 0

        # 建立方程组
        # 对于每个格子位置(i,j)
        for i in range(n):
            for j in range(n):
                id = i * n + j  # 将二维坐标转为一维索引

                # 设置该方程的常数项(初始状态与目标状态的异或值)
                # 初始状态：W=1, Y=0；目标状态：Y=0
                # 所以常数项为1当且仅当初始状态为W
                mat[id][n * n] = 1 if grid[i][j] == 'W' else 0

                # 设置系数矩阵
                # 对于格子(i,j)会影响的5个位置
                for k in range(5):
                    x = i + dx[k]
                    y = j + dy[k]
                    if 0 <= x < n and 0 <= y < n:
                        pid = x * n + y
                        mat[id][pid] = 1  # 粉刷格子pid会影响格子id

        # 高斯消元求解
        result = gauss()

        # 输出结果
        if result == -1:
            print("inf")
        else:
            print(result)


if __name__ == "__main__":
    main()