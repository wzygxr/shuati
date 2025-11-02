"""
LeetCode 233. 数字 1 的个数 - Python实现
题目链接：https://leetcode.cn/problems/number-of-digit-one/

题目描述：
给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。

解题思路：
使用数位动态规划（Digit DP）解决该问题。核心思想是逐位统计数字1的出现次数。

算法分析：
时间复杂度：O(L²) 其中L是数字n的位数
空间复杂度：O(L²) 用于存储DP状态

Python实现特点：
1. 使用lru_cache实现自动记忆化
2. 使用装饰器简化代码结构
3. 支持大整数运算

最优解分析：
这是数位DP的标准解法，对于此类计数问题是最优解。数学方法虽然可以达到O(L)时间复杂度，
但数位DP方法更加通用，易于扩展到其他数字统计问题。

工程化考量：
1. 异常处理：处理n为负数的情况
2. 边界测试：测试n=0, n=1等边界情况
3. 性能优化：使用记忆化搜索避免重复计算
4. 代码可读性：清晰的变量命名和详细注释

相关题目链接：
- LeetCode 233: https://leetcode.cn/problems/number-of-digit-one/
- 剑指Offer 43: https://leetcode.cn/problems/1nzheng-shu-zhong-1chu-xian-de-ci-shu-lcof/

多语言实现：
- Java: LeetCode233_NumberOfDigitOne.java
- Python: LeetCode233_NumberOfDigitOne.py
- C++: LeetCode233_NumberOfDigitOne.cpp
"""

from functools import lru_cache
import time

class Solution:
    def countDigitOne(self, n: int) -> int:
        """
        主函数：计算所有小于等于n的非负整数中数字1出现的个数
        
        Args:
            n: 目标数字
            
        Returns:
            int: 数字1出现的总次数
            
        时间复杂度：O(L²) 其中L是数字n的位数
        空间复杂度：O(L²) 用于存储DP状态
        """
        if n < 0:
            return 0
            
        # 将数字转换为字符串，便于按位处理
        s = str(n)
        length = len(s)
        
        @lru_cache(maxsize=None)
        def dfs(pos: int, count: int, is_limit: bool, is_lead: bool) -> int:
            """
            数位DP核心递归函数
            
            Args:
                pos: 当前处理的位置
                count: 已经统计到的数字1的个数
                is_limit: 是否受到上界限制
                is_lead: 是否有前导零
                
            Returns:
                int: 从当前状态开始，满足条件的数字个数
            """
            # 递归终止条件：处理完所有数位
            if pos == length:
                return count
                
            result = 0
            # 确定当前位可以填入的数字范围
            upper = int(s[pos]) if is_limit else 9
            
            for digit in range(0, upper + 1):
                # 计算新的count值
                new_count = count + (1 if digit == 1 else 0)
                # 计算新的限制状态
                new_limit = is_limit and (digit == upper)
                new_lead = is_lead and (digit == 0)
                
                result += dfs(pos + 1, new_count, new_limit, new_lead)
                
            return result
            
        # 从最高位开始进行数位DP
        return dfs(0, 0, True, True)

class SolutionMath:
    """
    数学方法实现 - 最优解，时间复杂度O(L)，空间复杂度O(1)
    这是该问题的最优数学解法，但只适用于统计特定数字出现次数的问题
    """
    def countDigitOne(self, n: int) -> int:
        if n <= 0:
            return 0
            
        count = 0
        factor = 1
        
        while n // factor != 0:
            # 计算当前位的高位、当前位、低位
            higher = n // (factor * 10)
            curr = (n // factor) % 10
            lower = n - (n // factor) * factor
            
            if curr == 0:
                count += higher * factor
            elif curr == 1:
                count += higher * factor + lower + 1
            else:
                count += (higher + 1) * factor
                
            factor *= 10
            
        return count

def test_solution():
    """测试函数，验证算法正确性"""
    solution_dp = Solution()
    solution_math = SolutionMath()
    
    # 测试用例1：n=13
    test_cases = [
        (13, 6),    # 1,10,11,12,13 → 6次
        (0, 0),     # 边界情况
        (100, 21),  # 典型情况
        (1, 1),     # 最小正整数
        (10, 2),    # 10包含1个1
        (11, 4),    # 10,11 → 1+2=3？实际是1,10,11 → 1+1+2=4
    ]
    
    print("数位DP vs 数学方法 对比测试")
    print("=" * 50)
    
    for i, (n, expected) in enumerate(test_cases, 1):
        # 数位DP方法
        start_time = time.time()
        result_dp = solution_dp.countDigitOne(n)
        dp_time = time.time() - start_time
        
        # 数学方法
        start_time = time.time()
        result_math = solution_math.countDigitOne(n)
        math_time = time.time() - start_time
        
        status_dp = "通过" if result_dp == expected else "失败"
        status_math = "通过" if result_math == expected else "失败"
        
        print(f"测试用例{i}: n = {n}")
        print(f"期望结果: {expected}")
        print(f"数位DP结果: {result_dp} ({status_dp}), 耗时: {dp_time:.6f}秒")
        print(f"数学方法结果: {result_math} ({status_math}), 耗时: {math_time:.6f}秒")
        
        if dp_time > 0 and math_time > 0:
            speed_ratio = dp_time / math_time
            print(f"数学方法比数位DP快 {speed_ratio:.2f} 倍")
        print("-" * 30)

def performance_test():
    """性能测试函数"""
    solution_dp = Solution()
    solution_math = SolutionMath()
    
    large_numbers = [10**6, 10**7, 10**8, 10**9]
    
    print("性能测试 - 大数情况")
    print("=" * 50)
    
    for n in large_numbers:
        print(f"测试 n = {n}")
        
        # 数位DP方法
        start_time = time.time()
        result_dp = solution_dp.countDigitOne(n)
        dp_time = time.time() - start_time
        
        # 数学方法
        start_time = time.time()
        result_math = solution_math.countDigitOne(n)
        math_time = time.time() - start_time
        
        print(f"数位DP结果: {result_dp}, 耗时: {dp_time:.4f}秒")
        print(f"数学方法结果: {result_math}, 耗时: {math_time:.4f}秒")
        
        if result_dp == result_math:
            print("✓ 结果一致")
            if math_time > 0:
                speedup = dp_time / math_time
                print(f"数学方法快 {speedup:.2f} 倍")
        else:
            print("✗ 结果不一致")
        print("-" * 30)

def edge_case_test():
    """边界情况测试"""
    solution_dp = Solution()
    solution_math = SolutionMath()
    
    edge_cases = [-1, 0, 1, 9, 10, 99, 100, 999, 1000]
    
    print("边界情况测试")
    print("=" * 50)
    
    for n in edge_cases:
        result_dp = solution_dp.countDigitOne(n)
        result_math = solution_math.countDigitOne(n)
        
        status = "通过" if result_dp == result_math else "失败"
        print(f"n = {n:4d}: 数位DP = {result_dp:4d}, 数学方法 = {result_math:4d} [{status}]")

if __name__ == "__main__":
    # 运行所有测试
    test_solution()
    print("\n")
    performance_test()
    print("\n")
    edge_case_test()
    
    # 结论分析
    print("\n" + "="*60)
    print("算法分析总结:")
    print("1. 数位DP方法: 通用性强，易于扩展到其他数字约束问题")
    print("2. 数学方法: 针对特定问题最优，时间复杂度O(L)，空间复杂度O(1)")
    print("3. 实际应用: 对于统计特定数字出现次数的问题，推荐使用数学方法")
    print("4. 学习价值: 数位DP方法有助于理解动态规划在数字问题中的应用")