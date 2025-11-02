#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
APIO2012 派遣

题目描述：
在一个忍者的帮派里，一些忍者们被选中派遣给顾客，然后依据自己的工作获取报偿。
在这个帮派里，有一名忍者被称之为Master。除了Master以外，每名忍者都有且仅有一个上级。
为保密，同时增强忍者们的领导力，所有与他们工作相关的指令总是由上级发送给他的直接下属，
而不允许通过其他的方式发送。

现在你要招募一批忍者，并把它们派遣给顾客。你需要为每个被派遣的忍者支付一定的薪水，
同时使得支付的薪水总额不超过你的预算。另外，为了发送指令，你需要选择一名忍者作为管理者，
要求这个管理者可以向所有被派遣的忍者发送指令，在发送指令时，任何忍者（不管是否被派遣）
都可以作为消息的传递人。管理者自己可以被派遣，也可以不被派遣。当然，如果管理者没有被派遣，
你就不需要支付管理者的薪水。

你的目标是在预算内使顾客的满意度最大。这里定义顾客的满意度为派遣的忍者总数乘以管理者的领导力水平，
其中每个忍者的领导力水平也是一定的。

写一个程序，给定每一个忍者i的上级Bi，薪水Ci，领导力Li，以及支付给忍者们的薪水总预算M，
输出在预算内满足上述要求时顾客满意度的最大值。

解题思路：
这是一道经典的树形DP+左偏树优化的题目。
1. 建立树形结构，以Master为根节点
2. 从叶子节点向上进行DFS，对于每个节点维护一个大根堆（左偏树）
3. 堆中存储以该节点为根的子树中所有忍者的薪水
4. 当堆中薪水总和超过预算M时，不断弹出薪水最大的忍者，直到总和不超过M
5. 计算以当前节点为管理者时的满意度：忍者数量 * 领导力
6. 向上传递时，将当前节点的左偏树与其所有子节点的左偏树合并

时间复杂度分析：
- 树形DFS: O(N)
- 左偏树合并: O(N log N)
- 左偏树删除: O(N log N)
- 总体复杂度: O(N log N)

空间复杂度分析:
- 树形结构存储: O(N)
- 左偏树节点存储: O(N)
- 总体空间复杂度: O(N)
"""


class Node:
    """
    左偏树节点类
    """
    def __init__(self, val, index):
        self.val = val          # 节点权值（忍者薪水）
        self.dist = 0           # 节点距离（到最近外节点的距离）
        self.index = index      # 节点索引
        self.left = None        # 左子节点
        self.right = None       # 右子节点


class LeftistTree:
    """
    左偏树类
    """
    def __init__(self):
        self.nodes = {}         # 节点字典
        self.node_count = 0     # 节点计数器

    def init_node(self, val):
        """
        初始化节点
        :param val: 节点权值
        :return: 节点索引
        """
        self.node_count += 1
        self.nodes[self.node_count] = Node(val, self.node_count)
        return self.node_count

    def merge(self, a, b):
        """
        合并两个左偏树
        :param a: 第一棵左偏树根节点索引
        :param b: 第二棵左偏树根节点索引
        :return: 合并后左偏树根节点索引
        """
        # 如果其中一个为空，返回另一个
        if not a:
            return b
        if not b:
            return a

        # 确保a节点权值 >= b节点权值（大根堆）
        if self.nodes[a].val < self.nodes[b].val:
            a, b = b, a

        # 递归合并右子树和b树
        right_index = self.nodes[a].right.index if self.nodes[a].right else 0
        merged_index = self.merge(right_index, b)

        if merged_index:
            self.nodes[a].right = self.nodes[merged_index]
        else:
            self.nodes[a].right = None

        # 维护左偏性质：左子树距离 >= 右子树距离
        left_dist = self.nodes[a].left.dist if self.nodes[a].left else -1
        right_dist = self.nodes[a].right.dist if self.nodes[a].right else -1

        if left_dist < right_dist:
            # 交换左右子树
            self.nodes[a].left, self.nodes[a].right = self.nodes[a].right, self.nodes[a].left

        # 更新距离
        new_right_dist = self.nodes[a].right.dist if self.nodes[a].right else -1
        self.nodes[a].dist = new_right_dist + 1

        return a

    def pop(self, root):
        """
        删除左偏树根节点
        :param root: 根节点索引
        :return: 新的根节点索引
        """
        if not root:
            return 0

        left_index = self.nodes[root].left.index if self.nodes[root].left else 0
        right_index = self.nodes[root].right.index if self.nodes[root].right else 0

        return self.merge(left_index, right_index)


def main():
    """
    主函数
    """
    import sys

    # 读取所有输入
    lines = []
    for line in sys.stdin:
        line = line.strip()
        if line:
            lines.append(line)

    i = 0
    # 读取输入
    n, budget = map(int, lines[i].split())
    i += 1

    # 初始化数据结构
    tree = LeftistTree()
    boss = [0] * (n + 1)        # 上级忍者
    salary = [0] * (n + 1)      # 薪水
    leadership = [0] * (n + 1)  # 领导力
    children = [[] for _ in range(n + 1)]  # 子节点列表
    roots = [0] * (n + 1)       # 每个节点对应的左偏树根
    sum_salary = [0] * (n + 1)  # 每个左偏树的薪水总和
    size = [0] * (n + 1)        # 每个左偏树的节点数量
    max_satisfaction = 0        # 最大满意度

    master = 0  # Master节点编号

    # 读取每个忍者的信息
    for j in range(1, n + 1):
        b, s, l = map(int, lines[i].split())
        i += 1
        boss[j] = b
        salary[j] = s
        leadership[j] = l

        # Master节点的上级为0
        if b == 0:
            master = j
        else:
            # 建立树形结构
            children[b].append(j)

    def dfs(u):
        """
        DFS遍历树形结构
        :param u: 当前节点
        """
        nonlocal max_satisfaction
        
        # 初始化当前节点的左偏树
        roots[u] = tree.init_node(salary[u])
        sum_salary[u] = salary[u]
        size[u] = 1

        # 遍历所有子节点
        for v in children[u]:
            dfs(v)

            # 合并子节点的左偏树到当前节点
            roots[u] = tree.merge(roots[u], roots[v])
            sum_salary[u] += sum_salary[v]
            size[u] += size[v]

        # 当薪水总和超过预算时，不断弹出薪水最大的忍者
        while sum_salary[u] > budget:
            sum_salary[u] -= tree.nodes[roots[u]].val
            size[u] -= 1
            roots[u] = tree.pop(roots[u])

        # 计算以当前节点为管理者时的满意度
        satisfaction = size[u] * leadership[u]
        max_satisfaction = max(max_satisfaction, satisfaction)

    # 从Master节点开始DFS
    dfs(master)

    # 输出最大满意度
    print(max_satisfaction)


if __name__ == "__main__":
    main()
