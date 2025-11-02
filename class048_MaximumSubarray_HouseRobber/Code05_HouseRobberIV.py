"""
打家劫舍 IV - Python实现
沿街有一排连续的房屋。每间房屋内都藏有一定的现金
现在有一位小偷计划从这些房屋中窃取现金
由于相邻的房屋装有相互连通的防盗系统，所以小偷不会窃取相邻的房屋
小偷的 窃取能力 定义为他在窃取过程中能从单间房屋中窃取的 最大金额
给你一个整数数组 nums 表示每间房屋存放的现金金额
第i间房屋中放有nums[i]的钱数
另给你一个整数k，表示小偷需要窃取至少 k 间房屋
返回小偷需要的最小窃取能力值
测试链接 : https://leetcode.cn/problems/house-robber-iv/

算法核心思想：
1. 这是一个二分搜索 + 动态规划（或贪心）的问题
2. 关键观察：窃取能力值越大，能偷的房屋越多（单调性）
3. 使用二分搜索在[min(nums), max(nums)]范围内寻找最小满足条件的ability
4. 对于每个候选ability，计算最多能偷多少间房屋

时间复杂度分析：
- 最优时间复杂度：O(n * log(max-min)) - 二分搜索 + 线性验证
- 空间复杂度：O(1) - 优化后只需常数空间

工程化考量：
1. 边界处理：处理空数组、k=0等特殊情况
2. 性能优化：使用贪心算法代替动态规划进行验证
3. 鲁棒性：处理数值范围和边界条件
4. 可测试性：提供完整的测试用例和性能分析
"""

from typing import List
import time

class HouseRobberIV:
    """
    打家劫舍 IV 问题的解决方案类
    提供多种实现方式和工具方法
    """
    
    @staticmethod
    def min_capability(nums: List[int], k: int) -> int:
        """
        计算小偷需要的最小窃取能力值
        
        算法原理：
        - 二分搜索：在房屋金额的最小值和最大值之间搜索
        - 验证函数：对于每个候选ability，计算最多能偷多少间房屋
        - 单调性：ability越大，能偷的房屋越多
        
        时间复杂度：O(n * log(max-min))
        空间复杂度：O(1)
        
        Args:
            nums: 房屋金额数组
            k: 需要窃取的最小房屋数量
            
        Returns:
            int: 最小窃取能力值
            
        Raises:
            ValueError: 如果输入无效
            
        Examples:
            >>> HouseRobberIV.min_capability([2, 3, 5, 9], 2)
            5
            >>> HouseRobberIV.min_capability([2, 7, 9, 3, 1], 2)
            2
        """
        # 边界检查
        if not nums:
            raise ValueError("输入数组不能为空")
        if k <= 0:
            raise ValueError("k必须大于0")
        if k > len(nums):
            raise ValueError("k不能大于数组长度")
        
        n = len(nums)
        # 确定二分搜索的范围 [min_val, max_val]
        min_val = min(nums)
        max_val = max(nums)
        
        # 特殊情况：k=1时，最小能力值就是数组中的最小值
        if k == 1:
            return min_val
        
        # 二分搜索：在[min_val, max_val]范围内寻找最小满足条件的ability
        left, right = min_val, max_val
        answer = max_val  # 初始化为最大值
        
        while left <= right:
            mid = left + (right - left) // 2  # 防止溢出
            # 验证当前ability是否能偷至少k间房屋
            if HouseRobberIV._most_rob_greedy(nums, mid) >= k:
                answer = mid  # 当前ability满足条件，尝试更小的值
                right = mid - 1
            else:
                left = mid + 1  # 当前ability不满足条件，需要更大的值
        
        return answer
    
    @staticmethod
    def _most_rob_greedy(nums: List[int], ability: int) -> int:
        """
        贪心算法：计算给定ability时最多能偷多少间房屋
        
        算法原理：
        - 贪心策略：只要能偷就偷，然后跳过下一个房屋
        - 这种策略在打家劫舍约束下是最优的
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        
        Args:
            nums: 房屋金额数组
            ability: 窃取能力值
            
        Returns:
            int: 最多能偷的房屋数量
        """
        count = 0
        i = 0
        n = len(nums)
        
        while i < n:
            if nums[i] <= ability:
                # 偷当前房屋，然后跳过下一个
                count += 1
                i += 2  # 跳过下一个房屋
            else:
                # 不能偷当前房屋，检查下一个
                i += 1
        
        return count
    
    @staticmethod
    def _most_rob_dp(nums: List[int], ability: int) -> int:
        """
        动态规划版本：用于对比验证
        
        算法细节：
        - dp[i]表示考虑前i+1个房屋时最多能偷的房屋数量
        - 状态转移：dp[i] = max(dp[i-1], dp[i-2] + (nums[i] <= ability))
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            nums: 房屋金额数组
            ability: 窃取能力值
            
        Returns:
            int: 最多能偷的房屋数量
        """
        n = len(nums)
        if n == 0:
            return 0
        if n == 1:
            return 1 if nums[0] <= ability else 0
        if n == 2:
            return 1 if (nums[0] <= ability or nums[1] <= ability) else 0
        
        dp = [0] * n
        dp[0] = 1 if nums[0] <= ability else 0
        dp[1] = 1 if (nums[0] <= ability or nums[1] <= ability) else 0
        
        for i in range(2, n):
            steal_current = 1 if nums[i] <= ability else 0
            dp[i] = max(dp[i-1], steal_current + dp[i-2])
        
        return dp[n-1]
    
    @staticmethod
    def _most_rob_optimized(nums: List[int], ability: int) -> int:
        """
        空间优化版本：使用两个变量代替dp数组
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        
        Args:
            nums: 房屋金额数组
            ability: 窃取能力值
            
        Returns:
            int: 最多能偷的房屋数量
        """
        n = len(nums)
        if n == 0:
            return 0
        if n == 1:
            return 1 if nums[0] <= ability else 0
        if n == 2:
            return 1 if (nums[0] <= ability or nums[1] <= ability) else 0
        
        prepre = 1 if nums[0] <= ability else 0
        pre = 1 if (nums[0] <= ability or nums[1] <= ability) else 0
        
        for i in range(2, n):
            steal_current = 1 if nums[i] <= ability else 0
            current = max(pre, steal_current + prepre)
            prepre, pre = pre, current
        
        return pre
    
    @staticmethod
    def test_all_methods():
        """测试所有实现方法的一致性"""
        test_cases = [
            ([2, 3, 5, 9], 2, 5),           # 标准情况
            ([2, 7, 9, 3, 1], 2, 2),        # 简单情况
            ([1, 2, 3], 1, 1),              # k=1特殊情况
            ([4, 1, 2, 7, 5, 3, 1], 3, 4),  # 复杂情况
            ([1, 1, 1, 1], 2, 1),           # 全相同值
            ([10, 5, 2, 8, 3], 3, 5)        # 混合情况
        ]
        
        print("=== 打家劫舍 IV 算法测试 ===")
        
        for i, (nums, k, expected) in enumerate(test_cases, 1):
            try:
                result = HouseRobberIV.min_capability(nums, k)
                greedy_count = HouseRobberIV._most_rob_greedy(nums, result)
                dp_count = HouseRobberIV._most_rob_dp(nums, result)
                optimized_count = HouseRobberIV._most_rob_optimized(nums, result)
                
                print(f"测试用例 {i}:")
                print(f"  输入: nums={nums}, k={k}")
                print(f"  期望: {expected}")
                print(f"  结果: {result} {'✓' if result == expected else '✗'}")
                print(f"  验证: 贪心={greedy_count}, DP={dp_count}, 优化={optimized_count}")
                print(f"  一致性: {'一致' if greedy_count == dp_count == optimized_count else '不一致'}")
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
        k = size // 2
        
        print("=== 性能测试开始 ===")
        print(f"数据量: {size} 个元素, k={k}")
        
        start_time = time.time()
        result = HouseRobberIV.min_capability(large_array, k)
        end_time = time.time()
        
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        
        print(f"计算结果: {result}")
        print(f"执行时间: {duration:.2f} 毫秒")
        print("=== 性能测试结束 ===")


def min_capability_simple(nums: List[int], k: int) -> int:
    """
    简化版本：适合快速实现和面试
    
    Args:
        nums: 房屋金额数组
        k: 需要窃取的最小房屋数量
        
    Returns:
        int: 最小窃取能力值
    """
    if not nums or k <= 0:
        return 0
    
    left, right = min(nums), max(nums)
    answer = right
    
    while left <= right:
        mid = (left + right) // 2
        count = 0
        i = 0
        
        while i < len(nums):
            if nums[i] <= mid:
                count += 1
                i += 2
            else:
                i += 1
        
        if count >= k:
            answer = mid
            right = mid - 1
        else:
            left = mid + 1
    
    return answer


if __name__ == "__main__":
    # 运行功能测试
    HouseRobberIV.test_all_methods()
    
    # 运行性能测试（可选）
    # HouseRobberIV.performance_test()
    
    # 简单使用示例
    test_nums = [2, 3, 5, 9]
    k = 2
    result = min_capability_simple(test_nums, k)
    print(f"简单版本测试: nums={test_nums}, k={k} -> {result}")

"""
扩展思考与工程化考量：

1. 算法正确性深度分析：
   - 二分搜索的正确性基于单调性：ability越大，能偷的房屋越多
   - 贪心算法的正确性：在不能偷相邻房屋的约束下，贪心策略是最优的
   - 边界情况处理：k=1、k=n等特殊情况需要单独处理

2. 工程实践要点：
   - 边界处理：各种特殊情况需要单独处理
   - 性能优化：二分搜索 + 贪心验证的组合
   - 代码可读性：清晰的变量命名和注释

3. 测试策略：
   - 单元测试：覆盖各种边界情况
   - 性能测试：验证大数据量处理能力
   - 一致性测试：确保不同实现方法结果一致

4. 多语言对比优势：
   - Python：开发效率高，适合快速原型
   - Java：企业级应用，类型安全
   - C++：性能最优，适合高性能场景
"""