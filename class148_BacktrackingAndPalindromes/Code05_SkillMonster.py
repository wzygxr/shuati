"""
技能打怪问题（升级版） - Python版本

问题描述：
现在有一个打怪类型的游戏，你有n个技能，每个技能有伤害值、魔法消耗值和触发双倍伤害的血量阈值。
每个技能最多只能释放一次，怪物有m点血量。问如何用最少的魔法值消灭怪物（血量≤0）。

算法思路：
方法1：回溯算法 - 遍历所有可能的技能使用顺序
方法2：动态规划 - 使用状态压缩DP
方法3：贪心+回溯 - 按技能性价比排序优化

时间复杂度分析：
- 回溯算法：O(n!)，需要尝试所有技能的排列组合
- 动态规划：O(n * 2^n)，状态压缩DP
- 贪心+回溯：O(n!)，但通过剪枝优化实际运行更快

空间复杂度分析：
- 回溯算法：O(n)，递归栈深度
- 动态规划：O(2^n)，DP数组空间
- 贪心+回溯：O(n)，递归栈深度

工程化考量：
1. 输入验证：检查参数合法性
2. 边界处理：怪物血量为0、技能伤害不足等情况
3. 性能优化：剪枝、排序、缓存等优化技术
4. 可测试性：设计全面的测试用例
"""

from typing import List, Tuple
import sys

class SkillMonsterAdvancedSolver:
    def __init__(self, damage: List[int], cost: List[int], threshold: List[int]):
        """
        构造函数
        Args:
            damage: 技能伤害值列表
            cost: 魔法消耗值列表
            threshold: 触发双倍伤害的血量阈值列表
        """
        self.damage = damage
        self.cost = cost
        self.threshold = threshold
        self.min_cost = float('inf')
        self.n = len(damage)
        
        # 输入验证
        if len(damage) != len(cost) or len(damage) != len(threshold):
            raise ValueError("伤害值、消耗值和阈值数组长度必须相同")
        
        if any(d < 0 for d in damage) or any(c < 0 for c in cost) or any(t < 0 for t in threshold):
            raise ValueError("伤害值、消耗值和阈值必须为非负数")
    
    def min_magic_cost(self, m: int) -> int:
        """
        方法1：回溯算法
        遍历所有可能的技能使用顺序
        """
        if m <= 0:
            return 0
        
        if self.n == 0:
            return -1
        
        self.min_cost = float('inf')
        used = [False] * self.n
        
        self._backtrack(m, 0, used)
        
        result = -1 if self.min_cost == float('inf') else int(self.min_cost)
        return result
    
    def min_magic_cost_dp(self, m: int) -> int:
        """
        方法2：动态规划解法
        使用状态压缩DP，mask表示技能使用状态
        """
        if m <= 0:
            return 0
        
        total_states = 1 << self.n
        
        # dp[mask]表示使用mask对应技能时的最小魔法消耗
        dp = [float('inf')] * total_states
        dp[0] = 0  # 没有使用任何技能
        
        # hp[mask]表示使用mask对应技能后怪物的剩余血量
        hp = [m] * total_states
        
        for mask in range(total_states):
            if dp[mask] == float('inf'):
                continue
                
            for i in range(self.n):
                if not (mask & (1 << i)):  # 技能i未使用
                    new_mask = mask | (1 << i)
                    actual_damage = self._calculate_damage(hp[mask], i)
                    new_hp = hp[mask] - actual_damage
                    new_cost = dp[mask] + self.cost[i]
                    
                    if new_hp <= 0:
                        # 怪物被击败
                        if new_cost < dp[new_mask]:
                            dp[new_mask] = new_cost
                    else:
                        if new_cost < dp[new_mask]:
                            dp[new_mask] = new_cost
                            hp[new_mask] = new_hp
        
        # 找到所有状态中的最小魔法消耗
        result = float('inf')
        for mask in range(total_states):
            if hp[mask] <= 0 and dp[mask] < result:
                result = dp[mask]
        
        return -1 if result == float('inf') else int(result)
    
    def min_magic_cost_greedy(self, m: int) -> int:
        """
        方法3：贪心+回溯优化
        按技能性价比排序，优先尝试高性价比技能
        """
        if m <= 0:
            return 0
        
        if self.n == 0:
            return -1
        
        # 计算技能性价比（伤害/消耗）
        skills = []
        for i in range(self.n):
            efficiency = self.damage[i] / self.cost[i] if self.cost[i] > 0 else float('inf')
            skills.append((i, efficiency))
        
        # 按性价比降序排序
        skills.sort(key=lambda x: x[1], reverse=True)
        
        self.min_cost = float('inf')
        used = [False] * self.n
        
        self._backtrack_greedy(m, 0, used, skills)
        
        result = -1 if self.min_cost == float('inf') else int(self.min_cost)
        return result
    
    def _backtrack(self, remaining_hp: int, current_cost: int, used: List[bool]):
        """
        回溯函数
        """
        # 基础情况：怪物已被击败
        if remaining_hp <= 0:
            if current_cost < self.min_cost:
                self.min_cost = current_cost
            return
        
        # 剪枝优化：如果当前魔法消耗已经超过已知最优解，提前返回
        if current_cost >= self.min_cost:
            return
        
        # 尝试使用每个未使用的技能
        for i in range(self.n):
            if not used[i]:
                used[i] = True
                
                actual_damage = self._calculate_damage(remaining_hp, i)
                self._backtrack(remaining_hp - actual_damage, current_cost + self.cost[i], used)
                
                used[i] = False
    
    def _backtrack_greedy(self, remaining_hp: int, current_cost: int, used: List[bool], 
                         skills: List[Tuple[int, float]]):
        """
        贪心优化的回溯函数
        """
        if remaining_hp <= 0:
            if current_cost < self.min_cost:
                self.min_cost = current_cost
            return
        
        if current_cost >= self.min_cost:
            return
        
        # 按性价比顺序尝试技能
        for skill_idx, _ in skills:
            i = skill_idx
            if not used[i]:
                used[i] = True
                
                actual_damage = self._calculate_damage(remaining_hp, i)
                self._backtrack_greedy(remaining_hp - actual_damage, current_cost + self.cost[i], used, skills)
                
                used[i] = False
    
    def _calculate_damage(self, remaining_hp: int, skill_index: int) -> int:
        """
        计算实际伤害（考虑双倍伤害）
        """
        return self.damage[skill_index] * 2 if remaining_hp <= self.threshold[skill_index] else self.damage[skill_index]

# 补充训练题目 - Python实现

def coin_change(coins: List[int], amount: int) -> int:
    """
    LeetCode 322. 零钱兑换
    给定不同面额的硬币coins和总金额amount，计算凑成总金额所需的最少硬币个数。
    """
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0
    
    for coin in coins:
        for i in range(coin, amount + 1):
            dp[i] = min(dp[i], dp[i - coin] + 1)
    
    return -1 if dp[amount] > amount else dp[amount]

def change(amount: int, coins: List[int]) -> int:
    """
    LeetCode 518. 零钱兑换 II
    给定不同面额的硬币和一个总金额，计算可以凑成总金额的硬币组合数。
    """
    dp = [0] * (amount + 1)
    dp[0] = 1
    
    for coin in coins:
        for i in range(coin, amount + 1):
            dp[i] += dp[i - coin]
    
    return dp[amount]

def num_squares(n: int) -> int:
    """
    LeetCode 279. 完全平方数
    给定正整数n，找到若干个完全平方数使得它们的和等于n，返回需要的最少完全平方数。
    """
    dp = [float('inf')] * (n + 1)
    dp[0] = 0
    
    for i in range(1, n + 1):
        j = 1
        while j * j <= i:
            dp[i] = min(dp[i], dp[i - j * j] + 1)
            j += 1
    
    return dp[n]

def combination_sum4(nums: List[int], target: int) -> int:
    """
    LeetCode 377. 组合总和 Ⅳ
    给定一个由正整数组成且不存在重复数字的数组，找出和为给定目标正整数的组合的个数。
    """
    dp = [0] * (target + 1)
    dp[0] = 1
    
    for i in range(1, target + 1):
        for num in nums:
            if i >= num:
                dp[i] += dp[i - num]
    
    return dp[target]

def can_partition(nums: List[int]) -> bool:
    """
    LeetCode 416. 分割等和子集
    给定一个只包含正整数的非空数组，判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
    """
    total_sum = sum(nums)
    
    if total_sum % 2 != 0:
        return False
    
    target = total_sum // 2
    dp = [False] * (target + 1)
    dp[0] = True
    
    for num in nums:
        for i in range(target, num - 1, -1):
            dp[i] = dp[i] or dp[i - num]
    
    return dp[target]

def knapsack(weights: List[int], values: List[int], capacity: int) -> int:
    """
    0-1背包问题
    给定物品重量和价值，在不超过背包容量的情况下，求能装入的最大价值。
    """
    n = len(weights)
    dp = [0] * (capacity + 1)
    
    for i in range(n):
        for j in range(capacity, weights[i] - 1, -1):
            dp[j] = max(dp[j], dp[j - weights[i]] + values[i])
    
    return dp[capacity]

def unbounded_knapsack(weights: List[int], values: List[int], capacity: int) -> int:
    """
    完全背包问题
    物品可以无限次使用，求在不超过背包容量的情况下的最大价值。
    """
    n = len(weights)
    dp = [0] * (capacity + 1)
    
    for i in range(n):
        for j in range(weights[i], capacity + 1):
            dp[j] = max(dp[j], dp[j - weights[i]] + values[i])
    
    return dp[capacity]

# 测试函数
def test_skill_monster_advanced():
    """测试函数"""
    
    # 测试用例1：简单的打怪场景
    damage1 = [3, 4, 5]
    cost1 = [2, 3, 4]
    threshold1 = [5, 3, 2]
    m1 = 10
    
    solver1 = SkillMonsterAdvancedSolver(damage1, cost1, threshold1)
    result1 = solver1.min_magic_cost(m1)
    result1_dp = solver1.min_magic_cost_dp(m1)
    result1_greedy = solver1.min_magic_cost_greedy(m1)
    
    print("测试用例1:")
    print(f"技能伤害: {damage1}")
    print(f"技能消耗: {cost1}")
    print(f"技能阈值: {threshold1}")
    print(f"怪物血量: {m1}")
    print(f"回溯算法结果: {result1}")
    print(f"动态规划结果: {result1_dp}")
    print(f"贪心优化结果: {result1_greedy}")
    print()
    
    # 测试用例2：无法击败怪物的情况
    damage2 = [3, 4]
    cost2 = [2, 3]
    threshold2 = [10, 8]
    m2 = 20
    
    solver2 = SkillMonsterAdvancedSolver(damage2, cost2, threshold2)
    result2 = solver2.min_magic_cost(m2)
    
    print("测试用例2:")
    print(f"技能伤害: {damage2}")
    print(f"技能消耗: {cost2}")
    print(f"技能阈值: {threshold2}")
    print(f"怪物血量: {m2}")
    print(f"最少魔法消耗: {result2}")
    print()
    
    # 测试补充题目
    print("=== 补充训练题目测试 ===")
    
    # 测试零钱兑换
    coins = [1, 2, 5]
    amount = 11
    print(f"零钱兑换: coins={coins}, amount={amount}, 结果={coin_change(coins, amount)}")
    
    # 测试零钱兑换II
    print(f"零钱兑换II: coins={coins}, amount={amount}, 结果={change(amount, coins)}")
    
    # 测试完全平方数
    n = 12
    print(f"完全平方数: n={n}, 结果={num_squares(n)}")
    
    # 测试组合总和IV
    nums = [1, 2, 3]
    target = 4
    print(f"组合总和IV: nums={nums}, target={target}, 结果={combination_sum4(nums, target)}")
    
    # 测试分割等和子集
    partition_nums = [1, 5, 11, 5]
    print(f"分割等和子集: nums={partition_nums}, 结果={can_partition(partition_nums)}")
    
    # 测试0-1背包问题
    weights = [2, 3, 4, 5]
    values = [3, 4, 5, 6]
    capacity = 8
    print(f"0-1背包问题: weights={weights}, values={values}, capacity={capacity}, 结果={knapsack(weights, values, capacity)}")
    
    # 测试完全背包问题
    print(f"完全背包问题: weights={weights}, values={values}, capacity={capacity}, 结果={unbounded_knapsack(weights, values, capacity)}")

if __name__ == "__main__":
    test_skill_monster_advanced()

"""
算法技巧总结 - Python版本

核心概念：
1. 回溯算法框架：
   - 状态定义：当前选择、剩余选择、目标状态
   - 选择策略：遍历所有可能的选择
   - 终止条件：达到目标状态或无法继续
   - 回溯操作：撤销当前选择，尝试其他选择

2. 动态规划技术：
   - 状态压缩：使用位运算表示状态
   - 状态转移：根据当前状态推导下一状态
   - 边界处理：处理初始状态和终止状态

3. 优化技巧：
   - 剪枝优化：提前终止无效分支
   - 排序优化：优先搜索更优路径
   - 贪心策略：局部最优选择

Python特有优势：
1. 内置大整数支持：无需担心数值溢出
2. 列表操作简便：灵活的列表切片和操作
3. 类型注解：提高代码可读性

调试技巧：
1. 使用pdb进行调试
2. 打印中间状态变量
3. 使用assert进行条件验证

性能优化：
1. 避免不必要的列表复制
2. 使用局部变量减少属性查找
3. 利用Python内置函数的高效实现

工程化实践：
1. 类型注解：提高代码可读性
2. 异常处理：确保程序健壮性
3. 单元测试：保证代码质量
4. 文档字符串：提供清晰的接口说明

边界情况处理：
1. 空输入：返回默认值或错误
2. 边界值：测试最小/最大输入
3. 异常情况：处理非法输入和边界条件
"""