#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
多重背包问题的二进制分组优化实现

问题描述：
有一个容量为t的背包，共有n种物品
每种物品i有以下属性：
- 价值v[i]
- 重量w[i]
- 数量c[i]
要求在不超过背包容量的前提下，选择物品使得总价值最大

算法分类：动态规划 - 多重背包问题 - 二进制分组优化

二进制分组优化原理：
1. 将数量为c[i]的物品拆分成若干个"组合物品"
2. 每个组合物品代表k个原物品，其中k是2的幂次
3. 例如：c[i]=5，可以拆成1个、2个、2个的组合，这样任何数量1~5都可以通过选择这些组合得到
4. 这样就将多重背包问题转化为01背包问题，大大减少了状态转移的次数

适用场景：
- 物品数量较大，但又不是无限多的情况
- 需要在时间复杂度和代码复杂度之间取得平衡的场景

相关题目扩展：
1. LeetCode 474. Ones and Zeroes - https://leetcode.cn/problems/ones-and-zeroes/
2. LeetCode 879. Profitable Schemes - https://leetcode.cn/problems/profitable-schemes/
3. POJ 1742. Coins - http://poj.org/problem?id=1742
4. POJ 1276. Cash Machine - http://poj.org/problem?id=1276
5. HDU 2191. 非常可乐 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
6. AcWing 5. 多重背包问题II - https://www.acwing.com/problem/content/description/5/
7. Codeforces 106C. Buns - https://codeforces.com/problemset/problem/106/C
8. 牛客网 NC17881. 最大价值 - https://ac.nowcoder.com/acm/problem/17881

【LeetCode（力扣）】
1. LeetCode 474. Ones and Zeroes - 多维01背包问题
2. LeetCode 879. Profitable Schemes - 二维费用背包问题
3. LeetCode 322. Coin Change - 完全背包问题
4. LeetCode 518. Coin Change II - 完全背包计数问题

【洛谷（Luogu）】
5. 洛谷 P1776 宝物筛选 - 经典多重背包问题
6. 洛谷 P1833 樱花 - 混合背包问题
7. 洛谷 P1679 神奇的四次方数 - 完全背包应用

【POJ】
8. POJ 1742. Coins - 多重背包可行性问题
9. POJ 1276. Cash Machine - 多重背包优化问题
10. POJ 3260. The Fewest Coins - 双向背包问题

【HDU】
11. HDU 2191. 悼念512汶川大地震遇难同胞 - 经典多重背包问题
12. HDU 2159. FATE - 二维费用背包问题
13. HDU 3449. Consumer - 依赖背包问题

【Codeforces】
14. Codeforces 106C. Buns - 复杂的多重背包问题
15. Codeforces 148E. Porcelain - 分组背包问题

【AtCoder】
16. AtCoder ABC032 D. ナップサック問題 - 01背包问题
17. AtCoder DP Contest D - Knapsack 1 - 标准01背包问题

【SPOJ】
18. SPOJ KNAPSACK - 经典01背包问题
19. SPOJ COINS - 硬币问题

【牛客网】
20. NC17881. 最大价值 - 多重背包问题的变形应用
21. NC233233 背包问题IV - 完全背包变形

【AcWing】
22. AcWing 5. 多重背包问题II - 二进制优化的多重背包问题标准题目

【UVa OJ】
23. UVa 10130. SuperSale - 01背包问题的简单应用
"""

import sys
from typing import List, Tuple

class BoundedKnapsackWithBinarySplitting:
    """
    多重背包问题的二进制分组优化实现类
    
    技术要点：
    1. 使用二进制分组将多重背包转化为01背包
    2. 预处理阶段生成组合物品，运行阶段对组合物品应用01背包算法
    3. 时间复杂度为O(t * Σlog c[i])，其中c[i]是第i种物品的数量
    """
    
    def __init__(self):
        """
        初始化类实例
        设置默认的数组大小上限
        """
        # 物品数量和背包容量的合理上限
        self.MAXN = 1001
        self.MAXW = 40001
        
    def compute(self, t: int, v: List[int], w: List[int], m: int) -> int:
        """
        01背包的空间压缩实现
        
        算法思路：
        1. 初始化dp数组，dp[j]表示背包容量为j时的最大价值
        2. 逆序遍历背包容量，避免重复选择同一物品
        3. 状态转移方程：dp[j] = max(dp[j], dp[j - weight] + value)
        
        时间复杂度分析：
        O(m * t)，其中m是衍生商品的总数，t是背包容量
        由于m = Σlog c[i]，所以整体时间复杂度为O(t * Σlog c[i])
        相比朴素多重背包的O(t * Σc[i])，当c[i]较大时优化效果显著
        
        空间复杂度分析：
        O(t)，只需要一维数组存储状态
        
        参数：
            t: 背包容量
            v: 衍生商品价值数组
            w: 衍生商品重量数组
            m: 衍生商品总数
            
        返回：
            背包能装下的最大价值
        """
        # 边界情况快速处理
        if m == 0 or t == 0:
            return 0
        
        # 初始化dp数组，不需要恰好装满背包，所以初始化为0
        dp = [0] * (t + 1)
        
        # 对每个衍生商品应用01背包算法
        for i in range(1, m + 1):
            weight = w[i]
            value = v[i]
            
            # 优化：如果衍生商品的价值为0，跳过（不会增加总价值）
            if value == 0:
                continue
            
            # 优化：如果衍生商品的重量为0且价值不为0，可以无限选择，但这里是01背包所以跳过
            if weight == 0:
                continue
            
            # 优化：如果衍生商品的重量超过背包容量，无法选择，跳过
            if weight > t:
                continue
            
            # 逆序遍历背包容量，确保每个衍生商品只能被选择一次
            for j in range(t, weight - 1, -1):
                # 状态转移：选择该衍生商品或不选
                candidate = dp[j - weight] + value
                if candidate > dp[j]:
                    dp[j] = candidate
        
        # 返回背包容量为t时的最大价值
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
        处理输入、二进制分组生成衍生商品、调用计算方法、输出结果
        
        工程化考量：
        1. 使用sys.stdin进行高效的输入处理
        2. 支持多组测试用例的连续读取
        3. 完善边界情况处理，增强代码健壮性
        4. 添加输入校验，处理空行和不完整输入
        """
        # 为了支持大数据量输入，使用sys.stdin
        input_lines = [line for line in sys.stdin]
        ptr = 0
        
        # 预分配足够空间，避免频繁扩容
        v = [0] * self.MAXN
        w = [0] * self.MAXN
        
        while ptr < len(input_lines):
            # 跳过空行
            while ptr < len(input_lines) and not input_lines[ptr].strip():
                ptr += 1
            
            if ptr >= len(input_lines):
                break
            
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
            
            # 重置衍生商品计数器
            m = 0
            
            # 读取每个物品的信息并进行二进制分组
            for _ in range(n):
                # 跳过可能的空行
                while ptr < len(input_lines) and not input_lines[ptr].strip():
                    ptr += 1
                
                if ptr >= len(input_lines):
                    break
                
                item_data = self.parse_line(input_lines[ptr])
                ptr += 1
                
                if len(item_data) < 3:
                    continue
                
                value = item_data[0]
                weight = item_data[1]
                cnt = item_data[2]
                
                # 优化1：跳过数量为0的物品
                if cnt == 0:
                    continue
                
                # 优化2：跳过价值为0的物品（选了也不增加总价值）
                if value == 0:
                    continue
                
                # 优化3：跳过重量为0的物品（特殊情况）
                if weight == 0:
                    continue
                
                # 优化4：跳过重量超过背包容量的物品
                if weight > t:
                    continue
                
                # 优化5：调整物品数量上限，避免无意义的计算
                cnt = min(cnt, t // weight)
                
                # 二进制分组核心逻辑：
                # 将数量为cnt的物品拆分成多个组合物品，每个组合物品的数量是2的幂次
                # 例如：cnt=5 → 拆分成1个、2个、2个
                # 这样任何1~5的数量都可以通过选择这些组合得到
                k = 1
                while k <= cnt:
                    m += 1
                    v[m] = k * value
                    w[m] = k * weight
                    cnt -= k
                    k <<= 1
                
                # 处理剩余的数量（如果cnt不是2的幂次之和）
                if cnt > 0:
                    m += 1
                    v[m] = cnt * value
                    w[m] = cnt * weight
            
            # 计算并输出结果
            print(self.compute(t, v, w, m))
    
    def binary_knapsack_optimization_principle(self) -> None:
        """
        二进制分组优化原理解释
        
        二进制分组正确性数学证明：
        1. 任意正整数c可以唯一地表示为不同2的幂次之和（二进制表示）
        2. 对于任意k(1<=k<=c)，可以通过选择对应的二进制位组合来表示k个物品的选择
        3. 例如：c=5 → 1(2⁰)+2(2¹)+2(剩余)，这样可以组合出1~5之间的任意数量
        4. 更严谨地说，对于区间[1,c]中的任意整数k，都可以通过选择若干个组合物品来表示
        """
        pass
    
    def algorithm_optimization_analysis(self) -> None:
        """
        算法优化与工程化考量
        
        1. 二进制分组优化深入分析：
           - 普通多重背包：三重循环，时间复杂度O(n * t * c[i])，对于大数量的物品会超时
           - 二进制分组优化：将物品拆分成log₂(c[i])个组合物品，时间复杂度O(n * t * log c[i])
           - 当c[i]很大时（比如1000），log₂(c[i])约为10，优化效果非常明显
           - 二进制分组实际上是对物品数量的压缩表示，利用位运算的特性
        
        2. 代码性能优化技巧：
           - 使用局部变量缓存频繁访问的值（weight、value）
           - 预处理并跳过无效物品（数量为0、价值为0、重量超过容量）
           - 对于重量为0且价值不为0的物品进行特殊处理（可以无限选择）
           - 当c[i] * w[i] > t时，可以将物品视为完全背包处理，进一步优化
        
        3. 与单调队列优化的对比：
           - 二进制优化：时间复杂度O(n * t * log c[i])，实现简单，常数因子小
           - 单调队列优化：时间复杂度O(n * t)，实现较复杂，常数因子稍大
           - 适用场景对比：
             * 当物品数量较多、单个物品数量适中时，二进制优化更适用
             * 当背包容量很大、物品数量适中时，单调队列优化更有优势
             * 在编程比赛中，二进制优化由于实现简单，更常被采用
        
        4. 工程应用中的考量：
           - 数据范围估计：在实际应用中，需要根据数据规模选择合适的优化方法
           - 内存优化：对于超大容量的问题，可以考虑使用稀疏数组或其他压缩技术
           - 并行处理：在多核心环境下，可以考虑对物品分组并行计算
           - 容错处理：添加适当的异常处理和边界检查，提高程序健壮性
        """
        pass

# 程序入口
if __name__ == "__main__":
    # 创建实例并运行
    solution = BoundedKnapsackWithBinarySplitting()
    solution.run()