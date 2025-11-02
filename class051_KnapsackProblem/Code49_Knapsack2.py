import sys
from typing import List, Tuple
import random
import time

# AtCoder DP Contest E - Knapsack 2
# 题目描述：经典的01背包问题，但是背包容量非常大（10^9），而物品价值比较小（10^3）。
# 链接：https://atcoder.jp/contests/dp/tasks/dp_e
# 
# 解题思路：
# 当背包容量非常大时，传统的DP方法会超时或超内存。
# 需要转换思路：将价值作为背包容量，求达到某个价值所需的最小重量。
# 
# 状态定义：dp[i] 表示达到价值i所需的最小重量
# 状态转移方程：dp[i] = min(dp[i], dp[i-value[j]] + weight[j])
# 初始状态：dp[0] = 0，其他为无穷大
# 
# 时间复杂度：O(N * totalValue)，其中N是物品数量，totalValue是总价值
# 空间复杂度：O(totalValue)
# 
# 工程化考量：
# 1. 问题转化：从重量维度转为价值维度
# 2. 边界处理：处理无穷大值和结果提取
# 3. 性能优化：计算总价值作为新背包容量
# 4. 异常处理：处理空输入和边界值

class Knapsack2:
    """
    大容量背包问题的多种解法
    """
    
    INF = 10**18  # 表示不可达状态
    
    @staticmethod
    def knapsack2(W: int, weights: List[int], values: List[int]) -> int:
        """
        动态规划解法 - 价值维度DP
        
        Args:
            W: 背包容量
            weights: 物品重量数组
            values: 物品价值数组
            
        Returns:
            int: 能装入背包的最大价值
        """
        # 参数验证
        if len(weights) != len(values):
            raise ValueError("Weights and values arrays must have same size")
        if W < 0:
            raise ValueError("Capacity W must be non-negative")
        
        n = len(weights)
        if n == 0:
            return 0
        
        # 计算总价值
        total_value = sum(values)
        
        # 创建DP数组，dp[i]表示达到价值i所需的最小重量
        dp = [Knapsack2.INF] * (total_value + 1)
        dp[0] = 0  # 价值为0时重量为0
        
        # 遍历每个物品
        for i in range(n):
            weight = weights[i]
            value = values[i]
            
            # 倒序遍历价值，避免重复选择
            for j in range(total_value, value - 1, -1):
                if dp[j - value] != Knapsack2.INF:
                    dp[j] = min(dp[j], dp[j - value] + weight)
        
        # 寻找最大的价值，使得所需重量 <= W
        max_value = 0
        for j in range(total_value, -1, -1):
            if dp[j] <= W:
                max_value = j
                break
        
        return max_value
    
    @staticmethod
    def knapsack2_optimized(W: int, weights: List[int], values: List[int]) -> int:
        """
        优化的动态规划解法 - 提前终止
        
        Args:
            W: 背包容量
            weights: 物品重量数组
            values: 物品价值数组
            
        Returns:
            int: 能装入背包的最大价值
        """
        if len(weights) != len(values):
            raise ValueError("Weights and values arrays must have same size")
        if W < 0:
            raise ValueError("Capacity W must be non-negative")
        
        n = len(weights)
        if n == 0:
            return 0
        
        # 计算总价值
        total_value = sum(values)
        
        dp = [Knapsack2.INF] * (total_value + 1)
        dp[0] = 0
        
        for i in range(n):
            weight = weights[i]
            value = values[i]
            
            for j in range(total_value, value - 1, -1):
                if dp[j - value] != Knapsack2.INF:
                    new_weight = dp[j - value] + weight
                    if new_weight < dp[j]:
                        dp[j] = new_weight
        
        # 从大到小遍历，找到第一个满足条件的价值
        for j in range(total_value, -1, -1):
            if dp[j] <= W:
                return j
        
        return 0
    
    @staticmethod
    def knapsack_traditional(W: int, weights: List[int], values: List[int]) -> int:
        """
        传统DP解法（用于对比）- 仅适用于小容量
        
        Args:
            W: 背包容量
            weights: 物品重量数组
            values: 物品价值数组
            
        Returns:
            int: 能装入背包的最大价值
        """
        if len(weights) != len(values):
            raise ValueError("Weights and values arrays must have same size")
        if W < 0:
            raise ValueError("Capacity W must be non-negative")
        
        n = len(weights)
        if n == 0:
            return 0
        
        # 传统DP：dp[i]表示容量为i时的最大价值
        max_w = min(W, 10**6)  # 限制最大容量，避免内存溢出
        dp = [0] * (max_w + 1)
        
        for i in range(n):
            weight = weights[i]
            value = values[i]
            
            for j in range(max_w, weight - 1, -1):
                dp[j] = max(dp[j], dp[j - weight] + value)
        
        return dp[max_w]
    
    @staticmethod
    def run_tests():
        """
        运行测试用例，验证所有方法的正确性
        """
        # 测试用例
        W_list = [100, 1000, 10**9]
        weights_cases = [
            [10, 20, 30],
            [50, 100, 150],
            [1, 2, 3]
        ]
        values_cases = [
            [60, 100, 120],
            [60, 100, 120],
            [10, 15, 40]
        ]
        
        print("大容量背包问题测试：")
        for i, (W, weights, values) in enumerate(zip(W_list, weights_cases, values_cases)):
            try:
                result1 = Knapsack2.knapsack2(W, weights, values)
                result2 = Knapsack2.knapsack2_optimized(W, weights, values)
                
                # 对于小容量，可以用传统方法验证
                traditional_result = 0
                if W <= 10000:
                    traditional_result = Knapsack2.knapsack_traditional(W, weights, values)
                
                print(f"W={W}, weights={weights}, values={values}: "
                      f"方法1={result1}, 方法2={result2}", end="")
                
                if W <= 10000:
                    print(f", 传统方法={traditional_result}", end="")
                    # 验证结果一致性
                    if result1 != traditional_result or result2 != traditional_result:
                        print(" - 警告：结果不一致！")
                    else:
                        print(" - 验证通过")
                else:
                    print(" - 大容量测试")
                    
            except Exception as e:
                print(f"测试用例{i+1}时发生错误: {e}")
        
        # 性能测试 - 大规模数据
        n = 100
        large_weights = [random.randint(1, 1000) for _ in range(n)]
        large_values = [random.randint(1, 100) for _ in range(n)]
        large_W = 10**9  # 10^9
        
        start_time = time.time()
        large_result = Knapsack2.knapsack2_optimized(large_W, large_weights, large_values)
        end_time = time.time()
        
        print(f"大规模测试: 物品数量={n}, 容量={large_W}, "
              f"结果={large_result}, 耗时={end_time - start_time:.4f}秒")
        
        # 边界情况测试
        print("边界情况测试：")
        print(f"空数组: {Knapsack2.knapsack2(100, [], [])}")
        print(f"W=0: {Knapsack2.knapsack2(0, [10], [5])}")
        print(f"所有物品超重: {Knapsack2.knapsack2(5, [10, 20], [5, 10])}")
        
        # 特殊测试：价值为0的物品
        print("价值为0的物品测试：")
        special_result = Knapsack2.knapsack2(100, [10, 20], [0, 0])
        print(f"特殊测试结果: {special_result}")

def main():
    """
    主函数 - 运行测试和演示
    """
    try:
        Knapsack2.run_tests()
    except Exception as e:
        print(f"程序执行错误: {e}")
        return 1
    return 0

if __name__ == "__main__":
    sys.exit(main())

"""
复杂度分析：

方法1：价值维度DP
- 时间复杂度：O(N * totalValue)
  - N: 物品数量
  - totalValue: 总价值
- 空间复杂度：O(totalValue)

方法2：优化的价值维度DP
- 时间复杂度：O(N * totalValue)（与方法1相同）
- 空间复杂度：O(totalValue)

方法3：传统重量维度DP
- 时间复杂度：O(N * W)
- 空间复杂度：O(W)

Python特定优化：
1. 使用列表推导式和生成器表达式
2. 利用Python的动态类型特性
3. 使用类型注解提高代码可读性
4. 使用random模块进行性能测试

关键点分析：
1. 问题转化：当W很大时，从重量维度转为价值维度
2. 状态定义：dp[i]表示达到价值i所需的最小重量
3. 结果提取：从后向前遍历找到第一个满足重量约束的价值
4. 适用场景：W很大但总价值不大的情况

工程化考量：
1. 方法选择：根据W的大小选择合适的算法
2. 内存优化：限制传统方法的最大容量
3. 边界处理：处理各种极端情况
4. 性能测试：包含大规模数据测试

面试要点：
1. 理解传统DP的局限性
2. 掌握问题转化的思路
3. 了解不同维度DP的适用场景
4. 能够分析算法的时空复杂度

扩展应用：
1. 资源分配问题
2. 投资组合优化
3. 多约束优化问题
4. 大规模数据处理
"""