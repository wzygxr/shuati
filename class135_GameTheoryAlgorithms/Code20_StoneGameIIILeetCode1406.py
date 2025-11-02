# 石子游戏III (LeetCode 1406)
# 题目来源：LeetCode 1406. Stone Game III - https://leetcode.com/problems/stone-game-iii/
# 题目描述：爱丽丝和鲍勃用几堆石子做游戏。几堆石子排成一行，每堆都有正整数颗石子 piles[i]。
# 游戏以谁手中的石子最多来决出胜负。爱丽丝先开始。
# 在每个玩家的回合中，该玩家可以拿走剩下的石子堆的前 1、2 或 3 堆。
# 游戏持续到所有石子堆都被拿走。
# 假设爱丽丝和鲍勃都发挥出最佳水平，返回游戏结果。
#
# 算法核心思想：
# 1. 动态规划：dp[i]表示从第i堆开始，当前玩家能获得的最大净胜分数
# 2. 状态转移：dp[i] = max(当前玩家拿1堆 - dp[i+1], 拿2堆 - dp[i+2], 拿3堆 - dp[i+3])
# 3. 最终结果：根据dp[0]的值判断胜负
#
# 时间复杂度分析：
# 1. 时间复杂度：O(n) - 线性遍历石子堆
# 2. 空间复杂度：O(n) - 使用一维dp数组
#
# 工程化考量：
# 1. 异常处理：处理空数组和边界情况
# 2. 性能优化：使用动态规划避免重复计算
# 3. 可读性：添加详细注释说明算法原理
# 4. 可扩展性：支持不同的取石子策略

from typing import List

class Code20_StoneGameIIILeetCode1406:
    
    @staticmethod
    def stoneGameIII(piles: List[int]) -> str:
        """
        解决石子游戏III问题
        Args:
            piles: 石子堆数组
        Returns:
            str: 游戏结果："Alice"、"Bob"或"Tie"
        """
        # 异常处理：处理空数组
        if not piles:
            return "Tie"
        
        n = len(piles)
        
        # 创建dp数组，dp[i]表示从第i堆开始，当前玩家能获得的最大净胜分数
        dp = [0] * (n + 1)
        
        # 从后向前递推
        for i in range(n - 1, -1, -1):
            # 当前玩家拿1堆石子
            take1 = piles[i] - dp[i + 1]
            
            # 当前玩家拿2堆石子（如果可能）
            take2 = float('-inf')
            if i + 1 < n:
                take2 = piles[i] + piles[i + 1] - dp[i + 2]
            
            # 当前玩家拿3堆石子（如果可能）
            take3 = float('-inf')
            if i + 2 < n:
                take3 = piles[i] + piles[i + 1] + piles[i + 2] - dp[i + 3]
            
            # 当前玩家选择最优策略
            dp[i] = max(take1, take2, take3)
        
        # 根据dp[0]的值判断胜负
        if dp[0] > 0:
            return "Alice"
        elif dp[0] < 0:
            return "Bob"
        else:
            return "Tie"
    
    @staticmethod
    def stoneGameIIIOptimized(piles: List[int]) -> str:
        """
        优化版本：使用前缀和简化计算
        时间复杂度：O(n)，空间复杂度：O(n)
        """
        if not piles:
            return "Tie"
        
        n = len(piles)
        
        # 创建前缀和数组
        prefix_sum = [0] * (n + 1)
        for i in range(1, n + 1):
            prefix_sum[i] = prefix_sum[i - 1] + piles[i - 1]
        
        # 创建dp数组
        dp = [0] * (n + 1)
        
        # 从后向前递推
        for i in range(n - 1, -1, -1):
            max_score = float('-inf')
            
            # 尝试拿1、2、3堆石子
            for x in range(1, 4):
                if i + x > n:
                    break
                
                # 当前玩家拿x堆石子获得的总分数
                current_score = prefix_sum[i + x] - prefix_sum[i]
                # 对手从i+x位置开始的最优解
                opponent_score = dp[i + x] if i + x < n else 0
                # 当前玩家的净胜分数
                max_score = max(max_score, current_score - opponent_score)
            
            dp[i] = max_score
        
        # 根据dp[0]的值判断胜负
        if dp[0] > 0:
            return "Alice"
        elif dp[0] < 0:
            return "Bob"
        else:
            return "Tie"
    
    @staticmethod
    def stoneGameIIISpaceOptimized(piles: List[int]) -> str:
        """
        空间优化版本：使用滚动数组降低空间复杂度
        时间复杂度：O(n)，空间复杂度：O(1)
        """
        if not piles:
            return "Tie"
        
        n = len(piles)
        
        # 使用滚动数组，只需要保存最近3个状态
        dp1, dp2, dp3 = 0, 0, 0
        
        # 从后向前递推
        for i in range(n - 1, -1, -1):
            max_score = float('-inf')
            
            # 尝试拿1堆石子
            max_score = max(max_score, piles[i] - dp1)
            
            # 尝试拿2堆石子（如果可能）
            if i + 1 < n:
                max_score = max(max_score, piles[i] + piles[i + 1] - dp2)
            
            # 尝试拿3堆石子（如果可能）
            if i + 2 < n:
                max_score = max(max_score, piles[i] + piles[i + 1] + piles[i + 2] - dp3)
            
            # 更新滚动数组
            dp3, dp2, dp1 = dp2, dp1, max_score
        
        # 根据最终结果判断胜负
        if dp1 > 0:
            return "Alice"
        elif dp1 < 0:
            return "Bob"
        else:
            return "Tie"

# 测试函数
def main():
    # 测试用例1：标准情况
    piles1 = [1, 2, 3, 7]
    print(f"测试用例1 [1,2,3,7]: {Code20_StoneGameIIILeetCode1406.stoneGameIII(piles1)}")  # 应输出"Bob"
    
    # 测试用例2：平局
    piles2 = [1, 2, 3, 6]
    print(f"测试用例2 [1,2,3,6]: {Code20_StoneGameIIILeetCode1406.stoneGameIII(piles2)}")  # 应输出"Tie"
    
    # 测试用例3：爱丽丝获胜
    piles3 = [1, 2, 3, -1, -2, -3, 7]
    print(f"测试用例3 [1,2,3,-1,-2,-3,7]: {Code20_StoneGameIIILeetCode1406.stoneGameIII(piles3)}")  # 应输出"Alice"
    
    # 测试用例4：单堆石子
    piles4 = [10]
    print(f"测试用例4 [10]: {Code20_StoneGameIIILeetCode1406.stoneGameIII(piles4)}")  # 应输出"Alice"
    
    # 测试用例5：两堆石子
    piles5 = [3, 2]
    print(f"测试用例5 [3,2]: {Code20_StoneGameIIILeetCode1406.stoneGameIII(piles5)}")  # 应输出"Alice"
    
    # 验证优化版本
    print("优化版本测试:")
    print(f"测试用例1 [1,2,3,7]: {Code20_StoneGameIIILeetCode1406.stoneGameIIIOptimized(piles1)}")
    print(f"测试用例2 [1,2,3,6]: {Code20_StoneGameIIILeetCode1406.stoneGameIIIOptimized(piles2)}")
    
    # 验证空间优化版本
    print("空间优化版本测试:")
    print(f"测试用例1 [1,2,3,7]: {Code20_StoneGameIIILeetCode1406.stoneGameIIISpaceOptimized(piles1)}")
    print(f"测试用例2 [1,2,3,6]: {Code20_StoneGameIIILeetCode1406.stoneGameIIISpaceOptimized(piles2)}")
    
    # 边界测试：空数组
    empty_piles = []
    print(f"空数组测试: {Code20_StoneGameIIILeetCode1406.stoneGameIII(empty_piles)}")  # 应输出"Tie"

if __name__ == "__main__":
    main()