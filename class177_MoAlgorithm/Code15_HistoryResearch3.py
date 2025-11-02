# 历史研究 - 回滚莫队算法实现 (Python版本)
# 题目来源: AtCoder JOI 2014 Day1 历史研究
# 题目链接: https://www.luogu.com.cn/problem/AT_joisc2014_c
# 题目大意: 给定一个序列，多次询问一段区间，求区间中重要度最大的数字(重要度=数字值*出现次数)
# 时间复杂度: O(n*sqrt(m))
# 空间复杂度: O(n)
#
# 相关题目链接:
# 1. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py
#
# 2. 洛谷 P5906 【模板】回滚莫队&不删除莫队 - https://www.luogu.com.cn/problem/P5906
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_P5906_HistoryResearch1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_P5906_HistoryResearch2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_P5906_HistoryResearch3.py
#
# 3. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py
#
# 4. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
#
# 5. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
#
# 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
#
# 7. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
#
# 8. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
#
# 9. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
#
# 10. 洛谷 P3245 [HNOI2016]大数 - https://www.luogu.com.cn/problem/P3245
#     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery1.java
#     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery2.cpp
#     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery3.py

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, q = map(int, sys.stdin.readline().split())
    x = list(map(int, sys.stdin.readline().split()))
    
    # 存储查询
    queries = []
    for i in range(q):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 回滚莫队算法预处理
    block_size = int(math.sqrt(n))
    
    # 查询排序函数
    def query_sort_key(query):
        l, r, idx = query
        block_id = (l - 1) // block_size
        return (block_id, r)
    
    # 按照回滚莫队算法的顺序排序查询
    queries.sort(key=query_sort_key)
    
    # 离散化
    unique_values = sorted(set(x))
    value_to_index = {v: i+1 for i, v in enumerate(unique_values)}
    x = [value_to_index[v] for v in x]
    
    # 初始化变量
    cnt = defaultdict(int)  # 当前区间中每个数字的出现次数
    ans = [0] * q  # 答案数组
    
    # 当前维护的区间 [win_l, win_r]
    win_l, win_r = 0, -1
    last_block = -1
    current_max = 0
    
    # 处理每个查询
    for job_l, job_r, idx in queries:
        # 转换为0索引
        job_l -= 1
        job_r -= 1
        
        # 询问的左右端点同属于一个块则暴力扫描回答
        if job_l // block_size == job_r // block_size:
            temp_cnt = defaultdict(int)
            max_importance = 0
            for i in range(job_l, job_r + 1):
                temp_cnt[x[i]] += 1
                importance = temp_cnt[x[i]] * unique_values[x[i]-1]
                max_importance = max(max_importance, importance)
            ans[idx] = max_importance
            continue

        # 访问到了新的块则重新初始化莫队区间
        if job_l // block_size != last_block:
            # 清空当前计数
            cnt.clear()
            current_max = 0
            # 重新设置窗口位置
            win_l = (job_l // block_size + 1) * block_size
            win_r = win_l - 1
            last_block = job_l // block_size

        # 扩展右端点
        while win_r < job_r:
            win_r += 1
            cnt[x[win_r]] += 1
            current_max = max(current_max, cnt[x[win_r]] * unique_values[x[win_r]-1])

        # 临时扩展左端点并回滚
        temp_cnt = cnt.copy()
        temp_max = current_max
        temp_l = win_l
        
        while temp_l > job_l:
            temp_l -= 1
            temp_cnt[x[temp_l]] += 1
            temp_max = max(temp_max, temp_cnt[x[temp_l]] * unique_values[x[temp_l]-1])
        
        ans[idx] = temp_max
        
        # 回滚左端点的扩展
        while temp_l < win_l:
            temp_cnt[x[temp_l]] -= 1
            temp_l += 1

    # 输出答案
    for a in ans:
        print(a)

if __name__ == "__main__":
    main()