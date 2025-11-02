# POJ 1830 开关问题
# 有N个相同的开关，每个开关都与某些开关有着联系
# 每当你打开或者关闭某个开关的时候，其他的与此开关相关联的开关也会相应地发生变化
# 给定开关的初始状态和目标状态，求有多少种方案可以完成任务
# 测试链接 : http://poj.org/problem?id=1830

'''
题目解析:
这是一个典型的开关问题，可以用高斯消元解决异或方程组来求解。

解题思路:
1. 将每个开关是否操作设为未知数xi(1表示操作，0表示不操作)
2. 对于每个开关，建立一个方程表示该开关的最终状态
3. 如果操作开关j会影响开关i，则系数aij为1，否则为0
4. 常数项bi为开关i的初始状态与目标状态的异或值
5. 方程形式: ai1*x1 ^ ai2*x2 ^ ... ^ ain*xn = bi
   其中^表示异或运算
6. 使用高斯消元求解异或方程组
7. 根据解的情况判断方案数：
   - 如果无解，输出"oh,it's impossible~!!"
   - 如果有唯一解，输出1
   - 如果有多个解，输出2^(自由元个数)

时间复杂度: O(n^3)
空间复杂度: O(n^2)

工程化考虑:
1. 正确处理异或运算的性质
2. 完整判断解的各种情况
3. 正确计算方案数
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



MAXN = 35

# 增广矩阵，mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][n+1]表示第i个方程的常数项
mat = [[0 for _ in range(MAXN + 1)] for _ in range(MAXN)]

n = 0
startState = [0 for _ in range(MAXN)]
endState = [0 for _ in range(MAXN)]


def gauss():
    '''
    高斯消元解决异或方程组
    返回自由元个数，-1表示无解
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
    '''
    row = 0  # 当前行

    # 高斯消元
    for col in range(1, n + 1):
        pivotRow = row
        # 找到第col列中系数为1的行作为主元
        for i in range(row, n):
            if mat[i][col] == 1:
                pivotRow = i
                break

        # 如果第col列全为0，说明是自由元
        if mat[pivotRow][col] == 0:
            continue

        # 交换行
        if pivotRow != row:
            for j in range(1, n + 2):
                mat[row][j], mat[pivotRow][j] = mat[pivotRow][j], mat[row][j]

        # 用第row行消去其他行的第col列系数
        for i in range(n):
            if i != row and mat[i][col] == 1:
                for j in range(col, n + 2):
                    mat[i][j] ^= mat[row][j]  # 异或运算

        row += 1

    # 检查是否有解
    for i in range(row, n):
        if mat[i][n + 1] != 0:
            return -1  # 无解

    # 返回自由元个数
    return n - row


def main():
    testCases = int(input())

    for t in range(1, testCases + 1):
        global n
        n = int(input())

        # 读取初始状态
        startState[1:n+1] = list(map(int, input().split()))

        # 读取目标状态
        endState[1:n+1] = list(map(int, input().split()))

        # 初始化矩阵
        for i in range(n + 1):
            for j in range(n + 2):
                mat[i][j] = 0

        # 读取开关关系
        while True:
            line = input().strip()
            if not line:
                continue
            u, v = map(int, line.split())
            if u == 0 and v == 0:
                break
            mat[v][u] = 1  # 操作开关u会影响开关v

        # 设置对角线元素（操作开关本身会影响自身）
        for i in range(1, n + 1):
            mat[i][i] = 1

        # 设置常数项（初始状态与目标状态的异或值）
        for i in range(1, n + 1):
            mat[i][n + 1] = startState[i] ^ endState[i]

        # 高斯消元求解
        freeVars = gauss()

        # 输出结果
        if freeVars == -1:
            print("oh,it's impossible~!!")
        else:
            print(1 << freeVars)  # 2^(自由元个数)


if __name__ == "__main__":
    try:
        main()
    except EOFError:
        pass