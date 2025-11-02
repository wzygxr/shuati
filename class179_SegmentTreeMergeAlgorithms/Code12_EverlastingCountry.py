# BZOJ2733/HNOI2012 永无乡，Python版
# 测试链接 : https://www.luogu.com.cn/problem/P3224

import sys
from typing import Dict, List

"""
BZOJ2733/HNOI2012 永无乡

题目来源: 2012年全国青少年信息学奥林匹克竞赛（HNOI2012）
题目链接: https://www.luogu.com.cn/problem/P3224

题目描述:
永无乡包含n个岛，编号1到n。每个岛都有一个文明等级，初始时各岛的文明等级各不相同。
现在有两种操作：
1. 连接两个岛，使得这两个岛所在的连通块可以互相到达
2. 查询某个岛所在连通块中文明等级第k小的岛的编号

解题思路:
1. 使用并查集维护连通性，确保每次查询都是在同一个连通块内
2. 为每个岛建立一棵权值线段树，维护该岛所在连通块中各文明等级的出现次数
3. 当合并两个连通块时，将它们的线段树进行合并
4. 当查询第k小时，在线段树上进行二分查找

算法复杂度:
- 时间复杂度: O(n log n + m log n)，其中n是岛的数量，m是操作数量。
  每个操作（合并或查询）的时间复杂度为O(log n)，线段树合并操作的时间复杂度为O(log n)。
- 空间复杂度: O(n log n)，动态开点线段树的空间复杂度。

最优解验证:
线段树合并结合并查集是该问题的最优解。其他可能的解法包括Treap合并或Splay合并，
但线段树合并的实现更加直观，且空间效率较高。

线段树合并与并查集结合的核心思想:
1. 并查集维护连通性，找到每个节点的根节点
2. 每个根节点对应一棵权值线段树，维护该连通块中的信息
3. 合并连通块时，合并对应的线段树，并更新并查集
4. 查询时，在对应根节点的线段树上进行查询

Python实现注意事项:
1. Python的递归深度限制默认为1000，对于深度较大的线段树需要增加递归深度
2. 使用字典实现动态开点线段树，避免预分配大数组
"""

# 增加递归深度限制
sys.setrecursionlimit(1 << 25)

class SegmentTree:
    def __init__(self):
        self.cntt = 0  # 节点计数器
        # 使用字典来动态存储线段树节点，避免预分配大数组
        self.ls = dict()  # 左子节点
        self.rs = dict()  # 右子节点
        self.count = dict()  # 每个节点维护的计数
    
    def new_node(self):
        """创建新的线段树节点"""
        self.cntt += 1
        self.ls[self.cntt] = 0
        self.rs[self.cntt] = 0
        self.count[self.cntt] = 0
        return self.cntt
    
    def update(self, p, l, r, x, v):
        """线段树单点更新"""
        if l == r:
            # 叶子节点，直接更新计数
            self.count[p] = self.count.get(p, 0) + v
            return
        
        mid = (l + r) >> 1
        # 动态开点
        if x <= mid:
            if self.ls[p] == 0:
                self.ls[p] = self.new_node()
            self.update(self.ls[p], l, mid, x, v)
        else:
            if self.rs[p] == 0:
                self.rs[p] = self.new_node()
            self.update(self.rs[p], mid + 1, r, x, v)
        
        # 更新当前节点的计数
        left_count = self.count.get(self.ls[p], 0) if self.ls[p] != 0 else 0
        right_count = self.count.get(self.rs[p], 0) if self.rs[p] != 0 else 0
        self.count[p] = left_count + right_count
    
    def merge(self, x, y, l, r):
        """线段树合并操作"""
        # 如果其中一棵树为空，直接返回另一棵树
        if x == 0:
            return y
        if y == 0:
            return x
        
        # 叶子节点处理
        if l == r:
            # 合并计数
            self.count[x] = self.count.get(x, 0) + self.count.get(y, 0)
            return x
        
        mid = (l + r) >> 1
        
        # 递归合并左右子树
        self.ls[x] = self.merge(self.ls.get(x, 0), self.ls.get(y, 0), l, mid)
        self.rs[x] = self.merge(self.rs.get(x, 0), self.rs.get(y, 0), mid + 1, r)
        
        # 合并后更新当前节点计数
        left_count = self.count.get(self.ls[x], 0) if self.ls[x] != 0 else 0
        right_count = self.count.get(self.rs[x], 0) if self.rs[x] != 0 else 0
        self.count[x] = left_count + right_count
        
        return x
    
    def query_kth(self, p, l, r, k, index_map):
        """在线段树中查询第k小的值"""
        if l == r:
            # 叶子节点，返回对应的岛编号
            return index_map[l]
        
        mid = (l + r) >> 1
        # 左子树的节点数
        left_count = self.count.get(self.ls[p], 0) if self.ls[p] != 0 else 0
        
        if k <= left_count:
            # 第k小在左子树
            return self.query_kth(self.ls[p], l, mid, k, index_map)
        else:
            # 第k小在右子树
            return self.query_kth(self.rs[p], mid + 1, r, k - left_count, index_map)

class UnionFind:
    def __init__(self, size):
        self.parent = list(range(size + 1))  # 1-based索引
    
    def find(self, x):
        """查找根节点（带路径压缩）"""
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """合并两个集合"""
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x != root_y:
            self.parent[root_y] = root_x
            return True
        return False

def main():
    input = sys.stdin.read().split()
    ptr = 0
    
    # 读取岛的数量和操作数量
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 读取每个岛的文明等级
    rank_ = [0] * (n + 1)  # 1-based索引
    index_map = [0] * (n + 1)  # 文明等级到岛编号的映射
    
    for i in range(1, n + 1):
        rank_[i] = int(input[ptr])
        ptr += 1
        index_map[rank_[i]] = i
    
    # 初始化并查集
    uf = UnionFind(n)
    
    # 初始化线段树
    st = SegmentTree()
    root = [0] * (n + 1)  # 每个节点的线段树根节点
    
    for i in range(1, n + 1):
        # 为每个岛创建线段树，并插入自身的文明等级
        root[i] = st.new_node()
        st.update(root[i], 1, n, rank_[i], 1)
    
    # 处理初始连接操作
    for _ in range(m):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        
        # 查找u和v的根节点
        root_u = uf.find(u)
        root_v = uf.find(v)
        
        if root_u != root_v:
            # 合并两个连通块的线段树
            root[root_u] = st.merge(root[root_u], root[root_v], 1, n)
            # 更新并查集
            uf.parent[root_v] = root_u
    
    # 处理查询和合并操作
    q = int(input[ptr])
    ptr += 1
    
    output = []
    for _ in range(q):
        op = input[ptr]
        ptr += 1
        
        if op == 'Q':
            # 查询操作：Q x k
            x = int(input[ptr])
            ptr += 1
            k = int(input[ptr])
            ptr += 1
            
            root_x = uf.find(x)
            # 检查k是否合法
            total_count = st.count.get(root[root_x], 0)
            if k > total_count:
                output.append(-1)
            else:
                ans = st.query_kth(root[root_x], 1, n, k, index_map)
                output.append(ans)
        elif op == 'B':
            # 合并操作：B x y
            x = int(input[ptr])
            ptr += 1
            y = int(input[ptr])
            ptr += 1
            
            # 查找x和y的根节点
            root_x = uf.find(x)
            root_y = uf.find(y)
            
            if root_x != root_y:
                # 合并两个连通块的线段树
                root[root_x] = st.merge(root[root_x], root[root_y], 1, n)
                # 更新并查集
                uf.parent[root_y] = root_x
    
    # 输出结果
    print('\n'.join(map(str, output)))

if __name__ == "__main__":
    main()

"""
工程化考量：
1. 递归深度处理：Python的默认递归深度限制为1000，需要手动增加
2. 输入效率优化：使用sys.stdin.read()一次性读取所有输入，然后分割处理，提高IO效率
3. 内存管理：使用字典动态存储线段树节点，避免预分配大数组
4. 输出优化：收集所有输出结果，最后一次性打印，减少IO操作
5. 代码模块化：将线段树和并查集封装为类，提高代码可读性和复用性

Python语言特性差异：
1. 递归深度限制：需要显式设置sys.setrecursionlimit()
2. 动态类型：使用字典而非静态数组，更灵活但可能效率较低
3. 性能考虑：Python的递归实现比C++和Java慢，对于大规模数据可能需要优化

调试技巧：
1. 可以添加中间变量打印，观察线段树合并和查询过程
2. 使用try-except块捕获可能的递归错误

优化空间：
1. 可以将递归实现改为迭代实现，避免Python的递归深度限制
2. 可以使用按秩合并优化并查集，提高合并效率
3. 对于文明等级范围很大的情况，需要先进行离散化
"""