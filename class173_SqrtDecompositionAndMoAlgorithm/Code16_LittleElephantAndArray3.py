# Little Elephant and Array问题 - Mo算法实现 (Python版本)
# 题目来源: Codeforces
# 题目链接: https://codeforces.com/problemset/problem/220/B
# 题目大意: 给定一个长度为n的数组arr，有q次查询
# 每次查询[l,r]区间内有多少个数字x满足在该区间内恰好出现了x次
# 约束条件: 1 <= n, q <= 10^5, 1 <= arr[i] <= 10^9
# 解法: Mo算法（离线分块）
# 时间复杂度: O((n + q) * sqrt(n))
# 空间复杂度: O(n)
# 是否最优解: 是，Mo算法是解决此类区间查询问题的经典方法

import math

MAXN = 100005
n, q, blen = 0, 0, 0
arr = [0] * MAXN
ans = [0] * MAXN
count = [0] * MAXN  # 计数数组，记录每个数字在当前窗口中的出现次数
curAns = 0  # 当前窗口中满足条件的数字个数

# 查询结构体，用于存储查询信息
class Query:
    def __init__(self, l, r, id):
        self.l = l  # 查询左边界
        self.r = r  # 查询右边界
        self.id = id  # 查询编号

queries = []

# 添加元素到当前窗口
# 时间复杂度: O(1)
# 设计思路: 当向窗口中添加一个元素时，需要更新该元素的计数和满足条件的数字数量
# 如果添加前该数字出现次数等于它本身，说明它之前是满足条件的，现在不满足了，需要减1
# 如果添加后该数字出现次数等于它本身，说明它现在满足条件了，需要加1
def add(pos):
    global curAns
    val = arr[pos]
    # 边界处理，值域可能很大
    if val >= MAXN:
        return
    
    # 如果添加前该数字出现次数等于它本身，说明它之前是满足条件的，现在不满足了，需要减1
    if count[val] == val:
        curAns -= 1
    count[val] += 1
    # 如果添加后该数字出现次数等于它本身，说明它现在满足条件了，需要加1
    if count[val] == val:
        curAns += 1

# 从当前窗口移除元素
# 时间复杂度: O(1)
# 设计思路: 当从窗口中移除一个元素时，需要先更新满足条件的数字数量，再更新计数
def remove(pos):
    global curAns
    val = arr[pos]
    # 边界处理，值域可能很大
    if val >= MAXN:
        return
    
    # 如果移除前该数字出现次数等于它本身，说明它之前是满足条件的，现在不满足了，需要减1
    if count[val] == val:
        curAns -= 1
    count[val] -= 1
    # 如果移除后该数字出现次数等于它本身，说明它现在满足条件了，需要加1
    if count[val] == val:
        curAns += 1

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
            remove(curL - 1)
            curL += 1
        
        # 扩展左边界
        while curL > l:
            curL -= 1
            add(curL - 1)
        
        ans[id] = curAns

def main():
    global n, q, blen
    
    # 读取输入
    line = input().split()
    n = int(line[0])
    q = int(line[1])
    
    line = input().split()
    for i in range(1, n + 1):
        arr[i] = int(line[i - 1])
    
    # 读取查询
    for i in range(1, q + 1):
        line = input().split()
        l = int(line[0])
        r = int(line[1])
        queries.append(Query(l, r, i))
    
    # 计算块大小
    blen = int(math.sqrt(n))
    
    # Mo算法处理
    moAlgorithm()
    
    # 输出结果
    for i in range(1, q + 1):
        print(ans[i])

if __name__ == "__main__":
    main()