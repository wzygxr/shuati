# Destiny问题 - 分块算法实现 (Python版本)
# 题目来源: Codeforces
# 题目链接: https://codeforces.com/problemset/problem/840/D
# 题目大意: 给定一个数组，多次查询区间[l,r]内出现次数超过(r-l+1)/k的数字
# 约束条件: 1 <= n, q <= 3*10^5, 2 <= k <= 5
# 解法: 分块维护频率信息
# 时间复杂度: O((n + q) * sqrt(n))
# 空间复杂度: O(n)
# 是否最优解: 是，分块算法是解决此类区间查询问题的有效方法

import math
from collections import defaultdict

MAXN = 300005
n, q, k, blen = 0, 0, 0, 0
arr = [0] * MAXN
ans = [0] * MAXN

# 分块相关数组
belong = [0] * MAXN  # 每个位置属于哪个块
blockL = [0] * MAXN  # 每个块的左边界
blockR = [0] * MAXN  # 每个块的右边界
bcnt = 0  # 块的数量

# 块内频率信息，blockFreq[i]存储第i个块中每个数字的出现次数
blockFreq = [defaultdict(int) for _ in range(MAXN)]

# 构建分块结构
# 时间复杂度: O(n)
# 设计思路: 将数组分成大小约为sqrt(n)的块，预处理每个块内元素的频率信息
def build():
    global blen, bcnt
    
    blen = int(math.sqrt(n))
    bcnt = (n - 1) // blen + 1
    
    # 初始化块信息
    for i in range(1, bcnt + 1):
        blockL[i] = (i - 1) * blen + 1
        blockR[i] = min(i * blen, n)
    
    # 计算每个位置属于哪个块
    for i in range(1, n + 1):
        belong[i] = (i - 1) // blen + 1
    
    # 计算每个块内元素的频率
    for i in range(1, bcnt + 1):
        for j in range(blockL[i], blockR[i] + 1):
            blockFreq[i][arr[j]] += 1

# 查询区间[l,r]内出现次数超过(r-l+1)/k的数字
# 时间复杂度: O(sqrt(n) + 候选数字个数)
# 设计思路: 利用预处理的块频率信息快速计算候选数字，然后验证候选数字是否满足条件
def query(l, r):
    len_query = r - l + 1
    threshold = len_query // k
    
    # 候选数字集合
    candidates = defaultdict(int)
    
    lb = belong[l]
    rb = belong[r]
    
    # 如果在同一个块内，暴力计算
    if lb == rb:
        for i in range(l, r + 1):
            candidates[arr[i]] += 1
    else:
        # 添加左边不完整块的元素
        for i in range(l, blockR[lb] + 1):
            candidates[arr[i]] += 1
        
        # 添加中间完整块的频率信息
        for i in range(lb + 1, rb):
            for num, freq in blockFreq[i].items():
                candidates[num] += freq
        
        # 添加右边不完整块的元素
        for i in range(blockL[rb], r + 1):
            candidates[arr[i]] += 1
    
    # 检查候选数字
    for num, freq in candidates.items():
        if freq > threshold:
            return num
    
    return -1  # 没有满足条件的数字

def main():
    global n, q, k
    
    # 读取输入
    line = input().split()
    n = int(line[0])
    q = int(line[1])
    k = int(line[2])
    
    line = input().split()
    for i in range(1, n + 1):
        arr[i] = int(line[i - 1])
    
    # 构建分块结构
    build()
    
    # 处理查询
    for i in range(1, q + 1):
        line = input().split()
        l = int(line[0])
        r = int(line[1])
        ans[i] = query(l, r)
    
    # 输出结果
    for i in range(1, q + 1):
        print(ans[i])

if __name__ == "__main__":
    main()