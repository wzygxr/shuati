// 组合数学工具模块

/*
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

class Combinatorics {
private:
    static const long long MOD = 1000000007;
    static long long fact[1000001];  // 阶乘数组
    static long long ifact[1000001]; // 逆元阶乘数组
    
public:
    /**
     * 预处理阶乘和逆元阶乘
     * @param n 最大值
     */
    static void init(int n) {
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
    static long long comb(int n, int k) {
        if (k < 0 || k > n) return 0;
        return fact[n] * ifact[k] % MOD * ifact[n - k] % MOD;
    }
    
    /**
     * 容斥原理实现（简化版本）
     * @param sets 集合大小数组
     * @param n 集合数量
     * @return 并集大小估算
     */
    static long long inclusionExclusion(int* sets, int n) {
        long long result = 0;
        
        // 简化实现：仅演示原理，实际应用需根据具体问题调整
        for (int mask = 1; mask < (1 << n); mask++) {
            int intersection_size = 1000000; // 假设交集大小
            int count = 0;
            
            // 计算交集大小（简化处理）
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    count++;
                    if (sets[i] < intersection_size) {
                        intersection_size = sets[i];
                    }
                }
            }
            
            // 根据容斥原理加减
            if (count % 2 == 1) {
                result = (result + intersection_size) % MOD;
            } else {
                result = (result - intersection_size + MOD) % MOD;
            }
        }
        
        return result;
    }
    
    /**
     * 普通生成函数 (OGF)
     * @param coefficients 系数数组
     * @param coeff_len 系数数组长度
     * @param x 变量值
     * @return 生成函数值
     */
    static long long ogf(long long* coefficients, int coeff_len, long long x) {
        long long result = 0;
        long long power = 1;
        for (int i = 0; i < coeff_len; i++) {
            result = (result + coefficients[i] * power) % MOD;
            power = power * x % MOD;
        }
        return result;
    }
    
    /**
     * 指数生成函数 (EGF)
     * @param coefficients 系数数组
     * @param coeff_len 系数数组长度
     * @param x 变量值
     * @return 生成函数值
     */
    static long long egf(long long* coefficients, int coeff_len, long long x) {
        long long result = 0;
        long long power = 1;
        for (int i = 0; i < coeff_len; i++) {
            result = (result + coefficients[i] * power % MOD * ifact[i]) % MOD;
            power = power * x % MOD;
        }
        return result;
    }
    
    /**
     * 拉格朗日插值
     * @param x x坐标数组
     * @param y y坐标数组
     * @param n 点数
     * @param target 目标x值
     * @return 插值结果
     */
    static long long lagrangeInterpolation(long long* x, long long* y, int n, long long target) {
        long long result = 0;
        
        for (int i = 0; i < n; i++) {
            long long numerator = 1;   // 分子
            long long denominator = 1; // 分母
            
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    numerator = numerator * (target - x[j] + MOD) % MOD;
                    denominator = denominator * (x[i] - x[j] + MOD) % MOD;
                }
            }
            
            long long term = y[i] * numerator % MOD * modInverse(denominator, MOD) % MOD;
            result = (result + term) % MOD;
        }
        
        return result;
    }
    
    /**
     * 快速幂运算
     */
    static long long powMod(long long base, long long exp, long long mod) {
        long long result = 1;
        base %= mod;
        while (exp > 0) {
            if (exp & 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }
    
    /**
     * 模逆元
     */
    static long long modInverse(long long a, long long mod) {
        return powMod(a, mod - 2, mod);
    }
};

// 静态成员变量定义
long long Combinatorics::fact[1000001];
long long Combinatorics::ifact[1000001];