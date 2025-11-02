"""
LeetCode 327. 区间和的个数 (Count of Range Sum) - Python版本
题目链接: https://leetcode.cn/problems/count-of-range-sum/

题目描述:
给定一个整数数组 nums 以及两个整数 lower 和 upper，
求出数组中所有子数组的和在 [lower, upper] 范围内的个数。

解题思路:
使用归并排序的思想解决区间和计数问题。
1. 首先计算前缀和数组，将问题转化为：对于每个前缀和sum[i]，
   统计在它之前的前缀和sum[j] (j < i) 中，有多少个满足
   lower <= sum[i] - sum[j] <= upper，即 sum[i] - upper <= sum[j] <= sum[i] - lower
2. 利用归并排序的分治思想，在合并过程中统计满足条件的区间和个数
3. 在合并两个有序数组时，使用滑动窗口技术统计满足条件的元素对

时间复杂度分析:
- 计算前缀和: O(n)
- 归并排序: O(n log n)
- 总时间复杂度: O(n log n)
空间复杂度: O(n) 用于存储前缀和数组和辅助数组

算法详解:
归并排序分治解法是解决区间和计数问题的经典方法。通过将问题分解为子问题，
在合并过程中统计跨越中点的区间和个数，可以高效地解决这类问题。
"""

class Code01_CountOfRangeSum1:
    def __init__(self):
        self.sum = []      # 前缀和数组
        self.help = []      # 辅助数组，用于归并排序
        self.low = 0        # 区间下界
        self.up = 0         # 区间上界
    
    def countRangeSum(self, nums, lower, upper):
        """
        计算数组中区间和在指定范围内的子数组个数
        
        Args:
            nums: 输入数组
            lower: 区间下界
            upper: 区间上界
            
        Returns:
            满足条件的子数组个数
        """
        n = len(nums)
        if n == 0:
            return 0
        
        # 初始化数组
        self.sum = [0] * n
        self.help = [0] * n
        
        # 计算前缀和数组
        self.sum[0] = nums[0]
        for i in range(1, n):
            self.sum[i] = self.sum[i - 1] + nums[i]
        
        self.low = lower
        self.up = upper
        
        # 使用归并排序分治求解
        return self._f(0, n - 1)
    
    def _f(self, l, r):
        """
        归并排序分治求解
        
        Args:
            l: 区间左边界
            r: 区间右边界
            
        Returns:
            满足条件的区间和个数
        """
        if l == r:
            # 单个元素的情况，检查是否在区间内
            return 1 if self.sum[l] >= self.low and self.sum[l] <= self.up else 0
        
        mid = l + ((r - l) >> 1)
        ans = self._f(l, mid) + self._f(mid + 1, r)
        
        # 统计跨越中点的区间和个数
        windowL = l
        windowR = l
        
        for i in range(mid + 1, r + 1):
            min_val = self.sum[i] - self.up   # sum[j]的最小值
            max_val = self.sum[i] - self.low  # sum[j]的最大值
            
            # 移动左窗口指针，找到第一个满足sum[j] >= min_val的位置
            while windowL <= mid and self.sum[windowL] < min_val:
                windowL += 1
            
            # 移动右窗口指针，找到最后一个满足sum[j] <= max_val的位置
            while windowR <= mid and self.sum[windowR] <= max_val:
                windowR += 1
            
            # 统计满足条件的个数
            if windowL <= mid and windowR > windowL:
                ans += windowR - windowL
        
        # 合并两个有序数组
        self._merge(l, mid, r)
        
        return ans
    
    def _merge(self, l, mid, r):
        """
        合并两个有序数组
        
        Args:
            l: 左边界
            mid: 中间位置
            r: 右边界
        """
        i, j, k = l, mid + 1, l
        
        while i <= mid and j <= r:
            if self.sum[i] <= self.sum[j]:
                self.help[k] = self.sum[i]
                i += 1
            else:
                self.help[k] = self.sum[j]
                j += 1
            k += 1
        
        while i <= mid:
            self.help[k] = self.sum[i]
            i += 1
            k += 1
        
        while j <= r:
            self.help[k] = self.sum[j]
            j += 1
            k += 1
        
        # 将辅助数组的结果复制回原数组
        for idx in range(l, r + 1):
            self.sum[idx] = self.help[idx]

def test_countRangeSum():
    """
    测试函数
    """
    solution = Code01_CountOfRangeSum1()
    
    # 测试用例1
    nums1 = [-2, 5, -1]
    lower1 = -2
    upper1 = 2
    result1 = solution.countRangeSum(nums1, lower1, upper1)
    print(f"测试用例1: nums = {nums1}, lower = {lower1}, upper = {upper1}")
    print(f"结果: {result1} (期望: 3)")
    print()
    
    # 测试用例2
    nums2 = [0]
    lower2 = 0
    upper2 = 0
    result2 = solution.countRangeSum(nums2, lower2, upper2)
    print(f"测试用例2: nums = {nums2}, lower = {lower2}, upper = {upper2}")
    print(f"结果: {result2} (期望: 1)")
    print()
    
    # 测试用例3
    nums3 = [1, 2, 3, 4]
    lower3 = 3
    upper3 = 8
    result3 = solution.countRangeSum(nums3, lower3, upper3)
    print(f"测试用例3: nums = {nums3}, lower = {lower3}, upper = {upper3}")
    print(f"结果: {result3} (期望: 6)")
    print()
    
    # 测试用例4 - 边界情况：空数组
    nums4 = []
    lower4 = 0
    upper4 = 0
    result4 = solution.countRangeSum(nums4, lower4, upper4)
    print(f"测试用例4: nums = {nums4}, lower = {lower4}, upper = {upper4}")
    print(f"结果: {result4} (期望: 0)")
    print()
    
    # 测试用例5 - 边界情况：单个元素
    nums5 = [5]
    lower5 = 3
    upper5 = 7
    result5 = solution.countRangeSum(nums5, lower5, upper5)
    print(f"测试用例5: nums = {nums5}, lower = {lower5}, upper = {upper5}")
    print(f"结果: {result5} (期望: 1)")

if __name__ == "__main__":
    test_countRangeSum()