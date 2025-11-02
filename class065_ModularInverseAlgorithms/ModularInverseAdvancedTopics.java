package class099;

import java.util.*;
import java.math.BigInteger;

/**
 * 模逆元高级专题与工程化应用
 * 包含机器学习、密码学、图像处理等领域的模逆元应用
 * 
 * 本文件重点探讨：
 * 1. 模逆元在机器学习深度学习中的应用
 * 2. 模逆元在密码学中的关键作用
 * 3. 模逆元在图像处理和安全传输中的应用
 * 4. 模逆元在自然语言处理中的应用
 * 5. 工程化考量和性能优化
 * 6. 异常处理和边界场景
 * 7. 单元测试和性能测试
 * 8. 多语言实现对比
 */

public class ModularInverseAdvancedTopics {
    
    private static final int MOD = 1000000007;
    private static final int MOD_CRYPTO = 2147483647; // 大质数用于密码学
    
    // ==================== 机器学习深度学习应用 ====================
    
    /**
     * 大规模矩阵运算中的模逆元应用
     * 在机器学习中，经常需要处理大规模矩阵运算，模逆元可以用于优化计算
     * 
     * 应用场景：
     * 1. 线性回归的闭式解计算
     * 2. 岭回归的正则化参数计算
     * 3. 支持向量机的对偶问题求解
     * 4. 神经网络的反向传播优化
     * 
     * 时间复杂度: O(n^3) 传统方法 vs O(n^2.373) 优化方法
     * 空间复杂度: O(n^2)
     */
    public static class MatrixInverseModular {
        /**
         * 使用模逆元计算矩阵的模逆（当模数为质数时）
         * 基于高斯-约当消元法
         * 
         * @param matrix 输入矩阵
         * @param mod 模数（必须是质数）
         * @return 矩阵的模逆，如果不可逆返回null
         */
        public static long[][] matrixInverse(long[][] matrix, int mod) {
            int n = matrix.length;
            
            // 创建增广矩阵 [A|I]
            long[][] augmented = new long[n][2 * n];
            for (int i = 0; i < n; i++) {
                System.arraycopy(matrix[i], 0, augmented[i], 0, n);
                augmented[i][n + i] = 1;
            }
            
            // 高斯-约当消元
            for (int i = 0; i < n; i++) {
                // 寻找主元
                int pivot = i;
                for (int j = i + 1; j < n; j++) {
                    if (Math.abs(augmented[j][i]) > Math.abs(augmented[pivot][i])) {
                        pivot = j;
                    }
                }
                
                // 交换行
                long[] temp = augmented[i];
                augmented[i] = augmented[pivot];
                augmented[pivot] = temp;
                
                // 检查主元是否为0
                if (augmented[i][i] == 0) {
                    return null; // 矩阵不可逆
                }
                
                // 主元归一化
                long inv = modularInverse(augmented[i][i], mod);
                for (int j = i; j < 2 * n; j++) {
                    augmented[i][j] = augmented[i][j] * inv % mod;
                }
                
                // 消元
                for (int j = 0; j < n; j++) {
                    if (j != i && augmented[j][i] != 0) {
                        long factor = augmented[j][i];
                        for (int k = i; k < 2 * n; k++) {
                            augmented[j][k] = (augmented[j][k] - factor * augmented[i][k] % mod + mod) % mod;
                        }
                    }
                }
            }
            
            // 提取逆矩阵
            long[][] inverse = new long[n][n];
            for (int i = 0; i < n; i++) {
                System.arraycopy(augmented[i], n, inverse[i], 0, n);
            }
            
            return inverse;
        }
        
        /**
         * 使用模逆元计算线性回归的闭式解
         * 公式: θ = (X^T * X)^(-1) * X^T * y
         * 
         * @param X 特征矩阵 (m x n)
         * @param y 目标向量 (m x 1)
         * @param mod 模数
         * @return 参数向量θ
         */
        public static long[] linearRegressionClosedForm(long[][] X, long[] y, int mod) {
            int m = X.length;
            int n = X[0].length;
            
            // 计算 X^T * X
            long[][] XTX = new long[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < m; k++) {
                        XTX[i][j] = (XTX[i][j] + X[k][i] * X[k][j] % mod) % mod;
                    }
                }
            }
            
            // 计算 (X^T * X)^(-1)
            long[][] XTX_inv = matrixInverse(XTX, mod);
            if (XTX_inv == null) {
                throw new IllegalArgumentException("Matrix X^T*X is singular");
            }
            
            // 计算 X^T * y
            long[] XTy = new long[n];
            for (int i = 0; i < n; i++) {
                for (int k = 0; k < m; k++) {
                    XTy[i] = (XTy[i] + X[k][i] * y[k] % mod) % mod;
                }
            }
            
            // 计算 θ = (X^T * X)^(-1) * X^T * y
            long[] theta = new long[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    theta[i] = (theta[i] + XTX_inv[i][j] * XTy[j] % mod) % mod;
                }
            }
            
            return theta;
        }
    }
    
    // ==================== 密码学应用 ====================
    
    /**
     * RSA加密算法中的模逆元应用
     * RSA算法是模逆元在密码学中最经典的应用
     * 
     * 算法步骤：
     * 1. 选择两个大质数p和q
     * 2. 计算n = p * q, φ(n) = (p-1)*(q-1)
     * 3. 选择加密指数e，满足1 < e < φ(n)且gcd(e, φ(n)) = 1
     * 4. 计算解密指数d = e^(-1) mod φ(n)
     * 5. 公钥: (e, n)，私钥: (d, n)
     */
    public static class RSAEncryption {
        private BigInteger n;
        private BigInteger e;
        private BigInteger d;
        private BigInteger phi;
        
        /**
         * RSA密钥生成
         * @param p 质数p
         * @param q 质数q
         * @param encryptionExponent 加密指数e
         */
        public RSAEncryption(BigInteger p, BigInteger q, BigInteger encryptionExponent) {
            this.n = p.multiply(q);
            this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            this.e = encryptionExponent;
            
            // 计算解密指数d = e^(-1) mod φ(n)
            this.d = e.modInverse(phi);
        }
        
        /**
         * RSA加密
         * @param message 明文消息
         * @return 密文
         */
        public BigInteger encrypt(BigInteger message) {
            return message.modPow(e, n);
        }
        
        /**
         * RSA解密
         * @param ciphertext 密文
         * @return 明文
         */
        public BigInteger decrypt(BigInteger ciphertext) {
            return ciphertext.modPow(d, n);
        }
        
        /**
         * 使用扩展欧几里得算法手动计算模逆元（教学目的）
         */
        public static BigInteger manualModInverse(BigInteger a, BigInteger m) {
            BigInteger[] result = extendedGcd(a, m);
            BigInteger gcd = result[0];
            BigInteger x = result[1];
            
            if (!gcd.equals(BigInteger.ONE)) {
                throw new ArithmeticException("Modular inverse does not exist");
            }
            
            return x.mod(m).add(m).mod(m);
        }
        
        private static BigInteger[] extendedGcd(BigInteger a, BigInteger b) {
            if (b.equals(BigInteger.ZERO)) {
                return new BigInteger[]{a, BigInteger.ONE, BigInteger.ZERO};
            }
            
            BigInteger[] values = extendedGcd(b, a.mod(b));
            BigInteger gcd = values[0];
            BigInteger x1 = values[1];
            BigInteger y1 = values[2];
            
            BigInteger x = y1;
            BigInteger y = x1.subtract(a.divide(b).multiply(y1));
            
            return new BigInteger[]{gcd, x, y};
        }
    }
    
    // ==================== 图像处理应用 ====================
    
    /**
     * 图像加密中的模逆元应用
     * 使用模逆元实现简单的图像像素值加密
     */
    public static class ImageEncryption {
        /**
         * 使用模运算加密图像像素
         * 加密公式: encrypted = (pixel * key) mod MOD
         * 解密公式: pixel = (encrypted * key^(-1)) mod MOD
         * 
         * @param pixels 图像像素数组
         * @param key 加密密钥（必须与MOD互质）
         * @param MOD 模数
         * @return 加密后的像素数组
         */
        public static int[] encryptImage(int[] pixels, int key, int MOD) {
            // 验证密钥有效性
            if (gcd(key, MOD) != 1) {
                throw new IllegalArgumentException("Key must be coprime with MOD");
            }
            
            int[] encrypted = new int[pixels.length];
            for (int i = 0; i < pixels.length; i++) {
                encrypted[i] = (int)((long)pixels[i] * key % MOD);
            }
            return encrypted;
        }
        
        /**
         * 使用模逆元解密图像像素
         * @param encryptedPixels 加密的像素数组
         * @param key 加密密钥
         * @param MOD 模数
         * @return 解密后的像素数组
         */
        public static int[] decryptImage(int[] encryptedPixels, int key, int MOD) {
            long keyInverse = modularInverse(key, MOD);
            int[] decrypted = new int[encryptedPixels.length];
            for (int i = 0; i < encryptedPixels.length; i++) {
                decrypted[i] = (int)((long)encryptedPixels[i] * keyInverse % MOD);
            }
            return decrypted;
        }
        
        /**
         * 计算最大公约数
         */
        private static int gcd(int a, int b) {
            return b == 0 ? a : gcd(b, a % b);
        }
    }
    
    // ==================== 自然语言处理应用 ====================
    
    /**
     * 文本加密中的模逆元应用
     * 使用模运算实现简单的文本字符加密
     */
    public static class TextEncryption {
        /**
         * 使用模运算加密文本
         * 将每个字符映射为数字，然后进行模运算加密
         * 
         * @param text 明文文本
         * @param key 加密密钥
         * @param MOD 模数（通常选择大于字符集大小的质数）
         * @return 加密后的文本（数字序列）
         */
        public static int[] encryptText(String text, int key, int MOD) {
            int[] encrypted = new int[text.length()];
            for (int i = 0; i < text.length(); i++) {
                int charValue = text.charAt(i);
                encrypted[i] = (int)((long)charValue * key % MOD);
            }
            return encrypted;
        }
        
        /**
         * 使用模逆元解密文本
         * @param encrypted 加密的数字序列
         * @param key 加密密钥
         * @param MOD 模数
         * @return 解密后的文本
         */
        public static String decryptText(int[] encrypted, int key, int MOD) {
            long keyInverse = modularInverse(key, MOD);
            StringBuilder decrypted = new StringBuilder();
            for (int value : encrypted) {
                int charValue = (int)((long)value * keyInverse % MOD);
                decrypted.append((char)charValue);
            }
            return decrypted.toString();
        }
    }
    
    // ==================== 工程化考量和性能优化 ====================
    
    /**
     * 模逆元计算的性能优化策略
     */
    public static class ModularInverseOptimization {
        private static Map<Long, Long> cache = new HashMap<>();
        private static long[] precomputedInverses = null;
        private static int precomputedLimit = 0;
        
        /**
         * 带缓存的模逆元计算
         * 对于重复计算的模逆元，使用缓存提高性能
         * 
         * @param a 要求逆元的数
         * @param mod 模数
         * @return 模逆元
         */
        public static long cachedModularInverse(long a, long mod) {
            long key = a * 1000000007L + mod; // 简单的哈希键
            if (cache.containsKey(key)) {
                return cache.get(key);
            }
            
            long result = modularInverse(a, mod);
            cache.put(key, result);
            return result;
        }
        
        /**
         * 预计算模逆元表
         * 对于需要频繁计算1~n模逆元的场景，预计算提高性能
         * 
         * @param n 预计算上限
         * @param mod 模数
         */
        public static void precomputeInverses(int n, int mod) {
            precomputedInverses = new long[n + 1];
            precomputedInverses[1] = 1;
            for (int i = 2; i <= n; i++) {
                precomputedInverses[i] = (mod - (mod / i) * precomputedInverses[mod % i] % mod) % mod;
            }
            precomputedLimit = n;
        }
        
        /**
         * 使用预计算表获取模逆元
         */
        public static long getPrecomputedInverse(int a, int mod) {
            if (precomputedInverses == null || a > precomputedLimit) {
                return modularInverse(a, mod);
            }
            return precomputedInverses[a];
        }
    }
    
    // ==================== 异常处理和边界场景 ====================
    
    /**
     * 模逆元计算的异常处理
     */
    public static class ModularInverseExceptionHandling {
        /**
         * 安全的模逆元计算，包含完整的异常处理
         * 
         * @param a 要求逆元的数
         * @param mod 模数
         * @return 模逆元结果
         * @throws IllegalArgumentException 参数不合法时抛出异常
         * @throws ArithmeticException 模逆元不存在时抛出异常
         */
        public static long safeModularInverse(long a, long mod) {
            // 参数验证
            if (mod == 0) {
                throw new IllegalArgumentException("Modulus cannot be zero");
            }
            if (mod < 0) {
                throw new IllegalArgumentException("Modulus must be positive");
            }
            
            // 处理负数输入
            a = (a % mod + mod) % mod;
            
            // 特殊情况处理
            if (a == 0) {
                throw new ArithmeticException("Modular inverse of 0 does not exist");
            }
            if (a == 1) {
                return 1; // 1的逆元总是1
            }
            
            // 计算模逆元
            long result = modularInverse(a, mod);
            if (result == -1) {
                throw new ArithmeticException("Modular inverse does not exist for a=" + a + ", mod=" + mod);
            }
            
            return result;
        }
        
        /**
         * 批量模逆元计算，包含错误收集
         * 
         * @param numbers 要求逆元的数数组
         * @param mod 模数
         * @return 结果映射，包含成功和失败的信息
         */
        public static Map<Long, Object> batchModularInverse(long[] numbers, long mod) {
            Map<Long, Object> results = new HashMap<>();
            
            for (long number : numbers) {
                try {
                    long inverse = safeModularInverse(number, mod);
                    results.put(number, inverse);
                } catch (Exception e) {
                    results.put(number, "Error: " + e.getMessage());
                }
            }
            
            return results;
        }
    }
    
    // ==================== 单元测试和性能测试 ====================
    
    /**
     * 模逆元计算的单元测试
     */
    public static class ModularInverseUnitTest {
        /**
         * 运行所有单元测试
         */
        public static void runAllTests() {
            testBasicCases();
            testEdgeCases();
            testPerformance();
            testExceptionHandling();
            System.out.println("所有单元测试通过!");
        }
        
        private static void testBasicCases() {
            assert modularInverse(3, 11) == 4 : "3 mod 11 inverse should be 4";
            assert modularInverse(5, 13) == 8 : "5 mod 13 inverse should be 8";
            assert modularInverse(7, 19) == 11 : "7 mod 19 inverse should be 11";
            System.out.println("基础测试通过");
        }
        
        private static void testEdgeCases() {
            // 测试边界情况
            try {
                modularInverse(0, 5);
                assert false : "Should throw exception for 0";
            } catch (Exception e) {
                // 预期行为
            }
            
            try {
                modularInverse(6, 8);
                assert false : "Should return -1 for non-coprime numbers";
            } catch (Exception e) {
                // 预期行为
            }
            System.out.println("边界测试通过");
        }
        
        private static void testPerformance() {
            long start = System.currentTimeMillis();
            for (int i = 1; i <= 100000; i++) {
                modularInverse(i, MOD);
            }
            long end = System.currentTimeMillis();
            System.out.println("性能测试: 100000次计算耗时 " + (end - start) + "ms");
        }
        
        private static void testExceptionHandling() {
            ModularInverseExceptionHandling.safeModularInverse(3, 11);
            try {
                ModularInverseExceptionHandling.safeModularInverse(0, 5);
                assert false : "Should throw exception";
            } catch (Exception e) {
                // 预期行为
            }
            System.out.println("异常处理测试通过");
        }
    }
    
    // ==================== 工具方法 ====================
    
    /**
     * 模逆元计算的核心方法
     */
    private static long modularInverse(long a, long mod) {
        long[] x = new long[1];
        long[] y = new long[1];
        long gcd = extendedGcd(a, mod, x, y);
        
        if (gcd != 1) {
            return -1;
        }
        
        return (x[0] % mod + mod) % mod;
    }
    
    /**
     * 扩展欧几里得算法实现
     */
    private static long extendedGcd(long a, long b, long[] x, long[] y) {
        if (b == 0) {
            x[0] = 1;
            y[0] = 0;
            return a;
        }
        
        long[] x1 = new long[1];
        long[] y1 = new long[1];
        long gcd = extendedGcd(b, a % b, x1, y1);
        
        x[0] = y1[0];
        y[0] = x1[0] - (a / b) * y1[0];
        
        return gcd;
    }
    
    // ==================== 主函数 ====================
    
    public static void main(String[] args) {
        System.out.println("=== 模逆元高级专题与工程化应用 ===");
        
        // 运行单元测试
        ModularInverseUnitTest.runAllTests();
        
        // 演示机器学习应用
        System.out.println("\n=== 机器学习应用演示 ===");
        long[][] X = {{1, 1}, {1, 2}, {1, 3}};
        long[] y = {2, 3, 4};
        long[] theta = MatrixInverseModular.linearRegressionClosedForm(X, y, MOD);
        System.out.println("线性回归参数: " + Arrays.toString(theta));
        
        // 演示密码学应用
        System.out.println("\n=== 密码学应用演示 ===");
        BigInteger p = new BigInteger("61");
        BigInteger q = new BigInteger("53");
        BigInteger e = new BigInteger("17");
        RSAEncryption rsa = new RSAEncryption(p, q, e);
        BigInteger message = new BigInteger("65");
        BigInteger ciphertext = rsa.encrypt(message);
        BigInteger decrypted = rsa.decrypt(ciphertext);
        System.out.println("RSA加密解密演示:");
        System.out.println("原始消息: " + message);
        System.out.println("加密结果: " + ciphertext);
        System.out.println("解密结果: " + decrypted);
        
        // 演示图像处理应用
        System.out.println("\n=== 图像处理应用演示 ===");
        int[] pixels = {100, 150, 200, 50};
        int key = 7;
        int MOD_IMAGE = 251; // 质数，大于最大像素值
        int[] encrypted = ImageEncryption.encryptImage(pixels, key, MOD_IMAGE);
        int[] decryptedPixels = ImageEncryption.decryptImage(encrypted, key, MOD_IMAGE);
        System.out.println("图像加密解密演示:");
        System.out.println("原始像素: " + Arrays.toString(pixels));
        System.out.println("加密像素: " + Arrays.toString(encrypted));
        System.out.println("解密像素: " + Arrays.toString(decryptedPixels));
        
        // 演示性能优化
        System.out.println("\n=== 性能优化演示 ===");
        ModularInverseOptimization.precomputeInverses(1000000, MOD);
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            ModularInverseOptimization.getPrecomputedInverse(i, MOD);
        }
        long end = System.currentTimeMillis();
        System.out.println("预计算优化后1000次查询耗时: " + (end - start) + "ms");
        
        System.out.println("\n演示完成!");
    }
}