# Class069 补充题目清单

## 一、多维费用背包问题

### 1. 目标和 (Target Sum)
- **题目链接**: https://leetcode.cn/problems/target-sum/
- **题目描述**: 给你一个非负整数数组 nums 和一个整数 target。向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式。返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
- **实现文件**: TargetSum.java, TargetSum.cpp, TargetSum.py

### 2. 最后一块石头的重量 II (Last Stone Weight II)
- **题目链接**: https://leetcode.cn/problems/last-stone-weight-ii/
- **题目描述**: 有一堆石头，用整数数组 stones 表示。其中 stones[i] 表示第 i 块石头的重量。每一回合，从中选出任意两块石头，然后将它们一起粉碎。假设石头的重量分别为 x 和 y，且 x <= y。粉碎的可能结果如下：如果 x == y，那么两块石头都会被完全粉碎；如果 x != y，那么重量为 x 的石头完全粉碎，重量为 y 的石头新重量为 y-x。最后，最多只会剩下一块石头。返回此石头最小的可能重量。如果没有石头剩下，就返回 0。
- **实现文件**: LastStoneWeightII.java, LastStoneWeightII.cpp, LastStoneWeightII.py

### 3. 零钱兑换 II (Coin Change 2)
- **题目链接**: https://leetcode.cn/problems/coin-change-2/
- **题目描述**: 给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0。假设每一种面额的硬币有无限个。

### 4. 组合总和 Ⅳ (Combination Sum IV)
- **题目链接**: https://leetcode.cn/problems/combination-sum-iv/
- **题目描述**: 给你一个由不同整数组成的数组 nums，和一个目标整数 target。请你从 nums 中找出并返回总和为 target 的元素组合的个数。题目数据保证答案符合 32 位整数范围。

### 5. 潜水员 (Diver)
- **题目链接**: https://www.luogu.com.cn/problem/P1759
- **题目描述**: 潜水员为了潜水要使用特殊的装备。他有一个带2种气体的气缸：一个为氧气，一个为氮气。让潜水员下潜的深度需要各种的数量的氧和氮。潜水员有一定数量的气缸。每个气缸都有重量和气体容量。潜水员为了完成他的工作需要至少一定数量的氧和氮。他需要在这些条件下找到重量最轻的气缸组合。

### 6. 数位成本和为目标值的最大数字 (Largest Number With Digits That Add Up To Target)
- **题目链接**: https://leetcode.cn/problems/form-largest-integer-with-digits-that-add-up-to-target/
- **题目描述**: 给你一个整数数组 cost 和一个整数 target 。请你返回满足如下条件的字符串：
  - 字符串的长度必须是最小的
  - 字符串中的每一个字符都是从 '0' 到 '9' 的数字
  - 字符串的数值总和必须等于 target
  - 如果有多个答案，返回字典序最大的那个。

### 7. 背包问题VII (Knapsack Problem VII)
- **题目链接**: https://www.lintcode.com/problem/1538/
- **题目描述**: 给定 n 个物品，物品的体积为 A[i]，物品的价值为 V[i]。
  再给定一个整数 k，要求你选择一些物品，使得选中的物品的体积总和不超过背包的容量 m，并且选中的物品的价值总和最大。
  其中，每个物品只能选择一次，但是可以选择多个物品（即：物品可以重复选择），但最多只能选择k次。

### 8. 分组背包问题 (Grouped Knapsack Problem)
- **题目链接**: https://www.luogu.com.cn/problem/P1757
- **题目描述**: 有N组物品和一个容量是V的背包。每组物品中最多选一个物品。每组物品有若干个，同一组内的物品最多只能选一个。每件物品的体积是v_ij，价值是w_ij，其中i是组号，j是组内编号。求将哪些物品装入背包，可使物品的总体积不超过背包容量，且总价值最大。

## 二、概率动态规划问题

### 1. 骑士拨号器 (Knight Dialer)
- **题目链接**: https://leetcode.cn/problems/knight-dialer/
- **题目描述**: 象棋骑士有一个独特的移动方式，它可以垂直移动两个方格，水平移动一个方格，或者水平移动两个方格，垂直移动一个方格(两者都形成一个 L 的形状)。我们有一个象棋骑士和一个电话垫，如下所示，骑士只能站在一个数字单元格上。给定一个整数 n，返回我们可以拨多少个长度为 n 的不同电话号码。
- **实现文件**: Code07_KnightDialer.java, Code07_KnightDialer.cpp, Code07_KnightDialer.py

### 2. Coins (概率DP)
- **题目链接**: https://atcoder.jp/contests/dp/tasks/dp_i
- **题目描述**: 有N枚硬币，第i枚硬币抛出后正面朝上的概率是p[i]。现在将这N枚硬币都抛一次，求正面朝上的硬币数比反面朝上的硬币数多的概率。
- **实现文件**: Code06_Coins.java, Code06_Coins.cpp, Code06_Coins.py

### 3. 地下城游戏 (Dungeon Game)
- **题目链接**: https://leetcode.cn/problems/dungeon-game/
- **题目描述**: 一些恶魔抓住了公主（P）并将她关在了地下城的右下角。地下城是由 M x N 个房间组成的二维网格。我们英勇的骑士（K）最初被安置在左上角的房间里。骑士的初始健康点数为一个正整数。如果他的健康点数在某一时刻降至 0 或以下，他会立即死亡。有些房间由恶魔守卫，因此骑士在进入这些房间时会失去健康点数（若房间里的值为负整数，则表示骑士将损失健康点数）；其他房间要么是空的（房间里的值为 0），要么包含增加骑士健康点数的魔法球（若房间里的值为正整数，则表示骑士将增加健康点数）。为了尽快解救到公主，骑士决定每次只向右或向下移动一步。返回确保骑士能够拯救到公主所需的最低初始健康点数。

### 4. 鸡蛋掉落 (Super Egg Drop)
- **题目链接**: https://leetcode.cn/problems/super-egg-drop/
- **题目描述**: 给你 k 枚相同的鸡蛋，并可以使用一栋从第 1 层到第 n 层共有 n 层楼的建筑。已知存在楼层 f ，满足 0 <= f <= n ，任何从 高于 f 的楼层落下的鸡蛋都会碎，从 f 楼层或比它低的楼层落下的鸡蛋都不会破。每次操作，你可以取一枚没有碎的鸡蛋并把它从任一楼层 x 扔下（满足 1 <= x <= n）。如果鸡蛋碎了，你就不能再次使用它。如果某枚鸡蛋没有碎，则可以重复使用。请你计算并返回要确定 f 确切的值 的最小操作次数是多少？

### 5. 预测赢家 (Predict the Winner)
- **题目链接**: https://leetcode.cn/problems/predict-the-winner/
- **题目描述**: 给定一个表示分数的非负整数数组。玩家 1 从数组任意一端拿取一个分数，随后玩家 2 继续从剩余数组任意一端拿取分数，然后玩家 1 拿，…… 。每次一个玩家只能拿取一个分数，分数被拿取之后不再可取。直到没有剩余分数可取时游戏结束。最终获得分数总和最多的玩家获胜。给定一个表示分数的数组，预测玩家 1 是否会成为赢家。你可以假设每个玩家的玩法都会使他的分数最大化。

### 6. 灯泡开关 IV (Bulb Switcher IV)
- **题目链接**: https://leetcode.cn/problems/bulb-switcher-iv/
- **题目描述**: 房间中有 n 个灯泡，编号从 0 到 n-1，自左向右排成一行。最开始的时候，所有的灯泡都是关着的。
  请你执行 m 次开关操作，其中第 i 次操作会切换所有编号为 i 的倍数的灯泡的状态。
  请你返回在 m 次操作后，有多少个灯泡是亮着的？

### 7. 粉刷房子 III (Paint House III)
- **题目链接**: https://leetcode.cn/problems/paint-house-iii/
- **题目描述**: 在一个小城市里，有 m 个房子排成一排，你需要给每个房子涂上 n 种颜色之一（颜色编号为 1 到 n）。有的房子去年夏天已经涂过颜色了，所以这些房子不需要再涂色。
  我们需要让相邻的房子颜色不同，并且要满足以下额外条件：恰好有 target 个街区，一个街区是指连续相同颜色的房子。
  请你计算并返回涂色方案的最小成本。如果没有满足条件的涂色方案，则返回 -1。

### 8. 投骰子的N种方法 (Number of Dice Rolls With Target Sum)
- **题目链接**: https://leetcode.cn/problems/number-of-dice-rolls-with-target-sum/
- **题目描述**: 有 d 个骰子，每个骰子有 f 个面，分别标号为 1, 2, ..., f。我们约定：掷骰子的得到总点数为各骰子面朝上的数字之和。如果需要掷出的总点数为 target，请你计算并返回有多少种不同的组合情况（所有可能的骰子面朝上的数字的组合），模 10^9 + 7。

## 三、路径计数动态规划问题

### 1. 不同路径 (Unique Paths)
- **题目链接**: https://leetcode.cn/problems/unique-paths/
- **题目描述**: 一个机器人位于一个 m x n 网格的左上角。机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角。问总共有多少条不同的路径？
- **实现文件**: Code08_UniquePaths.java, Code08_UniquePaths.py

### 2. 不同路径 II (Unique Paths II)
- **题目链接**: https://leetcode.cn/problems/unique-paths-ii/
- **题目描述**: 一个机器人位于一个 m x n 网格的左上角。网格中有障碍物。从左上角到右下角将有多少条不同的路径？

### 3. 最小路径和 (Minimum Path Sum)
- **题目链接**: https://leetcode.cn/problems/minimum-path-sum/
- **题目描述**: 给定一个包含非负整数的 m x n 网格 grid，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。

### 4. 矩阵中的最长递增路径 (Longest Increasing Path in a Matrix)
- **题目链接**: https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
- **题目描述**: 给定一个 m x n 整数矩阵 matrix ，找出其中 最长递增路径 的长度。
  对于每个单元格，你可以往四个方向移动：左、右、上、下。你不能在对角线方向上移动或移动到边界外（即不允许环绕）。

### 5. 地下城游戏 (Dungeon Game)
- **题目链接**: https://leetcode.cn/problems/dungeon-game/
- **题目描述**: 一些恶魔抓住了公主（P）并将她关在了地下城的右下角。地下城是由 M x N 个房间组成的二维网格。我们英勇的骑士（K）最初被安置在左上角的房间里。骑士的初始健康点数为一个正整数。如果他的健康点数在某一时刻降至 0 或以下，他会立即死亡。有些房间由恶魔守卫，因此骑士在进入这些房间时会失去健康点数（若房间里的值为负整数，则表示骑士将损失健康点数）；其他房间要么是空的（房间里的值为 0），要么包含增加骑士健康点数的魔法球（若房间里的值为正整数，则表示骑士将增加健康点数）。为了尽快解救到公主，骑士决定每次只向右或向下移动一步。返回确保骑士能够拯救到公主所需的最低初始健康点数。

### 6. 方格取数 (Grid Number Collection)
- **题目链接**: https://www.luogu.com.cn/problem/P1004
- **题目描述**: 在一个 N*N 的方格内，每个格子都有一个数字。从左上角出发，每次只能向右或向下移动一格，直到到达右下角。路上经过的每个格子中的数字都要被取走。共走两次，求两次取数的总和的最大值。（注意：每个格子中的数字只能被取一次）

### 7. 摘樱桃 (Cherry Pickup)
- **题目链接**: https://leetcode.cn/problems/cherry-pickup/
- **题目描述**: 一个N x N的网格(grid) 代表了一块樱桃地，每个格子由以下三种数字的一种来表示：
  - 0 表示这个格子是空的，没有樱桃。
  - 1 表示这个格子里有一个樱桃，可以摘下来。
  - -1 表示这个格子里有荆棘，任何时候都不能进入。
  你的任务是在遵守下列规则的情况下，尽可能多地摘樱桃：
  - 从位置 (0, 0) 出发，最后到达 (N-1, N-1) ，只能向下或向右移动。
  - 然后从 (N-1, N-1) 出发，最后回到 (0, 0) ，只能向上或向左移动。
  - 当经过一个格子且这个格子包含樱桃时，你将摘樱桃，然后这个格子变成空的。
  - 当经过一个格子且这个格子包含荆棘时，你的任务立即失败，并且永远不能到达终点。

### 8. 机器人的运动范围 (Robot's Range of Motion)
- **题目链接**: https://leetcode.cn/problems/ji-qi-ren-de-yun-dong-fan-wei-lcof/
- **题目描述**: 地上有一个m行n列的方格，从坐标 [0,0] 到坐标 [m-1,n-1]。一个机器人从坐标 [0, 0] 的格子开始移动，它每次可以向左、右、上、下移动一格（不能移动到方格外），也不能进入行坐标和列坐标的数位之和大于k的格子。例如，当k为18时，机器人能够进入方格 [35, 37] ，因为3+5+3+7=18。但它不能进入方格 [35, 38]，因为3+5+3+8=19。请问该机器人能够到达多少个格子？

## 四、字符串动态规划问题

### 1. 交错字符串 (Interleaving String)
- **题目链接**: https://leetcode.cn/problems/interleaving-string/
- **题目描述**: 给定三个字符串 s1、s2、s3，请帮忙验证 s3 是否由 s1 和 s2 交错组成。

### 2. 不同的子序列 (Distinct Subsequences)
- **题目链接**: https://leetcode.cn/problems/distinct-subsequences/
- **题目描述**: 给定一个字符串 s 和一个字符串 t，计算在 s 的子序列中 t 出现的个数。

### 3. 编辑距离 (Edit Distance)
- **题目链接**: https://leetcode.cn/problems/edit-distance/
- **题目描述**: 给你两个单词 word1 和 word2，请你计算出将 word1 转换成 word2 所使用的最少操作数。你可以对一个单词进行插入、删除或替换操作。

### 4. 最长公共子序列 (Longest Common Subsequence)
- **题目链接**: https://leetcode.cn/problems/longest-common-subsequence/
- **题目描述**: 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。一个字符串的子序列是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串。

### 5. 扰乱字符串 (Scramble String)
- **题目链接**: https://leetcode.cn/problems/scramble-string/
- **题目描述**: 使用下面描述的算法可以扰乱字符串 s 得到字符串 t ：步骤1 : 如果字符串的长度为 1 ，算法停止；步骤2 : 如果字符串的长度 > 1 ，执行下述步骤：在一个随机下标处将字符串分割成两个非空的子字符串，已知字符串s，则可以将其分成两个子字符串x和y且满足s=x+y，可以决定是要 交换两个子字符串 还是要 保持这两个子字符串的顺序不变，即s可能是 s = x + y 或者 s = y + x，在x和y这两个子字符串上继续从步骤1开始递归执行此算法。给你两个 长度相等 的字符串 s1 和 s2，判断 s2 是否是 s1 的扰乱字符串。
- **实现文件**: Code05_ScrambleString.java

### 6. 最长回文子序列 (Longest Palindromic Subsequence)
- **题目链接**: https://leetcode.cn/problems/longest-palindromic-subsequence/
- **题目描述**: 给定一个字符串 s ，找到其中最长的回文子序列，并返回该序列的长度。
  子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。

### 7. 最长递增子序列 (Longest Increasing Subsequence)
- **题目链接**: https://leetcode.cn/problems/longest-increasing-subsequence/
- **题目描述**: 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
  子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。

### 8. 通配符匹配 (Wildcard Matching)
- **题目链接**: https://leetcode.cn/problems/wildcard-matching/
- **题目描述**: 给定一个字符串 (s) 和一个字符模式 (p) ，实现一个支持 '?' 和 '*' 的通配符匹配。
  - '?' 可以匹配任何单个字符。
  - '*' 可以匹配任意字符串（包括空字符串）。
  两个字符串完全匹配才算匹配成功。

### 9. 正则表达式匹配 (Regular Expression Matching)
- **题目链接**: https://leetcode.cn/problems/regular-expression-matching/
- **题目描述**: 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
  - '.' 匹配任意单个字符
  - '*' 匹配零个或多个前面的那一个元素
  所谓匹配，是要涵盖 整个 字符串 s的，而不是部分字符串。

### 10. 最长有效括号 (Longest Valid Parentheses)
- **题目链接**: https://leetcode.cn/problems/longest-valid-parentheses/
- **题目描述**: 给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度。

## 五、其他高级动态规划问题

### 1. 打家劫舍 III (House Robber III)
- **题目链接**: https://leetcode.cn/problems/house-robber-iii/
- **题目描述**: 在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为"根"。 除了"根"之外，每栋房子有且只有一个"父"房子与之相连。一番侦察之后，聪明的小偷意识到"这个地方的所有房屋的排列类似于一棵二叉树"。 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
  计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。

### 2. 买卖股票的最佳时机 IV (Best Time to Buy and Sell Stock IV)
- **题目链接**: https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
- **题目描述**: 给定一个整数数组 prices ，它的第 i 个元素 prices[i] 是一支给定的股票在第 i 天的价格。
  设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。
  注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。

### 3. 合并石头的最低成本 (Minimum Cost to Merge Stones)
- **题目链接**: https://leetcode.cn/problems/minimum-cost-to-merge-stones/
- **题目描述**: 有 N 堆石头排成一排，第 i 堆中有 stones[i] 块石头。
  每次移动（move）需要将连续的 K 堆石头合并为一堆，而这个移动的成本为这 K 堆石头的总数。
  找出把所有石头合并成一堆的最低成本。如果不可能，返回 -1 。

### 4. 比特位计数 (Counting Bits)
- **题目链接**: https://leetcode.cn/problems/counting-bits/
- **题目描述**: 给你一个非负整数 num 。对于 0 ≤ i ≤ num 范围中的每个数字 i ，计算其二进制数中的 1 的数目，并将它们作为数组返回。

### 5. 最大子数组和 (Maximum Subarray)
- **题目链接**: https://leetcode.cn/problems/maximum-subarray/
- **题目描述**: 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。

### 6. 乘积最大子数组 (Maximum Product Subarray)
- **题目链接**: https://leetcode.cn/problems/maximum-product-subarray/
- **题目描述**: 给你一个整数数组 nums ，请你找出数组中乘积最大的非空连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。

### 7. 分割等和子集 (Partition Equal Subset Sum)
- **题目链接**: https://leetcode.cn/problems/partition-equal-subset-sum/
- **题目描述**: 给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。

### 8. 零钱兑换 (Coin Change)
- **题目链接**: https://leetcode.cn/problems/coin-change/
- **题目描述**: 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
  计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
  你可以认为每种硬币的数量是无限的。

## 五、线性动态规划问题

### 1. 爬楼梯 (Climbing Stairs)
- **题目链接**: https://leetcode.cn/problems/climbing-stairs/
- **题目描述**: 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
- **实现文件**: Code09_ClimbingStairs.java, Code09_ClimbingStairs.cpp, Code09_ClimbingStairs.py

### 2. 打家劫舍 (House Robber)
- **题目链接**: https://leetcode.cn/problems/house-robber/
- **题目描述**: 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下，一夜之内能够偷窃到的最高金额。
- **实现文件**: Code10_HouseRobber.java, Code10_HouseRobber.cpp, Code10_HouseRobber.py

### 3. 最长递增子序列 (Longest Increasing Subsequence)
- **题目链接**: https://leetcode.cn/problems/longest-increasing-subsequence/
- **题目描述**: 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
- **实现文件**: Code11_LongestIncreasingSubsequence.java, Code11_LongestIncreasingSubsequence.cpp, Code11_LongestIncreasingSubsequence.py