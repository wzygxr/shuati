"""
二分查找算法实现 (Python版本)

核心思想：
1. 在有序数组中查找特定元素
2. 每次比较中间元素，根据比较结果缩小搜索范围
3. 时间复杂度：O(log n)，空间复杂度：O(1)

应用场景：
1. 在有序数组中查找元素
2. 查找插入位置
3. 查找边界值

工程化考量：
1. 边界条件处理（空数组、单元素数组）
2. 异常输入处理（None数组）
3. 可配置的比较策略
"""


class BinarySearch:
    """
    二分查找算法类
    """
    
    @staticmethod
    def binary_search(nums, target):
        """
        基础二分查找
        
        Args:
            nums: 有序数组
            target: 目标值
            
        Returns:
            目标值在数组中的索引，如果不存在则返回-1
            
        时间复杂度：O(log n)
        空间复杂度：O(1)
        
        示例：
        >>> BinarySearch.binary_search([1,2,3,4,5], 3)
        2
        >>> BinarySearch.binary_search([1,2,3,4,5], 6)
        -1
        """
        # 异常处理
        if nums is None or len(nums) == 0:
            return -1
        
        left = 0
        right = len(nums) - 1
        
        # 循环条件：left <= right
        while left <= right:
            # 防止整数溢出的中点计算方式
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                return mid  # 找到目标值，返回索引
            elif nums[mid] < target:
                left = mid + 1  # 目标值在右半部分
            else:
                right = mid - 1  # 目标值在左半部分
        
        return -1  # 未找到目标值
    
    @staticmethod
    def find_first(nums, target):
        """
        查找第一个等于目标值的元素
        
        Args:
            nums: 有序数组
            target: 目标值
            
        Returns:
            第一个等于目标值的元素索引，如果不存在则返回-1
            
        时间复杂度：O(log n)
        空间复杂度：O(1)
        """
        if nums is None or len(nums) == 0:
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
    def find_last(nums, target):
        """
        查找最后一个等于目标值的元素
        
        Args:
            nums: 有序数组
            target: 目标值
            
        Returns:
            最后一个等于目标值的元素索引，如果不存在则返回-1
            
        时间复杂度：O(log n)
        空间复杂度：O(1)
        """
        if nums is None or len(nums) == 0:
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
    def find_first_greater_or_equal(nums, target):
        """
        查找第一个大于等于目标值的元素
        
        Args:
            nums: 有序数组
            target: 目标值
            
        Returns:
            第一个大于等于目标值的元素索引，如果不存在则返回数组长度
            
        时间复杂度：O(log n)
        空间复杂度：O(1)
        """
        if nums is None or len(nums) == 0:
            return 0
        
        left = 0
        right = len(nums) - 1
        result = len(nums)
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] >= target:
                result = mid   # 记录可能的位置
                right = mid - 1  # 继续在左半部分查找
            else:
                left = mid + 1
        
        return result
    
    @staticmethod
    def find_last_less_or_equal(nums, target):
        """
        查找最后一个小于等于目标值的元素
        
        Args:
            nums: 有序数组
            target: 目标值
            
        Returns:
            最后一个小于等于目标值的元素索引，如果不存在则返回-1
            
        时间复杂度：O(log n)
        空间复杂度：O(1)
        """
        if nums is None or len(nums) == 0:
            return -1
        
        left = 0
        right = len(nums) - 1
        result = -1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] <= target:
                result = mid   # 记录可能的位置
                left = mid + 1  # 继续在右半部分查找
            else:
                right = mid - 1
        
        return result


# 测试函数
def test_binary_search():
    """测试二分查找算法"""
    # 测试基础二分查找
    nums1 = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    print("基础二分查找测试：")
    print(f"在数组 [1,2,3,4,5,6,7,8,9] 中查找 5: {BinarySearch.binary_search(nums1, 5)}")
    print(f"在数组 [1,2,3,4,5,6,7,8,9] 中查找 10: {BinarySearch.binary_search(nums1, 10)}")
    
    # 测试查找第一个等于目标值的元素
    nums2 = [1, 2, 2, 2, 3, 4, 5]
    print("\n查找第一个等于目标值的元素测试：")
    print(f"在数组 [1,2,2,2,3,4,5] 中查找第一个 2: {BinarySearch.find_first(nums2, 2)}")
    
    # 测试查找最后一个等于目标值的元素
    print("查找最后一个等于目标值的元素测试：")
    print(f"在数组 [1,2,2,2,3,4,5] 中查找最后一个 2: {BinarySearch.find_last(nums2, 2)}")
    
    # 测试查找第一个大于等于目标值的元素
    print("\n查找第一个大于等于目标值的元素测试：")
    print(f"在数组 [1,2,3,4,5] 中查找第一个 >= 3 的元素: {BinarySearch.find_first_greater_or_equal(nums1, 3)}")
    print(f"在数组 [1,2,3,4,5] 中查找第一个 >= 6 的元素: {BinarySearch.find_first_greater_or_equal(nums1, 6)}")
    
    # 测试查找最后一个小于等于目标值的元素
    print("\n查找最后一个小于等于目标值的元素测试：")
    print(f"在数组 [1,2,3,4,5] 中查找最后一个 <= 3 的元素: {BinarySearch.find_last_less_or_equal(nums1, 3)}")
    print(f"在数组 [1,2,3,4,5] 中查找最后一个 <= 0 的元素: {BinarySearch.find_last_less_or_equal(nums1, 0)}")


# 主函数
if __name__ == "__main__":
    test_binary_search()