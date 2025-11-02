# 小Z的袜子 (普通莫队应用)
# 题目来源: 洛谷P1494 [国家集训队] 小Z的袜子
# 题目链接: https://www.luogu.com.cn/problem/P1494
# 题意: 给定一个长度为n的数组arr，一共有m条查询，查询 l r : arr[l..r]范围上，随机选不同位置的两个数，打印数值相同的概率，概率用分数的形式表达，并且约分到最简的形式
# 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询
# 时间复杂度: O((n + m) * sqrt(n))
# 空间复杂度: O(n)
# 适用场景: 区间概率计算问题

import math
import sys
from collections import defaultdict
from math import gcd

def main():
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
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
    cnt = defaultdict(int)  # 记录每个数字出现的次数
    sum_val = 0  # 当前区间内相同数字对的数量
    results = [(0, 1)] * m  # 存储结果，每个结果是一个分数(分子, 分母)
    
    # 删除元素
    def remove(pos):
        nonlocal sum_val
        sum_val -= cnt[arr[pos]] * (cnt[arr[pos]] - 1)
        cnt[arr[pos]] -= 1
        sum_val += cnt[arr[pos]] * (cnt[arr[pos]] - 1)
    
    # 添加元素
    def add(pos):
        nonlocal sum_val
        sum_val -= cnt[arr[pos]] * (cnt[arr[pos]] - 1)
        cnt[arr[pos]] += 1
        sum_val += cnt[arr[pos]] * (cnt[arr[pos]] - 1)
    
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
        
        # 计算概率
        if l == r:
            results[idx] = (0, 1)
        else:
            numerator = sum_val  # 相同数字对的数量
            denominator = (r - l + 1) * (r - l)  # 总的数字对数量
            
            # 约分到最简分数
            if numerator == 0:
                results[idx] = (0, 1)
            else:
                g = gcd(numerator, denominator)
                results[idx] = (numerator // g, denominator // g)
    
    # 输出结果
    for numerator, denominator in results:
        print(f"{numerator}/{denominator}")

if __name__ == "__main__":
    main()