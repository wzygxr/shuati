# 莫队二次离线（第十四分块(前体)） (二次离线莫队应用)
# 题目来源: 洛谷P4887 【模板】莫队二次离线（第十四分块(前体)）
# 题目链接: https://www.luogu.com.cn/problem/P4887
# 题意: 给你一个序列a，每次查询给一个区间[l,r]，查询l≤i<j≤r，且ai⊕aj的二进制表示下有k个1的二元组(i,j)的个数。⊕是指按位异或。
# 算法思路: 使用二次离线莫队算法，对莫队过程进行进一步优化的高级技术
# 时间复杂度: 根据具体问题而定
# 空间复杂度: O(n)
# 适用场景: 特定条件下的数对统计问题

import sys
import math
from collections import defaultdict

def main():
    # 读取输入
    n, q, k = map(int, sys.stdin.readline().split())
    arr = [0] + list(map(int, sys.stdin.readline().split()))
    
    queries = []
    for i in range(q):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 莫队二次离线实现
    block_size = int(math.sqrt(n))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, idx = query
        return (l // block_size, r)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    cnt = defaultdict(int)  # 记录每个值出现的次数
    current_answer = 0
    results = [0] * q
    
    # 计算二进制表示中1的个数
    def count_bits(x):
        count = 0
        while x > 0:
            count += x & 1
            x >>= 1
        return count
    
    # 预处理：计算每个数与哪些数异或后有k个1
    MAXV = 16384  # 2^14
    valid = [[False] * MAXV for _ in range(MAXV)]
    
    def preprocess():
        for i in range(MAXV):
            for j in range(MAXV):
                xor = i ^ j
                if count_bits(xor) == k:
                    valid[i][j] = True
    
    preprocess()
    
    # 添加元素
    def add(pos):
        nonlocal current_answer
        val = arr[pos]
        # 更新答案
        for i in range(MAXV):
            if valid[val][i]:
                current_answer += cnt[i]
        cnt[val] += 1
    
    # 删除元素
    def remove(pos):
        nonlocal current_answer
        val = arr[pos]
        cnt[val] -= 1
        # 更新答案
        for i in range(MAXV):
            if valid[val][i]:
                current_answer -= cnt[i]
    
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
        
        results[idx] = current_answer
    
    # 输出结果
    for result in results:
        print(result)

if __name__ == "__main__":
    main()