package class140;

// UVA 10090 Marbles
// 有两种盒子：第一种盒子可以装n1个弹珠，价格为c1；第二种盒子可以装n2个弹珠，价格为c2
// 需要装恰好n个弹珠，求最小总价格
// 如果无法恰好装n个弹珠，输出"failed"
// 测试链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031

/**
 * UVA 10090 Marbles
 * 
 * 问题描述：
 * 有两种盒子：第一种盒子可以装n1个弹珠，价格为c1；第二种盒子可以装n2个弹珠，价格为c2
 * 需要装恰好n个弹珠，求最小总价格
 * 如果无法恰好装n个弹珠，输出"failed"
 * 
 * 解题思路：
 * 1. 设第一种盒子用x个，第二种盒子用y个，则方程为：n1*x + n2*y = n
 * 2. 使用扩展欧几里得算法求解方程
 * 3. 判断方程是否有解：当n能被gcd(n1,n2)整除时有解
 * 4. 如果有解，根据通解公式求出所有可能的解
 * 5. 在所有解中寻找c1*x + c2*y最小的解
 * 
 * 数学原理：
 * 1. 裴蜀定理：方程n1*x + n2*y = n有整数解当且仅当gcd(n1,n2)能整除n
 * 2. 扩展欧几里得算法：求解n1*x + n2*y = gcd(n1,n2)的一组特解
 * 3. 通解公式：如果(x0,y0)是n1*x + n2*y = n的一组特解，那么通解为：
 *    x = x0 + (n2/gcd(n1,n2)) * t
 *    y = y0 - (n1/gcd(n1,n2)) * t
 *    t为任意整数
 * 
 * 时间复杂度：O(log(min(n1,n2)))，主要消耗在扩展欧几里得算法上
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. UVA 10090 Marbles
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031
 * 2. POJ 2142 The Balance
 *    链接：http://poj.org/problem?id=2142
 * 3. Codeforces 7C. Line
 *    链接：https://codeforces.com/problemset/problem/7/C
 */

import java.util.Scanner;

public class Code13_Uva10090_Marbles {
    
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
     * 求解弹珠问题
     * 
     * @param n 总弹珠数
     * @param n1 第一种盒子的容量
     * @param c1 第一种盒子的价格
     * @param n2 第二种盒子的容量
     * @param c2 第二种盒子的价格
     * @return 包含x和y的数组，如果没有解返回null
     */
    public static long[] solveMarbles(long n, long n1, long c1, long n2, long c2) {
        // 特殊情况处理
        if (n1 == 0 && n2 == 0) {
            return n == 0 ? new long[]{0, 0} : null;
        }
        
        // 使用扩展欧几里得算法
        long[] result = extendedGcd(n1, n2);
        long x0 = result[0];
        long y0 = result[1];
        long gcd = result[2];
        
        // 判断是否有解
        if (n % gcd != 0) {
            return null;
        }
        
        // 计算原方程的特解
        long factor = n / gcd;
        x0 *= factor;
        y0 *= factor;
        
        // 通解公式参数
        long k1 = n2 / gcd;
        long k2 = n1 / gcd;
        
        // 通解：x = x0 + k1 * t, y = y0 - k2 * t
        // 我们需要x >= 0, y >= 0，且最小化c1*x + c2*y
        
        // 计算t的范围
        // x >= 0 => x0 + k1*t >= 0 => t >= -x0/k1
        // y >= 0 => y0 - k2*t >= 0 => t <= y0/k2
        long tMin = (long) Math.ceil((double) -x0 / k1);
        long tMax = (long) Math.floor((double) y0 / k2);
        
        if (tMin > tMax) {
            return null; // 没有非负整数解
        }
        
        // 目标函数：cost = c1*x + c2*y = c1*(x0 + k1*t) + c2*(y0 - k2*t)
        // = (c1*x0 + c2*y0) + (c1*k1 - c2*k2)*t
        
        // 如果c1*k1 - c2*k2 > 0，则cost随t增加而增加，最小值在tMin处
        // 如果c1*k1 - c2*k2 < 0，则cost随t增加而减少，最小值在tMax处
        // 如果c1*k1 - c2*k2 == 0，则cost为常数
        
        long bestT;
        long coefficient = c1 * k1 - c2 * k2;
        
        if (coefficient > 0) {
            bestT = tMin;
        } else if (coefficient < 0) {
            bestT = tMax;
        } else {
            // 系数为0，任意t都可以，我们选择tMin
            bestT = tMin;
        }
        
        long x = x0 + k1 * bestT;
        long y = y0 - k2 * bestT;
        
        // 验证解是否非负
        if (x < 0 || y < 0) {
            return null;
        }
        
        return new long[]{x, y};
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            long n = scanner.nextLong();
            if (n == 0) {
                break;
            }
            
            long c1 = scanner.nextLong();
            long n1 = scanner.nextLong();
            long c2 = scanner.nextLong();
            long n2 = scanner.nextLong();
            
            long[] result = solveMarbles(n, n1, c1, n2, c2);
            if (result == null) {
                System.out.println("failed");
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
        // 测试用例1：UVA 10090示例
        System.out.println("测试用例1: n=100, n1=3, c1=5, n2=7, c2=8");
        long[] result1 = solveMarbles(100, 3, 5, 7, 8);
        if (result1 != null) {
            System.out.println("x=" + result1[0] + ", y=" + result1[1]);
            System.out.println("总价格: " + (5*result1[0] + 8*result1[1]));
            System.out.println("验证: 3*" + result1[0] + " + 7*" + result1[1] + " = " + 
                             (3*result1[0] + 7*result1[1]));
        }
        
        // 测试用例2：简单情况
        System.out.println("\\n测试用例2: n=10, n1=2, c1=3, n2=3, c2=4");
        long[] result2 = solveMarbles(10, 2, 3, 3, 4);
        if (result2 != null) {
            System.out.println("x=" + result2[0] + ", y=" + result2[1]);
            System.out.println("总价格: " + (3*result2[0] + 4*result2[1]));
        }
        
        // 测试用例3：无解情况
        System.out.println("\\n测试用例3: n=1, n1=2, c1=3, n2=4, c2=5");
        long[] result3 = solveMarbles(1, 2, 3, 4, 5);
        if (result3 == null) {
            System.out.println("No solution (expected)");
        }
        
        // 测试扩展欧几里得算法
        System.out.println("\\n测试扩展欧几里得算法:");
        long[] gcdResult = extendedGcd(12, 18);
        System.out.println("12*" + gcdResult[0] + " + 18*" + gcdResult[1] + " = " + gcdResult[2]);
    }
}