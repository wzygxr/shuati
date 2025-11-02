# P3332 [ZJOI2013] K大数查询 - Python实现
# 题目来源：https://www.luogu.com.cn/problem/P3332
# 时间复杂度：O(M * logN * log(maxValue))
# 空间复杂度：O(N + M)

import sys

class KthLargestQuery:
    def __init__(self):
        self.MAXN = 50001
        self.MAXM = 50001
        self.n = 0
        self.m = 0

        # 事件编号组成的数组
        self.eid = [0] * (self.MAXM << 1)
        # op == 1，代表修改事件，区间[L,R]增加值val
        # op == 2，代表查询事件，[L,R]范围上查询第k大，q表示问题的编号
        self.op = [0] * (self.MAXM << 1)
        self.L = [0] * (self.MAXM << 1)
        self.R = [0] * (self.MAXM << 1)
        self.val = [0] * (self.MAXM << 1)
        self.k = [0] * (self.MAXM << 1)
        self.q = [0] * (self.MAXM << 1)
        self.cnte = 0

        # 树状数组，支持区间修改、单点查询
        self.tree = [0] * (self.MAXN << 1)

        # 整体二分
        self.lset = [0] * (self.MAXM << 1)
        self.rset = [0] * (self.MAXM << 1)

        # 查询的答案
        self.ans = [0] * self.MAXN

    def lowbit(self, i):
        return i & -i

    def add(self, i, v):
        siz = self.n
        while i <= siz:
            self.tree[i] += v
            i += self.lowbit(i)

    # 区间加法 [l, r] += v
    def add_range(self, l, r, v):
        self.add(l, v)
        self.add(r + 1, -v)

    def query(self, i):
        ret = 0
        while i > 0:
            ret += self.tree[i]
            i -= self.lowbit(i)
        return ret

    def compute(self, el, er, vl, vr):
        if el > er:
            return
        if vl == vr:
            for i in range(el, er + 1):
                id = self.eid[i]
                if self.op[id] == 2:
                    self.ans[self.q[id]] = vl
        else:
            mid = (vl + vr) >> 1
            lsiz = 0
            rsiz = 0
            for i in range(el, er + 1):
                id = self.eid[i]
                if self.op[id] == 1:
                    # 修改操作
                    if self.val[id] <= mid:
                        # 对左半区间有贡献，执行修改
                        self.add_range(self.L[id], self.R[id], 1)
                        lsiz += 1
                        self.lset[lsiz] = id
                    else:
                        # 对左半区间无贡献，放入右半区间
                        rsiz += 1
                        self.rset[rsiz] = id
                else:
                    # 查询操作
                    satisfy = self.query(self.R[id]) - self.query(self.L[id] - 1)
                    if satisfy >= self.k[id]:
                        # 说明第k大的数在左半部分
                        lsiz += 1
                        self.lset[lsiz] = id
                    else:
                        # 说明第k大的数在右半部分
                        self.k[id] -= satisfy
                        rsiz += 1
                        self.rset[rsiz] = id
            
            # 撤销对树状数组的修改
            for i in range(1, lsiz + 1):
                id = self.lset[i]
                if self.op[id] == 1 and self.val[id] <= (vl + vr) >> 1:
                    self.add_range(self.L[id], self.R[id], -1)
            
            # 重新排列事件顺序
            for i in range(1, lsiz + 1):
                self.eid[el + i - 1] = self.lset[i]
            for i in range(1, rsiz + 1):
                self.eid[el + lsiz + i - 1] = self.rset[i]
            
            # 递归处理左右两部分
            self.compute(el, el + lsiz - 1, vl, mid)
            self.compute(el + lsiz, er, mid + 1, vr)

    def solve(self):
        line = sys.stdin.readline().split()
        self.n = int(line[0])
        self.m = int(line[1])
        
        min_val = float('inf')
        max_val = float('-inf')
        
        for i in range(1, self.m + 1):
            event = sys.stdin.readline().split()
            self.eid[i] = i
            self.op[i] = int(event[0])
            self.L[i] = int(event[1])
            self.R[i] = int(event[2])
            
            if self.op[i] == 1:
                # 修改操作
                self.val[i] = int(event[3])
                min_val = min(min_val, self.val[i])
                max_val = max(max_val, self.val[i])
            else:
                # 查询操作
                self.k[i] = int(event[3])
                self.q[i] = i
        
        # 整体二分求解
        self.compute(1, self.m, min_val, max_val)
        
        # 输出结果
        for i in range(1, self.m + 1):
            if self.op[i] == 2:
                print(self.ans[self.q[i]])

# 程序入口
#if __name__ == "__main__":
#    solution = KthLargestQuery()
#    solution.solve()