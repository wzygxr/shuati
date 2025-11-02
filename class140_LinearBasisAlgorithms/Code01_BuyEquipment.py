# 装备购买
# 一共有n个物品，每个物品都有m个属性值
# 下面定义什么是不必要的物品：如果已经选择了k个物品，此时又有一件当前物品
# 如果给已经选择的物品分配一组相乘的系数，并把属性值相加，就能得到当前物品
# 那么就说当前物品是不必要的，比如下面的例子
# a = { 4, 6, 2 }, b = { 2, 8, 4 }, c = { 6, 19, 9 }
# a * 0.5 + b * 2 = c，那么c物品是不必要的
# 每个物品都有价格，现在希望尽量多的购买物品，但不能出现不必要的物品
# 返回最多能买几件物品和最少的花费
# 1 <= n、m <= 500
# 0 <= 属性值 <= 1000
# 测试链接 : https://www.luogu.com.cn/problem/P3265
# 请务必在原有代码基础上增加详细注释，确保代码可以编译运行且没有错误

import sys
from io import StringIO
import math

MAXN = 502
MAXM = 502
sml = 1e-5

# mat[i][j] 表示第i个物品的第j个属性值
# mat[i][m+1] 表示第i个物品的价格
mat = [[0.0 for _ in range(MAXM + 2)] for _ in range(MAXN)]

# basis[j] 记录第j个属性位置上线性基中物品的编号
basis = [0 for _ in range(MAXM + 2)]

n = 0
m = 0
cnt = 0
cost = 0

def compute():
    """
    计算最多能购买的物品数量和最少花费
    算法思路：
    1. 按价格从低到高排序物品
    2. 使用线性基判断物品是否线性相关
    3. 贪心地选择线性无关的物品
    时间复杂度：O(n*m^2)
    空间复杂度：O(n*m)
    """
    global cnt, cost
    cnt = 0
    cost = 0
    
    # 按价格排序，优先选择便宜的物品
    # 使用lambda表达式按价格排序，价格相同则按编号排序
    items = []
    for i in range(1, n + 1):
        items.append((mat[i][m + 1], i))
    items.sort()
    
    # 重新排列mat数组
    new_mat = [[0.0 for _ in range(MAXM + 2)] for _ in range(MAXN)]
    for i in range(1, n + 1):
        idx = items[i-1][1]
        for j in range(1, m + 2):
            new_mat[i][j] = mat[idx][j]
    
    # 复制回原数组
    for i in range(1, n + 1):
        for j in range(1, m + 2):
            mat[i][j] = new_mat[i][j]
    
    # 清空线性基
    for i in range(1, m + 1):
        basis[i] = 0
    
    # 贪心选择线性无关的物品
    for i in range(1, n + 1):
        if insert(i):
            cnt += 1
            cost += int(mat[i][m + 1])

def insert(i):
    """
    将第i个物品插入线性基
    算法思路：
    1. 遍历物品的所有属性
    2. 如果当前属性值不为0且该位置上线性基为空，则插入
    3. 否则用线性基中已有的向量消元
    返回值：如果成功插入返回True，否则返回False
    """
    for j in range(1, m + 1):
        if abs(mat[i][j]) >= sml:
            if basis[j] == 0:
                basis[j] = i
                return True
            # 消元操作
            rate = mat[i][j] / mat[basis[j]][j]
            for k in range(j, m + 1):
                mat[i][k] -= rate * mat[basis[j]][k]
    return False

def main():
    """
    主函数
    读取输入数据，调用计算函数，输出结果
    """
    global n, m
    
    # 读取输入
    line = sys.stdin.readline().strip().split()
    n = int(line[0])
    m = int(line[1])
    
    # 读取物品属性
    for i in range(1, n + 1):
        line = sys.stdin.readline().strip().split()
        for j in range(1, m + 1):
            mat[i][j] = float(line[j - 1])
    
    # 读取物品价格
    line = sys.stdin.readline().strip().split()
    for i in range(1, n + 1):
        mat[i][m + 1] = float(line[i - 1])
    
    # 计算结果
    compute()
    
    # 输出结果
    print(cnt, cost)

if __name__ == "__main__":
    # 用于测试的示例输入
    # input_data = """3 2
    # 1 0
    # 1 1
    # 0 1
    # 10 20 15"""
    # sys.stdin = StringIO(input_data)
    main()