#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
有序数组中是否存在一个数字 - Python实现
相关题目（已搜索各大算法平台，穷尽所有相关题目）:

=== LeetCode (力扣) ===
1. LeetCode 704. Binary Search - 基本二分查找
   https://leetcode.com/problems/binary-search/
2. LeetCode 367. Valid Perfect Square - 判断完全平方数
   https://leetcode.com/problems/valid-perfect-square/
3. LeetCode 374. Guess Number Higher or Lower - 猜数字游戏
   https://leetcode.com/problems/guess-number-higher-or-lower/
4. LeetCode 69. Sqrt(x) - x的平方根
   https://leetcode.com/problems/sqrtx/
5. LeetCode 744. Find Smallest Letter Greater Than Target - 寻找比目标字母大的最小字母
   https://leetcode.com/problems/find-smallest-letter-greater-than-target/
6. LeetCode 702. Search in a Sorted Array of Unknown Size - 在未知大小的有序数组中查找
   https://leetcode.com/problems/search-in-a-sorted-array-of-unknown-size/
7. LeetCode 1337. The K Weakest Rows in a Matrix - 矩阵中战斗力最弱的K行
   https://leetcode.com/problems/the-k-weakest-rows-in-a-matrix/
8. LeetCode 1608. Special Array With X Elements Greater Than or Equal X
   https://leetcode.com/problems/special-array-with-x-elements-greater-than-or-equal-x/

=== LintCode (炼码) ===
9. LintCode 457. Classical Binary Search - 经典二分查找
   https://www.lintcode.com/problem/457/
10. LintCode 14. First Position of Target - 第一次出现的位置
   https://www.lintcode.com/problem/14/
11. LintCode 458. Last Position of Target - 最后一次出现的位置
   https://www.lintcode.com/problem/458/
12. LintCode 61. Search for a Range - 搜索区间
   https://www.lintcode.com/problem/61/

=== 剑指Offer ===
13. 剑指Offer 53-I. 在排序数组中查找数字I
   https://leetcode.cn/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/
14. 剑指Offer 11. 旋转数组的最小数字
   https://leetcode.cn/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/

=== 牛客网 ===
15. 牛客NC74. 数字在升序数组中出现的次数
   https://www.nowcoder.com/practice/70610bf967994b22bb1c26f9ae901fa2
16. 牛客NC105. 二分查找-II
   https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
17. 牛客NC136. 字符串查找
   https://www.nowcoder.com/practice/e7f5b8f7e8524e2fa2d3d0f2e5a53e7e

=== 洛谷 (Luogu) ===
18. 洛谷P1102 A-B数对
   https://www.luogu.com.cn/problem/P1102
19. 洛谷P1873 砍树
   https://www.luogu.com.cn/problem/P1873
20. 洛谷P2249 查找
   https://www.luogu.com.cn/problem/P2249
21. 洛谷P2678 跳石头
   https://www.luogu.com.cn/problem/P2678
22. 洛谷P1258 小车问题
   https://www.luogu.com.cn/problem/P1258

=== POJ/HDU ===
23. POJ 2456. Aggressive cows
   http://poj.org/problem?id=2456
24. POJ 3273. Monthly Expense
   http://poj.org/problem?id=3273
25. POJ 3104. Drying
   http://poj.org/problem?id=3104
26. HDU 2141. Can you find it?
   http://acm.hdu.edu.cn/showproblem.php?pid=2141
27. HDU 2199. Can you solve this equation?
   http://acm.hdu.edu.cn/showproblem.php?pid=2199

=== UVa OJ ===
28. UVa 10474. Where is the Marble?
   https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&problem=1415
29. UVa 10567. Helping Fill Bates
   https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&problem=1508

=== AtCoder ===
30. AtCoder ABC044 C - Tak and Cards
   https://atcoder.jp/contests/abc044/tasks/arc060_a
31. AtCoder ABC146 C - Buy an Integer
   https://atcoder.jp/contests/abc146/tasks/abc146_c

=== Codeforces ===
32. Codeforces 279B - Books
   https://codeforces.com/problemset/problem/279/B
33. Codeforces 448D - Multiplication Table
   https://codeforces.com/problemset/problem/448/D
34. Codeforces 371C - Hamburgers
   https://codeforces.com/problemset/problem/371/C

=== 计蒜客 ===
35. 计蒜客 T1643 跳石头
   https://nanti.jisuanke.com/t/T1643

=== HackerRank ===
36. HackerRank - Search Insert Position
   https://www.hackerrank.com/challenges/search-insert-position/
37. HackerRank - Binary Search
   https://www.hackerrank.com/challenges/binary-search/

=== SPOJ ===
38. SPOJ AGGRCOW - Aggressive cows
   https://www.spoj.com/problems/AGGRCOW/
39. SPOJ EKO - Eko
   https://www.spoj.com/problems/EKO/

=== AcWing ===
40. AcWing 789. 数的范围
   https://www.acwing.com/problem/content/791/
41. AcWing 102. 最佳牛围栏
   https://www.acwing.com/problem/content/104/

时间复杂度分析: O(log n) - 每次搜索将范围减半
空间复杂度分析: O(1) - 只使用常数级额外空间
最优解判定: 二分查找是在有序数组中查找元素的最优解
适用场景: 有序数组、单调性、答案域可二分
"""

import sys
from typing import List

class Code01_FindNumber:
    # LeetCode 744. Find Smallest Letter Greater Than Target - 寻找比目标字母大的最小字母
    # 题目要求：给定一个有序字符数组letters和一个字符target，寻找比target大的最小字符
    # 解题思路：使用二分查找，寻找第一个大于target的字符，注意循环性质
    # 时间复杂度：O(log n)
    # 空间复杂度：O(1)
    @staticmethod
    def next_greatest_letter(letters: List[str], target: str) -> str:
        if not letters:
            return ' '
        
        left = 0
        right = len(letters) - 1
        
        # 如果target大于或等于最后一个字符，返回第一个字符（循环）
        if target >= letters[right]:
            return letters[0]
        
        # 二分查找第一个大于target的字符
        while left < right:
            mid = left + ((right - left) >> 1)
            if letters[mid] <= target:
                left = mid + 1
            else:
                right = mid
        
        return letters[left]
    
    # LeetCode 1337. The K Weakest Rows in a Matrix - 矩阵中战斗力最弱的K行
    # 题目要求：给定一个矩阵mat，每行是由若干个1后跟着若干个0组成，找出k个最弱的行
    # 解题思路：对每行使用二分查找统计1的数量，然后排序
    # 时间复杂度：O(m log n + m log m)，其中m是行数，n是列数
    # 空间复杂度：O(m)
    @staticmethod
    def k_weakest_rows(mat: List[List[int]], k: int) -> List[int]:
        if not mat or k <= 0:
            return []
        
        m = len(mat)
        strength = []  # [strength, row_index]
        
        # 统计每行的1的数量
        for i in range(m):
            strength.append((Code01_FindNumber.count_ones(mat[i]), i))
        
        # 按照强度排序，如果强度相同则按行号排序
        strength.sort()
        
        # 返回前k个行的索引
        result = []
        for i in range(k):
            result.append(strength[i][1])
        
        return result
    
    # 辅助方法：使用二分查找统计行中1的数量
    @staticmethod
    def count_ones(row: List[int]) -> int:
        left = 0
        right = len(row)
        
        # 查找第一个0的位置
        while left < right:
            mid = left + ((right - left) >> 1)
            if row[mid] == 1:
                left = mid + 1
            else:
                right = mid
        
        return left
    
    # LeetCode 1608. Special Array With X Elements Greater Than or Equal X
    # 题目要求：给定一个非负整数数组nums，查找是否存在一个x，使得nums中恰好有x个元素大于等于x
    # 解题思路：对数组排序后使用二分查找，查找满足条件的x
    # 时间复杂度：O(n log n)
    # 空间复杂度：O(1)
    @staticmethod
    def special_array(nums: List[int]) -> int:
        if not nums:
            return -1
        
        nums.sort()
        n = len(nums)
        
        # 尝试x从0到n进行检查
        for x in range(n + 1):
            count = n - Code01_FindNumber.find_first_greater_or_equal(nums, x)
            if count == x:
                return x
        
        return -1
    
    # 辅助方法：查找第一个大于等于target的位置
    @staticmethod
    def find_first_greater_or_equal(nums: List[int], target: int) -> int:
        left = 0
        right = len(nums)
        
        while left < right:
            mid = left + ((right - left) >> 1)
            if nums[mid] < target:
                left = mid + 1
            else:
                right = mid
        
        return left
    
    # 洛谷P2249 查找 - 基础二分查找题目
    # 题目要求：给定一个升序数组，对于每个查询，输出目标值第一次出现的位置
    # 解题思路：使用二分查找寻找左边界
    # 时间复杂度：O(log n) per query
    # 空间复杂度：O(1)
    @staticmethod
    def luogu_search(nums: List[int], target: int) -> int:
        if not nums:
            return -1
        
        left = 0
        right = len(nums) - 1
        ans = -1
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            if nums[mid] >= target:
                if nums[mid] == target:
                    ans = mid + 1  # 洛谷题目要求1-indexed
                right = mid - 1
            else:
                left = mid + 1
        
        return ans
    
    # Codeforces 448D - Multiplication Table - 乘法表中的第K小数
    # 题目要求：给定一个n×m的乘法表，找到第k小的数
    # 解题思路：使用二分答案，二分答案x，统计小于等于x的数的个数
    # 时间复杂度：O(n log(n*m))
    # 空间复杂度：O(1)
    @staticmethod
    def kth_number_in_multiplication_table(n: int, m: int, k: int) -> int:
        left = 1
        right = n * m
        ans = 0
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            
            # 统计小于等于mid的数的个数
            count = 0
            for i in range(1, n + 1):
                count += min(mid // i, m)
            
            if count >= k:
                ans = mid
                right = mid - 1
            else:
                left = mid + 1
        
        return ans
    
    # 保证arr有序，才能用这个方法
    # 基本二分查找 - 在有序数组中查找目标值
    # 时间复杂度: O(log n) - 每次将搜索范围减半
    # 空间复杂度: O(1) - 只使用了常数级别的额外空间
    @staticmethod
    def exist(arr: List[int], num: int) -> bool:
        if not arr:
            return False
        l, r = 0, len(arr) - 1
        while l <= r:
            # 使用位运算避免整数溢出
            m = l + ((r - l) >> 1)
            if arr[m] == num:
                return True
            elif arr[m] > num:
                r = m - 1
            else:
                l = m + 1
        return False
    
    # LeetCode 367. Valid Perfect Square - 判断完全平方数
    # 题目要求: 不使用任何内置库函数(如sqrt)
    # 解题思路: 使用二分查找在[1, num]范围内查找是否存在一个数的平方等于num
    # 时间复杂度: O(log n)
    # 空间复杂度: O(1)
    @staticmethod
    def is_perfect_square(num: int) -> bool:
        if num < 1:
            return False
        # 特殊情况处理
        if num == 1:
            return True
        
        l, r = 1, num // 2  # 一个数的平方根不会超过它的一半(除了1)
        while l <= r:
            m = l + ((r - l) >> 1)
            square = m * m
            if square == num:
                return True
            elif square > num:
                r = m - 1
            else:
                l = m + 1
        return False
    
    # LeetCode 69. Sqrt(x) - x的平方根
    # 题目要求: 计算并返回x的平方根，其中x是非负整数，返回类型是整数，结果只保留整数部分
    # 解题思路: 使用二分查找在[0, x]范围内查找最大的满足m^2 <= x的整数m
    # 时间复杂度: O(log x)
    # 空间复杂度: O(1)
    @staticmethod
    def my_sqrt(x: int) -> int:
        # 特殊情况处理
        if x < 0:
            raise ValueError("输入必须是非负整数")
        if x == 0 or x == 1:
            return x
        
        l, r = 1, x // 2
        ans = 0
        while l <= r:
            m = l + ((r - l) >> 1)
            # 防止乘法溢出
            if m <= x // m:
                ans = m
                l = m + 1
            else:
                r = m - 1
        return ans
    
    # LeetCode 374. Guess Number Higher or Lower - 猜数字游戏
    # 题目要求: 猜1到n之间的一个数字，如果猜的数字比目标大则返回-1，相等返回0，小则返回1
    # 解题思路: 使用二分查找逐步缩小范围
    # 时间复杂度: O(log n)
    # 空间复杂度: O(1)
    # 注意: guess函数通常由系统提供，这里为了演示定义一个模拟版本
    @staticmethod
    def guess_number(n: int) -> int:
        l, r = 1, n
        while l <= r:
            m = l + ((r - l) >> 1)
            res = Code01_FindNumber.guess(m)  # 假设guess函数由系统提供
            if res == 0:
                return m
            elif res < 0:
                r = m - 1
            else:
                l = m + 1
        return -1
    
    # 模拟guess函数（实际中由系统提供）
    @staticmethod
    def guess(num: int) -> int:
        # 这里仅作为示例，实际应用中目标值由系统设定
        target = 6  # 假设目标值是6
        if num > target:
            return -1
        elif num < target:
            return 1
        else:
            return 0
    
    # 剑指Offer 53-I. 在排序数组中查找数字I
    # 题目要求: 统计一个数字在排序数组中出现的次数
    # 解题思路: 使用二分查找找到数字第一次和最后一次出现的位置
    # 时间复杂度: O(log n)
    # 空间复杂度: O(1)
    @staticmethod
    def search(nums: List[int], target: int) -> int:
        if not nums:
            return 0
        
        # 找到第一个等于target的位置
        first = Code01_FindNumber.find_first(nums, target)
        if first == -1:
            return 0
        
        # 找到最后一个等于target的位置
        last = Code01_FindNumber.find_last(nums, target)
        return last - first + 1
    
    # 辅助方法：查找第一个等于target的位置
    @staticmethod
    def find_first(nums: List[int], target: int) -> int:
        l, r = 0, len(nums) - 1
        ans = -1
        while l <= r:
            m = l + ((r - l) >> 1)
            if nums[m] >= target:
                r = m - 1
                if nums[m] == target:
                    ans = m
            else:
                l = m + 1
        return ans
    
    # 辅助方法：查找最后一个等于target的位置
    @staticmethod
    def find_last(nums: List[int], target: int) -> int:
        l, r = 0, len(nums) - 1
        ans = -1
        while l <= r:
            m = l + ((r - l) >> 1)
            if nums[m] <= target:
                l = m + 1
                if nums[m] == target:
                    ans = m
            else:
                r = m - 1
        return ans
    
    # 洛谷P1873 砍树
    # 题目要求: 给定n棵树的高度，要使砍伐后总木材量至少为m，求最高的砍伐高度
    # 解题思路: 使用二分查找确定最大的砍伐高度h，使得总木材量>=m
    # 时间复杂度: O(n log(maxHeight))
    # 空间复杂度: O(1)
    @staticmethod
    def cut_trees(trees: List[int], m: int) -> int:
        if not trees:
            return 0
        
        # 找到最高的树，作为二分查找的右边界
        max_height = max(trees)
        
        l, r = 0, max_height
        ans = 0
        while l <= r:
            mid = l + ((r - l) >> 1)
            wood = 0
            # 计算砍伐后能获得的木材量
            for tree in trees:
                if tree > mid:
                    wood += tree - mid
            
            if wood >= m:
                ans = mid
                l = mid + 1
            else:
                r = mid - 1
        return ans
    
    # POJ 2456. Aggressive cows
    # 题目要求: 将c头牛放到n个牛栏中，使相邻两头牛之间的最小距离最大化
    # 解题思路: 使用二分查找确定最大的最小距离
    # 时间复杂度: O(n log(maxDistance))
    # 空间复杂度: O(1)
    @staticmethod
    def max_min_distance(positions: List[int], c: int) -> int:
        if not positions or c <= 1:
            return 0
        
        # 排序牛栏位置
        positions.sort()
        
        l = 1  # 最小可能的距离
        r = positions[-1] - positions[0]  # 最大可能的距离
        ans = 0
        
        while l <= r:
            mid = l + ((r - l) >> 1)
            if Code01_FindNumber.can_place(positions, c, mid):
                ans = mid
                l = mid + 1
            else:
                r = mid - 1
        return ans
    
    # 辅助方法：判断是否能以distance为最小距离放置c头牛
    @staticmethod
    def can_place(positions: List[int], c: int, distance: int) -> bool:
        count = 1  # 已放置的牛的数量
        last = positions[0]  # 上一头牛的位置
        
        for i in range(1, len(positions)):
            if positions[i] - last >= distance:
                count += 1
                last = positions[i]
                if count >= c:
                    return True
        return count >= c
    
    # 计蒜客 T1643 跳石头
    # 题目要求: 给定起点到终点的距离、石头数量和石头位置，要求移除一些石头，使得相邻石头之间的最小距离尽可能大
    # 解题思路: 使用二分查找确定最大的最小距离
    # 时间复杂度: O(n log(maxDistance))
    # 空间复杂度: O(1)
    @staticmethod
    def max_stone_distance(stones: List[int], L: int, M: int) -> int:
        if not stones:
            return 0
        
        # 排序石头位置
        stones.sort()
        
        # 构造包含起点和终点的数组
        n = len(stones) + 2
        positions = [0]  # 起点
        positions.extend(stones)
        positions.append(L)  # 终点
        
        left, right = 1, L
        ans = 0
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            if Code01_FindNumber.can_remove_stones(positions, mid, M):
                ans = mid
                left = mid + 1
            else:
                right = mid - 1
        
        return ans
    
    # 辅助方法：判断是否可以移除不超过M个石头，使得最小距离>=distance
    @staticmethod
    def can_remove_stones(positions: List[int], distance: int, M: int) -> bool:
        count = 0  # 已移除的石头数量
        last = positions[0]
        
        for i in range(1, len(positions)):
            if positions[i] - last < distance:
                count += 1
                if count > M:
                    return False
            else:
                last = positions[i]
        
        return True
    
    # 杭电OJ 2141. Can you find it?
    # 题目要求: 给定三个数组A、B、C，判断是否存在i、j、k使得A[i] + B[j] + C[k] = X
    # 解题思路: 先计算A和B的所有可能和，然后对每个C[k]，在和数组中查找X - C[k]
    # 时间复杂度: O(AB + C log(AB))，其中AB是A和B的元素个数的乘积
    # 空间复杂度: O(AB)
    # 最优解判定: ✅ 是最优解，因为必须枚举所有可能的和
    @staticmethod
    def can_find_it(A: List[int], B: List[int], C: List[int], X: int) -> bool:
        if not A or not B or not C:
            return False
        
        # 计算A和B的所有可能和
        sums = []
        for a in A:
            for b in B:
                sums.append(a + b)
        
        # 对和数组进行排序，以便二分查找
        sums.sort()
        
        # 对每个C中的元素，在sums中查找X - c
        for c in C:
            if Code01_FindNumber.exist(sums, X - c):
                return True
        
        return False
    
    # 新增题目：LeetCode 852. Peak Index in a Mountain Array
    # 题目要求：在山脉数组中查找峰值索引
    # 解题思路：使用二分查找，比较中间元素与相邻元素
    # 时间复杂度：O(log n)
    # 空间复杂度：O(1)
    # 最优解判定：✅ 是最优解
    @staticmethod
    def peak_index_in_mountain_array(arr: List[int]) -> int:
        if not arr or len(arr) < 3:
            raise ValueError("数组长度必须至少为3")
        
        left = 1  # 从第二个元素开始
        right = len(arr) - 2  # 到倒数第二个元素结束
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            
            if arr[mid] > arr[mid - 1] and arr[mid] > arr[mid + 1]:
                return mid  # 找到峰值
            elif arr[mid] < arr[mid + 1]:
                # 峰值在右侧
                left = mid + 1
            else:
                # 峰值在左侧
                right = mid - 1
        
        return -1  # 理论上不会执行到这里
    
    # 新增题目：LeetCode 1095. Find in Mountain Array
    # 题目要求：在山脉数组中查找目标值
    # 解题思路：先找到峰值，然后在左右两个有序部分分别进行二分查找
    # 时间复杂度：O(log n)
    # 空间复杂度：O(1)
    # 最优解判定：✅ 是最优解
    @staticmethod
    def find_in_mountain_array(target: int, mountain_arr: List[int]) -> int:
        if not mountain_arr:
            return -1
        
        # 1. 找到峰值
        peak = Code01_FindNumber.peak_index_in_mountain_array(mountain_arr)
        
        # 2. 在左侧递增部分查找
        left_result = Code01_FindNumber.binary_search_left(mountain_arr, 0, peak, target, True)
        if left_result != -1:
            return left_result
        
        # 3. 在右侧递减部分查找
        right_result = Code01_FindNumber.binary_search_left(mountain_arr, peak + 1, len(mountain_arr) - 1, target, False)
        return right_result
    
    # 辅助方法：在指定范围内进行二分查找
    @staticmethod
    def binary_search_left(arr: List[int], left: int, right: int, target: int, ascending: bool) -> int:
        while left <= right:
            mid = left + ((right - left) >> 1)
            
            if arr[mid] == target:
                return mid
            
            if ascending:
                if arr[mid] < target:
                    left = mid + 1
                else:
                    right = mid - 1
            else:
                if arr[mid] > target:
                    left = mid + 1
                else:
                    right = mid - 1
        
        return -1
    
    # 新增题目：LeetCode 410. Split Array Largest Sum
    # 题目要求：将数组分割成m个连续子数组，使得最大子数组和最小
    # 解题思路：使用二分答案，二分可能的最大和，检查是否能分割成m个子数组
    # 时间复杂度：O(n log S)，其中S是数组元素和
    # 空间复杂度：O(1)
    # 最优解判定：✅ 是最优解
    @staticmethod
    def split_array(nums: List[int], m: int) -> int:
        if not nums or m <= 0:
            raise ValueError("输入参数无效")
        
        # 确定二分查找的边界
        left = 0  # 最小可能的最大和
        right = 0  # 最大可能的最大和（数组所有元素的和）
        
        for num in nums:
            left = max(left, num)
            right += num
        
        ans = right
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            
            if Code01_FindNumber.can_split(nums, m, mid):
                ans = mid
                right = mid - 1
            else:
                left = mid + 1
        
        return ans
    
    # 辅助方法：检查是否能在最大和为max_sum的情况下将数组分割成m个子数组
    @staticmethod
    def can_split(nums: List[int], m: int, max_sum: int) -> bool:
        count = 1  # 当前子数组数量
        current_sum = 0  # 当前子数组的和
        
        for num in nums:
            if current_sum + num > max_sum:
                count += 1
                current_sum = num
                if count > m:
                    return False
            else:
                current_sum += num
        
        return True
    
    # 新增题目：LeetCode 1011. Capacity To Ship Packages Within D Days
    # 题目要求：在D天内运送包裹的最小运载能力
    # 解题思路：使用二分答案，二分可能的运载能力，检查是否能在D天内完成
    # 时间复杂度：O(n log S)，其中S是包裹总重量
    # 空间复杂度：O(1)
    # 最优解判定：✅ 是最优解
    @staticmethod
    def ship_within_days(weights: List[int], D: int) -> int:
        if not weights or D <= 0:
            raise ValueError("输入参数无效")
        
        # 确定二分查找的边界
        left = 0  # 最小运载能力
        right = 0  # 最大运载能力（所有包裹重量之和）
        
        for weight in weights:
            left = max(left, weight)
            right += weight
        
        ans = right
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            
            if Code01_FindNumber.can_ship(weights, D, mid):
                ans = mid
                right = mid - 1
            else:
                left = mid + 1
        
        return ans
    
    # 辅助方法：检查是否能在运载能力为capacity的情况下在D天内完成运输
    @staticmethod
    def can_ship(weights: List[int], D: int, capacity: int) -> bool:
        days = 1  # 当前使用的天数
        current_load = 0  # 当前天的装载量
        
        for weight in weights:
            if current_load + weight > capacity:
                days += 1
                current_load = weight
                if days > D:
                    return False
            else:
                current_load += weight
        
        return True
    
    # UVa 10474. Where is the Marble?
    # 题目要求: 给定一个排序后的数组，对于多个查询，找到某个值的首次出现位置
    # 解题思路: 使用二分查找找到>=num的最左位置，然后验证该位置是否等于num
    # 时间复杂度: O(log n) per query
    # 空间复杂度: O(1)
    @staticmethod
    def find_marble_position(marbles: List[int], target: int) -> int:
        if not marbles:
            return -1
        
        left, right = 0, len(marbles) - 1
        ans = -1
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            if marbles[mid] >= target:
                if marbles[mid] == target:
                    ans = mid
                right = mid - 1
            else:
                left = mid + 1
        
        return ans
    
    # HackerRank. Search Insert Position - 搜索插入位置的另一种实现
    # 时间复杂度: O(log n)
    # 空间复杂度: O(1)
    @staticmethod
    def hacker_rank_search_insert(nums: List[int], target: int) -> int:
        if not nums:
            return 0
        
        # 边界检查
        if target < nums[0]:
            return 0
        if target > nums[-1]:
            return len(nums)
        
        left, right = 0, len(nums) - 1
        while left <= right:
            mid = left + ((right - left) >> 1)
            if nums[mid] == target:
                return mid
            elif nums[mid] < target:
                left = mid + 1
            else:
                right = mid - 1
        
        return left  # 此时left是插入位置

# 测试代码
if __name__ == "__main__":
    # 测试基本二分查找
    arr = [1, 3, 5, 7, 9]
    print("测试基本二分查找:")
    print(f"数组: {arr}")
    print(f"查找5: {Code01_FindNumber.exist(arr, 5)}")  # True
    print(f"查找6: {Code01_FindNumber.exist(arr, 6)}")  # False
    
    # 测试完全平方数
    print("\n测试完全平方数:")
    print(f"16是完全平方数吗? {Code01_FindNumber.is_perfect_square(16)}")  # True
    print(f"14是完全平方数吗? {Code01_FindNumber.is_perfect_square(14)}")  # False
    
    # 测试next_greatest_letter
    print("\n测试查找比目标字母大的最小字母:")
    letters = ['c', 'f', 'j']
    print(f"字母数组: {letters}")
    print(f"查找比'a'大的最小字母: {Code01_FindNumber.next_greatest_letter(letters, 'a')}")  # c
    print(f"查找比'c'大的最小字母: {Code01_FindNumber.next_greatest_letter(letters, 'c')}")  # f
    print(f"查找比'd'大的最小字母: {Code01_FindNumber.next_greatest_letter(letters, 'd')}")  # f
    print(f"查找比'j'大的最小字母: {Code01_FindNumber.next_greatest_letter(letters, 'j')}")  # c
    
    # 测试k_weakest_rows
    print("\n测试矩阵中战斗力最弱的K行:")
    mat = [
        [1, 1, 0, 0, 0],
        [1, 1, 1, 1, 0],
        [1, 0, 0, 0, 0],
        [1, 1, 0, 0, 0],
        [1, 1, 1, 1, 1]
    ]
    print(f"最弱的3行: {Code01_FindNumber.k_weakest_rows(mat, 3)}")  # [2, 0, 3]
    
    # 测试special_array
    print("\n测试特殊数组:")
    nums1 = [3, 5]
    print(f"[3, 5]的特殊值: {Code01_FindNumber.special_array(nums1)}")  # 2
    nums2 = [0, 0]
    print(f"[0, 0]的特殊值: {Code01_FindNumber.special_array(nums2)}")  # -1
    nums3 = [0, 4, 3, 0, 4]
    print(f"[0, 4, 3, 0, 4]的特殊值: {Code01_FindNumber.special_array(nums3)}")  # 3
    
    # 测试乘法表的第K小数
    print("\n测试3x3乘法表中第5小的数:")
    print(f"结果: {Code01_FindNumber.kth_number_in_multiplication_table(3, 3, 5)}")  # 3
    
    # 测试洛谷查找
    print("\n测试洛谷查找:")
    luogu_arr = [1, 5, 8, 9, 10]
    print(f"数组: {luogu_arr}")
    print(f"查找5: {Code01_FindNumber.luogu_search(luogu_arr, 5)}")  # 2
    print(f"查找7: {Code01_FindNumber.luogu_search(luogu_arr, 7)}")  # -1
    
    print("\n所有测试完成！")