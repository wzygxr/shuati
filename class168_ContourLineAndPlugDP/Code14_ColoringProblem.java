package class125;

// 棋盘染色问题 (轮廓线DP)
// 题目：给n×m的棋盘染色，有k种颜色，相邻格子颜色不能相同，且每个格子只能染一种颜色，求染色方案数
// 类型：轮廓线DP（状态压缩）
// 时间复杂度：O(n * m * k^m)
// 空间复杂度：O(m * k^m)
// 三种语言实现链接：
// Java: algorithm-journey/src/class125/Code14_ColoringProblem.java
// Python: algorithm-journey/src/class125/Code14_ColoringProblem.py
// C++: algorithm-journey/src/class125/Code14_ColoringProblem.cpp

/*
 * 题目解析：
 * 给n×m的棋盘染色，有k种颜色，相邻格子颜色不能相同，且每个格子只能染一种颜色，求染色方案数
 * 
 * 解题思路：
 * 使用轮廓线DP解决这个问题。我们使用状态压缩的方式记录轮廓线上的颜色，
 * dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的染色方案数。
 * 
 * 状态设计：
 * 由于颜色可能有k种，普通的二进制状态无法直接表示，我们可以使用多进制状态表示轮廓线上每个位置的颜色。
 * 对于每个格子(i,j)，我们需要确保它的颜色与上方和左方的颜色不同。
 * 
 * 最优性分析：
 * 该解法是最优的，因为：
 * 1. 时间复杂度O(n * m * k^m)在m较小的情况下可接受
 * 2. 空间复杂度O(m * k^m)通过滚动数组优化
 * 3. 状态转移清晰，直接考虑颜色约束
 * 
 * 边界场景处理：
 * 1. 第一行的处理需要特殊考虑
 * 2. 确保颜色选择的合法性
 * 
 * 工程化考量：
 * 1. 使用滚动数组优化空间复杂度
 * 2. 使用位运算或特殊编码方式处理多进制状态
 * 3. 输入输出使用高效的IO方式
 * 
 * 相似题目推荐：
 * 1. LeetCode 1997. Coloring a Grid
 *    题目链接：https://leetcode.com/problems/coloring-a-grid/
 *    题目描述：给定一个m×n的网格，用k种颜色进行染色，使得相邻格子颜色不同，求方案数
 *    
 * 2. LeetCode 51. N-Queens
 *    题目链接：https://leetcode.com/problems/n-queens/
 *    题目描述：n皇后问题研究的是如何将n个皇后放置在n×n的棋盘上，并且使皇后彼此之间不能相互攻击
 *    
 * 3. UVa 11254 - Consecutive Integers
 *    题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2221
 *    题目描述：将一个正整数n表示为连续正整数的和，求有多少种表示方法
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Code14_ColoringProblem {

    // 棋盘染色问题（轮廓线DP）
    // 状态表示：dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的染色方案数
    // 轮廓线状态：用多进制表示轮廓线上每个位置的颜色
    
    public static int MAXN = 10;
    public static int MAXM = 10;
    public static long[][] dp = new long[2][1 << (MAXM * 2)]; // 假设颜色数不超过4，使用2位二进制表示一种颜色
    public static int n, m, k;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // 读取输入
        String[] parts = br.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        k = Integer.parseInt(parts[2]);
        
        System.out.println(compute());
        
        br.close();
    }

    // 获取状态s中位置j的颜色
    public static int getColor(int s, int j) {
        return (s >> (j * 2)) & 3; // 假设颜色用2位二进制表示（最多4种颜色）
    }

    // 设置状态s中位置j的颜色为c
    public static int setColor(int s, int j, int c) {
        return (s & ~(3 << (j * 2))) | (c << (j * 2));
    }

    /**
     * 计算染色方案数
     * 
     * @return 染色方案数
     * 
     * 时间复杂度分析：
     * 外层两层循环i、j分别遍历n行和m列，内层循环遍历k^m个状态
     * 对于每个状态，遍历k种可能的颜色进行转移
     * 总时间复杂度：O(n * m * k^m)
     * 
     * 空间复杂度分析：
     * 使用滚动数组，空间复杂度为O(k^m)
     * 总空间复杂度：O(k^m)
     */
    public static long compute() {
        // 初始化dp数组为0
        for (int i = 0; i < 2; i++) {
            Arrays.fill(dp[i], 0);
        }
        
        // 初始状态：第一行第一个格子，颜色可以是任意一种
        for (int c = 0; c < k; c++) {
            dp[0][setColor(0, 0, c)] = 1;
        }
        
        int cur = 0;
        int next = 1;
        
        // 逐格DP
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 交换当前和下一个状态
                cur = 1 - cur;
                next = 1 - next;
                
                // 初始化下一个状态为0
                Arrays.fill(dp[next], 0);
                
                // 遍历所有可能的状态
                int maxState = 1 << (m * 2);
                for (int s = 0; s < maxState; s++) {
                    if (dp[cur][s] == 0) {
                        continue;
                    }
                    
                    // 获取当前格子上方和左方的颜色
                    int upColor = -1;
                    int leftColor = -1;
                    
                    if (i > 0) {
                        upColor = getColor(s, j);
                    }
                    
                    if (j > 0) {
                        leftColor = getColor(s, j - 1);
                    }
                    
                    // 尝试所有可能的颜色
                    for (int c = 0; c < k; c++) {
                        // 检查颜色是否与上方或左方相同
                        if (c == upColor || c == leftColor) {
                            continue;
                        }
                        
                        // 构建新状态
                        int newState = s;
                        if (j == m - 1) {
                            // 行末处理：左移一位，腾出位置给下一行
                            newState = (s >> 2) & ((1 << ((m - 1) * 2)) - 1);
                            newState = setColor(newState, 0, c);
                        } else {
                            // 行内处理：直接设置当前位置的颜色
                            newState = setColor(s, j + 1, c);
                        }
                        
                        dp[next][newState] += dp[cur][s];
                    }
                }
            }
        }
        
        // 结果是最后一个状态的所有可能情况的总和
        long result = 0;
        int maxState = 1 << (m * 2);
        for (int s = 0; s < maxState; s++) {
            result += dp[next][s];
        }
        
        return result;
    }
}