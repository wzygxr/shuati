# 团建，Python版
# 一共有n个人，每个人给定组号，一共有m条边，代表两人之间有矛盾
# 一共有k个小组，可能有的组没人，但是组依然存在
# 假设组a和组b，两个组的人一起去团建，组a和组b的所有人，可以重新打乱
# 如果所有人最多分成两个集团，每人都要参加划分，并且每个集团的内部不存在矛盾
# 那么组a和组b就叫做一个"合法组对"，注意，组b和组a就不用重复计算了
# 一共有k个组，随意选两个组的情况很多，计算一共有多少个"合法组对"
# 1 <= n、m、k <= 5 * 10^5
# 测试链接 : https://www.luogu.com.cn/problem/CF1444C
# 测试链接 : https://codeforces.com/problemset/problem/1444/C
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

# 补充题目：
# 1. Codeforces 1444C - Team Building
#    链接：https://codeforces.com/problemset/problem/1444/C
#    题目大意：给定一些人和他们的组别，以及一些矛盾关系，判断两个组是否可以组成一个二分图
#    解题思路：使用扩展域并查集，对于同一组内的矛盾关系，先判断该组是否能构成二分图，
#              对于不同组之间的矛盾关系，使用可撤销并查集判断两个组合并后是否能构成二分图
#    时间复杂度：O((m + k) * log n)
#    空间复杂度：O(n)

# 2. 洛谷 P2024 - 食物链（经典种类并查集）
#    链接：https://www.luogu.com.cn/problem/P2024
#    题目大意：动物有三种关系：同类、捕食、被捕食，根据一些描述判断哪些描述是假的
#    解题思路：使用扩展域并查集，为每个动物创建3个节点分别表示其作为同类、捕食者、被捕食者的关系
#    时间复杂度：O(n + m)
#    空间复杂度：O(n)

# 3. HDU 3038 - How Many Answers Are Wrong
#    链接：http://acm.hdu.edu.cn/showproblem.php?pid=3038
#    题目大意：给出一些区间和的描述，判断哪些描述是错误的
#    解题思路：使用扩展域并查集维护前缀和关系，将区间和转化为前缀和的差
#    时间复杂度：O(n + m)
#    空间复杂度：O(n)

# 4. AtCoder ABC126 F - XOR Matching
#    链接：https://atcoder.jp/contests/abc126/tasks/abc126_f
#    题目大意：构造满足特定异或条件的序列
#    解题思路：可以使用扩展域并查集处理异或关系
#    时间复杂度：O(2^m)
#    空间复杂度：O(2^m)

import sys
from typing import List

class TeamBuildingUnionFind:
    def __init__(self, n: int):
        self.MAXN = n + 1
        self.n = n
        self.father = [0] * (self.MAXN << 1)
        self.siz = [0] * (self.MAXN << 1)
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
    k = int(data[2])
    
    # 每个节点的组号
    team = [0] * (n + 1)
    # 每条边有两个端点
    edge = [[0, 0] for _ in range(m + 1)]
    
    # 两个端点为不同组的边，u、uteam、v、vteam
    crossEdge = [[0, 0, 0, 0] for _ in range(m + 1)]
    # 两个端点为不同组的边的数量
    cnt = 0
    
    # conflict[i] = true，表示i号组自己去划分二分图，依然有冲突
    # conflict[i] = false，表示i号组自己去划分二分图，没有冲突
    conflict = [False] * (k + 1)
    
    idx = 3
    for i in range(1, n + 1):
        team[i] = int(data[idx])
        idx += 1
    
    for i in range(1, m + 1):
        edge[i][0] = int(data[idx])
        idx += 1
        edge[i][1] = int(data[idx])
        idx += 1
    
    # 初始化并查集
    uf = TeamBuildingUnionFind(n)
    for i in range(1, (n << 1) + 1):
        uf.father[i] = i
        uf.siz[i] = 1
    
    # 处理边
    for i in range(1, m + 1):
        u = edge[i][0]
        v = edge[i][1]
        if team[u] < team[v]:
            cnt += 1
            crossEdge[cnt][0] = u
            crossEdge[cnt][1] = team[u]
            crossEdge[cnt][2] = v
            crossEdge[cnt][3] = team[v]
        elif team[u] > team[v]:
            cnt += 1
            crossEdge[cnt][0] = v
            crossEdge[cnt][1] = team[v]
            crossEdge[cnt][2] = u
            crossEdge[cnt][3] = team[u]
        else:
            if conflict[team[u]]:
                continue
            if uf.find(u) == uf.find(v):
                k -= 1
                conflict[team[u]] = True
            else:
                uf.union(u, v + n)
                uf.union(v, u + n)
    
    # 对crossEdge按组号排序
    crossEdge[1:cnt+1] = sorted(crossEdge[1:cnt+1], key=lambda x: (x[1], x[3]))
    
    ans = k * (k - 1) // 2
    l = 1
    while l <= cnt:
        uteam = crossEdge[l][1]
        vteam = crossEdge[l][3]
        
        # 找到相同组对的所有边
        r = l
        while r + 1 <= cnt and crossEdge[r + 1][1] == uteam and crossEdge[r + 1][3] == vteam:
            r += 1
        
        if conflict[uteam] or conflict[vteam]:
            l = r + 1
            continue
        
        unionCnt = 0
        conflict_found = False
        for i in range(l, r + 1):
            u = crossEdge[i][0]
            v = crossEdge[i][2]
            if uf.find(u) == uf.find(v):
                ans -= 1
                conflict_found = True
                break
            else:
                uf.union(u, v + n)
                uf.union(v, u + n)
                unionCnt += 2
        
        # 撤销操作
        for i in range(unionCnt):
            uf.undo()
        
        l = r + 1
    
    print(ans)

if __name__ == "__main__":
    main()