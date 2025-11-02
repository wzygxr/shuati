"""
不含连续1的非负整数
题目来源：LeetCode 600. 不含连续1的非负整数
题目链接：https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/

题目描述：
给定一个正整数 n，返回在 [0, n] 范围内不含连续1的非负整数的个数。

解题思路：
1. 数位DP方法：使用数位DP框架，逐位确定二进制数字
2. 状态设计需要记录：
   - 当前处理位置
   - 是否受上界限制
   - 前一位是否为1
3. 关键点：当前位不能与前一位同时为1

时间复杂度分析：
- 状态数：二进制位数 × 2 × 2 ≈ 32 × 4 = 128
- 每个状态处理2种选择
- 总复杂度：O(256) 非常高效

空间复杂度分析：
- 记忆化数组：32 × 2 × 2 ≈ 128个状态

最优解分析：
这是标准的最优解，利用数位DP处理二进制约束条件
"""

class Solution:
    def findIntegers(self, n: int) -> int:
        """
        计算[0, n]中不含连续1的二进制数的个数
        时间复杂度: O(log n)
        空间复杂度: O(log n)
        """
        if n == 0:
            return 1
        if n == 1:
            return 2
        
        # 将n转换为二进制字符串
        binary = bin(n)[2:]
        
        from functools import lru_cache
        
        @lru_cache(maxsize=None)
        def dfs(pos: int, is_limit: bool, last_is_one: bool) -> int:
            """
            数位DP递归函数（二进制版本）
            
            Args:
                pos: 当前处理位置
                is_limit: 是否受到上界限制
                last_is_one: 前一位是否为1
            
            Returns:
                满足条件的数字个数
            """
            # 递归终止条件：处理完所有二进制位
            if pos == len(binary):
                return 1  # 成功构造一个有效数字
            
            ans = 0
            
            # 确定当前位可选数字范围（二进制只有0和1）
            up = int(binary[pos]) if is_limit else 1
            
            # 枚举当前位可选数字
            for d in range(0, up + 1):
                # 检查约束条件：不能有连续的1
                if last_is_one and d == 1:
                    continue  # 连续1，跳过
                
                # 递归处理下一位
                ans += dfs(pos + 1, is_limit and d == up, d == 1)
            
            return ans
        
        # 从第0位开始，初始状态：受限制、前一位不是1
        return dfs(0, True, False)
    
    def findIntegersMath(self, n: int) -> int:
        """
        数学方法（斐波那契数列）- 更高效的解法
        时间复杂度: O(log n)
        空间复杂度: O(1)
        
        解题思路：
        1. 观察发现，不含连续1的二进制数个数满足斐波那契数列
        2. 对于k位二进制数，有效数字个数为fib(k+2)
        3. 利用这个性质可以快速计算
        """
        if n == 0:
            return 1
        if n == 1:
            return 2
        
        # 预处理斐波那契数列
        fib = [1, 2]
        for i in range(2, 32):
            fib.append(fib[i-1] + fib[i-2])
        
        binary = bin(n)[2:]
        ans = 0
        prev_bit = False  # 前一位是否为1
        
        for i in range(len(binary)):
            if binary[i] == '1':
                # 如果当前位为1，可以选择填0，后面位可以任意填
                ans += fib[len(binary) - i - 1]
                
                # 如果前一位也是1，说明出现了连续1，后面的数字都不满足条件
                if prev_bit:
                    return ans
                prev_bit = True
            else:
                prev_bit = False
        
        # 加上n本身（如果n本身满足条件）
        return ans + 1

def test_find_integers():
    """
    单元测试函数
    """
    print("=== 测试不含连续1的非负整数 ===")
    
    sol = Solution()
    
    # 测试用例1: 小数字
    n1 = 5
    result1 = sol.findIntegers(n1)
    result1_math = sol.findIntegersMath(n1)
    print(f"n = {n1}")
    print(f"DP结果: {result1}")
    print(f"数学结果: {result1_math}")
    print(f"结果一致: {result1 == result1_math}")
    print("预期: [0,5]中不含连续1的数字有0,1,2,4,5共5个")
    print()
    
    # 测试用例2: 中等数字
    n2 = 10
    result2 = sol.findIntegers(n2)
    result2_math = sol.findIntegersMath(n2)
    print(f"n = {n2}")
    print(f"DP结果: {result2}")
    print(f"数学结果: {result2_math}")
    print(f"结果一致: {result2 == result2_math}")
    print()
    
    # 测试用例3: 边界情况
    n3 = 1
    result3 = sol.findIntegers(n3)
    result3_math = sol.findIntegersMath(n3)
    print(f"n = {n3}")
    print(f"DP结果: {result3}")
    print(f"数学结果: {result3_math}")
    print(f"结果一致: {result3 == result3_math}")
    print("预期: [0,1]中所有数字都满足，共2个")
    print()

def performance_test():
    """
    性能测试函数
    """
    import time
    print("=== 性能测试 ===")
    
    sol = Solution()
    test_cases = [100, 1000, 10000, 100000, 1000000, 10000000]
    
    for n in test_cases:
        # 测试DP方法
        start_time_dp = time.time()
        result_dp = sol.findIntegers(n)
        end_time_dp = time.time()
        
        # 测试数学方法
        start_time_math = time.time()
        result_math = sol.findIntegersMath(n)
        end_time_math = time.time()
        
        time_dp = (end_time_dp - start_time_dp) * 1e9  # 转换为纳秒
        time_math = (end_time_math - start_time_math) * 1e9  # 转换为纳秒
        
        print(f"n = {n}")
        print(f"DP方法耗时: {time_dp:.0f}ns")
        print(f"数学方法耗时: {time_math:.0f}ns")
        if time_math > 0:
            print(f"加速比: {time_dp/time_math:.2f}倍")
        print(f"结果一致: {result_dp == result_math}")
        print()

def debug_find_integers():
    """
    调试函数：验证特定范围内的结果
    """
    print("=== 调试不含连续1的非负整数 ===")
    
    sol = Solution()
    
    for n in range(0, 21):
        count = 0
        valid_numbers = []
        
        for i in range(0, n + 1):
            # 检查二进制表示是否包含"11"
            binary_str = bin(i)[2:]
            if "11" not in binary_str:
                count += 1
                if len(valid_numbers) < 10:  # 限制输出长度
                    valid_numbers.append(i)
        
        dp_result = sol.findIntegers(n)
        math_result = sol.findIntegersMath(n)
        
        print(f"n = {n}, 有效数字个数: {count}")
        print(f"DP结果: {dp_result}, 数学结果: {math_result}")
        print(f"结果一致: {count == dp_result == math_result}")
        
        if n <= 10:
            print(f"有效数字: {valid_numbers}")
        print()

def manual_verification():
    """
    手动验证函数：验证算法逻辑
    """
    print("=== 手动验证 ===")
    
    # 验证斐波那契性质
    fib = [1, 2]
    for i in range(2, 10):
        fib.append(fib[i-1] + fib[i-2])
    
    print("斐波那契数列（表示k位二进制数的有效数字个数）:")
    for i in range(10):
        print(f"fib({i}) = {fib[i]}")
    
    # 验证小范围结果
    test_cases = [
        (0, 1),  # 只有0
        (1, 2),  # 0,1
        (2, 3),  # 0,1,2
        (3, 4),  # 0,1,2,3? 3的二进制是11，应该排除
        (4, 5),  # 0,1,2,4,5
    ]
    
    sol = Solution()
    
    for n, expected in test_cases:
        actual = sol.findIntegers(n)
        status = "✓" if actual == expected else "✗"
        print(f"n={n}: 预期{expected}, 实际{actual} {status}")

"""
工程化考量总结：
1. 两种解法：提供DP和数学两种解法，便于理解和选择
2. 性能优化：数学方法更高效，DP方法更通用
3. 边界处理：正确处理n=0和n=1的情况
4. 状态设计：合理设计状态参数，减少状态数
5. 测试覆盖：全面的测试用例

Python语言特性利用：
1. 装饰器：使用@lru_cache实现自动记忆化
2. 内置函数：使用bin()进行二进制转换
3. 动态类型：灵活处理各种边界情况
4. 列表推导：简化代码逻辑

算法特色：
1. 二进制处理：针对二进制数的特殊约束
2. 斐波那契性质：发现并利用数学规律
3. 记忆化搜索：DP解法避免重复计算
4. 提前终止：数学解法在发现连续1时提前返回

调试技巧：
1. 小范围验证：手动计算小范围结果进行对拍
2. 中间状态打印：添加调试信息打印中间状态
3. 边界测试：测试0、1、边界值等特殊情况
4. 性能分析：使用time模块进行性能测试
"""

if __name__ == "__main__":
    # 运行功能测试
    test_find_integers()
    
    # 运行性能测试
    performance_test()
    
    # 调试模式
    debug_find_integers()
    
    # 手动验证
    manual_verification()
    
    # 边界测试
    print("=== 边界测试 ===")
    sol = Solution()
    print(f"n=0: {sol.findIntegers(0)}")
    print(f"n=1: {sol.findIntegers(1)}")
    print(f"n=2: {sol.findIntegers(2)}")
    print(f"n=3: {sol.findIntegers(3)}")