# 算法测试文件
# 用于测试class143中实现的各种最短路算法的Python版本

import heapq
from collections import deque

"""
相关题目链接：
1. LeetCode 743. Network Delay Time (Dijkstra算法)
   题目链接：https://leetcode.cn/problems/network-delay-time/
   题解链接：https://leetcode.cn/problems/network-delay-time/solution/

2. LeetCode 542. 01 Matrix (01-BFS)
   题目链接：https://leetcode.cn/problems/01-matrix/
   题解链接：https://leetcode.cn/problems/01-matrix/solution/

3. LeetCode 773. Sliding Puzzle (BFS)
   题目链接：https://leetcode.cn/problems/sliding-puzzle/
   题解链接：https://leetcode.cn/problems/sliding-puzzle/solution/

4. 洛谷 P3371 【模板】单源最短路径（弱化版）(Dijkstra算法)
   题目链接：https://www.luogu.com.cn/problem/P3371

5. 洛谷 P4779 【模板】单源最短路径（标准版）(Dijkstra算法)
   题目链接：https://www.luogu.com.cn/problem/P4779

6. AtCoder Regular Contest 084 D - Small Multiple (同余最短路)
   题目链接：https://atcoder.jp/contests/arc084/tasks/arc084_b

7. Codeforces 1063B Labyrinth (01-BFS)
   题目链接：https://codeforces.com/problemset/problem/1063/B

8. LeetCode 2290. 到达角落需要移除障碍物的最小数目 (01-BFS)
   题目链接：https://leetcode.cn/problems/minimum-obstacle-removal-to-reach-corner/

9. LeetCode 1368. 使网格图至少有一条有效路径的最小代价 (01-BFS)
   题目链接：https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/

10. CSP-J 2023 T4 旅游巴士 (同余最短路)
    题目链接：https://www.luogu.com.cn/problem/P9751

11. LibreOJ #10072. 「一本通 3.2 练习 4」新年好 (最短路)
    题目链接：https://loj.ac/p/10072

12. 洛谷 P1144 最短路计数 (最短路)
    题目链接：https://www.luogu.com.cn/problem/P1144

13. POJ 1723 SOLDIERS (类似思想)
    题目链接：http://poj.org/problem?id=1723

14. 洛谷 P2512 [HAOI2008] 糖果传递 (同余最短路)
    题目链接：https://www.luogu.com.cn/problem/P2512

15. 洛谷 P3403 跳楼机 (同余最短路)
    题目链接：https://www.luogu.com.cn/problem/P3403
"""

def test_dijkstra():
    """测试Dijkstra算法"""
    print("=== 测试Dijkstra算法 ===")
    
    # 创建测试图
    # 图结构：
    # 0 -> 1 (权值10)
    # 0 -> 2 (权值5)
    # 1 -> 2 (权值2)
    # 1 -> 3 (权值1)
    # 2 -> 1 (权值3)
    # 2 -> 3 (权值9)
    # 2 -> 4 (权值2)
    # 3 -> 4 (权值4)
    # 4 -> 0 (权值7)
    # 4 -> 3 (权值6)
    
    graph = [[] for _ in range(5)]
    graph[0].append((1, 10))
    graph[0].append((2, 5))
    graph[1].append((2, 2))
    graph[1].append((3, 1))
    graph[2].append((1, 3))
    graph[2].append((3, 9))
    graph[2].append((4, 2))
    graph[3].append((4, 4))
    graph[4].append((0, 7))
    graph[4].append((3, 6))
    
    # 测试从节点0开始的最短路径
    dist = dijkstra(graph, 0)
    print("从节点0到各节点的最短距离：")
    for i in range(len(dist)):
        print(f"到节点{i}的距离：{dist[i]}")
    
    # 预期结果：
    # 到节点0的距离：0
    # 到节点1的距离：5
    # 到节点2的距离：5
    # 到节点3的距离：6
    # 到节点4的距离：7

def dijkstra(graph, start):
    """Dijkstra算法实现"""
    n = len(graph)
    dist = [float('inf')] * n
    visited = [False] * n
    dist[start] = 0
    
    pq = [(0, start)]
    
    while pq:
        d, u = heapq.heappop(pq)
        
        if visited[u]:
            continue
        
        visited[u] = True
        
        for v, w in graph[u]:
            if not visited[v] and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                heapq.heappush(pq, (dist[v], v))
    
    return dist

def test_zero_one_bfs():
    """测试01-BFS算法"""
    print("\n=== 测试01-BFS算法 ===")
    
    # 创建测试网格
    # 0表示空地，1表示墙
    # 从(0,0)到(2,2)的最短路径
    grid = [
        [0, 0, 1],
        [1, 0, 0],
        [0, 0, 0]
    ]
    
    result = zero_one_bfs(grid)
    print(f"从(0,0)到(2,2)的最短路径长度：{result}")
    
    # 预期结果：4

def zero_one_bfs(grid):
    """01-BFS算法实现（简化版）"""
    m, n = len(grid), len(grid[0])
    dist = [[float('inf')] * n for _ in range(m)]
    visited = [[False] * n for _ in range(m)]
    
    dq = deque()
    dist[0][0] = 0
    dq.appendleft((0, 0, 0))
    
    dx = [1, -1, 0, 0]
    dy = [0, 0, 1, -1]
    
    while dq:
        x, y, d = dq.popleft()
        
        if visited[x][y]:
            continue
        
        visited[x][y] = True
        
        if x == m - 1 and y == n - 1:
            return d
        
        for i in range(4):
            nx, ny = x + dx[i], y + dy[i]
            
            if nx < 0 or nx >= m or ny < 0 or ny >= n or grid[nx][ny] == 1:
                continue
            
            cost = 1  # 假设每步代价为1
            
            if not visited[nx][ny] and d + cost < dist[nx][ny]:
                dist[nx][ny] = d + cost
                if cost == 0:
                    dq.appendleft((nx, ny, d + cost))
                else:
                    dq.append((nx, ny, d + cost))
    
    return -1

def test_modular_shortest_path():
    """测试同余最短路算法"""
    print("\n=== 测试同余最短路算法 ===")
    
    # 测试k=3的情况
    k1 = 3
    result1 = modular_shortest_path(k1)
    print(f"k={k1}时，结果：{result1}")
    
    # 测试k=7的情况
    k2 = 7
    result2 = modular_shortest_path(k2)
    print(f"k={k2}时，结果：{result2}")
    
    # 测试k=5的情况（无解）
    k3 = 5
    result3 = modular_shortest_path(k3)
    print(f"k={k3}时，结果：{result3}")
    
    # 预期结果：
    # k=3时，结果：3
    # k=7时，结果：6
    # k=5时，结果：-1

def modular_shortest_path(k):
    """同余最短路算法实现（简化版）"""
    if k == 1:
        return 1
    
    if k % 2 == 0 or k % 5 == 0:
        return -1
    
    dist = [float('inf')] * k
    visited = [False] * k
    
    dq = deque()
    dist[1] = 1
    dq.appendleft((1, 1))
    
    while dq:
        r, d = dq.popleft()
        
        if visited[r]:
            continue
        
        visited[r] = True
        
        if r == 0:
            return d
        
        new_remainder = (r * 10 + 1) % k
        if not visited[new_remainder] and d + 1 < dist[new_remainder]:
            dist[new_remainder] = d + 1
            dq.append((new_remainder, d + 1))
    
    return -1

def main():
    """主函数，运行所有测试"""
    test_dijkstra()
    test_zero_one_bfs()
    test_modular_shortest_path()

# 运行测试
if __name__ == "__main__":
    main()