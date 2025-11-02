# Little Elephant and Array问题 - Mo算法实现 (Python版本)
# 题目来源: https://codeforces.com/problemset/problem/220/B
# 题目大意: 给定一个长度为n的数组arr，有m次查询
# 每次查询[l,r]区间内，有多少个数x满足在该区间内x恰好出现了x次
# 约束条件: 
# 1 <= n, m <= 10^5
# 1 <= arr[i] <= 10^9

import math

# 定义最大数组长度
MAXN = 100005

# 全局变量
n, m, blen = 0, 0, 0

# arr: 原始数组
arr = [0] * MAXN

# ans: 存储每个查询的答案
ans = [0] * MAXN

# count: 记录当前窗口中每个数字的出现次数
count = {}

# 查询结构
class Query:
    def __init__(self, l, r, id):
        self.l = l
        self.r = r
        self.id = id

# queries: 存储所有查询
queries = []  # 使用列表存储查询

def add(pos):
    """
    添加元素到当前窗口
    参数:
        pos: 位置
    """
    val = arr[pos]
    oldCount = count.get(val, 0)
    
    # 如果之前恰好出现了val次，现在要减少一个计数
    if oldCount == val:
        ans[0] -= 1
    
    # 更新计数
    count[val] = oldCount + 1
    
    # 如果现在恰好出现了val次，增加一个计数
    if count[val] == val:
        ans[0] += 1

def remove(pos):
    """
    从当前窗口移除元素
    参数:
        pos: 位置
    """
    val = arr[pos]
    oldCount = count[val]
    
    # 如果之前恰好出现了val次，现在要减少一个计数
    if oldCount == val:
        ans[0] -= 1
    
    # 更新计数
    count[val] = oldCount - 1
    
    # 如果现在恰好出现了val次，增加一个计数
    if count[val] == val:
        ans[0] += 1

def main():
    global n, m, blen
    
    # 读取数组长度n和查询次数m
    n, m = map(int, input().split())
    
    # 读取初始数组
    temp = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = temp[i - 1]

    # 读取所有查询
    for i in range(1, m + 1):
        l, r = map(int, input().split())
        queries.append(Query(l, r, i))

    # 使用Mo算法进行排序
    # 按照左端点所在的块排序，块内按照右端点排序
    blen = int(math.sqrt(n))
    
    def compare(a):
        blockA = (a.l - 1) // blen
        return (blockA, a.r)
    
    queries.sort(key=compare)

    # Mo算法处理
    curL, curR = 1, 0
    for i in range(m):
        l = queries[i].l
        r = queries[i].r
        id = queries[i].id

        # 扩展右边界
        while curR < r:
            curR += 1
            add(curR)

        # 收缩左边界
        while curL > l:
            curL -= 1
            add(curL)

        # 收缩右边界
        while curR > r:
            remove(curR)
            curR -= 1

        # 扩展左边界
        while curL < l:
            remove(curL)
            curL += 1

        # 记录当前查询的答案
        ans[id] = ans[0]

    # 输出所有查询的结果
    for i in range(1, m + 1):
        print(ans[i])

if __name__ == "__main__":
    main()