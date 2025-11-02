# 得分最高的单词集合 (Maximum Score Words Formed by Letters)
# 你将会得到一份单词表 words，一个字母表 letters（可能会有重复字母），
# 以及每个字母对应的得分情况表 score。
# 请你计算利用 letters 里的字母拼写出任意数量的 words 中的单词，
# 能获得的得分最高是多少。
# 测试链接 : https://leetcode.cn/problems/maximum-score-words-formed-by-letters/

class Code13_MaximumScoreWordsFormedbyLetters:
    
    # 使用状态压缩动态规划解决得分最高的单词集合问题
    # 核心思想：用二进制位表示选择的单词集合，通过状态转移找到最高得分
    # 时间复杂度: O(2^n * L)，其中n是单词数量，L是单词平均长度
    # 空间复杂度: O(2^n)
    @staticmethod
    def maxScoreWords(words, letters, score):
        n = len(words)
        
        # 统计letters中各字母的数量
        cnt = [0] * 26
        for c in letters:
            cnt[ord(c) - ord('a')] += 1
        
        # word[i] 表示第i个单词的字母统计
        word = [[0] * 26 for _ in range(n)]
        for i in range(n):
            for c in words[i]:
                word[i][ord(c) - ord('a')] += 1
        
        # dp[mask] 表示选择mask代表的单词集合能获得的最高得分
        dp = [-1] * (1 << n)
        # 初始状态：不选择任何单词，得分为0
        dp[0] = 0
        
        result = 0
        
        # 状态转移：枚举所有可能的状态
        for mask in range(1 << n):
            # 如果当前状态不可达，跳过
            if dp[mask] == -1:
                continue
            
            # 更新最大得分
            result = max(result, dp[mask])
            
            # 统计当前选择的单词所需的字母数量
            need = [0] * 26
            for i in range(n):
                # 如果选择了第i个单词
                if (mask & (1 << i)) != 0:
                    for j in range(26):
                        need[j] += word[i][j]
            
            # 枚举下一个要选择的单词
            for i in range(n):
                # 如果还未选择第i个单词
                if (mask & (1 << i)) == 0:
                    # 检查是否有足够的字母来选择第i个单词
                    valid = True
                    for j in range(26):
                        if need[j] + word[i][j] > cnt[j]:
                            valid = False
                            break
                    
                    # 如果有足够的字母
                    if valid:
                        # 计算选择第i个单词能获得的得分
                        s = 0
                        for j in range(26):
                            s += word[i][j] * score[j]
                        
                        # 更新状态：选择mask+(1<<i)代表的单词集合能获得的最高得分
                        new_mask = mask | (1 << i)
                        dp[new_mask] = max(dp[new_mask], dp[mask] + s)
        
        return result
    
    # 更优化的解法
    # 时间复杂度: O(2^n * L)，其中n是单词数量，L是单词平均长度
    # 空间复杂度: O(2^n)
    @staticmethod
    def maxScoreWordsOptimized(words: List[str], letters: List[str], score: List[int]) -> int:
        n = len(words)
        
        # 预处理：统计letters中各字母的数量
        letter_count = [0] * 26
        for c in letters:
            letter_count[ord(c) - ord('a')] += 1
        
        # 预处理：计算每个单词的得分和所需字母数量
        word_scores = [0] * n
        word_letters = [[0] * 26 for _ in range(n)]
        
        for i in range(n):
            for c in words[i]:
                word_scores[i] += score[ord(c) - ord('a')]
                word_letters[i][ord(c) - ord('a')] += 1
        
        # dp[mask] 表示选择mask代表的单词集合能获得的最大得分
        dp = [0] * (1 << n)
        
        # 枚举所有可能的单词组合
        for mask in range(1 << n):
            # 统计当前组合所需的字母数量
            used_letters = [0] * 26
            
            for i in range(n):
                # 如果选择了第i个单词
                if (mask & (1 << i)) != 0:
                    # 检查字母是否足够
                    valid = True
                    for j in range(26):
                        used_letters[j] += word_letters[i][j]
                        if used_letters[j] > letter_count[j]:
                            # 字母不够，当前组合无效
                            dp[mask] = -1
                            valid = False
                            break
                    
                    if not valid:
                        break
            
            # 如果字母足够，则计算得分
            if dp[mask] != -1:
                total_score = 0
                for i in range(n):
                    if (mask & (1 << i)) != 0:
                        total_score += word_scores[i]
                dp[mask] = total_score
            
            # 通过子集更新当前状态的最大得分
            for i in range(n):
                if (mask & (1 << i)) != 0:
                    prev_mask = mask ^ (1 << i)
                    if dp[prev_mask] != -1:
                        dp[mask] = max(dp[mask], dp[prev_mask])
        
        # 找到所有有效组合中的最大得分
        max_score = 0
        for i in range(1 << n):
            max_score = max(max_score, dp[i])
        
        return max_score
    
    # 测试方法
    @staticmethod
    def test():
        # 测试用例1
        words1 = ["dog", "cat", "dad", "good"]
        letters1 = ['a','a','c','d','d','d','g','o','o']
        score1 = [1, 0, 9, 5, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        result1 = Code13_MaximumScoreWordsFormedbyLetters.maxScoreWordsOptimized(words1, letters1, score1)
        print(f"单词: {words1}, 最高得分: {result1}")  # 期望输出: 23
        
        # 测试用例2
        words2 = ["xxxz", "ax", "bx", "cx"]
        letters2 = ['z','a','b','c','x','x','x']
        score2 = [4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,10]
        result2 = Code13_MaximumScoreWordsFormedbyLetters.maxScoreWordsOptimized(words2, letters2, score2)
        print(f"单词: {words2}, 最高得分: {result2}")  # 期望输出: 27


if __name__ == "__main__":
    Code13_MaximumScoreWordsFormedbyLetters.test()