"""
删除并获得点数 - Python实现
给你一个整数数组 nums ，你可以对它进行一些操作。
每次操作中，选择任意一个 nums[i] ，删除它并获得 nums[i] 的点数。
之后，你必须删除 所有 等于 nums[i] - 1 和 nums[i] + 1 的元素。
开始你拥有 0 个点数。返回你能通过这些操作获得的最大点数。
测试链接 : https://leetcode.cn/problems/delete-and-earn/

算法核心思想：
1. 这个问题可以转化为打家劫舍问题的变体
2. 关键观察：选择某个数字x后，就不能选择x-1和x+1，这类似于打家劫舍中不能选择相邻房屋
3. 首先统计每个数字的总点数（数字值 × 出现次数）
4. 然后使用动态规划在数字序列中选择不相邻的数字以获得最大点数

时间复杂度分析：
- 最优时间复杂度：O(n + k) - 其中n是数组长度，k是数组中的最大值
- 空间复杂度：O(k) - 需要额外的points数组存储每个数字的总点数

工程化考量：
1. 边界处理：处理空数组、单元素数组等特殊情况
2. 性能优化：使用空间优化的动态规划
3. 数值范围：处理大数值范围的情况
4. 可测试性：提供完整的测试用例和性能分析
"""

from typing import List
import time
from collections import defaultdict

class DeleteAndEarn:
    """
    删除并获得点数问题的解决方案类
    提供多种实现方式和工具方法
    """
    
    @staticmethod
    def delete_and_earn(nums: List[int]) -> int:
        """
        计算通过删除元素能获得的最大点数（基础版本）
        
        算法原理：
        1. 统计阶段：计算每个数字的总点数 points[num] = num * count(num)
        2. 转化阶段：问题转化为在points数组中选择不相邻元素的最大和
        3. 动态规划：使用打家劫舍问题的标准解法
        
        时间复杂度：O(n + k) - n是数组长度，k是最大值
        空间复杂度：O(k) - 需要points和dp数组
        
        Args:
            nums: 输入的整数数组
            
        Returns:
            int: 能获得的最大点数
            
        Raises:
            ValueError: 如果输入数组为空
            
        Examples:
            >>> DeleteAndEarn.delete_and_earn([3, 4, 2])
            6
            >>> DeleteAndEarn.delete_and_earn([2, 2, 3, 3, 3, 4])
            9
        """
        # 边界检查
        if not nums:
            raise ValueError("输入数组不能为空")
        
        # 计算数组中的最大值
        max_val = max(nums) if nums else 0
        
        # points[i]表示选择所有数字i能获得的总点数
        points = [0] * (max_val + 1)
        for num in nums:
            points[num] += num
        
        # 特殊情况处理
        if max_val == 0:
            return points[0]
        if max_val == 1:
            return max(points[0], points[1])
        
        # 动态规划数组
        dp = [0] * (max_val + 1)
        dp[0] = points[0]
        dp[1] = max(points[0], points[1])
        
        # 状态转移：标准的打家劫舍问题
        for i in range(2, max_val + 1):
            dp[i] = max(dp[i - 1], dp[i - 2] + points[i])
        
        return dp[max_val]
    
    @staticmethod
    def delete_and_earn_optimized(nums: List[int]) -> int:
        """
        空间优化版本
        
        算法改进：
        - 使用两个变量代替dp数组，将空间复杂度从O(k)优化到O(1)
        - 保持相同的时间复杂度
        
        时间复杂度：O(n + k)
        空间复杂度：O(k) - 需要points数组
        
        Args:
            nums: 输入的整数数组
            
        Returns:
            int: 能获得的最大点数
        """
        if not nums:
            return 0
        
        max_val = max(nums)
        points = [0] * (max_val + 1)
        for num in nums:
            points[num] += num
        
        if max_val == 0:
            return points[0]
        if max_val == 1:
            return max(points[0], points[1])
        
        # 空间优化的动态规划
        prev_prev = points[0]  # dp[i-2]
        prev = max(points[0], points[1])  # dp[i-1]
        
        for i in range(2, max_val + 1):
            current = max(prev, prev_prev + points[i])
            prev_prev, prev = prev, current
        
        return prev
    
    @staticmethod
    def delete_and_earn_hashmap(nums: List[int]) -> int:
        """
        使用哈希表优化的版本（适用于数值范围很大的情况）
        
        算法改进：
        - 当数值范围很大但实际出现的数字很少时，使用字典存储点数
        - 只处理实际出现的数字，避免遍历整个数值范围
        
        时间复杂度：O(n log n) - 排序和遍历
        空间复杂度：O(n) - 存储实际出现的数字
        
        Args:
            nums: 输入的整数数组
            
        Returns:
            int: 能获得的最大点数
        """
        if not nums:
            return 0
        
        # 统计每个数字的总点数
        points_map = defaultdict(int)
        for num in nums:
            points_map[num] += num
        
        # 如果没有数字，返回0
        if not points_map:
            return 0
        
        # 将数字按顺序排列
        keys = sorted(points_map.keys())
        n = len(keys)
        
        # 特殊情况：只有一个数字
        if n == 1:
            return points_map[keys[0]]
        
        # 动态规划
        dp = [0] * n
        dp[0] = points_map[keys[0]]
        
        # 检查第二个数字是否与第一个相邻
        if keys[1] - keys[0] == 1:
            dp[1] = max(dp[0], points_map[keys[1]])
        else:
            dp[1] = dp[0] + points_map[keys[1]]
        
        for i in range(2, n):
            current_key = keys[i]
            prev_key = keys[i - 1]
            
            if current_key - prev_key == 1:
                # 当前数字与前一个数字相邻，不能同时选择
                dp[i] = max(dp[i - 1], dp[i - 2] + points_map[current_key])
            else:
                # 当前数字与前一个数字不相邻，可以同时选择
                dp[i] = dp[i - 1] + points_map[current_key]
        
        return dp[n - 1]
    
    @staticmethod
    def test_all_methods():
        """测试所有实现方法的一致性"""
        test_cases = [
            ([3, 4, 2], 6),                     # 标准情况
            ([2, 2, 3, 3, 3, 4], 9),            # 重复数字
            ([5], 5),                           # 单元素数组
            ([1, 1, 1, 2, 4, 5, 5, 5, 6], 18), # 大数值范围
            ([1, 2, 3, 4, 5], 9),              # 连续数字
            ([8, 10, 4, 9, 1, 3, 5, 9, 4, 10], 37) # 复杂情况
        ]
        
        print("=== 删除并获得点数算法测试 ===")
        
        for i, (nums, expected) in enumerate(test_cases, 1):
            try:
                result1 = DeleteAndEarn.delete_and_earn(nums)
                result2 = DeleteAndEarn.delete_and_earn_optimized(nums)
                result3 = DeleteAndEarn.delete_and_earn_hashmap(nums)
                
                print(f"测试用例 {i}:")
                print(f"  输入: {nums}")
                print(f"  期望: {expected}")
                print(f"  方法1结果: {result1} {'✓' if result1 == expected else '✗'}")
                print(f"  方法2结果: {result2} {'✓' if result2 == expected else '✗'}")
                print(f"  方法3结果: {result3} {'✓' if result3 == expected else '✗'}")
                print(f"  一致性: {'一致' if result1 == result2 == result3 else '不一致'}")
                print()
                
            except Exception as e:
                print(f"测试用例 {i} 异常: {e}")
                print()
        
        print("=== 测试完成 ===")
    
    @staticmethod
    def performance_test():
        """性能测试：大数据量验证"""
        import random
        
        size = 1000000
        max_val = 10000
        
        # 生成随机测试数据
        random.seed(42)
        large_array = [random.randint(1, max_val) for _ in range(size)]
        
        print("=== 性能测试开始 ===")
        print(f"数据量: {size} 个元素, 数值范围: 1-{max_val}")
        
        # 测试方法1
        start_time = time.time()
        result1 = DeleteAndEarn.delete_and_earn(large_array)
        time1 = (time.time() - start_time) * 1000
        
        # 测试方法2
        start_time = time.time()
        result2 = DeleteAndEarn.delete_and_earn_optimized(large_array)
        time2 = (time.time() - start_time) * 1000
        
        # 测试方法3
        start_time = time.time()
        result3 = DeleteAndEarn.delete_and_earn_hashmap(large_array)
        time3 = (time.time() - start_time) * 1000
        
        print(f"方法1结果: {result1}, 时间: {time1:.2f} 毫秒")
        print(f"方法2结果: {result2}, 时间: {time2:.2f} 毫秒")
        print(f"方法3结果: {result3}, 时间: {time3:.2f} 毫秒")
        print("=== 性能测试结束 ===")


def delete_and_earn_simple(nums: List[int]) -> int:
    """
    简化版本：适合快速实现和面试
    
    Args:
        nums: 输入的整数数组
        
    Returns:
        int: 能获得的最大点数
    """
    if not nums:
        return 0
    
    max_val = max(nums)
    points = [0] * (max_val + 1)
    
    for num in nums:
        points[num] += num
    
    prev_prev, prev = 0, 0
    for i in range(max_val + 1):
        prev_prev, prev = prev, max(prev, prev_prev + points[i])
    
    return prev


if __name__ == "__main__":
    # 运行功能测试
    DeleteAndEarn.test_all_methods()
    
    # 运行性能测试（可选）
    # DeleteAndEarn.performance_test()
    
    # 简单使用示例
    test_nums = [3, 4, 2]
    result = delete_and_earn_simple(test_nums)
    print(f"简单版本测试: {test_nums} -> {result}")

"""
扩展思考与工程化考量：

1. 算法正确性深度分析：
   - 为什么这个问题可以转化为打家劫舍问题？
     因为选择某个数字x后，就不能选择x-1和x+1，这类似于不能选择相邻房屋
   - 统计阶段的重要性：将重复数字的点数累加，简化问题
   - 动态规划的状态转移：标准的打家劫舍公式

2. 工程实践要点：
   - 边界处理：各种特殊情况需要单独处理
   - 性能优化：根据数据特征选择合适的方法
   - 代码可读性：清晰的变量命名和注释

3. 测试策略：
   - 单元测试：覆盖各种边界情况
   - 性能测试：验证大数据量处理能力
   - 一致性测试：确保不同实现方法结果一致

4. 多语言对比优势：
   - Python：开发效率高，字典操作方便
   - Java：企业级应用，类型安全
   - C++：性能最优，适合高性能场景
"""