# -*- coding: utf-8 -*-
"""
范围修改的可持久化线段树，标记永久化减少空间占用，Python版

题目来源: HDU 4348 To the moon
题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=4348

题目描述:
给定一个长度为n的数组arr，下标1~n，时间戳t=0，arr认为是0版本的数组
一共有m条查询，每条查询为如下四种类型中的一种
C x y z : 当前时间戳t版本的数组，[x..y]范围每个数字增加z，得到t+1版本数组，并且t++
Q x y   : 当前时间戳t版本的数组，打印[x..y]范围累加和
H x y z : z版本的数组，打印[x..y]范围的累加和
B x     : 当前时间戳t设置成x

解题思路:
使用标记永久化技术实现可持久化线段树，以减少空间占用。
1. 标记永久化是一种优化技巧，在处理区间更新时，不立即下传标记，
   而是在查询时根据路径上的标记计算结果
2. 在更新时，只复制被修改路径上的节点，共享未修改的部分
3. 通过标记永久化减少节点复制，从而减少空间占用
4. sum数组存储的是考虑所有任务后的累加和，而不是真实累加和

时间复杂度: O((n + m) log n)
空间复杂度: O(n log n)

1 <= n、m <= 10^5
-10^9 <= arr[i] <= +10^9

示例:
输入:
5 10
5 6 7 8 9
Q 1 5
C 2 4 10
Q 1 5
H 1 5 0
B 3
Q 1 5
C 1 5 20
Q 1 5
H 1 5 3
Q 1 5

输出:
35
55
35
55
75
55
"""

import sys
input = sys.stdin.read

# 全局变量
MAXN = 100001
MAXT = MAXN * 25

n, m, t = 0, 0, 0
arr = [0] * MAXN
root = [0] * MAXN
left = [0] * MAXT
right = [0] * MAXT
sum_tree = [0] * MAXT
add_tag = [0] * MAXT
cnt = 0

def build(l, r):
    """构建线段树"""
    global cnt
    cnt += 1
    rt = cnt
    add_tag[rt] = 0
    if l == r:
        sum_tree[rt] = arr[l]
    else:
        mid = (l + r) // 2
        left[rt] = build(l, mid)
        right[rt] = build(mid + 1, r)
        sum_tree[rt] = sum_tree[left[rt]] + sum_tree[right[rt]]
    return rt

def add(jobl, jobr, jobv, l, r, i):
    """区间增加操作（标记永久化）"""
    global cnt
    cnt += 1
    rt = cnt
    a, b = max(jobl, l), min(jobr, r)
    left[rt] = left[i]
    right[rt] = right[i]
    sum_tree[rt] = sum_tree[i] + jobv * (b - a + 1)
    add_tag[rt] = add_tag[i]
    
    if jobl <= l and r <= jobr:
        add_tag[rt] += jobv
    else:
        mid = (l + r) // 2
        if jobl <= mid:
            left[rt] = add(jobl, jobr, jobv, l, mid, left[rt])
        if jobr > mid:
            right[rt] = add(jobl, jobr, jobv, mid + 1, r, right[rt])
    return rt

def query(jobl, jobr, add_history, l, r, i):
    """区间查询操作（标记永久化）"""
    if jobl <= l and r <= jobr:
        return sum_tree[i] + add_history * (r - l + 1)
    
    mid = (l + r) // 2
    ans = 0
    if jobl <= mid:
        ans += query(jobl, jobr, add_history + add_tag[i], l, mid, left[i])
    if jobr > mid:
        ans += query(jobl, jobr, add_history + add_tag[i], mid + 1, r, right[i])
    return ans

def main():
    global n, m, t, cnt
    
    data = input().split()
    idx = 0
    
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
    
    root[0] = build(1, n)
    
    for _ in range(m):
        op = data[idx]
        idx += 1
        
        if op == "C":
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            z = int(data[idx])
            idx += 1
            cnt += 1
            root[t + 1] = add(x, y, z, 1, n, root[t])
            t += 1
        elif op == "Q":
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            print(query(x, y, 0, 1, n, root[t]))
        elif op == "H":
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            z = int(data[idx])
            idx += 1
            print(query(x, y, 0, 1, n, root[z]))
        else:  # op == "B"
            x = int(data[idx])
            idx += 1
            t = x

if __name__ == "__main__":
    main()