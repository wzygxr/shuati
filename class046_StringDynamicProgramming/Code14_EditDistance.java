import java.util.Arrays;

/**
 * 编辑距离 (Edit Distance / Levenshtein Distance)
 * 给你两个单词 word1 和 word2，计算将 word1 转换成 word2 所使用的最少操作数
 * 允许的三种操作：
 * 1. 插入一个字符
 * 2. 删除一个字符  
 * 3. 替换一个字符
 * 
 * 题目来源：LeetCode 72. 编辑距离
 * 测试链接：https://leetcode.cn/problems/edit-distance/
 * 
 * 算法核心思想：
 * 动态规划解决经典的字符串编辑距离问题，是自然语言处理中的基础算法
 * 
 * 时间复杂度分析：
 * - 基础版本：O(m*n)，其中m为word1的长度，n为word2的长度
 * - 空间优化版本：O(m*n)时间，O(min(m,n))空间
 * 
 * 空间复杂度分析：
 * - 基础版本：O(m*n)
 * - 空间优化版本：O(min(m,n))
 * 
 * 最优解判定：✅ 是最优解，时间复杂度无法进一步优化，空间复杂度已优化到最优
 * 
 * 工程化考量：
 * 1. 输入验证：检查空指针和边界条件
 * 2. 性能优化：空间优化和算法优化
 * 3. 异常处理：处理各种异常情况
 * 4. 测试覆盖：全面的单元测试
 * 
 * 应用场景：
 * - 拼写检查与纠错
 * - 生物信息学中的DNA序列比对
 * - 自然语言处理中的文本相似度计算
 * - 版本控制系统中的文件差异比较
 */
public class Code14_EditDistance {

    /**
     * 基础动态规划解法 - 二维DP数组
     * 使用标准的动态规划方法解决编辑距离问题
     * 
     * 状态定义：
     * dp[i][j] 表示将字符串word1的前i个字符转换为字符串word2的前j个字符所需的最小操作数
     * 
     * 状态转移方程：
     * 1. 如果word1[i-1] == word2[j-1]：字符匹配，不需要操作
     *    dp[i][j] = dp[i-1][j-1]
     * 2. 如果word1[i-1] != word2[j-1]：需要选择最小代价的操作
     *    dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
     *    其中：
     *    - dp[i-1][j] + 1：删除word1的第i个字符
     *    - dp[i][j-1] + 1：在word1的第i个位置后插入word2的第j个字符  
     *    - dp[i-1][j-1] + 1：将word1的第i个字符替换为word2的第j个字符
     * 
     * 边界条件：
     * - dp[i][0] = i：将word1的前i个字符转换为空字符串需要i次删除操作
     * - dp[0][j] = j：将空字符串转换为word2的前j个字符需要j次插入操作
     * 
     * @param word1 源字符串
     * @param word2 目标字符串
     * @return 最小编辑距离
     */
    public static int minDistance1(String word1, String word2) {
        // 输入验证
        if (word1 == null || word2 == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        int m = word1.length();
        int n = word2.length();
        
        // 边界情况处理
        if (m == 0) return n; // word1为空，需要n次插入
        if (n == 0) return m; // word2为空，需要m次删除
        
        // dp[i][j]: word1[0..i-1]转换为word2[0..j-1]的最小操作数
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // 删除所有字符
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // 插入所有字符
        }
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // 字符匹配，不需要操作
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 取三种操作的最小值
                    int deleteOp = dp[i - 1][j] + 1;     // 删除操作
                    int insertOp = dp[i][j - 1] + 1;     // 插入操作
                    int replaceOp = dp[i - 1][j - 1] + 1; // 替换操作
                    dp[i][j] = Math.min(Math.min(deleteOp, insertOp), replaceOp);
                }
            }
        }
        
        return dp[m][n];
    }

    /**
     * 空间优化版本 - 一维DP数组
     * 通过观察状态转移的依赖关系，使用滚动数组技术优化空间复杂度
     * 
     * 优化原理：
     * - dp[i][j]只依赖于当前行和上一行的数据
     * - 可以使用一维数组+临时变量保存必要的历史值
     * - 通过交换字符串顺序确保使用较短的数组
     * 
     * 关键技巧：
     * 1. 让较短的字符串作为内层循环，减少空间占用
     * 2. 使用pre变量保存dp[i-1][j-1]的值
     * 3. 使用temp变量暂存当前值用于下一轮计算
     * 
     * @param word1 源字符串
     * @param word2 目标字符串
     * @return 最小编辑距离
     */
    public static int minDistance2(String word1, String word2) {
        // 输入验证
        if (word1 == null || word2 == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        // 交换字符串顺序，确保第二个字符串较短
        if (word1.length() < word2.length()) {
            String temp = word1;
            word1 = word2;
            word2 = temp;
        }
        
        int m = word1.length();
        int n = word2.length();
        
        // 边界情况处理
        if (m == 0) return n;
        if (n == 0) return m;
        
        // 使用一维数组优化空间
        int[] dp = new int[n + 1];
        
        // 初始化第一行（空字符串转换为word2的前j个字符）
        for (int j = 0; j <= n; j++) {
            dp[j] = j;
        }
        
        // 动态规划过程
        for (int i = 1; i <= m; i++) {
            int pre = dp[0]; // 保存左上角的值(dp[i-1][j-1])
            dp[0] = i;       // 更新当前行的第一个元素
            
            for (int j = 1; j <= n; j++) {
                int temp = dp[j]; // 保存当前值，用于下一轮的pre
                
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // 字符匹配，不需要操作
                    dp[j] = pre;
                } else {
                    // 取三种操作的最小值
                    int deleteOp = dp[j] + 1;        // 删除操作（来自上方）
                    int insertOp = dp[j - 1] + 1;     // 插入操作（来自左方）
                    int replaceOp = pre + 1;          // 替换操作（来自左上角）
                    dp[j] = Math.min(Math.min(deleteOp, insertOp), replaceOp);
                }
                
                pre = temp; // 更新pre为当前轮的左上角值
            }
        }
        
        return dp[n];
    }

    /**
     * 带权重的编辑距离（扩展功能）
     * 不同的操作可以有不同的代价
     * 
     * @param word1 源字符串
     * @param word2 目标字符串
     * @param insertCost 插入操作的代价
     * @param deleteCost 删除操作的代价
     * @param replaceCost 替换操作的代价
     * @return 带权重的最小编辑距离
     */
    public static int minDistanceWithCost(String word1, String word2, 
                                         int insertCost, int deleteCost, int replaceCost) {
        if (word1 == null || word2 == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        int m = word1.length();
        int n = word2.length();
        
        if (m == 0) return n * insertCost;
        if (n == 0) return m * deleteCost;
        
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i * deleteCost; // 删除所有字符
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j * insertCost; // 插入所有字符
        }
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int deleteOp = dp[i - 1][j] + deleteCost;
                    int insertOp = dp[i][j - 1] + insertCost;
                    int replaceOp = dp[i - 1][j - 1] + replaceCost;
                    dp[i][j] = Math.min(Math.min(deleteOp, insertOp), replaceOp);
                }
            }
        }
        
        return dp[m][n];
    }

    /**
     * 重构编辑操作序列（扩展功能）
     * 不仅计算距离，还重构出具体的操作序列
     * 
     * @param word1 源字符串
     * @param word2 目标字符串
     * @return 操作序列的描述
     */
    public static String reconstructEditOperations(String word1, String word2) {
        if (word1 == null || word2 == null) {
            return "输入错误";
        }
        
        int m = word1.length();
        int n = word2.length();
        
        if (m == 0 && n == 0) return "无需操作";
        if (m == 0) return "插入 " + word2;
        if (n == 0) return "删除 " + word1;
        
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化DP表
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        // 重构操作序列
        StringBuilder operations = new StringBuilder();
        int i = m, j = n;
        
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && word1.charAt(i - 1) == word2.charAt(j - 1)) {
                // 字符匹配，无需操作
                operations.insert(0, "保留 '" + word1.charAt(i - 1) + "'\n");
                i--;
                j--;
            } else {
                int current = dp[i][j];
                
                if (i > 0 && j > 0 && dp[i - 1][j - 1] + 1 == current) {
                    // 替换操作
                    operations.insert(0, "将 '" + word1.charAt(i - 1) + "' 替换为 '" + word2.charAt(j - 1) + "'\n");
                    i--;
                    j--;
                } else if (i > 0 && dp[i - 1][j] + 1 == current) {
                    // 删除操作
                    operations.insert(0, "删除 '" + word1.charAt(i - 1) + "'\n");
                    i--;
                } else if (j > 0 && dp[i][j - 1] + 1 == current) {
                    // 插入操作
                    operations.insert(0, "插入 '" + word2.charAt(j - 1) + "'\n");
                    j--;
                }
            }
        }
        
        return operations.toString();
    }

    /**
     * 递归解法（带记忆化）
     * 用于理解问题本质和对比性能
     * 
     * @param word1 源字符串
     * @param word2 目标字符串
     * @return 最小编辑距离
     */
    public static int minDistanceRecursive(String word1, String word2) {
        if (word1 == null || word2 == null) {
            return 0;
        }
        
        int m = word1.length();
        int n = word2.length();
        
        if (m == 0) return n;
        if (n == 0) return m;
        
        int[][] memo = new int[m][n];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        
        return dfs(word1, word2, 0, 0, memo);
    }
    
    private static int dfs(String word1, String word2, int i, int j, int[][] memo) {
        if (i == word1.length()) {
            return word2.length() - j; // 需要插入剩余字符
        }
        if (j == word2.length()) {
            return word1.length() - i; // 需要删除剩余字符
        }
        
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        
        int result;
        if (word1.charAt(i) == word2.charAt(j)) {
            // 字符匹配，继续比较下一个字符
            result = dfs(word1, word2, i + 1, j + 1, memo);
        } else {
            // 三种操作的最小值
            int deleteOp = dfs(word1, word2, i + 1, j, memo) + 1;     // 删除
            int insertOp = dfs(word1, word2, i, j + 1, memo) + 1;     // 插入
            int replaceOp = dfs(word1, word2, i + 1, j + 1, memo) + 1; // 替换
            result = Math.min(Math.min(deleteOp, insertOp), replaceOp);
        }
        
        memo[i][j] = result;
        return result;
    }

    /**
     * 全面的单元测试
     * 覆盖各种边界情况和应用场景
     */
    public static void main(String[] args) {
        System.out.println("=== 编辑距离算法测试 ===");
        
        // 测试用例1：基本功能测试
        testCase("horse", "ros", 3, "基本功能测试");
        
        // 测试用例2：经典测试用例
        testCase("intention", "execution", 5, "经典测试用例");
        
        // 测试用例3：空字符串测试
        testCase("", "abc", 3, "空源字符串测试");
        testCase("abc", "", 3, "空目标字符串测试");
        testCase("", "", 0, "双空字符串测试");
        
        // 测试用例4：相同字符串测试
        testCase("abc", "abc", 0, "相同字符串测试");
        
        // 测试用例5：单字符测试
        testCase("a", "b", 1, "单字符不同测试");
        testCase("a", "a", 0, "单字符相同测试");
        
        // 测试用例6：LeetCode官方测试用例
        testCase("horse", "ros", 3, "LeetCode测试用例1");
        testCase("intention", "execution", 5, "LeetCode测试用例2");
        
        // 测试用例7：带权重编辑距离测试
        testWeightedDistance();
        
        // 测试用例8：重构操作序列测试
        testReconstruction();
        
        // 性能测试
        performanceTest();
        
        // 递归解法测试
        testRecursive();
        
        System.out.println("=== 所有测试通过 ===");
    }
    
    private static void testCase(String word1, String word2, int expected, String description) {
        System.out.println("\n测试: " + description);
        System.out.println("输入: word1 = \"" + word1 + "\", word2 = \"" + word2 + "\"");
        System.out.println("预期编辑距离: " + expected);
        
        int result1 = minDistance1(word1, word2);
        int result2 = minDistance2(word1, word2);
        
        System.out.println("方法1结果: " + result1 + " " + (result1 == expected ? "✓" : "✗"));
        System.out.println("方法2结果: " + result2 + " " + (result2 == expected ? "✓" : "✗"));
        
        if (result1 == expected && result2 == expected) {
            System.out.println("✅ 测试通过");
        } else {
            System.out.println("❌ 测试失败");
            throw new AssertionError("测试用例失败: " + description);
        }
    }
    
    private static void testWeightedDistance() {
        System.out.println("\n=== 带权重编辑距离测试 ===");
        
        String word1 = "kitten";
        String word2 = "sitting";
        
        // 标准权重（所有操作代价为1）
        int standard = minDistanceWithCost(word1, word2, 1, 1, 1);
        System.out.println("标准权重编辑距离: " + standard);
        
        // 高替换代价（替换代价为2）
        int highReplace = minDistanceWithCost(word1, word2, 1, 1, 2);
        System.out.println("高替换代价编辑距离: " + highReplace);
        
        // 禁止替换（替换代价为无穷大）
        int noReplace = minDistanceWithCost(word1, word2, 1, 1, Integer.MAX_VALUE);
        System.out.println("禁止替换编辑距离: " + noReplace);
    }
    
    private static void testReconstruction() {
        System.out.println("\n=== 重构编辑操作序列测试 ===");
        
        String word1 = "horse";
        String word2 = "ros";
        
        String operations = reconstructEditOperations(word1, word2);
        System.out.println("编辑操作序列:");
        System.out.println(operations);
        
        int distance = minDistance1(word1, word2);
        System.out.println("编辑距离: " + distance);
    }
    
    private static void testRecursive() {
        System.out.println("\n=== 递归解法测试 ===");
        
        String[][] testCases = {
            {"horse", "ros"},
            {"abc", "abc"},
            {"a", "b"}
        };
        
        for (String[] testCase : testCases) {
            String word1 = testCase[0];
            String word2 = testCase[1];
            
            int dpResult = minDistance1(word1, word2);
            int recursiveResult = minDistanceRecursive(word1, word2);
            
            System.out.printf("word1=\"%s\", word2=\"%s\": DP=%d, 递归=%d %s\n",
                word1, word2, dpResult, recursiveResult,
                dpResult == recursiveResult ? "✓" : "✗");
        }
    }
    
    private static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成大规模测试数据
        String word1 = "abcdefghij".repeat(100); // 1000字符
        String word2 = "acegikmoqs".repeat(50);  // 500字符
        
        long startTime, endTime;
        
        // 测试基础DP方法
        startTime = System.nanoTime();
        int result1 = minDistance1(word1, word2);
        endTime = System.nanoTime();
        System.out.println("基础DP耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        // 测试空间优化方法
        startTime = System.nanoTime();
        int result2 = minDistance2(word1, word2);
        endTime = System.nanoTime();
        System.out.println("优化DP耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        System.out.println("结果一致性: " + (result1 == result2 ? "✅" : "❌"));
        System.out.println("编辑距离: " + result1);
    }
    
    /**
     * 调试工具：打印DP表
     */
    public static void printDPTable(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化DP表
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        // 打印DP表
        System.out.println("编辑距离DP表:");
        System.out.print("    ");
        for (int j = 0; j <= n; j++) {
            System.out.printf("%3d", j);
        }
        System.out.println();
        
        for (int i = 0; i <= m; i++) {
            System.out.printf("%3d:", i);
            for (int j = 0; j <= n; j++) {
                System.out.printf("%3d", dp[i][j]);
            }
            System.out.println();
        }
        
        System.out.println("最小编辑距离: " + dp[m][n]);
    }
}