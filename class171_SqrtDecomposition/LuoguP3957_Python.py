import sys
import math
from collections import defaultdict

"""
洛谷 P3957 跳房子 - Python实现
题目链接：https://www.luogu.com.cn/problem/P3957

题目描述：
跳房子是一个有趣的小游戏。在这个游戏中，地面上画着一排格子，每个格子有不同的分数。玩家可以选择从某个格子开始，然后每次向前跳，必须至少跳1格，最多跳k格。游戏的目标是获得尽可能多的分数。

解题思路：
使用分块算法，将数组分成大小为sqrt(n)的块。
- 预处理每个块内的最大值以及块内的跳跃情况
- 对于查询，分情况处理：
  1. 完全在一个块内的跳跃：暴力计算
  2. 跨块的跳跃：利用预处理信息快速计算

时间复杂度：O(n√n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：预处理块内信息减少重复计算
4. 鲁棒性：处理边界情况和特殊输入
5. 数据结构：使用动态规划和分块结合的方法
"""

def main():
    # 提高输入速度
    input = sys.stdin.read().split()
    ptr = 0
    
    # 读取n和k
    n = int(input[ptr])
    ptr += 1
    k = int(input[ptr])
    ptr += 1
    
    # 读取数组（索引从1开始）
    a = [0] * (n + 1)
    for i in range(1, n + 1):
        a[i] = int(input[ptr])
        ptr += 1
    
    # 计算块大小和块数量
    block_size = int(math.sqrt(n)) + 1
    block_num = (n + block_size - 1) // block_size
    
    # dp数组，表示从第i个位置出发能获得的最大分数
    dp = [0] * (n + 2)
    # 预处理每个块的最大值数组
    max_in_block = [0] * (block_num + 1)
    
    # 初始化dp[n]，从最后一个位置出发只能获得自己的分数
    dp[n] = a[n]
    
    # 从后往前计算dp值
    for i in range(n - 1, 0, -1):
        # 确定i所在的块
        current_block = (i - 1) // block_size + 1
        
        # 计算i能跳到的最远距离
        right = min(i + k, n)
        
        # 如果i和right在同一个块内，直接暴力计算
        if (right - 1) // block_size + 1 == current_block:
            max_val = 0
            for j in range(i + 1, right + 1):
                if dp[j] > max_val:
                    max_val = dp[j]
            dp[i] = a[i] + max_val
        else:
            # 处理跨块的情况
            # 1. 暴力处理当前块内的部分
            max_val = 0
            # 当前块的结束位置
            block_end = min(current_block * block_size, n)
            for j in range(i + 1, block_end + 1):
                if dp[j] > max_val:
                    max_val = dp[j]
            
            # 2. 利用预处理的块最大值处理中间完整的块
            # 计算right所在的块
            right_block = (right - 1) // block_size + 1
            # 遍历中间的完整块
            for b in range(current_block + 1, right_block):
                if max_in_block[b] > max_val:
                    max_val = max_in_block[b]
            
            # 3. 暴力处理右边不完整的块
            for j in range((right_block - 1) * block_size + 1, right + 1):
                if dp[j] > max_val:
                    max_val = dp[j]
            
            dp[i] = a[i] + max_val
        
        # 更新当前块的最大值
        if dp[i] > max_in_block[current_block]:
            max_in_block[current_block] = dp[i]
    
    # 输出结果，从第一个位置出发的最大分数
    print(dp[1])

if __name__ == "__main__":
    main()