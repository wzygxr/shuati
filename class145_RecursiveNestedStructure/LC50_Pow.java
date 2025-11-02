package class039;

// LeetCode 50. Pow(x, n) (快速幂递归)
// 测试链接 : https://leetcode.cn/problems/powx-n/

public class LC50_Pow {
    public double myPow(double x, int n) {
        // 处理负指数
        long N = n;
        if (N < 0) {
            x = 1 / x;
            N = -N;
        }
        
        return fastPow(x, N);
    }
    
    private double fastPow(double x, long n) {
        // 基础情况
        if (n == 0) {
            return 1.0;
        }
        
        // 递归计算
        double half = fastPow(x, n / 2);
        
        if (n % 2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }
    
    // 测试用例
    public static void main(String[] args) {
        LC50_Pow solution = new LC50_Pow();
        
        // 测试用例1
        double x1 = 2.00000;
        int n1 = 10;
        System.out.println("输入: x = " + x1 + ", n = " + n1);
        System.out.println("输出: " + solution.myPow(x1, n1));
        System.out.println("期望: 1024.00000\n");
        
        // 测试用例2
        double x2 = 2.10000;
        int n2 = 3;
        System.out.println("输入: x = " + x2 + ", n = " + n2);
        System.out.println("输出: " + solution.myPow(x2, n2));
        System.out.println("期望: 9.26100\n");
        
        // 测试用例3
        double x3 = 2.00000;
        int n3 = -2;
        System.out.println("输入: x = " + x3 + ", n = " + n3);
        System.out.println("输出: " + solution.myPow(x3, n3));
        System.out.println("期望: 0.25000\n");
    }
}