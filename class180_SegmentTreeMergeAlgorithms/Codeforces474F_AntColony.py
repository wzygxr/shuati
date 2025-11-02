#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Codeforces 474F - Ant Colony
题目链接: https://codeforces.com/problemset/problem/474/F

题目描述:
给定一个数组，多次查询区间[l, r]内有多少个元素不能被区间内其他所有元素整除

线段树解法:
使用线段树维护区间GCD和最小值及其出现次数
时间复杂度: O((n+q) log n)
空间复杂度: O(n)

工程化考量:
1. 处理大数组输入优化
2. 边界条件处理
3. 性能优化
"""

import math
import sys

class SegmentTree:
    """线段树维护区间GCD和最小值信息"""
    
    def __init__(self, data):
        """
        初始化线段树
        
        参数:
            data: 输入数组
        """
        self.n = len(data)
        self.data = data
        self.size = 1
        while self.size < self.n:
            self.size *= 2
        
        # 存储区间GCD
        self.gcd_tree = [0] * (2 * self.size)
        # 存储区间最小值
        self.min_tree = [0] * (2 * self.size)
        # 存储最小值出现次数
        self.min_count = [0] * (2 * self.size)
        
        self._build(data)
    
    def _build(self, data):
        """构建线段树"""
        # 初始化叶子节点
        for i in range(self.n):
            self.gcd_tree[self.size + i] = data[i]
            self.min_tree[self.size + i] = data[i]
            self.min_count[self.size + i] = 1
        
        # 初始化剩余叶子节点
        for i in range(self.n, self.size):
            self.gcd_tree[self.size + i] = 0
            self.min_tree[self.size + i] = float('inf')
            self.min_count[self.size + i] = 0
        
        # 构建内部节点
        for i in range(self.size - 1, 0, -1):
            self._pull(i)
    
    def _pull(self, i):
        """更新父节点信息"""
        left = 2 * i
        right = 2 * i + 1
        
        # 更新GCD
        self.gcd_tree[i] = math.gcd(self.gcd_tree[left], self.gcd_tree[right])
        
        # 更新最小值及其计数
        if self.min_tree[left] < self.min_tree[right]:
            self.min_tree[i] = self.min_tree[left]
            self.min_count[i] = self.min_count[left]
        elif self.min_tree[left] > self.min_tree[right]:
            self.min_tree[i] = self.min_tree[right]
            self.min_count[i] = self.min_count[right]
        else:
            self.min_tree[i] = self.min_tree[left]
            self.min_count[i] = self.min_count[left] + self.min_count[right]
    
    def query(self, l, r):
        """
        查询区间[l, r]的信息
        
        返回: (区间GCD, 最小值, 最小值出现次数)
        """
        l += self.size
        r += self.size
        
        gcd_result = 0
        min_result = float('inf')
        min_count_result = 0
        
        while l <= r:
            if l % 2 == 1:
                gcd_result = math.gcd(gcd_result, self.gcd_tree[l]) if gcd_result != 0 else self.gcd_tree[l]
                if self.min_tree[l] < min_result:
                    min_result = self.min_tree[l]
                    min_count_result = self.min_count[l]
                elif self.min_tree[l] == min_result:
                    min_count_result += self.min_count[l]
                l += 1
            
            if r % 2 == 0:
                gcd_result = math.gcd(gcd_result, self.gcd_tree[r]) if gcd_result != 0 else self.gcd_tree[r]
                if self.min_tree[r] < min_result:
                    min_result = self.min_tree[r]
                    min_count_result = self.min_count[r]
                elif self.min_tree[r] == min_result:
                    min_count_result += self.min_count[r]
                r -= 1
            
            l //= 2
            r //= 2
        
        return gcd_result, min_result, min_count_result

def solve():
    """主解法函数"""
    # 读取输入
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    arr = list(map(int, data[1:1+n]))
    
    # 构建线段树
    seg_tree = SegmentTree(arr)
    
    # 处理查询
    q = int(data[1+n])
    results = []
    
    idx = 1 + n + 1
    for _ in range(q):
        l = int(data[idx]) - 1
        r = int(data[idx+1]) - 1
        idx += 2
        
        gcd_val, min_val, min_count = seg_tree.query(l, r)
        
        # 如果最小值等于GCD，说明最小值能被所有数整除
        if min_val == gcd_val:
            # 不能被整除的数量 = 区间长度 - 最小值出现次数
            result = (r - l + 1) - min_count
        else:
            # 所有数都不能被整除
            result = r - l + 1
        
        results.append(str(result))
    
    # 输出结果
    print('\n'.join(results))

def test():
    """测试函数"""
    print("=== Codeforces 474F - Ant Colony 测试 ===")
    
    # 测试用例1
    print("测试用例1: [2, 4, 6, 8, 10]")
    arr1 = [2, 4, 6, 8, 10]
    seg_tree1 = SegmentTree(arr1)
    
    # 查询整个区间
    gcd_val, min_val, min_count = seg_tree1.query(0, 4)
    print(f"区间[0,4]: GCD={gcd_val}, 最小值={min_val}, 最小值出现次数={min_count}")
    
    # 测试用例2
    print("\n测试用例2: [1, 2, 3, 4, 5]")
    arr2 = [1, 2, 3, 4, 5]
    seg_tree2 = SegmentTree(arr2)
    
    gcd_val, min_val, min_count = seg_tree2.query(0, 4)
    print(f"区间[0,4]: GCD={gcd_val}, 最小值={min_val}, 最小值出现次数={min_count}")
    
    # 边界测试
    print("\n边界测试: 单个元素")
    arr3 = [5]
    seg_tree3 = SegmentTree(arr3)
    gcd_val, min_val, min_count = seg_tree3.query(0, 0)
    print(f"单个元素: GCD={gcd_val}, 最小值={min_val}, 最小值出现次数={min_count}")

if __name__ == "__main__":
    # 如果是直接运行，执行测试
    if len(sys.argv) > 1 and sys.argv[1] == "--test":
        test()
    else:
        solve()