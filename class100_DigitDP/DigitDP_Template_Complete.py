#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
数位DP通用模板 (Python版本)

数位DP是一种用于解决与数字的数位相关问题的动态规划技术。
它通常用于统计某个区间内满足特定条件的数字个数，或者计算这些数字的某种属性总和。

核心思想：
1. 将问题转化为计算[0, n]范围内满足条件的数字个数，然后利用前缀和思想计算[a, b]区间的结果
2. 逐位处理数字，使用记忆化搜索避免重复计算
3. 状态设计通常包括：
   - 当前处理到第几位
   - 前一位数字（或前面的状态）
   - 是否受到上界限制
   - 是否有前导零
   - 其他题目相关的状态

时间复杂度：通常为O(log n * 状态数)
空间复杂度：O(状态数)

应用场景：
- 统计特定数字出现次数（如LeetCode 233）
- 统计满足数位条件的数字个数（如不含连续1的数字）
- 统计各位数字不同的数字个数（如LeetCode 1012）
- 统计包含或不包含特定子串的数字个数

作者：algorithm-journey
日期：2024
"""

from functools import lru_cache
import time

class DigitDP_Template_Complete:
    """
    数位DP通用模板类
    提供多种数位DP相关问题的解决方案
    """
    def __init__(self):
        """初始化数位DP模板类"""
        self.s = ""
        self.length = 0
    
    def count_special_numbers(self, n: int) -> int:
        """
        主函数：计算[0, n]范围内各位数字都不重复的数字个数
        这是LeetCode 2376的解决方案
        
        Args:
            n: 上界
            
        Returns:
            满足条件的数字个数
        """
        self.s = str(n)
        self.length = len(self.s)
        # 清除缓存，确保每次调用都重新计算
        self.dfs.cache_clear()
        return self.dfs(0, 0, True, False)
    
    @lru_cache(maxsize=None)
    def dfs(self, pos: int, mask: int, is_limit: bool, is_num: bool) -> int:
        """
        数位DP核心函数 - 统计各位数字不重复的数字个数
        
        Args:
            pos: 当前处理到第几位（从0开始）
            mask: 已使用的数字状态（用位运算表示），每一位表示对应数字是否已使用
            is_limit: 是否受到上界限制
            is_num: 是否已经开始选择数字（处理前导零）
            
        Returns:
            从当前状态到末尾可构造的满足条件的数字个数
        """
        # 递归终止条件：处理完所有数位
        if pos == self.length:
            # 只有在已经开始选择数字的情况下才算一个有效数字
            return int(is_num)
        
        result = 0
        
        # 如果还没有开始选择数字，可以继续跳过（处理前导零）
        if not is_num:
            result += self.dfs(pos + 1, mask, False, False)
        
        # 确定当前位可以填入的数字范围
        up = int(self.s[pos]) if is_limit else 9
        # 确定起始数字：如果还没开始选数字，则从1开始（避免前导零）
        start = 0 if is_num else 1
        
        # 枚举当前位可以填入的数字
        for digit in range(start, up + 1):
            # 约束条件：避免重复数字
            if (mask >> digit) & 1:
                continue
            
            # 递归处理下一位，更新状态
            # 新的limit状态：只有当前受限且填的数字等于上限时，下一位才受限
            # 新的is_num状态：当前已经开始选择数字
            result += self.dfs(
                pos + 1, 
                mask | (1 << digit),  # 标记当前数字已使用
                is_limit and digit == up, 
                True
            )
        
        return result
    
    def count_range(self, low: int, high: int) -> int:
        """
        统计[low, high]范围内满足条件的数字个数
        使用前缀和思想：count(high) - count(low-1)
        
        Args:
            low: 下界
            high: 上界
            
        Returns:
            区间[low, high]内满足条件的数字个数
        """
        if low <= 0:
            return self.count_special_numbers(high)
        return self.count_special_numbers(high) - self.count_special_numbers(low - 1)
    
    def count_digit_one(self, n: int) -> int:
        """
        统计[0, n]范围内数字1出现的次数
        这是LeetCode 233的解决方案
        
        Args:
            n: 上界
            
        Returns:
            数字1出现的总次数
        """
        if n < 0:
            return 0
        s = str(n)
        count = 0
        
        # 逐位分析1的出现次数
        for i in range(len(s)):
            # 高位部分
            high = int(s[:i]) if i > 0 else 0
            # 当前位
            current = int(s[i])
            # 低位部分
            low = int(s[i+1:]) if i < len(s)-1 else 0
            # 当前位的权值
            pos = 10 ** (len(s) - i - 1)
            
            if current == 0:
                # 当前位是0，则1出现的次数由高位决定
                count += high * pos
            elif current == 1:
                # 当前位是1，则1出现的次数由高位和低位共同决定
                count += high * pos + (low + 1)
            else:
                # 当前位大于1，则1出现的次数由高位决定（高位+1）
                count += (high + 1) * pos
        
        return count
    
    def find_integers(self, n: int) -> int:
        """
        统计[0, n]范围内二进制表示中不含连续1的数字个数
        这是LeetCode 600的解决方案
        
        Args:
            n: 上界
            
        Returns:
            满足条件的数字个数
        """
        # 转换为二进制字符串
        binary = bin(n)[2:]
        length = len(binary)
        
        # dp[i][0]表示i位二进制数，最高位为0时的有效数
        # dp[i][1]表示i位二进制数，最高位为1时的有效数
        dp = [[0] * 2 for _ in range(length)]
        
        # 初始状态：1位二进制数
        dp[0][0] = 1  # 数字0
        dp[0][1] = 1  # 数字1
        
        # 填充dp数组 - 自底向上的动态规划
        for i in range(1, length):
            dp[i][0] = dp[i-1][0] + dp[i-1][1]  # 最高位为0，后面可以接0或1
            dp[i][1] = dp[i-1][0]               # 最高位为1，后面只能接0
        
        # 计算结果
        result = dp[length-1][0] + dp[length-1][1]
        
        # 检查是否存在连续1的情况，需要减去不符合条件的数
        for i in range(1, length):
            if binary[i] == '1' and binary[i-1] == '1':
                break  # 出现连续1，不需要调整
            if binary[i] == '0' and binary[i-1] == '1':
                # 调整结果
                suffix = binary[i+1:] if i+1 < length else ''
                result -= int(suffix, 2) + 1 if suffix else 1
                break
        
        return result
    
    def count_no_62(self, n: int) -> int:
        """
        统计[0, n]范围内不含数字4和连续的62的数的个数
        这是HDU 2089的解决方案
        
        Args:
            n: 上界
            
        Returns:
            满足条件的数字个数
        """
        self.s = str(n)
        self.length = len(self.s)
        # 使用新的dfs函数处理这个特定问题
        
        @lru_cache(maxsize=None)
        def dfs_no_62(pos, last, is_limit, has_62):
            """处理不含4和连续62的问题"""
            if has_62:
                return 0
            if pos == self.length:
                return 1
            
            result = 0
            up = int(self.s[pos]) if is_limit else 9
            
            for digit in range(0, up + 1):
                if digit == 4:  # 不能包含4
                    continue
                new_has_62 = has_62 or (last == 6 and digit == 2)
                result += dfs_no_62(
                    pos + 1, 
                    digit, 
                    is_limit and digit == up, 
                    new_has_62
                )
            
            return result
        
        return dfs_no_62(0, -1, True, False)
    
    def measure_performance(self, func, *args, **kwargs):
        """
        测量函数执行时间
        
        Args:
            func: 要测量的函数
            *args: 函数参数
            **kwargs: 关键字参数
            
        Returns:
            tuple: (函数结果, 执行时间(秒))
        """
        start_time = time.time()
        result = func(*args, **kwargs)
        end_time = time.time()
        return result, end_time - start_time

# 详细测试函数
def run_comprehensive_tests():
    """运行全面的测试用例"""
    solution = DigitDP_Template_Complete()
    
    print("=== 数位DP模板综合测试 ===\n")
    
    # 测试用例1：统计各位数字不重复的数字个数
    test_cases = [
        (20, 19),      # [0,20]中有19个各位数字不重复的数
        (100, 91),     # [0,100]中有91个各位数字不重复的数
        (200, 189),    # [0,200]中有189个各位数字不重复的数
        (1, 1),        # 边界情况：只有0和1
        (0, 1)         # 边界情况：只有0
    ]
    
    print("1. 测试各位数字不重复的数字统计：")
    for n, expected in test_cases:
        result, time_taken = solution.measure_performance(solution.count_special_numbers, n)
        status = "通过" if result == expected else f"失败 (期望: {expected})"
        print(f"   n = {n}, 结果 = {result}, {status}, 耗时 = {time_taken:.6f}秒")
    
    # 测试用例2：统计数字1出现的次数
    digit_one_cases = [
        (13, 6),       # [0,13]中1出现6次
        (0, 0),        # 边界情况：0
        (1, 1),        # 边界情况：1
        (100, 21),     # [0,100]中1出现21次
        (1000, 301)    # [0,1000]中1出现301次
    ]
    
    print("\n2. 测试数字1出现次数统计：")
    for n, expected in digit_one_cases:
        result, time_taken = solution.measure_performance(solution.count_digit_one, n)
        status = "通过" if result == expected else f"失败 (期望: {expected})"
        print(f"   n = {n}, 结果 = {result}, {status}, 耗时 = {time_taken:.6f}秒")
    
    # 测试用例3：统计二进制中不含连续1的数字个数
    binary_cases = [
        (5, 5),        # 0,1,10,100,101 -> 5个
        (1, 2),        # 0,1 -> 2个
        (2, 3),        # 0,1,10 -> 3个
        (3, 3),        # 0,1,10 -> 3个（11不满足条件）
        (10, 8)        # 0,1,10,100,101,1000,1001,1010 -> 8个
    ]
    
    print("\n3. 测试二进制不含连续1的数字统计：")
    for n, expected in binary_cases:
        result, time_taken = solution.measure_performance(solution.find_integers, n)
        status = "通过" if result == expected else f"失败 (期望: {expected})"
        print(f"   n = {n}, 结果 = {result}, {status}, 耗时 = {time_taken:.6f}秒")
    
    # 测试用例4：测试区间统计
    range_cases = [
        (10, 20, 9),   # [10,20]中有9个各位数字不重复的数
        (50, 100, 41), # [50,100]中有41个各位数字不重复的数
        (1, 1, 1)      # 边界情况：单个数
    ]
    
    print("\n4. 测试区间统计功能：")
    for low, high, expected in range_cases:
        result, time_taken = solution.measure_performance(solution.count_range, low, high)
        status = "通过" if result == expected else f"失败 (期望: {expected})"
        print(f"   区间 [{low}, {high}], 结果 = {result}, {status}, 耗时 = {time_taken:.6f}秒")
    
    print("\n=== 测试完成 ===")

# 运行测试
if __name__ == "__main__":
    # 简单测试
    solution = DigitDP_Template_Complete()
    n = 100
    print(f"简单测试 - n = {n}, 结果 = {solution.count_special_numbers(n)}")
    
    # 运行综合测试（可选）
    # run_comprehensive_tests()
    
    # 实际应用示例
    print("\n实际应用示例：")
    print(f"数字1在[0, 1000]中出现的次数: {solution.count_digit_one(1000)}")
    print(f"[0, 100]中二进制不含连续1的数字个数: {solution.find_integers(100)}")
    print(f"[10, 200]中各位数字不重复的数字个数: {solution.count_range(10, 200)}")