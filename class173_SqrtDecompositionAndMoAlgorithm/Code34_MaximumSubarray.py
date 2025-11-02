# 最大子阵列 - 分块算法实现 (Python版本)
# 题目来源: 计蒜客
# 题目链接: https://www.jisuanke.com/course/705/27296
# 题目大意: 在数组中找出和最大的连续子数组（至少包含一个元素）
# 约束条件: 数组长度n不超过1000，元素为整数

import sys
import math

INF = 10**18

def main():
    n = int(sys.stdin.readline())
    s = sys.stdin.readline().split()
    arr = [0] * (n + 2)  # 1-based索引
    for i in range(1, n + 1):
        arr[i] = int(s[i - 1])
    
    # 分块相关变量
    blen = int(math.sqrt(n))
    if blen == 0:
        blen = 1
    block_count = (n + blen - 1) // blen
    
    # 分块预处理的结构
    pre_sum = [0] * (n + 2)
    block_sum = [0] * block_count
    l_max = [[-INF for _ in range(blen)] for __ in range(block_count)]  # l_max[i][j]: 从块i的第j个元素开始向右延伸的最大子数组和
    r_max = [[-INF for _ in range(blen)] for __ in range(block_count)]  # r_max[i][j]: 到块i的第j个元素结束向左延伸的最大子数组和
    max_sub = [-INF] * block_count  # 每个块内部的最大子数组和
    total_max = -INF
    
    # 计算前缀和
    for i in range(1, n + 1):
        pre_sum[i] = pre_sum[i - 1] + arr[i]
    
    # 预处理每个块的信息
    for b in range(block_count):
        start = b * blen + 1
        end = min((b + 1) * blen, n)
        
        # 计算块的总和
        block_sum[b] = pre_sum[end] - pre_sum[start - 1]
        
        # 计算l_max：从每个位置开始向右延伸的最大子数组和
        current_max = -INF
        current_sum = 0
        for i in range(end, start - 1, -1):
            current_sum += arr[i]
            current_max = max(current_max, current_sum)
            l_max[b][i - start] = current_max
        
        # 计算r_max：到每个位置结束向左延伸的最大子数组和
        current_max = -INF
        current_sum = 0
        for i in range(start, end + 1):
            current_sum += arr[i]
            current_max = max(current_max, current_sum)
            r_max[b][i - start] = current_max
        
        # 计算块内的最大子数组和（Kadane算法）
        kadane_max = -INF
        kadane_sum = 0
        for i in range(start, end + 1):
            kadane_sum = max(arr[i], kadane_sum + arr[i])
            kadane_max = max(kadane_max, kadane_sum)
        max_sub[b] = kadane_max
    
    # 计算整个数组的最大子数组和
    total_max = max(max_sub)
    
    # 检查跨越块的情况
    for b in range(block_count - 1):
        # 从块b的末尾向左延伸的最大值
        right_max = r_max[b][blen - 1] if b * blen + blen <= n else r_max[b][n - b * blen - 1]
        
        # 块b+1的总和累加
        current_sum = right_max
        total_max = max(total_max, current_sum)
        
        for next_b in range(b + 1, block_count):
            current_sum += block_sum[next_b]
            total_max = max(total_max, current_sum)
            # 考虑从next_b块的开始位置的情况
            total_max = max(total_max, current_sum - block_sum[next_b] + r_max[next_b][0])
    
    print(total_max)

# 朴素的Kadane算法实现（用于验证）
def kadane(arr, n):
    max_so_far = -INF
    max_ending_here = 0
    
    for i in range(1, n + 1):
        max_ending_here = max(arr[i], max_ending_here + arr[i])
        max_so_far = max(max_so_far, max_ending_here)
    
    return max_so_far

# 测试用例
# 示例1：
# 输入：
# 3
# 1 1 -2
# 输出：2
# 
# 示例2：
# 输入：
# 9
# -2 1 -3 4 -1 2 1 -5 4
# 输出：6

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 预处理时间：O(n)
- 分块初始化：O(n)
- 块内预处理：O(n)
- 跨越块处理：O(n) （最坏情况下）
- 总体时间复杂度：O(n)

空间复杂度分析：
- 数组arr：O(n)
- 前缀和数组pre_sum：O(n)
- 块信息数组：O(n)
- 总体空间复杂度：O(n)

Python语言特性注意事项：
1. 使用列表的列表来表示二维数组l_max和r_max
2. 注意Python中列表的索引处理，这里使用1-based索引
3. 对于最后一个块可能不完整的情况进行特殊处理
4. 输入输出使用sys.stdin.readline()和print()以提高效率

算法说明：
虽然对于静态数组，Kadane算法是最优的，但分块方法提供了一种可扩展的思路。
当需要支持动态更新操作时，分块方法可以在O(√n)时间内处理每次更新，
而如果直接使用Kadane算法，每次更新后重新计算需要O(n)时间。

分块方法的核心思想是预处理每个块的信息，包括：
1. 块内的最大子数组和
2. 从块内每个位置开始向右延伸的最大子数组和
3. 到块内每个位置结束向左延伸的最大子数组和

然后，对于跨越多个块的子数组，我们可以利用这些预处理的信息快速计算其和。
'''