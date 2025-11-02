# Powerful array问题 - Mo算法实现 (Python版本)
# 题目来源: https://codeforces.com/problemset/problem/86/D
# 题目大意: 给定一个长度为n的数组arr，有q次查询，每次查询[l,r]区间的加权和
# 权重计算: 对于区间[l,r]内的每个数字x，如果它出现了c[x]次，则贡献为c[x]^2*x
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

# curAns: 当前窗口的加权和
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
    设计思路: 当向窗口中添加一个元素时，需要更新该元素的计数和总贡献值
    由于贡献计算公式为c[x]^2*x，添加元素时需要先减去旧贡献，更新计数，再加新贡献
    参数:
        pos: 位置
    """
    global curAns
    # 移除旧贡献: count[arr[pos]]^2 * arr[pos]
    curAns -= count[arr[pos]] * count[arr[pos]] * arr[pos]
    count[arr[pos]] += 1
    # 添加新贡献: count[arr[pos]]^2 * arr[pos]
    curAns += count[arr[pos]] * count[arr[pos]] * arr[pos]

def remove(pos):
    """
    从当前窗口移除元素
    时间复杂度: O(1)
    设计思路: 当从窗口中移除一个元素时，需要更新该元素的计数和总贡献值
    由于贡献计算公式为c[x]^2*x，移除元素时需要先减去旧贡献，更新计数，再加新贡献
    参数:
        pos: 位置
    """
    global curAns
    # 移除旧贡献: count[arr[pos]]^2 * arr[pos]
    curAns -= count[arr[pos]] * count[arr[pos]] * arr[pos]
    count[arr[pos]] -= 1
    # 添加新贡献: count[arr[pos]]^2 * arr[pos]
    curAns += count[arr[pos]] * count[arr[pos]] * arr[pos]

def main():
    global n, q, blen, curAns
    
    # 读取数组长度n和查询次数q
    line = input().split()
    n = int(line[0])
    q = int(line[1])
    
    # 读取初始数组
    temp = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = temp[i - 1]

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