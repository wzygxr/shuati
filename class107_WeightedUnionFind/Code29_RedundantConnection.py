/**
 * LeetCode 684 - 冗余连接
 * https://leetcode-cn.com/problems/redundant-connection/
 * 
 * 题目描述：
 * 在本问题中，树指的是一个连通且无环的无向图。
 * 
 * 输入一个图，该图由一个有着n个节点（节点值不重复1，2，...，n）的树及一条附加的边构成。附加的边的两个顶点包含在1到n中间，
 * 这条附加的边不属于树中已存在的边。
 * 
 * 结果图是一个以边组成的二维数组edges。每一个边的元素是一对[u, v]，满足u < v，表示连接顶点u和v的无向图的边。
 * 
 * 返回一条可以删去的边，使得结果图是一个有着n个节点的树。如果有多个答案，则返回二维数组中最后出现的边。
 * 
 * 解题思路：
 * 1. 使用并查集来检测环
 * 2. 遍历每一条边，尝试将两个顶点合并
 * 3. 如果两个顶点已经在同一个集合中，说明添加这条边会形成环，这条边就是冗余的
 * 4. 返回最后一条导致环的边
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理每条边：O(m * α(n))，其中m是边的数量，α是阿克曼函数的反函数，近似为常数
 * - 总体时间复杂度：O(n + m * α(n)) ≈ O(n + m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

class RedundantConnection:
    def __init__(self):
        # 并查集的父节点数组
        self.parent = []
        # 并查集的秩数组，用于按秩合并优化
        self.rank = []
    
    def find(self, x):
        """
        查找元素所在集合的根节点，并进行路径压缩
        
        参数:
            x (int): 要查找的节点
            
        返回:
            int: 根节点
        """
        if self.parent[x] != x:
            # 路径压缩：将x的父节点直接设置为根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def init_union_find(self, n):
        """
        初始化并查集
        
        参数:
            n (int): 节点数量
        """
        # 初始化，每个节点的父节点是自己，秩为0
        self.parent = list(range(n + 1))  # 节点编号从1开始
        self.rank = [0] * (n + 1)
    
    def find_redundant_connection(self, edges):
        """
        查找冗余连接
        
        参数:
            edges (List[List[int]]): 边的数组
            
        返回:
            List[int]: 冗余的边
        """
        n = len(edges)  # 节点数量等于边的数量（树有n-1条边，加上一条冗余边）
        
        # 初始化并查集
        self.init_union_find(n)
        
        # 遍历每一条边
        for edge in edges:
            u, v = edge
            
            root_u = self.find(u)
            root_v = self.find(v)
            
            # 如果两个节点已经在同一个集合中，说明添加这条边会形成环
            if root_u == root_v:
                return edge
            
            # 按秩合并：将秩小的树连接到秩大的树下
            if self.rank[root_u] < self.rank[root_v]:
                self.parent[root_u] = root_v
            elif self.rank[root_u] > self.rank[root_v]:
                self.parent[root_v] = root_u
            else:
                # 秩相同时，任选一个作为根，并增加其秩
                self.parent[root_v] = root_u
                self.rank[root_u] += 1
        
        # 根据题目描述，一定存在冗余边，所以不会执行到这里
        return []

# 测试代码
def test_redundant_connection():
    solution = RedundantConnection()
    
    # 测试用例1
    edges1 = [[1, 2], [1, 3], [2, 3]]
    result1 = solution.find_redundant_connection(edges1)
    print("测试用例1结果：", result1)
    # 预期输出：[2, 3]
    
    # 测试用例2
    edges2 = [[1, 2], [2, 3], [3, 4], [1, 4], [1, 5]]
    result2 = solution.find_redundant_connection(edges2)
    print("测试用例2结果：", result2)
    # 预期输出：[1, 4]
    
    # 测试用例3
    edges3 = [[1, 2], [1, 3], [3, 4], [2, 4], [4, 5]]
    result3 = solution.find_redundant_connection(edges3)
    print("测试用例3结果：", result3)
    # 预期输出：[2, 4]
    
    # 测试用例4：简单情况
    edges4 = [[1, 2], [2, 1]]  # 自环的情况
    result4 = solution.find_redundant_connection(edges4)
    print("测试用例4结果：", result4)
    # 预期输出：[2, 1]

if __name__ == "__main__":
    test_redundant_connection()

'''
Python特定优化：
1. 使用列表推导式初始化parent数组，提高代码简洁性
2. 直接在遍历过程中检测环并返回结果，避免了额外的循环
3. 利用Python的动态列表特性，灵活处理节点编号从1开始的情况

算法思路详解：
1. 问题本质：找出导致图中出现环的最后一条边
2. 并查集应用：并查集是检测图中是否有环的高效数据结构
3. 贪心策略：依次添加边，并检测是否形成环，最后一个形成环的边就是答案

工程化考量：
1. 输入验证：在实际应用中，需要验证输入边的有效性
2. 性能优化：使用路径压缩和按秩合并优化并查集的性能
3. 可扩展性：可以将并查集抽象成一个独立的类，以便在其他问题中复用
4. 边界情况：需要处理节点编号从1开始的情况

时间复杂度分析深入：
- 并查集的find和union操作的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
- 对于m条边，总体时间复杂度为O(n + m * α(n))
- 在实际应用中，α(n)增长极其缓慢，对于任何可能的n值，α(n)不超过4，因此可以近似认为是O(n + m)

空间复杂度分析深入：
- 并查集需要两个长度为n+1的数组（因为节点编号从1开始），因此空间复杂度为O(n)
- 总体空间复杂度为O(n)
'''