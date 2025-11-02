from collections import deque
import sys

# Bellman-Ford + SPFA优化模版（洛谷）
# 给定n个点的有向图，请求出图中是否存在从顶点1出发能到达的负环
# 负环的定义是：一条边权之和为负数的回路
# 测试链接 : https://www.luogu.com.cn/problem/P3385
# 请同学们务必参考如下代码中关于输入、输出的处理
# 这是输入输出处理效率很高的写法
# 提交以下所有代码，把主类名改成Main，可以直接通过

# SPFA算法（Shortest Path Faster Algorithm）是Bellman-Ford算法的队列优化版本
# 通过维护一个队列，只对可能被更新的节点进行松弛操作，避免了不必要的计算
# 时间复杂度: 平均O(E)，最坏O(VE)，其中V是节点数，E是边数
# 空间复杂度: O(V+E)

# 定义常量
MAXN = 2001
MAXM = 6001
MAXQ = 4000001

def build(n, head, next_arr, to, weight, distance, updateCnt, enter):
    """初始化函数，重置所有数据结构"""
    global cnt
    cnt = 1
    
    # 将head数组从1到n初始化为0
    for i in range(1, n + 1):
        head[i] = 0
    
    # 将distance数组从1到n初始化为最大值
    for i in range(1, n + 1):
        distance[i] = float('inf')
    
    # 将updateCnt数组从1到n初始化为0
    for i in range(1, n + 1):
        updateCnt[i] = 0
    
    # 将enter数组从1到n初始化为False
    for i in range(1, n + 1):
        enter[i] = False

def addEdge(u, v, w, next_arr, to, weight):
    """添加边的函数，使用链式前向星存储图"""
    global cnt
    next_arr[cnt] = head[u]
    to[cnt] = v
    weight[cnt] = w
    head[u] = cnt
    cnt += 1

def spfa(n, head, next_arr, to, weight, distance, updateCnt, queue, enter):
    """Bellman-Ford + SPFA优化的模版
    通过队列优化，只处理可能被更新的节点
    
    Args:
        n: 节点数
        head: 链式前向星的head数组
        next_arr: 链式前向星的next数组
        to: 链式前向星的to数组
        weight: 链式前向星的weight数组
        distance: 源点出发到每个节点的距离表
        updateCnt: 节点被松弛的次数，用于检测负环
        queue: 队列，存储待处理的节点
        enter: 节点是否已经在队列中
    
    Returns:
        bool: 是否存在负环
    """
    # 初始化源点（节点1）的距离为0
    distance[1] = 0
    # 源点的松弛次数加1
    updateCnt[1] += 1
    # 将源点加入队列
    queue.append(1)
    # 标记源点已在队列中
    enter[1] = True
    
    # 当队列不为空时继续处理
    while queue:
        # 取出队首节点
        u = queue.popleft()
        # 标记该节点已出队
        enter[u] = False
        
        # 遍历从节点u出发的所有边
        ei = head[u]
        while ei > 0:
            v = to[ei]
            w = weight[ei]
            # 如果通过节点u可以缩短到节点v的距离
            if distance[u] + w < distance[v]:
                # 更新到节点v的最短距离
                distance[v] = distance[u] + w
                # 如果节点v不在队列中
                if not enter[v]:
                    # 松弛次数超过n-1说明存在负环
                    updateCnt[v] += 1
                    if updateCnt[v] > n - 1:
                        return True
                    # 将节点v加入队列
                    queue.append(v)
                    # 标记节点v已在队列中
                    enter[v] = True
            ei = next_arr[ei]
    
    # 不存在负环
    return False

def main():
    """主函数"""
    global cnt, head
    
    # 初始化数据结构
    head = [0] * MAXN
    next_arr = [0] * MAXM
    to = [0] * MAXM
    weight = [0] * MAXM
    distance = [0] * MAXN
    updateCnt = [0] * MAXN
    queue = deque()
    enter = [False] * MAXN
    
    try:
        cases = int(input())
        for _ in range(cases):
            n, m = map(int, input().split())
            build(n, head, next_arr, to, weight, distance, updateCnt, enter)
            
            for _ in range(m):
                u, v, w = map(int, input().split())
                if w >= 0:
                    # 如果权重非负，添加双向边
                    addEdge(u, v, w, next_arr, to, weight)
                    addEdge(v, u, w, next_arr, to, weight)
                else:
                    # 如果权重为负，只添加单向边
                    addEdge(u, v, w, next_arr, to, weight)
            
            # 调用SPFA算法检测负环
            result = "YES" if spfa(n, head, next_arr, to, weight, distance, updateCnt, queue, enter) else "NO"
            print(result)
            
    except EOFError:
        pass

if __name__ == "__main__":
    cnt = 0
    head = []
    main()