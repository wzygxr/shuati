package class074;

// 编辑距离
// 给你两个单词 word1 和 word2，请返回将 word1 转换成 word2 所使用的最少操作数
// 你可以对一个单词进行如下三种操作：
// 1. 插入一个字符
// 2. 删除一个字符
// 3. 替换一个字符
// 测试链接 : https://leetcode.cn/problems/edit-distance/

/*
 * 算法详解：
 * 编辑距离是动态规划的经典问题，用于衡量两个字符串之间的相似度。
 * 通过插入、删除、替换操作将一个字符串转换为另一个字符串所需的最少操作次数。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j]表示将word1的前i个字符转换成word2的前j个字符所需的最少操作数
 * 2. 状态转移方程：
 *    - 如果word1[i-1] == word2[j-1]，则dp[i][j] = dp[i-1][j-1]（不需要操作）
 *    - 否则，dp[i][j] = min(dp[i-1][j-1], dp[i-1][j], dp[i][j-1]) + 1
 *       其中：dp[i-1][j-1] + 1表示替换操作
 *            dp[i-1][j] + 1表示删除操作
 *            dp[i][j-1] + 1表示插入操作
 * 3. 初始化：
 *    - dp[i][0] = i（删除i个字符）
 *    - dp[0][j] = j（插入j个字符）
 * 
 * 时间复杂度分析：
 * 设word1长度为m，word2长度为n
 * 1. 动态规划计算：O(m * n)
 * 总时间复杂度：O(m * n)
 * 
 * 空间复杂度分析：
 * 1. 二维DP数组：O(m * n)
 * 2. 空间优化后：O(min(m, n))
 * 
 * 相关题目扩展：
 * 1. LeetCode 72. 编辑距离（本题）
 * 2. LeetCode 1143. 最长公共子序列
 * 3. LeetCode 97. 交错字符串
 * 4. LeetCode 115. 不同的子序列
 * 5. LeetCode 583. 两个字符串的删除操作
 * 6. LeetCode 712. 两个字符串的最小ASCII删除和
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空字符串、非法字符等边界情况
 * 3. 可配置性：可以将操作代价作为配置参数传入
 * 4. 单元测试：为minDistance方法编写测试用例
 * 5. 性能优化：对于长字符串，使用空间优化版本
 * 
 * 语言特性差异：
 * 1. Java：使用toCharArray转换字符串，便于随机访问
 * 2. 字符串操作：Java字符串不可变，需要注意性能影响
 * 3. 内存管理：自动垃圾回收，无需手动管理内存
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 空间压缩：从二维dp优化到一维dp
 * 2. 提前终止：当发现两个字符串相同时直接返回0
 * 3. 并行计算：对于大规模数据，可以考虑分块并行计算
 * 
 * 应用场景：
 * 1. 拼写检查：计算单词与字典中单词的编辑距离
 * 2. DNA序列比对：生物信息学中的序列相似度计算
 * 3. 自然语言处理：文本相似度计算
 * 4. 版本控制：文件差异比较
 */

public class Code13_EditDistance {
    
    // 标准二维DP版本
    public static int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        // 边界情况处理
        if (m == 0) return n;
        if (n == 0) return m;
        if (word1.equals(word2)) return 0;
        
        // 创建DP数组
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // 将word1的前i个字符转换为空字符串需要i次删除操作
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // 将空字符串转换为word2的前j个字符需要j次插入操作
        }
        
        // 填充DP数组
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // 字符相同，不需要操作
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 字符不同，取三种操作的最小值加1
                    dp[i][j] = Math.min(Math.min(
                        dp[i - 1][j - 1], // 替换操作
                        dp[i - 1][j]      // 删除操作
                    ), dp[i][j - 1])      // 插入操作
                    + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 空间优化版本（使用一维数组）
    public static int minDistanceOptimized(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        // 边界情况处理
        if (m == 0) return n;
        if (n == 0) return m;
        if (word1.equals(word2)) return 0;
        
        // 为了节省空间，让word2作为较短的字符串
        if (m < n) {
            return minDistanceOptimized(word2, word1);
        }
        
        // 使用一维DP数组
        int[] dp = new int[n + 1];
        
        // 初始化第一行
        for (int j = 0; j <= n; j++) {
            dp[j] = j;
        }
        
        // 填充DP数组
        for (int i = 1; i <= m; i++) {
            int prev = dp[0]; // 保存左上角的值
            dp[0] = i; // 更新第一列
            
            for (int j = 1; j <= n; j++) {
                int temp = dp[j]; // 保存当前值，用于下一轮计算
                
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[j] = prev; // 字符相同，直接继承左上角的值
                } else {
                    dp[j] = Math.min(Math.min(
                        prev,        // 替换操作（左上角）
                        dp[j]       // 删除操作（上方）
                    ), dp[j - 1])   // 插入操作（左方）
                    + 1;
                }
                
                prev = temp; // 更新左上角的值
            }
        }
        
        return dp[n];
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String word1_1 = "horse";
        String word2_1 = "ros";
        System.out.println("测试用例1:");
        System.out.println("标准版本: " + minDistance(word1_1, word2_1));
        System.out.println("优化版本: " + minDistanceOptimized(word1_1, word2_1));
        System.out.println("预期结果: 3");
        System.out.println();
        
        // 测试用例2
        String word1_2 = "intention";
        String word2_2 = "execution";
        System.out.println("测试用例2:");
        System.out.println("标准版本: " + minDistance(word1_2, word2_2));
        System.out.println("优化版本: " + minDistanceOptimized(word1_2, word2_2));
        System.out.println("预期结果: 5");
        System.out.println();
        
        // 测试用例3：边界情况
        String word1_3 = "";
        String word2_3 = "abc";
        System.out.println("测试用例3（空字符串）:");
        System.out.println("标准版本: " + minDistance(word1_3, word2_3));
        System.out.println("优化版本: " + minDistanceOptimized(word1_3, word2_3));
        System.out.println("预期结果: 3");
        System.out.println();
        
        // 测试用例4：相同字符串
        String word1_4 = "abc";
        String word2_4 = "abc";
        System.out.println("测试用例4（相同字符串）:");
        System.out.println("标准版本: " + minDistance(word1_4, word2_4));
        System.out.println("优化版本: " + minDistanceOptimized(word1_4, word2_4));
        System.out.println("预期结果: 0");
    }
    
    /*
     * =============================================================================================
     * 补充题目：LeetCode 1143. 最长公共子序列
     * 题目链接：https://leetcode.cn/problems/longest-common-subsequence/
     * 题目描述：给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
     * 如果不存在公共子序列，返回 0。
     * 
     * 解题思路：
     * 最长公共子序列（LCS）是动态规划的经典问题，用于找到两个字符串的最长公共子序列。
     * 
     * 状态定义：dp[i][j]表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
     * 状态转移方程：
     * - 如果text1[i-1] == text2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
     * - 否则，dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     * 
     * 时间复杂度：O(m * n)，其中m和n分别是两个字符串的长度
     * 空间复杂度：O(m * n)，可以优化到O(min(m, n))
     * 
     * Java实现：
     * public int longestCommonSubsequence(String text1, String text2) {
     *     int m = text1.length();
     *     int n = text2.length();
     *     
     *     int[][] dp = new int[m + 1][n + 1];
     *     
     *     for (int i = 1; i <= m; i++) {
     *         for (int j = 1; j <= n; j++) {
     *             if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
     *                 dp[i][j] = dp[i - 1][j - 1] + 1;
     *             } else {
     *                 dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
     *             }
     *         }
     *     }
     *     
     *     return dp[m][n];
     * }
     * 
     * 工程化考量：
     * 1. 边界检查：处理空字符串的情况
     * 2. 内存优化：使用滚动数组优化空间复杂度
     * 3. 性能优化：对于长字符串，考虑使用分治算法
     * 
     * 优化思路：
     * 1. 空间压缩：使用一维数组进行优化
     * 2. 早期剪枝：当发现不可能有更长的子序列时提前终止
     * 3. 并行计算：对于大规模数据，可以考虑并行计算
     * 
     * 应用场景：
     * 1. 文件差异比较：Git等版本控制系统的diff算法
     * 2. DNA序列比对：生物信息学中的序列相似度计算
     * 3. 文本相似度：搜索引擎中的文档相似度计算
     */
}