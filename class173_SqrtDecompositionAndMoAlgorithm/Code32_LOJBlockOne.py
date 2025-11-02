# LOJ 分块一 - 分块算法实现 (Python版本)
# 题目来源: https://loj.ac/problem/6277
# 题目大意: 给定一个长度为n的数组，支持两种操作：
# 1. 区间[l, r]每个数加k
# 2. 查询位置p的值
# 约束条件: 1 <= n, m <= 1e5

import sys
import math

def main():
    sys.setrecursionlimit(1 << 25)
    n = int(sys.stdin.readline())
    blen = int(math.sqrt(n))
    if blen == 0:
        blen = 1  # 避免除零错误
    
    block_count = (n + blen - 1) // blen
    
    # 初始化数组和标记
    arr = [0] * (n + 2)  # 1-based索引
    tag = [0] * (block_count + 2)  # 块的懒标记，块编号从1开始
    
    # 读取数组元素
    s = sys.stdin.readline().split()
    for i in range(1, n + 1):
        arr[i] = int(s[i - 1])
    
    # 获取元素p的值（考虑懒标记）
    def get(p):
        block = (p - 1) // blen + 1
        return arr[p] + tag[block]
    
    # 区间更新：将[l, r]区间内的每个数加上k
    def update_range(l, r, k):
        L = (l - 1) // blen + 1
        R = (r - 1) // blen + 1
        
        # 如果l和r在同一个块内
        if L == R:
            for i in range(l, r + 1):
                arr[i] += k
            return
        
        # 暴力更新左边不完整的块
        for i in range(l, L * blen + 1):
            arr[i] += k
        
        # 对中间完整的块打标记
        for i in range(L + 1, R):
            tag[i] += k
        
        # 暴力更新右边不完整的块
        for i in range((R - 1) * blen + 1, r + 1):
            arr[i] += k
    
    # 单点查询：查询位置p的值
    def query_point(p):
        return get(p)
    
    # 处理m次操作
    m = int(sys.stdin.readline())
    for _ in range(m):
        s = sys.stdin.readline().split()
        op = int(s[0])
        if op == 1:
            # 区间更新操作
            l = int(s[1])
            r = int(s[2])
            k = int(s[3])
            update_range(l, r, k)
        else:
            # 单点查询操作
            p = int(s[1])
            print(query_point(p))

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 预处理：O(n)
- 区间更新操作：O(√n) 每个完整块O(1)，不完整块O(√n)
- 单点查询操作：O(1) 直接计算
- 总体时间复杂度：O(m√n)，其中m为操作次数

空间复杂度分析：
- 数组arr：O(n)
- 数组tag：O(√n)
- 总体空间复杂度：O(n)

Python语言特性注意事项：
1. 使用sys.stdin.readline()来提高输入效率
2. 注意Python中列表的索引处理，这里使用1-based索引
3. 对于大规模数据，Python版本可能会遇到性能问题，需要注意优化
4. 块的大小选择为sqrt(n)，这是分块算法的经典选择
5. 本问题是分块算法的入门级题目，非常适合理解分块的基本思想
'''