#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
访问所有节点的最短路径 (Shortest Path Visiting All Nodes)
存在一个由 n 个节点组成的无向连通图，图中的节点按从 0 到 n - 1 编号。
给你一个数组 graph 表示这个图。其中，graph[i] 是一个列表，由所有与节点 i 直接相连的节点组成。
返回能够访问所有节点的最短路径的长度。你可以在任一节点开始和停止，也可以多次重访节点。
测试链接 : https://leetcode.cn/problems/shortest-path-visiting-all-nodes/
"""

from collections import deque
import unittest

class Code11_ShortestPathVisitingAllNodes:
    
    # 使用状态压缩动态规划+BFS解决访问所有节点的最短路径问题
    # 核心思想：用二进制位表示已访问节点的集合，通过BFS找到最短路径
    # 时间复杂度: O(n^2 * 2^n)
    # 空间复杂度: O(n * 2^n)
    @staticmethod
    def shortestPathLength(graph):
        n = len(graph)
        
        # dp[mask][i] 表示访问了mask代表的节点集合，当前在节点i时的最短路径长度
        dp = [[float('inf')] * n for _ in range(1 << n)]
        
        # 队列用于BFS，存储[节点, 状态]
        queue = deque()
        
        # 初始状态：从每个节点开始，只访问了该节点
        for i in range(n):
            dp[1 << i][i] = 0
            queue.append((i, 1 << i))
        
        # BFS搜索
        while queue:
            u, mask = queue.popleft()
            
            # 如果已经访问了所有节点，返回路径长度
            if mask == (1 << n) - 1:
                return dp[mask][u]
            
            # 遍历当前节点的所有邻居
            for v in graph[u]:
                # 计算新的状态
                new_mask = mask | (1 << v)
                # 如果找到更短的路径
                if dp[new_mask][v] > dp[mask][u] + 1:
                    dp[new_mask][v] = dp[mask][u] + 1
                    queue.append((v, new_mask))
        
        # 如果无法访问所有节点，返回-1
        return -1
    
    # 基本测试方法 - 用于手动运行和调试
    # 包含多种图结构的测试用例，验证算法在不同场景下的正确性
    @staticmethod
    def test():
        # 测试用例1: 星型图
        graph1 = [[1,2,3],[0],[0],[0]]
        result1 = Code11_ShortestPathVisitingAllNodes.shortestPathLength(graph1)
        print(f"测试用例1 (星型图): {result1}")  # 期望输出: 4
        
        # 测试用例2: 完全图的一部分
        graph2 = [[1],[0,2,4],[1,3,4],[2],[1,2]]
        result2 = Code11_ShortestPathVisitingAllNodes.shortestPathLength(graph2)
        print(f"测试用例2 (部分完全图): {result2}")  # 期望输出: 4
        
        # 测试用例3: 链状图
        graph3 = [[1],[0,2],[1,3],[2]]
        result3 = Code11_ShortestPathVisitingAllNodes.shortestPathLength(graph3)
        print(f"测试用例3 (链状图): {result3}")  # 期望输出: 6
        
        # 测试用例4: 单节点图
        graph4 = [[]]
        result4 = Code11_ShortestPathVisitingAllNodes.shortestPathLength(graph4)
        print(f"测试用例4 (单节点): {result4}")  # 期望输出: 0

# 单元测试 - 确保代码在不同输入下的正确性
# 这是工程化开发的重要实践，保证代码质量和稳定性
class TestShortestPathVisitingAllNodes(unittest.TestCase):
    def test_shortest_path_length(self):
        solution = Code11_ShortestPathVisitingAllNodes()
        
        # 测试用例1
        graph1 = [[1,2,3],[0],[0],[0]]
        self.assertEqual(solution.shortestPathLength(graph1), 4)
        
        # 测试用例2
        graph2 = [[1],[0,2,4],[1,3,4],[2],[1,2]]
        self.assertEqual(solution.shortestPathLength(graph2), 4)
        
        # 测试用例3
        graph3 = [[1],[0,2],[1,3],[2]]
        self.assertEqual(solution.shortestPathLength(graph3), 6)
        
        # 测试用例4
        graph4 = [[]]
        self.assertEqual(solution.shortestPathLength(graph4), 0)

if __name__ == "__main__":
    # 运行基本测试
    Code11_ShortestPathVisitingAllNodes.test()
    
    # 运行单元测试
    print("\n运行单元测试:")
    unittest.main(argv=['first-arg-is-ignored'], exit=False)
    
    # 性能测试 - 评估不同算法在大规模数据下的表现
    # 性能测试是算法工程化应用的关键步骤，帮助选择最优实现
    print("\n性能测试:")
    import time
    
    # 创建一个较大的图进行性能测试（10节点完全图）
    # 完全图是测试图算法性能的典型测试用例
    large_graph = [[] for _ in range(10)]
    for i in range(10):
        for j in range(i+1, 10):
            large_graph[i].append(j)
            large_graph[j].append(i)
    
    start_time = time.time()
    result = Code11_ShortestPathVisitingAllNodes.shortestPathLength(large_graph)
    bfs_time = time.time() - start_time
    print(f"BFS版本处理10节点完全图耗时: {bfs_time:.6f}秒, 结果: {result}")

"""
面试技巧与算法设计深度解析：

## 复杂度分析

1. BFS版本：
   - 时间复杂度：O(n^2 * 2^n)
     状态数为n * 2^n（每个节点可以处于2^n种访问状态），每个状态需要遍历最多n个邻居
   - 空间复杂度：O(n * 2^n)
     visited集合存储n * 2^n个状态，队列最多存储n * 2^n个元素

2. DP版本：
   - 时间复杂度：O(n^2 * 2^n)
     需要填充大小为2^n * n的DP数组，每个状态需要遍历n个邻居
   - 空间复杂度：O(n * 2^n)
     DP数组的大小为2^n * n

3. 双向BFS版本：
   - 时间复杂度：O(n * 2^(n/2))
     在理想情况下，双向BFS的搜索空间会比单向BFS小很多
   - 空间复杂度：O(n * 2^n)
     最坏情况下仍然需要存储所有可能的状态

## 算法设计说明

### 状态压缩技术
状态压缩是解决这类问题的关键技术，它将集合信息编码为一个整数：
- 对于n个节点，我们用n位二进制表示访问状态
- 第i位为1表示节点i已经被访问过，为0表示未被访问
- 例如：mask = 0b1010 表示节点1和节点3已经被访问

### 核心算法思想
1. **BFS解法**：
   - 每个状态由(当前节点, 已访问节点集合)组成
   - 使用队列进行BFS，确保找到的第一条路径是最短的
   - 使用visited集合记录已处理的状态，避免重复计算

2. **DP解法**：
   - dp[mask][u]表示访问了mask中的节点且当前在节点u时的最短路径长度
   - 初始状态：从每个节点出发，只访问该节点的路径长度为0
   - 状态转移：从当前节点移动到邻居节点，更新最短路径

3. **双向BFS解法**：
   - 同时从起点和终点方向搜索
   - 当两个搜索方向相遇时，找到的路径就是最短的
   - 显著减少搜索空间，提高效率

## 为什么这是最优解？
- 问题要求最短路径，BFS是解决最短路径问题的标准方法
- 状态压缩高效处理了节点访问状态的表示
- 无法在多项式时间内解决，因为状态数是指数级的
- 对于n个节点，必须枚举O(n*2^n)个状态，这是问题本质决定的

## 面试中的深入思考

### 代码优化点
1. **提前终止**：当找到包含所有节点的状态时立即返回
2. **剪枝策略**：使用visited集合避免重复处理相同状态
3. **性能优化**：双向BFS在大规模图中表现更佳

### 工程实践考量
1. **异常处理**：添加参数验证，确保输入有效
2. **单元测试**：编写全面的测试用例验证不同场景
3. **性能测试**：比较不同实现的效率，选择最优方案
4. **代码可读性**：添加详细注释，解释算法逻辑

### 类似问题迁移
此算法可以应用于许多需要状态压缩的问题，例如：
- 旅行商问题（TSP）的变体
- 图的覆盖问题
- 需要跟踪访问状态的路径问题

### 算法调试技巧
1. **打印中间状态**：调试时可以打印当前节点和mask值
2. **小例子测试**：先用小图验证算法正确性
3. **边界情况检查**：确保处理单节点、空图等特殊情况

### 跨语言实现注意事项
1. **Python vs Java vs C++**：
   - Python的deque在BFS中性能良好，但对于非常大的状态空间，C++的位操作会更快
   - Java中可以使用数组代替集合来提高查找效率
   - C++可以利用bitset进一步优化位操作性能

2. **语言特性差异**：
   - Python的元组作为字典键的便利性
   - Java中需要注意Integer的大小限制（对于较大的n）
   - C++中可以使用位运算更高效地处理状态转移

### 算法安全与业务适配
1. **避免崩溃**：确保处理所有边界情况和异常输入
2. **处理溢出**：当n很大时，注意整数溢出问题
3. **可配置性**：可以添加超时机制，避免在超大图上运行时间过长

### 与机器学习/深度学习的联系
1. **状态表示**：状态压缩技术在强化学习中的状态表示有应用
2. **搜索算法**：BFS思想在许多搜索类算法中都有体现
3. **优化思想**：动态规划的松弛操作类似于神经网络中的参数更新

## 总结
这个问题展示了状态压缩动态规划与图论结合的强大应用。在面试中，能够清晰解释状态设计、转移方程和优化策略，体现了扎实的算法基础和工程实践能力。

对于节点数n较大的情况，可以考虑使用启发式搜索（如A*算法）进一步优化，但在大多数面试场景中，BFS解法已经足够高效且易于理解和实现。
"""