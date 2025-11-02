package class147;

// 括号生成问题
// 给定n对括号，计算有多少种有效的括号组合方式
// 例如：n=3，输出5种有效组合：((())), (()()), (())(), ()(()), ()()()
// 测试链接：https://leetcode.com/problems/generate-parentheses/
// 也参考：https://www.nowcoder.com/practice/c18107181bf5405fb95993b84d625f39

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 卡特兰数应用 - 括号生成问题
 * 该问题是卡特兰数的经典应用，第n个卡特兰数即为n对括号的有效组合数量
 * 
 * 该类实现了：
 * 1. 括号有效组合数量计算（卡特兰数）的两种方法
 * 2. 生成所有有效括号组合的两种方法
 * 3. 完整的异常处理和边界条件检测
 * 4. 性能分析和工程化考量
 * 
 * 相关题目链接：
 * - LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
 * - LintCode 427. 生成括号: https://www.lintcode.com/problem/427/
 * - 牛客网 NC146. 括号生成: https://www.nowcoder.com/practice/c18107181bf5405fb95993b84d625f39
 * - LeetCode 856. 括号的分数: https://leetcode.cn/problems/score-of-parentheses/
 * - LeetCode 32. 最长有效括号: https://leetcode.cn/problems/longest-valid-parentheses/
 */
public class Code10_Parentheses {
    
    /**
     * 计算n对括号能生成的不同有效括号序列数量
     * 这是经典的卡特兰数应用，使用动态规划方法计算
     * 
     * 核心思路：
     * - 对于n对括号，枚举第一对括号将整体分为两部分：内部和外部
     * - dp[n] = Σ(i=0到n-1) dp[i] * dp[n-1-i]
     * - 其中dp[i]表示i对括号的有效组合数，dp[n-1-i]表示外部部分的有效组合数
     * 
     * 时间复杂度分析：
     * - 双重循环，外层循环n次，内层循环最多n次
     * - 总时间复杂度：O(n²)
     * 
     * 空间复杂度分析：
     * - 使用一个长度为n+1的数组存储中间结果
     * - 空间复杂度：O(n)
     * 
     * @param n 括号对数
     * @return 有效括号序列的数量
     * @throws IllegalArgumentException 当n为负数时抛出异常
     * @throws ArithmeticException 当计算结果溢出时抛出异常
     */
    public static int generateParenthesisCount(int n) {
        // 输入验证
        if (n < 0) {
            throw new IllegalArgumentException("括号对数不能为负数: " + n);
        }
        
        // 边界条件处理
        if (n <= 1) {
            return 1;
        }
        
        // dp[i] 表示i对括号能生成的有效序列数量
        int[] dp = new int[n + 1];
        
        // 初始化基本情况
        dp[0] = 1; // 0对括号有1种方案（空序列）
        dp[1] = 1; // 1对括号有1种方案："()"
        
        // 动态规划填表
        // 对于i对括号，枚举第一对括号内部包含的括号对数j
        // 那么第一对括号外部右侧就有i-1-j对括号
        // 总方案数就是内部j对括号的方案数乘以外部i-1-j对括号的方案数
        for (int i = 2; i <= n; i++) {
            // 对于i对括号，枚举第一对括号内包含的括号对数j（0到i-1）
            for (int j = 0; j < i; j++) {
                // dp[j] 是内部j对括号的方案数
                // dp[i-1-j] 是外部i-1-j对括号的方案数
                // 两者相乘得到当前j值下的方案数，累加到dp[i]中
                
                // 乘法溢出预防 - 工程化防御措施
                if (dp[j] > Integer.MAX_VALUE / dp[i - 1 - j]) {
                    throw new ArithmeticException("计算结果溢出，n=" + n + " 过大，建议使用更大的数据类型或取模运算");
                }
                
                dp[i] += dp[j] * dp[i - 1 - j];
                
                // 溢出检测
                if (dp[i] < 0) {
                    throw new ArithmeticException("计算结果溢出，n=" + n + " 过大");
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * 计算n对括号的有效组合数量（使用卡特兰数递推公式优化）
     * 应用递推公式：C(n) = C(n-1) * (4n-2)/(n+1)
     * 
     * 该递推式比动态规划更高效，且能保证整数结果
     * 数学证明：每个卡特兰数都是整数，所以除法操作不会产生小数
     * 
     * 时间复杂度：O(n)，单次循环
     * 空间复杂度：O(1)，只使用常量额外空间
     * 
     * @param n 括号对数
     * @return 有效组合数量
     * @throws IllegalArgumentException 当n为负数时抛出异常
     * @throws ArithmeticException 当计算溢出时抛出异常
     */
    public static long generateParenthesisCountOptimized(int n) {
        // 输入验证
        if (n < 0) {
            throw new IllegalArgumentException("括号对数不能为负数: " + n);
        }
        
        // 边界情况处理
        if (n <= 1) {
            return 1;
        }
        
        // 使用递推公式：C(n) = C(n-1) * (4n-2)/(n+1)
        long catalan = 1;
        for (int i = 1; i <= n; i++) {
            // 乘法溢出预防
            if (catalan > Long.MAX_VALUE / (4 * i - 2)) {
                throw new ArithmeticException("计算中间结果溢出，n=" + n + " 过大，建议使用BigInteger类型或取模运算");
            }
            
            // 先乘后除保证整除性
            catalan = catalan * (4 * i - 2) / (i + 1);
            
            // 检测结果是否在int范围内
            if (catalan < 0 || catalan > Integer.MAX_VALUE) {
                throw new ArithmeticException("计算结果溢出，n=" + n + " 过大，结果为: " + catalan);
            }
        }
        
        return catalan;
    }
    
    /**
     * 生成所有有效的括号序列
     * 使用递归回溯算法
     * 
     * 核心思路：
     * - 通过维护已使用的左括号和右括号数量来控制生成过程
     * - 只有左括号数量小于n时才能添加左括号
     * - 只有右括号数量小于左括号数量时才能添加右括号
     * - 这种方法确保生成的所有序列都是有效的
     * 
     * 时间复杂度：O(4^n / sqrt(n)) - 卡特兰数的渐近复杂度
     * 空间复杂度：O(n) - 递归调用栈深度
     * 
     * @param n 括号对数
     * @return 所有有效的括号序列
     * @throws IllegalArgumentException 当n为负数时抛出异常
     */
    public static List<String> generateAllParentheses(int n) {
        // 输入验证
        if (n < 0) {
            throw new IllegalArgumentException("括号对数不能为负数: " + n);
        }
        
        List<String> result = new ArrayList<>();
        generateHelper(result, "", 0, 0, n);
        return result;
    }
    
    /**
     * 使用动态规划生成所有有效括号组合
     * 核心思路：任何有效括号组合都可以表示为 "(A)B"，其中A和B也是有效括号组合
     * - A是j对括号的有效组合
     * - B是n-1-j对括号的有效组合
     * 
     * 时间复杂度：O(4^n / sqrt(n)) - 与回溯法相同的渐近复杂度
     * 空间复杂度：O(n * 4^n / sqrt(n)) - 存储所有中间结果和最终结果
     * 
     * @param n 括号对数
     * @return 所有有效括号组合的列表
     * @throws IllegalArgumentException 当n为负数时抛出异常
     */
    public static List<String> generateAllParenthesesDP(int n) {
        // 输入验证
        if (n < 0) {
            throw new IllegalArgumentException("括号对数不能为负数: " + n);
        }
        
        // 边界情况处理
        if (n == 0) {
            return Arrays.asList("");
        }
        
        // dp[i] 存储i对括号的所有有效组合
            List<List<String>> dp = new ArrayList<>();
            dp.add(Arrays.asList("") ); // dp[0] = [""] - 空字符串是基础情况
        
        for (int i = 1; i <= n; i++) {
            List<String> currentList = new ArrayList<>();
            
            // 枚举根位置，左侧有j对括号，右侧有i-j-1对括号
            for (int j = 0; j < i; j++) {
                List<String> leftCombinations = dp.get(j);
                List<String> rightCombinations = dp.get(i - j - 1);
                
                // 笛卡尔积组合左右结果
                for (String left : leftCombinations) {
                    for (String right : rightCombinations) {
                        // 构建新的有效组合："(left)right"
                        currentList.add("(" + left + ")" + right);
                    }
                }
            }
            
            dp.add(currentList);
        }
        
        return dp.get(n);
    }
    
    /**
     * 递归生成括号序列的辅助函数
     * 使用回溯算法构建所有可能的有效组合
     * 
     * @param result 结果列表，用于存储所有有效组合
     * @param current 当前正在构建的括号组合
     * @param open 已使用的左括号数量
     * @param close 已使用的右括号数量
     * @param max 最大括号对数
     */
    private static void generateHelper(List<String> result, String current, int open, int close, int max) {
        // 递归终止条件：已生成2*max个字符
        if (current.length() == max * 2) {
            result.add(current);
            return;
        }
        
        // 如果左括号数小于max，可以添加左括号
        if (open < max) {
            generateHelper(result, current + "(", open + 1, close, max);
        }
        
        // 如果右括号数小于左括号数，可以添加右括号
        // 这确保了生成的括号组合始终有效
        if (close < open) {
            generateHelper(result, current + ")", open, close + 1, max);
        }
    }
    
    /**
     * 验证括号组合是否有效
     * 使用平衡计数法检查括号序列的有效性
     * 
     * 时间复杂度：O(n)，其中n是字符串长度
     * 空间复杂度：O(1)，只使用一个变量
     * 
     * @param s 待验证的括号字符串
     * @return 如果字符串是有效的括号组合，返回true；否则返回false
     */
    public static boolean isValidParentheses(String s) {
        if (s == null) {
            return false;
        }
        
        int balance = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
                // 如果右括号过多，立即返回false
                if (balance < 0) {
                    return false;
                }
            }
            // 忽略其他字符（如果有的话）
        }
        // 最终平衡值应为0
        return balance == 0;
    }
    
    /**
     * 打印性能指标，格式化输出执行时间
     * 
     * @param operation 操作描述
     * @param duration 执行时间（纳秒）
     */
    private static void printPerformance(String operation, long duration) {
        if (duration < 1000) {
            System.out.println("  " + operation + ": " + duration + " ns");
        } else if (duration < 1000000) {
            System.out.println("  " + operation + ": " + String.format("%.2f", duration / 1000.0) + " μs");
        } else if (duration < 1000000000) {
            System.out.println("  " + operation + ": " + String.format("%.2f", duration / 1000000.0) + " ms");
        } else {
            System.out.println("  " + operation + ": " + String.format("%.2f", duration / 1000000000.0) + " s");
        }
    }
    
    /**
     * 主方法 - 测试所有实现并比较性能
     * 包含多种测试场景：基本测试、边界情况、异常处理、性能测试等
     */
    public static void main(String[] args) {
        System.out.println("===== 括号生成问题（Parentheses Generation）测试 =====");
        
        // 测试用例1: 括号数量计算
        System.out.println("\n1. 括号数量计算:");
        for (int i = 0; i <= 5; i++) {
            try {
                System.out.println("n=" + i + ":");
                
                long startTime = System.nanoTime();
                int result1 = generateParenthesisCount(i);
                long endTime = System.nanoTime();
                printPerformance("动态规划法: " + result1, endTime - startTime);
                
                startTime = System.nanoTime();
                long result2 = generateParenthesisCountOptimized(i);
                endTime = System.nanoTime();
                printPerformance("公式优化法: " + result2, endTime - startTime);
                
                // 验证结果一致性
                if (result1 == result2) {
                    System.out.println("  ✓ 结果一致");
                } else {
                    System.out.println("  ✗ 结果不一致，请检查实现");
                }
                System.out.println();
            } catch (Exception e) {
                System.out.println("  计算异常: " + e.getMessage());
                System.out.println();
            }
        }
        
        // 测试用例2: 生成所有括号组合
        System.out.println("\n2. 生成所有括号组合:");
        for (int i = 1; i <= 3; i++) {
            System.out.println("n=" + i + ":");
            
            // 使用回溯法生成
            long startTime = System.nanoTime();
            List<String> combinations1 = generateAllParentheses(i);
            long endTime = System.nanoTime();
            System.out.println("  回溯法结果:");
            for (String combination : combinations1) {
                System.out.println("    " + combination);
            }
            printPerformance("回溯法耗时", endTime - startTime);
            
            // 使用动态规划生成
            startTime = System.nanoTime();
            List<String> combinations2 = generateAllParenthesesDP(i);
            endTime = System.nanoTime();
            printPerformance("动态规划法耗时", endTime - startTime);
            
            // 验证两种方法结果一致性
            boolean consistent = new HashSet<>(combinations1).equals(new HashSet<>(combinations2));
            System.out.println("  结果一致性: " + (consistent ? "✓ 一致" : "✗ 不一致"));
        }
        
        // 测试用例3: 结果验证
        System.out.println("\n3. 结果验证:");
        List<String> testResult = generateAllParentheses(4);
        System.out.println("n=4 生成的组合数量: " + testResult.size());
        System.out.println("预期数量（卡特兰数）: " + generateParenthesisCount(4));
        
        boolean allValid = true;
        for (String s : testResult) {
            if (!isValidParentheses(s)) {
                allValid = false;
                System.out.println("  无效组合: " + s);
                break;
            }
        }
        System.out.println("  所有组合是否有效: " + (allValid ? "✓ 全部有效" : "✗ 存在无效组合"));
        
        // 测试用例4: 异常处理
        System.out.println("\n4. 异常处理测试:");
        try {
            generateParenthesisCount(-1);
            System.out.println("✗ 未能捕获负数输入异常");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ 正确捕获负数输入异常: " + e.getMessage());
        }
        
        try {
            generateAllParentheses(-1);
            System.out.println("✗ 未能捕获负数输入异常");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ 正确捕获generateAllParentheses负数输入异常: " + e.getMessage());
        }
        
        // 测试用例5: 性能对比（较大n值）
        System.out.println("\n5. 性能对比（较大n值）:");
        int[] largeTestCases = {10, 15, 19}; // 19是int能容纳的最大卡特兰数
        
        for (int largeN : largeTestCases) {
            try {
                System.out.println("n=" + largeN + ":");
                
                long startTime = System.nanoTime();
                int count1 = generateParenthesisCount(largeN);
                long endTime = System.nanoTime();
                printPerformance("动态规划计数法, 结果: " + count1, endTime - startTime);
                
                startTime = System.nanoTime();
                long count2 = generateParenthesisCountOptimized(largeN);
                endTime = System.nanoTime();
                printPerformance("公式优化计数法, 结果: " + count2, endTime - startTime);
                
                // 验证结果一致性
                if (count1 == count2) {
                    System.out.println("  ✓ 结果一致");
                } else {
                    System.out.println("  ✗ 结果不一致");
                }
                System.out.println();
            } catch (Exception e) {
                System.out.println("  性能测试异常: " + e.getMessage());
                System.out.println();
            }
        }
        
        // 最优解分析总结
        System.out.println("\n===== 最优解分析 =====");
        System.out.println("1. 算法选择建议:");
        System.out.println("   - 仅需计算数量：使用卡特兰数公式，时间复杂度O(n)，空间复杂度O(1)");
        System.out.println("   - 需要生成所有组合：使用回溯算法或动态规划方法");
        System.out.println("   - 回溯法实现简洁且直观，动态规划法在某些场景下可能更易于扩展");
        System.out.println();
        System.out.println("2. 工程化考量:");
        System.out.println("   - 输入验证：全面检查参数合法性，确保健壮性");
        System.out.println("   - 溢出检测：预先检测可能的溢出情况，提供清晰错误信息");
        System.out.println("   - 性能优化：对于n>15的情况，生成所有组合会导致性能急剧下降");
        System.out.println("   - 内存使用：生成大量组合时需要注意内存消耗");
        System.out.println();
        System.out.println("3. 相关题目和应用场景:");
        System.out.println("   - LeetCode 22. 括号生成: 本题的标准实现");
        System.out.println("   - LeetCode 856. 括号的分数: 括号组合的计分问题");
        System.out.println("   - LeetCode 32. 最长有效括号: 寻找最长有效括号子串");
        System.out.println("   - 卡特兰数的其他应用：出栈序列计数、不同二叉搜索树计数等");
        System.out.println();
        System.out.println("4. Java语言特性注意事项:");
        System.out.println("   - 字符串拼接：在回溯过程中使用StringBuilder可以提高性能");
        System.out.println("   - 整数类型限制：int类型最大只能表示到第19个卡特兰数");
        System.out.println("   - 异常处理：使用适当的异常类型提供清晰的错误信息");
        System.out.println();
        System.out.println("5. 进阶思考:");
        System.out.println("   - 如何高效生成特定长度的括号组合？");
        System.out.println("   - 如何优化回溯算法的性能？（考虑使用StringBuilder）");
        System.out.println("   - 对于非常大的n值，如何计算卡特兰数？（使用BigInteger）");
        System.out.println("   - 如何将该问题推广到其他类型的括号？（如 {}, [], () 混合）");
    }
}