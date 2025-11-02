from collections import deque
import sys

"""
题目名称：LeetCode 1696. 跳跃游戏 VI
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/jump-game-vi/
题目难度：中等

题目描述：
给你一个下标从 0 开始的整数数组 nums 和一个整数 k。
一开始你在下标 0 处。每一步，你最多可以往前跳 k 步，但你不能跳出数组的边界。
也就是说，你可以从下标 i 跳到 [i+1, min(n-1, i+k)] 包含两个端点的任意位置。
你的目标是到达数组最后一个位置（下标为 n-1 处），你得到的分数为经过的所有数字之和。
请你返回你能得到的 最大分数。

解题思路：
使用动态规划 + 单调队列优化
1. dp[i] 表示到达位置 i 时能获得的最大分数
2. 状态转移：dp[i] = nums[i] + max(dp[j])，其中 j ∈ [max(0, i-k), i-1]
3. 使用单调递减队列维护窗口 [i-k, i-1] 内的最大 dp 值
4. 队列中存储索引，对应的 dp 值保持单调递减

算法步骤：
1. 初始化 dp 数组，dp[0] = nums[0]
2. 初始化单调递减队列，将 0 加入队列
3. 遍历数组从 1 到 n-1：
    a. 移除队列中超出窗口范围的索引
    b. dp[i] = nums[i] + dp[队列头部]
    c. 维护队列单调性，移除尾部所有 dp 值小于等于 dp[i] 的索引
    d. 将 i 加入队列
4. 返回 dp[n-1]

时间复杂度分析：
O(n) - 每个元素最多入队出队一次

空间复杂度分析：
O(n) - dp 数组和单调队列

是否最优解：
✅ 是，这是处理此类问题的最优时间复杂度解法

工程化考量：
- 使用Python内置deque提高代码可读性
- 考虑边界条件处理（k=0, 数组长度为1等）
- 处理极端输入情况（大数组、极限值等）
"""

def maxResult(nums, k):
    """
    计算跳跃游戏 VI 的最大分数
    
    Args:
        nums: 输入数组
        k: 最大跳跃步数
        
    Returns:
        int: 最大分数
        
    Raises:
        TypeError: 如果输入不是列表或k不是整数
        ValueError: 如果数组为空
    """
    # 输入验证
    if not isinstance(nums, list):
        raise TypeError("nums must be a list")
    if not isinstance(k, int):
        raise TypeError("k must be an integer")
    if not nums:
        raise ValueError("nums cannot be empty")
    
    n = len(nums)
    if n == 1:
        return nums[0]
    
    # dp[i] 表示到达位置 i 时的最大分数
    dp = [0] * n
    dp[0] = nums[0]
    
    # 使用单调递减队列维护窗口内的最大 dp 值
    # 队列中存储索引，对应的 dp 值保持单调递减
    dq = deque()
    dq.append(0)
    
    for i in range(1, n):
        # 移除队列中超出窗口范围的索引
        # 窗口范围为 [i-k, i-1]
        while dq and dq[0] < i - k:
            dq.popleft()
        
        # 计算当前位置的最大分数
        # dp[i] = 当前值 + 窗口内的最大 dp 值
        dp[i] = nums[i] + dp[dq[0]]
        
        # 维护队列的单调递减性质
        # 从队尾开始，移除所有 dp 值小于等于当前 dp[i] 的索引
        while dq and dp[dq[-1]] <= dp[i]:
            dq.pop()
        
        # 将当前索引加入队列
        dq.append(i)
    
    return dp[n - 1]

def test_max_result():
    """测试函数 - 包含多种边界情况和测试用例"""
    print("=== LeetCode 1696 测试用例 ===")
    
    # 测试用例1：基础示例
    nums1 = [1, -1, -2, 4, -7, 3]
    k1 = 2
    result1 = maxResult(nums1, k1)
    print("测试用例1 - 输入: [1,-1,-2,4,-7,3], k=2")
    print(f"预期输出: 7, 实际输出: {result1}")
    print(f"测试结果: {'✓ 通过' if result1 == 7 else '✗ 失败'}")
    
    # 测试用例2：全部为正数
    nums2 = [10, -5, -2, 4, 0, 3]
    k2 = 3
    result2 = maxResult(nums2, k2)
    print("\n测试用例2 - 输入: [10,-5,-2,4,0,3], k=3")
    print(f"预期输出: 17, 实际输出: {result2}")
    print(f"测试结果: {'✓ 通过' if result2 == 17 else '✗ 失败'}")
    
    # 测试用例3：单个元素
    nums3 = [100]
    k3 = 1
    result3 = maxResult(nums3, k3)
    print("\n测试用例3 - 输入: [100], k=1")
    print(f"预期输出: 100, 实际输出: {result3}")
    print(f"测试结果: {'✓ 通过' if result3 == 100 else '✗ 失败'}")
    
    # 测试用例4：k=1的特殊情况
    nums4 = [1, -5, -20, 4, -1, 3, -6, -4]
    k4 = 1
    result4 = maxResult(nums4, k4)
    print("\n测试用例4 - 输入: [1,-5,-20,4,-1,3,-6,-4], k=1")
    print(f"预期输出: -3, 实际输出: {result4}")
    print(f"测试结果: {'✓ 通过' if result4 == -3 else '✗ 失败'}")
    
    # 测试用例5：k等于数组长度
    nums5 = [1, -1, -2, 4, -7, 3]
    k5 = 6
    result5 = maxResult(nums5, k5)
    print("\n测试用例5 - 输入: [1,-1,-2,4,-7,3], k=6")
    print(f"预期输出: 7, 实际输出: {result5}")
    print(f"测试结果: {'✓ 通过' if result5 == 7 else '✗ 失败'}")
    
    # 测试用例6：输入验证
    try:
        maxResult("not a list", 2)
    except TypeError as e:
        print(f"\n测试用例6 - 输入验证: ✓ 通过 - {e}")
    else:
        print("\n测试用例6 - 输入验证: ✗ 失败 - 应该抛出TypeError")
    
    try:
        maxResult([], 2)
    except ValueError as e:
        print(f"测试用例7 - 空数组验证: ✓ 通过 - {e}")
    else:
        print("测试用例7 - 空数组验证: ✗ 失败 - 应该抛出ValueError")
    
    print("\n=== 算法分析 ===")
    print("时间复杂度: O(n) - 每个元素最多入队出队一次")
    print("空间复杂度: O(n) - dp数组和单调队列")
    print("最优解: ✅ 是")
    
    print("\n=== Python语言特性分析 ===")
    print("1. 动态类型系统，无需声明变量类型")
    print("2. 内置collections.deque，高效双端队列操作")
    print("3. 简洁的语法和内置异常处理")
    print("4. 支持函数式编程风格")

if __name__ == "__main__":
    test_max_result()