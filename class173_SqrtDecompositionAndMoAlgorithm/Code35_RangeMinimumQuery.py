# 区间最小值查询 - 分块算法实现 (Python版本)
# 题目来源: AizuOJ 2442
# 题目链接: https://onlinejudge.u-aizu.ac.jp/problems/2442
# 题目大意: 实现一个数据结构，支持单点更新和区间最小值查询
# 约束条件: 数组长度n ≤ 10^5，操作次数q ≤ 10^5

import sys
import math

INF = 10**18

def main():
    # 读取输入
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    q = int(input[ptr])
    ptr += 1
    
    # 初始化数组
    arr = list(map(int, input[ptr:ptr+n]))
    ptr += n
    
    # 分块相关变量
    blen = int(math.sqrt(n))
    if blen == 0:
        blen = 1
    block_count = (n + blen - 1) // blen
    block_min = [INF] * block_count
    
    # 初始化每个块的最小值
    for i in range(block_count):
        min_val = INF
        for j in range(blen):
            idx = i * blen + j
            if idx >= n:
                break
            if arr[idx] < min_val:
                min_val = arr[idx]
        block_min[i] = min_val
    
    # 处理查询和更新操作
    for _ in range(q):
        type_op = int(input[ptr])
        ptr += 1
        
        if type_op == 0:  # 更新操作
            pos = int(input[ptr])
            ptr += 1
            val = int(input[ptr])
            ptr += 1
            
            # 更新原数组
            arr[pos] = val
            
            # 更新对应块的最小值
            block_idx = pos // blen
            min_val = INF
            start = block_idx * blen
            end = min((block_idx + 1) * blen, n)
            
            for i in range(start, end):
                if arr[i] < min_val:
                    min_val = arr[i]
            block_min[block_idx] = min_val
            
        else:  # 查询操作
            l = int(input[ptr])
            ptr += 1
            r = int(input[ptr])
            ptr += 1
            
            min_val = INF
            left_block = l // blen
            right_block = r // blen
            
            if left_block == right_block:
                # 所有元素都在同一个块内
                for i in range(l, r + 1):
                    if arr[i] < min_val:
                        min_val = arr[i]
            else:
                # 处理左边不完整的块
                for i in range(l, (left_block + 1) * blen):
                    if arr[i] < min_val:
                        min_val = arr[i]
                
                # 处理中间完整的块
                for i in range(left_block + 1, right_block):
                    if block_min[i] < min_val:
                        min_val = block_min[i]
                
                # 处理右边不完整的块
                for i in range(right_block * blen, r + 1):
                    if arr[i] < min_val:
                        min_val = arr[i]
            
            print(min_val)

# 测试用例
# 示例：
# 输入：
# 5 5
# 1 5 3 4 2
# 1 0 4
# 0 2 10
# 1 0 4
# 1 1 3
# 0 3 1
# 1 0 4
# 输出：
# 1
# 1
# 4
# 1

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 初始化：O(n)
- 更新操作：O(√n)
- 查询操作：O(√n)
- 对于q次操作，总体时间复杂度：O(n + q√n)

空间复杂度分析：
- 数组arr：O(n)
- 块最小值数组block_min：O(√n)
- 总体空间复杂度：O(n + √n) = O(n)

Python语言特性注意事项：
1. 为了提高输入效率，一次性读取所有输入并使用指针逐个处理
2. 使用列表存储数组和块最小值
3. 注意Python中整数的精度没有限制，不需要担心溢出问题
4. 使用math.sqrt计算块的大小

算法说明：
分块算法是一种将数组分成多个块的方法，通过预处理每个块的信息，
可以在O(√n)的时间复杂度内处理区间查询和单点更新操作。

分块算法的核心思想是：
1. 将数组分成大小为√n的块
2. 预处理每个块的信息（这里是块内最小值）
3. 对于区间查询，分别处理不完整的块和完整的块：
   - 不完整的块直接暴力查询
   - 完整的块利用预处理信息快速查询
4. 对于单点更新，更新原数组后重新计算对应块的预处理信息

与其他数据结构的对比：
- 线段树：时间复杂度O(log n)，但实现较复杂
- 稀疏表：查询O(1)，但不支持动态更新
- 分块算法：实现简单，时间复杂度适中，支持动态更新

在Python中，分块算法的效率可能不如C++，但对于大多数问题来说已经足够快。
如果数据规模非常大，可以考虑使用更高效的输入方法或使用其他语言实现。
'''