# Floyd算法模版（洛谷）
# 测试链接 : https://www.luogu.com.cn/problem/P2910
# 请同学们务必参考如下代码中关于输入、输出的处理
# 这是输入输出处理效率很高的写法
# 提交以下所有代码，把主类名改成Main，可以直接通过

# Floyd算法用于解决多源最短路径问题
# 基于动态规划思想，能够一次性求出图中任意两点之间的最短路径
# 时间复杂度: O(N^3)
# 空间复杂度: O(N^2)
# 适用于节点数较少的图，通常N <= 500

import sys
from sys import stdin
from sys import stdout

# 定义常量
MAXN = 101
MAXM = 10001

def build(n, distance):
    """初始时设置任意两点之间的最短距离为无穷大，表示任何路不存在
    对角线元素初始化为0，表示节点到自身的距离为0
    """
    for i in range(n):
        for j in range(n):
            distance[i][j] = float('inf')
        distance[i][i] = 0  # 节点到自身的距离为0

def floyd(n, distance):
    """Floyd算法核心实现
    通过中间节点k来更新任意两点i,j之间的最短距离
    状态转移方程: distance[i][j] = min(distance[i][j], distance[i][k] + distance[k][j])
    三层循环顺序很重要：最外层是中间节点k，然后是起点i和终点j
    """
    # O(N^3)的过程
    # 枚举每个跳板
    # 注意，跳板要最先枚举！跳板要最先枚举！跳板要最先枚举！
    for bridge in range(n):  # 跳板
        for i in range(n):
            for j in range(n):
                # i -> .....bridge .... -> j
                # distance[i][j]能不能缩短
                # distance[i][j] = min ( distance[i][j] , distance[i][bridge] + distance[bridge][j])
                if distance[i][bridge] != float('inf') and distance[bridge][j] != float('inf') and distance[i][j] > distance[i][bridge] + distance[bridge][j]:
                    distance[i][j] = distance[i][bridge] + distance[bridge][j]

def main():
    """主函数"""
    try:
        while True:
            line = stdin.readline()
            if not line:
                break
            
            values = list(map(int, line.split()))
            n, m = values[0], values[1]
            
            path = []
            for i in range(m):
                path.append(int(stdin.readline()) - 1)
            
            # 初始化距离矩阵
            distance = [[0] * MAXN for _ in range(MAXN)]
            build(n, distance)
            
            # 读取邻接矩阵
            for i in range(n):
                row = list(map(int, stdin.readline().split()))
                for j in range(n):
                    distance[i][j] = row[j]
            
            # 执行Floyd算法
            floyd(n, distance)
            
            # 计算路径总长度
            ans = 0
            for i in range(1, m):
                ans += distance[path[i - 1]][path[i]]
            
            stdout.write(str(ans) + '\n')
            stdout.flush()
            
    except EOFError:
        pass

if __name__ == "__main__":
    main()