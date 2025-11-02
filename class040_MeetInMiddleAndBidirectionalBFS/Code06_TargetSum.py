# 目标和
# 给你一个非负整数数组 nums 和一个整数 target 。
# 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式 。
# 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
# 测试链接 : https://leetcode.cn/problems/target-sum/
# 
# 算法思路：
# 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
# 然后通过字典统计不同表达式的数目
# 时间复杂度：O(2^(n/2))
# 空间复杂度：O(2^(n/2))
# 
# 工程化考量：
# 1. 异常处理：检查数组是否为空
# 2. 性能优化：使用折半搜索减少搜索空间
# 3. 可读性：变量命名清晰，注释详细
# 
# 语言特性差异：
# Python中使用字典进行计数统计，使用递归计算子集和

from typing import List
from collections import defaultdict

def findTargetSumWays(nums: List[int], target: int) -> int:
    """
    计算可以通过添加 '+' 或 '-' 构造的、运算结果等于 target 的不同表达式的数目
    
    Args:
        nums: 输入数组
        target: 目标值
    
    Returns:
        不同表达式的数目
    """
    # 边界条件检查
    if not nums:
        return 0
    
    n = len(nums)
    
    # 使用折半搜索，将数组分为两半
    left_sums = defaultdict(int)
    right_sums = defaultdict(int)
    
    # 计算左半部分所有可能的子集和及其出现次数
    generateSubsetSums(nums, 0, n // 2, 0, left_sums)
    
    # 计算右半部分所有可能的子集和及其出现次数
    generateSubsetSums(nums, n // 2, n, 0, right_sums)
    
    # 统计满足条件的表达式数目
    count = 0
    for left_sum, left_count in left_sums.items():
        # 查找右半部分是否存在和为(target - left_sum)的子集
        right_count = right_sums[target - left_sum]
        count += left_count * right_count
    
    return count

def generateSubsetSums(nums: List[int], start: int, end: int, current_sum: int, sums: defaultdict) -> None:
    """
    递归生成指定范围内所有可能的子集和及其出现次数
    
    Args:
        nums: 输入数组
        start: 起始索引
        end: 结束索引
        current_sum: 当前累积和
        sums: 存储结果的字典，key为和，value为出现次数
    """
    # 递归终止条件
    if start == end:
        sums[current_sum] += 1
        return
    
    # 不选择当前元素（相当于添加 '-' 号）
    generateSubsetSums(nums, start + 1, end, current_sum - nums[start], sums)
    
    # 选择当前元素（相当于添加 '+' 号）
    generateSubsetSums(nums, start + 1, end, current_sum + nums[start], sums)

# 测试方法
def main():
    # 测试用例1
    nums1 = [1, 1, 1, 1, 1]
    target1 = 3
    print("测试用例1:")
    print("nums = [1, 1, 1, 1, 1], target = 3")
    print("期望输出: 5")
    print("实际输出:", findTargetSumWays(nums1, target1))
    print()
    
    # 测试用例2
    nums2 = [1]
    target2 = 1
    print("测试用例2:")
    print("nums = [1], target = 1")
    print("期望输出: 1")
    print("实际输出:", findTargetSumWays(nums2, target2))
    print()
    
    # 测试用例3
    nums3 = [1, 0]
    target3 = 1
    print("测试用例3:")
    print("nums = [1, 0], target = 1")
    print("期望输出: 2")
    print("实际输出:", findTargetSumWays(nums3, target3))

if __name__ == "__main__":
    main()