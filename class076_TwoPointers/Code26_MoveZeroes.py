from typing import List
import time

class Solution:
    """
    LeetCode 283. 移动零 (Move Zeroes)
    
    题目描述:
    给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
    请注意 ，必须在不复制数组的情况下原地对数组进行操作。
    
    示例1:
    输入: nums = [0,1,0,3,12]
    输出: [1,3,12,0,0]
    
    示例2:
    输入: nums = [0]
    输出: [0]
    
    提示:
    1 <= nums.length <= 10^4
    -2^31 <= nums[i] <= 2^31 - 1
    
    题目链接: https://leetcode.com/problems/move-zeroes/
    
    解题思路:
    这道题可以使用双指针的方法来解决：
    
    方法一（双指针）：
    1. 使用两个指针 slow 和 fast，初始都指向0
    2. fast指针用于遍历整个数组，当遇到非零元素时，将其移动到 slow 指向的位置，然后 slow++
    3. 遍历结束后，将 slow 到数组末尾的所有元素都设为0
    
    方法二（优化的双指针）：
    1. 同样使用两个指针 slow 和 fast，初始都指向0
    2. 当 fast 指向非零元素时，如果 slow != fast，交换 nums[slow] 和 nums[fast]，然后 slow++
    3. 这种方法避免了不必要的赋值操作，只在需要时进行交换
    
    最优解是方法二，时间复杂度 O(n)，空间复杂度 O(1)。
    """
    
    def move_zeroes(self, nums: List[int]) -> None:
        """
        解法一: 双指针基础版
        
        Args:
            nums: 输入数组
        
        Returns:
            None: 原地修改数组
        
        时间复杂度: O(n) - 只需要一次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        # 参数校验
        if nums is None:
            raise ValueError("输入数组不能为None")
        
        # 慢指针，指向当前应该放置非零元素的位置
        slow = 0
        
        # 快指针遍历整个数组
        for fast in range(len(nums)):
            # 如果当前元素不为0，将其移到慢指针位置
            if nums[fast] != 0:
                nums[slow] = nums[fast]
                slow += 1
        
        # 将slow之后的所有元素都设为0
        for i in range(slow, len(nums)):
            nums[i] = 0
    
    def move_zeroes_optimized(self, nums: List[int]) -> None:
        """
        解法二: 优化的双指针（最优解）
        
        Args:
            nums: 输入数组
        
        Returns:
            None: 原地修改数组
        
        时间复杂度: O(n) - 只需要一次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        # 参数校验
        if nums is None:
            raise ValueError("输入数组不能为None")
        
        # 慢指针，指向当前应该放置非零元素的位置
        slow = 0
        
        # 快指针遍历整个数组
        for fast in range(len(nums)):
            # 如果当前元素不为0，且slow和fast不同，交换它们
            if nums[fast] != 0:
                if slow != fast:
                    nums[slow], nums[fast] = nums[fast], nums[slow]
                slow += 1
    
    def move_zeroes_one_pass(self, nums: List[int]) -> None:
        """
        解法三: 一次遍历的另一种实现
        
        Args:
            nums: 输入数组
        
        Returns:
            None: 原地修改数组
        
        时间复杂度: O(n) - 只需要一次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        # 参数校验
        if nums is None:
            raise ValueError("输入数组不能为None")
        
        last_non_zero_found_at = 0
        
        # 将所有非零元素移到数组前面
        for i in range(len(nums)):
            if nums[i] != 0:
                nums[last_non_zero_found_at] = nums[i]
                last_non_zero_found_at += 1
        
        # 填充剩余位置为0
        for i in range(last_non_zero_found_at, len(nums)):
            nums[i] = 0
    
    def validate_result(self, original: List[int], expected: List[int]) -> bool:
        """
        验证结果是否正确
        
        Args:
            original: 处理后的数组
            expected: 期望的结果数组
            
        Returns:
            bool: 如果结果正确返回True，否则返回False
        """
        if len(original) != len(expected):
            return False
        
        # 验证所有元素相等
        for i in range(len(original)):
            if original[i] != expected[i]:
                return False
        
        # 验证0都在末尾
        zero_started = False
        for num in original:
            if zero_started and num != 0:
                return False  # 发现0后面有非零元素
            if num == 0:
                zero_started = True
        
        return True
    
    def test(self):
        """
        测试函数
        """
        # 测试用例1
        nums1 = [0, 1, 0, 3, 12]
        expected1 = [1, 3, 12, 0, 0]
        print("测试用例1:")
        print(f"原数组: {nums1}")
        self.move_zeroes_optimized(nums1)
        print(f"处理后: {nums1}")
        print(f"验证结果: {self.validate_result(nums1, expected1)}")
        print()
        
        # 测试用例2
        nums2 = [0]
        expected2 = [0]
        print("测试用例2:")
        print(f"原数组: {nums2}")
        self.move_zeroes_optimized(nums2)
        print(f"处理后: {nums2}")
        print(f"验证结果: {self.validate_result(nums2, expected2)}")
        print()
        
        # 测试用例3 - 边界情况：没有零元素
        nums3 = [1, 2, 3, 4, 5]
        expected3 = [1, 2, 3, 4, 5]
        print("测试用例3（无零元素）:")
        print(f"原数组: {nums3}")
        self.move_zeroes_optimized(nums3)
        print(f"处理后: {nums3}")
        print(f"验证结果: {self.validate_result(nums3, expected3)}")
        print()
        
        # 测试用例4 - 边界情况：所有元素都是零
        nums4 = [0, 0, 0, 0, 0]
        expected4 = [0, 0, 0, 0, 0]
        print("测试用例4（全零元素）:")
        print(f"原数组: {nums4}")
        self.move_zeroes_optimized(nums4)
        print(f"处理后: {nums4}")
        print(f"验证结果: {self.validate_result(nums4, expected4)}")
        print()
        
        # 测试用例5 - 边界情况：零元素在开头
        nums5 = [0, 0, 1, 2, 3]
        expected5 = [1, 2, 3, 0, 0]
        print("测试用例5（零在开头）:")
        print(f"原数组: {nums5}")
        self.move_zeroes_optimized(nums5)
        print(f"处理后: {nums5}")
        print(f"验证结果: {self.validate_result(nums5, expected5)}")
        print()
        
        # 测试用例6 - 边界情况：零元素在末尾
        nums6 = [1, 2, 3, 0, 0]
        expected6 = [1, 2, 3, 0, 0]
        print("测试用例6（零在末尾）:")
        print(f"原数组: {nums6}")
        self.move_zeroes_optimized(nums6)
        print(f"处理后: {nums6}")
        print(f"验证结果: {self.validate_result(nums6, expected6)}")
        print()
    
    def performance_test(self):
        """
        性能测试
        """
        # 创建一个大数组进行性能测试
        size = 1000000
        large_array = [i % 2 for i in range(size)]  # 交替放置0和1
        
        # 测试解法一的性能
        array1 = large_array.copy()
        start_time = time.time()
        self.move_zeroes(array1)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法一（基础双指针）耗时: {duration:.2f}ms")
        
        # 测试解法二的性能
        array2 = large_array.copy()
        start_time = time.time()
        self.move_zeroes_optimized(array2)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法二（优化双指针）耗时: {duration:.2f}ms")
        
        # 测试解法三的性能
        array3 = large_array.copy()
        start_time = time.time()
        self.move_zeroes_one_pass(array3)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法三（一次遍历）耗时: {duration:.2f}ms")
        
        # 验证所有解法结果一致
        results_consistent = True
        for i in range(size):
            if array1[i] != array2[i] or array1[i] != array3[i]:
                results_consistent = False
                break
        print(f"所有解法结果一致: {results_consistent}")
        
        # 验证结果正确性
        is_correct = True
        zero_started = False
        for num in array1:
            if zero_started and num != 0:
                is_correct = False
                break
            if num == 0:
                zero_started = True
        print(f"结果正确: {is_correct}")

# 主函数
if __name__ == "__main__":
    solution = Solution()
    
    print("=== 测试用例 ===")
    solution.test()
    
    print("=== 性能测试 ===")
    solution.performance_test()