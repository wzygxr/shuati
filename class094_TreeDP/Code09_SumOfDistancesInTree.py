# 834. 树中距离之和
# 测试链接 : https://leetcode.cn/problems/sum-of-distances-in-tree/

from typing import List
from collections import deque, defaultdict, deque

class Solution:
    # 提交如下的方法
    # 时间复杂度: O(n) n为节点数量，需要遍历所有节点两次
    # 空间复杂度: O(n) 用于存储图、子树大小和距离数组
    # 是否为最优解: 是，这是计算树中距离之和的标准方法，使用换根DP技术
    def sumOfDistancesInTree(self, n: int, edges: List[List[int]]) -> List[int]:
        # 构建邻接表表示的树
        graph = [[] for _ in range(n)]
        
        for edge in edges:
            graph[edge[0]].append(edge[1])
            graph[edge[1]].append(edge[0])
        
        # dp[i] 表示以节点i为根的子树中，所有节点到节点i的距离之和
        dp = [0] * n
        # sz[i] 表示以节点i为根的子树的节点数量
        sz = [0] * n
        # result[i] 表示所有节点到节点i的距离之和
        result = [0] * n
        
        # 第一次DFS：计算以节点0为根时的dp和sz数组
        self.dfs1(0, -1, graph, dp, sz)
        
        # 第二次DFS：通过换根DP计算所有节点的结果
        self.dfs2(0, -1, graph, dp, sz, result, n)
        
        return result
    
    # 第一次DFS：计算以某个节点为根时，子树内的距离和以及子树大小
    def dfs1(self, u: int, parent: int, graph: List[List[int]], dp: List[int], sz: List[int]) -> None:
        # 初始化当前节点的子树大小为1（节点本身）
        sz[u] = 1
        # 初始化当前节点的子树内距离和为0
        dp[u] = 0
        
        # 遍历当前节点的所有子节点
        for v in graph[u]:
            # 避免回到父节点
            if v == parent:
                continue
            
            # 递归计算子节点的dp和sz
            self.dfs1(v, u, graph, dp, sz)
            
            # 更新当前节点的子树大小
            sz[u] += sz[v]
            # 更新当前节点的子树内距离和
            # 子节点v的子树中所有节点到u的距离比到v的距离多1
            dp[u] += dp[v] + sz[v]
    
    # 第二次DFS：通过换根DP计算所有节点到其他节点的距离之和
    def dfs2(self, u: int, parent: int, graph: List[List[int]], dp: List[int], sz: List[int], result: List[int], n: int) -> None:
        # 当前节点的结果就是dp[u]
        result[u] = dp[u]
        
        # 遍历当前节点的所有子节点
        for v in graph[u]:
            # 避免回到父节点
            if v == parent:
                continue
            
            # 换根：将根从u换到v
            # 保存原始值
            dp_u, dp_v = dp[u], dp[v]
            sz_u, sz_v = sz[u], sz[v]
            
            # 更新dp和sz值以反映根节点的变更
            # 当根从u变为v时：
            # 1. v的子树中的节点到v的距离比到u的距离少1，总共少sz[v]个距离单位
            # 2. 除了v的子树外，其他节点到v的距离比到u的距离多1，总共多(n - sz[v])个距离单位
            dp[u] = dp[u] - dp[v] - sz[v]
            sz[u] = sz[u] - sz[v]
            dp[v] = dp[v] + dp[u] + sz[u]
            sz[v] = sz[v] + sz[u]
            
            # 递归计算以v为根的结果
            self.dfs2(v, u, graph, dp, sz, result, n)
            
            # 恢复原始值，为处理下一个子节点做准备
            dp[u] = dp_u
            dp[v] = dp_v
            sz[u] = sz_u
            sz[v] = sz_v


# 补充题目1: 310. 最小高度树
# 题目链接: https://leetcode.cn/problems/minimum-height-trees/
# 题目描述: 对于一个具有n个节点的无向树，找到所有可能的最小高度树的根节点。
# 时间复杂度: O(n) 进行一次广度优先搜索
# 空间复杂度: O(n) 用于存储图和队列
# 是否为最优解: 是，这是解决最小高度树问题的高效方法
class MinimumHeightTreesSolution:
    def findMinHeightTrees(self, n: int, edges: List[List[int]]) -> List[int]:
        result = []
        
        # 边界情况：只有一个节点
        if n == 1:
            result.append(0)
            return result
        
        # 构建邻接表
        graph = [[] for _ in range(n)]
        # 存储每个节点的度数
        degree = [0] * n
        
        for edge in edges:
            graph[edge[0]].append(edge[1])
            graph[edge[1]].append(edge[0])
            degree[edge[0]] += 1
            degree[edge[1]] += 1
        
        # 将所有叶子节点（度数为1）加入队列
        queue = deque()
        for i in range(n):
            if degree[i] == 1:
                queue.append(i)
        
        # 逐步移除叶子节点，直到剩下1或2个节点
        while n > 2:
            size = len(queue)
            n -= size
            
            for _ in range(size):
                leaf = queue.popleft()
                for neighbor in graph[leaf]:
                    degree[neighbor] -= 1
                    if degree[neighbor] == 1:
                        queue.append(neighbor)
        
        # 剩余的节点就是最小高度树的根
        result.extend(queue)
        return result


# 补充题目2: 1617. 统计子树中城市之间最大距离
# 题目链接: https://leetcode.cn/problems/count-subtrees-with-max-distance-between-cities/
# 题目描述: 给定一个由n个城市组成的树，计算所有可能的子树中，城市之间的最大距离的出现次数。
# 时间复杂度: O(2^n * n) 枚举所有子集，并计算每个子集的直径
# 空间复杂度: O(n) 用于存储图和辅助数组
# 注意：这个实现使用暴力枚举，对于较大的n可能会超时
class CountSubgraphsForEachDiameterSolution:
    def countSubgraphsForEachDiameter(self, n: int, edges: List[List[int]]) -> List[int]:
        # 构建邻接表
        graph = [[] for _ in range(n)]
        
        for edge in edges:
            u = edge[0] - 1  # 转换为0-based索引
            v = edge[1] - 1
            graph[u].append(v)
            graph[v].append(u)
        
        result = [0] * (n - 1)
        
        # 枚举所有非空子集（除了单节点）
        for mask in range(1, 1 << n):
            # 检查子集是否连通
            if not self._is_connected(mask, graph):
                continue
            
            # 计算子树的直径
            diameter = self._get_diameter(mask, graph)
            if diameter > 0:
                result[diameter - 1] += 1
        
        return result
    
    # 检查给定mask表示的子集是否连通
    def _is_connected(self, mask: int, graph: List[List[int]]) -> bool:
        n = len(graph)
        visited = [0] * n
        start = -1
        
        # 找到第一个属于子集的节点
        for i in range(n):
            if (mask & (1 << i)) != 0:
                start = i
                break
        
        if start == -1:
            return False
        
        # DFS检查连通性
        self._dfs_connected(start, mask, graph, visited)
        
        # 验证所有属于子集的节点是否都被访问
        for i in range(n):
            if (mask & (1 << i)) != 0 and visited[i] == 0:
                return False
        
        return True
    
    def _dfs_connected(self, u: int, mask: int, graph: List[List[int]], visited: List[int]) -> None:
        visited[u] = 1
        for v in graph[u]:
            if (mask & (1 << v)) != 0 and visited[v] == 0:
                self._dfs_connected(v, mask, graph, visited)
    
    # 计算给定mask表示的子树的直径
    def _get_diameter(self, mask: int, graph: List[List[int]]) -> int:
        n = len(graph)
        max_diameter = 0
        
        # 找到子集中的所有节点
        nodes = []
        for i in range(n):
            if (mask & (1 << i)) != 0:
                nodes.append(i)
        
        # 枚举所有节点对，计算距离，找出最大值
        for i in range(len(nodes)):
            for j in range(i + 1, len(nodes)):
                distance = self._bfs_distance(nodes[i], nodes[j], mask, graph)
                max_diameter = max(max_diameter, distance)
        
        return max_diameter
    
    def _bfs_distance(self, start: int, end: int, mask: int, graph: List[List[int]]) -> int:
        queue = deque([(start, 0)])
        visited = set([start])
        
        while queue:
            node, dist = queue.popleft()
            
            if node == end:
                return dist
            
            for neighbor in graph[node]:
                if (mask & (1 << neighbor)) != 0 and neighbor not in visited:
                    visited.add(neighbor)
                    queue.append((neighbor, dist + 1))
        
        return -1  # 应该不会到达这里，因为已经确认是连通的


# 补充题目3: 2581. 统计可能的树根数目
# 题目链接: https://leetcode.cn/problems/count-number-of-possible-root-nodes/
# 题目描述: 给定一棵n个节点的无向树和k个查询，每个查询给出一个边，其中指定父节点和子节点的关系。
# 计算有多少个节点可以作为树的根，使得所有查询条件都满足。
# 时间复杂度: O(n+k) 进行两次DFS
# 空间复杂度: O(n+k) 用于存储图和边信息
class RootCountSolution:
    def rootCount(self, edges: List[List[int]], guesses: List[List[int]], k: int) -> int:
        n = len(edges) + 1
        graph = [[] for _ in range(n)]
        
        for edge in edges:
            graph[edge[0]].append(edge[1])
            graph[edge[1]].append(edge[0])
        
        # 将猜测的边存入集合，方便查询
        guess_set = set()
        for guess in guesses:
            u, v = guess[0], guess[1]
            guess_set.add((u, v))
        
        # 第一次DFS：以0为根，计算正确的猜测数
        correct = [0]  # 使用列表作为可变整数
        self._dfs_root_count(0, -1, graph, guess_set, correct)
        
        # 第二次DFS：通过换根计算每个节点作为根时的正确猜测数
        result = 0
        visited = [False] * n
        queue = deque([(0, -1, correct[0])])
        visited[0] = True
        
        while queue:
            u, parent, correct_count = queue.popleft()
            
            # 检查当前节点作为根时是否满足条件
            if correct_count >= k:
                result += 1
            
            # 遍历子节点
            for v in graph[u]:
                if v != parent and not visited[v]:
                    visited[v] = True
                    new_correct = correct_count
                    
                    # 当根从u换到v时，需要调整正确猜测数：
                    # 1. 边u->v在猜测中，现在变为v->u，可能不再正确
                    if (u, v) in guess_set:
                        new_correct -= 1
                    # 2. 边v->u在猜测中，现在变为u->v，可能变为正确
                    if (v, u) in guess_set:
                        new_correct += 1
                    
                    queue.append((v, u, new_correct))
        
        return result
    
    def _dfs_root_count(self, u: int, parent: int, graph: List[List[int]], 
                       guess_set: set, correct: List[int]) -> None:
        for v in graph[u]:
            if v != parent:
                # 检查u->v是否在猜测中
                if (u, v) in guess_set:
                    correct[0] += 1
                self._dfs_root_count(v, u, graph, guess_set, correct)


# 补充题目4: 1245. 树的直径（换根DP版本）
# 题目链接: https://leetcode.cn/problems/tree-diameter/
# 题目描述: 给一棵无向树，找到树中最长路径的长度。
# 时间复杂度: O(n) n为节点数量，需要遍历所有节点两次
# 空间复杂度: O(n) 用于存储图和辅助数组
class TreeDiameterDPSolution:
    def treeDiameterDP(self, edges: List[List[int]]) -> int:
        if not edges or len(edges) == 0:
            return 0
        
        n = len(edges) + 1
        graph = [[] for _ in range(n)]
        
        for edge in edges:
            graph[edge[0]].append(edge[1])
            graph[edge[1]].append(edge[0])
        
        # 第一次DFS找到离任意节点最远的节点
        result1 = self._dfs_tree_diameter(0, -1, graph)
        # 第二次DFS从最远节点出发找到树的直径
        result2 = self._dfs_tree_diameter(result1[0], -1, graph)
        
        return result2[1]
    
    # 返回最远节点和距离的元组 (最远节点, 距离)
    def _dfs_tree_diameter(self, u: int, parent: int, graph: List[List[int]]) -> tuple:
        result = (u, 0)  # 默认最远节点是自己，距离为0
        
        for v in graph[u]:
            if v != parent:  # 避免回到父节点
                current = self._dfs_tree_diameter(v, u, graph)
                distance = current[1] + 1
                
                if distance > result[1]:  # 更新最长距离和最远节点
                    result = (current[0], distance)
        
        return result