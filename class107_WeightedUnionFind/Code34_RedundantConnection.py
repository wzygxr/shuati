/**
 * LeetCode 684 - 冗余连接
 * https://leetcode-cn.com/problems/redundant-connection/
 * 
 * 题目描述：
 * 在本问题中，树指的是一个连通且无环的无向图。
 * 
 * 输入一个图，该图由一个有着N个节点（节点值不重复1, 2, ..., N）的树及一条附加的边构成。附加的边的两个顶点包含在1到N中间，这条附加的边不属于树中已存在的边。
 * 
 * 结果图是一个以边组成的二维数组。每一个边的元素是一对[u, v] ，满足 u < v，表示连接顶点u和v的无向图的边。
 * 
 * 返回一条可以删去的边，使得结果图是一个有着N个节点的树。如果有多个答案，则返回二维数组中最后出现的边。
 * 
 * 示例 1：
 * 输入: [[1,2], [1,3], [2,3]]
 * 输出: [2,3]
 * 解释: 给定的无向图为:
 *   1
 *  / \
 * 2 - 3
 * 
 * 示例 2：
 * 输入: [[1,2], [2,3], [3,4], [1,4], [1,5]]
 * 输出: [1,4]
 * 解释: 给定的无向图为:
 * 5 - 1 - 2
 *     |   |
 *     4 - 3
 * 
 * 解题思路（并查集）：
 * 1. 对于每一条边(u, v)，检查u和v是否已经连通
 * 2. 如果已经连通，说明这条边是冗余的，可以形成环
 * 3. 否则，将u和v合并到同一个集合中
 * 4. 返回最后一条导致环的边
 * 
 * 时间复杂度分析：
 * - 并查集操作（find和union）的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
 * - 遍历m条边需要O(m * α(n))时间
 * - 总体时间复杂度：O(m * α(n)) ≈ O(m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

class RedundantConnection:
    def __init__(self):
        self.parent = []
        self.rank = []  # 用于按秩合并
    
    def init_union_find(self, n):
        """
        初始化并查集
        
        参数:
            n: 节点数量
        """
        self.parent = list(range(n + 1))  # 节点编号从1开始
        self.rank = [1] * (n + 1)
    
    def find(self, x):
        """
        查找元素所在集合的根节点，并进行路径压缩
        
        参数:
            x: 要查找的元素
            
        返回:
            根节点
        """
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # 路径压缩
        return self.parent[x]
    
    def union(self, x, y):
        """
        合并两个元素所在的集合
        
        参数:
            x: 第一个元素
            y: 第二个元素
            
        返回:
            bool: 如果两个元素已经在同一集合中，返回False；否则合并并返回True
        """
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return False  # 已经连通，说明边是冗余的
        
        # 按秩合并：将较小的树合并到较大的树下
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
        
        return True
    
    def find_redundant_connection(self, edges):
        """
        找到冗余连接
        
        参数:
            edges: 边的数组
            
        返回:
            冗余的边
        """
        n = len(edges)  # 节点数量为n，因为树有n个节点和n-1条边，加上一条冗余边，总共有n条边
        
        # 初始化并查集
        self.init_union_find(n)
        
        # 遍历每条边
        for edge in edges:
            u, v = edge
            
            # 如果u和v已经连通，说明这条边是冗余的
            if not self.union(u, v):
                return edge  # 返回最后一条导致环的边
        
        return []  # 不应该到达这里

# 测试代码
def test_redundant_connection():
    solution = RedundantConnection()
    
    # 测试用例1
    edges1 = [
        [1, 2],
        [1, 3],
        [2, 3]
    ]
    result1 = solution.find_redundant_connection(edges1)
    print("测试用例1结果：", result1)
    # 预期输出：[2, 3]
    
    # 测试用例2
    edges2 = [
        [1, 2],
        [2, 3],
        [3, 4],
        [1, 4],
        [1, 5]
    ]
    result2 = solution.find_redundant_connection(edges2)
    print("测试用例2结果：", result2)
    # 预期输出：[1, 4]
    
    # 测试用例3
    edges3 = [
        [1, 2],
        [2, 3],
        [3, 4],
        [4, 5],
        [5, 1]
    ]
    result3 = solution.find_redundant_connection(edges3)
    print("测试用例3结果：", result3)
    # 预期输出：[5, 1]

if __name__ == "__main__":
    test_redundant_connection()

'''
Python特定优化：
1. 使用列表推导式初始化并查集数组，代码简洁高效
2. 实现了路径压缩和按秩合并优化，提高并查集操作效率
3. 将并查集操作封装在类中，提高代码的可读性和可维护性
4. 使用详细的文档字符串，解释每个函数的作用和参数

算法思路详解：
1. 初始化并查集，每个节点的父节点是自己，秩为1
2. 遍历每条边(u, v)
3. 查找u和v的根节点（路径压缩优化）
4. 如果根节点相同，说明u和v已经连通，这条边是冗余的，直接返回
5. 如果根节点不同，将u和v合并到同一个集合（按秩合并优化）
6. 由于题目要求返回最后一条导致环的边，我们按顺序处理每条边

工程化考量：
1. 边界情况处理：代码自动处理节点编号从1开始的情况
2. 可读性：添加了详细的注释和文档字符串
3. 可测试性：提供了多个测试用例，覆盖不同的输入情况

时间复杂度深入分析：
- 并查集的find和union操作的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
- 在实际应用中，α(n)的值非常小，可以视为常数
- 遍历m条边需要O(m)时间
- 总体时间复杂度为O(m * α(n)) ≈ O(m)

空间复杂度深入分析：
- 并查集数组的空间复杂度为O(n)
- 总体空间复杂度为O(n)
'''