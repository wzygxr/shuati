# 同在最小生成树里，Python版
# 一共有n个点，m条无向边，每条边有边权，图保证是连通的
# 一共有q次查询，每条查询都给定参数k，表示该查询涉及k条边
# 然后依次给出k条边的编号，打印这k条边能否同时出现在一颗最小生成树上
# 1 <= n、m、q、所有查询涉及边的总量 <= 5 * 10^5
# 测试链接 : https://www.luogu.com.cn/problem/CF891C
# 测试链接 : https://codeforces.com/problemset/problem/891/C
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

# 补充题目：
# 1. Codeforces 891C - Envy
#    链接：https://codeforces.com/problemset/problem/891/C
#    题目大意：给定一个图和一些边的集合，判断这些边是否可以同时出现在一个最小生成树中
#    解题思路：使用可撤销并查集，按照Kruskal算法的思想，先加入权重小于当前查询边的边，
#              然后尝试加入查询的边，如果会形成环则不能同时出现在MST中
#    时间复杂度：O(m log m + q * k * log n)
#    空间复杂度：O(n + m)

# 2. Codeforces 1291F - Coffee Varieties (hard version)
#    链接：https://codeforces.com/problemset/problem/1291/F
#    题目大意：交互题，需要通过特定操作识别咖啡品种
#    解题思路：可以使用可撤销并查集维护品种的等价关系
#    时间复杂度：O(n log n)
#    空间复杂度：O(n)

import sys
from typing import List

class EnvyUnionFind:
    def __init__(self, n: int):
        self.MAXN = n + 1
        self.n = n
        self.father = [0] * self.MAXN
        self.siz = [0] * self.MAXN
        self.rollback = [[0, 0] for _ in range(self.MAXN << 1)]
        self.opsize = 0

    def find(self, i: int) -> int:
        while i != self.father[i]:
            i = self.father[i]
        return i

    def union(self, x: int, y: int) -> None:
        fx = self.find(x)
        fy = self.find(y)
        if self.siz[fx] < self.siz[fy]:
            fx, fy = fy, fx
        self.father[fy] = fx
        self.siz[fx] += self.siz[fy]
        self.opsize += 1
        self.rollback[self.opsize][0] = fx
        self.rollback[self.opsize][1] = fy

    def undo(self) -> None:
        fx = self.rollback[self.opsize][0]
        fy = self.rollback[self.opsize][1]
        self.opsize -= 1
        self.father[fy] = fy
        self.siz[fx] -= self.siz[fy]

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    # 节点u、节点v、边权w
    edge = [[0, 0, 0] for _ in range(m + 1)]
    # 节点u、节点v、边权w、问题编号i
    queries = [[0, 0, 0, 0] for _ in range(m + 1)]  # 使用m+1作为最大可能的查询边数
    
    idx = 2
    for i in range(1, m + 1):
        edge[i][0] = int(data[idx])
        idx += 1
        edge[i][1] = int(data[idx])
        idx += 1
        edge[i][2] = int(data[idx])
        idx += 1
    
    q = int(data[idx])
    idx += 1
    
    k = 0
    query_edges = []  # 临时存储查询的边
    
    for i in range(1, q + 1):
        s = int(data[idx])
        idx += 1
        for j in range(1, s + 1):
            ei = int(data[idx])
            idx += 1
            query_edges.append([edge[ei][0], edge[ei][1], edge[ei][2], i])
    
    # 对边按权重排序
    edge[1:m+1] = sorted(edge[1:m+1], key=lambda x: x[2])
    
    # 对查询边按权重和问题编号排序
    query_edges.sort(key=lambda x: (x[2], x[3]))
    
    # 初始化并查集
    uf = EnvyUnionFind(n)
    for i in range(1, n + 1):
        uf.father[i] = i
        uf.siz[i] = 1
    
    # 答案数组
    ans = [True] * (q + 1)
    
    ei = 1
    k = len(query_edges)
    
    l = 0
    while l < k:
        r = l
        # 找到权重相同的边组
        while r + 1 < k and query_edges[l][2] == query_edges[r + 1][2] and query_edges[l][3] == query_edges[r + 1][3]:
            r += 1
        
        # 添加小于当前边权的边，利用Kruskal算法增加连通性，ei是不回退的
        while ei <= m and edge[ei][2] < query_edges[l][2]:
            if uf.find(edge[ei][0]) != uf.find(edge[ei][1]):
                uf.union(edge[ei][0], edge[ei][1])
            ei += 1
        
        queryId = query_edges[l][3]
        if not ans[queryId]:
            l = r + 1
            continue
        
        unionCnt = 0
        for i in range(l, r + 1):
            if uf.find(query_edges[i][0]) == uf.find(query_edges[i][1]):
                ans[queryId] = False
                break
            else:
                uf.union(query_edges[i][0], query_edges[i][1])
                unionCnt += 1
        
        for i in range(unionCnt):
            uf.undo()
        
        l = r + 1
    
    # 输出结果
    for i in range(1, q + 1):
        if ans[i]:
            print("YES")
        else:
            print("NO")

if __name__ == "__main__":
    main()