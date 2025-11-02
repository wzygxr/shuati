package class140;

// Codeforces 7C. Line
// 给定直线方程Ax + By + C = 0，求直线上任意一个整数点(x,y)
// 如果不存在整数点，输出-1
// 测试链接：https://codeforces.com/problemset/problem/7/C

/**
 * Codeforces 7C. Line
 * 
 * 问题描述：
 * 给定直线方程Ax + By + C = 0，求直线上任意一个整数点(x,y)
 * 如果不存在整数点，输出-1
 * 
 * 解题思路：
 * 1. 将直线方程转换为标准形式：Ax + By = -C
 * 2. 使用扩展欧几里得算法求解方程Ax + By = gcd(A,B)的一组特解
 * 3. 判断方程是否有整数解：当-C能被gcd(A,B)整除时有解
 * 4. 如果有解，将特解乘以(-C)/gcd(A,B)得到原方程的一组特解
 * 
 * 数学原理：
 * 1. 裴蜀定理：方程Ax + By = -C有整数解当且仅当gcd(A,B)能整除-C
 * 2. 扩展欧几里得算法：求解Ax + By = gcd(A,B)的一组特解
 * 3. 通解公式：如果(x0,y0)是Ax + By = -C的一组特解，那么通解为：
 *    x = x0 + (B/gcd(A,B)) * t
 *    y = y0 - (A/gcd(A,B)) * t
 *    t为任意整数
 * 
 * 时间复杂度：O(log(min(A,B)))，主要消耗在扩展欧几里得算法上
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. Codeforces 7C. Line
 *    链接：https://codeforces.com/problemset/problem/7/C
 * 2. POJ 2142 The Balance
 *    链接：http://poj.org/problem?id=2142
 * 3. UVA 10090 Marbles
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031
 */

import java.util.Scanner;

public class Code12_Codeforces7C_Line {
    
    /**
     * 扩展欧几里得算法，求解ax + by = gcd(a,b)的一组特解
     * 
     * @param a 第一个系数
     * @param b 第二个系数
     * @return 包含x, y, gcd的数组
     */
    private static long[] extendedGcd(long a, long b) {
        if (b == 0) {
            return new long[]{1, 0, a};
        }
        long[] result = extendedGcd(b, a % b);
        long x = result[0];
        long y = result[1];
        long gcd = result[2];
        return new long[]{y, x - (a / b) * y, gcd};
    }
    
    /**
     * 求解直线方程Ax + By + C = 0的整数解
     * 
     * @param A 系数A
     * @param B 系数B
     * @param C 常数C
     * @return 包含x和y的数组，如果没有整数解返回null
     */
    public static long[] solveLineEquation(long A, long B, long C) {
        // 特殊情况处理
        if (A == 0 && B == 0) {
            return C == 0 ? new long[]{0, 0} : null;
        }
        
        // 将方程转换为标准形式：Ax + By = -C
        long target = -C;
        
        // 处理A或B为0的情况
        if (A == 0) {
            if (target % B == 0) {
                // 任意x都可以，y = -C/B
                return new long[]{0, target / B};
            } else {
                return null;
            }
        }
        
        if (B == 0) {
            if (target % A == 0) {
                // 任意y都可以，x = -C/A
                return new long[]{target / A, 0};
            } else {
                return null;
            }
        }
        
        // 使用扩展欧几里得算法
        long[] result = extendedGcd(Math.abs(A), Math.abs(B));
        long x0 = result[0];
        long y0 = result[1];
        long gcd = result[2];
        
        // 判断是否有解
        if (target % gcd != 0) {
            return null;
        }
        
        // 计算原方程的特解
        long factor = target / gcd;
        x0 *= factor;
        y0 *= factor;
        
        // 处理A或B为负数的情况
        if (A < 0) {
            x0 = -x0;
        }
        if (B < 0) {
            y0 = -y0;
        }
        
        return new long[]{x0, y0};
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        long A = scanner.nextLong();
        long B = scanner.nextLong();
        long C = scanner.nextLong();
        
        long[] result = solveLineEquation(A, B, C);
        if (result == null) {
            System.out.println(-1);
        } else {
            System.out.println(result[0] + " " + result[1]);
        }
        
        scanner.close();
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1：Codeforces 7C示例
        System.out.println("测试用例1: A=2, B=5, C=3");
        long[] result1 = solveLineEquation(2, 5, 3);
        if (result1 != null) {
            System.out.println("x=" + result1[0] + ", y=" + result1[1]);
            // 验证：2*x + 5*y + 3 = 0
            System.out.println("验证: 2*" + result1[0] + " + 5*" + result1[1] + " + 3 = " + 
                             (2*result1[0] + 5*result1[1] + 3));
        }
        
        // 测试用例2：简单情况
        System.out.println("\\n测试用例2: A=1, B=1, C=1");
        long[] result2 = solveLineEquation(1, 1, 1);
        if (result2 != null) {
            System.out.println("x=" + result2[0] + ", y=" + result2[1]);
            System.out.println("验证: 1*" + result2[0] + " + 1*" + result2[1] + " + 1 = " + 
                             (result2[0] + result2[1] + 1));
        }
        
        // 测试用例3：无解情况
        System.out.println("\\n测试用例3: A=2, B=4, C=1");
        long[] result3 = solveLineEquation(2, 4, 1);
        if (result3 == null) {
            System.out.println("No solution (expected)");
        }
        
        // 测试用例4：A为0的情况
        System.out.println("\\n测试用例4: A=0, B=3, C=6");
        long[] result4 = solveLineEquation(0, 3, 6);
        if (result4 != null) {
            System.out.println("x=" + result4[0] + ", y=" + result4[1]);
            System.out.println("验证: 0*" + result4[0] + " + 3*" + result4[1] + " + 6 = " + 
                             (3*result4[1] + 6));
        }
        
        // 测试用例5：B为0的情况
        System.out.println("\\n测试用例5: A=4, B=0, C=8");
        long[] result5 = solveLineEquation(4, 0, 8);
        if (result5 != null) {
            System.out.println("x=" + result5[0] + ", y=" + result5[1]);
            System.out.println("验证: 4*" + result5[0] + " + 0*" + result5[1] + " + 8 = " + 
                             (4*result5[0] + 8));
        }
        
        // 测试扩展欧几里得算法
        System.out.println("\\n测试扩展欧几里得算法:");
        long[] gcdResult = extendedGcd(12, 18);
        System.out.println("12*" + gcdResult[0] + " + 18*" + gcdResult[1] + " = " + gcdResult[2]);
    }
}