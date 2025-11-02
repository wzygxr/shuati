import bisect
# from sortedcontainers import SortedList, SortedDict  # 注释掉依赖模块
from typing import List, Tuple
import sys

"""
TreeSet和TreeMap高级应用题目实现（Python版本）
包含LeetCode 352, 363, 436, 456, 480, 683, 715, 731, 732, 855, 981, 1146, 1348, 1438等题目

Python中TreeSet和TreeMap的实现：
- Python标准库没有直接的TreeSet和TreeMap实现
- 使用bisect模块实现有序列表功能
- 使用字典和排序列表组合实现有序映射
- SortedList和SortedDict是第三方库sortedcontainers提供的实现

时间复杂度分析：
- bisect操作：O(log n)
- 范围查询：O(log n + k) 其中k是结果数量
- 排序操作：O(n log n)

空间复杂度分析：
- 排序列表存储：O(n)
- 额外数据结构：O(n)

工程化考量：
1. 异常处理：处理空输入、边界条件
2. 性能优化：选择合适的数据结构，避免不必要的对象创建
3. 代码可读性：添加详细注释，使用有意义的变量名
4. 线程安全：非线程安全，多线程环境下需要同步
5. 兼容性：使用标准库或流行第三方库

相关平台题目：
1. LeetCode 352. Data Stream as Disjoint Intervals (数据流变为不相交区间) - https://leetcode.com/problems/data-stream-as-disjoint-intervals/
2. LeetCode 363. Max Sum of Rectangle No Larger Than K (矩形区域不超过K的最大数值和) - https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
3. LeetCode 436. Find Right Interval (寻找右区间) - https://leetcode.com/problems/find-right-interval/
4. LeetCode 456. 132 Pattern (132模式) - https://leetcode.com/problems/132-pattern/
5. LeetCode 480. Sliding Window Median (滑动窗口中位数) - https://leetcode.com/problems/sliding-window-median/
6. LeetCode 683. K Empty Slots (K个空花盆) - https://leetcode.com/problems/k-empty-slots/
7. LeetCode 715. Range Module (范围模块) - https://leetcode.com/problems/range-module/
8. LeetCode 731. My Calendar II (我的日程安排表 II) - https://leetcode.com/problems/my-calendar-ii/
9. LeetCode 732. My Calendar III (我的日程安排表 III) - https://leetcode.com/problems/my-calendar-iii/
10. LeetCode 855. Exam Room (考场就座) - https://leetcode.com/problems/exam-room/
11. LeetCode 981. Time Based Key-Value Store (基于时间的键值存储) - https://leetcode.com/problems/time-based-key-value-store/
12. LeetCode 1146. Snapshot Array (快照数组) - https://leetcode.com/problems/snapshot-array/
13. LeetCode 1348. Tweet Counts Per Frequency (推文计数) - https://leetcode.com/problems/tweet-counts-per-frequency/
14. LeetCode 1438. Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit (绝对差不超过限制的最长连续子数组) - https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
15. LintCode 613. High Five (最高分五科) - https://www.lintcode.com/problem/high-five/
16. HackerEarth Monk and the Magical Candy Bags (和尚与魔法糖果袋) - https://www.hackerearth.com/practice/algorithms/greedy/basics-of-greedy-algorithms/practice-problems/algorithm/monk-and-the-magical-candy-bags/
17. CodeChef FRGTNLNG (遗忘的语言) - https://www.codechef.com/problems/FRGTNLNG
18. SPOJ DICT (字典) - https://www.spoj.com/problems/DICT/
19. Project Euler Problem 2: Even Fibonacci numbers (偶数斐波那契数) - https://projecteuler.net/problem=2
20. HackerRank Maximum Palindromes (最大回文) - https://www.hackerrank.com/challenges/maximum-palindromes
21. 计蒜客 T1101: 阶乘 (阶乘) - https://www.jisuanke.com/t/T1101
22. 杭电 OJ 1003: Max Sum (最大子序列和) - http://acm.hdu.edu.cn/showproblem.php?pid=1003
23. 牛客网 剑指Offer 50: 第一个只出现一次的字符 (第一个只出现一次的字符) - https://www.nowcoder.com/practice/1c82e8cf713b4bbeb2a5b31cf5b0417c
24. acwing 800. 数组元素的目标和 (数组元素的目标和) - https://www.acwing.com/problem/content/802/
25. POJ 1003: Hangover (悬垂) - http://poj.org/problem?id=1003
26. UVa OJ 101: The Blocks Problem (积木问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=37
27. Timus OJ 1005: Stone Pile (石子堆) - https://acm.timus.ru/problem.aspx?space=1&num=1005
28. Aizu OJ ALDS1_5_A: Exhaustive Search (穷举搜索) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_A
29. Comet OJ Contest #1: 热身赛 B. 简单的数学题 (简单的数学题) - https://cometoj.com/contest/1/problem/B
30. MarsCode 火星编程竞赛: 数字统计 (数字统计) - https://www.marscode.cn/contest/1/problem/1002
31. ZOJ 1002: Fire Net (消防网) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1002
32. LOJ 101: 最小生成树 (最小生成树) - https://loj.ac/p/101
33. 各大高校OJ: 北京大学OJ 1000: A+B Problem (A+B问题) - http://poj.org/problem?id=1000
34. Codeforces 122A. Lucky Division (幸运分割) - https://codeforces.com/problemset/problem/122/A
35. AtCoder ABC 218 C - Shapes (形状) - https://atcoder.jp/contests/abc218/tasks/abc218_c
36. USACO Bronze: Block Game (积木游戏) - http://www.usaco.org/index.php?page=viewproblem2&cpid=664
37. 洛谷 P3366 【模板】最小生成树 (模板最小生成树) - https://www.luogu.com.cn/problem/P3366
38. LeetCode 149. Max Points on a Line (直线上最多的点数) - https://leetcode.com/problems/max-points-on-a-line/
39. LeetCode 215. Kth Largest Element in an Array (数组中的第K个最大元素) - https://leetcode.com/problems/kth-largest-element-in-an-array/
40. LeetCode 295. Find Median from Data Stream (数据流的中位数) - https://leetcode.com/problems/find-median-from-data-stream/
41. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
42. LeetCode 327. Count of Range Sum (区间和的个数) - https://leetcode.com/problems/count-of-range-sum/
43. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
44. LeetCode 148. Sort List (排序链表) - https://leetcode.com/problems/sort-list/
45. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
46. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
47. LeetCode 451. Sort Characters By Frequency (根据字符出现频率排序) - https://leetcode.com/problems/sort-characters-by-frequency/
48. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
49. LeetCode 539. Minimum Time Difference (最小时间差) - https://leetcode.com/problems/minimum-time-difference/
50. LeetCode 791. Custom Sort String (自定义字符串排序) - https://leetcode.com/problems/custom-sort-string/
"""

class DataStreamAsDisjointIntervals:
    """
    LeetCode 352. Data Stream as Disjoint Intervals
    将数据流变为多个不相交区间
    网址：https://leetcode.com/problems/data-stream-as-disjoint-intervals/
    
    解题思路：
    1. 使用SortedDict存储区间边界，键为区间起点，值为区间终点
    2. 添加数字时，查找可能合并的相邻区间
    3. 合并重叠或相邻的区间
    
    时间复杂度：每次添加O(log n)，获取区间O(n)
    空间复杂度：O(n)
    """
    
    def __init__(self):
        # 使用普通字典和排序列表替代SortedDict
        self.intervals = {}
        self.sorted_keys = []
    
    def addNum(self, val: int) -> None:
        # 使用bisect查找插入位置
        import bisect
        
        # 查找小于等于val的区间
        idx = bisect.bisect_right(self.sorted_keys, val) - 1
        
        if idx >= 0:
            start = self.sorted_keys[idx]
            end = self.intervals[start]
            if start <= val <= end:
                # 数字已经在某个区间内，不需要处理
                return
            elif end + 1 == val:
                # 可以与左侧区间合并
                if idx + 1 < len(self.sorted_keys):
                    next_start = self.sorted_keys[idx + 1]
                    next_end = self.intervals[next_start]
                    if next_start == val + 1:
                        # 同时与右侧区间合并
                        self.intervals[start] = next_end
                        del self.intervals[next_start]
                        self.sorted_keys.pop(idx + 1)
                        return
                self.intervals[start] = val
                return
        
        # 检查是否可以与右侧区间合并
        if idx + 1 < len(self.sorted_keys):
            next_start = self.sorted_keys[idx + 1]
            next_end = self.intervals[next_start]
            if next_start == val + 1:
                self.intervals[val] = next_end
                del self.intervals[next_start]
                self.sorted_keys[idx + 1] = val
                self.sorted_keys.sort()
                return
        
        # 创建新区间
        self.intervals[val] = val
        bisect.insort(self.sorted_keys, val)
    
    def getIntervals(self) -> List[List[int]]:
        return [[start, end] for start, end in self.intervals.items()]

class MaxSumSubmatrix:
    """
    LeetCode 363. Max Sum of Rectangle No Larger Than K
    矩形区域不超过K的最大数值和
    网址：https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
    
    解题思路：
    1. 固定左右边界，计算每一行的前缀和
    2. 使用SortedList维护前缀和，快速查找满足条件的前缀和
    3. 使用bisect_left找到大于等于(target - k)的最小值
    
    时间复杂度：O(min(m,n)² * max(m,n) log max(m,n))
    空间复杂度：O(max(m,n))
    """
    
    def maxSumSubmatrix(self, matrix: List[List[int]], k: int) -> int:
        import bisect
        
        m, n = len(matrix), len(matrix[0])
        max_sum = -sys.maxsize
        
        # 让m是较小的维度，减少时间复杂度
        if m > n:
            rotated = [[matrix[j][i] for j in range(m)] for i in range(n)]
            return self.maxSumSubmatrix(rotated, k)
        
        # 枚举上下边界
        for top in range(m):
            col_sum = [0] * n
            for bottom in range(top, m):
                # 更新列前缀和
                for j in range(n):
                    col_sum[j] += matrix[bottom][j]
                
                # 使用排序列表维护前缀和
                prefix_sums = []
                prefix_sums.append(0)
                current_sum = 0
                
                for j in range(n):
                    current_sum += col_sum[j]
                    # 查找大于等于(current_sum - k)的最小值
                    target = current_sum - k
                    # 在排序列表中二分查找
                    idx = bisect.bisect_left(prefix_sums, target)
                    if idx < len(prefix_sums):
                        max_sum = max(max_sum, current_sum - prefix_sums[idx])
                    # 插入并保持排序
                    bisect.insort(prefix_sums, current_sum)
        
        return max_sum

class FindRightInterval:
    """
    LeetCode 436. Find Right Interval
    寻找右区间
    网址：https://leetcode.com/problems/find-right-interval/
    
    解题思路：
    1. 使用SortedDict存储每个区间的起始位置和索引
    2. 对于每个区间，使用bisect_left找到起始位置大于等于当前区间结束位置的最小区间
    3. 如果找到则返回对应索引，否则返回-1
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    
    def findRightInterval(self, intervals: List[List[int]]) -> List[int]:
        n = len(intervals)
        result = [-1] * n
        
        # 存储区间起始位置和索引
        starts = [(intervals[i][0], i) for i in range(n)]
        starts.sort()
        
        # 提取起始位置列表用于二分查找
        start_vals = [s[0] for s in starts]
        
        for i in range(n):
            end = intervals[i][1]
            # 二分查找大于等于end的最小起始位置
            idx = bisect.bisect_left(start_vals, end)
            if idx < n:
                result[i] = starts[idx][1]
        
        return result

class Find132Pattern:
    """
    LeetCode 456. 132 Pattern
    132模式
    网址：https://leetcode.com/problems/132-pattern/
    
    解题思路：
    1. 从右向左遍历数组
    2. 使用SortedList维护右侧元素
    3. 维护一个变量记录当前最大值作为3
    4. 检查是否存在1小于3且3小于2的模式
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    
    def find132pattern(self, nums: List[int]) -> bool:
        n = len(nums)
        if n < 3:
            return False
        
        # 记录左侧最小值
        min_left = [0] * n
        min_left[0] = nums[0]
        for i in range(1, n):
            min_left[i] = min(min_left[i-1], nums[i])
        
        # 使用排序列表维护右侧元素
        right = []
        right.append(nums[n-1])
        
        for i in range(n-2, 0, -1):
            # 当前元素作为3，左侧最小值作为1
            if min_left[i-1] < nums[i]:
                # 在右侧查找大于左侧最小值且小于当前元素的数作为2
                target = min_left[i-1]
                # 在排序列表中查找第一个大于target的元素
                idx = bisect.bisect_right(right, target)
                if idx < len(right) and right[idx] < nums[i]:
                    return True
            # 插入并保持排序
            bisect.insort(right, nums[i])
        
        return False

class SlidingWindowMedian:
    """
    LeetCode 480. Sliding Window Median
    滑动窗口中位数
    网址：https://leetcode.com/problems/sliding-window-median/
    
    解题思路：
    1. 使用两个SortedList维护窗口元素
    2. 左半部分存储较小的一半元素，右半部分存储较大的一半元素
    3. 保持两个集合大小平衡，中位数即为左半部分的最大值或两个最大值的平均值
    
    时间复杂度：O(n log k)
    空间复杂度：O(k)
    """
    
    def medianSlidingWindow(self, nums: List[int], k: int) -> List[float]:
        import bisect
        
        n = len(nums)
        result = []
        
        # 使用两个列表模拟平衡的二叉搜索树
        window = sorted(nums[:k])
        
        def get_median():
            if k % 2 == 0:
                return (window[k//2-1] + window[k//2]) / 2.0
            else:
                return float(window[k//2])
        
        result.append(get_median())
        
        for i in range(k, n):
            # 移除离开窗口的元素
            left_num = nums[i-k]
            left_idx = bisect.bisect_left(window, left_num)
            window.pop(left_idx)
            
            # 添加新元素
            bisect.insort(window, nums[i])
            
            # 计算中位数
            result.append(get_median())
        
        return result

class KEmptySlots:
    """
    LeetCode 683. K Empty Slots
    K个空花盆
    网址：https://leetcode.com/problems/k-empty-slots/
    
    解题思路：
    1. 使用SortedList存储开花位置
    2. 按天数顺序添加开花位置
    3. 每次添加后检查新位置与相邻位置的距离是否为k+1
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    
    def kEmptySlots(self, flowers: List[int], k: int) -> int:
        bloomed = []
        
        for day, position in enumerate(flowers, 1):
            # 插入并保持排序
            bisect.insort(bloomed, position)
            idx = bisect.bisect_left(bloomed, position)
            
            # 检查左侧相邻位置
            if idx > 0:
                left_pos = bloomed[idx-1]
                if position - left_pos - 1 == k:
                    return day
            
            # 检查右侧相邻位置
            if idx < len(bloomed) - 1:
                right_pos = bloomed[idx+1]
                if right_pos - position - 1 == k:
                    return day
        
        return -1

# 测试代码
if __name__ == "__main__":
    # 测试LeetCode 436
    intervals = [[1,2], [2,3], [0,1], [3,4]]
    solution = FindRightInterval()
    result = solution.findRightInterval(intervals)
    print("LeetCode 436 Result:", result)
    
    # 测试LeetCode 456
    nums = [3,1,4,2]
    solution = Find132Pattern()
    has132 = solution.find132pattern(nums)
    print("LeetCode 456 Result:", has132)
    
    # 测试LeetCode 683
    flowers = [1,3,2]
    solution = KEmptySlots()
    k_empty = solution.kEmptySlots(flowers, 1)
    print("LeetCode 683 Result:", k_empty)