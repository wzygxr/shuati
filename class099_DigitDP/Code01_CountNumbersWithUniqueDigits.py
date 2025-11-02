"""
统计各位数字都不同的数字个数
题目来源：LeetCode 357. 统计各位数字都不同的数字个数
题目链接：https://leetcode.cn/problems/count-numbers-with-unique-digits/

题目描述：
给你一个整数n，代表十进制数字最多有n位。如果某个数字，每一位都不同，那么这个数字叫做有效数字。
返回有效数字的个数，不统计负数范围。

解题思路：
1. 数学方法（排列组合）：按位数分别计算，利用排列组合公式
2. 数位DP方法：使用数位DP框架，逐位确定数字，用位掩码记录已使用的数字

时间复杂度分析：
- 数学方法：O(n)，每个位数计算一次
- 数位DP方法：O(10 * 2^10 * n)，状态数为位数×状态数×数字选择

空间复杂度分析：
- 数学方法：O(1)，只使用常数空间
- 数位DP方法：O(2^10 * n)，用于记忆化存储

最优解分析：
数学方法是最优解，时间复杂度O(n)，空间复杂度O(1)
数位DP方法更通用但复杂度较高，适合学习数位DP框架
"""

import sys
from typing import List
from functools import lru_cache

class Solution:
    """
    数学方法（排列组合）- 最优解
    时间复杂度: O(n)
    空间复杂度: O(1)
    
    算法步骤：
    1. n=0时，只有数字0，返回1
    2. n=1时，0-9共10个数字
    3. 对于n>=2的情况：
        - 1位数：10个
        - 2位数：9*9个（第一位1-9，第二位0-9除去第一位）
        - 3位数：9*9*8个
        - 以此类推...
    """
    def countNumbersWithUniqueDigits_math(self, n: int) -> int:
        if n == 0:
            return 1
        if n == 1:
            return 10
        
        ans = 10
        available = 9
        current = 9
        
        for i in range(2, n + 1):
            current *= available
            ans += current
            available -= 1
        
        return ans
    
    """
    数位DP方法 - 通用解法
    时间复杂度: O(10 * 2^10 * n)
    空间复杂度: O(2^10 * n)
    
    状态设计：
    - pos: 当前处理到第几位
    - mask: 位掩码，记录已使用的数字（1<<d表示数字d已使用）
    - isLimit: 是否受到上界限制
    - isNum: 是否已开始填数字（处理前导零）
    
    记忆化优化：使用lru_cache避免重复计算相同状态
    Python特色：使用装饰器实现记忆化，代码更简洁
    """
    def countNumbersWithUniqueDigits_dp(self, n: int) -> int:
        if n == 0:
            return 1
        
        # 构造上界字符串（n个9）
        upper = '9' * n
        
        @lru_cache(maxsize=None)
        def dfs(pos: int, mask: int, is_limit: bool, is_num: bool) -> int:
            """
            数位DP递归函数
            
            Args:
                pos: 当前处理的位置
                mask: 已使用数字的位掩码
                is_limit: 是否受到上界限制
                is_num: 是否已开始填数字
            
            Returns:
                满足条件的数字个数
            """
            # 递归终止条件：处理完所有数位
            if pos == len(upper):
                return 1  # 无论是否已填数字，都返回1（表示数字0或已填数字）
            
            ans = 0
            
            # 处理前导零：可以选择跳过当前位
            if not is_num:
                ans += dfs(pos + 1, mask, False, False)
            
            # 确定当前位可选数字范围
            up = int(upper[pos]) if is_limit else 9
            start = 1 if not is_num else 0  # 处理前导零
            
            # 枚举当前位可选的数字
            for d in range(start, up + 1):
                # 检查数字d是否已被使用
                if mask & (1 << d) == 0:
                    # 递归处理下一位，更新状态
                    ans += dfs(pos + 1, mask | (1 << d), 
                              is_limit and d == up, True)
            
            return ans
        
        # 从第0位开始，初始状态：受限制、未开始填数字
        return dfs(0, 0, True, False)

def test_solution():
    """
    单元测试函数
    测试用例设计原则：
    1. 边界测试：n=0, n=1等边界情况
    2. 常规测试：n=2, n=3等正常情况
    3. 对拍测试：两种方法结果应该一致
    """
    sol = Solution()
    
    print("=== 测试统计各位数字都不同的数字个数 ===")
    
    # 测试用例1: n=0
    print("n=0:")
    result_math_0 = sol.countNumbersWithUniqueDigits_math(0)
    result_dp_0 = sol.countNumbersWithUniqueDigits_dp(0)
    print(f"数学方法: {result_math_0}")
    print(f"数位DP方法: {result_dp_0}")
    print("预期结果: 1")
    print(f"结果一致: {result_math_0 == result_dp_0}")
    print()
    
    # 测试用例2: n=1
    print("n=1:")
    result_math_1 = sol.countNumbersWithUniqueDigits_math(1)
    result_dp_1 = sol.countNumbersWithUniqueDigits_dp(1)
    print(f"数学方法: {result_math_1}")
    print(f"数位DP方法: {result_dp_1}")
    print("预期结果: 10")
    print(f"结果一致: {result_math_1 == result_dp_1}")
    print()
    
    # 测试用例3: n=2
    print("n=2:")
    result_math_2 = sol.countNumbersWithUniqueDigits_math(2)
    result_dp_2 = sol.countNumbersWithUniqueDigits_dp(2)
    print(f"数学方法: {result_math_2}")
    print(f"数位DP方法: {result_dp_2}")
    print("预期结果: 91")
    print(f"结果一致: {result_math_2 == result_dp_2}")
    print()
    
    # 测试用例4: n=3
    print("n=3:")
    result_math_3 = sol.countNumbersWithUniqueDigits_math(3)
    result_dp_3 = sol.countNumbersWithUniqueDigits_dp(3)
    print(f"数学方法: {result_math_3}")
    print(f"数位DP方法: {result_dp_3}")
    print("预期结果: 739")
    print(f"两种方法结果一致: {result_math_3 == result_dp_3}")
    print()

def performance_test():
    """
    性能测试函数
    测试两种方法在不同规模下的性能表现
    Python特色：使用time模块进行性能测试
    """
    import time
    sol = Solution()
    
    print("=== 性能测试 ===")
    
    for n in range(1, 9):
        # 测试数学方法
        start_math = time.time()
        result_math = sol.countNumbersWithUniqueDigits_math(n)
        end_math = time.time()
        
        # 测试数位DP方法
        start_dp = time.time()
        result_dp = sol.countNumbersWithUniqueDigits_dp(n)
        end_dp = time.time()
        
        time_math = (end_math - start_math) * 1000  # 转换为毫秒
        time_dp = (end_dp - start_dp) * 1000  # 转换为毫秒
        
        print(f"n={n}:")
        print(f"数学方法时间: {time_math:.3f}毫秒")
        print(f"数位DP方法时间: {time_dp:.3f}毫秒")
        print(f"结果一致: {result_math == result_dp}")
        if time_math > 0:
            print(f"加速比: {time_dp/time_math:.2f}倍")
        print()

def debug_dp(n: int):
    """
    调试函数：打印数位DP的中间状态
    用于理解算法执行过程和调试问题
    """
    print(f"=== 调试数位DP过程 n={n} ===")
    
    if n == 0:
        print("直接返回1")
        return
    
    upper = '9' * n
    call_count = 0
    
    @lru_cache(maxsize=None)
    def dfs_debug(pos, mask, is_limit, is_num, depth=0):
        nonlocal call_count
        call_count += 1
        
        indent = "  " * depth
        print(f"{indent}dfs(pos={pos}, mask={bin(mask)}, is_limit={is_limit}, is_num={is_num})")
        
        if pos == len(upper):
            result = 1 if is_num else 0
            print(f"{indent}-> 终止，返回{result}")
            return result
        
        ans = 0
        
        if not is_num:
            print(f"{indent}跳过当前位（前导零）")
            ans += dfs_debug(pos + 1, mask, False, False, depth + 1)
        
        up = int(upper[pos]) if is_limit else 9
        start = 0 if is_num else 1
        
        for d in range(start, up + 1):
            if mask & (1 << d) == 0:
                print(f"{indent}选择数字{d}")
                ans += dfs_debug(pos + 1, mask | (1 << d), 
                               is_limit and d == up, True, depth + 1)
        
        print(f"{indent}-> 返回{ans}")
        return ans
    
    result = dfs_debug(0, 0, True, False)
    print(f"总调用次数: {call_count}")
    print(f"最终结果: {result}")
    print()

"""
工程化考量：
1. 异常处理：处理n<0的情况，返回0或抛出异常
2. 边界情况：n=0, n=1的特殊处理
3. 性能优化：数学方法是最优选择，数位DP用于学习
4. 可读性：清晰的变量命名、类型注解和文档字符串
5. 测试覆盖：全面的测试用例和调试功能

Python语言特性利用：
- 类型注解：提高代码可读性和IDE支持
- 装饰器：使用@lru_cache实现记忆化，代码简洁
- 动态类型：灵活处理各种边界情况
- 内置函数：使用range、bin等简化代码

跨语言对比：
- Python: 代码简洁，使用装饰器实现记忆化
- Java: 使用多维数组，需要手动管理记忆化
- C++: 使用vector和lambda，注意内存管理
"""

if __name__ == "__main__":
    # 运行功能测试
    test_solution()
    
    # 运行性能测试
    performance_test()
    
    # 调试模式（可选）
    if len(sys.argv) > 1 and sys.argv[1] == "debug":
        debug_dp(2)