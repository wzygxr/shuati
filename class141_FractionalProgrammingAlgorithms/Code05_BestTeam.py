# -*- coding: utf-8 -*-

"""
最佳团体问题 - 树形背包 + 01分数规划解法

题目描述：
给定一棵树，节点编号0~n，0号节点是整棵树的头
编号1~n的节点，每个节点都有招募花费和战斗值，0号节点这两个值都是0
当招募某个节点时，必须招募该节点及其所有祖先节点
除了0号节点之外，一共可以招募k个人，希望让战斗值之和/招募花费之和的比值尽量大

数据范围：
1 <= k <= n <= 2500
0 <= 招募花费、战斗值 <= 10^4

测试链接：https://www.luogu.com.cn/problem/P4322

算法思路：01分数规划 + 树形背包 + DFN序优化

01分数规划的数学原理：
我们需要找到k个节点的集合S(不包括0号节点)，使得(Σstrength[i])/(Σcost[i])最大
且S满足树形依赖关系（选子必选父）

对于给定的L，判断是否存在合法集合S，使得Σstrength[i]/Σcost[i] > L
等价于：Σ(strength[i] - L * cost[i]) > 0

我们使用树形背包来解决依赖关系的选择问题，同时通过DFN序优化树形DP

时间复杂度：O(log(1/ε) * n^2)，其中ε是精度要求
空间复杂度：O(n^2)
"""

import sys
from typing import List, Tuple

# 常量定义
MAXN: int = 3001        # 最大节点数
LIMIT: int = 10000      # 最大可能的比值上限
NA: float = -1e9        # 无效解标记
PRECISION: float = 1e-6 # 精度控制

# 全局变量
# 链式前向星存储树结构
head: List[int] = [0] * MAXN         # 每个节点的第一条边
next_edge: List[int] = [0] * MAXN    # 每条边的下一条边
to: List[int] = [0] * MAXN           # 每条边的目标节点
edgeCnt: int = 0                     # 边的计数器

# 节点属性
cost: List[int] = [0] * MAXN         # 招募花费，下标为节点原始编号
strength: List[int] = [0] * MAXN     # 战斗值，下标为节点原始编号

# DFN序相关
dfn: List[int] = [0] * MAXN          # dfn[a] = b，表示原始a号节点的dfn编号为b
dfnCnt: int = 0                      # dfn序计数
size: List[int] = [0] * MAXN         # 子树大小，下标为节点dfn编号

# 01分数规划和树形DP相关
value: List[float] = [0.0] * MAXN    # (战斗值 - x * 招募花费)的结余
dp: List[List[float]] = [[0.0 for _ in range(MAXN)] for _ in range(MAXN)]  # 树形DP数组

k: int = 0   # 需要招募的人数
n: int = 0   # 总人数

def prepare() -> None:
    """
    初始化图的存储结构
    重置边计数器、dfn序计数器，并清空邻接表
    """
    global edgeCnt, dfnCnt
    edgeCnt = 1
    dfnCnt = 0
    for i in range(1, n + 1):
        head[i] = 0

def addEdge(u: int, v: int) -> None:
    """
    向树中添加一条有向边（父节点到子节点）
    
    Args:
        u: 父节点
        v: 子节点
    """
    global edgeCnt
    next_edge[edgeCnt] = head[u]
    to[edgeCnt] = v
    head[u] = edgeCnt
    edgeCnt += 1

def dfs(u: int) -> int:
    """
    计算每个节点的DFN编号和子树大小
    使用后序遍历，将树结构转换为线性结构，便于树形背包DP
    
    Args:
        u: 当前节点
    
    Returns:
        int: 当前子树的大小
    """
    global dfnCnt
    i: int = dfnCnt + 1
    dfnCnt = i
    dfn[u] = i      # 记录当前节点的DFN编号
    size[i] = 1     # 初始子树大小为1（包含自己）
    
    # 遍历所有子节点
    e: int = head[u]
    while e != 0:
        v: int = to[e]
        size[i] += dfs(v)  # 累加子节点的子树大小
        e = next_edge[e]
    
    return size[i]

def check(x: float) -> bool:
    """
    检查是否存在一种招募方式，使得比值大于x
    使用树形背包DP来求解最大结余和
    
    Args:
        x: 当前尝试的比值
    
    Returns:
        bool: 是否存在这样的招募方式
    """
    # 计算每个节点的结余值：战斗值 - x * 招募花费
    for i in range(n + 1):
        value[dfn[i]] = float(strength[i]) - x * cost[i]
    
    # 初始化越界位置为无效解
    for j in range(1, k + 1):
        dp[dfnCnt + 1][j] = NA
    
    # 树形背包DP核心逻辑（基于DFN序的后序遍历）
    for i in range(dfnCnt, 1, -1):  # 从后往前处理，0号节点的DFN是1，跳过
        for j in range(1, k + 1):  # 枚举招募人数
            # 两种选择：
            # 1. 不选当前节点所在子树：dp[i + size[i]][j]
            # 2. 选当前节点：value[i] + dp[i + 1][j - 1]
            dp[i][j] = max(
                dp[i + size[i]][j],  # 不选当前子树
                value[i] + dp[i + 1][j - 1]  # 选当前节点，然后从子节点中选j-1个
            )
    
    # 原始的0号节点的DFN编号是1，其他节点的DFN编号从2开始
    # 0号节点的战斗值和招募花费都是0，我们需要从其他节点中招募k个
    return dp[2][k] >= 0  # 如果最大结余和>=0，说明存在比值>=x的方案

def main() -> None:
    """
    主函数
    处理输入、构建树结构、执行算法并输出结果
    """
    global k, n
    
    try:
        # 读取输入：需要招募的人数k和总人数n
        line = sys.stdin.readline().split()
        k = int(line[0])
        n = int(line[1])
        
        # 初始化树结构
        prepare()
        
        # 读取每个节点的信息：招募花费、战斗值和父节点
        for i in range(1, n + 1):
            try:
                line = sys.stdin.readline().split()
                cost[i] = int(line[0])
                strength[i] = int(line[1])
                parent = int(line[2])
                addEdge(parent, i)  # 添加父节点到子节点的边
            except (IndexError, ValueError):
                # 处理输入格式错误的情况
                print(f"输入数据格式错误，第{i+1}行数据不符合要求")
                return
        
        # 计算DFN序和子树大小
        dfs(0)
        
        # 初始化二分查找的左右边界
        left: float = 0.0
        right: float = LIMIT
        result: float = 0.0
        
        # 二分查找最优比值
        while left < right and right - left >= PRECISION:
            mid: float = (left + right) / 2.0
            
            if check(mid):
                # 如果存在比值>=mid的方案，尝试更大的值
                result = mid
                left = mid + PRECISION
            else:
                # 否则尝试更小的值
                right = mid - PRECISION
        
        # 输出结果，保留3位小数
        print("%.3f" % result)
        
    except Exception as e:
        # 捕获所有异常并输出错误信息
        print(f"程序运行出错: {str(e)}")

if __name__ == "__main__":
    main()