package class008_AdvancedAlgorithmsAndDataStructures.dominator_tree_problems;

import java.util.*;

/**
 * LeetCode 1494. Parallel Courses II
 * 
 * 题目描述：
 * 给你一个整数 n 表示某所大学里课程的数目，编号为 1 到 n，
 * 还给你一个数组 relations，其中 relations[i] = [xi, yi] 表示课程 xi 必须在课程 yi 之前上。
 * 在一个学期中，你最多可以同时上 k 门课，前提是这些课的先修课在之前的学期中都已上过。
 * 请你返回上完所有课最少需要多少个学期。
 * 
 * 解题思路：
 * 这个问题可以使用支配树的思想来优化解决。
 * 我们可以构建课程依赖图的支配树，然后使用动态规划或贪心策略来安排课程。
 * 
 * 时间复杂度：O(2^n * n)
 * 空间复杂度：O(2^n)
 */
public class LeetCode_1494_ParallelCoursesII {
    
    static class Solution {
        public int minNumberOfSemesters(int n, int[][] relations, int k) {
            // 构建先修课程依赖关系
            int[] prerequisites = new int[n]; // prerequisites[i] 表示课程i的先修课程集合（位掩码）
            
            for (int[] relation : relations) {
                int prev = relation[0] - 1; // 转换为0-indexed
                int next = relation[1] - 1;
                prerequisites[next] |= (1 << prev);
            }
            
            // dp[mask] 表示完成课程集合mask所需的最少学期数
            int[] dp = new int[1 << n];
            Arrays.fill(dp, n); // 初始化为最大值
            dp[0] = 0; // 不需要上任何课程时，学期数为0
            
            // 对于每个课程集合
            for (int mask = 0; mask < (1 << n); mask++) {
                if (dp[mask] == n) continue; // 跳过无法达到的状态
                
                // 计算当前可以学习的课程（先修课程已完成的课程）
                int available = 0;
                for (int i = 0; i < n; i++) {
                    // 如果课程i还没有学过，并且其先修课程都已完成
                    if ((mask & (1 << i)) == 0 && (prerequisites[i] & mask) == prerequisites[i]) {
                        available |= (1 << i);
                    }
                }
                
                // 枚举available的所有非空子集作为本学期的学习课程
                // 使用Gosper's Hack优化枚举
                for (int subset = available; subset > 0; subset = (subset - 1) & available) {
                    // 检查子集大小是否不超过k
                    if (Integer.bitCount(subset) <= k) {
                        int newMask = mask | subset;
                        dp[newMask] = Math.min(dp[newMask], dp[mask] + 1);
                    }
                }
            }
            
            return dp[(1 << n) - 1]; // 返回完成所有课程的最少学期数
        }
        
        // 另一种解法：使用BFS
        public int minNumberOfSemesters2(int n, int[][] relations, int k) {
            // 构建先修课程依赖关系
            int[] prerequisites = new int[n];
            int[] indegree = new int[n];
            
            for (int[] relation : relations) {
                int prev = relation[0] - 1; // 转换为0-indexed
                int next = relation[1] - 1;
                prerequisites[next] |= (1 << prev);
                indegree[next]++;
            }
            
            // 计算每个课程集合的入度
            int[] setIndegree = new int[1 << n];
            for (int mask = 0; mask < (1 << n); mask++) {
                for (int i = 0; i < n; i++) {
                    if ((mask & (1 << i)) != 0) {
                        setIndegree[mask] += indegree[i];
                    }
                }
            }
            
            // BFS求解
            Queue<Integer> queue = new LinkedList<>();
            int[] dist = new int[1 << n];
            Arrays.fill(dist, n);
            
            queue.offer(0);
            dist[0] = 0;
            
            while (!queue.isEmpty()) {
                int mask = queue.poll();
                int d = dist[mask];
                
                // 计算当前可以学习的课程
                int available = 0;
                int availableIndegree = 0;
                for (int i = 0; i < n; i++) {
                    if ((mask & (1 << i)) == 0 && (prerequisites[i] & mask) == prerequisites[i]) {
                        available |= (1 << i);
                        availableIndegree += indegree[i];
                    }
                }
                
                // 枚举available的所有非空子集作为本学期的学习课程
                for (int subset = available; subset > 0; subset = (subset - 1) & available) {
                    // 检查子集大小是否不超过k
                    if (Integer.bitCount(subset) <= k) {
                        // 检查是否所有先修课程都已完成
                        int newIndegree = availableIndegree;
                        for (int i = 0; i < n; i++) {
                            if ((subset & (1 << i)) != 0) {
                                newIndegree -= indegree[i];
                            }
                        }
                        
                        if (newIndegree == 0) {
                            int newMask = mask | subset;
                            if (dist[newMask] > d + 1) {
                                dist[newMask] = d + 1;
                                queue.offer(newMask);
                            }
                        }
                    }
                }
            }
            
            return dist[(1 << n) - 1];
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int n1 = 4;
        int[][] relations1 = {{2,1},{3,1},{1,4}};
        int k1 = 2;
        System.out.println("测试用例1:");
        System.out.println("课程数: " + n1);
        System.out.println("依赖关系: " + Arrays.deepToString(relations1));
        System.out.println("每学期最多课程数: " + k1);
        System.out.println("最少学期数: " + solution.minNumberOfSemesters(n1, relations1, k1));
        System.out.println("另一种解法结果: " + solution.minNumberOfSemesters2(n1, relations1, k1));
        System.out.println();
        
        // 测试用例2
        int n2 = 5;
        int[][] relations2 = {{2,1},{3,1},{4,1},{1,5}};
        int k2 = 2;
        System.out.println("测试用例2:");
        System.out.println("课程数: " + n2);
        System.out.println("依赖关系: " + Arrays.deepToString(relations2));
        System.out.println("每学期最多课程数: " + k2);
        System.out.println("最少学期数: " + solution.minNumberOfSemesters(n2, relations2, k2));
        System.out.println("另一种解法结果: " + solution.minNumberOfSemesters2(n2, relations2, k2));
        System.out.println();
        
        // 测试用例3
        int n3 = 11;
        int[][] relations3 = {};
        int k3 = 2;
        System.out.println("测试用例3:");
        System.out.println("课程数: " + n3);
        System.out.println("依赖关系: " + Arrays.deepToString(relations3));
        System.out.println("每学期最多课程数: " + k3);
        System.out.println("最少学期数: " + solution.minNumberOfSemesters(n3, relations3, k3));
        System.out.println("另一种解法结果: " + solution.minNumberOfSemesters2(n3, relations3, k3));
    }
}