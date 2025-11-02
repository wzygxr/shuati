# 数颜色 - 带修莫队算法实现 (Python版本)
# 题目来源: 洛谷 P1903 [国家集训队] 数颜色 / 维护队列
# 题目链接: https://www.luogu.com.cn/problem/P1903
# 题目大意: 给定一个长度为n的序列，支持两种操作：
# 1. 修改某个位置的颜色
# 2. 查询区间[l,r]内有多少种不同的颜色
# 解题思路: 使用带修莫队算法，增加时间维度处理修改操作
# 时间复杂度: O(n^(5/3))
# 空间复杂度: O(n)
#
# 相关题目链接:
# 1. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
#
# 2. Codeforces 940F Machine Learning - https://codeforces.com/problemset/problem/940/F
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors3.py
#
# 3. UVA 12345 Dynamic len(set(a[L:R])) - https://vjudge.net/problem/UVA-12345
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors3.py
#
# 4. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
#
# 5. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
#
# 6. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
#
# 7. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
#
# 8. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
#
# 9. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py
#
# 10. AT1219 歴史の研究 - https://www.luogu.com.cn/problem/AT1219
#     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors1.java
#     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors2.cpp
#     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors3.py

import math
import sys

def main():
    # 读取输入
    line = sys.stdin.readline().split()
    n, m = int(line[0]), int(line[1])
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 存储查询和修改操作
    queries = []  # (l, r, time, id)
    modifies = []  # (pos, old_color, new_color)
    
    cntq = 0  # 查询数
    cntm = 0  # 修改数
    
    for i in range(m):
        line = sys.stdin.readline().split()
        if line[0] == 'Q':  # 查询操作
            cntq += 1
            l, r = int(line[1]), int(line[2])
            queries.append((l, r, cntm, cntq))  # 时间戳为当前修改数
        else:  # 修改操作
            cntm += 1
            pos, color = int(line[1]) - 1, int(line[2])  # 转为0索引
            old_color = arr[pos]
            modifies.append((pos, old_color, color))
            arr[pos] = color
    
    # 带修莫队算法预处理
    block_size = int(math.pow(n, 2.0 / 3.0))
    
    # 查询排序函数
    def query_sort_key(query):
        l, r, t, idx = query
        block_l = (l - 1) // block_size
        block_r = (r - 1) // block_size
        return (block_l, block_r, t)
    
    # 按照带修莫队算法的顺序排序查询
    queries.sort(key=query_sort_key)
    
    # 初始化变量
    cnt = [0] * (max(arr) + 1) if arr else [0]  # 记录每种颜色的出现次数
    cur_ans = 0  # 当前区间的答案
    answers = [0] * (cntq + 1)  # 存储答案
    
    # 添加元素到区间
    def add(color):
        nonlocal cur_ans
        if cnt[color] == 0:
            cur_ans += 1
        cnt[color] += 1
    
    # 从区间中删除元素
    def del_color(color):
        nonlocal cur_ans
        cnt[color] -= 1
        if cnt[color] == 0:
            cur_ans -= 1
    
    # 应用修改操作
    def apply_modify(time, l, r):
        pos, old_color, new_color = modifies[time - 1]  # 转为0索引
        # 如果修改位置在当前查询区间内，需要更新答案
        if l <= pos + 1 <= r:  # 转回1索引比较
            del_color(old_color)
            add(new_color)
        arr[pos] = new_color
    
    # 撤销修改操作
    def undo_modify(time, l, r):
        pos, old_color, new_color = modifies[time - 1]  # 转为0索引
        # 如果修改位置在当前查询区间内，需要更新答案
        if l <= pos + 1 <= r:  # 转回1索引比较
            del_color(new_color)
            add(old_color)
        arr[pos] = old_color
    
    # 当前维护的区间 [win_l, win_r]，对应1索引
    win_l, win_r = 1, 0
    now = 0  # 当前处理到第几个修改操作
    
    # 处理每个查询
    for job_l, job_r, job_t, idx in queries:
        # 处理时间维度
        while now < job_t:
            now += 1
            apply_modify(now, win_l, win_r)
        
        while now > job_t:
            undo_modify(now, win_l, win_r)
            now -= 1
        
        # 扩展左边界
        while win_l > job_l:
            win_l -= 1
            add(arr[win_l - 1])  # 转为0索引
        
        # 扩展右边界
        while win_r < job_r:
            win_r += 1
            add(arr[win_r - 1])  # 转为0索引
        
        # 收缩左边界
        while win_l < job_l:
            del_color(arr[win_l - 1])  # 转为0索引
            win_l += 1
        
        # 收缩右边界
        while win_r > job_r:
            del_color(arr[win_r - 1])  # 转为0索引
            win_r -= 1
        
        answers[idx] = cur_ans
    
    # 输出答案
    for i in range(1, cntq + 1):
        print(answers[i])

if __name__ == "__main__":
    main()