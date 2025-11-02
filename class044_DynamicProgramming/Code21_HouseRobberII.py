# 打家劫舍II (House Robber II)
# 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。
# 这个地方所有的房屋都围成一圈，这意味着第一个房屋和最后一个房屋是紧挨着的。
# 同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
# 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，能够偷窃到的最高金额。
# 测试链接 : https://leetcode.cn/problems/house-robber-ii/

from typing import List
from functools import lru_cache
import time

class Solution:
    """
    打家劫舍II - 环形数组的动态规划问题
    
    时间复杂度分析：
    - 暴力递归：O(2^n) 指数级，存在大量重复计算
    - 记忆化搜索：O(n) 每个状态只计算一次
    - 动态规划：O(n) 线性时间复杂度
    - 空间优化：O(1) 只保存必要的前两个状态
    
    空间复杂度分析：
    - 暴力递归：O(n) 递归调用栈深度
    - 记忆化搜索：O(n) 递归栈 + 记忆化缓存
    - 动态规划：O(n) dp数组存储所有状态
    - 空间优化：O(1) 工程首选
    
    工程化考量：
    1. 环形数组处理：分解为两个线性问题
    2. 边界处理：空数组、单元素数组等
    3. 性能优化：空间优化版本应对大规模数据
    4. Python特性：利用装饰器简化记忆化实现
    """
    
    # 方法1：分解为两个线性问题
    # 时间复杂度：O(n) - 解决两个线性问题
    # 空间复杂度：O(n) - 使用辅助数组
    # 核心思路：环形问题分解为[0, n-2]和[1, n-1]两个线性问题
    def rob1(self, nums: List[int]) -> int:
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        
        n = len(nums)
        # 情况1：不偷最后一间房（偷第一间房）
        max1 = self.rob_linear(nums[0:n-1])
        # 情况2：不偷第一间房（偷最后一间房）
        max2 = self.rob_linear(nums[1:n])
        
        return max(max1, max2)
    
    # 线性打家劫舍问题的解决方案（打家劫舍I）
    def rob_linear(self, nums: List[int]) -> int:
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        
        n = len(nums)
        dp = [0] * n
        dp[0] = nums[0]
        dp[1] = max(nums[0], nums[1])
        
        for i in range(2, n):
            dp[i] = max(dp[i-1], dp[i-2] + nums[i])
        
        return dp[n-1]

    # 方法2：空间优化的分解方案
    # 时间复杂度：O(n) - 解决两个线性问题
    # 空间复杂度：O(1) - 只使用常数空间
    # 优化：避免创建新数组，直接在原数组上操作
    def rob2(self, nums: List[int]) -> int:
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        
        n = len(nums)
        # 情况1：不偷最后一间房
        max1 = self.rob_linear_optimized(nums, 0, n-2)
        # 情况2：不偷第一间房
        max2 = self.rob_linear_optimized(nums, 1, n-1)
        
        return max(max1, max2)
    
    # 空间优化的线性打家劫舍
    def rob_linear_optimized(self, nums: List[int], start: int, end: int) -> int:
        if start > end:
            return 0
        if start == end:
            return nums[start]
        
        prev2 = nums[start]  # dp[i-2]
        prev1 = max(nums[start], nums[start+1])  # dp[i-1]
        
        for i in range(start+2, end+1):
            current = max(prev1, prev2 + nums[i])
            prev2, prev1 = prev1, current
        
        return prev1

    # 方法3：动态规划（统一处理）
    # 时间复杂度：O(n) - 遍历数组两次
    # 空间复杂度：O(n) - 使用dp数组
    # 核心思路：分别计算包含第一个元素和不包含第一个元素的情况
    def rob3(self, nums: List[int]) -> int:
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        
        n = len(nums)
        # dp1: 不偷第一间房的情况
        dp1 = [0] * n
        # dp2: 偷第一间房的情况（不能偷最后一间房）
        dp2 = [0] * n
        
        # 初始化dp1（不偷第一间房）
        dp1[0] = 0
        dp1[1] = nums[1]
        for i in range(2, n):
            dp1[i] = max(dp1[i-1], dp1[i-2] + nums[i])
        
        # 初始化dp2（偷第一间房，不能偷最后一间房）
        dp2[0] = nums[0]
        dp2[1] = max(nums[0], nums[1])
        for i in range(2, n-1):
            dp2[i] = max(dp2[i-1], dp2[i-2] + nums[i])
        
        return max(dp1[n-1], dp2[n-2])

    # 方法4：记忆化搜索（使用装饰器）
    # 时间复杂度：O(n) - 每个状态只计算一次
    # 空间复杂度：O(n) - 递归栈和缓存空间
    # 核心思路：递归解决两个子问题，使用记忆化避免重复计算
    def rob4(self, nums: List[int]) -> int:
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        
        n = len(nums)
        # 情况1：不偷最后一间房
        max1 = self.dfs(tuple(nums), 0, n-2)
        # 情况2：不偷第一间房
        max2 = self.dfs(tuple(nums), 1, n-1)
        
        return max(max1, max2)
    
    @lru_cache(maxsize=None)
    def dfs(self, nums: tuple, start: int, end: int) -> int:
        if start > end:
            return 0
        if start == end:
            return nums[start]
        if start + 1 == end:
            return max(nums[start], nums[end])
        
        # 选择1：偷当前房屋，跳过下一个
        rob_current = nums[start] + self.dfs(nums, start+2, end)
        # 选择2：不偷当前房屋，考虑下一个
        skip_current = self.dfs(nums, start+1, end)
        
        return max(rob_current, skip_current)

    # 方法5：暴力递归（用于对比）
    # 时间复杂度：O(2^n) - 指数级，效率极低
    # 空间复杂度：O(n) - 递归调用栈深度
    # 问题：存在大量重复计算，仅用于教学目的
    def rob5(self, nums: List[int]) -> int:
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        
        n = len(nums)
        # 分解为两个线性问题
        max1 = self.rob_linear_brute_force(nums[0:n-1])
        max2 = self.rob_linear_brute_force(nums[1:n])
        
        return max(max1, max2)
    
    def rob_linear_brute_force(self, nums: List[int]) -> int:
        return self.dfs_brute_force(nums, 0)
    
    def dfs_brute_force(self, nums: List[int], index: int) -> int:
        if index >= len(nums):
            return 0
        
        # 选择1：偷当前房屋，跳过下一个
        rob_current = nums[index] + self.dfs_brute_force(nums, index+2)
        # 选择2：不偷当前房屋，考虑下一个
        skip_current = self.dfs_brute_force(nums, index+1)
        
        return max(rob_current, skip_current)

def test_case(solution: Solution, nums: List[int], expected: int, description: str):
    """测试用例函数"""
    result1 = solution.rob1(nums)
    result2 = solution.rob2(nums)
    result3 = solution.rob3(nums)
    result4 = solution.rob4(nums)
    
    all_correct = (result1 == expected and result2 == expected and 
                  result3 == expected and result4 == expected)
    
    status = "✓" if all_correct else "✗"
    print(f"{description}: {status}")
    
    if not all_correct:
        print(f"  方法1: {result1} | 方法2: {result2} | 方法3: {result3} | "
              f"方法4: {result4} | 预期: {expected}")

def performance_test(solution: Solution, nums: List[int]):
    """性能测试函数"""
    print(f"\n性能测试 n={len(nums)}:")
    
    start = time.time()
    result2 = solution.rob2(nums)
    end = time.time()
    print(f"空间优化方法: {result2}, 耗时: {(end - start) * 1000:.2f}ms")
    
    start = time.time()
    result3 = solution.rob3(nums)
    end = time.time()
    print(f"统一处理方法: {result3}, 耗时: {(end - start) * 1000:.2f}ms")
    
    # 暴力方法太慢，不测试
    print("暴力方法在n=100时太慢，跳过测试")

if __name__ == "__main__":
    solution = Solution()
    
    print("=== 打家劫舍II测试 ===")
    
    # 边界测试
    test_case(solution, [], 0, "空数组")
    test_case(solution, [5], 5, "单元素数组")
    test_case(solution, [2, 3], 3, "双元素数组")
    
    # LeetCode示例测试
    test_case(solution, [2, 3, 2], 3, "示例1")
    test_case(solution, [1, 2, 3, 1], 4, "示例2")
    test_case(solution, [1, 2, 3], 3, "示例3")
    
    # 常规测试
    test_case(solution, [1, 2, 3, 4, 5], 8, "递增金额")
    test_case(solution, [5, 4, 3, 2, 1], 8, "递减金额")
    test_case(solution, [2, 7, 9, 3, 1], 11, "混合金额")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    large_nums = [i % 10 + 1 for i in range(100)]  # 1-10的循环金额
    performance_test(solution, large_nums)

"""
算法总结与工程化思考：

1. 问题本质：环形数组的最大不相邻子序列和问题
   - 关键洞察：环形问题可以分解为两个线性问题
   - 情况1：不偷最后一间房（可以偷第一间房）
   - 情况2：不偷第一间房（可以偷最后一间房）

2. 时间复杂度对比：
   - 暴力递归：O(2^n) - 不可接受
   - 记忆化搜索：O(n) - 可接受
   - 动态规划：O(n) - 推荐
   - 空间优化：O(n) - 工程首选

3. 空间复杂度对比：
   - 暴力递归：O(n) - 栈深度
   - 记忆化搜索：O(n) - 递归栈+缓存
   - 动态规划：O(n) - 数组存储
   - 空间优化：O(1) - 最优

4. Python特性利用：
   - @lru_cache装饰器简化记忆化实现
   - 多重赋值语法简化变量交换
   - 列表切片简化数组操作

5. 工程选择依据：
   - 面试笔试：方法2（空间优化分解）
   - 大规模数据：方法2或方法3
   - 代码清晰：方法1（分解思路明确）

6. 调试技巧：
   - 分别验证两个子问题的正确性
   - 边界测试确保环形处理正确
   - 性能测试选择最优算法

7. 关联题目：
   - 打家劫舍I（线性数组版本）
   - 打家劫舍III（树形结构版本）
   - 最大子序列和问题

8. 环形问题通用解法：
   - 分解为多个线性子问题
   - 分别求解后取最优解
   - 适用于环形房屋、环形道路等问题
"""