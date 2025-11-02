// FFT/NTT 算法的Java实现
// 时间复杂度: O(n log n)
// 空间复杂度: O(n)

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FFT_NTT {
    // FFT相关
    private static final double PI = Math.acos(-1.0);
    
    // NTT相关
    private static final long MOD = 998244353L;
    private static final long ROOT = 3L;
    
    // 复数类，用于FFT
    public static class Complex {
        public double real;
        public double imag;
        
        public Complex(double real, double imag) {
            this.real = real;
            this.imag = imag;
        }
        
        public Complex add(Complex other) {
            return new Complex(real + other.real, imag + other.imag);
        }
        
        public Complex subtract(Complex other) {
            return new Complex(real - other.real, imag - other.imag);
        }
        
        public Complex multiply(Complex other) {
            return new Complex(
                real * other.real - imag * other.imag,
                real * other.imag + imag * other.real
            );
        }
        
        public void divide(double n) {
            real /= n;
            imag /= n;
        }
    }
    
    // FFT算法实现
    public static void fft(List<Complex> a, boolean invert) {
        int n = a.size();
        
        // 位反转置换
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >>> 1;
            for (; (j & bit) != 0; bit >>>= 1) {
                j ^= bit;
            }
            j ^= bit;
            
            if (i < j) {
                Complex temp = a.get(i);
                a.set(i, a.get(j));
                a.set(j, temp);
            }
        }
        
        // 蝴蝶操作
        for (int len = 2; len <= n; len <<= 1) {
            double ang = 2 * PI / len * (invert ? -1 : 1);
            Complex wlen = new Complex(Math.cos(ang), Math.sin(ang));
            for (int i = 0; i < n; i += len) {
                Complex w = new Complex(1, 0);
                for (int j = 0; j < len / 2; j++) {
                    Complex u = a.get(i + j);
                    Complex v = a.get(i + j + len / 2).multiply(w);
                    a.set(i + j, u.add(v));
                    a.set(i + j + len / 2, u.subtract(v));
                    w = w.multiply(wlen);
                }
            }
        }
        
        // 逆变换时需要除以n
        if (invert) {
            for (Complex x : a) {
                x.divide(n);
            }
        }
    }
    
    // 多项式乘法 (FFT实现)
    public static List<Long> multiplyFFT(List<Long> a, List<Long> b) {
        List<Complex> fa = new ArrayList<>();
        List<Complex> fb = new ArrayList<>();
        
        // 转换为复数
        for (long x : a) fa.add(new Complex(x, 0));
        for (long x : b) fb.add(new Complex(x, 0));
        
        // 计算需要的最小长度（2的幂次）
        int n = 1;
        while (n < a.size() + b.size() - 1) {
            n <<= 1;
        }
        
        // 填充到足够长度
        while (fa.size() < n) fa.add(new Complex(0, 0));
        while (fb.size() < n) fb.add(new Complex(0, 0));
        
        // 执行FFT
        fft(fa, false);
        fft(fb, false);
        
        // 点值相乘
        for (int i = 0; i < n; i++) {
            fa.set(i, fa.get(i).multiply(fb.get(i)));
        }
        
        // 执行逆FFT
        fft(fa, true);
        
        // 转换为整数结果
        List<Long> result = new ArrayList<>();
        for (Complex x : fa) {
            result.add(Math.round(x.real));
        }
        
        // 移除末尾的零
        while (result.size() > 1 && result.get(result.size() - 1) == 0) {
            result.remove(result.size() - 1);
        }
        
        return result;
    }
    
    // 快速幂取模
    public static long powMod(long a, long b, long mod) {
        long res = 1;
        a %= mod;
        while (b > 0) {
            if ((b & 1) == 1) {
                res = res * a % mod;
            }
            a = a * a % mod;
            b >>>= 1;
        }
        return res;
    }
    
    // 数论逆元
    public static long invMod(long a, long mod) {
        return powMod(a, mod - 2, mod);
    }
    
    // NTT算法实现
    public static void ntt(List<Long> a, boolean invert) {
        int n = a.size();
        
        // 位反转置换
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >>> 1;
            for (; (j & bit) != 0; bit >>>= 1) {
                j ^= bit;
            }
            j ^= bit;
            
            if (i < j) {
                long temp = a.get(i);
                a.set(i, a.get(j));
                a.set(j, temp);
            }
        }
        
        // 蝴蝶操作
        for (int len = 2; len <= n; len <<= 1) {
            long wlen = powMod(ROOT, (MOD - 1) / len, MOD);
            if (invert) {
                wlen = invMod(wlen, MOD);
            }
            for (int i = 0; i < n; i += len) {
                long w = 1;
                for (int j = 0; j < len / 2; j++) {
                    long u = a.get(i + j);
                    long v = a.get(i + j + len / 2) * w % MOD;
                    a.set(i + j, (u + v) % MOD);
                    a.set(i + j + len / 2, (u - v + MOD) % MOD);
                    w = w * wlen % MOD;
                }
            }
        }
        
        // 逆变换时需要处理
        if (invert) {
            long invN = invMod(n, MOD);
            for (int i = 0; i < n; i++) {
                a.set(i, a.get(i) * invN % MOD);
            }
        }
    }
    
    // 多项式乘法 (NTT实现)
    public static List<Long> multiplyNTT(List<Long> a, List<Long> b) {
        List<Long> fa = new ArrayList<>(a);
        List<Long> fb = new ArrayList<>(b);
        
        // 计算需要的最小长度（2的幂次）
        int n = 1;
        while (n < a.size() + b.size() - 1) {
            n <<= 1;
        }
        
        // 填充到足够长度
        while (fa.size() < n) fa.add(0L);
        while (fb.size() < n) fb.add(0L);
        
        // 执行NTT
        ntt(fa, false);
        ntt(fb, false);
        
        // 点值相乘
        for (int i = 0; i < n; i++) {
            fa.set(i, fa.get(i) * fb.get(i) % MOD);
        }
        
        // 执行逆NTT
        ntt(fa, true);
        
        // 移除末尾的零
        while (fa.size() > 1 && fa.get(fa.size() - 1) == 0) {
            fa.remove(fa.size() - 1);
        }
        
        return fa;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // FFT测试
        List<Long> a = Arrays.asList(1L, 2L, 3L);
        List<Long> b = Arrays.asList(4L, 5L, 6L);
        List<Long> resFFT = multiplyFFT(a, b);
        
        System.out.print("FFT乘法结果: ");
        for (long x : resFFT) {
            System.out.print(x + " ");
        }
        System.out.println();
        
        // NTT测试
        List<Long> c = Arrays.asList(1L, 2L, 3L);
        List<Long> d = Arrays.asList(4L, 5L, 6L);
        List<Long> resNTT = multiplyNTT(c, d);
        
        System.out.print("NTT乘法结果: ");
        for (long x : resNTT) {
            System.out.print(x + " ");
        }
        System.out.println();
        
        // 边界情况测试
        List<Long> empty = new ArrayList<>();
        List<Long> single = Arrays.asList(5L);
        
        // 工程化考量：
        // 1. 异常处理：增加输入验证
        // 2. 性能优化：使用数组代替ArrayList
        // 3. 内存优化：复用对象以减少GC压力
        // 4. 线程安全：添加同步机制或使用线程安全的集合
    }
    
    /*
     * 算法细节说明：
     * 1. FFT利用复数运算将多项式转换为点值表示，实现O(n log n)的乘法
     * 2. NTT在模运算下进行类似操作，避免浮点精度问题
     * 3. 位反转置换是FFT/NTT的关键步骤，确保计算的正确性
     * 4. 蝴蝶操作是算法的核心，通过分治的思想降低时间复杂度
     * 
     * 常见应用：
     * - 大数乘法：如力扣43题《字符串相乘》
     * - 卷积计算：图像处理、信号处理
     * - 多项式幂运算：如求多项式的n次方
     * - 字符串匹配：通过卷积加速KMP算法
     * 
     * 优化方向：
     * 1. 常数优化：使用迭代而非递归
     * 2. 内存优化：原地算法实现
     * 3. 精度优化：使用双精度浮点数
     * 4. 并行优化：利用多核CPU并行计算
     */
}