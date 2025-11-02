"""
环形数组中不能选相邻元素的最大累加和（打家劫舍 II）- Python实现
给定一个数组nums，长度为n
nums是一个环形数组，下标0和下标n-1是连在一起的
可以随意选择数字，但是不能选择相邻的数字
返回能得到的最大累加和
测试链接 : https://leetcode.cn/problems/house-robber-ii/

算法核心思想：
1. 环形数组的打家劫舍问题可以分解为两个线性问题：
   a) 不偷第一个房屋（考虑nums[1...n-1]）
   b) 偷第一个房屋（考虑nums[0] + nums[2...n-2]）
2. 取这两种情况的最大值作为最终结果
3. 使用动态规划解决线性打家劫舍问题

时间复杂度分析：
- 最优时间复杂度：O(n) - 需要遍历数组两次
- 空间复杂度：O(1) - 优化后只需常数空间

工程化考量：
1. 边界处理：处理空数组、单元素数组等特殊情况
2. 异常防御：处理索引越界等错误情况
3. 性能优化：使用空间优化的动态规划
4. 可测试性：提供完整的测试用例和性能分析
"""

from typing import List
import time

class HouseRobberII:
    """
    环形打家劫舍问题的解决方案类
    提供多种实现方式和工具方法
    """
    
    @staticmethod
    def rob(nums: List[int]) -> int:
        """
        计算环形数组的打家劫舍最大金额
        
        算法原理：
        - 情况1：不偷第一个房屋，问题转化为nums[1...n-1]的线性打家劫舍
        - 情况2：偷第一个房屋，问题转化为nums[0] + nums[2...n-2]的线性打家劫舍
        - 取两种情况的最大值
        
        时间复杂度：O(n) - 两次线性打家劫舍计算
        空间复杂度：O(1) - 常数空间
        
        Args:
            nums: 环形数组，表示每个房屋的金额
            
        Returns:
            int: 最大可偷窃金额
            
        Raises:
            ValueError: 如果输入数组为空
            
        Examples:
            >>> HouseRobberII.rob([2, 3, 2])
            3
            >>> HouseRobberII.rob([1, 2, 3, 1])
            4
        """
        # 边界检查
        if not nums:
            raise ValueError("输入数组不能为空")
        
        n = len(nums)
        # 特殊情况：单元素数组
        if n == 1:
            return nums[0]
        
        # 情况1：不偷第一个房屋（考虑nums[1...n-1]）
        case1 = HouseRobberII._best(nums, 1, n - 1)
        # 情况2：偷第一个房屋（考虑nums[0] + nums[2...n-2]）
        case2 = nums[0] + HouseRobberII._best(nums, 2, n - 2)
        
        # 返回两种情况的最大值
        return max(case1, case2)
    
    @staticmethod
    def _best(nums: List[int], l: int, r: int) -> int:
        """
        计算线性数组的打家劫舍最大金额（空间优化版本）
        
        Args:
            nums: 原始数组
            l: 起始索引（包含）
            r: 结束索引（包含）
            
        Returns:
            int: 指定范围内的最大打家劫舍金额
        """
        # 边界检查：空范围
        if l > r:
            return 0
        # 单元素范围
        if l == r:
            return nums[l]
        # 双元素范围
        if l + 1 == r:
            return max(nums[l], nums[r])
        
        # 空间优化的动态规划
        prepre = nums[l]  # dp[i-2]
        pre = max(nums[l], nums[l + 1])  # dp[i-1]
        
        # 从第三个元素开始遍历
        for i in range(l + 2, r + 1):
            # 状态转移：选择偷或不偷当前房屋
            current = max(pre, nums[i] + max(0, prepre))
            # 更新状态
            prepre, pre = pre, current
        
        return pre
    
    @staticmethod
    def rob_state_machine(nums: List[int]) -> int:
        """
        使用状态机思想的另一种实现
        
        算法细节：
        - 分别计算包含第一个房屋和不包含第一个房屋的情况
        - 使用两个变量记录偷和不偷的状态
        
        Args:
            nums: 环形数组
            
        Returns:
            int: 最大可偷窃金额
        """
        if not nums:
            return 0
        
        n = len(nums)
        if n == 1:
            return nums[0]
        
        # 情况1：不包含第一个房屋
        not_rob_first = 0
        rob_first = 0
        
        for i in range(1, n):
            temp = not_rob_first
            not_rob_first = max(not_rob_first, rob_first)
            rob_first = temp + nums[i]
        case1 = max(not_rob_first, rob_first)
        
        # 情况2：包含第一个房屋（不能包含最后一个房屋）
        not_rob_first = 0
        rob_first = nums[0]
        
        for i in range(1, n - 1):
            temp = not_rob_first
            not_rob_first = max(not_rob_first, rob_first)
            rob_first = temp + nums[i]
        case2 = max(not_rob_first, rob_first)
        
        return max(case1, case2)
    
    @staticmethod
    def test_all_methods():
        """测试所有实现方法的一致性"""
        test_cases = [
            ([2, 3, 2], 3),           # 标准环形数组
            ([1], 1),                 # 单元素数组
            ([1, 2], 2),              # 双元素数组
            ([1, 2, 3, 1], 4),        # 复杂环形数组
            ([5, 10, 5, 10, 5], 20),  # 全正数数组
            ([2, 7, 9, 3, 1], 11),    # 混合数组
            ([4, 1, 2, 7, 5, 3, 1], 14) # 复杂情况
        ]
        
        print("=== 环形打家劫舍算法测试 ===")
        
        for i, (nums, expected) in enumerate(test_cases, 1):
            try:
                result1 = HouseRobberII.rob(nums)
                result2 = HouseRobberII.rob_state_machine(nums)
                
                print(f"测试用例 {i}:")
                print(f"  输入: {nums}")
                print(f"  期望: {expected}")
                print(f"  方法1结果: {result1} {'✓' if result1 == expected else '✗'}")
                print(f"  方法2结果: {result2} {'✓' if result2 == expected else '✗'}")
                print(f"  方法一致性: {'一致' if result1 == result2 else '不一致'}")
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
        result = HouseRobberII.rob(large_array)
        end_time = time.time()
        
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        
        print(f"计算结果: {result}")
        print(f"执行时间: {duration:.2f} 毫秒")
        print("=== 性能测试结束 ===")


def rob_simple(nums: List[int]) -> int:
    """
    简化版本：适合快速实现和面试
    
    Args:
        nums: 环形数组
        
    Returns:
        int: 最大可偷窃金额
    """
    if not nums:
        return 0
    
    n = len(nums)
    if n == 1:
        return nums[0]
    
    def linear_rob(start, end):
        if start > end:
            return 0
        if start == end:
            return nums[start]
        
        prepre, pre = nums[start], max(nums[start], nums[start + 1])
        for i in range(start + 2, end + 1):
            current = max(pre, nums[i] + max(0, prepre))
            prepre, pre = pre, current
        return pre
    
    return max(linear_rob(1, n - 1), nums[0] + linear_rob(2, n - 2))


if __name__ == "__main__":
    # 运行功能测试
    HouseRobberII.test_all_methods()
    
    # 运行性能测试（可选）
    # HouseRobberII.performance_test()
    
    # 简单使用示例
    test_nums = [2, 3, 2]
    result = rob_simple(test_nums)
    print(f"简单版本测试: {test_nums} -> {result}")

"""
扩展思考与工程化考量：

1. 算法正确性深度分析：
   - 环形数组的特性决定了必须考虑首尾相连的约束
   - 分解为两个线性问题保证了所有可能情况的覆盖
   - 数学证明：两种情况的并集是完备的

2. 工程实践要点：
   - 边界处理：各种特殊情况需要单独处理
   - 性能优化：空间复杂度从O(n)到O(1)的优化
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