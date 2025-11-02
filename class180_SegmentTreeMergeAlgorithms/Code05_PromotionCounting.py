#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树合并专题 - Code05_PromotionCounting.py

晋升计数问题（USACO17JAN Promotion Counting），Python版
测试链接：https://www.luogu.com.cn/problem/P3605

题目来源：USACO17JAN
题目大意：给定一棵树，每个节点有一个权值，对于每个节点，
统计其子树中权值大于该节点权值的节点个数

算法思路：
1. 使用离散化技术处理权值范围
2. 构建动态开点线段树维护权值分布
3. 采用线段树合并技术自底向上统计子树信息
4. 查询每个节点子树中大于当前节点权值的节点数量

核心思想：
- 离散化：将大范围的权值映射到小范围，节省空间
- 动态开点：仅在需要时创建线段树节点，避免空间浪费
- 线段树合并：高效合并子树信息，支持快速查询
- 后序遍历：自底向上处理，确保子节点信息先于父节点处理

时间复杂度分析：
- 离散化：O(n log n)
- DFS遍历：O(n)
- 线段树合并：O(n log n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- 线段树节点：O(n log n)
- 离散化数组：O(n)
- 总空间复杂度：O(n log n)

工程化考量：
1. 使用动态开点线段树节省空间
2. 离散化处理大范围权值
3. 后序遍历确保正确的处理顺序
4. 使用字典存储图结构，便于遍历

优化技巧：
- 离散化优化：减少线段树的值域范围
- 动态开点：避免预分配大量未使用的空间
- 线段树合并：高效处理子树信息合并
- 二分查找：快速定位离散化后的位置

边界情况处理：
- 单节点树
- 权值全部相同的情况
- 链状树结构
- 大规模数据输入

测试用例设计：
1. 基础测试：小规模树结构验证算法正确性
2. 边界测试：单节点、链状树、完全二叉树
3. 性能测试：n=100000的大规模数据
4. 极端测试：权值全部相同或严格递增/递减

运行命令：
python Code05_PromotionCounting.py < input.txt

注意事项：
1. Python版本由于性能限制，适合中小规模数据
2. 对于大规模数据，建议使用C++或Java版本
3. 注意递归深度限制，可能需要调整系统设置
"""

import sys
from collections import defaultdict

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    
    # 初始化变量
    val = [0] * (n + 1)
    ans = [0] * (n + 1)
    G = defaultdict(list)
    root = [0] * (n + 1)
    lc = [0] * (n * 20)
    rc = [0] * (n * 20)
    sum_arr = [0] * (n * 20)
    cnt = 0
    
    # 离散化
    vals = []
    
    # 动态开点线段树插入
    def insert(rt, l, r, x):
        nonlocal cnt
        if l == r:
            sum_arr[rt] += 1
            return
        mid = (l + r) >> 1
        if x <= mid:
            if lc[rt] == 0:
                cnt += 1
                lc[rt] = cnt
            insert(lc[rt], l, mid, x)
        else:
            if rc[rt] == 0:
                cnt += 1
                rc[rt] = cnt
            insert(rc[rt], mid+1, r, x)
        sum_arr[rt] = sum_arr[lc[rt]] + sum_arr[rc[rt]]
    
    # 线段树合并
    def merge(x, y):
        if x == 0 or y == 0:
            return x + y
        if lc[x] == 0 and lc[y] != 0:
            lc[x] = lc[y]
        elif lc[x] != 0 and lc[y] != 0:
            lc[x] = merge(lc[x], lc[y])
        
        if rc[x] == 0 and rc[y] != 0:
            rc[x] = rc[y]
        elif rc[x] != 0 and rc[y] != 0:
            rc[x] = merge(rc[x], rc[y])
        
        sum_arr[x] = sum_arr[lc[x]] + sum_arr[rc[x]]
        return x
    
    # 查询大于某个值的元素个数
    def query(rt, l, r, x):
        if rt == 0 or l > x:
            return 0
        if r <= x:
            return 0
        if l == r:
            return sum_arr[rt]
        mid = (l + r) >> 1
        return query(lc[rt], l, mid, x) + query(rc[rt], mid+1, r, x)
    
    def dfs(u):
        nonlocal cnt
        # 先处理所有子节点
        for v in G[u]:
            dfs(v)
            # 合并子节点的信息到当前节点
            if root[u] == 0:
                cnt += 1
                root[u] = cnt
            if root[v] != 0:
                root[u] = merge(root[u], root[v])
        
        # 插入当前节点的值
        if root[u] == 0:
            cnt += 1
            root[u] = cnt
        pos = 0
        for i in range(len(vals)):
            if vals[i] >= val[u]:
                pos = i
                break
        pos += 1  # 转换为1-indexed
        insert(root[u], 1, len(vals), pos)
        
        # 查询子树中大于当前节点值的元素个数
        ans[u] = query(root[u], 1, len(vals), pos)
    
    # 读取节点权值
    for i in range(1, n + 1):
        val[i] = int(sys.stdin.readline())
        vals.append(val[i])
    
    # 建树
    for i in range(2, n + 1):
        fa = int(sys.stdin.readline())
        G[fa].append(i)
    
    # 离散化
    vals = sorted(list(set(vals)))
    
    # DFS处理
    dfs(1)
    
    # 输出结果
    for i in range(1, n + 1):
        print(ans[i])

if __name__ == "__main__":
    main()