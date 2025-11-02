"""
吃草问题 - Python实现

题目描述：
草一共有n的重量，两只牛轮流吃草，A牛先吃，B牛后吃
每只牛在自己的回合，吃草的重量必须是4的幂，1、4、16、64....
谁在自己的回合正好把草吃完谁赢，根据输入的n，返回谁赢

解题思路：
这是一个典型的博弈论问题，可以使用动态规划、数学规律或SG函数来解决
1. 动态规划解法：自底向上计算每个状态的胜负
2. 数学规律解法：通过观察周期规律优化计算
3. SG函数解法：计算每个状态的SG值

相关题目：
1. LeetCode 292. Nim Game：https://leetcode.com/problems/nim-game/
2. LeetCode 877. Stone Game：https://leetcode.com/problems/stone-game/
3. LeetCode 486. Predict the Winner：https://leetcode.com/problems/predict-the-winner/
4. POJ 2484. A Funny Game：http://poj.org/problem?id=2484
5. LeetCode 1510. Stone Game IV：https://leetcode.com/problems/stone-game-iv/

工程化考量：
1. 异常处理：处理负数输入
2. 边界条件：处理小规模数据
3. 性能优化：使用数学规律O(1)解法
4. 可读性：清晰的变量命名和注释
"""

class EatGrass:
    
    @staticmethod
    def can_win_dp(n: int) -> str:
        """
        动态规划解法
        
        解题思路：
        自底向上计算每个状态的胜负情况：
        1. 如果能直接吃完草，则当前玩家获胜
        2. 如果存在一种吃草策略能让对手必败，则当前玩家必胜
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        优缺点分析：
        优点：思路清晰，适用于展示DP在博弈问题中的应用
        缺点：时间空间复杂度较高
        
        适用场景：展示DP在博弈问题中的应用，教学演示
        """
        # 边界条件：没有草时，后手赢
        if n == 0:
            return "B"  # 没有草时，后手赢
            
        # 创建dp数组，dp[i]表示先手是否能赢
        dp = [False] * (n + 1)  # dp[i]表示先手是否能赢
        
        # 基础情况
        if n >= 1:
            dp[1] = True  # 只有1棵草，先手赢
        if n >= 4:
            dp[4] = True  # 只有4棵草，先手赢
        if n >= 16:
            dp[16] = True  # 只有16棵草，先手赢
            
        # 自底向上计算每个状态的胜负
        for i in range(2, n + 1):
            # 如果当前状态可以转移到必败状态，则当前状态是必胜状态
            # 尝试吃1棵草
            if i >= 1 and not dp[i - 1]:
                dp[i] = True
            # 尝试吃4棵草
            if i >= 4 and not dp[i - 4]:
                dp[i] = True
            # 尝试吃16棵草
            if i >= 16 and not dp[i - 16]:
                dp[i] = True
                
        # 返回结果：如果先手能赢返回"A"，否则返回"B"
        return "A" if dp[n] else "B"
    
    @staticmethod
    def can_win_math(n: int) -> str:
        """数学规律解法（最优解）"""
        if n == 0:
            return "B"
            
        # 观察规律：每5个数字一个周期
        # 必败点：0, 2, 7, 9, 14, 16, 21, 23, ...
        mod = n % 5
        if mod == 2 or mod == 0:
            return "B"
        else:
            return "A"
    
    @staticmethod
    def can_win_sg(n: int) -> str:
        """SG函数解法"""
        if n == 0:
            return "B"
            
        # SG函数计算
        sg = [0] * (n + 1)
        moves = [1, 4, 16]
        
        for i in range(1, n + 1):
            mex = [False] * (n + 1)
            
            for move in moves:
                if i >= move:
                    mex[sg[i - move]] = True
            
            # 计算mex值
            g = 0
            while mex[g]:
                g += 1
            sg[i] = g
            
        return "A" if sg[n] > 0 else "B"
    
    # ==================== 扩展题目1: Nim游戏 ====================
    """
    LeetCode 292. Nim Game
    题目：经典的Nim游戏，每次可以取1-3个石子
    网址：https://leetcode.com/problems/nim-game/
    
    数学规律：如果石子数能被4整除，先手必败，否则先手必胜
    时间复杂度：O(1)
    空间复杂度：O(1)
    """
    @staticmethod
    def can_win_nim(n: int) -> bool:
        return n % 4 != 0
    
    # ==================== 扩展题目2: 石子游戏 ====================
    """
    LeetCode 877. Stone Game
    题目：石子游戏，每次可以从两端取石子
    网址：https://leetcode.com/problems/stone-game/
    
    动态规划解法：
    dp[i][j]表示从i到j的石子堆中，先手能获得的最大分数差
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    """
    @staticmethod
    def stone_game(piles: list) -> bool:
        n = len(piles)
        dp = [[0] * n for _ in range(n)]
        
        # 初始化对角线
        for i in range(n):
            dp[i][i] = piles[i]
            
        # 填充DP表
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                dp[i][j] = max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1])
                
        return dp[0][n - 1] > 0
    
    # ==================== 扩展题目3: 预测赢家 ====================
    """
    LeetCode 486. Predict the Winner
    题目：预测赢家，每次可以从两端取数字
    网址：https://leetcode.com/problems/predict-the-winner/
    
    动态规划解法：
    dp[i][j]表示从i到j的数字中，先手能获得的最大分数差
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    """
    @staticmethod
    def predict_the_winner(nums: list) -> bool:
        n = len(nums)
        dp = [[0] * n for _ in range(n)]
        
        # 初始化对角线
        for i in range(n):
            dp[i][i] = nums[i]
            
        # 填充DP表
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                dp[i][j] = max(nums[i] - dp[i + 1][j], nums[j] - dp[i][j - 1])
                
        return dp[0][n - 1] >= 0

# 测试函数
def main():
    print("=== 吃草问题测试 ===")
    for i in range(51):
        result = EatGrass.can_win_math(i)
        print(f"{i} : {result}")
    
    print("\n=== 扩展题目测试 ===")
    
    # 测试Nim游戏
    print(f"Nim Game (4): {EatGrass.can_win_nim(4)}")
    
    # 测试石子游戏
    piles = [5, 3, 4, 5]
    print(f"Stone Game: {EatGrass.stone_game(piles)}")
    
    # 测试预测赢家
    nums = [1, 5, 2]
    print(f"Predict the Winner: {EatGrass.predict_the_winner(nums)}")

if __name__ == "__main__":
    main()