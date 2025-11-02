"""
LeetCode 600. 不含连续1的非负整数 - Python实现
题目链接：https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/

题目描述：
给定一个正整数 n，统计在 [0, n] 范围的非负整数中，有多少个整数的二进制表示中不存在连续的1。

解题思路：
使用数位动态规划（Digit DP）解决该问题。核心思想是逐位处理二进制数字，确保不出现连续的1。

算法分析：
时间复杂度：O(L) 其中L是数字n的二进制位数
空间复杂度：O(L) 用于存储DP状态

Python实现特点：
1. 使用lru_cache实现自动记忆化
2. 使用装饰器简化代码结构
3. 支持大整数运算
"""

from functools import lru_cache
import time

class Solution:
    def findIntegers(self, n: int) -> int:
        """
        主函数：统计在[0, n]范围内二进制表示中不含连续1的非负整数个数
        
        Args:
            n: 目标数字
            
        Returns:
            int: 满足条件的数字个数
            
        时间复杂度：O(L) 其中L是数字n的二进制位数
        空间复杂度：O(L) 用于存储DP状态
        """
        if n < 0:
            return 0
        if n == 0:
            return 1
            
        # 将数字转换为二进制字符串
        binary_str = bin(n)[2:]
        length = len(binary_str)
        
        @lru_cache(maxsize=None)
        def dfs(pos: int, pre: int, is_limit: bool, is_lead: bool) -> int:
            """
            数位DP核心递归函数
            
            Args:
                pos: 当前处理的位置
                pre: 前一位数字（0或1）
                is_limit: 是否受到上界限制
                is_lead: 是否有前导零
                
            Returns:
                int: 从当前状态开始，满足条件的数字个数
            """
            # 递归终止条件：处理完所有二进制位
            if pos == length:
                return 1
                
            result = 0
            # 确定当前位可以填入的数字范围
            upper = int(binary_str[pos]) if is_limit else 1
            
            for digit in range(0, upper + 1):
                # 约束条件：不能有连续的1
                if pre == 1 and digit == 1:
                    continue
                    
                new_limit = is_limit and (digit == upper)
                new_lead = is_lead and (digit == 0)
                
                result += dfs(pos + 1, digit, new_limit, new_lead)
                
            return result
            
        # 从最高位开始进行数位DP
        return dfs(0, 0, True, True)

class SolutionFibonacci:
    """
    斐波那契数列方法 - 最优解，时间复杂度O(L)，空间复杂度O(1)
    数学发现：不含连续1的二进制数个数满足斐波那契数列规律
    """
    def findIntegers(self, n: int) -> int:
        if n < 0:
            return 0
        if n == 0:
            return 1
            
        # 预处理斐波那契数列，确保足够长度
        fib = [1, 2]
        max_length = 64  # 支持更大的二进制长度
        for i in range(2, max_length):
            fib.append(fib[i-1] + fib[i-2])
        
        binary = bin(n)[2:]
        length = len(binary)
        result = 0
        prev_bit = False  # 记录前一位是否为1
        
        for i in range(length):
            if binary[i] == '1':
                # 加上所有长度为length-i位且最高位为0的有效数
                result += fib[length - i - 1]
                if prev_bit:
                    # 出现连续1，后面的数都不符合条件
                    return result
                prev_bit = True
            else:
                prev_bit = False
                
        # 加上n本身（如果n本身符合条件）
        return result + 1

def test_solution():
    """测试函数，验证算法正确性"""
    solution_dp = Solution()
    solution_fib = SolutionFibonacci()
    
    test_cases = [
        (5, 5),   # 二进制: 101, 有效数: 0,1,10,100,101
        (1, 2),   # 二进制: 1, 有效数: 0,1
        (2, 3),   # 二进制: 10, 有效数: 0,1,10
        (10, 8),  # 二进制: 1010, 有效数需要手动计算
        (0, 1),   # 边界情况
        (100, None),  # 大数测试
    ]
    
    print("数位DP vs 斐波那契方法 对比测试")
    print("=" * 50)
    
    for i, (n, expected) in enumerate(test_cases, 1):
        # 数位DP方法
        start_time = time.time()
        result_dp = solution_dp.findIntegers(n)
        dp_time = time.time() - start_time
        
        # 斐波那契方法
        start_time = time.time()
        result_fib = solution_fib.findIntegers(n)
        fib_time = time.time() - start_time
        
        if expected is not None:
            status_dp = "通过" if result_dp == expected else "失败"
            status_fib = "通过" if result_fib == expected else "失败"
        else:
            status_dp = "N/A"
            status_fib = "N/A"
            
        print(f"测试用例{i}: n = {n} (二进制: {bin(n)[2:]})")
        if expected is not None:
            print(f"期望结果: {expected}")
        print(f"数位DP结果: {result_dp} ({status_dp}), 耗时: {dp_time:.6f}秒")
        print(f"斐波那契方法结果: {result_fib} ({status_fib}), 耗时: {fib_time:.6f}秒")
        
        if dp_time > 0 and fib_time > 0:
            speed_ratio = dp_time / fib_time
            print(f"斐波那契方法比数位DP快 {speed_ratio:.2f} 倍")
        print("-" * 30)

def performance_test():
    """性能测试函数"""
    solution_dp = Solution()
    solution_fib = SolutionFibonacci()
    
    large_numbers = [10**6, 10**9, 10**12, 10**15]
    
    print("性能测试 - 大数情况")
    print("=" * 50)
    
    for n in large_numbers:
        print(f"测试 n = {n} (二进制长度: {len(bin(n))-2})")
        
        # 数位DP方法
        start_time = time.time()
        result_dp = solution_dp.findIntegers(n)
        dp_time = time.time() - start_time
        
        # 斐波那契方法
        start_time = time.time()
        result_fib = solution_fib.findIntegers(n)
        fib_time = time.time() - start_time
        
        print(f"数位DP结果: {result_dp}, 耗时: {dp_time:.4f}秒")
        print(f"斐波那契方法结果: {result_fib}, 耗时: {fib_time:.4f}秒")
        
        if result_dp == result_fib:
            print("✓ 结果一致")
            if fib_time > 0:
                speedup = dp_time / fib_time
                print(f"斐波那契方法快 {speedup:.2f} 倍")
        else:
            print("✗ 结果不一致")
        print("-" * 30)

def manual_verification():
    """手动验证小范围结果"""
    print("小范围手动验证")
    print("=" * 30)
    
    for n in range(16):  # 测试0-15
        # 手动计算
        manual_count = 0
        for i in range(n + 1):
            binary = bin(i)[2:]
            if '11' not in binary:
                manual_count += 1
                
        # 算法计算
        solution = Solution()
        algo_count = solution.findIntegers(n)
        
        status = "✓" if manual_count == algo_count else "✗"
        print(f"n={n:2d} (二进制:{bin(n)[2:]:>4s}): 手动={manual_count:2d}, 算法={algo_count:2d} {status}")

def analyze_algorithm():
    """算法分析"""
    print("算法复杂度分析")
    print("=" * 50)
    
    print("1. 数位DP方法:")
    print("   - 时间复杂度: O(L) 其中L是二进制位数")
    print("   - 空间复杂度: O(L) 用于存储递归栈和记忆化状态")
    print("   - 优点: 通用性强，易于理解和扩展")
    print("   - 缺点: 递归深度较大，常数因子较高")
    
    print("\n2. 斐波那契方法:")
    print("   - 时间复杂度: O(L) 线性扫描二进制位")
    print("   - 空间复杂度: O(1) 只需要常数空间")
    print("   - 优点: 效率高，常数因子小")
    print("   - 缺点: 只适用于特定问题，不易扩展")
    
    print("\n3. 实际应用建议:")
    print("   - 对于此类特定问题，推荐使用斐波那契方法")
    print("   - 数位DP方法更适合学习动态规划思想")
    print("   - 在生产环境中应根据具体需求选择")

if __name__ == "__main__":
    # 运行所有测试
    test_solution()
    print("\n")
    performance_test()
    print("\n")
    manual_verification()
    print("\n")
    analyze_algorithm()
    
    # 结论总结
    print("\n" + "="*60)
    print("总结:")
    print("1. 斐波那契方法是此类问题的最优解")
    print("2. 数位DP方法具有更好的通用性和可扩展性")
    print("3. 实际应用中应根据问题特性和性能要求选择合适算法")
    print("4. 学习数位DP有助于理解动态规划在数字问题中的应用")