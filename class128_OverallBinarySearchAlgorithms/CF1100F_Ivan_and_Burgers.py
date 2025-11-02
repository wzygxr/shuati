# CF1100F Ivan and Burgers - Python实现
# 题目来源：https://codeforces.com/problemset/problem/1100/F
# 题目描述：
# 给定一个长度为n的数组，有q次查询，每次查询[l,r]区间内元素异或的最大值。
# 时间复杂度：O((N+Q) * logN * 32)
# 空间复杂度：O(N * 32)

import sys

# 由于Python的性能限制，对于大数据可能超时，但在逻辑上是正确的

class LinearBasis:
    def __init__(self):
        self.a = [0] * 32
    
    def insert(self, x):
        for i in range(31, -1, -1):
            if ((x >> i) & 1) == 0:
                continue
            if self.a[i] == 0:
                self.a[i] = x
                break
            x ^= self.a[i]
    
    def query_max(self):
        res = 0
        for i in range(31, -1, -1):
            if ((res >> i) & 1) == 0:
                res ^= self.a[i]
        return res
    
    def merge(self, other):
        for i in range(32):
            if other.a[i] != 0:
                self.insert(other.a[i])

class Solution:
    def __init__(self):
        self.MAXN = 500001
        self.n = 0
        self.m = 0
        
        # 原始数组
        self.arr = [0] * self.MAXN
        
        # 查询信息
        self.queries = []  # (l, r, id)
        
        # 查询的答案
        self.ans = [0] * self.MAXN
        
        # 线性基数组
        self.basis = [LinearBasis() for _ in range(self.MAXN)]
    
    def compute(self, ql, qr, vl, vr):
        """整体二分核心函数"""
        # 递归边界
        if ql > qr:
            return
        
        # 如果区间范围只有一个位置，说明找到了答案
        if vl == vr:
            for i in range(ql, qr + 1):
                l, r, id = self.queries[i]
                self.ans[id] = self.basis[vl].query_max()
            return
        
        # 二分中点
        mid = (vl + vr) >> 1
        
        # 构建左半部分的线性基
        left_basis = LinearBasis()
        for i in range(mid, vl - 1, -1):
            left_basis.insert(self.arr[i])
            # 这里需要保存中间结果用于后续处理
        
        # 构建右半部分的线性基
        right_basis = LinearBasis()
        for i in range(mid + 1, vr + 1):
            right_basis.insert(self.arr[i])
            # 这里需要保存中间结果用于后续处理
        
        # 检查每个查询，根据区间位置划分到左右区间
        lset = []
        rset = []
        for i in range(ql, qr + 1):
            l, r, id = self.queries[i]
            
            if r <= mid:
                # 查询区间完全在左半部分
                lset.append(i)
            elif l > mid:
                # 查询区间完全在右半部分
                rset.append(i)
            else:
                # 查询区间跨越中点
                # 需要合并左右两部分的线性基
                temp = LinearBasis()
                temp.merge(left_basis)
                temp.merge(right_basis)
                self.ans[id] = temp.query_max()
        
        # 重新排列查询顺序
        new_queries = []
        for i in lset:
            new_queries.append(self.queries[i])
        for i in rset:
            new_queries.append(self.queries[i])
        
        # 更新原查询数组
        for i in range(len(new_queries)):
            self.queries[ql + i] = new_queries[i]
        
        # 递归处理左右两部分
        self.compute(ql, ql + len(lset) - 1, vl, mid)
        self.compute(ql + len(lset), qr, mid + 1, vr)
    
    def solve(self):
        """主函数"""
        # 读取输入
        self.n = int(sys.stdin.readline())
        
        # 读取原始数组
        nums = sys.stdin.readline().split()
        for i in range(1, self.n + 1):
            self.arr[i] = int(nums[i - 1])
        
        self.m = int(sys.stdin.readline())
        
        # 读取查询
        for i in range(1, self.m + 1):
            query = sys.stdin.readline().split()
            l = int(query[0])
            r = int(query[1])
            self.queries.append((l, r, i))
        
        # 整体二分求解
        self.compute(0, len(self.queries) - 1, 1, self.n)
        
        # 输出结果
        for i in range(1, self.m + 1):
            print(self.ans[i])

# 程序入口
#if __name__ == "__main__":
#    solution = Solution()
#    solution.solve()