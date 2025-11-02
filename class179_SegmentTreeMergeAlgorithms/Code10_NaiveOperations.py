# Naive Operations - HDU 6315
# 测试链接 : https://acm.hdu.edu.cn/showproblem.php?pid=6315

import sys
import threading

# 由于Python递归深度限制，我们使用迭代方式实现
sys.setrecursionlimit(1000000)

class SegmentTree:
    def __init__(self, n):
        self.n = n
        self.tree = [0] * (n << 2)  # 区间和
        self.cnt = [0] * (n << 2)   # 区间最小值
        self.col = [0] * (n << 2)   # 延迟标记
        self.b = [0] * (n + 1)      # b数组
    
    def push_up(self, rt):
        """更新节点信息"""
        self.cnt[rt] = min(self.cnt[rt << 1], self.cnt[rt << 1 | 1])
        self.tree[rt] = self.tree[rt << 1] + self.tree[rt << 1 | 1]
    
    def push_down(self, rt):
        """下传标记"""
        if self.col[rt] != 0:
            self.cnt[rt << 1] -= self.col[rt]
            self.cnt[rt << 1 | 1] -= self.col[rt]
            self.col[rt << 1] += self.col[rt]
            self.col[rt << 1 | 1] += self.col[rt]
            self.col[rt] = 0
    
    def build(self, l, r, rt):
        """建树"""
        if l == r:
            self.cnt[rt] = self.b[l]
            self.tree[rt] = self.col[rt] = 0
            return
        
        mid = (l + r) >> 1
        self.build(l, mid, rt << 1)
        self.build(mid + 1, r, rt << 1 | 1)
        self.push_up(rt)
    
    def update(self, L, R, temp, l, r, rt):
        """更新操作"""
        if temp == 1:
            if l == r:
                self.cnt[rt] = self.b[l]
                self.tree[rt] += 1
                return
            
            self.push_down(rt)
            mid = (l + r) >> 1
            if L <= mid:
                self.update(L, R, 1 if self.cnt[rt << 1] == 1 else 0, l, mid, rt << 1)
            if R > mid:
                self.update(L, R, 1 if self.cnt[rt << 1 | 1] == 1 else 0, mid + 1, r, rt << 1 | 1)
        else:
            if L <= l and r <= R:
                self.cnt[rt] -= 1
                self.col[rt] += 1
                return
            
            self.push_down(rt)
            mid = (l + r) >> 1
            if L <= mid:
                self.update(L, R, 0, l, mid, rt << 1)
            if R > mid:
                self.update(L, R, 0, mid + 1, r, rt << 1 | 1)
        self.push_up(rt)
    
    def query(self, L, R, l, r, rt):
        """查询操作"""
        if L <= l and r <= R:
            return self.tree[rt]
        
        self.push_down(rt)
        mid = (l + r) >> 1
        ret = 0
        if L <= mid:
            ret += self.query(L, R, l, mid, rt << 1)
        if R > mid:
            ret += self.query(L, R, mid + 1, r, rt << 1 | 1)
        return ret

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    while idx < len(data):
        n = int(data[idx])
        idx += 1
        m = int(data[idx])
        idx += 1
        
        seg_tree = SegmentTree(n)
        
        for i in range(1, n + 1):
            seg_tree.b[i] = int(data[idx])
            idx += 1
        
        seg_tree.build(1, n, 1)
        
        for i in range(m):
            op = data[idx]
            idx += 1
            l = int(data[idx])
            idx += 1
            r = int(data[idx])
            idx += 1
            
            if op[0] == 'a':
                seg_tree.update(l, r, 1 if seg_tree.cnt[1] == 1 else 0, 1, n, 1)
            else:
                result = seg_tree.query(l, r, 1, n, 1)
                print(result)

# 由于Python的递归限制，使用线程来增加递归深度
threading.Thread(target=main).start()