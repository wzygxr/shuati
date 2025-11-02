# 除数博弈 (LeetCode 1025)
# 题目来源：LeetCode 1025. Divisor Game - https://leetcode.com/problems/divisor-game/
# 题目描述：爱丽丝和鲍勃一起玩游戏，他们轮流行动。爱丽丝先手开局。
# 最初，黑板上有一个数字 n 。在每个玩家的回合，玩家需要执行以下操作：
# 1. 选出任一 x，满足 0 < x < n 且 n % x == 0 。
# 2. 用 n - x 替换黑板上的数字 n 。
# 如果玩家无法执行这些操作，就会输掉游戏。
# 只有在爱丽丝在游戏中取得胜利时才返回 true 。假设两个玩家都以最佳状态参与游戏。
#
# 算法核心思想：
# 1. 动态规划：dp[i]表示当数字为i时，先手玩家是否能获胜
# 2. 状态转移：dp[i] = 存在x使得i%x==0且dp[i-x]==false
# 3. 数学规律：当n为偶数时爱丽丝获胜，当n为奇数时爱丽丝失败
#
# 时间复杂度分析：
# 1. 动态规划版本：O(n^2) - 需要遍历每个数字及其因子
# 2. 数学解法：O(1) - 直接判断奇偶性
#
# 空间复杂度分析：
# 1. 动态规划版本：O(n) - 使用一维dp数组
# 2. 数学解法：O(1) - 不需要额外空间
#
# 工程化考量：
# 1. 异常处理：处理边界情况（n=0,1）
# 2. 性能优化：利用数学规律优化
# 3. 可读性：添加详细注释说明算法原理
# 4. 可扩展性：支持不同的游戏规则

from typing import List
import math

class Code22_DivisorGameLeetCode1025:
    
    @staticmethod
    def divisorGameDP(n: int) -> bool:
        """
        动态规划解法：解决除数博弈问题
        Args:
            n: 初始数字
        Returns:
            bool: 爱丽丝是否能获胜
        """
        # 异常处理：边界情况
        if n <= 1:
            return False  # n=0或1时，爱丽丝无法操作，失败
        
        # 创建dp数组，dp[i]表示当数字为i时，先手玩家是否能获胜
        dp = [False] * (n + 1)
        
        # 基础情况：n=1时，先手玩家无法操作，失败
        dp[1] = False
        
        # 从2开始递推
        for i in range(2, n + 1):
            # 遍历所有可能的因子x
            for x in range(1, i):
                # 检查x是否是i的因子
                if i % x == 0:
                    # 如果存在一个因子x，使得后手玩家在i-x位置失败，则当前玩家获胜
                    if not dp[i - x]:
                        dp[i] = True
                        break  # 找到一个获胜策略即可
        
        return dp[n]
    
    @staticmethod
    def divisorGameMath(n: int) -> bool:
        """
        数学解法：利用奇偶性规律
        时间复杂度：O(1)，空间复杂度：O(1)
        """
        # 数学规律证明：
        # 1. 当n=1时，爱丽丝无法操作，失败
        # 2. 当n=2时，爱丽丝取x=1，n变为1，鲍勃无法操作，爱丽丝获胜
        # 3. 当n=3时，爱丽丝只能取x=1，n变为2，鲍勃获胜
        # 4. 当n=4时，爱丽丝可以取x=1或2
        #    - 取x=1：n变为3，鲍勃面对3会输
        #    - 取x=2：n变为2，鲍勃面对2会赢
        #    爱丽丝选择x=1获胜
        # 规律：当n为偶数时爱丽丝获胜，当n为奇数时爱丽丝失败
        
        return n % 2 == 0
    
    @staticmethod
    def divisorGameMemo(n: int) -> bool:
        """
        优化版本：使用记忆化搜索
        时间复杂度：O(n^2)，空间复杂度：O(n)
        """
        if n <= 1:
            return False
        
        # 记忆化字典
        memo = {}
        
        def dfs(current: int) -> bool:
            # 基础情况：n=1时无法操作，失败
            if current == 1:
                return False
            
            # 检查记忆化字典
            if current in memo:
                return memo[current]
            
            # 遍历所有可能的因子x
            for x in range(1, current):
                if current % x == 0:
                    # 如果后手玩家在current-x位置失败，则当前玩家获胜
                    if not dfs(current - x):
                        memo[current] = True
                        return True
            
            memo[current] = False
            return False
        
        return dfs(n)
    
    @staticmethod
    def divisorGameOptimized(n: int) -> bool:
        """
        迭代优化版本：避免重复计算
        时间复杂度：O(n^2)，空间复杂度：O(n)
        """
        if n <= 1:
            return False
        
        dp = [False] * (n + 1)
        dp[1] = False
        
        for i in range(2, n + 1):
            # 优化：只需要遍历到sqrt(i)即可
            for x in range(1, int(math.sqrt(i)) + 1):
                if i % x == 0:
                    # 检查因子x
                    if not dp[i - x]:
                        dp[i] = True
                        break
                    # 检查对应的因子i//x（如果不同）
                    y = i // x
                    if y != x and y < i:
                        if not dp[i - y]:
                            dp[i] = True
                            break
        
        return dp[n]

# 测试函数
def main():
    # 测试用例1：n=2，爱丽丝获胜
    print(f"n=2 (动态规划): {Code22_DivisorGameLeetCode1025.divisorGameDP(2)}")  # 应输出True
    print(f"n=2 (数学解法): {Code22_DivisorGameLeetCode1025.divisorGameMath(2)}")  # 应输出True
    
    # 测试用例2：n=3，爱丽丝失败
    print(f"n=3 (动态规划): {Code22_DivisorGameLeetCode1025.divisorGameDP(3)}")  # 应输出False
    print(f"n=3 (数学解法): {Code22_DivisorGameLeetCode1025.divisorGameMath(3)}")  # 应输出False
    
    # 测试用例3：n=4，爱丽丝获胜
    print(f"n=4 (动态规划): {Code22_DivisorGameLeetCode1025.divisorGameDP(4)}")  # 应输出True
    print(f"n=4 (数学解法): {Code22_DivisorGameLeetCode1025.divisorGameMath(4)}")  # 应输出True
    
    # 测试用例4：n=5，爱丽丝失败
    print(f"n=5 (动态规划): {Code22_DivisorGameLeetCode1025.divisorGameDP(5)}")  # 应输出False
    print(f"n=5 (数学解法): {Code22_DivisorGameLeetCode1025.divisorGameMath(5)}")  # 应输出False
    
    # 测试用例5：n=6，爱丽丝获胜
    print(f"n=6 (动态规划): {Code22_DivisorGameLeetCode1025.divisorGameDP(6)}")  # 应输出True
    print(f"n=6 (数学解法): {Code22_DivisorGameLeetCode1025.divisorGameMath(6)}")  # 应输出True
    
    # 验证记忆化搜索版本
    print("记忆化搜索版本测试:")
    print(f"n=2: {Code22_DivisorGameLeetCode1025.divisorGameMemo(2)}")
    print(f"n=3: {Code22_DivisorGameLeetCode1025.divisorGameMemo(3)}")
    print(f"n=4: {Code22_DivisorGameLeetCode1025.divisorGameMemo(4)}")
    
    # 验证优化版本
    print("优化版本测试:")
    print(f"n=2: {Code22_DivisorGameLeetCode1025.divisorGameOptimized(2)}")
    print(f"n=3: {Code22_DivisorGameLeetCode1025.divisorGameOptimized(3)}")
    print(f"n=4: {Code22_DivisorGameLeetCode1025.divisorGameOptimized(4)}")
    
    # 边界测试：n=1
    print(f"n=1 (边界测试): {Code22_DivisorGameLeetCode1025.divisorGameDP(1)}")  # 应输出False
    
    # 性能测试：较大数字
    print(f"n=1000 (性能测试): {Code22_DivisorGameLeetCode1025.divisorGameMath(1000)}")  # 应输出True

if __name__ == "__main__":
    main()