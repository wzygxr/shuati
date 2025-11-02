#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
补充题目：LeetCode 1283. 使结果不超过阈值的最小除数
问题描述：给定一个数组和阈值，找出最小的除数，使得所有元素除以除数的和不超过阈值
解法：二分答案 + 贪心验证
时间复杂度：O(n * log(max))，其中n是数组长度，max是数组中的最大值
空间复杂度：O(1)
链接：https://leetcode.cn/problems/find-the-smallest-divisor-given-a-threshold/
"""
class SmallestDivisorSolution:
    def smallest_divisor(self, nums, threshold):
        """
        寻找最小的除数，使得所有元素除以除数的和不超过阈值
        
        参数:
            nums: 输入数组
            threshold: 阈值
        返回:
            最小的除数
        """
        # 确定二分搜索的范围
        left = 1  # 最小可能的除数是1
        right = max(nums)  # 最大可能的除数是数组中的最大值
        
        # 二分搜索
        result = right  # 初始化为最大值，确保有解
        while left <= right:
            mid = left + (right - left) // 2  # 使用整数除法避免浮点运算
            
            # 计算当前除数下的和
            total_sum = self._calculate_sum(nums, mid)
            
            # 判断是否满足条件
            if total_sum <= threshold:
                # 满足条件，尝试更小的除数
                result = mid
                right = mid - 1
            else:
                # 不满足条件，需要增大除数
                left = mid + 1
        
        return result
    
    def _calculate_sum(self, nums, divisor):
        """
        计算数组元素除以除数的和（向上取整）
        
        参数:
            nums: 输入数组
            divisor: 除数
        返回:
            元素除以除数的和
        """
        total_sum = 0
        for num in nums:
            # (a + b - 1) // b 是对a/b向上取整的经典写法
            total_sum += (num + divisor - 1) // divisor
        return total_sum

"""
补充题目：LeetCode 1552. 两球之间的磁力
问题描述：在给定位置放置球，使得任意两球之间的最小磁力最大
解法：二分答案 + 贪心验证
时间复杂度：O(n * log(max-min))
空间复杂度：O(log(n))
链接：https://leetcode.cn/problems/magnetic-force-between-two-balls/
"""
class MagneticForceSolution:
    def max_distance(self, position, m):
        """
        计算在给定位置放置球时的最大可能最小磁力
        
        参数:
            position: 篮子的位置数组
            m: 球的数量
        返回:
            最大可能的最小磁力
        """
        # 对位置数组进行排序
        position.sort()
        
        # 确定二分搜索的范围
        left = 1  # 最小可能的磁力是1
        right = position[-1] - position[0]  # 最大可能的磁力是最远两个位置的距离
        
        result = 0
        while left <= right:
            mid = left + (right - left) // 2
            
            # 判断是否能以mid为最小磁力放置m个球
            if self._can_place_balls(position, m, mid):
                # 可以放置，尝试更大的磁力
                result = mid
                left = mid + 1
            else:
                # 不能放置，减小磁力
                right = mid - 1
        
        return result
    
    def _can_place_balls(self, position, m, min_force):
        """
        判断是否能以min_force为最小磁力放置m个球
        
        参数:
            position: 排序后的位置数组
            m: 球的数量
            min_force: 最小磁力
        返回:
            是否可以放置
        """
        count = 1  # 第一个球放在第一个位置
        last_pos = position[0]
        
        # 贪心策略：尽可能早地放置球
        for i in range(1, len(position)):
            if position[i] - last_pos >= min_force:
                count += 1
                last_pos = position[i]
                
                # 如果已经放置了m个球，返回True
                if count == m:
                    return True
        
        # 无法放置m个球
        return False

"""
补充题目：LeetCode 287. 寻找重复数
问题描述：找出数组中重复的数（数组长度为n+1，元素值在1到n之间，且只有一个重复数）
解法：二分答案 + 抽屉原理
时间复杂度：O(n * log n)
空间复杂度：O(1)
链接：https://leetcode.cn/problems/find-the-duplicate-number/
"""
class FindDuplicateSolution:
    def find_duplicate(self, nums):
        """
        找出数组中重复的数
        
        参数:
            nums: 输入数组
        返回:
            重复的数
        """
        # 确定二分搜索的范围
        left = 1
        right = len(nums) - 1  # 数组长度为n+1，元素值在1到n之间
        
        while left < right:
            mid = left + (right - left) // 2
            
            # 计算数组中小于等于mid的元素个数
            count = self._count_less_equal(nums, mid)
            
            # 应用抽屉原理：如果count > mid，说明[1,mid]范围内有重复数
            if count > mid:
                right = mid
            else:
                left = mid + 1
        
        return left
    
    def _count_less_equal(self, nums, target):
        """
        计算数组中小于等于target的元素个数
        
        参数:
            nums: 输入数组
            target: 目标值
        返回:
            小于等于target的元素个数
        """
        count = 0
        for num in nums:
            if num <= target:
                count += 1
        return count

# 测试代码
def run_tests():
    # 测试LeetCode 1283
    print("===== 测试 LeetCode 1283 ======")
    sol1 = SmallestDivisorSolution()
    
    # 测试用例1
    nums1 = [1, 2, 5, 9]
    threshold1 = 6
    result1 = sol1.smallest_divisor(nums1, threshold1)
    print(f"测试用例1: nums = {nums1}, threshold = {threshold1}")
    print(f"结果: {result1}")  # 预期输出：5
    print(f"是否正确: {result1 == 5}")
    
    # 测试用例2
    nums2 = [44, 22, 33, 11, 1]
    threshold2 = 5
    result2 = sol1.smallest_divisor(nums2, threshold2)
    print(f"\n测试用例2: nums = {nums2}, threshold = {threshold2}")
    print(f"结果: {result2}")  # 预期输出：44
    print(f"是否正确: {result2 == 44}")
    
    # 测试LeetCode 1552
    print("\n===== 测试 LeetCode 1552 ======")
    sol2 = MagneticForceSolution()
    
    # 测试用例
    position = [1, 2, 3, 4, 7]
    m = 3
    result3 = sol2.max_distance(position, m)
    print(f"测试用例: position = {position}, m = {m}")
    print(f"结果: {result3}")  # 预期输出：3
    print(f"是否正确: {result3 == 3}")
    
    # 测试LeetCode 287
    print("\n===== 测试 LeetCode 287 ======")
    sol3 = FindDuplicateSolution()
    
    # 测试用例
    nums3 = [1, 3, 4, 2, 2]
    result4 = sol3.find_duplicate(nums3)
    print(f"测试用例: nums = {nums3}")
    print(f"结果: {result4}")  # 预期输出：2
    print(f"是否正确: {result4 == 2}")

if __name__ == "__main__":
    run_tests()

"""
Python特有的实现细节和优化：

1. 整数除法：Python 3中使用 // 进行整数除法，而不是 /

2. 列表操作：Python的列表排序使用sort()方法，非常方便

3. 函数命名：使用下划线分隔的命名风格，符合Python的PEP 8规范

4. 私有方法：使用下划线前缀（如_calculate_sum）表示私有方法

5. 文档字符串：使用三引号编写详细的函数文档，提高代码可读性

6. 测试框架：实现了独立的测试函数，可以直接运行验证结果

工程化考量：

1. 异常处理：在实际应用中，可以添加输入验证，例如检查数组是否为空

2. 性能优化：对于大规模数据，可以考虑使用更高效的数据结构或算法

3. 代码复用：可以将二分答案的核心逻辑抽象出来，形成通用的二分查找函数

4. 可读性：使用清晰的变量名和详细的注释，便于维护

5. 测试覆盖：添加更多的边界测试用例，确保代码的鲁棒性
"""