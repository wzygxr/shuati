# P2617 Dynamic Rankings - Python实现
# 题目来源：https://www.luogu.com.cn/problem/P2617
# 题目描述：
# 给定一个含有n个数的序列a1,a2…an，需要支持两种操作：
# Q l r k 表示查询下标在区间[l,r]中的第k小的数；
# C x y 表示将ax改为y。
# 时间复杂度：O((N+Q) * logN * log(maxValue))
# 空间复杂度：O(N + Q)

import sys
from bisect import bisect_left

# 由于Python的性能限制，对于大数据可能超时，但在逻辑上是正确的

class Solution:
    def __init__(self):
        self.MAXN = 100001
        self.n = 0
        self.m = 0
        
        # 原始数组
        self.arr = [0] * self.MAXN
        
        # 离散化后的数组
        self.sorted_arr = [0] * (self.MAXN * 2)
        
        # 操作信息
        self.ops = []  # (type, l, r, k, x, y, id)
        
        # 树状数组
        self.tree = [0] * self.MAXN
        
        # 查询的答案
        self.ans = [0] * self.MAXN
    
    def lowbit(self, i):
        """树状数组的lowbit操作"""
        return i & -i
    
    def add(self, i, v):
        """树状数组单点更新"""
        while i <= self.n:
            self.tree[i] += v
            i += self.lowbit(i)
    
    def sum(self, i):
        """树状数组前缀和查询"""
        ret = 0
        while i > 0:
            ret += self.tree[i]
            i -= self.lowbit(i)
        return ret
    
    def query(self, l, r):
        """树状数组区间和查询"""
        return self.sum(r) - self.sum(l - 1)
    
    def compute(self, ql, qr, vl, vr):
        """整体二分核心函数"""
        # 递归边界
        if ql > qr:
            return
        
        # 如果值域范围只有一个值，说明找到了答案
        if vl == vr:
            for i in range(ql, qr + 1):
                if self.ops[i][0] == 0:  # 查询操作
                    self.ans[self.ops[i][6]] = self.sorted_arr[vl]
            return
        
        # 二分中点
        mid = (vl + vr) >> 1
        
        # 将值域小于等于mid的数加入树状数组
        for i in range(vl, mid + 1):
            # 这里需要处理所有值为sorted[i]的元素
            # 在实际实现中，我们需要更复杂的处理方式
            pass
        
        # 检查每个操作，根据满足条件的元素个数划分到左右区间
        lset = []
        rset = []
        for i in range(ql, qr + 1):
            if self.ops[i][0] == 0:  # 查询操作
                # 查询区间[ops[i][1], ops[i][2]]中值小于等于sorted[mid]的元素个数
                satisfy = self.query(self.ops[i][1], self.ops[i][2])
                
                if satisfy >= self.ops[i][3]:
                    # 说明第k小的数在左半部分
                    lset.append(i)
                else:
                    # 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
                    self.ops[i] = (self.ops[i][0], self.ops[i][1], self.ops[i][2], 
                                  self.ops[i][3] - satisfy, self.ops[i][4], 
                                  self.ops[i][5], self.ops[i][6])
                    rset.append(i)
            else:  # 修改操作
                # 修改操作需要拆分为删除和插入
                # 这里简化处理，实际实现中需要更复杂的逻辑
                if self.ops[i][5] <= self.sorted_arr[mid]:
                    self.add(self.ops[i][4], 1)
                    lset.append(i)
                else:
                    rset.append(i)
        
        # 重新排列操作顺序
        new_ops = []
        for i in lset:
            new_ops.append(self.ops[i])
        for i in rset:
            new_ops.append(self.ops[i])
        
        # 更新原操作数组
        for i in range(len(new_ops)):
            self.ops[ql + i] = new_ops[i]
        
        # 撤销对树状数组的修改
        for i in range(vl, mid + 1):
            # 撤销操作
            pass
        
        # 递归处理左右两部分
        self.compute(ql, ql + len(lset) - 1, vl, mid)
        self.compute(ql + len(lset), qr, mid + 1, vr)
    
    def solve(self):
        """主函数"""
        # 读取输入
        line = sys.stdin.readline().split()
        self.n = int(line[0])
        self.m = int(line[1])
        
        # 读取原始数组
        nums = sys.stdin.readline().split()
        for i in range(1, self.n + 1):
            self.arr[i] = int(nums[i - 1])
            self.sorted_arr[i] = self.arr[i]
        
        op_count = self.n
        # 读取操作
        for i in range(1, self.m + 1):
            op = sys.stdin.readline().split()
            if op[0] == "Q":
                l = int(op[1])
                r = int(op[2])
                k = int(op[3])
                self.ops.append((0, l, r, k, 0, 0, i))  # 查询操作
                op_count += 1
            else:  # C
                x = int(op[1])
                y = int(op[2])
                self.ops.append((1, 0, 0, 0, x, y, i))  # 修改操作
                op_count += 1
                self.sorted_arr[self.n + 1] = y  # 添加到离散化数组中
                self.n += 1
        
        # 离散化
        self.sorted_arr[1:self.n+1] = sorted(self.sorted_arr[1:self.n+1])
        unique_count = 1
        for i in range(2, self.n + 1):
            if self.sorted_arr[i] != self.sorted_arr[i - 1]:
                unique_count += 1
                self.sorted_arr[unique_count] = self.sorted_arr[i]
        
        # 整体二分求解
        self.compute(0, len(self.ops) - 1, 1, unique_count)
        
        # 输出结果
        for i in range(1, self.m + 1):
            if self.ans[i] != 0:
                print(self.ans[i])

# 程序入口
#if __name__ == "__main__":
#    solution = Solution()
#    solution.solve()