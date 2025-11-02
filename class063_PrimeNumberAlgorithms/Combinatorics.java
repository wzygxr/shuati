package number_theory;

import java.util.*;

/**
 * 组合数学工具类
 * 
 * 算法简介:
 * 实现组合数学中的常用算法，包括容斥原理、生成函数、拉格朗日插值等。
 * 
 * 适用场景:
 * 1. 计数问题
 * 2. 容斥原理应用
 * 3. 生成函数计算
 * 4. 多项式插值
 * 
 * 核心思想:
 * 1. 容斥原理处理重复计数问题
 * 2. 生成函数处理组合计数问题
 * 3. 拉格朗日插值进行多项式重建
 * 
 * 时间复杂度: 根据具体算法而定
 * 空间复杂度: 根据具体算法而定
 */
public class Combinatorics {
    private static final long MOD = 1000000007;
    private static long[] fact;  // 阶乘数组
    private static long[] ifact; // 逆元阶乘数组
    
    /**
     * 预处理阶乘和逆元阶乘
     * @param n 最大值
     */
    public static void init(int n) {
        fact = new long[n + 1];
        ifact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        ifact[n] = modInverse(fact[n], MOD);
        for (int i = n - 1; i >= 0; i--) {
            ifact[i] = ifact[i + 1] * (i + 1) % MOD;
        }
    }
    
    /**
     * 计算组合数 C(n, k)
     * @param n 总数
     * @param k 选择数
     * @return 组合数
     */
    public static long comb(int n, int k) {
        if (k < 0 || k > n) return 0;
        return fact[n] * ifact[k] % MOD * ifact[n - k] % MOD;
    }
    
    /**
     * 容斥原理实现
     * @param sets 集合列表
     * @return 并集大小
     */
    public static long inclusionExclusion(List<Set<Integer>> sets) {
        long result = 0;
        int n = sets.size();
        
        // 枚举所有子集
        for (int mask = 1; mask < (1 << n); mask++) {
            Set<Integer> intersection = new HashSet<>();
            boolean first = true;
            int count = 0;
            
            // 计算交集
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    count++;
                    if (first) {
                        intersection.addAll(sets.get(i));
                        first = false;
                    } else {
                        intersection.retainAll(sets.get(i));
                    }
                }
            }
            
            // 根据容斥原理加减
            if (count % 2 == 1) {
                result = (result + intersection.size()) % MOD;
            } else {
                result = (result - intersection.size() + MOD) % MOD;
            }
        }
        
        return result;
    }
    
    /**
     * 普通生成函数 (OGF)
     * @param coefficients 系数数组
     * @param x 变量值
     * @return 生成函数值
     */
    public static long ogf(long[] coefficients, long x) {
        long result = 0;
        long power = 1;
        for (int i = 0; i < coefficients.length; i++) {
            result = (result + coefficients[i] * power) % MOD;
            power = power * x % MOD;
        }
        return result;
    }
    
    /**
     * 指数生成函数 (EGF)
     * @param coefficients 系数数组
     * @param x 变量值
     * @return 生成函数值
     */
    public static long egf(long[] coefficients, long x) {
        if (fact == null) init(coefficients.length);
        
        long result = 0;
        long power = 1;
        for (int i = 0; i < coefficients.length; i++) {
            result = (result + coefficients[i] * power % MOD * ifact[i]) % MOD;
            power = power * x % MOD;
        }
        return result;
    }
    
    /**
     * 拉格朗日插值
     * @param x x坐标数组
     * @param y y坐标数组
     * @param target 目标x值
     * @return 插值结果
     */
    public static long lagrangeInterpolation(long[] x, long[] y, long target) {
        int n = x.length;
        long result = 0;
        
        for (int i = 0; i < n; i++) {
            long numerator = 1;   // 分子
            long denominator = 1; // 分母
            
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    numerator = numerator * (target - x[j] + MOD) % MOD;
                    denominator = denominator * (x[i] - x[j] + MOD) % MOD;
                }
            }
            
            long term = y[i] * numerator % MOD * modInverse(denominator, MOD) % MOD;
            result = (result + term) % MOD;
        }
        
        return result;
    }
    
    /**
     * 多点插值重建多项式
     * @param points 点集数组 [[x1,y1], [x2,y2], ...]
     * @return 多项式系数数组
     */
    public static long[] polynomialReconstruction(long[][] points) {
        int n = points.length;
        long[] result = new long[n];
        
        // 使用拉格朗日插值法重建多项式
        for (int i = 0; i < n; i++) {
            long[] x = new long[n];
            long[] y = new long[n];
            for (int j = 0; j < n; j++) {
                x[j] = points[j][0];
                y[j] = points[j][1];
            }
            
            // 计算第i项系数
            long coefficient = 0;
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    long numerator = 1;
                    long denominator = 1;
                    
                    for (int k = 0; k < n; k++) {
                        if (k != i) {
                            numerator = numerator * (0 - x[k] + MOD) % MOD;
                            if (k != j) {
                                denominator = denominator * (x[j] - x[k] + MOD) % MOD;
                            }
                        }
                    }
                    
                    long term = y[j] * numerator % MOD * modInverse(denominator, MOD) % MOD;
                    coefficient = (coefficient + term) % MOD;
                }
            }
            
            result[i] = coefficient;
        }
        
        return result;
    }
    
    /**
     * 快速幂运算
     */
    public static long powMod(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }
    
    /**
     * 模逆元
     */
    public static long modInverse(long a, long mod) {
        return powMod(a, mod - 2, mod);
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 初始化阶乘数组
        init(100);
        
        // 测试组合数计算
        System.out.println("C(10, 3) = " + comb(10, 3));
        
        // 测试容斥原理
        List<Set<Integer>> sets = new ArrayList<>();
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));
        Set<Integer> set3 = new HashSet<>(Arrays.asList(5, 6, 7, 8));
        sets.add(set1);
        sets.add(set2);
        sets.add(set3);
        System.out.println("Inclusion-Exclusion result: " + inclusionExclusion(sets));
        
        // 测试拉格朗日插值
        long[] x = {1, 2, 3, 4};
        long[] y = {1, 4, 9, 16}; // y = x^2
        long target = 5;
        long interpolated = lagrangeInterpolation(x, y, target);
        System.out.println("Lagrange interpolation result: " + interpolated);
    }
}