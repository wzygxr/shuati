#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
相邻与结果不为0的最长子序列 - Python实现
给定一个长度为n的数组arr，你可以随意选择数字组成子序列
但是要求任意相邻的两个数&的结果不能是0，这样的子序列才是合法的
返回最长合法子序列的长度
1 <= n <= 10^5
0 <= arr[i] <= 10^9
测试链接 : https://www.luogu.com.cn/problem/P4310

算法思路：
1. 这是一个动态规划问题
2. 对于每个数，我们关注它的二进制表示中为1的位
3. dp[i]表示以第i位为结尾的最长子序列长度
4. 对于当前数num，我们找出它二进制表示中为1的位j
5. 找到所有以位j结尾的最长子序列长度，取最大值+1作为新的长度
6. 更新所有num中为1的位j对应的dp[j]

时间复杂度：O(n * 31) = O(n)，其中n是数组长度
空间复杂度：O(1)，只使用了固定大小的数组
"""

import random
import time
from typing import List

class LongestAddNotZero:
    """
    相邻与结果不为0的最长子序列解决方案
    """
    
    def compute(self, arr: List[int]) -> int:
        """
        计算最长合法子序列的长度
        
        Args:
            arr: 输入数组
            
        Returns:
            int: 最长合法子序列的长度
        """
        if not arr:
            return 0
            
        n = len(arr)
        # pre数组存储以每个二进制位结尾的最长子序列长度
        pre = [0] * 32
        
        for num in arr:
            cur = 1  # 当前数字可以单独构成一个子序列
            
            # 第一遍遍历：找到当前数之前，以num中任意为1的位结尾的最长子序列长度的最大值
            for j in range(31):
                # 如果num的第j位为1
                if (num >> j) & 1:
                    # 更新cur为以第j位结尾的最长子序列长度+1的最大值
                    cur = max(cur, pre[j] + 1)
            
            # 第二遍遍历：更新pre数组
            for j in range(31):
                # 如果num的第j位为1
                if (num >> j) & 1:
                    # 更新以第j位结尾的最长子序列长度
                    pre[j] = max(pre[j], cur)
        
        # 找到所有位结尾的最长子序列长度的最大值
        return max(pre)
    
    def compute_optimized(self, arr: List[int]) -> int:
        """
        优化版本：使用更简洁的代码实现
        
        Args:
            arr: 输入数组
            
        Returns:
            int: 最长合法子序列的长度
        """
        if not arr:
            return 0
            
        dp = [0] * 32
        
        for num in arr:
            cur = 1
            
            # 找到当前数可以接在哪些位后面
            for j in range(31):
                if (num >> j) & 1:
                    cur = max(cur, dp[j] + 1)
            
            # 更新所有为1的位
            for j in range(31):
                if (num >> j) & 1:
                    dp[j] = max(dp[j], cur)
        
        return max(dp)
    
    def brute_force(self, arr: List[int]) -> int:
        """
        暴力方法 - 用于验证
        时间复杂度：O(2^n)，指数级复杂度，仅用于小规模测试
        
        Args:
            arr: 输入数组
            
        Returns:
            int: 最长合法子序列的长度
        """
        n = len(arr)
        max_len = 0
        
        # 枚举所有子序列
        for mask in range(1, 1 << n):
            seq = []
            for i in range(n):
                if mask & (1 << i):
                    seq.append(arr[i])
            
            # 检查子序列是否合法
            valid = True
            for i in range(1, len(seq)):
                if (seq[i-1] & seq[i]) == 0:
                    valid = False
                    break
            
            if valid:
                max_len = max(max_len, len(seq))
        
        return max_len
    
    def test_basic(self):
        """基础测试"""
        # 测试用例1
        test1 = [1, 2, 3, 4, 5]
        result1 = self.compute(test1)
        result1_opt = self.compute_optimized(test1)
        print(f"测试数组: {test1}")
        print(f"结果: {result1}, 优化版本: {result1_opt}")
        
        # 测试用例2：包含0的情况
        test2 = [0, 1, 2, 0, 4]
        result2 = self.compute(test2)
        result2_opt = self.compute_optimized(test2)
        print(f"测试数组(含0): {test2}")
        print(f"结果: {result2}, 优化版本: {result2_opt}")
        
        # 测试用例3：全0的情况
        test3 = [0, 0, 0, 0]
        result3 = self.compute(test3)
        result3_opt = self.compute_optimized(test3)
        print(f"测试数组(全0): {test3}")
        print(f"结果: {result3}, 优化版本: {result3_opt}")
        
        # 测试用例4：单个元素
        test4 = [1]
        result4 = self.compute(test4)
        result4_opt = self.compute_optimized(test4)
        print(f"测试数组(单个): {test4}")
        print(f"结果: {result4}, 优化版本: {result4_opt}")
    
    def test_validation(self):
        """正确性验证（小规模）"""
        print("\n=== 正确性验证 ===")
        
        # 小规模测试用例
        test_cases = [
            [1, 2, 3],
            [1, 3, 7],
            [2, 4, 8],
            [1, 2, 4, 8]
        ]
        
        for i, test_case in enumerate(test_cases):
            result = self.compute(test_case)
            result_opt = self.compute_optimized(test_case)
            result_brute = self.brute_force(test_case)
            
            print(f"测试用例 {i+1}: {test_case}")
            print(f"算法结果: {result}, 优化版本: {result_opt}, 暴力结果: {result_brute}")
            
            if result == result_brute and result_opt == result_brute:
                print("✓ 结果正确")
            else:
                print("✗ 结果错误")
    
    def performance_test(self, n: int = 100000):
        """性能测试"""
        print("\n=== 性能测试 ===")
        
        # 生成测试数据
        print("生成测试数据...")
        arr = [random.randint(0, 10**9) for _ in range(n)]
        
        # 测试标准版本
        print("测试标准版本...")
        start_time = time.time()
        result1 = self.compute(arr)
        end_time = time.time()
        time1 = end_time - start_time
        
        # 测试优化版本
        print("测试优化版本...")
        start_time = time.time()
        result2 = self.compute_optimized(arr)
        end_time = time.time()
        time2 = end_time - start_time
        
        print(f"测试数据规模: {n}")
        print(f"标准版本结果: {result1}, 耗时: {time1:.4f}秒")
        print(f"优化版本结果: {result2}, 耗时: {time2:.4f}秒")
        
        if result1 == result2:
            print("✓ 两个版本结果一致")
        else:
            print("✗ 两个版本结果不一致")


def analyze_algorithm():
    """算法分析"""
    print("\n=== 算法分析 ===")
    
    print("1. 算法思路：")
    print("   - 利用二进制位状态来维护子序列信息")
    print("   - 每个二进制位记录以该位结尾的最长子序列长度")
    print("   - 对于每个数字，找到可以接在哪些位后面")
    
    print("\n2. 时间复杂度分析：")
    print("   - 遍历数组: O(n)")
    print("   - 每个数字检查31个二进制位: O(31)")
    print("   - 总时间复杂度: O(n * 31) = O(n)")
    
    print("\n3. 空间复杂度分析：")
    print("   - 使用固定大小的数组存储二进制位状态: O(32) = O(1)")
    
    print("\n4. 关键技巧：")
    print("   - 将子序列约束条件转化为二进制位约束")
    print("   - 利用位运算高效检查二进制位")
    print("   - 维护每个二进制位的最优解")


if __name__ == "__main__":
    solution = LongestAddNotZero()
    
    # 基础测试
    print("=== 基础测试 ===")
    solution.test_basic()
    
    # 正确性验证
    solution.test_validation()
    
    # 性能测试
    solution.performance_test(100000)
    
    # 算法分析
    analyze_algorithm()


"""
Python工程化实战建议：

1. 类型注解：
   - 使用typing模块提供类型提示，提高代码可读性
   - 类型注解有助于IDE的智能提示和代码检查

2. 内存管理：
   - Python有自动垃圾回收，但要注意循环引用
   - 对于大规模数据，使用生成器表达式节省内存
   - 注意列表的深拷贝和浅拷贝区别

3. 性能优化：
   - 使用局部变量访问比全局变量更快
   - 避免在循环中重复计算相同的表达式
   - 使用列表推导式比循环更快

4. 位运算技巧：
   - 使用位运算检查二进制位，比除法取模更高效
   - 注意Python的整数是任意精度的，但位运算只关注32位
   - 使用(num >> j) & 1检查第j位是否为1

5. 异常处理：
   - 使用try-except处理可能的异常
   - 添加输入合法性检查
   - 使用logging模块记录错误信息

6. 调试技巧：
   - 使用pdb进行调试
   - 添加assert断言验证中间结果
   - 使用timeit模块测试性能

7. 算法优化思路：
   - 原始暴力方法的时间复杂度为O(2^n)，无法处理大规模数据
   - 优化方法利用二进制位状态，将时间复杂度降至O(n)
   - 关键思路是维护以每个二进制位结尾的最长子序列长度
   - 这种方法避免了枚举所有子序列，大大提高了效率

8. 相关题目扩展：
   - LeetCode 300: Longest Increasing Subsequence
   - LeetCode 128: Longest Consecutive Sequence
   - LeetCode 152: Maximum Product Subarray
   - 这些题目都涉及子序列或子数组的统计，可以对比学习

9. 数学原理：
   - 两个数相与不为0，意味着它们至少有一个相同的二进制位为1
   - 子序列中相邻数字的约束条件可以转化为二进制位的约束
   - 利用二进制位状态可以高效地维护子序列信息

10. 工程化考量：
    - 代码的可读性和可维护性
    - 异常处理和边界条件检查
    - 性能优化和内存管理
    - 测试用例的覆盖度

11. 跨语言对比：
    - Python代码更简洁，但运行速度较慢
    - 在Python中需要注意整数溢出问题，Python的整数精度不会有问题
    - 对于大规模数据，C++版本通常更快

12. 笔试面试技巧：
    - 理解二进制位状态的应用场景
    - 掌握位运算的巧妙用法
    - 能够分析时间复杂度和空间复杂度
    - 能够处理边界情况和极端输入
"""