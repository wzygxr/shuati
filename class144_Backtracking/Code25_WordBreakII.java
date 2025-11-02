package class038;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * LeetCode 140. 单词拆分 II
 * 
 * 题目描述：
 * 给定一个字符串 s 和一个字符串字典 wordDict，在字符串 s 中增加空格来构建一个句子，
 * 使得句子中所有的单词都在词典中。返回所有这些可能的句子。
 * 
 * 示例：
 * 输入：s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"]
 * 输出：["cats and dog","cat sand dog"]
 * 
 * 输入：s = "pineapplepenapple", wordDict = ["apple","pen","applepen","pine","pineapple"]
 * 输出：["pine apple pen apple","pineapple pen apple","pine applepen apple"]
 * 
 * 输入：s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
 * 输出：[]
 * 
 * 提示：
 * 1 <= s.length <= 20
 * 1 <= wordDict.length <= 1000
 * 1 <= wordDict[i].length <= 10
 * s 和 wordDict[i] 仅有小写英文字母组成
 * wordDict 中的所有字符串互不相同
 * 
 * 链接：https://leetcode.cn/problems/word-break-ii/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的分割方案
 * 2. 结合记忆化搜索优化重复计算
 * 3. 对于每个位置，尝试所有可能的单词分割
 * 4. 如果当前分割的单词在字典中，递归处理剩余部分
 * 
 * 时间复杂度：O(2^n * n)，最坏情况下需要尝试所有可能的分割
 * 空间复杂度：O(n^2)，递归栈深度和存储结果的空间
 */
public class Code25_WordBreakII {

    /**
     * 单词拆分II
     * 
     * @param s 输入字符串
     * @param wordDict 单词字典
     * @return 所有可能的句子
     */
    public static List<String> wordBreak(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        return backtrack(s, 0, dict, new HashMap<>());
    }

    /**
     * 回溯函数生成句子（带记忆化）
     * 
     * @param s 输入字符串
     * @param start 当前起始位置
     * @param dict 单词字典
     * @param memo 记忆化存储
     * @return 从start开始的所有可能句子
     */
    private static List<String> backtrack(String s, int start, Set<String> dict, Map<Integer, List<String>> memo) {
        // 如果已经计算过，直接返回结果
        if (memo.containsKey(start)) {
            return memo.get(start);
        }
        
        List<String> result = new ArrayList<>();
        
        // 终止条件：到达字符串末尾
        if (start == s.length()) {
            result.add("");  // 添加空字符串作为基础
            return result;
        }
        
        // 尝试所有可能的分割点
        for (int end = start + 1; end <= s.length(); end++) {
            String word = s.substring(start, end);
            
            // 如果当前单词在字典中
            if (dict.contains(word)) {
                // 递归处理剩余部分
                List<String> subSentences = backtrack(s, end, dict, memo);
                
                // 将当前单词与子句组合
                for (String subSentence : subSentences) {
                    if (subSentence.isEmpty()) {
                        result.add(word);
                    } else {
                        result.add(word + " " + subSentence);
                    }
                }
            }
        }
        
        // 存储结果到记忆化表
        memo.put(start, result);
        return result;
    }

    /**
     * 解法二：使用动态规划预处理 + 回溯
     * 先使用动态规划判断是否可分割，再进行回溯
     * 
     * @param s 输入字符串
     * @param wordDict 单词字典
     * @return 所有可能的句子
     */
    public static List<String> wordBreak2(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        
        // 使用动态规划预处理，判断是否可分割
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        
        // 如果不可分割，直接返回空列表
        if (!dp[n]) {
            return new ArrayList<>();
        }
        
        // 使用回溯生成所有句子
        List<String> result = new ArrayList<>();
        backtrack2(s, 0, dict, new StringBuilder(), result, dp);
        return result;
    }

    private static void backtrack2(String s, int start, Set<String> dict, StringBuilder path, List<String> result, boolean[] dp) {
        // 终止条件：到达字符串末尾
        if (start == s.length()) {
            result.add(path.toString().trim());
            return;
        }
        
        // 尝试所有可能的分割点
        for (int end = start + 1; end <= s.length(); end++) {
            String word = s.substring(start, end);
            
            // 如果当前单词在字典中且剩余部分可分割
            if (dict.contains(word) && dp[end]) {
                int originalLength = path.length();
                
                // 添加当前单词到路径
                if (path.length() > 0) {
                    path.append(" ");
                }
                path.append(word);
                
                // 递归处理剩余部分
                backtrack2(s, end, dict, path, result, dp);
                
                // 回溯
                path.setLength(originalLength);
            }
        }
    }

    /**
     * 解法三：纯回溯算法（无优化）
     * 适用于小规模数据
     * 
     * @param s 输入字符串
     * @param wordDict 单词字典
     * @return 所有可能的句子
     */
    public static List<String> wordBreak3(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        List<String> result = new ArrayList<>();
        backtrack3(s, 0, dict, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack3(String s, int start, Set<String> dict, List<String> path, List<String> result) {
        // 终止条件：到达字符串末尾
        if (start == s.length()) {
            result.add(String.join(" ", path));
            return;
        }
        
        // 尝试所有可能的分割点
        for (int end = start + 1; end <= s.length(); end++) {
            String word = s.substring(start, end);
            
            // 如果当前单词在字典中
            if (dict.contains(word)) {
                path.add(word);
                backtrack3(s, end, dict, path, result);
                path.remove(path.size() - 1);  // 回溯
            }
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "catsanddog";
        List<String> wordDict1 = List.of("cat", "cats", "and", "sand", "dog");
        List<String> result1 = wordBreak(s1, wordDict1);
        System.out.println("输入: s = \"" + s1 + "\", wordDict = " + wordDict1);
        System.out.println("输出: " + result1);
        
        // 测试用例2
        String s2 = "pineapplepenapple";
        List<String> wordDict2 = List.of("apple", "pen", "applepen", "pine", "pineapple");
        List<String> result2 = wordBreak(s2, wordDict2);
        System.out.println("\n输入: s = \"" + s2 + "\", wordDict = " + wordDict2);
        System.out.println("输出: " + result2);
        
        // 测试用例3
        String s3 = "catsandog";
        List<String> wordDict3 = List.of("cats", "dog", "sand", "and", "cat");
        List<String> result3 = wordBreak(s3, wordDict3);
        System.out.println("\n输入: s = \"" + s3 + "\", wordDict = " + wordDict3);
        System.out.println("输出: " + result3);
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        List<String> result4 = wordBreak2(s1, wordDict1);
        System.out.println("输入: s = \"" + s1 + "\", wordDict = " + wordDict1);
        System.out.println("输出: " + result4);
    }
}