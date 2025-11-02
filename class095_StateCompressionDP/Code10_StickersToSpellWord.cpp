// 贴纸拼词
// 我们有 n 种不同的贴纸。每个贴纸上都有一个小写字母序列。
// 你想要拼写出给定的字符串 target ，方法是从贴纸集合中裁剪单个字母。
// 如果你愿意，你可以多次使用每个贴纸，每个贴纸的数量是无限的。
// 返回你需要拼出 target 的最小贴纸数量。如果任务不可能，则返回 -1。
// 注意：在所有的测试用例中，所有的字符串都是小写字母。
// 测试链接 : https://leetcode.cn/problems/stickers-to-spell-word/

class Solution {
public:
    // 主函数
    int minStickers(char** stickers, int stickersSize, int* stickersColSize, char* target) {
        int n = 0;
        while (target[n] != '\0') {
            n++;
        }
        
        // dp[mask] 表示拼出mask对应的target子串所需的最小贴纸数
        // mask的第i位为1表示target的第i个字符已被拼出
        int dp[1024];  // 假设target最大长度为10
        for (int i = 0; i < (1 << n); i++) {
            dp[i] = 2147483647;  // INT_MAX
        }
        dp[0] = 0;  // 空字符串需要0张贴纸
        
        // 预处理每个贴纸的字符频率
        int stickerFreq[50][26];  // 假设最多50张贴纸
        for (int i = 0; i < stickersSize; i++) {
            // 初始化字符频率为0
            for (int k = 0; k < 26; k++) {
                stickerFreq[i][k] = 0;
            }
            
            // 统计字符频率
            int j = 0;
            while (stickers[i][j] != '\0') {
                stickerFreq[i][stickers[i][j] - 'a']++;
                j++;
            }
        }
        
        // 遍历所有状态
        for (int mask = 0; mask < (1 << n); mask++) {
            // 如果当前状态不可达，跳过
            if (dp[mask] == 2147483647) {
                continue;
            }
            
            // 尝试每一张贴纸
            for (int i = 0; i < stickersSize; i++) {
                int newMask = mask;
                int tempFreq[26];  // 复制贴纸的字符频率
                for (int k = 0; k < 26; k++) {
                    tempFreq[k] = stickerFreq[i][k];
                }
                
                // 尝试用这张贴纸覆盖尽可能多的未覆盖字符
                for (int j = 0; j < n; j++) {
                    // 如果字符j未被覆盖且贴纸中还有这个字符
                    if (!(newMask & (1 << j)) && tempFreq[target[j] - 'a'] > 0) {
                        newMask |= (1 << j);  // 标记为已覆盖
                        tempFreq[target[j] - 'a']--;  // 减少贴纸中的字符数量
                    }
                }
                
                // 更新新状态的最小贴纸数
                if (dp[newMask] > dp[mask] + 1) {
                    dp[newMask] = dp[mask] + 1;
                }
            }
        }
        
        // 如果最终状态不可达，返回-1，否则返回所需的最小贴纸数
        return dp[(1 << n) - 1] == 2147483647 ? -1 : dp[(1 << n) - 1];
    }
};

/*
复杂度分析：
1. 动态规划版本：
   - 时间复杂度：O(2^m * n * L)
     其中m是target的长度，n是贴纸的数量，L是贴纸的平均长度
     状态数为2^m个，每个状态需要遍历n张贴纸，每张贴纸最多处理m个字符
   - 空间复杂度：O(2^m + n * 26)
     dp数组需要存储2^m个状态，贴纸频率数组需要存储n * 26个整数

2. 记忆化搜索版本：
   - 时间复杂度：O(2^m * n * m)
     同样需要处理2^m个状态，每个状态需要尝试n张贴纸，每张贴纸最多处理m个字符
   - 空间复杂度：O(2^m + n * 26)
     memo数组存储2^m个状态，递归栈的深度为O(m)

算法说明：
1. 预处理：统计每张贴纸的字符频率，方便快速查询
2. 状态表示：使用二进制掩码mask表示已拼出的字符
3. 状态转移：对于每个状态，尝试使用每一张贴纸，更新可达的新状态
4. 优化点：
   - 剪枝：跳过无法到达的状态
   - 预处理：避免重复计算贴纸的字符频率
   - 贪心策略：优先覆盖最多的未覆盖字符

这是本题的最优解，因为我们需要考虑所有可能的贴纸组合，而状态压缩DP能够高效地处理这种组合优化问题。
*/