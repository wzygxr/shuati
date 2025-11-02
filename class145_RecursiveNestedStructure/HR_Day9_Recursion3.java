package class039;

// HackerRank Day 9: Recursion 3 (阶乘递归)
// 测试链接 : https://www.hackerrank.com/challenges/30-recursion/problem

public class HR_Day9_Recursion3 {
    public int factorial(int n) {
        // 基础情况
        if (n <= 1) {
            return 1;
        }
        
        // 递归情况
        return n * factorial(n - 1);
    }
    
    // 测试用例
    public static void main(String[] args) {
        HR_Day9_Recursion3 solution = new HR_Day9_Recursion3();
        
        // 测试用例1
        int n1 = 3;
        System.out.println("输入: " + n1);
        System.out.println("输出: " + solution.factorial(n1));
        System.out.println("期望: 6\n");
        
        // 测试用例2
        int n2 = 5;
        System.out.println("输入: " + n2);
        System.out.println("输出: " + solution.factorial(n2));
        System.out.println("期望: 120\n");
    }
}