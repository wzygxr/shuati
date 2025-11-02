# CF1009F Dominant Indices，Python版
# 测试链接 : https://codeforces.com/contest/1009/problem/F

import sys
from sys import stdin
from typing import List, Dict, Tuple

"""
CF1009F Dominant Indices

题目来源: Codeforces Round 484 (Div. 2)
题目链接: https://codeforces.com/contest/1009/problem/F

题目描述:
给定一棵树，对于每个节点u，求其子树中距离u恰好为k的节点数最大的k值。
如果有多个k值有相同的最大节点数，取最小的k。

解题思路:
1. 使用深度优先搜索（DFS）遍历整棵树
2. 对于每个节点u，维护一个线段树，记录其子树中各个深度的节点数目
3. 在DFS过程中，递归处理子节点，并将子节点的线段树合并到父节点
4. 在合并过程中，动态更新每个节点的最优k值（出现次数最多的深度，且最小）

算法复杂度:
- 时间复杂度: O(n log n)，其中n是树的节点数。每个节点最多被访问一次，
  每次线段树合并操作的时间复杂度是O(log n)。
- 空间复杂度: O(n log n)，动态开点线段树的空间复杂度。

最优解验证:
线段树合并是该问题的最优解。其他可能的解法包括暴力统计每个节点的子树深度分布，
但时间复杂度为O(n^2)，无法通过大规模测试用例。

线段树合并解决树形统计问题的核心思想:
1. 后序遍历树，先处理所有子节点
2. 为每个节点维护一个数据结构，记录所需的统计信息
3. 将子节点的数据结构合并到父节点，形成父节点的完整统计信息
4. 利用合并过程中的中间结果回答问题

Python实现注意事项:
1. Python的递归深度限制默认为1000，对于大规模数据需要增加递归深度
2. 使用字典实现动态开点线段树，避免预分配大数组
3. 由于Python的递归效率较低，需要注意优化递归实现
"""

# 增加递归深度限制
sys.setrecursionlimit(1 << 25)

class SegmentTree:
    def __init__(self):
        self.cntt = 0  # 节点计数器
        # 使用字典来动态存储线段树节点
        self.ls = dict()  # 左子节点
        self.rs = dict()  # 右子节点
        self.max_val = dict()  # 每个节点维护的最大值
        self.pos = dict()  # 最大值对应的位置
    
    def new_node(self) -> int:
        """创建新的线段树节点"""
        self.cntt += 1
        self.ls[self.cntt] = 0
        self.rs[self.cntt] = 0
        self.max_val[self.cntt] = 0
        self.pos[self.cntt] = 0
        return self.cntt
    
    def push_up(self, p: int) -> None:
        """向上合并线段树节点信息"""
        ls_p = self.ls[p]
        rs_p = self.rs[p]
        
        # 如果左子树为空，直接使用右子树的信息
        if ls_p == 0:
            self.max_val[p] = self.max_val.get(rs_p, 0)
            self.pos[p] = self.pos.get(rs_p, 0)
            return
        # 如果右子树为空，直接使用左子树的信息
        if rs_p == 0:
            self.max_val[p] = self.max_val.get(ls_p, 0)
            self.pos[p] = self.pos.get(ls_p, 0)
            return
        
        # 左右子树都不为空，比较两个子树的最大值
        if self.max_val.get(ls_p, 0) > self.max_val.get(rs_p, 0):
            # 左子树的最大值更大
            self.max_val[p] = self.max_val[ls_p]
            self.pos[p] = self.pos[ls_p]
        elif self.max_val.get(ls_p, 0) < self.max_val.get(rs_p, 0):
            # 右子树的最大值更大
            self.max_val[p] = self.max_val[rs_p]
            self.pos[p] = self.pos[rs_p]
        else:
            # 最大值相等，取位置较小的
            self.max_val[p] = self.max_val[ls_p]
            self.pos[p] = min(self.pos[ls_p], self.pos[rs_p])
    
    def update(self, p: int, l: int, r: int, x: int, v: int) -> None:
        """线段树单点更新"""
        if l == r:
            # 叶子节点，直接更新值
            self.max_val[p] = self.max_val.get(p, 0) + v
            self.pos[p] = l
            return
        
        mid = (l + r) >> 1
        
        # 根据x的位置决定更新左子树还是右子树
        if x <= mid:
            if self.ls[p] == 0:
                self.ls[p] = self.new_node()
            self.update(self.ls[p], l, mid, x, v)
        else:
            if self.rs[p] == 0:
                self.rs[p] = self.new_node()
            self.update(self.rs[p], mid + 1, r, x, v)
        
        # 更新当前节点的最大值和对应位置
        self.push_up(p)
    
    def merge(self, x: int, y: int, l: int, r: int) -> int:
        """线段树合并操作"""
        # 如果其中一棵树为空，直接返回另一棵树
        if x == 0:
            return y
        if y == 0:
            return x
        
        # 叶子节点处理
        if l == r:
            # 合并两个叶子节点的值
            self.max_val[x] = self.max_val.get(x, 0) + self.max_val.get(y, 0)
            self.pos[x] = l
            return x
        
        mid = (l + r) >> 1
        
        # 递归合并左右子树
        self.ls[x] = self.merge(self.ls.get(x, 0), self.ls.get(y, 0), l, mid)
        self.rs[x] = self.merge(self.rs.get(x, 0), self.rs.get(y, 0), mid + 1, r)
        
        # 合并后更新当前节点的信息
        self.push_up(x)
        
        return x

def main():
    # 使用快速输入
    input = stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    # 初始化树的边列表
    tree = [[] for _ in range(n + 1)]
    
    # 读取边
    for _ in range(n - 1):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        tree[u].append(v)
        tree[v].append(u)
    
    # 初始化变量
    ans = [0] * (n + 1)
    root = [0] * (n + 1)
    
    # 初始化线段树
    st = SegmentTree()
    
    def dfs(u: int, fa: int) -> None:
        """深度优先搜索遍历树"""
        # 为当前节点创建线段树，并初始化为深度0（距离自己0）
        root[u] = st.new_node()
        st.update(root[u], 0, 100000, 0, 1)
        
        # 遍历所有子节点（排除父节点）
        for v in tree[u]:
            if v == fa:
                continue
            
            # 递归处理子节点
            dfs(v, u)
            
            # 将子节点的线段树合并到当前节点
            root[u] = st.merge(root[u], root[v], 0, 100000)
        
        # 记录当前节点的答案（线段树中最大值对应的位置）
        ans[u] = st.pos[root[u]]
    
    # 从根节点（1号节点）开始DFS
    dfs(1, 0)
    
    # 输出所有节点的答案
    print('\n'.join(map(str, ans[1:n+1])))

if __name__ == "__main__":
    main()

"""
工程化考量：
1. 递归深度处理：Python的默认递归深度限制为1000，需要手动增加
2. 输入效率优化：使用sys.stdin.read()一次性读取所有输入，然后分割处理，提高IO效率
3. 内存管理：使用字典动态存储线段树节点，避免预分配大数组
4. 输出优化：收集所有输出结果，最后一次性打印，减少IO操作
5. 代码模块化：将线段树封装为类，提高代码可读性和复用性

Python语言特性差异：
1. 递归深度限制：需要显式设置sys.setrecursionlimit()
2. 动态类型：使用字典而非静态数组，更灵活但可能效率较低
3. 闭包函数：在main函数内部定义dfs函数，方便访问外部变量

调试技巧：
1. 可以添加中间变量打印，观察线段树合并和查询过程
2. 使用try-except块捕获可能的递归错误

优化空间：
1. 可以将递归实现改为迭代实现，避免Python的递归深度限制
2. 可以使用更高效的数据结构来模拟线段树，如使用数组代替字典
3. 对于大数据量测试用例，可以考虑使用PyPy运行以提高速度
"""