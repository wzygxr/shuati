"""
等式方程的可满足性
给定一个由表示变量之间关系的字符串方程组成的数组，每个字符串方程 equations[i] 的长度为 4，
并采用两种不同的形式之一："a==b" 或 "a!=b"。在这里，a 和 b 是小写字母（不一定不同），
表示单字母变量名。只有当可以将整数分配给变量名，以便满足所有给定的方程时才返回 true，否则返回 false。

示例 1:
输入: ["a==b","b!=a"]
输出: false
解释: 如果我们指定，a = 1 且 b = 1，那么可以满足第一个方程，但无法满足第二个方程。没有办法分配变量同时满足这两个方程。

示例 2:
输入: ["b==a","a==b"]
输出: true
解释: 我们可以指定 a = 1 且 b = 1 以满足满足这两个方程。

示例 3:
输入: ["a==b","b==c","a==c"]
输出: true

示例 4:
输入: ["a==b","b!=c","c==a"]
输出: false

示例 5:
输入: ["c==c","b==d","x!=z"]
输出: true

约束条件：
1 <= equations.length <= 500
equations[i].length == 4
equations[i][0] 和 equations[i][3] 是小写字母
equations[i][1] 是 '=' 或 '!'
equations[i][2] 是 '='

测试链接: https://leetcode.cn/problems/satisfiability-of-equality-equations/
相关平台: LeetCode 990
"""


class UnionFind:
    """
    并查集数据结构实现
    包含路径压缩优化
    """

    def __init__(self, n):
        """
        初始化并查集
        :param n: 节点数量
        """
        # parent[i]表示节点i的父节点
        self.parent = list(range(n))

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
        :param x: 第一个节点
        :param y: 第二个节点
        """
        root_x = self.find(x)
        root_y = self.find(y)
        # 如果已经在同一个集合中，则无需合并
        if root_x != root_y:
            self.parent[root_x] = root_y


def equations_possible(equations):
    """
    使用并查集解决等式方程的可满足性问题

    解题思路：
    1. 首先处理所有等式方程，将相等的变量连接到同一个集合中
    2. 然后检查所有不等式方程，如果两个变量在同一个集合中，则返回false
    3. 如果所有不等式方程都满足，则返回true

    时间复杂度：O(N * α(26))，其中N是方程数量，α是阿克曼函数的反函数
    空间复杂度：O(1)，因为只有26个小写字母
    是否为最优解：是

    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 可配置性：可以扩展支持更多变量类型
    3. 线程安全：当前实现不是线程安全的

    与机器学习等领域的联系：
    1. 逻辑推理：等式推理是符号AI中的基础问题
    2. 约束满足问题：在约束编程中有广泛应用

    语言特性差异:
    Java: 对象引用和垃圾回收，数组初始化
    C++: 指针操作和手动内存管理，vector容器
    Python: 动态类型和自动内存管理，list初始化

    极端输入场景:
    1. 空数组
    2. 只有等式方程
    3. 只有不等式方程
    4. 自相矛盾的方程（如a==b和a!=b同时存在）

    性能优化:
    1. 路径压缩优化find操作
    2. 分两阶段处理（先处理等式，再检查不等式）
    3. 提前终止优化

    :param equations: 方程数组
    :return: 如果可以满足所有方程返回True，否则返回False
    """
    # 边界条件检查
    if not equations:
        return True

    # 创建并查集，大小为26（对应26个小写字母）
    union_find = UnionFind(26)

    # 首先处理所有等式方程
    for equation in equations:
        if equation[1] == '=':
            x = ord(equation[0]) - ord('a')
            y = ord(equation[3]) - ord('a')
            union_find.union(x, y)

    # 然后检查所有不等式方程
    for equation in equations:
        if equation[1] == '!':
            x = ord(equation[0]) - ord('a')
            y = ord(equation[3]) - ord('a')
            # 如果两个变量在同一个集合中，则不等式不成立
            if union_find.find(x) == union_find.find(y):
                return False

    return True


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    equations1 = ["a==b", "b!=a"]
    print("测试用例1结果:", equations_possible(equations1))  # 预期输出: false

    # 测试用例2
    equations2 = ["b==a", "a==b"]
    print("测试用例2结果:", equations_possible(equations2))  # 预期输出: true

    # 测试用例3
    equations3 = ["a==b", "b==c", "a==c"]
    print("测试用例3结果:", equations_possible(equations3))  # 预期输出: true

    # 测试用例4
    equations4 = ["a==b", "b!=c", "c==a"]
    print("测试用例4结果:", equations_possible(equations4))  # 预期输出: false

    # 测试用例5
    equations5 = ["c==c", "b==d", "x!=z"]
    print("测试用例5结果:", equations_possible(equations5))  # 预期输出: true

    # 测试用例6：空数组
    equations6 = []
    print("测试用例6结果:", equations_possible(equations6))  # 预期输出: true