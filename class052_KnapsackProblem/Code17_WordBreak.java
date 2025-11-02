// 单词拆分
// 给你一个字符串 s 和一个字符串列表 wordDict 作为字典。
// 如果可以利用字典中出现的一个或多个单词拼接出 s 则返回 true。
// 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
// 测试链接 : https://leetcode.cn/problems/word-break/

/*
 * 算法详解：
 * 单词拆分问题可以看作是一个完全背包问题。我们将字符串s看作背包，字典中的单词看作物品，
 * 每个单词可以使用无限次（因为题目允许重复使用），目标是恰好装满背包。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i]表示字符串s的前i个字符是否可以被拆分为字典中的单词
 * 2. 状态转移方程：
 *    对于每个位置i，遍历字典中的每个单词word：
 *    如果i >= word.length()且s的子串s[i-word.length()..i]等于word，
 *    且dp[i-word.length()]为true，则dp[i] = true
 * 3. 初始化：dp[0] = true（空字符串可以被拆分）
 * 
 * 时间复杂度分析：
 * 设字符串s长度为n，字典大小为m，字典中单词平均长度为k
 * 1. 动态规划计算：O(n * m * k)
 * 总时间复杂度：O(n * m * k)
 * 
 * 空间复杂度分析：
 * 1. DP数组：O(n)
 * 2. 哈希表存储字典：O(m * k)
 * 总空间复杂度：O(n + m * k)
 * 
 * 相关题目扩展：
 * 1. LeetCode 139. 单词拆分（本题）
 * 2. LeetCode 140. 单词拆分 II（返回所有可能的拆分方案）
 * 3. LeetCode 322. 零钱兑换（完全背包求最小数量）
 * 4. LeetCode 518. 零钱兑换 II（完全背包求组合数）
 * 5. LeetCode 377. 组合总和 Ⅳ（完全背包求排列数）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空字符串、空字典等边界情况
 * 3. 可配置性：可以将字典作为配置参数传入
 * 4. 单元测试：为wordBreak方法编写测试用例
 * 5. 性能优化：使用哈希表优化单词查找
 * 
 * 语言特性差异：
 * 1. Java：使用HashSet提高查找效率
 * 2. 字符串操作：Java字符串不可变，需要注意性能影响
 * 3. 内存管理：自动垃圾回收，无需手动管理内存
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 剪枝优化：记录字典中最长单词长度，超过时提前终止
 * 2. 记忆化搜索：使用DFS+记忆化作为替代方案
 * 3. 字典树优化：对于大量单词，可以使用字典树优化匹配
 * 
 * 与标准背包的区别：
 * 1. 物品定义：物品是字符串而不是数值
 * 2. 匹配方式：需要完全匹配而不是数值比较
 * 3. 目标函数：求可行性而不是最值
 */

import java.util.*;

public class Code17_WordBreak {
    
    // 标准DP版本
    public static boolean wordBreak(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return true;
        if (wordDict == null || wordDict.isEmpty()) return false;
        
        // 将字典转换为HashSet，提高查找效率
        Set<String> wordSet = new HashSet<>(wordDict);
        
        int n = s.length();
        // dp[i]表示s的前i个字符是否可以被拆分
        boolean[] dp = new boolean[n + 1];
        dp[0] = true; // 空字符串可以被拆分
        
        // 记录字典中最长单词的长度，用于剪枝
        int maxLen = 0;
        for (String word : wordDict) {
            maxLen = Math.max(maxLen, word.length());
        }
        
        // 填充DP数组
        for (int i = 1; i <= n; i++) {
            // 从后往前遍历，减少不必要的比较
            for (int j = i - 1; j >= 0 && j >= i - maxLen; j--) {
                // 如果前j个字符可以被拆分，且s[j..i-1]在字典中
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break; // 找到一种可行方案即可退出
                }
            }
        }
        
        return dp[n];
    }
    
    // DFS + 记忆化搜索版本
    public static boolean wordBreakMemo(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return true;
        if (wordDict == null || wordDict.isEmpty()) return false;
        
        Set<String> wordSet = new HashSet<>(wordDict);
        Map<String, Boolean> memo = new HashMap<>();
        
        return dfs(s, wordSet, memo);
    }
    
    private static boolean dfs(String s, Set<String> wordSet, Map<String, Boolean> memo) {
        if (memo.containsKey(s)) {
            return memo.get(s);
        }
        
        if (s.length() == 0) {
            memo.put(s, true);
            return true;
        }
        
        for (int i = 1; i <= s.length(); i++) {
            String prefix = s.substring(0, i);
            if (wordSet.contains(prefix)) {
                String suffix = s.substring(i);
                if (dfs(suffix, wordSet, memo)) {
                    memo.put(s, true);
                    return true;
                }
            }
        }
        
        memo.put(s, false);
        return false;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "leetcode";
        List<String> wordDict1 = Arrays.asList("leet", "code");
        System.out.println("测试用例1:");
        System.out.println("标准版本: " + wordBreak(s1, wordDict1));
        System.out.println("记忆化版本: " + wordBreakMemo(s1, wordDict1));
        System.out.println("预期结果: true");
        System.out.println();
        
        // 测试用例2
        String s2 = "applepenapple";
        List<String> wordDict2 = Arrays.asList("apple", "pen");
        System.out.println("测试用例2:");
        System.out.println("标准版本: " + wordBreak(s2, wordDict2));
        System.out.println("记忆化版本: " + wordBreakMemo(s2, wordDict2));
        System.out.println("预期结果: true");
        System.out.println();
        
        // 测试用例3
        String s3 = "catsandog";
        List<String> wordDict3 = Arrays.asList("cats", "dog", "sand", "and", "cat");
        System.out.println("测试用例3:");
        System.out.println("标准版本: " + wordBreak(s3, wordDict3));
        System.out.println("记忆化版本: " + wordBreakMemo(s3, wordDict3));
        System.out.println("预期结果: false");
        System.out.println();
        
        // 测试用例4：边界情况
        String s4 = "";
        List<String> wordDict4 = Arrays.asList("a");
        System.out.println("测试用例4（空字符串）:");
        System.out.println("标准版本: " + wordBreak(s4, wordDict4));
        System.out.println("记忆化版本: " + wordBreakMemo(s4, wordDict4));
        System.out.println("预期结果: true");
        System.out.println();
        
        // 测试用例5：复杂情况
        String s5 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
        List<String> wordDict5 = Arrays.asList("a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa");
        System.out.println("测试用例5（复杂情况）:");
        System.out.println("标准版本: " + wordBreak(s5, wordDict5));
        System.out.println("记忆化版本: " + wordBreakMemo(s5, wordDict5));
        System.out.println("预期结果: false");
    }
}