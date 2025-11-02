package class098;

// Codeforces 166E Tetrahedron
// 题目链接: https://codeforces.com/problemset/problem/166/E
// 题目大意: 一个四面体有4个顶点A, B, C, D。一只蚂蚁从顶点D开始，
// 每次沿着棱移动到另一个顶点。求经过n步后回到顶点D的方案数。
// 解法: 使用矩阵快速幂
// 时间复杂度: O(logn)
// 空间复杂度: O(1)

import java.util.Scanner;

public class Code09_Tetrahedron {
    
    static final int MOD = 1000000007;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println(solve(n));
    }
    
    // 使用矩阵快速幂解决问题
    public static int solve(int n) {
        // 初始状态：[在D点的方案数, 不在D点的方案数]
        // 初始时在D点，所以是[1, 0]
        long[] start = {1, 0};
        
        // 转移矩阵：
        // 从D点只能到非D点，有3种选择
        // 从非D点可以到D点(1种选择)或非D点(2种选择)
        // [D点方案数]   [0 1] [D点方案数]
        // [非D点方案数] = [3 2] [非D点方案数]
        int[][] base = {
            {0, 1},
            {3, 2}
        };
        
        long[] result = multiply(start, power(base, n));
        return (int) result[0];
    }
    
    // 向量与矩阵相乘
    public static long[] multiply(long[] a, int[][] b) {
        int n = a.length;
        int m = b[0].length;
        long[] ans = new long[m];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                ans[j] = (ans[j] + (long) a[i] * b[i][j]) % MOD;
            }
        }
        return ans;
    }
    
    // 矩阵相乘
    public static int[][] multiply(int[][] a, int[][] b) {
        int n = a.length;
        int m = b[0].length;
        int k = a[0].length;
        int[][] ans = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int c = 0; c < k; c++) {
                    ans[i][j] = (int) (((long) a[i][c] * b[c][j] + ans[i][j]) % MOD);
                }
            }
        }
        return ans;
    }
    
    // 矩阵快速幂
    public static int[][] power(int[][] m, int p) {
        int n = m.length;
        int[][] ans = new int[n][n];
        for (int i = 0; i < n; i++) {
            ans[i][i] = 1;
        }
        for (; p != 0; p >>= 1) {
            if ((p & 1) != 0) {
                ans = multiply(ans, m);
            }
            m = multiply(m, m);
        }
        return ans;
    }
}