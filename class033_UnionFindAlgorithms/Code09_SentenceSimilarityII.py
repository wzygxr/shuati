"""
句子相似性II
给定两个句子 sentence1 和 sentence2 分别表示为一个字符串数组，并给定一个字符串对 similarPairs，
其中 similarPairs[i] = [xi, yi] 表示两个单词 xi 和 yi 是相似的。
如果 sentence1 和 sentence2 相似则返回 true，如果不相似则返回 false。

两个句子是相似的，如果：
1. 它们具有相同的长度（即相同的字数）
2. sentence1[i] 和 sentence2[i] 是相似的

注意，一个词总是与它自己相似。也注意相似关系是可传递的。
例如，如果单词 a 和 b 是相似的，单词 b 和 c 也是相似的，那么 a 和 c 也是相似的。

示例 1:
输入: 
sentence1 = ["great","acting","skills"]
sentence2 = ["fine","drama","talent"]
similarPairs = [["great","good"],["fine","good"],["drama","acting"],["skills","talent"]]
输出: true
解释: 这两个句子长度相同，每个单词都相似。

示例 2:
输入: 
sentence1 = ["I","love","leetcode"]
sentence2 = ["I","love","onepiece"]
similarPairs = [["manga","onepiece"],["platform","anime"],["leetcode","platform"],["anime","manga"]]
输出: true
解释: "leetcode" --> "platform" --> "anime" --> "manga" --> "onepiece"
因为"leetcode"和"onepiece"相似，而且前两个单词相同，所以这两个句子是相似的。

示例 3:
输入: 
sentence1 = ["I","love","leetcode"]
sentence2 = ["I","love","onepiece"]
similarPairs = [["manga","hunterXhunter"],["platform","anime"],["leetcode","platform"],["anime","manga"]]
输出: false
解释: "leetcode"和"onepiece"不相似。

约束条件：
1 <= sentence1.length, sentence2.length <= 1000
1 <= sentence1[i].length, sentence2[i].length <= 20
sentence1[i] 和 sentence2[i] 只包含英文字母
0 <= similarPairs.length <= 2000
similarPairs[i].length == 2
1 <= xi.length, yi.length <= 20
xi 和 yi 只包含英文字母

测试链接: https://leetcode.cn/problems/sentence-similarity-ii/
相关平台: LeetCode 737
"""


class UnionFind:
    """
    并查集数据结构实现
    包含路径压缩优化
    """

    def __init__(self):
        """
        初始化并查集
        """
        self.parent = {}  # parent[word]表示单词word的父节点

    def find(self, x):
        """
        查找单词的根节点（代表元素）
        使用路径压缩优化
        :param x: 要查找的单词
        :return: 单词x所在集合的根节点
        """
        # 如果单词不在并查集中，将其加入并设置为自己的父节点
        if x not in self.parent:
            self.parent[x] = x
            return x

        # 路径压缩：将路径上的所有节点直接连接到根节点
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x, y):
        """
        合并两个单词所在的集合
        :param x: 第一个单词
        :param y: 第二个单词
        """
        root_x = self.find(x)
        root_y = self.find(y)
        # 如果已经在同一个集合中，则无需合并
        if root_x != root_y:
            self.parent[root_x] = root_y

    def is_connected(self, x, y):
        """
        判断两个单词是否在同一个集合中
        :param x: 第一个单词
        :param y: 第二个单词
        :return: 如果在同一个集合中返回True，否则返回False
        """
        return self.find(x) == self.find(y)


def are_sentences_similar_two(sentence1, sentence2, similar_pairs):
    """
    使用并查集解决句子相似性问题

    解题思路：
    1. 首先检查两个句子长度是否相等，不相等直接返回False
    2. 使用并查集来处理相似单词的传递性关系
    3. 将所有相似单词对加入并查集
    4. 对于句子中的每一对单词，检查它们是否在同一个集合中

    时间复杂度：O(N + P * α(P))，其中N是句子长度，P是相似单词对数量，α是阿克曼函数的反函数
    空间复杂度：O(P)
    是否为最优解：是

    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 可配置性：可以扩展支持其他相似性规则
    3. 线程安全：当前实现不是线程安全的

    与机器学习等领域的联系：
    1. 自然语言处理：句子相似性判断是NLP中的基础任务
    2. 语义分析：通过传递性关系构建语义网络

    语言特性差异:
    Java: 对象引用和垃圾回收
    C++: 指针操作和手动内存管理
    Python: 动态类型和自动内存管理

    极端输入场景:
    1. 空句子
    2. 单词相同的句子
    3. 没有相似单词对的句子
    4. 大量相似单词对的句子

    性能优化:
    1. 路径压缩优化find操作
    2. 使用哈希表快速查找单词
    3. 提前终止优化

    :param sentence1: 第一个句子
    :param sentence2: 第二个句子
    :param similar_pairs: 相似单词对
    :return: 如果两个句子相似返回True，否则返回False
    """
    # 边界条件检查
    if not sentence1 and not sentence2:
        return True

    if not sentence1 or not sentence2:
        return False

    # 长度不同直接返回False
    if len(sentence1) != len(sentence2):
        return False

    # 创建并查集
    union_find = UnionFind()

    # 将所有相似单词对加入并查集
    for pair in similar_pairs:
        union_find.union(pair[0], pair[1])

    # 检查每一对单词是否相似
    for i in range(len(sentence1)):
        # 单词相同或者在同一个集合中则相似
        if sentence1[i] != sentence2[i] and \
                not union_find.is_connected(sentence1[i], sentence2[i]):
            return False

    return True


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    sentence1_1 = ["great", "acting", "skills"]
    sentence2_1 = ["fine", "drama", "talent"]
    similar_pairs1 = [["great", "good"], ["fine", "good"], ["drama", "acting"], ["skills", "talent"]]

    print("测试用例1结果:", are_sentences_similar_two(sentence1_1, sentence2_1, similar_pairs1))  # 预期输出: True

    # 测试用例2
    sentence1_2 = ["I", "love", "leetcode"]
    sentence2_2 = ["I", "love", "onepiece"]
    similar_pairs2 = [["manga", "onepiece"], ["platform", "anime"], ["leetcode", "platform"], ["anime", "manga"]]

    print("测试用例2结果:", are_sentences_similar_two(sentence1_2, sentence2_2, similar_pairs2))  # 预期输出: True

    # 测试用例3
    sentence1_3 = ["I", "love", "leetcode"]
    sentence2_3 = ["I", "love", "onepiece"]
    similar_pairs3 = [["manga", "hunterXhunter"], ["platform", "anime"], ["leetcode", "platform"], ["anime", "manga"]]

    print("测试用例3结果:", are_sentences_similar_two(sentence1_3, sentence2_3, similar_pairs3))  # 预期输出: False

    # 测试用例4：相同句子
    sentence1_4 = ["hello", "world"]
    sentence2_4 = ["hello", "world"]
    similar_pairs4 = []

    print("测试用例4结果:", are_sentences_similar_two(sentence1_4, sentence2_4, similar_pairs4))  # 预期输出: True