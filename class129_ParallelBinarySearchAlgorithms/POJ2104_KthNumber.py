"""
POJ 2104 K-th Number - Python实现
题目来源：http://poj.org/problem?id=2104
题目描述：静态区间第k小查询

问题描述：
给定一个长度为n的数组，有m个查询，每个查询要求在指定区间内找到第k小的数。

解题思路：
使用整体二分处理静态区间第k小问题。将所有查询一起处理，二分答案的值域，
利用树状数组维护区间内小于等于mid的元素个数。

时间复杂度：O((N+Q) * logN * log(maxValue))
空间复杂度：O(N + Q)
"""

class POJ2104_KthNumberSolution:
    """
    POJ 2104 K-th Number问题的解决方案类
    使用整体二分算法解决静态区间第k小查询问题
    """
    
    def __init__(self):
        """
        初始化解决方案类的成员变量
        """
        self.MAXN = 100001
        self.n = 0
        self.m = 0
        
        # 原始数组
        self.arr = [0] * self.MAXN
        
        # 离散化数组
        self.sorted = [0] * self.MAXN
        
        # 查询信息
        self.queryL = [0] * self.MAXN  # 查询区间左端点
        self.queryR = [0] * self.MAXN  # 查询区间右端点
        self.queryK = [0] * self.MAXN  # 查询第k小
        self.queryId = [0] * self.MAXN # 查询编号
        
        # 树状数组
        self.tree = [0] * self.MAXN
        
        # 整体二分
        self.lset = [0] * self.MAXN  # 左集合
        self.rset = [0] * self.MAXN  # 右集合
        
        # 查询的答案
        self.ans = [0] * self.MAXN
    
    def lowbit(self, i):
        """
        计算一个数的lowbit值
        :param i: 输入的数
        :return: lowbit值
        """
        return i & -i
    
    def add(self, i, v):
        """
        在树状数组中给位置i增加v
        :param i: 位置
        :param v: 增加的值
        """
        while i <= self.n:
            self.tree[i] += v
            i += self.lowbit(i)
    
    def sum(self, i):
        """
        计算前缀和[1..i]
        :param i: 位置
        :return: 前缀和
        """
        ret = 0
        while i > 0:
            ret += self.tree[i]
            i -= self.lowbit(i)
        return ret
    
    def query(self, l, r):
        """
        计算区间和[l..r]
        :param l: 左端点
        :param r: 右端点
        :return: 区间和
        """
        return self.sum(r) - self.sum(l - 1)
    
    def compute(self, ql, qr, vl, vr):
        """
        整体二分核心函数
        :param ql: 查询范围的左端点
        :param qr: 查询范围的右端点
        :param vl: 值域范围的左端点（离散化后的下标）
        :param vr: 值域范围的右端点（离散化后的下标）
        """
        # 递归边界
        if ql > qr:
            return
        
        # 如果值域范围只有一个值，说明找到了答案
        if vl == vr:
            for i in range(ql, qr + 1):
                self.ans[self.queryId[i]] = self.sorted[vl]
            return
        
        # 二分中点
        mid = (vl + vr) >> 1
        
        # 将值小于等于sorted[mid]的数加入树状数组
        for i in range(vl, mid + 1):
            # 遍历所有值为sorted[i]的元素，将其加入树状数组
            for j in range(1, self.n + 1):
                if self.arr[j] == self.sorted[i]:
                    self.add(j, 1)
        
        # 检查每个查询，根据满足条件的元素个数划分到左右区间
        lsiz = 0
        rsiz = 0
        for i in range(ql, qr + 1):
            # 查询区间[queryL[i], queryR[i]]中值小于等于sorted[mid]的元素个数
            satisfy = self.query(self.queryL[i], self.queryR[i])
            
            if satisfy >= self.queryK[i]:
                # 说明第k小的数在左半部分
                lsiz += 1
                self.lset[lsiz] = i
            else:
                # 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
                self.queryK[i] -= satisfy
                rsiz += 1
                self.rset[rsiz] = i
        
        # 重新排列查询顺序
        idx = ql
        for i in range(1, lsiz + 1):
            temp = self.lset[i]
            self.lset[i] = self.queryId[temp]
            self.queryId[idx] = temp
            idx += 1
        for i in range(1, rsiz + 1):
            temp = self.rset[i]
            self.rset[i] = self.queryId[temp]
            self.queryId[idx] = temp
            idx += 1
        
        # 撤销对树状数组的修改
        for i in range(vl, mid + 1):
            # 遍历所有值为sorted[i]的元素，将其从树状数组中删除
            for j in range(1, self.n + 1):
                if self.arr[j] == self.sorted[i]:
                    self.add(j, -1)
        
        # 递归处理左右两部分
        self.compute(ql, ql + lsiz - 1, vl, mid)
        self.compute(ql + lsiz, qr, mid + 1, vr)
    
    def solve(self):
        """
        解决POJ 2104 K-th Number问题的主函数
        """
        # 读取输入
        line = input().split()
        self.n = int(line[0])  # 数组长度
        self.m = int(line[1])  # 查询数量
        
        # 读取原始数组
        nums = input().split()
        for i in range(1, self.n + 1):
            self.arr[i] = int(nums[i - 1])
            self.sorted[i] = self.arr[i]
        
        # 读取查询
        for i in range(1, self.m + 1):
            query = input().split()
            self.queryL[i] = int(query[0])  # 查询区间左端点
            self.queryR[i] = int(query[1])  # 查询区间右端点
            self.queryK[i] = int(query[2])  # 查询第k小
            self.queryId[i] = i              # 查询编号
        
        # 离散化
        self.sorted[1:self.n + 1] = sorted(self.sorted[1:self.n + 1])
        uniqueCount = 1
        for i in range(2, self.n + 1):
            if self.sorted[i] != self.sorted[i - 1]:
                uniqueCount += 1
                self.sorted[uniqueCount] = self.sorted[i]
        
        # 整体二分求解
        self.compute(1, self.m, 1, uniqueCount)
        
        # 输出结果
        for i in range(1, self.m + 1):
            print(self.ans[i])


# 主程序
if __name__ == "__main__":
    """
    程序入口点
    创建解决方案实例并执行求解
    """
    solver = POJ2104_KthNumberSolution()
    solver.solve()