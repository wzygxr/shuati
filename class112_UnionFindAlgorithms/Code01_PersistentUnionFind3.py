# 可持久化并查集模版题，Python版
# 数字从1到n，一开始每个数字所在的集合只有自己
# 实现如下三种操作，第i条操作发生后，所有数字的状况记为i版本，操作一共发生m次
# 操作 1 x y : 基于上个操作生成的版本，将x的集合与y的集合合并，生成当前的版本
# 操作 2 x   : 拷贝第x号版本的状况，生成当前的版本
# 操作 3 x y : 拷贝上个操作生成的版本，生成当前的版本，查询x和y是否属于一个集合
# 1 <= n <= 10^5
# 1 <= m <= 2 * 10^5
# 测试链接 : https://www.luogu.com.cn/problem/P3402
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

# 补充题目：
# 1. 洛谷 P3402 - 可持久化并查集（模板题）
#    链接：https://www.luogu.com.cn/problem/P3402
#    题目大意：实现支持版本回退的并查集，支持合并、查询和回退操作
#    解题思路：使用主席树维护可持久化数组，实现可持久化并查集
#    时间复杂度：O(m log^2 n)
#    空间复杂度：O(n log n)

# 2. NOI 2018 - 归程
#    链接：https://www.luogu.com.cn/problem/P4768
#    题目大意：在一张图上进行多次询问，每次询问从某点开始，通过特定条件到达另一点的最短路径
#    解题思路：可以使用可持久化并查集维护不同条件下的连通性，结合最短路算法解决
#    时间复杂度：O((n + m) log n)
#    空间复杂度：O(n log n)

import sys
from typing import List

class PersistentUnionFind:
    def __init__(self, n: int, m: int):
        self.MAXM = m + 1
        self.MAXT = 8000001
        self.n = n
        self.m = m
        
        # rootfa[i] = j，表示father数组，i版本的头节点编号为j
        self.rootfa = [0] * self.MAXM
        
        # rootsiz[i] = j，表示siz数组，i版本的头节点编号为j
        self.rootsiz = [0] * self.MAXM
        
        # 可持久化father数组和可持久化siz数组，共用一个ls、rs、val
        # 因为可持久化时，分配的节点编号不同，所以可以共用
        self.ls = [0] * self.MAXT
        self.rs = [0] * self.MAXT
        self.val = [0] * self.MAXT
        self.cnt = 0

    # 建立可持久化的father数组
    def buildfa(self, l: int, r: int) -> int:
        rt = self.cnt + 1
        self.cnt += 1
        if l == r:
            self.val[rt] = l
        else:
            mid = (l + r) // 2
            self.ls[rt] = self.buildfa(l, mid)
            self.rs[rt] = self.buildfa(mid + 1, r)
        return rt

    # 建立可持久化的siz数组
    def buildsiz(self, l: int, r: int) -> int:
        rt = self.cnt + 1
        self.cnt += 1
        if l == r:
            self.val[rt] = 1
        else:
            mid = (l + r) // 2
            self.ls[rt] = self.buildsiz(l, mid)
            self.rs[rt] = self.buildsiz(mid + 1, r)
        return rt

    # 来自讲解157，题目1，修改数组中一个位置的值，生成新版本的数组
    # 如果i属于可持久化father数组的节点，那么修改的就是father数组
    # 如果i属于可持久化siz数组的节点，那么修改的就是siz数组
    def update(self, jobi: int, jobv: int, l: int, r: int, i: int) -> int:
        rt = self.cnt + 1
        self.cnt += 1
        self.ls[rt] = self.ls[i]
        self.rs[rt] = self.rs[i]
        if l == r:
            self.val[rt] = jobv
        else:
            mid = (l + r) // 2
            if jobi <= mid:
                self.ls[rt] = self.update(jobi, jobv, l, mid, self.ls[rt])
            else:
                self.rs[rt] = self.update(jobi, jobv, mid + 1, r, self.rs[rt])
        return rt

    # 来自讲解157，题目1，查询数组中一个位置的值
    # 如果i属于可持久化father数组的节点，那么查询的就是father数组
    # 如果i属于可持久化siz数组的节点，那么查询的就是siz数组
    def query(self, jobi: int, l: int, r: int, i: int) -> int:
        if l == r:
            return self.val[i]
        mid = (l + r) // 2
        if jobi <= mid:
            return self.query(jobi, l, mid, self.ls[i])
        else:
            return self.query(jobi, mid + 1, r, self.rs[i])

    # 基于v版本，查询x的集合头节点，不做扁平化
    def find(self, x: int, v: int) -> int:
        fa = self.query(x, 1, self.n, self.rootfa[v])
        while x != fa:
            x = fa
            fa = self.query(x, 1, self.n, self.rootfa[v])
        return x

    # v版本已经拷贝了v-1版本，合并x所在的集合和y所在的集合，去更新v版本
    def union(self, x: int, y: int, v: int) -> None:
        fx = self.find(x, v)
        fy = self.find(y, v)
        if fx != fy:
            xsiz = self.query(fx, 1, self.n, self.rootsiz[v])
            ysiz = self.query(fy, 1, self.n, self.rootsiz[v])
            if xsiz >= ysiz:
                self.rootfa[v] = self.update(fy, fx, 1, self.n, self.rootfa[v])
                self.rootsiz[v] = self.update(fx, xsiz + ysiz, 1, self.n, self.rootsiz[v])
            else:
                self.rootfa[v] = self.update(fx, fy, 1, self.n, self.rootfa[v])
                self.rootsiz[v] = self.update(fy, xsiz + ysiz, 1, self.n, self.rootsiz[v])

def main():
    import sys
    sys.setrecursionlimit(1000000)
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    uf = PersistentUnionFind(n, m)
    uf.rootfa[0] = uf.buildfa(1, n)
    uf.rootsiz[0] = uf.buildsiz(1, n)
    
    idx = 2
    for v in range(1, m + 1):
        op = int(data[idx])
        idx += 1
        uf.rootfa[v] = uf.rootfa[v - 1]
        uf.rootsiz[v] = uf.rootsiz[v - 1]
        if op == 1:
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            uf.union(x, y, v)
        elif op == 2:
            x = int(data[idx])
            idx += 1
            uf.rootfa[v] = uf.rootfa[x]
            uf.rootsiz[v] = uf.rootsiz[x]
        else:
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            if uf.find(x, v) == uf.find(y, v):
                print(1)
            else:
                print(0)

if __name__ == "__main__":
    main()