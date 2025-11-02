"""
LeetCode 704. 二分查找 (Python版本)

题目描述：
给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target ，
写一个函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。

示例：
输入: nums = [-1,0,3,5,9,12], target = 9
输出: 4
解释: 9 出现在 nums 中并且下标为 4

输入: nums = [-1,0,3,5,9,12], target = 2
输出: -1
解释: 2 不存在 nums 中因此返回 -1

约束条件：
- 你可以假设 nums 中的所有元素是不重复的。
- n 将在 [1, 10000]之间。
- nums 的每个元素都将在 [-9999, 9999]之间。

解题思路：
使用标准的二分查找算法。由于数组是有序的，我们可以每次比较中间元素与目标值，
根据比较结果缩小搜索范围，直到找到目标值或搜索范围为空。

时间复杂度：O(log n)，其中 n 是数组的长度。每次搜索都会将搜索范围缩小一半。
空间复杂度：O(1)，只使用了常数级别的额外空间。

工程化考量：
1. 边界条件处理：空数组、单元素数组
2. 异常输入处理：检查数组是否为 None
"""


class LeetCode704BinarySearch:
    """
    LeetCode 704 二分查找解决方案类
    """
    
    @staticmethod
    def search(nums, target):
        """
        标准二分查找实现
        
        Args:
            nums: 升序排列的整型数组
            target: 目标值
            
        Returns:
            目标值在数组中的索引，如果不存在则返回-1
        """
        # 异常处理：检查数组是否为 None 或空
        if nums is None or len(nums) == 0:
            return -1
        
        # 初始化左右边界
        left = 0
        right = len(nums) - 1
        
        # 循环条件：left <= right
        # 当 left > right 时，搜索范围为空，退出循环
        while left <= right:
            # 防止整数溢出的中点计算方式
            # 使用 left + (right - left) // 2 而不是 (left + right) // 2
            mid = left + (right - left) // 2
            
            # 找到目标值，直接返回索引
            if nums[mid] == target:
                return mid
            # 目标值在右半部分
            elif nums[mid] < target:
                left = mid + 1
            # 目标值在左半部分
            else:
                right = mid - 1
        
        # 未找到目标值
        return -1
    
    @staticmethod
    def search_recursive(nums, target):
        """
        递归版本的二分查找实现
        
        Args:
            nums: 升序排列的整型数组
            target: 目标值
            
        Returns:
            目标值在数组中的索引，如果不存在则返回-1
        """
        # 异常处理：检查数组是否为 None 或空
        if nums is None or len(nums) == 0:
            return -1
        
        # 调用递归辅助函数
        return LeetCode704BinarySearch._binary_search_recursive(nums, target, 0, len(nums) - 1)
    
    @staticmethod
    def _binary_search_recursive(nums, target, left, right):
        """
        递归辅助函数
        
        Args:
            nums: 升序排列的整型数组
            target: 目标值
            left: 搜索范围的左边界
            right: 搜索范围的右边界
            
        Returns:
            目标值在数组中的索引，如果不存在则返回-1
        """
        # 基本情况：搜索范围为空
        if left > right:
            return -1
        
        # 计算中点
        mid = left + (right - left) // 2
        
        # 找到目标值，直接返回索引
        if nums[mid] == target:
            return mid
        # 目标值在右半部分
        elif nums[mid] < target:
            return LeetCode704BinarySearch._binary_search_recursive(nums, target, mid + 1, right)
        # 目标值在左半部分
        else:
            return LeetCode704BinarySearch._binary_search_recursive(nums, target, left, mid - 1)


def run_tests():
    """运行测试用例"""
    # 测试用例1
    nums1 = [-1, 0, 3, 5, 9, 12]
    target1 = 9
    result1 = LeetCode704BinarySearch.search(nums1, target1)
    print("测试用例1:")
    print("数组: [-1, 0, 3, 5, 9, 12]")
    print(f"目标值: {target1}")
    print(f"结果: {result1}")
    print()
    
    # 测试用例2
    nums2 = [-1, 0, 3, 5, 9, 12]
    target2 = 2
    result2 = LeetCode704BinarySearch.search(nums2, target2)
    print("测试用例2:")
    print("数组: [-1, 0, 3, 5, 9, 12]")
    print(f"目标值: {target2}")
    print(f"结果: {result2}")
    print()
    
    # 测试用例3：单元素数组
    nums3 = [5]
    target3 = 5
    result3 = LeetCode704BinarySearch.search(nums3, target3)
    print("测试用例3:")
    print("数组: [5]")
    print(f"目标值: {target3}")
    print(f"结果: {result3}")
    print()
    
    # 测试用例4：目标值不存在
    nums4 = [2, 5]
    target4 = 0
    result4 = LeetCode704BinarySearch.search(nums4, target4)
    print("测试用例4:")
    print("数组: [2, 5]")
    print(f"目标值: {target4}")
    print(f"结果: {result4}")
    print()
    
    # 测试递归版本
    print("递归版本测试:")
    result5 = LeetCode704BinarySearch.search_recursive(nums1, target1)
    print(f"递归版本结果: {result5}")


# 主函数
if __name__ == "__main__":
    run_tests()