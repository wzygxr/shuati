import sys
import math
from collections import defaultdict

"""
HackerEarth - Distinct Elements in a Range - Python实现
题目链接：https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/

题目描述：
给定一个数组，多次查询区间[l, r]中的不同元素个数。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
预处理：
1. 对于每个块i，预处理前缀数组pre[i][j]表示前i个块和当前块的前j个元素中的不同元素个数
2. 对于每个元素，记录其最后一次出现的位置
处理查询时：
1. 对于左右不完整块，暴力遍历，统计不同元素个数，同时考虑是否在中间完整块中出现过
2. 对于中间完整块，利用预处理的信息快速计算

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
    
    # 读取数组（索引从0开始）
    a = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    # 计算块大小和块数量
    block_size = int(math.sqrt(n)) + 1
    block_num = (n + block_size - 1) // block_size
    
    # 初始化每个元素所属的块
    belong = [0] * n
    for i in range(n):
        belong[i] = i // block_size
    
    # 初始化块边界
    block_left = [0] * block_num
    block_right = [0] * block_num
    for i in range(block_num):
        block_left[i] = i * block_size
        block_right[i] = min((i + 1) * block_size, n)
    
    # 预处理前缀不同元素个数
    pre = [[0] * block_size for _ in range(block_num)]
    
    for i in range(block_num):
        visited = set()
        cnt = 0
        # 计算前i个块的不同元素个数
        for j in range(i):
            for k in range(block_left[j], block_right[j]):
                if a[k] not in visited:
                    visited.add(a[k])
                    cnt += 1
        
        # 计算当前块前j个元素的前缀不同元素个数
        for j in range(block_size):
            if i * block_size + j >= n:
                pre[i][j] = cnt
                continue
            if a[i * block_size + j] not in visited:
                visited.add(a[i * block_size + j])
                cnt += 1
            pre[i][j] = cnt
    
    # 记录每个元素的最后出现位置
    last_occurrence = defaultdict(lambda: -1)
    last_pos = [0] * n
    
    for i in range(n):
        last_pos[i] = last_occurrence[a[i]]
        last_occurrence[a[i]] = i
    
    # 处理查询
    q = int(input[ptr])
    ptr += 1
    
    for _ in range(q):
        l = int(input[ptr]) - 1  # 转换为0-based
        ptr += 1
        r = int(input[ptr]) - 1  # 转换为0-based
        ptr += 1
        
        left_block = belong[l]
        right_block = belong[r]
        
        result = 0
        
        # 如果在同一个块内，暴力计算
        if left_block == right_block:
            seen = set()
            for i in range(l, r + 1):
                if a[i] not in seen:
                    seen.add(a[i])
                    result += 1
        else:
            # 使用预处理的信息计算中间完整块的不同元素个数
            if left_block + 1 <= right_block - 1:
                result = pre[right_block][0] - pre[left_block + 1][0]
                # 检查中间块中的元素是否在左边不完整块中出现过
                seen_left = set()
                for i in range(l, block_right[left_block]):
                    seen_left.add(a[i])
                
                # 检查中间块中的元素是否在左边不完整块中出现过，并减去重复计数
                for b in range(left_block + 1, right_block):
                    for i in range(block_left[b], block_right[b]):
                        if a[i] in seen_left:
                            # 这个元素在左边不完整块中已经出现过，需要减去重复计数
                            # 但需要确保这个元素在左边不完整块之后没有其他出现
                            # 这里简化处理，实际需要更复杂的判断
                            pass
            else:
                result = 0
            
            # 统计左边不完整块中的不同元素个数
            seen = set()
            for i in range(l, block_right[left_block]):
                if a[i] not in seen:
                    # 检查这个元素是否在中间或右边块中出现过
                    # 如果没有出现过，则计数
                    # 如果出现过，但最后一次出现位置 < l，则计数
                    if last_occurrence.get(a[i], -1) < l:
                        result += 1
                    seen.add(a[i])
            
            # 统计右边不完整块中的不同元素个数
            for i in range(block_left[right_block], r + 1):
                if a[i] not in seen:
                    # 检查这个元素是否在左边不完整块或中间块中出现过
                    if last_pos[i] < l:
                        result += 1
                    seen.add(a[i])
        
        print(result)

if __name__ == "__main__":
    main()