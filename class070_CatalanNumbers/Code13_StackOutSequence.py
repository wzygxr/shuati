#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
出栈序列统计
栈是常用的一种数据结构，有n个元素在栈顶端一侧等待进栈，栈顶端另一侧是出栈序列。
你已经知道栈的操作有两种：push和pop，前者是将一个元素进栈，后者是将栈顶元素弹出。
现在要使用这两种操作，由一个操作序列可以得到一系列的输出序列。
请你编程求出对于给定的n，计算并输出由操作数序列1，2，…，n，经过一系列操作可能得到的输出序列总数。
测试链接：https://vijos.org/p/1122
也参考：https://www.luogu.com.cn/problem/P1044
"""

def count_stack_out_sequences(n: int) -> int:
    """
    计算n个元素的出栈序列数量
    这是经典的卡特兰数应用
    
    题目解析：
    1. 有n个元素按顺序进栈，求所有可能的出栈序列数量
    2. 这是卡特兰数的经典应用之一
    3. 对于n个元素，第k个元素是第一个出栈的元素时，将序列分为两部分：
       - 前面有k-1个元素，它们必须在k之前完成出入栈
       - 后面有n-k个元素，它们可以在k出栈之后任意顺序出入栈
    4. 总方案数满足卡特兰数的递推关系
    
    时间复杂度分析：
    1. 使用递推公式：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
    2. 双重循环，外层循环n次，内层循环最多n次
    3. 总时间复杂度：O(n²)
    
    空间复杂度分析：
    1. 使用了一个长度为n+1的数组存储中间结果
    2. 空间复杂度：O(n)
    
    参数:
        n (int): 元素数量
    返回:
        int: 可能的出栈序列总数
    """
    # 边界条件处理
    if n <= 1:
        return 1
    
    # dp[i] 表示i个元素能生成的不同出栈序列数量
    dp = [0] * (n + 1)
    
    # 初始化基本情况
    dp[0] = 1  # 0个元素有1种方案（空序列）
    dp[1] = 1  # 1个元素有1种方案（直接出栈）
    
    # 动态规划填表
    # 对于i个元素，枚举第j+1个元素是第一个出栈的元素
    # 那么前面j个元素必须在它之前完成出入栈，后面i-1-j个元素可以在它出栈之后任意顺序出入栈
    # 总方案数就是前面j个元素的方案数乘以后面i-1-j个元素的方案数
    for i in range(2, n + 1):
        # 对于i个元素，枚举第j+1个元素是第一个出栈的元素（j从0到i-1）
        for j in range(i):
            # dp[j] 是前面j个元素的出栈序列方案数
            # dp[i-1-j] 是后面i-1-j个元素的出栈序列方案数
            # 两者相乘得到当前j值下的方案数，累加到dp[i]中
            dp[i] += dp[j] * dp[i - 1 - j]
    
    return dp[n]

def count_stack_out_sequences_optimized(n: int) -> int:
    """
    使用卡特兰数的另一种递推公式计算
    C(0) = 1
    C(n) = C(n-1) * (4*n-2) / (n+1)
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    
    参数:
        n (int): 元素数量
    返回:
        int: 可能的出栈序列总数
    """
    if n <= 1:
        return 1
    
    catalan = 1
    for i in range(2, n + 1):
        catalan = catalan * (4 * i - 2) // (i + 1)
    return catalan

def main() -> None:
    """
    主函数 - 测试所有实现
    """
    print("出栈序列统计问题测试：")
    for i in range(1, 11):
        result1 = count_stack_out_sequences(i)
        result2 = count_stack_out_sequences_optimized(i)
        print(f"n = {i}, count = {result1}, optimized = {result2}")
    
    # 验证样例
    print("\n样例验证：")
    n = 3
    expected = 5
    actual = count_stack_out_sequences(n)
    print(f"n = {n}, expected = {expected}, actual = {actual}")

if __name__ == "__main__":
    main()