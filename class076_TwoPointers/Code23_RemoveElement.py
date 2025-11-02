import time
from typing import List

"""
LeetCode 27. 移除元素 (Remove Element)

题目描述:
给你一个数组 nums 和一个值 val，你需要原地移除所有数值等于 val 的元素，并返回移除后数组的新长度。
不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并原地修改输入数组。
元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。

示例1:
输入: nums = [3,2,2,3], val = 3
输出: 2, nums = [2,2]
解释: 函数应该返回新的长度 2, 并且 nums 中的前两个元素均为 2。
不需要考虑数组中超出新长度后面的元素。例如，函数返回的新长度为 2 ，
而 nums = [2,2,3,3] 或 nums = [2,2,0,0]，也会被视作正确答案。

示例2:
输入: nums = [0,1,2,2,3,0,4,2], val = 2
输出: 5, nums = [0,1,4,0,3]
解释: 函数应该返回新的长度 5, 并且 nums 中的前五个元素为 0, 1, 3, 0, 4。
注意这五个元素可为任意顺序。你不需要考虑数组中超出新长度后面的元素。

提示:
0 <= nums.length <= 100
0 <= nums[i] <= 50
0 <= val <= 100

题目链接: https://leetcode.com/problems/remove-element/

解题思路:
这道题与删除有序数组中的重复项类似，都可以使用双指针技术。
我们可以使用一个慢指针来跟踪应该放置下一个非val元素的位置，
然后用一个快指针来遍历整个数组。
当快指针找到一个不等于val的元素时，我们将该元素放到慢指针指向的位置，然后将慢指针向前移动一位。

时间复杂度: O(n)，其中n是数组的长度。快指针最多遍历数组一次。
空间复杂度: O(1)，只使用了常数级别的额外空间。
"""

class Solution:
    def remove_element(self, nums: List[int], val: int) -> int:
        """
        解法一: 双指针（最优解）
        
        Args:
            nums: 输入数组
            val: 要移除的元素值
            
        Returns:
            移除后数组的新长度
            
        Raises:
            TypeError: 如果输入不是列表类型
        """
        # 参数校验
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
        
        slow = 0  # 慢指针，指向下一个非val元素应该放置的位置
        
        # 快指针遍历整个数组
        for fast in range(len(nums)):
            # 如果当前元素不等于val，则将其移到慢指针位置，并将慢指针向前移动
            if nums[fast] != val:
                nums[slow] = nums[fast]
                slow += 1
        
        # 慢指针的值就是新数组的长度
        return slow
    
    def remove_element_optimized(self, nums: List[int], val: int) -> int:
        """
        解法二: 优化的双指针（当需要删除的元素很少时）
        
        当要删除的元素很少时，我们可以将不等于val的元素保留，而将等于val的元素与数组末尾的元素交换，
        然后减少数组的长度。这样可以减少不必要的赋值操作。
        
        Args:
            nums: 输入数组
            val: 要移除的元素值
            
        Returns:
            移除后数组的新长度
        """
        left = 0
        right = len(nums)
        
        while left < right:
            if nums[left] == val:
                # 将当前元素与数组末尾元素交换
                nums[left] = nums[right - 1]
                # 减少数组长度
                right -= 1
            else:
                # 当前元素不等于val，保留它
                left += 1
        
        return right
    
    def remove_element_concise(self, nums: List[int], val: int) -> int:
        """
        解法三: 简洁版双指针
        
        Args:
            nums: 输入数组
            val: 要移除的元素值
            
        Returns:
            移除后数组的新长度
        """
        index = 0
        
        for i in range(len(nums)):
            if nums[i] != val:
                nums[index] = nums[i]
                index += 1
        
        return index
    
    def removeElement(self, nums: List[int], val: int) -> int:
        """
        LeetCode官方接口的实现（使用双指针最优解）
        
        Args:
            nums: 输入数组
            val: 要移除的元素值
            
        Returns:
            移除后数组的新长度
        """
        slow = 0
        
        for fast in range(len(nums)):
            if nums[fast] != val:
                nums[slow] = nums[fast]
                slow += 1
        
        return slow

"""
打印数组的前k个元素
"""
def print_array(nums: List[int], k: int) -> None:
    print(f"[{', '.join(map(str, nums[:k]))}]")

"""
打印完整的数组
"""
def print_full_array(nums: List[int]) -> None:
    print(f"[{', '.join(map(str, nums))}]")

"""
测试函数
"""
def test():
    solution = Solution()
    
    # 测试用例1
    nums1 = [3, 2, 2, 3]
    val1 = 3
    print("测试用例1:")
    print("原始数组: ", end="")
    print_full_array(nums1)
    print(f"要移除的值: {val1}")
    length1 = solution.remove_element(nums1, val1)
    print(f"新长度: {length1}")
    print(f"新数组前{length1}个元素: ", end="")
    print_array(nums1, length1)
    print()
    
    # 测试用例2
    nums2 = [0, 1, 2, 2, 3, 0, 4, 2]
    val2 = 2
    print("测试用例2:")
    print("原始数组: ", end="")
    print_full_array(nums2)
    print(f"要移除的值: {val2}")
    length2 = solution.remove_element(nums2, val2)
    print(f"新长度: {length2}")
    print(f"新数组前{length2}个元素: ", end="")
    print_array(nums2, length2)
    print()
    
    # 测试用例3 - 边界情况：空数组
    nums3 = []
    val3 = 0
    print("测试用例3（空数组）:")
    print("原始数组: []")
    print(f"要移除的值: {val3}")
    length3 = solution.remove_element(nums3, val3)
    print(f"新长度: {length3}")
    print(f"新数组前{length3}个元素: []")
    print()
    
    # 测试用例4 - 边界情况：所有元素都等于val
    nums4 = [5, 5, 5, 5]
    val4 = 5
    print("测试用例4（全等于val的数组）:")
    print("原始数组: ", end="")
    print_full_array(nums4)
    print(f"要移除的值: {val4}")
    length4 = solution.remove_element(nums4, val4)
    print(f"新长度: {length4}")
    print(f"新数组前{length4}个元素: []")
    print()
    
    # 测试用例5 - 边界情况：没有元素等于val
    nums5 = [1, 2, 3, 4, 5]
    val5 = 6
    print("测试用例5（无等于val的元素）:")
    print("原始数组: ", end="")
    print_full_array(nums5)
    print(f"要移除的值: {val5}")
    length5 = solution.remove_element(nums5, val5)
    print(f"新长度: {length5}")
    print(f"新数组前{length5}个元素: ", end="")
    print_array(nums5, length5)
    print()

"""
性能测试
"""
def performance_test():
    solution = Solution()
    
    # 创建一个大数组进行性能测试
    size = 1000000
    # 填充数组，其中约20%的元素等于val
    val = 5
    large_array = [val if i % 10 == 0 else i % 10 for i in range(size)]
    
    # 测试解法一的性能
    array1 = large_array.copy()
    start_time = time.time()
    result1 = solution.remove_element(array1, val)
    end_time = time.time()
    print(f"解法一耗时: {(end_time - start_time) * 1000:.2f}ms, 结果长度: {result1}")
    
    # 测试解法二的性能
    array2 = large_array.copy()
    start_time = time.time()
    result2 = solution.remove_element_optimized(array2, val)
    end_time = time.time()
    print(f"解法二耗时: {(end_time - start_time) * 1000:.2f}ms, 结果长度: {result2}")
    
    # 测试解法三的性能
    array3 = large_array.copy()
    start_time = time.time()
    result3 = solution.remove_element_concise(array3, val)
    end_time = time.time()
    print(f"解法三耗时: {(end_time - start_time) * 1000:.2f}ms, 结果长度: {result3}")
    
    # 验证所有解法结果一致
    print(f"所有解法结果一致: {result1 == result2 and result2 == result3}")

"""
主函数
"""
def main():
    print("=== 测试用例 ===")
    test()
    
    print("=== 性能测试 ===")
    performance_test()

if __name__ == "__main__":
    main()