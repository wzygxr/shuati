"""
打家劫舍问题（数组中不能选相邻元素的最大累加和）- Python实现
题目描述：给定一个数组，可以随意选择数字，但是不能选择相邻的数字，返回能得到的最大累加和。
从另一个角度理解：你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，
一夜之内能够偷窃到的最高金额。

测试链接：https://leetcode.cn/problems/house-robber/

算法核心思想：
这是一个经典的动态规划问题，对于每个位置，我们有两种选择：选或者不选。
如果选当前元素，那么前一个元素就不能选；如果不选当前元素，那么可以选择前一个元素或不选。
我们需要在每一步做出最优选择，使得最终的累加和最大。

时间复杂度分析：
- 最优时间复杂度：O(n) - 只需遍历数组一次
- 空间复杂度：O(1) - 优化后只需常数空间

工程化考量：
1. 异常处理：对空数组和边界情况进行处理
2. 鲁棒性：处理极端输入（单元素、双元素、全零等）
3. 可测试性：提供完整的测试用例和性能分析
4. 多解法对比：展示不同实现方式的优缺点
"""

from typing import List
import time

class HouseRobber:
    """
    打家劫舍问题的解决方案类
    提供多种实现方式和工具方法
    """
    
    @staticmethod
    def rob(nums: List[int]) -> int:
        """
        使用动态规划计算最大可偷窃金额（空间优化版本）
        
        算法细节：
        - 维护两个状态：前两个房屋的最大金额
        - 状态转移：dp[i] = max(dp[i-1], dp[i-2] + nums[i])
        - 时间复杂度：O(n)，空间复杂度：O(1)
        
        Args:
            nums: 整数列表，表示每个房屋的金额
            
        Returns:
            int: 最大可偷窃金额
            
        Raises:
            ValueError: 如果输入为None
            
        Examples:
            >>> HouseRobber.rob([1, 2, 3, 1])
            4
            >>> HouseRobber.rob([2, 7, 9, 3, 1])
            12
        """
        if nums is None:
            raise ValueError("输入数组不能为None")
        
        if not nums:
            return 0
        
        n = len(nums)
        if n == 1:
            return nums[0]
        if n == 2:
            return max(nums[0], nums[1])
        
        # 空间优化：只保存前两个状态
        prev_prev = nums[0]  # dp[i-2]
        prev = max(nums[0], nums[1])  # dp[i-1]
        
        for i in range(2, n):
            # 状态转移：选择偷或不偷当前房屋
            current = max(prev, prev_prev + nums[i])
            # 更新状态
            prev_prev, prev = prev, current
        
        return prev
    
    @staticmethod
    def rob_dp(nums: List[int]) -> int:
        """
        动态规划版本（基础实现，用于教学）
        
        算法细节：
        - 使用dp数组记录每个位置的最大金额
        - 更直观地展示状态转移过程
        - 时间复杂度：O(n)，空间复杂度：O(n)
        
        Args:
            nums: 整数列表
            
        Returns:
            int: 最大可偷窃金额
        """
        if not nums:
            return 0
        
        n = len(nums)
        if n == 1:
            return nums[0]
        
        dp = [0] * n
        dp[0] = nums[0]
        dp[1] = max(nums[0], nums[1])
        
        for i in range(2, n):
            dp[i] = max(dp[i-1], dp[i-2] + nums[i])
        
        return dp[n-1]
    
    @staticmethod
    def rob_state_machine(nums: List[int]) -> int:
        """
        状态机版本：使用两个变量分别记录偷和不偷的状态
        
        算法细节：
        - not_rob: 不偷当前房屋的最大金额
        - rob: 偷当前房屋的最大金额
        - 状态转移更符合问题本质
        
        Args:
            nums: 整数列表
            
        Returns:
            int: 最大可偷窃金额
        """
        if not nums:
            return 0
        
        not_rob = 0      # 不偷当前房屋的最大金额
        rob = nums[0]    # 偷当前房屋的最大金额
        
        for i in range(1, len(nums)):
            # 保存上一轮的状态
            prev_not_rob = not_rob
            prev_rob = rob
            
            # 当前不偷的最大金额 = 上一轮偷或不偷的最大值
            not_rob = max(prev_not_rob, prev_rob)
            # 当前偷的最大金额 = 上一轮不偷的最大金额 + 当前房屋金额
            rob = prev_not_rob + nums[i]
        
        return max(not_rob, rob)
    
    @staticmethod
    def test_all_methods():
        """测试所有实现方法的一致性"""
        test_cases = [
            ([1, 2, 3, 1], 4),
            ([2, 7, 9, 3, 1], 12),
            ([5], 5),
            ([], 0),
            ([2, 1, 1, 2], 4),
            ([1, 3, 1], 3),
            ([4, 1, 2, 7, 5, 3, 1], 14)
        ]
        
        print("=== 打家劫舍算法测试 ===")
        
        for i, (nums, expected) in enumerate(test_cases, 1):
            try:
                result1 = HouseRobber.rob(nums)
                result2 = HouseRobber.rob_dp(nums)
                result3 = HouseRobber.rob_state_machine(nums)
                
                print(f"测试用例 {i}:")
                print(f"  输入: {nums}")
                print(f"  期望: {expected}")
                print(f"  方法1结果: {result1} {'✓' if result1 == expected else '✗'}")
                print(f"  方法2结果: {result2} {'✓' if result2 == expected else '✗'}")
                print(f"  方法3结果: {result3} {'✓' if result3 == expected else '✗'}")
                print(f"  方法一致性: {'一致' if result1 == result2 == result3 else '不一致'}")
                print()
                
            except Exception as e:
                print(f"测试用例 {i} 异常: {e}")
                print()
        
        print("=== 测试完成 ===")
    
    @staticmethod
    def performance_test():
        """性能测试：大数据量验证"""
        size = 1000000
        large_array = [1] * size  # 全1数组
        
        print("=== 性能测试开始 ===")
        print(f"数据量: {size} 个元素")
        
        start_time = time.time()
        result = HouseRobber.rob(large_array)
        end_time = time.time()
        
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        
        print(f"计算结果: {result}")
        print(f"执行时间: {duration:.2f} 毫秒")
        print("=== 性能测试结束 ===")


def rob_simple(nums: List[int]) -> int:
    """
    简化版本：适合快速实现和面试
    
    Args:
        nums: 整数列表
        
    Returns:
        int: 最大可偷窃金额
    """
    if not nums:
        return 0
    
    n = len(nums)
    if n == 1:
        return nums[0]
    
    prev_prev, prev = nums[0], max(nums[0], nums[1])
    
    for i in range(2, n):
        current = max(prev, prev_prev + nums[i])
        prev_prev, prev = prev, current
    
    return prev


if __name__ == "__main__":
    # 运行功能测试
    HouseRobber.test_all_methods()
    
    # 运行性能测试（可选）
    # HouseRobber.performance_test()
    
    # 简单使用示例
    test_nums = [1, 2, 3, 1]
    result = rob_simple(test_nums)
    print(f"简单版本测试: {test_nums} -> {result}")

"""
扩展思考与工程化考量：

1. 算法变体与应用：
   - 环形房屋：首尾相连的特殊情况处理
   - 树形房屋：扩展到树形结构的动态规划
   - 删除并获得点数：转化为打家劫舍问题的技巧

2. 工程实践要点：
   - 边界处理：空数组、单元素等特殊情况
   - 性能优化：空间复杂度从O(n)到O(1)的优化
   - 代码可读性：清晰的变量命名和注释

3. 测试策略：
   - 单元测试：覆盖各种边界情况
   - 性能测试：验证大数据量处理能力
   - 一致性测试：确保不同实现方法结果一致

4. 多语言对比：
   - Python：开发效率高，适合快速原型
   - Java：企业级应用，类型安全
   - C++：性能最优，适合高性能场景
"""