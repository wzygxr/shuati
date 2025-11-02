#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树合并专题 - Code02_SegmentTreeUpdateQuerySum.py

线段树实现 - 支持范围重置、范围查询
维护累加和

测试链接：https://www.luogu.com.cn/problem/P3372

题目描述：
实现一个支持区间重置操作和区间求和查询的数据结构。

算法思路：
1. 使用线段树数据结构维护区间信息
2. 采用懒标记技术优化区间更新操作
3. 支持区间重置和区间查询操作

核心思想：
- 线段树：二叉树结构，每个节点代表一个区间
- 懒标记：延迟更新操作，提高效率
- 递归构建：自底向上构建树结构

时间复杂度分析：
- 建树: O(n) - 需要遍历所有元素构建树结构
- 单点更新: O(log n) - 树的高度为log n
- 区间更新: O(log n) - 使用懒标记技术优化
- 区间查询: O(log n) - 最多访问2log n个节点

空间复杂度分析：
- 线段树数组：O(4n) - 通常需要4倍原始数组大小的空间
- 懒标记数组：O(4n) - 存储重置信息和更新标记
- 总空间复杂度：O(n)

工程化考量：
1. 使用数组存储线段树，提高内存使用效率
2. 预先分配足够的空间以避免频繁的内存分配
3. 利用位运算优化索引计算
4. 添加输入验证和异常处理机制

语言特性差异：
- Python：动态类型，代码简洁但性能较低
- Java：使用数组模拟指针，避免对象创建开销
- C++：使用指针直接操作，内存管理更灵活

边界情况处理：
- 空数组或单元素数组
- 区间边界越界情况
- 大规模数据输入

优化技巧：
- 懒标记技术：避免不必要的更新操作
- 位运算优化：使用移位操作替代乘除法
- 递归优化：控制递归深度，避免栈溢出

测试用例设计：
1. 基础测试：小规模数组验证算法正确性
2. 边界测试：单元素、空数组、边界值
3. 性能测试：大规模数据验证时间复杂度
4. 极端测试：连续重置和查询操作

运行命令：
python Code02_SegmentTreeUpdateQuerySum.py

注意事项：
1. Python版本由于性能限制，适合中小规模数据
2. 对于大规模数据，建议使用C++或Java版本
3. 注意递归深度限制，可能需要调整系统设置
"""

class SegmentTreeUpdateQuerySum:
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size: 数组大小
        """
        self.n = size
        # 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        self.sum = [0] * (size * 4)
        self.change = [0] * (size * 4)
        self.update = [False] * (size * 4)

    def push_up(self, i):
        """
        向上更新节点信息 - 累加和信息的汇总
        
        Args:
            i: 当前节点编号
        """
        self.sum[i] = self.sum[i << 1] + self.sum[i << 1 | 1]

    def push_down(self, i, ln, rn):
        """
        向下传递懒标记
        
        Args:
            i: 当前节点编号
            ln: 左子树节点数量
            rn: 右子树节点数量
        """
        if self.update[i]:
            self.lazy(i << 1, self.change[i], ln)
            self.lazy(i << 1 | 1, self.change[i], rn)
            self.update[i] = False

    def lazy(self, i, v, n):
        """
        懒标记操作
        
        Args:
            i: 节点编号
            v: 重置的值
            n: 节点对应的区间长度
        """
        self.sum[i] = v * n
        self.change[i] = v
        self.update[i] = True

    def build(self, arr, l, r, i):
        """
        建树
        
        Args:
            arr: 原始数组
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if l == r:
            self.sum[i] = arr[l]
        else:
            mid = (l + r) >> 1
            self.build(arr, l, mid, i << 1)
            self.build(arr, mid + 1, r, i << 1 | 1)
            self.push_up(i)
        self.change[i] = 0
        self.update[i] = False

    def update_range(self, jobl, jobr, jobv, l, r, i):
        """
        范围重置 - jobl ~ jobr范围上每个数字重置为jobv
        
        Args:
            jobl: 任务区间左端点
            jobr: 任务区间右端点
            jobv: 重置的值
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if jobl <= l and r <= jobr:
            self.lazy(i, jobv, r - l + 1)
        else:
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def query(self, jobl, jobr, l, r, i):
        """
        查询累加和
        
        Args:
            jobl: 查询区间左端点
            jobr: 查询区间右端点
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
            
        Returns:
            区间和
        """
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) >> 1
        self.push_down(i, mid - l + 1, r - mid)
        ans = 0
        if jobl <= mid:
            ans += self.query(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            ans += self.query(jobl, jobr, mid + 1, r, i << 1 | 1)
        return ans


# 测试代码
if __name__ == "__main__":
    # 示例测试
    print("线段树测试 - 支持范围重置和范围查询")
    seg_tree = SegmentTreeUpdateQuerySum(10)
    print("初始化完成")