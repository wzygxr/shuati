import sys
import collections

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
    __slots__ = ['l', 'r', 'sum', 'mul']
    
    def __init__(self):
        self.l = -1  # 左儿子下标
        self.r = -1  # 右儿子下标
        self.sum = 0  # 区间概率和
        self.mul = 1  # 懒标记，用于概率转移

class Code18_P5298_Minimax:
    """
    P5298 [PKUWC2018] Minimax - 线段树分裂算法实现 (Python版本)
    
    题目链接: https://www.luogu.com.cn/problem/P5298
    
    题目描述:
    给定一棵二叉树，每个叶子节点有一个权值，非叶子节点有一个概率p。
    对于每个非叶子节点，其权值有p的概率取左子树的最大值，有1-p的概率取右子树的最大值。
    求根节点取每个可能权值的概率。
    
    核心算法: 线段树合并 + 概率DP
    时间复杂度: O(n log n)
    空间复杂度: O(n log n)
    
    解题思路:
    1. 对每个节点维护一个权值线段树，记录每个权值出现的概率
    2. 使用动态开点线段树，支持线段树合并
    3. 在合并过程中维护概率转移
    4. 最后遍历根节点的线段树得到答案
    """
    
    MOD = 998244353
    INV10000 = 796898467  # 10000的逆元
    
    def __init__(self):
        self.n = 0
        self.tree = []  # 树结构
        self.seg = []   # 线段树节点数组
        self.root = []  # 每个节点的线段树根
        self.seg_cnt = 0
        self.vals = []  # 权值列表
        self.mapping = {}  # 权值映射
    
    def new_node(self):
        """动态开点线段树 - 新建节点"""
        if self.seg_cnt >= len(self.seg):
            self.seg.extend([SegmentTreeNode() for _ in range(len(self.seg))])
        
        node = SegmentTreeNode()
        if self.seg_cnt < len(self.seg):
            self.seg[self.seg_cnt] = node
        else:
            self.seg.append(node)
        
        self.seg_cnt += 1
        return self.seg_cnt - 1
    
    def push_down(self, p):
        """下传懒标记"""
        if self.seg[p].mul != 1:
            if self.seg[p].l != -1:
                self.seg[self.seg[p].l].sum = (self.seg[self.seg[p].l].sum * self.seg[p].mul) % self.MOD
                self.seg[self.seg[p].l].mul = (self.seg[self.seg[p].l].mul * self.seg[p].mul) % self.MOD
            if self.seg[p].r != -1:
                self.seg[self.seg[p].r].sum = (self.seg[self.seg[p].r].sum * self.seg[p].mul) % self.MOD
                self.seg[self.seg[p].r].mul = (self.seg[self.seg[p].r].mul * self.seg[p].mul) % self.MOD
            self.seg[p].mul = 1
    
    def update(self, p, l, r, pos, val):
        """单点更新"""
        if l == r:
            self.seg[p].sum = (self.seg[p].sum + val) % self.MOD
            return
        
        self.push_down(p)
        mid = (l + r) // 2
        
        if pos <= mid:
            if self.seg[p].l == -1:
                self.seg[p].l = self.new_node()
            self.update(self.seg[p].l, l, mid, pos, val)
        else:
            if self.seg[p].r == -1:
                self.seg[p].r = self.new_node()
            self.update(self.seg[p].r, mid + 1, r, pos, val)
        
        # 更新当前节点
        self.seg[p].sum = 0
        if self.seg[p].l != -1:
            self.seg[p].sum = (self.seg[p].sum + self.seg[self.seg[p].l].sum) % self.MOD
        if self.seg[p].r != -1:
            self.seg[p].sum = (self.seg[p].sum + self.seg[self.seg[p].r].sum) % self.MOD
    
    def merge(self, x, y, p, sum_x, sum_y):
        """线段树合并"""
        if x == -1 and y == -1:
            return -1
        if x == -1:
            # 只有y树存在
            mul = (p * sum_x + (1 - p) * sum_y) % self.MOD
            self.seg[y].sum = (self.seg[y].sum * mul) % self.MOD
            self.seg[y].mul = (self.seg[y].mul * mul) % self.MOD
            return y
        if y == -1:
            # 只有x树存在
            mul = (p * sum_x + (1 - p) * sum_y) % self.MOD
            self.seg[x].sum = (self.seg[x].sum * mul) % self.MOD
            self.seg[x].mul = (self.seg[x].mul * mul) % self.MOD
            return x
        
        self.push_down(x)
        self.push_down(y)
        
        # 计算左右子树的概率和
        left_sum_x = self.seg[self.seg[x].l].sum if self.seg[x].l != -1 else 0
        left_sum_y = self.seg[self.seg[y].l].sum if self.seg[y].l != -1 else 0
        right_sum_x = self.seg[self.seg[x].r].sum if self.seg[x].r != -1 else 0
        right_sum_y = self.seg[self.seg[y].r].sum if self.seg[y].r != -1 else 0
        
        # 递归合并左右子树
        self.seg[x].l = self.merge(self.seg[x].l, self.seg[y].l, p, 
                                  sum_x + right_sum_x, sum_y + right_sum_y)
        self.seg[x].r = self.merge(self.seg[x].r, self.seg[y].r, p,
                                  sum_x + left_sum_x, sum_y + left_sum_y)
        
        # 更新当前节点
        self.seg[x].sum = 0
        if self.seg[x].l != -1:
            self.seg[x].sum = (self.seg[x].sum + self.seg[self.seg[x].l].sum) % self.MOD
        if self.seg[x].r != -1:
            self.seg[x].sum = (self.seg[x].sum + self.seg[self.seg[x].r].sum) % self.MOD
        
        return x
    
    def dfs(self, u):
        """DFS遍历树结构"""
        if self.tree[u][0] == 0 and self.tree[u][1] == 0:
            # 叶子节点，初始化线段树
            self.root[u] = self.new_node()
            pos = self.mapping[int(self.tree[u][2])]
            self.update(self.root[u], 1, len(self.vals), pos, 1)
            return
        
        # 递归处理左右子树
        self.dfs(self.tree[u][0])
        self.dfs(self.tree[u][1])
        
        # 合并左右子树的线段树
        self.root[u] = self.merge(self.root[self.tree[u][0]], 
                                 self.root[self.tree[u][1]], 
                                 self.tree[u][2], 0, 0)
    
    def collect_answer(self, p, l, r, ans):
        """收集答案"""
        if p == -1:
            return
        if l == r:
            ans.append(self.seg[p].sum)
            return
        
        self.push_down(p)
        mid = (l + r) // 2
        self.collect_answer(self.seg[p].l, l, mid, ans)
        self.collect_answer(self.seg[p].r, mid + 1, r, ans)
    
    def solve(self):
        io = FastIO()
        
        self.n = io.read_int()
        
        # 初始化树结构
        self.tree = [[0, 0, 0] for _ in range(self.n + 1)]  # [左儿子, 右儿子, 概率/权值]
        
        # 读取树结构
        fa_list = io.read_ints()
        for i in range(1, self.n + 1):
            fa = fa_list[i - 1]
            if fa != 0:
                if self.tree[fa][0] == 0:
                    self.tree[fa][0] = i
                else:
                    self.tree[fa][1] = i
        
        # 读取概率和权值
        val_list = io.read_ints()
        for i in range(1, self.n + 1):
            val = val_list[i - 1]
            if self.tree[i][0] == 0 and self.tree[i][1] == 0:
                # 叶子节点，存储权值
                self.tree[i][2] = val
                self.vals.append(val)
            else:
                # 非叶子节点，存储概率
                self.tree[i][2] = val * self.INV10000 / 10000.0
        
        # 离散化权值
        self.vals = sorted(set(self.vals))
        for idx, val in enumerate(self.vals, 1):
            self.mapping[val] = idx
        
        # 初始化线段树
        max_nodes = self.n * 40
        self.seg = [SegmentTreeNode() for _ in range(max_nodes)]
        self.root = [-1] * (self.n + 1)
        
        # 从根节点开始DFS
        self.dfs(1)
        
        # 收集答案
        ans = []
        self.collect_answer(self.root[1], 1, len(self.vals), ans)
        
        # 输出答案
        for prob in ans:
            io.print(int(prob))

if __name__ == "__main__":
    solution = Code18_P5298_Minimax()
    solution.solve()

"""
类似题目推荐:
1. P4556 [Vani有约会]雨天的尾巴 - 树上差分 + 线段树合并
2. P3224 [HNOI2012]永无乡 - 平衡树合并/线段树合并  
3. P6773 [NOI2020]命运 - 树形DP + 线段树合并
4. CF911G Mass Change Queries - 线段树合并 + 映射维护
5. CF1401F Reverse and Swap - 线段树分裂经典应用

线段树分裂算法总结:
线段树分裂是线段树合并的逆操作，主要用于:
1. 将一棵线段树按照某种条件拆分成多棵
2. 支持区间分裂操作  
3. 与线段树合并配合实现复杂的数据结构

时间复杂度: O(log n) 每次分裂
空间复杂度: O(n log n)
"""