from typing import List
import time

class Solution:
    """
    LeetCode 80. 删除有序数组中的重复项 II (Remove Duplicates from Sorted Array II)
    
    题目描述:
    给你一个有序数组 nums，请你原地删除重复出现的元素，使得出现次数超过两次的元素只出现两次，返回删除后数组的新长度。
    不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
    
    示例1:
    输入: nums = [1,1,1,2,2,3]
    输出: 5, nums = [1,1,2,2,3]
    解释: 函数应返回新长度 length = 5，并且原数组的前五个元素被修改为 [1,1,2,2,3]。 不需要考虑数组中超出新长度后面的元素。
    
    示例2:
    输入: nums = [0,0,1,1,1,1,2,3,3]
    输出: 7, nums = [0,0,1,1,2,3,3]
    解释: 函数应返回新长度 length = 7，并且原数组的前七个元素被修改为 [0,0,1,1,2,3,3]。 不需要考虑数组中超出新长度后面的元素。
    
    提示:
    1 <= nums.length <= 3 * 10^4
    -10^4 <= nums[i] <= 10^4
    nums 已按升序排列
    
    题目链接: https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/
    
    解题思路:
    这道题是 LeetCode 26 的升级版，允许元素最多出现两次。我们可以使用快慢指针的方法来解决：
    
    方法一（快慢指针）：
    1. 使用 slow 指针指向当前已处理好的有效部分的下一个位置
    2. 使用 count 变量记录当前元素的重复次数
    3. 遍历数组，当发现当前元素与前一个元素相同时，增加计数；否则重置计数为1
    4. 如果当前元素的计数不超过2，将其移动到 slow 指向的位置，然后 slow++
    
    方法二（优化的快慢指针）：
    由于数组是有序的，我们可以简化为直接比较 nums[fast] 和 nums[slow-2] 的值
    - 如果 nums[fast] != nums[slow-2]，说明当前元素可以保留，放到 slow 位置，然后 slow++
    - 否则，说明这个元素已经出现了两次以上，跳过
    
    最优解是方法二，时间复杂度 O(n)，空间复杂度 O(1)。
    """
    
    def remove_duplicates(self, nums: List[int]) -> int:
        """
        解法一: 快慢指针 + 计数法
        
        Args:
            nums: 输入的有序数组
            
        Returns:
            int: 删除重复元素后的新长度
            
        时间复杂度: O(n) - 只需要一次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        # 边界情况：数组长度小于等于2，所有元素都可以保留
        n = len(nums)
        if n <= 2:
            return n
        
        slow = 1  # 慢指针，指向当前已处理好的不重复元素的最后一个位置
        count = 1  # 记录当前元素重复的次数
        
        # 从第二个元素开始遍历
        for fast in range(1, n):
            # 如果当前元素与前一个元素相同，增加计数
            if nums[fast] == nums[fast - 1]:
                count += 1
            else:
                # 否则重置计数
                count = 1
            
            # 如果当前元素的计数不超过2，将其移到慢指针位置
            if count <= 2:
                slow += 1
                # 只有当fast和slow不同时才需要复制，避免不必要的操作
                if fast != slow - 1:
                    nums[slow - 1] = nums[fast]
        
        return slow
    
    def remove_duplicates_optimized(self, nums: List[int]) -> int:
        """
        解法二: 优化的快慢指针（最优解）
        
        Args:
            nums: 输入的有序数组
            
        Returns:
            int: 删除重复元素后的新长度
            
        时间复杂度: O(n) - 只需要一次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        # 边界情况：数组长度小于等于2，所有元素都可以保留
        n = len(nums)
        if n <= 2:
            return n
        
        # 慢指针初始化为2，因为前两个元素肯定可以保留
        slow = 2
        
        # 快指针从第三个元素开始遍历
        for fast in range(2, n):
            # 如果当前元素与slow-2位置的元素不同，说明这个元素可以保留
            if nums[fast] != nums[slow - 2]:
                nums[slow] = nums[fast]
                slow += 1
        
        return slow
    
    def remove_duplicates_generic(self, nums: List[int], k: int) -> int:
        """
        解法三: 通用解法，支持最多k个重复元素
        
        Args:
            nums: 输入的有序数组
            k: 允许的最大重复次数
            
        Returns:
            int: 删除重复元素后的新长度
            
        时间复杂度: O(n) - 只需要一次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        if k <= 0:
            raise ValueError("k必须为正整数")
        
        # 边界情况：数组长度小于等于k，所有元素都可以保留
        n = len(nums)
        if n <= k:
            return n
        
        # 慢指针初始化为k，因为前k个元素肯定可以保留
        slow = k
        
        # 快指针从第k+1个元素开始遍历
        for fast in range(k, n):
            # 如果当前元素与slow-k位置的元素不同，说明这个元素可以保留
            if nums[fast] != nums[slow - k]:
                nums[slow] = nums[fast]
                slow += 1
        
        return slow
    
    def validate_result(self, original: List[int], expected: List[int], expected_len: int) -> bool:
        """
        验证结果是否正确
        
        Args:
            original: 处理后的数组
            expected: 期望的结果数组
            expected_len: 期望的数组长度
            
        Returns:
            bool: 如果结果正确返回True，否则返回False
        """
        # 检查长度
        if expected_len != len(expected):
            return False
        
        # 检查前expected_len个元素
        for i in range(expected_len):
            if original[i] != expected[i]:
                return False
        
        # 检查数组是否仍然有序
        for i in range(1, expected_len):
            if original[i] < original[i - 1]:
                return False
        
        return True
    
    def test(self):
        """
        测试函数
        """
        # 测试用例1
        nums1 = [1, 1, 1, 2, 2, 3]
        expected1 = [1, 1, 2, 2, 3]
        print("测试用例1:")
        print(f"原数组: {nums1}")
        len1 = self.remove_duplicates_optimized(nums1)
        print(f"处理后: {nums1[:len1]}")
        print(f"长度: {len1}")
        print(f"验证结果: {self.validate_result(nums1, expected1, len1)}")
        print()
        
        # 测试用例2
        nums2 = [0, 0, 1, 1, 1, 1, 2, 3, 3]
        expected2 = [0, 0, 1, 1, 2, 3, 3]
        print("测试用例2:")
        print(f"原数组: {nums2}")
        len2 = self.remove_duplicates_optimized(nums2)
        print(f"处理后: {nums2[:len2]}")
        print(f"长度: {len2}")
        print(f"验证结果: {self.validate_result(nums2, expected2, len2)}")
        print()
        
        # 测试用例3 - 边界情况：数组长度小于等于2
        nums3 = [1, 1]
        print("测试用例3（数组长度为2）:")
        print(f"原数组: {nums3}")
        len3 = self.remove_duplicates_optimized(nums3)
        print(f"处理后: {nums3[:len3]}")
        print(f"长度: {len3}")
        print()
        
        # 测试用例4 - 边界情况：所有元素都相同
        nums4 = [2, 2, 2, 2, 2]
        expected4 = [2, 2]
        print("测试用例4（所有元素相同）:")
        print(f"原数组: {nums4}")
        len4 = self.remove_duplicates_optimized(nums4)
        print(f"处理后: {nums4[:len4]}")
        print(f"长度: {len4}")
        print(f"验证结果: {self.validate_result(nums4, expected4, len4)}")
        print()
        
        # 测试用例5 - 边界情况：没有重复元素
        nums5 = [1, 2, 3, 4, 5]
        print("测试用例5（无重复元素）:")
        print(f"原数组: {nums5}")
        len5 = self.remove_duplicates_optimized(nums5)
        print(f"处理后: {nums5[:len5]}")
        print(f"长度: {len5}")
        print(f"验证结果: {self.validate_result(nums5, nums5[:len5], len5)}")
        print()
    
    def performance_test(self):
        """
        性能测试
        """
        # 创建一个大数组进行性能测试
        size = 1000000
        large_array = [i // 10 for i in range(size)]  # 每个数字重复10次
        
        # 测试解法一的性能
        array1 = large_array.copy()
        start_time = time.time()
        len1 = self.remove_duplicates(array1)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法一（快慢指针+计数）耗时: {duration:.2f}ms, 处理后长度: {len1}")
        
        # 测试解法二的性能
        array2 = large_array.copy()
        start_time = time.time()
        len2 = self.remove_duplicates_optimized(array2)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法二（优化快慢指针）耗时: {duration:.2f}ms, 处理后长度: {len2}")
        
        # 测试解法三的性能
        array3 = large_array.copy()
        start_time = time.time()
        len3 = self.remove_duplicates_generic(array3, 2)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法三（通用解法）耗时: {duration:.2f}ms, 处理后长度: {len3}")
        
        # 验证所有解法结果一致
        results_consistent = (len1 == len2 and len2 == len3)
        if results_consistent:
            for i in range(len1):
                if array1[i] != array2[i] or array1[i] != array3[i]:
                    results_consistent = False
                    break
        print(f"所有解法结果一致: {results_consistent}")

# 主函数
if __name__ == "__main__":
    solution = Solution()
    
    print("=== 测试用例 ===")
    solution.test()
    
    print("=== 性能测试 ===")
    solution.performance_test()