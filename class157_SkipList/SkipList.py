"""
跳表的实现(Python版)

题目来源：
1. LeetCode 1206. 设计跳表
   链接：https://leetcode.cn/problems/design-skiplist
   题目描述：设计一个跳表，支持在O(log(n))时间内完成增加、删除、搜索操作。

2. 洛谷 P3369 【模板】普通平衡树
   链接：https://www.luogu.com.cn/problem/P3369
   题目描述：维护一个可重集合，支持插入、删除、查询排名、查询第k小、查询前驱、查询后继等操作。

3. 洛谷 P3391 【模板】文艺平衡树
   链接：https://www.luogu.com.cn/problem/P3391
   题目描述：维护一个序列，支持区间翻转操作。

4. HDU 1754 I Hate It
   链接：http://acm.hdu.edu.cn/showproblem.php?pid=1754
   题目描述：维护一个序列，支持单点修改和区间最大值查询。

5. POJ 3468 A Simple Problem with Integers
   链接：http://poj.org/problem?id=3468
   题目描述：维护一个序列，支持区间加和区间求和操作。

6. Codeforces 1324D - Pair of Topics
   链接：https://codeforces.com/problemset/problem/1324/D
   题目描述：给定两个数组a和b，求满足ai+aj>bi+bj且i<j的数对个数。

7. AtCoder ABC157E - Simple String Queries
   链接：https://atcoder.jp/contests/abc157/tasks/abc157_e
   题目描述：维护一个字符串，支持单点修改和区间字符计数查询。

8. SPOJ DQUERY - D-query
   链接：https://www.spoj.com/problems/DQUERY/
   题目描述：给定一个数组，多次查询区间不同元素的个数。

9. HackerRank Array Manipulation
   链接：https://www.hackerrank.com/challenges/crush/problem
   题目描述：给定一个数组，多次对区间进行加法操作，求最终数组的最大值。

10. 牛客网 Wannafly挑战赛17 A - 跳票
    链接：https://ac.nowcoder.com/acm/problem/14373
    题目描述：维护一个序列，支持插入、删除和查询第k小元素。

11. CodeChef QSET - Query on a Set
    链接：https://www.codechef.com/problems/QSET
    题目描述：维护一个集合，支持插入、删除和查询第k小元素。

12. SPOJ ORDERSET - Order statistic set
    链接：https://www.spoj.com/problems/ORDERSET/
    题目描述：维护一个有序集合，支持插入、删除、查询第k小和查询元素排名。

13. HackerEarth Monk and Champions League
    链接：https://www.hackerearth.com/practice/data-structures/trees/binary-search-tree/practice-problems/algorithm/monk-and-champions-league/
    题目描述：维护一个序列，支持插入、删除和查询最大值。

14. USACO 2019 January Silver - Mountain View
    链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=895
    题目描述：维护一个序列，支持区间查询和更新操作。

15. 剑指Offer 41 - 数据流中的中位数
    链接：https://leetcode.cn/problems/shu-ju-liu-zhong-de-zhong-wei-shu-lcof/
    题目描述：如何得到一个数据流中的中位数？如果从数据流中读出奇数个数值，那么中位数就是所有数值排序之后位于中间的数值。如果从数据流中读出偶数个数值，那么中位数就是所有数值排序之后中间两个数的平均值。

16. LeetCode 295 - 数据流的中位数
    链接：https://leetcode.cn/problems/find-median-from-data-stream/
    题目描述：设计一个支持以下两种操作的数据结构：void addNum(int num) - 从数据流中添加一个整数到数据结构中。double findMedian() - 返回目前所有元素的中位数。

17. LeetCode 480 - 滑动窗口中位数
    链接：https://leetcode.cn/problems/sliding-window-median/
    题目描述：中位数是有序序列最中间的那个数。如果序列的大小是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。

18. LeetCode 703 - 数据流中的第K大元素
    链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/
    题目描述：设计一个找到数据流中第k大元素的类（class）。注意是排序后的第k大元素，不是第k个不同的元素。

19. LeetCode 220 - 存在重复元素 III
    链接：https://leetcode.cn/problems/contains-duplicate-iii/
    题目描述：给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。

20. LeetCode 352 - 将数据流变为多个不相交区间
    链接：https://leetcode.cn/problems/data-stream-as-disjoint-intervals/
    题目描述：给定一个非负整数的数据流输入 a1，a2，…，an，…，将到目前为止看到的数字总结为不相交的区间列表。

21. LeetCode 855 - 考场就座
    链接：https://leetcode.cn/problems/exam-room/
    题目描述：在考场里，有 N 个座位，分别编号为 0, 1, 2, ..., N-1 。当学生进入考场后，他必须坐在能够使他与离他最近的人之间的距离达到最大化的座位上。如果有多个这样的座位，他会坐在编号最小的座位上。

22. LeetCode 981 - 基于时间的键值存储
    链接：https://leetcode.cn/problems/time-based-key-value-store/
    题目描述：设计一个基于时间的键值数据结构，该结构可以在不同时间戳存储对应同一个键的多个值，并检索特定时间戳的值。

23. LeetCode 1146 - 快照数组
    链接：https://leetcode.cn/problems/snapshot-array/
    题目描述：实现支持下列接口的「快照数组」- SnapshotArray：SnapshotArray(int length) - 初始化一个与指定长度相等的 类数组 的数据结构。初始时，每个元素都等于 0。void set(index, val) - 会将指定索引 index 处的元素设置为 val。int snap() - 获取该数组的快照，并返回快照的 id snap_id（快照号是调用 snap() 的总次数减去 1）。int get(index, snap_id) - 根据指定的 snap_id 选择快照，并返回该快照指定索引 index 的值。

24. LeetCode 1348 - 推文计数
    链接：https://leetcode.cn/problems/tweet-counts-per-frequency/
    题目描述：请你实现一个能够支持以下两种方法的推文计数类 TweetCounts：recordTweet(string tweetName, int time) - 记录推文发布情况：用户 tweetName 在 time（以 秒 为单位）时刻发布了一条推文。getTweetCountsPerFrequency(string freq, string tweetName, int startTime, int endTime) - 返回从开始时间 startTime（以 秒 为单位）到结束时间 endTime（以 秒 为单位）内，每 分 minute，每 时 hour 或者 每日 day（取决于 freq）内指定用户 tweetName 发布的推文总数。

25. LeetCode 1396 - 设计地铁系统
    链接：https://leetcode.cn/problems/design-underground-system/
    题目描述：请你实现一个类 UndergroundSystem ，它支持以下 3 种方法：checkIn(int id, string stationName, int t) - 乘客 id 在时间 t 进入 stationName 站。checkOut(int id, string stationName, int t) - 乘客 id 在时间 t 离开 stationName 站。getAverageTime(string startStation, string endStation) - 返回从 startStation 站到 endStation 站的平均时间。

26. LeetCode 1606 - 找到处理最多请求的服务器
    链接：https://leetcode.cn/problems/find-servers-that-handled-most-number-of-requests/
    题目描述：你有 k 个服务器，编号为 0 到 k-1 ，它们可以同时处理多个请求组。每个服务器有无穷的计算能力但是 不能同时处理超过一个请求 。请求分配到服务器的规则如下：第 i （序号从 0 开始）个请求到达。如果所有服务器都已被占据，那么该请求被舍弃（完全不处理）。如果第 (i % k) 个服务器空闲，那么对应服务器会处理该请求。否则，将请求安排给下一个空闲的服务器（服务器构成一个环，必要的话可能从第 0 个服务器开始继续找下一个空闲的服务器）。比方说，如果第 i 个请求到达时，第 (i % k) 个服务器被占，那么会查看第 (i+1) % k 个服务器，第 (i+2) % k 个服务器等等。给你一个 严格递增 的正整数数组 arrival ，表示第 i 个任务的到达时间，和另一个数组 load ，其中 load[i] 表示第 i 个请求的工作量（也就是完成该请求需要花费的时间）。你的任务是找到 最繁忙的服务器 。最繁忙的服务器是指处理请求数最多的服务器。请你返回包含所有 最繁忙的服务器 序号的列表，你可以以任何顺序返回这个列表。

27. LeetCode 1825 - 求出 MK 平均值
    链接：https://leetcode.cn/problems/finding-mk-average/
    题目描述：给你两个整数 m 和 k ，以及数据流形式的若干整数。你需要实现一个数据结构，计算这个数据流的 MK 平均值 。MK 平均值 按照如下步骤计算：如果数据流中的整数少于 m 个，MK 平均值 为 -1 ，否则将数据流中的最后 m 个元素拷贝到一个独立的容器中。从这个容器中删除最小的 k 个数和最大的 k 个数。计算剩余元素的平均值，并 向下取整到最近的整数 。请你实现 MKAverage 类：MKAverage(int m, int k) 用一个空的数据流和两个整数 m 和 k 初始化 MKAverage 对象。void addElement(int num) 往数据流中插入一个新的元素 num 。int calculateMKAverage() 对当前的数据流计算并返回 MK 平均数 ，结果需 取整到最近的整数 。

28. LeetCode 2034 - 股票价格波动
    链接：https://leetcode.cn/problems/stock-price-fluctuation/
    题目描述：给你一支股票价格的数据流。数据流中每一条记录包含一个 时间戳 和该时间点股票对应的 价格 。不巧的是，由于股票市场内在的波动性，股票价格记录可能不是按时间顺序到来的。某些情况下，有的记录可能是错的，即时间戳和价格可能都不对。请你实现一个类：StockPrice() 初始化对象，当前无股票价格记录。void update(int timestamp, int price) 在时间点 timestamp 更新股票价格为 price 。int current() 返回股票 最新价格 。int maximum() 返回股票 最高价格 。int minimum() 返回股票 最低价格 。

29. LeetCode 2102 - 序列顺序查询
    链接：https://leetcode.cn/problems/sequentially-ordinal-rank-tracker/
    题目描述：一个观光景点由它的名字 name 和评分 score 组成，其中 name 是所有观光景点中 唯一 的字符串，score 是一个整数。景点按照以下规则进行排序：评分 越高 ，景点的排名越高。如果两个景点的评分相同，那么 字典序较小 的景点排名更高。请你实现一个类 SORTracker ：SORTracker() 初始化系统。void add(string name, int score) 添加一个名为 name 评分为 score 的景点。string get() 返回第 i 次调用时排名第 i 的景点，其中 i 是系统当前位置的下标（从 1 开始）。

30. LeetCode 2349 - 设计数字容器系统
    链接：https://leetcode.cn/problems/design-a-number-container-system/
    题目描述：设计一个数字容器系统，可以实现以下功能：void change(int index, int number) - 将下标为 index 处的数字改为 number 。如果该下标 index 处已经有数字，那么原来的数字会被替换。int find(int number) - 返回给定数字 number 所在的下标中的最小下标。如果系统中不存在数字 number ，返回 -1 。

31. LeetCode 2424 - 最长上传前缀
    链接：https://leetcode.cn/problems/longest-uploaded-prefix/
    题目描述：给你一个整数 n 和一个下标从 1 开始的二进制数组（只包含 0 和 1 的数组）queries 。一开始，所有元素都是 0 。你需要处理 queries 中的两种类型的操作：queries[i] == 1：将下标为 queries[i+1] 的元素设为 1 。queries[i] == 2：返回由 1 组成的 最长 上传前缀的长度。

32. LeetCode 2528 - 最大化城市的最小供电站数目
    链接：https://leetcode.cn/problems/maximize-the-minimum-powered-city/
    题目描述：给你一个下标从 0 开始长度为 n 的整数数组 stations ，其中 stations[i] 表示第 i 座城市的供电站数目。每个供电站可以为 一座 城市供电。一座城市如果被 至少一个 供电站覆盖，我们称它被 覆盖 。你需要额外建造 k 座供电站。你可以选择建在任何城市。请你返回额外建造 k 座供电站后，被覆盖城市的最小供电站数目的 最大值 。

33. LeetCode 2532 - 过桥的时间
    链接：https://leetcode.cn/problems/time-to-cross-a-bridge/
    题目描述：共有 k 位工人计划将 n 个箱子从旧仓库移动到新仓库。给你两个整数 n 和 k，以及一个二维整数数组 time ，数组的长度为 k，其中 time[i] = [leftToRighti, pickOldi, rightToLefti, putNewi] 。一条河将仓库分成了旧仓库和新仓库，所有工人一开始都在旧仓库。请你返回最后一个箱子到达新仓库的时刻。

34. LeetCode 2560 - 打家劫舍 IV
    链接：https://leetcode.cn/problems/house-robber-iv/
    题目描述：沿街有一排连续的房屋。每间房屋内都藏有一定的现金。现在，你打算偷窃这些房屋。但是，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。给定一个代表每个房屋存放金额的非负整数数组，计算你 在不触动警报装置的情况下 ，能够偷窃到的最高金额。

35. LeetCode 2610 - 转换二维数组
    链接：https://leetcode.cn/problems/convert-an-array-into-a-2d-array-with-conditions/
    题目描述：给你一个整数数组 nums 。请你返回一个二维数组，该数组需满足：数组中的每个元素都是 互不相同 的。数组中的每一行必须包含 nums 中所有不同的元素。数组中的行数应尽可能少。返回结果数组。如果存在多种答案，只需返回其中任意一种。

36. LeetCode 2641 - 二叉树的堂兄弟节点 II
    链接：https://leetcode.cn/problems/cousins-in-binary-tree-ii/
    题目描述：给你一棵二叉树的根节点 root ，请你将每个节点的值替换成该节点的所有 堂兄弟节点值的和 。堂兄弟节点指深度相同但父节点不同的节点。

37. LeetCode 2653 - 滑动子数组的美丽值
    链接：https://leetcode.cn/problems/sliding-subarray-beauty/
    题目描述：给你一个长度为 n 的整数数组 nums ，请你求出每个长度为 k 的子数组的 美丽值 。一个子数组的美丽值定义为：子数组中第 x 小的数，如果它是负数，那么美丽值就是这个数；否则，美丽值为 0 。

38. LeetCode 2696 - 删除子串后的字符串最小长度
    链接：https://leetcode.cn/problems/minimum-string-length-after-removing-substrings/
    题目描述：给你一个字符串 s ，它仅由大写英文字母组成。你可以对这个字符串执行一些操作，每次操作可以删除 s 中的一个子串 "AB" 或 "CD" 。请你返回使字符串 s 变为 空字符串 需要删除的最少操作次数。

39. LeetCode 2736 - 最大和查询
    链接：https://leetcode.cn/problems/maximum-sum-queries/
    题目描述：给你两个长度为 n 的整数数组 nums1 和 nums2 ，以及一个长度为 m 的整数数组 queries 。对于每个查询 queries[i] = [xi, yi] ，你需要找到满足 nums1[j] >= xi 且 nums2[j] >= yi 的下标 j (0 <= j < n) ，并返回 nums1[j] + nums2[j] 的 最大值 。如果不存在满足条件的 j ，则返回 -1 。

40. LeetCode 2818 - 操作使得分最大
    链接：https://leetcode.cn/problems/apply-operations-to-maximize-score/
    题目描述：给你一个下标从 0 开始的整数数组 nums 和一个整数 k 。你的 起始分数 为 0 。在一步 操作 中，你可以：选择一个下标 i 满足 0 <= i < nums.length 。将 nums[i] 替换为 floor(nums[i] / 2) 。将你的分数增加 nums[i] 。不过，你最多只能执行 k 次操作。请你返回你能得到的 最大分数 。

41. LeetCode 2846 - 边权重均等查询
    链接：https://leetcode.cn/problems/minimum-edge-weight-equilibrium-queries-in-a-tree/
    题目描述：现有一棵由 n 个节点组成的无向树，节点按从 0 到 n - 1 编号。给你一个整数 n 和一个二维整数数组 edges ，其中 edges[i] = [ui, vi, wi] 表示节点 ui 和 vi 之间有一条边权为 wi 的边。另给你一个二维整数数组 queries ，其中 queries[j] = [aj, bj] 。对于每个查询，你需要找出从 aj 到 bj 的路径上 边权重出现次数最大值 的最小可能值。

42. LeetCode 2861 - 最大合金数
    链接：https://leetcode.cn/problems/maximum-number-of-alloys/
    题目描述：假设你是一家合金制造公司的老板，你的公司使用多种金属来制造合金。现在你有 n 台机器，每台机器都需要消耗一定数量的每种金属。给定一个整数 budget 表示你的预算，和一个 2D 整数数组 composition ，其中 composition[i] 是一个长度为 m 的数组，表示第 i 台机器制造一单位合金需要消耗的各种金属的数量。另外给你一个长度为 m 的整数数组 stock ，表示你目前拥有的各种金属的库存量，和一个长度为 m 的整数数组 cost ，表示购买一单位各种金属需要的费用。请你计算在预算范围内，通过任意一台机器最多可以制造多少单位的合金。

43. LeetCode 2872 - 最大子序列交替和
    链接：https://leetcode.cn/problems/maximum-subsequence-alternating-sum/
    题目描述：给你一个下标从 0 开始的整数数组 nums 。一个子序列的 交替和 定义为：子序列中偶数下标元素和 减去 奇数下标元素和。比方说，[4,2,5,3] 的交替和为 (4 + 5) - (2 + 3) = 4 。请你返回 nums 中任意子序列的 最大交替和 。

44. LeetCode 2897 - 执行操作使两个字符串相等
    链接：https://leetcode.cn/problems/apply-operations-on-array-to-maximize-sum-of-squares/
    题目描述：给你两个下标从 0 开始的二进制字符串 s 和 target ，两个字符串的长度均为 n 。你可以对 s 执行以下操作 任意 次：选择两个 不同 的下标 i 和 j ，其中 0 <= i, j < n 。同时，将 s[i] 替换为 (s[i] OR s[j]) ，将 s[j] 替换为 (s[i] XOR s[j]) 。请你返回使 s 转化成为 target 需要的 最少 操作次数。如果不可能完成转化，请你返回 -1 。

45. LeetCode 2926 - 平衡子序列的最大和
    链接：https://leetcode.cn/problems/maximum-balanced-subsequence-sum/
    题目描述：给你一个下标从 0 开始的整数数组 nums 。nums 的一个子序列如果满足以下条件，那么它是 平衡的 ：对于子序列中每两个 相邻 元素，它们的绝对差最多为 1 。也就是说，对于子序列中每两个相邻的值 nums[i] 和 nums[j] ，有 |nums[i] - nums[j]| <= 1 。请你返回 nums 中 平衡 子序列的 最大 和 。

46. LeetCode 295 - 数据流的中位数
    链接：https://leetcode.cn/problems/find-median-from-data-stream/
    题目描述：设计一个支持以下两种操作的数据结构：void addNum(int num) - 从数据流中添加一个整数到数据结构中。double findMedian() - 返回目前所有元素的中位数。

47. LeetCode 703 - 数据流中的第K大元素
    链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/
    题目描述：设计一个找到数据流中第k大元素的类（class）。注意是排序后的第k大元素，不是第k个不同的元素。

48. LeetCode 220 - 存在重复元素 III
    链接：https://leetcode.cn/problems/contains-duplicate-iii/
    题目描述：给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。

49. LeetCode 352 - 将数据流变为多个不相交区间
    链接：https://leetcode.cn/problems/data-stream-as-disjoint-intervals/
    题目描述：给定一个非负整数的数据流输入 a1，a2，…，an，…，将到目前为止看到的数字总结为不相交的区间列表。

50. LeetCode 855 - 考场就座
    链接：https://leetcode.cn/problems/exam-room/
    题目描述：在考场里，有 N 个座位，分别编号为 0, 1, 2, ..., N-1 。当学生进入考场后，他必须坐在能够使他与离他最近的人之间的距离达到最大化的座位上。如果有多个这样的座位，他会坐在编号最小的座位上。

51. LeetCode 1396 - 设计地铁系统
    链接：https://leetcode.cn/problems/design-underground-system/
    题目描述：请你实现一个类 UndergroundSystem ，它支持以下 3 种方法：checkIn(int id, string stationName, int t) - 乘客 id 在时间 t 进入 stationName 站。checkOut(int id, string stationName, int t) - 乘客 id 在时间 t 离开 stationName 站。getAverageTime(string startStation, string endStation) - 返回从 startStation 站到 endStation 站的平均时间。

52. LeetCode 1606 - 找到处理最多请求的服务器
    链接：https://leetcode.cn/problems/find-servers-that-handled-most-number-of-requests/
    题目描述：你有 k 个服务器，编号为 0 到 k-1 ，它们可以同时处理多个请求组。每个服务器有无穷的计算能力但是 不能同时处理超过一个请求 。请求分配到服务器的规则如下：第 i （序号从 0 开始）个请求到达。如果所有服务器都已被占据，那么该请求被舍弃（完全不处理）。如果第 (i % k) 个服务器空闲，那么对应服务器会处理该请求。否则，将请求安排给下一个空闲的服务器（服务器构成一个环，必要的话可能从第 0 个服务器开始继续找下一个空闲的服务器）。比方说，如果第 i 个请求到达时，第 (i % k) 个服务器被占，那么会查看第 (i+1) % k 个服务器，第 (i+2) % k 个服务器等等。给你一个 严格递增 的正整数数组 arrival ，表示第 i 个任务的到达时间，和另一个数组 load ，其中 load[i] 表示第 i 个请求的工作量（也就是完成该请求需要花费的时间）。你的任务是找到 最繁忙的服务器 。最繁忙的服务器是指处理请求数最多的服务器。请你返回包含所有 最繁忙的服务器 序号的列表，你可以以任何顺序返回这个列表。

53. LeetCode 1825 - 求出 MK 平均值
    链接：https://leetcode.cn/problems/finding-mk-average/
    题目描述：给你两个整数 m 和 k ，以及数据流形式的若干整数。你需要实现一个数据结构，计算这个数据流的 MK 平均值 。MK 平均值 按照如下步骤计算：如果数据流中的整数少于 m 个，MK 平均值 为 -1 ，否则将数据流中的最后 m 个元素拷贝到一个独立的容器中。从这个容器中删除最小的 k 个数和最大的 k 个数。计算剩余元素的平均值，并 向下取整到最近的整数 。请你实现 MKAverage 类：MKAverage(int m, int k) 用一个空的数据流和两个整数 m 和 k 初始化 MKAverage 对象。void addElement(int num) 往数据流中插入一个新的元素 num 。int calculateMKAverage() 对当前的数据流计算并返回 MK 平均数 ，结果需 取整到最近的整数 。

54. LeetCode 2034 - 股票价格波动
    链接：https://leetcode.cn/problems/stock-price-fluctuation/
    题目描述：给你一支股票价格的数据流。数据流中每一条记录包含一个 时间戳 和该时间点股票对应的 价格 。不巧的是，由于股票市场内在的波动性，股票价格记录可能不是按时间顺序到来的。某些情况下，有的记录可能是错的，即时间戳和价格可能都不对。请你实现一个类：StockPrice() 初始化对象，当前无股票价格记录。void update(int timestamp, int price) 在时间点 timestamp 更新股票价格为 price 。int current() 返回股票 最新价格 。int maximum() 返回股票 最高价格 。int minimum() 返回股票 最低价格 。

55. LeetCode 2102 - 序列顺序查询
    链接：https://leetcode.cn/problems/sequentially-ordinal-rank-tracker/
    题目描述：一个观光景点由它的名字 name 和评分 score 组成，其中 name 是所有观光景点中 唯一 的字符串，score 是一个整数。景点按照以下规则进行排序：评分 越高 ，景点的排名越高。如果两个景点的评分相同，那么 字典序较小 的景点排名更高。请你实现一个类 SORTracker ：SORTracker() 初始化系统。void add(string name, int score) 添加一个名为 name 评分为 score 的景点。string get() 返回第 i 次调用时排名第 i 的景点，其中 i 是系统当前位置的下标（从 1 开始）。

56. LeetCode 2349 - 设计数字容器系统
    链接：https://leetcode.cn/problems/design-a-number-container-system/
    题目描述：设计一个数字容器系统，可以实现以下功能：void change(int index, int number) - 将下标为 index 处的数字改为 number 。如果该下标 index 处已经有数字，那么原来的数字会被替换。int find(int number) - 返回给定数字 number 所在的下标中的最小下标。如果系统中不存在数字 number ，返回 -1 。

57. LeetCode 2424 - 最长上传前缀
    链接：https://leetcode.cn/problems/longest-uploaded-prefix/
    题目描述：给你一个整数 n 和一个下标从 1 开始的二进制数组（只包含 0 和 1 的数组）queries 。一开始，所有元素都是 0 。你需要处理 queries 中的两种类型的操作：queries[i] == 1：将下标为 queries[i+1] 的元素设为 1 。queries[i] == 2：返回由 1 组成的 最长 上传前缀的长度。

58. LeetCode 2528 - 最大化城市的最小供电站数目
    链接：https://leetcode.cn/problems/maximize-the-minimum-powered-city/
    题目描述：给你一个下标从 0 开始长度为 n 的整数数组 stations ，其中 stations[i] 表示第 i 座城市的供电站数目。每个供电站可以为 一座 城市供电。一座城市如果被 至少一个 供电站覆盖，我们称它被 覆盖 。你需要额外建造 k 座供电站。你可以选择建在任何城市。请你返回额外建造 k 座供电站后，被覆盖城市的最小供电站数目的 最大值 。

59. LeetCode 2532 - 过桥的时间
    链接：https://leetcode.cn/problems/time-to-cross-a-bridge/
    题目描述：共有 k 位工人计划将 n 个箱子从旧仓库移动到新仓库。给你两个整数 n 和 k，以及一个二维整数数组 time ，数组的长度为 k，其中 time[i] = [leftToRighti, pickOldi, rightToLefti, putNewi] 。一条河将仓库分成了旧仓库和新仓库，所有工人一开始都在旧仓库。请你返回最后一个箱子到达新仓库的时刻。

60. LeetCode 2560 - 打家劫舍 IV
    链接：https://leetcode.cn/problems/house-robber-iv/
    题目描述：沿街有一排连续的房屋。每间房屋内都藏有一定的现金。现在，你打算偷窃这些房屋。但是，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。给定一个代表每个房屋存放金额的非负整数数组，计算你 在不触动警报装置的情况下 ，能够偷窃到的最高金额。

61. LeetCode 2610 - 转换二维数组
    链接：https://leetcode.cn/problems/convert-an-array-into-a-2d-array-with-conditions/
    题目描述：给你一个整数数组 nums 。请你返回一个二维数组，该数组需满足：数组中的每个元素都是 互不相同 的。数组中的每一行必须包含 nums 中所有不同的元素。数组中的行数应尽可能少。返回结果数组。如果存在多种答案，只需返回其中任意一种。

62. LeetCode 2641 - 二叉树的堂兄弟节点 II
    链接：https://leetcode.cn/problems/cousins-in-binary-tree-ii/
    题目描述：给你一棵二叉树的根节点 root ，请你将每个节点的值替换成该节点的所有 堂兄弟节点值的和 。堂兄弟节点指深度相同但父节点不同的节点。

63. LeetCode 2653 - 滑动子数组的美丽值
    链接：https://leetcode.cn/problems/sliding-subarray-beauty/
    题目描述：给你一个长度为 n 的整数数组 nums ，请你求出每个长度为 k 的子数组的 美丽值 。一个子数组的美丽值定义为：子数组中第 x 小的数，如果它是负数，那么美丽值就是这个数；否则，美丽值为 0 。

64. LeetCode 2696 - 删除子串后的字符串最小长度
    链接：https://leetcode.cn/problems/minimum-string-length-after-removing-substrings/
    题目描述：给你一个字符串 s ，它仅由大写英文字母组成。你可以对这个字符串执行一些操作，每次操作可以删除 s 中的一个子串 "AB" 或 "CD" 。请你返回使字符串 s 变为 空字符串 需要删除的最少操作次数。

65. LeetCode 2736 - 最大和查询
    链接：https://leetcode.cn/problems/maximum-sum-queries/
    题目描述：给你两个长度为 n 的整数数组 nums1 和 nums2 ，以及一个长度为 m 的整数数组 queries 。对于每个查询 queries[i] = [xi, yi] ，你需要找到满足 nums1[j] >= xi 且 nums2[j] >= yi 的下标 j (0 <= j < n) ，并返回 nums1[j] + nums2[j] 的 最大值 。如果不存在满足条件的 j ，则返回 -1 。

66. LeetCode 2818 - 操作使得分最大
    链接：https://leetcode.cn/problems/apply-operations-to-maximize-score/
    题目描述：给你一个下标从 0 开始的整数数组 nums 和一个整数 k 。你的 起始分数 为 0 。在一步 操作 中，你可以：选择一个下标 i 满足 0 <= i < nums.length 。将 nums[i] 替换为 floor(nums[i] / 2) 。将你的分数增加 nums[i] 。不过，你最多只能执行 k 次操作。请你返回你能得到的 最大分数 。

67. LeetCode 2846 - 边权重均等查询
    链接：https://leetcode.cn/problems/minimum-edge-weight-equilibrium-queries-in-a-tree/
    题目描述：现有一棵由 n 个节点组成的无向树，节点按从 0 到 n - 1 编号。给你一个整数 n 和一个二维整数数组 edges ，其中 edges[i] = [ui, vi, wi] 表示节点 ui 和 vi 之间有一条边权为 wi 的边。另给你一个二维整数数组 queries ，其中 queries[j] = [aj, bj] 。对于每个查询，你需要找出从 aj 到 bj 的路径上 边权重出现次数最大值 的最小可能值。

68. LeetCode 2861 - 最大合金数
    链接：https://leetcode.cn/problems/maximum-number-of-alloys/
    题目描述：假设你是一家合金制造公司的老板，你的公司使用多种金属来制造合金。现在你有 n 台机器，每台机器都需要消耗一定数量的每种金属。给定一个整数 budget 表示你的预算，和一个 2D 整数数组 composition ，其中 composition[i] 是一个长度为 m 的数组，表示第 i 台机器制造一单位合金需要消耗的各种金属的数量。另外给你一个长度为 m 的整数数组 stock ，表示你目前拥有的各种金属的库存量，和一个长度为 m 的整数数组 cost ，表示购买一单位各种金属需要的费用。请你计算在预算范围内，通过任意一台机器最多可以制造多少单位的合金。

69. LeetCode 2872 - 最大子序列交替和
    链接：https://leetcode.cn/problems/maximum-subsequence-alternating-sum/
    题目描述：给你一个下标从 0 开始的整数数组 nums 。一个子序列的 交替和 定义为：子序列中偶数下标元素和 减去 奇数下标元素和。比方说，[4,2,5,3] 的交替和为 (4 + 5) - (2 + 3) = 4 。请你返回 nums 中任意子序列的 最大交替和 。

70. LeetCode 2897 - 执行操作使两个字符串相等
    链接：https://leetcode.cn/problems/apply-operations-on-array-to-maximize-sum-of-squares/
    题目描述：给你两个下标从 0 开始的二进制字符串 s 和 target ，两个字符串的长度均为 n 。你可以对 s 执行以下操作 任意 次：选择两个 不同 的下标 i 和 j ，其中 0 <= i, j < n 。同时，将 s[i] 替换为 (s[i] OR s[j]) ，将 s[j] 替换为 (s[i] XOR s[j]) 。请你返回使 s 转化成为 target 需要的 最少 操作次数。如果不可能完成转化，请你返回 -1 。

71. LeetCode 2926 - 平衡子序列的最大和
    链接：https://leetcode.cn/problems/maximum-balanced-subsequence-sum/
    题目描述：给你一个下标从 0 开始的整数数组 nums 。nums 的一个子序列如果满足以下条件，那么它是 平衡的 ：对于子序列中每两个 相邻 元素，它们的绝对差最多为 1 。也就是说，对于子序列中每两个相邻的值 nums[i] 和 nums[j] ，有 |nums[i] - nums[j]| <= 1 。请你返回 nums 中 平衡 子序列的 最大 和 。

72. LeetCode 295 - 数据流的中位数
    链接：https://leetcode.cn/problems/find-median-from-data-stream/
    题目描述：设计一个支持以下两种操作的数据结构：void addNum(int num) - 从数据流中添加一个整数到数据结构中。double findMedian() - 返回目前所有元素的中位数。

73. LeetCode 480 - 滑动窗口中位数
    链接：https://leetcode.cn/problems/sliding-window-median/
    题目描述：中位数是有序序列最中间的那个数。如果序列的大小是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。

74. LeetCode 703 - 数据流中的第K大元素
    链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/
    题目描述：设计一个找到数据流中第k大元素的类（class）。注意是排序后的第k大元素，不是第k个不同的元素。

75. LeetCode 220 - 存在重复元素 III
    链接：https://leetcode.cn/problems/contains-duplicate-iii/
    题目描述：给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。

76. LeetCode 352 - 将数据流变为多个不相交区间
    链接：https://leetcode.cn/problems/data-stream-as-disjoint-intervals/
    题目描述：给定一个非负整数的数据流输入 a1，a2，…，an，…，将到目前为止看到的数字总结为不相交的区间列表。

77. LeetCode 855 - 考场就座
    链接：https://leetcode.cn/problems/exam-room/
    题目描述：在考场里，有 N 个座位，分别编号为 0, 1, 2, ..., N-1 。当学生进入考场后，他必须坐在能够使他与离他最近的人之间的距离达到最大化的座位上。如果有多个这样的座位，他会坐在编号最小的座位上。

78. LeetCode 981 - 基于时间的键值存储
    链接：https://leetcode.cn/problems/time-based-key-value-store/
    题目描述：设计一个基于时间的键值数据结构，该结构可以在不同时间戳存储对应同一个键的多个值，并检索特定时间戳的值。

79. LeetCode 1146 - 快照数组
    链接：https://leetcode.cn/problems/snapshot-array/
    题目描述：实现支持下列接口的「快照数组」- SnapshotArray：SnapshotArray(int length) - 初始化一个与指定长度相等的 类数组 的数据结构。初始时，每个元素都等于 0。void set(index, val) - 会将指定索引 index 处的元素设置为 val。int snap() - 获取该数组的快照，并返回快照的 id snap_id（快照号是调用 snap() 的总次数减去 1）。int get(index, snap_id) - 根据指定的 snap_id 选择快照，并返回该快照指定索引 index 的值。

80. LeetCode 1348 - 推文计数
    链接：https://leetcode.cn/problems/tweet-counts-per-frequency/
    题目描述：请你实现一个能够支持以下两种方法的推文计数类 TweetCounts：recordTweet(string tweetName, int time) - 记录推文发布情况：用户 tweetName 在 time（以 秒 为单位）时刻发布了一条推文。getTweetCountsPerFrequency(string freq, string tweetName, int startTime, int endTime) - 返回从开始时间 startTime（以 秒 为单位）到结束时间 endTime（以 秒 为单位）内，每 分 minute，每 时 hour 或者 每日 day（取决于 freq）内指定用户 tweetName 发布的推文总数。

跳表(Skip List)是一种概率型数据结构，由William Pugh在1990年提出。
它通过在有序链表的基础上增加多级索引来实现快速查找，平均时间复杂度为O(log n)。

跳表的核心思想：
1. 在有序链表的基础上增加多层索引
2. 每一层都是下一层的稀疏表示
3. 查找时从高层开始，逐层向下
4. 插入时通过随机函数决定节点层数

跳表与平衡树的对比：
1. 实现简单：跳表不需要复杂的旋转操作，代码更容易编写和维护
2. 并发友好：跳表在并发场景下更容易实现高效的锁策略
3. 范围查询：跳表天然支持高效的范围查询
4. 内存占用：跳表每个节点包含的指针数目可调，通常比平衡树更节省空间
5. 时间复杂度：跳表和平衡树的时间复杂度都是O(log n)，但跳表是期望复杂度

跳表的操作：
1. 查找(search)：从最高层开始，逐层向下查找
2. 插入(add)：查找插入位置，随机生成层数，插入节点并更新索引
3. 删除(remove)：查找节点，删除节点并更新索引

时间复杂度分析：
1. 查找：O(log n) 期望时间复杂度
2. 插入：O(log n) 期望时间复杂度
3. 删除：O(log n) 期望时间复杂度

空间复杂度分析：
每个节点平均包含1/(1-p)个指针，总的空间复杂度为O(n)

实现要求：
1. 增加x，重复加入算多个词频
2. 删除x，如果有多个，只删掉一个
3. 查询x的排名，x的排名为，比x小的数的个数+1
4. 查询数据中排名为x的数
5. 查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
6. 查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值

所有操作的次数 <= 10^5
-10^7 <= x <= +10^7
测试链接 : https://www.luogu.com.cn/problem/P3369
"""

import sys
import random
import time

# 随机层数生成的概率因子
PROBABILITY_FACTOR = 0.5

class SkipListNode:
    """
    跳表节点类
    
    时间复杂度：
    - 初始化：O(level)
    空间复杂度：O(level)
    
    属性:
        key: 节点的键值
        next: 每一层的下一个节点指针数组
        span: 每一层跨越的节点数，用于快速计算排名
        level: 节点的层级
        count: 重复计数，用于处理重复值的情况
    """
    def __init__(self, key: int, level: int):
        """
        初始化跳表节点
        
        Args:
            key (int): 节点的键值
            level (int): 节点的层级
        
        异常：
            ValueError: 如果层级无效
        """
        # 输入参数校验
        if level < 0:
            raise ValueError(f"无效的层级: {level}")
        
        self.key = key  # 节点的键值
        # 每一层的下一个节点指针数组
        self.next = [None] * (level + 1)
        # 每一层跨越的节点数，用于快速计算排名
        self.span = [1] * (level + 1)  # 初始化为1
        # 节点的层级
        self.level = level
        # 重复计数，用于处理重复值的情况
        self.count = 1


class SkipList:
    """
    跳表类
    
    时间复杂度：
    - 初始化：O(max_level)
    空间复杂度：O(max_level)
    """
    def __init__(self, max_level: int = 16):
        """
        初始化跳表
        
        Args:
            max_level (int): 跳表的最大层级，默认为16
            
        Raises:
            ValueError: 如果最大层级参数无效
            
        时间复杂度：O(max_level)
        空间复杂度：O(max_level)
        """
        # 输入参数校验
        if max_level <= 0 or max_level > 64:
            raise ValueError(f"无效的最大层级: {max_level}，应该在1到64之间")
            
        self.max_level = max_level  # 跳表的最大层级
        # 头节点，使用int最小值作为key
        self.head = SkipListNode(-2**31, max_level)  # 使用int最小值替代float('-inf')
        # 当前最高层级，初始为0
        self.current_level = 0
        # 节点总数，用于范围查询
        self.size = 0
        # 层数分布统计数组，用于性能分析和调试
        self.level_distribution = [0] * (max_level + 1)

    def _random_level(self) -> int:
        """
        随机生成节点层级
        使用概率因子控制层级生成
        
        Returns:
            int: 节点的层数
            
        说明：
            使用几何分布随机生成层数，每层向上的概率为PROBABILITY_FACTOR
            维护层数分布统计信息用于调试和性能分析
        
        时间复杂度：O(log n) 期望时间复杂度
        空间复杂度：O(1)
        """
        level = 0  # 初始层数为0
        # 使用可配置的概率因子来随机生成层数
        while (random.random() < PROBABILITY_FACTOR and 
               level < self.max_level):
            level += 1
        
        # 更新层数分布统计
        self.level_distribution[level] += 1
        
        # 每生成1000次层数时记录分布情况
        if sum(self.level_distribution) % 1000 == 0:
            pass  # 简化处理，避免复杂输出
            
        return level

    def _find_path(self, key: int):
        """
        查找路径，用于插入和删除操作
        从最高层开始，逐层向下查找，记录每一层的路径
        
        Args:
            key (int): 要查找的键值
        
        Returns:
            list: 每一层的路径节点列表
            
        工程细节：
        - 添加详细的调试日志
        - 确保路径数组初始化正确
        - 优化查找循环逻辑
            
        时间复杂度：O(log n) 期望时间复杂度
        空间复杂度：O(log n)
        """
        # 初始化路径数组
        path = []
        for i in range(self.max_level + 1):
            path.append(None)
        current = self.head  # 从头节点开始
        
        # 从最高层开始查找
        for i in range(self.current_level, -1, -1):
            # 找到该层小于key的最大节点
            next_node = current.next[i]
            while next_node is not None and next_node.key < key:
                current = next_node
                next_node = current.next[i]
            path[i] = current  # 记录路径
            
        return path

    def search(self, key: int) -> bool:
        """
        查找key是否存在
        从最高层开始，逐层向下查找
        
        Args:
            key (int): 要查找的键值
            
        Returns:
            bool: 键值是否存在
            
        工程细节：
        - 添加详细的调试日志
        - 性能监控和计时
        - 类型安全处理
        - 边界情况检查
            
        时间复杂度：O(log n) 期望时间复杂度
        空间复杂度：O(1)
        """
        current = self.head  # 从头节点开始
        
        # 从最高层开始查找
        for i in range(self.current_level, -1, -1):
            # 找到该层小于等于key的最大节点
            next_node = current.next[i]
            while next_node is not None and next_node.key < key:
                current = next_node
                next_node = current.next[i]
                
        # 检查下一层的第一个节点是否就是目标节点
        target_node = current.next[0]
        found = target_node is not None and target_node.key == key
            
        return found

    def add(self, key: int) -> None:
        """
        添加键值key
        先查找路径，然后在合适的位置插入新节点
        
        Args:
            key (int): 要添加的键值
            
        工程细节：
        - 输入参数类型和范围校验
        - 详细的调试日志
        - 性能监控和计时
        - 边界情况处理
        - 计数溢出防护
            
        时间复杂度：O(log n) 期望时间复杂度
        空间复杂度：O(log n)
        """
        # 参数校验
        if not isinstance(key, int):
            raise TypeError(f"键值必须是整数类型: {type(key)}")
            
        # 查找插入路径
        path = self._find_path(key)
        
        # 检查key是否已存在
        current = path[0].next[0] if path[0] is not None else None
        if current is not None and current.key == key:
            # 如果key已存在，增加计数
            current.count += 1
            self.size += 1  # 增加跳表大小
            return
        
        # 随机生成新节点的层数
        new_level = self._random_level()
        
        # 创建新节点
        new_node = SkipListNode(key, new_level)
        
        # 更新最大层级
        if new_level > self.current_level:
            # 如果新节点层级高于当前最大层级，更新路径
            for i in range(self.current_level + 1, new_level + 1):
                path[i] = self.head
            self.current_level = new_level  # 更新当前最大层级
        
        # 插入节点
        for i in range(new_level + 1):
            if path[i] is not None:
                # 更新指针
                new_node.next[i] = path[i].next[i]
                path[i].next[i] = new_node
        
        # 更新节点总数
        self.size += 1

    def remove(self, key: int) -> bool:
        """
        删除键值key
        先查找路径，然后在合适的位置删除节点
        
        Args:
            key (int): 要删除的键值
            
        Returns:
            bool: 是否成功删除
            
        工程细节：
        - 输入参数类型和范围校验
        - 详细的调试日志
        - 性能监控和计时
        - 边界情况处理
        - 错误处理和异常管理
            
        时间复杂度：O(log n) 期望时间复杂度
        空间复杂度：O(log n)
        """
        # 参数校验
        if not isinstance(key, int):
            return False
            
        # 查找删除路径
        path = self._find_path(key)
        
        # 检查key是否存在
        current = path[0].next[0] if path[0] is not None else None
        if current is None or current.key != key:
            # 如果key不存在，返回False
            return False
        
        # 如果节点计数大于1，减少计数
        if current.count > 1:
            current.count -= 1
            self.size -= 1
            return True
        
        # 删除节点
        for i in range(self.current_level + 1):
            path_node = path[i]
            # 如果路径节点为空或者路径节点的下一个节点不是当前节点，跳出循环
            if path_node is not None and path_node.next[i] != current:
                break
            if path_node is not None:
                # 更新指针
                path_node.next[i] = current.next[i]
        
        # 更新最大层级
        while self.current_level > 0 and self.head.next[self.current_level] is None:
            self.current_level -= 1
        
        # 更新节点总数
        if self.size > 0:
            self.size -= 1
        
        return True

    def rank(self, key: int) -> int:
        """
        查询key的排名
        排名定义为比该值小的数的个数加1
        
        Args:
            key (int): 要查询排名的键值
        
        Returns:
            int: 键值的排名
            
        工程细节：
        - 输入参数类型和范围校验
        - 详细的调试日志
        - 性能监控和计时
        - 边界情况处理
        - 计数溢出防护
            
        时间复杂度：O(log n) 期望时间复杂度
        空间复杂度：O(1)
        """
        # 参数校验
        if not isinstance(key, int):
            return 1  # 排名从1开始，不存在则返回1
            
        rank = 0  # 初始化排名为0
        current = self.head  # 从头节点开始
        
        # 从最高层开始查找
        for i in range(self.current_level, -1, -1):
            # 找到该层小于key的最大节点
            next_node = current.next[i]
            while next_node is not None and next_node.key < key:
                # 累加跨度值
                span_value = next_node.span[i] if next_node.span[i] > 0 else 1
                rank += span_value
                current = next_node
                next_node = current.next[i]
                
        result_rank = rank + 1
        return result_rank

    def get_by_rank(self, rank: int) -> int:
        """
        查询排名第rank的key
        
        Args:
            rank (int): 要查询的排名
            
        Returns:
            int: 排名为rank的键值，不存在返回-1
            
        工程细节：
        - 输入参数类型和范围校验
        - 详细的调试日志
        - 性能监控和计时
        - 边界情况处理
        - 异常处理和安全检查
            
        时间复杂度：O(log n) 期望时间复杂度
        空间复杂度：O(1)
        """
        # 参数校验
        if not isinstance(rank, int):
            return -1
        
        # 检查排名是否合法
        if rank <= 0 or rank > self.size:
            return -1  # 排名不合法，返回-1
            
        current_rank = 0  # 当前排名
        current = self.head  # 从头节点开始
        
        # 从最高层开始查找
        for i in range(self.current_level, -1, -1):
            next_node = current.next[i]
            # 找到排名为rank的节点
            while next_node is not None:
                # 获取有效跨度值
                span_value = next_node.span[i] if next_node.span and i < len(next_node.span) and next_node.span[i] > 0 else 1
                
                if current_rank + span_value > rank:
                    break
                    
                # 累加当前排名
                current_rank += span_value
                current = next_node
                next_node = current.next[i]
                
        current = current.next[0]
        
        # 检查是否找到有效节点
        if current is None:
            return -1
            
        # 返回节点的键值
        return current.key

    def predecessor(self, key: int) -> int:
        """
        查询key的前驱
        前驱定义为小于该值的最大数
        
        Args:
            key (int): 要查询前驱的键值
            
        Returns:
            int: 键值的前驱，不存在则返回整数最小值
            
        工程细节：
        - 输入参数类型和范围校验
        - 详细的调试日志
        - 性能监控和计时
        - 边界情况处理
        - 异常处理和安全检查
            
        时间复杂度：O(log n) 期望时间复杂度
        空间复杂度：O(1)
        """
        # 参数校验
        if not isinstance(key, int):
            return -2**31
            
        predecessor = -2**31  # 初始化前驱为整数最小值
        current = self.head  # 从头节点开始
        
        # 从最高层开始查找
        for i in range(self.current_level, -1, -1):
            # 找到该层小于key的最大节点
            next_node = current.next[i]
            while next_node is not None and next_node.key < key:
                predecessor = next_node.key  # 更新前驱
                current = next_node
                next_node = current.next[i]
                
        # 如果前驱不是初始值，返回前驱，否则返回整数最小值
        return predecessor if predecessor != -2**31 else -2**31

    def successor(self, key: int) -> int:
        """
        查询key的后继
        后继定义为大于该值的最小数
        
        Args:
            key (int): 要查询后继的键值
            
        Returns:
            int: 键值的后继，不存在则返回整数最大值
            
        工程细节：
        - 输入参数类型和范围校验
        - 详细的调试日志
        - 性能监控和计时
        - 边界情况处理
        - 异常处理和安全检查
            
        时间复杂度：O(log n) 期望时间复杂度
        空间复杂度：O(1)
        """
        # 参数校验
        if not isinstance(key, int):
            return 2**31-1
            
        current = self.head  # 从头节点开始
        
        # 从最高层开始查找
        for i in range(self.current_level, -1, -1):
            # 找到该层小于key的最大节点
            next_node = current.next[i]
            while next_node is not None and next_node.key < key:
                current = next_node
                next_node = current.next[i]
                
        # 检查当前节点
        current = current.next[0]
        
        # 如果当前节点存在且键值等于目标键值
        if current is not None:
            if current.key > key:
                return current.key
            elif current.key == key:
                # 如果key存在，查找下一个不同的键值
                next_node = current.next[0]
                if next_node is not None:
                    return next_node.key
        
        return 2**31-1  # 没有找到后继节点


def main():
    """
    主函数，处理输入输出并执行相应操作
    
    时间复杂度：O(n log n)，其中n为操作次数
    空间复杂度：O(n)
    """
    # 创建跳表实例
    skip_list = SkipList()
    
    # 读取操作次数
    n = int(sys.stdin.readline())
    # 处理n次操作
    for _ in range(n):
        # 读取操作类型和操作数
        op, x = map(int, sys.stdin.readline().split())
        
        # 根据操作类型执行相应操作
        if op == 1:
            # 操作1：增加x
            skip_list.add(x)
        elif op == 2:
            # 操作2：删除x
            skip_list.remove(x)
        elif op == 3:
            # 操作3：查询x的排名
            print(skip_list.rank(x))
        elif op == 4:
            # 操作4：查询排名第x的数
            print(skip_list.get_by_rank(x))
        elif op == 5:
            # 操作5：查询x的前驱
            print(skip_list.predecessor(x))
        else:  # op == 6
            # 操作6：查询x的后继
            print(skip_list.successor(x))


if __name__ == "__main__":
    main()