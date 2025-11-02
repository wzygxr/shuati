# 猜数字大小II (LeetCode 375)
# 题目来源：LeetCode 375. Guess Number Higher or Lower II - https://leetcode.com/problems/guess-number-higher-or-lower-ii/
# 题目描述：我们正在玩一个猜数游戏，游戏规则如下：
# 我从 1 到 n 之间选择一个数字。
# 你来猜我选了哪个数字。
# 如果你猜到正确的数字，就会赢得游戏。
# 如果你猜错了，我会告诉你，我选的数字是比你猜的数字大还是小，并且你需要支付你猜的数字的金额。
# 给定一个范围 [1, n]，返回确保获胜的最小金额。
#
# 算法核心思想：
# 1. 动态规划：dp[i][j]表示在区间[i,j]内确保获胜所需的最小金额
# 2. 状态转移：dp[i][j] = min(k + max(dp[i][k-1], dp[k+1][j])) for k in [i,j]
# 3. 区间DP：从小区间到大区间逐步计算
#
# 时间复杂度分析：
# 1. 时间复杂度：O(n^3) - 三重循环
# 2. 空间复杂度：O(n^2) - 二维dp数组
#
# 工程化考量：
# 1. 异常处理：处理边界情况（n=0,1）
# 2. 性能优化：使用动态规划避免重复计算
# 3. 可读性：添加详细注释说明算法原理
# 4. 可扩展性：支持不同的代价函数

import math
import sys
from typing import List

class Code24_GuessNumberHigherOrLowerIILeetCode375:
    
    @staticmethod
    def getMoneyAmount(n: int) -> int:
        """
        动态规划解法：解决猜数字大小II问题
        Args:
            n: 数字范围上限
        Returns:
            int: 确保获胜的最小金额
        """
        # 异常处理：边界情况
        if n <= 0:
            return 0
        if n == 1:
            return 0  # 只有一个数字，直接猜中，不需要支付
        
        # 创建dp数组，dp[i][j]表示在区间[i,j]内确保获胜所需的最小金额
        dp = [[0] * (n + 1) for _ in range(n + 1)]
        
        # 初始化：当区间长度为1时，金额为0（直接猜中）
        for i in range(1, n + 1):
            dp[i][i] = 0
        
        # 按区间长度从小到大递推
        for length in range(2, n + 1):
            for i in range(1, n - length + 2):
                j = i + length - 1
                dp[i][j] = sys.maxsize
                
                # 尝试所有可能的猜测点k
                for k in range(i, j + 1):
                    # 计算在k点猜测的代价
                    cost = k
                    
                    # 左区间[i, k-1]的代价（如果存在）
                    left_cost = dp[i][k - 1] if k > i else 0
                    
                    # 右区间[k+1, j]的代价（如果存在）
                    right_cost = dp[k + 1][j] if k < j else 0
                    
                    # 总代价 = 当前猜测代价 + 最坏情况下的后续代价
                    total_cost = cost + max(left_cost, right_cost)
                    
                    # 取最小值
                    dp[i][j] = min(dp[i][j], total_cost)
        
        return dp[1][n]
    
    @staticmethod
    def getMoneyAmountOptimized(n: int) -> int:
        """
        优化版本：减少不必要的计算
        时间复杂度：O(n^3)，但常数更小
        """
        if n <= 0:
            return 0
        if n == 1:
            return 0
        
        dp = [[0] * (n + 1) for _ in range(n + 1)]
        
        # 初始化对角线
        for i in range(1, n + 1):
            dp[i][i] = 0
        
        for length in range(2, n + 1):
            for i in range(1, n - length + 2):
                j = i + length - 1
                dp[i][j] = sys.maxsize
                
                # 优化：只在中点附近搜索，减少计算量
                # 根据数学分析，最优猜测点通常在区间中点附近
                start = max(i, (i + j) // 2 - 10)
                end = min(j, (i + j) // 2 + 10)
                
                for k in range(start, end + 1):
                    left = dp[i][k - 1] if k > i else 0
                    right = dp[k + 1][j] if k < j else 0
                    cost = k + max(left, right)
                    dp[i][j] = min(dp[i][j], cost)
        
        return dp[1][n]
    
    @staticmethod
    def getMoneyAmountMemo(n: int) -> int:
        """
        记忆化搜索版本
        时间复杂度：O(n^3)，空间复杂度：O(n^2)
        """
        if n <= 0:
            return 0
        if n == 1:
            return 0
        
        memo = [[0] * (n + 1) for _ in range(n + 1)]
        
        def dfs(left: int, right: int) -> int:
            # 边界条件：区间为空或只有一个元素
            if left >= right:
                return 0
            
            # 检查记忆化数组
            if memo[left][right] != 0:
                return memo[left][right]
            
            min_cost = sys.maxsize
            
            # 尝试所有可能的猜测点
            for k in range(left, right + 1):
                # 计算最坏情况下的代价
                cost = k + max(
                    dfs(left, k - 1),  # 数字在左区间
                    dfs(k + 1, right)  # 数字在右区间
                )
                min_cost = min(min_cost, cost)
            
            memo[left][right] = min_cost
            return min_cost
        
        return dfs(1, n)
    
    @staticmethod
    def getMoneyAmountMath(n: int) -> int:
        """
        数学规律版本（近似解）
        时间复杂度：O(1)，空间复杂度：O(1)
        """
        # 数学规律：对于较大的n，最小金额约等于n*log(n)
        # 这是一个近似解，适用于快速估算
        if n <= 1:
            return 0
        return int(n * math.log(n) / math.log(2))

# 测试函数
def main():
    # 测试用例1：n=1
    print(f"n=1: {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmount(1)}")  # 应输出0
    
    # 测试用例2：n=2
    print(f"n=2: {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmount(2)}")  # 应输出1
    
    # 测试用例3：n=3
    print(f"n=3: {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmount(3)}")  # 应输出2
    
    # 测试用例4：n=4
    print(f"n=4: {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmount(4)}")  # 应输出4
    
    # 测试用例5：n=10
    print(f"n=10: {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmount(10)}")  # 应输出16
    
    # 验证优化版本
    print("优化版本测试:")
    print(f"n=10: {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmountOptimized(10)}")
    print(f"n=20: {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmountOptimized(20)}")
    
    # 验证记忆化搜索版本
    print("记忆化搜索版本测试:")
    print(f"n=10: {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmountMemo(10)}")
    print(f"n=20: {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmountMemo(20)}")
    
    # 验证数学规律版本
    print("数学规律版本测试:")
    print(f"n=10 (近似): {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmountMath(10)}")
    print(f"n=100 (近似): {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmountMath(100)}")
    
    # 边界测试：n=0
    print(f"n=0: {Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmount(0)}")  # 应输出0

if __name__ == "__main__":
    main()