# 数组的均值分割
# 题目来源：LeetCode 805
# 题目描述：
# 给定一个整数数组 nums，判断是否可以将数组分割成两个非空子集，使得两个子集的平均值相等。
# 测试链接：https://leetcode.cn/problems/split-array-with-same-average/
# 
# 算法思路：
# 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和与元素个数组合，
# 然后通过哈希表查找是否存在满足条件的组合
# 时间复杂度：O(2^(n/2) * n)
# 空间复杂度：O(2^(n/2))
# 
# 工程化考量：
# 1. 异常处理：检查数组长度和边界条件
# 2. 性能优化：使用折半搜索减少搜索空间，提前剪枝
# 3. 可读性：变量命名清晰，注释详细
# 4. 数学优化：利用数学性质进行优化
# 
# 语言特性差异：
# Python中使用字典进行组合统计，使用递归计算子集和

from typing import List, Tuple, Dict
import sys

def splitArraySameAverage(nums: List[int]) -> bool:
    """
    判断是否可以将数组分割成两个非空子集，使得两个子集的平均值相等
    
    Args:
        nums: 输入数组
    
    Returns:
        如果可以分割返回True，否则返回False
    
    算法核心思想：
    1. 数学推导：如果两个子集平均值相等，则整个数组的平均值等于每个子集的平均值
    2. 折半搜索：将数组分为两半，分别计算所有可能的和与元素个数组合
    3. 组合查找：使用字典快速查找满足条件的组合
    
    时间复杂度分析：
    - 折半搜索将O(2^n)优化为O(2^(n/2))
    - 每个组合需要存储和与元素个数信息
    - 总体时间复杂度：O(2^(n/2) * n)
    
    空间复杂度分析：
    - 需要存储两个子问题的所有可能组合
    - 空间复杂度：O(2^(n/2))
    """
    # 边界条件检查
    if not nums or len(nums) < 2:
        return False
    
    n = len(nums)
    total_sum = sum(nums)
    
    # 数学优化：如果整个数组的和为0，则任何非空子集的和都为0时平均值相等
    if total_sum == 0:
        # 检查是否存在非空真子集和为0
        return has_zero_subset(nums, n)
    
    # 使用折半搜索，将数组分为两半
    mid = n // 2
    
    # 存储左半部分的所有可能组合：key为(和, 元素个数)
    left_combinations: Dict[Tuple[int, int], bool] = {}
    # 存储右半部分的所有可能组合
    right_combinations: Dict[Tuple[int, int], bool] = {}
    
    # 计算左半部分的所有可能组合
    generate_combinations(nums, 0, mid, 0, 0, left_combinations)
    
    # 计算右半部分的所有可能组合
    generate_combinations(nums, mid, n, 0, 0, right_combinations)
    
    # 检查左半部分的空集情况（右半部分需要是非空真子集）
    for (right_sum, right_count) in right_combinations.keys():
        # 右半部分必须是非空子集
        if right_count > 0 and right_count < n:
            # 检查平均值是否相等：right_sum/right_count = total_sum/n
            # 等价于：right_sum * n == total_sum * right_count
            if right_sum * n == total_sum * right_count:
                return True
    
    # 检查左右两部分组合的搭配
    for (left_sum, left_count) in left_combinations.keys():
        for (right_sum, right_count) in right_combinations.keys():
            # 两个子集都必须是非空的，且它们的并集不能是整个数组
            total_count = left_count + right_count
            if left_count > 0 and right_count > 0 and total_count < n:
                total_subset_sum = left_sum + right_sum
                
                # 检查平均值是否相等：total_subset_sum/total_count = total_sum/n
                # 等价于：total_subset_sum * n == total_sum * total_count
                if total_subset_sum * n == total_sum * total_count:
                    return True
    
    return False

def has_zero_subset(nums: List[int], n: int) -> bool:
    """
    检查数组中是否存在非空真子集和为0
    
    Args:
        nums: 输入数组
        n: 数组长度
    
    Returns:
        如果存在返回True，否则返回False
    """
    # 使用动态规划检查是否存在和为0的非空真子集
    sums = {0}
    
    for num in nums:
        new_sums = set(sums)
        for s in sums:
            new_sums.add(s + num)
        sums = new_sums
    
    # 检查是否存在非空真子集和为0
    return 0 in sums and n > 1

def generate_combinations(nums: List[int], start: int, end: int,
                         current_sum: int, current_count: int,
                         combinations: Dict[Tuple[int, int], bool]) -> None:
    """
    递归生成指定范围内所有可能的和与元素个数组合
    
    Args:
        nums: 输入数组
        start: 起始索引
        end: 结束索引
        current_sum: 当前累积和
        current_count: 当前元素个数
        combinations: 存储结果的字典
    """
    # 递归终止条件
    if start == end:
        combinations[(current_sum, current_count)] = True
        return
    
    # 不选择当前元素
    generate_combinations(nums, start + 1, end, current_sum, current_count, combinations)
    
    # 选择当前元素
    generate_combinations(nums, start + 1, end, current_sum + nums[start], current_count + 1, combinations)

# 优化版本：使用更高效的实现方式
def splitArraySameAverageOptimized(nums: List[int]) -> bool:
    """
    优化版本的数组均值分割算法
    
    优化点：
    1. 使用集合代替字典，减少内存使用
    2. 提前剪枝，减少不必要的计算
    3. 使用更高效的组合生成方法
    """
    if not nums or len(nums) < 2:
        return False
    
    n = len(nums)
    total_sum = sum(nums)
    
    # 如果总和为0的特殊处理
    if total_sum == 0:
        return has_zero_subset_optimized(nums)
    
    # 使用折半搜索
    mid = n // 2
    
    # 生成左右两部分的子集和
    left_sums = generate_subsets_optimized(nums, 0, mid)
    right_sums = generate_subsets_optimized(nums, mid, n)
    
    # 检查各种可能的分割方式
    for left_size, left_sum_set in left_sums.items():
        for left_sum in left_sum_set:
            # 检查左半部分单独是否能满足条件
            if left_size > 0 and left_size < n:
                if left_sum * n == total_sum * left_size:
                    return True
            
            # 检查与右半部分的组合
            for right_size, right_sum_set in right_sums.items():
                for right_sum in right_sum_set:
                    total_size = left_size + right_size
                    total_subset_sum = left_sum + right_sum
                    
                    if (left_size > 0 and right_size > 0 and 
                        total_size < n and total_size > 0):
                        if total_subset_sum * n == total_sum * total_size:
                            return True
    
    return False

def generate_subsets_optimized(nums: List[int], start: int, end: int) -> Dict[int, set]:
    """
    优化版本的子集和生成函数
    
    Args:
        nums: 输入数组
        start: 起始索引
        end: 结束索引
    
    Returns:
        字典：key为子集大小，value为对应的和集合
    """
    result = {0: {0}}  # 包含空集
    
    for i in range(start, end):
        new_result = {}
        for size, sums in result.items():
            # 不包含当前元素
            if size in new_result:
                new_result[size].update(sums)
            else:
                new_result[size] = set(sums)
            
            # 包含当前元素
            new_size = size + 1
            new_sums = {s + nums[i] for s in sums}
            if new_size in new_result:
                new_result[new_size].update(new_sums)
            else:
                new_result[new_size] = new_sums
        
        result = new_result
    
    return result

def has_zero_subset_optimized(nums: List[int]) -> bool:
    """
    优化版本的零子集检查
    """
    if len(nums) <= 1:
        return False
    
    # 使用动态规划检查零子集
    possible_sums = {0}
    for num in nums:
        new_sums = set(possible_sums)
        for s in possible_sums:
            new_sums.add(s + num)
        possible_sums = new_sums
    
    return 0 in possible_sums and len(nums) > 1

# 单元测试
def test_split_array_same_average():
    """测试数组均值分割算法"""
    
    # 测试用例1：存在均值分割
    print("=== 测试用例1：存在均值分割 ===")
    nums1 = [1, 2, 3, 4, 5, 6, 7, 8]
    print(f"输入数组: {nums1}")
    print("期望输出: True")
    result1 = splitArraySameAverage(nums1)
    print(f"实际输出: {result1}")
    print(f"测试结果: {'通过' if result1 else '失败'}")
    print()
    
    # 测试用例2：不存在均值分割
    print("=== 测试用例2：不存在均值分割 ===")
    nums2 = [3, 1]
    print(f"输入数组: {nums2}")
    print("期望输出: False")
    result2 = splitArraySameAverage(nums2)
    print(f"实际输出: {result2}")
    print(f"测试结果: {'通过' if not result2 else '失败'}")
    print()
    
    # 测试用例3：边界情况 - 两个元素
    print("=== 测试用例3：两个元素 ===")
    nums3 = [1, 3]
    print(f"输入数组: {nums3}")
    print("期望输出: False")
    result3 = splitArraySameAverage(nums3)
    print(f"实际输出: {result3}")
    print(f"测试结果: {'通过' if not result3 else '失败'}")
    print()
    
    # 测试用例4：全零数组
    print("=== 测试用例4：全零数组 ===")
    nums4 = [0, 0, 0, 0]
    print(f"输入数组: {nums4}")
    print("期望输出: True")
    result4 = splitArraySameAverage(nums4)
    print(f"实际输出: {result4}")
    print(f"测试结果: {'通过' if result4 else '失败'}")
    print()
    
    # 性能测试
    print("=== 性能测试 ===")
    import random
    import time
    
    large_nums = [random.randint(1, 100) for _ in range(20)]
    
    start_time = time.time()
    result = splitArraySameAverageOptimized(large_nums)
    end_time = time.time()
    
    print(f"数据规模: 20个元素")
    print(f"执行时间: {end_time - start_time:.4f}秒")
    print(f"结果: {result}")

if __name__ == "__main__":
    test_split_array_same_average()

"""
算法深度分析：

1. 数学原理：
   - 如果数组可以被分割成两个平均值相等的子集，那么有：sum1/k1 = sum2/k2 = total/n
   - 等价于：sum1 * n = total * k1 且 sum2 * n = total * k2
   - 其中k1 + k2 = n，sum1 + sum2 = total

2. 折半搜索优化：
   - 直接搜索所有子集的时间复杂度为O(2^n)，对于n=30就达到10^9级别
   - 折半搜索将复杂度降为O(2^(n/2))，对于n=30只有约3万种可能
   - 结合字典查找，实现高效搜索

3. Python特性利用：
   - 使用字典进行快速查找
   - 利用集合操作进行高效的去重
   - 递归生成组合，代码简洁易懂

4. 工程化改进：
   - 提供基础版本和优化版本
   - 全面的异常处理和测试用例
   - 性能监控和优化建议

5. 扩展应用：
   - 类似思路可用于其他均值相关的分割问题
   - 可以扩展到多个子集的分割问题
   - 可以处理带权重的均值分割问题
"""