package class175.随机化与复杂度分析;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

/**
 * 高精度小数与格式化工具类
 * 提供各种高精度小数操作和格式化方法
 * 适用于需要极高精度计算和显示的场景
 */
public class HighPrecisionDecimal {
    
    /**
     * 将科学计数法表示的字符串转换为普通小数表示
     * 
     * @param scientificNotation 科学计数法字符串
     * @param precision 小数点后保留位数
     * @return 普通小数表示的字符串
     */
    public static String scientificToDecimal(String scientificNotation, int precision) {
        BigDecimal bd = new BigDecimal(scientificNotation);
        // 设置精度
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.toPlainString();
    }
    
    /**
     * 将大整数和小数字符串合并成一个高精度小数
     * 
     * @param integerPart 整数部分字符串
     * @param fractionalPart 小数部分字符串
     * @return BigDecimal表示的高精度小数
     */
    public static BigDecimal combineDecimalParts(String integerPart, String fractionalPart) {
        // 处理空字符串情况
        if (integerPart == null || integerPart.isEmpty()) {
            integerPart = "0";
        }
        if (fractionalPart == null || fractionalPart.isEmpty()) {
            fractionalPart = "0";
        }
        
        // 合并整数和小数部分
        StringBuilder sb = new StringBuilder();
        sb.append(integerPart);
        sb.append('.');
        sb.append(fractionalPart);
        
        return new BigDecimal(sb.toString());
    }
    
    /**
     * 格式化高精度小数，添加千位分隔符
     * 
     * @param value 高精度小数
     * @param fractionalDigits 小数位数
     * @return 格式化后的字符串
     */
    public static String formatWithSeparators(BigDecimal value, int fractionalDigits) {
        value = value.setScale(fractionalDigits, RoundingMode.HALF_UP);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.setGroupingUsed(true); // 启用千位分隔符
        formatter.setMinimumFractionDigits(fractionalDigits);
        formatter.setMaximumFractionDigits(fractionalDigits);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return formatter.format(value);
    }
    
    /**
     * 生成指定位数的随机高精度小数
     * 
     * @param integerDigits 整数部分位数
     * @param fractionalDigits 小数部分位数
     * @return 随机高精度小数
     */
    public static BigDecimal generateRandomDecimal(int integerDigits, int fractionalDigits) {
        // 生成整数部分（不能以0开头，除非只有一位且是0）
        StringBuilder integerPart = new StringBuilder();
        if (integerDigits > 0) {
            // 第一位不能是0
            integerPart.append((char) ('1' + Math.random() * 9));
            // 生成剩余的整数位
            for (int i = 1; i < integerDigits; i++) {
                integerPart.append((char) ('0' + Math.random() * 10));
            }
        } else {
            integerPart.append('0');
        }
        
        // 生成小数部分
        StringBuilder fractionalPart = new StringBuilder();
        for (int i = 0; i < fractionalDigits; i++) {
            fractionalPart.append((char) ('0' + Math.random() * 10));
        }
        
        // 合并并返回
        return combineDecimalParts(integerPart.toString(), fractionalPart.toString());
    }
    
    /**
     * 计算大数的平方根，保留指定精度
     * 
     * @param n 大整数
     * @param precision 精度（小数点后位数）
     * @return 平方根的高精度表示
     */
    public static BigDecimal calculateSquareRoot(BigInteger n, int precision) {
        // 对于负数，抛出异常
        if (n.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("无法计算负数的平方根");
        }
        // 对于0，直接返回0
        if (n.equals(BigInteger.ZERO)) {
            return BigDecimal.ZERO.setScale(precision, RoundingMode.HALF_UP);
        }
        
        // 使用牛顿迭代法计算平方根
        BigDecimal x = new BigDecimal(n.toString());
        int scale = precision + 5; // 多计算几位以保证精度
        
        // 初始猜测值：使用二分法的初始值
        BigDecimal low = BigDecimal.ZERO;
        BigDecimal high = x;
        BigDecimal mid = BigDecimal.ZERO;
        
        // 二分法找到初始近似值
        for (int i = 0; i < 30; i++) { // 30次迭代足以得到较好的初始值
            mid = low.add(high).divide(BigDecimal.valueOf(2), scale, RoundingMode.HALF_UP);
            BigDecimal midSquared = mid.multiply(mid);
            
            if (midSquared.compareTo(x) < 0) {
                low = mid;
            } else {
                high = mid;
            }
        }
        
        // 使用牛顿迭代法进一步提高精度
        for (int i = 0; i < 10; i++) { // 牛顿法收敛很快，10次迭代足够
            mid = mid.add(x.divide(mid, scale, RoundingMode.HALF_UP))
                     .divide(BigDecimal.valueOf(2), scale, RoundingMode.HALF_UP);
        }
        
        // 设置到所需精度
        return mid.setScale(precision, RoundingMode.HALF_UP);
    }
    
    /**
     * 比较两个高精度小数的大小
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @return a < b 返回-1，a = b 返回0，a > b 返回1
     */
    public static int compareDecimals(BigDecimal a, BigDecimal b) {
        return a.compareTo(b);
    }
    
    /**
     * 格式化高精度小数，显示为工程计数法
     * 
     * @param value 高精度小数
     * @param precision 有效数字位数
     * @return 工程计数法表示的字符串
     */
    public static String formatToEngineeringNotation(BigDecimal value, int precision) {
        // 设置科学计数法格式
        DecimalFormat formatter = new DecimalFormat("0.0000000000000000E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        
        // 转换为科学计数法
        String scientificString = formatter.format(value);
        
        // 解析科学计数法字符串
        int eIndex = scientificString.indexOf('E');
        String mantissaPart = scientificString.substring(0, eIndex);
        int exponent = Integer.parseInt(scientificString.substring(eIndex + 1));
        
        // 调整指数为3的倍数
        int remainder = exponent % 3;
        int newExponent = exponent - remainder;
        if (remainder < 0) {
            newExponent += 3;
            remainder += 3;
        }
        
        // 调整尾数
        BigDecimal mantissa = new BigDecimal(mantissaPart);
        BigDecimal adjustment = BigDecimal.TEN.pow(remainder);
        mantissa = mantissa.multiply(adjustment);
        
        // 格式化结果
        formatter.applyPattern("#." + "#".repeat(precision - 1) + "E0");
        String result = formatter.format(new BigDecimal(mantissa.toPlainString() + "E" + newExponent));
        
        // 将E替换为engineering notation中的符号
        return result.replace('E', 'e');
    }
    
    /**
     * 解析格式化的数字字符串（可能包含千位分隔符）为高精度小数
     * 
     * @param formattedString 格式化的数字字符串
     * @return 解析后的高精度小数
     */
    public static BigDecimal parseFormattedDecimal(String formattedString) {
        // 移除千位分隔符
        String cleanString = formattedString.replaceAll(",", "");
        // 解析为BigDecimal
        return new BigDecimal(cleanString);
    }
    
    /**
     * 对高精度小数进行舍入操作
     * 
     * @param value 原始值
     * @param scale 保留的小数位数
     * @param roundingMode 舍入模式
     * @return 舍入后的高精度小数
     */
    public static BigDecimal roundDecimal(BigDecimal value, int scale, RoundingMode roundingMode) {
        return value.setScale(scale, roundingMode);
    }
    
    /**
     * 计算高精度小数的幂，保留指定精度
     * 
     * @param base 底数
     * @param exponent 指数
     * @param precision 精度（小数点后位数）
     * @return 幂的高精度表示
     */
    public static BigDecimal calculatePower(BigDecimal base, int exponent, int precision) {
        if (exponent == 0) {
            return BigDecimal.ONE.setScale(precision, RoundingMode.HALF_UP);
        }
        
        // 使用快速幂算法
        boolean isNegative = exponent < 0;
        int absExponent = Math.abs(exponent);
        
        BigDecimal result = BigDecimal.ONE;
        BigDecimal currentBase = base;
        
        while (absExponent > 0) {
            if (absExponent % 2 == 1) {
                result = result.multiply(currentBase)
                              .setScale(precision + 5, RoundingMode.HALF_UP);
            }
            currentBase = currentBase.multiply(currentBase)
                                     .setScale(precision + 5, RoundingMode.HALF_UP);
            absExponent /= 2;
        }
        
        // 如果是负指数，取倒数
        if (isNegative) {
            result = BigDecimal.ONE.divide(result, precision + 5, RoundingMode.HALF_UP);
        }
        
        return result.setScale(precision, RoundingMode.HALF_UP);
    }
    
    /**
     * 测试高精度小数的各种操作
     */
    public static void testOperations() {
        System.out.println("=== 高精度小数操作测试 ===");
        
        // 测试科学计数法转换
        String scientific = "1.23456789E5";
        System.out.println("科学计数法: " + scientific);
        System.out.println("转为普通小数: " + scientificToDecimal(scientific, 10));
        
        // 测试合并整数和小数部分
        BigDecimal combined = combineDecimalParts("12345", "6789");
        System.out.println("\n合并整数和小数部分: " + combined.toPlainString());
        
        // 测试格式化
        BigDecimal value = new BigDecimal("1234567.890123456789");
        System.out.println("\n原始值: " + value.toPlainString());
        System.out.println("格式化(带千位分隔符): " + formatWithSeparators(value, 8));
        System.out.println("工程计数法: " + formatToEngineeringNotation(value, 10));
        
        // 测试随机数生成
        BigDecimal randomDecimal = generateRandomDecimal(8, 10);
        System.out.println("\n随机高精度小数: " + randomDecimal.toPlainString());
        
        // 测试平方根计算
        BigInteger bigNum = new BigInteger("123456789012345678901234567890");
        System.out.println("\n计算大数的平方根: sqrt(" + bigNum.toString() + ")");
        BigDecimal sqrt = calculateSquareRoot(bigNum, 20);
        System.out.println("平方根: " + sqrt.toPlainString());
        
        // 验证平方根计算的正确性
        BigDecimal squared = sqrt.multiply(sqrt);
        System.out.println("平方根的平方: " + squared.toPlainString());
        
        // 测试比较
        BigDecimal a = new BigDecimal("123.456");
        BigDecimal b = new BigDecimal("123.457");
        System.out.println("\n比较两个高精度小数: " + a + " 和 " + b);
        System.out.println("比较结果: " + compareDecimals(a, b));
        
        // 测试解析
        String formatted = "1,234,567.8901";
        System.out.println("\n解析格式化字符串: " + formatted);
        System.out.println("解析结果: " + parseFormattedDecimal(formatted).toPlainString());
        
        // 测试舍入
        BigDecimal valueToRound = new BigDecimal("123.456789");
        System.out.println("\n舍入测试: " + valueToRound.toPlainString());
        System.out.println("四舍五入到3位小数: " + roundDecimal(valueToRound, 3, RoundingMode.HALF_UP).toPlainString());
        System.out.println("向上舍入到2位小数: " + roundDecimal(valueToRound, 2, RoundingMode.CEILING).toPlainString());
        System.out.println("向下舍入到2位小数: " + roundDecimal(valueToRound, 2, RoundingMode.FLOOR).toPlainString());
        
        // 测试幂计算
        BigDecimal base = new BigDecimal("2.5");
        int exponent = 10;
        System.out.println("\n计算幂: " + base + "^" + exponent);
        System.out.println("结果: " + calculatePower(base, exponent, 10).toPlainString());
    }
    
    /**
     * 交互式测试函数
     */
    public static void interactiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== 高精度小数与格式化工具 ===");
        System.out.println("输入操作编号:");
        System.out.println("1. 科学计数法转普通小数");
        System.out.println("2. 合并整数和小数部分");
        System.out.println("3. 格式化带千位分隔符");
        System.out.println("4. 生成随机高精度小数");
        System.out.println("5. 计算大数平方根");
        System.out.println("6. 比较两个高精度小数");
        System.out.println("7. 工程计数法格式化");
        System.out.println("8. 解析格式化的数字");
        System.out.println("9. 小数舍入操作");
        System.out.println("10. 计算高精度小数的幂");
        System.out.println("0. 退出");
        
        while (true) {
            System.out.print("\n请输入操作编号: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
            
            try {
                switch (choice) {
                    case 0:
                        System.out.println("程序已退出");
                        scanner.close();
                        return;
                    case 1:
                        System.out.print("请输入科学计数法表示的数字: ");
                        String sciNotation = scanner.nextLine();
                        System.out.print("请输入保留的小数位数: ");
                        int precision1 = scanner.nextInt();
                        scanner.nextLine(); // 消耗换行符
                        System.out.println("转换结果: " + scientificToDecimal(sciNotation, precision1));
                        break;
                    case 2:
                        System.out.print("请输入整数部分: ");
                        String integerPart = scanner.nextLine();
                        System.out.print("请输入小数部分: ");
                        String fractionalPart = scanner.nextLine();
                        System.out.println("合并结果: " + combineDecimalParts(integerPart, fractionalPart).toPlainString());
                        break;
                    case 3:
                        System.out.print("请输入要格式化的数字: ");
                        String numToFormat = scanner.nextLine();
                        System.out.print("请输入小数位数: ");
                        int fracDigits = scanner.nextInt();
                        scanner.nextLine(); // 消耗换行符
                        System.out.println("格式化结果: " + formatWithSeparators(new BigDecimal(numToFormat), fracDigits));
                        break;
                    case 4:
                        System.out.print("请输入整数部分位数: ");
                        int intDigits = scanner.nextInt();
                        System.out.print("请输入小数部分位数: ");
                        int fracDigits2 = scanner.nextInt();
                        scanner.nextLine(); // 消耗换行符
                        System.out.println("随机小数: " + generateRandomDecimal(intDigits, fracDigits2).toPlainString());
                        break;
                    case 5:
                        System.out.print("请输入要计算平方根的整数: ");
                        String intToSqrt = scanner.nextLine();
                        System.out.print("请输入保留的小数位数: ");
                        int precision2 = scanner.nextInt();
                        scanner.nextLine(); // 消耗换行符
                        System.out.println("平方根: " + calculateSquareRoot(new BigInteger(intToSqrt), precision2).toPlainString());
                        break;
                    case 6:
                        System.out.print("请输入第一个数字: ");
                        String num1 = scanner.nextLine();
                        System.out.print("请输入第二个数字: ");
                        String num2 = scanner.nextLine();
                        int result = compareDecimals(new BigDecimal(num1), new BigDecimal(num2));
                        String resultText = (result < 0) ? "小于" : (result > 0) ? "大于" : "等于";
                        System.out.println(num1 + " " + resultText + " " + num2);
                        break;
                    case 7:
                        System.out.print("请输入要格式化的数字: ");
                        String numToEng = scanner.nextLine();
                        System.out.print("请输入有效数字位数: ");
                        int sigDigits = scanner.nextInt();
                        scanner.nextLine(); // 消耗换行符
                        System.out.println("工程计数法表示: " + formatToEngineeringNotation(new BigDecimal(numToEng), sigDigits));
                        break;
                    case 8:
                        System.out.print("请输入要解析的格式化数字字符串: ");
                        String formattedStr = scanner.nextLine();
                        System.out.println("解析结果: " + parseFormattedDecimal(formattedStr).toPlainString());
                        break;
                    case 9:
                        System.out.print("请输入要舍入的数字: ");
                        String numToRound = scanner.nextLine();
                        System.out.print("请输入保留的小数位数: ");
                        int scale = scanner.nextInt();
                        scanner.nextLine(); // 消耗换行符
                        System.out.println("四舍五入: " + roundDecimal(new BigDecimal(numToRound), scale, RoundingMode.HALF_UP).toPlainString());
                        System.out.println("向上舍入: " + roundDecimal(new BigDecimal(numToRound), scale, RoundingMode.CEILING).toPlainString());
                        System.out.println("向下舍入: " + roundDecimal(new BigDecimal(numToRound), scale, RoundingMode.FLOOR).toPlainString());
                        break;
                    case 10:
                        System.out.print("请输入底数: ");
                        String baseStr = scanner.nextLine();
                        System.out.print("请输入指数: ");
                        int exponent = scanner.nextInt();
                        System.out.print("请输入保留的小数位数: ");
                        int precision3 = scanner.nextInt();
                        scanner.nextLine(); // 消耗换行符
                        System.out.println("幂计算结果: " + calculatePower(new BigDecimal(baseStr), exponent, precision3).toPlainString());
                        break;
                    default:
                        System.out.println("无效的操作编号，请重新输入");
                }
            } catch (Exception e) {
                System.out.println("操作出错: " + e.getMessage());
                scanner.nextLine(); // 消耗错误输入后的换行符
            }
        }
    }
    
    public static void main(String[] args) {
        // 运行测试
        testOperations();
        
        // 启动交互模式
        interactiveMode();
    }
}