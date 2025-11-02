# 贪心算法专题

本目录包含了一系列使用贪心算法解决的经典问题，涵盖了跳跃游戏、区间覆盖、字符串映射、过河问题和资源平衡分配等多个方面。

## 题目列表

### 1. 跳跃游戏 II (Jump Game II)
- **文件**: Code01_JumpGameII.java, Code01_JumpGameII.py, Code01_JumpGameII.cpp
- **题目来源**: LeetCode 45
- **题目链接**: https://leetcode.cn/problems/jump-game-ii/
- **问题描述**: 给定一个长度为n的整数数组nums，初始在0下标，nums[i]表示可以从i下标往右跳的最大距离，返回到达n-1下标的最少跳跃次数。

### 2. 灌溉花园的最少水龙头数目 (Minimum Number of Taps to Open to Water a Garden)
- **文件**: Code02_MinimumTaps.java, Code02_MinimumTaps.py, Code02_MinimumTaps.cpp
- **题目来源**: LeetCode 1326
- **题目链接**: https://leetcode.cn/problems/minimum-number-of-taps-to-open-to-water-a-garden/
- **问题描述**: 在x轴上有一个一维的花园，花园长度为n，从点0开始，到点n结束。花园里总共有n+1个水龙头，分别位于[0, 1, ... n]。给定整数n和长度为n+1的整数数组ranges，其中ranges[i]表示如果打开点i处的水龙头，可以灌溉的区域为[i-ranges[i], i+ranges[i]]，返回可以灌溉整个花园的最少水龙头数目。

### 3. 字符串转化 (String Transforms Into Another String)
- **文件**: Code03_StringTransforms.java, Code03_StringTransforms.py, Code03_StringTransforms.cpp
- **题目来源**: LeetCode 917
- **题目链接**: https://leetcode.cn/problems/string-transforms-into-another-string/
- **问题描述**: 给出两个长度相同的字符串str1和str2，判断字符串str1能不能在零次或多次转化后变成字符串str2。每一次转化时，可以将str1中出现的所有相同字母变成其他任何小写英文字母。

### 4. 过河问题 (Cross River)
- **文件**: Code04_CrossRiver.java, Code04_CrossRiver.py, Code04_CrossRiver.cpp
- **题目来源**: 洛谷 P1809
- **题目链接**: https://www.luogu.com.cn/problem/P1809
- **问题描述**: 一共n人出游，他们走到一条河的西岸，想要过河到东岸。每个人都有一个渡河时间ti，西岸有一条船，一次最多乘坐两人。如果船上有一个人，划到对岸的时间等于这个人的渡河时间；如果船上有两个人，划到对岸的时间等于两个人的渡河时间的最大值。返回最少要花费多少时间，才能使所有人都过河。

### 5. 超级洗衣机 (Super Washing Machines)
- **文件**: Code05_SuperWashingMachines.java, Code05_SuperWashingMachines.py, Code05_SuperWashingMachines.cpp
- **题目来源**: LeetCode 517
- **题目链接**: https://leetcode.cn/problems/super-washing-machines/
- **问题描述**: 假设有n台超级洗衣机放在同一排上，开始的时候，每台洗衣机内可能有一定量的衣服，也可能是空的。在每一步操作中，可以选择任意m (1 <= m <= n)台洗衣机，与此同时将每台洗衣机的一件衣服送到相邻的一台洗衣机。给定一个整数数组machines代表从左至右每台洗衣机中的衣物数量，给出能让所有洗衣机中剩下的衣物的数量相等的最少的操作步数。

## 补充题目列表

### 6. 分发饼干 (Assign Cookies)
- **题目来源**: LeetCode 455
- **题目链接**: https://leetcode.cn/problems/assign-cookies/
- **问题描述**: 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。每个孩子最多只能给一块饼干。对每个孩子i，都有一个胃口值g[i]，这是能让孩子们满足胃口的最小尺寸。分配饼干使最多孩子满足。

### 7. 柠檬水找零 (Lemonade Change)
- **题目来源**: LeetCode 860
- **题目链接**: https://leetcode.cn/problems/lemonade-change/
- **问题描述**: 每杯柠檬水售价5美元，顾客支付5、10或20美元。初始时没有零钱，需要判断是否能给每个顾客正确找零。

### 8. 跳跃游戏 (Jump Game)
- **题目来源**: LeetCode 55
- **题目链接**: https://leetcode.cn/problems/jump-game/
- **问题描述**: 给定一个非负整数数组，你最初位于数组的第一个位置。数组中的每个元素代表你在该位置可以跳跃的最大长度。判断你是否能够到达最后一个位置。

### 9. 加油站 (Gas Station)
- **题目来源**: LeetCode 134
- **题目链接**: https://leetcode.cn/problems/gas-station/

### 10. 分发糖果 (Candy)
- **题目来源**: LeetCode 135
- **题目链接**: https://leetcode.cn/problems/candy/

### 11. 无重叠区间 (Non-overlapping Intervals)
- **题目来源**: LeetCode 435
- **题目链接**: https://leetcode.cn/problems/non-overlapping-intervals/

### 12. 用最少数量的箭引爆气球 (Minimum Number of Arrows to Burst Balloons)
- **题目来源**: LeetCode 452
- **题目链接**: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/

### 13. 最大子数组和 (Maximum Subarray)
- **题目来源**: LeetCode 53
- **题目链接**: https://leetcode.cn/problems/maximum-subarray/

### 14. 买卖股票的最佳时机 II (Best Time to Buy and Sell Stock II)
- **题目来源**: LeetCode 122
- **题目链接**: https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

### 15. 合并区间 (Merge Intervals)
- **题目来源**: LeetCode 56
- **题目链接**: https://leetcode.cn/problems/merge-intervals/

### 16. 根据身高重建队列 (Queue Reconstruction by Height)
- **题目来源**: LeetCode 406
- **题目链接**: https://leetcode.cn/problems/queue-reconstruction-by-height/

### 17. 最低加油次数 (Minimum Number of Refueling Stops)
- **题目来源**: LeetCode 871
- **题目链接**: https://leetcode.cn/problems/minimum-number-of-refueling-stops/

### 18. 最大数 (Largest Number)
- **题目来源**: LeetCode 179
- **题目链接**: https://leetcode.cn/problems/largest-number/

### 19. 摆动序列 (Wiggle Subsequence)
- **题目来源**: LeetCode 376
- **题目链接**: https://leetcode.cn/problems/wiggle-subsequence/

### 20. 单调递增的数字 (Monotone Increasing Digits)
- **题目来源**: LeetCode 738
- **题目链接**: https://leetcode.cn/problems/monotone-increasing-digits/

### 21. 划分字母区间 (Partition Labels)
- **题目来源**: LeetCode 763
- **题目链接**: https://leetcode.cn/problems/partition-labels/

### 22. 森林中的兔子 (Rabbits in Forest)
- **题目来源**: LeetCode 781
- **题目链接**: https://leetcode.cn/problems/rabbits-in-forest/

### 23. 合并果子 (Merge Fruits)
- **题目来源**: 洛谷 P1090
- **题目链接**: https://www.luogu.com.cn/problem/P1090

### 24. 排队接水 (Queue for Water)
- **题目来源**: 洛谷 P1223
- **题目链接**: https://www.luogu.com.cn/problem/P1223

### 25. 凌乱的yyy/线段覆盖 (Messy yyy/Segment Coverage)
- **题目来源**: 洛谷 P1803
- **题目链接**: https://www.luogu.com.cn/problem/P1803

### 26. 加油站 (Gas Station)
- **文件**: Code11_GasStation.java, Code11_GasStation.py, Code11_GasStation.cpp
- **题目来源**: LeetCode 134
- **题目链接**: https://leetcode.cn/problems/gas-station/
- **问题描述**: 在一条环路上有N个加油站，每个加油站有汽油gas[i]和消耗cost[i]。从某个加油站出发，按顺序访问每个加油站，判断是否能绕环路行驶一周。

### 27. 分发糖果 (Candy)
- **文件**: Code12_Candy.java, Code12_Candy.py, Code12_Candy.cpp
- **题目来源**: LeetCode 135
- **题目链接**: https://leetcode.cn/problems/candy/
- **问题描述**: 老师想给孩子们分发糖果，有N个孩子站成了一条直线，每个孩子至少分配到1个糖果。相邻的孩子中，评分高的孩子必须获得更多的糖果。

### 28. 无重叠区间 (Non-overlapping Intervals)
- **文件**: Code13_NonOverlappingIntervals.java, Code13_NonOverlappingIntervals.py, Code13_NonOverlappingIntervals.cpp
- **题目来源**: LeetCode 435
- **题目链接**: https://leetcode.cn/problems/non-overlapping-intervals/
- **问题描述**: 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。

### 29. 用最少数量的箭引爆气球 (Minimum Number of Arrows to Burst Balloons)
- **文件**: Code14_MinimumArrowsToBurstBalloons.java, Code14_MinimumArrowsToBurstBalloons.py, Code14_MinimumArrowsToBurstBalloons.cpp
- **题目来源**: LeetCode 452
- **题目链接**: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
- **问题描述**: 在二维空间中有许多球形的气球，每个气球在水平方向上的直径范围是[xstart, xend]。用最少数量的箭引爆所有气球。

### 30. 最大子数组和 (Maximum Subarray)
- **文件**: Code15_MaximumSubarray.java, Code15_MaximumSubarray.py, Code15_MaximumSubarray.cpp
- **题目来源**: LeetCode 53
- **题目链接**: https://leetcode.cn/problems/maximum-subarray/
- **问题描述**: 给定一个整数数组nums，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。

### 31. 合并区间 (Merge Intervals)
- **文件**: Code16_MergeIntervals.java, Code16_MergeIntervals.py, Code16_MergeIntervals.cpp
- **题目来源**: LeetCode 56
- **题目链接**: https://leetcode.cn/problems/merge-intervals/
- **问题描述**: 以数组intervals表示若干个区间的集合，请合并所有重叠的区间，并返回一个不重叠的区间数组。

### 32. 根据身高重建队列 (Queue Reconstruction by Height)
- **文件**: Code17_QueueReconstructionByHeight.java, Code17_QueueReconstructionByHeight.py, Code17_QueueReconstructionByHeight.cpp
- **题目来源**: LeetCode 406
- **题目链接**: https://leetcode.cn/problems/queue-reconstruction-by-height/
- **问题描述**: 假设有打乱顺序的一群人站成一个队列，每个人由一个整数对(h, k)表示，其中h是这个人的身高，k是排在这个人前面且身高大于或等于h的人数。

### 33. 最低加油次数 (Minimum Number of Refueling Stops)
- **文件**: Code18_MinimumRefuelingStops.java, Code18_MinimumRefuelingStops.py, Code18_MinimumRefuelingStops.cpp
- **题目来源**: LeetCode 871
- **题目链接**: https://leetcode.cn/problems/minimum-number-of-refueling-stops/
- **问题描述**: 汽车从起点出发驶向目的地，该目的地距离起点target英里。沿途有加油站，每个station[i]代表一个加油站，位于距离起点station[i][0]英里处，有station[i][1]升汽油。

### 34. 最大数 (Largest Number)
- **文件**: Code19_LargestNumber.java, Code19_LargestNumber.py, Code19_LargestNumber.cpp
- **题目来源**: LeetCode 179
- **题目链接**: https://leetcode.cn/problems/largest-number/
- **问题描述**: 给定一组非负整数nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。

### 35. 摆动序列 (Wiggle Subsequence)
- **文件**: Code20_WiggleSubsequence.java, Code20_WiggleSubsequence.py, Code20_WiggleSubsequence.cpp
- **题目来源**: LeetCode 376
- **题目链接**: https://leetcode.cn/problems/wiggle-subsequence/
- **问题描述**: 如果连续数字之间的差严格地在正数和负数之间交替，则数字序列称为摆动序列。求最长摆动子序列的长度。

### 36. 单调递增的数字 (Monotone Increasing Digits)
- **文件**: Code21_MonotoneIncreasingDigits.java, Code21_MonotoneIncreasingDigits.py, Code21_MonotoneIncreasingDigits.cpp
- **题目来源**: LeetCode 738
- **题目链接**: https://leetcode.cn/problems/monotone-increasing-digits/
- **问题描述**: 给定一个非负整数N，找出小于或等于N的最大的整数，同时这个整数需要满足其各个位数上的数字是单调递增。

### 37. 划分字母区间 (Partition Labels)
- **文件**: Code22_PartitionLabels.java, Code22_PartitionLabels.py, Code22_PartitionLabels.cpp
- **题目来源**: LeetCode 763
- **题目链接**: https://leetcode.cn/problems/partition-labels/
- **问题描述**: 字符串S由小写字母组成。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。

### 38. 森林中的兔子 (Rabbits in Forest)
- **文件**: Code23_RabbitsInForest.java, Code23_RabbitsInForest.py, Code23_RabbitsInForest.cpp
- **题目来源**: LeetCode 781
- **题目链接**: https://leetcode.cn/problems/rabbits-in-forest/
- **问题描述**: 森林中，每个兔子都有颜色。其中一些兔子（可能是全部）告诉你还有多少其他的兔子和自己有相同的颜色。

### 39. 合并果子 (Merge Fruits)
- **文件**: Code24_MergeFruits.java, Code24_MergeFruits.py, Code24_MergeFruits.cpp
- **题目来源**: 洛谷 P1090
- **题目链接**: https://www.luogu.com.cn/problem/P1090
- **问题描述**: 在一个果园里，多多已经将所有的果子打了下来，而且按果子的不同种类分成了不同的堆。多多决定把所有的果子合成一堆。

### 40. 排队接水 (Queue for Water)
- **文件**: Code25_QueueForWater.java, Code25_QueueForWater.py, Code25_QueueForWater.cpp
- **题目来源**: 洛谷 P1223
- **题目链接**: https://www.luogu.com.cn/problem/P1223
- **问题描述**: 有n个人在一个水龙头前排队接水，假如每个人接水的时间为Ti，请编程找出这n个人排队的一种顺序，使得n个人的平均等待时间最小。

### 41. 凌乱的yyy/线段覆盖 (Messy yyy/Segment Coverage)
- **文件**: Code26_SegmentCoverage.java, Code26_SegmentCoverage.py, Code26_SegmentCoverage.cpp
- **题目来源**: 洛谷 P1803
- **题目链接**: https://www.luogu.com.cn/problem/P1803
- **问题描述**: 现在各大oj上有n个比赛，每个比赛的开始、结束的时间点是知道的。yyy参加比赛的策略是：参加尽可能多的比赛。

### 11. 加油站 (Gas Station)
- **文件**: Code11_GasStation.java, Code11_GasStation.py, Code11_GasStation.cpp
- **题目来源**: LeetCode 134
- **题目链接**: https://leetcode.cn/problems/gas-station/
- **问题描述**: 在一条环路上有N个加油站，每个加油站有汽油gas[i]和消耗cost[i]。从某个加油站出发，按顺序访问每个加油站，判断是否能绕环路行驶一周。

### 12. 分发糖果 (Candy)
- **文件**: Code12_Candy.java, Code12_Candy.py, Code12_Candy.cpp
- **题目来源**: LeetCode 135
- **题目链接**: https://leetcode.cn/problems/candy/
- **问题描述**: 老师想给孩子们分发糖果，有N个孩子站成了一条直线，每个孩子至少分配到1个糖果。相邻的孩子中，评分高的孩子必须获得更多的糖果。

### 13. 无重叠区间 (Non-overlapping Intervals)
- **文件**: Code13_NonOverlappingIntervals.java, Code13_NonOverlappingIntervals.py, Code13_NonOverlappingIntervals.cpp
- **题目来源**: LeetCode 435
- **题目链接**: https://leetcode.cn/problems/non-overlapping-intervals/
- **问题描述**: 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。

### 14. 用最少数量的箭引爆气球 (Minimum Number of Arrows to Burst Balloons)
- **文件**: Code14_MinimumArrowsToBurstBalloons.java, Code14_MinimumArrowsToBurstBalloons.py, Code14_MinimumArrowsToBurstBalloons.cpp
- **题目来源**: LeetCode 452
- **题目链接**: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
- **问题描述**: 在二维空间中有许多球形的气球，每个气球在水平方向上的直径范围是[xstart, xend]。用最少数量的箭引爆所有气球。

### 15. 最大子数组和 (Maximum Subarray)
- **文件**: Code15_MaximumSubarray.java, Code15_MaximumSubarray.py, Code15_MaximumSubarray.cpp
- **题目来源**: LeetCode 53
- **题目链接**: https://leetcode.cn/problems/maximum-subarray/
- **问题描述**: 给定一个整数数组nums，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。

### 16. 合并区间 (Merge Intervals)
- **文件**: Code16_MergeIntervals.java, Code16_MergeIntervals.py, Code16_MergeIntervals.cpp
- **题目来源**: LeetCode 56
- **题目链接**: https://leetcode.cn/problems/merge-intervals/
- **问题描述**: 以数组intervals表示若干个区间的集合，请合并所有重叠的区间，并返回一个不重叠的区间数组。

### 17. 根据身高重建队列 (Queue Reconstruction by Height)
- **文件**: Code17_QueueReconstructionByHeight.java, Code17_QueueReconstructionByHeight.py, Code17_QueueReconstructionByHeight.cpp
- **题目来源**: LeetCode 406
- **题目链接**: https://leetcode.cn/problems/queue-reconstruction-by-height/
- **问题描述**: 假设有打乱顺序的一群人站成一个队列，每个人由一个整数对(h, k)表示，其中h是这个人的身高，k是排在这个人前面且身高大于或等于h的人数。

### 18. 最低加油次数 (Minimum Number of Refueling Stops)
- **文件**: Code18_MinimumRefuelingStops.java, Code18_MinimumRefuelingStops.py, Code18_MinimumRefuelingStops.cpp
- **题目来源**: LeetCode 871
- **题目链接**: https://leetcode.cn/problems/minimum-number-of-refueling-stops/
- **问题描述**: 汽车从起点出发驶向目的地，该目的地距离起点target英里。沿途有加油站，每个station[i]代表一个加油站，位于距离起点station[i][0]英里处，有station[i][1]升汽油。

### 19. 任务调度器 (Task Scheduler)
- **文件**: Code19_TaskScheduler.java, Code19_TaskScheduler.py, Code19_TaskScheduler.cpp
- **题目来源**: LeetCode 621
- **题目链接**: https://leetcode.cn/problems/task-scheduler/
- **问题描述**: 给定一个用字符数组表示的CPU需要执行的任务列表。其中包含使用大写的A-Z字母表示的26种不同种类的任务。

### 20. 移掉K位数字 (Remove K Digits)
- **文件**: Code20_RemoveKDigits.java, Code20_RemoveKDigits.py, Code20_RemoveKDigits.cpp
- **题目来源**: LeetCode 402
- **题目链接**: https://leetcode.cn/problems/remove-k-digits/
- **问题描述**: 给定一个以字符串表示的非负整数num，移除这个数中的k位数字，使得剩下的数字最小。

### 43. 重构字符串 (Reorganize String)
- **文件**: Code28_ReorganizeString.java, Code28_ReorganizeString.py, Code28_ReorganizeString.cpp
- **题目来源**: LeetCode 767
- **题目链接**: https://leetcode.cn/problems/reorganize-string/
- **问题描述**: 给定一个字符串S，检查是否能重新排布其中的字母，使得两相邻的字符不同。

### 44. 优势洗牌 (Advantage Shuffle)
- **文件**: Code29_AdvantageShuffle.java, Code29_AdvantageShuffle.py, Code29_AdvantageShuffle.cpp
- **题目来源**: LeetCode 870
- **题目链接**: https://leetcode.cn/problems/advantage-shuffle/
- **问题描述**: 给定两个大小相等的数组A和B，A相对于B的优势可以用满足A[i] > B[i]的索引i的数目来描述。

### 45. 救生艇 (Boats to Save People)
- **文件**: Code30_BoatsToSavePeople.java, Code30_BoatsToSavePeople.py, Code30_BoatsToSavePeople.cpp
- **题目来源**: LeetCode 881
- **题目链接**: https://leetcode.cn/problems/boats-to-save-people/
- **问题描述**: 第i个人的体重为people[i]，每艘船可以承载的最大重量为limit。每艘船最多可同时载两人，但条件是这些人的重量之和最多为limit。

### 46. 视频拼接 (Video Stitching)
- **文件**: Code31_VideoStitching.java, Code31_VideoStitching.py, Code31_VideoStitching.cpp
- **题目来源**: LeetCode 1024
- **题目链接**: https://leetcode.cn/problems/video-stitching/
- **问题描述**: 你将会获得一系列视频片段，这些片段来自于一项持续时长为T秒的体育赛事。这些片段可能有所重叠，也可能长度不一。

### 47. 删除被覆盖区间 (Remove Covered Intervals)
- **文件**: Code32_RemoveCoveredIntervals.java, Code32_RemoveCoveredIntervals.py, Code32_RemoveCoveredIntervals.cpp
- **题目来源**: LeetCode 1288
- **题目链接**: https://leetcode.cn/problems/remove-covered-intervals/
- **问题描述**: 给你一个区间列表，请你删除列表中被其他区间所覆盖的区间。

### 48. 区间列表的交集 (Interval List Intersections)
- **文件**: Code33_IntervalListIntersections.java, Code33_IntervalListIntersections.py, Code33_IntervalListIntersections.cpp
- **题目来源**: LeetCode 986
- **题目链接**: https://leetcode.cn/problems/interval-list-intersections/
- **问题描述**: 给定两个由一些闭区间组成的列表，每个区间列表都是成对不相交的，并且已经排序。

### 49. 安排工作以达到最大收益 (Maximum Profit in Job Scheduling)
- **文件**: Code34_MaximumProfitJobScheduling.java, Code34_MaximumProfitJobScheduling.py, Code34_MaximumProfitJobScheduling.cpp
- **题目来源**: LeetCode 1235
- **题目链接**: https://leetcode.cn/problems/maximum-profit-in-job-scheduling/
- **问题描述**: 你打算利用空闲时间来做兼职工作赚些零花钱。这里有n份兼职工作，每份工作预计从startTime[i]开始到endTime[i]结束，报酬为profit[i]。

### 50. 最小化舍入误差 (Minimize Rounding Error)
- **文件**: Code35_MinimizeRoundingError.java, Code35_MinimizeRoundingError.py, Code35_MinimizeRoundingError.cpp
- **题目来源**: LeetCode 1058
- **题目链接**: https://leetcode.cn/problems/minimize-rounding-error/
- **问题描述**: 给定一个数组prices，其中prices[i]表示第i件商品的价格。商店想要将价格调整为整数，但调整后的价格总和必须等于原始价格总和。

### 51. 分割数组为连续子序列 (Split Array into Consecutive Subsequences)
- **文件**: Code36_SplitArrayIntoConsecutiveSubsequences.java, Code36_SplitArrayIntoConsecutiveSubsequences.py, Code36_SplitArrayIntoConsecutiveSubsequences.cpp
- **题目来源**: LeetCode 659
- **题目链接**: https://leetcode.cn/problems/split-array-into-consecutive-subsequences/
- **问题描述**: 给你一个按升序排序的整数数组num（可能包含重复数字），请你将它们分割成一个或多个子序列，其中每个子序列都由连续整数组成且长度至少为3。

### 52. 安排电影院座位 (Cinema Seat Allocation)
- **文件**: Code37_CinemaSeatAllocation.java, Code37_CinemaSeatAllocation.py, Code37_CinemaSeatAllocation.cpp
- **题目来源**: LeetCode 1386
- **题目链接**: https://leetcode.cn/problems/cinema-seat-allocation/
- **问题描述**: 电影院的观影厅一共有n行座位，编号从1到n，每一行有10个座位，编号从1到10。给你一个数组reservedSeats，包含已经被预约的座位。

### 53. 使数组唯一的最小增量 (Minimum Increment to Make Array Unique)
- **文件**: Code38_MinimumIncrementToMakeArrayUnique.java, Code38_MinimumIncrementToMakeArrayUnique.py, Code38_MinimumIncrementToMakeArrayUnique.cpp
- **题目来源**: LeetCode 945
- **题目链接**: https://leetcode.cn/problems/minimum-increment-to-make-array-unique/
- **问题描述**: 给定整数数组A，每次move操作将会选择任意A[i]，并将其递增1。返回使A中的每个值都是唯一的最少操作次数。

### 54. 两地调度 (Two City Scheduling)
- **文件**: Code39_TwoCityScheduling.java, Code39_TwoCityScheduling.py, Code39_TwoCityScheduling.cpp
- **题目来源**: LeetCode 1029
- **题目链接**: https://leetcode.cn/problems/two-city-scheduling/
- **问题描述**: 公司计划面试2N人。第i人飞往A市的费用为costs[i][0]，飞往B市的费用为costs[i][1]。返回将每个人都飞到某座城市的最低费用。

### 55. 不邻接植花 (Flower Planting With No Adjacent)
- **文件**: Code40_FlowerPlantingWithNoAdjacent.java, Code40_FlowerPlantingWithNoAdjacent.py, Code40_FlowerPlantingWithNoAdjacent.cpp
- **题目来源**: LeetCode 1042
- **题目链接**: https://leetcode.cn/problems/flower-planting-with-no-adjacent/
- **问题描述**: 有N个花园，按从1到N标记。在每个花园中，你打算种下四种花之一。paths[i] = [x, y]描述了花园x到花园y的双向路径。

### 56. 坏了的计算器 (Broken Calculator)
- **文件**: Code41_BrokenCalculator.java, Code41_BrokenCalculator.py, Code41_BrokenCalculator.cpp
- **题目来源**: LeetCode 991
- **题目链接**: https://leetcode.cn/problems/broken-calculator/
- **问题描述**: 在显示着数字startValue的坏计算器上，我们可以执行以下两种操作：双倍（Double）：将显示屏上的数字乘2；递减（Decrement）：将显示屏上的数字减1。

### 57. 删列造序 (Delete Columns to Make Sorted)
- **文件**: Code42_DeleteColumnsToMakeSorted.java, Code42_DeleteColumnsToMakeSorted.py, Code42_DeleteColumnsToMakeSorted.cpp
- **题目来源**: LeetCode 944
- **题目链接**: https://leetcode.cn/problems/delete-columns-to-make-sorted/
- **问题描述**: 给定由N个小写字母字符串组成的数组A，每个字符串长度相同。你需要选出一组要删除的列，删除A中对应列中的所有字符。

### 58. 单调递增的数字 (Monotone Increasing Digits)
- **文件**: Code43_MonotoneIncreasingDigits.java, Code43_MonotoneIncreasingDigits.py, Code43_MonotoneIncreasingDigits.cpp
- **题目来源**: LeetCode 738
- **题目链接**: https://leetcode.cn/problems/monotone-increasing-digits/
- **问题描述**: 给定一个非负整数N，找出小于或等于N的最大的整数，同时这个整数需要满足其各个位数上的数字是单调递增。

### 59. 划分字母区间 (Partition Labels)
- **文件**: Code44_PartitionLabels.java, Code44_PartitionLabels.py, Code44_PartitionLabels.cpp
- **题目来源**: LeetCode 763
- **题目链接**: https://leetcode.cn/problems/partition-labels/
- **问题描述**: 字符串S由小写字母组成。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。

### 60. 森林中的兔子 (Rabbits in Forest)
- **文件**: Code45_RabbitsInForest.java, Code45_RabbitsInForest.py, Code45_RabbitsInForest.cpp
- **题目来源**: LeetCode 781
- **题目链接**: https://leetcode.cn/problems/rabbits-in-forest/
- **问题描述**: 森林中，每个兔子都有颜色。其中一些兔子（可能是全部）告诉你还有多少其他的兔子和自己有相同的颜色。

## 各大算法平台补充题目

### 牛客网 (Nowcoder) 题目
1. **NC48 跳跃游戏** - 与LeetCode 55相同
2. **NC140 排序** - 各种排序算法实现
3. **NC135 买票需要多少时间** - 队列模拟相关
4. **NC141 判断回文串** - 字符串回文判断

### LintCode (炼码) 题目
1. **LintCode 116. 跳跃游戏** - 与LeetCode 55相同
2. **LintCode 117. 跳跃游戏 II** - 与LeetCode 45相同
3. **LintCode 391. 数飞机** - 区间调度相关
4. **LintCode 636. 二进制手表** - 位运算相关

### HackerRank 题目
1. **Jumping on the Clouds** - 简化版跳跃游戏
2. **Jim and the Orders** - 贪心调度问题
3. **String Similarity** - 字符串相似度计算

### CodeChef 题目
1. **JUMP** - 类似跳跃游戏的变种
2. **TACHSTCK** - 区间配对问题
3. **STRPALIN** - 回文字符串相关

### AtCoder 题目
1. **ABC161D - Lunlun Number** - BFS搜索相关
2. **ABC104C - All Green** - 动态规划相关
3. **ABC126C - Dice and Coin** - 概率相关

### Codeforces 题目
1. **1324B - Yet Another Palindrome Problem** - 子序列相关
2. **1324C - Frog Jumps** - 贪心跳跃问题
3. **1363C - Game On Leaves** - 博弈论相关

### SPOJ 题目
1. **AIBOHP - Aibohphobia** - 回文相关动态规划
2. **ANARC08E - Relax! I am a legend** - 数学相关
3. **ANARC09A - Seinfeld** - 栈相关

### POJ 题目
1. **POJ 1513 - Scheduling Lectures** - 区间调度相关
2. **POJ 1700 - Crossing River** - 经典过河问题
3. **POJ 3096 - Surprising Strings** - 字符串模式识别
4. **POJ 3169 - Layout** - 差分约束系统

### HDU 题目
1. **HDU 2037 - 今年暑假不AC** - 经典区间调度贪心问题
2. **HDU 2586 - How far away?** - LCA最近公共祖先
3. **HDU 1028 - Ignatius and the Princess III** - 整数划分

### USACO 题目
1. **USACO 2014 January Gold - Ski Course Rating** - 图论相关
2. **USACO 2014 January Silver - Cross Country Skiing** - BFS搜索
3. **USACO 2014 January Bronze - Learning by Example** - 字符串处理

### 洛谷 (Luogu) 题目
1. **P1091 - 合唱队形** - 动态规划最长子序列
2. **P1208 - 混合牛奶** - 经典贪心问题
3. **P1579 - 哥德巴赫猜想** - 数论相关
4. **P1809 - 过河问题** - 与本题相同

### Project Euler 题目
1. **Project Euler 357 - Prime generating integers** - 数论相关

### 其他平台题目
1. **MarsCode** - 各种算法竞赛题目
2. **UVa OJ** - 经典算法题目
3. **TimusOJ** - 俄罗斯在线评测系统
4. **AizuOJ** - 日本会津大学在线评测
5. **Comet OJ** - 编程竞赛平台
6. **杭电 OJ** - 杭州电子科技大学在线评测
7. **LOJ** - LibreOJ在线评测系统
8. **剑指Offer** - 面试经典题目

## 贪心算法深度分析

### 贪心算法核心思想详解
贪心算法是一种在每一步选择中都采取在当前状态下最好或最优（即最有利）的选择，从而希望导致结果是最好或最优的算法。贪心算法与动态规划的主要区别在于它对每个子问题的解决方案都做出选择，不能回退。

### 贪心算法的适用条件
1. **最优子结构**：问题的最优解包含子问题的最优解
2. **贪心选择性质**：所求问题的整体最优解可以通过一系列局部最优的选择得到
3. **无后效性**：某个状态以前的过程不会影响以后的状态，只与当前状态有关

### 贪心算法的证明方法
1. **数学归纳法**：证明贪心选择在每一步都是最优的
2. **交换论证法**：证明任何最优解都可以通过贪心选择得到
3. **反证法**：假设存在更优解，推导出矛盾

### 贪心算法的常见类型
1. **区间调度类**：选择结束时间最早的活动
2. **哈夫曼编码**：构建最优前缀编码
3. **最小生成树**：Prim算法和Kruskal算法
4. **最短路径**：Dijkstra算法
5. **背包问题**：分数背包问题

### 贪心算法的局限性
1. 不能保证得到全局最优解
2. 需要严格的数学证明
3. 适用范围有限

## 新题目详细分析

### 11. 加油站问题 (Gas Station)
**算法思路**：使用贪心策略，从起点开始遍历，维护当前油量和总油量。如果当前油量不足，说明从之前的位置无法到达当前位置，需要重新选择起点。

**时间复杂度**：O(n) - 单次遍历
**空间复杂度**：O(1) - 常数空间
**是否最优解**：是，这是该问题的最优解法

**关键技巧**：
- 维护当前剩余油量和总剩余油量
- 当当前油量不足时，重新选择起点
- 如果总油量不足，直接返回-1

### 12. 分发糖果问题 (Candy)
**算法思路**：两次遍历，从左到右和从右到左分别满足左右规则。先保证每个孩子至少有一个糖果，然后根据评分调整糖果数量。

**时间复杂度**：O(n) - 两次遍历
**空间复杂度**：O(n) - 糖果数组
**是否最优解**：是，这是该问题的最优解法

**关键技巧**：
- 从左到右遍历，保证右边评分高的孩子糖果更多
- 从右到左遍历，保证左边评分高的孩子糖果更多
- 取两次遍历的最大值作为最终结果

### 13. 无重叠区间问题 (Non-overlapping Intervals)
**算法思路**：按照区间结束时间排序，选择结束时间最早的区间，这样可以给后面的区间留出更多空间。

**时间复杂度**：O(n log n) - 排序的时间复杂度
**空间复杂度**：O(1) - 常数空间
**是否最优解**：是，这是该问题的最优解法

**关键技巧**：
- 按结束时间排序区间
- 维护当前选择的最后一个区间
- 如果新区间与当前区间不重叠，选择该区间

### 14. 用最少数量的箭引爆气球问题 (Minimum Number of Arrows to Burst Balloons)
**算法思路**：按照气球结束位置排序，每次选择一支箭射在第一个气球的结束位置，这样可以引爆所有重叠的气球。

**时间复杂度**：O(n log n) - 排序的时间复杂度
**空间复杂度**：O(1) - 常数空间
**是否最优解**：是，这是该问题的最优解法

**关键技巧**：
- 按结束位置排序气球
- 维护当前箭的位置
- 如果气球开始位置大于当前箭位置，需要新的箭

### 15. 最大子数组和问题 (Maximum Subarray)
**算法思路**：Kadane算法，维护当前子数组和和最大子数组和。如果当前子数组和小于0，重新开始计算。

**时间复杂度**：O(n) - 单次遍历
**空间复杂度**：O(1) - 常数空间
**是否最优解**：是，这是该问题的最优解法

**关键技巧**：
- 维护当前子数组和
- 维护最大子数组和
- 当前子数组和小于0时重置为0

### 16. 合并区间问题 (Merge Intervals)
**算法思路**：按照区间开始位置排序，然后合并重叠的区间。

**时间复杂度**：O(n log n) - 排序的时间复杂度
**空间复杂度**：O(n) - 结果数组
**是否最优解**：是，这是该问题的最优解法

**关键技巧**：
- 按开始位置排序区间
- 维护当前合并的区间
- 如果新区间与当前区间重叠，合并它们

### 17. 根据身高重建队列问题 (Queue Reconstruction by Height)
**算法思路**：先按身高降序、k值升序排序，然后按照k值插入到相应位置。

**时间复杂度**：O(n²) - 插入排序的时间复杂度
**空间复杂度**：O(n) - 结果列表
**是否最优解**：是，这是该问题的最优解法

**关键技巧**：
- 按身高降序、k值升序排序
- 按照k值插入到相应位置
- 使用链表提高插入效率

### 18. 最低加油次数问题 (Minimum Number of Refueling Stops)
**算法思路**：使用贪心策略，维护当前油量和最大堆。当油量不足时，从堆中选择加油量最大的加油站。

**时间复杂度**：O(n log n) - 堆操作的时间复杂度
**空间复杂度**：O(n) - 堆的空间
**是否最优解**：是，这是该问题的最优解法

**关键技巧**：
- 维护当前油量和位置
- 使用最大堆存储经过的加油站
- 当油量不足时，从堆中选择加油

### 19. 任务调度器问题 (Task Scheduler)
**算法思路**：统计任务频率，按照频率排序。每次选择频率最高的任务执行，保证冷却时间。

**时间复杂度**：O(n) - 统计频率的时间复杂度
**空间复杂度**：O(1) - 固定大小的频率数组
**是否最优解**：是，这是该问题的最优解法

**关键技巧**：
- 统计每个任务的频率
- 按照频率降序排序
- 计算最小时间间隔

### 20. 移掉K位数字问题 (Remove K Digits)
**算法思路**：使用单调栈，维护一个递增的栈。当遇到比栈顶小的数字时，弹出栈顶元素。

**时间复杂度**：O(n) - 每个元素最多入栈出栈一次
**空间复杂度**：O(n) - 栈的空间
**是否最优解**：是，这是该问题的最优解法

**关键技巧**：
- 使用单调栈维护递增序列
- 处理前导零
- 处理k大于数字长度的情况

## 工程化考量深度分析

### 异常处理策略
1. **输入验证**：检查数组是否为空、元素是否合法
2. **边界条件**：处理单个元素、两个元素等特殊情况
3. **无效输入**：对于无法完成转换的情况返回适当错误码

### 性能优化策略
1. **避免重复计算**：通过维护状态变量减少重复计算
2. **空间优化**：尽可能使用原地算法或固定大小的额外空间
3. **算法选择**：根据问题特点选择最适合的贪心策略

### 跨语言特性对比
1. **Java**：使用数组和基本数据类型提高性能
2. **Python**：利用内置函数和列表推导式简化代码
3. **C++**：通过指针和内存管理优化性能

### 测试策略
1. **单元测试**：覆盖所有边界条件和特殊情况
2. **性能测试**：测试大规模数据的处理能力
3. **压力测试**：测试极端输入情况下的稳定性

## 复杂度分析详解

### 新题目时间复杂度分析
- **加油站问题**: O(n) - 单次遍历
- **分发糖果问题**: O(n) - 两次遍历
- **无重叠区间问题**: O(n log n) - 排序的时间复杂度
- **用最少数量的箭引爆气球**: O(n log n) - 排序的时间复杂度
- **最大子数组和**: O(n) - 单次遍历
- **合并区间**: O(n log n) - 排序的时间复杂度
- **根据身高重建队列**: O(n²) - 插入排序的时间复杂度
- **最低加油次数**: O(n log n) - 堆操作的时间复杂度
- **任务调度器**: O(n) - 统计频率的时间复杂度
- **移掉K位数字**: O(n) - 单调栈操作

### 新题目空间复杂度分析
- **加油站问题**: O(1) - 常数空间
- **分发糖果问题**: O(n) - 糖果数组
- **无重叠区间问题**: O(1) - 常数空间
- **用最少数量的箭引爆气球**: O(1) - 常数空间
- **最大子数组和**: O(1) - 常数空间
- **合并区间**: O(n) - 结果数组
- **根据身高重建队列**: O(n) - 结果列表
- **最低加油次数**: O(n) - 堆的空间
- **任务调度器**: O(1) - 固定大小的频率数组
- **移掉K位数字**: O(n) - 栈的空间

## 算法与机器学习联系

### 贪心算法在机器学习中的应用
1. **决策树构建**：ID3、C4.5算法使用贪心策略选择最优划分
2. **聚类算法**：K-means算法使用贪心策略更新聚类中心
3. **特征选择**：前向选择、后向消除使用贪心策略
4. **神经网络训练**：梯度下降可以看作贪心优化

### 贪心算法与深度学习的联系
1. **优化算法**：Adam、RMSProp等优化器包含贪心思想
2. **模型压缩**：剪枝算法使用贪心策略移除不重要的权重
3. **架构搜索**：神经架构搜索中的贪心策略

### 贪心算法与强化学习的联系
1. **策略选择**：ε-贪心策略平衡探索和利用
2. **价值迭代**：动态规划中的贪心策略
3. **蒙特卡洛方法**：基于贪心策略的采样

## 反直觉设计分析

### 贪心算法的反直觉特性
1. **局部最优不等于全局最优**：需要严格证明
2. **贪心选择顺序的影响**：不同顺序可能导致不同结果
3. **问题转化的技巧**：将复杂问题转化为贪心可解问题

### 关键设计决策
1. **选择标准**：如何定义"最优"选择
2. **处理顺序**：按什么顺序处理元素
3. **状态维护**：需要维护哪些状态信息

## 极端场景鲁棒性

### 边界条件测试
1. **空输入**：空数组、空字符串
2. **单元素**：只有一个元素的情况
3. **极值**：最大值、最小值、零值
4. **重复数据**：大量重复元素
5. **有序/逆序**：已经排序的数据

### 异常情况处理
1. **内存溢出**：处理大规模数据
2. **计算溢出**：处理大数运算
3. **无效输入**：处理不符合约束的输入
4. **并发访问**：多线程环境下的安全性

## 性能优化深度分析

### 时间优化策略
1. **避免冗余循环**：减少不必要的迭代
2. **减少重复计算**：缓存中间结果
3. **提前终止**：一旦满足条件立即返回
4. **算法选择**：选择时间复杂度更低的算法

### 空间优化策略
1. **原地算法**：不创建额外数据结构
2. **数据压缩**：使用更紧凑的数据表示
3. **内存复用**：重复使用已分配的内存
4. **延迟加载**：按需分配内存

## 调试与问题定位

### 调试技巧
1. **打印中间过程**：跟踪关键变量的变化
2. **使用断言**：验证中间结果的正确性
3. **边界测试**：测试极端情况
4. **对比验证**：与已知正确解对比

### 问题定位方法
1. **二分查找法**：逐步缩小问题范围
2. **增量调试法**：逐步添加功能并测试
3. **日志分析法**：分析运行日志定位问题
4. **性能剖析**：使用性能分析工具定位瓶颈

## 代码编译验证

### Java代码编译验证
所有Java代码都经过编译验证，确保语法正确性和逻辑完整性。每个文件都包含详细的注释说明和测试用例。

### C++代码编译验证
所有C++代码都经过编译验证，确保头文件包含正确、语法规范。修复了iostream头文件问题，确保跨平台兼容性。

### Python代码运行验证
所有Python代码都经过运行验证，确保语法正确、逻辑完整。包含完整的测试用例和异常处理。

## 最优解验证

### 算法正确性验证
每个题目都经过严格的数学证明和测试验证，确保贪心策略的正确性。通过对比已知最优解和边界条件测试，验证算法的最优性。

### 性能对比分析
与暴力解法、动态规划解法进行性能对比，验证贪心算法在时间和空间复杂度上的优势。

## 总结

本贪心算法专题包含了20个经典题目，涵盖了跳跃游戏、区间调度、字符串处理、资源分配等多种应用场景。每个题目都提供了Java、C++、Python三种语言的实现，包含详细的注释说明、复杂度分析和工程化考量。

通过本专题的学习，可以深入理解贪心算法的核心思想、适用条件和局限性，掌握贪心策略的设计方法和证明技巧，提升算法设计和工程实现能力。

### 学习建议
1. **理解原理**：深入理解每个题目的贪心策略和证明方法
2. **多语言实现**：掌握不同语言下的算法实现技巧
3. **实践练习**：通过大量练习培养贪心算法的直觉
4. **总结归纳**：总结贪心算法的常见模式和适用场景

### 进阶方向
1. **动态规划**：学习贪心算法与动态规划的结合使用
2. **近似算法**：了解贪心算法在近似解中的应用
3. **组合优化**：探索贪心算法在组合优化问题中的应用
4. **机器学习**：研究贪心算法在机器学习模型中的应用

## 面试技巧深度分析

### 解题思路框架
1. **问题分析**：理解题目要求，识别是否适合使用贪心算法
2. **策略选择**：确定贪心策略，证明其正确性
3. **实现细节**：注意边界条件和特殊情况处理
4. **复杂度分析**：准确计算时间和空间复杂度
5. **测试验证**：通过多个测试用例验证算法正确性

### 常见误区避免
1. 贪心选择不正确导致结果错误
2. 忽略边界条件和特殊情况
3. 复杂度分析不准确
4. 代码实现中出现逻辑错误

### 优化建议
1. 多练习不同类型的贪心算法题目
2. 理解贪心算法的适用条件和局限性
3. 掌握常见的贪心策略和优化技巧
4. 注重代码的可读性和可维护性

## 复杂度分析详解

### 时间复杂度分析
- **跳跃游戏 II**: O(n) - 线性扫描
- **灌溉花园的最少水龙头数目**: O(n) - 预处理和扫描
- **字符串转化**: O(n) - 字符映射检查
- **过河问题**: O(n log n) - 排序的时间复杂度
- **超级洗衣机**: O(n) - 单次遍历

### 空间复杂度分析
- **跳跃游戏 II**: O(1) - 常数空间
- **灌溉花园的最少水龙头数目**: O(n) - 额外数组
- **字符串转化**: O(1) - 固定大小数组
- **过河问题**: O(n) - 动态规划数组
- **超级洗衣机**: O(1) - 常数空间

## 算法与机器学习联系

### 贪心算法在机器学习中的应用
1. **决策树构建**：ID3、C4.5算法使用贪心策略选择最优划分
2. **聚类算法**：K-means算法使用贪心策略更新聚类中心
3. **特征选择**：前向选择、后向消除使用贪心策略
4. **神经网络训练**：梯度下降可以看作贪心优化

### 贪心算法与深度学习的联系
1. **优化算法**：Adam、RMSProp等优化器包含贪心思想
2. **模型压缩**：剪枝算法使用贪心策略移除不重要的权重
3. **架构搜索**：神经架构搜索中的贪心策略

### 贪心算法与强化学习的联系
1. **策略选择**：ε-贪心策略平衡探索和利用
2. **价值迭代**：动态规划中的贪心策略
3. **蒙特卡洛方法**：基于贪心策略的采样

## 反直觉设计分析

### 贪心算法的反直觉特性
1. **局部最优不等于全局最优**：需要严格证明
2. **贪心选择顺序的影响**：不同顺序可能导致不同结果
3. **问题转化的技巧**：将复杂问题转化为贪心可解问题

### 关键设计决策
1. **选择标准**：如何定义"最优"选择
2. **处理顺序**：按什么顺序处理元素
3. **状态维护**：需要维护哪些状态信息

## 极端场景鲁棒性

### 边界条件测试
1. **空输入**：空数组、空字符串
2. **单元素**：只有一个元素的情况
3. **极值**：最大值、最小值、零值
4. **重复数据**：大量重复元素
5. **有序/逆序**：已经排序的数据

### 异常情况处理
1. **内存溢出**：处理大规模数据
2. **计算溢出**：处理大数运算
3. **无效输入**：处理不符合约束的输入
4. **并发访问**：多线程环境下的安全性

## 性能优化深度分析

### 时间优化策略
1. **避免冗余循环**：减少不必要的迭代
2. **减少重复计算**：缓存中间结果
3. **提前终止**：一旦满足条件立即返回
4. **算法选择**：选择时间复杂度更低的算法

### 空间优化策略
1. **原地算法**：不创建额外数据结构
2. **数据压缩**：使用更紧凑的数据表示
3. **内存复用**：重复使用已分配的内存
4. **延迟加载**：按需分配内存

## 调试与问题定位

### 调试技巧
1. **打印中间过程**：跟踪关键变量的变化
2. **使用断言**：验证中间结果的正确性
3. **边界测试**：测试极端情况
4. **对比验证**：与已知正确解对比

### 问题定位方法
1. **二分查找法**：逐步缩小问题范围
2. **增量调试法**：逐步添加功能并测试
3. **日志分析法**：分析运行日志定位问题
4. **性能剖析**：使用性能分析工具定位瓶颈

## 总结

贪心算法是算法设计中的重要思想，虽然适用范围有限，但在适合的问题上能够提供简单高效的解决方案。掌握贪心算法需要深入理解其原理、适用条件和证明方法，同时需要大量的练习来培养直觉和经验。

在实际工程应用中，贪心算法常常与其他算法结合使用，或者在特定约束条件下作为近似解法。理解贪心算法的局限性和优势，能够帮助我们在实际问题中做出更好的算法选择。

## 算法思路总结

### 贪心算法核心思想
贪心算法是一种在每一步选择中都采取在当前状态下最好或最优（即最有利）的选择，从而希望导致结果是最好或最优的算法。

### 适用场景
1. **最优子结构**: 问题的最优解包含子问题的最优解
2. **贪心选择性质**: 所求问题的整体最优解可以通过一系列局部最优的选择得到
3. **无后效性**: 某个状态以前的过程不会影响以后的状态，只与当前状态有关

### 常见题型
1. **跳跃游戏类**: 通过维护能到达的最远位置来优化跳跃次数
2. **区间覆盖类**: 通过预处理区间信息，使用贪心策略选择最少区间覆盖目标
3. **字符串映射类**: 通过分析字符间的映射关系判断转换可能性
4. **资源调度类**: 通过排序和动态规划结合贪心策略优化资源分配
5. **平衡分配类**: 通过分析流量瓶颈确定最少操作步数
6. **分配问题类**: 通过排序和匹配策略最大化满足条件的数量
7. **序列变换类**: 通过特定规则重新排列序列以达到最优结果

## 复杂度分析

### 时间复杂度
- 跳跃游戏 II: O(n)
- 灌溉花园的最少水龙头数目: O(n)
- 字符串转化: O(n)
- 过河问题: O(n log n) (主要是排序的时间复杂度)
- 超级洗衣机: O(n)

### 空间复杂度
- 跳跃游戏 II: O(1)
- 灌溉花园的最少水龙头数目: O(n)
- 字符串转化: O(1)
- 过河问题: O(n)
- 超级洗衣机: O(1)

## 工程化考量

### 异常处理
1. 输入验证：检查数组是否为空、元素是否合法
2. 边界条件：处理单个元素、两个元素等特殊情况
3. 无效输入：对于无法完成转换的情况返回适当错误码

### 性能优化
1. 避免重复计算：通过维护状态变量减少重复计算
2. 空间优化：尽可能使用原地算法或固定大小的额外空间
3. 算法选择：根据问题特点选择最适合的贪心策略

### 跨语言特性
1. Java: 使用数组和基本数据类型提高性能
2. Python: 利用内置函数和列表推导式简化代码
3. C++: 通过指针和内存管理优化性能

## 面试技巧

### 解题思路
1. **问题分析**: 理解题目要求，识别是否适合使用贪心算法
2. **策略选择**: 确定贪心策略，证明其正确性
3. **实现细节**: 注意边界条件和特殊情况处理
4. **复杂度分析**: 准确计算时间和空间复杂度
5. **测试验证**: 通过多个测试用例验证算法正确性

### 常见误区
1. 贪心选择不正确导致结果错误
2. 忽略边界条件和特殊情况
3. 复杂度分析不准确
4. 代码实现中出现逻辑错误

### 优化建议
1. 多练习不同类型的贪心算法题目
2. 理解贪心算法的适用条件和局限性
3. 掌握常见的贪心策略和优化技巧
4. 注重代码的可读性和可维护性