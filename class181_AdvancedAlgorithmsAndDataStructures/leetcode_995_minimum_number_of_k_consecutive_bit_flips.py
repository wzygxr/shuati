#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 995 Minimum Number of K Consecutive Bit Flips

题目描述：
在仅包含 0 和 1 的数组 A 中，可以进行任意次数的翻转操作。
一次翻转操作选择长度为 K 的连续子数组，然后将该子数组中的每个值都翻转。
返回使数组变为全1所需的最少翻转次数。如果不可能，返回-1。

解题思路：
我们可以使用差分数组来优化翻转操作的记录。
1. 遍历数组，当遇到0时，需要进行翻转操作
2. 使用差分数组记录翻转操作的影响范围
3. 通过前缀和计算当前位置的实际翻转次数
4. 根据实际翻转次数判断当前位置的值

时间复杂度：O(n)
空间复杂度：O(n)
"""

class Solution:
    def min_k_bit_flips(self, A, K):
        """
        计算最少翻转次数
        
        Args:
            A: 输入数组，只包含0和1
            K: 翻转窗口大小
            
        Returns:
            最少翻转次数，如果不可能则返回-1
        """
        n = len(A)
        # 创建差分数组记录翻转操作
        diff = [0] * (n + 1)
        flips = 0  # 当前翻转次数
        result = 0  # 总翻转次数
        
        for i in range(n):
            # 通过差分数组计算当前位置的实际翻转次数
            flips += diff[i]
            
            # 判断当前位置的实际值
            # 如果A[i] + flips是偶数，说明实际值为0，需要翻转
            if (A[i] + flips) % 2 == 0:
                # 检查是否有足够的元素进行K长度的翻转
                if i + K > n:
                    return -1
                
                # 记录翻转操作
                diff[i] += 1
                diff[i + K] -= 1
                flips += 1
                result += 1
        
        return result


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    A1 = [0,1,0]
    K1 = 1
    print("测试用例1:")
    print("数组:", A1)
    print("K =", K1)
    print("结果:", solution.min_k_bit_flips(A1, K1))
    print()
    
    # 测试用例2
    A2 = [1,1,0]
    K2 = 2
    print("测试用例2:")
    print("数组:", A2)
    print("K =", K2)
    print("结果:", solution.min_k_bit_flips(A2, K2))
    print()
    
    # 测试用例3
    A3 = [0,0,0,1,0,1,1,0]
    K3 = 3
    print("测试用例3:")
    print("数组:", A3)
    print("K =", K3)
    print("结果:", solution.min_k_bit_flips(A3, K3))


if __name__ == "__main__":
    main()