#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
多重背包问题的单调队列优化实现

问题描述：
有一个容量为t的背包，共有n种物品
每种物品i有以下属性：
- 价值v[i]
- 重量w[i]
- 数量c[i]
要求在不超过背包容量的前提下，选择物品使得总价值最大

算法分类：动态规划 - 多重背包问题 - 单调队列优化

单调队列优化原理：
1. 将物品按照重量分组，对每组容量余数相同的状态进行优化
2. 使用单调队列维护窗口内的最优状态
3. 通过数学变形将状态转移方程转化为滑动窗口最大值问题
4. 时间复杂度优化至O(n * t)，无论物品数量多大

适用场景：
- 物品数量和背包容量都很大的情况
- 对于二进制优化仍然不够高效的场景
- 需要达到理论最优时间复杂度的问题

相关题目扩展：
1. LeetCode 474. Ones and Zeroes - https://leetcode.cn/problems/ones-and-zeroes/
2. LeetCode 879. Profitable Schemes - https://leetcode.cn/problems/profitable-schemes/
3. POJ 1742. Coins - http://poj.org/problem?id=1742
4. POJ 1276. Cash Machine - http://poj.org/problem?id=1276
5. HDU 2191. 非常可乐 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
6. AcWing 5. 多重背包问题II - https://www.acwing.com/problem/content/description/5/
7. Codeforces 106C. Buns - https://codeforces.com/problemset/problem/106/C
8. 牛客网 NC17881. 最大价值 - https://ac.nowcoder.com/acm/problem/17881
"""

import sys
from collections import deque
from typing import List

class BoundedKnapsackWithMonotonicQueue:
    """
    多重背包问题的单调队列优化实现类
    
    技术要点：
    1. 使用单调队列优化状态转移过程
    2. 按照同余类分组处理容量
    3. 维护一个双端队列存储最优状态的索引
    4. 时间复杂度为O(n * t)，空间复杂度为O(t)
    """
    
    def __init__(self):
        """
        初始化类实例
        设置默认的数组大小上限
        """
        # 物品数量和背包容量的合理上限
        self.MAXN = 101
        self.MAXW = 40001
    
    def compute1(self, n: int, t: int, v: List[int], w: List[int], c: List[int]) -> int:
        """
        基本的动态规划 + 单调队列优化枚举
        使用二维数组dp[i][j]表示前i种物品，背包容量为j时的最大价值
        
        算法思路：
        1. 对于每种物品，按照容量的余数分组
        2. 对每组使用单调队列维护滑动窗口内的最优状态
        3. 通过同余分组和滑动窗口技术，将时间复杂度优化至O(n * t)
        
        数学原理：
        状态转移方程：dp[i][j] = max{ dp[i-1][j - k*w[i]] + k*v[i] }, 其中0 ≤ k ≤ min(c[i], j/w[i])
        令j = m * w[i] + r，那么方程可以变形为：
        dp[i][m*w[i]+r] = max{ dp[i-1][(m-k)*w[i]+r] + k*v[i] }, 其中0 ≤ k ≤ min(c[i], m)
        令k' = m - k，则：
        dp[i][m*w[i]+r] = max{ dp[i-1][k'*w[i]+r] - k'*v[i] } + m*v[i], 其中max(0, m-c[i]) ≤ k' ≤ m
        这样就转化为了求滑动窗口内最大值的问题
        
        时间复杂度分析：
        O(n * t)，对于每种物品和每个容量位置，最多入队和出队一次
        相比朴素多重背包的O(n * t * c[i])，优化效果显著
        
        空间复杂度分析：
        O(n * t)，使用二维数组存储所有状态
        
        参数：
            n: 物品数量
            t: 背包容量
            v: 物品价值数组（从1开始索引）
            w: 物品重量数组（从1开始索引）
            c: 物品数量数组（从1开始索引）
            
        返回：
            背包能装下的最大价值
        """
        # 边界情况快速处理
        if n == 0 or t == 0:
            return 0
        
        # 初始化二维dp数组
        dp = [[0] * (t + 1) for _ in range(n + 1)]
        
        # 同余分组和单调队列优化
        for i in range(1, n + 1):
            int_vi = v[i]
            int_wi = w[i]
            int_ci = c[i]
            
            # 优化1：跳过数量为0的物品
            if int_ci == 0:
                # 直接继承上一行的状态
                for j in range(t + 1):
                    dp[i][j] = dp[i-1][j]
                continue
            
            # 优化2：跳过价值为0的物品（选了也不增加总价值）
            if int_vi == 0:
                # 直接继承上一行的状态
                for j in range(t + 1):
                    dp[i][j] = dp[i-1][j]
                continue
            
            # 优化3：跳过重量为0的物品（特殊情况）
            if int_wi == 0:
                # 直接继承上一行的状态
                for j in range(t + 1):
                    dp[i][j] = dp[i-1][j]
                continue
            
            # 优化4：跳过重量超过背包容量的物品
            if int_wi > t:
                # 直接继承上一行的状态
                for j in range(t + 1):
                    dp[i][j] = dp[i-1][j]
                continue
            
            # 优化5：调整物品数量上限，避免无意义的计算
            int_ci = min(int_ci, t // int_wi)
            
            # 同余分组处理，对每个余数r进行处理
            for r in range(int_wi):
                # 创建单调队列
                dq = deque()
                
                # 计算当前余数下的有效容量范围
                max_m = (t - r) // int_wi
                for m in range(max_m + 1):
                    # 当前容量j = m * wi + r
                    j = m * int_wi + r
                    
                    # 计算要加入队列的候选值
                    # 这个值是 dp[i-1][j] - m * vi，用于在后续计算中加上 m * vi 得到最终结果
                    candidate = dp[i-1][j] - m * int_vi
                    
                    # 维护单调队列：移除队尾小于等于candidate的元素
                    while dq and candidate >= dp[i-1][dq[-1] * int_wi + r] - dq[-1] * int_vi:
                        dq.pop()
                    
                    # 将当前m加入队列
                    dq.append(m)
                    
                    # 移除队列头部超出窗口范围的元素（窗口大小为ci+1）
                    while dq and m - dq[0] > int_ci:
                        dq.popleft()
                    
                    # 队列头部就是当前窗口内的最优解
                    best_k = dq[0]
                    # 计算当前状态值：最优解 + m * vi
                    dp[i][j] = dp[i-1][best_k * int_wi + r] + (m - best_k) * int_vi
                    
                    # 确保不会比不选当前物品差
                    dp[i][j] = max(dp[i][j], dp[i-1][j])
        
        # 返回最终结果
        return dp[n][t]
    
    def compute2(self, n: int, t: int, v: List[int], w: List[int], c: List[int]) -> int:
        """
        空间压缩的动态规划 + 单调队列优化枚举
        
        算法特点：
        1. 使用一维数组dp[j]表示容量为j时的最大价值
        2. 从右向左遍历容量，确保使用的是上一轮的状态值
        3. 仍然使用同余分组和单调队列优化
        4. 比二维DP版本节省O(n*t)的空间
        
        时间复杂度：O(n * t)
        空间复杂度：O(t)
        
        参数：
            n: 物品数量
            t: 背包容量
            v: 物品价值数组（从1开始索引）
            w: 物品重量数组（从1开始索引）
            c: 物品数量数组（从1开始索引）
            
        返回：
            背包能装下的最大价值
        """
        # 边界情况快速处理
        if n == 0 or t == 0:
            return 0
        
        # 初始化一维dp数组
        dp = [0] * (t + 1)
        
        # 临时数组，用于保存上一轮的值（因为会边遍历边更新）
        pre = [0] * (t + 1)
        
        # 同余分组和单调队列优化
        for i in range(1, n + 1):
            # 复制上一轮的dp值到pre数组
            pre[:] = dp[:]
            
            int_vi = v[i]
            int_wi = w[i]
            int_ci = c[i]
            
            # 优化1：跳过数量为0的物品
            if int_ci == 0:
                continue
            
            # 优化2：跳过价值为0的物品（选了也不增加总价值）
            if int_vi == 0:
                continue
            
            # 优化3：跳过重量为0的物品（特殊情况）
            if int_wi == 0:
                continue
            
            # 优化4：跳过重量超过背包容量的物品
            if int_wi > t:
                continue
            
            # 优化5：调整物品数量上限，避免无意义的计算
            int_ci = min(int_ci, t // int_wi)
            
            # 同余分组处理，对每个余数r进行处理
            for r in range(int_wi):
                # 创建单调队列
                dq = deque()
                
                # 计算当前余数下的有效容量范围
                max_m = (t - r) // int_wi
                for m in range(max_m + 1):
                    # 当前容量j = m * wi + r
                    j = m * int_wi + r
                    
                    # 计算要加入队列的候选值
                    candidate = pre[j] - m * int_vi
                    
                    # 维护单调队列：移除队尾小于等于candidate的元素
                    while dq and candidate >= pre[dq[-1] * int_wi + r] - dq[-1] * int_vi:
                        dq.pop()
                    
                    # 将当前m加入队列
                    dq.append(m)
                    
                    # 移除队列头部超出窗口范围的元素（窗口大小为ci+1）
                    while dq and m - dq[0] > int_ci:
                        dq.popleft()
                    
                    # 队列头部就是当前窗口内的最优解
                    best_k = dq[0]
                    # 计算当前状态值：最优解 + m * vi
                    current_value = pre[best_k * int_wi + r] + (m - best_k) * int_vi
                    
                    # 更新dp数组
                    if current_value > dp[j]:
                        dp[j] = current_value
        
        # 返回最终结果
        return dp[t]
    
    def parse_line(self, line: str) -> List[int]:
        """
        解析一行输入为整数列表
        
        参数：
            line: 输入的一行字符串
            
        返回：
            解析后的整数列表
        """
        return list(map(int, filter(lambda x: x.strip(), line.strip().split())))
    
    def run(self):
        """
        运行程序的主方法
        处理输入、调用计算方法、输出结果
        
        工程化考量：
        1. 使用sys.stdin进行高效的输入处理
        2. 支持多组测试用例的连续读取
        3. 完善边界情况处理，增强代码健壮性
        4. 添加输入校验，处理空行和不完整输入
        """
        # 为了支持大数据量输入，使用sys.stdin读取所有行
        input_lines = [line.strip() for line in sys.stdin if line.strip()]
        ptr = 0
        
        # 预分配足够空间，避免频繁扩容
        v = [0] * (self.MAXN)
        w = [0] * (self.MAXN)
        c = [0] * (self.MAXN)
        
        while ptr < len(input_lines):
            # 解析第一行：物品种类数和背包容量
            first_line = self.parse_line(input_lines[ptr])
            ptr += 1
            
            if len(first_line) < 2:
                continue
            
            n = first_line[0]
            t = first_line[1]
            
            # 边界情况快速处理
            if n == 0 or t == 0:
                print(0)
                continue
            
            # 读取每个物品的信息
            valid_items = 0
            for _ in range(n):
                if ptr >= len(input_lines):
                    break
                
                item_data = self.parse_line(input_lines[ptr])
                ptr += 1
                
                if len(item_data) < 3:
                    continue
                
                value = item_data[0]
                weight = item_data[1]
                cnt = item_data[2]
                
                # 物品过滤优化
                if value <= 0 or weight <= 0 or cnt <= 0 or weight > t:
                    continue
                
                valid_items += 1
                v[valid_items] = value
                w[valid_items] = weight
                c[valid_items] = cnt
            
            # 使用空间压缩版本进行计算并输出结果
            print(self.compute2(valid_items, t, v, w, c))
    
    def monotonic_queue_optimization_principle(self) -> None:
        """
        单调队列优化原理解释
        
        1. 问题分析：
           对于多重背包问题，传统的状态转移方程是：
           dp[i][j] = max{ dp[i-1][j - k*w[i]] + k*v[i] }, 0 ≤ k ≤ min(c[i], j/w[i])
           这个方程的时间复杂度为O(n * t * c[i])，在物品数量很大时会很慢
        
        2. 数学变形：
           令j = m * w[i] + r，那么状态转移方程可以变形为：
           dp[i][m*w[i]+r] = max{ dp[i-1][(m-k)*w[i]+r] + k*v[i] }, 0 ≤ k ≤ min(c[i], m)
           再令k' = m - k，得到：
           dp[i][m*w[i]+r] = max{ dp[i-1][k'*w[i]+r] - k'*v[i] } + m*v[i], max(0, m-c[i]) ≤ k' ≤ m
           这样就将问题转化为在滑动窗口内求最大值的问题
        
        3. 单调队列应用：
           使用一个双端队列维护可能成为最优解的状态索引
           - 当新元素进入队列时，从队尾移除所有小于等于它的值
           - 这样保证队列中的元素从队头到队尾是单调递减的
           - 队头元素始终是当前窗口内的最大值
        
        4. 正确性证明：
           - 如果一个元素的值比后面的元素小，那么它永远不可能成为最优解
           - 滑动窗口确保只考虑合理数量范围内的物品选择
           - 时间复杂度分析：每个元素最多入队和出队一次，因此总时间复杂度为O(n * t)
        """
        pass
    
    def algorithm_optimization_analysis(self) -> None:
        """
        算法优化与工程化考量
        
        1. 单调队列优化深入分析：
           - 普通多重背包：三重循环，时间复杂度O(n * t * c[i])
           - 二进制优化：将物品拆分成log₂(c[i])个组合物品，时间复杂度O(n * t * log c[i])
           - 单调队列优化：时间复杂度O(n * t)，无论物品数量多大，都是线性的
           - 三种方法对比：
             * 朴素方法：实现最简单，效率最低
             * 二进制优化：实现简单，效率适中
             * 单调队列优化：实现复杂，效率最高
        
        2. 代码性能优化技巧：
           - 预处理并跳过无效物品（数量为0、价值为0、重量超过容量）
           - 使用局部变量缓存频繁访问的值
           - 提前调整物品数量上限（min(c[i], t/w[i])）
           - 使用deque作为单调队列的实现，支持O(1)的队头和队尾操作
        
        3. 空间优化策略：
           - 一维数组优化：只保留当前状态和前一状态
           - 对于每组同余类，可以复用同一个单调队列
           - 在Python中，列表切片操作可以高效地复制数组
        
        4. 工程应用中的考量：
           - 实现复杂度：单调队列优化实现复杂，容易出错，调试困难
           - 适用场景：当物品数量很大且背包容量也很大时，单调队列优化的优势明显
           - 代码可维护性：需要添加详细注释说明算法原理和数学推导
           - 替代方案：在实际应用中，如果性能要求不是极高，可以考虑二进制优化
        """
        pass

# 程序入口
if __name__ == "__main__":
    # 创建实例并运行
    solution = BoundedKnapsackWithMonotonicQueue()
    solution.run()