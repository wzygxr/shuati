# One Occurrence - 普通莫队算法实现 (Python版本)
# 题目来源: Codeforces 1000F One Occurrence
# 题目链接: https://codeforces.com/problemset/problem/1000/F
# 题目大意: 给定一个数组，每次查询区间[l,r]中恰好出现一次的元素，如果有多个，输出任意一个，否则输出0
# 时间复杂度: O(n*sqrt(n))
# 空间复杂度: O(n)

import sys
import math
import heapq

def main():
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    arr = [0] * (n + 1)  # 1-based索引
    for i in range(1, n + 1):
        arr[i] = int(input[ptr])
        ptr += 1
    
    m = int(input[ptr])
    ptr += 1
    
    queries = []
    for i in range(m):
        l = int(input[ptr])
        r = int(input[ptr + 1])
        queries.append( (l, r, i) )
        ptr += 2
    
    # 分块
    block_size = int(math.sqrt(n)) + 1
    bi = [0] * (n + 1)
    for i in range(1, n + 1):
        bi[i] = (i - 1) // block_size
    
    # 查询排序 - 使用奇偶优化
    def query_cmp(q):
        l, r, idx = q
        if bi[l] % 2 == 0:
            return (bi[l], r)
        else:
            return (bi[l], -r)
    
    queries.sort(key=query_cmp)
    
    # 初始化变量
    cnt = dict()  # 记录每种数值的出现次数
    unique_elements = set()  # 维护当前区间中恰好出现一次的元素
    ans = [0] * m
    win_l, win_r = 1, 0
    
    # 添加元素到区间
    def add(value):
        nonlocal cnt, unique_elements
        if value in cnt:
            if cnt[value] == 1:
                # 如果之前出现过一次，现在出现第二次，需要从unique集合中移除
                unique_elements.discard(value)
            cnt[value] += 1
        else:
            # 如果之前没出现过，现在出现第一次，需要加入unique集合
            cnt[value] = 1
            unique_elements.add(value)
    
    # 从区间中删除元素
    def delete(value):
        nonlocal cnt, unique_elements
        cnt[value] -= 1
        if cnt[value] == 1:
            # 如果删除后只出现一次，需要加入unique集合
            unique_elements.add(value)
        elif cnt[value] == 0:
            # 如果删除后不出现了，需要从unique集合中移除
            unique_elements.discard(value)
            del cnt[value]  # 优化空间，删除计数为0的元素
    
    # 处理每个查询
    for l, r, idx in queries:
        # 移动指针
        while win_r < r:
            win_r += 1
            add(arr[win_r])
        while win_l > l:
            win_l -= 1
            add(arr[win_l])
        while win_r > r:
            delete(arr[win_r])
            win_r -= 1
        while win_l < l:
            delete(arr[win_l])
            win_l += 1
        
        # 记录答案
        if unique_elements:
            # Python中set是无序的，取第一个元素
            ans[idx] = next(iter(unique_elements))
        else:
            ans[idx] = 0
    
    # 输出答案
    sys.stdout.write('\n'.join(map(str, ans)) + '\n')

if __name__ == "__main__":
    main()