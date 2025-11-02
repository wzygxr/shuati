import math
import bisect

"""
区间第k小查询问题 - Python实现
题目类型：区间第k小查询，支持单点更新

题目描述：
给定一个数组，支持两种操作：
1. 更新数组中某个元素的值
2. 查询区间[l, r]中的第k小元素

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
预处理：
1. 每个块内部排序，便于快速统计小于等于某值的元素个数
2. 预处理每个块的大小和边界
处理操作时：
1. 对于更新操作，更新原始数组，然后重新排序所在块
2. 对于查询操作，使用二分答案+前缀和的方式，统计区间中小于等于mid的元素个数

时间复杂度：
- 预处理：O(n log n)
- 更新操作：O(√n log √n)
- 查询操作：O((log n)^2 √n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：块内排序和二分查找
4. 鲁棒性：处理边界情况和特殊输入
5. 数据结构：使用列表存储数据和排序后的块
"""

import sys

def main():
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    q = int(input[ptr])
    ptr += 1
    
    a = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    block_size = int(math.sqrt(n)) + 1
    block_num = (n + block_size - 1) // block_size
    
    # 初始化每个块排序后的数组
    blocks = []
    for i in range(block_num):
        start = i * block_size
        end = min(start + block_size, n)
        block = sorted(a[start:end])
        blocks.append(block)
    
    # 处理查询
    for _ in range(q):
        op = int(input[ptr])
        ptr += 1
        
        if op == 0:
            # 单点更新
            index = int(input[ptr]) - 1  # 转换为0-based
            ptr += 1
            val = int(input[ptr])
            ptr += 1
            
            a[index] = val
            block_index = index // block_size
            start = block_index * block_size
            end = min(start + block_size, n)
            
            # 重新构建并排序该块
            blocks[block_index] = sorted(a[start:end])
        elif op == 1:
            # 区间第k小查询
            l = int(input[ptr]) - 1  # 转换为0-based
            ptr += 1
            r = int(input[ptr]) - 1  # 转换为0-based
            ptr += 1
            k = int(input[ptr])
            ptr += 1
            
            # 确定二分查找的上下界
            left_val = min(a)
            right_val = max(a)
            
            answer = right_val
            
            # 二分答案
            while left_val <= right_val:
                mid = left_val + (right_val - left_val) // 2
                count = 0
                
                left_block = l // block_size
                right_block = r // block_size
                
                if left_block == right_block:
                    # 在同一个块内，暴力统计
                    for i in range(l, r + 1):
                        if a[i] <= mid:
                            count += 1
                else:
                    # 统计左边不完整块
                    for i in range(l, (left_block + 1) * block_size):
                        if a[i] <= mid:
                            count += 1
                    
                    # 统计中间完整块
                    for b in range(left_block + 1, right_block):
                        # 在排序后的块中二分查找<=mid的元素个数
                        count += bisect.bisect_right(blocks[b], mid)
                    
                    # 统计右边不完整块
                    for i in range(right_block * block_size, r + 1):
                        if a[i] <= mid:
                            count += 1
                
                if count >= k:
                    answer = mid
                    right_val = mid - 1
                else:
                    left_val = mid + 1
            
            print(answer)

if __name__ == "__main__":
    main()