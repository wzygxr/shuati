# 贴纸拼词 (Stickers to Spell Word)
# 我们给出了一个字符串数组 stickers，每个字符串都是一个小写字母的单词。
# 目标是拼出给定的字符串 target。我们可以按任意次数使用 stickers 中的每个贴纸。
# 如果任务不可能，则返回 -1。
# 测试链接 : https://leetcode.cn/problems/stickers-to-spell-word/

class Code10_StickersToSpellWord:
    
    # 使用状态压缩动态规划解决贴纸拼词问题
    # 核心思想：用二进制位表示target字符串的匹配状态，通过状态转移找到最少贴纸数
    # 时间复杂度: O(2^m * n * L)，其中m是target长度，n是贴纸数量，L是贴纸平均长度
    # 空间复杂度: O(2^m)
    @staticmethod
    def minStickers(stickers, target):
        m = len(target)
        
        # dp[mask] 表示匹配mask代表的target子序列所需的最少贴纸数
        dp = [-1] * (1 << m)
        # 初始状态：不匹配任何字符，需要0张贴纸
        dp[0] = 0
        
        # 状态转移：枚举所有可能的状态
        for mask in range(1 << m):
            # 如果当前状态不可达，跳过
            if dp[mask] == -1:
                continue
            
            # 枚举每个贴纸
            for sticker in stickers:
                # 计算使用当前贴纸后的新状态
                new_mask = mask
                # 统计贴纸中各字符的数量
                cnt = [0] * 26
                for c in sticker:
                    cnt[ord(c) - ord('a')] += 1
                
                # 使用贴纸中的字符来匹配target中未匹配的字符
                for i in range(m):
                    # 如果第i个字符还未匹配，且贴纸中有该字符
                    if (new_mask & (1 << i)) == 0 and cnt[ord(target[i]) - ord('a')] > 0:
                        # 使用该字符匹配第i个字符
                        cnt[ord(target[i]) - ord('a')] -= 1
                        new_mask |= 1 << i
                
                # 更新状态：匹配new_mask代表的字符序列所需的最少贴纸数
                if dp[new_mask] == -1 or dp[new_mask] > dp[mask] + 1:
                    dp[new_mask] = dp[mask] + 1
        
        # 返回匹配所有字符所需的最少贴纸数
        return dp[(1 << m) - 1]

# 测试代码
if __name__ == "__main__":
    solution = Code10_StickersToSpellWord()
    
    # 测试用例1
    stickers1 = ["with", "example", "science"]
    target1 = "thehat"
    result1 = solution.minStickers(stickers1, target1)
    print(f"测试用例1: stickers={stickers1}, target='{target1}', 结果={result1}")  # 期望输出: 3
    
    # 测试用例2
    stickers2 = ["notice", "possible"]
    target2 = "basicbasic"
    result2 = solution.minStickers(stickers2, target2)
    print(f"测试用例2: stickers={stickers2}, target='{target2}', 结果={result2}")  # 期望输出: -1