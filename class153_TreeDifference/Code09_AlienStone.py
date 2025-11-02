# 异象石 (LOJ 10132)
# 题目描述：
# 在一个圆上有n个点，按顺时针编号为0到n-1。
# 有m次操作，每次操作会在两个点之间连一条弦。
# 每次操作后，求所有弦将圆分割成多少个区域。
# 测试链接 : https://loj.ac/problem/10132

import sys
from collections import defaultdict

# 读取输入
input = sys.stdin.read
tokens = input().split()

# 读取节点数量
n = int(tokens[0])

# 读取树的边
edges = []
idx = 1
for i in range(n - 1):
    u = int(tokens[idx])
    v = int(tokens[idx + 1])
    edges.append((u, v))
    idx += 2

# 构建邻接表表示的树
tree = defaultdict(list)
for u, v in edges:
    tree[u].append(v)
    tree[v].append(u)

# 深度数组和父节点数组
deep = [0] * (n + 1)
parent = [0] * (n + 1)

# 倍增数组的大小限制
LIMIT = 17
# 倍增跳跃数组，用于LCA计算
stjump = [[0] * LIMIT for _ in range(n + 1)]

# dfs序数组
dfn = [0] * (n + 1)
dfn2 = [0] * (n + 1)
dfc = 0

# 子树大小数组
size = [0] * (n + 1)

# 被选中的点集合
chosen = set()

# 计算log2(n)的函数
def log2(n):
    ans = 0
    while (1 << ans) <= (n >> 1):
        ans += 1
    return ans

power = log2(n)

# 第一次DFS，预处理深度、dfs序和子树大小
def dfs1(u, f):
    global dfc
    deep[u] = deep[f] + 1
    parent[u] = f
    stjump[u][0] = f
    dfc += 1
    dfn[u] = dfc
    size[u] = 1
    # 预处理倍增数组
    for p in range(1, power + 1):
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1]
    # 遍历所有子节点
    for v in tree[u]:
        if v != f:
            dfs1(v, u)
            size[u] += size[v]
    dfn2[u] = dfc

# 计算最近公共祖先(LCA)
def lca(a, b):
    # 确保a的深度不小于b
    if deep[a] < deep[b]:
        a, b = b, a
    # 将a向上跳到与b同一深度
    for p in range(power, -1, -1):
        if deep[stjump[a][p]] >= deep[b]:
            a = stjump[a][p]
    # 如果a和b在同一位置，说明b是a的祖先
    if a == b:
        return a
    # 同时向上跳，直到找到最近公共祖先
    for p in range(power, -1, -1):
        if stjump[a][p] != stjump[b][p]:
            a = stjump[a][p]
            b = stjump[b][p]
    return stjump[a][0]

# 计算两点间的距离
def dis(a, b):
    l = lca(a, b)
    return deep[a] + deep[b] - 2 * deep[l]

# 预处理深度、dfs序和子树大小
dfs1(1, 0)

# 读取操作数量
m = int(tokens[idx])
idx += 1

# 处理每次操作
for i in range(m):
    op = tokens[idx]
    idx += 1
    
    if op == "+":
        x = int(tokens[idx])
        idx += 1
        chosen.add(x)
    elif op == "-":
        x = int(tokens[idx])
        idx += 1
        chosen.discard(x)
    else:  # op == "?"
        if len(chosen) <= 1:
            print(len(chosen))
        else:
            # 构建虚树并计算答案
            nodes = list(chosen)
            nodes.sort(key=lambda x: dfn[x])
            
            # 构建虚树
            stack = [1]
            ans = 0
            
            for u in nodes:
                if len(stack) == 1:
                    stack.append(u)
                else:
                    l = lca(stack[-1], u)
                    while len(stack) > 1 and deep[stack[-1]] > deep[l]:
                        if deep[stack[-2]] <= deep[l]:
                            ans += dis(stack[-1], l)
                            stack[-1] = l
                            break
                        else:
                            ans += dis(stack[-1], stack[-2])
                            stack.pop()
                    if stack[-1] != u:
                        stack.append(u)
            
            while len(stack) > 1:
                ans += dis(stack[-1], stack[-2])
                stack.pop()
            
            print(ans // 2 + 1)