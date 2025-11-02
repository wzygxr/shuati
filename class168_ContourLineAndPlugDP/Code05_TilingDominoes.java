package class125;

// Tiling Dominoes (轮廓线DP)
// 用1×2的骨牌铺满n×m棋盘，求方案数
// 1 <= n, m <= 100
// n*m <= 100
// 测试链接 : https://vjudge.net/problem/UVA-11270

// 题目解析：
// 给定一个n×m的棋盘，需要用1×2的骨牌完全覆盖它，求有多少种不同的覆盖方案。
// 这是一个经典的骨牌覆盖问题，也称为多米诺骨牌覆盖问题。

// 解题思路：
// 使用轮廓线DP解决这个问题。轮廓线DP是一种特殊的动态规划方法，
// 适用于解决棋盘类问题。我们逐格转移，将棋盘的边界线（轮廓线）
// 的状态作为DP状态的一部分。

// 状态设计：
// dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s的方案数。
// 轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段。
// 状态s用二进制表示，第k位为1表示轮廓线第k个位置已被占用，为0表示未被占用。

// 状态转移：
// 对于当前格子(i,j)，我们考虑三种放置骨牌的方式：
// 1. 不放骨牌（前提是上面已经被覆盖）
// 2. 竖着放（当前格子和下面格子），前提是上面没有被覆盖
// 3. 横着放（当前格子和右面格子），前提是左面没有被覆盖

// 最优性分析：
// 该解法是最优的，因为：
// 1. 时间复杂度O(n * m * 2^m)在可接受范围内
// 2. 空间复杂度通过滚动数组优化至O(2^m)
// 3. 状态转移清晰，没有冗余计算

// 边界场景处理：
// 1. 当n=0或m=0时，方案数为1（空棋盘有一种覆盖方案）
// 2. 当n或m为奇数且另一个为偶数时，方案数为0
// 3. 通过交换n和m确保m较小，优化时间复杂度

// 工程化考量：
// 1. 使用滚动数组优化空间复杂度
// 2. 输入输出使用BufferedReader和PrintWriter提高效率
// 3. 对于特殊情况进行了预处理优化

// Java实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code05_TilingDominoes.java
// Python实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code05_TilingDominoes.py
// C++实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code05_TilingDominoes.cpp

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code05_TilingDominoes {

    // 由于n*m <= 100，且min(n,m) <= 10，使用轮廓线DP
    // 轮廓线DP的状态表示：dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s的方案数
    // 轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段
    
    public static int MAXM = 10;
    public static long[][] dp = new long[2][1 << MAXM];
    public static int n, m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            n = (int) in.nval;
            in.nextToken();
            m = (int) in.nval;
            
            // 为了优化，让较小的值作为列数
            if (n < m) {
                int temp = n;
                n = m;
                m = temp;
            }
            
            out.println(compute());
        }
        
        out.flush();
        out.close();
        br.close();
    }

    // 时间复杂度: O(n * m * 2^m)
    // 空间复杂度: O(2^m)
    public static long compute() {
        // 初始化
        Arrays.fill(dp[0], 0, 1 << m, 0);
        dp[0][(1 << m) - 1] = 1; // 初始状态，第一行之前的所有位置都被覆盖
        
        int cur = 0;
        int next = 1;
        
        // 逐格DP
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 交换当前和下一个状态
                cur = 1 - cur;
                next = 1 - next;
                Arrays.fill(dp[next], 0, 1 << m, 0);
                
                // 遍历所有轮廓线状态
                for (int s = 0; s < (1 << m); s++) {
                    if (dp[cur][s] == 0) continue;
                    
                    // 当前格子(i,j)的上面是第j位，左面是第j-1位
                    // 获取当前格子上面是否被覆盖
                    boolean up = (s & (1 << (m - 1 - j))) != 0;
                    // 获取当前格子左面是否被覆盖
                    boolean left = j > 0 && (s & (1 << (m - j))) != 0;
                    
                    // 三种转移方式：
                    // 1. 不放骨牌（前提是上面已经被覆盖）
                    if (up) {
                        int newState = s & ~(1 << (m - 1 - j)); // 当前格子不覆盖
                        dp[next][newState] += dp[cur][s];
                    }
                    
                    // 2. 竖着放（当前格子和下面格子），前提是上面没有被覆盖
                    if (!up && i + 1 < n) {
                        int newState = (s | (1 << (m - 1 - j))); // 当前格子覆盖
                        dp[next][newState] += dp[cur][s];
                    }
                    
                    // 3. 横着放（当前格子和右面格子），前提是左面没有被覆盖
                    if (!left && j + 1 < m) {
                        int newState = s & ~(1 << (m - 1 - j)); // 当前格子覆盖
                        newState |= (1 << (m - 2 - j)); // 右边格子覆盖
                        dp[next][newState] += dp[cur][s];
                    }
                }
            }
        }
        
        return dp[next][(1 << m) - 1];
    }
}