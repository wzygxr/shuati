# LeetCode 1155. 掷骰子的N种方法
# 题目描述：这里有 n 个一样的骰子，每个骰子上都有 k 个面，分别标有 1 到 k 的数字。
# 给定三个整数 n, k 和 target，请你计算并返回投掷骰子的所有可能得到的结果等于 target 的方案数。
# 答案可能很大，所以需要返回模 10^9 + 7 的结果。
# 链接：https://leetcode.cn/problems/number-of-dice-rolls-with-target-sum/
# 
# 解题思路：
# 这是一个典型的分组背包问题，每个骰子可以看作一组，每组有k个选项（1到k的点数）
# 我们需要从每组中选择一个选项，使得它们的总和等于target，求总共有多少种选法。
# 
# 状态定义：dp[i][j] 表示使用i个骰子能得到点数和为j的方案数
# 状态转移方程：dp[i][j] = sum(dp[i-1][j-m])，其中m从1到k且j-m >= i-1（因为每个骰子至少为1，i-1个骰子至少为i-1）
# 初始状态：dp[0][0] = 1（使用0个骰子得到点数和为0只有一种方式）
# 
# 时间复杂度：O(n * k * target)
# 空间复杂度：O(n * target)，可以优化为O(target)

MOD = 10**9 + 7

def num_rolls_to_target(n: int, k: int, target: int) -> int:
    """
    计算投掷骰子得到目标点数和的方案数
    
    参数:
        n: 骰子数量
        k: 每个骰子的面数（1到k）
        target: 目标点数和
    
    返回:
        方案数模10^9+7的结果
    """
    # 参数验证
    if n < 1 or k < 1 or target < n or target > n * k:
        return 0  # 不可能的情况：target小于骰子数或大于骰子数*最大面数
    
    # 创建二维DP数组，dp[i][j]表示使用i个骰子能得到点数和为j的方案数
    dp = [[0] * (target + 1) for _ in range(n + 1)]
    
    # 初始状态：使用0个骰子得到点数和为0只有一种方式
    dp[0][0] = 1
    
    # 填充DP数组
    for i in range(1, n + 1):  # 遍历骰子数量
        for j in range(i, min(target, i * k) + 1):  # 遍历可能的点数和（至少i，最多i*k）
            for m in range(1, k + 1):  # 遍历当前骰子的可能点数
                if m > j:  # 当前点数超过目标，停止循环
                    break
                dp[i][j] = (dp[i][j] + dp[i-1][j-m]) % MOD
    
    return dp[n][target]

def num_rolls_to_target_optimized(n: int, k: int, target: int) -> int:
    """
    优化空间复杂度的版本，使用一维DP数组
    
    参数:
        n: 骰子数量
        k: 每个骰子的面数（1到k）
        target: 目标点数和
    
    返回:
        方案数模10^9+7的结果
    """
    # 参数验证
    if n < 1 or k < 1 or target < n or target > n * k:
        return 0
    
    # 创建一维DP数组，dp[j]表示使用当前骰子数能得到点数和为j的方案数
    dp = [0] * (target + 1)
    
    # 初始状态：使用0个骰子得到点数和为0只有一种方式
    dp[0] = 1
    
    # 遍历骰子数量
    for i in range(1, n + 1):
        # 创建一个新数组来保存当前轮次的结果
        new_dp = [0] * (target + 1)
        
        # 遍历可能的点数和
        for j in range(i, min(target, i * k) + 1):
            # 遍历当前骰子的可能点数
            for m in range(1, k + 1):
                if m > j:  # 当前点数超过目标，停止循环
                    break
                new_dp[j] = (new_dp[j] + dp[j - m]) % MOD
        
        # 更新dp数组为当前轮次的结果
        dp = new_dp
    
    return dp[target]

def num_rolls_to_target_alternative(n: int, k: int, target: int) -> int:
    """
    另一种空间优化的方式，只使用一个一维数组，并倒序更新
    这种方式不适用，因为我们需要严格区分不同骰子数的状态
    所以这里只是作为对比展示，不推荐使用
    
    参数:
        n: 骰子数量
        k: 每个骰子的面数（1到k）
        target: 目标点数和
    
    返回:
        方案数模10^9+7的结果
    """
    # 参数验证
    if n < 1 or k < 1 or target < n or target > n * k:
        return 0
    
    # 创建DP数组
    dp = [0] * (target + 1)
    dp[0] = 1
    
    # 遍历骰子数量
    for i in range(1, n + 1):
        # 创建临时数组来保存前一轮的结果
        prev_dp = dp.copy()
        
        # 重置当前轮次的结果数组（除了dp[0]）
        for j in range(1, target + 1):
            dp[j] = 0
        
        # 更新当前轮次的结果
        for j in range(1, target + 1):
            for m in range(1, k + 1):
                if m > j:
                    break
                dp[j] = (dp[j] + prev_dp[j - m]) % MOD
    
    return dp[target]

def num_rolls_to_target_dfs(n: int, k: int, target: int) -> int:
    """
    递归+记忆化搜索实现
    
    参数:
        n: 骰子数量
        k: 每个骰子的面数（1到k）
        target: 目标点数和
    
    返回:
        方案数模10^9+7的结果
    """
    # 参数验证
    if n < 1 or k < 1 or target < n or target > n * k:
        return 0
    
    # 创建记忆化缓存，dp[i][j]表示使用i个骰子能得到点数和为j的方案数
    # 使用二维列表作为缓存
    memo = [[-1] * (target + 1) for _ in range(n + 1)]
    
    def dfs(remaining_dice: int, remaining_target: int) -> int:
        """
        递归辅助函数
        
        参数:
            remaining_dice: 剩余骰子数量
            remaining_target: 剩余目标点数
        
        返回:
            方案数模10^9+7的结果
        """
        # 基础情况：如果没有骰子了，检查是否达成目标
        if remaining_dice == 0:
            return 1 if remaining_target == 0 else 0
        
        # 检查缓存
        if memo[remaining_dice][remaining_target] != -1:
            return memo[remaining_dice][remaining_target]
        
        ways = 0
        
        # 尝试当前骰子的所有可能点数
        for i in range(1, k + 1):
            # 只有当前点数不超过剩余目标，并且剩余的骰子可以凑成剩余的点数时，才继续递归
            if (i <= remaining_target and 
                (remaining_dice - 1) <= (remaining_target - i) and 
                (remaining_target - i) <= (remaining_dice - 1) * k):
                ways = (ways + dfs(remaining_dice - 1, remaining_target - i)) % MOD
        
        # 缓存结果
        memo[remaining_dice][remaining_target] = ways
        return ways
    
    return dfs(n, target)

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    n1, k1, target1 = 1, 6, 3
    print(f"测试用例1结果: {num_rolls_to_target(n1, k1, target1)}")  # 预期输出: 1
    
    # 测试用例2
    n2, k2, target2 = 2, 6, 7
    print(f"测试用例2结果: {num_rolls_to_target(n2, k2, target2)}")  # 预期输出: 6
    
    # 测试用例3
    n3, k3, target3 = 30, 30, 500
    print(f"测试用例3结果: {num_rolls_to_target(n3, k3, target3)}")  # 预期输出: 222616187
    
    # 测试优化版本
    print(f"测试用例2 (优化版本): {num_rolls_to_target_optimized(n2, k2, target2)}")
    
    # 测试DFS实现
    print(f"测试用例2 (DFS): {num_rolls_to_target_dfs(n2, k2, target2)}")