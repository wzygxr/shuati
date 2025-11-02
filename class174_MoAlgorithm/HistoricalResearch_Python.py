# 歴史の研究 (AtCoder AT1219) - 回滚莫队
# 题目来源: AtCoder AT1219
# 题目链接: https://www.luogu.com.cn/problem/AT1219
# 题意: 给定一个长度为n的序列，每次询问给定一个区间，定义一种颜色的价值为它的大小乘上它在这个区间内的出现次数，
# 求所有颜色最大的价值。
#
# 算法思路:
# 1. 使用回滚莫队算法，适用于只能添加不能删除或者只能删除不能添加的区间问题
# 2. 对于左右端点在同一块内的查询，直接暴力计算
# 3. 对于跨块的查询，先扩展右边界到R，然后收缩左边界到L，最后恢复状态
#
# 时间复杂度分析:
# - 排序: O(q * log q)
# - 左指针移动: O(q * sqrt(n))
# - 右指针移动: O(n * sqrt(n))
# - 总体复杂度: O((n + q) * sqrt(n))
#
# 空间复杂度分析:
# - 存储原数组: O(n)
# - 存储计数字典: O(n)
# - 存储查询结果: O(q)
# - 总体空间复杂度: O(n)
# 适用场景: 区间众数相关问题、最大值维护问题

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
    cnt = defaultdict(int)  # 记录每个数字出现的次数
    max_val = 0  # 当前区间最大价值
    results = [0] * q  # 存储结果
    
    # 添加元素
    def add(pos):
        nonlocal max_val
        cnt[arr[pos]] += 1
        max_val = max(max_val, arr[pos] * cnt[arr[pos]])
    
    # 删除元素（不更新max_val，用于回滚）
    def remove_without_update(pos):
        cnt[arr[pos]] -= 1
    
    # 处理查询
    cur_l, cur_r = 1, 0
    
    for l, r, idx in queries:
        # 如果左右端点在同一块内，暴力计算
        if (l - 1) // block_size == (r - 1) // block_size:
            temp_max = 0
            temp_cnt = defaultdict(int)
            for i in range(l, r + 1):
                temp_cnt[arr[i]] += 1
                temp_max = max(temp_max, arr[i] * temp_cnt[arr[i]])
            results[idx] = temp_max
            continue
        
        # 扩展右边界到R
        while cur_r < r:
            cur_r += 1
            add(cur_r)
        
        # 保存当前状态
        saved_max = max_val
        saved_r = cur_r
        
        # 收缩左边界到L
        while cur_l < l:
            remove_without_update(cur_l)
            cur_l += 1
        
        results[idx] = max_val
        
        # 恢复状态
        while cur_l > ((l - 1) // block_size) * block_size + 1:
            cur_l -= 1
            add(cur_l)
        
        # 恢复右边界
        while cur_r > saved_r:
            remove_without_update(cur_r)
            cur_r -= 1
        max_val = saved_max
    
    # 输出结果
    for result in results:
        print(result)

if __name__ == "__main__":
    main()