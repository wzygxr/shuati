# AtCoder AGC020 C - Median Sum
# 题目链接: https://atcoder.jp/contests/agc020/tasks/agc020_c
# 题目大意:
# 给定N个整数A1, A2, ..., AN。
# 考虑A的所有非空子序列的和。有2^N-1个这样的和，这是一个奇数。
# 将这些和按非递减顺序排列为S1, S2, ..., S_{2^N-1}。
# 找到这个列表的中位数S_{2^{N-1}}。
#
# 约束条件:
# 1 ≤ N ≤ 2000
# 1 ≤ Ai ≤ 2000
# 所有输入值都是整数。
#
# 输入:
# 输入以以下格式从标准输入给出:
# N
# A1 A2 ... AN
#
# 输出:
# 打印A的所有非空子序列的和按排序后的中位数。
#
# 解题思路:
# 这是一个经典的bitset优化DP问题。
# 1. 使用bitset来表示所有可能的子集和
# 2. bitset的第i位为1表示存在一个子集的和为i
# 3. 对于每个元素x，我们执行: dp |= dp << x
#    这表示将之前所有可达的和都加上x，同时保留原来的和
# 4. 中位数的计算有一个技巧:
#    所有子集和的总和为sum，那么中位数就是从(sum+1)/2开始第一个可达的和
#
# 时间复杂度: O(N * sum / 32)  其中sum是所有元素的和
# 空间复杂度: O(sum) bit

# Python中没有内置的bitset，但我们可以使用set来模拟
# 或者使用第三方库bitarray

# 方法1: 使用位运算模拟bitset
# 利用Python整数的位运算功能来模拟bitset
def solve_with_bitwise():
    # 读取元素数量
    n = int(input())
    # 读取所有元素
    a = list(map(int, input().split()))
    
    # 计算所有元素的总和
    sum_a = sum(a)
    
    # 使用整数的位来模拟bitset
    # dp的第i位为1表示存在一个子集的和为i
    dp = 1  # 初始状态，空集的和为0，即第0位为1
    
    # 对于每个元素，更新所有可能的子集和
    for x in a:
        # dp |= dp << x
        # 这表示既保留原来的和，又加上x后的新和
        # dp << x 将dp中所有为1的位向左移动x位
        # dp | (dp << x) 按位或操作，将原来的和与新和合并
        dp |= (dp << x)
    
    # 找到中位数
    # 有一个数学技巧: 从(sum+1)/2开始第一个可达的和就是中位数
    # 计算目标位置
    target = (sum_a + 1) // 2
    # 循环找到第一个可达的和
    while True:
        # 检查dp的第target位是否为1
        # dp & (1 << target) 提取第target位
        if dp & (1 << target):
            # 找到中位数，输出并结束程序
            print(target)
            break
        target += 1

# 方法2: 使用set来模拟可达的和
# 利用Python的set数据结构来记录所有可达的和
def solve_with_set():
    # 读取元素数量
    n = int(input())
    # 读取所有元素
    a = list(map(int, input().split()))
    
    # 计算所有元素的总和
    sum_a = sum(a)
    
    # 使用set来记录所有可达的和
    dp = {0}  # 初始状态，空集的和为0
    
    # 对于每个元素，更新所有可能的子集和
    for x in a:
        # 将之前所有可达的和都加上x，同时保留原来的和
        new_dp = set()
        # 遍历当前所有可达的和
        for val in dp:
            new_dp.add(val)      # 保留原来的和
            new_dp.add(val + x)  # 加上x后的新和
        # 更新dp为新的可达和集合
        dp = new_dp
    
    # 转换为排序后的列表
    sums = sorted(list(dp))
    
    # 找到中位数
    # 所有子集和的总数是2^N - 1，中位数是第2^(N-1)个(从1开始计数)
    # 在0索引中，它是第2^(N-1) - 1个元素
    # 计算中位数的索引位置
    median_index = (1 << (n - 1)) - 1  # 2^(N-1) - 1
    # 输出中位数
    print(sums[median_index])

# 方法3: 使用bitarray库（如果安装了的话）
# 需要先安装: pip install bitarray
'''
from bitarray import bitarray

def solve_with_bitarray():
    # 读取元素数量
    n = int(input())
    # 读取所有元素
    a = list(map(int, input().split()))
    
    # 计算所有元素的总和
    sum_a = sum(a)
    
    # 使用bitarray来模拟bitset
    # dp的第i位为1表示存在一个子集的和为i
    dp = bitarray(sum_a + 1)
    # 初始化为全0
    dp.setall(0)
    # 初始状态，空集的和为0
    dp[0] = 1
    
    # 对于每个元素，更新所有可能的子集和
    for x in a:
        # dp |= dp << x
        # 复制dp
        shifted = dp.copy()
        # 左移x位
        shifted <<= x
        # 按位或操作
        dp |= shifted
    
    # 找到中位数
    # 从(sum+1)/2开始第一个可达的和就是中位数
    # 循环找到第一个可达的和
    for i in range((sum_a + 1) // 2, sum_a + 1):
        if dp[i]:
            print(i)
            break
'''

# 程序入口点
if __name__ == "__main__":
    # 选择其中一种方法来解决问题
    solve_with_bitwise()  # 使用位运算模拟bitset（推荐，效率最高）
    # solve_with_set()     # 使用set模拟（容易理解但效率较低）
    # solve_with_bitarray() # 使用bitarray库（需要额外安装）