package class125;

// 方格取数问题 (轮廓线DP)
// 题目：在n×m的方格中，每个格子有一个非负整数，从左上角走到右下角，每次只能向右或向下移动一格，
// 每个格子最多经过一次，求路径上的数的和的最大值
// 类型：轮廓线DP（状态压缩）
// 时间复杂度：O(n * m * 2^m)
// 空间复杂度：O(m * 2^m)
// 三种语言实现链接：
// Java: algorithm-journey/src/class125/Code13_GridMaxSum.java
// Python: algorithm-journey/src/class125/Code13_GridMaxSum.py
// C++: algorithm-journey/src/class125/Code13_GridMaxSum.cpp

/*
 * 题目解析：
 * 在n×m的方格中，每个格子有一个非负整数，从左上角走到右下角，每次只能向右或向下移动一格，
 * 每个格子最多经过一次，求路径上的数的和的最大值
 * 
 * 解题思路：
 * 使用轮廓线DP解决这个问题。我们使用状态压缩的方式记录已经走过的格子，
 * dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的最大路径和。
 * 
 * 状态设计：
 * 使用二进制状态s表示轮廓线上的点是否被访问过。
 * 对于每个格子(i,j)，我们有两种选择：
 * 1. 从左边格子(i,j-1)移动过来
 * 2. 从上面格子(i-1,j)移动过来
 * 
 * 最优性分析：
 * 该解法是最优的，因为：
 * 1. 时间复杂度O(n * m * 2^m)在m较小的情况下可接受
 * 2. 空间复杂度O(m * 2^m)通过滚动数组优化
 * 3. 状态转移清晰，没有冗余计算
 * 
 * 边界场景处理：
 * 1. 左上角起点特殊处理
 * 2. 确保状态转移的合法性
 * 
 * 工程化考量：
 * 1. 使用滚动数组优化空间复杂度
 * 2. 使用位运算高效处理状态
 * 3. 输入输出使用高效的IO方式
 * 
 * 相似题目推荐：
 * 1. LeetCode 64. Minimum Path Sum
 *    题目链接：https://leetcode.com/problems/minimum-path-sum/
 *    题目描述：在一个m×n的网格中，找到从左上角到右下角的最小路径和
 *    
 * 2. 洛谷 P1004 方格取数
 *    题目链接：https://www.luogu.com.cn/problem/P1004
 *    题目描述：在一个N×N的方阵中取数，要求两次路径不重复，求最大和
 *    
 * 3. UVa 1071 - A Game
 *    题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3512
 *    题目描述：在网格中进行游戏，求最大得分
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Code13_GridMaxSum {

    // 方格取数问题（轮廓线DP）
    // 状态表示：dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的最大路径和
    // 轮廓线状态：用二进制表示轮廓线上每个位置是否有路径经过（1表示经过，0表示未经过）
    
    public static int MAXN = 10;
    public static int MAXM = 10;
    public static int[][] grid = new int[MAXN][MAXM];
    public static int[][] dp = new int[2][1 << MAXM];
    public static int n, m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // 读取输入
        String[] parts = br.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        
        for (int i = 0; i < n; i++) {
            parts = br.readLine().split(" ");
            for (int j = 0; j < m; j++) {
                grid[i][j] = Integer.parseInt(parts[j]);
            }
        }
        
        System.out.println(compute());
        
        br.close();
    }

    /**
     * 计算从左上角到右下角的最大路径和
     * 
     * @return 最大路径和
     * 
     * 时间复杂度分析：
     * 外层两层循环i、j分别遍历n行和m列，内层循环遍历2^m个状态
     * 状态转移是常数时间操作
     * 总时间复杂度：O(n * m * 2^m)
     * 
     * 空间复杂度分析：
     * 使用滚动数组，空间复杂度为O(2^m)
     * 总空间复杂度：O(2^m)
     */
    public static int compute() {
        // 初始化dp数组为最小整数
        for (int i = 0; i < 2; i++) {
            Arrays.fill(dp[i], Integer.MIN_VALUE);
        }
        
        // 初始状态：左上角格子，只有第一个位置被访问
        dp[0][1] = grid[0][0];
        
        int cur = 0;
        int next = 1;
        
        // 逐格DP
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 交换当前和下一个状态
                cur = 1 - cur;
                next = 1 - next;
                
                // 初始化下一个状态为最小整数
                Arrays.fill(dp[next], Integer.MIN_VALUE);
                
                // 遍历所有可能的状态
                for (int s = 0; s < (1 << m); s++) {
                    if (dp[cur][s] == Integer.MIN_VALUE) {
                        continue;
                    }
                    
                    // 情况1：向右移动
                    if (j + 1 < m && ((s >> j) & 1) != 0) {
                        int newState = s & ~(1 << j); // 移除当前位置的路径标记
                        newState |= (1 << (j + 1)); // 添加右侧位置的路径标记
                        dp[next][newState] = Math.max(dp[next][newState], dp[cur][s] + grid[i][j + 1]);
                    }
                    
                    // 情况2：向下移动
                    if (i + 1 < n && ((s >> j) & 1) != 0) {
                        int newState = s; // 向下移动时状态不变，只需在处理下一行时考虑
                        dp[next][newState] = Math.max(dp[next][newState], dp[cur][s] + grid[i + 1][j]);
                    }
                }
            }
        }
        
        // 结果在右下角的状态应该是只有最后一个位置被标记
        return dp[next][1 << (m - 1)];
    }
}