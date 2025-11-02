# Prefix Sum Queries (CSES 2166)
# 
# 题目描述:
# 给定一个数组，支持两种操作：
# 1. 更新位置k的值为u
# 2. 查询区间[a,b]内的最大前缀和
# 
# 示例:
# 输入:
# 5 3
# 2 2 2 2 2
# 2 1 5
# 1 3 4
# 2 1 5
# 输出:
# 10
# 12
# 
# 提示:
# 1 <= n, q <= 2 * 10^5
# -10^9 <= x <= 10^9
# 
# 题目链接: https://cses.fi/problemset/task/2166
# 
# 解题思路:
# 使用线段树维护区间信息，每个节点存储：
# 1. 区间和
# 2. 区间最大前缀和
# 3. 区间最大后缀和
# 4. 区间最大子段和
# 
# 时间复杂度: 
# - 初始化: O(n) - 需要遍历整个数组构建线段树
# - 更新: O(log n) - 每次更新操作的时间复杂度
# - 查询: O(log n) - 每次查询操作的时间复杂度
# 空间复杂度: O(n) - 线段树需要额外的空间
# 
# 工程化考量:
# 1. 边界条件处理：空数组、单元素数组
# 2. 性能优化：使用线段树提供高效的区间查询和更新
# 3. 数据结构选择：线段树适合频繁的区间操作
# 4. 大数处理：元素值可能很大，需要确保整数范围
# 
# 最优解分析:
# 这是最优解，因为需要支持动态更新和查询操作，线段树提供了O(log n)的时间复杂度。
# 对于频繁的区间操作，线段树是最佳选择。
# 
# 算法核心:
# 线段树的合并操作：
# - 区间和 = 左子树区间和 + 右子树区间和
# - 区间最大前缀和 = max(左子树最大前缀和, 左子树区间和 + 右子树最大前缀和)
# - 区间最大后缀和 = max(右子树最大后缀和, 右子树区间和 + 左子树最大后缀和)
# - 区间最大子段和 = max(左子树最大子段和, 右子树最大子段和, 左子树最大后缀和 + 右子树最大前缀和)
# 
# 算法调试技巧:
# 1. 打印中间过程：显示线段树的构建和更新过程
# 2. 边界测试：测试空数组、单元素数组等特殊情况
# 3. 性能测试：测试大规模数组下的性能表现
# 
# 语言特性差异:
# Python中列表是动态数组，可以直接获取长度。
# 与Java相比，Python有自动内存管理，无需手动释放内存。
# 与C++相比，Python是动态类型语言，无需显式声明类型。

import sys
from math import inf

class SegmentTreeNode:
    def __init__(self):
        self.sum = 0           # 区间和
        self.maxPrefixSum = 0  # 区间最大前缀和
        self.maxSuffixSum = 0  # 区间最大后缀和
        self.maxSubarraySum = 0 # 区间最大子段和

class SegmentTree:
    def __init__(self, array):
        self.n = len(array)
        self.arr = array[:]
        # 线段树数组大小通常为4*n
        self.tree = [SegmentTreeNode() for _ in range(4 * self.n)]
        # 构建线段树
        self.build(1, 0, self.n - 1)
    
    def build(self, node, start, end):
        # 叶子节点
        if start == end:
            self.tree[node].sum = self.arr[start]
            self.tree[node].maxPrefixSum = self.arr[start]
            self.tree[node].maxSuffixSum = self.arr[start]
            self.tree[node].maxSubarraySum = self.arr[start]
            return
        
        mid = (start + end) // 2
        # 递归构建左右子树
        self.build(2 * node, start, mid)
        self.build(2 * node + 1, mid + 1, end)
        
        # 合并左右子树的信息
        self.merge(node)
    
    def merge(self, node):
        leftChild = 2 * node
        rightChild = 2 * node + 1
        
        # 区间和 = 左子树区间和 + 右子树区间和
        self.tree[node].sum = self.tree[leftChild].sum + self.tree[rightChild].sum
        
        # 区间最大前缀和 = max(左子树最大前缀和, 左子树区间和 + 右子树最大前缀和)
        self.tree[node].maxPrefixSum = max(
            self.tree[leftChild].maxPrefixSum,
            self.tree[leftChild].sum + self.tree[rightChild].maxPrefixSum
        )
        
        # 区间最大后缀和 = max(右子树最大后缀和, 右子树区间和 + 左子树最大后缀和)
        self.tree[node].maxSuffixSum = max(
            self.tree[rightChild].maxSuffixSum,
            self.tree[rightChild].sum + self.tree[leftChild].maxSuffixSum
        )
        
        # 区间最大子段和 = max(左子树最大子段和, 右子树最大子段和, 左子树最大后缀和 + 右子树最大前缀和)
        self.tree[node].maxSubarraySum = max(
            max(self.tree[leftChild].maxSubarraySum, self.tree[rightChild].maxSubarraySum),
            self.tree[leftChild].maxSuffixSum + self.tree[rightChild].maxPrefixSum
        )
    
    def update(self, index, value):
        self.arr[index] = value
        self._update(1, 0, self.n - 1, index, value)
    
    def _update(self, node, start, end, index, value):
        # 叶子节点
        if start == end:
            self.tree[node].sum = value
            self.tree[node].maxPrefixSum = value
            self.tree[node].maxSuffixSum = value
            self.tree[node].maxSubarraySum = value
            return
        
        mid = (start + end) // 2
        # 根据索引决定更新左子树还是右子树
        if index <= mid:
            self._update(2 * node, start, mid, index, value)
        else:
            self._update(2 * node + 1, mid + 1, end, index, value)
        
        # 更新后重新合并信息
        self.merge(node)
    
    def queryMaxPrefixSum(self, end):
        return self._queryMaxPrefixSum(1, 0, self.n - 1, 0, end)
    
    def _queryMaxPrefixSum(self, node, start, end, left, right):
        # 完全不在查询区间内
        if start > right or end < left:
            return -inf
        
        # 完全在查询区间内
        if start >= left and end <= right:
            return self.tree[node].maxPrefixSum
        
        mid = (start + end) // 2
        # 递归查询左右子树
        leftResult = self._queryMaxPrefixSum(2 * node, start, mid, left, right)
        rightResult = self._queryMaxPrefixSum(2 * node + 1, mid + 1, end, left, right)
        
        # 返回较大值
        return max(leftResult, rightResult)

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    q = int(data[1])
    
    arr = [int(data[i + 2]) for i in range(n)]
    
    segTree = SegmentTree(arr)
    
    index = 2 + n
    results = []
    
    for _ in range(q):
        type = int(data[index])
        index += 1
        
        if type == 1:
            # 更新操作
            k = int(data[index]) - 1  # 转换为0-based索引
            index += 1
            u = int(data[index])
            index += 1
            segTree.update(k, u)
        else:
            # 查询操作
            a = int(data[index]) - 1  # 转换为0-based索引
            index += 1
            b = int(data[index]) - 1  # 转换为0-based索引
            index += 1
            result = segTree.queryMaxPrefixSum(b)
            results.append(str(result))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()