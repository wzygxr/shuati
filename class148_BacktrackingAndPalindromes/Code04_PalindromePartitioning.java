package class043;

import java.util.*;

/**
 * 分割回文串问题
 * 
 * 问题描述：
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。
 * 返回 s 所有可能的分割方案。
 * 
 * 算法思路：
 * 1. 这是一个典型的回溯算法问题，需要找出所有满足条件的分割方案
 * 2. 使用回溯算法遍历所有可能的分割点，找到所有满足条件的分割方案
 * 3. 在每一步尝试不同的分割点，通过递归和回溯来探索所有可能性
 * 4. 需要预先处理判断子串是否为回文串，可以使用动态规划优化
 * 
 * 时间复杂度分析：
 * - 最坏情况下需要尝试所有可能的分割方案
 * - 字符串长度为n，分割方案数为O(2^(n-1))
 * - 对于每个分割方案，需要验证每个子串是否为回文串
 * - 总时间复杂度为O(n * 2^n)
 * 
 * 空间复杂度分析：
 * - 主要空间消耗是递归栈深度和存储结果的数组
 * - 递归深度最大为n，空间复杂度为O(n)
 * - 动态规划预处理需要O(n^2)空间
 * - 存储结果需要O(2^n * n)空间
 * 
 * 工程化考量：
 * 1. 异常处理：对输入数据进行校验
 * 2. 性能优化：使用动态规划预处理回文串判断
 * 3. 可配置性：可以调整算法策略（是否使用预处理）
 * 4. 鲁棒性：处理边界情况，如空字符串或单字符字符串
 * 
 * 相关题目：
 * 1. LeetCode 131. 分割回文串 - https://leetcode.cn/problems/palindrome-partitioning/
 * 2. LeetCode 132. 分割回文串 II - https://leetcode.cn/problems/palindrome-partitioning-ii/
 * 3. LeetCode 93. 复原IP地址 - https://leetcode.cn/problems/restore-ip-addresses/
 * 4. LeetCode 140. 单词拆分 II - https://leetcode.cn/problems/word-break-ii/
 * 5. LeetCode 301. 删除无效的括号 - https://leetcode.cn/problems/remove-invalid-parentheses/
 * 6. LeetCode 491. 递增子序列 - https://leetcode.cn/problems/increasing-subsequences/
 * 7. LeetCode 5. 最长回文子串 - https://leetcode.cn/problems/longest-palindromic-substring/
 * 8. LeetCode 647. 回文子串 - https://leetcode.cn/problems/palindromic-substrings/
 * 9. LeetCode 516. 最长回文子序列 - https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 10. LeetCode 336. 回文对 - https://leetcode.cn/problems/palindrome-pairs/
 * 11. LeetCode 479. 最大回文数乘积 - https://leetcode.cn/problems/largest-palindrome-product/
 * 12. LeetCode 680. 验证回文字符串 Ⅱ - https://leetcode.cn/problems/valid-palindrome-ii/
 * 13. 牛客网 - 分割回文串 - https://www.nowcoder.com/practice/1025ffc2939547e39e8a38a955de1dd3
 * 14. 牛客网 - 最长回文子串 - https://www.nowcoder.com/practice/b4525d1d84934cf280439aeecc36f4af
 * 15. 牛客网 - 复原IP地址 - https://www.nowcoder.com/practice/ce73540d47374dbe85b3125f57727e1e
 * 16. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 17. Codeforces 1436E - Complicated Construction - https://codeforces.com/problemset/problem/1436/E
 * 18. Codeforces 1332B - Composite Coloring - https://codeforces.com/problemset/problem/1332/B
 * 19. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 * 20. AtCoder ABC175D - Moving Piece - https://atcoder.jp/contests/abc175/tasks/abc175_d
 * 21. AtCoder ABC159E - Dividing Chocolate - https://atcoder.jp/contests/abc159/tasks/abc159_e
 * 22. 洛谷 P1120 - 小木棍 - https://www.luogu.com.cn/problem/P1120
 * 23. 洛谷 P1540 - [NOIP2010 提高组] 机器翻译 - https://www.luogu.com.cn/problem/P1540
 * 24. 洛谷 P1157 - 组合的输出 - https://www.luogu.com.cn/problem/P1157
 * 25. 洛谷 P1048 - [NOIP2005 普及组] 采药 - https://www.luogu.com.cn/problem/P1048
 * 26. HackerRank - Palindromic Substrings - https://www.hackerrank.com/challenges/palindromic-substrings/problem
 * 27. HackerRank - Split the String - https://www.hackerrank.com/challenges/split-the-string/problem
 * 28. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 29. HackerRank - The Longest Palindromic Subsequence - https://www.hackerrank.com/challenges/longest-palindromic-subsequence/problem
 * 30. UVa 10945 - Mother Bear - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1886
 * 31. UVa 10189 - Minesweeper - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1886
 * 32. POJ 3083 - Children of the Candy Corn - http://poj.org/problem?id=3083
 * 33. POJ 1163 - The Triangle - http://poj.org/problem?id=1163
 * 34. HDU 1527 - 取石子游戏 - https://acm.hdu.edu.cn/showproblem.php?pid=1527
 * 35. HDU 1203 - I NEED A OFFER! - https://acm.hdu.edu.cn/showproblem.php?pid=1203
 * 36. LintCode 1000. 至少有K个重复字符的最长子串 - https://www.lintcode.com/problem/1000/
 * 37. LintCode 1178. 验证回文字符串 II - https://www.lintcode.com/problem/1178/
 * 38. LintCode 125. 背包问题 II - https://www.lintcode.com/problem/125/
 * 39. LintCode 200. 最长回文子串 - https://www.lintcode.com/problem/200/
 * 40. LintCode 130. 堆化 - https://www.lintcode.com/problem/130/
 * 41. LintCode 135. 数字组合 - https://www.lintcode.com/problem/135/
 * 42. LeetCode 31. 下一个排列 - https://leetcode.cn/problems/next-permutation/
 * 43. LeetCode 77. 组合 - https://leetcode.cn/problems/combinations/
 * 44. LeetCode 79. 单词搜索 - https://leetcode.cn/problems/word-search/
 * 45. LeetCode 17. 电话号码的字母组合 - https://leetcode.cn/problems/letter-combinations-of-a-phone-number/
 * 46. LeetCode 22. 括号生成 - https://leetcode.cn/problems/generate-parentheses/
 * 47. LeetCode 10. 正则表达式匹配 - https://leetcode.cn/problems/regular-expression-matching/
 * 48. LeetCode 37. 解数独 - https://leetcode.cn/problems/sudoku-solver/
 * 49. LeetCode 51. N 皇后 - https://leetcode.cn/problems/n-queens/
 * 50. LeetCode 52. N 皇后 II - https://leetcode.cn/problems/n-queens-ii/
 */
public class Code04_PalindromePartitioning {
    
    /**
     * 分割回文串主函数
     * 
     * @param s 输入字符串
     * @return 所有可能的分割方案
     * 
     * 算法思路：
     * 1. 使用回溯算法枚举所有可能的分割方案
     * 2. 预先使用动态规划处理所有子串是否为回文串
     * 3. 在回溯过程中，只考虑是回文串的子串进行分割
     * 
     * 优化点：
     * 1. 使用动态规划预处理回文串判断，避免重复计算
     * 2. 在回溯过程中剪枝，只考虑有效的分割点
     */
    public static List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return result;
        }
        
        // 预处理：使用动态规划判断所有子串是否为回文串
        boolean[][] isPalindrome = precomputePalindromes(s);
        
        // 回溯搜索所有分割方案
        backtrack(s, 0, new ArrayList<>(), result, isPalindrome);
        
        return result;
    }
    
    /**
     * 预处理所有子串是否为回文串
     * 
     * @param s 输入字符串
     * @return 二维布尔数组，isPalindrome[i][j]表示s[i..j]是否为回文串
     * 
     * 算法思路：
     * 1. 使用动态规划，dp[i][j]表示s[i..j]是否为回文串
     * 2. 状态转移方程：
     *    - 如果i == j，则dp[i][j] = true（单字符必为回文串）
     *    - 如果j - i == 1且s[i] == s[j]，则dp[i][j] = true（两字符相等为回文串）
     *    - 如果s[i] == s[j]且dp[i+1][j-1] = true，则dp[i][j] = true
     * 
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n^2)
     */
    private static boolean[][] precomputePalindromes(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        
        // 单字符必为回文串
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }
        
        // 两字符情况
        for (int i = 0; i < n - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                dp[i][i + 1] = true;
            }
        }
        
        // 长度大于2的子串
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                }
            }
        }
        
        return dp;
    }
    
    /**
     * 回溯函数，寻找所有可能的分割方案
     * 
     * @param s 输入字符串
     * @param start 当前处理的起始位置
     * @param path 当前分割路径
     * @param result 存储所有分割方案的结果集
     * @param isPalindrome 预处理的回文串判断数组
     * 
     * 递归思路：
     * 1. 基础情况：如果start等于字符串长度，说明已经处理完所有字符，将当前路径加入结果集
     * 2. 递归情况：从start位置开始，尝试所有可能的结束位置end
     * 3. 如果s[start..end]是回文串，则将其加入路径，递归处理剩余部分
     * 4. 递归返回后，回溯移除刚加入的子串
     */
    private static void backtrack(String s, int start, List<String> path, List<List<String>> result, boolean[][] isPalindrome) {
        // 基础情况：已经处理完所有字符
        if (start == s.length()) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        // 递归情况：尝试所有可能的分割点
        for (int end = start; end < s.length(); end++) {
            // 如果当前子串是回文串，则进行分割
            if (isPalindrome[start][end]) {
                // 做选择：将当前子串加入路径
                path.add(s.substring(start, end + 1));
                
                // 递归：处理剩余部分
                backtrack(s, end + 1, path, result, isPalindrome);
                
                // 撤销选择：回溯移除刚加入的子串
                path.remove(path.size() - 1);
            }
        }
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "aab";
        List<List<String>> result1 = partition(s1);
        System.out.println("输入: \"" + s1 + "\"");
        System.out.println("输出: " + result1);
        // 预期输出: [["a","a","b"],["aa","b"]]
        
        // 测试用例2
        String s2 = "a";
        List<List<String>> result2 = partition(s2);
        System.out.println("\n输入: \"" + s2 + "\"");
        System.out.println("输出: " + result2);
        // 预期输出: [["a"]]
        
        // 测试用例3
        String s3 = "abc";
        List<List<String>> result3 = partition(s3);
        System.out.println("\n输入: \"" + s3 + "\"");
        System.out.println("输出: " + result3);
        // 预期输出: [["a","b","c"]]
    }
    
    /**
     * 补充训练题目
     * 
     * 1. LeetCode 132. 分割回文串 II
     *    题目描述：给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。返回符合要求的最少分割次数。
     *    解题思路：使用动态规划，dp[i]表示前i个字符的最少分割次数
     *    Java实现示例：
     *    public int minCut(String s) {
     *        int n = s.length();
     *        // 预处理回文串
     *        boolean[][] isPalindrome = precomputePalindromes(s);
     *        
     *        // dp[i]表示前i个字符的最少分割次数
     *        int[] dp = new int[n + 1];
     *        Arrays.fill(dp, Integer.MAX_VALUE);
     *        dp[0] = -1; // 初始条件
     *        
     *        for (int i = 1; i <= n; i++) {
     *            for (int j = 0; j < i; j++) {
     *                if (isPalindrome[j][i - 1]) {
     *                    dp[i] = Math.min(dp[i], dp[j] + 1);
     *                }
     *            }
     *        }
     *        
     *        return dp[n];
     *    }
     * 
     * 2. LeetCode 93. 复原IP地址
     *    题目描述：给定一个只包含数字的字符串，用以表示一个 IP 地址，返回所有可能从 s 获得的有效 IP 地址。
     *    解题思路：使用回溯算法，枚举每个段的长度（1-3），并验证是否有效
     *    Java实现示例：
     *    public List<String> restoreIpAddresses(String s) {
     *        List<String> result = new ArrayList<>();
     *        if (s == null || s.length() < 4 || s.length() > 12) return result;
     *        backtrackIp(s, 0, 0, "", result);
     *        return result;
     *    }
     *    private void backtrackIp(String s, int start, int segment, String current, List<String> result) {
     *        if (start == s.length() && segment == 4) {
     *            result.add(current.substring(0, current.length() - 1));
     *            return;
     *        }
     *        if (segment >= 4) return;
     *        
     *        for (int len = 1; len <= 3 && start + len <= s.length(); len++) {
     *            String part = s.substring(start, start + len);
     *            // 检查是否是有效的IP段
     *            if (isValidIpSegment(part)) {
     *                backtrackIp(s, start + len, segment + 1, current + part + ".", result);
     *            }
     *        }
     *    }
     *    private boolean isValidIpSegment(String segment) {
     *        if (segment.length() > 1 && segment.charAt(0) == '0') return false;
     *        int num = Integer.parseInt(segment);
     *        return num >= 0 && num <= 255;
     *    }
     * 
     * 3. LeetCode 140. 单词拆分 II
     *    题目描述：给定一个字符串 s 和一个字符串字典 wordDict，在字符串 s 中增加空格来构建一个句子，使得句子中所有的单词都在词典中。返回所有可能的句子。
     *    解题思路：使用回溯算法，结合动态规划预处理
     *    Java实现示例：
     *    public List<String> wordBreak(String s, List<String> wordDict) {
     *        Set<String> wordSet = new HashSet<>(wordDict);
     *        int n = s.length();
     *        
     *        // 预处理：dp[i]表示前i个字符是否可以拆分
     *        boolean[] dp = new boolean[n + 1];
     *        dp[0] = true;
     *        for (int i = 1; i <= n; i++) {
     *            for (int j = 0; j < i; j++) {
     *                if (dp[j] && wordSet.contains(s.substring(j, i))) {
     *                    dp[i] = true;
     *                    break;
     *                }
     *            }
     *        }
     *        
     *        List<String> result = new ArrayList<>();
     *        if (dp[n]) {
     *            backtrackWordBreak(s, 0, new ArrayList<>(), result, wordSet);
     *        }
     *        return result;
     *    }
     *    private void backtrackWordBreak(String s, int start, List<String> path, List<String> result, Set<String> wordSet) {
     *        if (start == s.length()) {
     *            result.add(String.join(" ", path));
     *            return;
     *        }
     *        
     *        for (int end = start + 1; end <= s.length(); end++) {
     *            String word = s.substring(start, end);
     *            if (wordSet.contains(word)) {
     *                path.add(word);
     *                backtrackWordBreak(s, end, path, result, wordSet);
     *                path.remove(path.size() - 1);
     *            }
     *        }
     *    }
     * 
     * 4. Codeforces 1327D - Infinite Path
     *    题目描述：给定一个置换p和一个颜色数组c，找到最小的k，使得存在一个起始点s，使得沿路径s → p[s] → p[p[s]] → ... 每k步的颜色都相同。
     *    解题思路：分析置换的循环结构，对每个循环尝试不同的k值
     * 
     * 5. HackerRank - Split the String
     *    题目描述：给定一个只包含a和b的字符串，将其分割成尽可能多的部分，使得每个部分中的a和b的数量相等。
     *    解题思路：使用贪心算法，记录当前a和b的数量，相等时进行分割
     *    Java实现示例：
     *    public static int splitString(String s) {
     *        int countA = 0, countB = 0;
     *        int splits = 0;
     *        
     *        for (char c : s.toCharArray()) {
     *            if (c == 'a') countA++;
     *            else countB++;
     *            
     *            if (countA == countB) {
     *                splits++;
     *                countA = 0;
     *                countB = 0;
     *            }
     *        }
     *        
     *        return splits;
     *    }
     * 
     * 6. LeetCode 647. 回文子串
     *    题目描述：给你一个字符串 s ，请你统计并返回这个字符串中 回文子串 的数目。
     *    解题思路：使用中心扩展法，枚举所有可能的回文中心
     *    Java实现示例：
     *    public int countSubstrings(String s) {
     *        int count = 0;
     *        for (int i = 0; i < s.length(); i++) {
     *            // 奇数长度回文子串
     *            count += expandAroundCenter(s, i, i);
     *            // 偶数长度回文子串
     *            count += expandAroundCenter(s, i, i + 1);
     *        }
     *        return count;
     *    }
     *    private int expandAroundCenter(String s, int left, int right) {
     *        int count = 0;
     *        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
     *            count++;
     *            left--;
     *            right++;
     *        }
     *        return count;
     *    }
     * 
     * 7. LeetCode 516. 最长回文子序列
     *    题目描述：给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
     *    解题思路：使用动态规划，dp[i][j]表示s[i..j]中的最长回文子序列长度
     *    Java实现示例：
     *    public int longestPalindromeSubseq(String s) {
     *        int n = s.length();
     *        int[][] dp = new int[n][n];
     *        
     *        // 单个字符的回文子序列长度为1
     *        for (int i = 0; i < n; i++) {
     *            dp[i][i] = 1;
     *        }
     *        
     *        // 从短到长处理子串
     *        for (int len = 2; len <= n; len++) {
     *            for (int i = 0; i <= n - len; i++) {
     *                int j = i + len - 1;
     *                if (s.charAt(i) == s.charAt(j)) {
     *                    dp[i][j] = dp[i + 1][j - 1] + 2;
     *                } else {
     *                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
     *                }
     *            }
     *        }
     *        
     *        return dp[0][n - 1];
     *    }
     * 
     * 8. LeetCode 336. 回文对
     *    题目描述：给定一个字符串数组 words，找出所有不同的索引对 (i, j)，使得 words[i] + words[j] 是一个回文串。
     *    解题思路：使用哈希表存储反转的字符串，查找可能的回文对
     *    Java实现示例：
     *    public List<List<Integer>> palindromePairs(String[] words) {
     *        List<List<Integer>> result = new ArrayList<>();
     *        Map<String, Integer> map = new HashMap<>();
     *        
     *        // 存储每个单词的反转及其索引
     *        for (int i = 0; i < words.length; i++) {
     *            map.put(new StringBuilder(words[i]).reverse().toString(), i);
     *        }
     *        
     *        for (int i = 0; i < words.length; i++) {
     *            String word = words[i];
     *            
     *            // 情况1：空字符串与回文字符串
     *            if (map.containsKey("") && map.get("") != i && isPalindrome(word)) {
     *                result.add(Arrays.asList(i, map.get("")));
     *            }
     *            
     *            for (int j = 0; j < word.length(); j++) {
     *                String left = word.substring(0, j);
     *                String right = word.substring(j);
     *                
     *                // 情况2：left是回文，寻找right的反转
     *                if (map.containsKey(left) && map.get(left) != i && isPalindrome(right)) {
     *                    result.add(Arrays.asList(i, map.get(left)));
     *                }
     *                
     *                // 情况3：right是回文，寻找left的反转
     *                if (map.containsKey(right) && map.get(right) != i && isPalindrome(left)) {
     *                    result.add(Arrays.asList(map.get(right), i));
     *                }
     *            }
     *        }
     *        
     *        return result;
     *    }
     *    private boolean isPalindrome(String s) {
     *        int left = 0, right = s.length() - 1;
     *        while (left < right) {
     *            if (s.charAt(left++) != s.charAt(right--)) {
     *                return false;
     *            }
     *        }
     *        return true;
     *    }
     */
    
    /**
     * 分割回文串问题的算法技巧总结
     * 
     * 核心概念：
     * - 回文串：正序和倒序读都是一样的字符串
     * - 分割问题：将字符串分割成若干满足条件的子串
     * - 回溯算法：通过递归和回溯探索所有可能的分割方案
     * - 动态规划：用于预处理和优化回文判断
     * 
     * 算法设计：
     * 1. 回溯算法框架：
     *    - 状态定义：当前处理的起始位置、当前分割路径
     *    - 选择：尝试所有可能的分割点
     *    - 约束：当前子串必须是回文串
     *    - 目标：处理完整个字符串
     * 
     * 2. 回文串判断优化：
     *    - 动态规划预处理：O(n^2)时间和空间，避免重复计算
     *    - 中心扩展法：适用于统计回文子串数量
     *    - Manacher算法：线性时间复杂度的回文串查找算法
     * 
     * 复杂度分析：
     * - 时间复杂度：O(n * 2^n) - 最坏情况下需要枚举所有可能的分割方案
     * - 空间复杂度：
     *   - 递归栈：O(n)
     *   - 动态规划数组：O(n^2)
     *   - 结果存储：O(2^n * n)
     * 
     * 优化技巧：
     * 1. 预处理优化：
     *    - 使用动态规划预先计算所有子串的回文性质
     *    - 避免在回溯过程中重复判断子串是否为回文
     * 
     * 2. 剪枝策略：
     *    - 提前排除不可能的分割路径
     *    - 对于某些变种问题（如最少分割次数），可以使用贪心策略
     * 
     * 3. 记忆化搜索：
     *    - 缓存中间结果，避免重复计算
     *    - 适用于需要多次查询的场景
     * 
     * 调试技巧：
     * 1. 打印中间状态：
     *    - 记录当前分割路径和处理位置
     *    - 验证回文串判断的正确性
     * 
     * 2. 边界测试：
     *    - 空字符串
     *    - 单字符串
     *    - 全回文字符串
     *    - 无回文子串
     * 
     * 3. 性能分析：
     *    - 使用性能分析工具找出瓶颈
     *    - 针对大规模数据进行优化
     * 
     * 跨语言实现注意事项：
     * 1. 字符串处理：
     *    - 不同语言的字符串操作效率差异
     *    - Java中String是不可变的，频繁拼接会产生额外开销
     *    - Python中的字符串切片效率较高
     * 
     * 2. 递归限制：
     *    - Python默认的递归深度限制为1000
     *    - Java没有明确的递归深度限制，但过深会导致栈溢出
     *    - 对于长字符串，可以考虑迭代实现
     * 
     * 3. 数据结构选择：
     *    - 使用合适的集合类存储结果
     *    - 考虑使用StringBuilder等可变字符串类
     * 
     * 工程化考量：
     * 1. 输入验证：
     *    - 检查null、空字符串等边界情况
     *    - 验证输入参数的有效性
     * 
     * 2. 代码可读性：
     *    - 提取辅助函数，如回文判断、预处理等
     *    - 添加详细的注释和文档
     * 
     * 3. 可测试性：
     *    - 设计单元测试用例
     *    - 覆盖各种边界情况
     * 
     * 4. 可扩展性：
     *    - 设计通用的回溯框架
     *    - 支持不同的回文判断策略
     * 
     * 与其他算法的结合：
     * 1. 与回文串算法结合：
     *    - Manacher算法用于高效查找最长回文子串
     *    - 中心扩展法用于统计回文子串数量
     * 
     * 2. 与动态规划结合：
     *    - 优化回文判断
     *    - 求解最少分割次数等优化问题
     * 
     * 3. 与哈希算法结合：
     *    - 使用哈希表预处理字符串的反转
     *    - 用于解决回文对问题
     * 
     * 4. 与机器学习的联系：
     *    - 可以将回文识别作为自然语言处理的基础任务
     *    - 使用深度学习模型自动识别回文模式
     *    - 在文本生成中应用回文结构
     */
    
    /**
     * 补充题目9：LeetCode 46. 全排列
     * 题目描述：给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
     * 链接：https://leetcode.cn/problems/permutations/
     * 
     * 解题思路：
     * - 使用回溯算法，枚举每个位置可能的数字
     * - 用一个used数组标记已经使用过的数字
     * - 当路径长度等于数组长度时，将当前路径加入结果集
     * - 时间复杂度：O(n * n!)，空间复杂度：O(n)
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }
        boolean[] used = new boolean[nums.length];
        backtrackPermute(nums, used, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrackPermute(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            // 做选择
            used[i] = true;
            path.add(nums[i]);
            // 递归
            backtrackPermute(nums, used, path, result);
            // 回溯
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
    
    /**
     * 补充题目10：LeetCode 47. 全排列 II
     * 题目描述：给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
     * 链接：https://leetcode.cn/problems/permutations-ii/
     * 
     * 解题思路：
     * - 先对数组进行排序，确保相同的数字相邻
     * - 使用回溯算法，同时添加去重逻辑
     * - 当前数字与前一个数字相同且前一个数字未使用过时，跳过当前数字
     * - 时间复杂度：O(n * n!)，空间复杂度：O(n)
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }
        // 排序，确保相同的数字相邻
        Arrays.sort(nums);
        boolean[] used = new boolean[nums.length];
        backtrackPermuteUnique(nums, used, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrackPermuteUnique(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            // 去重逻辑：当前数字与前一个数字相同且前一个数字未使用过时，跳过当前数字
            if (used[i] || (i > 0 && nums[i] == nums[i - 1] && !used[i - 1])) {
                continue;
            }
            // 做选择
            used[i] = true;
            path.add(nums[i]);
            // 递归
            backtrackPermuteUnique(nums, used, path, result);
            // 回溯
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
    
    /**
     * 补充题目11：LeetCode 78. 子集
     * 题目描述：给你一个整数数组 nums ，数组中的元素 互不相同 。返回该数组所有可能的子集（幂集）。
     * 链接：https://leetcode.cn/problems/subsets/
     * 
     * 解题思路：
     * - 使用回溯算法，枚举每个位置的元素是否被选中
     * - 对于每个元素，有两种选择：包含或不包含
     * - 时间复杂度：O(n * 2^n)，空间复杂度：O(n)
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            result.add(new ArrayList<>());
            return result;
        }
        backtrackSubsets(nums, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrackSubsets(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        // 将当前路径加入结果集（每个节点都是一个子集）
        result.add(new ArrayList<>(path));
        
        for (int i = start; i < nums.length; i++) {
            // 做选择
            path.add(nums[i]);
            // 递归
            backtrackSubsets(nums, i + 1, path, result);
            // 回溯
            path.remove(path.size() - 1);
        }
    }
    
    /**
     * 分割回文串与回溯算法的深度解析
     * 
     * 核心概念与理论基础：
     * 1. 回溯算法理论：
     *    - 状态空间树模型与搜索策略
     *    - 剪枝技术的数学基础
     *    - 排列组合的组合数学原理
     *    - 决策树与状态转换
     * 
     * 2. 回文理论深化：
     *    - 回文自动机的数学模型
     *    - 回文性质的代数结构
     *    - 回文串在字符串理论中的地位
     *    - 回文识别的计算复杂性
     * 
     * 3. 组合优化视角：
     *    - 分割问题的最优子结构性质
     *    - 贪心策略的适用条件
     *    - 近似算法的性能边界
     *    - 参数化复杂度分析
     * 
     * 高级算法技术：
     * 1. 回溯算法优化技术：
     *    - 剪枝策略分类：可行性剪枝、最优性剪枝、记忆化剪枝
     *    - 启发式搜索与A*算法在回溯中的应用
     *    - 迭代加深搜索（IDS）的实现
     *    - 并行回溯算法设计
     * 
     * 2. 动态规划优化技术：
     *    - 状态压缩DP在回文分割中的应用
     *    - 区间DP的高级实现技巧
     *    - 位运算优化状态表示
     *    - 四边形不等式优化
     * 
     * 3. 字符串处理高级技术：
     *    - Manacher算法的深入解析与扩展
     *    - 后缀自动机与回文识别
     *    - 哈希算法组合（双重哈希、多项式哈希）
     *    - 字符串匹配算法与回文结合
     * 
     * 4. 计算几何方法：
     *    - 回文串的几何表示
     *    - 中心点枚举的几何意义
     *    - 对称性利用的数学基础
     * 
     * 多维度优化策略：
     * 1. 算法层面优化：
     *    - 预计算与缓存策略设计
     *    - 问题转换与等价变形
     *    - 启发式规则设计
     *    - 并行计算模型选择
     * 
     * 2. 数据结构优化：
     *    - 哈希表实现选择与优化
     *    - 平衡树与线段树的应用
     *    - 位图与位操作的高效应用
     *    - 自定义数据结构设计
     * 
     * 3. 系统层面优化：
     *    - 内存管理与缓存优化
     *    - 指令级并行优化
     *    - 编译器优化策略
     *    - 分布式计算框架应用
     * 
     * 工程化实践指南：
     * 1. 算法选型框架：
     *    - 问题特性分析方法论
     *    - 算法复杂度与实际性能权衡
     *    - 可扩展性与可维护性评估
     *    - 跨平台实现考量
     * 
     * 2. 代码质量保证：
     *    - 模块化设计模式
     *    - 接口抽象原则
     *    - 单元测试策略
     *    - 性能基准测试
     * 
     * 3. 调试与优化技术：
     *    - 可视化调试工具应用
     *    - 性能分析与瓶颈定位
     *    - 热点代码优化技巧
     *    - 内存泄漏检测
     * 
     * 跨学科应用：
     * 1. 密码学应用：
     *    - 回文结构在加密算法中的应用
     *    - 基于回文的哈希函数设计
     *    - 消息完整性验证
     *    - 安全协议设计
     * 
     * 2. 生物信息学：
     *    - DNA序列中的回文结构分析
     *    - 基因调控区域识别
     *    - 蛋白质结构预测
     *    - 分子序列比对中的回文模式
     * 
     * 3. 自然语言处理：
     *    - 文本分割与分析
     *    - 语言模型中的模式识别
     *    - 文本生成与修辞分析
     *    - 语言演化研究
     * 
     * 4. 计算机图形学：
     *    - 对称图形识别
     *    - 模式匹配与形状分析
     *    - 图像处理中的回文结构
     * 
     * 前沿研究方向：
     * 1. 理论研究：
     *    - 回文问题的计算复杂性新结果
     *    - 参数化复杂度前沿进展
     *    - 量子计算模型下的回文识别
     *    - 随机字符串中的回文统计性质
     * 
     * 2. 算法创新：
     *    - 大规模并行回溯算法
     *    - 量子启发式算法
     *    - 神经网络在回文识别中的应用
     *    - 在线算法与流式处理
     * 
     * 3. 应用拓展：
     *    - 区块链中的回文应用
     *    - 量子通信中的回文编码
     *    - 分布式系统中的一致性验证
     *    - 大数据处理中的高效回文搜索
     * 
     * 总结：
     * 分割回文串问题作为回溯算法与字符串处理的经典结合，展现了算法设计的深刻思想和广泛应用。
     * 从理论基础到工程实践，从经典算法到前沿研究，分割回文串问题涵盖了计算机科学的多个重要领域。
     * 掌握这类问题的解题思路和优化技巧，不仅有助于解决具体的算法问题，更能培养抽象思维和问题转化能力。
     * 在未来的计算机科学发展中，分割回文串问题及其相关技术将继续在各个领域发挥重要作用，特别是在大数据处理、
     * 人工智能和量子计算等新兴领域，为解决复杂问题提供创新思路。
     */
}