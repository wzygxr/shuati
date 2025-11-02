"""
AtCoder ABC135 D - Digits Parade
题目链接：https://atcoder.jp/contests/abc135/tasks/abc135_d

题目描述：
给定一个由数字和'?'组成的字符串S，'?'可以替换成0-9的任意数字。
求有多少种替换方案使得结果能被13整除，结果对10^9+7取模。

解题思路：
1. 动态规划方法：使用DP框架，逐位确定数字
2. 状态设计需要记录：
   - 当前处理位置
   - 当前数字对13的余数
3. 关键点：'?'可以替换为0-9的任意数字

时间复杂度分析：
- 状态数：字符串长度 × 13 ≈ 10^5 × 13 = 1.3×10^6
- 每个状态处理最多10种选择
- 总复杂度：O(13×10^5) 在可接受范围内

空间复杂度分析：
- DP数组：10^5 × 13 ≈ 1.3MB

最优解分析：
这是标准的最优解，利用DP处理模运算和通配符替换
"""

class DigitsParade:
    MOD = 10**9 + 7
    DIVISOR = 13
    
    def count_divisible_by_13(self, s: str) -> int:
        """
        计算有多少种替换方案使得结果能被13整除
        时间复杂度: O(n * 13)
        空间复杂度: O(n * 13)
        """
        n = len(s)
        
        # dp[i][r] 表示处理到第i位，当前余数为r的方案数
        dp = [[0] * self.DIVISOR for _ in range(n + 1)]
        dp[0][0] = 1  # 初始状态：余数为0有1种方案（空数字）
        
        # 从高位到低位动态规划
        for i in range(n):
            for r in range(self.DIVISOR):
                if dp[i][r] == 0:
                    continue
                
                if s[i] == '?':
                    # '?'可以替换为0-9的任意数字
                    for d in range(10):
                        new_r = (r * 10 + d) % self.DIVISOR
                        dp[i + 1][new_r] = (dp[i + 1][new_r] + dp[i][r]) % self.MOD
                else:
                    # 固定数字
                    d = int(s[i])
                    new_r = (r * 10 + d) % self.DIVISOR
                    dp[i + 1][new_r] = (dp[i + 1][new_r] + dp[i][r]) % self.MOD
        
        return dp[n][0]
    
    def count_divisible_by_13_dfs(self, s: str) -> int:
        """
        使用记忆化DFS的替代解法（更符合数位DP传统风格）
        时间复杂度: O(n * 13)
        空间复杂度: O(n * 13)
        """
        from functools import lru_cache
        
        @lru_cache(maxsize=None)
        def dfs(pos: int, remainder: int) -> int:
            """
            递归函数：计算从位置pos开始，当前余数为remainder的方案数
            """
            # 递归终止条件：处理完所有字符
            if pos == len(s):
                return 1 if remainder == 0 else 0
            
            count = 0
            
            if s[pos] == '?':
                # '?'可以替换为0-9的任意数字
                for d in range(10):
                    new_remainder = (remainder * 10 + d) % self.DIVISOR
                    count = (count + dfs(pos + 1, new_remainder)) % self.MOD
            else:
                # 固定数字
                d = int(s[pos])
                new_remainder = (remainder * 10 + d) % self.DIVISOR
                count = (count + dfs(pos + 1, new_remainder)) % self.MOD
            
            return count
        
        return dfs(0, 0)

def test_digits_parade():
    """
    单元测试函数
    """
    print("=== 测试Digits Parade ===")
    
    dp = DigitsParade()
    
    # 测试用例1: 简单情况
    s1 = "??"
    result1 = dp.count_divisible_by_13(s1)
    result1_dfs = dp.count_divisible_by_13_dfs(s1)
    print(f"输入: {s1}")
    print(f"DP结果: {result1}")
    print(f"DFS结果: {result1_dfs}")
    print(f"结果一致: {result1 == result1_dfs}")
    print("预期: 100种组合中有几个能被13整除")
    print()
    
    # 测试用例2: 固定数字
    s2 = "13"
    result2 = dp.count_divisible_by_13(s2)
    result2_dfs = dp.count_divisible_by_13_dfs(s2)
    print(f"输入: {s2}")
    print(f"DP结果: {result2}")
    print(f"DFS结果: {result2_dfs}")
    print(f"结果一致: {result2 == result2_dfs}")
    print("预期: 13能被13整除，所以为1")
    print()
    
    # 测试用例3: 混合情况
    s3 = "1?2"
    result3 = dp.count_divisible_by_13(s3)
    result3_dfs = dp.count_divisible_by_13_dfs(s3)
    print(f"输入: {s3}")
    print(f"DP结果: {result3}")
    print(f"DFS结果: {result3_dfs}")
    print(f"结果一致: {result3 == result3_dfs}")
    print()

def performance_test():
    """
    性能测试函数
    """
    import time
    print("=== 性能测试 ===")
    
    dp = DigitsParade()
    
    # 生成测试用例
    long_string = "?" * 1000
    
    # 测试DP方法
    start_time_dp = time.time()
    result_dp = dp.count_divisible_by_13(long_string)
    end_time_dp = time.time()
    
    # 测试DFS方法
    start_time_dfs = time.time()
    result_dfs = dp.count_divisible_by_13_dfs(long_string)
    end_time_dfs = time.time()
    
    time_dp = (end_time_dp - start_time_dp) * 1000
    time_dfs = (end_time_dfs - start_time_dfs) * 1000
    
    print(f"字符串长度: {len(long_string)}")
    print(f"DP方法耗时: {time_dp:.2f}ms")
    print(f"DFS方法耗时: {time_dfs:.2f}ms")
    print(f"结果一致: {result_dp == result_dfs}")
    print()

def debug_digits_parade():
    """
    调试函数：验证特定字符串的替换方案
    """
    print("=== 调试Digits Parade ===")
    
    dp = DigitsParade()
    
    test_cases = [
        "0", "1", "13", "26", "39", "52",
        "1?", "?3", "??", "1?3"
    ]
    
    for s in test_cases:
        result = dp.count_divisible_by_13(s)
        print(f"输入: {s}, 方案数: {result}")
        
        # 对于短字符串，可以手动验证
        if len(s) <= 2 and '?' in s:
            print("  具体方案: ", end="")
            count = 0
            max_num = 10 ** len(s)
            for i in range(max_num):
                candidate = str(i).zfill(len(s))
                
                match = True
                for j in range(len(s)):
                    if s[j] != '?' and s[j] != candidate[j]:
                        match = False
                        break
                
                if match and i % 13 == 0:
                    print(candidate, end=" ")
                    count += 1
            print(f"\n  手动计数: {count}")
        print()

def manual_verification():
    """
    手动验证函数：验证算法逻辑
    """
    print("=== 手动验证 ===")
    
    # 验证模运算逻辑
    test_cases = [
        ("0", True),    # 0 % 13 = 0 ✓
        ("13", True),   # 13 % 13 = 0 ✓
        ("26", True),   # 26 % 13 = 0 ✓
        ("1", False),   # 1 % 13 = 1 ✗
        ("27", False),  # 27 % 13 = 1 ✗
    ]
    
    dp = DigitsParade()
    
    for s, expected in test_cases:
        result = dp.count_divisible_by_13(s)
        actual = (result == 1)
        status = "✓" if actual == expected else "✗"
        print(f"{s}: 预期{expected}, 实际{actual} {status}")

"""
工程化考量总结：
1. 模运算：结果对10^9+7取模，避免溢出
2. 状态设计：合理设计状态参数，减少状态数
3. 两种实现：提供DP和DFS两种解法，便于理解
4. 性能优化：使用迭代DP避免递归栈开销
5. 边界处理：正确处理空字符串和全'?'情况

Python语言特性利用：
1. 装饰器：使用@lru_cache实现自动记忆化
2. 类型注解：提高代码可读性
3. 内置函数：使用zfill等简化代码
4. 动态类型：灵活处理各种情况

算法特色：
1. 通配符处理：'?'可以替换为任意数字
2. 模运算约束：结果必须能被13整除
3. 动态规划：从高位到低位逐步计算
4. 记忆化搜索：DFS解法更符合数位DP传统

调试技巧：
1. 小范围验证：手动计算小范围结果进行对拍
2. 中间状态打印：添加调试信息打印中间状态
3. 边界测试：测试0、1、边界值等特殊情况
4. 性能分析：使用time模块进行性能测试
"""

if __name__ == "__main__":
    # 运行功能测试
    test_digits_parade()
    
    # 运行性能测试
    performance_test()
    
    # 调试模式
    debug_digits_parade()
    
    # 手动验证
    manual_verification()
    
    # 边界测试
    print("=== 边界测试 ===")
    dp = DigitsParade()
    print(f"空字符串: {dp.count_divisible_by_13('')}")
    print(f"单个'?': {dp.count_divisible_by_13('?')}")
    print(f"全'?': {dp.count_divisible_by_13('???')}")