"""
高级哈希算法问题与解析
包含一系列使用哈希技术的高级算法问题，以及详细的解析和实现
"""

from typing import List, Dict, Tuple
import collections
import random
import heapq
import bisect

class AdvancedHashProblems:
    @staticmethod
    def longest_duplicate_substring(s: str) -> str:
        """
        LeetCode 1044. Longest Duplicate Substring (最长重复子串)
        题目来源: https://leetcode.com/problems/longest-duplicate-substring/
        
        题目描述:
        给你一个字符串 s，考虑其所有重复子串：即 s 的（连续）子串，在 s 中出现至少两次。
        返回任何具有最长长度的重复子串。如果 s 不含重复子串，那么答案为 ""。
        
        示例:
        输入：s = "banana"
        输出："ana"
        
        输入：s = "abcd"
        输出：""
        
        算法思路:
        1. 使用二分查找确定可能的最长重复子串长度
        2. 对于每个长度，使用Rabin-Karp算法检查是否存在重复子串
        3. 如果存在，尝试更长的长度；否则，尝试更短的长度
        
        时间复杂度: O(n log n)，其中n是字符串长度
        空间复杂度: O(n)
        """
        def rabin_karp(m):
            """检查长度为m的重复子串"""
            if m == 0:
                return ""
                
            # 计算hash值
            h = 0
            for i in range(m):
                h = (h * 26 + (ord(s[i]) - ord('a'))) % MOD
                
            # 存储hash值和对应的起始位置
            seen = {h: 0}
            # 计算最高位的权重
            aL = pow(26, m, MOD)
            
            for start in range(1, n - m + 1):
                # 使用滚动哈希计算下一个子串的哈希值
                h = (h * 26 - (ord(s[start - 1]) - ord('a')) * aL + (ord(s[start + m - 1]) - ord('a'))) % MOD
                
                if h in seen:
                    # 哈希冲突检查：比较实际子串
                    candidate = s[seen[h]:seen[h] + m]
                    curr = s[start:start + m]
                    if candidate == curr:
                        return candidate
                seen[h] = start
                
            return ""
            
        n = len(s)
        MOD = 10**9 + 7
        
        # 二分查找最长重复子串长度
        left, right = 0, n
        result = ""
        
        while left < right:
            mid = (left + right) // 2
            candidate = rabin_karp(mid)
            if candidate:
                # 找到重复子串，尝试更长的长度
                left = mid + 1
                result = candidate
            else:
                # 没有找到，尝试更短的长度
                right = mid
                
        return result

    @staticmethod
    def substring_with_concatenation_of_all_words(s: str, words: List[str]) -> List[int]:
        """
        LeetCode 30. Substring with Concatenation of All Words (串联所有单词的子串)
        题目来源: https://leetcode.com/problems/substring-with-concatenation-of-all-words/
        
        题目描述:
        给定一个字符串 s 和一个字符串数组 words。words 中所有字符串长度相同。
        s 中的 串联子串 是指一个包含 words 中所有字符串以任意顺序排列连接起来的子串。
        例如，如果 words = ["ab","cd","ef"]，那么 "abcdef"、"abefcd"、"cdabef"、"cdefab"、"efabcd" 和 "efcdab" 都是串联子串。
        "acdbef" 不是串联子串，因为它不是任何 words 排列的连接。
        返回所有串联子串在 s 中的开始索引。你可以以任意顺序返回答案。
        
        示例:
        输入：s = "barfoothefoobarman", words = ["foo","bar"]
        输出：[0,9]
        解释：串联子串为 "barfoo" 和 "foobar"，它们的起始位置分别为 0 和 9。
        
        算法思路:
        1. 使用滑动窗口和哈希表计数
        2. 每次移动一个单词长度，检查窗口内的单词是否符合要求
        3. 由于所有单词长度相同，可以从不同偏移量开始滑动窗口
        
        时间复杂度: O(n * m)，其中n是字符串s的长度，m是words中单词的总长度
        空间复杂度: O(m)
        """
        if not s or not words:
            return []
            
        word_len = len(words[0])
        word_count = len(words)
        total_len = word_len * word_count
        result = []
        
        if len(s) < total_len:
            return result
            
        # 统计words中每个单词出现的次数
        word_freq = collections.Counter(words)
        
        # 考虑不同的起始偏移
        for i in range(word_len):
            left = i
            right = i
            current_count = collections.Counter()
            
            while right + word_len <= len(s):
                word = s[right:right + word_len]
                right += word_len
                
                if word not in word_freq:
                    # 遇到不在words中的单词，重置窗口
                    current_count.clear()
                    left = right
                else:
                    current_count[word] += 1
                    
                    # 如果当前单词出现次数超过需要的次数，移动左指针
                    while current_count[word] > word_freq[word]:
                        current_count[s[left:left + word_len]] -= 1
                        left += word_len
                        
                    # 检查是否找到了所有单词的串联
                    if right - left == total_len:
                        result.append(left)
                        # 移动左指针，继续寻找下一个匹配
                        current_count[s[left:left + word_len]] -= 1
                        left += word_len
                        
        return result

    @staticmethod
    def longest_consecutive_sequence(nums: List[int]) -> int:
        """
        LeetCode 128. Longest Consecutive Sequence (最长连续序列)
        题目来源: https://leetcode.com/problems/longest-consecutive-sequence/
        
        题目描述:
        给定一个未排序的整数数组 nums，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
        请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
        
        示例:
        输入：nums = [100,4,200,1,3,2]
        输出：4
        解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
        
        输入：nums = [0,3,7,2,5,8,4,6,0,1]
        输出：9
        
        算法思路:
        1. 使用哈希集合存储所有数字
        2. 对于每个数字，如果它是序列的起点（即它前面的数字不存在），则尝试找出以它开始的最长序列
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        if not nums:
            return 0
            
        num_set = set(nums)
        max_length = 0
        
        for num in num_set:
            # 只检查序列的起点（前一个数字不在集合中）
            if num - 1 not in num_set:
                current_num = num
                current_length = 1
                
                # 检查连续的数字是否存在
                while current_num + 1 in num_set:
                    current_num += 1
                    current_length += 1
                    
                max_length = max(max_length, current_length)
                
        return max_length

    @staticmethod
    def four_sum(nums: List[int], target: int) -> List[List[int]]:
        """
        LeetCode 18. 4Sum (四数之和)
        题目来源: https://leetcode.com/problems/4sum/
        
        题目描述:
        给你一个由 n 个整数组成的数组 nums，和一个目标值 target。请你找出并返回满足下述全部条件且不重复的四元组 [nums[a], nums[b], nums[c], nums[d]]：
        0 <= a, b, c, d < n
        a, b, c, d 互不相同
        nums[a] + nums[b] + nums[c] + nums[d] == target
        你可以按任意顺序返回答案。
        
        示例:
        输入：nums = [1,0,-1,0,-2,2], target = 0
        输出：[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
        
        算法思路:
        1. 使用哈希表存储所有可能的两数之和
        2. 排序数组，然后使用两层循环固定前两个数
        3. 对于剩下的两个数，使用哈希表查找是否存在满足条件的组合
        
        时间复杂度: O(n^2)
        空间复杂度: O(n^2)
        """
        if len(nums) < 4:
            return []
            
        nums.sort()
        n = len(nums)
        result = []
        
        for i in range(n - 3):
            # 跳过重复元素
            if i > 0 and nums[i] == nums[i - 1]:
                continue
                
            # 如果当前四数之和的最小值已经大于target，则后面的组合都会大于target
            if nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target:
                break
                
            # 如果当前值加上三个最大值小于target，则当前值不可能是答案的一部分
            if nums[i] + nums[n - 3] + nums[n - 2] + nums[n - 1] < target:
                continue
                
            for j in range(i + 1, n - 2):
                # 跳过重复元素
                if j > i + 1 and nums[j] == nums[j - 1]:
                    continue
                    
                # 如果当前四数之和的最小值已经大于target，则后面的组合都会大于target
                if nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target:
                    break
                    
                # 如果当前两个值加上两个最大值小于target，则当前组合不可能是答案的一部分
                if nums[i] + nums[j] + nums[n - 2] + nums[n - 1] < target:
                    continue
                    
                # 使用双指针查找剩余两个数
                left, right = j + 1, n - 1
                
                while left < right:
                    curr_sum = nums[i] + nums[j] + nums[left] + nums[right]
                    
                    if curr_sum < target:
                        left += 1
                    elif curr_sum > target:
                        right -= 1
                    else:
                        result.append([nums[i], nums[j], nums[left], nums[right]])
                        
                        # 跳过重复元素
                        while left < right and nums[left] == nums[left + 1]:
                            left += 1
                        while left < right and nums[right] == nums[right - 1]:
                            right -= 1
                            
                        left += 1
                        right -= 1
                        
        return result

    @staticmethod
    def palindrome_pairs(words: List[str]) -> List[List[int]]:
        """
        LeetCode 336. Palindrome Pairs (回文对)
        题目来源: https://leetcode.com/problems/palindrome-pairs/
        
        题目描述:
        给定一组互不相同的单词，找出所有不同的索引对(i, j)，使得列表中的两个单词，words[i] + words[j]，可拼接成回文串。
        
        示例:
        输入：words = ["abcd","dcba","lls","s","sssll"]
        输出：[[0,1],[1,0],[3,2],[2,4]]
        解释：可拼接成的回文串为 ["dcbaabcd","abcddcba","slls","llssssll"]
        
        算法思路:
        1. 使用哈希表存储每个单词及其索引
        2. 对于每个单词，检查其所有前缀和后缀是否可以与其他单词组成回文串
        3. 特殊处理空字符串的情况
        
        时间复杂度: O(n * k^2)，其中n是单词数量，k是单词的平均长度
        空间复杂度: O(n * k)
        """
        def is_palindrome(word, start, end):
            """检查word[start:end+1]是否为回文"""
            while start < end:
                if word[start] != word[end]:
                    return False
                start += 1
                end -= 1
            return True
        
        result = []
        word_dict = {word: i for i, word in enumerate(words)}
        
        for i, word in enumerate(words):
            for j in range(len(word) + 1):
                # 检查前缀是否可以组成回文
                prefix = word[:j]
                suffix = word[j:]
                
                # 如果前缀是回文，查找是否存在与后缀相反的单词
                if is_palindrome(prefix, 0, len(prefix) - 1):
                    reverse_suffix = suffix[::-1]
                    if reverse_suffix in word_dict and word_dict[reverse_suffix] != i:
                        result.append([word_dict[reverse_suffix], i])
                
                # 如果后缀是回文且前缀非空（避免重复），查找是否存在与前缀相反的单词
                if j > 0 and is_palindrome(suffix, 0, len(suffix) - 1):
                    reverse_prefix = prefix[::-1]
                    if reverse_prefix in word_dict and word_dict[reverse_prefix] != i:
                        result.append([i, word_dict[reverse_prefix]])
        
        return result

    @staticmethod
    def count_of_smaller_numbers_after_self(nums: List[int]) -> List[int]:
        """
        LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
        题目来源: https://leetcode.com/problems/count-of-smaller-numbers-after-self/
        
        题目描述:
        给你一个整数数组 nums，按要求返回一个新数组 counts。
        数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
        
        示例:
        输入：nums = [5,2,6,1]
        输出：[2,1,1,0]
        解释：
        5 的右侧有 2 个更小的元素 (2 和 1)
        2 的右侧有 1 个更小的元素 (1)
        6 的右侧有 1 个更小的元素 (1)
        1 的右侧有 0 个更小的元素
        
        算法思路:
        1. 使用有序数组（或平衡二叉搜索树）从右到左处理数组
        2. 对于每个元素，查找有序数组中小于该元素的元素个数
        3. 将当前元素插入有序数组
        
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        def binary_search(arr, target):
            """查找target应该插入的位置（即小于target的元素个数）"""
            left, right = 0, len(arr)
            while left < right:
                mid = (left + right) // 2
                if arr[mid] < target:
                    left = mid + 1
                else:
                    right = mid
            return left
        
        result = []
        sorted_nums = []
        
        # 从右向左处理数组
        for num in reversed(nums):
            # 查找小于当前元素的元素个数
            pos = binary_search(sorted_nums, num)
            result.append(pos)
            
            # 将当前元素插入有序数组
            sorted_nums.insert(pos, num)
        
        # 由于是从右向左处理的，需要反转结果
        return result[::-1]

    @staticmethod
    def maximum_frequency_stack(operations: List[List[str]]) -> List[int]:
        """
        LeetCode 895. Maximum Frequency Stack (最大频率栈)
        题目来源: https://leetcode.com/problems/maximum-frequency-stack/
        
        题目描述:
        设计一个类似堆栈的数据结构，将元素推入堆栈，并从堆栈中弹出出现频率最高的元素。
        
        实现 FreqStack 类:
        - FreqStack() 构造一个空的堆栈。
        - void push(int val) 将一个整数 val 压入栈顶。
        - int pop() 删除并返回堆栈中出现频率最高的元素。
          如果出现频率最高的元素不只一个，则移除并返回最接近栈顶的元素。
        
        示例:
        输入：
        ["FreqStack", "push", "push", "push", "push", "push", "push", "pop", "pop", "pop", "pop"]
        [[], [5], [7], [5], [7], [4], [5], [], [], [], []]
        输出：
        [null, null, null, null, null, null, null, 5, 7, 5, 4]
        
        算法思路:
        1. 使用哈希表记录每个元素的频率
        2. 使用另一个哈希表，以频率为键，值为该频率下的元素栈
        3. 维护一个最大频率变量
        
        时间复杂度: O(1) 每次操作
        空间复杂度: O(n)
        """
        class FreqStack:
            def __init__(self):
                self.freq = {}  # 元素 -> 频率
                self.group = {}  # 频率 -> 元素栈
                self.max_freq = 0
            
            def push(self, val: int) -> None:
                # 更新元素频率
                f = self.freq.get(val, 0) + 1
                self.freq[val] = f
                
                # 更新最大频率
                self.max_freq = max(self.max_freq, f)
                
                # 将元素添加到对应频率的栈中
                if f not in self.group:
                    self.group[f] = []
                self.group[f].append(val)
            
            def pop(self) -> int:
                # 从最大频率栈中弹出元素
                val = self.group[self.max_freq].pop()
                
                # 更新元素频率
                self.freq[val] -= 1
                
                # 如果最大频率栈为空，减小最大频率
                if not self.group[self.max_freq]:
                    self.max_freq -= 1
                
                return val
        
        freq_stack = FreqStack()
        results = []
        
        for op in operations:
            if op[0] == "push":
                freq_stack.push(int(op[1]))
                results.append(None)
            elif op[0] == "pop":
                results.append(freq_stack.pop())
        
        return results

    @staticmethod
    def design_twitter(operations: List[List[str]]) -> List[List[int]]:
        """
        LeetCode 355. Design Twitter (设计推特)
        题目来源: https://leetcode.com/problems/design-twitter/
        
        题目描述:
        设计一个简化版的推特(Twitter)，可以让用户实现发送推文，关注/取消关注其他用户，能够看见关注人（包括自己）的最近十条推文。
        
        实现 Twitter 类:
        - Twitter() 初始化推特对象
        - void postTweet(int userId, int tweetId) 用户 userId 发送一条新推文 tweetId
        - List<Integer> getNewsFeed(int userId) 检索当前用户最新的十条推文。
          每条推文都必须是由此用户关注的人或者是用户自己发出的。推文必须按照时间顺序由最近的开始排序。
        - void follow(int followerId, int followeeId) ID 为 followerId 的用户开始关注 ID 为 followeeId 的用户
        - void unfollow(int followerId, int followeeId) ID 为 followerId 的用户不再关注 ID 为 followeeId 的用户
        
        算法思路:
        1. 使用哈希表存储用户关注列表
        2. 使用哈希表存储用户发布的推文（带时间戳）
        3. 获取新闻源时，合并所有关注用户的推文，使用优先队列获取最近的10条
        
        时间复杂度:
        - postTweet: O(1)
        - getNewsFeed: O(N log N)，其中N是关注的用户数
        - follow/unfollow: O(1)
        
        空间复杂度: O(U + T)，其中U是用户数，T是推文数
        """
        class Twitter:
            def __init__(self):
                self.followees = collections.defaultdict(set)  # 用户 -> 关注列表
                self.tweets = collections.defaultdict(list)    # 用户 -> 推文列表
                self.timestamp = 0
            
            def postTweet(self, userId: int, tweetId: int) -> None:
                # 发布推文，记录时间戳
                self.tweets[userId].append((self.timestamp, tweetId))
                self.timestamp += 1
            
            def getNewsFeed(self, userId: int) -> List[int]:
                # 获取自己和关注者的所有推文
                all_tweets = []
                
                # 添加自己的推文
                for time, tweet_id in self.tweets[userId]:
                    all_tweets.append((time, tweet_id))
                
                # 添加关注者的推文
                for followee in self.followees[userId]:
                    for time, tweet_id in self.tweets[followee]:
                        all_tweets.append((time, tweet_id))
                
                # 按时间戳排序，获取最近的10条
                all_tweets.sort(reverse=True)
                return [tweet_id for _, tweet_id in all_tweets[:10]]
            
            def follow(self, followerId: int, followeeId: int) -> None:
                # 不能关注自己
                if followerId != followeeId:
                    self.followees[followerId].add(followeeId)
            
            def unfollow(self, followerId: int, followeeId: int) -> None:
                # 取消关注
                if followeeId in self.followees[followerId]:
                    self.followees[followerId].remove(followeeId)
        
        twitter = Twitter()
        results = []
        
        for op in operations:
            if op[0] == "postTweet":
                twitter.postTweet(int(op[1]), int(op[2]))
                results.append(None)
            elif op[0] == "getNewsFeed":
                results.append(twitter.getNewsFeed(int(op[1])))
            elif op[0] == "follow":
                twitter.follow(int(op[1]), int(op[2]))
                results.append(None)
            elif op[0] == "unfollow":
                twitter.unfollow(int(op[1]), int(op[2]))
                results.append(None)
        
        return results

    @staticmethod
    def lfu_cache(operations: List[List[str]]) -> List[int]:
        """
        LeetCode 460. LFU Cache (LFU缓存)
        题目来源: https://leetcode.com/problems/lfu-cache/
        
        题目描述:
        请你为 最不经常使用（LFU）缓存算法设计并实现数据结构。
        
        实现 LFUCache 类:
        - LFUCache(int capacity) - 用数据结构的容量 capacity 初始化对象
        - int get(int key) - 如果键 key 存在于缓存中，则获取键的值，否则返回 -1。
        - void put(int key, int value) - 如果键 key 已存在，则变更其值；如果键不存在，请插入键值对。
          当缓存达到其容量 capacity 时，则应该在插入新项之前，移除最不经常使用的项。
          在此问题中，当存在平局（即两个或更多个键具有相同使用频率）时，应该去除 最近最久未使用 的键。
        
        为了确定最不常使用的键，可以为缓存中的每个键维护一个 使用计数器。使用计数最小的键是最久未使用的键。
        当一个键首次插入到缓存中时，它的使用计数器被设置为 1 。对缓存中的键执行 get 或 put 操作，使用计数器的值将会递增。
        
        算法思路:
        1. 使用哈希表存储键值对和频率
        2. 使用另一个哈希表，以频率为键，值为该频率下的元素（使用双向链表或有序集合）
        3. 维护一个最小频率变量
        
        时间复杂度: O(1) 每次操作
        空间复杂度: O(capacity)
        """
        class LFUCache:
            def __init__(self, capacity: int):
                self.capacity = capacity
                self.key_to_val = {}  # 键 -> 值
                self.key_to_freq = {}  # 键 -> 频率
                self.freq_to_keys = collections.defaultdict(collections.OrderedDict)  # 频率 -> 有序的键集合
                self.min_freq = 0  # 当前最小频率
            
            def get(self, key: int) -> int:
                if key not in self.key_to_val:
                    return -1
                
                # 更新频率
                self._update_freq(key)
                return self.key_to_val[key]
            
            def put(self, key: int, value: int) -> None:
                if self.capacity == 0:
                    return
                
                # 如果键已存在，更新值和频率
                if key in self.key_to_val:
                    self.key_to_val[key] = value
                    self._update_freq(key)
                    return
                
                # 如果缓存已满，移除最不经常使用的项
                if len(self.key_to_val) >= self.capacity:
                    # 获取最小频率的第一个键（最近最久未使用）
                    lfu_key, _ = self.freq_to_keys[self.min_freq].popitem(last=False)
                    
                    # 如果该频率下没有键了，删除该频率
                    if not self.freq_to_keys[self.min_freq]:
                        del self.freq_to_keys[self.min_freq]
                    
                    # 删除键值对
                    del self.key_to_val[lfu_key]
                    del self.key_to_freq[lfu_key]
                
                # 插入新键值对
                self.key_to_val[key] = value
                self.key_to_freq[key] = 1
                self.freq_to_keys[1][key] = None
                self.min_freq = 1  # 新插入的键频率为1
            
            def _update_freq(self, key: int) -> None:
                # 获取当前频率
                freq = self.key_to_freq[key]
                
                # 从当前频率列表中移除
                del self.freq_to_keys[freq][key]
                
                # 如果当前频率列表为空且是最小频率，增加最小频率
                if not self.freq_to_keys[freq] and freq == self.min_freq:
                    self.min_freq += 1
                
                # 更新频率
                self.key_to_freq[key] = freq + 1
                
                # 添加到新频率列表
                self.freq_to_keys[freq + 1][key] = None
        
        lfu_cache = None
        results = []
        
        for op in operations:
            if op[0] == "LFUCache":
                lfu_cache = LFUCache(int(op[1]))
                results.append(None)
            elif op[0] == "put":
                lfu_cache.put(int(op[1]), int(op[2]))
                results.append(None)
            elif op[0] == "get":
                results.append(lfu_cache.get(int(op[1])))
        
        return results

    @staticmethod
    def subarray_with_distinct_elements(nums: List[int], k: int) -> int:
        """
        LeetCode 992. Subarrays with K Different Integers (K个不同整数的子数组)
        题目来源: https://leetcode.com/problems/subarrays-with-k-different-integers/
        
        题目描述:
        给定一个正整数数组 nums 和一个整数 k，返回 nums 中恰好包含 k 个不同整数的子数组的个数。
        
        示例:
        输入：nums = [1,2,1,2,3], k = 2
        输出：7
        解释：恰好包含 2 个不同整数的子数组：[1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2]
        
        算法思路:
        1. 使用滑动窗口和哈希表计数
        2. 计算恰好包含k个不同整数的子数组个数 = 最多包含k个不同整数的子数组个数 - 最多包含(k-1)个不同整数的子数组个数
        
        时间复杂度: O(n)
        空间复杂度: O(k)
        """
        def at_most_k_distinct(nums, k):
            """计算最多包含k个不同整数的子数组个数"""
            count = collections.Counter()
            left = 0
            result = 0
            
            for right in range(len(nums)):
                # 如果当前数字不在窗口中，增加不同整数计数
                count[nums[right]] += 1
                
                # 当不同整数个数超过k时，移动左指针
                while len(count) > k:
                    count[nums[left]] -= 1
                    if count[nums[left]] == 0:
                        del count[nums[left]]
                    left += 1
                
                # 以right结尾的、最多包含k个不同整数的子数组个数为right-left+1
                result += right - left + 1
            
            return result
        
        # 恰好包含k个不同整数的子数组个数 = 最多包含k个 - 最多包含(k-1)个
        return at_most_k_distinct(nums, k) - at_most_k_distinct(nums, k - 1)

    @staticmethod
    def find_duplicate_file_in_system(paths: List[str]) -> List[List[str]]:
        """
        LeetCode 609. Find Duplicate File in System (在系统中查找重复文件)
        题目来源: https://leetcode.com/problems/find-duplicate-file-in-system/
        
        题目描述:
        给定一个目录信息列表，包括目录路径，以及该目录中的所有包含内容的文件，
        您需要找到文件系统中的所有重复文件组的路径。一组重复的文件至少包括二个具有完全相同内容的文件。
        
        输入列表中的单个目录信息字符串的格式如下：
        "root/d1/d2/.../dm f1.txt(f1_content) f2.txt(f2_content) ... fn.txt(fn_content)"
        
        这意味着有 n 个文件（f1.txt, f2.txt ... fn.txt）在目录 root/d1/d2/.../dm 下。
        f1.txt 的内容被表示为 f1_content，f2.txt 的内容被表示为 f2_content，以此类推。
        
        示例:
        输入：paths = ["root/a 1.txt(abcd) 2.txt(efgh)","root/c 3.txt(abcd)","root/c/d 4.txt(efgh)"]
        输出：[["root/a/1.txt","root/c/3.txt"],["root/a/2.txt","root/c/d/4.txt"]]
        
        算法思路:
        1. 解析每个目录信息字符串，提取文件路径和内容
        2. 使用哈希表以文件内容为键，文件路径列表为值
        3. 返回所有包含至少两个文件的内容组
        
        时间复杂度: O(n)，其中n是所有文件路径和内容的总长度
        空间复杂度: O(n)
        """
        content_to_paths = collections.defaultdict(list)
        
        for path in paths:
            parts = path.split()
            directory = parts[0]
            
            for file_info in parts[1:]:
                # 解析文件名和内容
                left_paren = file_info.find('(')
                right_paren = file_info.find(')')
                
                filename = file_info[:left_paren]
                content = file_info[left_paren+1:right_paren]
                
                # 构建完整文件路径
                full_path = directory + '/' + filename
                
                # 将文件路径添加到对应内容的列表中
                content_to_paths[content].append(full_path)
        
        # 返回所有包含至少两个文件的内容组
        return [paths for paths in content_to_paths.values() if len(paths) > 1]

    @staticmethod
    def brick_wall(wall: List[List[int]]) -> int:
        """
        LeetCode 554. Brick Wall (砖墙)
        题目来源: https://leetcode.com/problems/brick-wall/
        
        题目描述:
        你的面前有一堵矩形的、由 n 行砖块组成的砖墙。这些砖块高度相同（也就是一个单位高）但是宽度不同。
        每一行砖块的宽度之和相等。
        
        你现在要画一条 自顶向下 的、穿过 最少 砖块的垂线。如果你画的线只是从砖块的边缘经过，就不算穿过这块砖。
        你不能沿着墙的两个垂直边缘之一画线，这样显然是没有穿过一块砖的。
        
        给你一个二维数组 wall，该数组包含这堵墙的相关信息。其中，wall[i] 是一个代表从左至右每块砖的宽度的数组。
        你需要找出怎样画才能使这条线 穿过的砖块数量最少，并且返回 穿过的砖块数量。
        
        示例:
        输入：wall = [[1,2,2,1],[3,1,2],[1,3,2],[2,4],[3,1,2],[1,3,1,1]]
        输出：2
        
        算法思路:
        1. 计算每一行砖块的边缘位置（不包括最右边缘）
        2. 使用哈希表统计每个边缘位置出现的次数
        3. 找出出现次数最多的边缘位置，穿过的砖块数量 = 总行数 - 最大出现次数
        
        时间复杂度: O(n)，其中n是所有砖块的数量
        空间复杂度: O(m)，其中m是不同的边缘位置数量
        """
        if not wall:
            return 0
        
        edge_count = collections.defaultdict(int)
        
        for row in wall:
            edge_pos = 0
            # 不考虑最右边缘
            for brick in row[:-1]:
                edge_pos += brick
                edge_count[edge_pos] += 1
        
        # 如果没有边缘（每行只有一块砖），则必须穿过所有行
        if not edge_count:
            return len(wall)
        
        # 穿过的砖块数量 = 总行数 - 最大出现次数
        return len(wall) - max(edge_count.values())

    @staticmethod
    def minimum_window_substring(s: str, t: str) -> str:
        """
        LeetCode 76. Minimum Window Substring (最小覆盖子串)
        题目来源: https://leetcode.com/problems/minimum-window-substring/
        
        题目描述:
        给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。
        如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
        
        注意：
        - 对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量。
        - 如果 s 中存在这样的子串，我们保证它是唯一的答案。
        
        示例:
        输入：s = "ADOBECODEBANC", t = "ABC"
        输出："BANC"
        解释：最小覆盖子串 "BANC" 包含来自字符串 t 的 'A'、'B' 和 'C'。
        
        算法思路:
        1. 使用滑动窗口和两个哈希表
        2. 一个哈希表记录目标字符串t中每个字符的出现次数
        3. 另一个哈希表记录当前窗口中每个字符的出现次数
        4. 使用一个计数器记录已经匹配的字符数量
        
        时间复杂度: O(|s| + |t|)
        空间复杂度: O(|s| + |t|)
        """
        if not s or not t:
            return ""
        
        # 统计t中每个字符的出现次数
        target_count = collections.Counter(t)
        required_chars = len(target_count)
        
        # 初始化窗口
        window_count = collections.defaultdict(int)
        formed = 0  # 已匹配的字符种类数
        
        # 初始化结果
        min_len = float('inf')
        result = ""
        
        left = 0
        for right in range(len(s)):
            # 扩大窗口
            char = s[right]
            window_count[char] += 1
            
            # 检查是否匹配了一个字符
            if char in target_count and window_count[char] == target_count[char]:
                formed += 1
            
            # 尝试缩小窗口
            while left <= right and formed == required_chars:
                char = s[left]
                
                # 更新结果
                if right - left + 1 < min_len:
                    min_len = right - left + 1
                    result = s[left:right+1]
                
                # 缩小窗口
                window_count[char] -= 1
                if char in target_count and window_count[char] < target_count[char]:
                    formed -= 1
                
                left += 1
        
        return result if min_len != float('inf') else ""

if __name__ == "__main__":
    # 测试最长重复子串
    print("=== LeetCode 1044. Longest Duplicate Substring ===")
    print(f"longest_duplicate_substring('banana'): {AdvancedHashProblems.longest_duplicate_substring('banana')}")
    print(f"longest_duplicate_substring('abcd'): {AdvancedHashProblems.longest_duplicate_substring('abcd')}")
    
    # 测试串联所有单词的子串
    print("\n=== LeetCode 30. Substring with Concatenation of All Words ===")
    print(f"substring_with_concatenation_of_all_words('barfoothefoobarman', ['foo', 'bar']): {AdvancedHashProblems.substring_with_concatenation_of_all_words('barfoothefoobarman', ['foo', 'bar'])}")
    
    # 测试最长连续序列
    print("\n=== LeetCode 128. Longest Consecutive Sequence ===")
    print(f"longest_consecutive_sequence([100,4,200,1,3,2]): {AdvancedHashProblems.longest_consecutive_sequence([100,4,200,1,3,2])}")
    print(f"longest_consecutive_sequence([0,3,7,2,5,8,4,6,0,1]): {AdvancedHashProblems.longest_consecutive_sequence([0,3,7,2,5,8,4,6,0,1])}")
    
    # 测试四数之和
    print("\n=== LeetCode 18. 4Sum ===")
    print(f"four_sum([1,0,-1,0,-2,2], 0): {AdvancedHashProblems.four_sum([1,0,-1,0,-2,2], 0)}")
    
    # 测试回文对
    print("\n=== LeetCode 336. Palindrome Pairs ===")
    print(f"palindrome_pairs(['abcd', 'dcba', 'lls', 's', 'sssll']): {AdvancedHashProblems.palindrome_pairs(['abcd', 'dcba', 'lls', 's', 'sssll'])}")
    
    # 测试计算右侧小于当前元素的个数
    print("\n=== LeetCode 315. Count of Smaller Numbers After Self ===")
    print(f"count_of_smaller_numbers_after_self([5,2,6,1]): {AdvancedHashProblems.count_of_smaller_numbers_after_self([5,2,6,1])}")
    
    # 测试最小窗口子串
    print("\n=== LeetCode 76. Minimum Window Substring ===")
    print(f"minimum_window_substring('ADOBECODEBANC', 'ABC'): {AdvancedHashProblems.minimum_window_substring('ADOBECODEBANC', 'ABC')}")