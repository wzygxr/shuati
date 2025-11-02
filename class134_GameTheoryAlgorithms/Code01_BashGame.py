"""
巴什博弈(Bash Game) - Python实现

题目来源：
1. LeetCode 292. Nim Game (简化版巴什博弈): https://leetcode.com/problems/nim-game/
2. HDU 1846. Brave Game (经典巴什博弈): http://acm.hdu.edu.cn/showproblem.php?pid=1846
3. POJ 2313. Bash Game (巴什博弈变形): http://poj.org/problem?id=2313
4. 牛客网 NC13685. 取石子游戏 (企业笔试题目): https://www.nowcoder.com/practice/f6153503169545229c77481040056a63
5. 洛谷 P4018: https://www.luogu.com.cn/problem/P4018

算法思路：
1. 巴什博弈是博弈论中最基础的模型之一
2. 核心定理：当石子总数n是(m+1)的倍数时，后手必胜；否则先手必胜
3. 数学原理：先手可以通过控制每次取石子后剩余石子数为(m+1)的倍数来确保胜利

时间复杂度：O(1) - 只需要进行一次取模运算
空间复杂度：O(1) - 只使用了常数级别的额外空间
是否最优解：✅ 是 - 基于数学定理的最优解

Python特性利用：
1. 使用字典进行记忆化搜索
2. 利用Python的动态类型和简洁语法
3. 使用装饰器进行性能测试
4. 利用断言进行单元测试
"""

import random
import time
from functools import lru_cache
from typing import Dict, Tuple

class BashGame:
    """巴什博弈算法类"""
    
    def __init__(self):
        self.dp_cache: Dict[Tuple[int, int], str] = {}
    
    def bash_game_dp(self, n: int, m: int) -> str:
        """
        动态规划解法 - 用于验证数学规律的正确性
        
        Args:
            n: 石子总数
            m: 每次最多可取的石子数
            
        Returns:
            str: "先手" 或 "后手"
            
        时间复杂度：O(n * m)
        空间复杂度：O(n * m)
        
        算法思路：
        1. dp[i]表示有i个石子时当前玩家的胜负情况
        2. True表示当前玩家必胜，False表示当前玩家必败
        3. 对于每个状态i，尝试所有可能的取法(1到min(m,i))
        4. 如果存在一种取法使得对手处于必败状态，则当前玩家必胜
        """
        # 参数校验
        if n < 0 or m <= 0:
            raise ValueError("参数不合法：n不能为负数，m必须为正整数")
        
        # 边界条件处理
        if n == 0:
            return "后手"
        
        # 记忆化搜索
        if (n, m) in self.dp_cache:
            return self.dp_cache[(n, m)]
        
        result = "后手"  # 默认当前玩家必败
        
        # 限制递归深度，避免栈溢出
        if n > 1000:  # 对于大规模数据，直接使用数学解法
            return self.bash_game_math(n, m)
        
        # 尝试所有可能的取法（1到min(m, n)）
        for pick in range(1, min(m, n) + 1):
            # 如果对手在剩余石子中处于必败态，则当前玩家必胜
            if self.bash_game_dp(n - pick, m) == "后手":
                result = "先手"
                break  # 找到一个必胜策略即可退出
        
        self.dp_cache[(n, m)] = result
        return result
    
    @staticmethod
    def bash_game_math(n: int, m: int) -> str:
        """
        数学解法 - 基于巴什博弈定理的最优解
        
        Args:
            n: 石子总数
            m: 每次最多可取的石子数
            
        Returns:
            str: "先手" 或 "后手"
            
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        算法原理：
        1. 当n = k*(m+1)时，无论先手取多少石子（1到m个），后手总可以取相应的石子使得剩余石子数仍然是(m+1)的倍数
        2. 当n ≠ k*(m+1)时，先手可以一次取走n % (m+1)个石子，使得剩余石子数是(m+1)的倍数，从而将必败局面留给对手
        """
        # 参数校验
        if n < 0:
            raise ValueError("石子数量不能为负数")
        if m <= 0:
            raise ValueError("每次可取石子数必须为正整数")
        
        # 特殊边界情况处理
        if n == 0:
            return "后手"
        
        # 巴什博弈核心定理
        return "先手" if n % (m + 1) != 0 else "后手"
    
    @staticmethod
    def bash_game_misere(n: int, m: int) -> str:
        """
        变种问题：最后取石子者失败
        
        Args:
            n: 石子总数
            m: 每次最多可取的石子数
            
        Returns:
            str: "先手" 或 "后手"
        """
        if n <= 0 or m <= 0:
            raise ValueError("参数不合法")
        
        # 特殊情况：只有1颗石子时，先手必败（必须取走最后一颗）
        if n == 1:
            return "后手"
        
        # 变种巴什博弈定理
        return "先手" if (n - 1) % (m + 1) != 0 else "后手"
    
    def validate_algorithm(self, test_times: int = 1000, max_n: int = 500) -> None:
        """验证算法正确性"""
        print("开始验证算法正确性...")
        error_count = 0
        
        for i in range(test_times):
            n = random.randint(0, max_n)
            m = random.randint(1, max_n)
            
            try:
                dp_result = self.bash_game_dp(n, m)
                math_result = self.bash_game_math(n, m)
                
                if dp_result != math_result:
                    error_count += 1
                    print(f"发现不一致：n={n}, m={m}, DP={dp_result}, Math={math_result}")
            except Exception as e:
                print(f"测试异常：n={n}, m={m}, 错误：{e}")
                error_count += 1
        
        print(f"验证完成：测试{test_times}次，错误{error_count}次")
        if error_count == 0:
            print("✅ 算法验证通过，数学解法正确！")
        else:
            print("❌ 算法存在错误，需要调试！")
    
    def performance_test(self) -> None:
        """性能测试"""
        print("开始性能测试...")
        
        large_n = 1000000
        m = 3
        
        # 测试数学解法
        start_time = time.time()
        math_result = self.bash_game_math(large_n, m)
        math_time = (time.time() - start_time) * 1e6  # 转换为微秒
        
        # 对于大规模数据，直接使用数学解法，避免递归深度问题
        small_n = 100  # 减小测试规模
        start_time = time.time()
        dp_result = self.bash_game_dp(small_n, m)
        dp_time = (time.time() - start_time) * 1e6  # 转换为微秒
        
        print(f"数学解法（n=1,000,000）：结果={math_result}，耗时={math_time:.3f}微秒")
        print(f"动态规划（n=100）：结果={dp_result}，耗时={dp_time:.3f}微秒")
        if math_time > 0:
            print(f"性能提升倍数：≈{dp_time / math_time:.0f}倍")
        else:
            print("数学解法耗时过短，无法计算性能提升倍数")
    
    @staticmethod
    def unit_test() -> None:
        """单元测试"""
        print("开始单元测试...")
        
        game = BashGame()
        
        # 测试用例1：正常情况
        assert game.bash_game_math(10, 3) == "先手", "测试用例1失败"
        assert game.bash_game_math(12, 3) == "后手", "测试用例2失败"
        
        # 测试用例2：边界情况
        assert game.bash_game_math(0, 3) == "后手", "测试用例3失败"
        assert game.bash_game_math(1, 3) == "先手", "测试用例4失败"
        
        # 测试用例3：变种问题
        assert game.bash_game_misere(1, 3) == "后手", "测试用例5失败"
        # 修复断言错误：变种问题的正确结果
        # 修复变种问题的断言
        # 对于n=5, m=3: (5-1) % (3+1) = 4 % 4 = 0，所以应该是后手胜
        assert game.bash_game_misere(5, 3) == "后手", "测试用例6失败"
        
        # 测试用例4：异常输入
        try:
            game.bash_game_math(-1, 3)
            assert False, "测试用例7失败：应该抛出异常"
        except ValueError:
            pass  # 预期异常
        
        print("✅ 所有单元测试通过！")
    
    def demo(self) -> None:
        """算法演示"""
        print("=== 巴什博弈算法演示（Python实现）===")
        
        # 1. 单元测试
        self.unit_test()
        
        # 2. 算法验证
        self.validate_algorithm(100, 500)
        
        # 3. 性能测试
        self.performance_test()
        
        # 4. 实际应用示例
        print("\n=== 实际应用示例 ===")
        test_cases = [
            (10, 3),   # LeetCode 292类似场景
            (15, 4),   # HDU 1846类似场景
            (20, 5),   # POJ 2313类似场景
            (100, 10)  # 大规模测试
        ]
        
        for n, m in test_cases:
            winner = self.bash_game_math(n, m)
            print(f"石子数={n}, 每次最多取={m} → {winner}获胜")
        
        print("\n=== 各大平台题目链接 ===")
        print("1. LeetCode 292: https://leetcode.com/problems/nim-game/")
        print("2. HDU 1846: http://acm.hdu.edu.cn/showproblem.php?pid=1846")
        print("3. POJ 2313: http://poj.org/problem?id=2313")
        print("4. 牛客网 NC13685: https://www.nowcoder.com/practice/f6153503169545229c77481040056a63")
        print("5. 洛谷 P4018: https://www.luogu.com.cn/problem/P4018")
        print("6. HackerRank Game of Stones: https://www.hackerrank.com/challenges/game-of-stones")
        print("7. CodeChef STONEGAM: https://www.codechef.com/problems/STONEGAM")
        print("8. Project Euler Problem 301: https://projecteuler.net/problem=301")


def main():
    """主函数"""
    game = BashGame()
    game.demo()


if __name__ == "__main__":
    main()