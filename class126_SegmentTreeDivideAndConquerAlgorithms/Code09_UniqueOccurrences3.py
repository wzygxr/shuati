"""
Codeforces 1681F Unique Occurrences - Python实现

题目来源: Codeforces
题目链接: https://codeforces.com/problemset/problem/1681/F
洛谷链接: https://www.luogu.com.cn/problem/CF1681F
题目描述: 
  给定一棵n个节点的树，每条边有一个颜色值
  定义f(u, v)为点u到点v的简单路径上恰好出现一次的颜色的数量
  求∑(u = 1..n) ∑(v = u + 1..n) f(u, v) 的结果

解题思路:
  使用线段树分治 + 可撤销并查集
  1. 对于每种颜色，找出所有该颜色的边
  2. 对于每种颜色c，将其作为"不存在"的颜色处理
  3. 将颜色不存在的时间区间映射到线段树上
  4. DFS遍历时，计算各个连通块之间的贡献

时间复杂度: O((n + m) log n)
空间复杂度: O(n + m)

是否为最优解: 是
  这是处理树上路径颜色计数问题的高效解法

工程化考量:
  1. 使用sys.stdin提高输入效率
  2. 按秩合并优化并查集性能
  3. 精确回滚保证状态一致性

适用场景:
  1. 树上路径颜色计数问题
  2. 离线处理树论问题
  3. 需要统计恰好出现一次元素的场景

注意事项:
  1. 可撤销并查集不能使用路径压缩，只能按秩合并
  2. 线段树分治是离线算法
  3. 需要正确处理颜色不存在的时间区间

1 <= 颜色值 <= n <= 2 * 10^5
提交以下的code，可以通过所有测试用例
"""

import sys

MAXN = 500001
MAXT = 10000001
n, v = 0, 0

father = [0] * MAXN
siz = [0] * MAXN
rollback = [[0, 0] for _ in range(MAXN)]
opsize = 0

# 每种颜色拥有哪些边的列表
headc = [0] * MAXN
nextc = [0] * MAXN
xc = [0] * MAXN
yc = [0] * MAXN
cntc = 0

# 颜色轴线段树的区间任务列表
headt = [0] * (MAXN << 2)
nextt = [0] * MAXT
xt = [0] * MAXT
yt = [0] * MAXT
cntt = 0

ans = 0

def addEdgeC(i, x, y):
    global cntc
    cntc += 1
    nextc[cntc] = headc[i]
    xc[cntc] = x
    yc[cntc] = y
    headc[i] = cntc

def addEdgeS(i, x, y):
    global cntt
    cntt += 1
    nextt[cntt] = headt[i]
    xt[cntt] = x
    yt[cntt] = y
    headt[i] = cntt

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
        addEdgeS(i, jobx, joby)
    else:
        mid = (l + r) >> 1
        if jobl <= mid:
            add_range(jobl, jobr, jobx, joby, l, mid, i << 1)
        if jobr > mid:
            add_range(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1)

def dfs(l, r, i):
    global ans
    unionCnt = 0
    ei = headt[i]
    while ei > 0:
        union(xt[ei], yt[ei])
        unionCnt += 1
        ei = nextt[ei]
    
    if l == r:
        ei = headc[l]
        while ei > 0:
            fx = find(xc[ei])
            fy = find(yc[ei])
            ans += siz[fx] * siz[fy]
            ei = nextc[ei]
    else:
        mid = (l + r) >> 1
        dfs(l, mid, i << 1)
        dfs(mid + 1, r, i << 1 | 1)
    
    for k in range(1, unionCnt + 1):
        undo()

def main():
    global n, v
    line = sys.stdin.readline().split()
    n = int(line[0])
    v = n
    
    for i in range(1, n):
        line = sys.stdin.readline().split()
        x = int(line[0])
        y = int(line[1])
        c = int(line[2])
        addEdgeC(c, x, y)
        if c > 1:
            add_range(1, c - 1, x, y, 1, v, 1)
        if c < v:
            add_range(c + 1, v, x, y, 1, v, 1)
    
    for i in range(1, n + 1):
        father[i] = i
        siz[i] = 1
    
    dfs(1, v, 1)
    print(ans)

if __name__ == "__main__":
    main()