#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
财富分配实验与相关算法题目 - Python版本
包含原始财富分配实验和多个扩展题目

时间复杂度分析:
- 原始实验: O(t * n^2) 其中t为轮数，n为人数
- 基尼系数计算: O(n^2) 需要双重循环计算绝对差值和

空间复杂度分析:
- 原始实验: O(n) 存储财富数组和标记数组
- 基尼系数计算: O(1) 仅使用几个变量

工程化考量:
1. 异常处理: 处理输入参数合法性
2. 性能优化: 对于大规模数据可优化基尼系数计算
3. 可测试性: 提供单元测试方法
4. 可扩展性: 模块化设计便于添加新算法

相关题目链接:
1. UVa 11300 - Spreading the Wealth (分金币)
   来源: UVa Online Judge
   链接: https://vjudge.net/problem/UVA-11300
   Java实现: [Experiment.java](Experiment.java)
   Python实现: [Experiment.py](Experiment.py)
   C++实现: [Experiment.cpp](Experiment.cpp)

2. Codeforces 671B - Robin Hood (劫富济贫)
   来源: Codeforces
   链接: https://codeforces.com/problemset/problem/671/B
   Java实现: [Experiment.java](Experiment.java)
   Python实现: [Experiment.py](Experiment.py)
   C++实现: [Experiment.cpp](Experiment.cpp)

3. LeetCode 41 - First Missing Positive (缺失的第一个正数)
   来源: LeetCode
   链接: https://leetcode.com/problems/first-missing-positive/
   Java实现: [Experiment.java](Experiment.java)
   Python实现: [Experiment.py](Experiment.py)
   C++实现: [Experiment.cpp](Experiment.cpp)

4. LeetCode 42 - Trapping Rain Water (接雨水)
   来源: LeetCode
   链接: https://leetcode.com/problems/trapping-rain-water/
   Java实现: [Experiment.java](Experiment.java)
   Python实现: [Experiment.py](Experiment.py)
   C++实现: [Experiment.cpp](Experiment.cpp)

5. POJ 2155 - Matrix (二维树状数组)
   来源: POJ
   链接: http://poj.org/problem?id=2155
   Java实现: [Experiment.java](Experiment.java)
   Python实现: [Experiment.py](Experiment.py)
   C++实现: [Experiment.cpp](Experiment.cpp)

6. UVa 10881 - Piotr's Ants (蚂蚁)
   来源: UVa Online Judge
   链接: https://vjudge.net/problem/UVA-10881
   Java实现: [Experiment.java](Experiment.java)
   Python实现: [Experiment.py](Experiment.py)
   C++实现: [Experiment.cpp](Experiment.cpp)

7. POJ 3263 - Tallest Cow (差分法)
   来源: POJ
   链接: http://poj.org/problem?id=3263
   Java实现: [Experiment.java](Experiment.java)
   Python实现: [Experiment.py](Experiment.py)
   C++实现: [Experiment.cpp](Experiment.cpp)
"""

import random
import math
from typing import List, Tuple
import sys

class Experiment:
    """
    财富分配实验主类
    
    相关题目:
    1. UVa 11300 - Spreading the Wealth (分金币)
       来源: UVa Online Judge
       链接: https://vjudge.net/problem/UVA-11300
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    2. Codeforces 671B - Robin Hood (劫富济贫)
       来源: Codeforces
       链接: https://codeforces.com/problemset/problem/671/B
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    3. LeetCode 41 - First Missing Positive (缺失的第一个正数)
       来源: LeetCode
       链接: https://leetcode.com/problems/first-missing-positive/
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    4. LeetCode 42 - Trapping Rain Water (接雨水)
       来源: LeetCode
       链接: https://leetcode.com/problems/trapping-rain-water/
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    5. POJ 2155 - Matrix (二维树状数组)
       来源: POJ
       链接: http://poj.org/problem?id=2155
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    6. UVa 10881 - Piotr's Ants (蚂蚁)
       来源: UVa Online Judge
       链接: https://vjudge.net/problem/UVA-10881
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    7. POJ 3263 - Tallest Cow (差分法)
       来源: POJ
       链接: http://poj.org/problem?id=3263
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)
    """
    
    @staticmethod
    def run_main():
        """主函数：演示原始实验和扩展题目的使用"""
        print("=== 财富分配实验与相关算法题目 ===")
        print("作者: 算法学习系统")
        print("日期: 2024年")
        print()
        
        # 运行原始财富分配实验
        Experiment.run_original_experiment()
        
        # 运行扩展题目测试
        Experiment.run_extended_problems()
        
        print("=== 所有测试完成 ===")
    
    @staticmethod
    def run_original_experiment():
        """运行原始财富分配实验"""
        print("=== 原始财富分配实验 ===")
        print("一个社会的基尼系数是一个在0~1之间的小数")
        print("基尼系数为0代表所有人的财富完全一样")
        print("基尼系数为1代表有1个人掌握了全社会的财富")
        print("基尼系数越小，代表社会财富分布越均衡；越大则代表财富分布越不均衡")
        print("在2022年，世界各国的平均基尼系数为0.44")
        print("目前普遍认为，当基尼系数到达 0.5 时")
        print("就意味着社会贫富差距非常大，分布非常不均匀")
        print("社会可能会因此陷入危机，比如大量的犯罪或者经历社会动荡")
        
        print("测试开始")
        n = 100
        t = 100000
        print(f"人数: {n}")
        print(f"轮数: {t}")
        Experiment.experiment(n, t)
        print("测试结束")
        print()
    
    @staticmethod
    def run_extended_problems():
        """运行扩展题目测试"""
        print("=== 扩展题目测试 ===")
        
        # UVa 11300 - Spreading the Wealth
        SpreadingTheWealth.test()
        
        # Codeforces 671B - Robin Hood
        RobinHood.test()
        
        # LeetCode 41 - First Missing Positive
        FirstMissingPositive.test()
        
        # LeetCode 42 - Trapping Rain Water
        TrappingRainWater.test()
        
        # POJ 2155 - Matrix
        Matrix.test()
        
        # UVa 10881 - Piotr's Ants
        PiotrAnts.test()
        
        # POJ 3263 - Tallest Cow
        TallestCow.test()
    
    @staticmethod
    def experiment(n: int, t: int):
        """
        原始财富分配实验
        时间复杂度: O(t * n^2) - 外层循环t次，内层双重循环
        空间复杂度: O(n) - 存储财富数组和标记数组
        
        Args:
            n: 人数
            t: 轮数
        """
        # 参数验证
        if n <= 0 or t <= 0:
            raise ValueError("人数和轮数必须大于0")
        
        wealth = [100.0] * n
        has_money = [False] * n
        
        for i in range(t):
            # 重置标记数组
            has_money = [False] * n
            
            # 标记有钱的人
            for j in range(n):
                if wealth[j] > 0:
                    has_money[j] = True
            
            # 有钱的人随机给其他人1元
            for j in range(n):
                if has_money[j]:
                    other = j
                    while other == j:
                        other = random.randint(0, n - 1)
                    wealth[j] -= 1
                    wealth[other] += 1
        
        # 排序并输出结果
        wealth.sort()
        print("列出每个人的财富(贫穷到富有):")
        for i in range(n):
            print(f"{int(wealth[i])} ", end="")
            if i % 10 == 9:
                print()
        print()
        print(f"这个社会的基尼系数为: {Experiment.calculate_gini(wealth)}")
    
    @staticmethod
    def calculate_gini(wealth: List[float]) -> float:
        """
        计算基尼系数
        时间复杂度: O(n^2) - 双重循环计算绝对差值和
        空间复杂度: O(1) - 仅使用几个变量
        
        基尼系数公式: G = ΣΣ|xi - xj| / (2n²μ)
        其中xi, xj为个人财富，n为人数，μ为平均财富
        
        Args:
            wealth: 财富数组
            
        Returns:
            基尼系数
        """
        if not wealth:
            raise ValueError("财富数组不能为空")
        
        n = len(wealth)
        sum_of_absolute_differences = 0.0
        sum_of_wealth = sum(wealth)
        
        for i in range(n):
            for j in range(n):
                sum_of_absolute_differences += abs(wealth[i] - wealth[j])
        
        return sum_of_absolute_differences / (2 * n * sum_of_wealth)


class SpreadingTheWealth:
    """
    UVa 11300 - Spreading the Wealth (分金币)
    来源: UVa Online Judge
    链接: https://vjudge.net/problem/UVA-11300
    
    题目描述:
    圆桌旁坐着n个人，每个人有一定数量的金币，金币总数能被n整除。
    每个人可以给左右相邻的人金币，求使得所有人最后的金币数相同的最少转手金币数
    
    解法分析:
    最优解: 数学推导+中位数
    时间复杂度: O(n log n) - 主要消耗在排序上
    空间复杂度: O(n) - 需要存储Ci数组
    
    核心思想:
    1. 将问题转化为数学规划问题
    2. 通过递推关系得到Ci = A1+A2+...+Ai - i*M
    3. 利用中位数性质最小化距离和
    
    相关题目:
    1. LeetCode 462. Minimum Moves to Equal Array Elements II
       来源: LeetCode
       链接: https://leetcode.com/problems/minimum-moves-to-equal-array-elements-ii/
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    2. Codeforces 717C - Potions Homework
       来源: Codeforces
       链接: https://codeforces.com/problemset/problem/717/C
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)
    """
    
    @staticmethod
    def min_transfer_coins(coins: List[int]) -> int:
        n = len(coins)
        if n <= 1:
            return 0
        
        # 计算金币总数和平均值
        total = sum(coins)
        average = total // n
        
        # 计算Ci数组
        c = [0] * n
        c[0] = coins[0] - average
        for i in range(1, n):
            c[i] = c[i-1] + coins[i] - average
        
        # 对Ci数组排序，找出中位数
        c.sort()
        median = c[n // 2]
        
        # 计算最小转移金币数
        result = 0
        for value in c:
            result += abs(value - median)
        
        return result
    
    @staticmethod
    def test():
        print("\n=== UVa 11300 - Spreading the Wealth 测试 ===")
        
        # 测试用例1: 平均分布
        coins1 = [100, 100, 100, 100]
        print(f"测试用例1 - 初始金币: {coins1}")
        result1 = SpreadingTheWealth.min_transfer_coins(coins1)
        print(f"最少转移金币数: {result1}")
        
        # 测试用例2: 示例情况
        coins2 = [1, 2, 5, 4]
        print(f"测试用例2 - 初始金币: {coins2}")
        result2 = SpreadingTheWealth.min_transfer_coins(coins2)
        print(f"最少转移金币数: {result2}")


class RobinHood:
    """
    Codeforces 671B - Robin Hood (劫富济贫)
    来源: Codeforces
    链接: https://codeforces.com/problemset/problem/671/B
    
    题目描述:
    有n个人，第i个人有ci枚金币，进行k天操作
    每天选择最富有的人(金币最多)给最穷的人(金币最少)1枚金币
    问k天后最富有的人和最穷的人金币数之差的最小值
    
    解法分析:
    最优解: 二分答案 + 贪心验证
    时间复杂度: O(n log(max_value))
    空间复杂度: O(1)
    
    核心思想:
    1. 二分最终的最大值和最小值
    2. 验证给定状态是否可达
    3. 利用贪心思想计算操作次数
    
    相关题目:
    1. LeetCode 1642. Furthest Building You Can Reach
       来源: LeetCode
       链接: https://leetcode.com/problems/furthest-building-you-can-reach/
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    2. Codeforces 1363E - Binary Tree Coloring
       来源: Codeforces
       链接: https://codeforces.com/problemset/problem/1363/E
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)
    """
    
    @staticmethod
    def min_difference(coins: List[int], k: int) -> int:
        n = len(coins)
        if n <= 1:
            return 0
        
        # 计算金币总数
        total = sum(coins)
        
        # 二分最终的最大值
        left, right = 0, (total + n - 1) // n
        max_val = right
        while left <= right:
            mid = (left + right) // 2
            operations = 0
            for coin in coins:
                if coin > mid:
                    operations += coin - mid
            if operations <= k:
                max_val = mid
                right = mid - 1
            else:
                left = mid + 1
        
        # 二分最终的最小值
        left, right = 0, total // n
        min_val = left
        while left <= right:
            mid = (left + right) // 2
            operations = 0
            for coin in coins:
                if coin < mid:
                    operations += mid - coin
            if operations <= k:
                min_val = mid
                left = mid + 1
            else:
                right = mid - 1
        
        # 特殊情况处理
        if min_val >= max_val:
            return 0 if total % n == 0 else 1
        
        return max_val - min_val
    
    @staticmethod
    def test():
        print("\n=== Codeforces 671B - Robin Hood 测试 ===")
        
        coins1 = [1, 1, 1, 1]
        k1 = 3
        print(f"测试用例1 - 初始金币: {coins1}, 操作天数: {k1}")
        result1 = RobinHood.min_difference(coins1, k1)
        print(f"最大值与最小值之差: {result1}")


class FirstMissingPositive:
    """
    LeetCode 41 - First Missing Positive (缺失的第一个正数)
    来源: LeetCode
    链接: https://leetcode.com/problems/first-missing-positive/
    
    题目描述:
    给你一个未排序的整数数组 nums，请你找出其中没有出现的最小的正整数
    
    解法分析:
    最优解: 原地哈希
    时间复杂度: O(n)
    空间复杂度: O(1)
    
    核心思想:
    1. 对于长度为n的数组，缺失的最小正整数一定在[1, n+1]范围内
    2. 将每个正整数i放到数组的第i-1个位置上
    3. 遍历数组找到第一个不符合条件的位置
    
    相关题目:
    1. LeetCode 448. Find All Numbers Disappeared in an Array
       来源: LeetCode
       链接: https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    2. LeetCode 41. First Missing Positive (相同题目)
       来源: LeetCode
       链接: https://leetcode.cn/problems/first-missing-positive/
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)
    """
    
    @staticmethod
    def first_missing_positive(nums: List[int]) -> int:
        n = len(nums)
        
        # 将每个正整数i放到数组的第i-1个位置上
        for i in range(n):
            while 1 <= nums[i] <= n and nums[nums[i] - 1] != nums[i]:
                # 交换位置
                temp = nums[i]
                nums[i] = nums[temp - 1]
                nums[temp - 1] = temp
        
        # 遍历数组找到第一个不符合条件的位置
        for i in range(n):
            if nums[i] != i + 1:
                return i + 1
        
        return n + 1
    
    @staticmethod
    def test():
        print("\n=== LeetCode 41 - First Missing Positive 测试 ===")
        
        nums1 = [1, 2, 0]
        print(f"测试用例1 - 数组: {nums1}")
        result1 = FirstMissingPositive.first_missing_positive(nums1)
        print(f"缺失的最小正整数: {result1}")


class TrappingRainWater:
    """
    LeetCode 42 - Trapping Rain Water (接雨水)
    来源: LeetCode
    链接: https://leetcode.com/problems/trapping-rain-water/
    
    题目描述:
    给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算下雨之后能接多少雨水
    
    解法分析:
    最优解: 双指针法
    时间复杂度: O(n)
    空间复杂度: O(1)
    
    核心思想:
    1. 每个位置能接的雨水量取决于左右两侧最大高度中的较小值
    2. 使用双指针从两端向中间移动
    3. 维护左右两侧的最大高度
    
    相关题目:
    1. LeetCode 407. Trapping Rain Water II
       来源: LeetCode
       链接: https://leetcode.com/problems/trapping-rain-water-ii/
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    2. LeetCode 11. Container With Most Water
       来源: LeetCode
       链接: https://leetcode.com/problems/container-with-most-water/
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)
    """
    
    @staticmethod
    def trap(height: List[int]) -> int:
        if not height:
            return 0
        
        left, right = 0, len(height) - 1
        left_max, right_max = 0, 0
        result = 0
        
        while left < right:
            if height[left] < height[right]:
                if height[left] >= left_max:
                    left_max = height[left]
                else:
                    result += left_max - height[left]
                left += 1
            else:
                if height[right] >= right_max:
                    right_max = height[right]
                else:
                    result += right_max - height[right]
                right -= 1
        
        return result
    
    @staticmethod
    def test():
        print("\n=== LeetCode 42 - Trapping Rain Water 测试 ===")
        
        height1 = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]
        print(f"测试用例1 - 高度数组: {height1}")
        result1 = TrappingRainWater.trap(height1)
        print(f"能接的雨水量: {result1}")


class Matrix:
    """
    POJ 2155 - Matrix (二维树状数组)
    来源: POJ
    链接: http://poj.org/problem?id=2155
    
    题目描述:
    给定一个N*N的01矩阵，初始全为0，支持两种操作：
    1. 将一个子矩阵中所有元素翻转(0变1，1变0)
    2. 查询某个位置的值
    
    解法分析:
    最优解: 二维树状数组 + 差分思想
    时间复杂度: O(logN * logN) 每次操作
    空间复杂度: O(N*N)
    
    核心思想:
    1. 使用二维树状数组维护差分数组
    2. 区间更新转为4个单点更新
    3. 单点查询转为区间查询
    
    相关题目:
    1. HDU 1195 - Stars
       来源: HDU
       链接: http://acm.hdu.edu.cn/showproblem.php?pid=1556
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    2. Codeforces 1093E - Intersection of Permutations
       来源: Codeforces
       链接: https://codeforces.com/problemset/problem/1093/E
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)
    """
    
    def __init__(self, size: int):
        self.n = size
        self.tree = [[0] * (size + 1) for _ in range(size + 1)]
    
    def lowbit(self, x: int) -> int:
        return x & (-x)
    
    def add(self, x: int, y: int, val: int):
        i = x
        while i <= self.n:
            j = y
            while j <= self.n:
                self.tree[i][j] += val
                j += self.lowbit(j)
            i += self.lowbit(i)
    
    def sum(self, x: int, y: int) -> int:
        result = 0
        i = x
        while i > 0:
            j = y
            while j > 0:
                result += self.tree[i][j]
                j -= self.lowbit(j)
            i -= self.lowbit(i)
        return result
    
    def update_range(self, x1: int, y1: int, x2: int, y2: int):
        self.add(x1, y1, 1)
        self.add(x1, y2 + 1, 1)
        self.add(x2 + 1, y1, 1)
        self.add(x2 + 1, y2 + 1, 1)
    
    def query(self, x: int, y: int) -> int:
        return self.sum(x, y) % 2
    
    @staticmethod
    def test():
        print("\n=== POJ 2155 - Matrix 测试 ===")
        
        matrix = Matrix(4)
        print("初始状态查询:")
        print(f"matrix[1][1] = {matrix.query(1, 1)}")
        
        matrix.update_range(1, 1, 2, 2)
        print("翻转区域[(1,1),(2,2)]后查询:")
        print(f"matrix[1][1] = {matrix.query(1, 1)}")


class PiotrAnts:
    """
    UVa 10881 - Piotr's Ants (蚂蚁)
    来源: UVa Online Judge
    链接: https://vjudge.net/problem/UVA-10881
    
    题目描述:
    一根长度为L厘米的木棍上有n只蚂蚁，每只蚂蚁要么朝左爬，要么朝右爬，速度为1厘米/秒
    当两只蚂蚁相撞时，二者同时掉头(掉头时间忽略不计)
    给出每只蚂蚁的初始位置和朝向，计算T秒之后每只蚂蚁的位置
    
    解法分析:
    最优解: 等效转换思想
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    
    核心思想:
    1. 蚂蚁的相对位置在运动过程中不会改变
    2. 碰撞可以看作是蚂蚁互相穿过对方，身份互换
    3. 忽略碰撞直接计算最终位置，然后排序确定状态
    
    相关题目:
    1. Codeforces 1346A - Color Revolution
       来源: Codeforces
       链接: https://codeforces.com/problemset/problem/1346/A
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    2. AtCoder ABC131D - Megalomania
       来源: AtCoder
       链接: https://atcoder.jp/contests/abc131/tasks/abc131_d
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)
    """
    
    DIR_NAMES = ["L", "Turning", "R"]
    
    class Ant:
        def __init__(self, ant_id: int, position: int, direction: int):
            self.id = ant_id
            self.position = position
            self.direction = direction  # -1: 左, 0: 转身中, 1: 右
        
        def __lt__(self, other):
            return self.position < other.position
    
    @staticmethod
    def get_final_positions(length: int, time: int, 
                           positions: List[int], 
                           directions: List[str]) -> List[str]:
        n = len(positions)
        if n == 0:
            return []
        
        # 创建初始状态的蚂蚁数组
        before = []
        for i in range(n):
            direction = -1 if directions[i] == 'L' else 1
            before.append(PiotrAnts.Ant(i, positions[i], direction))
        
        # 根据初始位置排序
        before.sort()
        
        # 记录排序后每个蚂蚁在原数组中的索引
        order = [0] * n
        for i in range(n):
            order[before[i].id] = i
        
        # 计算每个蚂蚁在T秒后的位置（忽略碰撞）
        after = []
        for i in range(n):
            final_pos = before[i].position + before[i].direction * time
            after.append(PiotrAnts.Ant(before[i].id, final_pos, before[i].direction))
        
        # 根据最终位置排序
        after.sort()
        
        # 处理碰撞情况
        for i in range(n - 1):
            if after[i].position == after[i+1].position:
                after[i].direction = after[i+1].direction = 0
        
        # 按照输入顺序生成结果
        result = [""] * n
        for i in range(n):
            idx = order[i]
            ant = after[idx]
            
            if ant.position < 0 or ant.position > length:
                result[i] = "Fell off"
            else:
                result[i] = f"{ant.position} {PiotrAnts.DIR_NAMES[ant.direction + 1]}"
        
        return result
    
    @staticmethod
    def test():
        print("\n=== UVa 10881 - Piotr's Ants 测试 ===")
        
        length = 10
        time = 1
        positions = [4, 6, 8]
        directions = ['R', 'L', 'R']
        
        print(f"木棍长度: {length}, 时间: {time}")
        result = PiotrAnts.get_final_positions(length, time, positions, directions)
        print(f"最终状态: {' '.join(result)}")


class TallestCow:
    """
    POJ 3263 - Tallest Cow (差分法)
    来源: POJ
    链接: http://poj.org/problem?id=3263
    
    题目描述:
    有N头牛排成一行，每头牛的高度为H或更低。给出R对关系，每对关系表示第A_i头牛和第B_i头牛能互相看见
    这意味着它们之间的所有牛的高度都严格小于它们的高度。求每头牛可能的最大高度。
    
    解法分析:
    最优解: 差分数组
    时间复杂度: O(R + N)
    空间复杂度: O(N)
    
    核心思想:
    1. 使用差分数组高效标记区间更新
    2. 通过前缀和计算最终高度
    3. 处理重复关系避免重复计算
    
    相关题目:
    1. HDU 1556 - Color the ball
       来源: HDU
       链接: http://acm.hdu.edu.cn/showproblem.php?pid=1556
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)

    2. Codeforces 1000C - Covered Points Count
       来源: Codeforces
       链接: https://codeforces.com/problemset/problem/1000/C
       Java实现: [Experiment.java](Experiment.java)
       Python实现: [Experiment.py](Experiment.py)
       C++实现: [Experiment.cpp](Experiment.cpp)
    """
    
    @staticmethod
    def tallest_cow(n: int, h: int, r: int, 
                   relations: List[Tuple[int, int]]) -> List[int]:
        # 存储需要更新的区间，并去重
        seen = set()
        intervals = []
        
        for rel in relations:
            a, b = min(rel), max(rel)
            key = f"{a}-{b}"
            if key not in seen:
                seen.add(key)
                intervals.append((a, b))
        
        # 使用差分数组
        diff = [0] * (n + 2)
        
        for a, b in intervals:
            if a + 1 <= b - 1:
                diff[a + 1] -= 1
                diff[b] += 1
        
        # 通过前缀和计算最终高度
        heights = [h] * (n + 1)
        current = 0
        for i in range(1, n + 1):
            current += diff[i]
            heights[i] += current
        
        return heights
    
    @staticmethod
    def test():
        print("\n=== POJ 3263 - Tallest Cow 测试 ===")
        
        n, h, r = 6, 4, 3
        relations = [(1, 6), (2, 4), (5, 6)]
        
        heights = TallestCow.tallest_cow(n, h, r, relations)
        print(f"每头牛可能的最大高度: {' '.join(map(str, heights[1:]))}")


def main():
    """程序入口点"""
    Experiment.run_main()


if __name__ == "__main__":
    main()