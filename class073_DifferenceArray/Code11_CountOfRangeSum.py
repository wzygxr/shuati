import time

"""
LeetCode 327. 区间和的个数 (Count of Range Sum)

题目描述:
给定一个整数数组 nums，返回区间和在 [lower, upper] 之间的区间个数，按数组索引 i, j 满足 0 <= i <= j < n。

示例:
输入: nums = [-2,5,-1], lower = -2, upper = 2
输出: 3 
解释:
三个区间: [0,0], [2,2], [0,2]，它们的区间和分别为: -2, -1, 2。

提示:
1. 最直观的算法复杂度是 O(n^2) ，请尝试在线性时间复杂度 O(n log n) 内解决此问题。

题目链接: https://leetcode.com/problems/count-of-range-sum/

解题思路:
这个问题可以用归并排序的思想来解决，通过前缀和和归并排序相结合：
1. 计算前缀和数组 prefixSum，其中 prefixSum[i] 表示 nums[0...i-1] 的和
2. 对于每个 j，我们需要找到有多少个 i (i < j) 满足 lower <= prefixSum[j] - prefixSum[i] <= upper
3. 这等价于 prefixSum[j] - upper <= prefixSum[i] <= prefixSum[j] - lower
4. 使用归并排序过程中的有序性质，可以高效地统计满足条件的 i 的数量

具体步骤：
1. 计算前缀和数组
2. 对前缀和数组进行归并排序，并在归并排序的过程中统计满足条件的区间数量
3. 在归并排序的合并阶段，对于右半部分的每个元素，在左半部分中找到满足条件的元素范围
4. 使用双指针技术在 O(n) 时间内完成对每个右半部分元素的统计

时间复杂度: O(n log n) - 归并排序的时间复杂度
空间复杂度: O(n) - 需要额外空间存储前缀和数组和归并排序的临时数组

这是最优解，因为我们利用了归并排序的特性，将问题转化为在有序数组中查找范围，避免了暴力枚举的 O(n^2) 复杂度。
"""

class Solution:
    """
    计算区间和在 [lower, upper] 之间的区间个数
    
    Args:
        nums: 整数数组
        lower: 区间和的下限
        upper: 区间和的上限
    
    Returns:
        满足条件的区间个数
    """
    def countRangeSum(self, nums, lower, upper):
        if not nums:
            return 0
        
        n = len(nums)
        # 计算前缀和数组，prefixSum[i] 表示 nums[0...i-1] 的和
        prefix_sum = [0] * (n + 1)
        for i in range(n):
            prefix_sum[i + 1] = prefix_sum[i] + nums[i]
        
        # 对前缀和数组进行归并排序，并统计满足条件的区间数量
        return self._merge_sort(prefix_sum, 0, len(prefix_sum) - 1, lower, upper)
    
    """
    对前缀和数组进行归并排序，并在过程中统计满足条件的区间数量
    
    Args:
        prefix_sum: 前缀和数组
        left: 当前排序区间的左边界
        right: 当前排序区间的右边界
        lower: 区间和的下限
        upper: 区间和的上限
    
    Returns:
        满足条件的区间个数
    """
    def _merge_sort(self, prefix_sum, left, right, lower, upper):
        # 递归终止条件：区间只有一个元素
        if left >= right:
            return 0
        
        # 分治：将数组分成左右两部分
        mid = left + (right - left) // 2
        
        # 统计左半部分和右半部分各自满足条件的区间数量
        count = (self._merge_sort(prefix_sum, left, mid, lower, upper) +
                self._merge_sort(prefix_sum, mid + 1, right, lower, upper))
        
        # 统计跨越中点的满足条件的区间数量
        count += self._count_cross_range(prefix_sum, left, mid, right, lower, upper)
        
        # 合并左右两个有序数组
        self._merge(prefix_sum, left, mid, right)
        
        return count
    
    """
    统计跨越中点的满足条件的区间数量
    
    Args:
        prefix_sum: 前缀和数组
        left: 左边界
        mid: 中点
        right: 右边界
        lower: 区间和的下限
        upper: 区间和的上限
    
    Returns:
        满足条件的跨中点区间个数
    """
    def _count_cross_range(self, prefix_sum, left, mid, right, lower, upper):
        count = 0
        # 对于右半部分的每个元素 j，找到左半部分中满足条件的元素 i 的范围
        lower_bound = left  # 左边界指针，寻找 prefix_sum[i] >= prefix_sum[j] - upper
        upper_bound = left  # 右边界指针，寻找 prefix_sum[i] <= prefix_sum[j] - lower
        
        for j in range(mid + 1, right + 1):
            # 计算当前 j 对应的 i 的范围条件
            target_lower = prefix_sum[j] - upper
            target_upper = prefix_sum[j] - lower
            
            # 找到第一个大于等于 target_lower 的位置
            while lower_bound <= mid and prefix_sum[lower_bound] < target_lower:
                lower_bound += 1
            
            # 找到第一个大于 target_upper 的位置
            while upper_bound <= mid and prefix_sum[upper_bound] <= target_upper:
                upper_bound += 1
            
            # 满足条件的 i 的数量是 upper_bound - lower_bound
            count += upper_bound - lower_bound
        
        return count
    
    """
    合并两个有序数组
    
    Args:
        prefix_sum: 前缀和数组
        left: 左边界
        mid: 中点
        right: 右边界
    """
    def _merge(self, prefix_sum, left, mid, right):
        # 创建临时数组
        temp = []
        i = left      # 左半部分的指针
        j = mid + 1   # 右半部分的指针
        
        # 合并两个有序数组
        while i <= mid and j <= right:
            if prefix_sum[i] <= prefix_sum[j]:
                temp.append(prefix_sum[i])
                i += 1
            else:
                temp.append(prefix_sum[j])
                j += 1
        
        # 处理左半部分的剩余元素
        while i <= mid:
            temp.append(prefix_sum[i])
            i += 1
        
        # 处理右半部分的剩余元素
        while j <= right:
            temp.append(prefix_sum[j])
            j += 1
        
        # 将临时数组复制回原数组
        for k in range(len(temp)):
            prefix_sum[left + k] = temp[k]

"""
使用暴力法解决，时间复杂度 O(n^2)，仅用于测试
"""
def countRangeSumBruteForce(nums, lower, upper):
    count = 0
    n = len(nums)
    
    for i in range(n):
        current_sum = 0
        for j in range(i, n):
            current_sum += nums[j]
            if lower <= current_sum <= upper:
                count += 1
    
    return count

# 辅助函数：打印数组
def print_array(arr):
    print(f"[{', '.join(map(str, arr))}]")

# 测试代码
def main():
    solution = Solution()
    
    # 测试用例1
    nums1 = [-2, 5, -1]
    lower1 = -2
    upper1 = 2
    print("测试用例1 结果:", solution.countRangeSum(nums1, lower1, upper1))  # 预期输出: 3
    print("测试用例1 (暴力法) 结果:", countRangeSumBruteForce(nums1, lower1, upper1))  # 预期输出: 3

    # 测试用例2
    nums2 = [0]
    lower2 = 0
    upper2 = 0
    print("测试用例2 结果:", solution.countRangeSum(nums2, lower2, upper2))  # 预期输出: 1

    # 测试用例3 - 空数组
    nums3 = []
    lower3 = -1
    upper3 = 1
    print("测试用例3 (空数组) 结果:", solution.countRangeSum(nums3, lower3, upper3))  # 预期输出: 0
    
    # 测试用例4 - 大量数据
    nums4 = [i % 10 - 5 for i in range(1000)]  # 生成-5到4的随机数
    lower4 = -10
    upper4 = 10
    start_time = time.time()
    result4 = solution.countRangeSum(nums4, lower4, upper4)
    end_time = time.time()
    print("测试用例4 (大数据) 结果:", result4)
    print("测试用例4 耗时:", (end_time - start_time) * 1000, "ms")
    
    # 测试用例5 - 边界情况，有大数值
    nums5 = [2**31-1, -2**31, -1, 0]  # 对应Java的Integer.MAX_VALUE和Integer.MIN_VALUE
    lower5 = -1
    upper5 = 0
    print("测试用例5 (边界值) 结果:", solution.countRangeSum(nums5, lower5, upper5))  # 预期输出: 4

if __name__ == "__main__":
    main()