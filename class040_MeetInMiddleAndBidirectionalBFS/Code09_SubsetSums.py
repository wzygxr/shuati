# Subset Sums (SPOJ SUBSUMS)
# 题目来源：SPOJ
# 题目描述：
# 给定一个数组和两个整数a和b，找出有多少个子集的和在[a, b]区间内。
# 注意：空集的和为0，空集也应该被考虑。
# 测试链接：https://www.spoj.com/problems/SUBSUMS/
# 
# 算法思路：
# 使用折半搜索（Meet in the Middle）算法，将数组分为两半分别计算所有可能的和，
# 然后对其中一半进行排序，通过二分查找找到符合条件的组合数目
# 时间复杂度：O(2^(n/2) * log(2^(n/2))) = O(2^(n/2) * n)
# 空间复杂度：O(2^(n/2))
# 
# 工程化考量：
# 1. 异常处理：检查输入是否合法
# 2. 性能优化：使用折半搜索减少搜索空间
# 3. 可读性：变量命名清晰，注释详细
# 
# 语言特性差异：
# Python中使用列表存储子集和，使用sort方法进行排序，使用bisect模块进行二分查找

from bisect import bisect_left, bisect_right
from typing import List

def countSubsets(arr: List[int], a: int, b: int) -> int:
    """
    计算数组中和在[a, b]区间内的子集数目
    
    Args:
        arr: 输入数组
        a: 区间左边界
        b: 区间右边界
    
    Returns:
        符合条件的子集数目
    """
    # 边界条件检查
    if not arr:
        # 空数组只有空集一种可能，检查0是否在[a, b]区间内
        return 1 if a <= 0 <= b else 0
    
    n = len(arr)
    mid = n // 2
    
    # 分别存储左右两部分的所有可能子集和
    left_sums = []
    right_sums = []
    
    # 计算左半部分的所有可能子集和
    generateSubsetSums(arr, 0, mid - 1, 0, left_sums)
    
    # 计算右半部分的所有可能子集和
    generateSubsetSums(arr, mid, n - 1, 0, right_sums)
    
    # 对右半部分的子集和进行排序，以便进行二分查找
    right_sums.sort()
    
    # 统计符合条件的组合数目
    count = 0
    for left_sum in left_sums:
        # 查找右半部分中满足 a - leftSum <= rightSum <= b - leftSum 的数目
        lower_bound = a - left_sum
        upper_bound = b - left_sum
        
        # 查找第一个大于等于lower_bound的位置
        start_index = bisect_left(right_sums, lower_bound)
        
        # 查找第一个大于upper_bound的位置
        end_index = bisect_right(right_sums, upper_bound)
        
        # 累加符合条件的数目
        count += (end_index - start_index)
    
    return count

def generateSubsetSums(arr: List[int], start: int, end: int, current_sum: int, sums: List[int]) -> None:
    """
    递归生成指定范围内所有可能的子集和
    
    Args:
        arr: 输入数组
        start: 起始索引
        end: 结束索引
        current_sum: 当前累积和
        sums: 存储结果的列表
    """
    # 递归终止条件
    if start > end:
        sums.append(current_sum)
        return
    
    # 不选择当前元素
    generateSubsetSums(arr, start + 1, end, current_sum, sums)
    
    # 选择当前元素
    generateSubsetSums(arr, start + 1, end, current_sum + arr[start], sums)

# 测试方法
def main():
    # 读取输入
    print("请输入数组长度n，以及区间[a, b]：")
    n, a, b = map(int, input().split())
    
    print("请输入数组元素：")
    arr = list(map(int, input().split()))
    
    # 计算结果
    result = countSubsets(arr, a, b)
    print(f"满足条件的子集数目：{result}")
    
    # 测试用例1
    print("\n测试用例1：")
    arr1 = [1, -2, 3]
    a1 = -1
    b1 = 2
    print(f"数组：{arr1}")
    print(f"区间：[{a1}, {b1}]")
    print("期望输出：3")  # 空集(0), {1}, {-2, 3}
    print(f"实际输出：{countSubsets(arr1, a1, b1)}")
    
    # 测试用例2
    print("\n测试用例2：")
    arr2 = [1, 2, 3, 4]
    a2 = 4
    b2 = 7
    print(f"数组：{arr2}")
    print(f"区间：[{a2}, {b2}]")
    print("期望输出：6")  # {4}, {1,3}, {2,3}, {1,2,3}, {1,4}, {2,4}
    print(f"实际输出：{countSubsets(arr2, a2, b2)}")

if __name__ == "__main__":
    main()