from typing import List
import time

class Solution:
    """
    LeetCode 15. 三数之和 (3Sum)
    
    题目描述:
    给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，
    同时还满足 nums[i] + nums[j] + nums[k] == 0 。请你找出所有和为 0 且不重复的三元组。
    注意：答案中不可以包含重复的三元组。
    
    示例1:
    输入: nums = [-1,0,1,2,-1,-4]
    输出: [[-1,-1,2],[-1,0,1]]
    
    示例2:
    输入: nums = [0,1,1]
    输出: []
    
    示例3:
    输入: nums = [0,0,0]
    输出: [[0,0,0]]
    
    提示:
    3 <= nums.length <= 3000
    -10^5 <= nums[i] <= 10^5
    
    题目链接: https://leetcode.com/problems/3sum/
    
    解题思路:
    这道题可以使用排序 + 双指针的方法来解决：
    
    方法一（暴力解法）：
    使用三层嵌套循环遍历所有可能的三元组，找出和为0的三元组。使用set去重。
    时间复杂度：O(n^3)，空间复杂度：O(n)
    
    方法二（排序 + 双指针）：
    1. 先对数组进行排序
    2. 遍历数组，对于每个元素nums[i]，使用双指针left和right分别指向i+1和数组末尾
    3. 计算当前三个数的和：sum = nums[i] + nums[left] + nums[right]
    4. 如果sum == 0，将这三个数加入结果集，并移动left和right指针
       - 为了避免重复，需要跳过相同的元素
    5. 如果sum < 0，说明需要增大和，移动left指针
    6. 如果sum > 0，说明需要减小和，移动right指针
    7. 重复步骤3-6，直到left >= right
    时间复杂度：O(n^2)，空间复杂度：O(1)或O(log n)（排序的空间复杂度）
    
    最优解是方法二，时间复杂度 O(n^2)，空间复杂度 O(1)或O(log n)。
    """
    
    def three_sum_brute_force(self, nums: List[int]) -> List[List[int]]:
        """
        解法一: 暴力解法（不推荐，会超时）
        
        Args:
            nums: 输入数组
        
        Returns:
            List[List[int]]: 所有和为0且不重复的三元组
            
        Raises:
            ValueError: 如果输入数组为None或长度小于3
            
        时间复杂度: O(n^3) - 需要三层循环遍历所有可能的三元组
        空间复杂度: O(n) - 使用set存储结果进行去重
        """
        # 参数校验
        if nums is None:
            raise ValueError("输入数组不能为None")
        if len(nums) < 3:
            raise ValueError("输入数组长度必须至少为3")
        
        result_set = set()
        n = len(nums)
        
        # 三层循环遍历所有可能的三元组
        for i in range(n):
            for j in range(i + 1, n):
                for k in range(j + 1, n):
                    if nums[i] + nums[j] + nums[k] == 0:
                        # 将三元组排序后加入结果集，利用set去重
                        triplet = tuple(sorted([nums[i], nums[j], nums[k]]))
                        result_set.add(triplet)
        
        # 将set转换为列表格式
        return [list(triplet) for triplet in result_set]
    
    def three_sum(self, nums: List[int]) -> List[List[int]]:
        """
        解法二: 排序 + 双指针（最优解）
        
        Args:
            nums: 输入数组
        
        Returns:
            List[List[int]]: 所有和为0且不重复的三元组
            
        Raises:
            ValueError: 如果输入数组为None或长度小于3
            
        时间复杂度: O(n^2) - 排序O(n log n) + 两层循环O(n^2)
        空间复杂度: O(1)或O(log n) - 排序的空间复杂度
        """
        # 参数校验
        if nums is None:
            raise ValueError("输入数组不能为None")
        if len(nums) < 3:
            raise ValueError("输入数组长度必须至少为3")
        
        result = []
        n = len(nums)
        
        # 对数组进行排序
        sorted_nums = sorted(nums)
        
        # 遍历数组，固定第一个元素
        for i in range(n):
            # 跳过重复的第一个元素，避免产生重复的三元组
            if i > 0 and sorted_nums[i] == sorted_nums[i - 1]:
                continue
            
            # 如果当前元素已经大于0，由于数组已排序，后面的元素都大于0，三数之和不可能为0
            if sorted_nums[i] > 0:
                break
            
            # 初始化双指针
            left = i + 1
            right = n - 1
            
            while left < right:
                current_sum = sorted_nums[i] + sorted_nums[left] + sorted_nums[right]
                
                if current_sum == 0:
                    # 找到一个三元组
                    result.append([sorted_nums[i], sorted_nums[left], sorted_nums[right]])
                    
                    # 跳过重复的左指针元素
                    while left < right and sorted_nums[left] == sorted_nums[left + 1]:
                        left += 1
                    # 跳过重复的右指针元素
                    while left < right and sorted_nums[right] == sorted_nums[right - 1]:
                        right -= 1
                    
                    # 移动两个指针
                    left += 1
                    right -= 1
                elif current_sum < 0:
                    # 和小于0，需要增大和，移动左指针
                    left += 1
                else:
                    # 和大于0，需要减小和，移动右指针
                    right -= 1
        
        return result
    
    def three_sum_optimized(self, nums: List[int]) -> List[List[int]]:
        """
        解法三: 优化的双指针实现
        
        Args:
            nums: 输入数组
        
        Returns:
            List[List[int]]: 所有和为0且不重复的三元组
            
        Raises:
            ValueError: 如果输入数组为None或长度小于3
            
        时间复杂度: O(n^2) - 排序O(n log n) + 两层循环O(n^2)
        空间复杂度: O(1)或O(log n) - 排序的空间复杂度
        """
        # 参数校验
        if nums is None:
            raise ValueError("输入数组不能为None")
        if len(nums) < 3:
            raise ValueError("输入数组长度必须至少为3")
        
        result = []
        n = len(nums)
        
        # 对数组进行排序
        sorted_nums = sorted(nums)
        
        # 遍历数组，固定第一个元素
        for i in range(n - 2):
            # 跳过重复的第一个元素，避免产生重复的三元组
            if i > 0 and sorted_nums[i] == sorted_nums[i - 1]:
                continue
            
            # 剪枝：如果当前元素已经大于0，三数之和不可能为0
            if sorted_nums[i] > 0:
                break
            
            # 剪枝：如果当前元素和最大的两个元素之和仍小于0，跳过
            if sorted_nums[i] + sorted_nums[n - 1] + sorted_nums[n - 2] < 0:
                continue
            
            # 初始化双指针
            left = i + 1
            right = n - 1
            
            while left < right:
                current_sum = sorted_nums[i] + sorted_nums[left] + sorted_nums[right]
                
                if current_sum == 0:
                    # 找到一个三元组
                    result.append([sorted_nums[i], sorted_nums[left], sorted_nums[right]])
                    
                    # 跳过重复的左指针元素
                    while left < right and sorted_nums[left] == sorted_nums[left + 1]:
                        left += 1
                    # 跳过重复的右指针元素
                    while left < right and sorted_nums[right] == sorted_nums[right - 1]:
                        right -= 1
                    
                    # 移动两个指针
                    left += 1
                    right -= 1
                elif current_sum < 0:
                    # 和小于0，需要增大和，移动左指针
                    left += 1
                else:
                    # 和大于0，需要减小和，移动右指针
                    right -= 1
        
        return result
    
    def test(self):
        """
        测试函数
        """
        # 测试用例1
        nums1 = [-1, 0, 1, 2, -1, -4]
        expected1 = [[-1, -1, 2], [-1, 0, 1]]
        print("测试用例1:")
        print(f"输入数组: {nums1}")
        result1 = self.three_sum(nums1)
        print(f"结果: {result1}")
        print(f"期望: {expected1}")
        # 由于三元组的顺序可能不同，这里不做严格的相等性验证
        print()
        
        # 测试用例2
        nums2 = [0, 1, 1]
        expected2 = []
        print("测试用例2:")
        print(f"输入数组: {nums2}")
        result2 = self.three_sum(nums2)
        print(f"结果: {result2}")
        print(f"期望: {expected2}")
        print()
        
        # 测试用例3
        nums3 = [0, 0, 0]
        expected3 = [[0, 0, 0]]
        print("测试用例3:")
        print(f"输入数组: {nums3}")
        result3 = self.three_sum(nums3)
        print(f"结果: {result3}")
        print(f"期望: {expected3}")
        print()
        
        # 测试用例4 - 边界情况：多个重复元素
        nums4 = [-2, 0, 0, 2, 2]
        expected4 = [[-2, 0, 2]]
        print("测试用例4（多个重复元素）:")
        print(f"输入数组: {nums4}")
        result4 = self.three_sum(nums4)
        print(f"结果: {result4}")
        print(f"期望: {expected4}")
        print()
        
        # 测试用例5 - 边界情况：所有元素都为负数
        nums5 = [-1, -2, -3, -4, -5]
        expected5 = []
        print("测试用例5（全负数）:")
        print(f"输入数组: {nums5}")
        result5 = self.three_sum(nums5)
        print(f"结果: {result5}")
        print(f"期望: {expected5}")
        print()
    
    def performance_test(self):
        """
        性能测试
        """
        # 创建一个中等大小的数组进行性能测试
        size = 1000
        medium_array = [(i % 100) - 50 for i in range(size)]  # -50 到 49
        
        # 测试解法二的性能
        array2 = medium_array.copy()
        start_time = time.time()
        result2 = self.three_sum(array2)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法二（双指针）耗时: {duration:.2f}ms, 找到的三元组数量: {len(result2)}")
        
        # 测试解法三的性能
        array3 = medium_array.copy()
        start_time = time.time()
        result3 = self.three_sum_optimized(array3)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法三（优化双指针）耗时: {duration:.2f}ms, 找到的三元组数量: {len(result3)}")
        
        # 验证两种解法结果一致（这里只比较数量，不比较具体内容）
        results_consistent = (len(result2) == len(result3))
        print(f"所有解法结果数量一致: {results_consistent}")
    
    def boundary_test(self):
        """
        边界条件测试
        """
        try:
            # 测试null输入
            self.three_sum(None)
            print("边界测试失败：None输入没有抛出异常")
        except ValueError as e:
            print(f"边界测试通过：None输入正确抛出异常: {e}")
        
        try:
            # 测试长度为2的输入
            self.three_sum([1, 2])
            print("边界测试失败：长度为2的输入没有抛出异常")
        except ValueError as e:
            print(f"边界测试通过：长度为2的输入正确抛出异常: {e}")

# 主函数
if __name__ == "__main__":
    solution = Solution()
    
    print("=== 测试用例 ===")
    solution.test()
    
    print("=== 性能测试 ===")
    solution.performance_test()
    
    print("=== 边界条件测试 ===")
    solution.boundary_test()