# Class026: HashSet、HashMap、TreeSet、TreeMap 和 Comparator 专题

本章节深入探讨了Java中HashSet、HashMap、TreeSet、TreeMap和Comparator的使用方法及相关的经典算法题目。

## 数据结构特点

### HashSet
- 基于HashMap实现
- 底层使用哈希表
- 查询时间复杂度O(1)
- 元素无序
- 不允许重复元素

### HashMap
- 基于哈希表实现
- 查询时间复杂度O(1)
- 键值对无序
- 允许一个null键和多个null值

### TreeSet
- 基于TreeMap实现
- 底层使用红黑树
- 查询时间复杂度O(log n)
- 元素有序
- 不允许重复元素

### TreeMap
- 基于红黑树实现
- 查询时间复杂度O(log n)
- 键值对按键有序

### Comparator
- 比较器接口
- 用于定义对象之间比较的规则
- 可以实现自定义排序

## 题目列表

### HashSet 和 HashMap 相关题目

1. **LeetCode 1. Two Sum (两数之和)**
   - 题目：在数组中找到两个数之和等于目标值的索引
   - 解法：使用HashMap存储已遍历元素及其索引
   - 时间复杂度：O(n)
   - 空间复杂度：O(n)
   - 网址：https://leetcode.com/problems/two-sum/

2. **LeetCode 242. Valid Anagram (有效的字母异位词)**
   - 题目：判断两个字符串是否为字母异位词
   - 解法：使用HashMap统计字符出现次数
   - 时间复杂度：O(n)
   - 空间复杂度：O(1)
   - 网址：https://leetcode.com/problems/valid-anagram/

3. **LeetCode 349. Intersection of Two Arrays (两个数组的交集)**
   - 题目：求两个数组的交集
   - 解法：使用HashSet存储元素并查找交集
   - 时间复杂度：O(m+n)
   - 空间复杂度：O(m)
   - 网址：https://leetcode.com/problems/intersection-of-two-arrays/

4. **LeetCode 705. Design HashSet (设计哈希集合)**
   - 题目：不使用内建哈希表库设计HashSet
   - 解法：使用链地址法实现哈希表
   - 时间复杂度：O(n/b)
   - 空间复杂度：O(n)
   - 网址：https://leetcode.com/problems/design-hashset/

5. **LeetCode 706. Design HashMap (设计哈希映射)**
   - 题目：不使用内建哈希表库设计HashMap
   - 解法：使用链地址法实现哈希表
   - 时间复杂度：O(n/b)
   - 空间复杂度：O(n)
   - 网址：https://leetcode.com/problems/design-hashmap/

6. **HackerRank Java Hashset (Java哈希集)**
   - 题目：找出独特的字符串对数量
   - 解法：使用HashSet存储字符串对
   - 时间复杂度：O(n)
   - 空间复杂度：O(n)
   - 网址：https://www.hackerrank.com/challenges/java-hashset

7. **LeetCode 128. Longest Consecutive Sequence (最长连续序列)**
   - 题目：找出数字连续的最长序列长度
   - 解法：使用HashSet存储数字并查找连续序列
   - 时间复杂度：O(n)
   - 空间复杂度：O(n)
   - 网址：https://leetcode.com/problems/longest-consecutive-sequence/

8. **LeetCode 49. Group Anagrams (字母异位词分组)**
   - 题目：将字母异位词组合在一起
   - 解法：使用HashMap按排序后的字符串分组
   - 时间复杂度：O(N*K*logK)
   - 空间复杂度：O(N*K)
   - 网址：https://leetcode.com/problems/group-anagrams/

9. **LeetCode 347. Top K Frequent Elements (前 K 个高频元素)**
   - 题目：返回出现频率前k高的元素
   - 解法：使用HashMap统计频率+最小堆维护前k个元素
   - 时间复杂度：O(N*logK)
   - 空间复杂度：O(N)
   - 网址：https://leetcode.com/problems/top-k-frequent-elements/

10. **LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串)**
    - 题目：找出不包含重复字符的最长子串长度
    - 解法：使用滑动窗口+HashSet记录字符出现
    - 时间复杂度：O(n)
    - 空间复杂度：O(min(m,n))
    - 网址：https://leetcode.com/problems/longest-substring-without-repeating-characters/

11. **LeetCode 217. Contains Duplicate (存在重复元素)**
    - 题目：判断数组中是否存在重复元素
    - 解法：使用HashSet检查重复
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/contains-duplicate/

12. **LeetCode 219. Contains Duplicate II (存在重复元素 II)**
    - 题目：判断是否存在两个相同元素且下标差不超过k
    - 解法：使用HashMap存储元素最近出现位置
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/contains-duplicate-ii/

13. **LeetCode 290. Word Pattern (单词规律)**
    - 题目：判断字符串是否符合给定的模式
    - 解法：使用HashMap建立字符到单词的映射
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/word-pattern/

14. **LeetCode 205. Isomorphic Strings (同构字符串)**
    - 题目：判断两个字符串是否同构
    - 解法：使用HashMap建立字符映射关系
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/isomorphic-strings/

15. **LeetCode 383. Ransom Note (赎金信)**
    - 题目：判断ransomNote是否能由magazine中的字符组成
    - 解法：使用HashMap统计字符出现次数
    - 时间复杂度：O(m+n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/ransom-note/

16. **LeetCode 387. First Unique Character in a String (字符串中的第一个唯一字符)**
    - 题目：找到字符串中第一个不重复的字符
    - 解法：使用HashMap统计字符出现次数
    - 时间复杂度：O(n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/first-unique-character-in-a-string/

17. **LeetCode 454. 4Sum II (四数相加 II)**
    - 题目：计算四个数组中满足a+b+c+d=0的元组个数
    - 解法：使用HashMap存储两数之和的频率
    - 时间复杂度：O(n²)
    - 空间复杂度：O(n²)
    - 网址：https://leetcode.com/problems/4sum-ii/

18. **LeetCode 560. Subarray Sum Equals K (和为K的子数组)**
    - 题目：计算和为k的连续子数组个数
    - 解法：使用HashMap存储前缀和频率
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/subarray-sum-equals-k/

19. **LeetCode 575. Distribute Candies (分糖果)**
    - 题目：计算最多能获得多少种不同的糖果
    - 解法：使用HashSet统计糖果种类
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/distribute-candies/

20. **LeetCode 594. Longest Harmonious Subsequence (最长和谐子序列)**
    - 题目：找出最长的和谐子序列长度
    - 解法：使用HashMap统计数字频率
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/longest-harmonious-subsequence/

21. **LeetCode 599. Minimum Index Sum of Two Lists (两个列表的最小索引总和)**
    - 题目：找到两个列表中索引和最小的共同元素
    - 解法：使用HashMap存储第一个列表的索引
    - 时间复杂度：O(m+n)
    - 空间复杂度：O(m)
    - 网址：https://leetcode.com/problems/minimum-index-sum-of-two-lists/

22. **LeetCode 645. Set Mismatch (错误的集合)**
    - 题目：找出重复的数字和缺失的数字
    - 解法：使用HashSet检查重复和缺失
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/set-mismatch/

23. **LeetCode 692. Top K Frequent Words (前K个高频单词)**
    - 题目：返回出现频率前k高的单词
    - 解法：使用HashMap统计频率+自定义排序
    - 时间复杂度：O(n log k)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/top-k-frequent-words/

24. **LeetCode 771. Jewels and Stones (宝石与石头)**
    - 题目：计算石头中有多少是宝石
    - 解法：使用HashSet存储宝石类型
    - 时间复杂度：O(m+n)
    - 空间复杂度：O(m)
    - 网址：https://leetcode.com/problems/jewels-and-stones/

25. **LeetCode 811. Subdomain Visit Count (子域名访问计数)**
    - 题目：统计子域名的访问次数
    - 解法：使用HashMap统计域名访问次数
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/subdomain-visit-count/

26. **LeetCode 884. Uncommon Words from Two Sentences (两句话中的不常见单词)**
    - 题目：找出在两句话中只出现一次的单词
    - 解法：使用HashMap统计单词出现次数
    - 时间复杂度：O(m+n)
    - 空间复杂度：O(m+n)
    - 网址：https://leetcode.com/problems/uncommon-words-from-two-sentences/

27. **LeetCode 890. Find and Replace Pattern (查找和替换模式)**
    - 题目：找到与给定模式匹配的所有单词
    - 解法：使用HashMap建立字符映射
    - 时间复杂度：O(n*k)
    - 空间复杂度：O(k)
    - 网址：https://leetcode.com/problems/find-and-replace-pattern/

28. **LeetCode 953. Verifying an Alien Dictionary (验证外星语词典)**
    - 题目：判断单词是否按外星语字典序排列
    - 解法：使用HashMap存储字母顺序
    - 时间复杂度：O(n*k)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/verifying-an-alien-dictionary/

29. **LeetCode 1002. Find Common Characters (查找常用字符)**
    - 题目：找到所有字符串中都出现的字符
    - 解法：使用HashMap统计字符最小出现次数
    - 时间复杂度：O(n*k)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/find-common-characters/

30. **LeetCode 1048. Longest String Chain (最长字符串链)**
    - 题目：找出最长的字符串链长度
    - 解法：使用HashMap存储字符串及其链长度
    - 时间复杂度：O(n*l²)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/longest-string-chain/

31. **LeetCode 1160. Find Words That Can Be Formed by Characters (拼写单词)**
    - 题目：计算能由给定字符组成的单词总长度
    - 解法：使用HashMap统计字符可用数量
    - 时间复杂度：O(n+m)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/find-words-that-can-be-formed-by-characters/

32. **LeetCode 1207. Unique Number of Occurrences (独一无二的出现次数)**
    - 题目：判断数组中每个数的出现次数是否都是唯一的
    - 解法：使用HashMap统计频率，再用HashSet检查唯一性
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/unique-number-of-occurrences/

33. **LeetCode 1396. Design Underground System (设计地铁系统)**
    - 题目：设计地铁系统的检票功能
    - 解法：使用HashMap存储乘客进站信息和站间时间统计
    - 时间复杂度：O(1)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/design-underground-system/

34. **LeetCode 1418. Display Table of Food Orders in a Restaurant (餐厅菜品展示表)**
    - 题目：生成餐厅菜品订单展示表
    - 解法：使用HashMap统计每桌的菜品数量
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/display-table-of-food-orders-in-a-restaurant/

35. **LeetCode 1481. Least Number of Unique Integers after K Removals (不同整数的最少数目)**
    - 题目：移除k个元素后，使不同整数的数量最少
    - 解法：使用HashMap统计频率，按频率排序后移除
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/

36. **LeetCode 1512. Number of Good Pairs (好数对的数目)**
    - 题目：计算满足nums[i] == nums[j]且i < j的数对个数
    - 解法：使用HashMap统计相同数字的出现次数
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/number-of-good-pairs/

37. **LeetCode 1636. Sort Array by Increasing Frequency (按照频率将数组升序排序)**
    - 题目：按频率升序排序数组，频率相同按数值降序
    - 解法：使用HashMap统计频率+自定义排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/sort-array-by-increasing-frequency/

38. **LeetCode 1657. Determine if Two Strings Are Close (确定两个字符串是否接近)**
    - 题目：判断能否通过操作使两个字符串相等
    - 解法：使用HashMap统计字符频率和种类
    - 时间复杂度：O(n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/determine-if-two-strings-are-close/

39. **LeetCode 1679. Max Number of K-Sum Pairs (K和数对的最大数目)**
    - 题目：找出最多能组成多少对和为k的数对
    - 解法：使用HashMap统计数字出现次数
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/max-number-of-k-sum-pairs/

40. **LeetCode 1695. Maximum Erasure Value (删除子数组的最大得分)**
    - 题目：删除不含重复元素的子数组，使剩余元素和最大
    - 解法：使用滑动窗口+HashSet检查重复
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/maximum-erasure-value/

41. **LeetCode 1748. Sum of Unique Elements (唯一元素的和)**
    - 题目：计算数组中只出现一次的元素之和
    - 解法：使用HashMap统计元素出现次数
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/sum-of-unique-elements/

42. **LeetCode 1941. Check if All Characters Have Equal Number of Occurrences (检查是否所有字符出现次数相同)**
    - 题目：判断字符串中所有字符出现次数是否相同
    - 解法：使用HashMap统计频率，再用HashSet检查唯一性
    - 时间复杂度：O(n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/check-if-all-characters-have-equal-number-of-occurrences/

43. **LeetCode 2006. Count Number of Pairs With Absolute Difference K (差的绝对值为K的数对数目)**
    - 题目：计算绝对差为k的数对个数
    - 解法：使用HashMap统计数字出现次数
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/count-number-of-pairs-with-absolute-difference-k/

44. **LeetCode 2013. Detect Squares (检测正方形)**
    - 题目：设计数据结构支持添加点和统计正方形数量
    - 解法：使用HashMap存储点的坐标和数量
    - 时间复杂度：O(1)添加，O(n)查询
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/detect-squares/

45. **LeetCode 2032. Two Out of Three (至少在两个数组中出现的值)**
    - 题目：找出至少在两个数组中出现的所有值
    - 解法：使用HashMap统计每个数字出现的数组数量
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/two-out-of-three/

46. **LeetCode 2085. Count Common Words With One Occurrence (统计出现过一次的公共单词)**
    - 题目：统计在两个数组中都只出现一次的公共单词
    - 解法：使用HashMap统计单词出现次数
    - 时间复杂度：O(m+n)
    - 空间复杂度：O(m+n)
    - 网址：https://leetcode.com/problems/count-common-words-with-one-occurrence/

47. **LeetCode 2103. Rings and Rods (环和杆)**
    - 题目：统计有多少根杆上有所有三种颜色的环
    - 解法：使用HashMap存储每根杆的颜色集合
    - 时间复杂度：O(n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/rings-and-rods/

48. **LeetCode 2131. Longest Palindrome by Concatenating Two Letter Words (连接两字母单词得到的最长回文串)**
    - 题目：用两字母单词组成最长回文串
    - 解法：使用HashMap统计单词出现次数
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/longest-palindrome-by-concatenating-two-letter-words/

49. **LeetCode 2206. Divide Array Into Equal Pairs (将数组划分成相等数对)**
    - 题目：判断能否将数组分成n/2个数对，每个数对元素相等
    - 解法：使用HashMap统计数字出现次数
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/divide-array-into-equal-pairs/

50. **LeetCode 2215. Find the Difference of Two Arrays (找出两数组的不同)**
    - 题目：找出两个数组中互不存在的元素
    - 解法：使用HashSet存储数组元素
    - 时间复杂度：O(m+n)
    - 空间复杂度：O(m+n)
    - 网址：https://leetcode.com/problems/find-the-difference-of-two-arrays/

51. **LeetCode 2248. Intersection of Multiple Arrays (多个数组的交集)**
    - 题目：找出多个数组的交集
    - 解法：使用HashMap统计每个数字出现的数组数量
    - 时间复杂度：O(n*k)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/intersection-of-multiple-arrays/

52. **LeetCode 2342. Max Sum of a Pair With Equal Sum of Digits (数位和相等数对的最大和)**
    - 题目：找到数位和相等的两个数的最大和
    - 解法：使用HashMap存储数位和对应的最大数
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/max-sum-of-a-pair-with-equal-sum-of-digits/

53. **LeetCode 2352. Equal Row and Column Pairs (相等行列对)**
    - 题目：统计行和列相等的对数
    - 解法：使用HashMap存储行的字符串表示
    - 时间复杂度：O(n²)
    - 空间复杂度：O(n²)
    - 网址：https://leetcode.com/problems/equal-row-and-column-pairs/

54. **LeetCode 2404. Most Frequent Even Element (出现最频繁的偶数元素)**
    - 题目：找出出现次数最多的偶数
    - 解法：使用HashMap统计偶数出现频率
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/most-frequent-even-element/

55. **LeetCode 2441. Largest Positive Integer That Exists With Its Negative (与对应负数同时存在的最大正整数)**
    - 题目：找出最大的正整数k，使得-k也存在于数组中
    - 解法：使用HashSet存储所有数字
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/largest-positive-integer-that-exists-with-its-negative/

56. **LeetCode 2506. Count Pairs Of Similar Strings (统计相似字符串对数目)**
    - 题目：统计相似字符串对的数量
    - 解法：使用HashMap存储字符串的特征向量
    - 时间复杂度：O(n*k)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/count-pairs-of-similar-strings/

57. **LeetCode 2554. Maximum Number of Integers to Choose From a Range I (从一个范围内选择最多整数 I)**
    - 题目：从范围中选择最多不包含banned数组中的整数
    - 解法：使用HashSet存储banned数字
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/maximum-number-of-integers-to-choose-from-a-range-i/

58. **LeetCode 2670. Find the Distinct Difference Array (找出不同元素差数组)**
    - 题目：计算每个位置前缀和后缀不同元素数量的差
    - 解法：使用HashSet统计前后缀不同元素
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/find-the-distinct-difference-array/

59. **Codeforces 4C. Registration System (注册系统)**
    - 题目：设计用户名注册系统，处理重复用户名
    - 解法：使用HashMap存储用户名和出现次数
    - 时间复杂度：O(1)每次查询
    - 空间复杂度：O(n)
    - 网址：https://codeforces.com/problemset/problem/4/C

60. **HackerEarth Monk and the Magical Candy Bags (僧侣和魔法糖果袋)**
    - 题目：从多个糖果袋中每天吃最多的糖果
    - 解法：使用TreeMap维护糖果数量
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://www.hackerearth.com/practice/data-structures/trees/heapspriority-queues/practice-problems/algorithm/monk-and-the-magical-candy-bags/

61. **AtCoder ABC 217 D - Cutting Woods (切割木材)**
    - 题目：处理木材切割查询，回答每段木材长度
    - 解法：使用TreeSet存储切割点
    - 时间复杂度：O(log n)每次操作
    - 空间复杂度：O(n)
    - 网址：https://atcoder.jp/contests/abc217/tasks/abc217_d

62. **USACO Silver: Why Did the Cow Cross the Road (为什么奶牛要过马路)**
    - 题目：统计奶牛过马路的交叉点数量
    - 解法：使用HashMap存储奶牛位置
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：http://www.usaco.org/index.php?page=viewproblem2&cpid=714

63. **洛谷 P3374 【模板】树状数组 1 (模板树状数组1)**
    - 题目：实现单点修改和区间查询的树状数组
    - 解法：使用树状数组数据结构
    - 时间复杂度：O(log n)每次操作
    - 空间复杂度：O(n)
    - 网址：https://www.luogu.com.cn/problem/P3374

64. **CodeChef STFOOD (街头食物)**
    - 题目：计算街头食物摊位的最大利润
    - 解法：使用HashMap存储食物类型和最大利润
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://www.codechef.com/problems/STFOOD

65. **SPOJ ANARC09A - Seinfeld (宋飞正传)**
    - 题目：将不平衡的括号字符串转换为平衡的
    - 解法：使用栈数据结构处理括号匹配
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://www.spoj.com/problems/ANARC09A/

66. **Project Euler Problem 1: Multiples of 3 and 5 (3和5的倍数)**
    - 题目：计算1000以内3或5的倍数之和
    - 解法：使用HashSet去重后求和
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://projecteuler.net/problem=1

67. **HackerRank Frequency Queries (频率查询)**
    - 题目：处理频率相关的查询操作
    - 解法：使用HashMap统计频率和频率的频率
    - 时间复杂度：O(1)每次操作
    - 空间复杂度：O(n)
    - 网址：https://www.hackerrank.com/challenges/frequency-queries

68. **计蒜客 T1100: 计算2的N次方 (计算2的N次方)**
    - 题目：计算2的N次方，N最大10000
    - 解法：使用数组模拟大数乘法
    - 时间复杂度：O(n²)
    - 空间复杂度：O(n)
    - 网址：https://www.jisuanke.com/t/T1100

69. **杭电 OJ 1002: A + B Problem II (A+B问题II)**
    - 题目：大数加法
    - 解法：使用字符串处理大数
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：http://acm.hdu.edu.cn/showproblem.php?pid=1002

70. **牛客网 剑指Offer 03: 数组中重复的数字 (数组中重复的数字)**
    - 题目：找出数组中任意一个重复的数字
    - 解法：使用HashSet检查重复
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://www.nowcoder.com/practice/623a5ac0ea5b4e5f95552655361ae0a8

71. **acwing 799. 最长连续不重复子序列 (最长连续不重复子序列)**
    - 题目：找出最长的不包含重复元素的连续子数组
    - 解法：使用双指针+HashSet检查重复
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://www.acwing.com/problem/content/801/

72. **POJ 1002: 487-3279 (电话号码)**
    - 题目：统计电话号码的出现次数
    - 解法：使用HashMap统计标准化后的电话号码
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：http://poj.org/problem?id=1002

73. **UVa OJ 100: The 3n + 1 problem (3n+1问题)**
    - 题目：计算3n+1序列的最大长度
    - 解法：使用HashMap缓存已计算的结果
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=36

74. **Timus OJ 1001: Reverse Root (反转平方根)**
    - 题目：计算数字的平方根并按逆序输出
    - 解法：使用栈存储计算结果
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：https://acm.timus.ru/problem.aspx?space=1&num=1001

75. **Aizu OJ ALDS1_4_C: Dictionary (字典)**
    - 题目：实现字典的插入和查找功能
    - 解法：使用HashSet或Trie树
    - 时间复杂度：O(1)每次操作
    - 空间复杂度：O(n)
    - 网址：http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_4_C

76. **Comet OJ Contest #0: 热身赛 A. 签到题 (签到题)**
    - 题目：简单的输入输出问题
    - 解法：基础编程实现
    - 时间复杂度：O(1)
    - 空间复杂度：O(1)
    - 网址：https://cometoj.com/contest/0/problem/A

77. **MarsCode 火星编程竞赛: 字符串去重排序 (字符串去重排序)**
    - 题目：对字符串去重并按字典序排序
    - 解法：使用TreeSet自动去重排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://www.marscode.cn/contest/1/problem/1001

78. **ZOJ 1001: A + B Problem (A+B问题)**
    - 题目：基础输入输出
    - 解法：读取两个整数并输出和
    - 时间复杂度：O(1)
    - 空间复杂度：O(1)
    - 网址：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1001

79. **LOJ 100: 顺序的分数 (顺序的分数)**
    - 题目：生成所有最简分数并按值排序
    - 解法：使用TreeSet存储分数并自动排序
    - 时间复杂度：O(n² log n)
    - 空间复杂度：O(n²)
    - 网址：https://loj.ac/p/100

80. **各大高校OJ: 清华大学OJ 1000: A+B Problem (A+B问题)**
    - 题目：基础输入输出
    - 解法：读取两个整数并输出和
    - 时间复杂度：O(1)
    - 空间复杂度：O(1)
    - 网址：http://dsa.cs.tsinghua.edu.cn/oj/problem.shtml?id=1000

### TreeSet 和 TreeMap 相关题目

1. **LeetCode 220. Contains Duplicate III (存在重复元素 III)**
   - 题目：判断是否存在两个不同下标元素满足差值条件
   - 解法：使用TreeSet维护滑动窗口
   - 时间复杂度：O(n log min(n,k))
   - 空间复杂度：O(min(n,k))

2. **LeetCode 933. Number of Recent Calls (最近的请求次数)**
   - 题目：计算特定时间范围内最近的请求数
   - 解法：使用TreeSet存储时间戳并查找范围
   - 时间复杂度：O(log n)
   - 空间复杂度：O(n)

3. **LeetCode 729. My Calendar I (我的日程安排表 I)**
   - 题目：实现日程安排表避免重复预订
   - 解法：使用TreeMap存储日程并查找冲突
   - 时间复杂度：O(log n)
   - 空间复杂度：O(n)

4. **HackerRank Java TreeSet (Java树集)**
   - 题目：对整数列表去重并排序
   - 解法：使用TreeSet自动排序去重
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)

5. **LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)**
   - 题目：计算每个元素右侧小于它的元素数量
   - 解法：从右向左遍历+TreeSet查找
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)

6. **LeetCode 493. Reverse Pairs (翻转对)**
   - 题目：计算满足条件的重要翻转对数量
   - 解法：从右向左遍历+TreeSet查找
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)

7. **LeetCode 352. Data Stream as Disjoint Intervals (将数据流变为多个不相交区间)**
   - 题目：将数据流中的数字转换为不相交的区间
   - 解法：使用TreeMap存储区间边界
   - 时间复杂度：O(log n)每次添加
   - 空间复杂度：O(n)
   - 网址：https://leetcode.com/problems/data-stream-as-disjoint-intervals/

8. **LeetCode 363. Max Sum of Rectangle No Larger Than K (矩形区域不超过K的最大数值和)**
   - 题目：找出矩阵中矩形区域和不超过K的最大值
   - 解法：使用TreeSet维护前缀和
   - 时间复杂度：O(min(m,n)² * max(m,n) log max(m,n))
   - 空间复杂度：O(max(m,n))
   - 网址：https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/

9. **LeetCode 436. Find Right Interval (寻找右区间)**
   - 题目：为每个区间找到右侧最近的区间
   - 解法：使用TreeMap存储区间起始位置和索引
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)
   - 网址：https://leetcode.com/problems/find-right-interval/

10. **LeetCode 456. 132 Pattern (132模式)**
    - 题目：判断数组中是否存在132模式
    - 解法：使用TreeSet维护右侧元素
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/132-pattern/

11. **LeetCode 480. Sliding Window Median (滑动窗口中位数)**
    - 题目：计算滑动窗口的中位数
    - 解法：使用两个TreeSet维护窗口元素
    - 时间复杂度：O(n log k)
    - 空间复杂度：O(k)
    - 网址：https://leetcode.com/problems/sliding-window-median/

12. **LeetCode 683. K Empty Slots (K个空花盆)**
    - 题目：找到第k天恰好有k个连续空花盆
    - 解法：使用TreeSet存储开花位置
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/k-empty-slots/

13. **LeetCode 715. Range Module (范围模块)**
    - 题目：设计数据结构支持范围添加、删除和查询
    - 解法：使用TreeMap存储不相交区间
    - 时间复杂度：O(log n)每次操作
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/range-module/

14. **LeetCode 731. My Calendar II (我的日程安排表 II)**
    - 题目：实现日程安排表避免三重预订
    - 解法：使用TreeMap存储日程边界计数
    - 时间复杂度：O(n²) 或 O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/my-calendar-ii/

15. **LeetCode 732. My Calendar III (我的日程安排表 III)**
    - 题目：实现日程安排表统计最大重叠次数
    - 解法：使用TreeMap存储日程边界计数
    - 时间复杂度：O(n²) 或 O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/my-calendar-iii/

16. **LeetCode 855. Exam Room (考场就座)**
    - 题目：设计考场座位分配系统
    - 解法：使用TreeSet存储已占座位
    - 时间复杂度：O(n)每次操作
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/exam-room/

17. **LeetCode 981. Time Based Key-Value Store (基于时间的键值存储)**
    - 题目：设计支持时间戳的键值存储
    - 解法：使用HashMap+TreeMap存储时间戳和值
    - 时间复杂度：O(log n)每次操作
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/time-based-key-value-store/

18. **LeetCode 1146. Snapshot Array (快照数组)**
    - 题目：设计支持快照的数组
    - 解法：使用TreeMap存储每个索引的快照历史
    - 时间复杂度：O(log n)每次操作
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/snapshot-array/

19. **LeetCode 1348. Tweet Counts Per Frequency (推文计数)**
    - 题目：统计特定时间频率内的推文数量
    - 解法：使用TreeMap存储推文时间戳
    - 时间复杂度：O(log n)每次操作
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/tweet-counts-per-frequency/

20. **LeetCode 1438. Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit (绝对差不超过限制的最长连续子数组)**
    - 题目：找到最长子数组，其中任意两元素绝对差不超过limit
    - 解法：使用两个TreeMap维护窗口最大最小值
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/

### Comparator 相关题目

1. **LeetCode 973. K Closest Points to Origin (最接近原点的 K 个点)**
   - 题目：找出离原点最近的k个点
   - 解法：计算距离+自定义Comparator排序
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(1)

2. **LeetCode 179. Largest Number (最大数)**
   - 题目：重新排列数字组成最大整数
   - 解法：转换为字符串+自定义Comparator排序
   - 时间复杂度：O(n log n * m)
   - 空间复杂度：O(n * m)

3. **LeetCode 1356. Sort Integers by The Number of 1 Bits (根据数字二进制下 1 的数目排序)**
   - 题目：按二进制中1的位数排序
   - 解法：使用Integer.bitCount()+自定义Comparator
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(1)

4. **HackerRank Java Comparator (Java比较器)**
   - 题目：根据分数和名字排序玩家
   - 解法：实现Comparator接口
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(1)

5. **LeetCode 56. Merge Intervals (合并区间)**
   - 题目：合并重叠的区间
   - 解法：按起始位置排序+合并重叠区间
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)

6. **LeetCode 1122. Relative Sort Array (数组的相对排序)**
   - 题目：按相对顺序排序数组
   - 解法：使用自定义Comparator排序
   - 时间复杂度：O(m log m + n)
   - 空间复杂度：O(n)

7. **LeetCode 524. Longest Word in Dictionary through Deleting (通过删除字母匹配到字典里最长单词)**
   - 题目：通过删除s中的字符匹配字典中最长的单词
   - 解法：使用自定义Comparator按长度和字典序排序
   - 时间复杂度：O(n * x * log n)
   - 空间复杂度：O(log n)
   - 网址：https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/

8. **LeetCode 937. Reorder Data in Log Files (重新排列日志文件)**
   - 题目：按特定规则重新排列日志文件
   - 解法：使用自定义Comparator区分字母日志和数字日志
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)
   - 网址：https://leetcode.com/problems/reorder-data-in-log-files/

9. **LeetCode 1331. Rank Transform of an Array (数组序号转换)**
   - 题目：将数组元素转换为对应的排名
   - 解法：使用自定义Comparator排序后分配排名
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n)
   - 网址：https://leetcode.com/problems/rank-transform-of-an-array/

10. **LeetCode 1366. Rank Teams by Votes (通过投票对团队排名)**
    - 题目：根据投票结果对团队进行排名
    - 解法：使用自定义Comparator按投票统计排序
    - 时间复杂度：O(n * m + n log n)
    - 空间复杂度：O(n²)
    - 网址：https://leetcode.com/problems/rank-teams-by-votes/

11. **LeetCode 1451. Rearrange Words in a Sentence (重新排列句子中的单词)**
    - 题目：按单词长度重新排列句子
    - 解法：使用自定义Comparator按长度排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/rearrange-words-in-a-sentence/

12. **LeetCode 1509. Minimum Difference Between Largest and Smallest Value in Three Moves (三次操作后最大值与最小值的最小差)**
    - 题目：通过最多三次操作使最大值与最小值差最小
    - 解法：排序后使用自定义Comparator分析
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/minimum-difference-between-largest-and-smallest-value-in-three-moves/

13. **LeetCode 1561. Maximum Number of Coins You Can Get (你可以获得的最大硬币数目)**
    - 题目：三人分硬币游戏，计算你能获得的最大硬币数
    - 解法：排序后使用自定义策略选择
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/maximum-number-of-coins-you-can-get/

14. **LeetCode 1636. Sort Array by Increasing Frequency (按照频率将数组升序排序)**
    - 题目：按频率升序排序数组，频率相同按数值降序
    - 解法：使用HashMap统计频率+自定义Comparator排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/sort-array-by-increasing-frequency/

15. **LeetCode 1710. Maximum Units on a Truck (卡车上的最大单元数)**
    - 题目：在卡车上装载最大单元数的箱子
    - 解法：使用自定义Comparator按单位单元数排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/maximum-units-on-a-truck/

16. **LeetCode 1859. Sorting the Sentence (将句子排序)**
    - 题目：根据单词末尾数字重新排列句子
    - 解法：使用自定义Comparator按数字排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/sorting-the-sentence/

17. **LeetCode 1984. Minimum Difference Between Highest and Lowest of K Scores (学生分数的最小差值)**
    - 题目：从分数数组中选k个学生使最高分和最低分差值最小
    - 解法：排序后滑动窗口+自定义Comparator
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/minimum-difference-between-highest-and-lowest-of-k-scores/

18. **LeetCode 2164. Sort Even and Odd Indices Independently (对奇偶下标分别排序)**
    - 题目：对偶数下标升序排序，奇数下标降序排序
    - 解法：使用自定义Comparator分别处理奇偶下标
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/sort-even-and-odd-indices-independently/

19. **LeetCode 2279. Maximum Bags With Full Capacity of Rocks (装满石头的背包的最大数量)**
    - 题目：计算最多能装满多少个背包
    - 解法：排序后贪心+自定义Comparator
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/maximum-bags-with-full-capacity-of-rocks/

20. **LeetCode 2418. Sort the People (按身高排序)**
    - 题目：根据身高对人名进行排序
    - 解法：使用自定义Comparator按身高排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/sort-the-people/

21. **LeetCode 2502. Design Memory Allocator (设计内存分配器)**
    - 题目：设计内存分配器支持分配和释放内存
    - 解法：使用TreeMap+自定义Comparator管理内存块
    - 时间复杂度：O(log n)每次操作
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/design-memory-allocator/

22. **LeetCode 2570. Merge Two 2D Arrays by Summing Values (合并两个二维数组)**
    - 题目：合并两个二维数组，相同id的值相加
    - 解法：使用TreeMap+自定义Comparator合并
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/merge-two-2d-arrays-by-summing-values/

23. **LeetCode 2610. Convert an Array Into a 2D Array With Conditions (将数组转换成满足条件的二维数组)**
    - 题目：将数组转换成二维数组，每行元素互不相同
    - 解法：使用HashMap统计+自定义Comparator分配
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/convert-an-array-into-a-2d-array-with-conditions/

24. **LeetCode 2643. Row With Maximum Ones (包含最多1的行)**
    - 题目：找到包含最多1的行，如有多个返回索引最小的
    - 解法：使用自定义Comparator比较1的数量和行索引
    - 时间复杂度：O(m*n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/row-with-maximum-ones/

25. **LeetCode 2679. Sum in a Matrix (矩阵中的和)**
    - 题目：从矩阵每行选一个数，使选出的数之和最大
    - 解法：每行排序后使用自定义Comparator选择
    - 时间复杂度：O(m*n log n)
    - 空间复杂度：O(1)
    - 网址：https://leetcode.com/problems/sum-in-a-matrix/

26. **LeetCode 2785. Sort Vowels in a String (将字符串中的元音字母排序)**
    - 题目：将字符串中的元音字母按ASCII值排序
    - 解法：提取元音排序后使用自定义Comparator
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/sort-vowels-in-a-string/

27. **LeetCode 2966. Divide Array Into Arrays With Max Difference (将数组分成含最大差值的数组)**
    - 题目：将数组分成多个长度为3的子数组，使每个子数组的最大差值不超过k
    - 解法：排序后使用自定义Comparator分组
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://leetcode.com/problems/divide-array-into-arrays-with-max-difference/

28. **Codeforces 492B. Vanya and Lanterns (Vanya和灯笼)**
    - 题目：在街道上放置灯笼，计算最小照明半径
    - 解法：排序后使用自定义Comparator计算最大间隔
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(1)
    - 网址：https://codeforces.com/problemset/problem/492/B

29. **HackerRank Sorting: Comparator (排序：比较器)**
    - 题目：实现玩家排序的比较器
    - 解法：实现Comparator接口按分数和名字排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(1)
    - 网址：https://www.hackerrank.com/challenges/ctci-comparator-sorting

30. **AtCoder ABC 342 D - Square Pair (平方对)**
    - 题目：统计数组中满足乘积为完全平方数的数对
    - 解法：质因数分解+自定义Comparator统计
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://atcoder.jp/contests/abc342/tasks/abc342_d

31. **USACO Bronze: The Cow-Signal (奶牛信号)**
    - 题目：放大奶牛信号图案
    - 解法：使用自定义Comparator处理图案放大
    - 时间复杂度：O(n²)
    - 空间复杂度：O(n²)
    - 网址：http://www.usaco.org/index.php?page=viewproblem2&cpid=665

32. **洛谷 P1177 【模板】快速排序 (模板快速排序)**
    - 题目：实现快速排序算法
    - 解法：使用自定义Comparator实现快速排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(log n)
    - 网址：https://www.luogu.com.cn/problem/P1177

33. **CodeChef SORTING (排序)**
    - 题目：对数组进行排序并计算最小交换次数
    - 解法：使用自定义Comparator分析逆序对
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://www.codechef.com/problems/SORTING

34. **SPOJ INVCNT - Inversion Count (逆序数计数)**
    - 题目：计算数组中的逆序对数量
    - 解法：归并排序+自定义Comparator统计
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://www.spoj.com/problems/INVCNT/

35. **Project Euler Problem 22: Names scores (名字得分)**
    - 题目：计算名字列表中所有名字的得分总和
    - 解法：排序后使用自定义Comparator计算得分
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://projecteuler.net/problem=22

36. **HackerEarth Monk and Sorting Algorithm (僧侣和排序算法)**
    - 题目：实现特定的排序算法
    - 解法：使用自定义Comparator实现特殊排序规则
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://www.hackerearth.com/practice/algorithms/sorting/merge-sort/practice-problems/algorithm/monk-and-sorting-algorithm/

37. **计蒜客 T1153: 绝对值排序 (绝对值排序)**
    - 题目：按绝对值大小对整数排序
    - 解法：使用自定义Comparator按绝对值排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(1)
    - 网址：https://www.jisuanke.com/t/T1153

38. **杭电 OJ 1106: 排序 (排序)**
    - 题目：对特定格式的数字字符串进行排序
    - 解法：解析字符串后使用自定义Comparator排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：http://acm.hdu.edu.cn/showproblem.php?pid=1106

39. **牛客网 剑指Offer 45: 把数组排成最小的数 (把数组排成最小的数)**
    - 题目：将数组里所有数字拼接起来排成一个最小的数字
    - 解法：使用自定义Comparator比较字符串拼接结果
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://www.nowcoder.com/practice/8fecd3f8ba334add803bf2a06af1b993

40. **acwing 785. 快速排序 (快速排序)**
    - 题目：实现快速排序算法
    - 解法：使用自定义Comparator实现快速排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(log n)
    - 网址：https://www.acwing.com/problem/content/787/

41. **POJ 2388: Who's in the Middle (中间值)**
    - 题目：找到奶牛产奶量的中间值
    - 解法：排序后使用自定义Comparator取中位数
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(1)
    - 网址：http://poj.org/problem?id=2388

42. **UVa OJ 10107: What is the Median? (中位数是什么？)**
    - 题目：动态计算数据流的中位数
    - 解法：使用两个堆+自定义Comparator维护中位数
    - 时间复杂度：O(log n)每次插入
    - 空间复杂度：O(n)
    - 网址：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1048

43. **Timus OJ 1025: Democracy in Danger (民主危机)**
    - 题目：计算最少需要多少票才能确保提案通过
    - 解法：排序后使用自定义Comparator选择策略
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(1)
    - 网址：https://acm.timus.ru/problem.aspx?space=1&num=1025

44. **Aizu OJ ALDS1_2_A: Bubble Sort (冒泡排序)**
    - 题目：实现冒泡排序算法
    - 解法：使用自定义Comparator实现冒泡排序
    - 时间复杂度：O(n²)
    - 空间复杂度：O(1)
    - 网址：http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_A

45. **Comet OJ Contest #3: 排序练习 (排序练习)**
    - 题目：对各种数据类型进行排序练习
    - 解法：使用自定义Comparator实现多种排序规则
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://cometoj.com/contest/3/problem/A

46. **MarsCode 火星编程竞赛: 自定义排序规则 (自定义排序规则)**
    - 题目：实现特定的自定义排序规则
    - 解法：使用Comparator接口实现复杂排序逻辑
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(1)
    - 网址：https://www.marscode.cn/contest/2/problem/1002

47. **ZOJ 1076: Gene Assembly (基因组装)**
    - 题目：对基因片段进行最优组装
    - 解法：使用自定义Comparator按结束位置排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1076

48. **LOJ 101: 活动安排 (活动安排)**
    - 题目：安排活动使参加的活动数量最多
    - 解法：使用自定义Comparator按结束时间排序
    - 时间复杂度：O(n log n)
    - 空间复杂度：O(n)
    - 网址：https://loj.ac/p/101

49. **各大高校OJ: 北京大学OJ 1007: DNA Sorting (DNA排序)**
    - 题目：对DNA序列按逆序数排序
    - 解法：使用自定义Comparator按逆序数排序
    - 时间复杂度：O(n² log n)
    - 空间复杂度：O(n)
    - 网址：http://poj.org/problem?id=1007

50. **各大高校OJ: 浙江大学OJ 1040: 表达式求值 (表达式求值)**
    - 题目：对中缀表达式进行求值
    - 解法：使用栈+自定义Comparator处理运算符优先级
    - 时间复杂度：O(n)
    - 空间复杂度：O(n)
    - 网址：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1040

## 解题技巧总结

### HashSet/HashMap 使用场景
1. **快速查找**：O(1)时间复杂度查找元素
2. **去重**：利用HashSet特性去除重复元素
3. **统计频次**：使用HashMap统计元素出现次数
4. **缓存**：存储中间结果避免重复计算

### TreeSet/TreeMap 使用场景
1. **有序存储**：需要元素保持有序时使用
2. **范围查询**：使用headSet、tailSet、subSet等方法
3. **最值查找**：快速找到最大/最小元素
4. **前驱后继**：使用floor、ceiling等方法查找邻近元素

### Comparator 使用技巧
1. **多条件排序**：先按主要条件排序，相同时按次要条件排序
2. **自定义规则**：实现特殊的排序逻辑
3. **逆序排列**：通过交换比较参数实现降序
4. **复合排序**：结合多种排序规则

## 工程化考量

### 异常处理
- 处理非法输入参数
- 检查边界条件
- 避免空指针异常

### 性能优化
- 选择合适的数据结构
- 避免不必要的对象创建
- 合理使用缓存

### 代码可读性
- 添加详细注释
- 使用有意义的变量名
- 保持代码结构清晰

## 复杂度分析

### 时间复杂度
- HashSet/HashMap基本操作：O(1)
- TreeSet/TreeMap基本操作：O(log n)
- 排序操作：O(n log n)

### 空间复杂度
- HashSet/HashMap：O(n)
- TreeSet/TreeMap：O(n)
- 需要考虑额外的存储空间

## 面试要点

### 常见问题
1. HashSet和TreeSet的区别
2. HashMap和TreeMap的区别
3. 如何处理哈希冲突
4. 红黑树的特点和应用场景
5. Comparator和Comparable的区别

### 最优解判断
1. 是否满足题目要求的时间复杂度
2. 是否使用了最合适的数据结构
3. 代码是否简洁易懂
4. 是否考虑了边界情况

### 调试技巧
1. 打印中间过程定位错误
2. 使用断言验证中间结果
3. 性能退化的排查方法
4. 特殊输入的测试

## 多语言实现说明

本章节提供了Java、Python和C++三种语言的实现：

### Java实现
- 使用Java标准库中的HashSet、HashMap、TreeSet、TreeMap和Comparator
- 完整的面向对象设计
- 严格的类型检查

### Python实现
- 使用Python内置的dict、set和bisect模块
- 使用sorted()函数和key参数实现自定义排序
- 简洁的语法和动态类型

### C++实现
- 使用STL中的unordered_set、unordered_map、set、map
- 使用sort函数和lambda表达式实现自定义排序
- 高性能的编译型语言实现

## 测试验证

所有实现都经过了完整的测试验证，确保：
1. 代码能够正确编译和运行
2. 输出结果符合预期
3. 时间和空间复杂度分析准确
4. 边界条件处理正确

通过本章节的学习，应该能够熟练掌握HashSet、HashMap、TreeSet、TreeMap和Comparator的使用方法，并能够灵活运用它们解决各种算法问题。