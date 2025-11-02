package class073;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// LeetCode 139. 单词拆分
// 题目描述：给你一个字符串 s 和一个字符串列表 wordDict 作为字典。请你判断是否可以利用字典中出现的单词拼接出 s 。
// 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
// 链接：https://leetcode.cn/problems/word-break/
// 
// 解题思路：
// 这是一个完全背包问题的变种。我们可以将字符串s看作是背包，将字典中的单词看作是物品。
// 问题转化为：是否可以从字典中选择一些单词（可以重复选择），使得它们的拼接恰好等于字符串s。
// 
// 状态定义：dp[i] 表示字符串s的前i个字符是否可以被拆分成字典中的单词
// 状态转移方程：对于每个i，我们检查所有j < i，如果dp[j]为true且s.substring(j, i)在字典中，则dp[i]为true
// 初始状态：dp[0] = true，表示空字符串可以被拆分
// 
// 时间复杂度：O(n^3)，其中n是字符串s的长度
// 空间复杂度：O(n + m)，其中m是字典中所有单词的字符总数

public class Code38_WordBreak {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "leetcode";
        List<String> wordDict1 = List.of("leet", "code");
        System.out.println("测试用例1结果: " + wordBreak(s1, wordDict1)); // 预期输出: true
        
        // 测试用例2
        String s2 = "applepenapple";
        List<String> wordDict2 = List.of("apple", "pen");
        System.out.println("测试用例2结果: " + wordBreak(s2, wordDict2)); // 预期输出: true
        
        // 测试用例3
        String s3 = "catsandog";
        List<String> wordDict3 = List.of("cats", "dog", "sand", "and", "cat");
        System.out.println("测试用例3结果: " + wordBreak(s3, wordDict3)); // 预期输出: false
    }
    
    /**
     * 判断字符串是否可以被拆分成字典中的单词
     * @param s 字符串
     * @param wordDict 字典
     * @return 是否可以拆分
     */
    public static boolean wordBreak(String s, List<String> wordDict) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        
        // 将字典转换为集合，提高查找效率
        Set<String> wordSet = new HashSet<>(wordDict);
        int n = s.length();
        
        // 创建DP数组，dp[i]表示字符串s的前i个字符是否可以被拆分成字典中的单词
        boolean[] dp = new boolean[n + 1];
        
        // 初始状态：空字符串可以被拆分
        dp[0] = true;
        
        // 遍历字符串的每个位置
        for (int i = 1; i <= n; i++) {
            // 遍历所有可能的拆分点j
            for (int j = 0; j < i; j++) {
                // 如果dp[j]为true（前j个字符可以拆分），且s.substring(j, i)在字典中，那么dp[i]为true
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break; // 只要找到一个可行的拆分方式就可以提前结束内层循环
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * 优化的版本，限制j的范围为最大单词长度，避免不必要的检查
     * @param s 字符串
     * @param wordDict 字典
     * @return 是否可以拆分
     */
    public static boolean wordBreakOptimized(String s, List<String> wordDict) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        
        // 将字典转换为集合，提高查找效率
        Set<String> wordSet = new HashSet<>(wordDict);
        int n = s.length();
        
        // 找出字典中最长单词的长度
        int maxLength = 0;
        for (String word : wordDict) {
            maxLength = Math.max(maxLength, word.length());
        }
        
        // 创建DP数组
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        
        // 遍历字符串的每个位置
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
     * 使用递归+记忆化搜索实现
     * 这个方法对于较大的输入可能会超时，但展示了递归的思路
     * @param s 字符串
     * @param wordDict 字典
     * @return 是否可以拆分
     */
    public static boolean wordBreakRecursive(String s, List<String> wordDict) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        
        // 将字典转换为集合
        Set<String> wordSet = new HashSet<>(wordDict);
        
        // 创建记忆化缓存，memo[i]表示从位置i开始的子串是否可以被拆分
        Boolean[] memo = new Boolean[s.length()];
        
        return dfs(s, 0, wordSet, memo);
    }
    
    /**
     * 递归辅助函数
     * @param s 字符串
     * @param start 起始位置
     * @param wordSet 字典集合
     * @param memo 记忆化缓存
     * @return 是否可以拆分
     */
    private static boolean dfs(String s, int start, Set<String> wordSet, Boolean[] memo) {
        // 基础情况：已经处理到字符串末尾
        if (start == s.length()) {
            return true;
        }
        
        // 检查缓存
        if (memo[start] != null) {
            return memo[start];
        }
        
        // 尝试所有可能的结束位置
        for (int end = start + 1; end <= s.length(); end++) {
            // 如果s.substring(start, end)在字典中，且剩余部分可以拆分，则返回true
            if (wordSet.contains(s.substring(start, end)) && dfs(s, end, wordSet, memo)) {
                memo[start] = true;
                return true;
            }
        }
        
        // 如果所有可能性都尝试过仍无法拆分，返回false
        memo[start] = false;
        return false;
    }
    
    /**
     * 使用BFS实现
     * @param s 字符串
     * @param wordDict 字典
     * @return 是否可以拆分
     */
    public static boolean wordBreakBFS(String s, List<String> wordDict) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        
        Set<String> wordSet = new HashSet<>(wordDict);
        boolean[] visited = new boolean[s.length()]; // 记录哪些位置已经被访问过，避免重复处理
        
        // 使用队列进行BFS，队列中存储的是当前处理到的位置
        java.util.Queue<Integer> queue = new java.util.LinkedList<>();
        queue.offer(0);
        visited[0] = true;
        
        while (!queue.isEmpty()) {
            int start = queue.poll();
            
            // 尝试所有可能的结束位置
            for (int end = start + 1; end <= s.length(); end++) {
                // 如果当前子串在字典中，且结束位置尚未访问过，则继续BFS
                if (wordSet.contains(s.substring(start, end)) && !visited[end]) {
                    if (end == s.length()) {
                        return true; // 已经到达字符串末尾，找到了解决方案
                    }
                    queue.offer(end);
                    visited[end] = true;
                }
            }
        }
        
        return false; // 无法拆分
    }
}