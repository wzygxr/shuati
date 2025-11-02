package class080;

// 得分最高的单词集合 (Maximum Score Words Formed by Letters)
// 你将会得到一份单词表 words，一个字母表 letters（可能会有重复字母），
// 以及每个字母对应的得分情况表 score。
// 请你计算利用 letters 里的字母拼写出任意数量的 words 中的单词，
// 能获得的得分最高是多少。
// 测试链接 : https://leetcode.cn/problems/maximum-score-words-formed-by-letters/
public class Code13_MaximumScoreWordsFormedbyLetters {

    // 使用状态压缩动态规划解决得分最高的单词集合问题
    // 核心思想：用二进制位表示选择的单词集合，通过状态转移找到最高得分
    // 时间复杂度: O(2^n * L)，其中n是单词数量，L是单词平均长度
    // 空间复杂度: O(2^n)
    public static int maxScoreWords(String[] words, char[] letters, int[] score) {
        int n = words.length;
        
        // 统计letters中各字母的数量
        int[] cnt = new int[26];
        for (char c : letters) {
            cnt[c - 'a']++;
        }
        
        // word[i] 表示第i个单词的字母统计
        int[][] word = new int[n][26];
        for (int i = 0; i < n; i++) {
            for (char c : words[i].toCharArray()) {
                word[i][c - 'a']++;
            }
        }
        
        // dp[mask] 表示选择mask代表的单词集合能获得的最高得分
        int[] dp = new int[1 << n];
        // 初始化：将所有状态设为-1（表示不可达）
        for (int i = 0; i < (1 << n); i++) {
            dp[i] = -1;
        }
        // 初始状态：不选择任何单词，得分为0
        dp[0] = 0;
        
        int result = 0;
        
        // 状态转移：枚举所有可能的状态
        for (int mask = 0; mask < (1 << n); mask++) {
            // 如果当前状态不可达，跳过
            if (dp[mask] == -1) {
                continue;
            }
            
            // 更新最大得分
            result = Math.max(result, dp[mask]);
            
            // 统计当前选择的单词所需的字母数量
            int[] need = new int[26];
            for (int i = 0; i < n; i++) {
                // 如果选择了第i个单词
                if ((mask & (1 << i)) != 0) {
                    for (int j = 0; j < 26; j++) {
                        need[j] += word[i][j];
                    }
                }
            }
            
            // 枚举下一个要选择的单词
            for (int i = 0; i < n; i++) {
                // 如果还未选择第i个单词
                if ((mask & (1 << i)) == 0) {
                    // 检查是否有足够的字母来选择第i个单词
                    boolean valid = true;
                    for (int j = 0; j < 26; j++) {
                        if (need[j] + word[i][j] > cnt[j]) {
                            valid = false;
                            break;
                        }
                    }
                    
                    // 如果有足够的字母
                    if (valid) {
                        // 计算选择第i个单词能获得的得分
                        int s = 0;
                        for (int j = 0; j < 26; j++) {
                            s += word[i][j] * score[j];
                        }
                        
                        // 更新状态：选择mask+(1<<i)代表的单词集合能获得的最高得分
                        int newMask = mask | (1 << i);
                        dp[newMask] = Math.max(dp[newMask], dp[mask] + s);
                    }
                }
            }
        }
        
        return result;
    }

    // 更优化的解法
    // 时间复杂度: O(2^n * L)，其中n是单词数量，L是单词平均长度
    // 空间复杂度: O(2^n)
    public static int maxScoreWordsOptimized(String[] words, char[] letters, int[] score) {
        int n = words.length;
        
        // 预处理：统计letters中各字母的数量
        int[] letterCount = new int[26];
        for (char c : letters) {
            letterCount[c - 'a']++;
        }
        
        // 预处理：计算每个单词的得分和所需字母数量
        int[] wordScores = new int[n];
        int[][] wordLetters = new int[n][26];
        
        for (int i = 0; i < n; i++) {
            for (char c : words[i].toCharArray()) {
                wordScores[i] += score[c - 'a'];
                wordLetters[i][c - 'a']++;
            }
        }
        
        // dp[mask] 表示选择mask代表的单词集合能获得的最大得分
        int[] dp = new int[1 << n];
        
        // 枚举所有可能的单词组合
        for (int mask = 0; mask < (1 << n); mask++) {
            // 统计当前组合所需的字母数量
            int[] usedLetters = new int[26];
            
            for (int i = 0; i < n; i++) {
                // 如果选择了第i个单词
                if ((mask & (1 << i)) != 0) {
                    // 检查字母是否足够
                    for (int j = 0; j < 26; j++) {
                        usedLetters[j] += wordLetters[i][j];
                        if (usedLetters[j] > letterCount[j]) {
                            // 字母不够，当前组合无效
                            dp[mask] = -1;
                            break;
                        }
                    }
                    
                    if (dp[mask] == -1) break;
                }
            }
            
            // 如果字母足够，则计算得分
            if (dp[mask] != -1) {
                int totalScore = 0;
                for (int i = 0; i < n; i++) {
                    if ((mask & (1 << i)) != 0) {
                        totalScore += wordScores[i];
                    }
                }
                dp[mask] = totalScore;
            }
            
            // 通过子集更新当前状态的最大得分
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    int prevMask = mask ^ (1 << i);
                    if (dp[prevMask] != -1) {
                        dp[mask] = Math.max(dp[mask], dp[prevMask]);
                    }
                }
            }
        }
        
        // 找到所有有效组合中的最大得分
        int maxScore = 0;
        for (int i = 0; i < (1 << n); i++) {
            maxScore = Math.max(maxScore, dp[i]);
        }
        
        return maxScore;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String[] words1 = {"dog", "cat", "dad", "good"};
        char[] letters1 = {'a','a','c','d','d','d','g','o','o'};
        int[] score1 = {1, 0, 9, 5, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int result1 = maxScoreWordsOptimized(words1, letters1, score1);
        System.out.println("单词: " + java.util.Arrays.toString(words1) + ", 最高得分: " + result1); // 期望输出: 23
        
        // 测试用例2
        String[] words2 = {"xxxz", "ax", "bx", "cx"};
        char[] letters2 = {'z','a','b','c','x','x','x'};
        int[] score2 = {4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,10};
        int result2 = maxScoreWordsOptimized(words2, letters2, score2);
        System.out.println("单词: " + java.util.Arrays.toString(words2) + ", 最高得分: " + result2); // 期望输出: 27
    }
}