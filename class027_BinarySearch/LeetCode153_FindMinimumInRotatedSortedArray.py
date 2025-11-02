"""
LeetCode 153. 寻找旋转排序数组中的最小值 (Python版本)

题目描述：
已知一个长度为 n 的数组，预先按照升序排列，经由 1 到 n 次旋转后，得到输入数组。
例如，原数组 nums = [0,1,2,4,5,6,7] 在变化后可能得到：
若旋转 4 次，则可以得到 [4,5,6,7,0,1,2]
若旋转 7 次，则可以得到 [0,1,2,4,5,6,7]
注意，数组 [a[0], a[1], a[2], ..., a[n-1]] 旋转一次的结果为数组 [a[n-1], a[1], a[2], ..., a[n-2]] 。
给你一个元素值互不相同的数组 nums，它原来是一个升序排列的数组，并按上述情形进行了多次旋转。
请你找出并返回数组中的最小元素。
必须设计一个时间复杂度为 O(log n) 的算法解决此问题。

示例：
输入：nums = [3,4,5,1,2]
输出：1
解释：原数组为 [1,2,3,4,5] ，旋转 3 次得到输入数组。

输入：nums = [4,5,6,7,0,1,2]
输出：0
解释：原数组为 [0,1,2,4,5,6,7] ，旋转 3 次得到输入数组。

输入：nums = [11,13,15,17]
输出：11
解释：原数组为 [11,13,15,17] ，旋转 4 次得到输入数组。

约束条件：
- n == nums.length
- 1 <= n <= 5000
- -5000 <= nums[i] <= 5000
- nums 中的所有整数互不相同
- nums 原来是一个升序排序的数组，并进行了 1 至 n 次旋转

解题思路：
这是二分查找在旋转数组中的应用。旋转后的数组可以分为两个有序部分，
最小值位于第二个有序部分的开头。
我们可以通过比较中间元素与右边界元素的大小关系来判断最小值在哪一部分。

时间复杂度：O(log n)，其中 n 是数组的长度。
空间复杂度：O(1)，只使用了常数级别的额外空间。

工程化考量：
1. 边界条件处理：单元素数组、未旋转数组
2. 异常输入处理：检查数组是否为 None
"""


class LeetCode153FindMinimumInRotatedSortedArray:
    """
    LeetCode 153 寻找旋转排序数组中的最小值解决方案类
    """
    
    @staticmethod
    def find_min(nums):
        """
        寻找旋转排序数组中的最小值
        
        Args:
            nums: 旋转后的升序数组
            
        Returns:
            数组中的最小元素
            
        Raises:
            ValueError: 当数组为 None 或空时抛出异常
        """
        # 异常处理：检查数组是否为 None
        if nums is None:
            raise ValueError("数组不能为 None")
        
        # 异常处理：检查数组是否为空
        if len(nums) == 0:
            raise ValueError("数组不能为空")
        
        # 初始化左右边界
        left = 0
        right = len(nums) - 1
        
        # 如果数组没有旋转，直接返回第一个元素
        if nums[left] < nums[right]:
            return nums[left]
        
        # 二分查找最小值
        while left < right:
            # 防止整数溢出的中点计算方式
            mid = left + (right - left) // 2
            
            # 如果中间元素大于右边界元素，说明最小值在右半部分
            if nums[mid] > nums[right]:
                left = mid + 1
            # 如果中间元素小于右边界元素，说明最小值在左半部分（包括mid）
            elif nums[mid] < nums[right]:
                right = mid
            # 这种情况不会出现，因为题目说明所有元素互不相同
            else:
                # 为了代码完整性，处理相等的情况
                right = mid
        
        # 循环结束时，left == right，指向最小值
        return nums[left]
    
    @staticmethod
    def find_min_alternative(nums):
        """
        另一种实现方式：比较中间元素与左边界元素
        
        Args:
            nums: 旋转后的升序数组
            
        Returns:
            数组中的最小元素
            
        Raises:
            ValueError: 当数组为 None 或空时抛出异常
        """
        # 异常处理：检查数组是否为 None
        if nums is None:
            raise ValueError("数组不能为 None")
        
        # 异常处理：检查数组是否为空
        if len(nums) == 0:
            raise ValueError("数组不能为空")
        
        # 初始化左右边界
        left = 0
        right = len(nums) - 1
        
        # 如果数组没有旋转，直接返回第一个元素
        if nums[left] < nums[right]:
            return nums[left]
        
        # 二分查找最小值
        while left < right:
            # 防止整数溢出的中点计算方式
            mid = left + (right - left) // 2
            
            # 如果中间元素大于左边界元素，说明最小值在右半部分
            if nums[mid] > nums[left]:
                # 特殊情况：如果左边界元素小于右边界元素，说明最小值是左边界元素
                if nums[left] < nums[right]:
                    return nums[left]
                left = mid + 1
            # 如果中间元素小于左边界元素，说明最小值在左半部分（包括mid）
            elif nums[mid] < nums[left]:
                right = mid
            # 这种情况不会出现，因为题目说明所有元素互不相同
            else:
                # 为了代码完整性，处理相等的情况
                left = mid + 1
        
        # 循环结束时，left == right，指向最小值
        return nums[left]
    
    @staticmethod
    def find_min_linear(nums):
        """
        线性查找方法（用于对比和验证）
        时间复杂度：O(n)
        
        Args:
            nums: 旋转后的升序数组
            
        Returns:
            数组中的最小元素
            
        Raises:
            ValueError: 当数组为 None 或空时抛出异常
        """
        # 异常处理：检查数组是否为 None
        if nums is None:
            raise ValueError("数组不能为 None")
        
        # 异常处理：检查数组是否为空
        if len(nums) == 0:
            raise ValueError("数组不能为空")
        
        min_val = nums[0]
        for i in range(1, len(nums)):
            if nums[i] < min_val:
                min_val = nums[i]
        
        return min_val


def run_tests():
    """运行测试用例"""
    # 测试用例1
    nums1 = [3, 4, 5, 1, 2]
    result1 = LeetCode153FindMinimumInRotatedSortedArray.find_min(nums1)
    print("测试用例1:")
    print("数组: [3, 4, 5, 1, 2]")
    print(f"结果: {result1}")
    print()
    
    # 测试用例2
    nums2 = [4, 5, 6, 7, 0, 1, 2]
    result2 = LeetCode153FindMinimumInRotatedSortedArray.find_min(nums2)
    print("测试用例2:")
    print("数组: [4, 5, 6, 7, 0, 1, 2]")
    print(f"结果: {result2}")
    print()
    
    # 测试用例3
    nums3 = [11, 13, 15, 17]
    result3 = LeetCode153FindMinimumInRotatedSortedArray.find_min(nums3)
    print("测试用例3:")
    print("数组: [11, 13, 15, 17]")
    print(f"结果: {result3}")
    print()
    
    # 测试用例4：单元素数组
    nums4 = [5]
    result4 = LeetCode153FindMinimumInRotatedSortedArray.find_min(nums4)
    print("测试用例4:")
    print("数组: [5]")
    print(f"结果: {result4}")
    print()
    
    # 测试用例5：两元素数组
    nums5 = [2, 1]
    result5 = LeetCode153FindMinimumInRotatedSortedArray.find_min(nums5)
    print("测试用例5:")
    print("数组: [2, 1]")
    print(f"结果: {result5}")
    print()
    
    # 测试替代方法
    print("替代方法测试:")
    result6 = LeetCode153FindMinimumInRotatedSortedArray.find_min_alternative(nums2)
    print(f"替代方法结果: {result6}")
    
    # 测试线性方法
    print("线性方法测试:")
    result7 = LeetCode153FindMinimumInRotatedSortedArray.find_min_linear(nums2)
    print(f"线性方法结果: {result7}")


# 主函数
if __name__ == "__main__":
    run_tests()