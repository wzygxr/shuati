# Tree Rotations P3521
# 测试链接 : https://www.luogu.com.cn/problem/P3521
# 线段树合并解法
#
# 题目来源：POI2011
# 题目大意：给定一棵二叉树，叶子节点有权值，可以交换任意节点的左右子树，
# 求最小逆序对数
# 解法：线段树合并 + 树形DP
# 时间复杂度：O(n log n)
# 空间复杂度：O(n log n)

import sys
from collections import deque

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    
    # 初始化变量
    val = [0] * (n + 1)
    lc = [0] * (n + 1)
    rc = [0] * (n + 1)
    ans = 0
    
    # 线段树合并相关
    root = [0] * (n + 1)
    segLc = [0] * (n * 20)
    segRc = [0] * (n * 20)
    segSum = [0] * (n * 20)
    segCnt = 0
    
    # 离散化
    vals = []
    
    # 动态开点线段树插入
    def insert(rt, l, r, x):
        nonlocal segCnt
        if l == r:
            segSum[rt] += 1
            return
        mid = (l + r) >> 1
        if x <= mid:
            if segLc[rt] == 0:
                segCnt += 1
                segLc[rt] = segCnt
            insert(segLc[rt], l, mid, x)
        else:
            if segRc[rt] == 0:
                segCnt += 1
                segRc[rt] = segCnt
            insert(segRc[rt], mid+1, r, x)
        segSum[rt] = segSum[segLc[rt]] + segSum[segRc[rt]]
    
    # 线段树合并
    def merge(x, y):
        if x == 0 or y == 0:
            return x + y
        if segLc[x] == 0 and segLc[y] != 0:
            segLc[x] = segLc[y]
        elif segLc[x] != 0 and segLc[y] != 0:
            segLc[x] = merge(segLc[x], segLc[y])
        
        if segRc[x] == 0 and segRc[y] != 0:
            segRc[x] = segRc[y]
        elif segRc[x] != 0 and segRc[y] != 0:
            segRc[x] = merge(segRc[x], segRc[y])
        
        segSum[x] = segSum[segLc[x]] + segSum[segRc[x]]
        return x
    
    # 查询小于某个值的元素个数
    def queryLess(rt, l, r, x):
        if rt == 0 or r < x:
            return 0
        if l >= x:
            return segSum[rt]
        if l == r:
            return segSum[rt] if l < x else 0
        mid = (l + r) >> 1
        return queryLess(segLc[rt], l, mid, x) + queryLess(segRc[rt], mid+1, r, x)
    
    # 查询大于某个值的元素个数
    def queryGreater(rt, l, r, x):
        if rt == 0 or l > x:
            return 0
        if r <= x:
            return 0
        if l == r:
            return segSum[rt] if l > x else 0
        mid = (l + r) >> 1
        return queryGreater(segLc[rt], l, mid, x) + queryGreater(segRc[rt], mid+1, r, x)
    
    # DFS处理树结构
    def dfs(u):
        nonlocal ans, segCnt
        if lc[u] == 0 and rc[u] == 0:
            # 叶子节点
            segCnt += 1
            root[u] = segCnt
            pos = 0
            for i in range(len(vals)):
                if vals[i] >= val[u]:
                    pos = i
                    break
            pos += 1  # 转换为1-indexed
            insert(root[u], 1, len(vals), pos)
            return root[u]
        
        # 递归处理左右子树
        leftRoot = dfs(lc[u])
        rightRoot = dfs(rc[u])
        
        # 计算不交换的逆序对数
        inv1 = 0
        # 左子树中大于右子树中最小值的个数
        inv1 += queryGreater(leftRoot, 1, len(vals), 1)
        # 右子树中小于左子树中最大值的个数
        inv1 += queryLess(rightRoot, 1, len(vals), len(vals))
        
        # 计算交换后的逆序对数
        inv2 = 0
        # 右子树中大于左子树中最小值的个数
        inv2 += queryGreater(rightRoot, 1, len(vals), 1)
        # 左子树中小于右子树中最大值的个数
        inv2 += queryLess(leftRoot, 1, len(vals), len(vals))
        
        # 选择逆序对数更小的方案
        ans += min(inv1, inv2)
        
        # 合并左右子树的信息
        root[u] = merge(leftRoot, rightRoot)
        return root[u]
    
    # 读取树结构和节点权值
    nodes = deque([1])
    nodeCnt = 1
    
    while nodes and nodeCnt <= n:
        u = nodes.popleft()
        parts = sys.stdin.readline().split()
        left = int(parts[0])
        right = int(parts[1])
        
        if left == 0 and right == 0:
            # 叶子节点
            val[u] = int(parts[2])
            vals.append(val[u])
        else:
            lc[u] = left
            rc[u] = right
            if left != 0:
                nodes.append(left)
            if right != 0:
                nodes.append(right)
            nodeCnt += 2
    
    # 离散化
    vals = sorted(list(set(vals)))
    
    # DFS处理
    dfs(1)
    
    # 输出结果
    print(ans)

if __name__ == "__main__":
    main()