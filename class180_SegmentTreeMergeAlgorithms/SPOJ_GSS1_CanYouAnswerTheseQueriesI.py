#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
SPOJ GSS1 - Can you answer these queries I
题目链接: https://www.spoj.com/problems/GSS1/

题目描述:
给定一个长度为n的整数序列a，需要处理m个查询。
每个查询给定两个整数l和r，要求找出区间[l,r]内的最大子段和。

解题思路:
这是一个经典的线段树维护区间最大子段和的问题。
对于每个线段树节点，我们需要维护以下信息：
1. 区间和(sum)
2. 区间最大子段和(max_sum)
3. 区间前缀最大和(prefix_max)
4. 区间后缀最大和(suffix_max)

合并两个子区间时：
- 区间和 = 左子区间和 + 右子区间和
- 区间最大子段和 = max(左子区间最大子段和, 右子区间最大子段和, 左子区间后缀最大和 + 右子区间前缀最大和)
- 区间前缀最大和 = max(左子区间前缀最大和, 左子区间和 + 右子区间前缀最大和)
- 区间后缀最大和 = max(右子区间后缀最大和, 右子区间和 + 左子区间后缀最大和)

时间复杂度分析:
- 建树: O(n)
- 区间查询: O(log n)

空间复杂度: O(n)

工程化考量:
1. 异常处理: 检查输入参数的有效性
2. 边界情况: 处理空数组、单个元素等情况
3. 性能优化: 使用结构体优化节点信息存储
4. 可测试性: 提供完整的测试用例覆盖各种场景
5. 可读性: 添加详细的注释说明设计思路和实现细节
6. 鲁棒性: 处理极端输入和非理想数据
"""


class SegmentNode:
    """线段树节点信息"""
    def __init__(self):
        self.sum = 0           # 区间和
        self.max_sum = 0       # 区间最大子段和
        self.prefix_max = 0    # 区间前缀最大和
        self.suffix_max = 0    # 区间后缀最大和


class GSS1SegmentTree:
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
        self.nodes = [SegmentNode() for _ in range(size * 4)]

    def push_up(self, i):
        """
        向上更新节点信息
        
        :param i: 当前节点编号
        """
        left_child = self.nodes[i << 1]
        right_child = self.nodes[i << 1 | 1]
        current_node = self.nodes[i]
        
        # 区间和 = 左子区间和 + 右子区间和
        current_node.sum = left_child.sum + right_child.sum
        
        # 区间最大子段和 = max(左子区间最大子段和, 右子区间最大子段和, 左子区间后缀最大和 + 右子区间前缀最大和)
        current_node.max_sum = max(
            left_child.max_sum,
            right_child.max_sum,
            left_child.suffix_max + right_child.prefix_max
        )
        
        # 区间前缀最大和 = max(左子区间前缀最大和, 左子区间和 + 右子区间前缀最大和)
        current_node.prefix_max = max(
            left_child.prefix_max,
            left_child.sum + right_child.prefix_max
        )
        
        # 区间后缀最大和 = max(右子区间后缀最大和, 右子区间和 + 左子区间后缀最大和)
        current_node.suffix_max = max(
            right_child.suffix_max,
            right_child.sum + left_child.suffix_max
        )

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
            
        current_node = self.nodes[i]
        if l == r:
            # 叶子节点
            current_node.sum = arr[l]
            current_node.max_sum = arr[l]
            current_node.prefix_max = max(0, arr[l])  # 前缀最大和可以为空(和为0)
            current_node.suffix_max = max(0, arr[l])  # 后缀最大和可以为空(和为0)
        else:
            mid = (l + r) >> 1
            self.build(arr, l, mid, i << 1)
            self.build(arr, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def query(self, jobl, jobr, l, r, i):
        """
        查询区间[jobl, jobr]内的最大子段和
        
        :param jobl: 查询区间左端点
        :param jobr: 查询区间右端点
        :param l:    当前区间左端点
        :param r:    当前区间右端点
        :param i:    当前节点编号
        :return: 区间最大子段和
        """
        if jobl <= l and r <= jobr:
            # 当前区间完全包含在查询区间内
            return self.nodes[i]
        else:
            mid = (l + r) >> 1
            if jobr <= mid:
                # 查询区间完全在左子树
                return self.query(jobl, jobr, l, mid, i << 1)
            elif jobl > mid:
                # 查询区间完全在右子树
                return self.query(jobl, jobr, mid + 1, r, i << 1 | 1)
            else:
                # 查询区间跨越左右子树
                left_result = self.query(jobl, jobr, l, mid, i << 1)
                right_result = self.query(jobl, jobr, mid + 1, r, i << 1 | 1)
                
                # 合并结果
                merged_result = SegmentNode()
                # 区间和 = 左子区间和 + 右子区间和
                merged_result.sum = left_result.sum + right_result.sum
                
                # 区间最大子段和 = max(左子区间最大子段和, 右子区间最大子段和, 左子区间后缀最大和 + 右子区间前缀最大和)
                merged_result.max_sum = max(
                    left_result.max_sum,
                    right_result.max_sum,
                    left_result.suffix_max + right_result.prefix_max
                )
                
                # 区间前缀最大和 = max(左子区间前缀最大和, 左子区间和 + 右子区间前缀最大和)
                merged_result.prefix_max = max(
                    left_result.prefix_max,
                    left_result.sum + right_result.prefix_max
                )
                
                # 区间后缀最大和 = max(右子区间后缀最大和, 右子区间和 + 左子区间后缀最大和)
                merged_result.suffix_max = max(
                    right_result.suffix_max,
                    right_result.sum + left_result.suffix_max
                )
                
                return merged_result


def solve_gss1(arr, queries):
    """
    解决SPOJ GSS1问题
    
    :param arr: 原始数组
    :param queries: 查询列表，每个查询为(l, r)
    :return: 查询结果列表
    """
    if not arr or not queries:
        return []
    
    # 初始化线段树
    seg_tree = GSS1SegmentTree(len(arr))
    seg_tree.build(arr, 0, len(arr) - 1, 1)
    
    # 处理查询
    results = []
    for l, r in queries:
        # 转换为0索引
        l_idx = l - 1
        r_idx = r - 1
        result_node = seg_tree.query(l_idx, r_idx, 0, len(arr) - 1, 1)
        results.append(result_node.max_sum)
        
    return results


def main():
    """
    主函数 - 处理输入输出
    """
    print("开始测试 SPOJ GSS1 - Can you answer these queries I")
    
    # 测试用例1
    arr1 = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    queries1 = [(1, 9)]  # 查询整个数组的最大子段和
    
    result1 = solve_gss1(arr1, queries1)
    print("测试用例1结果:", result1[0])  # 应该输出6 (子段[4,-1,2,1])
    
    # 测试用例2
    arr2 = [1, 2, 3, 4, 5]
    queries2 = [(2, 4)]  # 查询区间[2,4]的最大子段和
    
    result2 = solve_gss1(arr2, queries2)
    print("测试用例2结果:", result2[0])  # 应该输出9 (子段[2,3,4])
    
    # 测试用例3
    arr3 = [-1, -2, -3, -4, -5]
    queries3 = [(1, 5)]  # 查询整个数组的最大子段和
    
    result3 = solve_gss1(arr3, queries3)
    print("测试用例3结果:", result3[0])  # 应该输出-1 (单个元素-1)
    
    # 多查询测试
    arr4 = [1, -2, 3, -4, 5, -6, 7]
    queries4 = [(1, 3), (2, 5), (4, 7)]  # 多个查询
    
    results4 = solve_gss1(arr4, queries4)
    print("多查询测试结果:", results4)  # 应该输出[3, 5, 7]
    
    print("测试完成")


if __name__ == "__main__":
    main()