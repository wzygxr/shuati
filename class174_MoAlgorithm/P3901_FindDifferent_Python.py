# 数列找不同 (普通莫队应用)
# 题目来源: 洛谷P3901 数列找不同
# 题目链接: https://www.luogu.com.cn/problem/P3901
# 题意: 现有数列 A1,A2,...,AN，Q 个询问 (Li,Ri)，询问 ALi,ALi+1,...,ARi 是否互不相同。
# 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
# 时间复杂度: O((n + q) * sqrt(n))
# 空间复杂度: O(n)
# 适用场景: 区间元素互异性判断问题

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, q = map(int, sys.stdin.readline().split())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 为了方便处理，将数组下标从1开始
    arr = [0] + arr
    
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
    different_count = 0  # 当前区间不同数字的个数
    results = [False] * q  # 存储结果
    
    # 添加元素
    def add(pos):
        nonlocal different_count
        if cnt[arr[pos]] == 0:
            different_count += 1
        cnt[arr[pos]] += 1
    
    # 删除元素
    def remove(pos):
        nonlocal different_count
        cnt[arr[pos]] -= 1
        if cnt[arr[pos]] == 0:
            different_count -= 1
    
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
        
        # 判断区间内元素是否互不相同
        results[idx] = (different_count == (r - l + 1))
    
    # 输出结果
    for result in results:
        print("Yes" if result else "No")

if __name__ == "__main__":
    main()