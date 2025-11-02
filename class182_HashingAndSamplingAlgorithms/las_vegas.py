#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
拉斯维加斯算法 (Las Vegas Algorithm)

算法原理：
拉斯维加斯算法是一种随机化算法，它总是给出正确或最优的结果，
但运行时间是随机的。算法可能会"失败"并报告失败，但只要成功就保证结果正确。

算法特点：
1. 结果总是正确的
2. 运行时间是随机变量
3. 可能会失败（返回特殊值表示失败）
4. 通过重复执行可以降低失败概率

应用场景：
- 快速排序的随机化版本
- 素数测试
- 图论算法
- 计算几何
- 寻找数组中第k小元素

算法流程：
1. 随机化选择策略
2. 执行确定性计算
3. 验证结果正确性
4. 如果正确则返回，否则重新尝试

时间复杂度：期望O(f(n))，最坏情况可能无限
空间复杂度：取决于具体实现
"""

import random
from typing import List


class LasVegas:
    def __init__(self):
        """构造函数"""
        self.random = random.Random()
    
    def quick_select(self, array: List[int], k: int) -> int:
        """
        拉斯维加斯快速选择算法 - 寻找数组中第k小的元素
        
        Args:
            array: 输入数组
            k: 第k小元素（从0开始计数）
            
        Returns:
            第k小的元素
        """
        if k < 0 or k >= len(array):
            raise ValueError("k超出数组范围")
        
        arr = array.copy()  # 避免修改原数组
        return self._quick_select_helper(arr, 0, len(arr) - 1, k)
    
    def _quick_select_helper(self, array: List[int], left: int, right: int, k: int) -> int:
        """
        快速选择算法辅助函数
        
        Args:
            array: 数组
            left: 左边界
            right: 右边界
            k: 第k小元素
            
        Returns:
            第k小的元素
        """
        if left == right:
            return array[left]
        
        # 随机选择基准元素
        pivot_index = self._randomized_partition(array, left, right)
        
        # 根据基准元素位置决定下一步
        if k == pivot_index:
            return array[k]
        elif k < pivot_index:
            return self._quick_select_helper(array, left, pivot_index - 1, k)
        else:
            return self._quick_select_helper(array, pivot_index + 1, right, k)
    
    def _randomized_partition(self, array: List[int], left: int, right: int) -> int:
        """
        随机化分区函数
        
        Args:
            array: 数组
            left: 左边界
            right: 右边界
            
        Returns:
            基准元素的最终位置
        """
        # 随机选择基准元素并与最后一个元素交换
        random_index = left + self.random.randint(0, right - left)
        array[random_index], array[right] = array[right], array[random_index]
        
        return self._partition(array, left, right)
    
    def _partition(self, array: List[int], left: int, right: int) -> int:
        """
        分区函数
        
        Args:
            array: 数组
            left: 左边界
            right: 右边界
            
        Returns:
            基准元素的最终位置
        """
        pivot = array[right]  # 选择最后一个元素作为基准
        i = left - 1
        
        for j in range(left, right):
            if array[j] <= pivot:
                i += 1
                array[i], array[j] = array[j], array[i]
        
        array[i + 1], array[right] = array[right], array[i + 1]
        return i + 1
    
    def miller_rabin_test(self, n: int, k: int) -> bool:
        """
        拉斯维加斯素数测试 - Miller-Rabin素数测试
        
        Args:
            n: 待测试的数
            k: 测试轮数
            
        Returns:
            是否可能为素数
        """
        if n < 2:
            return False
        if n == 2 or n == 3:
            return True
        if n % 2 == 0:
            return False
        
        # 将 n-1 写成 d * 2^r 的形式
        d = n - 1
        r = 0
        while d % 2 == 0:
            d //= 2
            r += 1
        
        # 进行k轮测试
        for _ in range(k):
            if not self._miller_rabin_round(n, d, r):
                return False  # 肯定是合数
        
        return True  # 可能是素数
    
    def _miller_rabin_round(self, n: int, d: int, r: int) -> bool:
        """
        Miller-Rabin单轮测试
        
        Args:
            n: 待测试的数
            d: n-1 = d * 2^r 中的d
            r: n-1 = d * 2^r 中的r
            
        Returns:
            单轮测试结果
        """
        # 随机选择 a ∈ [2, n-2]
        a = 2 + self.random.randint(0, n - 4)
        
        # 计算 x = a^d mod n
        x = pow(a, d, n)
        
        if x == 1 or x == n - 1:
            return True  # 通过测试
        
        # 重复平方 r-1 次
        for _ in range(r - 1):
            x = pow(x, 2, n)
            if x == n - 1:
                return True  # 通过测试
        
        return False  # 未通过测试，是合数
    
    def _modular_exponentiation(self, base: int, exponent: int, modulus: int) -> int:
        """
        模幂运算 (a^b mod m)
        
        Args:
            base: 底数
            exponent: 指数
            modulus: 模数
            
        Returns:
            (base^exponent) mod modulus
        """
        return pow(base, exponent, modulus)


def main():
    """测试示例"""
    lv = LasVegas()
    
    print("=== 拉斯维加斯算法测试 ===")
    
    # 测试快速选择算法
    print("\n1. 快速选择算法测试:")
    array = [3, 6, 8, 10, 1, 2, 1]
    print("原数组:", array)
    
    for k in range(len(array)):
        test_array = array.copy()
        result = lv.quick_select(test_array, k)
        print(f"第{k}小的元素: {result}")
    
    # 验证结果正确性
    sorted_array = sorted(array)
    print("排序后数组:", sorted_array)
    
    # 测试素数测试算法
    print("\n2. Miller-Rabin素数测试:")
    test_numbers = [17, 18, 97, 100, 101, 982451653, 982451654]
    rounds = 10
    
    for num in test_numbers:
        is_prime = lv.miller_rabin_test(num, rounds)
        print(f"{num} {'是' if is_prime else '不是'}素数")
    
    # 性能测试
    print("\n3. 性能测试:")
    sizes = [1000, 10000, 100000]
    for size in sizes:
        # 生成随机数组
        random.seed(42)  # 固定种子以保证可重复性
        test_array = [random.randint(0, 1000000) for _ in range(size)]
        
        # 测试快速选择算法性能
        import time
        start_time = time.time()
        median = lv.quick_select(test_array, size // 2)
        end_time = time.time()
        
        print(f"数组大小: {size}, 中位数: {median}, 时间: {(end_time - start_time) * 1000:.2f} ms")


if __name__ == "__main__":
    main()