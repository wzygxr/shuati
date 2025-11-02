import math
from typing import List
import sys
from collections import deque

# LeetCode 279. 完全平方数
# 题目描述：给定正整数 n，找到若干个完全平方数（比如 1, 4, 9, 16, ...）使得它们的和等于 n。
# 你需要让组成和的完全平方数的个数最少。
# 链接：https://leetcode.cn/problems/perfect-squares/
# 
# 解题思路：
# 这是一个完全背包问题，其中：
# - 背包容量：正整数 n
# - 物品：完全平方数（1, 4, 9, 16, ...）
# - 每个物品可以无限次使用（完全背包）
# - 目标：使用最少数量的物品（完全平方数）装满背包
# 
# 状态定义：dp[i] 表示和为 i 的完全平方数的最少数量
# 状态转移方程：dp[i] = min(dp[i], dp[i - j*j] + 1)，其中 j*j <= i
# 初始状态：dp[0] = 0，dp[i] = float('inf')（表示不可达）
# 
# 时间复杂度：O(n * √n)，其中 n 是给定的正整数
# 空间复杂度：O(n)，使用一维DP数组
# 
# 工程化考量：
# 1. 异常处理：处理 n <= 0 的情况
# 2. 边界条件：n=0时返回0，n=1时返回1
# 3. 性能优化：预先生成完全平方数列表
# 4. 可读性：清晰的变量命名和注释
# 5. Python特性：使用类型注解提高代码可读性

class PerfectSquares:
    """
    完全平方数问题的多种解法
    """
    
    @staticmethod
    def num_squares_dp(n: int) -> int:
        """
        动态规划解法 - 完全背包问题
        
        Args:
            n: 目标正整数
            
        Returns:
            int: 组成n的最少完全平方数个数
            
        Raises:
            ValueError: 当n <= 0时抛出异常
        """
        # 参数验证
        if n <= 0:
            raise ValueError("n must be positive")
        
        # 特殊情况处理
        if n == 1:
            return 1
        
        # 创建DP数组，dp[i]表示和为i的最少完全平方数个数
        dp = [float('inf')] * (n + 1)
        dp[0] = 0
        
        # 遍历所有可能的完全平方数
        i = 1
        while i * i <= n:
            square = i * i
            # 完全背包：正序遍历容量
            for j in range(square, n + 1):
                if dp[j - square] != float('inf'):
                    dp[j] = min(dp[j], dp[j - square] + 1)
            i += 1
        
        return dp[n] if dp[n] != float('inf') else -1
    
    @staticmethod
    def num_squares_optimized(n: int) -> int:
        """
        优化的动态规划解法 - 预先生成完全平方数列表
        
        Args:
            n: 目标正整数
            
        Returns:
            int: 组成n的最少完全平方数个数
        """
        if n <= 0:
            raise ValueError("n must be positive")
        
        # 预先生成所有可能的完全平方数
        max_square_root = int(math.sqrt(n))
        squares = [i * i for i in range(1, max_square_root + 1)]
        
        dp = [float('inf')] * (n + 1)
        dp[0] = 0
        
        # 先遍历物品（完全平方数），再遍历容量
        for square in squares:
            for j in range(square, n + 1):
                if dp[j - square] != float('inf'):
                    dp[j] = min(dp[j], dp[j - square] + 1)
        
        return dp[n] if dp[n] != float('inf') else -1
    
    @staticmethod
    def _is_perfect_square(x: int) -> bool:
        """
        判断一个数是否是完全平方数
        
        Args:
            x: 要判断的数
            
        Returns:
            bool: 如果是完全平方数返回True，否则返回False
        """
        sqrt_val = int(math.sqrt(x))
        return sqrt_val * sqrt_val == x
    
    @staticmethod
    def _check_legendre_three_square(n: int) -> bool:
        """
        检查是否满足勒让德三平方定理的排除条件
        即 n = 4^a(8b+7)
        
        Args:
            n: 要检查的数
            
        Returns:
            bool: 如果满足排除条件返回True，否则返回False
        """
        while n % 4 == 0:
            n //= 4
        return n % 8 == 7
    
    @staticmethod
    def num_squares_math(n: int) -> int:
        """
        数学解法 - 利用四平方定理
        拉格朗日四平方定理：每个正整数都可以表示为4个整数的平方和
        勒让德三平方定理：当且仅当n≠4^a(8b+7)时，n可以表示为3个整数的平方和
        
        Args:
            n: 目标正整数
            
        Returns:
            int: 组成n的最少完全平方数个数
        """
        # 检查n是否是完全平方数
        if PerfectSquares._is_perfect_square(n):
            return 1
        
        # 检查是否满足勒让德三平方定理的排除条件
        if PerfectSquares._check_legendre_three_square(n):
            return 4
        
        # 检查是否可以表示为两个平方数之和
        i = 1
        while i * i <= n:
            j = n - i * i
            if PerfectSquares._is_perfect_square(j):
                return 2
            i += 1
        
        # 其他情况返回3
        return 3
    
    @staticmethod
    def num_squares_bfs(n: int) -> int:
        """
        BFS解法 - 将问题转化为图的最短路径问题
        每个数字是一个节点，如果两个数字相差一个完全平方数，则它们之间有边
        
        Args:
            n: 目标正整数
            
        Returns:
            int: 组成n的最少完全平方数个数
        """
        if n <= 0:
            raise ValueError("n must be positive")
        
        # 使用队列进行BFS
        queue = deque()
        # 记录到达每个数字的最短步数
        steps = [-1] * (n + 1)
        
        # 从0开始
        queue.append(0)
        steps[0] = 0
        
        while queue:
            current = queue.popleft()
            
            # 尝试所有可能的完全平方数
            i = 1
            while i * i <= n - current:
                next_val = current + i * i
                
                # 如果超出范围或已经访问过，跳过
                if next_val > n or steps[next_val] != -1:
                    i += 1
                    continue
                
                steps[next_val] = steps[current] + 1
                
                # 如果到达目标，直接返回
                if next_val == n:
                    return steps[next_val]
                
                queue.append(next_val)
                i += 1
        
        return steps[n]
    
    @staticmethod
    def run_tests():
        """
        运行测试用例，验证所有方法的正确性
        """
        # 测试用例
        test_cases = [12, 13, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]
        
        print("完全平方数问题测试：")
        for n in test_cases:
            try:
                result1 = PerfectSquares.num_squares_dp(n)
                result2 = PerfectSquares.num_squares_optimized(n)
                result3 = PerfectSquares.num_squares_bfs(n)
                result4 = PerfectSquares.num_squares_math(n)
                
                print(f"n={n}: DP={result1}, Optimized={result2}, BFS={result3}, Math={result4}")
                
                # 验证所有方法结果一致
                if result1 != result2 or result2 != result3 or result3 != result4:
                    print("警告：不同方法结果不一致！")
                    
            except Exception as e:
                print(f"测试n={n}时发生错误: {e}")
        
        # 性能测试
        import time
        start_time = time.time()
        large_result = PerfectSquares.num_squares_dp(10000)
        end_time = time.time()
        print(f"n=10000 的结果: {large_result}, 耗时: {end_time - start_time:.4f}秒")

def main():
    """
    主函数 - 运行测试和演示
    """
    try:
        PerfectSquares.run_tests()
    except Exception as e:
        print(f"程序执行错误: {e}")
        return 1
    return 0

if __name__ == "__main__":
    sys.exit(main())

"""
复杂度分析：

方法1：动态规划（完全背包）
- 时间复杂度：O(n * √n)
  - 外层循环：√n 次（完全平方数的个数）
  - 内层循环：n 次（背包容量）
- 空间复杂度：O(n)

方法2：优化的动态规划
- 时间复杂度：O(n * √n)（与方法1相同，但常数更小）
- 空间复杂度：O(n)

方法3：数学解法
- 时间复杂度：O(√n)
  - 检查完全平方数：O(1)
  - 检查勒让德条件：O(log n)
  - 检查两个平方数之和：O(√n)
- 空间复杂度：O(1)

方法4：BFS解法
- 时间复杂度：O(n * √n)（最坏情况）
- 空间复杂度：O(n)

最优解分析：
- 对于小规模n（n < 1000）：所有方法都很快
- 对于大规模n（n >= 10000）：数学解法最优，时间复杂度最低
- 在实际工程中：推荐使用动态规划，代码清晰易懂

Python特定优化：
1. 使用类型注解提高代码可读性
2. 使用deque进行BFS，效率更高
3. 使用math.sqrt进行平方根计算
4. 使用float('inf')表示无穷大
5. 异常处理使用Python标准异常

边界场景测试：
1. n=0：应该返回0（根据题目定义，n是正整数，但需要处理边界）
2. n=1：应该返回1（1本身就是完全平方数）
3. n=2：应该返回2（1+1）
4. n=3：应该返回3（1+1+1）
5. n=4：应该返回1（4本身就是完全平方数）
6. n=12：应该返回3（4+4+4）
7. n=13：应该返回2（4+9）

工程化考量：
1. 模块化设计：将不同解法封装为静态方法
2. 类型注解：提高代码可读性和可维护性
3. 异常处理：对非法输入抛出明确异常
4. 测试覆盖：包含正常情况、边界情况、性能测试
5. 文档完善：详细的docstring和注释
"""