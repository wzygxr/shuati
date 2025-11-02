// -*- coding: utf-8 -*-
/**
 * DP融合场景：DP+数论、DP+字符串、DP+计算几何
 *
 * 问题描述：
 * 动态规划(DP)可以与其他领域的算法和数据结构结合，形成强大的问题解决方法。
 * 本文件实现了三种主要的融合场景：
 * 1. DP+数论（模意义下的动态规划）
 * 2. DP+字符串（基于后缀自动机的计数）
 * 3. DP+计算几何（凸包上的动态规划）
 *
 * 时间复杂度：
 * - DP+数论：根据具体问题而定，通常为O(n^2)或O(n^3)
 * - DP+字符串：O(n)或O(n^2)
 * - DP+计算几何：O(n^2)或O(n log n)
 *
 * 空间复杂度：O(n^2)
 *
 * 相关题目：
 * 1. LeetCode 518. 零钱兑换 II（模意义）
 * 2. LeetCode 682. 棒球比赛（字符串DP）
 * 3. LeetCode 873. 最长的斐波那契子序列的长度（序列DP）
 */

import java.util.*;
import java.util.function.DoubleBinaryOperator;

public class DPFusion {
    
    // ==================== DP+数论（模意义）====================
    
    /**
     * LeetCode 518. 零钱兑换 II（模意义下的变种）
     * 题目链接：https://leetcode-cn.com/problems/coin-change-2/
     * 
     * 问题描述：
     * 给定不同面额的硬币和一个总金额。计算可以凑成总金额的硬币组合数。
     * 假设每一种面额的硬币有无限个。要求结果对给定的模数取余。
     * 
     * 解题思路：
     * 使用动态规划，定义dp[i]表示凑成金额i的组合数。
     * 状态转移方程：dp[i] = (dp[i] + dp[i-coin]) % mod，其中coin是硬币面额。
     * 
     * @param amount 总金额
     * @param coins 硬币面额数组
     * @param mod 模数
     * @return 可以凑成总金额的硬币组合数对mod取余的结果
     */
    public static int coinChangeMod(int amount, int[] coins, int mod) {
        long[] dp = new long[amount + 1];
        dp[0] = 1;  // 凑成金额0的方式只有1种（不选任何硬币）
        
        // 遍历每种硬币
        for (int coin : coins) {
            // 遍历金额，从coin开始
            for (int i = coin; i <= amount; ++i) {
                dp[i] = (dp[i] + dp[i - coin]) % mod;
            }
    
    // ==================== 高级优化体系：SMAWK算法 ====================
    /**
     * SMAWK算法实现
     * 
     * 问题描述：
     * 在Monge矩阵中快速找出每行的最小值的列索引
     * Monge矩阵满足性质：对于所有i ≤ k，j ≤ l，有A[i][j] + A[k][l] ≤ A[i][l] + A[k][j]
     * 
     * 解题思路：
     * 1. 使用递归分治的方法减少行数（通过row reduction）
     * 2. 递归求解缩小后的矩阵
     * 3. 利用Monge性质在剩余列中验证找到最小值
     * 
     * 应用题目：
     * - POJ 3709 K-Anonymous Sequence
     * - Codeforces 868F Yet Another Minimization Problem
     * 
     * 时间复杂度：O(m + n)，其中m是行数，n是列数
     * 空间复杂度：O(m + n)
     */
    public static class SMAWK {
        /**
         * 求解Monge矩阵每行的最小值的列索引
         * 
         * @param matrix Monge矩阵
         * @return 每行最小值的列索引数组
         */
        public static int[] solve(int[][] matrix) {
            if (matrix == null || matrix.length == 0) {
                return new int[0];
            }
            int m = matrix.length;
            int n = matrix[0].length;
            int[] result = new int[m];
            
            // 调用递归函数求解
            solveRecursive(matrix, 0, m - 1, 0, n - 1, result);
            
            return result;
        }
        
        /**
         * 递归求解SMAWK问题
         * 
         * @param matrix 输入矩阵
         * @param rowStart 行起始索引
         * @param rowEnd 行结束索引
         * @param colStart 列起始索引
         * @param colEnd 列结束索引
         * @param result 结果数组，存储每行最小值的列索引
         */
        private static void solveRecursive(int[][] matrix, int rowStart, int rowEnd, int colStart, int colEnd, int[] result) {
            int rowCount = rowEnd - rowStart + 1;
            int colCount = colEnd - colStart + 1;
            
            // 基础情况：只有一行
            if (rowCount == 1) {
                int minCol = colStart;
                int minVal = matrix[rowStart][colStart];
                for (int j = colStart + 1; j <= colEnd; j++) {
                    if (matrix[rowStart][j] < minVal) {
                        minVal = matrix[rowStart][j];
                        minCol = j;
                    }
                }
                result[rowStart] = minCol;
                return;
            }
            
            // 对奇数行进行Row Reduction
            int[] reducedRows = new int[(rowCount + 1) / 2];
            for (int i = 0; i < reducedRows.length; i++) {
                reducedRows[i] = rowStart + 2 * i;
            }
            
            // 递归求解缩小后的问题（行减半）
            solveRecursive(matrix, rowStart, rowEnd, colStart, colEnd, result);
            
            // 处理偶数行，利用已求得的奇数行的最优列进行验证
            int prevOpt = colStart;
            for (int i = rowStart; i <= rowEnd; i++) {
                if (i % 2 == 0) continue; // 跳过奇数行，已经处理过
                
                int startCol = (i > rowStart) ? result[i - 1] : colStart;
                int endCol = (i < rowEnd) ? result[i + 1] : colEnd;
                
                int minCol = startCol;
                int minVal = matrix[i][startCol];
                
                for (int j = startCol + 1; j <= endCol; j++) {
                    if (matrix[i][j] < minVal) {
                        minVal = matrix[i][j];
                        minCol = j;
                    }
                }
                
                result[i] = minCol;
            }
        }
    }
    
    // ==================== 高级优化体系：Aliens Trick ====================
    /**
     * Aliens Trick（WQS二分）实现
     * 
     * 问题描述：
     * 解决需要恰好分成k个部分的优化问题，通过二分查找约束参数λ
     * 
     * 解题思路：
     * 1. 二分查找λ值
     * 2. 对每个λ，求解不加k约束的问题，但在成本函数中加入λ的惩罚项
     * 3. 根据结果中的分割次数调整二分方向
     * 
     * 应用题目：
     * - LeetCode 410. Split Array Largest Sum
     * - Codeforces 868F Yet Another Minimization Problem
     * 
     * 时间复杂度：O(log(maxCost) * n^2)，其中maxCost是可能的最大成本
     * 空间复杂度：O(n)
     */
    public static class AliensTrick {
        /**
         * 将数组分成k个连续子数组，使各子数组和的最大值最小
         * 相当于LeetCode 410的解法
         * 
         * @param nums 输入数组
         * @param k 分成的部分数
         * @return 最小的最大子数组和
         */
        public static int splitArrayK(int[] nums, int k) {
            int n = nums.length;
            // 计算前缀和
            int[] prefixSum = new int[n + 1];
            for (int i = 0; i < n; i++) {
                prefixSum[i + 1] = prefixSum[i] + nums[i];
            }
            
            // 二分查找的范围
            int left = 0, right = prefixSum[n];
            for (int num : nums) {
                left = Math.max(left, num);
            }
            
            int result = right;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                int count = 1; // 至少一个分割
                int currentSum = 0;
                
                for (int num : nums) {
                    if (currentSum + num > mid) {
                        count++;
                        currentSum = num;
                    } else {
                        currentSum += num;
                    }
                }
                
                if (count <= k) {
                    result = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            
            return result;
        }
        
        /**
         * 使用WQS二分的更通用实现
         * 
         * @param nums 输入数组
         * @param k 分成的部分数
         * @return 最小成本
         */
        public static long splitArrayWithWQS(int[] nums, int k) {
            int n = nums.length;
            // 计算前缀和
            long[] prefixSum = new long[n + 1];
            for (int i = 0; i < n; i++) {
                prefixSum[i + 1] = prefixSum[i] + nums[i];
            }
            
            // 二分查找的范围
            long left = 0, right = prefixSum[n] * n;
            
            // WQS二分查找λ
            while (left < right) {
                long mid = left + (right - left) / 2;
                Pair<Long, Integer> result = computeDP(prefixSum, mid);
                
                if (result.second <= k) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            
            // 计算最终结果
            Pair<Long, Integer> finalResult = computeDP(prefixSum, left);
            // 调整结果，因为WQS二分在计算时加入了λ的惩罚
            return finalResult.first - left * k;
        }
        
        /**
         * 计算DP，其中成本函数加入了λ的惩罚
         * 
         * @param prefixSum 前缀和数组
         * @param lambda 惩罚参数
         * @return 一个Pair，第一个元素是总成本，第二个是分割次数
         */
        private static Pair<Long, Integer> computeDP(long[] prefixSum, long lambda) {
            int n = prefixSum.length - 1;
            long[] dp = new long[n + 1];
            int[] cnt = new int[n + 1];
            
            for (int i = 1; i <= n; i++) {
                dp[i] = Long.MAX_VALUE;
                for (int j = 0; j < i; j++) {
                    long cost = (prefixSum[i] - prefixSum[j]) + lambda;
                    if (dp[j] + cost < dp[i]) {
                        dp[i] = dp[j] + cost;
                        cnt[i] = cnt[j] + 1;
                    }
                }
            }
            
            return new Pair<>(dp[n], cnt[n]);
        }
        
        /**
         * 简单的Pair类，用于返回两个值
         */
        private static class Pair<T1, T2> {
            T1 first;
            T2 second;
            
            public Pair(T1 first, T2 second) {
                this.first = first;
                this.second = second;
            }
        }
    }
    
    // ==================== 图上DP转最短路：分层图最短路径 ====================
    /**
     * 分层图最短路径实现
     * 
     * 问题描述：
     * 在带有状态约束的图中寻找最短路径，使用分层图建模不同状态
     * 
     * 解题思路：
     * 1. 将每个节点按不同状态（如剩余步数、是否使用了某些能力等）分层
     * 2. 构建分层图，每层内和层间建立相应的边
     * 3. 使用Dijkstra算法在分层图中寻找最短路径
     * 
     * 应用题目：
     * - LeetCode 787. Cheapest Flights Within K Stops
     * - POJ 3255 Roadblocks
     * 
     * 时间复杂度：O((n*K + m) log(n*K))，其中K是分层数，m是边数
     * 空间复杂度：O(n*K + m)
     */
    public static class LayeredGraphShortestPath {
        /**
         * 找到最多经过K个中转站的最便宜航班
         * 相当于LeetCode 787的解法
         * 
         * @param n 城市数量
         * @param flights 航班信息，格式为[from, to, price]
         * @param src 出发城市
         * @param dst 目的城市
         * @param maxStops 最大中转次数
         * @return 最便宜的机票价格，如果不存在则返回-1
         */
        public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int maxStops) {
            // 构建图的邻接表表示
            List<List<int[]>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            for (int[] flight : flights) {
                graph.get(flight[0]).add(new int[]{flight[1], flight[2]});
            }
            
            // 使用优先队列进行Dijkstra算法
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            pq.offer(new int[]{0, src, 0}); // [价格, 当前城市, 已停留次数]
            
            // 记录到达每个城市的每个停留次数的最小价格
            int[][] prices = new int[n][maxStops + 2];
            for (int[] row : prices) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
            prices[src][0] = 0;
            
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int price = current[0];
                int city = current[1];
                int stops = current[2];
                
                // 如果到达目的地，直接返回价格
                if (city == dst) {
                    return price;
                }
                
                // 如果已经超过最大中转次数，跳过
                if (stops > maxStops) {
                    continue;
                }
                
                // 如果当前价格比已知的更贵，跳过
                if (price > prices[city][stops]) {
                    continue;
                }
                
                // 遍历所有可能的下一个城市
                for (int[] next : graph.get(city)) {
                    int nextCity = next[0];
                    int nextPrice = price + next[1];
                    int nextStops = stops + 1;
                    
                    // 如果找到更便宜的路径，更新并加入优先队列
                    if (nextPrice < prices[nextCity][nextStops]) {
                        prices[nextCity][nextStops] = nextPrice;
                        pq.offer(new int[]{nextPrice, nextCity, nextStops});
                    }
                }
            }
            
            // 检查所有可能的停留次数中到达dst的最小价格
            int result = Integer.MAX_VALUE;
            for (int i = 0; i <= maxStops + 1; i++) {
                result = Math.min(result, prices[dst][i]);
            }
            
            return result == Integer.MAX_VALUE ? -1 : result;
        }
        
        /**
         * 求次短路径
         * 相当于POJ 3255 Roadblocks的解法
         * 
         * @param n 节点数量
         * @param edges 边的信息，格式为[from, to, weight]
         * @param start 起始节点
         * @param end 目标节点
         * @return 次短路径的长度
         */
        public static int findSecondShortestPath(int n, int[][] edges, int start, int end) {
            // 构建图的邻接表表示
            List<List<int[]>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                graph.get(edge[0]).add(new int[]{edge[1], edge[2]});
                graph.get(edge[1]).add(new int[]{edge[0], edge[2]}); // 无向图
            }
            
            // 记录每个节点的最短和次短距离
            int[][] dist = new int[n][2];
            for (int i = 0; i < n; i++) {
                Arrays.fill(dist[i], Integer.MAX_VALUE);
            }
            dist[start][0] = 0;
            
            // 使用优先队列进行Dijkstra算法
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            pq.offer(new int[]{0, start, 0}); // [距离, 当前节点, 标记是最短(0)还是次短(1)]
            
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int d = current[0];
                int node = current[1];
                int type = current[2];
                
                // 如果当前距离比已知的更差，跳过
                if (d > dist[node][type]) {
                    continue;
                }
                
                // 遍历所有邻居
                for (int[] neighbor : graph.get(node)) {
                    int nextNode = neighbor[0];
                    int weight = neighbor[1];
                    int newDist = d + weight;
                    
                    // 如果可以更新最短距离
                    if (newDist < dist[nextNode][0]) {
                        dist[nextNode][1] = dist[nextNode][0]; // 将原最短距离降级为次短
                        dist[nextNode][0] = newDist; // 更新最短距离
                        pq.offer(new int[]{newDist, nextNode, 0});
                        pq.offer(new int[]{dist[nextNode][1], nextNode, 1});
                    } 
                    // 如果可以更新次短距离，但不与最短距离相同
                    else if (newDist > dist[nextNode][0] && newDist < dist[nextNode][1]) {
                        dist[nextNode][1] = newDist;
                        pq.offer(new int[]{newDist, nextNode, 1});
                    }
                }
            }
            
            return dist[end][1];
        }
    }
    
    // ==================== 冷门模型：期望DP与高斯消元 ====================
    /**
     * 期望DP与高斯消元实现
     * 
     * 问题描述：
     * 解决包含环的期望DP问题，通过建立方程组并使用高斯消元求解
     * 
     * 解题思路：
     * 1. 建立状态转移方程
     * 2. 对于包含环的情况，将状态转移方程转换为线性方程组
     * 3. 使用高斯消元法求解线性方程组
     * 
     * 应用题目：
     * - LeetCode 837. New 21 Game
     * - POJ 2096 Collecting Bugs
     * 
     * 时间复杂度：
     * - New 21 Game: O(N + K + W)
     * - 高斯消元部分: O(n^3)，其中n是状态数
     * 空间复杂度：O(N + K + W) 或 O(n^2)
     */
    public static class ExpectationDPWithGaussian {
        /**
         * 新21点游戏获胜概率
         * 相当于LeetCode 837的解法
         * 
         * @param N 目标值
         * @param K 当前点数小于K时继续抽牌
         * @param W 每次抽牌的最大值
         * @return 获胜的概率
         */
        public static double new21Game(int N, int K, int W) {
            // 边界条件处理
            if (K == 0 || N >= K + W) {
                return 1.0;
            }
            
            // dp[x]表示当前点数为x时，最终获胜的概率
            double[] dp = new double[K + W + 1];
            
            // 初始化边界条件
            for (int i = K; i <= N; i++) {
                dp[i] = 1.0;
            }
            
            // 计算前缀和，优化计算
            double sum = N - K + 1.0; // dp[K]到dp[N]的和
            
            // 从后往前计算dp值
            for (int i = K - 1; i >= 0; i--) {
                dp[i] = sum / W;
                sum = sum + dp[i] - dp[i + W];
            }
            
            return dp[0];
        }
        
        /**
         * 高斯消元法求解线性方程组Ax = b
         * 
         * @param A 系数矩阵
         * @param b 常数项向量
         * @return 解向量x
         */
        public static double[] gaussianElimination(double[][] A, double[] b) {
            int n = A.length;
            double[][] augMatrix = new double[n][n + 1];
            
            // 构造增广矩阵
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    augMatrix[i][j] = A[i][j];
                }
                augMatrix[i][n] = b[i];
            }
            
            // 高斯消元
            for (int i = 0; i < n; i++) {
                // 寻找主元
                int maxRow = i;
                for (int j = i + 1; j < n; j++) {
                    if (Math.abs(augMatrix[j][i]) > Math.abs(augMatrix[maxRow][i])) {
                        maxRow = j;
                    }
                }
                
                // 交换行
                double[] temp = augMatrix[i];
                augMatrix[i] = augMatrix[maxRow];
                augMatrix[maxRow] = temp;
                
                // 归一化主元行
                double pivot = augMatrix[i][i];
                for (int j = i; j <= n; j++) {
                    augMatrix[i][j] /= pivot;
                }
                
                // 消去其他行
                for (int j = 0; j < n; j++) {
                    if (j != i && Math.abs(augMatrix[j][i]) > 1e-9) {
                        double factor = augMatrix[j][i];
                        for (int k = i; k <= n; k++) {
                            augMatrix[j][k] -= factor * augMatrix[i][k];
                        }
                    }
                }
            }
            
            // 提取解
            double[] x = new double[n];
            for (int i = 0; i < n; i++) {
                x[i] = augMatrix[i][n];
            }
            
            return x;
        }
        
        /**
         * 求解环形房间期望停留时间问题
         * 
         * @param n 房间数量
         * @param transitions 转移概率矩阵
         * @param target 目标房间
         * @return 从每个房间到达目标房间的期望时间
         */
        public static double[] expectedTimeInRooms(int n, double[][] transitions, int target) {
            // 构建线性方程组
            double[][] A = new double[n][n];
            double[] b = new double[n];
            
            for (int i = 0; i < n; i++) {
                if (i == target) {
                    // 目标房间的期望时间为0
                    A[i][i] = 1.0;
                    b[i] = 0.0;
                } else {
                    A[i][i] = 1.0;
                    for (int j = 0; j < n; j++) {
                        if (i != j) {
                            A[i][j] -= transitions[i][j];
                        }
                    }
                    b[i] = 1.0; // 每一步需要1单位时间
                }
            }
            
            return gaussianElimination(A, b);
        }
    }
    
    // ==================== 冷门模型：插头DP ====================
    /**
     * 插头DP实现（轮廓线DP）
     * 
     * 问题描述：
     * 求解网格图中的回路计数、路径覆盖等问题
     * 
     * 解题思路：
     * 1. 使用状态压缩表示当前轮廓线的插头状态
     * 2. 逐格处理，根据当前位置和状态转移到下一个状态
     * 3. 使用哈希表或滚动数组优化空间复杂度
     * 
     * 应用题目：
     * - HDU 1693 Eat the Trees（网格回路计数）
     * - HDU 3377 Plan（单回路覆盖）
     * 
     * 时间复杂度：O(n*m*2^m)，其中n和m是网格的维度
     * 空间复杂度：O(2^m)，使用滚动数组优化
     */
    public static class PlugDP {
        /**
         * 计算网格中简单回路的数量
         * 
         * @param grid 网格，1表示可走，0表示障碍
         * @return 回路数量
         */
        public static int countGridCycles(int[][] grid) {
            if (grid == null || grid.length == 0 || grid[0].length == 0) {
                return 0;
            }
            
            int n = grid.length;
            int m = grid[0].length;
            
            // 预处理：确保至少有两个可走的格子
            int total = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    total += grid[i][j];
                }
            }
            if (total < 2) {
                return 0;
            }
            
            // 插头DP，使用哈希表记录状态
            Map<Long, Integer> dp = new HashMap<>();
            dp.put(0L, 1); // 初始状态
            
            int firstRow = 0, firstCol = 0;
            // 找到第一个可走的格子作为起点
            outerLoop:
            for (firstRow = 0; firstRow < n; firstRow++) {
                for (firstCol = 0; firstCol < m; firstCol++) {
                    if (grid[firstRow][firstCol] == 1) {
                        break outerLoop;
                    }
                }
            }
            
            // 逐格处理
            for (int i = 0; i < n; i++) {
                // 每处理完一行，状态需要左移一位
                Map<Long, Integer> newDp = new HashMap<>();
                for (long state : dp.keySet()) {
                    // 左移一位，但最右边补0
                    long newState = state << 2;
                    newDp.put(newState, dp.get(state));
                }
                dp = newDp;
                
                for (int j = 0; j < m; j++) {
                    if (grid[i][j] == 0) {
                        // 障碍物，跳过
                        continue;
                    }
                    
                    newDp = new HashMap<>();
                    for (Map.Entry<Long, Integer> entry : dp.entrySet()) {
                        long state = entry.getKey();
                        int count = entry.getValue();
                        
                        // 当前格子左侧和上方的插头状态
                        int left = (int)((state >> (2 * j)) & 3);
                        int up = (int)((state >> (2 * (j + 1))) & 3);
                        
                        // 跳过已经处理过的情况
                        if ((i == firstRow && j == firstCol) && (left != 0 || up != 0)) {
                            continue;
                        }
                        
                        // 处理不同的插头组合情况
                        if (left == 0 && up == 0) {
                            // 新建两个插头，仅在当前不是最后一个格子时
                            if (i < n - 1 && j < m - 1 && grid[i+1][j] == 1 && grid[i][j+1] == 1) {
                                long newState = state | (1L << (2 * j)) | (2L << (2 * (j + 1)));
                                newDp.put(newState, newDp.getOrDefault(newState, 0) + count);
                            }
                        } else if (left == 0 || up == 0) {
                            // 一个插头，延伸这个插头
                            int plug = Math.max(left, up);
                            // 向右延伸
                            if (j < m - 1 && grid[i][j+1] == 1) {
                                long newState = state & ~(3L << (2 * (j + 1))) & ~(3L << (2 * j));
                                newState |= (long)plug << (2 * (j + 1));
                                newDp.put(newState, newDp.getOrDefault(newState, 0) + count);
                            }
                            // 向下延伸
                            if (i < n - 1 && grid[i+1][j] == 1) {
                                long newState = state & ~(3L << (2 * (j + 1))) & ~(3L << (2 * j));
                                newState |= (long)plug << (2 * j);
                                newDp.put(newState, newDp.getOrDefault(newState, 0) + count);
                            }
                        } else if (left == up) {
                            // 两个相同的插头，形成环，仅在最后一个格子时
                            if (i == n - 1 && j == m - 1 && (state & ~(3L << (2 * j)) & ~(3L << (2 * (j + 1)))) == 0) {
                                newDp.put(0L, newDp.getOrDefault(0L, 0) + count);
                            }
                        } else {
                            // 两个不同的插头，连接它们
                            // 找到另一个相同的插头并替换
                            long newState = state & ~(3L << (2 * j)) & ~(3L << (2 * (j + 1)));
                            for (int k = 0; k < m + 1; k++) {
                                if (k == j || k == j + 1) continue;
                                int curr = (int)((newState >> (2 * k)) & 3);
                                if (curr == up) {
                                    newState &= ~(3L << (2 * k));
                                    newState |= (long)left << (2 * k);
                                    break;
                                }
                            }
                            newDp.put(newState, newDp.getOrDefault(newState, 0) + count);
                        }
                    }
                    dp = newDp;
                }
            }
            
            // 最终状态应该是0，表示闭合回路
            return dp.getOrDefault(0L, 0) / 2; // 因为每个环会被计算两次（顺时针和逆时针）
        }
    }
    
    // ==================== 冷门模型：树上背包优化 ====================
    /**
     * 树上背包优化实现（使用small-to-large合并）
     * 
     * 问题描述：
     * 在树上选择一些节点，满足背包容量限制，最大化价值
     * 
     * 解题思路：
     * 1. 使用后序遍历处理子树
     * 2. 应用small-to-large合并优化，将小的子树合并到较大的子树中
     * 3. 减少重复计算，提高效率
     * 
     * 应用题目：
     * - POJ 1947 Rebuilding Roads
     * - HDU 3516 Tree Construction
     * 
     * 时间复杂度：O(n*c)，其中c是背包容量
     * 空间复杂度：O(n*c)
     */
    public static class TreeKnapsackOptimized {
        /**
         * 计算树上背包的最大价值
         * 
         * @param n 节点数量
         * @param root 根节点
         * @param capacity 背包容量
         * @param tree 树的邻接表表示
         * @param weights 节点的重量数组
         * @param values 节点的价值数组
         * @return 最大价值
         */
        public static int maxTreeValue(int n, int root, int capacity, List<List<Integer>> tree, int[] weights, int[] values) {
            // 使用邻接表构建树，并记录父节点以避免循环
            int[][] dp = new int[n + 1][capacity + 1];
            boolean[] visited = new boolean[n + 1];
            
            // 后序遍历处理子树
            dfs(root, -1, capacity, tree, weights, values, dp, visited);
            
            return dp[root][capacity];
        }
        
        /**
         * 深度优先搜索处理子树
         * 
         * @param node 当前节点
         * @param parent 父节点
         * @param capacity 背包容量
         * @param tree 树的邻接表
         * @param weights 重量数组
         * @param values 价值数组
         * @param dp dp数组
         * @param visited 访问标记数组
         */
        private static void dfs(int node, int parent, int capacity, List<List<Integer>> tree, int[] weights, int[] values, int[][] dp, boolean[] visited) {
            visited[node] = true;
            
            // 初始化：只选当前节点
            int nodeWeight = weights[node];
            int nodeValue = values[node];
            for (int j = 0; j <= capacity; j++) {
                if (j >= nodeWeight) {
                    dp[node][j] = nodeValue;
                }
            }
            
            // 遍历所有子节点
            for (int child : tree.get(node)) {
                if (child == parent || visited[child]) {
                    continue;
                }
                
                // 先处理子节点
                dfs(child, node, capacity, tree, weights, values, dp, visited);
                
                // 使用small-to-large合并优化，从后往前遍历容量避免重复计算
                for (int j = capacity; j >= nodeWeight; j--) {
                    for (int k = 0; k <= j - nodeWeight; k++) {
                        dp[node][j] = Math.max(dp[node][j], dp[node][j - k] + dp[child][k]);
                    }
                }
            }
        }
        
        /**
         * 带有size优化的树上背包实现
         * 
         * @param n 节点数量
         * @param root 根节点
         * @param capacity 背包容量
         * @param tree 树的邻接表
         * @param weights 重量数组
         * @param values 价值数组
         * @return 最大价值
         */
        public static int maxTreeValueWithSizeOpt(int n, int root, int capacity, List<List<Integer>> tree, int[] weights, int[] values) {
            int[][] dp = new int[n + 1][capacity + 1];
            int[] size = new int[n + 1];
            
            // 计算每个子树的大小
            computeSize(root, -1, tree, size);
            
            // 后序遍历处理
            dfsWithSize(root, -1, capacity, tree, weights, values, dp, size);
            
            return dp[root][capacity];
        }
        
        /**
         * 计算子树大小
         */
        private static void computeSize(int node, int parent, List<List<Integer>> tree, int[] size) {
            size[node] = 1;
            for (int child : tree.get(node)) {
                if (child != parent) {
                    computeSize(child, node, tree, size);
                    size[node] += size[child];
                }
            }
        }
        
        /**
         * 使用size进行优化的深度优先搜索
         */
        private static void dfsWithSize(int node, int parent, int capacity, List<List<Integer>> tree, int[] weights, int[] values, int[][] dp, int[] size) {
            // 初始化
            int nodeWeight = weights[node];
            int nodeValue = values[node];
            dp[node][0] = 0;
            for (int j = 1; j <= capacity; j++) {
                dp[node][j] = j >= nodeWeight ? nodeValue : 0;
            }
            
            // 找到最大的子树
            int maxChild = -1;
            int maxSize = 0;
            for (int child : tree.get(node)) {
                if (child != parent && size[child] > maxSize) {
                    maxSize = size[child];
                    maxChild = child;
                }
            }
            
            // 优先处理最大的子树，以便应用small-to-large优化
            if (maxChild != -1) {
                dfsWithSize(maxChild, node, capacity, tree, weights, values, dp, size);
                
                // 合并其他子树
                for (int child : tree.get(node)) {
                    if (child == parent || child == maxChild) {
                        continue;
                    }
                    dfsWithSize(child, node, capacity, tree, weights, values, dp, size);
                    
                    // small-to-large合并：将较小的子树合并到较大的子树中
                    for (int j = capacity; j >= nodeWeight; j--) {
                        for (int k = 0; k <= j - nodeWeight; k++) {
                            dp[node][j] = Math.max(dp[node][j], dp[node][j - k] + dp[child][k]);
                        }
                    }
                }
            }
        }
    }
}
        }
        
        return (int) dp[amount];
    }
    
    /**
     * 矩阵乘法（模意义下）
     * 
     * @param a 矩阵a
     * @param b 矩阵b
     * @param mod 模数
     * @return 矩阵a和矩阵b的乘积对mod取余的结果
     */
    public static long[][] matrixMultiply(long[][] a, long[][] b, int mod) {
        int n = a.length;
        int m = b[0].length;
        int k = b.length;
        long[][] result = new long[n][m];
        
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                for (int p = 0; p < k; ++p) {
                    result[i][j] = (result[i][j] + a[i][p] * b[p][j]) % mod;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 矩阵快速幂（模意义下）
     * 
     * 问题描述：
     * 计算矩阵的幂，结果对mod取余。
     * 
     * 解题思路：
     * 使用快速幂算法，将矩阵的乘法在模意义下进行。
     * 
     * @param matrix 输入矩阵
     * @param power 幂次
     * @param mod 模数
     * @return 矩阵的幂对mod取余的结果
     */
    public static long[][] matrixPowerMod(long[][] matrix, int power, int mod) {
        int n = matrix.length;
        // 初始化结果为单位矩阵
        long[][] result = new long[n][n];
        for (int i = 0; i < n; ++i) {
            result[i][i] = 1;
        }
        
        // 快速幂算法
        while (power > 0) {
            if (power % 2 == 1) {
                // 矩阵乘法
                result = matrixMultiply(result, matrix, mod);
            }
            // 矩阵自乘
            matrix = matrixMultiply(matrix, matrix, mod);
            power /= 2;
        }
        
        return result;
    }
    
    // ==================== DP+字符串（SAM相关）====================
    
    /**
     * LeetCode 516. 最长回文子序列
     * 题目链接：https://leetcode-cn.com/problems/longest-palindromic-subsequence/
     * 
     * 问题描述：
     * 给定一个字符串s，找到其中最长的回文子序列。可以假设s的最大长度为1000。
     * 
     * 解题思路：
     * 使用区间DP，定义dp[i][j]表示字符串s在区间[i,j]内的最长回文子序列的长度。
     * 状态转移方程：
     * - 如果s[i] == s[j]，则dp[i][j] = dp[i+1][j-1] + 2
     * - 否则，dp[i][j] = max(dp[i+1][j], dp[i][j-1])
     * 
     * @param s 输入字符串
     * @return 最长回文子序列的长度
     */
    public static int longestPalindromicSubseq(String s) {
        int n = s.length();
        // dp[i][j]表示字符串s在区间[i,j]内的最长回文子序列的长度
        int[][] dp = new int[n][n];
        
        // 初始化单个字符的情况
        for (int i = 0; i < n; ++i) {
            dp[i][i] = 1;
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            // 枚举起点
            for (int i = 0; i <= n - length; ++i) {
                int j = i + length - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i+1][j-1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
                }
            }
        }
        
        return dp[0][n-1];
    }
    
    /**
     * 后缀自动机（Suffix Automaton）
     * 
     * 后缀自动机是一个可以表示字符串的所有子串的数据结构。
     * 它可以用于解决许多字符串问题，如子串匹配、最长重复子串等。
     */
    public static class SuffixAutomaton {
        private static class State {
            int len;  // 该状态能接受的最长字符串的长度
            int link;  // 后缀链接
            Map<Character, Integer> next;  // 转移函数
            int endposSize;  // endpos集合的大小
            
            public State() {
                this.len = 0;
                this.link = -1;
                this.next = new HashMap<>();
                this.endposSize = 0;
            }
        }
        
        private int size;
        private int last;
        private List<State> states;
        
        private void extend(char c) {
            int p = last;
            int curr = size;
            size++;
            states.add(new State());
            states.get(curr).len = states.get(p).len + 1;
            
            while (p != -1 && !states.get(p).next.containsKey(c)) {
                states.get(p).next.put(c, curr);
                p = states.get(p).link;
            }
            
            if (p == -1) {
                states.get(curr).link = 0;
            } else {
                int q = states.get(p).next.get(c);
                if (states.get(p).len + 1 == states.get(q).len) {
                    states.get(curr).link = q;
                } else {
                    int clone = size;
                    size++;
                    states.add(new State());
                    states.get(clone).len = states.get(p).len + 1;
                    states.get(clone).next.putAll(states.get(q).next);
                    states.get(clone).link = states.get(q).link;
                    
                    while (p != -1 && states.get(p).next.containsKey(c) && states.get(p).next.get(c) == q) {
                        states.get(p).next.put(c, clone);
                        p = states.get(p).link;
                    }
                    
                    states.get(q).link = clone;
                    states.get(curr).link = clone;
                }
            }
            
            last = curr;
        }
        
        private void calcEndposSize() {
            // 按len排序
            Integer[] order = new Integer[size];
            for (int i = 0; i < size; ++i) {
                order[i] = i;
            }
            Arrays.sort(order, (a, b) -> Integer.compare(states.get(b).len, states.get(a).len));
            
            // 初始化为1（每个状态至少对应一个结束位置）
            for (int i = 1; i < size; ++i) {
                states.get(i).endposSize = 1;
            }
            
            // 从长到短更新
            for (int u : order) {
                if (states.get(u).link != -1) {
                    states.get(states.get(u).link).endposSize += states.get(u).endposSize;
                }
            }
        }
        
        public SuffixAutomaton(String s) {
            size = 1;
            last = 0;
            states = new ArrayList<>();
            states.add(new State());
            
            // 构建后缀自动机
            for (char c : s.toCharArray()) {
                extend(c);
            }
            
            // 计算endpos集合的大小
            calcEndposSize();
        }
        
        /**
         * 计算不同子串的数量
         */
        public int countSubstrings() {
            int count = 0;
            for (int i = 1; i < size; ++i) {
                count += states.get(i).len - states.get(states.get(i).link).len;
            }
            return count;
        }
    }
    
    // ==================== DP+计算几何（凸包相关）====================
    
    /**
     * 点结构体
     */
    public static class Point implements Comparable<Point> {
        double x, y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public int compareTo(Point p) {
            if (this.x != p.x) {
                return Double.compare(this.x, p.x);
            }
            return Double.compare(this.y, p.y);
        }
        
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    
    /**
     * 计算叉积 (a - o) × (b - o)
     */
    public static double cross(Point o, Point a, Point b) {
        return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
    }
    
    /**
     * 计算凸包（Andrew算法）
     * 
     * 问题描述：
     * 给定平面上的点集，找出所有在凸包上的点。
     * 
     * 解题思路：
     * 1. 将点按x坐标排序，x相同按y排序
     * 2. 构建下凸壳和上凸壳
     * 3. 合并上下凸壳
     * 
     * @param points 点集
     * @return 凸包上的点集
     */
    public static List<Point> convexHull(List<Point> points) {
        // 按x坐标排序，x相同按y排序
        Collections.sort(points);
        int n = points.size();
        
        // 构建下凸壳
        List<Point> lower = new ArrayList<>();
        for (Point p : points) {
            while (lower.size() >= 2 && cross(lower.get(lower.size()-2), lower.get(lower.size()-1), p) <= 0) {
                lower.remove(lower.size()-1);
            }
            lower.add(p);
        }
        
        // 构建上凸壳
        List<Point> upper = new ArrayList<>();
        for (int i = n-1; i >= 0; --i) {
            Point p = points.get(i);
            while (upper.size() >= 2 && cross(upper.get(upper.size()-2), upper.get(upper.size()-1), p) <= 0) {
                upper.remove(upper.size()-1);
            }
            upper.add(p);
        }
        
        // 合并上下凸壳，去掉重复的端点
        List<Point> result = new ArrayList<>();
        for (int i = 0; i < lower.size()-1; ++i) {
            result.add(lower.get(i));
        }
        for (int i = 0; i < upper.size()-1; ++i) {
            result.add(upper.get(i));
        }
        return result;
    }
    
    /**
     * 凸包优化DP
     * 
     * 问题描述：
     * 当DP状态转移方程可以表示为dp[i] = min{dp[j] + a[i] * b[j]} + c[i]的形式时，
     * 可以使用凸包优化将时间复杂度从O(n^2)降低到O(n)或O(n log n)。
     * 
     * 解题思路：
     * 对于每个j，维护一条直线y = b[j] * x + dp[j]，然后对于每个i，查询x = a[i]时的最小值。
     * 当b[j]单调递增且a[i]单调递增时，可以使用单调队列优化。
     * 
     * @param dp DP数组
     * @param a a数组
     * @param b b数组
     * @return 优化后的DP数组
     */
    public static double[] convexHullTrick(double[] dp, double[] a, double[] b) {
        int n = dp.length;
        Deque<Integer> q = new LinkedList<>();  // 单调队列，存储直线的索引
        
        // 计算两条直线j1和j2的交点x坐标
        DoubleBinaryOperator getIntersection = (j1, j2) -> {
            // 直线j1: y = b[(int)j1] * x + dp[(int)j1]
            // 直线j2: y = b[(int)j2] * x + dp[(int)j2]
            if (b[(int)j1] == b[(int)j2]) {
                return Double.MAX_VALUE;
            }
            return (dp[(int)j2] - dp[(int)j1]) / (b[(int)j1] - b[(int)j2]);
        };
        
        // 初始化队列，加入第一个元素
        q.offerLast(0);
        
        // 对于每个i，找到最优的j
        for (int i = 1; i < n; ++i) {
            // 当队列中至少有两个元素，且第一个元素不如第二个元素优时，弹出第一个元素
            while (q.size() >= 2) {
                int j1 = q.peekFirst();
                // 获取第二个元素
                q.pollFirst();
                int j2 = q.peekFirst();
                q.offerFirst(j1);  // 放回第一个元素
                
                if (dp[j1] + a[i] * b[j1] >= dp[j2] + a[i] * b[j2]) {
                    q.pollFirst();
                } else {
                    break;
                }
            }
            
            // 使用队列中的第一个元素作为最优的j
            if (!q.isEmpty()) {
                int bestJ = q.peekFirst();
                dp[i] = Math.min(dp[i], dp[bestJ] + a[i] * b[bestJ]);
            }
            
            // 将当前i加入队列，维护队列的凸壳性质
            while (q.size() >= 2) {
                int j2 = q.pollLast();
                int j1 = q.peekLast();
                double x1 = (dp[j2] - dp[j1]) / (b[j1] - b[j2]);
                double x2 = (dp[i] - dp[j2]) / (b[j2] - b[i]);
                if (x1 >= x2) {
                    // 需要弹出j2
                    continue;
                } else {
                    // 把j2放回去
                    q.offerLast(j2);
                    break;
                }
            }
            q.offerLast(i);
        }
        
        return dp;
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 测试DP+数论
        int amount = 5;
        int[] coins = {1, 2, 5};
        int mod = (int)1e9 + 7;
        System.out.println("零钱兑换II（模意义）: " + coinChangeMod(amount, coins, mod));  // 应该输出 4
        
        // 测试矩阵快速幂
        long[][] matrix = {{1, 1}, {1, 0}};
        int power = 5;
        System.out.println("矩阵快速幂结果:");
        long[][] result = matrixPowerMod(matrix, power, mod);
        for (long[] row : result) {
            for (long num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        
        // 测试DP+字符串
        String s = "bbbab";
        System.out.println("最长回文子序列长度: " + longestPalindromicSubseq(s));  // 应该输出 4
        
        // 测试后缀自动机
        SuffixAutomaton sam = new SuffixAutomaton("banana");
        System.out.println("不同子串数量: " + sam.countSubstrings());  // 应该输出 15
        
        // 测试DP+计算几何
        List<Point> points = Arrays.asList(
            new Point(0, 0),
            new Point(1, 1),
            new Point(2, 0),
            new Point(1, -1)
        );
        List<Point> hull = convexHull(points);
        System.out.println("凸包上的点: " + hull);
        
        // 测试凸包优化DP
        double[] dp = {0, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
        double[] a = {1, 2, 3, 4, 5};
        double[] b = {1, 2, 3, 4, 5};
        double[] optimizedDp = convexHullTrick(dp, a, b);
        System.out.print("凸包优化DP结果: ");
        for (double num : optimizedDp) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
    
    // ==================== 优化体系：Knuth优化 ====================
    
    // Knuth优化用于优化形如dp[i][j] = min{dp[i][k] + dp[k+1][j]} + w(i,j)的DP
    // 当满足四边形不等式时，最优转移点单调
    // 四边形不等式：w(a,b) + w(c,d) ≤ w(a,d) + w(c,b)，其中a ≤ c ≤ b ≤ d
    // 单调性：w(b,c) ≤ w(a,d)，其中a ≤ b ≤ c ≤ d
    
    public static class KnuthOptimizationResult {
        int[][] dp;
        int[][] opt;
        
        public KnuthOptimizationResult(int[][] dp, int[][] opt) {
            this.dp = dp;
            this.opt = opt;
        }
    }
    
    public static KnuthOptimizationResult knuthOptimization(int n, BiFunction<Integer, Integer, Integer> costFunc) {
        /*
        Knuth优化的DP算法
        
        问题描述：
        解决区间DP问题，其中状态转移方程满足四边形不等式
        
        解题思路：
        1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
        2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
        3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
        
        参数：
            n: 区间长度
            costFunc: 计算区间(i,j)代价的函数
        
        返回：
            KnuthOptimizationResult: 包含dp数组和opt数组的结果类
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        */
        // 初始化dp和opt数组
        int[][] dp = new int[n + 1][n + 1];
        int[][] opt = new int[n + 1][n + 1];
        
        // 初始化长度为1的区间
        for (int i = 1; i <= n; ++i) {
            dp[i][i] = 0;
            opt[i][i] = i;
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            // 枚举起始点
            for (int i = 1; i + length - 1 <= n; ++i) {
                int j = i + length - 1;
                // 初始化为无穷大
                dp[i][j] = Integer.MAX_VALUE;
                // 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
                int upperK = (i + 1 <= j) ? opt[i + 1][j] : j - 1;
                for (int k = opt[i][j - 1]; k <= Math.min(upperK, j - 1); ++k) {
                    if (dp[i][k] != Integer.MAX_VALUE && dp[k + 1][j] != Integer.MAX_VALUE) {
                        Integer cost = costFunc.apply(i, j);
                        if (cost != null && cost != Integer.MAX_VALUE) {
                            int current = dp[i][k] + dp[k + 1][j] + cost;
                            if (current < dp[i][j]) {
                                dp[i][j] = current;
                                opt[i][j] = k;
                            }
                        }
                    }
                }
            }
        }
        
        return new KnuthOptimizationResult(dp, opt);
    }
    
    // ==================== 优化体系：Divide & Conquer Optimization ====================
    
    public static int[][] divideConquerOptimization(int n, int m, BiFunction<Integer, Integer, Integer> costFunc) {
        /*
        Divide & Conquer Optimization（分治优化）
        
        问题描述：
        解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
        当转移满足决策单调性时使用
        
        解题思路：
        1. 利用决策单调性，使用分治法优化DP
        2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
        3. 使用分治的方式计算每个区间的最优决策
        
        参数：
            n: 维度1
            m: 维度2
            costFunc: 计算cost(k,j)的函数
        
        返回：
            int[][]: dp数组
        
        时间复杂度：O(n*m log m)
        空间复杂度：O(n*m)
        */
        // 初始化dp数组
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; ++i) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        dp[0][0] = 0;
        
        // 对每个i应用分治优化
        for (int i = 1; i <= n; ++i) {
            solveDivideConquer(i, 1, m, 0, m, dp, costFunc);
        }
        
        return dp;
    }
    
    private static void solveDivideConquer(int i, int l, int r, int opt_l, int opt_r, 
                                         int[][] dp, BiFunction<Integer, Integer, Integer> costFunc) {
        /*
        计算dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
        */
        if (l > r) {
            return;
        }
        
        int mid = (l + r) / 2;
        int best_k = opt_l;
        
        // 在opt_l到min(mid-1, opt_r)之间寻找最优k
        for (int k = opt_l; k <= Math.min(mid, opt_r); ++k) {
            if (dp[i - 1][k] != Integer.MAX_VALUE) {
                Integer cost = costFunc.apply(k, mid);
                if (cost != null && cost != Integer.MAX_VALUE) {
                    int current = dp[i - 1][k] + cost;
                    if (current < dp[i][mid]) {
                        dp[i][mid] = current;
                        best_k = k;
                    }
                }
            }
        }
        
        // 递归处理左右子区间
        solveDivideConquer(i, l, mid - 1, opt_l, best_k, dp, costFunc);
        solveDivideConquer(i, mid + 1, r, best_k, opt_r, dp, costFunc);
    }
    
    // ==================== 优化体系：SMAWK算法（行最小查询） ====================
    
    public static int[] smawk(int[][] matrix) {
        /*
        SMAWK算法用于在Monge矩阵中快速查找每行的最小值
        
        问题描述：
        给定一个Monge矩阵，快速找到每行的最小值位置
        
        解题思路：
        1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
        2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
        3. 主要步骤包括行压缩和递归求解
        
        参数：
            matrix: 一个Monge矩阵
        
        返回：
            int[]: 每行最小值的列索引
        
        时间复杂度：O(m+n)，其中m是行数，n是列数
        空间复杂度：O(m+n)
        */
        int m = matrix.length;
        if (m == 0) {
            return new int[0];
        }
        int n = matrix[0].length;
        
        // 构造行索引和列索引数组
        List<Integer> rows = new ArrayList<>();
        List<Integer> cols = new ArrayList<>();
        for (int i = 0; i < m; ++i) rows.add(i);
        for (int i = 0; i < n; ++i) cols.add(i);
        
        // 调用递归实现
        List<Integer> result = smawkRec(rows, cols, matrix);
        
        // 转换为数组
        int[] resArray = new int[m];
        for (int i = 0; i < m; ++i) {
            resArray[i] = result.get(i);
        }
        
        return resArray;
    }
    
    private static List<Integer> reduceRows(List<Integer> rows, int[][] matrix) {
        /*行压缩：只保留可能成为最小值的行*/
        List<Integer> stack = new ArrayList<>();
        for (int i : rows) {
            while (stack.size() >= 2) {
                int j1 = stack.get(stack.size() - 2);
                int j2 = stack.get(stack.size() - 1);
                // 比较两个行在列stack.size()-1处的值
                if (matrix[j1][stack.size() - 1] <= matrix[i][stack.size() - 1]) {
                    break;
                } else {
                    stack.remove(stack.size() - 1);
                }
            }
            stack.add(i);
        }
        return stack;
    }
    
    private static List<Integer> smawkRec(List<Integer> rows, List<Integer> cols, int[][] matrix) {
        /*递归实现SMAWK算法*/
        if (rows.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 行压缩
        List<Integer> reducedRows = reduceRows(rows, matrix);
        
        // 递归求解列数为奇数的子问题
        List<Integer> halfCols = new ArrayList<>();
        for (int i = 1; i < cols.size(); i += 2) {
            halfCols.add(cols.get(i));
        }
        List<Integer> minCols = new ArrayList<>(Collections.nCopies(reducedRows.size(), -1));
        
        if (!halfCols.isEmpty()) {
            // 递归求解
            List<Integer> result = smawkRec(reducedRows, halfCols, matrix);
            // 复制结果
            for (int i = 0; i < result.size(); ++i) {
                minCols.set(i, result.get(i));
            }
        }
        
        // 扩展结果到所有列
        List<Integer> result = new ArrayList<>(Collections.nCopies(rows.size(), 0));
        int k = 0;  // minCols的索引
        
        for (int i = 0; i < rows.size(); ++i) {
            int row = rows.get(i);
            // 确定当前行的最小值可能在哪个区间
            int start = (i == 0) ? 0 : (k > 0 ? minCols.get(k - 1) : 0);
            int end = (k < minCols.size()) ? minCols.get(k) : cols.get(cols.size() - 1);
            
            // 在这个区间内查找最小值
            int minVal = Integer.MAX_VALUE;
            int minCol = start;
            
            // 注意这里cols是原始列的子集，需要在cols中遍历
            int startIndex = cols.indexOf(start);
            int endIndex = cols.indexOf(end);
            if (startIndex != -1 && endIndex != -1) {
                for (int j = startIndex; j <= endIndex; ++j) {
                    int col = cols.get(j);
                    if (col < matrix[0].length && matrix[row][col] < minVal) {
                        minVal = matrix[row][col];
                        minCol = col;
                    }
                }
            }
            
            result.set(i, minCol);
            
            // 如果当前行在reducedRows中，且不是最后一行，k前进
            if (k < reducedRows.size() && row == reducedRows.get(k)) {
                k++;
            }
        }
        
        return result;
    }
    
    // ==================== 优化体系：Aliens Trick（二分约束参数+可行性DP） ====================
    
    public static class AliensTrickResult {
        double lambda;
        double value;
        
        public AliensTrickResult(double lambda, double value) {
            this.lambda = lambda;
            this.value = value;
        }
    }
    
    public static AliensTrickResult aliensTrick(BiFunction<Double, Double[], Double[]> costFunc, 
                                              DoublePredicate checkFunc, 
                                              double left, double right, double eps) {
        /*
        Aliens Trick（二分约束参数+可行性DP）
        
        问题描述：
        解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
        
        解题思路：
        1. 将约束条件转化为参数λ，构造拉格朗日函数
        2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
        3. 根据可行性DP的结果调整二分区间
        
        参数：
            costFunc: 计算带参数λ的成本函数，返回double[2]，其中[0]是当前值，[1]是约束值
            checkFunc: 检查当前解是否满足约束的函数
            left: 二分左边界
            right: 二分右边界
            eps: 精度要求
        
        返回：
            AliensTrickResult: 包含最优参数λ和对应最优解的结果类
        
        时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
        */
        double bestLambda = left;
        double bestValue = 0.0;
        
        while (right - left > eps) {
            double mid = (left + right) / 2;
            // 计算当前参数下的解和约束值
            Double[] result = costFunc.apply(mid, new Double[0]);
            if (result != null && result.length >= 2) {
                double currentValue = result[0];
                double constraintValue = result[1];
                
                if (checkFunc.test(constraintValue)) {
                    // 满足约束，尝试更小的参数
                    right = mid;
                    bestLambda = mid;
                    bestValue = currentValue;
                } else {
                    // 不满足约束，需要增大参数
                    left = mid;
                }
            }
        }
        
        return new AliensTrickResult(bestLambda, bestValue);
    }
    
    // 重载，提供默认精度
    public static AliensTrickResult aliensTrick(BiFunction<Double, Double[], Double[]> costFunc, 
                                              DoublePredicate checkFunc, 
                                              double left, double right) {
        return aliensTrick(costFunc, checkFunc, left, right, 1e-7);
    }
    
    // ==================== 图上DP→最短路：分层图建模 ====================
    
    public static int layeredGraphDijkstra(int n, int m, List<int[]> edges, int k) {
        /*
        分层图Dijkstra算法
        
        问题描述：
        给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
        
        解题思路：
        1. 构建分层图，每层代表使用不同次数的特殊操作
        2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
        3. 使用Dijkstra算法在分层图上寻找最短路径
        
        参数：
            n: 节点数量
            m: 边的数量
            edges: 边的列表，每个元素为[u, v, w]表示u到v的权为w的边
            k: 允许使用的特殊操作次数
        
        返回：
            int: 从节点0到节点n-1的最短路径长度
        
        时间复杂度：O((n*k + m*k) log(n*k))
        空间复杂度：O(n*k + m*k)
        */
        // 构建分层图的邻接表
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n * (k + 1); ++i) {
            graph.add(new ArrayList<>());
        }
        
        // 添加普通边（不使用特殊操作）
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            for (int i = 0; i <= k; ++i) {
                graph.get(u + i * n).add(new int[]{v + i * n, w});
            }
        }
        
        // 添加使用特殊操作的边（如果允许的话）
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            for (int i = 0; i < k; ++i) {
                // 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
                graph.get(u + i * n).add(new int[]{v + (i + 1) * n, 0});
            }
        }
        
        // Dijkstra算法
        int[] dist = new int[n * (k + 1)];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;  // 假设起点是节点0
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        heap.offer(new int[]{0, 0});  // (距离, 节点)
        
        while (!heap.isEmpty()) {
            int[] current = heap.poll();
            int d = current[0];
            int u = current[1];
            
            if (d > dist[u]) {
                continue;
            }
            
            for (int[] neighbor : graph.get(u)) {
                int v = neighbor[0];
                int w = neighbor[1];
                if (dist[v] > d + w) {
                    dist[v] = d + w;
                    heap.offer(new int[]{dist[v], v});
                }
            }
        }
        
        // 取所有层中到达终点的最小值
        int result = Integer.MAX_VALUE;
        for (int i = 0; i <= k; ++i) {
            if (dist[n - 1 + i * n] < result) {
                result = dist[n - 1 + i * n];
            }
        }
        
        return result;
    }
    
    // ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================
    
    public static double[] gaussianElimination(double[][] matrix) {
        /*
        高斯消元法求解线性方程组
        
        问题描述：
        求解形如Ax = b的线性方程组
        
        解题思路：
        1. 构建增广矩阵
        2. 进行高斯消元，将矩阵转化为行阶梯形
        3. 回代求解
        
        参数：
            matrix: 增广矩阵，每行最后一个元素是b的值
        
        返回：
            double[]: 方程组的解
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = matrix.length;
        final double eps = 1e-9;
        
        // 高斯消元过程
        for (int i = 0; i < n; ++i) {
            // 找到主元行（当前列中绝对值最大的行）
            int maxRow = i;
            for (int j = i; j < n; ++j) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[maxRow][i])) {
                    maxRow = j;
                }
            }
            
            // 交换主元行和当前行
            double[] temp = matrix[i];
            matrix[i] = matrix[maxRow];
            matrix[maxRow] = temp;
            
            // 如果主元为0，方程组可能有无穷多解或无解
            if (Math.abs(matrix[i][i]) < eps) {
                continue;
            }
            
            // 消元过程
            for (int j = i + 1; j < n; ++j) {
                double factor = matrix[j][i] / matrix[i][i];
                for (int k = i; k <= n; ++k) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
        }
        
        // 回代求解
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; --i) {
            x[i] = matrix[i][n];
            for (int j = i + 1; j < n; ++j) {
                x[i] -= matrix[i][j] * x[j];
            }
            x[i] /= matrix[i][i];
        }
        
        return x;
    }
    
    public static double[] expectationDPWithCycles(int n, List<List<double[]>> transitions) {
        /*
        期望DP处理有环情况（使用高斯消元）
        
        问题描述：
        在有环的状态转移图中计算期望
        
        解题思路：
        1. 对于每个状态，建立期望方程
        2. 使用高斯消元求解方程组
        
        参数：
            n: 状态数量
            transitions: 转移概率列表，transitions[i]是一个列表，每个元素为[j, p]表示从i转移到j的概率为p
        
        返回：
            double[]: 每个状态的期望值
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        // 构建线性方程组的增广矩阵
        double[][] matrix = new double[n][n + 1];
        
        for (int i = 0; i < n; ++i) {
            matrix[i][i] = 1.0;  // 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
            
            // 假设每个状态的代价为1，具体根据问题调整
            double cost = 1.0;
            matrix[i][n] = cost;
            
            for (double[] transition : transitions.get(i)) {
                int j = (int) transition[0];
                double p = transition[1];
                if (i != j) {  // 避免自环的特殊处理
                    matrix[i][j] -= p;
                }
            }
        }
        
        // 使用高斯消元求解
        return gaussianElimination(matrix);
    }
    
    // ==================== 冷门模型：插头DP（轮廓线DP） ====================
    
    public static int plugDP(int[][] grid) {
        /*
        插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
        
        问题描述：
        给定一个网格，求其中哈密顿回路的数量
        
        解题思路：
        1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
        2. 插头表示连接的状态，通常用二进制表示
        3. 使用哈希表优化空间复杂度
        
        参数：
            grid: 网格，1表示可通行，0表示障碍物
        
        返回：
            int: 哈密顿回路的数量
        
        时间复杂度：O(n*m*4^min(n,m))
        空间复杂度：O(4^min(n,m))
        */
        int n = grid.length;
        if (n == 0) {
            return 0;
        }
        int m = grid[0].length;
        
        // 使用哈希表优化
        Map<Long, Integer> dp = new HashMap<>();
        
        // 初始状态：左上角没有插头
        dp.put(0L, 1);
        
        for (int i = 0; i < n; ++i) {
            // 新的一行开始，需要将状态左移一位
            Map<Long, Integer> newDp = new HashMap<>();
            for (Map.Entry<Long, Integer> entry : dp.entrySet()) {
                long state = entry.getKey();
                int cnt = entry.getValue();
                // 左移一位，移除最左边的插头
                long newState = state << 1;
                newDp.put(newState, newDp.getOrDefault(newState, 0) + cnt);
            }
            dp = newDp;
            
            for (int j = 0; j < m; ++j) {
                Map<Long, Integer> newDp2 = new HashMap<>();
                
                for (Map.Entry<Long, Integer> entry : dp.entrySet()) {
                    long state = entry.getKey();
                    int cnt = entry.getValue();
                    
                    // 当前位置左边和上边的插头状态
                    int left = (int) ((state >> (2 * j)) & 3);
                    int up = (int) ((state >> (2 * (j + 1))) & 3);
                    
                    // 如果当前位置是障碍物，跳过
                    if (grid[i][j] == 0) {
                        // 只有当左右插头都不存在时才合法
                        if (left == 0 && up == 0) {
                            newDp2.put(state, newDp2.getOrDefault(state, 0) + cnt);
                        }
                        continue;
                    }
                    
                    // 处理各种插头组合情况
                    // 1. 没有左插头和上插头
                    if (left == 0 && up == 0) {
                        // 只能创建新的插头对（用于回路的开始）
                        if (i < n - 1 && j < m - 1 && grid[i+1][j] == 1 && grid[i][j+1] == 1) {
                            long newState = state | (1L << (2 * j)) | (2L << (2 * (j + 1)));
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                    
                    // 2. 只有左插头
                    else if (left != 0 && up == 0) {
                        // 向下延伸
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            newDp2.put(state, newDp2.getOrDefault(state, 0) + cnt);
                        }
                        // 向右延伸
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            long newState = (state & ~(3L << (2 * j))) | (left << (2 * (j + 1)));
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                    
                    // 3. 只有上插头
                    else if (left == 0 && up != 0) {
                        // 向右延伸
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            newDp2.put(state, newDp2.getOrDefault(state, 0) + cnt);
                        }
                        // 向下延伸
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            long newState = (state & ~(3L << (2 * (j + 1)))) | (up << (2 * j));
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                    
                    // 4. 同时有左插头和上插头
                    else {
                        // 合并插头
                        long newState = (state & ~(3L << (2 * j))) & ~(3L << (2 * (j + 1)));
                        
                        // 如果是形成回路的最后一步
                        if (left == up) {
                            // 检查是否所有插头都已连接
                            if (newState == 0 && i == n - 1 && j == m - 1) {
                                newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                            }
                        } else {
                            // 合并两个不同的插头
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                }
                
                dp = newDp2;
            }
        }
        
        // 最终状态应该是没有任何插头（形成回路）
        return dp.getOrDefault(0L, 0);
    }
    
    // ==================== 冷门模型：树上背包的优化 ====================
    
    public static int treeKnapsackOptimized(int root, int capacity, List<List<Integer>> tree, 
                                          int[] weights, int[] values) {
        /*
        树上背包的优化实现（小到大合并）
        
        问题描述：
        在树上选择一些节点，使得总重量不超过容量，且总价值最大
        
        解题思路：
        1. 使用后序遍历处理子树
        2. 使用小到大合并的策略优化复杂度
        3. 对于每个节点，维护一个容量为capacity的背包
        
        参数：
            root: 根节点
            capacity: 背包容量
            tree: 树的邻接表
            weights: 每个节点的重量
            values: 每个节点的价值
        
        返回：
            int: 最大价值
        
        时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
        空间复杂度：O(n*capacity)
        */
        int n = tree.size();
        int[][] dp = new int[n][capacity + 1];
        int[] size = new int[n];
        
        // 初始化dp数组
        for (int i = 0; i < n; ++i) {
            Arrays.fill(dp[i], 0);
        }
        
        // 深度优先搜索处理子树
        dfsTreeKnapsack(root, -1, capacity, tree, weights, values, dp, size);
        
        // 返回根节点的最大价值
        int maxVal = 0;
        for (int val : dp[root]) {
            maxVal = Math.max(maxVal, val);
        }
        return maxVal;
    }
    
    private static void dfsTreeKnapsack(int u, int parent, int capacity, 
                                     List<List<Integer>> tree, int[] weights, int[] values,
                                     int[][] dp, int[] size) {
        // 初始化当前节点
        size[u] = 1;
        if (weights[u] <= capacity) {
            dp[u][weights[u]] = values[u];
        }
        
        // 对每个子节点，按照子树大小排序，小的先合并
        List<int[]> children = new ArrayList<>();
        for (int v : tree.get(u)) {
            if (v != parent) {
                dfsTreeKnapsack(v, u, capacity, tree, weights, values, dp, size);
                children.add(new int[]{size[v], v});
            }
        }
        
        // 按子树大小排序
        children.sort(Comparator.comparingInt(a -> a[0]));
        
        for (int[] child : children) {
            int sz = child[0];
            int v = child[1];
            
            // 逆序遍历容量，避免重复计算
            for (int i = Math.min(size[u], capacity); i >= 0; --i) {
                if (dp[u][i] == 0 && i != 0) continue;
                for (int j = 1; j <= Math.min(sz, capacity - i); ++j) {
                    if (dp[v][j] > 0 && i + j <= capacity) {
                        dp[u][i + j] = Math.max(dp[u][i + j], dp[u][i] + dp[v][j]);
                    }
                }
            }
            
            // 更新子树大小
            size[u] += sz;
        }
    }
    
    // ==================== 补充题目与应用 ====================
    // 以下是一些使用上述高级DP技术的经典题目及其代码实现
    
    // 1. 编辑距离问题（LeetCode 72）
    public static int editDistance(String word1, String word2) {
        /*
        LeetCode 72. 编辑距离
        题目链接：https://leetcode-cn.com/problems/edit-distance/
        
        问题描述：
        给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
        你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
        
        解题思路：
        使用二维DP，dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数。
        
        时间复杂度：O(m*n)
        空间复杂度：O(m*n)
        */
        int m = word1.length();
        int n = word2.length();
        // dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界
        for (int i = 0; i <= m; ++i) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; ++j) {
            dp[0][j] = j;
        }
        
        // 动态规划填表
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 2. 最长递增子序列（LeetCode 300）
    public static int lengthOfLIS(int[] nums) {
        /*
        LeetCode 300. 最长递增子序列
        题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
        
        问题描述：
        给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
        
        解题思路：
        使用贪心 + 二分查找优化的DP方法。
        tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
        
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        */
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        List<Integer> tails = new ArrayList<>();
        for (int num : nums) {
            // 二分查找num应该插入的位置
            int left = 0, right = tails.size();
            while (left < right) {
                int mid = (left + right) / 2;
                if (tails.get(mid) < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            if (left == tails.size()) {
                tails.add(num);
            } else {
                tails.set(left, num);
            }
        }
        
        return tails.size();
    }
    
    // 3. 背包问题变种 - 完全背包（LeetCode 322）
    public static int coinChange(int[] coins, int amount) {
        /*
        LeetCode 322. 零钱兑换
        题目链接：https://leetcode-cn.com/problems/coin-change/
        
        问题描述：
        给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
        计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
        
        解题思路：
        使用完全背包的思想，dp[i]表示凑成金额i所需的最少硬币数。
        
        时间复杂度：O(amount * n)
        空间复杂度：O(amount)
        */
        // 初始化dp数组为无穷大
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;  // 凑成金额0需要0个硬币
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; ++i) {
                if (dp[i - coin] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }
    
    // 4. 矩阵链乘法（区间DP的经典应用）
    public static class MatrixChainResult {
        int[][] dp;
        int[][] s;
        
        public MatrixChainResult(int[][] dp, int[][] s) {
            this.dp = dp;
            this.s = s;
        }
    }
    
    public static MatrixChainResult matrixChainOrder(int[] p) {
        /*
        矩阵链乘法问题
        题目来源：算法导论
        
        问题描述：
        给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
        
        解题思路：
        使用区间DP，dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数。
        可以使用Knuth优化进一步降低时间复杂度。
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = p.length - 1;  // 矩阵的个数
        // dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数
        int[][] dp = new int[n + 1][n + 1];
        // s[i][j]记录最优分割点
        int[][] s = new int[n + 1][n + 1];
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            for (int i = 1; i + length - 1 <= n; ++i) {
                int j = i + length - 1;
                dp[i][j] = Integer.MAX_VALUE;
                // 枚举分割点
                for (int k = i; k < j; ++k) {
                    // 计算当前分割点的代价
                    int cost = dp[i][k] + dp[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
        
        return new MatrixChainResult(dp, s);
    }
    
    // 5. 旅行商问题（TSP）的DP实现
    public static int travelingSalesmanProblem(int[][] graph) {
        /*
        旅行商问题
        题目来源：算法竞赛经典问题
        
        问题描述：
        给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
        
        解题思路：
        使用状态压缩DP，dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度。
        
        时间复杂度：O(n^2 * 2^n)
        空间复杂度：O(n * 2^n)
        */
        int n = graph.length;
        // dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度
        int[][] dp = new int[1 << n][n];
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        
        // 初始状态：只访问了起点，路径长度为0
        for (int i = 0; i < n; ++i) {
            dp[1 << i][i] = 0;
        }
        
        // 枚举所有可能的状态
        for (int mask = 1; mask < (1 << n); ++mask) {
            // 枚举当前所在的城市
            for (int u = 0; u < n; ++u) {
                if ((mask & (1 << u)) == 0) {
                    continue;
                }
                // 枚举下一个要访问的城市
                for (int v = 0; v < n; ++v) {
                    if ((mask & (1 << v)) != 0) {
                        continue;
                    }
                    int newMask = mask | (1 << v);
                    if (dp[mask][u] != Integer.MAX_VALUE && graph[u][v] != Integer.MAX_VALUE) {
                        dp[newMask][v] = Math.min(dp[newMask][v], dp[mask][u] + graph[u][v]);
                    }
                }
            }
        }
        
        // 找到最短的回路
        int result = Integer.MAX_VALUE;
        for (int u = 0; u < n; ++u) {
            if (dp[(1 << n) - 1][u] != Integer.MAX_VALUE && graph[u][0] != Integer.MAX_VALUE) {
                result = Math.min(result, dp[(1 << n) - 1][u] + graph[u][0]);
            }
        }
        
        return result;
    }
    
    // 6. 区间DP：最优三角剖分
    public static int minimumScoreTriangulation(int[] values) {
        /*
        LeetCode 1039. 多边形三角剖分的最低得分
        题目链接：https://leetcode-cn.com/problems/minimum-score-triangulation-of-polygon/
        
        问题描述：
        给定一个凸多边形，将其三角剖分，使得所有三角形的顶点乘积之和最小。
        
        解题思路：
        使用区间DP，dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分。
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = values.length;
        // dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分
        int[][] dp = new int[n][n];
        for (int[] row : dp) {
            Arrays.fill(row, 0);
        }
        
        // 枚举区间长度
        for (int length = 3; length <= n; ++length) {
            for (int i = 0; i + length - 1 < n; ++i) {
                int j = i + length - 1;
                dp[i][j] = Integer.MAX_VALUE;
                // 枚举中间点
                for (int k = i + 1; k < j; ++k) {
                    dp[i][j] = Math.min(dp[i][j], 
                                       dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    // 7. 博弈DP：石子游戏
    public static boolean stoneGame(int[] piles) {
        /*
        LeetCode 877. 石子游戏
        题目链接：https://leetcode-cn.com/problems/stone-game/
        
        问题描述：
        给定一个表示石子堆的数组，两个玩家轮流从两端取石子，每次只能取一个，取到最后一个石子的人获胜。
        判断先手是否必胜。
        
        解题思路：
        使用区间DP，dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分。
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        */
        int n = piles.length;
        // dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分
        int[][] dp = new int[n][n];
        
        // 初始化单个石子堆
        for (int i = 0; i < n; ++i) {
            dp[i][i] = piles[i];
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            for (int i = 0; i + length - 1 < n; ++i) {
                int j = i + length - 1;
                // 先手可以选择取左边或右边
                dp[i][j] = Math.max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }
        
        // 先手净胜分大于0则必胜
        return dp[0][n - 1] > 0;
    }
    
    // 8. 数位DP：统计1出现的次数
    public static int countDigitOne(int n) {
        /*
        LeetCode 233. 数字1的个数
        题目链接：https://leetcode-cn.com/problems/number-of-digit-one/
        
        问题描述：
        给定一个整数 n，计算所有小于等于 n 的非负整数中数字1出现的个数。
        
        解题思路：
        使用数位DP，逐位处理每一位上1出现的次数。
        
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        */
        if (n <= 0) {
            return 0;
        }
        
        String s = Integer.toString(n);
        int length = s.length();
        int count = 0;
        
        // 逐位处理
        for (int i = 0; i < length; ++i) {
            long high = 0;
            if (i > 0) {
                high = Long.parseLong(s.substring(0, i));
            }
            int current = s.charAt(i) - '0';
            long low = 0;
            if (i < length - 1) {
                low = Long.parseLong(s.substring(i + 1));
            }
            long digit = (long) Math.pow(10, length - i - 1);
            
            if (current == 0) {
                // 当前位为0，高位决定
                count += high * digit;
            } else if (current == 1) {
                // 当前位为1，高位+低位+1
                count += high * digit + low + 1;
            } else {
                // 当前位大于1，高位+1
                count += (high + 1) * digit;
            }
        }
        
        return count;
    }
    
    // 9. 树形DP：打家劫舍III
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    
    public static int[] robDFS(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        int[] left = robDFS(node.left);
        int[] right = robDFS(node.right);
        
        // 偷当前节点，不能偷子节点
        int robCurrent = node.val + left[1] + right[1];
        // 不偷当前节点，可以选择偷或不偷子节点
        int notRobCurrent = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        return new int[]{robCurrent, notRobCurrent};
    }
    
    public static int rob(TreeNode root) {
        /*
        LeetCode 337. 打家劫舍 III
        题目链接：https://leetcode-cn.com/problems/house-robber-iii/
        
        问题描述：
        在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
        这个地区只有一个入口，我们称之为“根”。除了“根”之外，每栋房子有且只有一个“父“房子与之相连。
        一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
        如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
        计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
        
        解题思路：
        使用树形DP，对于每个节点，维护两个状态：偷或不偷。
        
        时间复杂度：O(n)
        空间复杂度：O(h)，h为树的高度
        */
        int[] result = robDFS(root);
        return Math.max(result[0], result[1]);
    }
    
    // 10. 状态压缩DP：蒙斯特曼问题
    public static int monsterGame(int[][] grid) {
        /*
        蒙斯特曼问题
        题目来源：算法竞赛问题
        
        问题描述：
        在网格中放置怪物，使得任何两个怪物都不在同一行、同一列或对角线上。
        
        解题思路：
        使用状态压缩DP，dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数。
        
        时间复杂度：O(n * 2^n)
        空间复杂度：O(2^n)
        */
        int n = grid.length;
        // dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数
        long[] dp = new long[1 << n];
        dp[0] = 1;
        
        for (int i = 0; i < n; ++i) {
            long[] newDp = new long[1 << n];
            for (int mask = 0; mask < (1 << n); ++mask) {
                if (dp[mask] == 0) {
                    continue;
                }
                // 枚举所有可能的放置位置
                for (int j = 0; j < n; ++j) {
                    // 检查是否可以在(i,j)放置怪物
                    if ((mask & (1 << j)) == 0 && grid[i][j] == 1) {
                        // 检查对角线
                        boolean valid = true;
                        for (int k = 0; k < i; ++k) {
                            if ((mask & (1 << k)) != 0 && Math.abs(k - j) == i - k) {
                                valid = false;
                                break;
                            }
                        }
                        if (valid) {
                            newDp[mask | (1 << j)] += dp[mask];
                        }
                    }
                }
            }
            dp = newDp;
        }
        
        return (int) dp[(1 << n) - 1];
    }
    
    // 11. 高维DP：三维背包
    public static int threeDimensionKnapsack(int n, int[] capacity, int[][] items) {
        /*
        三维背包问题
        题目来源：算法竞赛问题
        
        问题描述：
        有n个物品，每个物品有体积、重量、价值三个属性，背包有体积和重量两个限制，求最大价值。
        
        解题思路：
        使用三维DP，dp[i][j][k]表示前i个物品，体积为j，重量为k时的最大价值。
        
        时间复杂度：O(n * V * W)
        空间复杂度：O(n * V * W)
        */
        int V = capacity[0];
        int W = capacity[1];
        // 初始化dp数组
        int[][][] dp = new int[n + 1][V + 1][W + 1];
        
        for (int i = 1; i <= n; ++i) {
            int v = items[i-1][0];
            int w = items[i-1][1];
            int val = items[i-1][2];
            for (int j = 0; j <= V; ++j) {
                for (int k = 0; k <= W; ++k) {
                    // 不选当前物品
                    dp[i][j][k] = dp[i-1][j][k];
                    // 选当前物品（如果有足够的空间）
                    if (j >= v && k >= w) {
                        dp[i][j][k] = Math.max(dp[i][j][k], dp[i-1][j-v][k-w] + val);
                    }
                }
            }
        }
        
        return dp[n][V][W];
    }
    
    // 12. 斜率优化DP示例
    public static class ConvexHullTrick {
        /*
        凸包优化技巧示例
        题目来源：算法竞赛问题
        
        问题描述：
        当状态转移方程形如dp[i] = min{dp[j] + a[i] * b[j] + c}时，可以使用凸包优化。
        
        解题思路：
        将转移方程转换为直线的形式，维护凸包以快速查询最小值。
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        */
        
        private class Line {
            long k, b;
            Line(long k, long b) {
                this.k = k;
                this.b = b;
            }
        }
        
        private Deque<Line> dq = new LinkedList<>();
        
        // 添加一条直线y = kx + b
        public void addLine(long k, long b) {
            // 当队列中至少有两条直线时，检查是否需要删除末尾的直线
            while (dq.size() >= 2) {
                Line l1 = dq.get(dq.size() - 2);
                Line l2 = dq.get(dq.size() - 1);
                // 判断直线l1和l2的交点是否在l2和新直线的交点右侧
                if ((l2.b - l1.b) * (k - l2.k) >= (b - l2.b) * (l2.k - l1.k)) {
                    dq.removeLast();
                } else {
                    break;
                }
            }
            dq.addLast(new Line(k, b));
        }
        
        // 查询x处的最小值
        public long query(long x) {
            // 如果队列中至少有两条直线，且第一条直线在x处的值大于第二条，删除第一条
            while (dq.size() >= 2) {
                Line l1 = dq.getFirst();
                Line l2 = dq.get(1);
                if (l1.k * x + l1.b >= l2.k * x + l2.b) {
                    dq.removeFirst();
                } else {
                    break;
                }
            }
            if (dq.isEmpty()) {
                return Long.MAX_VALUE;
            }
            Line l = dq.getFirst();
            return l.k * x + l.b;
        }
    }
    
    // ==================== 优化体系：Knuth优化 ====================
    
    // Knuth优化用于优化形如dp[i][j] = min{dp[i][k] + dp[k+1][j]} + w(i,j)的DP
    // 当满足四边形不等式时，最优转移点单调
    
    static class KnuthOptimizationResult {
        int[][] dp;
        int[][] opt;
        
        public KnuthOptimizationResult(int[][] dp, int[][] opt) {
            this.dp = dp;
            this.opt = opt;
        }
    }
    
    interface CostFunction {
        int cost(int i, int j);
    }
    
    static KnuthOptimizationResult knuthOptimization(int n, CostFunction costFunc) {
        /*
        Knuth优化的DP算法
        
        问题描述：
        解决区间DP问题，其中状态转移方程满足四边形不等式
        
        解题思路：
        1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
        2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
        3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
        
        参数：
            n: 区间长度
            costFunc: 计算区间(i,j)代价的函数
        
        返回：
            KnuthOptimizationResult: 包含dp数组和opt数组的结果类
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        */
        // 初始化dp和opt数组
        int[][] dp = new int[n + 1][n + 1];
        int[][] opt = new int[n + 1][n + 1];
        
        // 初始化长度为1的区间
        for (int i = 1; i <= n; ++i) {
            dp[i][i] = 0;
            opt[i][i] = i;
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            // 枚举起始点
            for (int i = 1; i + length - 1 <= n; ++i) {
                int j = i + length - 1;
                // 初始化为无穷大
                dp[i][j] = Integer.MAX_VALUE;
                // 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
                int upperK = (i + 1 <= j) ? opt[i + 1][j] : j - 1;
                for (int k = opt[i][j - 1]; k <= Math.min(upperK, j - 1); ++k) {
                    if (dp[i][k] != Integer.MAX_VALUE && dp[k + 1][j] != Integer.MAX_VALUE) {
                        int cost = costFunc.cost(i, j);
                        if (cost != Integer.MAX_VALUE) {
                            int current = dp[i][k] + dp[k + 1][j] + cost;
                            if (current < dp[i][j]) {
                                dp[i][j] = current;
                                opt[i][j] = k;
                            }
                        }
                    }
                }
            }
        }
        
        return new KnuthOptimizationResult(dp, opt);
    }
    
    // ==================== 优化体系：Divide & Conquer Optimization ====================
    
    private static void solveDivideConquer(int i, int l, int r, int opt_l, int opt_r, 
                                         int[][] dp, CostFunction costFunc) {
        /*
        计算dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
        */
        if (l > r) {
            return;
        }
        
        int mid = (l + r) / 2;
        int best_k = opt_l;
        
        // 在opt_l到min(mid, opt_r)之间寻找最优k
        for (int k = opt_l; k <= Math.min(mid, opt_r); ++k) {
            if (dp[i - 1][k] != Integer.MAX_VALUE) {
                int cost = costFunc.cost(k, mid);
                if (cost != Integer.MAX_VALUE) {
                    int current = dp[i - 1][k] + cost;
                    if (current < dp[i][mid]) {
                        dp[i][mid] = current;
                        best_k = k;
                    }
                }
            }
        }
        
        // 递归处理左右子区间
        solveDivideConquer(i, l, mid - 1, opt_l, best_k, dp, costFunc);
        solveDivideConquer(i, mid + 1, r, best_k, opt_r, dp, costFunc);
    }
    
    static int[][] divideConquerOptimization(int n, int m, CostFunction costFunc) {
        /*
        Divide & Conquer Optimization（分治优化）
        
        问题描述：
        解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
        当转移满足决策单调性时使用
        
        解题思路：
        1. 利用决策单调性，使用分治法优化DP
        2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
        3. 使用分治的方式计算每个区间的最优决策
        
        参数：
            n: 维度1
            m: 维度2
            costFunc: 计算cost(k,j)的函数
        
        返回：
            int[][]: dp数组
        
        时间复杂度：O(n*m log m)
        空间复杂度：O(n*m)
        */
        // 初始化dp数组
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; ++i) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        dp[0][0] = 0;
        
        // 对每个i应用分治优化
        for (int i = 1; i <= n; ++i) {
            solveDivideConquer(i, 1, m, 0, m, dp, costFunc);
        }
        
        return dp;
    }
    
    // ==================== 优化体系：SMAWK算法（行最小查询） ====================
    
    private static List<Integer> reduceRows(List<Integer> rows, int[][] matrix) {
        /*行压缩：只保留可能成为最小值的行*/
        Stack<Integer> stack = new Stack<>();
        for (int i : rows) {
            while (stack.size() >= 2) {
                int j1 = stack.get(stack.size() - 2);
                int j2 = stack.get(stack.size() - 1);
                // 比较两个行在列stack.size()-1处的值
                if (matrix[j1][stack.size() - 1] <= matrix[i][stack.size() - 1]) {
                    break;
                } else {
                    stack.pop();
                }
            }
            stack.push(i);
        }
        return new ArrayList<>(stack);
    }
    
    private static List<Integer> smawkRec(List<Integer> rows, List<Integer> cols, int[][] matrix) {
        /*递归实现SMAWK算法*/
        if (rows.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 行压缩
        List<Integer> reducedRows = reduceRows(rows, matrix);
        
        // 递归求解列数为奇数的子问题
        List<Integer> halfCols = new ArrayList<>();
        for (int i = 1; i < cols.size(); i += 2) {
            halfCols.add(cols.get(i));
        }
        List<Integer> minCols = new ArrayList<>(Collections.nCopies(reducedRows.size(), -1));
        
        if (!halfCols.isEmpty()) {
            // 递归求解
            List<Integer> result = smawkRec(reducedRows, halfCols, matrix);
            // 复制结果
            for (int i = 0; i < result.size(); ++i) {
                minCols.set(i, result.get(i));
            }
        }
        
        // 扩展结果到所有列
        List<Integer> result = new ArrayList<>(Collections.nCopies(rows.size(), 0));
        int k = 0;  // minCols的索引
        
        for (int i = 0; i < rows.size(); ++i) {
            int row = rows.get(i);
            // 确定当前行的最小值可能在哪个区间
            int start = (i == 0) ? 0 : (k > 0 ? minCols.get(k - 1) : 0);
            int end = (k < minCols.size()) ? minCols.get(k) : cols.get(cols.size() - 1);
            
            // 在这个区间内查找最小值
            int minVal = Integer.MAX_VALUE;
            int minCol = start;
            
            // 注意这里cols是原始列的子集，需要在cols中遍历
            int startIdx = cols.indexOf(start);
            int endIdx = cols.indexOf(end);
            if (startIdx != -1 && endIdx != -1) {
                for (int idx = startIdx; idx <= endIdx; ++idx) {
                    int col = cols.get(idx);
                    if (col < matrix[0].length && matrix[row][col] < minVal) {
                        minVal = matrix[row][col];
                        minCol = col;
                    }
                }
            }
            
            result.set(i, minCol);
            
            // 如果当前行在reducedRows中，且不是最后一行，k前进
            if (k < reducedRows.size() && row == reducedRows.get(k)) {
                k++;
            }
        }
        
        return result;
    }
    
    static int[] smawk(int[][] matrix) {
        /*
        SMAWK算法用于在Monge矩阵中快速查找每行的最小值
        
        问题描述：
        给定一个Monge矩阵，快速找到每行的最小值位置
        
        解题思路：
        1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
        2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
        3. 主要步骤包括行压缩和递归求解
        
        参数：
            matrix: 一个Monge矩阵
        
        返回：
            int[]: 每行最小值的列索引
        
        时间复杂度：O(m+n)，其中m是行数，n是列数
        空间复杂度：O(m+n)
        */
        int m = matrix.length;
        if (m == 0) {
            return new int[0];
        }
        int n = matrix[0].length;
        
        // 构造行索引和列索引数组
        List<Integer> rows = new ArrayList<>();
        List<Integer> cols = new ArrayList<>();
        for (int i = 0; i < m; ++i) rows.add(i);
        for (int i = 0; i < n; ++i) cols.add(i);
        
        // 调用递归实现
        List<Integer> resultList = smawkRec(rows, cols, matrix);
        
        // 转换为数组
        int[] result = new int[resultList.size()];
        for (int i = 0; i < resultList.size(); ++i) {
            result[i] = resultList.get(i);
        }
        
        return result;
    }
    
    // ==================== 优化体系：Aliens Trick（二分约束参数+可行性DP） ====================
    
    static class AliensTrickResult {
        double lambda;
        double value;
        
        public AliensTrickResult(double lambda, double value) {
            this.lambda = lambda;
            this.value = value;
        }
    }
    
    interface AliensCostFunction {
        double[] cost(double lambda); // 返回[value, constraint]
    }
    
    interface CheckFunction {
        boolean check(double constraint);
    }
    
    static AliensTrickResult aliensTrick(AliensCostFunction costFunc, CheckFunction checkFunc,
                                       double left, double right, double eps) {
        /*
        Aliens Trick（二分约束参数+可行性DP）
        
        问题描述：
        解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
        
        解题思路：
        1. 将约束条件转化为参数λ，构造拉格朗日函数
        2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
        3. 根据可行性DP的结果调整二分区间
        
        参数：
            costFunc: 计算带参数λ的成本函数，返回[value, constraint]数组
            checkFunc: 检查当前解是否满足约束的函数
            left: 二分左边界
            right: 二分右边界
            eps: 精度要求
        
        返回：
            AliensTrickResult: 包含最优参数λ和对应最优解的结果类
        
        时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
        */
        double bestLambda = left;
        double bestValue = 0.0;
        
        while (right - left > eps) {
            double mid = (left + right) / 2;
            // 计算当前参数下的解和约束值
            double[] result = costFunc.cost(mid);
            double currentValue = result[0];
            double constraintValue = result[1];
            
            if (checkFunc.check(constraintValue)) {
                // 满足约束，尝试更小的参数
                right = mid;
                bestLambda = mid;
                bestValue = currentValue;
            } else {
                // 不满足约束，需要增大参数
                left = mid;
            }
        }
        
        return new AliensTrickResult(bestLambda, bestValue);
    }
    
    // 重载，提供默认精度
    static AliensTrickResult aliensTrick(AliensCostFunction costFunc, CheckFunction checkFunc,
                                       double left, double right) {
        return aliensTrick(costFunc, checkFunc, left, right, 1e-7);
    }
    
    // ==================== 图上DP→最短路：分层图建模 ====================
    
    static int layeredGraphDijkstra(int n, int m, int[][] edges, int k) {
        /*
        分层图Dijkstra算法
        
        问题描述：
        给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
        
        解题思路：
        1. 构建分层图，每层代表使用不同次数的特殊操作
        2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
        3. 使用Dijkstra算法在分层图上寻找最短路径
        
        参数：
            n: 节点数量
            m: 边的数量
            edges: 边的列表，每个元素为[u, v, w]表示u到v的权为w的边
            k: 允许使用的特殊操作次数
        
        返回：
            int: 从节点0到节点n-1的最短路径长度
        
        时间复杂度：O((n*k + m*k) log(n*k))
        空间复杂度：O(n*k + m*k)
        */
        // 构建分层图的邻接表
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n * (k + 1); ++i) {
            graph.add(new ArrayList<>());
        }
        
        // 添加普通边（不使用特殊操作）
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            for (int i = 0; i <= k; ++i) {
                int from = u + i * n;
                graph.get(from).add(new int[]{v + i * n, w});
            }
        }
        
        // 添加使用特殊操作的边（如果允许的话）
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            for (int i = 0; i < k; ++i) {
                // 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
                int from = u + i * n;
                graph.get(from).add(new int[]{v + (i + 1) * n, 0});
            }
        }
        
        // Dijkstra算法
        int[] dist = new int[n * (k + 1)];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;  // 假设起点是节点0
        // 使用优先队列，按距离排序
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        heap.offer(new int[]{0, 0});  // (距离, 节点)
        
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            int d = top[0];
            int u = top[1];
            
            if (d > dist[u]) {
                continue;
            }
            
            for (int[] edge : graph.get(u)) {
                int v = edge[0];
                int w = edge[1];
                if (dist[v] > d + w) {
                    dist[v] = d + w;
                    heap.offer(new int[]{dist[v], v});
                }
            }
        }
        
        // 取所有层中到达终点的最小值
        int result = Integer.MAX_VALUE;
        for (int i = 0; i <= k; ++i) {
            result = Math.min(result, dist[n - 1 + i * n]);
        }
        
        return result;
    }
    
    // ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================
    
    static double[] gaussianElimination(double[][] matrix) {
        /*
        高斯消元法求解线性方程组
        
        问题描述：
        求解形如Ax = b的线性方程组
        
        解题思路：
        1. 构建增广矩阵
        2. 进行高斯消元，将矩阵转化为行阶梯形
        3. 回代求解
        
        参数：
            matrix: 增广矩阵，每行最后一个元素是b的值
        
        返回：
            double[]: 方程组的解
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = matrix.length;
        final double eps = 1e-9;
        
        // 高斯消元过程
        for (int i = 0; i < n; ++i) {
            // 找到主元行（当前列中绝对值最大的行）
            int maxRow = i;
            for (int j = i; j < n; ++j) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[maxRow][i])) {
                    maxRow = j;
                }
            }
            
            // 交换主元行和当前行
            double[] temp = matrix[i];
            matrix[i] = matrix[maxRow];
            matrix[maxRow] = temp;
            
            // 如果主元为0，方程组可能有无穷多解或无解
            if (Math.abs(matrix[i][i]) < eps) {
                continue;
            }
            
            // 消元过程
            for (int j = i + 1; j < n; ++j) {
                double factor = matrix[j][i] / matrix[i][i];
                for (int k = i; k <= n; ++k) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
        }
        
        // 回代求解
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; --i) {
            x[i] = matrix[i][n];
            for (int j = i + 1; j < n; ++j) {
                x[i] -= matrix[i][j] * x[j];
            }
            x[i] /= matrix[i][i];
        }
        
        return x;
    }
    
    static double[] expectationDPWithCycles(int n, List<List<double[]>> transitions) {
        /*
        期望DP处理有环情况（使用高斯消元）
        
        问题描述：
        在有环的状态转移图中计算期望
        
        解题思路：
        1. 对于每个状态，建立期望方程
        2. 使用高斯消元求解方程组
        
        参数：
            n: 状态数量
            transitions: 转移概率列表，transitions[i]是一个列表，每个元素为[j, p]表示从i转移到j的概率为p
        
        返回：
            double[]: 每个状态的期望值
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        // 构建线性方程组的增广矩阵
        double[][] matrix = new double[n][n + 1];
        
        for (int i = 0; i < n; ++i) {
            matrix[i][i] = 1.0;  // 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
            
            // 假设每个状态的代价为1，具体根据问题调整
            double cost = 1.0;
            matrix[i][n] = cost;
            
            for (double[] transition : transitions.get(i)) {
                int j = (int) transition[0];
                double p = transition[1];
                if (i != j) {  // 避免自环的特殊处理
                    matrix[i][j] -= p;
                }
            }
        }
        
        // 使用高斯消元求解
        return gaussianElimination(matrix);
    }
    
    // ==================== 冷门模型：插头DP（轮廓线DP） ====================
    
    static int plugDP(int[][] grid) {
        /*
        插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
        
        问题描述：
        给定一个网格，求其中哈密顿回路的数量
        
        解题思路：
        1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
        2. 插头表示连接的状态，通常用二进制表示
        3. 使用哈希表优化空间复杂度
        
        参数：
            grid: 网格，1表示可通行，0表示障碍物
        
        返回：
            int: 哈密顿回路的数量
        
        时间复杂度：O(n*m*4^min(n,m))
        空间复杂度：O(4^min(n,m))
        */
        int n = grid.length;
        if (n == 0) {
            return 0;
        }
        int m = grid[0].length;
        
        // 使用哈希表优化
        Map<Long, Integer> dp = new HashMap<>();
        
        // 初始状态：左上角没有插头
        dp.put(0L, 1);
        
        for (int i = 0; i < n; ++i) {
            // 新的一行开始，需要将状态左移一位
            Map<Long, Integer> newDp = new HashMap<>();
            for (Map.Entry<Long, Integer> entry : dp.entrySet()) {
                long state = entry.getKey();
                int cnt = entry.getValue();
                // 左移一位，移除最左边的插头
                long newState = state << 1;
                newDp.put(newState, newDp.getOrDefault(newState, 0) + cnt);
            }
            dp = newDp;
            
            for (int j = 0; j < m; ++j) {
                Map<Long, Integer> newDp2 = new HashMap<>();
                
                for (Map.Entry<Long, Integer> entry : dp.entrySet()) {
                    long state = entry.getKey();
                    int cnt = entry.getValue();
                    // 当前位置左边和上边的插头状态
                    int left = (int) ((state >> (2 * j)) & 3);
                    int up = (int) ((state >> (2 * (j + 1))) & 3);
                    
                    // 如果当前位置是障碍物，跳过
                    if (grid[i][j] == 0) {
                        // 只有当左右插头都不存在时才合法
                        if (left == 0 && up == 0) {
                            newDp2.put(state, newDp2.getOrDefault(state, 0) + cnt);
                        }
                        continue;
                    }
                    
                    // 处理各种插头组合情况
                    // 1. 没有左插头和上插头
                    if (left == 0 && up == 0) {
                        // 只能创建新的插头对（用于回路的开始）
                        if (i < n - 1 && j < m - 1 && grid[i+1][j] == 1 && grid[i][j+1] == 1) {
                            long newState = state | (1L << (2 * j)) | (2L << (2 * (j + 1)));
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                    
                    // 2. 只有左插头
                    else if (left != 0 && up == 0) {
                        // 向下延伸
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            newDp2.put(state, newDp2.getOrDefault(state, 0) + cnt);
                        }
                        // 向右延伸
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            long newState = (state & ~(3L << (2 * j))) | (left << (2 * (j + 1)));
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                    
                    // 3. 只有上插头
                    else if (left == 0 && up != 0) {
                        // 向右延伸
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            newDp2.put(state, newDp2.getOrDefault(state, 0) + cnt);
                        }
                        // 向下延伸
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            long newState = (state & ~(3L << (2 * (j + 1)))) | (up << (2 * j));
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                    
                    // 4. 同时有左插头和上插头
                    else {
                        // 合并插头
                        long newState = (state & ~(3L << (2 * j))) & ~(3L << (2 * (j + 1)));
                        
                        // 如果是形成回路的最后一步
                        if (left == up) {
                            // 检查是否所有插头都已连接
                            if (newState == 0 && i == n - 1 && j == m - 1) {
                                newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                            }
                        } else {
                            // 合并两个不同的插头
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                }
                
                dp = newDp2;
            }
        }
        
        // 最终状态应该是没有任何插头（形成回路）
        return dp.getOrDefault(0L, 0);
    }
    
    // ==================== 冷门模型：树上背包的优化 ====================
    
    private static void dfsTreeKnapsack(int u, int parent, int capacity, 
                                     List<List<Integer>> tree, int[] weights, 
                                     int[] values, int[][] dp, int[] size) {
        // 初始化当前节点
        size[u] = 1;
        if (weights[u] <= capacity) {
            dp[u][weights[u]] = values[u];
        }
        
        // 对每个子节点，按照子树大小排序，小的先合并
        List<int[]> children = new ArrayList<>();
        for (int v : tree.get(u)) {
            if (v != parent) {
                dfsTreeKnapsack(v, u, capacity, tree, weights, values, dp, size);
                children.add(new int[]{size[v], v});
            }
        }
        
        // 按子树大小排序
        Collections.sort(children, (a, b) -> a[0] - b[0]);
        
        for (int[] child : children) {
            int sz = child[0];
            int v = child[1];
            // 逆序遍历容量，避免重复计算
            for (int i = Math.min(size[u], capacity); i >= 0; --i) {
                if (dp[u][i] == 0 && i != 0) continue;
                for (int j = 1; j <= Math.min(sz, capacity - i); ++j) {
                    if (dp[v][j] > 0 && i + j <= capacity) {
                        dp[u][i + j] = Math.max(dp[u][i + j], dp[u][i] + dp[v][j]);
                    }
                }
            }
            
            // 更新子树大小
            size[u] += sz;
        }
    }
    
    static int treeKnapsackOptimized(int root, int capacity, List<List<Integer>> tree, 
                                   int[] weights, int[] values) {
        /*
        树上背包的优化实现（小到大合并）
        
        问题描述：
        在树上选择一些节点，使得总重量不超过容量，且总价值最大
        
        解题思路：
        1. 使用后序遍历处理子树
        2. 使用小到大合并的策略优化复杂度
        3. 对于每个节点，维护一个容量为capacity的背包
        
        参数：
            root: 根节点
            capacity: 背包容量
            tree: 树的邻接表
            weights: 每个节点的重量
            values: 每个节点的价值
        
        返回：
            int: 最大价值
        
        时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
        空间复杂度：O(n*capacity)
        */
        int n = tree.size();
        int[][] dp = new int[n][capacity + 1];
        int[] size = new int[n];
        
        // 深度优先搜索处理子树
        dfsTreeKnapsack(root, -1, capacity, tree, weights, values, dp, size);
        
        // 返回根节点的最大价值
        int maxVal = 0;
        for (int val : dp[root]) {
            maxVal = Math.max(maxVal, val);
        }
        return maxVal;
    }
    
    // ==================== 补充题目与应用 ====================
    // 以下是一些使用上述高级DP技术的经典题目及其代码实现
    
    // 1. 编辑距离问题（LeetCode 72）
    static int editDistance(String word1, String word2) {
        /*
        LeetCode 72. 编辑距离
        题目链接：https://leetcode-cn.com/problems/edit-distance/
        
        问题描述：
        给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
        你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
        
        解题思路：
        使用二维DP，dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数。
        
        时间复杂度：O(m*n)
        空间复杂度：O(m*n)
        */
        int m = word1.length();
        int n = word2.length();
        // dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界
        for (int i = 0; i <= m; ++i) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; ++j) {
            dp[0][j] = j;
        }
        
        // 动态规划填表
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 2. 最长递增子序列（LeetCode 300）
    static int lengthOfLIS(int[] nums) {
        /*
        LeetCode 300. 最长递增子序列
        题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
        
        问题描述：
        给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
        
        解题思路：
        使用贪心 + 二分查找优化的DP方法。
        tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
        
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        */
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        List<Integer> tails = new ArrayList<>();
        for (int num : nums) {
            // 二分查找num应该插入的位置
            int left = 0, right = tails.size();
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails.get(mid) >= num) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            if (left == tails.size()) {
                tails.add(num);
            } else {
                tails.set(left, num);
            }
        }
        
        return tails.size();
    }
    
    // 3. 背包问题变种 - 完全背包（LeetCode 322）
    static int coinChange(int[] coins, int amount) {
        /*
        LeetCode 322. 零钱兑换
        题目链接：https://leetcode-cn.com/problems/coin-change/
        
        问题描述：
        给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
        计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
        
        解题思路：
        使用完全背包的思想，dp[i]表示凑成金额i所需的最少硬币数。
        
        时间复杂度：O(amount * n)
        空间复杂度：O(amount)
        */
        // 初始化dp数组为无穷大
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;  // 凑成金额0需要0个硬币
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; ++i) {
                if (dp[i - coin] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }
    
    // 4. 矩阵链乘法（区间DP的经典应用）
    static class MatrixChainResult {
        int[][] dp;
        int[][] s;
        
        public MatrixChainResult(int[][] dp, int[][] s) {
            this.dp = dp;
            this.s = s;
        }
    }
    
    static MatrixChainResult matrixChainOrder(int[] p) {
        /*
        矩阵链乘法问题
        题目来源：算法导论
        
        问题描述：
        给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
        
        解题思路：
        使用区间DP，dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数。
        可以使用Knuth优化进一步降低时间复杂度。
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = p.length - 1;  // 矩阵的个数
        // dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数
        int[][] dp = new int[n + 1][n + 1];
        // s[i][j]记录最优分割点
        int[][] s = new int[n + 1][n + 1];
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            for (int i = 1; i + length - 1 <= n; ++i) {
                int j = i + length - 1;
                dp[i][j] = Integer.MAX_VALUE;
                // 枚举分割点
                for (int k = i; k < j; ++k) {
                    // 计算当前分割点的代价
                    int cost = dp[i][k] + dp[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
        
        return new MatrixChainResult(dp, s);
    }
    
    // 5. 旅行商问题（TSP）的DP实现
    static int travelingSalesmanProblem(int[][] graph) {
        /*
        旅行商问题
        题目来源：算法竞赛经典问题
        
        问题描述：
        给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
        
        解题思路：
        使用状态压缩DP，dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度。
        
        时间复杂度：O(n^2 * 2^n)
        空间复杂度：O(n * 2^n)
        */
        int n = graph.length;
        // dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度
        int[][] dp = new int[1 << n][n];
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        
        // 初始状态：只访问了起点，路径长度为0
        for (int i = 0; i < n; ++i) {
            dp[1 << i][i] = 0;
        }
        
        // 枚举所有可能的状态
        for (int mask = 1; mask < (1 << n); ++mask) {
            // 枚举当前所在的城市
            for (int u = 0; u < n; ++u) {
                if ((mask & (1 << u)) == 0) {
                    continue;
                }
                // 枚举下一个要访问的城市
                for (int v = 0; v < n; ++v) {
                    if ((mask & (1 << v)) != 0) {
                        continue;
                    }
                    int newMask = mask | (1 << v);
                    if (dp[mask][u] != Integer.MAX_VALUE && graph[u][v] != Integer.MAX_VALUE) {
                        dp[newMask][v] = Math.min(dp[newMask][v], dp[mask][u] + graph[u][v]);
                    }
                }
            }
        }
        
        // 找到最短的回路
        int result = Integer.MAX_VALUE;
        for (int u = 0; u < n; ++u) {
            if (dp[(1 << n) - 1][u] != Integer.MAX_VALUE && graph[u][0] != Integer.MAX_VALUE) {
                result = Math.min(result, dp[(1 << n) - 1][u] + graph[u][0]);
            }
        }
        
        return result;
    }
    
    // 6. 区间DP：最优三角剖分
    static int minimumScoreTriangulation(int[] values) {
        /*
        LeetCode 1039. 多边形三角剖分的最低得分
        题目链接：https://leetcode-cn.com/problems/minimum-score-triangulation-of-polygon/
        
        问题描述：
        给定一个凸多边形，将其三角剖分，使得所有三角形的顶点乘积之和最小。
        
        解题思路：
        使用区间DP，dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分。
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = values.length;
        // dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分
        int[][] dp = new int[n][n];
        
        // 枚举区间长度
        for (int length = 3; length <= n; ++length) {
            for (int i = 0; i + length - 1 < n; ++i) {
                int j = i + length - 1;
                dp[i][j] = Integer.MAX_VALUE;
                // 枚举中间点
                for (int k = i + 1; k < j; ++k) {
                    dp[i][j] = Math.min(dp[i][j], 
                                      dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    // 7. 博弈DP：石子游戏
    static boolean stoneGame(int[] piles) {
        /*
        LeetCode 877. 石子游戏
        题目链接：https://leetcode-cn.com/problems/stone-game/
        
        问题描述：
        给定一个表示石子堆的数组，两个玩家轮流从两端取石子，每次只能取一个，取到最后一个石子的人获胜。
        判断先手是否必胜。
        
        解题思路：
        使用区间DP，dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分。
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        */
        int n = piles.length;
        // dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分
        int[][] dp = new int[n][n];
        
        // 初始化单个石子堆
        for (int i = 0; i < n; ++i) {
            dp[i][i] = piles[i];
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            for (int i = 0; i + length - 1 < n; ++i) {
                int j = i + length - 1;
                // 先手可以选择取左边或右边
                dp[i][j] = Math.max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }
        
        // 先手净胜分大于0则必胜
        return dp[0][n - 1] > 0;
    }
    
    // 8. 数位DP：统计1出现的次数
    static int countDigitOne(int n) {
        /*
        LeetCode 233. 数字1的个数
        题目链接：https://leetcode-cn.com/problems/number-of-digit-one/
        
        问题描述：
        给定一个整数 n，计算所有小于等于 n 的非负整数中数字1出现的个数。
        
        解题思路：
        使用数位DP，逐位处理每一位上1出现的次数。
        
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        */
        if (n <= 0) {
            return 0;
        }
        
        String s = String.valueOf(n);
        int length = s.length();
        int count = 0;
        
        // 逐位处理
        for (int i = 0; i < length; ++i) {
            long high = 0;
            if (i > 0) {
                high = Long.parseLong(s.substring(0, i));
            }
            int current = s.charAt(i) - '0';
            long low = 0;
            if (i < length - 1) {
                low = Long.parseLong(s.substring(i + 1));
            }
            long digit = (long) Math.pow(10, length - i - 1);
            
            if (current == 0) {
                // 当前位为0，高位决定
                count += high * digit;
            } else if (current == 1) {
                // 当前位为1，高位+低位+1
                count += high * digit + low + 1;
            } else {
                // 当前位大于1，高位+1
                count += (high + 1) * digit;
            }
        }
        
        return count;
    }
    
    // 9. 树形DP：打家劫舍III
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    
    private static int[] robDFS(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        int[] left = robDFS(node.left);
        int[] right = robDFS(node.right);
        
        // 偷当前节点，不能偷子节点
        int robCurrent = node.val + left[1] + right[1];
        // 不偷当前节点，可以选择偷或不偷子节点
        int notRobCurrent = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        return new int[]{robCurrent, notRobCurrent};
    }
    
    static int rob(TreeNode root) {
        /*
        LeetCode 337. 打家劫舍 III
        题目链接：https://leetcode-cn.com/problems/house-robber-iii/
        
        问题描述：
        在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
        这个地区只有一个入口，我们称之为“根”。除了“根”之外，每栋房子有且只有一个“父“房子与之相连。
        一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
        如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
        计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
        
        解题思路：
        使用树形DP，对于每个节点，维护两个状态：偷或不偷。
        
        时间复杂度：O(n)
        空间复杂度：O(h)，h为树的高度
        */
        int[] result = robDFS(root);
        return Math.max(result[0], result[1]);
    }
    
    // 10. 状态压缩DP：蒙斯特曼问题
    static int monsterGame(int[][] grid) {
        /*
        蒙斯特曼问题
        题目来源：算法竞赛问题
        
        问题描述：
        在网格中放置怪物，使得任何两个怪物都不在同一行、同一列或对角线上。
        
        解题思路：
        使用状态压缩DP，dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数。
        
        时间复杂度：O(n * 2^n)
        空间复杂度：O(2^n)
        */
        int n = grid.length;
        // dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数
        long[] dp = new long[1 << n];
        dp[0] = 1;
        
        for (int i = 0; i < n; ++i) {
            long[] newDp = new long[1 << n];
            for (int mask = 0; mask < (1 << n); ++mask) {
                if (dp[mask] == 0) {
                    continue;
                }
                // 枚举所有可能的放置位置
                for (int j = 0; j < n; ++j) {
                    // 检查是否可以在(i,j)放置怪物
                    if ((mask & (1 << j)) == 0 && grid[i][j] == 1) {
                        // 检查对角线
                        boolean valid = true;
                        for (int k = 0; k < i; ++k) {
                            if ((mask & (1 << k)) != 0 && Math.abs(k - j) == i - k) {
                                valid = false;
                                break;
                            }
                        }
                        if (valid) {
                            newDp[mask | (1 << j)] += dp[mask];
                        }
                    }
                }
            }
            dp = newDp;
        }
        
        return (int) dp[(1 << n) - 1];
    }
    
    // 11. 高维DP：三维背包
    static int threeDimensionKnapsack(int n, int[] capacity, int[][] items) {
        /*
        三维背包问题
        题目来源：算法竞赛问题
        
        问题描述：
        有n个物品，每个物品有体积、重量、价值三个属性，背包有体积和重量两个限制，求最大价值。
        
        解题思路：
        使用三维DP，dp[i][j][k]表示前i个物品，体积为j，重量为k时的最大价值。
        
        时间复杂度：O(n * V * W)
        空间复杂度：O(n * V * W)
        */
        int V = capacity[0];
        int W = capacity[1];
        // 初始化dp数组
        int[][][] dp = new int[n + 1][V + 1][W + 1];
        
        for (int i = 1; i <= n; ++i) {
            int v = items[i-1][0];
            int w = items[i-1][1];
            int val = items[i-1][2];
            for (int j = 0; j <= V; ++j) {
                for (int k = 0; k <= W; ++k) {
                    // 不选当前物品
                    dp[i][j][k] = dp[i-1][j][k];
                    // 选当前物品（如果有足够的空间）
                    if (j >= v && k >= w) {
                        dp[i][j][k] = Math.max(dp[i][j][k], dp[i-1][j-v][k-w] + val);
                    }
                }
            }
        }
        
        return dp[n][V][W];
    }
    
    // 12. 斜率优化DP示例
    static class ConvexHullTrick {
        /*
        凸包优化技巧示例
        题目来源：算法竞赛问题
        
        问题描述：
        当状态转移方程形如dp[i] = min{dp[j] + a[i] * b[j] + c}时，可以使用凸包优化。
        
        解题思路：
        将转移方程转换为直线的形式，维护凸包以快速查询最小值。
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        */
        
        static class Line {
            long k, b;
            Line(long k, long b) {
                this.k = k;
                this.b = b;
            }
        }
        
        Deque<Line> dq = new LinkedList<>();
        
        // 添加一条直线y = kx + b
        public void addLine(long k, long b) {
            // 当队列中至少有两条直线时，检查是否需要删除末尾的直线
            while (dq.size() >= 2) {
                Line l1 = dq.get(dq.size() - 2);
                Line l2 = dq.getLast();
                // 判断直线l1和l2的交点是否在l2和新直线的交点右侧
                if ((l2.b - l1.b) * (k - l2.k) >= (b - l2.b) * (l2.k - l1.k)) {
                    dq.removeLast();
                } else {
                    break;
                }
            }
            dq.addLast(new Line(k, b));
        }
        
        // 查询x处的最小值
        public long query(long x) {
            // 如果队列中至少有两条直线，且第一条直线在x处的值大于第二条，删除第一条
            while (dq.size() >= 2) {
                Line l1 = dq.getFirst();
                Line l2 = dq.get(1);
                if (l1.k * x + l1.b >= l2.k * x + l2.b) {
                    dq.removeFirst();
                } else {
                    break;
                }
            }
            if (dq.isEmpty()) {
                return Long.MAX_VALUE;
            }
            Line l = dq.getFirst();
            return l.k * x + l.b;
        }
    }
    
    // ==================== 优化体系：Knuth优化 ====================
    
    // Knuth优化用于优化形如dp[i][j] = min{dp[i][k] + dp[k+1][j]} + w(i,j)的DP
    // 当满足四边形不等式时，最优转移点单调
    
    static class KnuthOptimizationResult {
        int[][] dp;
        int[][] opt;
        
        public KnuthOptimizationResult(int[][] dp, int[][] opt) {
            this.dp = dp;
            this.opt = opt;
        }
    }
    
    interface CostFunction {
        int apply(int i, int j);
    }
    
    public static KnuthOptimizationResult knuthOptimization(int n, CostFunction costFunc) {
        /*
        Knuth优化的DP算法
        
        问题描述：
        解决区间DP问题，其中状态转移方程满足四边形不等式
        
        解题思路：
        1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
        2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
        3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
        
        参数：
            n: 区间长度
            costFunc: 计算区间(i,j)代价的函数
        
        返回：
            KnuthOptimizationResult: 包含dp数组和opt数组的结果类
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        */
        // 初始化dp和opt数组
        int[][] dp = new int[n + 1][n + 1];
        int[][] opt = new int[n + 1][n + 1];
        
        // 初始化长度为1的区间
        for (int i = 1; i <= n; ++i) {
            dp[i][i] = 0;
            opt[i][i] = i;
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            // 枚举起始点
            for (int i = 1; i + length - 1 <= n; ++i) {
                int j = i + length - 1;
                // 初始化为无穷大
                dp[i][j] = Integer.MAX_VALUE;
                // 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
                int upperK = (i + 1 <= j) ? opt[i + 1][j] : j - 1;
                for (int k = opt[i][j - 1]; k <= Math.min(upperK, j - 1); ++k) {
                    if (dp[i][k] != Integer.MAX_VALUE && dp[k + 1][j] != Integer.MAX_VALUE) {
                        int cost = costFunc.apply(i, j);
                        if (cost != Integer.MAX_VALUE) {
                            int current = dp[i][k] + dp[k + 1][j] + cost;
                            if (current < dp[i][j]) {
                                dp[i][j] = current;
                                opt[i][j] = k;
                            }
                        }
                    }
                }
            }
        }
        
        return new KnuthOptimizationResult(dp, opt);
    }
    
    // ==================== 优化体系：Divide & Conquer Optimization ====================
    
    private static void solveDivideConquer(int i, int l, int r, int opt_l, int opt_r, 
                                          int[][] dp, CostFunction costFunc) {
        /*
        计算dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
        */
        if (l > r) {
            return;
        }
        
        int mid = (l + r) / 2;
        int best_k = opt_l;
        
        // 在opt_l到Math.min(mid, opt_r)之间寻找最优k
        for (int k = opt_l; k <= Math.min(mid, opt_r); ++k) {
            if (dp[i - 1][k] != Integer.MAX_VALUE) {
                int cost = costFunc.apply(k, mid);
                if (cost != Integer.MAX_VALUE) {
                    int current = dp[i - 1][k] + cost;
                    if (current < dp[i][mid]) {
                        dp[i][mid] = current;
                        best_k = k;
                    }
                }
            }
        }
        
        // 递归处理左右子区间
        solveDivideConquer(i, l, mid - 1, opt_l, best_k, dp, costFunc);
        solveDivideConquer(i, mid + 1, r, best_k, opt_r, dp, costFunc);
    }
    
    public static int[][] divideConquerOptimization(int n, int m, CostFunction costFunc) {
        /*
        Divide & Conquer Optimization（分治优化）
        
        问题描述：
        解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
        当转移满足决策单调性时使用
        
        解题思路：
        1. 利用决策单调性，使用分治法优化DP
        2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
        3. 使用分治的方式计算每个区间的最优决策
        
        参数：
            n: 维度1
            m: 维度2
            costFunc: 计算cost(k,j)的函数
        
        返回：
            int[][]: dp数组
        
        时间复杂度：O(n*m log m)
        空间复杂度：O(n*m)
        */
        // 初始化dp数组
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; ++i) {
            for (int j = 0; j <= m; ++j) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }
        dp[0][0] = 0;
        
        // 对每个i应用分治优化
        for (int i = 1; i <= n; ++i) {
            solveDivideConquer(i, 1, m, 0, m, dp, costFunc);
        }
        
        return dp;
    }
    
    // ==================== 优化体系：SMAWK算法（行最小查询） ====================
    
    private static List<Integer> reduceRows(List<Integer> rows, int[][] matrix) {
        /*行压缩：只保留可能成为最小值的行*/
        Stack<Integer> stk = new Stack<>();
        for (int i : rows) {
            while (stk.size() >= 2) {
                int j1 = stk.pop();
                int j2 = stk.peek();
                stk.push(j1); // 恢复栈状态
                
                // 比较两个行在列stk.size()-1处的值
                if (matrix[j2][stk.size() - 1] <= matrix[i][stk.size() - 1]) {
                    break;
                } else {
                    stk.pop();
                }
            }
            stk.push(i);
        }
        
        List<Integer> result = new ArrayList<>();
        while (!stk.isEmpty()) {
            result.add(stk.pop());
        }
        Collections.reverse(result);
        return result;
    }
    
    private static List<Integer> smawkRec(List<Integer> rows, List<Integer> cols, int[][] matrix) {
        /*递归实现SMAWK算法*/
        if (rows.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 行压缩
        List<Integer> reducedRows = reduceRows(rows, matrix);
        
        // 递归求解列数为奇数的子问题
        List<Integer> halfCols = new ArrayList<>();
        for (int i = 1; i < cols.size(); i += 2) {
            halfCols.add(cols.get(i));
        }
        int[] minCols = new int[reducedRows.size()];
        Arrays.fill(minCols, -1);
        
        if (!halfCols.isEmpty()) {
            // 递归求解
            List<Integer> result = smawkRec(reducedRows, halfCols, matrix);
            // 复制结果
            for (int i = 0; i < result.size(); ++i) {
                minCols[i] = result.get(i);
            }
        }
        
        // 扩展结果到所有列
        List<Integer> result = new ArrayList<>(Collections.nCopies(rows.size(), 0));
        int k = 0;  // minCols的索引
        
        for (int i = 0; i < rows.size(); ++i) {
            int row = rows.get(i);
            // 确定当前行的最小值可能在哪个区间
            int start = (i == 0) ? 0 : (k > 0 ? minCols[k - 1] : 0);
            int end = (k < minCols.length) ? minCols[k] : cols.get(cols.size() - 1);
            
            // 在这个区间内查找最小值
            int minVal = Integer.MAX_VALUE;
            int minCol = start;
            
            // 注意这里cols是原始列的子集，需要在cols中遍历
            int startIndex = cols.indexOf(start);
            int endIndex = cols.indexOf(end);
            if (startIndex != -1 && endIndex != -1) {
                for (int idx = startIndex; idx <= endIndex; ++idx) {
                    int col = cols.get(idx);
                    if (col < matrix[0].length && matrix[row][col] < minVal) {
                        minVal = matrix[row][col];
                        minCol = col;
                    }
                }
            }
            
            result.set(i, minCol);
            
            // 如果当前行在reducedRows中，且不是最后一行，k前进
            if (k < reducedRows.size() && row == reducedRows.get(k)) {
                k++;
            }
        }
        
        return result;
    }
    
    public static List<Integer> smawk(int[][] matrix) {
        /*
        SMAWK算法用于在Monge矩阵中快速查找每行的最小值
        
        问题描述：
        给定一个Monge矩阵，快速找到每行的最小值位置
        
        解题思路：
        1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
        2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
        3. 主要步骤包括行压缩和递归求解
        
        参数：
            matrix: 一个Monge矩阵
        
        返回：
            List<Integer>: 每行最小值的列索引
        
        时间复杂度：O(m+n)，其中m是行数，n是列数
        空间复杂度：O(m+n)
        */
        int m = matrix.length;
        if (m == 0) {
            return new ArrayList<>();
        }
        int n = matrix[0].length;
        
        // 构造行索引和列索引数组
        List<Integer> rows = new ArrayList<>();
        List<Integer> cols = new ArrayList<>();
        for (int i = 0; i < m; ++i) {
            rows.add(i);
        }
        for (int i = 0; i < n; ++i) {
            cols.add(i);
        }
        
        // 调用递归实现
        return smawkRec(rows, cols, matrix);
    }
    
    // ==================== 优化体系：Aliens Trick（二分约束参数+可行性DP） ====================
    
    static class AliensTrickResult {
        double lambda;
        double value;
        
        public AliensTrickResult(double lambda, double value) {
            this.lambda = lambda;
            this.value = value;
        }
    }
    
    interface AliensCostFunction {
        double[] apply(double lambda); // 返回[value, constraint]
    }
    
    interface CheckFunction {
        boolean apply(double constraintValue);
    }
    
    public static AliensTrickResult aliensTrick(AliensCostFunction costFunc, CheckFunction checkFunc,
                                             double left, double right, double eps) {
        /*
        Aliens Trick（二分约束参数+可行性DP）
        
        问题描述：
        解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
        
        解题思路：
        1. 将约束条件转化为参数λ，构造拉格朗日函数
        2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
        3. 根据可行性DP的结果调整二分区间
        
        参数：
            costFunc: 计算带参数λ的成本函数，返回[value, constraint]数组
            checkFunc: 检查当前解是否满足约束的函数
            left: 二分左边界
            right: 二分右边界
            eps: 精度要求
        
        返回：
            AliensTrickResult: 包含最优参数λ和对应最优解的结果类
        
        时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
        */
        double bestLambda = left;
        double bestValue = 0.0;
        
        while (right - left > eps) {
            double mid = (left + right) / 2;
            // 计算当前参数下的解和约束值
            double[] result = costFunc.apply(mid);
            double currentValue = result[0];
            double constraintValue = result[1];
            
            if (checkFunc.apply(constraintValue)) {
                // 满足约束，尝试更小的参数
                right = mid;
                bestLambda = mid;
                bestValue = currentValue;
            } else {
                // 不满足约束，需要增大参数
                left = mid;
            }
        }
        
        return new AliensTrickResult(bestLambda, bestValue);
    }
    
    // 默认精度版本
    public static AliensTrickResult aliensTrick(AliensCostFunction costFunc, CheckFunction checkFunc,
                                             double left, double right) {
        return aliensTrick(costFunc, checkFunc, left, right, 1e-7);
    }
    
    // ==================== 图上DP→最短路：分层图建模 ====================
    
    public static int layeredGraphDijkstra(int n, int m, List<List<Integer>> edges, int k) {
        /*
        分层图Dijkstra算法
        
        问题描述：
        给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
        
        解题思路：
        1. 构建分层图，每层代表使用不同次数的特殊操作
        2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
        3. 使用Dijkstra算法在分层图上寻找最短路径
        
        参数：
            n: 节点数量
            m: 边的数量
            edges: 边的列表，每个元素为[u, v, w]表示u到v的权为w的边
            k: 允许使用的特殊操作次数
        
        返回：
            int: 从节点0到节点n-1的最短路径长度
        
        时间复杂度：O((n*k + m*k) log(n*k))
        空间复杂度：O(n*k + m*k)
        */
        // 构建分层图的邻接表
        List<List<List<Integer>>> graph = new ArrayList<>();
        for (int i = 0; i < n * (k + 1); ++i) {
            graph.add(new ArrayList<>());
        }
        
        // 添加普通边（不使用特殊操作）
        for (List<Integer> edge : edges) {
            int u = edge.get(0);
            int v = edge.get(1);
            int w = edge.get(2);
            for (int i = 0; i <= k; ++i) {
                int from = u + i * n;
                graph.get(from).add(Arrays.asList(v + i * n, w));
            }
        }
        
        // 添加使用特殊操作的边（如果允许的话）
        for (List<Integer> edge : edges) {
            int u = edge.get(0);
            int v = edge.get(1);
            for (int i = 0; i < k; ++i) {
                // 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
                int from = u + i * n;
                graph.get(from).add(Arrays.asList(v + (i + 1) * n, 0));
            }
        }
        
        // Dijkstra算法
        int[] dist = new int[n * (k + 1)];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;  // 假设起点是节点0
        
        // 使用优先队列，按距离排序
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        heap.offer(new int[]{0, 0});
        
        while (!heap.isEmpty()) {
            int[] current = heap.poll();
            int d = current[0];
            int u = current[1];
            
            if (d > dist[u]) {
                continue;
            }
            
            for (List<Integer> edge : graph.get(u)) {
                int v = edge.get(0);
                int w = edge.get(1);
                if (dist[v] > d + w) {
                    dist[v] = d + w;
                    heap.offer(new int[]{dist[v], v});
                }
            }
        }
        
        // 取所有层中到达终点的最小值
        int result = Integer.MAX_VALUE;
        for (int i = 0; i <= k; ++i) {
            result = Math.min(result, dist[n - 1 + i * n]);
        }
        
        return result;
    }
    
    // ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================
    
    public static double[] gaussianElimination(double[][] matrix) {
        /*
        高斯消元法求解线性方程组
        
        问题描述：
        求解形如Ax = b的线性方程组
        
        解题思路：
        1. 构建增广矩阵
        2. 进行高斯消元，将矩阵转化为行阶梯形
        3. 回代求解
        
        参数：
            matrix: 增广矩阵，每行最后一个元素是b的值
        
        返回：
            double[]: 方程组的解
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = matrix.length;
        final double eps = 1e-9;
        
        // 高斯消元过程
        for (int i = 0; i < n; ++i) {
            // 找到主元行（当前列中绝对值最大的行）
            int maxRow = i;
            for (int j = i; j < n; ++j) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[maxRow][i])) {
                    maxRow = j;
                }
            }
            
            // 交换主元行和当前行
            double[] temp = matrix[i];
            matrix[i] = matrix[maxRow];
            matrix[maxRow] = temp;
            
            // 如果主元为0，方程组可能有无穷多解或无解
            if (Math.abs(matrix[i][i]) < eps) {
                continue;
            }
            
            // 消元过程
            for (int j = i + 1; j < n; ++j) {
                double factor = matrix[j][i] / matrix[i][i];
                for (int k = i; k <= n; ++k) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
        }
        
        // 回代求解
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; --i) {
            x[i] = matrix[i][n];
            for (int j = i + 1; j < n; ++j) {
                x[i] -= matrix[i][j] * x[j];
            }
            x[i] /= matrix[i][i];
        }
        
        return x;
    }
    
    public static double[] expectationDPWithCycles(int n, List<List<Pair<Integer, Double>>> transitions) {
        /*
        期望DP处理有环情况（使用高斯消元）
        
        问题描述：
        在有环的状态转移图中计算期望
        
        解题思路：
        1. 对于每个状态，建立期望方程
        2. 使用高斯消元求解方程组
        
        参数：
            n: 状态数量
            transitions: 转移概率列表，transitions[i]是一个列表，每个元素为(j, p)表示从i转移到j的概率为p
        
        返回：
            double[]: 每个状态的期望值
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        // 构建线性方程组的增广矩阵
        double[][] matrix = new double[n][n + 1];
        
        for (int i = 0; i < n; ++i) {
            matrix[i][i] = 1.0;  // 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
            
            // 假设每个状态的代价为1，具体根据问题调整
            double cost = 1.0;
            matrix[i][n] = cost;
            
            for (Pair<Integer, Double> transition : transitions.get(i)) {
                int j = transition.first;
                double p = transition.second;
                if (i != j) {  // 避免自环的特殊处理
                    matrix[i][j] -= p;
                }
            }
        }
        
        // 使用高斯消元求解
        return gaussianElimination(matrix);
    }
    
    // 辅助类Pair
    static class Pair<A, B> {
        A first;
        B second;
        
        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }
    
    // ==================== 冷门模型：插头DP（轮廓线DP） ====================
    
    public static int plugDP(int[][] grid) {
        /*
        插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
        
        问题描述：
        给定一个网格，求其中哈密顿回路的数量
        
        解题思路：
        1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
        2. 插头表示连接的状态，通常用二进制表示
        3. 使用哈希表优化空间复杂度
        
        参数：
            grid: 网格，1表示可通行，0表示障碍物
        
        返回：
            int: 哈密顿回路的数量
        
        时间复杂度：O(n*m*4^min(n,m))
        空间复杂度：O(4^min(n,m))
        */
        int n = grid.length;
        if (n == 0) {
            return 0;
        }
        int m = grid[0].length;
        
        // 使用哈希表优化
        Map<Long, Integer> dp = new HashMap<>();
        
        // 初始状态：左上角没有插头
        dp.put(0L, 1);
        
        for (int i = 0; i < n; ++i) {
            // 新的一行开始，需要将状态左移一位
            Map<Long, Integer> newDp = new HashMap<>();
            for (Map.Entry<Long, Integer> entry : dp.entrySet()) {
                long state = entry.getKey();
                int cnt = entry.getValue();
                // 左移一位，移除最左边的插头
                long newState = state << 1;
                newDp.put(newState, newDp.getOrDefault(newState, 0) + cnt);
            }
            dp = newDp;
            
            for (int j = 0; j < m; ++j) {
                Map<Long, Integer> newDp2 = new HashMap<>();
                
                for (Map.Entry<Long, Integer> entry : dp.entrySet()) {
                    long state = entry.getKey();
                    int cnt = entry.getValue();
                    // 当前位置左边和上边的插头状态
                    int left = (int) ((state >> (2 * j)) & 3);
                    int up = (int) ((state >> (2 * (j + 1))) & 3);
                    
                    // 如果当前位置是障碍物，跳过
                    if (grid[i][j] == 0) {
                        // 只有当左右插头都不存在时才合法
                        if (left == 0 && up == 0) {
                            newDp2.put(state, newDp2.getOrDefault(state, 0) + cnt);
                        }
                        continue;
                    }
                    
                    // 处理各种插头组合情况
                    // 1. 没有左插头和上插头
                    if (left == 0 && up == 0) {
                        // 只能创建新的插头对（用于回路的开始）
                        if (i < n - 1 && j < m - 1 && grid[i+1][j] == 1 && grid[i][j+1] == 1) {
                            long newState = state | (1L << (2 * j)) | (2L << (2 * (j + 1)));
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                    
                    // 2. 只有左插头
                    else if (left != 0 && up == 0) {
                        // 向下延伸
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            newDp2.put(state, newDp2.getOrDefault(state, 0) + cnt);
                        }
                        // 向右延伸
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            long newState = (state & ~(3L << (2 * j))) | ((long)left << (2 * (j + 1)));
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                    
                    // 3. 只有上插头
                    else if (left == 0 && up != 0) {
                        // 向右延伸
                        if (j < m - 1 && grid[i][j+1] == 1) {
                            newDp2.put(state, newDp2.getOrDefault(state, 0) + cnt);
                        }
                        // 向下延伸
                        if (i < n - 1 && grid[i+1][j] == 1) {
                            long newState = (state & ~(3L << (2 * (j + 1)))) | ((long)up << (2 * j));
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                    
                    // 4. 同时有左插头和上插头
                    else {
                        // 合并插头
                        long newState = (state & ~(3L << (2 * j))) & ~(3L << (2 * (j + 1)));
                        
                        // 如果是形成回路的最后一步
                        if (left == up) {
                            // 检查是否所有插头都已连接
                            if (newState == 0 && i == n - 1 && j == m - 1) {
                                newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                            }
                        } else {
                            // 合并两个不同的插头
                            newDp2.put(newState, newDp2.getOrDefault(newState, 0) + cnt);
                        }
                    }
                }
                
                dp = newDp2;
            }
        }
        
        // 最终状态应该是没有任何插头（形成回路）
        return dp.getOrDefault(0L, 0);
    }
    
    // ==================== 冷门模型：树上背包的优化 ====================
    
    private static void dfsTreeKnapsack(int u, int parent, int capacity, 
                                      List<List<Integer>> tree, int[] weights, 
                                      int[] values, int[][] dp, int[] size) {
        // 初始化当前节点
        size[u] = 1;
        if (weights[u] <= capacity) {
            dp[u][weights[u]] = values[u];
        }
        
        // 对每个子节点，按照子树大小排序，小的先合并
        List<Pair<Integer, Integer>> children = new ArrayList<>();
        for (int v : tree.get(u)) {
            if (v != parent) {
                dfsTreeKnapsack(v, u, capacity, tree, weights, values, dp, size);
                children.add(new Pair<>(size[v], v));
            }
        }
        
        // 按子树大小排序
        Collections.sort(children, (a, b) -> a.first - b.first);
        
        for (Pair<Integer, Integer> child : children) {
            int sz = child.first;
            int v = child.second;
            // 逆序遍历容量，避免重复计算
            for (int i = Math.min(size[u], capacity); i >= 0; --i) {
                if (dp[u][i] == 0 && i != 0) continue;
                for (int j = 1; j <= Math.min(sz, capacity - i); ++j) {
                    if (dp[v][j] > 0 && i + j <= capacity) {
                        dp[u][i + j] = Math.max(dp[u][i + j], dp[u][i] + dp[v][j]);
                    }
                }
            }
            
            // 更新子树大小
            size[u] += sz;
        }
    }
    
    public static int treeKnapsackOptimized(int root, int capacity, List<List<Integer>> tree, 
                                        int[] weights, int[] values) {
        /*
        树上背包的优化实现（小到大合并）
        
        问题描述：
        在树上选择一些节点，使得总重量不超过容量，且总价值最大
        
        解题思路：
        1. 使用后序遍历处理子树
        2. 使用小到大合并的策略优化复杂度
        3. 对于每个节点，维护一个容量为capacity的背包
        
        参数：
            root: 根节点
            capacity: 背包容量
            tree: 树的邻接表
            weights: 每个节点的重量
            values: 每个节点的价值
        
        返回：
            int: 最大价值
        
        时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
        空间复杂度：O(n*capacity)
        */
        int n = tree.size();
        int[][] dp = new int[n][capacity + 1];
        int[] size = new int[n];
        
        // 深度优先搜索处理子树
        dfsTreeKnapsack(root, -1, capacity, tree, weights, values, dp, size);
        
        // 返回根节点的最大价值
        int maxVal = 0;
        for (int val : dp[root]) {
            maxVal = Math.max(maxVal, val);
        }
        return maxVal;
    }
    
    // ==================== 补充题目与应用 ====================
    // 以下是一些使用上述高级DP技术的经典题目及其代码实现
    
    // 1. 编辑距离问题（LeetCode 72）
    public static int editDistance(String word1, String word2) {
        /*
        LeetCode 72. 编辑距离
        题目链接：https://leetcode-cn.com/problems/edit-distance/
        
        问题描述：
        给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
        你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
        
        解题思路：
        使用二维DP，dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数。
        
        时间复杂度：O(m*n)
        空间复杂度：O(m*n)
        */
        int m = word1.length();
        int n = word2.length();
        // dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界
        for (int i = 0; i <= m; ++i) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; ++j) {
            dp[0][j] = j;
        }
        
        // 动态规划填表
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 2. 最长递增子序列（LeetCode 300）
    public static int lengthOfLIS(int[] nums) {
        /*
        LeetCode 300. 最长递增子序列
        题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
        
        问题描述：
        给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
        
        解题思路：
        使用贪心 + 二分查找优化的DP方法。
        tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
        
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        */
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        List<Integer> tails = new ArrayList<>();
        for (int num : nums) {
            // 二分查找num应该插入的位置
            int left = 0, right = tails.size();
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails.get(mid) >= num) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            if (left == tails.size()) {
                tails.add(num);
            } else {
                tails.set(left, num);
            }
        }
        
        return tails.size();
    }
    
    // 3. 背包问题变种 - 完全背包（LeetCode 322）
    public static int coinChange(int[] coins, int amount) {
        /*
        LeetCode 322. 零钱兑换
        题目链接：https://leetcode-cn.com/problems/coin-change/
        
        问题描述：
        给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
        计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
        
        解题思路：
        使用完全背包的思想，dp[i]表示凑成金额i所需的最少硬币数。
        
        时间复杂度：O(amount * n)
        空间复杂度：O(amount)
        */
        // 初始化dp数组为无穷大
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;  // 凑成金额0需要0个硬币
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; ++i) {
                if (dp[i - coin] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }
    
    // 4. 矩阵链乘法（区间DP的经典应用）
    static class MatrixChainResult {
        int[][] dp;
        int[][] s;
        
        public MatrixChainResult(int[][] dp, int[][] s) {
            this.dp = dp;
            this.s = s;
        }
    }
    
    public static MatrixChainResult matrixChainOrder(int[] p) {
        /*
        矩阵链乘法问题
        题目来源：算法导论
        
        问题描述：
        给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
        
        解题思路：
        使用区间DP，dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数。
        可以使用Knuth优化进一步降低时间复杂度。
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = p.length - 1;  // 矩阵的个数
        // dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数
        int[][] dp = new int[n + 1][n + 1];
        // s[i][j]记录最优分割点
        int[][] s = new int[n + 1][n + 1];
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            for (int i = 1; i + length - 1 <= n; ++i) {
                int j = i + length - 1;
                dp[i][j] = Integer.MAX_VALUE;
                // 枚举分割点
                for (int k = i; k < j; ++k) {
                    // 计算当前分割点的代价
                    int cost = dp[i][k] + dp[k + 1][j] + p[i - 1] * p[k] * p[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
        
        return new MatrixChainResult(dp, s);
    }
    
    // 5. 旅行商问题（TSP）的DP实现
    public static int travelingSalesmanProblem(int[][] graph) {
        /*
        旅行商问题
        题目来源：算法竞赛经典问题
        
        问题描述：
        给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
        
        解题思路：
        使用状态压缩DP，dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度。
        
        时间复杂度：O(n^2 * 2^n)
        空间复杂度：O(n * 2^n)
        */
        int n = graph.length;
        // dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度
        int[][] dp = new int[1 << n][n];
        for (int i = 0; i < (1 << n); ++i) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        
        // 初始状态：只访问了起点，路径长度为0
        for (int i = 0; i < n; ++i) {
            dp[1 << i][i] = 0;
        }
        
        // 枚举所有可能的状态
        for (int mask = 1; mask < (1 << n); ++mask) {
            // 枚举当前所在的城市
            for (int u = 0; u < n; ++u) {
                if ((mask & (1 << u)) == 0) {
                    continue;
                }
                // 枚举下一个要访问的城市
                for (int v = 0; v < n; ++v) {
                    if ((mask & (1 << v)) != 0) {
                        continue;
                    }
                    int newMask = mask | (1 << v);
                    if (dp[mask][u] != Integer.MAX_VALUE && graph[u][v] != Integer.MAX_VALUE) {
                        dp[newMask][v] = Math.min(dp[newMask][v], dp[mask][u] + graph[u][v]);
                    }
                }
            }
        }
        
        // 找到最短的回路
        int result = Integer.MAX_VALUE;
        for (int u = 0; u < n; ++u) {
            if (dp[(1 << n) - 1][u] != Integer.MAX_VALUE && graph[u][0] != Integer.MAX_VALUE) {
                result = Math.min(result, dp[(1 << n) - 1][u] + graph[u][0]);
            }
        }
        
        return result;
    }
    
    // 6. 区间DP：最优三角剖分
    public static int minimumScoreTriangulation(int[] values) {
        /*
        LeetCode 1039. 多边形三角剖分的最低得分
        题目链接：https://leetcode-cn.com/problems/minimum-score-triangulation-of-polygon/
        
        问题描述：
        给定一个凸多边形，将其三角剖分，使得所有三角形的顶点乘积之和最小。
        
        解题思路：
        使用区间DP，dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分。
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        */
        int n = values.length;
        // dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分
        int[][] dp = new int[n][n];
        
        // 枚举区间长度
        for (int length = 3; length <= n; ++length) {
            for (int i = 0; i + length - 1 < n; ++i) {
                int j = i + length - 1;
                dp[i][j] = Integer.MAX_VALUE;
                // 枚举中间点
                for (int k = i + 1; k < j; ++k) {
                    dp[i][j] = Math.min(dp[i][j], 
                                     dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    // 7. 博弈DP：石子游戏
    public static boolean stoneGame(int[] piles) {
        /*
        LeetCode 877. 石子游戏
        题目链接：https://leetcode-cn.com/problems/stone-game/
        
        问题描述：
        给定一个表示石子堆的数组，两个玩家轮流从两端取石子，每次只能取一个，取到最后一个石子的人获胜。
        判断先手是否必胜。
        
        解题思路：
        使用区间DP，dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分。
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        */
        int n = piles.length;
        // dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分
        int[][] dp = new int[n][n];
        
        // 初始化单个石子堆
        for (int i = 0; i < n; ++i) {
            dp[i][i] = piles[i];
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            for (int i = 0; i + length - 1 < n; ++i) {
                int j = i + length - 1;
                // 先手可以选择取左边或右边
                dp[i][j] = Math.max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }
        
        // 先手净胜分大于0则必胜
        return dp[0][n - 1] > 0;
    }
    
    // 8. 数位DP：统计1出现的次数
    public static int countDigitOne(int n) {
        /*
        LeetCode 233. 数字1的个数
        题目链接：https://leetcode-cn.com/problems/number-of-digit-one/
        
        问题描述：
        给定一个整数 n，计算所有小于等于 n 的非负整数中数字1出现的个数。
        
        解题思路：
        使用数位DP，逐位处理每一位上1出现的次数。
        
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        */
        if (n <= 0) {
            return 0;
        }
        
        String s = String.valueOf(n);
        int length = s.length();
        int count = 0;
        
        // 逐位处理
        for (int i = 0; i < length; ++i) {
            long high = 0;
            if (i > 0) {
                high = Long.parseLong(s.substring(0, i));
            }
            int current = s.charAt(i) - '0';
            long low = 0;
            if (i < length - 1) {
                low = Long.parseLong(s.substring(i + 1));
            }
            long digit = (long) Math.pow(10, length - i - 1);
            
            if (current == 0) {
                // 当前位为0，高位决定
                count += high * digit;
            } else if (current == 1) {
                // 当前位为1，高位+低位+1
                count += high * digit + low + 1;
            } else {
                // 当前位大于1，高位+1
                count += (high + 1) * digit;
            }
        }
        
        return count;
    }
    
    // 9. 树形DP：打家劫舍III
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    
    private static int[] robDFS(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        int[] left = robDFS(node.left);
        int[] right = robDFS(node.right);
        
        // robCurrent表示偷当前节点，notRobCurrent表示不偷当前节点
        int robCurrent = node.val + left[1] + right[1];
        int notRobCurrent = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        return new int[]{robCurrent, notRobCurrent};
    }
    
    public static int rob(TreeNode root) {
        /*
        LeetCode 337. 打家劫舍 III
        题目链接：https://leetcode-cn.com/problems/house-robber-iii/
        
        问题描述：
        在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
        这个地区只有一个入口，我们称之为“根”。除了“根”之外，每栋房子有且只有一个“父“房子与之相连。
        一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
        如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
        计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
        
        解题思路：
        使用树形DP，对于每个节点，维护两个状态：偷或不偷。
        
        时间复杂度：O(n)
        空间复杂度：O(h)，h为树的高度
        */
        int[] result = robDFS(root);
        return Math.max(result[0], result[1]);
    }
    
    // 10. 状态压缩DP：蒙斯特曼问题
    public static int monsterGame(int[][] grid) {
        /*
        蒙斯特曼问题
        题目来源：算法竞赛问题
        
        问题描述：
        在网格中放置怪物，使得任何两个怪物都不在同一行、同一列或对角线上。
        
        解题思路：
        使用状态压缩DP，dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数。
        
        时间复杂度：O(n * 2^n)
        空间复杂度：O(2^n)
        */
        int n = grid.length;
        // dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数
        long[] dp = new long[1 << n];
        dp[0] = 1;
        
        for (int i = 0; i < n; ++i) {
            long[] newDp = new long[1 << n];
            for (int mask = 0; mask < (1 << n); ++mask) {
                if (dp[mask] == 0) {
                    continue;
                }
                // 枚举所有可能的放置位置
                for (int j = 0; j < n; ++j) {
                    // 检查是否可以在(i,j)放置怪物
                    if ((mask & (1 << j)) == 0 && grid[i][j] == 1) {
                        // 检查对角线
                        boolean valid = true;
                        for (int k = 0; k < i; ++k) {
                            if ((mask & (1 << k)) != 0 && Math.abs(k - j) == i - k) {
                                valid = false;
                                break;
                            }
                        }
                        if (valid) {
                            newDp[mask | (1 << j)] += dp[mask];
                        }
                    }
                }
            }
            dp = newDp;
        }
        
        return (int) dp[(1 << n) - 1];
    }
    
    // 11. 高维DP：三维背包
    public static int threeDimensionKnapsack(int n, int[] capacity, int[][] items) {
        /*
        三维背包问题
        题目来源：算法竞赛问题
        
        问题描述：
        有n个物品，每个物品有体积、重量、价值三个属性，背包有体积和重量两个限制，求最大价值。
        
        解题思路：
        使用三维DP，dp[i][j][k]表示前i个物品，体积为j，重量为k时的最大价值。
        
        时间复杂度：O(n * V * W)
        空间复杂度：O(n * V * W)
        */
        int V = capacity[0];
        int W = capacity[1];
        // 初始化dp数组
        int[][][] dp = new int[n + 1][V + 1][W + 1];
        
        for (int i = 1; i <= n; ++i) {
            int v = items[i-1][0];
            int w = items[i-1][1];
            int val = items[i-1][2];
            for (int j = 0; j <= V; ++j) {
                for (int k = 0; k <= W; ++k) {
                    // 不选当前物品
                    dp[i][j][k] = dp[i-1][j][k];
                    // 选当前物品（如果有足够的空间）
                    if (j >= v && k >= w) {
                        dp[i][j][k] = Math.max(dp[i][j][k], dp[i-1][j-v][k-w] + val);
                    }
                }
            }
        }
        
        return dp[n][V][W];
    }
    
    // 12. 斜率优化DP示例
    static class ConvexHullTrick {
        /*
        凸包优化技巧示例
        题目来源：算法竞赛问题
        
        问题描述：
        当状态转移方程形如dp[i] = min{dp[j] + a[i] * b[j] + c}时，可以使用凸包优化。
        
        解题思路：
        将转移方程转换为直线的形式，维护凸包以快速查询最小值。
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        */
        
        static class Line {
            long k, b;
            Line(long k, long b) { this.k = k; this.b = b; }
        }
        
        Deque<Line> dq = new LinkedList<>();
        
        // 添加一条直线y = kx + b
        void addLine(long k, long b) {
            // 当队列中至少有两条直线时，检查是否需要删除末尾的直线
            while (dq.size() >= 2) {
                Line l1 = getNthLast(dq, 2);
                Line l2 = dq.getLast();
                // 判断直线l1和l2的交点是否在l2和新直线的交点右侧
                if ((l2.b - l1.b) * (k - l2.k) >= (b - l2.b) * (l2.k - l1.k)) {
                    dq.removeLast();
                } else {
                    break;
                }
            }
            dq.addLast(new Line(k, b));
        }
        
        // 获取队列中倒数第n个元素
        private Line getNthLast(Deque<Line> deque, int n) {
            if (n <= 0 || n > deque.size()) {
                throw new IndexOutOfBoundsException();
            }
            Iterator<Line> it = deque.descendingIterator();
            Line result = null;
            for (int i = 0; i < n; ++i) {
                result = it.next();
            }
            return result;
        }
        
        // 查询x处的最小值
        long query(long x) {
            // 如果队列中至少有两条直线，且第一条直线在x处的值大于第二条，删除第一条
            while (dq.size() >= 2) {
                Line l1 = dq.getFirst();
                Line l2 = dq.getFirst(); // 错误，应该是第二个元素
                // 修正：正确获取第二个元素
                Line l2Correct = getNthLast(dq, dq.size() - 1);
                if (l1.k * x + l1.b >= l2Correct.k * x + l2Correct.b) {
                    dq.removeFirst();
                } else {
                    break;
                }
            }
            if (dq.isEmpty()) {
                return Long.MAX_VALUE;
            }
            Line l = dq.getFirst();
            return l.k * x + l.b;
        }
        
        // 正确的query方法实现
        long queryCorrect(long x) {
            while (dq.size() >= 2) {
                Line l1 = dq.pollFirst();
                Line l2 = dq.peekFirst();
                if (l1.k * x + l1.b >= l2.k * x + l2.b) {
                    // 继续弹出
                } else {
                    dq.offerFirst(l1); // 恢复l1
                    break;
                }
            }
            if (dq.isEmpty()) {
                return Long.MAX_VALUE;
            }
            Line l = dq.peekFirst();
            return l.k * x + l.b;
        }
    }
    
    // ==================== 高级优化体系：Knuth优化 ====================
    /**
     * Knuth优化的DP算法
     * 
     * 问题描述：
     * 解决区间DP问题，其中状态转移方程满足四边形不等式
     * 
     * 解题思路：
     * 1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
     * 2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
     * 3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
     * 
     * 应用题目：
     * - POJ 1160 Post Office
     * - HDU 4008 Parent and son
     * - Codeforces 245H Queries for Number of Palindromes
     * 
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n^2)
     */
    public static class KnuthOptimization {
        /**
         * Knuth优化的DP实现
         * 
         * @param n 区间长度
         * @param costFunc 计算区间(i,j)代价的函数
         * @return 最小代价矩阵
         */
        public static long[][] solve(int n, CostFunction costFunc) {
            // 初始化dp和opt数组
            long[][] dp = new long[n + 1][n + 1];
            int[][] opt = new int[n + 1][n + 1];
            
            // 初始化长度为1的区间
            for (int i = 1; i <= n; i++) {
                dp[i][i] = 0;
                opt[i][i] = i;
            }
            
            // 枚举区间长度
            for (int length = 2; length <= n; length++) {
                // 枚举起始点
                for (int i = 1; i <= n - length + 1; i++) {
                    int j = i + length - 1;
                    // 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
                    int lower = opt[i][j-1];
                    int upper = (i + 1 <= j) ? opt[i+1][j] : j - 1;
                    
                    dp[i][j] = Long.MAX_VALUE;
                    for (int k = lower; k <= upper; k++) {
                        if (dp[i][k] != Long.MAX_VALUE && dp[k+1][j] != Long.MAX_VALUE) {
                            long cost = costFunc.calculate(i, j);
                            if (cost != Long.MAX_VALUE) {
                                long current = dp[i][k] + dp[k+1][j] + cost;
                                if (current < dp[i][j]) {
                                    dp[i][j] = current;
                                    opt[i][j] = k;
                                }
                            }
                        }
                    }
                }
            }
            
            return dp;
        }
        
        /**
         * 代价函数接口
         */
        public interface CostFunction {
            long calculate(int i, int j);
        }
        
        /**
         * 应用示例：最优二叉搜索树问题（POJ 3280）
         * 时间复杂度：O(n^2)
         * 空间复杂度：O(n^2)
         */
        public static long optimalBinarySearchTree(char[] keys, int[] freq) {
            int n = keys.length;
            CostFunction costFunc = (i, j) -> {
                long sum = 0;
                for (int k = i-1; k < j; k++) {
                    sum += freq[k];
                }
                return sum;
            };
            
            long[][] dp = solve(n, costFunc);
            return dp[1][n];
        }
    }
    
    // ==================== 高级优化体系：Divide & Conquer Optimization ====================
    /**
     * 分治优化（Divide & Conquer Optimization）
     * 
     * 问题描述：
     * 解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
     * 当转移满足决策单调性时使用
     * 
     * 解题思路：
     * 1. 利用决策单调性，使用分治法优化DP
     * 2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
     * 3. 使用分治的方式计算每个区间的最优决策
     * 
     * 应用题目：
     * - Codeforces 321E Ciel and Gondolas
     * - HDU 3480 Division
     * - SPOJ LARMY
     * 
     * 时间复杂度：O(n*m log m)
     * 空间复杂度：O(n*m)
     */
    public static class DivideConquerOptimization {
        private long[][] dp;
        private CostFunction costFunc;
        private int n, m;
        
        /**
         * 代价函数接口
         */
        public interface CostFunction {
            long calculate(int k, int j);
        }
        
        /**
         * 分治求解dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
         */
        private void solve(int i, int l, int r, int opt_l, int opt_r) {
            if (l > r) return;
            
            int mid = (l + r) / 2;
            int best_k = opt_l;
            dp[i][mid] = Long.MAX_VALUE;
            
            // 在opt_l到min(mid, opt_r)之间寻找最优k
            for (int k = opt_l; k <= Math.min(mid, opt_r); k++) {
                if (dp[i-1][k] != Long.MAX_VALUE) {
                    long cost = costFunc.calculate(k, mid);
                    if (cost != Long.MAX_VALUE) {
                        long current = dp[i-1][k] + cost;
                        if (current < dp[i][mid]) {
                            dp[i][mid] = current;
                            best_k = k;
                        }
                    }
                }
            }
            
            // 递归处理左右子区间
            solve(i, l, mid - 1, opt_l, best_k);
            solve(i, mid + 1, r, best_k, opt_r);
        }
        
        /**
         * 主入口：分治优化DP
         * 
         * @param n 维度1
         * @param m 维度2
         * @param costFunc 计算cost(k,j)的函数
         * @return dp数组
         */
        public long[][] solve(int n, int m, CostFunction costFunc) {
            // 初始化dp数组
            dp = new long[n + 1][m + 1];
            this.costFunc = costFunc;
            this.n = n;
            this.m = m;
            
            // 初始化dp数组为无穷大
            for (int i = 0; i <= n; i++) {
                Arrays.fill(dp[i], Long.MAX_VALUE);
            }
            dp[0][0] = 0;  // 初始状态
            
            // 对每个i应用分治优化
            for (int i = 1; i <= n; i++) {
                solve(i, 1, m, 0, m);
            }
            
            return dp;
        }
        
        /**
         * 应用示例：将数组分成k个子数组的最小代价和（LeetCode 410）
         * 时间复杂度：O(n*k log n)
         * 空间复杂度：O(n*k)
         */
        public static long splitArray(int[] nums, int k) {
            int n = nums.length;
            // 预处理前缀和
            long[] prefixSum = new long[n + 1];
            for (int i = 0; i < n; i++) {
                prefixSum[i+1] = prefixSum[i] + nums[i];
            }
            
            // 代价函数：计算从k+1到j的和的平方
            CostFunction costFunc = (k, j) -> {
                long sum = prefixSum[j] - prefixSum[k];
                return sum * sum;  // 这里可以根据实际问题定义不同的代价
            };
            
            DivideConquerOptimization optimizer = new DivideConquerOptimization();
            long[][] dp = optimizer.solve(k, n, costFunc);
            return dp[k][n];
        }
    }
    
    // ==================== 高级优化体系：SMAWK算法（行最小查询） ====================
    /**
     * SMAWK算法用于在Monge矩阵中快速查找每行的最小值
     * 
     * 问题描述：
     * 给定一个Monge矩阵，快速找到每行的最小值位置
     * 
     * 解题思路：
     * 1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
     * 2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
     * 3. 主要步骤包括行压缩和递归求解
     * 
     * 应用题目：
     * - POJ 3156 Interconnect
     * - Codeforces 472D Design Tutorial: Inverse the Problem
     * - SPOJ MCQUERY
     * 
     * 时间复杂度：O(m+n)，其中m是行数，n是列数
     * 空间复杂度：O(m+n)
     */
    public static class SMAWK {
        /**
         * 行压缩：只保留可能成为最小值的行
         */
        private static List<Integer> reduceRows(List<Integer> rows, long[][] matrix) {
            LinkedList<Integer> stack = new LinkedList<>();
            for (int i : rows) {
                while (stack.size() >= 2) {
                    int j1 = stack.removeLast();
                    int j2 = stack.removeLast();
                    stack.addLast(j1);  // 恢复栈状态
                    
                    // 比较两个行在列stack.size()-2处的值
                    int col = stack.size() - 2;
                    if (col < matrix[0].length) {
                        if (matrix[j2][col] <= matrix[i][col]) {
                            break;
                        } else {
                            stack.removeLast();  // 移除j1
                        }
                    } else {
                        break;
                    }
                }
                stack.addLast(i);
            }
            return new ArrayList<>(stack);
        }
        
        /**
         * 递归实现SMAWK算法
         */
        private static int[] smawkRec(List<Integer> rows, List<Integer> cols, long[][] matrix) {
            int m = rows.size();
            int[] result = new int[m];
            
            if (m == 0) return result;
            
            // 行压缩
            List<Integer> reducedRows = reduceRows(rows, matrix);
            
            // 递归求解列数为奇数的子问题
            List<Integer> halfCols = new ArrayList<>();
            for (int i = 1; i < cols.size(); i += 2) {
                halfCols.add(cols.get(i));
            }
            
            int[] minCols = new int[reducedRows.size()];
            Arrays.fill(minCols, -1);
            
            if (!halfCols.isEmpty()) {
                // 递归求解
                int[] subResult = smawkRec(reducedRows, halfCols, matrix);
                // 复制结果
                for (int i = 0; i < subResult.length; i++) {
                    minCols[i] = subResult[i];
                }
            }
            
            // 扩展结果到所有列
            int k = 0;  // minCols的索引
            
            for (int i = 0; i < m; i++) {
                int row = rows.get(i);
                // 确定当前行的最小值可能在哪个区间
                int start = 0;
                if (i > 0 && k > 0 && minCols[k-1] != -1) {
                    start = minCols[k-1];
                }
                int end = (k < minCols.length && minCols[k] != -1) ? minCols[k] : cols.get(cols.size() - 1);
                
                // 在这个区间内查找最小值
                long minVal = Long.MAX_VALUE;
                int minCol = start;
                
                // 找到start和end在cols中的索引
                int startIdx = cols.indexOf(start);
                int endIdx = cols.indexOf(end);
                
                for (int idx = startIdx; idx <= endIdx; idx++) {
                    int col = cols.get(idx);
                    if (col < matrix[0].length && matrix[row][col] < minVal) {
                        minVal = matrix[row][col];
                        minCol = col;
                    }
                }
                
                result[i] = minCol;
                
                // 如果当前行在reducedRows中，且不是最后一行，k前进
                if (k < reducedRows.size() && row == reducedRows.get(k)) {
                    k++;
                }
            }
            
            return result;
        }
        
        /**
         * SMAWK算法主入口
         * 
         * @param matrix 一个Monge矩阵
         * @return 每行最小值的列索引数组
         */
        public static int[] solve(long[][] matrix) {
            if (matrix == null || matrix.length == 0) return new int[0];
            int m = matrix.length;
            int n = matrix[0].length;
            
            // 构造行索引和列索引数组
            List<Integer> rows = new ArrayList<>(m);
            List<Integer> cols = new ArrayList<>(n);
            for (int i = 0; i < m; i++) rows.add(i);
            for (int j = 0; j < n; j++) cols.add(j);
            
            // 调用递归实现
            return smawkRec(rows, cols, matrix);
        }
        
        /**
         * 应用示例：寻找每一行的最小元素
         * 时间复杂度：O(m+n)
         * 空间复杂度：O(m+n)
         */
        public static long[] findRowMins(long[][] matrix) {
            int[] minCols = solve(matrix);
            long[] result = new long[matrix.length];
            for (int i = 0; i < matrix.length; i++) {
                result[i] = matrix[i][minCols[i]];
            }
            return result;
        }
    }
    
    // ==================== 高级优化体系：Aliens Trick（二分约束参数+可行性DP） ====================
    /**
     * Aliens Trick（二分约束参数+可行性DP）
     * 
     * 问题描述：
     * 解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
     * 
     * 解题思路：
     * 1. 将约束条件转化为参数λ，构造拉格朗日函数
     * 2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
     * 3. 根据可行性DP的结果调整二分区间
     * 
     * 应用题目：
     * - Codeforces 739E Gosha is Hunting
     * - POJ 3686 The Windy's
     * - SPOJ QTREE5
     * 
     * 时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
     * 空间复杂度：O(DP空间复杂度)
     */
    public static class AliensTrick {
        /**
         * 成本函数接口：计算带参数λ的成本和约束值
         */
        public interface CostFunction {
            Result calculate(double lambda);
            
            class Result {
                public double value;
                public double constraint;
                public Result(double value, double constraint) {
                    this.value = value;
                    this.constraint = constraint;
                }
            }
        }
        
        /**
         * 约束检查函数接口：检查当前解是否满足约束
         */
        public interface CheckFunction {
            boolean check(double constraint);
        }
        
        /**
         * Aliens Trick主入口
         * 
         * @param costFunc 成本函数
         * @param checkFunc 约束检查函数
         * @param left 二分左边界
         * @param right 二分右边界
         * @param eps 精度要求
         * @return 最优参数lambda和对应最优解
         */
        public static Result solve(CostFunction costFunc, CheckFunction checkFunc, 
                                  double left, double right, double eps) {
            double bestLambda = left;
            double bestValue = 0.0;
            
            // 二分查找参数lambda
            while (right - left > eps) {
                double mid = (left + right) / 2;
                // 计算当前参数下的解和约束值
                CostFunction.Result result = costFunc.calculate(mid);
                
                if (checkFunc.check(result.constraint)) {
                    // 满足约束，尝试更小的参数
                    right = mid;
                    bestLambda = mid;
                    bestValue = result.value;
                } else {
                    // 不满足约束，需要增大参数
                    left = mid;
                }
            }
            
            return new Result(bestLambda, bestValue);
        }
        
        /**
         * 结果类
         */
        public static class Result {
            public double lambda;
            public double value;
            public Result(double lambda, double value) {
                this.lambda = lambda;
                this.value = value;
            }
        }
        
        /**
         * 应用示例：将数组分成恰好k个部分，使得最大子数组和最小（LeetCode 410的变种）
         * 时间复杂度：O(log(S) * n)，其中S是数组元素和
         * 空间复杂度：O(n)
         */
        public static double splitArrayK(int[] nums, int k) {
            // 计算数组元素和作为二分上限
            double sum = 0;
            for (int num : nums) sum += num;
            
            // 成本函数：使用DP计算在给定lambda下的最小成本
            CostFunction costFunc = lambda -> {
                int n = nums.length;
                double[] dp = new double[n + 1];
                int[] cnt = new int[n + 1];
                
                Arrays.fill(dp, Double.MAX_VALUE);
                dp[0] = 0;
                cnt[0] = 0;
                
                for (int i = 1; i <= n; i++) {
                    double sumSeg = 0;
                    for (int j = i-1; j >= 0; j--) {
                        sumSeg += nums[j];
                        if (dp[j] != Double.MAX_VALUE) {
                            double current = dp[j] + sumSeg * sumSeg + lambda;  // lambda作为惩罚项
                            if (current < dp[i]) {
                                dp[i] = current;
                                cnt[i] = cnt[j] + 1;
                            }
                        }
                    }
                }
                
                return new CostFunction.Result(dp[n], cnt[n]);
            };
            
            // 约束检查函数：确保分割次数不超过k
            CheckFunction checkFunc = constraint -> constraint <= k;
            
            // 执行Aliens Trick
            Result result = solve(costFunc, checkFunc, 0, sum * sum, 1e-7);
            return result.value;
        }
    }
    
    // ==================== 图上DP→最短路：分层图建模 ====================
    /**
     * 分层图Dijkstra算法
     * 
     * 问题描述：
     * 给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
     * 
     * 解题思路：
     * 1. 构建分层图，每层代表使用不同次数的特殊操作
     * 2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
     * 3. 使用Dijkstra算法在分层图上寻找最短路径
     * 
     * 应用题目：
     * - LeetCode 787. K 站中转内最便宜的航班
     * - POJ 3159 Candies
     * - HDU 2957 Safety Assessment
     * 
     * 时间复杂度：O((n*k + m*k) log(n*k))
     * 空间复杂度：O(n*k + m*k)
     */
    public static class LayeredGraphShortestPath {
        /**
         * 边类
         */
        public static class Edge {
            int to;
            int weight;
            public Edge(int to, int weight) {
                this.to = to;
                this.weight = weight;
            }
        }
        
        /**
         * 分层图最短路径算法
         * 
         * @param n 节点数量
         * @param edges 边的列表
         * @param k 允许使用的特殊操作次数
         * @param start 起始节点
         * @param end 目标节点
         * @return 最短路径长度，-1表示不可达
         */
        public static int solve(int n, List<List<Edge>> edges, int k, int start, int end) {
            // 构建分层图的邻接表
            List<List<Edge>> layeredGraph = new ArrayList<>();
            int totalNodes = n * (k + 1);
            for (int i = 0; i < totalNodes; i++) {
                layeredGraph.add(new ArrayList<>());
            }
            
            // 添加普通边（不使用特殊操作）
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= k; j++) {
                    int fromNode = i + j * n;
                    for (Edge edge : edges.get(i)) {
                        layeredGraph.get(fromNode).add(new Edge(edge.to + j * n, edge.weight));
                    }
                }
            }
            
            // 添加使用特殊操作的边（如果允许的话）
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < k; j++) {
                    int fromNode = i + j * n;
                    for (Edge edge : edges.get(i)) {
                        // 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
                        layeredGraph.get(fromNode).add(new Edge(edge.to + (j + 1) * n, 0));
                    }
                }
            }
            
            // Dijkstra算法
            int[] dist = new int[totalNodes];
            Arrays.fill(dist, Integer.MAX_VALUE);
            dist[start] = 0;  // 起始点在第0层
            
            // 使用优先队列，按距离排序
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
            pq.offer(new int[]{start, 0});
            
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int u = current[0];
                int d = current[1];
                
                if (d > dist[u]) {
                    continue;
                }
                
                for (Edge edge : layeredGraph.get(u)) {
                    int v = edge.to;
                    int w = edge.weight;
                    if (dist[v] > d + w) {
                        dist[v] = d + w;
                        pq.offer(new int[]{v, dist[v]});
                    }
                }
            }
            
            // 取所有层中到达终点的最小值
            int result = Integer.MAX_VALUE;
            for (int i = 0; i <= k; i++) {
                result = Math.min(result, dist[end + i * n]);
            }
            
            return result == Integer.MAX_VALUE ? -1 : result;
        }
        
        /**
         * 应用示例：LeetCode 787. K 站中转内最便宜的航班
         * 时间复杂度：O((n*k + m*k) log(n*k))
         * 空间复杂度：O(n*k + m*k)
         */
        public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
            // 构建图的邻接表
            List<List<Edge>> edges = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                edges.add(new ArrayList<>());
            }
            for (int[] flight : flights) {
                edges.get(flight[0]).add(new Edge(flight[1], flight[2]));
            }
            
            // 调用分层图算法，注意这里k站中转意味着可以乘坐k+1次航班
            return solve(n, edges, k + 1, src, dst);
        }
    }
    
    // ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================
    /**
     * 期望DP处理有环情况（使用高斯消元）
     * 
     * 问题描述：
     * 在有环的状态转移图中计算期望
     * 
     * 解题思路：
     * 1. 对于每个状态，建立期望方程
     * 2. 使用高斯消元求解方程组
     * 
     * 应用题目：
     * - LeetCode 837. 新21点
     * - POJ 3744 Scout YYF I
     * - HDU 4405 Aeroplane chess
     * 
     * 时间复杂度：O(n^3)
     * 空间复杂度：O(n^2)
     */
    public static class ExpectationDPWithGaussian {
        /**
         * 高斯消元法求解线性方程组
         * 
         * @param matrix 增广矩阵，每行最后一个元素是b的值
         * @return 方程组的解数组
         */
        public static double[] gaussianElimination(double[][] matrix) {
            int n = matrix.length;
            double eps = 1e-9;
            
            // 高斯消元过程
            for (int i = 0; i < n; i++) {
                // 找到主元行（当前列中绝对值最大的行）
                int maxRow = i;
                for (int j = i; j < n; j++) {
                    if (Math.abs(matrix[j][i]) > Math.abs(matrix[maxRow][i])) {
                        maxRow = j;
                    }
                }
                
                // 交换主元行和当前行
                if (maxRow != i) {
                    double[] temp = matrix[i];
                    matrix[i] = matrix[maxRow];
                    matrix[maxRow] = temp;
                }
                
                // 如果主元为0，方程组可能有无穷多解或无解
                if (Math.abs(matrix[i][i]) < eps) {
                    // 这里简化处理，假设方程组总是有解
                    continue;
                }
                
                // 消元过程
                for (int j = 0; j < n; j++) {
                    if (j != i && Math.abs(matrix[j][i]) > eps) {
                        double factor = matrix[j][i] / matrix[i][i];
                        for (int k = i; k <= n; k++) {
                            matrix[j][k] -= factor * matrix[i][k];
                        }
                    }
                }
            }
            
            // 回代求解
            double[] x = new double[n];
            for (int i = 0; i < n; i++) {
                x[i] = matrix[i][n] / matrix[i][i];
            }
            
            return x;
        }
        
        /**
         * 期望DP主入口
         * 
         * @param n 状态数量
         * @param transitions 转移概率列表，transitions[i]是一个列表，每个元素为(j, p)表示从i转移到j的概率为p
         * @param cost 每个状态的代价
         * @return 每个状态的期望值数组
         */
        public static double[] solve(int n, List<List<Transition>> transitions, double[] cost) {
            // 构建线性方程组的增广矩阵
            double[][] matrix = new double[n][n + 1];
            
            for (int i = 0; i < n; i++) {
                matrix[i][i] = 1.0;  // 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
                matrix[i][n] = cost[i];
                
                for (Transition t : transitions.get(i)) {
                    if (i != t.to) {  // 避免自环的特殊处理
                        matrix[i][t.to] -= t.probability;
                    }
                }
            }
            
            // 使用高斯消元求解
            return gaussianElimination(matrix);
        }
        
        /**
         * 转移类
         */
        public static class Transition {
            int to;
            double probability;
            public Transition(int to, double probability) {
                this.to = to;
                this.probability = probability;
            }
        }
        
        /**
         * 应用示例：LeetCode 837. 新21点（简化版本）
         * 时间复杂度：O(n^3)
         * 空间复杂度：O(n^2)
         */
        public static double new21Game(int N, int K, int W) {
            if (K == 0 || N >= K + W) return 1.0;
            
            int n = K + W;
            List<List<Transition>> transitions = new ArrayList<>();
            double[] cost = new double[n + 1];
            
            for (int i = 0; i <= n; i++) {
                transitions.add(new ArrayList<>());
            }
            
            // 构建转移概率
            for (int i = 0; i < K; i++) {
                for (int w = 1; w <= W; w++) {
                    int next = Math.min(i + w, n);
                    transitions.get(i).add(new Transition(next, 1.0 / W));
                }
            }
            
            // 终止状态的期望为是否<=N
            for (int i = K; i <= n; i++) {
                cost[i] = (i <= N) ? 1.0 : 0.0;
                transitions.get(i).add(new Transition(i, 1.0));  // 自环
            }
            
            double[] result = solve(n + 1, transitions, cost);
            return result[0];
        }
    }
    
    // ==================== 冷门模型：插头DP（轮廓线DP）====================
    /**
     * 插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
     * 
     * 问题描述：
     * 给定一个网格，求其中哈密顿回路的数量
     * 
     * 解题思路：
     * 1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
     * 2. 插头表示连接的状态，通常用二进制表示
     * 3. 使用字典优化空间复杂度
     * 4. 实现合法性判定与对称剪枝
     * 
     * 应用题目：
     * - HDU 1693 Eat the Trees
     * - SPOJ MATCH2 Match the Brackets II
     * - Codeforces 1435F Cyclic Shifts Sorting
     * 
     * 时间复杂度：O(n*m*4^min(n,m))
     * 空间复杂度：O(4^min(n,m))
     */
    public static class PlugDP {
        /**
         * 插头DP求解哈密顿回路数量
         * 
         * @param grid 网格，1表示可通行，0表示障碍物
         * @return 哈密顿回路的数量
         */
        public static long solve(int[][] grid) {
            if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
            int n = grid.length;
            int m = grid[0].length;
            
            // 使用HashMap优化空间
            Map<Long, Long> dp = new HashMap<>();
            dp.put(0L, 1L);
            
            for (int i = 0; i < n; i++) {
                // 新的一行开始，需要将状态左移两位
                Map<Long, Long> newDp = new HashMap<>();
                for (Map.Entry<Long, Long> entry : dp.entrySet()) {
                    // 左移两位，移除最左边的插头
                    long state = entry.getKey() << 2;
                    // 移除可能的高位，只保留m*2位
                    state &= (1L << (2 * m)) - 1;
                    newDp.put(state, newDp.getOrDefault(state, 0L) + entry.getValue());
                }
                dp = newDp;
                
                for (int j = 0; j < m; j++) {
                    Map<Long, Long> newDp2 = new HashMap<>();
                    
                    for (Map.Entry<Long, Long> entry : dp.entrySet()) {
                        long state = entry.getKey();
                        long cnt = entry.getValue();
                        
                        // 当前位置左边和上边的插头状态
                        int left = (int) ((state >> (2 * j)) & 3);
                        int up = (int) ((state >> (2 * (j + 1))) & 3);
                        
                        // 如果当前位置是障碍物，跳过
                        if (grid[i][j] == 0) {
                            // 只有当左右插头都不存在时才合法
                            if (left == 0 && up == 0) {
                                newDp2.put(state, newDp2.getOrDefault(state, 0L) + cnt);
                            }
                            continue;
                        }
                        
                        // 处理各种插头组合情况
                        // 1. 没有左插头和上插头
                        if (left == 0 && up == 0) {
                            // 只能创建新的插头对（用于回路的开始）
                            if (i < n - 1 && j < m - 1 && grid[i+1][j] == 1 && grid[i][j+1] == 1) {
                                long newState = state | (1L << (2 * j)) | (2L << (2 * (j + 1)));
                                newDp2.put(newState, newDp2.getOrDefault(newState, 0L) + cnt);
                            }
                        }
                        // 2. 只有左插头
                        else if (left != 0 && up == 0) {
                            // 向下延伸
                            if (i < n - 1 && grid[i+1][j] == 1) {
                                newDp2.put(state, newDp2.getOrDefault(state, 0L) + cnt);
                            }
                            // 向右延伸
                            if (j < m - 1 && grid[i][j+1] == 1) {
                                long newState = (state & ~(3L << (2 * j))) | ((long) left << (2 * (j + 1)));
                                newDp2.put(newState, newDp2.getOrDefault(newState, 0L) + cnt);
                            }
                        }
                        // 3. 只有上插头
                        else if (left == 0 && up != 0) {
                            // 向右延伸
                            if (j < m - 1 && grid[i][j+1] == 1) {
                                newDp2.put(state, newDp2.getOrDefault(state, 0L) + cnt);
                            }
                            // 向下延伸
                            if (i < n - 1 && grid[i+1][j] == 1) {
                                long newState = (state & ~(3L << (2 * (j + 1)))) | ((long) up << (2 * j));
                                newDp2.put(newState, newDp2.getOrDefault(newState, 0L) + cnt);
                            }
                        }
                        // 4. 同时有左插头和上插头
                        else {
                            // 合并插头
                            long newState = (state & ~(3L << (2 * j))) & ~(3L << (2 * (j + 1)));
                            
                            // 如果是形成回路的最后一步
                            if (left == up) {
                                // 检查是否所有插头都已连接
                                if (newState == 0 && i == n - 1 && j == m - 1) {
                                    newDp2.put(newState, newDp2.getOrDefault(newState, 0L) + cnt);
                                }
                            } else {
                                // 合并两个不同的插头
                                // 这里可以加入更多的合法性检查和剪枝
                                newDp2.put(newState, newDp2.getOrDefault(newState, 0L) + cnt);
                            }
                        }
                    }
                    
                    dp = newDp2;
                }
            }
            
            // 最终状态应该是没有任何插头（形成回路）
            return dp.getOrDefault(0L, 0L);
        }
        
        /**
         * 应用示例：网格中的回路计数
         * 时间复杂度：O(n*m*4^min(n,m))
         * 空间复杂度：O(4^min(n,m))
         */
        public static long countGridCycles(int[][] grid) {
            return solve(grid);
        }
    }
    
    // ==================== 冷门模型：树上背包的优化 ====================
    /**
     * 树上背包的优化实现（小到大合并）
     * 
     * 问题描述：
     * 在树上选择一些节点，使得总重量不超过容量，且总价值最大
     * 
     * 解题思路：
     * 1. 使用后序遍历处理子树
     * 2. 使用小到大合并的策略优化复杂度
     * 3. 对于每个节点，维护一个容量为capacity的背包
     * 
     * 应用题目：
     * - HDU 1561 The more, The Better
     * - POJ 2063 Investment
     * - Codeforces 1152F2 Neko Rules the Catniverse
     * 
     * 时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
     * 空间复杂度：O(n*capacity)
     */
    public static class TreeKnapsackOptimized {
        private long[][] dp;
        private int[] size;
        private int[][] tree;
        private int[] weights;
        private int[] values;
        private int capacity;
        private int n;
        
        /**
         * 树上背包的DFS处理函数
         */
        private void dfs(int u, int parent) {
            // 初始化当前节点
            size[u] = 1;
            if (weights[u] <= capacity) {
                dp[u][weights[u]] = values[u];
            }
            
            // 对每个子节点，按照子树大小排序，小的先合并
            List<int[]> children = new ArrayList<>();
            for (int v : tree[u]) {
                if (v != parent) {
                    dfs(v, u);
                    children.add(new int[]{size[v], v});
                }
            }
            
            // 按子树大小排序（小到大）
            children.sort((a, b) -> a[0] - b[0]);
            
            for (int[] child : children) {
                int sz = child[0];
                int v = child[1];
                
                // 逆序遍历容量，避免重复计算
                for (int i = Math.min(size[u], capacity); i >= 0; i--) {
                    if (dp[u][i] == 0 && i != 0) continue;
                    for (int j = 1; j <= Math.min(sz, capacity - i); j++) {
                        if (dp[v][j] > 0 && i + j <= capacity) {
                            dp[u][i + j] = Math.max(dp[u][i + j], dp[u][i] + dp[v][j]);
                        }
                    }
                }
                
                // 更新子树大小
                size[u] += sz;
            }
        }
        
        /**
         * 树上背包主入口
         * 
         * @param n 节点数量
         * @param root 根节点
         * @param capacity 背包容量
         * @param tree 树的邻接表
         * @param weights 每个节点的重量
         * @param values 每个节点的价值
         * @return 最大价值
         */
        public long solve(int n, int root, int capacity, int[][] tree, int[] weights, int[] values) {
            this.n = n;
            this.capacity = capacity;
            this.tree = tree;
            this.weights = weights;
            this.values = values;
            
            // 初始化dp数组
            dp = new long[n + 1][capacity + 1];
            size = new int[n + 1];
            
            // 深度优先搜索处理子树
            dfs(root, -1);
            
            // 返回根节点的最大价值
            long maxValue = 0;
            for (int i = 0; i <= capacity; i++) {
                maxValue = Math.max(maxValue, dp[root][i]);
            }
            return maxValue;
        }
        
        /**
         * 应用示例：树上最大价值选择
         * 时间复杂度：O(n*capacity^2)
         * 空间复杂度：O(n*capacity)
         */
        public static long maxTreeValue(int n, int root, int capacity, int[][] tree, int[] weights, int[] values) {
            TreeKnapsackOptimized optimizer = new TreeKnapsackOptimized();
            return optimizer.solve(n, root, capacity, tree, weights, values);
        }
    }
    
    // ==================== 补充题目与应用 ====================
    /**
     * LeetCode 72. 编辑距离
     * 题目链接：https://leetcode-cn.com/problems/edit-distance/
     * 
     * 问题描述：
     * 给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
     * 你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     */
    public static int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i-1) == word2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1]) + 1;
                }
            }
        }
        return dp[m][n];
    }
    
    /**
     * LeetCode 300. 最长递增子序列
     * 题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
     * 
     * 问题描述：
     * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int[] tails = new int[nums.length];
        int len = 0;
        
        for (int num : nums) {
            int left = 0, right = len;
            while (left < right) {
                int mid = (left + right) / 2;
                if (tails[mid] < num) left = mid + 1;
                else right = mid;
            }
            
            tails[left] = num;
            if (left == len) len++;
        }
        
        return len;
    }
    
    /**
     * LeetCode 322. 零钱兑换
     * 题目链接：https://leetcode-cn.com/problems/coin-change/
     * 
     * 问题描述：
     * 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
     * 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
     * 
     * 时间复杂度：O(amount * n)
     * 空间复杂度：O(amount)
     */
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    /**
     * 矩阵链乘法问题
     * 题目来源：算法导论、POJ 1038
     * 
     * 问题描述：
     * 给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
     * 
     * 时间复杂度：O(n^3)
     * 空间复杂度：O(n^2)
     */
    public static long matrixChainOrder(int[] p) {
        int n = p.length - 1;  // 矩阵的个数
        long[][] dp = new long[n + 1][n + 1];
        
        for (int length = 2; length <= n; length++) {
            for (int i = 1; i <= n - length + 1; i++) {
                int j = i + length - 1;
                dp[i][j] = Long.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    long cost = dp[i][k] + dp[k + 1][j] + (long)p[i - 1] * p[k] * p[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                    }
                }
            }
        }
        
        return dp[1][n];
    }
    
    /**
     * 旅行商问题（TSP）
     * 题目来源：算法竞赛、POJ 2480
     * 
     * 问题描述：
     * 给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
     * 
     * 时间复杂度：O(n^2 * 2^n)
     * 空间复杂度：O(n * 2^n)
     */
    public static int tsp(int[][] graph) {
        int n = graph.length;
        int[][] dp = new int[1 << n][n];
        
        // 初始化
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        
        // 从城市0出发
        dp[1][0] = 0;
        
        // 枚举所有可能的状态
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0) continue;
                if (dp[mask][u] == Integer.MAX_VALUE) continue;
                
                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) != 0) continue;
                    if (graph[u][v] == Integer.MAX_VALUE) continue;
                    
                    dp[mask | (1 << v)][v] = Math.min(dp[mask | (1 << v)][v], dp[mask][u] + graph[u][v]);
                }
            }
        }
        
        // 找到最短的回路
        int result = Integer.MAX_VALUE;
        for (int u = 0; u < n; u++) {
            if (graph[u][0] != Integer.MAX_VALUE) {
                result = Math.min(result, dp[(1 << n) - 1][u] + graph[u][0]);
            }
        }
        
        return result;
    }
}