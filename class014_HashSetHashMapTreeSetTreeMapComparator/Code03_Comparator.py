#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Python版本：Comparator比较器相关题目与解析

在Python中，我们使用sorted()函数和key参数来实现自定义排序规则
Python Comparator特点：使用key函数或cmp_to_key转换比较函数实现自定义排序
时间复杂度：取决于具体实现，通常为O(logN)到O(NlogN)
空间复杂度：取决于具体实现

相关平台题目：
1. LeetCode 973. K Closest Points to Origin (最接近原点的 K 个点) - https://leetcode.com/problems/k-closest-points-to-origin/
2. LeetCode 179. Largest Number (最大数) - https://leetcode.com/problems/largest-number/
3. LeetCode 1356. Sort Integers by The Number of 1 Bits (根据数字二进制下 1 的数目排序) - https://leetcode.com/problems/sort-integers-by-the-number-of-1-bits/
4. LeetCode 56. Merge Intervals (合并区间) - https://leetcode.com/problems/merge-intervals/
5. LeetCode 1122. Relative Sort Array (数组的相对排序) - https://leetcode.com/problems/relative-sort-array/
6. LintCode 613. High Five (最高分五科) - https://www.lintcode.com/problem/high-five/
7. HackerEarth Monk and the Magical Candy Bags (和尚与魔法糖果袋) - https://www.hackerearth.com/practice/algorithms/greedy/basics-of-greedy-algorithms/practice-problems/algorithm/monk-and-the-magical-candy-bags/
8. CodeChef FRGTNLNG (遗忘的语言) - https://www.codechef.com/problems/FRGTNLNG
9. SPOJ DICT (字典) - https://www.spoj.com/problems/DICT/
10. Project Euler Problem 2: Even Fibonacci numbers (偶数斐波那契数) - https://projecteuler.net/problem=2
11. HackerRank Maximum Palindromes (最大回文) - https://www.hackerrank.com/challenges/maximum-palindromes
12. 计蒜客 T1101: 阶乘 (阶乘) - https://www.jisuanke.com/t/T1101
13. 杭电 OJ 1003: Max Sum (最大子序列和) - http://acm.hdu.edu.cn/showproblem.php?pid=1003
14. 牛客网 剑指Offer 50: 第一个只出现一次的字符 (第一个只出现一次的字符) - https://www.nowcoder.com/practice/1c82e8cf713b4bbeb2a5b31cf5b0417c
15. acwing 800. 数组元素的目标和 (数组元素的目标和) - https://www.acwing.com/problem/content/802/
16. POJ 1003: Hangover (悬垂) - http://poj.org/problem?id=1003
17. UVa OJ 101: The Blocks Problem (积木问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=37
18. Timus OJ 1005: Stone Pile (石子堆) - https://acm.timus.ru/problem.aspx?space=1&num=1005
19. Aizu OJ ALDS1_5_A: Exhaustive Search (穷举搜索) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_A
20. Comet OJ Contest #1: 热身赛 B. 简单的数学题 (简单的数学题) - https://cometoj.com/contest/1/problem/B
21. MarsCode 火星编程竞赛: 数字统计 (数字统计) - https://www.marscode.cn/contest/1/problem/1002
22. ZOJ 1002: Fire Net (消防网) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1002
23. LOJ 101: 最小生成树 (最小生成树) - https://loj.ac/p/101
24. 各大高校OJ: 北京大学OJ 1000: A+B Problem (A+B问题) - http://poj.org/problem?id=1000
25. LeetCode 352. Data Stream as Disjoint Intervals (数据流变为不相交区间) - https://leetcode.com/problems/data-stream-as-disjoint-intervals/
26. Codeforces 122A. Lucky Division (幸运分割) - https://codeforces.com/problemset/problem/122/A
27. AtCoder ABC 218 C - Shapes (形状) - https://atcoder.jp/contests/abc218/tasks/abc218_c
28. USACO Bronze: Block Game (积木游戏) - http://www.usaco.org/index.php?page=viewproblem2&cpid=664
29. 洛谷 P3366 【模板】最小生成树 (模板最小生成树) - https://www.luogu.com.cn/problem/P3366
30. LeetCode 149. Max Points on a Line (直线上最多的点数) - https://leetcode.com/problems/max-points-on-a-line/
31. LeetCode 215. Kth Largest Element in an Array (数组中的第K个最大元素) - https://leetcode.com/problems/kth-largest-element-in-an-array/
32. LeetCode 295. Find Median from Data Stream (数据流的中位数) - https://leetcode.com/problems/find-median-from-data-stream/
33. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
34. LeetCode 327. Count of Range Sum (区间和的个数) - https://leetcode.com/problems/count-of-range-sum/
35. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
36. LeetCode 148. Sort List (排序链表) - https://leetcode.com/problems/sort-list/
37. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
38. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
39. LeetCode 451. Sort Characters By Frequency (根据字符出现频率排序) - https://leetcode.com/problems/sort-characters-by-frequency/
40. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
41. LeetCode 539. Minimum Time Difference (最小时间差) - https://leetcode.com/problems/minimum-time-difference/
42. LeetCode 791. Custom Sort String (自定义字符串排序) - https://leetcode.com/problems/custom-sort-string/
43. LeetCode 937. Reorder Data in Log Files (重新排列日志文件) - https://leetcode.com/problems/reorder-data-in-log-files/
44. LeetCode 1030. Matrix Cells in Distance Order (距离顺序排列矩阵单元格) - https://leetcode.com/problems/matrix-cells-in-distance-order/
45. LeetCode 1636. Sort Array by Increasing Frequency (按照频率将数组升序排序) - https://leetcode.com/problems/sort-array-by-increasing-frequency/
46. LeetCode 1710. Maximum Units on a Truck (卡车上的最大单元数) - https://leetcode.com/problems/maximum-units-on-a-truck/
47. HackerRank Java Comparator (Java比较器) - https://www.hackerrank.com/challenges/java-comparator/problem
48. LeetCode 147. Insertion Sort List (对链表进行插入排序) - https://leetcode.com/problems/insertion-sort-list/
49. LeetCode 252. Meeting Rooms (会议室) - https://leetcode.com/problems/meeting-rooms/
50. LeetCode 253. Meeting Rooms II (会议室 II) - https://leetcode.com/problems/meeting-rooms-ii/
"""

from functools import cmp_to_key
import math

# LeetCode 973. K Closest Points to Origin (最接近原点的 K 个点)
def k_closest(points, k):
    """
    题目描述：
    给定一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，并且是一个整数 k ，
    返回离原点 (0,0) 最近的 k 个点。
    这里，平面上两点之间的距离是 欧几里德距离（ √(x1 - x2)^2 + (y1 - y2)^2 ）。
    你可以按 任何顺序 返回答案。除了点坐标的顺序之外，答案 确保 是 唯一 的。
    
    示例：
    输入：points = [[1,1],[3,3],[2,2]], k = 2
    输出：[[1,1],[2,2]]
    
    输入：points = [[3,3],[5,-1],[-2,4]], k = 2
    输出：[[3,3],[-2,4]]
    （答案 [[-2,4],[3,3]] 也会被接受。）
    
    约束条件：
    1 <= k <= points.length <= 10^4
    -10^4 < xi, yi < 10^4
    
    解题思路：
    计算每个点到原点的距离的平方（避免开方运算），然后使用自定义key函数按距离排序，
    最后取前k个点。
    
    时间复杂度：O(n log n)，其中n是点的数量
    空间复杂度：O(1)，如果不考虑输出数组的空间
    """
    # 使用自定义key函数按距离排序
    points.sort(key=lambda point: point[0]*point[0] + point[1]*point[1])
    
    # 返回前k个点
    return points[:k]


# LeetCode 179. Largest Number (最大数)
def largest_number(nums):
    """
    题目描述：
    给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
    注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。
    
    示例：
    输入：nums = [10,2]
    输出："210"
    
    输入：nums = [3,30,34,5,9]
    输出："9534330"
    
    约束条件：
    1 <= nums.length <= 100
    0 <= nums[i] <= 10^9
    
    解题思路：
    将整数数组转换为字符串数组，然后使用自定义比较函数排序。
    对于两个字符串a和b，比较a+b和b+a的大小，决定它们的顺序。
    
    时间复杂度：O(n log n * m)，其中n是数组长度，m是数字的平均长度
    空间复杂度：O(n * m)
    """
    # 转换为字符串数组
    strs = [str(num) for num in nums]
    
    # 自定义比较函数
    def compare(a, b):
        # 比较b+a和a+b的大小，注意是降序排列所以顺序颠倒
        if a + b > b + a:
            return -1
        elif a + b < b + a:
            return 1
        else:
            return 0
    
    # 使用自定义比较函数排序
    strs.sort(key=cmp_to_key(compare))
    
    # 处理全为0的特殊情况
    if strs[0] == "0":
        return "0"
    
    # 拼接结果
    return "".join(strs)


# LeetCode 1356. Sort Integers by The Number of 1 Bits (根据数字二进制下 1 的数目排序)
def sort_by_bits(arr):
    """
    题目描述：
    给你一个整数数组 arr 。请你将数组中的元素按照其二进制表示中数字 1 的数目升序排序。
    如果存在多个数字二进制中 1 的数目相同，则必须将它们按照数值大小升序排列。
    请你返回排序后的数组。
    
    示例：
    输入：arr = [0,1,2,3,4,5,6,7,8]
    输出：[0,1,2,4,8,3,5,6,7]
    
    输入：arr = [1024,512,256,128,64,32,16,8,4,2,1]
    输出：[1,2,4,8,16,32,64,128,256,512,1024]
    
    约束条件：
    1 <= arr.length <= 500
    0 <= arr[i] <= 10^4
    
    解题思路：
    使用自定义key函数排序，首先按二进制中1的位数排序，如果相同则按数值大小排序。
    计算二进制中1的位数可以使用bin().count('1')方法。
    
    时间复杂度：O(n log n)
    空间复杂度：O(1)
    """
    # 使用自定义key函数排序
    arr.sort(key=lambda x: (bin(x).count('1'), x))
    return arr


# HackerRank Java Comparator (Java比较器)的Python版本
class Player:
    def __init__(self, name, score):
        self.name = name
        self.score = score
    
    def __repr__(self):
        return f"{self.name} {self.score}"

def player_comparator(a, b):
    """
    题目描述：
    创建一个比较器，根据玩家的分数降序排列，如果分数相同则按名字升序排列。
    
    示例：
    输入：
    5
    amy 100
    david 100
    heraldo 50
    aakansha 75
    aleksa 150
    
    输出：
    aleksa 150
    amy 100
    david 100
    aakansha 75
    heraldo 50
    
    解题思路：
    实现一个比较函数，首先比较分数（降序），如果分数相同则比较名字（升序）。
    
    时间复杂度：O(n log n)，其中n是玩家数量
    空间复杂度：O(1)
    """
    # 首先按分数降序排列
    if a.score != b.score:
        return b.score - a.score
    # 如果分数相同，按名字升序排列
    if a.name < b.name:
        return -1
    elif a.name > b.name:
        return 1
    else:
        return 0


# LeetCode 56. Merge Intervals (合并区间)
def merge(intervals):
    """
    题目描述：
    以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
    请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
    
    示例：
    输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
    输出：[[1,6],[8,10],[15,18]]
    解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
    
    输入：intervals = [[1,4],[4,5]]
    输出：[[1,5]]
    解释：区间 [1,4] 和 [4,5] 可被视为重叠区间。
    
    约束条件：
    1 <= intervals.length <= 10^4
    intervals[i].length == 2
    0 <= starti <= endi <= 10^4
    
    解题思路：
    1. 首先按区间的起始位置排序
    2. 遍历排序后的区间，合并重叠的区间
    
    时间复杂度：O(n log n)，其中n是区间数量
    空间复杂度：O(n)，用于存储结果
    """
    if len(intervals) <= 1:
        return intervals

    # 按区间的起始位置排序
    intervals.sort(key=lambda x: x[0])

    result = []
    current = intervals[0]
    result.append(current)

    for i in range(1, len(intervals)):
        interval = intervals[i]
        # 如果当前区间与下一个区间重叠，合并它们
        if interval[0] <= current[1]:
            current[1] = max(current[1], interval[1])
        else:
            # 否则，将下一个区间添加到结果中
            current = interval
            result.append(current)

    return result


# LeetCode 1122. Relative Sort Array (数组的相对排序)
def relative_sort_array(arr1, arr2):
    """
    题目描述：
    给你两个数组，arr1 和 arr2，arr2 中的元素各不相同，arr2 中的每个元素都出现在 arr1 中。
    对 arr1 中的元素进行排序，使 arr1 中项的相对顺序和 arr2 中的相对顺序相同。
    未在 arr2 中出现过的元素需要按照升序放在 arr1 的末尾。
    
    示例：
    输入：arr1 = [2,3,1,3,2,4,6,7,9,2,19], arr2 = [2,1,4,3,9,6]
    输出：[2,2,2,1,4,3,3,9,6,7,19]
    
    约束条件：
    1 <= arr1.length, arr2.length <= 1000
    0 <= arr1[i], arr2[i] <= 1000
    arr2 中的元素 arr2[i] 各不相同
    arr2 中的每个元素 arr2[i] 都出现在 arr1 中
    
    解题思路：
    1. 使用自定义key函数排序
    2. 对于在arr2中出现的元素，按其在arr2中的索引排序
    3. 对于不在arr2中出现的元素，按数值大小升序排序，并放在末尾
    
    时间复杂度：O(m log m + n)，其中m是arr1的长度，n是arr2的长度
    空间复杂度：O(n)，用于存储arr2中元素的索引映射
    """
    # 创建arr2中元素到索引的映射
    index_map = {num: i for i, num in enumerate(arr2)}
    
    # 使用自定义key函数排序
    arr1.sort(key=lambda x: (0, index_map[x]) if x in index_map else (1, x))
    
    return arr1


# 测试代码
if __name__ == "__main__":
    # 测试最接近原点的 K 个点
    print("测试最接近原点的 K 个点:")
    points = [[1,1],[3,3],[2,2]]
    result = k_closest(points, 2)
    print(result)
    
    # 测试最大数
    print("测试最大数:")
    nums = [3,30,34,5,9]
    print(largest_number(nums))
    
    # 测试根据数字二进制下 1 的数目排序
    print("测试根据数字二进制下 1 的数目排序:")
    arr = [0,1,2,3,4,5,6,7,8]
    result = sort_by_bits(arr)
    print(result)
    
    # 测试HackerRank Java Comparator
    print("测试HackerRank Java Comparator:")
    players = [
        Player("amy", 100),
        Player("david", 100),
        Player("heraldo", 50),
        Player("aakansha", 75),
        Player("aleksa", 150)
    ]
    players.sort(key=cmp_to_key(player_comparator))
    for player in players:
        print(player)
    
    # 测试合并区间
    print("测试合并区间:")
    intervals = [[1,3],[2,6],[8,10],[15,18]]
    merged = merge(intervals)
    print(merged)
    
    # 测试数组的相对排序
    print("测试数组的相对排序:")
    arr1 = [2,3,1,3,2,4,6,7,9,2,19]
    arr2 = [2,1,4,3,9,6]
    result = relative_sort_array(arr1, arr2)
    print(result)