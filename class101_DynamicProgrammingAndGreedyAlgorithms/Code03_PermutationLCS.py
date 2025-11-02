#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
两个排列的最长公共子序列长度 - Python实现

问题描述:
给出由1~n这些数字组成的两个排列
求它们的最长公共子序列长度

解题思路:
利用排列的特殊性质，将LCS问题转化为LIS问题
1. 将第二个排列转换为第一个排列中对应数字的位置
2. 问题转化为求位置序列的最长递增子序列
3. 使用贪心+二分查找求解LIS

约束条件:
1 <= n <= 10^5

测试链接: https://www.luogu.com.cn/problem/P1439

工程化考量:
1. 使用类型注解提高代码可读性
2. 添加输入验证和边界检查
3. 实现完整的单元测试
4. 提供性能测试功能
5. 使用bisect模块简化二分查找实现
"""

from typing import List
import bisect
import time

class Code03_PermutationLCS:
    """
    排列LCS算法解决方案类
    提供基于LIS变换的高效解法
    """
    
    @staticmethod
    def compute(a: List[int], b: List[int]) -> int:
        """
        计算两个排列的最长公共子序列长度
        
        算法原理:
        利用排列的特殊性质，将LCS问题转化为LIS问题
        1. 创建位置映射: where[ai] = i
        2. 将第二个排列转换为位置序列: b'[i] = where[b[i]]
        3. 问题转化为求位置序列的最长递增子序列
        4. 则LCS(A, B) = LIS(b')
        
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        
        Args:
            a: 第一个排列
            b: 第二个排列
            
        Returns:
            int: 最长公共子序列长度
        """
        # 输入验证
        if not a or not b or len(a) != len(b):
            return 0
            
        n = len(a)
        
        # 创建位置映射: where[ai] = i
        where = [0] * (n + 1)
        for i, num in enumerate(a):
            where[num] = i
            
        # 将第二个排列转换为位置序列
        pos = [where[num] for num in b]
        
        # 计算位置序列的最长递增子序列
        return Code03_PermutationLCS.lis(pos)
        
    @staticmethod
    def lis(nums: List[int]) -> int:
        """
        计算最长递增子序列长度
        使用贪心+二分查找算法
        
        算法思路:
        维护一个数组tails，tails[i]表示长度为i+1的递增子序列的最小结尾元素
        遍历序列，对于每个元素:
        1. 如果大于tails的最后一个元素，扩展tails
        2. 否则替换tails中第一个大于等于该元素的位置
        
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        if not nums:
            return 0
            
        tails = []
        
        for num in nums:
            # 使用二分查找找到插入位置
            idx = bisect.bisect_left(tails, num)
            
            if idx == len(tails):
                # 可以扩展tails
                tails.append(num)
            else:
                # 替换该位置，使得结尾元素更小
                tails[idx] = num
                
        return len(tails)
        
    @staticmethod
    def longest_common_subsequence(text1: str, text2: str) -> int:
        """
        类似题目1: 最长公共子序列（LeetCode 1143）
        经典LCS问题的动态规划解法
        
        时间复杂度: O(m × n)
        空间复杂度: O(min(m, n))
        """
        m, n = len(text1), len(text2)
        if m == 0 or n == 0:
            return 0
            
        # 确保text1是较短的字符串，优化空间
        if m < n:
            return Code03_PermutationLCS.longest_common_subsequence(text2, text1)
            
        # 使用一维数组进行空间优化
        dp = [0] * (n + 1)
        
        for i in range(1, m + 1):
            prev = 0  # 保存dp[i-1][j-1]的值
            for j in range(1, n + 1):
                temp = dp[j]  # 保存当前dp[j]的值
                if text1[i-1] == text2[j-1]:
                    dp[j] = prev + 1
                else:
                    dp[j] = max(dp[j], dp[j-1])
                prev = temp
                
        return dp[n]
        
    @staticmethod
    def find_length(A: List[int], B: List[int]) -> int:
        """
        类似题目2: 最长重复子数组（LeetCode 718）
        求两个数组的最长公共连续子数组
        
        时间复杂度: O(m × n)
        空间复杂度: O(n)
        """
        m, n = len(A), len(B)
        if m == 0 or n == 0:
            return 0
            
        # 使用一维数组进行空间优化
        dp = [0] * (n + 1)
        max_len = 0
        
        for i in range(1, m + 1):
            prev = 0
            for j in range(1, n + 1):
                temp = dp[j]
                if A[i-1] == B[j-1]:
                    dp[j] = prev + 1
                    max_len = max(max_len, dp[j])
                else:
                    dp[j] = 0
                prev = temp
                
        return max_len
        
    @staticmethod
    def min_distance(word1: str, word2: str) -> int:
        """
        类似题目3: 编辑距离（LeetCode 72）
        计算将word1转换为word2所需的最少操作数
        
        时间复杂度: O(m × n)
        空间复杂度: O(min(m, n))
        """
        m, n = len(word1), len(word2)
        if m == 0:
            return n
        if n == 0:
            return m
            
        # 确保word1是较短的字符串
        if m < n:
            return Code03_PermutationLCS.min_distance(word2, word1)
            
        # 使用一维数组进行空间优化
        dp = list(range(n + 1))
        
        for i in range(1, m + 1):
            prev = i - 1  # dp[i-1][0]
            dp[0] = i     # dp[i][0]
            
            for j in range(1, n + 1):
                temp = dp[j]  # 保存dp[i-1][j]
                if word1[i-1] == word2[j-1]:
                    dp[j] = prev
                else:
                    dp[j] = min(prev, dp[j], dp[j-1]) + 1
                prev = temp
                
        return dp[n]
        
    @staticmethod
    def test() -> None:
        """单元测试函数"""
        print("=== 测试排列LCS算法 ===")
        
        # 测试用例1
        a1 = [3, 2, 1, 4, 5]
        b1 = [1, 2, 3, 4, 5]
        result1 = Code03_PermutationLCS.compute(a1, b1)
        print(f"测试用例1结果: {result1} (期望: 3)")
        
        # 测试用例2
        a2 = [1, 2, 3, 4, 5]
        b2 = [5, 4, 3, 2, 1]
        result2 = Code03_PermutationLCS.compute(a2, b2)
        print(f"测试用例2结果: {result2} (期望: 1)")
        
        # 测试类似题目
        text1, text2 = "abcde", "ace"
        result3 = Code03_PermutationLCS.longest_common_subsequence(text1, text2)
        print(f"最长公共子序列结果: {result3} (期望: 3)")
        
        # 测试编辑距离
        word1, word2 = "horse", "ros"
        result4 = Code03_PermutationLCS.min_distance(word1, word2)
        print(f"编辑距离结果: {result4} (期望: 3)")
        
        print("=== 测试完成 ===")
        
    @staticmethod
    def performance_test() -> None:
        """性能测试函数"""
        print("\n=== 性能测试 ===")
        
        # 创建大规模测试数据
        n = 100000
        a = list(range(1, n + 1))
        b = list(range(n, 0, -1))  # 逆序排列
        
        start_time = time.time()
        result = Code03_PermutationLCS.compute(a, b)
        end_time = time.time()
        
        execution_time = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"大规模测试结果: {result}")
        print(f"执行时间: {execution_time:.2f}ms")
        
    @staticmethod
    def main() -> None:
        """主函数"""
        Code03_PermutationLCS.test()
        Code03_PermutationLCS.performance_test()

if __name__ == "__main__":
    Code03_PermutationLCS.main()

"""
调试技巧:
1. 打印中间的位置映射序列验证转换正确性
2. 使用小规模数据手动验证LIS算法
3. 对比不同算法的结果确保一致性

面试要点:
1. 理解排列LCS转化为LIS的数学原理
2. 掌握LIS的贪心+二分查找算法
3. 能够分析算法的时间复杂度和空间复杂度
4. 了解如何根据数据特征选择最优算法

语言特性差异:
1. Python使用bisect模块简化二分查找实现
2. Python列表推导式可以简化代码编写
3. Python的动态类型需要更多类型注解
4. Python的字符串处理与其他语言类似
"""