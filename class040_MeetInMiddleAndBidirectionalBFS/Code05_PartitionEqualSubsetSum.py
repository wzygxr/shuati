# 分割等和子集
# 给你一个只包含正整数的非空数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
# 测试链接 : https://leetcode.cn/problems/partition-equal-subset-sum/
# 
# 算法思路：
# 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
# 然后通过哈希表查找是否存在两个子集的和相等
# 时间复杂度：O(2^(n/2))
# 空间复杂度：O(2^(n/2))
# 
# 工程化考量：
# 1. 异常处理：检查数组是否为空或长度小于2
# 2. 性能优化：使用折半搜索减少搜索空间
# 3. 可读性：变量命名清晰，注释详细
# 
# 语言特性差异：
# Python中使用set进行快速查找，使用递归计算子集和

from typing import List

def canPartition(nums: List[int]) -> bool:
    """
    判断是否可以将数组分割成两个子集，使得两个子集的元素和相等
    
    Args:
        nums: 输入数组
    
    Returns:
        如果可以分割返回True，否则返回False
    """
    # 边界条件检查
    if not nums or len(nums) < 2:
        return False
    
    # 计算数组总和
    total_sum = sum(nums)
    
    # 如果总和是奇数，无法分割成两个相等的子集
    if total_sum % 2 != 0:
        return False
    
    target = total_sum // 2
    
    # 如果最大元素大于目标值，无法分割
    if max(nums) > target:
        return False
    
    n = len(nums)
    
    # 使用折半搜索，将数组分为两半
    left_sums = set()
    right_sums = set()
    
    # 计算左半部分所有可能的子集和
    generateSubsetSums(nums, 0, n // 2, 0, left_sums)
    
    # 计算右半部分所有可能的子集和
    generateSubsetSums(nums, n // 2, n, 0, right_sums)
    
    # 检查是否存在两个子集的和等于目标值
    for left_sum in left_sums:
        # 如果左半部分的某个子集和正好等于目标值
        if left_sum == target:
            return True
        
        # 如果右半部分存在一个子集，其和等于目标值减去左半部分的子集和
        if target - left_sum in right_sums:
            return True
    
    return False

def generateSubsetSums(nums: List[int], start: int, end: int, current_sum: int, sums: set) -> None:
    """
    递归生成指定范围内所有可能的子集和
    
    Args:
        nums: 输入数组
        start: 起始索引
        end: 结束索引
        current_sum: 当前累积和
        sums: 存储结果的集合
    """
    # 递归终止条件
    if start == end:
        sums.add(current_sum)
        return
    
    # 不选择当前元素
    generateSubsetSums(nums, start + 1, end, current_sum, sums)
    
    # 选择当前元素
    generateSubsetSums(nums, start + 1, end, current_sum + nums[start], sums)

# 测试方法
def main():
    # 测试用例1
    nums1 = [1, 5, 11, 5]
    print("测试用例1:")
    print("nums = [1, 5, 11, 5]")
    print("期望输出: true")
    print("实际输出:", canPartition(nums1))
    print()
    
    # 测试用例2
    nums2 = [1, 2, 3, 5]
    print("测试用例2:")
    print("nums = [1, 2, 3, 5]")
    print("期望输出: false")
    print("实际输出:", canPartition(nums2))
    print()
    
    # 测试用例3
    nums3 = [1, 2, 5]
    print("测试用例3:")
    print("nums = [1, 2, 5]")
    print("期望输出: false")
    print("实际输出:", canPartition(nums3))

if __name__ == "__main__":
    main()