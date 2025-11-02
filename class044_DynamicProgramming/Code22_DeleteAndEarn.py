# 删除并获得点数 (Delete and Earn)
# 给你一个整数数组 nums ，你可以对它进行一些操作。
# 每次操作中，选择任意一个 nums[i] ，删除它并获得 nums[i] 的点数。
# 之后，你必须删除所有等于 nums[i] - 1 和 nums[i] + 1 的元素。
# 开始你拥有 0 个点数。返回你能通过这些操作获得的最大点数。
# 测试链接 : https://leetcode.cn/problems/delete-and-earn/

from typing import List
from collections import defaultdict
from functools import lru_cache
import time

class Solution:
    """
    删除并获得点数 - 打家劫舍问题的变种
    
    时间复杂度分析：
    - 预处理：O(n + k) 其中n是数组长度，k是最大值
    - 动态规划：O(k) 其中k是数组中的最大值
    - 总体：O(n + k)
    
    空间复杂度分析：
    - 计数数组：O(k)
    - dp数组：O(k) 或 O(1)（空间优化版本）
    
    工程化考量：
    1. 问题转化：将问题转化为打家劫舍问题
    2. 边界处理：空数组、单元素数组等
    3. 性能优化：空间优化版本应对大规模数据
    4. Python特性：利用装饰器简化记忆化实现
    """
    
    # 方法1：动态规划（转化为打家劫舍问题）
    # 时间复杂度：O(n + k) - n为数组长度，k为最大值
    # 空间复杂度：O(k) - 计数数组和dp数组
    # 核心思路：将问题转化为不能选择相邻数字的打家劫舍问题
    def deleteAndEarn1(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        # 找到数组中的最大值
        max_val = max(nums)
        
        # 创建计数数组，统计每个数字出现的总点数
        sum_arr = [0] * (max_val + 1)
        for num in nums:
            sum_arr[num] += num
        
        # 转化为打家劫舍问题：不能选择相邻的数字
        return self.rob_house(sum_arr)
    
    # 打家劫舍问题的解决方案
    def rob_house(self, sum_arr: List[int]) -> int:
        n = len(sum_arr)
        if n == 1:
            return sum_arr[0]
        
        dp = [0] * n
        dp[0] = sum_arr[0]
        dp[1] = max(sum_arr[0], sum_arr[1])
        
        for i in range(2, n):
            dp[i] = max(dp[i-1], dp[i-2] + sum_arr[i])
        
        return dp[n-1]

    # 方法2：空间优化的动态规划
    # 时间复杂度：O(n + k) - 与方法1相同
    # 空间复杂度：O(k) - 只使用计数数组，dp使用常数空间
    # 优化：使用滚动数组减少空间使用
    def deleteAndEarn2(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        max_val = max(nums)
        sum_arr = [0] * (max_val + 1)
        for num in nums:
            sum_arr[num] += num
        
        return self.rob_house_optimized(sum_arr)
    
    def rob_house_optimized(self, sum_arr: List[int]) -> int:
        n = len(sum_arr)
        if n == 1:
            return sum_arr[0]
        
        prev2 = sum_arr[0]  # dp[i-2]
        prev1 = max(sum_arr[0], sum_arr[1])  # dp[i-1]
        
        for i in range(2, n):
            current = max(prev1, prev2 + sum_arr[i])
            prev2, prev1 = prev1, current
        
        return prev1

    # 方法3：使用字典优化空间（当数字范围很大但实际数字很少时）
    # 时间复杂度：O(n log n) - 排序和遍历
    # 空间复杂度：O(n) - 字典存储
    # 核心思路：当数字范围很大但实际出现的数字很少时，避免创建大数组
    def deleteAndEarn3(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        # 统计每个数字的总点数
        point_dict = defaultdict(int)
        for num in nums:
            point_dict[num] += num
        
        # 如果没有数字，返回0
        if not point_dict:
            return 0
        
        # 将数字按顺序排列
        keys = sorted(point_dict.keys())
        
        # 动态规划处理
        n = len(keys)
        dp = [0] * n
        dp[0] = point_dict[keys[0]]
        
        for i in range(1, n):
            current_key = keys[i]
            current_value = point_dict[current_key]
            
            if current_key == keys[i-1] + 1:
                # 当前数字与前一个数字相邻
                if i >= 2:
                    dp[i] = max(dp[i-1], dp[i-2] + current_value)
                else:
                    dp[i] = max(dp[i-1], current_value)
            else:
                # 当前数字与前一个数字不相邻
                dp[i] = dp[i-1] + current_value
        
        return dp[n-1]

    # 方法4：记忆化搜索（使用装饰器）
    # 时间复杂度：O(n + k) - 与方法1相同
    # 空间复杂度：O(k) - 递归栈和缓存空间
    # 核心思路：递归解决，使用记忆化避免重复计算
    def deleteAndEarn4(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        max_val = max(nums)
        sum_arr = [0] * (max_val + 1)
        for num in nums:
            sum_arr[num] += num
        
        return self.dfs(tuple(sum_arr), 1)
    
    @lru_cache(maxsize=None)
    def dfs(self, sum_arr: tuple, i: int) -> int:
        if i >= len(sum_arr):
            return 0
        
        # 选择1：不取当前数字，考虑下一个
        skip = self.dfs(sum_arr, i + 1)
        # 选择2：取当前数字，跳过下一个（相邻数字）
        take = sum_arr[i] + self.dfs(sum_arr, i + 2)
        
        return max(skip, take)

def test_case(solution: Solution, nums: List[int], expected: int, description: str):
    """测试用例函数"""
    result1 = solution.deleteAndEarn1(nums)
    result2 = solution.deleteAndEarn2(nums)
    result3 = solution.deleteAndEarn3(nums)
    result4 = solution.deleteAndEarn4(nums)
    
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
    result1 = solution.deleteAndEarn1(nums)
    end = time.time()
    print(f"方法1: {result1}, 耗时: {(end - start) * 1000:.2f}ms")
    
    start = time.time()
    result2 = solution.deleteAndEarn2(nums)
    end = time.time()
    print(f"方法2: {result2}, 耗时: {(end - start) * 1000:.2f}ms")

if __name__ == "__main__":
    solution = Solution()
    
    print("=== 删除并获得点数测试 ===")
    
    # 边界测试
    test_case(solution, [], 0, "空数组")
    test_case(solution, [5], 5, "单元素数组")
    test_case(solution, [3, 3], 6, "重复元素")
    
    # LeetCode示例测试
    test_case(solution, [3, 4, 2], 6, "示例1")
    test_case(solution, [2, 2, 3, 3, 3, 4], 9, "示例2")
    test_case(solution, [1, 1, 1, 2], 3, "示例3")
    
    # 常规测试
    test_case(solution, [1, 2, 3, 4, 5], 9, "连续数字")
    test_case(solution, [5, 5, 5, 5, 5], 25, "全部相同")
    test_case(solution, [1, 3, 5, 7, 9], 25, "间隔数字")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    large_nums = [(i % 50) + 1 for i in range(1000)]  # 1-50的循环数字
    performance_test(solution, large_nums)

"""
算法总结与工程化思考：

1. 问题本质：打家劫舍问题的变种
   - 关键洞察：选择某个数字时，不能选择其相邻数字（num-1和num+1）
   - 转化思路：统计每个数字的总点数，转化为不能选择相邻数字的问题

2. 时间复杂度对比：
   - 暴力递归：O(2^n) - 不可接受
   - 记忆化搜索：O(n + k) - 可接受
   - 动态规划：O(n + k) - 推荐
   - 空间优化：O(n + k) - 工程首选

3. 空间复杂度对比：
   - 暴力递归：O(n) - 栈深度
   - 记忆化搜索：O(k) - 递归栈+缓存
   - 动态规划：O(k) - 数组存储
   - 空间优化：O(k) - 计数数组（无法避免）

4. 特殊情况处理：
   - 数字范围很大但实际数字很少：使用方法3（字典）
   - 数字范围小但重复多：使用方法1或2
   - 极端情况：全相同数字或连续数字

5. Python特性利用：
   - @lru_cache装饰器简化记忆化实现
   - defaultdict简化计数统计
   - 列表推导式简化数组操作

6. 工程选择依据：
   - 一般情况：方法2（空间优化）
   - 数字范围大但实际少：方法3（字典）
   - 需要递归思路：方法4（记忆化搜索）

7. 调试技巧：
   - 验证计数数组的正确性
   - 检查状态转移逻辑
   - 边界测试确保正确性

8. 关联题目：
   - 打家劫舍I、II（基础版本）
   - 最大子序列和问题
   - 不相邻元素最大和问题
"""