# AtCoder ABC221F Diameter Set
# 题目：给定一棵N个顶点的树，顶点编号为1到N。
# 选择两个或更多顶点并将其涂成红色的方法数是多少，
# 使得红色顶点之间的最大距离等于树的直径？
# 答案对998244353取模。
# 来源：AtCoder Beginner Contest 221 Problem F
# 链接：https://atcoder.jp/contests/abc221/tasks/abc221_f

from collections import deque, defaultdict

MOD = 998244353

class AtCoderABC221FDiameterSet:
    def __init__(self):
        self.n = 0  # 节点数
        self.graph = defaultdict(list)  # 邻接表存储树
        self.diameter = 0  # 树的直径
    
    def add_edge(self, u, v):
        """添加无向边"""
        self.graph[u].append(v)
        self.graph[v].append(u)
    
    def power(self, base, exp):
        """
        快速幂运算
        :param base: 底数
        :param exp: 指数
        :return: (base^exp) % MOD
        
        时间复杂度：O(log exp)
        空间复杂度：O(1)
        """
        result = 1
        while exp > 0:
            if exp % 2 == 1:
                result = (result * base) % MOD
            base = (base * base) % MOD
            exp //= 2
        return result
    
    def bfs(self, start):
        """
        BFS求从起点开始的最远节点
        :param start: 起点
        :return: (最远节点, 距离)
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        visited = [False] * (self.n + 1)
        queue = deque([(start, 0)])  # (节点, 距离)
        visited[start] = True
        
        farthest_node = start
        max_distance = 0
        
        while queue:
            current, distance = queue.popleft()
            farthest_node = current
            max_distance = distance
            
            # 遍历当前节点的所有邻居
            for neighbor in self.graph[current]:
                if not visited[neighbor]:
                    visited[neighbor] = True
                    queue.append((neighbor, distance + 1))
        
        return farthest_node, max_distance
    
    def find_diameter(self):
        """
        使用两次BFS法求树的直径
        :return: 树的直径
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        # 第一次BFS，从节点1开始找到最远节点
        first_node, _ = self.bfs(1)
        
        # 第二次BFS，从第一次找到的最远节点开始找到另一个最远节点
        _, diameter = self.bfs(first_node)
        
        return diameter
    
    def dfs_subtree_size(self, u, parent, subtree_size):
        """
        DFS计算子树大小
        :param u: 当前节点
        :param parent: 父节点
        :param subtree_size: 子树大小数组
        :return: 子树大小
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        subtree_size[u] = 1
        for v in self.graph[u]:
            if v != parent:
                subtree_size[u] += self.dfs_subtree_size(v, u, subtree_size)
        return subtree_size[u]
    
    def dfs_depth(self, u, parent, d, depth):
        """
        DFS计算深度
        :param u: 当前节点
        :param parent: 父节点
        :param d: 当前深度
        :param depth: 深度数组
        :return: None
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        depth[u] = d
        for v in self.graph[u]:
            if v != parent:
                self.dfs_depth(v, u, d + 1, depth)
    
    def solve(self):
        """
        计算满足条件的方案数
        :return: 满足条件的方案数
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        # 计算树的直径
        self.diameter = self.find_diameter()
        
        # 特殊情况：直径为0（只有一个节点）
        if self.diameter == 0:
            return 1  # 只有一种方案：选择这个节点
        
        # 计算子树大小
        subtree_size = [0] * (self.n + 1)
        self.dfs_subtree_size(1, 0, subtree_size)
        
        # 计算深度
        depth = [0] * (self.n + 1)
        self.dfs_depth(1, 0, 0, depth)
        
        # 找到深度最大的节点
        deepest_node = 1
        for i in range(2, self.n + 1):
            if depth[i] > depth[deepest_node]:
                deepest_node = i
        
        # 从最深节点再次DFS，找到直径的端点
        self.dfs_depth(deepest_node, 0, 0, depth)
        endpoint1 = deepest_node
        for i in range(1, self.n + 1):
            if depth[i] > depth[endpoint1]:
                endpoint1 = i
        
        # 从endpoint1再次DFS，找到另一个端点
        self.dfs_depth(endpoint1, 0, 0, depth)
        endpoint2 = 1
        for i in range(2, self.n + 1):
            if depth[i] > depth[endpoint2]:
                endpoint2 = i
        
        # 计算满足条件的方案数
        # 如果直径是偶数，有一个中心点
        # 如果直径是奇数，有一个中心边
        if self.diameter % 2 == 0:
            # 直径为偶数，有一个中心点
            # 找到中心点
            center = 0
            self.dfs_depth(endpoint1, 0, 0, depth)
            for i in range(1, self.n + 1):
                if depth[i] == self.diameter // 2 and self.bfs(i)[1] == self.diameter // 2:
                    center = i
                    break
            
            # 计算以中心点为根的子树中满足条件的方案数
            result = 1
            for v in self.graph[center]:
                # 计算每个子树中满足条件的方案数
                subtree_ways = self.power(2, subtree_size[v]) - 1
                result = (result * subtree_ways) % MOD
            
            # 至少选择两个节点
            result = (result - 1 + MOD) % MOD
            return result
        else:
            # 直径为奇数，有一个中心边
            # 找到中心边的两个端点
            self.dfs_depth(endpoint1, 0, 0, depth)
            center1 = 0
            center2 = 0
            for i in range(1, self.n + 1):
                if depth[i] == self.diameter // 2:
                    if center1 == 0:
                        center1 = i
                    else:
                        center2 = i
                        break
            
            # 计算每个部分中满足条件的方案数
            ways1 = self.power(2, subtree_size[center1]) - 1
            ways2 = self.power(2, subtree_size[center2]) - 1
            
            result = (ways1 * ways2) % MOD
            return result
    
    def read_input_and_solve(self):
        """
        读取输入并求解
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        # 读取节点数
        self.n = int(input())
        
        # 读取边信息
        for _ in range(self.n - 1):
            u, v = map(int, input().split())
            self.add_edge(u, v)
        
        # 计算并输出结果
        print(self.solve())

# 主函数
if __name__ == "__main__":
    # 由于这是在线评测题目，实际提交时需要取消下面的注释
    # solution = AtCoderABC221FDiameterSet()
    # solution.read_input_and_solve()
    
    # 示例测试
    solution = AtCoderABC221FDiameterSet()
    solution.n = 4
    solution.add_edge(1, 2)
    solution.add_edge(2, 3)
    solution.add_edge(3, 4)
    print("示例输出:", solution.solve())