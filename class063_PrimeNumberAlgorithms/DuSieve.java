package number_theory;

import java.util.*;

/**
 * 杜教筛算法实现
 * 
 * 算法简介:
 * 杜教筛是一种用于计算积性函数前缀和的算法，由杜教发明。
 * 它可以在亚线性时间复杂度内计算积性函数的前缀和。
 * 
 * 适用场景:
 * 1. 计算积性函数f(x)的前缀和S(n) = Σ(i=1 to n) f(i)
 * 2. 存在另一个积性函数g(x)，使得f*g的前缀和容易计算
 * 3. f的前缀和可以通过卷积关系递推得到
 * 
 * 核心思想:
 * 1. 利用狄利克雷卷积的性质：S(n) = Σ(d|n) g(d) * S(n/d)
 * 2. 通过莫比乌斯函数等构造辅助函数
 * 3. 结合记忆化搜索优化递归计算
 * 
 * 时间复杂度: O(n^(2/3))
 * 空间复杂度: O(n^(2/3))
 */
public class DuSieve {
    private Map<Long, Long> memo; // 记忆化缓存
    private static final long MOD = 1000000007;
    
    public DuSieve() {
        this.memo = new HashMap<>();
    }
    
    /**
     * 计算莫比乌斯函数μ(n)
     */
    public long mu(long n) {
        if (n == 1) return 1;
        long result = 1;
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                long cnt = 0;
                while (n % i == 0) {
                    n /= i;
                    cnt++;
                }
                if (cnt > 1) return 0; // 有平方因子
                result = -result;
            }
        }
        if (n > 1) result = -result; // 剩下的质因子
        return result;
    }
    
    /**
     * 计算欧拉函数φ(n)
     */
    public long phi(long n) {
        long result = n;
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                result = result / i * (i - 1);
                while (n % i == 0) n /= i;
            }
        }
        if (n > 1) result = result / n * (n - 1);
        return result;
    }
    
    /**
     * 杜教筛计算莫比乌斯函数前缀和
     * S(n) = Σ(i=1 to n) μ(i)
     */
    public long sumMu(long n) {
        if (n == 0) return 0;
        if (memo.containsKey(n)) return memo.get(n);
        
        if (n <= 1000000) {
            // 小数据直接计算
            long result = 0;
            for (long i = 1; i <= n; i++) {
                result = (result + mu(i)) % MOD;
            }
            memo.put(n, result);
            return result;
        }
        
        // 杜教筛递推公式
        long result = 1; // μ(1) = 1
        for (long i = 2, last; i <= n; i = last + 1) {
            last = n / (n / i);
            long rangeSum = (last - i + 1) % MOD;
            result = (result - rangeSum * sumMu(n / i) % MOD + MOD) % MOD;
        }
        
        result = (result % MOD + MOD) % MOD;
        memo.put(n, result);
        return result;
    }
    
    /**
     * 杜教筛计算欧拉函数前缀和
     * S(n) = Σ(i=1 to n) φ(i)
     */
    public long sumPhi(long n) {
        if (n == 0) return 0;
        if (memo.containsKey(n)) return memo.get(n);
        
        if (n <= 1000000) {
            // 小数据直接计算
            long result = 0;
            for (long i = 1; i <= n; i++) {
                result = (result + phi(i)) % MOD;
            }
            memo.put(n, result);
            return result;
        }
        
        // 杜教筛递推公式
        long result = n % MOD * ((n + 1) % MOD) % MOD;
        if (result % 2 == 0) result /= 2;
        else result = (result + MOD) / 2 % MOD;
        
        for (long i = 2, last; i <= n; i = last + 1) {
            last = n / (n / i);
            long rangeSum = (last - i + 1) % MOD;
            result = (result - rangeSum * sumPhi(n / i) % MOD + MOD) % MOD;
        }
        
        result = (result % MOD + MOD) % MOD;
        memo.put(n, result);
        return result;
    }
    
    /**
     * 清空记忆化缓存
     */
    public void clearMemo() {
        memo.clear();
    }
    
    /**
     * 洛谷P4213 【模板】杜教筛
     * 题目来源: https://www.luogu.com.cn/problem/P4213
     * 题目描述: 给定一个正整数n，求
     * ans1 = Σ(i=1 to n) φ(i)
     * ans2 = Σ(i=1 to n) μ(i)
     * 解题思路: 直接使用杜教筛算法分别计算欧拉函数和莫比乌斯函数的前缀和
     * 时间复杂度: O(n^(2/3))
     * 空间复杂度: O(n^(2/3))
     * 
     * @param n 正整数
     * @return 包含ans1和ans2的数组
     */
    public long[] solveP4213(long n) {
        long ans1 = sumPhi(n);  // 欧拉函数前缀和
        long ans2 = sumMu(n);   // 莫比乌斯函数前缀和
        return new long[]{ans1, ans2};
    }
    
    // 测试用例
    public static void main(String[] args) {
        DuSieve solver = new DuSieve();
        
        // 测试莫比乌斯函数前缀和
        long n1 = 1000000;
        System.out.println("Sum of μ(i) for i=1 to " + n1 + " is: " + solver.sumMu(n1));
        
        // 清空缓存，测试欧拉函数前缀和
        solver.clearMemo();
        long n2 = 1000000;
        System.out.println("Sum of φ(i) for i=1 to " + n2 + " is: " + solver.sumPhi(n2));
        
        // 测试洛谷P4213题目
        solver.clearMemo();
        long n3 = 10;
        long[] result = solver.solveP4213(n3);
        System.out.println("P4213: n=" + n3 + ", ans1=" + result[0] + ", ans2=" + result[1]);
        
        // 边界情况测试
        // 测试小数值
        solver.clearMemo();
        long n4 = 1;
        long[] result4 = solver.solveP4213(n4);
        System.out.println("Boundary test 1: n=" + n4 + ", ans1=" + result4[0] + ", ans2=" + result4[1]);
        
        // 测试较大数值
        solver.clearMemo();
        long n5 = 100;
        long[] result5 = solver.solveP4213(n5);
        System.out.println("Boundary test 2: n=" + n5 + ", ans1=" + result5[0] + ", ans2=" + result5[1]);
        
        // 测试特殊情况：n=0
        solver.clearMemo();
        long n6 = 0;
        long[] result6 = solver.solveP4213(n6);
        System.out.println("Boundary test 3: n=" + n6 + ", ans1=" + result6[0] + ", ans2=" + result6[1]);
    }
}