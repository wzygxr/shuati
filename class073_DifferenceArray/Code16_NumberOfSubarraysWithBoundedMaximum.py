import time

"""
LeetCode 795. 区间子数组个数 (Number of Subarrays with Bounded Maximum)

题目描述:
给定一个整数数组 nums 和两个整数 left 和 right，请返回数组中满足以下条件的非空子数组的个数：
- 子数组中的最大值在区间 [left, right] 内

示例1:
输入: nums = [2, 1, 4, 3], left = 2, right = 3
输出: 3
解释:
满足条件的子数组是 [2], [2, 1], [3]

示例2:
输入: nums = [2, 9, 2, 5, 6], left = 2, right = 8
输出: 7
解释:
满足条件的子数组有 [2], [2, 9], [2], [2, 5], [5], [5, 6], [6]

提示:
1. 1 <= nums.length <= 10^5
2. 0 <= nums[i] <= 10^9
3. 0 <= left <= right <= 10^9

题目链接: https://leetcode.com/problems/number-of-subarrays-with-bounded-maximum/

解题思路:
这个问题可以通过以下两种方法来解决：

方法一：暴力枚举所有子数组并检查最大值（不推荐，时间复杂度太高）

方法二：使用计数法，考虑每个位置作为子数组最大值时的贡献
但这种方法实现起来比较复杂。

方法三：使用前缀和的思想，计算「最大值小于等于 right」的子数组个数，
减去「最大值小于 left」的子数组个数，得到「最大值在 [left, right] 之间」的子数组个数。

这里我们选择方法三，具体实现思路如下：
1. 定义一个辅助函数 count_subarrays，用于计算数组中最大值小于等于给定阈值的子数组个数
2. 最终结果就是 count_subarrays(nums, right) - count_subarrays(nums, left - 1)

时间复杂度: O(n)，其中 n 是数组的长度。我们只需要遍历数组两次。
空间复杂度: O(1)，只使用了常数级别的额外空间。

这是最优解，因为我们需要至少遍历数组一次，时间复杂度无法更低。
"""

def count_subarrays(nums, threshold):
    """
    计算数组中最大值小于等于给定阈值的子数组个数
    
    Args:
        nums: 整数数组
        threshold: 阈值
    
    Returns:
        最大值小于等于阈值的子数组个数
    """
    count = 0
    current_length = 0
    
    for num in nums:
        # 如果当前元素小于等于阈值，则可以将它加入到当前连续子数组中
        if num <= threshold:
            current_length += 1
            # 增加的子数组个数就是当前连续子数组的长度
            # 例如，[1,2,3]增加一个4，则新增的子数组有[4], [3,4], [2,3,4], [1,2,3,4]，共4个
            count += current_length
        else:
            # 遇到大于阈值的元素，重置连续子数组长度
            current_length = 0
    
    return count

def num_subarray_bounded_max(nums, left, right):
    """
    计算数组中满足条件的非空子数组的个数
    
    Args:
        nums: 整数数组
        left: 左边界
        right: 右边界
    
    Returns:
        满足条件的非空子数组个数
    """
    # 参数校验
    if not nums or left > right:
        return 0
    
    # 计算最大值小于等于right的子数组个数，减去最大值小于left的子数组个数
    return count_subarrays(nums, right) - count_subarrays(nums, left - 1)

def num_subarray_bounded_max_alternative(nums, left, right):
    """
    另一种实现方式，直接计算满足条件的子数组个数
    
    Args:
        nums: 整数数组
        left: 左边界
        right: 右边界
    
    Returns:
        满足条件的非空子数组个数
    """
    # 参数校验
    if not nums or left > right:
        return 0
    
    count = 0
    # 记录上一个大于right的位置
    last_invalid = -1
    # 记录上一个在[left, right]范围内的位置
    last_valid = -1
    
    for i in range(len(nums)):
        if nums[i] > right:
            # 遇到大于right的元素，更新last_invalid
            last_invalid = i
        elif left <= nums[i] <= right:
            # 遇到在[left, right]范围内的元素
            last_valid = i
            # 以当前元素为最大值的子数组个数为i - last_invalid
            count += i - last_invalid
        else:  # nums[i] < left
            # 遇到小于left的元素
            # 如果之前有在[left, right]范围内的元素，则可以与当前元素组成有效子数组
            if last_valid > last_invalid:
                count += last_valid - last_invalid
    
    return count

def print_array(nums):
    """
    打印数组
    
    Args:
        nums: 要打印的数组
    """
    print(f"[{' '.join(map(str, nums))}]")

# 测试代码
def main():
    # 测试用例1
    nums1 = [2, 1, 4, 3]
    left1 = 2
    right1 = 3
    
    print("测试用例1 - nums = ")
    print_array(nums1)
    print(f"left = {left1}, right = {right1}")
    print(f"num_subarray_bounded_max 结果: {num_subarray_bounded_max(nums1, left1, right1)}")  # 预期输出: 3
    print(f"num_subarray_bounded_max_alternative 结果: {num_subarray_bounded_max_alternative(nums1, left1, right1)}")  # 预期输出: 3
    print()
    
    # 测试用例2
    nums2 = [2, 9, 2, 5, 6]
    left2 = 2
    right2 = 8
    
    print("测试用例2 - nums = ")
    print_array(nums2)
    print(f"left = {left2}, right = {right2}")
    print(f"num_subarray_bounded_max 结果: {num_subarray_bounded_max(nums2, left2, right2)}")  # 预期输出: 7
    print(f"num_subarray_bounded_max_alternative 结果: {num_subarray_bounded_max_alternative(nums2, left2, right2)}")  # 预期输出: 7
    print()
    
    # 测试用例3 - 边界情况：数组长度为1
    nums3 = [1]
    left3 = 1
    right3 = 1
    
    print("测试用例3 - nums = ")
    print_array(nums3)
    print(f"left = {left3}, right = {right3}")
    print(f"num_subarray_bounded_max 结果: {num_subarray_bounded_max(nums3, left3, right3)}")  # 预期输出: 1
    print(f"num_subarray_bounded_max_alternative 结果: {num_subarray_bounded_max_alternative(nums3, left3, right3)}")  # 预期输出: 1
    print()
    
    # 测试用例4 - 边界情况：所有元素都在范围内
    nums4 = [2, 3, 4]
    left4 = 1
    right4 = 5
    
    print("测试用例4 - nums = ")
    print_array(nums4)
    print(f"left = {left4}, right = {right4}")
    print(f"num_subarray_bounded_max 结果: {num_subarray_bounded_max(nums4, left4, right4)}")  # 预期输出: 6
    print(f"num_subarray_bounded_max_alternative 结果: {num_subarray_bounded_max_alternative(nums4, left4, right4)}")  # 预期输出: 6
    print()
    
    # 测试用例5 - 边界情况：没有元素在范围内
    nums5 = [1, 1, 1]
    left5 = 2
    right5 = 3
    
    print("测试用例5 - nums = ")
    print_array(nums5)
    print(f"left = {left5}, right = {right5}")
    print(f"num_subarray_bounded_max 结果: {num_subarray_bounded_max(nums5, left5, right5)}")  # 预期输出: 0
    print(f"num_subarray_bounded_max_alternative 结果: {num_subarray_bounded_max_alternative(nums5, left5, right5)}")  # 预期输出: 0
    print()
    
    # 性能测试
    print("性能测试:")
    large_nums = []
    # 生成一个混合数组，一部分元素在范围内，一部分不在
    for i in range(100000):
        if i % 5 == 0:
            large_nums.append(100)  # 大于right的元素
        elif i % 3 == 0:
            large_nums.append(50)   # 在范围内的元素
        else:
            large_nums.append(10)   # 小于left的元素
    left6 = 40
    right6 = 60
    
    start_time = time.time()
    result1 = num_subarray_bounded_max(large_nums, left6, right6)
    end_time = time.time()
    print(f"大数组 - num_subarray_bounded_max 结果: {result1}")
    print(f"大数组 - num_subarray_bounded_max 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    start_time = time.time()
    result2 = num_subarray_bounded_max_alternative(large_nums, left6, right6)
    end_time = time.time()
    print(f"大数组 - num_subarray_bounded_max_alternative 结果: {result2}")
    print(f"大数组 - num_subarray_bounded_max_alternative 耗时: {(end_time - start_time) * 1000:.2f}ms")

if __name__ == "__main__":
    main()