# D-query - 普通莫队算法实现 (Python版本)
# 题目来源: SPOJ DQUERY - D-query
# 题目链接: https://www.spoj.com/problems/DQUERY/
# 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内有多少种不同的数字
# 时间复杂度: O(n*sqrt(n))
# 空间复杂度: O(n)
#
# 相关题目链接:
# 1. 洛谷 P1972 [SDOI2009] HH的项链 - https://www.luogu.com.cn/problem/P1972
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery3.py
#
# 2. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
#
# 3. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
#
# 4. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
#
# 5. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
#
# 6. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
#
# 7. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py
#
# 8. AT1219 歴史の研究 - https://www.luogu.com.cn/problem/AT1219
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors3.py
#
# 9. 洛谷 P3245 [HNOI2016]大数 - https://www.luogu.com.cn/problem/P3245
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery3.py
#
# 10. Codeforces 1000F One Occurrence - https://codeforces.com/problemset/problem/1000/F
#     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_CF1000F_DQuery1.java
#     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_CF1000F_DQuery2.cpp
#     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_CF1000F_DQuery3.py

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 存储查询
    m = int(sys.stdin.readline())
    queries = []
    for i in range(m):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
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
    cnt = defaultdict(int)  # 记录每种数值的出现次数
    cur_ans = 0  # 当前区间的答案（不同数字的个数）
    answers = [0] * m  # 存储答案
    
    # 当前维护的区间 [win_l, win_r]
    win_l, win_r = 1, 0
    
    # 处理每个查询
    for job_l, job_r, idx in queries:
        # 调整左边界
        while win_l > job_l:
            win_l -= 1
            value = arr[win_l - 1]  # arr索引从0开始
            # 如果这是该数值第一次出现，则不同数字的个数增加1
            if cnt[value] == 0:
                cur_ans += 1
            cnt[value] += 1
        
        # 调整右边界
        while win_r < job_r:
            win_r += 1
            value = arr[win_r - 1]  # arr索引从0开始
            # 如果这是该数值第一次出现，则不同数字的个数增加1
            if cnt[value] == 0:
                cur_ans += 1
            cnt[value] += 1
        
        # 收缩左边界
        while win_l < job_l:
            value = arr[win_l - 1]  # arr索引从0开始
            cnt[value] -= 1
            # 如果该数值不再出现，则不同数字的个数减少1
            if cnt[value] == 0:
                cur_ans -= 1
            win_l += 1
        
        # 收缩右边界
        while win_r > job_r:
            value = arr[win_r - 1]  # arr索引从0开始
            cnt[value] -= 1
            # 如果该数值不再出现，则不同数字的个数减少1
            if cnt[value] == 0:
                cur_ans -= 1
            win_r -= 1
        
        answers[idx] = cur_ans
    
    # 输出答案
    for ans in answers:
        print(ans)

if __name__ == "__main__":
    main()