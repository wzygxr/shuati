# 石子游戏II (LeetCode 1140)
# 题目来源：LeetCode 1140. Stone Game II - https://leetcode.com/problems/stone-game-ii/
# 题目描述：爱丽丝和鲍勃继续他们的石子游戏。许多堆石子排成一行，每堆都有正整数颗石子 piles[i]。
# 游戏以谁手中的石子最多来决出胜负。爱丽丝先开始。
# 在每个玩家的回合中，该玩家可以拿走剩下的石子堆的前 X 堆，其中 1 <= X <= 2M。
# 然后，我们将 M 更新为 max(M, X)。游戏持续到所有石子堆都被拿走。
# 假设爱丽丝和鲍勃都发挥出最佳水平，返回爱丽丝可以得到的最大数量的石子。
#
# 算法核心思想：
# 1. 动态规划：dp[i][m]表示从第i堆开始，当前M值为m时，当前玩家能获得的最大石子数
# 2. 前缀和：使用前缀和数组快速计算区间和
# 3. 状态转移：dp[i][m] = max(当前玩家拿x堆 + 剩余石子总数 - 对手在i+x位置的最优解)
#
# 时间复杂度分析：
# 1. 时间复杂度：O(n^3) - 三重循环，但通过优化可以降低到O(n^2)
# 2. 空间复杂度：O(n^2) - 使用二维dp数组
#
# 工程化考量：
# 1. 异常处理：处理空数组和边界情况
# 2. 性能优化：使用前缀和和记忆化搜索
# 3. 可读性：添加详细注释说明算法原理
# 4. 可扩展性：支持不同的M值限制

from typing import List

class Code19_StoneGameIILeetCode1140:
    
    @staticmethod
    def stoneGameII(piles: List[int]) -> int:
        """
        解决石子游戏II问题
        Args:
            piles: 石子堆数组
        Returns:
            int: 爱丽丝可以得到的最大石子数
        """
        # 异常处理：处理空数组
        if not piles:
            return 0
        
        n = len(piles)
        
        # 创建前缀和数组，prefixSum[i]表示前i堆石子的总和
        prefixSum = [0] * (n + 1)
        for i in range(1, n + 1):
            prefixSum[i] = prefixSum[i - 1] + piles[i - 1]
        
        # 创建dp数组，dp[i][m]表示从第i堆开始，当前M值为m时，当前玩家能获得的最大石子数
        dp = [[0] * (n + 1) for _ in range(n + 1)]
        
        # 从后向前递推，因为后面的状态会影响前面的决策
        for i in range(n - 1, -1, -1):
            for m in range(1, n + 1):
                # 如果当前玩家可以拿走所有剩余石子
                if i + 2 * m >= n:
                    dp[i][m] = prefixSum[n] - prefixSum[i]
                    continue
                
                # 当前玩家尝试拿1到2*m堆石子
                max_stones = 0
                for x in range(1, 2 * m + 1):
                    if i + x > n:
                        break
                    
                    # 当前玩家拿x堆石子，获得prefixSum[i+x] - prefixSum[i]石子
                    # 对手从i+x位置开始，新的M值为max(m, x)
                    opponent_stones = dp[i + x][max(m, x)]
                    current_stones = prefixSum[i + x] - prefixSum[i]
                    
                    # 当前玩家能获得的最大石子数 = 当前拿的石子数 + 剩余总石子数 - 对手获得的最优解
                    max_stones = max(max_stones, current_stones + (prefixSum[n] - prefixSum[i + x] - opponent_stones))
                
                dp[i][m] = max_stones
        
        return dp[0][1]
    
    @staticmethod
    def stoneGameIIOptimized(piles: List[int]) -> int:
        """
        优化版本：使用记忆化搜索，避免重复计算
        时间复杂度：O(n^2)，空间复杂度：O(n^2)
        """
        if not piles:
            return 0
        
        n = len(piles)
        prefixSum = [0] * (n + 1)
        for i in range(1, n + 1):
            prefixSum[i] = prefixSum[i - 1] + piles[i - 1]
        
        # 记忆化数组
        memo = [[-1] * (n + 1) for _ in range(n + 1)]
        
        def dfs(i: int, m: int) -> int:
            # 如果已经处理完所有石子堆
            if i >= n:
                return 0
            
            # 如果当前玩家可以拿走所有剩余石子
            if i + 2 * m >= n:
                return prefixSum[n] - prefixSum[i]
            
            # 检查记忆化数组
            if memo[i][m] != -1:
                return memo[i][m]
            
            max_stones = 0
            # 当前玩家尝试拿1到2*m堆石子
            for x in range(1, 2 * m + 1):
                if i + x > n:
                    break
                
                # 当前玩家拿x堆石子
                current_stones = prefixSum[i + x] - prefixSum[i]
                # 对手从i+x位置开始，新的M值为max(m, x)
                opponent_stones = dfs(i + x, max(m, x))
                
                # 当前玩家能获得的最大石子数 = 当前拿的石子数 + 剩余总石子数 - 对手获得的最优解
                max_stones = max(max_stones, current_stones + (prefixSum[n] - prefixSum[i + x] - opponent_stones))
            
            memo[i][m] = max_stones
            return max_stones
        
        return dfs(0, 1)

# 测试函数
def main():
    # 测试用例1：标准情况
    piles1 = [2, 7, 9, 4, 4]
    print(f"测试用例1 [2,7,9,4,4]: {Code19_StoneGameIILeetCode1140.stoneGameII(piles1)}")  # 应输出10
    
    # 测试用例2：边界情况
    piles2 = [1, 2, 3, 4, 5, 100]
    print(f"测试用例2 [1,2,3,4,5,100]: {Code19_StoneGameIILeetCode1140.stoneGameII(piles2)}")  # 应输出104
    
    # 测试用例3：两堆石子
    piles3 = [1, 100]
    print(f"测试用例3 [1,100]: {Code19_StoneGameIILeetCode1140.stoneGameII(piles3)}")  # 应输出101
    
    # 测试用例4：单堆石子
    piles4 = [100]
    print(f"测试用例4 [100]: {Code19_StoneGameIILeetCode1140.stoneGameII(piles4)}")  # 应输出100
    
    # 验证优化版本
    print("优化版本测试:")
    print(f"测试用例1 [2,7,9,4,4]: {Code19_StoneGameIILeetCode1140.stoneGameIIOptimized(piles1)}")
    print(f"测试用例2 [1,2,3,4,5,100]: {Code19_StoneGameIILeetCode1140.stoneGameIIOptimized(piles2)}")
    
    # 性能测试：大规模数据
    large_piles = [1] * 100
    print(f"大规模测试 [100个1]: {Code19_StoneGameIILeetCode1140.stoneGameIIOptimized(large_piles)}")

if __name__ == "__main__":
    main()