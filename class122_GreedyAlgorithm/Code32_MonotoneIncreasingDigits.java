package class091;

/**
 * 单调递增的数字
 * 
 * 题目描述：
 * 给定一个非负整数 N，找出小于或等于 N 的最大的整数，同时这个整数需要满足其各个位数上的数字是单调递增。
 * （当且仅当每个相邻位数上的数字 x 和 y 满足 x <= y 时，我们称这个整数是单调递增的。）
 * 
 * 来源：LeetCode 738
 * 链接：https://leetcode.cn/problems/monotone-increasing-digits/
 * 
 * 算法思路：
 * 使用贪心算法：
 * 1. 将数字转换为字符数组方便处理
 * 2. 从右向左遍历，找到第一个不满足单调递增的位置
 * 3. 将该位置数字减1，并将后面所有数字设为9
 * 4. 继续向左检查，确保整个数字单调递增
 * 
 * 时间复杂度：O(logN) - 数字的位数
 * 空间复杂度：O(logN) - 字符数组的空间
 * 
 * 关键点分析：
 * - 贪心策略：找到第一个不满足条件的位置进行调整
 * - 数字处理：字符数组操作和转换
 * - 边界处理：处理0和边界情况
 * 
 * 工程化考量：
 * - 输入验证：检查数字是否非负
 * - 性能优化：避免不必要的转换
 * - 可读性：清晰的变量命名和注释
 */
public class Code32_MonotoneIncreasingDigits {
    
    /**
     * 找到小于等于N的最大单调递增数字
     * 
     * @param N 输入数字
     * @return 最大的单调递增数字
     */
    public static int monotoneIncreasingDigits(int N) {
        // 输入验证
        if (N < 0) {
            throw new IllegalArgumentException("输入数字必须是非负整数");
        }
        if (N < 10) {
            return N; // 单个数字总是单调递增的
        }
        
        // 将数字转换为字符数组
        char[] digits = String.valueOf(N).toCharArray();
        int n = digits.length;
        
        // 标记需要修改的位置
        int mark = n;
        
        // 从右向左遍历，找到第一个不满足单调递增的位置
        for (int i = n - 1; i > 0; i--) {
            if (digits[i] < digits[i - 1]) {
                mark = i;
                digits[i - 1]--; // 前一位数字减1
            }
        }
        
        // 将mark位置及后面的所有数字设为9
        for (int i = mark; i < n; i++) {
            digits[i] = '9';
        }
        
        // 转换回数字
        return Integer.parseInt(new String(digits));
    }
    
    /**
     * 另一种实现：使用数学运算而非字符数组
     * 时间复杂度：O(logN)
     * 空间复杂度：O(1)
     */
    public static int monotoneIncreasingDigitsMath(int N) {
        if (N < 0) {
            throw new IllegalArgumentException("输入数字必须是非负整数");
        }
        if (N < 10) {
            return N;
        }
        
        int result = N;
        int power = 1;
        int prevDigit = 9; // 初始设为9，确保第一次比较正确
        
        while (result > 0) {
            int currentDigit = result % 10;
            result /= 10;
            
            if (currentDigit > prevDigit) {
                // 需要调整：当前数字太大，需要减1并将后面设为9
                result = result * 10 + currentDigit - 1;
                // 将后面的数字都设为9
                int temp = result;
                int ninePower = power / 10;
                while (ninePower > 0) {
                    temp = temp * 10 + 9;
                    ninePower /= 10;
                }
                result = temp;
                prevDigit = 9; // 重置为9
            } else {
                prevDigit = currentDigit;
            }
            power *= 10;
        }
        
        return result;
    }
    
    /**
     * 递归解法
     * 时间复杂度：O(logN)
     * 空间复杂度：O(logN) - 递归栈深度
     */
    public static int monotoneIncreasingDigitsRecursive(int N) {
        if (N < 0) {
            throw new IllegalArgumentException("输入数字必须是非负整数");
        }
        if (N < 10) {
            return N;
        }
        
        char[] digits = String.valueOf(N).toCharArray();
        return Integer.parseInt(helper(digits, 0));
    }
    
    private static String helper(char[] digits, int index) {
        if (index == digits.length - 1) {
            return String.valueOf(digits[index]);
        }
        
        // 递归处理后面的数字
        String rest = helper(digits, index + 1);
        
        // 如果当前数字大于后面数字的首位，需要调整
        if (digits[index] > rest.charAt(0)) {
            // 当前数字减1，后面全部设为9
            if (digits[index] > '1') {
                // 当前数字可以减1
                StringBuilder sb = new StringBuilder();
                sb.append((char)(digits[index] - 1));
                for (int i = 0; i < digits.length - index - 1; i++) {
                    sb.append('9');
                }
                return sb.toString();
            } else {
                // 当前数字是1，不能减1，需要特殊处理
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < digits.length - index - 1; i++) {
                    sb.append('9');
                }
                return sb.toString();
            }
        } else {
            // 当前数字可以保持不变
            return digits[index] + rest;
        }
    }
    
    /**
     * 验证数字是否单调递增
     * 
     * @param num 要验证的数字
     * @return 是否单调递增
     */
    public static boolean isMonotoneIncreasing(int num) {
        if (num < 10) {
            return true;
        }
        
        String s = String.valueOf(num);
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) < s.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: N = 10 -> 9
        int N1 = 10;
        System.out.println("测试用例1: N = " + N1);
        System.out.println("方法1结果: " + monotoneIncreasingDigits(N1)); // 9
        System.out.println("方法2结果: " + monotoneIncreasingDigitsMath(N1)); // 9
        System.out.println("方法3结果: " + monotoneIncreasingDigitsRecursive(N1)); // 9
        System.out.println("验证: " + isMonotoneIncreasing(monotoneIncreasingDigits(N1))); // true
        
        // 测试用例2: N = 1234 -> 1234
        int N2 = 1234;
        System.out.println("\n测试用例2: N = " + N2);
        System.out.println("方法1结果: " + monotoneIncreasingDigits(N2)); // 1234
        System.out.println("方法2结果: " + monotoneIncreasingDigitsMath(N2)); // 1234
        System.out.println("方法3结果: " + monotoneIncreasingDigitsRecursive(N2)); // 1234
        System.out.println("验证: " + isMonotoneIncreasing(monotoneIncreasingDigits(N2))); // true
        
        // 测试用例3: N = 332 -> 299
        int N3 = 332;
        System.out.println("\n测试用例3: N = " + N3);
        System.out.println("方法1结果: " + monotoneIncreasingDigits(N3)); // 299
        System.out.println("方法2结果: " + monotoneIncreasingDigitsMath(N3)); // 299
        System.out.println("方法3结果: " + monotoneIncreasingDigitsRecursive(N3)); // 299
        System.out.println("验证: " + isMonotoneIncreasing(monotoneIncreasingDigits(N3))); // true
        
        // 测试用例4: N = 100 -> 99
        int N4 = 100;
        System.out.println("\n测试用例4: N = " + N4);
        System.out.println("方法1结果: " + monotoneIncreasingDigits(N4)); // 99
        System.out.println("方法2结果: " + monotoneIncreasingDigitsMath(N4)); // 99
        System.out.println("方法3结果: " + monotoneIncreasingDigitsRecursive(N4)); // 99
        System.out.println("验证: " + isMonotoneIncreasing(monotoneIncreasingDigits(N4))); // true
        
        // 测试用例5: N = 9 -> 9
        int N5 = 9;
        System.out.println("\n测试用例5: N = " + N5);
        System.out.println("方法1结果: " + monotoneIncreasingDigits(N5)); // 9
        System.out.println("方法2结果: " + monotoneIncreasingDigitsMath(N5)); // 9
        System.out.println("方法3结果: " + monotoneIncreasingDigitsRecursive(N5)); // 9
        System.out.println("验证: " + isMonotoneIncreasing(monotoneIncreasingDigits(N5))); // true
        
        // 边界测试：N = 0
        int N6 = 0;
        System.out.println("\n测试用例6: N = " + N6);
        System.out.println("方法1结果: " + monotoneIncreasingDigits(N6)); // 0
        System.out.println("方法2结果: " + monotoneIncreasingDigitsMath(N6)); // 0
        System.out.println("方法3结果: " + monotoneIncreasingDigitsRecursive(N6)); // 0
        System.out.println("验证: " + isMonotoneIncreasing(monotoneIncreasingDigits(N6))); // true
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        int largeN = 1000000000; // 10亿
        
        System.out.println("\n=== 性能测试 ===");
        
        long startTime1 = System.currentTimeMillis();
        int result1 = monotoneIncreasingDigits(largeN);
        long endTime1 = System.currentTimeMillis();
        System.out.println("方法1执行时间: " + (endTime1 - startTime1) + "ms");
        System.out.println("结果: " + result1);
        System.out.println("验证: " + isMonotoneIncreasing(result1));
        
        long startTime2 = System.currentTimeMillis();
        int result2 = monotoneIncreasingDigitsMath(largeN);
        long endTime2 = System.currentTimeMillis();
        System.out.println("方法2执行时间: " + (endTime2 - startTime2) + "ms");
        System.out.println("结果: " + result2);
        System.out.println("验证: " + isMonotoneIncreasing(result2));
        
        long startTime3 = System.currentTimeMillis();
        int result3 = monotoneIncreasingDigitsRecursive(largeN);
        long endTime3 = System.currentTimeMillis();
        System.out.println("方法3执行时间: " + (endTime3 - startTime3) + "ms");
        System.out.println("结果: " + result3);
        System.out.println("验证: " + isMonotoneIncreasing(result3));
    }
    
    /**
     * 算法复杂度分析
     */
    public static void analyzeComplexity() {
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("方法1（字符数组）:");
        System.out.println("- 时间复杂度: O(logN)");
        System.out.println("  - 数字位数: O(logN)");
        System.out.println("  - 遍历处理: O(logN)");
        System.out.println("- 空间复杂度: O(logN)");
        System.out.println("  - 字符数组: O(logN)");
        
        System.out.println("\n方法2（数学运算）:");
        System.out.println("- 时间复杂度: O(logN)");
        System.out.println("  - 数字位数: O(logN)");
        System.out.println("  - 数学运算: O(logN)");
        System.out.println("- 空间复杂度: O(1)");
        System.out.println("  - 只使用常数空间");
        
        System.out.println("\n方法3（递归）:");
        System.out.println("- 时间复杂度: O(logN)");
        System.out.println("  - 递归深度: O(logN)");
        System.out.println("  - 每次递归操作: O(1)");
        System.out.println("- 空间复杂度: O(logN)");
        System.out.println("  - 递归栈深度: O(logN)");
        
        System.out.println("\n贪心策略证明:");
        System.out.println("1. 找到第一个不满足条件的位置是最优调整点");
        System.out.println("2. 将该位置减1，后面设为9可以保证得到最大可能值");
        System.out.println("3. 数学归纳法证明贪心选择性质");
        
        System.out.println("\n工程化考量:");
        System.out.println("1. 输入验证：处理负数和边界情况");
        System.out.println("2. 性能优化：选择合适的数字处理方法");
        System.out.println("3. 可读性：清晰的算法逻辑和注释");
        System.out.println("4. 测试覆盖：全面的测试用例设计");
    }
}