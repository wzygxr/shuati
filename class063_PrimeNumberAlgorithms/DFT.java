// 离散傅里叶变换（DFT）的Java实现
// 时间复杂度：O(n²)
// 空间复杂度：O(n)

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DFT {
    // 复数类
    public static class Complex {
        public double real;
        public double imag;
        
        public Complex(double real, double imag) {
            this.real = real;
            this.imag = imag;
        }
        
        public Complex() {
            this(0, 0);
        }
        
        // 加法
        public Complex add(Complex other) {
            return new Complex(this.real + other.real, this.imag + other.imag);
        }
        
        // 减法
        public Complex subtract(Complex other) {
            return new Complex(this.real - other.real, this.imag - other.imag);
        }
        
        // 乘法
        public Complex multiply(Complex other) {
            return new Complex(
                this.real * other.real - this.imag * other.imag,
                this.real * other.imag + this.imag * other.real
            );
        }
        
        // 除法
        public Complex divide(double divisor) {
            return new Complex(this.real / divisor, this.imag / divisor);
        }
        
        @Override
        public String toString() {
            return "(" + real + ", " + imag + ")";
        }
    }
    
    private static final double PI = Math.PI;
    
    // 离散傅里叶变换（DFT）
    // 时间复杂度：O(n²)
    public static List<Complex> dft(List<Complex> a, boolean invert) {
        int n = a.size();
        List<Complex> result = new ArrayList<>(n);
        
        for (int k = 0; k < n; k++) {
            Complex sum = new Complex(0, 0);
            for (int j = 0; j < n; j++) {
                // 计算旋转因子 W_n^(kj) = e^(-2πikj/n) 或 W_n^(-kj) = e^(2πikj/n)
                double angle = 2 * PI * k * j / n;
                if (invert) {
                    angle = -angle; // 逆变换时角度取反
                }
                Complex w = new Complex(Math.cos(angle), Math.sin(angle));
                // a[j] * w
                Complex product = a.get(j).multiply(w);
                // 累加
                sum = sum.add(product);
            }
            result.add(sum);
        }
        
        // 逆变换需要除以n
        if (invert) {
            for (int i = 0; i < result.size(); i++) {
                result.set(i, result.get(i).divide(n));
            }
        }
        
        return result;
    }
    
    // 快速傅里叶变换（FFT）- 用于对比
    // 时间复杂度：O(n log n)
    public static void fft(List<Complex> a, boolean invert) {
        int n = a.size();
        
        // 位反转置换
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >> 1;
            for (; (j & bit) != 0; bit >>= 1) {
                j ^= bit;
            }
            j ^= bit;
            
            if (i < j) {
                Complex temp = a.get(i);
                a.set(i, a.get(j));
                a.set(j, temp);
            }
        }
        
        // 迭代实现的FFT
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
        
        if (invert) {
            for (int i = 0; i < n; i++) {
                a.set(i, a.get(i).divide(n));
            }
        }
    }
    
    // 多项式乘法 - 使用DFT
    // 时间复杂度：O(n²)
    public static List<Long> multiplyPolynomialsDFT(List<Long> a, List<Long> b) {
        int n = 1;
        while (n < a.size() + b.size() - 1) {
            n <<= 1; // 向上取到2的幂次
        }
        
        // 转换为复数
        List<Complex> fa = new ArrayList<>(n);
        List<Complex> fb = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            fa.add(new Complex(i < a.size() ? a.get(i) : 0, 0));
            fb.add(new Complex(i < b.size() ? b.get(i) : 0, 0));
        }
        
        // 进行DFT
        List<Complex> faDFT = dft(fa, false);
        List<Complex> fbDFT = dft(fb, false);
        
        // 点乘
        List<Complex> fcDFT = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            fcDFT.add(faDFT.get(i).multiply(fbDFT.get(i)));
        }
        
        // 逆DFT得到结果
        List<Complex> fc = dft(fcDFT, true);
        
        // 转换回整数
        List<Long> result = new ArrayList<>(a.size() + b.size() - 1);
        for (int i = 0; i < result.size(); i++) {
            result.add(Math.round(fc.get(i).real));
        }
        
        return result;
    }
    
    // 多项式乘法 - 使用FFT
    // 时间复杂度：O(n log n)
    public static List<Long> multiplyPolynomialsFFT(List<Long> a, List<Long> b) {
        int n = 1;
        while (n < a.size() + b.size() - 1) {
            n <<= 1;
        }
        
        List<Complex> fa = new ArrayList<>(n);
        List<Complex> fb = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            fa.add(new Complex(i < a.size() ? a.get(i) : 0, 0));
            fb.add(new Complex(i < b.size() ? b.get(i) : 0, 0));
        }
        
        fft(fa, false);
        fft(fb, false);
        
        for (int i = 0; i < n; i++) {
            fa.set(i, fa.get(i).multiply(fb.get(i)));
        }
        
        fft(fa, true);
        
        List<Long> result = new ArrayList<>(a.size() + b.size() - 1);
        for (int i = 0; i < result.size(); i++) {
            result.add(Math.round(fa.get(i).real));
        }
        
        return result;
    }
    
    // 力扣第43题：字符串相乘 - 大数乘法
    // 时间复杂度：O(m*n)
    // 空间复杂度：O(m+n)
    public static String multiplyStrings(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        
        int m = num1.length();
        int n = num2.length();
        int[] result = new int[m + n];
        
        // 逐位相乘
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int product = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int p1 = i + j;
                int p2 = i + j + 1;
                int sum = product + result[p2];
                
                result[p1] += sum / 10;
                result[p2] = sum % 10;
            }
        }
        
        // 构造结果字符串
        StringBuilder sb = new StringBuilder();
        for (int digit : result) {
            if (!(sb.length() == 0 && digit == 0)) { // 跳过前导零
                sb.append(digit);
            }
        }
        
        return sb.toString();
    }
    
    // 力扣第363题：矩形区域不超过 K 的最大数值和
    // 使用二维前缀和优化
    // 时间复杂度：O(m²*n*log(n))
    // 空间复杂度：O(n)
    public static int maxSumSubmatrix(int[][] matrix, int k) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        int result = Integer.MIN_VALUE;
        
        // 枚举上下边界
        for (int top = 0; top < m; top++) {
            int[] sum = new int[n]; // 每一列的和
            
            for (int bottom = top; bottom < m; bottom++) {
                // 更新每一列的和
                for (int col = 0; col < n; col++) {
                    sum[col] += matrix[bottom][col];
                }
                
                // 在一维数组中找到最大子数组和不超过k的值
                // 使用TreeSet进行二分查找优化
                java.util.TreeSet<Integer> set = new java.util.TreeSet<>();
                set.add(0);
                int prefixSum = 0;
                
                for (int col = 0; col < n; col++) {
                    prefixSum += sum[col];
                    // 查找是否存在前缀和使得 prefixSum - previousPrefixSum <= k
                    // 即 previousPrefixSum >= prefixSum - k
                    java.util.SortedSet<Integer> tailSet = set.tailSet(prefixSum - k);
                    if (!tailSet.isEmpty()) {
                        result = Math.max(result, prefixSum - tailSet.first());
                    }
                    set.add(prefixSum);
                }
            }
        }
        
        return result;
    }
    
    // 打印复数列表
    public static void printComplexList(List<Complex> list, String name) {
        System.out.println(name + ":");
        for (Complex c : list) {
            System.out.println(c);
        }
    }
    
    // 打印整数列表
    public static void printLongList(List<Long> list, String name) {
        System.out.println(name + ":");
        for (Long l : list) {
            System.out.print(l + " ");
        }
        System.out.println();
    }
    
    // 测量执行时间
    public static long measureTime(Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        return (end - start) / 1_000_000; // 转换为毫秒
    }
    
    public static void main(String[] args) {
        // 测试DFT
        System.out.println("=== DFT测试 ===");
        List<Complex> a = Arrays.asList(
            new Complex(1, 0),
            new Complex(2, 0),
            new Complex(3, 0),
            new Complex(4, 0)
        );
        
        List<Complex> aDFT = dft(a, false);
        printComplexList(aDFT, "DFT结果");
        
        List<Complex> aIDFT = dft(aDFT, true);
        printComplexList(aIDFT, "逆DFT结果");
        
        // 测试多项式乘法
        System.out.println("\n=== 多项式乘法测试 ===");
        List<Long> poly1 = Arrays.asList(1L, 2L, 3L);
        List<Long> poly2 = Arrays.asList(4L, 5L, 6L);
        
        List<Long> resultDFT = multiplyPolynomialsDFT(poly1, poly2);
        printLongList(resultDFT, "DFT多项式乘法结果");
        
        List<Long> resultFFT = multiplyPolynomialsFFT(poly1, poly2);
        printLongList(resultFFT, "FFT多项式乘法结果");
        
        // 测试大数乘法（力扣第43题）
        System.out.println("\n=== 力扣第43题测试 ===");
        String num1 = "123";
        String num2 = "456";
        System.out.println(num1 + " * " + num2 + " = " + multiplyStrings(num1, num2));
        
        // 测试力扣第363题
        System.out.println("\n=== 力扣第363题测试 ===");
        int[][] matrix1 = {{1, 0, 1}, {0, -2, 3}};
        int k1 = 2;
        System.out.println("矩阵:");
        for (int[] row : matrix1) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
        System.out.println("k = " + k1);
        System.out.println("最大和不超过k的矩形和: " + maxSumSubmatrix(matrix1, k1));
        
        // 性能对比（简化版）
        System.out.println("\n=== 性能对比测试（简化）===");
        System.out.println("注意：在Java中，对于小规模数据，性能差异可能不明显");
        System.out.println("DFT时间复杂度: O(n²)");
        System.out.println("FFT时间复杂度: O(n log n)");
        
        /*
         * 离散傅里叶变换（DFT）算法解释：
         * 1. DFT将时域信号转换为频域表示
         * 2. 基本公式：X[k] = Σ(x[n] * e^(-2πikn/N)) for n = 0..N-1
         * 3. 逆变换公式：x[n] = (1/N) * Σ(X[k] * e^(2πikn/N)) for k = 0..N-1
         * 
         * 时间复杂度分析：
         * - 直接DFT：O(n²)，因为需要计算n个k值，每个k值需要n次乘法和加法
         * - FFT（快速傅里叶变换）：O(n log n)，利用了旋转因子的周期性和对称性
         * 
         * 空间复杂度：
         * - O(n)，需要存储输入和输出数组
         * 
         * 应用场景：
         * 1. 信号处理和频谱分析
         * 2. 图像处理中的卷积和滤波
         * 3. 多项式乘法（如本题中的应用）
         * 4. 密码学中的某些算法
         * 5. 量子计算中的量子傅里叶变换
         * 
         * 相关题目：
         * 1. 力扣第43题：字符串相乘 - 大数乘法，可以使用FFT优化
         * 2. 力扣第363题：矩形区域不超过 K 的最大数值和 - 二维前缀和的应用
         * 3. Codeforces 954I：Yet Another String Matching Problem - 字符串匹配问题
         * 4. Codeforces 914G：Sum the Fibonacci - 斐波那契数列相关的卷积问题
         */
    }
}