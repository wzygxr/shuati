package class175.随机化与复杂度分析;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.math.BigInteger;

/**
 * Karatsuba乘法算法实现
 * 
 * Karatsuba算法是一种高效的大整数乘法算法，时间复杂度为O(n^log₂3) ≈ O(n^1.585)，
 * 优于传统的O(n²)复杂度算法。算法基于分治思想，通过减少乘法操作次数来提高效率。
 * 
 * 算法原理：
 * 对于两个n位数a和b，可以将它们分为高低两部分：
 * a = a₁ × 10^(n/2) + a₀
 * b = b₁ × 10^(n/2) + b₀
 * 
 * 则a×b = (a₁ × 10^(n/2) + a₀) × (b₁ × 10^(n/2) + b₀)
 *       = a₁b₁ × 10^n + (a₁b₀ + a₀b₁) × 10^(n/2) + a₀b₀
 * 
 * 传统方法需要计算4次乘法：a₁×b₁, a₁×b₀, a₀×b₁, a₀×b₀
 * 
 * Karatsuba的优化之处在于只计算3次乘法：
 * z₁ = a₁ × b₁
 * z₂ = (a₁ + a₀) × (b₁ + b₀)
 * z₃ = a₀ × b₀
 * 
 * 然后通过加减法得到中间项：a₁b₀ + a₀b₁ = z₂ - z₁ - z₃
 * 
 * 最终结果：a×b = z₁ × 10^n + (z₂ - z₁ - z₃) × 10^(n/2) + z₃
 * 
 * 虽然增加了加减法操作，但减少了一次乘法，对于大数乘法，乘法操作的开销远大于加减法。
 * 这种分治策略使得算法复杂度从O(n²)降低到O(n^log₂3) ≈ O(n^1.585)。
 */
public class KaratsubaMultiplication {

    /**
     * 传统的大整数乘法算法（O(n²)复杂度）
     * 作为Karatsuba算法的基础情况，当数字位数较小时使用
     * 
     * @param x 第一个整数的数字数组表示（低位在前）
     * @param y 第二个整数的数字数组表示（低位在前）
     * @return 乘积的数字数组表示（低位在前）
     */
    public static int[] naiveMultiply(int[] x, int[] y) {
        int n = x.length;
        int m = y.length;
        
        // 创建结果数组，长度最大为n+m
        int[] result = new int[n + m];

        // 传统的O(n²)乘法算法
        // 逐位相乘并累加
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 每一位相乘，并累加到对应的位置
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
     * 使用Karatsuba算法进行大整数乘法，支持负数
     * 时间复杂度：O(n^log₂3) ≈ O(n^1.585)
     * 
     * @param x 第一个整数的字符串表示，可以是负数
     * @param y 第二个整数的字符串表示，可以是负数
     * @return 乘积的字符串表示
     */
    public static String karatsubaMultiply(String x, String y) {
        // 处理负数情况
        boolean isNegative = false;
        if (x.startsWith("-")) {
            isNegative = !isNegative;
            x = x.substring(1);
        }
        if (y.startsWith("-")) {
            isNegative = !isNegative;
            y = y.substring(1);
        }

        // 处理特殊情况
        if (x.equals("0") || y.equals("0")) {
            return "0";
        }
        if (x.equals("1")) {
            return isNegative ? "-" + y : y;
        }
        if (y.equals("1")) {
            return isNegative ? "-" + x : x;
        }

        // 将字符串转换为数字数组（低位在前）
        int[] xDigits = stringToDigits(x);
        int[] yDigits = stringToDigits(y);

        // 调整数组长度为相等且为2的幂（以优化分治过程）
        int maxLength = Math.max(xDigits.length, yDigits.length);
        int n = 1;
        while (n < maxLength) {
            n <<= 1; // 向上取最近的2的幂
        }

        xDigits = padArray(xDigits, n);
        yDigits = padArray(yDigits, n);

        // 调用递归Karatsuba算法
        int[] product = karatsubaMultiplyRecursive(xDigits, yDigits);

        // 移除前导零并转换为字符串
        String result = digitsToString(product);
        
        // 添加负号（如果需要）
        return isNegative ? "-" + result : result;
    }

    /**
     * Karatsuba递归乘法实现
     * 
     * @param x 第一个整数的数字数组表示（低位在前）
     * @param y 第二个整数的数字数组表示（低位在前）
     * @return 乘积的数字数组表示（低位在前）
     * 
     * 算法原理：
     * 对于两个大数x和y，将其分为高半部分和低半部分：
     * x = a*10^(n/2) + b，其中a是高位部分，b是低位部分
     * y = c*10^(n/2) + d，其中c是高位部分，d是低位部分
     * 
     * 计算三个主要乘积：
     * 1. z1 = a*c（高位部分相乘）
     * 2. z3 = b*d（低位部分相乘）
     * 3. z2 = (a+b)*(c+d)（交叉项计算）
     * 
     * 然后通过z2-z1-z3得到交叉乘积项，最终组合结果：
     * x*y = z1*10^n + (z2-z1-z3)*10^(n/2) + z3
     */
    private static int[] karatsubaMultiplyRecursive(int[] x, int[] y) {
        int n = x.length;
        
        // 基础情况：当数组长度较小时，使用传统乘法算法以避免递归开销
        if (n <= 64) { // 阈值可以根据实际情况调整
            return naiveMultiply(x, y);
        }

        // 计算中点位置，将数组分为高低两部分
        int m = n / 2;

        // 分割数组为高位和低位部分
        int[] a = Arrays.copyOfRange(x, m, n); // x的高位部分
        int[] b = Arrays.copyOfRange(x, 0, m); // x的低位部分
        int[] c = Arrays.copyOfRange(y, m, n); // y的高位部分
        int[] d = Arrays.copyOfRange(y, 0, m); // y的低位部分

        // 计算三个核心乘积
        // 1. z1 = a * c (高位乘高位)
        int[] z1 = karatsubaMultiplyRecursive(a, c);
        
        // 2. z3 = b * d (低位乘低位)
        int[] z3 = karatsubaMultiplyRecursive(b, d);
        
        // 3. z2 = (a + b) * (c + d) (组合项)
        int[] sumAandB = addArrays(a, b); // a + b
        int[] sumCandD = addArrays(c, d); // c + d
        int[] z2 = karatsubaMultiplyRecursive(sumAandB, sumCandD);
        
        // 计算中间交叉项：(a+b)*(c+d) - a*c - b*d = a*d + b*c
        z2 = subtractArrays(z2, addArrays(z1, z3));

        // 组合最终结果: z1 * 10^n + (z2-z1-z3) * 10^m + z3
        // 创建足够大的结果数组
        int[] result = new int[2 * n];
        
        // 添加z3到结果的最低位部分
        addToResult(result, z3, 0);
        
        // 添加中间项 (z2-z1-z3) * 10^m
        addToResult(result, z2, m);
        
        // 添加最高位部分 z1 * 10^(2*m)
        addToResult(result, z1, 2 * m);

        // 移除结果数组中的前导零
        int lastNonZero = result.length - 1;
        while (lastNonZero > 0 && result[lastNonZero] == 0) {
            lastNonZero--;
        }

        // 返回有效部分
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
        // 确保不会数组越界
        for (int i = 0; i < addend.length && (i + offset) < result.length; i++) {
            result[i + offset] += addend[i];
        }
        
        // 处理进位
        carryPropagation(result);
    }

    /**
     * 处理数组中的进位
     * 
     * @param array 需要处理进位的数组
     */
    private static void carryPropagation(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            array[i + 1] += array[i] / 10;
            array[i] %= 10;
        }
        
        // 处理最高位进位
        if (array[array.length - 1] >= 10) {
            // 如果最高位有进位，需要创建新数组
            // 注意：这个情况在当前实现中应该不会发生，因为我们预先分配了足够大的数组
            // 但为了鲁棒性，保留此检查
            int carry = array[array.length - 1] / 10;
            array[array.length - 1] %= 10;
            // 这里不扩展数组，因为我们已经有了足够的空间
        }
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
        int[] result = new int[maxLength + 1]; // 额外一位用于处理最高位进位

        // 逐位相加
        for (int i = 0; i < maxLength; i++) {
            int digitA = (i < a.length) ? a[i] : 0;
            int digitB = (i < b.length) ? b[i] : 0;
            result[i] = digitA + digitB;

            // 处理进位
            result[i + 1] += result[i] / 10;
            result[i] %= 10;
        }

        // 移除前导零
        int lastNonZero = result.length - 1;
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

        // 逐位相减
        for (int i = 0; i < b.length; i++) {
            result[i] = a[i] - b[i];
        }
        
        // 处理a中剩余的高位
        for (int i = b.length; i < a.length; i++) {
            result[i] = a[i];
        }

        // 处理借位
        for (int i = 0; i < result.length - 1; i++) {
            while (result[i] < 0) {
                result[i] += 10;  // 当前位借位
                result[i + 1]--;  // 高位减1
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
     * 将字符串转换为数字数组（低位在前）
     * 例如："1234" 转换为 [4, 3, 2, 1]
     * 
     * @param s 数字字符串
     * @return 数字数组（低位在前）
     */
    private static int[] stringToDigits(String s) {
        int[] digits = new int[s.length()];
        // 反向读取字符串，低位在前
        for (int i = 0; i < s.length(); i++) {
            digits[i] = s.charAt(s.length() - 1 - i) - '0';
        }
        return digits;
    }

    /**
     * 将数字数组转换为字符串（低位在前转换为正常表示）
     * 例如：[4, 3, 2, 1] 转换为 "1234"
     * 
     * @param digits 数字数组（低位在前）
     * @return 数字字符串
     */
    private static String digitsToString(int[] digits) {
        StringBuilder sb = new StringBuilder();
        // 反向遍历数组，生成正确的数字表示
        for (int i = digits.length - 1; i >= 0; i--) {
            sb.append(digits[i]);
        }
        return sb.toString();
    }

    /**
     * 填充数组到指定长度
     * 
     * @param arr 原始数组
     * @param length 目标长度
     * @return 填充后的数组
     */
    private static int[] padArray(int[] arr, int length) {
        if (arr.length >= length) {
            return arr;
        }
        int[] padded = new int[length];
        System.arraycopy(arr, 0, padded, 0, arr.length);
        // 新添加的元素默认为0，无需额外设置
        return padded;
    }

    /**
     * 性能测试方法，比较Karatsuba算法与传统算法和Java内置BigInteger
     * 
     * @param size 测试数字的位数
     */
    public static void benchmark(int size) {
        // 生成测试用的大整数
        String num1 = generateRandomNumber(size);
        String num2 = generateRandomNumber(size);

        // 测试Karatsuba算法
        long startTime = System.currentTimeMillis();
        String resultKaratsuba = karatsubaMultiply(num1, num2);
        long karatsubaTime = System.currentTimeMillis() - startTime;

        // 测试传统算法（对于较小的数字）
        String resultNaive = "";
        long naiveTime = 0;
        if (size <= 1000) { // 对于大数字，传统算法可能太慢
            int[] digits1 = stringToDigits(num1);
            int[] digits2 = stringToDigits(num2);
            startTime = System.currentTimeMillis();
            int[] naiveResult = naiveMultiply(digits1, digits2);
            resultNaive = digitsToString(naiveResult);
            naiveTime = System.currentTimeMillis() - startTime;
        }

        // 测试Java内置的BigInteger（用于验证结果和性能比较）
        long bigIntegerTime = 0;
        boolean resultsMatch = true;
        if (size <= 10000) { // 对于非常大的数字，BigInteger可能也会很慢
            startTime = System.currentTimeMillis();
            BigInteger bigNum1 = new BigInteger(num1);
            BigInteger bigNum2 = new BigInteger(num2);
            BigInteger bigResult = bigNum1.multiply(bigNum2);
            String expected = bigResult.toString();
            bigIntegerTime = System.currentTimeMillis() - startTime;
            resultsMatch = resultKaratsuba.equals(expected);
        }

        System.out.println("数字位数: " + size);
        System.out.println("Karatsuba算法耗时: " + karatsubaTime + " ms");
        if (size <= 1000) {
            System.out.println("传统算法耗时: " + naiveTime + " ms");
            System.out.println("Karatsuba vs 传统算法加速比: " + String.format("%.2f", (double) naiveTime / karatsubaTime) + "x");
        }
        if (size <= 10000) {
            System.out.println("Java BigInteger耗时: " + bigIntegerTime + " ms");
            System.out.println("结果正确性验证: " + (resultsMatch ? "正确" : "错误"));
        }
        System.out.println("乘积位数: " + resultKaratsuba.length());
        System.out.println();
    }

    /**
     * 生成指定长度的随机数字字符串
     * 
     * @param length 字符串长度
     * @return 随机数字字符串
     */
    private static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        
        // 第一位不能是0，确保生成的是有效的数字
        sb.append((char) ('1' + random.nextInt(9)));
        
        // 生成剩余位，可以是0-9的任意数字
        for (int i = 1; i < length; i++) {
            sb.append((char) ('0' + random.nextInt(10)));
        }
        return sb.toString();
    }

    /**
     * 验证算法正确性
     * 测试各种边界情况和常见情况，确保Karatsuba算法在所有情况下都能正确工作
     */
    public static void verifyCorrectness() {
        List<String[]> testCases = new ArrayList<>();
        
        // 边界情况
        testCases.add(new String[]{"0", "12345"});            // 乘以0
        testCases.add(new String[]{"12345", "0"});            // 0乘以
        testCases.add(new String[]{"1", "98765"});            // 乘以1
        testCases.add(new String[]{"98765", "1"});            // 1乘以
        testCases.add(new String[]{"-1234", "5678"});         // 负数乘正数
        testCases.add(new String[]{"1234", "-5678"});         // 正数乘负数
        testCases.add(new String[]{"-1234", "-5678"});        // 负数乘负数
        
        // 常见测试用例
        testCases.add(new String[]{"1234", "5678"});          // 普通数字相乘
        testCases.add(new String[]{"9999", "9999"});          // 大数相乘
        testCases.add(new String[]{"999999", "999999"});      // 更大的数字相乘
        testCases.add(new String[]{"123456789", "987654321"}); // 长数字相乘
        
        // 不同位数的数字
        testCases.add(new String[]{"123", "45678"});          // 位数不同
        testCases.add(new String[]{"999999999", "1"});        // 大数乘1

        System.out.println("=== 算法正确性验证 ===");
        for (String[] testCase : testCases) {
            String x = testCase[0];
            String y = testCase[1];
            
            // 使用Karatsuba算法
            String result = karatsubaMultiply(x, y);
            
            // 使用Java内置的大整数类验证
            BigInteger num1 = new BigInteger(x);
            BigInteger num2 = new BigInteger(y);
            String expected = num1.multiply(num2).toString();
            
            boolean correct = result.equals(expected);
            System.out.println(x + " * " + y + " = " + result);
            System.out.println("  验证结果: " + (correct ? "✓ 正确" : "✗ 错误") + 
                             (correct ? "" : " (期望值: " + expected + ")"));
            System.out.println();
        }
        System.out.println("=== 验证完成 ===");
    }

    /**
     * 主方法，用于运行验证和性能测试
     */
    public static void main(String[] args) {
        System.out.println("Karatsuba乘法算法实现\n");
        
        // 验证算法正确性
        verifyCorrectness();
        
        System.out.println("\n=== 性能测试 ===");
        System.out.println("注意：对于非常大的数字，测试可能需要较长时间\n");
        
        // 性能测试 - 测试不同大小的数字
        benchmark(100);    // 100位数字
        benchmark(500);    // 500位数字
        benchmark(1000);   // 1000位数字
        benchmark(2000);   // 2000位数字
        
        // 交互式测试
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== 交互式测试 ===");
        System.out.println("请输入两个大整数进行乘法计算（输入exit退出）:");
        
        while (true) {
            System.out.print("第一个数: ");
            String num1 = scanner.nextLine();
            if (num1.equalsIgnoreCase("exit")) break;
            
            System.out.print("第二个数: ");
            String num2 = scanner.nextLine();
            if (num2.equalsIgnoreCase("exit")) break;
            
            try {
                long startTime = System.currentTimeMillis();
                String result = karatsubaMultiply(num1, num2);
                long endTime = System.currentTimeMillis();
                
                System.out.println("结果: " + result);
                System.out.println("计算耗时: " + (endTime - startTime) + " ms");
                System.out.println("乘积位数: " + result.length());
            } catch (Exception e) {
                System.out.println("计算错误: " + e.getMessage());
            }
            System.out.println();
        }
        scanner.close();
        System.out.println("程序结束");
    }
}