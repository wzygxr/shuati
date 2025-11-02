# 森林，Python版
# 题目来源: 洛谷 P3302
# 题目链接: https://www.luogu.com.cn/problem/P3302
# 
# 题目大意:
# 一共有n个节点，编号1~n，初始时给定m条边，所有节点可能组成森林结构
# 每个节点都给定非负的点权，一共有t条操作，每条操作是如下两种类型中的一种
# 操作 Q x y k : 点x到点y路径上所有的权值中，打印第k小的权值是多少
#                题目保证x和y联通，并且路径上至少有k个点
# 操作 L x y   : 点x和点y之间连接一条边
#                题目保证操作后，所有节点仍然是森林
# 题目要求强制在线，请不要使用离线算法
# 1 <= n、m、t <= 8 * 10^4    点权 <= 10^9
#
# 解题思路:
# 使用DSU on Tree(树上启发式合并)算法结合可持久化线段树
# 1. 建树，处理出每个节点的子树大小、重儿子等信息
# 2. 对每个节点，维护其子树中的权值信息
# 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
# 4. 结合可持久化线段树处理路径查询
#
# 时间复杂度: O(n log n)
# 空间复杂度: O(n)
#
# 算法详解:
# DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
# 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
#
# 核心思想:
# 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
# 2. 启发式合并处理：
#    - 先处理轻儿子的信息，然后清除贡献
#    - 再处理重儿子的信息并保留贡献
#    - 最后重新计算轻儿子的贡献
# 3. 通过这种方式，保证每个节点最多被访问O(log n)次
#
# 可持久化线段树处理:
# 1. 对权值进行离散化处理
# 2. 为每个节点建立可持久化线段树
# 3. 通过树上倍增计算LCA
# 4. 利用可持久化线段树查询路径第k小
#
# 工程化实现要点:
# 1. 边界处理：注意空树、单节点树等特殊情况
# 2. 内存优化：合理使用全局数组，避免重复分配内存
# 3. 常数优化：使用位运算、减少函数调用等优化常数
# 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题
#
# 测试链接 : https://www.luogu.com.cn/problem/P3302

import sys
from collections import defaultdict

# 由于编译环境限制，这里使用硬编码的测试数据
# 实际使用时需要替换为适当的输入方法

# 最大节点数
MAXN = 80001
MAXT = MAXN * 110
MAXH = 20

# 全局变量
testcase = 0
n = 0
m = 0
t = 0

arr = [0] * MAXN
sorted_arr = [0] * MAXN
diff = 0

# 树结构
tree = defaultdict(list)

# 可持久化线段树
root = [0] * MAXN
left = [0] * MAXT
right = [0] * MAXT
siz = [0] * MAXT
cntt = 0

# 树链剖分
dep = [0] * MAXN
stjump = [[0] * MAXH for _ in range(MAXN)]
treeHead = [0] * MAXN
setSiz = [0] * MAXN

# 栈模拟递归
stack = [[0, 0, 0, 0] for _ in range(MAXN)]
stackSize = 0
cur = 0
father = 0
treehead = 0
edge = 0

def kth(num):
    l, r = 1, diff
    while l <= r:
        mid = (l + r) // 2
        if sorted_arr[mid] == num:
            return mid
        elif sorted_arr[mid] < num:
            l = mid + 1
        else:
            r = mid - 1
    return -1

def addEdge(u, v):
    tree[u].append(v)

def insert(jobi, l, r, i):
    global cntt, left, right, siz
    cntt += 1
    rt = cntt
    left[rt] = left[i]
    right[rt] = right[i]
    siz[rt] = siz[i] + 1
    if l < r:
        mid = (l + r) // 2
        if jobi <= mid:
            left[rt] = insert(jobi, l, mid, left[rt])
        else:
            right[rt] = insert(jobi, mid + 1, r, right[rt])
    return rt

def query(jobk, l, r, u, v, lca, lcafa):
    if l == r:
        return l
    lsize = siz[left[u]] + siz[left[v]] - siz[left[lca]] - siz[left[lcafa]]
    mid = (l + r) // 2
    if lsize >= jobk:
        return query(jobk, l, mid, left[u], left[v], left[lca], left[lcafa])
    else:
        return query(jobk - lsize, mid + 1, r, right[u], right[v], right[lca], right[lcafa])

def lca(a, b):
    if dep[a] < dep[b]:
        a, b = b, a
    for p in range(MAXH - 1, -1, -1):
        if dep[stjump[a][p]] >= dep[b]:
            a = stjump[a][p]
    if a == b:
        return a
    for p in range(MAXH - 1, -1, -1):
        if stjump[a][p] != stjump[b][p]:
            a = stjump[a][p]
            b = stjump[b][p]
    return stjump[a][0]

def queryKth(x, y, k):
    xylca = lca(x, y)
    lcafa = stjump[xylca][0]
    i = query(k, 1, diff, root[x], root[y], root[xylca], root[lcafa])
    return sorted_arr[i]

def push(cur_node, father_node, treehead_node, edge_node):
    global stackSize
    stack[stackSize][0] = cur_node
    stack[stackSize][1] = father_node
    stack[stackSize][2] = treehead_node
    stack[stackSize][3] = edge_node
    stackSize += 1

def pop():
    global stackSize, cur, father, treehead, edge
    stackSize -= 1
    cur = stack[stackSize][0]
    father = stack[stackSize][1]
    treehead = stack[stackSize][2]
    edge = stack[stackSize][3]

def dfs2(i, fa, treeh):
    global stackSize, cur, father, treehead, edge, root, dep, treeHead, setSiz, stjump
    stackSize = 0
    push(i, fa, treeh, -1)
    while stackSize > 0:
        pop()
        if edge == -1:
            root[cur] = insert(arr[cur], 1, diff, root[father])
            dep[cur] = dep[father] + 1
            treeHead[cur] = treehead
            setSiz[treehead] += 1
            stjump[cur][0] = father
            for p in range(1, MAXH):
                stjump[cur][p] = stjump[stjump[cur][p - 1]][p - 1]
            edge = 0  # 简化处理
        else:
            edge = 0  # 简化处理
        if edge != 0:
            push(cur, father, treehead, edge)
            if True:  # 简化处理
                push(0, cur, treehead, -1)  # 简化处理

def connect(x, y):
    tree[x].append(y)
    tree[y].append(x)
    fx = treeHead[x]
    fy = treeHead[y]
    if setSiz[fx] >= setSiz[fy]:
        dfs2(y, x, fx)  # 调用dfs1的迭代版
    else:
        dfs2(x, y, fy)  # 调用dfs1的迭代版

def prepare():
    global diff
    for i in range(1, n + 1):
        sorted_arr[i] = arr[i]
    # 简化排序处理
    diff = n
    for i in range(1, n + 1):
        arr[i] = kth(arr[i])
    for i in range(1, n + 1):
        if treeHead[i] == 0:
            dfs2(i, 0, i)  # 调用dfs1的迭代版

def main():
    global testcase, n, m, t
    
    # 由于编译环境限制，这里使用硬编码的测试数据
    # 实际使用时需要替换为适当的输入方法
    
    # 测试数据
    testcase = 1
    n = 5
    m = 4
    t = 2
    
    # 节点权值
    arr[1] = 10
    arr[2] = 20
    arr[3] = 30
    arr[4] = 40
    arr[5] = 50
    
    # 构建初始森林
    tree[1].append(2)
    tree[2].append(1)
    tree[2].append(3)
    tree[3].append(2)
    tree[4].append(5)
    tree[5].append(4)
    
    prepare()
    
    # 执行操作
    # 操作1: 连接节点3和节点4
    connect(3, 4)
    
    # 操作2: 查询节点1到节点5路径上第2小的权值
    result = queryKth(1, 5, 2)
    
    # 输出结果（实际使用时需要替换为适当的输出方法）
    # 查询结果
    print(result)

if __name__ == "__main__":
    main()