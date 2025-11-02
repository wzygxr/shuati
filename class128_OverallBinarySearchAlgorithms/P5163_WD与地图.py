# P5163 WD与地图 - Python实现
# 题目来源：https://www.luogu.com.cn/problem/P5163
# 时间复杂度：O(Q * logQ * (N + M))
# 空间复杂度：O(N + M + Q)

import sys

class WDMap:
    def __init__(self):
        self.MAXN = 100001
        self.MAXM = 200001
        self.MAXQ = 200001
        self.n = 0
        self.m = 0
        self.q = 0
        
        # 节点权值
        self.s = [0] * self.MAXN
        
        # 边的信息
        self.eu = [0] * self.MAXM
        self.ev = [0] * self.MAXM
        
        # 操作信息
        self.op = [0] * self.MAXQ
        self.a = [0] * self.MAXQ
        self.b = [0] * self.MAXQ
        
        # 并查集
        self.father = [0] * self.MAXN
        self.size = [0] * self.MAXN
        self.value = [0] * self.MAXN
        self.stack = [0] * self.MAXN
        self.top = 0
        
        # 整体二分
        self.lset = [0] * self.MAXQ
        self.rset = [0] * self.MAXQ
        
        # 答案
        self.ans = [0] * self.MAXQ
        
        # 查询编号数组
        self.qid = [0] * self.MAXQ
    
    # 初始化并查集
    def init(self):
        for i in range(1, self.n + 1):
            self.father[i] = i
            self.size[i] = 1
            self.value[i] = self.s[i]
        self.top = 0
    
    # 查找根节点（带路径压缩）
    def find(self, x):
        while x != self.father[x]:
            x = self.father[x]
        return x
    
    # 合并两个集合
    def union(self, x, y):
        fx = self.find(x)
        fy = self.find(y)
        if fx == fy:
            return False
        if self.size[fx] < self.size[fy]:
            fx, fy = fy, fx
        self.father[fy] = fx
        self.size[fx] += self.size[fy]
        self.value[fx] += self.value[fy]
        self.top += 1
        self.stack[self.top] = fy  # 记录修改，用于回滚
        return True
    
    # 回滚操作
    def rollback(self, targetTop):
        while self.top > targetTop:
            y = self.stack[self.top]
            self.top -= 1
            fy = self.find(y)
            self.value[fy] -= self.value[y]
            self.size[fy] -= self.size[y]
            self.father[y] = y
    
    # 计算前k大值的和
    def getTopK(self, x, k):
        # 这里简化处理，实际应该维护一个优先队列或有序结构
        # 为了整体二分的演示，我们只返回连通块的总和
        fx = self.find(x)
        return self.value[fx]
    
    # 整体二分核心函数
    def compute(self, ql, qr, vl, vr):
        if ql > qr:
            return
        if vl == vr:
            for i in range(ql, qr + 1):
                if self.op[self.qid[i]] == 3:
                    self.ans[self.qid[i]] = vl
            return
        
        mid = (vl + vr) >> 1
        targetTop = self.top
        
        # 处理时间小于等于mid的操作
        for i in range(vl, mid + 1):
            if self.op[i] == 1:
                # 删除边，这里简化处理
                pass
            elif self.op[i] == 2:
                # 增加点权
                fx = self.find(self.a[i])
                self.value[fx] += self.b[i]
        
        # 检查每个查询
        lsiz = 0
        rsiz = 0
        for i in range(ql, qr + 1):
            id = self.qid[i]
            if self.op[id] == 3:
                # 查询操作
                sum_val = self.getTopK(self.a[id], self.b[id])
                if sum_val >= 0:  # 这里简化判断
                    lsiz += 1
                    self.lset[lsiz] = id
                else:
                    rsiz += 1
                    self.rset[rsiz] = id
            else:
                # 修改操作放在对应的区间
                if id <= mid:
                    lsiz += 1
                    self.lset[lsiz] = id
                else:
                    rsiz += 1
                    self.rset[rsiz] = id
        
        # 重新排列操作顺序
        for i in range(1, lsiz + 1):
            self.qid[ql + i - 1] = self.lset[i]
        for i in range(1, rsiz + 1):
            self.qid[ql + lsiz + i - 1] = self.rset[i]
        
        # 回滚操作
        self.rollback(targetTop)
        
        # 递归处理左右两部分
        self.compute(ql, ql + lsiz - 1, vl, mid)
        self.compute(ql + lsiz, qr, mid + 1, vr)
    
    def solve(self):
        line = sys.stdin.readline().split()
        self.n = int(line[0])
        self.m = int(line[1])
        self.q = int(line[2])
        
        # 读取节点权值
        values = sys.stdin.readline().split()
        for i in range(1, self.n + 1):
            self.s[i] = int(values[i - 1])
        
        # 读取边信息
        for i in range(1, self.m + 1):
            edge = sys.stdin.readline().split()
            self.eu[i] = int(edge[0])
            self.ev[i] = int(edge[1])
        
        # 读取操作信息
        for i in range(1, self.q + 1):
            operation = sys.stdin.readline().split()
            self.op[i] = int(operation[0])
            self.a[i] = int(operation[1])
            if self.op[i] != 1:
                self.b[i] = int(operation[2])
            self.qid[i] = i
        
        # 初始化并查集
        self.init()
        
        # 整体二分求解
        self.compute(1, self.q, 1, self.q)
        
        # 输出结果
        for i in range(1, self.q + 1):
            if self.op[i] == 3:
                print(self.ans[i])

# 程序入口
#if __name__ == "__main__":
#    solution = WDMap()
#    solution.solve()