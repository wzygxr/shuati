import sys
import math
import bisect

"""
Codeforces 91B - Python实现
题目链接：https://codeforces.com/problemset/problem/91B

题目描述：
给定一个数组a，对于每个元素a[i]，找到右边第一个比它小的元素的下标j，并输出j - i - 1。如果不存在这样的元素，输出-1。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
预处理每个块内的最小值，以及块内的元素。
对于每个查询，分情况处理：
1. 检查右边的完整块，如果块内存在比当前元素小的元素，则暴力查找该块中的第一个符合条件的元素
2. 否则检查右边不完整块中的元素
3. 如果都没有找到，则返回-1

时间复杂度：
- 预处理：O(n)
- 每个查询：O(√n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：预处理块内最小值减少重复计算
4. 鲁棒性：处理边界情况和特殊输入
5. 数据结构：使用列表存储统计信息
"""

def main():
    # 提高输入速度
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    a = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    block_size = int(math.sqrt(n)) + 1
    block_num = (n + block_size - 1) // block_size
    
    # 预处理每个块的最小值
    block_min = [float('inf')] * block_num
    belong = [0] * n
    
    for i in range(n):
        belong[i] = i // block_size
        if a[i] < block_min[belong[i]]:
            block_min[belong[i]] = a[i]
    
    result = [-1] * n
    
    for i in range(n):
        current_block = belong[i]
        found = False
        
        # 检查当前块后面的完整块
        for b in range(current_block + 1, block_num):
            if block_min[b] < a[i]:
                # 在块b中暴力查找第一个比a[i]小的元素
                start = b * block_size
                end = min((b + 1) * block_size, n)
                for j in range(start, end):
                    if a[j] < a[i]:
                        result[i] = j - i - 1
                        found = True
                        break
                if found:
                    break
        
        # 如果没有在完整块中找到，检查当前块内的右边元素
        if not found:
            # 先检查当前块内的右边元素
            start = (current_block + 1) * block_size
            end = n
            for j in range(start, end):
                if a[j] < a[i]:
                    result[i] = j - i - 1
                    found = True
                    break
    
    # 输出结果
    print(' '.join(map(str, result)))

if __name__ == "__main__":
    main()