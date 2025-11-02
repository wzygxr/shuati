"""
HackerRank Components in a graph

题目描述：
Given a list of edges, determine the size of the smallest and largest connected components that have 2 or more nodes. A node can have any number of connections.

输入格式：
The first line contains an integer, q, the number of queries.
Each of the following q sets of lines is as follows:
- The first line contains an integer, n, the number of nodes in the graph.
- Each of the next n lines contains two space-separated integers, u and v, describing an edge connecting node u to node v.

输出格式：
For each query, print two space-separated integers, the smallest and largest components with 2 or more nodes.

样例输入：
1
5
1 6 
2 7
3 8
4 9
2 6

样例输出：
2 4

题目链接：https://www.hackerrank.com/challenges/components-in-graph/problem

解题思路：
使用并查集解决。将相连的节点合并到同一个集合中，统计每个集合的大小，找出最小和最大的集合大小。

时间复杂度：O(n*α(n))，其中α是阿克曼函数的反函数
空间复杂度：O(n)
是否为最优解：是

工程化考量：
1. 异常处理：检查输入是否合法
2. 可配置性：可以修改节点连接规则
3. 线程安全：当前实现不是线程安全的

与机器学习等领域的联系：
1. 社交网络分析：识别社区结构
2. 图论算法：连通分量分析

语言特性差异:
Java: 对象引用和垃圾回收
C++: 指针操作和手动内存管理
Python: 动态类型和自动内存管理

极端输入场景:
1. 没有边
2. 所有节点连通
3. 每个节点独立

性能优化:
1. 路径压缩优化find操作
2. 按秩合并优化union操作
"""


class UnionFind:
    """
    并查集类
    """

    def __init__(self, n):
        """
        初始化并查集
        :param n: 节点数量
        """
        self.parent = list(range(n + 1))  # parent[i]表示节点i的父节点，节点编号从1开始
        self.rank = [1] * (n + 1)        # rank[i]表示以i为根的树的高度上界
        self.size = [1] * (n + 1)        # size[i]表示以i为根的集合的大小

    def find(self, x):
        """
        查找节点的根节点（代表元素）
        使用路径压缩优化
        :param x: 要查找的节点
        :return: 节点x所在集合的根节点
        """
        if self.parent[x] != x:
            # 路径压缩：将路径上的所有节点直接连接到根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x, y):
        """
        合并两个集合
        使用按秩合并优化
        :param x: 第一个节点
        :param y: 第二个节点
        """
        root_x = self.find(x)
        root_y = self.find(y)

        # 如果已经在同一个集合中，直接返回
        if root_x == root_y:
            return

        # 按秩合并：将秩小的树合并到秩大的树下
        if self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
            self.size[root_x] += self.size[root_y]  # 更新集合大小
        elif self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
            self.size[root_y] += self.size[root_x]  # 更新集合大小
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
            self.size[root_x] += self.size[root_y]  # 更新集合大小

    def get_size(self, x):
        """
        获取包含指定节点的集合的大小
        :param x: 节点
        :return: 集合大小
        """
        return self.size[self.find(x)]


def find_components(n, edges):
    """
    计算最小和最大连通分量大小
    :param n: 节点数量
    :param edges: 边
    :return: 包含最小和最大连通分量大小的数组
    """
    # 创建并查集
    uf = UnionFind(2 * n)  # 节点编号可能达到2*n

    # 处理每条边
    for edge in edges:
        uf.union(edge[0], edge[1])

    # 统计每个集合的大小
    min_size = float('inf')
    max_size = 0

    # 遍历所有节点，找出根节点并统计集合大小
    for i in range(1, 2 * n + 1):
        # 如果是根节点且集合大小大于1
        if uf.find(i) == i and uf.get_size(i) > 1:
            min_size = min(min_size, uf.get_size(i))
            max_size = max(max_size, uf.get_size(i))

    # 如果没有找到大小大于1的集合，返回[0, 0]
    if min_size == float('inf'):
        return [0, 0]

    return [min_size, max_size]


# 测试方法
if __name__ == "__main__":
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    # 读取查询数量
    q = int(data[idx])
    idx += 1
    
    # 处理每个查询
    for i in range(q):
        # 读取节点数量
        n = int(data[idx])
        idx += 1
        
        # 存储边
        edges = []
        
        # 读取边
        for j in range(n):
            u = int(data[idx])
            idx += 1
            v = int(data[idx])
            idx += 1
            edges.append([u, v])
        
        # 计算结果
        result = find_components(n, edges)
        
        # 输出结果
        print(result[0], result[1])