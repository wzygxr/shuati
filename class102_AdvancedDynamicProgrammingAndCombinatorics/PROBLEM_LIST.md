# Class128 问题清单与扩展题目

## 📚 核心题目

### 1. 苹果和盘子（球盒模型）
- **文件**: Code01_ApplesPlates.java/.cpp/.py
- **题目链接**: https://www.nowcoder.com/practice/bfd8234bb5e84be0b493656e390bdebf
- **类型**: 组合数学、动态规划
- **难度**: 中等
- **核心思路**: 组合数学中的球盒模型，使用动态规划解决
- **时间复杂度**: O(m*n)
- **空间复杂度**: O(m*n)
- **是否最优解**: ✅ 是

### 2. 数的划分方法
- **文件**: Code01_SplitNumber.java/.cpp/.py
- **题目链接**: https://www.luogu.com.cn/problem/P1025
- **类型**: 组合数学、动态规划
- **难度**: 中等
- **核心思路**: 整数划分问题，使用动态规划解决
- **时间复杂度**: O(m*n)
- **空间复杂度**: O(m*n)
- **是否最优解**: ✅ 是

### 3. 最好的部署
- **文件**: Code02_BestDeploy.java/.cpp/.py
- **类型**: 动态规划（区间DP/线性DP）
- **难度**: 困难
- **核心思路**: 线性DP优化，状态设计优化
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **是否最优解**: ✅ 是

### 4. 增加限制的最长公共子序列
- **文件**: Code03_AddLimitLcs.java/.cpp/.py
- **类型**: 动态规划、状态设计优化
- **难度**: 困难
- **核心思路**: 利用输入数据特点优化状态设计
- **时间复杂度**: O(26*n + m²)
- **空间复杂度**: O(n*26 + m²)
- **是否最优解**: ✅ 是

### 5. 大楼扔鸡蛋问题
- **文件**: Code04_EggDrop.java/.cpp/.py
- **题目链接**: https://leetcode.cn/problems/super-egg-drop/
- **类型**: 动态规划优化
- **难度**: 困难
- **核心思路**: DP状态设计优化，自底向上计算
- **时间复杂度**: O(k*n)
- **空间复杂度**: O(k)
- **是否最优解**: ✅ 是

### 6. 相邻必选的子序列最大中位数
- **文件**: Code05_MaximizeMedian1.java/.cpp/.py, Code05_MaximizeMedian2.java/.cpp/.py
- **题目链接**: https://atcoder.jp/contests/abc236/tasks/abc236_e
- **类型**: 二分答案、动态规划
- **难度**: 困难
- **核心思路**: 二分答案 + DP判定
- **时间复杂度**: O(n * log(n) * log(max))
- **空间复杂度**: O(n)
- **是否最优解**: ✅ 是

### 7. 将珠子放进背包中
- **文件**: Code06_MarblesInBags.java/.cpp/.py
- **题目链接**: https://leetcode.cn/problems/put-marbles-in-bags/
- **类型**: 贪心算法
- **难度**: 中等
- **核心思路**: 贪心策略，排序后取极值
- **时间复杂度**: O(n*log(n))
- **空间复杂度**: O(n)
- **是否最优解**: ✅ 是

### 8. 爬楼梯问题
- **文件**: Code07_ClimbingStairs.java/.cpp/.py
- **题目链接**: https://leetcode.cn/problems/climbing-stairs/
- **类型**: 动态规划、空间优化
- **难度**: 简单
- **核心思路**: 经典动态规划问题，使用滚动数组优化空间复杂度
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

### 9. 分割数组的最大值
- **文件**: Code08_SplitArray.java/.cpp/.py
- **题目链接**: https://leetcode.cn/problems/split-array-largest-sum/
- **类型**: 二分答案、贪心算法
- **难度**: 困难
- **核心思路**: 二分答案 + 贪心判定
- **时间复杂度**: O(n * log(sum))
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

### 10. 制作 m 束花所需的最少天数
- **文件**: Code10_MinDaysToBloom.java/.cpp/.py
- **题目链接**: https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/
- **类型**: 二分答案、贪心算法
- **难度**: 中等
- **核心思路**: 二分答案 + 贪心判定
- **时间复杂度**: O(n * log(max-min))
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

## 🌐 扩展题目清单

### LeetCode (力扣)
1. **LeetCode 887. 鸡蛋掉落** - https://leetcode.cn/problems/super-egg-drop/
   - 类型：动态规划优化
   - 难度：困难
   - 相关题目：Code04_EggDrop

2. **LeetCode 1143. 最长公共子序列** - https://leetcode.cn/problems/longest-common-subsequence/
   - 类型：动态规划
   - 难度：中等
   - 相关题目：Code03_AddLimitLcs

3. **LeetCode 516. 最长回文子序列** - https://leetcode.cn/problems/longest-palindromic-subsequence/
   - 类型：区间DP
   - 难度：中等

4. **LeetCode 312. 戳气球** - https://leetcode.cn/problems/burst-balloons/
   - 类型：区间DP
   - 难度：困难

5. **LeetCode 1547. 切棍子的最小成本** - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
   - 类型：区间DP
   - 难度：困难

6. **LeetCode 1751. 最多可以参加的会议数目 II** - https://leetcode.cn/problems/maximum-number-of-events-that-can-be-attended-ii/
   - 类型：动态规划优化
   - 难度：困难

7. **LeetCode 1335. 工作计划的最低难度** - https://leetcode.cn/problems/minimum-difficulty-of-a-job-schedule/
   - 类型：动态规划
   - 难度：困难

8. **LeetCode 410. 分割数组的最大值** - https://leetcode.cn/problems/split-array-largest-sum/
   - 类型：二分答案+DP
   - 难度：困难
   - 相关题目：Code08_SplitArray

9. **LeetCode 1482. 制作 m 束花所需的最少天数** - https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/
   - 类型：二分答案
   - 难度：中等
   - 相关题目：Code10_MinDaysToBloom

10. **LeetCode 1011. 在 D 天内送达包裹的能力** - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
    - 类型：二分答案
    - 难度：中等

11. **LeetCode 70. 爬楼梯** - https://leetcode.cn/problems/climbing-stairs/
    - 类型：动态规划、空间优化
    - 难度：简单
    - 相关题目：Code07_ClimbingStairs

12. **LeetCode 2551. 将珠子放进背包中** - https://leetcode.cn/problems/put-marbles-in-bags/
    - 类型：贪心算法
    - 难度：中等
    - 相关题目：Code09_PutMarblesInBags

13. **LeetCode 343. 整数拆分** - https://leetcode.cn/problems/integer-break/
    - 类型：动态规划、数学
    - 难度：中等
    - 相关题目：Code01_SplitNumber

14. **LeetCode 455. 分发饼干** - https://leetcode.cn/problems/assign-cookies/
    - 类型：贪心算法
    - 难度：简单

15. **LeetCode 435. 无重叠区间** - https://leetcode.cn/problems/non-overlapping-intervals/
    - 类型：贪心算法
    - 难度：中等

16. **LeetCode 452. 用最少数量的箭引爆气球** - https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
    - 类型：贪心算法
    - 难度：中等

17. **LeetCode 198. 打家劫舍** - https://leetcode.cn/problems/house-robber/
    - 类型：动态规划、空间优化
    - 难度：中等
    - 相关题目：Code07_ClimbingStairs

18. **LeetCode 121. 买卖股票的最佳时机** - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
    - 类型：动态规划
    - 难度：简单

19. **LeetCode 122. 买卖股票的最佳时机 II** - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
    - 类型：贪心算法
    - 难度：中等

20. **LeetCode 123. 买卖股票的最佳时机 III** - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
    - 类型：动态规划
    - 难度：困难

21. **LeetCode 188. 买卖股票的最佳时机 IV** - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
    - 类型：动态规划
    - 难度：困难

22. **LeetCode 309. 最佳买卖股票时机含冷冻期** - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/
    - 类型：动态规划
    - 难度：中等

23. **LeetCode 714. 买卖股票的最佳时机含手续费** - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
    - 类型：动态规划、贪心算法
    - 难度：中等

24. **LeetCode 300. 最长递增子序列** - https://leetcode.cn/problems/longest-increasing-subsequence/
    - 类型：动态规划、二分查找
    - 难度：中等

25. **LeetCode 354. 俄罗斯套娃信封问题** - https://leetcode.cn/problems/russian-doll-envelopes/
    - 类型：动态规划、排序
    - 难度：困难

26. **LeetCode 322. 零钱兑换** - https://leetcode.cn/problems/coin-change/
    - 类型：动态规划
    - 难度：中等

27. **LeetCode 518. 零钱兑换 II** - https://leetcode.cn/problems/coin-change-ii/
    - 类型：动态规划
    - 难度：中等

28. **LeetCode 91. 解码方法** - https://leetcode.cn/problems/decode-ways/
    - 类型：动态规划
    - 难度：中等

29. **LeetCode 62. 不同路径** - https://leetcode.cn/problems/unique-paths/
    - 类型：动态规划、数学
    - 难度：中等

30. **LeetCode 63. 不同路径 II** - https://leetcode.cn/problems/unique-paths-ii/
    - 类型：动态规划
    - 难度：中等

31. **LeetCode 64. 最小路径和** - https://leetcode.cn/problems/minimum-path-sum/
    - 类型：动态规划
    - 难度：中等

32. **LeetCode 72. 编辑距离** - https://leetcode.cn/problems/edit-distance/
    - 类型：动态规划
    - 难度：困难

33. **LeetCode 97. 交错字符串** - https://leetcode.cn/problems/interleaving-string/
    - 类型：动态规划
    - 难度：中等

34. **LeetCode 139. 单词拆分** - https://leetcode.cn/problems/word-break/
    - 类型：动态规划
    - 难度：中等

35. **LeetCode 140. 单词拆分 II** - https://leetcode.cn/problems/word-break-ii/
    - 类型：动态规划、回溯
    - 难度：困难

36. **LeetCode 174. 地下城游戏** - https://leetcode.cn/problems/dungeon-game/
    - 类型：动态规划
    - 难度：困难

37. **LeetCode 221. 最大正方形** - https://leetcode.cn/problems/maximal-square/
    - 类型：动态规划
    - 难度：中等

38. **LeetCode 368. 最大整除子集** - https://leetcode.cn/problems/largest-divisible-subset/
    - 类型：动态规划
    - 难度：中等

39. **LeetCode 416. 分割等和子集** - https://leetcode.cn/problems/partition-equal-subset-sum/
    - 类型：动态规划
    - 难度：中等

40. **LeetCode 474. 一和零** - https://leetcode.cn/problems/ones-and-zeroes/
    - 类型：动态规划
    - 难度：中等

41. **LeetCode 494. 目标和** - https://leetcode.cn/problems/target-sum/
    - 类型：动态规划
    - 难度：中等

42. **LeetCode 576. 出界的路径数** - https://leetcode.cn/problems/out-of-boundary-paths/
    - 类型：动态规划
    - 难度：中等

43. **LeetCode 688. 骑士在棋盘上的概率** - https://leetcode.cn/problems/knight-probability-in-chessboard/
    - 类型：动态规划
    - 难度：中等

44. **LeetCode 877. 石子游戏** - https://leetcode.cn/problems/stone-game/
    - 类型：动态规划
    - 难度：中等

45. **LeetCode 1155. 掷骰子等于目标和的方法数** - https://leetcode.cn/problems/number-of-dice-rolls-with-target-sum/
    - 类型：动态规划
    - 难度：中等

46. **LeetCode 1444. 切披萨的方案数** - https://leetcode.cn/problems/number-of-ways-of-cutting-a-pizza/
    - 类型：动态规划
    - 难度：困难

47. **LeetCode 1463. 摘樱桃 II** - https://leetcode.cn/problems/cherry-pickup-ii/
    - 类型：动态规划
    - 难度：困难

48. **LeetCode 1563. 石子游戏 V** - https://leetcode.cn/problems/stone-game-v/
    - 类型：动态规划
    - 难度：困难

49. **LeetCode 1755. 最接近目标值的子序列和** - https://leetcode.cn/problems/closest-subsequence-sum/
    - 类型：动态规划、折半搜索
    - 难度：困难

50. **LeetCode 1787. 使所有区间的异或结果为零** - https://leetcode.cn/problems/make-the-xor-of-all-segments-equal-to-zero/
    - 类型：动态规划
    - 难度：困难

### 牛客网 (NowCoder)
1. **牛客 NC128. 苹果和盘子** - https://www.nowcoder.com/practice/bfd8234bb5e84be0b493656e390bdebf
   - 类型：组合数学
   - 难度：中等
   - 相关题目：Code01_ApplesPlates

2. **牛客 NC104. 求正数数组的最小不可组成和** - https://www.nowcoder.com/practice/3350d379a5d44054b219de7af6708894
   - 类型：动态规划、贪心算法
   - 难度：中等

3. **牛客 NC14138. 整数分拆** - https://www.nowcoder.com/practice/38b6d26b18bf49bc9fae3a3e2322a471
   - 类型：动态规划
   - 难度：中等
   - 相关题目：Code01_ApplesPlates

4. **牛客 NC16313. 分巧克力** - https://www.nowcoder.com/practice/351192348a6746d98a23a91155529fca
   - 类型：二分答案、贪心算法
   - 难度：中等

5. **牛客 NC16531. 硬币面值组合** - https://www.nowcoder.com/practice/2b7995aa4f7949d99674d975489cb7da
   - 类型：动态规划
   - 难度：中等

6. **牛客 NC16745. 最少砝码** - https://www.nowcoder.com/practice/e3531a87aedf4d2aacb370396f4f0845
   - 类型：数学分析
   - 难度：中等

7. **牛客 NC17583. 分割数组的方案数** - https://www.nowcoder.com/practice/16b21975862345a298a6c7b3f1b2516f
   - 类型：动态规划
   - 难度：中等

8. **牛客 NC19153. 砝码称重** - https://www.nowcoder.com/practice/67984bd528844622b4b85562269dc706
   - 类型：动态规划、位运算
   - 难度：中等

9. **牛客 NC13273. 最长公共子序列** - https://www.nowcoder.com/practice/8cb00d419d9a4c658995905282b2e45f
   - 类型：动态规划
   - 难度：中等
   - 相关题目：Code03_AddLimitLcs

10. **牛客 NC14508. 最长上升子序列** - https://www.nowcoder.com/practice/d83721575bd4418eae76c916483493de
   - 类型：动态规划
   - 难度：中等
   - 相关题目：LeetCode 300

### 洛谷 (Luogu)
1. **洛谷 P1025. 数的划分** - https://www.luogu.com.cn/problem/P1025
   - 类型：组合数学、动态规划
   - 难度：中等
   - 相关题目：Code01_SplitNumber

2. **洛谷 P2858. 奶牛零食** - https://www.luogu.com.cn/problem/P2858
   - 类型：区间DP
   - 难度：中等

3. **洛谷 P1775. 石子合并** - https://www.luogu.com.cn/problem/P1775
   - 类型：区间DP
   - 难度：中等

4. **洛谷 P1287. 盒子与球** - https://www.luogu.com.cn/problem/P1287
   - 类型：组合数学、球盒模型
   - 难度：中等
   - 相关题目：Code01_ApplesPlates

5. **洛谷 P5824. 十二重计数法** - https://www.luogu.com.cn/problem/P5824
   - 类型：组合数学、球盒模型
   - 难度：困难
   - 相关题目：Code01_ApplesPlates

6. **洛谷 P1044. 栈** - https://www.luogu.com.cn/problem/P1044
   - 类型：动态规划
   - 难度：中等

7. **洛谷 P1028. 数的计算** - https://www.luogu.com.cn/problem/P1028
   - 类型：递归、动态规划
   - 难度：简单

8. **洛谷 P2404. 自然数的拆分问题** - https://www.luogu.com.cn/problem/P2404
   - 类型：递归回溯
   - 难度：中等

9. **洛谷 P1049. 装箱问题** - https://www.luogu.com.cn/problem/P1049
   - 类型：动态规划
   - 难度：简单

10. **洛谷 P1064. 金明的预算方案** - https://www.luogu.com.cn/problem/P1064
    - 类型：动态规划
    - 难度：中等

11. **洛谷 P1060. 开心的金明** - https://www.luogu.com.cn/problem/P1060
    - 类型：动态规划
    - 难度：简单

12. **洛谷 P1164. 小A点菜** - https://www.luogu.com.cn/problem/P1164
    - 类型：动态规划
    - 难度：简单

13. **洛谷 P1616. 疯狂的采药** - https://www.luogu.com.cn/problem/P1616
    - 类型：动态规划
    - 难度：简单

14. **洛谷 P1833. 樱花** - https://www.luogu.com.cn/problem/P1833
    - 类型：动态规划
    - 难度：中等

15. **洛谷 P1507. NASA的食物计划** - https://www.luogu.com.cn/problem/P1507
    - 类型：动态规划
    - 难度：中等

16. **洛谷 P1514. 引水入城** - https://www.luogu.com.cn/problem/P1514
    - 类型：动态规划、图论
    - 难度：困难

17. **洛谷 P1352. 没有上司的舞会** - https://www.luogu.com.cn/problem/P1352
    - 类型：树形DP
    - 难度：中等

18. **洛谷 P1122. 最大子树和** - https://www.luogu.com.cn/problem/P1122
    - 类型：树形DP
    - 难度：中等

19. **洛谷 P2014. 选课** - https://www.luogu.com.cn/problem/P2014
    - 类型：树形DP
    - 难度：中等

20. **洛谷 P2015. 二叉苹果树** - https://www.luogu.com.cn/problem/P2015
    - 类型：树形DP
    - 难度：中等

21. **洛谷 P1091. 合唱队形** - https://www.luogu.com.cn/problem/P1091
    - 类型：动态规划
    - 难度：中等

22. **洛谷 P1280. 尼克的任务** - https://www.luogu.com.cn/problem/P1280
    - 类型：动态规划
    - 难度：中等

23. **洛谷 P1282. 多米诺骨牌** - https://www.luogu.com.cn/problem/P1282
    - 类型：动态规划
    - 难度：中等

24. **洛谷 P1387. 最大正方形** - https://www.luogu.com.cn/problem/P1387
    - 类型：动态规划
    - 难度：简单

25. **洛谷 P1579. 哥德巴赫猜想（升级版）** - https://www.luogu.com.cn/problem/P1579
    - 类型：数论、枚举
    - 难度：简单

26. **洛谷 P1880. 石子合并** - https://www.luogu.com.cn/problem/P1880
    - 类型：区间DP
    - 难度：中等

27. **洛谷 P3205. 合唱队** - https://www.luogu.com.cn/problem/P3205
    - 类型：动态规划
    - 难度：中等

28. **洛谷 P1006. 传纸条** - https://www.luogu.com.cn/problem/P1006
    - 类型：动态规划
    - 难度：中等

29. **洛谷 P1140. 相似基因** - https://www.luogu.com.cn/problem/P1140
    - 类型：动态规划
    - 难度：中等

30. **洛谷 P1020. 导弹拦截** - https://www.luogu.com.cn/problem/P1020
    - 类型：动态规划、贪心算法
    - 难度：中等

31. **洛谷 P1091. 合唱队形** - https://www.luogu.com.cn/problem/P1091
    - 类型：动态规划
    - 难度：中等

32. **洛谷 P1233. 木棍加工** - https://www.luogu.com.cn/problem/P1233
    - 类型：动态规划、排序
    - 难度：中等

33. **洛谷 P1029. 最大公约数和最小公倍数问题** - https://www.luogu.com.cn/problem/P1029
    - 类型：数论
    - 难度：简单

34. **洛谷 P1135. 奇怪的电梯** - https://www.luogu.com.cn/problem/P1135
    - 类型：BFS、动态规划
    - 难度：简单

35. **洛谷 P1169. [ZJOI2007]棋盘制作** - https://www.luogu.com.cn/problem/P1169
    - 类型：动态规划、单调栈
    - 难度：中等

36. **洛谷 P1387. 最大正方形** - https://www.luogu.com.cn/problem/P1387
    - 类型：动态规划
    - 难度：简单

37. **洛谷 P1736. 创意吃鱼法** - https://www.luogu.com.cn/problem/P1736
    - 类型：动态规划
    - 难度：中等

38. **洛谷 P2704 [NOI2001] 炮兵阵地** - https://www.luogu.com.cn/problem/P2704
    - 类型：状态压缩DP
    - 难度：困难

39. **洛谷 P1896 [SCOI2005]互不侵犯** - https://www.luogu.com.cn/problem/P1896
    - 类型：状态压缩DP
    - 难度：中等

40. **Project Euler Problem 76** - https://projecteuler.net/problem=76
    - 类型：组合数学、整数划分
    - 难度：中等
    - 相关题目：Code01_SplitNumber

### AtCoder
1. **AtCoder ABC236E. Average and Median** - https://atcoder.jp/contests/abc236/tasks/abc236_e
   - 类型：二分答案、动态规划
   - 难度：中等
   - 相关题目：Code05_MaximizeMedian2

2. **AtCoder ABC231G. Balls in Boxes** - https://atcoder.jp/contests/abc231/tasks/abc231_g
   - 类型：概率论、动态规划
   - 难度：中等

3. **AtCoder ARC189C. Balls and Boxes** - https://atcoder.jp/contests/arc189/tasks/arc189_c
   - 类型：组合数学
   - 难度：中等

4. **AtCoder ABC422G. Balls and Boxes** - https://atcoder.jp/contests/abc422/tasks/abc422_g
   - 类型：组合数学
   - 难度：困难

5. **AtCoder ARC186C. Ball and Box** - https://atcoder.jp/contests/arc186/tasks/arc186_c
   - 类型：模拟
   - 难度：中等

### Codeforces
1. **Codeforces 1327D. Infinite Path** - https://codeforces.com/problemset/problem/1327/D
   - 类型：动态规划
   - 难度：困难

2. **Codeforces 449B. Jzzhu and Cities** - https://codeforces.com/problemset/problem/449/B
   - 类型：图论、最短路径
   - 难度：困难

3. **Codeforces 550B. Preparing Olympiad** - https://codeforces.com/problemset/problem/550/B
   - 类型：位运算、枚举
   - 难度：中等

4. **Codeforces 260C. Balls and Boxes** - https://codeforces.com/problemset/problem/260/C
   - 类型：构造、贪心
   - 难度：中等

5. **Codeforces 1845E. Boxes and Balls** - https://codeforces.com/problemset/problem/1845/E
   - 类型：二分答案、贪心
   - 难度：中等

6. **Codeforces 103821J. Balls in Boxes** - https://codeforces.com/problemset/gymProblem/103821/J
   - 类型：组合数学
   - 难度：中等

7. **Codeforces 460B. Little Dima and Equation** - https://codeforces.com/problemset/problem/460/B
   - 类型：数学分析
   - 难度：中等

8. **Codeforces 1132E. Knapsack** - https://codeforces.com/problemset/problem/1132/E
   - 类型：背包问题、优化
   - 难度：困难

9. **Codeforces 1327D. Infinite Path** - https://codeforces.com/problemset/problem/1327/D
   - 类型：动态规划
   - 难度：困难

10. **Codeforces 449B. Jzzhu and Cities** - https://codeforces.com/problemset/problem/449/B
    - 类型：图论、最短路径
    - 难度：困难

11. **Codeforces 550B. Preparing Olympiad** - https://codeforces.com/problemset/problem/550/B
    - 类型：位运算、枚举
    - 难度：中等

12. **Codeforces 1845E. Boxes and Balls** - https://codeforces.com/problemset/problem/1845/E
    - 类型：二分答案、贪心
    - 难度：中等

### 其他平台
1. **POJ 2456. Aggressive cows** - http://poj.org/problem?id=2456
   - 类型：二分答案
   - 难度：中等

2. **ZOJ 3509. Kind of a Blur** - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827369477
   - 类型：动态规划
   - 难度：困难

3. **HackerRank - Sherlock and Cost** - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
   - 类型：动态规划
   - 难度：中等

4. **SPOJ - ASSIGN - Assignments** - https://www.spoj.com/problems/ASSIGN/
   - 类型：动态规划、状态压缩
   - 难度：困难

5. **Project Euler - Problem 76** - https://projecteuler.net/problem=76
   - 类型：组合数学、动态规划
   - 难度：中等
   - 相关题目：Code01_SplitNumber

6. **洛谷 P2704 [NOI2001] 炮兵阵地** - https://www.luogu.com.cn/problem/P2704
   - 类型：状态压缩DP
   - 难度：困难

7. **洛谷 P1896 [SCOI2005]互不侵犯** - https://www.luogu.com.cn/problem/P1896
   - 类型：状态压缩DP
   - 难度：中等

8. **HDU 1028. Ignatius and the Princess III** - http://acm.hdu.edu.cn/showproblem.php?pid=1028
   - 类型：动态规划、整数划分
   - 难度：中等
   - 相关题目：Code01_ApplesPlates

9. **SPOJ QCJ2. Another Box Problem** - https://www.spoj.com/problems/QCJ2/
   - 类型：组合数学、动态规划
   - 难度：中等

10. **Aizu DPL_5_D. Balls and Boxes 4** - https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=DPL_5_D
    - 类型：组合数学、动态规划
    - 难度：中等

11. **Timus 1437. Gasoline Station** - https://acm.timus.ru/problem.aspx?space=1&num=1437
    - 类型：动态规划
    - 难度：中等

12. **UVa 103. Stacking Boxes** - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=39
    - 类型：动态规划、LIS
    - 难度：中等

13. **HackerEarth - Misha and Boxes** - https://www.hackerearth.com/practice/algorithms/dynamic-programming/bit-masking/practice-problems/algorithm/misha-and-boxes-b7e70bc6/
    - 类型：动态规划、位运算
    - 难度：中等

14. **CodeChef - BALLGAME** - https://www.codechef.com/problems/BALLGAME
    - 类型：动态规划
    - 难度：中等

15. **Project Euler 426. Box-Ball System** - https://projecteuler.net/problem=426
    - 类型：模拟、数学
    - 难度：困难

## 🧠 算法技巧总结

### 1. 动态规划优化技巧
- **状态设计优化**：根据输入数据特点重新设计状态表示
- **转移优化**：利用数据结构或数学性质优化状态转移
- **空间优化**：使用滚动数组等技巧降低空间复杂度

### 2. 二分答案技巧
- **适用条件**：答案具有单调性，可以快速判断某个答案是否可行
- **实现要点**：确定上下界，设计判定函数，正确处理边界

### 3. 组合数学技巧
- **球盒模型**：区分球和盒子是否有区别，是否允许为空
- **整数划分**：将一个整数划分为若干正整数之和的方案数
- **生成函数**：用形式幂级数表示序列的工具

### 4. 贪心算法技巧
- **适用条件**：问题具有贪心选择性质和最优子结构
- **实现要点**：证明贪心策略的正确性，设计合适的贪心规则

## 📈 复杂度分析

| 题目 | 时间复杂度 | 空间复杂度 | 是否最优解 |
|------|------------|------------|------------|
| 苹果和盘子 | O(m*n) | O(m*n) | ✅ |
| 数的划分方法 | O(m*n) | O(m*n) | ✅ |
| 最好的部署 | O(n) | O(n) | ✅ |
| 增加限制的LCS | O(26*n+m²) | O(n*26+m²) | ✅ |
| 大楼扔鸡蛋 | O(k*n) | O(k) | ✅ |
| 最大化中位数 | O(n*log(n)*log(max)) | O(n) | ✅ |
| 将珠子放进背包中 | O(n*log(n)) | O(n) | ✅ |
| 爬楼梯问题 | O(n) | O(1) | ✅ |
| 分割数组的最大值 | O(n * log(sum)) | O(1) | ✅ |
| 制作 m 束花所需的最少天数 | O(n * log(max-min)) | O(1) | ✅ |

## 🎯 学习路径建议

### 初学者
1. 理解每道题目的问题背景和约束条件
2. 掌握基础的动态规划思想
3. 熟悉组合数学的基本概念

### 进阶学习者
1. 学习状态设计优化技巧
2. 掌握二分答案的应用场景
3. 理解贪心算法的适用条件

### 高级学习者
1. 研究复杂DP问题的优化方法
2. 探索组合数学在算法中的高级应用
3. 分析实际问题与经典算法模型的对应关系