# DQUERY - D-query (普通莫队模板题)
# 题目来源: SPOJ SP3267
# 题目链接: https://www.spoj.com/problems/DQUERY/
# 洛谷链接: https://www.luogu.com.cn/problem/SP3267
# 题意: 给定一个长度为n的数组，每次查询一个区间[l,r]，求该区间内不同数字的个数
# 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
# 时间复杂度: O((n + q) * sqrt(n))
# 空间复杂度: O(n)
# 适用场景: 区间不同元素个数统计问题

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 为了方便处理，将数组下标从1开始
    arr = [0] + arr
    
    q = int(sys.stdin.readline())
    queries = []
    for i in range(q):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 莫队算法实现
    block_size = int(math.sqrt(n))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, idx = query
        return (l // block_size, r)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    cnt = defaultdict(int)  # 记录每个数字出现的次数
    answer = 0  # 当前区间不同数字的个数
    results = [0] * q  # 存储结果
    
    # 添加元素
    def add(pos):
        nonlocal answer
        if cnt[arr[pos]] == 0:
            answer += 1
        cnt[arr[pos]] += 1
    
    # 删除元素
    def remove(pos):
        nonlocal answer
        cnt[arr[pos]] -= 1
        if cnt[arr[pos]] == 0:
            answer -= 1
    
    # 处理查询
    cur_l, cur_r = 1, 0
    
    for l, r, idx in queries:
        # 扩展右边界
        while cur_r < r:
            cur_r += 1
            add(cur_r)
        
        # 收缩右边界
        while cur_r > r:
            remove(cur_r)
            cur_r -= 1
        
        # 收缩左边界
        while cur_l < l:
            remove(cur_l)
            cur_l += 1
        
        # 扩展左边界
        while cur_l > l:
            cur_l -= 1
            add(cur_l)
        
        results[idx] = answer
    
    # 输出结果
    for result in results:
        print(result)

if __name__ == "__main__":
    main()