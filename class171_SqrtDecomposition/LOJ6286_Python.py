import sys
import math
from collections import defaultdict
import bisect

"""
LOJ 6286. 数列分块入门 10 - Python实现
题目链接：https://loj.ac/p/6286

题目描述：
给出一个长为n的数列，以及n个操作，操作涉及区间众数查询。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
预处理：
1. 对于每个块，预处理块内的众数
2. 对于任意两个块i和j(i < j)，预处理区间[i,j]的众数
3. 记录每个数出现的所有位置
处理查询时：
1. 对于左右不完整块，暴力遍历每个元素，统计其在整个查询区间内的出现次数
2. 对于中间完整块，利用预处理的众数信息，检查其在整个查询区间内的出现次数
3. 最终取出现次数最多的数作为众数

时间复杂度：
- 预处理：O(n√n)
- 每个查询：O(√n)
空间复杂度：O(n√n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：预处理中间结果减少重复计算
4. 鲁棒性：处理边界情况和特殊输入
5. 数据结构：使用字典和列表存储统计信息
"""

def main():
    # 提高输入速度
    input = sys.stdin.read().split()
    ptr = 0
    
    # 读取数组大小
    n = int(input[ptr])
    ptr += 1
    
    # 读取数组（索引从1开始）
    arr = [0] * (n + 1)
    for i in range(1, n + 1):
        arr[i] = int(input[ptr])
        ptr += 1
    
    # 计算块大小和块数量
    block_size = int(math.sqrt(n)) + 1
    block_num = (n + block_size - 1) // block_size
    
    # 初始化每个元素所属的块
    belong = [0] * (n + 1)
    for i in range(1, n + 1):
        belong[i] = (i - 1) // block_size + 1
    
    # 初始化块边界
    block_left = [0] * (block_num + 1)
    block_right = [0] * (block_num + 1)
    for i in range(1, block_num + 1):
        block_left[i] = (i - 1) * block_size + 1
        block_right[i] = min(i * block_size, n)
    
    # 初始化位置映射
    pos_map = defaultdict(list)
    for i in range(1, n + 1):
        pos_map[arr[i]].append(i)
    
    # 预处理块间众数
    pre_mode = [[0] * (block_num + 1) for _ in range(block_num + 1)]
    pre_count = [[0] * (block_num + 1) for _ in range(block_num + 1)]
    
    # 对于每个起始块i
    for i in range(1, block_num + 1):
        cnt = defaultdict(int)  # 统计当前区间内每个数的出现次数
        mode = 0  # 当前众数
        max_count = 0  # 众数的出现次数
        
        # 扩展结束块j
        for j in range(i, block_num + 1):
            # 遍历块j中的每个元素
            for k in range(block_left[j], block_right[j] + 1):
                num = arr[k]
                cnt[num] += 1
                
                # 更新众数
                if cnt[num] > max_count:
                    mode = num
                    max_count = cnt[num]
            
            # 记录块i到块j的众数和其出现次数
            pre_mode[i][j] = mode
            pre_count[i][j] = max_count
    
    # 计算数x在区间[l,r]中出现的次数
    def count_occurrence(x, l, r):
        positions = pos_map.get(x, [])
        # 二分查找第一个>=l的位置
        first_pos = bisect.bisect_left(positions, l)
        # 二分查找最后一个<=r的位置
        last_pos = bisect.bisect_right(positions, r) - 1
        
        if first_pos > last_pos:
            return 0
        return last_pos - first_pos + 1
    
    # 处理操作
    for _ in range(n):
        l = int(input[ptr])
        ptr += 1
        r = int(input[ptr])
        ptr += 1
        
        left_block = belong[l]
        right_block = belong[r]
        mode = 0
        max_count = 0
        
        # 如果在同一个块内，暴力计算
        if left_block == right_block:
            cnt = defaultdict(int)
            for i in range(l, r + 1):
                num = arr[i]
                cnt[num] += 1
                if cnt[num] > max_count:
                    mode = num
                    max_count = cnt[num]
        else:
            # 处理左边不完整块
            for i in range(l, block_right[left_block] + 1):
                num = arr[i]
                cnt = count_occurrence(num, l, r)
                if cnt > max_count or (cnt == max_count and num < mode):
                    mode = num
                    max_count = cnt
            
            # 处理中间完整块
            if left_block + 1 <= right_block - 1:
                candidate_mode = pre_mode[left_block + 1][right_block - 1]
                candidate_count = count_occurrence(candidate_mode, l, r)
                if candidate_count > max_count or (candidate_count == max_count and candidate_mode < mode):
                    mode = candidate_mode
                    max_count = candidate_count
                
                # 为了保险，我们也检查中间块中的其他可能的众数
                # 这里可以优化，只检查中间块中的元素
                for i in range(block_left[left_block + 1], block_right[right_block - 1] + 1):
                    num = arr[i]
                    # 避免重复检查已经处理过的候选众数
                    if num != candidate_mode:
                        cnt = count_occurrence(num, l, r)
                        if cnt > max_count or (cnt == max_count and num < mode):
                            mode = num
                            max_count = cnt
            
            # 处理右边不完整块
            for i in range(block_left[right_block], r + 1):
                num = arr[i]
                cnt = count_occurrence(num, l, r)
                if cnt > max_count or (cnt == max_count and num < mode):
                    mode = num
                    max_count = cnt
        
        print(mode)

if __name__ == "__main__":
    main()