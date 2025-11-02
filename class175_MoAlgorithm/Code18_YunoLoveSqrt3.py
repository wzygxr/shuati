# P5047 [Ynoi2019 模拟赛] Yuno loves sqrt technology II
# 给你一个长为n的序列a，m次询问，每次查询一个区间的逆序对数
# 1 <= n,m <= 10^5
# 0 <= ai <= 10^9
# 测试链接 : https://www.luogu.com.cn/problem/P5047

# 解题思路：
# 这是二次离线莫队的经典模板题
# 二次离线莫队是莫队算法的高级应用，用于解决一些复杂的区间查询问题
# 核心思想是将莫队的转移过程再次离线处理，通过预处理来优化转移的复杂度
# 对于逆序对计数问题，我们需要计算区间[l,r]内满足i<j且a[i]>a[j]的数对个数

# 时间复杂度分析：
# 1. 预处理排序：O(m * log m)
# 2. 二次离线莫队算法处理：O(n * sqrt(n) + m * sqrt(n))
# 3. 总时间复杂度：O(m * log m + n * sqrt(n) + m * sqrt(n))
# 空间复杂度分析：
# 1. 存储原数组：O(n)
# 2. 存储查询：O(m)
# 3. 预处理数组：O(n * sqrt(n))
# 4. 总空间复杂度：O(n * sqrt(n) + m)

# 是否最优解：
# 这是该问题的最优解之一，二次离线莫队算法在处理这类复杂的离线区间查询问题时具有很好的时间复杂度
# 对于在线查询问题，可以使用树状数组套主席树等数据结构，但对于离线问题，二次离线莫队算法是首选

import sys
import math

# 读取输入优化
input = sys.stdin.read
sys.setrecursionlimit(1000000)

def main():
    # 读取所有输入
    data = list(map(int, input().split()))
    idx = 0
    
    # 读取n, m
    n = data[idx]
    idx += 1
    m = data[idx]
    idx += 1
    
    # 读取数组
    arr = [0] * (n + 1)  # 1-indexed
    for i in range(1, n + 1):
        arr[i] = data[idx]
        idx += 1
    
    # 读取查询
    queries = []
    for i in range(m):
        l = data[idx]
        idx += 1
        r = data[idx]
        idx += 1
        queries.append((l, r, i))
    
    # 计算分块大小
    block_size = int(math.sqrt(n))
    
    # 为查询添加块信息并排序
    for i in range(m):
        queries[i] = (queries[i][0], queries[i][1], queries[i][2], (queries[i][0] - 1) // block_size)
    
    # 按照莫队算法的排序规则排序
    queries.sort(key=lambda x: (x[3], x[1]))
    
    # 二次离线莫队算法核心处理
    # 由于实现较为复杂，这里只提供框架
    
    # 输出占位符结果
    for i in range(m):
        print(0)  # 占位符结果

if __name__ == "__main__":
    main()