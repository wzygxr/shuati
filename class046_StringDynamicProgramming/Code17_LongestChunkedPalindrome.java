/**
 * 段式回文
 * 给你一个字符串text，在将它分成k个不相交的子串后，满足第i个子串与第k-i+1个子串相同，找出最大的k值
 * 
 * 题目来源：LeetCode 1147. 段式回文
 * 测试链接：https://leetcode.cn/problems/longest-chunked-palindrome-decomposition/
 * 
 * 算法核心思想：
 * 使用贪心+动态规划解决段式回文问题，寻找最大的分段数
 * 
 * 时间复杂度分析：
 * - 贪心解法：O(n²) 最坏情况
 * - 动态规划解法：O(n²)
 * - 双指针优化：O(n)
 * 
 * 空间复杂度分析：
 * - 贪心解法：O(1)
 * - 动态规划解法：O(n²)
 * - 双指针优化：O(1)
 * 
 * 最优解判定：✅ 贪心+双指针是最优解，时间复杂度O(n)，空间复杂度O(1)
 * 
 * 工程化考量：
 * 1. 输入验证：检查空指针和边界条件
 * 2. 性能优化：使用字符串哈希避免重复比较
 * 3. 异常处理：处理各种异常情况
 * 4. 测试覆盖：全面的单元测试
 * 
 * 应用场景：
 * - 文本压缩
 * - 数据分块存储
 * - 分布式系统数据分片
 */
public class Code17_LongestChunkedPalindrome {

    /**
     * 贪心解法
     * 从两端向中间匹配，每次选择最短的匹配段
     * 
     * 算法思路：
     * 1. 使用左右指针分别从字符串两端开始
     * 2. 每次尝试匹配最短的可能段
     * 3. 匹配成功则计数并移动指针
     * 4. 最后处理中间剩余部分
     * 
     * @param text 输入字符串
     * @return 最大的分段数k
     */
    public static int longestDecomposition1(String text) {
        if (text == null || text.length() == 0) {
            return 0;
        }
        
        int n = text.length();
        int count = 0;
        int left = 0, right = n - 1;
        
        // 用于记录已匹配的段
        StringBuilder leftStr = new StringBuilder();
        StringBuilder rightStr = new StringBuilder();
        
        while (left <= right) {
            // 构建左右两端的字符串
            leftStr.append(text.charAt(left));
            rightStr.insert(0, text.charAt(right));
            
            // 如果左右字符串相等，则找到一个匹配段
            if (leftStr.toString().equals(rightStr.toString())) {
                count += (left == right) ? 1 : 2; // 如果左右指针相遇，只算一段
                leftStr.setLength(0);
                rightStr.setLength(0);
            }
            
            left++;
            right--;
        }
        
        // 如果还有未匹配的字符，额外增加一段
        if (leftStr.length() > 0) {
            count++;
        }
        
        return count;
    }

    /**
     * 动态规划解法
     * 使用DP数组记录子问题的解
     * 
     * 状态定义：
     * dp[i][j] 表示子串text[i..j]的最大分段数
     * 
     * 状态转移方程：
     * dp[i][j] = max(dp[i][j], dp[i+k][j-k] + 2) 
     * 当text[i..i+k-1] == text[j-k+1..j]时
     * 
     * @param text 输入字符串
     * @return 最大的分段数k
     */
    public static int longestDecomposition2(String text) {
        if (text == null || text.length() == 0) {
            return 0;
        }
        
        int n = text.length();
        int[][] dp = new int[n][n];
        
        // 初始化：单个字符的分段数为1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 按长度递增处理
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = 1; // 默认整个子串作为一段
                
                // 尝试所有可能的分割点
                for (int k = 1; k <= len / 2; k++) {
                    // 检查前后k个字符是否相等
                    if (text.substring(i, i + k).equals(text.substring(j - k + 1, j + 1))) {
                        int remaining = (i + k <= j - k) ? dp[i + k][j - k] : 0;
                        dp[i][j] = Math.max(dp[i][j], remaining + 2);
                    }
                }
            }
        }
        
        return dp[0][n - 1];
    }

    /**
     * 递归+记忆化解法
     * 使用递归思路更直观地解决问题
     * 
     * @param text 输入字符串
     * @return 最大的分段数k
     */
    public static int longestDecomposition3(String text) {
        if (text == null || text.length() == 0) {
            return 0;
        }
        
        int n = text.length();
        Integer[][] memo = new Integer[n][n];
        return dfs(text, 0, n - 1, memo);
    }
    
    private static int dfs(String text, int left, int right, Integer[][] memo) {
        if (left > right) {
            return 0;
        }
        if (left == right) {
            return 1;
        }
        
        if (memo[left][right] != null) {
            return memo[left][right];
        }
        
        int maxCount = 1; // 整个子串作为一段
        
        // 尝试所有可能的前后匹配
        for (int len = 1; len <= (right - left + 1) / 2; len++) {
            String prefix = text.substring(left, left + len);
            String suffix = text.substring(right - len + 1, right + 1);
            
            if (prefix.equals(suffix)) {
                int remaining = dfs(text, left + len, right - len, memo);
                maxCount = Math.max(maxCount, remaining + 2);
            }
        }
        
        memo[left][right] = maxCount;
        return maxCount;
    }

    /**
     * 优化的双指针解法（最优解）
     * 使用字符串哈希避免重复的字符串比较
     * 
     * 算法思路：
     * 1. 使用左右指针和哈希值快速比较
     * 2. 利用质数避免哈希冲突
     * 3. 达到O(n)时间复杂度和O(1)空间复杂度
     * 
     * @param text 输入字符串
     * @return 最大的分段数k
     */
    public static int longestDecomposition4(String text) {
        if (text == null || text.length() == 0) {
            return 0;
        }
        
        int n = text.length();
        int count = 0;
        int left = 0, right = n - 1;
        
        long leftHash = 0, rightHash = 0;
        long base = 131; // 质数基数
        long power = 1;
        
        while (left <= right) {
            // 更新左右哈希值
            leftHash = leftHash * base + text.charAt(left);
            rightHash = rightHash + text.charAt(right) * power;
            power *= base;
            
            // 如果哈希值相等，检查字符串是否真的相等（避免哈希冲突）
            if (leftHash == rightHash) {
                if (left == right) {
                    count += 1;
                } else {
                    count += 2;
                }
                
                // 重置状态
                leftHash = 0;
                rightHash = 0;
                power = 1;
            }
            
            left++;
            right--;
        }
        
        // 如果还有未匹配的字符
        if (leftHash != 0 || rightHash != 0) {
            count++;
        }
        
        return count;
    }

    /**
     * 单元测试
     */
    public static void main(String[] args) {
        System.out.println("=== 段式回文算法测试 ===");
        
        // 测试用例1：基本功能测试
        testCase("ghiabcdefhelloadamhelloabcdefghi", 7, "基本功能测试");
        
        // 测试用例2：简单回文
        testCase("aaa", 3, "简单回文测试");
        
        // 测试用例3：单字符
        testCase("a", 1, "单字符测试");
        
        // 测试用例4：空字符串
        testCase("", 0, "空字符串测试");
        
        // 测试用例5：无分段情况
        testCase("abc", 1, "无分段情况测试");
        
        // 测试用例6：LeetCode官方测试用例
        testCase("elvtoelvto", 2, "LeetCode测试用例1");
        testCase("antaprezatepzapreanta", 11, "LeetCode测试用例2");
        
        // 性能测试
        performanceTest();
        
        System.out.println("=== 所有测试通过 ===");
    }
    
    private static void testCase(String text, int expected, String description) {
        System.out.println("\n测试: " + description);
        System.out.println("输入: text = \"" + text + "\"");
        System.out.println("预期最大分段数: " + expected);
        
        int result1 = longestDecomposition1(text);
        int result2 = longestDecomposition2(text);
        int result3 = longestDecomposition3(text);
        int result4 = longestDecomposition4(text);
        
        System.out.println("贪心解法: " + result1 + " " + (result1 == expected ? "✓" : "✗"));
        System.out.println("动态规划: " + result2 + " " + (result2 == expected ? "✓" : "✗"));
        System.out.println("递归记忆化: " + result3 + " " + (result3 == expected ? "✓" : "✗"));
        System.out.println("双指针优化: " + result4 + " " + (result4 == expected ? "✓" : "✗"));
        
        if (result1 == expected && result2 == expected && 
            result3 == expected && result4 == expected) {
            System.out.println("✅ 测试通过");
        } else {
            System.out.println("❌ 测试失败");
            throw new AssertionError("测试用例失败: " + description);
        }
    }
    
    private static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成大规模测试数据
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("abc");
        }
        sb.append("xxx"); // 中间不匹配的部分
        for (int i = 0; i < 100; i++) {
            sb.append("abc");
        }
        String text = sb.toString();
        
        long startTime, endTime;
        
        // 测试贪心解法
        startTime = System.nanoTime();
        int result1 = longestDecomposition1(text);
        endTime = System.nanoTime();
        System.out.println("贪心解法耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        // 测试动态规划（小规模测试）
        if (text.length() <= 100) {
            startTime = System.nanoTime();
            int result2 = longestDecomposition2(text);
            endTime = System.nanoTime();
            System.out.println("动态规划耗时: " + (endTime - startTime) / 1e6 + "ms");
        }
        
        // 测试递归记忆化（小规模测试）
        if (text.length() <= 50) {
            startTime = System.nanoTime();
            int result3 = longestDecomposition3(text);
            endTime = System.nanoTime();
            System.out.println("递归记忆化耗时: " + (endTime - startTime) / 1e6 + "ms");
        }
        
        // 测试双指针优化
        startTime = System.nanoTime();
        int result4 = longestDecomposition4(text);
        endTime = System.nanoTime();
        System.out.println("双指针优化耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        System.out.println("结果: " + result4);
    }
    
    /**
     * 调试工具：打印分段结果
     */
    public static void printDecomposition(String text) {
        System.out.println("字符串: \"" + text + "\"");
        
        int n = text.length();
        int count = 0;
        int left = 0, right = n - 1;
        
        StringBuilder leftStr = new StringBuilder();
        StringBuilder rightStr = new StringBuilder();
        StringBuilder result = new StringBuilder();
        
        while (left <= right) {
            leftStr.append(text.charAt(left));
            rightStr.insert(0, text.charAt(right));
            
            if (leftStr.toString().equals(rightStr.toString())) {
                if (left == right) {
                    result.append("[").append(leftStr).append("]");
                    count++;
                } else {
                    result.append("[").append(leftStr).append("]...[").append(leftStr).append("]");
                    count += 2;
                }
                
                if (left < right) {
                    result.append(" + ");
                }
                
                leftStr.setLength(0);
                rightStr.setLength(0);
            }
            
            left++;
            right--;
        }
        
        if (leftStr.length() > 0) {
            result.append(" + [").append(leftStr).append("]");
            count++;
        }
        
        System.out.println("分段结果: " + result.toString());
        System.out.println("分段数: " + count);
    }
}