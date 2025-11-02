package class141;

/*
 * LeetCode 372. 超级次方
 * 链接：https://leetcode.cn/problems/super-pow/
 * 题目大意：计算 a^b mod 1337，其中b是一个非常大的数，用数组表示
 * 
 * 算法思路：
 * 这道题可以使用快速幂和欧拉定理来解决。
 * 根据欧拉定理：如果a和m互质，则 a^φ(m) ≡ 1 (mod m)
 * 其中φ(m)是欧拉函数。
 * 
 * 对于这道题，m=1337=7×191，φ(1337)=6×190=1140
 * 所以 a^b ≡ a^(b mod 1140) (mod 1337)，当a和1337互质时
 * 
 * 但我们还需要处理a和1337不互质的情况。
 * 
 * 另一种思路是使用中国剩余定理：
 * 由于1337=7×191，且gcd(7,191)=1，我们可以分别计算：
 * x1 = a^b mod 7
 * x2 = a^b mod 191
 * 然后使用中国剩余定理合并结果。
 * 
 * 算法原理：
 * 这道题展示了中国剩余定理在大数运算中的应用，通过分解模数来简化计算。
 * 
 * 时间复杂度：O(n)，其中n是数组b的长度
 * 空间复杂度：O(1)
 * 
 * 适用场景：
 * 1. 大数幂运算
 * 2. 密码学中的模幂运算
 * 
 * 注意事项：
 * 1. 需要处理大数运算
 * 2. 需要考虑a和模数是否互质
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理边界情况
 * 3. 大数处理：使用模运算防止溢出
 * 
 * 与其他算法的关联：
 * 1. 快速幂：核心算法
 * 2. 欧拉定理：用于优化指数
 * 3. 中国剩余定理：可选的解法
 * 
 * 实际应用：
 * 1. RSA加密算法中的模幂运算
 * 2. 大数计算
 * 
 * 相关题目：
 * 1. LeetCode 50. Pow(x, n)
 *    链接：https://leetcode.cn/problems/powx-n/
 *    题目大意：实现pow(x, n)
 * 
 * 2. LeetCode 372. Super Pow
 *    链接：https://leetcode.cn/problems/super-pow/
 *    题目大意：与本题相同
 */

public class LeetCode372_SuperPow_Java {

    private static final int MOD = 1337;

    // 快速幂
    public static int quickPower(int a, int b, int mod) {
        a %= mod;
        int result = 1;
        while (b > 0) {
            if ((b & 1) == 1) {
                result = (int) ((1L * result * a) % mod);
            }
            a = (int) ((1L * a * a) % mod);
            b >>= 1;
        }
        return result;
    }

    // 扩展欧几里得算法
    public static int[] exgcd(int a, int b) {
        if (b == 0) {
            return new int[]{a, 1, 0};
        } else {
            int[] result = exgcd(b, a % b);
            int d = result[0];
            int x = result[1];
            int y = result[2];
            result[1] = y;
            result[2] = x - (a / b) * y;
            return result;
        }
    }

    // 求逆元
    public static int modInverse(int a, int mod) {
        int[] result = exgcd(a, mod);
        int d = result[0];
        int x = result[1];
        if (d != 1) {
            return -1; // 逆元不存在
        } else {
            return (x % mod + mod) % mod;
        }
    }

    // 龟速乘法，防止溢出
    public static int multiply(int a, int b, int mod) {
        a = (a % mod + mod) % mod;
        b = (b % mod + mod) % mod;
        int ans = 0;
        while (b != 0) {
            if ((b & 1) != 0) {
                ans = (ans + a) % mod;
            }
            a = (a + a) % mod;
            b >>= 1;
        }
        return ans;
    }

    // 中国剩余定理
    public static int crt(int r1, int r2) {
        // 1337 = 7 * 191
        int m1 = 7, m2 = 191;
        int M = m1 * m2;
        
        // M1 = M / m1 = 191
        int M1 = M / m1;
        // M2 = M / m2 = 7
        int M2 = M / m2;
        
        // 求逆元
        int inv_M1 = modInverse(M1, m1);
        int inv_M2 = modInverse(M2, m2);
        
        // 计算解
        int x = (multiply(r1, multiply(M1, inv_M1, M), M) +
                 multiply(r2, multiply(M2, inv_M2, M), M)) % M;
        
        return x;
    }

    public static int superPow(int a, int[] b) {
        if (a == 0) return 0;
        
        // 使用中国剩余定理分解
        // 1337 = 7 * 191
        int a1 = a % 7;
        int a2 = a % 191;
        
        // 计算 b mod φ(7) = b mod 6
        int b1 = 0;
        for (int i = 0; i < b.length; i++) {
            b1 = (b1 * 10 + b[i]) % 6;
        }
        
        // 计算 b mod φ(191) = b mod 190
        int b2 = 0;
        for (int i = 0; i < b.length; i++) {
            b2 = (b2 * 10 + b[i]) % 190;
        }
        
        // 分别计算
        int r1 = quickPower(a1, b1, 7);
        int r2 = quickPower(a2, b2, 191);
        
        // 使用中国剩余定理合并结果
        return crt(r1, r2);
    }

    public static void main(String[] args) {
        // 测试用例
        int a1 = 2;
        int[] b1 = {3};
        System.out.println(superPow(a1, b1)); // 期望输出: 8
        
        int a2 = 2;
        int[] b2 = {1, 0};
        System.out.println(superPow(a2, b2)); // 期望输出: 1024
        
        int a3 = 1;
        int[] b3 = {4, 3, 3, 8, 5, 2};
        System.out.println(superPow(a3, b3)); // 期望输出: 1
        
        int a4 = 2147483647;
        int[] b4 = {2, 0, 0};
        System.out.println(superPow(a4, b4)); // 期望输出: 1198
    }
}