# 测试链接 : https://www.luogu.com.cn/problem/P5494
# 线段树分裂模板题 - Python实现

import sys
import math
from typing import List, Tuple

class FastIO:
    def __init__(self):
        self.stdin = sys.stdin
        self.buffer = []
        
    def readline(self):
        while not self.buffer:
            self.buffer = self.stdin.readline().split()
        return self.buffer.pop(0)
    
    def readint(self):
        return int(self.readline())
    
    def readlong(self):
        return int(self.readline())

class SegmentTreeNode:
    """线段树节点类"""
    __slots__ = ('l', 'r', 'sum', 'cnt', 'left', 'right')
    
    def __init__(self):
        self.l = -1  # 左子节点索引
        self.r = -1  # 右子节点索引
        self.sum = 0  # 区间和
        self.cnt = 0  # 区间内元素个数
        self.left = None  # 左子节点
        self.right = None  # 右子节点

class SegmentTreeSplit:
    """线段树分裂算法实现"""
    
    def __init__(self, maxn: int = 200010, maxm: int = 20000000):
        self.maxn = maxn
        self.maxm = maxm
        self.nodes = []  # 节点池
        self.roots = {}  # 各个序列的根节点
        self.node_cnt = 0  # 节点计数器
        self.seq_cnt = 1   # 序列计数器
        
        # 预分配节点
        for _ in range(maxm):
            self.nodes.append(SegmentTreeNode())
    
    def new_node(self) -> int:
        """创建新节点"""
        if self.node_cnt >= self.maxm:
            # 内存不足时进行垃圾回收
            import gc
            gc.collect()
            return -1
        
        node = self.nodes[self.node_cnt]
        node.left = node.right = None
        node.sum = node.cnt = 0
        self.node_cnt += 1
        return self.node_cnt - 1
    
    def push_up(self, rt: int) -> None:
        """更新节点信息"""
        if rt == -1:
            return
        
        node = self.nodes[rt]
        node.sum = 0
        node.cnt = 0
        
        if node.left is not None:
            left_node = self.nodes[node.l]
            node.sum += left_node.sum
            node.cnt += left_node.cnt
        
        if node.right is not None:
            right_node = self.nodes[node.r]
            node.sum += right_node.sum
            node.cnt += right_node.cnt
    
    def update(self, rt: int, l: int, r: int, pos: int, val: int) -> None:
        """单点更新"""
        if l == r:
            node = self.nodes[rt]
            node.sum += val
            node.cnt += 1
            return
        
        mid = (l + r) // 2
        node = self.nodes[rt]
        
        if pos <= mid:
            if node.left is None:
                new_rt = self.new_node()
                node.l = new_rt
                node.left = self.nodes[new_rt]
            self.update(node.l, l, mid, pos, val)
        else:
            if node.right is None:
                new_rt = self.new_node()
                node.r = new_rt
                node.right = self.nodes[new_rt]
            self.update(node.r, mid + 1, r, pos, val)
        
        self.push_up(rt)
    
    def split(self, p: int, q: int, l: int, r: int, L: int, R: int) -> None:
        """线段树分裂操作"""
        if p == -1:
            return
        
        if L > r or R < l:
            return
        
        if L >= l and R <= r:
            # 整个区间需要分裂
            if q == -1:
                q = self.new_node()
            
            # 复制节点信息
            p_node = self.nodes[p]
            q_node = self.nodes[q]
            
            q_node.l = p_node.l
            q_node.r = p_node.r
            q_node.sum = p_node.sum
            q_node.cnt = p_node.cnt
            q_node.left = p_node.left
            q_node.right = p_node.right
            
            # 清空原节点
            p_node.l = p_node.r = -1
            p_node.sum = p_node.cnt = 0
            p_node.left = p_node.right = None
            return
        
        mid = (L + R) // 2
        p_node = self.nodes[p]
        q_node = self.nodes[q]
        
        if p_node.left is not None and l <= mid:
            if q_node.left is None:
                new_rt = self.new_node()
                q_node.l = new_rt
                q_node.left = self.nodes[new_rt]
            self.split(p_node.l, q_node.l, l, r, L, mid)
        
        if p_node.right is not None and r > mid:
            if q_node.right is None:
                new_rt = self.new_node()
                q_node.r = new_rt
                q_node.right = self.nodes[new_rt]
            self.split(p_node.r, q_node.r, l, r, mid + 1, R)
        
        self.push_up(p)
        self.push_up(q)
    
    def merge(self, p: int, q: int, l: int, r: int) -> int:
        """线段树合并操作"""
        if p == -1:
            return q
        if q == -1:
            return p
        
        if l == r:
            p_node = self.nodes[p]
            q_node = self.nodes[q]
            p_node.sum += q_node.sum
            p_node.cnt += q_node.cnt
            return p
        
        mid = (l + r) // 2
        p_node = self.nodes[p]
        q_node = self.nodes[q]
        
        if p_node.left is not None and q_node.left is not None:
            p_node.l = self.merge(p_node.l, q_node.l, l, mid)
        elif q_node.left is not None:
            p_node.l = q_node.l
            p_node.left = q_node.left
        
        if p_node.right is not None and q_node.right is not None:
            p_node.r = self.merge(p_node.r, q_node.r, mid + 1, r)
        elif q_node.right is not None:
            p_node.r = q_node.r
            p_node.right = q_node.right
        
        self.push_up(p)
        return p
    
    def query_kth(self, rt: int, l: int, r: int, k: int) -> int:
        """查询区间第k小"""
        if l == r:
            return l
        
        mid = (l + r) // 2
        node = self.nodes[rt]
        
        left_cnt = 0
        if node.left is not None:
            left_cnt = self.nodes[node.l].cnt
        
        if k <= left_cnt:
            return self.query_kth(node.l, l, mid, k)
        else:
            return self.query_kth(node.r, mid + 1, r, k - left_cnt)
    
    def query_sum(self, rt: int, l: int, r: int, L: int, R: int) -> int:
        """查询区间和"""
        if rt == -1 or L > r or R < l:
            return 0
        
        if L >= l and R <= r:
            return self.nodes[rt].sum
        
        mid = (L + R) // 2
        node = self.nodes[rt]
        res = 0
        
        if node.left is not None:
            res += self.query_sum(node.l, l, r, L, mid)
        if node.right is not None:
            res += self.query_sum(node.r, l, r, mid + 1, R)
        
        return res

def main():
    io = FastIO()
    
    n = io.readint()
    m = io.readint()
    
    # 创建线段树分裂实例
    st = SegmentTreeSplit()
    
    # 初始化根节点
    st.roots[1] = st.new_node()
    
    # 读入初始序列
    for i in range(1, n + 1):
        x = io.readlong()
        st.update(st.roots[1], 1, n, i, x)
    
    results = []
    
    for _ in range(m):
        op = io.readint()
        
        if op == 0:
            # 分裂操作
            p = io.readint()
            l = io.readint()
            r = io.readint()
            
            st.seq_cnt += 1
            st.roots[st.seq_cnt] = st.new_node()
            st.split(st.roots[p], st.roots[st.seq_cnt], l, r, 1, n)
            
        elif op == 1:
            # 合并操作
            p = io.readint()
            q = io.readint()
            st.roots[p] = st.merge(st.roots[p], st.roots[q], 1, n)
            st.roots[q] = -1
            
        elif op == 2:
            # 插入操作
            p = io.readint()
            x = io.readlong()
            pos = st.nodes[st.roots[p]].cnt + 1
            st.update(st.roots[p], 1, n, pos, x)
            
        elif op == 3:
            # 查询区间和
            p = io.readint()
            l = io.readint()
            r = io.readint()
            sum_val = st.query_sum(st.roots[p], l, r, 1, n)
            results.append(str(sum_val))
            
        elif op == 4:
            # 查询第k小
            p = io.readint()
            k = io.readint()
            if st.nodes[st.roots[p]].cnt < k:
                results.append("-1")
            else:
                kth = st.query_kth(st.roots[p], 1, n, k)
                results.append(str(kth))
    
    # 输出结果
    print("\n".join(results))

if __name__ == "__main__":
    main()

"""
线段树分裂算法详解：

1. 算法特点：
   - 支持动态序列的分裂和合并
   - 使用动态开点节省空间
   - 时间复杂度：O(n log n)

2. 核心操作流程：
   - 分裂：将原线段树的指定区间分离出来
   - 合并：将两个线段树合并成一个
   - 查询：支持区间和、第k小等操作

3. 应用场景：
   - 序列操作问题
   - 区间管理
   - 可持久化数据结构

4. 类似题目：
   - P4556 [Vani有约会]雨天的尾巴
   - P3224 [HNOI2012]永无乡
   - P5298 [PKUWC2018]Minimax
   - CF911G Mass Change Queries
   - P6773 [NOI2020]命运
"""