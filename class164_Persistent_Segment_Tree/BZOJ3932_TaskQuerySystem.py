# -*- coding: utf-8 -*-
"""
BZOJ 3932 [CQOI2015]任务查询系统

题目来源: BZOJ 3932
题目链接: https://www.lydsy.com/JudgeOnline/problem.php?id=3932

题目描述:
最近实验室正在为其管理的超级计算机编制一套任务管理系统，而你被安排完成其中的查询部分。
超级计算机中的任务用三元组(Si,Ei,Pi)描述，(Si,Ei,Pi)表示任务从第Si秒开始，在第Ei秒后结束（第Ei秒结束），
其优先级为Pi。同一时间可能有多个任务同时执行，它们的优先级可能相同，也可能不同。
调度系统会经常向查询系统询问，第Xi秒正在执行的任务中，优先级第Yi小的任务的优先级是多少。
在任意两个时刻，不会有相同优先级的任务正在执行。

解题思路:
使用可持久化线段树解决任务查询问题。
1. 将所有任务按照时间轴进行差分处理，每个任务在开始时间+1，在结束时间+1处-1
2. 按照时间顺序建立可持久化线段树，每个时间点对应一个版本
3. 对于每个查询，在对应时间点的线段树版本中查询第K小的优先级

时间复杂度: O((n+m) log n)
空间复杂度: O(n log n)

1 <= n, m <= 10^5
1 <= Si, Ei <= 10^9
1 <= Pi <= 10^9
1 <= Xi <= 10^9
1 <= Yi <= sum(Pj) (第Xj秒正在运行的任务总数)

示例:
输入:
2 3
1 2 6
2 3 3
1 1
2 1
3 1

输出:
6
3
3
"""

import sys
import bisect
input = sys.stdin.read

# 全局变量
MAXN = 100010

# 任务信息
S = [0] * MAXN
E = [0] * MAXN
P = [0] * MAXN

# 离散化相关
times = [0] * (MAXN * 2)
priorities = [0] * MAXN

# 可持久化线段树
root = [0] * (MAXN * 2)
left = [0] * (MAXN * 20)
right = [0] * (MAXN * 20)
sum_tree = [0] * (MAXN * 20)
cnt = 0

class Event:
    def __init__(self, time, priority, type):
        self.time = time
        self.priority = priority
        self.type = type

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

def insert(pos, l, r, pre, val):
    """插入操作"""
    global cnt
    cnt += 1
    rt = cnt
    left[rt] = left[pre]
    right[rt] = right[pre]
    sum_tree[rt] = sum_tree[pre] + val
    
    if l < r:
        mid = (l + r) // 2
        if pos <= mid:
            left[rt] = insert(pos, l, mid, left[rt], val)
        else:
            right[rt] = insert(pos, mid + 1, r, right[rt], val)
    return rt

def query_kth(k, l, r, u, v):
    """查询第k小"""
    if l >= r:
        return l
    mid = (l + r) // 2
    x = sum_tree[left[v]] - sum_tree[left[u]]
    if x >= k:
        return query_kth(k, l, mid, left[u], left[v])
    else:
        return query_kth(k - x, mid + 1, r, right[u], right[v])

def main():
    global cnt
    
    data = input().split()
    idx = 0
    
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    # 读取任务信息
    events = []
    priority_set = set()
    time_set = set()
    
    for i in range(1, n + 1):
        S[i] = int(data[idx])
        idx += 1
        E[i] = int(data[idx])
        idx += 1
        P[i] = int(data[idx])
        idx += 1
        
        # 收集时间和优先级用于离散化
        time_set.add(S[i])
        time_set.add(E[i] + 1)
        priority_set.add(P[i])
    
    # 离散化处理
    sorted_times = sorted(list(time_set))
    sorted_priorities = sorted(list(priority_set))
    
    # 构建事件列表
    for i in range(1, n + 1):
        start_time_idx = bisect.bisect_left(sorted_times, S[i])
        end_time_idx = bisect.bisect_left(sorted_times, E[i] + 1)
        priority_idx = bisect.bisect_left(sorted_priorities, P[i]) + 1
        
        events.append(Event(start_time_idx, priority_idx, 1))
        events.append(Event(end_time_idx, priority_idx, -1))
    
    # 按时间排序事件
    events.sort(key=lambda x: (x.time, x.type))
    
    # 构建初始线段树
    root[0] = build(1, len(sorted_priorities))
    
    # 处理事件，构建可持久化线段树
    version = 0
    for event in events:
        while version < event.time:
            root[version + 1] = root[version]
            version += 1
        root[version] = insert(event.priority, 1, len(sorted_priorities), root[version], event.type)
    
    # 处理查询
    last_ans = 1
    for i in range(m):
        x = int(data[idx])
        idx += 1
        y = int(data[idx])
        idx += 1
        
        # 根据题目要求调整查询参数
        k = (last_ans + y) % n + 1
        
        # 找到对应时间点的版本
        time_idx = bisect.bisect_right(sorted_times, x) - 1
        if time_idx < 0:
            time_idx = 0
        
        # 查询第k小的优先级
        if sum_tree[root[time_idx]] < k:
            print(sorted_priorities[-1])
            last_ans = sorted_priorities[-1]
        else:
            pos = query_kth(k, 1, len(sorted_priorities), 0, root[time_idx])
            ans = sorted_priorities[pos - 1]
            print(ans)
            last_ans = ans

if __name__ == "__main__":
    main()