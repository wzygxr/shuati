"""
LeetCode 34. 在排序数组中查找元素的第一个和最后一个位置 (Python版本)

题目描述：
给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。
请你找出给定目标值在数组中的开始位置和结束位置。
如果数组中不存在目标值 target，返回 [-1, -1]。
必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。

示例：
输入：nums = [5,7,7,8,8,10], target = 8
输出：[3,4]

输入：nums = [5,7,7,8,8,10], target = 6
输出：[-1,-1]

输入：nums = [], target = 0
输出：[-1,-1]

约束条件：
- 0 <= nums.length <= 10^5
- -10^9 <= nums[i] <= 10^9
- nums 是一个非递减数组
- -10^9 <= target <= 10^9

解题思路：
这是二分查找的高级应用。我们需要分别找到目标值的第一个位置和最后一个位置。
1. 查找第一个位置：找到第一个等于目标值的元素
2. 查找最后一个位置：找到最后一个等于目标值的元素
两种查找都可以通过修改二分查找的逻辑来实现。

时间复杂度：O(log n)，其中 n 是数组的长度。需要执行两次二分查找。
空间复杂度：O(1)，只使用了常数级别的额外空间。

工程化考量：
1. 边界条件处理：空数组、单元素数组、目标值不存在
2. 异常输入处理：检查数组是否为 None
"""


class LeetCode34FindFirstAndLastPosition:
    """
    LeetCode 34 在排序数组中查找元素的第一个和最后一个位置解决方案类
    """
    
    @staticmethod
    def search_range(nums, target):
        """
        查找目标值的第一个和最后一个位置
        
        Args:
            nums: 非递减顺序排列的整数数组
            target: 目标值
            
        Returns:
            包含开始位置和结束位置的数组，如果不存在目标值则返回[-1, -1]
        """
        # 异常处理：检查数组是否为 None
        if nums is None:
            return [-1, -1]
        
        # 查找第一个位置
        first = LeetCode34FindFirstAndLastPosition._find_first(nums, target)
        
        # 如果第一个位置不存在，说明目标值不存在
        if first == -1:
            return [-1, -1]
        
        # 查找最后一个位置
        last = LeetCode34FindFirstAndLastPosition._find_last(nums, target)
        
        return [first, last]
    
    @staticmethod
    def _find_first(nums, target):
        """
        查找第一个等于目标值的元素
        
        Args:
            nums: 非递减顺序排列的整数数组
            target: 目标值
            
        Returns:
            第一个等于目标值的元素索引，如果不存在则返回-1
        """
        # 异常处理：检查数组是否为空
        if len(nums) == 0:
            return -1
        
        left = 0
        right = len(nums) - 1
        result = -1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                result = mid    # 记录找到的位置
                right = mid - 1  # 继续在左半部分查找
            elif nums[mid] < target:
                left = mid + 1
            else:
                right = mid - 1
        
        return result
    
    @staticmethod
    def _find_last(nums, target):
        """
        查找最后一个等于目标值的元素
        
        Args:
            nums: 非递减顺序排列的整数数组
            target: 目标值
            
        Returns:
            最后一个等于目标值的元素索引，如果不存在则返回-1
        """
        # 异常处理：检查数组是否为空
        if len(nums) == 0:
            return -1
        
        left = 0
        right = len(nums) - 1
        result = -1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                result = mid   # 记录找到的位置
                left = mid + 1  # 继续在右半部分查找
            elif nums[mid] < target:
                left = mid + 1
            else:
                right = mid - 1
        
        return result
    
    @staticmethod
    def search_range_alternative(nums, target):
        """
        另一种实现方式：使用一次二分查找找到任意一个目标值，然后向两边扩展
        注意：这种方法的时间复杂度在最坏情况下是O(n)，不满足题目要求
        
        Args:
            nums: 非递减顺序排列的整数数组
            target: 目标值
            
        Returns:
            包含开始位置和结束位置的数组，如果不存在目标值则返回[-1, -1]
        """
        # 异常处理：检查数组是否为 None 或空
        if nums is None or len(nums) == 0:
            return [-1, -1]
        
        # 使用标准二分查找找到任意一个目标值
        index = LeetCode34FindFirstAndLastPosition._binary_search(nums, target)
        
        # 如果没有找到目标值
        if index == -1:
            return [-1, -1]
        
        # 向左扩展找到第一个位置
        left = index
        while left > 0 and nums[left - 1] == target:
            left -= 1
        
        # 向右扩展找到最后一个位置
        right = index
        while right < len(nums) - 1 and nums[right + 1] == target:
            right += 1
        
        return [left, right]
    
    @staticmethod
    def _binary_search(nums, target):
        """
        标准二分查找实现
        
        Args:
            nums: 升序排列的整型数组
            target: 目标值
            
        Returns:
            目标值在数组中的索引，如果不存在则返回-1
        """
        left = 0
        right = len(nums) - 1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                return mid
            elif nums[mid] < target:
                left = mid + 1
            else:
                right = mid - 1
        
        return -1


def run_tests():
    """运行测试用例"""
    # 测试用例1
    nums1 = [5, 7, 7, 8, 8, 10]
    target1 = 8
    result1 = LeetCode34FindFirstAndLastPosition.search_range(nums1, target1)
    print("测试用例1:")
    print("数组: [5, 7, 7, 8, 8, 10]")
    print(f"目标值: {target1}")
    print(f"结果: [{result1[0]}, {result1[1]}]")
    print()
    
    # 测试用例2
    nums2 = [5, 7, 7, 8, 8, 10]
    target2 = 6
    result2 = LeetCode34FindFirstAndLastPosition.search_range(nums2, target2)
    print("测试用例2:")
    print("数组: [5, 7, 7, 8, 8, 10]")
    print(f"目标值: {target2}")
    print(f"结果: [{result2[0]}, {result2[1]}]")
    print()
    
    # 测试用例3
    nums3 = []
    target3 = 0
    result3 = LeetCode34FindFirstAndLastPosition.search_range(nums3, target3)
    print("测试用例3:")
    print("数组: []")
    print(f"目标值: {target3}")
    print(f"结果: [{result3[0]}, {result3[1]}]")
    print()
    
    # 测试用例4：单元素数组
    nums4 = [1]
    target4 = 1
    result4 = LeetCode34FindFirstAndLastPosition.search_range(nums4, target4)
    print("测试用例4:")
    print("数组: [1]")
    print(f"目标值: {target4}")
    print(f"结果: [{result4[0]}, {result4[1]}]")
    print()
    
    # 测试替代方法
    print("替代方法测试:")
    result5 = LeetCode34FindFirstAndLastPosition.search_range_alternative(nums1, target1)
    print(f"替代方法结果: [{result5[0]}, {result5[1]}]")


# 主函数
if __name__ == "__main__":
    run_tests()