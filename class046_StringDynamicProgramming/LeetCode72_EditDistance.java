// 编辑距离 (Edit Distance)
// 给你两个单词 word1 和 word2，计算将 word1 转换成 word2 所使用的最少操作数
// 你可以对一个单词进行如下三种操作：
// 插入一个字符
// 删除一个字符
// 替换一个字符
// 
// 题目来源：LeetCode 72. 编辑距离
// 测试链接：https://leetcode.cn/problems/edit-distance/
//
// 算法核心思想：
// 使用动态规划解决字符串编辑距离问题，通过构建二维DP表来计算最小编辑操作次数
//
// 时间复杂度分析：
// - 基础版本：O(n*m)，其中n为word1的长度，m为word2的长度
// - 空间优化版本：O(n*m)时间，O(min(n,m))空间
//
// 空间复杂度分析：
// - 基础版本：O(n*m)
// - 空间优化版本：O(min(n,m))
//
// 最优解判定：✅ 是最优解，时间复杂度无法进一步优化，空间复杂度已优化到O(min(n,m))
//
// 工程化考量：
// 1. 异常处理：检查输入参数合法性
// 2. 边界条件：处理空字符串和极端情况
// 3. 性能优化：使用滚动数组减少空间占用
// 4. 代码可读性：添加详细注释和测试用例
//
// 与其他领域的联系：
// - 自然语言处理：文本相似度计算、拼写检查
// - 生物信息学：DNA序列比对、基因序列分析
// - 信息检索：文档相似度计算、搜索引擎优化
// - 版本控制：Git等版本控制系统中的diff算法

public class LeetCode72_EditDistance {
    
    /*
     * 算法思路：
     * 使用动态规划解决编辑距离问题
     * dp[i][j] 表示将 word1 的前 i 个字符转换为 word2 的前 j 个字符所需的最小操作数
     * 
     * 状态转移方程：
     * 如果 word1[i-1] == word2[j-1]，则不需要操作
     *   dp[i][j] = dp[i-1][j-1]
     * 否则，取三种操作的最小值：
     *   dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
     * 其中：
     *   dp[i-1][j] + 1 表示删除操作（删除 word1 的第 i 个字符）
     *   dp[i][j-1] + 1 表示插入操作（在 word1 的第 i 个位置后插入 word2 的第 j 个字符）
     *   dp[i-1][j-1] + 1 表示替换操作（将 word1 的第 i 个字符替换为 word2 的第 j 个字符）
     * 
     * 边界条件：
     * dp[i][0] = i，表示将 word1 的前 i 个字符转换为空字符串需要 i 次删除操作
     * dp[0][j] = j，表示将空字符串转换为 word2 的前 j 个字符需要 j 次插入操作
     * 
     * 时间复杂度：O(m*n)，其中m为word1的长度，n为word2的长度
     * 空间复杂度：O(m*n)
     */
    public int minDistance1(String word1, String word2) {
        // 输入验证
        if (word1 == null || word2 == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        int m = word1.length();
        int n = word2.length();
        // dp[i][j] 表示word1[0...i-1]转换为word2[0...j-1]所需的最小操作数
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // 将word1转换为空字符串，需要i次删除操作
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // 将空字符串转换为word2，需要j次插入操作
        }
        
        // 填充dp表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // 当前字符相同，不需要操作
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 取三种操作的最小值
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        return dp[m][n];
    }

    /*
     * 空间优化版本
     * 观察状态转移方程，dp[i][j]只依赖于dp[i-1][j-1]、dp[i-1][j]和dp[i][j-1]
     * 可以使用一维数组优化空间复杂度
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(min(m,n))
     */
    public int minDistance2(String word1, String word2) {
        // 输入验证
        if (word1 == null || word2 == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        // 为了节省空间，确保第二个参数是较短的字符串
        if (word1.length() < word2.length()) {
            String temp = word1;
            word1 = word2;
            word2 = temp;
        }
        
        int m = word1.length();
        int n = word2.length();
        // 使用一维数组存储当前行的数据
        int[] dp = new int[n + 1];
        // 初始化dp[0][j] = j
        for (int j = 0; j <= n; j++) {
            dp[j] = j;
        }
        
        // 按行填充dp表
        for (int i = 1; i <= m; i++) {
            int pre = dp[0]; // 保存左上角的值(dp[i-1][j-1])
            dp[0] = i; // 更新dp[i][0] = i
            for (int j = 1; j <= n; j++) {
                int temp = dp[j]; // 保存当前dp[j]，用于下一轮的pre
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // 当前字符相同，不需要操作
                    dp[j] = pre;
                } else {
                    // 取三种操作的最小值
                    dp[j] = Math.min(Math.min(dp[j], dp[j - 1]), pre) + 1;
                }
                pre = temp; // 更新pre为下一轮的左上角值
            }
        }
        
        return dp[n];
    }

    // 测试函数
    public static void main(String[] args) {
        LeetCode72_EditDistance solution = new LeetCode72_EditDistance();
        
        // 测试用例1: word1 = "horse", word2 = "ros"
        // 预期输出: 3
        // 解释: 
        // horse -> rorse (替换 'h' 为 'r')
        // rorse -> rose (删除 'r')
        // rose -> ros (删除 'e')
        System.out.println("Test 1: " + solution.minDistance1("horse", "ros")); // 应输出3
        System.out.println("Test 1 (Space Optimized): " + solution.minDistance2("horse", "ros")); // 应输出3
        
        // 测试用例2: word1 = "intention", word2 = "execution"
        // 预期输出: 5
        // 解释: 
        // intention -> inention (删除 't')
        // inention -> enention (替换 'i' 为 'e')
        // enention -> exention (替换 'n' 为 'x')
        // exention -> exection (替换 'n' 为 'c')
        // exection -> execution (插入 'u')
        System.out.println("Test 2: " + solution.minDistance1("intention", "execution")); // 应输出5
        System.out.println("Test 2 (Space Optimized): " + solution.minDistance2("intention", "execution")); // 应输出5
        
        // 边界测试: 空字符串
        System.out.println("Test 3 (Empty String): " + solution.minDistance1("", "abc")); // 应输出3
        System.out.println("Test 3 (Empty String, Space Optimized): " + solution.minDistance2("", "abc")); // 应输出3
        
        // 边界测试: 相同字符串
        System.out.println("Test 4 (Same String): " + solution.minDistance1("abc", "abc")); // 应输出0
        System.out.println("Test 4 (Same String, Space Optimized): " + solution.minDistance2("abc", "abc")); // 应输出0
        
        // 边界测试: 单字符不同
        System.out.println("Test 5 (Single Char Different): " + solution.minDistance1("a", "b")); // 应输出1
        System.out.println("Test 5 (Single Char Different, Space Optimized): " + solution.minDistance2("a", "b")); // 应输出1
    }
}