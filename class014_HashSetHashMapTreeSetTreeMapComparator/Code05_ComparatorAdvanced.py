from typing import List
import sys

"""
Comparator高级应用题目实现（Python版本）
包含LeetCode 524, 937, 1331, 1366, 1451, 1509, 1561, 1636, 1710, 1859等题目

Python中Comparator的实现：
- Python使用key参数实现自定义排序
- 使用lambda表达式简化排序逻辑
- sorted函数是稳定排序
- functools.cmp_to_key可以将比较函数转换为key函数

时间复杂度分析：
- 排序操作：O(n log n)
- 自定义比较器：O(1)每次比较
- 复杂比较逻辑：O(k)每次比较，k为比较元素的复杂度

空间复杂度分析：
- 排序算法：O(n) 临时空间
- 额外数据结构：O(n)

工程化考量：
1. 异常处理：处理空输入、边界条件
2. 性能优化：使用key参数避免重复计算
3. 代码可读性：清晰的比较逻辑和注释
4. 稳定性：Python的sorted是稳定排序

相关平台题目：
1. LeetCode 524. Longest Word in Dictionary through Deleting (通过删除字母匹配到字典里最长单词) - https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/
2. LeetCode 937. Reorder Data in Log Files (重新排列日志文件) - https://leetcode.com/problems/reorder-data-in-log-files/
3. LeetCode 1331. Rank Transform of an Array (数组序号转换) - https://leetcode.com/problems/rank-transform-of-an-array/
4. LeetCode 1366. Rank Teams by Votes (通过投票对团队排名) - https://leetcode.com/problems/rank-teams-by-votes/
5. LeetCode 1451. Rearrange Words in a Sentence (重新排列句子中的单词) - https://leetcode.com/problems/rearrange-words-in-a-sentence/
6. LeetCode 1509. Minimum Difference Between Largest and Smallest Value in Three Moves (三次操作后最大值与最小值的最小差) - https://leetcode.com/problems/minimum-difference-between-largest-and-smallest-value-in-three-moves/
7. LeetCode 1561. Maximum Number of Coins You Can Get (你可以获得的最大硬币数目) - https://leetcode.com/problems/maximum-number-of-coins-you-can-get/
8. LeetCode 1636. Sort Array by Increasing Frequency (按照频率将数组升序排序) - https://leetcode.com/problems/sort-array-by-increasing-frequency/
9. LeetCode 1710. Maximum Units on a Truck (卡车上的最大单元数) - https://leetcode.com/problems/maximum-units-on-a-truck/
10. LeetCode 1859. Sorting the Sentence (将句子排序) - https://leetcode.com/problems/sorting-the-sentence/
11. LintCode 613. High Five (最高分五科) - https://www.lintcode.com/problem/high-five/
12. HackerEarth Monk and the Magical Candy Bags (和尚与魔法糖果袋) - https://www.hackerearth.com/practice/algorithms/greedy/basics-of-greedy-algorithms/practice-problems/algorithm/monk-and-the-magical-candy-bags/
13. CodeChef FRGTNLNG (遗忘的语言) - https://www.codechef.com/problems/FRGTNLNG
14. SPOJ DICT (字典) - https://www.spoj.com/problems/DICT/
15. Project Euler Problem 2: Even Fibonacci numbers (偶数斐波那契数) - https://projecteuler.net/problem=2
16. HackerRank Maximum Palindromes (最大回文) - https://www.hackerrank.com/challenges/maximum-palindromes
17. 计蒜客 T1101: 阶乘 (阶乘) - https://www.jisuanke.com/t/T1101
18. 杭电 OJ 1003: Max Sum (最大子序列和) - http://acm.hdu.edu.cn/showproblem.php?pid=1003
19. 牛客网 剑指Offer 50: 第一个只出现一次的字符 (第一个只出现一次的字符) - https://www.nowcoder.com/practice/1c82e8cf713b4bbeb2a5b31cf5b0417c
20. acwing 800. 数组元素的目标和 (数组元素的目标和) - https://www.acwing.com/problem/content/802/
21. POJ 1003: Hangover (悬垂) - http://poj.org/problem?id=1003
22. UVa OJ 101: The Blocks Problem (积木问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=37
23. Timus OJ 1005: Stone Pile (石子堆) - https://acm.timus.ru/problem.aspx?space=1&num=1005
24. Aizu OJ ALDS1_5_A: Exhaustive Search (穷举搜索) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_A
25. Comet OJ Contest #1: 热身赛 B. 简单的数学题 (简单的数学题) - https://cometoj.com/contest/1/problem/B
26. MarsCode 火星编程竞赛: 数字统计 (数字统计) - https://www.marscode.cn/contest/1/problem/1002
27. ZOJ 1002: Fire Net (消防网) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1002
28. LOJ 101: 最小生成树 (最小生成树) - https://loj.ac/p/101
29. 各大高校OJ: 北京大学OJ 1000: A+B Problem (A+B问题) - http://poj.org/problem?id=1000
30. Codeforces 122A. Lucky Division (幸运分割) - https://codeforces.com/problemset/problem/122/A
31. AtCoder ABC 218 C - Shapes (形状) - https://atcoder.jp/contests/abc218/tasks/abc218_c
32. USACO Bronze: Block Game (积木游戏) - http://www.usaco.org/index.php?page=viewproblem2&cpid=664
33. 洛谷 P3366 【模板】最小生成树 (模板最小生成树) - https://www.luogu.com.cn/problem/P3366
34. LeetCode 149. Max Points on a Line (直线上最多的点数) - https://leetcode.com/problems/max-points-on-a-line/
35. LeetCode 215. Kth Largest Element in an Array (数组中的第K个最大元素) - https://leetcode.com/problems/kth-largest-element-in-an-array/
36. LeetCode 295. Find Median from Data Stream (数据流的中位数) - https://leetcode.com/problems/find-median-from-data-stream/
37. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
38. LeetCode 327. Count of Range Sum (区间和的个数) - https://leetcode.com/problems/count-of-range-sum/
39. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
40. LeetCode 148. Sort List (排序链表) - https://leetcode.com/problems/sort-list/
41. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
42. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
43. LeetCode 451. Sort Characters By Frequency (根据字符出现频率排序) - https://leetcode.com/problems/sort-characters-by-frequency/
44. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
45. LeetCode 539. Minimum Time Difference (最小时间差) - https://leetcode.com/problems/minimum-time-difference/
46. LeetCode 791. Custom Sort String (自定义字符串排序) - https://leetcode.com/problems/custom-sort-string/
47. LeetCode 1030. Matrix Cells in Distance Order (距离顺序排列矩阵单元格) - https://leetcode.com/problems/matrix-cells-in-distance-order/
48. LeetCode 1122. Relative Sort Array (数组的相对排序) - https://leetcode.com/problems/relative-sort-array/
49. LeetCode 1356. Sort Integers by The Number of 1 Bits (根据数字二进制下 1 的数目排序) - https://leetcode.com/problems/sort-integers-by-the-number-of-1-bits/
50. LeetCode 179. Largest Number (最大数) - https://leetcode.com/problems/largest-number/
"""

class LongestWordInDictionary:
    """
    LeetCode 524. Longest Word in Dictionary through Deleting
    通过删除字母匹配到字典里最长单词
    网址：https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/
    
    解题思路：
    1. 使用自定义key按长度降序、字典序升序排序
    2. 检查每个单词是否可以通过删除s中的字符得到
    3. 返回第一个满足条件的单词
    
    时间复杂度：O(n * x * log n)，其中x是单词平均长度
    空间复杂度：O(n)
    """
    
    def findLongestWord(self, s: str, dictionary: List[str]) -> str:
        # 自定义排序：先按长度降序，再按字典序升序
        dictionary.sort(key=lambda x: (-len(x), x))
        
        for word in dictionary:
            if self.is_subsequence(word, s):
                return word
        return ""
    
    def is_subsequence(self, word: str, s: str) -> bool:
        i, j = 0, 0
        while i < len(word) and j < len(s):
            if word[i] == s[j]:
                i += 1
            j += 1
        return i == len(word)

class ReorderLogFiles:
    """
    LeetCode 937. Reorder Data in Log Files
    重新排列日志文件
    网址：https://leetcode.com/problems/reorder-data-in-log-files/
    
    解题思路：
    1. 区分字母日志和数字日志
    2. 字母日志按内容字典序排序，内容相同按标识符排序
    3. 数字日志保持原有顺序
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    
    def reorderLogFiles(self, logs: List[str]) -> List[str]:
        def get_key(log):
            # 分割标识符和内容
            identifier, content = log.split(" ", 1)
            
            # 判断是否是数字日志
            if content[0].isdigit():
                return (1, )  # 数字日志排在后面
            else:
                return (0, content, identifier)  # 字母日志按内容和标识符排序
        
        return sorted(logs, key=get_key)

class RankTransformArray:
    """
    LeetCode 1331. Rank Transform of an Array
    数组序号转换
    网址：https://leetcode.com/problems/rank-transform-of-an-array/
    
    解题思路：
    1. 创建排序后的唯一值列表
    2. 使用字典存储值到排名的映射
    3. 根据映射转换原数组
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    
    def arrayRankTransform(self, arr: List[int]) -> List[int]:
        if not arr:
            return []
        
        # 创建排序后的唯一值列表
        sorted_unique = sorted(set(arr))
        
        # 创建值到排名的映射
        rank_map = {}
        for rank, num in enumerate(sorted_unique, 1):
            rank_map[num] = rank
        
        # 转换原数组
        return [rank_map[num] for num in arr]

class RankTeamsByVotes:
    """
    LeetCode 1366. Rank Teams by Votes
    通过投票对团队排名
    网址：https://leetcode.com/problems/rank-teams-by-votes/
    
    解题思路：
    1. 统计每个团队在每个位置的得票数
    2. 使用自定义key按得票规则排序团队
    3. 按得票规则排序团队
    
    时间复杂度：O(n * m + n log n)，其中m是投票位置数
    空间复杂度：O(n²)
    """
    
    def rankTeams(self, votes: List[str]) -> str:
        if not votes:
            return ""
        if len(votes) == 1:
            return votes[0]
        
        n = len(votes[0])
        # 统计每个团队在每个位置的得票数
        vote_count = {}
        for vote in votes:
            for i, team in enumerate(vote):
                if team not in vote_count:
                    vote_count[team] = [0] * n
                vote_count[team][i] += 1
        
        # 自定义排序key
        def sort_key(team):
            return (-vote_count[team][0], -vote_count[team][1], 
                    -vote_count[team][2], team)  # 负号实现降序
        
        # 按自定义规则排序团队
        sorted_teams = sorted(vote_count.keys(), key=sort_key)
        
        return "".join(sorted_teams)

class RearrangeWords:
    """
    LeetCode 1451. Rearrange Words in a Sentence
    重新排列句子中的单词
    网址：https://leetcode.com/problems/rearrange-words-in-a-text/
    
    解题思路：
    1. 按单词长度排序
    2. 长度相同保持原有顺序（稳定排序）
    3. 首字母大写处理
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    
    def arrangeWords(self, text: str) -> str:
        if not text:
            return text
        
        # 分割单词并转换为小写
        words = text.lower().split()
        
        # 按长度排序（稳定排序）
        words.sort(key=len)
        
        # 首字母大写处理
        if words:
            words[0] = words[0][0].upper() + words[0][1:]
        
        return " ".join(words)

class MinimumDifference:
    """
    LeetCode 1509. Minimum Difference Between Largest and Smallest Value in Three Moves
    三次操作后最大值与最小值的最小差
    网址：https://leetcode.com/problems/minimum-difference-between-largest-and-smallest-value-in-three-moves/
    
    解题思路：
    1. 排序数组
    2. 分析移除三个元素后的可能情况
    3. 计算最小差值
    
    时间复杂度：O(n log n)
    空间复杂度：O(1)
    """
    
    def minDifference(self, nums: List[int]) -> int:
        n = len(nums)
        if n <= 4:
            return 0
        
        nums.sort()
        
        # 四种可能的情况
        min_diff = sys.maxsize
        min_diff = min(min_diff, nums[-1] - nums[3])      # 移除最小的3个
        min_diff = min(min_diff, nums[-2] - nums[2])      # 移除最小的2个和最大的1个
        min_diff = min(min_diff, nums[-3] - nums[1])      # 移除最小的1个和最大的2个
        min_diff = min(min_diff, nums[-4] - nums[0])      # 移除最大的3个
        
        return min_diff

class MaximumCoins:
    """
    LeetCode 1561. Maximum Number of Coins You Can Get
    你可以获得的最大硬币数目
    网址：https://leetcode.com/problems/maximum-number-of-coins-you-can-get/
    
    解题思路：
    1. 排序数组
    2. 每次取第二大的堆（贪心策略）
    3. 计算你能获得的总硬币数
    
    时间复杂度：O(n log n)
    空间复杂度：O(1)
    """
    
    def maxCoins(self, piles: List[int]) -> int:
        piles.sort()
        n = len(piles)
        result = 0
        
        # 每次取第二大的堆
        for i in range(n - 2, n // 3 - 1, -2):
            result += piles[i]
        
        return result

class SortByFrequency:
    """
    LeetCode 1636. Sort Array by Increasing Frequency
    按照频率将数组升序排序
    网址：https://leetcode.com/problems/sort-array-by-increasing-frequency/
    
    解题思路：
    1. 统计每个数字的频率
    2. 使用自定义key按频率升序排序
    3. 频率相同按数值降序排序
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    
    def frequencySort(self, nums: List[int]) -> List[int]:
        from collections import Counter
        
        # 统计频率
        freq = Counter(nums)
        
        # 自定义排序key
        def sort_key(num):
            return (freq[num], -num)  # 频率升序，数值降序
        
        return sorted(nums, key=sort_key)

class MaximumUnits:
    """
    LeetCode 1710. Maximum Units on a Truck
    卡车上的最大单元数
    网址：https://leetcode.com/problems/maximum-units-on-a-truck/
    
    解题思路：
    1. 按单位单元数（每个箱子的单元数）降序排序
    2. 贪心选择单位单元数最大的箱子
    3. 计算最大单元数
    
    时间复杂度：O(n log n)
    空间复杂度：O(1)
    """
    
    def maximumUnits(self, boxTypes: List[List[int]], truckSize: int) -> int:
        # 按单位单元数降序排序
        boxTypes.sort(key=lambda x: -x[1])
        
        result = 0
        remaining = truckSize
        
        for boxes, units in boxTypes:
            if remaining <= 0:
                break
            
            boxes_to_take = min(remaining, boxes)
            result += boxes_to_take * units
            remaining -= boxes_to_take
        
        return result

class SortSentence:
    """
    LeetCode 1859. Sorting the Sentence
    将句子排序
    网址：https://leetcode.com/problems/sorting-the-sentence/
    
    解题思路：
    1. 提取单词末尾的数字
    2. 按数字排序单词
    3. 重新组合成句子
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    
    def sortSentence(self, s: str) -> str:
        words = s.split()
        
        # 自定义排序key：按末尾数字排序
        words.sort(key=lambda word: int(word[-1]))
        
        # 构建结果句子，去除末尾数字
        return " ".join(word[:-1] for word in words)

# 测试代码
if __name__ == "__main__":
    # 测试LeetCode 524
    solution = LongestWordInDictionary()
    s = "abpcplea"
    dictionary = ["ale", "apple", "monkey", "plea"]
    longest_word = solution.findLongestWord(s, dictionary)
    print("LeetCode 524 Result:", longest_word)
    
    # 测试LeetCode 937
    solution = ReorderLogFiles()
    logs = ["dig1 8 1 5 1", "let1 art can", "dig2 3 6", "let2 own kit dig", "let3 art zero"]
    reordered_logs = solution.reorderLogFiles(logs)
    print("LeetCode 937 Result:", reordered_logs)
    
    # 测试LeetCode 1331
    solution = RankTransformArray()
    arr = [40, 10, 20, 30]
    ranks = solution.arrayRankTransform(arr)
    print("LeetCode 1331 Result:", ranks)
    
    # 测试LeetCode 1710
    solution = MaximumUnits()
    boxTypes = [[1,3], [2,2], [3,1]]
    max_units = solution.maximumUnits(boxTypes, 4)
    print("LeetCode 1710 Result:", max_units)