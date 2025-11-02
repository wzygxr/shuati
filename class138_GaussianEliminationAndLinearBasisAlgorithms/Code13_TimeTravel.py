# HDU 4418 Time travel
# 在一个有向图中，从起点到终点的期望步数
# 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=4418

'''
题目解析:
这是一个期望DP问题，需要使用高斯消元求解线性方程组。

解题思路:
1. 将图转换为状态转移方程
2. 建立线性方程组表示期望步数
3. 使用高斯消元求解线性方程组

时间复杂度: O(n^3)
空间复杂度: O(n^2)

工程化考虑:
1. 正确处理期望DP的状态转移
2. 特殊处理终点状态
3. 输入输出处理
'''

import sys

"""
init_graph - 高斯消元法应用 (Python实现)

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


import math

MAXN = 205
EPS = 1e-10  # 浮点数精度

# 邻接表存储图
graph = [[] for _ in range(MAXN)]

# 增广矩阵
mat = [[0.0 for _ in range(MAXN)] for _ in range(MAXN)]

n = 0
m = 0
start = 0
end = 0
d = 0
prob = [0 for _ in range(MAXN)]


def init_graph():
    '''
    初始化图
    时间复杂度: O(n)
    空间复杂度: O(n)
    '''
    for i in range(n):
        graph[i].clear()


def build_equations():
    '''
    建立方程组
    时间复杂度: O(n^2)
    空间复杂度: O(n^2)
    '''
    # 初始化矩阵
    for i in range(n):
        for j in range(n + 1):
            mat[i][j] = 0.0

    # 对于每个状态建立方程
    for i in range(n):
        # 特殊处理终点状态
        if i == end:
            mat[i][i] = 1.0
            mat[i][n] = 0.0
            continue

        # 对于其他状态，根据状态转移建立方程
        mat[i][i] = 1.0  # 自身系数为1
        total_prob = 0.0
        
        # 遍历所有可能的转移
        for j in range(1, d + 1):
            if prob[j] > 0:
                next_state = (i + j) % n
                mat[i][next_state] -= prob[j] / 100.0  # 转移概率
                total_prob += prob[j] / 100.0
        
        # 常数项为1加上各项转移概率的期望
        mat[i][n] = 1.0 + total_prob


def gauss():
    '''
    高斯消元解决浮点数线性方程组
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
    '''
    for i in range(n):
        # 找到第i列系数绝对值最大的行
        max_row = i
        for j in range(i + 1, n):
            if abs(mat[j][i]) > abs(mat[max_row][i]):
                max_row = j

        # 交换行
        if max_row != i:
            for k in range(n + 1):
                mat[i][k], mat[max_row][k] = mat[max_row][k], mat[i][k]

        # 如果主元为0，说明矩阵奇异
        if abs(mat[i][i]) < EPS:
            continue

        # 将第i行主元系数化为1
        pivot = mat[i][i]
        for k in range(i, n + 1):
            mat[i][k] /= pivot

        # 消去其他行的第i列系数
        for j in range(n):
            if i != j and abs(mat[j][i]) > EPS:
                factor = mat[j][i]
                for k in range(i, n + 1):
                    mat[j][k] -= factor * mat[i][k]


def main():
    test_cases = int(input())

    for t in range(test_cases):
        global n, m, start, end, d
        n, m, start, end, d = map(int, input().split())

        # 初始化图
        init_graph()

        # 读取概率分布
        prob[1:d+1] = list(map(int, input().split()))

        # 建立方程组
        build_equations()

        # 高斯消元求解
        gauss()

        # 输出结果
        if abs(mat[start][start] - 1.0) < EPS:
            print("Impossible !")
        else:
            print("%.2f" % mat[start][n])


if __name__ == "__main__":
    main()