# CF603E Pastoral Oddities - Python实现
# 题目来源：https://www.luogu.com.cn/problem/CF603E
# 时间复杂度：O(M * logN * logM)
# 空间复杂度：O(N + M)
# 
# 题目大意：
# 给定一张图，每次加边后求一个边集，使得每个点度数为奇数且最大边权最小。
# 
# 解题思路：
# 1. 使用整体二分对边权进行二分
# 2. 使用可撤销并查集维护连通性
# 3. 检查所有连通块大小是否都是偶数
# 4. 根据统计结果将操作分为两类递归处理
# 
# 算法详解：
# 1. 整体二分：将所有操作一起处理，对边权进行二分，避免对每个查询单独二分
# 2. 可撤销并查集：支持合并操作的回滚，用于维护图的连通性
# 3. 度数检查：通过检查每个连通块的大小是否为偶数来判断是否存在满足条件的边集

import sys

class PastoralOddities:
    def __init__(self):
        self.MAXN = 100001
        self.MAXM = 300001
        self.n = 0
        self.m = 0

        # 边的信息
        self.u = [0] * self.MAXM
        self.v = [0] * self.MAXM
        self.w = [0] * self.MAXM
        
        # 查询编号
        self.qid = [0] * self.MAXM
        
        # 并查集
        self.father = [0] * self.MAXN
        self.size = [0] * self.MAXN
        self.stack = [0] * self.MAXN
        self.top = 0
        
        # 整体二分
        self.lset = [0] * self.MAXM
        self.rset = [0] * self.MAXM
        
        # 答案
        self.ans = [0] * self.MAXM
    
    # 初始化并查集
    # 将每个节点初始化为一个独立的集合
    def init(self):
        for i in range(1, self.n + 1):
            self.father[i] = i
            self.size[i] = 1
        self.top = 0
    
    # 查找根节点（带路径压缩）
    # 使用路径压缩优化，使查找操作的时间复杂度接近O(1)
    def find(self, x):
        while x != self.father[x]:
            x = self.father[x]
        return x
    
    # 合并两个集合
    # 使用按秩合并优化，将较小的树合并到较大的树上
    # 返回True表示成功合并，False表示已在同一集合中
    def union(self, x, y):
        fx = self.find(x)
        fy = self.find(y)
        if fx == fy:
            return False
        # 按秩合并，将较小的树合并到较大的树上
        if self.size[fx] < self.size[fy]:
            fx, fy = fy, fx
        self.father[fy] = fx
        self.size[fx] += self.size[fy]
        self.top += 1
        self.stack[self.top] = fy  # 记录修改，用于回滚
        return True
    
    # 检查所有连通块大小是否都是偶数
    # 根据题目要求，每个点的度数必须为奇数，这等价于每个连通块的大小为偶数
    def check(self):
        for i in range(1, self.n + 1):
            # 只检查根节点，避免重复计算
            if self.find(i) == i and (self.size[i] & 1) == 1:
                # 如果存在大小为奇数的连通块，则无法满足条件
                return False
        return True
    
    # 回滚操作
    # 将并查集的状态回滚到targetTop时刻
    def rollback(self, targetTop):
        while self.top > targetTop:
            y = self.stack[self.top]
            self.top -= 1
            fy = self.find(y)
            self.size[fy] -= self.size[y]
            self.father[y] = y
    
    # 整体二分核心函数
    # ql, qr: 当前处理的查询范围
    # vl, vr: 当前处理的值域范围（边权范围）
    def compute(self, ql, qr, vl, vr):
        # 递归边界：没有查询需要处理
        if ql > qr:
            return
        # 递归边界：值域范围只有一个值，找到了答案
        if vl == vr:
            for i in range(ql, qr + 1):
                self.ans[self.qid[i]] = vl
            return
        
        mid = (vl + vr) >> 1
        targetTop = self.top
        
        # 添加编号小于等于mid的边到并查集中
        for i in range(vl, mid + 1):
            self.union(self.u[i], self.v[i])
        
        # 检查每个查询，并根据结果将查询分为两类
        lsiz = 0
        rsiz = 0
        for i in range(ql, qr + 1):
            id = self.qid[i]
            # 检查当前状态是否满足条件
            if self.check():
                # 满足条件，答案可能更小，分到左区间
                lsiz += 1
                self.lset[lsiz] = id
            else:
                # 不满足条件，答案必须更大，分到右区间
                rsiz += 1
                self.rset[rsiz] = id
        
        # 重新排列查询顺序，使左区间的查询在前，右区间的查询在后
        for i in range(1, lsiz + 1):
            self.qid[ql + i - 1] = self.lset[i]
        for i in range(1, rsiz + 1):
            self.qid[ql + lsiz + i - 1] = self.rset[i]
        
        # 回滚操作，恢复到添加边之前的状态
        self.rollback(targetTop)
        
        # 递归处理左右两部分
        # 左区间：答案范围[vl, mid]
        self.compute(ql, ql + lsiz - 1, vl, mid)
        # 右区间：答案范围[mid+1, vr]
        self.compute(ql + lsiz, qr, mid + 1, vr)
    
    def solve(self):
        line = sys.stdin.readline().split()
        self.n = int(line[0])
        self.m = int(line[1])
        
        # 读取边信息
        edges = []
        for i in range(1, self.m + 1):
            edge = sys.stdin.readline().split()
            self.u[i] = int(edge[0])
            self.v[i] = int(edge[1])
            self.w[i] = int(edge[2])
            edges.append((self.w[i], self.u[i], self.v[i], i))
        
        # 按权重排序，这是整体二分的前提条件
        edges.sort()
        
        # 重新编号，使边按权重递增顺序排列
        for i in range(self.m):
            _, u, v, id = edges[i]
            self.u[i + 1] = u
            self.v[i + 1] = v
            self.w[i + 1] = edges[i][0]
            self.qid[i + 1] = id
        
        # 初始化并查集
        self.init()
        
        # 整体二分求解
        self.compute(1, self.m, 1, self.m)
        
        # 输出结果
        result = [0] * (self.m + 1)
        for i in range(1, self.m + 1):
            result[self.qid[i]] = self.ans[self.qid[i]]
        
        maxWeight = -1
        results = []
        for i in range(1, self.m + 1):
            if result[i] > 0:
                maxWeight = max(maxWeight, self.w[result[i]])
            if maxWeight > 0:
                results.append(str(maxWeight))
            else:
                results.append("-1")
        
        print("\n".join(results))

# 程序入口
#if __name__ == "__main__":
#    solution = PastoralOddities()
#    solution.solve()