"""
并查集Python实现合集
包含各种并查集变种的Python实现和测试
"""

from typing import List, Dict, Set, Tuple, Optional
import collections
import heapq
import unittest

class UnionFind:
    """标准并查集实现（路径压缩 + 按秩合并）"""
    
    def __init__(self, n: int):
        self.parent = list(range(n))
        self.rank = [1] * n
        self.count = n  # 连通分量数量
    
    def find(self, x: int) -> int:
        """查找根节点，并进行路径压缩"""
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x: int, y: int) -> bool:
        """合并两个节点所在的集合"""
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return False  # 已经在同一集合
        
        # 按秩合并
        if self.rank[root_x] < self.rank[root_y]:
            root_x, root_y = root_y, root_x
        
        self.parent[root_y] = root_x
        if self.rank[root_x] == self.rank[root_y]:
            self.rank[root_x] += 1
        
        self.count -= 1
        return True
    
    def is_connected(self, x: int, y: int) -> bool:
        """检查两个节点是否连通"""
        return self.find(x) == self.find(y)
    
    def get_count(self) -> int:
        """获取连通分量数量"""
        return self.count

class WeightedUnionFind:
    """带权并查集实现"""
    
    def __init__(self, n: int):
        self.parent = list(range(n))
        self.weight = [0.0] * n  # 存储到父节点的权重
    
    def find(self, x: int) -> Tuple[int, float]:
        """查找根节点，并返回权重（x的值 / 根节点的值）"""
        if self.parent[x] != x:
            root, root_weight = self.find(self.parent[x])
            self.parent[x] = root
            self.weight[x] *= root_weight
            return root, self.weight[x]
        return x, 1.0
    
    def union(self, x: int, y: int, value: float) -> bool:
        """合并集合，表示 x / y = value"""
        root_x, weight_x = self.find(x)
        root_y, weight_y = self.find(y)
        
        if root_x == root_y:
            # 检查是否冲突
            return abs(weight_x / weight_y - value) < 1e-9
        
        # 合并集合
        self.parent[root_x] = root_y
        # 更新权重：x到root_y的权重 = (x到y的权重) * (y到root_y的权重)
        self.weight[root_x] = value * weight_y / weight_x
        return True
    
    def query(self, x: int, y: int) -> Optional[float]:
        """查询 x / y 的值"""
        if not self.is_connected(x, y):
            return None
        root_x, weight_x = self.find(x)
        root_y, weight_y = self.find(y)
        return weight_x / weight_y
    
    def is_connected(self, x: int, y: int) -> bool:
        """检查是否连通"""
        return self.find(x)[0] == self.find(y)[0]

class LeetCode128:
    """LeetCode 128. 最长连续序列"""
    
    def longestConsecutive(self, nums: List[int]) -> int:
        """
        方法1: 使用并查集
        时间复杂度: O(n * α(n))
        空间复杂度: O(n)
        """
        if not nums:
            return 0
        
        # 创建数字到索引的映射
        num_to_index = {num: i for i, num in enumerate(nums)}
        n = len(nums)
        uf = UnionFind(n)
        
        # 合并相邻数字
        for i, num in enumerate(nums):
            if num - 1 in num_to_index:
                uf.union(i, num_to_index[num - 1])
            if num + 1 in num_to_index:
                uf.union(i, num_to_index[num + 1])
        
        # 统计每个连通分量的大小
        size_map = collections.defaultdict(int)
        for i in range(n):
            root = uf.find(i)
            size_map[root] += 1
        
        return max(size_map.values()) if size_map else 0
    
    def longestConsecutiveHashSet(self, nums: List[int]) -> int:
        """
        方法2: 使用哈希表（最优解）
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        num_set = set(nums)
        longest_streak = 0
        
        for num in num_set:
            # 只有当num是序列起点时才查找
            if num - 1 not in num_set:
                current_num = num
                current_streak = 1
                
                while current_num + 1 in num_set:
                    current_num += 1
                    current_streak += 1
                
                longest_streak = max(longest_streak, current_streak)
        
        return longest_streak

class LeetCode547:
    """LeetCode 547. 省份数量（朋友圈）"""
    
    def findCircleNum(self, isConnected: List[List[int]]) -> int:
        """
        使用并查集解决省份数量问题
        时间复杂度: O(n^2 * α(n))
        空间复杂度: O(n)
        """
        n = len(isConnected)
        uf = UnionFind(n)
        
        for i in range(n):
            for j in range(i + 1, n):
                if isConnected[i][j] == 1:
                    uf.union(i, j)
        
        return uf.get_count()
    
    def findCircleNumDFS(self, isConnected: List[List[int]]) -> int:
        """
        使用DFS解决省份数量问题
        时间复杂度: O(n^2)
        空间复杂度: O(n)
        """
        n = len(isConnected)
        visited = [False] * n
        count = 0
        
        def dfs(city: int):
            visited[city] = True
            for neighbor in range(n):
                if isConnected[city][neighbor] == 1 and not visited[neighbor]:
                    dfs(neighbor)
        
        for i in range(n):
            if not visited[i]:
                count += 1
                dfs(i)
        
        return count

class LeetCode684:
    """LeetCode 684. 冗余连接"""
    
    def findRedundantConnection(self, edges: List[List[int]]) -> List[int]:
        """
        使用并查集检测环
        时间复杂度: O(n * α(n))
        空间复杂度: O(n)
        """
        n = len(edges)
        uf = UnionFind(n + 1)  # 节点从1开始编号
        
        for u, v in edges:
            if not uf.union(u, v):
                return [u, v]  # 检测到环
        
        return []  # 理论上不会执行到这里

class LeetCode399:
    """LeetCode 399. 除法求值"""
    
    def calcEquation(self, equations: List[List[str]], values: List[float], queries: List[List[str]]) -> List[float]:
        """
        使用带权并查集解决除法求值问题
        时间复杂度: O((E + Q) * α(V))
        空间复杂度: O(V)
        """
        # 创建变量到索引的映射
        variables = set()
        for eq in equations:
            variables.add(eq[0])
            variables.add(eq[1])
        
        var_to_idx = {var: i for i, var in enumerate(variables)}
        n = len(variables)
        uf = WeightedUnionFind(n)
        
        # 构建并查集
        for (a, b), value in zip(equations, values):
            idx_a, idx_b = var_to_idx[a], var_to_idx[b]
            uf.union(idx_a, idx_b, value)
        
        # 处理查询
        results = []
        for a, b in queries:
            if a not in var_to_idx or b not in var_to_idx:
                results.append(-1.0)
            else:
                idx_a, idx_b = var_to_idx[a], var_to_idx[b]
                result = uf.query(idx_a, idx_b)
                results.append(result if result is not None else -1.0)
        
        return results

class LeetCode803:
    """LeetCode 803. 打砖块"""
    
    def hitBricks(self, grid: List[List[int]], hits: List[List[int]]) -> List[int]:
        """
        使用逆向并查集解决打砖块问题
        时间复杂度: O((M*N + H) * α(M*N))
        空间复杂度: O(M*N)
        """
        m, n = len(grid), len(grid[0])
        
        # 复制网格
        copy = [row[:] for row in grid]
        
        # 先消除所有要打的砖块
        for i, j in hits:
            copy[i][j] = 0
        
        # 初始化并查集（增加一个虚拟顶部节点）
        size = m * n
        uf = UnionFind(size + 1)
        
        # 将第一行砖块连接到虚拟顶部节点
        for j in range(n):
            if copy[0][j] == 1:
                uf.union(j, size)
        
        # 构建初始连通性
        for i in range(1, m):
            for j in range(n):
                if copy[i][j] == 1:
                    # 连接上方砖块
                    if copy[i-1][j] == 1:
                        uf.union(i * n + j, (i-1) * n + j)
                    # 连接左方砖块
                    if j > 0 and copy[i][j-1] == 1:
                        uf.union(i * n + j, i * n + j - 1)
        
        # 逆向处理hits
        directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        result = [0] * len(hits)
        
        for idx in range(len(hits) - 1, -1, -1):
            i, j = hits[idx]
            
            # 如果原始位置没有砖块
            if grid[i][j] == 0:
                result[idx] = 0
                continue
            
            # 记录添加前的稳定砖块数量
            origin_size = self.get_stable_bricks_count(uf, size)
            
            # 恢复砖块
            copy[i][j] = 1
            
            # 如果是第一行，连接到顶部
            if i == 0:
                uf.union(j, size)
            
            # 连接相邻砖块
            for di, dj in directions:
                ni, nj = i + di, j + dj
                if 0 <= ni < m and 0 <= nj < n and copy[ni][nj] == 1:
                    uf.union(i * n + j, ni * n + nj)
            
            # 计算新增的稳定砖块数量
            current_size = self.get_stable_bricks_count(uf, size)
            result[idx] = max(0, current_size - origin_size - 1)
        
        return result
    
    def get_stable_bricks_count(self, uf: UnionFind, top: int) -> int:
        """获取连接到顶部的砖块数量"""
        # 这里简化实现，实际需要维护每个连通分量的大小
        # 为了简化，我们假设UnionFind类有get_size方法
        try:
            return uf.get_component_size(top)
        except:
            # 简化实现：统计所有与顶部连通的节点
            count = 0
            for i in range(len(uf.parent)):
                if uf.find(i) == uf.find(top):
                    count += 1
            return count

class TestUnionFind(unittest.TestCase):
    """并查集测试用例"""
    
    def test_basic_union_find(self):
        uf = UnionFind(5)
        self.assertEqual(uf.get_count(), 5)
        
        uf.union(0, 1)
        self.assertEqual(uf.get_count(), 4)
        self.assertTrue(uf.is_connected(0, 1))
        
        uf.union(2, 3)
        uf.union(1, 2)
        self.assertEqual(uf.get_count(), 2)
        self.assertTrue(uf.is_connected(0, 3))
    
    def test_leetcode_128(self):
        sol = LeetCode128()
        
        # 测试用例1
        nums1 = [100, 4, 200, 1, 3, 2]
        self.assertEqual(sol.longestConsecutive(nums1), 4)
        self.assertEqual(sol.longestConsecutiveHashSet(nums1), 4)
        
        # 测试用例2
        nums2 = [0, 3, 7, 2, 5, 8, 4, 6, 0, 1]
        self.assertEqual(sol.longestConsecutive(nums2), 9)
        self.assertEqual(sol.longestConsecutiveHashSet(nums2), 9)
    
    def test_leetcode_547(self):
        sol = LeetCode547()
        
        # 测试用例1
        isConnected1 = [[1,1,0],[1,1,0],[0,0,1]]
        self.assertEqual(sol.findCircleNum(isConnected1), 2)
        self.assertEqual(sol.findCircleNumDFS(isConnected1), 2)
        
        # 测试用例2
        isConnected2 = [[1,0,0],[0,1,0],[0,0,1]]
        self.assertEqual(sol.findCircleNum(isConnected2), 3)
        self.assertEqual(sol.findCircleNumDFS(isConnected2), 3)
    
    def test_leetcode_684(self):
        sol = LeetCode684()
        
        edges1 = [[1,2],[1,3],[2,3]]
        self.assertEqual(sol.findRedundantConnection(edges1), [2,3])
        
        edges2 = [[1,2],[2,3],[3,4],[1,4],[1,5]]
        self.assertEqual(sol.findRedundantConnection(edges2), [1,4])
    
    def test_weighted_union_find(self):
        uf = WeightedUnionFind(4)
        
        # 测试除法关系: a/b=2, b/c=3 => a/c=6
        uf.union(0, 1, 2.0)  # a/b = 2
        uf.union(1, 2, 3.0)  # b/c = 3
        
        result = uf.query(0, 2)  # a/c = ?
        self.assertAlmostEqual(result, 6.0, places=6)

def performance_analysis():
    """性能分析示例"""
    import time
    import random
    
    print("=== 并查集性能分析 ===")
    
    # 测试不同规模数据的性能
    sizes = [1000, 10000, 100000]
    
    for size in sizes:
        # 生成随机操作序列
        operations = [(random.randint(0, 1), 
                      random.randint(0, size-1), 
                      random.randint(0, size-1)) 
                     for _ in range(size * 2)]
        
        # 测试标准并查集
        start_time = time.time()
        uf = UnionFind(size)
        
        for op_type, x, y in operations:
            if op_type == 0:
                uf.union(x, y)
            else:
                uf.is_connected(x, y)
        
        end_time = time.time()
        print(f"规模 {size}: {end_time - start_time:.4f} 秒")

if __name__ == "__main__":
    # 运行性能分析
    performance_analysis()
    
    # 运行单元测试
    unittest.main(argv=[''], exit=False)
    
    print("\n=== Python并查集实现总结 ===")
    print("1. 标准并查集: 路径压缩 + 按秩合并")
    print("2. 带权并查集: 支持关系维护")
    print("3. 逆向并查集: 处理删除操作")
    print("4. 多解法对比: 提供最优解和替代方案")
    print("5. 完整测试: 包含单元测试和性能分析")