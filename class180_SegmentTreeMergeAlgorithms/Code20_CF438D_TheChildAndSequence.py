import sys

sys.setrecursionlimit(300000)

class FastIO:
    def __init__(self):
        self.stdin = sys.stdin
        self.stdout = sys.stdout
        
    def read(self):
        return self.stdin.readline().rstrip()
    
    def read_int(self):
        return int(self.read())
    
    def read_ints(self):
        return list(map(int, self.read().split()))
    
    def print(self, *args, **kwargs):
        print(*args, **kwargs, file=self.stdout)

class SegmentTreeNode:
    __slots__ = ['sum', 'max_val', 'left', 'right']
    
    def __init__(self):
        self.sum = 0  # 区间和
        self.max_val = 0  # 区间最大值
        self.left = None  # 左儿子
        self.right = None  # 右儿子

class Code20_CF438D_TheChildAndSequence:
    """
    CF438D The Child and Sequence - 线段树分裂算法实现 (Python版本)
    
    题目链接: https://codeforces.com/contest/438/problem/D
    
    题目描述:
    给定一个序列，支持三种操作:
    1. 区间求和
    2. 区间取模 (每个数对x取模)
    3. 单点修改
    
    核心算法: 线段树 + 取模优化 + 线段树分裂
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    
    解题思路:
    1. 使用线段树维护区间最大值和区间和
    2. 对于取模操作，如果区间最大值小于模数，则不需要递归
    3. 使用线段树分裂优化取模操作
    4. 维护区间最大值来剪枝
    """
    
    def __init__(self):
        self.n = 0
        self.m = 0
        self.arr = []
        self.root = None
    
    def build(self, l, r):
        """构建线段树"""
        node = SegmentTreeNode()
        if l == r:
            node.sum = self.arr[l]
            node.max_val = self.arr[l]
            return node
        
        mid = (l + r) // 2
        node.left = self.build(l, mid)
        node.right = self.build(mid + 1, r)
        node.sum = node.left.sum + node.right.sum
        node.max_val = max(node.left.max_val, node.right.max_val)
        return node
    
    def update(self, node, l, r, pos, val):
        """单点更新"""
        if l == r:
            node.sum = val
            node.max_val = val
            return
        
        mid = (l + r) // 2
        if pos <= mid:
            self.update(node.left, l, mid, pos, val)
        else:
            self.update(node.right, mid + 1, r, pos, val)
        
        node.sum = node.left.sum + node.right.sum
        node.max_val = max(node.left.max_val, node.right.max_val)
    
    def modulo(self, node, l, r, ql, qr, mod):
        """区间取模"""
        if node.max_val < mod:
            return  # 剪枝：最大值小于模数，不需要处理
        
        if l == r:
            # 叶子节点直接取模
            node.sum %= mod
            node.max_val = node.sum
            return
        
        mid = (l + r) // 2
        if ql <= mid:
            self.modulo(node.left, l, mid, ql, qr, mod)
        if qr > mid:
            self.modulo(node.right, mid + 1, r, ql, qr, mod)
        
        node.sum = node.left.sum + node.right.sum
        node.max_val = max(node.left.max_val, node.right.max_val)
    
    def query(self, node, l, r, ql, qr):
        """区间查询"""
        if ql <= l and r <= qr:
            return node.sum
        
        mid = (l + r) // 2
        res = 0
        if ql <= mid:
            res += self.query(node.left, l, mid, ql, qr)
        if qr > mid:
            res += self.query(node.right, mid + 1, r, ql, qr)
        return res
    
    def split_for_modulo(self, node, l, r, ql, qr, mod):
        """线段树分裂 - 将需要取模的区间分裂出来"""
        if node.max_val < mod:
            return None  # 不需要处理的部分
        
        if ql <= l and r <= qr:
            # 整个区间都在查询范围内
            if node.max_val < mod:
                return None
            
            # 创建新的节点来处理取模
            new_node = SegmentTreeNode()
            if l == r:
                # 叶子节点直接取模
                new_node.sum = node.sum % mod
                new_node.max_val = new_node.sum
            else:
                mid = (l + r) // 2
                new_node.left = self.split_for_modulo(node.left, l, mid, ql, qr, mod)
                new_node.right = self.split_for_modulo(node.right, mid + 1, r, ql, qr, mod)
                
                if new_node.left is None and new_node.right is None:
                    return None
                
                new_node.sum = (new_node.left.sum if new_node.left else 0) + \
                             (new_node.right.sum if new_node.right else 0)
                new_node.max_val = max(new_node.left.max_val if new_node.left else 0, 
                                    new_node.right.max_val if new_node.right else 0)
            return new_node
        
        mid = (l + r) // 2
        left_part = None
        right_part = None
        
        if ql <= mid:
            left_part = self.split_for_modulo(node.left, l, mid, ql, qr, mod)
        if qr > mid:
            right_part = self.split_for_modulo(node.right, mid + 1, r, ql, qr, mod)
        
        if left_part is None and right_part is None:
            return None
        
        new_node = SegmentTreeNode()
        new_node.left = left_part
        new_node.right = right_part
        new_node.sum = (left_part.sum if left_part else 0) + \
                     (right_part.sum if right_part else 0)
        new_node.max_val = max(left_part.max_val if left_part else 0, 
                            right_part.max_val if right_part else 0)
        
        return new_node
    
    def merge_back(self, original, processed, l, r, ql, qr):
        """线段树合并 - 将处理后的区间合并回原树"""
        if processed is None:
            return
        
        if ql <= l and r <= qr:
            # 整个区间都在查询范围内
            if l == r:
                # 叶子节点直接替换
                original.sum = processed.sum
                original.max_val = processed.max_val
            else:
                # 递归合并左右子树
                mid = (l + r) // 2
                self.merge_back(original.left, processed.left, l, mid, ql, qr)
                self.merge_back(original.right, processed.right, mid + 1, r, ql, qr)
                original.sum = original.left.sum + original.right.sum
                original.max_val = max(original.left.max_val, original.right.max_val)
            return
        
        mid = (l + r) // 2
        if ql <= mid:
            self.merge_back(original.left, processed.left, l, mid, ql, qr)
        if qr > mid:
            self.merge_back(original.right, processed.right, mid + 1, r, ql, qr)
        
        original.sum = original.left.sum + original.right.sum
        original.max_val = max(original.left.max_val, original.right.max_val)
    
    def optimized_modulo(self, l, r, mod):
        """优化的取模操作 - 使用线段树分裂"""
        # 分裂出需要处理的区间
        processed = self.split_for_modulo(self.root, 1, self.n, l, r, mod)
        
        if processed is not None:
            # 将处理后的区间合并回原树
            self.merge_back(self.root, processed, 1, self.n, l, r)
    
    def solve(self):
        io = FastIO()
        
        self.n, self.m = io.read_ints()
        self.arr = [0] * (self.n + 1)
        
        arr_vals = io.read_ints()
        for i in range(1, self.n + 1):
            self.arr[i] = arr_vals[i - 1]
        
        # 构建线段树
        self.root = self.build(1, self.n)
        
        for _ in range(self.m):
            data = io.read_ints()
            type_op = data[0]
            
            if type_op == 1:  # 区间求和
                l, r = data[1], data[2]
                result = self.query(self.root, 1, self.n, l, r)
                io.print(result)
            elif type_op == 2:  # 区间取模
                l, r, mod = data[1], data[2], data[3]
                self.optimized_modulo(l, r, mod)
            elif type_op == 3:  # 单点修改
                pos, val = data[1], data[2]
                self.update(self.root, 1, self.n, pos, val)
        
        io.stdout.flush()

if __name__ == "__main__":
    solution = Code20_CF438D_TheChildAndSequence()
    solution.solve()

"""
类似题目推荐:
1. CF1401F Reverse and Swap - 线段树分裂经典应用
2. CF474F Ant Colony - GCD操作 + 线段树
3. CF52C Circular RMQ - 环形区间操作
4. P5494 【模板】线段树分裂 - 线段树分裂基础模板
5. P4556 [Vani有约会]雨天的尾巴 - 树上差分 + 线段树合并

线段树分裂算法总结:
线段树分裂在取模操作中的应用:
1. 将需要取模的区间分裂出来单独处理
2. 避免对整个线段树进行不必要的递归
3. 通过最大值剪枝优化性能
4. 处理完成后合并回原树

时间复杂度: O(log n) 每次分裂/合并
空间复杂度: O(n log n)
"""