package class062;

import java.util.*;

// 骑士拨号器
// 国际象棋中的骑士可以按下图所示进行移动：
// 这一次，我们将 "骑士" 放在电话拨号盘的任意数字键（如上图所示）上，接下来，骑士将会跳 N-1 步。每一步必须是从一个数字键跳到另一个数字键。
// 每当它落在一个键上（包括骑士的初始位置），都会拨出键所对应的数字，总共拨出 N 位数字。
// 你能用这种方式拨出多少个不同的号码？
// 因为答案可能很大，所以输出答案模 10^9 + 7。
// 测试链接 : https://leetcode.cn/problems/knight-dialer/
// 
// 算法思路：
// 使用动态规划 + BFS思想。dp[i][j]表示长度为i且以数字j结尾的号码数量。
// 根据骑士的移动规则构建转移图，然后进行动态规划计算。
// 
// 时间复杂度：O(N)，其中N是号码长度
// 空间复杂度：O(1)，只使用常数空间
// 
// 工程化考量：
// 1. 转移图构建：预计算每个数字可以跳转到哪些数字
// 2. 动态规划优化：使用滚动数组减少空间复杂度
// 3. 模运算：处理大数取模问题
// 4. 边界情况：号码长度为1的特殊处理
public class Code29_MinimumKnightMoves {

    private static final int MOD = 1000000007;
    
    // 骑士的移动规则：每个数字可以跳转到哪些数字
    private static final int[][] MOVES = {
        {4, 6},     // 0 -> 4, 6
        {6, 8},     // 1 -> 6, 8
        {7, 9},     // 2 -> 7, 9
        {4, 8},     // 3 -> 4, 8
        {0, 3, 9}, // 4 -> 0, 3, 9
        {},         // 5 -> 无
        {0, 1, 7}, // 6 -> 0, 1, 7
        {2, 6},     // 7 -> 2, 6
        {1, 3},     // 8 -> 1, 3
        {2, 4}      // 9 -> 2, 4
    };
    
    public static int knightDialer(int n) {
        if (n == 1) return 10;
        
        // 动态规划数组：dp[i]表示以数字i结尾的号码数量
        long[] dp = new long[10];
        // 初始化：长度为1的号码，每个数字都可以作为起点
        Arrays.fill(dp, 1);
        
        for (int step = 2; step <= n; step++) {
            long[] newDp = new long[10];
            
            for (int i = 0; i < 10; i++) {
                for (int next : MOVES[i]) {
                    newDp[next] = (newDp[next] + dp[i]) % MOD;
                }
            }
            
            dp = newDp;
        }
        
        long result = 0;
        for (long count : dp) {
            result = (result + count) % MOD;
        }
        
        return (int) result;
    }
    
    // 优化版本：使用矩阵快速幂，将时间复杂度优化到O(logN)
    public static int knightDialerFast(int n) {
        if (n == 1) return 10;
        
        // 构建转移矩阵（10x10）
        long[][] transition = new long[10][10];
        for (int i = 0; i < 10; i++) {
            for (int next : MOVES[i]) {
                transition[i][next] = 1;
            }
        }
        
        // 初始向量：所有数字都可以作为起点
        long[] initial = new long[10];
        Arrays.fill(initial, 1);
        
        // 计算转移矩阵的(n-1)次幂
        long[][] matrixPower = matrixPower(transition, n - 1);
        
        // 计算结果
        long result = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                result = (result + initial[j] * matrixPower[j][i]) % MOD;
            }
        }
        
        return (int) result;
    }
    
    // 矩阵快速幂算法
    private static long[][] matrixPower(long[][] matrix, int power) {
        int n = matrix.length;
        long[][] result = new long[n][n];
        
        // 初始化为单位矩阵
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }
        
        while (power > 0) {
            if ((power & 1) == 1) {
                result = matrixMultiply(result, matrix);
            }
            matrix = matrixMultiply(matrix, matrix);
            power >>= 1;
        }
        
        return result;
    }
    
    // 矩阵乘法（带模运算）
    private static long[][] matrixMultiply(long[][] a, long[][] b) {
        int n = a.length;
        long[][] result = new long[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] = (result[i][j] + a[i][k] * b[k][j]) % MOD;
                }
            }
        }
        
        return result;
    }
    
    // BFS版本：用于理解和验证，不适用于大规模数据
    public static int knightDialerBFS(int n) {
        if (n == 1) return 10;
        
        Queue<Integer> queue = new LinkedList<>();
        // 初始化：所有数字都可以作为起点
        for (int i = 0; i < 10; i++) {
            queue.offer(i);
        }
        
        int steps = 1;
        long count = 10;
        
        while (steps < n && !queue.isEmpty()) {
            int size = queue.size();
            long newCount = 0;
            
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                
                for (int next : MOVES[current]) {
                    queue.offer(next);
                    newCount++;
                }
            }
            
            count = newCount % MOD;
            steps++;
        }
        
        return (int) count;
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：n=1
        System.out.println("测试用例1 - 号码数量: " + knightDialer(1)); // 期望输出: 10
        
        // 测试用例2：n=2
        System.out.println("测试用例2 - 号码数量: " + knightDialer(2)); // 期望输出: 20
        
        // 测试用例3：n=3
        System.out.println("测试用例3 - 号码数量: " + knightDialer(3)); // 期望输出: 46
        
        // 测试用例4：n=4
        System.out.println("测试用例4 - 号码数量: " + knightDialer(4)); // 期望输出: 104
        
        // 测试用例5：大数测试
        System.out.println("测试用例5 - 号码数量: " + knightDialer(5000)); // 快速验证
    }
}