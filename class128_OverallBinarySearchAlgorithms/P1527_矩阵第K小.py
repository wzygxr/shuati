# P1527 [国家集训队] 矩阵乘法 / 矩阵第K小 - Python实现
# 题目来源：https://www.luogu.com.cn/problem/P1527
# 时间复杂度：O(N² * logN * log(maxValue) + Q * logN * log(maxValue))
# 空间复杂度：O(N² + Q)

import sys

class MatrixKth:
    def __init__(self):
        self.MAXN = 501
        self.MAXQ = 1000001
        self.n = 0
        self.q = 0

        # 矩阵中的每个数字，所在行x、所在列y、数值v
        self.xyv = [[0, 0, 0] for _ in range(self.MAXN * self.MAXN)]
        # 矩阵中一共有多少个数字，cntv就是矩阵的规模
        self.cntv = 0

        # 查询任务的编号
        self.qid = [0] * self.MAXQ
        # 查询范围的左上角坐标
        self.a = [0] * self.MAXQ
        self.b = [0] * self.MAXQ
        # 查询范围的右下角坐标
        self.c = [0] * self.MAXQ
        self.d = [0] * self.MAXQ
        # 查询矩阵内第k小
        self.k = [0] * self.MAXQ

        # 二维树状数组
        self.tree = [[0] * self.MAXN for _ in range(self.MAXN)]

        # 整体二分
        self.lset = [0] * self.MAXQ
        self.rset = [0] * self.MAXQ

        # 每条查询的答案
        self.ans = [0] * self.MAXQ

    def lowbit(self, i):
        return i & -i

    # 二维空间中，(x,y)位置的词频加v
    def add(self, x, y, v):
        i = x
        while i <= self.n:
            j = y
            while j <= self.n:
                self.tree[i][j] += v
                j += self.lowbit(j)
            i += self.lowbit(i)

    # 二维空间中，左上角(1,1)到右下角(x,y)范围上的词频累加和
    def sum(self, x, y):
        ret = 0
        i = x
        while i > 0:
            j = y
            while j > 0:
                ret += self.tree[i][j]
                j -= self.lowbit(j)
            i -= self.lowbit(i)
        return ret

    # 二维空间中，左上角(a,b)到右下角(c,d)范围上的词频累加和
    def query(self, a, b, c, d):
        return self.sum(c, d) - self.sum(a - 1, d) - self.sum(c, b - 1) + self.sum(a - 1, b - 1)

    def compute(self, ql, qr, vl, vr):
        if ql > qr:
            return
        if vl == vr:
            for i in range(ql, qr + 1):
                self.ans[self.qid[i]] = self.xyv[vl][2]
        else:
            mid = (vl + vr) >> 1
            for i in range(vl, mid + 1):
                self.add(self.xyv[i][0], self.xyv[i][1], 1)
            lsiz = 0
            rsiz = 0
            for i in range(ql, qr + 1):
                id = self.qid[i]
                satisfy = self.query(self.a[id], self.b[id], self.c[id], self.d[id])
                if satisfy >= self.k[id]:
                    lsiz += 1
                    self.lset[lsiz] = id
                else:
                    self.k[id] -= satisfy
                    rsiz += 1
                    self.rset[rsiz] = id
            for i in range(1, lsiz + 1):
                self.qid[ql + i - 1] = self.lset[i]
            for i in range(1, rsiz + 1):
                self.qid[ql + lsiz + i - 1] = self.rset[i]
            for i in range(vl, mid + 1):
                self.add(self.xyv[i][0], self.xyv[i][1], -1)
            self.compute(ql, ql + lsiz - 1, vl, mid)
            self.compute(ql + lsiz, qr, mid + 1, vr)

    def solve(self):
        line = sys.stdin.readline().split()
        self.n = int(line[0])
        self.q = int(line[1])
        
        for i in range(1, self.n + 1):
            row = sys.stdin.readline().split()
            for j in range(1, self.n + 1):
                self.cntv += 1
                self.xyv[self.cntv][0] = i
                self.xyv[self.cntv][1] = j
                self.xyv[self.cntv][2] = int(row[j - 1])
        
        for i in range(1, self.q + 1):
            query_line = sys.stdin.readline().split()
            self.qid[i] = i
            self.a[i] = int(query_line[0])
            self.b[i] = int(query_line[1])
            self.c[i] = int(query_line[2])
            self.d[i] = int(query_line[3])
            self.k[i] = int(query_line[4])
        
        # 按照数值排序
        self.xyv[1:self.cntv+1] = sorted(self.xyv[1:self.cntv+1], key=lambda x: x[2])
        
        self.compute(1, self.q, 1, self.cntv)
        
        for i in range(1, self.q + 1):
            print(self.ans[i])

# 程序入口
#if __name__ == "__main__":
#    solution = MatrixKth()
#    solution.solve()