package class086;

// LeetCode 72. 编辑距离
// 给你两个单词 word1 和 word2，请返回将 word1 转换成 word2 所使用的最少操作数。
// 你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
// 测试链接 : https://leetcode.cn/problems/edit-distance/

/**
 * 算法详解：编辑距离（LeetCode 72）
 * 
 * 问题描述：
 * 给定两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
 * 操作包括：插入一个字符、删除一个字符、替换一个字符。
 * 
 * 算法思路：
 * 使用动态规划方法解决编辑距离问题。
 * 1. 定义状态：dp[i][j]表示将word1的前i个字符转换为word2的前j个字符所需的最小操作数
 * 2. 状态转移方程：
 *    - 如果word1[i-1] == word2[j-1]：dp[i][j] = dp[i-1][j-1]
 *    - 否则：dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
 *      分别对应删除、插入和替换操作
 * 
 * 时间复杂度分析：
 * 1. 填充dp表：需要遍历两个单词的所有字符组合，时间复杂度为O(m*n)
 * 2. 总体时间复杂度：O(m*n)
 * 
 * 空间复杂度分析：
 * 1. dp数组：需要存储m*n个状态值，空间复杂度为O(m*n)
 * 2. 空间优化版本：使用滚动数组可将空间复杂度优化到O(min(m,n))
 * 3. 总体空间复杂度：O(m*n) 或 O(min(m,n))
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入单词是否为空
 * 2. 边界处理：正确处理空字符串的情况
 * 3. 性能优化：使用空间优化版本减少内存使用
 * 4. 代码可读性：使用有意义的变量名，添加清晰的注释
 * 
 * 极端场景验证：
 * 1. 输入单词为空的情况
 * 2. 两个单词完全相同的情况
 * 3. 两个单词完全不同的情况
 * 4. 一个单词为空，另一个单词非空的情况
 * 5. 单词长度达到边界的情况
 * 
 * 与LCS的关系：
 * 编辑距离问题是LCS问题的一个扩展，通过不同的操作代价计算序列转换的最小成本。
 * 当只允许删除操作时，编辑距离退化为LCS问题的变种。
 */
public class LeetCode72_Edit_Distance {
    
    /**
     * 基础动态规划解法
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     */
    public static int minDistance(String word1, String word2) {
        // 异常处理：检查输入是否为空
        if (word1 == null || word2 == null) {
            throw new IllegalArgumentException("输入单词不能为null");
        }
        
        int m = word1.length();
        int n = word2.length();
        
        // 特殊情况处理
        if (m == 0) return n; // word1为空，需要插入n个字符
        if (n == 0) return m; // word2为空，需要删除m个字符
        
        // dp[i][j] 表示将word1的前i个字符转换为word2的前j个字符所需的最小操作数
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件
        // 将空字符串转换为word2的前j个字符需要j次插入操作
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        
        // 将word1的前i个字符转换为空字符串需要i次删除操作
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        
        // 填充dp表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // 字符相同，不需要操作
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 字符不同，取三种操作的最小值加1
                    // dp[i-1][j] : 删除word1的第i个字符
                    // dp[i][j-1] : 在word1中插入word2的第j个字符
                    // dp[i-1][j-1] : 将word1的第i个字符替换为word2的第j个字符
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 空间优化版本
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(min(m,n))
     * 
     * 优化思路：
     * 1. 使用滚动数组，只保留当前行和上一行的状态
     * 2. 确保word1是较短的字符串，减少空间使用
     */
    public static int minDistanceOptimized(String word1, String word2) {
        // 异常处理
        if (word1 == null || word2 == null) {
            throw new IllegalArgumentException("输入单词不能为null");
        }
        
        int m = word1.length();
        int n = word2.length();
        
        // 特殊情况处理
        if (m == 0) return n;
        if (n == 0) return m;
        
        // 空间优化：确保word1是较短的字符串
        if (m > n) {
            return minDistanceOptimized(word2, word1);
        }
        
        // 使用两行数组存储状态
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];
        
        // 初始化第一行（对应dp[0][j]）
        for (int i = 0; i <= m; i++) {
            prev[i] = i;
        }
        
        // 填充dp表
        for (int j = 1; j <= n; j++) {
            // 初始化当前行的第一个元素（对应dp[j][0]）
            curr[0] = j;
            
            for (int i = 1; i <= m; i++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    curr[i] = prev[i - 1];
                } else {
                    curr[i] = Math.min(Math.min(prev[i], curr[i - 1]), prev[i - 1]) + 1;
                }
            }
            
            // 交换数组，准备下一轮计算
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[m];
    }
    
    /**
     * 进一步空间优化版本（使用一维数组）
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(min(m,n))
     */
    public static int minDistanceSuperOptimized(String word1, String word2) {
        if (word1 == null || word2 == null) {
            throw new IllegalArgumentException("输入单词不能为null");
        }
        
        int m = word1.length();
        int n = word2.length();
        
        if (m == 0) return n;
        if (n == 0) return m;
        
        // 确保word1是较短的字符串
        if (m > n) {
            return minDistanceSuperOptimized(word2, word1);
        }
        
        int[] dp = new int[m + 1];
        
        // 初始化：将空字符串转换为word1的前i个字符需要i次删除操作
        for (int i = 0; i <= m; i++) {
            dp[i] = i;
        }
        
        for (int j = 1; j <= n; j++) {
            int prev = dp[0]; // 保存左上角的值
            dp[0] = j; // 当前行的第一个元素
            
            for (int i = 1; i <= m; i++) {
                int temp = dp[i]; // 保存当前值，用于下一轮计算
                
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i] = prev;
                } else {
                    dp[i] = Math.min(Math.min(dp[i], dp[i - 1]), prev) + 1;
                }
                
                prev = temp; // 更新左上角的值
            }
        }
        
        return dp[m];
    }
    
    /**
     * 单元测试方法
     * 验证算法的正确性和鲁棒性
     */
    public static void main(String[] args) {
        // 测试用例1：基本功能测试
        String word1 = "horse";
        String word2 = "ros";
        System.out.println("测试用例1 - 基础功能:");
        System.out.println("word1 = " + word1 + ", word2 = " + word2);
        System.out.println("基础版本: " + minDistance(word1, word2));
        System.out.println("优化版本: " + minDistanceOptimized(word1, word2));
        System.out.println("超级优化: " + minDistanceSuperOptimized(word1, word2));
        System.out.println("期望结果: 3");
        System.out.println();
        
        // 测试用例2：相同单词
        word1 = "intention";
        word2 = "intention";
        System.out.println("测试用例2 - 相同单词:");
        System.out.println("word1 = " + word1 + ", word2 = " + word2);
        System.out.println("基础版本: " + minDistance(word1, word2));
        System.out.println("优化版本: " + minDistanceOptimized(word1, word2));
        System.out.println("超级优化: " + minDistanceSuperOptimized(word1, word2));
        System.out.println("期望结果: 0");
        System.out.println();
        
        // 测试用例3：完全不同的单词
        word1 = "abc";
        word2 = "def";
        System.out.println("测试用例3 - 完全不同单词:");
        System.out.println("word1 = " + word1 + ", word2 = " + word2);
        System.out.println("基础版本: " + minDistance(word1, word2));
        System.out.println("优化版本: " + minDistanceOptimized(word1, word2));
        System.out.println("超级优化: " + minDistanceSuperOptimized(word1, word2));
        System.out.println("期望结果: 3");
        System.out.println();
        
        // 测试用例4：空字符串
        word1 = "";
        word2 = "abc";
        System.out.println("测试用例4 - 空字符串:");
        System.out.println("word1 = " + word1 + ", word2 = " + word2);
        System.out.println("基础版本: " + minDistance(word1, word2));
        System.out.println("优化版本: " + minDistanceOptimized(word1, word2));
        System.out.println("超级优化: " + minDistanceSuperOptimized(word1, word2));
        System.out.println("期望结果: 3");
        System.out.println();
        
        // 测试用例5：两个空字符串
        word1 = "";
        word2 = "";
        System.out.println("测试用例5 - 两个空字符串:");
        System.out.println("word1 = " + word1 + ", word2 = " + word2);
        System.out.println("基础版本: " + minDistance(word1, word2));
        System.out.println("优化版本: " + minDistanceOptimized(word1, word2));
        System.out.println("超级优化: " + minDistanceSuperOptimized(word1, word2));
        System.out.println("期望结果: 0");
        System.out.println();
        
        // 测试用例6：LeetCode官方示例
        word1 = "intention";
        word2 = "execution";
        System.out.println("测试用例6 - LeetCode示例:");
        System.out.println("word1 = " + word1 + ", word2 = " + word2);
        System.out.println("基础版本: " + minDistance(word1, word2));
        System.out.println("优化版本: " + minDistanceOptimized(word1, word2));
        System.out.println("超级优化: " + minDistanceSuperOptimized(word1, word2));
        System.out.println("期望结果: 5");
        System.out.println();
        
        // 性能测试：大规模数据
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb1.append('a');
            sb2.append('b');
        }
        word1 = sb1.toString();
        word2 = sb2.toString();
        
        long startTime = System.currentTimeMillis();
        int result = minDistanceOptimized(word1, word2);
        long endTime = System.currentTimeMillis();
        
        System.out.println("性能测试 - 100个字符:");
        System.out.println("结果: " + result);
        System.out.println("耗时: " + (endTime - startTime) + "ms");
        System.out.println("期望结果: 100");
    }
    
    /**
     * 复杂度分析详细计算：
     * 
     * 基础版本：
     * - 时间：外层循环m次，内层循环n次，每次操作O(1) → O(m*n)
     * - 空间：dp数组大小(m+1)*(n+1) → O(m*n)
     * 
     * 优化版本：
     * - 时间：同上 → O(m*n)
     * - 空间：两个数组，每个大小min(m,n)+1 → O(min(m,n))
     * 
     * 超级优化版本：
     * - 时间：同上 → O(m*n)  
     * - 空间：一个数组，大小min(m,n)+1 → O(min(m,n))
     * 
     * 最优解确认：
     * 当前实现的时间复杂度O(m*n)是最优的，因为必须比较所有字符对。
     * 空间复杂度O(min(m,n))也是最优的，无法进一步优化。
     */
}