# More Powerful P3899
# 测试链接 : https://www.luogu.com.cn/problem/P3899
# 线段树合并解法
#
# 题目来源：湖南集训
# 题目大意：树上DP问题，需要维护子树信息
# 解法：线段树合并 + 树形DP
# 时间复杂度：O(n log n)
# 空间复杂度：O(n log n)

import sys
from collections import defaultdict

def main():
    # 读取输入
    parts = sys.stdin.readline().split()
    n = int(parts[0])
    q = int(parts[1])
    
    # 初始化变量
    G = defaultdict(list)
    ans = [0] * (n + 1)
    size_arr = [0] * (n + 1)  # 子树大小
    
    # 线段树合并相关
    root = [0] * (n + 1)
    lc = [0] * (n * 20)
    rc = [0] * (n * 20)
    sum_arr = [0] * (n * 20)
    cnt_arr = [0] * (n * 20)
    segCnt = 0
    
    # 计算子树大小
    def dfs1(u, father):
        size_arr[u] = 1
        for v in G[u]:
            if v != father:
                dfs1(v, u)
                size_arr[u] += size_arr[v]
    
    # 动态开点线段树插入
    def insert(rt, l, r, x, val):
        if l == r:
            sum_arr[rt] += x * val
            cnt_arr[rt] += val
            return
        mid = (l + r) >> 1
        if x <= mid:
            if lc[rt] == 0:
                nonlocal segCnt
                segCnt += 1
                lc[rt] = segCnt
            insert(lc[rt], l, mid, x, val)
        else:
            if rc[rt] == 0:
                nonlocal segCnt
                segCnt += 1
                rc[rt] = segCnt
            insert(rc[rt], mid+1, r, x, val)
        sum_arr[rt] = sum_arr[lc[rt]] + sum_arr[rc[rt]]
        cnt_arr[rt] = cnt_arr[lc[rt]] + cnt_arr[rc[rt]]
    
    # 线段树合并
    def merge(x, y, l, r):
        if x == 0 or y == 0:
            return x + y
        if l == r:
            sum_arr[x] += sum_arr[y]
            cnt_arr[x] += cnt_arr[y]
            return x
        mid = (l + r) >> 1
        lc[x] = merge(lc[x], lc[y], l, mid)
        rc[x] = merge(rc[x], rc[y], mid+1, r)
        sum_arr[x] = sum_arr[lc[x]] + sum_arr[rc[x]]
        cnt_arr[x] = cnt_arr[lc[x]] + cnt_arr[rc[x]]
        return x
    
    # 查询前k大的和
    def query(rt, l, r, k):
        if k <= 0:
            return 0
        if l == r:
            return min(k, cnt_arr[rt]) * l
        mid = (l + r) >> 1
        if cnt_arr[rc[rt]] >= k:
            return query(rc[rt], mid+1, r, k)
        else:
            return sum_arr[rc[rt]] + query(lc[rt], l, mid, k - cnt_arr[rc[rt]])
    
    # DFS处理线段树合并
    def dfs2(u, father):
        nonlocal segCnt
        # 先处理所有子节点
        for v in G[u]:
            if v != father:
                dfs2(v, u)
                # 合并子节点的信息到当前节点
                root[u] = merge(root[u], root[v], 1, n)
        
        # 插入当前节点的信息
        if root[u] == 0:
            segCnt += 1
            root[u] = segCnt
        insert(root[u], 1, n, size_arr[u], 1)
        
        # 记录答案
        ans[u] = query(root[u], 1, n, min(size_arr[u], n))
    
    # 读取树结构
    for i in range(1, n):
        parts = sys.stdin.readline().split()
        u = int(parts[0])
        v = int(parts[1])
        G[u].append(v)
        G[v].append(u)
    
    # 计算子树大小
    dfs1(1, 0)
    
    # DFS处理线段树合并
    dfs2(1, 0)
    
    # 处理查询
    for i in range(q):
        x = int(sys.stdin.readline())
        print(ans[x])

if __name__ == "__main__":
    main()