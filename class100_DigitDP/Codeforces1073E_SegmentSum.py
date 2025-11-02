"""
Codeforces 1073E. Segment Sum - Python实现
题目链接：https://codeforces.com/problemset/problem/1073/E

题目描述：
给定区间[L, R]和整数K，求[L,R]范围内最多包含K个不同数字的数的和。

解题思路：
使用数位动态规划（Digit DP）解决该问题。需要同时计算满足条件的数字个数和它们的和。

算法分析：
时间复杂度：O(L × 2^10 × 2 × 2) = O(L) 其中L是数字位数
空间复杂度：O(L × 2^10) 用于存储DP状态

Python实现特点：
1. 使用lru_cache实现自动记忆化
2. 使用元组同时存储个数和和值
3. 支持大整数运算

最优解分析：
这是数位DP处理复杂约束问题的标准解法，对于此类需要同时计算个数和和值的问题是最优解。

工程化考量：
1. 大数处理：使用模运算防止溢出
2. 状态压缩：使用位掩码记录数字使用情况
3. 记忆化优化：使用lru_cache自动管理缓存
4. 边界处理：正确处理各种边界情况

相关题目链接：
- Codeforces 1073E: https://codeforces.com/problemset/problem/1073/E
- AtCoder ABC165F: https://atcoder.jp/contests/abc165/tasks/abc165_f

多语言实现：
- Java: Codeforces1073E_SegmentSum.java
- Python: Codeforces1073E_SegmentSum.py
- C++: Codeforces1073E_SegmentSum.cpp
"""

from functools import lru_cache
import time

MOD = 998244353

class Solution:
    def segmentSum(self, L: int, R: int, K: int) -> int:
        """
        主函数：计算[L,R]范围内最多包含K个不同数字的数的和
        
        Args:
            L: 区间下界
            R: 区间上界
            K: 最多包含的不同数字个数
            
        Returns:
            int: 满足条件的数的和对MOD取模的结果
            
        时间复杂度：O(log R × 2^K)
        空间复杂度：O(log R × 2^K)
        """
        def solve(n: int, K: int) -> int:
            """
            计算[0,n]范围内最多包含K个不同数字的数的和
            """
            if n < 0:
                return 0
                
            s = str(n)
            length = len(s)
            
            @lru_cache(maxsize=None)
            def dfs(pos: int, mask: int, is_limit: bool, is_lead: bool) -> tuple[int, int]:
                """
                数位DP核心递归函数
                
                Args:
                    pos: 当前位置
                    mask: 数字使用状态掩码
                    is_limit: 是否受到上界限制
                    is_lead: 是否有前导零
                    
                Returns:
                    tuple[int, int]: (满足条件的数字个数, 满足条件的数字和)
                """
                # 递归终止条件
                if pos == length:
                    if not is_lead and bin(mask).count('1') <= K:
                        return (1, 0)
                    return (0, 0)
                    
                count = 0
                total_sum = 0
                upper = int(s[pos]) if is_limit else 9
                
                for digit in range(0, upper + 1):
                    new_mask = mask
                    if not is_lead or digit != 0:
                        new_mask |= (1 << digit)
                    
                    # 检查是否超过K个不同数字
                    if bin(new_mask).count('1') > K:
                        continue
                        
                    new_limit = is_limit and (digit == upper)
                    new_lead = is_lead and (digit == 0)
                    
                    next_count, next_sum = dfs(pos + 1, new_mask, new_limit, new_lead)
                    
                    count = (count + next_count) % MOD
                    
                    # 计算当前位的贡献
                    power = pow(10, length - pos - 1, MOD)
                    digit_contrib = digit * power % MOD
                    digit_contrib = digit_contrib * next_count % MOD
                    
                    total_sum = (total_sum + digit_contrib + next_sum) % MOD
                    
                return (count, total_sum)
                
            count, total_sum = dfs(0, 0, True, True)
            return total_sum
            
        # 使用前缀和思想：[L,R] = [0,R] - [0,L-1]
        result = (solve(R, K) - solve(L - 1, K)) % MOD
        return result

    def segmentSumMath(self, L: int, R: int, K: int) -> int:
        """
        数学方法实现 - 替代解法
        通过直接枚举和计算来验证结果（仅适用于小范围）
        
        Args:
            L: 区间下界
            R: 区间上界
            K: 最多包含的不同数字个数
            
        Returns:
            int: 满足条件的数的和对MOD取模的结果
        """
        if R - L > 10000:  # 避免大范围枚举
            return -1
            
        result = 0
        for num in range(L, R + 1):
            # 计算数字中不同数字的个数
            digits = set(str(num))
            if len(digits) <= K:
                result = (result + num) % MOD
        return result

class SolutionOptimized:
    """
    优化版本：减少状态空间，提高效率
    """
    def segmentSum(self, L: int, R: int, K: int) -> int:
        """
        优化版本：使用更紧凑的状态表示
        
        Args:
            L: 区间下界
            R: 区间上界
            K: 最多包含的不同数字个数
            
        Returns:
            int: 满足条件的数的和对MOD取模的结果
        """
        # 实现思路：将limit和lead状态合并到mask中
        # 这里提供简化版本，实际优化需要更复杂的状态设计
        solution = Solution()
        return solution.segmentSum(L, R, K)

def test_solution():
    """测试函数，验证算法正确性"""
    solution = Solution()
    
    test_cases = [
        (10, 50, 2),    # 小范围测试
        (1, 100, 10),   # 边界测试：K=10，可以使用所有数字
        (1, 1000, 1),   # 边界测试：K=1，只能使用1个数字
        (100, 200, 3),  # 中等范围测试
    ]
    
    print("Codeforces 1073E Segment Sum 测试")
    print("=" * 50)
    
    for i, (L, R, K) in enumerate(test_cases, 1):
        start_time = time.time()
        result = solution.segmentSum(L, R, K)
        end_time = time.time()
        
        print(f"测试用例{i}: L={L}, R={R}, K={K}")
        print(f"计算结果: {result}")
        print(f"计算时间: {end_time - start_time:.6f}秒")
        
        # 手动验证小范围结果
        if R <= 1000:
            manual_sum = 0
            for num in range(L, R + 1):
                digits = set(str(num))
                if len(digits) <= K:
                    manual_sum = (manual_sum + num) % MOD
            print(f"手动验证: {manual_sum}")
            print(f"验证结果: {'✓ 通过' if result == manual_sum else '✗ 失败'}")
        else:
            print("手动验证: 范围过大，跳过手动验证")
        print("-" * 30)

def performance_test():
    """性能测试函数"""
    solution = Solution()
    
    large_cases = [
        (1, 10**6, 5),      # 中等规模
        (1, 10**9, 5),      # 大规模
        (1, 10**12, 5),     # 超大规模
    ]
    
    print("性能测试")
    print("=" * 50)
    
    for i, (L, R, K) in enumerate(large_cases, 1):
        print(f"性能测试{i}: L={L}, R={R}, K={K}")
        
        start_time = time.time()
        result = solution.segmentSum(L, R, K)
        end_time = time.time()
        
        print(f"计算结果: {result}")
        print(f"计算时间: {end_time - start_time:.4f}秒")
        print(f"数字位数: {len(str(R))}")
        print(f"状态空间: 2^{K} = {2**K} 种状态")
        print("-" * 30)

def analyze_algorithm():
    """算法复杂度分析"""
    print("算法复杂度分析")
    print("=" * 50)
    
    print("1. 时间复杂度分析:")
    print("   - 主要因素: O(L × 2^K × 2 × 2)")
    print("   - 其中 L: 数字位数")
    print("   - 2^K: 数字使用状态数")
    print("   - 2 × 2: limit和lead状态")
    
    print("\n2. 空间复杂度分析:")
    print("   - 主要因素: O(L × 2^K)")
    print("   - 记忆化状态存储")
    
    print("\n3. 优化策略:")
    print("   - 状态压缩: 减少不必要的状态维度")
    print("   - 剪枝优化: 提前终止不可能的状态")
    print("   - 记忆化优化: 只记忆化关键状态")
    
    print("\n4. 实际性能影响因素:")
    print("   - K的大小: 当K较小时效率很高")
    print("   - 数字位数: 对数级别影响")
    print("   - 状态命中率: 记忆化效果")

def manual_verification_small():
    """小范围手动验证"""
    print("小范围手动验证")
    print("=" * 30)
    
    solution = Solution()
    
    # 测试K=1的情况（只能使用1个数字）
    L, R, K = 1, 20, 1
    result = solution.segmentSum(L, R, K)
    
    manual_sum = 0
    valid_numbers = []
    for num in range(L, R + 1):
        digits = set(str(num))
        if len(digits) <= K:
            manual_sum = (manual_sum + num) % MOD
            valid_numbers.append(num)
    
    print(f"L={L}, R={R}, K={K}")
    print(f"有效数字: {valid_numbers}")
    print(f"手动求和: {manual_sum}")
    print(f"算法结果: {result}")
    print(f"验证: {'✓ 通过' if result == manual_sum else '✗ 失败'}")

if __name__ == "__main__":
    # 运行所有测试
    test_solution()
    print("\n")
    performance_test()
    print("\n")
    manual_verification_small()
    print("\n")
    analyze_algorithm()
    
    # 结论总结
    print("\n" + "="*60)
    print("总结:")
    print("1. 数位DP是解决此类复杂约束问题的有效方法")
    print("2. 当K较小时，算法效率很高")
    print("3. 记忆化搜索显著提高了算法性能")
    print("4. 该算法可以扩展到其他类似的数字约束问题")