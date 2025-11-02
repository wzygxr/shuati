# Powerful Array - 普通莫队算法实现 (Python版本)
# 题目来源: Codeforces 86D - Powerful array
# 题目链接: https://codeforces.com/problemset/problem/86/D
# 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内所有数字的贡献和
# 每个数字x的贡献为 (出现次数)^2 * x
# 解题思路: 使用普通莫队算法，通过维护区间内每个数字的出现次数来计算贡献和
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

def main():
    # 读取输入
    line = sys.stdin.readline().split()
    n, m = int(line[0]), int(line[1])
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
    max_val = max(arr) if arr else 0
    cnt = [0] * (max_val + 1)  # 记录每种数值的出现次数
    cur_ans = 0  # 当前区间的答案
    answers = [0] * m  # 存储答案
    
    # 当前维护的区间 [win_l, win_r]
    win_l, win_r = 1, 0
    
    # 处理每个查询
    for job_l, job_r, idx in queries:
        # 调整左边界
        while win_l > job_l:
            win_l -= 1
            value = arr[win_l - 1]  # arr索引从0开始
            # 当添加一个数值时，先从当前答案中减去旧的贡献
            cur_ans -= cnt[value] * cnt[value] * value
            # 增加该数值的计数
            cnt[value] += 1
            # 再加上新的贡献
            cur_ans += cnt[value] * cnt[value] * value
        
        while win_l < job_l:
            value = arr[win_l - 1]  # arr索引从0开始
            # 当删除一个数值时，先从当前答案中减去旧的贡献
            cur_ans -= cnt[value] * cnt[value] * value
            # 减少该数值的计数
            cnt[value] -= 1
            # 再加上新的贡献
            cur_ans += cnt[value] * cnt[value] * value
            win_l += 1
        
        # 调整右边界
        while win_r < job_r:
            win_r += 1
            value = arr[win_r - 1]  # arr索引从0开始
            # 当添加一个数值时，先从当前答案中减去旧的贡献
            cur_ans -= cnt[value] * cnt[value] * value
            # 增加该数值的计数
            cnt[value] += 1
            # 再加上新的贡献
            cur_ans += cnt[value] * cnt[value] * value
        
        while win_r > job_r:
            value = arr[win_r - 1]  # arr索引从0开始
            # 当删除一个数值时，先从当前答案中减去旧的贡献
            cur_ans -= cnt[value] * cnt[value] * value
            # 减少该数值的计数
            cnt[value] -= 1
            # 再加上新的贡献
            cur_ans += cnt[value] * cnt[value] * value
            win_r -= 1
        
        answers[idx] = cur_ans
    
    # 输出答案
    for ans in answers:
        print(ans)

if __name__ == "__main__":
    main()