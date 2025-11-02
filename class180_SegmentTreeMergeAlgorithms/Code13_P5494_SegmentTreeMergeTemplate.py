import sys
sys.setrecursionlimit(300000)

"""
题目：P5494 【模板】线段树合并
测试链接：https://www.luogu.com.cn/problem/P5494

题目描述：
给定一个长度为n的序列，支持两种操作：
1. 将区间[l, r]内的所有数加上k
2. 查询区间[l, r]内所有数的和

解题思路：
1. 线段树合并模板题，演示线段树合并的基本操作
2. 支持动态开点、区间修改、区间查询
3. 时间复杂度：O(n log n)

核心思想：
- 动态开点线段树，避免内存浪费
- 懒标记优化区间修改操作
- 线段树合并用于合并两棵线段树的信息
"""

class SegmentTree:
    """动态开点线段树类"""
    def __init__(self, n):
        self.n = n
        self.cnt = 0
        # 预分配足够空间
        self.tr = [{'l': 0, 'r': 0, 'sum': 0, 'add': 0} for _ in range(40 * n)]
    
    def new_node(self):
        """创建新节点"""
        self.cnt += 1
        return self.cnt
    
    def pushup(self, u):
        """上传信息"""
        left = self.tr[u]['l']
        right = self.tr[u]['r']
        self.tr[u]['sum'] = self.tr[left]['sum'] + self.tr[right]['sum']
    
    def pushdown(self, u, l, r):
        """下传懒标记"""
        if self.tr[u]['add'] != 0:
            mid = (l + r) // 2
            
            # 更新左子树
            if self.tr[u]['l'] != 0:
                left = self.tr[u]['l']
                self.tr[left]['sum'] += self.tr[u]['add'] * (mid - l + 1)
                self.tr[left]['add'] += self.tr[u]['add']
            
            # 更新右子树
            if self.tr[u]['r'] != 0:
                right = self.tr[u]['r']
                self.tr[right]['sum'] += self.tr[u]['add'] * (r - mid)
                self.tr[right]['add'] += self.tr[u]['add']
            
            self.tr[u]['add'] = 0
    
    def build(self, l, r, a):
        """构建线段树"""
        u = self.new_node()
        if l == r:
            self.tr[u]['sum'] = a[l]
            return u
        
        mid = (l + r) // 2
        self.tr[u]['l'] = self.build(l, mid, a)
        self.tr[u]['r'] = self.build(mid + 1, r, a)
        self.pushup(u)
        return u
    
    def update(self, u, l, r, ql, qr, k):
        """区间更新"""
        if ql <= l and r <= qr:
            self.tr[u]['sum'] += k * (r - l + 1)
            self.tr[u]['add'] += k
            return
        
        self.pushdown(u, l, r)
        mid = (l + r) // 2
        
        if ql <= mid:
            self.update(self.tr[u]['l'], l, mid, ql, qr, k)
        if qr > mid:
            self.update(self.tr[u]['r'], mid + 1, r, ql, qr, k)
        
        self.pushup(u)
    
    def query(self, u, l, r, ql, qr):
        """区间查询"""
        if ql <= l and r <= qr:
            return self.tr[u]['sum']
        
        self.pushdown(u, l, r)
        mid = (l + r) // 2
        res = 0
        
        if ql <= mid:
            res += self.query(self.tr[u]['l'], l, mid, ql, qr)
        if qr > mid:
            res += self.query(self.tr[u]['r'], mid + 1, r, ql, qr)
        
        return res
    
    def merge(self, u, v, l, r):
        """线段树合并（核心函数）"""
        if u == 0:
            return v
        if v == 0:
            return u
        
        if l == r:
            # 叶子节点直接合并
            self.tr[u]['sum'] += self.tr[v]['sum']
            return u
        
        self.pushdown(u, l, r)
        self.pushdown(v, l, r)
        
        mid = (l + r) // 2
        self.tr[u]['l'] = self.merge(self.tr[u]['l'], self.tr[v]['l'], l, mid)
        self.tr[u]['r'] = self.merge(self.tr[u]['r'], self.tr[v]['r'], mid + 1, r)
        
        self.pushup(u)
        return u
    
    def clone(self, u):
        """复制线段树（用于持久化）"""
        v = self.new_node()
        self.tr[v]['l'] = self.tr[u]['l']
        self.tr[v]['r'] = self.tr[u]['r']
        self.tr[v]['sum'] = self.tr[u]['sum']
        self.tr[v]['add'] = self.tr[u]['add']
        return v

class Solution:
    def __init__(self):
        self.n = 0
        self.m = 0
        self.a = []
        self.seg = None
        self.root = 0
    
    def solve(self):
        """主求解函数"""
        data = sys.stdin.read().split()
        it = iter(data)
        
        self.n = int(next(it))
        self.m = int(next(it))
        
        # 读取初始序列
        self.a = [0] * (self.n + 1)
        for i in range(1, self.n + 1):
            self.a[i] = int(next(it))
        
        # 初始化线段树
        self.seg = SegmentTree(self.n)
        self.root = self.seg.build(1, self.n, self.a)
        
        # 处理操作
        for _ in range(self.m):
            op = int(next(it))
            if op == 1:
                l = int(next(it))
                r = int(next(it))
                k = int(next(it))
                self.seg.update(self.root, 1, self.n, l, r, k)
            else:
                l = int(next(it))
                r = int(next(it))
                ans = self.seg.query(self.root, 1, self.n, l, r)
                print(ans)

if __name__ == "__main__":
    solution = Solution()
    solution.solve()

"""
线段树合并的应用场景：
1. 树形DP优化：将子树信息合并到父节点
2. 可持久化线段树：支持历史版本查询
3. 区间赋值问题：如CF911G Mass Change Queries
4. 树上问题：如P4556 雨天的尾巴

线段树合并的复杂度分析：
- 每次合并操作的时间复杂度为两棵线段树公共节点数
- 总时间复杂度为O(n log n)，因为每个节点最多被合并log n次

注意事项：
1. 合并前需要下传懒标记
2. 合并后需要上传信息
3. 注意内存分配，避免内存溢出
4. 对于持久化线段树，需要复制节点

类似题目推荐：
1. P6773 [NOI2020] 命运 - 树形DP+线段树合并
2. CF911G Mass Change Queries - 区间赋值+线段树合并
3. P4556 [Vani有约会]雨天的尾巴 - 树上差分+线段树合并
4. P3224 [HNOI2012]永无乡 - 并查集+线段树合并
5. P5298 [PKUWC2018]Minimax - 概率DP+线段树合并

线段树合并的变种：
1. 权值线段树合并：用于维护值域信息
2. 主席树合并：支持可持久化操作
3. 李超线段树合并：用于维护函数最值
"""