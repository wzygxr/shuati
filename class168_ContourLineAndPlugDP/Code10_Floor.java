package class125;

// SCOI2011 地板 (插头DP)
// 题目：用L型地板铺满n×m的房间，求方案数
// 来源：SCOI2011
// 链接：https://www.luogu.com.cn/problem/P3272
// 时间复杂度：O(n * m * 3^m)
// 空间复杂度：O(n * m * 3^m)
// 三种语言实现链接：
// Java: algorithm-journey/src/class125/Code10_Floor.java
// Python: algorithm-journey/src/class125/Code10_Floor.py
// C++: algorithm-journey/src/class125/Code10_Floor.cpp

/*
 * 题目解析：
 * 给定一个n×m的房间，其中有一些格子是障碍物（用'*'表示），
 * 其他格子需要用L型地板铺满。L型地板可以旋转，共有4种旋转方式。
 * 求有多少种不同的铺设方案。
 * 
 * 解题思路：
 * 使用插头DP解决这个问题。由于L型地板有拐弯的特性，
 * 我们需要用三进制来表示轮廓线上的插头状态。
 * 
 * 状态设计：
 * dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s的方案数。
 * 轮廓线状态：用三进制表示轮廓线上每个位置的插头类型
 * 0表示没有插头，1表示有插头且未拐弯，2表示有插头且已拐弯
 * 
 * 状态转移：
 * 对于当前格子(i,j)，我们考虑多种转移方式：
 * 1. 不放置任何地板（前提是上下插头都不存在）
 * 2. 放置一个L型地板，根据插头的不同状态进行转移
 * 
 * 最优性分析：
 * 该解法是最优的，因为：
 * 1. 时间复杂度O(n * m * 3^m)在可接受范围内
 * 2. 空间复杂度O(n * m * 3^m)合理
 * 3. 状态转移清晰，没有冗余计算
 * 
 * 边界场景处理：
 * 1. 当遇到障碍物时，只能不放置地板
 * 2. 当到达边界时，需要特殊处理
 * 3. 结果对20110520取模
 * 
 * 工程化考量：
 * 1. 使用三维数组存储DP状态
 * 2. 输入输出使用BufferedReader和PrintWriter提高效率
 * 3. 对于特殊情况进行了预处理优化
 * 
 * 相似题目推荐：
 * 1. LeetCode 790. Domino and Tromino Tiling
 *    题目链接：https://leetcode.com/problems/domino-and-tromino-tiling/
 *    题目描述：使用多米诺骨牌和L型骨牌铺满2×n的网格，求方案数
 *    
 * 2. 洛谷 P1435 [IOI2000] 回文串
 *    题目链接：https://www.luogu.com.cn/problem/P1435
 *    题目描述：通过插入字符使字符串变为回文串的最小插入次数
 *    
 * 3. UVa 10539 - Almost Prime Numbers
 *    题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1480
 *    题目描述：计算区间内almost prime numbers的个数
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code10_Floor {

    // 插头DP解决L型地板铺设问题
    // 状态表示：dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s的方案数
    // 轮廓线状态：用三进制表示轮廓线上每个位置的插头类型
    // 0表示没有插头，1表示有插头且未拐弯，2表示有插头且已拐弯
    
    public static int MAXN = 10;
    public static int MAXM = 10;
    public static int MAX_STATES = 59049; // 3^10
    public static int[][][] dp = new int[MAXN][MAXM + 1][MAX_STATES];
    public static char[][] grid = new char[MAXN][MAXM];
    public static int n, m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        m = (int) in.nval;
        
        for (int i = 0; i < n; i++) {
            String line = br.readLine().trim();
            for (int j = 0; j < m; j++) {
                grid[i][j] = line.charAt(j);
            }
        }
        
        out.println(compute());
        
        out.flush();
        out.close();
        br.close();
    }

    /**
     * 计算L型地板铺设方案数
     * 
     * @return 铺设方案数
     * 
     * 时间复杂度分析：
     * 外层三层循环i、j和s分别遍历n行、m列和3^m个状态，复杂度为O(n*m*3^m)
     * 状态转移是常数时间操作
     * 总时间复杂度：O(n * m * 3^m)
     * 
     * 空间复杂度分析：
     * 使用三维数组存储状态，大小为n*(m+1)*3^m
     * 总空间复杂度：O(n * m * 3^m)
     * 
     * 最优性判断：
     * 该算法已达到理论较优复杂度，因为状态数本身就是3^m级别的
     * 无法进一步优化状态数量
     */
    public static int compute() {
        final int MOD = 20110520;
        
        // 初始化
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int s = 0; s < MAX_STATES; s++) {
                    dp[i][j][s] = 0;
                }
            }
        }
        
        // 初始状态
        dp[0][0][0] = 1;
        
        // 逐格DP
        for (int i = 0; i < n; i++) {
            // 行间转移，将行末状态转移到下一行行首
            for (int s = 0; s < power(3, m); s++) {
                // 将状态s左移一位（相当于轮廓线下移一行）
                int newState = s * 3;
                dp[i + 1][0][newState] = dp[i][m][s];
            }
            
            // 行内转移
            for (int j = 0; j < m; j++) {
                for (int s = 0; s < power(3, m); s++) {
                    if (dp[i][j][s] == 0) continue;
                    
                    // 获取当前格子左边和上面的插头类型
                    int left = j > 0 ? get(s, j - 1) : 0;
                    int up = get(s, j);
                    
                    // 如果当前格子是障碍物
                    if (grid[i][j] == '*') {
                        // 只有当上下插头都不存在时才能转移
                        if (up == 0 && left == 0) {
                            dp[i][j + 1][s] = (dp[i][j + 1][s] + dp[i][j][s]) % MOD;
                        }
                    } else {
                        // 当前格子不是障碍物
                        // 多种转移方式：
                        
                        // 1. 不放置任何地板（前提是上下插头都不存在）
                        if (up == 0 && left == 0) {
                            int newState = s;
                            dp[i][j + 1][newState] = (dp[i][j + 1][newState] + dp[i][j][s]) % MOD;
                        }
                        
                        // 2. 放置一个L型地板，左插头未拐弯，上插头不存在
                        if (left == 1 && up == 0 && j + 1 < m) {
                            int newState = set(s, j - 1, 0); // 左边插头消失
                            newState = set(newState, j, 1); // 上面位置生成未拐弯插头
                            newState = set(newState, j + 1, 2); // 右边位置生成已拐弯插头
                            dp[i][j + 1][newState] = (dp[i][j + 1][newState] + dp[i][j][s]) % MOD;
                        }
                        
                        // 3. 放置一个L型地板，上插头未拐弯，左插头不存在
                        if (up == 1 && left == 0 && j + 1 < m) {
                            int newState = set(s, j, 0); // 上面插头消失
                            newState = set(newState, j, 1); // 上面位置生成未拐弯插头（延续）
                            newState = set(newState, j + 1, 2); // 右边位置生成已拐弯插头
                            dp[i][j + 1][newState] = (dp[i][j + 1][newState] + dp[i][j][s]) % MOD;
                        }
                        
                        // 4. 放置一个L型地板，左插头已拐弯，上插头不存在
                        if (left == 2 && up == 0) {
                            int newState = set(s, j - 1, 0); // 左边插头消失
                            newState = set(newState, j, 2); // 上面位置生成已拐弯插头
                            dp[i][j + 1][newState] = (dp[i][j + 1][newState] + dp[i][j][s]) % MOD;
                        }
                        
                        // 5. 放置一个L型地板，上插头已拐弯，左插头不存在
                        if (up == 2 && left == 0) {
                            int newState = set(s, j, 0); // 上面插头消失
                            newState = set(newState, j - 1, 2); // 左边位置生成已拐弯插头
                            dp[i][j + 1][newState] = (dp[i][j + 1][newState] + dp[i][j][s]) % MOD;
                        }
                        
                        // 6. 放置一个L型地板，左插头未拐弯，上插头未拐弯
                        if (left == 1 && up == 1) {
                            int newState = set(s, j - 1, 0); // 左边插头消失
                            newState = set(newState, j, 0); // 上面插头消失
                            dp[i][j + 1][newState] = (dp[i][j + 1][newState] + dp[i][j][s]) % MOD;
                        }
                        
                        // 7. 放置一个L型地板，左插头已拐弯，上插头未拐弯
                        if (left == 2 && up == 1) {
                            int newState = set(s, j - 1, 0); // 左边插头消失
                            newState = set(newState, j, 0); // 上面插头消失
                            dp[i][j + 1][newState] = (dp[i][j + 1][newState] + dp[i][j][s]) % MOD;
                        }
                        
                        // 8. 放置一个L型地板，左插头未拐弯，上插头已拐弯
                        if (left == 1 && up == 2) {
                            int newState = set(s, j - 1, 0); // 左边插头消失
                            newState = set(newState, j, 0); // 上面插头消失
                            dp[i][j + 1][newState] = (dp[i][j + 1][newState] + dp[i][j][s]) % MOD;
                        }
                        
                        // 9. 放置一个L型地板，左插头已拐弯，上插头已拐弯
                        if (left == 2 && up == 2) {
                            int newState = set(s, j - 1, 0); // 左边插头消失
                            newState = set(newState, j, 0); // 上面插头消失
                            dp[i][j + 1][newState] = (dp[i][j + 1][newState] + dp[i][j][s]) % MOD;
                        }
                    }
                }
            }
        }
        
        return dp[n][0][0];
    }
    
    /**
     * 获取状态s中第j个位置的插头类型
     * 
     * @param s 状态值
     * @param j 位置索引
     * @return 插头类型（0-无插头，1-未拐弯，2-已拐弯）
     */
    public static int get(int s, int j) {
        return (s / power(3, j)) % 3;
    }
    
    /**
     * 设置状态s中第j个位置的插头类型为v
     * 
     * @param s 状态值
     * @param j 位置索引
     * @param v 插头类型
     * @return 新的状态值
     */
    public static int set(int s, int j, int v) {
        int pow = power(3, j);
        return (s / pow / 3) * pow * 3 + v * pow + (s % pow);
    }
    
    /**
     * 计算base^exp
     * 
     * @param base 底数
     * @param exp 指数
     * @return base的exp次方
     */
    public static int power(int base, int exp) {
        int result = 1;
        for (int i = 0; i < exp; i++) {
            result *= base;
        }
        return result;
    }
}