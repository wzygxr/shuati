// 编辑距离 (Edit Distance)
// 给你两个单词 word1 和 word2，请你计算出将 word1 转换成 word2 所使用的最少操作数。
// 你可以对一个单词进行如下三种操作：
// 插入一个字符
// 删除一个字符
// 替换一个字符
// 测试链接 : https://leetcode.cn/problems/edit-distance/
public class Code10_EditDistance {

    // 方法1：暴力递归解法
    // 时间复杂度：O(3^(m+n)) - 指数级时间复杂度，效率极低
    // 空间复杂度：O(m+n) - 递归调用栈的深度
    // 问题：存在大量重复计算，效率低下
    public static int minDistance1(String word1, String word2) {
        return f1(word1.toCharArray(), word2.toCharArray(), word1.length() - 1, word2.length() - 1);
    }

    // str1[0..i] 与 str2[0..j] 的编辑距离
    public static int f1(char[] str1, char[] str2, int i, int j) {
        // base case
        if (i == -1) {
            return j + 1; // str1为空，需要插入j+1个字符
        }
        if (j == -1) {
            return i + 1; // str2为空，需要删除i+1个字符
        }
        if (str1[i] == str2[j]) {
            return f1(str1, str2, i - 1, j - 1); // 字符相同，不需要操作
        } else {
            // 字符不同，三种操作中取最小值
            int replace = f1(str1, str2, i - 1, j - 1) + 1; // 替换
            int delete = f1(str1, str2, i - 1, j) + 1; // 删除
            int insert = f1(str1, str2, i, j - 1) + 1; // 插入
            return Math.min(replace, Math.min(delete, insert));
        }
    }

    // 方法2：记忆化搜索（自顶向下动态规划）
    // 时间复杂度：O(m*n) - 每个状态只计算一次
    // 空间复杂度：O(m*n) - dp数组和递归调用栈
    // 优化：通过缓存已经计算的结果避免重复计算
    public static int minDistance2(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = -1;
            }
        }
        return f2(word1.toCharArray(), word2.toCharArray(), m - 1, n - 1, dp);
    }

    // str1[0..i] 与 str2[0..j] 的编辑距离
    public static int f2(char[] str1, char[] str2, int i, int j, int[][] dp) {
        if (i == -1) {
            return j + 1;
        }
        if (j == -1) {
            return i + 1;
        }
        if (dp[i][j] != -1) {
            return dp[i][j];
        }
        int ans;
        if (str1[i] == str2[j]) {
            ans = f2(str1, str2, i - 1, j - 1, dp);
        } else {
            int replace = f2(str1, str2, i - 1, j - 1, dp) + 1;
            int delete = f2(str1, str2, i - 1, j, dp) + 1;
            int insert = f2(str1, str2, i, j - 1, dp) + 1;
            ans = Math.min(replace, Math.min(delete, insert));
        }
        dp[i][j] = ans;
        return ans;
    }

    // 方法3：动态规划（自底向上）
    // 时间复杂度：O(m*n) - 需要填满整个dp表
    // 空间复杂度：O(m*n) - dp数组存储所有状态
    // 优化：避免了递归调用的开销
    public static int minDistance3(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        // dp[i][j] 表示 word1[0..i-1] 和 word2[0..j-1] 的编辑距离
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // word2为空，需要删除i个字符
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // word1为空，需要插入j个字符
        }
        
        // 填表过程
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // 字符相同，不需要操作
                } else {
                    // 字符不同，三种操作中取最小值
                    int replace = dp[i - 1][j - 1] + 1; // 替换
                    int delete = dp[i - 1][j] + 1; // 删除
                    int insert = dp[i][j - 1] + 1; // 插入
                    dp[i][j] = Math.min(replace, Math.min(delete, insert));
                }
            }
        }
        return dp[m][n];
    }

    // 方法4：空间优化的动态规划
    // 时间复杂度：O(m*n) - 仍然需要计算所有状态
    // 空间复杂度：O(min(m,n)) - 只保存必要的状态值
    // 优化：只保存必要的状态，大幅减少空间使用
    public static int minDistance4(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        // 确保word1是较短的字符串，以优化空间
        if (m > n) {
            return minDistance4(word2, word1);
        }
        
        // 使用两个一维数组来代替二维数组
        int[] dp = new int[m + 1];
        int[] pre = new int[m + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            pre[i] = i;
        }
        
        for (int j = 1; j <= n; j++) {
            dp[0] = j; // word1为空，需要插入j个字符
            for (int i = 1; i <= m; i++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i] = pre[i - 1]; // 字符相同，不需要操作
                } else {
                    // 字符不同，三种操作中取最小值
                    int replace = pre[i - 1] + 1; // 替换
                    int delete = pre[i] + 1; // 删除
                    int insert = dp[i - 1] + 1; // 插入
                    dp[i] = Math.min(replace, Math.min(delete, insert));
                }
            }
            // 交换dp和pre数组
            int[] temp = pre;
            pre = dp;
            dp = temp;
        }
        return pre[m];
    }

    // 测试用例和性能对比
    public static void main(String[] args) {
        System.out.println("测试编辑距离实现：");
        
        // 测试用例1
        String word1 = "horse";
        String word2 = "ros";
        System.out.println("word1 = \"" + word1 + "\", word2 = \"" + word2 + "\"");
        System.out.println("方法3 (动态规划): " + minDistance3(word1, word2));
        System.out.println("方法4 (空间优化): " + minDistance4(word1, word2));
        
        // 测试用例2
        word1 = "intention";
        word2 = "execution";
        System.out.println("\nword1 = \"" + word1 + "\", word2 = \"" + word2 + "\"");
        System.out.println("方法3 (动态规划): " + minDistance3(word1, word2));
        System.out.println("方法4 (空间优化): " + minDistance4(word1, word2));
        
        // 测试用例3
        word1 = "a";
        word2 = "b";
        System.out.println("\nword1 = \"" + word1 + "\", word2 = \"" + word2 + "\"");
        System.out.println("方法3 (动态规划): " + minDistance3(word1, word2));
        System.out.println("方法4 (空间优化): " + minDistance4(word1, word2));
    }
}