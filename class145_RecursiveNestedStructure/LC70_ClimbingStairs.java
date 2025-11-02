package class039;

// LeetCode 70. Climbing Stairs (爬楼梯递归)
// 测试链接 : https://leetcode.cn/problems/climbing-stairs/

public class LC70_ClimbingStairs {
    public int climbStairs(int n) {
        // 使用记忆化递归
        int[] memo = new int[n + 1];
        return climbStairsHelper(n, memo);
    }
    
    private int climbStairsHelper(int n, int[] memo) {
        // 基础情况
        if (n <= 2) {
            return n;
        }
        
        // 如果已经计算过，直接返回
        if (memo[n] != 0) {
            return memo[n];
        }
        
        // 递归计算并存储结果
        memo[n] = climbStairsHelper(n - 1, memo) + climbStairsHelper(n - 2, memo);
        return memo[n];
    }
    
    // 测试用例
    public static void main(String[] args) {
        LC70_ClimbingStairs solution = new LC70_ClimbingStairs();
        
        // 测试用例1
        int n1 = 2;
        System.out.println("输入: " + n1);
        System.out.println("输出: " + solution.climbStairs(n1));
        System.out.println("期望: 2\n");
        
        // 测试用例2
        int n2 = 3;
        System.out.println("输入: " + n2);
        System.out.println("输出: " + solution.climbStairs(n2));
        System.out.println("期望: 3\n");
    }
}