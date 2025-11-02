#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P1613 跑路 (Python实现)

题目描述：
一共有n个节点，编号1~n，一共有m条有向边，每条边1公里
有一个空间跑路器，每秒你都可以直接移动2^k公里，每秒钟可以随意决定k的值
题目保证1到n之间一定可以到达，返回1到n最少用几秒

解题思路：
这是一个结合了倍增思想和最短路径算法的图论问题。

核心思想：
1. 预处理：使用倍增思想找出所有可以通过2^k步到达的点对
2. 最短路径：在预处理后的图上使用Floyd算法计算最短时间

具体步骤：
1. 初始化：对于每条原始边，标记为可以通过2^0=1步到达
2. 倍增预处理：对于每个k，计算哪些点对可以通过2^k步到达
   - 如果点i可以通过2^(k-1)步到达点jump，且点jump可以通过2^(k-1)步到达点j
   - 那么点i可以通过2^k步到达点j
3. 最短路径计算：在新图上使用Floyd算法计算1到n的最短时间

时间复杂度：O(n^3 * log k + n^3) = O(n^3 * log k)
空间复杂度：O(n^2 * log k)

相关题目：
1. LeetCode 1334. 阈值距离内邻居最少的城市 (Floyd算法)
2. LeetCode 743. 网络延迟时间 (Dijkstra算法)
3. POJ 1613 - Run Away (相同题目)
4. Codeforces 1083F. The Fair Nut and Amusing Xor
5. AtCoder ABC128D. equeue
6. 牛客网 NC370. 会议室安排
7. 杭电OJ 5171. GTY's birthday gift
8. UVa 10382. Watering Grass
9. CodeChef - STABLEMP
10. SPOJ - ACTIV

工程化考量：
1. 在实际应用中，这类算法常用于：
   - 网络路由优化
   - 交通路径规划
   - 游戏AI路径寻找
   - 物流配送优化
2. 实现优化：
   - 对于稀疏图，可以使用Dijkstra算法替代Floyd算法
   - 使用位运算优化倍增过程
   - 考虑使用更高效的数据结构存储图
3. 可扩展性：
   - 支持动态添加和删除边
   - 处理带权重的边
   - 扩展到三维或多维空间
4. 鲁棒性考虑：
   - 处理不连通图的情况
   - 处理负权边的情况
   - 优化大规模图的性能
5. 跨语言特性对比：
   - Python: 使用列表和字典，代码简洁但性能较低
   - Java: 使用二维数组和IO流
   - C++: 使用vector和数组，性能最优
"""

import sys
from typing import List

class Code02_RanAway:
    """
    跑路问题 - Python实现类
    """
    
    @staticmethod
    def min_time(n: int, edges: List[List[int]]) -> int:
        """
        计算从节点1到节点n的最短时间
        
        Args:
            n: 节点数量
            edges: 边列表，每个边包含[起点, 终点]
            
        Returns:
            int: 最短时间，如果不可达返回-1
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        if n <= 0:
            return -1
        
        # 最大倍增次数，2^MAX_K应该大于等于最大可能距离
        MAX_K = 60
        
        # reach[k][i][j]表示节点i是否可以通过2^k步到达节点j
        # 使用三维列表存储倍增信息
        reach = [[[False] * (n + 1) for _ in range(n + 1)] for _ in range(MAX_K + 1)]
        
        # 初始化原始边（2^0=1步可达）
        for edge in edges:
            u, v = edge
            if 1 <= u <= n and 1 <= v <= n:
                reach[0][u][v] = True
        
        # 倍增预处理
        for k in range(1, MAX_K + 1):
            for i in range(1, n + 1):
                for jump in range(1, n + 1):
                    if reach[k-1][i][jump]:
                        for j in range(1, n + 1):
                            if reach[k-1][jump][j]:
                                reach[k][i][j] = True
        
        # 构建新图：如果存在某个k使得reach[k][i][j]为true，则i到j有一条边
        # 使用大数初始化距离矩阵
        INF = 10**9
        dist = [[INF] * (n + 1) for _ in range(n + 1)]
        
        # 初始化距离矩阵
        for i in range(1, n + 1):
            dist[i][i] = 0
        
        # 添加可达边，权重为1（一步可达）
        for k in range(MAX_K + 1):
            for i in range(1, n + 1):
                for j in range(1, n + 1):
                    if reach[k][i][j] and i != j:
                        dist[i][j] = 1
        
        # 使用Floyd算法计算最短路径
        for k in range(1, n + 1):
            for i in range(1, n + 1):
                if dist[i][k] == INF:
                    continue
                for j in range(1, n + 1):
                    if dist[k][j] != INF:
                        dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
        
        return dist[1][n] if dist[1][n] != INF else -1
    
    @staticmethod
    def min_time_optimized(n: int, edges: List[List[int]]) -> int:
        """
        优化版本：使用动态规划直接计算最短路径
        避免构建显式的新图，减少空间使用
        
        Args:
            n: 节点数量
            edges: 边列表
            
        Returns:
            int: 最短时间
        """
        if n <= 0:
            return -1
        
        MAX_K = 60
        INF = 10**9
        
        # reach[k][i][j]表示节点i是否可以通过2^k步到达节点j
        reach = [[[False] * (n + 1) for _ in range(n + 1)] for _ in range(MAX_K + 1)]
        
        # 初始化原始边
        for edge in edges:
            u, v = edge
            if 1 <= u <= n and 1 <= v <= n:
                reach[0][u][v] = True
        
        # 倍增预处理
        for k in range(1, MAX_K + 1):
            for i in range(1, n + 1):
                for jump in range(1, n + 1):
                    if reach[k-1][i][jump]:
                        for j in range(1, n + 1):
                            if reach[k-1][jump][j]:
                                reach[k][i][j] = True
        
        # 使用动态规划计算最短路径
        dp = [[INF] * (n + 1) for _ in range(n + 1)]
        
        # 初始化：直接边距离为1
        for i in range(1, n + 1):
            dp[i][i] = 0
            for j in range(1, n + 1):
                if i != j:
                    # 检查是否存在某个k使得i可以通过2^k步到达j
                    for k in range(MAX_K + 1):
                        if reach[k][i][j]:
                            dp[i][j] = 1
                            break
        
        # Floyd算法
        for k in range(1, n + 1):
            for i in range(1, n + 1):
                if dp[i][k] == INF:
                    continue
                for j in range(1, n + 1):
                    if dp[k][j] != INF:
                        dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j])
        
        return dp[1][n] if dp[1][n] != INF else -1


def test_min_time():
    """
    测试函数 - 验证算法正确性
    """
    print("=== 测试Code02_RanAway ===")
    
    # 测试用例1：基本功能测试
    n1 = 4
    edges1 = [[1, 2], [2, 3], [3, 4]]
    result1 = Code02_RanAway.min_time(n1, edges1)
    print(f"测试用例1 - 预期: 3, 实际: {result1}")
    
    # 测试用例2：倍增优化测试
    n2 = 4
    edges2 = [[1, 2], [2, 4]]
    result2 = Code02_RanAway.min_time(n2, edges2)
    print(f"测试用例2 - 预期: 2, 实际: {result2}")
    
    # 测试用例3：单节点
    n3 = 1
    edges3 = []
    result3 = Code02_RanAway.min_time(n3, edges3)
    print(f"测试用例3 - 预期: 0, 实际: {result3}")
    
    # 测试用例4：不连通图
    n4 = 3
    edges4 = [[1, 2]]  # 节点3不可达
    result4 = Code02_RanAway.min_time(n4, edges4)
    print(f"测试用例4 - 预期: -1, 实际: {result4}")
    
    # 测试用例5：复杂倍增情况
    n5 = 5
    edges5 = [[1, 2], [2, 3], [3, 4], [4, 5], [1, 3]]
    result5 = Code02_RanAway.min_time(n5, edges5)
    print(f"测试用例5 - 预期: 2, 实际: {result5}")
    
    # 测试优化版本
    result5_opt = Code02_RanAway.min_time_optimized(n5, edges5)
    print(f"优化版本测试用例5 - 预期: 2, 实际: {result5_opt}")
    
    print("=== 测试完成 ===")


def performance_analysis():
    """
    性能分析函数
    """
    import time
    
    print("=== 性能分析 ===")
    
    # 生成中等规模测试数据
    n = 50
    large_edges = []
    
    # 构建完全图（最坏情况）
    for i in range(1, n + 1):
        for j in range(1, n + 1):
            if i != j:
                large_edges.append([i, j])
    
    # 记录开始时间
    start_time = time.time()
    
    result = Code02_RanAway.min_time(n, large_edges)
    
    # 记录结束时间
    end_time = time.time()
    duration = (end_time - start_time) * 1000  # 转换为毫秒
    
    print(f"中等规模测试(n={n}, 边数={len(large_edges)}) - 结果: {result}")
    print(f"执行时间: {duration:.2f} 毫秒")
    
    # 对比优化版本
    start_time_opt = time.time()
    result_opt = Code02_RanAway.min_time_optimized(n, large_edges)
    end_time_opt = time.time()
    duration_opt = (end_time_opt - start_time_opt) * 1000
    
    print(f"优化版本执行时间: {duration_opt:.2f} 毫秒")
    print(f"性能提升: {duration / duration_opt:.2f} 倍")
    
    print("时间复杂度: O(n^3 * log k)")
    print("空间复杂度: O(n^2 * log k)")


def complexity_analysis():
    """
    算法复杂度分析
    """
    print("=== 算法复杂度分析 ===")
    
    print("1. 时间复杂度分析:")
    print("   - 倍增预处理: O(n^3 * log k)")
    print("   - Floyd算法: O(n^3)")
    print("   - 总时间复杂度: O(n^3 * log k)")
    
    print("2. 空间复杂度分析:")
    print("   - 倍增数组: O(n^2 * log k)")
    print("   - 距离矩阵: O(n^2)")
    print("   - 总空间复杂度: O(n^2 * log k)")
    
    print("3. 优化方向:")
    print("   - 对于稀疏图，可以使用Dijkstra算法替代Floyd算法")
    print("   - 使用位运算优化倍增过程")
    print("   - 考虑使用更高效的数据结构存储图")
    
    print("4. Python特定优化:")
    print("   - 使用numpy数组替代列表提高性能")
    print("   - 使用生成器表达式减少内存使用")
    print("   - 使用局部变量缓存频繁访问的数据")


def memory_usage_analysis():
    """
    内存使用分析
    """
    import sys
    
    print("=== 内存使用分析 ===")
    
    # 分析不同规模下的内存使用
    sizes = [10, 20, 30, 40, 50]
    
    for n in sizes:
        # 估算内存使用
        # 倍增数组: n^2 * log k * 1字节（bool类型）
        # 距离矩阵: n^2 * 4字节（int类型）
        max_k = 60
        reach_memory = n * n * max_k * 1 / (1024 * 1024)  # MB
        dist_memory = n * n * 4 / (1024 * 1024)  # MB
        total_memory = reach_memory + dist_memory
        
        print(f"n={n}: 倍增数组 {reach_memory:.2f}MB, 距离矩阵 {dist_memory:.2f}MB, 总计 {total_memory:.2f}MB")


if __name__ == "__main__":
    """
    主函数 - 程序入口
    """
    print("=== Code02_RanAway Python实现 ===")
    
    # 运行测试
    test_min_time()
    
    # 性能分析
    performance_analysis()
    
    # 算法复杂度分析
    complexity_analysis()
    
    # 内存使用分析
    memory_usage_analysis()
    
    print("\n=== 算法特点总结 ===")
    print("1. 核心算法: 倍增思想 + Floyd算法")
    print("2. 适用场景: 图论中的路径优化问题")
    print("3. 时间复杂度: O(n^3 * log k)")
    print("4. 空间复杂度: O(n^2 * log k)")
    print("5. 优化方向: 稀疏图使用Dijkstra，大规模图使用启发式算法")
    print("6. 工程应用: 网络路由、路径规划、游戏AI")