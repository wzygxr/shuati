#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
完全背包问题 - Python实现

问题描述：
有一个容量为t的背包，共有n种物品
每种物品i有以下属性：
- 价值v[i]
- 重量w[i]
每种物品可以选择任意次数（0次或多次）
要求在不超过背包容量的前提下，选择物品使得总价值最大

算法分类：动态规划 - 完全背包问题

算法原理：
1. 状态定义：dp[j]表示背包容量为j时的最大价值
2. 状态转移方程：dp[j] = max(dp[j], dp[j-w[i]] + v[i])
3. 遍历顺序：与01背包不同，完全背包需要正序遍历背包容量，允许物品被多次选择

时间复杂度：O(n*t)
空间复杂度：O(t)

测试链接：https://www.luogu.com.cn/problem/P1616（宝物筛选扩展问题）

实现特点：
1. 使用一维DP数组进行空间优化
2. 采用正序遍历背包容量，允许物品被多次选择
3. 支持多组测试用例处理
4. 包含详细的错误处理和边界检查

相关题目扩展：

【LeetCode（力扣）】
1. LeetCode 322. Coin Change - https://leetcode.cn/problems/coin-change/
   完全背包问题，求组成金额所需的最少硬币数
2. LeetCode 518. Coin Change II - https://leetcode.cn/problems/coin-change-ii/
   完全背包计数问题，求组成金额的方案数
3. LeetCode 377. 组合总和 Ⅳ - https://leetcode.cn/problems/combination-sum-iv/
   顺序相关的组合问题，类似完全背包

【洛谷（Luogu）】
4. 洛谷 P1616. 疯狂的采药 - https://www.luogu.com.cn/problem/P1616
   经典完全背包问题，数据规模较大
5. 洛谷 P1679. 神奇的四次方数 - https://www.luogu.com.cn/problem/P1679
   完全背包在数学问题中的应用

【POJ】
6. POJ 1114. Piggy-Bank - http://poj.org/problem?id=1114
   完全背包问题，求装满背包的最小价值
7. POJ 2063. Investment - http://poj.org/problem?id=2063
   完全背包问题的实际应用

【HDU】
8. HDU 1114. Piggy-Bank - http://acm.hdu.edu.cn/showproblem.php?pid=1114
   完全背包问题，求装满背包的最小价值
9. HDU 2159. FATE - http://acm.hdu.edu.cn/showproblem.php?pid=2159
   二维费用背包问题，同时考虑忍耐度和杀怪数

【Codeforces】
10. Codeforces 148E. Porcelain - https://codeforces.com/problemset/problem/148/E
    分组背包问题，从每组中选择物品

【AtCoder】
11. AtCoder DP Contest E - Knapsack 2 - https://atcoder.jp/contests/dp/tasks/dp_e
    大体积小价值的01背包问题，需要价值维度DP

【SPOJ】
12. SPOJ COINS - https://www.spoj.com/problems/COINS/
    硬币问题，完全背包的变形

【牛客网】
13. NC16552. 买苹果 - https://ac.nowcoder.com/acm/problem/16552
    完全背包问题
14. NC242214 买饮料 - https://ac.nowcoder.com/acm/problem/242214
    多重背包变形

【UVa OJ】
15. UVa 10130. SuperSale - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1071
    01背包问题的简单应用

【AcWing】
16. AcWing 3. 完全背包问题 - https://www.acwing.com/problem/content/3/
    标准完全背包问题
"""

import sys

def unbounded_knapsack(n, t, weights, values):
    """
    完全背包问题的空间优化实现
    
    算法思路：
    1. 初始化dp数组为0
    2. 对每种物品，正序遍历背包容量
    3. 对于每个容量j，考虑选择当前物品或不选择
    4. 状态转移：dp[j] = max(dp[j], dp[j-weights[i]] + values[i])
    
    时间复杂度分析：
    O(n * t)，其中n是物品数量，t是背包容量
    
    空间复杂度分析：
    O(t)，只需要一维数组存储状态
    
    优化点：
    1. 跳过重量为0的物品（特殊情况）
    2. 跳过重量超过背包容量的物品
    3. 使用局部变量缓存频繁访问的值
    4. 添加边界条件检查
    
    Args:
        n: 物品数量
        t: 背包容量
        weights: 物品重量列表
        values: 物品价值列表
        
    Returns:
        int: 背包能装下的最大价值
        
    Raises:
        ValueError: 当输入参数不合法时抛出
    """
    # 参数校验
    if n <= 0 or t < 0:
        return 0
    
    if len(weights) != n or len(values) != n:
        raise ValueError("输入数组长度不匹配")
    
    # 初始化dp数组
    dp = [0] * (t + 1)
    
    # 遍历每种物品
    for i in range(n):
        current_weight = weights[i]
        current_value = values[i]
        
        # 参数校验
        if current_weight < 0 or current_value < 0:
            raise ValueError("物品重量或价值不能为负数")
        
        # 优化：跳过重量为0的物品（特殊情况）
        if current_weight == 0:
            continue
            
        # 优化：跳过重量超过背包容量的物品
        if current_weight > t:
            continue
            
        # 完全背包：正序遍历背包容量
        # 从current_weight开始，因为j < current_weight时无法选择该物品
        for j in range(current_weight, t + 1):
            # 状态转移：选择当前物品或不选择
            candidate = dp[j - current_weight] + current_value
            if candidate > dp[j]:
                dp[j] = candidate
                
    return dp[t]

def main():
    """
    主函数：处理输入、调用算法、输出结果
    
    工程化考量：
    1. 使用高效的输入处理方式
    2. 支持多组测试用例
    3. 完善的错误处理机制
    4. 清晰的输出格式
    """
    # 读取所有输入行
    lines = []
    for line in sys.stdin:
        stripped = line.strip()
        if stripped:  # 跳过空行
            lines.append(stripped)
    
    idx = 0
    while idx < len(lines):
        try:
            # 读取物品数量和背包容量
            parts = lines[idx].split()
            idx += 1
            
            if len(parts) < 2:
                continue
                
            n = int(parts[0])
            t = int(parts[1])
            
            # 边界情况快速处理
            if n == 0 or t == 0:
                print(0)
                continue
                
            # 读取物品重量和价值
            weights = []
            values = []
            
            item_count = 0
            while item_count < n and idx < len(lines):
                parts = lines[idx].split()
                idx += 1
                
                if len(parts) < 2:
                    continue
                    
                weight = int(parts[0])
                value = int(parts[1])
                
                # 过滤无效物品
                if weight < 0 or value < 0:
                    continue
                    
                weights.append(weight)
                values.append(value)
                item_count += 1
                
            # 如果实际读取的物品数量不足n，调整n的值
            n = len(weights)
            
            # 调用算法并输出结果
            result = unbounded_knapsack(n, t, weights, values)
            print(result)
            
        except (ValueError, IndexError) as e:
            # 处理输入格式错误
            print(f"输入格式错误: {e}")
            continue
        except Exception as e:
            # 处理其他异常
            print(f"计算错误: {e}")
            continue

if __name__ == "__main__":
    main()

'''
算法详解与原理解析

1. 完全背包与01背包的区别：
   - 01背包：每种物品只能选一次，需要逆序遍历容量
   - 完全背包：每种物品可以选任意次数，需要正序遍历容量
   - 关键区别：状态转移时使用的dp[j-w[i]]是已经考虑过当前物品的状态

2. 正确性分析：
   - 正序遍历时，dp[j-w[i]]可能已经包含了当前物品的选择
   - 这样自然实现了物品的多次选择
   - 例如：当j=w[i]时选择1个，j=2*w[i]时可以选择2个，依此类推

3. 数学推导：
   - 设f(j)表示容量为j时的最大价值
   - 对于物品i，我们可以选择0,1,2,...,k个，其中k = j/w[i]
   - 状态转移方程：f(j) = max{ f(j-k*w[i]) + k*v[i] }, 0≤k≤j/w[i]
   - 通过正序遍历，我们实际上在计算：f(j) = max(f(j), f(j-w[i]) + v[i])
   - 这等价于考虑了所有可能的选择次数
'''

'''
工程化考量与代码优化

1. 输入优化：
   - 一次性读取所有输入行，便于处理多组测试用例
   - 跳过空行，增强鲁棒性
   - 使用列表预处理输入数据

2. 错误处理：
   - 添加参数校验，确保输入数据的合法性
   - 使用try-except捕获和处理异常
   - 提供清晰的错误信息

3. 性能优化：
   - 使用一维数组替代二维数组，减少内存占用
   - 提前过滤无效物品，减少不必要的计算
   - 使用局部变量缓存频繁访问的值

4. 代码可读性：
   - 使用有意义的变量名
   - 添加详细的文档字符串
   - 模块化设计，将算法逻辑与IO处理分离
'''

'''
调试与测试建议

1. 小数据测试：
   - 使用简单的测试用例验证算法正确性
   - 例如：n=3, t=5, weights=[2,3,4], values=[3,4,5]，预期结果应为最大值

2. 边界测试：
   - 测试n=0或t=0的情况
   - 测试所有物品重量都大于t的情况
   - 测试存在重量为0的物品的情况

3. 性能测试：
   - 对于大规模数据，测试算法的运行时间和内存使用
   - 比较不同优化方法的效果

4. 正确性验证：
   - 与二维DP实现的结果进行对比
   - 确保空间优化版本的结果正确
'''

'''
Python语言特性利用

1. 动态类型：
   - 无需声明变量类型，代码更简洁
   - 但需要注意类型安全，添加适当的校验

2. 列表操作：
   - 使用列表推导式可以简化代码
   - 但要注意大规模数据下的性能问题

3. 异常处理：
   - Python的异常处理机制完善
   - 可以方便地捕获和处理各种错误情况

4. 内置函数：
   - 利用Python丰富的内置函数提高开发效率
   - 但要注意算法竞赛中的性能要求
'''