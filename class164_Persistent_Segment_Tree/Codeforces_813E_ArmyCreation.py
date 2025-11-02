# -*- coding: utf-8 -*-
"""
Codeforces 813E - Army Creation

题目来源: Codeforces 813E
题目链接: https://codeforces.com/problemset/problem/813/E

题目描述:
Vova非常喜欢玩电脑游戏，现在他正在玩一款叫做Rage of Empires的策略游戏。
在这个游戏里，Vova可以雇佣n个不同的战士，第i个战士的类型为ai。
Vova想要雇佣其中一些战士，从而建立一支平衡的军队。
如果对于任何一种类型，军队中这种类型的战士不超过k个，那么这支军队就是平衡的。
现在Vova有q个计划，第i个计划他只能雇佣区间[li, ri]之间的战士。
对于每个计划，你需要求出可以组建的平衡军队的最多人数。

解题思路:
使用可持久化线段树（主席树）解决限制性区间选择问题。
1. 预处理：对于每个位置i，计算next[i]表示从位置i开始，第k+1个与a[i]相同元素的位置
2. 建立可持久化线段树，每个位置对应一个版本
3. 对于每个查询，在对应区间的线段树版本中查询区间和

时间复杂度: O((n + q) log n)
空间复杂度: O(n log n)

约束条件:
1 <= n, q <= 10^5
1 <= k <= n
1 <= ai <= 10^9
1 <= li <= ri <= n

示例:
输入:
5 2
1 1 2 1 1
3
1 5
2 5
1 3

输出:
4
3
3
"""

import sys
from collections import defaultdict
input = sys.stdin.read

# 全局变量
MAXN = 100010

# 原始数据
arr = [0] * MAXN

# 预处理相关
next_pos = [0] * MAXN
positions = defaultdict(list)

# 可持久化线段树
root = [0] * MAXN
left = [0] * (MAXN * 20)
right = [0] * (MAXN * 20)
sum_tree = [0] * (MAXN * 20)
cnt = 0

def build(l, r):
    """构建空线段树"""
    global cnt
    cnt += 1
    rt = cnt
    sum_tree[rt] = 0
    if l < r:
        mid = (l + r) // 2
        left[rt] = build(l, mid)
        right[rt] = build(mid + 1, r)
    return rt

def update(pos, val, l, r, pre):
    """区间更新操作（单点更新）"""
    global cnt
    cnt += 1
    rt = cnt
    left[rt] = left[pre]
    right[rt] = right[pre]
    sum_tree[rt] = sum_tree[pre] + val
    
    if l < r:
        mid = (l + r) // 2
        if pos <= mid:
            left[rt] = update(pos, val, l, mid, left[rt])
        else:
            right[rt] = update(pos, val, mid + 1, r, right[rt])
    return rt

def query(jobl, jobr, l, r, i):
    """区间查询操作"""
    if jobl <= l and r <= jobr:
        return sum_tree[i]
    mid = (l + r) // 2
    ans = 0
    if jobl <= mid:
        ans += query(jobl, jobr, l, mid, left[i])
    if jobr > mid:
        ans += query(jobl, jobr, mid + 1, r, right[i])
    return ans

def main():
    global cnt
    
    data = input().split()
    idx = 0
    
    n = int(data[idx])
    idx += 1
    k = int(data[idx])
    idx += 1
    
    # 读取数据
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
        # 记录每个值出现的位置
        positions[arr[i]].append(i)
    
    # 预处理next数组
    for val, pos_list in positions.items():
        for i in range(len(pos_list)):
            if i + k < len(pos_list):
                next_pos[pos_list[i]] = pos_list[i + k]
            else:
                next_pos[pos_list[i]] = n + 1
    
    # 构建初始线段树
    root[0] = build(1, n)
    
    # 逐个插入元素，构建可持久化线段树
    for i in range(1, n + 1):
        # 在位置i处+1，在位置next[i]处-1
        root[i] = update(i, 1, 1, n, root[i-1])
        if next_pos[i] <= n:
            root[i] = update(next_pos[i], -1, 1, n, root[i])
    
    q = int(data[idx])
    idx += 1
    last_ans = 0
    
    # 处理查询
    for i in range(q):
        l = int(data[idx])
        idx += 1
        r = int(data[idx])
        idx += 1
        
        # 异或上一次的答案
        l = (l + last_ans - 1) % n + 1
        r = (r + last_ans - 1) % n + 1
        
        if l > r:
            l, r = r, l
        
        # 查询区间[l,r]的和
        result = query(l, r, 1, n, root[r])
        print(result)
        last_ans = result

if __name__ == "__main__":
    main()