from collections import deque
import sys
import time

"""
题目名称：LeetCode 1438. 绝对差不超过限制的最长连续子数组
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
题目难度：中等

题目描述：
给你一个整数数组 nums ，和一个表示限制的整数 limit，
请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit。
如果不存在满足条件的子数组，则返回 0。

解题思路：
使用滑动窗口配合双单调队列解决该问题。
1. 使用单调递增队列维护窗口内的最小值
2. 使用单调递减队列维护窗口内的最大值
3. 滑动窗口右边界不断扩展，当窗口内最大值与最小值的差超过limit时，收缩左边界
4. 记录满足条件的最长窗口长度

算法步骤：
1. 初始化双指针和双单调队列
2. 右指针不断向右扩展窗口
3. 维护两个单调队列的性质
4. 当窗口不满足条件时，收缩左边界
5. 记录最长窗口长度

时间复杂度分析：
O(n) - 每个元素最多入队出队一次

空间复杂度分析：
O(n) - 两个单调队列最多存储n个元素

是否最优解：
✅ 是，这是处理此类问题的最优解法

工程化考量：
- 使用Python内置deque提高代码可读性
- 考虑边界条件处理（空数组、单个元素等）
- 处理极端输入情况（大数组、极限值等）
"""

def longestSubarray(nums, limit):
    """
    计算绝对差不超过限制的最长连续子数组长度
    
    Args:
        nums: 输入数组
        limit: 绝对差限制
        
    Returns:
        int: 最长满足条件的子数组长度
        
    Raises:
        TypeError: 如果输入不是列表或limit不是整数
    """
    # 输入验证
    if not isinstance(nums, list):
        raise TypeError("nums must be a list")
    if not isinstance(limit, int):
        raise TypeError("limit must be an integer")
    
    # 边界条件处理
    if not nums:
        return 0
    
    # 使用双端队列维护最大值和最小值
    max_deque = deque()  # 单调递减队列，维护最大值
    min_deque = deque()  # 单调递增队列，维护最小值
    
    left = 0  # 窗口左边界
    max_length = 0  # 记录最大长度
    
    # 遍历数组，扩展窗口右边界
    for right in range(len(nums)):
        current = nums[right]
        
        # 维护单调递减队列（最大值队列）
        # 从队尾开始，移除所有小于等于当前元素的索引
        while max_deque and nums[max_deque[-1]] <= current:
            max_deque.pop()
        max_deque.append(right)
        
        # 维护单调递增队列（最小值队列）
        # 从队尾开始，移除所有大于等于当前元素的索引
        while min_deque and nums[min_deque[-1]] >= current:
            min_deque.pop()
        min_deque.append(right)
        
        # 检查当前窗口是否满足条件
        # 如果最大值与最小值的差超过limit，需要收缩左边界
        while max_deque and min_deque and \
              nums[max_deque[0]] - nums[min_deque[0]] > limit:
            # 如果左边界指向的是最小值队列的头部，需要移除
            if min_deque[0] == left:
                min_deque.popleft()
            # 如果左边界指向的是最大值队列的头部，需要移除
            if max_deque[0] == left:
                max_deque.popleft()
            left += 1  # 收缩左边界
        
        # 更新最大窗口长度
        max_length = max(max_length, right - left + 1)
    
    return max_length

def test_longest_subarray():
    """测试函数 - 包含多种边界情况和测试用例"""
    print("=== LeetCode 1438 测试用例 ===")
    
    # 测试用例1：基础示例
    nums1 = [8, 2, 4, 7]
    limit1 = 4
    result1 = longestSubarray(nums1, limit1)
    print("测试用例1 - 输入: [8,2,4,7], limit=4")
    print(f"预期输出: 2, 实际输出: {result1}")
    print(f"测试结果: {'✓ 通过' if result1 == 2 else '✗ 失败'}")
    
    # 测试用例2：包含重复元素
    nums2 = [10, 1, 2, 4, 7, 2]
    limit2 = 5
    result2 = longestSubarray(nums2, limit2)
    print("\n测试用例2 - 输入: [10,1,2,4,7,2], limit=5")
    print(f"预期输出: 4, 实际输出: {result2}")
    print(f"测试结果: {'✓ 通过' if result2 == 4 else '✗ 失败'}")
    
    # 测试用例3：limit为0的特殊情况
    nums3 = [4, 2, 2, 2, 4, 4, 2, 2]
    limit3 = 0
    result3 = longestSubarray(nums3, limit3)
    print("\n测试用例3 - 输入: [4,2,2,2,4,4,2,2], limit=0")
    print(f"预期输出: 3, 实际输出: {result3}")
    print(f"测试结果: {'✓ 通过' if result3 == 3 else '✗ 失败'}")
    
    # 测试用例4：单个元素
    nums4 = [5]
    limit4 = 10
    result4 = longestSubarray(nums4, limit4)
    print("\n测试用例4 - 输入: [5], limit=10")
    print(f"预期输出: 1, 实际输出: {result4}")
    print(f"测试结果: {'✓ 通过' if result4 == 1 else '✗ 失败'}")
    
    # 测试用例5：空数组
    nums5 = []
    limit5 = 5
    result5 = longestSubarray(nums5, limit5)
    print("\n测试用例5 - 输入: [], limit=5")
    print(f"预期输出: 0, 实际输出: {result5}")
    print(f"测试结果: {'✓ 通过' if result5 == 0 else '✗ 失败'}")
    
    # 测试用例6：递减序列
    nums6 = [5, 4, 3, 2, 1]
    limit6 = 2
    result6 = longestSubarray(nums6, limit6)
    print("\n测试用例6 - 输入: [5,4,3,2,1], limit=2")
    print(f"预期输出: 3, 实际输出: {result6}")
    print(f"测试结果: {'✓ 通过' if result6 == 3 else '✗ 失败'}")
    
    # 测试用例7：输入验证
    try:
        longestSubarray("not a list", 5)
    except TypeError as e:
        print(f"\n测试用例7 - 输入验证: ✓ 通过 - {e}")
    else:
        print("\n测试用例7 - 输入验证: ✗ 失败 - 应该抛出TypeError")
    
    print("\n=== 性能测试 ===")
    
    # 性能测试：大数组测试
    large_nums = [1] * 10000
    start_time = time.time()
    large_result = longestSubarray(large_nums, 0)
    end_time = time.time()
    print(f"大数组测试 (10000个元素): {large_result}")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f}ms")
    
    print("\n=== 算法分析 ===")
    print("时间复杂度: O(n) - 每个元素最多入队出队一次")
    print("空间复杂度: O(n) - 两个单调队列最多存储n个元素")
    print("最优解: ✅ 是")
    
    print("\n=== 语言特性差异分析 ===")
    print("Python版本特点:")
    print("1. 使用collections.deque代替数组模拟队列")
    print("2. 动态类型系统，无需声明变量类型")
    print("3. 内置异常处理机制")
    print("4. 更简洁的语法和内置函数")

if __name__ == "__main__":
    test_longest_subarray()