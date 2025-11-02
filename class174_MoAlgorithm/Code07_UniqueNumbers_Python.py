# ADAUNIQ - Ada and Unique Vegetable (带修改莫队应用)
# 题目来源: SPOJ SP30906 ADAUNIQ - Ada and Unique Vegetable
# 题目链接: https://www.luogu.com.cn/problem/SP30906
# 题目链接: https://www.spoj.com/problems/ADAUNIQ/
# 题意: 给定一个长度为n的数组arr，下标0~n-1，一共有m条操作，格式如下：操作 1 pos val : 把arr[pos]的值设置成val；操作 2 l r : 查询arr[l..r]范围上，有多少种数出现了1次
# 算法思路: 使用带修改莫队算法，增加时间维度，将修改操作也纳入排序考虑
# 时间复杂度: O(n^(5/3))
# 空间复杂度: O(n)
# 适用场景: 带单点修改的区间查询问题

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 为了方便处理，将数组下标从1开始
    arr = [0] + arr
    
    queries = []  # 存储查询操作 (l, r, t, id)
    updates = []  # 存储修改操作 (pos, val)
    
    query_count = 0
    update_count = 0
    
    for _ in range(m):
        parts = sys.stdin.readline().split()
        op = int(parts[0])
        
        if op == 2:
            # 查询操作
            l = int(parts[1]) + 1  # 转换为1-based索引
            r = int(parts[2]) + 1  # 转换为1-based索引
            query_count += 1
            queries.append((l, r, update_count, query_count))
        else:
            # 修改操作
            pos = int(parts[1]) + 1  # 转换为1-based索引
            val = int(parts[2])
            update_count += 1
            updates.append((pos, val))
    
    # 带修改莫队算法实现
    block_size = max(1, int(n ** (2/3)))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, t, idx = query
        block_l = (l - 1) // block_size + 1
        block_r = (r - 1) // block_size + 1
        return (block_l, block_r, t)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    cnt = defaultdict(int)  # 每种数字的词频统计
    cur_cnt = [0]  # 出现次数1次的数有几种，使用列表包装以在内部函数中修改
    results = [0] * (query_count + 1)  # 存储结果
    
    # 删除元素
    def remove(num):
        if cnt[num] == 1:
            cur_cnt[0] -= 1
        if cnt[num] == 2:
            cur_cnt[0] += 1
        cnt[num] -= 1
    
    # 添加元素
    def add(num):
        if cnt[num] == 0:
            cur_cnt[0] += 1
        if cnt[num] == 1:
            cur_cnt[0] -= 1
        cnt[num] += 1
    
    # 执行或撤销修改操作
    def move_time(job_l, job_r, tim):
        pos, val = updates[tim - 1]  # tim从1开始，数组索引从0开始
        
        # 如果修改位置在当前查询区间内，需要更新答案
        if job_l <= pos <= job_r:
            remove(arr[pos])
            add(val)
        
        # 交换数组中的值和修改记录中的值
        arr[pos], updates[tim - 1] = val, (pos, arr[pos])
    
    # 处理查询
    cur_l, cur_r, cur_t = 1, 0, 0
    
    for l, r, t, idx in queries:
        # 扩展右边界
        while cur_r < r:
            cur_r += 1
            add(arr[cur_r])
        
        # 收缩右边界
        while cur_r > r:
            remove(arr[cur_r])
            cur_r -= 1
        
        # 收缩左边界
        while cur_l < l:
            remove(arr[cur_l])
            cur_l += 1
        
        # 扩展左边界
        while cur_l > l:
            cur_l -= 1
            add(arr[cur_l])
        
        # 处理时间戳
        while cur_t < t:
            cur_t += 1
            move_time(l, r, cur_t)
        
        while cur_t > t:
            move_time(l, r, cur_t)
            cur_t -= 1
        
        results[idx] = cur_cnt[0]
    
    # 输出结果
    for i in range(1, query_count + 1):
        print(results[i])

if __name__ == "__main__":
    main()