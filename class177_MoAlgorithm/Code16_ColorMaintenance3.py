# 数颜色/维护队列 - 带修莫队算法实现 (Python版本)
# 题目来源: 洛谷 P1903 [国家集训队] 数颜色 / 维护队列
# 题目链接: https://www.luogu.com.cn/problem/P1903
# 题目大意: 维护一个序列，支持两种操作：1. 查询区间不同颜色数 2. 单点修改颜色
# 时间复杂度: O(n^(5/3))
# 空间复杂度: O(n)
#
# 相关题目链接:
# 1. 洛谷 P1903 [国家集训队] 数颜色 / 维护队列 - https://www.luogu.com.cn/problem/P1903
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
#
# 2. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
#
# 3. Codeforces 940F Machine Learning - https://codeforces.com/problemset/problem/940/F
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors3.py
#
# 4. UVA 12345 Dynamic len(set(a[L:R])) - https://vjudge.net/problem/UVA-12345
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors3.py
#
# 5. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
#
# 6. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
#
# 7. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
#
# 8. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
#
# 9. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
#
# 10. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
#     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
#     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
#     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
    a = list(map(int, sys.stdin.readline().split()))
    
    # 存储查询和修改操作
    queries = []  # (l, r, id)
    operations = []  # (p, x)
    
    qcnt = 0
    rcnt = 0
    
    for i in range(m):
        line = sys.stdin.readline().split()
        op = line[0]
        x = int(line[1])
        y = int(line[2])
        
        if op == 'Q':
            qcnt += 1
            queries.append((x, y, qcnt-1, rcnt))  # l, r, id, time
        elif op == 'R':
            rcnt += 1
            operations.append((x, y))  # p, x
    
    # 带修莫队算法预处理
    block_size = int(math.pow(n, 2.0 / 3.0))
    
    # 查询排序函数
    def query_sort_key(query):
        l, r, idx, t = query
        block_l = (l - 1) // block_size
        block_r = (r - 1) // block_size
        return (block_l, block_r, t)
    
    # 按照带修莫队算法的顺序排序查询
    queries.sort(key=query_sort_key)
    
    # 初始化变量
    cnt = defaultdict(int)  # 记录每种颜色的出现次数
    cur = 0  # 当前区间不同颜色数
    ans = [0] * qcnt  # 答案数组
    
    # 当前维护的区间 [win_l, win_r] 和时间戳
    win_l, win_r = 1, 0
    current_time = 0
    
    # 添加元素
    def add(value):
        nonlocal cur
        if cnt[value] == 0:
            cur += 1
        cnt[value] += 1
    
    # 删除元素
    def del_(value):
        nonlocal cur
        cnt[value] -= 1
        if cnt[value] == 0:
            cur -= 1
    
    # 处理每个查询
    for job_l, job_r, idx, job_time in queries:
        # 转换为0索引
        job_l -= 1
        # 调整右边界
        while win_r < job_r:
            win_r += 1
            add(a[win_r - 1])
        
        # 调整左边界
        while win_l > job_l:
            win_l -= 1
            add(a[win_l - 1])
        
        # 收缩右边界
        while win_r > job_r:
            del_(a[win_r - 1])
            win_r -= 1
        
        # 收缩左边界
        while win_l < job_l:
            del_(a[win_l - 1])
            win_l += 1
        
        # 处理时间维度
        while current_time < job_time:
            p, x = operations[current_time]
            p -= 1  # 转换为0索引
            if win_l <= p + 1 <= win_r:  # p+1是因为题目中是1索引
                del_(a[p])
                add(x)
            # 执行修改
            a[p], x = x, a[p]
            operations[current_time] = (p + 1, x)  # 转换回1索引存储
            current_time += 1
        
        while current_time > job_time:
            current_time -= 1
            p, x = operations[current_time]
            p -= 1  # 转换为0索引
            if win_l <= p + 1 <= win_r:  # p+1是因为题目中是1索引
                del_(a[p])
                add(x)
            # 执行修改
            a[p], x = x, a[p]
            operations[current_time] = (p + 1, x)  # 转换回1索引存储
        
        ans[idx] = cur
    
    # 输出答案
    for a in ans:
        print(a)

if __name__ == "__main__":
    main()