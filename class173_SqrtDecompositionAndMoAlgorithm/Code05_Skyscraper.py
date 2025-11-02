# 雅加达的摩天楼问题 - 分块算法优化BFS (Python版本)
# 题目来源: https://www.luogu.com.cn/problem/P3645
# 题目来源: https://uoj.ac/problem/111
# 题目大意: 有n个大楼，编号0~n-1，有m个狗子，编号0~m-1
# 每只狗子有两个参数，idx表示狗子的初始大楼，jump表示狗子的跳跃能力
# 狗子在i位置，可以来到 i - jump 或 i + jump，向左向右自由跳跃，但不能越界
# 0号狗子有消息希望传给1号狗子，所有狗子都可帮忙，返回至少传送几次，无法送达打印-1
# 约束条件: 1 <= n、m <= 30000

import sys
from collections import deque

# BFS节点类，记录当前位置、跳跃能力和已用时间
class Node:
    def __init__(self, idx, jump, time):
        self.idx = idx
        self.jump = jump
        self.time = time

# 定义最大数组长度
MAXN = 30001

# 全局变量
n, m = 0, 0

# 邻接表存储每个大楼拥有的狗子列表
# head[i]: 大楼i的狗子链表头节点
head = [0] * MAXN

# next[i]: 链表中第i个节点的下一个节点
next_arr = [0] * MAXN

# to[i]: 链表中第i个节点存储的跳跃能力
to = [0] * MAXN

# cnt: 链表节点计数器
cnt = 0

# bfs过程使用的队列
que = deque()

# vis[idx]是个集合，可以表示vis[idx][jump]是否出现过
# 用于避免重复访问相同状态（位置+跳跃能力）
vis = [set() for _ in range(MAXN)]

def add(idx, jump):
    """
    添加狗子到邻接表
    参数:
        idx: 大楼编号
        jump: 跳跃能力
    """
    global cnt
    # 创建新节点
    cnt += 1
    next_arr[cnt] = head[idx]
    to[cnt] = jump
    head[idx] = cnt

def trigger(idx, time):
    """
    触发大楼idx中的所有狗子
    参数:
        idx: 大楼编号
        time: 当前时间
    """
    global que
    # 遍历大楼idx中的所有狗子
    e = head[idx]
    while e > 0:
        jump = to[e]
        # 如果这个状态（位置+跳跃能力）没有访问过
        if jump not in vis[idx]:
            # 标记为已访问
            vis[idx].add(jump)
            # 加入队列
            que.append(Node(idx, jump, time))
        e = next_arr[e]
    
    # 清空该大楼的狗子列表，避免重复处理
    head[idx] = 0

def extend(idx, jump, time):
    """
    扩展状态
    参数:
        idx: 大楼编号
        jump: 跳跃能力
        time: 当前时间
    """
    # 触发该大楼的所有狗子
    trigger(idx, time)
    
    # 如果这个状态（位置+跳跃能力）没有访问过
    if jump not in vis[idx]:
        # 标记为已访问
        vis[idx].add(jump)
        # 加入队列
        que.append(Node(idx, jump, time))

def bfs(s, t):
    """
    BFS搜索最短路径
    参数:
        s: 起始大楼
        t: 目标大楼
    返回:
        最少传送次数，无法送达返回-1
    """
    global que
    # 如果起始和目标相同，不需要传送
    if s == t:
        return 0
    
    # 初始化vis数组
    for i in range(n):
        vis[i].clear()
    
    # 清空队列
    que.clear()
    
    # 触发起始大楼的所有狗子
    trigger(s, 0)
    
    # BFS过程
    while que:
        # 取出队首节点
        cur = que.popleft()
        idx = cur.idx
        jump = cur.jump
        time = cur.time
        
        # 如果向左或向右跳跃能到达目标大楼
        if idx - jump == t or idx + jump == t:
            # 返回传送次数+1
            return time + 1
        
        # 向左跳跃
        if idx - jump >= 0:
            extend(idx - jump, jump, time + 1)
        
        # 向右跳跃
        if idx + jump < n:
            extend(idx + jump, jump, time + 1)
    
    # 无法送达
    return -1

def main():
    global n, m
    # 读取大楼数量n和狗子数量m
    n, m = map(int, sys.stdin.readline().split())
    
    # 读取起始狗子和目标狗子的信息
    s, sjump, t, tjump = map(int, sys.stdin.readline().split())
    
    # 添加起始和目标狗子
    add(s, sjump)
    add(t, tjump)
    
    # 读取其他狗子的信息
    for i in range(2, m):
        idx, jump = map(int, sys.stdin.readline().split())
        add(idx, jump)
    
    # BFS搜索最短路径
    print(bfs(s, t))

if __name__ == "__main__":
    main()