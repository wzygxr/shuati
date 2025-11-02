#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
账户合并 (Python版本)
给定一个列表 accounts，每个元素 accounts[i] 是一个字符串列表，其中第一个元素是名称 (name)，
其余元素是邮箱地址表示该账户的邮箱地址。
现在我们想合并这些账户。如果两个账户都有一些共同的邮箱地址，则两个账户必定属于同一个人。
请注意，即使两个账户具有相同的名称，它们也可能属于不同的人，因为人们可能具有相同的名称。
一个人最初可以拥有任意数量的账户，但其所有账户都具有相同的名称。
合并账户后，按以下格式返回账户：每个账户的第一个元素是名称，其余元素是按字符 ASCII 顺序排列的邮箱地址。
账户本身可以以任意顺序返回。

示例 1:
输入: 
accounts = [
  ["John", "johnsmith@mail.com", "john00@mail.com"],
  ["John", "johnnybravo@mail.com"],
  ["John", "johnsmith@mail.com", "john_newyork@mail.com"],
  ["Mary", "mary@mail.com"]
]
输出: 
[
  ["John", 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com'],
  ["John", "johnnybravo@mail.com"],
  ["Mary", "mary@mail.com"]
]

解释: 
第一个和第三个 John 是同一个人，因为他们有共同的邮箱地址 "johnsmith@mail.com"。
第二个 John 和 Mary 是不同的人，因为他们的邮箱地址没有被其他帐户使用。
可以以任意顺序返回这些列表，例如答案 
[['Mary', 'mary@mail.com'], ['John', 'johnnybravo@mail.com'], 
 ['John', 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com']]
 也是正确的。

约束条件：
1 <= accounts.length <= 1000
2 <= accounts[i].length <= 10
1 <= accounts[i][j].length <= 30
accounts[i][0] 由英文字母组成
accounts[i][j] (for j > 0) 是有效的邮箱地址

测试链接: https://leetcode.cn/problems/accounts-merge/
相关平台: LeetCode 721, LintCode 1297, 牛客网
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


def accounts_merge(accounts):
    """
    使用并查集解决账户合并问题

    解题思路：
    1. 将每个账户看作一个节点，使用并查集来维护账户之间的连接关系
    2. 对于每个邮箱地址，记录它第一次出现的账户索引
    3. 如果一个邮箱地址在多个账户中出现，则将这些账户合并到同一个集合中
    4. 最后，将同一个集合中的所有账户的邮箱地址合并，并按ASCII顺序排序

    时间复杂度：O(N * M * α(N))，其中N是账户数量，M是每个账户的平均邮箱数量，α是阿克曼函数的反函数
    空间复杂度：O(N * M)

    :param accounts: 账户列表
    :return: 合并后的账户列表
    """
    if not accounts:
        return []

    n = len(accounts)
    union_find = UnionFind(n)

    # 记录每个邮箱第一次出现的账户索引
    email_to_index = {}

    # 遍历所有账户
    for i in range(n):
        account = accounts[i]
        # 从索引1开始，因为索引0是账户名称
        for j in range(1, len(account)):
            email = account[j]
            if email in email_to_index:
                # 如果邮箱已经出现过，则合并当前账户和之前出现该邮箱的账户
                prev_index = email_to_index[email]
                union_find.union(i, prev_index)
            else:
                # 记录邮箱第一次出现的账户索引
                email_to_index[email] = i

    # 将同一个集合中的所有邮箱地址合并到一起
    index_to_emails = {}
    for i in range(n):
        root = union_find.find(i)
        if root not in index_to_emails:
            index_to_emails[root] = set()
        account = accounts[i]
        for j in range(1, len(account)):
            index_to_emails[root].add(account[j])

    # 构造结果
    result = []
    for index, emails in index_to_emails.items():
        # 添加账户名称
        merged_account = [accounts[index][0]]
        # 添加排序后的邮箱地址
        merged_account.extend(sorted(emails))
        result.append(merged_account)

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

    print("测试用例1结果:")
    result1 = accounts_merge(accounts1)
    for account in result1:
        print(account)

    # 测试用例2
    accounts2 = [
        ["Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"],
        ["Kevin","Kevin3@m.co","Kevin5@m.co","Kevin0@m.co"],
        ["Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"],
        ["Hanzo","Hanzo3@m.co","Hanzo1@m.co","Hanzo0@m.co"],
        ["Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"]
    ]

    print("\n测试用例2结果:")
    result2 = accounts_merge(accounts2)
    for account in result2:
        print(account)