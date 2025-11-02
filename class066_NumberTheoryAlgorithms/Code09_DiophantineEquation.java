package class139;

/**
 * 二元一次不定方程
 * 
 * 题目描述：
 * 给定不定方程 ax + by = c
 * 若该方程无整数解，输出-1。
 * 若该方程有整数解，且有正整数解，则输出其正整数解的数量，所有正整数解中x的最小值，
 * 所有正整数解中y的最小值，所有正整数解中x的最大值，所有正整数解中y的最大值。
 * 若该方程有整数解，但没有正整数解，输出0。
 * 
 * 解题思路：
 * 1. 根据裴蜀定理，方程ax + by = c有整数解当且仅当gcd(a,b) | c
 * 2. 如果有解，使用扩展欧几里得算法求出一组特解x0, y0
 * 3. 方程的所有解可以表示为：
 *    x = x0 + (b/gcd(a,b)) * t
 *    y = y0 - (a/gcd(a,b)) * t
 * 4. 通过不等式组求出t的取值范围，从而确定正整数解的存在性和个数
 * 
 * 时间复杂度：O(log(min(a,b)))
 * 空间复杂度：O(1)
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P5656
 */
public class Code09_DiophantineEquation {
    
    // 用于存储扩展欧几里得算法的结果
    static long[] result = new long[3]; // [gcd, x, y]
    
    /**
     * 扩展欧几里得算法 - 迭代实现
     * 求解 ax + by = gcd(a,b) 的一组整数解 x, y
     * 
     * 算法思路：
     * 1. 初始化 x0=1, y0=0, x1=0, y1=1
     * 2. 当 b≠0 时，计算 q=a/b, r=a%b
     * 3. 更新系数：x=x0-q*x1, y=y0-q*y1
     * 4. 更新变量：a=b, b=r, x0=x1, y0=y1, x1=x, y1=y
     * 5. 重复步骤2-4直到 b=0
     * 
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(1)
     * 
     * @param a 系数a
     * @param b 系数b
     * @param result 存储结果的数组，result[0]为gcd, result[1]为x, result[2]为y
     */
    public static void exgcdIterative(long a, long b, long[] result) {
        // 初始化系数
        long x0 = 1, y0 = 0;  // 初始解 (1, 0)
        long x1 = 0, y1 = 1;  // 初始解 (0, 1)
        
        // 迭代计算
        while (b != 0) {
            long q = a / b;  // 商
            long r = a % b;  // 余数
            
            // 更新系数
            long x = x0 - q * x1;
            long y = y0 - q * y1;
            
            // 更新变量
            a = b;
            b = r;
            x0 = x1;
            y0 = y1;
            x1 = x;
            y1 = y;
        }
        
        // 返回结果
        result[0] = a;   // gcd
        result[1] = x0;  // x
        result[2] = y0;  // y
    }
    
    /**
     * 求解二元一次不定方程的正整数解
     * 
     * @param a 系数a
     * @param b 系数b
     * @param c 等式右边
     * @return 解的结果数组，具体格式见函数内注释
     */
    public static long[] solveDiophantineEquation(long a, long b, long c) {
        // 使用扩展欧几里得算法求解 ax + by = gcd(a,b)
        exgcdIterative(a, b, result);
        
        long gcd = result[0];
        long x0 = result[1];
        long y0 = result[2];
        
        // 判断方程是否有解
        if (c % gcd != 0) {
            // 无整数解
            return new long[]{-1};
        }
        
        // 将特解调整为原方程的解
        x0 = x0 * (c / gcd);
        y0 = y0 * (c / gcd);
        
        // 计算系数
        long a1 = a / gcd;
        long b1 = b / gcd;
        
        // 如果a1或b1为0，特殊处理
        if (a1 == 0 || b1 == 0) {
            // 有整数解但没有正整数解
            return new long[]{0};
        }
        
        // 通解形式：
        // x = x0 + b1 * t
        // y = y0 - a1 * t
        // 其中t为任意整数
        
        // 求正整数解的t的范围
        // x > 0 => x0 + b1 * t > 0 => t > -x0/b1
        // y > 0 => y0 - a1 * t > 0 => t < y0/a1
        
        // 计算t的上下界
        long minT = Long.MIN_VALUE, maxT = Long.MAX_VALUE;
        
        if (b1 > 0) {
            // t > -x0/b1
            long tempMin = (long) Math.ceil((-x0) * 1.0 / b1);
            if (-x0 % b1 == 0 && -x0 >= 0) {
                tempMin = (-x0) / b1 + 1;
            }
            minT = Math.max(minT, tempMin);
        } else if (b1 < 0) {
            // t < -x0/b1
            long tempMax = (long) Math.floor((-x0) * 1.0 / b1);
            if (-x0 % b1 == 0 && -x0 <= 0) {
                tempMax = (-x0) / b1 - 1;
            }
            maxT = Math.min(maxT, tempMax);
        } else {
            // b1 == 0，需要x0 > 0才有解
            if (x0 <= 0) {
                return new long[]{0};
            }
        }
        
        if (a1 > 0) {
            // t < y0/a1
            long tempMax = (long) Math.floor(y0 * 1.0 / a1);
            if (y0 % a1 == 0 && y0 >= 0) {
                tempMax = y0 / a1 - 1;
            }
            maxT = Math.min(maxT, tempMax);
        } else if (a1 < 0) {
            // t > y0/a1
            long tempMin = (long) Math.ceil(y0 * 1.0 / a1);
            if (y0 % a1 == 0 && y0 <= 0) {
                tempMin = y0 / a1 + 1;
            }
            minT = Math.max(minT, tempMin);
        } else {
            // a1 == 0，需要y0 > 0才有解
            if (y0 <= 0) {
                return new long[]{0};
            }
        }
        
        // 如果minT > maxT，则无正整数解
        if (minT > maxT) {
            // 有整数解但没有正整数解
            return new long[]{0};
        }
        
        // 计算正整数解的个数
        long count = maxT - minT + 1;
        
        // 计算x和y的最小值和最大值
        long minX = x0 + b1 * minT;
        long maxX = x0 + b1 * maxT;
        long minY = y0 - a1 * maxT;  // 注意这里是maxT，因为y随t增大而减小
        long maxY = y0 - a1 * minT;  // 注意这里是minT，因为y随t增大而减小
        
        // 返回结果：正整数解的数量，x的最小值，y的最小值，x的最大值，y的最大值
        return new long[]{count, minX, minY, maxX, maxY};
    }
    
    /**
     * 主方法 - 测试二元一次不定方程问题
     */
    public static void main(String[] args) {
        System.out.println("=== 二元一次不定方程问题测试 ===\n");
        
        // 测试用例1：有正整数解
        long a1 = 6, b1 = 9, c1 = 15;
        long[] result1 = solveDiophantineEquation(a1, b1, c1);
        System.out.printf("测试1: %dx + %dy = %d\n", a1, b1, c1);
        if (result1[0] == -1) {
            System.out.println("结果: 无整数解");
        } else if (result1[0] == 0) {
            System.out.println("结果: 有整数解但无正整数解");
        } else {
            System.out.printf("结果: 正整数解数量=%d, x最小值=%d, y最小值=%d, x最大值=%d, y最大值=%d\n", 
                             result1[0], result1[1], result1[2], result1[3], result1[4]);
        }
        
        // 测试用例2：无整数解
        long a2 = 4, b2 = 6, c2 = 9;
        long[] result2 = solveDiophantineEquation(a2, b2, c2);
        System.out.printf("\n测试2: %dx + %dy = %d\n", a2, b2, c2);
        if (result2[0] == -1) {
            System.out.println("结果: 无整数解");
        } else if (result2[0] == 0) {
            System.out.println("结果: 有整数解但无正整数解");
        } else {
            System.out.printf("结果: 正整数解数量=%d, x最小值=%d, y最小值=%d, x最大值=%d, y最大值=%d\n", 
                             result2[0], result2[1], result2[2], result2[3], result2[4]);
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}