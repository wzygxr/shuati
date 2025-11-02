#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
SPOJ GSS1 - Can you answer these queries I

题目描述：
给定一个长度为n的整数序列，执行m次查询操作
每次查询[l,r]区间内的最大子段和
最大子段和：在给定区间内找到连续子序列，使得其元素和最大

解题思路：
使用线段树维护区间信息，每个节点存储以下信息：
1. 区间最大子段和(maxSum)
2. 区间从左端点开始的最大子段和(lSum)
3. 区间到右端点结束的最大子段和(rSum)
4. 区间总和(sum)

关键技术：
1. 线段树区间信息维护：每个节点维护四个关键信息
2. 信息合并：通过push_up函数合并左右子区间信息
3. 区间查询：通过分治思想查询任意区间最大子段和

合并两个子区间[l,mid]和[mid+1,r]的信息：
1. 区间总和 = 左区间总和 + 右区间总和
2. 区间从左端点开始的最大子段和 = max(左区间lSum, 左区间sum + 右区间lSum)
3. 区间到右端点结束的最大子段和 = max(右区间rSum, 右区间sum + 左区间rSum)
4. 区间最大子段和 = max(左区间maxSum, 右区间maxSum, 左区间rSum + 右区间lSum)

时间复杂度分析：
1. 建树：O(n)
2. 查询：O(log n)
3. 空间复杂度：O(n)

是否最优解：是
这是解决最大子段和区间查询问题的最优解法，时间复杂度为O(log n)

工程化考量：
1. 内存管理：预分配列表避免频繁内存分配
2. 边界处理：处理区间完全包含和部分重叠的情况
3. 输入输出优化：批量读取输入数据提高效率

题目链接：https://www.spoj.com/problems/GSS1/

@author Algorithm Journey
@version 1.0
"""

import sys
from collections import namedtuple

# 定义节点信息类
# 每个线段树节点维护区间的关键信息
Node = namedtuple('Node', ['maxSum', 'lSum', 'rSum', 'sum'])

class SegmentTree:
    """
    线段树类
    用于维护区间最大子段和信息
    """
    
    def __init__(self, arr):
        """
        初始化线段树
        
        @param arr: 输入数组
        """
        self.n = len(arr)
        self.arr = arr
        self.tree = [None] * (4 * self.n)
        self.build(1, 0, self.n - 1)
    
    def push_up(self, left, right):
        """
        合并两个子节点的信息
        将左右子区间的信息合并到父区间
        
        @param left: 左子节点信息
        @param right: 右子节点信息
        @return: 合并后的节点信息
        """
        # 区间总和 = 左区间总和 + 右区间总和
        sum_val = left.sum + right.sum
        
        # 区间从左端点开始的最大子段和 = max(左区间lSum, 左区间sum + 右区间lSum)
        lSum = max(left.lSum, left.sum + right.lSum)
        
        # 区间到右端点结束的最大子段和 = max(右区间rSum, 右区间sum + 左区间rSum)
        rSum = max(right.rSum, right.sum + left.rSum)
        
        # 区间最大子段和 = max(左区间maxSum, 右区间maxSum, 左区间rSum + 右区间lSum)
        maxSum = max(max(left.maxSum, right.maxSum), left.rSum + right.lSum)
        
        return Node(maxSum, lSum, rSum, sum_val)
    
    def build(self, rt, l, r):
        """
        建立线段树
        递归构建线段树，每个节点存储对应区间的四个关键信息
        
        @param rt: 节点索引
        @param l: 区间左端点
        @param r: 区间右端点
        """
        # 叶子节点，直接赋值
        if l == r:
            self.tree[rt] = Node(self.arr[l], self.arr[l], self.arr[l], self.arr[l])
            return
        
        # 递归构建左右子树
        mid = (l + r) // 2
        self.build(2 * rt, l, mid)
        self.build(2 * rt + 1, mid + 1, r)
        
        # 合并左右子树信息
        self.tree[rt] = self.push_up(self.tree[2 * rt], self.tree[2 * rt + 1])
    
    def query(self, jobl, jobr, l, r, rt):
        """
        查询区间[jobl, jobr]的最大子段和
        通过分治思想查询任意区间的最大子段和
        
        @param jobl: 查询区间左端点
        @param jobr: 查询区间右端点
        @param l: 当前节点表示的区间左端点
        @param r: 当前节点表示的区间右端点
        @param rt: 当前节点索引
        @return: 查询区间的节点信息
        """
        # 当前区间完全被查询区间包含，直接返回
        if jobl <= l and r <= jobr:
            return self.tree[rt]
        
        mid = (l + r) // 2
        
        # 查询区间完全在左子树
        if jobr <= mid:
            return self.query(jobl, jobr, l, mid, 2 * rt)
        # 查询区间完全在右子树
        elif jobl > mid:
            return self.query(jobl, jobr, mid + 1, r, 2 * rt + 1)
        # 查询区间跨越左右子树
        else:
            left = self.query(jobl, jobr, l, mid, 2 * rt)
            right = self.query(jobl, jobr, mid + 1, r, 2 * rt + 1)
            return self.push_up(left, right)

def main():
    """
    主函数
    处理输入输出，执行查询操作
    """
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    
    arr = [int(data[idx + i]) for i in range(n)]
    idx += n
    
    seg_tree = SegmentTree(arr)
    
    m = int(data[idx])
    idx += 1
    
    results = []
    for _ in range(m):
        l = int(data[idx]) - 1  # 转换为0索引
        r = int(data[idx + 1]) - 1
        idx += 2
        result = seg_tree.query(l, r, 0, n - 1, 1)
        results.append(str(result.maxSum))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()