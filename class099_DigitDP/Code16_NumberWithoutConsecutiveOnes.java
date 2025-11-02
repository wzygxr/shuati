package class084;

/**
 * 不含连续1的非负整数
 * 题目来源：LeetCode 600. 不含连续1的非负整数
 * 题目链接：https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/
 * 
 * 题目描述：
 * 给定一个正整数 n，返回在 [0, n] 范围内不含连续1的非负整数的个数。
 * 
 * 解题思路：
 * 1. 数位DP方法：使用数位DP框架，逐位确定二进制数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 是否受上界限制
 *    - 前一位是否为1
 * 3. 关键点：当前位不能与前一位同时为1
 * 
 * 时间复杂度分析：
 * - 状态数：二进制位数 × 2 × 2 ≈ 32 × 4 = 128
 * - 每个状态处理2种选择
 * - 总复杂度：O(256) 非常高效
 * 
 * 空间复杂度分析：
 * - 记忆化数组：32 × 2 × 2 ≈ 128个状态
 * 
 * 最优解分析：
 * 这是标准的最优解，利用数位DP处理二进制约束条件
 */

public class Code16_NumberWithoutConsecutiveOnes {
    
    /**
     * 计算[0, n]中不含连续1的二进制数的个数
     * 时间复杂度: O(log n)
     * 空间复杂度: O(log n)
     */
    public static int findIntegers(int n) {
        if (n == 0) return 1;
        if (n == 1) return 2;
        
        // 将n转换为二进制字符串
        String binary = Integer.toBinaryString(n);
        int len = binary.length();
        
        // 记忆化数组：dp[pos][isLimit][lastIsOne]
        Integer[][][] dp = new Integer[len][2][2];
        
        return dfs(binary.toCharArray(), 0, true, false, dp);
    }
    
    /**
     * 数位DP递归函数（二进制版本）
     * 
     * @param bits 二进制位数组
     * @param pos 当前处理位置
     * @param isLimit 是否受到上界限制
     * @param lastIsOne 前一位是否为1
     * @param dp 记忆化数组
     * @return 满足条件的数字个数
     */
    private static int dfs(char[] bits, int pos, boolean isLimit, boolean lastIsOne, Integer[][][] dp) {
        // 递归终止条件：处理完所有二进制位
        if (pos == bits.length) {
            return 1; // 成功构造一个有效数字
        }
        
        // 记忆化搜索
        int limitIndex = isLimit ? 1 : 0;
        int lastIndex = lastIsOne ? 1 : 0;
        if (dp[pos][limitIndex][lastIndex] != null) {
            return dp[pos][limitIndex][lastIndex];
        }
        
        int ans = 0;
        
        // 确定当前位可选数字范围（二进制只有0和1）
        int up = isLimit ? (bits[pos] - '0') : 1;
        
        // 枚举当前位可选数字
        for (int d = 0; d <= up; d++) {
            // 检查约束条件：不能有连续的1
            if (lastIsOne && d == 1) {
                continue; // 连续1，跳过
            }
            
            // 递归处理下一位
            ans += dfs(bits, pos + 1, isLimit && (d == up), d == 1, dp);
        }
        
        // 记忆化存储
        dp[pos][limitIndex][lastIndex] = ans;
        return ans;
    }
    
    /**
     * 数学方法（斐波那契数列）- 更高效的解法
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * 解题思路：
     * 1. 观察发现，不含连续1的二进制数个数满足斐波那契数列
     * 2. 对于k位二进制数，有效数字个数为fib(k+2)
     * 3. 利用这个性质可以快速计算
     */
    public static int findIntegersMath(int n) {
        if (n == 0) return 1;
        if (n == 1) return 2;
        
        // 预处理斐波那契数列
        int[] fib = new int[32];
        fib[0] = 1;
        fib[1] = 2;
        for (int i = 2; i < 32; i++) {
            fib[i] = fib[i-1] + fib[i-2];
        }
        
        String binary = Integer.toBinaryString(n);
        int len = binary.length();
        int ans = 0;
        boolean prevBit = false; // 前一位是否为1
        
        for (int i = 0; i < len; i++) {
            if (binary.charAt(i) == '1') {
                // 如果当前位为1，可以选择填0，后面位可以任意填
                ans += fib[len - i - 1];
                
                // 如果前一位也是1，说明出现了连续1，后面的数字都不满足条件
                if (prevBit) {
                    return ans;
                }
                prevBit = true;
            } else {
                prevBit = false;
            }
        }
        
        // 加上n本身（如果n本身满足条件）
        return ans + 1;
    }
    
    /**
     * 单元测试函数
     */
    public static void testFindIntegers() {
        System.out.println("=== 测试不含连续1的非负整数 ===");
        
        // 测试用例1: 小数字
        int n1 = 5;
        int result1 = findIntegers(n1);
        int result1Math = findIntegersMath(n1);
        System.out.println("n = " + n1);
        System.out.println("DP结果: " + result1);
        System.out.println("数学结果: " + result1Math);
        System.out.println("结果一致: " + (result1 == result1Math));
        System.out.println("预期: [0,5]中不含连续1的数字有0,1,2,4,5共5个");
        System.out.println();
        
        // 测试用例2: 中等数字
        int n2 = 10;
        int result2 = findIntegers(n2);
        int result2Math = findIntegersMath(n2);
        System.out.println("n = " + n2);
        System.out.println("DP结果: " + result2);
        System.out.println("数学结果: " + result2Math);
        System.out.println("结果一致: " + (result2 == result2Math));
        System.out.println();
        
        // 测试用例3: 边界情况
        int n3 = 1;
        int result3 = findIntegers(n3);
        int result3Math = findIntegersMath(n3);
        System.out.println("n = " + n3);
        System.out.println("DP结果: " + result3);
        System.out.println("数学结果: " + result3Math);
        System.out.println("结果一致: " + (result3 == result3Math));
        System.out.println("预期: [0,1]中所有数字都满足，共2个");
        System.out.println();
    }
    
    /**
     * 性能测试函数
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        int[] testCases = {100, 1000, 10000, 100000, 1000000, 10000000};
        
        for (int n : testCases) {
            long startTimeDP = System.nanoTime();
            int resultDP = findIntegers(n);
            long endTimeDP = System.nanoTime();
            
            long startTimeMath = System.nanoTime();
            int resultMath = findIntegersMath(n);
            long endTimeMath = System.nanoTime();
            
            long timeDP = endTimeDP - startTimeDP;
            long timeMath = endTimeMath - startTimeMath;
            
            System.out.println("n = " + n);
            System.out.println("DP方法耗时: " + timeDP + "ns");
            System.out.println("数学方法耗时: " + timeMath + "ns");
            System.out.println("加速比: " + (double)timeDP / timeMath + "倍");
            System.out.println("结果一致: " + (resultDP == resultMath));
            System.out.println();
        }
    }
    
    /**
     * 调试函数：验证特定范围内的结果
     */
    public static void debugFindIntegers() {
        System.out.println("=== 调试不含连续1的非负整数 ===");
        
        for (int n = 0; n <= 20; n++) {
            int count = 0;
            StringBuilder validNumbers = new StringBuilder();
            
            for (int i = 0; i <= n; i++) {
                String binary = Integer.toBinaryString(i);
                if (!binary.contains("11")) {
                    count++;
                    if (validNumbers.length() < 50) { // 限制输出长度
                        validNumbers.append(i).append(" ");
                    }
                }
            }
            
            int dpResult = findIntegers(n);
            int mathResult = findIntegersMath(n);
            
            System.out.println("n = " + n + ", 有效数字个数: " + count);
            System.out.println("DP结果: " + dpResult + ", 数学结果: " + mathResult);
            System.out.println("结果一致: " + (count == dpResult && dpResult == mathResult));
            
            if (n <= 10) {
                System.out.println("有效数字: " + validNumbers);
            }
            System.out.println();
        }
    }
    
    /**
     * 工程化考量总结：
     * 1. 两种解法：提供DP和数学两种解法，便于理解和选择
     * 2. 性能优化：数学方法更高效，DP方法更通用
     * 3. 边界处理：正确处理n=0和n=1的情况
     * 4. 状态设计：合理设计状态参数，减少状态数
     * 5. 测试覆盖：全面的测试用例
     * 
     * 算法特色：
     * 1. 二进制处理：针对二进制数的特殊约束
     * 2. 斐波那契性质：发现并利用数学规律
     * 3. 记忆化搜索：DP解法避免重复计算
     * 4. 提前终止：数学解法在发现连续1时提前返回
     */
    
    public static void main(String[] args) {
        // 运行功能测试
        testFindIntegers();
        
        // 运行性能测试
        performanceTest();
        
        // 调试模式
        debugFindIntegers();
        
        // 边界测试
        System.out.println("=== 边界测试 ===");
        System.out.println("n=0: " + findIntegers(0));
        System.out.println("n=1: " + findIntegers(1));
        System.out.println("n=2: " + findIntegers(2));
        System.out.println("n=3: " + findIntegers(3));
    }
}