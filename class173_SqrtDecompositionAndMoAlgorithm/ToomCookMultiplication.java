package class175.随机化与复杂度分析;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Toom-Cook乘法算法实现
 * 是Karatsuba算法的一般化，通过更高阶的分治策略进一步降低时间复杂度
 * 传统乘法: O(n²)，Karatsuba: O(n^1.585)，Toom-Cook (k=3): O(n^1.465)
 * 适用于高精度大整数乘法计算
 */
public class ToomCookMultiplication {

    /**
     * 使用传统方法进行大整数乘法
     * 时间复杂度：O(n²)
     * 作为Toom-Cook算法的基础情况
     * 
     * @param x 第一个整数的数字数组表示（低位在前）
     * @param y 第二个整数的数字数组表示（低位在前）
     * @return 乘积的数字数组表示（低位在前）
     */
    public static int[] naiveMultiply(int[] x, int[] y) {
        int n = x.length;
        int m = y.length;
        int[] result = new int[n + m];

        // 逐位相乘并累加
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i + j] += x[i] * y[j];
                // 处理进位
                result[i + j + 1] += result[i + j] / 10;
                result[i + j] %= 10;
            }
        }

        // 移除前导零
        int lastNonZero = n + m - 1;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }

        return Arrays.copyOfRange(result, 0, lastNonZero + 1);
    }

    /**
     * 使用Toom-Cook乘法算法进行大整数乘法
     * 这里实现了Toom-3算法，是Toom-Cook的三阶版本
     * 时间复杂度：O(n^log₃5) ≈ O(n^1.465)
     * 
     * @param x 第一个整数的字符串表示
     * @param y 第二个整数的字符串表示
     * @return 乘积的字符串表示
     */
    public static String toomCookMultiply(String x, String y) {
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

        // 调用递归Toom-Cook算法
        int[] product = toomCookMultiplyRecursive(xDigits, yDigits);

        // 移除前导零并转换为字符串
        return digitsToString(product);
    }

    /**
     * Toom-Cook (Toom-3) 递归乘法实现
     * 
     * @param x 第一个整数的数字数组表示（低位在前）
     * @param y 第二个整数的数字数组表示（低位在前）
     * @return 乘积的数字数组表示（低位在前）
     */
    private static int[] toomCookMultiplyRecursive(int[] x, int[] y) {
        int n = Math.max(x.length, y.length);
        
        // 基础情况：当数组长度较小时，使用传统乘法
        if (n <= 128) { // 阈值可以根据实际情况调整
            return naiveMultiply(x, y);
        }

        // 计算分割点，将数字分成三个部分
        int m = (n + 2) / 3; // 向上取整到3

        // 分割x为三个部分: x = x2*10^(2*m) + x1*10^m + x0
        int[] x0 = Arrays.copyOfRange(x, 0, Math.min(m, x.length));
        int[] x1 = (m < x.length) ? Arrays.copyOfRange(x, m, Math.min(2*m, x.length)) : new int[0];
        int[] x2 = (2*m < x.length) ? Arrays.copyOfRange(x, 2*m, x.length) : new int[0];

        // 分割y为三个部分: y = y2*10^(2*m) + y1*10^m + y0
        int[] y0 = Arrays.copyOfRange(y, 0, Math.min(m, y.length));
        int[] y1 = (m < y.length) ? Arrays.copyOfRange(y, m, Math.min(2*m, y.length)) : new int[0];
        int[] y2 = (2*m < y.length) ? Arrays.copyOfRange(y, 2*m, y.length) : new int[0];

        // 计算f(k)和g(k)在k=-1,0,1,2,∞处的值
        // f(0) = x0, f(1) = x0+x1+x2, f(-1) = x0-x1+x2, f(2) = x0+2x1+4x2, f(∞) = x2
        // g(0) = y0, g(1) = y0+y1+y2, g(-1) = y0-y1+y2, g(2) = y0+2y1+4y2, g(∞) = y2
        int[] f0 = x0;
        int[] f1 = addArrays(addArrays(x0, x1), x2);
        int[] fNeg1 = addArrays(subtractArrays(x0, x1), x2);
        int[] f2 = addArrays(addArrays(x0, multiplyByPowerOfTwo(x1, 1)), multiplyByPowerOfTwo(x2, 2));
        int[] fInfty = x2;

        int[] g0 = y0;
        int[] g1 = addArrays(addArrays(y0, y1), y2);
        int[] gNeg1 = addArrays(subtractArrays(y0, y1), y2);
        int[] g2 = addArrays(addArrays(y0, multiplyByPowerOfTwo(y1, 1)), multiplyByPowerOfTwo(y2, 2));
        int[] gInfty = y2;

        // 计算乘积h(k) = f(k)*g(k) 在各点的值
        int[] h0 = toomCookMultiplyRecursive(f0, g0); // h(0) = x0*y0
        int[] h1 = toomCookMultiplyRecursive(f1, g1); // h(1) = (x0+x1+x2)*(y0+y1+y2)
        int[] hNeg1 = toomCookMultiplyRecursive(fNeg1, gNeg1); // h(-1) = (x0-x1+x2)*(y0-y1+y2)
        int[] h2 = toomCookMultiplyRecursive(f2, g2); // h(2) = (x0+2x1+4x2)*(y0+2y1+4y2)
        int[] hInfty = toomCookMultiplyRecursive(fInfty, gInfty); // h(∞) = x2*y2

        // 使用拉格朗日插值法求解多项式系数
        // h(z) = z^4*h_4 + z^3*h_3 + z^2*h_2 + z*h_1 + h_0
        // 其中h_4 = hInfty

        // 计算中间值
        int[] a = h1; // h(1)
        int[] b = hNeg1; // h(-1)
        int[] c = h2; // h(2)
        int[] d = h0; // h(0)

        // 通过插值公式计算h3, h2, h1
        // h3 = (c - 8a + 6b - d) / 6
        int[] term1 = subtractArrays(c, multiplyByPowerOfTwo(a, 3)); // c - 8a
        int[] term2 = multiplyByPowerOfTwo(b, 2) + multiplyByPowerOfTwo(b, 1); // 6b
        int[] numerator = subtractArrays(addArrays(term1, term2), d);
        int[] h3 = divideBySix(numerator);

        // h2 = (a + b - 2d - 6h3 - 2h4) / 2
        int[] h4 = hInfty;
        int[] term3 = addArrays(a, b);
        int[] term4 = addArrays(multiplyByPowerOfTwo(d, 1), multiplyByPowerOfTwo(multiplyByPowerOfTwo(h3, 2) + h3, 1)); // 2d + 6h3
        int[] term5 = multiplyByPowerOfTwo(h4, 1); // 2h4
        numerator = subtractArrays(subtractArrays(term3, term4), term5);
        int[] h2 = divideByTwo(numerator);

        // h1 = (a - b) / 2 - 2h3 - 3h4
        term1 = divideByTwo(subtractArrays(a, b));
        term2 = addArrays(multiplyByPowerOfTwo(h3, 1), multiplyByPowerOfTwo(h4, 1) + h4); // 2h3 + 3h4
        int[] h1_coeff = subtractArrays(term1, term2);

        // 现在我们有了所有系数：h4, h3, h2, h1, h0
        // 组合结果: h4*10^(4*m) + h3*10^(3*m) + h2*10^(2*m) + h1*10^m + h0
        int[] result = new int[5 * m]; // 可能需要调整大小

        // 添加各部分到结果中
        addToResult(result, h0, 0);
        addToResult(result, h1_coeff, m);
        addToResult(result, h2, 2 * m);
        addToResult(result, h3, 3 * m);
        addToResult(result, h4, 4 * m);

        // 移除前导零
        int lastNonZero = result.length - 1;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }

        return Arrays.copyOfRange(result, 0, lastNonZero + 1);
    }

    /**
     * 将数字数组添加到结果数组的指定位置
     * 
     * @param result 结果数组
     * @param addend 要添加的数字数组
     * @param offset 起始位置
     */
    private static void addToResult(int[] result, int[] addend, int offset) {
        int newLength = Math.max(result.length, addend.length + offset);
        if (newLength > result.length) {
            result = Arrays.copyOf(result, newLength);
        }

        for (int i = 0; i < addend.length; i++) {
            if (i + offset < result.length) {
                result[i + offset] += addend[i];
                // 处理进位
                propagateCarry(result, i + offset);
            }
        }
    }

    /**
     * 处理进位传播
     * 
     * @param arr 数字数组
     * @param pos 起始处理位置
     */
    private static void propagateCarry(int[] arr, int pos) {
        while (pos < arr.length - 1 && arr[pos] >= 10) {
            arr[pos + 1] += arr[pos] / 10;
            arr[pos] %= 10;
            pos++;
        }
        // 处理最高位的进位（如果需要）
        // 在这个方法中，我们假设arr足够大，不需要扩展
    }

    /**
     * 对两个数字数组进行加法操作
     * 
     * @param a 第一个数字数组（低位在前）
     * @param b 第二个数字数组（低位在前）
     * @return 和的数字数组（低位在前）
     */
    private static int[] addArrays(int[] a, int[] b) {
        int maxLength = Math.max(a.length, b.length);
        int[] result = new int[maxLength + 1]; // 预留进位空间

        for (int i = 0; i < maxLength; i++) {
            int digitA = (i < a.length) ? a[i] : 0;
            int digitB = (i < b.length) ? b[i] : 0;
            result[i] = digitA + digitB;

            // 处理进位
            result[i + 1] += result[i] / 10;
            result[i] %= 10;
        }

        // 移除前导零
        int lastNonZero = maxLength;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }

        return Arrays.copyOfRange(result, 0, lastNonZero + 1);
    }

    /**
     * 对两个数字数组进行减法操作
     * 假设 a >= b
     * 
     * @param a 被减数的数字数组（低位在前）
     * @param b 减数的数字数组（低位在前）
     * @return 差的数字数组（低位在前）
     */
    private static int[] subtractArrays(int[] a, int[] b) {
        int[] result = new int[a.length];

        for (int i = 0; i < b.length; i++) {
            result[i] = a[i] - b[i];
        }
        
        for (int i = b.length; i < a.length; i++) {
            result[i] = a[i];
        }

        // 处理借位
        for (int i = 0; i < result.length - 1; i++) {
            while (result[i] < 0) {
                result[i] += 10;
                result[i + 1]--;
            }
        }

        // 移除前导零
        int lastNonZero = result.length - 1;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }

        return Arrays.copyOfRange(result, 0, lastNonZero + 1);
    }

    /**
     * 将数字数组乘以2的幂（乘以2^power）
     * 
     * @param arr 数字数组（低位在前）
     * @param power 2的幂次
     * @return 结果数组（低位在前）
     */
    private static int[] multiplyByPowerOfTwo(int[] arr, int power) {
        int[] result = Arrays.copyOf(arr, arr.length);
        
        for (int p = 0; p < power; p++) {
            int carry = 0;
            for (int i = 0; i < result.length; i++) {
                int product = result[i] * 2 + carry;
                result[i] = product % 10;
                carry = product / 10;
            }
            if (carry > 0) {
                result = Arrays.copyOf(result, result.length + 1);
                result[result.length - 1] = carry;
            }
        }
        
        return result;
    }

    /**
     * 将数字数组除以2
     * 
     * @param arr 数字数组（低位在前）
     * @return 结果数组（低位在前）
     */
    private static int[] divideByTwo(int[] arr) {
        int[] result = new int[arr.length];
        int remainder = 0;
        
        // 从高位开始除（数组的末尾）
        for (int i = arr.length - 1; i >= 0; i--) {
            int current = arr[i] + remainder * 10;
            result[i] = current / 2;
            remainder = current % 2;
        }
        
        // 移除前导零
        int lastNonZero = result.length - 1;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }
        
        return Arrays.copyOfRange(result, 0, lastNonZero + 1);
    }

    /**
     * 将数字数组除以6
     * 
     * @param arr 数字数组（低位在前）
     * @return 结果数组（低位在前）
     */
    private static int[] divideBySix(int[] arr) {
        int[] result = new int[arr.length];
        int remainder = 0;
        
        // 从高位开始除（数组的末尾）
        for (int i = arr.length - 1; i >= 0; i--) {
            int current = arr[i] + remainder * 10;
            result[i] = current / 6;
            remainder = current % 6;
        }
        
        // 移除前导零
        int lastNonZero = result.length - 1;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }
        
        return Arrays.copyOfRange(result, 0, lastNonZero + 1);
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
     * 性能测试方法，比较Toom-Cook算法与其他算法
     * 
     * @param size 测试数字的位数
     */
    public static void benchmark(int size) {
        // 生成测试用的大整数
        String num1 = generateRandomNumber(size);
        String num2 = generateRandomNumber(size);

        // 测试Toom-Cook算法
        long startTime = System.currentTimeMillis();
        String resultToomCook = toomCookMultiply(num1, num2);
        long toomCookTime = System.currentTimeMillis() - startTime;

        // 测试Karatsuba算法
        startTime = System.currentTimeMillis();
        String resultKaratsuba = KaratsubaMultiplication.karatsubaMultiply(num1, num2);
        long karatsubaTime = System.currentTimeMillis() - startTime;

        // 测试传统算法（对于较小的数字）
        String resultNaive = "";
        long naiveTime = 0;
        if (size <= 500) { // 对于大数字，传统算法可能太慢
            int[] digits1 = stringToDigits(num1);
            int[] digits2 = stringToDigits(num2);
            startTime = System.currentTimeMillis();
            int[] naiveResult = naiveMultiply(digits1, digits2);
            resultNaive = digitsToString(naiveResult);
            naiveTime = System.currentTimeMillis() - startTime;
        }

        System.out.println("数字位数: " + size);
        System.out.println("Toom-Cook算法耗时: " + toomCookTime + " ms");
        System.out.println("Karatsuba算法耗时: " + karatsubaTime + " ms");
        System.out.println("算法加速比 (Karatsuba/Toom-Cook): " + (double) karatsubaTime / toomCookTime + "x");
        
        if (size <= 500) {
            System.out.println("传统算法耗时: " + naiveTime + " ms");
            System.out.println("算法加速比 (传统/Toom-Cook): " + (double) naiveTime / toomCookTime + "x");
            System.out.println("结果一致 (Toom-Cook vs 传统): " + resultToomCook.equals(resultNaive));
        }
        System.out.println("结果一致 (Toom-Cook vs Karatsuba): " + resultToomCook.equals(resultKaratsuba));
        System.out.println("乘积位数: " + resultToomCook.length());
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
            
            // 使用Toom-Cook算法
            String result = toomCookMultiply(x, y);
            
            // 对于小数字，使用Java内置的大整数类验证
            if (x.length() <= 20 && y.length() <= 20) { // 确保可以放入long
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
            
            long startTime = System.currentTimeMillis();
            String result = toomCookMultiply(num1, num2);
            long endTime = System.currentTimeMillis();
            
            System.out.println("结果: " + result);
            System.out.println("计算耗时: " + (endTime - startTime) + " ms");
        }
        scanner.close();
    }
}