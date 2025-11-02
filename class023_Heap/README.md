# 堆（优先队列）算法专题

## 概述

堆是一种特殊的完全二叉树数据结构，满足堆属性：
1. 最大堆：任意节点的值 >= 其子节点的值（根节点最大）
2. 最小堆：任意节点的值 <= 其子节点的值（根节点最小）

## 堆的核心操作及时间复杂度

1. 插入元素：O(log n)
2. 获取最值：O(1)
3. 删除最值：O(log n)
4. 建堆：O(n)

## 堆的常见应用场景

1. Top K 问题：找最大/最小的 K 个元素
2. 数据流处理：动态维护最值
3. 优先级队列：按优先级处理任务
4. 调度算法：如操作系统进程调度
5. 图算法：如Dijkstra最短路径算法
6. 合并多个有序序列

## 相关题目平台

1. LeetCode: https://leetcode.cn/tag/heap/
2. 牛客网: https://www.nowcoder.com/
3. LintCode: https://www.lintcode.com/
4. HackerRank: https://www.hackerrank.com/
5. AtCoder: https://atcoder.jp/
6. CodeChef: https://www.codechef.com/
7. SPOJ: https://www.spoj.com/
8. Project Euler: https://projecteuler.net/
9. HackerEarth: https://www.hackerearth.com/
10. 计蒜客: https://www.jisuanke.com/
11. 洛谷: https://www.luogu.com.cn/
12. USACO: http://usaco.org/
13. UVa OJ: https://onlinejudge.org/
14. Codeforces: https://codeforces.com/
15. POJ: http://poj.org/
16. HDU: http://acm.hdu.edu.cn/

## 题目列表与解决方案

### 1. 数组中的第K个最大元素（LeetCode 215）
- 文件: Code01_KthLargestElementInArray.java/Code01_KthLargestElementInArray.cpp/Code01_KthLargestElementInArray.py
- 题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
- 解题思路: 使用最小堆维护最大的k个元素，堆顶即为第k大元素
- 时间复杂度: O(n log k)
- 空间复杂度: O(k)

### 2. 最多线段重合问题
- 文件: Code02_MaxCover.java/Code02_MaxCover.cpp/Code02_MaxCover.py
- 题目链接: https://www.nowcoder.com/practice/1ae8d0b6bb4e4bcdbf64ec491f63fc37
- 解题思路: 按开始时间排序，使用最小堆维护当前活跃的线段结束时间
- 时间复杂度: O(n log n)
- 空间复杂度: O(n)

### 3. 将数组和减半的最少操作次数
- 文件: Code03_MinimumOperationsToHalveArraySum.java/Code03_MinimumOperationsToHalveArraySum.cpp/Code03_MinimumOperationsToHalveArraySum.py
- 题目链接: https://leetcode.cn/problems/minimum-operations-to-halve-array-sum/
- 解题思路: 使用最大堆每次取出最大元素减半，直到总和减半
- 时间复杂度: O(n log n)
- 空间复杂度: O(n)

### 4. 数组中的第K个最大元素（优化版）
- 文件: Code04_KthLargestElementInArray.java/Code04_KthLargestElementInArray.cpp/Code04_KthLargestElementInArray.py
- 题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
- 解题思路: 优化的快速选择算法或堆排序实现
- 时间复杂度: O(n log k)
- 空间复杂度: O(k)

### 5. 前 K 个高频元素
- 文件: Code05_TopKFrequentElements.java/Code05_TopKFrequentElements.cpp/Code05_TopKFrequentElements.py
- 题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
- 解题思路: 哈希表统计频率，最小堆找出高频元素
- 时间复杂度: O(n log k)
- 空间复杂度: O(n + k)

### 6. 数据流的中位数
- 文件: Code06_FindMedianFromDataStream.java/Code06_FindMedianFromDataStream.cpp/Code06_FindMedianFromDataStream.py
- 题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
- 解题思路: 使用最大堆存储较小一半元素，最小堆存储较大一半元素
- 时间复杂度: addNum: O(log n), findMedian: O(1)
- 空间复杂度: O(n)

### 7. 滑动窗口最大值
- 文件: Code07_SlidingWindowMaximum.java/Code07_SlidingWindowMaximum.cpp/Code07_SlidingWindowMaximum.py
- 题目链接: https://leetcode.cn/problems/sliding-window-maximum/
- 解题思路: 双端队列实现单调队列，保持队首为当前窗口最大值
- 时间复杂度: O(n)
- 空间复杂度: O(k)

### 8. 数据流的第K大元素
- 文件: Code08_KthLargestElementInStream.java/Code08_KthLargestElementInStream.cpp/Code08_KthLargestElementInStream.py
- 题目链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
- 解题思路: 最小堆维护最大的k个元素
- 时间复杂度: 初始化: O(n log k), 添加元素: O(log k)
- 空间复杂度: O(k)

### 9. 最接近原点的 K 个点
- 文件: Code09_KClosestPointsToOrigin.java/Code09_KClosestPointsToOrigin.cpp/Code09_KClosestPointsToOrigin.py
- 题目链接: https://leetcode.cn/problems/k-closest-points-to-origin/
- 解题思路: 最大堆维护距离最近的k个点
- 时间复杂度: O(n log k)
- 空间复杂度: O(k)

### 10. 最多线段重合问题
- 文件: Code10_MaxCovers.java/Code10_MaxCovers.cpp/Code10_MaxCovers.py
- 题目链接: https://www.nowcoder.com/practice/1ae8d0b6bb4e4bcdbf64ec491f63fc37
- 解题思路: 扫描线算法结合最小堆
- 时间复杂度: O(n log n)
- 空间复杂度: O(n)

### 11. 丑数 II
- 文件: Code11_UglyNumberII.java/Code11_UglyNumberII.cpp/Code11_UglyNumberII.py
- 题目链接: https://leetcode.cn/problems/ugly-number-ii/
- 解题思路: 最小堆生成丑数序列或动态规划
- 时间复杂度: O(n log n)
- 空间复杂度: O(n)

### 12. 重构字符串
- 文件: Code13_RearrangeString.java/Code13_RearrangeString.cpp/Code13_RearrangeString.py
- 题目链接: https://leetcode.cn/problems/reorganize-string/
- 解题思路: 使用最大堆按字符频率排序，然后贪心选择频率最高的字符进行放置
- 时间复杂度: O(n log k)，其中n是字符串长度，k是不同字符的数量
- 空间复杂度: O(k)

### 13. 任务调度器
- 文件: Code14_TaskScheduler.java/Code14_TaskScheduler.cpp/Code14_TaskScheduler.py
- 题目链接: https://leetcode.cn/problems/task-scheduler/
- 解题思路: 使用最大堆按任务频率排序，然后贪心安排任务
- 时间复杂度: O(m log k)，其中m是任务总数，k是不同任务的数量
- 空间复杂度: O(k)

### 14. 寻找第K大的异或坐标值
- 文件: Code15_FindKthLargestXORCoordinateValue.java/Code15_FindKthLargestXORCoordinateValue.cpp/Code15_FindKthLargestXORCoordinateValue.py
- 题目链接: https://leetcode.cn/problems/find-kth-largest-xor-coordinate-value/
- 解题思路: 二维前缀异或和结合最小堆
- 时间复杂度: O(m*n log k)
- 空间复杂度: O(k)

### 15. 分割数组为连续子序列
- 文件: Code16_SplitArrayIntoConsecutiveSubsequences.java/Code16_SplitArrayIntoConsecutiveSubsequences.cpp/Code16_SplitArrayIntoConsecutiveSubsequences.py
- 题目链接: https://leetcode.cn/problems/split-array-into-consecutive-subsequences/
- 解题思路: 哈希表+最小堆贪心策略
- 时间复杂度: O(n log n)
- 空间复杂度: O(n)

### 16. 超级丑数（LeetCode 313）
- 文件: Code23_SuperUglyNumber.java/Code23_SuperUglyNumber.cpp/Code23_SuperUglyNumber.py
- 题目链接: https://leetcode.cn/problems/super-ugly-number/
- 解题思路: 使用最小堆生成超级丑数序列或动态规划
- 时间复杂度: O(n log k)，其中k是primes数组的长度
- 空间复杂度: O(n)

### 17. 数据流的中位数（LeetCode 295）
- 文件: Code24_FindMedianFromDataStream.java/Code24_FindMedianFromDataStream.cpp/Code24_FindMedianFromDataStream.py
- 题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
- 解题思路: 使用两个堆（最大堆和最小堆）维护数据流的中位数
- 时间复杂度: addNum() O(log n)，findMedian() O(1)
- 空间复杂度: O(n)

### 18. 前K个高频元素（LeetCode 347）
- 文件: Code25_TopKFrequentElements.java/Code25_TopKFrequentElements.cpp/Code25_TopKFrequentElements.py
- 题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
- 解题思路: 使用最小堆维护前k个高频元素
- 时间复杂度: O(n log k)
- 空间复杂度: O(n)

### 19. 数组中的第K个最大元素（LeetCode 215）
- 文件: Code26_KthLargestElementInAnArray.java/Code26_KthLargestElementInAnArray.cpp/Code26_KthLargestElementInAnArray.py
- 题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
- 解题思路: 使用最小堆维护前k个最大元素
- 时间复杂度: O(n log k)
- 空间复杂度: O(k)

### 20. 设计推特（LeetCode 355）
- 文件: Code17_Twitter.java/Code17_Twitter.cpp/Code17_Twitter.py
- 题目链接: https://leetcode.cn/problems/design-twitter/
- 解题思路: 使用堆来合并多个用户的推文时间线
- 时间复杂度: postTweet: O(1), getNewsFeed: O(k log n)，其中k是关注的用户数，n是推文总数
- 空间复杂度: O(n + m)，其中n是推文数，m是用户数

### 21. 滑动窗口最大值（LeetCode 239）
- 文件: Code17_SlidingWindowMaximum.java/Code17_SlidingWindowMaximum.cpp/Code17_SlidingWindowMaximum.py
- 题目链接: https://leetcode.cn/problems/sliding-window-maximum/
- 解题思路: 使用最大堆维护当前窗口内的元素，堆顶始终是最大值
- 时间复杂度: O(n log k)，每个元素入堆和出堆的时间复杂度为O(log k)
- 空间复杂度: O(k)，堆中最多存储k个元素

### 22. 合并K个排序链表（LeetCode 23）
- 文件: Code18_MergeKSortedLists.java/Code18_MergeKSortedLists.cpp/Code18_MergeKSortedLists.py
- 题目链接: https://leetcode.cn/problems/merge-k-sorted-lists/
- 解题思路: 使用最小堆维护K个链表的头节点，每次从堆中取出最小值
- 时间复杂度: O(N log K)，其中N是所有节点的总数，K是链表的数量
- 空间复杂度: O(K)，堆中最多存储K个节点

### 23. 前K个高频元素（LeetCode 347）
- 文件: Code19_TopKFrequentElements.java/Code19_TopKFrequentElements.cpp/Code19_TopKFrequentElements.py
- 文件: Code25_TopKFrequentElements.java/Code25_TopKFrequentElements.cpp/Code25_TopKFrequentElements.py
- 题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
- 解题思路: 使用哈希表统计频率，最小堆筛选出频率最高的k个元素
- 时间复杂度: O(n log k)，其中n是数组长度
- 空间复杂度: O(n)，哈希表需要O(n)空间，堆需要O(k)空间

### 24. 数据流的中位数（LeetCode 295）
- 文件: Code20_FindMedianFromDataStream.java/Code20_FindMedianFromDataStream.cpp/Code20_FindMedianFromDataStream.py
- 文件: Code24_FindMedianFromDataStream.java/Code24_FindMedianFromDataStream.cpp/Code24_FindMedianFromDataStream.py
- 题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
- 解题思路: 使用最大堆存储较小的一半元素，最小堆存储较大的一半元素
- 时间复杂度: addNum() O(log n)，findMedian() O(1)
- 空间复杂度: O(n)，其中n是数据流中的元素个数

### 25. 数据流中的第K大元素（LeetCode 703）
- 文件: Code21_KthLargestElementInAStream.java/Code21_KthLargestElementInAStream.cpp/Code21_KthLargestElementInAStream.py
- 题目链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
- 解题思路: 使用最小堆维护数据流中最大的k个元素，堆顶即为第k大元素
- 时间复杂度: add() O(log k)，初始化 O(n log k)
- 空间复杂度: O(k)，堆中最多存储k个元素

### 26. 丑数II（LeetCode 264）
- 文件: Code22_UglyNumberII.java/Code22_UglyNumberII.cpp/Code22_UglyNumberII.py
- 题目链接: https://leetcode.cn/problems/ugly-number-ii/
- 解题思路: 使用最小堆生成丑数序列，或者使用动态规划维护三个指针
- 时间复杂度: 最小堆O(n log n)，动态规划O(n)
- 空间复杂度: 最小堆O(n)，动态规划O(n)

### 27. 超级丑数（LeetCode 313）
- 文件: Code23_SuperUglyNumber.java/Code23_SuperUglyNumber.cpp/Code23_SuperUglyNumber.py
- 题目链接: https://leetcode.cn/problems/super-ugly-number/
- 解题思路: 使用最小堆生成超级丑数序列，或者使用动态规划为每个质数维护指针
- 时间复杂度: 最小堆O(n log k)，动态规划O(nk)，其中k是primes数组的长度
- 空间复杂度: 最小堆O(n)，动态规划O(n + k)

### 28. 数组中的第K个最大元素（LeetCode 215）
- 文件: Code26_KthLargestElementInAnArray.java/Code26_KthLargestElementInAnArray.cpp/Code26_KthLargestElementInAnArray.py
- 题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
- 解题思路: 使用最小堆维护前k个最大元素，或者使用快速选择算法
- 时间复杂度: 最小堆O(n log k)，快速选择平均O(n)，最坏O(n²)
- 空间复杂度: 最小堆O(k)，快速选择O(1)（原地版本）

## 扩展题目集（新增）

### 29. 堆算法扩展题目集（27个新增题目）
- 文件: Code27_HeapExtendedProblems.java/Code27_HeapExtendedProblems.cpp/Code27_HeapExtendedProblems.py
- 包含题目: 
  1. LeetCode 378. 有序矩阵中第K小的元素
  2. LeetCode 767. 重构字符串
  3. LeetCode 502. IPO
  4. LeetCode 630. 课程表 III
  5. LeetCode 857. 雇佣 K 名工人的最低成本
  6. LeetCode 1054. 距离相等的条形码
  7. LeetCode 1383. 最大的团队表现值
  8. LeetCode 1642. 可以到达的最远建筑
  9. LeetCode 1705. 吃苹果的最大数目
  10. LeetCode 1834. 单线程 CPU
- 解题思路: 涵盖贪心算法、调度问题、数据流处理等多种堆应用场景
- 时间复杂度: 从O(n log k)到O(n log n)不等
- 空间复杂度: 从O(k)到O(n)不等

### 30. 更多堆算法题目集（20个新增题目）
- 文件: Code28_MoreHeapProblems.java/Code28_MoreHeapProblems.cpp/Code28_MoreHeapProblems.py
- 包含题目: 
  1. 牛客网 - 最多线段重合问题（优化版）
  2. LintCode 104. 合并k个排序链表
  3. HackerRank - 查找运行中位数
  4. AtCoder - 最小成本连接点
  5. CodeChef - 厨师和食谱
  6. SPOJ - 军事调度
  7. Project Euler - 高度合成数
  8. HackerEarth - 最小化最大延迟
  9. 计蒜客 - 任务调度器
  10. 洛谷 - 合并果子
- 解题思路: 涵盖各大算法平台的经典堆问题
- 时间复杂度: 从O(n log k)到O(n log n)不等
- 空间复杂度: 从O(k)到O(n)不等

## 堆算法总结与技巧

### 1. 识别堆问题的关键特征
- "前 K 大/小"元素问题
- "动态最值"维护需求
- "数据流"处理场景
- "频率排序"需求
- "实时最优解"要求
- "合并多个有序序列"需求

### 2. 堆类型选择策略
- 找最大K个元素：使用大小为K的最小堆
- 找最小K个元素：使用大小为K的最大堆
- 维护当前最大值：使用最大堆
- 维护当前最小值：使用最小堆
- 数据流中位数：使用双堆结构（最大堆+最小堆）

### 3. 时间复杂度分析
- 插入/删除操作：O(log n)
- 获取最值操作：O(1)
- 建堆操作：O(n)
- Top K问题：O(n log k)
- 数据流处理：O(n log n)

### 4. 空间复杂度优化
- 控制堆大小以优化内存使用
- 避免不必要的对象创建
- 合理选择堆的实现方式

### 5. 工程化考量
- 异常处理：空输入、边界条件处理
- 性能优化：避免重复计算，缓存结果
- 可配置性：自定义比较器支持不同排序需求
- 线程安全：多线程环境下的同步机制
- 代码可读性：清晰的命名和注释
- 单元测试：覆盖各种边界情况

### 6. 调试技巧
- 打印堆状态用于调试
- 使用断言验证中间结果
- 性能分析工具定位瓶颈
- 小例子测试法定位逻辑漏洞

### 7. 跨语言特性对比
- Java: PriorityQueue（默认最小堆）
- Python: heapq模块（最小堆）
- C++: priority_queue（默认最大堆）
- 注意各语言堆实现的差异和特性

### 8. 与标准库对比
- 理解标准库实现的优势和局限性
- 在性能要求极高时考虑自实现堆
- 关注标准库的边界处理和异常防御机制

## 学习建议

1. **基础掌握**：先熟练掌握堆的基本操作和经典应用
2. **题型分类**：将堆问题按应用场景分类学习
3. **多解法对比**：对比堆解法与其他解法的优劣
4. **实战练习**：在各大算法平台进行大量练习
5. **总结归纳**：总结各类堆问题的解题模板和技巧

通过系统学习本专题的所有题目，您将能够：
- 熟练运用堆解决各类算法问题
- 理解堆在不同场景下的应用原理
- 掌握堆算法的时间和空间复杂度分析
- 具备解决复杂堆相关问题的能力
- 在算法竞赛和面试中游刃有余