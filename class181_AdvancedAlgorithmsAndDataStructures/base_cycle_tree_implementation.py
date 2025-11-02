#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
基环树 (Base Cycle Tree) Python 实现

基环树是一种特殊的图结构，它由一个环和若干棵以环上节点为根的树组成。
每个节点恰好有一条入边，因此整个图由一个或多个环组成，每个环上可能挂着一些树。

应用场景：
1. 数据结构：函数式数据结构、持久化数据结构
2. 图论算法：环检测、强连通分量
3. 数学：置换群、循环结构

算法思路：
1. 检测图中的环
2. 对环上的每个节点，构建以其为根的子树
3. 分析环的性质和子树的性质

时间复杂度：O(V + E)
空间复杂度：O(V)
"""

class BaseCycleTree:
    def __init__(self, n, parent):
        """
        初始化基环树
        
        Args:
            n: 节点数
            parent: 每个节点的父节点数组
        """
        self.n = n
        self.parent = parent[:]
        self.visited = [False] * n
        self.children = [[] for _ in range(n)]
        self.cycle = []
        self.inCycle = [False] * n
        
        # 构建子节点关系
        for i in range(n):
            if parent[i] != -1:
                self.children[parent[i]].append(i)
        
        # 检测环
        self.findCycle()
    
    def findCycle(self):
        """检测环"""
        self.cycle = []
        self.visited = [False] * self.n
        self.inCycle = [False] * self.n
        
        # 找到环的起始节点
        start = -1
        for i in range(self.n):
            if not self.visited[i]:
                path = []
                if self.dfs(i, path):
                    start = i
                    break
        
        if start == -1:
            return  # 没有环
        
        # 重新遍历以找到完整的环
        self.visited = [False] * self.n
        path = []
        self.findCyclePath(start, path)
        
        # 标记环上的节点
        self.cycle = path
        for node in self.cycle:
            self.inCycle[node] = True
    
    def dfs(self, node, path):
        """
        DFS检测环
        
        Args:
            node: 当前节点
            path: 当前路径
            
        Returns:
            是否找到环
        """
        if self.visited[node]:
            # 找到环的起点
            path.append(node)
            return True
        
        self.visited[node] = True
        path.append(node)
        
        if self.parent[node] != -1 and self.dfs(self.parent[node], path):
            return True
        
        path.pop()
        return False
    
    def findCyclePath(self, node, path):
        """
        找到环的路径
        
        Args:
            node: 当前节点
            path: 当前路径
            
        Returns:
            是否找到环
        """
        if self.visited[node]:
            # 找到环的起点，截取环的部分
            try:
                start_index = path.index(node)
                cycle_path = path[start_index:]
                path[:] = cycle_path
                return True
            except ValueError:
                return False
        
        self.visited[node] = True
        path.append(node)
        
        if self.parent[node] != -1 and self.findCyclePath(self.parent[node], path):
            return True
        
        path.pop()
        return False
    
    def get_cycle(self):
        """
        获取环上的节点
        
        Returns:
            环上的节点列表
        """
        return self.cycle[:]
    
    def is_in_cycle(self, node):
        """
        检查节点是否在环上
        
        Args:
            node: 节点
            
        Returns:
            节点是否在环上
        """
        return self.inCycle[node]
    
    def get_subtree_size(self, root):
        """
        获取以指定节点为根的子树大小
        
        Args:
            root: 根节点
            
        Returns:
            子树大小
        """
        return self._get_subtree_size_helper(root)
    
    def _get_subtree_size_helper(self, node):
        """
        递归计算子树大小
        
        Args:
            node: 当前节点
            
        Returns:
            以当前节点为根的子树大小
        """
        size = 1  # 包括节点本身
        for child in self.children[node]:
            if not self.inCycle[child]:  # 只计算不在环上的子节点
                size += self._get_subtree_size_helper(child)
        return size
    
    def get_cycle_length(self):
        """
        获取环的长度
        
        Returns:
            环的长度
        """
        return len(self.cycle)
    
    def get_all_subtree_sizes(self):
        """
        获取所有子树的大小
        
        Returns:
            包含节点和子树大小的字典
        """
        sizes = {}
        for node in self.cycle:
            sizes[node] = self.get_subtree_size(node)
        return sizes
    
    def print_structure(self):
        """打印基环树结构"""
        print("Base Cycle Tree Structure:")
        print("Cycle:", self.cycle)
        print("Cycle Length:", self.get_cycle_length())
        
        print("Subtree Sizes:")
        sizes = self.get_all_subtree_sizes()
        for node, size in sizes.items():
            print(f"  Node {node}: {size}")
        
        print("Children Relationships:")
        for i in range(self.n):
            if self.children[i]:
                print(f"  Node {i} -> {self.children[i]}")


# 测试方法
def main():
    # 测试用例1：简单的基环树
    print("测试用例1: 简单基环树")
    # 节点: 0 1 2 3 4
    # 父节点: 1 2 0 4 1
    # 结构: 0->1->2->0 (环), 4->1, 3->4
    parent1 = [1, 2, 0, 4, 1]
    bct1 = BaseCycleTree(5, parent1)
    bct1.print_structure()
    print()
    
    # 测试用例2：单个环
    print("测试用例2: 单个环")
    # 节点: 0 1 2
    # 父节点: 1 2 0
    # 结构: 0->1->2->0 (环)
    parent2 = [1, 2, 0]
    bct2 = BaseCycleTree(3, parent2)
    bct2.print_structure()
    print()
    
    # 测试用例3：复杂基环树
    print("测试用例3: 复杂基环树")
    # 节点: 0 1 2 3 4 5 6
    # 父节点: 1 2 0 5 3 2 4
    # 结构: 0->1->2->0 (环), 3->5->2, 4->3, 6->4
    parent3 = [1, 2, 0, 5, 3, 2, 4]
    bct3 = BaseCycleTree(7, parent3)
    bct3.print_structure()
    print()


if __name__ == "__main__":
    main()