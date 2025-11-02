"""
绝对差不超过限制的最长连续子数组 - 双单调队列算法深度解析

【题目背景】
这是一道结合滑动窗口和双单调队列的高级算法题目，需要同时维护窗口内的最大值和最小值。
通过双单调队列技术，可以在O(n)时间内解决该问题。

【题目描述】
给你一个整数数组 nums ，和一个表示限制的整数 limit
请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit
如果不存在满足条件的子数组，则返回 0
测试链接 : https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/

【核心算法思想】
使用滑动窗口技术结合双单调队列：
1. 维护两个单调队列：一个单调递减队列（最大值队列）和一个单调递增队列（最小值队列）
2. 通过双指针控制滑动窗口的左右边界
3. 当窗口内最大值与最小值的差超过limit时，收缩窗口左边界
4. 记录满足条件的最长窗口长度

【算法复杂度分析】
- 时间复杂度：O(n) - 每个元素最多入队和出队各两次（最大值队列和最小值队列各一次）
- 空间复杂度：O(n) - 两个单调队列最多存储n个元素

【工程化考量】
1. 使用collections.deque实现高效的双端队列操作
2. 提供详细的边界检查和异常处理
3. 代码结构清晰，便于理解和维护
4. 包含单元测试和性能测试

【面试要点】
- 理解双单调队列的工作原理和维护策略
- 能够解释为什么需要同时维护最大值和最小值队列
- 分析时间复杂度的均摊分析原理
- 处理各种边界情况和特殊输入
"""

from collections import deque
from typing import List
import time
import random

class Solution:
    def longestSubarray(self, nums: List[int], limit: int) -> int:
        """
        计算绝对差不超过限制的最长连续子数组长度
        
        【算法原理深度解析】
        本算法通过双单调队列技术维护窗口内的最大值和最小值，实现高效的滑动窗口操作。
        关键设计要点：
        1. 双单调队列：一个维护最大值（单调递减），一个维护最小值（单调递增）
        2. 滑动窗口：通过双指针控制窗口范围，动态调整窗口大小
        3. 条件判断：当最大值与最小值的差超过limit时收缩窗口
        
        【时间复杂度数学证明】
        虽然算法包含嵌套循环，但通过均摊分析可知：
        - 每个元素最多入队两次（最大值队列和最小值队列各一次）
        - 每个元素最多出队两次（最大值队列和最小值队列各一次）
        - 总操作次数为O(n)，因此时间复杂度为O(n)
        
        【空间复杂度分析】
        - 两个队列最多各存储n个元素索引
        - 因此空间复杂度为O(n)
        
        :param nums: 输入整数数组
        :param limit: 绝对差限制值
        :return: 满足条件的最长连续子数组长度
        
        【测试用例覆盖】
        - 常规测试：[8,2,4,7], limit=4 → 2
        - 边界测试：单元素数组、空数组、极限值等
        - 特殊测试：全相同元素、递增序列、递减序列等
        """
        # 【边界检查】处理异常输入
        if not nums or limit < 0:
            return 0
        
        # 【数据结构初始化】使用双端队列存储索引
        max_deque = deque()  # 单调递减队列，维护窗口最大值
        min_deque = deque()  # 单调递增队列，维护窗口最小值
        
        n = len(nums)
        ans = 0
        l = 0  # 窗口左边界
        
        # 【滑动窗口主循环】遍历数组中的每个元素
        for r in range(n):
            # 【步骤1】维护最大值队列的单调递减性质
            # 从队尾开始，移除所有小于等于当前元素的索引
            while max_deque and nums[max_deque[-1]] <= nums[r]:
                max_deque.pop()
            max_deque.append(r)
            
            # 【步骤2】维护最小值队列的单调递增性质
            # 从队尾开始，移除所有大于等于当前元素的索引
            while min_deque and nums[min_deque[-1]] >= nums[r]:
                min_deque.pop()
            min_deque.append(r)
            
            # 【步骤3】窗口收缩条件判断
            # 当窗口内最大值与最小值的差超过limit时，收缩窗口左边界
            while nums[max_deque[0]] - nums[min_deque[0]] > limit:
                # 检查最大值队列的队首元素是否过期
                if max_deque[0] == l:
                    max_deque.popleft()
                # 检查最小值队列的队首元素是否过期
                if min_deque[0] == l:
                    min_deque.popleft()
                l += 1
            
            # 【步骤4】更新最长子数组长度
            ans = max(ans, r - l + 1)
        
        return ans

def run_unit_tests():
    """
    单元测试函数 - 验证算法在各种测试场景下的正确性
    """
    print("=== 绝对差不超过限制的最长连续子数组 - 单元测试 ===")
    solution = Solution()
    
    # 测试用例1：常规测试
    nums1 = [8, 2, 4, 7]
    limit1 = 4
    result1 = solution.longestSubarray(nums1, limit1)
    print(f"测试用例1 - 输入: nums={nums1}, limit={limit1}")
    print(f"期望输出: 2")
    print(f"实际输出: {result1}")
    print(f"测试结果: {'✅ 通过' if result1 == 2 else '❌ 失败'}")
    print()
    
    # 测试用例2：边界测试 - 单元素数组
    nums2 = [5]
    limit2 = 3
    result2 = solution.longestSubarray(nums2, limit2)
    print(f"测试用例2 - 单元素数组测试")
    print(f"期望输出: 1")
    print(f"实际输出: {result2}")
    print(f"测试结果: {'✅ 通过' if result2 == 1 else '❌ 失败'}")
    print()
    
    # 测试用例3：全相同元素
    nums3 = [3, 3, 3, 3, 3]
    limit3 = 0
    result3 = solution.longestSubarray(nums3, limit3)
    print(f"测试用例3 - 全相同元素测试")
    print(f"期望输出: 5")
    print(f"实际输出: {result3}")
    print(f"测试结果: {'✅ 通过' if result3 == 5 else '❌ 失败'}")
    print()
    
    # 测试用例4：递增序列
    nums4 = [1, 2, 3, 4, 5]
    limit4 = 2
    result4 = solution.longestSubarray(nums4, limit4)
    print(f"测试用例4 - 递增序列测试")
    print(f"期望输出: 3")
    print(f"实际输出: {result4}")
    print(f"测试结果: {'✅ 通过' if result4 == 3 else '❌ 失败'}")
    print()
    
    # 测试用例5：无法满足条件
    nums5 = [1, 5, 10, 15, 20]
    limit5 = 3
    result5 = solution.longestSubarray(nums5, limit5)
    print(f"测试用例5 - 无法满足条件测试")
    print(f"期望输出: 1")
    print(f"实际输出: {result5}")
    print(f"测试结果: {'✅ 通过' if result5 == 1 else '❌ 失败'}")
    print()

def run_performance_test():
    """
    性能测试函数 - 验证算法在大规模数据下的表现
    """
    print("=== 性能测试 ===")
    solution = Solution()
    
    # 测试1：中等规模数据
    size1 = 10000
    nums1 = [random.randint(0, 1000) for _ in range(size1)]
    limit1 = 100
    
    start_time = time.time()
    result1 = solution.longestSubarray(nums1, limit1)
    end_time = time.time()
    
    print(f"测试1 - 中等规模数据:")
    print(f"- 数据规模: {size1} 个元素")
    print(f"- 执行时间: {end_time - start_time:.6f} 秒")
    print(f"- 最长子数组长度: {result1}")
    print(f"- 时间复杂度验证: O(n) 算法表现良好")
    print()
    
    # 测试2：大规模数据
    size2 = 100000
    nums2 = [random.randint(0, 10000) for _ in range(size2)]
    limit2 = 500
    
    start_time = time.time()
    result2 = solution.longestSubarray(nums2, limit2)
    end_time = time.time()
    
    print(f"测试2 - 大规模数据:")
    print(f"- 数据规模: {size2} 个元素")
    print(f"- 执行时间: {end_time - start_time:.6f} 秒")
    print(f"- 最长子数组长度: {result2}")
    print(f"- 性能表现: 线性时间复杂度，适合大规模数据处理")
    print()

# 主函数
if __name__ == "__main__":
    # 运行单元测试
    run_unit_tests()
    
    # 运行性能测试
    run_performance_test()
    
    print("=== 测试完成 ===")