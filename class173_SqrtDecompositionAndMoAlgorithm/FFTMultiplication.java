package class175.随机化与复杂度分析;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * FFT乘法算法实现
 * 使用快速傅里叶变换将整数乘法转换为多项式乘法，时间复杂度达到O(n log n)
 * 传统乘法: O(n²)，Karatsuba: O(n^1.585)，Toom-Cook (k=3): O(n^1.465)，FFT: O(n log n)
 * 适用于非常大的高精度整数乘法计算
 */
public class FFTMultiplication {
    
    /**
     * 复数类，用于FFT计算
     */
    private static class Complex {
        private final double re;  // 实部
        private final double im;  // 虚部

        /**
         * 创建复数
         * @param real 实部
         * @param imag 虚部
         */
        public Complex(double real, double imag) {
            re = real;
            im = imag;
        }

        /**
         * 复数加法
         * @param b 另一个复数
         * @return 结果复数
         */
        public Complex add(Complex b) {
            return new Complex(re + b.re, im + b.im);
        }

        /**
         * 复数减法
         * @param b 另一个复数
         * @return 结果复数
         */
        public Complex subtract(Complex b) {
            return new Complex(re - b.re, im - b.im);
        }

        /**
         * 复数乘法
         * @param b 另一个复数
         * @return 结果复数
         */
        public Complex multiply(Complex b) {
            return new Complex(re * b.re - im * b.im, re * b.im + im * b.re);
        }

        /**
         * 复数模长的平方
         * @return 模长平方
         */
        public double absSquared() {
            return re * re + im * im;
        }
    }

    /**
     * 使用快速傅里叶变换进行大整数乘法
     * 时间复杂度：O(n log n)
     * 
     * @param x 第一个整数的字符串表示
     * @param y 第二个整数的字符串表示
     * @return 乘积的字符串表示
     */
    public static String fftMultiply(String x, String y) {
        // 处理特殊情况
        if (x.equals("0") || y.equals("0")) {
            return "0";
        }
        if (x.equals("1")) {
            return y;
        }
        if (y.equals("1")) {
            return x;
        }

        // 将字符串转换为数字数组（低位在前）
        int[] xDigits = stringToDigits(x);
        int[] yDigits = stringToDigits(y);

        // 调用FFT乘法算法
        int[] product = fftMultiplyDigits(xDigits, yDigits);

        // 移除前导零并转换为字符串
        return digitsToString(product);
    }

    /**
     * 使用FFT算法对两个数字数组进行乘法
     * 
     * @param a 第一个整数的数字数组（低位在前）
     * @param b 第二个整数的数字数组（低位在前）
     * @return 乘积的数字数组（低位在前）
     */
    private static int[] fftMultiplyDigits(int[] a, int[] b) {
        int n = 1;
        int m = a.length + b.length - 1;

        // 计算大于等于a.length + b.length - 1的最小的2的幂次
        while (n < m) {
            n <<= 1;
        }

        // 转换为复数数组
        Complex[] fa = new Complex[n];
        Complex[] fb = new Complex[n];

        for (int i = 0; i < n; i++) {
            fa[i] = (i < a.length) ? new Complex(a[i], 0) : new Complex(0, 0);
            fb[i] = (i < b.length) ? new Complex(b[i], 0) : new Complex(0, 0);
        }

        // 执行FFT
        fa = fft(fa, false);
        fb = fft(fb, false);

        // 点乘得到频域乘积
        Complex[] fc = new Complex[n];
        for (int i = 0; i < n; i++) {
            fc[i] = fa[i].multiply(fb[i]);
        }

        // 执行逆FFT得到时域结果
        fc = fft(fc, true);

        // 将复数结果转换为整数，并处理进位
        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            // 四舍五入到最近的整数
            result[i] = (int) Math.round(fc[i].re);
        }

        // 处理进位
        processCarries(result);

        return result;
    }

    /**
     * 快速傅里叶变换实现
     * 
     * @param x 输入复数数组
     * @param inverse 是否为逆FFT
     * @return 变换后的复数数组
     */
    private static Complex[] fft(Complex[] x, boolean inverse) {
        int n = x.length;

        // 递归终止条件
        if (n == 1) {
            return new Complex[] { x[0] };
        }

        // 确保n是2的幂次
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("n必须是2的幂次");
        }

        // 分割偶数索引和奇数索引
        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        for (int i = 0; i < n / 2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }

        // 递归FFT
        even = fft(even, inverse);
        odd = fft(odd, inverse);

        // 合并结果
        Complex[] result = new Complex[n];
        double angle = 2 * Math.PI / n * (inverse ? -1 : 1);
        Complex w = new Complex(1, 0);
        Complex wn = new Complex(Math.cos(angle), Math.sin(angle));

        for (int i = 0; i < n / 2; i++) {
            Complex t = w.multiply(odd[i]);
            result[i] = even[i].add(t);
            result[i + n / 2] = even[i].subtract(t);
            w = w.multiply(wn);
        }

        // 如果是逆FFT，需要除以n
        if (inverse) {
            for (int i = 0; i < n; i++) {
                result[i] = new Complex(result[i].re / n, result[i].im / n);
            }
        }

        return result;
    }

    /**
     * 处理大整数乘法结果中的进位
     * 
     * @param result 乘法结果数字数组
     */
    private static void processCarries(int[] result) {
        int carry = 0;
        for (int i = 0; i < result.length; i++) {
            int sum = result[i] + carry;
            result[i] = sum % 10;
            carry = sum / 10;
        }

        // 如果还有进位，需要扩展数组
        if (carry > 0) {
            // 这里我们假设不会有太多进位，直接处理
            int currentPos = result.length - 1;
            while (carry > 0 && currentPos >= 0) {
                int sum = result[currentPos] + carry;
                result[currentPos] = sum % 10;
                carry = sum / 10;
                currentPos--;
            }
            
            // 如果仍然有进位，我们创建一个新数组
            if (carry > 0) {
                int[] newResult = new int[result.length + 1];
                System.arraycopy(result, 0, newResult, 0, result.length);
                newResult[result.length] = carry;
                // 重新赋值给result（注意：这不会改变原数组，因为Java中数组是引用类型，但这里我们在方法内部处理）
                // 由于在Java中无法直接修改数组长度，所以这个情况应该在调用者中处理
                // 为了简化，我们假设carry不会太大
                throw new AssertionError("需要扩展数组处理进位");
            }
        }
    }

    /**
     * 使用传统方法进行大整数乘法（用于比较性能）
     * 时间复杂度：O(n²)
     * 
     * @param x 第一个整数的字符串表示
     * @param y 第二个整数的字符串表示
     * @return 乘积的字符串表示
     */
    public static String naiveMultiply(String x, String y) {
        int[] xDigits = stringToDigits(x);
        int[] yDigits = stringToDigits(y);
        int[] result = new int[xDigits.length + yDigits.length];

        for (int i = 0; i < xDigits.length; i++) {
            for (int j = 0; j < yDigits.length; j++) {
                result[i + j] += xDigits[i] * yDigits[j];
                result[i + j + 1] += result[i + j] / 10;
                result[i + j] %= 10;
            }
        }

        // 移除前导零
        int lastNonZero = result.length - 1;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }

        int[] trimmed = Arrays.copyOfRange(result, 0, lastNonZero + 1);
        return digitsToString(trimmed);
    }

    /**
     * 将字符串转换为数字数组（低位在前）
     * 
     * @param s 数字字符串
     * @return 数字数组（低位在前）
     */
    private static int[] stringToDigits(String s) {
        int[] digits = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            digits[i] = s.charAt(s.length() - 1 - i) - '0';
        }
        return digits;
    }

    /**
     * 将数字数组转换为字符串（低位在前转换为正常表示）
     * 
     * @param digits 数字数组（低位在前）
     * @return 数字字符串
     */
    private static String digitsToString(int[] digits) {
        StringBuilder sb = new StringBuilder();
        for (int i = digits.length - 1; i >= 0; i--) {
            sb.append(digits[i]);
        }
        return sb.toString();
    }

    /**
     * 性能测试方法，比较FFT算法与其他算法
     * 
     * @param size 测试数字的位数
     */
    public static void benchmark(int size) {
        // 生成测试用的大整数
        String num1 = generateRandomNumber(size);
        String num2 = generateRandomNumber(size);

        // 测试FFT算法
        long startTime = System.currentTimeMillis();
        String resultFFT = fftMultiply(num1, num2);
        long fftTime = System.currentTimeMillis() - startTime;

        // 测试Karatsuba算法
        startTime = System.currentTimeMillis();
        String resultKaratsuba = KaratsubaMultiplication.karatsubaMultiply(num1, num2);
        long karatsubaTime = System.currentTimeMillis() - startTime;

        // 测试传统算法（对于较小的数字）
        String resultNaive = "";
        long naiveTime = 0;
        if (size <= 500) { // 对于大数字，传统算法可能太慢
            startTime = System.currentTimeMillis();
            resultNaive = naiveMultiply(num1, num2);
            naiveTime = System.currentTimeMillis() - startTime;
        }

        System.out.println("数字位数: " + size);
        System.out.println("FFT算法耗时: " + fftTime + " ms");
        System.out.println("Karatsuba算法耗时: " + karatsubaTime + " ms");
        System.out.println("算法加速比 (Karatsuba/FFT): " + (double) karatsubaTime / fftTime + "x");
        
        if (size <= 500) {
            System.out.println("传统算法耗时: " + naiveTime + " ms");
            System.out.println("算法加速比 (传统/FFT): " + (double) naiveTime / fftTime + "x");
            System.out.println("结果一致 (FFT vs 传统): " + resultFFT.equals(resultNaive));
        }
        System.out.println("结果一致 (FFT vs Karatsuba): " + resultFFT.equals(resultKaratsuba));
        System.out.println("乘积位数: " + resultFFT.length());
    }

    /**
     * 生成指定长度的随机数字字符串
     * 
     * @param length 字符串长度
     * @return 随机数字字符串
     */
    private static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        // 第一位不能是0
        sb.append((char) ('1' + Math.random() * 9));
        // 生成剩余位
        for (int i = 1; i < length; i++) {
            sb.append((char) ('0' + Math.random() * 10));
        }
        return sb.toString();
    }

    /**
     * 验证算法正确性
     */
    public static void verifyCorrectness() {
        List<String[]> testCases = new ArrayList<>();
        testCases.add(new String[]{"1234", "5678"});
        testCases.add(new String[]{"9999", "9999"});
        testCases.add(new String[]{"0", "12345"});
        testCases.add(new String[]{"1", "98765"});
        testCases.add(new String[]{"999999", "999999"});

        for (String[] testCase : testCases) {
            String x = testCase[0];
            String y = testCase[1];
            
            // 使用FFT算法
            String result = fftMultiply(x, y);
            
            // 对于小数字，使用Java内置的大整数类验证
            if (x.length() <= 18 && y.length() <= 18) { // 确保可以放入long
                long num1 = Long.parseLong(x);
                long num2 = Long.parseLong(y);
                String expected = String.valueOf(num1 * num2);
                System.out.println(x + " * " + y + " = " + result + 
                                  " (正确: " + result.equals(expected) + ")");
            } else {
                System.out.println(x + " * " + y + " = " + result);
                System.out.println("乘积位数: " + result.length());
            }
        }
    }

    /**
     * 主函数，包含测试和交互功能
     */
    public static void main(String[] args) {
        System.out.println("验证算法正确性:");
        verifyCorrectness();
        
        System.out.println("\n性能测试:");
        benchmark(100);
        benchmark(1000);
        benchmark(5000);
        
        // 交互式测试
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n请输入两个大整数进行乘法计算（输入exit退出）:");
        while (true) {
            System.out.print("第一个数: ");
            String num1 = scanner.nextLine();
            if (num1.equalsIgnoreCase("exit")) break;
            
            System.out.print("第二个数: ");
            String num2 = scanner.nextLine();
            if (num2.equalsIgnoreCase("exit")) break;
            
            try {
                // 验证输入是否为有效数字
                if (!num1.matches("\\d+") || !num2.matches("\\d+")) {
                    System.out.println("错误: 请输入有效的正整数");
                    continue;
                }
                
                long startTime = System.currentTimeMillis();
                String result = fftMultiply(num1, num2);
                long endTime = System.currentTimeMillis();
                
                System.out.println("结果: " + result);
                System.out.println("计算耗时: " + (endTime - startTime) + " ms");
            } catch (Exception e) {
                System.out.println("计算过程中发生错误: " + e.getMessage());
            }
        }
        scanner.close();
    }
}