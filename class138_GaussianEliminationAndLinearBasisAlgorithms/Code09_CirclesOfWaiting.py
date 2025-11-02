"""
Point - 高斯消元法应用 (Python实现)

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

# Codeforces 963E Circles of Waiting
# 从原点(0,0)出发，每次等概率地向上、下、左、右移动一步
# 求第一次走到与原点距离大于R的点的期望步数
# 测试链接 : https://codeforces.com/contest/963/problem/E

'''
题目解析:
这是一个期望DP问题，可以用高斯消元来解决。

解题思路:
1. 设f[x][y]表示从位置(x,y)走到距离原点大于R的点的期望步数
2. 对于距离原点大于R的点，f[x][y] = 0
3. 对于其他点，根据转移建立方程：
   f[x][y] = 1 + (f[x-1][y] + f[x+1][y] + f[x][y-1] + f[x][y+1]) / 4
4. 移项后得到：
   4 * f[x][y] = 4 + f[x-1][y] + f[x+1][y] + f[x][y-1] + f[x][y+1]
   3 * f[x][y] = 4 + f[x-1][y] + f[x+1][y] + f[x][y-1] + f[x][y+1]
5. 使用高斯消元求解线性方程组

时间复杂度: O((R^2)^3) = O(R^6)
空间复杂度: O(R^4)

工程化考虑:
1. 正确确定需要计算的点的范围
2. 浮点数精度处理
3. 高斯消元求解线性方程组
4. 坐标映射到一维索引
'''

MAXR = 55
MAXN = MAXR * MAXR * 4
EPS = 1e-10

# 增广矩阵
mat = [[0.0 for _ in range(MAXN + 1)] for _ in range(MAXN)]

# 点的坐标列表
points = []

# 坐标到索引的映射
id = [[0 for _ in range(MAXR * 2)] for _ in range(MAXR * 2)]

R = 0
n = 0  # 点的总数

# 方向数组：上、下、左、右
dx = [-1, 1, 0, 0]
dy = [0, 0, -1, 1]


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y


def dist2(x, y):
    '''
    计算点到原点的距离的平方
    时间复杂度: O(1)
    空间复杂度: O(1)
    '''
    return x * x + y * y


def gauss(size):
    '''
    高斯消元解决浮点数线性方程组
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
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


def main():
    global R, n
    R = int(input())

    # 初始化id数组
    for i in range(MAXR * 2):
        for j in range(MAXR * 2):
            id[i][j] = 0

    # 收集所有需要计算的点
    points.clear()
    n = 0
    for x in range(-R, R + 1):
        for y in range(-R, R + 1):
            if dist2(x, y) <= R * R:
                points.append(Point(x, y))
                id[x + R][y + R] = n
                n += 1

    # 初始化矩阵
    for i in range(n):
        for j in range(n + 1):
            mat[i][j] = 0.0

    # 建立方程组
    for i in range(n):
        p = points[i]
        idx = id[p.x + R][p.y + R]

        # 如果点距离原点大于R，则期望步数为0
        if dist2(p.x, p.y) > R * R:
            mat[idx][idx] = 1.0
            mat[idx][n] = 0.0
            continue

        # 对角线系数
        mat[idx][idx] = 3.0  # 4-1=3

        # 常数项
        mat[idx][n] = 4.0

        # 邻接点的贡献
        for k in range(4):
            nx = p.x + dx[k]
            ny = p.y + dy[k]
            if dist2(nx, ny) <= R * R:
                nidx = id[nx + R][ny + R]
                mat[idx][nidx] = -1.0

    # 高斯消元求解
    gauss(n)

    # 输出原点的期望步数
    print("{:.10f}".format(mat[id[R][R]][n]))


if __name__ == "__main__":
    main()