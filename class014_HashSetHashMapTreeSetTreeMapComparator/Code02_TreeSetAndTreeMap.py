#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Python版本：TreeSet和TreeMap相关题目与解析

在Python中，我们使用sortedcontainers库中的SortedList和SortedDict来实现TreeSet和TreeMap功能
或者使用bisect模块来实现有序列表功能

相关平台题目：
1. LeetCode 220. Contains Duplicate III (存在重复元素 III) - https://leetcode.com/problems/contains-duplicate-iii/
2. LeetCode 933. Number of Recent Calls (最近的请求次数) - https://leetcode.com/problems/number-of-recent-calls/
3. LeetCode 729. My Calendar I (我的日程安排表 I) - https://leetcode.com/problems/my-calendar-i/
4. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
5. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
6. Codeforces 4C. Registration System (注册系统) - https://codeforces.com/problemset/problem/4/C
7. AtCoder ABC 217 D - Cutting Woods (切割木材) - https://atcoder.jp/contests/abc217/tasks/abc217_d
8. USACO Silver: Why Did the Cow Cross the Road (为什么奶牛要过马路) - http://www.usaco.org/index.php?page=viewproblem2&cpid=714
9. 洛谷 P3374 【模板】树状数组 1 (模板树状数组1) - https://www.luogu.com.cn/problem/P3374
10. CodeChef STFOOD (街头食物) - https://www.codechef.com/problems/STFOOD
11. SPOJ ANARC09A - Seinfeld (宋飞正传) - https://www.spoj.com/problems/ANARC09A/
12. Project Euler Problem 1: Multiples of 3 and 5 (3和5的倍数) - https://projecteuler.net/problem=1
13. HackerRank Frequency Queries (频率查询) - https://www.hackerrank.com/challenges/frequency-queries
14. 计蒜客 T1100: 计算2的N次方 (计算2的N次方) - https://www.jisuanke.com/t/T1100
15. 杭电 OJ 1002: A + B Problem II (A+B问题II) - http://acm.hdu.edu.cn/showproblem.php?pid=1002
16. 牛客网 剑指Offer 03: 数组中重复的数字 (数组中重复的数字) - https://www.nowcoder.com/practice/623a5ac0ea5b4e5f95552655361ae0a8
17. acwing 799. 最长连续不重复子序列 (最长连续不重复子序列) - https://www.acwing.com/problem/content/801/
18. POJ 1002: 487-3279 (电话号码) - http://poj.org/problem?id=1002
19. UVa OJ 100: The 3n + 1 problem (3n+1问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=36
20. Timus OJ 1001: Reverse Root (反转平方根) - https://acm.timus.ru/problem.aspx?space=1&num=1001
21. Aizu OJ ALDS1_4_C: Dictionary (字典) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_4_C
22. Comet OJ Contest #0: 热身赛 A. 签到题 (签到题) - https://cometoj.com/contest/0/problem/A
23. MarsCode 火星编程竞赛: 字符串去重排序 (字符串去重排序) - https://www.marscode.cn/contest/1/problem/1001
24. ZOJ 1001: A + B Problem (A+B问题) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1001
25. LOJ 100: 顺序的分数 (顺序的分数) - https://loj.ac/p/100
26. 各大高校OJ: 清华大学OJ 1000: A+B Problem (A+B问题) - http://dsa.cs.tsinghua.edu.cn/oj/problem.shtml?id=1000
"""
import bisect
from collections import defaultdict

# LeetCode 220. Contains Duplicate III (存在重复元素 III)
def contains_nearby_almost_duplicate(nums, k, t):
    """
    题目描述：
    给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在两个不同下标 i 和 j，
    使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。
    如果存在则返回 true，不存在返回 false。
    
    示例：
    输入：nums = [1,2,3,1], k = 3, t = 0
    输出：true
    
    输入：nums = [1,0,1,1], k = 1, t = 2
    输出：true
    
    输入：nums = [1,5,9,1,5,9], k = 2, t = 3
    输出：false
    
    约束条件：
    0 <= nums.length <= 2 * 10^4
    -2^31 <= nums[i] <= 2^31 - 1
    0 <= k <= 10^4
    0 <= t <= 2^31 - 1
    
    解题思路：
    使用滑动窗口维护一个大小为k的窗口，对于每个新元素，在窗口中查找是否存在一个元素
    与当前元素的差值不超过t。利用bisect模块可以高效地找到最接近的元素。
    
    时间复杂度：O(n log min(n,k))，其中n是数组长度
    空间复杂度：O(min(n,k))，用于存储滑动窗口中的元素
    """
    # 使用列表维护滑动窗口，并保持有序
    window = []
    
    for i in range(len(nums)):
        # 查找大于等于当前元素的最小元素
        pos = bisect.bisect_left(window, nums[i])
        
        # 检查右侧元素
        if pos < len(window) and abs(window[pos] - nums[i]) <= t:
            return True
            
        # 检查左侧元素
        if pos > 0 and abs(window[pos-1] - nums[i]) <= t:
            return True
        
        # 添加当前元素到窗口
        bisect.insort(window, nums[i])
        
        # 维护滑动窗口大小为k
        if len(window) > k:
            # 移除窗口中最旧的元素
            window.remove(nums[i - k])
    
    return False


# LeetCode 933. Number of Recent Calls (最近的请求次数)
class RecentCounter:
    """
    题目描述：
    写一个 RecentCounter 类来计算特定时间范围内最近的请求。
    请你实现 RecentCounter 类：
    RecentCounter() 初始化计数器，请求数为 0 。
    int ping(int t) 在时间 t 添加一个新请求，其中 t 表示以毫秒为单位的某个时间，
    并返回过去 3000 毫秒内发生的所有请求数（包括新请求）。
    精确地说，返回在 [t-3000, t] 内发生的请求数。
    保证每次对 ping 的调用都使用比之前更大的 t 值。
    
    示例：
    输入：
    ["RecentCounter", "ping", "ping", "ping", "ping"]
    [[], [1], [100], [3001], [3002]]
    输出：
    [null, 1, 2, 3, 3]
    
    约束条件：
    1 <= t <= 10^9
    保证每次对 ping 调用所使用的 t 值都 严格递增
    至多调用 ping 方法 10^4 次
    
    解题思路：
    使用列表存储所有请求的时间戳，每次ping时，使用bisect查找[t-3000, t]范围内的请求数量。
    
    时间复杂度：O(log n) 每次ping操作
    空间复杂度：O(n) 存储所有请求
    """
    
    def __init__(self):
        self.requests = []
    
    def ping(self, t: int) -> int:
        self.requests.append(t)
        # 查找大于等于t-3000的第一个位置
        left = bisect.bisect_left(self.requests, t - 3000)
        # 查找大于t的最后一个位置
        right = bisect.bisect_right(self.requests, t)
        # 返回范围内的请求数量
        return right - left


# LeetCode 729. My Calendar I (我的日程安排表 I)
class MyCalendar:
    """
    题目描述：
    实现一个 MyCalendar 类来存放你的日程安排。如果要添加的日程安排不会造成 重复预订 ，则可以存储这个新的日程安排。
    当两个日程安排有一些时间上的交叉时（例如两个日程安排都在同一时间内），就会产生 重复预订 。
    日程可以用一对整数 start 和 end 表示，这里的时间是半开区间，即 [start, end)，
    实数 x 的范围为 start <= x < end 。
    
    实现 MyCalendar 类：
    MyCalendar() 初始化日历对象。
    boolean book(int start, int end) 如果可以将日程安排成功添加到日历中而不会导致重复预订，返回 true 。
    否则，返回 false 并且不要将该日程安排添加到日历中。
    
    示例：
    输入：
    ["MyCalendar", "book", "book", "book"]
    [[], [10, 20], [15, 25], [20, 30]]
    输出：
    [null, true, false, true]
    
    约束条件：
    0 <= start < end <= 10^9
    每个测试用例，调用 book 方法的次数最多不超过 1000 次。
    
    解题思路：
    使用列表存储已预订的日程，并保持有序。
    对于新的日程[start, end)，使用bisect查找可能冲突的日程：
    1. 查找开始时间小于等于start的最大日程，检查其结束时间是否大于start
    2. 查找开始时间大于等于start的最小日程，检查其开始时间是否小于end
    如果没有冲突，则添加新日程。
    
    时间复杂度：O(log n) 每次book操作
    空间复杂度：O(n) 存储所有日程
    """
    
    def __init__(self):
        self.calendar = []  # 存储 (start, end) 元组，并保持有序
    
    def book(self, start: int, end: int) -> bool:
        # 查找插入位置
        pos = bisect.bisect_left(self.calendar, (start, end))
        
        # 检查与前一个日程是否冲突
        if pos > 0 and self.calendar[pos-1][1] > start:
            return False
        
        # 检查与后一个日程是否冲突
        if pos < len(self.calendar) and self.calendar[pos][0] < end:
            return False
        
        # 没有冲突，插入新日程
        self.calendar.insert(pos, (start, end))
        return True


# LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
def count_smaller(nums):
    """
    题目描述：
    给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： 
    counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
    
    示例：
    输入：nums = [5,2,6,1]
    输出：[2,1,1,0]
    
    解释：
    5 的右侧有 2 个更小的元素 (2 和 1)
    2 的右侧仅有 1 个更小的元素 (1)
    6 的右侧有 1 个更小的元素 (1)
    1 的右侧有 0 个更小的元素
    
    约束条件：
    1 <= nums.length <= 10^5
    -10^4 <= nums[i] <= 10^4
    
    解题思路：
    从右向左遍历数组，使用列表维护已遍历的元素并保持有序。
    对于每个元素，在列表中查找小于它的元素个数。
    
    时间复杂度：O(n log n)，其中n是数组长度
    空间复杂度：O(n)，用于存储列表
    """
    result = []
    sorted_list = []  # 保持有序的列表
    
    # 从右向左遍历
    for i in range(len(nums) - 1, -1, -1):
        # 查找小于当前元素的插入位置，即小于当前元素的元素个数
        pos = bisect.bisect_left(sorted_list, nums[i])
        result.append(pos)
        # 将当前元素插入到有序列表中
        bisect.insort(sorted_list, nums[i])
    
    # 反转结果数组
    return result[::-1]


# LeetCode 493. Reverse Pairs (翻转对)
def reverse_pairs(nums):
    """
    题目描述：
    给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们将 (i, j) 称作一个重要翻转对。
    你需要返回给定数组中的重要翻转对的数量。
    
    示例：
    输入: [1,3,2,3,1]
    输出: 2
    
    输入: [2,4,3,5,1]
    输出: 3
    
    约束条件：
    给定数组的长度不会超过50000。
    输入数组中的所有数字都在32位整数的表示范围内。
    
    解题思路：
    使用列表维护已遍历的元素并保持有序，对于每个新元素，在列表中查找满足条件的元素个数。
    
    时间复杂度：O(n log n)，其中n是数组长度
    空间复杂度：O(n)，用于存储列表
    """
    count = 0
    sorted_list = []  # 保持有序的列表
    
    # 从右向左遍历
    for i in range(len(nums) - 1, -1, -1):
        # 查找小于nums[i]/2.0的元素个数
        pos = bisect.bisect_left(sorted_list, nums[i] / 2.0)
        count += pos
        # 将当前元素插入到有序列表中
        bisect.insort(sorted_list, nums[i])
    
    return count


# 测试代码
if __name__ == "__main__":
    # 测试存在重复元素 III
    print("测试存在重复元素 III:")
    nums = [1, 2, 3, 1]
    print(contains_nearby_almost_duplicate(nums, 3, 0))  # True
    
    # 测试最近的请求次数
    print("测试最近的请求次数:")
    counter = RecentCounter()
    print(counter.ping(1))     # 1
    print(counter.ping(100))   # 2
    print(counter.ping(3001))  # 3
    print(counter.ping(3002))  # 3
    
    # 测试我的日程安排表
    print("测试我的日程安排表:")
    calendar = MyCalendar()
    print(calendar.book(10, 20))  # True
    print(calendar.book(15, 25))  # False
    print(calendar.book(20, 30))  # True
    
    # 测试计算右侧小于当前元素的个数
    print("测试计算右侧小于当前元素的个数:")
    nums = [5, 2, 6, 1]
    result = count_smaller(nums)
    print(result)  # [2, 1, 1, 0]
    
    # 测试翻转对
    print("测试翻转对:")
    nums = [1, 3, 2, 3, 1]
    print(reverse_pairs(nums))  # 2