import sys
from typing import List, Tuple
import random
import time

# UVA 10616 Divisible Group Sums
# 题目描述：给定N个整数，选择M个数字使得它们的和能被D整除，求方案数。
# 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1557
# 
# 解题思路：
# 这是一个分组背包+模数运算的问题，需要计算选择M个数字的和能被D整除的方案数。
# 
# 状态定义：dp[i][j][k] 表示前i个数字，选择j个数字，和对D取模为k的方案数
# 状态转移方程：
#   dp[i][j][k] = dp[i-1][j][k] + dp[i-1][j-1][(k - num[i] % D + D) % D]
# 
# 关键点：
# 1. 模数运算：处理负数取模，使用(k - num[i] % D + D) % D
# 2. 分组背包：每个数字只能选择一次
# 3. 空间优化：使用滚动数组优化空间复杂度
# 
# 时间复杂度：O(N * M * D)，其中N是数字数量，M是选择数量，D是除数
# 空间复杂度：O(M * D)，使用滚动数组优化
# 
# 工程化考量：
# 1. 模数处理：正确处理负数取模
# 2. 边界条件：处理M=0、D=0等特殊情况
# 3. 性能优化：使用滚动数组和模数运算优化
# 4. 异常处理：处理除数为0的情况

class DivisibleGroupSums:
    """
    可整除组和问题的多种解法
    """
    
    @staticmethod
    def divisible_group_sums(nums: List[int], M: int, D: int) -> int:
        """
        动态规划解法 - 分组背包+模数运算
        
        Args:
            nums: 整数数组
            M: 需要选择的数字个数
            D: 除数
            
        Returns:
            int: 方案数
        """
        # 参数验证
        if not nums:
            return 1 if M == 0 else 0
        if D == 0:
            raise ValueError("Divisor D cannot be zero")
        if M < 0 or M > len(nums):
            return 0
        
        n = len(nums)
        
        # 创建DP数组，使用滚动数组优化
        dp = [[0] * D for _ in range(M + 1)]
        dp[0][0] = 1  # 选择0个数字，和为0（模D为0）的方案数为1
        
        # 遍历每个数字
        for i in range(n):
            num = nums[i]
            mod = (num % D + D) % D  # 处理负数取模
            
            # 倒序遍历选择数量，避免重复选择
            for j in range(M, 0, -1):
                # 创建临时数组保存当前状态
                temp = dp[j][:]
                for k in range(D):
                    prev_mod = (k - mod + D) % D
                    dp[j][k] += dp[j - 1][prev_mod]
        
        return dp[M][0]
    
    @staticmethod
    def divisible_group_sums_2d(nums: List[int], M: int, D: int) -> int:
        """
        优化的动态规划解法 - 二维数组
        
        Args:
            nums: 整数数组
            M: 需要选择的数字个数
            D: 除数
            
        Returns:
            int: 方案数
        """
        if not nums:
            return 1 if M == 0 else 0
        if D == 0:
            raise ValueError("Divisor D cannot be zero")
        if M < 0 or M > len(nums):
            return 0
        
        n = len(nums)
        
        # 创建三维DP数组
        dp = [[[0] * D for _ in range(M + 1)] for _ in range(n + 1)]
        dp[0][0][0] = 1
        
        for i in range(1, n + 1):
            num = nums[i - 1]
            mod = (num % D + D) % D
            
            for j in range(M + 1):
                for k in range(D):
                    # 不选当前数字
                    dp[i][j][k] += dp[i - 1][j][k]
                    
                    # 选当前数字
                    if j >= 1:
                        prev_mod = (k - mod + D) % D
                        dp[i][j][k] += dp[i - 1][j - 1][prev_mod]
        
        return dp[n][M][0]
    
    @staticmethod
    def divisible_group_sums_optimized(nums: List[int], M: int, D: int) -> int:
        """
        空间优化的解法 - 使用两个二维数组交替
        
        Args:
            nums: 整数数组
            M: 需要选择的数字个数
            D: 除数
            
        Returns:
            int: 方案数
        """
        if not nums:
            return 1 if M == 0 else 0
        if D == 0:
            raise ValueError("Divisor D cannot be zero")
        if M < 0 or M > len(nums):
            return 0
        
        n = len(nums)
        
        # 使用两个二维数组交替
        dp = [[0] * D for _ in range(M + 1)]
        next_dp = [[0] * D for _ in range(M + 1)]
        dp[0][0] = 1
        
        for i in range(n):
            num = nums[i]
            mod = (num % D + D) % D
            
            # 复制当前状态到next_dp
            for j in range(M + 1):
                next_dp[j] = dp[j][:]
            
            # 更新next_dp数组
            for j in range(1, M + 1):
                for k in range(D):
                    prev_mod = (k - mod + D) % D
                    next_dp[j][k] += dp[j - 1][prev_mod]
            
            # 交换数组
            dp, next_dp = next_dp, dp
            
            # 清空next_dp数组用于下一次迭代
            for j in range(M + 1):
                next_dp[j] = [0] * D
        
        return dp[M][0]
    
    @staticmethod
    def combination(n: int, k: int) -> int:
        """
        计算组合数C(n, k)
        """
        if k < 0 or k > n:
            return 0
        if k == 0 or k == n:
            return 1
        
        result = 1
        for i in range(1, k + 1):
            result = result * (n - i + 1) // i
        return result
    
    @staticmethod
    def run_tests():
        """
        运行测试用例，验证所有方法的正确性
        """
        # 测试用例
        nums_cases = [
            [1, 2, 3, 4, 5],
            [2, 4, 6, 8, 10],
            [-1, 1, -2, 2, -3, 3],
            [10, 20, 30, 40, 50]
        ]
        M_list = [2, 3, 2, 3]
        D_list = [3, 2, 3, 10]
        
        print("可整除组和问题测试：")
        for i, (nums, M, D) in enumerate(zip(nums_cases, M_list, D_list)):
            try:
                result1 = DivisibleGroupSums.divisible_group_sums(nums, M, D)
                result2 = DivisibleGroupSums.divisible_group_sums_2d(nums, M, D)
                result3 = DivisibleGroupSums.divisible_group_sums_optimized(nums, M, D)
                
                print(f"nums={nums}, M={M}, D={D}: "
                      f"方法1={result1}, 方法2={result2}, 方法3={result3}")
                
                # 验证结果一致性
                if result1 != result2 or result2 != result3:
                    print("警告：不同方法结果不一致！")
                    
            except Exception as e:
                print(f"测试用例{i+1}时发生错误: {e}")
        
        # 性能测试 - 大规模数据
        n = 50
        large_nums = [random.randint(-500, 500) for _ in range(n)]
        large_M = 10
        large_D = 7
        
        start_time = time.time()
        large_result = DivisibleGroupSums.divisible_group_sums_optimized(large_nums, large_M, large_D)
        end_time = time.time()
        
        print(f"大规模测试: 数字数量={n}, M={large_M}, D={large_D}, "
              f"结果={large_result}, 耗时={end_time - start_time:.4f}秒")
        
        # 边界情况测试
        print("边界情况测试：")
        print(f"空数组, M=0: {DivisibleGroupSums.divisible_group_sums([], 0, 5)}")
        print(f"空数组, M=1: {DivisibleGroupSums.divisible_group_sums([], 1, 5)}")
        print(f"M=0: {DivisibleGroupSums.divisible_group_sums([1, 2, 3], 0, 5)}")
        print(f"M>n: {DivisibleGroupSums.divisible_group_sums([1, 2], 3, 5)}")
        
        # 特殊测试：D=1（所有组合都满足）
        print("D=1特殊测试：")
        test_nums = [1, 2, 3]
        special_result = DivisibleGroupSums.divisible_group_sums(test_nums, 2, 1)
        expected = DivisibleGroupSums.combination(3, 2)
        print(f"D=1, 预期=C(3,2)={expected}, 实际={special_result}")
        
        if special_result == expected:
            print("D=1测试验证通过")
        else:
            print("D=1测试验证失败")

def main():
    """
    主函数 - 运行测试和演示
    """
    try:
        DivisibleGroupSums.run_tests()
    except Exception as e:
        print(f"程序执行错误: {e}")
        return 1
    return 0

if __name__ == "__main__":
    sys.exit(main())

"""
复杂度分析：

方法1：动态规划（滚动数组）
- 时间复杂度：O(N * M * D)
  - N: 数字数量
  - M: 选择数量
  - D: 除数
- 空间复杂度：O(M * D)

方法2：三维动态规划
- 时间复杂度：O(N * M * D)
- 空间复杂度：O(N * M * D)

方法3：空间优化的动态规划
- 时间复杂度：O(N * M * D)
- 空间复杂度：O(M * D)（使用两个二维数组）

Python特定优化：
1. 使用列表推导式和切片操作
2. 利用Python的动态类型特性
3. 使用类型注解提高代码可读性
4. 使用random模块进行性能测试

关键点分析：
1. 模数运算：正确处理负数取模，使用(k - mod + D) % D
2. 状态定义：dp[i][j][k]表示前i个数字选j个模D为k的方案数
3. 空间优化：使用滚动数组减少空间复杂度
4. 边界处理：M=0时方案数为1（空选择）

工程化考量：
1. 模块化设计：将不同解法封装为静态方法
2. 异常处理：完善的参数验证和错误处理
3. 性能监控：包含性能测试和时间测量
4. 测试覆盖：包含各种边界情况和性能测试

面试要点：
1. 理解分组背包+模数运算的组合
2. 掌握模数运算的处理技巧
3. 了解不同DP实现的空间优化
4. 能够分析算法的时空复杂度

扩展应用：
1. 数论问题中的模数运算
2. 组合数学中的计数问题
3. 密码学中的模数运算
4. 分布式系统中的一致性哈希
"""