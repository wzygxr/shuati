#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
上学需要的最少跳跃能力 - Python实现
青蛙住在一条河边，家在0位置, 每天到河对岸的上学，学校在n位置
河里的石头排成了一条直线，青蛙每次跳跃必须落在一块石头或者岸上
给定一个长度为n-1的数组arr，表示1~n-1位置每块石头的高度数值
每次青蛙从一块石头起跳，这块石头的高度就会下降1
当石头的高度下降到0时，青蛙不能再跳到这块石头上，跳跃后使石头高度下降到0是允许的
青蛙一共需要去学校上x天课, 所以它需要往返x次，青蛙具有跳跃能力y, 它可以跳跃不超过y的距离
请问青蛙的跳跃能力至少是多少，才能用这些石头往返x次
1 <= n <= 10^5
1 <= arr[i] <= 10^4
1 <= x <= 10^9
测试链接 : https://www.luogu.com.cn/problem/P8775

算法思路：
1. 二分答案：将问题转化为判定问题，对于每个可能的跳跃能力y，验证是否能完成x次往返
2. 滑动窗口：对于给定的y，使用滑动窗口验证是否可行
3. 模拟往返：每次往返消耗窗口内石头的高度

时间复杂度：O(n * log(n))，其中n是石头的数量
空间复杂度：O(n)，需要复制数组进行验证
"""

import sys
from typing import List

class FrogToSchool:
    """
    青蛙上学问题解决方案
    """
    
    def compute(self, n: int, x: int, arr: List[int]) -> int:
        """
        计算青蛙的最小跳跃能力
        
        Args:
            n: 学校位置
            x: 往返次数
            arr: 石头高度数组，长度为n-1，表示位置1到n-1的石头高度
            
        Returns:
            int: 最小跳跃能力
        """
        # 将学校位置n的高度设为足够大
        arr = arr[:]  # 复制数组
        arr.extend([0] * (n - len(arr) + 1))  # 确保数组长度足够
        arr[n] = 2 * x  # 学校位置有足够的高度
        
        left, right = 1, n
        ans = 0
        
        # 二分查找最小跳跃能力
        while left <= right:
            mid = (left + right) // 2
            if self._can_finish(n, x, arr, mid):
                ans = mid
                right = mid - 1
            else:
                left = mid + 1
        
        return ans
    
    def _can_finish(self, n: int, x: int, arr: List[int], power: int) -> bool:
        """
        检查给定跳跃能力是否能完成x次往返
        
        Args:
            n: 学校位置
            x: 往返次数
            arr: 石头高度数组
            power: 跳跃能力
            
        Returns:
            bool: 是否能完成x次往返
        """
        # 复制数组，避免修改原数组
        temp = arr[:]
        
        # 模拟x次往返
        for _ in range(x):
            total = 0
            l, r = 1, 1
            
            # 滑动窗口验证
            while l <= n and r <= n:
                # 扩展右边界
                while r <= n and r - l < power:
                    total += temp[r]
                    r += 1
                
                # 检查窗口内石头是否足够
                if total >= 2:
                    need = min(total, 2)
                    total -= need
                    
                    # 消耗石头高度
                    j = l
                    while j < r and need > 0:
                        deduct = min(temp[j], need)
                        temp[j] -= deduct
                        need -= deduct
                        j += 1
                    
                    if need == 0:
                        break
                
                # 移动左边界
                total -= temp[l]
                l += 1
            
            # 如果无法完成本次往返
            if total < 2:
                return False
        
        return True


class FrogToSchoolOptimized:
    """
    优化版本：使用数学分析优化验证过程
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    
    def compute(self, n: int, x: int, arr: List[int]) -> int:
        """
        计算青蛙的最小跳跃能力（优化版本）
        
        Args:
            n: 学校位置
            x: 往返次数
            arr: 石头高度数组
            
        Returns:
            int: 最小跳跃能力
        """
        # 将学校位置n的高度设为足够大
        arr = arr[:]  # 复制数组
        arr.extend([0] * (n - len(arr) + 1))  # 确保数组长度足够
        arr[n] = 2 * x  # 学校位置有足够的高度
        
        # 计算每个位置最多能被使用的次数
        usage = [min(arr[i], x) for i in range(n + 1)]
        
        left, right = 1, n
        ans = 0
        
        # 二分查找
        while left <= right:
            mid = (left + right) // 2
            if self._can_finish_optimized(n, x, usage, mid):
                ans = mid
                right = mid - 1
            else:
                left = mid + 1
        
        return ans
    
    def _can_finish_optimized(self, n: int, x: int, usage: List[int], power: int) -> bool:
        """
        优化版本的验证函数
        
        Args:
            n: 学校位置
            x: 往返次数
            usage: 每个位置最多可被使用的次数
            power: 跳跃能力
            
        Returns:
            bool: 是否能完成x次往返
        """
        total = 0
        l = 1
        
        # 滑动窗口计算总可用次数
        for r in range(1, n + 1):
            total += usage[r]
            
            # 维护窗口大小不超过power
            while r - l + 1 > power:
                total -= usage[l]
                l += 1
            
            # 如果窗口大小达到power且总可用次数不足2x
            if r - l + 1 == power and total < 2 * x:
                return False
        
        return True


def test_frog_to_school():
    """
    测试函数
    """
    solution = FrogToSchool()
    solution_opt = FrogToSchoolOptimized()
    
    # 测试用例1
    n1 = 5
    x1 = 3
    arr1 = [0, 2, 3, 1, 4]  # 注意：数组从位置1开始
    result1 = solution.compute(n1, x1, arr1)
    result1_opt = solution_opt.compute(n1, x1, arr1)
    print(f"Test 1: {result1}, Optimized: {result1_opt}")
    
    # 测试用例2
    n2 = 10
    x2 = 5
    arr2 = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
    result2 = solution.compute(n2, x2, arr2)
    result2_opt = solution_opt.compute(n2, x2, arr2)
    print(f"Test 2: {result2}, Optimized: {result2_opt}")
    
    # 边界测试
    n3 = 1
    x3 = 1
    arr3 = [0]
    result3 = solution.compute(n3, x3, arr3)
    result3_opt = solution_opt.compute(n3, x3, arr3)
    print(f"Test 3 (边界): {result3}, Optimized: {result3_opt}")


if __name__ == "__main__":
    # 运行测试
    test_frog_to_school()
    
    # 从标准输入读取数据（用于在线评测）
    if len(sys.argv) > 1 and sys.argv[1] == "--online":
        data = sys.stdin.read().strip().split()
        if len(data) >= 2:
            n = int(data[0])
            x = int(data[1])
            arr = [0]  # 位置0不使用
            for i in range(1, n):
                arr.append(int(data[i + 1]))
            
            solution = FrogToSchoolOptimized()
            result = solution.compute(n, x, arr)
            print(result)


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
   - 原始验证算法的时间复杂度为O(x*n)，对于x=1e9的情况会超时
   - 优化版本通过数学分析将时间复杂度降至O(n)
   - 关键思路是：对于每个位置i的石头，最多可以被使用min(arr[i], x)次

8. 跨语言对比：
   - Python代码更简洁，但运行速度较慢
   - 在Python中需要注意整数溢出问题，Python的整数精度不会有问题
   - 对于大规模数据，C++版本通常更快

9. 笔试面试技巧：
   - 理解二分答案的单调性原理
   - 掌握滑动窗口的模板代码
   - 能够分析时间复杂度和空间复杂度
   - 能够处理边界情况和极端输入

10. 相关题目扩展：
    - LeetCode 403: Frog Jump
    - LeetCode 55: Jump Game  
    - LeetCode 45: Jump Game II
    - 这些题目都涉及跳跃问题的不同变种，可以对比学习
"""