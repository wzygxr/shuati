# Dijkstra算法练习题1：网络延迟时间
# 有 n 个网络节点，标记为 1 到 n。
# 给你一个列表 times，表示信号经过有向边的传递时间。 
# times[i] = (ui, vi, wi)，其中 ui 是源节点，vi 是目标节点，wi 是信号从源节点传递到目标节点的时间。
# 现在，从某个节点 K 发出一个信号。需要多久才能使所有节点都收到信号？
# 如果不能使所有节点收到信号，返回 -1。
# 测试链接: https://leetcode.cn/problems/network-delay-time/
# 
# 算法思路：
# 这是一道典型的单源最短路径问题，使用Dijkstra算法解决。
# 1. 构建图的邻接表表示
# 2. 使用优先队列优化的Dijkstra算法计算从节点K到所有节点的最短距离
# 3. 如果存在无法到达的节点，返回-1
# 4. 否则返回所有最短距离中的最大值
#
# 具体实现：
# 1. 初始化图的邻接表表示
# 2. 使用优先队列存储待处理的节点，按距离从小到大排序
# 3. 从起始节点开始，逐步扩展到其他节点
# 4. 对于每个节点，更新其相邻节点的最短距离
# 5. 最后检查是否所有节点都可达，并返回最大距离
#
# 时间复杂度：O((V + E) * log V)，其中V是节点数，E是边数
# 空间复杂度：O(V + E)
#
# 相关题目链接：
# 1. LeetCode 743. 网络延迟时间 - https://leetcode.cn/problems/network-delay-time/
# 2. 洛谷 P4779 单源最短路径 - https://www.luogu.com.cn/problem/P4779
# 3. POJ 2387 Til the Cows Come Home - http://poj.org/problem?id=2387
# 4. Codeforces 20C Dijkstra? - https://codeforces.com/problemset/problem/20/C
# 5. 洛谷 P3371 单源最短路径 - https://www.luogu.com.cn/problem/P3371
# 6. HDU 2544 最短路 - https://acm.hdu.edu.cn/showproblem.php?pid=2544
# 7. AtCoder ABC070 D - Transit Tree Path - https://atcoder.jp/contests/abc070/tasks/abc070_d
# 8. 牛客 NC50439 最短路 - https://ac.nowcoder.com/acm/problem/50439
# 9. SPOJ SHPATH - https://www.spoj.com/problems/SHPATH/
# 10. ZOJ 2818 The Traveling Judges Problem - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827366818
# 11. 51Nod 1018 最短路 - https://www.51nod.com/Challenge/Problem.html#problemId=1018
# 12. 洛谷 P1144 最短路计数 - https://www.luogu.com.cn/problem/P1144
# 13. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
# 14. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/
# 15. 牛客 NC50522 跳楼机 - https://ac.nowcoder.com/acm/problem/50522

import sys
import heapq

# 常量定义
MAXN = 101
INF = float('inf')

# 全局变量
n = k = 0
graph = [[] for _ in range(MAXN)]  # 邻接表表示图
dist = [INF] * MAXN                # 距离数组
visited = [False] * MAXN           # 访问标记数组

# 添加边
def addEdge(from_node, to_node, weight):
    graph[from_node].append((to_node, weight))

# Dijkstra算法实现
def dijkstra():
    global n, k, graph, dist, visited
    
    # 初始化距离数组为无穷大
    for i in range(1, n + 1):
        dist[i] = INF
    # 初始化访问标记数组为False
    for i in range(1, n + 1):
        visited[i] = False
    
    # 起点距离为0
    dist[k] = 0
    
    # 优先队列，按距离排序，存储待处理的节点
    pq = [(0, k)]
    
    # 当优先队列不为空时，继续处理
    while pq:
        # 取出距离最小的节点
        curr_dist, u = heapq.heappop(pq)
        
        # 如果已经访问过，跳过（避免重复处理）
        if visited[u]:
            continue
        
        # 标记为已访问
        visited[u] = True
        
        # 遍历当前节点的所有邻接节点
        for v, w in graph[u]:
            # 松弛操作：如果通过当前节点u可以缩短到节点v的距离，则更新
            if not visited[v] and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                # 将更新后的节点加入优先队列
                heapq.heappush(pq, (dist[v], v))
    
    # 计算最大距离：遍历所有节点的最短距离，找出最大值
    maxDist = 0
    for i in range(1, n + 1):
        # 如果存在无法到达的节点，返回-1
        if dist[i] == INF:
            return -1
        # 更新最大距离
        maxDist = max(maxDist, dist[i])
    
    # 返回所有节点都能到达时的最大距离
    return maxDist

def main():
    global n, k, graph
    
    # 读取边数和节点数
    line = sys.stdin.readline().strip().split()
    m = int(line[0])
    n = int(line[1])
    k = int(line[2])
    
    # 读取边信息并构图
    for i in range(m):
        line = sys.stdin.readline().strip().split()
        u = int(line[0])
        v = int(line[1])
        w = int(line[2])
        addEdge(u, v, w)
    
    # 计算结果并输出
    print(dijkstra())

if __name__ == "__main__":
    main()