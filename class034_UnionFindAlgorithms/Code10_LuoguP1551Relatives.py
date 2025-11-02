"""
洛谷 P1551 亲戚

题目描述：
若某个家族人员过于庞大，要判断两个是否是亲戚，确实还很不容易，现在给出某个亲戚关系图，求任意给出的两个人是否具有亲戚关系。

输入格式：
第一行：三个整数n,m,p，(n<=5000,m<=5000,p<=5000)，分别表示有n个人，m个亲戚关系，询问p对亲戚关系。
以下m行：每行两个数Mi，Mj，1<=Mi,Mj<=N，表示Mi和Mj具有亲戚关系。
接下来p行：每行两个数Pi，Pj，询问Pi和Pj是否具有亲戚关系。

输出格式：
对于每个询问，输出"YES"或"NO"。

样例输入：
6 5 3
1 2
1 5
3 4
5 2
1 3
1 4
2 3
5 6

样例输出：
YES
NO
NO

题目链接：https://www.luogu.com.cn/problem/P1551

解题思路：
使用并查集解决。将具有亲戚关系的人合并到同一个集合中，判断两个人是否是亲戚只需判断他们是否在同一个集合中。

时间复杂度：O((m+p)*α(n))，其中α是阿克曼函数的反函数
空间复杂度：O(n)
是否为最优解：是

工程化考量：
1. 异常处理：检查输入是否合法
2. 可配置性：可以修改亲戚关系的定义
3. 线程安全：当前实现不是线程安全的

与机器学习等领域的联系：
1. 社交网络分析：识别社交关系
2. 推荐系统：基于关系网络的推荐

语言特性差异:
Java: 对象引用和垃圾回收
C++: 指针操作和手动内存管理
Python: 动态类型和自动内存管理

极端输入场景:
1. 没有亲戚关系
2. 所有人都是亲戚
3. 每个人只和自己是亲戚

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
        self.parent = list(range(n + 1))  # parent[i]表示节点i的父节点，人员编号从1开始
        self.rank = [1] * (n + 1)        # rank[i]表示以i为根的树的高度上界

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
        elif self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1

    def is_connected(self, x, y):
        """
        判断两个节点是否在同一个集合中
        :param x: 第一个节点
        :param y: 第二个节点
        :return: 如果在同一个集合中返回True，否则返回False
        """
        return self.find(x) == self.find(y)


def check_relatives(n, relations, queries):
    """
    判断两个人是否是亲戚
    :param n: 人员数量
    :param relations: 亲戚关系
    :param queries: 查询
    :return: 查询结果
    """
    # 创建并查集
    uf = UnionFind(n)

    # 处理每个亲戚关系
    for relation in relations:
        uf.union(relation[0], relation[1])

    # 处理每个查询
    results = []
    for query in queries:
        if uf.is_connected(query[0], query[1]):
            results.append("YES")
        else:
            results.append("NO")

    return results


# 测试方法
if __name__ == "__main__":
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    # 读取人员数量、亲戚关系数量和查询数量
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    p = int(data[idx])
    idx += 1
    
    # 存储亲戚关系
    relations = []
    
    # 读取亲戚关系
    for i in range(m):
        mi = int(data[idx])
        idx += 1
        mj = int(data[idx])
        idx += 1
        relations.append([mi, mj])
    
    # 存储查询
    queries = []
    
    # 读取查询
    for i in range(p):
        pi = int(data[idx])
        idx += 1
        pj = int(data[idx])
        idx += 1
        queries.append([pi, pj])
    
    # 计算结果
    results = check_relatives(n, relations, queries)
    
    # 输出结果
    for result in results:
        print(result)