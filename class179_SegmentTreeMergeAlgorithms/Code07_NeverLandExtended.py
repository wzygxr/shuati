# 永无乡扩展版，Python版
# 测试链接 : https://www.luogu.com.cn/problem/P3224

import sys
from collections import defaultdict

class SegmentTreeNode:
    def __init__(self):
        self.left = None    # 左子节点
        self.right = None   # 右子节点
        self.sum = 0        # 区间和

class NeverLand:
    def __init__(self, n):
        self.n = n
        self.pos = [0] * (n + 1)  # 重要度到编号的映射
        self.root = [None] * (n + 1)  # 每个连通块的线段树根节点
        self.father = list(range(n + 1))  # 并查集
    
    def find(self, x):
        """并查集查找"""
        if x != self.father[x]:
            self.father[x] = self.find(self.father[x])
        return self.father[x]
    
    def union(self, x, y):
        """并查集合并"""
        x_fa = self.find(x)
        y_fa = self.find(y)
        if x_fa != y_fa:
            self.father[x_fa] = y_fa
            self.root[y_fa] = self.merge(1, self.n, self.root[y_fa], self.root[x_fa])
    
    def up(self, node):
        """更新节点信息"""
        node.sum = 0
        if node.left:
            node.sum += node.left.sum
        if node.right:
            node.sum += node.right.sum
    
    def add(self, jobi, l, r, node):
        """在位置jobi插入元素"""
        if not node:
            node = SegmentTreeNode()
        
        if l == r:
            node.sum += 1
        else:
            mid = (l + r) // 2
            if jobi <= mid:
                node.left = self.add(jobi, l, mid, node.left)
            else:
                node.right = self.add(jobi, mid + 1, r, node.right)
            self.up(node)
        return node
    
    def merge(self, l, r, t1, t2):
        """合并两棵线段树"""
        # 如果其中一个节点为空，返回另一个节点
        if not t1:
            return t2
        if not t2:
            return t1
        
        # 如果是叶子节点，合并节点信息
        if l == r:
            t1.sum += t2.sum
        else:
            # 递归合并左右子树
            mid = (l + r) // 2
            t1.left = self.merge(l, mid, t1.left, t2.left)
            t1.right = self.merge(mid + 1, r, t1.right, t2.right)
            self.up(t1)
        return t1
    
    def query(self, jobk, l, r, node):
        """查询第k小的元素"""
        if not node or jobk > node.sum:
            return -1
        
        if l == r:
            return self.pos[l]
        
        mid = (l + r) // 2
        left_sum = node.left.sum if node.left else 0
        
        if left_sum >= jobk:
            return self.query(jobk, l, mid, node.left)
        else:
            return self.query(jobk - left_sum, mid + 1, r, node.right)
    
    def build(self, importance):
        """初始化每个岛屿"""
        for i in range(1, self.n + 1):
            self.pos[importance[i-1]] = i  # 重要度为importance[i-1]的岛屿编号是i
            self.root[i] = self.add(importance[i-1], 1, self.n, None)  # 为每个岛屿建立线段树

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    # 读取重要度
    importance = []
    for i in range(n):
        importance.append(int(data[idx]))
        idx += 1
    
    # 初始化
    neverland = NeverLand(n)
    neverland.build(importance)
    
    # 处理初始连接关系
    for i in range(m):
        x = int(data[idx])
        idx += 1
        y = int(data[idx])
        idx += 1
        neverland.union(x, y)
    
    # 处理查询操作
    q = int(data[idx])
    idx += 1
    
    results = []
    for i in range(q):
        op = data[idx]
        idx += 1
        if op == 'B':
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            neverland.union(x, y)
        else:
            x = int(data[idx])
            idx += 1
            k = int(data[idx])
            idx += 1
            result = neverland.query(k, 1, n, neverland.root[neverland.find(x)])
            results.append(str(result))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()