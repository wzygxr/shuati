import sys
sys.setrecursionlimit(300000)

"""
题目：CF911G Mass Change Queries
测试链接：https://www.luogu.com.cn/problem/CF911G

题目描述：
给定一个长度为n的序列，支持m次操作，每次操作将区间[l, r]内所有等于x的数改为y。
最后输出整个序列。

解题思路：
1. 使用线段树合并解决区间赋值问题
2. 每个节点维护一个映射，表示当前区间内值的转换关系
3. 使用懒标记优化区间修改操作
4. 时间复杂度：O((n + m) log n)

核心思想：
- 对于每个线段树节点，维护一个大小为100的数组，表示值i被映射到哪个值
- 区间修改时，更新对应区间的映射关系
- 查询时，通过懒标记下传和映射关系获取最终结果
"""

class SegmentTree:
    """动态开点线段树类，支持区间赋值操作"""
    def __init__(self, n):
        self.n = n
        self.MAX_VAL = 100  # 值的范围是1-100
        self.cnt = 0
        # 预分配足够空间
        self.tr = [None] * (40 * n)
        for i in range(len(self.tr)):
            self.tr[i] = {
                'l': 0, 'r': 0,
                'map': [i for i in range(self.MAX_VAL + 1)],  # 恒等映射
                'lazy': False
            }
    
    def new_node(self):
        """创建新节点"""
        self.cnt += 1
        return self.cnt
    
    def pushdown(self, u):
        """下传懒标记"""
        if self.tr[u]['lazy']:
            # 更新左子树
            if self.tr[u]['l'] != 0:
                self.apply_mapping(self.tr[u]['l'], self.tr[u]['map'])
            
            # 更新右子树
            if self.tr[u]['r'] != 0:
                self.apply_mapping(self.tr[u]['r'], self.tr[u]['map'])
            
            # 重置当前节点的映射为恒等映射
            for i in range(1, self.MAX_VAL + 1):
                self.tr[u]['map'][i] = i
            self.tr[u]['lazy'] = False
    
    def apply_mapping(self, u, parent_map):
        """应用映射关系"""
        # 创建新的映射：new_map[i] = parent_map[tr[u].map[i]]
        new_map = [0] * (self.MAX_VAL + 1)
        for i in range(1, self.MAX_VAL + 1):
            new_map[i] = parent_map[self.tr[u]['map'][i]]
        
        # 如果子节点已经有懒标记，需要合并映射
        if self.tr[u]['lazy']:
            temp = new_map.copy()
            new_map = temp
        else:
            # 否则直接设置映射
            self.tr[u]['map'] = new_map
            self.tr[u]['lazy'] = True
    
    def build(self, l, r):
        """构建线段树"""
        u = self.new_node()
        if l == r:
            # 叶子节点不需要特殊处理，映射关系已经是恒等映射
            return u
        
        mid = (l + r) // 2
        self.tr[u]['l'] = self.build(l, mid)
        self.tr[u]['r'] = self.build(mid + 1, r)
        return u
    
    def update(self, u, l, r, ql, qr, x, y):
        """区间更新：将区间[l, r]内所有值为x的数改为y"""
        if ql <= l and r <= qr:
            # 整个区间都在查询范围内
            if not self.tr[u]['lazy']:
                self.tr[u]['lazy'] = True
            
            # 更新映射：将x映射到y，其他值保持不变
            for i in range(1, self.MAX_VAL + 1):
                if self.tr[u]['map'][i] == x:
                    self.tr[u]['map'][i] = y
            return
        
        self.pushdown(u)
        mid = (l + r) // 2
        
        if ql <= mid:
            self.update(self.tr[u]['l'], l, mid, ql, qr, x, y)
        if qr > mid:
            self.update(self.tr[u]['r'], mid + 1, r, ql, qr, x, y)
    
    def query(self, u, l, r, pos, a):
        """单点查询：获取位置pos的值"""
        if l == r:
            # 叶子节点，应用映射关系后返回
            return self.tr[u]['map'][a[l]]
        
        self.pushdown(u)
        mid = (l + r) // 2
        
        if pos <= mid:
            return self.query(self.tr[u]['l'], l, mid, pos, a)
        else:
            return self.query(self.tr[u]['r'], mid + 1, r, pos, a)
    
    def merge(self, u, v, l, r):
        """线段树合并（可选功能，用于优化）"""
        if u == 0:
            return v
        if v == 0:
            return u
        
        if l == r:
            # 叶子节点合并：应用v的映射到u
            if self.tr[v]['lazy']:
                if not self.tr[u]['lazy']:
                    self.tr[u]['lazy'] = True
                    # 复制恒等映射
                    for i in range(1, self.MAX_VAL + 1):
                        self.tr[u]['map'][i] = i
                
                # 合并映射
                for i in range(1, self.MAX_VAL + 1):
                    self.tr[u]['map'][i] = self.tr[v]['map'][self.tr[u]['map'][i]]
            return u
        
        self.pushdown(u)
        self.pushdown(v)
        
        mid = (l + r) // 2
        self.tr[u]['l'] = self.merge(self.tr[u]['l'], self.tr[v]['l'], l, mid)
        self.tr[u]['r'] = self.merge(self.tr[u]['r'], self.tr[v]['r'], mid + 1, r)
        
        return u

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
        
        # 读取初始序列
        self.a = [0] * (self.n + 1)
        for i in range(1, self.n + 1):
            self.a[i] = int(next(it))
        
        # 初始化线段树
        self.seg = SegmentTree(self.n)
        self.root = self.seg.build(1, self.n)
        
        self.m = int(next(it))
        
        # 处理操作
        for _ in range(self.m):
            l = int(next(it))
            r = int(next(it))
            x = int(next(it))
            y = int(next(it))
            
            if x != y:
                self.seg.update(self.root, 1, self.n, l, r, x, y)
        
        # 输出最终序列
        result = []
        for i in range(1, self.n + 1):
            result.append(str(self.seg.query(self.root, 1, self.n, i, self.a)))
        print(' '.join(result))

if __name__ == "__main__":
    solution = Solution()
    solution.solve()

"""
解题技巧总结：
1. 映射关系的维护：每个节点维护一个值域大小的映射数组
2. 懒标记的应用：只有当需要下传时才创建新的映射关系
3. 映射合并：父节点的映射应用到子节点的映射上
4. 内存优化：动态开点避免内存浪费

类似题目推荐：
1. P5494 【模板】线段树合并 - 线段树合并基础
2. P6773 [NOI2020] 命运 - 树形DP+线段树合并
3. P4556 [Vani有约会]雨天的尾巴 - 树上差分+线段树合并
4. P3224 [HNOI2012]永无乡 - 并查集+线段树合并

线段树合并的变种应用：
1. 区间赋值：通过维护映射关系实现高效区间修改
2. 颜色段合并：维护连续相同值的区间
3. 历史版本管理：通过可持久化线段树支持历史查询

性能优化建议：
1. 使用数组而非字典提高访问速度
2. 懒标记及时下传避免深度递归
3. 合理预分配内存减少动态分配开销
"""