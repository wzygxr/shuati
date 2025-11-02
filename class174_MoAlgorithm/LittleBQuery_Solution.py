# 小B的询问 (普通莫队应用)
# 题目来源: 洛谷P2709
# 题目链接: https://www.luogu.com.cn/problem/P2709
# 题意: 给定一个长度为n的数组，所有数字在[1..k]范围上
# 定义f(i) = i这种数的出现次数的平方
# 一共有m条查询，查询[l,r]范围内f(1) + f(2) + ... + f(k)的值
# 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
# 时间复杂度: O((n + m) * sqrt(n))
# 空间复杂度: O(n)
# 适用场景: 区间元素出现次数的统计信息计算

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
    
    # 莫队算法实现
    block_size = int(math.sqrt(n))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, idx = query
        return (l // block_size, r)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    cnt = [0] * (n + 1)  # 记录每个数字出现的次数
    sum_val = 0  # 当前区间f(1) + f(2) + ... + f(k)的值
    results = [0] * m  # 存储结果
    
    # 删除元素
    def remove(pos):
        nonlocal sum_val
        sum_val -= cnt[arr[pos]] * cnt[arr[pos]]
        cnt[arr[pos]] -= 1
        sum_val += cnt[arr[pos]] * cnt[arr[pos]]
    
    # 添加元素
    def add(pos):
        nonlocal sum_val
        sum_val -= cnt[arr[pos]] * cnt[arr[pos]]
        cnt[arr[pos]] += 1
        sum_val += cnt[arr[pos]] * cnt[arr[pos]]
    
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
        
        results[idx] = sum_val
    
    # 输出结果
    for result in results:
        print(result)

if __name__ == "__main__":
    main()