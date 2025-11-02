"""
除自身以外数组的乘积 (Product of Array Except Self)

题目描述:
给你一个长度为 n 的整数数组 nums，其中 n > 1，返回输出数组 output，
其中 output[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积。

示例:
输入: [1,2,3,4]
输出: [24,12,8,6]

提示:
题目数据保证数组之中任意元素的全部前缀元素和后缀（甚至是整个数组）的乘积都在 32 位整数范围内。

说明:
请不要使用除法，且在 O(n) 时间复杂度内完成此题。

进阶:
你可以在常数空间复杂度内完成这个题目吗？（出于对空间复杂度分析的目的，输出数组不被视为额外空间。）

题目链接: https://leetcode.com/problems/product-of-array-except-self/

解题思路:
1. 使用两个数组分别存储左侧所有元素的乘积和右侧所有元素的乘积
2. 对于位置i，结果为左侧乘积乘以右侧乘积
3. 进阶：使用输出数组存储左侧乘积，然后从右向左遍历计算右侧乘积并直接更新结果

时间复杂度: O(n) - 需要遍历数组两次
空间复杂度: O(1) - 不考虑输出数组，只使用常数额外空间

工程化考量:
1. 边界条件处理：空数组、单元素数组
2. 零元素处理：当数组中有0时，乘积结果的特殊处理
3. 性能优化：两次遍历完成计算，避免使用除法
4. 空间优化：使用输出数组存储中间结果

最优解分析:
这是最优解，时间复杂度O(n)，空间复杂度O(1)（不算输出数组）。
必须遍历所有元素才能计算每个位置的乘积。

数学原理:
对于位置i，结果 = (nums[0] * ... * nums[i-1]) * (nums[i+1] * ... * nums[n-1])

算法调试技巧:
1. 打印中间过程：显示前缀乘积和后缀乘积的计算过程
2. 边界测试：测试包含0的数组、负数数组等特殊情况
3. 性能测试：测试大规模数据下的性能表现

语言特性差异:
Python的整数范围较大，无需担心溢出问题。
Java/C++需要考虑整数溢出，可能需要使用long类型。
"""

class Solution:
    def productExceptSelf(self, nums):
        """
        计算除自身以外数组的乘积
        
        Args:
            nums (List[int]): 输入数组
            
        Returns:
            List[int]: 除自身以外数组的乘积
            
        异常场景处理:
        - 空数组：返回空数组
        - 单元素数组：返回[1]
        - 包含0的数组：需要特殊处理0的影响
        
        边界条件:
        - 数组长度为0或1
        - 数组包含0元素
        - 数组包含负数元素
        """
        # 边界情况处理
        if not nums or len(nums) <= 1:
            return nums
        
        n = len(nums)
        result = [0] * n
        
        # 第一遍遍历：计算每个位置左侧所有元素的乘积
        # result[i] 存储 nums[0] * nums[1] * ... * nums[i-1]
        result[0] = 1
        for i in range(1, n):
            result[i] = result[i - 1] * nums[i - 1]
            # 调试打印：左侧乘积计算
            # print(f"左侧计算 位置 {i}: result[{i}] = {result[i]}")
        
        # 第二遍遍历：从右向左，计算每个位置右侧所有元素的乘积，并与左侧乘积相乘
        # result[i] *= (nums[i+1] * nums[i+2] * ... * nums[n-1])
        right_product = 1
        for i in range(n - 1, -1, -1):
            result[i] *= right_product
            right_product *= nums[i]
            # 调试打印：右侧乘积计算
            # print(f"右侧计算 位置 {i}: result[{i}] = {result[i]}, right_product = {right_product}")
        
        return result


def test_product_except_self():
    """单元测试函数"""
    print("=== 除自身以外数组的乘积单元测试 ===")
    solution = Solution()
    
    # 测试用例1：基础情况
    nums1 = [1, 2, 3, 4]
    result1 = solution.productExceptSelf(nums1)
    print(f"测试用例1 [1,2,3,4]: {result1} (预期: [24,12,8,6])")
    
    # 测试用例2：包含0的数组
    nums2 = [-1, 1, 0, -3, 3]
    result2 = solution.productExceptSelf(nums2)
    print(f"测试用例2 [-1,1,0,-3,3]: {result2} (预期: [0,0,9,0,0])")
    
    # 测试用例3：空数组
    nums3 = []
    result3 = solution.productExceptSelf(nums3)
    print(f"测试用例3 []: {result3} (预期: [])")
    
    # 测试用例4：单元素数组
    nums4 = [5]
    result4 = solution.productExceptSelf(nums4)
    print(f"测试用例4 [5]: {result4} (预期: [1])")
    
    # 测试用例5：负数数组
    nums5 = [-2, -3, 4]
    result5 = solution.productExceptSelf(nums5)
    print(f"测试用例5 [-2,-3,4]: {result5} (预期: [-12,-8,6])")

def performance_test():
    """性能测试函数"""
    print("\n=== 性能测试 ===")
    solution = Solution()
    size = 100000  # 10万元素
    import random
    large_array = [random.randint(-30, 30) for _ in range(size)]
    
    import time
    start_time = time.time()
    result = solution.productExceptSelf(large_array)
    end_time = time.time()
    
    print(f"处理 {size} 个元素，耗时: {end_time - start_time:.4f}秒")

if __name__ == "__main__":
    # 运行单元测试
    test_product_except_self()
    
    # 运行性能测试（可选）
    # performance_test()
    
    print("\n=== 测试完成 ===")