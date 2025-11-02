# Maximum Frequency - 回滚莫队算法实现 (Python版本)
# 题目来源: 模板题 - 区间众数查询（强制在线）
# 题目链接: https://www.luogu.com.cn/problem/P5906
# 题目大意: 给定一个数组，每次查询区间[l,r]中的众数（出现次数最多的数）的出现次数
# 时间复杂度: O(n*sqrt(n))，空间复杂度: O(n)

import sys
import math
from collections import defaultdict

def main():
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    arr = [0] * (n + 1)  # 1-based索引
    for i in range(1, n + 1):
        arr[i] = int(input[ptr])
        ptr += 1
    
    # 分块
    block_size = int(math.sqrt(n)) + 1
    bi = [0] * (n + 1)
    for i in range(1, n + 1):
        bi[i] = (i - 1) // block_size
    
    max_block = bi[n] + 1
    
    # 按块存储查询
    block_queries = [[] for _ in range(max_block)]
    queries = []
    
    # 读取查询
    for i in range(m):
        l = int(input[ptr])
        r = int(input[ptr + 1])
        queries.append( (l, r, i) )
        block_queries[bi[l]].append( (l, r, i) )
        ptr += 2
    
    ans = [0] * m
    
    # 回滚莫队处理
    for b in range(max_block):
        # 当前块的右端点
        block_r = min((b + 1) * block_size, n)
        
        # 按右端点排序同一块内的查询
        block_queries[b].sort(key=lambda x: x[1])
        
        # 初始化计数器
        cnt = defaultdict(int)
        max_freq = 0
        
        # 右指针从块的右端点开始
        r = block_r
        
        for q in block_queries[b]:
            ql, qr, qid = q
            
            # 如果查询的r也在当前块内，直接暴力查询
            if bi[qr] == b:
                # 暴力查询
                current_max = 0
                temp_cnt = defaultdict(int)
                for i in range(ql, qr + 1):
                    temp_cnt[arr[i]] += 1
                    if temp_cnt[arr[i]] > current_max:
                        current_max = temp_cnt[arr[i]]
                ans[qid] = current_max
                continue
            
            # 否则使用回滚莫队
            # 1. 将右指针移动到qr
            while r < qr:
                r += 1
                cnt[arr[r]] += 1
                if cnt[arr[r]] > max_freq:
                    max_freq = cnt[arr[r]]
            
            # 2. 记录当前状态
            temp_max_freq = max_freq
            temp_cnt = cnt.copy()
            
            # 3. 移动左指针到ql，统计答案
            # 这里只处理块内的左部分
            current_max = temp_max_freq
            for i in range(block_r, ql - 1, -1):
                cnt[arr[i]] += 1
                if cnt[arr[i]] > current_max:
                    current_max = cnt[arr[i]]
            
            # 4. 记录答案
            ans[qid] = current_max
            
            # 5. 回滚到之前的状态
            cnt = temp_cnt
            max_freq = temp_max_freq
    
    # 输出答案
    sys.stdout.write('\n'.join(map(str, ans)) + '\n')

if __name__ == "__main__":
    main()