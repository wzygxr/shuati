# 小Z的袜子 - 普通莫队算法实现 (Python版本)
# 题目来源: 洛谷 P1494 [国家集训队]小Z的袜子
# 题目链接: https://www.luogu.com.cn/problem/P1494
# 题目大意: 给定一个长度为n的序列，每个元素代表袜子的颜色
# 有m次询问，每次询问区间[l,r]中随机抽取两只袜子颜色相同的概率
# 输出要求: 每个询问输出一个分数A/B表示概率，要求为最简分数
# 解题思路: 使用普通莫队算法，通过维护区间内每种颜色的出现次数来计算概率
# 时间复杂度: O(n*sqrt(m))
# 空间复杂度: O(n)
# 相关题目:
# 1. CF617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
# 2. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
# 3. BZOJ2038 [国家集训队]小Z的袜子 - https://www.lydsy.com/JudgeOnline/problem.php?id=2038
# 4. HDU4638 Group - http://acm.hdu.edu.cn/showproblem.php?pid=4638
# 5. AtCoder ABC176 D - Wizard in Maze - https://atcoder.jp/contests/abc176/tasks/abc176_d

import math
import sys
from fractions import Fraction
from typing import List, Optional

def main():
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 存储查询
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
    cnt = [0] * (max(arr) + 1)  # 记录每种颜色的出现次数
    cur_ans = 0  # 当前区间的答案（分子）
    answers: List[Optional[Fraction]] = [None] * m  # 存储答案
    
    # 当前维护的区间 [win_l, win_r]
    win_l, win_r = 1, 0
    
    # 处理每个查询
    for job_l, job_r, idx in queries:
        # 调整左边界
        while win_l > job_l:
            win_l -= 1
            color = arr[win_l - 1]  # arr索引从0开始
            # 当添加一个颜色时，该颜色对答案的贡献增加2*cnt[color]
            # 因为对于每一对已存在的该颜色袜子，新加入的袜子都能和它们组成一对
            cur_ans += cnt[color] * 2
            cnt[color] += 1
        
        while win_l < job_l:
            color = arr[win_l - 1]  # arr索引从0开始
            cnt[color] -= 1
            # 当删除一个颜色时，该颜色对答案的贡献减少2*cnt[color]
            # 因为对于每一对剩余的该颜色袜子，被删除的袜子不能再和它们组成一对
            cur_ans -= cnt[color] * 2
            win_l += 1
        
        # 调整右边界
        while win_r < job_r:
            win_r += 1
            color = arr[win_r - 1]  # arr索引从0开始
            # 当添加一个颜色时，该颜色对答案的贡献增加2*cnt[color]
            cur_ans += cnt[color] * 2
            cnt[color] += 1
        
        while win_r > job_r:
            color = arr[win_r - 1]  # arr索引从0开始
            cnt[color] -= 1
            # 当删除一个颜色时，该颜色对答案的贡献减少2*cnt[color]
            cur_ans -= cnt[color] * 2
            win_r -= 1
        
        # 特殊情况：区间长度为1时概率为0
        if job_l == job_r:
            answers[idx] = Fraction(0, 1)
            continue
        
        # 计算答案
        length = job_r - job_l + 1
        denominator = length * (length - 1)
        answers[idx] = Fraction(cur_ans, denominator)
    
    # 输出答案
    for ans in answers:
        if ans is not None:
            print(f"{ans.numerator}/{ans.denominator}")

if __name__ == "__main__":
    main()