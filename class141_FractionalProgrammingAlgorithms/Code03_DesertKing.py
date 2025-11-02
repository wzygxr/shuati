# -*- coding: utf-8 -*-

"""
01分数规划问题 - 最优比率生成树（Desert King）

题目来源：POJ 2728
题目描述：有n个村庄，每个村庄由(x, y, z)表示，其中(x,y)是二维地图中的位置，z是海拔高度。
任意两个村庄之间的距离是二维地图中的欧式距离，修路花费是海拔差值的绝对值。
要求将所有村庄连通，使得总花费/总距离的比值最小，结果保留小数点后3位。

数据范围：
2 <= n <= 10^3
0 <= x、y <= 10^4
0 <= z <= 10^7
测试链接：http://poj.org/problem?id=2728

算法思路：使用二分法求解01分数规划问题，结合Prim算法求最小生成树进行可行性判断
时间复杂度：O(n^2 * log(1/ε))，其中ε是精度要求
空间复杂度：O(n^2)

01分数规划的数学原理：
我们需要最小化 R = (sum(cost_e)) / (sum(dist_e))，其中e是生成树中的边
对于给定的L，判断是否存在生成树使得 R < L
等价于：sum(cost_e) < L * sum(dist_e)
等价于：sum(cost_e - L * dist_e) < 0
我们通过二分L的值，使用Prim算法计算最小生成树的权值和来判断是否可行
如果最小生成树的权值和 < 0，则说明L可行，可以尝试更小的值
"""

import sys
import math

# 常量定义
MAXN = 1001  # 最大村庄数量
PRECISION = 1e-6  # 精度控制，用于二分结束条件

# 全局变量初始化
x = [0] * MAXN  # 村庄的x坐标
y = [0] * MAXN  # 村庄的y坐标
z = [0] * MAXN  # 村庄的海拔高度

dist = [[0.0 for _ in range(MAXN)] for _ in range(MAXN)]  # 任意两村庄间的欧式距离
cost = [[0.0 for _ in range(MAXN)] for _ in range(MAXN)]  # 任意两村庄间的修路花费（海拔差绝对值）
visit = [False] * MAXN  # 标记村庄是否已加入生成树
value = [0.0] * MAXN  # 存储每个村庄到生成树的最小边权（cost_e - L * dist_e）

n = 0  # 村庄数量

def prim(L):
    """
    邻接矩阵结构下的Prim算法，从节点1出发计算最小生成树的权值和
    边权为 cost_e - L * dist_e，用于01分数规划的可行性判断
    
    参数：
        L (float): 当前尝试的比率值
    
    返回：
        float: 最小生成树的权值和
    """
    # 初始化visit数组和value数组
    for i in range(1, n + 1):
        visit[i] = False
        value[i] = cost[1][i] - L * dist[1][i]  # 初始化为从节点1出发的边权
    
    visit[1] = True  # 标记节点1已加入生成树
    total_weight = 0.0  # 用于存储最小生成树的权值和
    
    # 最小生成树一定有n-1条边，需要进行n-1轮选择
    for i in range(1, n):
        # 在未加入生成树的节点中，找到到生成树点集距离最小的点
        min_edge = float('inf')
        next_node = 0
        for j in range(1, n + 1):
            if not visit[j] and value[j] < min_edge:
                min_edge = value[j]
                next_node = j
        
        # 将选中的边加入生成树，更新总和
        total_weight += min_edge
        visit[next_node] = True  # 标记新节点已加入生成树
        
        # 查看新加入的节点能否更新其他未加入节点到生成树的最小距离
        for j in range(1, n + 1):
            if not visit[j]:
                new_edge_value = cost[next_node][j] - L * dist[next_node][j]
                if value[j] > new_edge_value:
                    value[j] = new_edge_value
    
    return total_weight  # 返回最小生成树的权值和

def read_input_line():
    """
    读取输入行，处理可能的输入错误
    
    返回：
        list: 分割后的输入数据列表
    
    异常：
        EOFError: 当遇到文件结束符时
    """
    line = sys.stdin.readline()
    if not line:  # 检查是否到达文件末尾
        raise EOFError("输入已结束")
    return line.strip().split()

def main():
    """
    主函数：处理输入输出，执行二分查找算法求解最优比率生成树问题
    """
    global n
    
    try:
        # 读取村庄数量
        line = read_input_line()
        n = int(line[0])
        
        # 处理多组测试用例，直到输入0
        while n != 0:
            # 读取每个村庄的坐标和海拔
            for i in range(1, n + 1):
                line = read_input_line()
                x[i] = int(line[0])
                y[i] = int(line[1])
                z[i] = int(line[2])
            
            # 预处理计算任意两个村庄之间的距离和花费
            for i in range(1, n + 1):
                for j in range(1, n + 1):
                    if i != j:
                        # 计算欧式距离
                        dx = x[i] - x[j]
                        dy = y[i] - y[j]
                        dist[i][j] = math.sqrt(dx * dx + dy * dy)
                        
                        # 计算海拔差绝对值作为花费
                        cost[i][j] = abs(z[i] - z[j])
            
            # 初始化二分查找的左右边界
            # 左边界为0，右边界可以根据数据范围估算
            left = 0.0
            right = 100.0  # 最大可能的比值，根据数据范围设置
            result = 0.0
            
            # 二分查找过程
            # 当左右边界的差大于精度要求时继续循环
            while left < right and right - left >= PRECISION:
                mid = (left + right) / 2.0
                
                # 调用Prim算法计算当前比率下的最小生成树权值和
                # 如果权值和 <= 0，说明可以找到更小的比值，调整右边界
                if prim(mid) <= 0:
                    result = mid
                    right = mid - PRECISION
                else:
                    # 否则调整左边界
                    left = mid + PRECISION
            
            # 输出结果，保留3位小数
            print("%.3f" % result)
            
            # 读取下一组测试用例的村庄数量
            line = read_input_line()
            n = int(line[0])
    except EOFError:
        # 处理输入结束的情况
        pass
    except (ValueError, IndexError) as e:
        # 处理输入格式错误
        print(f"输入格式错误: {e}", file=sys.stderr)

if __name__ == "__main__":
    main()