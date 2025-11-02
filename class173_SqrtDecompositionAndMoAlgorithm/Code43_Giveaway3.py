# GIVEAWAY - 区间查询与更新问题 - 分块算法实现 (Python版本)
# 题目来源: SPOJ
# 题目链接: https://www.spoj.com/problems/GIVEAWAY/
# 题目大意: 维护一个数组，支持两种操作：
# 1. C x y: 将位置x的值更新为y
# 2. Q l r k: 查询区间[l,r]内大于等于k的元素个数
# 约束条件: 数组长度n ≤ 5*10^5，操作次数q ≤ 10^5

import sys
import math
import bisect

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    
    arr = [0] + list(map(int, sys.stdin.readline().split()))  # 1-indexed
    
    # 初始化分块结构
    blen = int(math.sqrt(n))
    if blen == 0:
        blen = 1
    block_count = (n + blen - 1) // blen
    
    # 为每个元素分配块
    block = [0] * (n + 1)
    for i in range(1, n + 1):
        block[i] = (i - 1) // blen
    
    # 每个块中排序后的元素
    block_elements = [[] for _ in range(block_count)]
    
    # 将元素分配到对应的块中并排序
    for i in range(1, n + 1):
        block_elements[block[i]].append(arr[i])
    
    # 对每个块中的元素进行排序
    for i in range(block_count):
        block_elements[i].sort()
    
    # 更新操作：将位置pos的值更新为val
    def update(pos, val):
        block_id = block[pos]
        old_val = arr[pos]
        arr[pos] = val
        
        # 从块中移除旧值
        block_elements[block_id].remove(old_val)
        # 向块中添加新值
        block_elements[block_id].append(val)
        # 重新排序
        block_elements[block_id].sort()
    
    # 查询操作：查询区间[l,r]内大于等于k的元素个数
    def query(l, r, k):
        left_block = block[l]
        right_block = block[r]
        result = 0
        
        if left_block == right_block:
            # 所有元素都在同一个块内，直接暴力查询
            for i in range(l, r + 1):
                if arr[i] >= k:
                    result += 1
        else:
            # 处理左边不完整的块
            for i in range(l, min((left_block + 1) * blen + 1, n + 1)):
                if arr[i] >= k:
                    result += 1
            
            # 处理中间完整的块，使用二分查找
            for i in range(left_block + 1, right_block):
                # 使用二分查找找到第一个大于等于k的位置
                idx = bisect.bisect_left(block_elements[i], k)
                # 计算大于等于k的元素个数
                result += len(block_elements[i]) - idx
            
            # 处理右边不完整的块
            for i in range(right_block * blen + 1, r + 1):
                if arr[i] >= k:
                    result += 1
        
        return result
    
    q = int(sys.stdin.readline())
    
    for _ in range(q):
        op, *args = sys.stdin.readline().split()
        
        if op == "C":
            # 更新操作
            x, y = map(int, args)
            update(x, y)
        elif op == "Q":
            # 查询操作
            l, r, k = map(int, args)
            result = query(l, r, k)
            print(result)

if __name__ == "__main__":
    main()

"""
时间复杂度分析：
- 初始化：O(n * sqrt(n))，因为需要对每个块进行排序
- 更新操作：
  - 对于不完整的块：O(1)（直接修改原始数组）
  - 对于完整块的排序：O(sqrt(n))（需要重新排序）
  - 总体时间复杂度：O(sqrt(n))
- 查询操作：
  - 对于不完整的块：O(sqrt(n))（直接遍历）
  - 对于完整块：O(log(sqrt(n)))（二分查找）
  - 总体时间复杂度：O(sqrt(n) * log(sqrt(n)))

空间复杂度分析：
- 原始数组：O(n)
- 块分配数组：O(n)
- 块元素列表：O(n)
- 总体空间复杂度：O(n)

优化说明：
1. 使用分块算法将数组分成大小为sqrt(n)的块
2. 对每个块中的元素进行排序，便于二分查找
3. 更新操作时，需要重新排序对应的块
4. 查询操作时，对不完整的块直接遍历，对完整的块使用二分查找

算法说明：
GIVEAWAY问题要求支持区间更新和区间查询，可以使用分块算法解决：
1. 将数组分成大小为sqrt(n)的块
2. 对每个块中的元素进行排序
3. 更新操作时，修改原始数组并重新排序对应的块
4. 查询操作时，对不完整的块直接遍历，对完整的块使用二分查找统计大于等于k的元素个数

与其他方法的对比：
- 暴力法：更新O(1)，查询O(n)，总时间复杂度O(q * n)
- 线段树：更新O(log n)，查询O(log n)，但实现复杂
- 分块算法：更新O(sqrt(n))，查询O(sqrt(n) * log(sqrt(n)))，实现相对简单

工程化考虑：
1. 使用sys.stdin.readline()提高输入效率
2. 使用bisect模块进行二分查找
3. 对于频繁更新的场景，可以考虑使用懒惰重建策略
4. 注意边界条件的处理
"""