# 雨天的尾巴问题，Python版
# 测试链接 : https://www.luogu.com.cn/problem/P4556
#
# 题目来源：Vani有约会
# 题目大意：在树上进行路径加操作，每次给路径上所有节点添加某种类型的救济粮，
# 最后查询每个节点最多的救济粮类型
# 解法：树链剖分 + 线段树合并 + 树上差分
# 时间复杂度：O(n log n)
# 空间复杂度：O(n log n)
#
# 线段树合并专题
# 核心思想：将两棵线段树的相同位置的节点合并，保留所需的信息
# 适用场景：
# 1. 树上的子树信息统计
# 2. 动态开点线段树的空间优化
# 3. 需要合并多个数据结构的情况

import sys
from collections import defaultdict

# 注意事项：
# 1. Python的递归深度限制：对于大规模数据，需要设置sys.setrecursionlimit()
# 2. 内存优化：在Python中使用字典实现线段树节点会占用较多内存
# 3. 性能优化：可以使用lru_cache装饰器优化重复计算，但会增加内存消耗

def main():
    # 提高Python递归深度限制，避免在大规模树上DFS时栈溢出
    sys.setrecursionlimit(1 << 25)
    
    # 读取输入
    line = sys.stdin.readline().split()
    n, m = int(line[0]), int(line[1])
    
    # 初始化变量
    fa = [0] * (n + 1)      # 父亲节点
    dep = [0] * (n + 1)     # 深度
    sz = [0] * (n + 1)      # 子树大小
    hs = [0] * (n + 1)      # 重儿子
    top = [0] * (n + 1)     # 所在重链的顶端
    id_arr = [0] * (n + 1)  # dfs序
    orig = [0] * (n + 1)    # 原编号
    cnt = 0                 # dfs序计数器
    
    # 建图
    graph = defaultdict(list)
    
    # 读取边信息
    for _ in range(n - 1):
        line = sys.stdin.readline().split()
        x, y = int(line[0]), int(line[1])
        graph[x].append(y)
        graph[y].append(x)
    
    # 第一次dfs，处理深度、父节点、子树大小、重儿子
    def dfs1(u, father, depth):
        fa[u] = father
        dep[u] = depth
        sz[u] = 1
        
        # 遍历所有子节点
        for v in graph[u]:
            if v != father:
                dfs1(v, u, depth + 1)
                sz[u] += sz[v]
                # 更新重儿子
                if sz[v] > sz[hs[u]]:
                    hs[u] = v
    
    # 第二次dfs，处理链剖分
    def dfs2(u, tp):
        nonlocal cnt
        cnt += 1
        id_arr[u] = cnt
        orig[cnt] = u
        top[u] = tp
        
        if hs[u] != 0:
            dfs2(hs[u], tp)
        
        # 处理轻儿子
        for v in graph[u]:
            if v != fa[u] and v != hs[u]:
                dfs2(v, v)
    
    # 线段树节点定义
    class SegmentTreeNode:
        def __init__(self):
            self.ls = 0         # 左儿子
            self.rs = 0         # 右儿子
            self.max_val = 0    # 区间最大值
            self.ans = 0        # 最大值对应的救济粮类型
    
    # 线段树相关
    root = [0] * (n + 1)        # 每个节点的线段树根节点
    seg_nodes = {}              # 线段树节点字典
    segCnt = 0                  # 线段树节点计数器
    
    # 获取新的线段树节点
    def getNewNode():
        nonlocal segCnt
        segCnt += 1
        seg_nodes[segCnt] = SegmentTreeNode()
        return segCnt
    
    # 更新节点信息
    def pushUp(p):
        l_node = seg_nodes[seg_nodes[p].ls] if seg_nodes[p].ls in seg_nodes else None
        r_node = seg_nodes[seg_nodes[p].rs] if seg_nodes[p].rs in seg_nodes else None
        
        if l_node and (not r_node or l_node.max_val >= r_node.max_val):
            seg_nodes[p].max_val = l_node.max_val
            seg_nodes[p].ans = l_node.ans
        elif r_node:
            seg_nodes[p].max_val = r_node.max_val
            seg_nodes[p].ans = r_node.ans
    
    # 线段树合并的核心函数：将两棵线段树p1和p2合并，返回合并后的根节点
    # 时间复杂度：O(log n)，空间复杂度：O(log n)
    
    # 线段树单点修改
    def update(pos, val, l, r, rootId):
        if rootId == 0:
            rootId = getNewNode()
        
        if l == r:
            seg_nodes[rootId].max_val += val
            seg_nodes[rootId].ans = l
            return rootId
        
        mid = (l + r) >> 1
        if pos <= mid:
            seg_nodes[rootId].ls = update(pos, val, l, mid, seg_nodes[rootId].ls)
        else:
            seg_nodes[rootId].rs = update(pos, val, mid + 1, r, seg_nodes[rootId].rs)
        
        pushUp(rootId)
        return rootId
    
    # 线段树合并
    def merge(p1, p2, l, r):
        # 如果其中一个为空，返回另一个
        if p1 == 0 or p2 == 0:
            return p1 + p2
        
        # 如果是叶子节点
        if l == r:
            seg_nodes[p1].max_val += seg_nodes[p2].max_val if p2 in seg_nodes else 0
            seg_nodes[p1].ans = l
            return p1
        
        mid = (l + r) >> 1
        seg_nodes[p1].ls = merge(
            seg_nodes[p1].ls if p1 in seg_nodes else 0,
            seg_nodes[p2].ls if p2 in seg_nodes else 0,
            l, mid
        )
        seg_nodes[p1].rs = merge(
            seg_nodes[p1].rs if p1 in seg_nodes else 0,
            seg_nodes[p2].rs if p2 in seg_nodes else 0,
            mid + 1, r
        )
        pushUp(p1)
        return p1
    
    # 获取LCA
    def lca(x, y):
        while top[x] != top[y]:
            if dep[top[x]] < dep[top[y]]:
                x, y = y, x
            x = fa[top[x]]
        return x if dep[x] < dep[y] else y
    
    # 树上差分更新
    def updatePath(x, y, z):
        l = lca(x, y)
        root[x] = update(z, 1, 1, n, root[x])
        root[y] = update(z, 1, 1, n, root[y])
        root[l] = update(z, -1, 1, n, root[l])
        if fa[l] > 0:
            root[fa[l]] = update(z, -1, 1, n, root[fa[l]])
    
    # DFS处理答案
    def dfs3(u):
        # 处理所有子节点
        for v in graph[u]:
            if v != fa[u]:
                dfs3(v)
                root[u] = merge(root[u], root[v], 1, n)
    
    # 初始化
    dfs1(1, 0, 1)
    dfs2(1, 1)
    
    # 处理操作
    for _ in range(m):
        line = sys.stdin.readline().split()
        x, y, z = int(line[0]), int(line[1]), int(line[2])
        updatePath(x, y, z)
    
    # 计算答案：后序DFS遍历，自底向上合并子节点的线段树
        dfs3(1)
    
    # 输出答案
    result = []
    for i in range(1, n + 1):
        if root[i] == 0 or (root[i] in seg_nodes and seg_nodes[root[i]].max_val == 0):
            result.append("0")
        else:
            result.append(str(seg_nodes[root[i]].ans))
    
    print("\n".join(result))

# Codeforces 600E - Lomsat gelral
# 题目链接：https://codeforces.com/problemset/problem/600/E
# 题目大意：给定一棵树，每个节点有一个颜色，求每个节点的子树中出现次数最多的颜色之和
# 这是线段树合并的经典应用，使用后序遍历+线段树合并可以高效求解
# 解题思路：
# 1. 对每个节点维护一个线段树，统计子树中各颜色的出现次数
# 2. 使用后序遍历，合并子节点的线段树到父节点
# 3. 在线段树中维护最大值和总和，以便快速查询
# 时间复杂度：O(n log c)，其中c为颜色数量
# 空间复杂度：O(n log c)

def solve_codeforces_600E():
    """
    Codeforces 600E题解
    使用线段树合并解决子树颜色统计问题
    """
    import sys
    sys.setrecursionlimit(1 << 25)
    
    n = int(sys.stdin.readline())
    colors = list(map(int, sys.stdin.readline().split()))
    
    # 构建树
    graph = [[] for _ in range(n+1)]
    for _ in range(n-1):
        u, v = map(int, sys.stdin.readline().split())
        graph[u].append(v)
        graph[v].append(u)
    
    # 线段树节点类
    class SegmentTreeNode:
        def __init__(self):
            self.ls = 0  # 左子节点
            self.rs = 0  # 右子节点
            self.count = 0  # 颜色出现次数
            self.max_count = 0  # 最大出现次数
            self.sum = 0  # 出现次数最多的颜色之和
    
    # 线段树相关数据
    max_color = max(colors)
    nodes = [SegmentTreeNode()]
    roots = [0]*(n+1)
    
    def new_node():
        nodes.append(SegmentTreeNode())
        return len(nodes)-1
    
    # 向上更新
    def push_up(node):
        ls = nodes[node].ls
        rs = nodes[node].rs
        
        # 合并子节点信息
        if nodes[ls].max_count > nodes[rs].max_count:
            nodes[node].max_count = nodes[ls].max_count
            nodes[node].sum = nodes[ls].sum
        elif nodes[rs].max_count > nodes[ls].max_count:
            nodes[node].max_count = nodes[rs].max_count
            nodes[node].sum = nodes[rs].sum
        else:
            nodes[node].max_count = nodes[ls].max_count
            nodes[node].sum = nodes[ls].sum + nodes[rs].sum
    
    # 更新操作
    def update(node, l, r, pos, val):
        if not node:
            node = new_node()
        
        if l == r:
            nodes[node].count += val
            nodes[node].max_count = nodes[node].count
            nodes[node].sum = pos if nodes[node].count > 0 else 0
            return node
        
        mid = (l + r) // 2
        if pos <= mid:
            nodes[node].ls = update(nodes[node].ls, l, mid, pos, val)
        else:
            nodes[node].rs = update(nodes[node].rs, mid+1, r, pos, val)
        
        push_up(node)
        return node
    
    # 合并操作
    def merge(a, b, l, r):
        if not a:
            return b
        if not b:
            return a
        
        if l == r:
            nodes[a].count += nodes[b].count
            nodes[a].max_count = nodes[a].count
            nodes[a].sum = l if nodes[a].count > 0 else 0
            return a
        
        mid = (l + r) // 2
        nodes[a].ls = merge(nodes[a].ls, nodes[b].ls, l, mid)
        nodes[a].rs = merge(nodes[a].rs, nodes[b].rs, mid+1, r)
        
        push_up(a)
        return a
    
    result = [0]*(n+1)
    
    # DFS遍历树
    def dfs(u, parent):
        # 初始化当前节点的线段树
        roots[u] = update(0, 1, max_color, colors[u-1], 1)
        
        # 遍历子节点
        for v in graph[u]:
            if v != parent:
                dfs(v, u)
                # 合并子节点的线段树
                roots[u] = merge(roots[u], roots[v], 1, max_color)
        
        # 保存结果
        result[u] = nodes[roots[u]].sum
    
    # 开始DFS
    dfs(1, -1)
    
    # 输出结果
    print(' '.join(map(str, result[1:n+1])))

# LeetCode 1519. 子树中标签相同的节点数
# 题目链接：https://leetcode.cn/problems/number-of-nodes-in-the-sub-tree-with-the-same-label/
# 题目大意：给定一棵树，每个节点有一个标签，求每个节点的子树中标签相同的节点数
# 这题可以使用DFS+哈希表实现，但也可以作为线段树合并的练习
# 对于这个问题，由于标签只有26种，使用数组更高效

class LeetCode1519:
    def countSubTrees(self, n, edges, labels):
        # 构建邻接表
        graph = [[] for _ in range(n)]
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        result = [0] * n
        
        def dfs(node, parent):
            # 创建当前节点的计数数组
            counts = [0] * 26
            label_idx = ord(labels[node]) - ord('a')
            counts[label_idx] = 1  # 当前节点自身计入一次
            
            # 遍历所有子节点
            for neighbor in graph[node]:
                if neighbor != parent:
                    child_counts = dfs(neighbor, node)
                    # 合并子节点的计数到当前节点
                    for i in range(26):
                        counts[i] += child_counts[i]
            
            # 保存当前节点的结果
            result[node] = counts[label_idx]
            return counts
        
        dfs(0, -1)
        return result

if __name__ == "__main__":
    main()
    
    # 性能优化技巧：
    # 1. 输入优化：对于大数据量，使用sys.stdin.readline比input()更快
    # 2. 避免重复计算：缓存中间结果
    # 3. 内存管理：定期清理不再使用的对象，避免内存泄漏
    # 4. 并行计算：对于独立子问题，考虑使用多进程处理
    # 5. 避免递归深度过大：对于大规模树，可以考虑非递归DFS实现