"""
一维数组的动态和 (Running Sum of 1d Array)

题目描述:
给你一个数组 nums 。数组「动态和」的计算公式为：runningSum[i] = sum(nums[0]…nums[i]) 。
请返回 nums 的动态和。

示例:
输入：nums = [1,2,3,4]
输出：[1,3,6,10]
解释：动态和计算过程为 [1, 1+2, 1+2+3, 1+2+3+4] 。

输入：nums = [1,1,1,1,1]
输出：[1,2,3,4,5]
解释：动态和计算过程为 [1, 1+1, 1+1+1, 1+1+1+1, 1+1+1+1+1] 。

输入：nums = [3,1,2,10,1]
输出：[3,4,6,16,17]

提示:
1 <= nums.length <= 1000
-10^6 <= nums[i] <= 10^6

题目链接: https://leetcode.com/problems/running-sum-of-1d-array/

解题思路:
使用前缀和的思想，从前向后累加即可。

时间复杂度: O(n) - 需要遍历数组一次
空间复杂度: O(1) - 不考虑输出数组，只使用常数额外空间

工程化考量:
1. 边界条件处理：空数组、单元素数组
2. 原地修改：节省空间，避免创建新数组
3. 整数溢出：虽然题目保证在32位整数范围内，但实际工程中需要考虑
4. 代码可读性：清晰的变量命名和注释

最优解分析:
这是最优解，因为必须遍历所有元素才能计算前缀和，时间复杂度O(n)无法优化。
空间复杂度O(1)也是最优的（不考虑输出数组）。

算法调试技巧:
1. 打印中间过程：可以在循环中打印每个位置的前缀和
2. 边界测试：测试空数组、单元素数组等特殊情况
3. 性能测试：测试大规模数据下的性能表现

语言特性差异:
Python是动态类型语言，无需显式声明变量类型。
与Java/C++相比，Python有更简洁的语法和内置的列表操作。
Python支持负索引和切片操作，但本算法不需要这些特性。
"""

class Solution:
    def runningSum(self, nums):
        """
        计算数组的动态和
        
        Args:
            nums (List[int]): 输入数组
            
        Returns:
            List[int]: 动态和数组
            
        异常场景处理:
        - 空数组：直接返回原数组
        - 单元素数组：直接返回原数组
        - 大数组：使用原地修改避免内存浪费
        
        边界条件:
        - 数组长度为0或1
        - 数组元素包含负数
        - 数组元素包含大数（可能溢出）
        """
        # 边界情况处理：空数组或单元素数组直接返回
        if not nums or len(nums) <= 1:
            return nums
        
        # 直接在原数组上进行修改，节省空间
        # 从第二个元素开始，每个位置的值等于前一个位置的前缀和加上当前位置的原始值
        for i in range(1, len(nums)):
            # 调试打印：显示中间过程
            # print(f"位置 {i}: 前一个前缀和 = {nums[i-1]}, 当前值 = {nums[i]}")
            nums[i] += nums[i - 1]
        
        return nums

def test_running_sum():
    """单元测试函数"""
    print("=== 一维数组的动态和单元测试 ===")
    solution = Solution()
    
    # 测试用例1：正常情况
    nums1 = [1, 2, 3, 4]
    result1 = solution.runningSum(nums1.copy())
    print(f"测试用例1 [1,2,3,4]: {result1} (预期: [1, 3, 6, 10])")
    
    # 测试用例2：全1数组
    nums2 = [1, 1, 1, 1, 1]
    result2 = solution.runningSum(nums2.copy())
    print(f"测试用例2 [1,1,1,1,1]: {result2} (预期: [1, 2, 3, 4, 5])")
    
    # 测试用例3：混合数值
    nums3 = [3, 1, 2, 10, 1]
    result3 = solution.runningSum(nums3.copy())
    print(f"测试用例3 [3,1,2,10,1]: {result3} (预期: [3, 4, 6, 16, 17])")
    
    # 测试用例4：空数组
    nums4 = []
    result4 = solution.runningSum(nums4)
    print(f"测试用例4 []: {result4} (预期: [])")
    
    # 测试用例5：单元素数组
    nums5 = [5]
    result5 = solution.runningSum(nums5)
    print(f"测试用例5 [5]: {result5} (预期: [5])")
    
    # 测试用例6：包含负数
    nums6 = [-1, 2, -3, 4]
    result6 = solution.runningSum(nums6.copy())
    print(f"测试用例6 [-1,2,-3,4]: {result6} (预期: [-1, 1, -2, 2])")

def performance_test():
    """性能测试函数"""
    print("\n=== 性能测试 ===")
    solution = Solution()
    size = 1000000  # 100万元素
    large_array = list(range(size))
    
    import time
    start_time = time.time()
    solution.runningSum(large_array)
    end_time = time.time()
    
    print(f"处理 {size} 个元素耗时: {end_time - start_time:.4f}秒")

if __name__ == "__main__":
    # 运行单元测试
    test_running_sum()
    
    # 运行性能测试（可选）
    # performance_test()
    
    print("\n=== 测试完成 ===")