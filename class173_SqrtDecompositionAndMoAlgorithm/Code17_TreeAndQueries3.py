# Tree and Queries问题 - 树上Mo算法实现 (Python版本)
# 题目来源: Codeforces
# 题目链接: https://codeforces.com/problemset/problem/375/D
# 题目大意: 给定一棵树，每个节点有一个颜色，有q次查询
# 每次查询某个子树内出现次数>=k的颜色数量
# 约束条件: 1 <= n, q <= 10^5
# 解法: 树上Mo算法（离线分块）+ 欧拉序
# 时间复杂度: O((n + q) * sqrt(n))
# 空间复杂度: O(n)
# 是否最优解: 是，树上Mo算法是解决此类树上区间查询问题的经典方法

import math
from collections import defaultdict

MAXN = 100005
n, q, blen = 0, 0, 0
color = [0] * MAXN  # 节点颜色数组
ans = [0] * MAXN
count = [0] * MAXN  # 颜色计数数组，记录每种颜色在当前窗口中的出现次数
colorCount = [0] * MAXN  # 颜色出现次数的计数数组，记录出现i次的颜色数量
curAns = 0  # 当前窗口中满足条件的颜色数量

# 邻接表存储树结构
graph = defaultdict(list)

# 欧拉序相关变量
euler = [0] * (2 * MAXN)  # 欧拉序数组，记录DFS访问节点的顺序
first = [0] * MAXN  # 节点第一次出现在欧拉序中的位置
last = [0] * MAXN  # 节点最后一次出现在欧拉序中的位置
eulerIdx = 0  # 欧拉序索引

# 查询结构体，用于存储查询信息
class Query:
    def __init__(self, l, r, k, id):
        self.l = l  # 查询区间左边界
        self.r = r  # 查询区间右边界
        self.k = k  # 颜色出现次数阈值
        self.id = id  # 查询编号

queries = []

# 添加元素到当前窗口
# 时间复杂度: O(1)
# 设计思路: 更新颜色计数和颜色出现次数的计数
def add(pos):
    global curAns
    col = color[euler[pos]]
    # 更新颜色出现次数的计数
    colorCount[count[col]] -= 1
    count[col] += 1
    colorCount[count[col]] += 1

# 从当前窗口移除元素
# 时间复杂度: O(1)
# 设计思路: 更新颜色计数和颜色出现次数的计数
def remove(pos):
    global curAns
    col = color[euler[pos]]
    # 更新颜色出现次数的计数
    colorCount[count[col]] -= 1
    count[col] -= 1
    colorCount[count[col]] += 1

# DFS生成欧拉序
# 时间复杂度: O(n)
# 设计思路: 通过DFS遍历树，记录每个节点的进入和离开时间，形成欧拉序
# 这样可以将子树查询转换为区间查询
def dfs(u, parent):
    global eulerIdx
    eulerIdx += 1
    euler[eulerIdx] = u
    first[u] = eulerIdx
    
    for v in graph[u]:
        if v != parent:
            dfs(v, u)
    
    eulerIdx += 1
    euler[eulerIdx] = u
    last[u] = eulerIdx

# 查询比较函数，用于Mo算法排序
# 设计思路: 按照块编号排序，同一块内按右端点排序，这样可以最小化指针移动次数
def compare(query):
    block = (query.l - 1) // blen
    return (block, query.r)

# Mo算法主函数
# 时间复杂度: O((n + q) * sqrt(n))
# 设计思路: 通过巧妙的排序策略，使得相邻查询之间的指针移动次数最少
def moAlgorithm():
    global curAns
    
    # 对查询进行排序
    queries.sort(key=compare)
    
    # Mo算法处理
    curL, curR = 1, 0
    for i in range(q):
        l = queries[i].l
        r = queries[i].r
        k = queries[i].k
        id = queries[i].id
        
        # 扩展右边界
        while curR < r:
            curR += 1
            add(curR)
        
        # 收缩右边界
        while curR > r:
            remove(curR)
            curR -= 1
        
        # 收缩左边界
        while curL < l:
            remove(curL)
            curL += 1
        
        # 扩展左边界
        while curL > l:
            curL -= 1
            add(curL)
        
        # 计算答案：出现次数>=k的颜色数量
        result = 0
        for j in range(k, MAXN):
            result += colorCount[j]
        ans[id] = result

def main():
    global n, q, blen, eulerIdx
    
    # 读取输入
    line = input().split()
    n = int(line[0])
    q = int(line[1])
    
    line = input().split()
    for i in range(1, n + 1):
        color[i] = int(line[i - 1])
    
    # 读取边
    for i in range(1, n):
        line = input().split()
        u = int(line[0])
        v = int(line[1])
        graph[u].append(v)
        graph[v].append(u)
    
    # 生成欧拉序
    eulerIdx = 0
    dfs(1, 0)
    
    # 读取查询
    for i in range(1, q + 1):
        line = input().split()
        v = int(line[0])
        k = int(line[1])
        # 转换为欧拉序上的区间查询
        queries.append(Query(first[v], last[v], k, i))
    
    # 计算块大小
    blen = int(math.sqrt(2 * n))
    
    # Mo算法处理
    moAlgorithm()
    
    # 输出结果
    for i in range(1, q + 1):
        print(ans[i])

if __name__ == "__main__":
    main()