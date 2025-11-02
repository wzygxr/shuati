# CGCDSSQ - Codeforces 475D
# 题目大意：
# 给定一个长度为n的整数序列a[1], a[2], ..., a[n]
# 有q个查询x[1], x[2], ..., x[q]
# 对于每个查询x[i]，需要计算有多少个区间[l, r]满足gcd(a[l], a[l+1], ..., a[r]) = x[i]
# 其中gcd表示最大公约数

# 解题思路：
# 1. 使用Sparse Table预处理区间GCD查询
# 2. 利用GCD的性质：随着区间长度增加，GCD值单调不增
# 3. 对于每个左端点，不同的GCD值最多有log(max_value)种
# 4. 预处理所有可能的GCD值及其出现次数
# 5. 对于每个查询，直接输出对应的计数

# 时间复杂度分析：
# 预处理Sparse Table: O(n * logn)
# 预处理所有GCD值: O(n * log(max_value))
# 查询: O(1)
# 总时间复杂度: O(n * log(max_value) + q)

import math
from collections import defaultdict
import sys

def main():
    # 计算两个数的最大公约数
    def gcd(a, b):
        return a if b == 0 else gcd(b, a % b)
    
    # 读取输入
    n = int(input())
    a = [0] + list(map(int, input().split()))  # 使下标从1开始
    
    # 预处理log2数组
    log2 = [0] * (n + 1)
    for i in range(2, n + 1):
        log2[i] = log2[i >> 1] + 1
    
    # Sparse Table数组，用于区间GCD查询
    st = [[0] * 20 for _ in range(n + 1)]
    
    # 构建Sparse Table用于区间GCD查询
    def build_sparse_table():
        # 初始化Sparse Table的第一层
        for i in range(1, n + 1):
            st[i][0] = a[i]
        
        # 动态规划构建Sparse Table
        j = 1
        while (1 << j) <= n:
            i = 1
            while i + (1 << j) - 1 <= n:
                st[i][j] = gcd(st[i][j - 1], st[i + (1 << (j - 1))][j - 1])
                i += 1
            j += 1
    
    # 查询区间[l,r]的GCD值
    def query_gcd(l, r):
        k = log2[r - l + 1]
        return gcd(st[l][k], st[r - (1 << k) + 1][k])
    
    # 构建Sparse Table
    build_sparse_table()
    
    # 记录每个GCD值出现的次数
    gcd_count = defaultdict(int)
    
    # 预处理所有可能的GCD值及其出现次数
    def preprocess_gcd():
        # 对于每个左端点
        for i in range(1, n + 1):
            # 从左端点开始，向右扩展区间
            j = i
            while j <= n:
                # 当前区间的GCD值
                current_gcd = query_gcd(i, j)
                
                # 找到GCD值保持不变的最长区间
                left, right = j, n
                pos = j
                
                while left <= right:
                    mid = (left + right) // 2
                    if query_gcd(i, mid) == current_gcd:
                        pos = mid
                        left = mid + 1
                    else:
                        right = mid - 1
                
                # 更新GCD值的计数
                gcd_count[current_gcd] += (pos - j + 1)
                
                # 移动到下一个可能的GCD值
                j = pos + 1
    
    # 预处理所有GCD值
    preprocess_gcd()
    
    # 处理查询
    q = int(input())
    queries = list(map(int, input().split()))
    for x in queries:
        print(gcd_count[x])

if __name__ == "__main__":
    main()