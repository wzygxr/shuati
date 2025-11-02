import sys
from typing import List, Tuple
import random
import time

# HDU 2955 Robberies
# 题目描述：抢劫犯想抢劫银行，每个银行有一定的金额和被抓的概率。
# 抢劫犯希望在被抓概率不超过某个值的情况下，抢劫到最多的钱。
# 链接：https://acm.hdu.edu.cn/showproblem.php?pid=2955
# 
# 解题思路：
# 这是一个概率背包问题，需要将问题转化为标准的01背包问题。
# 关键点：将金额作为背包容量，将安全概率（1-被抓概率）作为价值。
# 
# 状态定义：dp[i] 表示抢劫到金额i时的最大安全概率
# 状态转移方程：dp[i] = max(dp[i], dp[i-money[j]] * (1-p[j]))
# 初始状态：dp[0] = 1（抢劫0元时安全概率为1）
# 
# 时间复杂度：O(N * totalMoney)，其中N是银行数量，totalMoney是总金额
# 空间复杂度：O(totalMoney)，使用一维DP数组
# 
# 工程化考量：
# 1. 精度处理：使用float类型处理概率
# 2. 边界条件：处理概率为0或1的情况
# 3. 性能优化：计算总金额作为背包容量上限
# 4. 异常处理：处理非法输入

class Robberies:
    """
    抢劫银行问题的多种解法
    """
    
    @staticmethod
    def rob(P: float, money: List[int], prob: List[float]) -> float:
        """
        动态规划解法 - 概率背包问题
        
        Args:
            P: 最大允许被抓概率
            money: 每个银行的金额数组
            prob: 每个银行的被抓概率数组
            
        Returns:
            float: 在安全概率范围内的最大抢劫金额
        """
        # 参数验证
        if len(money) != len(prob):
            raise ValueError("Money and probability arrays must have same size")
        if P < 0 or P > 1:
            raise ValueError("Probability P must be between 0 and 1")
        
        n = len(money)
        if n == 0:
            return 0
        
        # 计算总金额作为背包容量上限
        total_money = sum(money)
        
        # 创建DP数组，dp[i]表示抢劫到金额i时的最大安全概率
        dp = [0.0] * (total_money + 1)
        dp[0] = 1.0  # 抢劫0元时安全概率为1
        
        # 01背包：遍历每个银行
        for i in range(n):
            m = money[i]
            p = prob[i]
            safe_prob = 1 - p  # 安全概率
            
            # 倒序遍历金额，避免重复选择
            for j in range(total_money, m - 1, -1):
                if dp[j - m] > 0:
                    dp[j] = max(dp[j], dp[j - m] * safe_prob)
        
        # 寻找最大的金额，使得安全概率 >= 1-P
        min_safe_prob = 1 - P
        max_money = 0
        for j in range(total_money, -1, -1):
            if dp[j] >= min_safe_prob:
                max_money = j
                break
        
        return max_money
    
    @staticmethod
    def rob_optimized(P: float, money: List[int], prob: List[float]) -> float:
        """
        优化的动态规划解法 - 提前终止遍历
        
        Args:
            P: 最大允许被抓概率
            money: 每个银行的金额数组
            prob: 每个银行的被抓概率数组
            
        Returns:
            float: 在安全概率范围内的最大抢劫金额
        """
        if len(money) != len(prob):
            raise ValueError("Money and probability arrays must have same size")
        if P < 0 or P > 1:
            raise ValueError("Probability P must be between 0 and 1")
        
        n = len(money)
        if n == 0:
            return 0
        
        # 计算总金额
        total_money = sum(money)
        
        dp = [0.0] * (total_money + 1)
        dp[0] = 1.0
        
        for i in range(n):
            m = money[i]
            safe_prob = 1 - prob[i]
            
            for j in range(total_money, m - 1, -1):
                if dp[j - m] > 0:
                    new_prob = dp[j - m] * safe_prob
                    if new_prob > dp[j]:
                        dp[j] = new_prob
        
        min_safe_prob = 1 - P
        # 从大到小遍历，找到第一个满足条件的金额
        for j in range(total_money, -1, -1):
            if dp[j] >= min_safe_prob:
                return j
        
        return 0
    
    @staticmethod
    def rob_alternative(P: float, money: List[int], prob: List[float]) -> float:
        """
        另一种思路：将金额作为价值，概率作为约束
        
        Args:
            P: 最大允许被抓概率
            money: 每个银行的金额数组
            prob: 每个银行的被抓概率数组
            
        Returns:
            float: 在安全概率范围内的最大抢劫金额
        """
        if len(money) != len(prob):
            raise ValueError("Money and probability arrays must have same size")
        if P < 0 or P > 1:
            raise ValueError("Probability P must be between 0 and 1")
        
        n = len(money)
        if n == 0:
            return 0
        
        # 计算总金额
        total_money = sum(money)
        
        # dp[i]表示达到金额i所需的最小被抓概率
        dp = [1.0] * (total_money + 1)  # 初始化为最大概率1
        dp[0] = 0.0  # 抢劫0元时被抓概率为0
        
        for i in range(n):
            m = money[i]
            p = prob[i]
            
            for j in range(total_money, m - 1, -1):
                # 计算选择当前银行的被抓概率
                new_prob = 1 - (1 - dp[j - m]) * (1 - p)
                if new_prob < dp[j]:
                    dp[j] = new_prob
        
        # 寻找最大的金额，使得被抓概率 <= P
        for j in range(total_money, -1, -1):
            if dp[j] <= P:
                return j
        
        return 0
    
    @staticmethod
    def run_tests():
        """
        运行测试用例，验证所有方法的正确性
        """
        # 测试用例
        P_list = [0.1, 0.05, 0.5]
        money_cases = [
            [10, 20, 30],
            [1, 2, 3, 4],
            [100, 200, 300]
        ]
        prob_cases = [
            [0.05, 0.1, 0.2],
            [0.01, 0.02, 0.03, 0.04],
            [0.3, 0.2, 0.1]
        ]
        
        print("抢劫银行问题测试：")
        for i, (P, money, prob) in enumerate(zip(P_list, money_cases, prob_cases)):
            try:
                result1 = Robberies.rob(P, money, prob)
                result2 = Robberies.rob_optimized(P, money, prob)
                result3 = Robberies.rob_alternative(P, money, prob)
                
                print(f"P={P}, money={money}, prob={prob}: "
                      f"方法1={result1}, 方法2={result2}, 方法3={result3}")
                
                # 验证结果一致性（允许小的浮点数误差）
                if abs(result1 - result2) > 1e-6 or abs(result2 - result3) > 1e-6:
                    print("警告：不同方法结果不一致！")
                    
            except Exception as e:
                print(f"测试用例{i+1}时发生错误: {e}")
        
        # 性能测试 - 大规模数据
        n = 50
        large_money = [random.randint(1, 1000) for _ in range(n)]
        large_prob = [random.uniform(0, 0.1) for _ in range(n)]
        
        start_time = time.time()
        large_result = Robberies.rob_optimized(0.1, large_money, large_prob)
        end_time = time.time()
        
        print(f"大规模测试: 银行数量={n}, 结果={large_result}, "
              f"耗时={end_time - start_time:.4f}秒")
        
        # 边界情况测试
        print("边界情况测试：")
        print(f"空数组: {Robberies.rob(0.1, [], [])}")
        print(f"P=0: {Robberies.rob(0.0, [10], [0.1])}")
        print(f"P=1: {Robberies.rob(1.0, [10, 20], [0.1, 0.2])}")
        
        # 特殊测试：概率为0的银行
        print("概率为0的银行测试：")
        special_result = Robberies.rob(0.01, [100, 200], [0.0, 0.0])
        print(f"特殊测试结果: {special_result}")

def main():
    """
    主函数 - 运行测试和演示
    """
    try:
        Robberies.run_tests()
    except Exception as e:
        print(f"程序执行错误: {e}")
        return 1
    return 0

if __name__ == "__main__":
    sys.exit(main())

"""
复杂度分析：

方法1：动态规划（概率背包）
- 时间复杂度：O(N * totalMoney)
  - N: 银行数量
  - totalMoney: 总金额
- 空间复杂度：O(totalMoney)

方法2：优化的动态规划
- 时间复杂度：O(N * totalMoney)（与方法1相同）
- 空间复杂度：O(totalMoney)

方法3：替代思路的动态规划
- 时间复杂度：O(N * totalMoney)
- 空间复杂度：O(totalMoney)

Python特定优化：
1. 使用列表推导式和生成器表达式
2. 利用Python的动态类型特性
3. 使用类型注解提高代码可读性
4. 使用random模块进行性能测试

关键点分析：
1. 问题转化：将概率问题转化为标准的背包问题
2. 精度处理：使用float类型处理概率计算
3. 状态定义：dp[i]表示金额i对应的最大安全概率
4. 结果提取：从后向前遍历找到第一个满足条件的金额

工程化考量：
1. 模块化设计：将不同解法封装为静态方法
2. 异常处理：完善的参数验证和错误处理
3. 性能监控：包含性能测试和时间测量
4. 测试覆盖：包含各种边界情况和性能测试

面试要点：
1. 理解概率背包问题的转化思路
2. 掌握浮点数精度处理技巧
3. 了解不同状态定义对算法的影响
4. 能够分析算法的时空复杂度

扩展应用：
1. 风险管理问题
2. 投资组合优化
3. 资源分配问题
4. 多约束优化问题
"""