"""
带权并查集解决最小字符串交换问题 (Python版本)

问题分析：
通过给定的索引对，将字符串中可以交换的字符分组，每组内字符可以任意交换位置，
求字典序最小的字符串。

核心思想：
1. 使用并查集将可以交换的索引分组
2. 对每组内的字符按字典序排序
3. 将排序后的字符按索引顺序重新组合成字符串

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- 总体: O(n * log(n) + m * α(n))，其中n是字符串长度，m是索引对数量

空间复杂度: O(n) 用于存储father数组和每组的字符列表

应用场景：
- 字符串重排优化
- 连通分量排序
- 图的连通性应用

题目来源：LeetCode 1202
题目链接：https://leetcode.com/problems/smallest-string-with-swaps/
题目名称：Smallest String With Swaps
"""

class UnionFind:
    def __init__(self, n):
        """
        初始化并查集
        :param n: 字符串长度
        """
        self.father = list(range(n))  # father[i] 表示索引i的父节点
        
    def find(self, i):
        """
        查找索引i的根节点，并进行路径压缩
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的索引
        :return: 索引i所在集合的根节点
        """
        # 如果不是根节点
        if i != self.father[i]:
            # 递归查找根节点，同时进行路径压缩
            self.father[i] = self.find(self.father[i])
        return self.father[i]
    
    def union(self, i, j):
        """
        合并两个索引所在的集合
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 索引i
        :param j: 索引j
        """
        # 查找两个索引的根节点
        fi = self.find(i)
        fj = self.find(j)
        # 如果不在同一集合中
        if fi != fj:
            # 合并两个集合
            self.father[fi] = fj

def smallestStringWithSwaps(s, pairs):
    """
    通过索引对交换得到字典序最小的字符串
    
    :param s: 输入字符串
    :param pairs: 索引对数组
    :return: 字典序最小的字符串
    """
    n = len(s)
    
    # 初始化并查集
    uf = UnionFind(n)
    
    # 处理所有索引对，建立连通关系
    for pair in pairs:
        uf.union(pair[0], pair[1])
    
    # 将同一连通分量的字符分组
    from collections import defaultdict
    groups = defaultdict(list)
    for i in range(n):
        root = uf.find(i)
        groups[root].append(s[i])
    
    # 对每组内的字符按字典序排序（降序，为了后面能从尾部取最小的）
    for group in groups.values():
        group.sort(reverse=True)
    
    # 构造结果字符串
    result = []
    for i in range(n):
        root = uf.find(i)
        group = groups[root]
        # 取出当前组中字典序最小的字符
        result.append(group.pop())
    
    return ''.join(result)

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    s1 = "dcab"
    pairs1 = [[0, 3], [1, 2]]
    print(smallestStringWithSwaps(s1, pairs1))  # bacd
    
    # 测试用例2
    s2 = "dcab"
    pairs2 = [[0, 3], [1, 2], [0, 2]]
    print(smallestStringWithSwaps(s2, pairs2))  # abcd
    
    # 测试用例3
    s3 = "cba"
    pairs3 = [[0, 1], [1, 2]]
    print(smallestStringWithSwaps(s3, pairs3))  # abc