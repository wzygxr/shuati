# LintCode 1577. 子树计数
# 题目描述：给定一棵树，计算以每个节点为根的子树的重心。
# 算法思想：对于每个子树，找到其重心。利用树的重心的性质：子树的重心一定在原树重心到该子树根的路径上。
# 测试链接：https://www.lintcode.com/problem/1577/
# 时间复杂度：O(n^2)，对于每个节点都要重新计算子树的重心
# 空间复杂度：O(n)

class Code22_LintCode1577:
    def __init__(self):
        self.n = 0
        self.graph = []
        self.res = []
        self.visited = []
        self.size = []
        self.maxSubtree = []
        self.minMaxSubtree = 0
        self.centroid = -1
    
    def dfs(self, u, parent):
        """计算子树大小"""
        self.visited[u] = True
        self.size[u] = 1
        self.maxSubtree[u] = 0
        for v in self.graph[u]:
            if not self.visited[v] and v != parent:
                self.dfs(v, u)
                self.size[u] += self.size[v]
                self.maxSubtree[u] = max(self.maxSubtree[u], self.size[v])
    
    def find_centroid(self, u, parent, total_size):
        """寻找子树的重心"""
        # 计算父方向的子树大小
        max_size = max(self.maxSubtree[u], total_size - self.size[u])
        
        # 更新重心
        if max_size < self.minMaxSubtree or (max_size == self.minMaxSubtree and u < self.centroid):
            self.minMaxSubtree = max_size
            self.centroid = u
        
        for v in self.graph[u]:
            if v != parent and self.visited[v]:
                self.find_centroid(v, u, total_size)
    
    def get_subtree_centroid(self, n, edges):
        """
        计算以每个节点为根的子树的重心
        :param n: 节点数
        :param edges: 边列表
        :return: 结果数组
        """
        self.n = n
        # 构建邻接表
        self.graph = [[] for _ in range(n)]
        for u, v in edges:
            self.graph[u].append(v)
            self.graph[v].append(u)
        
        self.res = [0] * n
        # 对每个节点作为根，计算其子树的重心
        for i in range(n):
            self.visited = [False] * n
            self.size = [0] * n
            self.maxSubtree = [0] * n
            self.minMaxSubtree = float('inf')
            self.centroid = -1
            
            # 计算子树大小
            self.dfs(i, -1)
            
            # 找到重心
            self.find_centroid(i, -1, self.size[i])
            
            self.res[i] = self.centroid
        
        return self.res
    
    def print_array(self, arr):
        """打印数组"""
        print(f"{arr}")

# 测试代码
def main():
    solution = Code22_LintCode1577()
    
    # 测试用例1
    n1 = 3
    edges1 = [[0, 1], [0, 2]]
    res1 = solution.get_subtree_centroid(n1, edges1)
    print("测试用例1结果:", end=" ")
    solution.print_array(res1)
    # 期望输出: [0, 0, 0]
    
    # 测试用例2
    n2 = 4
    edges2 = [[0, 1], [1, 2], [1, 3]]
    res2 = solution.get_subtree_centroid(n2, edges2)
    print("测试用例2结果:", end=" ")
    solution.print_array(res2)
    # 期望输出: [1, 1, 1, 1]
    
    # 测试用例3：一条链的情况
    n3 = 5
    edges3 = [[0, 1], [1, 2], [2, 3], [3, 4]]
    res3 = solution.get_subtree_centroid(n3, edges3)
    print("测试用例3结果:", end=" ")
    solution.print_array(res3)
    # 对于链状结构，每个子树的重心应该在中间位置

if __name__ == "__main__":
    main()

# 注意：
# 1. 树的重心是指：对于节点u，删除u后剩余的各个连通块的大小不超过原树大小的一半
# 2. 本算法对于每个节点都重新计算子树的重心，时间复杂度为O(n^2)
# 3. 对于更大的数据规模，可以利用树的重心的性质进行优化，如利用点分治的思想
# 4. 树的重心的重要性质：子树的重心一定在原树重心到该子树根的路径上
# 5. 可以利用这个性质将时间复杂度优化到O(n)，但需要更复杂的实现