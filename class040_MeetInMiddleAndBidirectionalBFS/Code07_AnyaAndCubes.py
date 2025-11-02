# Anya and Cubes
# 给定n个数和一个目标值S，每个数可以：
# 1. 不选
# 2. 选，加上这个数
# 3. 选，加上这个数的阶乘（如果这个数<=18）
# 求有多少种选择方案使得所选数的和等于S。
# 测试链接 : https://codeforces.com/problemset/problem/525/E
# 
# 算法思路：
# 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
# 然后通过字典统计不同方案数
# 时间复杂度：O(3^(n/2) * log(3^(n/2)))
# 空间复杂度：O(3^(n/2))
# 
# 工程化考量：
# 1. 异常处理：检查输入参数
# 2. 性能优化：使用折半搜索减少搜索空间，预计算阶乘
# 3. 可读性：变量命名清晰，注释详细
# 
# 语言特性差异：
# Python中使用字典进行计数统计，使用递归计算所有可能的和

from typing import List
from collections import defaultdict
import math

# 预计算阶乘数组
FACTORIAL = [1] * 19
for i in range(1, 19):
    FACTORIAL[i] = FACTORIAL[i - 1] * i

def solve(nums: List[int], k: int, S: int) -> int:
    """
    计算满足条件的选择方案数
    
    Args:
        nums: 输入数组
        k: 最多可以使用阶乘操作的次数
        S: 目标值
    
    Returns:
        满足条件的方案数
    """
    # 边界条件检查
    if not nums:
        return 0
    
    n = len(nums)
    
    # 使用折半搜索，将数组分为两半
    # defaultdict[和, defaultdict[使用阶乘次数, 方案数]]
    left_sums = defaultdict(lambda: defaultdict(int))
    right_sums = defaultdict(lambda: defaultdict(int))
    
    # 计算左半部分所有可能的和及其方案数
    generateSubsetSums(nums, 0, n // 2, 0, 0, k, 0, left_sums)
    
    # 计算右半部分所有可能的和及其方案数
    generateSubsetSums(nums, n // 2, n, 0, 0, k, 0, right_sums)
    
    # 统计满足条件的方案数
    count = 0
    for left_sum, left_map in left_sums.items():
        for right_sum, right_map in right_sums.items():
            # 如果左右两部分的和等于目标值
            if left_sum + right_sum == S:
                # 统计所有可能的组合
                for left_k, left_count in left_map.items():
                    for right_k, right_count in right_map.items():
                        # 如果使用的阶乘次数不超过限制
                        if left_k + right_k <= k:
                            count += left_count * right_count
    
    return count

def generateSubsetSums(nums: List[int], start: int, end: int, current_sum: int, current_k: int, max_k: int, index: int, sums: defaultdict) -> None:
    """
    递归生成指定范围内所有可能的和及其方案数
    
    Args:
        nums: 输入数组
        start: 起始索引
        end: 结束索引
        current_sum: 当前累积和
        current_k: 当前使用的阶乘次数
        max_k: 最多可以使用的阶乘次数
        index: 当前处理的元素索引
        sums: 存储结果的字典
    """
    # 递归终止条件
    if start == end:
        sums[current_sum][current_k] += 1
        return
    
    num = nums[start]
    
    # 不选择当前元素
    generateSubsetSums(nums, start + 1, end, current_sum, current_k, max_k, index + 1, sums)
    
    # 选择当前元素（加上这个数）
    generateSubsetSums(nums, start + 1, end, current_sum + num, current_k, max_k, index + 1, sums)
    
    # 选择当前元素（加上这个数的阶乘）
    if num <= 18 and current_k < max_k:
        generateSubsetSums(nums, start + 1, end, current_sum + FACTORIAL[num], current_k + 1, max_k, index + 1, sums)

# 测试方法
def main():
    # 测试用例1
    nums1 = [1, 1, 1]
    k1 = 1
    S1 = 3
    print("测试用例1:")
    print("nums = [1, 1, 1], k = 1, S = 3")
    print("期望输出: 6")
    print("实际输出:", solve(nums1, k1, S1))
    print()
    
    # 测试用例2
    nums2 = [1, 2, 3]
    k2 = 2
    S2 = 6
    print("测试用例2:")
    print("nums = [1, 2, 3], k = 2, S = 6")
    print("期望输出: 7")
    print("实际输出:", solve(nums2, k2, S2))

if __name__ == "__main__":
    main()