"""
Python 线段树实现 - Luogu P3373. 【模板】线段树 2
题目链接: https://www.luogu.com.cn/problem/P3373
题目描述:
如题，已知一个数列a，你需要进行下面三种操作：
1. 将某区间每一个数乘上x
2. 将某区间每一个数加上x
3. 求出某区间每一个数的和

输入:
第一行包含三个整数n, m, p，分别表示该数列数字的个数、操作的总个数和模数。
第二行包含n个用空格分隔的整数，其中第i个数字表示数列第i项的初始值。
接下来m行每行包含若干个整数，表示一个操作，操作有以下三种：
1. 1 l r x：将区间[l,r]内每个数乘上x
2. 2 l r x：将区间[l,r]内每个数加上x
3. 3 l r：求区间[l,r]内每个数的和对p取模的值

输出:
对于每个操作3，输出一行包含一个整数，表示区间和对p取模的值。

示例:
输入:
5 5 38
1 2 3 4 5
1 1 5 2
2 1 5 1
3 1 5
2 1 5 2
3 1 5

输出:
32
36

解题思路:
这是一个支持区间乘法、区间加法和区间求和的线段树模板题。
需要同时维护两个懒标记：乘法标记和加法标记。
1. 乘法标记优先级高于加法标记
2. 下发标记时，先下发乘法标记，再下发加法标记
3. 更新标记时，需要考虑标记的优先级和组合

时间复杂度: 
- 建树: O(n)
- 区间更新: O(log n)
- 区间查询: O(log n)
空间复杂度: O(n)
"""


class Node:
    def __init__(self, l=0, r=0):
        """
        线段树节点
        :param l: 区间左边界
        :param r: 区间右边界
        """
        self.l = l
        self.r = r
        self.sum = 0   # 区间和
        self.mul = 1   # 乘法懒标记
        self.add = 0   # 加法懒标记


class SegmentTree:
    def __init__(self, arr, p):
        """
        初始化线段树
        :param arr: 输入数组
        :param p: 模数
        """
        self.n = len(arr) - 1  # 数组索引从1开始
        self.arr = arr[:]
        self.p = p
        
        # 线段树数组，大小为4*n
        self.tree = [Node() for _ in range(4 * self.n)]
    
    def _push_up(self, i):
        """
        向上传递
        :param i: 当前节点在tree数组中的索引
        """
        self.tree[i].sum = (self.tree[i << 1].sum + self.tree[i << 1 | 1].sum) % self.p
    
    def _push_down(self, i):
        """
        懒标记下发
        :param i: 当前节点在tree数组中的索引
        """
        if self.tree[i].mul != 1 or self.tree[i].add != 0:
            mul = self.tree[i].mul
            add = self.tree[i].add
            
            # 下发给左子树
            self._lazy(i << 1, mul, add)
            
            # 下发给右子树
            self._lazy(i << 1 | 1, mul, add)
            
            # 清除父节点的懒标记
            self.tree[i].mul = 1
            self.tree[i].add = 0
    
    def _lazy(self, i, mul, add):
        """
        懒标记更新
        :param i: 节点索引
        :param mul: 乘法标记
        :param add: 加法标记
        """
        # 更新区间和
        self.tree[i].sum = (self.tree[i].sum * mul % self.p + add * (self.tree[i].r - self.tree[i].l + 1) % self.p) % self.p
        
        # 更新乘法标记
        self.tree[i].mul = self.tree[i].mul * mul % self.p
        
        # 更新加法标记
        self.tree[i].add = (self.tree[i].add * mul % self.p + add) % self.p
    
    def build(self, l, r, i):
        """
        建立线段树
        :param l: 区间左边界
        :param r: 区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        self.tree[i].l = l
        self.tree[i].r = r
        self.tree[i].mul = 1
        self.tree[i].add = 0
        
        if l == r:
            self.tree[i].sum = self.arr[l] % self.p
            return
        
        mid = (l + r) // 2
        self.build(l, mid, i << 1)
        self.build(mid + 1, r, i << 1 | 1)
        self._push_up(i)
    
    def multiply(self, jobl, jobr, val, l, r, i):
        """
        区间乘法
        :param jobl: 操作区间左边界
        :param jobr: 操作区间右边界
        :param val: 乘数
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        if jobl <= l and r <= jobr:
            self._lazy(i, val, 0)
            return
        
        self._push_down(i)
        mid = (l + r) // 2
        if jobl <= mid:
            self.multiply(jobl, jobr, val, l, mid, i << 1)
        if jobr > mid:
            self.multiply(jobl, jobr, val, mid + 1, r, i << 1 | 1)
        self._push_up(i)
    
    def add(self, jobl, jobr, val, l, r, i):
        """
        区间加法
        :param jobl: 操作区间左边界
        :param jobr: 操作区间右边界
        :param val: 加数
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        if jobl <= l and r <= jobr:
            self._lazy(i, 1, val)
            return
        
        self._push_down(i)
        mid = (l + r) // 2
        if jobl <= mid:
            self.add(jobl, jobr, val, l, mid, i << 1)
        if jobr > mid:
            self.add(jobl, jobr, val, mid + 1, r, i << 1 | 1)
        self._push_up(i)
    
    def query(self, jobl, jobr, l, r, i):
        """
        区间查询和
        :param jobl: 查询区间左边界
        :param jobr: 查询区间右边界
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        :return: 区间和
        """
        if jobl <= l and r <= jobr:
            return self.tree[i].sum
        
        self._push_down(i)
        mid = (l + r) // 2
        ans = 0
        if jobl <= mid:
            ans = (ans + self.query(jobl, jobr, l, mid, i << 1)) % self.p
        if jobr > mid:
            ans = (ans + self.query(jobl, jobr, mid + 1, r, i << 1 | 1)) % self.p
        return ans


class Solution:
    def process_operations(self, n, m, p, initial_array, operations):
        """
        处理操作序列
        :param n: 数组长度
        :param m: 操作数量
        :param p: 模数
        :param initial_array: 初始数组
        :param operations: 操作列表
        :return: 查询结果列表
        """
        # 初始化数组，索引从1开始
        arr = [0] + initial_array
        
        # 创建线段树
        st = SegmentTree(arr, p)
        
        # 建立线段树
        st.build(1, n, 1)
        
        # 处理操作并收集查询结果
        results = []
        for operation in operations:
            op = operation[0]
            if op == 1:
                # 区间乘法
                l, r, x = operation[1], operation[2], operation[3]
                st.multiply(l, r, x, 1, n, 1)
            elif op == 2:
                # 区间加法
                l, r, x = operation[1], operation[2], operation[3]
                st.add(l, r, x, 1, n, 1)
            elif op == 3:
                # 区间查询
                l, r = operation[1], operation[2]
                result = st.query(l, r, 1, n, 1)
                results.append(result)
        
        return results


# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 示例测试
    n, m, p = 5, 5, 38
    initial_array = [1, 2, 3, 4, 5]
    operations = [
        [1, 1, 5, 2],
        [2, 1, 5, 1],
        [3, 1, 5],
        [2, 1, 5, 2],
        [3, 1, 5]
    ]
    
    results = solution.process_operations(n, m, p, initial_array, operations)
    
    print("初始数组: [1, 2, 3, 4, 5]")
    print("操作过程:")
    print("1 1 5 2 (将区间[1,5]内每个数乘上2)")
    print("2 1 5 1 (将区间[1,5]内每个数加上1)")
    print("3 1 5 (求区间[1,5]内每个数的和)")
    print("2 1 5 2 (将区间[1,5]内每个数加上2)")
    print("3 1 5 (求区间[1,5]内每个数的和)")
    
    print("\n输出:")
    for result in results:
        print(result)
    
    print("\n期望输出:")
    print("32")
    print("36")