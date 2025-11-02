package class139;

/**
 * C Looooops
 * 
 * 题目描述：
 * 考虑以下C语言伪代码：
 * for (variable = A; variable != B; variable += C)
 *   statement;
 * 
 * 由于变量是k位无符号整数，其取值范围为[0, 2^k-1]，当变量超过范围时会回绕。
 * 例如k=3时，变量取值为0,1,2,3,4,5,6,7，当变量为7时再加1会变成0。
 * 
 * 任务是计算这个循环执行多少次语句后结束（即variable == B）。
 * 如果循环永远不会结束，输出"FOREVER"。
 * 
 * 解题思路：
 * 1. 循环执行t次后，variable的值为(A + C*t) % 2^k
 * 2. 当variable == B时，有(A + C*t) % 2^k = B
 * 3. 转化为：(A + C*t) ≡ B (mod 2^k)
 * 4. 移项得：C*t ≡ (B-A) (mod 2^k)
 * 5. 这是一个线性同余方程，可以用扩展欧几里得算法求解
 * 
 * 时间复杂度：O(log(min(C, 2^k)))
 * 空间复杂度：O(1)
 * 
 * 测试链接：http://poj.org/problem?id=2115
 */
public class Code08_CLooooops {
    
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
     * 求解线性同余方程
     * 求解 ax ≡ b (mod m) 的最小非负整数解 x
     * 
     * 算法思路：
     * 1. 将同余方程转换为不定方程：ax + my = b
     * 2. 使用扩展欧几里得算法求解 ax + my = gcd(a,m)
     * 3. 如果 b 不能被 gcd(a,m) 整除，则方程无解
     * 4. 否则，将解乘以 b/gcd(a,m) 得到特解
     * 5. 调整为最小非负整数解
     * 
     * 时间复杂度：O(log(min(a,m)))
     * 空间复杂度：O(1)
     * 
     * @param a 系数a
     * @param b 等式右边
     * @param m 模数
     * @return 方程的最小非负整数解，如果无解则返回 -1
     */
    public static long linearCongruence(long a, long b, long m) {
        exgcdIterative(a, m, result);
        
        long gcd = result[0];
        long x = result[1];
        
        // 如果b不能被gcd整除，则方程无解
        if (b % gcd != 0) {
            return -1;
        }
        
        // 计算解
        long mod = m / gcd;
        long sol = ((x * (b / gcd)) % mod + mod) % mod;
        return sol;
    }
    
    /**
     * 求解C Looooops问题
     * 
     * @param A 初始值
     * @param B 目标值
     * @param C 步长
     * @param k 位数
     * @return 循环执行次数，如果无法结束则返回-1
     */
    public static long solveCLooooops(long A, long B, long C, int k) {
        // 计算模数
        long mod = 1L << k; // 2^k
        
        // 方程转化为 C*t ≡ (B-A) (mod 2^k)
        long a = C;
        long b = (B - A) % mod;
        // 处理负数情况
        b = (b + mod) % mod;
        
        // 特殊情况处理
        if (a == 0) {
            if (b == 0) {
                return 0; // 初始就满足条件
            } else {
                return -1; // 无法满足条件
            }
        }
        
        // 求解线性同余方程
        return linearCongruence(a, b, mod);
    }
    
    /**
     * 主方法 - 测试C Looooops问题
     */
    public static void main(String[] args) {
        System.out.println("=== C Looooops问题测试 ===\n");
        
        // 测试用例1
        long A1 = 3, B1 = 3, C1 = 2, k1 = 16;
        long result1 = solveCLooooops(A1, B1, C1, (int)k1);
        System.out.printf("测试1: A=%d, B=%d, C=%d, k=%d\n", A1, B1, C1, k1);
        if (result1 != -1) {
            System.out.printf("结果: 循环执行%d次后结束\n", result1);
        } else {
            System.out.println("结果: 循环永远不会结束(FOREVER)");
        }
        
        // 测试用例2
        long A2 = 1, B2 = 3, C2 = 2, k2 = 16;
        long result2 = solveCLooooops(A2, B2, C2, (int)k2);
        System.out.printf("\n测试2: A=%d, B=%d, C=%d, k=%d\n", A2, B2, C2, k2);
        if (result2 != -1) {
            System.out.printf("结果: 循环执行%d次后结束\n", result2);
        } else {
            System.out.println("结果: 循环永远不会结束(FOREVER)");
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}