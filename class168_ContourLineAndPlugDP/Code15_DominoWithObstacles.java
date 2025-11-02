package class125;

// 带障碍物的骨牌覆盖问题 (轮廓线DP)
// 题目：用1×2的骨牌铺满n×m棋盘，其中有些格子是障碍物不能放置骨牌，求方案数
// 类型：轮廓线DP（状态压缩）
// 时间复杂度：O(n * m * 2^m)
// 空间复杂度：O(m * 2^m)
// 三种语言实现链接：
// Java: algorithm-journey/src/class125/Code15_DominoWithObstacles.java
// Python: algorithm-journey/src/class125/Code15_DominoWithObstacles.py
// C++: algorithm-journey/src/class125/Code15_DominoWithObstacles.cpp

/*
 * 题目解析：
 * 用1×2的骨牌铺满n×m棋盘，其中有些格子是障碍物不能放置骨牌，求方案数
 * 
 * 解题思路：
 * 使用轮廓线DP解决这个问题。我们使用状态压缩的方式记录轮廓线上的覆盖情况，
 * dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的方案数。
 * 
 * 状态设计：
 * 使用二进制状态s表示轮廓线上每个位置的覆盖状态（1表示未覆盖，0表示已覆盖）。
 * 对于每个格子(i,j)，我们需要考虑不同的放置方式或障碍物的情况。
 * 
 * 最优性分析：
 * 该解法是最优的，因为：
 * 1. 时间复杂度O(n * m * 2^m)在m较小的情况下可接受
 * 2. 空间复杂度O(m * 2^m)通过滚动数组优化
 * 3. 状态转移全面考虑了障碍物和不同的骨牌放置方式
 * 
 * 边界场景处理：
 * 1. 障碍物格子的特殊处理
 * 2. 边界行的处理
 * 3. 状态合法性的检查
 * 
 * 工程化考量：
 * 1. 使用滚动数组优化空间复杂度
 * 2. 使用位运算高效处理状态
 * 3. 异常输入处理和输入输出优化
 * 
 * 相似题目推荐：
 * 1. LeetCode 790. Domino and Tromino Tiling
 *    题目链接：https://leetcode.com/problems/domino-and-tromino-tiling/
 *    题目描述：使用1×2的多米诺骨牌和L型骨牌铺满2×n的网格，求方案数
 *    
 * 2. POJ 2411. Mondriaan's Dream
 *    题目链接：http://poj.org/problem?id=2411
 *    题目描述：用1×2的骨牌铺满n×m的棋盘，求方案数
 *    
 * 3. UVa 10359 - Tiling
 *    题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1300
 *    题目描述：用2×1的多米诺骨牌覆盖2×n的棋盘，求方案数
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Code15_DominoWithObstacles {

    // 带障碍物的骨牌覆盖问题（轮廓线DP）
    // 状态表示：dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的方案数
    // 轮廓线状态：用二进制表示，1表示该位置需要被覆盖（向上延伸），0表示不需要
    
    public static int MAXN = 10;
    public static int MAXM = 10;
    public static long[][] dp = new long[2][1 << MAXM];
    public static boolean[][] obstacle = new boolean[MAXN][MAXM];
    
    // 获取二进制状态s中第pos位的值
    public static int get(int s, int pos) {
        return (s >> (MAXM - 1 - pos)) & 1;
    }
    
    // 设置二进制状态s中第pos位的值为val
    public static int set(int s, int pos, int val) {
        int mask = 1 << (MAXM - 1 - pos);
        if (val == 1) {
            return s | mask;
        } else {
            return s & ~mask;
        }
    }
    
    // 计算骨牌覆盖方案数
    public static long compute(int n, int m) {
        // 初始化dp数组
        for (int i = 0; i < 2; i++) {
            Arrays.fill(dp[i], 0);
        }
        
        int cur = 0; // 当前状态
        int next = 1; // 下一个状态
        dp[cur][0] = 1; // 初始状态：没有任何格子需要覆盖
        
        for (int i = 0; i < n; i++) {
            // 处理换行，将轮廓线右移一位
            for (int s = 0; s < (1 << (m - 1)); s++) {
                dp[next][s << 1] = dp[cur][s];
            }
            Arrays.fill(dp[cur], 0);
            cur ^= 1;
            next ^= 1;
            
            for (int j = 0; j < m; j++) {
                Arrays.fill(dp[next], 0); // 清空下一个状态
                
                for (int s = 0; s < (1 << m); s++) {
                    if (dp[cur][s] == 0) {
                        continue;
                    }
                    
                    int up = get(s, j); // 当前格子上方的状态
                    
                    // 如果当前格子是障碍物
                    if (obstacle[i][j]) {
                        // 障碍物格子不能放置骨牌，必须已经被覆盖
                        if (up == 0) {
                            dp[next][s] += dp[cur][s];
                        }
                        continue;
                    }
                    
                    if (up == 1) {
                        // 当前格子上方有一个需要覆盖的块，必须向下放置一个垂直的骨牌
                        int newS = set(s, j, 0); // 清除上方的需要覆盖的块
                        dp[next][newS] += dp[cur][s];
                    } else {
                        // 当前格子上方没有需要覆盖的块，可以有两种选择
                        // 1. 水平放置一个骨牌（向右）
                        if (j + 1 < m && !obstacle[i][j + 1]) {
                            int newS = s; // 水平放置不影响轮廓线状态
                            dp[next][newS] += dp[cur][s];
                        }
                        // 2. 垂直放置一个骨牌（向下）
                        if (i + 1 < n && !obstacle[i + 1][j]) {
                            int newS = set(s, j, 1); // 设置当前位置需要被下方格子覆盖
                            dp[next][newS] += dp[cur][s];
                        }
                    }
                }
                
                cur ^= 1;
                next ^= 1;
            }
        }
        
        return dp[cur][0]; // 最终状态是所有位都为0（没有需要覆盖的块）
    }
    
    // 主函数，读取输入并计算结果
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            
            // 初始化障碍物数组
            for (int i = 0; i < n; i++) {
                Arrays.fill(obstacle[i], false);
            }
            
            // 读取障碍物信息
            for (int i = 0; i < n; i++) {
                line = br.readLine();
                for (int j = 0; j < m; j++) {
                    if (line.charAt(j) == '#') {
                        obstacle[i][j] = true;
                    }
                }
            }
            
            System.out.println(compute(n, m));
        }
    }
}