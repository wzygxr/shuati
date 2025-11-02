package class086;

// LeetCode 474. 一和零
// 给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
// 请你找出并返回 strs 的最大子集的大小，该子集中最多有 m 个 0 和 n 个 1 。
// 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
// 测试链接 : https://leetcode.cn/problems/ones-and-zeroes/

/**
 * 算法详解：一和零问题（LeetCode 474）
 * 
 * 问题描述：
 * 给定二进制字符串数组，找到最大的子集，使得子集中0的总数不超过m，1的总数不超过n。
 * 
 * 算法思路：
 * 1. 转化为二维费用的0-1背包问题
 * 2. 动态规划：dp[k][i][j]表示前k个字符串，使用i个0和j个1时的最大子集大小
 * 3. 空间优化：使用二维数组进行状态压缩
 * 
 * 时间复杂度分析：
 * 1. 预处理：统计每个字符串的0和1数量 O(L)，其中L是所有字符串总长度
 * 2. 动态规划：O(len * m * n)，其中len是字符串数组长度
 * 3. 总体时间复杂度：O(len * m * n + L)
 * 
 * 空间复杂度分析：
 * 1. 基础版本：O(len * m * n)
 * 2. 空间优化版本：O(m * n)
 * 3. 总体空间复杂度：O(len * m * n) 或 O(m * n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数有效性
 * 2. 边界处理：m或n为0的情况
 * 3. 性能优化：使用空间优化技术
 * 4. 代码可读性：清晰的变量命名和状态转移逻辑
 * 
 * 极端场景验证：
 * 1. 输入数组为空的情况
 * 2. m或n为0的情况
 * 3. 所有字符串都相同的情况
 * 4. 大规模数据的性能测试
 */
public class LeetCode474_Ones_and_Zeroes {
    
    /**
     * 基础动态规划解法（三维DP）
     * 时间复杂度：O(len * m * n)
     * 空间复杂度：O(len * m * n)
     * 
     * 算法思想：
     * 将问题转化为二维费用的0-1背包问题，每个字符串有0和1两种费用。
     */
    public static int findMaxForm(String[] strs, int m, int n) {
        // 异常处理
        if (strs == null || strs.length == 0 || m < 0 || n < 0) {
            return 0;
        }
        
        int len = strs.length;
        
        // 预处理：统计每个字符串的0和1数量
        int[][] counts = new int[len][2];
        for (int i = 0; i < len; i++) {
            counts[i] = countZeroesAndOnes(strs[i]);
        }
        
        // dp[k][i][j]表示前k个字符串，使用i个0和j个1时的最大子集大小
        int[][][] dp = new int[len + 1][m + 1][n + 1];
        
        // 填充dp表
        for (int k = 1; k <= len; k++) {
            int zeroes = counts[k - 1][0];
            int ones = counts[k - 1][1];
            
            for (int i = 0; i <= m; i++) {
                for (int j = 0; j <= n; j++) {
                    if (i >= zeroes && j >= ones) {
                        // 可以选择当前字符串或不选择
                        dp[k][i][j] = Math.max(dp[k - 1][i][j], 
                                              dp[k - 1][i - zeroes][j - ones] + 1);
                    } else {
                        // 不能选择当前字符串
                        dp[k][i][j] = dp[k - 1][i][j];
                    }
                }
            }
        }
        
        return dp[len][m][n];
    }
    
    /**
     * 空间优化版本（二维DP）
     * 时间复杂度：O(len * m * n)
     * 空间复杂度：O(m * n)
     * 
     * 优化思路：
     * 观察状态转移方程，dp[k][i][j]只依赖于dp[k-1][i][j]和dp[k-1][i-zeroes][j-ones]
     * 可以使用二维数组，从后往前更新避免覆盖。
     */
    public static int findMaxFormOptimized(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0 || m < 0 || n < 0) {
            return 0;
        }
        
        int len = strs.length;
        
        // 预处理
        int[][] counts = new int[len][2];
        for (int i = 0; i < len; i++) {
            counts[i] = countZeroesAndOnes(strs[i]);
        }
        
        // dp[i][j]表示使用i个0和j个1时的最大子集大小
        int[][] dp = new int[m + 1][n + 1];
        
        // 遍历每个字符串
        for (int k = 0; k < len; k++) {
            int zeroes = counts[k][0];
            int ones = counts[k][1];
            
            // 从后往前更新，避免重复使用同一个字符串
            for (int i = m; i >= zeroes; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeroes][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 进一步优化的版本（减少内存分配）
     * 时间复杂度：O(len * m * n)
     * 空间复杂度：O(m * n)
     * 
     * 优化点：
     * 1. 避免创建额外的counts数组
     * 2. 直接在循环中统计0和1数量
     */
    public static int findMaxFormHighlyOptimized(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0 || m < 0 || n < 0) {
            return 0;
        }
        
        int[][] dp = new int[m + 1][n + 1];
        
        for (String str : strs) {
            // 直接统计当前字符串的0和1数量
            int zeroes = 0, ones = 0;
            for (char c : str.toCharArray()) {
                if (c == '0') zeroes++;
                else ones++;
            }
            
            // 从后往前更新dp表
            for (int i = m; i >= zeroes; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeroes][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 统计字符串中0和1的数量
     * 时间复杂度：O(L)，其中L是字符串长度
     * 空间复杂度：O(1)
     */
    private static int[] countZeroesAndOnes(String str) {
        int zeroes = 0, ones = 0;
        for (char c : str.toCharArray()) {
            if (c == '0') zeroes++;
            else ones++;
        }
        return new int[]{zeroes, ones};
    }
    
    /**
     * 带剪枝的优化版本
     * 时间复杂度：O(len * m * n)
     * 空间复杂度：O(m * n)
     * 
     * 优化点：
     * 1. 提前跳过0和1数量都超过限制的字符串
     * 2. 对字符串按长度排序，先处理较短的字符串
     */
    public static int findMaxFormWithPruning(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0 || m < 0 || n < 0) {
            return 0;
        }
        
        // 按字符串长度排序（短字符串优先）
        java.util.Arrays.sort(strs, (a, b) -> a.length() - b.length());
        
        int[][] dp = new int[m + 1][n + 1];
        
        for (String str : strs) {
            int zeroes = 0, ones = 0;
            for (char c : str.toCharArray()) {
                if (c == '0') zeroes++;
                else ones++;
            }
            
            // 剪枝：如果0或1数量超过限制，跳过该字符串
            if (zeroes > m || ones > n) {
                continue;
            }
            
            for (int i = m; i >= zeroes; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeroes][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 单元测试方法
     * 验证算法的正确性和各种边界情况
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 474 一和零问题测试 ===\n");
        
        // 测试用例1：基本功能测试
        testCase("测试用例1 - 基本功能", 
                new String[]{"10", "0001", "111001", "1", "0"}, 5, 3, 4);
        
        // 测试用例2：LeetCode官方示例
        testCase("测试用例2 - 官方示例", 
                new String[]{"10", "0", "1"}, 1, 1, 2);
        
        // 测试用例3：m或n为0
        testCase("测试用例3 - m为0", 
                new String[]{"10", "0", "1"}, 0, 1, 1);
        
        // 测试用例4：空数组
        testCase("测试用例4 - 空数组", 
                new String[]{}, 5, 3, 0);
        
        // 测试用例5：所有字符串都相同
        testCase("测试用例5 - 全相同", 
                new String[]{"1", "1", "1", "1"}, 3, 3, 3);
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 测试用例辅助方法
     */
    private static void testCase(String description, String[] strs, int m, int n, int expected) {
        System.out.println(description);
        System.out.println("输入字符串: " + java.util.Arrays.toString(strs));
        System.out.println("m = " + m + ", n = " + n);
        System.out.println("期望结果: " + expected);
        
        int result1 = findMaxForm(strs, m, n);
        int result2 = findMaxFormOptimized(strs, m, n);
        int result3 = findMaxFormHighlyOptimized(strs, m, n);
        int result4 = findMaxFormWithPruning(strs, m, n);
        
        System.out.println("基础DP: " + result1 + " " + (result1 == expected ? "✓" : "✗"));
        System.out.println("优化DP: " + result2 + " " + (result2 == expected ? "✓" : "✗"));
        System.out.println("高度优化: " + result3 + " " + (result3 == expected ? "✓" : "✗"));
        System.out.println("剪枝优化: " + result4 + " " + (result4 == expected ? "✓" : "✗"));
        
        if (result1 == result2 && result2 == result3 && result3 == result4 && result1 == expected) {
            System.out.println("测试通过 ✓\n");
        } else {
            System.out.println("测试失败 ✗\n");
        }
    }
    
    /**
     * 性能测试方法
     * 测试算法在大规模数据下的表现
     */
    private static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成测试数据：大规模字符串数组
        int len = 100;
        int strLen = 10;
        String[] strs = new String[len];
        java.util.Random random = new java.util.Random();
        
        // 生成随机二进制字符串
        for (int i = 0; i < len; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < strLen; j++) {
                sb.append(random.nextInt(2));
            }
            strs[i] = sb.toString();
        }
        
        int m = 50, n = 50;
        
        System.out.println("测试数据规模: " + len + "个字符串");
        System.out.println("每个字符串长度: " + strLen);
        System.out.println("m = " + m + ", n = " + n);
        
        // 测试高度优化版本
        long startTime = System.currentTimeMillis();
        int result1 = findMaxFormHighlyOptimized(strs, m, n);
        long endTime = System.currentTimeMillis();
        System.out.println("高度优化版本:");
        System.out.println("  结果: " + result1);
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 测试剪枝优化版本
        startTime = System.currentTimeMillis();
        int result2 = findMaxFormWithPruning(strs, m, n);
        endTime = System.currentTimeMillis();
        System.out.println("剪枝优化版本:");
        System.out.println("  结果: " + result2);
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 验证结果一致性
        if (result1 == result2) {
            System.out.println("结果一致性验证: 通过 ✓");
        } else {
            System.out.println("结果一致性验证: 失败 ✗");
        }
    }
    
    /**
     * 复杂度分析详细计算：
     * 
     * 基础动态规划：
     * - 时间：预处理O(L) + DP计算O(len * m * n) → O(len * m * n + L)
     * - 空间：三维dp数组大小len×m×n → O(len * m * n)
     * 
     * 空间优化版本：
     * - 时间：O(len * m * n + L)
     * - 空间：二维dp数组大小m×n → O(m * n)
     * - 最优解：是，综合性能最好
     * 
     * 高度优化版本：
     * - 时间：O(len * m * n + L)
     * - 空间：O(m * n)
     * - 最优解：是，内存使用更少
     * 
     * 剪枝优化版本：
     * - 时间：O(len * m * n + L)，但实际运行可能更快
     * - 空间：O(m * n)
     * - 最优解：是，适合特定场景
     * 
     * 工程选择依据：
     * 1. 对于小规模数据：任意方法都可
     * 2. 对于中等规模数据：优先选择高度优化版本
     * 3. 对于大规模数据：剪枝优化版本可能更好
     * 
     * 算法调试技巧：
     * 1. 打印每个字符串的0和1数量
     * 2. 观察dp表的填充过程
     * 3. 使用小规模测试用例验证正确性
     */
}