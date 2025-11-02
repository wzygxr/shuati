/**
 * 青蛙的约会测试版本
 */
public class TestFrogDate {
    
    // 用于存储扩展欧几里得算法的结果
    static long[] result = new long[3]; // [gcd, x, y]
    
    /**
     * 扩展欧几里得算法 - 迭代实现
     * 求解 ax + by = gcd(a,b) 的一组整数解 x, y
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
     * 求解青蛙约会问题
     */
    public static long solveFrogDate(long x, long y, long m, long n, long L) {
        // 方程转化为 (m-n)*t ≡ (y-x) (mod L)
        long a = m - n;
        long b = y - x;
        
        // 处理负数情况
        a = (a % L + L) % L;
        b = (b % L + L) % L;
        
        // 特殊情况处理
        if (a == 0) {
            if (b == 0) {
                return 0; // 初始位置就相同
            } else {
                return -1; // 无法相遇
            }
        }
        
        // 求解线性同余方程
        return linearCongruence(a, b, L);
    }
    
    /**
     * 主方法 - 测试青蛙约会问题
     */
    public static void main(String[] args) {
        System.out.println("=== 青蛙的约会问题测试 ===\n");
        
        // 测试用例1
        long x1 = 1, y1 = 2, m1 = 3, n1 = 4, L1 = 5;
        long result1 = solveFrogDate(x1, y1, m1, n1, L1);
        System.out.printf("测试1: x=%d, y=%d, m=%d, n=%d, L=%d\n", x1, y1, m1, n1, L1);
        if (result1 != -1) {
            System.out.printf("结果: %d次后相遇\n", result1);
        } else {
            System.out.println("结果: 无法相遇");
        }
        
        // 测试用例2
        long x2 = 5, y2 = 10, m2 = 2, n2 = 3, L2 = 10;
        long result2 = solveFrogDate(x2, y2, m2, n2, L2);
        System.out.printf("\n测试2: x=%d, y=%d, m=%d, n=%d, L=%d\n", x2, y2, m2, n2, L2);
        if (result2 != -1) {
            System.out.printf("结果: %d次后相遇\n", result2);
        } else {
            System.out.println("结果: 无法相遇");
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}