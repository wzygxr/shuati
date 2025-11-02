import time
from typing import List

"""
LeetCode 26. 删除有序数组中的重复项 (Remove Duplicates from Sorted Array)

题目描述:
给你一个升序排列的数组 nums，请你原地删除重复出现的元素，使得每个元素只出现一次，返回删除后数组的新长度。
元素的相对顺序应该保持一致。
由于在某些语言中不能改变数组的长度，所以必须将结果放在数组nums的第一部分。
更规范地说，如果在删除重复项之后有k个元素，那么nums的前k个元素应该保存最终结果。
将最终结果插入nums的前k个位置后返回k。
不要使用额外的空间，你必须在原地修改输入数组并在O(1)额外空间的条件下完成。

示例1:
输入: nums = [1,1,2]
输出: 2, nums = [1,2,_]
解释: 函数应该返回新的长度 2 ，并且原数组 nums 的前两个元素被修改为 1, 2 。
不需要考虑数组中超出新长度后面的元素。

示例2:
输入: nums = [0,0,1,1,1,2,2,3,3,4]
输出: 5, nums = [0,1,2,3,4,_,_,_,_,_]
解释: 函数应该返回新的长度 5 ， 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4 。
不需要考虑数组中超出新长度后面的元素。

提示:
0 <= nums.length <= 3 * 10^4
-10^4 <= nums[i] <= 10^4
nums 已按升序排列

题目链接: https://leetcode.com/problems/remove-duplicates-from-sorted-array/

解题思路:
这道题是一个经典的双指针应用场景。由于数组是已排序的，所有重复的元素都会相邻。
我们可以使用快慢指针来解决这个问题：
1. 慢指针slow指向当前已处理好的不重复元素的最后一个位置
2. 快指针fast遍历整个数组
3. 当nums[fast]不等于nums[slow]时，说明找到了一个新的不重复元素，将slow向前移动一位，然后将nums[fast]赋值给nums[slow]
4. 当nums[fast]等于nums[slow]时，说明遇到了重复元素，fast继续向前移动
5. 遍历结束后，slow+1就是新数组的长度

时间复杂度: O(n)，其中n是数组的长度。快指针最多遍历数组一次。
空间复杂度: O(1)，只使用了常数级别的额外空间。
"""

class Solution:
    def remove_duplicates(self, nums: List[int]) -> int:
        """
        LeetCode官方接口的实现（使用快慢指针最优解）
        
        Args:
            nums: 升序排列的数组
            
        Returns:
            删除重复元素后的数组新长度
            
        Raises:
            TypeError: 如果输入不是列表
        """
        # 参数校验
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
        
        # 边界情况：数组长度为0或1，直接返回原长度
        n = len(nums)
        if n <= 1:
            return n
        
        slow = 0  # 慢指针，指向当前已处理好的不重复元素的最后一个位置
        
        # 快指针遍历整个数组
        for fast in range(1, n):
            # 当遇到不同的元素时
            if nums[fast] != nums[slow]:
                slow += 1  # 慢指针向前移动一位
                nums[slow] = nums[fast]  # 将不重复的元素移动到前面
            # 当遇到相同的元素时，fast继续向前移动，slow保持不变
        
        # 新数组的长度是slow + 1
        return slow + 1
    
    def remove_duplicates_optimized(self, nums: List[int]) -> int:
        """
        解法二: 快慢指针的另一种写法，包含更多优化
        
        Args:
            nums: 升序排列的数组
            
        Returns:
            删除重复元素后的数组新长度
        """
        # 边界情况：数组长度为0或1，直接返回原长度
        n = len(nums)
        if n <= 1:
            return n
        
        slow = 1  # 这里slow初始化为1，表示第一个元素已经是唯一的
        
        # 从第二个元素开始遍历
        for fast in range(1, n):
            # 当遇到不同的元素时
            if nums[fast] != nums[fast - 1]:
                nums[slow] = nums[fast]  # 将不重复的元素移动到前面
                slow += 1  # 慢指针向前移动一位
        
        return slow
    
    def remove_duplicates_generic(self, nums: List[int]) -> int:
        """
        解法三: 优化版快慢指针，适用于更通用的场景
        
        Args:
            nums: 升序排列的数组
            
        Returns:
            删除重复元素后的数组新长度
        """
        # 边界情况：数组长度为0，直接返回0
        n = len(nums)
        if n == 0:
            return 0
        
        # 初始化慢指针
        insert_pos = 1
        
        # 从第二个元素开始遍历
        for i in range(1, n):
            # 如果当前元素与上一个保留的元素不同，则保留它
            if nums[i] != nums[insert_pos - 1]:
                nums[insert_pos] = nums[i]
                insert_pos += 1
        
        return insert_pos

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
    nums1 = [1, 1, 2]
    print("测试用例1:")
    print("原始数组: ", end="")
    print_full_array(nums1)
    length1 = solution.remove_duplicates(nums1)
    print(f"新长度: {length1}")
    print(f"新数组前{length1}个元素: ", end="")
    print_array(nums1, length1)
    print()
    
    # 测试用例2
    nums2 = [0, 0, 1, 1, 1, 2, 2, 3, 3, 4]
    print("测试用例2:")
    print("原始数组: ", end="")
    print_full_array(nums2)
    length2 = solution.remove_duplicates(nums2)
    print(f"新长度: {length2}")
    print(f"新数组前{length2}个元素: ", end="")
    print_array(nums2, length2)
    print()
    
    # 测试用例3 - 边界情况：空数组
    nums3 = []
    print("测试用例3（空数组）:")
    print("原始数组: []")
    length3 = solution.remove_duplicates(nums3)
    print(f"新长度: {length3}")
    print(f"新数组前{length3}个元素: []")
    print()
    
    # 测试用例4 - 边界情况：只有一个元素的数组
    nums4 = [5]
    print("测试用例4（单元素数组）:")
    print("原始数组: ", end="")
    print_full_array(nums4)
    length4 = solution.remove_duplicates(nums4)
    print(f"新长度: {length4}")
    print(f"新数组前{length4}个元素: ", end="")
    print_array(nums4, length4)
    print()
    
    # 测试用例5 - 边界情况：没有重复元素的数组
    nums5 = [1, 2, 3, 4, 5]
    print("测试用例5（无重复元素数组）:")
    print("原始数组: ", end="")
    print_full_array(nums5)
    length5 = solution.remove_duplicates(nums5)
    print(f"新长度: {length5}")
    print(f"新数组前{length5}个元素: ", end="")
    print_array(nums5, length5)
    print()
    
    # 测试用例6 - 边界情况：所有元素都相同的数组
    nums6 = [3, 3, 3, 3, 3]
    print("测试用例6（全相同元素数组）:")
    print("原始数组: ", end="")
    print_full_array(nums6)
    length6 = solution.remove_duplicates(nums6)
    print(f"新长度: {length6}")
    print(f"新数组前{length6}个元素: ", end="")
    print_array(nums6, length6)
    print()

"""
性能测试
"""
def performance_test():
    solution = Solution()
    
    # 创建一个大数组进行性能测试
    size = 100000
    large_array = [i // 10 for i in range(size)]
    
    # 测试解法一的性能
    array1 = large_array.copy()
    start_time = time.time()
    result1 = solution.remove_duplicates(array1)
    end_time = time.time()
    print(f"解法一耗时: {(end_time - start_time) * 1000:.2f}ms, 结果长度: {result1}")
    
    # 测试解法二的性能
    array2 = large_array.copy()
    start_time = time.time()
    result2 = solution.remove_duplicates_optimized(array2)
    end_time = time.time()
    print(f"解法二耗时: {(end_time - start_time) * 1000:.2f}ms, 结果长度: {result2}")
    
    # 测试解法三的性能
    array3 = large_array.copy()
    start_time = time.time()
    result3 = solution.remove_duplicates_generic(array3)
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