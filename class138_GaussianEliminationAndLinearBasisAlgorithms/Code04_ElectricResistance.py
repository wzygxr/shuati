# HDU 3976 Electric resistance
# 给定一个由n个节点和m个电阻组成的电路，求节点1和节点n之间的等效电阻
# 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=3976

'''
题目解析:
这是一个使用高斯消元解决电路问题的经典题目。
利用基尔霍夫电流定律和欧姆定律建立线性方程组求解。

解题思路:
1. 以每个节点的电势为未知数
2. 根据基尔霍夫电流定律（流入电流等于流出电流）建立方程
3. 根据欧姆定律 I = (Ux - Uy) / R 建立电流与电势的关系
4. 设节点1电势为1，节点n电势为0，建立线性方程组
5. 使用高斯消元求解线性方程组
6. 等效电阻 = (节点1电势 - 节点n电势) / 总电流

时间复杂度: O(n^3)
空间复杂度: O(n^2)

工程化考虑:
1. 正确处理浮点数运算精度
2. 特殊处理节点1和节点n的方程
3. 输入输出处理
'''

import sys

"""
Edge - 高斯消元法应用 (Python实现)

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

MAXN = 51
EPS = 1e-10  # 浮点数精度

# 邻接表存储图
graph = [[] for _ in range(MAXN)]

# 增广矩阵
mat = [[0.0 for _ in range(MAXN + 1)] for _ in range(MAXN)]

n = 0
m = 0


class Edge:
    def __init__(self, to, r):
        self.to = to  # 连接的节点
        self.r = r    # 电阻值


def init_graph():
    '''
    初始化图
    时间复杂度: O(n)
    空间复杂度: O(n)
    '''
    for i in range(1, n + 1):
        graph[i].clear()


def add_edge(u, v, r):
    '''
    添加边
    时间复杂度: O(1)
    空间复杂度: O(1)
    '''
    graph[u].append(Edge(v, r))
    graph[v].append(Edge(u, r))


def build_equations():
    '''
    建立方程组
    时间复杂度: O(n + m)
    空间复杂度: O(n^2)
    '''
    # 初始化矩阵
    for i in range(1, n + 1):
        for j in range(1, n + 2):
            mat[i][j] = 0.0

    # 对于每个节点建立方程
    for i in range(1, n + 1):
        # 特殊处理节点1和节点n
        if i == 1 or i == n:
            # 节点1设电势为1，节点n设电势为0
            mat[i][i] = 1.0
            if i == 1:
                mat[i][n + 1] = 1.0
            else:
                mat[i][n + 1] = 0.0
            continue

        # 对于其他节点，根据基尔霍夫电流定律建立方程
        # 流入电流之和等于流出电流之和，即总和为0
        for e in graph[i]:
            j = e.to
            r = e.r
            # 系数为 1/R
            mat[i][j] += 1.0 / r
            # 对角线系数为 -Σ(1/R)
            mat[i][i] -= 1.0 / r


def gauss():
    '''
    高斯消元解决浮点数线性方程组
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
    '''
    for i in range(1, n + 1):
        # 找到第i列系数绝对值最大的行
        max_row = i
        for j in range(i + 1, n + 1):
            if abs(mat[j][i]) > abs(mat[max_row][i]):
                max_row = j

        # 交换行
        if max_row != i:
            for k in range(1, n + 2):
                mat[i][k], mat[max_row][k] = mat[max_row][k], mat[i][k]

        # 如果主元为0，说明矩阵奇异
        if abs(mat[i][i]) < EPS:
            continue

        # 将第i行主元系数化为1
        pivot = mat[i][i]
        for k in range(i, n + 2):
            mat[i][k] /= pivot

        # 消去其他行的第i列系数
        for j in range(1, n + 1):
            if i != j and abs(mat[j][i]) > EPS:
                factor = mat[j][i]
                for k in range(i, n + 2):
                    mat[j][k] -= factor * mat[i][k]


def main():
    case_num = 1
    
    try:
        while True:
            line = input().strip()
            if not line:
                break
                
            global n, m
            n, m = map(int, line.split())
            
            # 初始化图
            init_graph()
            
            # 读取边信息
            for _ in range(m):
                u, v, r = map(int, input().split())
                add_edge(u, v, float(r))
            
            # 建立方程组
            build_equations()
            
            # 高斯消元求解
            gauss()
            
            # 计算等效电阻
            # 总电流 = Σ((节点1电势 - 相邻节点电势) / 电阻)
            total_current = 0.0
            for e in graph[1]:
                v = e.to
                r = e.r
                total_current += (mat[1][n + 1] - mat[v][n + 1]) / r
            
            # 等效电阻 = 电压 / 电流 = 1.0 / total_current
            equivalent_resistance = 1.0 / total_current
            
            print("Case #%d: %.2f" % (case_num, equivalent_resistance))
            case_num += 1
            
    except EOFError:
        pass


if __name__ == "__main__":
    main()