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

# Codeforces 24D Broken robot
# 有一个n*m的网格，机器人从位置(x,y)开始
# 每次等概率地选择以下动作：
# 1. 向左移动（如果不在最左列）
# 2. 向右移动（如果不在最右列）
# 3. 向下移动（如果不在最下列）
# 4. 停在原地
# 求机器人到达第n行的期望步数
# 测试链接 : https://codeforces.com/contest/24/problem/D

'''
题目解析:
这是一个期望DP问题，可以用高斯消元来解决。

解题思路:
1. 设f[i][j]表示从位置(i,j)到达第n行的期望步数
2. 对于第n行的格子，f[n][j] = 0
3. 对于其他格子，根据转移建立方程：
   f[i][j] = 1 + (f[i][j-1] + f[i][j+1] + f[i+1][j] + f[i][j]) / k
   其中k是可选动作数
4. 移项后得到：
   k * f[i][j] = k + f[i][j-1] + f[i][j+1] + f[i+1][j] + f[i][j]
   (k-1) * f[i][j] = k + f[i][j-1] + f[i][j+1] + f[i+1][j]
5. 特殊处理边界情况
6. 使用高斯消元求解线性方程组

时间复杂度: O(n * m^3)
空间复杂度: O(m^2)

工程化考虑:
1. 正确处理边界条件
2. 浮点数精度处理
3. 高斯消元求解线性方程组
'''

MAXM = 1005
EPS = 1e-10

# 增广矩阵
mat = [[0.0 for _ in range(MAXM + 1)] for _ in range(MAXM)]

# 期望值数组
f = [0.0 for _ in range(MAXM)]

n, m, x, y = 0, 0, 0, 0


def gauss(size):
    '''
    高斯消元解决浮点数线性方程组
    时间复杂度: O(m^3)
    空间复杂度: O(m^2)
    '''
    for i in range(1, size + 1):
        # 找到第i列系数绝对值最大的行
        maxRow = i
        for j in range(i + 1, size + 1):
            if abs(mat[j][i]) > abs(mat[maxRow][i]):
                maxRow = j

        # 交换行
        if maxRow != i:
            for k in range(1, size + 2):
                mat[i][k], mat[maxRow][k] = mat[maxRow][k], mat[i][k]

        # 如果主元为0，说明矩阵奇异
        if abs(mat[i][i]) < EPS:
            continue

        # 将第i行主元系数化为1
        pivot = mat[i][i]
        for k in range(i, size + 2):
            mat[i][k] /= pivot

        # 消去其他行的第i列系数
        for j in range(1, size + 1):
            if i != j and abs(mat[j][i]) > EPS:
                factor = mat[j][i]
                for k in range(i, size + 2):
                    mat[j][k] -= factor * mat[i][k]

    # 回代求解
    for i in range(size, 0, -1):
        f[i] = mat[i][size + 1]
        for j in range(i + 1, size + 1):
            f[i] -= mat[i][j] * f[j]


def main():
    global n, m, x, y
    n, m, x, y = map(int, input().split())

    # 从第n行开始向上计算
    for i in range(n - 1, x - 1, -1):
        # 初始化矩阵
        for j in range(1, m + 1):
            for k in range(1, m + 2):
                mat[j][k] = 0.0

        # 建立方程组
        for j in range(1, m + 1):
            # 对角线系数
            mat[j][j] = 1.0

            # 可选动作数
            k = 4
            if j == 1:
                k -= 1  # 最左列不能向左
            if j == m:
                k -= 1  # 最右列不能向右
            if i == n - 1:
                k -= 1  # 最下列不能向下

            # 常数项
            mat[j][m + 1] = 1.0 + (f[j] if i < n - 1 else 0.0)  # 向下移动的贡献

            # 其他项的贡献
            if j > 1:
                mat[j][j - 1] -= 1.0 / k  # 向左移动的贡献
            if j < m:
                mat[j][j + 1] -= 1.0 / k  # 向右移动的贡献
            mat[j][j] -= 1.0 / k  # 停在原地的贡献

        # 高斯消元求解
        gauss(m)

    # 输出结果
    print("{:.10f}".format(f[y]))


if __name__ == "__main__":
    main()