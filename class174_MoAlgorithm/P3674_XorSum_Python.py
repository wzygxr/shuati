# 小清新人渣的本愿 (普通莫队应用 - Bitset优化)
# 题目来源: 洛谷P3674 小清新人渣的本愿
# 题目链接: https://www.luogu.com.cn/problem/P3674
# 题意: 给你一个序列 a，长度为 n，有 m 次操作，每次询问一个区间是否可以选出两个数它们的差为 x，或者询问一个区间是否可以选出两个数它们的和为 x，或者询问一个区间是否可以选出两个数它们的乘积为 x ，这三个操作分别为操作 1,2,3。
# 算法思路: 使用普通莫队算法，结合bitset优化区间查询
# 时间复杂度: O((n + m) * sqrt(n) * c / 32)，其中c为值域大小
# 空间复杂度: O(n + c)
# 适用场景: 区间和、差、积查询问题

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 为了方便处理，将数组下标从1开始
    arr = [0] + arr
    
    queries = []
    for i in range(m):
        opt, l, r, x = map(int, sys.stdin.readline().split())
        queries.append((l, r, x, opt, i))
    
    # 莫队算法实现
    block_size = int(math.sqrt(n))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, x, opt, idx = query
        return (l // block_size, r)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    cnt = defaultdict(int)  # 记录每个值出现的次数
    values = set()  # 记录当前区间中存在的值
    results = [False] * m  # 存储结果
    
    # 添加元素
    def add(pos):
        val = arr[pos]
        cnt[val] += 1
        values.add(val)
    
    # 删除元素
    def remove(pos):
        val = arr[pos]
        cnt[val] -= 1
        if cnt[val] == 0:
            values.discard(val)
    
    # 检查是否存在两个数的差为x
    def check_difference(x):
        # 检查是否存在 a - b = x，即 a = b + x
        for b in list(values):
            if b + x in values:
                # 特殊情况：x=0时需要至少有两个相同的数
                if x == 0 and cnt[b] >= 2:
                    return True
                elif x != 0:
                    return True
        return False
    
    # 检查是否存在两个数的和为x
    def check_sum(x):
        # 检查是否存在 a + b = x，即 b = x - a
        for a in list(values):
            if 0 <= x - a and x - a in values:
                # 特殊情况：a = x-a时需要至少有两个相同的数
                if a == x - a and cnt[a] >= 2:
                    return True
                elif a != x - a:
                    return True
        return False
    
    # 检查是否存在两个数的乘积为x
    def check_product(x):
        # 检查是否存在 a * b = x
        for a in list(values):
            if a != 0 and x % a == 0:
                b = x // a
                if b in values:
                    # 特殊情况：a = b时需要至少有两个相同的数
                    if a == b and cnt[a] >= 2:
                        return True
                    elif a != b:
                        return True
        return False
    
    # 处理查询
    cur_l, cur_r = 1, 0
    
    for l, r, x, opt, idx in queries:
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
        
        # 根据操作类型检查结果
        if opt == 1:  # 差
            results[idx] = check_difference(x)
        elif opt == 2:  # 和
            results[idx] = check_sum(x)
        elif opt == 3:  # 积
            results[idx] = check_product(x)
    
    # 输出结果
    for result in results:
        print("hana" if result else "bi")

if __name__ == "__main__":
    main()