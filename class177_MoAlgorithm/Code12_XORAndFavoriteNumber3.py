# XOR and Favorite Number - 普通莫队算法实现 (Python版本)
# 题目来源: Codeforces 617E - XOR and Favorite Number
# 题目链接: https://codeforces.com/problemset/problem/617/E
# 题目大意: 给定一个长度为n的数组和一个目标值k，每次查询区间[l,r]内有多少对(i,j)满足i<=j，
# 使得a[i] XOR a[i+1] XOR ... XOR a[j] = k
# 解题思路: 使用前缀异或和 + 莫队算法
# 时间复杂度: O(n*sqrt(n))
# 空间复杂度: O(n)

import math
import sys

def main():
    # 读取输入
    line = sys.stdin.readline().split()
    n, m, k = int(line[0]), int(line[1]), int(line[2])
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 存储查询
    queries = []
    for i in range(m):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 莫队算法预处理
    block_size = int(math.sqrt(n))
    
    # 查询排序函数
    def query_sort_key(query):
        l, r, idx = query
        block_id = (l - 1) // block_size
        if block_id % 2 == 1:
            return (block_id, r)
        else:
            return (block_id, -r)
    
    # 按照莫队算法的顺序排序查询
    queries.sort(key=query_sort_key)
    
    # 计算前缀异或和
    pre = [0] * (n + 1)
    for i in range(1, n + 1):
        pre[i] = pre[i - 1] ^ arr[i - 1]  # arr索引从0开始
    
    # 初始化变量
    cnt = [0] * (1 << 20)  # 记录每种前缀异或值的出现次数 (2^20 > 10^6)
    cur_ans = 0  # 当前区间的答案
    answers = [0] * m  # 存储答案
    
    # 当前维护的区间 [win_l, win_r]，对应前缀异或区间
    win_l, win_r = 1, 0
    
    # 处理每个查询
    for job_l, job_r, idx in queries:
        # 注意：查询区间是[job_l, job_r]，对应前缀异或区间是[job_l-1, job_r]
        # 调整右边界
        while win_r < job_r:
            win_r += 1
            prefix = pre[win_r]  # 前缀异或值
            # 根据异或性质，如果要找区间[i,j]异或值为k，
            # 即pre[j] XOR pre[i-1] = k，也就是pre[i-1] = pre[j] XOR k
            # 所以我们要统计有多少个pre[i-1]满足这个条件
            cur_ans += cnt[prefix ^ k]
            cnt[prefix] += 1
        
        # 收缩左边界
        while win_l < job_l:
            prefix = pre[win_l - 1]  # 前缀异或值
            cnt[prefix] -= 1
            cur_ans -= cnt[prefix ^ k]
            win_l += 1
        
        # 扩展左边界
        while win_l > job_l:
            win_l -= 1
            prefix = pre[win_l - 1]  # 前缀异或值
            # 根据异或性质，如果要找区间[i,j]异或值为k，
            cur_ans += cnt[prefix ^ k]
            cnt[prefix] += 1
        
        # 收缩右边界
        while win_r > job_r:
            prefix = pre[win_r]  # 前缀异或值
            cnt[prefix] -= 1
            cur_ans -= cnt[prefix ^ k]
            win_r -= 1
        
        answers[idx] = cur_ans
    
    # 输出答案
    for ans in answers:
        print(ans)

if __name__ == "__main__":
    main()