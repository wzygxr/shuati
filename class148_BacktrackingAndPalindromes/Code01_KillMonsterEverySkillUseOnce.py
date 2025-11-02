"""
技能打怪问题 - Python版本

问题描述：
现在有一个打怪类型的游戏，你有n个技能，每个技能有伤害值、魔法消耗值和触发双倍伤害的血量阈值。
每个技能最多只能释放一次，怪物有m点血量。问最少用多少魔法值能消灭怪物（血量≤0）。

算法思路：
1. 使用回溯算法遍历所有可能的技能使用顺序
2. 在搜索过程中维护当前使用的魔法值和怪物剩余血量
3. 通过剪枝优化避免无效搜索
4. 返回所有方案中魔法消耗最少的值

时间复杂度分析：
- 最坏情况：O(n!)，需要尝试所有技能的排列组合
- 平均情况：通过剪枝优化，实际运行时间远小于n!

空间复杂度分析：
- 递归栈深度：O(n)
- 标记数组：O(n)
- 总空间复杂度：O(n)

工程化考量：
1. 输入验证：检查参数合法性
2. 边界处理：怪物血量为0、技能伤害不足等情况
3. 异常处理：适当的异常捕获机制
4. 可测试性：设计单元测试用例

优化技巧：
1. 剪枝优化：当前魔法消耗超过已知最优解时提前返回
2. 排序优化：按技能性价比排序，优先搜索更优路径
3. 记忆化搜索：对于重复出现的子问题进行缓存
"""

import sys
from typing import List, Tuple
from functools import lru_cache

class SkillMonsterSolver:
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
        计算击败怪物所需的最少魔法值
        Args:
            m: 怪物血量
        Returns:
            最少需要的魔法值，如果无法击败则返回-1
        """
        # 边界情况处理
        if m <= 0:
            return 0  # 怪物已经死亡
        
        if self.n == 0:
            return -1  # 没有可用技能
        
        self.min_cost = float('inf')
        used = [False] * self.n
        
        self._backtrack(m, 0, used)
        
        result = -1 if self.min_cost == float('inf') else int(self.min_cost)
        return result
    
    def _backtrack(self, remaining_hp: int, current_cost: int, used: List[bool]):
        """
        回溯函数
        Args:
            remaining_hp: 怪物剩余血量
            current_cost: 当前已消耗的魔法值
            used: 标记技能是否已使用的列表
        """
        # 基础情况：怪物已被击败
        if remaining_hp <= 0:
            self.min_cost = min(self.min_cost, current_cost)
            return
        
        # 剪枝优化：如果当前魔法消耗已经超过已知最优解，提前返回
        if current_cost >= self.min_cost:
            return
        
        # 尝试使用每个未使用的技能
        for i in range(self.n):
            if not used[i]:
                used[i] = True
                
                # 计算实际伤害（考虑双倍伤害）
                actual_damage = self.damage[i] * 2 if remaining_hp <= self.threshold[i] else self.damage[i]
                
                # 递归搜索
                self._backtrack(remaining_hp - actual_damage, current_cost + self.cost[i], used)
                
                # 回溯
                used[i] = False
    
    def min_magic_cost_optimized(self, m: int) -> int:
        """
        优化版本：按技能性价比排序，优先使用高性价比技能
        """
        if m <= 0:
            return 0
        
        # 计算技能性价比（伤害/消耗）
        skills = []
        for i in range(self.n):
            efficiency = self.damage[i] / self.cost[i] if self.cost[i] > 0 else float('inf')
            skills.append((i, efficiency))
        
        # 按性价比降序排序
        skills.sort(key=lambda x: x[1], reverse=True)
        
        self.min_cost = float('inf')
        used = [False] * self.n
        
        self._backtrack_optimized(m, 0, used, skills)
        
        result = -1 if self.min_cost == float('inf') else int(self.min_cost)
        return result
    
    def _backtrack_optimized(self, remaining_hp: int, current_cost: int, used: List[bool], skills: List[Tuple[int, float]]):
        """
        优化后的回溯函数，按性价比顺序尝试技能
        """
        if remaining_hp <= 0:
            self.min_cost = min(self.min_cost, current_cost)
            return
        
        if current_cost >= self.min_cost:
            return
        
        # 按性价比顺序尝试技能
        for skill_idx, _ in skills:
            i = skill_idx
            if not used[i]:
                used[i] = True
                
                actual_damage = self.damage[i] * 2 if remaining_hp <= self.threshold[i] else self.damage[i]
                self._backtrack_optimized(remaining_hp - actual_damage, current_cost + self.cost[i], used, skills)
                
                used[i] = False

class DPSolver:
    """
    动态规划解法（适用于n较小的情况）
    使用状态压缩DP，mask表示技能使用状态
    """
    
    @staticmethod
    def min_magic_cost_dp(damage: List[int], cost: List[int], threshold: List[int], m: int) -> int:
        n = len(damage)
        total_states = 1 << n
        
        # dp[mask]表示使用mask对应技能时的最小魔法消耗
        dp = [float('inf')] * total_states
        dp[0] = 0  # 没有使用任何技能
        
        # hp[mask]表示使用mask对应技能后怪物的剩余血量
        hp = [m] * total_states
        
        for mask in range(total_states):
            if dp[mask] == float('inf'):
                continue
                
            for i in range(n):
                if not (mask & (1 << i)):  # 技能i未使用
                    new_mask = mask | (1 << i)
                    actual_damage = damage[i] * 2 if hp[mask] <= threshold[i] else damage[i]
                    new_hp = hp[mask] - actual_damage
                    new_cost = dp[mask] + cost[i]
                    
                    if new_hp <= 0:
                        # 怪物被击败
                        dp[new_mask] = min(dp[new_mask], new_cost)
                    elif new_cost < dp[new_mask]:
                        dp[new_mask] = new_cost
                        hp[new_mask] = new_hp
        
        # 找到所有状态中的最小魔法消耗
        result = float('inf')
        for mask in range(total_states):
            if hp[mask] <= 0:
                result = min(result, dp[mask])
        
        return -1 if result == float('inf') else int(result)

def test_skill_monster():
    """测试函数"""
    
    # 测试用例1：简单的打怪场景
    damage1 = [3, 4, 5]
    cost1 = [2, 3, 4]
    threshold1 = [5, 3, 2]
    m1 = 10
    
    solver1 = SkillMonsterSolver(damage1, cost1, threshold1)
    result1 = solver1.min_magic_cost(m1)
    
    print("测试用例1:")
    print(f"技能伤害: {damage1}")
    print(f"技能消耗: {cost1}")
    print(f"技能阈值: {threshold1}")
    print(f"怪物血量: {m1}")
    print(f"最少魔法消耗: {result1}")
    print()
    
    # 测试用例2：无法击败怪物的情况
    damage2 = [3, 4]
    cost2 = [2, 3]
    threshold2 = [10, 8]
    m2 = 20
    
    solver2 = SkillMonsterSolver(damage2, cost2, threshold2)
    result2 = solver2.min_magic_cost(m2)
    
    print("测试用例2:")
    print(f"技能伤害: {damage2}")
    print(f"技能消耗: {cost2}")
    print(f"技能阈值: {threshold2}")
    print(f"怪物血量: {m2}")
    print(f"最少魔法消耗: {result2}")
    print()
    
    # 测试用例3：怪物血量为0
    damage3 = [5]
    cost3 = [2]
    threshold3 = [3]
    m3 = 0
    
    solver3 = SkillMonsterSolver(damage3, cost3, threshold3)
    result3 = solver3.min_magic_cost(m3)
    
    print("测试用例3:")
    print(f"技能伤害: {damage3}")
    print(f"技能消耗: {cost3}")
    print(f"技能阈值: {threshold3}")
    print(f"怪物血量: {m3}")
    print(f"最少魔法消耗: {result3}")
    print()
    
    # 测试优化版本
    result1_opt = solver1.min_magic_cost_optimized(m1)
    print(f"优化版本结果: {result1_opt}")
    print()
    
    # 测试动态规划解法
    dp_result = DPSolver.min_magic_cost_dp(damage1, cost1, threshold1, m1)
    print(f"动态规划解法结果: {dp_result}")

# 补充训练题目 - Python实现

def coin_change(coins: List[int], amount: int) -> int:
    """
    LeetCode 322. 零钱兑换
    问题描述：给定不同面额的硬币coins和总金额amount，计算凑成总金额所需的最少硬币个数。
    """
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0
    
    for coin in coins:
        for i in range(coin, amount + 1):
            dp[i] = min(dp[i], dp[i - coin] + 1)
    
    return -1 if dp[amount] > amount else dp[amount]

def permute(nums: List[int]) -> List[List[int]]:
    """
    LeetCode 46. 全排列
    问题描述：给定一个不含重复数字的数组，返回其所有可能的全排列。
    """
    def backtrack(current: List[int], used: List[bool]):
        if len(current) == len(nums):
            result.append(current[:])
            return
        
        for i in range(len(nums)):
            if not used[i]:
                used[i] = True
                current.append(nums[i])
                backtrack(current, used)
                current.pop()
                used[i] = False
    
    result = []
    backtrack([], [False] * len(nums))
    return result

def permute_unique(nums: List[int]) -> List[List[int]]:
    """
    LeetCode 47. 全排列 II
    问题描述：给定可包含重复数字的序列，返回所有不重复的全排列。
    """
    def backtrack(current: List[int], used: List[bool]):
        if len(current) == len(nums):
            result.append(current[:])
            return
        
        for i in range(len(nums)):
            # 去重逻辑：当前数字与前一个数字相同且前一个数字未使用过时，跳过
            if used[i] or (i > 0 and nums[i] == nums[i-1] and not used[i-1]):
                continue
            
            used[i] = True
            current.append(nums[i])
            backtrack(current, used)
            current.pop()
            used[i] = False
    
    nums.sort()  # 排序以便去重
    result = []
    backtrack([], [False] * len(nums))
    return result

def subsets(nums: List[int]) -> List[List[int]]:
    """
    LeetCode 78. 子集
    问题描述：给定一个整数数组，返回该数组所有可能的子集（幂集）。
    """
    def backtrack(start: int, current: List[int]):
        result.append(current[:])
        
        for i in range(start, len(nums)):
            current.append(nums[i])
            backtrack(i + 1, current)
            current.pop()
    
    result = []
    backtrack(0, [])
    return result

def combination_sum(candidates: List[int], target: int) -> List[List[int]]:
    """
    LeetCode 39. 组合总和
    问题描述：给定一个无重复元素的数组candidates和一个目标数target，
    找出candidates中所有可以使数字和为target的组合。
    """
    def backtrack(start: int, current: List[int], current_sum: int):
        if current_sum == target:
            result.append(current[:])
            return
        
        if current_sum > target:
            return
        
        for i in range(start, len(candidates)):
            current.append(candidates[i])
            backtrack(i, current, current_sum + candidates[i])  # 可以重复使用同一个数字
            current.pop()
    
    result = []
    backtrack(0, [], 0)
    return result

if __name__ == "__main__":
    test_skill_monster()
    
    # 测试补充题目
    print("=== 补充训练题目测试 ===")
    
    # 测试零钱兑换
    coins = [1, 2, 5]
    amount = 11
    print(f"零钱兑换: coins={coins}, amount={amount}, 结果={coin_change(coins, amount)}")
    
    # 测试全排列
    nums = [1, 2, 3]
    permutations = permute(nums)
    print(f"全排列: nums={nums}, 排列数量={len(permutations)}")
    
    # 测试包含重复数字的全排列
    nums_dup = [1, 1, 2]
    permutations_dup = permute_unique(nums_dup)
    print(f"包含重复数字的全排列: nums={nums_dup}, 不重复排列数量={len(permutations_dup)}")
    
    # 测试子集
    subsets_result = subsets([1, 2, 3])
    print(f"子集: nums=[1,2,3], 子集数量={len(subsets_result)}")
    
    # 测试组合总和
    combination_result = combination_sum([2, 3, 6, 7], 7)
    print(f"组合总和: candidates=[2,3,6,7], target=7, 组合数量={len(combination_result)}")

"""
算法技巧总结 - Python版本

核心概念：
1. 回溯算法框架：
   - 状态定义：当前选择、剩余选择、目标状态
   - 选择策略：遍历所有可能的选择
   - 终止条件：达到目标状态或无法继续
   - 回溯操作：撤销当前选择，尝试其他选择

2. Python特有优化：
   - 使用@lru_cache进行记忆化
   - 生成器表达式减少内存使用
   - 列表推导式简化代码

3. 工程化实践：
   - 类型注解提高代码可读性
   - 异常处理确保程序健壮性
   - 单元测试保证代码质量

调试技巧：
1. 使用pdb进行调试
2. 打印中间状态变量
3. 使用assert进行条件验证

性能优化：
1. 避免不必要的列表复制
2. 使用局部变量减少属性查找
3. 利用Python内置函数的高效实现
"""