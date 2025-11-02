# 爬楼梯 (Climbing Stairs)
# 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
# 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
# 测试链接 : https://leetcode.cn/problems/climbing-stairs/

import time
from functools import lru_cache

class Solution:
    """
    爬楼梯问题 - 斐波那契数列的经典应用
    
    时间复杂度分析：
    - 暴力递归：O(2^n) 指数级，存在大量重复计算
    - 记忆化搜索：O(n) 每个状态只计算一次
    - 动态规划：O(n) 线性时间复杂度
    - 矩阵快速幂：O(log n) 最优解
    
    空间复杂度分析：
    - 暴力递归：O(n) 递归调用栈深度
    - 记忆化搜索：O(n) 递归栈 + 记忆化缓存
    - 动态规划：O(n) dp数组存储所有状态
    - 空间优化：O(1) 只保存必要的前两个状态
    
    工程化考量：
    1. 异常处理：处理n为负数或0的情况
    2. 边界测试：n=0,1,2等小数值
    3. 性能优化：选择空间优化版本应对大规模数据
    4. Python特性：利用装饰器简化记忆化实现
    """
    
    # 方法1：暴力递归解法
    # 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    # 空间复杂度：O(n) - 递归调用栈的深度
    # 问题：存在大量重复计算，n较大时栈溢出
    def climbStairs1(self, n: int) -> int:
        if n <= 0:
            return 0
        if n == 1:
            return 1
        if n == 2:
            return 2
        
        return self.climbStairs1(n - 1) + self.climbStairs1(n - 2)

    # 方法2：记忆化搜索（使用装饰器）
    # 时间复杂度：O(n) - 每个状态只计算一次
    # 空间复杂度：O(n) - 递归栈和缓存空间
    # 优化：通过缓存避免重复计算，Pythonic实现
    @lru_cache(maxsize=None)
    def climbStairs2(self, n: int) -> int:
        if n <= 0:
            return 0
        if n == 1:
            return 1
        if n == 2:
            return 2
        
        return self.climbStairs2(n - 1) + self.climbStairs2(n - 2)

    # 方法3：动态规划（自底向上）
    # 时间复杂度：O(n) - 从底向上计算每个状态
    # 空间复杂度：O(n) - dp数组存储所有状态
    # 优化：避免了递归调用的开销
    def climbStairs3(self, n: int) -> int:
        if n <= 0:
            return 0
        if n == 1:
            return 1
        if n == 2:
            return 2
        
        dp = [0] * (n + 1)
        dp[1] = 1
        dp[2] = 2
        
        for i in range(3, n + 1):
            dp[i] = dp[i - 1] + dp[i - 2]
        
        return dp[n]

    # 方法4：空间优化的动态规划
    # 时间复杂度：O(n) - 仍然需要计算所有状态
    # 空间复杂度：O(1) - 只保存必要的前两个状态值
    # 优化：大幅减少空间使用，工程首选
    def climbStairs4(self, n: int) -> int:
        if n <= 0:
            return 0
        if n == 1:
            return 1
        if n == 2:
            return 2
        
        prev1, prev2 = 1, 2  # dp[i-2], dp[i-1]
        
        for i in range(3, n + 1):
            current = prev1 + prev2
            prev1, prev2 = prev2, current
        
        return prev2

    # 方法5：矩阵快速幂（最优解）
    # 时间复杂度：O(log n) - 通过矩阵快速幂加速
    # 空间复杂度：O(1) - 常数空间
    # 核心思路：将递推关系转化为矩阵乘法，使用快速幂算法
    def climbStairs5(self, n: int) -> int:
        if n <= 0:
            return 0
        if n == 1:
            return 1
        if n == 2:
            return 2
        
        # 递推关系矩阵：[[1,1],[1,0]]
        base = [[1, 1], [1, 0]]
        result = self.matrix_power(base, n - 2)
        
        # 结果矩阵与初始状态相乘
        return result[0][0] * 2 + result[0][1] * 1
    
    def matrix_power(self, base: list, power: int) -> list:
        """矩阵快速幂算法"""
        result = [[1, 0], [0, 1]]  # 单位矩阵
        
        while power > 0:
            if power & 1:
                result = self.matrix_multiply(result, base)
            base = self.matrix_multiply(base, base)
            power >>= 1
        
        return result
    
    def matrix_multiply(self, a: list, b: list) -> list:
        """2x2矩阵乘法"""
        return [
            [a[0][0] * b[0][0] + a[0][1] * b[1][0], a[0][0] * b[0][1] + a[0][1] * b[1][1]],
            [a[1][0] * b[0][0] + a[1][1] * b[1][0], a[1][0] * b[0][1] + a[1][1] * b[1][1]]
        ]

def test_case(solution: Solution, n: int, expected: int, description: str):
    """测试用例函数"""
    result1 = solution.climbStairs1(n)
    result2 = solution.climbStairs2(n)
    result3 = solution.climbStairs3(n)
    result4 = solution.climbStairs4(n)
    result5 = solution.climbStairs5(n)
    
    all_correct = (result1 == expected and result2 == expected and 
                  result3 == expected and result4 == expected and result5 == expected)
    
    status = "✓" if all_correct else "✗"
    print(f"{description}: {status}")
    
    if not all_correct:
        print(f"  方法1: {result1} | 方法2: {result2} | 方法3: {result3} | "
              f"方法4: {result4} | 方法5: {result5} | 预期: {expected}")

def performance_test(solution: Solution, n: int):
    """性能测试函数"""
    print(f"\n性能测试 n={n}:")
    
    start = time.time()
    result3 = solution.climbStairs3(n)
    end = time.time()
    print(f"动态规划: {result3}, 耗时: {(end - start) * 1000:.2f}ms")
    
    start = time.time()
    result4 = solution.climbStairs4(n)
    end = time.time()
    print(f"空间优化: {result4}, 耗时: {(end - start) * 1000:.2f}ms")
    
    start = time.time()
    result5 = solution.climbStairs5(n)
    end = time.time()
    print(f"矩阵快速幂: {result5}, 耗时: {(end - start) * 1000:.2f}ms")

if __name__ == "__main__":
    solution = Solution()
    
    print("=== 爬楼梯问题测试 ===")
    
    # 边界测试
    test_case(solution, 0, 0, "n=0")
    test_case(solution, 1, 1, "n=1")
    test_case(solution, 2, 2, "n=2")
    
    # 常规测试
    test_case(solution, 3, 3, "n=3")
    test_case(solution, 4, 5, "n=4")
    test_case(solution, 5, 8, "n=5")
    test_case(solution, 10, 89, "n=10")
    
    # 性能对比测试（只测试高效方法）
    print("\n=== 性能对比测试 ===")
    performance_test(solution, 40)
    
    # 错误处理测试
    print("\n=== 错误处理测试 ===")
    try:
        result = solution.climbStairs4(-1)
        print(f"n=-1 结果: {result}")
    except Exception as e:
        print(f"n=-1 异常: {e}")

"""
算法总结与工程化思考：

1. 问题本质：斐波那契数列的变种，f(n) = f(n-1) + f(n-2)

2. 时间复杂度对比：
   - 暴力递归：O(2^n) - 不可接受
   - 记忆化搜索：O(n) - 可接受
   - 动态规划：O(n) - 推荐
   - 矩阵快速幂：O(log n) - 最优

3. 空间复杂度对比：
   - 暴力递归：O(n) - 栈深度
   - 记忆化搜索：O(n) - 递归栈+缓存
   - 动态规划：O(n) - 数组存储
   - 空间优化：O(1) - 工程首选

4. Python特性利用：
   - @lru_cache装饰器简化记忆化实现
   - 多重赋值语法简化变量交换
   - 列表推导式简化矩阵操作

5. 工程选择依据：
   - 小规模数据：任意方法都可
   - 大规模数据：空间优化版本或矩阵快速幂
   - 内存敏感：空间优化版本
   - 性能极致：矩阵快速幂

6. 调试技巧：
   - 打印中间状态验证递推关系
   - 边界测试确保正确性
   - 性能测试选择最优算法
"""