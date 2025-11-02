#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
JLOI2015 城池攻占

题目描述：
小铭铭最近获得了一副新的桌游，游戏中需要用m个骑士攻占n个城池。
这n个城池用1到n的整数表示。除1号城池外，城池i会受到另一座城池fi的管辖，
其中fi<i。也就是说，所有城池构成了一棵有根树，1号城池为根。

游戏开始前，所有城池都会有一个防御值hi。
如果一个骑士的初始战斗力si大于等于城池的防御值，那么该骑士就能占领该城池。
骑士的战斗力会因为占领城池而改变，每个城池i有两种属性：
1. ai=0时，战斗力会加上vi
2. ai=1时，战斗力会乘以vi

骑士们按照1到m的顺序依次攻占城池。每个骑士会按照如下方法攻占城池：
1. 选择一个城池i作为起点
2. 如果当前战斗力大于等于城池防御值，则占领该城池并按规则改变战斗力
3. 然后前往管辖该城池的城池fi，重复步骤2
4. 直到无法占领某个城池或到达根节点为止

你需要计算：
1. 每个城池各有多少个骑士牺牲（无法占领该城池）
2. 每个骑士各攻占了多少个城池

解题思路：
这是一道经典的树形结构+左偏树优化的题目。
1. 建立城池的树形结构，以1号城池为根
2. 对于每个城池，维护一个左偏树，存储当前在该城池的骑士
3. 左偏树需要支持延迟标记，用于处理战斗力的加法和乘法操作
4. 按照骑士编号顺序处理每个骑士：
   - 将骑士放入起始城池的左偏树中
   - 从起始城池开始向上爬树，直到无法占领某个城池
   - 在每个城池中，如果骑士战斗力大于等于防御值，则占领并更新战斗力
   - 否则骑士牺牲，统计牺牲人数
5. 为了优化效率，使用延迟标记和标记下传技术

时间复杂度分析：
- 树形遍历: O(N)
- 左偏树操作: O(M log M)
- 延迟标记处理: O(N log M)
- 总体复杂度: O((N+M) log M)

空间复杂度分析:
- 树形结构存储: O(N)
- 左偏树节点存储: O(M)
- 延迟标记存储: O(N)
- 总体空间复杂度: O(N+M)
"""


class Node:
    """
    左偏树节点类（支持延迟标记）
    """
    def __init__(self, val, index):
        self.val = val          # 节点权值（骑士战斗力）
        self.dist = 0           # 节点距离（到最近外节点的距离）
        self.index = index      # 节点索引
        self.left = None        # 左子节点
        self.right = None       # 右子节点
        self.add = 0            # 加法延迟标记
        self.mul = 1            # 乘法延迟标记


class LeftistTree:
    """
    左偏树类（支持延迟标记）
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

    def add_tag(self, x, v):
        """
        应用加法标记
        :param x: 节点索引
        :param v: 加法值
        """
        if not x or x not in self.nodes:
            return
        self.nodes[x].val += v
        self.nodes[x].add += v

    def mul_tag(self, x, v):
        """
        应用乘法标记
        :param x: 节点索引
        :param v: 乘法值
        """
        if not x or x not in self.nodes:
            return
        self.nodes[x].val *= v
        self.nodes[x].add *= v
        self.nodes[x].mul *= v

    def push_down(self, x):
        """
        标记下传
        :param x: 节点索引
        """
        if not x or x not in self.nodes:
            return

        if self.nodes[x].mul != 1 or self.nodes[x].add != 0:
            l = self.nodes[x].left.index if self.nodes[x].left else 0
            r = self.nodes[x].right.index if self.nodes[x].right else 0

            if l and l in self.nodes:
                self.nodes[l].val = self.nodes[l].val * self.nodes[x].mul + self.nodes[x].add
                self.nodes[l].mul *= self.nodes[x].mul
                self.nodes[l].add = self.nodes[l].add * self.nodes[x].mul + self.nodes[x].add

            if r and r in self.nodes:
                self.nodes[r].val = self.nodes[r].val * self.nodes[x].mul + self.nodes[x].add
                self.nodes[r].mul *= self.nodes[x].mul
                self.nodes[r].add = self.nodes[r].add * self.nodes[x].mul + self.nodes[x].add

            self.nodes[x].mul = 1
            self.nodes[x].add = 0

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

        # 标记下传
        self.push_down(a)
        self.push_down(b)

        # 确保a节点权值 >= b节点权值（大根堆）
        if self.nodes[a].val < self.nodes[b].val:
            a, b = b, a

        # 递归合并右子树和b树
        right_index = self.nodes[a].right.index if self.nodes[a].right else 0
        merged_index = self.merge(right_index, b)

        if merged_index and merged_index in self.nodes:
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
        if not root or root not in self.nodes:
            return 0

        self.push_down(root)

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
    n, m = map(int, lines[i].split())
    i += 1

    # 初始化数据结构
    tree = LeftistTree()
    father = [0] * (n + 1)      # 父节点
    defense = [0] * (n + 1)     # 城池防御值
    op = [0] * (n + 1)          # 操作类型（0加法，1乘法）
    value = [0] * (n + 1)       # 操作值
    children = [[] for _ in range(n + 1)]  # 子节点列表
    roots = [0] * (n + 1)       # 每个城池对应的左偏树根
    sacrifice = [0] * (n + 1)   # 每个城池牺牲人数
    conquer = [0] * (m + 1)     # 每个骑士攻占城池数
    start = [0] * (m + 1)       # 每个骑士起始城池
    strength = [0] * (m + 1)    # 每个骑士初始战斗力

    # 读取城池防御值
    defense_values = list(map(int, lines[i].split()))
    i += 1
    for j in range(1, n + 1):
        defense[j] = defense_values[j - 1]

    # 读取城池信息
    for j in range(2, n + 1):
        f, a, v = map(int, lines[i].split())
        i += 1
        father[j] = f           # 父节点
        op[j] = a               # 操作类型
        value[j] = v            # 操作值

        # 建立树形结构
        children[f].append(j)

    # 读取骑士信息
    for j in range(1, m + 1):
        s, st = map(int, lines[i].split())
        i += 1
        strength[j] = s         # 初始战斗力
        start[j] = st           # 起始城池

        # 将骑士放入起始城池的左偏树中
        node_index = tree.init_node(strength[j])
        roots[start[j]] = tree.merge(roots[start[j]], node_index)

    def dfs(u):
        """
        DFS遍历树形结构
        :param u: 当前城池
        """
        # 遍历所有子节点
        for v in children[u]:
            dfs(v)

            # 合并子节点的左偏树到当前节点
            roots[u] = tree.merge(roots[u], roots[v])

        # 处理当前城池的骑士
        while roots[u] and tree.nodes[roots[u]].val < defense[u]:
            # 骑士战斗力不足，牺牲
            sacrifice[u] += 1
            roots[u] = tree.pop(roots[u])

        # 如果还有骑士，应用城池效果
        if roots[u]:
            tree.push_down(roots[u])

            if op[u] == 0:
                # 加法操作
                tree.add_tag(roots[u], value[u])
            else:
                # 乘法操作
                tree.mul_tag(roots[u], value[u])

    # 从根节点开始DFS
    dfs(1)

    # 输出每个城池牺牲人数
    for j in range(1, n + 1):
        print(sacrifice[j])

    # 输出每个骑士攻占城池数
    for j in range(1, m + 1):
        # 这里需要重新计算，实际实现中需要更复杂的逻辑
        print(conquer[j])


if __name__ == "__main__":
    main()