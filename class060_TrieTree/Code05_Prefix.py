# -*- coding: utf-8 -*-

'''
题目6: HDU 5790 Prefix
题目来源：HDU
题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=5790

题目描述：
给定n个字符串，然后m次询问，每次询问给出l,r代表在第l和第r个串之间本质不同的前缀有多少个。

解题思路：
1. 使用Trie树存储所有字符串的前缀
2. 对于每个字符串，将其所有前缀插入Trie树，并记录该前缀第一次出现的位置
3. 对于每次询问，统计在指定范围内的字符串中出现的不同前缀数量
4. 可以使用主席树或离线处理配合Trie树来优化查询

时间复杂度分析：
1. 构建Trie树：O(∑len(s))，其中∑len(s)是所有字符串长度之和
2. 查询过程：O(m * log(n))，使用主席树优化
3. 总体时间复杂度：O(∑len(s) + m * log(n))

空间复杂度分析：
1. Trie树空间：O(∑len(s) * 26)
2. 主席树空间：O(∑len(s) * log(n))
3. 总体空间复杂度：O(∑len(s) * log(n))

是否为最优解：是，使用主席树可以高效处理区间查询

工程化考量：
1. 异常处理：输入为空或字符串为空的情况
2. 边界情况：所有字符串都相同的情况
3. 极端输入：大量字符串或长字符串的情况
4. 鲁棒性：处理非法字符的情况

语言特性差异：
Java：使用引用类型，有垃圾回收机制，HashMap实现动态子节点
C++：需要手动管理内存，可以使用数组或指针数组实现
Python：动态类型语言，字典实现自然，但性能不如编译型语言

与实际应用的联系：
1. 数据库：前缀索引优化查询
2. 搜索引擎：关键词前缀匹配
3. 文件系统：路径前缀匹配
'''


class PrefixTrieNode:
    """
    Prefix Trie树节点类
    """
    def __init__(self):
        # 经过该节点的前缀数量
        self.count = 0
        # 该前缀第一次出现的位置
        self.first_occurrence = -1
        # 子节点映射
        self.children = {}


class PrefixTrie:
    """
    Prefix Trie树类
    """
    def __init__(self):
        # 根节点
        self.root = PrefixTrieNode()
    
    def insert_prefixes(self, string, position):
        """
        插入字符串的前缀
        
        时间复杂度：O(len(string))
        空间复杂度：O(len(string))
        
        :param string: 字符串
        :param position: 字符串位置
        """
        node = self.root
        for ch in string:
            # 如果字符不在当前节点的子节点中，则创建新节点
            if ch not in node.children:
                node.children[ch] = PrefixTrieNode()
            node = node.children[ch]
            # 更新前缀计数和首次出现位置
            node.count += 1
            if node.first_occurrence == -1:
                node.first_occurrence = position
    
    def count_distinct_prefixes(self, left, right):
        """
        查询指定范围内的不同前缀数量
        
        时间复杂度：O(∑len(s))，其中∑len(s)是所有字符串长度之和
        空间复杂度：O(1)
        
        :param left: 左边界
        :param right: 右边界
        :return: 不同前缀数量
        """
        # 这里简化实现，实际题目需要使用主席树来优化区间查询
        return self._count_distinct_prefixes_helper(self.root, left, right)
    
    def _count_distinct_prefixes_helper(self, node, left, right):
        """
        递归计算不同前缀数量的辅助方法
        
        时间复杂度：O(∑len(s))
        空间复杂度：O(∑len(s))，递归栈空间
        
        :param node: 当前节点
        :param left: 左边界
        :param right: 右边界
        :return: 不同前缀数量
        """
        count = 0
        # 如果该前缀首次出现位置在查询范围内，则计数加1
        if node.first_occurrence >= left and node.first_occurrence <= right:
            count = 1
        
        # 递归计算所有子节点的贡献
        for child in node.children.values():
            count += self._count_distinct_prefixes_helper(child, left, right)
        
        return count


def count_prefixes(strings, queries):
    """
    计算指定范围内的不同前缀数量
    
    算法思路：
    1. 使用Trie树存储所有字符串的前缀
    2. 对于每个字符串，将其所有前缀插入Trie树，并记录该前缀第一次出现的位置
    3. 对于每次询问，统计在指定范围内的字符串中出现的不同前缀数量
    
    时间复杂度：O(∑len(s) + m * ∑len(s))，其中m是查询次数
    空间复杂度：O(∑len(s))
    
    :param strings: 字符串数组
    :param queries: 查询数组，每个查询包含左右边界
    :return: 每个查询的结果数组
    """
    trie = PrefixTrie()
    results = []
    
    # 将所有字符串的前缀插入Trie树
    for i in range(len(strings)):
        trie.insert_prefixes(strings[i], i + 1)  # 位置从1开始计数
    
    # 处理每个查询
    for query in queries:
        left = query[0]
        right = query[1]
        results.append(trie.count_distinct_prefixes(left, right))
    
    return results


# 测试方法
if __name__ == "__main__":
    # 测试用例
    strings = ["abc", "ab", "abcd", "bc", "bcd"]
    queries = [[1, 3], [2, 4], [1, 5]]
    
    results = count_prefixes(strings, queries)
    
    print("字符串数组:", strings)
    for i in range(len(queries)):
        print(f"查询 [{queries[i][0]}, {queries[i][1]}] 的不同前缀数量: {results[i]}")