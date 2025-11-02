#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
多重背包问题的基础实现

问题描述：
有n种物品，每种物品有价值v[i]，重量w[i]，以及数量c[i]。背包容量为t。
每种物品最多可以选c[i]个。要求选择若干物品装入背包，使得总价值最大，且总重量不超过背包容量。

算法分类：
- 动态规划
- 背包问题

实现特点：
- 提供二维DP实现（compute1）和一维空间优化DP实现（compute2）
- 包含多重剪枝和优化
- 支持多组测试用例

适用场景：
- 物品数量和背包容量不是特别大的情况
- 需要理解多重背包问题基本原理的场景
- 作为二进制优化和单调队列优化的基础对比

测试链接：
- 牛客网：多重背包问题
- 洛谷：P1776 宝物筛选

核心思想：
- 状态定义：dp[i][j]表示前i种物品，背包容量为j时的最大价值
- 状态转移：对于每种物品，可以选择0到c[i]个中的任意数量
- 一维优化：通过逆序遍历背包容量，确保每个物品只能被选择有限次数

相关题目扩展（多重背包及其变种问题）：

【入门级】
1. LeetCode 416. Partition Equal Subset Sum - 变种问题，检查是否可以分成两个和相等的子集
2. LeetCode 494. Target Sum - 变种问题，寻找目标和的子集个数
3. LeetCode 322. Coin Change - 硬币找零问题，可以视为完全背包的变种

【进阶级】
4. LeetCode 518. Coin Change II - 硬币找零问题II，计算组合方式数
5. LeetCode 1049. Last Stone Weight II - 变种问题，最小石头重量差
6. LeetCode 474. Ones and Zeroes - 二维费用背包问题

【挑战级】
7. LeetCode 879. Profitable Schemes - 二维费用背包问题的变种
8. LeetCode 956. Tallest Billboard - 较复杂的变种问题
9. LeetCode 1220. Count Vowels Permutation - 状态转移类似背包问题
10. LeetCode 1449. Form Largest Integer With Digits That Add up to Target - 变种问题

【经典OJ】
11. 牛客网 NC18377 硬币问题 - 多重背包经典问题
12. 牛客网 NC214100 小A的购物袋 - 多重背包变形
13. 牛客网 NC233233 背包问题IV - 完全背包变形
14. 牛客网 NC242214 买饮料 - 多重背包变形
15. 牛客网 NC249417 金币阵列 - 背包问题的二维变种

【洛谷】
16. 洛谷 P1776 宝物筛选 - 多重背包经典问题
17. 洛谷 P1833 樱花 - 多重背包应用
18. 洛谷 P1679 神奇的四次方数 - 完全背包应用
19. 洛谷 P2077 星球大战 - 多维背包应用

【POJ】
20. POJ 1742. Coins - 多重背包可行性问题
21. POJ 1276. Cash Machine - 多重背包优化问题
22. POJ 3260. The Fewest Coins - 双向背包问题

【HDU】
23. HDU 2191. 悼念512汶川大地震遇难同胞 - 经典多重背包问题
24. HDU 2159. FATE - 二维费用背包问题
25. HDU 3449. Consumer - 依赖背包问题

【Codeforces】
26. Codeforces 106C. Buns - 多重背包问题
27. Codeforces 148E. Porcelain - 分组背包问题
28. Codeforces 455A. Boredom - 打家劫舍类型问题

【AtCoder】
29. AtCoder ABC032 D. ナップサック問題 - 01背包问题
30. AtCoder ABC153 F. Silver Fox vs Monster - 贪心+前缀和优化问题

【SPOJ】
31. SPOJ KNAPSACK - 经典01背包问题
32. SPOJ COINS - 硬币问题

【UVa OJ】
33. UVa 562. Dividing coins - 01背包变形
34. UVa 10130. SuperSale - 01背包问题

【ZOJ】
35. ZOJ 2136. Longest Ordered Subsequence - 最长递增子序列
36. ZOJ 1002. Fire Net - 回溯法与背包思想结合

【AcWing】
37. AcWing 5. 多重背包问题II - 二进制优化的多重背包问题

【剑指Offer】
38. 剑指 Offer 42. 连续子数组的最大和 - 动态规划基础问题
39. 剑指 Offer 10- II. 青蛙跳台阶问题 - 动态规划基础问题
"""

import sys

# 全局变量定义
n = 0  # 物品数量
t = 0  # 背包容量
v = []  # 物品价值列表
w = []  # 物品重量列表
c = []  # 物品数量列表
dp = []  # 一维DP数组


def compute1():
    """
    严格位置依赖的动态规划实现
    使用二维数组存储状态
    
    算法思路：
    1. dp[i][j]表示前i种物品，背包容量为j时的最大价值
    2. 对于每个物品，可以选择不选或者选k个（1<=k<=c[i]且k*w[i]<=j）
    3. 状态转移方程：dp[i][j] = max(dp[i][j], dp[i-1][j-k*w[i]] + k*v[i])
    
    时间复杂度分析：
    O(n * t * k_avg)，其中n是物品数量，t是背包容量，k_avg是每种物品的平均数量
    在最坏情况下（每种物品数量都很大），时间复杂度可能达到O(n * t^2)
    
    空间复杂度分析：
    O(n * t)，使用二维数组存储所有状态
    
    优化思路：
    1. 可以提前计算每种物品的最大可选择数量，避免无效循环
    2. 对于重量为0的物品（如果允许的话），可以特殊处理
    3. 对于价值为0的物品，可以直接跳过，因为选择它们不会增加总价值
    
    Returns:
        int: 背包能装下的最大价值
    """
    # dp[0][....] = 0，表示没有货物的情况下，背包容量不管是多少，最大价值都是0
    dp_table = [[0] * (t + 1) for _ in range(n + 1)]
    
    # 枚举前i种物品
    for i in range(1, n + 1):
        vi = v[i - 1]  # 当前物品价值（注意Python列表索引从0开始）
        wi = w[i - 1]  # 当前物品重量
        ci = c[i - 1]  # 当前物品数量
        
        # 优化：跳过价值为0的物品
        if vi == 0:
            continue
        
        # 优化：跳过重量超过背包容量的物品
        if wi > t:
            continue
        
        # 枚举背包容量j
        for j in range(0, t + 1):
            # 初始状态：不选第i种物品，继承前i-1种物品的最大价值
            dp_table[i][j] = dp_table[i - 1][j]
            
            # 计算当前容量下最多能选多少个该物品
            maxK = min(ci, j // wi)
            
            # 枚举选择第i种物品的数量k（1到maxK个）
            for k in range(1, maxK + 1):
                # 状态转移：选择k个第i种物品，那么剩余容量为j - k*wi，价值增加k*vi
                dp_table[i][j] = max(dp_table[i][j], dp_table[i - 1][j - k * wi] + k * vi)
    
    # 返回所有物品、背包容量为t时的最大价值
    return dp_table[n][t]


def compute2():
    """
    空间优化的动态规划实现
    使用一维数组存储状态，逆序遍历背包容量
    
    算法思路：
    1. dp[j]表示背包容量为j时的最大价值
    2. 逆序遍历背包容量，确保每个物品只能被选择有限次数
    3. 枚举每种物品选择的数量，更新状态
    
    时间复杂度分析：
    O(n * t * k_avg)，与compute1相同
    注意：部分测试用例可能超时，因为没有对枚举进行优化
    
    空间复杂度分析：
    O(t)，只需要一维数组存储状态，大幅降低了空间消耗
    
    核心优化：
    1. 使用一维数组替代二维数组，减少空间占用
    2. 逆序遍历背包容量，确保每种物品只能被选择有限次数
    3. 提前计算maxK，避免重复计算
    4. 添加多重剪枝条件，跳过无效物品
    
    Returns:
        int: 背包能装下的最大价值
    """
    # 枚举每种物品
    for i in range(n):
        vi = v[i]  # 当前物品价值
        wi = w[i]  # 当前物品重量
        ci = c[i]  # 当前物品数量
        
        # 优化1：跳过价值为0的物品
        if vi == 0:
            continue
        
        # 优化2：跳过重量为0且数量无限的物品（理论上可以无限取，但题目通常不会出现）
        if wi == 0 and ci >= float('inf'):
            continue
        
        # 优化3：跳过重量超过背包容量的物品
        if wi > t:
            continue
        
        # 优化4：跳过数量为0的物品
        if ci == 0:
            continue
        
        # 优化5：当物品重量总和超过背包容量时，可以视为完全背包
        if ci * wi >= t:
            # 完全背包处理方式（正序遍历）
            for j in range(wi, t + 1):
                if dp[j - wi] + vi > dp[j]:
                    dp[j] = dp[j - wi] + vi
        else:
            # 逆序枚举背包容量，避免物品被重复选择
            for j in range(t, wi - 1, -1):  # 从wi开始，因为j < wi时无法选择该物品
                # 计算当前容量下最多能选多少个该物品
                maxK = min(ci, j // wi)
                
                # 枚举选择当前物品的数量k（1到maxK个）
                for k in range(1, maxK + 1):
                    prevJ = j - k * wi
                    # 状态转移：选择k个第i种物品，那么剩余容量为prevJ，价值增加k*vi
                    if dp[prevJ] + k * vi > dp[j]:
                        dp[j] = dp[prevJ] + k * vi
    
    # 返回背包容量为t时的最大价值
    return dp[t]


def parse_line(line):
    """
    解析输入行，提取整数列表
    
    Args:
        line: 输入行字符串
    
    Returns:
        list: 整数列表
    """
    return list(map(int, line.strip().split()))


def run():
    """
    处理输入并运行算法
    支持多组测试用例
    """
    global n, t, v, w, c, dp
    
    # 读取所有输入行
    lines = []
    for line in sys.stdin:
        stripped = line.strip()
        if stripped:  # 跳过空行
            lines.append(stripped)
    
    idx = 0
    while idx < len(lines):
        # 读取物品数量和背包容量
        parts = parse_line(lines[idx])
        idx += 1
        
        n = parts[0]
        t = parts[1]
        
        # 边界情况快速处理
        if n == 0 or t == 0:
            print(0)
            continue
        
        # 初始化列表
        v = []
        w = []
        c = []
        dp = [0] * (t + 1)
        
        # 读取每种物品的价值、重量和数量
        item_count = 0
        while item_count < n and idx < len(lines):
            parts = parse_line(lines[idx])
            idx += 1
            
            # 过滤无效物品
            if len(parts) != 3:
                continue
            
            vi, wi, ci = parts
            
            # 物品过滤优化
            if vi <= 0:  # 价值为0或负数，跳过
                continue
            if wi <= 0:  # 重量为0，特殊处理
                if ci > 0:
                    # 如果允许选，理论上可以选无限个，但这里最多选到背包容量
                    dp[t] += vi * ci
                continue
            if wi > t:  # 重量超过背包容量，跳过
                continue
            if ci <= 0:  # 数量为0或负数，跳过
                continue
            
            # 调整数量上限，避免无效计算
            max_possible = t // wi
            if ci > max_possible:
                ci = max_possible
            
            if ci > 0:  # 只有有效物品才加入列表
                v.append(vi)
                w.append(wi)
                c.append(ci)
                item_count += 1
        
        # 更新实际物品数量
        n = len(v)
        
        # 调用空间优化的求解方法并输出结果
        print(compute2())


if __name__ == "__main__":
    run()


'''
算法详解与原理解析

1. 问题建模：
   - 每种物品是一种资源，有价值、重量和数量限制
   - 背包容量是资源约束
   - 目标是在约束条件下最大化总价值

2. 状态定义：
   - 二维DP：dp[i][j]表示前i种物品，背包容量为j时的最大价值
   - 一维DP：dp[j]表示背包容量为j时的最大价值

3. 状态转移方程推导：
   对于第i种物品，我们可以选择0到c[i]个中的任意数量
   dp[i][j] = max{ dp[i-1][j - k*w[i]] + k*v[i] }, 其中0 ≤ k ≤ min(c[i], j/w[i])
   
   一维优化后：
   dp[j] = max{ dp[j - k*w[i]] + k*v[i] }, 其中1 ≤ k ≤ min(c[i], j/w[i])
   （从后向前遍历j，确保每种物品只能选有限次数）

4. 边界条件：
   - dp[0][j] = 0（没有物品可选时，任何容量的最大价值都是0）
   - dp[i][0] = 0（背包容量为0时，无法装任何物品，价值为0）
   - dp[0] = 0（一维DP的初始状态）
'''

'''
代码优化与工程化考量

1. 输入优化：
   - 一次性读取所有输入行，便于处理多组测试用例
   - 跳过空行，增强鲁棒性
   - 使用列表预处理输入数据

2. 算法优化：
   - 提前剪枝：跳过价值为0、重量超过容量或数量为0的物品
   - 计算maxK，避免重复计算j/w[i]和比较c[i]
   - 从wi开始遍历j，减少无效循环
   - 对于重量总和超过背包容量的物品，采用完全背包的处理方式

3. 代码健壮性：
   - 处理各种边界情况：n=0、t=0、物品重量或价值为0等
   - 避免除零错误（虽然题目通常保证w[i]>0）
   - 处理可能的整数溢出问题

4. 性能优化：
   - 使用一维数组替代二维数组，减少内存占用
   - 逆序遍历j，确保状态转移的正确性
   - 优化循环顺序，提高缓存局部性
   - 局部变量缓存，减少索引访问
'''

'''
多重背包问题的高级优化方法

1. 二进制优化：
   - 思路：将数量为c[i]的物品拆分为log(c[i])个物品组
   - 每组代表2^k个该物品，转化为01背包问题
   - 时间复杂度：O(n * t * log c[i])
   - 实现简单，适用范围广
   
2. 单调队列优化：
   - 思路：利用同余分组和单调队列维护最优状态
   - 时间复杂度：O(n * t)
   - 实现较复杂，但效率最高
   - 适合大规模数据
   
3. 完全背包优化：
   - 当c[i] * w[i] >= t时，可以将物品视为完全背包
   - 时间复杂度：O(n * t)
   - 可以结合其他优化方法使用
'''

'''
工程应用场景：
1. 资源分配问题：在有限资源约束下实现收益最大化
2. 投资组合优化：选择多种投资产品，在风险和收益之间取得平衡
3. 生产计划制定：安排不同产品的生产数量，最大化利润
4. 物流配送优化：在载重限制下选择最优配送方案
5. 项目选择问题：在预算和时间约束下选择最优项目组合
6. 广告投放优化：在预算限制下选择最优广告组合以最大化转化率
'''

'''
代码调试与测试建议：
1. 小数据测试：使用简单的测试用例验证算法正确性
2. 边界测试：测试n=0、t=0、物品重量或价值为0等边界情况
3. 性能测试：对于大数据集，可以比较不同优化方法的性能差异
4. 调试技巧：添加中间状态输出，观察dp数组的变化过程
'''

'''
算法学习建议：
1. 先掌握01背包和完全背包的基础实现
2. 理解多重背包问题的本质和状态转移过程
3. 学习二进制优化和单调队列优化的原理
4. 尝试解决各种变形问题，加深理解
5. 对比不同背包问题的异同，建立知识体系
'''

'''
面试要点：
1. 能够清晰解释多重背包问题的状态定义和转移方程
2. 了解常见的优化方法（二进制、单调队列等）
3. 能够分析算法的时间复杂度和空间复杂度
4. 能够处理各种边界情况
5. 能够将背包问题思想应用到实际场景中
'''