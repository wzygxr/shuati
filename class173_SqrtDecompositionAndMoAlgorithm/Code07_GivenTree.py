# 给你一棵树问题 - 分块算法优化动态规划 (Python版本)
# 题目来源: https://www.luogu.com.cn/problem/CF1039D
# 题目来源: https://codeforces.com/problemset/problem/1039/D
# 题目大意: 一共有n个节点，给定n-1条边，所有节点连成一棵树
# 树的路径是指，从端点x到端点y的简单路径，k路径是指，路径的节点数正好为k
# 整棵树希望分解成尽量多的k路径，k路径的节点不能复用，所有k路径不要求包含所有点
# 打印k = 1, 2, 3..n时，k路径有最多有几条
# 约束条件: 1 <= n <= 200000

import math
import sys

# 定义最大数组长度
MAXN = 200001

# 全局变量
n, blen = 0, 0

# 邻接表存储树的结构
# head[i]: 节点i的邻接表头节点
head = [0] * MAXN

# next[i]: 邻接表中第i个节点的下一个节点
next_arr = [0] * (MAXN << 1)

# to[i]: 邻接表中第i个节点存储的相邻节点
to = [0] * (MAXN << 1)

# cntg: 邻接表节点计数器
cntg = 0

# fa[i]: 节点i的父节点编号
fa = [0] * MAXN

# dfnOrder: 根据dfn序，依次收集上来的节点编号
dfnOrder = [0] * MAXN

# cntd: dfn序计数器
cntd = 0

# len[i]: 当前i号节点只能往下走，没分配成路径的最长链的长度
len_arr = [0] * MAXN

# max1[i]: 最大值 { len[a], len[b], len[c] ... }，其中a、b、c..是i的子节点
max1 = [0] * MAXN

# max2[i]: 次大值 { len[a], len[b], len[c] ... }，其中a、b、c..是i的子节点
max2 = [0] * MAXN

# ans: 存储每个k对应的答案
ans = [0] * MAXN

def addEdge(u, v):
    """
    添加边到邻接表
    参数:
        u: 节点u
        v: 节点v
    """
    global cntg
    # 添加u->v的边
    cntg += 1
    next_arr[cntg] = head[u]
    to[cntg] = v
    head[u] = cntg
    
    # 添加v->u的边
    cntg += 1
    next_arr[cntg] = head[v]
    to[cntg] = u
    head[v] = cntg

def dfs(u, f):
    """
    DFS遍历树，生成dfn序
    参数:
        u: 当前节点
        f: 父节点
    """
    global cntd
    # 记录父节点
    fa[u] = f
    
    # 记录dfn序
    cntd += 1
    dfnOrder[cntd] = u
    
    # 遍历所有子节点
    e = head[u]
    while e > 0:
        # 避免回到父节点
        if to[e] != f:
            dfs(to[e], u)
        e = next_arr[e]

def query(k):
    """
    查询当路径长度为k时，最多能分解成几条路径
    参数:
        k: 路径长度
    返回:
        最多路径数
    """
    cnt = 0
    
    # 按照dfn序的逆序处理节点
    for i in range(n, 0, -1):
        cur = dfnOrder[i]
        father = fa[cur]
        
        # 如果当前节点的最长链和次长链之和+1 >= k
        # 说明可以形成一条长度为k的路径
        if max1[cur] + max2[cur] + 1 >= k:
            cnt += 1  # 路径数+1
            len_arr[cur] = 0  # 当前节点的最长链长度重置为0
        else:
            # 否则更新当前节点的最长链长度
            len_arr[cur] = max1[cur] + 1
        
        # 更新父节点的最长链和次长链
        if len_arr[cur] > max1[father]:
            max2[father] = max1[father]
            max1[father] = len_arr[cur]
        elif len_arr[cur] > max2[father]:
            max2[father] = len_arr[cur]
    
    # 重置数组
    for i in range(1, n + 1):
        len_arr[i] = max1[i] = max2[i] = 0
    return cnt

def jump(l, r, curAns):
    """
    跳跃函数，用于优化计算
    参数:
        l: 左边界
        r: 右边界
        curAns: 当前答案
    返回:
        下一个需要计算的位置
    """
    find = l
    while l <= r:
        mid = (l + r) >> 1
        check = query(mid)
        
        if check < curAns:
            r = mid - 1
        elif check > curAns:
            l = mid + 1
        else:
            find = mid
            l = mid + 1
    return find + 1

def compute():
    """
    计算所有答案
    """
    # 对于k <= sqrt(n)的情况，直接计算
    for i in range(1, blen + 1):
        ans[i] = query(i)
    
    # 对于k > sqrt(n)的情况，使用跳跃优化
    i = blen + 1
    while i <= n:
        ans[i] = query(i)
        i = jump(i, n, ans[i])

def prepare():
    """
    预处理函数
    """
    global blen
    # 计算块大小，选择sqrt(n * log2(n))以优化性能
    log2n = 0
    while (1 << log2n) <= (n >> 1):
        log2n += 1
    blen = max(1, int(math.sqrt(n * log2n)))
    
    # 初始化答案数组为-1，表示未计算
    for i in range(1, n + 1):
        ans[i] = -1

def main():
    global n
    # 读取节点数量n
    n = int(sys.stdin.readline())
    
    # 读取n-1条边
    for i in range(1, n):
        u, v = map(int, sys.stdin.readline().split())
        addEdge(u, v)
    
    # DFS生成dfn序
    dfs(1, 0)
    
    # 进行预处理
    prepare()
    
    # 计算所有答案
    compute()
    
    # 输出所有答案
    for i in range(1, n + 1):
        # 如果答案未计算，则继承前一个答案
        if ans[i] == -1:
            ans[i] = ans[i - 1]
        print(ans[i])

if __name__ == "__main__":
    main()