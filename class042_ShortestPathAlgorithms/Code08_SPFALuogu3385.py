from collections import deque

# 洛谷 P3385 【模板】负环
# 题目链接: https://www.luogu.com.cn/problem/P3385
# 题目描述: 给定一个有向图，请求出图中是否存在从顶点 1 出发能到达的负环。
# 负环的定义是：一条边权之和为负数的回路。
#
# 解题思路:
# 这道题可以使用SPFA算法来解决。SPFA是Bellman-Ford算法的队列优化版本，
# 通过维护一个队列，只对可能被更新的节点进行松弛操作，避免了不必要的计算。
# 我们使用一个数组记录每个节点被松弛的次数，如果某个节点被松弛的次数超过n-1次，
# 说明存在负环。
#
# 时间复杂度: 平均O(E)，最坏O(VE)，其中V是节点数，E是边数
# 空间复杂度: O(V+E)

def build(n):
    """
    初始化函数
    
    Args:
        n: 节点数量
    """
    global head, next_arr, to, weight, cnt
    global distance, updateCnt, inQueue
    
    cnt = 1
    head = [0] * (n + 1)
    next_arr = [0] * (2 * n)  # 假设最多2*n条边
    to = [0] * (2 * n)
    weight = [0] * (2 * n)
    
    distance = [float('inf')] * (n + 1)
    updateCnt = [0] * (n + 1)
    inQueue = [False] * (n + 1)

def addEdge(u, v, w):
    """
    添加边的函数，使用链式前向星存储图
    
    Args:
        u: 起点
        v: 终点
        w: 权重
    """
    global cnt, head, next_arr, to, weight
    
    next_arr[cnt] = head[u]
    to[cnt] = v
    weight[cnt] = w
    head[u] = cnt
    cnt += 1

def spfa(n):
    """
    SPFA算法检测负环
    
    Args:
        n: 节点数量
    
    Returns:
        bool: 是否存在负环
    """
    global distance, updateCnt, inQueue
    
    # 初始化源点（节点1）的距离为0
    distance[1] = 0
    queue = deque([1])
    inQueue[1] = True
    updateCnt[1] += 1
    
    while queue:
        u = queue.popleft()
        inQueue[u] = False
        
        # 遍历从节点u出发的所有边
        i = head[u]
        while i > 0:
            v = to[i]
            w = weight[i]
            
            # 如果通过节点u可以缩短到节点v的距离
            if distance[u] + w < distance[v]:
                distance[v] = distance[u] + w
                
                # 如果节点v不在队列中
                if not inQueue[v]:
                    # 松弛次数超过n-1说明存在负环
                    updateCnt[v] += 1
                    if updateCnt[v] > n - 1:
                        return True
                    queue.append(v)
                    inQueue[v] = True
            
            i = next_arr[i]
    
    return False

# 测试函数
if __name__ == "__main__":
    cases = int(input())
    
    for _ in range(cases):
        n, m = map(int, input().split())
        build(n)
        
        for _ in range(m):
            u, v, w = map(int, input().split())
            addEdge(u, v, w)
        
        print("YES" if spfa(n) else "NO")