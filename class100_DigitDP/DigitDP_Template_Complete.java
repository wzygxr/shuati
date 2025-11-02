package class085;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 数位DP通用模板 (Java版本)
 * 
 * 数位DP是一种用于解决与数字的数位相关问题的动态规划技术。
 * 它通常用于统计某个区间内满足特定条件的数字个数，或者计算这些数字的某种属性总和。
 * 
 * 核心思想：
 * 1. 将问题转化为计算[0, n]范围内满足条件的数字个数，然后利用前缀和思想计算[a, b]区间的结果
 * 2. 逐位处理数字，使用记忆化搜索避免重复计算
 * 3. 状态设计通常包括：
 *    - 当前处理到第几位
 *    - 前一位数字（或前面的状态）
 *    - 是否受到上界限制
 *    - 是否有前导零
 *    - 其他题目相关的状态
 * 
 * 时间复杂度：通常为O(log n * 状态数)
 * 空间复杂度：O(状态数)
 * 
 * 应用场景：
 * - 统计特定数字出现次数（如LeetCode 233）
 * - 统计满足数位条件的数字个数（如不含连续1的数字）
 * - 统计各位数字不同的数字个数（如LeetCode 2376）
 * - 统计包含或不包含特定子串的数字个数
 * 
 * 作者：algorithm-journey
 * 日期：2024
 */
public class DigitDP_Template_Complete {
    // 存储数字的各位
    private int[] digits;
    // 记忆化数组 - 对于不同的问题需要不同维度的记忆化数组
    private long[][][][] memo; // 位置，已使用数字mask，是否受限(0/1)，是否已经开始选择数字(0/1)
    
    /**
     * 数位DP核心函数 - 统计各位数字不重复的数字个数
     * 
     * @param pos 当前处理到第几位（从0开始）
     * @param mask 已使用的数字状态（用位运算表示），每一位表示对应数字是否已使用
     * @param isLimit 是否受到上界限制
     * @param isNum 是否已经开始选择数字（处理前导零）
     * @return 从当前状态到末尾可构造的满足条件的数字个数
     */
    private long dfs(int pos, int mask, boolean isLimit, boolean isNum) {
        // 递归终止条件：处理完所有数位
        if (pos == digits.length) {
            // 只有在已经开始选择数字的情况下才算一个有效数字
            return isNum ? 1 : 0;
        }
        
        // 如果当前状态已经被计算过且不受限制，则直接返回缓存的结果
        if (!isLimit && memo[pos][mask][isNum ? 1 : 0][0] != -1) {
            return memo[pos][mask][isNum ? 1 : 0][0];
        }
        // 如果当前状态已经被计算过且受限制，则直接返回缓存的结果
        if (isLimit && memo[pos][mask][isNum ? 1 : 0][1] != -1) {
            return memo[pos][mask][isNum ? 1 : 0][1];
        }
        
        long result = 0;
        
        // 如果还没有开始选择数字，可以继续跳过（处理前导零）
        if (!isNum) {
            result += dfs(pos + 1, mask, false, false);
        }
        
        // 确定当前位可以填入的数字范围
        int up = isLimit ? digits[pos] : 9;
        // 确定起始数字：如果还没开始选数字，则从1开始（避免前导零）
        int start = isNum ? 0 : 1;
        
        // 枚举当前位可以填入的数字
        for (int digit = start; digit <= up; ++digit) {
            // 约束条件：避免重复数字
            if ((mask >> digit & 1) == 1) {
                continue;
            }
            
            // 递归处理下一位，更新状态
            // 新的limit状态：只有当前受限且填的数字等于上限时，下一位才受限
            // 新的isNum状态：当前已经开始选择数字
            result += dfs(
                pos + 1, 
                mask | (1 << digit),  // 标记当前数字已使用
                isLimit && (digit == up), 
                true
            );
        }
        
        // 缓存结果
        if (isLimit) {
            memo[pos][mask][isNum ? 1 : 0][1] = result;
        } else {
            memo[pos][mask][isNum ? 1 : 0][0] = result;
        }
        
        return result;
    }
    
    /**
     * 初始化数字的各位
     * 
     * @param n 输入数字
     */
    private void init(int n) {
        if (n == 0) {
            digits = new int[]{0};
            return;
        }
        
        int len = 0;
        int temp = n;
        while (temp > 0) {
            len++;
            temp /= 10;
        }
        
        digits = new int[len];
        temp = n;
        for (int i = len - 1; i >= 0; i--) {
            digits[i] = temp % 10;
            temp /= 10;
        }
    }
    
    /**
     * 清除记忆化数组
     */
    private void clearMemo() {
        // 最多20位数字，10个数字的mask，2种limit状态，2种isNum状态
        memo = new long[20][1 << 10][2][2];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < (1 << 10); j++) {
                for (int k = 0; k < 2; k++) {
                    Arrays.fill(memo[i][j][k], -1);
                }
            }
        }
    }
    
    /**
     * 主函数：计算[0, n]范围内各位数字都不重复的数字个数
     * 这是LeetCode 2376的解决方案
     * 
     * @param n 上界
     * @return 满足条件的数字个数
     */
    public long countSpecialNumbers(int n) {
        if (n < 0) {
            return 0;
        }
        init(n);
        clearMemo();
        return dfs(0, 0, true, false);
    }
    
    /**
     * 统计[low, high]范围内满足条件的数字个数
     * 使用前缀和思想：count(high) - count(low-1)
     * 
     * @param low 下界
     * @param high 上界
     * @return 区间[low, high]内满足条件的数字个数
     */
    public long countRange(int low, int high) {
        if (low <= 0) {
            return countSpecialNumbers(high);
        }
        return countSpecialNumbers(high) - countSpecialNumbers(low - 1);
    }
    
    /**
     * 统计[0, n]范围内数字1出现的次数
     * 这是LeetCode 233的解决方案
     * 
     * @param n 上界
     * @return 数字1出现的总次数
     */
    public long countDigitOne(int n) {
        if (n < 0) {
            return 0;
        }
        
        String s = String.valueOf(n);
        long count = 0;
        int len = s.length();
        
        // 逐位分析1的出现次数
        for (int i = 0; i < len; i++) {
            // 高位部分
            String highStr = s.substring(0, i);
            long high = highStr.isEmpty() ? 0 : Long.parseLong(highStr);
            // 当前位
            int current = s.charAt(i) - '0';
            // 低位部分
            String lowStr = i < len - 1 ? s.substring(i + 1) : "";
            long low = lowStr.isEmpty() ? 0 : Long.parseLong(lowStr);
            // 当前位的权值
            long pos = (long) Math.pow(10, len - i - 1);
            
            if (current == 0) {
                // 当前位是0，则1出现的次数由高位决定
                count += high * pos;
            } else if (current == 1) {
                // 当前位是1，则1出现的次数由高位和低位共同决定
                count += high * pos + (low + 1);
            } else {
                // 当前位大于1，则1出现的次数由高位决定（高位+1）
                count += (high + 1) * pos;
            }
        }
        
        return count;
    }
    
    /**
     * 统计[0, n]范围内二进制表示中不含连续1的数字个数
     * 这是LeetCode 600的解决方案
     * 
     * @param n 上界
     * @return 满足条件的数字个数
     */
    public int findIntegers(int n) {
        if (n == 0) {
            return 1;
        }
        
        // 转换为二进制字符串
        String binary = Integer.toBinaryString(n);
        int length = binary.length();
        
        // dp[i][0]表示i位二进制数，最高位为0时的有效数
        // dp[i][1]表示i位二进制数，最高位为1时的有效数
        int[][] dp = new int[length][2];
        
        // 初始状态：1位二进制数
        dp[0][0] = 1; // 数字0
        dp[0][1] = 1; // 数字1
        
        // 填充dp数组 - 自底向上的动态规划
        for (int i = 1; i < length; i++) {
            dp[i][0] = dp[i-1][0] + dp[i-1][1];  // 最高位为0，后面可以接0或1
            dp[i][1] = dp[i-1][0];               // 最高位为1，后面只能接0
        }
        
        // 计算结果
        int result = dp[length-1][0] + dp[length-1][1];
        
        // 检查是否存在连续1的情况，需要减去不符合条件的数
        for (int i = 1; i < length; i++) {
            if (binary.charAt(i) == '1' && binary.charAt(i-1) == '1') {
                break; // 出现连续1，不需要调整
            }
            if (binary.charAt(i) == '0' && binary.charAt(i-1) == '1') {
                // 调整结果
                String suffix = i + 1 < length ? binary.substring(i + 1) : "";
                int suffixVal = 0;
                if (!suffix.isEmpty()) {
                    suffixVal = Integer.parseInt(suffix, 2);
                }
                result -= suffixVal + 1;
                break;
            }
        }
        
        return result;
    }
    
    /**
     * 统计[0, n]范围内不含数字4和连续的62的数的个数
     * 这是HDU 2089的解决方案
     * 
     * @param n 上界
     * @return 满足条件的数字个数
     */
    public long countNo62(int n) {
        if (n < 0) {
            return 0;
        }
        
        init(n);
        // 使用记忆化Map来缓存结果，避免复杂的四维数组
        Map<String, Long> no62Memo = new HashMap<>();
        
        return dfsNo62(0, -1, true, false, no62Memo);
    }
    
    /**
     * 数位DP核心函数 - 不含4和连续62的问题
     * 
     * @param pos 当前处理到第几位
     * @param last 上一位的数字
     * @param isLimit 是否受到上界限制
     * @param has62 是否已经出现62
     * @param memo 记忆化Map
     * @return 满足条件的数字个数
     */
    private long dfsNo62(int pos, int last, boolean isLimit, boolean has62, Map<String, Long> memo) {
        if (has62) {
            return 0;
        }
        if (pos == digits.length) {
            return 1;
        }
        
        // 生成缓存键
        String key = pos + "," + last + "," + isLimit + "," + has62;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        long result = 0;
        int up = isLimit ? digits[pos] : 9;
        
        for (int digit = 0; digit <= up; digit++) {
            if (digit == 4) {  // 不能包含4
                continue;
            }
            boolean newHas62 = has62 || (last == 6 && digit == 2);
            result += dfsNo62(
                pos + 1, 
                digit,
                isLimit && (digit == up), 
                newHas62,
                memo
            );
        }
        
        memo.put(key, result);
        return result;
    }
    
    /**
     * 测量函数执行时间的辅助方法
     * 
     * @param task 要执行的任务
     * @param <T> 返回类型
     * @return 包含结果和执行时间的结果对象
     */
    public static <T> PerformanceResult<T> measurePerformance(Task<T> task) {
        long startTime = System.nanoTime();
        T result = task.execute();
        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime) / 1_000_000_000.0; // 转换为秒
        return new PerformanceResult<>(result, timeTaken);
    }
    
    /**
     * 函数式接口，用于测量性能
     */
    public interface Task<T> {
        T execute();
    }
    
    /**
     * 性能测量结果
     */
    public static class PerformanceResult<T> {
        public final T result;
        public final double timeTaken;
        
        public PerformanceResult(T result, double timeTaken) {
            this.result = result;
            this.timeTaken = timeTaken;
        }
    }
    
    /**
     * 运行全面的测试用例
     */
    public static void runComprehensiveTests() {
        DigitDP_Template_Complete solution = new DigitDP_Template_Complete();
        
        System.out.println("=== 数位DP模板综合测试 ===\n");
        
        // 测试用例1：统计各位数字不重复的数字个数
        int[][] testCases = {
            {20, 19},      // [0,20]中有19个各位数字不重复的数
            {100, 91},     // [0,100]中有91个各位数字不重复的数
            {200, 189},    // [0,200]中有189个各位数字不重复的数
            {1, 1},        // 边界情况：只有0和1
            {0, 1}         // 边界情况：只有0
        };
        
        System.out.println("1. 测试各位数字不重复的数字统计：");
        for (int[] tc : testCases) {
            int n = tc[0];
            long expected = tc[1];
            PerformanceResult<Long> result = measurePerformance(() -> solution.countSpecialNumbers(n));
            String status = (result.result == expected) ? "通过" : "失败";
            System.out.printf("   n = %d, 结果 = %d, %s, 耗时 = %.6f秒\n", 
                             n, result.result, status, result.timeTaken);
        }
        
        // 测试用例2：统计数字1出现的次数
        int[][] digitOneCases = {
            {13, 6},       // [0,13]中1出现6次
            {0, 0},        // 边界情况：0
            {1, 1},        // 边界情况：1
            {100, 21},     // [0,100]中1出现21次
            {1000, 301}    // [0,1000]中1出现301次
        };
        
        System.out.println("\n2. 测试数字1出现次数统计：");
        for (int[] tc : digitOneCases) {
            int n = tc[0];
            long expected = tc[1];
            PerformanceResult<Long> result = measurePerformance(() -> solution.countDigitOne(n));
            String status = (result.result == expected) ? "通过" : "失败";
            System.out.printf("   n = %d, 结果 = %d, %s, 耗时 = %.6f秒\n", 
                             n, result.result, status, result.timeTaken);
        }
        
        // 测试用例3：统计二进制中不含连续1的数字个数
        int[][] binaryCases = {
            {5, 5},        // 0,1,10,100,101 -> 5个
            {1, 2},        // 0,1 -> 2个
            {2, 3},        // 0,1,10 -> 3个
            {3, 3},        // 0,1,10 -> 3个（11不满足条件）
            {10, 8}        // 0,1,10,100,101,1000,1001,1010 -> 8个
        };
        
        System.out.println("\n3. 测试二进制不含连续1的数字统计：");
        for (int[] tc : binaryCases) {
            int n = tc[0];
            int expected = tc[1];
            PerformanceResult<Integer> result = measurePerformance(() -> solution.findIntegers(n));
            String status = (result.result == expected) ? "通过" : "失败";
            System.out.printf("   n = %d, 结果 = %d, %s, 耗时 = %.6f秒\n", 
                             n, result.result, status, result.timeTaken);
        }
        
        // 测试用例4：测试区间统计
        int[][] rangeCases = {
            {10, 20, 9},   // [10,20]中有9个各位数字不重复的数
            {50, 100, 41}, // [50,100]中有41个各位数字不重复的数
            {1, 1, 1}      // 边界情况：单个数
        };
        
        System.out.println("\n4. 测试区间统计功能：");
        for (int[] tc : rangeCases) {
            int low = tc[0];
            int high = tc[1];
            long expected = tc[2];
            PerformanceResult<Long> result = measurePerformance(() -> solution.countRange(low, high));
            String status = (result.result == expected) ? "通过" : "失败";
            System.out.printf("   区间 [%d, %d], 结果 = %d, %s, 耗时 = %.6f秒\n", 
                             low, high, result.result, status, result.timeTaken);
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
    
    public static void main(String[] args) {
        // 简单测试
        DigitDP_Template_Complete solution = new DigitDP_Template_Complete();
        int n = 100;
        System.out.println("简单测试 - n = " + n + ", 结果 = " + solution.countSpecialNumbers(n));
        
        // 运行综合测试（可选）
        // runComprehensiveTests();
        
        // 实际应用示例
        System.out.println("\n实际应用示例：");
        System.out.println("数字1在[0, 1000]中出现的次数: " + solution.countDigitOne(1000));
        System.out.println("[0, 100]中二进制不含连续1的数字个数: " + solution.findIntegers(100));
        System.out.println("[10, 200]中各位数字不重复的数字个数: " + solution.countRange(10, 200));
        System.out.println("[0, 200]中不含数字4和连续62的数字个数: " + solution.countNo62(200));
    }
}