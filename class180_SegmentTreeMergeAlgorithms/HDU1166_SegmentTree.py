#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
HDU 1166 敌兵布阵
题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1166

C国的死对头A国这段时间正在进行军事演习，所以C国间谍头子Derek和他手下Tidy又开始忙乎了。
A国在海岸线沿直线布置了N个工兵营地,Derek和Tidy的任务就是要监视这些工兵营地的活动情况。
由于采取了先进的雷达系统，所以每个工兵营地的人数C国都掌握的一清二楚,
每个工兵营地的人数都有可能发生变动，可能增加或减少若干人手,
但这些都逃不过C国的监视。上级下达了一个任务：给定一些部队的调集信息，
要求你实时地报告某段连续营地的士兵总数。

输入格式:
第一行一个整数T，表示有T组测试数据。
每组测试数据第一行一个正整数N（N<=50000）,表示营地个数。
接下来有N个正整数,第i个正整数ai代表第i个营地开始时有ai个人（1<=ai<=50）。
接下来每行有一条命令，命令有4种形式：
(1) Add i j,i和j为正整数,表示第i个营地增加j个人（j不超过30）
(2) Sub i j ,i和j为正整数,表示第i个营地减少j个人（j不超过30）;
(3) Query i j ,i和j为正整数,i<=j，表示询问第i到第j个营地的总人数;
(4) End 表示结束，这条命令在每组数据最后出现;

输出格式:
对于每组测试数据,输出Case #:，#表示当前是第几组测试数据。
对于每个Query询问，输出一个整数并回车,表示询问的段中的总人数,这个数保持在int以内。

解题思路:
这是一个线段树的基础应用问题，支持单点更新和区间查询。
1. 使用线段树维护数组区间和
2. 单点增加/减少时，从根节点向下递归找到目标位置并更新，然后向上传递更新区间和
3. 区间查询时，根据查询区间与当前节点区间的关系进行递归查询

时间复杂度:
- 建树: O(n)
- 单点更新: O(log n)
- 区间查询: O(log n)
空间复杂度: O(n)

工程化考量:
1. 异常处理: 检查输入参数的有效性
2. 边界情况: 处理空数组、单个元素等情况
3. 性能优化: 使用位运算优化计算
4. 可测试性: 提供完整的测试用例覆盖各种场景
5. 可读性: 添加详细的注释说明设计思路和实现细节
6. 鲁棒性: 处理极端输入和非理想数据
"""


class HDU1166_SegmentTree:
    def __init__(self, size):
        """
        构造函数 - 初始化线段树
        
        :param size: 数组大小
        :type size: int
        """
        # 参数校验
        if size <= 0:
            raise ValueError("数组大小必须为正整数")
            
        self.n = size
        # 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        self.sum = [0] * (size * 4)
        self.add = [0] * (size * 4)

    def push_up(self, i):
        """
        向上更新节点信息 - 累加和信息的汇总
        
        :param i: 当前节点编号
        """
        # 父范围的累加和 = 左范围累加和 + 右范围累加和
        self.sum[i] = self.sum[i << 1] + self.sum[i << 1 | 1]

    def push_down(self, i, ln, rn):
        """
        向下传递懒标记
        
        :param i:  当前节点编号
        :param ln: 左子树节点数量
        :param rn: 右子树节点数量
        """
        if self.add[i] != 0:
            # 发左
            self.lazy(i << 1, self.add[i], ln)
            # 发右
            self.lazy(i << 1 | 1, self.add[i], rn)
            # 父范围懒信息清空
            self.add[i] = 0

    def lazy(self, i, v, n):
        """
        懒标记操作
        
        :param i: 节点编号
        :param v: 增加的值
        :param n: 节点对应的区间长度
        """
        self.sum[i] += v * n
        self.add[i] += v

    def build(self, arr, l, r, i):
        """
        建树
        
        :param arr: 原始数组
        :param l:   当前区间左端点
        :param r:   当前区间右端点
        :param i:   当前节点编号
        """
        # 参数校验
        if not arr or l < 0 or r >= len(arr) or l > r:
            raise ValueError("参数无效")
            
        if l == r:
            self.sum[i] = arr[l]
        else:
            mid = (l + r) >> 1
            self.build(arr, l, mid, i << 1)
            self.build(arr, mid + 1, r, i << 1 | 1)
            self.push_up(i)
        self.add[i] = 0

    def add_single(self, idx, val, l, r, i):
        """
        单点增加 - 将索引idx处的值增加val
        
        :param idx: 要增加的索引
        :param val: 增加的值
        :param l:   当前区间左端点
        :param r:   当前区间右端点
        :param i:   当前节点编号
        """
        # 参数校验
        if idx < 0 or idx >= self.n or val < 0:
            raise ValueError("索引或值无效")
            
        if l == r:
            self.sum[i] += val
        else:
            mid = (l + r) >> 1
            if idx <= mid:
                self.add_single(idx, val, l, mid, i << 1)
            else:
                self.add_single(idx, val, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def sub_single(self, idx, val, l, r, i):
        """
        单点减少 - 将索引idx处的值减少val
        
        :param idx: 要减少的索引
        :param val: 减少的值
        :param l:   当前区间左端点
        :param r:   当前区间右端点
        :param i:   当前节点编号
        """
        # 参数校验
        if idx < 0 or idx >= self.n or val < 0:
            raise ValueError("索引或值无效")
            
        if l == r:
            self.sum[i] -= val
        else:
            mid = (l + r) >> 1
            if idx <= mid:
                self.sub_single(idx, val, l, mid, i << 1)
            else:
                self.sub_single(idx, val, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def query(self, jobl, jobr, l, r, i):
        """
        查询累加和
        
        :param jobl: 查询区间左端点
        :param jobr: 查询区间右端点
        :param l:    当前区间左端点
        :param r:    当前区间右端点
        :param i:    当前节点编号
        :return: 区间和
        :rtype: int
        """
        # 参数校验
        if jobl < 0 or jobr >= self.n or jobl > jobr:
            raise ValueError("查询区间无效")
            
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


# 主函数 - 用于测试
if __name__ == "__main__":
    print("开始测试 HDU 1166 敌兵布阵")
    
    # 示例测试
    arr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    n = len(arr)
    
    seg_tree = HDU1166_SegmentTree(n)
    seg_tree.build(arr, 0, n - 1, 1)
    
    # 查询 [1,3] (转换为0索引为[0,2])
    result1 = seg_tree.query(0, 2, 0, n - 1, 1)
    print("查询 [1,3]:", result1)  # 应该输出6 (1+2+3)
    
    # 增加第3个营地6个人 (转换为0索引为2)
    seg_tree.add_single(2, 6, 0, n - 1, 1)
    
    # 查询 [2,7] (转换为0索引为[1,6])
    result2 = seg_tree.query(1, 6, 0, n - 1, 1)
    print("增加后查询 [2,7]:", result2)  # 应该输出2+3+10+5+6+7=33
    
    # 减少第10个营地2个人 (转换为0索引为9)
    seg_tree.sub_single(9, 2, 0, n - 1, 1)
    
    # 增加第6个营地3个人 (转换为0索引为5)
    seg_tree.add_single(5, 3, 0, n - 1, 1)
    
    # 查询 [3,10] (转换为0索引为[2,9])
    result3 = seg_tree.query(2, 9, 0, n - 1, 1)
    print("操作后查询 [3,10]:", result3)
    
    print("测试结果:", "通过" if result1 == 6 and result2 == 33 and result3 == 59 else "失败")

    # 边界测试
    arr2 = [5]
    n2 = len(arr2)
    seg_tree2 = HDU1166_SegmentTree(n2)
    seg_tree2.build(arr2, 0, n2 - 1, 1)
    
    result4 = seg_tree2.query(0, 0, 0, n2 - 1, 1)
    print("单元素查询:", result4)  # 应该输出5
    
    seg_tree2.add_single(0, 3, 0, n2 - 1, 1)
    result5 = seg_tree2.query(0, 0, 0, n2 - 1, 1)
    print("单元素增加后查询:", result5)  # 应该输出8
    
    seg_tree2.sub_single(0, 2, 0, n2 - 1, 1)
    result6 = seg_tree2.query(0, 0, 0, n2 - 1, 1)
    print("单元素减少后查询:", result6)  # 应该输出6
    
    print("边界测试结果:", "通过" if result4 == 5 and result5 == 8 and result6 == 6 else "失败")
    print()
    
    # 异常处理测试
    try:
        seg_tree3 = HDU1166_SegmentTree(-1)
        print("异常测试1: 失败 - 应该抛出异常")
    except Exception as e:
        print("异常测试1: 通过 -", type(e).__name__)
        
    try:
        arr4 = [1, 2, 3]
        seg_tree4 = HDU1166_SegmentTree(len(arr4))
        seg_tree4.build(arr4, 0, len(arr4) - 1, 1)
        seg_tree4.query(2, 1, 0, len(arr4) - 1, 1)  # 无效区间
        print("异常测试2: 失败 - 应该抛出异常")
    except Exception as e:
        print("异常测试2: 通过 -", type(e).__name__)
    
    print("测试完成")