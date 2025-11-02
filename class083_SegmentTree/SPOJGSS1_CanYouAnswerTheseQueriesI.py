"""
Python 线段树实现 - SPOJ GSS1. Can you answer these queries I
题目链接: https://www.spoj.com/problems/GSS1/
题目描述:
给定一个长度为N的整数序列A1, A2, ..., AN。你需要处理M个查询。
对于每个查询，给定两个整数i和j，你需要找到序列中从Ai到Aj的最大子段和。
最大子段和定义为：max{Ak + Ak+1 + ... + Al | i <= k <= l <= j}

输入:
第一行包含一个整数N (1 <= N <= 50000)，表示序列的长度。
第二行包含N个整数，表示序列A1, A2, ..., AN (-15007 <= Ai <= 15007)。
第三行包含一个整数M (1 <= M <= 10000)，表示查询的数量。
接下来M行，每行包含两个整数i和j (1 <= i <= j <= N)，表示一个查询。

输出:
对于每个查询，输出一行包含一个整数，表示从Ai到Aj的最大子段和。

示例:
输入:
5
-1 2 -3 4 -5
3
1 3
2 5
1 5

输出:
2
4
4

解题思路:
这是一个经典的线段树问题，需要维护区间最大子段和。
对于每个线段树节点，我们需要维护以下信息：
1. 区间和(sum)
2. 区间最大子段和(maxSum)
3. 区间以左端点开始的最大子段和(prefixMax)
4. 区间以右端点结束的最大子段和(suffixMax)

合并两个子区间[l, mid]和[mid+1, r]的信息时：
1. 区间和 = 左区间和 + 右区间和
2. 区间最大子段和 = max(左区间最大子段和, 右区间最大子段和, 左区间后缀最大值 + 右区间前缀最大值)
3. 区间前缀最大值 = max(左区间前缀最大值, 左区间和 + 右区间前缀最大值)
4. 区间后缀最大值 = max(右区间后缀最大值, 右区间和 + 左区间后缀最大值)

时间复杂度: 
- 建树: O(n)
- 查询: O(log n)
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
        self.sum = 0           # 区间和
        self.maxSum = 0        # 区间最大子段和
        self.prefixMax = 0     # 区间以左端点开始的最大子段和
        self.suffixMax = 0     # 区间以右端点结束的最大子段和


class SegmentTree:
    def __init__(self, arr):
        """
        初始化线段树
        :param arr: 输入数组
        """
        self.n = len(arr) - 1  # 数组索引从1开始
        self.arr = arr[:]
        
        # 线段树数组，大小为4*n
        self.tree = [Node() for _ in range(4 * self.n)]
        
        # 构建线段树
        self._build(1, self.n, 1)
    
    def _build(self, l, r, i):
        """
        构建线段树
        :param l: 区间左边界
        :param r: 区间右边界
        :param i: 当前节点在tree数组中的索引
        """
        self.tree[i].l = l
        self.tree[i].r = r
        
        # 递归终止条件：到达叶子节点
        if l == r:
            self.tree[i].sum = self.arr[l]
            self.tree[i].maxSum = self.arr[l]
            self.tree[i].prefixMax = self.arr[l]
            self.tree[i].suffixMax = self.arr[l]
            return
        
        # 计算中点
        mid = (l + r) // 2
        # 递归构建左子树
        self._build(l, mid, i << 1)
        # 递归构建右子树
        self._build(mid + 1, r, i << 1 | 1)
        # 合并左右子树的结果
        self._push_up(i)
    
    def _push_up(self, i):
        """
        向上传递
        :param i: 当前节点在tree数组中的索引
        """
        left = self.tree[i << 1]
        right = self.tree[i << 1 | 1]
        current = self.tree[i]
        
        # 区间和 = 左区间和 + 右区间和
        current.sum = left.sum + right.sum
        
        # 区间最大子段和 = max(左区间最大子段和, 右区间最大子段和, 左区间后缀最大值 + 右区间前缀最大值)
        cross_sum = left.suffixMax + right.prefixMax
        current.maxSum = max(left.maxSum, right.maxSum, cross_sum)
        
        # 区间前缀最大值 = max(左区间前缀最大值, 左区间和 + 右区间前缀最大值)
        current.prefixMax = max(left.prefixMax, left.sum + right.prefixMax)
        
        # 区间后缀最大值 = max(右区间后缀最大值, 右区间和 + 左区间后缀最大值)
        current.suffixMax = max(right.suffixMax, right.sum + left.suffixMax)
    
    def query(self, jobl, jobr, l, r, i):
        """
        区间查询最大子段和
        :param jobl: 查询区间左边界
        :param jobr: 查询区间右边界
        :param l: 当前区间左边界
        :param r: 当前区间右边界
        :param i: 当前节点在tree数组中的索引
        :return: 区间最大子段和
        """
        if jobl <= l and r <= jobr:
            return self.tree[i].maxSum
        
        mid = (l + r) // 2
        ans = float('-inf')
        
        if jobl <= mid and jobr > mid:
            # 查询区间跨越左右子树
            left = self.tree[i << 1]
            right = self.tree[i << 1 | 1]
            
            # 计算跨越中间点的最大子段和
            cross_sum = left.suffixMax + right.prefixMax
            ans = max(ans, cross_sum)
            
            # 递归查询左右子树
            if jobl <= mid:
                ans = max(ans, self.query(jobl, jobr, l, mid, i << 1))
            if jobr > mid:
                ans = max(ans, self.query(jobl, jobr, mid + 1, r, i << 1 | 1))
        elif jobr <= mid:
            # 查询区间完全在左子树
            ans = self.query(jobl, jobr, l, mid, i << 1)
        else:
            # 查询区间完全在右子树
            ans = self.query(jobl, jobr, mid + 1, r, i << 1 | 1)
        
        return ans


class Solution:
    def process_queries(self, n, arr, queries):
        """
        处理查询
        :param n: 数组长度
        :param arr: 输入数组
        :param queries: 查询列表
        :return: 查询结果列表
        """
        # 初始化数组，索引从1开始
        array = [0] + arr
        
        # 创建线段树
        st = SegmentTree(array)
        
        # 处理查询并收集结果
        results = []
        for query in queries:
            i, j = query[0], query[1]
            result = st.query(i, j, 1, n, 1)
            results.append(result)
        
        return results


# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 示例测试
    n = 5
    arr = [-1, 2, -3, 4, -5]
    queries = [
        [1, 3],
        [2, 5],
        [1, 5]
    ]
    
    results = solution.process_queries(n, arr, queries)
    
    print("输入序列: [-1, 2, -3, 4, -5]")
    for i, query in enumerate(queries):
        print("查询 {} {}: {}".format(query[0], query[1], results[i]))
    
    print("\n期望输出:")
    print("2")
    print("4")
    print("4")