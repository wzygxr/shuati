# 区间第k大 - 分块算法实现 (Python版本)
# 题目来源: POJ 2104
# 题目链接: http://poj.org/problem?id=2104
# 题目大意: 多次查询区间[l,r]内第k小的数字
# 约束条件: 数组长度n ≤ 1e5，查询次数m ≤ 1e4

import sys
import math

def main():
    # 读取输入
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 初始化数组
    arr = list(map(int, input[ptr:ptr+n]))
    ptr += n
    
    # 分块相关变量
    blen = int(math.sqrt(n))
    if blen == 0:
        blen = 1
    
    # 复制数组并排序，用于二分查找
    sorted_arr = sorted(arr)
    
    # 分块并对每个块进行排序
    block_count = (n + blen - 1) // blen
    blocks = []
    
    for i in range(block_count):
        start = i * blen
        end = min((i + 1) * blen, n)
        block = sorted(arr[start:end])
        blocks.append(block)
    
    # 查询区间[l,r]内小于等于x的元素个数
    def query_count(l, r, x):
        count = 0
        left_block = l // blen
        right_block = r // blen
        
        if left_block == right_block:
            # 所有元素都在同一个块内
            for i in range(l, r + 1):
                if arr[i] <= x:
                    count += 1
        else:
            # 处理左边不完整的块
            for i in range(l, (left_block + 1) * blen):
                if arr[i] <= x:
                    count += 1
            
            # 处理中间完整的块
            for i in range(left_block + 1, right_block):
                # 利用块的有序性进行二分查找
                block = blocks[i]
                # 使用bisect_right找到第一个大于x的元素的位置
                import bisect
                pos = bisect.bisect_right(block, x)
                count += pos
            
            # 处理右边不完整的块
            for i in range(right_block * blen, r + 1):
                if arr[i] <= x:
                    count += 1
        
        return count
    
    # 二分查找第k小的元素
    def find_kth_smallest(l, r, k):
        left = 0
        right = n - 1
        while left < right:
            mid = (left + right) // 2
            x = sorted_arr[mid]
            cnt = query_count(l, r, x)
            
            if cnt >= k:
                right = mid
            else:
                left = mid + 1
        return sorted_arr[left]
    
    # 处理查询
    for _ in range(m):
        l = int(input[ptr]) - 1  # 转换为0-based索引
        ptr += 1
        r = int(input[ptr]) - 1  # 转换为0-based索引
        ptr += 1
        k = int(input[ptr])
        ptr += 1
        
        result = find_kth_smallest(l, r, k)
        print(result)

# 测试用例
# 示例：
# 输入：
# 7 3
# 1 5 2 6 3 7 4
# 1 5 3
# 2 7 1
# 3 5 3
# 输出：
# 3
# 2
# 6

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 初始化：O(n log n)
- 单次查询：
  - query_count函数：O(√n + √n log √n) = O(√n log n)
  - find_kth_smallest函数：O(log n)次query_count调用
  - 总体单次查询时间复杂度：O(√n (log n)^2)
- 对于m次查询，总体时间复杂度：O(n log n + m√n (log n)^2)

空间复杂度分析：
- 数组arr：O(n)
- 数组sorted_arr：O(n)
- 块排序数组blocks：O(n)
- 总体空间复杂度：O(n)

Python语言特性注意事项：
1. 使用列表存储数组和排序后的块
2. 使用bisect模块进行二分查找，bisect_right函数返回第一个大于x的元素的位置
3. 为了提高输入效率，一次性读取所有输入并使用指针逐个处理
4. 注意Python中整数的精度没有限制，不需要担心溢出问题

算法说明：
这是一个经典的区间第k小查询问题，分块算法结合了二分查找和分块处理的思想：

1. 预处理阶段：
   - 将数组分成大小为√n的块，并对每个块进行排序
   - 对整个数组进行排序，用于二分查找候选值

2. 查询阶段：
   - 使用二分查找确定可能的答案值
   - 对于每个候选值x，统计区间内小于等于x的元素个数cnt
   - 如果cnt >= k，说明第k小的元素不超过x，缩小右边界
   - 否则，说明第k小的元素大于x，缩小左边界

3. 统计阶段（query_count函数）：
   - 对于完整的块，利用块的有序性进行二分查找快速统计
   - 对于不完整的块，直接暴力统计

优化说明：
- 块的大小选择为√n，平衡了查询和预处理的时间复杂度
- 使用bisect模块的bisect_right函数高效地统计块内小于等于x的元素个数
- 一次性读取所有输入以提高Python程序的输入效率

与其他方法的对比：
- 分块算法实现简单，但时间复杂度较高
- 主席树（可持久化线段树）时间复杂度更低，但实现复杂
- 对于Python来说，分块算法在大多数情况下已经足够快
'''