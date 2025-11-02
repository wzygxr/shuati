# Dominant Indices CF1009F
# 测试链接 : https://codeforces.com/problemset/problem/1009/F
# 线段树合并解法
#
# 题目来源：Codeforces
# 题目大意：对于每个节点，求其子树中深度最大的节点的深度
# 解法：线段树合并 + 树形DP
# 时间复杂度：O(n log n)
# 空间复杂度：O(n log n)

import sys
from collections import defaultdict

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    
    # 初始化变量
    G = defaultdict(list)
    ans = [0] * (n + 1)
    
    # 线段树合并相关
    root = [0] * (n + 1)
    lc = [0] * (n * 20)
    rc = [0] * (n * 20)
    sum_arr = [0] * (n * 20)
    maxDepth = [0] * (n * 20)  # 记录最大深度
    maxCount = [0] * (n * 20)  # 记录最大深度的出现次数
    cnt = 0
    
    # 动态开点线段树插入
    def insert(rt, l, r, x, val):
        nonlocal cnt
        if l == r:
            sum_arr[rt] += val
            maxDepth[rt] = l
            maxCount[rt] = sum_arr[rt]
            return
        mid = (l + r) >> 1
        if x <= mid:
            if lc[rt] == 0:
                cnt += 1
                lc[rt] = cnt
            insert(lc[rt], l, mid, x, val)
        else:
            if rc[rt] == 0:
                cnt += 1
                rc[rt] = cnt
            insert(rc[rt], mid+1, r, x, val)
        # 合并左右子树信息
        sum_arr[rt] = sum_arr[lc[rt]] + sum_arr[rc[rt]]
        if maxCount[lc[rt]] > maxCount[rc[rt]]:
            maxDepth[rt] = maxDepth[lc[rt]]
            maxCount[rt] = maxCount[lc[rt]]
        elif maxCount[lc[rt]] < maxCount[rc[rt]]:
            maxDepth[rt] = maxDepth[rc[rt]]
            maxCount[rt] = maxCount[rc[rt]]
        else:
            maxDepth[rt] = min(maxDepth[lc[rt]], maxDepth[rc[rt]])
            maxCount[rt] = maxCount[lc[rt]]
    
    # 线段树合并
    def merge(x, y, l, r):
        if x == 0 or y == 0:
            return x + y
        if l == r:
            sum_arr[x] += sum_arr[y]
            maxDepth[x] = l
            maxCount[x] = sum_arr[x]
            return x
        mid = (l + r) >> 1
        lc[x] = merge(lc[x], lc[y], l, mid)
        rc[x] = merge(rc[x], rc[y], mid+1, r)
        # 合并左右子树信息
        sum_arr[x] = sum_arr[lc[x]] + sum_arr[rc[x]]
        if maxCount[lc[x]] > maxCount[rc[x]]:
            maxDepth[x] = maxDepth[lc[x]]
            maxCount[x] = maxCount[lc[x]]
        elif maxCount[lc[x]] < maxCount[rc[x]]:
            maxDepth[x] = maxDepth[rc[x]]
            maxCount[x] = maxCount[rc[x]]
        else:
            maxDepth[x] = min(maxDepth[lc[x]], maxDepth[rc[x]])
            maxCount[x] = maxCount[lc[x]]
        return x
    
    # DFS处理线段树合并
    def dfs(u, father):
        nonlocal cnt
        # 先处理所有子节点
        for v in G[u]:
            if v != father:
                dfs(v, u)
                # 合并子节点的信息到当前节点
                root[u] = merge(root[u], root[v], 1, n)
        
        # 插入当前节点的深度信息（深度为1）
        if root[u] == 0:
            cnt += 1
            root[u] = cnt
        insert(root[u], 1, n, 1, 1)
        
        # 记录答案
        ans[u] = maxDepth[root[u]] - 1  # 减去1得到相对于当前节点的深度
    
    # 读取树结构
    for i in range(1, n):
        parts = sys.stdin.readline().split()
        u = int(parts[0])
        v = int(parts[1])
        G[u].append(v)
        G[v].append(u)
    
    # DFS处理
    dfs(1, 0)
    
    # 输出结果
    for i in range(1, n + 1):
        print(ans[i])

if __name__ == "__main__":
    main()