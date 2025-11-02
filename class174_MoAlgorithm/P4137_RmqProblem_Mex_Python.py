# Rmq Problem / mex (回滚莫队应用)
# 题目来源: 洛谷P4137 Rmq Problem / mex
# 题目链接: https://www.luogu.com.cn/problem/P4137
# 题意: 有一个长度为 n 的数组 {a1,a2,...,an}。m 次询问，每次询问一个区间内最小没有出现过的自然数。
# 算法思路: 使用回滚莫队算法，适用于只能添加不能删除或者只能删除不能添加的区间问题
# 时间复杂度: O((n + q) * sqrt(n))
# 空间复杂度: O(n)
# 适用场景: 区间Mex查询问题

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, q = map(int, sys.stdin.readline().split())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 为了方便处理，将数组下标从1开始
    arr = [0] + arr
    
    queries = []
    for i in range(q):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 回滚莫队算法实现
    block_size = int(math.sqrt(n))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, idx = query
        return (l // block_size, r)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    cnt = [0] * (n + 1)  # 记录每个数字出现的次数
    results = [0] * q  # 存储结果
    
    # 添加元素
    def add(pos):
        cnt[arr[pos]] += 1
    
    # 删除元素（不更新答案，用于回滚）
    def remove_without_update(pos):
        cnt[arr[pos]] -= 1
    
    # 计算Mex
    def calculate_mex():
        mex = 0
        while cnt[mex] > 0:
            mex += 1
        return mex
    
    # 处理查询
    cur_l, cur_r = 1, 0
    
    for l, r, idx in queries:
        # 如果左右端点在同一块内，暴力计算
        if (l - 1) // block_size == (r - 1) // block_size:
            temp_cnt = [0] * (n + 1)
            for i in range(l, r + 1):
                temp_cnt[arr[i]] += 1
            # 找到最小的未出现的自然数
            mex = 0
            while temp_cnt[mex] > 0:
                mex += 1
            results[idx] = mex
            continue
        
        # 扩展右边界到R
        while cur_r < r:
            cur_r += 1
            add(cur_r)
        
        # 保存当前状态
        saved_r = cur_r
        
        # 收缩左边界到L
        while cur_l < l:
            remove_without_update(cur_l)
            cur_l += 1
        
        # 计算Mex
        results[idx] = calculate_mex()
        
        # 恢复状态
        while cur_l > ((l - 1) // block_size) * block_size + 1:
            cur_l -= 1
            add(cur_l)
        
        # 恢复右边界
        while cur_r > saved_r:
            remove_without_update(cur_r)
            cur_r -= 1
    
    # 输出结果
    for result in results:
        print(result)

if __name__ == "__main__":
    main()