#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
LeetCode 1738: 寻找第K大的异或坐标值

解题思路：
1. 使用二维前缀异或和计算每个坐标的异或值
2. 使用最小堆维护前K个最大的异或值
3. 最终堆顶元素即为第K大的异或值

时间复杂度：O(m*n log k)
空间复杂度：O(k)
"""

import heapq
from typing import List


class Solution:
    """
    寻找第K大的异或坐标值的解决方案类
    
    该类提供了一个方法来解决LeetCode 1738题，
    通过计算二维数组中所有可能的子矩阵的异或和，
    并找出其中第K大的值。
    """
    
    def kthLargestValue(self, matrix: List[List[int]], k: int) -> int:
        """
        计算二维矩阵中所有可能的子矩阵的异或和，并返回第K大的值
        
        Args:
            matrix: 二维整数数组
            k: 需要返回的第K大元素的索引
            
        Returns:
            int: 所有可能的子矩阵异或和中第K大的值
            
        Raises:
            ValueError: 当输入参数无效时抛出
        """
        # 输入参数校验
        if not matrix or not matrix[0] or k <= 0:
            raise ValueError("输入参数无效：矩阵不能为空且k必须为正整数")
            
        m, n = len(matrix), len(matrix[0])
        
        # 检查k是否超过可能的子矩阵数量
        if k > m * n:
            raise ValueError("k值超过了矩阵中可能的子矩阵数量")
            
        # 初始化前缀异或和矩阵
        pre_xor = [[0] * (n + 1) for _ in range(m + 1)]
        
        # 最小堆，用于维护前K个最大的异或值
        min_heap = []
        
        # 计算前缀异或和并维护最小堆
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                # 计算当前位置的前缀异或和
                # pre_xor[i][j] 表示从 (0,0) 到 (i-1,j-1) 的子矩阵的异或和
                pre_xor[i][j] = pre_xor[i-1][j-1] ^ pre_xor[i-1][j] ^ pre_xor[i][j-1] ^ matrix[i-1][j-1]
                
                # 将当前异或值添加到最小堆
                heapq.heappush(min_heap, pre_xor[i][j])
                
                # 如果堆的大小超过k，弹出最小元素
                if len(min_heap) > k:
                    heapq.heappop(min_heap)
        
        # 堆顶元素即为第K大的异或值
        return min_heap[0]


class AlternativeSolution:
    """
    寻找第K大的异或坐标值的替代解决方案类
    
    该类提供了一种不使用堆的替代方法，
    而是收集所有异或值后排序获取第K大的元素。
    """
    
    def kthLargestValue(self, matrix: List[List[int]], k: int) -> int:
        """
        计算二维矩阵中所有可能的子矩阵的异或和，并返回第K大的值
        
        Args:
            matrix: 二维整数数组
            k: 需要返回的第K大元素的索引
            
        Returns:
            int: 所有可能的子矩阵异或和中第K大的值
            
        Raises:
            ValueError: 当输入参数无效时抛出
        """
        # 输入参数校验
        if not matrix or not matrix[0] or k <= 0:
            raise ValueError("输入参数无效：矩阵不能为空且k必须为正整数")
            
        m, n = len(matrix), len(matrix[0])
        
        # 检查k是否超过可能的子矩阵数量
        if k > m * n:
            raise ValueError("k值超过了矩阵中可能的子矩阵数量")
            
        # 初始化前缀异或和矩阵
        pre_xor = [[0] * (n + 1) for _ in range(m + 1)]
        
        # 存储所有异或值的列表
        values = []
        
        # 计算前缀异或和并收集所有异或值
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                pre_xor[i][j] = pre_xor[i-1][j-1] ^ pre_xor[i-1][j] ^ pre_xor[i][j-1] ^ matrix[i-1][j-1]
                values.append(pre_xor[i][j])
        
        # 排序并返回第K大的值
        values.sort(reverse=True)
        return values[k - 1]


class OptimizedSolution:
    """
    寻找第K大的异或坐标值的优化解决方案类
    
    该类在基础解法上进行了优化，减少了一些不必要的内存使用。
    """
    
    def kthLargestValue(self, matrix: List[List[int]], k: int) -> int:
        """
        计算二维矩阵中所有可能的子矩阵的异或和，并返回第K大的值（优化版本）
        
        Args:
            matrix: 二维整数数组
            k: 需要返回的第K大元素的索引
            
        Returns:
            int: 所有可能的子矩阵异或和中第K大的值
            
        Raises:
            ValueError: 当输入参数无效时抛出
        """
        # 输入参数校验
        if not matrix or not matrix[0] or k <= 0:
            raise ValueError("输入参数无效：矩阵不能为空且k必须为正整数")
            
        m, n = len(matrix), len(matrix[0])
        
        # 检查k是否超过可能的子矩阵数量
        if k > m * n:
            raise ValueError("k值超过了矩阵中可能的子矩阵数量")
            
        # 使用一维数组存储当前行的前缀异或和，节省空间
        prev_row = [0] * (n + 1)
        min_heap = []
        
        # 遍历每一行
        for i in range(m):
            # 当前行的前缀异或和
            curr_row = [0] * (n + 1)
            for j in range(n):
                # 计算当前位置的前缀异或和
                curr_row[j + 1] = curr_row[j] ^ prev_row[j + 1] ^ prev_row[j] ^ matrix[i][j]
                
                # 维护最小堆
                heapq.heappush(min_heap, curr_row[j + 1])
                if len(min_heap) > k:
                    heapq.heappop(min_heap)
            
            # 更新前一行的前缀异或和
            prev_row = curr_row
        
        return min_heap[0]


# 测试代码
def test_kth_largest_value():
    """
    测试寻找第K大的异或坐标值的函数
    """
    # 测试用例1：基本用例
    matrix1 = [[5, 2], [1, 6]]
    k1 = 1
    # 所有可能的子矩阵异或和：5, 2, 1, 6, 5^2, 1^6, 5^2^1^6
    # 即：5, 2, 1, 6, 7, 7, 0
    # 排序后：7, 7, 6, 5, 2, 1, 0
    # 第1大是7
    print("测试用例1：")
    print(f"矩阵: {matrix1}, k: {k1}")
    solution = Solution()
    result1 = solution.kthLargestValue(matrix1, k1)
    print(f"结果: {result1}")
    print(f"预期结果: 7, 测试{'通过' if result1 == 7 else '失败'}")
    print()
    
    # 测试用例2：k=2
    k2 = 2
    result2 = solution.kthLargestValue(matrix1, k2)
    print("测试用例2：")
    print(f"矩阵: {matrix1}, k: {k2}")
    print(f"结果: {result2}")
    print(f"预期结果: 7, 测试{'通过' if result2 == 7 else '失败'}")
    print()
    
    # 测试用例3：k=3
    k3 = 3
    result3 = solution.kthLargestValue(matrix1, k3)
    print("测试用例3：")
    print(f"矩阵: {matrix1}, k: {k3}")
    print(f"结果: {result3}")
    print(f"预期结果: 6, 测试{'通过' if result3 == 6 else '失败'}")
    print()
    
    # 测试用例4：3x3矩阵
    matrix4 = [[10, 8, 6], [3, 5, 7], [4, 9, 2]]
    k4 = 4
    solution4 = AlternativeSolution()
    result4 = solution4.kthLargestValue(matrix4, k4)
    print("测试用例4：")
    print(f"3x3矩阵, k: {k4}")
    print(f"结果: {result4}")
    print()
    
    # 测试用例5：单元素矩阵
    matrix5 = [[42]]
    k5 = 1
    solution5 = OptimizedSolution()
    result5 = solution5.kthLargestValue(matrix5, k5)
    print("测试用例5：")
    print(f"单元素矩阵, k: {k5}")
    print(f"结果: {result5}")
    print(f"预期结果: 42, 测试{'通过' if result5 == 42 else '失败'}")
    print()
    
    # 测试异常处理
    try:
        solution.kthLargestValue([], 1)
        print("测试用例6：空矩阵异常处理 - 失败")
    except ValueError:
        print("测试用例6：空矩阵异常处理 - 通过")
        
    try:
        solution.kthLargestValue(matrix1, 0)
        print("测试用例7：k=0异常处理 - 失败")
    except ValueError:
        print("测试用例7：k=0异常处理 - 通过")


if __name__ == "__main__":
    test_kth_largest_value()