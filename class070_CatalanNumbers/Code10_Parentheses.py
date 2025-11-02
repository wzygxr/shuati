#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
卡特兰数应用 - 括号生成问题
该问题是卡特兰数的经典应用，第n个卡特兰数即为n对括号的有效组合数量

该模块实现了：
1. 括号有效组合数量计算（卡特兰数）
2. 生成所有有效括号组合
3. 多种优化实现方式
4. 完整的边界条件检测

相关题目链接：
- LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
- LintCode 427. 生成括号: https://www.lintcode.com/problem/427/
- 牛客网 NC146. 括号生成: https://www.nowcoder.com/practice/c18107181bf5405fb95993b84d625f39
- LeetCode 856. 括号的分数: https://leetcode.cn/problems/score-of-parentheses/
- LeetCode 32. 最长有效括号: https://leetcode.cn/problems/longest-valid-parentheses/
"""

import sys
import time
from typing import List, Set
import math

def generate_parenthesis_count(n: int) -> int:
    """
    计算n对括号能生成的不同有效括号序列数量
    使用动态规划方法，基于卡特兰数递推公式
    
    时间复杂度：O(n²) - 双重循环结构
    空间复杂度：O(n) - 使用一维数组存储中间结果
    
    参数:
        n (int): 括号对数
    返回:
        int: 有效括号序列的数量
    """
    # 边界条件处理
    if n <= 1:
        return 1
    
    # 检查是否可能发生整数溢出
    # 对于Python来说，整数溢出不是问题，但我们可以警告用户结果可能非常大
    if n > 30:
        print("警告: n值较大，结果可能非常大，但Python会自动处理大整数", file=sys.stderr)
    
    # dp[i] 表示i对括号能生成的有效序列数量
    dp = [0] * (n + 1)
    
    # 初始化基本情况
    dp[0] = 1  # 0对括号有1种方案（空序列）
    dp[1] = 1  # 1对括号有1种方案："()"
    
    # 动态规划填表
    for i in range(2, n + 1):
        for j in range(i):
            # 计算当前j值下的方案数，累加到dp[i]中
            dp[i] += dp[j] * dp[i - 1 - j]
    
    return dp[n]

def generate_parenthesis_count_optimized(n: int) -> int:
    """
    计算n对括号的有效组合数量（使用递推公式优化）
    使用卡特兰数公式：C(2n, n)/(n+1)
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    
    参数:
        n (int): 括号对数
    返回:
        int: 有效组合数量
    """
    # 边界情况处理
    if n <= 1:
        return 1
    
    # 计算卡特兰数：C(2n, n)/(n+1)
    result = 1
    for i in range(n):
        # 逐步计算组合数以避免中间结果过大
        result *= (2 * n - i)
        result //= (i + 1)
    
    # 除以(n+1)得到最终结果
    return result // (n + 1)

def generate_all_parentheses(n: int) -> List[str]:
    """
    生成所有有效的括号序列
    使用递归回溯算法构建所有可能的有效组合
    
    时间复杂度：O(4^n / sqrt(n)) - 卡特兰数的渐近复杂度
    空间复杂度：O(n) - 递归调用栈深度
    
    参数:
        n (int): 括号对数
    返回:
        List[str]: 所有有效的括号序列
    """
    # 边界条件处理
    if n < 0:
        return []
    
    result = []
    
    def generate_helper(current, open_count, close_count):
        # 递归终止条件：已生成2*n个字符
        if len(current) == n * 2:
            result.append(current)
            return
        
        # 如果左括号数小于n，可以添加左括号
        if open_count < n:
            generate_helper(current + "(", open_count + 1, close_count)
        
        # 如果右括号数小于左括号数，可以添加右括号
        if close_count < open_count:
            generate_helper(current + ")", open_count, close_count + 1)
    
    generate_helper("", 0, 0)
    return result

def generate_all_parentheses_dp(n: int) -> List[str]:
    """
    使用动态规划生成所有有效括号组合
    思路：有效括号组合可以表示为 "(A)B"，其中A和B也是有效括号组合
    
    时间复杂度：O(4^n / sqrt(n))
    空间复杂度：O(n * 4^n / sqrt(n)) - 存储所有结果
    
    参数:
        n (int): 括号对数
    返回:
        List[str]: 所有有效括号组合的列表
    """
    # 边界情况处理
    if n < 0:
        return []
    
    # 边界情况处理
    if n == 0:
        return [""]
    
    # dp[i] 存储i对括号的所有有效组合
    dp = [[] for _ in range(n + 1)]
    dp[0] = [""]  # dp[0] = [""]
    
    for i in range(1, n + 1):
        current_list = []
        
        # 枚举根位置，左侧有j对括号，右侧有i-j-1对括号
        for j in range(i):
            for left in dp[j]:
                for right in dp[i - j - 1]:
                    # 构建新的有效组合："(left)right"
                    current_list.append(f"({left}){right}")
        
        dp[i] = current_list
    
    return dp[n]

def is_valid_parentheses(s: str) -> bool:
    """
    验证括号组合是否有效
    可以用于检查生成结果的正确性
    
    参数:
        s (str): 待验证的括号字符串
    返回:
        bool: 如果字符串是有效的括号组合，返回True；否则返回False
    """
    if s is None:
        return False
    
    balance = 0
    for c in s:
        if c == '(':
            balance += 1
        elif c == ')':
            balance -= 1
            # 如果右括号过多，立即返回False
            if balance < 0:
                return False
        # 忽略其他字符（如果有的话）
    # 最终平衡值应为0
    return balance == 0

def print_performance(operation: str, duration: float) -> None:
    """
    打印性能指标
    打印给定操作的耗时
    
    参数:
        operation (str): 操作描述
        duration (float): 耗时（秒）
    """
    if duration < 1e-6:
        print(f"  {operation}: {duration * 1e9:.2f} ns")
    elif duration < 1e-3:
        print(f"  {operation}: {duration * 1e6:.2f} μs")
    elif duration < 1:
        print(f"  {operation}: {duration * 1e3:.2f} ms")
    else:
        print(f"  {operation}: {duration:.2f} s")

def main() -> None:
    """
    主方法 - 测试所有实现并比较性能
    """
    print("===== 括号生成问题（Parentheses Generation）测试 =====")
    
    try:
        # 测试用例1: 括号数量计算
        print("\n1. 括号数量计算:")
        for i in range(0, 6):
            print(f"n={i}:")
            print(f"  动态规划法: {generate_parenthesis_count(i)}")
            print(f"  公式优化法: {generate_parenthesis_count_optimized(i)}")
        
        # 测试用例2: 生成所有括号组合
        print("\n2. 生成所有括号组合:")
        for i in range(1, 4):
            print(f"n={i}:")
            
            # 使用回溯法生成
            start_time = time.time()
            combinations1 = generate_all_parentheses(i)
            end_time = time.time()
            duration = end_time - start_time
            
            print("  回溯法结果:")
            for combination in combinations1:
                print(f"    {combination}")
            print_performance("回溯法耗时", duration)
            
            # 使用动态规划生成
            start_time = time.time()
            combinations2 = generate_all_parentheses_dp(i)
            end_time = time.time()
            duration = end_time - start_time
            
            print_performance("动态规划法耗时", duration)
            
            # 验证两种方法结果一致性
            set1 = set(combinations1)
            set2 = set(combinations2)
            consistent = (set1 == set2)
            print(f"  结果一致性: {'✓ 一致' if consistent else '✗ 不一致'}")
        
        # 测试用例3: 结果验证
        print("\n3. 结果验证:")
        test_result = generate_all_parentheses(4)
        print(f"n=4 生成的组合数量: {len(test_result)}")
        print(f"预期数量（卡特兰数）: {generate_parenthesis_count(4)}")
        
        all_valid = True
        for s in test_result:
            if not is_valid_parentheses(s):
                all_valid = False
                print(f"  无效组合: {s}")
                break
        print(f"  所有组合是否有效: {'✓ 全部有效' if all_valid else '✗ 存在无效组合'}")
        
        # 测试用例4: 性能对比（较大n值）
        print("\n4. 性能对比（较大n值）:")
        large_n = 10
        
        start_time = time.time()
        count1 = generate_parenthesis_count(large_n)
        end_time = time.time()
        duration = end_time - start_time
        print(f"  动态规划计数法结果: {count1}")
        print_performance("动态规划计数法耗时", duration)
        
        start_time = time.time()
        count2 = generate_parenthesis_count_optimized(large_n)
        end_time = time.time()
        duration = end_time - start_time
        print(f"  公式优化计数法结果: {count2}")
        print_performance("公式优化计数法耗时", duration)
        
    except Exception as e:
        print(f"错误: {e}", file=sys.stderr)
        sys.exit(1)
    
    # 最优解分析总结
    print("\n===== 最优解分析 =====")
    print("1. 括号生成问题的最优解取决于问题要求：")
    print("   - 仅需计算数量：使用卡特兰数公式，时间复杂度O(n)，空间复杂度O(1)")
    print("   - 需要生成所有组合：使用回溯算法，时间复杂度O(4^n / sqrt(n))")
    print("2. 回溯法是生成所有组合的最优方法，因为它避免了重复计算和不必要的字符串操作")
    print("3. 在工程应用中，需要注意：")
    print("   - 输入验证（负数处理）")
    print("   - 对于n > 15的情况，生成所有组合会导致性能急剧下降")
    print("   - Python可以自动处理大整数，不需要担心整数溢出")
    print("4. 该问题本质上是卡特兰数的应用，体现了递归思想和动态规划的核心原理")
    print("5. 与其他语言对比:")
    print("   - Python的优势：内置大整数支持，代码简洁易读")
    print("   - Python的劣势：递归深度有限制，对于非常大的n可能需要调整递归深度")

if __name__ == "__main__":
    main()