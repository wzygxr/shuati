from typing import List
import time

class Solution:
    """
    LeetCode 75. 颜色分类 (Sort Colors)
    
    题目描述:
    给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
    我们使用整数 0、1 和 2 分别表示红色、白色和蓝色。
    必须在不使用库内置的 sort 函数的情况下解决这个问题。
    
    示例1:
    输入: nums = [2,0,2,1,1,0]
    输出: [0,0,1,1,2,2]
    
    示例2:
    输入: nums = [2,0,1]
    输出: [0,1,2]
    
    提示:
    n == nums.length
    1 <= n <= 300
    nums[i] 为 0、1 或 2
    
    题目链接: https://leetcode.com/problems/sort-colors/
    
    解题思路:
    这道题是一个经典的「荷兰国旗问题」，可以使用双指针或者三指针技术来解决。
    
    方法一（三指针法）：
    1. 使用三个指针：left（指向0的右边界）、mid（当前处理的元素）、right（指向2的左边界）
    2. 初始化left=0，mid=0，right=nums.length-1
    3. 当mid<=right时，根据nums[mid]的值进行不同的处理：
       - 如果nums[mid]==0，交换nums[left]和nums[mid]，然后left++，mid++
       - 如果nums[mid]==1，mid++
       - 如果nums[mid]==2，交换nums[mid]和nums[right]，然后right--（注意此时mid不增加，因为交换后的元素还未处理）
    
    方法二（两次遍历）：
    1. 第一次遍历统计0、1、2的个数
    2. 第二次遍历根据统计结果填充数组
    
    最优解是三指针法，时间复杂度O(n)，空间复杂度O(1)，且只需要一次遍历。
    """
    
    def sort_colors(self, nums: List[int]) -> None:
        """
        解法一: 三指针法（最优解）
        
        Args:
            nums: 输入数组，只包含0、1、2三种元素
        
        Returns:
            None: 原地修改数组
        
        时间复杂度: O(n) - 只需要一次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        if nums is None:
            raise ValueError("输入数组不能为None")
        
        left = 0      # 0的右边界
        mid = 0       # 当前处理的元素
        right = len(nums) - 1  # 2的左边界
        
        while mid <= right:
            if nums[mid] == 0:
                # 当前元素是0，放到left指针位置
                nums[left], nums[mid] = nums[mid], nums[left]
                left += 1
                mid += 1
            elif nums[mid] == 1:
                # 当前元素是1，已经在正确位置
                mid += 1
            elif nums[mid] == 2:
                # 当前元素是2，放到right指针位置
                nums[mid], nums[right] = nums[right], nums[mid]
                right -= 1
                # 注意：此时mid不增加，因为交换后的元素还未处理
            else:
                # 非法输入检查
                raise ValueError("输入数组包含非法元素，只能包含0、1、2")
    
    def sort_colors_two_pass(self, nums: List[int]) -> None:
        """
        解法二: 两次遍历（计数排序思想）
        
        Args:
            nums: 输入数组，只包含0、1、2三种元素
        
        Returns:
            None: 原地修改数组
        
        时间复杂度: O(n) - 需要两次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        if nums is None:
            raise ValueError("输入数组不能为None")
        
        count0, count1, count2 = 0, 0, 0
        
        # 第一次遍历：统计0、1、2的个数
        for num in nums:
            if num == 0:
                count0 += 1
            elif num == 1:
                count1 += 1
            elif num == 2:
                count2 += 1
            else:
                raise ValueError("输入数组包含非法元素，只能包含0、1、2")
        
        # 第二次遍历：根据统计结果填充数组
        index = 0
        while count0 > 0:
            nums[index] = 0
            index += 1
            count0 -= 1
        while count1 > 0:
            nums[index] = 1
            index += 1
            count1 -= 1
        while count2 > 0:
            nums[index] = 2
            index += 1
            count2 -= 1
    
    def sort_colors_two_pointers(self, nums: List[int]) -> None:
        """
        解法三: 双指针优化版
        
        Args:
            nums: 输入数组，只包含0、1、2三种元素
        
        Returns:
            None: 原地修改数组
        
        时间复杂度: O(n) - 需要两次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        if nums is None:
            raise ValueError("输入数组不能为None")
        
        # 先将所有的0移到数组前面
        p0 = 0
        for i in range(len(nums)):
            if nums[i] == 0:
                nums[i], nums[p0] = nums[p0], nums[i]
                p0 += 1
        
        # 再将所有的1移到0之后
        p1 = p0
        for i in range(p0, len(nums)):
            if nums[i] == 1:
                nums[i], nums[p1] = nums[p1], nums[i]
                p1 += 1
    
    def is_sorted(self, nums: List[int]) -> bool:
        """
        验证排序结果是否正确
        
        Args:
            nums: 要验证的数组
        
        Returns:
            bool: 如果数组已排序返回True，否则返回False
        """
        for i in range(1, len(nums)):
            if nums[i] < nums[i-1]:
                return False
        return True
    
    def test(self):
        """
        测试函数
        """
        # 测试用例1
        nums1 = [2, 0, 2, 1, 1, 0]
        print("测试用例1:")
        print(f"排序前: {nums1}")
        self.sort_colors(nums1)
        print(f"排序后: {nums1}")
        print()
        
        # 测试用例2
        nums2 = [2, 0, 1]
        print("测试用例2:")
        print(f"排序前: {nums2}")
        self.sort_colors(nums2)
        print(f"排序后: {nums2}")
        print()
        
        # 测试用例3 - 边界情况：只有一个元素
        nums3 = [0]
        print("测试用例3（单元素数组）:")
        print(f"排序前: {nums3}")
        self.sort_colors(nums3)
        print(f"排序后: {nums3}")
        print()
        
        # 测试用例4 - 边界情况：已经排序的数组
        nums4 = [0, 0, 1, 1, 2, 2]
        print("测试用例4（已排序数组）:")
        print(f"排序前: {nums4}")
        self.sort_colors(nums4)
        print(f"排序后: {nums4}")
        print()
        
        # 测试用例5 - 边界情况：逆序排列的数组
        nums5 = [2, 2, 1, 1, 0, 0]
        print("测试用例5（逆序数组）:")
        print(f"排序前: {nums5}")
        self.sort_colors(nums5)
        print(f"排序后: {nums5}")
        print()
        
        # 测试用例6 - 边界情况：所有元素都相同
        nums6 = [1, 1, 1, 1]
        print("测试用例6（全相同元素）:")
        print(f"排序前: {nums6}")
        self.sort_colors(nums6)
        print(f"排序后: {nums6}")
        print()
    
    def performance_test(self):
        """
        性能测试
        """
        # 创建一个大数组进行性能测试
        size = 1000000
        large_array = [i % 3 for i in range(size)]
        
        # 测试解法一的性能
        array1 = large_array.copy()
        start_time = time.time()
        self.sort_colors(array1)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法一（三指针）耗时: {duration:.2f}ms")
        
        # 测试解法二的性能
        array2 = large_array.copy()
        start_time = time.time()
        self.sort_colors_two_pass(array2)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法二（两次遍历）耗时: {duration:.2f}ms")
        
        # 测试解法三的性能
        array3 = large_array.copy()
        start_time = time.time()
        self.sort_colors_two_pointers(array3)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法三（双指针优化）耗时: {duration:.2f}ms")
        
        # 验证所有解法结果一致且已排序
        results_consistent = True
        for i in range(size):
            if array1[i] != array2[i] or array1[i] != array3[i]:
                results_consistent = False
                break
        print(f"所有解法结果一致: {results_consistent}")
        print(f"排序正确: {self.is_sorted(array1)}")

# 主函数
if __name__ == "__main__":
    solution = Solution()
    
    print("=== 测试用例 ===")
    solution.test()
    
    print("=== 性能测试 ===")
    solution.performance_test()