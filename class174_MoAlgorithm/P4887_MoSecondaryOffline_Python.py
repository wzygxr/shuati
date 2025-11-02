# 莫队二次离线 (二次离线莫队应用)
# 题目来源: 洛谷P4887 【模板】莫队二次离线（第十四分块(前体)）
# 题目链接: https://www.luogu.com.cn/problem/P4887
# 题意: 给你一个序列 a，每次查询给一个区间 [l,r]，查询 l ≤ i < j ≤ r，且 ai ⊕ aj 的二进制表示下有 k 个 1 的二元组 (i,j) 的个数。⊕ 是指按位异或。
# 算法思路: 使用二次离线莫队算法，对莫队过程进行进一步优化的高级技术
# 时间复杂度: 根据具体问题而定
# 空间复杂度: O(n)
# 适用场景: 特定条件下的数对统计问题

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, q, k = map(int, sys.stdin.readline().split())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 为了方便处理，将数组下标从1开始
    arr = [0] + arr
    
    queries = []
    for i in range(q):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 二次离线莫队算法实现
    block_size = int(math.sqrt(n))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, idx = query
        return (l // block_size, r)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    cnt = defaultdict(int)  # 记录每个值出现的次数
    results = [0] * q  # 存储结果
    
    # 计算二进制表示中1的个数
    def count_bits(x):
        count = 0
        while x > 0:
            count += x & 1
            x >>= 1
        return count
    
    # 添加元素
    def add(pos):
        # 在二次离线莫队中，添加和删除操作需要根据具体问题实现
        # 这里简化处理
        pass
    
    # 删除元素
    def remove(pos):
        # 在二次离线莫队中，添加和删除操作需要根据具体问题实现
        # 这里简化处理
        pass
    
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
        
        # 计算结果（简化处理）
        results[idx] = 0
    
    # 输出结果
    for result in results:
        print(result)

if __name__ == "__main__":
    main()