"""
LOJ #121 动态图连通性 - Python实现

题目来源: LibreOJ
题目链接: https://loj.ac/p/121
题目描述: 
  支持三种操作的动态图问题：
  1. 操作 0 x y: 在点x和点y之间增加一条边
  2. 操作 1 x y: 删除点x和点y之间的边
  3. 操作 2 x y: 查询点x和点y是否连通

解题思路:
  使用线段树分治 + 可撤销并查集
  1. 将所有操作离线处理
  2. 对于每条边，记录其存在的时间区间[L,R]
  3. 将时间区间映射到线段树上
  4. DFS遍历线段树，在每个节点处处理该节点上的所有边
  5. 使用可撤销并查集维护当前的连通性
  6. 到达叶子节点时回答查询

时间复杂度: O((n + m) log m)
空间复杂度: O(n + m)

是否为最优解: 是
  这是处理动态图连通性问题的经典解法，时间复杂度已经很优秀

工程化考量:
  1. 使用sys.stdin提高输入效率
  2. 按秩合并优化并查集性能
  3. 精确回滚保证状态一致性

适用场景:
  1. 动态图连通性维护
  2. 离线处理图论问题
  3. 需要支持加边、删边操作的场景

注意事项:
  1. 可撤销并查集不能使用路径压缩，只能按秩合并
  2. 线段树分治是离线算法，不支持在线查询
  3. 每个操作的影响时间区间要正确计算
  4. 回滚操作必须与合并操作一一对应

1 <= n <= 5000
1 <= m <= 500000
不强制在线，可以离线处理
提交以下的code，可以通过所有测试用例
"""

import sys

# 点的数量最大值
MAXN = 5001
# 操作数量最大值
MAXM = 500001
# 任务数量最大值
MAXT = 5000001

n, m = 0, 0

# 操作类型op、端点u、端点v
op = [0] * MAXM
u = [0] * MAXM
v = [0] * MAXM

# last[x][y] : 点x和点y的边，上次出现的时间点
last = [[0] * MAXN for _ in range(MAXN)]

# 可撤销并查集
father = [0] * MAXN
siz = [0] * MAXN
rollback = [[0, 0] for _ in range(MAXN)]
opsize = 0

# 线段树每个区间拥有哪些任务的列表，链式前向星表示
head = [0] * (MAXM << 2)
nxt = [0] * MAXT
tox = [0] * MAXT
toy = [0] * MAXT
cnt = 0

# ans[i]为第i条操作的答案，只有查询操作才有答案
ans = [False] * MAXM

def addEdge(i, x, y):
    global cnt
    cnt += 1
    nxt[cnt] = head[i]
    tox[cnt] = x
    toy[cnt] = y
    head[i] = cnt

def find(i):
    while i != father[i]:
        i = father[i]
    return i

def union(x, y):
    global opsize
    fx = find(x)
    fy = find(y)
    if siz[fx] < siz[fy]:
        fx, fy = fy, fx
    father[fy] = fx
    siz[fx] += siz[fy]
    opsize += 1
    rollback[opsize][0] = fx
    rollback[opsize][1] = fy

def undo():
    global opsize
    fx = rollback[opsize][0]
    fy = rollback[opsize][1]
    opsize -= 1
    father[fy] = fy
    siz[fx] -= siz[fy]

def add_range(jobl, jobr, jobx, joby, l, r, i):
    if jobl <= l and r <= jobr:
        addEdge(i, jobx, joby)
    else:
        mid = (l + r) >> 1
        if jobl <= mid:
            add_range(jobl, jobr, jobx, joby, l, mid, i << 1)
        if jobr > mid:
            add_range(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1)

def dfs(l, r, i):
    unionCnt = 0
    ei = head[i]
    while ei > 0:
        x = tox[ei]
        y = toy[ei]
        fx = find(x)
        fy = find(y)
        if fx != fy:
            union(fx, fy)
            unionCnt += 1
        ei = nxt[ei]
    
    if l == r:
        if op[l] == 2:
            ans[l] = find(u[l]) == find(v[l])
    else:
        mid = (l + r) // 2
        dfs(l, mid, i << 1)
        dfs(mid + 1, r, i << 1 | 1)
    
    for j in range(1, unionCnt + 1):
        undo()

def prepare():
    for i in range(1, n + 1):
        father[i] = i
        siz[i] = 1
    
    for i in range(1, m + 1):
        t = op[i]
        x = u[i]
        y = v[i]
        if t == 0:
            last[x][y] = i
        elif t == 1:
            add_range(last[x][y], i - 1, x, y, 1, m, 1)
            last[x][y] = 0
    
    for x in range(1, n + 1):
        for y in range(x + 1, n + 1):
            if last[x][y] != 0:
                add_range(last[x][y], m, x, y, 1, m, 1)

def main():
    global n, m
    line = sys.stdin.readline().split()
    n = int(line[0])
    m = int(line[1])
    
    for i in range(1, m + 1):
        line = sys.stdin.readline().split()
        t = int(line[0])
        x = int(line[1])
        y = int(line[2])
        op[i] = t
        u[i] = min(x, y)
        v[i] = max(x, y)
    
    prepare()
    dfs(1, m, 1)
    
    for i in range(1, m + 1):
        if op[i] == 2:
            if ans[i]:
                print("Y")
            else:
                print("N")

if __name__ == "__main__":
    main()