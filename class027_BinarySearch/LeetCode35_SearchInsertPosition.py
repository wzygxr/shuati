"""
LeetCode 35. 搜索插入位置 (Python版本)

题目描述：
给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。
如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
必须使用时间复杂度为 O(log n) 的算法。

示例：
输入: nums = [1,3,5,6], target = 5
输出: 2

输入: nums = [1,3,5,6], target = 2
输出: 1

输入: nums = [1,3,5,6], target = 7
输出: 4

约束条件：
- 1 <= nums.length <= 10^4
- -10^4 <= nums[i] <= 10^4
- nums 为无重复元素的升序排列数组
- -10^4 <= target <= 10^4

解题思路：
这是二分查找的一个变种。我们需要找到第一个大于等于目标值的位置。
如果找到目标值，直接返回其索引；如果没有找到，则返回应该插入的位置。

时间复杂度：O(log n)，其中 n 是数组的长度。
空间复杂度：O(1)，只使用了常数级别的额外空间。

工程化考量：
1. 边界条件处理：空数组、目标值小于所有元素、目标值大于所有元素
2. 异常输入处理：检查数组是否为 None
"""


class LeetCode35SearchInsertPosition:
    """
    LeetCode 35 搜索插入位置解决方案类
    """
    
    @staticmethod
    def search_insert(nums, target):
        """
        搜索插入位置实现
        
        Args:
            nums: 升序排列的整型数组
            target: 目标值
            
        Returns:
            目标值在数组中的索引，或应该插入的位置
        """
        # 异常处理：检查数组是否为 None
        if nums is None:
            return 0
        
        # 初始化左右边界
        left = 0
        right = len(nums) - 1
        
        # 循环条件：left <= right
        while left <= right:
            # 防止整数溢出的中点计算方式
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
        
        # 循环结束时，left 就是应该插入的位置
        return left
    
    @staticmethod
    def search_insert_alternative(nums, target):
        """
        另一种实现方式：查找第一个大于等于目标值的位置
        
        Args:
            nums: 升序排列的整型数组
            target: 目标值
            
        Returns:
            目标值在数组中的索引，或应该插入的位置
        """
        # 异常处理：检查数组是否为 None
        if nums is None:
            return 0
        
        # 初始化左右边界
        left = 0
        right = len(nums) - 1
        result = len(nums)  # 默认插入到数组末尾
        
        # 查找第一个大于等于目标值的位置
        while left <= right:
            # 防止整数溢出的中点计算方式
            mid = left + (right - left) // 2
            
            if nums[mid] >= target:
                result = mid  # 记录可能的位置
                right = mid - 1  # 继续在左半部分查找
            else:
                left = mid + 1  # 继续在右半部分查找
        
        return result


def run_tests():
    """运行测试用例"""
    # 测试用例1
    nums1 = [1, 3, 5, 6]
    target1 = 5
    result1 = LeetCode35SearchInsertPosition.search_insert(nums1, target1)
    print("测试用例1:")
    print("数组: [1, 3, 5, 6]")
    print(f"目标值: {target1}")
    print(f"结果: {result1}")
    print()
    
    # 测试用例2
    nums2 = [1, 3, 5, 6]
    target2 = 2
    result2 = LeetCode35SearchInsertPosition.search_insert(nums2, target2)
    print("测试用例2:")
    print("数组: [1, 3, 5, 6]")
    print(f"目标值: {target2}")
    print(f"结果: {result2}")
    print()
    
    # 测试用例3
    nums3 = [1, 3, 5, 6]
    target3 = 7
    result3 = LeetCode35SearchInsertPosition.search_insert(nums3, target3)
    print("测试用例3:")
    print("数组: [1, 3, 5, 6]")
    print(f"目标值: {target3}")
    print(f"结果: {result3}")
    print()
    
    # 测试用例4：目标值小于所有元素
    nums4 = [1, 3, 5, 6]
    target4 = 0
    result4 = LeetCode35SearchInsertPosition.search_insert(nums4, target4)
    print("测试用例4:")
    print("数组: [1, 3, 5, 6]")
    print(f"目标值: {target4}")
    print(f"结果: {result4}")
    print()
    
    # 测试替代方法
    print("替代方法测试:")
    result5 = LeetCode35SearchInsertPosition.search_insert_alternative(nums2, target2)
    print(f"替代方法结果: {result5}")


# 主函数
if __name__ == "__main__":
    run_tests()