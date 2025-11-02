package class081.补充题目;

import java.util.Arrays;

// 旅行商问题 (Traveling Salesman Problem)
// 题目来源: 经典TSP问题，各大OJ均有变种
// 题目描述:
// 给定n个城市和它们之间的距离矩阵，求从某个城市出发，访问每个城市恰好一次并回到起点的最短路径长度。
//
// 解题思路:
// 使用状态压缩动态规划解决经典的TSP问题
// dp[mask][i] 表示访问了mask代表的城市集合，且当前位于城市i时的最短路径长度
//
// 时间复杂度: O(n^2 * 2^n)
// 空间复杂度: O(n * 2^n)
//
// 补充题目1: 哈密顿路径 (Hamiltonian Path)
// 题目来源: LeetCode 980. Unique Paths III
// 题目链接: https://leetcode.com/problems/unique-paths-iii/
// 题目描述:
// 在网格中找到从起点到终点，经过所有空单元格恰好一次的路径数量
// 解题思路:
// 1. 使用状态压缩DP记录访问过的单元格
// 2. dp[mask][pos] 表示访问了mask代表的单元格集合，当前位置为pos时的路径数量
// 时间复杂度: O(2^k * k) 其中k是空单元格数量
// 空间复杂度: O(2^k * k)

// 补充题目2: 最大兼容数对 (Compatible Numbers)
// 题目来源: CodeForces 165E
// 题目链接: https://codeforces.com/problemset/problem/165/E
// 题目描述:
// 给定一个数组，对于每个数字，找到另一个数字，使得它们的按位与结果为0
// 解题思路:
// 1. 使用SOS DP（Sum Over Subsets）技术
// 2. 预处理每个数字的补集的所有子集
// 时间复杂度: O(n * 2^k) 其中k是位数
// 空间复杂度: O(2^k)

public class Code09_TSP {
    
    // 经典TSP问题解法
    public static int tsp(int[][] graph) {
        int n = graph.length;
        if (n == 0) return 0;
        
        // dp[mask][i] 表示访问了mask代表的城市集合，当前在城市i的最短路径长度
        int[][] dp = new int[1 << n][n];
        
        // 初始化：所有状态设为最大值
        for (int i = 0; i < (1 << n); i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        
        // 起点状态：只访问了起点城市
        dp[1][0] = 0;
        
        // 状态转移
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int i = 0; i < n; i++) {
                // 如果当前状态不可达，跳过
                if (dp[mask][i] == Integer.MAX_VALUE) {
                    continue;
                }
                
                // 尝试访问下一个未访问的城市
                for (int j = 0; j < n; j++) {
                    // 如果城市j已经被访问过，跳过
                    if ((mask & (1 << j)) != 0) {
                        continue;
                    }
                    
                    // 如果从i到j有路径
                    if (graph[i][j] > 0) {
                        int newMask = mask | (1 << j);
                        int newDist = dp[mask][i] + graph[i][j];
                        if (dp[newMask][j] > newDist) {
                            dp[newMask][j] = newDist;
                        }
                    }
                }
            }
        }
        
        // 找到访问所有城市并回到起点的最短路径
        int result = Integer.MAX_VALUE;
        int allVisited = (1 << n) - 1;
        for (int i = 0; i < n; i++) {
            // 从城市i回到起点0
            if (dp[allVisited][i] != Integer.MAX_VALUE && graph[i][0] > 0) {
                result = Math.min(result, dp[allVisited][i] + graph[i][0]);
            }
        }
        
        return result == Integer.MAX_VALUE ? -1 : result;
    }
    
    // LeetCode 980 哈密顿路径解法
    public static int uniquePathsIII(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int startX = -1, startY = -1;
        int targetX = -1, targetY = -1;
        int emptyCount = 0;
        
        // 统计空单元格数量，找到起点和终点
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    emptyCount++;
                } else if (grid[i][j] == 1) {
                    startX = i;
                    startY = j;
                } else if (grid[i][j] == 2) {
                    targetX = i;
                    targetY = j;
                }
            }
        }
        
        // 总单元格数 = 空单元格 + 起点 + 终点
        int totalCells = emptyCount + 2;
        
        // dp[mask][pos] 表示访问了mask代表的单元格集合，当前位置为pos时的路径数量
        // pos = i * n + j
        int totalStates = 1 << totalCells;
        int[][] dp = new int[totalStates][totalCells];
        
        // 起点状态
        int startPos = startX * n + startY;
        int startMask = 1 << startPos;
        dp[startMask][startPos] = 1;
        
        // 状态转移
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        
        for (int mask = 0; mask < totalStates; mask++) {
            for (int pos = 0; pos < totalCells; pos++) {
                if (dp[mask][pos] == 0) {
                    continue;
                }
                
                int x = pos / n;
                int y = pos % n;
                
                // 尝试四个方向移动
                for (int d = 0; d < 4; d++) {
                    int nx = x + dx[d];
                    int ny = y + dy[d];
                    
                    // 检查边界和障碍物
                    if (nx < 0 || nx >= m || ny < 0 || ny >= n || grid[nx][ny] == -1) {
                        continue;
                    }
                    
                    int newPos = nx * n + ny;
                    // 如果新位置已经被访问过，跳过
                    if ((mask & (1 << newPos)) != 0) {
                        continue;
                    }
                    
                    int newMask = mask | (1 << newPos);
                    dp[newMask][newPos] += dp[mask][pos];
                }
            }
        }
        
        // 终点状态：访问了所有单元格
        int targetPos = targetX * n + targetY;
        int targetMask = (1 << totalCells) - 1;
        return dp[targetMask][targetPos];
    }
    
    // CodeForces 165E 最大兼容数对解法
    public static int[] compatibleNumbers(int[] nums) {
        int n = nums.length;
        int maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }
        
        // 找到最大值的位数
        int bits = 0;
        while ((1 << bits) <= maxVal) {
            bits++;
        }
        
        // 预处理每个数字的补集
        int[] complement = new int[1 << bits];
        Arrays.fill(complement, -1);
        
        // 将数组中的数字存入complement数组
        for (int i = 0; i < n; i++) {
            complement[nums[i]] = i;
        }
        
        // 使用SOS DP技术填充补集的子集
        for (int mask = 0; mask < (1 << bits); mask++) {
            if (complement[mask] != -1) {
                // 对于mask的所有子集，如果还没有赋值，就赋值为当前索引
                for (int subMask = mask; subMask > 0; subMask = (subMask - 1) & mask) {
                    if (complement[subMask] == -1) {
                        complement[subMask] = complement[mask];
                    }
                }
            }
        }
        
        // 结果数组
        int[] result = new int[n];
        Arrays.fill(result, -1);
        
        // 对每个数字寻找兼容数
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            // 计算num的补集
            int complementMask = ((1 << bits) - 1) ^ num;
            if (complement[complementMask] != -1) {
                result[i] = complement[complementMask];
            }
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试TSP问题
        int[][] graph = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        System.out.println("TSP问题测试:");
        System.out.println("最短路径长度: " + tsp(graph));
        
        // 测试LeetCode 980 哈密顿路径
        int[][] grid = {
            {1, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 2, -1}
        };
        System.out.println("\nLeetCode 980 哈密顿路径测试:");
        System.out.println("路径数量: " + uniquePathsIII(grid));
        
        // 测试CodeForces 165E 最大兼容数对
        int[] nums = {3, 1, 4, 2};
        System.out.println("\nCodeForces 165E 最大兼容数对测试:");
        int[] result = compatibleNumbers(nums);
        System.out.print("数组: ");
        for (int num : nums) {
            System.out.print(num + " ");
        }
        System.out.println();
        System.out.print("结果: ");
        for (int idx : result) {
            System.out.print(idx + " ");
        }
        System.out.println();
    }
}

// C++实现请参考独立的Code09_TSP.cpp文件
// Python实现请参考独立的Code09_TSP.py文件