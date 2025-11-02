# XOR and Favorite Number, Python version
# 题目来源: Codeforces
# 题目链接: https://codeforces.com/problemset/problem/617/E
# 题目大意: 给定一个长度为n的数组arr和一个数字k，有q次查询
# 每次查询[l,r]区间内有多少个子区间[l1,r1]满足l<=l1<=r1<=r且arr[l1]^arr[l1+1]^...^arr[r1]=k
# 约束条件: 1 <= n, q <= 10^5, 0 <= k, arr[i] <= 10^6
# 解法: Mo算法（离线分块）+ 异或前缀和
# 时间复杂度: O((n + q) * sqrt(n))
# 空间复杂度: O(n + V), 其中V为值域大小(10^6)
# 是否最优解: 是，Mo算法是解决此类区间查询问题的经典方法

import math

MAXN = 100005
MAXV = 1000005
n, q, k, blen = 0, 0, 0, 0
arr = [0] * MAXN
ans = [0] * MAXN
count = [0] * MAXV  # 计数数组
curAns = 0  # 当前答案
prefixXor = [0] * MAXN  # 异或前缀和数组

# 查询结构
class Query:
    def __init__(self, l, r, id):
        self.l = l
        self.r = r
        self.id = id

queries = []

# 添加元素到当前窗口
# 时间复杂度: O(1)
# 设计思路: 当向窗口中添加一个元素时，需要更新该元素的计数和满足条件的子区间数量
# 如果当前前缀异或值为x，那么我们需要查找之前出现过多少次x^k
def add(pos):
    global curAns
    # pos位置对应的前缀异或值
    xorVal = prefixXor[pos]
    # 查找之前出现过多少次xorVal^k
    curAns += count[xorVal ^ k]
    # 更新计数
    count[xorVal] += 1

# 从当前窗口移除元素
# 时间复杂度: O(1)
# 设计思路: 当从窗口中移除一个元素时，需要先更新计数，再更新满足条件的子区间数量
def remove(pos):
    global curAns
    # pos位置对应的前缀异或值
    xorVal = prefixXor[pos]
    # 更新计数
    count[xorVal] -= 1
    # 查找之前出现过多少次xorVal^k
    curAns -= count[xorVal ^ k]

# 查询比较函数，用于Mo算法排序
def compare(query):
    block = (query.l - 1) // blen
    return (block, query.r)

# Mo算法主函数
# 时间复杂度: O((n + q) * sqrt(n))
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
    global n, q, k, blen
    
    # 读取输入
    line = input().split()
    n = int(line[0])
    q = int(line[1])
    k = int(line[2])
    
    line = input().split()
    for i in range(1, n + 1):
        arr[i] = int(line[i - 1])
    
    # 计算异或前缀和
    prefixXor[0] = 0
    for i in range(1, n + 1):
        prefixXor[i] = prefixXor[i - 1] ^ arr[i]
    
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