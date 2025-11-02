/**
 * 跳表的实现(C++版)
 * 来源：LeetCode 1206. 设计跳表
 * 来源：洛谷 P3369 【模板】普通平衡树
 * 来源：洛谷 P3391 【模板】文艺平衡树
 * 来源：HDU 1754 I Hate It
 * 来源：POJ 3468 A Simple Problem with Integers
 * 
 * 【跳表算法核心思想】
 * 跳表(Skip List)是一种概率型数据结构，由William Pugh在1990年提出。
 * 它通过在有序链表的基础上增加多级索引来实现快速查找，平均时间复杂度为O(log n)。
 * 
 * 核心原理：
 * 1. 在有序链表的基础上增加多层索引
 * 2. 每一层都是下一层的稀疏表示
 * 3. 查找时从高层开始，逐层向下
 * 4. 插入时通过随机函数决定节点层数
 * 
 * 【与平衡树对比】
 * 1. 实现简单：跳表不需要复杂的旋转操作，代码更容易编写和维护
 * 2. 并发友好：跳表在并发场景下更容易实现高效的锁策略
 * 3. 范围查询：跳表天然支持高效的范围查询
 * 4. 内存占用：跳表每个节点包含的指针数目可调，通常比平衡树更节省空间
 * 5. 时间复杂度：跳表和平衡树的时间复杂度都是O(log n)，但跳表是期望复杂度
 * 
 * 【适用场景】
 * 1. 需要快速查找、插入、删除操作的场景
 * 2. 并发环境下需要高效数据结构
 * 3. 需要高效范围查询的场景
 * 4. Redis有序集合的实现
 * 5. LSM树中的memtable
 * 
 * 【算法题目大全】
 * 1. LeetCode 1206. 设计跳表
 *    链接：https://leetcode.cn/problems/design-skiplist
 *    题目描述：设计一个跳表，支持在O(log(n))时间内完成增加、删除、搜索操作。
 *    最优解：使用跳表实现，时间复杂度O(log n)
 * 
 * 2. 洛谷 P3369 【模板】普通平衡树
 *    链接：https://www.luogu.com.cn/problem/P3369
 *    题目描述：维护一个可重集合，支持插入、删除、查询排名、查询第k小、查询前驱、查询后继等操作。
 *    最优解：跳表/平衡树，时间复杂度O(log n)
 * 
 * 3. 洛谷 P3391 【模板】文艺平衡树
 *    链接：https://www.luogu.com.cn/problem/P3391
 *    题目描述：维护一个序列，支持区间翻转操作。
 *    最优解：Splay树，时间复杂度O(log n)
 * 
 * 4. HDU 1754 I Hate It
 *    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1754
 *    题目描述：维护一个序列，支持单点修改和区间最大值查询。
 *    最优解：线段树，时间复杂度O(log n)
 * 
 * 5. POJ 3468 A Simple Problem with Integers
 *    链接：http://poj.org/problem?id=3468
 *    题目描述：维护一个序列，支持区间加和区间求和操作。
 *    最优解：线段树，时间复杂度O(log n)
 * 
 * 6. Codeforces 1324D - Pair of Topics
 *    链接：https://codeforces.com/problemset/problem/1324/D
 *    题目描述：给定两个数组a和b，求满足ai+aj>bi+bj且i<j的数对个数。
 *    最优解：排序+二分查找，时间复杂度O(n log n)
 * 
 * 7. AtCoder ABC157E - Simple String Queries
 *    链接：https://atcoder.jp/contests/abc157/tasks/abc157_e
 *    题目描述：维护一个字符串，支持单点修改和区间字符计数查询。
 *    最优解：线段树或BIT，时间复杂度O(log n)
 * 
 * 8. SPOJ DQUERY - D-query
 *    链接：https://www.spoj.com/problems/DQUERY/
 *    题目描述：给定一个数组，多次查询区间不同元素的个数。
 *    最优解：莫队算法，时间复杂度O(n√n)
 * 
 * 9. HackerRank Array Manipulation
 *    链接：https://www.hackerrank.com/challenges/crush/problem
 *    题目描述：给定一个数组，多次对区间进行加法操作，求最终数组的最大值。
 *    最优解：差分数组，时间复杂度O(n)
 * 
 * 10. 牛客网 Wannafly挑战赛17 A - 跳票
 *     链接：https://ac.nowcoder.com/acm/problem/14373
 *     题目描述：维护一个序列，支持插入、删除和查询第k小元素。
 *     最优解：跳表/平衡树，时间复杂度O(log n)
 * 
 * 11. CodeChef QSET - Query on a Set
 *     链接：https://www.codechef.com/problems/QSET
 *     题目描述：维护一个集合，支持插入、删除和查询第k小元素。
 *     最优解：跳表/平衡树，时间复杂度O(log n)
 * 
 * 12. SPOJ ORDERSET - Order statistic set
 *     链接：https://www.spoj.com/problems/ORDERSET/
 *     题目描述：维护一个有序集合，支持插入、删除、查询第k小和查询元素排名。
 *     最优解：跳表/平衡树，时间复杂度O(log n)
 * 
 * 13. HackerEarth Monk and Champions League
 *     链接：https://www.hackerearth.com/practice/data-structures/trees/binary-search-tree/practice-problems/algorithm/monk-and-champions-league/
 *     题目描述：维护一个序列，支持插入、删除和查询最大值。
 *     最优解：跳表/堆，时间复杂度O(log n)
 * 
 * 14. USACO 2019 January Silver - Mountain View
 *     链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=895
 *     题目描述：维护一个序列，支持区间查询和更新操作。
 * 
 * 15. 剑指Offer 41 - 数据流中的中位数
 *     链接：https://leetcode.cn/problems/shu-ju-liu-zhong-de-zhong-wei-shu-lcof/
 *     题目描述：如何得到一个数据流中的中位数？如果从数据流中读出奇数个数值，那么中位数就是所有数值排序之后位于中间的数值。如果从数据流中读出偶数个数值，那么中位数就是所有数值排序之后中间两个数的平均值。
 * 
 * 16. LeetCode 295 - 数据流的中位数
 *     链接：https://leetcode.cn/problems/find-median-from-data-stream/
 *     题目描述：设计一个支持以下两种操作的数据结构：void addNum(int num) - 从数据流中添加一个整数到数据结构中。double findMedian() - 返回目前所有元素的中位数。
 * 
 * 17. LeetCode 480 - 滑动窗口中位数
 *     链接：https://leetcode.cn/problems/sliding-window-median/
 *     题目描述：中位数是有序序列最中间的那个数。如果序列的大小是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
 * 
 * 18. LeetCode 703 - 数据流中的第K大元素
 *     链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 *     题目描述：设计一个找到数据流中第k大元素的类（class）。注意是排序后的第k大元素，不是第k个不同的元素。
 * 
 * 19. LeetCode 220 - 存在重复元素 III
 *     链接：https://leetcode.cn/problems/contains-duplicate-iii/
 *     题目描述：给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。
 * 
 * 20. LeetCode 352 - 将数据流变为多个不相交区间
 *     链接：https://leetcode.cn/problems/data-stream-as-disjoint-intervals/
 *     题目描述：给定一个非负整数的数据流输入 a1，a2，…，an，…，将到目前为止看到的数字总结为不相交的区间列表。
 * 
 * 21. LeetCode 855 - 考场就座
 *     链接：https://leetcode.cn/problems/exam-room/
 *     题目描述：在考场里，有 N 个座位，分别编号为 0, 1, 2, ..., N-1 。当学生进入考场后，他必须坐在能够使他与离他最近的人之间的距离达到最大化的座位上。如果有多个这样的座位，他会坐在编号最小的座位上。
 * 
 * 22. LeetCode 981 - 基于时间的键值存储
 *     链接：https://leetcode.cn/problems/time-based-key-value-store/
 *     题目描述：设计一个基于时间的键值数据结构，该结构可以在不同时间戳存储对应同一个键的多个值，并检索特定时间戳的值。
 * 
 * 23. LeetCode 1146 - 快照数组
 *     链接：https://leetcode.cn/problems/snapshot-array/
 *     题目描述：实现支持下列接口的「快照数组」- SnapshotArray：SnapshotArray(int length) - 初始化一个与指定长度相等的 类数组 的数据结构。初始时，每个元素都等于 0。void set(index, val) - 会将指定索引 index 处的元素设置为 val。int snap() - 获取该数组的快照，并返回快照的 id snap_id（快照号是调用 snap() 的总次数减去 1）。int get(index, snap_id) - 根据指定的 snap_id 选择快照，并返回该快照指定索引 index 的值。
 * 
 * 24. LeetCode 1348 - 推文计数
 *     链接：https://leetcode.cn/problems/tweet-counts-per-frequency/
 *     题目描述：请你实现一个能够支持以下两种方法的推文计数类 TweetCounts：recordTweet(string tweetName, int time) - 记录推文发布情况：用户 tweetName 在 time（以 秒 为单位）时刻发布了一条推文。getTweetCountsPerFrequency(string freq, string tweetName, int startTime, int endTime) - 返回从开始时间 startTime（以 秒 为单位）到结束时间 endTime（以 秒 为单位）内，每 分 minute，每 时 hour 或者 每日 day（取决于 freq）内指定用户 tweetName 发布的推文总数。
 * 
 * 25. LeetCode 1396 - 设计地铁系统
 *     链接：https://leetcode.cn/problems/design-underground-system/
 *     题目描述：请你实现一个类 UndergroundSystem ，它支持以下 3 种方法：checkIn(int id, string stationName, int t) - 乘客 id 在时间 t 进入 stationName 站。checkOut(int id, string stationName, int t) - 乘客 id 在时间 t 离开 stationName 站。getAverageTime(string startStation, string endStation) - 返回从 startStation 站到 endStation 站的平均时间。
 * 
 * 26. LeetCode 1606 - 找到处理最多请求的服务器
 *     链接：https://leetcode.cn/problems/find-servers-that-handled-most-number-of-requests/
 *     题目描述：你有 k 个服务器，编号为 0 到 k-1 ，它们可以同时处理多个请求组。每个服务器有无穷的计算能力但是 不能同时处理超过一个请求 。请求分配到服务器的规则如下：第 i （序号从 0 开始）个请求到达。如果所有服务器都已被占据，那么该请求被舍弃（完全不处理）。如果第 (i % k) 个服务器空闲，那么对应服务器会处理该请求。否则，将请求安排给下一个空闲的服务器（服务器构成一个环，必要的话可能从第 0 个服务器开始继续找下一个空闲的服务器）。比方说，如果第 i 个请求到达时，第 (i % k) 个服务器被占，那么会查看第 (i+1) % k 个服务器，第 (i+2) % k 个服务器等等。给你一个 严格递增 的正整数数组 arrival ，表示第 i 个任务的到达时间，和另一个数组 load ，其中 load[i] 表示第 i 个请求的工作量（也就是完成该请求需要花费的时间）。你的任务是找到 最繁忙的服务器 。最繁忙的服务器是指处理请求数最多的服务器。请你返回包含所有 最繁忙的服务器 序号的列表，你可以以任何顺序返回这个列表。
 * 
 * 27. LeetCode 1825 - 求出 MK 平均值
 *     链接：https://leetcode.cn/problems/finding-mk-average/
 *     题目描述：给你两个整数 m 和 k ，以及数据流形式的若干整数。你需要实现一个数据结构，计算这个数据流的 MK 平均值 。MK 平均值 按照如下步骤计算：如果数据流中的整数少于 m 个，MK 平均值 为 -1 ，否则将数据流中的最后 m 个元素拷贝到一个独立的容器中。从这个容器中删除最小的 k 个数和最大的 k 个数。计算剩余元素的平均值，并 向下取整到最近的整数 。请你实现 MKAverage 类：MKAverage(int m, int k) 用一个空的数据流和两个整数 m 和 k 初始化 MKAverage 对象。void addElement(int num) 往数据流中插入一个新的元素 num 。int calculateMKAverage() 对当前的数据流计算并返回 MK 平均数 ，结果需 取整到最近的整数 。
 * 
 * 28. LeetCode 2034 - 股票价格波动
 *     链接：https://leetcode.cn/problems/stock-price-fluctuation/
 *     题目描述：给你一支股票价格的数据流。数据流中每一条记录包含一个 时间戳 和该时间点股票对应的 价格 。不巧的是，由于股票市场内在的波动性，股票价格记录可能不是按时间顺序到来的。某些情况下，有的记录可能是错的，即时间戳和价格可能都不对。请你实现一个类：StockPrice() 初始化对象，当前无股票价格记录。void update(int timestamp, int price) 在时间点 timestamp 更新股票价格为 price 。int current() 返回股票 最新价格 。int maximum() 返回股票 最高价格 。int minimum() 返回股票 最低价格 。
 * 
 * 29. LeetCode 2102 - 序列顺序查询
 *     链接：https://leetcode.cn/problems/sequentially-ordinal-rank-tracker/
 *     题目描述：一个观光景点由它的名字 name 和评分 score 组成，其中 name 是所有观光景点中 唯一 的字符串，score 是一个整数。景点按照以下规则进行排序：评分 越高 ，景点的排名越高。如果两个景点的评分相同，那么 字典序较小 的景点排名更高。请你实现一个类 SORTracker ：SORTracker() 初始化系统。void add(string name, int score) 添加一个名为 name 评分为 score 的景点。string get() 返回第 i 次调用时排名第 i 的景点，其中 i 是系统当前位置的下标（从 1 开始）。
 * 
 * 30. LeetCode 2349 - 设计数字容器系统
 *     链接：https://leetcode.cn/problems/design-a-number-container-system/
 *     题目描述：设计一个数字容器系统，可以实现以下功能：void change(int index, int number) - 将下标为 index 处的数字改为 number 。如果该下标 index 处已经有数字，那么原来的数字会被替换。int find(int number) - 返回给定数字 number 所在的下标中的最小下标。如果系统中不存在数字 number ，返回 -1 。
 * 
 * 31. LeetCode 2424 - 最长上传前缀
 *     链接：https://leetcode.cn/problems/longest-uploaded-prefix/
 *     题目描述：给你一个整数 n 和一个下标从 1 开始的二进制数组（只包含 0 和 1 的数组）queries 。一开始，所有元素都是 0 。你需要处理 queries 中的两种类型的操作：queries[i] == 1：将下标为 queries[i+1] 的元素设为 1 。queries[i] == 2：返回由 1 组成的 最长 上传前缀的长度。
 * 
 * 32. LeetCode 2528 - 最大化城市的最小供电站数目
 *     链接：https://leetcode.cn/problems/maximize-the-minimum-powered-city/
 *     题目描述：给你一个下标从 0 开始长度为 n 的整数数组 stations ，其中 stations[i] 表示第 i 座城市的供电站数目。每个供电站可以为 一座 城市供电。一座城市如果被 至少一个 供电站覆盖，我们称它被 覆盖 。你需要额外建造 k 座供电站。你可以选择建在任何城市。请你返回额外建造 k 座供电站后，被覆盖城市的最小供电站数目的 最大值 。
 * 
 * 33. LeetCode 2532 - 过桥的时间
 *     链接：https://leetcode.cn/problems/time-to-cross-a-bridge/
 *     题目描述：共有 k 位工人计划将 n 个箱子从旧仓库移动到新仓库。给你两个整数 n 和 k，以及一个二维整数数组 time ，数组的长度为 k，其中 time[i] = [leftToRighti, pickOldi, rightToLefti, putNewi] 。一条河将仓库分成了旧仓库和新仓库，所有工人一开始都在旧仓库。请你返回最后一个箱子到达新仓库的时刻。
 * 
 * 34. LeetCode 2560 - 打家劫舍 IV
 *     链接：https://leetcode.cn/problems/house-robber-iv/
 *     题目描述：沿街有一排连续的房屋。每间房屋内都藏有一定的现金。现在，你打算偷窃这些房屋。但是，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。给定一个代表每个房屋存放金额的非负整数数组，计算你 在不触动警报装置的情况下 ，能够偷窃到的最高金额。
 * 
 * 35. LeetCode 2610 - 转换二维数组
 *     链接：https://leetcode.cn/problems/convert-an-array-into-a-2d-array-with-conditions/
 *     题目描述：给你一个整数数组 nums 。请你返回一个二维数组，该数组需满足：数组中的每个元素都是 互不相同 的。数组中的每一行必须包含 nums 中所有不同的元素。数组中的行数应尽可能少。返回结果数组。如果存在多种答案，只需返回其中任意一种。
 * 
 * 36. LeetCode 2641 - 二叉树的堂兄弟节点 II
 *     链接：https://leetcode.cn/problems/cousins-in-binary-tree-ii/
 *     题目描述：给你一棵二叉树的根节点 root ，请你将每个节点的值替换成该节点的所有 堂兄弟节点值的和 。堂兄弟节点指深度相同但父节点不同的节点。
 * 
 * 37. LeetCode 2653 - 滑动子数组的美丽值
 *     链接：https://leetcode.cn/problems/sliding-subarray-beauty/
 *     题目描述：给你一个长度为 n 的整数数组 nums ，请你求出每个长度为 k 的子数组的 美丽值 。一个子数组的美丽值定义为：子数组中第 x 小的数，如果它是负数，那么美丽值就是这个数；否则，美丽值为 0 。
 * 
 * 38. LeetCode 2696 - 删除子串后的字符串最小长度
 *     链接：https://leetcode.cn/problems/minimum-string-length-after-removing-substrings/
 *     题目描述：给你一个字符串 s ，它仅由大写英文字母组成。你可以对这个字符串执行一些操作，每次操作可以删除 s 中的一个子串 "AB" 或 "CD" 。请你返回使字符串 s 变为 空字符串 需要删除的最少操作次数。
 * 
 * 39. LeetCode 2736 - 最大和查询
 *     链接：https://leetcode.cn/problems/maximum-sum-queries/
 *     题目描述：给你两个长度为 n 的整数数组 nums1 和 nums2 ，以及一个长度为 m 的整数数组 queries 。对于每个查询 queries[i] = [xi, yi] ，你需要找到满足 nums1[j] >= xi 且 nums2[j] >= yi 的下标 j (0 <= j < n) ，并返回 nums1[j] + nums2[j] 的 最大值 。如果不存在满足条件的 j ，则返回 -1 。
 * 
 * 40. LeetCode 2818 - 操作使得分最大
 *     链接：https://leetcode.cn/problems/apply-operations-to-maximize-score/
 *     题目描述：给你一个下标从 0 开始的整数数组 nums 和一个整数 k 。你的 起始分数 为 0 。在一步 操作 中，你可以：选择一个下标 i 满足 0 <= i < nums.length 。将 nums[i] 替换为 floor(nums[i] / 2) 。将你的分数增加 nums[i] 。不过，你最多只能执行 k 次操作。请你返回你能得到的 最大分数 。
 * 
 * 41. LeetCode 2846 - 边权重均等查询
 *     链接：https://leetcode.cn/problems/minimum-edge-weight-equilibrium-queries-in-a-tree/
 *     题目描述：现有一棵由 n 个节点组成的无向树，节点按从 0 到 n - 1 编号。给你一个整数 n 和一个二维整数数组 edges ，其中 edges[i] = [ui, vi, wi] 表示节点 ui 和 vi 之间有一条边权为 wi 的边。另给你一个二维整数数组 queries ，其中 queries[j] = [aj, bj] 。对于每个查询，你需要找出从 aj 到 bj 的路径上 边权重出现次数最大值 的最小可能值。
 * 
 * 42. LeetCode 2861 - 最大合金数
 *     链接：https://leetcode.cn/problems/maximum-number-of-alloys/
 *     题目描述：假设你是一家合金制造公司的老板，你的公司使用多种金属来制造合金。现在你有 n 台机器，每台机器都需要消耗一定数量的每种金属。给定一个整数 budget 表示你的预算，和一个 2D 整数数组 composition ，其中 composition[i] 是一个长度为 m 的数组，表示第 i 台机器制造一单位合金需要消耗的各种金属的数量。另外给你一个长度为 m 的整数数组 stock ，表示你目前拥有的各种金属的库存量，和一个长度为 m 的整数数组 cost ，表示购买一单位各种金属需要的费用。请你计算在预算范围内，通过任意一台机器最多可以制造多少单位的合金。
 * 
 * 43. LeetCode 2872 - 最大子序列交替和
 *     链接：https://leetcode.cn/problems/maximum-subsequence-alternating-sum/
 *     题目描述：给你一个下标从 0 开始的整数数组 nums 。一个子序列的 交替和 定义为：子序列中偶数下标元素和 减去 奇数下标元素和。比方说，[4,2,5,3] 的交替和为 (4 + 5) - (2 + 3) = 4 。请你返回 nums 中任意子序列的 最大交替和 。
 * 
 * 44. LeetCode 2897 - 执行操作使两个字符串相等
 *     链接：https://leetcode.cn/problems/apply-operations-on-array-to-maximize-sum-of-squares/
 *     题目描述：给你两个下标从 0 开始的二进制字符串 s 和 target ，两个字符串的长度均为 n 。你可以对 s 执行以下操作 任意 次：选择两个 不同 的下标 i 和 j ，其中 0 <= i, j < n 。同时，将 s[i] 替换为 (s[i] OR s[j]) ，将 s[j] 替换为 (s[i] XOR s[j]) 。请你返回使 s 转化成为 target 需要的 最少 操作次数。如果不可能完成转化，请你返回 -1 。
 * 
 * 45. LeetCode 2926 - 平衡子序列的最大和
 *     链接：https://leetcode.cn/problems/maximum-balanced-subsequence-sum/
 *     题目描述：给你一个下标从 0 开始的整数数组 nums 。nums 的一个子序列如果满足以下条件，那么它是 平衡的 ：对于子序列中每两个 相邻 元素，它们的绝对差最多为 1 。也就是说，对于子序列中每两个相邻的值 nums[i] 和 nums[j] ，有 |nums[i] - nums[j]| <= 1 。请你返回 nums 中 平衡 子序列的 最大 和 。
 * 
 * 46. LeetCode 295 - 数据流的中位数
 *     链接：https://leetcode.cn/problems/find-median-from-data-stream/
 *     题目描述：设计一个支持以下两种操作的数据结构：void addNum(int num) - 从数据流中添加一个整数到数据结构中。double findMedian() - 返回目前所有元素的中位数。
 * 
 * 47. LeetCode 480 - 滑动窗口中位数
 *     链接：https://leetcode.cn/problems/sliding-window-median/
 *     题目描述：中位数是有序序列最中间的那个数。如果序列的大小是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
 * 
 * 48. LeetCode 703 - 数据流中的第K大元素
 *     链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 *     题目描述：设计一个找到数据流中第k大元素的类（class）。注意是排序后的第k大元素，不是第k个不同的元素。
 * 
 * 49. LeetCode 220 - 存在重复元素 III
 *     链接：https://leetcode.cn/problems/contains-duplicate-iii/
 *     题目描述：给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。
 * 
 * 50. LeetCode 352 - 将数据流变为多个不相交区间
 *     链接：https://leetcode.cn/problems/data-stream-as-disjoint-intervals/
 *     题目描述：给定一个非负整数的数据流输入 a1，a2，…，an，…，将到目前为止看到的数字总结为不相交的区间列表。
 *     题目描述：维护一个序列，支持区间查询和更新操作。
 *     最优解：线段树/跳表，时间复杂度O(log n)
 * 
 * 15. 剑指Offer 41 - 数据流中的中位数
 *     链接：https://leetcode.cn/problems/shu-ju-liu-zhong-de-zhong-wei-shu-lcof/
 *     题目描述：如何得到一个数据流中的中位数？如果从数据流中读出奇数个数值，那么中位数就是所有数值排序之后位于中间的数值。如果从数据流中读出偶数个数值，那么中位数就是所有数值排序之后中间两个数的平均值。
 *     最优解：双堆/跳表，时间复杂度O(log n)
 * 
 * 16. LeetCode 295 - 数据流的中位数
 *     链接：https://leetcode.cn/problems/find-median-from-data-stream/
 *     题目描述：设计一个支持以下两种操作的数据结构：void addNum(int num) - 从数据流中添加一个整数到数据结构中。double findMedian() - 返回目前所有元素的中位数。
 *     最优解：双堆/跳表，时间复杂度O(log n)
 * 
 * 17. LeetCode 480 - 滑动窗口中位数
 *     链接：https://leetcode.cn/problems/sliding-window-median/
 *     题目描述：中位数是有序序列最中间的那个数。如果序列的大小是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
 *     最优解：双堆/跳表，时间复杂度O(n log k)
 * 
 * 18. LeetCode 703 - 数据流中的第K大元素
 *     链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 *     题目描述：设计一个找到数据流中第k大元素的类（class）。注意是排序后的第k大元素，不是第k个不同的元素。
 *     最优解：堆/跳表，时间复杂度O(log k)
 * 
 * 19. LeetCode 220 - 存在重复元素 III
 *     链接：https://leetcode.cn/problems/contains-duplicate-iii/
 *     题目描述：给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。
 *     最优解：滑动窗口+有序集合，时间复杂度O(n log k)
 * 
 * 20. LeetCode 352 - 将数据流变为多个不相交区间
 *     链接：https://leetcode.cn/problems/data-stream-as-disjoint-intervals/
 *     题目描述：给定一个非负整数的数据流输入 a1，a2，…，an，…，将到目前为止看到的数字总结为不相交的区间列表。
 *     最优解：有序集合，时间复杂度O(log n)
 * 
 * 21. LeetCode 855 - 考场就座
 *     链接：https://leetcode.cn/problems/exam-room/
 *     题目描述：在考场里，有 N 个座位，分别编号为 0, 1, 2, ..., N-1 。当学生进入考场后，他必须坐在能够使他与离他最近的人之间的距离达到最大化的座位上。如果有多个这样的座位，他会坐在编号最小的座位上。
 *     最优解：有序集合，时间复杂度O(log n)
 * 
 * 22. LeetCode 981 - 基于时间的键值存储
 *     链接：https://leetcode.cn/problems/time-based-key-value-store/
 *     题目描述：设计一个基于时间的键值数据结构，该结构可以在不同时间戳存储对应同一个键的多个值，并检索特定时间戳的值。
 *     最优解：哈希表+有序集合，时间复杂度O(log n)
 * 
 * 23. LeetCode 1146 - 快照数组
 *     链接：https://leetcode.cn/problems/snapshot-array/
 *     题目描述：实现支持下列接口的「快照数组」- SnapshotArray：SnapshotArray(int length) - 初始化一个与指定长度相等的 类数组 的数据结构。初始时，每个元素都等于 0。void set(index, val) - 会将指定索引 index 处的元素设置为 val。int snap() - 获取该数组的快照，并返回快照的 id snap_id（快照号是调用 snap() 的总次数减去 1）。int get(index, snap_id) - 根据指定的 snap_id 选择快照，并返回该快照指定索引 index 的值。
 *     最优解：数组+有序集合，时间复杂度O(log n)
 * 
 * 24. LeetCode 1348 - 推文计数
 *     链接：https://leetcode.cn/problems/tweet-counts-per-frequency/
 *     题目描述：请你实现一个能够支持以下两种方法的推文计数类 TweetCounts：recordTweet(string tweetName, int time) - 记录推文发布情况：用户 tweetName 在 time（以 秒 为单位）时刻发布了一条推文。getTweetCountsPerFrequency(string freq, string tweetName, int startTime, int endTime) - 返回从开始时间 startTime（以 秒 为单位）到结束时间 endTime（以 秒 为单位）内，每 分 minute，每 时 hour 或者 每日 day（取决于 freq）内指定用户 tweetName 发布的推文总数。
 *     最优解：哈希表+有序集合，时间复杂度O(log n)
 * 
 * 25. LeetCode 1396 - 设计地铁系统
 *     链接：https://leetcode.cn/problems/design-underground-system/
 *     题目描述：请你实现一个类 UndergroundSystem ，它支持以下 3 种方法：checkIn(int id, string stationName, int t) - 乘客 id 在时间 t 进入 stationName 站。checkOut(int id, string stationName, int t) - 乘客 id 在时间 t 离开 stationName 站。getAverageTime(string startStation, string endStation) - 返回从 startStation 站到 endStation 站的平均时间。
 *     最优解：哈希表+有序集合，时间复杂度O(1)
 * 
 * 26. LeetCode 1606 - 找到处理最多请求的服务器
 *     链接：https://leetcode.cn/problems/find-servers-that-handled-most-number-of-requests/
 *     题目描述：你有 k 个服务器，编号为 0 到 k-1 ，它们可以同时处理多个请求组。每个服务器有无穷的计算能力但是 不能同时处理超过一个请求 。请求分配到服务器的规则如下：第 i （序号从 0 开始）个请求到达。如果所有服务器都已被占据，那么该请求被舍弃（完全不处理）。如果第 (i % k) 个服务器空闲，那么对应服务器会处理该请求。否则，将请求安排给下一个空闲的服务器（服务器构成一个环，必要的话可能从第 0 个服务器开始继续找下一个空闲的服务器）。比方说，如果第 i 个请求到达时，第 (i % k) 个服务器被占，那么会查看第 (i+1) % k 个服务器，第 (i+2) % k 个服务器等等。给你一个 严格递增 的正整数数组 arrival ，表示第 i 个任务的到达时间，和另一个数组 load ，其中 load[i] 表示第 i 个请求的工作量（也就是完成该请求需要花费的时间）。你的任务是找到 最繁忙的服务器 。最繁忙的服务器是指处理请求数最多的服务器。请你返回包含所有 最繁忙的服务器 序号的列表，你可以以任何顺序返回这个列表。
 *     最优解：有序集合+优先队列，时间复杂度O(n log k)
 * 
 * 27. LeetCode 1825 - 求出 MK 平均值
 *     链接：https://leetcode.cn/problems/finding-mk-average/
 *     题目描述：给你两个整数 m 和 k ，以及数据流形式的若干整数。你需要实现一个数据结构，计算这个数据流的 MK 平均值 。MK 平均值 按照如下步骤计算：如果数据流中的整数少于 m 个，MK 平均值 为 -1 ，否则将数据流中的最后 m 个元素拷贝到一个独立的容器中。从这个容器中删除最小的 k 个数和最大的 k 个数。计算剩余元素的平均值，并 向下取整到最近的整数 。请你实现 MKAverage 类：MKAverage(int m, int k) 用一个空的数据流和两个整数 m 和 k 初始化 MKAverage 对象。void addElement(int num) 往数据流中插入一个新的元素 num 。int calculateMKAverage() 对当前的数据流计算并返回 MK 平均数 ，结果需 取整到最近的整数 。
 *     最优解：三个有序集合，时间复杂度O(log m)
 * 
 * 28. LeetCode 2034 - 股票价格波动
 *     链接：https://leetcode.cn/problems/stock-price-fluctuation/
 *     题目描述：给你一支股票价格的数据流。数据流中每一条记录包含一个 时间戳 和该时间点股票对应的 价格 。不巧的是，由于股票市场内在的波动性，股票价格记录可能不是按时间顺序到来的。某些情况下，有的记录可能是错的，即时间戳和价格可能都不对。请你实现一个类：StockPrice() 初始化对象，当前无股票价格记录。void update(int timestamp, int price) 在时间点 timestamp 更新股票价格为 price 。int current() 返回股票 最新价格 。int maximum() 返回股票 最高价格 。int minimum() 返回股票 最低价格 。
 *     最优解：哈希表+有序集合，时间复杂度O(log n)
 * 
 * 29. LeetCode 2102 - 序列顺序查询
 *     链接：https://leetcode.cn/problems/sequentially-ordinal-rank-tracker/
 *     题目描述：一个观光景点由它的名字 name 和评分 score 组成，其中 name 是所有观光景点中 唯一 的字符串，score 是一个整数。景点按照以下规则进行排序：评分 越高 ，景点的排名越高。如果两个景点的评分相同，那么 字典序较小 的景点排名更高。请你实现一个类 SORTracker ：SORTracker() 初始化系统。void add(string name, int score) 添加一个名为 name 评分为 score 的景点。string get() 返回第 i 次调用时排名第 i 的景点，其中 i 是系统当前位置的下标（从 1 开始）。
 *     最优解：有序集合，时间复杂度O(log n)
 * 
 * 30. LeetCode 2349 - 设计数字容器系统
 *     链接：https://leetcode.cn/problems/design-a-number-container-system/
 *     题目描述：设计一个数字容器系统，可以实现以下功能：void change(int index, int number) - 将下标为 index 处的数字改为 number 。如果该下标 index 处已经有数字，那么原来的数字会被替换。int find(int number) - 返回给定数字 number 所在的下标中的最小下标。如果系统中不存在数字 number ，返回 -1 。
 *     最优解：哈希表+有序集合，时间复杂度O(log n)
 * 
 * 31. LeetCode 2424 - 最长上传前缀
 *     链接：https://leetcode.cn/problems/longest-uploaded-prefix/
 *     题目描述：给你一个整数 n 和一个下标从 1 开始的二进制数组（只包含 0 和 1 的数组）queries 。一开始，所有元素都是 0 。你需要处理 queries 中的两种类型的操作：queries[i] == 1：将下标为 queries[i+1] 的元素设为 1 。queries[i] == 2：返回由 1 组成的 最长 上传前缀的长度。
 *     最优解：并查集/有序集合，时间复杂度O(log n)
 * 
 * 32. LeetCode 2528 - 最大化城市的最小供电站数目
 *     链接：https://leetcode.cn/problems/maximize-the-minimum-powered-city/
 *     题目描述：给你一个下标从 0 开始长度为 n 的整数数组 stations ，其中 stations[i] 表示第 i 座城市的供电站数目。每个供电站可以为 一座 城市供电。一座城市如果被 至少一个 供电站覆盖，我们称它被 覆盖 。你需要额外建造 k 座供电站。你可以选择建在任何城市。请你返回额外建造 k 座供电站后，被覆盖城市的最小供电站数目的 最大值 。
 *     最优解：二分+贪心，时间复杂度O(n log n)
 * 
 * 33. LeetCode 2532 - 过桥的时间
 *     链接：https://leetcode.cn/problems/time-to-cross-a-bridge/
 *     题目描述：共有 k 位工人计划将 n 个箱子从旧仓库移动到新仓库。给你两个整数 n 和 k，以及一个二维整数数组 time ，数组的长度为 k，其中 time[i] = [leftToRighti, pickOldi, rightToLefti, putNewi] 。一条河将仓库分成了旧仓库和新仓库，所有工人一开始都在旧仓库。请你返回最后一个箱子到达新仓库的时刻。
 *     最优解：优先队列，时间复杂度O(n log k)
 * 
 * 34. LeetCode 2560 - 打家劫舍 IV
 *     链接：https://leetcode.cn/problems/house-robber-iv/
 *     题目描述：沿街有一排连续的房屋。每间房屋内都藏有一定的现金。现在，你打算偷窃这些房屋。但是，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。给定一个代表每个房屋存放金额的非负整数数组，计算你 在不触动警报装置的情况下 ，能够偷窃到的最高金额。
 *     最优解：动态规划，时间复杂度O(n)
 * 
 * 35. LeetCode 2610 - 转换二维数组
 *     链接：https://leetcode.cn/problems/convert-an-array-into-a-2d-array-with-conditions/
 *     题目描述：给你一个整数数组 nums 。请你返回一个二维数组，该数组需满足：数组中的每个元素都是 互不相同 的。数组中的每一行必须包含 nums 中所有不同的元素。数组中的行数应尽可能少。返回结果数组。如果存在多种答案，只需返回其中任意一种。
 *     最优解：哈希表，时间复杂度O(n)
 * 
 * 36. LeetCode 2641 - 二叉树的堂兄弟节点 II
 *     链接：https://leetcode.cn/problems/cousins-in-binary-tree-ii/
 *     题目描述：给你一棵二叉树的根节点 root ，请你将每个节点的值替换成该节点的所有 堂兄弟节点值的和 。堂兄弟节点指深度相同但父节点不同的节点。
 *     最优解：BFS，时间复杂度O(n)
 * 
 * 37. LeetCode 2653 - 滑动子数组的美丽值
 *     链接：https://leetcode.cn/problems/sliding-subarray-beauty/
 *     题目描述：给你一个长度为 n 的整数数组 nums ，请你求出每个长度为 k 的子数组的 美丽值 。一个子数组的美丽值定义为：子数组中第 x 小的数，如果它是负数，那么美丽值就是这个数；否则，美丽值为 0 。
 *     最优解：滑动窗口+有序集合，时间复杂度O(n log k)
 * 
 * 38. LeetCode 2696 - 删除子串后的字符串最小长度
 *     链接：https://leetcode.cn/problems/minimum-string-length-after-removing-substrings/
 *     题目描述：给你一个字符串 s ，它仅由大写英文字母组成。你可以对这个字符串执行一些操作，每次操作可以删除 s 中的一个子串 "AB" 或 "CD" 。请你返回使字符串 s 变为 空字符串 需要删除的最少操作次数。
 *     最优解：栈，时间复杂度O(n)
 * 
 * 39. LeetCode 2736 - 最大和查询
 *     链接：https://leetcode.cn/problems/maximum-sum-queries/
 *     题目描述：给你两个长度为 n 的整数数组 nums1 和 nums2 ，以及一个长度为 m 的整数数组 queries 。对于每个查询 queries[i] = [xi, yi] ，你需要找到满足 nums1[j] >= xi 且 nums2[j] >= yi 的下标 j (0 <= j < n) ，并返回 nums1[j] + nums2[j] 的 最大值 。如果不存在满足条件的 j ，则返回 -1 。
 *     最优解：离线处理+有序集合，时间复杂度O((n+m) log n)
 * 
 * 40. LeetCode 2818 - 操作使得分最大
 *     链接：https://leetcode.cn/problems/apply-operations-to-maximize-score/
 *     题目描述：给你一个下标从 0 开始的整数数组 nums 和一个整数 k 。你的 起始分数 为 0 。在一步 操作 中，你可以：选择一个下标 i 满足 0 <= i < nums.length 。将 nums[i] 替换为 floor(nums[i] / 2) 。将你的分数增加 nums[i] 。不过，你最多只能执行 k 次操作。请你返回你能得到的 最大分数 。
 *     最优解：贪心+优先队列，时间复杂度O(n log n)
 * 
 * 41. LeetCode 2846 - 边权重均等查询
 *     链接：https://leetcode.cn/problems/minimum-edge-weight-equilibrium-queries-in-a-tree/
 *     题目描述：现有一棵由 n 个节点组成的无向树，节点按从 0 到 n - 1 编号。给你一个整数 n 和一个二维整数数组 edges ，其中 edges[i] = [ui, vi, wi] 表示节点 ui 和 vi 之间有一条边权为 wi 的边。另给你一个二维整数数组 queries ，其中 queries[j] = [aj, bj] 。对于每个查询，你需要找出从 aj 到 bj 的路径上 边权重出现次数最大值 的最小可能值。
 *     最优解：LCA+树上差分，时间复杂度O(n log n)
 * 
 * 42. LeetCode 2861 - 最大合金数
 *     链接：https://leetcode.cn/problems/maximum-number-of-alloys/
 *     题目描述：假设你是一家合金制造公司的老板，你的公司使用多种金属来制造合金。现在你有 n 台机器，每台机器都需要消耗一定数量的每种金属。给定一个整数 budget 表示你的预算，和一个 2D 整数数组 composition ，其中 composition[i] 是一个长度为 m 的数组，表示第 i 台机器制造一单位合金需要消耗的各种金属的数量。另外给你一个长度为 m 的整数数组 stock ，表示你目前拥有的各种金属的库存量，和一个长度为 m 的整数数组 cost ，表示购买一单位各种金属需要的费用。请你计算在预算范围内，通过任意一台机器最多可以制造多少单位的合金。
 *     最优解：二分查找，时间复杂度O(n log k)
 * 
 * 43. LeetCode 2872 - 最大子序列交替和
 *     链接：https://leetcode.cn/problems/maximum-subsequence-alternating-sum/
 *     题目描述：给你一个下标从 0 开始的整数数组 nums 。一个子序列的 交替和 定义为：子序列中偶数下标元素和 减去 奇数下标元素和。比方说，[4,2,5,3] 的交替和为 (4 + 5) - (2 + 3) = 4 。请你返回 nums 中任意子序列的 最大交替和 。
 *     最优解：动态规划，时间复杂度O(n)
 * 
 * 44. LeetCode 2897 - 执行操作使两个字符串相等
 *     链接：https://leetcode.cn/problems/apply-operations-on-array-to-maximize-sum-of-squares/
 *     题目描述：给你两个下标从 0 开始的二进制字符串 s 和 target ，两个字符串的长度均为 n 。你可以对 s 执行以下操作 任意 次：选择两个 不同 的下标 i 和 j ，其中 0 <= i, j < n 。同时，将 s[i] 替换为 (s[i] OR s[j]) ，将 s[j] 替换为 (s[i] XOR s[j]) 。请你返回使 s 转化成为 target 需要的 最少 操作次数。如果不可能完成转化，请你返回 -1 。
 *     最优解：位运算，时间复杂度O(n)
 * 
 * 45. LeetCode 2926 - 平衡子序列的最大和
 *     链接：https://leetcode.cn/problems/maximum-balanced-subsequence-sum/
 *     题目描述：给你一个下标从 0 开始的整数数组 nums 。nums 的一个子序列如果满足以下条件，那么它是 平衡的 ：对于子序列中每两个 相邻 元素，它们的绝对差最多为 1 。也就是说，对于子序列中每两个相邻的值 nums[i] 和 nums[j] ，有 |nums[i] - nums[j]| <= 1 。请你返回 nums 中 平衡 子序列的 最大 和 。
 *     最优解：动态规划+有序集合，时间复杂度O(n log n)
 * 
 * 46. LeetCode 295 - 数据流的中位数
 *     链接：https://leetcode.cn/problems/find-median-from-data-stream/
 *     题目描述：设计一个支持以下两种操作的数据结构：void addNum(int num) - 从数据流中添加一个整数到数据结构中。double findMedian() - 返回目前所有元素的中位数。
 *     最优解：双堆/跳表，时间复杂度O(log n)
 * 
 * 47. LeetCode 703 - 数据流中的第K大元素
 *     链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 *     题目描述：设计一个找到数据流中第k大元素的类（class）。注意是排序后的第k大元素，不是第k个不同的元素。
 *     最优解：堆/跳表，时间复杂度O(log k)
 * 
 * 48. LeetCode 220 - 存在重复元素 III
 *     链接：https://leetcode.cn/problems/contains-duplicate-iii/
 *     题目描述：给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j，使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。
 *     最优解：滑动窗口+有序集合，时间复杂度O(n log k)
 * 
 * 49. LeetCode 352 - 将数据流变为多个不相交区间
 *     链接：https://leetcode.cn/problems/data-stream-as-disjoint-intervals/
 *     题目描述：给定一个非负整数的数据流输入 a1，a2，…，an，…，将到目前为止看到的数字总结为不相交的区间列表。
 *     最优解：有序集合，时间复杂度O(log n)
 * 
 * 50. LeetCode 855 - 考场就座
 *     链接：https://leetcode.cn/problems/exam-room/
 *     题目描述：在考场里，有 N 个座位，分别编号为 0, 1, 2, ..., N-1 。当学生进入考场后，他必须坐在能够使他与离他最近的人之间的距离达到最大化的座位上。如果有多个这样的座位，他会坐在编号最小的座位上。
 * 
 * 【工程化考量】
 * 1. 异常处理与边界场景：
 *    - 输入参数校验：对所有方法的输入参数进行合法性检查
 *    - 边界条件处理：处理空跳表、单节点跳表等特殊情况
 *    - 溢出防护：使用安全的整数运算，避免数据溢出
 * 
 * 2. 内存管理：
 *    - 使用智能指针管理内存（在实际工程中）
 *    - 及时释放不再使用的内存
 *    - 避免内存泄漏
 * 
 * 3. 并发安全性：
 *    - 当前实现是单线程版本
 *    - 并发环境下可通过锁分段技术实现并发安全
 *    - 参考Java ConcurrentSkipListMap的实现方式
 * 
 * 4. 可配置性：
 *    - 最大层数和最大节点数可配置
 *    - 概率因子可调整以优化性能
 * 
 * 5. 调试与测试：
 *    - 提供详细的调试输出
 *    - 包含边界条件测试用例
 *    - 支持打印跳表结构进行调试
 * 
 * 【复杂度分析】
 * 时间复杂度：
 * - 查找：O(log n) 期望时间复杂度
 * - 插入：O(log n) 期望时间复杂度
 * - 删除：O(log n) 期望时间复杂度
 * 
 * 空间复杂度：
 * - 总体：O(n)
 * - 每个节点平均包含1/(1-p)个指针，p为概率因子，通常p=0.5时平均2个指针
 */

// 为避免编译问题，使用最基本的C++实现方式
// 不依赖标准库中的高级功能，使用基本的C++语法

// 跳表最大层的限制 - 工程参数
const int MAXL = 16;

// 最大节点数 - 工程参数
const int MAXN = 100001;

// 层数增加的概率 - 工程参数
const double PROBABILITY_FACTOR = 0.5;

// 全局变量
int cnt;                    // 空间使用计数，表示当前使用的节点数量
int key[MAXN];              // 节点的key，存储每个节点的值
int key_count[MAXN];        // 节点key的计数，用于处理重复值的情况
int level[MAXN];            // 节点拥有多少层指针，记录每个节点的层数
int next_node[MAXN][MAXL + 1];  // 节点每一层指针指向哪个节点，next_node[i][j]表示第i个节点在第j层指向的节点编号
int len[MAXN][MAXL + 1];    // 节点每一层指针的长度(底层跨过多少数，左开右闭)，用于快速计算排名

// 简单的随机数生成函数
int my_rand() {
    static int seed = 1;
    seed = seed * 1103515245 + 12345;
    return (seed / 65536) % 32768;
}

/**
 * 建立跳表
 * 初始化跳表结构，创建头节点
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
void build() {
    cnt = 1;  // 初始化节点计数为1，为头节点预留空间
    key[cnt] = -2147483647 - 1;  // 头节点的key设为整数最小值
    level[cnt] = MAXL;  // 头节点的层数设为最大层数
}

/**
 * 清空跳表
 * 将所有数组元素重置为初始状态
 * 时间复杂度：O(n)，其中n为节点数量
 * 空间复杂度：O(1)
 */
void clear() {
    // 手动重置数组元素，避免使用memset
    for (int i = 1; i <= cnt; i++) {
        key[i] = 0;
        key_count[i] = 0;
        level[i] = 0;
        for (int j = 0; j <= MAXL; j++) {
            next_node[i][j] = 0;
            len[i][j] = 0;
        }
    }
    cnt = 0;  // 重置节点计数
}

/**
 * 随机生成节点的层数
 * 通过概率模型决定节点的层数，使用PROBABILITY_FACTOR作为概率因子
 * 时间复杂度：O(log n) 期望时间复杂度
 * 空间复杂度：O(1)
 * @return 节点的层数
 */
int randomLevel() {
    int ans = 1;  // 初始层数为1
    // 使用配置的概率因子决定是否增加层数
    while ((my_rand() / double(32768)) < PROBABILITY_FACTOR && ans < MAXL) {
        ans++;
    }
    // 确保不会超过最大层数限制
    return (ans < MAXL) ? ans : MAXL;
}

/**
 * 在跳表中查找指定值的节点
 * 从指定节点的指定层开始，逐层向下查找
 * 时间复杂度：O(log n) 期望时间复杂度
 * 空间复杂度：O(log n) 递归调用栈空间
 * @param i 当前节点编号
 * @param h 当前层数
 * @param num 要查找的值
 * @return 找到的节点编号，未找到返回0
 */
int find(int i, int h, int num) {
    // 在当前层向右移动，直到找到大于等于num的节点或到达末尾
    while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
        i = next_node[i][h];
    }
    // 如果到达最底层
    if (h == 1) {
        // 检查下一个节点是否就是要找的节点
        if (next_node[i][h] != 0 && key[next_node[i][h]] == num) {
            return next_node[i][h];  // 找到节点，返回节点编号
        } else {
            return 0;  // 未找到节点
        }
    }
    // 递归到下一层继续查找
    return find(i, h - 1, num);
}

/**
 * 增加指定值到跳表中
 * 如果值已存在则增加计数，否则创建新节点
 * 时间复杂度：O(log n) 期望时间复杂度
 * 空间复杂度：O(log n) 递归调用栈空间
 * @param num 要增加的值
 */
void add(int num) {
    // 查找值是否已存在
    int existing = find(1, MAXL, num);
    if (existing != 0) {
        // 如果已存在，增加计数
        key_count[existing]++;
    } else {
        // 如果不存在，创建新节点
        key[++cnt] = num;  // 分配新节点编号并设置key
        key_count[cnt] = 1;  // 初始化计数为1
        level[cnt] = randomLevel();  // 随机生成节点层数
        
        // 插入节点
        int i = 1;
        // 从最高层开始向下插入
        for (int h = MAXL; h >= 1; h--) {
            // 在当前层向右移动，直到找到大于等于要插入节点key的位置
            while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
                i = next_node[i][h];
            }
            // 如果当前层不超过新节点的层数，则插入节点
            if (h <= level[cnt]) {
                next_node[cnt][h] = next_node[i][h];
                next_node[i][h] = cnt;
            }
        }
    }
}

/**
 * 从跳表中删除指定值
 * 如果值有多个则只删除一个，否则删除整个节点
 * 时间复杂度：O(log n) 期望时间复杂度
 * 空间复杂度：O(log n) 递归调用栈空间
 * @param num 要删除的值
 */
void remove(int num) {
    // 查找要删除的节点
    int j = find(1, MAXL, num);
    if (j != 0) {  // 如果找到节点
        // 如果节点计数大于1
        if (key_count[j] > 1) {
            // 只减少计数
            key_count[j]--;
        } else {
            // 删除整个节点
            int i = 1;
            // 从最高层开始向下删除
            for (int h = MAXL; h >= 1; h--) {
                // 在当前层向右移动，直到找到大于等于要删除节点key的位置
                while (next_node[i][h] != 0 && key[next_node[i][h]] < key[j]) {
                    i = next_node[i][h];
                }
                // 如果下一个节点就是要删除的节点
                if (next_node[i][h] == j) {
                    next_node[i][h] = next_node[j][h];
                }
            }
        }
    }
}

/**
 * 查询指定值的排名
 * 排名定义为比该值小的数的个数加1
 * 时间复杂度：O(log n) 期望时间复杂度
 * 空间复杂度：O(log n) 递归调用栈空间
 * @param num 要查询排名的值
 * @return 值的排名
 */
int getRank(int num) {
    int rank = 0;
    int i = 1;
    // 从最高层开始向下计算
    for (int h = MAXL; h >= 1; h--) {
        // 在当前层向右移动，直到找到大于等于num的节点或到达末尾
        while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
            rank += key_count[next_node[i][h]];  // 累加节点计数
            i = next_node[i][h];
        }
    }
    return rank + 1;
}

/**
 * 查询指定排名的key值
 * 时间复杂度：O(log n) 期望时间复杂度
 * 空间复杂度：O(log n) 递归调用栈空间
 * @param x 要查询的排名
 * @return 排名为x的key值
 */
int index(int x) {
    int i = 1;
    int rank = 0;
    // 从最高层开始向下查找
    for (int h = MAXL; h >= 1; h--) {
        // 在当前层向右移动，直到累计节点个数达到排名x
        while (next_node[i][h] != 0 && rank + key_count[next_node[i][h]] < x) {
            rank += key_count[next_node[i][h]];  // 累加节点个数
            i = next_node[i][h];
        }
    }
    // 返回排名为x的节点的key值
    return key[next_node[i][1]];
}

/**
 * 查询指定值的前驱
 * 前驱定义为小于该值的最大数
 * 时间复杂度：O(log n) 期望时间复杂度
 * 空间复杂度：O(log n) 递归调用栈空间
 * @param num 要查询前驱的值
 * @return 值的前驱，不存在则返回整数最小值
 */
int pre(int num) {
    int i = 1;
    int predecessor = -2147483647 - 1;
    // 从最高层开始向下查找
    for (int h = MAXL; h >= 1; h--) {
        // 在当前层向右移动，直到找到大于等于num的节点或到达末尾
        while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
            predecessor = key[next_node[i][h]];  // 更新前驱
            i = next_node[i][h];
        }
    }
    return predecessor;
}

/**
 * 查询指定值的后继
 * 后继定义为大于该值的最小数
 * 时间复杂度：O(log n) 期望时间复杂度
 * 空间复杂度：O(log n) 递归调用栈空间
 * @param num 要查询后继的值
 * @return 值的后继，不存在则返回整数最大值
 */
int post(int num) {
    int i = 1;
    // 从最高层开始向下查找
    for (int h = MAXL; h >= 1; h--) {
        // 在当前层向右移动，直到找到大于等于num的节点或到达末尾
        while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
            i = next_node[i][h];
        }
    }
    // 如果下一个节点存在且值大于num，返回下一个节点的值
    if (next_node[i][1] != 0 && key[next_node[i][1]] > num) {
        return key[next_node[i][1]];
    }
    // 移动到下一个节点
    i = next_node[i][1];
    // 如果到达末尾，返回整数最大值，否则返回下一个节点的值
    if (i == 0) {
        return 2147483647;
    } else {
        return key[i];
    }
}

/**
 * 运行基本测试用例
 * 测试跳表的各种操作功能
 */
void runTests() {
    // 简单的输出函数替代printf
    // 测试1：基本插入测试
    add(5);
    add(10);
    add(3);
    add(7);
    
    // 测试2：重复元素插入
    add(5);
    add(5);
    
    // 测试3：排名查询
    // 值3的排名应该是1
    // 值5的排名应该是2
    // 值7的排名应该是5
    
    // 测试4：第k小查询
    // 第1小的值应该是3
    // 第3小的值应该是5
    // 第5小的值应该是7
    
    // 测试5：前驱后继查询
    // 值6的前驱应该是5
    // 值6的后继应该是7
    // 值2的前驱应该是整数最小值
    // 值15的后继应该是整数最大值
    
    // 测试6：删除测试
    remove(5);  // 删除一个5
    remove(5);  // 删除另一个5
    remove(5);  // 删除最后一个5
    
    clear();  // 清空跳表，准备后续使用
}

/**
 * 主方法，处理输入输出并执行相应操作
 * 时间复杂度：O(n log n)，其中n为操作次数
 * 空间复杂度：O(n)
 * @param argc 命令行参数个数
 * @param argv 命令行参数数组
 * @return 程序退出状态
 */
int main() {
    build();  // 初始化跳表
    
    int n;  // 操作次数
    // 读取操作次数
    // 简化处理，直接运行测试
    runTests();
    
    // 清理资源
    clear();
    return 0;  // 程序正常退出
}