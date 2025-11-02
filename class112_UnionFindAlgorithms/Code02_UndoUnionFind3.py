# 可撤销并查集模版题，Python版
# 一共有n个点，每个点有两个小球，每个点给定两个小球的编号
# 一共有n-1条无向边，所有节点连成一棵树
# 对i号点，2 <= i <= n，都计算如下问题的答案并打印
# 从1号点到i号点的最短路径上，每个点只能拿一个小球，最多能拿几个编号不同的小球
# 1 <= n <= 2 * 10^5
# 测试链接 : https://www.luogu.com.cn/problem/AT_abc302_h
# 测试链接 : https://atcoder.jp/contests/abc302/tasks/abc302_h
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

# 补充题目：
# 1. AtCoder ABC302 H - Ball Collector
#    链接：https://atcoder.jp/contests/abc302/tasks/abc302_h
#    题目大意：在一棵树上，每个节点有两个球，要求从根节点到每个节点的路径上收集球，使得收集的球编号各不相同
#    解题思路：使用可撤销并查集维护连通性，在DFS过程中合并节点，回溯时撤销操作
#    时间复杂度：O(n log n)
#    空间复杂度：O(n)

# 2. Codeforces 1681F - Unique Occurrences
#    链接：https://codeforces.com/problemset/problem/1681/F
#    题目大意：在树上处理路径查询问题，统计某些路径上唯一出现的颜色数量
#    解题思路：可以使用可撤销并查集维护路径的连通性信息
#    时间复杂度：O(n log n)
#    空间复杂度：O(n)

import sys
from typing import List, Tuple

class UndoUnionFind:
    def __init__(self, n: int):
        self.MAXN = n + 1
        self.arr = [[0, 0] for _ in range(self.MAXN)]
        self.head = [0] * self.MAXN
        self.next = [0] * (self.MAXN << 1)
        self.to = [0] * (self.MAXN << 1)
        self.cnt = 0
        self.father = [0] * self.MAXN
        self.siz = [0] * self.MAXN
        self.edgeCnt = [0] * self.MAXN
        self.rollback = [[0, 0] for _ in range(self.MAXN)]
        self.opsize = 0
        self.ans = [0] * self.MAXN
        self.ball = 0

    def addEdge(self, u: int, v: int) -> None:
        self.cnt += 1
        self.next[self.cnt] = self.head[u]
        self.to[self.cnt] = v
        self.head[u] = self.cnt

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
        self.edgeCnt[fx] += self.edgeCnt[fy] + 1
        self.opsize += 1
        self.rollback[self.opsize][0] = fx
        self.rollback[self.opsize][1] = fy

    def undo(self) -> None:
        fx = self.rollback[self.opsize][0]
        fy = self.rollback[self.opsize][1]
        self.opsize -= 1
        self.father[fy] = fy
        self.siz[fx] -= self.siz[fy]
        self.edgeCnt[fx] -= self.edgeCnt[fy] + 1

    def dfs(self, u: int, fa: int) -> None:
        fx = self.find(self.arr[u][0])
        fy = self.find(self.arr[u][1])
        added = False
        unioned = False
        if fx == fy:
            if self.edgeCnt[fx] < self.siz[fx]:
                self.ball += 1
                added = True
            self.edgeCnt[fx] += 1
        else:
            if self.edgeCnt[fx] < self.siz[fx] or self.edgeCnt[fy] < self.siz[fy]:
                self.ball += 1
                added = True
            self.union(fx, fy)
            unioned = True
            
        self.ans[u] = self.ball
        
        e = self.head[u]
        while e > 0:
            if self.to[e] != fa:
                self.dfs(self.to[e], u)
            e = self.next[e]
            
        if added:
            self.ball -= 1
        if unioned:
            self.undo()
        else:
            self.edgeCnt[fx] -= 1

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    uf = UndoUnionFind(n)
    
    idx = 1
    for i in range(1, n + 1):
        uf.arr[i][0] = int(data[idx])
        idx += 1
        uf.arr[i][1] = int(data[idx])
        idx += 1
    
    for i in range(1, n):
        u = int(data[idx])
        idx += 1
        v = int(data[idx])
        idx += 1
        uf.addEdge(u, v)
        uf.addEdge(v, u)
    
    for i in range(1, n + 1):
        uf.father[i] = i
        uf.siz[i] = 1
        uf.edgeCnt[i] = 0
    
    uf.dfs(1, 0)
    
    result = []
    for i in range(2, n + 1):
        result.append(str(uf.ans[i]))
    
    print(' '.join(result))

if __name__ == "__main__":
    main()