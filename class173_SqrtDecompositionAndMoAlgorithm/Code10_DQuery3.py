# D-query问题 - Mo算法实现 (Python版本)
# 题目来源: https://www.spoj.com/problems/DQUERY/
# 题目大意: 给定一个长度为n的数组arr，有q次查询，每次查询[l,r]区间内不同数字的个数
# 约束条件: 1 <= n, q <= 2*10^5, 1 <= arr[i] <= 10^6
# 解法: Mo算法（离线分块）
# 时间复杂度: O((n + q) * sqrt(n))
# 空间复杂度: O(n + V), 其中V为值域大小(10^6)
# 是否最优解: 是，Mo算法是解决此类区间查询问题的经典方法

import math

# 定义最大数组长度和值域大小
MAXN = 200005
MAXV = 1000005

# 全局变量
n, q, blen = 0, 0, 0

# arr: 原始数组
arr = [0] * MAXN

# ans: 存储每个查询的答案
ans = [0] * MAXN

# count: 计数数组，记录当前窗口中每个数字的出现次数
count = [0] * MAXV

# curAns: 当前窗口中不同数字的个数
curAns = 0

# 查询结构
class Query:
    def __init__(self, l, r, id):
        self.l = l
        self.r = r
        self.id = id

# queries: 存储所有查询
queries = []

def add(pos):
    """
    添加元素到当前窗口
    时间复杂度: O(1)
    设计思路: 当向窗口中添加一个元素时，需要更新该元素的计数和不同数字的总数
    如果该元素之前在窗口中出现次数为0，说明是新数字，不同数字总数加1
    参数:
        pos: 位置
    """
    global curAns
    # 如果之前该数字出现次数为0，说明是新数字
    if count[arr[pos]] == 0:
        curAns += 1
    count[arr[pos]] += 1

def remove(pos):
    """
    从当前窗口移除元素
    时间复杂度: O(1)
    设计思路: 当从窗口中移除一个元素时，需要更新该元素的计数和不同数字的总数
    如果移除后该元素在窗口中出现次数为0，说明少了一个不同数字，不同数字总数减1
    参数:
        pos: 位置
    """
    global curAns
    count[arr[pos]] -= 1
    # 如果移除后该数字出现次数为0，说明少了一个不同数字
    if count[arr[pos]] == 0:
        curAns -= 1

def main():
    global n, q, blen
    
    # 读取数组长度n
    n = int(input())
    
    # 读取初始数组
    temp = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = temp[i - 1]

    # 读取查询次数q
    q = int(input())
    
    # 读取所有查询
    for i in range(1, q + 1):
        l, r = map(int, input().split())
        queries.append(Query(l, r, i))

    # 使用Mo算法进行排序
    # 块大小选择: sqrt(n)，这是经过理论分析得出的最优块大小
    # 时间复杂度: O(q * log(q))，主要是排序的复杂度
    # 排序策略: 按照左端点所在的块编号排序，块内按右端点排序
    # 这样可以最小化指针移动的次数，从而优化整体时间复杂度
    blen = int(math.sqrt(n))
    
    def compare(query):
        block = (query.l - 1) // blen
        return (block, query.r)
    
    # 对查询进行排序
    queries.sort(key=compare)
    
    # Mo算法处理
    curL, curR = 1, 0
    for i in range(q):
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
        ans[id] = curAns

    # 输出所有查询的结果
    for i in range(1, q + 1):
        print(ans[i])

if __name__ == "__main__":
    main()