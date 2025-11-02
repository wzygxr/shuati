"""
连通网络的操作次数
用以太网线缆将 n 台计算机连接成一个网络，计算机的编号从 0 到 n-1。
线缆用 connections 表示，其中 connections[i] = [a, b] 连接了计算机 a 和 b。
网络中的任何一台计算机都可以通过网络直接或间接访问同一个网络中其他任意一台计算机。
给你这个计算机网络的初始布线 connections，你可以拔开任意两台直连计算机之间的线缆，
并用它连接一对未直连的计算机。请你计算并返回使所有计算机都连通所需的最少操作次数。
如果不可能，则返回 -1。

示例 1:
输入: n = 4, connections = [[0,1],[0,2],[1,2]]
输出: 1
解释: 拔下计算机 1 和 2 之间的线缆，并将它插到计算机 1 和 3 上。

示例 2:
输入: n = 6, connections = [[0,1],[0,2],[0,3],[1,2],[1,3]]
输出: 2

示例 3:
输入: n = 6, connections = [[0,1],[0,2],[0,3],[1,2]]
输出: -1
解释: 线缆数量不足。

示例 4:
输入: n = 5, connections = [[0,1],[0,2],[3,4],[2,3]]
输出: 0

约束条件：
1 <= n <= 10^5
1 <= connections.length <= min(n*(n-1)/2, 10^5)
connections[i].length == 2
0 <= connections[i][0], connections[i][1] < n
connections[i][0] != connections[i][1]
没有重复的连接
两台计算机不会通过多条线缆连接

测试链接: https://leetcode.cn/problems/number-of-operations-to-make-network-connected/
相关平台: LeetCode 1319
"""


class UnionFind:
    """
    并查集数据结构实现
    包含路径压缩和按秩合并优化
    """

    def __init__(self, n):
        """
        初始化并查集
        :param n: 节点数量
        """
        # parent[i]表示节点i的父节点
        self.parent = list(range(n))
        # rank[i]表示以i为根的树的高度上界
        self.rank = [1] * n
        # 连通分量数量
        self.components = n

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

        # 如果已经在同一个集合中，则无需合并
        if root_x != root_y:
            # 按秩合并：将秩小的树合并到秩大的树下
            if self.rank[root_x] > self.rank[root_y]:
                self.parent[root_y] = root_x
            elif self.rank[root_x] < self.rank[root_y]:
                self.parent[root_x] = root_y
            else:
                # 秩相等时，任选一个作为根，并将其秩加1
                self.parent[root_y] = root_x
                self.rank[root_x] += 1
            # 连通分量数量减1
            self.components -= 1

    def get_components(self):
        """
        获取连通分量数量
        :return: 连通分量数量
        """
        return self.components


def make_connected(n, connections):
    """
    使用并查集解决连通网络的操作次数问题

    解题思路：
    1. 首先检查线缆数量是否足够，至少需要n-1条线缆才能连接n台计算机
    2. 使用并查集统计当前有多少个连通分量
    3. 要连接k个连通分量，需要k-1次操作

    时间复杂度：O(M * α(N))，其中M是connections的长度，N是计算机数量，α是阿克曼函数的反函数
    空间复杂度：O(N)
    是否为最优解：是

    工程化考量：
    1. 异常处理：检查输入参数的有效性
    2. 可配置性：可以扩展支持权重边或其他网络属性
    3. 线程安全：当前实现不是线程安全的

    与机器学习等领域的联系：
    1. 网络分析：社交网络、通信网络的连通性分析
    2. 图论：最小生成树、网络流等算法的基础

    语言特性差异:
    Java: 对象引用和垃圾回收，数组初始化
    C++: 指针操作和手动内存管理，vector容器
    Python: 动态类型和自动内存管理，list初始化

    极端输入场景:
    1. 单个计算机
    2. 线缆数量不足
    3. 所有计算机已经连通
    4. 大规模网络

    性能优化:
    1. 路径压缩优化find操作
    2. 按秩合并优化union操作
    3. 提前终止优化（线缆数量检查）

    :param n: 计算机数量
    :param connections: 线缆连接关系
    :return: 最少操作次数，如果不可能则返回-1
    """
    # 边界条件检查
    if n <= 1:
        return 0

    # 线缆数量不足，无法连接所有计算机
    if len(connections) < n - 1:
        return -1

    # 创建并查集
    union_find = UnionFind(n)

    # 处理所有连接
    for connection in connections:
        a, b = connection[0], connection[1]
        # 如果两个计算机已经在同一个连通分量中，则这条线缆是冗余的
        if union_find.find(a) == union_find.find(b):
            continue
        else:
            union_find.union(a, b)

    # 统计连通分量数量
    components = union_find.get_components()

    # 需要components-1次操作来连接所有连通分量
    return components - 1


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    n1 = 4
    connections1 = [[0, 1], [0, 2], [1, 2]]
    print("测试用例1结果:", make_connected(n1, connections1))  # 预期输出: 1

    # 测试用例2
    n2 = 6
    connections2 = [[0, 1], [0, 2], [0, 3], [1, 2], [1, 3]]
    print("测试用例2结果:", make_connected(n2, connections2))  # 预期输出: 2

    # 测试用例3
    n3 = 6
    connections3 = [[0, 1], [0, 2], [0, 3], [1, 2]]
    print("测试用例3结果:", make_connected(n3, connections3))  # 预期输出: -1

    # 测试用例4
    n4 = 5
    connections4 = [[0, 1], [0, 2], [3, 4], [2, 3]]
    print("测试用例4结果:", make_connected(n4, connections4))  # 预期输出: 0

    # 测试用例5：单个计算机
    n5 = 1
    connections5 = []
    print("测试用例5结果:", make_connected(n5, connections5))  # 预期输出: 0