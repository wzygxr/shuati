#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
数的划分问题

问题描述：
将整数m分成n份，且每份不能为空，任意两个方案不相同（不考虑顺序）
例如，m=7、n=3，那么(1, 1, 5)、(1, 5, 1)、(5, 1, 1)被认为是同一种方法
返回有多少种不同的划分方法

测试链接：https://www.luogu.com.cn/problem/P1025

算法思路：
这是一个经典的整数分拆问题，可以用动态规划或记忆化搜索来解决

问题转化：
原始问题等价于求将整数m分成恰好n个正整数之和的方案数，不考虑顺序
为了简化计算，我们可以将问题转化为将整数m-n分成最多n个非负整数之和的方案数
（通过先给每个部分分配1，然后再分配剩下的m-n）

状态定义：
f(m, n)表示将整数m分成最多n个非负整数之和的方案数

状态转移方程：
1. 当n > m时：f(m, n) = f(m, m)，因为最多只能分成m个非零部分
2. 当n ≤ m时：f(m, n) = f(m, n-1) + f(m-n, n)
   - f(m, n-1)：不使用第n个部分（即最多使用n-1个部分）
   - f(m-n, n)：使用第n个部分（给每个部分至少分配1，然后分配剩下的m-n）

边界条件：
- f(0, n) = 1（只有一种方式将0分成任意数量的部分：所有部分都是0）
- f(m, 0) = 0（不能将正整数分成0个部分）

时间复杂度：O(m*n)
空间复杂度：O(m*n)

输入输出示例：
输入：7 3
输出：4
解释：有4种不同的划分方法：(1, 1, 5), (1, 2, 4), (1, 3, 3), (2, 2, 3)

整数分拆理论背景：
这个问题与组合数学中的Partition函数相关，但更具体地是求将m分成恰好n个部分的Partition数
可以通过生成函数的方法进一步研究，生成函数为：x^n * (1/(1-x)) * (1/(1-x^2)) * ... * (1/(1-x^n))
"""

import sys
from functools import lru_cache

# 定义常量
MAXN = 201  # 最大整数m的可能值，根据问题约束为200
MAXK = 7    # 最大划分份数n的可能值，根据问题约束为6

class SplitNumber:
    """
    数的划分问题求解类
    提供记忆化搜索和迭代动态规划两种解法
    """
    
    @staticmethod
    @lru_cache(maxsize=None)
    def dfs(m, n):
        """
        记忆化搜索函数：计算将整数m分成最多n个非负整数之和的方案数
        
        Args:
            m: 要分配的整数（可以为0）
            n: 最多可使用的份数
            
        Returns:
            方案数
        """
        # 边界条件1：将0分成任意数量的部分，只有一种方式
        if m == 0:
            return 1
        # 边界条件2：不能将正整数分成0个部分
        if n == 0:
            return 0
        
        # 状态转移
        if n > m:
            # 如果份数大于要分配的数，最多只能分成m个非零部分
            # 因为剩下的n-m个部分必须全为0，不影响方案数
            return SplitNumber.dfs(m, m)
        else:
            # 两种情况的和：
            # 1. 不使用第n个部分：dfs(m, n-1)
            # 2. 使用第n个部分，至少分配1：dfs(m-n, n)
            return SplitNumber.dfs(m, n - 1) + SplitNumber.dfs(m - n, n)
    
    @staticmethod
    def compute(m, n):
        """
        计算将整数m分成恰好n个正整数之和的方案数（不考虑顺序）
        使用Python的lru_cache进行记忆化搜索
        
        Args:
            m: 要划分的整数
            n: 要分成的份数
            
        Returns:
            不同的划分方法数
        """
        # 特殊情况处理
        # 如果m < n，不可能将m分成n个非空部分
        if m < n:
            return 0
        # 如果m == n，只有一种分法：每个部分都是1
        if m == n:
            return 1
        
        # 问题转化：
        # 先给每个部分分配1，剩下m-n需要分配到n个部分（可以为0）
        m -= n
        
        # 使用Python内置的lru_cache进行记忆化搜索
        return SplitNumber.dfs(m, n)
    
    @staticmethod
    def compute_dp(m, n):
        """
        使用动态规划迭代方法计算结果
        
        Args:
            m: 要划分的整数
            n: 要分成的份数
            
        Returns:
            不同的划分方法数
        """
        # 特殊情况处理
        if m < n:
            return 0
        if m == n:
            return 1
        
        # 问题转化
        m -= n
        
        # 初始化DP表
        # dp[i][j]表示将整数i分成最多j个非负整数之和的方案数
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        # 初始化边界条件：将0分成任意数量的部分，只有一种方式
        for j in range(n + 1):
            dp[0][j] = 1
        
        # 填充dp表
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if j > i:
                    # 如果份数大于要分配的数，最多只能分成i个非零部分
                    dp[i][j] = dp[i][i]
                else:
                    # 状态转移方程
                    dp[i][j] = dp[i][j-1] + dp[i-j][j]
        
        return dp[m][n]

"""
Python工程化实战建议：

1. 代码结构优化：
   - 使用类封装相关功能，提高代码的可维护性和复用性
   - 遵循Python的PEP 8规范进行代码风格统一
   - 使用文档字符串详细说明函数功能、参数和返回值

2. 性能优化策略：
   - 使用functools.lru_cache进行自动记忆化，简化代码
   - 对于大规模数据，考虑使用迭代版本避免递归深度问题
   - 使用列表推导式和生成器表达式提高效率
   - 避免在循环中进行不必要的函数调用

3. 内存管理：
   - 合理使用数据结构，避免不必要的内存消耗
   - 对于大规模数据，可以考虑使用numpy进行数组操作
   - 使用弱引用避免内存泄漏
   - 考虑使用滚动数组优化空间复杂度

4. 异常处理：
   - 添加适当的输入验证和异常处理
   - 使用try-except语句处理可能的错误
   - 定义自定义异常类以便更好地处理特定错误

5. 测试策略：
   - 使用unittest或pytest框架编写单元测试
   - 测试边界条件和特殊情况
   - 使用参数化测试覆盖多种输入组合
   - 进行性能测试以优化算法

6. Python特有的优化技巧：
   - 使用内置装饰器如lru_cache进行记忆化
   - 利用Python的动态特性简化代码
   - 使用列表推导式代替显式循环
   - 使用生成器节省内存
   - 使用适当的数据结构（如集合、字典）提高查询效率

7. 可读性提升：
   - 使用有意义的变量名和函数名
   - 添加适当的注释解释复杂逻辑
   - 保持函数的单一职责原则
   - 使用空白行和缩进提高代码可读性
"""

"""
类似题目与训练拓展：

1. LeetCode 343 - Integer Break
   链接：https://leetcode.cn/problems/integer-break/
   区别：将整数拆分成至少两个正整数的和，求乘积的最大值
   算法：动态规划或数学推导
   
2. LeetCode 279 - Perfect Squares
   链接：https://leetcode.cn/problems/perfect-squares/
   区别：将整数拆分成完全平方数的和，求最少的个数
   算法：BFS或动态规划
   
3. LeetCode 518 - Coin Change II
   链接：https://leetcode.cn/problems/coin-change-ii/
   区别：使用给定面额的硬币组成指定金额，求组合数
   算法：动态规划
   
4. LeetCode 322 - Coin Change
   链接：https://leetcode.cn/problems/coin-change/
   区别：使用给定面额的硬币组成指定金额，求最少硬币数
   算法：动态规划或BFS
   
5. 洛谷 P1044 - 栈
   链接：https://www.luogu.com.cn/problem/P1044
   区别：计算合法的入栈出栈序列数（卡特兰数）
   算法：动态规划
   
6. 洛谷 P1028 - 数的计算
   链接：https://www.luogu.com.cn/problem/P1028
   区别：计算满足特定条件的数的个数
   算法：递归或动态规划
   
7. 洛谷 P2404 - 自然数的拆分问题
   链接：https://www.luogu.com.cn/problem/P2404
   区别：输出所有的拆分方案
   算法：递归回溯
   
8. 牛客网 NC14299 - 整数划分
   链接：https://www.nowcoder.com/practice/a3fb363a90c241d696543e9c7817a1e0
   区别：求划分方法数，考虑不同的模数
   算法：动态规划
"""

"""
算法本质与技巧总结：

1. 问题转化的艺术：
   - 将"分成n个正整数"转化为"先各分1，再分剩下的m-n到n个非负整数"
   - 这种转化大大简化了问题的处理
   
2. 动态规划的核心思想：
   - 最优子结构：问题的解可以由子问题的解构成
   - 无后效性：当前状态的计算只依赖于之前的状态
   - 重叠子问题：使用记忆化避免重复计算
   
3. 状态设计的巧妙之处：
   - f(m, n)表示将m分成最多n个部分的方案数
   - 这种设计使得状态转移方程更加简洁
   
4. 组合数学中的整数分拆理论：
   - 与Partition函数密切相关
   - 可以通过生成函数进一步研究
   - 划分函数p(n, k)表示将n分成最多k个部分的方法数
   
5. 递归与记忆化搜索：
   - 递归实现简洁直观
   - 记忆化避免重复计算
   - 在Python中使用lru_cache非常方便
   - 对于大规模数据，迭代实现可能更高效
   
6. 边界条件的重要性：
   - 正确处理边界条件是动态规划成功的关键
   - 本题中的边界条件f(0, n)=1和f(m, 0)=0
   - 特殊情况m < n和m = n的处理
"""

# 主函数，用于处理输入输出
if __name__ == "__main__":
    # 读取输入
    m, n = map(int, sys.stdin.readline().split())
    
    # 使用记忆化搜索方法
    result1 = SplitNumber.compute(m, n)
    print("记忆化搜索结果:", result1)
    
    # 使用动态规划方法
    result2 = SplitNumber.compute_dp(m, n)
    print("动态规划结果:", result2)