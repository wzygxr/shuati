package class107;

import java.util.*;

/**
 * Codeforces 1415A - Prison Break
 * 
 * 问题描述：
 * 有一个 n×m 的监狱网格，每个囚犯都在自己的牢房里。
 * 当夜晚来临时，囚犯们可以开始移动到相邻的牢房（上、下、左、右）。
 * 有一个秘密隧道在位置 (r,c)，所有囚犯都想逃到那里。
 * 问题是要找到所有囚犯到达位置 (r,c) 所需的最少秒数。
 * 
 * 解题思路：
 * 这是一个简单的数学问题。对于网格中的任意位置 (i,j) 到目标位置 (r,c)，
 * 最短距离就是曼哈顿距离：|i-r| + |j-c|
 * 但是题目要求的是所有囚犯都能到达目标位置的最少时间，
 * 这意味着我们需要找到所有可能位置到目标位置的最大曼哈顿距离。
 * 
 * 在 n×m 的网格中，距离 (r,c) 最远的角落是四个角落之一：
 * 1. (1,1) - 左上角
 * 2. (1,m) - 右上角
 * 3. (n,1) - 左下角
 * 4. (n,m) - 右下角
 * 
 * 我们只需要计算这四个角落到目标位置的距离，然后取最大值。
 * 
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
public class Code03_PrisonBreak {
    
    /**
     * 计算监狱逃脱所需的最少时间
     * @param n 网格行数
     * @param m 网格列数
     * @param r 目标行位置（1-indexed）
     * @param c 目标列位置（1-indexed）
     * @return 最少时间
     */
    public static int minTimeToEscape(int n, int m, int r, int c) {
        // 计算四个角落到目标位置的曼哈顿距离，取最大值
        int dist1 = Math.abs(1 - r) + Math.abs(1 - c);      // 左上角 (1,1)
        int dist2 = Math.abs(1 - r) + Math.abs(m - c);      // 右上角 (1,m)
        int dist3 = Math.abs(n - r) + Math.abs(1 - c);      // 左下角 (n,1)
        int dist4 = Math.abs(n - r) + Math.abs(m - c);      // 右下角 (n,m)
        
        return Math.max(Math.max(dist1, dist2), Math.max(dist3, dist4));
    }
    
    /**
     * 另一种更直观的解法
     * 对于每个维度，找到最远的距离
     * @param n 网格行数
     * @param m 网格列数
     * @param r 目标行位置（1-indexed）
     * @param c 目标列位置（1-indexed）
     * @return 最少时间
     */
    public static int minTimeToEscapeV2(int n, int m, int r, int c) {
        // 在行方向上，最远距离是到第一行或最后一行的距离
        int rowDist = Math.max(r - 1, n - r);
        
        // 在列方向上，最远距离是到第一列或最后一列的距离
        int colDist = Math.max(c - 1, m - c);
        
        // 总的最远距离是两个方向距离之和
        return rowDist + colDist;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Codeforces 1415A - Prison Break ===");
        System.out.println("问题：计算所有囚犯逃到位置(r,c)所需的最少时间");
        
        // 测试用例1
        System.out.println("\n测试用例1:");
        int n1 = 10, m1 = 10, r1 = 1, c1 = 1;
        int result1 = minTimeToEscape(n1, m1, r1, c1);
        int result1_v2 = minTimeToEscapeV2(n1, m1, r1, c1);
        System.out.printf("网格大小: %d×%d, 目标位置: (%d,%d)\n", n1, m1, r1, c1);
        System.out.printf("方法1结果: %d\n", result1);
        System.out.printf("方法2结果: %d\n", result1_v2);
        
        // 测试用例2
        System.out.println("\n测试用例2:");
        int n2 = 5, m2 = 3, r2 = 2, c2 = 2;
        int result2 = minTimeToEscape(n2, m2, r2, c2);
        int result2_v2 = minTimeToEscapeV2(n2, m2, r2, c2);
        System.out.printf("网格大小: %d×%d, 目标位置: (%d,%d)\n", n2, m2, r2, c2);
        System.out.printf("方法1结果: %d\n", result2);
        System.out.printf("方法2结果: %d\n", result2_v2);
        
        // 测试用例3
        System.out.println("\n测试用例3:");
        int n3 = 3, m3 = 3, r3 = 2, c3 = 2;
        int result3 = minTimeToEscape(n3, m3, r3, c3);
        int result3_v2 = minTimeToEscapeV2(n3, m3, r3, c3);
        System.out.printf("网格大小: %d×%d, 目标位置: (%d,%d)\n", n3, m3, r3, c3);
        System.out.printf("方法1结果: %d\n", result3);
        System.out.printf("方法2结果: %d\n", result3_v2);
        
        // 交互式测试
        System.out.println("\n=== 交互式测试 ===");
        System.out.println("请输入网格大小和目标位置 (n m r c)，或输入0 0 0 0退出:");
        while (true) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            int r = scanner.nextInt();
            int c = scanner.nextInt();
            
            if (n == 0 && m == 0 && r == 0 && c == 0) {
                break;
            }
            
            int result = minTimeToEscape(n, m, r, c);
            System.out.printf("网格大小: %d×%d, 目标位置: (%d,%d), 最少时间: %d\n", n, m, r, c, result);
        }
        
        scanner.close();
        System.out.println("程序结束");
    }
}