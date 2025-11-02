# 最接近目标值的子序列和
# 给你一个整数数组 nums 和一个目标值 goal
# 你需要从 nums 中选出一个子序列，使子序列元素总和最接近 goal
# 也就是说，如果子序列元素和为 sum ，你需要 最小化绝对差 abs(sum - goal)
# 返回 abs(sum - goal) 可能的 最小值
# 注意，数组的子序列是通过移除原始数组中的某些元素（可能全部或无）而形成的数组。
# 数据量描述:
# 1 <= nums.length <= 40
# -10^7 <= nums[i] <= 10^7
# -10^9 <= goal <= 10^9
# 测试链接 : https://leetcode.cn/problems/closest-subsequence-sum/
# 
# 算法思路：
# 使用折半搜索（Meet in the Middle）算法，将数组分为两半分别计算所有可能的和，
# 然后通过双指针技术找到最接近目标值的组合
# 时间复杂度：O(n * 2^(n/2))
# 空间复杂度：O(2^(n/2))

from typing import List
import math

def collect(nums: List[int], i: int, e: int, s: int, sum_arr: List[int], fill: List[int]) -> None:
    """
    递归计算数组指定范围内所有可能的和（考虑相同元素的优化）
    
    Args:
        nums: 输入数组
        i: 当前处理的元素索引
        e: 结束索引
        s: 当前累积和
        sum_arr: 存储结果的数组
        fill: 记录当前填充位置的数组（使用数组模拟引用传递）
    """
    if i == e:
        # 到达边界，将当前和加入结果数组
        sum_arr[fill[0]] = s
        fill[0] += 1
    else:
        # nums[i.....]这一组，相同的数字有几个
        j = i + 1
        # 找到所有与当前元素相同的元素
        while j < e and nums[j] == nums[i]:
            j += 1
        # nums[ 1 1 1 1 1 2....
        #       i         j
        # 对于相同的元素，考虑选择0个、1个、2个...k个的情况
        for k in range(j - i + 1):
            # k = 0个
            # k = 1个
            # k = 2个
            # 递归处理下一个不同元素
            collect(nums, j, e, s + k * nums[i], sum_arr, fill)

def minAbsDifference(nums: List[int], goal: int) -> int:
    """
    计算子序列和与目标值的最小绝对差
    
    Args:
        nums: 输入数组
        goal: 目标值
    
    Returns:
        最小绝对差
    """
    n = len(nums)
    # 计算数组中所有正数和负数的和，用于边界判断
    min_sum = 0
    max_sum = 0
    for i in range(n):
        if nums[i] >= 0:
            max_sum += nums[i]
        else:
            min_sum += nums[i]
    
    # 如果最大和小于目标值，返回目标值与最大和的差
    if max_sum < goal:
        return abs(max_sum - goal)
    
    # 如果最小和大于目标值，返回目标值与最小和的差
    if min_sum > goal:
        return abs(min_sum - goal)
    
    # 原始数组排序，为了后面递归的时候，还能剪枝
    # 常数优化
    nums.sort()
    
    MAXN = 1 << 20
    lsum = [0] * MAXN
    rsum = [0] * MAXN
    
    # 计算前半部分所有可能的和
    fill = [0]
    collect(nums, 0, n // 2, 0, lsum, fill)
    lsize = fill[0]
    
    # 计算后半部分所有可能的和
    fill[0] = 0
    collect(nums, n // 2, n, 0, rsum, fill)
    rsize = fill[0]
    
    # 对两个数组进行排序
    lsum[:lsize] = sorted(lsum[:lsize])
    rsum[:rsize] = sorted(rsum[:rsize])
    
    # 初始化答案为目标值的绝对值（空子序列的情况）
    ans = abs(goal)
    
    # 使用双指针技术找到最接近目标值的组合
    j = rsize - 1
    for i in range(lsize):
        # 移动右指针，找到更接近目标值的位置
        while j > 0 and abs(goal - lsum[i] - rsum[j - 1]) <= abs(goal - lsum[i] - rsum[j]):
            j -= 1
        # 更新最小绝对差
        ans = min(ans, abs(goal - lsum[i] - rsum[j]))
    
    return ans

# 测试方法
def main():
    # 测试用例1
    nums1 = [5, -7, 3, 5]
    goal1 = 6
    print("测试用例1:")
    print("nums = [5, -7, 3, 5], goal = 6")
    print("期望输出: 0")
    print("实际输出:", minAbsDifference(nums1, goal1))
    print()
    
    # 测试用例2
    nums2 = [7, -9, 15, -2]
    goal2 = -5
    print("测试用例2:")
    print("nums = [7, -9, 15, -2], goal = -5")
    print("期望输出: 1")
    print("实际输出:", minAbsDifference(nums2, goal2))
    print()
    
    # 测试用例3
    nums3 = [1, 2, 3]
    goal3 = -7
    print("测试用例3:")
    print("nums = [1, 2, 3], goal = -7")
    print("期望输出: 7")
    print("实际输出:", minAbsDifference(nums3, goal3))

if __name__ == "__main__":
    main()