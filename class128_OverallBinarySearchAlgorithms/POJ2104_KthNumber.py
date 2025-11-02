# POJ 2104 K-th Number - Python实现
# 题目来源：http://poj.org/problem?id=2104
# 题目描述：给定一个长度为n的数组，有m个查询，每个查询要求在指定区间内找到第k小的数
#
# 解题思路：使用整体二分算法，将所有查询一起处理，二分答案的值域，利用树状数组维护区间内小于等于mid的元素个数
# 时间复杂度：O((N+Q) * logN * log(maxValue))
# 空间复杂度：O(N + Q)
#
# 算法适用条件：
# 1. 询问的答案具有可二分性
# 2. 修改对判定答案的贡献互相独立
# 3. 修改如果对判定答案有贡献，则贡献为确定值
# 4. 贡献满足交换律、结合律，具有可加性
# 5. 题目允许离线操作
#
# 工程化考量：
# - 数据结构选择：使用树状数组实现前缀和查询
# - Python特有优化：使用列表预先分配空间，避免频繁动态扩展
# - 边界处理：注意Python中的索引从0开始的特性，但保持与其他语言版本一致，使用1-based索引
# - 性能优化：使用离散化减少计算量，避免大数值操作

import sys

class POJ2104_KthNumber:
    def __init__(self):
        self.MAXN = 100001  # 定义数组最大长度
        self.n = 0  # 数组长度
        self.m = 0  # 查询次数
        self.arr = [0] * (self.MAXN)  # 原始数组
        self.sorted = [0] * (self.MAXN)  # 离散化后的数组
        self.queryL = [0] * (self.MAXN)  # 查询区间左端点
        self.queryR = [0] * (self.MAXN)  # 查询区间右端点
        self.queryK = [0] * (self.MAXN)  # 查询第k小
        self.queryId = [0] * (self.MAXN)  # 查询编号
        self.tree = [0] * (self.MAXN)  # 树状数组
        self.lset = [0] * (self.MAXN)  # 满足条件的查询
        self.rset = [0] * (self.MAXN)  # 不满足条件的查询
        self.ans = [0] * (self.MAXN)  # 查询的答案
    
    def lowbit(self, i):
        """
        计算一个数的lowbit值
        功能：返回二进制表示中最低位的1所代表的数值
        例如：lowbit(6) = lowbit(110) = 2
        时间复杂度：O(1)
        """
        return i & -i
    
    def add(self, i, v):
        """
        在树状数组中给位置i增加v
        功能：更新树状数组中的值，用于后续前缀和查询
        时间复杂度：O(logN)
        """
        while i <= self.n:
            self.tree[i] += v
            i += self.lowbit(i)
    
    def sum(self, i):
        """
        计算前缀和[1..i]
        功能：计算从1到i的元素和
        时间复杂度：O(logN)
        """
        ret = 0
        while i > 0:
            ret += self.tree[i]
            i -= self.lowbit(i)
        return ret
    
    def query(self, l, r):
        """
        计算区间和[l..r]
        功能：计算从l到r的元素和
        时间复杂度：O(logN)
        """
        return self.sum(r) - self.sum(l - 1)
    
    def compute(self, ql, qr, vl, vr):
        """
        整体二分核心函数
        功能：递归地对值域进行二分，并将查询分类处理
        参数：
            ql: 查询范围的左端点
            qr: 查询范围的右端点
            vl: 值域范围的左端点（离散化后的下标）
            vr: 值域范围的右端点（离散化后的下标）
        时间复杂度：O(log(maxValue))
        """
        # 递归边界1：没有查询需要处理
        if ql > qr:
            return
        
        # 递归边界2：如果值域范围只有一个值，说明找到了答案
        if vl == vr:
            for i in range(ql, qr + 1):
                self.ans[self.queryId[i]] = self.sorted[vl]
            return
        
        # 二分中点，将值域划分为左右两部分
        mid = (vl + vr) >> 1
        
        # 预处理：为树状数组添加贡献
        # 记录添加的位置，用于后续撤销操作
        positions = []
        for j in range(1, self.n + 1):
            if self.arr[j] <= self.sorted[mid]:
                self.add(j, 1)
                positions.append(j)
        
        # 检查每个查询，根据满足条件的元素个数划分到左右区间
        lsiz = 0
        rsiz = 0
        for i in range(ql, qr + 1):
            id = self.queryId[i]
            # 查询区间[self.queryL[id], self.queryR[id]]中值小于等于self.sorted[mid]的元素个数
            satisfy = self.query(self.queryL[id], self.queryR[id])
            
            if satisfy >= self.queryK[id]:
                # 说明第k小的数在左半部分值域
                lsiz += 1
                self.lset[lsiz] = id
            else:
                # 说明第k小的数在右半部分值域，需要在右半部分找第(k-satisfy)小的数
                self.queryK[id] -= satisfy
                rsiz += 1
                self.rset[rsiz] = id
        
        # 撤销对树状数组的修改，恢复到处理前的状态
        for pos in positions:
            self.add(pos, -1)
        
        # 保存当前查询ID数组的临时副本
        temp = self.queryId.copy()
        
        # 重新排列查询顺序，使得左集合的查询在前，右集合的查询在后
        for i in range(1, lsiz + 1):
            self.queryId[ql + i - 1] = self.lset[i]
        for i in range(1, rsiz + 1):
            self.queryId[ql + lsiz + i - 1] = self.rset[i]
        
        # 递归处理左右两部分
        # 左半部分：值域在[vl, mid]范围内的查询
        self.compute(ql, ql + lsiz - 1, vl, mid)
        # 右半部分：值域在[mid+1, vr]范围内的查询
        self.compute(ql + lsiz, qr, mid + 1, vr)
    
    def main(self):
        """
        主函数，处理输入输出并调用整体二分算法
        工程化特点：
        - 使用sys.stdin.readline提高输入效率
        - 进行离散化处理减少计算量
        - 注意Python中的边界条件处理
        """
        # 读取数组长度和查询次数
        input_line = sys.stdin.readline()
        self.n, self.m = map(int, input_line.split())
        
        # 读取原始数组
        nums = list(map(int, sys.stdin.readline().split()))
        for i in range(1, self.n + 1):
            self.arr[i] = nums[i - 1]
            self.sorted[i] = self.arr[i]
        
        # 读取查询
        for i in range(1, self.m + 1):
            query = list(map(int, sys.stdin.readline().split()))
            self.queryL[i] = query[0]
            self.queryR[i] = query[1]
            self.queryK[i] = query[2]
            self.queryId[i] = i  # 记录查询编号
        
        # 离散化：将大值域映射到小下标范围，减少二分的值域范围
        self.sorted[1:self.n+1].sort()
        uniqueCount = 1
        for i in range(2, self.n + 1):
            if self.sorted[i] != self.sorted[i - 1]:
                uniqueCount += 1
                self.sorted[uniqueCount] = self.sorted[i]
        
        # 整体二分求解
        # 初始查询范围[1, m]，初始值域范围[1, uniqueCount]
        self.compute(1, self.m, 1, uniqueCount)
        
        # 输出结果
        for i in range(1, self.m + 1):
            print(self.ans[i])

# 程序入口
if __name__ == "__main__":
    solution = POJ2104_KthNumber()
    solution.main()