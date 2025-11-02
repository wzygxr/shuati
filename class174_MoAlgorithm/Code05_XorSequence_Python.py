# XOR and Favorite Number (普通莫队应用 - 异或和)
# 题目来源: Codeforces 617E XOR and Favorite Number
# 题目链接: https://codeforces.com/problemset/problem/617/E
# 题目链接: https://www.luogu.com.cn/problem/CF617E
# 题意: 给定一个长度为n的数组arr，给定一个数字k，一共有m条查询，查询 l r : arr[l..r]范围上，有多少子数组的异或和为k，打印其数量
# 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询，利用前缀异或和将区间异或和问题转化为点对问题
# 时间复杂度: O((n + m) * sqrt(n))
# 空间复杂度: O(n)
# 适用场景: 区间异或和查询问题

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, m, k = map(int, sys.stdin.readline().split())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 为了方便处理，将数组下标从1开始
    arr = [0] + arr
    
    queries = []
    for i in range(m):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 计算前缀异或和
    pre = [0] * (n + 1)
    for i in range(1, n + 1):
        pre[i] = pre[i - 1] ^ arr[i]
    
    # 莫队算法实现
    block_size = int(math.sqrt(n))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, idx = query
        return (l // block_size, r)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    cnt = defaultdict(int)  # 记录每个前缀异或和出现的次数
    num = 0  # 当前区间内异或和为k的子数组数量
    results = [0] * m  # 存储结果
    
    # 前缀异或和x要删除一次
    def delete_prefix(x):
        nonlocal num
        if k != 0:
            num -= cnt[x] * cnt[x ^ k]
        else:
            num -= (cnt[x] * (cnt[x] - 1)) // 2
        cnt[x] -= 1
        if k != 0:
            num += cnt[x] * cnt[x ^ k]
        else:
            num += (cnt[x] * (cnt[x] - 1)) // 2
    
    # 前缀异或和x要增加一次
    def add_prefix(x):
        nonlocal num
        if k != 0:
            num -= cnt[x] * cnt[x ^ k]
        else:
            num -= (cnt[x] * (cnt[x] - 1)) // 2
        cnt[x] += 1
        if k != 0:
            num += cnt[x] * cnt[x ^ k]
        else:
            num += (cnt[x] * (cnt[x] - 1)) // 2
    
    # 处理查询
    cur_l, cur_r = 1, 0
    
    for l, r, idx in queries:
        # 任务范围[l, r]，但是前缀可能性会多一种
        # 所以左边界-1
        job_l = l - 1
        job_r = r
        
        # 扩展右边界
        while cur_r < job_r:
            cur_r += 1
            add_prefix(pre[cur_r])
        
        # 收缩右边界
        while cur_r > job_r:
            delete_prefix(pre[cur_r])
            cur_r -= 1
        
        # 收缩左边界
        while cur_l < job_l:
            delete_prefix(pre[cur_l])
            cur_l += 1
        
        # 扩展左边界
        while cur_l > job_l:
            cur_l -= 1
            add_prefix(pre[cur_l])
        
        results[idx] = num
    
    # 输出结果
    for result in results:
        print(result)

if __name__ == "__main__":
    main()