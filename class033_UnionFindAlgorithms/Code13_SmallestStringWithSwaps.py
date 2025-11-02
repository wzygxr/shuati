"""
交换字符串中的元素
给你一个字符串 s，以及该字符串中的一些「索引对」数组 pairs，
其中 pairs[i] = [a, b] 表示字符串中的两个索引（编号从 0 开始）。
你可以任意多次交换在 pairs 中任意一对索引处的字符。
返回在经过若干次交换后，s 可以变成的按字典序最小的字符串。

示例 1:
输入: s = "dcab", pairs = [[0,3],[1,2]]
输出: "bacd"
解释: 
交换 s[0] 和 s[3], s = "bcad"
交换 s[1] 和 s[2], s = "bacd"

示例 2:
输入: s = "dcab", pairs = [[0,3],[1,2],[0,2]]
输出: "abcd"
解释: 
交换 s[0] 和 s[3], s = "bcad"
交换 s[0] 和 s[2], s = "acbd"
交换 s[1] 和 s[2], s = "abcd"

示例 3:
输入: s = "cba", pairs = [[0,1],[1,2]]
输出: "abc"
解释: 
交换 s[0] 和 s[1], s = "bca"
交换 s[1] 和 s[2], s = "bac"
交换 s[0] 和 s[1], s = "abc"

约束条件：
1 <= s.length <= 10^5
0 <= pairs.length <= 10^5
0 <= pairs[i][0], pairs[i][1] < s.length
s 中只含有小写英文字母

测试链接: https://leetcode.cn/problems/smallest-string-with-swaps/
相关平台: LeetCode 1202
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


def smallest_string_with_swaps(s, pairs):
    """
    使用并查集解决交换字符串中的元素问题

    解题思路：
    1. 使用并查集将可以相互交换的索引分组到同一个连通组件中
    2. 对于每个连通组件，将对应的字符收集起来并排序
    3. 将排序后的字符按顺序放回原位置，得到字典序最小的字符串

    时间复杂度：O(N * log N + M * α(N))，其中N是字符串长度，M是索引对数量，α是阿克曼函数的反函数
    空间复杂度：O(N)
    是否为最优解：是

    工程化考量：
    1. 异常处理：检查输入参数的有效性
    2. 可配置性：可以扩展支持其他排序规则
    3. 线程安全：当前实现不是线程安全的

    与机器学习等领域的联系：
    1. 字符串处理：文本预处理和特征工程中的基础操作
    2. 优化问题：在约束条件下寻找最优解

    语言特性差异:
    Java: 对象引用和垃圾回收，数组初始化
    C++: 指针操作和手动内存管理，vector容器
    Python: 动态类型和自动内存管理，list初始化

    极端输入场景:
    1. 空字符串
    2. 单字符字符串
    3. 没有索引对
    4. 所有索引都可以相互交换
    5. 大规模字符串

    性能优化:
    1. 路径压缩优化find操作
    2. 使用哈希表快速分组索引
    3. 对字符进行排序以获得最小字典序

    :param s: 输入字符串
    :param pairs: 索引对数组
    :return: 字典序最小的字符串
    """
    # 边界条件检查
    if not s or len(s) <= 1:
        return s

    n = len(s)

    # 创建并查集
    union_find = UnionFind(n)

    # 处理所有索引对
    for pair in pairs:
        union_find.union(pair[0], pair[1])

    # 将同一连通组件的索引和字符分组
    components = {}
    for i in range(n):
        root = union_find.find(i)
        if root not in components:
            components[root] = []
        components[root].append(i)

    # 构造结果字符串
    result = [''] * n

    # 对每个连通组件内的字符进行排序并放回对应位置
    for indices in components.values():
        # 收集字符
        chars = [s[index] for index in indices]

        # 排序字符
        chars.sort()

        # 将排序后的字符放回对应位置
        for i, index in enumerate(indices):
            result[index] = chars[i]

    return ''.join(result)


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    s1 = "dcab"
    pairs1 = [[0, 3], [1, 2]]
    print("测试用例1结果:", smallest_string_with_swaps(s1, pairs1))  # 预期输出: "bacd"

    # 测试用例2
    s2 = "dcab"
    pairs2 = [[0, 3], [1, 2], [0, 2]]
    print("测试用例2结果:", smallest_string_with_swaps(s2, pairs2))  # 预期输出: "abcd"

    # 测试用例3
    s3 = "cba"
    pairs3 = [[0, 1], [1, 2]]
    print("测试用例3结果:", smallest_string_with_swaps(s3, pairs3))  # 预期输出: "abc"

    # 测试用例4：空字符串
    s4 = ""
    pairs4 = []
    print("测试用例4结果:", smallest_string_with_swaps(s4, pairs4))  # 预期输出: ""

    # 测试用例5：单字符
    s5 = "a"
    pairs5 = []
    print("测试用例5结果:", smallest_string_with_swaps(s5, pairs5))  # 预期输出: "a"