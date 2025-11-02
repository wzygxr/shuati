package class140;

// POJ 2142 The Balance
// 给定a、b、c，求解方程ax + by = c
// 要求找到一组解(x,y)，使得|x| + |y|最小
// 如果有多个解，选择x最小的解
// 测试链接：http://poj.org/problem?id=2142

/**
 * POJ 2142 The Balance
 * 
 * 问题描述：
 * 给定a、b、c，求解方程ax + by = c
 * 要求找到一组解(x,y)，使得|x| + |y|最小
 * 如果有多个解，选择x最小的解
 * 
 * 解题思路：
 * 1. 使用扩展欧几里得算法求解ax + by = gcd(a,b)的一组特解
 * 2. 判断方程是否有解：当c能被gcd(a,b)整除时有解
 * 3. 如果有解，将特解乘以c/gcd(a,b)得到原方程的一组特解
 * 4. 根据通解公式求出满足条件的解
 * 5. 在所有解中寻找|x| + |y|最小的解
 * 
 * 数学原理：
 * 1. 裴蜀定理：方程ax + by = c有整数解当且仅当gcd(a,b)能整除c
 * 2. 扩展欧几里得算法：求解ax + by = gcd(a,b)的一组特解
 * 3. 通解公式：如果(x0,y0)是ax + by = c的一组特解，那么通解为：
 *    x = x0 + (b/gcd(a,b)) * t
 *    y = y0 - (a/gcd(a,b)) * t
 *    t为任意整数
 * 
 * 时间复杂度：O(log(min(a,b)))，主要消耗在扩展欧几里得算法上
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. POJ 2142 The Balance
 *    链接：http://poj.org/problem?id=2142
 * 2. UVA 10090 Marbles
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031
 * 3. Codeforces 7C. Line
 *    链接：https://codeforces.com/problemset/problem/7/C
 */

import java.util.Scanner;

public class Code11_Poj2142_TheBalance {
    
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
     * 求解方程ax + by = c，并找到|x| + |y|最小的解
     * 
     * @param a 第一个系数
     * @param b 第二个系数
     * @param c 常数项
     * @return 包含x和y的数组，如果没有解返回null
     */
    public static long[] solveEquation(long a, long b, long c) {
        // 特殊情况处理
        if (a == 0 && b == 0) {
            return c == 0 ? new long[]{0, 0} : null;
        }
        
        // 使用扩展欧几里得算法
        long[] result = extendedGcd(a, b);
        long x0 = result[0];
        long y0 = result[1];
        long gcd = result[2];
        
        // 判断是否有解
        if (c % gcd != 0) {
            return null;
        }
        
        // 计算原方程的特解
        long factor = c / gcd;
        x0 *= factor;
        y0 *= factor;
        
        // 通解公式参数
        long k1 = b / gcd;
        long k2 = a / gcd;
        
        // 寻找|x| + |y|最小的解
        // 通解：x = x0 + k1 * t, y = y0 - k2 * t
        // 我们需要最小化 |x0 + k1*t| + |y0 - k2*t|
        
        // 使用数学方法找到最优的t值
        // 最优t应该在x0/k1和y0/k2附近
        long t1 = (long) Math.floor((double) -x0 / k1);
        long t2 = (long) Math.ceil((double) y0 / k2);
        
        // 检查几个候选t值
        long bestX = 0, bestY = 0;
        long minSum = Long.MAX_VALUE;
        
        // 检查t1-1, t1, t1+1, t2-1, t2, t2+1
        for (long t = t1 - 1; t <= t1 + 1; t++) {
            long x = x0 + k1 * t;
            long y = y0 - k2 * t;
            long sum = Math.abs(x) + Math.abs(y);
            if (sum < minSum || (sum == minSum && x < bestX)) {
                minSum = sum;
                bestX = x;
                bestY = y;
            }
        }
        
        for (long t = t2 - 1; t <= t2 + 1; t++) {
            long x = x0 + k1 * t;
            long y = y0 - k2 * t;
            long sum = Math.abs(x) + Math.abs(y);
            if (sum < minSum || (sum == minSum && x < bestX)) {
                minSum = sum;
                bestX = x;
                bestY = y;
            }
        }
        
        return new long[]{bestX, bestY};
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            long a = scanner.nextLong();
            long b = scanner.nextLong();
            long c = scanner.nextLong();
            
            if (a == 0 && b == 0 && c == 0) {
                break;
            }
            
            long[] result = solveEquation(a, b, c);
            if (result == null) {
                System.out.println("No solution");
            } else {
                System.out.println(result[0] + " " + result[1]);
            }
        }
        
        scanner.close();
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1：POJ 2142示例
        System.out.println("测试用例1: a=700, b=300, c=200");
        long[] result1 = solveEquation(700, 300, 200);
        if (result1 != null) {
            System.out.println("x=" + result1[0] + ", y=" + result1[1]);
            System.out.println("|x| + |y| = " + (Math.abs(result1[0]) + Math.abs(result1[1])));
        }
        
        // 测试用例2：简单情况
        System.out.println("\\n测试用例2: a=2, b=3, c=5");
        long[] result2 = solveEquation(2, 3, 5);
        if (result2 != null) {
            System.out.println("x=" + result2[0] + ", y=" + result2[1]);
            System.out.println("|x| + |y| = " + (Math.abs(result2[0]) + Math.abs(result2[1])));
        }
        
        // 测试用例3：无解情况
        System.out.println("\\n测试用例3: a=2, b=4, c=1");
        long[] result3 = solveEquation(2, 4, 1);
        if (result3 == null) {
            System.out.println("No solution (expected)");
        }
        
        // 测试扩展欧几里得算法
        System.out.println("\\n测试扩展欧几里得算法:");
        long[] gcdResult = extendedGcd(12, 18);
        System.out.println("12*" + gcdResult[0] + " + 18*" + gcdResult[1] + " = " + gcdResult[2]);
    }
}