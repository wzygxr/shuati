# 莫队二次离线 - 二次离线莫队算法实现 (Python版本)
# 题目来源: 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)）
# 题目链接: https://www.luogu.com.cn/problem/P4887
# 题目大意: 给定一个序列，每次查询区间[l,r]内满足a[i] XOR a[j]的二进制表示有k个1的二元组(i,j)个数
# 时间复杂度: O(n*sqrt(m) + n*sqrt(n))
# 空间复杂度: O(n + m)
#
# 相关题目链接:
# 1. 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)） - https://www.luogu.com.cn/problem/P4887
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline3.py
#
# 2. 洛谷 P5398 [Ynoi2018]GOSICK - https://www.luogu.com.cn/problem/P5398
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5398_SecondaryOffline1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5398_SecondaryOffline2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5398_SecondaryOffline3.py
#
# 3. 洛谷 P5047 [Ynoi2019模拟赛]Yuno loves sqrt technology II - https://www.luogu.com.cn/problem/P5047
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5047_SecondaryOffline1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5047_SecondaryOffline2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code18_P5047_SecondaryOffline3.py
#
# 4. SPOJ COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo3.py
#
# 5. 洛谷 P1903 [国家集训队]数颜色/维护队列 - https://www.luogu.com.cn/problem/P1903
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
#
# 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
#
# 7. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
#
# 8. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
#
# 9. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
#
# 10. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
#     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
#     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
#     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, m, k = map(int, sys.stdin.readline().split())
    a = list(map(int, sys.stdin.readline().split()))
    
    # 读取查询
    queries = []
    for i in range(m):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 计算二进制中1的个数
    def count_bits(x):
        return bin(x).count('1')
    
    # 预处理所有可能的值与给定值异或后有k个1的数
    # 由于k <= 14，我们可以预处理所有可能的情况
    valid_pairs = defaultdict(list)
    for i in range(16384):  # 2^14
        for j in range(16384):
            if count_bits(i ^ j) == k:
                valid_pairs[i].append(j)
    
    # 二次离线莫队算法预处理
    block_size = int(math.sqrt(n))
    
    # 查询排序函数
    def query_sort_key(query):
        l, r, idx = query
        block_id = (l - 1) // block_size
        return (block_id, r)
    
    # 按照莫队算法的顺序排序查询
    sorted_queries = sorted(queries, key=query_sort_key)
    
    # 离线操作列表
    offline_ops = [[] for _ in range(n + 1)]
    
    # 初始化变量
    ans = [0] * m  # 答案数组
    
    # 生成离线操作
    l, r = 1, 0
    for ql, qr, idx in sorted_queries:
        # 转换为0索引
        ql -= 1
        qr -= 1
        
        # 扩展右端点
        while r < qr:
            r += 1
            # 添加离线操作：查询[ql, r-1]中与a[r]异或后有k个1的数的个数
            if ql <= r - 1:
                offline_ops[r].append((ql, r - 1, idx, 1, 0))  # 1表示增加，0表示前缀
        
        # 收缩右端点
        while r > qr:
            # 添加离线操作：查询[ql, qr]中与a[r]异或后有k个1的数的个数
            if ql <= qr:
                offline_ops[r].append((ql, qr, idx, -1, 0))  # -1表示减少，0表示前缀
            r -= 1
        
        # 扩展左端点
        while l > ql:
            l -= 1
            # 添加离线操作：查询[l+1, qr]中与a[l]异或后有k个1的数的个数
            if l + 1 <= qr:
                offline_ops[l].append((l + 1, qr, idx, 1, 1))  # 1表示增加，1表示后缀
        
        # 收缩左端点
        while l < ql:
            # 添加离线操作：查询[ql, qr]中与a[l]异或后有k个1的数的个数
            if ql <= qr:
                offline_ops[l].append((ql, qr, idx, -1, 1))  # -1表示减少，1表示后缀
            l += 1
    
    # 值域分块计数数组
    cnt_in_value = defaultdict(int)
    
    # 值域分块添加元素
    def add_value(x):
        cnt_in_value[x] += 1
    
    # 值域分块删除元素
    def del_value(x):
        cnt_in_value[x] -= 1
        if cnt_in_value[x] == 0:
            del cnt_in_value[x]
    
    # 查询值域分块中与x异或后有k个1的数的个数
    def query_count(x):
        res = 0
        for val in valid_pairs.get(x, []):
            res += cnt_in_value.get(val, 0)
        return res
    
    # 执行离线操作
    cnt_in_value.clear()
    
    # 从左到右扫描处理前缀操作
    for i in range(1, n + 1):
        add_value(a[i - 1])  # 转换为0索引
        for op_l, op_r, op_idx, op_sign, op_type in offline_ops[i]:
            if op_type == 0:  # 前缀操作
                count = query_count(a[i - 1])  # 转换为0索引
                ans[op_idx] += op_sign * count
    
    # 从右到左扫描处理后缀操作
    cnt_in_value.clear()
    
    for i in range(n, 0, -1):
        add_value(a[i - 1])  # 转换为0索引
        for op_l, op_r, op_idx, op_sign, op_type in offline_ops[i]:
            if op_type == 1:  # 后缀操作
                count = query_count(a[i - 1])  # 转换为0索引
                ans[op_idx] += op_sign * count
    
    # 计算前缀和得到最终答案
    for i in range(1, m):
        ans[i] += ans[i - 1]
    
    # 按照原始顺序输出答案
    final_ans = [0] * m
    for i, (ql, qr, idx) in enumerate(queries):
        final_ans[idx] = ans[i]
    
    # 输出答案
    for a in final_ans:
        print(a)

if __name__ == "__main__":
    main()