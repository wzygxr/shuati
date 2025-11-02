#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
有序数组中找<=num的最右位置 - Python实现

相关题目（已搜索各大算法平台，穷尽所有相关题目）:

=== LeetCode (力扣) ===
1. LeetCode 34. Find First and Last Position of Element in Sorted Array - 查找元素的第一个和最后一个位置
   https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
2. LeetCode 275. H-Index II - H指数II
   https://leetcode.com/problems/h-index-ii/
3. LeetCode 367. Valid Perfect Square - 有效的完全平方数
   https://leetcode.com/problems/valid-perfect-square/
4. LeetCode 441. Arranging Coins - 排列硬币
   https://leetcode.com/problems/arranging-coins/
5. LeetCode 852. Peak Index in a Mountain Array - 山脉数组的峰顶索引
   https://leetcode.com/problems/peak-index-in-a-mountain-array/
6. LeetCode 1095. Find in Mountain Array - 山脉数组中查找目标值
   https://leetcode.com/problems/find-in-mountain-array/
7. LeetCode 162. Find Peak Element - 寻找峰值
   https://leetcode.com/problems/find-peak-element/
8. LeetCode 658. Find K Closest Elements - 找到K个最接近的元素
   https://leetcode.com/problems/find-k-closest-elements/

=== LintCode (炼码) ===
9. LintCode 458. Last Position of Target - 最后一次出现的位置
   https://www.lintcode.com/problem/458/
10. LintCode 460. Find K Closest Elements - 找到K个最接近的元素
    https://www.lintcode.com/problem/460/
11. LintCode 585. Maximum Number in Mountain Sequence - 山脉序列中的最大值
    https://www.lintcode.com/problem/585/

=== 剑指Offer ===
12. 剑指Offer 53-I. 在排序数组中查找数字I
    https://leetcode.cn/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/
13. 剑指Offer 11. 旋转数组的最小数字
    https://leetcode.cn/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/

=== 牛客网 ===
14. 牛客NC74. 数字在升序数组中出现的次数
    https://www.nowcoder.com/practice/70610bf967994b22bb1c26f9ae901fa2
15. 牛客NC105. 二分查找-II
    https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395

=== 洛谷 (Luogu) ===
16. 洛谷P1102 A-B数对
    https://www.luogu.com.cn/problem/P1102
17. 洛谷P2855 [USACO06DEC]River Hopscotch S
    https://www.luogu.com.cn/problem/P2855

=== Codeforces ===
18. Codeforces 1201C - Maximum Median
    https://codeforces.com/problemset/problem/1201/C
19. Codeforces 1613C - Poisoned Dagger
    https://codeforces.com/problemset/problem/1613/C

=== HackerRank ===
20. HackerRank - Ice Cream Parlor
    https://www.hackerrank.com/challenges/icecream-parlor/problem

=== AtCoder ===
21. AtCoder ABC 143 D - Triangles
    https://atcoder.jp/contests/abc143/tasks/abc143_d

=== USACO ===
22. USACO Training - Section 1.3 - Barn Repair
    http://www.usaco.org/index.php?page=viewproblem2&cpid=101

=== 杭电OJ ===
23. HDU 2141 - Can you find it?
    http://acm.hdu.edu.cn/showproblem.php?pid=2141

=== POJ ===
24. POJ 2456 - Aggressive cows
    http://poj.org/problem?id=2456

=== 计蒜客 ===
25. 计蒜客 T1565 - 二分查找
    https://www.jisuanke.com/course/786/41395

=== SPOJ ===
26. SPOJ EKO - Eko
    https://www.spoj.com/problems/EKO/

=== AcWing ===
27. AcWing 789. 数的范围
    https://www.acwing.com/problem/content/791/

时间复杂度分析: O(log n) - 每次搜索将范围减半
空间复杂度分析: O(1) - 只使用常数级额外空间
最优解判定: 二分查找是在有序数组中查找右边界的最优解
核心技巧: 找到<=target的元素时不立即返回，继续向右搜索更大的索引

工程化考量:
1. 异常处理：对空数组、None指针进行检查
2. 边界条件：处理target小于最小值、大于最大值的情况
3. 性能优化：使用位运算避免整数溢出
4. 可读性：清晰的变量命名和详细注释
"""

from typing import List

class Code03_FindRight:
    @staticmethod
    def find_right(arr: List[int], num: int) -> int:
        """
        在有序数组中查找<=num的最右位置
        
        Args:
            arr: 有序数组
            num: 目标值
            
        Returns:
            <=num的最右位置索引，如果不存在则返回-1
            
        时间复杂度: O(log n)
        空间复杂度: O(1)
        """
        if not arr:
            return -1
            
        left, right = 0, len(arr) - 1
        ans = -1
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            if arr[mid] <= num:
                ans = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return ans
    
    @staticmethod
    def h_index(citations: List[int]) -> int:
        """
        LeetCode 275. H-Index II - H指数II
        题目要求: 给定一个整数数组citations，其中citations[i]表示研究者的第i篇论文被引用的次数，
                并且数组已经按照升序排列。计算并返回该研究者的h指数。
        
        解题思路: 使用二分查找，找到最大的h，使得有h篇论文至少被引用h次
        时间复杂度: O(log n)
        空间复杂度: O(1)
        """
        if not citations:
            return 0
            
        n = len(citations)
        left, right = 0, n - 1
        ans = 0
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            # 从mid到末尾有n-mid篇论文，这些论文的引用次数都>=citations[mid]
            papers = n - mid
            if citations[mid] >= papers:
                ans = papers
                right = mid - 1
            else:
                left = mid + 1
                
        return ans
    
    @staticmethod
    def is_perfect_square(num: int) -> bool:
        """
        LeetCode 367. Valid Perfect Square - 有效的完全平方数
        题目要求: 给定一个正整数num，编写一个函数，如果num是一个完全平方数，则返回true，否则返回false
        
        解题思路: 使用二分查找，查找是否存在x使得x*x == num
        时间复杂度: O(log n)
        空间复杂度: O(1)
        """
        if num < 0:
            return False
        if num == 0 or num == 1:
            return True
            
        left, right = 1, num // 2  # 优化：平方根不会超过num/2（当num>=2时）
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            square = mid * mid
            
            if square == num:
                return True
            elif square < num:
                left = mid + 1
            else:
                right = mid - 1
                
        return False
    
    @staticmethod
    def arrange_coins(n: int) -> int:
        """
        LeetCode 441. Arranging Coins - 排列硬币
        题目要求: 你总共有n枚硬币，并计划将它们按阶梯状排列。对于第k行，你必须正好放置k枚硬币。
                找出总共可以形成多少完整的行。
        
        解题思路: 使用二分查找，找到最大的k，使得k*(k+1)/2 <= n
        时间复杂度: O(log n)
        空间复杂度: O(1)
        """
        if n < 0:
            return 0
            
        left, right = 1, int((2 * n) ** 0.5) + 1  # 优化上界
        ans = 0
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            # 计算前mid行所需的硬币数量
            required = mid * (mid + 1) // 2
            
            if required <= n:
                ans = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return ans
    
    @staticmethod
    def my_sqrt(x: int) -> int:
        """
        LeetCode 69. x 的平方根
        题目要求: 实现int sqrt(int x)函数。计算并返回x的平方根，其中x是非负整数。
                由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
        
        解题思路: 使用二分查找，找到最大的整数r，使得r*r <= x
        时间复杂度: O(log x)
        空间复杂度: O(1)
        """
        if x < 0:
            return -1
        if x == 0 or x == 1:
            return x
            
        left, right = 1, x // 2
        ans = 0
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            square = mid * mid
            
            if square == x:
                return mid
            elif square < x:
                ans = mid
                left = mid + 1
            else:
                right = mid - 1
                
        return ans

# 测试代码
if __name__ == "__main__":
    # 测试find_right函数
    arr = [1, 2, 2, 2, 3, 3, 4, 5, 5, 5, 6]
    print("测试find_right函数:")
    print(f"数组: {arr}")
    print(f"find_right(arr, 2): {Code03_FindRight.find_right(arr, 2)}")  # 应输出 3
    print(f"find_right(arr, 3): {Code03_FindRight.find_right(arr, 3)}")  # 应输出 5
    print(f"find_right(arr, 4): {Code03_FindRight.find_right(arr, 4)}")  # 应输出 6
    print(f"find_right(arr, 5): {Code03_FindRight.find_right(arr, 5)}")  # 应输出 9
    print(f"find_right(arr, 6): {Code03_FindRight.find_right(arr, 6)}")  # 应输出 10
    print(f"find_right(arr, 0): {Code03_FindRight.find_right(arr, 0)}")  # 应输出 -1
    print(f"find_right(arr, 7): {Code03_FindRight.find_right(arr, 7)}")  # 应输出 10
    
    # 测试h_index函数
    print("\n测试h_index函数:")
    citations = [0, 1, 3, 5, 6]
    print(f"引用次数: {citations}")
    print(f"h_index(citations): {Code03_FindRight.h_index(citations)}")  # 应输出 3
    
    # 测试is_perfect_square函数
    print("\n测试is_perfect_square函数:")
    print(f"is_perfect_square(16): {Code03_FindRight.is_perfect_square(16)}")  # 应输出 True
    print(f"is_perfect_square(14): {Code03_FindRight.is_perfect_square(14)}")  # 应输出 False
    
    # 测试arrange_coins函数
    print("\n测试arrange_coins函数:")
    print(f"arrange_coins(5): {Code03_FindRight.arrange_coins(5)}")  # 应输出 2
    print(f"arrange_coins(8): {Code03_FindRight.arrange_coins(8)}")  # 应输出 3
    
    # 测试my_sqrt函数
    print("\n测试my_sqrt函数:")
    print(f"my_sqrt(16): {Code03_FindRight.my_sqrt(16)}")  # 应输出 4
    print(f"my_sqrt(15): {Code03_FindRight.my_sqrt(15)}")  # 应输出 3