# FHQ-Treap实现Graph and Queries
# Codeforces Problem F. Graph and Queries
# 实现图相关的查询操作
# 测试链接 : https://codeforces.com/contest/1416/problem/F

import sys
import random
from io import StringIO

class GraphAndQueriesFHQTreap:
    def __init__(self, max_n=100001):
        """
        初始化FHQ Treap图查询结构
        
        Args:
            max_n: 最大节点数
        """
        self.MAXN = max_n
        
        # 并查集数组
        self.parent = [0] * (self.MAXN)
        
        # FHQ Treap相关变量
        self.head = 0
        self.cnt = 0
        self.key = [0] * self.MAXN
        self.count = [0] * self.MAXN
        self.left = [0] * self.MAXN
        self.right = [0] * self.MAXN
        self.size = [0] * self.MAXN
        self.priority = [0.0] * self.MAXN
    
    # 初始化并查集
    def init_union_find(self, n):
        """
        初始化并查集
        
        Args:
            n: 节点数
        """
        for i in range(1, n + 1):
            self.parent[i] = i
    
    # 查找根节点（路径压缩）
    def find(self, x):
        """
        查找根节点（路径压缩）
        
        Args:
            x: 节点
            
        Returns:
            根节点
        """
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    # 合并两个集合
    def union(self, x, y):
        """
        合并两个集合
        
        Args:
            x: 节点x
            y: 节点y
        """
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x != root_y:
            self.parent[root_x] = root_y
    
    # 初始化FHQ Treap
    def init_fhq_treap(self):
        """
        初始化FHQ Treap
        """
        self.head = 0
        self.cnt = 0
        self.key = [0] * self.MAXN
        self.count = [0] * self.MAXN
        self.left = [0] * self.MAXN
        self.right = [0] * self.MAXN
        self.size = [0] * self.MAXN
        self.priority = [0.0] * self.MAXN
    
    # 更新节点信息
    def up(self, i):
        """
        更新节点信息
        
        Args:
            i: 节点编号
        """
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + self.count[i]
    
    # 按值分裂
    def split(self, l, r, i, num):
        """
        按值分裂
        
        Args:
            l: 左树根节点编号（结果）
            r: 右树根节点编号（结果）
            i: 待分裂的树根节点编号
            num: 分裂数值
        """
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            if self.key[i] <= num:
                self.right[l] = i
                self.split(i, r, self.right[i], num)
            else:
                self.left[r] = i
                self.split(l, i, self.left[i], num)
            self.up(i)
    
    # 合并操作
    def merge(self, l, r):
        """
        合并操作
        
        Args:
            l: 左树根节点编号
            r: 右树根节点编号
            
        Returns:
            合并后树的根节点编号
        """
        if l == 0 or r == 0:
            return l + r
        if self.priority[l] >= self.priority[r]:
            self.right[l] = self.merge(self.right[l], r)
            self.up(l)
            return l
        else:
            self.left[r] = self.merge(l, self.left[r])
            self.up(r)
            return r
    
    # 查找值为num的节点
    def find_node(self, i, num):
        """
        查找值为num的节点
        
        Args:
            i: 树根节点编号
            num: 查找的数值
            
        Returns:
            节点编号，如果不存在返回0
        """
        if i == 0:
            return 0
        if self.key[i] == num:
            return i
        elif self.key[i] > num:
            return self.find_node(self.left[i], num)
        else:
            return self.find_node(self.right[i], num)
    
    # 改变节点计数
    def change_count(self, i, num, change):
        """
        改变节点计数
        
        Args:
            i: 树根节点编号
            num: 目标数值
            change: 变化量
        """
        if self.key[i] == num:
            self.count[i] += change
        elif self.key[i] > num:
            self.change_count(self.left[i], num, change)
        else:
            self.change_count(self.right[i], num, change)
        self.up(i)
    
    # 插入数值
    def insert(self, num):
        """
        插入数值
        
        Args:
            num: 插入的数值
        """
        if self.find_node(self.head, num) != 0:
            self.change_count(self.head, num, 1)
        else:
            self.split(0, 0, self.head, num)
            self.cnt += 1
            self.key[self.cnt] = num
            self.count[self.cnt] = self.size[self.cnt] = 1
            self.priority[self.cnt] = random.random()
            self.head = self.merge(self.merge(self.right[0], self.cnt), self.left[0])
    
    # 删除数值
    def remove(self, num):
        """
        删除数值
        
        Args:
            num: 删除的数值
        """
        i = self.find_node(self.head, num)
        if i != 0:
            if self.count[i] > 1:
                self.change_count(self.head, num, -1)
            else:
                self.split(0, 0, self.head, num)
                lm = self.right[0]
                r = self.left[0]
                self.split(0, 0, lm, num - 1)
                l = self.right[0]
                self.head = self.merge(l, r)
    
    # 查询排名为x的数值
    def index(self, i, x):
        """
        查询排名为x的数值
        
        Args:
            i: 树根节点编号
            x: 排名
            
        Returns:
            排名为x的数值
        """
        if self.size[self.left[i]] >= x:
            return self.index(self.left[i], x)
        elif self.size[self.left[i]] + self.count[i] < x:
            return self.index(self.right[i], x - self.size[self.left[i]] - self.count[i])
        return self.key[i]
    
    # 查询最大值
    def query_max(self):
        """
        查询最大值
        
        Returns:
            最大值
        """
        if self.head == 0:
            return 0
        i = self.head
        while self.right[i] != 0:
            i = self.right[i]
        return self.key[i]


def main():
    """
    主函数，处理输入输出
    """
    # 重定向输入输出用于测试
    input_text = """5 5
1 2 3 4 5
1 2 1
2 3 2
3 4 3
4 5 4
1 5 5
3
2 1
1 1
2 1"""
    
    sys.stdin = StringIO(input_text)
    
    treap = GraphAndQueriesFHQTreap()
    
    n, m = map(int, input().split())  # 节点数和边数
    
    # 读取节点权重
    weights = [0] + list(map(int, input().split()))  # 0索引不使用，从1开始
    
    # 读取边 (简化处理，不使用类)
    edges = []
    for i in range(m + 1):
        edges.append([0, 0, 0, False])  # [u, v, w, deleted]
    for i in range(1, m + 1):
        u, v, w = map(int, input().split())
        edges[i] = [u, v, w, False]
    
    q = int(input())  # 查询数
    
    # 初始化并查集
    treap.init_union_find(n)
    
    # 处理查询
    for _ in range(q):
        query = list(map(int, input().split()))
        type = query[0]
        
        if type == 1:
            # 删除边
            edge_id = query[1]
            if edge_id < len(edges):
                edges[edge_id][3] = True  # 标记为已删除
        else:
            # 查询连通分量最大权重
            node_id = query[1]
            
            # 重新初始化FHQ Treap
            treap.init_fhq_treap()
            
            # 将与node_id在同一连通分量的所有节点的权重插入到FHQ Treap中
            root = treap.find(node_id)
            for j in range(1, n + 1):
                if treap.find(j) == root:
                    treap.insert(weights[j])
            
            # 查询最大权重
            max_weight = treap.query_max()
            print(max_weight)
            
            # 从FHQ Treap中删除最大权重的节点
            if max_weight > 0:
                treap.remove(max_weight)
                # 更新节点权重为0
                for j in range(1, n + 1):
                    if treap.find(j) == root and weights[j] == max_weight:
                        weights[j] = 0
                        break


if __name__ == "__main__":
    main()