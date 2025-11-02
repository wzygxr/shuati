# CSES 1131 Tree Diameter
# 题目：给定一棵树，求树的直径（树中任意两点间最长的简单路径）
# 来源：CSES Problem Set - Tree Algorithms
# 链接：https://cses.fi/problemset/task/1131

from collections import deque, defaultdict

class CS ESTreeDiameter:
    def __init__(self):
        self.n = 0  # 节点数
        self.graph = defaultdict(list)  # 邻接表存储树
    
    def add_edge(self, u, v):
        """添加无向边"""
        self.graph[u].append(v)
        self.graph[v].append(u)
    
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
    
    def solve(self):
        """
        主方法
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        # 读取节点数
        self.n = int(input())
        
        # 读取边信息
        for _ in range(self.n - 1):
            u, v = map(int, input().split())
            self.add_edge(u, v)
        
        # 计算并输出树的直径
        print(self.find_diameter())

# 主函数
if __name__ == "__main__":
    # 由于这是在线评测题目，实际提交时需要取消下面的注释
    # solution = CSES1131TreeDiameter()
    # solution.solve()
    
    # 示例测试
    solution = CSES1131TreeDiameter()
    solution.n = 4
    solution.add_edge(1, 2)
    solution.add_edge(2, 3)
    solution.add_edge(3, 4)
    print("示例输出:", solution.find_diameter())  # 应该输出3