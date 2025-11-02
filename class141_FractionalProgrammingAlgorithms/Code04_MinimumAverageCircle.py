# 最小圈问题 - 01分数规划解法
# 题目描述：给定一个有向带权图，求所有环的平均值中最小的平均值
# 环的平均值定义为：环中边的权值和 / 环中边的数量
# 输入：n个节点，m条有向边及各自的权值
# 输出：最小的环平均值，保留8位小数
# 测试链接：https://www.luogu.com.cn/problem/P3199

'''
算法思路：01分数规划 + 二分查找 + DFS判负环

01分数规划的数学原理：
我们需要找到环C，使得 (Σw(e))/|C| 最小，其中e∈C，|C|是环的边数

对于给定的L，判断是否存在环C，使得 (Σw(e))/|C| < L
等价于：Σw(e) < L * |C|
等价于：Σ(w(e) - L) < 0

这相当于在原图每条边的权值减去L后，判断图中是否存在负环

通过二分L的值，我们可以逐步逼近最小的环平均值
'''

import sys
from sys import stdin

sys.setrecursionlimit(1 << 25)  # 设置递归深度，避免DFS时栈溢出

# 常量定义
MAXN = 3001  # 最大节点数
MAXM = 10001  # 最大边数
MAXE = 1e7  # 最大边权绝对值
PRECISION = 1e-9  # 精度控制

# 全局变量
graph = []  # 邻接表存储图
value = []  # 每个点的累积边权
path = []   # 每个点是否在当前递归路径上
n, m = 0, 0  # 节点数和边数

def dfs(u, L):
    """
    DFS判断负环
    在每条边减去L后，判断图中是否存在负环
    
    参数：
        u (int): 当前访问的节点
        L (float): 当前的比值候选值
    
    返回：
        bool: 是否存在负环
    """
    # 标记当前节点在递归路径上
    path[u] = True
    
    # 遍历u的所有出边
    for v, w in graph[u]:
        # 边权减去L
        new_weight = w - L
        
        # 松弛操作：如果通过u到v可以使value[v]更小
        if value[v] > value[u] + new_weight:
            value[v] = value[u] + new_weight
            
            # 如果v已经在当前递归路径上，说明找到了一个负环
            # 或者从v出发找到了负环
            if path[v] or dfs(v, L):
                return True
    
    # 回溯，标记当前节点不在递归路径上
    path[u] = False
    return False

def check(L):
    """
    检查是否存在一个环，其平均值小于L
    
    参数：
        L (float): 当前的比值候选值
    
    返回：
        bool: 是否存在这样的环
    """
    # 初始化每个节点的累积边权为0
    for i in range(1, n + 1):
        value[i] = 0.0
    
    # 初始化每个节点不在任何递归路径上
    for i in range(1, n + 1):
        path[i] = False
    
    # 对每个节点进行DFS，因为图可能不连通
    # 如果从任何一个节点出发能找到负环，则返回True
    for i in range(1, n + 1):
        if dfs(i, L):
            return True
    
    return False

def main():
    """
    主函数：读取输入，执行二分查找算法求解最小圈问题
    """
    global n, m, graph, value, path
    
    # 读取输入
    input_lines = [line.strip() for line in stdin.readlines()]
    ptr = 0
    
    # 读取节点数和边数
    while ptr < len(input_lines):
        if input_lines[ptr].strip():  # 跳过空行
            break
        ptr += 1
    
    n, m = map(int, input_lines[ptr].split())
    ptr += 1
    
    # 初始化图的邻接表
    graph = [[] for _ in range(n + 1)]  # 节点编号从1开始
    
    # 初始化全局数组
    value = [0.0] * (n + 1)
    path = [False] * (n + 1)
    
    # 读取每条边
    edges_read = 0
    while ptr < len(input_lines) and edges_read < m:
        if not input_lines[ptr].strip():  # 跳过空行
            ptr += 1
            continue
        
        parts = input_lines[ptr].split()
        u = int(parts[0])
        v = int(parts[1])
        w = float(parts[2])
        graph[u].append((v, w))
        
        ptr += 1
        edges_read += 1
    
    # 初始化二分查找的左右边界
    # 左边界为最小可能的边权，右边界为最大可能的边权
    left = -MAXE
    right = MAXE
    result = 0.0
    
    # 二分查找过程
    # 当左右边界的差大于精度要求时继续循环
    while left < right and right - left >= PRECISION:
        mid = (left + right) / 2.0
        
        # 检查是否存在环的平均值小于mid
        if check(mid):
            # 如果存在，则调整右边界
            right = mid - PRECISION
        else:
            # 否则调整左边界，并记录当前结果
            result = mid
            left = mid + PRECISION
    
    # 输出结果，保留8位小数
    print("%.8f" % result)

if __name__ == "__main__":
    main()