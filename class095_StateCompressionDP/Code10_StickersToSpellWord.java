package class080;

import java.util.*;

// 贴纸拼词 (Stickers to Spell Word)
// 我们给出了一个字符串数组 stickers，每个字符串都是一个小写字母的单词。
// 目标是拼出给定的字符串 target。我们可以按任意次数使用 stickers 中的每个贴纸。
// 如果任务不可能，则返回 -1。
// 测试链接 : https://leetcode.cn/problems/stickers-to-spell-word/
public class Code10_StickersToSpellWord {

    // 使用状态压缩动态规划解决贴纸拼词问题
    // 核心思想：用二进制位表示target字符串的匹配状态，通过状态转移找到最少贴纸数
    // 时间复杂度: O(2^m * n * L)，其中m是target长度，n是贴纸数量，L是贴纸平均长度
    // 空间复杂度: O(2^m)
    public static int minStickers(String[] stickers, String target) {
        int m = target.length();
        
        // dp[mask] 表示匹配mask代表的target子序列所需的最少贴纸数
        int[] dp = new int[1 << m];
        // 初始化：将所有状态设为-1（表示不可达）
        for (int i = 0; i < (1 << m); i++) {
            dp[i] = -1;
        }
        // 初始状态：不匹配任何字符，需要0张贴纸
        dp[0] = 0;
        
        // 状态转移：枚举所有可能的状态
        for (int mask = 0; mask < (1 << m); mask++) {
            // 如果当前状态不可达，跳过
            if (dp[mask] == -1) {
                continue;
            }
            
            // 枚举每个贴纸
            for (String sticker : stickers) {
                // 计算使用当前贴纸后的新状态
                int newMask = mask;
                // 统计贴纸中各字符的数量
                int[] cnt = new int[26];
                for (char c : sticker.toCharArray()) {
                    cnt[c - 'a']++;
                }
                
                // 使用贴纸中的字符来匹配target中未匹配的字符
                for (int i = 0; i < m; i++) {
                    // 如果第i个字符还未匹配，且贴纸中有该字符
                    if ((newMask & (1 << i)) == 0 && cnt[target.charAt(i) - 'a'] > 0) {
                        // 使用该字符匹配第i个字符
                        cnt[target.charAt(i) - 'a']--;
                        newMask |= 1 << i;
                    }
                }
                
                // 更新状态：匹配newMask代表的字符序列所需的最少贴纸数
                if (dp[newMask] == -1 || dp[newMask] > dp[mask] + 1) {
                    dp[newMask] = dp[mask] + 1;
                }
            }
        }
        
        // 返回匹配所有字符所需的最少贴纸数
        return dp[(1 << m) - 1];
    }

}