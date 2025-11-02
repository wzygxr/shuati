package class072;

import java.util.Arrays;

/**
 * AtCoder LIS问题变种
 * 
 * 题目来源：AtCoder ABC237 F |LIS| = 3
 * 题目链接：https://atcoder.jp/contests/abc237/tasks/abc237_f
 * 题目描述：给定三个整数N, M, K，求满足以下条件的序列A=(A1,A2,…,AN)的数量：
 * 1. 1 ≤ Ai ≤ M (1 ≤ i ≤ N)
 * 2. 序列A的最长递增子序列的长度恰好等于K
 * 
 * 算法思路：
 * 1. 使用动态规划计数LIS长度恰好为K的序列数量
 * 2. 状态定义：dp[i][a][b][c]表示处理到第i个元素，当前LIS状态为(a,b,c)的方案数
 * 3. 状态转移：对于每个新元素，更新LIS状态
 * 4. 由于K≤3，可以使用三维状态压缩
 * 
 * 时间复杂度：O(N*M^K) - 状态数为N*M^K
 * 空间复杂度：O(N*M^K) - 需要存储DP状态
 * 是否最优解：是，这是标准解法
 * 
 * 示例：
 * 输入: N=3, M=2, K=2
 * 输出: 4
 * 解释：满足条件的序列有：[1,1,2], [1,2,1], [1,2,2], [2,1,2]
 */

public class Code18_AtCoderLISProblem {

    /**
     * 计算满足条件的序列数量
     * 
     * @param N 序列长度
     * @param M 元素取值范围[1, M]
     * @param K 要求的LIS长度
     * @return 满足条件的序列数量（对998244353取模）
     */
    public static int countSequences(int N, int M, int K) {
        if (K > 3) return 0; // 题目保证K≤3
        
        final int MOD = 998244353;
        
        // dp[i][a][b][c]表示处理到第i个元素，当前LIS状态为(a,b,c)的方案数
        // 其中a≤b≤c，且a,b,c表示当前LIS的最小可能结尾元素
        int[][][][] dp = new int[N + 1][M + 2][M + 2][M + 2];
        
        // 初始化：空序列的LIS长度为0
        dp[0][M + 1][M + 1][M + 1] = 1; // 用M+1表示无穷大
        
        for (int i = 0; i < N; i++) {
            for (int a = 1; a <= M + 1; a++) {
                for (int b = a; b <= M + 1; b++) {
                    for (int c = b; c <= M + 1; c++) {
                        if (dp[i][a][b][c] == 0) continue;
                        
                        int current = dp[i][a][b][c];
                        
                        // 尝试添加新的元素x (1 ≤ x ≤ M)
                        for (int x = 1; x <= M; x++) {
                            int na = a, nb = b, nc = c;
                            
                            if (x <= a) {
                                // 替换第一个位置
                                na = x;
                            } else if (x <= b) {
                                // 替换第二个位置
                                nb = x;
                            } else if (x <= c) {
                                // 替换第三个位置
                                nc = x;
                            } else {
                                // 如果x大于c，需要扩展LIS（但K≤3，所以最多到第三个位置）
                                continue;
                            }
                            
                            // 更新状态
                            dp[i + 1][na][nb][nc] = (dp[i + 1][na][nb][nc] + current) % MOD;
                        }
                    }
                }
            }
        }
        
        // 统计所有LIS长度恰好为K的序列数量
        int result = 0;
        for (int a = 1; a <= M + 1; a++) {
            for (int b = a; b <= M + 1; b++) {
                for (int c = b; c <= M + 1; c++) {
                    // 计算当前状态的LIS长度
                    int lisLength = 0;
                    if (a <= M) lisLength++;
                    if (b <= M) lisLength++;
                    if (c <= M) lisLength++;
                    
                    if (lisLength == K) {
                        result = (result + dp[N][a][b][c]) % MOD;
                    }
                }
            }
        }
        
        return result;
    }

    /**
     * 简化版本：使用更高效的状态表示
     * 
     * 算法思路：
     * 1. 使用一维数组存储状态，通过编码减少维度
     * 2. 状态编码：将(a,b,c)编码为一个整数
     * 3. 减少内存使用，提高效率
     * 
     * 时间复杂度：O(N*M^K) - 与原始方法相同
     * 空间复杂度：O(N*M^K) - 但通过编码减少实际内存使用
     * 是否最优解：是，优化版本
     * 
     * @param N 序列长度
     * @param M 元素取值范围[1, M]
     * @param K 要求的LIS长度
     * @return 满足条件的序列数量（对998244353取模）
     */
    public static int countSequencesOptimized(int N, int M, int K) {
        if (K > 3) return 0;
        
        final int MOD = 998244353;
        final int MAX_STATES = (M + 2) * (M + 2) * (M + 2);
        
        int[][] dp = new int[2][MAX_STATES];
        // 初始化状态
        int initialState = (M + 1) * (M + 2) * (M + 2) + (M + 1) * (M + 2) + (M + 1);
        dp[0][initialState] = 1;
        
        for (int i = 0; i < N; i++) {
            int current = i % 2;
            int next = (i + 1) % 2;
            Arrays.fill(dp[next], 0);
            
            for (int state = 0; state < MAX_STATES; state++) {
                if (dp[current][state] == 0) continue;
                
                // 解码状态
                int c = state % (M + 2);
                int b = (state / (M + 2)) % (M + 2);
                int a = state / ((M + 2) * (M + 2));
                
                int currentVal = dp[current][state];
                
                for (int x = 1; x <= M; x++) {
                    int na = a, nb = b, nc = c;
                    
                    if (x <= a) {
                        na = x;
                    } else if (x <= b) {
                        nb = x;
                    } else if (x <= c) {
                        nc = x;
                    } else {
                        continue;
                    }
                    
                    // 状态编码
                    int nextState = na * (M + 2) * (M + 2) + nb * (M + 2) + nc;
                    dp[next][nextState] = (dp[next][nextState] + currentVal) % MOD;
                }
            }
        }
        
        int result = 0;
        int finalIndex = N % 2;
        
        for (int state = 0; state < MAX_STATES; state++) {
            if (dp[finalIndex][state] == 0) continue;
            
            // 解码状态
            int c = state % (M + 2);
            int b = (state / (M + 2)) % (M + 2);
            int a = state / ((M + 2) * (M + 2));
            
            int lisLength = 0;
            if (a <= M) lisLength++;
            if (b <= M) lisLength++;
            if (c <= M) lisLength++;
            
            if (lisLength == K) {
                result = (result + dp[finalIndex][state]) % MOD;
            }
        }
        
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：AtCoder示例
        System.out.println("AtCoder示例: N=3, M=2, K=2");
        System.out.println("标准方法输出: " + countSequences(3, 2, 2));
        System.out.println("优化方法输出: " + countSequencesOptimized(3, 2, 2));
        System.out.println("期望: 4");
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例: N=4, M=3, K=2");
        System.out.println("标准方法输出: " + countSequences(4, 3, 2));
        System.out.println("优化方法输出: " + countSequencesOptimized(4, 3, 2));
        System.out.println();
        
        // 测试用例3
        System.out.println("测试用例: N=5, M=5, K=3");
        System.out.println("标准方法输出: " + countSequences(5, 5, 3));
        System.out.println("优化方法输出: " + countSequencesOptimized(5, 5, 3));
        System.out.println();
        
        // 性能测试
        long startTime = System.currentTimeMillis();
        int result1 = countSequencesOptimized(10, 10, 3);
        long endTime = System.currentTimeMillis();
        System.out.println("优化方法处理N=10,M=10,K=3耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果: " + result1);
        
        startTime = System.currentTimeMillis();
        int result2 = countSequences(10, 10, 3);
        endTime = System.currentTimeMillis();
        System.out.println("标准方法处理N=10,M=10,K=3耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果: " + result2);
    }
}