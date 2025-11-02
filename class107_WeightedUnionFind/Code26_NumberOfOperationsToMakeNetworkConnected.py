/**
 * LeetCode 1319 - 连通网络的操作次数
 * https://leetcode-cn.com/problems/number-of-operations-to-make-network-connected/
 * 
 * 题目描述：
 * 用以太网线缆将 n 台计算机连接成一个网络，计算机的编号从 0 到 n-1。线缆用 connections 表示，其中 connections[i] = [a, b] 表示连接了计算机 a 和 b。
 * 
 * 网络中的任何一台计算机都可以通过网络直接或者间接访问同一个网络中其他任意一台计算机。
 * 
 * 给你这个计算机网络的初始布线 connections，你可以拔开任意两台直连计算机之间的线缆，并用它连接一对未直连的计算机。请你计算并返回使所有计算机都连通所需的最少操作次数。如果不可能，则返回 -1 。
 * 
 * 解题思路：
 * 1. 使用并查集来计算网络中的连通分量数量
 * 2. 首先，我们需要检查线缆数量是否足够：至少需要n-1条线缆才能连接n台计算机
 * 3. 使用并查集统计连通分量的数量count
 * 4. 将所有计算机连通所需的最少操作次数为count - 1
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理所有连接：O(m * α(n))，其中m是connections数组的长度，α是阿克曼函数的反函数，近似为常数
 * - 计算连通分量：O(n)
 * - 总体时间复杂度：O(n + m * α(n)) ≈ O(n + m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

class NumberOfOperationsToMakeNetworkConnected:
    def __init__(self):
        # 并查集的父节点数组
        self.parent = []
        # 并查集的秩数组，用于按秩合并优化
        self.rank = []
        # 连通分量的数量
        self.count = 0
    
    def find(self, x):
        """
        查找元素所在集合的根节点，并进行路径压缩
        
        参数:
            x (int): 要查找的元素
            
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
            n (int): 计算机数量
        """
        # 初始化，每个元素的父节点是自己，秩为0
        self.parent = list(range(n))
        self.rank = [0] * n
        self.count = n  # 初始时，每个计算机都是一个独立的连通分量
    
    def make_connected(self, n, connections):
        """
        计算使所有计算机都连通所需的最少操作次数
        
        参数:
            n (int): 计算机数量
            connections (List[List[int]]): 连接列表
            
        返回:
            int: 最少操作次数，如果不可能则返回-1
        """
        # 检查线缆数量是否足够：至少需要n-1条线缆
        if len(connections) < n - 1:
            return -1
        
        # 初始化并查集
        self.init_union_find(n)
        
        # 处理所有连接
        for a, b in connections:
            root_a = self.find(a)
            root_b = self.find(b)
            
            if root_a != root_b:
                # 按秩合并：将秩小的树连接到秩大的树下
                if self.rank[root_a] < self.rank[root_b]:
                    self.parent[root_a] = root_b
                elif self.rank[root_a] > self.rank[root_b]:
                    self.parent[root_b] = root_a
                else:
                    # 秩相同时，任选一个作为根，并增加其秩
                    self.parent[root_b] = root_a
                    self.rank[root_a] += 1
                # 合并后，连通分量数量减1
                self.count -= 1
        
        # 将所有计算机连通所需的最少操作次数为连通分量数量减1
        return self.count - 1

# 测试代码
def test_make_connected():
    solution = NumberOfOperationsToMakeNetworkConnected()
    
    # 测试用例1
    n1 = 4
    connections1 = [[0, 1], [0, 2], [1, 2]]
    print("测试用例1结果：", solution.make_connected(n1, connections1))
    # 预期输出：1
    
    # 测试用例2
    n2 = 6
    connections2 = [[0, 1], [0, 2], [0, 3], [1, 2], [1, 3]]
    print("测试用例2结果：", solution.make_connected(n2, connections2))
    # 预期输出：2
    
    # 测试用例3
    n3 = 6
    connections3 = [[0, 1], [0, 2], [0, 3], [1, 2]]
    print("测试用例3结果：", solution.make_connected(n3, connections3))
    # 预期输出：-1
    
    # 测试用例4：已经连通的情况
    n4 = 5
    connections4 = [[0, 1], [1, 2], [2, 3], [3, 4]]
    print("测试用例4结果：", solution.make_connected(n4, connections4))
    # 预期输出：0
    
    # 测试用例5：只有一台计算机
    n5 = 1
    connections5 = []
    print("测试用例5结果：", solution.make_connected(n5, connections5))
    # 预期输出：0
    
    # 测试用例6：大型测试用例
    n6 = 100
    connections6 = [[i, i+1] for i in range(90)]  # 90条连接，足够连接100台计算机
    print("测试用例6结果：", solution.make_connected(n6, connections6))
    # 预期输出：10 （100-90-1=9？不，实际计算应该是连通分量数量减1，初始有100个连通分量，每次连接减少一个，90次连接后有11个连通分量，所以需要10次操作）

if __name__ == "__main__":
    test_make_connected()

'''
Python特定优化：
1. 使用列表推导式初始化parent数组，提高代码简洁性
2. 利用Python的列表索引特性，简化并查集的实现
3. 将所有方法封装在类中，提高代码的组织性和可复用性
4. 使用Python的动态类型特性，避免了不必要的类型声明

算法思路详解：
1. 问题转化：将问题转化为求连通分量的数量
2. 贪心策略：为了最小化操作次数，我们需要连接所有的连通分量
3. 并查集应用：并查集是处理连通分量问题的高效数据结构

工程化考量：
1. 输入验证：在实际应用中，需要验证输入参数的有效性
2. 性能优化：对于大规模数据，可以考虑使用更高效的路径压缩实现
3. 可扩展性：可以将并查集抽象成一个独立的类，以便在其他问题中复用
4. 异常处理：对于边界情况（如n=1），需要特殊处理

时间复杂度分析深入：
- 并查集的find和union操作的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
- 对于n个元素和m次操作，总体时间复杂度为O(n + m * α(n))
- 在实际应用中，α(n)增长极其缓慢，对于任何可能的n值，α(n)不超过4，因此可以近似认为是O(n + m)

空间复杂度分析深入：
- 并查集需要两个长度为n的数组，因此空间复杂度为O(n)
- 不需要额外的空间存储中间结果，空间利用率高
'''