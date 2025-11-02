# P3527 [POI2011] MET-Meteors - 陨石雨 - Python实现
# 题目来源：https://www.luogu.com.cn/problem/P3527
# 时间复杂度：O(K * logK * logM)
# 空间复杂度：O(N + M + K)

import sys

class MeteorRain:
    def __init__(self):
        self.MAXN = 300001
        self.n = 0
        self.m = 0
        self.k = 0

        # 国家编号
        self.qid = [0] * self.MAXN
        # 国家的需求
        self.need = [0] * self.MAXN

        # 陨石雨的参数
        self.rainl = [0] * self.MAXN
        self.rainr = [0] * self.MAXN
        self.num = [0] * self.MAXN

        # 国家拥有的区域列表
        self.head = [0] * self.MAXN
        self.next = [0] * self.MAXN
        self.to = [0] * self.MAXN
        self.cnt = 0

        # 树状数组，支持范围修改、单点查询
        self.tree = [0] * (self.MAXN << 1)

        # 整体二分
        self.lset = [0] * self.MAXN
        self.rset = [0] * self.MAXN

        # 每个国家的答案
        self.ans = [0] * self.MAXN

    def addEdge(self, i, v):
        self.cnt += 1
        self.next[self.cnt] = self.head[i]
        self.to[self.cnt] = v
        self.head[i] = self.cnt

    def lowbit(self, i):
        return i & -i

    def add(self, i, v):
        siz = self.m * 2
        while i <= siz:
            self.tree[i] += v
            i += self.lowbit(i)

    def add_range(self, l, r, v):
        self.add(l, v)
        self.add(r + 1, -v)

    def query(self, i):
        ret = 0
        while i > 0:
            ret += self.tree[i]
            i -= self.lowbit(i)
        return ret

    def compute(self, ql, qr, vl, vr):
        if ql > qr:
            return
        if vl == vr:
            for i in range(ql, qr + 1):
                self.ans[self.qid[i]] = vl
        else:
            mid = (vl + vr) >> 1
            for i in range(vl, mid + 1):
                self.add_range(self.rainl[i], self.rainr[i], self.num[i])
            lsiz = 0
            rsiz = 0
            for i in range(ql, qr + 1):
                id = self.qid[i]
                satisfy = 0
                e = self.head[id]
                while e > 0:
                    satisfy += self.query(self.to[e]) + self.query(self.to[e] + self.m)
                    if satisfy >= self.need[id]:
                        break
                    e = self.next[e]
                if satisfy >= self.need[id]:
                    lsiz += 1
                    self.lset[lsiz] = id
                else:
                    self.need[id] -= satisfy
                    rsiz += 1
                    self.rset[rsiz] = id
            for i in range(1, lsiz + 1):
                self.qid[ql + i - 1] = self.lset[i]
            for i in range(1, rsiz + 1):
                self.qid[ql + lsiz + i - 1] = self.rset[i]
            for i in range(vl, mid + 1):
                self.add_range(self.rainl[i], self.rainr[i], -self.num[i])
            self.compute(ql, ql + lsiz - 1, vl, mid)
            self.compute(ql + lsiz, qr, mid + 1, vr)

    def solve(self):
        line = sys.stdin.readline().split()
        self.n = int(line[0])
        self.m = int(line[1])
        
        for i in range(1, self.m + 1):
            nation = int(sys.stdin.readline())
            self.addEdge(nation, i)
        
        for i in range(1, self.n + 1):
            self.qid[i] = i
            self.need[i] = int(sys.stdin.readline())
        
        self.k = int(sys.stdin.readline())
        
        for i in range(1, self.k + 1):
            rain = sys.stdin.readline().split()
            self.rainl[i] = int(rain[0])
            self.rainr[i] = int(rain[1])
            if self.rainr[i] < self.rainl[i]:
                self.rainr[i] += self.m
            self.num[i] = int(rain[2])
        
        # 答案范围[1..k+1]，第k+1场陨石雨认为满足不了要求
        self.compute(1, self.n, 1, self.k + 1)
        
        for i in range(1, self.n + 1):
            if self.ans[i] == self.k + 1:
                print("NIE")
            else:
                print(self.ans[i])

# 程序入口
#if __name__ == "__main__":
#    solution = MeteorRain()
#    solution.solve()