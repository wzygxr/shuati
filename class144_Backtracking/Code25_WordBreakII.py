from typing import List

class Solution:
    """
    LeetCode 140. 单词拆分 II
    
    题目描述：
    给定一个字符串 s 和一个字符串字典 wordDict，在字符串 s 中增加空格来构建一个句子，
    使得句子中所有的单词都在词典中。返回所有这些可能的句子。
    
    示例：
    输入: s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"]
    输出: ["cats and dog","cat sand dog"]
    
    输入: s = "pineapplepenapple", wordDict = ["apple","pen","applepen","pine","pineapple"]
    输出: ["pine apple pen apple","pineapple pen apple","pine applepen apple"]
    
    输入: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
    输出: []
    
    提示：
    1 <= s.length <= 20
    1 <= wordDict.length <= 1000
    1 <= wordDict[i].length <= 10
    s 和 wordDict[i] 仅有小写英文字母组成
    wordDict 中的所有字符串互不相同
    
    链接：https://leetcode.cn/problems/word-break-ii/
    
    算法思路：
    1. 使用回溯算法分割字符串
    2. 使用记忆化搜索优化，避免重复计算
    3. 对于每个位置，尝试所有可能的单词分割
    4. 当分割到字符串末尾时，将结果加入结果集
    
    时间复杂度：O(2^n * n)，最坏情况下需要尝试所有分割方式
    空间复杂度：O(n^2)，记忆化存储的空间
    """
    
    def wordBreak(self, s: str, wordDict: List[str]) -> List[str]:
        word_set = set(wordDict)
        memo = {}
        return self.backtrack(s, 0, word_set, memo)
    
    def backtrack(self, s: str, start: int, word_set: set, memo: dict) -> List[str]:
        # 如果已经计算过这个位置的结果，直接返回
        if start in memo:
            return memo[start]
        
        # 如果已经到达字符串末尾，返回空列表（表示一个有效的分割结束）
        if start == len(s):
            return [""]
        
        result = []
        
        # 尝试所有可能的分割点
        for end in range(start + 1, len(s) + 1):
            word = s[start:end]
            
            # 如果当前单词在字典中
            if word in word_set:
                # 递归处理剩余部分
                sub_results = self.backtrack(s, end, word_set, memo)
                
                # 将当前单词与子结果组合
                for sub_result in sub_results:
                    if sub_result:
                        result.append(word + " " + sub_result)
                    else:
                        result.append(word)
        
        # 记忆化存储结果
        memo[start] = result
        return result

def test_word_break_ii():
    solution = Solution()
    
    # 测试用例1
    s1 = "catsanddog"
    wordDict1 = ["cat", "cats", "and", "sand", "dog"]
    result1 = solution.wordBreak(s1, wordDict1)
    print(f'输入: s = "{s1}", wordDict = {wordDict1}')
    print("输出:", result1)
    
    # 测试用例2
    s2 = "pineapplepenapple"
    wordDict2 = ["apple", "pen", "applepen", "pine", "pineapple"]
    result2 = solution.wordBreak(s2, wordDict2)
    print(f'\n输入: s = "{s2}", wordDict = {wordDict2}')
    print("输出:", result2)
    
    # 测试用例3
    s3 = "catsandog"
    wordDict3 = ["cats", "dog", "sand", "and", "cat"]
    result3 = solution.wordBreak(s3, wordDict3)
    print(f'\n输入: s = "{s3}", wordDict = {wordDict3}')
    print("输出:", result3)

if __name__ == "__main__":
    test_word_break_ii()