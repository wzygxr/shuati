#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
HashSet和HashMap相关题目与解析

Python中使用set和dict（字典）
set基于哈希表，查询时间复杂度O(1)，元素无序
dict基于哈希表，查询时间复杂度O(1)，键值对无序

相关平台题目：
1. LeetCode 1. Two Sum (两数之和) - https://leetcode.com/problems/two-sum/
2. LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串) - https://leetcode.com/problems/longest-substring-without-repeating-characters/
3. LeetCode 36. Valid Sudoku (有效的数独) - https://leetcode.com/problems/valid-sudoku/
4. LeetCode 136. Single Number (只出现一次的数字) - https://leetcode.com/problems/single-number/
5. LeetCode 202. Happy Number (快乐数) - https://leetcode.com/problems/happy-number/
6. LeetCode 217. Contains Duplicate (存在重复元素) - https://leetcode.com/problems/contains-duplicate/
7. LeetCode 219. Contains Duplicate II (存在重复元素 II) - https://leetcode.com/problems/contains-duplicate-ii/
8. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
9. LeetCode 349. Intersection of Two Arrays (两个数组的交集) - https://leetcode.com/problems/intersection-of-two-arrays/
10. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
11. LeetCode 387. First Unique Character in a String (字符串中的第一个唯一字符) - https://leetcode.com/problems/first-unique-character-in-a-string/
12. LeetCode 448. Find All Numbers Disappeared in an Array (找到所有数组中消失的数字) - https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
13. LeetCode 575. Distribute Candies (分糖果) - https://leetcode.com/problems/distribute-candies/
14. LeetCode 811. Subdomain Visit Count (子域名访问计数) - https://leetcode.com/problems/subdomain-visit-count/
15. LeetCode 705. Design HashSet (设计哈希集合) - https://leetcode.com/problems/design-hashset/
16. LeetCode 706. Design HashMap (设计哈希映射) - https://leetcode.com/problems/design-hashmap/
17. HackerRank Java Hashset (Java哈希集) - https://www.hackerrank.com/challenges/java-hashset
18. LeetCode 128. Longest Consecutive Sequence (最长连续序列) - https://leetcode.com/problems/longest-consecutive-sequence/
19. LeetCode 49. Group Anagrams (字母异位词分组) - https://leetcode.com/problems/group-anagrams/
20. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
21. LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串) - https://leetcode.com/problems/longest-substring-without-repeating-characters/
22. LeetCode 36. Valid Sudoku (有效的数独) - https://leetcode.com/problems/valid-sudoku/
23. LeetCode 141. Linked List Cycle (环形链表) - https://leetcode.com/problems/linked-list-cycle/
24. LeetCode 160. Intersection of Two Linked Lists (相交链表) - https://leetcode.com/problems/intersection-of-two-linked-lists/
25. LintCode 547. Intersection of Two Arrays (两个数组的交集) - https://www.lintcode.com/problem/intersection-of-two-arrays/
26. Codeforces 4C. Registration System (注册系统) - https://codeforces.com/problemset/problem/4/C
27. AtCoder ABC 217 D - Cutting Woods (切割木材) - https://atcoder.jp/contests/abc217/tasks/abc217_d
28. USACO Silver: Why Did the Cow Cross the Road (为什么奶牛要过马路) - http://www.usaco.org/index.php?page=viewproblem2&cpid=714
29. 洛谷 P3374 【模板】树状数组 1 (模板树状数组1) - https://www.luogu.com.cn/problem/P3374
30. CodeChef STFOOD (街头食物) - https://www.codechef.com/problems/STFOOD
31. SPOJ ANARC09A - Seinfeld (宋飞正传) - https://www.spoj.com/problems/ANARC09A/
32. Project Euler Problem 1: Multiples of 3 and 5 (3和5的倍数) - https://projecteuler.net/problem=1
33. HackerRank Frequency Queries (频率查询) - https://www.hackerrank.com/challenges/frequency-queries
34. 计蒜客 T1100: 计算2的N次方 (计算2的N次方) - https://www.jisuanke.com/t/T1100
35. 杭电 OJ 1002: A + B Problem II (A+B问题II) - http://acm.hdu.edu.cn/showproblem.php?pid=1002
36. 牛客网 剑指Offer 03: 数组中重复的数字 (数组中重复的数字) - https://www.nowcoder.com/practice/623a5ac0ea5b4e5f95552655361ae0a8
37. acwing 799. 最长连续不重复子序列 (最长连续不重复子序列) - https://www.acwing.com/problem/content/801/
38. POJ 1002: 487-3279 (电话号码) - http://poj.org/problem?id=1002
39. UVa OJ 100: The 3n + 1 problem (3n+1问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=36
40. Timus OJ 1001: Reverse Root (反转平方根) - https://acm.timus.ru/problem.aspx?space=1&num=1001
41. Aizu OJ ALDS1_4_C: Dictionary (字典) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_4_C
42. Comet OJ Contest #0: 热身赛 A. 签到题 (签到题) - https://cometoj.com/contest/0/problem/A
43. MarsCode 火星编程竞赛: 字符串去重排序 (字符串去重排序) - https://www.marscode.cn/contest/1/problem/1001
44. ZOJ 1001: A + B Problem (A+B问题) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1001
45. LOJ 100: 顺序的分数 (顺序的分数) - https://loj.ac/p/100
46. 各大高校OJ: 清华大学OJ 1000: A+B Problem (A+B问题) - http://dsa.cs.tsinghua.edu.cn/oj/problem.shtml?id=1000
"""

from collections import defaultdict
import heapq

# LeetCode 1. Two Sum (两数之和)
def two_sum(nums, target):
    """
    题目描述：
    给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，
    并返回它们的数组下标。你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
    
    示例：
    输入：nums = [2,7,11,15], target = 9
    输出：[0,1]
    解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
    
    约束条件：
    2 <= nums.length <= 10^4
    -10^9 <= nums[i] <= 10^9
    -10^9 <= target <= 10^9
    只会存在一个有效答案
    
    解题思路：
    使用dict存储已经遍历过的数字及其索引，对于每个数字，检查target - 当前数字是否存在于dict中，
    如果存在则返回两个数字的索引，否则将当前数字和索引存入dict。
    
    时间复杂度：O(n)，其中n是数组长度，我们只需要遍历数组一次
    空间复杂度：O(n)，最坏情况下需要存储数组中所有元素及其索引
    
    :param nums: 整数数组
    :param target: 目标值
    :return: 两个整数的数组下标
    """
    # 创建dict存储数字和其索引
    map = {}
    
    # 遍历数组
    for i in range(len(nums)):
        # 计算需要找到的另一个数字
        complement = target - nums[i]
        
        # 检查该数字是否已存在于dict中
        if complement in map:
            # 如果存在，返回两个数字的索引
            return [map[complement], i]
        
        # 将当前数字和索引存入dict
        map[nums[i]] = i
    
    # 根据题目保证总会有一个解，这里仅为避免编译错误
    raise ValueError("No two sum solution")


# LeetCode 242. Valid Anagram (有效的字母异位词)
def is_anagram(s, t):
    """
    题目描述：
    给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
    注意：若 s 和 t 中每个字符出现的次数都相同，则称 s 和 t 互为字母异位词。
    
    示例：
    输入: s = "anagram", t = "nagaram"
    输出: true
    
    输入: s = "rat", t = "car"
    输出: false
    
    约束条件：
    1 <= s.length, t.length <= 5 * 10^4
    s 和 t 仅包含小写字母
    
    解题思路：
    使用dict记录字符串s中每个字符出现的次数，然后遍历字符串t，对每个字符在dict中对应的计数减1，
    如果某个字符不存在或计数小于0，则返回false。最后检查dict中是否所有字符计数都为0。
    
    时间复杂度：O(n)，其中n是字符串长度，需要遍历两个字符串
    空间复杂度：O(1)，因为字符集大小固定（小写字母26个），所以空间复杂度是常数
    
    :param s: 字符串s
    :param t: 字符串t
    :return: 如果t是s的字母异位词返回True，否则返回False
    """
    # 如果两个字符串长度不同，肯定不是字母异位词
    if len(s) != len(t):
        return False
    
    # 创建dict记录字符出现次数
    char_count = {}
    
    # 遍历字符串s，统计每个字符出现次数
    for c in s:
        char_count[c] = char_count.get(c, 0) + 1
    
    # 遍历字符串t，减少对应字符计数
    for c in t:
        # 如果字符不存在或计数已为0，返回False
        if c not in char_count or char_count[c] == 0:
            return False
        # 减少字符计数
        char_count[c] -= 1
    
    return True


# LeetCode 349. Intersection of Two Arrays (两个数组的交集)
def intersection(nums1, nums2):
    """
    题目描述：
    给定两个数组 nums1 和 nums2 ，返回它们的交集。输出结果中的每个元素一定是唯一的。
    我们可以不考虑输出结果的顺序。
    
    示例：
    输入：nums1 = [1,2,2,1], nums2 = [2,2]
    输出：[2]
    
    输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
    输出：[9,4]
    解释：[4,9] 也是可通过的
    
    约束条件：
    1 <= nums1.length, nums2.length <= 1000
    0 <= nums1[i], nums2[i] <= 1000
    
    解题思路：
    使用set存储nums1中的所有元素，然后遍历nums2，检查每个元素是否存在于set中，
    如果存在则加入结果集。
    
    时间复杂度：O(m+n)，其中m和n分别是两个数组的长度
    空间复杂度：O(m)，用于存储nums1中的元素
    
    :param nums1: 数组1
    :param nums2: 数组2
    :return: 交集数组
    """
    # 使用set存储nums1中的元素
    set1 = set(nums1)
    
    # 使用set存储交集结果，自动去重
    intersection_set = set()
    
    # 遍历nums2，查找交集
    for num in nums2:
        if num in set1:
            intersection_set.add(num)
    
    # 将结果转换为list
    return list(intersection_set)


# LeetCode 705. Design HashSet (设计哈希集合)
class MyHashSet:
    """
    题目描述：
    不使用任何内建的哈希表库设计一个哈希集合（HashSet）。
    实现 MyHashSet 类：
    void add(key) 向哈希集合中插入值 key 。
    bool contains(key) 返回哈希集合中是否存在这个值 key 。
    void remove(key) 将给定值 key 从哈希集合中删除。如果哈希集合中没有这个值，什么也不做。
    
    示例：
    输入：
    ["MyHashSet", "add", "add", "contains", "contains", "add", "contains", "remove", "contains"]
    [[], [1], [2], [1], [3], [2], [2], [2], [2]]
    输出：
    [null, null, null, true, false, null, true, null, false]
    
    约束条件：
    0 <= key <= 10^6
    最多调用 10^4 次 add、remove 和 contains
    
    解题思路：
    使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
    当发生哈希冲突时，将元素添加到对应位置的链表中。
    
    时间复杂度：O(n/b)，其中n是元素个数，b是桶数（在实际实现中我们使用10000作为桶数）
    空间复杂度：O(n)，存储所有元素
    """
    
    def __init__(self):
        """Initialize your data structure here."""
        self.BASE = 10000
        self.data = [[] for _ in range(self.BASE)]
    
    def _hash(self, key):
        return key % self.BASE
    
    def add(self, key):
        h = self._hash(key)
        if key not in self.data[h]:
            self.data[h].append(key)
    
    def remove(self, key):
        h = self._hash(key)
        if key in self.data[h]:
            self.data[h].remove(key)
    
    def contains(self, key):
        """Returns true if this set contains the specified element"""
        h = self._hash(key)
        return key in self.data[h]


# LeetCode 706. Design HashMap (设计哈希映射)
class MyHashMap:
    """
    题目描述：
    不使用任何内建的哈希表库设计一个哈希映射（HashMap）。
    实现 MyHashMap 类：
    MyHashMap() 用空映射初始化对象
    void put(int key, int value) 向 HashMap 插入一个键值对 (key, value) 。如果 key 已经存在于映射中，则更新其对应的值 value 。
    int get(int key) 返回特定的 key 所映射的 value ；如果映射中不包含 key 的映射，返回 -1 。
    void remove(key) 如果映射中存在 key 的映射，则移除 key 和它所对应的 value 。
    
    示例：
    输入：
    ["MyHashMap", "put", "put", "get", "get", "put", "get", "remove", "get"]
    [[], [1, 1], [2, 2], [1], [3], [2, 1], [2], [2], [2]]
    输出：
    [null, null, null, 1, -1, null, 1, null, -1]
    
    约束条件：
    0 <= key, value <= 10^6
    最多调用 10^4 次 put、get 和 remove 方法
    
    解题思路：
    使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
    每个链表节点存储键值对，当发生哈希冲突时，将节点添加到对应位置的链表中。
    
    时间复杂度：O(n/b)，其中n是元素个数，b是桶数（在实际实现中我们使用10000作为桶数）
    空间复杂度：O(n)，存储所有键值对
    """
    
    def __init__(self):
        """Initialize your data structure here."""
        self.BASE = 10000
        self.data = [[] for _ in range(self.BASE)]
    
    def _hash(self, key):
        return key % self.BASE
    
    def put(self, key, value):
        h = self._hash(key)
        for i, (k, v) in enumerate(self.data[h]):
            if k == key:
                self.data[h][i] = (key, value)
                return
        self.data[h].append((key, value))
    
    def get(self, key):
        """Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key"""
        h = self._hash(key)
        for k, v in self.data[h]:
            if k == key:
                return v
        return -1
    
    def remove(self, key):
        """Removes the mapping of the specified value key if this map contains a mapping for the key"""
        h = self._hash(key)
        for i, (k, v) in enumerate(self.data[h]):
            if k == key:
                del self.data[h][i]
                return


# LeetCode 128. Longest Consecutive Sequence (最长连续序列)
def longest_consecutive(nums):
    """
    题目描述：
    给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
    请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
    
    示例：
    输入：nums = [100,4,200,1,3,2]
    输出：4
    解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
    
    输入：nums = [0,3,7,2,5,8,4,6,0,1]
    输出：9
    
    约束条件：
    0 <= nums.length <= 10^5
    -10^9 <= nums[i] <= 10^9
    
    解题思路：
    使用set存储所有数字，然后对于每个数字，如果它是某个序列的开始（即num-1不在set中），
    则从该数字开始向后查找连续的数字，计算序列长度。
    
    时间复杂度：O(n)，虽然有嵌套循环，但每个元素最多被访问两次
    空间复杂度：O(n)，用于存储set
    """
    num_set = set(nums)
    longest_streak = 0
    
    for num in num_set:
        # 只有当num-1不存在时，num才是一个序列的开始
        if num - 1 not in num_set:
            current_num = num
            current_streak = 1
            
            # 向后查找连续的数字
            while current_num + 1 in num_set:
                current_num += 1
                current_streak += 1
            
            longest_streak = max(longest_streak, current_streak)
    
    return longest_streak


# LeetCode 49. Group Anagrams (字母异位词分组)
def group_anagrams(strs):
    """
    题目描述：
    给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
    字母异位词是由重新排列源单词的所有字母得到的一个新单词。
    
    示例：
    输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
    输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
    
    输入: strs = [""]
    输出: [[""]]
    
    输入: strs = ["a"]
    输出: [["a"]]
    
    约束条件：
    1 <= strs.length <= 10^4
    0 <= strs[i].length <= 100
    strs[i] 仅包含小写字母
    
    解题思路：
    使用dict存储分组结果，键为排序后的字符串，值为该组的所有字符串。
    对于每个字符串，将其字符排序后作为键，将原字符串添加到对应的列表中。
    
    时间复杂度：O(N*K*logK)，其中N是字符串数组的长度，K是字符串的最大长度
    空间复杂度：O(N*K)，用于存储所有字符串
    """
    map = defaultdict(list)
    
    for s in strs:
        # 将字符串排序后作为键
        key = ''.join(sorted(s))
        # 将原字符串添加到对应的列表中
        map[key].append(s)
    
    return list(map.values())


# LeetCode 347. Top K Frequent Elements (前 K 个高频元素)
def top_k_frequent(nums, k):
    """
    题目描述：
    给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
    
    示例：
    输入: nums = [1,1,1,2,2,3], k = 2
    输出: [1,2]
    
    输入: nums = [1], k = 1
    输出: [1]
    
    约束条件：
    1 <= nums.length <= 10^5
    k 的取值范围是 [1, 数组中不相同的元素的个数]
    题目数据保证答案唯一
    
    解题思路：
    1. 使用dict统计每个元素的频率
    2. 使用最小堆维护前k个高频元素
    3. 遍历dict，维护堆的大小为k
    4. 最后从堆中取出所有元素
    
    时间复杂度：O(N*logK)，其中N是数组长度
    空间复杂度：O(N)，用于存储dict和堆
    """
    # 统计每个元素的频率
    freq_map = defaultdict(int)
    for num in nums:
        freq_map[num] += 1
    
    # 使用最小堆维护前k个高频元素
    heap = []
    
    # 遍历频率映射，维护堆的大小为k
    for key, value in freq_map.items():
        heapq.heappush(heap, (value, key))
        if len(heap) > k:
            heapq.heappop(heap)
    
    # 从堆中取出所有元素
    result = []
    while heap:
        result.append(heapq.heappop(heap)[1])
    
    return result[::-1]  # 反转结果以获得正确顺序


# LeetCode 219. Contains Duplicate II (存在重复元素 II)
def contains_nearby_duplicate(nums, k):
    """
    题目描述：
    给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，
    使得 nums[i] == nums[j]，并且 i 和 j 的差的绝对值至多为 k。
    
    示例：
    输入: nums = [1,2,3,1], k = 3
    输出: True
    
    输入: nums = [1,0,1,1], k = 1
    输出: True
    
    输入: nums = [1,2,3,1,2,3], k = 2
    输出: False
    
    约束条件：
    1 <= nums.length <= 10^5
    -10^9 <= nums[i] <= 10^9
    0 <= k <= 10^5
    
    解题思路：
    使用dict存储每个元素最后一次出现的索引，遍历数组时，检查当前元素是否在dict中存在，
    如果存在且当前索引与存储的索引之差的绝对值小于等于k，则返回True；
    否则更新dict中该元素的索引为当前索引。
    
    时间复杂度：O(n)，其中n是数组长度，我们只需要遍历数组一次
    空间复杂度：O(min(n,k))，最坏情况下需要存储min(n,k)个元素
    """
    # 创建字典存储数字和其索引
    num_dict = {}
    
    # 遍历数组
    for i in range(len(nums)):
        # 检查当前数字是否已存在于字典中
        if nums[i] in num_dict:
            # 如果存在，检查索引差是否小于等于k
            if i - num_dict[nums[i]] <= k:
                return True
        # 更新或添加数字的索引
        num_dict[nums[i]] = i
    
    # 遍历完数组未找到符合条件的重复元素
    return False


# LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串)
def length_of_longest_substring(s):
    """
    题目描述：
    给定一个字符串 s，请你找出其中不含有重复字符的最长子串的长度。
    
    示例：
    输入: s = "abcabcbb"
    输出: 3
    解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
    
    输入: s = "bbbbb"
    输出: 1
    解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
    
    输入: s = "pwwkew"
    输出: 3
    解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
    注意，你的答案必须是子串的长度，"pwke" 是一个子序列，不是子串。
    
    约束条件：
    0 <= s.length <= 5 * 10^4
    s 由英文字母、数字、符号和空格组成
    
    解题思路：
    使用滑动窗口和dict，dict记录每个字符最后一次出现的位置。
    维护一个左边界left，当遇到重复字符时，将left更新为重复字符上一次出现位置的下一个位置。
    计算当前窗口长度i-left+1，并更新最大长度。
    
    时间复杂度：O(n)，其中n是字符串长度
    空间复杂度：O(min(n, m))，其中m是字符集大小
    """
    # 创建字典存储字符和其位置
    char_dict = {}
    max_length = 0
    left = 0
    
    # 遍历字符串
    for i in range(len(s)):
        # 如果字符已存在且在当前窗口内，更新左边界
        if s[i] in char_dict and char_dict[s[i]] >= left:
            left = char_dict[s[i]] + 1
        # 更新字符位置
        char_dict[s[i]] = i
        # 更新最大长度
        max_length = max(max_length, i - left + 1)
    
    return max_length


# LeetCode 36. Valid Sudoku (有效的数独)
def is_valid_sudoku(board):
    """
    题目描述：
    请你判断一个 9 x 9 的数独是否有效。只需要 根据以下规则 ，验证已经填入的数字是否有效即可。
    1. 数字 1-9 在每一行只能出现一次。
    2. 数字 1-9 在每一列只能出现一次。
    3. 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
    
    注意：
    - 一个有效的数独（部分已被填充）不一定是可解的。
    - 只需要根据以上规则，验证已经填入的数字是否有效即可。
    - 空白格用 '.' 表示。
    
    示例：
    输入：board = 
    [
      ["5","3",".",".","7",".",".",".","0"],
      ["6",".",".","1","9","5",".",".","0"],
      [".","9","8",".",".",".",".","6","0"],
      ["8",".",".",".","6",".",".",".","3"],
      ["4",".",".","8",".","3",".",".","1"],
      ["7",".",".",".","2",".",".",".","6"],
      [".","6",".",".",".",".","2","8","0"],
      [".",".",".","4","1","9",".",".","5"],
      [".",".",".",".","8",".",".","7","9"]
    ]
    输出：True
    
    约束条件：
    board.length == 9
    board[i].length == 9
    board[i][j] 是一位数字或者 '.'
    
    解题思路：
    使用三个列表分别存储行、列、3x3宫格中出现过的数字。
    每个列表包含9个set，分别对应9行、9列和9个宫格。
    遍历数独，对于每个非空白字符，检查是否已经在对应的行、列或宫格中出现过。
    宫格索引可以通过公式：box_index = (row // 3) * 3 + (col // 3) 计算得到。
    
    时间复杂度：O(1)，因为数独大小固定为9x9
    空间复杂度：O(1)，固定大小的set列表
    """
    # 初始化行、列、宫格的集合列表
    rows = [set() for _ in range(9)]
    cols = [set() for _ in range(9)]
    boxes = [set() for _ in range(9)]
    
    # 遍历数独
    for row in range(9):
        for col in range(9):
            # 获取当前单元格的值
            val = board[row][col]
            # 跳过空白格
            if val == '.':
                continue
            
            # 计算宫格索引
            box_index = (row // 3) * 3 + (col // 3)
            
            # 检查是否重复
            if (val in rows[row] or 
                val in cols[col] or 
                val in boxes[box_index]):
                return False
            
            # 添加到对应的集合中
            rows[row].add(val)
            cols[col].add(val)
            boxes[box_index].add(val)
    
    # 所有检查通过，返回True
    return True


# LeetCode 141. Linked List Cycle (环形链表)
# 定义链表节点类
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

def has_cycle(head):
    """
    题目描述：
    给定一个链表，判断链表中是否有环。
    如果链表中存在环，则返回 True 。 否则，返回 False 。
    
    进阶：
    你能否不使用额外空间解决此题？
    
    解题思路1（使用set）：
    遍历链表，将每个节点存入set中，如果遇到已存在的节点，则说明有环。
    
    时间复杂度：O(n)，其中n是链表长度
    空间复杂度：O(n)，需要存储所有节点
    """
    # 创建集合存储已访问的节点
    seen = set()
    current = head
    
    # 遍历链表
    while current:
        # 如果节点已存在于集合中，说明有环
        if current in seen:
            return True
        # 将当前节点添加到集合
        seen.add(current)
        # 移动到下一个节点
        current = current.next
    
    # 遍历完链表未发现环
    return False


# LeetCode 160. Intersection of Two Linked Lists (相交链表)
def get_intersection_node(headA, headB):
    """
    题目描述：
    给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表没有交点，返回 None 。
    
    解题思路1（使用set）：
    遍历链表A，将每个节点存入set中，然后遍历链表B，检查节点是否存在于set中。
    
    时间复杂度：O(m+n)，其中m和n分别是两个链表的长度
    空间复杂度：O(m)，需要存储链表A的所有节点
    """
    # 创建集合存储链表A的节点
    seen = set()
    current = headA
    
    # 将链表A的所有节点存入集合
    while current:
        seen.add(current)
        current = current.next
    
    # 遍历链表B，查找交集
    current = headB
    while current:
        # 如果节点存在于集合中，说明是交点
        if current in seen:
            return current
        current = current.next
    
    # 没有交点
    return None


# 测试代码
if __name__ == "__main__":
    # 测试两数之和
    print("测试两数之和:")
    nums = [2, 7, 11, 15]
    target = 9
    result = two_sum(nums, target)
    print(f"[{result[0]}, {result[1]}]")
    
    # 测试有效的字母异位词
    print("测试有效的字母异位词:")
    print(is_anagram("anagram", "nagaram"))  # True
    print(is_anagram("rat", "car"))  # False
    
    # 测试两个数组的交集
    print("测试两个数组的交集:")
    nums1 = [1, 2, 2, 1]
    nums2 = [2, 2]
    result = intersection(nums1, nums2)
    print(f"交集: {result}")
    
    # 测试设计哈希集合
    print("测试设计哈希集合:")
    my_hash_set = MyHashSet()
    my_hash_set.add(1)
    my_hash_set.add(2)
    print(my_hash_set.contains(1))  # True
    print(my_hash_set.contains(3))  # False
    my_hash_set.add(2)
    print(my_hash_set.contains(2))  # True
    my_hash_set.remove(2)
    print(my_hash_set.contains(2))  # False
    
    # 测试设计哈希映射
    print("测试设计哈希映射:")
    my_hash_map = MyHashMap()
    my_hash_map.put(1, 1)
    my_hash_map.put(2, 2)
    
    # 测试存在重复元素 II
    print("测试存在重复元素 II:")
    nums1 = [1, 2, 3, 1]
    print(contains_nearby_duplicate(nums1, 3))  # True
    nums2 = [1, 0, 1, 1]
    print(contains_nearby_duplicate(nums2, 1))  # True
    nums3 = [1, 2, 3, 1, 2, 3]
    print(contains_nearby_duplicate(nums3, 2))  # False
    
    # 测试无重复字符的最长子串
    print("测试无重复字符的最长子串:")
    print(length_of_longest_substring("abcabcbb"))  # 3
    print(length_of_longest_substring("bbbbb"))  # 1
    print(length_of_longest_substring("pwwkew"))  # 3
    
    # 测试有效的数独
    print("测试有效的数独:")
    board = [
        ["5", "3", ".", ".", "7", ".", ".", ".", "."],
        ["6", ".", ".", "1", "9", "5", ".", ".", "."],
        [".", "9", "8", ".", ".", ".", ".", "6", "."],
        ["8", ".", ".", ".", "6", ".", ".", ".", "3"],
        ["4", ".", ".", "8", ".", "3", ".", ".", "1"],
        ["7", ".", ".", ".", "2", ".", ".", ".", "6"],
        [".", "6", ".", ".", ".", ".", "2", "8", "."],
        [".", ".", ".", "4", "1", "9", ".", ".", "5"],
        [".", ".", ".", ".", "8", ".", ".", "7", "9"]
    ]
    print(is_valid_sudoku(board))  # True
    
    # 测试环形链表
    print("测试环形链表:")
    # 创建有环链表
    node1 = ListNode(3)
    node2 = ListNode(2)
    node3 = ListNode(0)
    node4 = ListNode(-4)
    node1.next = node2
    node2.next = node3
    node3.next = node4
    node4.next = node2  # 环
    print(has_cycle(node1))  # True
    
    # 创建无环链表
    node1 = ListNode(1)
    node2 = ListNode(2)
    node1.next = node2
    print(has_cycle(node1))  # False
    
    # 测试相交链表
    print("测试相交链表:")
    # 创建相交节点
    intersect = ListNode(8)
    intersect.next = ListNode(4)
    intersect.next.next = ListNode(5)
    
    # 创建链表A
    headA = ListNode(4)
    headA.next = ListNode(1)
    headA.next.next = intersect
    
    # 创建链表B
    headB = ListNode(5)
    headB.next = ListNode(6)
    headB.next.next = ListNode(1)
    headB.next.next.next = intersect
    
    result = get_intersection_node(headA, headB)
    print(result.val if result else None)  # 8
    print(my_hash_map.get(1))  # 1
    print(my_hash_map.get(3))  # -1
    my_hash_map.put(2, 1)
    print(my_hash_map.get(2))  # 1
    my_hash_map.remove(2)
    print(my_hash_map.get(2))  # -1
    
    # 测试最长连续序列
    print("测试最长连续序列:")
    nums = [100, 4, 200, 1, 3, 2]
    print(longest_consecutive(nums))  # 4
    
    # 测试字母异位词分组
    print("测试字母异位词分组:")
    strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
    groups = group_anagrams(strs)
    print(groups)
    
    # 测试前K个高频元素
    print("测试前K个高频元素:")
    nums = [1, 1, 1, 2, 2, 3]
    k = 2
    result = top_k_frequent(nums, k)
    print(result)  # [1, 2]
    print("测试最长连续序列:")
    nums = [100, 4, 200, 1, 3, 2]
    print(longest_consecutive(nums))  # 4
    
    # 测试字母异位词分组
    print("测试字母异位词分组:")
    strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
    groups = group_anagrams(strs)
    print(groups)
    
    # 测试前K个高频元素
    print("测试前K个高频元素:")
    nums = [1, 1, 1, 2, 2, 3]
    k = 2
    result = top_k_frequent(nums, k)
    print(result)  # [1, 2]