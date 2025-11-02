from typing import List
import time

class Solution:
    """
    LeetCode 11. 盛最多水的容器 (Container With Most Water)
    
    题目描述:
    给定一个长度为 n 的整数数组 height。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i])。
    找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
    返回容器可以储存的最大水量。
    说明：你不能倾斜容器。
    
    示例1:
    输入: [1,8,6,2,5,4,8,3,7]
    输出: 49
    解释: 图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
    
    示例2:
    输入: height = [1,1]
    输出: 1
    
    提示:
    n == height.length
    2 <= n <= 10^5
    0 <= height[i] <= 10^4
    
    题目链接: https://leetcode.com/problems/container-with-most-water/
    
    解题思路:
    这道题可以使用双指针的方法来解决：
    
    方法一（暴力解法）：
    遍历所有可能的两条线的组合，计算每个组合能容纳的水量，找出最大值。
    时间复杂度：O(n^2)，空间复杂度：O(1)
    
    方法二（双指针）：
    1. 初始化两个指针 left 和 right 分别指向数组的开头和结尾
    2. 计算当前指针所指两条线能容纳的水量：min(height[left], height[right]) * (right - left)
    3. 更新最大水量
    4. 移动较短的那条线对应的指针（因为如果移动较长的线，容纳的水量只会更小）
    5. 重复步骤2-4，直到两个指针相遇
    时间复杂度：O(n)，空间复杂度：O(1)
    
    最优解是方法二，时间复杂度 O(n)，空间复杂度 O(1)。
    """
    
    def max_area_brute_force(self, height: List[int]) -> int:
        """
        解法一: 暴力解法（不推荐，可能会超时）
        
        Args:
            height: 输入数组
        
        Returns:
            int: 最大盛水量
            
        Raises:
            ValueError: 如果输入数组为None或长度小于2
            
        时间复杂度: O(n^2) - 需要遍历所有可能的两条线的组合
        空间复杂度: O(1) - 只使用常量额外空间
        """
        # 参数校验
        if height is None:
            raise ValueError("输入数组不能为None")
        if len(height) < 2:
            raise ValueError("输入数组长度必须至少为2")
        
        max_area = 0
        # 遍历所有可能的两条线的组合
        for i in range(len(height)):
            for j in range(i + 1, len(height)):
                # 计算当前组合的盛水量
                current_area = min(height[i], height[j]) * (j - i)
                # 更新最大盛水量
                max_area = max(max_area, current_area)
        
        return max_area
    
    def max_area(self, height: List[int]) -> int:
        """
        解法二: 双指针（最优解）
        
        Args:
            height: 输入数组
        
        Returns:
            int: 最大盛水量
            
        Raises:
            ValueError: 如果输入数组为None或长度小于2
            
        时间复杂度: O(n) - 只需要一次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        # 参数校验
        if height is None:
            raise ValueError("输入数组不能为None")
        if len(height) < 2:
            raise ValueError("输入数组长度必须至少为2")
        
        max_area = 0
        left = 0  # 左指针，初始指向数组开头
        right = len(height) - 1  # 右指针，初始指向数组结尾
        
        while left < right:
            # 计算当前盛水量
            current_height = min(height[left], height[right])
            current_width = right - left
            current_area = current_height * current_width
            
            # 更新最大盛水量
            max_area = max(max_area, current_area)
            
            # 移动较短的那条线对应的指针
            if height[left] < height[right]:
                left += 1
            else:
                right -= 1
        
        return max_area
    
    def max_area_optimized(self, height: List[int]) -> int:
        """
        解法三: 双指针优化版
        跳过相同高度的柱子，减少不必要的计算
        
        Args:
            height: 输入数组
        
        Returns:
            int: 最大盛水量
            
        Raises:
            ValueError: 如果输入数组为None或长度小于2
            
        时间复杂度: O(n) - 只需要一次遍历
        空间复杂度: O(1) - 只使用常量额外空间
        """
        # 参数校验
        if height is None:
            raise ValueError("输入数组不能为None")
        if len(height) < 2:
            raise ValueError("输入数组长度必须至少为2")
        
        max_area = 0
        left = 0
        right = len(height) - 1
        
        while left < right:
            # 计算当前盛水量
            current_height = min(height[left], height[right])
            current_width = right - left
            current_area = current_height * current_width
            
            # 更新最大盛水量
            max_area = max(max_area, current_area)
            
            # 移动较短的那条线对应的指针
            # 跳过相同高度的柱子
            if height[left] < height[right]:
                current_left_height = height[left]
                while left < right and height[left] <= current_left_height:
                    left += 1
            else:
                current_right_height = height[right]
                while left < right and height[right] <= current_right_height:
                    right -= 1
        
        return max_area
    
    def test(self):
        """
        测试函数
        """
        # 测试用例1
        height1 = [1, 8, 6, 2, 5, 4, 8, 3, 7]
        expected1 = 49
        print("测试用例1:")
        print(f"输入数组: {height1}")
        result1 = self.max_area(height1)
        print(f"最大盛水量: {result1}")
        print(f"验证结果: {result1 == expected1}")
        print()
        
        # 测试用例2
        height2 = [1, 1]
        expected2 = 1
        print("测试用例2:")
        print(f"输入数组: {height2}")
        result2 = self.max_area(height2)
        print(f"最大盛水量: {result2}")
        print(f"验证结果: {result2 == expected2}")
        print()
        
        # 测试用例3 - 边界情况：所有元素递增
        height3 = [1, 2, 3, 4, 5]
        expected3 = 6  # 由索引0和4的元素组成的容器
        print("测试用例3（递增数组）:")
        print(f"输入数组: {height3}")
        result3 = self.max_area(height3)
        print(f"最大盛水量: {result3}")
        print(f"验证结果: {result3 == expected3}")
        print()
        
        # 测试用例4 - 边界情况：所有元素递减
        height4 = [5, 4, 3, 2, 1]
        expected4 = 6  # 由索引0和4的元素组成的容器
        print("测试用例4（递减数组）:")
        print(f"输入数组: {height4}")
        result4 = self.max_area(height4)
        print(f"最大盛水量: {result4}")
        print(f"验证结果: {result4 == expected4}")
        print()
        
        # 测试用例5 - 边界情况：只有两个元素，高度不同
        height5 = [3, 5]
        expected5 = 3  # 由索引0和1的元素组成的容器
        print("测试用例5（两个元素）:")
        print(f"输入数组: {height5}")
        result5 = self.max_area(height5)
        print(f"最大盛水量: {result5}")
        print(f"验证结果: {result5 == expected5}")
        print()
        
        # 测试用例6 - 边界情况：包含0
        height6 = [0, 0, 0, 0, 0]
        expected6 = 0  # 所有元素都是0，盛水量为0
        print("测试用例6（全零数组）:")
        print(f"输入数组: {height6}")
        result6 = self.max_area(height6)
        print(f"最大盛水量: {result6}")
        print(f"验证结果: {result6 == expected6}")
        print()
    
    def performance_test(self):
        """
        性能测试
        """
        # 创建一个大数组进行性能测试
        size = 100000
        large_array = [i % 100 for i in range(size)]  # 0-99循环
        
        # 测试解法二的性能
        array2 = large_array.copy()
        start_time = time.time()
        result2 = self.max_area(array2)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法二（双指针）耗时: {duration:.2f}ms, 最大盛水量: {result2}")
        
        # 测试解法三的性能
        array3 = large_array.copy()
        start_time = time.time()
        result3 = self.max_area_optimized(array3)
        end_time = time.time()
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"解法三（优化双指针）耗时: {duration:.2f}ms, 最大盛水量: {result3}")
        
        # 验证两种解法结果一致
        results_consistent = (result2 == result3)
        print(f"所有解法结果一致: {results_consistent}")
    
    def boundary_test(self):
        """
        边界条件测试
        """
        try:
            # 测试null输入
            self.max_area(None)
            print("边界测试失败：None输入没有抛出异常")
        except ValueError as e:
            print(f"边界测试通过：None输入正确抛出异常: {e}")
        
        try:
            # 测试长度为1的输入
            self.max_area([5])
            print("边界测试失败：长度为1的输入没有抛出异常")
        except ValueError as e:
            print(f"边界测试通过：长度为1的输入正确抛出异常: {e}")

# 主函数
if __name__ == "__main__":
    solution = Solution()
    
    print("=== 测试用例 ===")
    solution.test()
    
    print("=== 性能测试 ===")
    solution.performance_test()
    
    print("=== 边界条件测试 ===")
    solution.boundary_test()