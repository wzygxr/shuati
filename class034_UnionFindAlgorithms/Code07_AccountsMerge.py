#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
账户合并
给定一个列表 accounts，每个元素 accounts[i] 是一个字符串列表，
其中第一个元素 accounts[i][0] 是 名称 (name)，其余元素是 emails 表示该账户的邮箱地址。
现在我们想合并这些账户。如果两个账户都有一些共同的邮箱地址，则两个账户必定属于同一个人。
请注意，即使两个账户具有相同的名称，它们也可能属于不同的人，因为人们可能具有相同的名称。
一个人最初可以拥有任意数量的账户，但其所有账户都具有相同的名称。
合并账户后，按以下格式返回账户：每个账户的第一个元素是名称，其余元素是按字符 ASCII 顺序排列的邮箱地址。
账户本身可以以任意顺序返回。

示例 1:
输入：
[["John", "johnsmith@mail.com", "john00@mail.com"],
 ["John", "johnnybravo@mail.com"],
 ["John", "johnsmith@mail.com", "john_newyork@mail.com"],
 ["Mary", "mary@mail.com"]]
输出：
[["John", 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com'],
 ["John", "johnnybravo@mail.com"],
 ["Mary", "mary@mail.com"]]

示例 2:
输入：
[["Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"],
 ["Kevin","Kevin3@m.co","Kevin5@m.co","Kevin0@m.co"],
 ["Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"],
 ["Hanzo","Hanzo3@m.co","Hanzo1@m.co","Hanzo0@m.co"],
 ["Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"]]
输出：
[["Ethan","Ethan0@m.co","Ethan4@m.co","Ethan5@m.co"],
 ["Gabe","Gabe0@m.co","Gabe1@m.co","Gabe3@m.co"],
 ["Hanzo","Hanzo0@m.co","Hanzo1@m.co","Hanzo3@m.co"],
 ["Kevin","Kevin0@m.co","Kevin3@m.co","Kevin5@m.co"],
 ["Fern","Fern0@m.co","Fern1@m.co","Fern5@m.co"]]

测试链接: https://leetcode.cn/problems/accounts-merge/

解题思路:
使用并查集将属于同一个人的账户连接起来。首先建立邮箱到账户索引的映射，
如果一个邮箱出现在多个账户中，就将这些账户连接起来。然后将同一连通分量中的所有邮箱合并。

时间复杂度: O(n*m*log(m))，其中n是账户数量，m是每个账户平均邮箱数量
空间复杂度: O(n*m)
是否为最优解: 是

工程化考量:
1. 异常处理：检查输入是否为空
2. 可配置性：可以扩展支持其他合并规则
3. 线程安全：当前实现不是线程安全的

与机器学习等领域的联系:
1. 数据清洗：合并重复的用户数据
2. 社交网络分析：识别同一用户的不同账户

语言特性差异:
Java: 对象引用和垃圾回收，TreeSet保持排序
C++: 指针操作和手动内存管理，set保持排序
Python: 动态类型和自动内存管理，sorted函数排序

极端输入场景:
1. 空账户列表
2. 单个账户
3. 所有账户都属于同一个人
4. 没有重复邮箱的账户

性能优化:
1. 路径压缩优化find操作
2. 按秩合并优化union操作
3. 使用哈希表快速查找邮箱
4. 使用sorted函数排序邮箱
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
        self.parent = list(range(n))  # parent[i]表示节点i的父节点
        self.rank = [1] * n          # rank[i]表示以i为根的树的高度上界

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


def accounts_merge(accounts):
    """
    合并账户
    :param accounts: 账户列表
    :return: 合并后的账户列表
    """
    # 边界条件检查
    if not accounts:
        return []

    n = len(accounts)

    # 创建并查集
    uf = UnionFind(n)

    # 建立邮箱到账户索引的映射
    email_to_index = {}

    # 遍历所有账户
    for i in range(n):
        account = accounts[i]

        # 从第二个元素开始是邮箱地址
        for j in range(1, len(account)):
            email = account[j]

            # 如果邮箱已经出现过，将当前账户与之前出现该邮箱的账户连接
            if email in email_to_index:
                uf.union(i, email_to_index[email])
            else:
                # 记录邮箱对应的账户索引
                email_to_index[email] = i

    # 将同一连通分量中的所有邮箱合并到一起
    index_to_emails = {}

    for i in range(n):
        root = uf.find(i)

        if root not in index_to_emails:
            index_to_emails[root] = set()

        account = accounts[i]
        for j in range(1, len(account)):
            index_to_emails[root].add(account[j])

    # 构造结果
    result = []

    for index, emails in index_to_emails.items():
        # 对邮箱进行排序
        sorted_emails = sorted(emails)
        # 添加名称
        account = [accounts[index][0]] + sorted_emails
        result.append(account)

    return result


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    accounts1 = [
        ["John", "johnsmith@mail.com", "john00@mail.com"],
        ["John", "johnnybravo@mail.com"],
        ["John", "johnsmith@mail.com", "john_newyork@mail.com"],
        ["Mary", "mary@mail.com"]
    ]

    result1 = accounts_merge(accounts1)
    print("测试用例1结果:")
    for account in result1:
        print(account)

    # 测试用例2
    accounts2 = [
        ["Gabe", "Gabe0@m.co", "Gabe3@m.co", "Gabe1@m.co"],
        ["Kevin", "Kevin3@m.co", "Kevin5@m.co", "Kevin0@m.co"],
        ["Ethan", "Ethan5@m.co", "Ethan4@m.co", "Ethan0@m.co"],
        ["Hanzo", "Hanzo3@m.co", "Hanzo1@m.co", "Hanzo0@m.co"],
        ["Fern", "Fern5@m.co", "Fern1@m.co", "Fern0@m.co"]
    ]

    result2 = accounts_merge(accounts2)
    print("\n测试用例2结果:")
    for account in result2:
        print(account)