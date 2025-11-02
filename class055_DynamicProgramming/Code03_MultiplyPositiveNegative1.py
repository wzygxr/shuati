#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
相乘为正或负的子数组数量 - Python实现
给定一个长度为n的数组arr，其中所有值都不是0
返回有多少个子数组相乘的结果是正
返回有多少个子数组相乘的结果是负
1 <= n <= 10^6
-10^9 <= arr[i] <= +10^9，arr[i]一定不是0
来自真实大厂笔试，对数器验证

算法思路：
1. 使用前缀和思想，维护当前位置之前正数和负数的子数组数量
2. 遍历数组，维护一个变量cur表示到当前位置的累积符号（0表示正，1表示负）
3. cnt[0]表示累积符号为正的前缀数量，cnt[1]表示累积符号为负的前缀数量
4. 对于当前位置i：
   - 如果arr[i]为正数，符号不变，cur保持不变
   - 如果arr[i]为负数，符号改变，cur ^= 1
5. 如果当前累积符号为cur，那么：
   - 与之前累积符号为cur的前缀组合，乘积为正数
   - 与之前累积符号为cur^1的前缀组合，乘积为负数
6. 更新cnt数组

时间复杂度：O(n)，只需要遍历一次数组
空间复杂度：O(1)，只使用了常数额外空间
"""

import random
import time
from typing import List

class MultiplyPositiveNegative:
    """
    相乘为正或负的子数组数量解决方案
    """
    
    def num(self, arr: List[int]) -> List[int]:
        """
        计算正数和负数子数组的数量
        
        Args:
            arr: 输入数组，所有元素都不为0
            
        Returns:
            List[int]: [正数子数组数量, 负数子数组数量]
        """
        # cnt[0]: 累积符号为正的前缀数量
        # cnt[1]: 累积符号为负的前缀数量
        cnt = [1, 0]  # 初始化，空数组乘积为正数
        
        ans1 = 0  # 正数子数组数量
        ans2 = 0  # 负数子数组数量
        cur = 0   # 当前累积符号，0表示正，1表示负
        
        for num in arr:
            # 如果当前元素为负数，改变符号
            cur ^= 0 if num > 0 else 1
            
            # 与之前相同符号的前缀组合，乘积为正数
            ans1 += cnt[cur]
            # 与之前不同符号的前缀组合，乘积为负数
            ans2 += cnt[cur ^ 1]
            
            # 更新cnt数组
            cnt[cur] += 1
        
        return [ans1, ans2]
    
    def right(self, arr: List[int]) -> List[int]:
        """
        暴力方法 - 用于验证
        时间复杂度：O(n^2)
        空间复杂度：O(1)
        
        Args:
            arr: 输入数组
            
        Returns:
            List[int]: [正数子数组数量, 负数子数组数量]
        """
        n = len(arr)
        ans1 = 0
        ans2 = 0
        
        for i in range(n):
            cur = 1
            for j in range(i, n):
                cur = cur * arr[j]
                if cur > 0:
                    ans1 += 1
                else:
                    ans2 += 1
        
        return [ans1, ans2]
    
    def random_array(self, n: int, v: int) -> List[int]:
        """
        生成随机数组用于测试
        
        Args:
            n: 数组长度
            v: 数值范围[-v, v]，但不包含0
            
        Returns:
            List[int]: 随机数组
        """
        arr = []
        for _ in range(n):
            num = 0
            while num == 0:
                num = random.randint(-v, v)
            arr.append(num)
        return arr
    
    def test(self, n: int = 20, v: int = 10, test_time: int = 10000):
        """
        运行正确性测试
        
        Args:
            n: 最大数组长度
            v: 数值范围
            test_time: 测试次数
        """
        print("测试开始")
        start_time = time.time()
        
        for _ in range(test_time):
            size = random.randint(0, n)
            arr = self.random_array(size, v)
            ans1 = self.num(arr)
            ans2 = self.right(arr)
            
            if ans1 != ans2:
                print("出错了!")
                print(f"数组: {arr}")
                print(f"算法结果: {ans1}")
                print(f"暴力结果: {ans2}")
                return
        
        end_time = time.time()
        print(f"测试结束，耗时: {end_time - start_time:.2f}秒")
    
    def performance_test(self, n: int = 1000000, v: int = 1000):
        """
        性能测试：大规模数据
        
        Args:
            n: 数组长度
            v: 数值范围
        """
        print("生成大规模测试数据...")
        arr = self.random_array(n, v)
        
        print("开始性能测试...")
        start_time = time.time()
        
        result = self.num(arr)
        
        end_time = time.time()
        
        print("性能测试结果:")
        print(f"正数子数组数量: {result[0]}")
        print(f"负数子数组数量: {result[1]}")
        print(f"耗时: {end_time - start_time:.2f}秒")


class MultiplyPositiveNegativeOptimized:
    """
    优化版本：使用更简洁的代码实现
    """
    
    def num(self, arr: List[int]) -> List[int]:
        """
        优化版本的实现
        
        Args:
            arr: 输入数组
            
        Returns:
            List[int]: [正数子数组数量, 负数子数组数量]
        """
        cnt = [1, 0]  # cnt[0]:正, cnt[1]:负
        cur = ans1 = ans2 = 0
        
        for num in arr:
            cur ^= (num < 0)  # 负数时异或1，正数时异或0
            ans1 += cnt[cur]
            ans2 += cnt[cur ^ 1]
            cnt[cur] += 1
        
        return [ans1, ans2]


def test_basic():
    """基础测试"""
    solution = MultiplyPositiveNegative()
    
    # 测试用例1
    test1 = [1, -2, 3, -4, 5]
    result1 = solution.num(test1)
    print(f"测试数组: {test1}")
    print(f"正数子数组数量: {result1[0]}")
    print(f"负数子数组数量: {result1[1]}")
    
    # 测试用例2：全正数
    test2 = [1, 2, 3, 4, 5]
    result2 = solution.num(test2)
    print(f"\n测试数组(全正): {test2}")
    print(f"正数子数组数量: {result2[0]}")
    print(f"负数子数组数量: {result2[1]}")
    
    # 测试用例3：全负数
    test3 = [-1, -2, -3, -4, -5]
    result3 = solution.num(test3)
    print(f"\n测试数组(全负): {test3}")
    print(f"正数子数组数量: {result3[0]}")
    print(f"负数子数组数量: {result3[1]}")


def test_edge_cases():
    """边界测试"""
    solution = MultiplyPositiveNegative()
    
    # 空数组
    test1 = []
    result1 = solution.num(test1)
    print(f"空数组结果: {result1}")
    
    # 单个元素
    test2 = [1]
    result2 = solution.num(test2)
    print(f"单个正数结果: {result2}")
    
    test3 = [-1]
    result3 = solution.num(test3)
    print(f"单个负数结果: {result3}")


if __name__ == "__main__":
    solution = MultiplyPositiveNegative()
    
    # 基础测试
    print("=== 基础测试 ===")
    test_basic()
    
    # 边界测试
    print("\n=== 边界测试 ===")
    test_edge_cases()
    
    # 正确性测试
    print("\n=== 正确性测试 ===")
    solution.test()
    
    # 性能测试
    print("\n=== 性能测试 ===")
    solution.performance_test()
    
    # 优化版本测试
    print("\n=== 优化版本测试 ===")
    solution_opt = MultiplyPositiveNegativeOptimized()
    test1 = [1, -2, 3, -4, 5]
    result_opt = solution_opt.num(test1)
    print(f"优化版本结果: {result_opt}")


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

4. 异常处理：
   - 使用try-except处理可能的异常
   - 添加输入合法性检查
   - 使用logging模块记录错误信息

5. 调试技巧：
   - 使用pdb进行调试
   - 添加assert断言验证中间结果
   - 使用timeit模块测试性能

6. Python特有优化：
   - 使用numpy处理数值计算（如果允许）
   - 使用functools.lru_cache进行记忆化
   - 注意Python的GIL对多线程的影响

7. 算法优化思路：
   - 原始暴力方法的时间复杂度为O(n^2)，无法处理大规模数据
   - 优化方法利用前缀和思想，将时间复杂度降至O(n)
   - 关键思路是维护累积符号的前缀数量
   - 这种方法避免了重复计算，大大提高了效率

8. 相关题目扩展：
   - LeetCode 152: Maximum Product Subarray
   - LeetCode 53: Maximum Subarray  
   - Codeforces 1215B: The Number of Products
   - 这些题目都涉及子数组乘积或和的统计，可以对比学习

9. 笔试面试技巧：
   - 理解前缀和思想的应用场景
   - 掌握符号累积的巧妙用法
   - 能够分析时间复杂度和空间复杂度
   - 能够处理边界情况和极端输入

10. 数学原理：
    - 两个正数相乘为正，两个负数相乘为正
    - 正数和负数相乘为负
    - 利用异或运算可以高效地切换符号状态

11. 工程化考量：
    - 代码的可读性和可维护性
    - 异常处理和边界条件检查
    - 性能优化和内存管理
    - 测试用例的覆盖度

12. 跨语言对比：
    - Python代码更简洁，但运行速度较慢
    - 在Python中需要注意整数溢出问题，Python的整数精度不会有问题
    - 对于大规模数据，C++版本通常更快
"""