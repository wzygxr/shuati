# XOR and Favorite Number - 普通莫队算法实现 (Python版本)
# 题目来源: CodeForces 617E XOR and Favorite Number
# 题目链接: https://codeforces.com/problemset/problem/617/E
# 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内有多少对(i,j)满足i<=j且a[i]^a[i+1]^...^a[j]=k
# 时间复杂度: O(n*sqrt(n))
# 空间复杂度: O(n)
#
# 相关题目链接:
# 1. CodeForces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code19_XorFavorite1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code19_XorFavorite2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code19_XorFavorite3.py
#
# 2. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
#
# 3. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
#
# 4. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
#
# 5. 洛谷 P1972 [SDOI2009] HH的项链 - https://www.luogu.com.cn/problem/P1972
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery3.py
#
# 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
#
# 7. 洛谷 P1903 [国家集训队]数颜色/维护队列 - https://www.luogu.com.cn/problem/P1903
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
#
# 8. SPOJ COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo3.py
#
# 9. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py
#
# 10. 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)） - https://www.luogu.com.cn/problem/P4887
#     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline1.java
#     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline2.cpp
#     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline3.py

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, m, k = map(int, sys.stdin.readline().split())
    a = list(map(int, sys.stdin.readline().split()))
    
    # 存储查询
    queries = []
    for i in range(m):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 计算前缀异或和
    prefix_xor = [0] * (n + 1)
    for i in range(n):
        prefix_xor[i + 1] = prefix_xor[i] ^ a[i]
    
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
    
    # 初始化变量
    cnt = defaultdict(int)  # 计数数组
    cur_ans = 0  # 当前答案
    answers = [0] * m  # 答案数组
    
    # 初始化计数数组，空前缀的异或和为0
    cnt[0] = 1
    
    # 当前维护的区间 [win_l, win_r]
    win_l, win_r = 1, 0
    
    # 添加元素到区间
    def add(value):
        nonlocal cur_ans
        cur_ans += cnt[value ^ k]
        cnt[value] += 1
    
    # 从区间中删除元素
    def del_(value):
        nonlocal cur_ans
        cnt[value] -= 1
        cur_ans -= cnt[value ^ k]
    
    # 处理每个查询
    for job_l, job_r, idx in queries:
        # 调整左边界
        while win_l > job_l:
            win_l -= 1
            add(prefix_xor[win_l - 1])  # 转换为0索引
        
        # 调整右边界
        while win_r < job_r:
            win_r += 1
            add(prefix_xor[win_r])  # 转换为0索引
        
        # 收缩左边界
        while win_l < job_l:
            del_(prefix_xor[win_l - 1])  # 转换为0索引
            win_l += 1
        
        # 收缩右边界
        while win_r > job_r:
            del_(prefix_xor[win_r])  # 转换为0索引
            win_r -= 1
        
        answers[idx] = cur_ans
    
    # 输出答案
    for ans in answers:
        print(ans)

if __name__ == "__main__":
    main()