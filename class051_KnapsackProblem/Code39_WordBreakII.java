package class073;

import java.util.*;

// LeetCode 140. 单词拆分 II
// 题目描述：给定一个字符串 s 和一个字符串字典 wordDict ，在字符串 s 中增加空格来构建一个句子，使得句子中所有的单词都在词典中。返回所有这样的可能句子。
// 链接：https://leetcode.cn/problems/word-break-ii/
// 
// 解题思路：
// 这是一个完全背包问题的变种，同时也是一个组合问题。我们需要找到所有可能的单词组合，使得它们的拼接等于字符串s。
// 
// 我们可以使用递归+记忆化搜索来解决这个问题：
// 1. 使用记忆化缓存，避免重复计算
// 2. 对于每个位置i，我们尝试所有可能的单词，如果s.substring(i, j)在字典中，我们递归处理剩余部分
// 3. 将当前单词与剩余部分的结果组合
// 
// 时间复杂度：O(n^2 * 2^n)，其中n是字符串s的长度。在最坏情况下，每个字符之间都可以拆分，会产生2^(n-1)种拆分方式。
// 空间复杂度：O(n^2)，用于存储记忆化缓存。

public class Code39_WordBreakII {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "catsanddog";
        List<String> wordDict1 = Arrays.asList("cat", "cats", "and", "sand", "dog");
        System.out.println("测试用例1结果: " + wordBreak(s1, wordDict1));
        // 预期输出: ["cats and dog", "cat sand dog"]
        
        // 测试用例2
        String s2 = "pineapplepenapple";
        List<String> wordDict2 = Arrays.asList("apple", "pen", "applepen", "pine", "pineapple");
        System.out.println("测试用例2结果: " + wordBreak(s2, wordDict2));
        // 预期输出: ["pine apple pen apple", "pineapple pen apple", "pine applepen apple"]
        
        // 测试用例3
        String s3 = "catsandog";
        List<String> wordDict3 = Arrays.asList("cats", "dog", "sand", "and", "cat");
        System.out.println("测试用例3结果: " + wordBreak(s3, wordDict3));
        // 预期输出: []
    }
    
    /**
     * 返回所有可能的单词拆分方案
     * @param s 字符串
     * @param wordDict 字典
     * @return 所有可能的拆分方案列表
     */
    public static List<String> wordBreak(String s, List<String> wordDict) {
        if (s == null || s.isEmpty() || wordDict == null || wordDict.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 将字典转换为集合，提高查找效率
        Set<String> wordSet = new HashSet<>(wordDict);
        
        // 首先使用动态规划检查是否可以拆分，如果不能拆分直接返回空列表
        // 这一步可以避免不必要的递归计算
        if (!canBreak(s, wordSet)) {
            return new ArrayList<>();
        }
        
        // 创建记忆化缓存，memo[i]表示从位置i开始的子串的所有可能拆分方案
        Map<Integer, List<String>> memo = new HashMap<>();
        
        return dfs(s, 0, wordSet, memo);
    }
    
    /**
     * 使用动态规划检查字符串是否可以拆分
     * @param s 字符串
     * @param wordSet 字典集合
     * @return 是否可以拆分
     */
    private static boolean canBreak(String s, Set<String> wordSet) {
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true; // 空字符串可以被拆分
        
        // 找出字典中最长单词的长度
        int maxLength = 0;
        for (String word : wordSet) {
            maxLength = Math.max(maxLength, word.length());
        }
        
        for (int i = 1; i <= n; i++) {
            // 只检查j >= i - maxLength的情况，避免不必要的检查
            int start = Math.max(0, i - maxLength);
            for (int j = start; j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * 递归辅助函数，使用记忆化搜索找出所有可能的拆分方案
     * @param s 字符串
     * @param start 起始位置
     * @param wordSet 字典集合
     * @param memo 记忆化缓存
     * @return 从start位置开始的子串的所有可能拆分方案
     */
    private static List<String> dfs(String s, int start, Set<String> wordSet, Map<Integer, List<String>> memo) {
        // 如果已经计算过，直接返回缓存的结果
        if (memo.containsKey(start)) {
            return memo.get(start);
        }
        
        List<String> result = new ArrayList<>();
        int n = s.length();
        
        // 基础情况：已经处理到字符串末尾
        if (start == n) {
            result.add(""); // 添加空字符串作为递归终止条件
            return result;
        }
        
        // 尝试所有可能的结束位置
        for (int end = start + 1; end <= n; end++) {
            // 获取当前子串
            String word = s.substring(start, end);
            
            // 如果当前子串在字典中，递归处理剩余部分
            if (wordSet.contains(word)) {
                // 递归获取剩余部分的所有拆分方案
                List<String> subList = dfs(s, end, wordSet, memo);
                
                // 将当前单词与剩余部分的拆分方案组合
                for (String sub : subList) {
                    // 如果sub为空字符串，说明已经到达字符串末尾，不需要添加空格
                    if (sub.isEmpty()) {
                        result.add(word);
                    } else {
                        result.add(word + " " + sub);
                    }
                }
            }
        }
        
        // 缓存结果
        memo.put(start, result);
        return result;
    }
    
    /**
     * 另一种实现方式，使用动态规划来存储所有可能的拆分方案
     * @param s 字符串
     * @param wordDict 字典
     * @return 所有可能的拆分方案列表
     */
    public static List<String> wordBreakDP(String s, List<String> wordDict) {
        if (s == null || s.isEmpty() || wordDict == null || wordDict.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 将字典转换为集合，提高查找效率
        Set<String> wordSet = new HashSet<>(wordDict);
        int n = s.length();
        
        // dp[i]存储前i个字符的所有可能拆分方案
        List<List<String>> dp = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            dp.add(new ArrayList<>());
        }
        
        // 初始状态：空字符串有一个拆分方案（空字符串）
        dp.get(0).add("");
        
        // 填充dp数组
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                String word = s.substring(j, i);
                if (wordSet.contains(word) && !dp.get(j).isEmpty()) {
                    // 将当前单词与前j个字符的所有拆分方案组合
                    for (String prev : dp.get(j)) {
                        if (prev.isEmpty()) {
                            dp.get(i).add(word);
                        } else {
                            dp.get(i).add(prev + " " + word);
                        }
                    }
                }
            }
        }
        
        return dp.get(n);
    }
    
    /**
     * 优化的DFS实现，使用最大单词长度来限制搜索范围
     * @param s 字符串
     * @param wordDict 字典
     * @return 所有可能的拆分方案列表
     */
    public static List<String> wordBreakOptimized(String s, List<String> wordDict) {
        if (s == null || s.isEmpty() || wordDict == null || wordDict.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 将字典转换为集合，提高查找效率
        Set<String> wordSet = new HashSet<>(wordDict);
        
        // 找出字典中最长单词的长度
        int maxLength = 0;
        for (String word : wordDict) {
            maxLength = Math.max(maxLength, word.length());
        }
        
        // 首先检查是否可以拆分
        if (!canBreak(s, wordSet)) {
            return new ArrayList<>();
        }
        
        // 创建记忆化缓存
        Map<Integer, List<String>> memo = new HashMap<>();
        
        return dfsOptimized(s, 0, wordSet, memo, maxLength);
    }
    
    /**
     * 优化的递归辅助函数，使用最大单词长度来限制搜索范围
     * @param s 字符串
     * @param start 起始位置
     * @param wordSet 字典集合
     * @param memo 记忆化缓存
     * @param maxLength 字典中最长单词的长度
     * @return 从start位置开始的子串的所有可能拆分方案
     */
    private static List<String> dfsOptimized(String s, int start, Set<String> wordSet, 
                                          Map<Integer, List<String>> memo, int maxLength) {
        // 如果已经计算过，直接返回缓存的结果
        if (memo.containsKey(start)) {
            return memo.get(start);
        }
        
        List<String> result = new ArrayList<>();
        int n = s.length();
        
        // 基础情况：已经处理到字符串末尾
        if (start == n) {
            result.add("");
            return result;
        }
        
        // 限制end的范围为start + maxLength，避免不必要的检查
        int endMax = Math.min(start + maxLength, n);
        for (int end = start + 1; end <= endMax; end++) {
            String word = s.substring(start, end);
            
            if (wordSet.contains(word)) {
                List<String> subList = dfsOptimized(s, end, wordSet, memo, maxLength);
                
                for (String sub : subList) {
                    if (sub.isEmpty()) {
                        result.add(word);
                    } else {
                        result.add(word + " " + sub);
                    }
                }
            }
        }
        
        // 缓存结果
        memo.put(start, result);
        return result;
    }
}